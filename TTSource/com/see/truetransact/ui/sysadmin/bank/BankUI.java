/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * BankUI.java
 *
 * Created on February 5, 2004, 12:16 PM
 */

package com.see.truetransact.ui.sysadmin.bank;

/**
 *
 * @author  Hemant
 */
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uivalidation.IPValidation;
import com.see.truetransact.uivalidation.EmailValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import java.util.Date;
public class BankUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {
    
    private BankRB resourceBundle = new BankRB();
    private HashMap mandatoryMap;
    private BankOB observable;
    private BankMRB objMandatoryRB;
    private boolean isFilled = false;
    private final        String AUTHORIZE = "AUTHORIZE";
    
    private String viewType = "";
    private Date currDt = null;
    /** Creates new form BeanForm */
    public BankUI() {
        currDt = ClientUtil.getCurrentDate();
        settingupUI();
        setInvisble();
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW_MODE);
        observable.setStatus();
        fillData(null);
        tbrAdvances.setVisible(false);
        mnuProcess.setVisible(false);
        txtWebsite.setEnabled(false);
        txtSiteIP.setEditable(false);
        txtSupportEmail.setEditable(false);
        txtDataIP.setEditable(false);
        tdtBankOpeningDate.setEnabled(false);
        cboHours.setEnabled(false);
        cboMins.setEnabled(false);
    }
    
    //--- Sets the following components Invisible
    private void setInvisble(){
        lblBaseCurrency.setVisible(false);
        cboBaseCurrency.setVisible(false);
        lblConversion.setVisible(false);
        cboConversion.setVisible(false);
        lblTranPosting.setVisible(false);
        cboTranPosting.setVisible(false);
        lblCashLimit.setVisible(false);
        txtCashLimit.setVisible(false);
        panTransactions.setVisible(false);
        txtBankCode.setEditable(false);
        txtBankName.setEditable(false);
        
    }
    
    private void settingupUI(){
        initComponents();
        btnNew.setVisible(false);
        btnDelete.setVisible(false);
        btnEdit.setEnabled(false);
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        initComponentData();
        setHelpMessage();
        setComponentLength();
        btnCancelActionPerformed(null);
    }
    private void setObservable() {
        observable = BankOB.getInstance();
        observable.addObserver(this);
    }
    
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        btnAuthorize.setName("btnAuthorize");
        btnReject.setName("btnReject");
        btnException.setName("btnException");
        cboConversion.setName("cboConversion");
        cboHours.setName("cboHours");
        cboMins.setName("cboMins");
        cboTranPosting.setName("cboTranPosting");
        cboBaseCurrency.setName("cboBaseCurrency");
        lblBaseCurrency.setName("lblBaseCurrency");
        lblB2B.setName("lblB2B");
        lblBankCode.setName("lblBankCode");
        lblBankName.setName("lblBankName");
        lblBankOpeningDate.setName("lblBankOpeningDate");
        tdtBankOpeningDate.setName("tdtBankOpeningDate");
        lblCashLimit.setName("lblCashLimit");
        lblConversion.setName("lblConversion");
        lblDataIP.setName("lblDataIP");
        lblDayEndProcessTime.setName("lblDayEndProcessTime");
        lblHours.setName("lblHours");
        lblMins.setName("lblMins");
        lblMsg.setName("lblMsg");
        lblSiteIP.setName("lblSiteIP");
        lblSpace.setName("lblSpace");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        lblT2T.setName("lblT2T");
        lblTranPosting.setName("lblTranPosting");
        lblWebsite.setName("lblWebsite");
        mbrMain.setName("mbrMain");
        panDEPTime.setName("panDEPTime");
        panMainBankUI.setName("panMainBankUI");
        panStatus.setName("panStatus");
        panTransactions.setName("panTransactions ");
        rdoB2B_No.setName("rdoB2B_No");
        rdoB2B_Yes.setName("rdoB2B_Yes");
        rdoT2T_No.setName("rdoT2T_No");
        rdoT2T_Yes.setName("rdoT2T_Yes");
        txtBankCode.setName("txtBankCode");
        txtBankName.setName("txtBankName");
        txtCashLimit.setName("txtCashLimit");
        txtDataIP.setName("txtDataIP");
        txtSiteIP.setName("txtSiteIP");
        txtWebsite.setName("txtWebsite");
        txtSupportEmail.setName("txtSupportEmail");
    }
    
    private void internationalize() {
        lblBankOpeningDate.setText(resourceBundle.getString("lblBankOpeningDate"));
        lblBankName.setText(resourceBundle.getString("lblBankName"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        rdoB2B_No.setText(resourceBundle.getString("rdoB2B_No"));
        rdoT2T_Yes.setText(resourceBundle.getString("rdoT2T_Yes"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblMins.setText(resourceBundle.getString("lblMins"));
        rdoT2T_No.setText(resourceBundle.getString("rdoT2T_No"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblT2T.setText(resourceBundle.getString("lblT2T"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblDataIP.setText(resourceBundle.getString("lblDataIP"));
        lblCashLimit.setText(resourceBundle.getString("lblCashLimit"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblSiteIP.setText(resourceBundle.getString("lblSiteIP"));
        lblWebsite.setText(resourceBundle.getString("lblWebsite"));
        ((javax.swing.border.TitledBorder)panMainBankUI.getBorder()).setTitle(resourceBundle.getString("panMainBankUI"));
        lblHours.setText(resourceBundle.getString("lblHours"));
        rdoB2B_Yes.setText(resourceBundle.getString("rdoB2B_Yes"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnException.setText(resourceBundle.getString("btnException"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        lblDayEndProcessTime.setText(resourceBundle.getString("lblDayEndProcessTime"));
        lblTranPosting.setText(resourceBundle.getString("lblTranPosting"));
        lblConversion.setText(resourceBundle.getString("lblConversion"));
        lblBaseCurrency.setText(resourceBundle.getString("lblBaseCurrency"));
        lblBankCode.setText(resourceBundle.getString("lblBankCode"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblB2B.setText(resourceBundle.getString("lblB2B"));
        ((javax.swing.border.TitledBorder)panTransactions.getBorder()).setTitle(resourceBundle.getString("panTransactions"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtBankCode", new Boolean(true));
        mandatoryMap.put("txtBankName", new Boolean(true));
        mandatoryMap.put("txtWebsite", new Boolean(true));
        mandatoryMap.put("txtSiteIP", new Boolean(true));
        mandatoryMap.put("txtDataIP", new Boolean(true));
        mandatoryMap.put("txtCashLimit", new Boolean(true));
        mandatoryMap.put("cboConversion", new Boolean(true));
        mandatoryMap.put("cboTranPosting", new Boolean(true));
        mandatoryMap.put("rdoT2T_Yes", new Boolean(true));
        mandatoryMap.put("rdoB2B_Yes", new Boolean(true));
        mandatoryMap.put("cboHours", new Boolean(true));
        mandatoryMap.put("cboMins", new Boolean(true));
        mandatoryMap.put("cboBaseCurrency", new Boolean(true));
        mandatoryMap.put("txtSupportEmail", new Boolean(true));
        mandatoryMap.put("tdtBankOpeningDate", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    public static void main(String[] arg){
        javax.swing.JFrame jf = new javax.swing.JFrame();
        
        BankUI bui = new BankUI();
        jf.setSize(300,525);
        jf.getContentPane().add(bui);
        jf.show();
        bui.show();
        
    }
    
    public void update(Observable observed, Object arg) {
        tdtBankOpeningDate.setDateValue(observable.getTdtBankOpeningDate());
        txtBankCode.setText(observable.getTxtBankCode());
        txtBankName.setText(observable.getTxtBankName());
        txtWebsite.setText(observable.getTxtWebsite());
        txtSiteIP.setText(observable.getTxtSiteIP());
        txtDataIP.setText(observable.getTxtDataIP());
        txtCashLimit.setText(observable.getTxtCashLimit());
        cboConversion.setSelectedItem(observable.getCboConversion());
        cboTranPosting.setSelectedItem(observable.getCboTranPosting());
        rdoT2T_Yes.setSelected(observable.getRdoT2T_Yes());
        rdoT2T_No.setSelected(observable.getRdoT2T_No());
        rdoB2B_Yes.setSelected(observable.getRdoB2B_Yes());
        rdoB2B_No.setSelected(observable.getRdoB2B_No());
        cboHours.setSelectedItem(observable.getCboHours());
        cboMins.setSelectedItem(observable.getCboMins());
        cboBaseCurrency.setSelectedItem(((ComboBoxModel) cboBaseCurrency.getModel()).getDataForKey(observable.getCboBaseCurrency()));
        txtSupportEmail.setText(observable.getTxtSupportEmail());
        lblStatus.setText(observable.getLblStatus());
    }
    
    public void updateOBFields() {
        observable.setTdtBankOpeningDate(tdtBankOpeningDate.getDateValue());
        observable.setTxtBankCode(txtBankCode.getText());
        observable.setTxtBankName(txtBankName.getText());
        observable.setTxtWebsite(txtWebsite.getText());
        observable.setTxtSiteIP(txtSiteIP.getText());
        observable.setTxtDataIP(txtDataIP.getText());
        observable.setTxtCashLimit(txtCashLimit.getText());
        observable.setCboConversion((String) cboConversion.getSelectedItem());
        observable.setCboTranPosting((String) cboTranPosting.getSelectedItem());
        observable.setCboBaseCurrency((String) ((ComboBoxModel) cboBaseCurrency.getModel()).getKeyForSelected());
        observable.setRdoT2T_Yes(rdoT2T_Yes.isSelected());
        observable.setRdoT2T_No(rdoT2T_No.isSelected());
        observable.setRdoB2B_Yes(rdoB2B_Yes.isSelected());
        observable.setRdoB2B_No(rdoB2B_No.isSelected());
        observable.setCboHours((String) cboHours.getSelectedItem());
        observable.setCboMins((String) cboMins.getSelectedItem());
        observable.setTxtSupportEmail(txtSupportEmail.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }
    
    public void setHelpMessage() {
        objMandatoryRB = new BankMRB();
        tdtBankOpeningDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtBankOpeningDate"));
        txtBankCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBankCode"));
        txtBankName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBankName"));
        txtWebsite.setHelpMessage(lblMsg, objMandatoryRB.getString("txtWebsite"));
        txtSiteIP.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSiteIP"));
        txtDataIP.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDataIP"));
        txtCashLimit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCashLimit"));
        cboConversion.setHelpMessage(lblMsg, objMandatoryRB.getString("cboConversion"));
        cboTranPosting.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTranPosting"));
        cboBaseCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBaseCurrency"));
        rdoT2T_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoT2T_Yes"));
        rdoB2B_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoB2B_Yes"));
        cboHours.setHelpMessage(lblMsg, objMandatoryRB.getString("cboHours"));
        cboMins.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMins"));
        txtSupportEmail.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSupportEmail"));
    }
    
    private void initComponentData() {
        try{
            cboConversion.setModel(observable.getCbmConversion());
            cboTranPosting.setModel(observable.getCbmTranPosting());
            cboHours.setModel(observable.getCbmHours());
            cboMins.setModel(observable.getCbmMins());
            cboBaseCurrency.setModel(observable.getCbmBaseCurrency());
            
        }catch(ClassCastException e){
            e.printStackTrace();
        }
    }
    
    private void setComponentLength(){
        txtBankCode.setMaxLength(8);
        txtBankName.setMaxLength(128);
        txtWebsite.setMaxLength(64);
        txtSiteIP.setMaxLength(32);
        txtSupportEmail.setMaxLength(64);
        txtDataIP.setMaxLength(32);
        txtCashLimit.setMaxLength(21);
        
    }
    
    private void callView(String currField) {
        viewType = currField;
        //        HashMap viewMap = new HashMap();
        //        if (currField.equals("Edit") || currField.equals("Delete")){
        //            viewMap.put("MAPNAME", "getSelectBankTOList");
        //        }
        HashMap objHashMap = new HashMap();
        java.util.List list = ClientUtil.executeQuery("getSelectBankTOList", objHashMap);
        System.out.println(list);
        //        new ViewAll(this, viewMap).show();
    }
    
    public void fillData(Object obj) {
        if (obj != null){
            // For authorization only
            viewType = AUTHORIZE;
        }
        observable.doAction("FetchData");
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(this, true);
        }else{
            ClientUtil.enableDisable(this, false);
        }
        setButtonEnableDisable();
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_VIEW_MODE)
            setModified(true);
    }
    
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(!btnEdit.isEnabled());
        btnCancel.setEnabled(!btnEdit.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        
        /* */
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoB2B = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoT2T = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace19 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
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
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panMainBankUI = new com.see.truetransact.uicomponent.CPanel();
        lblBankCode = new com.see.truetransact.uicomponent.CLabel();
        lblBankName = new com.see.truetransact.uicomponent.CLabel();
        lblWebsite = new com.see.truetransact.uicomponent.CLabel();
        lblSiteIP = new com.see.truetransact.uicomponent.CLabel();
        lblDataIP = new com.see.truetransact.uicomponent.CLabel();
        lblDayEndProcessTime = new com.see.truetransact.uicomponent.CLabel();
        lblConversion = new com.see.truetransact.uicomponent.CLabel();
        lblTranPosting = new com.see.truetransact.uicomponent.CLabel();
        lblCashLimit = new com.see.truetransact.uicomponent.CLabel();
        txtBankCode = new com.see.truetransact.uicomponent.CTextField();
        txtBankName = new com.see.truetransact.uicomponent.CTextField();
        txtWebsite = new com.see.truetransact.uicomponent.CTextField();
        txtSiteIP = new com.see.truetransact.uicomponent.CTextField();
        txtDataIP = new com.see.truetransact.uicomponent.CTextField();
        txtCashLimit = new com.see.truetransact.uicomponent.CTextField();
        cboConversion = new com.see.truetransact.uicomponent.CComboBox();
        cboTranPosting = new com.see.truetransact.uicomponent.CComboBox();
        panTransactions = new com.see.truetransact.uicomponent.CPanel();
        lblT2T = new com.see.truetransact.uicomponent.CLabel();
        lblB2B = new com.see.truetransact.uicomponent.CLabel();
        rdoT2T_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoT2T_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoB2B_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoB2B_No = new com.see.truetransact.uicomponent.CRadioButton();
        panDEPTime = new com.see.truetransact.uicomponent.CPanel();
        lblHours = new com.see.truetransact.uicomponent.CLabel();
        lblMins = new com.see.truetransact.uicomponent.CLabel();
        cboHours = new com.see.truetransact.uicomponent.CComboBox();
        cboMins = new com.see.truetransact.uicomponent.CComboBox();
        lblSupportEmail = new com.see.truetransact.uicomponent.CLabel();
        txtSupportEmail = new com.see.truetransact.uicomponent.CTextField();
        lblBaseCurrency = new com.see.truetransact.uicomponent.CLabel();
        cboBaseCurrency = new com.see.truetransact.uicomponent.CComboBox();
        lblBankOpeningDate = new com.see.truetransact.uicomponent.CLabel();
        tdtBankOpeningDate = new com.see.truetransact.uicomponent.CDateField();
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

        lblSpace4.setText("     ");
        lblSpace4.setEnabled(false);
        tbrAdvances.add(lblSpace4);

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

        lblSpace2.setText("     ");
        lblSpace2.setEnabled(false);
        tbrAdvances.add(lblSpace2);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setPreferredSize(new java.awt.Dimension(29, 27));
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
        btnException.setMaximumSize(new java.awt.Dimension(29, 27));
        btnException.setMinimumSize(new java.awt.Dimension(29, 27));
        btnException.setPreferredSize(new java.awt.Dimension(29, 27));
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
        btnReject.setMaximumSize(new java.awt.Dimension(29, 27));
        btnReject.setMinimumSize(new java.awt.Dimension(29, 27));
        btnReject.setPreferredSize(new java.awt.Dimension(29, 27));
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnReject);

        lblSpace3.setText("     ");
        tbrAdvances.add(lblSpace3);

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

        getContentPane().add(tbrAdvances, java.awt.BorderLayout.NORTH);

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

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        panMainBankUI.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panMainBankUI.setLayout(new java.awt.GridBagLayout());

        lblBankCode.setText("Bank Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(lblBankCode, gridBagConstraints);

        lblBankName.setText("Bank Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(lblBankName, gridBagConstraints);

        lblWebsite.setText(" Website Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(lblWebsite, gridBagConstraints);

        lblSiteIP.setText("Site IP Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(lblSiteIP, gridBagConstraints);

        lblDataIP.setText("Data Center IP Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(lblDataIP, gridBagConstraints);

        lblDayEndProcessTime.setText("Day End Process Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(lblDayEndProcessTime, gridBagConstraints);

        lblConversion.setText("Currency Conversion");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(lblConversion, gridBagConstraints);

        lblTranPosting.setText("Transactions Posting");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(lblTranPosting, gridBagConstraints);

        lblCashLimit.setText("Cash Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(lblCashLimit, gridBagConstraints);

        txtBankCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(txtBankCode, gridBagConstraints);

        txtBankName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(txtBankName, gridBagConstraints);

        txtWebsite.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(txtWebsite, gridBagConstraints);

        txtSiteIP.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSiteIP.setValidation(new IPValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(txtSiteIP, gridBagConstraints);

        txtDataIP.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDataIP.setValidation(new IPValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(txtDataIP, gridBagConstraints);

        txtCashLimit.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCashLimit.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(txtCashLimit, gridBagConstraints);

        cboConversion.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(cboConversion, gridBagConstraints);

        cboTranPosting.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(cboTranPosting, gridBagConstraints);

        panTransactions.setBorder(javax.swing.BorderFactory.createTitledBorder("User Defined Transactions"));
        panTransactions.setLayout(new java.awt.GridBagLayout());

        lblT2T.setText("Teller To Teller Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransactions.add(lblT2T, gridBagConstraints);

        lblB2B.setText("Branch To Branch Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransactions.add(lblB2B, gridBagConstraints);

        rdoT2T.add(rdoT2T_Yes);
        rdoT2T_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panTransactions.add(rdoT2T_Yes, gridBagConstraints);

        rdoT2T.add(rdoT2T_No);
        rdoT2T_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panTransactions.add(rdoT2T_No, gridBagConstraints);

        rdoB2B.add(rdoB2B_Yes);
        rdoB2B_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panTransactions.add(rdoB2B_Yes, gridBagConstraints);

        rdoB2B.add(rdoB2B_No);
        rdoB2B_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panTransactions.add(rdoB2B_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panMainBankUI.add(panTransactions, gridBagConstraints);

        panDEPTime.setLayout(new java.awt.GridBagLayout());

        lblHours.setText("Hrs");
        lblHours.setMinimumSize(new java.awt.Dimension(20, 21));
        lblHours.setPreferredSize(new java.awt.Dimension(20, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDEPTime.add(lblHours, gridBagConstraints);

        lblMins.setText("Mins");
        lblMins.setMinimumSize(new java.awt.Dimension(30, 21));
        lblMins.setPreferredSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDEPTime.add(lblMins, gridBagConstraints);

        cboHours.setMinimumSize(new java.awt.Dimension(60, 21));
        cboHours.setPreferredSize(new java.awt.Dimension(60, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panDEPTime.add(cboHours, gridBagConstraints);

        cboMins.setMinimumSize(new java.awt.Dimension(60, 21));
        cboMins.setPreferredSize(new java.awt.Dimension(60, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panDEPTime.add(cboMins, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMainBankUI.add(panDEPTime, gridBagConstraints);

        lblSupportEmail.setText("Support Email");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(lblSupportEmail, gridBagConstraints);

        txtSupportEmail.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSupportEmail.setValidation(new EmailValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(txtSupportEmail, gridBagConstraints);

        lblBaseCurrency.setText("Base Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(lblBaseCurrency, gridBagConstraints);

        cboBaseCurrency.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(cboBaseCurrency, gridBagConstraints);

        lblBankOpeningDate.setText("Bank Opening Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(lblBankOpeningDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMainBankUI.add(tdtBankOpeningDate, gridBagConstraints);

        getContentPane().add(panMainBankUI, java.awt.BorderLayout.CENTER);

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
    }// </editor-fold>//GEN-END:initComponents
    
    // Actions have to be taken when Authorize button is pressed
    private void authorizeActionPerformed(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)){
            HashMap mapParam = new HashMap();
            
            setModified(true);
            HashMap authorizeMapCondition = new HashMap();
            authorizeMapCondition.put("STATUS_BY", TrueTransactMain.USER_ID);
            authorizeMapCondition.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            mapParam.put(CommonConstants.MAP_WHERE, authorizeMapCondition);
            mapParam.put(CommonConstants.MAP_NAME, "getSelectBankAuthorizeTOList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeBankDetails");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            observable.setStatus();
            btnSave.setEnabled(false);
            btnEdit.setEnabled(false);
            btnCancel.setEnabled(true);
            authorizeMapCondition = null;
        } else if (viewType.equals(AUTHORIZE)){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            ClientUtil.execute("authorizeBankDetails", singleAuthorizeMap);
            observable.setLblStatus(authorizeStatus);
            lblStatus.setText(observable.getLblStatus());
            viewType = "";
        }
    }
    
    private void authDisable(){
        txtWebsite.setEnabled(false);
        txtSiteIP.setEnabled(false);
        txtDataIP.setEnabled(false);
        txtSupportEmail.setEnabled(false);
        cboHours.setEnabled(false);
        cboMins.setEnabled(false);
        tdtBankOpeningDate.setEnabled(false);
    }
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        btnExceptionActionPerformed();
    }//GEN-LAST:event_btnExceptionActionPerformed
    private void btnExceptionActionPerformed() {
        authDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeActionPerformed(CommonConstants.STATUS_EXCEPTION);
    }
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        btnRejectActionPerformed();
    }//GEN-LAST:event_btnRejectActionPerformed
    private void btnRejectActionPerformed() {
        authDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeActionPerformed(CommonConstants.STATUS_REJECTED);
    }
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        btnAuthorizeActionPerformed();
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void btnAuthorizeActionPerformed() {
        authDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeActionPerformed(CommonConstants.STATUS_AUTHORIZED);
    }
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        observable.setStatus();
        callView("Delete");
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        btnEditActionPerformed(evt);
        // Add your handling code here:
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        observable.doAction("FetchData");
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            ClientUtil.enableDisable(this, false);
        }else{
            ClientUtil.enableDisable(this, true);
        }
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        setModified(true);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        txtBankCode.setEditable(true);
        ClientUtil.enableDisable(this, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        // Add your handling code here:
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        btnSaveActionPerformed(evt);
        // Add your handling code here:
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        String strWarnMsg = new MandatoryCheck().checkMandatory(getClass().getName(), panMainBankUI);
        if (strWarnMsg.length() > 0){
            displayAlert(strWarnMsg);
        }else{
            updateOBFields();
            observable.doAction(observable.statusInsorUpd);
            observable.setResultStatus();
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                setModified(false);
                ClientUtil.enableDisable(this, false);
                setButtonEnableDisable();
            }else{
                ClientUtil.enableDisable(this, true);
            }
        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
        // Add your handling code here:
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        observable.setStatus();
        observable.resetOBFields();
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        viewType = "";
        setModified(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    
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
    private com.see.truetransact.uicomponent.CComboBox cboBaseCurrency;
    private com.see.truetransact.uicomponent.CComboBox cboConversion;
    private com.see.truetransact.uicomponent.CComboBox cboHours;
    private com.see.truetransact.uicomponent.CComboBox cboMins;
    private com.see.truetransact.uicomponent.CComboBox cboTranPosting;
    private com.see.truetransact.uicomponent.CLabel lblB2B;
    private com.see.truetransact.uicomponent.CLabel lblBankCode;
    private com.see.truetransact.uicomponent.CLabel lblBankName;
    private com.see.truetransact.uicomponent.CLabel lblBankOpeningDate;
    private com.see.truetransact.uicomponent.CLabel lblBaseCurrency;
    private com.see.truetransact.uicomponent.CLabel lblCashLimit;
    private com.see.truetransact.uicomponent.CLabel lblConversion;
    private com.see.truetransact.uicomponent.CLabel lblDataIP;
    private com.see.truetransact.uicomponent.CLabel lblDayEndProcessTime;
    private com.see.truetransact.uicomponent.CLabel lblHours;
    private com.see.truetransact.uicomponent.CLabel lblMins;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSiteIP;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSupportEmail;
    private com.see.truetransact.uicomponent.CLabel lblT2T;
    private com.see.truetransact.uicomponent.CLabel lblTranPosting;
    private com.see.truetransact.uicomponent.CLabel lblWebsite;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panDEPTime;
    private com.see.truetransact.uicomponent.CPanel panMainBankUI;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransactions;
    private com.see.truetransact.uicomponent.CButtonGroup rdoB2B;
    private com.see.truetransact.uicomponent.CRadioButton rdoB2B_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoB2B_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoT2T;
    private com.see.truetransact.uicomponent.CRadioButton rdoT2T_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoT2T_Yes;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CDateField tdtBankOpeningDate;
    private com.see.truetransact.uicomponent.CTextField txtBankCode;
    private com.see.truetransact.uicomponent.CTextField txtBankName;
    private com.see.truetransact.uicomponent.CTextField txtCashLimit;
    private com.see.truetransact.uicomponent.CTextField txtDataIP;
    private com.see.truetransact.uicomponent.CTextField txtSiteIP;
    private com.see.truetransact.uicomponent.CTextField txtSupportEmail;
    private com.see.truetransact.uicomponent.CTextField txtWebsite;
    // End of variables declaration//GEN-END:variables
    
}
