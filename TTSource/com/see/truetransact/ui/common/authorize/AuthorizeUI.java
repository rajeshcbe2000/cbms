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
package com.see.truetransact.ui.common.authorize;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListModel;

import org.apache.log4j.Logger;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.EnhancedComboBoxModel;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.awt.Color;
import java.util.*;
/**
 * @author balachandar
 */
public class AuthorizeUI extends com.see.truetransact.uicomponent.CDialog implements Observer, ListSelectionListener {

    private final AuthorizeRB resourceBundle = new AuthorizeRB();
    private AuthorizeOB observable;
    HashMap paramMap = null;
    CInternalFrame parent = null;
    javax.swing.JList lstSearch;
    java.util.ArrayList arrLst = new java.util.ArrayList();
    private final static Logger log = Logger.getLogger(AuthorizeUI.class);
    private static Date currDt = null;
    /**
     * Creates new form AuthorizeUI
     */
    public AuthorizeUI(HashMap paramMap) {
        this.paramMap = paramMap;
        setupInit();
    }

    /**
     * Creates new form AuthorizeUI
     */
    public AuthorizeUI(CInternalFrame parent, HashMap paramMap) {
        this.parent = parent;
        this.paramMap = paramMap;
        //System.out.println("paramMap" + this.paramMap);
        setupInit();
        setupScreen();
    }

    private void setupInit() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        internationalize();
        setObservable();
        populateData(paramMap);
        //System.out.println("fgdfg" + paramMap);
        panMultiSearch.setVisible(false);
        cboAddFind.setVisible(true);
        btnRealize.setVisible(false);
        btnAuthorize.setVisible(false);
        btnException.setVisible(false);
        btnReject.setVisible(false);
        if (!paramMap.containsKey("MULTISELECT")) {
            chkSelectAll.setVisible(false);
        }
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
            observable = new AuthorizeOB();
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

