  /*
   * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
   *
   * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
   * 
   * PensionSchemeUI.java
   * Created on November 26, 2003, 11:27 AM
   *
   */

package com.see.truetransact.ui.share.pensionScheme;

/**
 *
 * @author Suresh
 *
 **/
import com.see.truetransact.ui.termloan.groupLoan.*;
import com.see.truetransact.ui.termloan.SHG.*;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.termloan.groupLoan.GroupLoanTO;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.uicomponent.CTextField;
import java.util.ArrayList;
import java.awt.*;
import java.lang.Boolean;
import java.util.*;
import javax.swing.*;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;



public class PensionSchemeUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField{
    private final static ClientParseException parseException = ClientParseException.getInstance();
    //    private RemittanceProductRB resourceBundle = new RemittanceProductRB();
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.mdsapplication.MDSApplicationRB", ProxyParameters.LANGUAGE);
//    final MDSApplicationMRB objMandatoryMRB = new MDSApplicationMRB();
    TransactionUI transactionUI = new TransactionUI();
    
    private HashMap mandatoryMap;
    PensionSchemeOB observable = null;
    final String AUTHORIZE="Authorize";
    private String viewType = new String();
    private List finalList = null;
    public int selectedRow = -1;
    private boolean isFilled = false;
    private boolean isDuePay = false;
    Date currDt = null;
    private TableModelListener tableModelListener;
    boolean fromAuthorizeUI = false;    
    AuthorizeListUI authorizeListUI = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    
    /** Creates new form BeanForm */
    public PensionSchemeUI() {
        initComponents();
        settingupUI();
        tabSHGTransaction.resetVisits();
        panTransactionDetails.add(transactionUI);
        transactionUI.setSourceScreen("GROUP_LOAN_PAYMENT");
        observable.setTransactionOB(transactionUI.getTransactionOB());
        currDt = ClientUtil.getCurrentDate();              
    }
    
    private void settingupUI(){
        setFieldNames();
        observable = new PensionSchemeOB();
        //initComponentData();
        setMaximumLength();
        setButtonEnableDisable();
        ClientUtil.enableDisable(panSHGDetails,false);
        //cLabel1.setVisible(false);
        //txtNarration.setVisible(false);
        txtPayment.setVisible(false);
        System.out.println("observable.getCbmShareType()&$&$&"+observable.getCbmShareType());
        cboShareType.setModel(observable.getCbmShareType());
        cboShareType1.setModel(observable.getCbmShareType1());
        initTableDataUI();
        btnDebitGl.setEnabled(false);
        btnLedger.setEnabled(false);
        btnDisplay.setEnabled(false);
    }
    
    private void initTableDataUI() { 
       tblSHGDetails.setModel(observable.getTblPensionScheme());
       tblPensionDetails.setModel(observable.getTbmPensionDetails());
    }
      
