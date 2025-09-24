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
public class TransHOCommonUI extends com.see.truetransact.uicomponent.CDialog implements Observer {
    private final TransCommonRB resourceBundle = new TransCommonRB();
    private TransHOCommonOB observable;
    HashMap paramMap = null;
    CInternalFrame parent = null;
    private double amount = 0.0;
    private double transAmount = 0.0;
    private ArrayList outPutList = new ArrayList();
    private HashMap returnMap = new HashMap();
    public double reconciledAmt = 0.0;
    private String typeOfTrans = "";
    
    private String cboBranchValue = "";
    private String txtAdviceNoValue = "";
    private String tdtAdviceDateValue = "";
    private String txtAmountValue = "";
    private String txtParticularsValue = "";
    private String cboCategoryValue = "";
    private String txtRemarksValue = "";
    private String modeOFStatus = "";
    private HashMap adviceDebitMap = new HashMap();
    private HashMap adviceCreditMap = new HashMap();
//    ExceptionOptionsWF exceptionOptions = new ExceptionOptionsWF();

    private final static Logger log = Logger.getLogger(TransCommonUI.class);

    /** Creates new form AuthorizeUI */
    public TransHOCommonUI(HashMap paramMap,String TransactionType) {
        if(!paramMap.isEmpty()){
            this.paramMap = paramMap; 
        }else{
            paramMap = new HashMap();
        }
        typeOfTrans = TransactionType;
        setupInit();
    }
    
    /** Creates new form AuthorizeUI */
    public TransHOCommonUI(CInternalFrame parent, HashMap paramMap) {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
    }

    public TransHOCommonUI() {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
//        setObservable();
    }
    