        //System.out.println("pppp" + paramMap);
        HashMap wer = new HashMap();
        int werconatins = 0;
        //System.out.println("paramMap.containsKey()" + paramMap.containsKey("WHERE"));
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
                panMultiSearch.setForeground(Color.red);
                panTable.setForeground(Color.red);
                cPanel1.setForeground(Color.red);
                // tblData.setForeground(Color.red);
                srcTable.setForeground(Color.red);
                panSearchCondition.setForeground(Color.red);
                panMultiSearch.setForeground(Color.red);
                panTable.setForeground(Color.red);
                panSearchCondition.setBackground(Color.red);
                panMultiSearch.setBackground(Color.red);
                panTable.setBackground(Color.red);
                srcTable.setBackground(Color.red);
                cPanel1.setBackground(Color.red);
                this.setTitle("Reject");
            } 
            else
            if(sts.equalsIgnoreCase("AUTHORIZED"))
                    {
                int red = 98;
                int green = 167;
                int blue = 107;
                Color myBlue = new Color(98, 167, 107);
                panSearchCondition.setForeground(Color.green);
                panMultiSearch.setForeground(Color.green);
                panTable.setForeground(Color.green);
                cPanel1.setForeground(Color.green);
                srcTable.setForeground(Color.green);
                panSearchCondition.setForeground(Color.green);
                panMultiSearch.setForeground(Color.green);
                panTable.setForeground(Color.green);
                panSearchCondition.setBackground(myBlue);
                panMultiSearch.setBackground(myBlue);
                panTable.setBackground(myBlue);
                srcTable.setBackground(myBlue);
                cPanel1.setBackground(myBlue);

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
            parent.setMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
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
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        lblSearch = new com.see.truetransact.uicomponent.CLabel();
        cboSearchCol = new com.see.truetransact.uicomponent.CComboBox();
        btnSearch = new com.see.truetransact.uicomponent.CButton();
        cboSearchCriteria = new com.see.truetransact.uicomponent.CComboBox();
        chkCase = new com.see.truetransact.uicomponent.CCheckBox();
        chkMultiSearch = new com.see.truetransact.uicomponent.CCheckBox();
        txtSearchData = new javax.swing.JTextField();
        panMultiSearch = new com.see.truetransact.uicomponent.CPanel();
        cboAddFind = new com.see.truetransact.uicomponent.CComboBox();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnRealize = new com.see.truetransact.uicomponent.CButton();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        btnException = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();

        setTitle("Authorize");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        cPanel1.setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(574, 35));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(574, 35));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        lblSearch.setText("Search");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(lblSearch, gridBagConstraints);

        cboSearchCol.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSearchCol.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(cboSearchCol, gridBagConstraints);

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

        chkMultiSearch.setText("Multi Search");
        chkMultiSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMultiSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(chkMultiSearch, gridBagConstraints);

        txtSearchData.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSearchData.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panSearchCondition.add(txtSearchData, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        cPanel1.add(panSearchCondition, gridBagConstraints);

        panMultiSearch.setMinimumSize(new java.awt.Dimension(200, 55));
        panMultiSearch.setPreferredSize(new java.awt.Dimension(200, 55));
        panMultiSearch.setLayout(new java.awt.GridBagLayout());

        cboAddFind.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "And", "Or" }));
        cboAddFind.setMinimumSize(new java.awt.Dimension(75, 21));
        cboAddFind.setPreferredSize(new java.awt.Dimension(75, 21));
        cboAddFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAddFindActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 2);
        panMultiSearch.add(cboAddFind, gridBagConstraints);

        cScrollPane1.setMinimumSize(new java.awt.Dimension(200, 40));
        cScrollPane1.setPreferredSize(new java.awt.Dimension(200, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        panMultiSearch.add(cScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel1.add(panMultiSearch, gridBagConstraints);

        panTable.setMinimumSize(new java.awt.Dimension(600, 500));
        panTable.setPreferredSize(new java.awt.Dimension(600, 500));
        panTable.setLayout(new java.awt.GridBagLayout());

        chkSelectAll.setText("Select All");
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTable.add(chkSelectAll, gridBagConstraints);

        srcTable.setViewport(srcTable.getRowHeader());

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel1.add(panTable, gridBagConstraints);

        panSearch.setLayout(new java.awt.GridBagLayout());

        btnRealize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/regularisation.gif"))); // NOI18N
        btnRealize.setText("Realize");
        btnRealize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizeActionPerformed(evt);
            }
        });
        panSearch.add(btnRealize, new java.awt.GridBagConstraints());

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
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel1.add(panSearch, gridBagConstraints);

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel1.add(sptLine, gridBagConstraints);

        getContentPane().add(cPanel1, new java.awt.GridBagConstraints());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chkMultiSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMultiSearchActionPerformed
        // TODO add your handling code here:
        panMultiSearch.setVisible(chkMultiSearch.isSelected());
        lstSearch = new javax.swing.JList(arrLst.toArray());
        lstSearch.setMaximumSize(new java.awt.Dimension(200, 40));
        lstSearch.setMinimumSize(new java.awt.Dimension(200, 40));
        lstSearch.setPreferredSize(new java.awt.Dimension(200, 40));
        cScrollPane1.setViewportView(lstSearch);
        arrLst = new ArrayList();

    }//GEN-LAST:event_chkMultiSearchActionPerformed
    private void tblDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseMoved
        Point p = evt.getPoint();
        String tip =
                String.valueOf(
                tblData.getModel().getValueAt(
                tblData.rowAtPoint(p),
                tblData.columnAtPoint(p)));
        tblData.setToolTipText(tip);
    }//GEN-LAST:event_tblDataMouseMoved
    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
        if ((evt.getClickCount() == 2) && (evt.getModifiers() == 16)) {
            tblData.setEnabled(false);
            whenTableRowSelected();
        }
    }//GEN-LAST:event_tblDataMousePressed
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

    public void valueChanged(javax.swing.event.ListSelectionEvent e) {
    }

    private void cboAddFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAddFindActionPerformed
        // Add your handling code here:
        if (CommonUtil.convertObjToStr(cboAddFind.getSelectedItem()).length() > 0) {
            addMultiSearchCriteria();
        }
    }    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-LAST:event_cboAddFindActionPerformed
        // Add your handling code here://GEN-FIRST:event_btnSearchActionPerformed
            String searchTxt = txtSearchData.getText().trim();
            if (!chkCase.isSelected()) {
                searchTxt = searchTxt.toUpperCase();
            }

            tblData.setModel(observable.getTableModel());
            tblData.revalidate();
            if (chkMultiSearch.isSelected()) {
                if (arrLst.size() > 0) {
                    addMultiSearchCriteria();
                    ArrayList lst;
                    int lstSize = lstSearch.getModel().getSize();
                    observable.setSearchTableModel((com.see.truetransact.clientutil.TableModel) tblData.getModel());
                    for (int i = 0; i < lstSize; i++) {
                        //System.out.println("#@$#$ lst " + i + " : " + lstSearch.getModel().getElementAt(i));
                        lst = (ArrayList) lstSearch.getModel().getElementAt(i);
                        cboSearchCol.setSelectedItem(CommonUtil.convertObjToStr(lst.get(0)));
                        int selCol = cboSearchCol.getSelectedIndex();
                        cboSearchCriteria.setSelectedItem(CommonUtil.convertObjToStr(lst.get(1)));
                        int selColCri = cboSearchCriteria.getSelectedIndex();
                        searchTxt = CommonUtil.convertObjToStr(lst.get(2));
                        if (!chkCase.isSelected()) {
                            searchTxt = searchTxt.toUpperCase();
                        }
                        //                if (!CommonUtil.convertObjToStr(lst.get(3)).equals("Or"))
                        //                    observable.setSearchTableModel((com.see.truetransact.clientutil.TableModel)tblData.getModel());
                        observable.searchData(searchTxt, selCol, selColCri, chkCase.isSelected(), CommonUtil.convertObjToStr(lst.get(3)));
                    }
                } else {
                    int selCol = cboSearchCol.getSelectedIndex();
                    int selColCri = cboSearchCriteria.getSelectedIndex();
                    observable.searchData(searchTxt, selCol, selColCri, chkCase.isSelected());
                }
            } else {
                int selCol = cboSearchCol.getSelectedIndex();
                int selColCri = cboSearchCriteria.getSelectedIndex();
                observable.searchData(searchTxt, selCol, selColCri, chkCase.isSelected());
            }
    }    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-LAST:event_btnSearchActionPerformed
        // Add your handling code here://GEN-FIRST:event_btnCancelActionPerformed
