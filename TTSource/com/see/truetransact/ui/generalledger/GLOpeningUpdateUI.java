/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GLOpeningUpdateUI.java
 *
 * Created on October 16, 2009, 1:46 PM
 */
package com.see.truetransact.ui.generalledger;
//import com.see.tools.workflow.util.Component;
import com.lowagie.text.Font;
import com.see.truetransact.ui.supporting.inventory.*;

import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;
import java.awt.Dimension;
import java.awt.Toolkit;
import org.apache.log4j.Logger;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import javax.swing.table.DefaultTableModel;
import java.util.Enumeration;
import java.text.SimpleDateFormat;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import com.see.truetransact.ui.common.viewall.RejectionApproveUI;
import com.see.truetransact.uimandatory.UIMandatoryField;
import java.text.DateFormat;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.interestcalc.CommonCalculateInterest;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;

import com.see.truetransact.ui.common.viewall.RejectionApproveUI;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CLabel;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.awt.Color;
//import java.awt.font;
import java.awt.Component;
import javax.resource.spi.CommException;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import com.see.truetransact.clientutil.ComboBoxModel;
//import java.awt.Color;
//import javax.swing.JTable;
//import java.awt.Component;
//import javax.swing.table.DefaultTableCellRenderer;


/**
 * @author Swaroop
 */
public class GLOpeningUpdateUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {

    private GLOpeningUpdateOB observable;
    boolean collDet = false;
    DefaultTableModel model = null;
    Date currDt = null;
    ArrayList _heading = null;
    ArrayList data = null;
    ArrayList colorList = new ArrayList();
   // private EnhancedTableModel tmbBalanceUpdate;
    String node = "";
    List lst1;
    List lst2;
    RejectionApproveUI rejectionApproveUI = null;
    private final static Logger log = Logger.getLogger(GLOpeningUpdateUI.class);
    final int EDIT=0, DELETE=1, AUTHORIZE=2, VIEW =3;
    int viewType=-1;
    private String viewType1 = new String();
    boolean isFilled = false;
    private TableModelListener tableModelListener;
    static com.see.truetransact.uicomponent.CComboBox cboBalType = new com.see.truetransact.uicomponent.CComboBox();
    private ComboBoxModel cbmBalType;

    public ComboBoxModel getCbmBalType() {
        return cbmBalType;
    }

    public void setCbmBalType(ComboBoxModel cbmBalType) {
        this.cbmBalType = cbmBalType;
    }
    private ArrayList key;
    private ArrayList value;
    //ProcessUI pUi;
    
    public GLOpeningUpdateUI() {       
        currDt = ClientUtil.getCurrentDate();
        settingupUI();
        setupInit();
        setupScreen();
        btnNew.setEnabled(true);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
        btnItemLst.setVisible(false);   
        btnClear.setEnabled(false);  
        btnProcess.setEnabled(false); 
        btnAcHd.setEnabled(false); 
        txtAcHD.setEnabled(false); 
        ClientUtil.enableDisable(this, false);
        tabDCDayBegin.remove(panBatchProcess1);
        panTable.setPreferredSize(new Dimension(500, 500));
        setSize(new Dimension(1000, 1000));
        setSizeTableData();
        btnRollBack.setEnabled(false);
        fillCombo();
        //setUpComboBox(tblBalanceUpdate,tblBalanceUpdate.getColumnModel().getColumn(2));
    }
    private void settingupUI() {
        // initComponentData();
        
    }
    private void setupInit() {
        initComponents();
        //internationalize();
        setObservable();
        toFront();
        observable.fillDropDown();
        cboBranch.setModel(observable.getCbmbranch());
        cboBranchFinal.setModel(observable.getCbmbranch());
    }
    
    public void fillCombo(){
        key = new ArrayList();
        value = new ArrayList();
        value.add("CREDIT");
        value.add("DEBIT");
        key.add("CREDIT");
        key.add("DEBIT");
        cbmBalType = new ComboBoxModel(key,value);
        cboBalType.setModel(getCbmBalType());
        
    }
    private void setupScreen() {
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
            observable = new GLOpeningUpdateOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        System.out.println("hash filldata#####"+hash);
        if(viewType1 == "AC_HD"){
           String acHd = CommonUtil.convertObjToStr(hash.get("AC_HD_CODE"));
           txtAcHD.setText(acHd);
           observable.setGlAcHd(acHd);
           System.out.println("observable.GetGlAcHd(acHd)#####"+observable.getGlAcHd());
        }
        if (viewType == AUTHORIZE || viewType == VIEW) {
            isFilled = true;
            hash.put(CommonConstants.MAP_WHERE, hash.get("BAL_SHEET_ID"));
            observable.setBalSheetId(CommonUtil.convertObjToStr(hash.get("BAL_SHEET_ID")));
            // observable.populateData(hash);
            btnProcess.setEnabled(false);
            btnFinalProcess.setEnabled(false);
            if (tabDCDayBegin.getSelectedIndex() == 0) {
                initTableAuthData();
            }
            if (tabDCDayBegin.getSelectedIndex() == 1) {
                if(hash.get("FINAL_ACCOUNT_TYPE")!=null && hash.get("FINAL_ACCOUNT_TYPE").equals("BALANCE SHEET"))
                initFinalProcessAuthTableData("Liabilities","Asset",hash.get("FINAL_ACCOUNT_TYPE").toString());
                else
                  initFinalProcessAuthTableData("Expenditure","Income",hash.get("FINAL_ACCOUNT_TYPE").toString()); 
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType == AUTHORIZE || viewType == VIEW) {
                ClientUtil.enableDisable(this, false);     // Disables the panel...
            } else {
                if (hash.get("AUTHORIZE_STATUS").equals("AUTHORIZED")) {
                    ClientUtil.enableDisable(this, false);     // Enables the panel...
                } else {
                    ClientUtil.enableDisable(this, true);
                }
                //                setFieldsEnable(false);
            }

            setButtonEnableDisable();         // Enables or Disables the buttons and menu Items depending on their previous state...
            if (viewType == AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
            //__ To Save the data in the Internal Frame...
            if (hash.containsKey("AUTHORIZE_STATUS")) {
                if (hash.get("AUTHORIZE_STATUS").equals("AUTHORIZED")) {
                    btnSave.setEnabled(false);
                }
            }
            setModified(true);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgAndOr = new com.see.truetransact.uicomponent.CButtonGroup();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        tabDCDayBegin = new com.see.truetransact.uicomponent.CTabbedPane();
        panMainSI = new com.see.truetransact.uicomponent.CPanel();
        panSI = new com.see.truetransact.uicomponent.CPanel();
        panSIIDt = new com.see.truetransact.uicomponent.CPanel();
        panSIID = new com.see.truetransact.uicomponent.CPanel();
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblBalanceUpdate = new com.see.truetransact.uicomponent.CTable();
        lblTotal = new javax.swing.JLabel();
        txtOpenTotal = new javax.swing.JTextField();
        txtClosTotal = new com.see.truetransact.uicomponent.CTextField();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        txtDifTotal = new com.see.truetransact.uicomponent.CTextField();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        txtNewTotal = new com.see.truetransact.uicomponent.CTextField();
        panSearchCondition1 = new com.see.truetransact.uicomponent.CPanel();
        cPanel4 = new com.see.truetransact.uicomponent.CPanel();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        cboBranch = new com.see.truetransact.uicomponent.CComboBox();
        jPanel1 = new javax.swing.JPanel();
        lblBalance = new com.see.truetransact.uicomponent.CLabel();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        tdtFrmDt = new com.see.truetransact.uicomponent.CDateField();
        cLabel4 = new com.see.truetransact.uicomponent.CLabel();
        txtAcHD = new com.see.truetransact.uicomponent.CTextField();
        btnAcHd = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        lblCount = new com.see.truetransact.uicomponent.CLabel();
        lblNo = new com.see.truetransact.uicomponent.CLabel();
        btnReport = new com.see.truetransact.uicomponent.CButton();
        chkAsset = new com.see.truetransact.uicomponent.CCheckBox();
        chkLiability = new com.see.truetransact.uicomponent.CCheckBox();
        btnRollBack = new com.see.truetransact.uicomponent.CButton();
        panBatchProcess1 = new com.see.truetransact.uicomponent.CPanel();
        cPanel5 = new com.see.truetransact.uicomponent.CPanel();
        lblDate1 = new com.see.truetransact.uicomponent.CLabel();
        cboBranchFinal = new com.see.truetransact.uicomponent.CComboBox();
        jPanel2 = new javax.swing.JPanel();
        lblFinalAccountType1 = new com.see.truetransact.uicomponent.CLabel();
        lblBalance1 = new com.see.truetransact.uicomponent.CLabel();
        cboFinalPrcessType = new com.see.truetransact.uicomponent.CComboBox();
        btnFinalProcess = new com.see.truetransact.uicomponent.CButton();
        tdtFinalDate = new com.see.truetransact.uicomponent.CDateField();
        panSearchCondition2 = new com.see.truetransact.uicomponent.CPanel();
        panTable1 = new com.see.truetransact.uicomponent.CPanel();
        cScrollPane2 = new com.see.truetransact.uicomponent.CScrollPane();
        tblBalanceSheetPreparation = new com.see.truetransact.uicomponent.CTable();
        lblTotExp = new com.see.truetransact.uicomponent.CLabel();
        lblTotalIncome = new com.see.truetransact.uicomponent.CLabel();
        txtTotalExp = new com.see.truetransact.uicomponent.CTextField();
        txtToatalIncome = new com.see.truetransact.uicomponent.CTextField();
        lblNetProfit = new com.see.truetransact.uicomponent.CLabel();
        txtNetProfit = new com.see.truetransact.uicomponent.CTextField();
        lblNetloss = new com.see.truetransact.uicomponent.CLabel();
        txtNetLoss = new com.see.truetransact.uicomponent.CTextField();
        panSearchCondition3 = new com.see.truetransact.uicomponent.CPanel();
        tbrLoantProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace11 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace12 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace13 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace14 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace15 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace16 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnItemLst = new com.see.truetransact.uicomponent.CButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(975, 680));
        setMinimumSize(new java.awt.Dimension(975, 680));
        setPreferredSize(new java.awt.Dimension(975, 680));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 812;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 72, 0, 0);
        getContentPane().add(sptLine, gridBagConstraints);

        tabDCDayBegin.setMinimumSize(new java.awt.Dimension(890, 524));

        panMainSI.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panMainSI.setMaximumSize(new java.awt.Dimension(750, 500));
        panMainSI.setMinimumSize(new java.awt.Dimension(652, 371));
        panMainSI.setPreferredSize(new java.awt.Dimension(700, 600));
        panMainSI.setLayout(null);

        panSI.setLayout(new java.awt.GridBagLayout());
        panMainSI.add(panSI);
        panSI.setBounds(0, 0, 0, 0);

        panSIIDt.setLayout(new java.awt.GridBagLayout());
        panMainSI.add(panSIIDt);
        panSIIDt.setBounds(0, 0, 0, 0);

        panSIID.setLayout(new java.awt.GridBagLayout());
        panMainSI.add(panSIID);
        panSIID.setBounds(0, 0, 0, 0);

        panSearchCondition.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSearchCondition.setMinimumSize(new java.awt.Dimension(700, 600));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(700, 600));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        panTable.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        panTable.setMinimumSize(new java.awt.Dimension(980, 250));
        panTable.setPreferredSize(new java.awt.Dimension(980, 250));
        panTable.setLayout(null);

        cScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        cScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        cScrollPane1.setMaximumSize(new java.awt.Dimension(469, 302));
        cScrollPane1.setMinimumSize(new java.awt.Dimension(469, 302));
        cScrollPane1.setPreferredSize(new java.awt.Dimension(469, 302));

        tblBalanceUpdate.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account ID", "Account Head", "Opening Balance", "Closing Balance", "New Closing Balance", "Difference", "Head Type"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        cScrollPane1.setViewportView(tblBalanceUpdate);

        panTable.add(cScrollPane1);
        cScrollPane1.setBounds(10, 11, 921, 368);

        lblTotal.setText("AssetTotal");
        panTable.add(lblTotal);
        lblTotal.setBounds(20, 400, 80, 20);

        txtOpenTotal.setEditable(false);
        txtOpenTotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtOpenTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOpenTotalActionPerformed(evt);
            }
        });
        panTable.add(txtOpenTotal);
        txtOpenTotal.setBounds(80, 400, 140, 25);

        txtClosTotal.setBackground(new java.awt.Color(212, 208, 200));
        txtClosTotal.setEditable(false);
        txtClosTotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtClosTotal.setMinimumSize(new java.awt.Dimension(6, 20));
        panTable.add(txtClosTotal);
        txtClosTotal.setBounds(310, 400, 140, 25);

        cLabel1.setText("LiabilityTotal");
        panTable.add(cLabel1);
        cLabel1.setBounds(230, 400, 80, 18);

        cLabel2.setText("Difference Total");
        panTable.add(cLabel2);
        cLabel2.setBounds(700, 400, 100, 18);

        txtDifTotal.setBackground(new java.awt.Color(212, 208, 200));
        txtDifTotal.setEditable(false);
        txtDifTotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        panTable.add(txtDifTotal);
        txtDifTotal.setBounds(790, 400, 140, 25);

        cLabel3.setText("New Bal Total");
        panTable.add(cLabel3);
        cLabel3.setBounds(460, 400, 90, 18);

        txtNewTotal.setBackground(new java.awt.Color(212, 208, 200));
        txtNewTotal.setEditable(false);
        txtNewTotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        panTable.add(txtNewTotal);
        txtNewTotal.setBounds(550, 400, 140, 25);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = -40;
        gridBagConstraints.ipady = 190;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 17, 10);
        panSearchCondition.add(panTable, gridBagConstraints);

        panSearchCondition1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panSearchCondition.add(panSearchCondition1, gridBagConstraints);

        cPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cPanel4.setMaximumSize(new java.awt.Dimension(260, 260));
        cPanel4.setMinimumSize(new java.awt.Dimension(260, 260));
        cPanel4.setPreferredSize(new java.awt.Dimension(260, 260));
        cPanel4.setLayout(null);

        lblDate.setText(" Date");
        lblDate.setMaximumSize(new java.awt.Dimension(115, 21));
        lblDate.setMinimumSize(new java.awt.Dimension(115, 21));
        lblDate.setPreferredSize(new java.awt.Dimension(115, 21));
        cPanel4.add(lblDate);
        lblDate.setBounds(110, 50, 60, 21);

        cboBranch.setMaximumSize(new java.awt.Dimension(200, 21));
        cboBranch.setMinimumSize(new java.awt.Dimension(200, 21));
        cboBranch.setOpaque(false);
        cboBranch.setPreferredSize(new java.awt.Dimension(200, 21));
        cPanel4.add(cboBranch);
        cboBranch.setBounds(170, 20, 200, 21);

        jPanel1.setLayout(new java.awt.GridBagLayout());
        cPanel4.add(jPanel1);
        jPanel1.setBounds(0, 0, 0, 0);

        lblBalance.setText("Branch");
        lblBalance.setMaximumSize(new java.awt.Dimension(115, 21));
        lblBalance.setMinimumSize(new java.awt.Dimension(115, 21));
        lblBalance.setPreferredSize(new java.awt.Dimension(115, 21));
        cPanel4.add(lblBalance);
        lblBalance.setBounds(110, 20, 70, 21);

        btnProcess.setText("Display");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        cPanel4.add(btnProcess);
        btnProcess.setBounds(430, 70, 100, 27);

        tdtFrmDt.setMaximumSize(new java.awt.Dimension(101, 21));
        cPanel4.add(tdtFrmDt);
        tdtFrmDt.setBounds(170, 50, 101, 21);

        cLabel4.setText("GL Head");
        cPanel4.add(cLabel4);
        cLabel4.setBounds(490, 20, 70, 18);
        cPanel4.add(txtAcHD);
        txtAcHD.setBounds(560, 20, 130, 24);

        btnAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAcHd.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAcHd.setMinimumSize(new java.awt.Dimension(31, 23));
        btnAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcHdActionPerformed(evt);
            }
        });
        cPanel4.add(btnAcHd);
        btnAcHd.setBounds(700, 20, 40, 22);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        cPanel4.add(btnClear);
        btnClear.setBounds(550, 70, 40, 29);

        lblCount.setForeground(new java.awt.Color(0, 0, 153));
        lblCount.setText("No.Of Heads  :");
        lblCount.setFont(new java.awt.Font("MS Sans Serif", 1, 14)); // NOI18N
        cPanel4.add(lblCount);
        lblCount.setBounds(650, 80, 110, 19);

        lblNo.setForeground(new java.awt.Color(0, 0, 153));
        lblNo.setFont(new java.awt.Font("MS Sans Serif", 1, 14)); // NOI18N
        cPanel4.add(lblNo);
        lblNo.setBounds(760, 80, 100, 20);

        btnReport.setText("Updated Heads");
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportActionPerformed(evt);
            }
        });
        cPanel4.add(btnReport);
        btnReport.setBounds(770, 20, 140, 27);

        chkAsset.setText("Asset");
        cPanel4.add(chkAsset);
        chkAsset.setBounds(110, 80, 59, 27);

        chkLiability.setText("Liability");
        cPanel4.add(chkLiability);
        chkLiability.setBounds(220, 80, 69, 27);

        btnRollBack.setText("Roll Back");
        btnRollBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRollBackActionPerformed(evt);
            }
        });
        btnRollBack.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btnRollBackFocusGained(evt);
            }
        });
        cPanel4.add(btnRollBack);
        btnRollBack.setBounds(820, 50, 90, 27);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 660;
        gridBagConstraints.ipady = -150;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        panSearchCondition.add(cPanel4, gridBagConstraints);

        panMainSI.add(panSearchCondition);
        panSearchCondition.setBounds(0, 0, 960, 577);

        tabDCDayBegin.addTab("GL Opening", panMainSI);

        panBatchProcess1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panBatchProcess1.setMinimumSize(new java.awt.Dimension(740, 624));
        panBatchProcess1.setName(""); // NOI18N
        panBatchProcess1.setPreferredSize(new java.awt.Dimension(740, 624));
        panBatchProcess1.setLayout(null);

        cPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cPanel5.setMaximumSize(new java.awt.Dimension(260, 260));
        cPanel5.setMinimumSize(new java.awt.Dimension(260, 260));
        cPanel5.setPreferredSize(new java.awt.Dimension(260, 260));
        cPanel5.setLayout(new java.awt.GridBagLayout());

        lblDate1.setText(" Date");
        lblDate1.setMaximumSize(new java.awt.Dimension(115, 21));
        lblDate1.setMinimumSize(new java.awt.Dimension(115, 21));
        lblDate1.setPreferredSize(new java.awt.Dimension(115, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        cPanel5.add(lblDate1, gridBagConstraints);

        cboBranchFinal.setMaximumSize(new java.awt.Dimension(200, 21));
        cboBranchFinal.setMinimumSize(new java.awt.Dimension(200, 21));
        cboBranchFinal.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        cPanel5.add(cboBranchFinal, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        lblFinalAccountType1.setText("Final Account Type");
        lblFinalAccountType1.setMaximumSize(new java.awt.Dimension(115, 21));
        lblFinalAccountType1.setMinimumSize(new java.awt.Dimension(115, 21));
        lblFinalAccountType1.setPreferredSize(new java.awt.Dimension(115, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        jPanel2.add(lblFinalAccountType1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        cPanel5.add(jPanel2, gridBagConstraints);

        lblBalance1.setText("Branch");
        lblBalance1.setMaximumSize(new java.awt.Dimension(115, 21));
        lblBalance1.setMinimumSize(new java.awt.Dimension(115, 21));
        lblBalance1.setPreferredSize(new java.awt.Dimension(115, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        cPanel5.add(lblBalance1, gridBagConstraints);

        cboFinalPrcessType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "TRADING", "PROFIT AND LOSS", "BALANCE SHEET" }));
        cboFinalPrcessType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboFinalPrcessType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboFinalPrcessType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFinalPrcessTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        cPanel5.add(cboFinalPrcessType, gridBagConstraints);

        btnFinalProcess.setText("Process");
        btnFinalProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(6, 19, 0, 0);
        cPanel5.add(btnFinalProcess, gridBagConstraints);

        tdtFinalDate.setMaximumSize(new java.awt.Dimension(101, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        cPanel5.add(tdtFinalDate, gridBagConstraints);

        panBatchProcess1.add(cPanel5);
        cPanel5.setBounds(2, 13, 948, 140);

        panSearchCondition2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSearchCondition2.setMaximumSize(new java.awt.Dimension(700, 600));
        panSearchCondition2.setMinimumSize(new java.awt.Dimension(700, 600));
        panSearchCondition2.setPreferredSize(new java.awt.Dimension(700, 600));
        panSearchCondition2.setLayout(null);

        panTable1.setMaximumSize(new java.awt.Dimension(500, 250));
        panTable1.setMinimumSize(new java.awt.Dimension(500, 250));
        panTable1.setPreferredSize(new java.awt.Dimension(500, 250));

        cScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        cScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        cScrollPane2.setMaximumSize(new java.awt.Dimension(469, 302));
        cScrollPane2.setMinimumSize(new java.awt.Dimension(469, 302));

        tblBalanceSheetPreparation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Iteam Head", "Account Head Desc", "Income", "Expendeture", "BalanceHead"
            }
        ));
        cScrollPane2.setViewportView(tblBalanceSheetPreparation);

        lblTotExp.setText("Total Income");

        lblTotalIncome.setText("Toatl Expenditure");

        txtTotalExp.setEditable(false);
        txtTotalExp.setEnabled(false);

        txtToatalIncome.setEnabled(false);

        lblNetProfit.setText("Net Profit");

        txtNetProfit.setEditable(false);
        txtNetProfit.setEnabled(false);

        lblNetloss.setText("Net Loss");

        txtNetLoss.setEditable(false);
        txtNetLoss.setEnabled(false);

        javax.swing.GroupLayout panTable1Layout = new javax.swing.GroupLayout(panTable1);
        panTable1.setLayout(panTable1Layout);
        panTable1Layout.setHorizontalGroup(
            panTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTable1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(cScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panTable1Layout.createSequentialGroup()
                .addContainerGap(399, Short.MAX_VALUE)
                .addGroup(panTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(panTable1Layout.createSequentialGroup()
                        .addComponent(lblNetProfit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtNetProfit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panTable1Layout.createSequentialGroup()
                        .addComponent(lblTotExp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalExp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panTable1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(lblTotalIncome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtToatalIncome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panTable1Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(lblNetloss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtNetLoss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(80, 80, 80))
        );
        panTable1Layout.setVerticalGroup(
            panTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTable1Layout.createSequentialGroup()
                .addComponent(cScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panTable1Layout.createSequentialGroup()
                        .addGroup(panTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNetProfit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNetProfit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panTable1Layout.createSequentialGroup()
                        .addGroup(panTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNetloss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNetLoss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addGroup(panTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotExp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalIncome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotalExp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtToatalIncome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panSearchCondition2.add(panTable1);
        panTable1.setBounds(10, 10, 920, 390);

        panSearchCondition3.setLayout(new java.awt.GridBagLayout());
        panSearchCondition2.add(panSearchCondition3);
        panSearchCondition3.setBounds(0, 0, 0, 0);

        panBatchProcess1.add(panSearchCondition2);
        panSearchCondition2.setBounds(10, 160, 940, 409);

        tabDCDayBegin.addTab("BalanceSheetPreparation", panBatchProcess1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 77;
        gridBagConstraints.ipady = 75;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        getContentPane().add(tabDCDayBegin, gridBagConstraints);

        tbrLoantProduct.setMaximumSize(new java.awt.Dimension(349, 27));
        tbrLoantProduct.setMinimumSize(new java.awt.Dimension(349, 27));
        tbrLoantProduct.setPreferredSize(new java.awt.Dimension(349, 27));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrLoantProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnNew);

        lblSpace11.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace11.setText("     ");
        lblSpace11.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace11);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        lblSpace12.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace12.setText("     ");
        lblSpace12.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace12);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrLoantProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnSave);

        lblSpace13.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace13.setText("     ");
        lblSpace13.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace13);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrLoantProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnAuthorize);

        lblSpace14.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace14.setText("     ");
        lblSpace14.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace14);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnException);

        lblSpace15.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace15.setText("     ");
        lblSpace15.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace15);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrLoantProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrLoantProduct.add(btnPrint);

        lblSpace16.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace16.setText("     ");
        lblSpace16.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace16);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnClose);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace17);

        btnItemLst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnItemLst.setToolTipText("Item Balance Listing");
        btnItemLst.setMinimumSize(new java.awt.Dimension(31, 23));
        btnItemLst.setPreferredSize(new java.awt.Dimension(21, 21));
        btnItemLst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemLstActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnItemLst);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 622;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(tbrLoantProduct, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    private Object[][] setFinalProcessTableData() {
        HashMap whereMap = new HashMap();
        System.out.println("BRR==" + observable.getCbmbranch().getKeyForSelected() + "DATE----" + tdtFinalDate.getDateValue()
                + "FINAL----" + cboFinalPrcessType.getSelectedItem());
        whereMap.put("DT", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtFinalDate.getDateValue())));
        if(observable.getCbmbranch().getKeyForSelected()!=null && observable.getCbmbranch().getKeyForSelected().equals("")){
            whereMap.put("BRANCH_CODE", null);
        }
        else{
             whereMap.put("BRANCH_CODE", observable.getCbmbranch().getKeyForSelected());
        }
        whereMap.put("FINAL_ACCOUNT_TYPE", cboFinalPrcessType.getSelectedItem());
        
        lst1 = ClientUtil.executeQuery("getFinalBalanceSheet", whereMap);
        HashMap finalProcessMap = new HashMap();
        if (lst1 != null && lst1.size() > 0) {
            Object totalList[][] = new Object[lst1.size()][6];
            for (int j = 0; j < lst1.size(); j++) {
                finalProcessMap = (HashMap) lst1.get(j);
                totalList[j][0] = CommonUtil.convertObjToStr(finalProcessMap.get("MJR_AC_HD_DESC"));
                totalList[j][1] = CommonUtil.convertObjToStr(finalProcessMap.get("AC_HD_DESC"));
                totalList[j][2] = CommonUtil.convertObjToStr(finalProcessMap.get("AC_HD_ID"));
                if((cboFinalPrcessType.getSelectedItem()!=null && cboFinalPrcessType.getSelectedItem().equals("TRADING")) ||
                     (cboFinalPrcessType.getSelectedItem()!=null && cboFinalPrcessType.getSelectedItem().equals("PROFIT AND LOSS"))   )
                {
                    if (finalProcessMap.get("BALANCE_TYPE") != null && finalProcessMap.get("BALANCE_TYPE").equals("DEBIT")) {
                         totalList[j][3] = CommonUtil.convertObjToStr(finalProcessMap.get("CLOSE_BAL"));
                         totalList[j][4] = "0.00";
                        
                    }
                    if (finalProcessMap.get("BALANCE_TYPE") != null && finalProcessMap.get("BALANCE_TYPE").equals("CREDIT")) {
                         totalList[j][3] = "0.00";
                         totalList[j][4] = CommonUtil.convertObjToStr(finalProcessMap.get("CLOSE_BAL"));
                    }
                }
                else{
                    if (finalProcessMap.get("BALANCE_TYPE") != null && finalProcessMap.get("BALANCE_TYPE").equals("DEBIT")) {
                        
                        totalList[j][3] = "0.00";
                        totalList[j][4] = CommonUtil.convertObjToStr(finalProcessMap.get("CLOSE_BAL"));
                    }
                    if (finalProcessMap.get("BALANCE_TYPE") != null && finalProcessMap.get("BALANCE_TYPE").equals("CREDIT")) {
                        
                        totalList[j][3] = CommonUtil.convertObjToStr(finalProcessMap.get("CLOSE_BAL"));
                        totalList[j][4] = "0.00";
                    }
                }
                totalList[j][5] = CommonUtil.convertObjToStr(finalProcessMap.get("BALANCE_TYPE"));
            }
            return totalList;
        } else {
            ClientUtil.displayAlert("No Data!!! ");
        }
        return null;

    }

    private Object[][] setFinalProcessAuthTableData(String finaltype) {
        HashMap whereMap = new HashMap();
        whereMap.put("BAL_SHEET_ID", observable.getBalSheetId());
        lst1 = ClientUtil.executeQuery("getSelectBalanceFinalSheetTO", whereMap);
        HashMap finalProcessAuthMap = new HashMap();
        if (lst1 != null && lst1.size() > 0) {
            Object totalList[][] = new Object[lst1.size()][6];
            for (int j = 0; j < lst1.size(); j++) {
                finalProcessAuthMap = (HashMap) lst1.get(j);
                String descData = CommonUtil.convertObjToStr(finalProcessAuthMap.get("ACCOUNT_HEAD_DESC"));
                String part1 = "", part2 = "";
                if (descData.contains("-")) {
                    String[] parts = descData.split("-");
                    part1 = parts[0];
                    part2 = parts[1];
                }
                totalList[j][0] = CommonUtil.convertObjToStr(part1);
                totalList[j][1] = CommonUtil.convertObjToStr(part2);
                totalList[j][2] = CommonUtil.convertObjToStr(finalProcessAuthMap.get("ACCOUNT_HEAD_ID"));
              /*  if (finalProcessAuthMap.get("BALANCE_TYPE") != null && finalProcessAuthMap.get("BALANCE_TYPE").equals("DEBIT")) {
                    totalList[j][3] = CommonUtil.convertObjToStr(finalProcessAuthMap.get("AMOUNT"));
                    totalList[j][4] = "0.00";
                }
                if (finalProcessAuthMap.get("BALANCE_TYPE") != null && finalProcessAuthMap.get("BALANCE_TYPE").equals("CREDIT")) {
                    totalList[j][3] = "0.00";
                    totalList[j][4] = CommonUtil.convertObjToStr(finalProcessAuthMap.get("AMOUNT"));
                }*/
                if((finaltype!=null && finaltype.equals("TRADING")) ||
                     (finaltype!=null && finaltype.equals("PROFIT AND LOSS"))   )
                {
                    if (finalProcessAuthMap.get("BALANCE_TYPE") != null && finalProcessAuthMap.get("BALANCE_TYPE").equals("DEBIT")) {
                         totalList[j][3] = CommonUtil.convertObjToStr(finalProcessAuthMap.get("AMOUNT"));
                         totalList[j][4] = "0.00";
                        
                    }
                    if (finalProcessAuthMap.get("BALANCE_TYPE") != null && finalProcessAuthMap.get("BALANCE_TYPE").equals("CREDIT")) {
                         totalList[j][3] = "0.00";
                         totalList[j][4] = CommonUtil.convertObjToStr(finalProcessAuthMap.get("AMOUNT"));
                    }
                }
                else{
                    if (finalProcessAuthMap.get("BALANCE_TYPE") != null && finalProcessAuthMap.get("BALANCE_TYPE").equals("DEBIT")) {
                        
                        totalList[j][3] = "0.00";
                        totalList[j][4] = CommonUtil.convertObjToStr(finalProcessAuthMap.get("AMOUNT"));
                    }
                    if (finalProcessAuthMap.get("BALANCE_TYPE") != null && finalProcessAuthMap.get("BALANCE_TYPE").equals("CREDIT")) {
                        
                        totalList[j][3] = CommonUtil.convertObjToStr(finalProcessAuthMap.get("AMOUNT"));
                        totalList[j][4] = "0.00";
                    }
                }
                totalList[j][5] = CommonUtil.convertObjToStr(finalProcessAuthMap.get("BALANCE_TYPE"));
                observable.getCbmbranch().setKeyForSelected(CommonUtil.convertObjToStr(finalProcessAuthMap.get("BRANCH_CODE")));
                tdtFinalDate.setDateValue(CommonUtil.convertObjToStr(finalProcessAuthMap.get("TO_DT")));
                cboFinalPrcessType.setSelectedItem(CommonUtil.convertObjToStr(finalProcessAuthMap.get("FINAL_ACCOUNT_TYPE")));
            }
            return totalList;
        } else {
            ClientUtil.displayAlert("No Data!!! ");

        }
        return null;

    }

    private Object[][] setTableData() {
        HashMap whereMap = new HashMap();
        //boolean flag = false;
        colorList = new ArrayList();
        whereMap.put("DT", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtFrmDt.getDateValue())));
        whereMap.put("BRANCH_CODE", observable.getCbmbranch().getKeyForSelected());
        if(txtAcHD.getText() != null && txtAcHD.getText().length()>0){
            whereMap.put("AC_HD",(String)txtAcHD.getText());
        }
        if(chkAsset.isSelected()==true){
            whereMap.put("MJR_AC_HD_TYPE","ASSETS");
        }
        if(chkLiability.isSelected()==true){
            whereMap.put("MJR_AC_HD_TYPE","LIABILITY");
        }
        if(chkAsset.isSelected()==true && chkLiability.isSelected()==true){
            whereMap.remove("MJR_AC_HD_TYPE");
        }        
        lst1 = ClientUtil.executeQuery("getClosingGLBalance", whereMap);
        HashMap processMap = new HashMap();
        if (lst1 != null && lst1.size() > 0) {
            Object totalList[][] = new Object[lst1.size()][7];
            for (int j = 0; j < lst1.size(); j++) {
                processMap = (HashMap) lst1.get(j);
               // if(!CommonUtil.convertObjToStr(processMap.get("CLOSE_BAL")).equals("null")){
                    totalList[j][0] = CommonUtil.convertObjToStr(processMap.get("AC_HD_CODE"));
                    totalList[j][1] = CommonUtil.convertObjToStr(processMap.get("AC_HD_DESC"));
                    totalList[j][2] = CommonUtil.convertObjToStr(processMap.get("OPN_BAL"));
                    totalList[j][3] = CommonUtil.convertObjToStr(processMap.get("CLOSE_BAL"));
                    totalList[j][6] = CommonUtil.convertObjToStr(processMap.get("HEAD_TYPE"));
                    if(CommonUtil.convertObjToStr(processMap.get("UPDATED_HEAD")).equals("Y")){                      
                      colorList.add(String.valueOf(j));
                    }
                     
               // }else{
               //     flag = true;
               // }
           }
           // System.out.println("flag####"+flag);
           //if(flag){
            //     ClientUtil.displayAlert("Day End not Completed in this Date!!! ");
            //     btnCancelActionPerformed(null);
           //}
           return totalList;
        } else {
            ClientUtil.displayAlert("No Data!!! ");

        }
        return null;

    }

    private Object[][] setTableAuthData() {
        HashMap whereMap = new HashMap();
        whereMap.put("BAL_SHEET_ID", observable.getBalSheetId());
        lst1 = ClientUtil.executeQuery("getSelectBalanceSheetTO", whereMap);
        System.out.println("lst1lst1lst1lst1" + lst1);
        HashMap processAuthMap = new HashMap();
        if (lst1 != null && lst1.size() > 0) {
            Object totalList[][] = new Object[lst1.size()][4];
            for (int j = 0; j < lst1.size(); j++) {
                processAuthMap = (HashMap) lst1.get(j);
                totalList[j][0] = CommonUtil.convertObjToStr(processAuthMap.get("ACCOUNT_HEAD_ID"));
                totalList[j][1] = CommonUtil.convertObjToStr(processAuthMap.get("ACCOUNT_HEAD_DESC"));
                totalList[j][2] = CommonUtil.convertObjToStr(processAuthMap.get("AMOUNT"));
                totalList[j][3] = CommonUtil.convertObjToStr(processAuthMap.get("BALANCE_TYPE"));
                observable.getCbmbranch().setKeyForSelected(CommonUtil.convertObjToStr(processAuthMap.get("BRANCH_CODE")));
                tdtFrmDt.setDateValue(CommonUtil.convertObjToStr(processAuthMap.get("TO_DT")));
                //cboFinalActType.setSelectedItem(CommonUtil.convertObjToStr(processAuthMap.get("FINAL_ACCOUNT_TYPE")));
                //cboAccountHead.setSelectedItem(CommonUtil.convertObjToStr(processAuthMap.get("SUB_ACCOUNT_TYPE")));
          
            }
            return totalList;
        }
        return null;

    }

    public void initTableData() {
        // model=new javax.swing.table.DefaultTableModel();

        tblBalanceUpdate.setModel(new javax.swing.table.DefaultTableModel(
                setTableData(),
                new String[]{
                    "Account ID","Account Head", "Opening Balance", "Closing Balance","New Closing Balance","Difference","Head type","Bal Type"}) {

            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                 false,false, false, false, true, false,false,true
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 4) {
                    return true;
                }
                if (columnIndex == 7) {
                    tblBalanceUpdate.getColumnModel().getColumn(columnIndex).setCellEditor(new DefaultCellEditor(cboBalType));
                    tblBalanceUpdate.setColumnSelectionAllowed(true);
                    return true;
                }  
                return canEdit[columnIndex];
            }
        });
        setSizeTableData();
        setColour();
        //setUpComboBox(tblBalanceUpdate,tblBalanceUpdate.getColumnModel().getColumn(2));
        tblBalanceUpdate.setCellSelectionEnabled(true);
        tblBalanceUpdate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //  tblBalanceUpdatePropertyChange(evt);
            }
        });

        setTableModelListener();
        if (tblBalanceUpdate.getRowCount() > 0) {
            // boolean chk=((Boolean)tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 0)).booleanValue(); 
            System.out.println("calcTotal inuit=====");
            calcTotal();
        }
    }
    //public void setUpComboBox(JTable table,TableColumn comboColumn){
    //    JComboBox comboBox = new JComboBox();
    //    comboBox.addItem("CREDIT");
    //    comboBox.addItem("DEBIT");
      //  comboColumn.setCellEditor(new DefaultCellEditor(comboBox)); 
    //}
    private void setSizeTableData(){
        tblBalanceUpdate.getColumnModel().getColumn(0).setPreferredWidth(30);
        tblBalanceUpdate.getColumnModel().getColumn(1).setPreferredWidth(180);
        tblBalanceUpdate.getColumnModel().getColumn(2).setPreferredWidth(55);
        tblBalanceUpdate.getColumnModel().getColumn(3).setPreferredWidth(55);
        tblBalanceUpdate.getColumnModel().getColumn(4).setPreferredWidth(55);
        tblBalanceUpdate.getColumnModel().getColumn(5).setPreferredWidth(55);
        tblBalanceUpdate.getColumnModel().getColumn(5).setPreferredWidth(35);
        
    }
    private void setColour() {
        /* Set a cellrenderer to this table in order format the date */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                 if (colorList.contains(String.valueOf(row))) {
                    setForeground(Color.RED);
                    setBackground(Color.BLACK);
                    setFont(new java.awt.Font("Tahoma", Font.BOLD, 12));
                } else {
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblBalanceUpdate.setDefaultRenderer(Object.class, renderer);
    }
    private void setTableModelListener() {
        try{
        tableModelListener = new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    double Opentotal=0;
                    double Clostotal=0;
                    double Newtotal=0;
                    double Diftotal=0;
                    int RowCount=tblBalanceUpdate.getRowCount(); 
                    System.out.println("row#####"+row);
                    System.out.println("column#####"+column);
                    //for(int i=row;i<RowCount;i++ ){
                       // System.out.println("iiiiiiii#####"+i);
                        //if(tblBalanceUpdate.getValueAt(row, 2)!=null && tblBalanceUpdate.getValueAt(row, column)!=null) {
                    if (row!=RowCount) {
                        if (column == 4) {
                            if(tblBalanceUpdate.getValueAt(row, 4)!=null && !tblBalanceUpdate.getValueAt(row, 4).toString().equals("") && !isNumeric(tblBalanceUpdate.getValueAt(row, 4).toString()))
                            {
                                System.out.println("4356346436346====");
                                ClientUtil.showAlertWindow("Please enter Numeric Value!!!"); 
                                tblBalanceUpdate.setValueAt("", row, 4);
                                tblBalanceUpdate.setValueAt("", row, 5);
                               return;
                            }
                            double oldamount =  CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(row, 3)) .doubleValue();
                            double newamount =  CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(row, column)) .doubleValue();
                            //double difamount = newamount - oldamount;
                                //tblBalanceUpdate.setValueAt(formatCrore(String.valueOf(difamount))), row, 5));
                            System.out.println("value#######"+tblBalanceUpdate.getValueAt(row, column));
                            if(!tblBalanceUpdate.getValueAt(row, column).toString().equals("")){
                                System.out.println("enterd####");
                                double difamount = newamount - oldamount;
                                tblBalanceUpdate.setValueAt((CommonUtil.convertObjToStr(difamount)), row, 5);
                            }else{
                                //tblBalanceUpdate.setValueAt("", row, 4);
                                tblBalanceUpdate.setValueAt("", row, 5);
                            }
                                
                       }
                            //if (tblBalanceUpdate.getRowCount() > 0) {
                            for (int i = 0; i < tblBalanceUpdate.getRowCount(); i++) {
                                Newtotal =  Newtotal + CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(i, 4)).doubleValue();
                                Diftotal =  Diftotal + CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(i, 5)).doubleValue();
                            }
                            System.out.println("total#####3"+Diftotal);
                            txtNewTotal.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(Newtotal)));
                            txtDifTotal.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(Diftotal)));
                    }
                        // for (int i = 0; i< RowCount; i++) {
                                    //  total +=((Double) model.getValueAt(i, 2)).doubleValue();
                          //          if(tblBalanceUpdate.getModel().getValueAt(i,1)!=null && 
                           //            tblBalanceUpdate.getModel().getValueAt(i,2)!=null &&
                            //           tblBalanceUpdate.getModel().getValueAt(i,3)!=null &&
                             //          tblBalanceUpdate.getModel().getValueAt(i,4)!=null) {
                              ///          // totalPay += ((Double) tblData.getModel().getValueAt(i,2)).doubleValue();
                                //        String Openval=tblBalanceUpdate.getModel().getValueAt(i,1).toString();
                               //         String Closval=tblBalanceUpdate.getModel().getValueAt(i,2).toString();
                                  //      String Newval=tblBalanceUpdate.getModel().getValueAt(i,3).toString();
                                  //      String Difval=tblBalanceUpdate.getModel().getValueAt(i,4).toString();
                                        //Openval=Openval.replaceAll(",","");
                                  //      //Closval=Closval.replaceAll(",","");
                                        //Newval=Newval.replaceAll(",","");
                                  // / /    //Difval=Difval.replaceAll(",","");
                                        // totalPay += Double.parseDouble(tblData.getModel().getValueAt(i,2).toString());
                                     //   Opentotal += Double.parseDouble(Openval);
                                    //    Clostotal += Double.parseDouble(Closval);
                                     //   Newtotal += Double.parseDouble(Newval);
                                      //  Diftotal += Double.parseDouble(Difval);
                                    //}
                               // }
                    
                        //txtOpenTotal.setText(CommonUtil.convertObjToStr(Opentotal));
                        //txtClosTotal.setText(CommonUtil.convertObjToStr(Clostotal));
                        //txtDifTotal.setText(CommonUtil.convertObjToStr(Diftotal));
                       // }
                    //}
                }
                
            }
           
        };
        tblBalanceUpdate.getModel().addTableModelListener(tableModelListener);
    }catch(Exception e){
            e.printStackTrace();
            }
    }
    public void initTableAuthData() {
        // model=new javax.swing.table.DefaultTableModel();

        tblBalanceUpdate.setModel(new javax.swing.table.DefaultTableModel(
                setTableAuthData(),
                new String[]{
                    "Iteam Head", "Account Head Desc", "Balance", "Type"}) {

            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        tblBalanceUpdate.setCellSelectionEnabled(true);
        tblBalanceUpdate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //  tblBalanceUpdatePropertyChange(evt);
            }
        });
        if (tblBalanceUpdate.getRowCount() > 0) {
            calcTotal();
        }
    }

    public void initFinalProcessTableData(String head1,String head2) {
        tblBalanceSheetPreparation.setModel(new javax.swing.table.DefaultTableModel(
                setFinalProcessTableData(),
                new String[]{
                    "Group", "Head Desc", "Head", head1, head2, "Type"}) {

            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        tblBalanceSheetPreparation.getColumnModel().getColumn(5).setMinWidth(0);
        tblBalanceSheetPreparation.getColumnModel().getColumn(5).setMaxWidth(0);
         tblBalanceSheetPreparation.getColumnModel().getColumn(5).setPreferredWidth(0);
        tblBalanceSheetPreparation.setCellSelectionEnabled(true);
        tblBalanceSheetPreparation.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //  tblBalanceUpdatePropertyChange(evt);
            }
        });
        if (tblBalanceSheetPreparation.getRowCount() > 0) {
            calcFinalProcessTotal();
        }
    }

    public void initFinalProcessAuthTableData(String head1,String head2,String finaltype) {
        tblBalanceSheetPreparation.setModel(new javax.swing.table.DefaultTableModel(
                setFinalProcessAuthTableData(finaltype),
                new String[]{
                    "Group", "Head Desc", "Head",head1, head2, "Type"}) {

            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
         tblBalanceSheetPreparation.getColumnModel().getColumn(5).setMinWidth(0);
        tblBalanceSheetPreparation.getColumnModel().getColumn(5).setMaxWidth(0);
         tblBalanceSheetPreparation.getColumnModel().getColumn(5).setPreferredWidth(0);
        tblBalanceSheetPreparation.setCellSelectionEnabled(true);
        tblBalanceSheetPreparation.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //  tblBalanceUpdatePropertyChange(evt);
            }
        });

        if (tblBalanceSheetPreparation.getRowCount() > 0) {
            calcFinalProcessTotal();
        }
    }

    public void calcFinalProcessTotal() {
        double totalExp = 0, totalInc = 0, diff = 0;
        if (tblBalanceSheetPreparation.getRowCount() > 0) {
            for (int i = 0; i < tblBalanceSheetPreparation.getRowCount(); i++) {
                totalExp = totalExp + CommonUtil.convertObjToDouble(tblBalanceSheetPreparation.getValueAt(i, 3)).doubleValue();
                totalInc = totalInc + CommonUtil.convertObjToDouble(tblBalanceSheetPreparation.getValueAt(i, 4)).doubleValue();
            }
        }
        
        if (totalInc < totalExp) {
            diff = totalExp - totalInc;
            txtNetProfit.setText("0.0");
            txtNetLoss.setText(CommonUtil.convertObjToStr(diff));
            totalInc = totalInc + diff;
            System.out.println("Diffeence-111-------------"+diff);
        } else {
            diff = totalInc - totalExp;
            txtNetProfit.setText(CommonUtil.convertObjToStr(diff));
            txtNetLoss.setText("0.0");
            totalExp = totalExp + diff;
            System.out.println("Diffeence--222------------"+diff);
        }
         System.out.println("cboFinalPrcessType.getSelectedItem()-------"+cboFinalPrcessType.getSelectedItem());
        if(cboFinalPrcessType.getSelectedItem()!=null && cboFinalPrcessType.getSelectedItem().equals("BALANCE SHEET")){
            System.out.println("Diffeence--------------"+diff);
            txtNetProfit.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(diff)));
            txtNetLoss.setText("0.0");
        }
        txtTotalExp.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(totalExp)));
        txtToatalIncome.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(totalInc)));
    }

    public void calcTotal() {
        double Asstotal = 0;
        double Libtotal = 0;
        double Newtotal = 0;
        double Diftotal = 0;
        if (tblBalanceUpdate.getRowCount() > 0) {
            for (int i = 0; i < tblBalanceUpdate.getRowCount(); i++) {
                if(CommonUtil.convertObjToStr(tblBalanceUpdate.getValueAt(i, 6)).equals("ASSETS")){
                    Asstotal = Asstotal + CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(i, 3)).doubleValue();
                }
                if(CommonUtil.convertObjToStr(tblBalanceUpdate.getValueAt(i, 6)).equals("LIABILITY")){
                    Libtotal  = Libtotal + CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(i, 3)).doubleValue();
                }
                //if(CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(i, 3))!=null && CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(i, 4))!=null){
                Newtotal =  Newtotal + CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(i, 4)).doubleValue();
                Diftotal =  Diftotal + CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(i, 5)).doubleValue();
                //}
            }
        }
        txtOpenTotal.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(Asstotal)));
        txtClosTotal.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(Libtotal)));
        txtNewTotal.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(Newtotal)));
        txtDifTotal.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(Diftotal)));
        lblNo.setText(CommonUtil.convertObjToStr(tblBalanceUpdate.getRowCount()));
    
    }
    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        System.out.println("tdtFrmDt.getDateValue()###"+tdtFrmDt.getDateValue());
        boolean flag = false;
        //String branch = CommonUtil.convertObjToStr(observable.getCbmbranch().getKeyForSelected());
        //Date dayendDate =  setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtFrmDt.getDateValue()));
        HashMap dataMap = new HashMap();
        dataMap.put("BRANCH_CODE", observable.getCbmbranch().getKeyForSelected());
        dataMap.put("END_DT", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtFrmDt.getDateValue())));
        if(tdtFrmDt.getDateValue().equals("") || tdtFrmDt.getDateValue().isEmpty()){
            ClientUtil.displayAlert("Date Should not be null!!! "); 
            return;
        }else{
            if(!observable.dayEndChek(dataMap)){
              initTableData();
           }else{
              ClientUtil.displayAlert("Day End Not completed!!! ");
              clear();
              return;
           } 
        }
           
    }//GEN-LAST:event_btnProcessActionPerformed
   
