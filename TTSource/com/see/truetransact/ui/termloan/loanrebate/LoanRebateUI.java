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
package com.see.truetransact.ui.termloan.loanrebate;

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import javax.swing.table.*;
import javax.swing.*;
import javax.swing.event.*;
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
import com.see.truetransact.transferobject.termloan.loanrebate.LoanRebateTO;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.transaction.cash.CashTransactionUI;

/**
 * @author  bala
 */
public class LoanRebateUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {

    private final LoanRebateRB resourceBundle = new LoanRebateRB();
    private TableModelListener tableModelListener;
    private LoanRebateOB observable;
    HashMap paramMap = null;
    CInternalFrame parent = null;
    javax.swing.JList lstSearch;
    java.util.ArrayList arrLst = new java.util.ArrayList();
    Date currDt = null;
    TTIntegration ttIntegration = null;
    int previousRow = -1;
    HashMap accountNumberMap = null;
    HashMap guarantorMemberMap = null;
    HashMap accountChargeMap = null;
    HashMap guarantorChargeMap = null;
    String bankName = "";
    String rebatePeriod = "";
    int finDD;
    int finMM;
    boolean generateNotice = false;
    final int TO = 0, FROM = 1;
    int viewType1 = -1;
    int viewType;
    private String subsidyId = "";
    boolean isFilled = false;
    boolean transAmtEdit = false;
    private StringBuffer acccountList = new StringBuffer();
    private final static Logger log = Logger.getLogger(LoanRebateUI.class);

    /** Creates new form AuthorizeUI */
    public LoanRebateUI() {
        setupInit();
        setupScreen();
    }

    /** Creates new form AuthorizeUI */
    public LoanRebateUI(CInternalFrame parent, HashMap paramMap) {
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
        btnNewRecord.setEnabled(false);
        lblRebateDate.setVisible(true);
        tdtFromDate.setVisible(true);
        lblNoOfRecords2.setVisible(false);
        btnView.setEnabled(true);
    }

    private void enableDisableSearchDetails(boolean flag) {
        ClientUtil.enableDisable(panMultiSearch, flag);
        btnProcess.setEnabled(flag);
        btnFromAccountNo.setEnabled(flag);
        btnToAccountNo.setEnabled(flag);
    }

    private void setMaxLength() {
        txtFromAccountNo.setAllowAll(true);
        txtToAccountNo.setAllowAll(true);

    }

    private void setupScreen() {
        //        setModal(true);

        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    private void setObservable() {
        try {
            observable = new LoanRebateOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initLoanMasterData(HashMap hmap) {
        cboProdType.setSelectedItem(hmap.get("PROD_TYPE"));
        observable.setCbmProdId(CommonUtil.convertObjToStr(hmap.get("PROD_TYPE")));
        hmap.get("PROD_ID");
        //       String prd= CommonUtil.convertObjToStr((ComboBoxModel)observable.getCbmProdId().getKeyForSelected());
        String prd = CommonUtil.convertObjToStr(observable.getCbmProdId().getDataForKey(hmap.get("PROD_ID")));
        cboProdId.setSelectedItem(prd);

        txtFromAccountNo.setText(CommonUtil.convertObjToStr(hmap.get("FROM_ACC_NO")));
        txtToAccountNo.setText(CommonUtil.convertObjToStr(hmap.get("TO_ACC_NO")));
        tdtFromDate.setDateValue(CommonUtil.convertObjToStr(hmap.get("REBATE_UPTO")));
    }

    public void fillData(Object obj) {
        HashMap dataMap = (HashMap) obj;
        System.out.println("obj is" + obj);
        if (viewType == ClientConstants.ACTIONTYPE_DELETE || viewType == ClientConstants.ACTIONTYPE_EDIT || viewType == ClientConstants.ACTIONTYPE_AUTHORIZE || viewType == ClientConstants.ACTIONTYPE_REJECT || viewType == ClientConstants.ACTIONTYPE_VIEW) {
            //            subsidyId="";
            observable.setRebateID(CommonUtil.convertObjToStr(dataMap.get("REBATE_ID")));
            observable.setAccNo(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
            observable.setRebateUpto(CommonUtil.convertObjToStr(dataMap.get("REBATE_UPTO")));
            //            subsidyId=CommonUtil.convertObjToStr(dataMap.get("SUBSIDY_ID"));
            if (viewType == ClientConstants.ACTIONTYPE_AUTHORIZE || viewType == ClientConstants.ACTIONTYPE_REJECT) {
                isFilled = true;
            }
            observable.setSubsidyId(subsidyId);
            initLoanMasterData(dataMap);
            if (viewType == ClientConstants.ACTIONTYPE_EDIT) {
                initLoanRebateTableData(dataMap);
            } else {
                initLoanRebateTableDataWithoutSelect(dataMap);
            }
            editDeleteTableData(dataMap);
            setButtonEnableDisable();
        } else {
            if (viewType1 == FROM) {
                if (dataMap.containsKey("ACCOUNTNO")) {
                    txtFromAccountNo.setText(CommonUtil.convertObjToStr(dataMap.get("ACCOUNTNO")));
                } else {
                    txtFromAccountNo.setText(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
                }
            } else if (viewType1 == TO) {
                if (dataMap.containsKey("ACCOUNTNO")) {
                    txtToAccountNo.setText(CommonUtil.convertObjToStr(dataMap.get("ACCOUNTNO")));
                } else {
                    txtToAccountNo.setText(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
                }

            }
        }
    }

    private void editDeleteTableData(HashMap map) {
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            transAmtEdit = false;
        } else {
            transAmtEdit = true;
        }

        // initSubsidyTableData();
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
        if (viewType == ClientConstants.ACTIONTYPE_EDIT || viewType == ClientConstants.ACTIONTYPE_NEW) {
            setColour();
        }

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
        for (int i = 0, j = table.getRowCount(); i < j; i++) {
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
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        panToAccount = new com.see.truetransact.uicomponent.CPanel();
        lblFromAccount = new com.see.truetransact.uicomponent.CLabel();
        txtFromAccountNo = new com.see.truetransact.uicomponent.CTextField();
        lblRebateDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        btnFromAccountNo = new com.see.truetransact.uicomponent.CButton();
        panToAccount1 = new com.see.truetransact.uicomponent.CPanel();
        lblToAccount = new com.see.truetransact.uicomponent.CLabel();
        txtToAccountNo = new com.see.truetransact.uicomponent.CTextField();
        btnToAccountNo = new com.see.truetransact.uicomponent.CButton();
        panMultiSearch2 = new com.see.truetransact.uicomponent.CPanel();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        lblToDate1 = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecords = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecordsVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotalTransAmt = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecords2 = new com.see.truetransact.uicomponent.CLabel();
        btnNewRecord = new com.see.truetransact.uicomponent.CButton();
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
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace57 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace58 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace59 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace60 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblspace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace61 = new com.see.truetransact.uicomponent.CLabel();
        btnClose1 = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        lblPanNumber1 = new com.see.truetransact.uicomponent.CLabel();
        lblSpace7 = new com.see.truetransact.uicomponent.CLabel();
        txtEditTermLoanNo = new com.see.truetransact.uicomponent.CTextField();
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

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 4);
        panMultiSearch.add(lblProdType, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(160);
        cboProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 9, 2, 4);
        panMultiSearch.add(cboProdId, gridBagConstraints);

        lblProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 11, 2, 4);
        panMultiSearch.add(lblProdId, gridBagConstraints);

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

        lblFromAccount.setText("From Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToAccount.add(lblFromAccount, gridBagConstraints);

        txtFromAccountNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromAccountNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromAccountNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToAccount.add(txtFromAccountNo, gridBagConstraints);

        lblRebateDate.setText("RebateUpto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToAccount.add(lblRebateDate, gridBagConstraints);

        tdtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToAccount.add(tdtFromDate, gridBagConstraints);

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

        panToAccount1.setLayout(new java.awt.GridBagLayout());

        lblToAccount.setText("To Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToAccount1.add(lblToAccount, gridBagConstraints);

        txtToAccountNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToAccountNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToAccountNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToAccount1.add(txtToAccountNo, gridBagConstraints);

        btnToAccountNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToAccountNo.setToolTipText("To Account");
        btnToAccountNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnToAccountNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToAccountNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panToAccount1.add(btnToAccountNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMultiSearch.add(panToAccount1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(panMultiSearch, gridBagConstraints);

        panMultiSearch2.setMaximumSize(new java.awt.Dimension(200, 55));
        panMultiSearch2.setMinimumSize(new java.awt.Dimension(200, 35));
        panMultiSearch2.setPreferredSize(new java.awt.Dimension(200, 35));
        panMultiSearch2.setLayout(new java.awt.GridBagLayout());

        btnProcess.setText("Search");
        btnProcess.setPreferredSize(new java.awt.Dimension(100, 27));
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
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

        chkSelectAll.setText("Select All");
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTable.add(chkSelectAll, gridBagConstraints);

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
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDataMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblDataMouseReleased(evt);
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

        lblNoOfRecords.setText("No. of Records Found");
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

        lblNoOfRecordsVal.setMaximumSize(new java.awt.Dimension(230, 85));
        lblNoOfRecordsVal.setMinimumSize(new java.awt.Dimension(80, 18));
        lblNoOfRecordsVal.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTable.add(lblNoOfRecordsVal, gridBagConstraints);

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

        btnNewRecord.setText("Add New Record");
        btnNewRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewRecordActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 25);
        panTable.add(btnNewRecord, gridBagConstraints);

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

        tbrTermLoan.setMinimumSize(new java.awt.Dimension(895, 29));
        tbrTermLoan.setPreferredSize(new java.awt.Dimension(895, 29));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(29, 27));
        btnView.setPreferredSize(new java.awt.Dimension(29, 27));
        btnView.setEnabled(false);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
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

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace56);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnEdit);

        lblSpace57.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace57.setText("     ");
        lblSpace57.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace57);

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

        lblSpace58.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace58.setText("     ");
        lblSpace58.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace58.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace58.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace58);

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
        btnAuthorize.setEnabled(false);
        btnAuthorize.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setPreferredSize(new java.awt.Dimension(29, 27));
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnAuthorize);

        lblSpace59.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace59.setText("     ");
        lblSpace59.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace59.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace59.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace59);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setEnabled(false);
        btnException.setMaximumSize(new java.awt.Dimension(29, 27));
        btnException.setMinimumSize(new java.awt.Dimension(29, 27));
        btnException.setPreferredSize(new java.awt.Dimension(29, 27));
        tbrTermLoan.add(btnException);

        lblSpace60.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace60.setText("     ");
        lblSpace60.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace60.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace60.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace60);

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

        lblSpace61.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace61.setText("     ");
        lblSpace61.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace61.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace61.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace61);

