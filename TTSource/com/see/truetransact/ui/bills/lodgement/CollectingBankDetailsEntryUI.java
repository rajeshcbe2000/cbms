/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * CollectingBankDetailsEntryUI.java
 *
 * Created on February 25, 2004, 11:55 AM
 */

package com.see.truetransact.ui.bills.lodgement;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
//import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizewf.AuthorizeWFUI;
//import com.see.truetransact.ui.common.authorizewf.AuthorizeWFCheckUI;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.denomination.DenominationUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.ui.common.powerofattorney.PowerOfAttorneyUI;
import com.see.truetransact.uicomponent.CButtonGroup;// To add and Remove the Radio Buttons...
//import com.see.truetransact.ui.common.viewall.TableDialogUI;
//import com.see.truetransact.ui.common.viewphotosign.ViewPhotoSignUI;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.deposit.TermDepositUI;
import com.see.truetransact.transferobject.product.operativeacct.OperativeAcctProductTO;
import com.see.truetransact.ui.transaction.common.TransCommonUI;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.ui.common.viewall.ViewLoansTransUI;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryUI;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
//import java.util.Observer;
import java.util.Observable;

import org.apache.log4j.Logger;
//import java.util.Date;

import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.bills.lodgement.LodgementBillsOB;
import com.see.truetransact.uicomponent.CLabel;
import java.util.Date;
/**
 *
 * @author  rahul, bala
 * @todoh Add other modules into the transaction
 *
 */
public class CollectingBankDetailsEntryUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {
    
    private HashMap mandatoryMap;
     private LodgementBillsOB observable;
    final int EDIT=0, DELETE=1, ACCNO=2, AUTHORIZE=8, ACCTHDID = 4, VIEW = 5, LINK_BATCH_TD=6, LINK_BATCH=7,TELLER_ENTRY_DETIALS=10;
    String viewType = ClientConstants.VIEW_TYPE_CANCEL;
    boolean isFilled = false;
    
    AuthorizedSignatoryUI authSignUI;
    PowerOfAttorneyUI poaUI;
    private TransDetailsUI transDetails = null;
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    private TransCommonUI transCommonUI = null;
    private boolean flag = false;
    private boolean flagDeposit = false;
    private boolean flagDepAuth = false;
    private boolean flagDepLink = false;
    private boolean flagDepositAuthorize = false;
    private boolean afterSaveCancel = false;
    private CTextField txtDepositAmount;
    private double intAmtDep =0.0;
    TermDepositUI termDepositUI;
    public String designation="";
    private String custStatus="";
    private String depBehavesLike = "";
    private HashMap intMap = new HashMap();
    private boolean reconcilebtnDisable = false;
    ArrayList termLoanDetails= null;
    private boolean termLoanDetailsFlag = false;
    HashMap termLoansDetailsMap = null;
    private String depPartialWithDrawalAllowed = "";
  
    List accountNoList=null;
    boolean fromAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    private String mandatoryMessage;
    private final String QOB = "Query Other Bank";
    private final String QOBB = "Query Other Bank Branch";
    private Date currDt = null;
    /** Creates new form CashTransaction */
    public CollectingBankDetailsEntryUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
         setObservable();
        initComponentData();
       
