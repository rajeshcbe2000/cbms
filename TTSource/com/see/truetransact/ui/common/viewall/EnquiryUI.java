/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * EnquiryUI.java
 *
 * Created on August 24, 2003, 1:46 PM
 */

package com.see.truetransact.ui.common.viewall;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;

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

/**
 * @author  balachandar
 */
public class EnquiryUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {
    private final ViewAllRB resourceBundle = new ViewAllRB();
    private EnquiryOB observable;
    HashMap paramMap = null;
    int amtColumnNo=0;
    double tot = 0;
    String amtColName = "";
    String behavesLike = "";
    boolean collDet=false;

    boolean StopPaymentView = false;
    private Date currDt = null;
    private  boolean btnDividendPressed = false;
    
    private final static Logger log = Logger.getLogger(ViewAllTransactions.class);
    
    private TransDetailsUI transDetails = null;
    
    /** Creates new form ViewAll */
    public EnquiryUI() {
        setupInit();
        setupScreen();
        currDt = ClientUtil.getCurrentDate();
    }

    private void setupInit() {
        initComponents();
        btnIntDet.setVisible(false);
        internationalize();
        setObservable();
        toFront();
        setCombos();
        transDetails = new TransDetailsUI(panMultiSearch);
        transDetails.setRefreshActDetails(true);
        panMultiSearch.setVisible(true);
        btnClear.setVisible(true);
        btnrecurrView.setVisible(false);
        observable.resetForm();
        update(observable, null);
        txtAccNo.setAllowAll(true);
        btnCollDt.setVisible(false);
        btnStopPaymentView.setVisible(false);
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
            observable = EnquiryOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updateOBFields() {
        observable.setProdType(CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected()));
	observable.setTxtAccNo(txtAccNo.getText());
        observable.setTdtFromDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
        observable.setTdtToDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
    }
    
    private void setCombos() {
        cboProdType.setModel(observable.getCbmProdType());
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
        if (!CommonUtil.convertObjToStr(observable.getProdType()).equals("")) {
            if (observable.getTdtFromDate()!=null || observable.StopPaymentView == true) {
                if (btnIntDetPressed) {
                    viewMap.put(CommonConstants.MAP_NAME, "getAllIntTransactions"+observable.getProdType());
                    if(observable.getLinkMap()!=null && observable.getLinkMap().containsKey("AS_CUSTOMER_COMES") && 
                        observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y")) {
                        if(observable.getProdType().equals("TL"))
                            viewMap.put(CommonConstants.MAP_NAME,"getAllIntTransactionsAsAnWhenTL");
                        else
                            viewMap.put(CommonConstants.MAP_NAME,"getAllIntTransactionsAsAnWhenAD");
                    }
                } else if(observable.btnRDTransPressed == true){
                    whereMap.put("CURR_DT", currDt);
                    viewMap.put(CommonConstants.MAP_NAME,"getAllRDTransactionsTD");            
                } else if(btnDividendPressed == true){
                    viewMap.put(CommonConstants.MAP_NAME,"getAllDiovidendSH");
                }else if(observable.StopPaymentView == true){
                    viewMap.put(CommonConstants.MAP_NAME, "getSelectStopPaymentDetails");
                }else{
                    viewMap.put(CommonConstants.MAP_NAME, "getAllTransactions"+observable.getProdType());
                }
                if(observable.getProdType().equals("TD")){
                    String actNum = "";
                    if (observable.getTxtAccNo().lastIndexOf("_")!=-1){
                        actNum = observable.getTxtAccNo().substring(0,observable.getTxtAccNo().lastIndexOf("_"));
                    }
                    whereMap.put("DEPOSIT_NO", actNum);
                }
                whereMap.put("ACT_NUM", observable.getTxtAccNo());
                if(observable.getTdtFromDate()!=null){
                    whereMap.put("FROM_DT", getProperDate(observable.getTdtFromDate()));
                }
                if (observable.getTdtToDate()!=null) {
                    whereMap.put("TO_DT", getProperDate(observable.getTdtToDate()));
                } else {
                    whereMap.put("TO_DT", currDt);
                }
                if (collDet)
                    whereMap.put("BEHAVES_LIKE", "DAILY");
                else
                    whereMap.put("BEHAVES_LIKE", null);
                viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                try {
                    log.info("populateData...");
                    ArrayList heading = observable.populateData(viewMap, tblData);
                    heading = null;
                } catch( Exception e ) {
                    System.err.println( "Exception " + e.toString() + "Caught" );
                    e.printStackTrace();
                }
            } else {
                ClientUtil.displayAlert("Enter From Date...");
            }
        } else {
            ClientUtil.displayAlert("Select Product Type...");
        }        
        viewMap = null;
        whereMap = null;
    }
    
