/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LoanLiabilityUI.java
 *
 * Created on April 13, 2011, 10:10 PM
 */

package com.see.truetransact.ui.termloan;

import com.see.truetransact.clientproxy.ProxyParameters;
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
public class LoanLiabilityUI extends com.see.truetransact.uicomponent.CDialog implements Observer {
    
    
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
    CInternalFrame parent = null;
//    public LienDetailsUI() {
     public LoanLiabilityUI() {
        initComponents();
        initForm();
        
    }
    /** Account Number Constructor */
//    public LienDetailsUI(HashMap termLoanDataMap, String totalAmount, String transType, boolean showDueTable, boolean penalDepositFlag) {
        public LoanLiabilityUI(CInternalFrame parent,HashMap map/*ArrayList alist*/) {
            currDt = ClientUtil.getCurrentDate();
            LinkedHashMap where = new LinkedHashMap();
            LinkedHashMap viewMap = new LinkedHashMap();
            this.parent = parent;
            initComponents();
            //setSizeTableData();
            lblLienDetails.setText("<HTML><b><font color=Blue>"+"Liability Details"+
            "</font></b></html>");
            //if(alist!=null && alist.size()>0){
            //    for(int i=0;i<alist.size();i++){
              //      HashMap hmap=(HashMap)alist.get(i);
                   
                   // if(LOS.equals("Y")){
               //         tblLoanDetails.setValueAt(CommonUtil.convertObjToStr(hmap.get("PROD_DESC")),i,0);
                ///        tblLoanDetails.setValueAt(CommonUtil.convertObjToStr(hmap.get("ACCT_NUM")),i,1);
               //         tblLoanDetails.setValueAt(CommonUtil.convertObjToStr(hmap.get("LIMIT")),i,2);
                //        tblLoanDetails.setValueAt(CommonUtil.convertObjToStr(hmap.get("LOAN_BALANCE_PRINCIPAL")),i,3);
                //        tblLoanDetails.setValueAt(CommonUtil.convertObjToStr(hmap.get("FROM_DT")),i,4);
                 //       tblLoanDetails.setValueAt(CommonUtil.convertObjToStr(hmap.get("TO_DT")),i,5);
                //        tblLoanDetails.setValueAt(CommonUtil.convertObjToStr(hmap.get("ORANAMENTS")),i,6);
                //        tblLoanDetails.setValueAt(CommonUtil.convertObjToStr(hmap.get("GROSS_WEIGHT")),i,7);
                //        tblLoanDetails.setValueAt(CommonUtil.convertObjToStr(hmap.get("NET_WEIGHT")),i,8);
                        
                    //}
              //  }
          //  }
            where.put("MEMBER_NO",map.get("MEMBER_NO"));
            viewMap.put(CommonConstants.MAP_NAME, "getLoanLiability");
            viewMap.put(CommonConstants.MAP_WHERE, map);
            try {
                populateData(viewMap, tblLoanDetails);
                javax.swing.table.TableColumn col = tblLoanDetails.getColumn(tblLoanDetails.getColumnName(1));
                col.setMaxWidth(120);
                col.setMinWidth(120);
                col.setWidth(120);
                col.setPreferredWidth(120);
            } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
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
            setTitle("LoanLiability"+"["+branchID+"]");
            /* Calculate the screen size */
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            System.out.println("@$#@$#@# screenSize : "+screenSize);
            setSize(800,600);
            /* Center frame on the screen */
            Dimension frameSize = this.getSize();
            if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
            if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
            this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        }
      
