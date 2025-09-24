/*
 * Copyright 2019 Fincuro Solutions (P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of Fincuro Solutions (P) Ltd..  
 * 
 *
 * LoanRecoveryUI.java
 *
 * Created on Jan 3, 2019, 1:46 PM
 */
package com.see.truetransact.ui.termloan.recovery;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.AcctSearchUI;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * @author Rishad
 */
public class LoanRecoveryUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {

    private LoanRecoveryOB observable;
    HashMap paramMap = null;
    CInternalFrame parent = null;
    javax.swing.JList lstSearch;
    java.util.ArrayList arrLst = new java.util.ArrayList();
    TTIntegration ttIntegration = null;
    int previousRow = -1;
    HashMap accountNumberMap = null;
    HashMap guarantorMemberMap = null;
    HashMap accountChargeMap = null;
    HashMap guarantorChargeMap = null;
    String bankName = "";
    boolean generateNotice = false;
    int viewType = 0;
    int FROMACTNO = 1, TOACTNO = 2, EDITVIEW = 3, PROCESSEDITVIEW = 4; 
    private final static Logger log = Logger.getLogger(LoanRecoveryUI.class);
    private String actionType = "NEW";
    private String recoveryId = "";
    private Date currDt = null;
    private AcctSearchUI acctsearch = null;
    private boolean finalChecking = false;
    boolean chktrans = false;
    private String suspenseActNum = null;
    boolean chkok = false;

    /**
     * Creates new form LoanRecoveryUI
     */
    public LoanRecoveryUI() {

        setupInit();
    }

    /**
     * Creates new form LoanRecoveryUI
     */
    public LoanRecoveryUI(CInternalFrame parent, HashMap paramMap) {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
    }

    private void setupInit() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        addtoBtnGp();
        internationalize();
        setButtonEnableDisable();
        observable = new LoanRecoveryOB();
        initTableData();
        btnGenerate.setEnabled(true);
        btnClear.setEnabled(true);
        btnPrintRecoveryList.setEnabled(true);
        btnExportRecoveryList.setEnabled(true);
        btnGenerateNotice.setEnabled(false);
        bankName = ((String) TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase();
        if (bankName.lastIndexOf("MAHILA") != -1) {
        }
        hideRecalculateButton();
        enableDisableComponants(false);
        btnProcessEdit.setEnabled(true);
        tbpRecovery.remove(panRDRecovery);
        tbpRecovery.remove(panMDSRecovery);
        tbpRecovery.remove(panLoanRecovery);
        tbpRecovery.remove(panProcessListUpdate);
        setRDMDSRecoveryVisibility();
    }

