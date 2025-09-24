/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DividendTransferUI.java
 *
 * Created on April 23, 2005, 4:02 PM
 */

package com.see.truetransact.ui.share.dividendTransfer;
import com.see.truetransact.ui.share.shareresolution.*;

import java.util.Observable;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.EnhancedComboBoxModel;
import java.util.ArrayList;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.COptionPane;
/**
 *
 * @author  JK
 */
public class DividendTransferUI extends com.see.truetransact.uicomponent.CInternalFrame implements com.see.truetransact.uimandatory.UIMandatoryField, java.util.Observer {
   
    /** Creates new form ShareResolutionUI */
    public DividendTransferUI() {
        setupInit();
        tblData.setAutoCreateRowSorter(true);
        
    }
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.share.dividendTransfer.DividendTransferRB", ProxyParameters.LANGUAGE);
    java.util.ResourceBundle resourceBundle1 = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.TermLoanRB", ProxyParameters.LANGUAGE);
    private DividendTransferOB observable;
      private boolean finalChecking = false;
    private void setupInit() {
        initComponents();
        setFieldNames();
        internationalize();
        setMaxLength();
        setMandatoryHashMap();
        setHelpMessage();
        setObservable();
        cboShareClass.setModel(observable.getCbmShareType());
        //populateData();
        cboShareClass.setEnabled(false);
        btnNew.setEnabled(true);
        btnException.setVisible(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }
    
    private void setMaxLength(){
        
    }
    
    private void populateData() {
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(tblData,CommonUtil.convertObjToStr(observable.getCbmShareType().getKeyForSelected()));
            if (heading != null) {
                EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(heading);
            }
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            log.info("Exception in populateData..." + e.toString());
        }
    }
    
    private void setObservable() {
        observable = DividendTransferOB.getInstance();
        observable.addObserver(this);
    }
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblShareClass.setText(resourceBundle.getString("lblShareClass"));
        btnClose.setText(resourceBundle.getString("btnClose"));        
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnException.setText(resourceBundle.getString("btnException"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
       // btnDeffered.setText(resourceBundle.getString("btnDeffered"));
        chkSelectAll.setText(resourceBundle.getString("chkSelectAll"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        chkSelectAll.setSelected(observable.getChkSelectAll());        
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setChkSelectAll(chkSelectAll.isSelected());        
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
This method list out all the Input Fields available in the UI.
It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        HashMap mandatoryMap = new HashMap();
        mandatoryMap.put("txtResolutionNo", new Boolean(true));
        mandatoryMap.put("tdtResolutionDt", new Boolean(true));
    }
    
    
    
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        chkSelectAll.setHelpMessage(lblMsg, resourceBundle.getString("chkSelectAll"));
        
    }
    
    private final static Logger log = Logger.getLogger(DividendTransferUI.class);
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrShareAcct = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace51 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace52 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace53 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace54 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace55 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panShareResolution = new com.see.truetransact.uicomponent.CPanel();
        tabShareResolution = new com.see.truetransact.uicomponent.CTabbedPane();
        panResolution = new com.see.truetransact.uicomponent.CPanel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panResolFields = new com.see.truetransact.uicomponent.CPanel();
        lblShareClass = new com.see.truetransact.uicomponent.CLabel();
        cboShareClass = new com.see.truetransact.uicomponent.CComboBox();
        panButton = new com.see.truetransact.uicomponent.CPanel();
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
        getContentPane().setLayout(new java.awt.GridBagLayout());

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnNew);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareAcct.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareAcct.add(lblSpace52);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrShareAcct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnSave);

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareAcct.add(lblSpace53);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrShareAcct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        tbrShareAcct.add(btnAuthorize);

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareAcct.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        tbrShareAcct.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareAcct.add(lblSpace55);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.setMaximumSize(new java.awt.Dimension(29, 27));
        btnReject.setMinimumSize(new java.awt.Dimension(29, 27));
        btnReject.setPreferredSize(new java.awt.Dimension(29, 27));
        tbrShareAcct.add(btnReject);

