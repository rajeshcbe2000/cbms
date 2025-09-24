  /*
   * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
   *
   * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
   * 
   * GDSBankAdvanceUI.java
   *
   * Created on November 26, 2003, 11:27 AM
   */

package com.see.truetransact.ui.gdsapplication.gdsbankadvance;

import com.see.truetransact.ui.mdsapplication.mdsbankadvance.*;
import java.util.*;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import javax.swing.*;
/**
 *
 * @author Nithya
 *
 *      
 */

public class GDSBankAdvanceUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField{
    private final static ClientParseException parseException = ClientParseException.getInstance();
    GDSBankAdvanceOB observable = null;
    private HashMap mandatoryMap;
    private String viewType = "";
    private boolean isFilled = false;
    private final String AUTHORIZE="AUTHORIZE";
    private Date curr_dt=null;
    HashMap addressMap = null;
    private List finalList = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    private int rejectFlag = 0;
    
    /** Creates new form BeanForm */
    public GDSBankAdvanceUI() {
        initComponents();
        settingupUI();
    }
    
    private void settingupUI(){
        setFieldNames();
        internationalize();
        observable = new GDSBankAdvanceOB();
        setMandatoryHashMap();
        initTableData();
        setMaximumLength();
        setHelpMessage();
        setButtonEnableDisable();
        ClientUtil.enableDisable(this,false);
        curr_dt=ClientUtil.getCurrentDate();
        btnDelete.setVisible(false);
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
        btnSchemeName.setName("btnSchemeName");
        btnView.setName("btnView");
        lblMsg.setName("lblMsg");
        lblSchemeName.setName("lblSchemeName");
        lblSpace.setName("lblSpace");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        lblStatus.setName("lblStatus");
        lblTotalAmount.setName("lblTotalAmount");
        mbrMain.setName("mbrMain");
        panMDSBankAdvance.setName("panMDSBankAdvance");
        panSchemeName.setName("panSchemeName");
        panSchemeNameDetails.setName("panSchemeNameDetails");
        panSchemeNameTableData.setName("panSchemeNameTableData");
        panStatus.setName("panStatus");
        panTotalPayment.setName("panTotalPayment");
        srpMDSBankAdvance.setName("srpMDSBankAdvance");
        tabMDSBankAdvance.setName("tabMDSBankAdvance");
        tblMDSBankAdvance.setName("tblMDSBankAdvance");
        txtSchemeName.setName("txtSchemeName");
        txtTotalAmount.setName("txtTotalAmount");
    }
    
    
    
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
    }
    
    /* Auto Generated Method - setMandatoryHashMap()
     
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void initComponentData() {
        try{
            
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
    private void setMaximumLength(){
        txtSchemeName.setAllowAll(true);
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
    
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
    }
    
     private void initTableData(){
        tblMDSBankAdvance.setModel(observable.getTblMDSBankAdvance());
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
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgPrizeMember = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgBankAdvance = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace76 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace77 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace78 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabMDSBankAdvance = new com.see.truetransact.uicomponent.CTabbedPane();
        panMDSBankAdvance = new com.see.truetransact.uicomponent.CPanel();
        panSchemeNameDetails = new com.see.truetransact.uicomponent.CPanel();
        panSchemeName = new com.see.truetransact.uicomponent.CPanel();
        txtSchemeName = new com.see.truetransact.uicomponent.CTextField();
        btnSchemeName = new com.see.truetransact.uicomponent.CButton();
        lblSchemeName = new com.see.truetransact.uicomponent.CLabel();
        panSchemeNameTableData = new com.see.truetransact.uicomponent.CPanel();
        srpMDSBankAdvance = new com.see.truetransact.uicomponent.CScrollPane();
        tblMDSBankAdvance = new com.see.truetransact.uicomponent.CTable();
        panTotalPayment = new com.see.truetransact.uicomponent.CPanel();
        txtTotalAmount = new com.see.truetransact.uicomponent.CTextField();
        lblTotalAmount = new com.see.truetransact.uicomponent.CLabel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfChitals = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(850, 600));
        setPreferredSize(new java.awt.Dimension(850, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(29, 27));
        btnView.setPreferredSize(new java.awt.Dimension(29, 27));
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

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace73);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace74);

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

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace75);

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

        lblSpace76.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace76.setText("     ");
        lblSpace76.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace76);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace77.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace77.setText("     ");
        lblSpace77.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace77);

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

        lblSpace78.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace78.setText("     ");
        lblSpace78.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace78.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace78.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace78);

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

        tabMDSBankAdvance.setMinimumSize(new java.awt.Dimension(850, 480));
        tabMDSBankAdvance.setPreferredSize(new java.awt.Dimension(850, 480));

        panMDSBankAdvance.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMDSBankAdvance.setMinimumSize(new java.awt.Dimension(850, 450));
        panMDSBankAdvance.setPreferredSize(new java.awt.Dimension(850, 450));
        panMDSBankAdvance.setLayout(new java.awt.GridBagLayout());

        panSchemeNameDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Scheme Name"));
        panSchemeNameDetails.setMinimumSize(new java.awt.Dimension(830, 50));
        panSchemeNameDetails.setPreferredSize(new java.awt.Dimension(830, 50));
        panSchemeNameDetails.setLayout(new java.awt.GridBagLayout());

        panSchemeName.setLayout(new java.awt.GridBagLayout());

        txtSchemeName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSchemeName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSchemeNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panSchemeName.add(txtSchemeName, gridBagConstraints);

        btnSchemeName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSchemeName.setEnabled(false);
        btnSchemeName.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSchemeName.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSchemeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSchemeNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSchemeName.add(btnSchemeName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 9, 4);
        panSchemeNameDetails.add(panSchemeName, gridBagConstraints);

        lblSchemeName.setText("MDS Scheme Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 10, 6);
        panSchemeNameDetails.add(lblSchemeName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 12, 1);
        panMDSBankAdvance.add(panSchemeNameDetails, gridBagConstraints);

        panSchemeNameTableData.setBorder(javax.swing.BorderFactory.createTitledBorder("MDS Scheme Details"));
        panSchemeNameTableData.setMinimumSize(new java.awt.Dimension(830, 330));
        panSchemeNameTableData.setPreferredSize(new java.awt.Dimension(830, 330));
        panSchemeNameTableData.setLayout(new java.awt.GridBagLayout());

        srpMDSBankAdvance.setMinimumSize(new java.awt.Dimension(810, 300));
        srpMDSBankAdvance.setPreferredSize(new java.awt.Dimension(810, 300));

        tblMDSBankAdvance.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Chittal No", "Sub No", "Name", "Ins Amount", "Bonus"
            }
        ));
        tblMDSBankAdvance.setMinimumSize(new java.awt.Dimension(810, 3000));
        tblMDSBankAdvance.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 296));
        srpMDSBankAdvance.setViewportView(tblMDSBankAdvance);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        panSchemeNameTableData.add(srpMDSBankAdvance, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMDSBankAdvance.add(panSchemeNameTableData, gridBagConstraints);

        panTotalPayment.setMinimumSize(new java.awt.Dimension(825, 25));
        panTotalPayment.setPreferredSize(new java.awt.Dimension(825, 25));
        panTotalPayment.setLayout(new java.awt.GridBagLayout());

        txtTotalAmount.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTotalPayment.add(txtTotalAmount, gridBagConstraints);

        lblTotalAmount.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 425, 1, 0);
        panTotalPayment.add(lblTotalAmount, gridBagConstraints);

        cLabel1.setText("No Of Chittals :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        panTotalPayment.add(cLabel1, gridBagConstraints);

        lblNoOfChitals.setMinimumSize(new java.awt.Dimension(80, 18));
        lblNoOfChitals.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panTotalPayment.add(lblNoOfChitals, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panMDSBankAdvance.add(panTotalPayment, gridBagConstraints);

        tabMDSBankAdvance.addTab("MDS Bank Advance", panMDSBankAdvance);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabMDSBankAdvance, gridBagConstraints);

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

    private void btnSchemeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchemeNameActionPerformed
        // TODO add your handling code here:
        callView("SCHEME_DETAILS");
    }//GEN-LAST:event_btnSchemeNameActionPerformed

    private void txtSchemeNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSchemeNameFocusLost
        // TODO add your handling code here:
        if(txtSchemeName.getText().length()>0){
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAME",txtSchemeName.getText());
            List lst=ClientUtil.executeQuery("getSelectSchemeName",whereMap);
            if(lst !=null && lst.size()>0){
                viewType = "SCHEME_DETAILS";
                whereMap=(HashMap)lst.get(0);
                    fillData(whereMap);
                    lst=null;
                    whereMap=null;
            }else{
                ClientUtil.displayAlert("Invalid Scheme Name !!! ");
                txtSchemeName.setText("");
                observable.setTxtSchemeName("");
            }
        }
    }//GEN-LAST:event_txtSchemeNameFocusLost
                            
             
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
        btnReject.setEnabled(false);
        btnException.setEnabled(false);       
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);       
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
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
            singleAuthorizeMap.put("BANK_ADV_ID", observable.getBankAdvId());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT",ClientUtil.getCurrentDateWithTime());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap,observable.getBankAdvId());
            viewType = "";
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getGDSBankAdvanceAuthorize");
            isFilled = true;
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
            observable.doAction();
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                if (fromNewAuthorizeUI) {// Added by nithya on 20-12-2019 for KD-887
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.displayDetails("GDS Bank Advance");
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
        txtSchemeName.setEnabled(true);
        btnSchemeName.setEnabled(true);
        setButtonEnableDisable();
        ClientUtil.clearAll(this);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        //__ To Save the data in the Internal Frame...
        setModified(true);    
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        callView("Enquiry");
        lblStatus.setText("Edit");
        btnDelete.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false);
        btnView.setEnabled(false);           
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
       
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
         btnSave.setEnabled(false);
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
           if(txtTotalAmount.getText().length() >0 && CommonUtil.convertObjToDouble(txtTotalAmount.getText()).doubleValue()>0){
               savePerformed();
           }else{
               ClientUtil.showAlertWindow("No records to be inserted!!");
           }
       }
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed(){
        System.out.println("#$%#$%$# finalList"+observable.getFinalList());
        observable.setTxtTotalAmount(txtTotalAmount.getText()); 
        observable.setScreen(this.getScreen());
        //added by rishad for avoiding doubling issue 05/08/2015
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException /** Execute some operation */
            {
                try {
                    observable.doAction();
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
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
            displayTransDetail();
            btnCancelActionPerformed(null);
            btnCancel.setEnabled(true);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
        }
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        lblNoOfChitals.setText("");
    }
    
    private void displayTransDetail() {
        if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            String displayStr = "";
            String actNum = "";
            String transId = "";
            HashMap transMap = new HashMap();
            transMap.put("LOAN_NO",observable.getBankAdvId());
            transMap.put("CURR_DT", curr_dt);
            List lst = ClientUtil.executeQuery("getTransferTransLoanAuthDetails", transMap);
            if(lst !=null && lst.size()>0){
                displayStr += "Transfer Transaction Details...\n";
                for(int i = 0;i<lst.size();i++){
                    transMap = (HashMap)lst.get(i);
                    if (i==0) {
                        transId = (String)transMap.get("BATCH_ID");
                    }
                    displayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Batch Id : "+transMap.get("BATCH_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        displayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Deposit Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        displayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "  "+transMap.get("AMOUNT")+"\n";
                    }
                    System.out.println("#### :" +transMap);
                }
            }
            if(!displayStr.equals("")){
                ClientUtil.showMessageWindow(""+displayStr);
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
                paramMap.put("TransId", transId);
                paramMap.put("TransDt", curr_dt);
                paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                ttIntgration.setParam(paramMap);
                String reportName = "";
                reportName = "ReceiptPayment";
                ttIntgration.integrationForPrint(reportName, false);
            }
        }
    }
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        viewType = "CANCEL" ;
        lblStatus.setText("               ");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);
        btnSchemeName.setEnabled(false);
        observable.resetForm();
        btnNew.setEnabled(true);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        isFilled = false;
        //__ Make the Screen Closable..
        setModified(false);
        if (fromNewAuthorizeUI) { // Added by nithya on 12-20-2019 for KD-887
            this.dispose();
            fromNewAuthorizeUI = false;
            newauthorizeListUI.setFocusToTable();
        }
    }//GEN-LAST:event_btnCancelActionPerformed
    
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
    
    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equalsIgnoreCase("SCHEME_DETAILS")) {
         //   viewMap.put(CommonConstants.MAP_NAME, "getSelectEachSchemeDetails");
            viewMap.put(CommonConstants.MAP_NAME, "getSelectEachGDSGroupDetails");
        }else if(currField.equalsIgnoreCase("Enquiry")){
            viewMap.put(CommonConstants.MAP_NAME, "getBankAdvanceEditView");
        }
        new ViewAll(this,viewMap).show();
    }
    
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object  map) {
        try {
            HashMap hash = (HashMap) map;
            if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) { // Added by nithya on 20-12-2019 for KD-887
                isFilled = true;
                fromNewAuthorizeUI = true;
                newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
                hash.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                //transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(false);
                rejectFlag = 1;
            }
            if (viewType != null) {
                if (viewType.equalsIgnoreCase("SCHEME_DETAILS")) {
                    // Added by nithya on 11-06-2019 for KD 523 - gds bank advance updation
                    HashMap bankAdvMap = new HashMap();
                    bankAdvMap.put("GROUP_NO", hash.get("GROUP_NO"));
                    bankAdvMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                    List bankAdvanceList = ClientUtil.executeQuery("checkBankAdvanceAlreadyDone", bankAdvMap);
                    if (bankAdvanceList != null && bankAdvanceList.size() > 0) {
                        bankAdvMap = (HashMap) bankAdvanceList.get(0);
                        if (bankAdvMap.containsKey("BANK_ADVANCE_STATUS") && bankAdvMap.get("BANK_ADVANCE_STATUS") != null && CommonUtil.convertObjToStr(bankAdvMap.get("BANK_ADVANCE_STATUS")).equalsIgnoreCase("DONE")) {
                            ClientUtil.showMessageWindow("Bank advance already done for " + hash.get("GROUP_NO") + " this month");
                            return;
                        }
                    }
                    //End
                    txtSchemeName.setText(CommonUtil.convertObjToStr(hash.get("GROUP_NO")));
                    observable.setTxtSchemeName(CommonUtil.convertObjToStr(hash.get("GROUP_NO")));
                    observable.setTxtGroupNo(CommonUtil.convertObjToStr(hash.get("GROUP_NO")));
                    HashMap groupMap = new HashMap();
                    groupMap.put("GROUP_NO",txtSchemeName.getText());
                    List commonSchemeDetailsLst = ClientUtil.executeQuery("getDetailsForOneSchemeInGroup",groupMap);
                    observable.setSchemeCount(commonSchemeDetailsLst.size());
                    int schemeCount = commonSchemeDetailsLst.size();
                    HashMap commonSchemeMap = (HashMap)commonSchemeDetailsLst.get(0);
                    String commonScheme = CommonUtil.convertObjToStr(commonSchemeMap.get("SCHEME_NAME"));
                    System.out.println("commonGroup .. " + commonScheme);
                    observable.setCommonScheme(commonScheme);
                    btnSchemeName.setEnabled(true);
                    hash.put("SCHEME_NAME",commonScheme);
                    observable.insertTableData(hash);
                    tblMDSBankAdvance.setModel(observable.getTblMDSBankAdvance());
                    calcTotal();
                }else{
                    this.setButtonEnableDisable();
                    //getGDSGroupForChittal
                    HashMap groupMap = new HashMap();
                    groupMap.put("BANK_ADV_ID",hash.get("BANK_ADV_ID"));
                    List groupDetailLst = ClientUtil.executeQuery("getGDSGroupForChittal",groupMap);
                    String groupNo = CommonUtil.convertObjToStr(((HashMap)groupDetailLst.get(0)).get("GROUP_NO"));
                    txtSchemeName.setText(groupNo);
                    observable.setTxtGroupNo(groupNo);
                    HashMap commonSchemeMap = new HashMap();
                    commonSchemeMap.put("GROUP_NO",txtSchemeName.getText());
                    List commonSchemeDetailsLst = ClientUtil.executeQuery("getDetailsForOneSchemeInGroup",commonSchemeMap);
                    observable.setSchemeCount(commonSchemeDetailsLst.size());
                    int schemeCount = commonSchemeDetailsLst.size();
                    observable.setSchemeCount(schemeCount);
                    commonSchemeMap = (HashMap)commonSchemeDetailsLst.get(0);
                    String commonScheme = CommonUtil.convertObjToStr(commonSchemeMap.get("SCHEME_NAME"));
                    System.out.println("commonGroup .. " + commonScheme);
                    observable.setBankAdvId(CommonUtil.convertObjToStr(hash.get("BANK_ADV_ID")));
                    observable.insertTableDataEdit(hash);
                    tblMDSBankAdvance.setModel(observable.getTblMDSBankAdvance());
                    ClientUtil.enableDisable(panMDSBankAdvance,false);
                    calcTotal();
                }
                if(viewType ==  AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    ClientUtil.enableDisable(this,false);
                }
                hash = null;
                btnCancel.setEnabled(true);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void calcTotal() {
        double totAmt = 0;
        double totBonusAmt = 0;
        long selectedCount = 0;
        if(tblMDSBankAdvance.getRowCount()>0){
            selectedCount = tblMDSBankAdvance.getRowCount();
            for (int i=0; i< tblMDSBankAdvance.getRowCount(); i++) {
                totAmt = totAmt + CommonUtil.convertObjToDouble(tblMDSBankAdvance.getValueAt(i, 4).toString()).doubleValue();
                totBonusAmt = totBonusAmt + CommonUtil.convertObjToDouble(tblMDSBankAdvance.getValueAt(i, 5).toString()).doubleValue();
            }
        }
        txtTotalAmount.setText(String.valueOf(totAmt));        
        lblNoOfChitals.setText(String.valueOf(selectedCount));
        observable.setTotalBonusAmount(String.valueOf(totBonusAmt));
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSchemeName;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNoOfChitals;
    private com.see.truetransact.uicomponent.CLabel lblSchemeName;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblSpace76;
    private com.see.truetransact.uicomponent.CLabel lblSpace77;
    private com.see.truetransact.uicomponent.CLabel lblSpace78;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmount;
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
    private com.see.truetransact.uicomponent.CPanel panMDSBankAdvance;
    private com.see.truetransact.uicomponent.CPanel panSchemeName;
    private com.see.truetransact.uicomponent.CPanel panSchemeNameDetails;
    private com.see.truetransact.uicomponent.CPanel panSchemeNameTableData;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTotalPayment;
    private com.see.truetransact.uicomponent.CButtonGroup rdgBankAdvance;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrizeMember;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CScrollPane srpMDSBankAdvance;
    private com.see.truetransact.uicomponent.CTabbedPane tabMDSBankAdvance;
    private com.see.truetransact.uicomponent.CTable tblMDSBankAdvance;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CTextField txtSchemeName;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmount;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] arg){
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        GDSBankAdvanceUI gui = new GDSBankAdvanceUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
    }