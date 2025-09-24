/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ViewSearchUI.java
 *
 * Created on August 24, 2003, 1:46 PM
 */

package com.see.truetransact.ui.common.viewsearch;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;

import org.apache.log4j.Logger;

import com.see.truetransact.clientutil.EnhancedComboBoxModel;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CPanel;
import com.see.truetransact.uicomponent.CDialog;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ComboBoxModel;

/**
 * @author  balachandar
 */
public class ViewSearchUI extends com.see.truetransact.uicomponent.CDialog implements Observer {
    private final ViewSearchRB resourceBundle = new ViewSearchRB();
    private ViewSearchOB observable;
    HashMap paramMap = null;
    CInternalFrame parent = null;
    CPanel panelParent = null;

    private final static Logger log = Logger.getLogger(ViewSearchUI.class);

    /** Creates new form ViewAll */
    public ViewSearchUI(HashMap paramMap) {
        this.paramMap = paramMap;        
        setupInit();
    }
    
    /** Creates new form ViewAll */
    public ViewSearchUI(CInternalFrame parent, HashMap paramMap) {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
    }
    
    /** Creates new form ViewAll */
    public ViewSearchUI(CInternalFrame parent, String title, HashMap paramMap) {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
        setTitle(title);
    }
    
    /** Creates new form ViewAll */
    public ViewSearchUI(CPanel panelParent, String title, HashMap paramMap) {
        this.panelParent = panelParent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
        setTitle(title);
    }
    
    private void setupInit() {
        initComponents();
        internationalize();
        setObservable();
        populateData(paramMap);
        panMultiSearch.setVisible(false);
        cboAddFind.setVisible(false);
        if (parent != null)
            setTitle("List for " + parent.getTitle());
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

    private void setObservable() {
        try {
            observable = new ViewSearchOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void populateData(HashMap mapID) {
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(mapID, tblData);
            EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(heading);
            cboSearchCol.setModel(cboModel);
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
    }

    public void show() {
        if (observable.isAvailable()) {
            super.show();
        }
    }

    public boolean show(String returnYes) {
        boolean retYesNo = observable.isAvailable();
        if (observable.isAvailable()) {
            super.show();
        }
        return retYesNo;
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
            if (panelParent != null) ((CPanel) panelParent).fillData(observable.fillData(rowIndexSelected));
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
        btnOk = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        lblSearch.setText("Search");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(lblSearch, gridBagConstraints);

        cboSearchCol.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
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
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
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
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSearchCondition.add(btnSearch, gridBagConstraints);

        cboSearchCriteria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Starts with", "Ends with", "Exact Match", "Pattern Match" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSearchCondition.add(cboSearchCriteria, gridBagConstraints);

        chkCase.setText("Match Case");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
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
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.weightx = 1.0;
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
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        panSearchCondition.add(cboAddFind, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(panSearchCondition, gridBagConstraints);

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
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panTable, gridBagConstraints);

        panSearch.setLayout(new java.awt.GridBagLayout());

        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        panSearch.add(btnOk, new java.awt.GridBagConstraints());

        btnCancel.setText("Cancel");
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
        getContentPane().add(panSearch, gridBagConstraints);

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(sptLine, gridBagConstraints);

        pack();
    }//GEN-END:initComponents
    private ComboBoxModel getListModel() {
        ComboBoxModel listData = new ComboBoxModel();
        return listData;
    }
    private void cboAddFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAddFindActionPerformed
        // Add your handling code here:
        //((ComboBoxModel) lstSearch.getModel()).addElement(
        String strTxtData = txtSearchData.getText();
        
        if (!strTxtData.equals("")) {
            StringBuffer strBCondition = new StringBuffer();
            strBCondition.append ((String) cboSearchCol.getSelectedItem());
            strBCondition.append (" ");
            strBCondition.append ((String) cboSearchCriteria.getSelectedItem());
            strBCondition.append (" ");
            strBCondition.append (strTxtData);
            strBCondition.append (" ");
            strBCondition.append ((String) cboAddFind.getSelectedItem());
            ((ComboBoxModel) lstSearch.getModel()).addElement(strBCondition.toString());
        }
    }//GEN-LAST:event_cboAddFindActionPerformed
    private void txtSearchDataKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchDataKeyReleased
        // Add your handling code here:
        //btnSearchActionPerformed(null);
    }//GEN-LAST:event_txtSearchDataKeyReleased
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // Add your handling code here:
        String searchTxt = txtSearchData.getText().trim();
        if (!chkCase.isSelected()) searchTxt = searchTxt.toUpperCase();

        //tblData.setModel(observable.getTableModel());
        //tblData.revalidate();
        observable.refreshTable();
        
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
        // Add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // Add your handling code here:
        whenTableRowSelected();
    }//GEN-LAST:event_btnOkActionPerformed
    private void internationalize() {
        lblSearch.setText(resourceBundle.getString("lblSearch"));
        btnSearch.setText(resourceBundle.getString("btnSearch"));
        chkCase.setText(resourceBundle.getString("chkCase"));
        btnOk.setText(resourceBundle.getString("btnOk"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        HashMap mapParam = new HashMap();

        HashMap where = new HashMap();
        where.put("beh", "CA");

        mapParam.put("MAPNAME", "getDenominationList");
        //mapParam.put("WHERE", where);
       // HashMap rMap = (HashMap) dao.getData(mapParam);

       // HashMap testMap = new HashMap();
        //testMap.put("MAPNAME", "getSelectOperativeAcctProductTOList");
        new ViewSearchUI(mapParam).show();
    }

    public void update(Observable o, Object arg) {
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CButton btnSearch;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CComboBox cboAddFind;
    private com.see.truetransact.uicomponent.CComboBox cboSearchCol;
    private com.see.truetransact.uicomponent.CComboBox cboSearchCriteria;
    private com.see.truetransact.uicomponent.CCheckBox chkCase;
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