private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
    observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
}//GEN-LAST:event_btnViewActionPerformed

private void clear() {
    ((DefaultTableModel) tblBalanceUpdate.getModel()).setRowCount(0);
    txtOpenTotal.setText("0.0");
    txtClosTotal.setText("0.0");
    txtDifTotal.setText("0.0");
    txtNewTotal.setText("0.0");
    
}
private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
    observable.resetForm();
    ClientUtil.enableDisable(this, true);
    observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
    setButtonEnableDisable();
    btnAuthorize.setEnabled(false);
    btnReject.setEnabled(false);
    btnException.setEnabled(false);
    setModified(true);
    txtOpenTotal.setEnabled(false);
    txtClosTotal.setEnabled(false);
    txtDifTotal.setEnabled(false);
    txtNewTotal.setEnabled(false);
    txtNetProfit.setEnabled(false);
    txtNetLoss.setEnabled(false);
    txtTotalExp.setEnabled(false);
    txtToatalIncome.setEnabled(false);
    btnProcess.setEnabled(true);
    btnFinalProcess.setEnabled(true);
    btnClear.setEnabled(true);
    txtAcHD.setEnabled(true);
    btnAcHd.setEnabled(true);
    btnRollBack.setEnabled(true);
    //disableTabIndex(tabDCDayBegin.getSelectedIndex());
}//GEN-LAST:event_btnNewActionPerformed

