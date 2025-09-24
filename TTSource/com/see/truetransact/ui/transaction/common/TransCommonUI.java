/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * Authorize.java
 *
 * Created on March 3, 2004, 1:46 PM
 */

package com.see.truetransact.ui.transaction.common;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import com.see.truetransact.commonutil.DateUtil;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;
import com.see.truetransact.uicomponent.CComboBox;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedComboBoxModel;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uivalidation.CurrencyValidation;

/**
 * @author  balachandar
 */
public class TransCommonUI extends com.see.truetransact.uicomponent.CDialog implements Observer {
    private final TransCommonRB resourceBundle = new TransCommonRB();
    private TransCommonOB observable;
    HashMap paramMap = null;
    CInternalFrame parent = null;
    private double amount = 0.0;
    private double transAmount = 0.0;
    private ArrayList outPutList = new ArrayList();
    private HashMap returnMap = new HashMap();
    public double reconciledAmt = 0.0;
//    ExceptionOptionsWF exceptionOptions = new ExceptionOptionsWF();

    private final static Logger log = Logger.getLogger(TransCommonUI.class);

    /** Creates new form AuthorizeUI */
    public TransCommonUI(HashMap paramMap) {
        this.paramMap = paramMap; 
        setupInit();
    }
    
    /** Creates new form AuthorizeUI */
    public TransCommonUI(CInternalFrame parent, HashMap paramMap) {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
    }

    public TransCommonUI() {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
    }
    
    private void setupInit() {
        initComponents();
        setSize(700, 400);
        internationalize();
        setObservable();
        populateData(paramMap);
        btnAuthorize.setVisible(true);
        txtSearchData.setAllowAll(true);
    }

