/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 **
 *
 * Authorize.java
 *
 * Created on March 3, 2004, 1:46 PM
 */
package com.see.truetransact.ui.common.authorizewf;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;

import org.apache.log4j.Logger;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedComboBoxModel;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import java.awt.Color;
import java.util.*;

/**
 * @author balachandar
 */
public class AuthorizeWFUI extends com.see.truetransact.uicomponent.CDialog implements Observer {

    private final AuthorizeWFRB resourceBundle = new AuthorizeWFRB();
    private AuthorizeWFOB observable;
    HashMap paramMap = null;
    CInternalFrame parent = null;
    ExceptionOptionsWF exceptionOptions = new ExceptionOptionsWF();
    private final static Logger log = Logger.getLogger(AuthorizeWFUI.class);
    private static Date currDt = null;

    /**
     * Creates new form AuthorizeUI
     */
    public AuthorizeWFUI(HashMap paramMap) {
        this.paramMap = paramMap;
        setupInit();
    }

    /**
     * Creates new form AuthorizeUI
     */
    public AuthorizeWFUI(CInternalFrame parent, HashMap paramMap) {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
    }

    private void setupInit() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        internationalize();
        setObservable();
        populateData(paramMap);
        cboAddFind.setVisible(false);
        btnRealize.setVisible(false);
        btnAuthorize.setVisible(false);
        btnException.setVisible(false);
        btnReject.setVisible(false);
        chkSelectAll.setVisible(false);
        txtSearchData.setAllowAll(true);
    }

    private void setupScreen() {
        setModal(true);

        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    private void setObservable() {
        try {
            observable = new AuthorizeWFOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * visible / invisible the Authorize Button
     */
    public void setAuthorize(boolean authorize) {
        btnAuthorize.setVisible(authorize);
    }

    public boolean getAuthorize() {
        return btnAuthorize.isVisible();
    }

    /**
     * visible / invisible the Reject Button
     */
    public void setReject(boolean reject) {
        btnReject.setVisible(reject);
    }

    public boolean getReject() {
        return btnReject.isVisible();
    }

    /**
     * visible / invisible the realize Button
     */
    public void setRealize(boolean realize) {
        btnRealize.setVisible(realize);
    }

    public boolean getRealize() {
        return btnRealize.isVisible();
    }

    /**
     * visible / invisible the Exception Button
     */
    public void setException(boolean exception) {
        btnException.setVisible(exception);
    }

    public boolean getException() {
        return btnException.isVisible();
    }

    public void populateData(HashMap mapID) {
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(mapID, tblData);
            if (heading != null && heading.size() > 0) {
                EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(heading);
                cboSearchCol.setModel(cboModel);
            }
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
    }

    public void show() {

        System.out.println("pppp" + paramMap);
        HashMap wer = new HashMap();
        int werconatins = 0;
        System.out.println("paramMap.containsKey()" + paramMap.containsKey("WHERE"));
        if (paramMap.containsKey("WHERE")) {
            wer = (HashMap) paramMap.get("WHERE");
            werconatins = 1;
        }
        String sts = "";
        int k = 0;
        if (werconatins == 1) {
            if (wer.containsValue("AUTHORIZED")) {
                sts = "AUTHORIZED";
                k = 1;
            }
            if (wer.containsValue("REJECTED")) {
                sts = "REJECTED";
                k = 1;
            }
        } else {
            if (paramMap.containsValue("AUTHORIZED")) {
                sts = "AUTHORIZED";
                k = 1;
            }
            if (paramMap.containsValue("REJECTED")) {
                sts = "REJECTED";
                k = 1;
            }
        }

        if (k == 1) {
            if (sts.equalsIgnoreCase("REJECTED")) {
                // if(paramMap.get().toString().equals(CommonConstants.STATUS_AUTHORIZED))
                //this.setForeground(Color.red);

                panSearchCondition.setForeground(Color.red);
                // panMultiSearch.setForeground(Color.red);
                panTable.setForeground(Color.red);
                cPanel1.setForeground(Color.red);
                // tblData.setForeground(Color.red);
                srcTable.setForeground(Color.red);
                panSearchCondition.setForeground(Color.red);
                // panMultiSearch.setForeground(Color.red);
                panTable.setForeground(Color.red);
                panSearchCondition.setBackground(Color.red);
                // panMultiSearch.setBackground(Color.red);
                panTable.setBackground(Color.red);
                srcTable.setBackground(Color.red);
                cPanel1.setBackground(Color.red);
                this.setTitle("Reject");
            } else 
            if(sts.equalsIgnoreCase("AUTHORIZED")){
                int red = 98;
                int green = 167;
                int blue = 107;
                Color myBlue = new Color(98, 167, 107);
                panSearchCondition.setForeground(myBlue);
                //panMultiSearch.setForeground(myBlue);
                panTable.setForeground(myBlue);
                cPanel1.setForeground(myBlue);
                srcTable.setForeground(myBlue);
                panSearchCondition.setForeground(myBlue);
                //  panMultiSearch.setForeground(myBlue);
                panTable.setForeground(myBlue);
                panSearchCondition.setBackground(myBlue);
                // panMultiSearch.setBackground(myBlue);
                panTable.setBackground(myBlue);
                srcTable.setBackground(myBlue);
                cPanel1.setBackground(myBlue);
                this.setTitle("Authorize");
            }
        }
        if (observable.isAvailable()) {
            super.show();
        } else {
            if (parent != null) {
                parent.setModified(false);
            }
            ClientUtil.noDataAlert();
        }
    }

    public void setVisible(boolean visible) {
        if (observable.isAvailable()) {
            super.setVisible(visible);
        }
    }

    /**
     * Bring up and populate the temporary project detail screen.
     */
    private void whenTableRowSelected() {
        int rowIndexSelected = tblData.getSelectedRow();

        if (rowIndexSelected < 0) {
            COptionPane.showMessageDialog(null,
                    resourceBundle.getString("SelectRow"),
                    resourceBundle.getString("SelectRowHeading"),
                    COptionPane.OK_OPTION + COptionPane.INFORMATION_MESSAGE);
        } else {
            this.dispose();
             if (!getTitle().equalsIgnoreCase("Reject")
                    && cashierRejected(CommonUtil.convertObjToStr(tblData.getValueAt(rowIndexSelected, 0)))) {
                ClientUtil.showAlertWindow("Transaction is Rejected by Cashier...please reject transaction !!!");
                return;
            }
            if (parent != null) {
                ((CInternalFrame) parent).fillData(observable.fillData(rowIndexSelected));
            }
        }
    }
       public boolean cashierRejected(String transID){
        HashMap rejectMap = new HashMap();
        rejectMap.put("TRANS_ID", transID);
        rejectMap.put("TRANS_DT", currDt);
        List rejectList = (List) ClientUtil.executeQuery("getCashierRejectedTransaction", rejectMap);
        if (rejectList != null && rejectList.size() > 0) {
            return true;
        }
        else{
            return false;
        }        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        lblSearch = new com.see.truetransact.uicomponent.CLabel();
        cboSearchCol = new com.see.truetransact.uicomponent.CComboBox();
        txtSearchData = new com.see.truetransact.uicomponent.CTextField();
        btnSearch = new com.see.truetransact.uicomponent.CButton();
        cboSearchCriteria = new com.see.truetransact.uicomponent.CComboBox();
        chkCase = new com.see.truetransact.uicomponent.CCheckBox();
        panEscalation = new com.see.truetransact.uicomponent.CPanel();
        cSeparator1 = new com.see.truetransact.uicomponent.CSeparator();
        btnSendTo = new com.see.truetransact.uicomponent.CButton();
        cboAddFind = new com.see.truetransact.uicomponent.CComboBox();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        btnException = new com.see.truetransact.uicomponent.CButton();
        btnRealize = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();

        setTitle("Authorize");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        cPanel1.setLayout(new java.awt.GridBagLayout());

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel1.add(sptLine, gridBagConstraints);

        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        lblSearch.setText("Search");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(lblSearch, gridBagConstraints);

        cboSearchCol.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(cboSearchCol, gridBagConstraints);

        txtSearchData.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSearchData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchDataKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(txtSearchData, gridBagConstraints);

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_FIND.gif"))); // NOI18N
        btnSearch.setText("Find");
        btnSearch.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(btnSearch, gridBagConstraints);

        cboSearchCriteria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Starts with", "Ends with", "Exact Match", "Pattern Match" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(cboSearchCriteria, gridBagConstraints);

        chkCase.setText("Match Case");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(chkCase, gridBagConstraints);

        panEscalation.setLayout(new java.awt.GridBagLayout());

        cSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panEscalation.add(cSeparator1, gridBagConstraints);

        btnSendTo.setText("Send To");
        btnSendTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendToActionPerformed(evt);
            }
        });
        panEscalation.add(btnSendTo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(panEscalation, gridBagConstraints);

        cboAddFind.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "And", "Or" }));
        cboAddFind.setMinimumSize(new java.awt.Dimension(55, 21));
        cboAddFind.setPreferredSize(new java.awt.Dimension(55, 21));
        cboAddFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAddFindActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(cboAddFind, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        cPanel1.add(panSearchCondition, gridBagConstraints);

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
        tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblData.setPreferredScrollableViewportSize(new java.awt.Dimension(450000, 400000));
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDataMousePressed(evt);
            }
        });
        tblData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblDataMouseMoved(evt);
            }
        });
        srcTable.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(srcTable, gridBagConstraints);

        chkSelectAll.setText("Select All");
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTable.add(chkSelectAll, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel1.add(panTable, gridBagConstraints);

        panSearch.setLayout(new java.awt.GridBagLayout());

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        panSearch.add(btnAuthorize, new java.awt.GridBagConstraints());

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        panSearch.add(btnReject, new java.awt.GridBagConstraints());

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        panSearch.add(btnException, new java.awt.GridBagConstraints());

        btnRealize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/regularisation.gif"))); // NOI18N
        btnRealize.setText("Realize");
        btnRealize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizeActionPerformed(evt);
            }
        });
        panSearch.add(btnRealize, new java.awt.GridBagConstraints());

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnCancel.setText("Close");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        panSearch.add(btnCancel, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel1.add(panSearch, gridBagConstraints);

        getContentPane().add(cPanel1, new java.awt.GridBagConstraints());

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void btnSendToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendToActionPerformed
        // TODO add your handling code here:
        ClientUtil.showDialog(exceptionOptions);
    }//GEN-LAST:event_btnSendToActionPerformed
    private void btnRealizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizeActionPerformed
        // Add your handling code here:
        updateDBStatus(CommonConstants.STATUS_REALIZED);
    }//GEN-LAST:event_btnRealizeActionPerformed
    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // Add your handling code here:
        observable.setSelectAll(new Boolean(chkSelectAll.isSelected()));
    }//GEN-LAST:event_chkSelectAllActionPerformed
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        updateDBStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        updateDBStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    private ComboBoxModel getListModel() {
        ComboBoxModel listData = new ComboBoxModel();
        return listData;
    }
    private void cboAddFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAddFindActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_cboAddFindActionPerformed
    private void txtSearchDataKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchDataKeyReleased
        // Add your handling code here:
    }//GEN-LAST:event_txtSearchDataKeyReleased
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // Add your handling code here:
        String searchTxt = txtSearchData.getText().trim();
        if (!chkCase.isSelected()) {
            searchTxt = searchTxt.toUpperCase();
        }

        tblData.setModel(observable.getTableModel());
        tblData.revalidate();

        int selCol = cboSearchCol.getSelectedIndex();
        int selColCri = cboSearchCriteria.getSelectedIndex();

        observable.searchData(searchTxt, selCol, selColCri, chkCase.isSelected());
    }//GEN-LAST:event_btnSearchActionPerformed
    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
        // Add your handling code here:
        if ((evt.getClickCount() == 2) && (evt.getModifiers() == 16)) {
             tblData.setEnabled(false);
            whenTableRowSelected();
        }
    }//GEN-LAST:event_tblDataMousePressed
    private void tblDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseMoved
        // Add your handling code here:
        Point p = evt.getPoint();
        String tip =
                String.valueOf(
                tblData.getModel().getValueAt(
                tblData.rowAtPoint(p),
                tblData.columnAtPoint(p)));
        tblData.setToolTipText(tip);
    }//GEN-LAST:event_tblDataMouseMoved
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
//        if ( < 0) {
//            COptionPane.showMessageDialog(null,
//                resourceBundle.getString("SelectRow"), 
//                resourceBundle.getString("SelectRowHeading"),
//                COptionPane.OK_OPTION + COptionPane.INFORMATION_MESSAGE);
//        } 
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed
    private void updateDBStatus(String status) {
        //AuthorizeWFCheckUI dui = new AuthorizeWFCheckUI(observable.getSelected());

        HashMap screenParamMap = new HashMap();
        //screenParamMap.put (CommonConstants.AUTHORIZEDATA, dui.getCheckedAuthorizeList());
        screenParamMap.put(CommonConstants.AUTHORIZEDATA, observable.getSelected());
        screenParamMap.put(CommonConstants.AUTHORIZESTATUS, status);

        ArrayList usrList = exceptionOptions.getSelectedList();
        if (usrList != null && usrList.size() > 0) {
            screenParamMap.put(CommonConstants.AUTHORIZEUSERLIST, usrList);
        }

        System.out.println("screenParamMap:" + screenParamMap);
        // Calls InternalFrame's autorize method with Selected Rows and the status (authorize, reject or exception)
        if (parent != null) {
            ((CInternalFrame) parent).authorize(screenParamMap);
        }

        observable.updateStatus(paramMap);
    }
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        updateDBStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void internationalize() {
        lblSearch.setText(resourceBundle.getString("lblSearch"));
        btnSearch.setText(resourceBundle.getString("btnSearch"));
        chkCase.setText(resourceBundle.getString("chkCase"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        HashMap mapParam = new HashMap();

        HashMap whereMap = new HashMap();
        whereMap.put("USER_ID", "sysadmin1");

        //getSelectOperativeAcctProductAuthorizeTOList
        mapParam.put(CommonConstants.MAP_NAME, "getSelectInwardClearingAuthorizeTOList");
        mapParam.put(CommonConstants.MAP_WHERE, whereMap);

        mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeOperativeAcctProduct");

        AuthorizeWFUI authorizeUI = new AuthorizeWFUI(mapParam);
        authorizeUI.setAuthorize(true);
        authorizeUI.setException(false);
        authorizeUI.setReject(false);
        authorizeUI.setRealize(true);
        authorizeUI.show();
    }

    public void update(Observable o, Object arg) {
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnRealize;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSearch;
    private com.see.truetransact.uicomponent.CButton btnSendTo;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CSeparator cSeparator1;
    private com.see.truetransact.uicomponent.CComboBox cboAddFind;
    private com.see.truetransact.uicomponent.CComboBox cboSearchCol;
    private com.see.truetransact.uicomponent.CComboBox cboSearchCriteria;
    private com.see.truetransact.uicomponent.CCheckBox chkCase;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblSearch;
    private com.see.truetransact.uicomponent.CPanel panEscalation;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CTextField txtSearchData;
    // End of variables declaration//GEN-END:variables
}