        lblSpace5.setText("     ");
        tbrShareAcct.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnPrint);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareAcct.add(lblSpace56);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(tbrShareAcct, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panStatus, gridBagConstraints);

        panShareResolution.setMinimumSize(new java.awt.Dimension(613, 550));
        panShareResolution.setPreferredSize(new java.awt.Dimension(613, 550));
        panShareResolution.setLayout(new java.awt.GridBagLayout());

        panResolution.setLayout(new java.awt.GridBagLayout());

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
        panResolution.add(chkSelectAll, gridBagConstraints);

        panTable.setMinimumSize(new java.awt.Dimension(600, 500));
        panTable.setPreferredSize(new java.awt.Dimension(600, 500));
        panTable.setLayout(new java.awt.GridBagLayout());

        tblData.setModel(new javax.swing.table.DefaultTableModel(
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
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataMouseClicked(evt);
            }
        });
        srcTable.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(srcTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panResolution.add(panTable, gridBagConstraints);

        panResolFields.setLayout(new java.awt.GridBagLayout());

        lblShareClass.setText("Share Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panResolFields.add(lblShareClass, gridBagConstraints);

        cboShareClass.setMinimumSize(new java.awt.Dimension(100, 21));
        cboShareClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboShareClassActionPerformed(evt);
            }
        });
        panResolFields.add(cboShareClass, new java.awt.GridBagConstraints());

        panResolution.add(panResolFields, new java.awt.GridBagConstraints());

        panButton.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panResolution.add(panButton, gridBagConstraints);

        tabShareResolution.addTab("Resolution", panResolution);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panShareResolution.add(tabShareResolution, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panShareResolution, gridBagConstraints);

        mnuProcess.setText("Process");

        mitNew.setText("New");
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
                 boolean chk = false;
                 for (int i = 0; i < tblData.getRowCount(); i++) {   
                    if((Boolean) tblData.getValueAt(i, 0)){                        
                            chk = true; 
                            break;
                    } 
                 }
                 if(!chk){
                    ClientUtil.showMessageWindow("Please Select any row!!!");
                    return;
                 }
            try {
                savePerformed();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void savePerformed() throws Exception {
        String action;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            action = CommonConstants.TOSTATUS_INSERT;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            action = CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            action = CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
    }

    private void saveAction(String status) throws Exception {
        updateOBFields();
        HashMap hs = new HashMap();
        HashMap dataMap=new HashMap();
        //pUi = new ProcessUI();
        //String acHD = "";
        int count = tblData.getRowCount();
        int datacount = 0;
        //double newBal = 0.0;
        //for(int j=0;j<count;j++){
        //    if(CommonUtil.convertObjToDouble(tblBalanceUpdate.getValueAt(j, 5)).doubleValue()!=0){
         //      datacount++; 
         //   }   
        //}
        if (count > 0) {
             for(int i=0;i<count;i++){
                if((Boolean) tblData.getValueAt(i, 0)){      
                    String shareNo = CommonUtil.convertObjToStr(tblData.getValueAt(i,1));                    
                    dataMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                    dataMap.put("SHARE_ACCT_NO",shareNo);
                    dataMap.put("USER_ID", TrueTransactMain.USER_ID);
                    dataMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                    observable.displayTableData(dataMap);
                    System.out.println("observable.getProxyReturnMap()####1"+observable.getProxyReturnMap());
                }
             }
             System.out.println("observable.getProxyReturnMap().get(COUNT).equals(0)####"+observable.getProxyReturnMap());
             if(observable.getProxyReturnMap()!=null && !observable.getProxyReturnMap().equals("")){                     
                        ClientUtil.showAlertWindow("Dividend Transfer Completed!!");  
                        btnCancelActionPerformed(null);
             }
        } 
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        cboShareClass.setEnabled(true);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        ClientUtil.enableDisable(panResolution, true);
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
         HashMap reportParamMap = new HashMap();
 com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    //--- For Mandatory check
    private String mandatoryChk(){
        String shareResolutionMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panResolFields);
        if(shareResolutionMandatoryMessage.length()>0){
            ClientUtil.showAlertWindow(shareResolutionMandatoryMessage);
        }
        return shareResolutionMandatoryMessage;
    }
    
    //--- calls the Observable.doAction
    private void doAction(int actionType){
        try{
            String mandatoryChk = mandatoryChk();
            int no=15;
            if(actionType==no){
                updateOBFields();
                observable.setActionType(actionType);
                observable.setStatus();
                observable.doAction();
                populateData();
                observable.ttNotifyObservers();
            }else
            if(mandatoryChk.length()<=0){
                updateOBFields();
                observable.setActionType(actionType);
                observable.setStatus();             
                observable.doAction();
                populateData();
                observable.ttNotifyObservers();
         
        }
        }catch (Exception e ){
            e.printStackTrace();
            log.info("Exception in doAction UI..." + e.toString());
        }
    }            
    public void clear(){     
     chkSelectAll.setSelected(false);
    }
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        closeOperation();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // Add your handling code here:
        observable.setSelectAll(new Boolean(chkSelectAll.isSelected()));
    }//GEN-LAST:event_chkSelectAllActionPerformed
   
     private void insertTransactionPart() {
        HashMap singleAuthorizeMap = new HashMap();
        java.util.ArrayList arrList = new java.util.ArrayList();
        HashMap authDataMap = new HashMap();
        arrList.add(authDataMap);
      int flag1 = 1;
      for(int i=0;i<tblData.getRowCount();i++){
              if(((Boolean)tblData.getValueAt(i, 0)).booleanValue()){
                flag1 = 0;  
              }  
      }
      if(flag1 == 1){
          ClientUtil.showMessageWindow("Please select atleast one row to reject");
          return;
      }
      else if(flag1 == 0){
            String [] debitType = {"Cash","Transfer"};
            int option3 = 0 ;
            if(option3 == 0){
                String transType = "";
                //                System.out.println("!!! transType :"+transType);
                while (CommonUtil.convertObjToStr(transType).length()==0) {
                    transType = (String)COptionPane.showInputDialog(null,"Select Transaction Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null, debitType, "");
                    if (CommonUtil.convertObjToStr(transType).length()>0) {
                        authDataMap.put("TRANSACTION_PART","TRANSACTION_PART");
                        authDataMap.put("TRANS_TYPE",transType.toUpperCase());
                        //authDataMap.put("LIMIT",txtLimit_SD.getText());
                        if(CommonUtil.convertObjToStr(transType.toUpperCase()).equals("CASH")){
                            boolean flag = true;
                            do {
                                String tokenNo = COptionPane.showInputDialog(this,resourceBundle1.getString("REMARK_CASH_TRANS"));
                                if (tokenNo != null && tokenNo.length()>0) {
                                    flag = tokenValidation(tokenNo);
                                }else{
                                    flag = true;
                                }
                                if(flag == false){
                                    ClientUtil.showAlertWindow("Token is invalid or not issued for you. Please verify.");
                                }else{
                                    authDataMap.put("TOKEN_NO",tokenNo);
                                }
                            } while (!flag);
                        }else if(CommonUtil.convertObjToStr(transType.toUpperCase()).equals("TRANSFER")){
                            boolean flag = true;
                            do {
                                String sbAcNo = firstEnteredActNo();
                                if(sbAcNo!=null && sbAcNo.length()>0){
                                    flag = checkingActNo(sbAcNo);
                                    if(flag == false && finalChecking == false){
                                        ClientUtil.showAlertWindow("Account No is invalid, Please enter correct no");
                                    }else{
                                        authDataMap.put("CR_ACT_NUM",sbAcNo);
                                    }
                                    finalChecking = false;
                                } else {
                                    ClientUtil.showMessageWindow("Transaction Not Created");
                                    flag = true;
                                    authDataMap.remove("TRANSACTION_PART");
                                }
                            } while (!flag);
                        }
                        observable.setNewTransactionMap(authDataMap);
                    }else{
                        transType = "Cancel";
                    }
                }
            }
            
      }
      
    }
    
    private boolean tokenValidation(String tokenNo){
        boolean tokenflag = false;
        HashMap tokenWhereMap = new HashMap();// Separating Serias No and Token No
        char[] chrs = tokenNo.toCharArray();
        StringBuffer seriesNo = new StringBuffer();
        int i=0;
        for (int j= chrs.length; i < j; i++ ) {
            if (Character.isDigit(chrs[i]))
                break;
            else
                seriesNo.append(chrs[i]);
        }
        tokenWhereMap.put("SERIES_NO", seriesNo.toString());
        tokenWhereMap.put("TOKEN_NO", CommonUtil.convertObjToInt(tokenNo.substring(i)));
        tokenWhereMap.put("USER_ID", ProxyParameters.USER_ID);
        tokenWhereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        tokenWhereMap.put("CURRENT_DT", ClientUtil.getCurrentDate());
        List lst = ClientUtil.executeQuery("validateTokenNo", tokenWhereMap);
        if (((Integer) lst.get(0)).intValue() == 0) {
            tokenflag = false;
        }else{
            tokenflag = true;
        }
        return tokenflag;
    }
     private String firstEnteredActNo(){
        String sbAcNo = COptionPane.showInputDialog(this,resourceBundle1.getString("REMARK_TRANSFER_TRANS"));
        return sbAcNo;
    }
     
      private boolean checkingActNo(String sbAcNo){
        boolean flag = false;
        HashMap existingMap = new HashMap();
        existingMap.put("ACT_NUM",sbAcNo.toUpperCase());
        List mapDataList = ClientUtil.executeQuery("getAccNoDet", existingMap);
        System.out.println("#### mapDataList :"+mapDataList);
        if (mapDataList!=null && mapDataList.size()>0) {
            existingMap = (HashMap)mapDataList.get(0);
            String[] obj5 = {"Proceed","ReEnter"};
            int option4 = COptionPane.showOptionDialog(null,("Please check whether Account No, Name coreect or not " + "\nOperative AcctNo is : " + existingMap.get("Account Number")+"\nCustomer Name :" + existingMap.get("Customer Name")), ("Transaction Part"),
            COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj5,obj5[0]);
            if(option4 == 0){
                flag = true;
            }else{
                flag = false;
            }
        }
        return flag;
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
       // btnDeffered.setName("btnDeffered");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        chkSelectAll.setName("chkSelectAll");
        lblMsg.setName("lblMsg");
        lblShareClass.setName("lblShareClass");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        mbrMain.setName("mbrMain");
        panButton.setName("panButton");
        panResolFields.setName("panResolFields");
        panResolution.setName("panResolution");
        panShareResolution.setName("panShareResolution");
        panStatus.setName("panStatus");
        panTable.setName("panTable");
        srcTable.setName("srcTable");
        tabShareResolution.setName("tabShareResolution");
        tblData.setName("tblData");
    }
    private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseClicked
        // TODO add your handling code here:
        tblData.setEnabled(true);
        
    }//GEN-LAST:event_tblDataMouseClicked

