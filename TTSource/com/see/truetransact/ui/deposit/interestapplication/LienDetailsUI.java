/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LienDetailsUI.java
 *
 * Created on April 13, 2011, 10:10 PM
 */

package com.see.truetransact.ui.deposit.interestapplication;

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

/**
 *
 * @author  Suresh
 */
public class LienDetailsUI extends com.see.truetransact.uicomponent.CDialog implements Observer {
    
    
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
    private String transType = "";
    private double penalAmount = 0.0;
//    public LienDetailsUI() {
     public LienDetailsUI() {
        initComponents();
        initForm();
        
    }
    /** Account Number Constructor */
//    public LienDetailsUI(HashMap termLoanDataMap, String totalAmount, String transType, boolean showDueTable, boolean penalDepositFlag) {
        public LienDetailsUI(ArrayList alist) {
            currDt = ClientUtil.getCurrentDate();
            initComponents();
            lblLienDetails.setText("<HTML><b><font color=Blue>"+"Lien Mark Details"+
            "</font></b></html>");
            if(alist!=null && alist.size()>0){
                for(int i=0;i<alist.size();i++){
                    HashMap hmap=(HashMap)alist.get(i);
                    String LOS= CommonUtil.convertObjToStr(hmap.get("LOANS_OTHER_SOCIETY"));
                    String ACNO="";
                    String LTYPE="";
                    String CNAME="";
                    String LAMOUNT="";
                    String LDATE="";
                    String LREMARKS="";
                    if(LOS.equals("Y")){
                        tblLienDetails.setValueAt(CommonUtil.convertObjToStr(hmap.get("LOS_LIEN_AC_NO")),i,0);
                        tblLienDetails.setValueAt(CommonUtil.convertObjToStr(hmap.get("LOS_LIEN_DATE")),i,1);
                        tblLienDetails.setValueAt(CommonUtil.convertObjToStr(hmap.get("LOS_LIEN_AMOUNT")),i,2);
                        tblLienDetails.setValueAt(CommonUtil.convertObjToStr(hmap.get("LOS_LIEN_REMARKS")),i,3);
                        tblLienDetails.setValueAt(CommonUtil.convertObjToStr(hmap.get("LIEN_NO")),i,4);
                        
                    }else{
                        
                        tblLienDetails.setValueAt(CommonUtil.convertObjToStr(hmap.get("LIEN_AC_NO")),i,0);
                        tblLienDetails.setValueAt(CommonUtil.convertObjToStr(hmap.get("LIEN_DT")),i,1);
                        tblLienDetails.setValueAt(CommonUtil.convertObjToStr(hmap.get("LIEN_AMOUNT")),i,2);
                        tblLienDetails.setValueAt(CommonUtil.convertObjToStr(hmap.get("REMARKS")),i,3);
                        tblLienDetails.setValueAt(CommonUtil.convertObjToStr(hmap.get("LIEN_NO")),i,4);
                        
                    }
                }
            }
            
            
            this.transType = transType;
            
            System.out.println("%#$^%#$^%termLoanData:"+termLoanData);
            
            branchID = TrueTransactMain.BRANCH_ID;
            setupScreen();
            
        }
        
        /** Method which is used to initialize the form TokenConfig */
        private void initForm(){
            setMaxLengths();
            setFieldNames();
            internationalize();
            currDt = ClientUtil.getCurrentDate();
        }
        
