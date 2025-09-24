/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 * BranchManagement.java
 *
 * Created on August 6, 2003, 11:44 PM
 *
 * Modified on Feb 09,2004.
 * Modiofications:
 *  1. Added components for Application IP, Port, Data Server IP,Data Server Port, Database Name,
 *     url,Driver, userid, password,balance limit check, max and average cashstock, in User Interface.
 *  2. Added required code in all UI supporting classes i.e. OB RB MRB and HashMap.
 *  3. Regenerated TO classes and Map files.
 *  4. Changed DAO object functionality. For the BranchPhoneTO earlier list of HasMapobjects were passed
 *     now the list of BranchPhoneTO's is passed .
 *  5. Did required changes in OB to implement new DAO objects.
 *
 *  Modified on Apr 12,2004
 *  Modifications:
 *   1.Added components in BranchDetails Branch Short Name, Opening Date, Branch Master ID, Regional Office,
 *     MICR Code, BSR Code, Working Time
 *   2.Functions Added:
 *   2.1 updateOBComboBoxes
 *   2.2 updateComboBoxes
 *   2.3 setBranchDetailsButtonEnableDisable
 *   2.3 setBranchMgrIDandRODisable
 *   3 Modification done in existing function:
 *   3.1 popUp() is converted popUp(arg)
 *   3.2 setFieldNames
 *   3.3 internationalize
 *   3.4 setMandatoryHashMap
 *   3.5 initComponentData
 *   3.6 setHelpMessage
 *
 *  @modified : Sunil
 *      Added Edit Locking - 07-07-2005
 */

package com.see.truetransact.ui.sysadmin.branch;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observable;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.IPValidation;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author  Harvinder
 *  Modified by Karthik
 *  Modified by Hemant on 09.02.2004
 *  Modified by Prasath on 12.04.2004
 *
 */
