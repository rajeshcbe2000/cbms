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

package com.see.truetransact.ui.termloan.loansubsidy;

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import javax.swing.table.*; 
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Component;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.event.ListSelectionListener;
//import javax.swing.DefaultListModel;

import org.apache.log4j.Logger;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.EnhancedComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.viewall.ViewAll;

import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.LinkedHashMap;

/**
 * @author  bala
 */
public class LoanSubsidyProvisionUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {
    private final LoanSubsidyRB resourceBundle = new LoanSubsidyRB();
    private TableModelListener tableModelListener;
    private LoanSubsidyProvisionOB observable;
    HashMap paramMap = null;
    CInternalFrame parent = null;
    javax.swing.JList lstSearch;
    java.util.ArrayList arrLst=new java.util.ArrayList();
    Date currDt = null;
    TTIntegration ttIntegration = null;
    int previousRow = -1;
    HashMap accountNumberMap = null;
    HashMap guarantorMemberMap = null;
    HashMap accountChargeMap = null;
    HashMap guarantorChargeMap = null;
    String bankName = "";
    boolean generateNotice = false;
    final int TO=0, FROM=1;
    int viewType=-1;
    private String subsidyId="";
    boolean isFilled=false;
    boolean transAmtEdit=false;
    private StringBuffer acccountList =new StringBuffer();
    private final static Logger log = Logger.getLogger(LoanSubsidyProvisionUI.class);

    /** Creates new form AuthorizeUI */
    public LoanSubsidyProvisionUI() {
        setupInit();
        setupScreen();
    }
    
    /** Creates new form AuthorizeUI */
    public LoanSubsidyProvisionUI(CInternalFrame parent, HashMap paramMap) {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
    }
    
    private void setupInit() {
        currDt = ClientUtil.getCurrentDate();
       
        initComponents();
        internationalize();
        setObservable();
        createCboProdType();
        createCboNoticeType();
        setMaxLength();
        setButtonEnableDisable();
        //initSubsidyTableData();
        enableDisableSearchDetails(false);
        lblToDate.setVisible(true);
        tdtFromDt.setVisible(true);
        tdtFromDt.setEnabled(false);
        tdtFromDt.setDateValue(DateUtil.getStringDate(currDt));
    }

    private void enableDisableSearchDetails(boolean flag){
        ClientUtil.enableDisable(panMultiSearch,flag);
        btnProcess.setEnabled(flag);
        btnFromAccountNo.setEnabled(flag);
    }
    private void setMaxLength(){
        txtAcctNo.setAllowAll(true);
        
    }
    private void setupScreen() {
        //        setModal(true);
        
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
            observable = new LoanSubsidyProvisionOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void fillData(Object obj){
        HashMap dataMap =(HashMap)obj;
      
        if (viewType == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            subsidyId = "";
            observable.setSubsidyId("");
            subsidyId = CommonUtil.convertObjToStr(dataMap.get("SUBSIDY_ID"));
            if (viewType == ClientConstants.ACTIONTYPE_AUTHORIZE) {
                isFilled = true;
            }
            observable.setSubsidyId(subsidyId);
            lblSubsidyId.setText(subsidyId);
            editDeleteTableData(dataMap);
            setButtonEnableDisable();
            initSubsidyAuthTableData();
            javax.swing.table.TableColumn col = tblData.getColumn(tblData.getColumnName(1));
            col.setMaxWidth(150);
            col.setMinWidth(150);
            col.setWidth(150);
            col.setPreferredWidth(150);
        }
        if(viewType==FROM){
            if(dataMap.containsKey("ACCOUNTNO")){
                txtAcctNo.setText(CommonUtil.convertObjToStr(dataMap.get("ACCOUNTNO")));
            }else{
                txtAcctNo.setText(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
            }
        }
        
    }
    private void editDeleteTableData(HashMap map){
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE){
            transAmtEdit=false;
        }
        
        else{
            transAmtEdit=true;
        }
        //initSubsidyTableData();
    }
    
//    public void populateData(HashMap mapID) {
//        try {
//            log.info("populateData...");
//            ArrayList heading = observable.populateData(mapID, tblData);
//            if (heading != null && heading.size() > 0) {
//                EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(heading);
//                //                cboSearchCol.setModel(cboModel);
//            }
//        } catch( Exception e ) {
//            System.err.println( "Exception " + e.toString() + "Caught" );
//            e.printStackTrace();
//        }
//    }
    
    //    public void show() {
    //        if (observable.isAvailable()) {
    //            super.show();
    //        } else {
    //            if (parent != null) parent.setModified(false);
    //            ClientUtil.noDataAlert();
    //        }
    //    }
    
    //    public void setVisible(boolean visible) {
    //        if (observable.isAvailable()) {
    //            super.setVisible(visible);
    //        }
    //    }
    
    /**
     * Bring up and populate the temporary project detail screen.
     */
    private void whenTableRowSelected() {
        int rowIndexSelected = tblData.getSelectedRow();
        //        if (previousRow!=-1)
        //            if (!((Boolean) tblData.getValueAt(previousRow, 0)).booleanValue()) {
        //                int guarantorRowIndexSelected = tblGuarantorData.getSelectedRow();
        //                if (accountNumberMap==null) {
        //                    accountNumberMap = new HashMap();
        //                }
        //                if (guarantorMemberMap==null) {
        //                    guarantorMemberMap = new HashMap();
        //                }
        //                if (previousRow!=-1 && previousRow!=rowIndexSelected) {
//        isSelectedRowTicked(tblGuarantorData);
        setColour();
       
        //                }
        //            } else {
        //                observable.setSelectAll(tblGuarantorData, new Boolean(false));
        //            }
        
    }
    
//    private void whenGuarantorTableRowSelected() {
//        int rowIndexSelected = tblData.getSelectedRow();
//        if (!((Boolean) tblData.getValueAt(rowIndexSelected, 0)).booleanValue()) {
//            if (isSelectedRowTicked(tblGuarantorData)) {
//                ClientUtil.displayAlert("Loanee Record not selected...");
//                observable.setSelectAll(tblGuarantorData, new Boolean(false));
//            }
//        }
//    }
    
    private boolean isSelectedRowTicked(com.see.truetransact.uicomponent.CTable table) {
        boolean selected = false;
        for (int i=0, j=table.getRowCount(); i < j; i++) {
            selected = ((Boolean) table.getValueAt(i, 0)).booleanValue();
            if (!selected) {
                //            table.setForeground(Colu
                break;
            }
        }
        return selected;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        panMultiSearch = new com.see.truetransact.uicomponent.CPanel();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        panToAccount = new com.see.truetransact.uicomponent.CPanel();
        lblToAccount = new com.see.truetransact.uicomponent.CLabel();
        txtAcctNo = new com.see.truetransact.uicomponent.CTextField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDt = new com.see.truetransact.uicomponent.CDateField();
        btnFromAccountNo = new com.see.truetransact.uicomponent.CButton();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtsubsidyAmt = new com.see.truetransact.uicomponent.CTextField();
        panMultiSearch2 = new com.see.truetransact.uicomponent.CPanel();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        lblToDate1 = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecords = new com.see.truetransact.uicomponent.CLabel();
        lblSubsidyId = new com.see.truetransact.uicomponent.CLabel();
        lblTotalTransAmt = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecords2 = new com.see.truetransact.uicomponent.CLabel();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear1 = new com.see.truetransact.uicomponent.CButton();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        tbrTermLoan = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace32 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblspace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace33 = new com.see.truetransact.uicomponent.CLabel();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        lblSpace7 = new com.see.truetransact.uicomponent.CLabel();
        lblSpace9 = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Loan Notices");
        setMinimumSize(new java.awt.Dimension(800, 630));
        setPreferredSize(new java.awt.Dimension(800, 630));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(574, 140));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(574, 140));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        panMultiSearch.setMinimumSize(new java.awt.Dimension(460, 140));
        panMultiSearch.setPreferredSize(new java.awt.Dimension(460, 140));
        panMultiSearch.setLayout(new java.awt.GridBagLayout());

