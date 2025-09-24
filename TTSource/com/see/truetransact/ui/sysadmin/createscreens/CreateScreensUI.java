/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * CreateScreensUI.java
 *
 * Created on March 15, 2004, 3:07 PM
 */

package com.see.truetransact.ui.sysadmin.createscreens;

import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uicomponent.CLabel;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;

import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author  rahul
 * @modified  JK
 */
public class CreateScreensUI extends com.see.truetransact.uicomponent.CInternalFrame {
    
    private String screenId = "" ;
    private CLabel lblLookup_Id[];
    private CTextField txtLookup_Id[];
    private ComboBoxModel cbmScreenName;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private ArrayList key;
    private ArrayList value;
    
    private ArrayList description;
    private HashMap descValues;
    private HashMap displayMap;
    private List resultList;
    
//    final CreateScreensRB resourceBundle = new CreateScreensRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.createscreens.CreateScreensRB", ProxyParameters.LANGUAGE);
    
    final CreateScreensMRB objMandatoryRB = new  CreateScreensMRB();
    final String INSERT = "insert";
    final String UPDATE = "update";
    final String DEL = "delete";
    final String GET = "get";
    final String GETSELECT = "getSelect";
    
    String keySelected;
    
    final int EDIT=0,DELETE=1, NEW=2, CANCEL=-1;
    int viewType=-1, actionType=-1;
    int action = ClientConstants.ACTIONTYPE_CANCEL;
    
    /** Creates new form createScreens */
    public CreateScreensUI() {
        initComponents();
        initSetup();
    }
    