   /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnView.setName("btnView");
        lblGroupId.setName("lblGroupId");
        lblMsg.setName("lblMsg");
        lblSpace.setName("lblSpace");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        lblStatus.setName("lblStatus");
        //lblTotalPayment.setName("lblTotalPayment");
        mbrMain.setName("mbrMain");
        panSHGTransDetails.setName("panSHGTransDetails");
        panGroupId.setName("panGroupId");
        panGroupIdDetail.setName("panGroupIdDetail");
        panSHGDetails.setName("panSHGDetails");
        panSHGTableDetails.setName("panSHGTableDetails");
        panStatus.setName("panStatus");
        panTotalPayment.setName("panTotalPayment");
        panTransactionDetails.setName("panTransactionDetails");
        srpSHGTable.setName("srpSHGTable");
        tabSHGTransaction.setName("tabSHGTransaction");
        tblSHGDetails.setName("tblSHGDetails");
        //txtTotalPayment.setName("txtTotalPayment");
    }
    
    
    
    /** Auto Generated Method - setMandatoryHashMap()
     * This method list out all the Input Fields available in the UI.
     * It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
    }
    
    /** Auto Generated Method - getMandatoryHashMap()
     * Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    /*private void initComponentData() {
        try{
            TableCellEditor editor = new DefaultCellEditor(txtPayment);
            SimpleTableModel  stm = new SimpleTableModel((ArrayList)observable.getTableList(),(ArrayList)observable.getTableTitle());
            tblSHGDetails.setModel(stm);
            tblSHGDetails.getColumnModel().getColumn(6).setCellEditor(editor);
            tblSHGDetails.revalidate();
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }*/
    private void setMaximumLength(){
        //txtTotalPayment.setValidation(new CurrencyValidation());
        txtPayment.setAllowAll(true);
   }
    
    /** Auto Generated Method - update()
     * This method called by Observable. It updates the UI with
     * Observable's data. If needed add/Remove RadioButtons
     * method need to be added.*/
    public void update(Observable observed, Object arg) {
       
    }
    
   
    /** Auto Generated Method - updateOBFields()
     * This method called by Save option of UI.
     * It updates the OB with UI data.*/
    public void updateOBFields() {
  observable.setScreen(this.getScreen());
  observable.setTxtTotAmt(txtTotAmt.getText());
    }
    
    /** Auto Generated Method - setHelpMessage()
     * This method shows tooltip help for all the input fields
     * available in the UI. It needs the Mandatory Resource Bundle
     * object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgIsLapsedGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgEFTProductGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPayableBranchGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPrintServicesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgSeriesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace46 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace47 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace48 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace49 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace50 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace51 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabSHGTransaction = new com.see.truetransact.uicomponent.CTabbedPane();
        panSHGTransDetails = new com.see.truetransact.uicomponent.CPanel();
        panSHGDetails = new com.see.truetransact.uicomponent.CPanel();
        panSHGTableDetails = new com.see.truetransact.uicomponent.CPanel();
        srpSHGTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblSHGDetails = new com.see.truetransact.uicomponent.CTable();
        panGroupIdDetail = new com.see.truetransact.uicomponent.CPanel();
        panGroupId = new com.see.truetransact.uicomponent.CPanel();
        lblGroupId = new com.see.truetransact.uicomponent.CLabel();
        panGroupId1 = new com.see.truetransact.uicomponent.CPanel();
        cboShareType = new com.see.truetransact.uicomponent.CComboBox();
        lblDebitGl = new com.see.truetransact.uicomponent.CLabel();
        panDebitGl = new com.see.truetransact.uicomponent.CPanel();
        txtMemberNo = new com.see.truetransact.uicomponent.CTextField();
        btnDebitGl = new com.see.truetransact.uicomponent.CButton();
        btnDisplay = new com.see.truetransact.uicomponent.CButton();
        btnLedger = new com.see.truetransact.uicomponent.CButton();
        panTotalPayment = new com.see.truetransact.uicomponent.CPanel();
        txtPayment = new com.see.truetransact.uicomponent.CTextField();
        txtTotalPayment = new com.see.truetransact.uicomponent.CTextField();
        lblTotalPayment = new com.see.truetransact.uicomponent.CLabel();
        panTransactionDetails = new com.see.truetransact.uicomponent.CPanel();
        panPensionMultiPosting = new com.see.truetransact.uicomponent.CPanel();
        panShareType = new com.see.truetransact.uicomponent.CPanel();
        lblShareType = new com.see.truetransact.uicomponent.CLabel();
        cboShareType1 = new com.see.truetransact.uicomponent.CComboBox();
        btnShow = new com.see.truetransact.uicomponent.CButton();
        srpPensionDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblPensionDetails = new com.see.truetransact.uicomponent.CTable();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtTotAmt = new com.see.truetransact.uicomponent.CTextField();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        lblTotMembers = new com.see.truetransact.uicomponent.CLabel();
        cLabel4 = new com.see.truetransact.uicomponent.CLabel();
        lblSelectedMembers = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
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
        mitAuthorize = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMinimumSize(new java.awt.Dimension(860, 665));
        setPreferredSize(new java.awt.Dimension(860, 665));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(25, 27));
        btnView.setPreferredSize(new java.awt.Dimension(25, 27));
        btnView.setEnabled(false);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnView);

        lblSpace6.setText("     ");
        tbrAdvances.add(lblSpace6);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnNew);

        lblSpace46.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace46.setText("     ");
        lblSpace46.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace46.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace46.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace46);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace47.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace47.setText("     ");
        lblSpace47.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace47.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace47.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace47);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnDelete);

        lblSpace3.setText("     ");
        tbrAdvances.add(lblSpace3);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnSave);

        lblSpace48.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace48.setText("     ");
        lblSpace48.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace48.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace48.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace48);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnCancel);

        lblSpace4.setText("     ");
        tbrAdvances.add(lblSpace4);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnAuthorize);

        lblSpace49.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace49.setText("     ");
        lblSpace49.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace49.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace49.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace49);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace50.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace50.setText("     ");
        lblSpace50.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace50.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace50.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace50);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnReject);

        lblSpace5.setText("     ");
        tbrAdvances.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrAdvances.add(btnPrint);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace51);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(tbrAdvances, gridBagConstraints);

        tabSHGTransaction.setMinimumSize(new java.awt.Dimension(850, 480));
        tabSHGTransaction.setPreferredSize(new java.awt.Dimension(850, 480));

        panSHGTransDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSHGTransDetails.setMinimumSize(new java.awt.Dimension(850, 450));
        panSHGTransDetails.setPreferredSize(new java.awt.Dimension(850, 450));
        panSHGTransDetails.setLayout(new java.awt.GridBagLayout());

        panSHGDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSHGDetails.setMinimumSize(new java.awt.Dimension(840, 315));
        panSHGDetails.setPreferredSize(new java.awt.Dimension(840, 315));
        panSHGDetails.setLayout(new java.awt.GridBagLayout());

        panSHGTableDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSHGTableDetails.setMinimumSize(new java.awt.Dimension(825, 220));
        panSHGTableDetails.setPreferredSize(new java.awt.Dimension(825, 220));
        panSHGTableDetails.setLayout(new java.awt.GridBagLayout());

        srpSHGTable.setMinimumSize(new java.awt.Dimension(800, 200));
        srpSHGTable.setPreferredSize(new java.awt.Dimension(800, 200));
        srpSHGTable.setRequestFocusEnabled(false);

        tblSHGDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Share No", "Customer Name", "Age", "Share OpDt", "Share Amount", "Share Run Period", "Pension"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblSHGDetails.setMaximumSize(new java.awt.Dimension(2147483647, 64));
        tblSHGDetails.setMinimumSize(new java.awt.Dimension(60, 64));
        tblSHGDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(804, 296));
        tblSHGDetails.setPreferredSize(new java.awt.Dimension(775, 3000));
        tblSHGDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSHGDetailsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblSHGDetailsMouseEntered(evt);
            }
        });
        tblSHGDetails.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblSHGDetailsMouseMoved(evt);
            }
        });
        srpSHGTable.setViewportView(tblSHGDetails);

        panSHGTableDetails.add(srpSHGTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panSHGDetails.add(panSHGTableDetails, gridBagConstraints);

        panGroupIdDetail.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panGroupIdDetail.setMinimumSize(new java.awt.Dimension(825, 30));
        panGroupIdDetail.setPreferredSize(new java.awt.Dimension(825, 30));
        panGroupIdDetail.setLayout(new java.awt.GridBagLayout());

        panGroupId.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panGroupIdDetail.add(panGroupId, gridBagConstraints);

        lblGroupId.setText("Share Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 5);
        panGroupIdDetail.add(lblGroupId, gridBagConstraints);

        panGroupId1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 21;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panGroupIdDetail.add(panGroupId1, gridBagConstraints);

        cboShareType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboShareType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboShareType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboShareTypeActionPerformed(evt);
            }
        });
        cboShareType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboShareTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGroupIdDetail.add(cboShareType, gridBagConstraints);

        lblDebitGl.setText("Share No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 21, 1, 0);
        panGroupIdDetail.add(lblDebitGl, gridBagConstraints);

        panDebitGl.setMinimumSize(new java.awt.Dimension(130, 21));
        panDebitGl.setPreferredSize(new java.awt.Dimension(130, 21));
        panDebitGl.setLayout(new java.awt.GridBagLayout());

        txtMemberNo.setAllowAll(true);
        txtMemberNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMemberNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMemberNoActionPerformed(evt);
            }
        });
        txtMemberNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMemberNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panDebitGl.add(txtMemberNo, gridBagConstraints);

        btnDebitGl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDebitGl.setToolTipText("Share Account No.");
        btnDebitGl.setMinimumSize(new java.awt.Dimension(25, 23));
        btnDebitGl.setPreferredSize(new java.awt.Dimension(25, 24));
        btnDebitGl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDebitGlActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDebitGl.add(btnDebitGl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panGroupIdDetail.add(panDebitGl, gridBagConstraints);

        btnDisplay.setBorder(null);
        btnDisplay.setText("Pension Details");
        btnDisplay.setToolTipText("Display Pension Details");
        btnDisplay.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnDisplay.setPreferredSize(new java.awt.Dimension(110, 24));
        btnDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisplayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 22, 0, 0);
        panGroupIdDetail.add(btnDisplay, gridBagConstraints);

        btnLedger.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/ledger.jpg"))); // NOI18N
        btnLedger.setToolTipText("Ledger");
        btnLedger.setFocusable(false);
        btnLedger.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLedger.setMinimumSize(new java.awt.Dimension(25, 23));
        btnLedger.setPreferredSize(new java.awt.Dimension(25, 23));
        btnLedger.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLedger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLedgerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        panGroupIdDetail.add(btnLedger, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panSHGDetails.add(panGroupIdDetail, gridBagConstraints);

        panTotalPayment.setMinimumSize(new java.awt.Dimension(825, 25));
        panTotalPayment.setPreferredSize(new java.awt.Dimension(825, 25));
        panTotalPayment.setLayout(new java.awt.GridBagLayout());

        txtPayment.setBackground(new java.awt.Color(204, 204, 204));
        txtPayment.setBorder(null);
        txtPayment.setMinimumSize(new java.awt.Dimension(10, 21));
        txtPayment.setPreferredSize(new java.awt.Dimension(10, 21));
        txtPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPaymentActionPerformed(evt);
            }
        });
        txtPayment.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPaymentFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTotalPayment.add(txtPayment, gridBagConstraints);

        txtTotalPayment.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalPayment.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panTotalPayment.add(txtTotalPayment, gridBagConstraints);

        lblTotalPayment.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 101, 0, 1);
        panTotalPayment.add(lblTotalPayment, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panSHGDetails.add(panTotalPayment, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSHGTransDetails.add(panSHGDetails, gridBagConstraints);

        panTransactionDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panTransactionDetails.setMinimumSize(new java.awt.Dimension(840, 255));
        panTransactionDetails.setPreferredSize(new java.awt.Dimension(840, 255));
        panTransactionDetails.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSHGTransDetails.add(panTransactionDetails, gridBagConstraints);

        tabSHGTransaction.addTab("Pension Scheme", panSHGTransDetails);

        panShareType.setBorder(javax.swing.BorderFactory.createTitledBorder("Share Type"));

        lblShareType.setText("Share Type");

        btnShow.setText("Display");
        btnShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panShareTypeLayout = new javax.swing.GroupLayout(panShareType);
        panShareType.setLayout(panShareTypeLayout);
        panShareTypeLayout.setHorizontalGroup(
            panShareTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panShareTypeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblShareType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboShareType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnShow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(271, 271, 271))
        );
        panShareTypeLayout.setVerticalGroup(
            panShareTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panShareTypeLayout.createSequentialGroup()
                .addGroup(panShareTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblShareType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboShareType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnShow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        srpPensionDetails.setMinimumSize(new java.awt.Dimension(800, 200));
        srpPensionDetails.setRequestFocusEnabled(false);

        tblPensionDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Share No", "Customer Name", "Age", "Share OpDt", "Share Amount", "Share Run Period", "Pension"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblPensionDetails.setMaximumSize(new java.awt.Dimension(2147483647, 64));
        tblPensionDetails.setMinimumSize(new java.awt.Dimension(60, 64));
        tblPensionDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(804, 296));
        tblPensionDetails.setPreferredSize(new java.awt.Dimension(775, 3800));
        tblPensionDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPensionDetailsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblPensionDetailsMouseEntered(evt);
            }
        });
        tblPensionDetails.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblPensionDetailsMouseMoved(evt);
            }
        });
        srpPensionDetails.setViewportView(tblPensionDetails);

        chkSelectAll.setText("Select All");
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });

        cLabel1.setText("Total Amount");

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        cLabel2.setText("Total Members");

        cLabel4.setText("Selected Members");

        javax.swing.GroupLayout cPanel1Layout = new javax.swing.GroupLayout(cPanel1);
        cPanel1.setLayout(cPanel1Layout);
        cPanel1Layout.setHorizontalGroup(
            cPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(cLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTotMembers, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(cLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblSelectedMembers, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTotAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        cPanel1Layout.setVerticalGroup(
            cPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTotAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTotMembers, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblSelectedMembers, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panPensionMultiPostingLayout = new javax.swing.GroupLayout(panPensionMultiPosting);
        panPensionMultiPosting.setLayout(panPensionMultiPostingLayout);
        panPensionMultiPostingLayout.setHorizontalGroup(
            panPensionMultiPostingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panPensionMultiPostingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panPensionMultiPostingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(cPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panPensionMultiPostingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(chkSelectAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panPensionMultiPostingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(srpPensionDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panShareType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        panPensionMultiPostingLayout.setVerticalGroup(
            panPensionMultiPostingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panPensionMultiPostingLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(panShareType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkSelectAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(srpPensionDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(86, Short.MAX_VALUE))
        );

        tabSHGTransaction.addTab("Pension Multiple Posting - Transfer", panPensionMultiPosting);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabSHGTransaction, gridBagConstraints);
        tabSHGTransaction.getAccessibleContext().setAccessibleName("Pension Scheme");

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(panStatus, gridBagConstraints);

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

        mitAuthorize.setText("Authorize");
        mnuProcess.add(mitAuthorize);

        mitReject.setText("Rejection");
        mnuProcess.add(mitReject);

        mitException.setText("Exception");
        mnuProcess.add(mitException);
        mnuProcess.add(sptException);

        mitPrint.setText("Print");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);
    }// </editor-fold>//GEN-END:initComponents

    private void txtPaymentActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
//        calc();
//        int selectedRow = tblSHGDetails.getSelectedRow();
       // System.out.println("jdjfs333");
        //if(observable.duePayYN(txtAcctNum.getText(),CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(selectedRow, 1).toString()))){
         //  ClientUtil.showMessageWindow("Payment not allowed if due...");
         //  return;
        //}
        //checkingPaymentAmount(selectedRow);
    }                                          

    private void txtPaymentFocusLost(java.awt.event.FocusEvent evt) {                                     
        // TODO add your handling code here:
//        calc();
//        selectedRow = tblSHGDetails.getSelectedRow();
       // System.out.println("jdjfs444");
//       checkingPaymentAmount(selectedRow);
    }                                    
    /*public class SimpleTableModel extends AbstractTableModel {
        
        private ArrayList dataVector;
        private ArrayList headingVector;
        
        public SimpleTableModel(ArrayList dataVector, ArrayList headingVector){
            this.dataVector = dataVector;
            this.headingVector = headingVector;
        }
        public int getColumnCount() {
            return headingVector.size();
        }
        
        public int getRowCount() {
            return dataVector.size();
        }
        
        public Object getValueAt(int row, int col) {
            ArrayList rowVector = (ArrayList)dataVector.get(row);
            return rowVector.get(col);
        }
        
        public String getColumnName(int column) {
            return headingVector.get(column).toString();
        }
        
        //public Class getColumnClass(int c) {
          // return getValueAt(0, c).getClass();
        //}
        
        public boolean isCellEditable(int row, int col) {
                if (col == 6){
                    selectedRow = row;
                    System.out.println("jdjfs999");
                    checkingPaymentAmount(selectedRow);
                    if(observable.duePayYN(txtAcctNum.getText(),CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(selectedRow, 1).toString()))){
                            ClientUtil.showMessageWindow("Payment not allowed if due...");
                    
                    }
                     calc();
                    return true;
                }else{
                    return false;
                }
        }
        
                
        public void setValueAt(Object aValue, int row, int col) {
            ArrayList rowVector = (ArrayList)dataVector.get(row);
            rowVector.set(col, aValue);
//            selectedRow = row;
        }
        
        public void deleteData() {
            if (dataVector!=null && dataVector.size()>0) {
                dataVector.clear();
                fireTableDataChanged();
            }
        }
    }*/
    
    /*public void checkingPaymentAmount(int selectedRow){
        double paymentAmt = 0;
        double totAvailBal = 0;
        paymentAmt = CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(selectedRow, 6).toString()).doubleValue();
        if(paymentAmt > 0){
            totAvailBal = CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(selectedRow, 4).toString()).doubleValue(); 
            if(paymentAmt > totAvailBal ){
                ClientUtil.showMessageWindow("Payment Amount Should not Exceeds Avail Balance ");
                tblSHGDetails.setValueAt("", selectedRow, 6);
                ((SimpleTableModel)tblSHGDetails.getModel()).fireTableDataChanged();
                calc();
            }
        }
    }*/
    
    /*public void checkingDueAmount(int selectedRow){
        double dueAmt = 0;
        double avail_bal = 0;
        double limit = 0;
        dueAmt = CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(selectedRow, 5).toString()).doubleValue();
        avail_bal = CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(selectedRow, 4).toString()).doubleValue();
        limit = CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(selectedRow, 3).toString()).doubleValue();
        if((limit-avail_bal)< dueAmt){
                ClientUtil.showMessageWindow("Payment Cannot done if Due exist!!!");
                tblSHGDetails.setValueAt("", selectedRow, 6);
                ((SimpleTableModel)tblSHGDetails.getModel()).fireTableDataChanged();
                calc();
            }
    }*/
       
    public void calc(){
        double totPayment = 0;
        String custName = "";
        if(tblSHGDetails.getRowCount()>0){
            for (int i=0; i< tblSHGDetails.getRowCount(); i++) {
                System.out.println("totPayment###"+totPayment);
                totPayment = totPayment + CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(i, 6).toString()).doubleValue();                 
                txtTotalPayment.setText(CommonUtil.convertObjToStr(totPayment));
            }            
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);          
            transactionUI.resetObjects();
            transactionUI.setCallingTransType("CASH");            
            transactionUI.setCallingAmount(CommonUtil.convertObjToStr(totPayment));
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                custName = CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(0, 1));
                transactionUI.setCallingApplicantName(custName);
            }
        }
    }
        
    public void calcToatal(){               // Edit Or Authorize Time
        double totPayment = 0;
        if(tblSHGDetails.getRowCount()>0){
            for (int i=0; i< tblSHGDetails.getRowCount(); i++) {
                totPayment = totPayment + CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(i, 6).toString()).doubleValue();
                txtTotalPayment.setText(CurrencyValidation.formatCrore(String.valueOf(totPayment)));
            }
            tblSHGDetails.setEnabled(false);
        }
    }
    public void calcLoanBal(){               // Edit Or Authorize Time88888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888
        double totLoanBal = 0;
        if(tblSHGDetails.getRowCount()>0){
            for (int i=0; i< tblSHGDetails.getRowCount(); i++) {
                totLoanBal = totLoanBal + CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(i, 4).toString()).doubleValue();
               // txtODBal.setText(CurrencyValidation.formatCrore(String.valueOf(totLoanBal)));
            }//
            //transa//ctionUI.setCallingAmount(CommonUtil.convertObjToStr(txtTotalPayment.getText()));
            //tblSHGDetails.setEnabled(false);
        }
    }
    
    //    public class CustomTableCellRenderer extends DefaultTableCellRenderer
    //    {
    //        public Component getTableCellRendererComponent
    //           (JTable table, Object value, boolean isSelected,
    //           boolean hasFocus, int row, int column) {
    //            Component cell = super.getTableCellRendererComponent
    //               (table, value, isSelected, hasFocus, row, column);
    //
    //            if( value instanceof Boolean ) {
    //                HashMap returnMap = observable.getProxyReturnMap();
    //                for (int i=0; i<tblStandingInstruction.getRowCount(); i++) {
    //                     if (returnMap.containsKey(tblStandingInstruction.getValueAt(i, 1))) {
    //                            cell.setBackground( Color.red );
    //                     }
    //                }
    
    //                Integer amount = (Integer) value;
    //                if( amount.intValue() < 0 )
    //                {
    //                    cell.setBackground( Color.red );
    //                    // You can also customize the Font and Foreground this way
    //                    // cell.setForeground();
    //                    // cell.setFont();
    //                }
    //                else
    //                {
    //                    cell.setBackground( Color.white );
    //                }
    //            }
    //            return cell;
    //        }
    //    }
                   
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        callView("Enquiry");
        lblStatus.setText("Enquiry");
        btnDelete.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false);
        btnView.setEnabled(false);    
    }            //    private void enableDisableAliasBranchTable(boolean flag) {//GEN-LAST:event_btnViewActionPerformed
   
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EXCEPTION);
        updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
     private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled){
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("SHARE_ACCT_NO", txtMemberNo.getText());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT",ClientUtil.getCurrentDateWithTime());
            singleAuthorizeMap.put("TRANS_DT",currDt.clone());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap,observable.getTxtGroupId());
            viewType = "";
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;            
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("TRANS_DT", currDt);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put(CommonConstants.INITIATED_BRANCH, TrueTransactMain.BRANCH_ID);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);            
            whereMap = null;
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                mapParam.put(CommonConstants.MAP_NAME, "getPensionSchemeAuthorizeCashUI");
            } else {
                mapParam.put(CommonConstants.MAP_NAME, "getPensionSchemeAuthorizeUI");
            }
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }        
    }
    public void authorize(HashMap map,String id) {
        System.out.println("Authorize Map : " + map);
        
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            if(transactionUI.getOutputTO().size()>0){
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            observable.setSelectedBranchID(getSelectedBranchID());
            System.out.println("branch%#%@#%@%@%@#%@#"+getSelectedBranchID());
            observable.doAction();
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                 if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.displayDetails("Group loan Payments");
                }
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                    authorizeListUI.setFocusToTable();
                    authorizeListUI.displayDetails("Group loan Payments");
                }
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);     
        setButtonEnableDisable();
        ClientUtil.clearAll(this);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        transactionUI.setSaveEnableValue(ClientConstants.ACTIONTYPE_NEW);     
        cboShareType.setEnabled(true);
        txtMemberNo.setEnabled(true);
        btnDebitGl.setEnabled(true);
        btnLedger.setEnabled(true);
        btnDisplay.setEnabled(true);
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        callView("Enquiry");
        lblStatus.setText("Enquiry");