        lblProdId.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 4);
        panMultiSearch.add(lblProdId, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(160);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 9, 2, 4);
        panMultiSearch.add(cboProdId, gridBagConstraints);

        lblProdType.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 11, 2, 4);
        panMultiSearch.add(lblProdType, gridBagConstraints);

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 2, 4);
        panMultiSearch.add(cboProdType, gridBagConstraints);

        panToAccount.setLayout(new java.awt.GridBagLayout());

        lblToAccount.setText("From Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToAccount.add(lblToAccount, gridBagConstraints);

        txtAcctNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcctNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcctNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToAccount.add(txtAcctNo, gridBagConstraints);

        lblToDate.setText("From  Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToAccount.add(lblToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToAccount.add(tdtFromDt, gridBagConstraints);

        btnFromAccountNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromAccountNo.setToolTipText("To Account");
        btnFromAccountNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFromAccountNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromAccountNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panToAccount.add(btnFromAccountNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMultiSearch.add(panToAccount, gridBagConstraints);

        cLabel1.setText("Subsidy Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panMultiSearch.add(cLabel1, gridBagConstraints);

        txtsubsidyAmt.setAllowNumber(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panMultiSearch.add(txtsubsidyAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(panMultiSearch, gridBagConstraints);

        panMultiSearch2.setMaximumSize(new java.awt.Dimension(200, 55));
        panMultiSearch2.setMinimumSize(new java.awt.Dimension(200, 55));
        panMultiSearch2.setPreferredSize(new java.awt.Dimension(200, 55));
        panMultiSearch2.setLayout(new java.awt.GridBagLayout());

        btnProcess.setText("Display");
        btnProcess.setPreferredSize(new java.awt.Dimension(85, 27));
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panMultiSearch2.add(btnProcess, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(panMultiSearch2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(panSearchCondition, gridBagConstraints);

        panTable.setMinimumSize(new java.awt.Dimension(600, 350));
        panTable.setPreferredSize(new java.awt.Dimension(600, 350));
        panTable.setLayout(new java.awt.GridBagLayout());

        srcTable.setViewport(srcTable.getRowHeader());

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblData.setEditingColumn(5);
        tblData.setEditingRow(0);
        tblData.setMinimumSize(new java.awt.Dimension(350, 80));
        tblData.setPreferredScrollableViewportSize(new java.awt.Dimension(450000, 400000));
        tblData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblDataMouseMoved(evt);
            }
        });
        tblData.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblDataFocusLost(evt);
            }
        });
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDataMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblDataMouseReleased(evt);
            }
        });
        srcTable.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(srcTable, gridBagConstraints);

        lblToDate1.setText("Loan Account Holders List");
        lblToDate1.setMaximumSize(new java.awt.Dimension(230, 85));
        lblToDate1.setMinimumSize(new java.awt.Dimension(186, 18));
        lblToDate1.setPreferredSize(new java.awt.Dimension(186, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panTable.add(lblToDate1, gridBagConstraints);

        lblNoOfRecords.setText("Subsidy Id");
        lblNoOfRecords.setMaximumSize(new java.awt.Dimension(230, 85));
        lblNoOfRecords.setMinimumSize(new java.awt.Dimension(130, 18));
        lblNoOfRecords.setPreferredSize(new java.awt.Dimension(130, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTable.add(lblNoOfRecords, gridBagConstraints);

        lblSubsidyId.setMaximumSize(new java.awt.Dimension(230, 85));
        lblSubsidyId.setMinimumSize(new java.awt.Dimension(80, 18));
        lblSubsidyId.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTable.add(lblSubsidyId, gridBagConstraints);

        lblTotalTransAmt.setMaximumSize(new java.awt.Dimension(230, 85));
        lblTotalTransAmt.setMinimumSize(new java.awt.Dimension(130, 18));
        lblTotalTransAmt.setPreferredSize(new java.awt.Dimension(130, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTable.add(lblTotalTransAmt, gridBagConstraints);

        lblNoOfRecords2.setText("Total Transaction Amt");
        lblNoOfRecords2.setMaximumSize(new java.awt.Dimension(230, 85));
        lblNoOfRecords2.setMinimumSize(new java.awt.Dimension(130, 18));
        lblNoOfRecords2.setPreferredSize(new java.awt.Dimension(130, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTable.add(lblNoOfRecords2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panTable, gridBagConstraints);

        panSearch.setMinimumSize(new java.awt.Dimension(150, 35));
        panSearch.setPreferredSize(new java.awt.Dimension(150, 35));
        panSearch.setLayout(new java.awt.GridBagLayout());

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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSearch.add(btnClose, gridBagConstraints);

        btnClear1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear1.setText("Clear");
        btnClear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 25.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClear1, gridBagConstraints);

        cPanel1.setMinimumSize(new java.awt.Dimension(180, 35));
        cPanel1.setPreferredSize(new java.awt.Dimension(180, 35));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblSpace1, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblStatus, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 10.0;
        panSearch.add(cPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panSearch, gridBagConstraints);

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(sptLine, gridBagConstraints);

        tbrTermLoan.setMinimumSize(new java.awt.Dimension(800, 29));
        tbrTermLoan.setPreferredSize(new java.awt.Dimension(800, 29));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(29, 27));
        btnView.setPreferredSize(new java.awt.Dimension(29, 27));
        btnView.setEnabled(false);
        tbrTermLoan.add(btnView);

        lblSpace4.setText("     ");
        tbrTermLoan.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnNew);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace28);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnEdit);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace29);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnDelete);

        lblSpace2.setText("     ");
        tbrTermLoan.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnSave);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace30);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTermLoan.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setPreferredSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setEnabled(false);
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnAuthorize);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace31);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setEnabled(false);
        btnException.setMaximumSize(new java.awt.Dimension(29, 27));
        btnException.setMinimumSize(new java.awt.Dimension(29, 27));
        btnException.setPreferredSize(new java.awt.Dimension(29, 27));
        tbrTermLoan.add(btnException);

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace32);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setEnabled(false);
        btnReject.setMaximumSize(new java.awt.Dimension(29, 27));
        btnReject.setMinimumSize(new java.awt.Dimension(29, 27));
        btnReject.setPreferredSize(new java.awt.Dimension(29, 27));
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnReject);

        lblspace3.setMaximumSize(new java.awt.Dimension(15, 15));
        lblspace3.setMinimumSize(new java.awt.Dimension(15, 15));
        lblspace3.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrTermLoan.add(lblspace3);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrTermLoan.add(btnPrint);

        lblSpace33.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace33.setText("     ");
        lblSpace33.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace33);

        lblSpace6.setText("     ");
        lblSpace6.setMinimumSize(new java.awt.Dimension(100, 18));
        lblSpace6.setPreferredSize(new java.awt.Dimension(100, 18));
        tbrTermLoan.add(lblSpace6);

        lblSpace7.setText("     ");
        tbrTermLoan.add(lblSpace7);

        lblSpace9.setText("     ");
        tbrTermLoan.add(lblSpace9);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(tbrTermLoan, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblDataFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblDataFocusLost
        // TODO add your handling code here:
//        setTableModelListenerUpdate();
    }//GEN-LAST:event_tblDataFocusLost

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
 
        if (tblData.getRowCount() > 0) {
            updateOBFields();
            observable.doAction("");
            if (observable.getResult() == ClientConstants.ACTIONTYPE_FAILED) {
                return;
            } else {
                btnCancelActionPerformed(evt);
            }
            observable.setResultStatus();
        } else {
            ClientUtil.showMessageWindow("Please find Record then Applied Subsidy");
            return;
        }            
            
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        subsidyId="";
        isFilled=false;
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        observable.setSubsidyId("");
        enableDisableSearchDetails(false);
        btnCancelActionPerformed();
        setButtonEnableDisable();
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        observable.setActivateNewRecord(false);
        acccountList=new StringBuffer();
        txtsubsidyAmt.setText("");
        lblSubsidyId.setText("");
        tdtFromDt.setDateValue(null);
    }//GEN-LAST:event_btnCancelActionPerformed
    private void btnCancelActionPerformed(){
        cboProdType.setSelectedItem("");
        observable.setCbmProdId("");
        txtAcctNo.setText("");
        resetinitSubsidyTableData();
        observable.setStatus();
        
    }
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
         observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
         authorizeActionPerformed();
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeActionPerformed();
        
        
    }//GEN-LAST:event_btnAuthorizeActionPerformed
     private void authorizeActionPerformed(){
         
        if (viewType == ClientConstants.ACTIONTYPE_AUTHORIZE && isFilled){
          
            HashMap map=new HashMap();
//            map.put("ACT_NUM",txtAccountNumber.getText());
            map.put("AUTHORIZE_BY",TrueTransactMain.USER_ID);
            map.put("AUTHORIZE_DATE",ClientUtil.getCurrentDate());
            map.put("AUTHORIZE_STATUS","AUTHORIZED");
              if(tblData.getRowCount()>0){
                  updateOBFields();
                   observable.setTdtFromDt(CommonUtil.convertObjToStr(tblData.getValueAt(0,8)));
                   observable.setTxtAcctNum(CommonUtil.convertObjToStr(tblData.getValueAt(0,1)));
                   observable.setTxtSubsidyAmount(CommonUtil.convertObjToStr(tblData.getValueAt(0,7)));
                 observable.setAuthorizeMap(map);
                observable.doAction("");
                  if(observable.getResult() == ClientConstants.ACTIONTYPE_FAILED){
                        return;
                    }else{
                         btnCancelActionPerformed(null);
                    }
                observable.setResultStatus();
               
            }else{
                ClientUtil.showMessageWindow("Please find Record then Applied Subsidy");
                return;
            }
           isFilled=false;
//            observable.doAction("");
//            if (observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
//                observable.resetForm();
//            }
//            btnCancelActionPerformed(null);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
//            observable.setResultStatus();
        }else{
//            HashMap mapParam=new HashMap();
//            HashMap map=new HashMap();
//            mapParam.put(CommonConstants.MAP_NAME,"viewChargesDetailsForAuthorize");
           
//            mapParam.put(CommonConstants.MAP_WHERE,map);
//           
            viewType=ClientConstants.ACTIONTYPE_AUTHORIZE;
             observable.setStatus();
//            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
//            authorizeUI.show();
             popUpEdit(ClientConstants.ACTIONTYPE_AUTHORIZE);
           
            btnSave.setEnabled(false);
            
            btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
            btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
            btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            
        }
    }
    
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        btnSave.setEnabled( btnCancel.isEnabled());
        btnAuthorize.setEnabled( !btnAuthorize.isEnabled());
        btnReject.setEnabled( !btnReject.isEnabled());
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
                enableDisableSearchDetails(false);
            observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
            viewType =ClientConstants.ACTIONTYPE_DELETE;
            popUpEdit(ClientConstants.ACTIONTYPE_DELETE);
            observable.setStatus();
            
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
               enableDisableSearchDetails(false);
            observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
            viewType =ClientConstants.ACTIONTYPE_EDIT;
            popUpEdit(ClientConstants.ACTIONTYPE_EDIT);
            observable.setStatus();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        enableDisableSearchDetails(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        viewType =ClientConstants.ACTIONTYPE_NEW;
        setButtonEnableDisable();
        observable.setStatus();
     
        
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnFromAccountNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromAccountNoActionPerformed
        // TODO add your handling code here:
          popUp(FROM);
    }//GEN-LAST:event_btnFromAccountNoActionPerformed
 private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        HashMap hash = new HashMap();
        String prodType = CommonUtil.convertObjToStr(cboProdType.getSelectedItem());
        if(prodType.equals("Advances")){
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListAD");
            
        }
        else if(prodType.equals("Term Loans")){
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListTL");
           
        }
       
         hash.put("SELECTED_BRANCH", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
        hash.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmProdId().getKeyForSelected()));
        hash.put(CommonConstants.BRANCH_ID, com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
        //        if(viewType==TO){
        //            hash.put("ACCT_NO", txtFromAccount.getText());
        //        }
        viewMap.put(CommonConstants.MAP_WHERE, hash);
        
        new ViewAll(this, viewMap).show();
    }
 
 private void popUpEdit(int field) {
        final HashMap viewMap = new HashMap();
      
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT){
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            whereMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmProdId().getKeyForSelected()));
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put("AUTHORIZE", "AUTHORIZE");
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getEditTermLoanSubsidyDetailsTO");
           
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
        }else{
            viewType = field;
            HashMap hash = new HashMap();
            String prodType = CommonUtil.convertObjToStr(cboProdType.getSelectedItem());
            viewMap.put(CommonConstants.MAP_NAME, "getEditTermLoanSubsidyDetailsTO");
            hash.put("BRANCH_CODE", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            hash.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmProdId().getKeyForSelected()));
            hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, hash);
            
            new ViewAll(this, viewMap).show();
        }
 }
    private void txtAcctNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcctNoFocusLost
        // TODO add your handling code here:
             
    }//GEN-LAST:event_txtAcctNoFocusLost
  

     private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    private void btnClear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear1ActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this);
    }//GEN-LAST:event_btnClear1ActionPerformed

    private void tblDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseReleased
        // TODO add your handling code here:
       
    }//GEN-LAST:event_tblDataMouseReleased
    private void setVisibleFields(boolean flag){
//        lblFromDate.setVisible(flag);
//        lblToDate.setVisible(flag);
//        tdtFromDate.setVisible(flag);
//        tdtToDate.setVisible(flag);
//        lblOverDueDate.setVisible(flag);
//        tdtOverDueDate.setVisible(flag);
//        chkPrized.setVisible(false);
//        chkNonPrized.setVisible(false);
    }
    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        String prodType = String.valueOf(cboProdType.getSelectedItem());