    private void initSetup() {
        setFieldNames();
        internationalize();
//        ClientUtil.enableDisable(panCreateScreens, false);
        ClientUtil.enableDisable(panContents, false);// Disables all when the screen appears for the 1st time
        setButtonEnableDisable();// Enables/Disables the necessary buttons and menu items...
        fillDropdown();
        setHelpMessage();
    }
    
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        cboScreenName.setName("cboScreenName");
        lblMsg.setName("lblMsg");
        lblScreenName.setName("lblScreenName");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus1.setName("lblStatus1");
        mbrLoanProduct.setName("mbrLoanProduct");
        panContents.setName("panContents");
        panCreateScreens.setName("panCreateScreens");
        panScreenName.setName("panScreenName");
        panStatus.setName("panStatus");
        sptCrestaScreen.setName("sptCrestaScreen");
    }
    
    private void internationalize() {
        
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblScreenName.setText(resourceBundle.getString("lblScreenName"));
        lblStatus1.setText(resourceBundle.getString("lblStatus1"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }
    
    // To Fill the Combo boxes in the UI
    private void fillDropdown() {
        try {
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookup_keys = new ArrayList();

            lookup_keys.add("SCREENS");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);

            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("SCREENS"));
            cbmScreenName = new ComboBoxModel(key,value);
            cboScreenName.setModel(getCbmScreenName());
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    
    // Get the value from the Hash Map depending on the Value of Key...
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    public void setCbmScreenName(ComboBoxModel cbmScreenName){
        this.cbmScreenName = cbmScreenName;
    }
    
    ComboBoxModel getCbmScreenName(){
        return cbmScreenName;
    }
    
    public void setHelpMessage() {
        cboScreenName.setHelpMessage(lblMsg, objMandatoryRB.getString("cboScreenName"));
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
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panCreateScreens = new com.see.truetransact.uicomponent.CPanel();
        panScreenName = new com.see.truetransact.uicomponent.CPanel();
        lblScreenName = new com.see.truetransact.uicomponent.CLabel();
        cboScreenName = new com.see.truetransact.uicomponent.CComboBox();
        sptCrestaScreen = new com.see.truetransact.uicomponent.CSeparator();
        panContents = new com.see.truetransact.uicomponent.CPanel();
        tbrLoantProduct = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace23 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrLoanProduct = new com.see.truetransact.uicomponent.CMenuBar();
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

        panCreateScreens.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panCreateScreens.setLayout(new java.awt.GridBagLayout());

        panScreenName.setLayout(new java.awt.GridBagLayout());

        lblScreenName.setText("Screen Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panScreenName.add(lblScreenName, gridBagConstraints);

        cboScreenName.setMinimumSize(new java.awt.Dimension(100, 21));
        cboScreenName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboScreenNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panScreenName.add(cboScreenName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        panCreateScreens.add(panScreenName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreateScreens.add(sptCrestaScreen, gridBagConstraints);

        panContents.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreateScreens.add(panContents, gridBagConstraints);

        getContentPane().add(panCreateScreens, java.awt.BorderLayout.CENTER);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnNew);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace21);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace22);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrLoantProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnSave);

        lblSpace23.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace23.setText("     ");
        lblSpace23.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace23);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrLoantProduct.add(lblSpace3);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnPrint);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace24);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnClose);

        getContentPane().add(tbrLoantProduct, java.awt.BorderLayout.NORTH);

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

        mbrLoanProduct.add(mnuProcess);

        setJMenuBar(mbrLoanProduct);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        super.removeEditLock(screenId);
        resetFields();
        action = ClientConstants.ACTIONTYPE_CANCEL;
        lblStatus1.setText(ClientConstants.ACTION_STATUS[action]);
        ClientUtil.enableDisable(panContents, false);      // Disables the panel...
        setButtonEnableDisable();                         /* Enables or Disables the buttons and menu Items
                                                          depending on their previous state...*/
        
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        StringBuffer strBMandatory = new StringBuffer();
        if(resultList != null){
            final int lstSize = resultList.size();
             if (lstSize > 0){
                for(int i=0; i<lstSize; i++){
                     if(txtLookup_Id[i].getText().trim().length()==0){
                     HashMap hash = (HashMap)resultList.get(i);
                     strBMandatory.append("Enter the " + hash.get("LOOKUP_DESC") + "\n");
                    }
                }
            }
        }
        if(strBMandatory.length()==0){
        
            descValues = new HashMap();
            int descSize = description.size();
            String query = null;

            if(actionType==NEW)
                query = INSERT + keySelected;
            else if(actionType==EDIT)
                query = UPDATE + keySelected;
            else if(actionType==DELETE)
                query = DEL + keySelected;

            for (int j=0; j < descSize; j++) {
                descValues.put((String)description.get(j), txtLookup_Id[j].getText());
            }
            ClientUtil.execute(query, descValues);
            super.removeEditLock(screenId);
            resetFields();
            lblStatus1.setText(ClientConstants.RESULT_STATUS[action]);
            ClientUtil.enableDisable(panContents, false); // Disables the panel...
            setButtonEnableDisable();                     // Enables or Disables the buttons and menu Items depending on their previous state...
        } else {
            displayAlert(strBMandatory.toString());
        }
        
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void resetFields(){
        if(resultList != null){
            final int lstSize = resultList.size();
            if (lstSize > 0){
                for(int i=0; i<lstSize; i++){
                    txtLookup_Id[i].setText("");
                }
            }
        }
        ClientUtil.clearAll(panCreateScreens);
//        ClientUtil.enableDisable(panCreateScreens, false);
        panContents.removeAll();
        resultList = null;
        panContents.repaint();    
        pack();  
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        actionType=DELETE;
        if(cboScreenName.getSelectedItem().equals("") || cboScreenName.getSelectedItem().equals(null)){
            displayAlert(resourceBundle.getString("WARNING"));
            actionType=CANCEL;
        }else{
            action = ClientConstants.ACTIONTYPE_DELETE;
            popUp(DELETE);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        actionType=EDIT;
        if(cboScreenName.getSelectedItem().equals("") || cboScreenName.getSelectedItem().equals(null)){
            displayAlert(resourceBundle.getString("WARNING"));
            actionType=CANCEL;
        }else{
            action = ClientConstants.ACTIONTYPE_EDIT;
            popUp(EDIT);
        }
    }//GEN-LAST:event_btnEditActionPerformed
    // To display the All the Product Id's which r having status as
    // created or updated, in a table...
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        if(field==EDIT || field==DELETE ){ 
            //Edit=0 and Delete=1
            ArrayList lst = new ArrayList();
            lst.add("DESIG_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            final String query = GET + keySelected;
            viewMap.put(CommonConstants.MAP_NAME, query);
        }
        new ViewAll(this, viewMap).show();
    }
    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        List viewList;
        // To Take the selected data into a List Object...
        screenId = "" ;
        final String query = GETSELECT + keySelected;
        if (viewType==EDIT || viewType==DELETE) {
            
            viewList = ClientUtil.executeQuery(query, hash);
            displayMap = (HashMap)viewList.get(0);
            
            fillTextData(displayMap);                           // To fill the data into the text box...
            if (action == ClientConstants.ACTIONTYPE_DELETE) {
                ClientUtil.enableDisable(panContents, false);   // Disables the panel...
            }else{
                ClientUtil.enableDisable(panContents, true);   // Enables the panel...
            }
            lblStatus1.setText(ClientConstants.ACTION_STATUS[action]);
            setButtonEnableDisable();         // Enables or Disables the buttons and menu Items depending on their previous state...
            screenId = (String)hash.get("DESIG_ID");
        }
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
    // To fill the data into the Text box...
    public void fillTextData(HashMap viewMap){
        Object[] keys = viewMap.keySet().toArray();
        Object[] values = viewMap.values().toArray();
        for (int i = 0, j=keys.length; i < j; i++) {
            txtLookup_Id[description.indexOf(keys[i])].setText((String) values[i]);
            if(i==0){
            txtLookup_Id[description.indexOf(keys[i])].setEditable(true);
            txtLookup_Id[description.indexOf(keys[i])].setEnabled(true);
            }
            
        }
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        actionType=NEW;
//        ClientUtil.enableDisable(panCreateScreens, true);
        if(cboScreenName.getSelectedItem().equals("") || cboScreenName.getSelectedItem().equals(null)){
            displayAlert(resourceBundle.getString("WARNING"));
            actionType=CANCEL;
        }else{
            ClientUtil.enableDisable(this, true);     // Enables the panel...
            setButtonEnableDisable();                 // Enables or Disables the buttons and menu Items depending on their previous state...
            action = ClientConstants.ACTIONTYPE_NEW;
            lblStatus1.setText(ClientConstants.ACTION_STATUS[action]);
            
            if(resultList != null){
            final int lstSize = resultList.size();
                            if (lstSize > 0){
                for(int i=0; i<lstSize; i++){
                     txtLookup_Id[i].setText("");
                     txtLookup_Id[i].setEditable(true);
                     txtLookup_Id[i].setEnabled(true);
                }
            }
        }
        }
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
        
    }//GEN-LAST:event_btnNewActionPerformed
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception{
        new CreateScreensUI().show();
    }
    
    private void initScreenName() {
        panContents.removeAll();
        java.awt.GridBagConstraints gridBagConstraints;
        description = new ArrayList();
        
        final HashMap screenDataMap = new HashMap();
        screenDataMap.put("LOOKUPID",keySelected);
        resultList = ClientUtil.executeQuery("getScreenName", screenDataMap);
        final int lstSize = resultList.size();
        
        lblLookup_Id = new CLabel[lstSize];
        txtLookup_Id = new CTextField[lstSize];
        
        for (int i=0; i < lstSize; i++) {
            lblLookup_Id[i] = new CLabel();
            txtLookup_Id[i] = new CTextField();
            
            final HashMap resultMap = (HashMap)resultList.get(i);
            
            String LOOKUPDESC = resultMap.get("LOOKUP_DESC").toString();
            description.add(resultMap.get("LOOKUP_REF_ID").toString());
            
            lblLookup_Id[i].setText(LOOKUPDESC);
            lblLookup_Id[i].setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
            panContents.add(lblLookup_Id[i], gridBagConstraints);
            
            txtLookup_Id[i].setHorizontalAlignment(javax.swing.JTextField.LEFT);
            txtLookup_Id[i].setPreferredSize(new java.awt.Dimension(100, 21));
            if(actionType == -1) {
                txtLookup_Id[i].setEditable(false);
                txtLookup_Id[i].setEnabled(false);
            }
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = i+1;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            panContents.add(txtLookup_Id[i], gridBagConstraints);
            
            txtLookup_Id[i].setHelpMessage(lblMsg, objMandatoryRB.getString("txt"));
        }
        panContents.repaint();    // to add the new fields to the panel...
        pack();                   // To dispaly the window of exact size...
    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // Add your handling code here:
        btnPrintActionPerformed(evt);
    }//GEN-LAST:event_mitPrintActionPerformed
    
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
    
    private void cboScreenNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboScreenNameActionPerformed
        // Add your handling code here:
        keySelected = (String)cbmScreenName.getKeyForSelected();
        if(!keySelected.equals("")){
            initScreenName();
        }
    }//GEN-LAST:event_cboScreenNameActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CComboBox cboScreenName;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblScreenName;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace23;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CMenuBar mbrLoanProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panContents;
    private com.see.truetransact.uicomponent.CPanel panCreateScreens;
    private com.see.truetransact.uicomponent.CPanel panScreenName;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private com.see.truetransact.uicomponent.CSeparator sptCrestaScreen;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private javax.swing.JToolBar tbrLoantProduct;
    // End of variables declaration//GEN-END:variables
    
}