        initSetup();
        
        
    }
    
    private void initSetup(){
       
        btnBankCode.setEnabled(true);
        btnOtherBranchCode.setEnabled(true);
        btnNew.setEnabled(true);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        tdtRemittedDt.setEnabled(false);
        txtOtherBankCode.setEnabled(false);
      
        txtOtherBranchCode.setEnabled(false);
        txtOtherBranchCode.setEnabled(false);
        
        txtAcHead.setEnabled(false);
        tdtRemittedDt.setEnabled(false);
        btnEdit.setEnabled(true);
        observable.resetTable();
        
    }
    
    // Creates The Instance of InwardClearingOB
    private void setObservable() {
        
        try{
            
            observable = LodgementBillsOB.getInstance();
            observable.addObserver(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Authorize Button to be added...
    private void setFieldNames() {
        
    }
    
    private void internationalize() {
        
    }
    
    public void setMandatoryHashMap() {
        
        
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    // To Reset the Radio Buttons in the UI after any operation, We've to
    //1. Remove the Radio Buttons fron the Radio Groups...
    //2. Add the Radio Buttons Back in The Radio Groups...
    //a.) To Remove the Radio buttons...
    private void removeRadioButtons() {
        //---Account---
        
    }
    
    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        //---Account---
        rdoTransactionType = new CButtonGroup();
        
    }
    
    public void update(Observable observed, Object arg) {
        
        //        observable.setSelectedBranchID(getSelectedBranchID());
        tblOBDetails.setModel(observable.getTblOBDModel());
        
    }
    
    
    public void updateOBFields() {
        
    }
    
    public void setHelpMessage() {
        
        
        
    }
    
    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        //               cboProdId.setModel(observable.getCbmProdId());
        tblOBDetails.setModel(observable.getTblOBDModel());
    }
    
    //To set the length of the Text Fields and Numeric Validation...
    private void setMaxLenths() {
        
        
        
    }
    
    
    
    // To set The Value of the Buttons Depending on the Value or Condition...
    
    
    
    
    // To set The Value of the Buttons Depending on the Value or Condition...
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoTransactionType = new com.see.truetransact.uicomponent.CButtonGroup();
        panSalaryDeductionMapping = new com.see.truetransact.uicomponent.CPanel();
        panOtherBankDetails = new com.see.truetransact.uicomponent.CPanel();
        lblBankCode = new com.see.truetransact.uicomponent.CLabel();
        panBankCode = new com.see.truetransact.uicomponent.CPanel();
        btnBankCode = new com.see.truetransact.uicomponent.CButton();
        txtOtherBankCode = new com.see.truetransact.uicomponent.CTextField();
        lblBankName = new com.see.truetransact.uicomponent.CLabel();
        lblOtherBranchCode = new com.see.truetransact.uicomponent.CLabel();
        lblBankNameValue = new com.see.truetransact.uicomponent.CLabel();
        panOtherBranchCode = new com.see.truetransact.uicomponent.CPanel();
        btnOtherBranchCode = new com.see.truetransact.uicomponent.CButton();
        txtOtherBranchCode = new com.see.truetransact.uicomponent.CTextField();
        lblOtherBranchName = new com.see.truetransact.uicomponent.CLabel();
        lblOtherBranchNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        lblRemittedDt = new com.see.truetransact.uicomponent.CLabel();
        tdtRemittedDt = new com.see.truetransact.uicomponent.CDateField();
        txtAcHead = new com.see.truetransact.uicomponent.CTextField();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblOBDetails = new com.see.truetransact.uicomponent.CTable();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        tbrHead = new javax.swing.JToolBar();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace62 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace7 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace63 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace8 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace64 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace65 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(400, 400));
        setPreferredSize(new java.awt.Dimension(651, 450));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        panSalaryDeductionMapping.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSalaryDeductionMapping.setMinimumSize(new java.awt.Dimension(600, 360));
        panSalaryDeductionMapping.setPreferredSize(new java.awt.Dimension(605, 370));
        panSalaryDeductionMapping.setLayout(new java.awt.GridBagLayout());

        panOtherBankDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Other Bank Details"));
        panOtherBankDetails.setMinimumSize(new java.awt.Dimension(320, 180));
        panOtherBankDetails.setPreferredSize(new java.awt.Dimension(320, 180));
        panOtherBankDetails.setLayout(new java.awt.GridBagLayout());

        lblBankCode.setText("Other Bank Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOtherBankDetails.add(lblBankCode, gridBagConstraints);

        panBankCode.setLayout(new java.awt.GridBagLayout());

        btnBankCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBankCode.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBankCode.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBankCode.setEnabled(false);
        btnBankCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBankCodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panBankCode.add(btnBankCode, gridBagConstraints);

        txtOtherBankCode.setAllowAll(true);
        txtOtherBankCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 1);
        panBankCode.add(txtOtherBankCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherBankDetails.add(panBankCode, gridBagConstraints);

        lblBankName.setText("Other Bank Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOtherBankDetails.add(lblBankName, gridBagConstraints);

        lblOtherBranchCode.setText("Other Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOtherBankDetails.add(lblOtherBranchCode, gridBagConstraints);

        lblBankNameValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblBankNameValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblBankNameValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherBankDetails.add(lblBankNameValue, gridBagConstraints);

        panOtherBranchCode.setLayout(new java.awt.GridBagLayout());

        btnOtherBranchCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnOtherBranchCode.setMinimumSize(new java.awt.Dimension(21, 21));
        btnOtherBranchCode.setPreferredSize(new java.awt.Dimension(21, 21));
        btnOtherBranchCode.setEnabled(false);
        btnOtherBranchCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOtherBranchCodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panOtherBranchCode.add(btnOtherBranchCode, gridBagConstraints);

        txtOtherBranchCode.setAllowAll(true);
        txtOtherBranchCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 1);
        panOtherBranchCode.add(txtOtherBranchCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherBankDetails.add(panOtherBranchCode, gridBagConstraints);

        lblOtherBranchName.setText("Other Branch Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOtherBankDetails.add(lblOtherBranchName, gridBagConstraints);

        lblOtherBranchNameValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblOtherBranchNameValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherBankDetails.add(lblOtherBranchNameValue, gridBagConstraints);

        lblAccountHead.setText("A/c Head");
        lblAccountHead.setName("lblAccountHead");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 11, 4);
        panOtherBankDetails.add(lblAccountHead, gridBagConstraints);

        lblRemittedDt.setText("RemittedDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOtherBankDetails.add(lblRemittedDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherBankDetails.add(tdtRemittedDt, gridBagConstraints);

        txtAcHead.setAllowAll(true);
        txtAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 1);
        panOtherBankDetails.add(txtAcHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panSalaryDeductionMapping.add(panOtherBankDetails, gridBagConstraints);

        tblOBDetails.setModel(new javax.swing.table.DefaultTableModel(
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
        tblOBDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOBDetailsMouseClicked(evt);
            }
        });
        srcTable.setViewportView(tblOBDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panSalaryDeductionMapping.add(srcTable, gridBagConstraints);

        chkSelectAll.setText("Select All");
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 13);
        panSalaryDeductionMapping.add(chkSelectAll, gridBagConstraints);

        getContentPane().add(panSalaryDeductionMapping, java.awt.BorderLayout.CENTER);

        lblSpace5.setText("     ");
        tbrHead.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrHead.add(btnNew);

        lblSpace62.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace62.setText("     ");
        lblSpace62.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace62.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace62.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace62);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrHead.add(btnEdit);

        lblSpace7.setText("     ");
        tbrHead.add(lblSpace7);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrHead.add(btnSave);

        lblSpace63.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace63.setText("     ");
        lblSpace63.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace63.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace63.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace63);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrHead.add(btnCancel);

        lblSpace8.setText("     ");
        tbrHead.add(lblSpace8);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrHead.add(btnAuthorize);

        lblSpace64.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace64.setText("     ");
        lblSpace64.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace64.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace64.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace64);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrHead.add(btnReject);

        lblSpace6.setText("     ");
        tbrHead.add(lblSpace6);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrHead.add(btnPrint);

        lblSpace65.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace65.setText("     ");
        lblSpace65.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace65.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace65.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace65);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrHead.add(btnClose);

        getContentPane().add(tbrHead, java.awt.BorderLayout.NORTH);

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

        mnuProcess.setText("Process");

        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
        System.out.println("btnPrintActionPerformed ====== "+getScreenID());
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        HashMap hmap=new HashMap();
        viewType="EDIT";
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnNew.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        tdtRemittedDt.setEnabled(true);
        ArrayList list=(ArrayList)ClientUtil.executeQuery("getUnAuthorizedOBCRecords", hmap);
        observable.setTableDetails(list);
         for(int i=0;i <tblOBDetails.getRowCount();i++){
            tblOBDetails.setValueAt(new Boolean(true),i,0);
          
        }
        setOBCFieledValue(list);
        btnEdit.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
        if(viewType!="EDIT"){
         boolean flag;
        if(chkSelectAll.isSelected() == true)
            flag = true;
        else
            flag = false;
       
        for(int i=0;i <tblOBDetails.getRowCount();i++){
            tblOBDetails.setValueAt(new Boolean(flag),i,0);
          
        }
        }
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void tblOBDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOBDetailsMouseClicked
        // TODO add your handling code here:
        if(viewType.equals("EDIT")){
            ClientUtil.displayAlert("Deselection not allowed");
            return;
        }else{
         String st=CommonUtil.convertObjToStr(tblOBDetails.getValueAt(tblOBDetails.getSelectedRow(),0));
         if(st.equals("true")){
                tblOBDetails.setValueAt(new Boolean(false),tblOBDetails.getSelectedRow(),0);
            }else{
                tblOBDetails.setValueAt(new Boolean(true),tblOBDetails.getSelectedRow(),0);
            }
        }
    }//GEN-LAST:event_tblOBDetailsMouseClicked

    private void btnOtherBranchCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOtherBranchCodeActionPerformed
        // TODO add your handling code here:
         callView(QOBB);
    }//GEN-LAST:event_btnOtherBranchCodeActionPerformed

    private void btnBankCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBankCodeActionPerformed
        // TODO add your handling code here:
         callView(QOB);
    }//GEN-LAST:event_btnBankCodeActionPerformed
                        
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        //        btnVer.setVisible(btnAuthorize.isVisible());
    }//GEN-LAST:event_formInternalFrameOpened
        
     private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
         if(currField.equals(QOB) ){
            viewMap.put(CommonConstants.MAP_NAME, "getSelectOther_Bank");
        }else if(currField.equals(QOBB)){
            HashMap where = new HashMap();
            where.put("BANK_CODE", txtOtherBankCode.getText());
            viewMap.put(CommonConstants.MAP_NAME, "getSelectOther_Bank_Branch");
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
        }
          new ViewAll(this,viewMap).show();
     }
  
            
            
            
            
            
                    
	private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
            // Add your handling code here:
        ArrayList Alist=new ArrayList();
        HashMap hmap=new HashMap();
        viewType="REJECT";
        if( isFilled==false){
            ArrayList list=(ArrayList) ClientUtil.executeQuery("getUnAuthorizedOBCRecords",hmap);
            observable.setTableDetails(list);
            setOBCFieledValue(list);
            isFilled=true;
            return;
        }
        if(isFilled){
            for(int i=0;i<tblOBDetails.getRowCount();i++){
                String st= CommonUtil.convertObjToStr(tblOBDetails.getValueAt(i,0));
                
              
                    
                    HashMap haashmap=new HashMap();
                    haashmap.put("USER",TrueTransactMain.USER_ID);
                    haashmap.put("DATE",currDt.clone());
                    haashmap.put("AUTHORIZED","REJECTED");
                    haashmap.put("LODGEMENT_ID",CommonUtil.convertObjToStr(tblOBDetails.getValueAt(i,1)));
                    Alist.add(haashmap);
                    ClientUtil.execute("authorizeOBC", haashmap);
                    
               
            }
              btnCancelActionPerformed(null);
        }
     
    }//GEN-LAST:event_btnRejectActionPerformed
        
        
        
        
        
        public void authorize(HashMap map) {
          
        }
        
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        ArrayList Alist=new ArrayList();
        HashMap hmap=new HashMap();
        viewType="AUTHORIZE";
        if( isFilled==false){
            ArrayList list=(ArrayList) ClientUtil.executeQuery("getUnAuthorizedOBCRecords",hmap);
            observable.setTableDetails(list);
            setOBCFieledValue(list);
            isFilled=true;
            return;
        }
        if(isFilled){
            for(int i=0;i<tblOBDetails.getRowCount();i++){
                String st= CommonUtil.convertObjToStr(tblOBDetails.getValueAt(i,0));
                
              
                    
                    HashMap haashmap=new HashMap();
                    haashmap.put("USER",TrueTransactMain.USER_ID);
                    haashmap.put("DATE",currDt.clone());
                    haashmap.put("AUTHORIZED","AUTHORIZED");
                    haashmap.put("LODGEMENT_ID",CommonUtil.convertObjToStr(tblOBDetails.getValueAt(i,1)));
                    Alist.add(haashmap);
                    ClientUtil.execute("authorizeOBC", haashmap);
                    
                
            }
              btnCancelActionPerformed(null);
        }
      
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void setOBCFieledValue(ArrayList list){
        if(list!=null && list.size()>0){
            HashMap columnData =(HashMap) list.get(0);
            txtOtherBankCode.setText(CommonUtil.convertObjToStr(columnData.get("REC_OTHER_BANK")));
            txtOtherBranchCode.setText(CommonUtil.convertObjToStr(columnData.get("REC_OTHER_BRANCH")));
            fillBankBranchName(txtOtherBranchCode.getText(),txtOtherBankCode.getText(),lblOtherBranchNameValue,"getOtherBankBranchName");
            tdtRemittedDt.setDateValue(CommonUtil.convertObjToStr(columnData.get("REMITTED_DT")));
            fillBankBranchName(txtOtherBankCode.getText(),txtOtherBankCode.getText(),lblBankNameValue,"getBankName");
            if(txtOtherBankCode.getText().length()>0 && txtOtherBranchCode.getText().length()>0){
                HashMap accHeadMap = new HashMap();
                accHeadMap.put("BANK_CODE",txtOtherBankCode.getText());
                accHeadMap.put("BRANCH_CODE",txtOtherBranchCode.getText());
                List accHeadLst = ClientUtil.executeQuery("getSelectOtherBankBranchAccHead", accHeadMap);
                if(accHeadLst!=null &&accHeadLst.size()>0){
                    accHeadMap = (HashMap)accHeadLst.get(0);
                    txtAcHead.setText(CommonUtil.convertObjToStr(accHeadMap.get("ACCOUNT_HEAD")));
                }
            }
        }
        
    }
    
    
    
    
    
	private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
         
            cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
                
	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
            setModified(false);
            txtOtherBankCode.setText("");
            lblBankNameValue.setText("");
            txtOtherBranchCode.setText("");
            txtOtherBranchCode.setText("");
            lblOtherBranchNameValue.setText("");
            txtAcHead.setText("");
            tdtRemittedDt.setEnabled(false);
            txtOtherBankCode.setEnabled(false);
            btnSave.setEnabled(false);
            txtOtherBranchCode.setEnabled(false);
            txtOtherBranchCode.setEnabled(false);
            
            txtAcHead.setEnabled(false);
            tdtRemittedDt.setEnabled(false);
            observable.resetTable();
            btnNew.setEnabled(true);
            btnEdit.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
            isFilled=false;
            observable.resetTable();
            tdtRemittedDt.setDateValue("");
	}//GEN-LAST:event_btnCancelActionPerformed
        
	private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
            // Add your handling code here:
           setModified(false);
           for(int i=0;i<tblOBDetails.getRowCount();i++){
           String st= CommonUtil.convertObjToStr(tblOBDetails.getValueAt(i,0));
           ArrayList list=new ArrayList();
           if(st.equals("true")){
               HashMap hmap=new HashMap();
               hmap.put("LODGEMENT_N0",CommonUtil.convertObjToStr(tblOBDetails.getValueAt(i,1)));
               hmap.put("OBC_CODE",txtOtherBankCode.getText());
               hmap.put("OBC_NAME",lblBankNameValue.getText());
               hmap.put("BRANCH_CODE",txtOtherBranchCode.getText());
               hmap.put("BRANCH_NAME",lblOtherBranchNameValue.getText());
               hmap.put("HEAD",txtAcHead.getText());
               java.util.Date date=DateUtil.getDateMMDDYYYY(tdtRemittedDt.getDateValue());
               java.util.Date dd=(Date) currDt.clone();
               java.util.Date remtdt=(java.util.Date)dd.clone();
               remtdt.setDate(date.getDate());
               remtdt.setMonth(date.getMonth());
               remtdt.setYear(date.getYear());
               hmap.put("REMIT_DATE",remtdt);
               hmap.put("OBC_OTHER","Y");
               ClientUtil.execute("updateOBCDetails",hmap);
           }
           
            }
            btnCancelActionPerformed( null);
}//GEN-LAST:event_btnSaveActionPerformed
             
        // To display the All the Product Id's which r having status as
        // created or updated, in a table...
        
        private void setEnable(){
          
        }
        
        
        public void btnDepositCancel(){
         
        }
        
        
        
        
        
        
        // this method is called automatically from ViewAll...
        public void fillData(Object param) {
            HashMap hash = (HashMap) param;
             if(viewType.equals(QOB)){
                txtOtherBankCode.setText(CommonUtil.convertObjToStr(hash.get("BANK_CODE")));
                fillBankBranchName(txtOtherBankCode.getText(),txtOtherBankCode.getText(),lblBankNameValue,"getBankName");
            }else if (viewType.equals(QOBB)){
                txtOtherBranchCode.setText(CommonUtil.convertObjToStr(hash.get("BRANCH_CODE")));
               // txtSendingTo.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
//                txtDraweeBankCode.setText(CommonUtil.convertObjToStr(hash.get("BANK_CODE")));
//                txtDraweeBranchCode.setText(CommonUtil.convertObjToStr(hash.get("BRANCH_CODE")));
//                txtDraweeName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
//                txtDraweeAddress.setText(CommonUtil.convertObjToStr(hash.get("ADDRESS")));
//                cboDraweeCity.setSelectedItem(CommonUtil.convertObjToStr(hash.get("CITY")));
//                cboDraweeState.setSelectedItem(CommonUtil.convertObjToStr(hash.get("STATE")));
//                cboDraweeCountry.setSelectedItem(CommonUtil.convertObjToStr(hash.get("COUNTRY")));
//                txtDraweePinCode.setText(CommonUtil.convertObjToStr(hash.get("PINCODE")));
               // txtMICR.setText(CommonUtil.convertObjToStr(hash.get("MICR")));
                txtAcHead.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT_HEAD")));
                txtAcHead.setEnabled(false);
                fillBankBranchName(txtOtherBranchCode.getText(),txtOtherBankCode.getText(),lblOtherBranchNameValue,"getOtherBankBranchName");
            }
           
        }
        
         private void fillBankBranchName(String code, String bankCode,CLabel lblName, String mapName){
        if(code.length() != 0){
            HashMap map = new HashMap();
            map.put(CommonConstants.MAP_NAME, mapName);
            HashMap where = new HashMap();
            where.put("BRANCH_CODE", code);
            where.put("BANK_CODE", bankCode);
            map.put(CommonConstants.MAP_WHERE, where);
            where= null;
            lblName.setText(observable.getName(map));
        }
    }
       
        
        
        //To enable and/or Disable buttons(folder)...
     
        
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        HashMap hmap=new HashMap();
        hmap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
        ArrayList list=(ArrayList)ClientUtil.executeQuery("getLodgementBillWithoutBankDetails", hmap);
        observable. setTableDetails(list);
        btnSave.setEnabled(true);
        tdtRemittedDt.setEnabled(true);
        txtOtherBankCode.setEnabled(true);
        
        txtOtherBranchCode.setEnabled(true);
        txtOtherBranchCode.setEnabled(true);
        
        txtAcHead.setEnabled(true);
        tdtRemittedDt.setEnabled(true);
        btnEdit.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnCancel.setEnabled(true);
      
    }//GEN-LAST:event_btnNewActionPerformed
    
    // To set the Text Boxes for Account No. and InitiatorChannel, nonEditable...
    private void textDisable(){
        
                //To make this textBox non editable...
        //(new MandatoryDBCheck()).setComponentInit(getClass().getName(), panCashTransaction);
    }
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
	private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
            // Add your handling code here:
            btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
        
	private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
            // Add your handling code here:
            btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
        
	private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
          
	}//GEN-LAST:event_mitDeleteActionPerformed
        
	private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
            // Add your handling code here:
           
	}//GEN-LAST:event_mitEditActionPerformed
        
	private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
            // Add your handling code here:
          
}//GEN-LAST:event_mitNewActionPerformed
        
	private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
            //System.exit(0);
            this.dispose();
    }//GEN-LAST:event_exitForm
        
        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
            try {
                javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Throwable th) {
                th.printStackTrace();
            }
            
            javax.swing.JFrame jf = new javax.swing.JFrame();
            CollectingBankDetailsEntryUI gui = new CollectingBankDetailsEntryUI();
            jf.getContentPane().add(gui);
            jf.setSize(536, 566);
            jf.show();
            gui.show();
        }
        
        /**
         * Getter for property viewType.
         * @return Value of property viewType.
         */
        //        public int getViewType() {
        //            return viewType;
        //        }
        //
        /**
         * Setter for property viewType.
         * @param viewType New value of property viewType.
         */
        //        public void setViewType(int viewType) {
        //            this.viewType = viewType;
        //        }
        
        /**
         * Getter for property intAmtDep.
         * @return Value of property intAmtDep.
         */
        public double getIntAmtDep() {
            return intAmtDep;
        }
        
        /**
         * Setter for property intAmtDep.
         * @param intAmtDep New value of property intAmtDep.
         */
        public void setIntAmtDep(double intAmtDep) {
            this.intAmtDep = intAmtDep;
        }
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBankCode;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnOtherBranchCode;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblBankCode;
    private com.see.truetransact.uicomponent.CLabel lblBankName;
    private com.see.truetransact.uicomponent.CLabel lblBankNameValue;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOtherBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblOtherBranchName;
    private com.see.truetransact.uicomponent.CLabel lblOtherBranchNameValue;
    private com.see.truetransact.uicomponent.CLabel lblRemittedDt;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace62;
    private com.see.truetransact.uicomponent.CLabel lblSpace63;
    private com.see.truetransact.uicomponent.CLabel lblSpace64;
    private com.see.truetransact.uicomponent.CLabel lblSpace65;
    private com.see.truetransact.uicomponent.CLabel lblSpace7;
    private com.see.truetransact.uicomponent.CLabel lblSpace8;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panBankCode;
    private com.see.truetransact.uicomponent.CPanel panOtherBankDetails;
    private com.see.truetransact.uicomponent.CPanel panOtherBranchCode;
    private com.see.truetransact.uicomponent.CPanel panSalaryDeductionMapping;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransactionType;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblOBDetails;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CDateField tdtRemittedDt;
    private com.see.truetransact.uicomponent.CTextField txtAcHead;
    private com.see.truetransact.uicomponent.CTextField txtOtherBankCode;
    private com.see.truetransact.uicomponent.CTextField txtOtherBranchCode;
    // End of variables declaration//GEN-END:variables
}
