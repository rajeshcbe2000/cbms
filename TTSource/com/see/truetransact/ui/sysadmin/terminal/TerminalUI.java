/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TerminalUI.java
 *
 * Created on January 23, 2004, 3:29 PM
 */

package com.see.truetransact.ui.sysadmin.terminal;

import java.util.Observable;
import java.util.Observer;
import java.util.HashMap;
import javax.swing.JFrame;
import java.util.List;

import com.see.truetransact.ui.sysadmin.terminal.TerminalOB;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uivalidation.IPValidation;
import java.util.ResourceBundle;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
/**
 *
 * @author  Ashok
 */

public class TerminalUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer,UIMandatoryField{
    
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.terminal.TerminalRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private TerminalMRB objMandatoryRB;
    private TerminalOB observable;
    private int termId=0;
    private int viewType=0;
    private int BRANCH=50;
    private int selectedData=0;
    private int result;
    private boolean isFilled = false;
    private final int AUTHORIZE = 999;
    
    /** Creates new form TerminalUI */
    public TerminalUI() {
        initSettings();
    }
    
    /* Set the inital settings of TerminalUI    */
    private void initSettings(){
        initComponents();
        setFieldNames();
        internationalize();
        setMaximumLength();
        setObservable();
        cboBranchCode.setModel(observable.getCbmBranchCode());
        setMandatoryHashMap();
        ClientUtil.enableDisable(this, false);
        setHelpMessage();
        setButtonEnableDisable();
        resetTable();
        btnTerminalMasterTabNew.setEnabled(true);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
    }
    
    /* sets the FieldName for the components */
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnTerminalMasterTabDelete.setName("btnTerminalMasterTabDelete");
        btnTerminalMasterTabSave.setName("btnTerminalMasterTabSave");
        btnEdit.setName("btnEdit");
        btnTerminalMasterTabNew.setName("btnTerminalMasterTabNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        lblTerminalId.setName("lblTerminalId");
        lblBranchCode.setName("lblBranchCode");
        lblIpAddress.setName("lblIpAddress");
        lblMachineName.setName("lblMachineName");
        lblMsg.setName("lblMsg");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        lblTerminalDescription.setName("lblTerminalDescription");
        lblTerminalId.setName("lblTerminalName");
        mbrCustomer.setName("mbrCustomer");
        panBranchDet.setName("panBranchDet");
        panBranchTerminal.setName("panBranchTerminal");
        panStatus.setName("panStatus");
        srpTerminal_Master.setName("srpTerminal_Master");
        tblTerminal_Master.setName("tblTerminal_Master");
        txtTerminalId.setName("txtTerminalId");
//        txtBranchCode.setName("txtBranchCode");
        txtIPAddress.setName("txtIPAddress");
        txtMachineName.setName("txtMachineName");
        txtTerminalDescription.setName("txtTerminalDescription");
        txtTerminalId.setName("txtTerminalName");
        cboBranchCode.setName("cboBranchCode");
    }
    
    public static void main(String args[]) {
        JFrame frame=new JFrame();
        TerminalUI  tui = new TerminalUI();
        frame.getContentPane().add(tui);
        frame.setSize(600,400);
        frame.show();
        tui.show();
    }
    
    private void setMaximumLength(){
        txtTerminalId.setMaxLength(64);
        txtIPAddress.setMaxLength(32);
        txtMachineName.setMaxLength(128);
//        txtBranchCode.setMaxLength(32);
        txtTerminalDescription.setMaxLength(256);
        txtTerminalId.setMaxLength(16);
    }
    