//        btnDelete.setEnabled(false);
//        btnNew.setEnabled(false);
//        btnEdit.setEnabled(false);
//        btnDelete.setEnabled(false);
//        btnSave.setEnabled(false);
//        btnView.setEnabled(false);       
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(false);
        ClientUtil.enableDisable(panSHGDetails,false);       
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:   
        System.out.println("tabSHGTransaction..."+tabSHGTransaction.getSelectedIndex());
        if(tabSHGTransaction.getSelectedIndex() == 1){
           updateOBFields();
           savePerformed(); 
        }else{
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                int transactionSize = 0;
                if (transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(txtTotalPayment.getText()).doubleValue() > 0) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                    return;
                } else {
                    if (CommonUtil.convertObjToDouble(txtTotalPayment.getText()).doubleValue() > 0) {
                        transactionSize = (transactionUI.getOutputTO()).size();
                        if (transactionSize != 1 && CommonUtil.convertObjToDouble(txtTotalPayment.getText()).doubleValue() > 0) {
                            ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction");
                            return;
                        } else {
                            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                        }
                    } else if (transactionUI.getOutputTO().size() > 0) {
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    }
                }
                if (transactionSize == 0 && CommonUtil.convertObjToDouble(txtTotalPayment.getText()).doubleValue() > 0) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                    return;
                } else if (transactionSize != 0) {
                    if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                        return;
                    }
                    if (transactionUI.getOutputTO().size() > 0) {
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                        updateOBFields();
                        savePerformed();
                    }
                }
            }
        } 
     //__ Make the Screen Closable..
     setModified(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed(){
        if(tabSHGTransaction.getSelectedIndex() == 1){
            setTableFinalListForMultipleTransfer();
        }else{
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                setTableFinalList();
            }
        }
        observable.setSelectedBranchID(getSelectedBranchID());
         CommonUtil comm = new CommonUtil();
         final JDialog loading = comm.addProgressBar();
         SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
             @Override
            protected Void doInBackground() throws InterruptedException 
            {
                if(tabSHGTransaction.getSelectedIndex() == 1){
                    observable.doActionForMultipleTransfer(); 
                }else{
                    observable.doAction();
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
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
            if (observable.getProxyReturnMap() != null) {
                        displayTransDetailNew(observable.getProxyReturnMap());
            }
            //displayTransDetailNew();
            btnCancelActionPerformed(null);
            btnCancel.setEnabled(true);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
        }
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }
   
    public void  displayTransDetailNew(HashMap returnMap){
         String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
      //  System.out.println("jhj>>>>>>>");
        Object keys[] = returnMap.keySet().toArray();
        System.out.println("jhj>>>>>>>adad");
        //System.out.println("keeeeeeeeeeeyyy>>>>>>>>>>>"+keys[]);
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        HashMap transIdMap = new HashMap();
        HashMap transTypeMap = new HashMap();
       // System.out.println("keeeeeeeeeeeyyy>>>>>>>>>>>" + keys.length);
        for (int i = 0; i < keys.length; i++) {
            System.out.println("jhj>>>>>>>adad1211222@@@@" + (returnMap.get(keys[i]) instanceof String));
            if (returnMap.get(keys[i]) instanceof String) {
          //      System.out.println("hdfdasd");
                continue;
            }

          //  System.out.println("hdfdasd@@@@@");
            tempList = (List) returnMap.get(keys[i]);
          //  System.out.println("hdfdasd@@@@@>>>>>" + tempList);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
              //  System.out.println("haaaiii11....>>>");
                for (int j = 0; j < tempList.size(); j++) {
                  //  System.out.println("haaaiii11....>>>aa");
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                       // System.out.println("haaaiii11....>>>bb");
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                      //  System.out.println("haaaiii11....>>>cc");
                        cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                      //  System.out.println("haaaiii11....>>>dd");
                        cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transTypeMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                //System.out.println("haaaiii22....>>>");
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                      //  System.out.println("haaaiii22....>>>aa");
                        transId = (String) transMap.get("BATCH_ID");
                    }
                    transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                       // System.out.println("haaaiii22....>>>bb");
                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                      //  System.out.println("haaaiii22....>>>cc");
                        transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transIdMap.put(transMap.get("BATCH_ID"), "TRANSFER");
                }
                transferCount++;
            }
        }
        if (cashCount > 0) {
            displayStr += cashDisplayStr;
        }
        if (transferCount > 0) {
            displayStr += transferDisplayStr;
        }
        if (!displayStr.equals("")) {
            ClientUtil.showMessageWindow("" + displayStr);
        }
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        System.out.println("#$#$$ yesNo : "+yesNo);
        if (yesNo==0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            //paramMap.put("TransId", transId);
            paramMap.put("TransDt", currDt);
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            Object keys1[] = transIdMap.keySet().toArray();
             for (int i = 0; i < keys.length; i++) {
                            paramMap.put("TransId", keys1[i]);
                            ttIntgration.setParam(paramMap);
                            //                        if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
                            if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {
                                ttIntgration.integrationForPrint("MDSReceiptsTransfer");
                            } 
                            else if (CommonUtil.convertObjToStr(transTypeMap.get(keys1[i])).equals("DEBIT")) {
                                    ttIntgration.integrationForPrint("CashPayment", false);
                            }else {
                                ttIntgration.integrationForPrint("CashReceipt", false);
                            }
                        }     
        }
        
    }
    private void displayTransDetailOld() {
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
            null, options, options[0]);
            System.out.println("#$#$$ yesNo : "+yesNo);
            if (yesNo==0) {
                TTIntegration ttIntgration = null;
                HashMap paramMap = new HashMap();
                paramMap.put("TransId", observable.getShgTransId());
                paramMap.put("TransDt", ClientUtil.getCurrentDate());
                paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                ttIntgration.setParam(paramMap);
                String reportName = "";
                reportName = "SHGReceipt";
                ttIntgration.integrationForPrint(reportName, false);
            }
    }
    
    private void setTableFinalList(){
        finalList = observable.getFinalList();
        HashMap custMap = new HashMap();
        if(finalList!= null && finalList.size()>0){
            System.out.println("#$@$#@$@$@ FinalList : "+finalList);
            for(int i=0; i<finalList.size(); i++){
                String custNo="";
                custMap = (HashMap)finalList.get(i);
                custNo = CommonUtil.convertObjToStr(custMap.get("SHARE_ACCT_NO"));
                System.out.println("$#@@$@$#$@$ custNo : "+custNo);
                for(int j=0; j<tblSHGDetails.getRowCount();j++) {
                    if (CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(j, 0)).equals(custNo)) {
                        custMap.put("MIN_PENSION",tblSHGDetails.getValueAt(j, 6));
                    }
                                      
                }
            }
            System.out.println("#$########## custMap : "+custMap);
            System.out.println("#$########## finalList : "+finalList);
            observable.setFinalList(finalList);
        }
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        viewType = "CANCEL" ;
        if (fromNewAuthorizeUI) {
            this.dispose();
            fromNewAuthorizeUI = false;
            newauthorizeListUI.setFocusToTable();
        }
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.setFocusToTable();
        }
        lblStatus.setText("               ");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);        
        btnNew.setEnabled(true);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        btnCancel.setEnabled(false);
        isFilled = false;
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        transactionUI.setCallingTransAcctNo("");
        transactionUI.setCallingProdID("");
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();       
        ClientUtil.enableDisable(this, false);     
        observable.resetForm();
        clearTable();
        //__ Make the Screen Closable..
        setModified(false);
        txtMemberNo.setText("");
        txtMemberNo.setEnabled(false);  
        btnDebitGl.setEnabled(false);
        btnLedger.setEnabled(false);
        btnDisplay.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void clearTable(){
        if(tblSHGDetails.getRowCount()>0){
        ((DefaultTableModel) tblSHGDetails.getModel()).setRowCount(0);
        }
        if(tblPensionDetails.getRowCount()>0){
        ((DefaultTableModel) tblPensionDetails.getModel()).setRowCount(0);
        }
    }
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
   
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
        //        this.dispose();
    }//GEN-LAST:event_mitCloseActionPerformed

