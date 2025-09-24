  /*
   * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
   *
   * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
   * 
   * RTGSOtherBankBranchUI.java
   *
   * Created on November 06, 2015, 04:45 PM
   * @author Suresh R
   */
package com.see.truetransact.ui.product.remittance;

import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observable;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.clientexception.ClientParseException;
import org.jfree.chart.block.LengthConstraintType;

/**
 *
 * @author Suresh R
 *
 **/

public class RTGSOtherBankBranchUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField{
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.remittance.RTGSOtherBankBranchRB", ProxyParameters.LANGUAGE);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String viewType = new String();
    RTGSOtherBankBranchOB observable = null;
    final String AUTHORIZE="Authorize";
    HashMap mandatoryMap = null;
    boolean isFilled = false;
    private HashMap productMap = new HashMap();
    private boolean updateMode = false;
    int updateTab=-1;
    /** Creates new form BeanForm */
    public RTGSOtherBankBranchUI() {
        initComponents();
        initForm();
    }
    
    private void initForm(){
        setFieldNames();
        internationalize();
        observable = new RTGSOtherBankBranchOB();
        setMaxLengths();
        initComponentData();
        setButtonEnableDisable();
        buttonEnableDisable(false);
        ClientUtil.enableDisable(panRTGSOtherBankBranch,false);
        btnBankCode.setEnabled(false);
        txtBankCode.setEnabled(false);
        lblBankName.setText("");
        setSizeTableData();
    }
    
    private void setSizeTableData() {
        tblRTGSOtherBankBranchDetails.getColumnModel().getColumn(0).setPreferredWidth(43);
        tblRTGSOtherBankBranchDetails.getColumnModel().getColumn(1).setPreferredWidth(110);
        tblRTGSOtherBankBranchDetails.getColumnModel().getColumn(2).setPreferredWidth(60);
        tblRTGSOtherBankBranchDetails.getColumnModel().getColumn(3).setPreferredWidth(40);
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
        btnBankCode.setName("btnMemberNo");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        
    }
    