//        if ( < 0) {
//            COptionPane.showMessageDialog(null,
//                resourceBundle.getString("SelectRow"), 
//                resourceBundle.getString("SelectRowHeading"),
//                COptionPane.OK_OPTION + COptionPane.INFORMATION_MESSAGE);
//        } 
            if (parent != null) {
                parent.setModified(false);
            }
            this.dispose();

    }//GEN-LAST:event_btnCancelActionPerformed

    private void addMultiSearchCriteria() {
        java.util.ArrayList lst = new java.util.ArrayList();
        String searchCol = CommonUtil.convertObjToStr(cboSearchCol.getSelectedItem());
        String searchCriteria = CommonUtil.convertObjToStr(cboSearchCriteria.getSelectedItem());
        String searchData = txtSearchData.getText();
        boolean isError = false;
        if (searchData.length() == 0) {
            ClientUtil.displayAlert("Enter search Data...");
            isError = true;
        }
        int lstSize = arrLst.size();
        for (int i = 0; i < lstSize; i++) {
            lst = (ArrayList) arrLst.get(i);
            if ((i + 1 == lstSize) && CommonUtil.convertObjToStr(lst.get(0)).length() == 0) {
                ClientUtil.displayAlert("Connector not given in previous condition...");
                isError = true;
            }
        }
        if (!isError) {
            lst = new java.util.ArrayList();
            lst.add(searchCol);
            lst.add(searchCriteria);
            lst.add(searchData);
            lst.add(cboAddFind.getSelectedItem());
            arrLst.add(lst);
            lstSearch = new javax.swing.JList(arrLst.toArray());
            lstSearch.setMaximumSize(new java.awt.Dimension(200, 40));
            lstSearch.setMinimumSize(new java.awt.Dimension(200, 40));
            lstSearch.setPreferredSize(new java.awt.Dimension(200, 40));
            cScrollPane1.setViewportView(lstSearch);
            lstSearch.setVisibleRowCount(2);
            cboAddFind.setSelectedItem("");
            txtSearchData.setText("");
        }
    }

    private void updateDBStatus(String status) {
        //observable.updateStatus(paramMap, status);
        HashMap screenParamMap = new HashMap();
        screenParamMap.put(CommonConstants.AUTHORIZEDATA, observable.getSelected());
        screenParamMap.put(CommonConstants.AUTHORIZESTATUS, status);

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
        whereMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        whereMap.put("OUTWARD_DT", currDt.clone());
        mapParam.put(CommonConstants.MAP_NAME, "getSelectOutwardClearingRealizeTOList");
        mapParam.put(CommonConstants.MAP_WHERE, whereMap);

        mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeOperativeAcctProduct");

        AuthorizeUI authorizeUI = new AuthorizeUI(mapParam);
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
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CComboBox cboAddFind;
    private com.see.truetransact.uicomponent.CComboBox cboSearchCol;
    private com.see.truetransact.uicomponent.CComboBox cboSearchCriteria;
    private com.see.truetransact.uicomponent.CCheckBox chkCase;
    private com.see.truetransact.uicomponent.CCheckBox chkMultiSearch;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblSearch;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private javax.swing.JTextField txtSearchData;
    // End of variables declaration//GEN-END:variables
}
