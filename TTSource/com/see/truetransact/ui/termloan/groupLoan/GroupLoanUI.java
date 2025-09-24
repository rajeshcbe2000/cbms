  /*
   * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
   *
   * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
   * 
   * GroupLoanUI.java
   * Created on November 26, 2003, 11:27 AM
   *
   */

package com.see.truetransact.ui.termloan.groupLoan;

/**
 *
 * @author Suresh
 *
 **/
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
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import java.util.ArrayList;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;



public class GroupLoanUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField{
    private final static ClientParseException parseException = ClientParseException.getInstance();
    //    private RemittanceProductRB resourceBundle = new RemittanceProductRB();
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.mdsapplication.MDSApplicationRB", ProxyParameters.LANGUAGE);
//    final MDSApplicationMRB objMandatoryMRB = new MDSApplicationMRB();
    TransactionUI transactionUI = new TransactionUI();
    
    private HashMap mandatoryMap;
    GroupLoanOB observable = null;
    final String AUTHORIZE="Authorize";
    private String viewType = new String();
    private List finalList = null;
    public int selectedRow = -1;
    private boolean isFilled = false;
    Date currDt = null;
    private TableModelListener tableModelListener;
    boolean fromAuthorizeUI = false;    
    AuthorizeListUI authorizeListUI = null;
    private boolean dailyProduct = false;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    /** Creates new form BeanForm */
    public GroupLoanUI() {
        initComponents();
        settingupUI();
        tabSHGTransaction.resetVisits();
        panTransactionDetails.add(transactionUI);
        transactionUI.setSourceScreen("GROUP_LOAN");
        observable.setTransactionOB(transactionUI.getTransactionOB());
        currDt = ClientUtil.getCurrentDate();
    }
    