private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    observable.resetForm();
    observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
    btnAuthorize.setEnabled(false);
    btnReject.setEnabled(false);
    btnException.setEnabled(false);
}//GEN-LAST:event_btnEditActionPerformed

private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
    observable.resetForm();
    observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
    btnAuthorize.setEnabled(false);
    btnReject.setEnabled(false);
    btnException.setEnabled(false);
}//GEN-LAST:event_btnDeleteActionPerformed

private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
     boolean chk = false;
     for (int i = 0; i < tblBalanceUpdate.getRowCount(); i++) {   
        if(!CommonUtil.convertObjToStr(tblBalanceUpdate.getValueAt(i, 4)).equals("") && CommonUtil.convertObjToStr(tblBalanceUpdate.getValueAt(i, 4))!=null){
            if(CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(i, 4))>=0){
                chk = true; 
                break;
            }
        } 
     }
     if(!chk){
        ClientUtil.showMessageWindow("Please enter the New Closing Amount!!!");
        return;
     }
     savePerformed();
     btnAuthorize.setEnabled(true);
     btnReject.setEnabled(true);
     btnException.setEnabled(true);
}//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed() {
        String action;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            action = CommonConstants.TOSTATUS_INSERT;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            action = CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            action = CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
    }

    private void saveAction(String status) {
        updateOBFields();
        HashMap hs = new HashMap();
        HashMap dataMap=new HashMap();
        //pUi = new ProcessUI();
        //String acHD = "";
        int count = tblBalanceUpdate.getRowCount();
        int datacount = 0;
        //double newBal = 0.0;
        //for(int j=0;j<count;j++){
        //    if(CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(j, 5)).doubleValue()!=0){
         //      datacount++; 
         //   }   
        //}
        if (count > 0) {
             for(int i=0;i<count;i++){
                if(!CommonUtil.convertObjToStr(tblBalanceUpdate.getValueAt(i, 4)).equals("") && CommonUtil.convertObjToStr(tblBalanceUpdate.getValueAt(i, 4))!=null){
                    String acHD = CommonUtil.convertObjToStr(tblBalanceUpdate.getValueAt(i,0));
                    double newBal  = CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(i,5)).doubleValue();
                    double oldClosBal  = CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(i,3)).doubleValue();
                    double newClosBal  = CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(i,4)).doubleValue();
                    double openBal  = CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(i,2)).doubleValue();
                    //if(CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(i, 4)).doubleValue() < 0){
                    //    dataMap.put("BALANCE_TYPE",CommonConstants.DEBIT);
                    //    dataMap.put("NEW_CLOS_BAL", (-1*newClosBal));
                    //}else{
                    dataMap.put("BALANCE_TYPE",CommonUtil.convertObjToStr(tblBalanceUpdate.getValueAt(i,7)));
                    dataMap.put("NEW_CLOS_BAL", (newClosBal));
                   // }
                    dataMap.put("AC_HD",acHD);
                    dataMap.put("BRANCH_CODE", observable.getCbmbranch().getKeyForSelected());
                    dataMap.put("NEW_BAL",newBal);
                    dataMap.put("OPEN_DT", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtFrmDt.getDateValue())));
                    dataMap.put("OLD_CLOS_BAL", oldClosBal);
                    //dataMap.put("NEW_CLOS_BAL", (-1*newClosBal));
                    dataMap.put("OPEN_BAL", openBal);
                    dataMap.put("USER_ID", TrueTransactMain.USER_ID);
                    dataMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                    observable.doAction(dataMap);
                    System.out.println("observable.getProxyReturnMap()####1"+observable.getProxyReturnMap());
                }
             }
             System.out.println("observable.getProxyReturnMap().get(COUNT).equals(0)####"+observable.getProxyReturnMap().get("COUNT"));
             if(observable.getProxyReturnMap()!=null && observable.getProxyReturnMap().containsKey("COUNT")){
                     if(CommonUtil.convertObjToInt(observable.getProxyReturnMap().get("COUNT"))== 1 ) {
                         //pUi.close();
                         ClientUtil.showMessageWindow("Updated Succesfully!!");         
                         btnCancelActionPerformed(null);
                     }else{
                        //pUi.close();
                        ClientUtil.showAlertWindow("Updation Failed!!");  
                        btnCancelActionPerformed(null);
                     }
             }else{
                   //pUi.close();
                   ClientUtil.showAlertWindow("No Changes to Update!!");  
                   btnCancelActionPerformed(null);
             }
             
                 
        } 
    }
    public void updateOBFields() {
    
        observable.setCbobranch(CommonUtil.convertObjToStr(cboBranch.getSelectedItem()));
        observable.setGlAcHd(CommonUtil.convertObjToStr(txtAcHD.getText()));
        //observable.setCboFinalActType(CommonUtil.convertObjToStr(cboFinalActType.getSelectedItem()));
        //observable.setCboAccountHead(CommonUtil.convertObjToStr(cboAccountHead.getSelectedItem()));
        observable.setTdtFromDate(DateUtil.getDateMMDDYYYY(tdtFrmDt.getDateValue()));
        observable.setFrmDate(tdtFrmDt.getDateValue());
        observable.setFinalFrmDate(tdtFinalDate.getDateValue());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setScreen(getScreen());


    }

    private void settings() {
        observable.resetForm();
        ClientUtil.clearAll(this);
        observable.setResultStatus();
        btnCancelActionPerformed(null);
    }

    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
    public void disableTabIndex(int tabIndex){
        System.out.println("tabIndex  000====="+tabIndex);
        if(tabIndex==0){
             ((DefaultTableModel) tblBalanceSheetPreparation.getModel()).setRowCount(0);
               cboBranchFinal.setSelectedIndex(0);
               cboBranchFinal.setEnabled(false);
                tdtFinalDate.setDateValue("");
                tdtFinalDate.setEnabled(false);
                cboFinalPrcessType.setSelectedIndex(0);
                cboFinalPrcessType.setEnabled(false);
                cPanel5.setEnabled(false);
        }
        if(tabIndex==1){
            ((DefaultTableModel) tblBalanceUpdate.getModel()).setRowCount(0);
            cboBranch.setSelectedIndex(0);
            cboBranch.setEnabled(false);
            tdtFrmDt.setDateValue("");
            tdtFrmDt.setEnabled(false);
            //cboFinalActType.setSelectedIndex(0);
            //cboFinalActType.setEnabled(false);
            //cboAccountHead.setSelectedItem("");
            //cboAccountHead.setEnabled(false);
            cPanel4.setEnabled(false);
        }
    }