public class BranchManagementUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {
    private HashMap mandatoryMap;
    private BranchManagementOB observable;
    private int phoneMode = -1;
    final int EDIT=0, DELETE=1,RO=2,ID=3,AUTHORIZE=100, NEW = 4, VIEW=5;
    int viewType=-1;
    private HashMap branchCode;
    final int NO_DUPLICATION = 1;
    boolean isFilled = false;
    final BranchManagementRB resourceBundle = new BranchManagementRB();
    private Date currDt = null;
    /** Creates new form BranchManagement */
    public BranchManagementUI() {
        try{
            currDt = ClientUtil.getCurrentDate();
            initComponents();
            setFieldNames();
            internationalize();
            setMandatoryHashMap();
            setObservable();
            initComponentData();
            setComponentLength();
            update(observable, null);
            initTables();
            enableDisable(false);
            new MandatoryCheck().putMandatoryMarks(getClass().getName(),panContactCode);
            new MandatoryCheck().putMandatoryMarks(getClass().getName(),panBranchParameter);
            new MandatoryCheck().putMandatoryMarks(getClass().getName(),panBranchCoufig);
            new MandatoryCheck().putMandatoryMarks(getClass().getName(),panAddress);
            setBranchDetailsButtonEnableDisable(false);
            setButtonEnableDisable();
            setPhoneButtonEnableDisable();
            setHelpMessage();
            tabBranch.remove(2);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void setObservable(){
        observable = new BranchManagementOB();
        observable.addObserver(this);
    }
    
   /* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtStreet", new Boolean(true));
        mandatoryMap.put("txtArea", new Boolean(true));
        mandatoryMap.put("cboCity", new Boolean(true));
        mandatoryMap.put("cboState", new Boolean(true));
        mandatoryMap.put("cboCountry", new Boolean(true));
        mandatoryMap.put("txtPinCode", new Boolean(true));
        mandatoryMap.put("txtBranchCode", new Boolean(true));
        mandatoryMap.put("txtBranchName", new Boolean(true));
        mandatoryMap.put("txtBranchShortName", new Boolean(true));
        mandatoryMap.put("tdtOpeningDate", new Boolean(true));
        mandatoryMap.put("txtBranchManagerID", new Boolean(true));
        mandatoryMap.put("txtRegionalOffice", new Boolean(true));
        mandatoryMap.put("txtMICRCode", new Boolean(true));
        mandatoryMap.put("txtBSRCode", new Boolean(true));
        mandatoryMap.put("cboFromHrs", new Boolean(true));
        mandatoryMap.put("cboBranchGroup", new Boolean(true));
        mandatoryMap.put("cboFromMin", new Boolean(true));
        mandatoryMap.put("cboToHrs", new Boolean(true));
        mandatoryMap.put("cboToMin", new Boolean(true));
        mandatoryMap.put("cboContactType", new Boolean(true));
        mandatoryMap.put("txtContactNo", new Boolean(true));
        mandatoryMap.put("txtAreaCode", new Boolean(true));
        mandatoryMap.put("txtMaxCashStockBP", new Boolean(true));
        mandatoryMap.put("txtAvgCashStockBP", new Boolean(true));
        mandatoryMap.put("rdoBalanceLimitBP_Yes", new Boolean(true));
        mandatoryMap.put("txtIPAppBC", new Boolean(true));
        mandatoryMap.put("txtPortAppBC", new Boolean(true));
        mandatoryMap.put("txtIPDataBC", new Boolean(true));
        mandatoryMap.put("txtPortDataBC", new Boolean(true));
        mandatoryMap.put("txtDBNameDataBC", new Boolean(true));
        mandatoryMap.put("txtDriverDataBC", new Boolean(true));
        mandatoryMap.put("txtURLDataBC", new Boolean(true));
        mandatoryMap.put("txtUserIDDataBC", new Boolean(true));
        mandatoryMap.put("txtPasswordDataBC", new Boolean(true));
        mandatoryMap.put("cboBranchGLGroup", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void setComponentLength(){
        txtBranchCode.setMaxLength(8);
        txtBranchName.setMaxLength(128);
        txtStreet.setMaxLength(256);
        txtArea.setMaxLength(128);
        txtPinCode.setMaxLength(16);
        txtPinCode.setValidation(new PincodeValidation_IN());
        txtAreaCode.setMaxLength(8);
        txtIPAppBC.setMaxLength(32);
        txtPortAppBC.setMaxLength(6);
        txtMaxCashStockBP.setMaxLength(16);
        txtMaxCashStockBP.setValidation(new CurrencyValidation());
        txtAvgCashStockBP.setMaxLength(16);
        txtAvgCashStockBP.setValidation(new CurrencyValidation());
        txtDBNameDataBC.setMaxLength(64);
        txtIPDataBC.setMaxLength(32);
        txtPortDataBC.setMaxLength(6);
        txtUserIDDataBC.setMaxLength(64);
        txtPasswordDataBC.setMaxLength(64);
        txtDriverDataBC.setMaxLength(128);
        txtURLDataBC.setMaxLength(128);
        txtBranchShortName.setMaxLength(64);
        txtBranchManagerID.setMaxLength(16);
        txtRegionalOffice.setMaxLength(32);
        txtMICRCode.setMaxLength(12);
        txtMICRCode.setValidation(new NumericValidation(12, 0));
        txtBSRCode.setMaxLength(12);
        txtBSRCode.setValidation(new NumericValidation(12, 0));
        txtContactNo.setValidation(new NumericValidation(16, 0));
    }
    /**/
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoBalanceLimitBP = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoShiftGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace19 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace20 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tabBranch = new com.see.truetransact.uicomponent.CTabbedPane();
        panBranchDetails = new com.see.truetransact.uicomponent.CPanel();
        panContactList = new com.see.truetransact.uicomponent.CPanel();
        scrPhoneList = new com.see.truetransact.uicomponent.CScrollPane();
        tblPhoneNos = new com.see.truetransact.uicomponent.CTable();
        panAddress = new com.see.truetransact.uicomponent.CPanel();
        panLeftAddress = new com.see.truetransact.uicomponent.CPanel();
        lblStreet = new com.see.truetransact.uicomponent.CLabel();
        txtStreet = new com.see.truetransact.uicomponent.CTextField();
        lblArea = new com.see.truetransact.uicomponent.CLabel();
        txtArea = new com.see.truetransact.uicomponent.CTextField();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        cboCity = new com.see.truetransact.uicomponent.CComboBox();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        cboState = new com.see.truetransact.uicomponent.CComboBox();
        lblCountry = new com.see.truetransact.uicomponent.CLabel();
        cboCountry = new com.see.truetransact.uicomponent.CComboBox();
        lblPinCode = new com.see.truetransact.uicomponent.CLabel();
        txtPinCode = new com.see.truetransact.uicomponent.CTextField();
        lblBranchCode = new com.see.truetransact.uicomponent.CLabel();
        txtBranchCode = new com.see.truetransact.uicomponent.CTextField();
        lblBranchName = new com.see.truetransact.uicomponent.CLabel();
        txtBranchName = new com.see.truetransact.uicomponent.CTextField();
        lblBranchShortName = new com.see.truetransact.uicomponent.CLabel();
        txtBranchShortName = new com.see.truetransact.uicomponent.CTextField();
        lblBranchGroup = new com.see.truetransact.uicomponent.CLabel();
        cboBranchGroup = new com.see.truetransact.uicomponent.CComboBox();
        lblBranchGLGroup = new com.see.truetransact.uicomponent.CLabel();
        cboBranchGLGroup = new com.see.truetransact.uicomponent.CComboBox();
        panRightAddress = new com.see.truetransact.uicomponent.CPanel();
        lblOpeningDate = new com.see.truetransact.uicomponent.CLabel();
        lblBranchManagerID = new com.see.truetransact.uicomponent.CLabel();
        lblRegionalOffice = new com.see.truetransact.uicomponent.CLabel();
        tdtOpeningDate = new com.see.truetransact.uicomponent.CDateField();
        panBranchManagerID = new com.see.truetransact.uicomponent.CPanel();
        txtBranchManagerID = new com.see.truetransact.uicomponent.CTextField();
        btnBranchManagerID = new com.see.truetransact.uicomponent.CButton();
        panRegionalOffice = new com.see.truetransact.uicomponent.CPanel();
        txtRegionalOffice = new com.see.truetransact.uicomponent.CTextField();
        btnRegionalOffice = new com.see.truetransact.uicomponent.CButton();
        panWorkingTime = new com.see.truetransact.uicomponent.CPanel();
        lblWorkingFrom = new com.see.truetransact.uicomponent.CLabel();
        lblWorkingTo = new com.see.truetransact.uicomponent.CLabel();
        lblWorkingHrs = new com.see.truetransact.uicomponent.CLabel();
        lblWorkingMin = new com.see.truetransact.uicomponent.CLabel();
        cboFromHrs = new com.see.truetransact.uicomponent.CComboBox();
        cboFromMin = new com.see.truetransact.uicomponent.CComboBox();
        cboToHrs = new com.see.truetransact.uicomponent.CComboBox();
        cboToMin = new com.see.truetransact.uicomponent.CComboBox();
        rdoTransTime = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAuthTime = new com.see.truetransact.uicomponent.CRadioButton();
        chkShift = new com.see.truetransact.uicomponent.CCheckBox();
        lblMICRCode = new com.see.truetransact.uicomponent.CLabel();
        txtMICRCode = new com.see.truetransact.uicomponent.CTextField();
        lblBSRCode = new com.see.truetransact.uicomponent.CLabel();
        txtBSRCode = new com.see.truetransact.uicomponent.CTextField();
        lblBranchManagerName = new com.see.truetransact.uicomponent.CLabel();
        lblDisplayBranMgrName = new com.see.truetransact.uicomponent.CLabel();
        lblRegionalOfficerName = new com.see.truetransact.uicomponent.CLabel();
        lblDisplayRegOfficeName = new com.see.truetransact.uicomponent.CLabel();
        sptAddress = new com.see.truetransact.uicomponent.CSeparator();
        panContactDetails = new com.see.truetransact.uicomponent.CPanel();
        sptContact = new com.see.truetransact.uicomponent.CSeparator();
        panPhoneSave = new com.see.truetransact.uicomponent.CPanel();
        btnContactNoAdd = new com.see.truetransact.uicomponent.CButton();
        btnPhoneNew = new com.see.truetransact.uicomponent.CButton();
        btnPhoneDelete = new com.see.truetransact.uicomponent.CButton();
        panContactNo = new com.see.truetransact.uicomponent.CPanel();
        lblContactType = new com.see.truetransact.uicomponent.CLabel();
        lblContactNo = new com.see.truetransact.uicomponent.CLabel();
        cboContactType = new com.see.truetransact.uicomponent.CComboBox();
        txtContactNo = new com.see.truetransact.uicomponent.CTextField();
        panContactCode = new com.see.truetransact.uicomponent.CPanel();
        lblAreaCode = new com.see.truetransact.uicomponent.CLabel();
        txtAreaCode = new com.see.truetransact.uicomponent.CTextField();
        panBranchParameter = new com.see.truetransact.uicomponent.CPanel();
        lblBalanceLimitBP = new com.see.truetransact.uicomponent.CLabel();
        lblMaxCashStockBP = new com.see.truetransact.uicomponent.CLabel();
        lblAvgCashStockBP = new com.see.truetransact.uicomponent.CLabel();
        txtMaxCashStockBP = new com.see.truetransact.uicomponent.CTextField();
        txtAvgCashStockBP = new com.see.truetransact.uicomponent.CTextField();
        rdoBalanceLimitBP_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBalanceLimitBP_No = new com.see.truetransact.uicomponent.CRadioButton();
        panBranchCoufig = new com.see.truetransact.uicomponent.CPanel();
        panAppBC = new com.see.truetransact.uicomponent.CPanel();
        lblIPAppBC = new com.see.truetransact.uicomponent.CLabel();
        lblPortAppBC = new com.see.truetransact.uicomponent.CLabel();
        txtIPAppBC = new com.see.truetransact.uicomponent.CTextField();
        txtPortAppBC = new com.see.truetransact.uicomponent.CTextField();
        panDataBC = new com.see.truetransact.uicomponent.CPanel();
        lblIPDataBC = new com.see.truetransact.uicomponent.CLabel();
        lblPortDataBC = new com.see.truetransact.uicomponent.CLabel();
        lblDBNameDataBC = new com.see.truetransact.uicomponent.CLabel();
        lblDriverDataBC = new com.see.truetransact.uicomponent.CLabel();
        lblURLDataBC = new com.see.truetransact.uicomponent.CLabel();
        lblUserIDDataBC = new com.see.truetransact.uicomponent.CLabel();
        lblPasswordDataBC = new com.see.truetransact.uicomponent.CLabel();
        txtIPDataBC = new com.see.truetransact.uicomponent.CTextField();
        txtPortDataBC = new com.see.truetransact.uicomponent.CTextField();
        txtDBNameDataBC = new com.see.truetransact.uicomponent.CTextField();
        txtDriverDataBC = new com.see.truetransact.uicomponent.CTextField();
        txtURLDataBC = new com.see.truetransact.uicomponent.CTextField();
        txtUserIDDataBC = new com.see.truetransact.uicomponent.CTextField();
        txtPasswordDataBC = new com.see.truetransact.uicomponent.CTextField();
        mbrCustomer = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptNew = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptSave = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Branch Management");
        setMinimumSize(new java.awt.Dimension(830, 630));
        setPreferredSize(new java.awt.Dimension(830, 630));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnView);

        lblSpace4.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnNew);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace18);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnSave);

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace19);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnCancel);

        lblSpace5.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace5);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnAuthorize);

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace21);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnReject);

        lblSpace3.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace3);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace22);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

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

        tabBranch.setMinimumSize(new java.awt.Dimension(830, 600));
        tabBranch.setPreferredSize(new java.awt.Dimension(830, 600));

        panBranchDetails.setMinimumSize(new java.awt.Dimension(830, 600));
        panBranchDetails.setPreferredSize(new java.awt.Dimension(830, 600));
        panBranchDetails.setLayout(new java.awt.GridBagLayout());

        panContactList.setBorder(javax.swing.BorderFactory.createTitledBorder("List of Phone No's"));
        panContactList.setMinimumSize(new java.awt.Dimension(173, 100));
        panContactList.setPreferredSize(new java.awt.Dimension(173, 100));
        panContactList.setLayout(new java.awt.GridBagLayout());

        scrPhoneList.setPreferredSize(new java.awt.Dimension(175, 100));

        tblPhoneNos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Phone ID", "Contact Type", "Contact No"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPhoneNos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPhoneNosMousePressed(evt);
            }
        });
        scrPhoneList.setViewportView(tblPhoneNos);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panContactList.add(scrPhoneList, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 0.3;
        panBranchDetails.add(panContactList, gridBagConstraints);

        panAddress.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAddress.setMinimumSize(new java.awt.Dimension(800, 240));
        panAddress.setPreferredSize(new java.awt.Dimension(800, 240));
        panAddress.setLayout(new java.awt.GridBagLayout());

        panLeftAddress.setMinimumSize(new java.awt.Dimension(205, 415));
        panLeftAddress.setPreferredSize(new java.awt.Dimension(205, 415));
        panLeftAddress.setLayout(new java.awt.GridBagLayout());

        lblStreet.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(lblStreet, gridBagConstraints);

        txtStreet.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(txtStreet, gridBagConstraints);

        lblArea.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(lblArea, gridBagConstraints);

        txtArea.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(txtArea, gridBagConstraints);

        lblCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(lblCity, gridBagConstraints);

        cboCity.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(cboCity, gridBagConstraints);

        lblState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(lblState, gridBagConstraints);

        cboState.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(cboState, gridBagConstraints);

        lblCountry.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(lblCountry, gridBagConstraints);

        cboCountry.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(cboCountry, gridBagConstraints);

        lblPinCode.setText("Pin Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(lblPinCode, gridBagConstraints);

        txtPinCode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPinCode.setValidation(new PincodeValidation_IN());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(txtPinCode, gridBagConstraints);

        lblBranchCode.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblBranchCode.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(lblBranchCode, gridBagConstraints);

        txtBranchCode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBranchCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBranchCodeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(txtBranchCode, gridBagConstraints);

        lblBranchName.setText("Branch Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(lblBranchName, gridBagConstraints);

        txtBranchName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(txtBranchName, gridBagConstraints);

        lblBranchShortName.setText("Branch Short Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(lblBranchShortName, gridBagConstraints);

        txtBranchShortName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(txtBranchShortName, gridBagConstraints);

        lblBranchGroup.setText("Branch Screen Group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(lblBranchGroup, gridBagConstraints);

        cboBranchGroup.setMaximumSize(new java.awt.Dimension(100, 21));
        cboBranchGroup.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(cboBranchGroup, gridBagConstraints);

        lblBranchGLGroup.setText("Branch GLGroup");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(lblBranchGLGroup, gridBagConstraints);

        cboBranchGLGroup.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLeftAddress.add(cboBranchGLGroup, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAddress.add(panLeftAddress, gridBagConstraints);

        panRightAddress.setMinimumSize(new java.awt.Dimension(250, 200));
        panRightAddress.setPreferredSize(new java.awt.Dimension(250, 200));
        panRightAddress.setLayout(new java.awt.GridBagLayout());

        lblOpeningDate.setText("Opening Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRightAddress.add(lblOpeningDate, gridBagConstraints);

        lblBranchManagerID.setText("Branch Manager ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRightAddress.add(lblBranchManagerID, gridBagConstraints);

        lblRegionalOffice.setText("Regional Office");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRightAddress.add(lblRegionalOffice, gridBagConstraints);

        tdtOpeningDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtOpeningDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRightAddress.add(tdtOpeningDate, gridBagConstraints);

        panBranchManagerID.setLayout(new java.awt.GridBagLayout());

        txtBranchManagerID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchManagerID.add(txtBranchManagerID, gridBagConstraints);

        btnBranchManagerID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBranchManagerID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBranchManagerID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBranchManagerIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBranchManagerID.add(btnBranchManagerID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRightAddress.add(panBranchManagerID, gridBagConstraints);

        panRegionalOffice.setLayout(new java.awt.GridBagLayout());

        txtRegionalOffice.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRegionalOffice.add(txtRegionalOffice, gridBagConstraints);

        btnRegionalOffice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnRegionalOffice.setPreferredSize(new java.awt.Dimension(21, 21));
        btnRegionalOffice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegionalOfficeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRegionalOffice.add(btnRegionalOffice, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRightAddress.add(panRegionalOffice, gridBagConstraints);

        panWorkingTime.setBorder(javax.swing.BorderFactory.createTitledBorder("Working Time"));
        panWorkingTime.setMinimumSize(new java.awt.Dimension(350, 130));
        panWorkingTime.setPreferredSize(new java.awt.Dimension(350, 130));
        panWorkingTime.setLayout(new java.awt.GridBagLayout());

        lblWorkingFrom.setText("From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panWorkingTime.add(lblWorkingFrom, gridBagConstraints);

        lblWorkingTo.setText("To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panWorkingTime.add(lblWorkingTo, gridBagConstraints);

        lblWorkingHrs.setText("Hrs");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panWorkingTime.add(lblWorkingHrs, gridBagConstraints);

        lblWorkingMin.setText("Min");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panWorkingTime.add(lblWorkingMin, gridBagConstraints);

        cboFromHrs.setMinimumSize(new java.awt.Dimension(50, 21));
        cboFromHrs.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 2);
        panWorkingTime.add(cboFromHrs, gridBagConstraints);

        cboFromMin.setMinimumSize(new java.awt.Dimension(50, 21));
        cboFromMin.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panWorkingTime.add(cboFromMin, gridBagConstraints);

        cboToHrs.setMinimumSize(new java.awt.Dimension(50, 21));
        cboToHrs.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 2);
        panWorkingTime.add(cboToHrs, gridBagConstraints);

        cboToMin.setMinimumSize(new java.awt.Dimension(50, 21));
        cboToMin.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panWorkingTime.add(cboToMin, gridBagConstraints);

        rdoTransTime.setText("Transaction Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panWorkingTime.add(rdoTransTime, gridBagConstraints);

        rdoAuthTime.setText("Authorization Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panWorkingTime.add(rdoAuthTime, gridBagConstraints);

        chkShift.setText("Shift");
        chkShift.setMaximumSize(new java.awt.Dimension(100, 24));
        chkShift.setMinimumSize(new java.awt.Dimension(100, 24));
        chkShift.setPreferredSize(new java.awt.Dimension(100, 24));
        chkShift.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShiftActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panWorkingTime.add(chkShift, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 34, 0, 0);
        panRightAddress.add(panWorkingTime, gridBagConstraints);

        lblMICRCode.setText("MICR Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRightAddress.add(lblMICRCode, gridBagConstraints);

        txtMICRCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRightAddress.add(txtMICRCode, gridBagConstraints);

        lblBSRCode.setText("BSR Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRightAddress.add(lblBSRCode, gridBagConstraints);

        txtBSRCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRightAddress.add(txtBSRCode, gridBagConstraints);

        lblBranchManagerName.setText("Branch Manager Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRightAddress.add(lblBranchManagerName, gridBagConstraints);

        lblDisplayBranMgrName.setMaximumSize(new java.awt.Dimension(125, 15));
        lblDisplayBranMgrName.setMinimumSize(new java.awt.Dimension(125, 15));
        lblDisplayBranMgrName.setPreferredSize(new java.awt.Dimension(125, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRightAddress.add(lblDisplayBranMgrName, gridBagConstraints);

        lblRegionalOfficerName.setText("Regional Officer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRightAddress.add(lblRegionalOfficerName, gridBagConstraints);

        lblDisplayRegOfficeName.setMaximumSize(new java.awt.Dimension(125, 15));
        lblDisplayRegOfficeName.setMinimumSize(new java.awt.Dimension(125, 15));
        lblDisplayRegOfficeName.setPreferredSize(new java.awt.Dimension(125, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRightAddress.add(lblDisplayRegOfficeName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAddress.add(panRightAddress, gridBagConstraints);

        sptAddress.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptAddress.setMinimumSize(new java.awt.Dimension(2, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panAddress.add(sptAddress, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.weighty = 0.7;
        panBranchDetails.add(panAddress, gridBagConstraints);

        panContactDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Contact Details"));
        panContactDetails.setMinimumSize(new java.awt.Dimension(138, 100));
        panContactDetails.setPreferredSize(new java.awt.Dimension(138, 100));
        panContactDetails.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panContactDetails.add(sptContact, gridBagConstraints);

        panPhoneSave.setLayout(new java.awt.GridBagLayout());

        btnContactNoAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnContactNoAdd.setMaximumSize(new java.awt.Dimension(29, 27));
        btnContactNoAdd.setMinimumSize(new java.awt.Dimension(29, 27));
        btnContactNoAdd.setName("btnContactNoAdd");
        btnContactNoAdd.setPreferredSize(new java.awt.Dimension(29, 27));
        btnContactNoAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContactNoAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhoneSave.add(btnContactNoAdd, gridBagConstraints);

        btnPhoneNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnPhoneNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnPhoneNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnPhoneNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnPhoneNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhoneNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhoneSave.add(btnPhoneNew, gridBagConstraints);

        btnPhoneDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnPhoneDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnPhoneDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnPhoneDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnPhoneDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhoneDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhoneSave.add(btnPhoneDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 65, 0, 0);
        panContactDetails.add(panPhoneSave, gridBagConstraints);

        panContactNo.setLayout(new java.awt.GridBagLayout());

        lblContactType.setText("Contact Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactNo.add(lblContactType, gridBagConstraints);

        lblContactNo.setText("Contact No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactNo.add(lblContactNo, gridBagConstraints);

        cboContactType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactNo.add(cboContactType, gridBagConstraints);

        txtContactNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtContactNo.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactNo.add(txtContactNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 28, 0, 0);
        panContactDetails.add(panContactNo, gridBagConstraints);

        panContactCode.setLayout(new java.awt.GridBagLayout());

        lblAreaCode.setText("Area Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactCode.add(lblAreaCode, gridBagConstraints);

        txtAreaCode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAreaCode.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactCode.add(txtAreaCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 0, 0);
        panContactDetails.add(panContactCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 0.7;
        panBranchDetails.add(panContactDetails, gridBagConstraints);

        tabBranch.addTab("Branch Details", panBranchDetails);

        panBranchParameter.setLayout(new java.awt.GridBagLayout());

        lblBalanceLimitBP.setText("Check Balance Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchParameter.add(lblBalanceLimitBP, gridBagConstraints);

        lblMaxCashStockBP.setText("Maximum Cash Stock");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchParameter.add(lblMaxCashStockBP, gridBagConstraints);

        lblAvgCashStockBP.setText("Average Cash Stock");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchParameter.add(lblAvgCashStockBP, gridBagConstraints);

        txtMaxCashStockBP.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchParameter.add(txtMaxCashStockBP, gridBagConstraints);

        txtAvgCashStockBP.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchParameter.add(txtAvgCashStockBP, gridBagConstraints);

        rdoBalanceLimitBP.add(rdoBalanceLimitBP_Yes);
        rdoBalanceLimitBP_Yes.setText("Yes");
        rdoBalanceLimitBP_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBalanceLimitBP_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panBranchParameter.add(rdoBalanceLimitBP_Yes, gridBagConstraints);

        rdoBalanceLimitBP.add(rdoBalanceLimitBP_No);
        rdoBalanceLimitBP_No.setText("No");
        rdoBalanceLimitBP_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBalanceLimitBP_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panBranchParameter.add(rdoBalanceLimitBP_No, gridBagConstraints);

        tabBranch.addTab("Parameters", panBranchParameter);

        panBranchCoufig.setLayout(new java.awt.GridBagLayout());

        panAppBC.setBorder(javax.swing.BorderFactory.createTitledBorder("Application"));
        panAppBC.setLayout(new java.awt.GridBagLayout());

        lblIPAppBC.setText("IP Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 30, 4, 4);
        panAppBC.add(lblIPAppBC, gridBagConstraints);

        lblPortAppBC.setText("Port");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 30, 4, 4);
        panAppBC.add(lblPortAppBC, gridBagConstraints);

        txtIPAppBC.setValidation(new IPValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppBC.add(txtIPAppBC, gridBagConstraints);

        txtPortAppBC.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppBC.add(txtPortAppBC, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panBranchCoufig.add(panAppBC, gridBagConstraints);

        panDataBC.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Server"));
        panDataBC.setLayout(new java.awt.GridBagLayout());

        lblIPDataBC.setText("IP Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDataBC.add(lblIPDataBC, gridBagConstraints);

        lblPortDataBC.setText("Port");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDataBC.add(lblPortDataBC, gridBagConstraints);

        lblDBNameDataBC.setText("Database Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDataBC.add(lblDBNameDataBC, gridBagConstraints);

        lblDriverDataBC.setText("Database Driver");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDataBC.add(lblDriverDataBC, gridBagConstraints);

        lblURLDataBC.setText("Database URL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDataBC.add(lblURLDataBC, gridBagConstraints);

        lblUserIDDataBC.setText("User ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDataBC.add(lblUserIDDataBC, gridBagConstraints);

        lblPasswordDataBC.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDataBC.add(lblPasswordDataBC, gridBagConstraints);

        txtIPDataBC.setValidation(new IPValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDataBC.add(txtIPDataBC, gridBagConstraints);

        txtPortDataBC.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDataBC.add(txtPortDataBC, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDataBC.add(txtDBNameDataBC, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDataBC.add(txtDriverDataBC, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDataBC.add(txtURLDataBC, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDataBC.add(txtUserIDDataBC, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDataBC.add(txtPasswordDataBC, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panBranchCoufig.add(panDataBC, gridBagConstraints);

        tabBranch.addTab("Configuration", panBranchCoufig);

        getContentPane().add(tabBranch, java.awt.BorderLayout.CENTER);

        mbrCustomer.setName("mbrCustomer");

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess");

        mitNew.setText("New");
        mitNew.setName("mitNew");
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit");
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete");
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew");
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave");
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel");
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave");
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose");
        mnuProcess.add(mitClose);

        mbrCustomer.add(mnuProcess);

        setJMenuBar(mbrCustomer);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chkShiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkShiftActionPerformed
        // TODO add your handling code here:
       if(chkShift.isSelected())
       {
           rdoAuthTime.setVisible(true);
           rdoTransTime.setVisible(true);
       }
       if(!chkShift.isSelected())
       {
           rdoAuthTime.setVisible(false);
           rdoTransTime.setVisible(false);
       }
        
    }//GEN-LAST:event_chkShiftActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void tdtOpeningDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtOpeningDateFocusLost
        // TODO add your handling code here:
        tdtOpeningDateFocusLost();
    }//GEN-LAST:event_tdtOpeningDateFocusLost
    private void tdtOpeningDateFocusLost() {
        try
        {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        
        Date s1= (Date)formatter.parse(tdtOpeningDate.getDateValue());
        
       if(currDt.after(s1))
        {
          JOptionPane.showMessageDialog(this,"Opening date should be after current date","Warning",JOptionPane.WARNING_MESSAGE);
          return;
        }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
//        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)
//        {
//        if (tdtOpeningDate.getDateValue().length() > 0)
//        {
//            String strOpeningDate = observable.getBankOpeningDate();
//            if (strOpeningDate.length() <= 0)
//            {
//                observable.displayOpeningDateWarnMsg();
//                return;
//            }
//            else if (!tdtOpeningDate.getDateValue().equals(strOpeningDate)){
//                ClientUtil.validateToDate(tdtOpeningDate, strOpeningDate);
//            }
//        }
//        }
    }
    private void rdoBalanceLimitBP_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBalanceLimitBP_YesActionPerformed
        // TODO add your handling code here:
        rdoBalanceLimitBP_YesActionPerformed();
    }//GEN-LAST:event_rdoBalanceLimitBP_YesActionPerformed
    private void btnCheck(){
         btnCancel.setEnabled(true);
         btnSave.setEnabled(false);
         btnNew.setEnabled(false);
         btnDelete.setEnabled(false);
         btnAuthorize.setEnabled(false);
         btnReject.setEnabled(false);
         btnException.setEnabled(false);
         btnEdit.setEnabled(false);
     }
    
    
    private void rdoBalanceLimitBP_YesActionPerformed() {
        if (rdoBalanceLimitBP_Yes.isSelected()){
            txtMaxCashStockBP.setEditable(true);
            txtAvgCashStockBP.setEditable(true);
        }
    }
    
    private void rdoBalanceLimitBP_NoActionPerformed() {
        if (rdoBalanceLimitBP_No.isSelected()){
            txtMaxCashStockBP.setText("");
            txtAvgCashStockBP.setText("");
            observable.setTxtAvgCashStockBP("");
            observable.setTxtMaxCashStockBP("");
            txtMaxCashStockBP.setEditable(false);
            txtAvgCashStockBP.setEditable(false);
        }
    }
    private void rdoBalanceLimitBP_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBalanceLimitBP_NoActionPerformed
        // TODO add your handling code here:
        rdoBalanceLimitBP_NoActionPerformed();
    }//GEN-LAST:event_rdoBalanceLimitBP_NoActionPerformed
            private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
                observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
                authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
                        private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
                            observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
                            authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
                        
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("BRANCH CODE", txtBranchCode.getText());
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            ClientUtil.execute("authorizeBranchSetup", singleAuthorizeMap);
            
            btnCancelActionPerformed(null);
            viewType = 0;
        }else {
            final HashMap mapParam = new HashMap();
            
            HashMap authorizeMapCondition = new HashMap();
            authorizeMapCondition.put("STATUS_BY", TrueTransactMain.USER_ID);
            authorizeMapCondition.put("BRANCH CODE", txtBranchCode.getText());
            authorizeMapCondition.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMapCondition.put(CommonConstants.AUTHORIZEDT, currDt.clone());
//            setModified(true);
            mapParam.put(CommonConstants.MAP_WHERE, authorizeMapCondition);
            mapParam.put(CommonConstants.MAP_NAME, "getBranchSetupAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeBranchSetup");
            viewType = AUTHORIZE;
            isFilled = false;
            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            authorizeMapCondition = null;
          lblStatus.setText(observable.getLblStatus());
           
        }
    }
    private void txtBranchCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBranchCodeFocusLost
        // Add your handling code here:
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ){
            branchCode = new HashMap();
            branchCode = (HashMap)observable.checkingDuplicationForBranchCode(txtBranchCode.getText());
        }
    }//GEN-LAST:event_txtBranchCodeFocusLost
    // To update the ComboBoxes  in OB
    private void updateOBComboBoxes(){
        observable.setCboFromHrs((String) cboFromHrs.getSelectedItem());
        observable.setCboFromMin((String) cboFromMin.getSelectedItem());
        observable.setCboToMin((String) cboToMin.getSelectedItem());
        observable.setCboToHrs((String) cboToHrs.getSelectedItem());
    }
    /* To set the selected value in UI */
    private void updateComboBoxes(){
        cboFromHrs.setSelectedItem(observable.getCboFromHrs());
        cboFromMin.setSelectedItem(observable.getCboFromMin());
        cboToMin.setSelectedItem(observable.getCboToMin());
        cboToHrs.setSelectedItem(observable.getCboToHrs());
    }
    private void btnRegionalOfficeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegionalOfficeActionPerformed
        // Add your handling code here:
        popUp(RO);
    }//GEN-LAST:event_btnRegionalOfficeActionPerformed
    
    private void btnBranchManagerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBranchManagerIDActionPerformed
        // Add your handling code here:
        popUp(ID);
    }//GEN-LAST:event_btnBranchManagerIDActionPerformed
		private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
		// Add your handling code here:
		cifClosingAlert();
		}//GEN-LAST:event_btnCloseActionPerformed
		private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
		super.removeEditLock(txtBranchCode.getText());
		observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
		enableDisable(false);
		setBranchDetailsButtonEnableDisable(false);
		observable.nullifyPhoneList();
		setButtonEnableDisable();
		setPhoneButtonEnableDisable();
		observable.resetForm();
		isFilled = false;
		observable.setStatus();
		setModified(false);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(true);
                btnException.setEnabled(true);
		}//GEN-LAST:event_btnCancelActionPerformed
		private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
		// Add your handling code here:
		observable.resetForm();
		observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
		popUp(DELETE);
                btnAuthorize.setEnabled(false);
                btnReject.setEnabled(false);
                btnException.setEnabled(false);
		}//GEN-LAST:event_btnDeleteActionPerformed
		private void btnPhoneDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhoneDeleteActionPerformed
		// Add your handling code here:
		observable.deletePhoneDetails();
		ClientUtil.enableDisable(this.panContactNo, false);
		setPhoneButtonEnableDisableDefault();
		}//GEN-LAST:event_btnPhoneDeleteActionPerformed
		private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
		// Add your handling code here:
                setPhoneButtonEnableDisableSelect();
		observable.resetForm();
		observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
		popUp(EDIT);
                btnAuthorize.setEnabled(false);
                btnReject.setEnabled(false);
                btnException.setEnabled(false);

		}//GEN-LAST:event_btnEditActionPerformed
		private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
		// Add your handling code here:
		branchCode = new HashMap();
		branchCode.put("IntValue", "1");
		// To set the combobox values in OB
		updateOBComboBoxes();
		// To validate the Working time
		String timeValidated = observable.checkForTime();
		// To set the combobox values in UI
		updateComboBoxes();
		final StringBuffer mandatoryMessage =  new StringBuffer();
		mandatoryMessage.append( checkMandatory(panAddress) );
		// If atleast one phone details is entered then Area code must be entered
		if (tblPhoneNos.getRowCount() > 0){
		mandatoryMessage.append( checkMandatory(panContactCode) );
		}
		// Check Balance limit is Selected
		if (rdoBalanceLimitBP_Yes.isSelected()){
		mandatoryMessage.append( checkMandatory(panBranchParameter) );
		}
		mandatoryMessage.append( checkMandatory(panBranchCoufig) );
		if(!timeValidated.equals("Yes")){
		// The validated time is wrong append error message
		mandatoryMessage.append(timeValidated);
		}
		if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
		branchCode = (HashMap)observable.checkingDuplicationForBranchCode(txtBranchCode.getText().toUpperCase());
		if(CommonUtil.convertObjToInt(branchCode.get("IntValue")) != 1){
		mandatoryMessage.append(CommonUtil.convertObjToStr(branchCode.get("WarningMessage")));
		}
		}

		//To check whether all the mandatory fields of Phone details have been entered.
		//If not entered properly display alert message, else proceed
		if( mandatoryMessage.length() > 0 ){
		displayAlert(mandatoryMessage.toString());
		} else if (observable.validateContactDetails(
		(String)cboContactType.getSelectedItem(),txtContactNo.getText(), tblPhoneNos.getSelectedRow())) {
		displayAlert(resourceBundle.getString("ValidateContactTypeAndNo"));
		} else if(Integer.parseInt((String)branchCode.get("IntValue")) == NO_DUPLICATION){
		updateUIFields();
		observable.doAction();
		enableDisable(false);
		setBranchDetailsButtonEnableDisable(false);
		setButtonEnableDisable();
		setPhoneButtonEnableDisable();
		if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                    super.removeEditLock(txtBranchCode.getText());
                    setModified(false);
		}
		observable.setResultStatus();
		}
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(true);
                btnException.setEnabled(true);
		}//GEN-LAST:event_btnSaveActionPerformed
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        enableDisable(true);
        setBranchDetailsButtonEnableDisable(true);
        setButtonEnableDisable();
        setPhoneButtonEnableDisableDefault();
        setBranchMgrIDandRODisable();
        ClientUtil.enableDisable(this.panContactNo, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        setDefaultValueWhenNewBtnPressed();
        observable.setStatus();
        setModified(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed
    private void setDefaultValueWhenNewBtnPressed(){
        observable.setRdoBalanceLimitBP_Yes(true);
        addRadioButtons();
        removeRadioButtons();
        rdoBalanceLimitBP_Yes.setSelected(true);
    }
    private void tblPhoneNosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPhoneNosMousePressed
        // Add your handling code here:
        phoneMode=EDIT;
        observable.populatePhoneDetails(tblPhoneNos.getSelectedRow());
        //To enable the controls for NEW & EDIT options, else by default it will be disabled
        //        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ){
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(this.panContactNo, true);
            cboContactType.setEnabled(false);
            setPhoneButtonEnableDisableSelect();
        }
    }//GEN-LAST:event_tblPhoneNosMousePressed
            private void btnContactNoAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContactNoAddActionPerformed
                // Add your handling code here:
                final String mandatoryMessage =  checkMandatory(panContactNo);
                //To check whether all the mandatory fields of Phone details have been entered.
                //If not entered properly display alert message, else proceed
                if( mandatoryMessage.length() > 0 ){
                    displayAlert(mandatoryMessage);
                } else if (observable.validateContactDetails(
                (String)cboContactType.getSelectedItem(),
                txtContactNo.getText(),
                phoneMode == NEW ? -1 : tblPhoneNos.getSelectedRow())) {
                    displayAlert(resourceBundle.getString("ValidateContactTypeAndNo"));
                } else{
                    updateUIFields();
                    observable.addPhoneList();
                    setPhoneButtonEnableDisableDefault();
                    ClientUtil.enableDisable(this.panContactNo, false);
                }
    }//GEN-LAST:event_btnContactNoAddActionPerformed
            
    private void btnPhoneNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhoneNewActionPerformed
        observable.resetPhoneDetails();
        observable.newPhoneID();
        phoneMode = NEW;
        // Add your handling code here:
        ClientUtil.enableDisable(this.panContactNo, true);
        setPhoneButtonEnableDisableNew();
        updatePhoneDetails();
    }//GEN-LAST:event_btnPhoneNewActionPerformed
    
    private void updatePhoneDetails(){
        cboContactType.setSelectedItem(observable.getCboContactType());
        txtContactNo.setText(observable.getTxtContactNo());
    }
    private void enableDisableOnlyAddressFields(boolean val){
        txtStreet.setEnabled(val);
        txtStreet.setEditable(val);
        txtArea.setEnabled(val);
        txtArea.setEditable(val);
        txtPinCode.setEnabled(val);
        txtPinCode.setEditable(val);
        cboCity.setEnabled(val);
        cboCountry.setEnabled(val);
        cboState.setEnabled(val);
    }
    private void enableDisableAddressTab(boolean val){
        txtBranchCode.setEnabled(val);
        txtBranchShortName.setEnabled(val);
        txtBranchName.setEnabled(val);
        txtStreet.setEnabled(val);
        txtArea.setEnabled(val);
        cboCity.setEnabled(val);
        cboCountry.setEnabled(val);
        cboState.setEnabled(val);
        cboBranchGLGroup.setEnabled(val);
        cboBranchGroup.setEnabled(val);
    }
    /** To check mandatoryness of a specific component, mostly a Panel */
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    /** To display the given messsage as an alert in a Dialog box */
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /** To properly set the column widths of Phone list table */
    private void initTables(){
        final ArrayList identifiers = observable.getTblPhoneList().getIdentifiers();
        final javax.swing.table.TableColumnModel tcm = tblPhoneNos.getColumnModel();
        
        for( int i = identifiers.size(),j=0; i > 0; i--,j++){
            tcm.getColumn(j).setPreferredWidth( ((String)identifiers.get(j)).length()*80 );
            tcm.getColumn(j).setWidth( ((String)identifiers.get(j)).length()*80 );
        }
    }
    
    /** To populate Comboboxes with proper data */
    private void initComponentData() {
        rdoAuthTime.setVisible(false);
        rdoTransTime.setVisible(false);
        cboCountry.setModel(observable.getCbmCountry());
        cboCity.setModel(observable.getCbmCity());
        cboState.setModel(observable.getCbmState());
        cboContactType.setModel(observable.getCbmContactType());
        cboFromHrs.setModel(observable.getCbmWorkingHrsFrom());
        cboFromMin.setModel(observable.getCbmWorkingMinFrom());
        cboToHrs.setModel(observable.getCbmWorkingHrsTo());
        cboToMin.setModel(observable.getCbmWorkingMinTo());
        cboBranchGroup.setModel(observable.getCbmBranchGroup());
        cboBranchGLGroup.setModel(observable.getCbmBranchGLGroup());
        tblPhoneNos.setModel(observable.getTblPhoneList());
        
    }
    
    /** To display a popUp window for viewing existing data as well as to select new
     * Branch for entry
     */
    private void popUp(int field){
        if( field == RO || field == ID ) {
            lblStatus.setText(ClientConstants.ACTION_STATUS[observable.getActionType()]);
        }else {
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
        }
        viewType = field;
        final HashMap testMap = new HashMap();
        if(field==EDIT || field==DELETE || field==VIEW){
            ArrayList lst = new ArrayList();
            lst.add("BRANCH CODE");
            testMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            testMap.put("MAPNAME", "getSelectBranchList");
        }else if (field == RO){
            testMap.put(CommonConstants.MAP_NAME, "getRegionalOffice");
        }else if (field == ID){
            testMap.put(CommonConstants.MAP_NAME, "getBranchManagerID");
        }
        /*
        //To display customer info based on the selected ProductID
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ){
            testMap = accountViewMap();
        }
        //To display the existing accounts which are set to freezed
        else if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            testMap = LienEditMap();
        }*/
        
        new com.see.truetransact.ui.common.viewall.ViewAll(this, testMap).show();
    }
    
    /** Called by the Popup window created thru popUp method */
    public void fillData(Object obj) {
        final HashMap hash = (HashMap) obj;
        final String regionalOffice = CommonUtil.convertObjToStr(hash.get("BRANCH CODE"));
        final String branchManagerID = CommonUtil.convertObjToStr(hash.get("BRANCH MANAGER ID"));
        if (viewType != -1) {
            //To populate the fields with data for the selected Branch
            if (viewType==EDIT || viewType==DELETE || viewType == AUTHORIZE || viewType==VIEW) {
                isFilled = true;
                fillDataEdit(hash);
                // Displays Branch Manager Name
                observable.setBranchManagerName(observable.getTxtBranchManagerID());
                lblDisplayBranMgrName.setText(observable.getBranchManagerName());
                setBranchMgrIDandRODisable();
                // Displays Regional Office Name
                observable.setRegionalOfficerName(observable.getTxtRegionalOffice());
                lblDisplayRegOfficeName.setText(observable.getRegionalOfficerName());
                
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                    setBranchDetailsButtonEnableDisable(true);
                }else{
                    setBranchDetailsButtonEnableDisable(false);
                }
                setButtonEnableDisable();
                observable.setStatus();
                checkForEdit();
            }else if(viewType == RO){
                txtRegionalOffice.setText(regionalOffice);
                lblDisplayRegOfficeName.setText((String)hash.get("BRANCH NAME"));
            }else if (viewType == ID){
                txtBranchManagerID.setText(branchManagerID);
                lblDisplayBranMgrName.setText((String)hash.get("NAME"));
            }
            if(viewType==AUTHORIZE){
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
        }
        setModified(true);
    }
    
    /** To fillData for existing entry */
    private void fillDataEdit(HashMap hash){
        hash.put("BRANCHCODE", hash.get("BRANCH CODE"));
        observable.getBranchData(hash);
    }
    
    /** To perform necessary action, if the activity is EDIT */
    private void checkForEdit(){
        //To enable disable controls for EDIT operation
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ){
            enableDisable(true);
//            ClientUtil.enableDisable(this.panRightAddress, false);
//            enableDisableAddressTab(false);
//            enableDisableOnlyAddressFields(true);
//            //            setPhoneButtonEnableDisableDefault();
            this.txtBranchCode.setEnabled(false);
//            ClientUtil.enableDisable(this.panContactDetails, false);
//            ClientUtil.enableDisable(this.panBranchParameter, false);
        }
    }
    
   /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnBranchManagerID.setName("btnBranchManagerID");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnContactNoAdd.setName("btnContactNoAdd");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnNew.setName("btnNew");
        btnPhoneDelete.setName("btnPhoneDelete");
        btnPhoneNew.setName("btnPhoneNew");
        btnPrint.setName("btnPrint");
        btnRegionalOffice.setName("btnRegionalOffice");
        btnSave.setName("btnSave");
        cboCity.setName("cboCity");
        cboContactType.setName("cboContactType");
        cboCountry.setName("cboCountry");
        cboBranchGroup.setName("cboBranchGroup");
        cboFromHrs.setName("cboFromHrs");
        cboFromMin.setName("cboFromMin");
        cboState.setName("cboState");
        cboToHrs.setName("cboToHrs");
        cboToMin.setName("cboToMin");
        cboBranchGLGroup.setName("cboBranchGLGroup");
        lblArea.setName("lblArea");
        lblBranchManagerName.setName("lblBranchManagerName");
        lblAreaCode.setName("lblAreaCode");
        lblBranchGLGroup.setName("lblBranchGLGroup");
        lblAvgCashStockBP.setName("lblAvgCashStockBP");
        lblBSRCode.setName("lblBSRCode");
        lblBalanceLimitBP.setName("lblBalanceLimitBP");
        lblBranchCode.setName("lblBranchCode");
        lblBranchManagerID.setName("lblBranchManagerID");
        lblBranchName.setName("lblBranchName");
        lblBranchShortName.setName("lblBranchShortName");
        lblCity.setName("lblCity");
        lblContactNo.setName("lblContactNo");
        lblContactType.setName("lblContactType");
        lblCountry.setName("lblCountry");
        lblDBNameDataBC.setName("lblDBNameDataBC");
        lblDriverDataBC.setName("lblDriverDataBC");
        lblIPAppBC.setName("lblIPAppBC");
        lblIPDataBC.setName("lblIPDataBC");
        lblMICRCode.setName("lblMICRCode");
        lblMaxCashStockBP.setName("lblMaxCashStockBP");
        lblMsg.setName("lblMsg");
        lblOpeningDate.setName("lblOpeningDate");
        lblPasswordDataBC.setName("lblPasswordDataBC");
        lblPinCode.setName("lblPinCode");
        lblPortAppBC.setName("lblPortAppBC");
        lblPortDataBC.setName("lblPortDataBC");
        lblRegionalOffice.setName("lblRegionalOffice");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblState.setName("lblState");
        lblStatus.setName("lblStatus");
        lblStreet.setName("lblStreet");
        lblURLDataBC.setName("lblURLDataBC");
        lblUserIDDataBC.setName("lblUserIDDataBC");
        lblWorkingFrom.setName("lblWorkingFrom");
        lblWorkingHrs.setName("lblWorkingHrs");
        lblWorkingMin.setName("lblWorkingMin");
        lblWorkingTo.setName("lblWorkingTo");
        mbrCustomer.setName("mbrCustomer");
        panAddress.setName("panAddress");
        panAppBC.setName("panAppBC");
        panBranchCoufig.setName("panBranchCoufig");
        panBranchDetails.setName("panBranchDetails");
        panBranchManagerID.setName("panBranchManagerID");
        panBranchParameter.setName("panBranchParameter");
        panContactCode.setName("panContactCode");
        panContactDetails.setName("panContactDetails");
        panContactList.setName("panContactList");
        panContactNo.setName("panContactNo");
        panDataBC.setName("panDataBC");
        panLeftAddress.setName("panLeftAddress");
        panPhoneSave.setName("panPhoneSave");
        panRegionalOffice.setName("panRegionalOffice");
        panRightAddress.setName("panRightAddress");
        panStatus.setName("panStatus");
        panWorkingTime.setName("panWorkingTime");
        rdoBalanceLimitBP_No.setName("rdoBalanceLimitBP_No");
        rdoBalanceLimitBP_Yes.setName("rdoBalanceLimitBP_Yes");
        scrPhoneList.setName("scrPhoneList");
        sptAddress.setName("sptAddress");
        sptContact.setName("sptContact");
        tabBranch.setName("tabBranch");
        tblPhoneNos.setName("tblPhoneNos");
        tdtOpeningDate.setName("tdtOpeningDate");
        txtArea.setName("txtArea");
        txtAreaCode.setName("txtAreaCode");
        txtAvgCashStockBP.setName("txtAvgCashStockBP");
        txtBSRCode.setName("txtBSRCode");
        txtBranchCode.setName("txtBranchCode");
        txtBranchManagerID.setName("txtBranchManagerID");
        txtBranchName.setName("txtBranchName");
        txtBranchShortName.setName("txtBranchShortName");
        txtContactNo.setName("txtContactNo");
        txtDBNameDataBC.setName("txtDBNameDataBC");
        txtDriverDataBC.setName("txtDriverDataBC");
        txtIPAppBC.setName("txtIPAppBC");
        txtIPDataBC.setName("txtIPDataBC");
        txtMICRCode.setName("txtMICRCode");
        txtMaxCashStockBP.setName("txtMaxCashStockBP");
        txtPasswordDataBC.setName("txtPasswordDataBC");
        txtPinCode.setName("txtPinCode");
        txtPortAppBC.setName("txtPortAppBC");
        txtPortDataBC.setName("txtPortDataBC");
        txtRegionalOffice.setName("txtRegionalOffice");
        txtStreet.setName("txtStreet");
        txtURLDataBC.setName("txtURLDataBC");
        txtUserIDDataBC.setName("txtUserIDDataBC");
    }
    
    
    
   /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblBranchManagerName.setText(resourceBundle.getString("lblBranchManagerName"));
        lblMaxCashStockBP.setText(resourceBundle.getString("lblMaxCashStockBP"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        ((javax.swing.border.TitledBorder)panContactDetails.getBorder()).setTitle(resourceBundle.getString("panContactDetails"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblAreaCode.setText(resourceBundle.getString("lblAreaCode"));
        lblBranchGLGroup.setText(resourceBundle.getString("lblBranchGLGroup"));
        lblWorkingFrom.setText(resourceBundle.getString("lblWorkingFrom"));
        lblCountry.setText(resourceBundle.getString("lblCountry"));
        lblDBNameDataBC.setText(resourceBundle.getString("lblDBNameDataBC"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblMICRCode.setText(resourceBundle.getString("lblMICRCode"));
        lblDriverDataBC.setText(resourceBundle.getString("lblDriverDataBC"));
        lblWorkingMin.setText(resourceBundle.getString("lblWorkingMin"));
        lblPortAppBC.setText(resourceBundle.getString("lblPortAppBC"));
        lblIPAppBC.setText(resourceBundle.getString("lblIPAppBC"));
        lblContactType.setText(resourceBundle.getString("lblContactType"));
        btnBranchManagerID.setText(resourceBundle.getString("btnBranchManagerID"));
        lblBranchShortName.setText(resourceBundle.getString("lblBranchShortName"));
        ((javax.swing.border.TitledBorder)panContactList.getBorder()).setTitle(resourceBundle.getString("panContactList"));
        lblAvgCashStockBP.setText(resourceBundle.getString("lblAvgCashStockBP"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblIPDataBC.setText(resourceBundle.getString("lblIPDataBC"));
        ((javax.swing.border.TitledBorder)panDataBC.getBorder()).setTitle(resourceBundle.getString("panDataBC"));
        lblWorkingTo.setText(resourceBundle.getString("lblWorkingTo"));
        lblStreet.setText(resourceBundle.getString("lblStreet"));
        lblCity.setText(resourceBundle.getString("lblCity"));
        lblBalanceLimitBP.setText(resourceBundle.getString("lblBalanceLimitBP"));
        lblBranchCode.setText(resourceBundle.getString("lblBranchCode"));
        lblBranchGroup.setText(resourceBundle.getString("lblBranchGroup"));
        lblWorkingHrs.setText(resourceBundle.getString("lblWorkingHrs"));
        rdoBalanceLimitBP_Yes.setText(resourceBundle.getString("rdoBalanceLimitBP_Yes"));
        rdoBalanceLimitBP_No.setText(resourceBundle.getString("rdoBalanceLimitBP_No"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnPhoneDelete.setText(resourceBundle.getString("btnPhoneDelete"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblContactNo.setText(resourceBundle.getString("lblContactNo"));
        lblPasswordDataBC.setText(resourceBundle.getString("lblPasswordDataBC"));
        lblArea.setText(resourceBundle.getString("lblArea"));
        lblPinCode.setText(resourceBundle.getString("lblPinCode"));
        lblBranchManagerID.setText(resourceBundle.getString("lblBranchManagerID"));
        btnContactNoAdd.setText(resourceBundle.getString("btnContactNoAdd"));
        ((javax.swing.border.TitledBorder)panWorkingTime.getBorder()).setTitle(resourceBundle.getString("panWorkingTime"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblOpeningDate.setText(resourceBundle.getString("lblOpeningDate"));
        lblRegionalOffice.setText(resourceBundle.getString("lblRegionalOffice"));
        lblURLDataBC.setText(resourceBundle.getString("lblURLDataBC"));
        lblPortDataBC.setText(resourceBundle.getString("lblPortDataBC"));
        ((javax.swing.border.TitledBorder)panAddress.getBorder()).setTitle(resourceBundle.getString("panAddress"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnRegionalOffice.setText(resourceBundle.getString("btnRegionalOffice"));
        ((javax.swing.border.TitledBorder)panAppBC.getBorder()).setTitle(resourceBundle.getString("panAppBC"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPhoneNew.setText(resourceBundle.getString("btnPhoneNew"));
        lblState.setText(resourceBundle.getString("lblState"));
        lblUserIDDataBC.setText(resourceBundle.getString("lblUserIDDataBC"));
        lblBranchName.setText(resourceBundle.getString("lblBranchName"));
        lblBSRCode.setText(resourceBundle.getString("lblBSRCode"));
    }
    
    
   /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        txtStreet.setText(observable.getTxtStreet());
        txtArea.setText(observable.getTxtArea());
        cboCity.setSelectedItem(observable.getCboCity());
        cboState.setSelectedItem(observable.getCboState());
        cboCountry.setSelectedItem(observable.getCboCountry());
        txtPinCode.setText(observable.getTxtPinCode());
        txtBranchCode.setText(observable.getTxtBranchCode());
        txtBranchName.setText(observable.getTxtBranchName());
        txtBranchShortName.setText(observable.getTxtBranchShortName());
        tdtOpeningDate.setDateValue(observable.getTdtOpeningDate());
        txtBranchManagerID.setText(observable.getTxtBranchManagerID());
        txtRegionalOffice.setText(observable.getTxtRegionalOffice());
        txtMICRCode.setText(observable.getTxtMICRCode());
        txtBSRCode.setText(observable.getTxtBSRCode());
        cboFromHrs.setSelectedItem(observable.getCboFromHrs());
        cboFromMin.setSelectedItem(observable.getCboFromMin());
        cboToHrs.setSelectedItem(observable.getCboToHrs());
        cboToMin.setSelectedItem(observable.getCboToMin());
        cboContactType.setSelectedItem(observable.getCboContactType());
        cboBranchGroup.setSelectedItem(observable.getCboBranchGroup());
        cboBranchGLGroup.setSelectedItem(observable.getCboBranchGLGroup());
        txtContactNo.setText(observable.getTxtContactNo());
        txtAreaCode.setText(observable.getTxtAreaCode());
        txtMaxCashStockBP.setText(observable.getTxtMaxCashStockBP());
        txtAvgCashStockBP.setText(observable.getTxtAvgCashStockBP());
        rdoBalanceLimitBP_Yes.setSelected(observable.getRdoBalanceLimitBP_Yes());
        rdoBalanceLimitBP_No.setSelected(observable.getRdoBalanceLimitBP_No());
        txtIPAppBC.setText(observable.getTxtIPAppBC());
        txtPortAppBC.setText(observable.getTxtPortAppBC());
        txtIPDataBC.setText(observable.getTxtIPDataBC());
        txtPortDataBC.setText(observable.getTxtPortDataBC());
        txtDBNameDataBC.setText(observable.getTxtDBNameDataBC());
        txtDriverDataBC.setText(observable.getTxtDriverDataBC());
        txtURLDataBC.setText(observable.getTxtURLDataBC());
        txtUserIDDataBC.setText(observable.getTxtUserIDDataBC());
        txtPasswordDataBC.setText(observable.getTxtPasswordDataBC());
        lblStatus.setText(observable.getLblStatus());
        lblDisplayBranMgrName.setText(observable.getBranchManagerName());
        lblDisplayRegOfficeName.setText(observable.getRegionalOfficerName());
        addRadioButtons();
    }
    
    /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateUIFields() {
        observable.setTxtStreet(txtStreet.getText());
        observable.setTxtArea(txtArea.getText());
        observable.setCboCity((String) cboCity.getSelectedItem());
        observable.setCboState((String) cboState.getSelectedItem());
        observable.setCboCountry((String) cboCountry.getSelectedItem());
        observable.setTxtPinCode(txtPinCode.getText());
        observable.setTxtBranchCode(txtBranchCode.getText().toUpperCase());
        observable.setTxtBranchName(txtBranchName.getText());
        observable.setTxtBranchShortName(txtBranchShortName.getText());
        observable.setTdtOpeningDate(tdtOpeningDate.getDateValue());
        observable.setTxtBranchManagerID(txtBranchManagerID.getText());
        observable.setTxtRegionalOffice(txtRegionalOffice.getText());
        observable.setTxtMICRCode(txtMICRCode.getText());
        observable.setTxtBSRCode(txtBSRCode.getText());
        observable.setCboFromHrs((String) cboFromHrs.getSelectedItem());
        observable.setCboFromMin((String) cboFromMin.getSelectedItem());
        observable.setCboToHrs((String) cboToHrs.getSelectedItem());
        observable.setCboToMin((String) cboToMin.getSelectedItem());
        observable.setCboBranchGroup((String) cboBranchGroup.getSelectedItem());
        observable.setCboBranchGLGroup((String) cboBranchGLGroup.getSelectedItem());
        observable.setCboContactType((String) cboContactType.getSelectedItem());
        observable.setTxtContactNo(txtContactNo.getText());
        observable.setTxtAreaCode(txtAreaCode.getText());
        observable.setTxtMaxCashStockBP(txtMaxCashStockBP.getText());
        observable.setTxtAvgCashStockBP(txtAvgCashStockBP.getText());
        observable.setRdoBalanceLimitBP_Yes(rdoBalanceLimitBP_Yes.isSelected());
        observable.setRdoBalanceLimitBP_No(rdoBalanceLimitBP_No.isSelected());
        observable.setTxtIPAppBC(txtIPAppBC.getText());
        observable.setTxtPortAppBC(txtPortAppBC.getText());
        observable.setTxtIPDataBC(txtIPDataBC.getText());
        observable.setTxtPortDataBC(txtPortDataBC.getText());
        observable.setTxtDBNameDataBC(txtDBNameDataBC.getText());
        observable.setTxtDriverDataBC(txtDriverDataBC.getText());
        observable.setTxtURLDataBC(txtURLDataBC.getText());
        observable.setTxtUserIDDataBC(txtUserIDDataBC.getText());
        observable.setTxtPasswordDataBC(txtPasswordDataBC.getText());
        if(chkShift.isSelected())
        {
            observable.setChkShift("Y");
        }
        else
        {
            observable.setChkShift("N");
        }
        observable.setRdoAuthTime(rdoAuthTime.isSelected());
        observable.setRdoTransTime(rdoTransTime.isSelected());
        observable.setScreen(getScreen());
        observable.setModule(getModule());
    }
    /* To remove the radio buttons */
    private void removeRadioButtons(){
        rdoBalanceLimitBP.remove(rdoBalanceLimitBP_Yes);
        rdoBalanceLimitBP.remove(rdoBalanceLimitBP_No);
        rdoShiftGroup.remove(rdoTransTime);
        rdoShiftGroup.remove(rdoAuthTime);
    }
    
    /* To add the radio buttons */
    private void addRadioButtons(){
        rdoBalanceLimitBP = new CButtonGroup();
        rdoBalanceLimitBP.add(rdoBalanceLimitBP_Yes);
        rdoBalanceLimitBP.add(rdoBalanceLimitBP_No);
        rdoShiftGroup = new CButtonGroup();
        rdoShiftGroup.add(rdoTransTime);
        rdoShiftGroup.add(rdoAuthTime);
    }
    
        /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        BranchManagementMRB objMandatoryRB = new BranchManagementMRB();
        txtStreet.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStreet"));
        txtArea.setHelpMessage(lblMsg, objMandatoryRB.getString("txtArea"));
        cboCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCity"));
        cboState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboState"));
        cboCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCountry"));
        txtPinCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPinCode"));
        txtBranchCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchCode"));
        txtBranchName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchName"));
        txtBranchShortName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchShortName"));
        tdtOpeningDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtOpeningDate"));
        txtBranchManagerID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchManagerID"));
        txtRegionalOffice.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRegionalOffice"));
        txtMICRCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMICRCode"));
        txtBSRCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBSRCode"));
        cboFromHrs.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFromHrs"));
        cboFromMin.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFromMin"));
        cboBranchGroup.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBranchGroup"));
        cboBranchGLGroup.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBranchGLGroup"));
        cboToHrs.setHelpMessage(lblMsg, objMandatoryRB.getString("cboToHrs"));
        cboToMin.setHelpMessage(lblMsg, objMandatoryRB.getString("cboToMin"));
        cboContactType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboContactType"));
        txtContactNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtContactNo"));
        txtAreaCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAreaCode"));
        txtMaxCashStockBP.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxCashStockBP"));
        txtAvgCashStockBP.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAvgCashStockBP"));
        rdoBalanceLimitBP_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoBalanceLimitBP_Yes"));
        txtIPAppBC.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIPAppBC"));
        txtPortAppBC.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPortAppBC"));
        txtIPDataBC.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIPDataBC"));
        txtPortDataBC.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPortDataBC"));
        txtDBNameDataBC.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDBNameDataBC"));
        txtDriverDataBC.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDriverDataBC"));
        txtURLDataBC.setHelpMessage(lblMsg, objMandatoryRB.getString("txtURLDataBC"));
        txtUserIDDataBC.setHelpMessage(lblMsg, objMandatoryRB.getString("txtUserIDDataBC"));
        txtPasswordDataBC.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPasswordDataBC"));
    }
    
    
    
    
    
    
    private void enableDisable(boolean yesno){
        ClientUtil.enableDisable(this, yesno);
    }
    
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
    
    private void setPhoneButtonEnableDisable(){
        this.btnPhoneNew.setEnabled(false);
        this.btnContactNoAdd.setEnabled(false);
        this.btnPhoneDelete.setEnabled(false);
    }
    
    private void setPhoneButtonEnableDisableDefault(){
        this.btnPhoneNew.setEnabled(true);
        this.btnContactNoAdd.setEnabled(false);
        this.btnPhoneDelete.setEnabled(false);
    }
    
    private void setPhoneButtonEnableDisableNew(){
        this.btnPhoneNew.setEnabled(false);
        this.btnContactNoAdd.setEnabled(true);
        this.btnPhoneDelete.setEnabled(false);
    }
    
    private void setPhoneButtonEnableDisableSelect(){
        this.btnPhoneNew.setEnabled(true);
        this.btnContactNoAdd.setEnabled(true);
        this.btnPhoneDelete.setEnabled(true);
    }
    private void setBranchMgrIDandRODisable(){
        this.txtRegionalOffice.setEditable(false);
        this.txtBranchManagerID.setEditable(false);
    }
    private void setBranchDetailsButtonEnableDisable(boolean flag){
        btnRegionalOffice.setEnabled(flag);
        btnBranchManagerID.setEnabled(flag);
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBranchManagerID;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnContactNoAdd;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPhoneDelete;
    private com.see.truetransact.uicomponent.CButton btnPhoneNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnRegionalOffice;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboBranchGLGroup;
    private com.see.truetransact.uicomponent.CComboBox cboBranchGroup;
    private com.see.truetransact.uicomponent.CComboBox cboCity;
    private com.see.truetransact.uicomponent.CComboBox cboContactType;
    private com.see.truetransact.uicomponent.CComboBox cboCountry;
    private com.see.truetransact.uicomponent.CComboBox cboFromHrs;
    private com.see.truetransact.uicomponent.CComboBox cboFromMin;
    private com.see.truetransact.uicomponent.CComboBox cboState;
    private com.see.truetransact.uicomponent.CComboBox cboToHrs;
    private com.see.truetransact.uicomponent.CComboBox cboToMin;
    private com.see.truetransact.uicomponent.CCheckBox chkShift;
    private com.see.truetransact.uicomponent.CLabel lblArea;
    private com.see.truetransact.uicomponent.CLabel lblAreaCode;
    private com.see.truetransact.uicomponent.CLabel lblAvgCashStockBP;
    private com.see.truetransact.uicomponent.CLabel lblBSRCode;
    private com.see.truetransact.uicomponent.CLabel lblBalanceLimitBP;
    private com.see.truetransact.uicomponent.CLabel lblBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblBranchGLGroup;
    private com.see.truetransact.uicomponent.CLabel lblBranchGroup;
    private com.see.truetransact.uicomponent.CLabel lblBranchManagerID;
    private com.see.truetransact.uicomponent.CLabel lblBranchManagerName;
    private com.see.truetransact.uicomponent.CLabel lblBranchName;
    private com.see.truetransact.uicomponent.CLabel lblBranchShortName;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblContactNo;
    private com.see.truetransact.uicomponent.CLabel lblContactType;
    private com.see.truetransact.uicomponent.CLabel lblCountry;
    private com.see.truetransact.uicomponent.CLabel lblDBNameDataBC;
    private com.see.truetransact.uicomponent.CLabel lblDisplayBranMgrName;
    private com.see.truetransact.uicomponent.CLabel lblDisplayRegOfficeName;
    private com.see.truetransact.uicomponent.CLabel lblDriverDataBC;
    private com.see.truetransact.uicomponent.CLabel lblIPAppBC;
    private com.see.truetransact.uicomponent.CLabel lblIPDataBC;
    private com.see.truetransact.uicomponent.CLabel lblMICRCode;
    private com.see.truetransact.uicomponent.CLabel lblMaxCashStockBP;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOpeningDate;
    private com.see.truetransact.uicomponent.CLabel lblPasswordDataBC;
    private com.see.truetransact.uicomponent.CLabel lblPinCode;
    private com.see.truetransact.uicomponent.CLabel lblPortAppBC;
    private com.see.truetransact.uicomponent.CLabel lblPortDataBC;
    private com.see.truetransact.uicomponent.CLabel lblRegionalOffice;
    private com.see.truetransact.uicomponent.CLabel lblRegionalOfficerName;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStreet;
    private com.see.truetransact.uicomponent.CLabel lblURLDataBC;
    private com.see.truetransact.uicomponent.CLabel lblUserIDDataBC;
    private com.see.truetransact.uicomponent.CLabel lblWorkingFrom;
    private com.see.truetransact.uicomponent.CLabel lblWorkingHrs;
    private com.see.truetransact.uicomponent.CLabel lblWorkingMin;
    private com.see.truetransact.uicomponent.CLabel lblWorkingTo;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAddress;
    private com.see.truetransact.uicomponent.CPanel panAppBC;
    private com.see.truetransact.uicomponent.CPanel panBranchCoufig;
    private com.see.truetransact.uicomponent.CPanel panBranchDetails;
    private com.see.truetransact.uicomponent.CPanel panBranchManagerID;
    private com.see.truetransact.uicomponent.CPanel panBranchParameter;
    private com.see.truetransact.uicomponent.CPanel panContactCode;
    private com.see.truetransact.uicomponent.CPanel panContactDetails;
    private com.see.truetransact.uicomponent.CPanel panContactList;
    private com.see.truetransact.uicomponent.CPanel panContactNo;
    private com.see.truetransact.uicomponent.CPanel panDataBC;
    private com.see.truetransact.uicomponent.CPanel panLeftAddress;
    private com.see.truetransact.uicomponent.CPanel panPhoneSave;
    private com.see.truetransact.uicomponent.CPanel panRegionalOffice;
    private com.see.truetransact.uicomponent.CPanel panRightAddress;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panWorkingTime;
    private com.see.truetransact.uicomponent.CRadioButton rdoAuthTime;
    private com.see.truetransact.uicomponent.CButtonGroup rdoBalanceLimitBP;
    private com.see.truetransact.uicomponent.CRadioButton rdoBalanceLimitBP_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoBalanceLimitBP_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoShiftGroup;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransTime;
    private com.see.truetransact.uicomponent.CScrollPane scrPhoneList;
    private com.see.truetransact.uicomponent.CSeparator sptAddress;
    private com.see.truetransact.uicomponent.CSeparator sptContact;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CTabbedPane tabBranch;
    private com.see.truetransact.uicomponent.CTable tblPhoneNos;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtOpeningDate;
    private com.see.truetransact.uicomponent.CTextField txtArea;
    private com.see.truetransact.uicomponent.CTextField txtAreaCode;
    private com.see.truetransact.uicomponent.CTextField txtAvgCashStockBP;
    private com.see.truetransact.uicomponent.CTextField txtBSRCode;
    private com.see.truetransact.uicomponent.CTextField txtBranchCode;
    private com.see.truetransact.uicomponent.CTextField txtBranchManagerID;
    private com.see.truetransact.uicomponent.CTextField txtBranchName;
    private com.see.truetransact.uicomponent.CTextField txtBranchShortName;
    private com.see.truetransact.uicomponent.CTextField txtContactNo;
    private com.see.truetransact.uicomponent.CTextField txtDBNameDataBC;
    private com.see.truetransact.uicomponent.CTextField txtDriverDataBC;
    private com.see.truetransact.uicomponent.CTextField txtIPAppBC;
    private com.see.truetransact.uicomponent.CTextField txtIPDataBC;
    private com.see.truetransact.uicomponent.CTextField txtMICRCode;
    private com.see.truetransact.uicomponent.CTextField txtMaxCashStockBP;
    private com.see.truetransact.uicomponent.CTextField txtPasswordDataBC;
    private com.see.truetransact.uicomponent.CTextField txtPinCode;
    private com.see.truetransact.uicomponent.CTextField txtPortAppBC;
    private com.see.truetransact.uicomponent.CTextField txtPortDataBC;
    private com.see.truetransact.uicomponent.CTextField txtRegionalOffice;
    private com.see.truetransact.uicomponent.CTextField txtStreet;
    private com.see.truetransact.uicomponent.CTextField txtURLDataBC;
    private com.see.truetransact.uicomponent.CTextField txtUserIDDataBC;
    // End of variables declaration//GEN-END:variables
}