    private void settingupUI(){
        setFieldNames();
        observable = new GroupLoanOB();
        //initComponentData();
        setMaximumLength();
        setButtonEnableDisable();
        ClientUtil.enableDisable(panSHGDetails,false);
        btnDisplay.setEnabled(false);
//        txtPayment.setVisible(false);
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
        btnGroupId.setName("btnGroupId");
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
        lblTotalPayment.setName("lblTotalPayment");
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
        txtAcctNum.setName("txtGroupId");
        txtTotalPayment.setName("txtTotalPayment");
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
    
    //private void initComponentData() {
    //    try{
     //       TableCellEditor editor = new DefaultCellEditor(txtPayment);
     //       SimpleTableModel  stm = new SimpleTableModel((ArrayList)observable.getTableList(),(ArrayList)observable.getTableTitle());
       //     tblSHGDetails.setModel(stm);
       //     tblSHGDetails.getColumnModel().getColumn(8).setCellEditor(editor);
        //    tblSHGDetails.revalidate();
        //}catch(ClassCastException e){
        //    parseException.logException(e,true);
       // }
   // }
    private void setMaximumLength(){
        txtTotalPayment.setValidation(new CurrencyValidation());
        txtAcctNum.setAllowAll(true);
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
        txtAcctNum = new com.see.truetransact.uicomponent.CTextField();
        btnGroupId = new com.see.truetransact.uicomponent.CButton();
        lblGroupId = new com.see.truetransact.uicomponent.CLabel();
        lblName = new com.see.truetransact.uicomponent.CLabel();
        panGroupId1 = new com.see.truetransact.uicomponent.CPanel();
        btnDisplay = new com.see.truetransact.uicomponent.CButton();
        tdtTransDt = new com.see.truetransact.uicomponent.CDateField();
        panTotalPayment = new com.see.truetransact.uicomponent.CPanel();
        txtTotalPayment = new com.see.truetransact.uicomponent.CTextField();
        lblTotalPayment = new com.see.truetransact.uicomponent.CLabel();
        txtPayment = new com.see.truetransact.uicomponent.CTextField();
        lblODBal = new com.see.truetransact.uicomponent.CLabel();
        txtODBal = new com.see.truetransact.uicomponent.CTextField();
        panTransactionDetails = new com.see.truetransact.uicomponent.CPanel();
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

        tblSHGDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Credit Card No", "Customer Id", "Customer Name", "Principal", "Interest", "Penal", "Charge", "Total", "Receipt Amt"
            }
        ));
        tblSHGDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(796, 196));
        tblSHGDetails.setPreferredSize(new java.awt.Dimension(800, 2000));
        tblSHGDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSHGDetailsMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tblSHGDetailsMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSHGDetailsMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblSHGDetailsMouseReleased(evt);
            }
        });
        tblSHGDetails.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblSHGDetailsFocusLost(evt);
            }
        });
        tblSHGDetails.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblSHGDetailsKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tblSHGDetailsKeyTyped(evt);
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

        txtAcctNum.setBackground(new java.awt.Color(204, 204, 204));
        txtAcctNum.setAllowAll(true);
        txtAcctNum.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcctNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAcctNumActionPerformed(evt);
            }
        });
        txtAcctNum.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcctNumFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGroupId.add(txtAcctNum, gridBagConstraints);

        btnGroupId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnGroupId.setEnabled(false);
        btnGroupId.setMaximumSize(new java.awt.Dimension(21, 21));
        btnGroupId.setMinimumSize(new java.awt.Dimension(21, 21));
        btnGroupId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnGroupId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGroupIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGroupId.add(btnGroupId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panGroupIdDetail.add(panGroupId, gridBagConstraints);

        lblGroupId.setText("Group Loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        panGroupIdDetail.add(lblGroupId, gridBagConstraints);

        lblName.setForeground(new java.awt.Color(0, 0, 204));
        lblName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblName.setMaximumSize(new java.awt.Dimension(350, 18));
        lblName.setMinimumSize(new java.awt.Dimension(350, 18));
        lblName.setPreferredSize(new java.awt.Dimension(350, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 18;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
        panGroupIdDetail.add(lblName, gridBagConstraints);

        panGroupId1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 21;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panGroupIdDetail.add(panGroupId1, gridBagConstraints);

        btnDisplay.setText("Display");
        btnDisplay.setMaximumSize(new java.awt.Dimension(100, 21));
        btnDisplay.setMinimumSize(new java.awt.Dimension(100, 21));
        btnDisplay.setPreferredSize(new java.awt.Dimension(100, 21));
        btnDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisplayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 24;
        gridBagConstraints.gridy = 0;
        panGroupIdDetail.add(btnDisplay, gridBagConstraints);

        tdtTransDt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tdtTransDtMouseReleased(evt);
            }
        });
        tdtTransDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtTransDtFocusLost(evt);
            }
        });
        tdtTransDt.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tdtTransDtPropertyChange(evt);
            }
        });
        tdtTransDt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tdtTransDtKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 23;
        gridBagConstraints.gridy = 0;
        panGroupIdDetail.add(tdtTransDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panSHGDetails.add(panGroupIdDetail, gridBagConstraints);

        panTotalPayment.setMinimumSize(new java.awt.Dimension(825, 25));
        panTotalPayment.setPreferredSize(new java.awt.Dimension(825, 25));
        panTotalPayment.setLayout(new java.awt.GridBagLayout());

        txtTotalPayment.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalPayment.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panTotalPayment.add(txtTotalPayment, gridBagConstraints);

        lblTotalPayment.setText("Total Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 14, 0, 0);
        panTotalPayment.add(lblTotalPayment, gridBagConstraints);

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

        lblODBal.setText("OverDraft Balance ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panTotalPayment.add(lblODBal, gridBagConstraints);

        txtODBal.setBackground(new java.awt.Color(204, 204, 204));
        txtODBal.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panTotalPayment.add(txtODBal, gridBagConstraints);

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

        tabSHGTransaction.addTab("Group Loan Transaction", panSHGTransDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabSHGTransaction, gridBagConstraints);

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
                if (col != 8){
                    return false;
                }else{
                    selectedRow = row;
//                    if(selectedRow != -1)
                    System.out.println("isCellEditable");
                    checkingPaymentAmount(selectedRow);
                    calc();
                    return true;
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
    
    public void checkingPaymentAmount(int selectedRow){
        double paymentAmt = 0;
        double totPayableAmt = 0;
        if(tblSHGDetails.getValueAt(selectedRow, 8)!=null && !tblSHGDetails.getValueAt(selectedRow, 8).toString().equals("")){
                if(!isNumeric(tblSHGDetails.getValueAt(selectedRow, 8).toString()) 
               && !isLessThanZero(CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(selectedRow, 8).toString()).doubleValue())){
                    ClientUtil.showMessageWindow("Amount Should be greater than zero and numeric...");
                    tblSHGDetails.setValueAt("", selectedRow, 8);
                    return;
                }
        }
        paymentAmt = CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(selectedRow, 8).toString()).doubleValue();
        if(paymentAmt>0){
            totPayableAmt = CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(selectedRow,7).toString()).doubleValue();
            System.out.println("paymentAmt3333>>>>"+paymentAmt);
            if( totPayableAmt < paymentAmt ){
                ClientUtil.showMessageWindow("Receipt Amount Cannot exceeds the total amount..." + totPayableAmt);
                tblSHGDetails.setValueAt("", selectedRow, 8);
                //((SimpleTableModel)tblSHGDetails.getModel()).fireTableDataChanged();
                calc();
                return;
            }
        }
    }
    public void calc(){
        double totPayment = 0;
        if(tblSHGDetails.getRowCount()>0){
            for (int i=0; i< tblSHGDetails.getRowCount(); i++) {
                totPayment = totPayment + CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(i, 8).toString()).doubleValue();
                txtTotalPayment.setText(CurrencyValidation.formatCrore(String.valueOf(totPayment)));
            }
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            //String debitAcctNo=CommonUtil.convertObjToStr(transactionUI.getCallingTransAcctNo());
            transactionUI.resetObjects();
            //transactionUI.setCallingTransAcctNo(debitAcctNo);
            transactionUI.setCallingAmount(CommonUtil.convertObjToStr(txtTotalPayment.getText()));
            transactionUI.setCallingTransType("CASH");
            transactionUI.setCallingApplicantName(CommonUtil.convertObjToStr(lblName.getText()));
            //if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW )
             //   transactionUI.setCallingTransAcctNo(CommonUtil.convertObjToStr(observable.getCallingTransAcctNo()));
        }
    }
    
    public void calcToatal(){               // Edit Or Authorize Time
        double totPayment = 0;
        if(tblSHGDetails.getRowCount()>0){
            for (int i=0; i< tblSHGDetails.getRowCount(); i++) {
                totPayment = totPayment + CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(i, 8).toString()).doubleValue();
                txtTotalPayment.setText(CurrencyValidation.formatCrore(String.valueOf(totPayment)));
            }
            transactionUI.setCallingAmount(CommonUtil.convertObjToStr(txtTotalPayment.getText()));
            tblSHGDetails.setEnabled(false);
        }
    }
    
    public void calcLoanBal(){               // Edit Or Authorize Time88888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888
        double totLoanBal = 0;
        if(tblSHGDetails.getRowCount()>0){
            for (int i=0; i< tblSHGDetails.getRowCount(); i++) {
                totLoanBal = totLoanBal + CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(i, 4).toString()).doubleValue();
                //txtODBal.setText(CurrencyValidation.formatCrore(String.valueOf(totLoanBal)));
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
            singleAuthorizeMap.put("ACT_NUM", observable.getTxtAcctNum());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT",ClientUtil.getCurrentDateWithTime());
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
                mapParam.put(CommonConstants.MAP_NAME, "getGroupLoanReceiptForAuthCashUI");
            } else {
                mapParam.put(CommonConstants.MAP_NAME, "getGroupLoanReceiptForAuthUI");
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
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){                
                observable.setDailyGrpLoan(dailyProduct);           
            }
            observable.setSelectedBranchID(getSelectedBranchID());
            observable.doAction();
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.displayDetails("Group loan Receipts");
                }
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                    authorizeListUI.setFocusToTable();                    
                    authorizeListUI.displayDetails("Group loan Receipts");
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
        txtAcctNum.setEnabled(true);
        btnGroupId.setEnabled(true);
        btnDisplay.setEnabled(true);
        tdtTransDt.setEnabled(true);
        setButtonEnableDisable();
        ClientUtil.clearAll(this);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        transactionUI.setSaveEnableValue(ClientConstants.ACTIONTYPE_NEW);
        tdtTransDt.setDateValue(DateUtil.getStringDate(currDt));
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
       if(DateUtil.getDateMMDDYYYY(tdtTransDt.getDateValue()).equals(currDt)){
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                if(!dailyProduct){
            int transactionSize = 0 ;
                if(transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(txtTotalPayment.getText()).doubleValue() > 0){
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
                    return;
                }else {
                    if(CommonUtil.convertObjToDouble(txtTotalPayment.getText()).doubleValue()>0){
                        transactionSize = (transactionUI.getOutputTO()).size();
                        if(transactionSize != 1 && CommonUtil.convertObjToDouble(txtTotalPayment.getText()).doubleValue()>0){
                            ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction") ;
                            return;
                        }else{
                            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                        }
                    }else if(transactionUI.getOutputTO().size()>0){
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    }
                }
                if(transactionSize == 0 && CommonUtil.convertObjToDouble(txtTotalPayment.getText()).doubleValue()>0){
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
                    return;
                }else if(transactionSize != 0 ){
                    if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                        return;
                    }
                     if(transactionUI.getOutputTO().size()>0){
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                        savePerformed();
                    }
                }
            }else{
                    savePerformed();
                }
            }
        }else{
           ClientUtil.showAlertWindow("Transaction date should be application date...") ;
           return;
        }
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed(){
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            if(!dailyProduct){
                setTableFinalList();
            }else{
                boolean amountFlag = false;
                if(tblSHGDetails.getRowCount()>0){
//                    for(int i = 0;i<tblSHGDetails.getRowCount();i++){
//                        if(CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(i, 7))>0 &&
//                           CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(i, 9))>0){
//                                amountFlag = true;
//                                break;
//                        }
//                    } 
//                    if(!amountFlag){                    
//                        ClientUtil.showMessageWindow("No balace in debit account or Loan !!!");
//                        return;
//                    }
                    setDailyTableFinalList();
                    if(!(finalList!=null && finalList.size()>0)){                      
                        ClientUtil.displayAlert("Please Select and Enter The Debit Amount!!");
                        btnDisplayActionPerformed(null);
                        return;
                    }
                }else{
                    ClientUtil.showMessageWindow("Please process the data first!!!");
                    return;
                }                
                observable.setDailyGrpLoan(dailyProduct);
            }
        } 
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.doAction();
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size()>0) {
                        displayTransDetailNew(observable.getProxyReturnMap());
            }else{
                ClientUtil.showMessageWindow("Transaction Aborted!!!");
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
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                    }
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                    break;
//                    transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
//                            + "   Batch Id : " + transMap.get("BATCH_ID")
//                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
//                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
//                    if (actNum != null && !actNum.equals("")) {
//                       // System.out.println("haaaiii22....>>>bb");
//                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
//                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
//                    } else {
//                      //  System.out.println("haaaiii22....>>>cc");
//                        transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
//                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
//                    }                    
                }
                transferDisplayStr += "   Batch Id : " + transMap.get("BATCH_ID");
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
                                ttIntgration.integrationForPrint("MDSReceiptsTransfer",false);
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
                custNo = CommonUtil.convertObjToStr(custMap.get("CUST_ID"));
                System.out.println("$#@@$@$#$@$ custNo : "+custNo);
                for(int j=0; j<tblSHGDetails.getRowCount();j++) {
                    if (CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(j, 1)).equals(custNo)) {
                        custMap.put("RECEIPT",tblSHGDetails.getValueAt(j, 8));
                    }
                                      
                }
            }
            System.out.println("#$########## custMap : "+custMap);
            System.out.println("#$########## finalList : "+finalList);
            observable.setFinalList(finalList);
        }
    }
    
    private void setDailyTableFinalList(){
        finalList = observable.getFinalList();
        HashMap custMap = new HashMap();
        if(finalList!= null && finalList.size()>0){
            System.out.println("#$@$#@$@$@ FinalList : "+finalList);
            for(int i=0; i<finalList.size(); i++){
                String custNo="";
                double loanBal=0.0,actBal = 0.0;
                custMap = (HashMap)finalList.get(i);                
                custNo = CommonUtil.convertObjToStr(custMap.get("CUST_ID"));
                //System.out.println("$#@@$@$#$@$ custNo : "+custNo);                
                //if(loanBal>0 && actBal>0){
                for(int j=0; j<tblSHGDetails.getRowCount();j++) {  
                    if (CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(j, 2)).equals(custNo)){
                        if ((!((Boolean) tblSHGDetails.getValueAt(j, 0)).booleanValue() ||
                                CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(j, 10))==0 || 
                                CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(j, 9)).equals(""))) {
                                    finalList.remove(i--);
                        }else{
                            if(CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(j, 10)) <= CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(j, 8))){// Added by nithya on 07-12-2017 for 0008349
                                custMap.put("TRANS_AMT",tblSHGDetails.getValueAt(j, 10));
                            }else{
                                custMap.put("TRANS_AMT",tblSHGDetails.getValueAt(j, 8));
                            }
                            
                            custMap.put("INTEREST",tblSHGDetails.getValueAt(j, 5));
                        }
                    }
                }
                //}                
            }
            System.out.println("#$########## custMap daily: "+custMap);
            System.out.println("#$########## finalList : daily"+finalList);
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
        btnGroupId.setEnabled(false);
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
        clearTable();
        //__ Make the Screen Closable..
        setModified(false);
        lblName.setText("");
        tdtTransDt.setDateValue("");
        btnDisplay.setEnabled(false);
        dailyProduct = false;
    }//GEN-LAST:event_btnCancelActionPerformed
    private void clearTable(){
        observable.resetForm();
        if(tblSHGDetails.getRowCount()>0){
        ((DefaultTableModel) tblSHGDetails.getModel()).setRowCount(0);
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

private void btnDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisplayActionPerformed
// TODO add your handling code here:
    if(txtAcctNum.getText().length()>0){
            clearTable();
            HashMap whereMap = new HashMap();
            whereMap.put("ACT_NUM",txtAcctNum.getText());
            whereMap.put("TRANS_DT", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtTransDt.getDateValue())));
            //observable.getTableDetails(whereMap);
            observable.setTxtAcctNum(txtAcctNum.getText());
            if(dailyProduct){
                initTableDailyLoanData(null);
                transactionUI.setMainEnableDisable(false);
                ClientUtil.enableDisable(panTransactionDetails, false); 
            }else{
                initTableData();
                transactionUI.setMainEnableDisable(true);
                ClientUtil.enableDisable(panTransactionDetails, true); 
            }
            txtTotalPayment.setText("0.0");      
            txtPayment.setEnabled(true);
            tblSHGDetails.setEnabled(true);
            //((SimpleTableModel)tblSHGDetails.getModel()).fireTableDataChanged();
            //return;
        }else{
            ClientUtil.displayAlert("Account number should not be null!!");
    }
}//GEN-LAST:event_btnDisplayActionPerformed