    private void setupInit() {
        initComponents();
        setSize(700, 400);
        internationalize();
        setObservable();
        cboBranchVal.setModel(observable.getCbmBranchValue());
        cboCategoryVal.setModel(observable.getCbmCategoryVal());
        if(!typeOfTrans.equals("") && typeOfTrans.equals("DEBIT")){
            sptLine.setVisible(true);
            lblAdviceNo.setVisible(true);
            txtRemarks.setVisible(true);
            txtRemarksVal.setVisible(true);
            panTable.setVisible(true);
            lblSearch.setText("Originating Branch");
            lblBranchName.setText("Originating Branch Name");
            setTitle("Responding Details");
            if(paramMap.containsKey("EDIT_MODE") || paramMap.containsKey("AUTHORIZE_MODE")){
                List lst = ClientUtil.executeQuery("getSelectRespondingDetails", paramMap);
                if(lst!=null && lst.size()>0){
                    HashMap outPutMap = (HashMap)lst.get(0);
                    cboBranchVal.setSelectedItem(outPutMap.get("ORIGINATING_BRANCH_ID"));
                    lblBranchNameValue.setText(CommonUtil.convertObjToStr(outPutMap.get("ORIGINATING_NAME")));
                    txtAdviceNoVal.setText(CommonUtil.convertObjToStr(outPutMap.get("ADVICE_NUMBER")));
                    tdtAdviceDate.setDateValue(CommonUtil.convertObjToStr(outPutMap.get("ADVICE_DATE")));
                    if(!paramMap.containsKey("EDIT_MODE")){
                        txtAmountVal.setText(CommonUtil.convertObjToStr(outPutMap.get("AMOUNT")));
                    }else{
                        txtAmountVal.setText(CommonUtil.convertObjToStr(paramMap.get("AMOUNT")));
                    }
                    txtParticularsVal.setText(CommonUtil.convertObjToStr(outPutMap.get("PARTICULARS")));
                    cboCategoryVal.setSelectedItem(outPutMap.get("CATEGORY"));
                    txtRemarksVal.setText(CommonUtil.convertObjToStr(outPutMap.get("REMARKS")));                
                }
            }
            if(paramMap.containsKey("AUTHORIZE_MODE")){
                cboBranchVal.setEnabled(false);
                txtAdviceNoVal.setEnabled(false);
                tdtAdviceDate.setEnabled(false);
                txtAmountVal.setEnabled(false);
                txtParticularsVal.setEnabled(false);
                cboCategoryVal.setEnabled(false);
                txtRemarksVal.setEnabled(false);
                btnDepSubNoAccSave.setVisible(false);
                btnDepSubNoAccNew.setVisible(false);
            }                   
        }else if(!typeOfTrans.equals("") && typeOfTrans.equals("CREDIT")){
            sptLine.setVisible(false);
            lblAdviceNo.setVisible(true);
            panTable.setVisible(false);
            txtRemarks.setVisible(false);
            txtRemarksVal.setVisible(false);
            txtAmountVal.setText(CommonUtil.convertObjToStr(paramMap.get("AMOUNT")));
            txtAmountVal.setEnabled(false);
            lblSearch.setText("Responding Branch");
            lblBranchName.setText("Responding Branch Name");
            setTitle("Originating Details");
            if(paramMap.containsKey("EDIT_MODE") || paramMap.containsKey("AUTHORIZE_MODE")){
                List lst = ClientUtil.executeQuery("getSelectOriginatingDetails", paramMap);
                if(lst!=null && lst.size()>0){
                    HashMap outPutMap = (HashMap)lst.get(0);
                    cboBranchVal.setSelectedItem(outPutMap.get("RESPONDING_BRANCH_ID"));
                    lblBranchNameValue.setText(CommonUtil.convertObjToStr(outPutMap.get("BRANCH_NAME")));
                    txtAdviceNoVal.setText(CommonUtil.convertObjToStr(outPutMap.get("ADVICE_NUMBER")));
                    tdtAdviceDate.setDateValue(CommonUtil.convertObjToStr(outPutMap.get("ADVICE_DATE")));
                    if(!paramMap.containsKey("EDIT_MODE")){
                        txtAmountVal.setText(CommonUtil.convertObjToStr(outPutMap.get("AMOUNT")));
                    }else{
                        txtAmountVal.setText(CommonUtil.convertObjToStr(paramMap.get("AMOUNT")));
                    }
                    txtParticularsVal.setText(CommonUtil.convertObjToStr(outPutMap.get("PARTICULARS")));
                    cboCategoryVal.setSelectedItem(outPutMap.get("CATEGORY"));
                    txtRemarksVal.setText(CommonUtil.convertObjToStr(outPutMap.get("REMARKS")));                
                }
            }
            if(paramMap.containsKey("AUTHORIZE_MODE")){
                cboBranchVal.setEnabled(false);
                txtAdviceNoVal.setEnabled(false);
                tdtAdviceDate.setEnabled(false);
                txtAmountVal.setEnabled(false);
                txtParticularsVal.setEnabled(false);
                cboCategoryVal.setEnabled(false);
                txtRemarksVal.setEnabled(false);
                btnDepSubNoAccSave.setVisible(false);
                btnDepSubNoAccNew.setVisible(false);
            }            
        }

//        btnAuthorize.setVisible(true);
//        txtSearchData.setAllowAll(true);
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
            observable = new TransHOCommonOB();
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
//                cboSearchCol.setModel(cboModel);
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
//                lblTotalAmount.setText(String.valueOf(totalAmt));
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
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        lblSearch = new com.see.truetransact.uicomponent.CLabel();
        cboBranchVal = new com.see.truetransact.uicomponent.CComboBox();
        txtParticularsVal = new com.see.truetransact.uicomponent.CTextField();
        lblInstrumentDate1 = new com.see.truetransact.uicomponent.CLabel();
        cboCategoryVal = new com.see.truetransact.uicomponent.CComboBox();
        lblInstrumentDate2 = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentDate4 = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentDate5 = new com.see.truetransact.uicomponent.CLabel();
        tdtAdviceDate = new com.see.truetransact.uicomponent.CDateField();
        lblAdviceNo = new com.see.truetransact.uicomponent.CLabel();
        lblBranchName = new com.see.truetransact.uicomponent.CLabel();
        txtAmountVal = new com.see.truetransact.uicomponent.CTextField();
        txtAdviceNoVal = new com.see.truetransact.uicomponent.CTextField();
        txtRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarksVal = new com.see.truetransact.uicomponent.CTextField();
        lblBranchNameValue = new com.see.truetransact.uicomponent.CLabel();
        btnDepSubNoAccSave = new com.see.truetransact.uicomponent.CButton();
        btnDepSubNoAccNew = new com.see.truetransact.uicomponent.CButton();

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
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panTable, gridBagConstraints);

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(sptLine, gridBagConstraints);

        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(675, 200));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(675, 200));
        lblSearch.setText("Responding Branch");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(lblSearch, gridBagConstraints);

        cboBranchVal.setMinimumSize(new java.awt.Dimension(100, 21));
        cboBranchVal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBranchValActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(cboBranchVal, gridBagConstraints);

        txtParticularsVal.setMinimumSize(new java.awt.Dimension(200, 21));
        txtParticularsVal.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(txtParticularsVal, gridBagConstraints);

        lblInstrumentDate1.setText("Category");
        lblInstrumentDate1.setName("lblInstrumentDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(lblInstrumentDate1, gridBagConstraints);

        cboCategoryVal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Starts with", "Ends with", "Exact Match", "Pattern Match" }));
        cboCategoryVal.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(cboCategoryVal, gridBagConstraints);

        lblInstrumentDate2.setText("Amount");
        lblInstrumentDate2.setName("lblInstrumentDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(lblInstrumentDate2, gridBagConstraints);

        lblInstrumentDate4.setText("Particulars");
        lblInstrumentDate4.setName("lblInstrumentDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(lblInstrumentDate4, gridBagConstraints);

        lblInstrumentDate5.setText("Advice Date");
        lblInstrumentDate5.setName("lblInstrumentDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(lblInstrumentDate5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(tdtAdviceDate, gridBagConstraints);

        lblAdviceNo.setText("Advice No");
        lblAdviceNo.setName("lblInstrumentDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(lblAdviceNo, gridBagConstraints);

        lblBranchName.setText("Branch Name");
        lblBranchName.setName("lblInstrumentDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(lblBranchName, gridBagConstraints);

        txtAmountVal.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(txtAmountVal, gridBagConstraints);

        txtAdviceNoVal.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(txtAdviceNoVal, gridBagConstraints);

        txtRemarks.setText("Remarks");
        txtRemarks.setName("lblInstrumentDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(txtRemarks, gridBagConstraints);

        txtRemarksVal.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRemarksVal.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(txtRemarksVal, gridBagConstraints);

        lblBranchNameValue.setText("                                                              ");
        lblBranchNameValue.setMinimumSize(new java.awt.Dimension(250, 18));
        lblBranchNameValue.setName("lblInstrumentDate");
        lblBranchNameValue.setPreferredSize(new java.awt.Dimension(250, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(lblBranchNameValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(panSearchCondition, gridBagConstraints);

        btnDepSubNoAccSave.setText("Accept");
        btnDepSubNoAccSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepSubNoAccSaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 200, 0, 0);
        getContentPane().add(btnDepSubNoAccSave, gridBagConstraints);

        btnDepSubNoAccNew.setText("Celar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(btnDepSubNoAccNew, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void cboBranchValActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBranchValActionPerformed
        // TODO add your handling code here:
        if(cboBranchVal.getSelectedIndex()>0){
            HashMap branchNameMap = new HashMap();
            branchNameMap.put("BRANCH CODE",cboBranchVal.getSelectedItem());
            List lst = ClientUtil.executeQuery("getSelectBranchName", branchNameMap);
            if(lst != null && lst.size()>0){
                branchNameMap = (HashMap)lst.get(0);
                lblBranchNameValue.setText(String.valueOf(branchNameMap.get("BRANCH_NAME")));
            }
            if(!typeOfTrans.equals("") && typeOfTrans.equals("DEBIT")){
                HashMap viewMap = new HashMap();
                HashMap whereMap = new HashMap();
                whereMap.put("BRANCH_CODE", cboBranchVal.getSelectedItem());
                whereMap.put("TRANS_TYPE", paramMap.get("TRANS_TYPE"));
                viewMap.put(CommonConstants.MAP_NAME,"getSelectOriginatingAuthRecords");              
                viewMap.put(CommonConstants.MAP_WHERE,whereMap);
                try {
                    log.info("populateData...");
                    ArrayList heading = observable.populateData(viewMap, tblData);
                    if (heading != null && heading.size() > 0) {
                        EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(heading);
                    }else{
                        show();
                    }
                }catch(Exception e){

                }
            }
        }
    }//GEN-LAST:event_cboBranchValActionPerformed

    private void btnDepSubNoAccSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepSubNoAccSaveActionPerformed
        // TODO add your handling code here:
        HashMap listMap = new HashMap();
        if(!typeOfTrans.equals("") && typeOfTrans.equals("DEBIT")){
            adviceDebitMap = new HashMap();
            adviceDebitMap.put("ORIGINATING_BRANCH_ID",cboBranchVal.getSelectedItem());
            adviceDebitMap.put("ORIGINATING_NAME",lblBranchNameValue.getText());
            adviceDebitMap.put("ADVICE_NUMBER",txtAdviceNoVal.getText());
            java.util.Date closedDt = DateUtil.getDateMMDDYYYY(tdtAdviceDate.getDateValue());
            java.util.Date schemeCloseDt = ClientUtil.getCurrentDate();
            if(closedDt!=null && closedDt.getDate()>0){
                schemeCloseDt.setDate(closedDt.getDate());
                schemeCloseDt.setMonth(closedDt.getMonth());
                schemeCloseDt.setYear(closedDt.getYear());
            }
            adviceDebitMap.put("ADVICE_DATE",schemeCloseDt);
            adviceDebitMap.put("AMOUNT",txtAmountVal.getText());
            adviceDebitMap.put("PARTICULARS",txtParticularsVal.getText());
            adviceDebitMap.put("CATEGORY",cboCategoryVal.getSelectedItem());
            adviceDebitMap.put("REMARKS",txtRemarksVal.getText());
            System.out.println("######### adviceDebitMap :"+adviceDebitMap);
            listMap = adviceDebitMap;
//            List lst = ClientUtil.executeQuery("", listMap);
//            if(lst!=null && lst.size()>0){
//
//            }
        }
        if(!typeOfTrans.equals("") && typeOfTrans.equals("CREDIT")){
            adviceCreditMap = new HashMap();
            adviceCreditMap.put("RESPONDING_BRANCH_ID",cboBranchVal.getSelectedItem());
            adviceCreditMap.put("BRANCH_NAME",lblBranchNameValue.getText());
            adviceCreditMap.put("ADVICE_NUMBER",txtAdviceNoVal.getText());
            java.util.Date closedDt = DateUtil.getDateMMDDYYYY(tdtAdviceDate.getDateValue());
            java.util.Date schemeCloseDt = ClientUtil.getCurrentDate();
            if(closedDt!=null && closedDt.getDate()>0){
                schemeCloseDt.setDate(closedDt.getDate());
                schemeCloseDt.setMonth(closedDt.getMonth());
                schemeCloseDt.setYear(closedDt.getYear());
            }
            adviceCreditMap.put("ADVICE_DATE",schemeCloseDt);
            adviceCreditMap.put("AMOUNT",txtAmountVal.getText());
            adviceCreditMap.put("PARTICULARS",txtParticularsVal.getText());
            adviceCreditMap.put("CATEGORY",cboCategoryVal.getSelectedItem());
            adviceCreditMap.put("REMARKS",txtRemarksVal.getText());
            System.out.println("######### adviceCreditMap :"+adviceCreditMap);
            listMap = adviceCreditMap; 
//            List lst = ClientUtil.executeQuery("", listMap);
//            if(lst!=null && lst.size()>0){
//
//            }
        }        
        
    }//GEN-LAST:event_btnDepSubNoAccSaveActionPerformed

    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
        // Add your handling code here:
        //        if ((evt.getClickCount() == 2) && (evt.getModifiers() == 16)) {
        //            whenTableRowSelected();
        //        }
    }//GEN-LAST:event_tblDataMousePressed

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
//                    lblTotalAmount.setText(String.valueOf(totalAmt));
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
//                            lblTotalAmount.setText(String.valueOf(totalAmt));
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
//                    cboBranchVal.setSelectedItem(outPutMap.get("RESPONDING_BRANCH_ID"));
//                    lblBranchNameValue.setText(CommonUtil.convertObjToStr(outPutMap.get("BRANCH_NAME")));
                    txtAdviceNoVal.setText(CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(),6)));
                    tdtAdviceDate.setDateValue(CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(),7)));
                    txtAmountVal.setText(CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(),4)));
                    txtParticularsVal.setText(CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(),5)));
                    cboCategoryVal.setSelectedItem(tblData.getValueAt(tblData.getSelectedRow(),3));
