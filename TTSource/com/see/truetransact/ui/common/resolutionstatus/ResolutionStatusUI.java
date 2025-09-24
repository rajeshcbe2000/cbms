/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 **
 *
 * ResolutionStatusUI.java
 *
 * Created on March 3, 2004, 1:46 PM
 */

package com.see.truetransact.ui.common.resolutionstatus;

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
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ComboBoxModel;

/**
 * @author  balachandar
 */
public class ResolutionStatusUI extends com.see.truetransact.uicomponent.CDialog implements Observer {
    private final ResolutionStatusRB resourceBundle = new ResolutionStatusRB();
    private ResolutionStatusOB observable;
    HashMap paramMap = null;
    CInternalFrame parent = null;
//    int str = 0;

    private final static Logger log = Logger.getLogger(ResolutionStatusUI.class);

    /** Creates new form ResolutionStatusUI */
    public ResolutionStatusUI(HashMap paramMap) {
        this.paramMap = paramMap;        
        setupInit();
    }
    
    /** Creates new form ResolutionStatusUI */
    public ResolutionStatusUI(CInternalFrame parent, HashMap paramMap) {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
    }
    
    private void setupInit() {
        initComponents();
        internationalize();
        setObservable();
        populateData(paramMap);
        panMultiSearch.setVisible(false);
        cboAddFind.setVisible(false);
        btnReject.setVisible(false);
        btnException.setVisible(false);
    }

    private void setupScreen() {
        setModal(true);

        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    //__ Returns the Status of the taskperformed to the calling UI...
    public int getResultStatus() {
        return observable.getResultStatus();
    }
    
//    public void setResultStatus(int st) {
//        str = st;
//    }

    private void setObservable() {
        try {
            observable = new ResolutionStatusOB();
            observable.addObserver(this);
            observable.setTable(tblData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * visible / invisible the Resolution Button
     */
    public void setResolution(boolean resolution) {
        btnResolution.setVisible(resolution);
    }
    public boolean getResolution() {
        return btnResolution.isVisible();
    }
    
//    /**
//     * visible / invisible the Reject Button
//     */
//    public void setReject(boolean reject) {
//        btnReject.setVisible(reject);
//    }
//    public boolean getReject() {
//        return btnReject.isVisible();
//    }
//    
//    /**
//     * visible / invisible the Exception Button
//     */
//    public void setException(boolean exception) {
//        btnException.setVisible(exception);
//    }
//    public boolean getException() {
//        return btnException.isVisible();
//    }

    public void populateData(HashMap mapID) {
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(mapID, tblData);
            if (heading != null) {
                EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(heading);
                cboSearchCol.setModel(cboModel);
            }
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
    }

    public void show() {
        if (observable.isAvailable()) {
            super.show();
        } else {
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
            if (parent != null) ((CInternalFrame) parent).fillData(observable.fillData(rowIndexSelected));
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        lblSearch = new com.see.truetransact.uicomponent.CLabel();
        cboSearchCol = new com.see.truetransact.uicomponent.CComboBox();
        txtSearchData = new com.see.truetransact.uicomponent.CTextField();
        btnSearch = new com.see.truetransact.uicomponent.CButton();
        cboSearchCriteria = new com.see.truetransact.uicomponent.CComboBox();
        chkCase = new com.see.truetransact.uicomponent.CCheckBox();
        panMultiSearch = new com.see.truetransact.uicomponent.CPanel();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        lstSearch = new com.see.truetransact.uicomponent.CList();
        cboAddFind = new com.see.truetransact.uicomponent.CComboBox();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnResolution = new com.see.truetransact.uicomponent.CButton();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        btnException = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("Send to Resolution");
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

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_FIND.gif")));
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

        panMultiSearch.setLayout(new java.awt.GridBagLayout());

        cScrollPane1.setPreferredSize(new java.awt.Dimension(24, 28));
        lstSearch.setModel(getListModel());
        cScrollPane1.setViewportView(lstSearch);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMultiSearch.add(cScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(panMultiSearch, gridBagConstraints);

        cboAddFind.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "And", "Or" }));
        cboAddFind.setMinimumSize(new java.awt.Dimension(55, 21));
        cboAddFind.setPreferredSize(new java.awt.Dimension(55, 21));
        cboAddFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAddFindActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(cboAddFind, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(panSearchCondition, gridBagConstraints);

        panTable.setLayout(new java.awt.GridBagLayout());

        panTable.setMinimumSize(new java.awt.Dimension(600, 500));
        panTable.setPreferredSize(new java.awt.Dimension(600, 500));
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
        getContentPane().add(panTable, gridBagConstraints);

        panSearch.setLayout(new java.awt.GridBagLayout());

        btnResolution.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif")));
        btnResolution.setText("Send to Resolution");
        btnResolution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResolutionActionPerformed(evt);
            }
        });

        panSearch.add(btnResolution, new java.awt.GridBagConstraints());

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif")));
        btnReject.setText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });

        panSearch.add(btnReject, new java.awt.GridBagConstraints());

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif")));
        btnException.setText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });

        panSearch.add(btnException, new java.awt.GridBagConstraints());

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif")));
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
        getContentPane().add(panSearch, gridBagConstraints);

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(sptLine, gridBagConstraints);

        chkSelectAll.setText("Select All");
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(chkSelectAll, gridBagConstraints);

        pack();
    }//GEN-END:initComponents
    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // Add your handling code here:
        observable.setSelectAll(new Boolean(chkSelectAll.isSelected()));
    }//GEN-LAST:event_chkSelectAllActionPerformed
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
//        // Add your handling code here:
//        observable.setResultStatus(ClientConstants.ACTIONTYPE_EXCEPTION);
//        updateDBStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
//        // Add your handling code here:
//        observable.setResultStatus(ClientConstants.ACTIONTYPE_REJECT);
//        updateDBStatus(CommonConstants.STATUS_REJECTED);
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
        if (!chkCase.isSelected()) searchTxt = searchTxt.toUpperCase();

        tblData.setModel(observable.getTableModel());
        tblData.revalidate();

        int selCol = cboSearchCol.getSelectedIndex();
        int selColCri = cboSearchCriteria.getSelectedIndex();

        observable.searchData(searchTxt, selCol, selColCri, chkCase.isSelected());
    }//GEN-LAST:event_btnSearchActionPerformed
    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
        // Add your handling code here:
        if ((evt.getClickCount() == 2) && (evt.getModifiers() == 16)) {
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
        if (parent != null && parent instanceof CInternalFrame)
            parent.setModified(false);
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed
    private void updateDBStatus(String status) {
        observable.updateStatus(paramMap, status);
        if (parent != null && parent instanceof CInternalFrame)
            parent.setModified(false);
    }
    private void btnResolutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResolutionActionPerformed
        // Add your handling code here:
        observable.setResultStatus(ClientConstants.ACTIONTYPE_SEND_TO_RESOLUTION);
        updateDBStatus(CommonConstants.STATUS_SEND_TO_RESOLUTION);
    }//GEN-LAST:event_btnResolutionActionPerformed
    private void internationalize() {
        lblSearch.setText(resourceBundle.getString("lblSearch"));
        btnSearch.setText(resourceBundle.getString("btnSearch"));
        chkCase.setText(resourceBundle.getString("chkCase"));
        btnResolution.setText(resourceBundle.getString("btnResolution"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        HashMap mapParam = new HashMap();
        
        HashMap whereMap = new HashMap();
        whereMap.put("STATUS", null);
        whereMap.put("USER_ID", null);
        whereMap.put("SHARE ACCOUNT NO", null);
        whereMap.put("SERIAL NO", null);
        
        mapParam.put(CommonConstants.MAP_NAME, "getSelectShareResolutionTOList");
        mapParam.put(CommonConstants.MAP_WHERE, whereMap);
        
        mapParam.put(CommonConstants.UPDATE_MAP_NAME, "sendToResolutionShare");
        
        ResolutionStatusUI resolutionStatusUI = new ResolutionStatusUI(mapParam);
        //authorizeUI.setAuthorize(false);
        //authorizeUI.setException(false);
        resolutionStatusUI.show();
    }

    public void update(Observable o, Object arg) {

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnResolution;
    private com.see.truetransact.uicomponent.CButton btnSearch;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CComboBox cboAddFind;
    private com.see.truetransact.uicomponent.CComboBox cboSearchCol;
    private com.see.truetransact.uicomponent.CComboBox cboSearchCriteria;
    private com.see.truetransact.uicomponent.CCheckBox chkCase;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblSearch;
    private com.see.truetransact.uicomponent.CList lstSearch;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CTextField txtSearchData;
    // End of variables declaration//GEN-END:variables
}