    private void internationalize() {
        btnTerminalMasterTabDelete.setText(resourceBundle.getString("btnTerminalMasterTabDelete"));
        btnTerminalMasterTabSave.setText(resourceBundle.getString("btnTerminalMasterTabSave"));
        lblIpAddress.setText(resourceBundle.getString("lblIpAddress"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblTerminalId.setText(resourceBundle.getString("lblTerminalName"));
        lblBranchCode.setText(resourceBundle.getString("lblBranchCode"));
        lblTerminalId.setText(resourceBundle.getString("lblTerminalId"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnTerminalMasterTabNew.setText(resourceBundle.getString("btnTerminalMasterTabNew"));
        lblTerminalDescription.setText(resourceBundle.getString("lblTerminalDescription"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblMachineName.setText(resourceBundle.getString("lblMachineName"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtTerminalId", new Boolean(true));
//        mandatoryMap.put("txtBranchCode", new Boolean(true));
        mandatoryMap.put("txtTerminalName", new Boolean(true));
        mandatoryMap.put("txtIPAddress", new Boolean(true));
        mandatoryMap.put("txtMachineName", new Boolean(true));
        mandatoryMap.put("txtTerminalDescription", new Boolean(true));
        mandatoryMap.put("cboBranchCode", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    /** Sets the observable for the UI */
    private void setObservable() {
        try{
            observable = TerminalOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            System.out.println("Exception is caught "+e);
        }
    }
    
    /** Sets all the UI Fields to the new values */
    public void update(Observable observed, Object arg) {
        txtTerminalId.setText(observable.getTxtTerminalId());
//        txtBranchCode.setText(observable.getTxtBranchCode());
        txtTerminalId.setText(observable.getTxtTerminalName());
        txtIPAddress.setText(observable.getTxtIPAddress());
        txtMachineName.setText(observable.getTxtMachineName());
        txtTerminalDescription.setText(observable.getTxtTerminalDescription());
        tblTerminal_Master.setModel(observable.getTblTerminalMaster());
        tblTerminal_Master.revalidate();
//        cboBranchCode.setSelectedItem(observable.getCboBranchCode());
    }
    
    /* Updates the OBFields to the new Value entered in the UI **/
    private void updateOBFields() {
        observable.setTxtTerminalId(txtTerminalId.getText());
//        observable.setTxtBranchCode(txtBranchCode.getText());
        observable.setTxtTerminalName(txtTerminalId.getText());
        observable.setTxtIPAddress(txtIPAddress.getText());
        observable.setTxtMachineName(txtMachineName.getText());
        observable.setTxtTerminalDescription(txtTerminalDescription.getText());
        observable.setTblTerminalMaster((com.see.truetransact.clientutil.EnhancedTableModel)tblTerminal_Master.getModel());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
//        observable.setCboBranchCode((String) cboBranchCode.getSelectedItem());
    }
    
    private void setHelpMessage() {
        objMandatoryRB = new TerminalMRB();
        txtTerminalId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTerminalId"));
//        txtBranchCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchCode"));
        txtTerminalId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTerminalName"));
        txtIPAddress.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIPAddress"));
        txtMachineName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMachineName"));
        txtTerminalDescription.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTerminalDescription"));
         cboBranchCode.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBranchCode"));
    }
    
    private void setButtonEnableDisable() {
        btnEdit.setEnabled(!btnEdit.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        btnSave.setEnabled(!btnEdit.isEnabled());
        btnCancel.setEnabled(!btnEdit.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnTerminalMasterTabNew.setEnabled(!btnEdit.isEnabled());
        btnTerminalMasterTabDelete.setEnabled(!btnEdit.isEnabled());
        btnTerminalMasterTabSave.setEnabled(!btnEdit.isEnabled());
        lblStatus.setText(observable.getLblStatus());
        observable.resetForm();
        setTerminalId();
    }
    
    // To Enable or Disable the fields of the Table in Terminal Master...
    // when Edit is Pressed...
    private void setButtonEditEnableDisable(){
        btnEdit.setEnabled(!btnEdit.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        btnSave.setEnabled(!btnEdit.isEnabled());
        btnCancel.setEnabled(!btnEdit.isEnabled());
        mitSave.setEnabled(!btnEdit.isEnabled());
        mitCancel.setEnabled(!btnEdit.isEnabled());
        btnTerminalMasterTabNew.setEnabled(!btnEdit.isEnabled());
        btnTerminalMasterTabSave.setEnabled(btnEdit.isEnabled());
        btnTerminalMasterTabDelete.setEnabled(btnEdit.isEnabled());
        txtTerminalId.setEditable(btnEdit.isEnabled());
        txtTerminalId.setEnabled(btnEdit.isEnabled());
//        txtBranchCode.setEditable(btnEdit.isEnabled());
//        txtBranchCode.setEnabled(btnEdit.isEnabled());
        ClientUtil.enableDisable(panBranchDet, false);
        ClientUtil.enableDisable(panTerminalId, false);
    }
    
    // To Enable or Disable the fields of the Table in Terminal Master...
    // when New is Pressed...
    private void setButtonTableNewEnableDisable(){
        btnEdit.setEnabled(btnEdit.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        btnSave.setEnabled(!btnEdit.isEnabled());
        btnCancel.setEnabled(!btnEdit.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnTerminalMasterTabNew.setEnabled(!btnEdit.isEnabled());
        btnTerminalMasterTabDelete.setEnabled(btnEdit.isEnabled());
        btnTerminalMasterTabSave.setEnabled(!btnEdit.isEnabled());
        txtTerminalId.setEnabled(!btnEdit.isEnabled());
        txtTerminalId.setEditable(!btnEdit.isEnabled());
        txtMachineName.setEnabled(!btnEdit.isEnabled());
        txtMachineName.setEditable(!btnEdit.isEnabled());
        txtIPAddress.setEnabled(!btnEdit.isEnabled());
        txtIPAddress.setEditable(!btnEdit.isEnabled());
        txtTerminalDescription.setEditable(!btnEdit.isEnabled());
        txtTerminalDescription.setEnabled(!btnEdit.isEnabled());
        setTerminalId();
    }
    
    // To Enable or Disable the fields of the Table in Terminal Master...
    // when Table is selected...
    private void enableDisableTerminalMaster_TableSelected(){
        btnTerminalMasterTabNew.setEnabled(true);
        btnTerminalMasterTabSave.setEnabled(true);
        btnTerminalMasterTabDelete.setEnabled(true);
        ClientUtil.enableDisable(panBranchDet,true);
        ClientUtil.enableDisable(panTerminalId,false);
    }
    
    // To Enable or Disable the fields of the Table in Terminal Master...
    // when Cancel is Pressed...
    private void enableDisableTerminalMaster_Cancel(){
        txtTerminalId.setEnabled(btnEdit.isEnabled());
//        txtBranchCode.setEnabled(btnEdit.isEnabled());
        txtTerminalId.setEnabled(!btnEdit.isEnabled());
        txtMachineName.setEnabled(!btnEdit.isEnabled());
        txtIPAddress.setEnabled(!btnEdit.isEnabled());
        txtTerminalDescription.setEnabled(!btnEdit.isEnabled());
        btnTerminalMasterTabNew.setEnabled(!btnEdit.isEnabled());
        btnTerminalMasterTabSave.setEnabled(!btnEdit.isEnabled());
        btnTerminalMasterTabDelete.setEnabled(btnEdit.isEnabled());
    }
    
    /** Resets the TerminalId Fields */
    private void setTerminalId(){
        txtTerminalId.setText("");
        observable.setTxtTerminalId("");
    }
    
    private void addCTable(){
        if(selectedData==1){
            observable.setTxtTerminalName(txtTerminalId.getText());
            observable.setTxtMachineName(txtMachineName.getText());
            observable.setTxtIPAddress(txtIPAddress.getText());
            observable.setTxtTerminalDescription(txtTerminalDescription.getText());
            observable.setTableValueAt();
            observable.resetForm();
            enableDisableTerminalMaster_Save();
            cboBranchCode.setEnabled(false);
           cboBranchCode.setEditable(false);
        }
        else{
            /** when clicked on the new button of CTable **/
            result=0;
            updateOBFields();
            observable.setTxtTerminalId(String.valueOf(++termId));
            result = observable.addTerminalMasterTab();
            if(result!= 0){
                ClientUtil.enableDisable(panBranchDet,false);
                enableDisableTerminalMaster_Save();
            }
            if (result == 2){
                /** The action taken for the Cancel option **/
                ClientUtil.enableDisable(panBranchDet,true);
                enableDisableTerminalMaster_Cancel();
            }
        }
    }
    
    // To Enable or Disable the fields of the Table in Terminal Master...
    // When Save is Pressed...
    private void enableDisableTerminalMaster_Save(){
        setTerminalId();
        ClientUtil.enableDisable(panBranchDet,false);
        ClientUtil.enableDisable(panTerminalId, false);
        btnTerminalMasterTabNew.setEnabled(true);
        btnTerminalMasterTabSave.setEnabled(false);
        btnTerminalMasterTabDelete.setEnabled(false);
    }
    
    private void resetTable(){
        observable.removeTerminalMasterRow();
    }
    
    // To Enable or Disable the fields of the Table in Terminal Master...
    // When Delete is Pressed...
    private void enableDisableTerminalMaster_Delete(){
        btnTerminalMasterTabNew.setEnabled(true);
        btnTerminalMasterTabSave.setEnabled(false);
        btnTerminalMasterTabDelete.setEnabled(false);
        ClientUtil.enableDisable(panBranchDet, false);
        ClientUtil.enableDisable(panTerminalId,false);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panBranchTerminal = new com.see.truetransact.uicomponent.CPanel();
        srpTerminal_Master = new com.see.truetransact.uicomponent.CScrollPane();
        tblTerminal_Master = new com.see.truetransact.uicomponent.CTable();
        panBranchDet = new com.see.truetransact.uicomponent.CPanel();
        lblBranchCode = new com.see.truetransact.uicomponent.CLabel();
        txtTerminalId = new com.see.truetransact.uicomponent.CTextField();
        lblTerminalId = new com.see.truetransact.uicomponent.CLabel();
        lblIpAddress = new com.see.truetransact.uicomponent.CLabel();
        txtIPAddress = new com.see.truetransact.uicomponent.CTextField();
        txtMachineName = new com.see.truetransact.uicomponent.CTextField();
        lblMachineName = new com.see.truetransact.uicomponent.CLabel();
        lblTerminalDescription = new com.see.truetransact.uicomponent.CLabel();
        txtTerminalDescription = new com.see.truetransact.uicomponent.CTextField();
        cboBranchCode = new com.see.truetransact.uicomponent.CComboBox();
        lblTerminalName = new com.see.truetransact.uicomponent.CLabel();
        txtTerminalName = new com.see.truetransact.uicomponent.CTextField();
        panTerminalId = new com.see.truetransact.uicomponent.CPanel();
        sptHori = new com.see.truetransact.uicomponent.CSeparator();
        panButtons = new com.see.truetransact.uicomponent.CPanel();
        btnTerminalMasterTabNew = new com.see.truetransact.uicomponent.CButton();
        btnTerminalMasterTabSave = new com.see.truetransact.uicomponent.CButton();
        btnTerminalMasterTabDelete = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrTerminal = new javax.swing.JToolBar();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace19 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace20 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        mbrCustomer = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitEdit = new javax.swing.JMenuItem();
        sptEdit = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(600, 350));
        setPreferredSize(new java.awt.Dimension(600, 350));

        panBranchTerminal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panBranchTerminal.setMinimumSize(new java.awt.Dimension(600, 275));
        panBranchTerminal.setPreferredSize(new java.awt.Dimension(600, 275));
        panBranchTerminal.setLayout(new java.awt.GridBagLayout());

        srpTerminal_Master.setPreferredSize(new java.awt.Dimension(22, 22));
        srpTerminal_Master.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                srpTerminal_MasterMouseClicked(evt);
            }
        });

        tblTerminal_Master.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Terminal Id", "Terminal Name", "IP Address", "Machine Name"
            }
        ));
        tblTerminal_Master.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTerminal_MasterMouseClicked(evt);
            }
        });
        srpTerminal_Master.setViewportView(tblTerminal_Master);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchTerminal.add(srpTerminal_Master, gridBagConstraints);

        panBranchDet.setEnabled(false);
        panBranchDet.setMinimumSize(new java.awt.Dimension(248, 180));
        panBranchDet.setPreferredSize(new java.awt.Dimension(248, 180));
        panBranchDet.setLayout(new java.awt.GridBagLayout());

        lblBranchCode.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
        panBranchDet.add(lblBranchCode, gridBagConstraints);

        txtTerminalId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTerminalId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTerminalIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchDet.add(txtTerminalId, gridBagConstraints);

        lblTerminalId.setText("Terminal Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchDet.add(lblTerminalId, gridBagConstraints);

        lblIpAddress.setText("IP Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchDet.add(lblIpAddress, gridBagConstraints);

        txtIPAddress.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIPAddress.setValidation(new IPValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchDet.add(txtIPAddress, gridBagConstraints);

        txtMachineName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchDet.add(txtMachineName, gridBagConstraints);

        lblMachineName.setText("Machine Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchDet.add(lblMachineName, gridBagConstraints);

        lblTerminalDescription.setText("Terminal Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchDet.add(lblTerminalDescription, gridBagConstraints);

        txtTerminalDescription.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchDet.add(txtTerminalDescription, gridBagConstraints);

        cboBranchCode.setMinimumSize(new java.awt.Dimension(100, 21));
        cboBranchCode.setName("cboPrimaryOccupation"); // NOI18N
        cboBranchCode.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panBranchDet.add(cboBranchCode, gridBagConstraints);

        lblTerminalName.setText("Terminal Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchDet.add(lblTerminalName, gridBagConstraints);

        txtTerminalName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchDet.add(txtTerminalName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchTerminal.add(panBranchDet, gridBagConstraints);

        panTerminalId.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 44, 0, 0);
        panBranchTerminal.add(panTerminalId, gridBagConstraints);

        sptHori.setMinimumSize(new java.awt.Dimension(2, 2));
        sptHori.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchTerminal.add(sptHori, gridBagConstraints);

        panButtons.setMaximumSize(new java.awt.Dimension(59, 26));
        panButtons.setLayout(new java.awt.GridBagLayout());

        btnTerminalMasterTabNew.setText("New");
        btnTerminalMasterTabNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTerminalMasterTabNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTerminalMasterTabNew, gridBagConstraints);

        btnTerminalMasterTabSave.setText("Save");
        btnTerminalMasterTabSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTerminalMasterTabSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTerminalMasterTabSave, gridBagConstraints);

        btnTerminalMasterTabDelete.setText("Delete");
        btnTerminalMasterTabDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTerminalMasterTabDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTerminalMasterTabDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchTerminal.add(panButtons, gridBagConstraints);

        getContentPane().add(panBranchTerminal, java.awt.BorderLayout.CENTER);

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

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTerminal.add(btnEdit);

        lblSpace2.setText("     ");
        tbrTerminal.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTerminal.add(btnSave);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTerminal.add(lblSpace17);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTerminal.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTerminal.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTerminal.add(btnAuthorize);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTerminal.add(lblSpace18);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTerminal.add(btnException);

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTerminal.add(lblSpace19);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrTerminal.add(btnReject);

        lblSpace4.setText("     ");
        tbrTerminal.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrTerminal.add(btnPrint);

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTerminal.add(lblSpace20);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTerminal.add(btnClose);

        getContentPane().add(tbrTerminal, java.awt.BorderLayout.NORTH);

        mbrCustomer.setName("mbrCustomer"); // NOI18N

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess"); // NOI18N
        mnuProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProcessActionPerformed(evt);
            }
        });

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit"); // NOI18N
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        sptEdit.setName("sptNew"); // NOI18N
        mnuProcess.add(sptEdit);

        mitSave.setText("Save");
        mitSave.setName("mitSave"); // NOI18N
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel"); // NOI18N
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptCancel.setName("sptSave"); // NOI18N
        mnuProcess.add(sptCancel);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint"); // NOI18N
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose"); // NOI18N
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrCustomer.add(mnuProcess);

        setJMenuBar(mbrCustomer);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuProcessActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType != AUTHORIZE){
            viewType = AUTHORIZE;
            isFilled = false;
            //__ To Save the data in the Internal Frame...
            setModified(true);
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getTerminalMasterAuthorizeList");
            
            HashMap whereMap = new HashMap();
            //            whereMap.put("BRANCHID",TrueTransactMain.BRANCH_ID);
            //            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeTerminalMasterMasterData");
            mapParam.put(CommonConstants.AUTHORIZESTATUS,authorizeStatus);
            whereMap = null;
            
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType == AUTHORIZE && isFilled==true){
            System.out.println("Is Filled "+ isFilled);
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            singleAuthorizeMap.put("TERMINAL ID",CommonUtil.convertObjToStr(txtTerminalId.getText()));
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
            
            ClientUtil.execute("authorizeTerminalMasterMasterData", singleAuthorizeMap);
            viewType = -1;
            btnSave.setEnabled(true);
            btnCancelActionPerformed(null);
            
        }
    }
    private void btnTerminalMasterTabDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTerminalMasterTabDeleteActionPerformed
        // Add your handling code here:
        updateOBFields();
        observable.deleteTerminalMasterTab();
        enableDisableTerminalMaster_Delete();
        observable.resetForm();
    }//GEN-LAST:event_btnTerminalMasterTabDeleteActionPerformed
    
    private void btnTerminalMasterTabSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTerminalMasterTabSaveActionPerformed
        // Add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panBranchDet);
        /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage.length() > 0){
            observable.setResultStatus();
            ClientUtil.enableDisable(this, true);
            lblStatus.setText(observable.getLblStatus());
            displayAlert(mandatoryMessage);
            
        }else{
            addCTable();
        }
    }//GEN-LAST:event_btnTerminalMasterTabSaveActionPerformed
    
    private void tblTerminal_MasterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTerminal_MasterMouseClicked
        // Add your handling code here:
        selectedData = 1;
        observable.resetForm();
        isFilled = false;
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
            updateOBFields();
            observable.populateLookupMasterTab(tblTerminal_Master.getSelectedRow());
        }
        
        if(viewType != AUTHORIZE){
            enableDisableTerminalMaster_TableSelected();
        }
        
    }//GEN-LAST:event_tblTerminal_MasterMouseClicked
    