private void btnGroupIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGroupIdActionPerformed
        // TODO add your handling code here:
        callView("GROUP_ID");  
}//GEN-LAST:event_btnGroupIdActionPerformed

private void txtAcctNumFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcctNumFocusLost
        // TODO add your handling code here:
        if(txtAcctNum.getText().length()>0){
            HashMap whereMap = new HashMap();
            whereMap.put("ACT_NUM",txtAcctNum.getText());
            whereMap.put("BRANCH_CODE", getSelectedBranchID());
            List lst=ClientUtil.executeQuery("getGroupLoanCreditDetails",whereMap);
            if(lst !=null && lst.size()>0){
                viewType = "GROUP_ID";
                whereMap=(HashMap)lst.get(0);
                    fillData(whereMap);
                    lst=null;
                    whereMap=null;
                    btnGroupId.setSelected(true);
            }else{
                ClientUtil.displayAlert("Invalid Group Loan or Payment not completed ");
                txtAcctNum.setText("");
                observable.resetForm();
            }
        }
}//GEN-LAST:event_txtAcctNumFocusLost

private void txtAcctNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAcctNumActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtAcctNumActionPerformed

private void tblSHGDetailsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblSHGDetailsKeyTyped
        // TODO add your handling code here:
        //System.out.println("tblSHGDetailsKeyTyped");
         /////checkingPaymentAmount(selectedRow);
}//GEN-LAST:event_tblSHGDetailsKeyTyped

