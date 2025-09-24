/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SITransDetailsUI.java
 *
 * Created on April 13, 2011, 10:10 PM
 */

package com.see.truetransact.ui.common.customer;

import java.util.Observer;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Toolkit;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.DateUtil;
/**
 *
 * @author  Suresh
 */
public class SITransDetailsUI extends com.see.truetransact.uicomponent.CDialog implements Observer {
    
    
//    final CheckCustomerIdRB resourceBundle = new CheckCustomerIdRB();
    private CTable _tblData;
    private HashMap dataHash;
    private ArrayList data;
    private int dataSize;
    private ArrayList _heading;
    private boolean _isAvailable = true;
    Date currDt = null;
    public String branchID; 
    public String prodType;
    public String acNum =null;
    public String prodId ="";
    public SITransDetailsUI() {
        initComponents();
        initForm();
    }
    /** Account Number Constructor */
    public SITransDetailsUI(String actNum,String productType,String productId) {
        acNum=actNum;
        prodType=productType;
        prodId=productId;
        initComponents();
        setMaxLengths();          
        addToTableUsingActNo();              
        branchID = TrueTransactMain.BRANCH_ID;
        setupScreen();
        txtCustId.setEnabled(false);
//        lblMemName.setEnabled(false);        
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
        setTitle("SI Transaction Details "+"["+branchID+"]");
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("@$#@$#@# screenSize : "+screenSize); 
        setSize(650, 450);
        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
       
    
     private void addToTableUsingActNo(){
        HashMap map = new HashMap();
        HashMap custMap = new HashMap();        
        map.put("ACT_NUM", acNum);
        List custIdList = ClientUtil.executeQuery("getCustIdfromMembershipLiability",map);
        System.out.println("#$@#@$@#@# custIdList: "+custIdList);
        if(custIdList!= null && custIdList.size()>0){
            custMap = (HashMap) custIdList.get(0);
             txtCustId.setText(acNum);
            lblMemName.setText(CommonUtil.convertObjToStr(custMap.get("CUST_NAME")));                      
        }
        LinkedHashMap where = new LinkedHashMap();
        LinkedHashMap viewMap = new LinkedHashMap();
        where.put("ACCT_NO",acNum);  
        where.put("END_DT",ClientUtil.getCurrentDate());          

        viewMap.put(CommonConstants.MAP_NAME, "getStandingInstructionDetails");
        viewMap.put(CommonConstants.MAP_WHERE, where);        
        try {
            populateData(viewMap);
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
        viewMap = null;
        where = null;
    }
     
    public ArrayList populateData(HashMap whereMap) {
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID))
            whereMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
        dataHash = ClientUtil.executeTableQuery(whereMap);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        System.out.println("### Data : "+data);
        populateTable();
        whereMap = null;
        return _heading;
    }
    
    
    public void populateTable( ) {
        boolean dataExist;
        if (_heading != null){
             final ArrayList chequeTabTitle = new ArrayList();                      
            chequeTabTitle.add("SI Id"); 
            chequeTabTitle.add("SI Date");
            chequeTabTitle.add("Account No.");
            chequeTabTitle.add("Name");
            chequeTabTitle.add("Credit Amt");
            chequeTabTitle.add("Prod Desc");
            chequeTabTitle.add("SI_Start_Dt");
            chequeTabTitle.add("SI_End_Dt");

            TableModel model = new TableModel(new ArrayList(),chequeTabTitle );
                        
            for (int i=0, j= data.size();i<j;i++){  
                ArrayList  chequeTabRow = new ArrayList();                
                final ArrayList resultMap = (ArrayList)data.get(i);
                                
                chequeTabRow.add(resultMap.get(6));
                chequeTabRow.add(CommonUtil.convertObjToStr(resultMap.get(0)));
                chequeTabRow.add(resultMap.get(5));
                chequeTabRow.add(resultMap.get(2));
                chequeTabRow.add(resultMap.get(3));
                chequeTabRow.add(prodId);
                chequeTabRow.add(CommonUtil.convertObjToStr(resultMap.get(1)));
                chequeTabRow.add(CommonUtil.convertObjToStr(resultMap.get(4)));                                                                                                                                          
                                                
                model.insertRow(i, (ArrayList)chequeTabRow);                
            }
            model.fireTableDataChanged();
            tblTransTable.setModel(model);
            tblTransTable.revalidate();
        }
        /*else{           
            ClientUtil.noDataAlert();
        }   */     
    }   
    
    /*public void populateTable() {
        boolean dataExist;
        if (_heading != null){     
            TableModel model = new TableModel(new ArrayList(), _heading);
            setProperTransDate();
            for (int i=0; i<data.size(); i++) {
                model.insertRow(i, (ArrayList)data.get(i));
            }
            model.fireTableDataChanged();
            tblTransTable.setModel(model);
            tblTransTable.revalidate();
        }else{           
            ClientUtil.noDataAlert();
        }
    } */  
    
    
//    private void setProperTransDate() {
//        ArrayList arrList;
//        for (int i = 0; i < data.size(); i++) {
//            arrList = (ArrayList) data.get(i);
//            String transDt = CommonUtil.convertObjToStr(arrList.get(0));
//            if (transDt.length() > 0) {
//                Date transDtWithoutTimeStamp = DateUtil.getDateMMDDYYYY(transDt);
//                transDtWithoutTimeStamp = new Date(transDtWithoutTimeStamp.getYear(),
//                        transDtWithoutTimeStamp.getMonth(),
//                        transDtWithoutTimeStamp.getDate());
//                arrList.set(0, DateUtil.getStringDate(transDtWithoutTimeStamp));
//            }
//            data.set(i, arrList);
//        }
//        arrList = null;
//    }
    