private void cboShareTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboShareTypeActionPerformed
//    HashMap dataMap = new HashMap();
//    dataMap.put("SHARE_TYPE",CommonUtil.convertObjToStr(observable.getCbmShareType().getKeyForSelected()));
//    List lst = ClientUtil.executeQuery("getSharePensionScheme", dataMap);
//    dataMap = null;
//    if (lst != null && lst.size() > 0) {
//        dataMap = (HashMap) lst.get(0);
//        observable.setShareMap(dataMap);
//        tblSHGDetails.setEnabled(true);
//        //initTableData();
//        //observable.setTableData();
//        //tblSHGDetails.setModel(observable.getTblPensionScheme());
//        //calc();
//    }
}//GEN-LAST:event_cboShareTypeActionPerformed


private void cboShareTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboShareTypeFocusLost
    
}//GEN-LAST:event_cboShareTypeFocusLost

private void tblSHGDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSHGDetailsMouseClicked
        // TODO add your handling code here:
    if(evt.getClickCount()==2){
        com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
        HashMap paramMap = new HashMap();
        int column = 0;
        if((column = tblSHGDetails.getSelectedColumn())==0){
            if(CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(), column))!=null &&
              !CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(), column)).equals("")){
              paramMap.put("MemberNo", CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(), column)));
              ttIntgration.setParam(paramMap);            
              ttIntgration.integrationForPrint("SharePensionLedger",true); 
            }
       }  
    }
}//GEN-LAST:event_tblSHGDetailsMouseClicked

