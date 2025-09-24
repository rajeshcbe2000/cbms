  /*
   * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
   *
   * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
   * 
   * SHGUI.java
   *
   * Created on November 26, 2003, 11:27 AM
   */

package com.see.truetransact.ui.termloan.SHG;

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

/**
 *
 * @author Suresh
 *
 **/

public class SHGUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField{
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.SHG.SHGRB", ProxyParameters.LANGUAGE);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String viewType = new String();
    SHGOB observable = null;
    final String AUTHORIZE="Authorize";
    HashMap mandatoryMap = null;
    boolean isFilled = false;
    private HashMap productMap = new HashMap();
    private boolean updateMode = false;
    int updateTab=-1;
    /** Creates new form BeanForm */
    public SHGUI() {
        initComponents();
        initForm();
    }
    
    private void initForm(){
        setFieldNames();
        internationalize();
        observable = new SHGOB();
        setMaxLengths();
        initComponentData();
        setButtonEnableDisable();
        buttonEnableDisable(false);
        ClientUtil.enableDisable(panSHGDetails,false);
        btnMemberNo.setEnabled(false);
        txtGroupId.setEnabled(false);
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
        btnMemberNo.setName("btnMemberNo");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSHGDelete.setName("btnSHGDelete");
        btnSHGNew.setName("btnSHGNew");
        btnSHGSave.setName("btnSHGSave");
        btnSave.setName("btnSave");
        btnView.setName("btnView");
        lblArea.setName("lblArea");
        lblAreaVal.setName("lblAreaVal");
        lblCity.setName("lblCity");
        lblCityVal.setName("lblCityVal");
        lblGroupId.setName("lblGroupId");
        lblMemberName.setName("lblMemberName");
        lblMemberNameVal.setName("lblMemberNameVal");
        lblMemberNo.setName("lblMemberNo");
        lblMsg.setName("lblMsg");
        lblSpace.setName("lblSpace");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        lblState.setName("lblState");
        lblStateVal.setName("lblStateVal");
        lblStatus.setName("lblStatus");
        lblStreet.setName("lblStreet");
        lblStreetVal.setName("lblStreetVal");
        mbrMain.setName("mbrMain");
        panMemberNo.setName("panMemberNo");
        panSHGBtn.setName("panSHGBtn");
        panSHGDetails.setName("panSHGDetails");
        panSHGRecordDetails.setName("panSHGRecordDetails");
        panSHGTableDetails.setName("panSHGTableDetails");
        panStatus.setName("panStatus");
        srpSHGTable.setName("srpSHGTable");
        tabSHGDetails.setName("tabSHGDetails");
        tblSHGDetails.setName("tblSHGDetails");
        txtGroupId.setName("txtGroupId");
        txtMemberNo.setName("txtMemberNo");
    }
    