         private void setSizeTableData(){
        javax.swing.table.TableColumn col = tblLoanDetails.getColumn(tblLoanDetails.getColumnName(0));
        col.setMaxWidth(150);
        col = tblLoanDetails.getColumn(tblLoanDetails.getColumnName(1));
        col.setMaxWidth(120);
        col = tblLoanDetails.getColumn(tblLoanDetails.getColumnName(2));
        col.setMaxWidth(100);
        col = tblLoanDetails.getColumn(tblLoanDetails.getColumnName(3));
        col.setMaxWidth(100);
        col = tblLoanDetails.getColumn(tblLoanDetails.getColumnName(4));
        col.setMaxWidth(100);
        col = tblLoanDetails.getColumn(tblLoanDetails.getColumnName(5));
        col.setMaxWidth(100);
        col = tblLoanDetails.getColumn(tblLoanDetails.getColumnName(6));
        col.setMaxWidth(130);
        col = tblLoanDetails.getColumn(tblLoanDetails.getColumnName(7));
        col.setMaxWidth(80);
        col = tblLoanDetails.getColumn(tblLoanDetails.getColumnName(8));
        col.setMaxWidth(80);
        
    }
       /* public ArrayList populateData(ArrayList recievedList, CTable tblData) {
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
        }*/
      public ArrayList populateData(HashMap mapID, CTable tblData) {
        _tblData = tblData;
        
        // Adding Where Condition
        HashMap whereMap = null;
        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            if (mapID.get(CommonConstants.MAP_WHERE) instanceof HashMap)
                whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
            else 
                System.out.println ("Convert other data type to HashMap:" + mapID);
        } else {
            whereMap = new HashMap();
        }
        
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put (CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put (CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }
        
        mapID.put (CommonConstants.MAP_WHERE, whereMap);
        
        System.out.println ("Screen   : " + getClass());
        System.out.println ("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        System.out.println ("Map      : " + mapID);
        
        dataHash = ClientUtil.executeTableQuery(mapID);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        //if (_heading!=null && _heading.size()>0)
            //_heading.add(0, "Select");
        ArrayList arrList = new ArrayList();
        if (data!=null && data.size()>0){
            for (int i=0; i<data.size();i++) {
                arrList = (ArrayList)data.get(i);
                //arrList.add(0, new Boolean(false));
                data.set(i, arrList);
            }
        }
        System.out.println("### Data : "+data);
        populateTable();
        calculateTot();
        whereMap = null;
        return _heading;
        
    }
     public void calculateTot() {
        System.out.println("### tblLoanDetails.getRowCount() : "+tblLoanDetails.getRowCount());
        double totLimit = 0;
        double totBalanceDue = 0;
        double totGrossWt = 0;
        double totNetWt = 0;
        
        for (int i=0; i<_tblData.getRowCount(); i++) {
            totLimit = totLimit + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 2).toString()).doubleValue(); 
            totBalanceDue = totBalanceDue + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 3).toString()).doubleValue();
            //modified by rishad 16/05/2014
            if(CommonUtil.convertObjToStr(_tblData.getValueAt(i, 0))!=null&&CommonUtil.convertObjToStr(_tblData.getValueAt(i,10)).equalsIgnoreCase("GOLD_LOAN"))
            {
          totGrossWt = totGrossWt + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 7).toString()).doubleValue();        
          totNetWt = totNetWt + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 8).toString()).doubleValue();
            }
            }
        lblTotLmt.setText(CurrencyValidation.formatCrore(String.valueOf(totLimit)));
        lblBalLmt.setText(CurrencyValidation.formatCrore(String.valueOf(totBalanceDue)));
        lblGrsWt.setText(CurrencyValidation.formatCrore(String.valueOf(totGrossWt)));
        lblNetWt.setText(CurrencyValidation.formatCrore(String.valueOf(totNetWt)));
        
        System.out.println("### totLimit : "+totLimit);
        System.out.println("### totBalanceDue : "+totBalanceDue);
        System.out.println("### totGrossWt : "+totGrossWt);
        System.out.println("### totNetWt : "+totNetWt);
    }
        