private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
    // TODO add your handling code here: 
    observable.resetForm();
    ClientUtil.enableDisable(this, false);
    if (!btnSave.isEnabled()) {
        btnSave.setEnabled(true);
    }
    setButtonEnableDisable();
    observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
    observable.setProxyReturnMap(null);
    setModified(false);
    btnAuthorize.setEnabled(true);
    btnReject.setEnabled(true);
    btnException.setEnabled(true);
    ((DefaultTableModel) tblBalanceUpdate.getModel()).setRowCount(0);
    ((DefaultTableModel) tblBalanceSheetPreparation.getModel()).setRowCount(0);
    txtOpenTotal.setText("0.0");
    txtClosTotal.setText("0.0");
    txtDifTotal.setText("0.0");
    txtNewTotal.setText("0.0");
    txtNetProfit.setText("0.0");
    txtNetLoss.setText("0.0");
    txtTotalExp.setText("0.0");
    txtToatalIncome.setText("0.0");
    cboBranch.setSelectedIndex(0);
    cboBranchFinal.setSelectedIndex(0);
    tdtFrmDt.setDateValue("");
    tdtFinalDate.setDateValue("");
    //cboFinalActType.setSelectedIndex(0);
    cboFinalPrcessType.setSelectedIndex(0);
    //cboAccountHead.setSelectedItem("");
    txtOpenTotal.setEnabled(false);
    txtClosTotal.setEnabled(false);
    txtDifTotal.setEnabled(false);
    txtNewTotal.setEnabled(false);
    txtNetProfit.setEnabled(false);
    txtNetLoss.setEnabled(false);
    txtTotalExp.setEnabled(false);
    txtToatalIncome.setEnabled(false);
    viewType = -1;
    isFilled = false;
    btnFinalProcess.setEnabled(true);
    btnProcess.setEnabled(false);
    btnClear.setEnabled(false);
    btnAcHd.setEnabled(false);
    //lblCount.setText("");
    lblNo.setText("");
    txtAcHD.setText("");
    colorList.clear();
    btnRollBack.setEnabled(false);
}//GEN-LAST:event_btnCancelActionPerformed