private void tblSHGDetailsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblSHGDetailsKeyReleased
        // TODO add your handling code here:
       // System.out.println("tblSHGDetailsKeyReleased");
       ////checkingPaymentAmount(selectedRow);
}//GEN-LAST:event_tblSHGDetailsKeyReleased

private void tblSHGDetailsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblSHGDetailsFocusLost
        // TODO add your handling code here:
        //System.out.println("tblSHGDetailsFocusLost");
        //checkingPaymentAmount(selectedRow);
        
}//GEN-LAST:event_tblSHGDetailsFocusLost

private void tblSHGDetailsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSHGDetailsMouseReleased
        // TODO add your handling code here:
        //System.out.println("tblSHGDetailsMouseReleased");
        ////checkingPaymentAmount(selectedRow);
        
}//GEN-LAST:event_tblSHGDetailsMouseReleased

private void tblSHGDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSHGDetailsMousePressed
        // TODO add your handling code here:
        //System.out.println("tblSHGDetailsMousePressed");
        //checkingPaymentAmount(selectedRow);
        
}//GEN-LAST:event_tblSHGDetailsMousePressed

private void tblSHGDetailsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSHGDetailsMouseExited
        // TODO add your handling code here:
        //System.out.println("tblSHGDetailsMouseExited");
      // // checkingPaymentAmount(selectedRow);
       
}//GEN-LAST:event_tblSHGDetailsMouseExited