//                    txtRemarksVal.setText(CommonUtil.convertObjToStr(outPutMap.get("REMARKS")));                
                    
//                    outPutList = new ArrayList();
//                    int selected = tblData.getSelectedRow();
//                    int selectedColumn = tblData.getSelectedColumn();
//                    outPutList = observable.getSelected(selected,0,tblData);
//                    String presentTransId = "";
//                    presentTransId = CommonUtil.convertObjToStr(((ArrayList)outPutList.get(selected)).get(10));
//                    if(presentTransId.equals("") && presentTransId.length() == 0){
//                        double enteredAmt = CommonUtil.convertObjToDouble(COptionPane.showInputDialog(this,"Enter Input Amt")).doubleValue();
//                        if(enteredAmt!=0){
//                            outPutList = new ArrayList();
//                            outPutList = observable.getSelected(selected,enteredAmt,tblData);
//                            double totalAmt = 0.0;
//                            for(int j = 0;j<outPutList.size();j++){
//                                String value = CommonUtil.convertObjToStr(((ArrayList)observable.data.get(j)).get(0));
//                                if(value.equals("true")){
//                                    double reconcileAmt = CommonUtil.convertObjToDouble(((ArrayList)outPutList.get(j)).get(7)).doubleValue();
//                                    totalAmt = totalAmt + reconcileAmt;
//                                }else
//                                    totalAmt = totalAmt + 0;
//                            }
////                            lblTotalAmount.setText(String.valueOf(totalAmt));
//                            System.out.println("ArrayList :"+outPutList);
//                        }else{
//                            enteredAmt = 0;
//                            ClientUtil.showAlertWindow("Enter Correct amount");
//                            return;
//                        }
//                    }else{
//                        ClientUtil.showAlertWindow("Selection not allowed..."+"\n"+"Transaction already selected in : " +" "+
//                        presentTransId+"  and authorization is pending");
//                        return;
//                    }
                }
                if(paramMap.containsKey("AUTHORIZE_MODE")){
                    ClientUtil.showAlertWindow("Selection not allowed...");
                    return;
                }
            }
        }
    }//GEN-LAST:event_tblDataMouseClicked

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
    private ComboBoxModel getListModel() {
        ComboBoxModel listData = new ComboBoxModel();
        return listData;    
    }
    
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
        private void internationalize() {
//        lblSearch.setText(resourceBundle.getString("lblSearch"));
//        btnSearch.setText(resourceBundle.getString("btnSearch"));
//        chkCase.setText(resourceBundle.getString("chkCase"));
//        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
//        btnCancel.setText(resourceBundle.getString("btnCancel"));
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
    
    /**
     * Getter for property cboBranchValue.
     * @return Value of property cboBranchValue.
     */
    public java.lang.String getCboBranchValue() {
        return cboBranchValue;
    }
    
    /**
     * Setter for property cboBranchValue.
     * @param cboBranchValue New value of property cboBranchValue.
     */
    public void setCboBranchValue(java.lang.String cboBranchValue) {
        this.cboBranchValue = cboBranchValue;
    }
    
    /**
     * Getter for property txtAdviceNoValue.
     * @return Value of property txtAdviceNoValue.
     */
    public java.lang.String getTxtAdviceNoValue() {
        return txtAdviceNoValue;
    }
    
    /**
     * Setter for property txtAdviceNoValue.
     * @param txtAdviceNoValue New value of property txtAdviceNoValue.
     */
    public void setTxtAdviceNoValue(java.lang.String txtAdviceNoValue) {
        this.txtAdviceNoValue = txtAdviceNoValue;
    }
    
    /**
     * Getter for property tdtAdviceDateValue.
     * @return Value of property tdtAdviceDateValue.
     */
    public java.lang.String getTdtAdviceDateValue() {
        return tdtAdviceDateValue;
    }
    
    /**
     * Setter for property tdtAdviceDateValue.
     * @param tdtAdviceDateValue New value of property tdtAdviceDateValue.
     */
    public void setTdtAdviceDateValue(java.lang.String tdtAdviceDateValue) {
        this.tdtAdviceDateValue = tdtAdviceDateValue;
    }
    
    /**
     * Getter for property txtAmountValue.
     * @return Value of property txtAmountValue.
     */
    public java.lang.String getTxtAmountValue() {
        return txtAmountValue;
    }
    
    /**
     * Setter for property txtAmountValue.
     * @param txtAmountValue New value of property txtAmountValue.
     */
    public void setTxtAmountValue(java.lang.String txtAmountValue) {
        this.txtAmountValue = txtAmountValue;
    }
    
    /**
     * Getter for property txtParticularsValue.
     * @return Value of property txtParticularsValue.
     */
    public java.lang.String getTxtParticularsValue() {
        return txtParticularsValue;
    }
    
    /**
     * Setter for property txtParticularsValue.
     * @param txtParticularsValue New value of property txtParticularsValue.
     */
    public void setTxtParticularsValue(java.lang.String txtParticularsValue) {
        this.txtParticularsValue = txtParticularsValue;
    }
    
    /**
     * Getter for property cboCategoryValue.
     * @return Value of property cboCategoryValue.
     */
    public java.lang.String getCboCategoryValue() {
        return cboCategoryValue;
    }
    
    /**
     * Setter for property cboCategoryValue.
     * @param cboCategoryValue New value of property cboCategoryValue.
     */
    public void setCboCategoryValue(java.lang.String cboCategoryValue) {
        this.cboCategoryValue = cboCategoryValue;
    }
    
    /**
     * Getter for property txtRemarksValue.
     * @return Value of property txtRemarksValue.
     */
    public java.lang.String getTxtRemarksValue() {
        return txtRemarksValue;
    }
    
    /**
     * Setter for property txtRemarksValue.
     * @param txtRemarksValue New value of property txtRemarksValue.
     */
    public void setTxtRemarksValue(java.lang.String txtRemarksValue) {
        this.txtRemarksValue = txtRemarksValue;
    }
    
    /**
     * Getter for property adviceDebitMap.
     * @return Value of property adviceDebitMap.
     */
    public java.util.HashMap getAdviceDebitMap() {
        return adviceDebitMap;
    }
    
    /**
     * Setter for property adviceDebitMap.
     * @param adviceDebitMap New value of property adviceDebitMap.
     */
    public void setAdviceDebitMap(java.util.HashMap adviceDebitMap) {
        this.adviceDebitMap = adviceDebitMap;
    }
    
    /**
     * Getter for property adviceCreditMap.
     * @return Value of property adviceCreditMap.
     */
    public java.util.HashMap getAdviceCreditMap() {
        return adviceCreditMap;
    }
    
    /**
     * Setter for property adviceCreditMap.
     * @param adviceCreditMap New value of property adviceCreditMap.
     */
    public void setAdviceCreditMap(java.util.HashMap adviceCreditMap) {
        this.adviceCreditMap = adviceCreditMap;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnDepSubNoAccNew;
    private com.see.truetransact.uicomponent.CButton btnDepSubNoAccSave;
    private com.see.truetransact.uicomponent.CComboBox cboBranchVal;
    private com.see.truetransact.uicomponent.CComboBox cboCategoryVal;
    private com.see.truetransact.uicomponent.CLabel lblAdviceNo;
    private com.see.truetransact.uicomponent.CLabel lblBranchName;
    private com.see.truetransact.uicomponent.CLabel lblBranchNameValue;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentDate1;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentDate2;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentDate4;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentDate5;
    private com.see.truetransact.uicomponent.CLabel lblSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CDateField tdtAdviceDate;
    private com.see.truetransact.uicomponent.CTextField txtAdviceNoVal;
    private com.see.truetransact.uicomponent.CTextField txtAmountVal;
    private com.see.truetransact.uicomponent.CTextField txtParticularsVal;
    private com.see.truetransact.uicomponent.CLabel txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtRemarksVal;
    // End of variables declaration//GEN-END:variables
}