private void txtMemberNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMemberNoActionPerformed
       // TODO add your handling code here:
}//GEN-LAST:event_txtMemberNoActionPerformed

private void txtMemberNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMemberNoFocusLost
        // TODO add your handling code here:
    if(!observable.checkExistShare(txtMemberNo.getText())){
        ClientUtil.showMessageWindow("Invalid Share No.!!!");
        return;
    }else if(!observable.checkpPendingAuthorization(txtMemberNo.getText())){
        ClientUtil.showMessageWindow("Authorization Pending For This Share No.!!!");
        return;            
    }
}//GEN-LAST:event_txtMemberNoFocusLost

private void btnDebitGlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDebitGlActionPerformed
        // TODO add your handling code here:
        viewType = "MEMBER_NO";
        callView("MEMBER_NO");        
}//GEN-LAST:event_btnDebitGlActionPerformed

private void btnDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisplayActionPerformed
// TODO add your handling code here:   
            HashMap dataMap = new HashMap();
            dataMap.put("SHARE_TYPE",CommonUtil.convertObjToStr(observable.getCbmShareType().getKeyForSelected()));
            List lst = ClientUtil.executeQuery("getSharePensionScheme", dataMap);
            dataMap = null;
            if (lst != null && lst.size() > 0) {
                dataMap = (HashMap) lst.get(0);
                dataMap.put("SHARE_ACCT_NO", txtMemberNo.getText());
                if(observable.checkExistShare(txtMemberNo.getText())){
                    if(observable.checkpPendingAuthorization(txtMemberNo.getText())){ 
                        observable.setShareMap(dataMap);
                        tblSHGDetails.setEnabled(true);
                         CommonUtil comm = new CommonUtil();
                         final JDialog loading = comm.addProgressBar();
                         SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                             @Override
                            protected Void doInBackground() throws InterruptedException 
                            {
                                initTableData();
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
                        
                    }else{
                        ClientUtil.showMessageWindow("Authorization Pending For This Share No.!!!");
                        return;
                    }
                }else{
                    ClientUtil.showMessageWindow("Invalid Share No.!!!");
                    return;
                }
            }
}//GEN-LAST:event_btnDisplayActionPerformed

private void tblSHGDetailsMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSHGDetailsMouseMoved
// TODO add your handling code here:
   tblSHGDetails.setToolTipText("Double Click on ShareNo for Leadger");
}//GEN-LAST:event_tblSHGDetailsMouseMoved

private void btnLedgerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLedgerActionPerformed
// TODO add your handling code here:
    final com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
    HashMap paramMap = new HashMap();
    int column = 0;
    if(txtMemberNo.getText()!=null && txtMemberNo.getText().length()>0){
       paramMap.put("MemberNo", CommonUtil.convertObjToStr(txtMemberNo.getText()));
       ttIntgration.setParam(paramMap);  
         CommonUtil comm = new CommonUtil();
         final JDialog loading = comm.addProgressBar();
         SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
             @Override
            protected Void doInBackground() throws InterruptedException 
            {
                ttIntgration.integrationForPrint("SharePensionLedger",true);
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
   }else{
        ClientUtil.showAlertWindow("Please Select MemberShip No.!!");
        return;
    }
}//GEN-LAST:event_btnLedgerActionPerformed