//        if (prodType.length()>0) {
//            if(prodType.equals("Other Loans")){
//                prodType ="Other Loans And Advances";
//            }
//            observable.fillDropDown(prodType);
            observable.setCbmProdId(prodType);
            cboProdId.setModel(observable.getCbmProdId());
//        }
        if(prodType.equals("MDS")){
            setVisibleFields(false);
//            chkLoneeOnly.setText("Chittal Only");
//            chkPrized.setVisible(true);
//            chkNonPrized.setVisible(true);
        }else{
            setVisibleFields(true);
//            chkLoneeOnly.setText("Loanee Only");
        }
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        ///getSelectLoansSubsidyCount
        if (txtAcctNo.getText().length() == 0) {
            ClientUtil.showMessageWindow("Select Account No !!!");
        } else {
            HashMap whereMap = new LinkedHashMap();
            whereMap.put("ACCT_NUM", txtAcctNo.getText());
            List subsidyCntLst = ClientUtil.executeQuery("getSelectLoansSubsidyCount", whereMap);
            if (subsidyCntLst != null && subsidyCntLst.size() > 0) {
                ClientUtil.showMessageWindow("Subsidy already given !!!");
            } else {
                initSubsidyTableData();
                javax.swing.table.TableColumn col = tblData.getColumn(tblData.getColumnName(0));
                col.setMaxWidth(150);
                col.setMinWidth(150);
                col.setWidth(150);
                col.setPreferredWidth(150);
            }
        }
    }//GEN-LAST:event_btnProcessActionPerformed

    private void tblDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseMoved
//	Point p = evt.getPoint();
//        String tip =
//        String.valueOf(
//        tblData.getModel().getValueAt(
//        tblData.rowAtPoint(p),
//        tblData.columnAtPoint(p)));
//        tblData.setToolTipText(tip);
    }//GEN-LAST:event_tblDataMouseMoved

    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
//        if ((evt.getClickCount() == 2) && (evt.getModifiers() == 16)) {
//            HashMap whereMap = new HashMap();
//            whereMap.put("ACT_NUM", tblData.getValueAt(tblData.getSelectedRow(),1));
//            
//            TableDialogUI tableData = new TableDialogUI("getNoticeChargeDetails", whereMap);
//            tableData.setTitle("Notice Sent Details for "+tblData.getValueAt(tblData.getSelectedRow(),1));
//            tableData.setPreferredSize(new Dimension(750, 450));
//            tableData.show();
//          
//        }
    }//GEN-LAST:event_tblDataMousePressed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        observable.removeRowsFromGuarantorTable(tblData);
//        observable.removeRowsFromGuarantorTable(tblGuarantorData);
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    
    public ArrayList  populateData() {
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        ArrayList resultlist=new ArrayList();
        boolean isOK = false;
        if (!CommonUtil.convertObjToStr(cboProdType.getSelectedItem()).equals("")) {
            isOK = true;
        } else {
            isOK = false;
        }        
        
        if (isOK || observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT || observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE
        || observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT) {
            
            String prodType = String.valueOf(cboProdType.getSelectedItem());
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT || observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE
            || observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT){
                viewMap.put(CommonConstants.MAP_NAME, "getSelectTermLoanSubsidyDetailsTO");
                whereMap.put("SUBSIDY_ID", subsidyId);
                if(observable.isActivateNewRecord()){
                    viewMap.put(CommonConstants.MAP_NAME, "getSubsidyProvisionDetails");
                }
                 whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
            viewMap.put(CommonConstants.MAP_NAME, "getSubsidyProvisionDetails");
            }
            if (String.valueOf(cboProdType.getSelectedItem()).length()>0) {
                
                whereMap.put("PROD_TYPE", CommonUtil.convertObjToStr(cboProdType.getSelectedItem()));
            }
            if (String.valueOf(observable.getCbmProdId().getKeyForSelected()).length()>0) {
                whereMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmProdId().getKeyForSelected()));
            }
            if(CommonUtil.convertObjToStr(txtAcctNo.getText()).length()>0){
                whereMap.put("FROM_ACCT_NUM",txtAcctNo.getText());
            }
            if(observable.isActivateNewRecord()==true && acccountList !=null && acccountList.length()>0){
                whereMap.put("ADD_NEW_RECORDS",acccountList);
            }
            whereMap.put("TODAY_DT", currDt);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                log.info("populateData...");
