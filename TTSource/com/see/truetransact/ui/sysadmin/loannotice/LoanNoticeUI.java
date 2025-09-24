/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LoanNoticeUI.java
 * Swaroop 
 * Created on December 07, 2009, 1:46 PM
 */

package com.see.truetransact.ui.sysadmin.loannotice;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;

import org.apache.log4j.Logger;

import com.see.truetransact.clientutil.EnhancedComboBoxModel;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CPanel;
import com.see.truetransact.uicomponent.CDialog;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CComboBox;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.viewall.ViewAll;
import java.util.LinkedHashMap;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.Date;
import com.see.truetransact.uimandatory.MandatoryCheck;

/**
 * @author  Swaroop
 */
public class LoanNoticeUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {
    private LoanNoticeOB observable;
    HashMap paramMap = null;
    int amtColumnNo=0;
    double tot = 0;
    String amtColName = "";
    String behavesLike = "";
    private LinkedHashMap Selected = new LinkedHashMap();
    private LinkedHashMap NotSelected = new LinkedHashMap();
    private int viewType =0;
    private HashMap mandatoryMap;
    private  LoanNoticeMRB objMandatoryRB;
    private LinkedHashMap finalUpdateMap = new LinkedHashMap();
    boolean updateCount = false;
    
//    private final static Logger log = Logger.getLogger(ViewAllTransactions.class);
    
    private TransDetailsUI transDetails = null;
    private Date dt = null;
    /** Creates new form PassBook */
    public LoanNoticeUI() {
        setupInit();
        setupScreen();
    }