public void populateTable() {
//        ArrayList heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        boolean dataExist;
        if (_heading != null){
            _isAvailable = true;
            dataExist = true;         
            setTblModel(_tblData, data, _heading);     
        }else{
            _isAvailable = false;
            dataExist = false;
            
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(new ArrayList());
            tableModel.setData(new ArrayList());
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            
            _tblData.setModel(tableSorter);
            _tblData.revalidate();
            
            ClientUtil.noDataAlert();
        }
      
    }
  private void setTblModel(CTable tbl, ArrayList tblData, ArrayList head) {
        TableSorter tableSorter = new TableSorter();
        tableSorter.addMouseListenerToHeaderInTable(tbl);
        TableModel tableModel = new TableModel(tblData, head) {
                
            };

        tableModel.fireTableDataChanged();
        tableSorter.setModel(tableModel);
        tableSorter.fireTableDataChanged();

        tbl.setModel(tableSorter);
        tbl.revalidate();        
    }
        public void displayBalance(){
            if(!hasPenal){
                
            }else{
                System.out.println("#$%#$%#$%penalAmount"+penalAmount);
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
        btnClose = new com.see.truetransact.uicomponent.CButton();
        srpLoanDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblLoanDetails = new com.see.truetransact.uicomponent.CTable();
        lblLienDetails = new com.see.truetransact.uicomponent.CLabel();
        btnOk = new com.see.truetransact.uicomponent.CButton();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        lblTotLmt = new com.see.truetransact.uicomponent.CLabel();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        lblBalLmt = new com.see.truetransact.uicomponent.CLabel();
        cLabel5 = new com.see.truetransact.uicomponent.CLabel();
        lblGrsWt = new com.see.truetransact.uicomponent.CLabel();
        cLabel7 = new com.see.truetransact.uicomponent.CLabel();
        lblNetWt = new com.see.truetransact.uicomponent.CLabel();

        getContentPane().setLayout(null);

        panMemberShipFacility.setForeground(new java.awt.Color(0, 0, 204));
        panMemberShipFacility.setFont(new java.awt.Font("Arial Narrow", 1, 12));
        panMemberShipFacility.setMaximumSize(new java.awt.Dimension(1000, 500));
        panMemberShipFacility.setMinimumSize(new java.awt.Dimension(1000, 500));
        panMemberShipFacility.setPreferredSize(new java.awt.Dimension(1000, 500));
        panMemberShipFacility.setLayout(new java.awt.GridBagLayout());

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 61, 13, 0);
        panMemberShipFacility.add(btnClose, gridBagConstraints);

        srpLoanDetails.setMaximumSize(new java.awt.Dimension(1000, 350));
        srpLoanDetails.setMinimumSize(new java.awt.Dimension(1000, 350));
        srpLoanDetails.setPreferredSize(new java.awt.Dimension(1000, 350));

        tblLoanDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblLoanDetails.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblLoanDetails.setDragEnabled(true);
        tblLoanDetails.setInheritsPopupMenu(true);
        tblLoanDetails.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        tblLoanDetails.setMinimumSize(new java.awt.Dimension(1000, 500));
        tblLoanDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 2000));
        tblLoanDetails.setPreferredSize(new java.awt.Dimension(1000, 20000));
        tblLoanDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLoanDetailsMouseClicked(evt);
            }
        });
        srpLoanDetails.setViewportView(tblLoanDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 18;
        gridBagConstraints.ipadx = -250;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 35, 0, 35);
        panMemberShipFacility.add(srpLoanDetails, gridBagConstraints);

        lblLienDetails.setText("Loan Liability");
        lblLienDetails.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.ipadx = 333;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 35, 0, 0);
        panMemberShipFacility.add(lblLienDetails, gridBagConstraints);

        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 260, 13, 0);
        panMemberShipFacility.add(btnOk, gridBagConstraints);

        cLabel1.setForeground(new java.awt.Color(0, 0, 204));
        cLabel1.setText("Total Limit:");
        cLabel1.setFont(new java.awt.Font("CentSchbook BT", 1, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 37;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 190, 0, 0);
        panMemberShipFacility.add(cLabel1, gridBagConstraints);

        lblTotLmt.setForeground(new java.awt.Color(0, 0, 204));
        lblTotLmt.setText("cLabel2");
        lblTotLmt.setFont(new java.awt.Font("CentSchbook BT", 1, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 0, 0);
        panMemberShipFacility.add(lblTotLmt, gridBagConstraints);

        cLabel3.setForeground(new java.awt.Color(0, 0, 204));
        cLabel3.setText("Total Balance Due:");
        cLabel3.setFont(new java.awt.Font("CentSchbook BT", 1, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 150, 0, 0);
        panMemberShipFacility.add(cLabel3, gridBagConstraints);

        lblBalLmt.setForeground(new java.awt.Color(0, 0, 204));
        lblBalLmt.setText("cLabel4");
        lblBalLmt.setFont(new java.awt.Font("CentSchbook BT", 1, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 0, 0);
        panMemberShipFacility.add(lblBalLmt, gridBagConstraints);

        cLabel5.setForeground(new java.awt.Color(0, 0, 204));
        cLabel5.setText("Total Gross Wt:");
        cLabel5.setFont(new java.awt.Font("CentSchbook BT", 1, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 51, 0, 0);
        panMemberShipFacility.add(cLabel5, gridBagConstraints);

        lblGrsWt.setForeground(new java.awt.Color(0, 0, 204));
        lblGrsWt.setText("cLabel6");
        lblGrsWt.setFont(new java.awt.Font("CentSchbook BT", 1, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 16;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(17, 1, 0, 0);
        panMemberShipFacility.add(lblGrsWt, gridBagConstraints);

        cLabel7.setForeground(new java.awt.Color(0, 0, 204));
        cLabel7.setText("Total Net Wt:");
        cLabel7.setFont(new java.awt.Font("CentSchbook BT", 1, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 60, 0, 0);
        panMemberShipFacility.add(cLabel7, gridBagConstraints);

        lblNetWt.setForeground(new java.awt.Color(0, 0, 204));
        lblNetWt.setText("cLabel2");
        lblNetWt.setFont(new java.awt.Font("CentSchbook BT", 1, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 16;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 1, 0, 0);
        panMemberShipFacility.add(lblNetWt, gridBagConstraints);

        getContentPane().add(panMemberShipFacility);
        panMemberShipFacility.setBounds(0, 11, 820, 500);
        panMemberShipFacility.getAccessibleContext().setAccessibleName("MembershipFacifility");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
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

private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
       
            HashMap hash = new HashMap();
//            CommonUtil.convertObjToStr(tblLoanDetails.getValueAt(tblLoanDetails.getSelectedRow(),0));
            if(tblLoanDetails.getSelectedRow()>=0){    
            //hash.put("ACT_NUM",CommonUtil.convertObjToStr(tblLoanDetails.getValueAt(tblLoanDetails.getSelectedRow(),0)));
                //COMMENTED AND MODIFIED BY RISHAD 16/05/2014
            //hash.put("ACT_NUM",CommonUtil.convertObjToStr(tblLoanDetails.getValueAt(tblLoanDetails.getSelectedRow(),0))); 
            hash.put("ACT_NUM",CommonUtil.convertObjToStr(tblLoanDetails.getValueAt(tblLoanDetails.getSelectedRow(),1)));    
            hash.put("LOAN_LIABILITY","LOAN_LIABILITY");
            hash.put("PROD_DESC",CommonUtil.convertObjToStr(tblLoanDetails.getValueAt(tblLoanDetails.getSelectedRow(),0)));    
             ClientUtil.clearAll(this);
                this.dispose();
            parent.fillData(hash);
            }else{
               ClientUtil.showAlertWindow("Please select one Account!"); 
               return;
            }
}//GEN-LAST:event_btnOkActionPerformed

private void tblLoanDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLoanDetailsMouseClicked
// TODO add your handling code here:
    if(evt.getClickCount()>=2){
    btnOkActionPerformed(null);
    }
}//GEN-LAST:event_tblLoanDetailsMouseClicked
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //        new CheckCustomerIdUI().show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CLabel cLabel5;
    private com.see.truetransact.uicomponent.CLabel cLabel7;
    private com.see.truetransact.uicomponent.CLabel lblBalLmt;
    private com.see.truetransact.uicomponent.CLabel lblGrsWt;
    private com.see.truetransact.uicomponent.CLabel lblLienDetails;
    private com.see.truetransact.uicomponent.CLabel lblNetWt;
    private com.see.truetransact.uicomponent.CLabel lblTotLmt;
    private com.see.truetransact.uicomponent.CPanel panMemberShipFacility;
    private com.see.truetransact.uicomponent.CScrollPane srpLoanDetails;
    private com.see.truetransact.uicomponent.CTable tblLoanDetails;
    // End of variables declaration//GEN-END:variables
    
}