private void tblSHGDetailsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSHGDetailsMouseEntered
// TODO add your handling code here:
}//GEN-LAST:event_tblSHGDetailsMouseEntered

    private void tblPensionDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPensionDetailsMouseClicked
        // TODO add your handling code here:
        calTotalPayment();
    }//GEN-LAST:event_tblPensionDetailsMouseClicked

    private void tblPensionDetailsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPensionDetailsMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPensionDetailsMouseEntered

    private void tblPensionDetailsMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPensionDetailsMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPensionDetailsMouseMoved

    private void btnShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowActionPerformed
        // TODO add your handling code here:

        HashMap dataMap = new HashMap();
        dataMap.put("SHARE_TYPE", CommonUtil.convertObjToStr(observable.getCbmShareType1().getKeyForSelected()));
        List lst = ClientUtil.executeQuery("getSharePensionScheme", dataMap);
        dataMap = null;
        if (lst != null && lst.size() > 0) {
            dataMap = (HashMap) lst.get(0);
            observable.setShareMap(dataMap);
        }          
        tblPensionDetails.setEnabled(true);
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                initTableDataForMultipleTransfer();
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

    }//GEN-LAST:event_btnShowActionPerformed

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
        setSelectAll(new Boolean(chkSelectAll.isSelected()));
        calTotalPayment();
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this);
       // observable.resetTableValues();
        observable.resetForm();
        clearTable();
        lblTotMembers.setText(""); //Added by nithya on 25-08-2020 for KD-2208
        lblSelectedMembers.setText("");
    }//GEN-LAST:event_btnClearActionPerformed

     public void setSelectAll(Boolean selected) {
        for (int i = 0, j = tblPensionDetails.getRowCount(); i < j; i++) {
            tblPensionDetails.setValueAt(selected, i, 0);
        }
    }
    
     /** To display a popUp window for viewing existing data */
    private void callView(String currAction){
        viewType = currAction;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("BRANCH_CODE", getSelectedBranchID());
        if (currAction.equalsIgnoreCase("Edit")){
        }else if(currAction.equalsIgnoreCase("Delete")){
            viewMap.put(CommonConstants.MAP_NAME, "getGroupLoanDelete");
        }else if(currAction.equalsIgnoreCase("Enquiry")){
            viewMap.put(CommonConstants.MAP_NAME, "getGroupLoanEnquiry");
        }else if(viewType.equals("GROUP_ID")){
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getGroupLoanDetails");
        }else if(viewType.equals("MEMBER_NO")){         
            whereMap.put("SHARE_TYPE",CommonUtil.convertObjToStr(observable.getCbmShareType().getKeyForSelected()));
            viewMap.put(CommonConstants.MAP_NAME, "getShareMemeberDetails");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
        new ViewAll(this,viewMap).show();
    }
    
    /** This method helps in filling the data frm the data base to respective txt fields
     * @param obj param The selected data from the viewAll() is passed as a param
     */
        public void fillData(Object obj){
        try{
            HashMap hashMap=(HashMap)obj;
            System.out.println("### fillData Hash : "+hashMap);
            isFilled = true;
             if (hashMap.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
                fromNewAuthorizeUI = true;
               newauthorizeListUI = (NewAuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                //transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(true);                
            }
            if (hashMap.containsKey("FROM_AUTHORIZE_LIST_UI")) {
                fromAuthorizeUI = true;
                authorizeListUI = (AuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                //transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(true);                
            }
            if(viewType == "MEMBER_NO"){
                txtMemberNo.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBERSHIP_NO")));
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                this.setButtonEnableDisable();
              
                observable.setTxtGroupId(CommonUtil.convertObjToStr(hashMap.get("SHG_ID")));
                observable.setShgTransId(CommonUtil.convertObjToStr(hashMap.get("SHG_TRANS_ID")));
                observable.getData(hashMap);
                ClientUtil.enableDisable(panSHGTransDetails,false);
                //calcToatal();
                //calcLoanBal();
                }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                this.setButtonEnableDisable();             
                txtMemberNo.setText(CommonUtil.convertObjToStr(hashMap.get("SHARE_ACCT_NO")));               
                hashMap.put("TRANS_DT",CommonUtil.getProperDate(currDt,DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashMap.get("TRANS_DT")))));                
                System.out.println("hashMap.hashMap>>>>>"+hashMap);
                initTableAuthData(hashMap);
                //observable.setAuthTableData(hashMap);
                System.out.println("sdfsdfdsfdsfsdf"+observable.getTblAuthPensionScheme());
                //tblSHGDetails.setModel(observable.getTblAuthPensionScheme());
                observable.getData(hashMap);
                //ClientUtil.enableDisable(panSHGTransDetails,false);
                //calcToatal();
            }
            if(viewType ==  AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                ClientUtil.enableDisable(this,false);
            }
            hashMap = null;
            btnCancel.setEnabled(true);
        }catch(Exception e){
            e.printStackTrace();
        }
        //To Save the data in the Internal Frame...
        setModified(true);
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
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
        
        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
    
    public void initTableData() {
        // model=new javax.swing.table.DefaultTableModel();

        tblSHGDetails.setModel(new javax.swing.table.DefaultTableModel(
                setTableData(),
                new String[]{
                   "Share No", "Customer Name", "Age", "Share OpDate", "Share Amount", "Share Run Period","Pension"}) {

            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };            
            
            boolean[] canEdit = new boolean[]{
                 false, false, false, false,false,false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 6) {
                    return true;
                }
                return canEdit[columnIndex];
            }
        });
        //setSizeTableData();
        //setColour();
        //setUpComboBox(tblBalanceUpdate,tblBalanceUpdate.getColumnModel().getColumn(2));
        tblSHGDetails.setCellSelectionEnabled(true);
        tblSHGDetails.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //  tblBalanceUpdatePropertyChange(evt);
            }
        });
        calc();
        setTableModelListener();
        //calc();
    }
    
    public void initTableAuthData(HashMap editMap) {
           tblSHGDetails.setModel(new javax.swing.table.DefaultTableModel(
                setTableAuthData(editMap),
                new String[]{
                        "Trans Id", "Trans Dt", "Share No", "Customer Name", "Age", "Share Run Period","Pension amount"}) {
                            
            Class[] types = new Class[]{
                 java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                 false,false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        tblSHGDetails.setCellSelectionEnabled(true);
        tblSHGDetails.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //  tblBalanceUpdatePropertyChange(evt);
            }
        });
        if (tblSHGDetails.getRowCount() > 0) {
            calc();
        }
    }
    
    private Object[][] setTableData() {
        HashMap whereMap = new HashMap();
        HashMap dataMap = new HashMap();
        ArrayList rowList = new ArrayList();
        ArrayList tableList = new ArrayList();
        HashMap elseMap = new HashMap();
        System.out.println("getShareMap^$^#^^"+observable.getShareMap());
        whereMap.putAll(observable.getShareMap());         
        whereMap.put("CURR_DT",currDt.clone());
        List loanList = ClientUtil.executeQuery("getSharePensionSchemeDetails", whereMap);
        if (loanList != null && loanList.size() > 0) {
            Object totalList[][] = new Object[loanList.size()][9];
            for (int j = 0; j < loanList.size(); j++) {
                dataMap = (HashMap) loanList.get(j);
                totalList[j][0] = CommonUtil.convertObjToStr(dataMap.get("SHARE_ACCT_NO"));
                totalList[j][1] = CommonUtil.convertObjToStr(dataMap.get("CUST_NAME"));
                totalList[j][2] = CommonUtil.convertObjToStr(dataMap.get("AGE"));
                totalList[j][3] = CommonUtil.convertObjToStr(dataMap.get("CREATED_DT"));
                totalList[j][4] = CommonUtil.convertObjToStr(dataMap.get("OUTSTANDING_AMOUNT"));
                totalList[j][5] = CommonUtil.convertObjToStr(dataMap.get("DURATION"));
                totalList[j][6] = CommonUtil.convertObjToStr(dataMap.get("MIN_PENSION"));
            }
          observable.setFinalList(loanList);
            System.out.println("totalList..nithya .. testing.. " + totalList );
          return totalList;
        }else {
              txtTotalPayment.setText("");
              String paidDate = "";
              HashMap errorMap = new HashMap();
                errorMap.put("SHARE_TYPE",CommonUtil.convertObjToStr(observable.getShareMap().get("SHARE_TYPE")));
                errorMap.put("SHARE_ACCT_NO",CommonUtil.convertObjToStr(observable.getShareMap().get("SHARE_ACCT_NO")));
                errorMap.put("CURR_DT",currDt.clone());
                List elseList = ClientUtil.executeQuery("getSharePensionSchemeDetails", errorMap);
                if (elseList != null && elseList.size() > 0) {            
                    dataMap = (HashMap) elseList.get(0); 
                    if(!CommonUtil.convertObjToStr(dataMap.get("PAID_DATE")).equals("")&&
                          CommonUtil.convertObjToStr(dataMap.get("PAID_DATE"))!=null){
                        paidDate = CommonUtil.convertObjToStr(dataMap.get("PAID_DATE"));
                    }else{
                      paidDate = "Not Paid Yet!!!";
                    }
                    String displayDetailsStr = "";
                    displayDetailsStr += "Share Account Details... " + "\n"
                            + "Customer Name : " + CommonUtil.convertObjToStr(dataMap.get("CUST_NAME")) + "\n"
                            + "Customer Age  : " + CommonUtil.convertObjToStr(dataMap.get("AGE")) + "\n"
                            + "Share Opened Date: " + CommonUtil.convertObjToStr(dataMap.get("CREATED_DT")) + "\n"
                            + "Share Run Duration : " + CommonUtil.convertObjToStr(dataMap.get("DURATION")) +"\n"
                            + "Last Pension Paid Date : " + paidDate +"\n"
                            + "Conditions not Matched......"+"\n"
                            + "Pension Not Available For This Account!!!";
                    ClientUtil.showMessageWindow("" + displayDetailsStr);
                    txtMemberNo.setText("");
            }
        }
        return null;
    }
    
    private Object[][] setTableAuthData(HashMap editMap1) {
        HashMap whereMap = new HashMap();
        HashMap dataMap = new HashMap();
        whereMap.putAll(editMap1);
        List editList = ClientUtil.executeQuery("getSharePensionSchemeDetailsAuthorize", whereMap);
        if (editList != null && editList.size() > 0) {
            Object totalList[][] = new Object[editList.size()][8];
            for (int j = 0; j < editList.size(); j++) {
                dataMap = (HashMap) editList.get(j);
                observable.setShareMap(dataMap);
                System.out.println("dataMap####"+dataMap);
                totalList[j][0] = (CommonUtil.convertObjToStr(dataMap.get("TRANS_ID")));
                totalList[j][1] = (CommonUtil.convertObjToStr(dataMap.get("TRANS_DT")));
                totalList[j][2] = (CommonUtil.convertObjToStr(dataMap.get("SHARE_ACCT_NO")));
                totalList[j][3] = (CommonUtil.convertObjToStr(dataMap.get("CUST_NAME")));
                totalList[j][4] = (CommonUtil.convertObjToStr(dataMap.get("CUST_AGE")));
                totalList[j][5] = (CommonUtil.convertObjToStr(dataMap.get("SHARE_RUN_PERIOD")));
                totalList[j][6] = (CommonUtil.convertObjToStr(dataMap.get("TRANS_AMOUNT")));
            }
          observable.setFinalList(editList);
          return totalList;
        }else {
            ClientUtil.displayAlert("No Data!!! ");

        }
        return null;
    }
    
    private void setTableModelListener() {
        try{
        tableModelListener = new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    double recTotal=0;
                    int RowCount=tblSHGDetails.getRowCount(); 
                    System.out.println("row#####"+row);
                    System.out.println("column#####"+column);
                    if (column == 6) {
                            if(tblSHGDetails.getValueAt(row, 6)!=null && !tblSHGDetails.getValueAt(row, 6).toString().equals("") && !isNumeric(tblSHGDetails.getValueAt(row, 6).toString()))
                            {
                                System.out.println("4356346436346====");
                                ClientUtil.showAlertWindow("Please enter Numeric Value!!!"); 
                                tblSHGDetails.setValueAt("", row, 6);
                                calc();
                                return;
                            }
                            if(tblSHGDetails.getValueAt(row, 6)!=null && !tblSHGDetails.getValueAt(row, 6).toString().equals("") && isNegative(CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(row, 6)) .doubleValue()))
                            {
                                System.out.println("4356346436346====");
                                ClientUtil.showAlertWindow("Negative value not allowed!!!"); 
                                tblSHGDetails.setValueAt("", row, 6);
                                calc();
                                return;
                            }
                            calc();
                            transactionUI.cancelAction(false);
                            transactionUI.setButtonEnableDisable(true);           
                            transactionUI.resetObjects();                       
                            transactionUI.setCallingTransType("CASH");
                 }
                }
            }
        };
        tblSHGDetails.getModel().addTableModelListener(tableModelListener);        
    }catch(Exception e){
            e.printStackTrace();
            }
    }
    
     // Added by nithya on 19-04-2018 for 0009951: Member pension bulk posting required
     private Object[][] setTableDataForMultipleTransfer() {
        HashMap whereMap = new HashMap();
        HashMap dataMap = new HashMap();
        ArrayList rowList = new ArrayList();
        ArrayList tableList = new ArrayList();
        HashMap elseMap = new HashMap();
        System.out.println("getShareMap^$^#^^"+observable.getShareMap());
        whereMap.putAll(observable.getShareMap());         
        whereMap.put("CURR_DT",currDt.clone());
        List loanList = ClientUtil.executeQuery("getAllSharePensionMemberDetailsForTransfer", whereMap);
        if (loanList != null && loanList.size() > 0) {
            Object totalList[][] = new Object[loanList.size()][12];
            for (int j = 0; j < loanList.size(); j++) {
                dataMap = (HashMap) loanList.get(j);
                totalList[j][0] = new Boolean(false);
                totalList[j][1] = CommonUtil.convertObjToStr(dataMap.get("SHARE_ACCT_NO"));
                totalList[j][2] = CommonUtil.convertObjToStr(dataMap.get("CUST_NAME"));
                totalList[j][3] = CommonUtil.convertObjToStr(dataMap.get("AGE"));
                totalList[j][4] = CommonUtil.convertObjToStr(dataMap.get("CREATED_DT"));
                totalList[j][5] = CommonUtil.convertObjToStr(dataMap.get("OUTSTANDING_AMOUNT"));
                totalList[j][6] = CommonUtil.convertObjToStr(dataMap.get("DURATION"));
                totalList[j][7] = CommonUtil.convertObjToStr(dataMap.get("MIN_PENSION"));
                totalList[j][8] = CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_CREDIT_AC"));
                totalList[j][9] = CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_CREDIT_PRODUCT"));
                totalList[j][10] = CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_CREDIT_PRODUCT_ID"));
                //SA.DIVIDEND_CREDIT_PRODUCT,SA.DIVIDEND_CREDIT_PRODUCT_ID
            }
          observable.setFinalList(loanList);
          observable.setTxtTotAmt(txtTotAmt.getText());
          lblTotMembers.setText(CommonUtil.convertObjToStr(loanList.size()));//Added by nithya on 25-08-2020 for KD-2208
            System.out.println("totalList..nithya .. testing.. " + totalList );
          return totalList;
        }else{
          return null;
        }
    }
     
    public void initTableDataForMultipleTransfer() {
        // model=new javax.swing.table.DefaultTableModel();

        tblPensionDetails.setModel(new javax.swing.table.DefaultTableModel(
                setTableDataForMultipleTransfer(),
                new String[]{
                   "Select","Share No", "Customer Name", "Age", "Share OpDate", "Share Amount", "Share Run Period","Pension","CrActNum","CrPodType","CrProdId"}) {

            Class[] types = new Class[]{
                java.lang.Boolean.class,
                java.lang.String.class,
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
            
            boolean[] canEdit = new boolean[]{
                 true,false, false, false, false,false,false, true,false,false,false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 6) {
                    return true;
                }
                return canEdit[columnIndex];
            }
        });
        //setSizeTableData();
        //setColour();
        //setUpComboBox(tblBalanceUpdate,tblBalanceUpdate.getColumnModel().getColumn(2));
        tblPensionDetails.setCellSelectionEnabled(true);
        tblPensionDetails.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //  tblBalanceUpdatePropertyChange(evt);
            }
        });
        //calc();
        setTableModelListenerForMultipleTransfer();
        //calc();
    } 
    
    private void setTableModelListenerForMultipleTransfer() {
        try{
        tableModelListener = new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    double recTotal=0;
                    int RowCount=tblPensionDetails.getRowCount(); 
                    System.out.println("row#####"+row);
                    System.out.println("column#####"+column);
                    if (column == 6) {
                            if(tblPensionDetails.getValueAt(row, 6)!=null && !tblPensionDetails.getValueAt(row, 6).toString().equals("") && !isNumeric(tblPensionDetails.getValueAt(row, 6).toString()))
                            {
                                System.out.println("4356346436346====");
                                ClientUtil.showAlertWindow("Please enter Numeric Value!!!"); 
                                tblPensionDetails.setValueAt("", row, 6);
                                calc();
                                return;
                            }
                            if(tblPensionDetails.getValueAt(row, 6)!=null && !tblPensionDetails.getValueAt(row, 6).toString().equals("") && isNegative(CommonUtil.convertObjToDouble(tblPensionDetails.getValueAt(row, 6)) .doubleValue()))
                            {
                                System.out.println("4356346436346====");
                                ClientUtil.showAlertWindow("Negative value not allowed!!!"); 
                                tblPensionDetails.setValueAt("", row, 6);
                                calc();
                                return;
                            }
                            calc();
                            transactionUI.cancelAction(false);
                            transactionUI.setButtonEnableDisable(true);           
                            transactionUI.resetObjects();                       
                            transactionUI.setCallingTransType("CASH");
                 }
                }
            }
        };
        tblSHGDetails.getModel().addTableModelListener(tableModelListener);        
    }catch(Exception e){
            e.printStackTrace();
            }
    }
    
    public void calTotalPayment() {
        if (tblPensionDetails.getRowCount() > 0) {
            int count = tblPensionDetails.getRowCount();
            double total = 0.0;
            double selectedCount = 0.0;
            if (tblPensionDetails.getColumnName(7).equalsIgnoreCase("Pension")) {
                for (int i = 0; i < count; i++) {
                    if ((Boolean) tblPensionDetails.getValueAt(i, 0)) {
                        //tblData.getco
                        selectedCount = selectedCount + 1;
                        total = total + CommonUtil.convertObjToDouble(tblPensionDetails.getValueAt(i, 7));
                    }
                }
                txtTotAmt.setText(CommonUtil.convertObjToStr(total));
                lblSelectedMembers.setText(CommonUtil.convertObjToStr(selectedCount));//Added by nithya on 25-08-2020 for KD-2208
                lblTotMembers.setText(CommonUtil.convertObjToStr(tblPensionDetails.getRowCount()));
            }
        }
    }
    
    private void setTableFinalListForMultipleTransfer(){
        List removeActNoLst = new ArrayList();
        List tempFinalList = new ArrayList();
        finalList = observable.getFinalList();
        HashMap custMap = new HashMap();
        if(finalList!= null && finalList.size()>0){
            System.out.println("inside setTableFinalListForMultipleTransfer :: FinalList : "+finalList);
            for(int i=0; i<finalList.size(); i++){
                String custNo="";
                custMap = (HashMap)finalList.get(i);
                custNo = CommonUtil.convertObjToStr(custMap.get("SHARE_ACCT_NO"));
                System.out.println("$#@@$@$#$@$ custNo : "+custNo);
                for(int j=0; j<tblPensionDetails.getRowCount();j++) {
                    if (CommonUtil.convertObjToStr(tblPensionDetails.getValueAt(j, 1)).equals(custNo)) {
                        custMap.put("MIN_PENSION", tblPensionDetails.getValueAt(j, 7));
                        if ((((Boolean) tblPensionDetails.getValueAt(j, 0)).booleanValue())) {
                            tempFinalList.add(finalList.get(i));
                            break;
                        }
                    }                                   
                }
            }
//            System.out.println("removeActNoLst :: " + removeActNoLst);
//            for(int i=0; i<finalList.size(); i++){
//                HashMap memberMap = new HashMap();
//                memberMap = (HashMap)finalList.get(i);
//                for(int j = 0; j < removeActNoLst.size() ;  j++ ){
//                    if(CommonUtil.convertObjToStr(custMap.get("SHARE_ACCT_NO")).equalsIgnoreCase(CommonUtil.convertObjToStr(removeActNoLst.get(j)))){
//                        finalList.remove(i);
//                    }
//                }
//            }
            System.out.println("#$########## custMap : "+custMap);
            System.out.println("#$########## finalList : "+tempFinalList);
            observable.setFinalList(tempFinalList);
        }
    }
    
    
    public static boolean isNumeric(String str) {
        try {
            //Integer.parseInt(str);
            Float.parseFloat(str);
            //   System.out.println("ddd"+d);
        } catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }     
      
    public static boolean isNegative(double d) {
         return Double.compare(d, 0.0) < 0;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDebitGl;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDisplay;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnLedger;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnShow;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel4;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CComboBox cboShareType;
    private com.see.truetransact.uicomponent.CComboBox cboShareType1;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblDebitGl;
    private com.see.truetransact.uicomponent.CLabel lblGroupId;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSelectedMembers;
    private com.see.truetransact.uicomponent.CLabel lblShareType;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace46;
    private com.see.truetransact.uicomponent.CLabel lblSpace47;
    private com.see.truetransact.uicomponent.CLabel lblSpace48;
    private com.see.truetransact.uicomponent.CLabel lblSpace49;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace50;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotMembers;
    private com.see.truetransact.uicomponent.CLabel lblTotalPayment;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitException;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitReject;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panDebitGl;
    private com.see.truetransact.uicomponent.CPanel panGroupId;
    private com.see.truetransact.uicomponent.CPanel panGroupId1;
    private com.see.truetransact.uicomponent.CPanel panGroupIdDetail;
    private com.see.truetransact.uicomponent.CPanel panPensionMultiPosting;
    private com.see.truetransact.uicomponent.CPanel panSHGDetails;
    private com.see.truetransact.uicomponent.CPanel panSHGTableDetails;
    private com.see.truetransact.uicomponent.CPanel panSHGTransDetails;
    private com.see.truetransact.uicomponent.CPanel panShareType;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTotalPayment;
    private com.see.truetransact.uicomponent.CPanel panTransactionDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdgEFTProductGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIsLapsedGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPayableBranchGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrintServicesGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSeriesGR;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CScrollPane srpPensionDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpSHGTable;
    private com.see.truetransact.uicomponent.CTabbedPane tabSHGTransaction;
    private com.see.truetransact.uicomponent.CTable tblPensionDetails;
    private com.see.truetransact.uicomponent.CTable tblSHGDetails;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CTextField txtMemberNo;
    private com.see.truetransact.uicomponent.CTextField txtPayment;
    private com.see.truetransact.uicomponent.CTextField txtTotAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotalPayment;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] arg){
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        PensionSchemeUI gui = new PensionSchemeUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}