private void tblSHGDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSHGDetailsMouseClicked
        // TODO add your handling code here:
     if(evt.getClickCount()==2){
        com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
        HashMap paramMap = new HashMap();
        int column = 0;
        if((column=tblSHGDetails.getSelectedColumn())==9){
            if(CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(), column))!=null &&
              !CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(), column)).equals("")){
                paramMap.put("AccountNo", CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(), column))); 
                paramMap.put("FromDate", currDt.clone());
                paramMap.put("ToDate", currDt.clone());
                paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                ttIntgration.setParam(paramMap);            
                ttIntgration.integration("OALedger"); 
            }
       }else if((column=tblSHGDetails.getSelectedColumn())==2){
            if(CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(), column))!=null &&
              !CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(), column)).equals("")){
                paramMap.put("LoanNo", CommonUtil.convertObjToStr(txtAcctNum.getText())); 
                paramMap.put("CustomerId", CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(), column))); 
                ttIntgration.setParam(paramMap);   
                ttIntgration.integration("GrpLoanLedgerCustomerWise");        
            }
       }     
    }
}//GEN-LAST:event_tblSHGDetailsMouseClicked

private void tdtTransDtPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tdtTransDtPropertyChange
// TODO add your handling code here:
    System.out.println("tdtTransDtPropertyChangetdtTransDtPropertyChange");
    
}//GEN-LAST:event_tdtTransDtPropertyChange

private void txtPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPaymentActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtPaymentActionPerformed

private void txtPaymentFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPaymentFocusLost
// TODO add your handling code here:
}//GEN-LAST:event_txtPaymentFocusLost

private void tdtTransDtMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdtTransDtMouseReleased
// TODO add your handling code here:    
}//GEN-LAST:event_tdtTransDtMouseReleased

private void tdtTransDtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tdtTransDtKeyPressed
// TODO add your handling code here:
    System.out.println("tdtTransDtPropertyChangetdtTransDtPropertyChange");
    ClientUtil.showMessageWindow("Press Display!!!"); 
    btnDisplay.setEnabled(true);
}//GEN-LAST:event_tdtTransDtKeyPressed