        private void setupScreen() {
            setModal(true);
            setTitle("LienDetails"+"["+branchID+"]");
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
        
        private void addToTable(ArrayList termLoanData,String totalAmount){
            System.out.println("$#%#$%#$%#$termLoanData:"+termLoanData);
            String str1 = totalAmount;
            str1 = str1.replaceAll(",","");
            double totalAmt = CommonUtil.convertObjToDouble(str1).doubleValue();
            ArrayList dueList = new ArrayList();
            ArrayList recievedList = new ArrayList();
            ArrayList recievableList = new ArrayList();
            boolean principalDueExists = false;
            for(int k=0;k<termLoanData.size();k++){
                ArrayList balList=((ArrayList)termLoanData.get(k));
                if(balList != null && balList.contains("Total Balance")){
                    String str = CommonUtil.convertObjToStr(balList.get(1));
                    boolean isDebitBalance = str.indexOf("-")!=-1 ? true : false;
                    str = str.replaceAll("-","");
                    str = str.replaceAll(",","");
                    totalBalance = CommonUtil.convertObjToDouble(str).doubleValue();
                    if (isDebitBalance) {
                        totalBalance = -1 * totalBalance;
                    }
                    System.out.println("%#$%#$%#$%totalBalance :"+totalBalance);
                }
                if(!balList.contains("Limit Amount") && !balList.contains("Available Balance") && !balList.contains("Principal Amount")
                && !balList.contains("Total Balance") && !balList.contains("Last Int Calc Dt") && !balList.contains("Interest Rate") && !balList.contains("Total Recievable")
                && !balList.contains("Unclear Balance") && !balList.contains("Clear Balance") && !balList.contains("Drawing Power Amount") && showDueTableFinal){
                    String dueAmount = CommonUtil.convertObjToStr(balList.get(1));
                    dueAmount = dueAmount.replaceAll(",", "");
                    //                balList.remove(1);
                    balList.set(1, dueAmount);
                    System.out.println("#$%#$%$#%balList"+balList);
                    dueList.add(balList);
                }
                if(balList.contains("Pending Penalty for Delayed Months") && hasPenal){
                    String penalAmt = CommonUtil.convertObjToStr(balList.get(1));
                    penalAmt = penalAmt.replaceAll(",", "");
                    penalAmount = CommonUtil.convertObjToDouble(penalAmt).doubleValue();
                    if(penalAmount > 0){
                        hasPenal = true;
                        
                        
                        
                    }else{
                        hasPenal = false;
                        
                        
                        
                    }
                }
            }
            termLoanData = new ArrayList();
            termLoanData = dueList;
            System.out.println("@#%#@$%#$%dueList:"+dueList);
            if (dueList != null && dueList.size()==0) {
                displayBalance = totalAmt;
                if(transType.equals("CREDIT")){
                    recieptAmount = displayBalance;
                }else{
                    paymentAmount = displayBalance;
                }
            }else{
                for(int j=dueList.size()-1; j>=0;j--){
                    recievedList = new ArrayList();
                    recievedList=((ArrayList)dueList.get(j));
                    double dueAmt= 0.0;
                    String key = CommonUtil.convertObjToStr(recievedList.get(0));
                    String str= CommonUtil.convertObjToStr(recievedList.get(1));
                    str = str.replaceAll(",","");
                    dueAmt=CommonUtil.convertObjToDouble(str).doubleValue();
                    //            dueAmt= CommonUtil.convertObjToDouble(recievedList.get(1)).doubleValue();
                    if(dueAmt > 0 && totalAmt>0 ){
                        if(totalAmt >= dueAmt){
                            totalAmt = totalAmt - dueAmt;
                            if(recievedList.size() > 2 && recievedList.get(2)!= null){
                                recievedList.set(2,String.valueOf(dueAmt));
                            }else{
                                recievedList.add(String.valueOf(dueAmt));
                            }
                            if(recievedList.size() > 3 && recievedList.get(3)!= null){
                                recievedList.set(3,String.valueOf(0.0));
                            }else{
                                recievedList.add(String.valueOf(0.0));
                            }
                        }else{
                            if(recievedList.size() > 2 && recievedList.get(2)!= null){
                                recievedList.set(2,String.valueOf(totalAmt));
                            }else{
                                recievedList.add(String.valueOf(totalAmt));
                            }
                            
                            double recievableAmt = dueAmt - totalAmt;
                            totalAmt = totalAmt - dueAmt;
                            if(recievedList.size() > 3 && recievedList.get(3)!= null){
                                recievedList.set(3,String.valueOf(recievableAmt));
                            }else{
                                recievedList.add(String.valueOf(recievableAmt));
                            }
                            
                            //                    totalAmt = 0.0;
                        }
                    }else if(dueAmt > 0  && totalAmt <= 0 ){
                        if(recievedList.size() > 2 && recievedList.get(2)!= null){
                            recievedList.set(2,String.valueOf(0.0));
                        }else{
                            recievedList.add(String.valueOf(0.0));
                        }
                        if(recievedList.size() > 3 && recievedList.get(3)!= null){
                            recievedList.set(3,String.valueOf(dueAmt));
                        }else{
                            recievedList.add(String.valueOf(dueAmt));
                        }
                    }else if(dueAmt <=0 ){
                        if(recievedList.size() > 2 && recievedList.get(2)!= null){
                            recievedList.set(2,String.valueOf(0.0));
                        }else{
                            recievedList.add(String.valueOf(0.0));
                        }
                        
                        if(recievedList.size() > 3 && recievedList.get(3)!= null){
                            recievedList.set(3,String.valueOf(0.0));
                        }else{
                            recievedList.add(String.valueOf(0.0));
                        }
                        
                    }
                    if (key.equals("Principal Due")) {
                        if (totalAmt<0) {
                            totalAmt = 0;
                        }
                        displayBalance = CommonUtil.convertObjToDouble(recievedList.get(2)).doubleValue()+totalAmt;
                        recieptAmount = displayBalance;
                        principalDueExists = true;
                    }
                    recievableList.add(recievedList);
                }
                if (!principalDueExists && totalAmt>0) {
                    recieptAmount = totalAmt;
                }
            }
            System.out.println("#$%#$%#$%recievableList:"+recievableList);
            HashMap whereMap = new HashMap();
            dueList = null;
            recievedList = null;
            try {
                
                recievableList = null;
            } catch( Exception e ) {
                System.err.println( "Exception " + e.toString() + "Caught" );
                e.printStackTrace();
            }
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
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panMemberShipFacility = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        srpLienDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblLienDetails = new com.see.truetransact.uicomponent.CTable();
        lblLienDetails = new com.see.truetransact.uicomponent.CLabel();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        panMemberShipFacility.setLayout(new java.awt.GridBagLayout());

        panMemberShipFacility.setMaximumSize(new java.awt.Dimension(400, 200));
        panMemberShipFacility.setMinimumSize(new java.awt.Dimension(400, 200));
        panMemberShipFacility.setPreferredSize(new java.awt.Dimension(400, 200));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif")));
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        panMemberShipFacility.add(btnClose, gridBagConstraints);

        srpLienDetails.setMinimumSize(new java.awt.Dimension(450, 100));
        srpLienDetails.setPreferredSize(new java.awt.Dimension(450, 100));
        tblLienDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "LienAcNo", "LienDate", "LienAmount", "LienRemarks", "LienNo"
            }
        ));
        tblLienDetails.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblLienDetails.setDragEnabled(true);
        tblLienDetails.setInheritsPopupMenu(true);
        tblLienDetails.setMaximumSize(new java.awt.Dimension(450, 100));
        tblLienDetails.setMinimumSize(new java.awt.Dimension(450, 100));
        tblLienDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(100, 500));
        tblLienDetails.setPreferredSize(new java.awt.Dimension(450, 100));
        srpLienDetails.setViewportView(tblLienDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        panMemberShipFacility.add(srpLienDetails, gridBagConstraints);

        lblLienDetails.setText("Lien Mark Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        panMemberShipFacility.add(lblLienDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panMemberShipFacility, gridBagConstraints);
        panMemberShipFacility.getAccessibleContext().setAccessibleName("MembershipFacifility");

        pack();
    }//GEN-END:initComponents
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
       
        ClientUtil.clearAll(this);
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    
                                    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
                                                                                                                                                                                }//GEN-LAST:event_formWindowClosed
                                    
                                    
                                                            private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
                                                                                                                                                                                                                                                                                                                                                            }//GEN-LAST:event_formWindowClosing
                                                            
                                                            /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
                                                            }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //        new CheckCustomerIdUI().show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CLabel lblLienDetails;
    private com.see.truetransact.uicomponent.CPanel panMemberShipFacility;
    private com.see.truetransact.uicomponent.CScrollPane srpLienDetails;
    private com.see.truetransact.uicomponent.CTable tblLienDetails;
    // End of variables declaration//GEN-END:variables
    
}