private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
    // TODO add your handling code here:
    observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
    authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
}//GEN-LAST:event_btnAuthorizeActionPerformed

private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
    // TODO add your handling code here:
    observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
    authorizeStatus(CommonConstants.STATUS_REJECTED);
}//GEN-LAST:event_btnRejectActionPerformed

private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
    // TODO add your handling code here:
    observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
    authorizeStatus(CommonConstants.STATUS_EXCEPTION);
}//GEN-LAST:event_btnExceptionActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("BAL_SHEET_ID", observable.getBalSheetId());
            if (tabDCDayBegin.getSelectedIndex() == 0) {
                ClientUtil.execute("authBalanceSheet", singleAuthorizeMap);
            } else {
                ClientUtil.execute("authBalanceFinalSheet", singleAuthorizeMap);
            }
            btnCancelActionPerformed(null);
            observable.setResult(observable.getActionType());
            observable.setResultStatus();
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            //__ To Save the data in the Internal Frame...
            setModified(true);
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);

            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            if (tabDCDayBegin.getSelectedIndex() == 0) {
                mapParam.put(CommonConstants.MAP_NAME, "getSelectBalanceUpdateTOList");
            }
            if (tabDCDayBegin.getSelectedIndex() == 1) {
                mapParam.put(CommonConstants.MAP_NAME, "getSelectBalanceFinalTOList");
            }
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authInventoryMaster");
            //System.out
            isFilled = false;
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            whereMap = null;
        }
    }