    private void setupInit() {
        initComponents();
        internationalize();
        setFieldNames();
        setMandatoryHashMap();
        setObservable();
        setHelpMessage();
        toFront();
        setCombos();
        btnClear.setVisible(true);
        setMaxLenths();
        observable.resetForm();
        update(observable, null);
        dt = ClientUtil.getCurrentDate();
        Date dt1= DateUtil.addDays(dt, -1);
        tdtDayEndDt.setDateValue(CommonUtil.convertObjToStr(dt1));
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
    private void setFieldNames() {
        cboNoticeType.setName("cboNoticeType");
        cboProdType.setName("cboProdType");
        cboProdId.setName("cboProdId");
        tdtDayEndDt.setName("tdtDayEndDt");
        tdtFromInstDate.setName("tdtFromInstDate");
        tdtToInstDate.setName("tdtToInstDate");
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboNoticeType", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("cboProdId",new Boolean(true));
        mandatoryMap.put("tdtDayEndDt",new Boolean(true));
        mandatoryMap.put("tdtFromInstDate",new Boolean(true));
        mandatoryMap.put("tdtToInstDate",new Boolean(true));
        
    }
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
     public void setHelpMessage() {
//        objMandatoryRB = new LoanNoticeMRB();
//        cboNoticeType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboNoticeType"));
//        cboProdType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdType"));
//        cboProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdId"));
//        tdtDayEndDt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAreaDataParticular"));
//        tdtFromInstDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtFromInstDate"));
//        tdtToInstDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtToInstDate"));
    }
    private void setObservable() {
        try {
            observable = LoanNoticeOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setMaxLenths() {
        txtFromSanAmt.setValidation(new CurrencyValidation(16,2));
        txtToSanAm.setValidation(new CurrencyValidation(16,2));
        txtFromDueAmt.setValidation(new CurrencyValidation(16,2));
        txtToDueAmt.setValidation(new CurrencyValidation(16,2));
    }
    
    private void updateOBFields() {
        observable.setProdType(CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected()));
//        observable.setTdtFromDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
//        observable.setTdtToDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
    }
    
    private void setCombos() {
        cboProdType.setModel(observable.getCbmProdType());
        cboNoticeType.setModel(observable.getCbmNotType());
    }
    
    private void addComboValues(List lst, CComboBox cbo) {
        HashMap hash = new HashMap();
        ArrayList arr = new ArrayList();
        String str ="";
        arr.add("");
        for(int i=0; i<lst.size(); i++) {
            hash = (HashMap)lst.get(i);
            arr.add(hash.get("HEAD"));
        }
        EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(arr);
        cbo.setModel(cboModel);
        hash = null;
        arr = null;
        cboModel = null;
    }
    
    public void populateData() {
        updateOBFields();
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "getAllTransactionsPassBook"+observable.getProdType());
        whereMap.put("ACT_NUM", observable.getTxtAccNo());
        if(observable.getTdtFromDate()!=null)
        whereMap.put("FROM_DT", observable.getTdtFromDate());
        else if(observable.getTdtToDate()!=null){
             if(observable.getTdtFromDate()==null){
                 ClientUtil.displayAlert("Enter From And To Date");
                 return;
             }
        }
         if(observable.getTdtToDate()!=null)
        whereMap.put("TO_DT", observable.getTdtToDate());
         else if(observable.getTdtFromDate()!=null){
             java.util.Date dt = ClientUtil.getCurrentDateWithTime();
            whereMap.put("TO_DT", dt);
         }
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
//            log.info("populateData...");
//            ArrayList heading = observable.populateData(viewMap, tblData);
//            heading = null;
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
        viewMap = null;
        whereMap = null;
    }
    
    public void show() {
        /* The following if condition commented by Rajesh
         * Because observable is not making null after closing the UI
         * So, if no data found in previously opened EnquiryUI instance
         * the observable.isAvailable() is false, so EnquiryUI won't open.
        */
//        if (observable.isAvailable()) {
            super.show();
//        }
    }

    /**
     * Bring up and populate the temporary project detail screen.
     */
    private void whenTableRowSelected() {
        this.dispose();        

    }
    
    private String getLockDetails(String lockedBy, String screenId){
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer() ;
        map.put("LOCKED_BY", lockedBy) ;
        map.put("SCREEN_ID", screenId) ;
        List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if(lstLock.size() > 0){
            map = (HashMap)(lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME")) ;
            data.append("\nIP Address : ").append(map.get("IP_ADDR")) ;
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null ;
        map = null ;
        return data.toString();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        rdgAndOr = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPassBookType = new com.see.truetransact.uicomponent.CButtonGroup();
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        panAccountTo = new com.see.truetransact.uicomponent.CPanel();
        lblToAccount = new com.see.truetransact.uicomponent.CLabel();
        txtToAccount = new com.see.truetransact.uicomponent.CTextField();
        lblToInsDueDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToInstDate = new com.see.truetransact.uicomponent.CDateField();
        btnToAccount = new com.see.truetransact.uicomponent.CButton();
        txtToSanAm = new com.see.truetransact.uicomponent.CTextField();
        txtToDueAmt = new com.see.truetransact.uicomponent.CTextField();
        lblToSanAmt = new com.see.truetransact.uicomponent.CLabel();
        lblToDueAmt = new com.see.truetransact.uicomponent.CLabel();
        tdtToIntDate = new com.see.truetransact.uicomponent.CDateField();
        lblToInDueDate = new com.see.truetransact.uicomponent.CLabel();
        panAccountFrom = new com.see.truetransact.uicomponent.CPanel();
        lblfromAccount = new com.see.truetransact.uicomponent.CLabel();
        txtFromAccount = new com.see.truetransact.uicomponent.CTextField();
        lblFromInsDueDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromInstDate = new com.see.truetransact.uicomponent.CDateField();
        btnFromAccount = new com.see.truetransact.uicomponent.CButton();
        lblProdID = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboNoticeType = new com.see.truetransact.uicomponent.CComboBox();
        lblNotType = new com.see.truetransact.uicomponent.CLabel();
        txtFromSanAmt = new com.see.truetransact.uicomponent.CTextField();
        lblfromSanAmt = new com.see.truetransact.uicomponent.CLabel();
        txtFromDueAmt = new com.see.truetransact.uicomponent.CTextField();
        lblfromDueAmt = new com.see.truetransact.uicomponent.CLabel();
        tdtFromIntDate = new com.see.truetransact.uicomponent.CDateField();
        lblFromInDueDate = new com.see.truetransact.uicomponent.CLabel();
        tdtDayEndDt = new com.see.truetransact.uicomponent.CDateField();
        lblDayendDt = new com.see.truetransact.uicomponent.CLabel();
        panPassBookType = new com.see.truetransact.uicomponent.CPanel();
        btnFinal1 = new com.see.truetransact.uicomponent.CButton();
        btnView = new com.see.truetransact.uicomponent.CButton();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(750, 400));
        setPreferredSize(new java.awt.Dimension(750, 400));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(700, 310));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(700, 310));
        panAccountTo.setLayout(new java.awt.GridBagLayout());

        panAccountTo.setMinimumSize(new java.awt.Dimension(290, 155));
        panAccountTo.setPreferredSize(new java.awt.Dimension(290, 155));
        lblToAccount.setText("To Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountTo.add(lblToAccount, gridBagConstraints);

        txtToAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtToAccountActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountTo.add(txtToAccount, gridBagConstraints);

        lblToInsDueDate.setText("To Installment Due Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountTo.add(lblToInsDueDate, gridBagConstraints);

        tdtToInstDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToInstDateFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountTo.add(tdtToInstDate, gridBagConstraints);

        btnToAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnToAccount.setToolTipText("To Account");
        btnToAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnToAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToAccountActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountTo.add(btnToAccount, gridBagConstraints);

        txtToSanAm.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountTo.add(txtToSanAm, gridBagConstraints);

        txtToDueAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountTo.add(txtToDueAmt, gridBagConstraints);

        lblToSanAmt.setText("To Sanction Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountTo.add(lblToSanAmt, gridBagConstraints);

        lblToDueAmt.setText("To Due Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountTo.add(lblToDueAmt, gridBagConstraints);

        tdtToIntDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToIntDateFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountTo.add(tdtToIntDate, gridBagConstraints);

        lblToInDueDate.setText("To Int Due Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountTo.add(lblToInDueDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 32, 4);
        panSearchCondition.add(panAccountTo, gridBagConstraints);

        panAccountFrom.setLayout(new java.awt.GridBagLayout());

        panAccountFrom.setMinimumSize(new java.awt.Dimension(325, 270));
        panAccountFrom.setPreferredSize(new java.awt.Dimension(325, 270));
        lblfromAccount.setText("From Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountFrom.add(lblfromAccount, gridBagConstraints);

        txtFromAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFromAccountActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountFrom.add(txtFromAccount, gridBagConstraints);

        lblFromInsDueDate.setText("From Installment Due Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountFrom.add(lblFromInsDueDate, gridBagConstraints);

        tdtFromInstDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromInstDateFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountFrom.add(tdtFromInstDate, gridBagConstraints);

        btnFromAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnFromAccount.setToolTipText("To Account");
        btnFromAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFromAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromAccountActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountFrom.add(btnFromAccount, gridBagConstraints);

        lblProdID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountFrom.add(lblProdID, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountFrom.add(cboProdId, gridBagConstraints);

        cboProdType.setMaximumSize(new java.awt.Dimension(100, 21));
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountFrom.add(cboProdType, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        panAccountFrom.add(lblProdType, gridBagConstraints);

        cboNoticeType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboNoticeType.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountFrom.add(cboNoticeType, gridBagConstraints);

        lblNotType.setText("Notice Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountFrom.add(lblNotType, gridBagConstraints);

        txtFromSanAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountFrom.add(txtFromSanAmt, gridBagConstraints);

        lblfromSanAmt.setText("From Sanction Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountFrom.add(lblfromSanAmt, gridBagConstraints);

        txtFromDueAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountFrom.add(txtFromDueAmt, gridBagConstraints);

        lblfromDueAmt.setText("From Due Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountFrom.add(lblfromDueAmt, gridBagConstraints);

        tdtFromIntDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromIntDateFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountFrom.add(tdtFromIntDate, gridBagConstraints);

        lblFromInDueDate.setText("From Int Due Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountFrom.add(lblFromInDueDate, gridBagConstraints);

        tdtDayEndDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDayEndDtFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountFrom.add(tdtDayEndDt, gridBagConstraints);

        lblDayendDt.setText("Day End");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountFrom.add(lblDayendDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(panAccountFrom, gridBagConstraints);

        panPassBookType.setLayout(new java.awt.GridBagLayout());

        panPassBookType.setMinimumSize(new java.awt.Dimension(150, 30));
        panPassBookType.setPreferredSize(new java.awt.Dimension(140, 23));
        btnFinal1.setText("Update");
        btnFinal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinal1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 4, 4);
        panPassBookType.add(btnFinal1, gridBagConstraints);

        btnView.setText("Preview/Print");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 4, 4);
        panPassBookType.add(btnView, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 59;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 4, 4);
        panSearchCondition.add(panPassBookType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(panSearchCondition, gridBagConstraints);

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(sptLine, gridBagConstraints);

        panSearch.setLayout(new java.awt.GridBagLayout());

        panSearch.setMinimumSize(new java.awt.Dimension(750, 40));
        panSearch.setPreferredSize(new java.awt.Dimension(750, 40));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif")));
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClose, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif")));
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panSearch, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void btnFinal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinal1ActionPerformed
        // TODO add your handling code here:
            try{
               if(updateCount){
                    ClientUtil.displayAlert("Record Already Updated");
                    return;
                }
               HashMap map = new HashMap();
               String noticeType=(((ComboBoxModel)cboNoticeType.getModel()).getKeyForSelected().toString());
               map.put("REPORT_ID",noticeType);
               List langList = ClientUtil.executeQuery("getLanguageAndName",map);
               if(langList!=null && langList.size()>0){
                   map = null;
                map =(HashMap) langList.get(0); 
                finalUpdateMap.put("LANGUAGE",map.get("LANGUAGE"));
                finalUpdateMap.put("REPORT_ID",map.get("REPORT_ID"));
                updateTheEntries(finalUpdateMap); 
               }
               else{
                  ClientUtil.displayAlert("U cant Update The Data Without Having a Preview Or Printing The Data..."); 
                }
            }
            catch(Exception e){
                ClientUtil.displayAlert("Error In Updating");
                e.printStackTrace();
            }
    }//GEN-LAST:event_btnFinal1ActionPerformed

    private void tdtDayEndDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDayEndDtFocusLost
        // TODO add your handling code here:
        ClientUtil.validateLTDate(tdtDayEndDt);
    }//GEN-LAST:event_tdtDayEndDtFocusLost

    private void tdtFromIntDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromIntDateFocusLost
        // TODO add your handling code here:
        ClientUtil.validateLTDate(tdtFromIntDate);
    }//GEN-LAST:event_tdtFromIntDateFocusLost

    private void tdtFromInstDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromInstDateFocusLost
        // TODO add your handling code here:
        ClientUtil.validateLTDate(tdtFromInstDate);
    }//GEN-LAST:event_tdtFromInstDateFocusLost

    private void tdtToIntDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToIntDateFocusLost
        // TODO add your handling code here:
        java.util.Date from = DateUtil.getDateMMDDYYYY(tdtFromIntDate.getDateValue());
        java.util.Date to = DateUtil.getDateMMDDYYYY(tdtToIntDate.getDateValue());
        if (from!=null && to!=null && DateUtil.dateDiff(from,to)<0) {
            displayAlert("To date should be greater than From Date...");
            tdtToIntDate.setDateValue("");
            tdtToIntDate.requestFocus();
        }
    }//GEN-LAST:event_tdtToIntDateFocusLost

    private void tdtToInstDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToInstDateFocusLost
        // TODO add your handling code here:
        java.util.Date from = DateUtil.getDateMMDDYYYY(tdtFromInstDate.getDateValue());
        java.util.Date to = DateUtil.getDateMMDDYYYY(tdtToInstDate.getDateValue());
        if (from!=null && to!=null && DateUtil.dateDiff(from,to)<0) {
            displayAlert("To date should be greater than From Date...");
            tdtToInstDate.setDateValue("");
            tdtToInstDate.requestFocus();
        }
    }//GEN-LAST:event_tdtToInstDateFocusLost

    private void btnToAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToAccountActionPerformed
        // TODO add your handling code here:
        viewType=2;
        String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
        String PROD_ID = ((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString();
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        whereMap.put(CommonConstants.PRODUCT_TYPE, prodType);
        whereMap.put(CommonConstants.PRODUCT_ID, PROD_ID);
        viewMap.put(CommonConstants.MAP_NAME, "LoanNotAcctList");
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this, viewMap).show();
    }//GEN-LAST:event_btnToAccountActionPerformed

    private void btnFromAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromAccountActionPerformed
        // TODO add your handling code here:
        viewType=1;
        String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
        String PROD_ID = ((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString();
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        whereMap.put(CommonConstants.PRODUCT_TYPE, prodType);
        whereMap.put(CommonConstants.PRODUCT_ID, PROD_ID);
        viewMap.put(CommonConstants.MAP_NAME, "LoanNotAcctList");
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this, viewMap).show();
    }//GEN-LAST:event_btnFromAccountActionPerformed

    private void txtToAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtToAccountActionPerformed
        // TODO add your handling code here:
        String actNum = txtFromAccount.getText();
        observable.setTxtAccNo(txtToAccount.getText());
        observable.setAccountName();
        if (observable.getLblAccNameValue().length()<=0) {
            ClientUtil.displayAlert("Invalid Account No.");
            txtToAccount.setText("");
        }else{
            populateData();
        }
    }//GEN-LAST:event_txtToAccountActionPerformed

    private void txtFromAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFromAccountActionPerformed
        // TODO add your handling code here:
        String actNum = txtFromAccount.getText();
        observable.setTxtAccNo(txtFromAccount.getText());
        observable.setAccountName();
        if (observable.getLblAccNameValue().length()<=0) {
            ClientUtil.displayAlert("Invalid Account No.");
            txtFromAccount.setText("");
        }else{
            populateData();
        }
    }//GEN-LAST:event_txtFromAccountActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        String mandatoryMessage = checkMandatory(panSearchCondition);
        if(mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }
        else{
            String repName="";
            String notType = ((ComboBoxModel)cboNoticeType.getModel()).getKeyForSelected().toString();
            HashMap guarMap = new HashMap();
            guarMap.put("REPORT_ID",notType);
            List guarList = ClientUtil.executeQuery("getGuarDetailsForLoanNotice",guarMap);
            if(guarList!=null && guarList.size()>0){
                guarMap=null;
                guarMap=(HashMap)guarList.get(0);
                String gurantorDetails= (String)guarMap.get("GRNT_DETAILS");
                if(gurantorDetails.equalsIgnoreCase("WITHOUT_GUARANTOR"))
                    repName= "loannoticewithoutguar";
                else if(gurantorDetails.equalsIgnoreCase("WITH_GUARANTOR"))
                    repName= "loannoticewithguar";
                else
                    repName= "loannoticewithguar";
            }
            
            String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
            String prodID = ((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString();
            HashMap reportMap = new HashMap();
            HashMap tempMap = new HashMap();
            
            //         reportMap.put("VIEW","VIEW");
            if(txtFromAccount.getText().length()>0 && (txtToAccount.getText().length()<=0)){
                ClientUtil.displayAlert("Enter TO Account Number");
                return;
            }
            else if(txtFromAccount.getText().length()>0 && txtToAccount.getText().length()>0 ){
                tempMap = new HashMap();
                tempMap.put("PARAM_NAME","FROM_ACCT_NUM");
                tempMap.put("PARAM_VALUE",txtFromAccount.getText());
                finalUpdateMap.put("FROM_ACCOUNT",txtFromAccount.getText());
                reportMap.put("FROM_ACCT_NUM",tempMap);
                tempMap = new HashMap();
                tempMap.put("PARAM_NAME","TO_ACCT_NUM");
                tempMap.put("PARAM_VALUE",txtToAccount.getText());
                finalUpdateMap.put("TO_ACCOUNT",txtToAccount.getText());
                reportMap.put("TO_ACCT_NUM",tempMap);
            }
            if(txtFromSanAmt.getText().length()>0 && (txtToSanAm.getText().length()<=0)){
                ClientUtil.displayAlert("Enter TO Sanction Amount");
                return;
            }
            else if(txtFromSanAmt.getText().length()>0 && txtFromSanAmt.getText().length()>0 ){
                tempMap = new HashMap();
                tempMap.put("PARAM_NAME","FROM_SAN_AMT");
                tempMap.put("PARAM_VALUE",txtFromSanAmt.getText());
                finalUpdateMap.put("FROM_SAN_AMT",txtFromSanAmt.getText());
                reportMap.put("FROM_SAN_AMT",tempMap);
                tempMap = new HashMap();
                tempMap.put("PARAM_NAME","TO_SAN_AMT");
                tempMap.put("PARAM_VALUE",txtToSanAm.getText());
                finalUpdateMap.put("TO_SAN_AMT",txtToSanAm.getText());
                reportMap.put("TO_SAN_AMT",tempMap);
            }
            if(txtFromDueAmt.getText().length()>0 && (txtToDueAmt.getText().length()<=0)){
                ClientUtil.displayAlert("Enter TO Due Amount");
                return;
            }
            else if(txtFromDueAmt.getText().length()>0 && txtFromDueAmt.getText().length()>0 ){
                tempMap = new HashMap();
                tempMap.put("PARAM_NAME","FROM_DUE_AMT");
                tempMap.put("PARAM_VALUE",txtFromDueAmt.getText());
                finalUpdateMap.put("FROM_DUE_AMT",txtFromDueAmt.getText());
                reportMap.put("FROM_DUE_AMT",tempMap);
                tempMap = new HashMap();
                tempMap.put("PARAM_NAME","TO_DUE_AMT");
                tempMap.put("PARAM_VALUE",txtToDueAmt.getText());
                finalUpdateMap.put("TO_DUE_AMT",txtToDueAmt.getText());
                reportMap.put("TO_DUE_AMT",tempMap);
            }
            tempMap = new HashMap();
            tempMap.put("PARAM_NAME","PROD_TYPE");
            tempMap.put("PARAM_VALUE",prodType);
            finalUpdateMap.put("PROD_TYPE",prodType);
            reportMap.put("PROD_TYPE",tempMap);
            tempMap = new HashMap();
            tempMap.put("PARAM_NAME","PROD_ID");
            tempMap.put("PARAM_VALUE",prodID);
            finalUpdateMap.put("PROD_ID",prodID);
            reportMap.put("PROD_ID",tempMap);
            tempMap = new HashMap();
            tempMap.put("PARAM_NAME","NOT_TYPE");
            tempMap.put("PARAM_VALUE",notType);
            finalUpdateMap.put("NOTICE_TYPE",notType);
            reportMap.put("NOT_TYPE",tempMap);
            
            Date currDt = (Date)dt.clone();
            Date trans_Dt=(Date)currDt.clone();
            Date FromInstdt= DateUtil.getDateMMDDYYYY(tdtFromInstDate.getDateValue());
            if(FromInstdt!=null){
                trans_Dt.setDate(FromInstdt.getDate());
                trans_Dt.setMonth(FromInstdt.getMonth());
                trans_Dt.setYear(FromInstdt.getYear());
                tempMap = new HashMap();
                tempMap.put("PARAM_NAME","FROM_INST_DUEDT");
                tempMap.put("PARAM_VALUE",trans_Dt);
                finalUpdateMap.put("FROM_INST_DUEDT",trans_Dt);
                reportMap.put("FROM_INST_DUEDT",tempMap);
            }
            Date ToInstdt= DateUtil.getDateMMDDYYYY(tdtToInstDate.getDateValue());
            Date trans_Dt1=(Date)currDt.clone();
            if(ToInstdt!=null){
                trans_Dt1.setDate(ToInstdt.getDate());
                trans_Dt1.setMonth(ToInstdt.getMonth());
                trans_Dt1.setYear(ToInstdt.getYear());
                tempMap = new HashMap();
                tempMap.put("PARAM_NAME","TO_INST_DUEDT");
                tempMap.put("PARAM_VALUE",trans_Dt1);
                finalUpdateMap.put("TO_INST_DUEDT",trans_Dt1);
                reportMap.put("TO_INST_DUEDT",tempMap);
                tempMap = new HashMap();
            }
            Date dayend= DateUtil.getDateMMDDYYYY(tdtDayEndDt.getDateValue());
            Date dayendDt=(Date)currDt.clone();
            if(dayend!=null){
                dayendDt.setDate(dayend.getDate());
                dayendDt.setMonth(dayend.getMonth());
                dayendDt.setYear(dayend.getYear());
                tempMap = new HashMap();
                tempMap.put("PARAM_NAME","DAY_END_DT");
                tempMap.put("PARAM_VALUE",dayendDt);
                finalUpdateMap.put("DAY_END_DT",dayendDt);
                reportMap.put("DAY_END_DT",tempMap);
            }
            callTTIntergration(repName, reportMap);
 }   
    }//GEN-LAST:event_btnViewActionPerformed

    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        System.out.println("#$#$ Hash : "+hash);
        if(viewType==1){
            txtFromAccount.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
        }
        else if(viewType==2){
            txtToAccount.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
        }
    }
    
    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        if (cboProdType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
        observable.setProdType(CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected()));
        behavesLike = "";
        observable.setCbmProdId(prodType);
        cboProdId.setModel(observable.getCbmProdId());
        }
        setModified(true);    
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
            
    

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        btnClearActionPerformed(null);
        cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        if(!updateCount){
            int a= ClientUtil.confirmationAlert("Data Is Not Updated Do u want To Continue With Clearing??");
            if(a==0){
                observable.resetForm();
                txtFromAccount.setText("");
                txtToAccount.setText("");
                tdtFromInstDate.setDateValue("");
                tdtToInstDate.setDateValue("");
                tdtFromIntDate.setDateValue("");
                tdtFromIntDate.setDateValue("");
                txtFromDueAmt.setText("");
                txtToDueAmt.setText("");
                txtFromSanAmt.setText("");
                txtToSanAm.setText("");
                cboNoticeType.setSelectedItem("");
                cboProdId.setSelectedItem("");
                tdtDayEndDt.setDateValue("");
                behavesLike = "";
                updateCount=false;
                setModified(false);
            }
            
        }
        else{
                observable.resetForm();
                txtFromAccount.setText("");
                txtToAccount.setText("");
                tdtFromInstDate.setDateValue("");
                tdtToInstDate.setDateValue("");
                tdtFromIntDate.setDateValue("");
                tdtFromIntDate.setDateValue("");
                txtFromDueAmt.setText("");
                txtToDueAmt.setText("");
                txtFromSanAmt.setText("");
                txtToSanAm.setText("");
                cboNoticeType.setSelectedItem("");
                cboProdId.setSelectedItem("");
                tdtDayEndDt.setDateValue("");
                behavesLike = "";
                updateCount=false;
                setModified(false);
        }
        
    }//GEN-LAST:event_btnClearActionPerformed
    
        private ComboBoxModel getListModel() {
        ComboBoxModel listData = new ComboBoxModel();
        return listData;
    }                            
        
        private void internationalize() {
        }
        
        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
            HashMap mapParam = new HashMap();

            HashMap where = new HashMap();
            where.put("beh", "CA");

            mapParam.put("MAPNAME", "getSelectInwardClearingAuthorizeTOList");
            //mapParam.put("WHERE", where);
            // HashMap rMap = (HashMap) dao.getData(mapParam);

            // HashMap testMap = new HashMap();
            //testMap.put("MAPNAME", "getSelectOperativeAcctProductTOList");
            new ViewAll(mapParam).show();
        }

        public void update(Observable observed, Object arg) {
            ((ComboBoxModel)cboProdType.getModel()).setKeyForSelected(observable.getProdType());
//            tdtFromDate.setDateValue(DateUtil.getStringDate(observable.getTdtFromDate()));
//            tdtToDate.setDateValue(DateUtil.getStringDate(observable.getTdtToDate()));
        }
        public void callTTIntergration(String repName,HashMap parMap){
            TTIntegration ttIntgration = null;
            ttIntgration.setParam(parMap);
            ttIntgration.integration(repName);
        }
        
        private void updateTheEntries(LinkedHashMap map) throws Exception{
            try{
                HashMap updateMap = new HashMap();
                List updateList= ClientUtil.executeQuery("getLoanNoticeData", map);
                if(updateList!=null && updateList.size()>0){
                    for(int i=0;i<updateList.size();i++){
                        updateMap=(HashMap)updateList.get(i);
                        updateMap.put("CURR_DATE",dt.clone());
                        updateMap.put("USER_ID",TrueTransactMain.USER_ID);
                        ClientUtil.execute("updateLoanNotice",updateMap);
                        updateMap=null;
                    }
                    updateCount=true;
                    ClientUtil.displayAlert("Updated Successfully");
                }
                else{
                    ClientUtil.displayAlert("No Records To Update"); 
                }
            }
            catch (Exception e){
                ClientUtil.displayAlert("Error In Updating");
                e.printStackTrace();
            }
        }
        
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnFinal1;
    private com.see.truetransact.uicomponent.CButton btnFromAccount;
    private com.see.truetransact.uicomponent.CButton btnToAccount;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboNoticeType;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CLabel lblDayendDt;
    private com.see.truetransact.uicomponent.CLabel lblFromInDueDate;
    private com.see.truetransact.uicomponent.CLabel lblFromInsDueDate;
    private com.see.truetransact.uicomponent.CLabel lblNotType;
    private com.see.truetransact.uicomponent.CLabel lblProdID;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblToAccount;
    private com.see.truetransact.uicomponent.CLabel lblToDueAmt;
    private com.see.truetransact.uicomponent.CLabel lblToInDueDate;
    private com.see.truetransact.uicomponent.CLabel lblToInsDueDate;
    private com.see.truetransact.uicomponent.CLabel lblToSanAmt;
    private com.see.truetransact.uicomponent.CLabel lblfromAccount;
    private com.see.truetransact.uicomponent.CLabel lblfromDueAmt;
    private com.see.truetransact.uicomponent.CLabel lblfromSanAmt;
    private com.see.truetransact.uicomponent.CPanel panAccountFrom;
    private com.see.truetransact.uicomponent.CPanel panAccountTo;
    private com.see.truetransact.uicomponent.CPanel panPassBookType;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAndOr;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPassBookType;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CDateField tdtDayEndDt;
    private com.see.truetransact.uicomponent.CDateField tdtFromInstDate;
    private com.see.truetransact.uicomponent.CDateField tdtFromIntDate;
    private com.see.truetransact.uicomponent.CDateField tdtToInstDate;
    private com.see.truetransact.uicomponent.CDateField tdtToIntDate;
    private com.see.truetransact.uicomponent.CTextField txtFromAccount;
    private com.see.truetransact.uicomponent.CTextField txtFromDueAmt;
    private com.see.truetransact.uicomponent.CTextField txtFromSanAmt;
    private com.see.truetransact.uicomponent.CTextField txtToAccount;
    private com.see.truetransact.uicomponent.CTextField txtToDueAmt;
    private com.see.truetransact.uicomponent.CTextField txtToSanAm;
    // End of variables declaration//GEN-END:variables
}