//                observable.removeRowsFromGuarantorTable(tblGuarantorData);
//                ArrayList heading =(ArrayList)observable.populateDataNew(viewMap, tblData);
                 resultlist=(ArrayList)observable.populateDataNew(viewMap, tblData);
              
                
//                heading = null;
            } catch( Exception e ) {
                System.err.println( "Exception " + e.toString() + "Caught" );
                e.printStackTrace();
            }
        }
        viewMap = null;
        whereMap = null;
        return resultlist;
    }
    
   
    
    public void initSubsidyTableData() {
        tblData.setModel(new javax.swing.table.DefaultTableModel(
        setTableData(),
        new String [] {
            "Acct Num","Customer Id","Share No","Name","Loan Amount","Available Bal","Subsidy Amt","Subsidy Date"
        }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
               
            };
            
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false,false, false,false
            };
           
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        
        
        tblData.setCellSelectionEnabled(true);
       
        setTableModelListener();
        setSizeTallyTableData();
    }
    
    
    public void initSubsidyAuthTableData() {
        tblData.setModel(new javax.swing.table.DefaultTableModel(
        setAuthTableData(),
        new String [] {
          "Subsidy Id", "Acct Num","Customer Id","Share No","Name","Loan Amount","Available Bal","Subsidy Amt","Subsidy Date"
        }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
               
            };
            
            boolean[] canEdit = new boolean [] {
                false,false, false, false, false, false,false, false,false
            };
           
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        
        
        tblData.setCellSelectionEnabled(true);
       
        setTableModelListener();
        setSizeTallyTableData();
    }
    
    public void resetinitSubsidyTableData() {
        Object obj[][]=new Object[0][0];
        tblData.setModel(new javax.swing.table.DefaultTableModel(
        obj,
        new String [] {
            "Select","Acct Num","Name","Subsidy Adjust Achd","Subsidy Amt","Trans Amt","Subsidy Date"
        }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
                
            };
            
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false,transAmtEdit, false
            };
            
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex==5 && transAmtEdit){
                    return true;
                }
                
                return canEdit [columnIndex];
            }
        });
        
        
        tblData.setCellSelectionEnabled(true);
        tblData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblDataPropertyChange(evt);
            }
        });
        
        tblData.addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent me) {
        int selRow = tblData.getSelectedRow();
        int selCol = tblData.getSelectedColumn();
        Object value = tblData.getValueAt(selRow, selCol);
        }
        });
        setTableModelListener();