    private void setRightAlignment(int col) {
        javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
        r.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tblTransTable.getColumnModel().getColumn(col).setCellRenderer(r);
        tblTransTable.getColumnModel().getColumn(col).sizeWidthToFit();
    }
        
    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths(){
        
    }
    
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        panTransDetails.setName("panMemberShipFacility");
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

        panTransDetails = new com.see.truetransact.uicomponent.CPanel();
        panMemDetails = new com.see.truetransact.uicomponent.CPanel();
        lblCustId = new com.see.truetransact.uicomponent.CLabel();
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        txtCustId = new com.see.truetransact.uicomponent.CTextField();
        lblMemName = new com.see.truetransact.uicomponent.CLabel();
        panReportTrans = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panTransDetailTable = new com.see.truetransact.uicomponent.CPanel();
        srpMemberTransCTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblTransTable = new com.see.truetransact.uicomponent.CTable();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        panTransDetails.setMaximumSize(new java.awt.Dimension(825, 575));
        panTransDetails.setMinimumSize(new java.awt.Dimension(600, 500));
        panTransDetails.setPreferredSize(new java.awt.Dimension(600, 500));
        panTransDetails.setLayout(new java.awt.GridBagLayout());

        panMemDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Details"));
        panMemDetails.setMaximumSize(new java.awt.Dimension(265, 75));
        panMemDetails.setMinimumSize(new java.awt.Dimension(400, 90));
        panMemDetails.setPreferredSize(new java.awt.Dimension(300, 90));
        panMemDetails.setLayout(new java.awt.GridBagLayout());

        lblCustId.setText("Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        panMemDetails.add(lblCustId, gridBagConstraints);

        lblCustName.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 4);
        panMemDetails.add(lblCustName, gridBagConstraints);

        txtCustId.setBackground(new java.awt.Color(212, 208, 200));
        txtCustId.setForeground(new java.awt.Color(0, 51, 204));
        txtCustId.setMaxLength(16);
        txtCustId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        panMemDetails.add(txtCustId, gridBagConstraints);

        lblMemName.setForeground(new java.awt.Color(0, 51, 204));
        lblMemName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblMemName.setMaximumSize(new java.awt.Dimension(1000, 21));
        lblMemName.setMinimumSize(new java.awt.Dimension(270, 21));
        lblMemName.setPreferredSize(new java.awt.Dimension(270, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemDetails.add(lblMemName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        panTransDetails.add(panMemDetails, gridBagConstraints);

        panReportTrans.setMaximumSize(new java.awt.Dimension(823, 40));
        panReportTrans.setMinimumSize(new java.awt.Dimension(500, 40));
        panReportTrans.setPreferredSize(new java.awt.Dimension(823, 40));
        panReportTrans.setLayout(new java.awt.GridBagLayout());

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 21, 0, 0);
        panReportTrans.add(btnClose, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 42, 0, 42);
        panTransDetails.add(panReportTrans, gridBagConstraints);

        panTransDetailTable.setBorder(javax.swing.BorderFactory.createTitledBorder("SI Transactions Details"));
        panTransDetailTable.setMinimumSize(new java.awt.Dimension(500, 250));
        panTransDetailTable.setName("panTransInfo"); // NOI18N
        panTransDetailTable.setPreferredSize(new java.awt.Dimension(500, 250));
        panTransDetailTable.setLayout(new java.awt.GridBagLayout());

        srpMemberTransCTable.setMinimumSize(new java.awt.Dimension(410, 220));
        srpMemberTransCTable.setPreferredSize(new java.awt.Dimension(600, 220));

        tblTransTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "SI Id", "SI Date", "Account No", "Name", "Credit Amt", "Prod Desc", "SI_Start_Dt", "SI_End_Dt"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, true, true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTransTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblTransTable.setPreferredScrollableViewportSize(new java.awt.Dimension(750, 400));
        srpMemberTransCTable.setViewportView(tblTransTable);

        panTransDetailTable.add(srpMemberTransCTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTransDetails.add(panTransDetailTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panTransDetails, gridBagConstraints);
        panTransDetails.getAccessibleContext().setAccessibleName("MembershipFacifility");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        txtCustId.setText("");
        lblMemName.setText("");
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
    private com.see.truetransact.uicomponent.CLabel lblCustId;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblMemName;
    private com.see.truetransact.uicomponent.CPanel panMemDetails;
    private com.see.truetransact.uicomponent.CPanel panReportTrans;
    private com.see.truetransact.uicomponent.CPanel panTransDetailTable;
    private com.see.truetransact.uicomponent.CPanel panTransDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpMemberTransCTable;
    private com.see.truetransact.uicomponent.CTable tblTransTable;
    private com.see.truetransact.uicomponent.CTextField txtCustId;
    // End of variables declaration//GEN-END:variables
    
}