private void popUp(String currAction) {
        viewType1 = currAction;
       final HashMap viewMap = new HashMap();
        HashMap where = new HashMap();
       if(currAction.equalsIgnoreCase("AC_HD")) {
            where.put("BRANCH_CODE", observable.getCbmbranch().getKeyForSelected());
            viewMap.put(CommonConstants.MAP_WHERE, where);
            viewMap.put(CommonConstants.MAP_NAME, "getGLAcHd");
        }
        //observable.setStatus();
        //lblStatus.setText(observable.getLblStatus());
        new ViewAll(this, viewMap).show();
    }


private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
    cifClosingAlert();
}//GEN-LAST:event_btnCloseActionPerformed

private void btnItemLstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemLstActionPerformed
    btnSave.setEnabled(false);
    btnCancel.setEnabled(true);
}//GEN-LAST:event_btnItemLstActionPerformed

private void btnFinalProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalProcessActionPerformed
   if(cboFinalPrcessType.getSelectedItem()!=null && cboFinalPrcessType.getSelectedItem().equals("BALANCE SHEET"))
      initFinalProcessTableData("Liabilities","Asset");
   else
      initFinalProcessTableData("Expenditure","Income");  
}//GEN-LAST:event_btnFinalProcessActionPerformed

private void cboFinalPrcessTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFinalPrcessTypeActionPerformed
    // TODO add your handling code here:
        lblNetProfit.setText("Net Profit");
        lblNetloss.setVisible(true);
        txtNetLoss.setVisible(true);
    if(cboFinalPrcessType.getSelectedItem()!=null && cboFinalPrcessType.getSelectedItem().equals("BALANCE SHEET"))
    {
        lblTotExp.setText("Total Liablities");
        lblTotalIncome.setText("Total Asset");
        lblNetProfit.setText("Diff.Amount");
        lblNetloss.setVisible(false);
        txtNetLoss.setVisible(false);
    }
    if(cboFinalPrcessType.getSelectedItem()!=null && cboFinalPrcessType.getSelectedItem().equals("TRADING"))
    {
        lblNetProfit.setText("Gross Profit");
        lblNetloss.setText("Gross Loss");
   }
}//GEN-LAST:event_cboFinalPrcessTypeActionPerformed