        btnClose1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose1.setToolTipText("Close");
        tbrTermLoan.add(btnClose1);

        lblSpace6.setText("     ");
        lblSpace6.setMinimumSize(new java.awt.Dimension(100, 18));
        lblSpace6.setPreferredSize(new java.awt.Dimension(100, 18));
        tbrTermLoan.add(lblSpace6);

        lblPanNumber1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPanNumber1.setText("Account No");
        lblPanNumber1.setMinimumSize(new java.awt.Dimension(72, 16));
        tbrTermLoan.add(lblPanNumber1);

        lblSpace7.setText("     ");
        tbrTermLoan.add(lblSpace7);

        txtEditTermLoanNo.setMinimumSize(new java.awt.Dimension(100, 18));
        txtEditTermLoanNo.setPreferredSize(new java.awt.Dimension(100, 18));
        tbrTermLoan.add(txtEditTermLoanNo);

        lblSpace9.setText("     ");
        tbrTermLoan.add(lblSpace9);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(tbrTermLoan, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        popUpEdit(ClientConstants.ACTIONTYPE_VIEW);
        btnSave.setEnabled(false);
    }//GEN-LAST:event_btnViewActionPerformed

    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost
        // TODO add your handling code here:
        Date date = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFromDate.getDateValue()));

        if (date != null) {
            Date dt = getProperDate(date);
            if (DateUtil.dateDiff(dt, currDt) < 0) {

                ClientUtil.displayAlert("Rebate date should be less than or equal to current date");
                tdtFromDate.setDateValue("");
            }
        }
    }//GEN-LAST:event_tdtFromDateFocusLost

    private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed

        // TODO add your handling code here:
        HashMap hmap = new HashMap();
        String prod_id = CommonUtil.convertObjToStr(observable.getCbmProdId().getKeyForSelected());
        hmap.put("PROD_ID", prod_id);
        List list = ClientUtil.executeQuery("getRebateAllowedForProd", hmap);
        hmap = null;
        if (list != null && list.size() > 0) {
            hmap = (HashMap) list.get(0);
            String rebateCalculation = CommonUtil.convertObjToStr(hmap.get("REBATE_CALCULATION"));
            rebatePeriod = CommonUtil.convertObjToStr(hmap.get("REBATE_PERIOD"));
            finDD = CommonUtil.convertObjToInt(hmap.get("FIN_YEAR_START_DD"));
            finMM = CommonUtil.convertObjToInt(hmap.get("FIN_YEAR_START_MM"));
            String rebateAllowed = CommonUtil.convertObjToStr(hmap.get("REBATE_ALLOWED"));
            String rebatePercentage = CommonUtil.convertObjToStr(hmap.get("REBATE_PERCENTAGE"));
            String Y = "Y";
            String ycal = "Yearly calculation";
            if (rebateAllowed.equals(Y)) {
                if (!rebateCalculation.equals(ycal)) {
                    ClientUtil.displayAlert("Rebate calculation at the time of repayment");
                    cboProdId.setSelectedItem("");
                    return;
                }
            } else {
                ClientUtil.displayAlert("No rebate allowed under this product id");
                cboProdId.setSelectedItem("");
                return;
            }
        }
    }//GEN-LAST:event_cboProdIdActionPerformed

    private void btnNewRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewRecordActionPerformed
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            enableDisableSearchDetails(true);
            observable.setActivateNewRecord(true);
        }

    }//GEN-LAST:event_btnNewRecordActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:

        if (tblData.getRowCount() > 0) {
            //               lblTotalTransAmt.setText(String.valueOf(observable.totSubsidyAmt((DefaultTableModel)tblData.getModel())));
            updateOBFields();
            //            if(applySubsidy()){
            //                return;
            //            }

            doAction("");
            if (observable.getResult() == ClientConstants.ACTIONTYPE_FAILED) {
                return;
            } else {
                btnCancelActionPerformed(evt);
            }
            observable.setResultStatus();
            //                btnCancelActionPerformed(evt);
        } else {
            ClientUtil.showMessageWindow("Please find Record then Applied Subsidy");
            return;
        }


    }//GEN-LAST:event_btnSaveActionPerformed

    public void doAction(String actionType) {
        try {
            ArrayList alist = alist = new ArrayList();
            HashMap finalMap = null;
            String command = observable.getCommand();
            for (int i = 0; i < tblData.getRowCount(); i++) {
                String select = CommonUtil.convertObjToStr(tblData.getValueAt(i, 0));
                System.out.println("select######" + select);
                finalMap = new HashMap();
                if (viewType == ClientConstants.ACTIONTYPE_EDIT) {
                    finalMap.put("SELECT", tblData.getValueAt(i, 0));
                    finalMap.put("ACCT_NUM", tblData.getValueAt(i, 1));
                    finalMap.put("ACCT_NAME", tblData.getValueAt(i, 2));
                    finalMap.put("REBATE_INTEREST", tblData.getValueAt(i, 3));
                    finalMap.put("INTEREST_AMOUNT", tblData.getValueAt(i, 4));
                    finalMap.put("COMMAND", observable.getCommand());
                    finalMap.put("REBATE_UPTO", tdtFromDate.getDateValue());
                    alist.add(finalMap);
                } else if (viewType == ClientConstants.ACTIONTYPE_NEW) {
                    if (select.equals("true")) {
                        finalMap.put("ACCT_NUM", tblData.getValueAt(i, 1));
                        finalMap.put("ACCT_NAME", tblData.getValueAt(i, 2));
                        finalMap.put("REBATE_INTEREST", tblData.getValueAt(i, 3));
                        finalMap.put("INTEREST_AMOUNT", tblData.getValueAt(i, 4));
                        finalMap.put("COMMAND", observable.getCommand());
                        finalMap.put("REBATE_UPTO", tdtFromDate.getDateValue());
                        alist.add(finalMap);
                    }
                } else {
                    finalMap.put("ACCT_NUM", tblData.getValueAt(i, 0));
                    finalMap.put("ACCT_NAME", tblData.getValueAt(i, 1));
                    finalMap.put("REBATE_INTEREST", tblData.getValueAt(i, 2));
                    finalMap.put("INTEREST_AMOUNT", tblData.getValueAt(i, 3));
                    finalMap.put("COMMAND", observable.getCommand());
                    finalMap.put("REBATE_UPTO", tdtFromDate.getDateValue());
                    if (observable.checkClosedLoan(CommonUtil.convertObjToStr(tblData.getValueAt(i, 0)))) {
                        finalMap.put("CLOSED_LOAN", "CLOSED_LOAN");
                    }
                    alist.add(finalMap);
                }
            }
            observable.doActionPerformed(alist);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        subsidyId = "";
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        observable.setSubsidyId("");
        enableDisableSearchDetails(false);
        btnCancelActionPerformed();
        setButtonEnableDisable();
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        observable.setActivateNewRecord(false);
        btnNewRecord.setEnabled(false);
        acccountList = new StringBuffer();
        btnView.setEnabled(true);
        isFilled = false;

        //        tblData.set


    }//GEN-LAST:event_btnCancelActionPerformed
    private void btnCancelActionPerformed() {
        cboProdType.setSelectedItem("");
        observable.setCbmProdId("");
        txtFromAccountNo.setText("");
        txtToAccountNo.setText("");
        observable.setCboProdId("");
        observable.setFromAccNo("");
        observable.setToAccNo("");
        observable.setRebateUpto("");
        resetinitSubsidyTableData();
        observable.setStatus();
        tdtFromDate.setDateValue("");

    }
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        rejectActionPerformed();
        btnView.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void rejectActionPerformed() {
        if (viewType == ClientConstants.ACTIONTYPE_AUTHORIZE && isFilled) {

            HashMap map = new HashMap();
            map.put("AUTHORIZE_BY", TrueTransactMain.USER_ID);
            map.put("AUTHORIZE_DT", ClientUtil.getCurrentDate());
            map.put("AUTHORIZE_STATUS", "REJECTED");
            map.put("REBATE_ID", observable.getRebateID());
            if (tblData.getRowCount() > 0) {
                lblTotalTransAmt.setText(String.valueOf(observable.totSubsidyAmt((DefaultTableModel) tblData.getModel())));
                updateOBFields();
                observable.setAuthorizeMap(map);

                ClientUtil.execute("rejectLoanRebate", map);
                ClientUtil.execute("rejectLoanRebateMaster", map);
                if (observable.getResult() == ClientConstants.ACTIONTYPE_FAILED) {
                    return;
                } else {
                    btnCancelActionPerformed(null);
                }
                observable.setResultStatus();

            } else {
                ClientUtil.showMessageWindow("Please find Record then Applied Subsidy");
                return;
            }


            isFilled = false;

            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);

        } else {

            viewType = ClientConstants.ACTIONTYPE_AUTHORIZE;
            observable.setStatus();

            popUpEdit(ClientConstants.ACTIONTYPE_AUTHORIZE);

            btnSave.setEnabled(false);

            btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
            btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
            btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);

        }
    }

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeActionPerformed();
        btnView.setEnabled(false);

    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void authorizeActionPerformed() {

        if (viewType == ClientConstants.ACTIONTYPE_AUTHORIZE && isFilled) {
            LoanRebateTO loanrebateTO = new LoanRebateTO();
            ArrayList list = new ArrayList();
            int n = tblData.getRowCount();
            HashMap map = null;
            if (n > 0) {
                for (int i = 0; i < n; i++) {
                    map = new HashMap();
                    String acno = CommonUtil.convertObjToStr(tblData.getValueAt(i, 0));
                    long l = 0;
                    map.put("ACC_NUM", tblData.getValueAt(i, 0));
                    map.put("AMOUNT", tblData.getValueAt(i, 3));
                    HashMap hash = interestCalculationTLAD(acno, l);
                    String interest = CommonUtil.convertObjToStr(hash.get("INTEREST"));
                    map.put("NORMALINTEREST", interest);
                    map.put("REBATE_UPTO", tdtFromDate.getDateValue());
                    list.add(map);
                }
                observable.doActionPerformedAuthorize(list);
                if (observable.getResult() == ClientConstants.ACTIONTYPE_FAILED) {
                    return;
                } else {
                    btnCancelActionPerformed(null);
                }
                observable.setResultStatus();


            } else {
                ClientUtil.showMessageWindow("Please find Record then Applied Rebate");
                return;
            }


            isFilled = false;

            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);

        } else {

            viewType = ClientConstants.ACTIONTYPE_AUTHORIZE;
            observable.setStatus();

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
        btnSave.setEnabled(btnCancel.isEnabled());
        btnAuthorize.setEnabled(!btnAuthorize.isEnabled());
        btnReject.setEnabled(!btnReject.isEnabled());
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        enableDisableSearchDetails(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        viewType = ClientConstants.ACTIONTYPE_DELETE;
        popUpEdit(ClientConstants.ACTIONTYPE_DELETE);
        observable.setStatus();
        btnView.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        enableDisableSearchDetails(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        viewType = ClientConstants.ACTIONTYPE_EDIT;
        popUpEdit(ClientConstants.ACTIONTYPE_EDIT);
        btnNewRecord.setEnabled(false);
        observable.setStatus();
        btnView.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        enableDisableSearchDetails(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        viewType = ClientConstants.ACTIONTYPE_NEW;
        System.out.println("ViewType######" + viewType);
        setButtonEnableDisable();
        observable.setStatus();
        btnView.setEnabled(false);

    }//GEN-LAST:event_btnNewActionPerformed

    private void btnToAccountNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToAccountNoActionPerformed
        // TODO add your handling code here:
        popUp(TO);
    }//GEN-LAST:event_btnToAccountNoActionPerformed

    private void btnFromAccountNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromAccountNoActionPerformed
        // TODO add your handling code here:
        popUp(FROM);
    }//GEN-LAST:event_btnFromAccountNoActionPerformed
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType1 = field;
        HashMap hash = new HashMap();
        String prodType = CommonUtil.convertObjToStr(cboProdType.getSelectedItem());
        if (prodType.equals("Advances")) {
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListAD");

        } else if (prodType.equals("Term Loans")) {
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

        //        if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT){
        //            HashMap mapParam = new HashMap();
        //            HashMap whereMap = new HashMap();
        //            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
        //            whereMap.put("BRANCH_CODE", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
        //
        //
        //            whereMap.put("AUTHORIZE", "AUTHORIZE");
        //            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
        //            whereMap = null;
        //            mapParam.put(CommonConstants.MAP_NAME, "getEditTermLoanSubsidyDetailsTO");
        //
        //            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
        //            authorizeUI.show();
        //        }else{
        viewType = field;
        HashMap hash = new HashMap();
        String prodType = CommonUtil.convertObjToStr(cboProdType.getSelectedItem());
        if (viewType == ClientConstants.ACTIONTYPE_AUTHORIZE || viewType == ClientConstants.ACTIONTYPE_REJECT) {
            viewMap.put(CommonConstants.MAP_NAME, "getEditLoanRebateListForAuthorize");
        } else if (viewType == ClientConstants.ACTIONTYPE_VIEW) {
            viewMap.put(CommonConstants.MAP_NAME, "getViewLoanRebateList");
        } else {
            viewMap.put(CommonConstants.MAP_NAME, "getEditLoanRebateList");
        }
        hash.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        hash.put("USER_ID", TrueTransactMain.USER_ID);
        viewMap.put(CommonConstants.MAP_WHERE, hash);
        new ViewAll(this, viewMap).show();

    }
    private void txtToAccountNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToAccountNoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtToAccountNoFocusLost

    private void txtFromAccountNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromAccountNoFocusLost
        // TODO add your handling code here:
        final String MESSAGE = validateAccNo();
        if (!MESSAGE.equalsIgnoreCase("")) {
            displayAlert(MESSAGE);
        }

    }//GEN-LAST:event_txtFromAccountNoFocusLost
    private String validateAccNo() {
        String from = txtFromAccountNo.getText();
        String to = txtToAccountNo.getText();
        String message = "";
        if (!(from.equalsIgnoreCase("") || to.equalsIgnoreCase(""))) {
            if (from.compareTo(to) > 0) {
                message = resourceBundle.getString("ACCOUNTWARNING");
            }
        }
        //        if(CommonUtil.convertObjToStr(cboProdType.getSelectedItem()).equals("TL") || ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString().equals("AD"))
        //        {
        HashMap hash = new HashMap();
        hash.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmProdId().getKeyForSelected()));
        hash.put("ACT_NUM", txtFromAccountNo.getText());
        if (txtToAccountNo != null && (!txtToAccountNo.getText().equals(""))) {
            hash.put("ACT_NUM", txtToAccountNo.getText());
        }
        hash.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
        List actlst = ClientUtil.executeQuery("getActNotCLOSEDTL", hash);
        if (actlst != null && actlst.size() > 0) {
        } else {
            ClientUtil.displayAlert("Enter the Valid Number");
            txtFromAccountNo.setText("");
            txtToAccountNo.setText("");
            return message;
        }
        //        }
        return message;
    }

    private void displayAlert(String message) {
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    private void btnClear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear1ActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this);
        resetinitSubsidyTableData();
    }//GEN-LAST:event_btnClear1ActionPerformed

    private void tblDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseReleased
        // TODO add your handling code here:
        if (/*(evt.getClickCount() == 2) && */(evt.getModifiers() == 16)) {
            whenTableRowSelected();
        }
    }//GEN-LAST:event_tblDataMouseReleased
    private void setVisibleFields(boolean flag) {
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
        if (prodType.equals("MDS")) {
            setVisibleFields(false);
            //            chkLoneeOnly.setText("Chittal Only");
            //            chkPrized.setVisible(true);
            //            chkNonPrized.setVisible(true);
        } else {
            setVisibleFields(true);
            //            chkLoneeOnly.setText("Loanee Only");
        }
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        //        if(tblData.getRowCount()>0){
        //           observable.removeRowsFromGuarantorTable(tblData);
        //            observable.removeRowsFromGuarantorTable(tblGuarantorData);

        //        }
        //        HashMap hmap=new HashMap();
        //        ArrayList linkedlist=new ArrayList();
        //        String fromAccno=txtFromAccountNo.getText();
        //        String toAccNo=txtToAccountNo.getText();
        //        ArrayList alist=null;
        //        ArrayList tablelist=null;
        //        hmap.put("FROMACT_NUM",fromAccno);
        //        hmap.put("TOACT_NUM",toAccNo);
        //       List list= ClientUtil.executeQuery("getPenalAccountNumber", hmap);
        //       hmap=null;
        //       if(list.size()>0 && list!=null){
        //           for(int i=0;i<list.size();i++){
        //           hmap=(HashMap)list.get(i);
        //           linkedlist.add(hmap.get("ACCT_NUM"));
        //           linkedlist.add(hmap.get("PENAL_WAIVE_OFF"));
        //           }
        //           hmap=null;
        //       }
        //       for(int i=0;i<linkedlist.size();i++){
        //           hmap=(HashMap)linkedlist.get(i);
        //           list= ClientUtil.executeQuery("getPenalAmount", hmap);
        //           hmap=null;
        //           hmap=(HashMap)list.get(i);
        //           double penal=CommonUtil.convertObjToDouble(hmap.get("PENAL")).doubleValue();
        //           if(penal>0){
        //               alist=new ArrayList();
        //               alist.add(hmap.get("ACT_NUM"));
        //               alist.add(hmap.get("name"));
        //               alist.add(hmap.get("REBATE_PERCENAGE"));
        //               tablelist.add(alist);
        //           }
        //       }
        DefaultTableModel tblModel = (DefaultTableModel) tblData.getModel();
        transAmtEdit = true;
        HashMap hmap = null;
        String strdate = tdtFromDate.getDateValue();
        if (!strdate.equals("")) {
            if (observable.isActivateNewRecord()) {

                setTableData(hmap);

            } else {
                initSubsidyTableData();
            }
        } else {
            ClientUtil.displayAlert("Rebate upto is required");
            return;
        }
        //        populateData();
        generateNotice = false;
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

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // Add your handling code here:
        if (viewType == ClientConstants.ACTIONTYPE_EDIT || viewType == ClientConstants.ACTIONTYPE_NEW) {
            observable.setSelectAll(tblData, new Boolean(chkSelectAll.isSelected()));
        }
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private HashMap interestCalculationTLAD(String accountNo, long noOfInstallment) {
        HashMap map = new HashMap();
        HashMap hash = null;
        try {
            String prod_id = "";
            //            String stringdd=tdtFromDate.getDateValue();
            //            Date dd=DateUtil.getDateMMDDYYYY(stringdd);
            //            Date currdt=(Date)currDt.clone();
            //            currdt.setDate(dd.getDate());
            //            currdt.setMonth(dd.getMonth());
            //            currdt.setYear(dd.getYear());
            map.put("ACT_NUM", accountNo);
            //		if((ComboBoxModel)cboProdId.getModel()!=null)
            //                if((((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString())!=null)
            //                    prod_id=((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString();
            map.put("PROD_ID", prod_id);
            map.put("TRANS_DT", ClientUtil.getCurrentDate());
            map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);

            String mapNameForCalcInt = "IntCalculationDetail";
            if (observable.getProdType().equals("AD")) {
                mapNameForCalcInt = "IntCalculationDetailAD";
            }
            List lst = ClientUtil.executeQuery(mapNameForCalcInt, map);
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                if (noOfInstallment > 0) {
                    map.put("NO_OF_INSTALLMENT", new Long(noOfInstallment));
                }

                //                    InterestCalculationTask interestcalTask=new InterestCalculationTask(header);
                map.putAll(hash);
                map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");

                //                map.put("CURR_DATE",  ClientUtil.getCurrentDate());
                map.put("CURR_DATE", DateUtil.getDateMMDDYYYY(observable.getRebateUpto()));
                //map.put("REBATE_INTEREST_CALCULATION","REBATE_INTEREST_CALCULATION");
                System.out.println("map before intereest###" + map);
                //                    hash =interestcalTask.interestCalcTermLoanAD(map);
                observable.setAsAnWhenCustomer(CommonUtil.convertObjToStr(map.get("AS_CUSTOMER_COMES")));
                hash = observable.loanInterestCalculationAsAndWhen(map);

                if (hash == null) {
                    hash = new HashMap();
                }
                System.out.println("hashinterestoutput###" + hash);
                hash.put("AS_CUSTOMER_COMES", map.get("AS_CUSTOMER_COMES"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("hash is $$$$" + hash);
        return hash;
    }

    private HashMap interestCalculationTLAD1(String accountNo, long noOfInstallment) {
        HashMap map = new HashMap();
        HashMap hash = null;
        try {
            String prod_id = "";
            //            String stringdd=tdtFromDate.getDateValue();
            //            Date dd=DateUtil.getDateMMDDYYYY(stringdd);
            //            Date currdt=(Date)currDt.clone();
            //            currdt.setDate(dd.getDate());
            //            currdt.setMonth(dd.getMonth());
            //            currdt.setYear(dd.getYear());
            map.put("ACT_NUM", accountNo);
            //		if((ComboBoxModel)cboProdId.getModel()!=null)
            //                if((((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString())!=null)
            //                    prod_id=((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString();
            map.put("PROD_ID", prod_id);
            map.put("TRANS_DT", ClientUtil.getCurrentDate());
            map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);

            String mapNameForCalcInt = "IntCalculationDetail";
            if (observable.getProdType().equals("AD")) {
                mapNameForCalcInt = "IntCalculationDetailAD";
            }
            List lst = ClientUtil.executeQuery(mapNameForCalcInt, map);
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                if (noOfInstallment > 0) {
                    map.put("NO_OF_INSTALLMENT", new Long(noOfInstallment));
                }

                //                    InterestCalculationTask interestcalTask=new InterestCalculationTask(header);
                map.putAll(hash);
                map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                map.put("FORPENAL_INTEREST_PURPOSE", "FORPENAL_INTEREST_PURPOSE");
                //                map.put("CURR_DATE",  ClientUtil.getCurrentDate());
                map.put("CURR_DATE", getProperDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue())));
                //map.put("REBATE_INTEREST_CALCULATION","REBATE_INTEREST_CALCULATION");
                System.out.println("map before intereest###" + map);
                //                    hash =interestcalTask.interestCalcTermLoanAD(map);
                observable.setAsAnWhenCustomer(CommonUtil.convertObjToStr(map.get("AS_CUSTOMER_COMES")));
                hash = observable.loanInterestCalculationAsAndWhen(map);

                if (hash == null) {
                    hash = new HashMap();
                }
                System.out.println("hashinterestoutput###" + hash);
                hash.put("AS_CUSTOMER_COMES", map.get("AS_CUSTOMER_COMES"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("hash is $$$$" + hash);
        return hash;
    }

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        observable.removeRowsFromGuarantorTable(tblData);
        //        observable.removeRowsFromGuarantorTable(tblGuarantorData);
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    public ArrayList populateDataForTable(HashMap hmap) {
        HashMap hashmap = new HashMap();
        ArrayList arrayList = new ArrayList();
        List list = ClientUtil.executeQuery("getEditLoanRebateValues", hmap);
        if (list.size() > 0 && list != null) {
            for (int i = 0; i < list.size(); i++) {
                hashmap = (HashMap) list.get(i);
                arrayList.add(hashmap);
            }

        }
        return arrayList;
    }

    public ArrayList populateData() {
        HashMap hmap = new HashMap();
        ArrayList linkedlist = new ArrayList();
        String fromAccno = txtFromAccountNo.getText();
        String toAccNo = txtToAccountNo.getText();
        String prod_id = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
        ArrayList alist = null;
        ArrayList tablelist = new ArrayList();
        hmap.put("FROMACT_NUM", fromAccno);
        hmap.put("TOACT_NUM", toAccNo);
        if (fromAccno.length() > 0 && toAccNo.length() > 0 || prod_id.length() > 0) {
            List list;
            if (fromAccno.length() > 0 && toAccNo.length() > 0) {
                list = ClientUtil.executeQuery("getPenalAccountNumber", hmap);
                hmap = null;
                if (list.size() > 0 && list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        hmap = (HashMap) list.get(i);
                        linkedlist.add(hmap);

                    }

                }
            } else {
                hmap.put("PROD_ID", prod_id);
                list = ClientUtil.executeQuery("getPenalAccountNumberForProd", hmap);
                hmap = null;
                if (list.size() > 0 && list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        hmap = (HashMap) list.get(i);
                        linkedlist.add(hmap);

                    }

                }
            }
            for (int i = 0; i < linkedlist.size(); i++) {
                Date ffromdate = null;
                Date ftodate = null;
                Date acopdt = null;
                String sdate = tdtFromDate.getDateValue();
                Date appdt = DateUtil.getDateMMDDYYYY(sdate);
                ftodate = getProperDate(appdt);
                hmap = (HashMap) linkedlist.get(i);
                System.out.println("hmapT$%%$%$%$%$%$%$%$" + hmap);
                if (rebatePeriod.equals("Financial year")) {
                    Date dd = ClientUtil.getCurrentDate();
                    int finYear = dd.getYear() + 1900;
                    Date findate = DateUtil.getDate(finDD, finMM, finYear);
                    ffromdate = (Date) currDt.clone();
//                    ftodate=(Date)currDt.clone();
                    ffromdate.setDate(findate.getDate());
                    ffromdate.setMonth(findate.getMonth());
                    ffromdate.setYear(findate.getYear());

                    if (ffromdate.before(currDt)) {
                        ffromdate = ffromdate;
                        //                        ftodate=ftodate;
                    } else {
                        ffromdate.setYear(findate.getYear() - 1);
                        //                        ftodate.setYear(ffromdate.getYear()+1);
                    }
                    String stracopdate = CommonUtil.convertObjToStr(hmap.get("ACCT_OPEN_DT"));


                    Date acopdate = DateUtil.getDateMMDDYYYY(stracopdate);
                    acopdt = (Date) currDt.clone();
                    acopdt.setDate(acopdate.getDate());
                    acopdt.setMonth(acopdate.getMonth());
                    acopdt.setYear(acopdate.getYear());
                } else {
                    String acdt = CommonUtil.convertObjToStr(hmap.get("ACCT_OPEN_DT"));
                    Date dd = DateUtil.getDateMMDDYYYY(acdt);
                    Date acopdate = DateUtil.getDateMMDDYYYY(acdt);
                    acopdt = (Date) currDt.clone();
                    acopdt.setDate(acopdate.getDate());
                    acopdt.setMonth(acopdate.getMonth());
                    acopdt.setYear(acopdate.getYear());
                    ffromdate = (Date) currDt.clone();
//                    ftodate=(Date)currDt.clone();
                    ffromdate.setDate(dd.getDate());
                    ffromdate.setMonth(dd.getMonth());
                    ffromdate.setYear(dd.getYear());
                    //                    ftodate=currDt;

                }
                System.out.println("ffromdat%&&%&%&%&%&%&" + ffromdate);
                System.out.println("ftodate%&&%&%&%&%&%&" + ftodate);
                hmap.put("FROMFINDT", ffromdate);
                hmap.put("TOFINDT", ftodate);
                list = ClientUtil.executeQuery("getPenalAmountDetails", hmap);
                if (list != null && list.size() > 0) {
                    hmap = null;
                    hmap = (HashMap) list.get(0);
                    System.out.println("hmap^$^$^$^$^$^11111" + hmap);
                    String borrowNO = CommonUtil.convertObjToStr(hmap.get("BORROW_NO"));
                    String number = CommonUtil.convertObjToStr(hmap.get("ACCT_NUM"));
                    String name = CommonUtil.convertObjToStr(hmap.get("ACCT_NAME"));
                    Date calFromDt = getProperDateFormat(hmap.get("CAL_FROM_DT"));
                    int calFromCount = CommonUtil.convertObjToInt(hmap.get("REBATE_CALC_COUNT"));
                    int calProdCount = CommonUtil.convertObjToInt(hmap.get("REBATE_CALC_PERIOD"));
                    double rebate = CommonUtil.convertObjToDouble(hmap.get("REBATE_INTEREST")).doubleValue();
                    String rebateCalcType = CommonUtil.convertObjToStr(hmap.get("REBATE_CALC"));
                    if (calFromCount > calProdCount) {
                        hmap.put("FROMDATE", calFromDt);
                        hmap.put("TDATE", ftodate);
                        hmap.put("ACT_NUM", number);
                        List paidlist;
                        double amount = 0.0;
                        paidlist = ClientUtil.executeQuery("getTLPaidAmountForVariousRates", hmap);
                        if (paidlist != null && paidlist.size() > 0) {
                            hmap = (HashMap) paidlist.get(0);
                            System.out.println("hmap%$%$$%$%$%$%$%22222" + hmap);
                            double amt = CommonUtil.convertObjToDouble(hmap.get("INTEREST")).doubleValue();
                            if (amt > 0.0) {
                                hmap.put("FROMDATE", calFromDt);
                                hmap.put("TDATE", ftodate);
                                hmap.put("ACT_NUM", number);
                                hmap.put("FROMDT", calFromDt);
                                hmap.put("TODATE", ftodate);
                                List penallist = ClientUtil.executeQuery("getPenalIntAmounts", hmap);
                                if (penallist.size() >= 1) {
                                    if (penallist != null && penallist.size() > 0) {
                                        for (int p = 0; p < penallist.size(); p++) {
                                            hmap = (HashMap) penallist.get(p);
                                            System.out.println("penallist%$%$%$%hmap" + hmap);
                                            String strfdt = CommonUtil.convertObjToStr(hmap.get("FROM_DT"));
                                            String strtdt = CommonUtil.convertObjToStr(hmap.get("TO_DATE"));
                                            double pint = CommonUtil.convertObjToDouble(hmap.get("INT_RATE")).doubleValue();
                                            double calcamt = CommonUtil.convertObjToDouble(hmap.get("INT_AMT")).doubleValue();
                                            System.out.println("amt%$%$%$%amt" + amt + "calcamt^%^%^^%" + calcamt);
                                            if (amt > 0.0) {
                                               if(rebateCalcType!=null && !rebateCalcType.equals("") && rebateCalcType.equals("Y")){ 
                                                    if (amt <= calcamt) {
                                                        amount = amount + (amt * rebate) / pint;
                                                        amt = amt - calcamt;
                                                    } else {
                                                        amount = amount + (calcamt * rebate) / pint;
                                                        amt = amt - calcamt;
                                                        if ((p + 1) == penallist.size()) {
                                                            if (amt > 0.0) {
                                                                amount = amount + (amt * rebate) / pint;
                                                            }
                                                        }
                                                    }
                                                }else{
                                                   if (amt <= calcamt) {
                                                        amount = amount + (amt * rebate) / 100;
                                                        amt = amt - calcamt;
                                                    } else {
                                                        amount = amount + (calcamt * rebate) / 100;
                                                        amt = amt - calcamt;
                                                        if ((p + 1) == penallist.size()) {
                                                            if (amt > 0.0) {
                                                                amount = amount + (amt * rebate) / 100;
                                                            }
                                                        }
                                                    }
                                               }
                                            }

                                        }
                                    }
                                } else {
                                    List tlList = ClientUtil.executeQuery("getLastIntCalDateForRebate", hmap);
                                    if (tlList.size() > 0 && tlList != null) {
                                        HashMap hashmap = (HashMap) tlList.get(0);
                                        System.out.println("hashmap%$%$%$%getLastIntCalDateForRebate" + hashmap);
                                        String limit = CommonUtil.convertObjToStr(hashmap.get("LIMIT"));
                                        hashmap = null;
                                        hashmap = new HashMap();
                                        hashmap.put("LIMIT", limit);
                                        hashmap.put("BORROW_NO", borrowNO);
                                        hashmap.put("DEPOSIT_DT", calFromDt);
                                        hashmap.put("ACT_NUM", number);
                                        hashmap.put("PROD_ID", prod_id);
                                        List intlist = ClientUtil.executeQuery("getIntRateTLForRebate", hashmap);
                                        if (intlist.size() > 0 && intlist != null) {
                                            for (int m = 0; m < intlist.size(); m++) {
                                                hashmap = (HashMap) intlist.get(m);
                                                System.out.println("getIntRateTLForRebate^%^%^%" + hashmap);
                                                double pint = CommonUtil.convertObjToDouble(hashmap.get("INTEREST")).doubleValue();
                                                System.out.println("amt in getIntRateTLForRebate " + amt);
                                                if(rebateCalcType!=null && !rebateCalcType.equals("") && rebateCalcType.equals("Y")){ 
                                                    amount = amount + (amt * rebate) / pint;
                                                }else{
                                                    amount = amount + (amt * rebate) / 100;
                                                }
                                                System.out.println("rebate in getIntRateTLForRebate " + rebate);
                                                System.out.println("amount in getIntRateTLForRebate " + amount);
                                            }
                                        }
                                        hashmap = null;
                                    }
                                }
                                String RebateAmount = CommonUtil.convertObjToStr(new Double(amount));
                                String RebateInt = CommonUtil.convertObjToStr(new Double(rebate));
                                hmap.put("ACCT_NUM", number);
                                hmap.put("ACCT_NAME", name);
                                hmap.put("REBATE_INTEREST", RebateInt);
                                hmap.put("INTEREST_AMOUNT", RebateAmount);
                                tablelist.add(hmap);
                            }
                        }
                    } else if (calFromCount == calProdCount) {
                        double rateofint = 0.0;
                        long l = 0;
                        double loanclosing = 0.0;
                        double penalint = 0.0;
                        HashMap hash = interestCalculationTLAD1(number, l);
                        if (hash != null && hash.size() > 0 && !hash.isEmpty()) {
                            if (hash.containsKey("LOAN_CLOSING_PENAL_INT") && !hash.get("LOAN_CLOSING_PENAL_INT").equals("")) {
                                loanclosing = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                            }
                            if (hash.containsKey("TOT_PENAL_INT") && !hash.get("TOT_PENAL_INT").equals("")) {
                                penalint = CommonUtil.convertObjToDouble(hash.get("TOT_PENAL_INT")).doubleValue();
                            }
                        }
                        if (penalint <= 0.0 && loanclosing == 0.0) {
                            hmap.put("FROMDATE", calFromDt);
                            hmap.put("TDATE", ftodate);
                            hmap.put("ACT_NUM", number);
                            List paidlist;
                            double amount = 0.0;
                            paidlist = ClientUtil.executeQuery("getTLPaidAmountForVariousRates", hmap);
                            if (paidlist != null && paidlist.size() > 0) {
                                hmap = (HashMap) paidlist.get(0);
                                System.out.println("hmap%$%$$%$%$%$%$%22222" + hmap);
                                double amt = CommonUtil.convertObjToDouble(hmap.get("INTEREST")).doubleValue();
                                if (amt > 0.0) {
                                    hmap.put("FROMDATE", calFromDt);
                                    hmap.put("TDATE", ftodate);
                                    hmap.put("ACT_NUM", number);
                                    hmap.put("FROMDT", calFromDt);
                                    hmap.put("TODATE", ftodate);
                                    List penallist = ClientUtil.executeQuery("getPenalIntAmounts", hmap);
                                    if (penallist.size() >= 1) {
                                        if (penallist != null && penallist.size() > 0) {
                                            for (int p = 0; p < penallist.size(); p++) {
                                                hmap = (HashMap) penallist.get(p);
                                                System.out.println("penallist%$%$%$%hmap" + hmap);
                                                String strfdt = CommonUtil.convertObjToStr(hmap.get("FROM_DT"));
                                                String strtdt = CommonUtil.convertObjToStr(hmap.get("TO_DATE"));
                                                double pint = CommonUtil.convertObjToDouble(hmap.get("INT_RATE")).doubleValue();
                                                double calcamt = CommonUtil.convertObjToDouble(hmap.get("INT_AMT")).doubleValue();
                                                System.out.println("amt%$%$%$%amt" + amt + "calcamt^%^%^^%" + calcamt);
                                                if (amt > 0.0) {
                                                  if(rebateCalcType!=null && !rebateCalcType.equals("") && rebateCalcType.equals("Y")){ 
                                                        if (amt <= calcamt) {
                                                            amount = amount + (amt * rebate) / pint;
                                                            amt = amt - calcamt;
                                                        } else {

                                                            amount = amount + (calcamt * rebate) / pint;
                                                            amt = amt - calcamt;
                                                            if ((p + 1) == penallist.size()) {
                                                                if (amt > 0.0) {
                                                                    amount = amount + (amt * rebate) / pint;
                                                                }
                                                            }
                                                        }
                                                    }else{
                                                        if (amt <= calcamt) {
                                                            amount = amount + (amt * rebate) / 100;
                                                            amt = amt - calcamt;
                                                        } else {
                                                            amount = amount + (calcamt * rebate) / 100;
                                                            amt = amt - calcamt;
                                                            if ((p + 1) == penallist.size()) {
                                                                if (amt > 0.0) {
                                                                    amount = amount + (amt * rebate) / 100;
                                                                }
                                                            }
                                                        }
                                                  }
                                                }

                                            }
                                        }
                                    } else {
                                        List tlList = ClientUtil.executeQuery("getLastIntCalDateForRebate", hmap);
                                        if (tlList.size() > 0 && tlList != null) {
                                            HashMap hashmap = (HashMap) tlList.get(0);
                                            System.out.println("hashmap%$%$%$%getLastIntCalDateForRebate" + hashmap);
                                            String limit = CommonUtil.convertObjToStr(hashmap.get("LIMIT"));
                                            hashmap = null;
                                            hashmap = new HashMap();
                                            hashmap.put("LIMIT", limit);
                                            hashmap.put("BORROW_NO", borrowNO);
                                            hashmap.put("DEPOSIT_DT", calFromDt);
                                            hashmap.put("ACT_NUM", number);
                                            hashmap.put("PROD_ID", prod_id);
                                            List intlist = ClientUtil.executeQuery("getIntRateTLForRebate", hashmap);
                                            if (intlist.size() > 0 && intlist != null) {
                                                for (int m = 0; m < intlist.size(); m++) {
                                                    hashmap = (HashMap) intlist.get(m);
                                                    System.out.println("getIntRateTLForRebate^%^%^%" + hashmap);
                                                    double pint = CommonUtil.convertObjToDouble(hashmap.get("INTEREST")).doubleValue();
                                                    System.out.println("amt in getIntRateTLForRebate " + amt);
                                                    if(rebateCalcType!=null && !rebateCalcType.equals("") && rebateCalcType.equals("Y")){ 
                                                        amount = amount + (amt * rebate) / pint;
                                                    }else{
                                                        amount = amount + (amt * rebate) / 100;
                                                    }
                                                    System.out.println("rebate in getIntRateTLForRebate " + rebate);
                                                    System.out.println("amount in getIntRateTLForRebate " + amount);
                                                }
                                            }
                                            hashmap = null;
                                        }

                                    }
                                    String RebateAmount = CommonUtil.convertObjToStr(new Double(amount));
                                    String RebateInt = CommonUtil.convertObjToStr(new Double(rebate));
                                    hmap.put("ACCT_NUM", number);
                                    hmap.put("ACCT_NAME", name);
                                    hmap.put("REBATE_INTEREST", RebateInt);
                                    hmap.put("INTEREST_AMOUNT", RebateAmount);
                                    tablelist.add(hmap);
                                }
                            }
                        }
                    }
                }
                list = null;
            }

            hmap = null;
        }

        System.out.println("@@@@@@@tablelist" + tablelist);
        return tablelist;
    }

    public ArrayList populateDataOLD() {
        HashMap hmap = new HashMap();
        ArrayList linkedlist = new ArrayList();
        String fromAccno = txtFromAccountNo.getText();
        String toAccNo = txtToAccountNo.getText();
        String prod_id = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
        ArrayList alist = null;
        ArrayList tablelist = new ArrayList();
        hmap.put("FROMACT_NUM", fromAccno);
        hmap.put("TOACT_NUM", toAccNo);
        if (fromAccno.length() > 0 && toAccNo.length() > 0 || prod_id.length() > 0) {
            List list;
            if (fromAccno.length() > 0 && toAccNo.length() > 0) {
                list = ClientUtil.executeQuery("getPenalAccountNumber", hmap);
                hmap = null;
                if (list.size() > 0 && list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        hmap = (HashMap) list.get(i);
                        linkedlist.add(hmap);

                    }

                }
            } else {
                hmap.put("PROD_ID", prod_id);
                list = ClientUtil.executeQuery("getPenalAccountNumberForProd", hmap);
                hmap = null;
                if (list.size() > 0 && list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        hmap = (HashMap) list.get(i);
                        linkedlist.add(hmap);

                    }

                }
            }
            for (int i = 0; i < linkedlist.size(); i++) {
                Date ffromdate = null;
                Date ftodate = null;
                Date acopdt = null;
                String sdate = tdtFromDate.getDateValue();
                Date appdt = DateUtil.getDateMMDDYYYY(sdate);
                ftodate = getProperDate(appdt);
                hmap = (HashMap) linkedlist.get(i);
                System.out.println("hmapT$%%$%$%$%$%$%$%$" + hmap);
                if (rebatePeriod.equals("Financial year")) {
                    Date dd = ClientUtil.getCurrentDate();
                    int finYear = dd.getYear() + 1900;
                    Date findate = DateUtil.getDate(finDD, finMM, finYear);
                    ffromdate = (Date) currDt.clone();
//                    ftodate=(Date)currDt.clone();
                    ffromdate.setDate(findate.getDate());
                    ffromdate.setMonth(findate.getMonth());
                    ffromdate.setYear(findate.getYear());

                    if (ffromdate.before(currDt)) {
                        ffromdate = ffromdate;
                        //                        ftodate=ftodate;
                    } else {
                        ffromdate.setYear(findate.getYear() - 1);
                        //                        ftodate.setYear(ffromdate.getYear()+1);
                    }
                    String stracopdate = CommonUtil.convertObjToStr(hmap.get("ACCT_OPEN_DT"));


                    Date acopdate = DateUtil.getDateMMDDYYYY(stracopdate);
                    acopdt = (Date) currDt.clone();
                    acopdt.setDate(acopdate.getDate());
                    acopdt.setMonth(acopdate.getMonth());
                    acopdt.setYear(acopdate.getYear());
                } else {
                    String acdt = CommonUtil.convertObjToStr(hmap.get("ACCT_OPEN_DT"));
                    Date dd = DateUtil.getDateMMDDYYYY(acdt);
                    Date acopdate = DateUtil.getDateMMDDYYYY(acdt);
                    acopdt = (Date) currDt.clone();
                    acopdt.setDate(acopdate.getDate());
                    acopdt.setMonth(acopdate.getMonth());
                    acopdt.setYear(acopdate.getYear());
                    ffromdate = (Date) currDt.clone();
//                    ftodate=(Date)currDt.clone();
                    ffromdate.setDate(dd.getDate());
                    ffromdate.setMonth(dd.getMonth());
                    ffromdate.setYear(dd.getYear());
                    //                    ftodate=currDt;

                }
                System.out.println("ffromdat%&&%&%&%&%&%&" + ffromdate);
                System.out.println("ftodate%&&%&%&%&%&%&" + ftodate);
                hmap.put("FROMFINDT", ffromdate);
                hmap.put("TOFINDT", ftodate);
                list = ClientUtil.executeQuery("getPenalAmount", hmap);
                if (list != null && list.size() > 0) {
                    hmap = null;
                    hmap = (HashMap) list.get(0);
                    double penal = CommonUtil.convertObjToDouble(hmap.get("PENAL")).doubleValue();
                    String waiveoff = CommonUtil.convertObjToStr(hmap.get("PENAL_WAIVE_OFF"));
                    String borrowNO = CommonUtil.convertObjToStr(hmap.get("BORROW_NO"));
                    String number = CommonUtil.convertObjToStr(hmap.get("ACCT_NUM"));
                    String name = CommonUtil.convertObjToStr(hmap.get("ACCT_NAME"));
                    String rebdt = CommonUtil.convertObjToStr(hmap.get("REBATE_DT"));
                    Date rbtdt = (Date) currDt.clone();
                    if (!rebdt.equals("")) {
                        Date rebatedt = DateUtil.getDateMMDDYYYY(rebdt);
                        rbtdt.setDate(rebatedt.getDate());
                        rbtdt.setMonth(rebatedt.getMonth());
                        rbtdt.setYear(rebatedt.getYear());
                    }
                    System.out.println("rebdt%$%$$%$%$%$%$%" + rbtdt);
                    double rebate = CommonUtil.convertObjToDouble(hmap.get("REBATE_INTEREST")).doubleValue();
                    if (penal == 0) {
                        String limit = "";
                        String stracopdate = "";
                        double rateofint = 0.0;
                        long l = 0;
                        double loanclosing = 0.0;
                        double penalint = 0.0;
                        HashMap hash = interestCalculationTLAD1(number, l);
                        if (hash != null && hash.size() > 0 && !hash.isEmpty()) {
                            if (hash.containsKey("LOAN_CLOSING_PENAL_INT") && !hash.get("LOAN_CLOSING_PENAL_INT").equals("")) {
                                loanclosing = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                            }
                            if (hash.containsKey("TOT_PENAL_INT") && !hash.get("TOT_PENAL_INT").equals("")) {
                                penalint = CommonUtil.convertObjToDouble(hash.get("TOT_PENAL_INT")).doubleValue();
                            }
                        }
                        if (penalint <= 0.0 && loanclosing == 0.0) {
                            String stringdd = tdtFromDate.getDateValue();
                            Date d = DateUtil.getDateMMDDYYYY(stringdd);
                            Date date = (Date) currDt.clone();
                            date.setDate(d.getDate());
                            date.setMonth(d.getMonth());
                            date.setYear(d.getYear());
                            if (!rebdt.equals("")) {
                                hmap.put("FROMDATE", rbtdt);
                                hmap.put("TDATE", DateUtil.addDays(date, -1));
                            } else {
                                hmap.put("FROMDATE", acopdt);
                                hmap.put("TDATE", DateUtil.addDays(date, -1));
                            }
                            hmap.put("ACT_NUM", number);
                            List paidlist;
                            double amount = 0.0;
                            System.out.println("hmap%$%$$%$%$%$%$%" + hmap);
                            paidlist = ClientUtil.executeQuery("getTLPaidAmountForVariousRates", hmap);
                            if (paidlist != null && paidlist.size() > 0) {
                                hmap = (HashMap) paidlist.get(0);

                                double amt = CommonUtil.convertObjToDouble(hmap.get("INTEREST")).doubleValue();
                                if (amt > 0.0) {
                                    if (!rebdt.equals("")) {
                                        hmap.put("FROMDT", rbtdt);
                                        hmap.put("TODATE", DateUtil.addDays(date, -1));
                                    } else {
                                        hmap.put("FROMDT", acopdt);
                                        hmap.put("TODATE", DateUtil.addDays(date, -1));
                                    }
                                    hmap.put("ACT_NUM", number);
                                    List penallist = ClientUtil.executeQuery("getPenalIntAmounts", hmap);
                                    //                            List paidlist=ClientUtil.executeQuery("getTLPaidAmount", hmap);

                                    if (penallist.size() >= 1) {
                                        if (penallist != null && penallist.size() > 0) {

                                            //                                    if(penallist.size()>1){

                                            for (int p = 0; p < penallist.size(); p++) {
                                                hmap = (HashMap) penallist.get(p);
                                                System.out.println("penallist%$%$%$%hmap" + hmap);
                                                String strfdt = CommonUtil.convertObjToStr(hmap.get("FROM_DT"));
                                                String strtdt = CommonUtil.convertObjToStr(hmap.get("TO_DATE"));
                                                double pint = CommonUtil.convertObjToDouble(hmap.get("INT_RATE")).doubleValue();
                                                double calcamt = CommonUtil.convertObjToDouble(hmap.get("INT_AMT")).doubleValue();
                                                System.out.println("amt%$%$%$%amt" + amt + "calcamt^%^%^^%" + calcamt);
                                                if (amt > 0.0) {
                                                    if (amt <= calcamt) {
                                                        amount = amount + (amt * rebate) / pint;
                                                        amt = amt - calcamt;
                                                    } else {

                                                        amount = amount + (calcamt * rebate) / pint;
                                                        amt = amt - calcamt;
                                                        if ((p + 1) == penallist.size()) {
                                                            if (amt > 0.0) {
                                                                amount = amount + (amt * rebate) / pint;
                                                            }
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    } else {
                                        List tlList = ClientUtil.executeQuery("getLastIntCalDateForRebate", hmap);

                                        if (tlList.size() > 0 && tlList != null) {
                                            HashMap hashmap = (HashMap) tlList.get(0);
                                            System.out.println("hashmap%$%$%$%getLastIntCalDateForRebate" + hashmap);
                                            limit = CommonUtil.convertObjToStr(hashmap.get("LIMIT"));
                                            hashmap = null;
                                            hashmap = new HashMap();
                                            hashmap.put("LIMIT", limit);
                                            hashmap.put("BORROW_NO", borrowNO);
                                            if (!rebdt.equals("")) {

                                                hashmap.put("DEPOSIT_DT", rbtdt);
                                            } else {
                                                hashmap.put("DEPOSIT_DT", acopdt);
                                            }
                                            hashmap.put("ACT_NUM", number);
                                            hashmap.put("PROD_ID", prod_id);
                                            List intlist = ClientUtil.executeQuery("getIntRateTLForRebate", hashmap);
                                            if (intlist.size() > 0 && intlist != null) {
                                                for (int m = 0; m < intlist.size(); m++) {
                                                    hashmap = (HashMap) intlist.get(m);
                                                    System.out.println("getIntRateTLForRebate^%^%^%" + hashmap);
                                                    double pint = CommonUtil.convertObjToDouble(hashmap.get("INTEREST")).doubleValue();
                                                    System.out.println("amt in getIntRateTLForRebate " + amt);
                                                    amount = amount + (amt * rebate) / pint;
                                                    System.out.println("rebate in getIntRateTLForRebate " + rebate);
                                                    System.out.println("amount in getIntRateTLForRebate " + amount);
                                                }
                                            }
                                            hashmap = null;
                                        }

                                    }
                                    String RebateAmount = CommonUtil.convertObjToStr(new Double(amount));
                                    String RebateInt = CommonUtil.convertObjToStr(new Double(rebate));
                                    hmap.put("ACCT_NUM", number);
                                    hmap.put("ACCT_NAME", name);
                                    hmap.put("REBATE_INTEREST", RebateInt);
                                    hmap.put("INTEREST_AMOUNT", RebateAmount);
                                    tablelist.add(hmap);
                                }
                            }
                        }
                    }
                }
                list = null;
            }

            hmap = null;
        }

        System.out.println("@@@@@@@tablelist" + tablelist);
        return tablelist;
    }

    public void initLoanRebateTableData(HashMap hmap) {
        tblData.setModel(new javax.swing.table.DefaultTableModel(
                setTableData(hmap),
                new String[]{
                    "Select", "Acct Num", "Name", "RebateInterest", "InterestAmount"
                }) {

            Class[] types = new Class[]{
                java.lang.Boolean.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                true, false, false, false, false, transAmtEdit, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 5 && transAmtEdit) {
                    return true;
                }

                return canEdit[columnIndex];
            }
        });


        tblData.setCellSelectionEnabled(true);
        tblData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //tblDataPropertyChange(evt);
                //                lblTotalTransAmt.setText(String.valueOf(observable.totSubsidyAmt((DefaultTableModel)tblData.getModel())));
            }
        });
        setTableModelListener();
        setSizeTallyTableData();



    }

    public void initLoanRebateTableDataWithoutSelect(HashMap hmap) {
        tblData.setModel(new javax.swing.table.DefaultTableModel(
                setTableData(hmap),
                new String[]{
                    "Acct Num", "Name", "RebateInterest", "InterestAmount"
                }) {

            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, transAmtEdit, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 5 && transAmtEdit) {
                    return true;
                }

                return canEdit[columnIndex];
            }
        });


        tblData.setCellSelectionEnabled(true);
        tblData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //tblDataPropertyChange(evt);
                //                lblTotalTransAmt.setText(String.valueOf(observable.totSubsidyAmt((DefaultTableModel)tblData.getModel())));
            }
        });
        setTableModelListener();
        setSizeTallyTableData();



    }

    public void initSubsidyTableData() {
        HashMap hmap = null;
        tblData.setModel(new javax.swing.table.DefaultTableModel(
                setTableData(hmap),
                new String[]{
                    "Select", "Acct Num", "Name", "RebateInterest", "InterestAmount"
                }) {

            Class[] types = new Class[]{
                java.lang.Boolean.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                true, false, false, false, false, transAmtEdit, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 5 && transAmtEdit) {
                    return true;
                }

                return canEdit[columnIndex];
            }
        });


        tblData.setCellSelectionEnabled(true);
        tblData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //tblDataPropertyChange(evt);
                //                lblTotalTransAmt.setText(String.valueOf(observable.totSubsidyAmt((DefaultTableModel)tblData.getModel())));
            }
        });
        setTableModelListener();
        setSizeTallyTableData();
        //       lblTotalTransAmt.setText(String.valueOf(observable.totSubsidyAmt((DefaultTableModel)tblData.getModel())));
    }

    public void resetinitSubsidyTableData() {
        Object obj[][] = new Object[0][0];
        tblData.setModel(new javax.swing.table.DefaultTableModel(
                obj,
                new String[]{
                    "Select", "Acct Num", "Name", "RebateInterest", "InterestAmount"
                }) {

            Class[] types = new Class[]{
                java.lang.Boolean.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                true, false, false, false, false, transAmtEdit, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 5 && transAmtEdit) {
                    return true;
                }

                return canEdit[columnIndex];
            }
        });


        tblData.setCellSelectionEnabled(true);
        tblData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblDataPropertyChange(evt);
            }
        });
        setTableModelListener();
        setSizeTallyTableData();
    }

    private void tblDataPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblRecoveryListTallyPropertyChange
        // TODO add your handling code here:
        if (tblData.getSelectedRow() > -1) {
            //        double transAmt=CommonUtil.convertObjToDouble(((DefaultTableModel)tblData.getModel()).getValueAt( tblData.getSelectedRow(), 5)).doubleValue();
            //
            //        ((DefaultTableModel)tblData.getModel()).setValueAt(String.valueOf(transAmt), tblData.getSelectedRow(), 5);
        }
    }//GEN-LAST:event_tblRecoveryListTallyPropertyChange
    //    private Object[][] setTableData(HashMap hmap) {
    //          HashMap whereMap=new HashMap();
    //
    //         List list=ClientUtil.executeQuery("getEditLoanRebateValues",hmap);
    //          Object totalList[][] = new Object[list.size()][8];
    //             Object totalListRow[] = new Object[8];
    //          for(int i=0;i<list.size();i++){
    //                whereMap=(HashMap) list.get(i);
    //                    totalList[i][0] = new Boolean(true);
    //                    totalList[i][1]  = CommonUtil.convertObjToStr(whereMap.get("ACCT_NUM"));
    //                    totalList[i][2] = CommonUtil.convertObjToStr(whereMap.get("ACCT_NAME"));
    //                    totalList[i][3] = CommonUtil.convertObjToStr(whereMap.get("REBATE_INTEREST"));
    //                    totalList[i][4] = CommonUtil.convertObjToStr(whereMap.get("INTEREST_AMOUNT"));
    //
    //
    //
    //                    int count =tblData.getRowCount();
    //
    //                    ((DefaultTableModel)tblData.getModel()).insertRow(count,totalListRow);
    //                    ((DefaultTableModel)tblData.getModel()).fireTableRowsInserted(0, count);
    //          }
    //              return totalList;
    //    }

    private Object[][] setTableData(HashMap hmap) {
        DefaultTableModel tblModel = (DefaultTableModel) tblData.getModel();
        HashMap whereMap = new HashMap();
        ArrayList recoveryList = new ArrayList();
        if (viewType == ClientConstants.ACTIONTYPE_DELETE || viewType == ClientConstants.ACTIONTYPE_EDIT || viewType == ClientConstants.ACTIONTYPE_AUTHORIZE || viewType == ClientConstants.ACTIONTYPE_VIEW) {
            recoveryList = (ArrayList) populateDataForTable(hmap);
            System.out.println("####### recoveryList :" + recoveryList);
        } else {
            recoveryList = (ArrayList) populateData();
        }
        if (recoveryList != null && recoveryList.size() > 0) {

            Object totalList[][] = new Object[recoveryList.size()][8];
            Object totalListRow[] = new Object[8];


            whereMap = new HashMap();
            double total_Demand = 0.0;
            double total_RecoveredAmt = 0.0;
            for (int i = 0; i < recoveryList.size(); i++) {
                whereMap = (HashMap) recoveryList.get(i);
                System.out.println("####### recoveryList : " + i + "" + recoveryList);
                System.out.println("####### whereMap : " + i + "" + whereMap);
                double lamount = CommonUtil.convertObjToDouble(whereMap.get("INTEREST_AMOUNT")).doubleValue();
                double amt = (double) getNearest((long) (lamount * 100), 100) / 100;
                if (viewType == ClientConstants.ACTIONTYPE_EDIT || viewType == ClientConstants.ACTIONTYPE_NEW) {
                    totalList[i][0] = new Boolean(false);
                    totalList[i][1] = CommonUtil.convertObjToStr(whereMap.get("ACCT_NUM"));
                    totalList[i][2] = CommonUtil.convertObjToStr(whereMap.get("ACCT_NAME"));
                    totalList[i][3] = CommonUtil.convertObjToStr(whereMap.get("REBATE_INTEREST"));
                    totalList[i][4] = new Double(amt);

                } else {
                    totalList[i][0] = CommonUtil.convertObjToStr(whereMap.get("ACCT_NUM"));
                    totalList[i][1] = CommonUtil.convertObjToStr(whereMap.get("ACCT_NAME"));
                    totalList[i][2] = CommonUtil.convertObjToStr(whereMap.get("REBATE_INTEREST"));
                    //                    totalList[i][3] = CommonUtil.convertObjToStr(whereMap.get("INTEREST_AMOUNT"));
                    totalList[i][3] = new Double(amt);
                }

                if (observable.isActivateNewRecord()) {
                    if (viewType == ClientConstants.ACTIONTYPE_EDIT || viewType == ClientConstants.ACTIONTYPE_NEW) {
                        totalListRow[0] = new Boolean(true);
                        totalListRow[1] = CommonUtil.convertObjToStr(whereMap.get("ACCT_NUM"));
                        totalListRow[2] = CommonUtil.convertObjToStr(whereMap.get("ACCT_NAME"));
                        totalListRow[3] = CommonUtil.convertObjToStr(whereMap.get("REBATE_INTEREST"));
                        totalList[i][4] = new Double(amt);


                    } else {
                        totalListRow[0] = CommonUtil.convertObjToStr(whereMap.get("ACCT_NUM"));
                        totalListRow[1] = CommonUtil.convertObjToStr(whereMap.get("ACCT_NAME"));
                        totalListRow[2] = CommonUtil.convertObjToStr(whereMap.get("REBATE_INTEREST"));
                        totalList[i][3] = new Double(amt);
                    }


                    int count = tblData.getRowCount();

                    ((DefaultTableModel) tblData.getModel()).insertRow(count, totalListRow);
                    ((DefaultTableModel) tblData.getModel()).fireTableRowsInserted(0, count);

                }

            }

            return totalList;
        } else {
            ClientUtil.showMessageWindow("No Data!!");
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
                        if (CommonUtil.convertObjToDouble(tblData.getModel().getValueAt(e.getFirstRow(), e.getColumn())).doubleValue() > 0) {
                            double demand_Amount = CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 4).toString()).doubleValue();
                            double recovered_Amount = CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 5).toString()).doubleValue();
                            if (demand_Amount < recovered_Amount && recovered_Amount > 0) {
                                ClientUtil.showMessageWindow("Transaction Amount should not Cross Subsidy Amount !!!");
                                tblData.setValueAt(tblData.getValueAt(tblData.getSelectedRow(), 4), tblData.getSelectedRow(), 5);
                            }
                        }
                        TableModel model = tblData.getModel();
                        //                        calcTallyListTotal();
                    }
                    //Added by sreekrishnan
                    if (!chkSelectAll.isSelected()) {
                        if (column == 0) {
                            if ((Boolean) tblData.getModel().getValueAt(row, 0)) {
                                if (observable.checkClosedLoan(CommonUtil.convertObjToStr((tblData.getModel().getValueAt(row, 1))))) {
                                    ClientUtil.showMessageWindow("This account is closed or inactive!!!\n Do you want to continue?");
                                }
                            }
                        }
                    }
                }
            }
        };
        tblData.getModel().addTableModelListener(tableModelListener);
    }

    private void setSizeTallyTableData() {
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

    public boolean applySubsidy() {
        //        updateOBFields();
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        String param1 = observable.getSelected(tblData);
        String prodType = String.valueOf(cboProdType.getSelectedItem());
        if (param1.length() > 0) {
            observable.setTotListMap(new HashMap());
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Are you sure?", CommonConstants.WARNINGTITLE,
                    COptionPane.YES_NO_OPTION, COptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);
            System.out.println("#$#$$ yesNo : " + yesNo);
            if (yesNo == 0) {
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
                tempList = (ArrayList) observable.getTableModel(tblData);
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

                System.out.println("guarantorMemberMap###" + guarantorMemberMap + "tempList####" + tempList);
                paramMap.put("BranchCode", ProxyParameters.BRANCH_ID);
                paramMap.put("ProductId", observable.getCbmProdId().getKeyForSelected());
                paramMap.put("Param1", param1);
                paramMap.put("Param2", guarantorGetSelected());
                paramMap.put("OverDueDt", currDt);
                //                ttIntegration.setParam(paramMap);
                //                observable

            } else {
                return true;
            }

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
        String selected = "";
        Object obj[] = guarantorMemberMap.keySet().toArray();
        for (int i = 0, j = obj.length; i < j; i++) {
            selected += "'" + obj[i];
            selected += "',";
        }
        // If no guarantor selected also records should be selected from other than guarantor.
        selected = selected.equals("") ? "'aa'" : selected.substring(0, selected.length() - 1);
        System.out.println("#$#$ guaranter selected : " + selected);
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

    public Date getProperDateFormat(Object obj) {
        Date curDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            curDt = (Date) currDt.clone();
            curDt.setDate(tempDt.getDate());
            curDt.setMonth(tempDt.getMonth());
            curDt.setYear(tempDt.getYear());
        }
        return curDt;
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

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                System.out.println("row #####" + row);
                boolean selected = ((Boolean) table.getValueAt(row, 0)).booleanValue();
                if (!selected) {
                    setForeground(Color.RED);
                } else {
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
        lblProdType.setText(resourceBundle.getString("lblProdType"));
        lblProdId.setText(resourceBundle.getString("lblProdId"));
        lblFromAccount.setText(resourceBundle.getString("lblFromAccount"));
        lblToAccount.setText(resourceBundle.getString("lblToAccount"));
        lblRebateDate.setText(resourceBundle.getString("lblRebateDate"));
        btnProcess.setText(resourceBundle.getString("btnProcess"));

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
        //observable.setLblTotalTransAmt(lblTotalTransAmt.getText());

        observable.setProdType(CommonUtil.convertObjToStr(cboProdType.getSelectedItem()));
        String prod_id = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
        observable.setCboProdId(prod_id);
        //        observable.setCbmProdId(CommonUtil.convertObjToStr(cboProdType.getSelectedItem()));
        observable.setFromAccNo(txtFromAccountNo.getText());
        observable.setToAccNo(txtToAccountNo.getText());
        observable.setRebateUpto(tdtFromDate.getDateValue());
    }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod < (roundingFactor / 2)) || (mod < (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear1;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnClose1;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnFromAccountNo;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNewRecord;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnToAccountNo;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblFromAccount;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecords;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecords2;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecordsVal;
    private com.see.truetransact.uicomponent.CLabel lblPanNumber1;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblRebateDate;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblSpace57;
    private com.see.truetransact.uicomponent.CLabel lblSpace58;
    private com.see.truetransact.uicomponent.CLabel lblSpace59;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace60;
    private com.see.truetransact.uicomponent.CLabel lblSpace61;
    private com.see.truetransact.uicomponent.CLabel lblSpace7;
    private com.see.truetransact.uicomponent.CLabel lblSpace9;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToAccount;
    private com.see.truetransact.uicomponent.CLabel lblToDate1;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransAmt;
    private com.see.truetransact.uicomponent.CLabel lblspace3;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch2;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CPanel panToAccount;
    private com.see.truetransact.uicomponent.CPanel panToAccount1;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private javax.swing.JToolBar tbrTermLoan;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CTextField txtEditTermLoanNo;
    private com.see.truetransact.uicomponent.CTextField txtFromAccountNo;
    private com.see.truetransact.uicomponent.CTextField txtToAccountNo;
    // End of variables declaration//GEN-END:variables
}