    private Date getProperDate(Date sourceDate) {
        Date targetDate = (Date) currDt.clone();
        targetDate.setDate(sourceDate.getDate());
        targetDate.setMonth(sourceDate.getMonth());
        targetDate.setYear(sourceDate.getYear());
        return targetDate;
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
    
//    public void setVisible(boolean visible) {
//        if (observable.isAvailable()) {
//            super.setVisible(visible);
//        }
//    }
    
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
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        panMultiSearch = new com.see.truetransact.uicomponent.CPanel();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        panAcctNo = new com.see.truetransact.uicomponent.CPanel();
        txtAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        lblAccNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        panFind = new com.see.truetransact.uicomponent.CPanel();
        btnView = new com.see.truetransact.uicomponent.CButton();
        btnIntDet = new com.see.truetransact.uicomponent.CButton();
        btnrecurrView = new com.see.truetransact.uicomponent.CButton();
        btnCollDt = new com.see.truetransact.uicomponent.CButton();
        btnStopPaymentView = new com.see.truetransact.uicomponent.CButton();
        btnDivDet = new com.see.truetransact.uicomponent.CButton();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(750, 675));
        setPreferredSize(new java.awt.Dimension(850, 675));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(700, 280));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(700, 280));
        panMultiSearch.setLayout(new java.awt.GridBagLayout());

        panMultiSearch.setBorder(new javax.swing.border.EtchedBorder());
        panMultiSearch.setMinimumSize(new java.awt.Dimension(450, 278));
        panMultiSearch.setPreferredSize(new java.awt.Dimension(450, 278));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 5);
        panSearchCondition.add(panMultiSearch, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 6, 4, 6);
        panSearchCondition.add(lblProdType, gridBagConstraints);

        cboProdType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(130);
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(18, 4, 4, 6);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSearchCondition.add(cboProdType, gridBagConstraints);

        lblAccNo.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        panSearchCondition.add(lblAccNo, gridBagConstraints);

        panAcctNo.setLayout(new java.awt.GridBagLayout());

        panAcctNo.setMinimumSize(new java.awt.Dimension(121, 21));
        panAcctNo.setPreferredSize(new java.awt.Dimension(21, 200));
        txtAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccNoActionPerformed(evt);
            }
        });
        txtAccNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccNoFocusLost(evt);
            }
        });

        panAcctNo.add(txtAccNo, new java.awt.GridBagConstraints());

        btnAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnAccNo.setToolTipText("Account No.");
        btnAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNoActionPerformed(evt);
            }
        });

        panAcctNo.add(btnAccNo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(panAcctNo, gridBagConstraints);

        lblAccNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblAccNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        lblAccNameValue.setMaximumSize(new java.awt.Dimension(175, 21));
        lblAccNameValue.setMinimumSize(new java.awt.Dimension(175, 21));
        lblAccNameValue.setPreferredSize(new java.awt.Dimension(175, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        panSearchCondition.add(lblAccNameValue, gridBagConstraints);

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        panSearchCondition.add(lblFromDate, gridBagConstraints);

        tdtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        panSearchCondition.add(tdtFromDate, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        panSearchCondition.add(lblToDate, gridBagConstraints);

        tdtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        panSearchCondition.add(tdtToDate, gridBagConstraints);

        panFind.setLayout(new java.awt.GridBagLayout());

        panFind.setBorder(new javax.swing.border.TitledBorder("Press to view"));
        panFind.setMinimumSize(new java.awt.Dimension(260, 125));
        panFind.setPreferredSize(new java.awt.Dimension(260, 130));
        btnView.setText("Transaction Details");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });

        panFind.add(btnView, new java.awt.GridBagConstraints());

        btnIntDet.setText("Interest Details");
        btnIntDet.setMaximumSize(new java.awt.Dimension(145, 27));
        btnIntDet.setMinimumSize(new java.awt.Dimension(145, 27));
        btnIntDet.setPreferredSize(new java.awt.Dimension(145, 27));
        btnIntDet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntDetActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 0);
        panFind.add(btnIntDet, gridBagConstraints);

        btnrecurrView.setText("RD Transaction Details");
        btnrecurrView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnrecurrViewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        panFind.add(btnrecurrView, gridBagConstraints);

        btnCollDt.setText("Collection Details");
        btnCollDt.setMaximumSize(new java.awt.Dimension(145, 27));
        btnCollDt.setMinimumSize(new java.awt.Dimension(145, 27));
        btnCollDt.setPreferredSize(new java.awt.Dimension(145, 27));
        btnCollDt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCollDtActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        panFind.add(btnCollDt, gridBagConstraints);

        btnStopPaymentView.setText("Stop Payment Details");
        btnStopPaymentView.setMinimumSize(new java.awt.Dimension(181, 27));
        btnStopPaymentView.setPreferredSize(new java.awt.Dimension(181, 27));
        btnStopPaymentView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopPaymentViewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        panFind.add(btnStopPaymentView, gridBagConstraints);

        btnDivDet.setText("Divident Details");
        btnDivDet.setMaximumSize(new java.awt.Dimension(145, 27));
        btnDivDet.setMinimumSize(new java.awt.Dimension(145, 27));
        btnDivDet.setPreferredSize(new java.awt.Dimension(145, 27));
        btnDivDet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDivDetActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 0);
        panFind.add(btnDivDet, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 6, 3);
        panSearchCondition.add(panFind, gridBagConstraints);

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

        panTable.setLayout(new java.awt.GridBagLayout());

        srcTable.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14));
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
        tblData.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 13));
        srcTable.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(srcTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panTable, gridBagConstraints);

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

    private void btnStopPaymentViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopPaymentViewActionPerformed
        // TODO add your handling code here:
        observable.StopPaymentView = true;
        populateData();
        observable.StopPaymentView = false;
    }//GEN-LAST:event_btnStopPaymentViewActionPerformed


    private void btnDivDetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDivDetActionPerformed
        // TODO add your handling code here:
         btnDividendPressed = true;
         populateData();
         btnDividendPressed = false;
    }//GEN-LAST:event_btnDivDetActionPerformed


    private void btnCollDtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCollDtActionPerformed
        // TODO add your handling code here:
        collDet=true;
        populateData();
        collDet=false;
    }//GEN-LAST:event_btnCollDtActionPerformed

    private void btnrecurrViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnrecurrViewActionPerformed
        // TODO add your handling code here:
        observable.btnRDTransPressed = true;
        populateData();
        observable.btnRDTransPressed = false;
    }//GEN-LAST:event_btnrecurrViewActionPerformed

    boolean btnIntDetPressed = false;
    private void btnIntDetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIntDetActionPerformed
        // TODO add your handling code here:
        btnIntDetPressed = true;
        populateData();
        btnIntDetPressed = false;
    }//GEN-LAST:event_btnIntDetActionPerformed

    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        System.out.println("#$#$ Hash : "+hash);
        txtAccNo.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
        txtAccNoFocusLost(null);
    }
    
    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
        // TODO add your handling code here:
        if(cboProdType.getSelectedIndex()>0){
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        whereMap.put(CommonConstants.PRODUCT_TYPE, observable.getProdType());
        viewMap.put(CommonConstants.MAP_NAME, "Enquiry.getAccountList");
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this, viewMap).show();
        }else{
            ClientUtil.showAlertWindow("Product Type should not be empty");
            return;
        }
    }//GEN-LAST:event_btnAccNoActionPerformed

    private void txtAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccNoFocusLost
        // TODO add your handling code here:
        if (observable.getProdType().length()>0 && txtAccNo.getText().length()>0) {
            if(!txtAccNo.getText().equals(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID))){
            String actNum = txtAccNo.getText();
            if(actNum !=null && actNum.length()>0){
                observable.asAnWhenCustomerComesYesNO(actNum,null);
            }
            if((observable.getProdType().equals("TL") || observable.getProdType().equals("AD")) && (actNum!=null && actNum.length()>0)
            && observable.getLinkMap()!=null && observable.getLinkMap().containsKey("AS_CUSTOMER_COMES") &&
            observable.getLinkMap().get("AS_CUSTOMER_COMES")!=null && observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y")) {
                
                HashMap asAndWhenMap =  interestCalculationTLAD(actNum);
                if(asAndWhenMap!=null && asAndWhenMap.size()>0)
                    transDetails.setAsAndWhenMap(asAndWhenMap);
            }else{
                observable.setLinkMap(new HashMap());
            }
            if(observable.getProdType().equals("TD")){
                if (txtAccNo.getText().length()>0 && txtAccNo.getText().lastIndexOf("_")==-1) {
                    actNum = txtAccNo.getText();
                    txtAccNo.setText(txtAccNo.getText()+"_1");
                } else {
                    actNum = txtAccNo.getText();
                    actNum = txtAccNo.getText().substring(0, actNum.lastIndexOf("_"));
                    System.out.println("#$#$#$#$ actNum "+actNum);
                }
                
                HashMap where = new HashMap();
                where.put("ACT_NUM",actNum);
                List lst = ClientUtil.executeQuery("getBehavesLikeForDepositNo", where);
                if (lst!=null && lst.size()>0) {
                    behavesLike = CommonUtil.convertObjToStr(((HashMap)lst.get(0)).get("BEHAVES_LIKE"));
                    if (behavesLike.equals("DAILY"))
                        btnCollDt.setVisible(true);
                    else
                        btnCollDt.setVisible(false);
                    
                    HashMap behavesLikeMap = new HashMap();
                    behavesLikeMap.put("DEPOSIT_NO",actNum);
                    lst = ClientUtil.executeQuery("getStatusForDeposit",  behavesLikeMap);
                    if(lst != null && lst.size() > 0) {
                        behavesLikeMap = (HashMap)lst.get(0);
                        lst = ClientUtil.executeQuery("getBehavesLikeForDeposit",  behavesLikeMap);
                        if(lst != null && lst.size() > 0) {
                            behavesLikeMap = (HashMap)lst.get(0);
                            if(behavesLikeMap.get("BEHAVES_LIKE").equals("RECURRING")){
                                btnrecurrView.setVisible(true);
                                btnrecurrView.setEnabled(true);
                            }else{
                                btnrecurrView.setVisible(false);
                                btnrecurrView.setEnabled(false);
                            }
                        }
                        
                    }
                }
            } else{
                btnrecurrView.setVisible(false);
                btnrecurrView.setEnabled(false);
                btnCollDt.setVisible(false);
            }
            
            if(observable.getProdType().equals("OA")){
		HashMap chequeMap = new HashMap();
                chequeMap.put("ACT_NUM",txtAccNo.getText());
                List lst = ClientUtil.executeQuery("getSelectStopPaymentDetails",  chequeMap);
                if(lst != null && lst.size() > 0) {
                    chequeMap = (HashMap)lst.get(0);                    
                    btnStopPaymentView.setVisible(true);
                    btnStopPaymentView.setEnabled(true);
                }else{
                    btnStopPaymentView.setVisible(false);
                    btnStopPaymentView.setEnabled(false);
                }
            }else{
                btnStopPaymentView.setVisible(false);
                btnStopPaymentView.setEnabled(false);
            }
            observable.setTxtAccNo(txtAccNo.getText());
            transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, observable.getTxtAccNo());
            observable.setAccountName();
            lblAccNameValue.setText(observable.getLblAccNameValue());
            if (observable.getLblAccNameValue().length()<=0) {
                ClientUtil.displayAlert("Invalid Account No.");
                lblAccNameValue.setText("");
                txtAccNo.setText("");
                    //Added By Suresh
                    if(!observable.getProdType().equals("GL")){
                        txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
                    }
                }
            }
        } else {
            ClientUtil.displayAlert("Enter Product Type");
            btnClearActionPerformed(null);
            txtAccNo.setText("");
        }
        
    }//GEN-LAST:event_txtAccNoFocusLost

    private void txtAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccNoActionPerformed
        // TODO add your handling code here:
        String actNum = txtAccNo.getText();
        if(actNum !=null && actNum.length()>0){
            observable.asAnWhenCustomerComesYesNO(actNum,null);
        }
        if((observable.getProdType().equals("TL") || observable.getProdType().equals("AD")) && (actNum!=null && actNum.length()>0)
        && observable.getLinkMap()!=null && observable.getLinkMap().containsKey("AS_CUSTOMER_COMES") &&
        observable.getLinkMap().get("AS_CUSTOMER_COMES")!=null && observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y")) {
            
            HashMap asAndWhenMap =  interestCalculationTLAD(actNum);
            if(asAndWhenMap!=null && asAndWhenMap.size()>0)
                transDetails.setAsAndWhenMap(asAndWhenMap);
        }else{
            observable.setLinkMap(new HashMap());
        }
        if(observable.getProdType().equals("TD")){
            HashMap behavesLikeMap = new HashMap();
            behavesLikeMap.put("DEPOSIT_NO",txtAccNo.getText());
            List lst = ClientUtil.executeQuery("getStatusForDeposit",  behavesLikeMap);
            if(lst != null && lst.size() > 0) {
                behavesLikeMap = (HashMap)lst.get(0);
                lst = ClientUtil.executeQuery("getBehavesLikeForDeposit",  behavesLikeMap);
                if(lst != null && lst.size() > 0) {
                    behavesLikeMap = (HashMap)lst.get(0);
                    if(behavesLikeMap.get("BEHAVES_LIKE").equals("RECURRING")){
                        btnrecurrView.setVisible(true);
                        btnrecurrView.setEnabled(true);
                    }else{
                        btnrecurrView.setVisible(false);
                        btnrecurrView.setEnabled(false);
                    }
                }
            }
        }else{
            btnrecurrView.setVisible(false);
            btnrecurrView.setEnabled(false);
        }
        if (observable.getProdType().equals("TD")) {
            //            String actNum = txtAccNo.getText();
            if (txtAccNo.getText().length()>0 && txtAccNo.getText().lastIndexOf("_")==-1) {
                actNum = txtAccNo.getText();
                txtAccNo.setText(txtAccNo.getText()+"_1");
            } else {
                actNum = txtAccNo.getText();
                actNum = txtAccNo.getText().substring(0, actNum.lastIndexOf("_"));
                System.out.println("#$#$#$#$ actNum "+actNum);
            }
            HashMap where = new HashMap();
            where.put("ACT_NUM",actNum);
            List lst = ClientUtil.executeQuery("getBehavesLikeForDepositNo", where);
            if (lst!=null && lst.size()>0) {
                behavesLike = CommonUtil.convertObjToStr(((HashMap)lst.get(0)).get("BEHAVES_LIKE"));
                if (behavesLike.equals("DAILY"))
                    btnIntDet.setVisible(false);
                else
                    btnIntDet.setVisible(true);
            }
            lst = null;
        }
        observable.setTxtAccNo(txtAccNo.getText());
        transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, observable.getTxtAccNo());
        observable.setAccountName();
        lblAccNameValue.setText(observable.getLblAccNameValue());
    }//GEN-LAST:event_txtAccNoActionPerformed
 private HashMap interestCalculationTLAD(String accountNo){
            HashMap map=new HashMap();
            HashMap hash=null;
            try{
                
                map.put("ACT_NUM",accountNo);
                map.put("ACCT_NUM",accountNo);
                 List lst=ClientUtil.executeQuery("TermLoan.getBehavesLike", map);
                 String prod_id=null;
                 if(lst!=null && lst.size()>0){
                     HashMap prodMap=(HashMap)lst.get(0);
                     prod_id=CommonUtil.convertObjToStr(prodMap.get("PROD_ID"));
                 }
//                String prod_id=((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString();
                map.put("PROD_ID",prod_id);
                map.put("TRANS_DT", currDt);
                map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                lst=ClientUtil.executeQuery("IntCalculationDetail", map);
                if(lst==null || lst.isEmpty()) {
                    lst=ClientUtil.executeQuery("IntCalculationDetailAD", map);
                }
                if(lst !=null && lst.size()>0){
                    hash=(HashMap)lst.get(0);
                    if(hash.get("AS_CUSTOMER_COMES")!=null  && hash.get("AS_CUSTOMER_COMES").equals("N")){
                        hash=new HashMap();
                        return hash;
                    }
                    map.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
                    map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                    
                    //                    InterestCalculationTask interestcalTask=new InterestCalculationTask(header);
                    map.putAll(hash);
                    map.put("LOAN_ACCOUNT_CLOSING","LOAN_ACCOUNT_CLOSING");
                    map.put("CURR_DATE",ClientUtil.getCurrentDateProperFormat());
                    System.out.println("map before intereest###"+map);
                    //                    hash =interestcalTask.interestCalcTermLoanAD(map);
                    hash=observable.loanInterestCalculationAsAndWhen(map);
                    
                    System.out.println("hashinterestoutput###"+hash);
                    hash.put("AS_CUSTOMER_COMES",map.get("AS_CUSTOMER_COMES"));
                }
                
            }
            catch(Exception e){
                e.printStackTrace();
            }
            
            return hash;
        }
    private void tdtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateFocusLost
        // TODO add your handling code here:
        observable.setTdtToDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
        java.util.Date from = observable.getTdtFromDate();
        java.util.Date to = observable.getTdtToDate();
        if (from!=null && to!=null && DateUtil.dateDiff(from,to)<0) {
            displayAlert("To date should be greater than From Date...");
            tdtToDate.setDateValue("");
            tdtToDate.requestFocus();
        }
    }//GEN-LAST:event_tdtToDateFocusLost

    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost
        // TODO add your handling code here:
        observable.setTdtFromDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