    private void setMaxLengths(){
        txtGroupName.setAllowAll(true);
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        resourceBundle = new SHGRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblStreet.setText(resourceBundle.getString("lblStreet"));
        lblCity.setText(resourceBundle.getString("lblCity"));
        lblStreetVal.setText(resourceBundle.getString("lblStreetVal"));
        lblGroupId.setText(resourceBundle.getString("lblGroupId"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblAreaVal.setText(resourceBundle.getString("lblAreaVal"));
        lblMemberNo.setText(resourceBundle.getString("lblMemberNo"));
        btnSHGSave.setText(resourceBundle.getString("btnSHGSave"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblArea.setText(resourceBundle.getString("lblArea"));
        btnView.setText(resourceBundle.getString("btnView"));
        lblCityVal.setText(resourceBundle.getString("lblCityVal"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnMemberNo.setText(resourceBundle.getString("btnMemberNo"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblStateVal.setText(resourceBundle.getString("lblStateVal"));
        lblMemberNameVal.setText(resourceBundle.getString("lblMemberNameVal"));
        btnSHGNew.setText(resourceBundle.getString("btnSHGNew"));
        lblMemberName.setText(resourceBundle.getString("lblMemberName"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblState.setText(resourceBundle.getString("lblState"));
        lblSpace6.setText(resourceBundle.getString("lblSpace6"));
        btnSHGDelete.setText(resourceBundle.getString("btnSHGDelete"));
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
            cboProdId.setModel(observable.getCbmProdId());
            tblSHGDetails.setModel(observable.getTblSHGDetails());
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
        txtMemberNo.setText(observable.getTxtMemberNo());
        txtGroupId.setText(observable.getTxtGroupId());
        txtGroupName.setText(observable.getTxtGroupName());
        txtAccountNo.setText(observable.getTxtAccountNo());
        cboProdId.setSelectedItem(observable.getCboProdId());
        lblAreaVal.setText(observable.getLblAreaVal());
        lblCityVal.setText(observable.getLblCityVal());
        lblMemberNameVal.setText(observable.getLblMemberNameVal());
        lblStateVal.setText(observable.getLblStateVal());
        lblStreetVal.setText(observable.getLblStreetVal());
    }
    
    /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtMemberNo(txtMemberNo.getText());
        observable.setTxtGroupId(txtGroupId.getText());
        observable.setCboProdId((String) cboProdId.getSelectedItem());
        observable.setTxtGroupName(txtGroupName.getText());
        observable.setTxtAccountNo(txtAccountNo.getText());
        observable.setLblAreaVal(lblAreaVal.getText());
        observable.setLblCityVal(lblCityVal.getText());
        observable.setLblMemberNameVal(lblMemberNameVal.getText());
        observable.setLblStateVal(lblStateVal.getText());
        observable.setLblStreetVal(lblStreetVal.getText());
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
        btnSHGNew.setEnabled(flag);
        btnSHGSave.setEnabled(flag);
        btnSHGDelete.setEnabled(flag);
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
        tabSHGDetails = new com.see.truetransact.uicomponent.CTabbedPane();
        panSHGDetails = new com.see.truetransact.uicomponent.CPanel();
        panSHGRecordDetails = new com.see.truetransact.uicomponent.CPanel();
        panMemberNo = new com.see.truetransact.uicomponent.CPanel();
        txtMemberNo = new com.see.truetransact.uicomponent.CTextField();
        btnMemberNo = new com.see.truetransact.uicomponent.CButton();
        lblStreet = new com.see.truetransact.uicomponent.CLabel();
        lblMemberName = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        lblArea = new com.see.truetransact.uicomponent.CLabel();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        lblGroupName = new com.see.truetransact.uicomponent.CLabel();
        lblMemberNo = new com.see.truetransact.uicomponent.CLabel();
        txtGroupId = new com.see.truetransact.uicomponent.CTextField();
        lblStateVal = new com.see.truetransact.uicomponent.CLabel();
        panSHGBtn = new com.see.truetransact.uicomponent.CPanel();
        btnSHGNew = new com.see.truetransact.uicomponent.CButton();
        btnSHGSave = new com.see.truetransact.uicomponent.CButton();
        btnSHGDelete = new com.see.truetransact.uicomponent.CButton();
        lblAreaVal = new com.see.truetransact.uicomponent.CLabel();
        lblCityVal = new com.see.truetransact.uicomponent.CLabel();
        lblStreetVal = new com.see.truetransact.uicomponent.CLabel();
        lblMemberNameVal = new com.see.truetransact.uicomponent.CLabel();
        lblGroupId = new com.see.truetransact.uicomponent.CLabel();
        panAccountNo = new com.see.truetransact.uicomponent.CPanel();
        txtAccountNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNo = new com.see.truetransact.uicomponent.CButton();
        lblAccountNo = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        txtGroupName = new com.see.truetransact.uicomponent.CTextField();
        panSHGTableDetails = new com.see.truetransact.uicomponent.CPanel();
        srpSHGTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblSHGDetails = new com.see.truetransact.uicomponent.CTable();
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
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
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

        tabSHGDetails.setMinimumSize(new java.awt.Dimension(550, 490));
        tabSHGDetails.setPreferredSize(new java.awt.Dimension(550, 490));

        panSHGDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSHGDetails.setMinimumSize(new java.awt.Dimension(570, 450));
        panSHGDetails.setPreferredSize(new java.awt.Dimension(570, 450));
        panSHGDetails.setLayout(new java.awt.GridBagLayout());

        panSHGRecordDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSHGRecordDetails.setMinimumSize(new java.awt.Dimension(375, 440));
        panSHGRecordDetails.setPreferredSize(new java.awt.Dimension(375, 440));
        panSHGRecordDetails.setLayout(new java.awt.GridBagLayout());

        panMemberNo.setLayout(new java.awt.GridBagLayout());

        txtMemberNo.setAllowAll(true);
        txtMemberNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMemberNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMemberNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMemberNo.add(txtMemberNo, gridBagConstraints);

        btnMemberNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMemberNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnMemberNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMemberNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMemberNo.setEnabled(false);
        btnMemberNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMemberNo.add(btnMemberNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panSHGRecordDetails.add(panMemberNo, gridBagConstraints);

        lblStreet.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSHGRecordDetails.add(lblStreet, gridBagConstraints);

        lblMemberName.setText("Member Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSHGRecordDetails.add(lblMemberName, gridBagConstraints);

        lblCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSHGRecordDetails.add(lblCity, gridBagConstraints);

        lblArea.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSHGRecordDetails.add(lblArea, gridBagConstraints);

        lblState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSHGRecordDetails.add(lblState, gridBagConstraints);

        lblGroupName.setText("Group Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSHGRecordDetails.add(lblGroupName, gridBagConstraints);

        lblMemberNo.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSHGRecordDetails.add(lblMemberNo, gridBagConstraints);

        txtGroupId.setBackground(new java.awt.Color(204, 204, 204));
        txtGroupId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panSHGRecordDetails.add(txtGroupId, gridBagConstraints);

        lblStateVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblStateVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblStateVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panSHGRecordDetails.add(lblStateVal, gridBagConstraints);

        panSHGBtn.setMinimumSize(new java.awt.Dimension(95, 35));
        panSHGBtn.setPreferredSize(new java.awt.Dimension(95, 35));
        panSHGBtn.setLayout(new java.awt.GridBagLayout());

        btnSHGNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnSHGNew.setToolTipText("New");
        btnSHGNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSHGNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSHGNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSHGNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSHGNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSHGBtn.add(btnSHGNew, gridBagConstraints);

        btnSHGSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSHGSave.setToolTipText("Save");
        btnSHGSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSHGSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSHGSave.setName("btnContactNoAdd");
        btnSHGSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSHGSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSHGSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSHGBtn.add(btnSHGSave, gridBagConstraints);

        btnSHGDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnSHGDelete.setToolTipText("Delete");
        btnSHGDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSHGDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSHGDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSHGDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSHGDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSHGBtn.add(btnSHGDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(21, 0, 0, 15);
        panSHGRecordDetails.add(panSHGBtn, gridBagConstraints);

        lblAreaVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblAreaVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblAreaVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panSHGRecordDetails.add(lblAreaVal, gridBagConstraints);

        lblCityVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblCityVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblCityVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panSHGRecordDetails.add(lblCityVal, gridBagConstraints);

        lblStreetVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblStreetVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblStreetVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panSHGRecordDetails.add(lblStreetVal, gridBagConstraints);

        lblMemberNameVal.setForeground(new java.awt.Color(0, 51, 204));
        lblMemberNameVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblMemberNameVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblMemberNameVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panSHGRecordDetails.add(lblMemberNameVal, gridBagConstraints);

        lblGroupId.setText("Group ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSHGRecordDetails.add(lblGroupId, gridBagConstraints);

        panAccountNo.setLayout(new java.awt.GridBagLayout());

        txtAccountNo.setAllowAll(true);
        txtAccountNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountNo.add(txtAccountNo, gridBagConstraints);

        btnAccountNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAccountNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAccountNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccountNo.setEnabled(false);
        btnAccountNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountNo.add(btnAccountNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panSHGRecordDetails.add(panAccountNo, gridBagConstraints);

        lblAccountNo.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSHGRecordDetails.add(lblAccountNo, gridBagConstraints);

        cboProdId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(225);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panSHGRecordDetails.add(cboProdId, gridBagConstraints);

        lblProdId.setText("Advances Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSHGRecordDetails.add(lblProdId, gridBagConstraints);

        txtGroupName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panSHGRecordDetails.add(txtGroupName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        panSHGDetails.add(panSHGRecordDetails, gridBagConstraints);

        panSHGTableDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSHGTableDetails.setMinimumSize(new java.awt.Dimension(425, 440));
        panSHGTableDetails.setPreferredSize(new java.awt.Dimension(425, 440));
        panSHGTableDetails.setLayout(new java.awt.GridBagLayout());

        srpSHGTable.setMinimumSize(new java.awt.Dimension(400, 420));
        srpSHGTable.setPreferredSize(new java.awt.Dimension(400, 420));

        tblSHGDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Member No", "Member Name"
            }
        ));
        tblSHGDetails.setMinimumSize(new java.awt.Dimension(400, 1000));
        tblSHGDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(250, 400));
        tblSHGDetails.setPreferredSize(new java.awt.Dimension(400, 1000));
        tblSHGDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSHGDetailsMousePressed(evt);
            }
        });
        srpSHGTable.setViewportView(tblSHGDetails);

        panSHGTableDetails.add(srpSHGTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSHGDetails.add(panSHGTableDetails, gridBagConstraints);

        tabSHGDetails.addTab("SHG Details", panSHGDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabSHGDetails, gridBagConstraints);

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

    private void btnAccountNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNoActionPerformed
        // TODO add your handling code here:
        callView("ACCOUNT_NO");
        viewType = "ACCOUNT_NO";
    }//GEN-LAST:event_btnAccountNoActionPerformed

    private void btnSHGDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSHGDeleteActionPerformed
        // TODO add your handling code here:
        String memberNo=  CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(),0));
        List loanLst = null;
        if(observable.getActionType()!=ClientConstants.ACTIONTYPE_NEW){
            HashMap whereMap = new HashMap();
            whereMap.put("MEMBERSHIP_NO",memberNo);
            whereMap.put("SHG_ID",txtGroupId.getText());
            loanLst=ClientUtil.executeQuery("CheckSHGLoanDetails",whereMap);
        }
        if(loanLst !=null && loanLst.size()>0){
            ClientUtil.showMessageWindow("This Member already having Loan, Cannot Delete !!!");
        }else{
            observable.deleteTableData(memberNo,tblSHGDetails.getSelectedRow());
            observable.resetSHGDetails();
            resetSHGDetails();
            buttonEnableDisable(false);
            btnSHGNew.setEnabled(true);
        }
    }//GEN-LAST:event_btnSHGDeleteActionPerformed

    private void txtMemberNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMemberNoFocusLost
        // TODO add your handling code here:
        if(txtMemberNo.getText().length()>0){
            HashMap whereMap = new HashMap();
            whereMap.put("MEMBERSHIP_NO",txtMemberNo.getText());
            List lst=ClientUtil.executeQuery("findExistingMemberOrNot",whereMap);
            if(lst !=null && lst.size()>0){
                viewType = "MEMBER_NO";
                whereMap=(HashMap)lst.get(0);
                fillData(whereMap);
                lst=null;
                whereMap=null;
            }else{
                ClientUtil.displayAlert("Invalid Member No !!! ");
                resetSHGDetails();
            }
        }
    }//GEN-LAST:event_txtMemberNoFocusLost

    private void tblSHGDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSHGDetailsMousePressed
        // TODO add your handling code here:
        updateOBFields();
        updateMode = true;
        updateTab= tblSHGDetails.getSelectedRow();
        observable.setNewData(false);
        String st=CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(),0));
        observable.populateSHGDetails(st);
        update();
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE||observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW || observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE ||
        observable.getActionType()==ClientConstants.ACTIONTYPE_EXCEPTION){
            buttonEnableDisable(false);
            ClientUtil.enableDisable(panSHGRecordDetails,false);
        }else {
            buttonEnableDisable(true);
            btnSHGNew.setEnabled(false);
            btnMemberNo.setEnabled(false);
            txtMemberNo.setEnabled(false);
        }
    }//GEN-LAST:event_tblSHGDetailsMousePressed

    private void btnSHGSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSHGSaveActionPerformed
        // TODO add your handling code here:
        try{
                updateOBFields();
                String memberNo = txtMemberNo.getText();
                if(memberNo.length()>0){
                    observable.addToTable(updateTab,updateMode);
                    tblSHGDetails.setModel(observable.getTblSHGDetails());
                    observable.resetSHGDetails();
                    resetSHGDetails();
                    ClientUtil.enableDisable(panSHGRecordDetails,false);
                    buttonEnableDisable(false);
                    btnSHGNew.setEnabled(true);
                    btnMemberNo.setEnabled(false);
                    btnAccountNo.setEnabled(false);
                    txtGroupName.setEnabled(false);
                }else{
                    ClientUtil.showAlertWindow("Member No Should Not be Empty !!!");
                }
                
                
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSHGSaveActionPerformed
    private void resetSHGDetails(){
        txtMemberNo.setText("");
        lblMemberNameVal.setText("");
        lblStreetVal.setText("");
        lblAreaVal.setText("");
        lblCityVal.setText("");
        lblStateVal.setText("");
    }
    private void btnSHGNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSHGNewActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        btnMemberNo.setEnabled(true);
        buttonEnableDisable(false);
        btnSHGSave.setEnabled(true);
        observable.setNewData(true);
        txtMemberNo.setEnabled(true);
        if(tblSHGDetails.getRowCount()<=0) {
            txtGroupName.setEnabled(true);
            cboProdId.setEnabled(true);
            btnAccountNo.setEnabled(true);
        }
    }//GEN-LAST:event_btnSHGNewActionPerformed

    private void btnMemberNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberNoActionPerformed
        // TODO add your handling code here:
//        callView("MEMBER_NO");  
        viewType = "MEMBER_NO";
        new CheckCustomerIdUI(this);
    }//GEN-LAST:event_btnMemberNoActionPerformed
     
    /** To display a popUp window for viewing existing data */
    private void callView(String currAction){
        viewType = currAction;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")){
            viewMap.put(CommonConstants.MAP_NAME, "getSHGEditDelete");
        }else if(currAction.equalsIgnoreCase("Delete")){
            viewMap.put(CommonConstants.MAP_NAME, "getSHGDelete");
        }else if(currAction.equalsIgnoreCase("Enquiry")){
            viewMap.put(CommonConstants.MAP_NAME, "getSHGView");
        }else if(viewType.equals("ACCOUNT_NO")){
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListAD");
            whereMap.put("SELECTED_BRANCH", getSelectedBranchID());
            whereMap.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
        new ViewAll(this,viewMap).show();
    }
    
    public void fillData(Object obj){
        try{
            HashMap hashMap=(HashMap)obj;
            System.out.println("### fillData Hash : "+hashMap);
            isFilled = true;
             List lst1=null;
            if(viewType == "MEMBER_NO"){
                if(hashMap.containsKey("MEMBER_NO") || hashMap.containsKey("NAME")){
                    lst1=ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashMap);
                    hashMap.put("MEMBERSHIP_NO",hashMap.get("MEMBER_NO"));
                    hashMap.put("CUSTOMER",hashMap.get("NAME"));
                }
                txtMemberNo.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBERSHIP_NO")));
                lblMemberNameVal.setText(CommonUtil.convertObjToStr(hashMap.get("CUSTOMER")));
                if(lst1!=null && lst1.size()>0){
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    txtMemberNo.setText("");
                    lblMemberNameVal.setText("");
                    return;
                }
                observable.setTxtMemberNo(txtMemberNo.getText());
                observable.setLblMemberNameVal(lblMemberNameVal.getText());
                String memberNo = txtMemberNo.getText();
                if(tblSHGDetails.getRowCount()>0) {
                    for(int i=0;i<tblSHGDetails.getRowCount();i++){
                        String membNo = CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(i,0));
                        if(memberNo.equalsIgnoreCase(membNo) && !updateMode) {
                            ClientUtil.displayAlert("Member No Already Exists in this Table");
                            resetSHGDetails();
                            buttonEnableDisable(false);
                            btnSHGNew.setEnabled(true);
                            btnMemberNo.setEnabled(false);
                            txtMemberNo.setEnabled(false);
                            return;
                        }
                    }
                }
                observable.getCustomerAddressDetails(CommonUtil.convertObjToStr(hashMap.get("CUST_ID")));
                ClientUtil.enableDisable(panSHGRecordDetails,false);
                observable.setCboProdId((String) cboProdId.getSelectedItem());
                update();
                return;
            }else if(viewType == "ACCOUNT_NO"){
                txtAccountNo.setText(CommonUtil.convertObjToStr(hashMap.get("ACCOUNTNO")));
                observable.setTxtAccountNo(CommonUtil.convertObjToStr(hashMap.get("ACCOUNTNO")));
                observable.setTxtGroupName(txtGroupName.getText());
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                this.setButtonEnableDisable();
                txtGroupId.setText(CommonUtil.convertObjToStr(hashMap.get("SHG_ID")));
                observable.getData(hashMap);
                observable.setTxtGroupId(CommonUtil.convertObjToStr(hashMap.get("SHG_ID")));
                observable.setTxtGroupName(CommonUtil.convertObjToStr(hashMap.get("GROUP_NAME")));
                txtGroupName.setText(CommonUtil.convertObjToStr(hashMap.get("GROUP_NAME")));
                observable.setCboProdId(CommonUtil.convertObjToStr(observable.getCbmProdId().getDataForKey(CommonUtil.convertObjToStr(hashMap.get("PROD_ID")))));
                cboProdId.setSelectedItem(observable.getCboProdId());
                observable.setTxtAccountNo(CommonUtil.convertObjToStr(hashMap.get("ACCOUNT_NO")));
                txtAccountNo.setText(CommonUtil.convertObjToStr(hashMap.get("ACCOUNT_NO")));
                tblSHGDetails.setModel(observable.getTblSHGDetails());
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                this.setButtonEnableDisable();
                txtGroupId.setText(CommonUtil.convertObjToStr(hashMap.get("SHG_ID")));
                observable.getData(hashMap);
                observable.setTxtGroupId(CommonUtil.convertObjToStr(hashMap.get("SHG_ID")));
                observable.setTxtGroupName(CommonUtil.convertObjToStr(hashMap.get("GROUP_NAME")));
                txtGroupName.setText(CommonUtil.convertObjToStr(hashMap.get("GROUP_NAME")));
                observable.setCboProdId(CommonUtil.convertObjToStr(observable.getCbmProdId().getDataForKey(CommonUtil.convertObjToStr(hashMap.get("PROD_ID")))));
                cboProdId.setSelectedItem(observable.getCboProdId());
                observable.setTxtAccountNo(CommonUtil.convertObjToStr(hashMap.get("ACCOUNT_NO")));
                txtAccountNo.setText(CommonUtil.convertObjToStr(hashMap.get("ACCOUNT_NO")));
                tblSHGDetails.setModel(observable.getTblSHGDetails());
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
            singleAuthorizeMap.put("SHG_ID", txtGroupId.getText());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT",ClientUtil.getCurrentDateWithTime());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap,txtGroupId.getText());
            viewType = "";
            ClientUtil.enableDisable(panSHGDetails,false);
//            super.setOpenForEditBy(observable.getStatusBy());
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
            mapParam.put(CommonConstants.MAP_NAME, "getSHGAuthorize");
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
            observable.doAction();
//            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
//                super.setOpenForEditBy(observable.getStatusBy());
//                super.removeEditLock(id);
//            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        btnSHGNew.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        ClientUtil.clearAll(this);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
        lblStatus.setText("Edit");
        btnSHGNew.setEnabled(true);
//        btnDelete.setEnabled(false);
//        btnNew.setEnabled(false);
//        btnEdit.setEnabled(false);
//        btnDelete.setEnabled(false);
//        btnSave.setEnabled(true);
//        btnView.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panSHGDetails,false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
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
        btnMemberNo.setEnabled(false);
        btnAccountNo.setEnabled(false);
        resetSHGDetails();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
//    private void populateAddressData(HashMap addressMap){
//        lblValStreet.setText(CommonUtil.convertObjToStr(addressMap.get("HOUSE_ST")));
//        lblValArea.setText(CommonUtil.convertObjToStr(addressMap.get("AREA")));
//        lblValCity.setText(CommonUtil.convertObjToStr(addressMap.get("CITY")));
//        lblValState.setText(CommonUtil.convertObjToStr(addressMap.get("STATE")));
//        lblValPin.setText(CommonUtil.convertObjToStr(addressMap.get("PIN")));
//    }
    
    
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
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccountNo;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnMemberNo;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSHGDelete;
    private com.see.truetransact.uicomponent.CButton btnSHGNew;
    private com.see.truetransact.uicomponent.CButton btnSHGSave;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CLabel lblAccountNo;
    private com.see.truetransact.uicomponent.CLabel lblArea;
    private com.see.truetransact.uicomponent.CLabel lblAreaVal;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblCityVal;
    private com.see.truetransact.uicomponent.CLabel lblGroupId;
    private com.see.truetransact.uicomponent.CLabel lblGroupName;
    private com.see.truetransact.uicomponent.CLabel lblMemberName;
    private com.see.truetransact.uicomponent.CLabel lblMemberNameVal;
    private com.see.truetransact.uicomponent.CLabel lblMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
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
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStateVal;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStreet;
    private com.see.truetransact.uicomponent.CLabel lblStreetVal;
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
    private com.see.truetransact.uicomponent.CPanel panAccountNo;
    private com.see.truetransact.uicomponent.CPanel panMemberNo;
    private com.see.truetransact.uicomponent.CPanel panSHGBtn;
    private com.see.truetransact.uicomponent.CPanel panSHGDetails;
    private com.see.truetransact.uicomponent.CPanel panSHGRecordDetails;
    private com.see.truetransact.uicomponent.CPanel panSHGTableDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdgEFTProductGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIsLapsedGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPayableBranchGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrintServicesGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSeriesGR;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CScrollPane srpSHGTable;
    private com.see.truetransact.uicomponent.CTabbedPane tabSHGDetails;
    private com.see.truetransact.uicomponent.CTable tblSHGDetails;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CTextField txtAccountNo;
    private com.see.truetransact.uicomponent.CTextField txtGroupId;
    private com.see.truetransact.uicomponent.CTextField txtGroupName;
    private com.see.truetransact.uicomponent.CTextField txtMemberNo;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] arg){
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        SHGUI gui = new SHGUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }

}