private void txtOpenTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOpenTotalActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtOpenTotalActionPerformed

private void btnAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcHdActionPerformed
        popUp("AC_HD");
    // TODO add your handling code here:
}//GEN-LAST:event_btnAcHdActionPerformed

private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
// TODO add your handling code here:
    for(int i = 0 ;tblBalanceUpdate.getRowCount() > 0 ; i++){
        if(CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(i, 5)).doubleValue()!=0){
            tblBalanceUpdate.setValueAt("", i, 4);
            tblBalanceUpdate.setValueAt("", i, 5);
        }
    }
}//GEN-LAST:event_btnClearActionPerformed

private void btnReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportActionPerformed
// TODO add your handling code here:
   String branchID =  CommonUtil.convertObjToStr(observable.getCbmbranch().getKeyForSelected());
   System.out.println("branch#####"+branchID);
   if(branchID!=null && !branchID.equals(""))
   {
        TTIntegration ttIntgration = null;
        HashMap paramMap = new HashMap();
        paramMap.put("branch_id", branchID);
        ttIntgration.setParam(paramMap);
        ttIntgration.integrationForPrint("GL_OPENING_CHECKING", true);
  }else{
       ClientUtil.showMessageWindow("Please select Branch Code First!!"); 
       return;
  }
}//GEN-LAST:event_btnReportActionPerformed

private void btnRollBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRollBackActionPerformed
// TODO add your handling code here:
    if(tblBalanceUpdate.getSelectedRow()>=0){
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to Roll Back?", CommonConstants.WARNINGTITLE,
            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
            null, options, options[0]);
                            //system.out.println("#$#$$ yesNo : " + yesNo);
        if (yesNo == 0) {
            HashMap dataMap=new HashMap();
            if(CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(tblBalanceUpdate.getSelectedRow(), 4)).doubleValue() == 0){
                String acHd = CommonUtil.convertObjToStr(tblBalanceUpdate.getValueAt(tblBalanceUpdate.getSelectedRow(), 0));
                double newClosBal  = CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(tblBalanceUpdate.getSelectedRow(),4)).doubleValue();
                dataMap.put("AC_HD",acHd);
                dataMap.put("BRANCH_CODE", observable.getCbmbranch().getKeyForSelected());
                dataMap.put("NEW_CLOS_BAL",newClosBal);
                dataMap.put("OPEN_DT", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtFrmDt.getDateValue())));
                dataMap.put("COMMAND", CommonConstants.TOSTATUS_DELETE);
                observable.doAction(dataMap);
            } 
        }
    }else{
       ClientUtil.showMessageWindow("Please select any Row!!"); 
       return;
    }
}//GEN-LAST:event_btnRollBackActionPerformed

private void btnRollBackFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnRollBackFocusGained
// TODO add your handling code here:
    btnRollBack.setToolTipText("To Roll Back GL Updation");
}//GEN-LAST:event_btnRollBackFocusGained

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new GLOpeningUpdateUI().show();
    }

    public void update(Observable observed, Object arg) {
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAcHd;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnFinalProcess;
    private com.see.truetransact.uicomponent.CButton btnItemLst;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnReport;
    private com.see.truetransact.uicomponent.CButton btnRollBack;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CLabel cLabel4;
    private com.see.truetransact.uicomponent.CPanel cPanel4;
    private com.see.truetransact.uicomponent.CPanel cPanel5;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane2;
    private com.see.truetransact.uicomponent.CComboBox cboBranch;
    private com.see.truetransact.uicomponent.CComboBox cboBranchFinal;
    private com.see.truetransact.uicomponent.CComboBox cboFinalPrcessType;
    private com.see.truetransact.uicomponent.CCheckBox chkAsset;
    private com.see.truetransact.uicomponent.CCheckBox chkLiability;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private com.see.truetransact.uicomponent.CLabel lblBalance;
    private com.see.truetransact.uicomponent.CLabel lblBalance1;
    private com.see.truetransact.uicomponent.CLabel lblCount;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblDate1;
    private com.see.truetransact.uicomponent.CLabel lblFinalAccountType1;
    private com.see.truetransact.uicomponent.CLabel lblNetProfit;
    private com.see.truetransact.uicomponent.CLabel lblNetloss;
    private com.see.truetransact.uicomponent.CLabel lblNo;
    private com.see.truetransact.uicomponent.CLabel lblSpace11;
    private com.see.truetransact.uicomponent.CLabel lblSpace12;
    private com.see.truetransact.uicomponent.CLabel lblSpace13;
    private com.see.truetransact.uicomponent.CLabel lblSpace14;
    private com.see.truetransact.uicomponent.CLabel lblSpace15;
    private com.see.truetransact.uicomponent.CLabel lblSpace16;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblTotExp;
    private javax.swing.JLabel lblTotal;
    private com.see.truetransact.uicomponent.CLabel lblTotalIncome;
    private com.see.truetransact.uicomponent.CPanel panBatchProcess1;
    private com.see.truetransact.uicomponent.CPanel panMainSI;
    private com.see.truetransact.uicomponent.CPanel panSI;
    private com.see.truetransact.uicomponent.CPanel panSIID;
    private com.see.truetransact.uicomponent.CPanel panSIIDt;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition1;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition2;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition3;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CPanel panTable1;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAndOr;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CTabbedPane tabDCDayBegin;
    private com.see.truetransact.uicomponent.CTable tblBalanceSheetPreparation;
    private com.see.truetransact.uicomponent.CTable tblBalanceUpdate;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CDateField tdtFinalDate;
    private com.see.truetransact.uicomponent.CDateField tdtFrmDt;
    private com.see.truetransact.uicomponent.CTextField txtAcHD;
    private com.see.truetransact.uicomponent.CTextField txtClosTotal;
    private com.see.truetransact.uicomponent.CTextField txtDifTotal;
    private com.see.truetransact.uicomponent.CTextField txtNetLoss;
    private com.see.truetransact.uicomponent.CTextField txtNetProfit;
    private com.see.truetransact.uicomponent.CTextField txtNewTotal;
    private javax.swing.JTextField txtOpenTotal;
    private com.see.truetransact.uicomponent.CTextField txtToatalIncome;
    private com.see.truetransact.uicomponent.CTextField txtTotalExp;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setMandatoryHashMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HashMap getMandatoryHashMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
   //public static boolean isNumber(String str) {
    //   try {
     //       if(str.trim().length > 0 && str.t'rim().match("/^[0-9]*(\.[0-9]+)?$/")){
           
    //   }
            //   System.out.println("ddd"+d);
     //   }catch (NumberFormatException nfe) {
      //      return false;
      //  }
      //  return true;
  // }
    
    public static String formatCrore(String str) {
        java.text.DecimalFormat numberFormat = new java.text.DecimalFormat();
        numberFormat.applyPattern("########################0.00");

        double currData = Double.parseDouble(str.replaceAll(",", ""));
        str = numberFormat.format(currData);

        String num = str.substring(0, str.lastIndexOf(".")).replaceAll(",", "");
        String dec = str.substring(str.lastIndexOf("."));

        String sign = "";
        if (num.substring(0, 1).equals("-")) {
            sign = num.substring(0, 1);
            num = num.substring(1, num.length());
        }

        char[] chrArr = num.toCharArray();
        StringBuffer fmtStrB = new StringBuffer();

        for (int i = chrArr.length - 1, j = 0, k = 0; i >= 0; i--) {
            if ((j == 3 && k == 3) || (j == 2 && k == 5) || (j == 2 && k == 7)) {
                fmtStrB.insert(0, ",");
                if (k == 7) {
                    k = 0;
                }
                j = 0;
            }
            j++;
            k++;

            fmtStrB.insert(0, chrArr[i]);
        }
        fmtStrB.append(dec);

        str = fmtStrB.toString();

        str = sign + str;

        if (str.equals(".00")) {
            str = "0";
        }

        return str;
    }
    
}  
    
    