    private void setMaxLengths() {
        txtBankCode.setAllowAll(true);
        txtIFSCCode.setAllowAll(true);
        txtMICRCode.setAllowAll(true);
        txtContactNo.setAllowAll(true);
        txtBranchName.setAllowAll(true);
        txtCity.setAllowAll(true);
        txtDistrict.setAllowAll(true);
        txtState.setAllowAll(true);
        txtContactNo.setValidation(new NumericValidation());
        txtIFSCCode.setMaxLength(11); //Refered By Srinath Sir and Abi
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        resourceBundle = new RTGSOtherBankBranchRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblCity.setText(resourceBundle.getString("lblCity"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }
    
    /* Auto Generated Method - setMandatoryHashMap()
     
    ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    private void initComponentData() {
        try{
            tblRTGSOtherBankBranchDetails.setModel(observable.getTblRTGSOtherBankBranchDetails());
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
    private void setMaximumLength(){
        //        txtDivisionNo.setValidation(new NumericValidation());
    }
    
    /** Auto Generated Method - update()
     * This method called by Observable. It updates the UI with
     * Observable's data. If needed add/Remove RadioButtons
     * method need to be added.*/
    public void update(Observable observed, Object arg) {
    }
    
    
    public void update(){
        txtBankCode.setText(observable.getTxtBankCode());
        txtIFSCCode.setText(observable.getTxtIFSCCode());
        txtMICRCode.setText(observable.getTxtMICRCode());
        txtBranchName.setText(observable.getTxtBranchName());
        txtAddress.setText(observable.getTxtAreaAddress());
        txtContactNo.setText(observable.getTxtContactNo());
        txtCity.setText(observable.getTxtCity());
        txtDistrict.setText(observable.getTxtDistrict());
        txtState.setText(observable.getTxtState());
    }
    
    /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtBankCode(txtBankCode.getText());
        observable.setTxtIFSCCode(txtIFSCCode.getText());
        observable.setTxtMICRCode(txtMICRCode.getText());
        observable.setTxtBranchName(txtBranchName.getText());
        observable.setTxtAreaAddress(txtAddress.getText());
        observable.setTxtContactNo(txtContactNo.getText());
        observable.setTxtCity(txtCity.getText());
        observable.setTxtDistrict(txtDistrict.getText());
        observable.setTxtState(txtState.getText());
    }
    
    
    
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        
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
    
    private void buttonEnableDisable(boolean flag){
        btnRTGSNew.setEnabled(flag);
        btnRTGSSave.setEnabled(flag);
        btnRTGSDelete.setEnabled(flag);
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
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace19 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace20 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabRTGSOtherBankBranch = new com.see.truetransact.uicomponent.CTabbedPane();
        panRTGSOtherBankBranch = new com.see.truetransact.uicomponent.CPanel();
        panRTGSOtherBankBranchDetails = new com.see.truetransact.uicomponent.CPanel();
        panBankCode = new com.see.truetransact.uicomponent.CPanel();
        btnBankCode = new com.see.truetransact.uicomponent.CButton();
        txtBankCode = new com.see.truetransact.uicomponent.CTextField();
        lblContactNo = new com.see.truetransact.uicomponent.CLabel();
        lblAddress = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        lblDistrict = new com.see.truetransact.uicomponent.CLabel();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        lblIFSCCode = new com.see.truetransact.uicomponent.CLabel();
        lblBranchName = new com.see.truetransact.uicomponent.CLabel();
        panRTGSOtherBankBranchBtn = new com.see.truetransact.uicomponent.CPanel();
        btnRTGSNew = new com.see.truetransact.uicomponent.CButton();
        btnRTGSSave = new com.see.truetransact.uicomponent.CButton();
        btnRTGSDelete = new com.see.truetransact.uicomponent.CButton();
        lblBankName = new com.see.truetransact.uicomponent.CLabel();
        lblBankCode = new com.see.truetransact.uicomponent.CLabel();
        lblMICRCode = new com.see.truetransact.uicomponent.CLabel();
        txtIFSCCode = new com.see.truetransact.uicomponent.CTextField();
        txtMICRCode = new com.see.truetransact.uicomponent.CTextField();
        txtState = new com.see.truetransact.uicomponent.CTextField();
        txtDistrict = new com.see.truetransact.uicomponent.CTextField();
        txtCity = new com.see.truetransact.uicomponent.CTextField();
        txtContactNo = new com.see.truetransact.uicomponent.CTextField();
        txtBranchName = new com.see.truetransact.uicomponent.CTextField();
        srpTxtAreaAddress = new com.see.truetransact.uicomponent.CScrollPane();
        txtAddress = new com.see.truetransact.uicomponent.CTextArea();
        panRTGSOtherBankBranchTableDetails = new com.see.truetransact.uicomponent.CPanel();
        srpRTGSOtherBankBranchTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblRTGSOtherBankBranchDetails = new com.see.truetransact.uicomponent.CTable();
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
        setMinimumSize(new java.awt.Dimension(850, 630));
        setPreferredSize(new java.awt.Dimension(850, 630));
        getContentPane().setLayout(new java.awt.GridBagLayout());

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

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace18);

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

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace19);

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

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace21);

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

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace22);

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

        tabRTGSOtherBankBranch.setMinimumSize(new java.awt.Dimension(550, 490));
        tabRTGSOtherBankBranch.setPreferredSize(new java.awt.Dimension(550, 490));

        panRTGSOtherBankBranch.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panRTGSOtherBankBranch.setMinimumSize(new java.awt.Dimension(570, 450));
        panRTGSOtherBankBranch.setPreferredSize(new java.awt.Dimension(570, 450));
        panRTGSOtherBankBranch.setLayout(new java.awt.GridBagLayout());

        panRTGSOtherBankBranchDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panRTGSOtherBankBranchDetails.setMinimumSize(new java.awt.Dimension(375, 440));
        panRTGSOtherBankBranchDetails.setPreferredSize(new java.awt.Dimension(375, 440));
        panRTGSOtherBankBranchDetails.setLayout(new java.awt.GridBagLayout());

        panBankCode.setLayout(new java.awt.GridBagLayout());

        btnBankCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBankCode.setEnabled(false);
        btnBankCode.setMaximumSize(new java.awt.Dimension(21, 21));
        btnBankCode.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBankCode.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBankCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBankCodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBankCode.add(btnBankCode, gridBagConstraints);

        txtBankCode.setBackground(new java.awt.Color(204, 204, 204));
        txtBankCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panBankCode.add(txtBankCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panRTGSOtherBankBranchDetails.add(panBankCode, gridBagConstraints);

        lblContactNo.setText("Mobile Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRTGSOtherBankBranchDetails.add(lblContactNo, gridBagConstraints);

        lblAddress.setText("Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRTGSOtherBankBranchDetails.add(lblAddress, gridBagConstraints);

        lblCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRTGSOtherBankBranchDetails.add(lblCity, gridBagConstraints);

        lblDistrict.setText("District");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRTGSOtherBankBranchDetails.add(lblDistrict, gridBagConstraints);

        lblState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRTGSOtherBankBranchDetails.add(lblState, gridBagConstraints);

        lblIFSCCode.setText("IFSC Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRTGSOtherBankBranchDetails.add(lblIFSCCode, gridBagConstraints);

        lblBranchName.setText("Branch Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRTGSOtherBankBranchDetails.add(lblBranchName, gridBagConstraints);

        panRTGSOtherBankBranchBtn.setMinimumSize(new java.awt.Dimension(95, 35));
        panRTGSOtherBankBranchBtn.setPreferredSize(new java.awt.Dimension(95, 35));
        panRTGSOtherBankBranchBtn.setLayout(new java.awt.GridBagLayout());

        btnRTGSNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnRTGSNew.setToolTipText("New");
        btnRTGSNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnRTGSNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnRTGSNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnRTGSNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRTGSNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSOtherBankBranchBtn.add(btnRTGSNew, gridBagConstraints);

        btnRTGSSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnRTGSSave.setToolTipText("Save");
        btnRTGSSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnRTGSSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnRTGSSave.setName("btnContactNoAdd"); // NOI18N
        btnRTGSSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnRTGSSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRTGSSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSOtherBankBranchBtn.add(btnRTGSSave, gridBagConstraints);

        btnRTGSDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnRTGSDelete.setToolTipText("Delete");
        btnRTGSDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnRTGSDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnRTGSDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnRTGSDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRTGSDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSOtherBankBranchBtn.add(btnRTGSDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(21, 0, 0, 15);
        panRTGSOtherBankBranchDetails.add(panRTGSOtherBankBranchBtn, gridBagConstraints);

        lblBankName.setForeground(new java.awt.Color(204, 0, 0));
        lblBankName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblBankName.setMaximumSize(new java.awt.Dimension(320, 18));
        lblBankName.setMinimumSize(new java.awt.Dimension(320, 18));
        lblBankName.setPreferredSize(new java.awt.Dimension(320, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panRTGSOtherBankBranchDetails.add(lblBankName, gridBagConstraints);

        lblBankCode.setText("Bank Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRTGSOtherBankBranchDetails.add(lblBankCode, gridBagConstraints);

        lblMICRCode.setText("MICR Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRTGSOtherBankBranchDetails.add(lblMICRCode, gridBagConstraints);

        txtIFSCCode.setMinimumSize(new java.awt.Dimension(120, 21));
        txtIFSCCode.setPreferredSize(new java.awt.Dimension(120, 21));
        txtIFSCCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIFSCCodeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panRTGSOtherBankBranchDetails.add(txtIFSCCode, gridBagConstraints);

        txtMICRCode.setMinimumSize(new java.awt.Dimension(120, 21));
        txtMICRCode.setPreferredSize(new java.awt.Dimension(120, 21));
        txtMICRCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMICRCodeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panRTGSOtherBankBranchDetails.add(txtMICRCode, gridBagConstraints);

        txtState.setMinimumSize(new java.awt.Dimension(120, 21));
        txtState.setPreferredSize(new java.awt.Dimension(120, 21));
        txtState.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panRTGSOtherBankBranchDetails.add(txtState, gridBagConstraints);

        txtDistrict.setMinimumSize(new java.awt.Dimension(120, 21));
        txtDistrict.setPreferredSize(new java.awt.Dimension(120, 21));
        txtDistrict.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDistrictFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panRTGSOtherBankBranchDetails.add(txtDistrict, gridBagConstraints);

        txtCity.setMinimumSize(new java.awt.Dimension(120, 21));
        txtCity.setPreferredSize(new java.awt.Dimension(120, 21));
        txtCity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCityFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panRTGSOtherBankBranchDetails.add(txtCity, gridBagConstraints);

        txtContactNo.setMinimumSize(new java.awt.Dimension(120, 21));
        txtContactNo.setPreferredSize(new java.awt.Dimension(120, 21));
        txtContactNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtContactNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panRTGSOtherBankBranchDetails.add(txtContactNo, gridBagConstraints);

        txtBranchName.setMinimumSize(new java.awt.Dimension(120, 21));
        txtBranchName.setPreferredSize(new java.awt.Dimension(120, 21));
        txtBranchName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBranchNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panRTGSOtherBankBranchDetails.add(txtBranchName, gridBagConstraints);

        srpTxtAreaAddress.setMinimumSize(new java.awt.Dimension(250, 45));
        srpTxtAreaAddress.setPreferredSize(new java.awt.Dimension(250, 45));

        txtAddress.setBorder(javax.swing.BorderFactory.createBevelBorder(1));
        txtAddress.setLineWrap(true);
        txtAddress.setMinimumSize(new java.awt.Dimension(20, 100));
        txtAddress.setPreferredSize(new java.awt.Dimension(20, 100));
        srpTxtAreaAddress.setViewportView(txtAddress);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panRTGSOtherBankBranchDetails.add(srpTxtAreaAddress, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        panRTGSOtherBankBranch.add(panRTGSOtherBankBranchDetails, gridBagConstraints);

        panRTGSOtherBankBranchTableDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panRTGSOtherBankBranchTableDetails.setMinimumSize(new java.awt.Dimension(425, 440));
        panRTGSOtherBankBranchTableDetails.setPreferredSize(new java.awt.Dimension(425, 440));
        panRTGSOtherBankBranchTableDetails.setLayout(new java.awt.GridBagLayout());

        srpRTGSOtherBankBranchTable.setMinimumSize(new java.awt.Dimension(400, 420));
        srpRTGSOtherBankBranchTable.setPreferredSize(new java.awt.Dimension(400, 420));

        tblRTGSOtherBankBranchDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IFSC Code", "Branch Name", "City", "Auth Status"
            }
        ));
        tblRTGSOtherBankBranchDetails.setMinimumSize(new java.awt.Dimension(400, 1000));
        tblRTGSOtherBankBranchDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(250, 400));
        tblRTGSOtherBankBranchDetails.setPreferredSize(new java.awt.Dimension(400, 375000));
        tblRTGSOtherBankBranchDetails.setSelectionBackground(new java.awt.Color(255, 255, 51));
        tblRTGSOtherBankBranchDetails.setSelectionForeground(new java.awt.Color(153, 0, 0));
        tblRTGSOtherBankBranchDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblRTGSOtherBankBranchDetailsMousePressed(evt);
            }
        });
        srpRTGSOtherBankBranchTable.setViewportView(tblRTGSOtherBankBranchDetails);

        panRTGSOtherBankBranchTableDetails.add(srpRTGSOtherBankBranchTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panRTGSOtherBankBranch.add(panRTGSOtherBankBranchTableDetails, gridBagConstraints);

        tabRTGSOtherBankBranch.addTab("Other Bank Branch Details", panRTGSOtherBankBranch);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabRTGSOtherBankBranch, gridBagConstraints);

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

    private void btnRTGSDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRTGSDeleteActionPerformed
        // TODO add your handling code here:
        String ifscCode = CommonUtil.convertObjToStr(tblRTGSOtherBankBranchDetails.getValueAt(tblRTGSOtherBankBranchDetails.getSelectedRow(), 0));
        observable.deleteTableData(ifscCode, tblRTGSOtherBankBranchDetails.getSelectedRow());
        setSizeTableData();
        observable.resetRTGSDetails();
        resetBranchDetails();
        buttonEnableDisable(false);
        btnRTGSNew.setEnabled(true);
    }//GEN-LAST:event_btnRTGSDeleteActionPerformed

    private void tblRTGSOtherBankBranchDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRTGSOtherBankBranchDetailsMousePressed
        // TODO add your handling code here:
        if (tblRTGSOtherBankBranchDetails.getRowCount() > 0) {
            updateOBFields();
            updateMode = true;
            observable.setNewData(false);
            updateTab= tblRTGSOtherBankBranchDetails.getSelectedRow();
            String ifscCode = CommonUtil.convertObjToStr(tblRTGSOtherBankBranchDetails.getValueAt(tblRTGSOtherBankBranchDetails.getSelectedRow(), 0));
            observable.populateRTGSDetails(ifscCode);
            update();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
                buttonEnableDisable(false);
                ClientUtil.enableDisable(panRTGSOtherBankBranchDetails, false);
            } else {
                String authStatus = CommonUtil.convertObjToStr(tblRTGSOtherBankBranchDetails.getValueAt(tblRTGSOtherBankBranchDetails.getSelectedRow(), 3));
                if(authStatus.length()>0){
                    ClientUtil.enableDisable(panRTGSOtherBankBranchDetails, false);
                    buttonEnableDisable(false);
                    btnRTGSNew.setEnabled(true);
                }else{
                    ClientUtil.enableDisable(panRTGSOtherBankBranchDetails, true);
                    buttonEnableDisable(true);
                    btnRTGSNew.setEnabled(false);
                }
            }
            btnBankCode.setEnabled(false);
            txtBankCode.setEnabled(false);
            txtIFSCCode.setEnabled(false);
        }
    }//GEN-LAST:event_tblRTGSOtherBankBranchDetailsMousePressed

    private void btnRTGSSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRTGSSaveActionPerformed
        // TODO add your handling code here:
        try {
            updateOBFields();
            if (txtBankCode.getText().length() > 0) {
                if (txtIFSCCode.getText().length() > 0) {
                    if (txtMICRCode.getText().length() > 0) {
                        if (txtBranchName.getText().length() > 0) {
                            if (txtAddress.getText().length() > 0) {
                                if (txtCity.getText().length() > 0) {
                                    if (txtDistrict.getText().length() > 0) {
                                        if (txtState.getText().length() > 0) {
                                            if(updateMode == false){
                                                String ifscCode = txtIFSCCode.getText();
                                                if(tblRTGSOtherBankBranchDetails.getRowCount()>0){
                                                    for(int i=0; i<tblRTGSOtherBankBranchDetails.getRowCount(); i++){
                                                        if(ifscCode.equals(CommonUtil.convertObjToStr(tblRTGSOtherBankBranchDetails.getValueAt(i, 0)))){
                                                            ClientUtil.showMessageWindow("IFSC Code ("+ CommonUtil.convertObjToStr(tblRTGSOtherBankBranchDetails.getValueAt(i, 1))
                                                                    + " ) Already Exists... Please Enter new IFSC Code !!!");
                                                            txtBranchName.setText("");
                                                            return;
                                                        }
                                                    }
                                                }
                                            }
                                            observable.addToTable(updateTab, updateMode);
                                            tblRTGSOtherBankBranchDetails.setModel(observable.getTblRTGSOtherBankBranchDetails());
                                            setSizeTableData();
                                            observable.resetRTGSDetails();
                                            resetBranchDetails();
                                            ClientUtil.enableDisable(panRTGSOtherBankBranchDetails, false);
                                            buttonEnableDisable(false);
                                            btnRTGSNew.setEnabled(true);
                                            btnBankCode.setEnabled(false);
                                        } else {
                                            ClientUtil.showAlertWindow("State Should Not be Empty !!!");
                                        }
                                    } else {
                                        ClientUtil.showAlertWindow("District Should Not be Empty !!!");
                                    }
                                } else {
                                    ClientUtil.showAlertWindow("City Should Not be Empty !!!");
                                }
                            } else {
                                ClientUtil.showAlertWindow("Address Should Not be Empty !!!");
                            }
                        } else {
                            ClientUtil.showAlertWindow("Branch Name Should Not be Empty !!!");
                        }
                    } else {
                        ClientUtil.showAlertWindow("MICR Code Should Not be Empty !!!");
                    }
                } else {
                    ClientUtil.showAlertWindow("IFSC Code Should Not be Empty !!!");
                }
            } else {
                ClientUtil.showAlertWindow("Bank Name Should Not be Empty !!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnRTGSSaveActionPerformed
    private void setBtnEnableDisable(boolean val){
        btnRTGSNew.setEnabled(val);
        btnRTGSSave.setEnabled(val);
        btnRTGSDelete.setEnabled(val);
    }
    private void btnRTGSNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRTGSNewActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        setBtnEnableDisable(false);
        btnRTGSSave.setEnabled(true);
        observable.setNewData(true);
        resetBranchDetails();
        ClientUtil.enableDisable(panRTGSOtherBankBranchDetails, true);
        if(tblRTGSOtherBankBranchDetails.getRowCount()>0){
            btnBankCode.setEnabled(false);
        }else{
            btnBankCode.setEnabled(true);
        }
        txtBankCode.setEnabled(false);
    }//GEN-LAST:event_btnRTGSNewActionPerformed
    private void resetBranchDetails() {
        txtIFSCCode.setText("");
        txtMICRCode.setText("");
        txtBranchName.setText("");
        txtAddress.setText("");
        txtContactNo.setText("");
        txtCity.setText("");
        txtDistrict.setText("");
        txtState.setText("");
    }
    private void btnBankCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBankCodeActionPerformed
        // TODO add your handling code here:
        txtBankCode.setText("");
        lblBankName.setText("");
        callView("BANK_CODE");
        viewType = "BANK_CODE";
    }//GEN-LAST:event_btnBankCodeActionPerformed
     
    /** To display a popUp window for viewing existing data */
    private void callView(String currAction){
        viewType = currAction;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")){
            viewMap.put(CommonConstants.MAP_NAME, "getRTGSBankBranchEdit");
        }else if(currAction.equalsIgnoreCase("Delete")){
            viewMap.put(CommonConstants.MAP_NAME, "getRTGSBankBranchDelete");
        }else if(currAction.equalsIgnoreCase("Enquiry")){
            viewMap.put(CommonConstants.MAP_NAME, "getRTGSBankBranchEdit");
        }else if(currAction.equalsIgnoreCase("BANK_CODE")){
            viewMap.put(CommonConstants.MAP_NAME, "getLookUpOtherBankName");
        }
        new ViewAll(this,viewMap).show();
    }
    
    public void fillData(Object obj){
        try{
            HashMap hashMap=(HashMap)obj;
            //System.out.println("### fillData Hash : "+hashMap);
            isFilled = true;
            if(viewType == "BANK_CODE"){
                txtBankCode.setText(CommonUtil.convertObjToStr(hashMap.get("BANK_CODE")));
                lblBankName.setText(CommonUtil.convertObjToStr(hashMap.get("BANK_NAME")));
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                this.setButtonEnableDisable();
                txtBankCode.setText(CommonUtil.convertObjToStr(hashMap.get("BANK_CODE")));
                displayBankName();
                hashMap.put("OTHER_BANK_CODE", CommonUtil.convertObjToInt(hashMap.get("BANK_CODE")));
                observable.getData(hashMap);
                tblRTGSOtherBankBranchDetails.setModel(observable.getTblRTGSOtherBankBranchDetails());
                setSizeTableData();
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                this.setButtonEnableDisable();
                txtBankCode.setText(CommonUtil.convertObjToStr(hashMap.get("BANK_CODE")));
                displayBankName();
                hashMap.put("OTHER_BANK_CODE", hashMap.get("BANK_CODE"));
                hashMap.put("AUTH_MODE","AUTH_MODE");//Added By Kannan AR on 02-Feb-2017
                observable.getData(hashMap);
                tblRTGSOtherBankBranchDetails.setModel(observable.getTblRTGSOtherBankBranchDetails());
                setSizeTableData();
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
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
    private void displayBankName(){
        if(txtBankCode.getText().length()>0){
            HashMap whereMap = new HashMap();
            whereMap.put("OTHER_BANK_CODE", txtBankCode.getText());
            List bankLst=ClientUtil.executeQuery("getRTGSOtherBankName",whereMap);
            if(bankLst!=null && bankLst.size()>0){
                whereMap =(HashMap) bankLst.get(0);
                lblBankName.setText(CommonUtil.convertObjToStr(whereMap.get("BANK_NAME")));
            }
        }
    }
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
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
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
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
            singleAuthorizeMap.put("OTHER_BANK_CODE", txtBankCode.getText());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap,txtBankCode.getText());
            viewType = "";
            ClientUtil.enableDisable(panRTGSOtherBankBranchDetails,false);
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getRTGSOtherBankBranchAuthorize");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    public void authorize(HashMap map,String id) {
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            observable.doAction();
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        btnRTGSNew.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        ClientUtil.clearAll(this);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        txtBankCode.setText("");
        lblBankName.setText("");
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
        lblStatus.setText("Edit");
        btnRTGSNew.setEnabled(true);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panRTGSOtherBankBranchDetails,false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        savePerformed();
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed(){
        observable.doAction();
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
            btnCancelActionPerformed(null);
            btnCancel.setEnabled(true);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
        }
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        setModified(false);
        ClientUtil.clearAll(this);
        lblBankName.setText("");
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
                // Add your handling code here:
        viewType = "CANCEL" ;
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
        btnView.setEnabled(true);
        btnCancel.setEnabled(false);
        isFilled = false;
        buttonEnableDisable(false);
        btnBankCode.setEnabled(false);
        resetBranchDetails();
        setSizeTableData();
        lblBankName.setText("");
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
   
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

    private void txtIFSCCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIFSCCodeFocusLost
        // TODO add your handling code here:
        if (txtIFSCCode.getText().length() > 0 && !ClientUtil.validateIFSCCode(txtIFSCCode)) {
            ClientUtil.showMessageWindow("Invalid IFSC Code, Enter Proper IFSC Code First 4 Digit Should be Alpha (Format :ABCD)");
            txtIFSCCode.setText("");
        }
    }//GEN-LAST:event_txtIFSCCodeFocusLost

    private void txtMICRCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMICRCodeFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMICRCodeFocusLost

    private void txtStateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStateFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStateFocusLost

    private void txtDistrictFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDistrictFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDistrictFocusLost

    private void txtCityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCityFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCityFocusLost

    private void txtContactNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtContactNoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContactNoFocusLost

    private void txtBranchNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBranchNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBranchNameFocusLost
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBankCode;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnRTGSDelete;
    private com.see.truetransact.uicomponent.CButton btnRTGSNew;
    private com.see.truetransact.uicomponent.CButton btnRTGSSave;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel lblAddress;
    private com.see.truetransact.uicomponent.CLabel lblBankCode;
    private com.see.truetransact.uicomponent.CLabel lblBankName;
    private com.see.truetransact.uicomponent.CLabel lblBranchName;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblContactNo;
    private com.see.truetransact.uicomponent.CLabel lblDistrict;
    private com.see.truetransact.uicomponent.CLabel lblIFSCCode;
    private com.see.truetransact.uicomponent.CLabel lblMICRCode;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
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
    private com.see.truetransact.uicomponent.CPanel panBankCode;
    private com.see.truetransact.uicomponent.CPanel panRTGSOtherBankBranch;
    private com.see.truetransact.uicomponent.CPanel panRTGSOtherBankBranchBtn;
    private com.see.truetransact.uicomponent.CPanel panRTGSOtherBankBranchDetails;
    private com.see.truetransact.uicomponent.CPanel panRTGSOtherBankBranchTableDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdgEFTProductGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIsLapsedGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPayableBranchGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrintServicesGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSeriesGR;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CScrollPane srpRTGSOtherBankBranchTable;
    private com.see.truetransact.uicomponent.CScrollPane srpTxtAreaAddress;
    private com.see.truetransact.uicomponent.CTabbedPane tabRTGSOtherBankBranch;
    private com.see.truetransact.uicomponent.CTable tblRTGSOtherBankBranchDetails;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CTextArea txtAddress;
    private com.see.truetransact.uicomponent.CTextField txtBankCode;
    private com.see.truetransact.uicomponent.CTextField txtBranchName;
    private com.see.truetransact.uicomponent.CTextField txtCity;
    private com.see.truetransact.uicomponent.CTextField txtContactNo;
    private com.see.truetransact.uicomponent.CTextField txtDistrict;
    private com.see.truetransact.uicomponent.CTextField txtIFSCCode;
    private com.see.truetransact.uicomponent.CTextField txtMICRCode;
    private com.see.truetransact.uicomponent.CTextField txtState;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] arg){
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        RTGSOtherBankBranchUI gui = new RTGSOtherBankBranchUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }

}