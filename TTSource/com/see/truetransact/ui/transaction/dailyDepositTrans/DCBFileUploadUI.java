/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DCBFileUploadUI.java
 *
 * Created on Aug 10th, 2017, 11:23 AM
 */
package com.see.truetransact.ui.transaction.dailyDepositTrans;

/**
 *
 * @author Suresh R
 */
import java.util.*;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.TextUI;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.termloan.loanrebate.LoanRebateUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.serverside.fileupload.FileHeader;
import com.see.truetransact.serverside.fileupload.FileRead;
import com.see.truetransact.serverside.fileupload.MappingTO;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.uimandatory.UIMandatoryField;

import javax.swing.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import javax.swing.table.*;
import java.text.MessageFormat;
import java.awt.print.PrinterJob;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

public class DCBFileUploadUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    /**
     * Vairable Declarations
     */
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    DCBFileUploadOB observable = null;
    private boolean selectMode = false;
    private Date currDate = null;
    private HashMap returnMap = null;
    ArrayList colorList = new ArrayList();
    private List pendingTransLst = null;
    private boolean isProcessed = false;
    private HashMap mandatoryMap;
    final String AUTHORIZE = "Authorize";
    boolean isFilled = false;
    java.io.File selectedFile = null;
    private List finalFileNameLst = null;
    private double headerTotalAmount = 0.0;
    private double headerRecordCount = 0.0;
    private String headerDate = "";
    private String fileNameDate = "";
    List totCustomerList = new ArrayList();
    List totrtgsCustomerList = new ArrayList();
    String fileName = "";
    String rtgsFileName = "";
    private ArrayList fileFormat;
    private int formatSize = 0;

    /**
     * Creates new form TokenConfigUI
     */
    public DCBFileUploadUI() {
        returnMap = null;
        currDate = ClientUtil.getCurrentDate();
        initForm();
    }

    /**
     * Method which is used to initialize the form TokenConfig
     */
    private void initForm() {
        initComponents();
        observable = new DCBFileUploadOB();
        initTableData();
        ClientUtil.enableDisable(panLoadFromFile, false);
        ClientUtil.enableDisable(panRtgsLoadFromFile, false);
        btnBrowse.setEnabled(false);
        btnRtgsBrowse.setEnabled(false);
        setButtonEnableDisable();
        panRtgsFileLoad.setVisible(true);
        panFileLoad.setVisible(true);
        lblFileID.setVisible(false);
        lblFileID.setText("");
        calcTotal();
        setSizeTableData();
    }
    
    private void initTableData() {
        tblFileDataDetails.setModel(observable.getTblFileDataDetails());
    }

    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());

        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());

        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
    }

    /*
     * Auto Generated Method - setMandatoryHashMap()
     *
     * ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     *
     * This method list out all the Input Fields available in the UI. It needs a
     * class level HashMap variable mandatoryMap.
     */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
    }

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /**
     * Used to set Maximum possible lenghts for TextFields
     */
    private void setMaxLengths() {
    }

    /**
     * Method used to Give a Alert when any Mandatory Field is not filled by the
     * user
     */
    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    /** To display a popUp window for viewing existing data */
    private void callView(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        if (currAction.equalsIgnoreCase("Edit")) {
            viewMap.put(CommonConstants.MAP_NAME, "getDCBFileEdit");
        }
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this, viewMap).show();
    }

    /*
     * Fills up the HashMap with data when user selects the row in ViewAll
     * screen
     */
    public void fillData(Object obj) {
        try {
            HashMap hashMap = (HashMap) obj;
            isFilled = true;
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                observable.resetTableValues();
                panFileLoad.setVisible(false);
                lblFileID.setVisible(true);
                this.setButtonEnableDisable();
                lblFileID.setText(CommonUtil.convertObjToStr(hashMap.get("FILE_ID")));
                observable.getData(hashMap);
                totCustomerList = new ArrayList();
                totCustomerList = observable.getEditFileDataList();
                tableDataAlignment(totCustomerList);
                setSizeTableData();
                calcTotal();
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
                    btnSave.setEnabled(false);
                }
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                observable.resetTableValues();
                panFileLoad.setVisible(false);
                lblFileID.setVisible(true);
                this.setButtonEnableDisable();
                lblFileID.setText(CommonUtil.convertObjToStr(hashMap.get("FILE_ID")));
                observable.getData(hashMap);
                totCustomerList = new ArrayList();
                totCustomerList = observable.getEditFileDataList();
                tableDataAlignment(totCustomerList);
                setSizeTableData();
                calcTotal();
            }
            if (viewType == AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                ClientUtil.enableDisable(this, false);
            }
            hashMap = null;
            btnCancel.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panAchInwardDetails = new com.see.truetransact.uicomponent.CPanel();
        tabNachInwardDetails = new com.see.truetransact.uicomponent.CTabbedPane();
        panAchInwardInsideDetails = new com.see.truetransact.uicomponent.CPanel();
        panFileLoad = new com.see.truetransact.uicomponent.CPanel();
        panLoadFromFile = new com.see.truetransact.uicomponent.CPanel();
        txtFileName = new com.see.truetransact.uicomponent.CTextField();
        lblFileName = new com.see.truetransact.uicomponent.CLabel();
        btnBrowse = new com.see.truetransact.uicomponent.CButton();
        panFileTableData = new com.see.truetransact.uicomponent.CPanel();
        srpFileDataDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblFileDataDetails = new com.see.truetransact.uicomponent.CTable();
        panProcess = new com.see.truetransact.uicomponent.CPanel();
        lblTotalCreditAmt = new com.see.truetransact.uicomponent.CLabel();
        lblTotalCreditAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotalRecord = new com.see.truetransact.uicomponent.CLabel();
        lblTotalRecordVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotalCreditAmt1 = new com.see.truetransact.uicomponent.CLabel();
        lblTotalDebitAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblAlignment = new com.see.truetransact.uicomponent.CLabel();
        lblAlignment1 = new com.see.truetransact.uicomponent.CLabel();
        lblFileID = new com.see.truetransact.uicomponent.CLabel();
        panRtgsDetails = new com.see.truetransact.uicomponent.CPanel();
        panRtgsFileLoad = new com.see.truetransact.uicomponent.CPanel();
        panRtgsLoadFromFile = new com.see.truetransact.uicomponent.CPanel();
        txtRtgsFileName = new com.see.truetransact.uicomponent.CTextField();
        lblFileName1 = new com.see.truetransact.uicomponent.CLabel();
        btnRtgsBrowse = new com.see.truetransact.uicomponent.CButton();
        panRtgsFileTableData = new com.see.truetransact.uicomponent.CPanel();
        srpRtgsFileDataDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblRtgsFileDataDetails = new com.see.truetransact.uicomponent.CTable();
        panRtgsProcess = new com.see.truetransact.uicomponent.CPanel();
        lblRtgsTotalCreditAmt = new com.see.truetransact.uicomponent.CLabel();
        lblRtgsTotalCreditAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblRtgsTotalRecord = new com.see.truetransact.uicomponent.CLabel();
        lblRtgsTotalRecordVal = new com.see.truetransact.uicomponent.CLabel();
        lblRtgsTotalDebitAmt = new com.see.truetransact.uicomponent.CLabel();
        lblRtgsTotalDebitAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblRtgsAlignment = new com.see.truetransact.uicomponent.CLabel();
        lblAlignment3 = new com.see.truetransact.uicomponent.CLabel();
        lblRtgsFileID = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrHead = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(860, 655));
        setMinimumSize(new java.awt.Dimension(860, 655));
        setPreferredSize(new java.awt.Dimension(860, 655));

        panAchInwardDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panAchInwardDetails.setMaximumSize(new java.awt.Dimension(870, 630));
        panAchInwardDetails.setMinimumSize(new java.awt.Dimension(870, 630));
        panAchInwardDetails.setPreferredSize(new java.awt.Dimension(870, 630));
        panAchInwardDetails.setLayout(new java.awt.GridBagLayout());

        tabNachInwardDetails.setMinimumSize(new java.awt.Dimension(830, 640));
        tabNachInwardDetails.setName(""); // NOI18N
        tabNachInwardDetails.setPreferredSize(new java.awt.Dimension(830, 640));

        panAchInwardInsideDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panAchInwardInsideDetails.setMaximumSize(new java.awt.Dimension(870, 630));
        panAchInwardInsideDetails.setMinimumSize(new java.awt.Dimension(870, 630));
        panAchInwardInsideDetails.setPreferredSize(new java.awt.Dimension(870, 630));
        panAchInwardInsideDetails.setLayout(new java.awt.GridBagLayout());

        panFileLoad.setBorder(javax.swing.BorderFactory.createTitledBorder("File Details"));
        panFileLoad.setMinimumSize(new java.awt.Dimension(850, 100));
        panFileLoad.setPreferredSize(new java.awt.Dimension(850, 100));
        panFileLoad.setLayout(new java.awt.GridBagLayout());

        panLoadFromFile.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLoadFromFile.setMinimumSize(new java.awt.Dimension(200, 70));
        panLoadFromFile.setPreferredSize(new java.awt.Dimension(200, 70));
        panLoadFromFile.setLayout(new java.awt.GridBagLayout());

        txtFileName.setMinimumSize(new java.awt.Dimension(380, 23));
        txtFileName.setPreferredSize(new java.awt.Dimension(380, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoadFromFile.add(txtFileName, gridBagConstraints);

        lblFileName.setText("File");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoadFromFile.add(lblFileName, gridBagConstraints);

        btnBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_FIND.gif"))); // NOI18N
        btnBrowse.setText("Browse");
        btnBrowse.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoadFromFile.add(btnBrowse, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panFileLoad.add(panLoadFromFile, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAchInwardInsideDetails.add(panFileLoad, gridBagConstraints);

        panFileTableData.setBorder(javax.swing.BorderFactory.createTitledBorder("File Record Details"));
        panFileTableData.setMinimumSize(new java.awt.Dimension(850, 368));
        panFileTableData.setPreferredSize(new java.awt.Dimension(850, 368));
        panFileTableData.setLayout(new java.awt.GridBagLayout());

        srpFileDataDetails.setMinimumSize(new java.awt.Dimension(840, 335));
        srpFileDataDetails.setPreferredSize(new java.awt.Dimension(840, 335));
        srpFileDataDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                srpFileDataDetailsMouseClicked(evt);
            }
        });

        tblFileDataDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TRANS_CODE", "DEST_BANK", "ACC_TYPE", "FOLIO NUMBER", "AADHAAR NUM", "HOLDER NAME", "SPONSOR_BANK", "AMOUNT", "ACT_NUM"
            }
        ));
        tblFileDataDetails.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblFileDataDetails.setMaximumSize(new java.awt.Dimension(2147483647, 64));
        tblFileDataDetails.setMinimumSize(new java.awt.Dimension(975, 0));
        tblFileDataDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 331));
        tblFileDataDetails.setSelectionBackground(new java.awt.Color(255, 255, 51));
        tblFileDataDetails.setSelectionForeground(new java.awt.Color(0, 102, 0));
        tblFileDataDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFileDataDetailsMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblFileDataDetailsMouseReleased(evt);
            }
        });
        tblFileDataDetails.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblFileDataDetailsFocusLost(evt);
            }
        });
        srpFileDataDetails.setViewportView(tblFileDataDetails);

        panFileTableData.add(srpFileDataDetails, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAchInwardInsideDetails.add(panFileTableData, gridBagConstraints);

        panProcess.setMinimumSize(new java.awt.Dimension(780, 65));
        panProcess.setPreferredSize(new java.awt.Dimension(780, 65));
        panProcess.setLayout(new java.awt.GridBagLayout());

        lblTotalCreditAmt.setForeground(new java.awt.Color(0, 102, 0));
        lblTotalCreditAmt.setText("Total Credit Rs.");
        lblTotalCreditAmt.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 4);
        panProcess.add(lblTotalCreditAmt, gridBagConstraints);

        lblTotalCreditAmtVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTotalCreditAmtVal.setForeground(new java.awt.Color(0, 102, 0));
        lblTotalCreditAmtVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalCreditAmtVal.setMinimumSize(new java.awt.Dimension(140, 21));
        lblTotalCreditAmtVal.setPreferredSize(new java.awt.Dimension(140, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProcess.add(lblTotalCreditAmtVal, gridBagConstraints);

        lblTotalRecord.setForeground(new java.awt.Color(204, 0, 0));
        lblTotalRecord.setText("Total Record :");
        lblTotalRecord.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 4);
        panProcess.add(lblTotalRecord, gridBagConstraints);

        lblTotalRecordVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTotalRecordVal.setForeground(new java.awt.Color(204, 0, 0));
        lblTotalRecordVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalRecordVal.setMinimumSize(new java.awt.Dimension(70, 21));
        lblTotalRecordVal.setPreferredSize(new java.awt.Dimension(70, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        panProcess.add(lblTotalRecordVal, gridBagConstraints);

        lblTotalCreditAmt1.setForeground(new java.awt.Color(0, 102, 0));
        lblTotalCreditAmt1.setText("Total Debit Rs.");
        lblTotalCreditAmt1.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 4);
        panProcess.add(lblTotalCreditAmt1, gridBagConstraints);

        lblTotalDebitAmtVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTotalDebitAmtVal.setForeground(new java.awt.Color(0, 102, 0));
        lblTotalDebitAmtVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalDebitAmtVal.setMinimumSize(new java.awt.Dimension(140, 21));
        lblTotalDebitAmtVal.setPreferredSize(new java.awt.Dimension(140, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProcess.add(lblTotalDebitAmtVal, gridBagConstraints);

        lblAlignment.setForeground(new java.awt.Color(0, 0, 204));
        lblAlignment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAlignment.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAlignment.setMaximumSize(new java.awt.Dimension(325, 20));
        lblAlignment.setMinimumSize(new java.awt.Dimension(325, 20));
        lblAlignment.setPreferredSize(new java.awt.Dimension(325, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panProcess.add(lblAlignment, gridBagConstraints);

        lblAlignment1.setForeground(new java.awt.Color(0, 0, 204));
        lblAlignment1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAlignment1.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAlignment1.setMaximumSize(new java.awt.Dimension(70, 20));
        lblAlignment1.setMinimumSize(new java.awt.Dimension(70, 20));
        lblAlignment1.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panProcess.add(lblAlignment1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAchInwardInsideDetails.add(panProcess, gridBagConstraints);

        lblFileID.setForeground(new java.awt.Color(0, 0, 204));
        lblFileID.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFileID.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblFileID.setMaximumSize(new java.awt.Dimension(325, 20));
        lblFileID.setMinimumSize(new java.awt.Dimension(325, 20));
        lblFileID.setPreferredSize(new java.awt.Dimension(325, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAchInwardInsideDetails.add(lblFileID, gridBagConstraints);

        tabNachInwardDetails.addTab("DCB File Upload Details", panAchInwardInsideDetails);

        panRtgsDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panRtgsDetails.setMaximumSize(new java.awt.Dimension(870, 630));
        panRtgsDetails.setMinimumSize(new java.awt.Dimension(870, 630));
        panRtgsDetails.setPreferredSize(new java.awt.Dimension(870, 630));
        panRtgsDetails.setLayout(new java.awt.GridBagLayout());

        panRtgsFileLoad.setBorder(javax.swing.BorderFactory.createTitledBorder("File Details"));
        panRtgsFileLoad.setMinimumSize(new java.awt.Dimension(850, 100));
        panRtgsFileLoad.setPreferredSize(new java.awt.Dimension(850, 100));
        panRtgsFileLoad.setLayout(new java.awt.GridBagLayout());

        panRtgsLoadFromFile.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panRtgsLoadFromFile.setMinimumSize(new java.awt.Dimension(200, 70));
        panRtgsLoadFromFile.setPreferredSize(new java.awt.Dimension(200, 70));
        panRtgsLoadFromFile.setLayout(new java.awt.GridBagLayout());

        txtRtgsFileName.setMinimumSize(new java.awt.Dimension(380, 23));
        txtRtgsFileName.setPreferredSize(new java.awt.Dimension(380, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRtgsLoadFromFile.add(txtRtgsFileName, gridBagConstraints);

        lblFileName1.setText("File");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRtgsLoadFromFile.add(lblFileName1, gridBagConstraints);

        btnRtgsBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_FIND.gif"))); // NOI18N
        btnRtgsBrowse.setText("Browse");
        btnRtgsBrowse.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnRtgsBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRtgsBrowseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRtgsLoadFromFile.add(btnRtgsBrowse, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRtgsFileLoad.add(panRtgsLoadFromFile, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRtgsDetails.add(panRtgsFileLoad, gridBagConstraints);

        panRtgsFileTableData.setBorder(javax.swing.BorderFactory.createTitledBorder("File Record Details"));
        panRtgsFileTableData.setMinimumSize(new java.awt.Dimension(850, 368));
        panRtgsFileTableData.setPreferredSize(new java.awt.Dimension(850, 368));
        panRtgsFileTableData.setLayout(new java.awt.GridBagLayout());

        srpRtgsFileDataDetails.setMinimumSize(new java.awt.Dimension(840, 335));
        srpRtgsFileDataDetails.setPreferredSize(new java.awt.Dimension(840, 335));
        srpRtgsFileDataDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                srpRtgsFileDataDetailsMouseClicked(evt);
            }
        });

        tblRtgsFileDataDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TRANS_CODE", "DEST_BANK", "ACC_TYPE", "FOLIO NUMBER", "AADHAAR NUM", "HOLDER NAME", "SPONSOR_BANK", "AMOUNT", "ACT_NUM"
            }
        ));
        tblRtgsFileDataDetails.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblRtgsFileDataDetails.setMaximumSize(new java.awt.Dimension(2147483647, 64));
        tblRtgsFileDataDetails.setMinimumSize(new java.awt.Dimension(975, 0));
        tblRtgsFileDataDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 331));
        tblRtgsFileDataDetails.setSelectionBackground(new java.awt.Color(255, 255, 51));
        tblRtgsFileDataDetails.setSelectionForeground(new java.awt.Color(0, 102, 0));
        tblRtgsFileDataDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRtgsFileDataDetailsMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblRtgsFileDataDetailsMouseReleased(evt);
            }
        });
        tblRtgsFileDataDetails.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblRtgsFileDataDetailsFocusLost(evt);
            }
        });
        srpRtgsFileDataDetails.setViewportView(tblRtgsFileDataDetails);

        panRtgsFileTableData.add(srpRtgsFileDataDetails, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRtgsDetails.add(panRtgsFileTableData, gridBagConstraints);

        panRtgsProcess.setMinimumSize(new java.awt.Dimension(780, 65));
        panRtgsProcess.setPreferredSize(new java.awt.Dimension(780, 65));
        panRtgsProcess.setLayout(new java.awt.GridBagLayout());

        lblRtgsTotalCreditAmt.setForeground(new java.awt.Color(0, 102, 0));
        lblRtgsTotalCreditAmt.setText("Total Credit Rs.");
        lblRtgsTotalCreditAmt.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 4);
        panRtgsProcess.add(lblRtgsTotalCreditAmt, gridBagConstraints);

        lblRtgsTotalCreditAmtVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblRtgsTotalCreditAmtVal.setForeground(new java.awt.Color(0, 102, 0));
        lblRtgsTotalCreditAmtVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblRtgsTotalCreditAmtVal.setMinimumSize(new java.awt.Dimension(140, 21));
        lblRtgsTotalCreditAmtVal.setPreferredSize(new java.awt.Dimension(140, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panRtgsProcess.add(lblRtgsTotalCreditAmtVal, gridBagConstraints);

        lblRtgsTotalRecord.setForeground(new java.awt.Color(204, 0, 0));
        lblRtgsTotalRecord.setText("Total Record :");
        lblRtgsTotalRecord.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 4);
        panRtgsProcess.add(lblRtgsTotalRecord, gridBagConstraints);

        lblRtgsTotalRecordVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblRtgsTotalRecordVal.setForeground(new java.awt.Color(204, 0, 0));
        lblRtgsTotalRecordVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblRtgsTotalRecordVal.setMinimumSize(new java.awt.Dimension(70, 21));
        lblRtgsTotalRecordVal.setPreferredSize(new java.awt.Dimension(70, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        panRtgsProcess.add(lblRtgsTotalRecordVal, gridBagConstraints);

        lblRtgsTotalDebitAmt.setForeground(new java.awt.Color(0, 102, 0));
        lblRtgsTotalDebitAmt.setText("Total Debit Rs.");
        lblRtgsTotalDebitAmt.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 4);
        panRtgsProcess.add(lblRtgsTotalDebitAmt, gridBagConstraints);

        lblRtgsTotalDebitAmtVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblRtgsTotalDebitAmtVal.setForeground(new java.awt.Color(0, 102, 0));
        lblRtgsTotalDebitAmtVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblRtgsTotalDebitAmtVal.setMinimumSize(new java.awt.Dimension(140, 21));
        lblRtgsTotalDebitAmtVal.setPreferredSize(new java.awt.Dimension(140, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panRtgsProcess.add(lblRtgsTotalDebitAmtVal, gridBagConstraints);

        lblRtgsAlignment.setForeground(new java.awt.Color(0, 0, 204));
        lblRtgsAlignment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRtgsAlignment.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblRtgsAlignment.setMaximumSize(new java.awt.Dimension(325, 20));
        lblRtgsAlignment.setMinimumSize(new java.awt.Dimension(325, 20));
        lblRtgsAlignment.setPreferredSize(new java.awt.Dimension(325, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRtgsProcess.add(lblRtgsAlignment, gridBagConstraints);

        lblAlignment3.setForeground(new java.awt.Color(0, 0, 204));
        lblAlignment3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAlignment3.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAlignment3.setMaximumSize(new java.awt.Dimension(70, 20));
        lblAlignment3.setMinimumSize(new java.awt.Dimension(70, 20));
        lblAlignment3.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRtgsProcess.add(lblAlignment3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panRtgsDetails.add(panRtgsProcess, gridBagConstraints);

        lblRtgsFileID.setForeground(new java.awt.Color(0, 0, 204));
        lblRtgsFileID.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRtgsFileID.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblRtgsFileID.setMaximumSize(new java.awt.Dimension(325, 20));
        lblRtgsFileID.setMinimumSize(new java.awt.Dimension(325, 20));
        lblRtgsFileID.setPreferredSize(new java.awt.Dimension(325, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRtgsDetails.add(lblRtgsFileID, gridBagConstraints);

        tabNachInwardDetails.addTab("RTGS/NEFT File Upload Details", panRtgsDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAchInwardDetails.add(tabNachInwardDetails, gridBagConstraints);

        getContentPane().add(panAchInwardDetails, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrHead.add(btnNew);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace26);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrHead.add(btnEdit);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace27);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrHead.add(btnDelete);

        lblSpace2.setText("     ");
        tbrHead.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrHead.add(btnSave);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace28);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrHead.add(btnCancel);

        lblSpace3.setText("     ");
        tbrHead.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrHead.add(btnAuthorize);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace29);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrHead.add(btnException);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace30);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrHead.add(btnReject);

        lblSpace4.setText("     ");
        tbrHead.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrHead.add(btnPrint);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace31);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrHead.add(btnClose);

        getContentPane().add(tbrHead, java.awt.BorderLayout.NORTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblFileDataDetailsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblFileDataDetailsFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblFileDataDetailsFocusLost

    private void srpFileDataDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_srpFileDataDetailsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_srpFileDataDetailsMouseClicked

    private void tblFileDataDetailsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFileDataDetailsMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblFileDataDetailsMouseReleased

    private void setColorList() {
        if (tblFileDataDetails.getRowCount() > 0) {
            colorList = new ArrayList();
            for (int i = 0; i < tblFileDataDetails.getRowCount(); i++) {
                if (CommonUtil.convertObjToStr(tblFileDataDetails.getValueAt(i, 6)).length()<=0) {
                    colorList.add(String.valueOf(i));
                }
            }
        }
    }
    
    private void setColour() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (colorList.contains(String.valueOf(row))) {
                    setForeground(Color.RED);
                } else {
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblFileDataDetails.setDefaultRenderer(Object.class, renderer);
    }

    private void tblFileDataDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFileDataDetailsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblFileDataDetailsMouseClicked

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        // TODO add your handling code here:
        try {
            javax.swing.JFileChooser fc = new javax.swing.JFileChooser();;
            int returnVal = fc.showOpenDialog(null);
            if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                java.io.File file = fc.getSelectedFile();
                String fname = file.getName();
                fname = fname.toUpperCase();
                HashMap whereMap = new HashMap();
                if (fname != null || !fname.equalsIgnoreCase("")) {
                    if (fname.substring(fname.indexOf('.') + 1, fname.length()).equals("TXT")) {
                        txtFileName.setText(fc.getSelectedFile().toString());
                        txtFileName.setEnabled(false);
                        fileName = file.getName();
                        whereMap.put("FILE_NAME", fileName);
                        List fileAlreadyDoneLst = ClientUtil.executeQuery("getToCheckDCBFileAlreadyDone", whereMap);
                        if (fileAlreadyDoneLst != null && fileAlreadyDoneLst.size() > 0) {
                            whereMap = (HashMap) fileAlreadyDoneLst.get(0);
                            ClientUtil.showMessageWindow("This File Already Uploaded, Can't Upload Again  - "+whereMap.get("FILE_ID"));
                            btnCancelActionPerformed(null);
                            return;
                        }
                        observable.setFileName(fileName);
                    } 
                    else {
                        displayAlert("File should be .txt format");
                        return;
                    }
                }
                selectedFile = file;
                getFileData(file);
                setSizeTableData();
                calcTotal();
                btnBrowse.setEnabled(false);
            }
        } catch (Exception e) {
            displayAlert("File should be .txt format");
        }
    }//GEN-LAST:event_btnBrowseActionPerformed
    
    
    public void getFileData(java.io.File file) {
        try {
            totCustomerList = new ArrayList();
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);
            String line = "";
            String qualifeir = "";
            String colSeparator = "";
            HashMap whereMap = new HashMap();
            Iterator iterator;
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            List settingsList = ClientUtil.executeQuery("getParameterSettings", whereMap);
            if (settingsList != null && settingsList.size() > 0) {
                whereMap = (HashMap) settingsList.get(0);
                colSeparator = CommonUtil.convertObjToStr(whereMap.get("COLSEP"));
                if (colSeparator.equals("|")) {
                    colSeparator = "\\|";
                }
            } else {
                ClientUtil.showMessageWindow("Please Set File Column Separator Settings in this Branch!!!");
                return;
            }
            List customerList = new ArrayList();
            int x = 1;
            while (dis.available() > 0 && (line = dis.readLine()).length() > 0) {
                if (x == 0) {
                    x = x + 1;
                } else {
                    String[] parts = line.split(colSeparator);
                    customerList = new ArrayList();
                    for (String s : parts) {
                        customerList.add(s);
                    }
                    if (customerList != null && customerList.size() > 0) {
                        totCustomerList.add(customerList);
                    }
                }
            }
            if (!(totCustomerList != null && totCustomerList.size() > 0)) {
                ClientUtil.showMessageWindow("Can't Read the File!!");
                return;
            }
            //To Check Account Number is Valid or Not.
            if(totCustomerList != null && totCustomerList.size() > 0) {
                StringBuffer act_num = new StringBuffer();
                StringBuffer act_num1 = new StringBuffer();
             
                HashMap act_Map = new HashMap();
                HashMap accountMap = new HashMap();
                ArrayList singleList = new ArrayList();
                String actNum ="";
                for (int i = 0; i < totCustomerList.size(); i++) {
                    singleList = (ArrayList) totCustomerList.get(i);
                    if (i < 999) {
                        if (act_num.length() > 0) {
                            act_num.append(",");
                        }
                        act_num.append("'" + CommonUtil.convertObjToStr(singleList.get(7)) + "'");
                        accountMap.put(CommonUtil.convertObjToStr(singleList.get(7)), CommonUtil.convertObjToStr(singleList.get(7)));
                    } else {
                        if (act_num1.length() > 0) {
                            act_num1.append(",");
                        }
                        act_num1.append("'" + CommonUtil.convertObjToStr(singleList.get(7)) + "'");
                        accountMap.put(CommonUtil.convertObjToStr(singleList.get(7)), CommonUtil.convertObjToStr(singleList.get(7)));
                    }
                }
                if (totCustomerList.size() < 999) {
                    act_num1.append("''");
                }
                act_Map.put("ACT_NUM", act_num);
                act_Map.put("ACT_NUM1", act_num1);
                List actLst = ClientUtil.executeQuery("getVerifiedAllActNumber", act_Map);
                if (actLst != null && actLst.size() > 0) {
                    for (int i = 0; i < actLst.size(); i++) {
                        act_Map = new HashMap();
                        act_Map = (HashMap) actLst.get(i);
                        actNum = CommonUtil.convertObjToStr(act_Map.get("ACT_NUM"));
                        if(accountMap.containsKey(actNum)){
                           accountMap.remove(actNum);
                        }
                    }
                }
                if(accountMap!=null && accountMap.size()>0){
                    int count = 0;
                    act_num = new StringBuffer();
                    iterator = accountMap.keySet().iterator();
                    for (int i = 0; i < accountMap.size(); i++) {
                        if (act_num.length() > 0 && count > 0) {
                            act_num.append(",");
                        }
                        act_num.append(" " + CommonUtil.convertObjToStr(iterator.next()) + "");
                        count++;
                        if (count == 5) {
                            act_num.append(",");
                            act_num.append("\n");
                            count = 0;
                        }
                    }
                    if (act_num.length() > 0) {
                        ClientUtil.showMessageWindow("Please check the Txt data. The Following A/C Nos are Wrong : \n" + act_num.toString());
                        return;
                    }
                }                
            }
            tableDataAlignment(totCustomerList);
            if (dis != null) {
                dis.close();
            }
            if (bis != null) {
                bis.close();
            }
            if (fis != null) {
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("#### File Upload Error : " + e);
        }
    }

    private void tableDataAlignment(List totFileDataList) {
        ArrayList _heading = new ArrayList();
        if (totFileDataList != null && totFileDataList.size() > 0) {
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(tblFileDataDetails);
            TableModel tableModel = new TableModel();
            _heading.add("SOC_BRACH_ID");
            _heading.add("TRANS_DT");
            _heading.add("TRANS_TIME");
            _heading.add("RECORD_NO");
            _heading.add("DR/CR");
            _heading.add("CTQ");
            _heading.add("TRANS_AMOUNT");
            _heading.add("SOC_AC_NUM");
            _heading.add("EDCB_AC_NUM");
            _heading.add("CHQ_NUM");
            _heading.add("CHQ_DT");
            _heading.add("REF_NUM");
            _heading.add("PARTICULARS");
            _heading.add("CLIENT_ID");
            _heading.add("USER");
            _heading.add("TRANS_STATUS");
            tableModel.setHeading(_heading);
            tableModel.setData((ArrayList) totFileDataList);
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            tblFileDataDetails.setAutoResizeMode(0);
            tblFileDataDetails.doLayout();
            tblFileDataDetails.setModel(tableSorter);
            tblFileDataDetails.revalidate();
        }
    }
 private void rtgsTableDataAlignment(List totFileDataList) {
        ArrayList _heading = new ArrayList();
        if (totFileDataList != null && totFileDataList.size() > 0) {
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(tblRtgsFileDataDetails);
            TableModel tableModel = new TableModel();
            _heading.add("INSTITUTION_CODE"); 
            _heading.add("TRANS_DT");
            _heading.add("EMPLOYER_CODE");
            _heading.add("EMPLOYER_NAME");
            _heading.add("MOBILE_NO");
            _heading.add("UNIQUE_ID");
            _heading.add("TRANSACTION_TYPE");
            _heading.add("INSTRUMENT_NO");
            _heading.add("AMOUNT");
            _heading.add("TRANSACTION_SOL_ID");
            _heading.add("TRANSACTION_BRANCH_NAME");
            _heading.add("FOR_FUTURE_USE");
            _heading.add("DR/CR");
            _heading.add("AB_UNIQUE_ID");
            tableModel.setHeading(_heading);
            tableModel.setData((ArrayList) totFileDataList);
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            tblRtgsFileDataDetails.setAutoResizeMode(0);
            tblRtgsFileDataDetails.doLayout();
            tblRtgsFileDataDetails.setModel(tableSorter);
            tblRtgsFileDataDetails.revalidate();
        }
    }
    private void setSizeTableData() {
        tblFileDataDetails.getColumnModel().getColumn(0).setPreferredWidth(90);
        tblFileDataDetails.getColumnModel().getColumn(1).setPreferredWidth(70);
        tblFileDataDetails.getColumnModel().getColumn(2).setPreferredWidth(70);
        tblFileDataDetails.getColumnModel().getColumn(3).setPreferredWidth(70);
        tblFileDataDetails.getColumnModel().getColumn(4).setPreferredWidth(40);
        tblFileDataDetails.getColumnModel().getColumn(5).setPreferredWidth(40);
        tblFileDataDetails.getColumnModel().getColumn(6).setPreferredWidth(110);
        tblFileDataDetails.getColumnModel().getColumn(7).setPreferredWidth(90);
        tblFileDataDetails.getColumnModel().getColumn(8).setPreferredWidth(110);
        tblFileDataDetails.getColumnModel().getColumn(9).setPreferredWidth(70);
        tblFileDataDetails.getColumnModel().getColumn(10).setPreferredWidth(70);
        tblFileDataDetails.getColumnModel().getColumn(11).setPreferredWidth(70);
        tblFileDataDetails.getColumnModel().getColumn(12).setPreferredWidth(200);
        tblFileDataDetails.getColumnModel().getColumn(13).setPreferredWidth(60);
        tblFileDataDetails.getColumnModel().getColumn(14).setPreferredWidth(60);
        tblFileDataDetails.getColumnModel().getColumn(15).setPreferredWidth(90);
        setRightAlignment(6);
    }
     private void setSizeRtgsTableData() {
        tblRtgsFileDataDetails.getColumnModel().getColumn(0).setPreferredWidth(90);
        tblRtgsFileDataDetails.getColumnModel().getColumn(1).setPreferredWidth(70);
        tblRtgsFileDataDetails.getColumnModel().getColumn(2).setPreferredWidth(70);
        tblRtgsFileDataDetails.getColumnModel().getColumn(3).setPreferredWidth(70);
        tblRtgsFileDataDetails.getColumnModel().getColumn(4).setPreferredWidth(40);
        tblRtgsFileDataDetails.getColumnModel().getColumn(5).setPreferredWidth(40);
        tblRtgsFileDataDetails.getColumnModel().getColumn(6).setPreferredWidth(110);
        tblRtgsFileDataDetails.getColumnModel().getColumn(7).setPreferredWidth(90);
        tblRtgsFileDataDetails.getColumnModel().getColumn(8).setPreferredWidth(110);
        tblRtgsFileDataDetails.getColumnModel().getColumn(9).setPreferredWidth(70);
        tblRtgsFileDataDetails.getColumnModel().getColumn(10).setPreferredWidth(70);
        tblRtgsFileDataDetails.getColumnModel().getColumn(11).setPreferredWidth(70);
        tblRtgsFileDataDetails.getColumnModel().getColumn(12).setPreferredWidth(200);
        tblRtgsFileDataDetails.getColumnModel().getColumn(13).setPreferredWidth(60);
        setRtgsRightAlignment(6);
    }

    private void setRtgsRightAlignment(int col) {
        javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
        r.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tblRtgsFileDataDetails.getColumnModel().getColumn(col).setCellRenderer(r);
        tblRtgsFileDataDetails.getColumnModel().getColumn(col).sizeWidthToFit();
    }
    
    
    private void setRightAlignment(int col) {
        javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
        r.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tblFileDataDetails.getColumnModel().getColumn(col).setCellRenderer(r);
        tblFileDataDetails.getColumnModel().getColumn(col).sizeWidthToFit();
    }

    private void setCenterAlignment(int col) {
        javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
        r.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tblFileDataDetails.getColumnModel().getColumn(col).setCellRenderer(r);
        tblFileDataDetails.getColumnModel().getColumn(col).sizeWidthToFit();
    }
    
    private void setLeftAlignment(int col) {
        javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
        r.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tblFileDataDetails.getColumnModel().getColumn(col).setCellRenderer(r);
        tblFileDataDetails.getColumnModel().getColumn(col).sizeWidthToFit();
    }

    private void calcTotal() {
        double totalCrAmount = 0.0;
        double totalDrAmount = 0.0;
        String amount ="";
        int totalRecord = 0;
        HashMap singleDataMap = new HashMap();
        if (tblFileDataDetails.getRowCount() > 0) {
            for (int i = 0; i < tblFileDataDetails.getRowCount(); i++) {
                amount = CommonUtil.convertObjToStr(tblFileDataDetails.getValueAt(i, 6)).replaceAll(",", "");
                if (CommonUtil.convertObjToStr(tblFileDataDetails.getValueAt(i, 4)).equals("C")) {
                    totalCrAmount = totalCrAmount + CommonUtil.convertObjToDouble(amount).doubleValue();
                }else if (CommonUtil.convertObjToStr(tblFileDataDetails.getValueAt(i, 4)).equals("D")) {
                    totalDrAmount = totalDrAmount + CommonUtil.convertObjToDouble(amount).doubleValue();
                }
            }
            totalRecord = CommonUtil.convertObjToInt(tblFileDataDetails.getRowCount());
        }
        long dr = roundOff((long) (totalCrAmount * 1000));
        totalCrAmount = dr / 100.0;
        dr = roundOff((long) (totalDrAmount * 1000));
        totalDrAmount = dr / 100.0;
        lblTotalCreditAmtVal.setText(" "+CurrencyValidation.formatCrore(String.valueOf(totalCrAmount)));
        lblTotalDebitAmtVal.setText(" "+CurrencyValidation.formatCrore(String.valueOf(totalDrAmount)));
        lblTotalRecordVal.setText(" "+String.valueOf(totalRecord));
    }
      private void calcRtgsTotal() {
        double totalCrAmount = 0.0;
        double totalDrAmount = 0.0;
        String amount ="";
        int totalRecord = 0;
        HashMap singleDataMap = new HashMap();
        if (tblRtgsFileDataDetails.getRowCount() > 0) {
            for (int i = 0; i < tblRtgsFileDataDetails.getRowCount(); i++) {
                amount = CommonUtil.convertObjToStr(tblRtgsFileDataDetails.getValueAt(i, 8));
                if (CommonUtil.convertObjToStr(tblRtgsFileDataDetails.getValueAt(i, 12)).equals("C")) {
                    totalCrAmount = totalCrAmount + CommonUtil.convertObjToDouble(amount).doubleValue();
                }else if (CommonUtil.convertObjToStr(tblRtgsFileDataDetails.getValueAt(i, 12)).equals("D")) {
                    totalDrAmount = totalDrAmount + CommonUtil.convertObjToDouble(amount).doubleValue();
                }
            }
            totalRecord = CommonUtil.convertObjToInt(tblRtgsFileDataDetails.getRowCount());
        }
        long dr = roundOff((long) (totalCrAmount * 1000));
        totalCrAmount = dr / 100.0;
        dr = roundOff((long) (totalDrAmount * 1000));
        totalDrAmount = dr / 100.0;
        lblRtgsTotalCreditAmtVal.setText(" "+CurrencyValidation.formatCrore(String.valueOf(totalCrAmount)));
        lblRtgsTotalDebitAmtVal.setText(" "+CurrencyValidation.formatCrore(String.valueOf(totalDrAmount)));
        lblRtgsTotalRecordVal.setText(" "+String.valueOf(totalRecord));
    }
    
    private long roundOff(long amt) { //to avoid decimal part problems by kannan
        long amount = amt / 10;
        int lastDigit = (int) (amt % 10);  //() brackets added because sometimes returns 8 if 0 also.
        if (lastDigit > 5) {
            amount++;
        }
        return amount;
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        ClientUtil.clearAll(this);
        if (tabNachInwardDetails.getSelectedIndex() == 0) {
            btnBrowse.setEnabled(true);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
            panFileLoad.setVisible(true);
            lblFileID.setVisible(false);
            lblFileID.setText("");
            headerTotalAmount = 0.0;
            headerRecordCount = 0.0;
            fileName = "";
            headerDate = "";
            fileNameDate = "";
            finalFileNameLst = null;
        } else {
             observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
            rtgsFileName="";
            btnRtgsBrowse.setEnabled(true);
            panRtgsFileLoad.setVisible(true);
        }
     
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
         if (tabNachInwardDetails.getSelectedIndex() == 0) {
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        callView("Edit");
        lblStatus.setText("Enquiry");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
            if (tabNachInwardDetails.getSelectedIndex() == 0) {
        ClientUtil.enableDisable(panFileLoad,false);
            }
        btnSave.setEnabled(false);
         }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        if (tabNachInwardDetails.getSelectedIndex() == 0) {
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        callView("Edit");
        lblStatus.setText("Enquiry");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panFileLoad,false);
        btnSave.setEnabled(false);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
             if (tabNachInwardDetails.getSelectedIndex() == 0) {
                 observable.setGetSelectedTab("DCB_FILE_UPLOAD");
            if (tblFileDataDetails.getRowCount() > 0 && (totCustomerList != null && totCustomerList.size() > 0)) {
                if (fileName.length() > 0) {
                    HashMap whereMap = new HashMap();
                    whereMap.put("FILE_NAME", fileName);
                    List fileAlreadyDoneLst = ClientUtil.executeQuery("getToCheckDCBFileAlreadyDone", whereMap);
                    if (fileAlreadyDoneLst != null && fileAlreadyDoneLst.size() > 0) {
                        whereMap = (HashMap) fileAlreadyDoneLst.get(0);
                        ClientUtil.showMessageWindow("This File Already Uploaded, Can't Upload Again  - " + whereMap.get("FILE_ID"));
                        btnCancelActionPerformed(null);
                             return;
                         }
                     }
                     savePerformed();
                     btnCancel.setEnabled(true);
                 } else {
                     ClientUtil.showMessageWindow("No Record in Table!!!");
                     return;
                 }
             } else {
                 observable.setGetSelectedTab("RTGS_FILE_UPLOAD");
                 if (tblRtgsFileDataDetails.getRowCount() > 0 && (totrtgsCustomerList != null && totrtgsCustomerList.size() > 0)) {
                     if (rtgsFileName.length() > 0) {
                         HashMap whereMap = new HashMap();
                         whereMap.put("FILE_NAME", rtgsFileName);
                         List fileAlreadyDoneLst = ClientUtil.executeQuery("getToCheckDCBFileAlreadyDone", whereMap);
                         if (fileAlreadyDoneLst != null && fileAlreadyDoneLst.size() > 0) {
                             whereMap = (HashMap) fileAlreadyDoneLst.get(0);
                             ClientUtil.showMessageWindow("This File Already Uploaded, Can't Upload Again  - " + whereMap.get("FILE_ID"));
                             btnCancelActionPerformed(null);
                             return;
                         }
                     }
                     savePerformed();
                     btnCancel.setEnabled(true);
                 } else {
                     ClientUtil.showMessageWindow("No Record in Table!!!");
                     return;
            }
             }
        }else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            if (tblFileDataDetails.getRowCount() > 0) {
                if (lblFileID.getText().length() <= 0) {
                    ClientUtil.showMessageWindow("File ID Should not be Empty !!!");
                    return;
                } else {
                    observable.setFileID(lblFileID.getText());
                }
                savePerformed();
                btnCancel.setEnabled(true);
            } else {
                ClientUtil.showMessageWindow("No Record in Table!!!");
                return;
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed() {
        //Progrees bar added by Suresh R
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                try {
                    if (tabNachInwardDetails.getSelectedIndex() == 0) {
                        observable.doAction(totCustomerList);
                    } else {
                        observable.doAction(totrtgsCustomerList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    if (tabNachInwardDetails.getSelectedIndex() == 0) {
                        if (observable.getProxyReturnMap().containsKey("FILE_ID")) {
                            ClientUtil.showMessageWindow("File ID : " + CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("FILE_ID")));
                            displayTransDetails(CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("FILE_ID")));
                        }
                    } else {
                        ClientUtil.showMessageWindow("Batch ID : " + CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("BATCH_ID")));
                    }
                }
            }
            btnCancelActionPerformed(null);
            btnCancel.setEnabled(true);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
        }
        else
        {
         lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
        }
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        setModified(false);
        ClientUtil.clearAll(this);
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        viewType = "CANCEL";
        lblStatus.setText("               ");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);
        observable.resetForm();
        setModified(false);
        btnNew.setEnabled(true);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnCancel.setEnabled(false);
          if (tabNachInwardDetails.getSelectedIndex() == 0) {
        totCustomerList = null;
        setSizeTableData();
        lblFileID.setText("");
        calcTotal();
        btnBrowse.setEnabled(true);
        finalFileNameLst = null;
        fileName="";
          }
          else
          {
              btnRtgsBrowse.setEnabled(false);
              totrtgsCustomerList=null;
          }
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        if (tabNachInwardDetails.getSelectedIndex() == 0) {
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
          }
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            if (lblFileID.getText().length() > 0) {
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                HashMap singleAuthorizeMap = new HashMap();
                if (authorizeStatus.equals("AUTHORIZED")) {
                    HashMap whereMap = new HashMap();
                    whereMap.put("FILE_ID", lblFileID.getText());
                    List fileAlreadyDoneLst = ClientUtil.executeQuery("getToCheckDCBFileAlreadyDoneAuth", whereMap);
                    if (fileAlreadyDoneLst != null && fileAlreadyDoneLst.size() > 1) {
                        ClientUtil.showMessageWindow("This Batch Can't Authorize, Multiple time transaction done for the Same File, Please Reject the Batch !!! ");
                        btnCancelActionPerformed(null);
                        return;
                    }
                }
                singleAuthorizeMap.put("STATUS", authorizeStatus);
                singleAuthorizeMap.put("FILE_ID", lblFileID.getText());
                singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
                arrList.add(singleAuthorizeMap);
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorize(authorizeMap);
                viewType = "";
                ClientUtil.enableDisable(panLoadFromFile, false);
                singleAuthorizeMap = null;
                arrList = null;
                authorizeMap = null;
            } else {
                ClientUtil.showMessageWindow("File ID Should not be Empty");
                return;
            }
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getDCBFileAuthorize");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    public void authorize(HashMap map) {
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.setModule(getModule());
            observable.setScreen(getScreen());
            observable.setAuthorizeMap(map);
            observable.doAction(null);
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                btnCancelActionPerformed(null);
            }
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    
    private void displayTransDetails(String  linkBatchID) {
        HashMap transMap = new HashMap();
        transMap.put("BATCH_ID", linkBatchID);
        transMap.put("TRANS_DT", currDate);
        transMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        transMap.put("DCB_FILE_UPLOAD", "DCB_FILE_UPLOAD");
        TableDialogUI tableDialogUI = null;
        tableDialogUI = new TableDialogUI("getTransferDetailsWithAccHeadDesc", transMap, "");
        tableDialogUI.setTitle("DCB File Upload Transaction Details");
        tableDialogUI.show();
    }
  
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        if (tabNachInwardDetails.getSelectedIndex() == 0) {
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        }
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        //observable.resetOBFields();
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnRtgsBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRtgsBrowseActionPerformed
        // TODO add your handling code here:
         try {
            javax.swing.JFileChooser fc = new javax.swing.JFileChooser();;
            int returnVal = fc.showOpenDialog(null);
            if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                java.io.File file = fc.getSelectedFile();
                String fname = file.getName();
                fname = fname.toUpperCase();
                HashMap whereMap = new HashMap();
                if (fname != null || !fname.equalsIgnoreCase("")) {
                    if (fname.substring(fname.indexOf('.') + 1, fname.length()).equals("CSV")) {
                        txtRtgsFileName.setText(fc.getSelectedFile().toString());
                        txtRtgsFileName.setEnabled(false);
                        rtgsFileName = file.getName();
                        whereMap.put("FILE_NAME", rtgsFileName);
                        List fileAlreadyDoneLst = ClientUtil.executeQuery("getToCheckDCBFileAlreadyDone", whereMap);
                        if (fileAlreadyDoneLst != null && fileAlreadyDoneLst.size() > 0) {
                            whereMap = (HashMap) fileAlreadyDoneLst.get(0);
                            ClientUtil.showMessageWindow("This File Already Uploaded, Can't Upload Again  - "+whereMap.get("FILE_ID"));
                            btnCancelActionPerformed(null);
                            return;
                        }
                        observable.setFileName(rtgsFileName);
                    } 
                    else {
                        displayAlert("File should be .csv format");
                        return;
                    }
                }
                whereMap = new HashMap();
                whereMap.put("CBMS_KEY", "FILE_UPLOAD_HEAD");
                List debitAchd = ClientUtil.executeQuery("getSelectCbmsParameterValues", whereMap);
                if (debitAchd != null && debitAchd.size() > 0) {
                    HashMap resultMap = new HashMap();
                    resultMap = (HashMap) debitAchd.get(0);
                    if (resultMap.containsKey("CBMS_VALUE") && resultMap.get("CBMS_VALUE") != null) {
                        observable.setDebitAccount(CommonUtil.convertObjToStr(resultMap.get("CBMS_VALUE")));
                    }
                } else {
                    ClientUtil.showMessageWindow("Please Add Debit Account In CBMS_PARAMETERS Table ,Contact Support Team");
                    btnCancelActionPerformed(null);
                    return;
                }
                selectedFile = file;
                processCSV(file);
               setSizeRtgsTableData();
               calcRtgsTotal();
               btnRtgsBrowse.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            displayAlert("Error"+e.getMessage());
        }
    }//GEN-LAST:event_btnRtgsBrowseActionPerformed
 private void processCSV(File file) throws Exception {
      BufferedReader lineReader = new BufferedReader(new FileReader(file));
     String lineText = null;
     List rtgsCustomerList = new ArrayList();
     Iterator iterator;
//     int count = 0;
     lineReader.readLine(); // skip header line
     while ((lineText = lineReader.readLine()) != null) {
         String[] data = lineText.split(",");
         rtgsCustomerList = new ArrayList();
         for (String s : data) {
             rtgsCustomerList.add(s);
         }
         if (rtgsCustomerList != null && rtgsCustomerList.size() > 1) {
             totrtgsCustomerList.add(rtgsCustomerList);
         }
     }
     lineReader.close();
     if (!(totrtgsCustomerList != null && totrtgsCustomerList.size() > 0)) {
         ClientUtil.showMessageWindow("Can't Read the File!!");
         return;
     }
              //To Check Account Number is Valid or Not.
            if(totrtgsCustomerList != null && totrtgsCustomerList.size() > 0) {
                StringBuffer act_num = new StringBuffer();
                StringBuffer act_num1 = new StringBuffer();
             
                HashMap act_Map = new HashMap();
                HashMap accountMap = new HashMap();
                ArrayList singleList = new ArrayList();
                String actNum ="";
                for (int i = 0; i < totrtgsCustomerList.size(); i++) {
                    singleList = (ArrayList) totrtgsCustomerList.get(i);
                    if (i < 999) {
                        if (act_num.length() > 0) {
                            act_num.append(",");
                        }
                        act_num.append("'" +LPad(CommonUtil.convertObjToStr(singleList.get(5)), 13, '0') + "'");
                        accountMap.put(LPad(CommonUtil.convertObjToStr(singleList.get(5)), 13, '0'), LPad(CommonUtil.convertObjToStr(singleList.get(5)), 13, '0'));
                    } else {
                        if (act_num1.length() > 0) {
                            act_num1.append(",");
                        }
                        act_num1.append("'" + LPad(CommonUtil.convertObjToStr(singleList.get(5)), 13, '0') + "'");
                        accountMap.put(LPad(CommonUtil.convertObjToStr(singleList.get(5)), 13, '0'), LPad(CommonUtil.convertObjToStr(singleList.get(5)), 13, '0'));
                    }
                }
                if (totrtgsCustomerList.size() < 999) {
                    act_num1.append("''");
                }
                act_Map.put("ACT_NUM", act_num);
                act_Map.put("ACT_NUM1", act_num1);
                List actLst = ClientUtil.executeQuery("getVerifiedAllActNumber", act_Map);
                if (actLst != null && actLst.size() > 0) {
                    for (int i = 0; i < actLst.size(); i++) {
                        act_Map = new HashMap();
                        act_Map = (HashMap) actLst.get(i);
                        actNum = CommonUtil.convertObjToStr(act_Map.get("ACT_NUM"));
                        if(accountMap.containsKey(actNum)){
                           accountMap.remove(actNum);
                        }
                    }
                }
                if(accountMap!=null && accountMap.size()>0){
                    int count = 0;
                    act_num = new StringBuffer();
                    iterator = accountMap.keySet().iterator();
                    for (int i = 0; i < accountMap.size(); i++) {
                        if (act_num.length() > 0 && count > 0) {
                            act_num.append(",");
                        }
                        act_num.append(" " + CommonUtil.convertObjToStr(iterator.next()) + "");
                        count++;
                        if (count == 5) {
                            act_num.append(",");
                            act_num.append("\n");
                            count = 0;
                        }
                    }
                    if (act_num.length() > 0) {
                        ClientUtil.showMessageWindow("Please check the Txt data. The Following A/C Nos are Wrong : \n" + act_num.toString());
                        return;
                    }
                }                
            }
            rtgsTableDataAlignment(totrtgsCustomerList);
    }
    public static String LPad(String str, Integer length, char car) {
      return (String.format("%" + length + "s", "").replace(" ", String.valueOf(car)) + str).substring(str.length(), length + str.length());
    }
    private void tblRtgsFileDataDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRtgsFileDataDetailsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblRtgsFileDataDetailsMouseClicked

    private void tblRtgsFileDataDetailsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRtgsFileDataDetailsMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblRtgsFileDataDetailsMouseReleased

    private void tblRtgsFileDataDetailsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblRtgsFileDataDetailsFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblRtgsFileDataDetailsFocusLost

    private void srpRtgsFileDataDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_srpRtgsFileDataDetailsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_srpRtgsFileDataDetailsMouseClicked
    
    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDate.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBrowse;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnRtgsBrowse;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CLabel lblAlignment;
    private com.see.truetransact.uicomponent.CLabel lblAlignment1;
    private com.see.truetransact.uicomponent.CLabel lblAlignment3;
    private com.see.truetransact.uicomponent.CLabel lblFileID;
    private com.see.truetransact.uicomponent.CLabel lblFileName;
    private com.see.truetransact.uicomponent.CLabel lblFileName1;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblRtgsAlignment;
    private com.see.truetransact.uicomponent.CLabel lblRtgsFileID;
    private com.see.truetransact.uicomponent.CLabel lblRtgsTotalCreditAmt;
    private com.see.truetransact.uicomponent.CLabel lblRtgsTotalCreditAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblRtgsTotalDebitAmt;
    private com.see.truetransact.uicomponent.CLabel lblRtgsTotalDebitAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblRtgsTotalRecord;
    private com.see.truetransact.uicomponent.CLabel lblRtgsTotalRecordVal;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotalCreditAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalCreditAmt1;
    private com.see.truetransact.uicomponent.CLabel lblTotalCreditAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblTotalDebitAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblTotalRecord;
    private com.see.truetransact.uicomponent.CLabel lblTotalRecordVal;
    private com.see.truetransact.uicomponent.CPanel panAchInwardDetails;
    private com.see.truetransact.uicomponent.CPanel panAchInwardInsideDetails;
    private com.see.truetransact.uicomponent.CPanel panFileLoad;
    private com.see.truetransact.uicomponent.CPanel panFileTableData;
    private com.see.truetransact.uicomponent.CPanel panLoadFromFile;
    private com.see.truetransact.uicomponent.CPanel panProcess;
    private com.see.truetransact.uicomponent.CPanel panRtgsDetails;
    private com.see.truetransact.uicomponent.CPanel panRtgsFileLoad;
    private com.see.truetransact.uicomponent.CPanel panRtgsFileTableData;
    private com.see.truetransact.uicomponent.CPanel panRtgsLoadFromFile;
    private com.see.truetransact.uicomponent.CPanel panRtgsProcess;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CScrollPane srpFileDataDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpRtgsFileDataDetails;
    private com.see.truetransact.uicomponent.CTabbedPane tabNachInwardDetails;
    private com.see.truetransact.uicomponent.CTable tblFileDataDetails;
    private com.see.truetransact.uicomponent.CTable tblRtgsFileDataDetails;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CTextField txtFileName;
    private com.see.truetransact.uicomponent.CTextField txtRtgsFileName;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        DCBFileUploadUI fad = new DCBFileUploadUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(fad);
        j.show();
        fad.show();
    }
}