//        setTableModelListenerUpdate();
        setSizeTallyTableData();
       
    }
    
    private void tblDataPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblRecoveryListTallyPropertyChange
        // TODO add your handling code here:
       
//        if(tblData.getSelectedRow()>-1){
////            ((DefaultTableModel)tblData.getModel()).fireTableCellUpdated(tblData.getSelectedRow(), 5);
//            setTableModelListenerUpdate();
//            String amount=CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 5));
////        double transAmt=CommonUtil.convertObjToDouble(((DefaultTableModel)tblData.getModel()).getValueAt( tblData.getSelectedRow(), 5)).doubleValue();
////
////        ((DefaultTableModel)tblData.getModel()).setValueAt(String.valueOf(transAmt), tblData.getSelectedRow(), 5);
//        }
    }//GEN-LAST:event_tblRecoveryListTallyPropertyChange
    
    
     private void setTableModelListenerUpdate() {
        tableModelListener = new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    System.out.println("Cell " + e.getFirstRow() + ", "
                    + e.getColumn() + " changed. The new value: "
                    + tblData.getModel().getValueAt(e.getFirstRow(),
                    e.getColumn()));
                    int row = e.getFirstRow();
                    int column  = e.getColumn();
                    if (column == 5) {
                        TableModel model = tblData.getModel();
//                        tblData.setValueAt(model.getValueAt(tblData.getSelectedRow(),5),tblData.getSelectedRow(),5);
                        System.out.println("tblData getvalueate"+tblData.getValueAt(tblData.getSelectedRow(),5)+" e.getColumn()"+ e.getColumn());
//                        calcEachChittal();
//                        calcTotal();
                    }
                }
            }
        };
        tblData.getModel().addTableModelListener(tableModelListener);
    }   
     
     
    private Object[][] setTableData() {
        LinkedHashMap whereMap = new LinkedHashMap();
        whereMap.put("SELECTED_BRANCH", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
        whereMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmProdId().getKeyForSelected()));
        whereMap.put("ACCT_NUM",txtAcctNo.getText());
        List applnList = ClientUtil.executeQuery("getLoanDetailsForSubsidyProvision", whereMap);
        System.out.println("list1... :: " + applnList);
        HashMap processMap = new HashMap();
        if (applnList != null && applnList.size() > 0) {
            Object totalList[][] = new Object[applnList.size()][8];
            for (int j = 0; j < applnList.size(); j++) {                
                //"Acct Num","Customer Id","Share No","NAME","Loan Amount","Available Bal","Subsidy Amt","Subsidy Date"
                processMap = (HashMap) applnList.get(j);                
                totalList[j][0] = CommonUtil.convertObjToStr(processMap.get("ACCT_NUM"));
                totalList[j][1] = CommonUtil.convertObjToStr(processMap.get("CUST_ID"));
                totalList[j][2] = CommonUtil.convertObjToStr(processMap.get("MEMBER_NO"));
                totalList[j][3] = CommonUtil.convertObjToStr(processMap.get("Name"));
                totalList[j][4] = CommonUtil.convertObjToStr(processMap.get("LIMIT"));
                totalList[j][5] = CommonUtil.convertObjToStr(processMap.get("LOAN_BALANCE_PRINCIPAL"));
                totalList[j][6] = CommonUtil.convertObjToStr(txtsubsidyAmt.getText());
                totalList[j][7] = CommonUtil.convertObjToStr(tdtFromDt.getDateValue());
                
            }
            System.out.println("Populate complete");
            return totalList;
        } else {
            ClientUtil.displayAlert("No Data!!! ");
        }
        return null;
    }
 
      private Object[][] setAuthTableData() {
        LinkedHashMap whereMap = new LinkedHashMap();
        whereMap.put("SELECTED_BRANCH", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
        whereMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmProdId().getKeyForSelected()));
        whereMap.put("ACCT_NUM",txtAcctNo.getText());
        whereMap.put("SUBSIDY_ID",lblSubsidyId.getText());
        List applnList = ClientUtil.executeQuery("getAuthDetailsForSubsidyProvision", whereMap);
        System.out.println("list1... :: " + applnList);
        HashMap processMap = new HashMap();
        if (applnList != null && applnList.size() > 0) {
            Object totalList[][] = new Object[applnList.size()][9];
            for (int j = 0; j < applnList.size(); j++) {                
                //"Acct Num","Customer Id","Share No","NAME","Loan Amount","Available Bal","Subsidy Amt","Subsidy Date"
                processMap = (HashMap) applnList.get(j);   
                totalList[j][0] = CommonUtil.convertObjToStr(subsidyId);
                totalList[j][1] = CommonUtil.convertObjToStr(processMap.get("ACCT_NUM"));
                totalList[j][2] = CommonUtil.convertObjToStr(processMap.get("CUST_ID"));
                totalList[j][3] = CommonUtil.convertObjToStr(processMap.get("MEMBER_NO"));
                totalList[j][4] = CommonUtil.convertObjToStr(processMap.get("Name"));
                totalList[j][5] = CommonUtil.convertObjToStr(processMap.get("LIMIT"));
                totalList[j][6] = CommonUtil.convertObjToStr(processMap.get("LOAN_BALANCE_PRINCIPAL"));
                totalList[j][7] = CommonUtil.convertObjToStr(processMap.get("SUBSIDY_AMT"));
                totalList[j][8] = CommonUtil.convertObjToStr(processMap.get("SUBSIDY_ADJUST_DT"));                
            }
            System.out.println("Populate complete");
            return totalList;
        } else {
            ClientUtil.displayAlert("No Data!!! ");
        }
        return null;
    }
     
     
    
    private void setTableModelListener() {
        tableModelListener = new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    //                    System.out.println("Cell " + e.getFirstRow() + ", "
                    //                    + e.getColumn() + " changed. The new value: "
                    //                    + tblRecoveryListTally.getModel().getValueAt(e.getFirstRow(),
                    //                    e.getColumn()));
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    if (column == 5) {
                        if(CommonUtil.convertObjToStr(tblData.getValueAt(row, 5)) != null && !CommonUtil.convertObjToStr(tblData.getValueAt(row, 5)).equals("") && !isNumeric(CommonUtil.convertObjToStr(tblData.getModel().getValueAt(e.getFirstRow(),column)))){
                            ClientUtil.showAlertWindow("Please enter Numeric Value !!!");
                            tblData.getModel().setValueAt("", e.getFirstRow(), column);
                            return;    
                        }
                     }
                    if (column == 5) {                        
                        if(CommonUtil.convertObjToDouble(tblData.getModel().getValueAt(e.getFirstRow(),e.getColumn()))>0){
                            double demand_Amount =CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 4).toString()).doubleValue();
                            double recovered_Amount =CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 5).toString()).doubleValue();
                            if(demand_Amount<recovered_Amount && recovered_Amount>0){
                                ClientUtil.showMessageWindow("Transaction Amount should not Cross Subsidy Amount !!!");
                                tblData.setValueAt(tblData.getValueAt(tblData.getSelectedRow(), 4),  tblData.getSelectedRow(), 5);
                            }else{
                                String scheme_Name = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(),5));
                            }
                        }
                       // }
