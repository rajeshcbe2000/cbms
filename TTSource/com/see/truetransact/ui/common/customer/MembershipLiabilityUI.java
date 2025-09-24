/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MembershipLiabilityUI.java
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

/**
 *
 * @author  Suresh
 */
public class MembershipLiabilityUI extends com.see.truetransact.uicomponent.CDialog implements Observer {
    
    
//    final CheckCustomerIdRB resourceBundle = new CheckCustomerIdRB();
    private CTable _tblData;
    private HashMap dataHash;
    private ArrayList data;
    private int dataSize;
    private ArrayList _heading;
    private boolean _isAvailable = true;
    Date currDt = null;
    public String branchID;
    public String selStringVal = "";
    public MembershipLiabilityUI() {
        initComponents();
        initForm();
    }
    /** Account Number Constructor */
    public MembershipLiabilityUI(String actNum) {
        initComponents();
        setMaxLengths();
        addToTableUsingActNo(actNum);
        currDt = ClientUtil.getCurrentDate();
        branchID = TrueTransactMain.BRANCH_ID;
        setupScreen();
        txtCustId.setEnabled(false);
        txtMemberNo.setEnabled(false);
        txtTotalShareAmount.setEnabled(false);
        txtTotalNoOfShare.setEnabled(false);
       }
    