private void tdtTransDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtTransDtFocusLost
// TODO add your handling code here:
    btnDisplayActionPerformed(null);
}//GEN-LAST:event_tdtTransDtFocusLost
    
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
            viewMap.put(CommonConstants.MAP_NAME, "getGroupLoanCreditDetails");
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
            if(viewType == "GROUP_ID"){
                clearTable();
                txtAcctNum.setText(CommonUtil.convertObjToStr(hashMap.get("ACT_NUM")));
                lblName.setText(CommonUtil.convertObjToStr(hashMap.get("CUSTONER")));
                observable.setTxtAcctNum(txtAcctNum.getText());
                HashMap checkMap = new HashMap();
                checkMap.put("LINK_BATCH_ID", txtAcctNum.getText());
                checkMap.put("TRANS_DT", currDt.clone());
                checkMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                checkMap.put("ACT_NUM", txtAcctNum.getText());
                List checkList = ClientUtil.executeQuery("getPendingTransactionTransferAD", checkMap);
                if(checkList.size()>0){
                    ClientUtil.showMessageWindow("Authorization pending for the Group Loan");
                    txtAcctNum.setText("");
                    lblName.setText("");
                    return;
                }
                List prodList = ClientUtil.executeQuery("getDailyGroupLoan", checkMap);
                if(prodList.size()>0){                    
                      dailyProduct = true;                    
                }else{
                      dailyProduct = false;
                }
                //observable.getTableDetails(hashMap);
                //calcLoanBal();
                //txtTotalPayment.setText("0.0");      
                //txtPayment.setEnabled(true);
                //tblSHGDetails.setEnabled(true);
                //((SimpleTableModel)tblSHGDetails.getModel()).fireTableDataChanged();
                //return;
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                this.setButtonEnableDisable();
                txtAcctNum.setText(CommonUtil.convertObjToStr(hashMap.get("SHG_ID")));
                observable.setTxtGroupId(CommonUtil.convertObjToStr(hashMap.get("SHG_ID")));
                observable.setShgTransId(CommonUtil.convertObjToStr(hashMap.get("SHG_TRANS_ID")));
                observable.getData(hashMap);
                //calcLoanBal();
                ClientUtil.enableDisable(panSHGTransDetails,false);
                calcToatal();
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                this.setButtonEnableDisable();
                txtAcctNum.setText(CommonUtil.convertObjToStr(hashMap.get("ACT_NUM")));
                tdtTransDt.setDateValue(DateUtil.getStringDate(currDt));
                observable.setTxtAcctNum(txtAcctNum.getText());
                hashMap.put("TRANS_DT",currDt);
                System.out.println("hashMap.hashMap>>>>>"+hashMap);
                List prodList = ClientUtil.executeQuery("getDailyGroupLoan", hashMap);
                if(prodList.size()>0){                    
                      dailyProduct = true;   
                      initTableDailyLoanData(hashMap);
                }else{
                      dailyProduct = false;
                      initTableAuthData(hashMap);
                      observable.getData(hashMap);      
                }                          
                ClientUtil.enableDisable(panSHGTransDetails,false);
                calcToatal();
                //calcLoanBal();
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
    
    
    public static boolean isLessThanZero(Double amount) {
        boolean flag = false;
        System.out.println("amount------------#"+amount);
        try {
            if(amount<0)
                flag = true;
         } catch (Exception nfe) {
            nfe.printStackTrace();
        }  
        System.out.println("falg 22222222222222"+flag);
        return flag;
    }
    
    public static boolean isNegative(double d) {
         return Double.compare(d, 0.0) < 0;
    }
    
        public void initTableData() {
        // model=new javax.swing.table.DefaultTableModel();

        tblSHGDetails.setModel(new javax.swing.table.DefaultTableModel(
                setTableData(),
                new String[]{
                    "Credit Card No","Customer Id", "Customer Name", "Principal","Interest","Penal","Charge","Total","Receipt Amt"}) {

            Class[] types = new Class[]{
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
                 false,false, false, false, false, false,false,false,true
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 8) {
                    return true;
                }
                return canEdit[columnIndex];
            }
        });
        //setFont();
        //setSizeTableData();
        //setColour();
        //setUpComboBox(tblBalanceUpdate,tblBalanceUpdate.getColumnModel().getColumn(2));
        tblSHGDetails.setCellSelectionEnabled(true);
        tblSHGDetails.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //  tblBalanceUpdatePropertyChange(evt);
            }
        });

        setTableModelListener();
        calc();
    }
        
    public void initTableDailyLoanData(HashMap map) {
        // model=new javax.swing.table.DefaultTableModel();

        tblSHGDetails.setModel(new javax.swing.table.DefaultTableModel(
                setTableDailyLoanData(map),
                new String[]{
                    "Select","Member No","Customer Id", "Customer Name", "Principal","Interest","Penal","Charge","Total","Debit ActNo","Act Balance"}) {

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
                 true,false,false, false, false, true, false,false,false,false,true
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 9) {
                   if(CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(rowIndex, columnIndex))!=0){
                        return true;
                    }else{
                       return false;
                   }
                }
                if (columnIndex == 0 || columnIndex == 5) {
                    return true;
                }
                return canEdit[columnIndex];
            }
        });
        //setFont();
        //setSizeTableData();
        //setColour();
        //setUpComboBox(tblBalanceUpdate,tblBalanceUpdate.getColumnModel().getColumn(2));
        tblSHGDetails.setCellSelectionEnabled(true);
        tblSHGDetails.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //  tblBalanceUpdatePropertyChange(evt);
            }
        });

        setTableModelListener();
        calc();
    }
    
    private void setFont() {
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(Color.BLACK);
                setFont(new java.awt.Font("Tahoma", Font.BOLD, 12));
                setForeground(Color.BLACK);
                this.setOpaque(true);
                return this;
            }
        };
        tblSHGDetails.setDefaultRenderer(Object.class, renderer);
    }
    
    public void initTableAuthData(HashMap editMap) {
           tblSHGDetails.setModel(new javax.swing.table.DefaultTableModel(
                setTableAuthData(editMap),
                new String[]{
                    "Credit Card No","Customer Id", "Customer Name", "Principal","Interest","Penal","Charge","Total","Receipt Amt"}) {

            Class[] types = new Class[]{
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
                false, false, false, false
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
            calcToatal();
        }
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
                    if(!dailyProduct){
                    if (column == 8) {
                            if(tblSHGDetails.getValueAt(row, 8)!=null && !tblSHGDetails.getValueAt(row, 8).toString().equals("") && !isNumeric(tblSHGDetails.getValueAt(row, 8).toString()))
                            {
                                System.out.println("4356346436346====");
                                ClientUtil.showAlertWindow("Please enter Numeric Value!!!"); 
                                tblSHGDetails.setValueAt("", row, 8);
                                return;
                            }
                            if(tblSHGDetails.getValueAt(row, 8)!=null && !tblSHGDetails.getValueAt(row, 8).toString().equals("") && isNegative(CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(row, 8)) .doubleValue()))
                            {
                                System.out.println("4356346436346====");
                                ClientUtil.showAlertWindow("Negative value not allowed!!!"); 
                                tblSHGDetails.setValueAt("", row, 8);
                                return;
                            }
                            double receipt =  CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(row, 8)) .doubleValue();
                            double totalamt =  CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(row, 7)) .doubleValue();
                            System.out.println("negativeeee@@@@@@@@@@@@@@"+isNegative(receipt));
                            System.out.println("value#######"+tblSHGDetails.getValueAt(row, column));
                            if(receipt > 0){
                                 if(totalamt < receipt ){
                                    ClientUtil.showMessageWindow("Receipt Amount Cannot exceeds the total amount..." + totalamt);
                                    tblSHGDetails.setValueAt("", row, 8);
                                    calc();
                                    return;
                                }
                            }
                       }   
                    }
                       if (column == 10) {
                            if(tblSHGDetails.getValueAt(row, 10)!=null && !tblSHGDetails.getValueAt(row, 10).toString().equals("") && !isNumeric(tblSHGDetails.getValueAt(row, 10).toString()))
                            {
                                System.out.println("4356346436346====");
                                ClientUtil.showAlertWindow("Please enter Numeric Value!!!"); 
                                tblSHGDetails.setValueAt("", row, 10);
                                return;
                            }
                            if(tblSHGDetails.getValueAt(row, 10)!=null && !tblSHGDetails.getValueAt(row, 10).toString().equals("") && isNegative(CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(row, 10)) .doubleValue()))
                            {
                                System.out.println("4356346436346====");
                                ClientUtil.showAlertWindow("Negative value not allowed!!!"); 
                                tblSHGDetails.setValueAt("", row, 10);
                                return;
                            }
                            if(CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(row, 10)) .doubleValue()>0 && !(tblSHGDetails.getValueAt(row, 9)!=null && !tblSHGDetails.getValueAt(row, 9).toString().equals(""))){
                                ClientUtil.showMessageWindow("Account Number should not be empty!!!");
                                tblSHGDetails.setValueAt(0, row, 10);
                                return;
                            }
                            if(!(tblSHGDetails.getValueAt(row, 10)!=null && !tblSHGDetails.getValueAt(row, 10).toString().equals("")))
                            {
                                System.out.println("4356346436346====");
                                ClientUtil.showAlertWindow("Cant't enter null value!!!\n 1 will be set as defult value..Change it!!"); 
                                tblSHGDetails.setValueAt(1, row, 10);
                                return;
                            }
                       }
                       
                       if (column == 5) {
                            if(tblSHGDetails.getValueAt(row, 5)!=null && !tblSHGDetails.getValueAt(row, 5).toString().equals("") && !isNumeric(tblSHGDetails.getValueAt(row, 5).toString()))
                            {
                                System.out.println("4356346436346====");
                                ClientUtil.showAlertWindow("Please enter Numeric Value!!!"); 
                                tblSHGDetails.setValueAt("", row, 5);
                                return;
                            }
                            if(tblSHGDetails.getValueAt(row, 5)!=null && !tblSHGDetails.getValueAt(row, 5).toString().equals("") && isNegative(CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(row, 5)) .doubleValue()))
                            {
                                System.out.println("4356346436346====");
                                ClientUtil.showAlertWindow("Negative value not allowed!!!"); 
                                tblSHGDetails.setValueAt("", row, 5);
                                return;
                            }