private void cboShareClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboShareClassActionPerformed
// TODO add your handling code here:
    String shareType = CommonUtil.convertObjToStr(observable.getCbmShareType().getKeyForSelected());
    if(shareType!=null && shareType.length()>0){
        populateData();
    }
}//GEN-LAST:event_cboShareClassActionPerformed
private void clearTable() {
        //observable.resetForm();        
        observable.clearTable();
        tblData.setModel(observable.getClearTable());  
}

private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
// TODO add your handling code here:
    ClientUtil.clearAll(this);
    btnNew.setEnabled(true);
    btnSave.setEnabled(false);
    btnCancel.setEnabled(false);
    btnAuthorize.setEnabled(false);
    observable.resetForm();
    clearTable();
    ClientUtil.enableDisable(panResolution, false);    
}//GEN-LAST:event_btnCancelActionPerformed
      public void fillData(Object map) {
        try{
            String resolutionno="";
             HashMap hash = (HashMap) map;
            resolutionno=CommonUtil.convertObjToStr(hash.get("RESOLUTION_ID"));
        }
        catch(Exception e){
            log.error(e);
        }
    }

    
    private void closeOperation(){
        cancelOperation();
        this.dispose();
    }
    
    private void cancelOperation(){
        observable.resetForm();
        ClientUtil.enableDisable(this, false);
        observable.ttNotifyObservers();
    }
    
    public java.util.HashMap getMandatoryHashMap() {
        return new HashMap();
    }
    
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        javax.swing.JFrame frm = new javax.swing.JFrame();
        DividendTransferUI shareResolutionUI = new DividendTransferUI();
        frm.getContentPane().add(shareResolutionUI);
        shareResolutionUI.show();
        frm.setSize(800, 650);
        frm.show();
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
    private com.see.truetransact.uicomponent.CComboBox cboShareClass;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblShareClass;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace52;
    private com.see.truetransact.uicomponent.CLabel lblSpace53;
    private com.see.truetransact.uicomponent.CLabel lblSpace54;
    private com.see.truetransact.uicomponent.CLabel lblSpace55;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panResolFields;
    private com.see.truetransact.uicomponent.CPanel panResolution;
    private com.see.truetransact.uicomponent.CPanel panShareResolution;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTabbedPane tabShareResolution;
    private com.see.truetransact.uicomponent.CTable tblData;
    private javax.swing.JToolBar tbrShareAcct;
    // End of variables declaration//GEN-END:variables
    
}