    /** Account Number Constructor */
    public MembershipLiabilityUI(String custID, String memberNo) {
        initComponents();
        setMaxLengths();
        addToTableUsingCustId(custID,memberNo);
        currDt = ClientUtil.getCurrentDate();
        branchID = TrueTransactMain.BRANCH_ID;
        setupScreen();
        txtCustId.setEnabled(false);
        txtMemberNo.setEnabled(false);
        txtTotalShareAmount.setEnabled(false);
        txtTotalNoOfShare.setEnabled(false);
       }
     public MembershipLiabilityUI(String custID, String memberNo,String selString) {
        initComponents();
        setMaxLengths();
        selStringVal = selString;
        addToTableUsingCustId(custID,memberNo);
        currDt = ClientUtil.getCurrentDate();
        branchID = TrueTransactMain.BRANCH_ID;
        setupScreen();
        txtCustId.setEnabled(false);
        txtMemberNo.setEnabled(false);
        txtTotalShareAmount.setEnabled(false);
        txtTotalNoOfShare.setEnabled(false);
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
        setTitle("Membership Liability " + "[" + branchID + "]");
        if (selStringVal != null && selStringVal.equals("GOLD_LOAN")) {
            setTitle("GoldLoan Liability " + "[" + branchID + "]");
        }
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
    
    private void addToTableUsingActNo(String actNum){
        HashMap map = new HashMap();
        HashMap custMap = new HashMap();
        String custId="";
        String mem_No="";
        map.put("ACT_NUM", actNum);
        List custIdList = ClientUtil.executeQuery("getCustIdfromMembershipLiability",map);
        System.out.println("#$@#@$@#@# custIdList: "+custIdList);
        if(custIdList!= null && custIdList.size()>0){
            custMap = (HashMap) custIdList.get(0);
            custId = CommonUtil.convertObjToStr(custMap.get("CUST_ID"));
            mem_No = CommonUtil.convertObjToStr(custMap.get("MEM_NO"));
            txtCustId.setText(custId);
            txtMemberNo.setText(mem_No);
            custMap.put("CUSTOMER ID",custMap.get("CUST_ID"));
            displayShareDetails(custMap);
        }
        LinkedHashMap where = new LinkedHashMap();
        LinkedHashMap viewMap = new LinkedHashMap();
        where.put("CUST_ID",custId);
        where.put("MEMBER_NO",txtMemberNo.getText());
        viewMap.put(CommonConstants.MAP_NAME, "getSelectMembershipLiabilityDetails");
        viewMap.put(CommonConstants.MAP_WHERE, where);
        try {
            populateData(viewMap, tblMemberShipLiabilityList);
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
        viewMap = null;
        where = null;
    }
    
    private void addToTableUsingCustId(String custID, String memberNo){
        HashMap map = new HashMap();
        HashMap custMap = new HashMap();
        txtCustId.setText(custID);
        txtMemberNo.setText(memberNo);
        custMap.put("CUSTOMER ID",custID);
        displayShareDetails(custMap);
        LinkedHashMap where = new LinkedHashMap();
        LinkedHashMap viewMap = new LinkedHashMap();
        where.put("CUST_ID",custID);
        where.put("MEMBER_NO",txtMemberNo.getText());
        if(selStringVal!=null && selStringVal.equals("GOLD_LOAN")){
            where.put("GOLD_LOAN","GOLD_LOAN");
        }
        viewMap.put(CommonConstants.MAP_NAME, "getSelectMembershipLiabilityDetails");
        viewMap.put(CommonConstants.MAP_WHERE, where);
        try {
            populateData(viewMap, tblMemberShipLiabilityList);
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
        viewMap = null;
        where = null;
    }
    
    public ArrayList populateData(HashMap whereMap, CTable tblData) {
        _tblData = tblData;
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
            _tblData.getColumnModel().getColumn(0).setPreferredWidth(150);
            _tblData.getColumnModel().getColumn(1).setPreferredWidth(100);
            _tblData.getColumnModel().getColumn(2).setPreferredWidth(130);
            
        }
    }
    
    public void calculateTot() {
        double totLimit = 0;
        double totBalance = 0;
        double totPrincDue = 0;
        double totIntOverdue = 0;
        double totPenal = 0;
        double totCharge = 0;
        double totGrant = 0;
        for (int i=0; i<_tblData.getRowCount(); i++) {
            totLimit = totLimit + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 2).toString()).doubleValue();
            totBalance = totBalance + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 3).toString()).doubleValue();
            totPrincDue = totPrincDue + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 4).toString()).doubleValue();
            totIntOverdue = totIntOverdue + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 5).toString()).doubleValue();
            totPenal = totPenal + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 6).toString()).doubleValue();
            totCharge = totCharge + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 7).toString()).doubleValue();
            totGrant = totGrant + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 8).toString()).doubleValue();
            lblTotLimitAmountVal.setText(CurrencyValidation.formatCrore(String.valueOf(totLimit)));
            lblTotBalanceVal.setText(CurrencyValidation.formatCrore(String.valueOf(totBalance)));
            lblTotPrincipleDueVal.setText(CurrencyValidation.formatCrore(String.valueOf(totPrincDue)));
            lblTotalIntDueVal.setText(CurrencyValidation.formatCrore(String.valueOf(totIntOverdue)));
            lblTotPenalVal.setText(CurrencyValidation.formatCrore(String.valueOf(totPenal)));
            lblTotChargeVal.setText(CurrencyValidation.formatCrore(String.valueOf(totCharge)));
            lblGrantTotal.setText(CurrencyValidation.formatCrore(String.valueOf(totGrant)));
        }
    }
    
    private void displayShareDetails(HashMap hash){
        List shareLst =ClientUtil.executeQuery("getShareAccountDetails", hash);
        if(shareLst!=null && shareLst.size()>0){
            HashMap shareMap = new HashMap();
            shareMap = (HashMap)shareLst.get(0);
            txtTotalNoOfShare.setText(CommonUtil.convertObjToStr(shareMap.get("NO_OF_SHARES")));
            txtTotalShareAmount.setText(CommonUtil.convertObjToStr(shareMap.get("TOTAL_SHARE_AMOUNT")));
        }else{
            txtTotalNoOfShare.setText("");
            txtTotalShareAmount.setText("0");
        }
    }
    
    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths(){
        txtTotalShareAmount.setValidation(new CurrencyValidation(14,2));
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
        panMembershipTable = new com.see.truetransact.uicomponent.CPanel();
        srpMemberShipCTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblMemberShipLiabilityList = new com.see.truetransact.uicomponent.CTable();
        panMemDetails = new com.see.truetransact.uicomponent.CPanel();
        lblCustId = new com.see.truetransact.uicomponent.CLabel();
        lblMemberNo = new com.see.truetransact.uicomponent.CLabel();
        txtCustId = new com.see.truetransact.uicomponent.CTextField();
        txtMemberNo = new com.see.truetransact.uicomponent.CTextField();
        panTotal = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblTotLimitAmount = new com.see.truetransact.uicomponent.CLabel();
        lblTotLimitAmountVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotPrincipleDue = new com.see.truetransact.uicomponent.CLabel();
        lblTotPrincipleDueVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotalIntDue = new com.see.truetransact.uicomponent.CLabel();
        lblTotalIntDueVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotBalance = new com.see.truetransact.uicomponent.CLabel();
        lblTotBalanceVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotPenal = new com.see.truetransact.uicomponent.CLabel();
        lblTotPenalVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotCharge = new com.see.truetransact.uicomponent.CLabel();
        lblTotChargeVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotCharge1 = new com.see.truetransact.uicomponent.CLabel();
        lblGrantTotal = new com.see.truetransact.uicomponent.CLabel();
        panShareDetails = new com.see.truetransact.uicomponent.CPanel();
        lblTotalNoOfShare = new com.see.truetransact.uicomponent.CLabel();
        txtTotalNoOfShare = new com.see.truetransact.uicomponent.CTextField();
        lblTotalShareAmount = new com.see.truetransact.uicomponent.CLabel();
        txtTotalShareAmount = new com.see.truetransact.uicomponent.CTextField();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        panMemberShipFacility.setMaximumSize(new java.awt.Dimension(825, 575));
        panMemberShipFacility.setMinimumSize(new java.awt.Dimension(600, 575));
        panMemberShipFacility.setPreferredSize(new java.awt.Dimension(600, 575));
        panMemberShipFacility.setLayout(new java.awt.GridBagLayout());

        panMembershipTable.setMinimumSize(new java.awt.Dimension(350, 200));
        panMembershipTable.setPreferredSize(new java.awt.Dimension(350, 200));
        panMembershipTable.setLayout(new java.awt.GridBagLayout());

        srpMemberShipCTable.setMinimumSize(new java.awt.Dimension(600, 175));
        srpMemberShipCTable.setPreferredSize(new java.awt.Dimension(600, 175));

        tblMemberShipLiabilityList.setBackground(new java.awt.Color(212, 208, 200));
        tblMemberShipLiabilityList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Act Num", "Product Desc", "Limit Amount", "Balance", "Princ Overdue", "Int Overdue", "Penal", "Charges", "Total"
            }
        ));
        tblMemberShipLiabilityList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblMemberShipLiabilityList.setDragEnabled(true);
        tblMemberShipLiabilityList.setMaximumSize(new java.awt.Dimension(1000, 1000));
        tblMemberShipLiabilityList.setMinimumSize(new java.awt.Dimension(1000, 1000));
        tblMemberShipLiabilityList.setPreferredScrollableViewportSize(new java.awt.Dimension(100, 500));
        tblMemberShipLiabilityList.setPreferredSize(new java.awt.Dimension(1000, 1000));
        srpMemberShipCTable.setViewportView(tblMemberShipLiabilityList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panMembershipTable.add(srpMemberShipCTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        panMemberShipFacility.add(panMembershipTable, gridBagConstraints);

        panMemDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Details"));
        panMemDetails.setMaximumSize(new java.awt.Dimension(265, 75));
        panMemDetails.setMinimumSize(new java.awt.Dimension(265, 75));
        panMemDetails.setPreferredSize(new java.awt.Dimension(265, 75));
        panMemDetails.setLayout(new java.awt.GridBagLayout());

        lblCustId.setText("Cust ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        panMemDetails.add(lblCustId, gridBagConstraints);

        lblMemberNo.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 4);
        panMemDetails.add(lblMemberNo, gridBagConstraints);

        txtCustId.setBackground(new java.awt.Color(212, 208, 200));
        txtCustId.setMaxLength(16);
        txtCustId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        panMemDetails.add(txtCustId, gridBagConstraints);

        txtMemberNo.setBackground(new java.awt.Color(212, 208, 200));
        txtMemberNo.setMaxLength(16);
        txtMemberNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 5, 0);
        panMemDetails.add(txtMemberNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 17, 10);
        panMemberShipFacility.add(panMemDetails, gridBagConstraints);

        panTotal.setMaximumSize(new java.awt.Dimension(823, 100));
        panTotal.setMinimumSize(new java.awt.Dimension(500, 100));
        panTotal.setPreferredSize(new java.awt.Dimension(823, 100));
        panTotal.setLayout(new java.awt.GridBagLayout());

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        panTotal.add(btnClose, gridBagConstraints);

        lblTotLimitAmount.setText("Total Limit Amount  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 4);
        panTotal.add(lblTotLimitAmount, gridBagConstraints);

        lblTotLimitAmountVal.setMaximumSize(new java.awt.Dimension(100, 20));
        lblTotLimitAmountVal.setMinimumSize(new java.awt.Dimension(100, 20));
        lblTotLimitAmountVal.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panTotal.add(lblTotLimitAmountVal, gridBagConstraints);

        lblTotPrincipleDue.setText("Total Principle Due  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 4);
        panTotal.add(lblTotPrincipleDue, gridBagConstraints);

        lblTotPrincipleDueVal.setMinimumSize(new java.awt.Dimension(100, 20));
        lblTotPrincipleDueVal.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panTotal.add(lblTotPrincipleDueVal, gridBagConstraints);

        lblTotalIntDue.setText("Total Interest Due    : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 4);
        panTotal.add(lblTotalIntDue, gridBagConstraints);

        lblTotalIntDueVal.setMinimumSize(new java.awt.Dimension(100, 20));
        lblTotalIntDueVal.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panTotal.add(lblTotalIntDueVal, gridBagConstraints);

        lblTotBalance.setText("Total Balance  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        panTotal.add(lblTotBalance, gridBagConstraints);

        lblTotBalanceVal.setMaximumSize(new java.awt.Dimension(100, 20));
        lblTotBalanceVal.setMinimumSize(new java.awt.Dimension(100, 20));
        lblTotBalanceVal.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTotal.add(lblTotBalanceVal, gridBagConstraints);

        lblTotPenal.setText("Total Penal     : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        panTotal.add(lblTotPenal, gridBagConstraints);

        lblTotPenalVal.setMaximumSize(new java.awt.Dimension(100, 20));
        lblTotPenalVal.setMinimumSize(new java.awt.Dimension(100, 20));
        lblTotPenalVal.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTotal.add(lblTotPenalVal, gridBagConstraints);

        lblTotCharge.setText("Total Charge   : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        panTotal.add(lblTotCharge, gridBagConstraints);

        lblTotChargeVal.setMaximumSize(new java.awt.Dimension(100, 20));
        lblTotChargeVal.setMinimumSize(new java.awt.Dimension(100, 20));
        lblTotChargeVal.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTotal.add(lblTotChargeVal, gridBagConstraints);

        lblTotCharge1.setText("Grant Total     : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        panTotal.add(lblTotCharge1, gridBagConstraints);

        lblGrantTotal.setMaximumSize(new java.awt.Dimension(100, 20));
        lblGrantTotal.setMinimumSize(new java.awt.Dimension(100, 20));
        lblGrantTotal.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTotal.add(lblGrantTotal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 42, 0, 42);
        panMemberShipFacility.add(panTotal, gridBagConstraints);

        panShareDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Share Details"));
        panShareDetails.setMinimumSize(new java.awt.Dimension(285, 75));
        panShareDetails.setPreferredSize(new java.awt.Dimension(285, 75));
        panShareDetails.setLayout(new java.awt.GridBagLayout());

        lblTotalNoOfShare.setText("Total No Of Share");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        panShareDetails.add(lblTotalNoOfShare, gridBagConstraints);

        txtTotalNoOfShare.setBackground(new java.awt.Color(212, 208, 200));
        txtTotalNoOfShare.setMaxLength(16);
        txtTotalNoOfShare.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        panShareDetails.add(txtTotalNoOfShare, gridBagConstraints);

        lblTotalShareAmount.setText("Total Share Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 4);
        panShareDetails.add(lblTotalShareAmount, gridBagConstraints);

        txtTotalShareAmount.setBackground(new java.awt.Color(212, 208, 200));
        txtTotalShareAmount.setMaxLength(16);
        txtTotalShareAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 5, 0);
        panShareDetails.add(txtTotalShareAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 17, 44);
        panMemberShipFacility.add(panShareDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panMemberShipFacility, gridBagConstraints);
        panMemberShipFacility.getAccessibleContext().setAccessibleName("MembershipFacifility");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
         txtCustId.setText("");
         txtMemberNo.setText("");
         lblTotLimitAmountVal.setText("");
         lblTotPrincipleDueVal.setText("");
         lblTotPenalVal.setText("");
         lblTotBalanceVal.setText("");
         lblTotChargeVal.setText("");
         lblTotalIntDueVal.setText("");
         txtTotalNoOfShare.setText("");
         txtTotalShareAmount.setText("");
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
    private com.see.truetransact.uicomponent.CLabel lblGrantTotal;
    private com.see.truetransact.uicomponent.CLabel lblMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblTotBalance;
    private com.see.truetransact.uicomponent.CLabel lblTotBalanceVal;
    private com.see.truetransact.uicomponent.CLabel lblTotCharge;
    private com.see.truetransact.uicomponent.CLabel lblTotCharge1;
    private com.see.truetransact.uicomponent.CLabel lblTotChargeVal;
    private com.see.truetransact.uicomponent.CLabel lblTotLimitAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotLimitAmountVal;
    private com.see.truetransact.uicomponent.CLabel lblTotPenal;
    private com.see.truetransact.uicomponent.CLabel lblTotPenalVal;
    private com.see.truetransact.uicomponent.CLabel lblTotPrincipleDue;
    private com.see.truetransact.uicomponent.CLabel lblTotPrincipleDueVal;
    private com.see.truetransact.uicomponent.CLabel lblTotalIntDue;
    private com.see.truetransact.uicomponent.CLabel lblTotalIntDueVal;
    private com.see.truetransact.uicomponent.CLabel lblTotalNoOfShare;
    private com.see.truetransact.uicomponent.CLabel lblTotalShareAmount;
    private com.see.truetransact.uicomponent.CPanel panMemDetails;
    private com.see.truetransact.uicomponent.CPanel panMemberShipFacility;
    private com.see.truetransact.uicomponent.CPanel panMembershipTable;
    private com.see.truetransact.uicomponent.CPanel panShareDetails;
    private com.see.truetransact.uicomponent.CPanel panTotal;
    private com.see.truetransact.uicomponent.CScrollPane srpMemberShipCTable;
    private com.see.truetransact.uicomponent.CTable tblMemberShipLiabilityList;
    private com.see.truetransact.uicomponent.CTextField txtCustId;
    private com.see.truetransact.uicomponent.CTextField txtMemberNo;
    private com.see.truetransact.uicomponent.CTextField txtTotalNoOfShare;
    private com.see.truetransact.uicomponent.CTextField txtTotalShareAmount;
    // End of variables declaration//GEN-END:variables
    
}