    private void hideRecalculateButton(){
        btnRecalculate.setVisible(false);
        btnLoanRecalculate.setVisible(false);
        btnRDRecalculate.setVisible(false);
        btnMDSRecalculate.setVisible(false);
    }
    
    
    private void setRDMDSRecoveryVisibility(){
        //        PanWebCam
        String mdsRdRecovery = CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("RD_MDS_RECOVERY")); 
        System.out.println("mdsRdRecovery :: " + mdsRdRecovery);
        if(mdsRdRecovery.equals("Y")){
            tbpRecovery.add("RD Recovery Posting",panRDRecovery);
            tbpRecovery.add("MDS Recovery Posting",panMDSRecovery);
            tbpRecovery.add("Loan Recovery Posting",panLoanRecovery);
            tbpRecovery.add("Process List Balance Updation",panProcessListUpdate);
//            //tbpRecovery.setTitleAt(1, "RD Recovery Posting");
//            tbpRecovery.add(panMDSRecovery);
//            tbpRecovery.setTitleAt(2,"MDS Recovery Posting");
//            tbpRecovery.add(panLoanRecovery);
//            tbpRecovery.setTitleAt(3,"Loan Recovery Posting");
        }else{
            tbpRecovery.remove(panRDRecovery);
            tbpRecovery.remove(panMDSRecovery);
            tbpRecovery.remove(panProcessListUpdate);
            tbpRecovery.add("Loan Recovery Posting",panLoanRecovery);
        }
    }

    private void addtoBtnGp() {
    }

    private void setupScreen() {
        /*
         * Calculate the screen size
         */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        /*
         * Center frame on the screen
         */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    private void initTableData() {
        tblSalaryRecoveryList.setModel(observable.getTblSalaryRecoveryList());
    }

    private void setObservable() {
        try {
            observable = new LoanRecoveryOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Bring up and populate the temporary project detail screen.
     */
    private void whenTableRowSelected() {
        int rowIndexSelected = tblSalaryRecoveryList.getSelectedRow();
        if (previousRow != -1) {
            if (((Boolean) tblSalaryRecoveryList.getValueAt(previousRow, 0)).booleanValue()) {
                //    int guarantorRowIndexSelected = tblGuarantorData.getSelectedRow();
                if (accountNumberMap == null) {
                    accountNumberMap = new HashMap();
                }
                if (guarantorMemberMap == null) {
                    guarantorMemberMap = new HashMap();
                }
                if (previousRow != -1 && previousRow != rowIndexSelected) {
                }
            } else {
            }
        }
        previousRow = rowIndexSelected;
        int selectedColomn = tblSalaryRecoveryList.getSelectedColumn();
    }

    private boolean isSelectedRowTicked(com.see.truetransact.uicomponent.CTable table) {
        boolean selected = false;
        for (int i = 0, j = table.getRowCount(); i < j; i++) {
            selected = ((Boolean) table.getValueAt(i, 0)).booleanValue();
            if (selected) {
                break;
            }
        }
        return selected;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdbArbit = new com.see.truetransact.uicomponent.CButtonGroup();
        panSalaryRecoveryList = new com.see.truetransact.uicomponent.CPanel();
        tbrTokenConfig = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace76 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace77 = new com.see.truetransact.uicomponent.CLabel();
        btnClose1 = new com.see.truetransact.uicomponent.CButton();
        lblSpace78 = new com.see.truetransact.uicomponent.CLabel();
        btnProcessEdit = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        tbpRecovery = new com.see.truetransact.uicomponent.CTabbedPane();
        panSalaryRecoveryOptions = new com.see.truetransact.uicomponent.CPanel();
        lblCalcIntUpto = new com.see.truetransact.uicomponent.CLabel();
        tdtCalcIntUpto = new com.see.truetransact.uicomponent.CDateField();
        btnGenerate = new com.see.truetransact.uicomponent.CButton();
        btnPrintRecoveryList = new com.see.truetransact.uicomponent.CButton();
        btnExportRecoveryList = new com.see.truetransact.uicomponent.CButton();
        lblInstitutionName = new com.see.truetransact.uicomponent.CLabel();
        txtInstitutionName = new com.see.truetransact.uicomponent.CTextField();
        btnInstitution = new com.see.truetransact.uicomponent.CButton();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        lblActHoldersList = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecords = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecordsVal = new com.see.truetransact.uicomponent.CLabel();
        srpSalaryRecoveryList = new com.see.truetransact.uicomponent.CScrollPane();
        tblSalaryRecoveryList = new com.see.truetransact.uicomponent.CTable();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnGenerateNotice = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnPrintNotice = new com.see.truetransact.uicomponent.CButton();
        lblTotalTransactionAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotal = new com.see.truetransact.uicomponent.CLabel();
        lblSelectedCount = new com.see.truetransact.uicomponent.CLabel();
        lblSelectedCountVal = new com.see.truetransact.uicomponent.CLabel();
        btnRecalculate = new com.see.truetransact.uicomponent.CButton();
        panRDRecovery = new com.see.truetransact.uicomponent.CPanel();
        lblCalcIntUpto1 = new com.see.truetransact.uicomponent.CLabel();
        txtRDInstitutionName = new com.see.truetransact.uicomponent.CTextField();
        lblActHoldersList1 = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRDRecordsVal = new com.see.truetransact.uicomponent.CLabel();
        lblInstitutionName1 = new com.see.truetransact.uicomponent.CLabel();
        chkRDSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        sptLine1 = new com.see.truetransact.uicomponent.CSeparator();
        tdtRDCalcIntUpto = new com.see.truetransact.uicomponent.CDateField();
        srpRDSalaryRecoveryList = new com.see.truetransact.uicomponent.CScrollPane();
        tblRDSalaryRecoveryList = new com.see.truetransact.uicomponent.CTable();
        lblRDNoOfRecords = new com.see.truetransact.uicomponent.CLabel();
        panRDSearch = new com.see.truetransact.uicomponent.CPanel();
        btnRDGenerateNotice = new com.see.truetransact.uicomponent.CButton();
        btnRDClose = new com.see.truetransact.uicomponent.CButton();
        btnRDClear = new com.see.truetransact.uicomponent.CButton();
        btnRDPrintNotice = new com.see.truetransact.uicomponent.CButton();
        lblRDTotalTransactionAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblRDTotal = new com.see.truetransact.uicomponent.CLabel();
        lblRDSelectedCount = new com.see.truetransact.uicomponent.CLabel();
        lblRDSelectedCountVal = new com.see.truetransact.uicomponent.CLabel();
        btnRDRecalculate = new com.see.truetransact.uicomponent.CButton();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        lblRDInstRecAmtVal = new com.see.truetransact.uicomponent.CLabel();
        cLabel4 = new com.see.truetransact.uicomponent.CLabel();
        lblRDPostedAmtVal = new com.see.truetransact.uicomponent.CLabel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtRDRecoveryId = new com.see.truetransact.uicomponent.CTextField();
        panMDSRecovery = new com.see.truetransact.uicomponent.CPanel();
        lblMDSCalcIntUpto = new com.see.truetransact.uicomponent.CLabel();
        txtMDSInstitutionName = new com.see.truetransact.uicomponent.CTextField();
        lblActHoldersList2 = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfMDSRecordsVal = new com.see.truetransact.uicomponent.CLabel();
        lblInstitutionName2 = new com.see.truetransact.uicomponent.CLabel();
        chkMDSSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        sptLine2 = new com.see.truetransact.uicomponent.CSeparator();
        tdtMDSCalcIntUpto = new com.see.truetransact.uicomponent.CDateField();
        srpMDSSalaryRecoveryList = new com.see.truetransact.uicomponent.CScrollPane();
        tblMDSSalaryRecoveryList = new com.see.truetransact.uicomponent.CTable();
        lblMDSNoOfRecords = new com.see.truetransact.uicomponent.CLabel();
        panMDSSearch = new com.see.truetransact.uicomponent.CPanel();
        btnMDSGenerateNotice = new com.see.truetransact.uicomponent.CButton();
        btnMDSClose = new com.see.truetransact.uicomponent.CButton();
        btnMDSClear = new com.see.truetransact.uicomponent.CButton();
        btnMDSPrintNotice = new com.see.truetransact.uicomponent.CButton();
        lblMDSTotalTransactionAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblMDSTotal = new com.see.truetransact.uicomponent.CLabel();
        lblMDSSelectedCount = new com.see.truetransact.uicomponent.CLabel();
        lblMDSSelectedCountVal = new com.see.truetransact.uicomponent.CLabel();
        btnMDSRecalculate = new com.see.truetransact.uicomponent.CButton();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        lblMDSInstRecAmtVal = new com.see.truetransact.uicomponent.CLabel();
        cLabel6 = new com.see.truetransact.uicomponent.CLabel();
        lblMDSPostAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblMDSRecoveryId = new com.see.truetransact.uicomponent.CLabel();
        txtMDSRecoveryId = new com.see.truetransact.uicomponent.CTextField();
        panLoanRecovery = new com.see.truetransact.uicomponent.CPanel();
        lblLoanCalcIntUpto = new com.see.truetransact.uicomponent.CLabel();
        txtLoanInstitutionName = new com.see.truetransact.uicomponent.CTextField();
        lblActHoldersList3 = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfLoanRecordsVal = new com.see.truetransact.uicomponent.CLabel();
        lblLoanInstitutionName = new com.see.truetransact.uicomponent.CLabel();
        chkLoanSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        sptLine3 = new com.see.truetransact.uicomponent.CSeparator();
        tdtLoanCalcIntUpto = new com.see.truetransact.uicomponent.CDateField();
        srpLoanSalaryRecoveryList = new com.see.truetransact.uicomponent.CScrollPane();
        tblLoanSalaryRecoveryList = new com.see.truetransact.uicomponent.CTable();
        lblLoanNoOfRecords = new com.see.truetransact.uicomponent.CLabel();
        panLoanSearch = new com.see.truetransact.uicomponent.CPanel();
        btnLoanGenerateNotice = new com.see.truetransact.uicomponent.CButton();
        btnLoanClose = new com.see.truetransact.uicomponent.CButton();
        btnLoanClear = new com.see.truetransact.uicomponent.CButton();
        btnLoanPrintNotice = new com.see.truetransact.uicomponent.CButton();
        lblLoanTotalTransactionAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblLoanTotal = new com.see.truetransact.uicomponent.CLabel();
        lblLoanSelectedCount = new com.see.truetransact.uicomponent.CLabel();
        lblLoanSelectedCountVal = new com.see.truetransact.uicomponent.CLabel();
        btnLoanRecalculate = new com.see.truetransact.uicomponent.CButton();
        cLabel5 = new com.see.truetransact.uicomponent.CLabel();
        lblTLInstRecAmtVal = new com.see.truetransact.uicomponent.CLabel();
        cLabel7 = new com.see.truetransact.uicomponent.CLabel();
        lblTLPostedAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblLoanRecoveryId = new com.see.truetransact.uicomponent.CLabel();
        txtLoanRecoveryId = new com.see.truetransact.uicomponent.CTextField();
        panProcessListUpdate = new com.see.truetransact.uicomponent.CPanel();
        txtProcessListRecoveryId = new com.see.truetransact.uicomponent.CTextField();
        lblNoOfProcessListRecordsVal = new com.see.truetransact.uicomponent.CLabel();
        lblLoanNoOfRecords1 = new com.see.truetransact.uicomponent.CLabel();
        sptLine4 = new com.see.truetransact.uicomponent.CSeparator();
        lblLoanRecoveryId1 = new com.see.truetransact.uicomponent.CLabel();
        lblProcessListInstitutionName = new com.see.truetransact.uicomponent.CLabel();
        panProcessListSearch = new com.see.truetransact.uicomponent.CPanel();
        btnProcessListSave = new com.see.truetransact.uicomponent.CButton();
        btnProcessListClose = new com.see.truetransact.uicomponent.CButton();
        btnProcessListClear = new com.see.truetransact.uicomponent.CButton();
        lblProcessListTotalTransactionAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblLoanTotal1 = new com.see.truetransact.uicomponent.CLabel();
        lblLoanSelectedCount1 = new com.see.truetransact.uicomponent.CLabel();
        lblProcessListSelectedCountVal = new com.see.truetransact.uicomponent.CLabel();
        lblActHoldersList4 = new com.see.truetransact.uicomponent.CLabel();
        srpProcessListSalaryRecoveryList = new com.see.truetransact.uicomponent.CScrollPane();
        tblProcessListSalaryRecoveryList = new com.see.truetransact.uicomponent.CTable();
        txtProcessListLoanInstitutionName = new com.see.truetransact.uicomponent.CTextField();
        lblProcessListLoanCalcIntUpto = new com.see.truetransact.uicomponent.CLabel();
        tdProcessListtLoanCalcIntUpto = new com.see.truetransact.uicomponent.CDateField();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblStatusVal = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Loan Recovery");
        setMaximumSize(new java.awt.Dimension(900, 620));
        setMinimumSize(new java.awt.Dimension(900, 620));
        setOpaque(true);
        setPreferredSize(new java.awt.Dimension(900, 620));

        panSalaryRecoveryList.setMaximumSize(new java.awt.Dimension(830, 280));
        panSalaryRecoveryList.setMinimumSize(new java.awt.Dimension(830, 280));
        panSalaryRecoveryList.setOpaque(false);
        panSalaryRecoveryList.setPreferredSize(new java.awt.Dimension(830, 280));
        panSalaryRecoveryList.setLayout(new java.awt.GridBagLayout());

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        tbrTokenConfig.add(btnView);

        lblSpace6.setText("     ");
        tbrTokenConfig.add(lblSpace6);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnNew);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace73);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnEdit);

        lbSpace2.setText("     ");
        tbrTokenConfig.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnSave);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace74);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTokenConfig.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnAuthorize);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace75);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        tbrTokenConfig.add(btnException);

        lblSpace76.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace76.setText("     ");
        lblSpace76.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace76);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        tbrTokenConfig.add(btnReject);

        lblSpace5.setText("     ");
        tbrTokenConfig.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnPrint);

        lblSpace77.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace77.setText("     ");
        lblSpace77.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace77);

        btnClose1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose1.setToolTipText("Close");
        btnClose1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClose1ActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnClose1);

        lblSpace78.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace78.setText("     ");
        lblSpace78.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace78.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace78.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace78);

        btnProcessEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/ledger.jpg"))); // NOI18N
        btnProcessEdit.setToolTipText("Processed Files");
        btnProcessEdit.setFocusable(false);
        btnProcessEdit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnProcessEdit.setMinimumSize(new java.awt.Dimension(37, 30));
        btnProcessEdit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnProcessEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessEditActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnProcessEdit);

        lbSpace3.setText("     ");

        panSalaryRecoveryOptions.setBorder(javax.swing.BorderFactory.createTitledBorder("Salary Recovery Options"));
        panSalaryRecoveryOptions.setMaximumSize(new java.awt.Dimension(830, 90));
        panSalaryRecoveryOptions.setMinimumSize(new java.awt.Dimension(650, 150));
        panSalaryRecoveryOptions.setPreferredSize(new java.awt.Dimension(650, 90));

        lblCalcIntUpto.setText("Calculate Interest upto");

        tdtCalcIntUpto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtCalcIntUptoFocusLost(evt);
            }
        });

        btnGenerate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnGenerate.setText("Generate");
        btnGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateActionPerformed(evt);
            }
        });

        btnPrintRecoveryList.setText("Print Recovery List");
        btnPrintRecoveryList.setMaximumSize(new java.awt.Dimension(89, 21));
        btnPrintRecoveryList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintRecoveryListActionPerformed(evt);
            }
        });

        btnExportRecoveryList.setText("Export Recovery List");
        btnExportRecoveryList.setMaximumSize(new java.awt.Dimension(89, 21));
        btnExportRecoveryList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportRecoveryListActionPerformed(evt);
            }
        });

        lblInstitutionName.setText("Institution Name");

        txtInstitutionName.setMinimumSize(new java.awt.Dimension(100, 21));

        btnInstitution.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInstitution.setMaximumSize(new java.awt.Dimension(21, 21));
        btnInstitution.setMinimumSize(new java.awt.Dimension(21, 21));
        btnInstitution.setPreferredSize(new java.awt.Dimension(21, 21));
        btnInstitution.setRequestFocusEnabled(false);
        btnInstitution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInstitutionActionPerformed(evt);
            }
        });

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));

        chkSelectAll.setText("Select All");
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });

        lblActHoldersList.setText("Loan Account Holders List");
        lblActHoldersList.setMaximumSize(new java.awt.Dimension(230, 85));
        lblActHoldersList.setMinimumSize(new java.awt.Dimension(186, 18));
        lblActHoldersList.setPreferredSize(new java.awt.Dimension(186, 18));

        lblNoOfRecords.setText("No. of Records Found : ");
        lblNoOfRecords.setMaximumSize(new java.awt.Dimension(140, 18));
        lblNoOfRecords.setMinimumSize(new java.awt.Dimension(140, 18));
        lblNoOfRecords.setPreferredSize(new java.awt.Dimension(140, 18));

        lblNoOfRecordsVal.setMaximumSize(new java.awt.Dimension(230, 85));
        lblNoOfRecordsVal.setMinimumSize(new java.awt.Dimension(80, 18));
        lblNoOfRecordsVal.setPreferredSize(new java.awt.Dimension(80, 18));

        srpSalaryRecoveryList.setMinimumSize(new java.awt.Dimension(19, 22));
        srpSalaryRecoveryList.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                srpSalaryRecoveryListPropertyChange(evt);
            }
        });

        tblSalaryRecoveryList.setModel(new javax.swing.table.DefaultTableModel(
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
        tblSalaryRecoveryList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblSalaryRecoveryList.setMaximumSize(new java.awt.Dimension(2147483647, 0));
        tblSalaryRecoveryList.setMinimumSize(new java.awt.Dimension(350, 80));
        tblSalaryRecoveryList.setPreferredScrollableViewportSize(new java.awt.Dimension(880, 331));
        tblSalaryRecoveryList.setRowSelectionAllowed(false);
        tblSalaryRecoveryList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSalaryRecoveryListMouseClicked(evt);
            }
        });
        tblSalaryRecoveryList.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblSalaryRecoveryListPropertyChange(evt);
            }
        });
        srpSalaryRecoveryList.setViewportView(tblSalaryRecoveryList);

        panSearch.setLayout(new java.awt.GridBagLayout());

        btnGenerateNotice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnGenerateNotice.setText("Post");
        btnGenerateNotice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateNoticeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 0);
        panSearch.add(btnGenerateNotice, gridBagConstraints);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 0);
        panSearch.add(btnClose, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 0);
        panSearch.add(btnClear, gridBagConstraints);

        btnPrintNotice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrintNotice.setText("Print");
        btnPrintNotice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintNoticeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panSearch.add(btnPrintNotice, gridBagConstraints);

        lblTotalTransactionAmtVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalTransactionAmtVal.setPreferredSize(new java.awt.Dimension(100, 21));
        lblTotalTransactionAmtVal.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 1, 0, 0);
        panSearch.add(lblTotalTransactionAmtVal, gridBagConstraints);

        lblTotal.setText("Total Amount :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 0, 0);
        panSearch.add(lblTotal, gridBagConstraints);

        lblSelectedCount.setText("Selected Items :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 47, 0, 0);
        panSearch.add(lblSelectedCount, gridBagConstraints);

        lblSelectedCountVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblSelectedCountVal.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 35;
        gridBagConstraints.ipady = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 6, 0, 0);
        panSearch.add(lblSelectedCountVal, gridBagConstraints);

        btnRecalculate.setText("Recalculate");
        btnRecalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecalculateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 7, 0, 0);
        panSearch.add(btnRecalculate, gridBagConstraints);

        javax.swing.GroupLayout panSalaryRecoveryOptionsLayout = new javax.swing.GroupLayout(panSalaryRecoveryOptions);
        panSalaryRecoveryOptions.setLayout(panSalaryRecoveryOptionsLayout);
        panSalaryRecoveryOptionsLayout.setHorizontalGroup(
            panSalaryRecoveryOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSalaryRecoveryOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panSalaryRecoveryOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panSalaryRecoveryOptionsLayout.createSequentialGroup()
                        .addGroup(panSalaryRecoveryOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panSalaryRecoveryOptionsLayout.createSequentialGroup()
                                .addComponent(chkSelectAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(115, 115, 115)
                                .addComponent(lblActHoldersList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblNoOfRecords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(lblNoOfRecordsVal, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panSalaryRecoveryOptionsLayout.createSequentialGroup()
                                .addGroup(panSalaryRecoveryOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panSalaryRecoveryOptionsLayout.createSequentialGroup()
                                        .addGap(54, 54, 54)
                                        .addGroup(panSalaryRecoveryOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(btnGenerate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(panSalaryRecoveryOptionsLayout.createSequentialGroup()
                                                .addComponent(lblInstitutionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtInstitutionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(18, 18, 18)
                                        .addGroup(panSalaryRecoveryOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panSalaryRecoveryOptionsLayout.createSequentialGroup()
                                                .addComponent(btnInstitution, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(lblCalcIntUpto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tdtCalcIntUpto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panSalaryRecoveryOptionsLayout.createSequentialGroup()
                                                .addComponent(btnPrintRecoveryList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(32, 32, 32)
                                                .addComponent(btnExportRecoveryList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addComponent(sptLine, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(srpSalaryRecoveryList, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(11, 11, 11))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panSalaryRecoveryOptionsLayout.createSequentialGroup()
                        .addComponent(panSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        panSalaryRecoveryOptionsLayout.setVerticalGroup(
            panSalaryRecoveryOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSalaryRecoveryOptionsLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panSalaryRecoveryOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panSalaryRecoveryOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblInstitutionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtInstitutionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnInstitution, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panSalaryRecoveryOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(tdtCalcIntUpto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblCalcIntUpto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panSalaryRecoveryOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGenerate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrintRecoveryList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExportRecoveryList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sptLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panSalaryRecoveryOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panSalaryRecoveryOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chkSelectAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblActHoldersList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblNoOfRecords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblNoOfRecordsVal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(srpSalaryRecoveryList, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1, Short.MAX_VALUE))
        );

        tbpRecovery.addTab("Recovery Process", panSalaryRecoveryOptions);

        lblCalcIntUpto1.setText("Calculate Interest upto");

        txtRDInstitutionName.setEditable(false);
        txtRDInstitutionName.setMinimumSize(new java.awt.Dimension(100, 21));

        lblActHoldersList1.setText("RD Account Holders List");
        lblActHoldersList1.setMaximumSize(new java.awt.Dimension(230, 85));
        lblActHoldersList1.setMinimumSize(new java.awt.Dimension(186, 18));
        lblActHoldersList1.setPreferredSize(new java.awt.Dimension(186, 18));

        lblNoOfRDRecordsVal.setMaximumSize(new java.awt.Dimension(230, 85));
        lblNoOfRDRecordsVal.setMinimumSize(new java.awt.Dimension(80, 18));
        lblNoOfRDRecordsVal.setPreferredSize(new java.awt.Dimension(80, 18));

        lblInstitutionName1.setText("Institution Name");

        chkRDSelectAll.setText("Select All");
        chkRDSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRDSelectAllActionPerformed(evt);
            }
        });

        sptLine1.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine1.setPreferredSize(new java.awt.Dimension(2, 2));

        tdtRDCalcIntUpto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtRDCalcIntUptoFocusLost(evt);
            }
        });

        srpRDSalaryRecoveryList.setMinimumSize(new java.awt.Dimension(19, 22));

        tblRDSalaryRecoveryList.setModel(new javax.swing.table.DefaultTableModel(
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
        tblRDSalaryRecoveryList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblRDSalaryRecoveryList.setMaximumSize(new java.awt.Dimension(2147483647, 0));
        tblRDSalaryRecoveryList.setMinimumSize(new java.awt.Dimension(350, 80));
        tblRDSalaryRecoveryList.setPreferredScrollableViewportSize(new java.awt.Dimension(880, 331));
        tblRDSalaryRecoveryList.setRowSelectionAllowed(false);
        tblRDSalaryRecoveryList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRDSalaryRecoveryListMouseClicked(evt);
            }
        });
        tblRDSalaryRecoveryList.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblRDSalaryRecoveryListPropertyChange(evt);
            }
        });
        srpRDSalaryRecoveryList.setViewportView(tblRDSalaryRecoveryList);

        lblRDNoOfRecords.setText("No. of Records Found : ");
        lblRDNoOfRecords.setMaximumSize(new java.awt.Dimension(140, 18));
        lblRDNoOfRecords.setMinimumSize(new java.awt.Dimension(140, 18));
        lblRDNoOfRecords.setPreferredSize(new java.awt.Dimension(140, 18));

        btnRDGenerateNotice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnRDGenerateNotice.setText("Post");
        btnRDGenerateNotice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRDGenerateNoticeActionPerformed(evt);
            }
        });

        btnRDClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnRDClose.setText("Close");
        btnRDClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRDCloseActionPerformed(evt);
            }
        });

        btnRDClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnRDClear.setText("Clear");
        btnRDClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRDClearActionPerformed(evt);
            }
        });

        btnRDPrintNotice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnRDPrintNotice.setText("Print");
        btnRDPrintNotice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRDPrintNoticeActionPerformed(evt);
            }
        });

        lblRDTotalTransactionAmtVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblRDTotalTransactionAmtVal.setPreferredSize(new java.awt.Dimension(100, 21));
        lblRDTotalTransactionAmtVal.setRequestFocusEnabled(false);

        lblRDTotal.setText("Total Amount :");

        lblRDSelectedCount.setText("Selected Items :");

        lblRDSelectedCountVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblRDSelectedCountVal.setPreferredSize(new java.awt.Dimension(50, 21));

        btnRDRecalculate.setText("Recalculate");
        btnRDRecalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRDRecalculateActionPerformed(evt);
            }
        });

        cLabel2.setText("Total Amount To be Recovered :");

        lblRDInstRecAmtVal.setForeground(new java.awt.Color(51, 51, 255));
        lblRDInstRecAmtVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N

        cLabel4.setText("Balance Amount :");

        lblRDPostedAmtVal.setForeground(new java.awt.Color(51, 51, 255));
        lblRDPostedAmtVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N

        javax.swing.GroupLayout panRDSearchLayout = new javax.swing.GroupLayout(panRDSearch);
        panRDSearch.setLayout(panRDSearchLayout);
        panRDSearchLayout.setHorizontalGroup(
            panRDSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRDSearchLayout.createSequentialGroup()
                .addGroup(panRDSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRDSearchLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(btnRDPrintNotice, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnRDGenerateNotice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnRDClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRDClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(btnRDRecalculate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(85, 85, 85))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRDSearchLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblRDInstRecAmtVal, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblRDPostedAmtVal, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(panRDSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRDSearchLayout.createSequentialGroup()
                        .addComponent(lblRDSelectedCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(lblRDSelectedCountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panRDSearchLayout.createSequentialGroup()
                        .addComponent(lblRDTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblRDTotalTransactionAmtVal, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        panRDSearchLayout.setVerticalGroup(
            panRDSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRDSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRDSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRDSelectedCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRDSelectedCountVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panRDSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRDTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRDSearchLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(lblRDTotalTransactionAmtVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(panRDSearchLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(panRDSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRDPrintNotice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRDGenerateNotice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRDClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRDClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panRDSearchLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(btnRDRecalculate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panRDSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRDInstRecAmtVal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panRDSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblRDPostedAmtVal, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        cLabel1.setText("Recovery Id");

        txtRDRecoveryId.setEditable(false);

        javax.swing.GroupLayout panRDRecoveryLayout = new javax.swing.GroupLayout(panRDRecovery);
        panRDRecovery.setLayout(panRDRecoveryLayout);
        panRDRecoveryLayout.setHorizontalGroup(
            panRDRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRDRecoveryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRDRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRDRecoveryLayout.createSequentialGroup()
                        .addGroup(panRDRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panRDRecoveryLayout.createSequentialGroup()
                                .addComponent(chkRDSelectAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(115, 115, 115)
                                .addComponent(lblActHoldersList1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblRDNoOfRecords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(lblNoOfRDRecordsVal, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panRDRecoveryLayout.createSequentialGroup()
                                .addGroup(panRDRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(sptLine1, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRDRecoveryLayout.createSequentialGroup()
                                        .addGap(54, 54, 54)
                                        .addComponent(lblInstitutionName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addGroup(panRDRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(panRDRecoveryLayout.createSequentialGroup()
                                                .addComponent(txtRDInstitutionName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addComponent(lblCalcIntUpto1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(panRDRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtRDRecoveryId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tdtRDCalcIntUpto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 22, Short.MAX_VALUE))
                            .addComponent(srpRDSalaryRecoveryList, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(11, 11, 11))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRDRecoveryLayout.createSequentialGroup()
                        .addComponent(panRDSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        panRDRecoveryLayout.setVerticalGroup(
            panRDRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRDRecoveryLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panRDRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRDRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblInstitutionName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtRDInstitutionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblCalcIntUpto1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tdtRDCalcIntUpto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(panRDRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRDRecoveryId, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(sptLine1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panRDRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRDRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chkRDSelectAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblActHoldersList1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblRDNoOfRecords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblNoOfRDRecordsVal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(srpRDSalaryRecoveryList, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panRDSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        tbpRecovery.addTab("RD Recovery Posting", panRDRecovery);

        lblMDSCalcIntUpto.setText("Calculate Interest upto");

        txtMDSInstitutionName.setEditable(false);
        txtMDSInstitutionName.setMinimumSize(new java.awt.Dimension(100, 21));

        lblActHoldersList2.setText("MDS Account Holders List");
        lblActHoldersList2.setMaximumSize(new java.awt.Dimension(230, 85));
        lblActHoldersList2.setMinimumSize(new java.awt.Dimension(186, 18));
        lblActHoldersList2.setPreferredSize(new java.awt.Dimension(186, 18));

        lblNoOfMDSRecordsVal.setMaximumSize(new java.awt.Dimension(230, 85));
        lblNoOfMDSRecordsVal.setMinimumSize(new java.awt.Dimension(80, 18));
        lblNoOfMDSRecordsVal.setPreferredSize(new java.awt.Dimension(80, 18));

        lblInstitutionName2.setText("Institution Name");

        chkMDSSelectAll.setText("Select All");
        chkMDSSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMDSSelectAllActionPerformed(evt);
            }
        });

        sptLine2.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine2.setPreferredSize(new java.awt.Dimension(2, 2));

        tdtMDSCalcIntUpto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtMDSCalcIntUptoFocusLost(evt);
            }
        });

        srpMDSSalaryRecoveryList.setMinimumSize(new java.awt.Dimension(19, 22));

        tblMDSSalaryRecoveryList.setModel(new javax.swing.table.DefaultTableModel(
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
        tblMDSSalaryRecoveryList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblMDSSalaryRecoveryList.setMaximumSize(new java.awt.Dimension(2147483647, 0));
        tblMDSSalaryRecoveryList.setMinimumSize(new java.awt.Dimension(350, 80));
        tblMDSSalaryRecoveryList.setPreferredScrollableViewportSize(new java.awt.Dimension(880, 331));
        tblMDSSalaryRecoveryList.setRowSelectionAllowed(false);
        tblMDSSalaryRecoveryList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMDSSalaryRecoveryListMouseClicked(evt);
            }
        });
        tblMDSSalaryRecoveryList.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblMDSSalaryRecoveryListPropertyChange(evt);
            }
        });
        srpMDSSalaryRecoveryList.setViewportView(tblMDSSalaryRecoveryList);

        lblMDSNoOfRecords.setText("No. of Records Found : ");
        lblMDSNoOfRecords.setMaximumSize(new java.awt.Dimension(140, 18));
        lblMDSNoOfRecords.setMinimumSize(new java.awt.Dimension(140, 18));
        lblMDSNoOfRecords.setPreferredSize(new java.awt.Dimension(140, 18));

        btnMDSGenerateNotice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnMDSGenerateNotice.setText("Post");
        btnMDSGenerateNotice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMDSGenerateNoticeActionPerformed(evt);
            }
        });

        btnMDSClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnMDSClose.setText("Close");
        btnMDSClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMDSCloseActionPerformed(evt);
            }
        });

        btnMDSClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnMDSClear.setText("Clear");
        btnMDSClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMDSClearActionPerformed(evt);
            }
        });

        btnMDSPrintNotice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnMDSPrintNotice.setText("Print");
        btnMDSPrintNotice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMDSPrintNoticeActionPerformed(evt);
            }
        });

        lblMDSTotalTransactionAmtVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblMDSTotalTransactionAmtVal.setPreferredSize(new java.awt.Dimension(100, 21));
        lblMDSTotalTransactionAmtVal.setRequestFocusEnabled(false);

        lblMDSTotal.setText("Total Amount :");

        lblMDSSelectedCount.setText("Selected Items :");

        lblMDSSelectedCountVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblMDSSelectedCountVal.setPreferredSize(new java.awt.Dimension(50, 21));

        btnMDSRecalculate.setText("Recalculate");
        btnMDSRecalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMDSRecalculateActionPerformed(evt);
            }
        });

        cLabel3.setText("Total Amount To Be Recovered :");

        lblMDSInstRecAmtVal.setForeground(new java.awt.Color(51, 51, 255));
        lblMDSInstRecAmtVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N

        cLabel6.setText("Balance Amount :");

        lblMDSPostAmtVal.setForeground(new java.awt.Color(51, 51, 255));
        lblMDSPostAmtVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N

        javax.swing.GroupLayout panMDSSearchLayout = new javax.swing.GroupLayout(panMDSSearch);
        panMDSSearch.setLayout(panMDSSearchLayout);
        panMDSSearchLayout.setHorizontalGroup(
            panMDSSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMDSSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panMDSSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panMDSSearchLayout.createSequentialGroup()
                        .addComponent(btnMDSPrintNotice, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnMDSGenerateNotice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnMDSClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panMDSSearchLayout.createSequentialGroup()
                        .addComponent(cLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMDSInstRecAmtVal, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(panMDSSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panMDSSearchLayout.createSequentialGroup()
                        .addComponent(cLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMDSPostAmtVal, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panMDSSearchLayout.createSequentialGroup()
                        .addComponent(btnMDSClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(btnMDSRecalculate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23)
                .addGroup(panMDSSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panMDSSearchLayout.createSequentialGroup()
                        .addComponent(lblMDSSelectedCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(lblMDSSelectedCountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panMDSSearchLayout.createSequentialGroup()
                        .addComponent(lblMDSTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblMDSTotalTransactionAmtVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panMDSSearchLayout.setVerticalGroup(
            panMDSSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMDSSearchLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(panMDSSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panMDSSearchLayout.createSequentialGroup()
                        .addComponent(lblMDSSelectedCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(panMDSSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panMDSSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblMDSInstRecAmtVal, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblMDSPostAmtVal, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblMDSTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panMDSSearchLayout.createSequentialGroup()
                        .addGroup(panMDSSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnMDSPrintNotice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnMDSGenerateNotice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnMDSClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnMDSClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panMDSSearchLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(btnMDSRecalculate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panMDSSearchLayout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(lblMDSSelectedCountVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblMDSTotalTransactionAmtVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblMDSRecoveryId.setText("Recovery Id");

        txtMDSRecoveryId.setEditable(false);
        txtMDSRecoveryId.setAllowAll(true);

        javax.swing.GroupLayout panMDSRecoveryLayout = new javax.swing.GroupLayout(panMDSRecovery);
        panMDSRecovery.setLayout(panMDSRecoveryLayout);
        panMDSRecoveryLayout.setHorizontalGroup(
            panMDSRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMDSRecoveryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panMDSRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panMDSRecoveryLayout.createSequentialGroup()
                        .addGroup(panMDSRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panMDSRecoveryLayout.createSequentialGroup()
                                .addComponent(chkMDSSelectAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(115, 115, 115)
                                .addComponent(lblActHoldersList2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblMDSNoOfRecords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(lblNoOfMDSRecordsVal, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(sptLine2, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panMDSRecoveryLayout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addComponent(lblInstitutionName2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtMDSInstitutionName, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panMDSRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(panMDSRecoveryLayout.createSequentialGroup()
                                        .addComponent(lblMDSCalcIntUpto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tdtMDSCalcIntUpto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panMDSRecoveryLayout.createSequentialGroup()
                                        .addComponent(lblMDSRecoveryId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtMDSRecoveryId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(srpMDSSalaryRecoveryList, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(11, 11, 11))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panMDSRecoveryLayout.createSequentialGroup()
                        .addComponent(panMDSSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        panMDSRecoveryLayout.setVerticalGroup(
            panMDSRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMDSRecoveryLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(panMDSRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panMDSRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblInstitutionName2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtMDSInstitutionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panMDSRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(tdtMDSCalcIntUpto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblMDSCalcIntUpto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17)
                .addGroup(panMDSRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMDSRecoveryId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMDSRecoveryId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sptLine2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panMDSRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panMDSRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chkMDSSelectAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblActHoldersList2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblMDSNoOfRecords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblNoOfMDSRecordsVal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(srpMDSSalaryRecoveryList, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panMDSSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        tbpRecovery.addTab("MDS Recovery Posting", panMDSRecovery);

        panLoanRecovery.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panLoanRecoveryMouseClicked(evt);
            }
        });

        lblLoanCalcIntUpto.setText("Calculate Interest upto");

        txtLoanInstitutionName.setEditable(false);
        txtLoanInstitutionName.setMinimumSize(new java.awt.Dimension(100, 21));

        lblActHoldersList3.setText("Loan Account Holders List");
        lblActHoldersList3.setMaximumSize(new java.awt.Dimension(230, 85));
        lblActHoldersList3.setMinimumSize(new java.awt.Dimension(186, 18));
        lblActHoldersList3.setPreferredSize(new java.awt.Dimension(186, 18));

        lblNoOfLoanRecordsVal.setMaximumSize(new java.awt.Dimension(230, 85));
        lblNoOfLoanRecordsVal.setMinimumSize(new java.awt.Dimension(80, 18));
        lblNoOfLoanRecordsVal.setPreferredSize(new java.awt.Dimension(80, 18));

        lblLoanInstitutionName.setText("Institution Name");

        chkLoanSelectAll.setText("Select All");
        chkLoanSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLoanSelectAllActionPerformed(evt);
            }
        });

        sptLine3.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine3.setPreferredSize(new java.awt.Dimension(2, 2));

        tdtLoanCalcIntUpto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtLoanCalcIntUptoFocusLost(evt);
            }
        });

        srpLoanSalaryRecoveryList.setMinimumSize(new java.awt.Dimension(19, 22));

        tblLoanSalaryRecoveryList.setModel(new javax.swing.table.DefaultTableModel(
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
        tblLoanSalaryRecoveryList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblLoanSalaryRecoveryList.setMaximumSize(new java.awt.Dimension(2147483647, 0));
        tblLoanSalaryRecoveryList.setMinimumSize(new java.awt.Dimension(350, 80));
        tblLoanSalaryRecoveryList.setPreferredScrollableViewportSize(new java.awt.Dimension(880, 331));
        tblLoanSalaryRecoveryList.setRowSelectionAllowed(false);
        tblLoanSalaryRecoveryList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLoanSalaryRecoveryListMouseClicked(evt);
            }
        });
        tblLoanSalaryRecoveryList.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblLoanSalaryRecoveryListPropertyChange(evt);
            }
        });
        srpLoanSalaryRecoveryList.setViewportView(tblLoanSalaryRecoveryList);

        lblLoanNoOfRecords.setText("No. of Records Found : ");
        lblLoanNoOfRecords.setMaximumSize(new java.awt.Dimension(140, 18));
        lblLoanNoOfRecords.setMinimumSize(new java.awt.Dimension(140, 18));
        lblLoanNoOfRecords.setPreferredSize(new java.awt.Dimension(140, 18));

        btnLoanGenerateNotice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnLoanGenerateNotice.setText("Post");
        btnLoanGenerateNotice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoanGenerateNoticeActionPerformed(evt);
            }
        });

        btnLoanClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnLoanClose.setText("Close");
        btnLoanClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoanCloseActionPerformed(evt);
            }
        });

        btnLoanClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnLoanClear.setText("Clear");
        btnLoanClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoanClearActionPerformed(evt);
            }
        });

        btnLoanPrintNotice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnLoanPrintNotice.setText("Print");
        btnLoanPrintNotice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoanPrintNoticeActionPerformed(evt);
            }
        });

        lblLoanTotalTransactionAmtVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblLoanTotalTransactionAmtVal.setPreferredSize(new java.awt.Dimension(100, 21));
        lblLoanTotalTransactionAmtVal.setRequestFocusEnabled(false);

        lblLoanTotal.setText("Total Amount :");

        lblLoanSelectedCount.setText("Selected Items :");

        lblLoanSelectedCountVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblLoanSelectedCountVal.setPreferredSize(new java.awt.Dimension(50, 21));

        btnLoanRecalculate.setText("Recalculate");
        btnLoanRecalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoanRecalculateActionPerformed(evt);
            }
        });

        cLabel5.setText("Total Amount To Be Recovered :");

        lblTLInstRecAmtVal.setForeground(new java.awt.Color(51, 51, 255));
        lblTLInstRecAmtVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N

        cLabel7.setText("Balance Amount :");

        lblTLPostedAmtVal.setForeground(new java.awt.Color(51, 51, 255));
        lblTLPostedAmtVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N

        javax.swing.GroupLayout panLoanSearchLayout = new javax.swing.GroupLayout(panLoanSearch);
        panLoanSearch.setLayout(panLoanSearchLayout);
        panLoanSearchLayout.setHorizontalGroup(
            panLoanSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panLoanSearchLayout.createSequentialGroup()
                .addGroup(panLoanSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panLoanSearchLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(btnLoanPrintNotice, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnLoanGenerateNotice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnLoanClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLoanClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(btnLoanRecalculate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(75, 75, 75)
                        .addComponent(lblLoanSelectedCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panLoanSearchLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTLInstRecAmtVal, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblTLPostedAmtVal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(lblLoanTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panLoanSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLoanTotalTransactionAmtVal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panLoanSearchLayout.createSequentialGroup()
                        .addComponent(lblLoanSelectedCountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panLoanSearchLayout.setVerticalGroup(
            panLoanSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panLoanSearchLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(panLoanSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLoanPrintNotice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLoanGenerateNotice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLoanClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLoanClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panLoanSearchLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(panLoanSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblLoanSelectedCountVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panLoanSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnLoanRecalculate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblLoanSelectedCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(panLoanSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panLoanSearchLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(panLoanSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblLoanTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblLoanTotalTransactionAmtVal, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(panLoanSearchLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(panLoanSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panLoanSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblTLInstRecAmtVal, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panLoanSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblTLPostedAmtVal, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblLoanRecoveryId.setText("Recovery Id");

        txtLoanRecoveryId.setEditable(false);

        javax.swing.GroupLayout panLoanRecoveryLayout = new javax.swing.GroupLayout(panLoanRecovery);
        panLoanRecovery.setLayout(panLoanRecoveryLayout);
        panLoanRecoveryLayout.setHorizontalGroup(
            panLoanRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panLoanRecoveryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panLoanRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panLoanRecoveryLayout.createSequentialGroup()
                        .addGroup(panLoanRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panLoanRecoveryLayout.createSequentialGroup()
                                .addComponent(chkLoanSelectAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(115, 115, 115)
                                .addComponent(lblActHoldersList3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblLoanNoOfRecords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(lblNoOfLoanRecordsVal, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panLoanRecoveryLayout.createSequentialGroup()
                                .addGroup(panLoanRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(sptLine3, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panLoanRecoveryLayout.createSequentialGroup()
                                        .addGap(54, 54, 54)
                                        .addComponent(lblLoanInstitutionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addGroup(panLoanRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(panLoanRecoveryLayout.createSequentialGroup()
                                                .addComponent(txtLoanInstitutionName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addComponent(lblLoanCalcIntUpto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(lblLoanRecoveryId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(panLoanRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtLoanRecoveryId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tdtLoanCalcIntUpto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 22, Short.MAX_VALUE))
                            .addComponent(srpLoanSalaryRecoveryList, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(11, 11, 11))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panLoanRecoveryLayout.createSequentialGroup()
                        .addComponent(panLoanSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        panLoanRecoveryLayout.setVerticalGroup(
            panLoanRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panLoanRecoveryLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panLoanRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panLoanRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblLoanInstitutionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtLoanInstitutionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblLoanCalcIntUpto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tdtLoanCalcIntUpto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(panLoanRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblLoanRecoveryId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLoanRecoveryId, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(sptLine3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panLoanRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panLoanRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chkLoanSelectAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblActHoldersList3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblLoanNoOfRecords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblNoOfLoanRecordsVal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(srpLoanSalaryRecoveryList, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panLoanSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 73, Short.MAX_VALUE)
                .addContainerGap())
        );

        tbpRecovery.addTab("Loan Recovery Posting", panLoanRecovery);

        txtProcessListRecoveryId.setEditable(false);

        lblNoOfProcessListRecordsVal.setMaximumSize(new java.awt.Dimension(230, 85));
        lblNoOfProcessListRecordsVal.setMinimumSize(new java.awt.Dimension(80, 18));
        lblNoOfProcessListRecordsVal.setPreferredSize(new java.awt.Dimension(80, 18));

        lblLoanNoOfRecords1.setText("No. of Records Found : ");
        lblLoanNoOfRecords1.setMaximumSize(new java.awt.Dimension(140, 18));
        lblLoanNoOfRecords1.setMinimumSize(new java.awt.Dimension(140, 18));
        lblLoanNoOfRecords1.setPreferredSize(new java.awt.Dimension(140, 18));

        sptLine4.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine4.setPreferredSize(new java.awt.Dimension(2, 2));

        lblLoanRecoveryId1.setText("Recovery Id");

        lblProcessListInstitutionName.setText("Institution Name");

        btnProcessListSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnProcessListSave.setText("Save");
        btnProcessListSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessListSaveActionPerformed(evt);
            }
        });

        btnProcessListClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnProcessListClose.setText("Close");
        btnProcessListClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessListCloseActionPerformed(evt);
            }
        });

        btnProcessListClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnProcessListClear.setText("Clear");
        btnProcessListClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessListClearActionPerformed(evt);
            }
        });

        lblProcessListTotalTransactionAmtVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblProcessListTotalTransactionAmtVal.setPreferredSize(new java.awt.Dimension(100, 21));
        lblProcessListTotalTransactionAmtVal.setRequestFocusEnabled(false);

        lblLoanTotal1.setText("Total Amount :");

        lblLoanSelectedCount1.setText("Selected Items :");

        lblProcessListSelectedCountVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblProcessListSelectedCountVal.setPreferredSize(new java.awt.Dimension(50, 21));

        javax.swing.GroupLayout panProcessListSearchLayout = new javax.swing.GroupLayout(panProcessListSearch);
        panProcessListSearch.setLayout(panProcessListSearchLayout);
        panProcessListSearchLayout.setHorizontalGroup(
            panProcessListSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panProcessListSearchLayout.createSequentialGroup()
                .addGroup(panProcessListSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panProcessListSearchLayout.createSequentialGroup()
                        .addGap(124, 124, 124)
                        .addComponent(btnProcessListSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnProcessListClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(btnProcessListClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panProcessListSearchLayout.createSequentialGroup()
                        .addGap(448, 448, 448)
                        .addComponent(lblLoanSelectedCount1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(lblProcessListSelectedCountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblLoanTotal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblProcessListTotalTransactionAmtVal, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        panProcessListSearchLayout.setVerticalGroup(
            panProcessListSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panProcessListSearchLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(panProcessListSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnProcessListSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProcessListClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProcessListClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(panProcessListSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLoanSelectedCount1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblProcessListSelectedCountVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLoanTotal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panProcessListSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblProcessListTotalTransactionAmtVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3))
        );

        lblActHoldersList4.setText("Customer List");
        lblActHoldersList4.setMaximumSize(new java.awt.Dimension(230, 85));
        lblActHoldersList4.setMinimumSize(new java.awt.Dimension(186, 18));
        lblActHoldersList4.setPreferredSize(new java.awt.Dimension(186, 18));

        srpProcessListSalaryRecoveryList.setMinimumSize(new java.awt.Dimension(19, 22));

        tblProcessListSalaryRecoveryList.setModel(new javax.swing.table.DefaultTableModel(
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
        tblProcessListSalaryRecoveryList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblProcessListSalaryRecoveryList.setMaximumSize(new java.awt.Dimension(2147483647, 0));
        tblProcessListSalaryRecoveryList.setMinimumSize(new java.awt.Dimension(350, 80));
        tblProcessListSalaryRecoveryList.setPreferredScrollableViewportSize(new java.awt.Dimension(880, 331));
        tblProcessListSalaryRecoveryList.setRowSelectionAllowed(false);
        tblProcessListSalaryRecoveryList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProcessListSalaryRecoveryListMouseClicked(evt);
            }
        });
        tblProcessListSalaryRecoveryList.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblProcessListSalaryRecoveryListPropertyChange(evt);
            }
        });
        srpProcessListSalaryRecoveryList.setViewportView(tblProcessListSalaryRecoveryList);

        txtProcessListLoanInstitutionName.setEditable(false);
        txtProcessListLoanInstitutionName.setMinimumSize(new java.awt.Dimension(100, 21));

        lblProcessListLoanCalcIntUpto.setText("Calculate Interest upto");

        tdProcessListtLoanCalcIntUpto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdProcessListtLoanCalcIntUptoFocusLost(evt);
            }
        });

        javax.swing.GroupLayout panProcessListUpdateLayout = new javax.swing.GroupLayout(panProcessListUpdate);
        panProcessListUpdate.setLayout(panProcessListUpdateLayout);
        panProcessListUpdateLayout.setHorizontalGroup(
            panProcessListUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panProcessListUpdateLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panProcessListUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panProcessListUpdateLayout.createSequentialGroup()
                        .addGroup(panProcessListUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panProcessListUpdateLayout.createSequentialGroup()
                                .addGroup(panProcessListUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(sptLine4, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panProcessListUpdateLayout.createSequentialGroup()
                                        .addGap(54, 54, 54)
                                        .addComponent(lblProcessListInstitutionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addGroup(panProcessListUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(panProcessListUpdateLayout.createSequentialGroup()
                                                .addComponent(txtProcessListLoanInstitutionName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addComponent(lblProcessListLoanCalcIntUpto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(lblLoanRecoveryId1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(panProcessListUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtProcessListRecoveryId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tdProcessListtLoanCalcIntUpto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panProcessListUpdateLayout.createSequentialGroup()
                                .addGroup(panProcessListUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panProcessListUpdateLayout.createSequentialGroup()
                                        .addGap(196, 196, 196)
                                        .addComponent(lblActHoldersList4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblLoanNoOfRecords1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panProcessListUpdateLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(srpProcessListSalaryRecoveryList, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(27, 27, 27)
                                .addComponent(lblNoOfProcessListRecordsVal, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(11, 11, 11))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panProcessListUpdateLayout.createSequentialGroup()
                        .addComponent(panProcessListSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        panProcessListUpdateLayout.setVerticalGroup(
            panProcessListUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panProcessListUpdateLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panProcessListUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panProcessListUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblProcessListInstitutionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtProcessListLoanInstitutionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblProcessListLoanCalcIntUpto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tdProcessListtLoanCalcIntUpto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(panProcessListUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblLoanRecoveryId1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProcessListRecoveryId, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(sptLine4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panProcessListUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panProcessListUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblActHoldersList4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblLoanNoOfRecords1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblNoOfProcessListRecordsVal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(srpProcessListSalaryRecoveryList, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panProcessListSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        tbpRecovery.addTab("Process List Balance Updation", panProcessListUpdate);

        lblStatus.setText("Status");

        javax.swing.GroupLayout panStatusLayout = new javax.swing.GroupLayout(panStatus);
        panStatus.setLayout(panStatusLayout);
        panStatusLayout.setHorizontalGroup(
            panStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblStatusVal, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(604, Short.MAX_VALUE))
        );
        panStatusLayout.setVerticalGroup(
            panStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panStatusLayout.createSequentialGroup()
                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
            .addGroup(panStatusLayout.createSequentialGroup()
                .addComponent(lblStatusVal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tbrTokenConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tbpRecovery, javax.swing.GroupLayout.PREFERRED_SIZE, 828, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(102, 102, 102)
                                .addComponent(lbSpace3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(panSalaryRecoveryList, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(203, 203, 203))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tbrTokenConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(lbSpace3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(panSalaryRecoveryList, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tbpRecovery, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(133, Short.MAX_VALUE))
        );

        tbpRecovery.getAccessibleContext().setAccessibleName("Recovery Process");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public void calculatetot() {
    }

    public void fillData(Object param) {

        final HashMap hash = (HashMap) param;
        System.out.println("Hash: " + hash);
        if (viewType == EDITVIEW || viewType == PROCESSEDITVIEW) {
            recoveryId = CommonUtil.convertObjToStr(hash.get("RECOVERY_ID"));
            observable.setTxtrecoveryId(recoveryId);
            observable.setTdtCalcIntUpto(CommonUtil.convertObjToStr(hash.get("INT_CALC_UPTO_DT")));
            observable.setInstName(CommonUtil.convertObjToStr(hash.get("INSTITUTION_ID")));
            populateData();
        } else {
            if (hash.containsKey("INST_NAME") && hash.get("INST_NAME") != null) {
                txtInstitutionName.setText(CommonUtil.convertObjToStr(hash.get("INST_NAME")));

            }
        }
    }

    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        HashMap hash = new HashMap();
        HashMap whereMap = new HashMap();
        if (viewType != EDITVIEW && viewType != PROCESSEDITVIEW) {
            viewMap.put(CommonConstants.MAP_NAME, "getInstitutionList");
            hash.put(CommonConstants.BRANCH_ID, com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, hash);
            new ViewAll(this, viewMap).show();
        } else if (viewType == PROCESSEDITVIEW) {
            if (tbpRecovery.getSelectedIndex() > 0) {
                if (tbpRecovery.getSelectedIndex() == 3) {
                    hash.put("PROD_TYPE", "TL");
                } else if (tbpRecovery.getSelectedIndex() == 1) {
                    if (CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("RD_MDS_RECOVERY")).equals("Y")) {
                        hash.put("PROD_TYPE", "TD");
                    } else {
                        hash.put("PROD_TYPE", "TL");
                    }
                } else if (tbpRecovery.getSelectedIndex() == 2) {
                    hash.put("PROD_TYPE", "MDS");
                }
            viewMap.put(CommonConstants.MAP_NAME, "getSalaryRecoveryDetail");
        hash.put(CommonConstants.BRANCH_ID, com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
        viewMap.put(CommonConstants.MAP_WHERE, hash);
        new ViewAll(this, viewMap).show();
            }
        }
        

    }
    private void setSelectedRecord() {
        int count = 0;
        if (tblLoanSalaryRecoveryList.getRowCount() > 0) {
            for (int i = 0, j = tblLoanSalaryRecoveryList.getRowCount(); i < j; i++) {
                if (((Boolean) tblLoanSalaryRecoveryList.getValueAt(i, 0)).booleanValue()) {
                    count += 1;
                }
            }
        }
        lblLoanSelectedCountVal.setText(String.valueOf(count));
    }

     private void setMDSSelectedRecord() {
        int count = 0;
        if (tblMDSSalaryRecoveryList.getRowCount() > 0) {
            for (int i = 0, j = tblMDSSalaryRecoveryList.getRowCount(); i < j; i++) {
                if (((Boolean) tblMDSSalaryRecoveryList.getValueAt(i, 0)).booleanValue()) {
                    count += 1;
                }
            }
        }
        lblMDSSelectedCountVal.setText(String.valueOf(count));
    }
     
     private void setRDSelectedRecord() {
        int count = 0;
        if (tblRDSalaryRecoveryList.getRowCount() > 0) {
            for (int i = 0, j = tblRDSalaryRecoveryList.getRowCount(); i < j; i++) {
                if (((Boolean) tblRDSalaryRecoveryList.getValueAt(i, 0)).booleanValue()) {
                    count += 1;
                }
            }
        }
        lblRDSelectedCountVal.setText(String.valueOf(count));
    }
     

    public Date ConvertDate(Date date) {

        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String s = df.format(date);
        String result = s;
        try {
            date = df.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /*
     * Fills up the HashMap with data when user selects the row in ViewAll
     * screen
     */
    public void populateData() {
//        updateOBFields();
        String behavesLike = "";
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        String mapName = "";
        boolean isOK = false;
        if (actionType != null && actionType.equals("PROCESS_EDIT") && recoveryId != null && recoveryId.length() > 0) {
            viewMap.put("RECOVERY_ID", recoveryId);
             if(tbpRecovery.getSelectedIndex() == 3){
               viewMap.put("PROD_TYPE","TL");
               mapName = "getSalaryRecoveryDetailsProcessingEdit";
            }else if(tbpRecovery.getSelectedIndex() == 1){
               if(CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("RD_MDS_RECOVERY")).equals("Y")){
                  viewMap.put("PROD_TYPE","TD"); 
                  mapName = "getRDSalaryRecoveryDetailsProcessingEdit";
               }else{   
                  viewMap.put("PROD_TYPE","TL");
                  mapName = "getSalaryRecoveryDetailsProcessingEdit";
               }
            }else if(tbpRecovery.getSelectedIndex() == 2){
               viewMap.put("PROD_TYPE","MDS"); 
               mapName = "getMDSSalaryRecoveryDetailsProcessingEdit";
            }else if(tbpRecovery.getSelectedIndex() == 4 && CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("RD_MDS_RECOVERY")).equals("Y")){
               mapName = "getAllRecoveryProcessedData";
            }
            viewMap.put(CommonConstants.MAP_NAME, mapName);
            try {
                log.info("populateData.ddddddddddd..");
                if (tbpRecovery.getSelectedIndex() == 3) {
                    ArrayList heading = observable.insertProcessTableData(viewMap, tblLoanSalaryRecoveryList);
                heading = null;
                    tblLoanSalaryRecoveryList.setAutoCreateRowSorter(true);
                    lblNoOfLoanRecordsVal.setText(String.valueOf(tblLoanSalaryRecoveryList.getRowCount()));
                    txtLoanRecoveryId.setText(recoveryId);
                    tdtLoanCalcIntUpto.setDateValue(observable.getTdtCalcIntUpto());
                    txtLoanInstitutionName.setText(observable.getInstName());
                    setTotalRecoveryPostAmtDetails();
                    if (observable.getColourList() != null && observable.getColourList().size() > 0) {
                        setColour();
                    }
                } else if (tbpRecovery.getSelectedIndex() == 1) {
                    if (CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("RD_MDS_RECOVERY")).equals("Y")) {
                        System.out.println("Execute here for RD nithya ::");
                        ArrayList heading = observable.insertRDProcessTableData(viewMap, tblRDSalaryRecoveryList);
                        heading = null;
                        tblRDSalaryRecoveryList.setAutoCreateRowSorter(true);
                        lblNoOfRDRecordsVal.setText(String.valueOf(tblRDSalaryRecoveryList.getRowCount()));
                        txtRDRecoveryId.setText(recoveryId);
                        tdtRDCalcIntUpto.setDateValue(observable.getTdtCalcIntUpto());
                        txtRDInstitutionName.setText(observable.getInstName());
                        setTotalRecoveryPostAmtDetails();
                    } else {
                        ArrayList heading = observable.insertProcessTableData(viewMap, tblLoanSalaryRecoveryList);
                        heading = null;                       
                        tblLoanSalaryRecoveryList.setAutoCreateRowSorter(true);
                        lblNoOfLoanRecordsVal.setText(String.valueOf(tblLoanSalaryRecoveryList.getRowCount()));
                        txtLoanRecoveryId.setText(recoveryId);
                        tdtLoanCalcIntUpto.setDateValue(observable.getTdtCalcIntUpto());
                        txtLoanInstitutionName.setText(observable.getInstName());
                        setTotalRecoveryPostAmtDetails();
                        if (observable.getColourList() != null && observable.getColourList().size() > 0) {
                            setColour();
                        }
                        
                    }
                } else if (tbpRecovery.getSelectedIndex() == 2) {
                    System.out.println("Execute here for MDS nithya ::");
                    ArrayList heading = observable.insertMDSProcessTableData(viewMap, tblMDSSalaryRecoveryList);
                    heading = null;
                    tblMDSSalaryRecoveryList.setAutoCreateRowSorter(true);
                    lblNoOfMDSRecordsVal.setText(String.valueOf(tblMDSSalaryRecoveryList.getRowCount()));
                    txtMDSRecoveryId.setText(recoveryId);
                    tdtMDSCalcIntUpto.setDateValue(observable.getTdtCalcIntUpto()); 
                    txtMDSInstitutionName.setText(observable.getInstName());
                    setTotalRecoveryPostAmtDetails();
                } else if(tbpRecovery.getSelectedIndex() == 4 && CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("RD_MDS_RECOVERY")).equals("Y")){
                    ArrayList heading = observable.insertAllProcessTableData(viewMap, tblProcessListSalaryRecoveryList);
                    heading = null;
                    tblProcessListSalaryRecoveryList.setAutoCreateRowSorter(true);
                    javax.swing.table.TableColumn col = tblProcessListSalaryRecoveryList.getColumn(tblProcessListSalaryRecoveryList.getColumnName(1));
                    col.setMaxWidth(120);
                    col.setMinWidth(120);
                    col.setWidth(120);
                    col.setPreferredWidth(120);
                    col = tblProcessListSalaryRecoveryList.getColumn(tblProcessListSalaryRecoveryList.getColumnName(2));
                    col.setMaxWidth(120);
                    col.setMinWidth(120);
                    col.setWidth(120);
                    col.setPreferredWidth(120);
                    col = tblProcessListSalaryRecoveryList.getColumn(tblProcessListSalaryRecoveryList.getColumnName(3));
                    col.setMaxWidth(250);
                    col.setMinWidth(250);
                    col.setWidth(250);
                    col.setPreferredWidth(250);
                    col = tblProcessListSalaryRecoveryList.getColumn(tblProcessListSalaryRecoveryList.getColumnName(4));
                    col.setMaxWidth(120);
                    col.setMinWidth(120);
                    col.setWidth(120);
                    col.setPreferredWidth(120);
                    tblProcessListSalaryRecoveryList.setRowHeight(tblProcessListSalaryRecoveryList.getRowHeight() + 8);
                    lblProcessListSelectedCountVal.setText(String.valueOf(tblProcessListSalaryRecoveryList.getRowCount()));
                    calculateTotalRecoveryAmountForEachCustomer();
                    txtProcessListRecoveryId.setText(recoveryId);
                    tdProcessListtLoanCalcIntUpto.setDateValue(observable.getTdtCalcIntUpto()); 
                    txtProcessListLoanInstitutionName.setText(observable.getInstName());
                    btnProcessListSave.setEnabled(true);
               }
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
        }
        viewMap = null;
        whereMap = null;
    }

    
    private void calculateTotalRecoveryAmountForEachCustomer() {
        double totalAmt = 0.0;
        if (tblProcessListSalaryRecoveryList.getRowCount() > 0) {
            for (int i = 0; i < tblProcessListSalaryRecoveryList.getRowCount(); i++) {
                if (CommonUtil.convertObjToDouble(tblProcessListSalaryRecoveryList.getValueAt(i, 4)) > 0) {
                    totalAmt += CommonUtil.convertObjToDouble(tblProcessListSalaryRecoveryList.getValueAt(i, 4));
                }
            }
        }
        lblProcessListTotalTransactionAmtVal.setText(String.valueOf(totalAmt));
    }
    
    
    
    
     private void setColour() {
        /* Set a cellrenderer to this table in order format the date */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (observable.getColourList().contains(String.valueOf(tblLoanSalaryRecoveryList.getValueAt(row, 4)))) {
                    setForeground(Color.RED);
                } else {
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblLoanSalaryRecoveryList.setDefaultRenderer(Object.class, renderer);
    }
    
    
    
    private void setTotalRecoveryPostAmtDetails() {
        double totalRecoveryAmt = 0.0;
        double totalPostedAmt = 0.0;
        HashMap whereMap = new HashMap();
        whereMap.put("RECOVERY_ID", recoveryId);
        whereMap.put("INT_CALC_UPTO_DT", DateUtil.getDateMMDDYYYY(observable.getTdtCalcIntUpto()));
        List mapDataList = ClientUtil.executeQuery("getProcessedPostAmtDetails", whereMap);
        if (mapDataList != null && mapDataList.size() > 0) {
            whereMap = (HashMap) mapDataList.get(0);
            if (whereMap.containsKey("TOTAL_INSTITUTION_REC_AMT") && whereMap.get("TOTAL_INSTITUTION_REC_AMT") != null) {
                totalRecoveryAmt = CommonUtil.convertObjToDouble(whereMap.get("TOTAL_INSTITUTION_REC_AMT"));
            }
            if (whereMap.containsKey("POSTED_AMT") && whereMap.get("POSTED_AMT") != null) {
                totalPostedAmt = CommonUtil.convertObjToDouble(whereMap.get("POSTED_AMT"));
            }
        }
        if (tbpRecovery.getSelectedIndex() == 1) {
            if (CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("RD_MDS_RECOVERY")).equals("Y")) {
                lblRDInstRecAmtVal.setText(String.valueOf(totalRecoveryAmt));
                lblRDPostedAmtVal.setText(String.valueOf(totalPostedAmt));
            } else {
                lblTLInstRecAmtVal.setText(String.valueOf(totalRecoveryAmt));
                lblTLPostedAmtVal.setText(String.valueOf(totalPostedAmt));
            }
        }
        if (tbpRecovery.getSelectedIndex() == 2) {
            lblMDSInstRecAmtVal.setText(String.valueOf(totalRecoveryAmt));
            lblMDSPostAmtVal.setText(String.valueOf(totalPostedAmt));

        }
        if (tbpRecovery.getSelectedIndex() == 3) {
            lblTLInstRecAmtVal.setText(String.valueOf(totalRecoveryAmt));
            lblTLPostedAmtVal.setText(String.valueOf(totalPostedAmt));
        }
    }
    
    

    public void setTblModel(final CTable tbl, ArrayList tblData, ArrayList head) {
        TableSorter tableSorter = new TableSorter();
        TableModel tableModel = new TableModel(tblData, head) {

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 0 || mColIndex == 4 || mColIndex == 5 || mColIndex == 6 || mColIndex == 7 || mColIndex == 9 || mColIndex == 11
                        || mColIndex == 12 || mColIndex == 13) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        tableModel.fireTableDataChanged();
        tbl.setModel(tableSorter);
        tbl.revalidate();
    }

    private void createCboProdType() {
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

    public void valueChanged(javax.swing.event.ListSelectionEvent e) {
    }
    
    private HashMap firstEnteredActNo() {
//        String sbAcNo = COptionPane.showInputDialog(this, resourceBundle.getString("REMARK_TRANSFER_TRANS"));
        HashMap acctDetailsMap = new HashMap();
        acctsearch = new AcctSearchUI();
        acctsearch.show();
        String sbAcNo = acctsearch.getAccountNo();
        String prodType = acctsearch.getProdType();
        String productId = acctsearch.getProductId();
        System.out.println("prodType" + prodType);
        System.out.println("sbAcNo" + sbAcNo);
        System.out.println("prodid" + productId);
        acctDetailsMap.put("ACT_NUM", sbAcNo);
        acctDetailsMap.put("PROD_TYPE", prodType);
        acctDetailsMap.put("PRODUCT_ID", productId);
        return acctDetailsMap;
    }

    private boolean checkingActNo(String sbAcNo) {
        boolean flag = false;
        HashMap existingMap = new HashMap();
        existingMap.put("ACT_NUM", sbAcNo.toUpperCase());
        List mapDataList = ClientUtil.executeQuery("getAccNoDet", existingMap);
        if (mapDataList != null && mapDataList.size() > 0) {
            existingMap = (HashMap) mapDataList.get(0);
            if (existingMap != null && !ProxyParameters.BRANCH_ID.equals(CommonUtil.convertObjToStr(existingMap.get("BRANCH_CODE")))) {
                Date selectedBranchDt = ClientUtil.getOtherBranchCurrentDate(CommonUtil.convertObjToStr(existingMap.get("BRANCH_CODE")));
                Date currentDate = ClientUtil.getCurrentDate();
                if (selectedBranchDt == null) {
                    ClientUtil.displayAlert("BOD is not completed for the selected branch " + "\n" + "Interbranch Transaction Not allowed");
                    flag = false;
                    finalChecking = true;
                } else {
                    System.out.println("Continue for interbranch trasactions ...");
                    String[] obj5 = {"Proceed", "ReEnter"};
                    chktrans = false;
                    int option4 = COptionPane.showOptionDialog(null, ("Please check whether Account No, Name correct or not " + "\nOperative AcctNo is : " + CommonUtil.convertObjToStr((existingMap.get("Account Number") == null) ? existingMap.get("ACCOUNT NUMBER") : existingMap.get("Account Number")) + "\nCustomer Name :" + CommonUtil.convertObjToStr((existingMap.get("Customer Name") == null) ? existingMap.get("CUSTOMER NAME") : existingMap.get("Customer Name"))), ("Transaction Part"),
                            COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj5, obj5[0]);
                    if (option4 == 0) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                }
            } else {
                String[] obj5 = {"Proceed", "ReEnter"};
                chktrans = false;
                int option4 = COptionPane.showOptionDialog(null, ("Please check whether Account No, Name correct or not " + "\nOperative AcctNo is : " + CommonUtil.convertObjToStr((existingMap.get("Account Number") == null) ? existingMap.get("ACCOUNT NUMBER") : existingMap.get("Account Number")) + "\nCustomer Name :" + CommonUtil.convertObjToStr((existingMap.get("Customer Name") == null) ? existingMap.get("CUSTOMER NAME") : existingMap.get("Customer Name"))), ("Transaction Part"),
                        COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj5, obj5[0]);
                if (option4 == 0) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        }
        return flag;
    }

    public void displayPrintDetails(String arc_id) {
        try {
            System.out.println("transIdMap=11==" + arc_id);
            HashMap transTypeMap = new HashMap();
            HashMap transMap = new HashMap();
            HashMap transCashMap = new HashMap();
            String reportName = "";
            transCashMap.put("BATCH_ID", arc_id);
            transCashMap.put("TRANS_DT", currDt);
            transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            HashMap transIdMap = new HashMap();
            List list = null;
            list = ClientUtil.executeQuery("getTransferDetails", transCashMap);
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    transMap = (HashMap) list.get(i);
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                }
            }
            int yesNo = 0;
            String[] voucherOptions = {"Yes", "No"};
            if (list != null && list.size() > 0) {
                yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, voucherOptions, voucherOptions[0]);
                if (yesNo == 0) {
                    com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    paramMap.put("TransDt", currDt);
                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                    Object keys1[] = transIdMap.keySet().toArray();
                    for (int i = 0; i < keys1.length; i++) {
                        paramMap.put("TransId", keys1[i]);
                        ttIntgration.setParam(paramMap);
                        reportName = "ReceiptPayment";
                        ttIntgration.integrationForPrint(reportName, false);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
private void btnClose1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose1ActionPerformed
//    observable.removeRowsFromGuarantorTable(tblSalaryRecoveryList);
    dispose();
}//GEN-LAST:event_btnClose1ActionPerformed

private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
//    ClientUtil.clearAll(this);
    lblNoOfRecordsVal.setText("");
    lblTotalTransactionAmtVal.setText("");
    lblSelectedCountVal.setText("");
    setButtonEnableDisable();
    enableDisableComponants(false);
    btnProcessEdit.setEnabled(true);
    txtInstitutionName.setText("");
    tdtCalcIntUpto.setDateValue("");
     ClientUtil.clearAll(this);
//    observable.resetForm();
     resetCountValues();
}//GEN-LAST:event_btnCancelActionPerformed

private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
    actionType = "NEW";
    hideRecalculateButton();
    setButtonEnableDisable();
    enableDisableComponants(true);
    chkSelectAll.setVisible(false);
    btnGenerateNotice.setEnabled(false);
}//GEN-LAST:event_btnNewActionPerformed
    private void enableDisableComponants(boolean flag) {
        btnGenerateNotice.setEnabled(flag);
        txtInstitutionName.setEnabled(flag);
        btnInstitution.setEnabled(flag);
        tdtCalcIntUpto.setEnabled(flag);
        btnGenerate.setEnabled(flag);
        btnPrintRecoveryList.setEnabled(flag);
        btnExportRecoveryList.setEnabled(flag);
        panSalaryRecoveryOptions.setEnabled(flag);
        
        btnRDGenerateNotice.setEnabled(flag);
        txtMDSInstitutionName.setEnabled(flag);
        tdtMDSCalcIntUpto.setEnabled(flag);
        
    }
private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    actionType = "EDIT";
    setButtonEnableDisable();
    enableDisableComponants(false);
    btnProcessEdit.setEnabled(false);
    btnPrint.setEnabled(true);
    popUp(EDITVIEW);
}//GEN-LAST:event_btnEditActionPerformed

private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_btnAuthorizeActionPerformed

private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
    if (actionType != null && actionType.equals("EDIT")) {
        ArrayList arbList = new ArrayList();
        for (int i = 0; i < tblSalaryRecoveryList.getRowCount(); i++) {
            if (((Boolean) tblSalaryRecoveryList.getValueAt(i, 0)).booleanValue()) {
                ArrayList list = new ArrayList();
                list.add(tblSalaryRecoveryList.getValueAt(i, 1));
                list.add(tblSalaryRecoveryList.getValueAt(i, 11));
                arbList.add(list);
            }
        }
        HashMap arbMap = new HashMap();
        arbMap.put("ARBITRATION_EDIT_LIST", arbList);
    } else {
    }
}//GEN-LAST:event_btnSaveActionPerformed

    private void btnProcessEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessEditActionPerformed
        // TODO add your handling code here:
        System.out.println("tabindex selected :: " + tbpRecovery.getSelectedIndex());
        actionType = "PROCESS_EDIT";
        btnGenerateNotice.setEnabled(true);
        btnRDGenerateNotice.setEnabled(true);
        btnEdit.setEnabled(false);
        btnCancel.setEnabled(true);
        chkSelectAll.setVisible(true);
        ClientUtil.enableDisable(panSalaryRecoveryList, true);
        popUp(PROCESSEDITVIEW);
    }//GEN-LAST:event_btnProcessEditActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnRecalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecalculateActionPerformed

    }//GEN-LAST:event_btnRecalculateActionPerformed

    private void btnPrintNoticeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintNoticeActionPerformed

    }//GEN-LAST:event_btnPrintNoticeActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this);
        enableDisableComponants(false);
        setButtonEnableDisable();
        txtInstitutionName.setText("");
        tdtCalcIntUpto.setDateValue("");
        //       observable.resetForm();
        ClientUtil.enableDisable(panSalaryRecoveryList, false);
        lblNoOfRecordsVal.setText("");
        lblTotalTransactionAmtVal.setText("");
        lblSelectedCountVal.setText("");
        //btnProcessEdit.setEnabled(true);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnGenerateNoticeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateNoticeActionPerformed
//        if (!actionType.equals("EDIT")) {
//            ArrayList arbList = new ArrayList();
//            for (int i = 0; i < tblSalaryRecoveryList.getRowCount(); i++) {
//                if (((Boolean) tblSalaryRecoveryList.getValueAt(i, 0)).booleanValue()) {
//                    ArrayList list = new ArrayList();
//                    list.add(tblSalaryRecoveryList.getValueAt(i, 1));
//                    list.add(tblSalaryRecoveryList.getValueAt(i, 2));
//                    list.add(tblSalaryRecoveryList.getValueAt(i, 3));
//                    list.add(tblSalaryRecoveryList.getValueAt(i, 4));
//                    list.add(tblSalaryRecoveryList.getValueAt(i, 8));
//                    list.add(tblSalaryRecoveryList.getValueAt(i, 9));
//                    list.add(tblSalaryRecoveryList.getValueAt(i, 10));
//                    list.add(tblSalaryRecoveryList.getValueAt(i, 11));
//                    list.add(tblSalaryRecoveryList.getValueAt(i, 12));
//                    list.add(tblSalaryRecoveryList.getValueAt(i, 14));
//                    list.add(tblSalaryRecoveryList.getValueAt(i, 15));
//                    arbList.add(list);
//                }
//            }
//            HashMap processMap = new HashMap();
//            processMap.put("SALARYRECOVERY_POST_LIST", arbList);
//            observable.setFinalMap(processMap);
//            observable.setRecoveryProdType("TL");
//            HashMap authDataMap = new HashMap();
//            String[] debitType = {"Transfer"};
//            int option3 = 0;
//            if (option3 == 0) {
//                String transType = "";
//                while (CommonUtil.convertObjToStr(transType).length() == 0) {
//                    transType = (String) COptionPane.showInputDialog(null, "Select Transaction Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null, debitType, "");
//                    if (CommonUtil.convertObjToStr(transType).length() > 0) {
//                        authDataMap.put("TRANSACTION_PART", "TRANSACTION_PART");
//                        authDataMap.put("TRANS_TYPE", transType.toUpperCase());
//                        if (CommonUtil.convertObjToStr(transType.toUpperCase()).equals("TRANSFER")) {
//                            boolean flag = true;
//                            do {
//                                String sbAcNo = null;
//                                String prodType = null;
//                                HashMap acctDetailsMap = firstEnteredActNo();
//                                if (acctDetailsMap != null && acctDetailsMap.size() > 0 && acctDetailsMap.containsKey("ACT_NUM") && acctDetailsMap.containsKey("PROD_TYPE")) {
//                                    sbAcNo = CommonUtil.convertObjToStr(acctDetailsMap.get("ACT_NUM"));
//                                    prodType = CommonUtil.convertObjToStr(acctDetailsMap.get("PROD_TYPE"));
//                                }
//                                if (sbAcNo != null && sbAcNo.length() > 0) {
//                                    flag = checkingActNo(sbAcNo);
//                                    flag = true;
//                                    if (flag == false && finalChecking == false) {
//                                        ClientUtil.showAlertWindow("Account No is invalid, Please enter correct no");
//                                        return;
//                                    } else {
//                                        authDataMap.put("DR_ACT_NUM", sbAcNo);
//                                        authDataMap.put("DR_PROD_TYPE", prodType);
//                                        authDataMap.put("USER_ID", ProxyParameters.USER_ID);
//                                        chkok = false;
//                                    }
//                                    finalChecking = false;
//                                } else {
//                                    ClientUtil.showMessageWindow("Transaction Not Created");
//                                    flag = true;
//                                    authDataMap.remove("TRANSACTION_PART");
//                                    observable.setNewTransactionMap(null);
//                                    chktrans = false;
//                                    return;
//
//                                }
//                            } while (!flag);
//                        }
//                        observable.setNewTransactionMap(authDataMap);
//                    } else {
//                        transType = "Cancel";
//                        chktrans = true;
//                    }
//                }
//            }
//        }
//        CommonUtil comm = new CommonUtil();
//        final JDialog loading = comm.addProgressBar();
//        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
//
//            @Override
//            protected Void doInBackground() throws InterruptedException /**
//            * Execute some operation
//            */
//            {
//                observable.doActionPerform();
//                return null;
//            }
//
//            @Override
//            protected void done() {
//                loading.dispose();
//            }
//        };
//        worker.execute();
//        loading.show();
//        try {
//            worker.get();
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        } //Progress bar code ends here
//        try {
//            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0 && observable.getProxyReturnMap().containsKey("STATUS")) {
//                HashMap proxyResultMap = observable.getProxyReturnMap();
//                if (CommonUtil.convertObjToStr(proxyResultMap.get("STATUS")).equals("COMPLETED")) {
//                    ClientUtil.showMessageWindow("Transaction Completed!!!");
//                    //observable.resetForm();
//                    btnClearActionPerformed(null);
//                } else {
//                    ClientUtil.showMessageWindow("Transaction Failed!!!");
//                    observable.resetForm();
//                    btnClearActionPerformed(null);
//                }
//                ClientUtil.clearAll(this);
//            }
//        } catch (Exception ex) {
//            java.util.logging.Logger.getLogger(LoanRecoveryUI.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_btnGenerateNoticeActionPerformed

    private void tblSalaryRecoveryListPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblSalaryRecoveryListPropertyChange
        // TODO add your handling code here:
        double totalRecoAmt = 0.0;
        LinkedHashMap balMap = new LinkedHashMap();
        if (tblSalaryRecoveryList.getSelectedRowCount() > 0 && tblSalaryRecoveryList.getValueAt(tblSalaryRecoveryList.getSelectedRow(), 0).equals(true)) {
            int row = tblSalaryRecoveryList.getSelectedRow();
            int col = tblSalaryRecoveryList.getSelectedColumn();
            
            String custId = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(row, 15));
            double totalDemand = CommonUtil.convertObjToDouble(tblSalaryRecoveryList.getValueAt(row, 7));

            if (col == 12) {
               
                for (int i = 0; i < tblSalaryRecoveryList.getRowCount(); i++) {
                    if (custId.equals(CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(i, 15))) && CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(i, 0)).equals("true")) {
                        totalRecoAmt += CommonUtil.convertObjToDouble(tblSalaryRecoveryList.getValueAt(i, 12));
                    }
                }

                for (int i = 0; i < tblSalaryRecoveryList.getRowCount(); i++) {
                    //System.out.println("recovery amount :: " + tblSalaryRecoveryList.getValueAt(i, 14));
                    if (tblSalaryRecoveryList.getValueAt(i, 14) != null && CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(i, 14)).length() > 0) {
                        balMap.put(tblSalaryRecoveryList.getValueAt(i, 15), tblSalaryRecoveryList.getValueAt(i, 14));
                    }
                }

                if(balMap.containsKey(custId) && balMap.get(custId) != null){
                    if(CommonUtil.convertObjToDouble(balMap.get(custId)) < totalRecoAmt){
                        ClientUtil.showMessageWindow("Total Recover Amount - " + totalRecoAmt +"\n" + "Balance - "+ CommonUtil.convertObjToDouble(balMap.get(custId)));
                        tblSalaryRecoveryList.setValueAt(totalDemand,row, 12);
                    }
                }
            }
        }
    }//GEN-LAST:event_tblSalaryRecoveryListPropertyChange

    private void tblSalaryRecoveryListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSalaryRecoveryListMouseClicked
        // TODO add your handling code here:
        double totAmount = 0;
        int totalCount = 0;
        String st = "";
        double totalRecoAmt = 0.0;
        LinkedHashMap balMap = new LinkedHashMap();
        if (tblSalaryRecoveryList.getSelectedRowCount() > 0 && tblSalaryRecoveryList.getValueAt(tblSalaryRecoveryList.getSelectedRow(), 0).equals(true)) {
            int row = tblSalaryRecoveryList.getSelectedRow();
            int col = tblSalaryRecoveryList.getSelectedColumn();
            String custId = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(row, 15));
            double totalDemand = CommonUtil.convertObjToDouble(tblSalaryRecoveryList.getValueAt(row, 7));

            for (int i = 0; i < tblSalaryRecoveryList.getRowCount(); i++) {
                if (custId.equals(CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(i, 15))) && CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(i, 0)).equals("true")) {
                    totalRecoAmt += CommonUtil.convertObjToDouble(tblSalaryRecoveryList.getValueAt(i, 12));
                }
            }

            for (int i = 0; i < tblSalaryRecoveryList.getRowCount(); i++) {
                //System.out.println("recovery amount :: " + tblSalaryRecoveryList.getValueAt(i, 14));
                if (tblSalaryRecoveryList.getValueAt(i, 14) != null && CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(i, 14)).length() > 0) {
                    balMap.put(tblSalaryRecoveryList.getValueAt(i, 15), tblSalaryRecoveryList.getValueAt(i, 14));
                }
            }

            if (balMap.containsKey(custId) && balMap.get(custId) != null) {
                if (CommonUtil.convertObjToDouble(balMap.get(custId)) < totalRecoAmt) {
                    ClientUtil.showMessageWindow("Total Recover Amount - " + totalRecoAmt + "\n" + "Balance - " + CommonUtil.convertObjToDouble(balMap.get(custId)));
                    tblSalaryRecoveryList.setValueAt(false, row, 0);
                }
            }

        }

        for (int i = 0; i < tblSalaryRecoveryList.getRowCount(); i++) {
            st = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(i, 0));
            if (st.equals("true")) {
                totAmount = totAmount + CommonUtil.convertObjToDouble(tblSalaryRecoveryList.getValueAt(i, 12)).doubleValue();
                totalCount++;
            }
        }
        lblSelectedCountVal.setText(String.valueOf(totalCount));
        lblTotalTransactionAmtVal.setText(String.valueOf(totAmount));
    }//GEN-LAST:event_tblSalaryRecoveryListMouseClicked

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // Add your handling code here:
//        LinkedHashMap balMap = new LinkedHashMap();
//        observable.setSelectAll(tblSalaryRecoveryList, new Boolean(chkSelectAll.isSelected()));
//        setSelectedRecord();
//        calcProcessTotal();
//        for(int i=0; i<tblSalaryRecoveryList.getRowCount(); i++){
//            //System.out.println("recovery amount :: " + tblSalaryRecoveryList.getValueAt(i, 14));
//            if(tblSalaryRecoveryList.getValueAt(i, 14) != null && CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(i, 14)).length() > 0){
//                balMap.put(tblSalaryRecoveryList.getValueAt(i, 15),tblSalaryRecoveryList.getValueAt(i, 14));
//            }
//        }
//        
//        
//        System.out.println("balMap :: " + balMap);
//        LinkedHashMap custDataMap = checkbalAmountforCustomer(balMap);
//        blockCustIDRow(custDataMap,balMap);
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void blockCustIDRow(LinkedHashMap custDataMap,LinkedHashMap balMap){
        Iterator iterate = balMap.entrySet().iterator();
         if (null != balMap && balMap.size() > 0) {
            ArrayList list = new ArrayList();
            while (iterate.hasNext()) {
                Map.Entry entry = (Map.Entry) iterate.next();
                Object key1 = (Object) entry.getKey();
                Object value = (Object) entry.getValue();
                String checkCustId = CommonUtil.convertObjToStr(key1);
                double checkBal = CommonUtil.convertObjToDouble(value);
                if(custDataMap.containsKey(checkCustId) && custDataMap.get(checkCustId) != null){
                    if(CommonUtil.convertObjToDouble(custDataMap.get(checkCustId)) > checkBal){
                        for(int i=0;i<tblLoanSalaryRecoveryList.getRowCount();i++){
                            if(CommonUtil.convertObjToStr(tblLoanSalaryRecoveryList.getValueAt(i, 15)).equals(checkCustId)){
                                tblLoanSalaryRecoveryList.setValueAt(false, i, 0);
                            }
                        }
                    }
                }
            }
         }
    }
    
    private LinkedHashMap checkbalAmountforCustomer(LinkedHashMap balMap) {
        Iterator iterate = balMap.entrySet().iterator();
        LinkedHashMap custDataMap = new LinkedHashMap();
        double totalCustAmt = 0.0;
        if (null != balMap && balMap.size() > 0) {
            ArrayList list = new ArrayList();
            while (iterate.hasNext()) {
                Map.Entry entry = (Map.Entry) iterate.next();
                Object key1 = (Object) entry.getKey();
                Object value = (Object) entry.getValue();
                String checkCustId = CommonUtil.convertObjToStr(key1);
                double checkBal = CommonUtil.convertObjToDouble(value);
                for (int k = 0; k < tblLoanSalaryRecoveryList.getRowCount(); k++) {
                    if(checkCustId.equals(CommonUtil.convertObjToStr(tblLoanSalaryRecoveryList.getValueAt(k, 15)))){
                    double recAmt = CommonUtil.convertObjToDouble(tblLoanSalaryRecoveryList.getValueAt(k, 12));
                        System.out.println("custDataMap here first :: " + custDataMap);
                    if (custDataMap.containsKey(tblLoanSalaryRecoveryList.getValueAt(k, 15))) {
                        totalCustAmt = CommonUtil.convertObjToDouble(custDataMap.get(tblLoanSalaryRecoveryList.getValueAt(k, 15)));
                        custDataMap.put(tblLoanSalaryRecoveryList.getValueAt(k, 15), totalCustAmt + recAmt);
                    } else {
                        custDataMap.put(tblLoanSalaryRecoveryList.getValueAt(k, 15), recAmt);
                    }
                  }
                }
            }
        }
        System.out.println("custDataMap :: " + custDataMap);
        return custDataMap;
    }
    
    private void btnInstitutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInstitutionActionPerformed
        // TODO add your handling code here:
        popUp(1);
    }//GEN-LAST:event_btnInstitutionActionPerformed

    private void btnExportRecoveryListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportRecoveryListActionPerformed
        // TODO add your handling code here:

        if (tblSalaryRecoveryList.getRowCount() > 0) {
            try {
                createExcelFile();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(LoanRecoveryUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            ClientUtil.showMessageWindow("Your excel file has been Generated!!! ");
            btnExportRecoveryList.setEnabled(false);
            HashMap whereMap = new HashMap();
            whereMap.put("INT_CALC_UPTO_DT", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtCalcIntUpto.getDateValue())));
            ClientUtil.execute("updateRecoveryListExportStatus", whereMap);
        } else {
            ClientUtil.showMessageWindow("No Record in This Table!!! ");
        }
    }//GEN-LAST:event_btnExportRecoveryListActionPerformed

    private void btnPrintRecoveryListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintRecoveryListActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintRecoveryListActionPerformed

    private void btnGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateActionPerformed

        if (tdtCalcIntUpto.getDateValue().equals("")) {
            ClientUtil.showAlertWindow("Please enter 'Calculate Interest upto' Date");
        } else if (txtInstitutionName.getText().equals("")) {
            ClientUtil.showAlertWindow("Please enter Institution Name");
        } else {
            HashMap whereMap = new HashMap();
            whereMap.put("INT_CALC_UPTO_DT", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtCalcIntUpto.getDateValue())));
            whereMap.put("EXPORT_LIST", "EXPORT_LIST");
            whereMap.put("INST_ID", txtInstitutionName.getText());
            List recoveryList = ClientUtil.executeQuery("checkingSameDateLoanRecord", whereMap);
            if (recoveryList != null && recoveryList.size() > 0) {
                int dialogButton = COptionPane.YES_NO_OPTION;
                int dialogResult = COptionPane.showConfirmDialog(null, "Recovery List Already Exported for This Date... !!!Do you wish to generate again??", "Warning", dialogButton);
                if (dialogResult == 0) {
                    HashMap whereMap1 = new HashMap();
                    whereMap1.put("INT_CALC_UPTO_DT", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtCalcIntUpto.getDateValue())));
                    ClientUtil.execute("updateRecoveryListExportStatusNo", whereMap1);
                    btnGenerate.setEnabled(true);
                }
            } else {
                observable.setTdtCalcIntUpto(tdtCalcIntUpto.getDateValue());
                observable.setInstName(txtInstitutionName.getText());
                CommonUtil comm = new CommonUtil();
                final JDialog loading = comm.addProgressBar();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws InterruptedException /**
                     * Execute some operation
                     */
                    {
                        ArrayList heading = observable.insertTableData(null, tblSalaryRecoveryList);
                        heading = null;
                        return null;
                    }

                    @Override
                    protected void done() {
                        loading.dispose();
                    }
                };
                worker.execute();
                loading.show();
                try {
                    worker.get();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                tblSalaryRecoveryList.setAutoCreateRowSorter(true);;
                calcTotal();
                if (tblSalaryRecoveryList.getRowCount() > 0) {
                    tdtCalcIntUpto.setEnabled(false);
                    btnGenerate.setEnabled(false);
                } else {
                    ClientUtil.showMessageWindow(" No Data !!! ");
                    btnCancelActionPerformed(null);
                }
            }
        }
    }//GEN-LAST:event_btnGenerateActionPerformed

    private void tdtCalcIntUptoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtCalcIntUptoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtCalcIntUptoFocusLost

    private void chkRDSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRDSelectAllActionPerformed
        // TODO add your handling code here:
        observable.setSelectAll(tblRDSalaryRecoveryList, new Boolean(chkRDSelectAll.isSelected()));        
        calcRDProcessTotal();
        setRDSelectedRecord();
    }//GEN-LAST:event_chkRDSelectAllActionPerformed

    private void tdtRDCalcIntUptoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtRDCalcIntUptoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtRDCalcIntUptoFocusLost

    private void tblRDSalaryRecoveryListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRDSalaryRecoveryListMouseClicked
        // TODO add your handling code here:
        if(tblRDSalaryRecoveryList.getRowCount() > 0){
            int row = tblRDSalaryRecoveryList.getSelectedRow();
            int col = tblRDSalaryRecoveryList.getSelectedColumn();
            String custId = CommonUtil.convertObjToStr(tblRDSalaryRecoveryList.getValueAt(row, 15));
            String accNo = CommonUtil.convertObjToStr(tblRDSalaryRecoveryList.getValueAt(row, 4));
            int instNo = CommonUtil.convertObjToInt(tblRDSalaryRecoveryList.getValueAt(row, 6));
            String st = CommonUtil.convertObjToStr(tblRDSalaryRecoveryList.getValueAt(tblRDSalaryRecoveryList.getSelectedRow(), 0));
            if(st.equals("true")){
                boolean instExists = checkMinInstallmentsRD(custId, instNo,accNo);
                if(instExists){
                    tblRDSalaryRecoveryList.setValueAt(false, tblRDSalaryRecoveryList.getSelectedRow(), 0);
                }
            }
        }
        
         double totAmount = 0;
        int totalCount = 0;
        String st = "";
        for (int i = 0; i < tblRDSalaryRecoveryList.getRowCount(); i++) {
            st = CommonUtil.convertObjToStr(tblRDSalaryRecoveryList.getValueAt(i, 0));
            if (st.equals("true")) {
                totAmount = totAmount + CommonUtil.convertObjToDouble(tblRDSalaryRecoveryList.getValueAt(i, 12)).doubleValue();
                totalCount++;
            }
        }
        lblRDSelectedCountVal.setText(String.valueOf(totalCount));
        lblRDTotalTransactionAmtVal.setText(String.valueOf(totAmount));
        
    }//GEN-LAST:event_tblRDSalaryRecoveryListMouseClicked

    private void tblRDSalaryRecoveryListPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblRDSalaryRecoveryListPropertyChange
        // TODO add your handling code here:
         if (tblRDSalaryRecoveryList.getSelectedRowCount() > 0 && tblRDSalaryRecoveryList.getValueAt(tblRDSalaryRecoveryList.getSelectedRow(), 0).equals(true)) {
            int row = tblRDSalaryRecoveryList.getSelectedRow();
            int col = tblRDSalaryRecoveryList.getSelectedColumn();
            String custId = CommonUtil.convertObjToStr(tblRDSalaryRecoveryList.getValueAt(row, 15));
            int instNo = CommonUtil.convertObjToInt(tblRDSalaryRecoveryList.getValueAt(row, 6));
            if (col == 10) {                
                double penalAmt = CommonUtil.convertObjToDouble(tblRDSalaryRecoveryList.getValueAt(row, 10));
                double principal = CommonUtil.convertObjToDouble(tblRDSalaryRecoveryList.getValueAt(row, 8));
                tblRDSalaryRecoveryList.setValueAt(String.valueOf(principal+penalAmt), tblRDSalaryRecoveryList.getSelectedRow(), 12);            
            }
            if(col == 14){                
                setRDRecoveryBalance(custId);
            }
//            if(col == 0){
//                boolean instExists = checkMinInstallmentsRD(custId, instNo);
//                if(instExists){
//                    tblRDSalaryRecoveryList.setValueAt(false, tblRDSalaryRecoveryList.getSelectedRow(), 0);
//                }
//            }
        }
    }//GEN-LAST:event_tblRDSalaryRecoveryListPropertyChange

    private boolean checkMinInstallmentsRD(String custId, int instNo, String accNum){
        boolean instExists = false;
        for (int i = 0; i < tblRDSalaryRecoveryList.getRowCount(); i++) {
            if (CommonUtil.convertObjToStr(tblRDSalaryRecoveryList.getValueAt(i, 15)).equals(custId) && CommonUtil.convertObjToStr(tblRDSalaryRecoveryList.getValueAt(i, 4)).equals(accNum) &&CommonUtil.convertObjToInt(tblRDSalaryRecoveryList.getValueAt(i, 6)) < instNo && tblRDSalaryRecoveryList.getValueAt(i, 0).equals(false)) {                
                instExists =  true;
            }
        }
        return instExists;
        
    }
    
     private boolean checkMinInstallmentsMDS(String custId, int instNo, String accNo){
        boolean instExists = false;
        for (int i = 0; i < tblMDSSalaryRecoveryList.getRowCount(); i++) {
            if (CommonUtil.convertObjToStr(tblMDSSalaryRecoveryList.getValueAt(i, 18)).equals(custId) && CommonUtil.convertObjToStr(tblMDSSalaryRecoveryList.getValueAt(i, 4)).equals(accNo) && CommonUtil.convertObjToInt(tblMDSSalaryRecoveryList.getValueAt(i, 7)) < instNo && tblMDSSalaryRecoveryList.getValueAt(i, 0).equals(false)) {                
                instExists =  true;
            }
        }
        return instExists;
        
    }
    
    
    private void setRDRecoveryBalance(String custId){
        double recoveryBalance = CommonUtil.convertObjToDouble(tblRDSalaryRecoveryList.getValueAt(tblRDSalaryRecoveryList.getSelectedRow(), 14));
        System.out.println("inside RD recovery balance :: " + recoveryBalance);
        for (int i = 0; i < tblRDSalaryRecoveryList.getRowCount(); i++) {
            if (CommonUtil.convertObjToStr(tblRDSalaryRecoveryList.getValueAt(i, 15)).equals(custId)) {                
                tblRDSalaryRecoveryList.setValueAt(String.valueOf(recoveryBalance), i, 14);
            }
        }
    }
    
    
    
    private void btnRDGenerateNoticeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRDGenerateNoticeActionPerformed
        // TODO add your handling code here:        
         if (!actionType.equals("EDIT")) {
            ArrayList rdList = new ArrayList();
            for (int i = 0; i < tblRDSalaryRecoveryList.getRowCount(); i++) {
                if (((Boolean) tblRDSalaryRecoveryList.getValueAt(i, 0)).booleanValue()) {
                    ArrayList list = new ArrayList();
                    list.add(tblRDSalaryRecoveryList.getValueAt(i, 1));
                    list.add(tblRDSalaryRecoveryList.getValueAt(i, 2));
                    list.add(tblRDSalaryRecoveryList.getValueAt(i, 3));
                    list.add(tblRDSalaryRecoveryList.getValueAt(i, 4));
                    list.add(tblRDSalaryRecoveryList.getValueAt(i, 6));
                    list.add(tblRDSalaryRecoveryList.getValueAt(i, 7));
                    list.add(tblRDSalaryRecoveryList.getValueAt(i, 8));
                    list.add(tblRDSalaryRecoveryList.getValueAt(i, 9));
                    list.add(tblRDSalaryRecoveryList.getValueAt(i, 10));
                    list.add(tblRDSalaryRecoveryList.getValueAt(i, 11));
                    list.add(tblRDSalaryRecoveryList.getValueAt(i, 12));
                    list.add(tblRDSalaryRecoveryList.getValueAt(i, 14));
                    list.add(tblRDSalaryRecoveryList.getValueAt(i, 15));
               
                    rdList.add(list);
                }
            }
            HashMap processMap = new HashMap();
            processMap.put("SALARYRECOVERY_RD_POST_LIST", rdList);
            observable.setFinalMap(processMap);
            observable.setRecoveryProdType("RD");
            HashMap authDataMap = new HashMap();
            String[] debitType = {"Transfer"};
            int option3 = 0;
            if (option3 == 0) {
                String transType = "";
                while (CommonUtil.convertObjToStr(transType).length() == 0) {
                    transType = (String) COptionPane.showInputDialog(null, "Select Transaction Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null, debitType, "");
                    if (CommonUtil.convertObjToStr(transType).length() > 0) {
                        authDataMap.put("TRANSACTION_PART", "TRANSACTION_PART");
                        authDataMap.put("TRANS_TYPE", transType.toUpperCase());
                        if (CommonUtil.convertObjToStr(transType.toUpperCase()).equals("TRANSFER")) {
                            boolean flag = true;
                            do {
                                String sbAcNo = null;
                                String prodType = null;
                                String productId = null;
                                HashMap acctDetailsMap = firstEnteredActNo();
                                if (acctDetailsMap != null && acctDetailsMap.size() > 0 && acctDetailsMap.containsKey("ACT_NUM") && acctDetailsMap.containsKey("PROD_TYPE")) {
                                    sbAcNo = CommonUtil.convertObjToStr(acctDetailsMap.get("ACT_NUM"));
                                    prodType = CommonUtil.convertObjToStr(acctDetailsMap.get("PROD_TYPE"));
                                    productId = CommonUtil.convertObjToStr(acctDetailsMap.get("PRODUCT_ID"));
                                }
                                if (sbAcNo != null && sbAcNo.length() > 0) {
                                    flag = checkingActNo(sbAcNo);
                                    flag = true;
                                    if (flag == false && finalChecking == false) {
                                        ClientUtil.showAlertWindow("Account No is invalid, Please enter correct no");
                                        return;
                                    } else {
                                        authDataMap.put("DR_ACT_NUM", sbAcNo);
                                        authDataMap.put("DR_PROD_TYPE", prodType);
                                        authDataMap.put("DR_PROD_ID", productId);
                                        authDataMap.put("USER_ID", ProxyParameters.USER_ID);
                                        chkok = false;
                                    }
                                    finalChecking = false;
                                } else {
                                    ClientUtil.showMessageWindow("Transaction Not Created");
                                    flag = true;
                                    authDataMap.remove("TRANSACTION_PART");
                                    observable.setNewTransactionMap(null);
                                    chktrans = false;
                                    return;

                                }
                            } while (!flag);
                        }
                        observable.setNewTransactionMap(authDataMap);
                    } else {
                        transType = "Cancel";
                        chktrans = true;
                    }
                }
            }
        }
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException /**
            * Execute some operation
            */
            {
                observable.doActionPerform();
                return null;
            }

            @Override
            protected void done() {
                loading.dispose();
            }
        };
        worker.execute();
        loading.show();
        try {
            worker.get();
        } catch (Exception e1) {
            e1.printStackTrace();
        } //Progress bar code ends here
        try {
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0 && observable.getProxyReturnMap().containsKey("STATUS")) {
                HashMap proxyResultMap = observable.getProxyReturnMap();
                if (CommonUtil.convertObjToStr(proxyResultMap.get("STATUS")).equals("COMPLETED")) {
                    ClientUtil.showMessageWindow("Transaction Completed!!!");
                    //observable.resetForm();
                    btnClearActionPerformed(null);
                } else {
                    ClientUtil.showMessageWindow("Transaction Failed!!!");
                    //observable.resetForm();
                    btnClearActionPerformed(null);
                }
                ClientUtil.clearAll(this);
                if (!btnProcessEdit.isEnabled()) {
                    btnProcessEdit.setEnabled(true);
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(LoanRecoveryUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRDGenerateNoticeActionPerformed

    private void btnRDCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRDCloseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRDCloseActionPerformed

    private void btnRDClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRDClearActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this);
        enableDisableComponants(false);
        setButtonEnableDisable();
        txtRDInstitutionName.setText("");
        tdtRDCalcIntUpto.setDateValue("");
        //       observable.resetForm();
        ClientUtil.enableDisable(panSalaryRecoveryList, false);
        ClientUtil.enableDisable(panRDRecovery, false);
        lblNoOfRDRecordsVal.setText("");
        lblRDTotalTransactionAmtVal.setText("");
        lblRDSelectedCountVal.setText("");
        resetCountValues();
    }//GEN-LAST:event_btnRDClearActionPerformed

    private void btnRDPrintNoticeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRDPrintNoticeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRDPrintNoticeActionPerformed

    private void btnRDRecalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRDRecalculateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRDRecalculateActionPerformed

    private void chkMDSSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMDSSelectAllActionPerformed
        // TODO add your handling code here:
        observable.setSelectAll(tblMDSSalaryRecoveryList, new Boolean(chkMDSSelectAll.isSelected()));
        setMDSSelectedRecord();
        calcMDSProcessTotal();
    }//GEN-LAST:event_chkMDSSelectAllActionPerformed

    private void tdtMDSCalcIntUptoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtMDSCalcIntUptoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtMDSCalcIntUptoFocusLost

    private void tblMDSSalaryRecoveryListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMDSSalaryRecoveryListMouseClicked
        // TODO add your handling code here:
        if(tblMDSSalaryRecoveryList.getRowCount() > 0){
            int row = tblMDSSalaryRecoveryList.getSelectedRow();
            int col = tblMDSSalaryRecoveryList.getSelectedColumn();
            String custId = CommonUtil.convertObjToStr(tblMDSSalaryRecoveryList.getValueAt(row, 18));
            String accNo = CommonUtil.convertObjToStr(tblMDSSalaryRecoveryList.getValueAt(row, 4));
            int instNo = CommonUtil.convertObjToInt(tblMDSSalaryRecoveryList.getValueAt(row, 7));
            String st = CommonUtil.convertObjToStr(tblMDSSalaryRecoveryList.getValueAt(tblMDSSalaryRecoveryList.getSelectedRow(), 0));
            if(st.equals("true")){
                boolean instExists = checkMinInstallmentsMDS(custId, instNo,accNo);
                if(instExists){
                    tblMDSSalaryRecoveryList.setValueAt(false, tblMDSSalaryRecoveryList.getSelectedRow(), 0);
                }
            }
        } 
        
         double totAmount = 0;
        int totalCount = 0;
        String st = "";
        for (int i = 0; i < tblMDSSalaryRecoveryList.getRowCount(); i++) {
            st = CommonUtil.convertObjToStr(tblMDSSalaryRecoveryList.getValueAt(i, 0));
            if (st.equals("true")) {
                totAmount = totAmount + CommonUtil.convertObjToDouble(tblMDSSalaryRecoveryList.getValueAt(i, 15)).doubleValue();
                totalCount++;
            }
        }
        lblMDSSelectedCountVal.setText(String.valueOf(totalCount));
        lblMDSTotalTransactionAmtVal.setText(String.valueOf(totAmount));
    }//GEN-LAST:event_tblMDSSalaryRecoveryListMouseClicked

    private void tblMDSSalaryRecoveryListPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblMDSSalaryRecoveryListPropertyChange
        // TODO add your handling code here:
        if (tblMDSSalaryRecoveryList.getSelectedRowCount() > 0 && tblMDSSalaryRecoveryList.getValueAt(tblMDSSalaryRecoveryList.getSelectedRow(), 0).equals(true)) {
            int row = tblMDSSalaryRecoveryList.getSelectedRow();
            int col = tblMDSSalaryRecoveryList.getSelectedColumn();
            if (col == 11) {
                System.out.println("Execute inside if");
                double recoveryBalance = CommonUtil.convertObjToDouble(tblMDSSalaryRecoveryList.getValueAt(row, 17));
                //String custId = CommonUtil.convertObjToStr(tblMDSSalaryRecoveryList.getValueAt(row, 18));
                double penalAmt = CommonUtil.convertObjToDouble(tblMDSSalaryRecoveryList.getValueAt(row, 11));
                double principal = CommonUtil.convertObjToDouble(tblMDSSalaryRecoveryList.getValueAt(row, 9));
                tblMDSSalaryRecoveryList.setValueAt(String.valueOf(principal+penalAmt), tblMDSSalaryRecoveryList.getSelectedRow(), 15);
                
            }
            if(col == 17){
                String custId = CommonUtil.convertObjToStr(tblMDSSalaryRecoveryList.getValueAt(row, 18));
                setRecoveryBalance(custId);
            }
        }
    }//GEN-LAST:event_tblMDSSalaryRecoveryListPropertyChange

    
    private void setRecoveryBalance(String custId){
        double totalRecoveryBal = 0.0;
        double totalMDSRecovery = 0.0;
        double finalRecoveryBal = 0.0;
//        HashMap recoveryMap = new HashMap();
//        recoveryMap.put("CUST_ID", custId);
//        recoveryMap.put("RECOVERY_ID", txtMDSRecoveryId.getText());
//        recoveryMap.put("INT_CALC_UPTO_DT", DateUtil.getDateMMDDYYYY(tdtMDSCalcIntUpto.getDateValue()));
//        List list = null;
//        list = ClientUtil.executeQuery("getCustomerRecoveryBalance", recoveryMap);
//        if (list != null && list.size() > 0) {
//            recoveryMap = (HashMap) list.get(0);
//            totalRecoveryBal = CommonUtil.convertObjToDouble(recoveryMap.get("TOTAL_RECOVERY_AMT"));
//        }

//        for (int i = 0; i < tblMDSSalaryRecoveryList.getRowCount(); i++) {
//            if (tblMDSSalaryRecoveryList.getValueAt(i, 0).equals(true) && CommonUtil.convertObjToStr(tblMDSSalaryRecoveryList.getValueAt(i, 18)).equals(custId)) {
//                totalMDSRecovery += CommonUtil.convertObjToDouble(tblMDSSalaryRecoveryList.getValueAt(i, 15));
//            }
//        }
//
//        System.out.println("recovery bal :: " + CommonUtil.convertObjToDouble(tblMDSSalaryRecoveryList.getValueAt(tblMDSSalaryRecoveryList.getSelectedRow(), 17)));
//        finalRecoveryBal = CommonUtil.convertObjToDouble(tblMDSSalaryRecoveryList.getValueAt(tblMDSSalaryRecoveryList.getSelectedRow(), 17)) - totalMDSRecovery;
        double recoveryBalance = CommonUtil.convertObjToDouble(tblMDSSalaryRecoveryList.getValueAt(tblMDSSalaryRecoveryList.getSelectedRow(), 17));
        for (int i = 0; i < tblMDSSalaryRecoveryList.getRowCount(); i++) {
            if (CommonUtil.convertObjToStr(tblMDSSalaryRecoveryList.getValueAt(i, 18)).equals(custId)) {                
                tblMDSSalaryRecoveryList.setValueAt(String.valueOf(recoveryBalance), i, 17);
            }
        }
    }
    
    
    private void btnMDSGenerateNoticeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMDSGenerateNoticeActionPerformed
        // Posting code changes for MDS
        
           if (!actionType.equals("EDIT")) {
            ArrayList mdsList = new ArrayList();
            for (int i = 0; i < tblMDSSalaryRecoveryList.getRowCount(); i++) {
                if (((Boolean) tblMDSSalaryRecoveryList.getValueAt(i, 0)).booleanValue()) {
                    ArrayList list = new ArrayList();
                    list.add(tblMDSSalaryRecoveryList.getValueAt(i, 1));
                    list.add(tblMDSSalaryRecoveryList.getValueAt(i, 2));
                    list.add(tblMDSSalaryRecoveryList.getValueAt(i, 3));
                    list.add(tblMDSSalaryRecoveryList.getValueAt(i, 4));
                    list.add(tblMDSSalaryRecoveryList.getValueAt(i, 7));
                    list.add(tblMDSSalaryRecoveryList.getValueAt(i, 8));
                    list.add(tblMDSSalaryRecoveryList.getValueAt(i, 9));
                    list.add(tblMDSSalaryRecoveryList.getValueAt(i, 10));
                    list.add(tblMDSSalaryRecoveryList.getValueAt(i, 11));
                    list.add(tblMDSSalaryRecoveryList.getValueAt(i, 12));
                    list.add(tblMDSSalaryRecoveryList.getValueAt(i, 13));
                    list.add(tblMDSSalaryRecoveryList.getValueAt(i, 14));
                    list.add(tblMDSSalaryRecoveryList.getValueAt(i, 15));
                    list.add(tblMDSSalaryRecoveryList.getValueAt(i, 17));
                    list.add(tblMDSSalaryRecoveryList.getValueAt(i, 18));
                    mdsList.add(list);
                }
            }
            HashMap processMap = new HashMap();
            processMap.put("SALARYRECOVERY_MDS_POST_LIST", mdsList);
            observable.setFinalMap(processMap);
            observable.setRecoveryProdType("MDS");
            HashMap authDataMap = new HashMap();
            String[] debitType = {"Transfer"};
            int option3 = 0;
            if (option3 == 0) {
                String transType = "";
                while (CommonUtil.convertObjToStr(transType).length() == 0) {
                    transType = (String) COptionPane.showInputDialog(null, "Select Transaction Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null, debitType, "");
                    if (CommonUtil.convertObjToStr(transType).length() > 0) {
                        authDataMap.put("TRANSACTION_PART", "TRANSACTION_PART");
                        authDataMap.put("TRANS_TYPE", transType.toUpperCase());
                        if (CommonUtil.convertObjToStr(transType.toUpperCase()).equals("TRANSFER")) {
                            boolean flag = true;
                            do {
                                String sbAcNo = null;
                                String prodType = null;
                                String productId = null;
                                HashMap acctDetailsMap = firstEnteredActNo();
                                if (acctDetailsMap != null && acctDetailsMap.size() > 0 && acctDetailsMap.containsKey("ACT_NUM") && acctDetailsMap.containsKey("PROD_TYPE")) {
                                    sbAcNo = CommonUtil.convertObjToStr(acctDetailsMap.get("ACT_NUM"));
                                    prodType = CommonUtil.convertObjToStr(acctDetailsMap.get("PROD_TYPE"));
                                    productId = CommonUtil.convertObjToStr(acctDetailsMap.get("PRODUCT_ID"));
                                }
                                if (sbAcNo != null && sbAcNo.length() > 0) {
                                    flag = checkingActNo(sbAcNo);
                                    flag = true;
                                    if (flag == false && finalChecking == false) {
                                        ClientUtil.showAlertWindow("Account No is invalid, Please enter correct no");
                                        return;
                                    } else {
                                        authDataMap.put("DR_ACT_NUM", sbAcNo);
                                        authDataMap.put("DR_PROD_TYPE", prodType);
                                        authDataMap.put("DR_PROD_ID", productId);
                                        authDataMap.put("USER_ID", ProxyParameters.USER_ID);
                                        chkok = false;
                                    }
                                    finalChecking = false;
                                } else {
                                    ClientUtil.showMessageWindow("Transaction Not Created");
                                    flag = true;
                                    authDataMap.remove("TRANSACTION_PART");
                                    observable.setNewTransactionMap(null);
                                    chktrans = false;
                                    return;

                                }
                            } while (!flag);
                        }
                        observable.setNewTransactionMap(authDataMap);
                    } else {
                        transType = "Cancel";
                        chktrans = true;
                    }
                }
            }
        }
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException /**
            * Execute some operation
            */
            {
                observable.doActionPerform();
                return null;
            }

            @Override
            protected void done() {
                loading.dispose();
            }
        };
        worker.execute();
        loading.show();
        try {
            worker.get();
        } catch (Exception e1) {
            e1.printStackTrace();
        } //Progress bar code ends here
        try {
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0 && observable.getProxyReturnMap().containsKey("STATUS")) {
                HashMap proxyResultMap = observable.getProxyReturnMap();
                if (CommonUtil.convertObjToStr(proxyResultMap.get("STATUS")).equals("COMPLETED")) {
                    ClientUtil.showMessageWindow("Transaction Completed!!!");
                    //observable.resetForm();
                    btnClearActionPerformed(null);
                } else {
                    ClientUtil.showMessageWindow("Transaction Failed!!!");
                    //observable.resetForm();
                    btnClearActionPerformed(null);
                }
                ClientUtil.clearAll(this);
            }
            if(!btnProcessEdit.isEnabled()){
                btnProcessEdit.setEnabled(true);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(LoanRecoveryUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_btnMDSGenerateNoticeActionPerformed

    private void btnMDSCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMDSCloseActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnMDSCloseActionPerformed

    private void btnMDSClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMDSClearActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this);
        enableDisableComponants(false);
        setButtonEnableDisable();
        txtInstitutionName.setText("");
        tdtCalcIntUpto.setDateValue("");
        //       observable.resetForm();
        ClientUtil.enableDisable(panSalaryRecoveryList, false);
        lblNoOfRecordsVal.setText("");
        lblTotalTransactionAmtVal.setText("");
        lblSelectedCountVal.setText("");
        resetCountValues();
        
    }//GEN-LAST:event_btnMDSClearActionPerformed

    private void resetCountValues(){
        lblMDSTotalTransactionAmtVal.setText("");
        lblNoOfMDSRecordsVal.setText("");
        lblMDSSelectedCountVal.setText("");
        lblNoOfRecordsVal.setText("");
        lblTotalTransactionAmtVal.setText("");
        lblSelectedCountVal.setText("");
        lblNoOfLoanRecordsVal.setText("");
        lblLoanTotalTransactionAmtVal.setText("");
        lblLoanSelectedCountVal.setText("");
        lblProcessListSelectedCountVal.setText("");
        lblProcessListTotalTransactionAmtVal.setText("");  
        lblRDInstRecAmtVal.setText("");
        lblRDPostedAmtVal.setText("");
        lblTLInstRecAmtVal.setText("");
        lblTLPostedAmtVal.setText("");
        lblMDSInstRecAmtVal.setText("");
        lblMDSPostAmtVal.setText("");
    }
    
    
    private void btnMDSPrintNoticeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMDSPrintNoticeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMDSPrintNoticeActionPerformed

    private void btnMDSRecalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMDSRecalculateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMDSRecalculateActionPerformed

    private void chkLoanSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLoanSelectAllActionPerformed
        // TODO add your handling code here:
        LinkedHashMap balMap = new LinkedHashMap();
        observable.setSelectAll(tblLoanSalaryRecoveryList, new Boolean(chkLoanSelectAll.isSelected()));
        setSelectedRecord();
        calcProcessTotal();
        for(int i=0; i<tblLoanSalaryRecoveryList.getRowCount(); i++){
            //System.out.println("recovery amount :: " + tblSalaryRecoveryList.getValueAt(i, 14));
            if(tblLoanSalaryRecoveryList.getValueAt(i, 14) != null && CommonUtil.convertObjToStr(tblLoanSalaryRecoveryList.getValueAt(i, 14)).length() > 0){
                balMap.put(tblLoanSalaryRecoveryList.getValueAt(i, 15),tblLoanSalaryRecoveryList.getValueAt(i, 14));
            }
        }
        
        
        System.out.println("balMap :: " + balMap);
        LinkedHashMap custDataMap = checkbalAmountforCustomer(balMap);
        blockCustIDRow(custDataMap,balMap);
    }//GEN-LAST:event_chkLoanSelectAllActionPerformed

    private void tdtLoanCalcIntUptoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtLoanCalcIntUptoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtLoanCalcIntUptoFocusLost

    private void tblLoanSalaryRecoveryListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLoanSalaryRecoveryListMouseClicked
        double totAmount = 0;
        int totalCount = 0;
        String st = "";
        double totalRecoAmt = 0.0;
        LinkedHashMap balMap = new LinkedHashMap();
        if (tblLoanSalaryRecoveryList.getSelectedRowCount() > 0 && tblLoanSalaryRecoveryList.getValueAt(tblLoanSalaryRecoveryList.getSelectedRow(), 0).equals(true)) {
            int row = tblLoanSalaryRecoveryList.getSelectedRow();
            int col = tblLoanSalaryRecoveryList.getSelectedColumn();
            String custId = CommonUtil.convertObjToStr(tblLoanSalaryRecoveryList.getValueAt(row, 15));
            double totalDemand = CommonUtil.convertObjToDouble(tblLoanSalaryRecoveryList.getValueAt(row, 7));

            for (int i = 0; i < tblLoanSalaryRecoveryList.getRowCount(); i++) {
                if (custId.equals(CommonUtil.convertObjToStr(tblLoanSalaryRecoveryList.getValueAt(i, 15))) && CommonUtil.convertObjToStr(tblLoanSalaryRecoveryList.getValueAt(i, 0)).equals("true")) {
                    totalRecoAmt += CommonUtil.convertObjToDouble(tblLoanSalaryRecoveryList.getValueAt(i, 12));
                }
            }

            for (int i = 0; i < tblLoanSalaryRecoveryList.getRowCount(); i++) {
                //System.out.println("recovery amount :: " + tblSalaryRecoveryList.getValueAt(i, 14));
                if (tblLoanSalaryRecoveryList.getValueAt(i, 14) != null && CommonUtil.convertObjToStr(tblLoanSalaryRecoveryList.getValueAt(i, 14)).length() > 0) {
                    balMap.put(tblLoanSalaryRecoveryList.getValueAt(i, 15), tblLoanSalaryRecoveryList.getValueAt(i, 14));
                }
            }

            if (balMap.containsKey(custId) && balMap.get(custId) != null) {
                if (CommonUtil.convertObjToDouble(balMap.get(custId)) < totalRecoAmt) {
                    ClientUtil.showMessageWindow("Total Recover Amount - " + totalRecoAmt + "\n" + "Balance - " + CommonUtil.convertObjToDouble(balMap.get(custId)));
                    tblLoanSalaryRecoveryList.setValueAt(false, row, 0);
                }
            }

        }

        for (int i = 0; i < tblLoanSalaryRecoveryList.getRowCount(); i++) {
            st = CommonUtil.convertObjToStr(tblLoanSalaryRecoveryList.getValueAt(i, 0));
            if (st.equals("true")) {
                totAmount = totAmount + CommonUtil.convertObjToDouble(tblLoanSalaryRecoveryList.getValueAt(i, 12)).doubleValue();
                totalCount++;
            }
        }
        lblLoanSelectedCountVal.setText(String.valueOf(totalCount));
        lblLoanTotalTransactionAmtVal.setText(String.valueOf(totAmount));
    }//GEN-LAST:event_tblLoanSalaryRecoveryListMouseClicked

    private void tblLoanSalaryRecoveryListPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblLoanSalaryRecoveryListPropertyChange
        double totalRecoAmt = 0.0;
        LinkedHashMap balMap = new LinkedHashMap();
        if (tblLoanSalaryRecoveryList.getSelectedRowCount() > 0 && tblLoanSalaryRecoveryList.getValueAt(tblLoanSalaryRecoveryList.getSelectedRow(), 0).equals(true)) {
            int row = tblLoanSalaryRecoveryList.getSelectedRow();
            int col = tblLoanSalaryRecoveryList.getSelectedColumn();
            
            String custId = CommonUtil.convertObjToStr(tblLoanSalaryRecoveryList.getValueAt(row, 15));
            double totalDemand = CommonUtil.convertObjToDouble(tblLoanSalaryRecoveryList.getValueAt(row, 7));

            if (col == 12) {
               
                for (int i = 0; i < tblLoanSalaryRecoveryList.getRowCount(); i++) {
                    if (custId.equals(CommonUtil.convertObjToStr(tblLoanSalaryRecoveryList.getValueAt(i, 15))) && CommonUtil.convertObjToStr(tblLoanSalaryRecoveryList.getValueAt(i, 0)).equals("true")) {
                        totalRecoAmt += CommonUtil.convertObjToDouble(tblLoanSalaryRecoveryList.getValueAt(i, 12));
                    }
                }

                for (int i = 0; i < tblLoanSalaryRecoveryList.getRowCount(); i++) {
                    //System.out.println("recovery amount :: " + tblSalaryRecoveryList.getValueAt(i, 14));
                    if (tblLoanSalaryRecoveryList.getValueAt(i, 14) != null && CommonUtil.convertObjToStr(tblLoanSalaryRecoveryList.getValueAt(i, 14)).length() > 0) {
                        balMap.put(tblLoanSalaryRecoveryList.getValueAt(i, 15), tblLoanSalaryRecoveryList.getValueAt(i, 14));
                    }
                }

                if(balMap.containsKey(custId) && balMap.get(custId) != null){
                    if(CommonUtil.convertObjToDouble(balMap.get(custId)) < totalRecoAmt){
                        ClientUtil.showMessageWindow("Total Recover Amount - " + totalRecoAmt +"\n" + "Balance - "+ CommonUtil.convertObjToDouble(balMap.get(custId)));
                        tblLoanSalaryRecoveryList.setValueAt(String.valueOf(totalDemand),row, 12);
                    }
                }
            }
        }
    }//GEN-LAST:event_tblLoanSalaryRecoveryListPropertyChange

    private void btnLoanGenerateNoticeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoanGenerateNoticeActionPerformed
        // TODO add your handling code here:
          if (!actionType.equals("EDIT")) {
            ArrayList arbList = new ArrayList();
            for (int i = 0; i < tblLoanSalaryRecoveryList.getRowCount(); i++) {
                if (((Boolean) tblLoanSalaryRecoveryList.getValueAt(i, 0)).booleanValue()) {
                    ArrayList list = new ArrayList();
                    list.add(tblLoanSalaryRecoveryList.getValueAt(i, 1));
                    list.add(tblLoanSalaryRecoveryList.getValueAt(i, 2));
                    list.add(tblLoanSalaryRecoveryList.getValueAt(i, 3));
                    list.add(tblLoanSalaryRecoveryList.getValueAt(i, 4));
                    list.add(tblLoanSalaryRecoveryList.getValueAt(i, 8));
                    list.add(tblLoanSalaryRecoveryList.getValueAt(i, 9));
                    list.add(tblLoanSalaryRecoveryList.getValueAt(i, 10));
                    list.add(tblLoanSalaryRecoveryList.getValueAt(i, 11));
                    list.add(tblLoanSalaryRecoveryList.getValueAt(i, 12));
                    list.add(tblLoanSalaryRecoveryList.getValueAt(i, 14));
                    list.add(tblLoanSalaryRecoveryList.getValueAt(i, 15));
                    arbList.add(list);
                }
            }
            HashMap processMap = new HashMap();
            processMap.put("SALARYRECOVERY_POST_LIST", arbList);
            observable.setFinalMap(processMap);
            observable.setRecoveryProdType("TL");
            HashMap authDataMap = new HashMap();
            String[] debitType = {"Transfer"};
            int option3 = 0;
            if (option3 == 0) {
                String transType = "";
                while (CommonUtil.convertObjToStr(transType).length() == 0) {
                    transType = (String) COptionPane.showInputDialog(null, "Select Transaction Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null, debitType, "");
                    if (CommonUtil.convertObjToStr(transType).length() > 0) {
                        authDataMap.put("TRANSACTION_PART", "TRANSACTION_PART");
                        authDataMap.put("TRANS_TYPE", transType.toUpperCase());
                        if (CommonUtil.convertObjToStr(transType.toUpperCase()).equals("TRANSFER")) {
                            boolean flag = true;
                            do {
                                String sbAcNo = null;
                                String prodType = null;
                                HashMap acctDetailsMap = firstEnteredActNo();
                                if (acctDetailsMap != null && acctDetailsMap.size() > 0 && acctDetailsMap.containsKey("ACT_NUM") && acctDetailsMap.containsKey("PROD_TYPE")) {
                                    sbAcNo = CommonUtil.convertObjToStr(acctDetailsMap.get("ACT_NUM"));
                                    prodType = CommonUtil.convertObjToStr(acctDetailsMap.get("PROD_TYPE"));
                                }
                                if (sbAcNo != null && sbAcNo.length() > 0) {
                                    flag = checkingActNo(sbAcNo);
                                    flag = true;
                                    if (flag == false && finalChecking == false) {
                                        ClientUtil.showAlertWindow("Account No is invalid, Please enter correct no");
                                        return;
                                    } else {
                                        authDataMap.put("DR_ACT_NUM", sbAcNo);
                                        authDataMap.put("DR_PROD_TYPE", prodType);
                                        authDataMap.put("USER_ID", ProxyParameters.USER_ID);
                                        chkok = false;
                                    }
                                    finalChecking = false;
                                } else {
                                    ClientUtil.showMessageWindow("Transaction Not Created");
                                    flag = true;
                                    authDataMap.remove("TRANSACTION_PART");
                                    observable.setNewTransactionMap(null);
                                    chktrans = false;
                                    return;

                                }
                            } while (!flag);
                        }
                        observable.setNewTransactionMap(authDataMap);
                    } else {
                        transType = "Cancel";
                        chktrans = true;
                    }
                }
            }
        }
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException /**
            * Execute some operation
            */
            {
                observable.doActionPerform();
                return null;
            }

            @Override
            protected void done() {
                loading.dispose();
            }
        };
        worker.execute();
        loading.show();
        try {
            worker.get();
        } catch (Exception e1) {
            e1.printStackTrace();
        } //Progress bar code ends here
        try {
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0 && observable.getProxyReturnMap().containsKey("STATUS")) {
                HashMap proxyResultMap = observable.getProxyReturnMap();
                if (CommonUtil.convertObjToStr(proxyResultMap.get("STATUS")).equals("COMPLETED")) {
                    ClientUtil.showMessageWindow("Transaction Completed!!!");
                    //observable.resetForm();
                    btnClearActionPerformed(null);
                } else {
                    ClientUtil.showMessageWindow("Transaction Failed!!!");
                    //observable.resetForm();
                    btnClearActionPerformed(null);
                }
                ClientUtil.clearAll(this);
                if (!btnProcessEdit.isEnabled()) {
                    btnProcessEdit.setEnabled(true);
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(LoanRecoveryUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnLoanGenerateNoticeActionPerformed

    private void btnLoanCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoanCloseActionPerformed
        // TODO add your handling code here:
         dispose();
    }//GEN-LAST:event_btnLoanCloseActionPerformed

    private void btnLoanClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoanClearActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this);
        enableDisableComponants(false);
        setButtonEnableDisable();
        txtLoanInstitutionName.setText("");
        tdtLoanCalcIntUpto.setDateValue("");
        //       observable.resetForm();
        ClientUtil.enableDisable(panLoanRecovery, false);
        lblNoOfLoanRecordsVal.setText("");
        lblLoanTotalTransactionAmtVal.setText("");
        lblLoanSelectedCountVal.setText("");
        ((DefaultTableModel) tblLoanSalaryRecoveryList.getModel()).setRowCount(0);
    }//GEN-LAST:event_btnLoanClearActionPerformed

    private void btnLoanPrintNoticeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoanPrintNoticeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLoanPrintNoticeActionPerformed

    private void btnLoanRecalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoanRecalculateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLoanRecalculateActionPerformed

    private void srpSalaryRecoveryListPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_srpSalaryRecoveryListPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_srpSalaryRecoveryListPropertyChange

    private void panLoanRecoveryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panLoanRecoveryMouseClicked
        // TODO add your handling code here:
        btnProcessEdit.setEnabled(true);
    }//GEN-LAST:event_panLoanRecoveryMouseClicked

    private boolean allCustomersUpdated(){
        boolean update = true;
        if(tblProcessListSalaryRecoveryList.getRowCount() > 0){
            for(int i=0;i<tblProcessListSalaryRecoveryList.getRowCount(); i++){
                if(CommonUtil.convertObjToStr(tblProcessListSalaryRecoveryList.getValueAt(i, 4)).length() <= 0){
                    update = false;
                    break;
                }
            }
        }
        return update;
    }
    
    
    private void btnProcessListSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessListSaveActionPerformed
          if (!actionType.equals("EDIT")) {
           if(!allCustomersUpdated()){
               ClientUtil.displayAlert("All Rows are not updated . Please check !!!");
               return;
           }
            ArrayList arbList = new ArrayList();
            for (int i = 0; i < tblProcessListSalaryRecoveryList.getRowCount(); i++) {
                if (((Boolean) tblProcessListSalaryRecoveryList.getValueAt(i, 0)).booleanValue()) {
                    ArrayList list = new ArrayList();
                    list.add(txtProcessListRecoveryId.getText());
                    list.add(tblProcessListSalaryRecoveryList.getValueAt(i, 1));
                    list.add(tblProcessListSalaryRecoveryList.getValueAt(i, 2));
                    list.add(tblProcessListSalaryRecoveryList.getValueAt(i, 3));
                    list.add(tblProcessListSalaryRecoveryList.getValueAt(i, 4));
                    arbList.add(list);
                }
            }
            HashMap processMap = new HashMap();
            processMap.put("SALARYRECOVERY_AMT_UPDATE_LIST", arbList);
            observable.setFinalMap(processMap);
            observable.setRecoveryProdType("REC_AMT");  
        }
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException /**
            * Execute some operation
            */
            {
                observable.doActionPerform();
                return null;
            }

            @Override
            protected void done() {
                loading.dispose();
            }
        };
        worker.execute();
        loading.show();
        try {
            worker.get();
        } catch (Exception e1) {
            e1.printStackTrace();
        } //Progress bar code ends here
        try {
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0 && observable.getProxyReturnMap().containsKey("STATUS")) {
                HashMap proxyResultMap = observable.getProxyReturnMap();
                if (CommonUtil.convertObjToStr(proxyResultMap.get("STATUS")).equals("COMPLETED")) {
                    ClientUtil.showMessageWindow("Transaction Completed!!!");
                    //observable.resetForm();
                    btnClearActionPerformed(null);
                } else {
                    ClientUtil.showMessageWindow("Transaction Failed!!!");
                    //observable.resetForm();
                    btnClearActionPerformed(null);
                }
                ClientUtil.clearAll(this);
                if (!btnProcessEdit.isEnabled()) {
                    btnProcessEdit.setEnabled(true);
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(LoanRecoveryUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnProcessListSaveActionPerformed

    private void btnProcessListCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessListCloseActionPerformed
        // TODO add your handling code here:
         dispose();
    }//GEN-LAST:event_btnProcessListCloseActionPerformed

    private void btnProcessListClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessListClearActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this);
        enableDisableComponants(false);
        setButtonEnableDisable();
        txtProcessListLoanInstitutionName.setText("");
        tdProcessListtLoanCalcIntUpto.setDateValue("");
        //       observable.resetForm();
        ClientUtil.enableDisable(panLoanRecovery, false);
        ClientUtil.enableDisable(panRDRecovery, false);
        ClientUtil.enableDisable(panMDSRecovery, false);
        ClientUtil.enableDisable(panProcessListUpdate, false);
        lblNoOfProcessListRecordsVal.setText("");
        lblProcessListTotalTransactionAmtVal.setText("");
        lblProcessListSelectedCountVal.setText("");
    }//GEN-LAST:event_btnProcessListClearActionPerformed

    private void tblProcessListSalaryRecoveryListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProcessListSalaryRecoveryListMouseClicked
        // TODO add your handling code here:
         if (tblProcessListSalaryRecoveryList.getSelectedRowCount() > 0 && tblProcessListSalaryRecoveryList.getValueAt(tblProcessListSalaryRecoveryList.getSelectedRow(), 0).equals(false)) {
           tblProcessListSalaryRecoveryList.setValueAt(true, tblProcessListSalaryRecoveryList.getSelectedRow(), 0);
         }
    }//GEN-LAST:event_tblProcessListSalaryRecoveryListMouseClicked

    private void tblProcessListSalaryRecoveryListPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblProcessListSalaryRecoveryListPropertyChange
        // TODO add your handling code here:
        if (tblProcessListSalaryRecoveryList.getSelectedRowCount() > 0 && tblProcessListSalaryRecoveryList.getValueAt(tblProcessListSalaryRecoveryList.getSelectedRow(), 0).equals(true)) {
            int row = tblProcessListSalaryRecoveryList.getSelectedRow();
            int col = tblProcessListSalaryRecoveryList.getSelectedColumn();
            if (col == 4) {
              calculateTotalRecoveryAmountForEachCustomer();
            }
        }
    }//GEN-LAST:event_tblProcessListSalaryRecoveryListPropertyChange

    private void tdProcessListtLoanCalcIntUptoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdProcessListtLoanCalcIntUptoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdProcessListtLoanCalcIntUptoFocusLost
    private void calcTotal() {
        double totAmt = 0;
        if (tblSalaryRecoveryList.getRowCount() > 0) {
            for (int i = 0; i < tblSalaryRecoveryList.getRowCount(); i++) {
                totAmt = totAmt + CommonUtil.convertObjToDouble(tblSalaryRecoveryList.getValueAt(i, 6).toString()).doubleValue();
            }
        }

        lblTotalTransactionAmtVal.setText(String.valueOf(totAmt));
        lblNoOfRecordsVal.setText(String.valueOf(tblSalaryRecoveryList.getRowCount()));
        lblSelectedCountVal.setText(String.valueOf(tblSalaryRecoveryList.getRowCount()));
    }

    private void calcProcessTotal() {
        double totAmt = 0;
        if (tblLoanSalaryRecoveryList.getRowCount() > 0) {
            for (int i = 0; i < tblLoanSalaryRecoveryList.getRowCount(); i++) {
                if(((Boolean) tblLoanSalaryRecoveryList.getValueAt(i, 0)).booleanValue())
                totAmt = totAmt + CommonUtil.convertObjToDouble(tblLoanSalaryRecoveryList.getValueAt(i, 12).toString()).doubleValue();
            }
        }

        lblLoanTotalTransactionAmtVal.setText(String.valueOf(totAmt));
        lblNoOfLoanRecordsVal.setText(String.valueOf(tblLoanSalaryRecoveryList.getRowCount()));
         //lblLoanSelectedCountVal.setText(String.valueOf(tblLoanSalaryRecoveryList.getRowCount()));
    }
    
      private void calcMDSProcessTotal() {
        double totAmt = 0;
        if (tblMDSSalaryRecoveryList.getRowCount() > 0) {
            for (int i = 0; i < tblMDSSalaryRecoveryList.getRowCount(); i++) {
                if(((Boolean) tblMDSSalaryRecoveryList.getValueAt(i, 0)).booleanValue())
                  totAmt = totAmt + CommonUtil.convertObjToDouble(tblMDSSalaryRecoveryList.getValueAt(i, 15).toString()).doubleValue();
            }
        }

        lblMDSTotalTransactionAmtVal.setText(String.valueOf(totAmt));
        lblNoOfMDSRecordsVal.setText(String.valueOf(tblMDSSalaryRecoveryList.getRowCount()));
        lblMDSSelectedCountVal.setText(String.valueOf(tblMDSSalaryRecoveryList.getRowCount()));
    }
      
     private void calcRDProcessTotal() {
        double totAmt = 0;
        if (tblRDSalaryRecoveryList.getRowCount() > 0) {
            for (int i = 0; i < tblRDSalaryRecoveryList.getRowCount(); i++) {
                if(((Boolean) tblRDSalaryRecoveryList.getValueAt(i, 0)).booleanValue())
                  totAmt = totAmt + CommonUtil.convertObjToDouble(tblRDSalaryRecoveryList.getValueAt(i, 12).toString()).doubleValue();
            }
        }

        lblRDTotalTransactionAmtVal.setText(String.valueOf(totAmt));
        lblNoOfRDRecordsVal.setText(String.valueOf(tblRDSalaryRecoveryList.getRowCount()));
        lblRDSelectedCountVal.setText(String.valueOf(tblRDSalaryRecoveryList.getRowCount()));
    }  
    
    
    private void createExcelFile() throws IOException {

        Date intDt = DateUtil.getDateMMDDYYYY(tdtCalcIntUpto.getDateValue());
        String path = System.getProperty("user.home") + "/Desktop/" + intDt.getDate() + "-" + (intDt.getMonth() + 1) + "-" + (intDt.getYear() + 1900) + "-" + "RecoveryList.xls";
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Recovery List"); //WorkSheet
        HSSFRow row = sheet.createRow(2); //Row created at line 3
        HSSFCellStyle cellStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.BLACK.index);
        cellStyle.setFont(font);
        cellStyle.setBorderBottom((short) 1); // single line border
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        EnhancedTableModel model = (EnhancedTableModel) tblSalaryRecoveryList.getModel(); //Table model
        HSSFRow headerRow = sheet.createRow(0); //Create row at line 0

        for (int headings = 0; headings < model.getColumnCount(); headings++) { //For each column
            HSSFCell cell = headerRow.createCell((short) headings);
            cell.setCellValue(model.getColumnName(headings));//Write column name
            cell.setCellStyle(cellStyle);
        }
        headerRow.setHeightInPoints(25);
        for (int rows = 0; rows < model.getRowCount(); rows++) { //For each table row
            for (int cols = 0; cols < tblSalaryRecoveryList.getColumnCount(); cols++) { //For each table column
                HSSFCell cell = row.createCell((short) cols);
                cell.setCellValue(model.getValueAt(rows, cols).toString()); //Write value
                cell.setCellStyle(cellStyle);
            }
            row.setHeightInPoints(25);
            //Set the row to the next one in the sequence 
            row = sheet.createRow((rows + 3));
        }
        wb.write(new FileOutputStream(path.toString()));//Save the file     
    }

    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        btnProcessEdit.setEnabled(!btnView.isEnabled()); // Added by nithya
    }

    private void internationalize() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        HashMap mapParam = new HashMap();
    }

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

    public void update(Observable o, Object arg) {
//         tblSalaryRecoveryList.setModel(observable.getTblSalaryRecoveryList());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnClose1;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnExportRecoveryList;
    private com.see.truetransact.uicomponent.CButton btnGenerate;
    private com.see.truetransact.uicomponent.CButton btnGenerateNotice;
    private com.see.truetransact.uicomponent.CButton btnInstitution;
    private com.see.truetransact.uicomponent.CButton btnLoanClear;
    private com.see.truetransact.uicomponent.CButton btnLoanClose;
    private com.see.truetransact.uicomponent.CButton btnLoanGenerateNotice;
    private com.see.truetransact.uicomponent.CButton btnLoanPrintNotice;
    private com.see.truetransact.uicomponent.CButton btnLoanRecalculate;
    private com.see.truetransact.uicomponent.CButton btnMDSClear;
    private com.see.truetransact.uicomponent.CButton btnMDSClose;
    private com.see.truetransact.uicomponent.CButton btnMDSGenerateNotice;
    private com.see.truetransact.uicomponent.CButton btnMDSPrintNotice;
    private com.see.truetransact.uicomponent.CButton btnMDSRecalculate;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnPrintNotice;
    private com.see.truetransact.uicomponent.CButton btnPrintRecoveryList;
    private com.see.truetransact.uicomponent.CButton btnProcessEdit;
    private com.see.truetransact.uicomponent.CButton btnProcessListClear;
    private com.see.truetransact.uicomponent.CButton btnProcessListClose;
    private com.see.truetransact.uicomponent.CButton btnProcessListSave;
    private com.see.truetransact.uicomponent.CButton btnRDClear;
    private com.see.truetransact.uicomponent.CButton btnRDClose;
    private com.see.truetransact.uicomponent.CButton btnRDGenerateNotice;
    private com.see.truetransact.uicomponent.CButton btnRDPrintNotice;
    private com.see.truetransact.uicomponent.CButton btnRDRecalculate;
    private com.see.truetransact.uicomponent.CButton btnRecalculate;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CLabel cLabel4;
    private com.see.truetransact.uicomponent.CLabel cLabel5;
    private com.see.truetransact.uicomponent.CLabel cLabel6;
    private com.see.truetransact.uicomponent.CLabel cLabel7;
    private com.see.truetransact.uicomponent.CCheckBox chkLoanSelectAll;
    private com.see.truetransact.uicomponent.CCheckBox chkMDSSelectAll;
    private com.see.truetransact.uicomponent.CCheckBox chkRDSelectAll;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblActHoldersList;
    private com.see.truetransact.uicomponent.CLabel lblActHoldersList1;
    private com.see.truetransact.uicomponent.CLabel lblActHoldersList2;
    private com.see.truetransact.uicomponent.CLabel lblActHoldersList3;
    private com.see.truetransact.uicomponent.CLabel lblActHoldersList4;
    private com.see.truetransact.uicomponent.CLabel lblCalcIntUpto;
    private com.see.truetransact.uicomponent.CLabel lblCalcIntUpto1;
    private com.see.truetransact.uicomponent.CLabel lblInstitutionName;
    private com.see.truetransact.uicomponent.CLabel lblInstitutionName1;
    private com.see.truetransact.uicomponent.CLabel lblInstitutionName2;
    private com.see.truetransact.uicomponent.CLabel lblLoanCalcIntUpto;
    private com.see.truetransact.uicomponent.CLabel lblLoanInstitutionName;
    private com.see.truetransact.uicomponent.CLabel lblLoanNoOfRecords;
    private com.see.truetransact.uicomponent.CLabel lblLoanNoOfRecords1;
    private com.see.truetransact.uicomponent.CLabel lblLoanRecoveryId;
    private com.see.truetransact.uicomponent.CLabel lblLoanRecoveryId1;
    private com.see.truetransact.uicomponent.CLabel lblLoanSelectedCount;
    private com.see.truetransact.uicomponent.CLabel lblLoanSelectedCount1;
    private com.see.truetransact.uicomponent.CLabel lblLoanSelectedCountVal;
    private com.see.truetransact.uicomponent.CLabel lblLoanTotal;
    private com.see.truetransact.uicomponent.CLabel lblLoanTotal1;
    private com.see.truetransact.uicomponent.CLabel lblLoanTotalTransactionAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblMDSCalcIntUpto;
    private com.see.truetransact.uicomponent.CLabel lblMDSInstRecAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblMDSNoOfRecords;
    private com.see.truetransact.uicomponent.CLabel lblMDSPostAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblMDSRecoveryId;
    private com.see.truetransact.uicomponent.CLabel lblMDSSelectedCount;
    private com.see.truetransact.uicomponent.CLabel lblMDSSelectedCountVal;
    private com.see.truetransact.uicomponent.CLabel lblMDSTotal;
    private com.see.truetransact.uicomponent.CLabel lblMDSTotalTransactionAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblNoOfLoanRecordsVal;
    private com.see.truetransact.uicomponent.CLabel lblNoOfMDSRecordsVal;
    private com.see.truetransact.uicomponent.CLabel lblNoOfProcessListRecordsVal;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRDRecordsVal;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecords;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecordsVal;
    private com.see.truetransact.uicomponent.CLabel lblProcessListInstitutionName;
    private com.see.truetransact.uicomponent.CLabel lblProcessListLoanCalcIntUpto;
    private com.see.truetransact.uicomponent.CLabel lblProcessListSelectedCountVal;
    private com.see.truetransact.uicomponent.CLabel lblProcessListTotalTransactionAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblRDInstRecAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblRDNoOfRecords;
    private com.see.truetransact.uicomponent.CLabel lblRDPostedAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblRDSelectedCount;
    private com.see.truetransact.uicomponent.CLabel lblRDSelectedCountVal;
    private com.see.truetransact.uicomponent.CLabel lblRDTotal;
    private com.see.truetransact.uicomponent.CLabel lblRDTotalTransactionAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblSelectedCount;
    private com.see.truetransact.uicomponent.CLabel lblSelectedCountVal;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblSpace76;
    private com.see.truetransact.uicomponent.CLabel lblSpace77;
    private com.see.truetransact.uicomponent.CLabel lblSpace78;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStatusVal;
    private com.see.truetransact.uicomponent.CLabel lblTLInstRecAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblTLPostedAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblTotal;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmtVal;
    private com.see.truetransact.uicomponent.CPanel panLoanRecovery;
    private com.see.truetransact.uicomponent.CPanel panLoanSearch;
    private com.see.truetransact.uicomponent.CPanel panMDSRecovery;
    private com.see.truetransact.uicomponent.CPanel panMDSSearch;
    private com.see.truetransact.uicomponent.CPanel panProcessListSearch;
    private com.see.truetransact.uicomponent.CPanel panProcessListUpdate;
    private com.see.truetransact.uicomponent.CPanel panRDRecovery;
    private com.see.truetransact.uicomponent.CPanel panRDSearch;
    private com.see.truetransact.uicomponent.CPanel panSalaryRecoveryList;
    private com.see.truetransact.uicomponent.CPanel panSalaryRecoveryOptions;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdbArbit;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CSeparator sptLine1;
    private com.see.truetransact.uicomponent.CSeparator sptLine2;
    private com.see.truetransact.uicomponent.CSeparator sptLine3;
    private com.see.truetransact.uicomponent.CSeparator sptLine4;
    private com.see.truetransact.uicomponent.CScrollPane srpLoanSalaryRecoveryList;
    private com.see.truetransact.uicomponent.CScrollPane srpMDSSalaryRecoveryList;
    private com.see.truetransact.uicomponent.CScrollPane srpProcessListSalaryRecoveryList;
    private com.see.truetransact.uicomponent.CScrollPane srpRDSalaryRecoveryList;
    private com.see.truetransact.uicomponent.CScrollPane srpSalaryRecoveryList;
    private com.see.truetransact.uicomponent.CTable tblLoanSalaryRecoveryList;
    private com.see.truetransact.uicomponent.CTable tblMDSSalaryRecoveryList;
    private com.see.truetransact.uicomponent.CTable tblProcessListSalaryRecoveryList;
    private com.see.truetransact.uicomponent.CTable tblRDSalaryRecoveryList;
    private com.see.truetransact.uicomponent.CTable tblSalaryRecoveryList;
    private com.see.truetransact.uicomponent.CTabbedPane tbpRecovery;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig;
    private com.see.truetransact.uicomponent.CDateField tdProcessListtLoanCalcIntUpto;
    private com.see.truetransact.uicomponent.CDateField tdtCalcIntUpto;
    private com.see.truetransact.uicomponent.CDateField tdtLoanCalcIntUpto;
    private com.see.truetransact.uicomponent.CDateField tdtMDSCalcIntUpto;
    private com.see.truetransact.uicomponent.CDateField tdtRDCalcIntUpto;
    private com.see.truetransact.uicomponent.CTextField txtInstitutionName;
    private com.see.truetransact.uicomponent.CTextField txtLoanInstitutionName;
    private com.see.truetransact.uicomponent.CTextField txtLoanRecoveryId;
    private com.see.truetransact.uicomponent.CTextField txtMDSInstitutionName;
    private com.see.truetransact.uicomponent.CTextField txtMDSRecoveryId;
    private com.see.truetransact.uicomponent.CTextField txtProcessListLoanInstitutionName;
    private com.see.truetransact.uicomponent.CTextField txtProcessListRecoveryId;
    private com.see.truetransact.uicomponent.CTextField txtRDInstitutionName;
    private com.see.truetransact.uicomponent.CTextField txtRDRecoveryId;
    // End of variables declaration//GEN-END:variables
}