    private void setupScreen() {
        setModal(true);

        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    private void setObservable() {
        try {
            observable = new TransCommonOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void populateData(HashMap mapID) {
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(mapID, tblData);
            if (heading != null && heading.size() > 0) {
                ArrayList head = new ArrayList();
                head.addAll(heading);
                EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(head);
                head.remove("Select");
                head.remove("TRANS_TYPE");
                head.remove("TRANS_DT");
                head.remove("RECONCILE_AMOUNT");
                cboSearchCol.setModel(cboModel);
            }
            if(observable.data != null && observable.data.size()>0){
                double totalAmt = 0.0;
                for(int i = 0;i<observable.data.size();i++){
                    String value = CommonUtil.convertObjToStr(((ArrayList)observable.data.get(i)).get(0));
                    if(value.equals("true")){
                        double reconcileAmt = CommonUtil.convertObjToDouble(((ArrayList)observable.data.get(i)).get(7)).doubleValue();
                        totalAmt = totalAmt + reconcileAmt;
                    }
                }
                lblTotalAmount.setText(String.valueOf(totalAmt));
            }
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
    }

    public void show() {
        if (observable.isAvailable()) {
            super.show();
        } else {
            if (parent != null) parent.setModified(false);
            ClientUtil.noDataAlert();
        }        
    }

    /**
     * Bring up and populate the temporary project detail screen.
     */
    private void whenTableRowSelected() {
        int rowIndexSelected = tblData.getSelectedRow();
        
        if (rowIndexSelected < 0) {
            COptionPane.showMessageDialog(null,
                resourceBundle.getString("SelectRow"), 
                resourceBundle.getString("SelectRowHeading"),
                COptionPane.OK_OPTION + COptionPane.INFORMATION_MESSAGE);
        } else {
            this.dispose();
//            if (parent != null) ((CInternalFrame) parent).fillData(observable.fillData(rowIndexSelected));
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblBatchID = new com.see.truetransact.uicomponent.CLabel();
        lblTotalAmount = new com.see.truetransact.uicomponent.CLabel();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        lblSearch = new com.see.truetransact.uicomponent.CLabel();
        cboSearchCol = new com.see.truetransact.uicomponent.CComboBox();
        txtSearchData = new com.see.truetransact.uicomponent.CTextField();
        btnAddDebit = new com.see.truetransact.uicomponent.CButton();
        lblInstrumentDate1 = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        btnSearch = new com.see.truetransact.uicomponent.CButton();
        cboSearchCriteria = new com.see.truetransact.uicomponent.CComboBox();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("Reconciliation Transaction");
        setSize(683,500);
        panTable.setLayout(new java.awt.GridBagLayout());

        panTable.setMinimumSize(new java.awt.Dimension(675, 500));
        panTable.setPreferredSize(new java.awt.Dimension(675, 500));
        srcTable.setMinimumSize(new java.awt.Dimension(675, 500));
        srcTable.setPreferredSize(new java.awt.Dimension(675, 500));
        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "BatchId", "TransType", "TransDate", "TotalTransAmt", "ReconcileAmt", "BalanceAmt", "Present Amount", "TransId"
            }
        ));
        tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblData.setPreferredScrollableViewportSize(new java.awt.Dimension(450000, 400000));
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDataMousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataMouseClicked(evt);
            }
        });
        tblData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblDataMouseMoved(evt);
            }
        });

        srcTable.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(srcTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panTable, gridBagConstraints);

        panSearch.setLayout(new java.awt.GridBagLayout());

        panSearch.setMinimumSize(new java.awt.Dimension(675, 27));
        panSearch.setPreferredSize(new java.awt.Dimension(675, 27));
        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif")));
        btnAuthorize.setText("Accept");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });

        panSearch.add(btnAuthorize, new java.awt.GridBagConstraints());

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif")));
        btnCancel.setText("Close");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        panSearch.add(btnCancel, new java.awt.GridBagConstraints());

        lblBatchID.setText("Selected Transaction Total");
        lblBatchID.setName("lblBatchID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 10);
        panSearch.add(lblBatchID, gridBagConstraints);

        lblTotalAmount.setText("[xxxxxxxxxx]");
        lblTotalAmount.setName("lblBatchIDValue");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 10);
        panSearch.add(lblTotalAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panSearch, gridBagConstraints);

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(sptLine, gridBagConstraints);

        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(675, 95));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(675, 95));
        lblSearch.setText("Search");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(lblSearch, gridBagConstraints);

        cboSearchCol.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(cboSearchCol, gridBagConstraints);

        txtSearchData.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(txtSearchData, gridBagConstraints);

        btnAddDebit.setText("View");
        btnAddDebit.setMaximumSize(new java.awt.Dimension(100, 27));
        btnAddDebit.setMinimumSize(new java.awt.Dimension(100, 27));
        btnAddDebit.setPreferredSize(new java.awt.Dimension(150, 27));
        btnAddDebit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDebitActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(btnAddDebit, gridBagConstraints);

        lblInstrumentDate1.setText("From Date");
        lblInstrumentDate1.setName("lblInstrumentDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(lblInstrumentDate1, gridBagConstraints);

        lblInstrumentDate.setText("To Date");
        lblInstrumentDate.setName("lblInstrumentDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(lblInstrumentDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(tdtFromDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(tdtToDate, gridBagConstraints);

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_FIND.gif")));
        btnSearch.setText("Find");
        btnSearch.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(btnSearch, gridBagConstraints);

        cboSearchCriteria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Starts with", "Ends with", "Exact Match", "Pattern Match" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(cboSearchCriteria, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(panSearchCondition, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void btnAddDebitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDebitActionPerformed
        // TODO add your handling code here:
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("TRANS_FROM_DT", DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
        whereMap.put("TRANS_TO_DT", DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
        whereMap.put("TRANS_TYPE", paramMap.get("TRANS_TYPE"));
        viewMap.put(CommonConstants.MAP_NAME,"getSelectReconciliationTransaction");              
        viewMap.put(CommonConstants.MAP_WHERE,whereMap);
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(viewMap, tblData);
            if (heading != null && heading.size() > 0) {
                EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(heading);
                txtSearchData.setText("");
            }else{
                show();
            }
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_btnAddDebitActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        String searchTxt = txtSearchData.getText().trim();
        tblData.setModel(observable.getTableModel());
        tblData.revalidate();
        String searchCol = CommonUtil.convertObjToStr(cboSearchCol.getSelectedItem());
        ArrayList head = observable.getTableModel().getIdentifiers();
        int selCol = 0;
        for (int i=0; i<head.size(); i++) {
            if (CommonUtil.convertObjToStr(head.get(i)).equals(searchCol))
                selCol = i;
        }
        int selColCri = cboSearchCriteria.getSelectedIndex();
        observable.searchData(searchTxt, selCol, selColCri);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseClicked
        // TODO add your handling code here:
        if(((evt.getClickCount() == 1)||(evt.getClickCount() == 2)) && (evt.getModifiers() == 16)){
            if((evt.getClickCount() == 1)){
                if(paramMap.containsKey("AUTHORIZE_MODE")){
                    ClientUtil.showAlertWindow("Selection not allowed...");
                    return;                    
                }
                outPutList = new ArrayList();
                int selected = tblData.getSelectedRow();
                int selectedColumn = tblData.getSelectedColumn();
                if(selectedColumn == 0){
                    observable.editTrans = CommonUtil.convertObjToStr(paramMap.get("TRANS_ID"));                    
                    outPutList = observable.getSelectedSingleClick(selectedColumn,selected,0,tblData);
                    double totalAmt = 0.0;
                    for(int j = 0;j<outPutList.size();j++){
                        String value = CommonUtil.convertObjToStr(((ArrayList)observable.data.get(j)).get(0));
                        if(value.equals("true")){
                            double reconcileAmt = CommonUtil.convertObjToDouble(((ArrayList)outPutList.get(j)).get(7)).doubleValue();
                            totalAmt = totalAmt + reconcileAmt;
                        }else
                            totalAmt = totalAmt + 0;
                    }
                    lblTotalAmount.setText(String.valueOf(totalAmt));
                    System.out.println("ArrayList :"+outPutList);
                    return;
                }
            }

            if((evt.getClickCount() == 2)){
                if(paramMap.containsKey("EDIT_MODE")){
                    observable.editTrans = CommonUtil.convertObjToStr(paramMap.get("TRANS_ID"));
                    outPutList = new ArrayList();
                    int selected = tblData.getSelectedRow();
                    int selectedColumn = tblData.getSelectedColumn();
                    outPutList = observable.getSelected(selected,0,tblData);
                    String presentTransId = "";
                    presentTransId = CommonUtil.convertObjToStr(((ArrayList)outPutList.get(selected)).get(10));
                    if(presentTransId.equals("") || observable.editTrans.equals(presentTransId)){
                        double enteredAmt = CommonUtil.convertObjToDouble(COptionPane.showInputDialog(this,"Enter Input Amt")).doubleValue();
                        if(enteredAmt!=0){
                            outPutList = new ArrayList();
                            outPutList = observable.getSelected(selected,enteredAmt,tblData);
                            double totalAmt = 0.0;
                            for(int j = 0;j<outPutList.size();j++){
                                String value = CommonUtil.convertObjToStr(((ArrayList)observable.data.get(j)).get(0));
                                if(value.equals("true")){
                                    double reconcileAmt = CommonUtil.convertObjToDouble(((ArrayList)outPutList.get(j)).get(7)).doubleValue();
                                    totalAmt = totalAmt + reconcileAmt;
                                }else
                                    totalAmt = totalAmt + 0;
                            }
                            lblTotalAmount.setText(String.valueOf(totalAmt));
                            System.out.println("ArrayList :"+outPutList);
                        }else{
                            enteredAmt = 0;
                            ClientUtil.showAlertWindow("Enter Correct amount");
                            return;
                        }
                    }else{
                        ClientUtil.showAlertWindow("Selection not allowed..."+"\n"+"Transaction already selected in : " +" "+
                        presentTransId+"  and authorization is pending");
                        return;
                    }
                }
                if(paramMap.containsKey("NEW_MODE")){
                    outPutList = new ArrayList();
                    int selected = tblData.getSelectedRow();
                    int selectedColumn = tblData.getSelectedColumn();
                    outPutList = observable.getSelected(selected,0,tblData);
                    String presentTransId = "";
                    presentTransId = CommonUtil.convertObjToStr(((ArrayList)outPutList.get(selected)).get(10));
                    if(presentTransId.equals("") && presentTransId.length() == 0){
                        double enteredAmt = CommonUtil.convertObjToDouble(COptionPane.showInputDialog(this,"Enter Input Amt")).doubleValue();
                        if(enteredAmt!=0){
                            outPutList = new ArrayList();
                            outPutList = observable.getSelected(selected,enteredAmt,tblData);
                            double totalAmt = 0.0;
                            for(int j = 0;j<outPutList.size();j++){
                                String value = CommonUtil.convertObjToStr(((ArrayList)observable.data.get(j)).get(0));
                                if(value.equals("true")){
                                    double reconcileAmt = CommonUtil.convertObjToDouble(((ArrayList)outPutList.get(j)).get(7)).doubleValue();
                                    totalAmt = totalAmt + reconcileAmt;
                                }else
                                    totalAmt = totalAmt + 0;
                            }
                            lblTotalAmount.setText(String.valueOf(totalAmt));
                            System.out.println("ArrayList :"+outPutList);
                        }else{
                            enteredAmt = 0;
                            ClientUtil.showAlertWindow("Enter Correct amount");
                            return;
                        }
                    }else{
                        ClientUtil.showAlertWindow("Selection not allowed..."+"\n"+"Transaction already selected in : " +" "+
                        presentTransId+"  and authorization is pending");
                        return;
                    }
                }
                if(paramMap.containsKey("AUTHORIZE_MODE")){
                    ClientUtil.showAlertWindow("Selection not allowed...");
                    return;                    
                }
            }
        }        
        
    }    //GEN-LAST:event_tblDataMouseClicked
    private ComboBoxModel getListModel() {
        ComboBoxModel listData = new ComboBoxModel();
        return listData;
    }    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
        // Add your handling code here:
//        if ((evt.getClickCount() == 2) && (evt.getModifiers() == 16)) {
//            whenTableRowSelected();
//        }
    }//GEN-LAST:event_tblDataMousePressed
    private void tblDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseMoved
//        // Add your handling code here:
//	Point p = evt.getPoint();
//	String tip =
//		String.valueOf(
//			tblData.getModel().getValueAt(
//			tblData.rowAtPoint(p),
//                        tblData.columnAtPoint(p)));
//        tblData.setToolTipText(tip);
    }//GEN-LAST:event_tblDataMouseMoved
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        setReconciledAmt(0);
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed
    private void updateDBStatus(String status) {
        //AuthorizeWFCheckUI dui = new AuthorizeWFCheckUI(observable.getSelected());
        
        HashMap screenParamMap = new HashMap();
        //screenParamMap.put (CommonConstants.AUTHORIZEDATA, dui.getCheckedAuthorizeList());
//        screenParamMap.put (CommonConstants.AUTHORIZEDATA, observable.getSelected());
//        screenParamMap.put (CommonConstants.AUTHORIZESTATUS, status);
//
//        ArrayList usrList = exceptionOptions.getSelectedList();
//        if (usrList != null && usrList.size() > 0) {
//            screenParamMap.put (CommonConstants.AUTHORIZEUSERLIST, usrList);
//        }

        System.out.println ("screenParamMap:" + screenParamMap);
        // Calls InternalFrame's autorize method with Selected Rows and the status (authorize, reject or exception)
        if (parent != null) ((CInternalFrame) parent).authorize(screenParamMap);

//        observable.updateStatus(paramMap);
    }
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        double lblAmt = CommonUtil.convertObjToDouble(lblTotalAmount.getText()).doubleValue();
        setReconciledAmt(lblAmt);
        double transactionAmt = getTransAmount();
        if(lblAmt == transactionAmt){
            this.dispose();
            HashMap resultMap = new HashMap();
            resultMap.put("TOTAL_LIST",observable.data);
            returnMap.putAll(resultMap);
        }else{
            ClientUtil.showAlertWindow("Selected amount not equal to transaction amount");
            return;
        }
        updateDBStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void internationalize() {
//        lblSearch.setText(resourceBundle.getString("lblSearch"));
//        btnSearch.setText(resourceBundle.getString("btnSearch"));
//        chkCase.setText(resourceBundle.getString("chkCase"));
//        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        HashMap mapParam = new HashMap();
        
        HashMap whereMap = new HashMap();
        whereMap.put("USER_ID", "sysadmin1");
            
        //getSelectOperativeAcctProductAuthorizeTOList
        mapParam.put(CommonConstants.MAP_NAME, "getSelectInwardClearingAuthorizeTOList");
        mapParam.put(CommonConstants.MAP_WHERE, whereMap);
        
        mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeOperativeAcctProduct");
    }

    public void update(Observable o, Object arg) {

    }
    
    /**
     * Getter for property transAmount.
     * @return Value of property transAmount.
     */
    public double getTransAmount() {
        return transAmount;
    }
    
    /**
     * Setter for property transAmount.
     * @param transAmount New value of property transAmount.
     */
    public void setTransAmount(double transAmount) {
        this.transAmount = transAmount;
    }
    
    /**
     * Getter for property returnMap.
     * @return Value of property returnMap.
     */
    public java.util.HashMap getReturnMap() {
        return returnMap;
    }
    
    /**
     * Setter for property returnMap.
     * @param returnMap New value of property returnMap.
     */
    public void setReturnMap(java.util.HashMap returnMap) {
        this.returnMap = returnMap;
    }
    
    /**
     * Getter for property reconciledAmt.
     * @return Value of property reconciledAmt.
     */
    public double getReconciledAmt() {
        return reconciledAmt;
    }
    
    /**
     * Setter for property reconciledAmt.
     * @param reconciledAmt New value of property reconciledAmt.
     */
    public void setReconciledAmt(double reconciledAmt) {
        this.reconciledAmt = reconciledAmt;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAddDebit;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnSearch;
    private com.see.truetransact.uicomponent.CComboBox cboSearchCol;
    private com.see.truetransact.uicomponent.CComboBox cboSearchCriteria;
    private com.see.truetransact.uicomponent.CLabel lblBatchID;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentDate;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentDate1;
    private com.see.truetransact.uicomponent.CLabel lblSearch;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmount;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtSearchData;
    // End of variables declaration//GEN-END:variables
}