//                            if(CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(row, 5)) .doubleValue()>0 && !(tblSHGDetails.getValueAt(row, 5)!=null && !tblSHGDetails.getValueAt(row, 5).toString().equals(""))){
//                                ClientUtil.showMessageWindow("Account Number should not be empty!!!");
//                                tblSHGDetails.setValueAt(0, row, 9);
//                                return;
//                            }
                            if(!(tblSHGDetails.getValueAt(row, 5)!=null && !tblSHGDetails.getValueAt(row, 5).toString().equals("")))
                            {
                                System.out.println("4356346436346====");
                                ClientUtil.showAlertWindow("Cant't enter null value!!!\n 0 will be set as defult value..Change it!!"); 
                                tblSHGDetails.setValueAt(0, row, 5);
                                return;
                            }
                            if((tblSHGDetails.getValueAt(row, 5)!=null && !tblSHGDetails.getValueAt(row, 5).toString().equals("")))
                            {      
                                tblSHGDetails.setValueAt((CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(row, 5))+CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(row, 4))), row, 8);
                            }
                       }
                                              
                       for (int i = 0; i < tblSHGDetails.getRowCount(); i++) {
                           recTotal =  recTotal + CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(i, 8)).doubleValue();
                       }                        
                       if(!dailyProduct){
                            txtTotalPayment.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(recTotal)));      
                            transactionUI.cancelAction(false);
                            transactionUI.setButtonEnableDisable(true);           
                            transactionUI.resetObjects();
                            transactionUI.setCallingAmount(CommonUtil.convertObjToStr(txtTotalPayment.getText()));
                            transactionUI.setCallingTransType("CASH");
                            transactionUI.setCallingApplicantName(CommonUtil.convertObjToStr(lblName.getText()));
                       }
                 }
            }
        };
        tblSHGDetails.getModel().addTableModelListener(tableModelListener);        
    }catch(Exception e){
            e.printStackTrace();
            }
    }
    
    private Object[][] setTableData() {
        HashMap whereMap = new HashMap();
        HashMap dataMap = new HashMap();
        whereMap.put("BRANCH_CODE", getSelectedBranchID());
        whereMap.put("TRANS_DT", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtTransDt.getDateValue())));
        whereMap.put("ACT_NUM", (String) txtAcctNum.getText());           
        List loanList = ClientUtil.executeQuery("getGroupLoanTableDetails", whereMap);
        if (loanList != null && loanList.size() > 0) {
            Object totalList[][] = new Object[loanList.size()][9];
            for (int j = 0; j < loanList.size(); j++) {
                double princ_Due = 0.0;
                double int_Due = 0.0;
                double penal = 0.0;
                double charge = 0.0;
                double total_Due = 0.0;
                dataMap = (HashMap) loanList.get(j);
                System.out.println("processMap####"+dataMap);
                totalList[j][0] = CommonUtil.convertObjToStr(dataMap.get("CREDIT_CARD_NO"));
                totalList[j][1] = CommonUtil.convertObjToStr(dataMap.get("CUST_ID"));
                totalList[j][2] = CommonUtil.convertObjToStr(dataMap.get("CUSTOMER"));
                totalList[j][3] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(dataMap.get("PRIN_DUE")));
                totalList[j][4] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(dataMap.get("INTEREST")));
                totalList[j][5] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(dataMap.get("PENAL")));
                totalList[j][6] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(dataMap.get("CHARGES")));
                princ_Due = CommonUtil.convertObjToDouble(dataMap.get("PRIN_DUE")).doubleValue();
                int_Due = CommonUtil.convertObjToDouble(dataMap.get("INTEREST")).doubleValue();
                penal = CommonUtil.convertObjToDouble(dataMap.get("PENAL")).doubleValue();
                charge = CommonUtil.convertObjToDouble(dataMap.get("CHARGES")).doubleValue();
                total_Due = princ_Due + int_Due + penal + charge;
                totalList[j][7] = CommonUtil.convertObjToStr(total_Due);
                totalList[j][8] = CommonUtil.convertObjToStr(String.valueOf(0));
          }
          observable.setFinalList(loanList);
          return totalList;
        }else {
            ClientUtil.displayAlert("No Data!!! ");

        }
        return null;
    }
        
    private Object[][] setTableDailyLoanData(HashMap map) {
        HashMap whereMap = new HashMap();
        HashMap dataMap = new HashMap();
        whereMap.put("BRANCH_CODE",getSelectedBranchID());
        whereMap.put("TRANS_DT", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtTransDt.getDateValue())));
        whereMap.put("ACT_NUM",(String) txtAcctNum.getText()); 
        List loanList = null;
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
            loanList = ClientUtil.executeQuery("getDailyGroupLoanTableDetails", whereMap);
        }else{
            whereMap.putAll(map);
            loanList = ClientUtil.executeQuery("getDailyGroupLoanAuthTableDetails", whereMap);
        }
        if (loanList != null && loanList.size() > 0) {
            Object totalList[][] = new Object[loanList.size()][11];
            for (int j = 0; j < loanList.size(); j++) {
                double princ_Due = 0.0;
                double int_Due = 0.0;
                double penal = 0.0;
                double charge = 0.0;
                double total_Due = 0.0;
                dataMap = (HashMap) loanList.get(j);
                System.out.println("processMap####"+dataMap); 
                totalList[j][0] = new Boolean(false);
                totalList[j][1] = CommonUtil.convertObjToStr(dataMap.get("MEMBER_NO"));
                totalList[j][2] = CommonUtil.convertObjToStr(dataMap.get("CUST_ID"));
                totalList[j][3] = CommonUtil.convertObjToStr(dataMap.get("CUSTOMER"));
                totalList[j][4] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(dataMap.get("PRIN_DUE")));
                totalList[j][5] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(dataMap.get("INTEREST")));
                totalList[j][6] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(dataMap.get("PENAL")));
                totalList[j][7] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(dataMap.get("CHARGES"))); 
                princ_Due = CommonUtil.convertObjToDouble(dataMap.get("PRIN_DUE")).doubleValue();
                int_Due = CommonUtil.convertObjToDouble(dataMap.get("INTEREST")).doubleValue();
                penal = CommonUtil.convertObjToDouble(dataMap.get("PENAL")).doubleValue();
                charge = CommonUtil.convertObjToDouble(dataMap.get("CHARGES")).doubleValue();
                total_Due = princ_Due + int_Due + penal + charge;
                totalList[j][8] = CommonUtil.convertObjToStr(total_Due);               
                totalList[j][9] = CommonUtil.convertObjToStr(dataMap.get("CUST_ACT_NUM"));
                totalList[j][10] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(dataMap.get("ACT_BALANCE")));                
          }
          observable.setFinalList(loanList);
          return totalList;
        }else {
            ClientUtil.displayAlert("No Data!!! ");

        }
        return null;
    }
    
     private Object[][] setTableAuthData(HashMap editMap1) {
        HashMap editMap = new HashMap();
        editMap1.put("BRANCH_CODE", getSelectedBranchID());
        List editList = ClientUtil.executeQuery("getGroupTransAuthorize", editMap1);
        if (editList != null && editList.size() > 0) {
            Object totalList[][] = new Object[editList.size()][9];
            for (int j = 0; j < editList.size(); j++) {
                double princ_Due = 0.0;
                double int_Due = 0.0;
                double penal = 0.0;
                double charge = 0.0;
                double total_Due = 0.0;
                editMap = (HashMap) editList.get(j);
                System.out.println("processMap####"+editMap);
                totalList[j][0] = CommonUtil.convertObjToStr(editMap.get("CREDIT_CARD_NO"));
                totalList[j][1] = CommonUtil.convertObjToStr(editMap.get("CUST_ID"));
                totalList[j][2] = CommonUtil.convertObjToStr(editMap.get("CUSTOMER"));
                totalList[j][3] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(editMap.get("PRIN_DUE")));
                totalList[j][4] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(editMap.get("INTEREST")));
                totalList[j][5] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(editMap.get("PENAL")));
                totalList[j][6] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(editMap.get("CHARGES")));
                princ_Due = CommonUtil.convertObjToDouble(editMap.get("PRIN_DUE")).doubleValue();
                int_Due = CommonUtil.convertObjToDouble(editMap.get("INTEREST")).doubleValue();
                penal = CommonUtil.convertObjToDouble(editMap.get("PENAL")).doubleValue();
                charge = CommonUtil.convertObjToDouble(editMap.get("CHARGES")).doubleValue();
                total_Due = princ_Due + int_Due + penal + charge;
                totalList[j][7] = CommonUtil.convertObjToStr(total_Due);
                totalList[j][8] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(editMap.get("RECEIPT")));
          }
          observable.setFinalList(editList);
          return totalList;
        }else {
            ClientUtil.displayAlert("No Data!!! ");

        }
        return null;
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDisplay;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnGroupId;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel lblGroupId;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblODBal;
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
    private com.see.truetransact.uicomponent.CPanel panGroupId;
    private com.see.truetransact.uicomponent.CPanel panGroupId1;
    private com.see.truetransact.uicomponent.CPanel panGroupIdDetail;
    private com.see.truetransact.uicomponent.CPanel panSHGDetails;
    private com.see.truetransact.uicomponent.CPanel panSHGTableDetails;
    private com.see.truetransact.uicomponent.CPanel panSHGTransDetails;
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
    private com.see.truetransact.uicomponent.CScrollPane srpSHGTable;
    private com.see.truetransact.uicomponent.CTabbedPane tabSHGTransaction;
    private com.see.truetransact.uicomponent.CTable tblSHGDetails;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CDateField tdtTransDt;
    private com.see.truetransact.uicomponent.CTextField txtAcctNum;
    private com.see.truetransact.uicomponent.CTextField txtODBal;
    private com.see.truetransact.uicomponent.CTextField txtPayment;
    private com.see.truetransact.uicomponent.CTextField txtTotalPayment;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] arg){
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        GroupLoanUI gui = new GroupLoanUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}