    private void srpTerminal_MasterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_srpTerminal_MasterMouseClicked
        // Add your handling code here:
    }//GEN-LAST:event_srpTerminal_MasterMouseClicked
    
    private void btnTerminalMasterTabNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTerminalMasterTabNewActionPerformed
        // Add your handling code here:
        selectedData=0;
        updateOBFields();
        ClientUtil.enableDisable(panBranchDet,true);
        observable.resetForm();
//        if(txtBranchCode.getText().equals("")){
//            txtBranchCode.setText(TrueTransactMain.BRANCH_ID);
//        }
       btnTerminalMasterTabSave.setEnabled(true);
        btnTerminalMasterTabDelete.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        //setButtonTableNewEnableDisable();
        
    }//GEN-LAST:event_btnTerminalMasterTabNewActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        setModified(false);
        observable.resetStatus();
        resetTable();
        ClientUtil.enableDisable(this, false);
        observable.resetForm();
        setButtonEnableDisable();
       
        setTerminalId();
        
        viewType = 0;
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnTerminalMasterTabNew.setEnabled(true);
        btnTerminalMasterTabSave.setEnabled(true);
        btnTerminalMasterTabDelete.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        setModified(false);
        updateOBFields();
        if(tblTerminal_Master.getRowCount()>0){
            observable.doSave();
            observable.setResultStatus();
            ClientUtil.enableDisable(this, false);
            observable.setResult(ClientConstants.ACTIONTYPE_EDIT);
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                observable.resetForm();
                resetTable();
                setButtonEnableDisable();
                setTerminalId();
            }
        }else{
            displayAlert(resourceBundle.getString("DataEntryMsg"));
        }
       
        viewType = 0;
        setButtonEnableDisable();
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUpItems();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        observable.existingData();
        
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
        
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
        
    }//GEN-LAST:event_mitSaveActionPerformed

    private void txtTerminalIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTerminalIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTerminalIdActionPerformed
    
    /* Fills up the viewAllUI Table with either TERMINAL_MASTER rows or BRANCH_MASTER rows */
    private void callView(int currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField == ClientConstants.ACTIONTYPE_EDIT  ||
        currField == ClientConstants.ACTIONTYPE_DELETE) {
            observable.setActionType(currField);
            viewMap.put(CommonConstants.MAP_NAME, "viewTerminalMaster");
        } else {
            viewMap.put(CommonConstants.MAP_NAME, "getBranchDetailsTUI");
        }
        
        new ViewAll(this, viewMap).show();
    }
    
    /* Fills the TerminalUI fields according to the row selected in the ViewAllUI table */
    public void fillData(Object  map) {
        isFilled = true;
        final HashMap hash=(HashMap) map;
        //__ To Pass the data for the Query...
        HashMap whereMap = new HashMap();
        whereMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(hash.get("BRANCH CODE")));
        if(viewType == AUTHORIZE){
            whereMap.put("TERMINAL ID", CommonUtil.convertObjToStr(hash.get("TERMINAL ID")));
        }
        
        //        else{
        //            whereMap.put("TERMINAL ID","");
        //        }
        
        HashMap hashMap = new HashMap();
        hashMap.put(CommonConstants.MAP_NAME, "SelectTerminalMaster");
        //        hashMap.put(CommonConstants.MAP_WHERE, hash.get("BRANCH_CODE"));
        hashMap.put(CommonConstants.MAP_WHERE, whereMap);
        hashMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        observable.populateData(hashMap);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        
        //__ Populate the Selected Row in the fields ...
        if(viewType == AUTHORIZE){
            observable.populateLookupMasterTab(0);
            btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
            btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
            btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
        }
        //        else{
        setButtonEditEnableDisable();
        //        }
        
        setModified(true);
    }
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void popUpItems() {
        final HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "viewTerminalMaster");
        new ViewAll(this, viewMap).show();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTerminalMasterTabDelete;
    private com.see.truetransact.uicomponent.CButton btnTerminalMasterTabNew;
    private com.see.truetransact.uicomponent.CButton btnTerminalMasterTabSave;
    private com.see.truetransact.uicomponent.CComboBox cboBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblIpAddress;
    private com.see.truetransact.uicomponent.CLabel lblMachineName;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTerminalDescription;
    private com.see.truetransact.uicomponent.CLabel lblTerminalId;
    private com.see.truetransact.uicomponent.CLabel lblTerminalName;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panBranchDet;
    private com.see.truetransact.uicomponent.CPanel panBranchTerminal;
    private com.see.truetransact.uicomponent.CPanel panButtons;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTerminalId;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptEdit;
    private com.see.truetransact.uicomponent.CSeparator sptHori;
    private com.see.truetransact.uicomponent.CScrollPane srpTerminal_Master;
    private com.see.truetransact.uicomponent.CTable tblTerminal_Master;
    private javax.swing.JToolBar tbrTerminal;
    private com.see.truetransact.uicomponent.CTextField txtIPAddress;
    private com.see.truetransact.uicomponent.CTextField txtMachineName;
    private com.see.truetransact.uicomponent.CTextField txtTerminalDescription;
    private com.see.truetransact.uicomponent.CTextField txtTerminalId;
    private com.see.truetransact.uicomponent.CTextField txtTerminalName;
    // End of variables declaration//GEN-END:variables
    
}
