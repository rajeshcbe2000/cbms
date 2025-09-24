/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GoldConfigurationUI.java
 *
 * Created on February 2, 2005, 12:20 PM
 */

package com.see.truetransact.ui.termloan.goldLoanConfiguration;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.DateUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import java.util.Hashtable;
/**
 *
 * @author  Sathiya
 */
public class GoldConfigurationUI extends CInternalFrame implements java.util.Observer, UIMandatoryField{
    HashMap mandatoryMap;
    GoldConfigurationOB observable;
//    GoldConfigurationRB resourceBundle = new GoldConfigurationRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.goldLoanConfiguration.GoldConfigurationRB", ProxyParameters.LANGUAGE);
    
    final int EDIT = 0,DELETE = 1,AUTHORIZE = 2, VIEW = 10;
    final int SUSPPROD_TYPE=6,SUSPPROD_ID=7,SUSPPROD_ACCN=8;
    int viewType=-1;
    private boolean selectedRow = false;
    private boolean newRecords = false;
    Date currDate = null;
    
    /** Creates new form GoldConfigurationUI */
    public GoldConfigurationUI() {
        initComponents();
        initSetup();
        initComponentData();
        currDate = ClientUtil.getCurrentDate();
    }
    
    private void initSetup(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        setMaxLenths();
        setHelpMessage();
        ClientUtil.enableDisable(this, false); //__ Disables all when the screen appears for the 1st time
        setButtonEnableDisable();              //__ Enables/Disables the necessary buttons and menu items...
        observable.resetForm();                //__ Resets the Data in the Form...
        observable.resetTable();
        observable.resetStatus();              //__ to reset the status...
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panGoldData);        
        btnGoldNew.setEnabled(false);
        btnGoldSave.setEnabled(false);
        btnGoldDel.setEnabled(false);
        btnGoldDel.setVisible(false);
//        final HashMap viewMap = new HashMap();
//        HashMap whereMap = new HashMap();
//        viewType = EDIT;
//        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
//        whereMap.put("BRANCHID", TrueTransactMain.BRANCH_ID);
//        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
//        viewMap.put(CommonConstants.MAP_NAME, "getSelectNotAuthRecords");
//        fillData(viewMap);
    }
    
    private void setObservable() {
        try{
            observable = GoldConfigurationOB.getInstance();
            observable.addObserver(this);
        }catch (Exception e){
            e.printStackTrace();
        }
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
        
//        lblRemarks.setName("lblRemarks");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus1.setName("lblStatus1");
        
        mbrGold.setName("mbrGold");
        panGoldData.setName("panGoldData");
        panStatus.setName("panStatus");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblStatus1.setText(resourceBundle.getString("lblStatus1"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        
        lblDate.setText(resourceBundle.getString("lblDate"));
        lblPerGramRate.setText(resourceBundle.getString("lblPerGramRate"));
        lblPurityOfGold.setText(resourceBundle.getString("lblPurityOfGold"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        tdtFromDate.setDateValue(observable.getTdtFromDate());
        tdtDate.setDateValue(observable.getTdtToDate());
        txtPerGramRate.setText(observable.getTxtPerGramRate());
        cboPurityOfGold.setSelectedItem(observable.getCboPurityOfGold());
        tblGoldMarketRate.setModel(observable.getTblGold());
        lblStatus1.setText(observable.getLblStatus());
        if(observable.getDefaultItem()!=null && observable.getDefaultItem().equals("Y")){
            chkDefault.setSelected(true);
        }else{
            chkDefault.setSelected(false);
        }
    }
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTdtFromDate(tdtFromDate.getDateValue());
        observable.setTdtToDate(tdtDate.getDateValue());
        observable.setTxtPerGramRate(txtPerGramRate.getText());
        observable.setCboPurityOfGold(CommonUtil.convertObjToStr(cboPurityOfGold.getSelectedItem()));        
        observable.setSelectedBranchID(getSelectedBranchID());//__ To update the BranchSelected...
        if(chkDefault.isSelected()){
            observable.setDefaultItem("Y");
        }else{
            observable.setDefaultItem("N");
        }
        
    }
  
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboPurityOfGold", new Boolean(false));
        mandatoryMap.put("tdtDate", new Boolean(false));
        mandatoryMap.put("txtPerGramRate", new Boolean(false));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        GoldConfigurationMRB objGoldConfigurationMRB = new GoldConfigurationMRB();
        tdtFromDate.setHelpMessage(lblMsg, objGoldConfigurationMRB.getString("tdtFromDate"));
        txtPerGramRate.setHelpMessage(lblMsg, objGoldConfigurationMRB.getString("txtPerGramRate"));
        cboPurityOfGold.setHelpMessage(lblMsg, objGoldConfigurationMRB.getString("cboPurityOfGold"));
    }
    
    private void setMaxLenths() {
        txtPerGramRate.setMaxLength(16);        
        txtPerGramRate.setValidation(new CurrencyValidation(14,2));
    }
    
    // To set The Value of the Buttons Depending on the Value or Condition...
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(!btnSave.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
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

        panGold = new com.see.truetransact.uicomponent.CPanel();
        panGoldData = new com.see.truetransact.uicomponent.CPanel();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblPerGramRate = new com.see.truetransact.uicomponent.CLabel();
        txtPerGramRate = new com.see.truetransact.uicomponent.CTextField();
        lblPurityOfGold = new com.see.truetransact.uicomponent.CLabel();
        cboPurityOfGold = new com.see.truetransact.uicomponent.CComboBox();
        lblDate1 = new com.see.truetransact.uicomponent.CLabel();
        tdtDate = new com.see.truetransact.uicomponent.CDateField();
        panBtnDepSubNoAcc = new com.see.truetransact.uicomponent.CPanel();
        btnGoldNew = new com.see.truetransact.uicomponent.CButton();
        btnGoldSave = new com.see.truetransact.uicomponent.CButton();
        btnGoldDel = new com.see.truetransact.uicomponent.CButton();
        chkDefault = new com.see.truetransact.uicomponent.CCheckBox();
        panGoldDisplay = new com.see.truetransact.uicomponent.CPanel();
        srpGold = new com.see.truetransact.uicomponent.CScrollPane();
        tblGoldMarketRate = new com.see.truetransact.uicomponent.CTable();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrGoldProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace35 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace36 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace37 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace38 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace39 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        mbrGold = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        sptPrint = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(775, 525));
        setPreferredSize(new java.awt.Dimension(775, 525));

        panGold.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panGold.setFocusCycleRoot(true);
        panGold.setMinimumSize(new java.awt.Dimension(750, 500));
        panGold.setPreferredSize(new java.awt.Dimension(750, 500));
        panGold.setLayout(new java.awt.GridBagLayout());

        panGoldData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panGoldData.setFocusCycleRoot(true);
        panGoldData.setMinimumSize(new java.awt.Dimension(350, 200));
        panGoldData.setPreferredSize(new java.awt.Dimension(350, 200));
        panGoldData.setLayout(new java.awt.GridBagLayout());

        lblDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panGoldData.add(lblDate, gridBagConstraints);

        tdtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panGoldData.add(tdtFromDate, gridBagConstraints);

        lblPerGramRate.setText("Per Gram Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panGoldData.add(lblPerGramRate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panGoldData.add(txtPerGramRate, gridBagConstraints);

        lblPurityOfGold.setText("Purity of Gold");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panGoldData.add(lblPurityOfGold, gridBagConstraints);

        cboPurityOfGold.setMaximumSize(new java.awt.Dimension(100, 21));
        cboPurityOfGold.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPurityOfGold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPurityOfGoldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panGoldData.add(cboPurityOfGold, gridBagConstraints);

        lblDate1.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panGoldData.add(lblDate1, gridBagConstraints);

        tdtDate.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panGoldData.add(tdtDate, gridBagConstraints);

        panBtnDepSubNoAcc.setLayout(new java.awt.GridBagLayout());

        btnGoldNew.setText("New");
        btnGoldNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoldNewActionPerformed(evt);
            }
        });
        panBtnDepSubNoAcc.add(btnGoldNew, new java.awt.GridBagConstraints());

        btnGoldSave.setText("Save");
        btnGoldSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoldSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 1;
        panBtnDepSubNoAcc.add(btnGoldSave, gridBagConstraints);

        btnGoldDel.setText("Delete");
        btnGoldDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoldDelActionPerformed(evt);
            }
        });
        panBtnDepSubNoAcc.add(btnGoldDel, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panGoldData.add(panBtnDepSubNoAcc, gridBagConstraints);

        chkDefault.setText("Make Default");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 15, 4);
        panGoldData.add(chkDefault, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGold.add(panGoldData, gridBagConstraints);

        panGoldDisplay.setMinimumSize(new java.awt.Dimension(400, 400));
        panGoldDisplay.setPreferredSize(new java.awt.Dimension(400, 400));
        panGoldDisplay.setLayout(new java.awt.GridBagLayout());

        srpGold.setMinimumSize(new java.awt.Dimension(400, 400));
        srpGold.setPreferredSize(new java.awt.Dimension(400, 400));

        tblGoldMarketRate.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "From Date", "To Date", "Purity of Gold", "Deposit Amount"
            }
        ));
        tblGoldMarketRate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGoldMarketRateMouseClicked(evt);
            }
        });
        srpGold.setViewportView(tblGoldMarketRate);

        panGoldDisplay.add(srpGold, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGold.add(panGoldDisplay, gridBagConstraints);

        getContentPane().add(panGold, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus1.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

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
        tbrGoldProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrGoldProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrGoldProduct.add(btnNew);

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrGoldProduct.add(lblSpace34);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrGoldProduct.add(btnEdit);

        lblSpace35.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace35.setText("     ");
        lblSpace35.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrGoldProduct.add(lblSpace35);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrGoldProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrGoldProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrGoldProduct.add(btnSave);

        lblSpace36.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace36.setText("     ");
        lblSpace36.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrGoldProduct.add(lblSpace36);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrGoldProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrGoldProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrGoldProduct.add(btnAuthorize);

        lblSpace37.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace37.setText("     ");
        lblSpace37.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrGoldProduct.add(lblSpace37);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrGoldProduct.add(btnException);

        lblSpace38.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace38.setText("     ");
        lblSpace38.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrGoldProduct.add(lblSpace38);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrGoldProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrGoldProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrGoldProduct.add(btnPrint);

        lblSpace39.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace39.setText("     ");
        lblSpace39.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrGoldProduct.add(lblSpace39);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrGoldProduct.add(btnClose);

        getContentPane().add(tbrGoldProduct, java.awt.BorderLayout.NORTH);

        mnuProcess.setText("Process");

        mitNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNew.setMnemonic('N');
        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptProcess);

        mitSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitSave.setMnemonic('S');
        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitPrint.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        mitPrint.setMnemonic('P');
        mitPrint.setText("Print");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);
        mnuProcess.add(sptPrint);

        mitClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mitClose.setMnemonic('l');
        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrGold.add(mnuProcess);

        setJMenuBar(mbrGold);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus1.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        List lst = ClientUtil.executeQuery("getSelectNotAuthRecordsinAuthMode", null);
        if(lst!=null && lst.size()>0){
            ClientUtil.showAlertWindow("Already record exists for authorization");
            return;
        }else{
            observable.resetForm();               // to Reset all the Fields and Status in UI...
            observable.resetTable();
            setButtonEnableDisable();             // Enables/Disables the necessary buttons and menu items...
            ClientUtil.enableDisable(this, true); // Enables the panel...
            observable.setActionType(ClientConstants.ACTIONTYPE_NEW);   //Sets the Action Type to be performed...
            observable.setStatus();               // To set the Value of lblStatus...
            btnReject.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnException.setEnabled(false);
            ClientUtil.enableDisable(panGoldData,false);
            btnGoldNew.setEnabled(true);
            setModified(true);
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT); //__ Sets the Action Type to be performed...
        popUp(EDIT);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE); //__ Sets the Action Type to be performed...
        popUp(DELETE);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panGold);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
            return;
        }else if(tblGoldMarketRate.getRowCount() == 0){
            ClientUtil.showAlertWindow("No records to save");
        }else if(tblGoldMarketRate.getRowCount()>0 && tblGoldMarketRate.getRowCount() < 3){
            if(tblGoldMarketRate.getRowCount() == 1){
                String carrot = CommonUtil.convertObjToStr(tblGoldMarketRate.getValueAt(tblGoldMarketRate.getRowCount()-1, 3));
                String msg = "";
                if(carrot.equals("18CT")){
                    msg = "Enter Record for 20CT and 22CT";
                }else if(carrot.equals("20CT")){
                    msg = "Enter Record for 18CT and 22CT";
                }else if(carrot.equals("22CT")){
                    msg = "Enter Record for 18CT and 20CT";
                }
                ClientUtil.showMessageWindow(""+msg);
            }else if(tblGoldMarketRate.getRowCount() == 2){
                String msg = "";
                for(int i = 0;i<tblGoldMarketRate.getRowCount();i++){
                    String carrot = CommonUtil.convertObjToStr(tblGoldMarketRate.getValueAt(tblGoldMarketRate.getRowCount()-1, 3));
                    if(tblGoldMarketRate.getRowCount() == 2 && carrot.equals("18CT") || carrot.equals("20CT")){
                        msg = "Enter Record for 22CT";
                    }else if(tblGoldMarketRate.getRowCount() == 2 && carrot.equals("20CT") ||carrot.equals("22CT")){
                        msg = "Enter Record for 18CT";
                    }else if(tblGoldMarketRate.getRowCount() == 2 && carrot.equals("22CT")||carrot.equals("18CT")){
                        msg = "Enter Record for 20CT";
                    }                
                }
                ClientUtil.showMessageWindow(""+msg);
            }
        }else{
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                List list = ClientUtil.executeQuery("getSelectMaxRefMaxSetNo", null);
                if(list != null && list.size()>0){
                    Hashtable existingRec = (Hashtable)list.get(0);
                    long referenceNo = CommonUtil.convertObjToLong(existingRec.get("REFERENCE_NO"));
                    observable.setReferenceNo(referenceNo + 1);
                    long setNo = CommonUtil.convertObjToLong(existingRec.get("SET_NO"));
                    observable.setSetNo(setNo + 1);
                }else{
                    observable.setReferenceNo(1);
                    observable.setSetNo(1);
                }
            }
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
                List list = ClientUtil.executeQuery("getSelectMaxRefMaxSetNo", null);
                if(list != null && list.size()>0){
                    Hashtable existingRec = (Hashtable)list.get(0);
                    long referenceNo = CommonUtil.convertObjToLong(existingRec.get("REFERENCE_NO"));
                    observable.setReferenceNo(referenceNo);
                    long setNo = CommonUtil.convertObjToLong(existingRec.get("SET_NO"));
                    observable.setSetNo(setNo);
                }
            }
            if(tblGoldMarketRate.getRowCount() >1){
                Date firstDt = null;
                Date secondDt = null;
                for(int i = 0;i<tblGoldMarketRate.getRowCount();i++){
                    if(i == 0){
                        firstDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblGoldMarketRate.getValueAt(tblGoldMarketRate.getRowCount()-3, 1)));
                    }else if(i == 1){
                        secondDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblGoldMarketRate.getValueAt(tblGoldMarketRate.getRowCount()-2, 1)));
                        if(!firstDt.equals(secondDt)){
                            ClientUtil.showAlertWindow("All the records in a set should have same from date");
                            return;
                        }
                    }else if(i == 2){
                        Date thirdDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblGoldMarketRate.getValueAt(tblGoldMarketRate.getRowCount()-1, 1)));
                        if(!secondDt.equals(thirdDt)){
                            ClientUtil.showAlertWindow("All the records in a set should have same from date");
                            return;
                        }                        
                    }
                }
            }
            observable.doAction();// To perform the necessary operation depending on the Action type...
            //__ If the Operation is Not Failed, Clear the Screen...
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("SET_NO");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("SET ID")) {
                        lockMap.put("SET_NO",observable.getProxyReturnMap().get("SET_NO"));
                    }
                }
                setEditLockMap(lockMap);
                setEditLock();
                observable.resetForm();                    //__ Reset the fields in the UI to null...
                observable.resetTable();
                ClientUtil.enableDisable(this, false);     //__ Disables the panel...
                setButtonEnableDisable();                  //__ Enables or Disables the buttons and menu Items depending on their previous state...
                observable.setResultStatus();              //__ To Reset the Value of lblStatus...
            }
        }
        
        //__ Make the Screen Closable..
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setModified(false);
        observable.resetForm();                 //__ Reset the fields in the UI to null...
        observable.resetTable();
        ClientUtil.enableDisable(this, false);  //__ Disables the panel...
        if(!btnSave.isEnabled()){
            btnSave.setEnabled(true);
        }
        setButtonEnableDisable();               //__ Enables or Disables the buttons and menu Items depending on their previous state...
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);//Sets the Action Type to be performed...
        observable.setStatus();                 //__ To set the Value of lblStatus..
        
        viewType = -1;
        
        //__ Make the Screen Closable..
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        newRecords = false;
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
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }        
    
    private void btnGoldDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoldDelActionPerformed
        // TODO add your handling code here:
        if(tblGoldMarketRate.getSelectedRow()>=0){
            observable.removeDeleteValues(tblGoldMarketRate.getSelectedRow());
//            updateValues();        
            resetUIValues();
            tdtFromDate.setEnabled(false);
            tdtDate.setEnabled(false);
            cboPurityOfGold.setEnabled(false);
            txtPerGramRate.setEnabled(false);            
            btnGoldNew.setEnabled(false);
            btnGoldSave.setEnabled(true);
            btnGoldDel.setEnabled(true);
        }
    }//GEN-LAST:event_btnGoldDelActionPerformed

    private void tblGoldMarketRateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGoldMarketRateMouseClicked
        // TODO add your handling code here:
        if(tblGoldMarketRate.getSelectedRow()>=0){
            observable.populateallValues(tblGoldMarketRate.getSelectedRow());
            updateValues();        
            btnGoldNew.setEnabled(false);
            btnGoldSave.setEnabled(true);
            btnGoldDel.setEnabled(true);
            if(observable.getActionType() != ClientConstants.ACTIONTYPE_NEW){
                if(observable.getAuthStatus().length() == 0 && observable.getAuthStatus().equals("") && observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                    ClientUtil.enableDisable(panGoldData,true);
                    tdtDate.setEnabled(false);
                    cboPurityOfGold.setEnabled(false);
                }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
                    btnSave.setEnabled(true);
                }else{
                    ClientUtil.enableDisable(panGoldData,false);
                    btnGoldSave.setEnabled(false);
                    btnGoldDel.setEnabled(false);
                    btnSave.setEnabled(false);
                }
                cboPurityOfGold.setEnabled(false);
            }else{
                tdtFromDate.setEnabled(true);
//                cboPurityOfGold.setEnabled(true);
                txtPerGramRate.setEnabled(true);
            }
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                ClientUtil.enableDisable(this,false);
            }
            selectedRow = true;
            newRecords = true;
        }
    }//GEN-LAST:event_tblGoldMarketRateMouseClicked

    private void btnGoldNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoldNewActionPerformed
        // TODO add your handling code here:
        if(tblGoldMarketRate.getRowCount() <3){
            btnGoldNew.setEnabled(false);
            btnGoldSave.setEnabled(true);
            btnGoldDel.setEnabled(false);
            ClientUtil.enableDisable(panGoldData,true);
            tdtDate.setEnabled(false);
            btnSave.setEnabled(true);
            if(tblGoldMarketRate.getRowCount() == 0){
                cboPurityOfGold.setSelectedItem("18CT");
                tdtFromDate.setDateValue(DateUtil.getStringDate(currDate));
            }else if(tblGoldMarketRate.getRowCount() == 1){
                cboPurityOfGold.setSelectedItem("20CT");
            }else if(tblGoldMarketRate.getRowCount() == 2){
                cboPurityOfGold.setSelectedItem("22CT");
            }
            cboPurityOfGold.setEnabled(false);
        }else{
            ClientUtil.showAlertWindow("already records are entered");
        }
    }//GEN-LAST:event_btnGoldNewActionPerformed
    
    private void updateValues(){
        tdtFromDate.setDateValue(observable.getTdtFromDate());
        tdtDate.setDateValue(observable.getTdtToDate());
        txtPerGramRate.setText(observable.getTxtPerGramRate());
        cboPurityOfGold.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboPurityOfGold()));        
        observable.setSelectedBranchID(getSelectedBranchID());//__ To update the BranchSelected...
    }
    
    private void btnGoldSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoldSaveActionPerformed
        // TODO add your handling code here:
        if(tdtFromDate.getDateValue().length() == 0){
            ClientUtil.showAlertWindow("From date should not be empty");
            return;
        }else if(cboPurityOfGold.getSelectedIndex()<=0){
            ClientUtil.showAlertWindow("Purity of gold should not be empty");
            return;
        }else if(txtPerGramRate.getText().length() == 0){
            ClientUtil.showAlertWindow("Per gram rate should not be empty");
            return;
        }else{
            double txtValue = txtValue = CommonUtil.convertObjToDouble(txtPerGramRate.getText()).doubleValue();
            double recAmt = 0;
            if(tblGoldMarketRate.getRowCount()>0){
                recAmt = CommonUtil.convertObjToDouble(tblGoldMarketRate.getValueAt(tblGoldMarketRate.getRowCount()-1, 4)).doubleValue();
            }
            if(newRecords == false && tblGoldMarketRate.getRowCount()>0){
                for (int i = 0;i<tblGoldMarketRate.getRowCount();i++){
                    if(cboPurityOfGold.getSelectedIndex()>0 && cboPurityOfGold.getSelectedItem().toString().toUpperCase().equals(tblGoldMarketRate.getValueAt(i, 3))){
                        ClientUtil.showAlertWindow("This Record already Exists");
                        return;
                    }
                }
            }else if(newRecords == true && tblGoldMarketRate.getRowCount()>0){
                if(tblGoldMarketRate.getRowCount() == tblGoldMarketRate.getSelectedRow()+1){
                    recAmt = CommonUtil.convertObjToDouble(tblGoldMarketRate.getValueAt(tblGoldMarketRate.getSelectedRow(), 4)).doubleValue();
                }else{
                    recAmt = CommonUtil.convertObjToDouble(tblGoldMarketRate.getValueAt(tblGoldMarketRate.getSelectedRow()+1, 4)).doubleValue();
                }
            }
            if(newRecords == false && recAmt>txtValue){
                ClientUtil.showAlertWindow("amount should be greater than already entered");
                return;
            }else if(newRecords == true && recAmt<txtValue && tblGoldMarketRate.getRowCount() != tblGoldMarketRate.getSelectedRow()+1){
                ClientUtil.showAlertWindow("amount should be less than already entered");
                return;
            }else if(newRecords == true && recAmt>txtValue && tblGoldMarketRate.getRowCount() == tblGoldMarketRate.getSelectedRow()+1){
                ClientUtil.showAlertWindow("amount should be greater than already entered");
                return;
            }else{
                updateOBFields();
                if(selectedRow != true){
                    observable.setGoldTabData(-1);
                }if(selectedRow == true){
                    observable.setGoldTabData(tblGoldMarketRate.getSelectedRow());
                }
                observable.resetForm();
                resetUIValues();
                tdtFromDate.setEnabled(false);
                tdtDate.setEnabled(false);
                cboPurityOfGold.setEnabled(false);
                txtPerGramRate.setEnabled(false);
                btnGoldNew.setEnabled(true);
                btnGoldSave.setEnabled(false);
                newRecords = false;
            }
        }
    }//GEN-LAST:event_btnGoldSaveActionPerformed
    
    private void resetUIValues(){
        tdtDate.setDateValue("");
        cboPurityOfGold.setSelectedItem("");
        txtPerGramRate.setText("");
    }        

    private void cboPurityOfGoldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPurityOfGoldActionPerformed
        // TODO add your handling code here:        
    }//GEN-LAST:event_cboPurityOfGoldActionPerformed
    
    public void authorizeStatus(String authorizeStatus) {

            if (viewType != AUTHORIZE){
                viewType = AUTHORIZE;
                //__ To Save the data in the Internal Frame...
                setModified(true);
                HashMap mapParam = new HashMap();
                mapParam.put(CommonConstants.MAP_NAME, "getSelectNotAuthRecordsinAuthMode");
                HashMap whereMap = new HashMap();
                whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                mapParam.put(CommonConstants.MAP_WHERE, whereMap);
                mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeAgentData");

                whereMap = null;

                AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
                authorizeUI.show();

                btnSave.setEnabled(false);

                //__ If there's no data to be Authorized, call Cancel action...  
                if(!isModified()){
                    setButtonEnableDisable();
                    btnCancelActionPerformed(null);     
                }

            } else if (viewType == AUTHORIZE){
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
                    singleAuthorizeMap.put("AUTHORIZE_STATUS", authorizeStatus);
                    singleAuthorizeMap.put("AUTHORIZE_BY", TrueTransactMain.USER_ID);
                    singleAuthorizeMap.put("AUTHORIZE_DT",currDate.clone());
                    singleAuthorizeMap.put("SET_NO",CommonUtil.convertObjToInt(observable.getSetNo()));
                    System.out.println("singleAuthorizeMap: " + singleAuthorizeMap);
                    ClientUtil.execute("updateAuthorizeStatus", singleAuthorizeMap);
                }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                    long oldvalue = (observable.getSetNo() - 1);                
                    singleAuthorizeMap.put("SET_NO",String.valueOf(oldvalue));                
                    ClientUtil.execute("updateNullValueforToDate", singleAuthorizeMap);
                    HashMap rejectTable = new HashMap();
                    rejectTable.put("SET_NO",new Long(observable.getSetNo()));
                    rejectTable.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                    ClientUtil.execute("rejectGoldConfigurationTO", rejectTable);
                }
                super.setOpenForEditBy(observable.getStatusBy());
                viewType = -1;
                btnSave.setEnabled(true);
                btnCancelActionPerformed(null);
                lblStatus1.setText(authorizeStatus);
            }
        }     
    
    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost
        // TODO add your handling code here:
//        Date daybeDt=ClientUtil.getCurrentDate();
//        Date appDt= DateUtil.getDateMMDDYYYY(tdtApptDate.getDateValue());
//        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
//           if(appDt!=null)
//            if(DateUtil.dateDiff(daybeDt,appDt)>0){
//            ClientUtil.displayAlert("Appointed Date is grater then DaybeginDate");
//            tdtApptDate.setDateValue("");
//        }
//        }

    }//GEN-LAST:event_tdtFromDateFocusLost
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
    
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        HashMap whereMap = new HashMap();
        HashMap operativeMap = new HashMap();
        HashMap depositMap = new HashMap();
        whereMap.put("BRANCHID", getSelectedBranchID());
        if(field == EDIT || field == DELETE || field == AUTHORIZE || field == VIEW){
            if(field == VIEW || field == AUTHORIZE ){
                viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                viewMap.put(CommonConstants.MAP_NAME, "getSelectNotAuthRecords");
                new ViewAll(this, viewMap, false).show();
            }
            if(field == DELETE){
                viewType = DELETE;
                observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
                whereMap.put("BRANCHID", TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                viewMap.put(CommonConstants.MAP_NAME, "getSelectNotAuthRecordsinAuthMode");
                fillData(viewMap);
            }if(field == EDIT){
                viewType = EDIT;
                observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
                whereMap.put("BRANCHID", TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                viewMap.put(CommonConstants.MAP_NAME, "getSelectNotAuthRecords");
                fillData(viewMap);
            }
        }
    }
    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        System.out.println("hash:  " + hash);
        
        if (viewType == EDIT || viewType == DELETE || viewType == AUTHORIZE || viewType == VIEW) {
            hash.put("BRANCHID", getSelectedBranchID());
            observable.populateData(hash);     //__ Called to display the Data in the UI fields...
            //__ If the Action type is Delete...
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || 
            observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
            ClientUtil.enableDisable(this, true);     //__ Anable the panel...
            if(viewType==DELETE){
                btnSave.setEnabled(true);
            }else{
                btnSave.setEnabled(false);
            }
            btnPrint.setEnabled(false);
            observable.setStatus();             //__ To set the Value of lblStatus...
            setButtonEnableDisable();           //__ Enables or Disables the buttons and menu Items depending on their previous state...
            ClientUtil.enableDisable(panGoldData,false);
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(null);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // TODO add your handling code here:
        btnPrintActionPerformed(null);
    }//GEN-LAST:event_mitPrintActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(null);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(null);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(null);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(null);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(null);
    }//GEN-LAST:event_mitNewActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    private void initComponentData() {
        try{
            cboPurityOfGold.setModel(observable.getCbmPurityOfGold());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new GoldConfigurationUI().show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnGoldDel;
    private com.see.truetransact.uicomponent.CButton btnGoldNew;
    private com.see.truetransact.uicomponent.CButton btnGoldSave;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboPurityOfGold;
    private com.see.truetransact.uicomponent.CCheckBox chkDefault;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblDate1;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPerGramRate;
    private com.see.truetransact.uicomponent.CLabel lblPurityOfGold;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace35;
    private com.see.truetransact.uicomponent.CLabel lblSpace36;
    private com.see.truetransact.uicomponent.CLabel lblSpace37;
    private com.see.truetransact.uicomponent.CLabel lblSpace38;
    private com.see.truetransact.uicomponent.CLabel lblSpace39;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CMenuBar mbrGold;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panBtnDepSubNoAcc;
    private com.see.truetransact.uicomponent.CPanel panGold;
    private com.see.truetransact.uicomponent.CPanel panGoldData;
    private com.see.truetransact.uicomponent.CPanel panGoldDisplay;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpGold;
    private com.see.truetransact.uicomponent.CTable tblGoldMarketRate;
    private javax.swing.JToolBar tbrGoldProduct;
    private com.see.truetransact.uicomponent.CDateField tdtDate;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CTextField txtPerGramRate;
    // End of variables declaration//GEN-END:variables
    
}