//        tdtToDate.setDateValue("");
    }//GEN-LAST:event_tdtFromDateFocusLost

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        btnStopPaymentView.setVisible(false);
        System.out.println("#$#$ prodType : "+((ComboBoxModel)cboProdType.getModel()).getKeyForSelected());
        observable.setProdType(CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected()));
        behavesLike = "";
        if (observable.getProdType().equals("TD") ||observable.getProdType().equals("TL")|| observable.getProdType().equals("ATL") || observable.getProdType().equals("AD") || observable.getProdType().equals("AAD")) {
            btnIntDet.setVisible(true);
        } else {
            btnIntDet.setVisible(false);
            
        }
        if(observable.getProdType().equals("TD"))
            btnCollDt.setVisible(true);
        else
            btnCollDt.setVisible(false);
        
        if(observable.getProdType().equals("SH"))
            btnDivDet.setVisible(true);
        else
            btnDivDet.setVisible(false);
        if(!observable.getProdType().equals("GL")){
            txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
        }else{
            txtAccNo.setText("");
        }
            
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
            
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        populateData();
    }//GEN-LAST:event_btnViewActionPerformed

//    public void calculateTot() {
////        int colcnt = tblData.getColumnCount();
////        for (int c=0; c<colcnt; c++) {
////            amtColName = tblData.getColumnName(c);
////            if (amtColName.equals("AMOUNT")) {
////                amtColPos = c;
////                break;
////            }
////        }
//        
//        int rowcnt = tblData.getRowCount();
//        int colcnt = tblData.getColumnCount();
//        long tot = 0;
////        System.out.println("#### rows : "+rows);
//        System.out.println("#### rowcnt : "+rowcnt);
//        String colName="";
//        for (int i=0; i<colcnt; i++) {
//            colName = tblData.getColumnName(i);
//            if (colName.equals("AMOUNT")) {
//                amtColumnNo = i;
//                break;
//            }
//        }        
//        for (int i=0; i<rowcnt; i++) {
//            tot = tot + new Long(tblData.getValueAt(i, amtColumnNo).toString()).longValue();
////            System.out.println("#### tot : "+tot);
//            lblAllTransValue.setText(String.valueOf(tot));            
//        }        
//    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        btnClearActionPerformed(null);
        cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        observable.resetForm();
        update(observable, null);
        behavesLike = "";
        transDetails.setTransDetails(null,null,null);
        lblAccNameValue.setText("");
    }//GEN-LAST:event_btnClearActionPerformed
    
        private ComboBoxModel getListModel() {
        ComboBoxModel listData = new ComboBoxModel();
        return listData;
    }                            
        
        private void internationalize() {
//            lblSearch.setText(resourceBundle.getString("lblSearch"));
//            btnSearch.setText(resourceBundle.getString("btnSearch"));
//            chkCase.setText(resourceBundle.getString("chkCase"));
//            btnOk.setText(resourceBundle.getString("btnOk"));
//            btnCancel.setText(resourceBundle.getString("btnCancel"));
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
            txtAccNo.setText(observable.getTxtAccNo());
            tdtFromDate.setDateValue(DateUtil.getStringDate(observable.getTdtFromDate()));
            tdtToDate.setDateValue(DateUtil.getStringDate(observable.getTdtToDate()));
        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCollDt;
    private com.see.truetransact.uicomponent.CButton btnDivDet;
    private com.see.truetransact.uicomponent.CButton btnIntDet;
    private com.see.truetransact.uicomponent.CButton btnStopPaymentView;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnrecurrView;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CLabel lblAccNameValue;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CPanel panAcctNo;
    private com.see.truetransact.uicomponent.CPanel panFind;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAndOr;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtAccNo;
    // End of variables declaration//GEN-END:variables
}