//                        TableModel model = tblData.getModel();
                        System.out.println("tblData getvalueate"+tblData.getValueAt(tblData.getSelectedRow(),5)+" e.getColumn()"+ e.getColumn());
                        //                        calcTallyListTotal();
                    }
                }
            }
        };
        tblData.getModel().addTableModelListener(tableModelListener);
    }
    
    private void setSizeTallyTableData(){
        //        if(tblData.getRowCount()>0){
        //            tblData.getColumnModel().getColumn(0).setPreferredWidth(140);
        //            tblData.getColumnModel().getColumn(1).setPreferredWidth(70);
        //            tblData.getColumnModel().getColumn(2).setPreferredWidth(30);
        //            tblData.getColumnModel().getColumn(3).setPreferredWidth(30);
        //            tblData.getColumnModel().getColumn(4).setPreferredWidth(40);
        //            tblData.getColumnModel().getColumn(5).setPreferredWidth(30);
        //            tblData.getColumnModel().getColumn(6).setPreferredWidth(70);
        //            tblData.getColumnModel().getColumn(7).setPreferredWidth(90);
        //        }
    }
    
    public static boolean isNumeric(String str) {
        try {
            //Integer.parseInt(str);
            Float.parseFloat(str);
            //   System.out.println("ddd"+d);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    public boolean applySubsidy() {
        //        updateOBFields();
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        String param1 = observable.getSelected(tblData);
        String prodType = String.valueOf(cboProdType.getSelectedItem());
        if (param1.length()>0) {
            observable.setTotListMap(new HashMap());
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null,"Are you sure?", CommonConstants.WARNINGTITLE,
            COptionPane.YES_NO_OPTION, COptionPane.QUESTION_MESSAGE,
            null, options, options[0]);
            System.out.println("#$#$$ yesNo : "+yesNo);
            if (yesNo==0) {
                boolean selected = false;
                accountNumberMap = new HashMap();
                guarantorMemberMap = new HashMap();
                accountChargeMap = new HashMap();
                java.util.Map guarantorMap = observable.getGuarantorMap();
                ArrayList totalList = null;
                ArrayList rowList = null;
                String actNum;
                ArrayList tempList = null;
                //                observable.getTableModel(tblData);
                //                for (int i=0, j=tblData.getRowCount(); i < j; i++) {
                //                    selected = ((Boolean) tblData.getValueAt(i, 0)).booleanValue();
                //                    if (selected) {
                tempList = new ArrayList();
                
                //                        tempList.add((ArrayList)observable.getTableModel(tblData).get(i));
                //                         tempList.add((ArrayList)observable.getTableModel(tblData));
                tempList=(ArrayList)observable.getTableModel(tblData);
                //                        if(prodType.equals("MDS")){
                //                            actNum = String.valueOf(tblData.getValueAt(i, 2));
                //                        }else{
                //                            actNum = String.valueOf(tblData.getValueAt(i, 1));
                //                        }
                
                //                        accountNumberMap.put(actNum,null);
                //                        if (guarantorMap!=null && guarantorMap.size()>0) {
                //                            totalList = (ArrayList) guarantorMap.get(actNum);
                //                            if (totalList!=null && totalList.size()>0) {
                //                                for (int g=0; g<totalList.size(); g++) {
                //                                    rowList = (ArrayList) totalList.get(g);
                //                                    selected = ((Boolean) rowList.get(0)).booleanValue();
                //                                    if (selected) {
                //                                        tempList.add(rowList);
                //                                        guarantorMemberMap.put(actNum+rowList.get(3),null);
                //                                    }
                //                                }
                //                            }
                //                        }
                //                        accountChargeMap.put(actNum,tempList);
                observable.getTotalList(tempList);
                //                    }
                //                }
                ttIntegration = null;
                HashMap paramMap = new HashMap();
                
                System.out.println("guarantorMemberMap###"+guarantorMemberMap+"tempList####"+tempList);
                paramMap.put("BranchCode", ProxyParameters.BRANCH_ID);
                paramMap.put("ProductId", observable.getCbmProdId().getKeyForSelected());
                paramMap.put("Param1", param1);
                paramMap.put("Param2", guarantorGetSelected());
                paramMap.put("OverDueDt", currDt);
                //                ttIntegration.setParam(paramMap);
                //                observable
                
            }else
                return true;
            
        } else {
            generateNotice = false;
            ClientUtil.displayAlert("No Records found...");
        }
        viewMap = null;
        whereMap = null;
        return false;
    }
    
    public String guarantorGetSelected() {
        //        Boolean bln;
        //        ArrayList arrRow;
        //        HashMap selectedMap;
        //        ArrayList selectedList = new ArrayList();
        String selected="";
        Object obj[] = guarantorMemberMap.keySet().toArray();
        for (int i=0, j=obj.length; i < j; i++) {
            selected+="'"+obj[i];
            selected+="',";
        }
        // If no guarantor selected also records should be selected from other than guarantor.
        selected = selected.equals("") ? "'aa'" : selected.substring(0,selected.length()-1);
        System.out.println("#$#$ guaranter selected : "+selected);
        return selected;
    }
    
    private void createCboProdType() {
        //        if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("MAHILA")!=-1) {
        //            cboProdType.addItem("");
        //            cboProdType.addItem("Advances");
        //            cboProdType.addItem("Term Loans");
        //        } else {
        cboProdType.addItem("");
        cboProdType.addItem("Advances");
        cboProdType.addItem("Term Loans");
        //        }
    }
    
    private void createCboNoticeType() {
        //        if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("MAHILA")!=-1) {
        //            cboNoticeType.addItem("");
        //            cboNoticeType.addItem("First Notice");
        //            cboNoticeType.addItem("Second Notice");
        //            cboNoticeType.addItem("Third Notice");
        //        } else {
        //            cboNoticeType.addItem("");
        //            cboNoticeType.addItem("Ordinary Notice");
        //            cboNoticeType.addItem("Registered Notice");
        //            cboNoticeType.addItem("Auction Notice");
        //        }
    }
    
    private Date getProperDate(Date sourceDate) {
        Date targetDate = (Date) currDt.clone();
        targetDate.setDate(sourceDate.getDate());
        targetDate.setMonth(sourceDate.getMonth());
        targetDate.setYear(sourceDate.getYear());
        return targetDate;
    }
    
    private ComboBoxModel getListModel() {
        ComboBoxModel listData = new ComboBoxModel();
        return listData;
    }
    
    
    
    
    private void updateDBStatus(String status) {
        //observable.updateStatus(paramMap, status);
        HashMap screenParamMap = new HashMap();
        screenParamMap.put(CommonConstants.AUTHORIZEDATA, observable.getSelected(tblData));
        screenParamMap.put(CommonConstants.AUTHORIZESTATUS, status);
        
        //        observable.insertCharges(paramMap);
//        ClientUtil.showMessageWindow(observable.getResult());
//        if (observable.getResult().equals(ClientConstants.RESULT_STATUS[4])) {
//            observable.removeRowsFromGuarantorTable(tblData);
//            //            observable.removeRowsFromGuarantorTable(tblGuarantorData);
//        }
        
    }
    
    private void setColour() {
        /* Set a cellrenderer to this table in order format the date */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected,hasFocus, row, column);
                System.out.println("row #####"+row);
                boolean selected = ((Boolean) table.getValueAt(row, 0)).booleanValue();
                if (!selected) {
                    setForeground(Color.RED);
                }
                else {
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblData.setDefaultRenderer(Object.class, renderer);
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
        whereMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        whereMap.put("OUTWARD_DT", ClientUtil.getCurrentDate());
        mapParam.put(CommonConstants.MAP_NAME, "getSelectOutwardClearingRealizeTOList");
        mapParam.put(CommonConstants.MAP_WHERE, whereMap);
        
        mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeOperativeAcctProduct");
        
        //        AuthorizeUI authorizeUI = new AuthorizeUI(mapParam);
        //        authorizeUI.setAuthorize(true);
        //        authorizeUI.setException(false);
        //        authorizeUI.setReject(false);
        //        authorizeUI.setRealize(true);
        //        authorizeUI.show();
    }
    
    public void update(Observable o, Object arg) {
        lblStatus.setText(observable.getLblStatus());
        
    }
    public void updateOBFields() {
        observable.setLblTotalTransAmt(lblTotalTransAmt.getText());
        observable.setTdtFromDt(tdtFromDt.getDateValue());
        observable.setTxtAcctNum(txtAcctNo.getText());
        observable.setTxtSubsidyAmount(txtsubsidyAmt.getText());
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear1;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnFromAccountNo;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecords;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecords2;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
    private com.see.truetransact.uicomponent.CLabel lblSpace33;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace7;
    private com.see.truetransact.uicomponent.CLabel lblSpace9;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSubsidyId;
    private com.see.truetransact.uicomponent.CLabel lblToAccount;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblToDate1;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransAmt;
    private com.see.truetransact.uicomponent.CLabel lblspace3;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch2;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CPanel panToAccount;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private javax.swing.JToolBar tbrTermLoan;
    private com.see.truetransact.uicomponent.CDateField tdtFromDt;
    private com.see.truetransact.uicomponent.CTextField txtAcctNo;
    private com.see.truetransact.uicomponent.CTextField txtsubsidyAmt;
    // End of variables declaration//GEN-END:variables
}

