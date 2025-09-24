/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ViewAll.java
 *
 * Created on August 24, 2003, 1:46 PM
 */

package com.see.truetransact.ui.common.viewall;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
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
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.deposit.DepositGroupsUI;
import com.see.truetransact.ui.deposit.MdsGroupUI;
import com.see.truetransact.ui.termloan.SuspenceAcctSearchUI;
import com.see.truetransact.uicomponent.*;
import java.awt.*;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * @author  balachandar
 */
public class ViewAll extends com.see.truetransact.uicomponent.CDialog implements Observer {
    private final ViewAllRB resourceBundle = new ViewAllRB();
    private ViewAllOB observable;
    HashMap paramMap = null;
    CInternalFrame parent = null;
    CPanel panelParent = null;
    JDialog dialogparent=null;
    String selItem = "";
    
    private final static Logger log = Logger.getLogger(ViewAll.class);
    
    /** Creates new form ViewAll */
     public ViewAll(JDialog dialogparent,HashMap paramMap) {
        super(dialogparent, true);
        this.dialogparent =dialogparent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
       
    }

    public ViewAll(HashMap paramMap) {
        this.paramMap = paramMap;
        setupInit();
    }
    
    /** Creates new form ViewAll */
    public ViewAll(CInternalFrame parent, HashMap paramMap) {
        super(parent, true);
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
    }
    
    /** Creates new form ViewAll */
    public ViewAll(CInternalFrame parent, HashMap paramMap, boolean clearEnabled) {
        super(parent, true);
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
        if (clearEnabled) {
            btnClear.setVisible(true);
        } else {
            btnClear.setVisible(false);
        }
    }
    
    /** Creates new form ViewAll */
    public ViewAll(CInternalFrame parent, String title, HashMap paramMap) {
        super(parent, true);
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
        setTitle(title);
    }
    
    /** Creates new form ViewAll */
    public ViewAll(CPanel panelParent, String title, HashMap paramMap) {
        this.panelParent = panelParent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
        setTitle(title);
        observable.setScreenName(title);
    }
    
    private void setupInit() {
        initComponents();
        internationalize();
        setObservable();
        toFront();
        //System.out.println("paramap....."+paramMap);
          HashMap whereMap = new HashMap();
          if (paramMap.containsKey(CommonConstants.MAP_WHERE) && paramMap.get(CommonConstants.MAP_WHERE) instanceof HashMap) {
                    whereMap = (HashMap) paramMap.get(CommonConstants.MAP_WHERE);
                    whereMap.put("FILTERED_LIST","");
                    paramMap.put(CommonConstants.MAP_WHERE, whereMap);
                }
        //System.out.println("paramammmmmmmmmmmmmmm"+paramMap);
        populateData(paramMap);
        panMultiSearch.setVisible(false);
        cboAddFind.setVisible(false);
        if (parent != null) {
            parent.toFront();
            setTitle("List for " + parent.getTitle());
        }
        btnClear.setVisible(false);
        resizeColumnWidth(tblData);
    }
    
    private void setupScreen() {
        setModal(true);
        
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //System.out.println("setUpScreen");
        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
    public void resizeColumnWidth(CTable tblData) {
        tblData.setAutoResizeMode(tblData.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        srcTable.setAutoscrolls(true);

        for (int column = 0; column < tblData.getColumnCount(); column++) {
            TableColumn tableColumn = tblData.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < tblData.getRowCount(); row++) {
                setColour();
                TableCellRenderer cellRenderer = tblData.getCellRenderer(row, column);
                Component c = tblData.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + tblData.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                //  We've exceeded the maximum width, no need to check other rows

                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }
                  
            tableColumn.setPreferredWidth(preferredWidth);
        }
}
    private void setObservable() {
        try {
            observable = new ViewAllOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void populateData(HashMap mapID) {
        try {
            //log.info("populateData...");
            ArrayList heading = observable.populateData(mapID, tblData);
            //System.out.println("headingheading"+heading);
            if (heading!=null) {
                EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(heading);
                cboSearchCol.setModel(cboModel);
                cboSearchCriteria.setSelectedItem("Pattern Match");
                 if (selItem.length()>0) {
                    cboSearchCol.setSelectedItem(selItem);
                }
                HashMap whereMap = new HashMap();
                if (paramMap.containsKey(CommonConstants.MAP_WHERE) && paramMap.get(CommonConstants.MAP_WHERE) instanceof HashMap) {
                    whereMap = (HashMap) paramMap.get(CommonConstants.MAP_WHERE);
                }
                if (whereMap.containsKey("FILTERED_LIST")) {
                   cboSearchCriteria.setVisible(false);
                   chkCase.setVisible(false);
                } else {
                    cboSearchCriteria.setVisible(true);
                    chkCase.setVisible(true);
                }
            }
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
            if (parent != null) {
                HashMap fillMap = observable.fillData(rowIndexSelected);
                //Commented By Revathi.L reff by Mr.Rajesh
//                String lockedBy = "";
//                if (paramMap.containsKey(ClientConstants.RECORD_KEY_COL)) {
//                    HashMap map = new HashMap();
//                    map.put("SCREEN_ID", parent.getScreenID());
//                    
//                    ArrayList lstRecKey = (ArrayList) paramMap.get(ClientConstants.RECORD_KEY_COL);
//                    
//                    StringBuffer strRecKey = new StringBuffer();
//                    for (int i=0, j=lstRecKey.size(); i < j; i++) {
//                        strRecKey.append(fillMap.get(lstRecKey.get(i)));
//                    }
//                    
//                    map.put("RECORD_KEY", strRecKey.toString());
//                    map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
//                    map.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
//                    map.put("CUR_DATE", ClientUtil.getCurrentDate());
//                    //System.out.println("Record Key Map : " + map);
//                    
//                    strRecKey = null;
//                    List lstLock = ClientUtil.executeQuery("selectEditLock", map);
//                    
//                    if (lstLock.size() > 0) {
//                        lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
//                        if (!lockedBy.equals(ProxyParameters.USER_ID)) {
//                            parent.setMode(ClientConstants.ACTIONTYPE_VIEW_MODE);
//                        } else {
//                            parent.setMode(ClientConstants.ACTIONTYPE_EDIT);
//                        }
//                    } else {
//                        parent.setMode(ClientConstants.ACTIONTYPE_EDIT);
//                    }
//                    
//                    parent.setOpenForEditBy(lockedBy);
//                    if (lockedBy.equals(""))
//                        ClientUtil.execute("insertEditLock", map);
//                    
//                    map = null;
//                }
                
                ((CInternalFrame) parent).fillData(fillMap);
                
//                if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
//                    String data = getLockDetails(lockedBy, parent.getScreenID()) ;
//                    ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
//                    parent.setMode(ClientConstants.ACTIONTYPE_VIEW_MODE);
//                }
                
                fillMap = null;
            }
            
            if (panelParent != null) ((CPanel) panelParent).fillData(observable.fillData(rowIndexSelected));
             TTIntegration c=new  TTIntegration();
            if (dialogparent != null) c.fillData(observable.fillData(rowIndexSelected));
            if (paramMap.containsKey("ACCT_SEARCH")) {
                AcctSearchUI ac = new AcctSearchUI("view");
                if (dialogparent != null) {
                    ac.fillData(observable.fillData(rowIndexSelected));
                }
            }
            if (paramMap.containsKey("SUSPENSE_SEARCH")) {
                SuspenceAcctSearchUI atsa = new SuspenceAcctSearchUI("SuspenceAcctSearch");
                if (dialogparent != null) {
                    atsa.fillData(observable.fillData(rowIndexSelected));
                }
            }
            if (paramMap.containsKey("MDS_GROUP")) {
                MdsGroupUI mdsGroup = new MdsGroupUI("mdsGroup");
                if (dialogparent != null) {
                    mdsGroup.fillData(observable.fillData(rowIndexSelected));
                }
            }
             if (paramMap.containsKey("DEPOSIT_GROUP")) {
                DepositGroupsUI depositGroupsUI = new DepositGroupsUI("depositGroup");
                if (dialogparent != null) {
                    depositGroupsUI.fillData(observable.fillData(rowIndexSelected));
                }
            }

        }
    }
    
    private String getLockDetails(String lockedBy, String screenId){
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer() ;
        map.put("LOCKED_BY", lockedBy) ;
        map.put("SCREEN_ID", screenId) ;
        List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if(lstLock.size() > 0){
            map = (HashMap)(lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME")) ;
            data.append("\nIP Address : ").append(map.get("IP_ADDR")) ;
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null ;
        map = null ;
        return data.toString();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        lblSearch = new com.see.truetransact.uicomponent.CLabel();
        cboSearchCol = new com.see.truetransact.uicomponent.CComboBox();
        btnSearch = new com.see.truetransact.uicomponent.CButton();
        cboSearchCriteria = new com.see.truetransact.uicomponent.CComboBox();
        txtSearchData = new javax.swing.JTextField();
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
        btnClear = new com.see.truetransact.uicomponent.CButton();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        lblSearch.setText("Search");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(lblSearch, gridBagConstraints);

        cboSearchCol.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSearchCol.setPopupWidth(180);
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
        panSearchCondition.add(btnSearch, gridBagConstraints);

        cboSearchCriteria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ends with", "Starts with", "Exact Match", "Pattern Match" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(cboSearchCriteria, gridBagConstraints);

        txtSearchData.setPreferredSize(new java.awt.Dimension(100, 21));
        txtSearchData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchDataActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panSearchCondition.add(txtSearchData, gridBagConstraints);

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

        panTable.setMaximumSize(new java.awt.Dimension(800, 500));
        panTable.setMinimumSize(new java.awt.Dimension(800, 500));
        panTable.setPreferredSize(new java.awt.Dimension(800, 500));
        panTable.setLayout(new java.awt.GridBagLayout());

        srcTable.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        srcTable.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

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

        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnOk, gridBagConstraints);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnCancel, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClear, gridBagConstraints);

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
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchDataActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchDataActionPerformed
    
    
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        this.dispose();
        //System.out.println("observable.fillData(-1) " + observable.fillData(-1));
        if (parent != null) ((CInternalFrame) parent).fillData(observable.fillData(-1));
        if (panelParent != null) ((CPanel) panelParent).fillData(observable.fillData(-1));
        
    }//GEN-LAST:event_btnClearActionPerformed
    private ComboBoxModel getListModel() {
        ComboBoxModel listData = new ComboBoxModel();
        return listData;
    }
    
     private void setColour() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String str = CommonUtil.convertObjToStr(table.getValueAt(row, 1));
               // System.out.println("setColour str : "+str);
                //if (colorList.contains(String.valueOf(row))) {
                if (CommonUtil.convertObjToStr(str).equals("CLOSED")) {
                   // System.out.println("setColour str inside closed : "+str);
                    setForeground(Color.red);
                } else {
                    //System.out.println("setColour str else : "+str);
                    setForeground(Color.BLACK);
                }
                this.setOpaque(true);
                return this;
            }
        };
        tblData.setDefaultRenderer(Object.class, renderer);
    }
    
    private void cboAddFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAddFindActionPerformed
        // Add your handling code here:
        //((ComboBoxModel) lstSearch.getModel()).addElement(
        String strTxtData = txtSearchData.getText();
        
        if (!strTxtData.equals("")) {
            StringBuffer strBCondition = new StringBuffer();
            strBCondition.append((String) cboSearchCol.getSelectedItem());
            strBCondition.append(" ");
            strBCondition.append((String) cboSearchCriteria.getSelectedItem());
            strBCondition.append(" ");
            strBCondition.append(strTxtData);
            strBCondition.append(" ");
            strBCondition.append((String) cboAddFind.getSelectedItem());
            ((ComboBoxModel) lstSearch.getModel()).addElement(strBCondition.toString());
        }
    }            private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-LAST:event_cboAddFindActionPerformed
                // Add your handling code here://GEN-FIRST:event_btnSearchActionPerformed
               // Add your handling code here:                                          
                String searchTxt = txtSearchData.getText().trim();
                        if (searchTxt.length()<=0) {
                    ClientUtil.showAlertWindow("Please enter the search String...");
                    txtSearchData.requestFocus();
                    return;
//                    if (observable.getDataSize()>=observable.MAXDATA) { 
//                        int opt=ClientUtil.confirmationAlert("You have not entered the search String.\nThis will take several minutes.\nContinue Anyway?");
//                        if (opt!=0) {
//                            txtSearchData.requestFocus();
//                            return;
//                        }
//                        else {
//                            observable.populateTable();
//                            return;
//                        }
//                    } else {
//                        observable.populateTable();
//                        return;
//                    }
                } 
                if (!chkCase.isSelected()) searchTxt = searchTxt.toUpperCase();

//                _tblData.setModel(observable.getTableModel());
//                _tblData.revalidate();
//                observable.refreshTable();

                int selCol = cboSearchCol.getSelectedIndex();
                int selColCri = cboSearchCriteria.getSelectedIndex();
                selItem = cboSearchCol.getSelectedItem()+"";
                HashMap whereMap = new HashMap();
                if (paramMap.containsKey(CommonConstants.MAP_WHERE) && paramMap.get(CommonConstants.MAP_WHERE) instanceof HashMap) {
                    whereMap = (HashMap) paramMap.get(CommonConstants.MAP_WHERE);
                }
                if (whereMap.containsKey("FILTERED_LIST")) {
                    for (int i=0; i<cboSearchCol.getItemCount(); i++) {
                        if (whereMap.containsKey(cboSearchCol.getItemAt(i))) {
                            whereMap.remove(cboSearchCol.getItemAt(i));
                        }
                    }
                    whereMap.remove("FILTERED_LIST");
                    if (searchTxt.length()>0) {
                        whereMap.put(selItem, searchTxt);
                    }
                   //System.out.println("#$#$ Before populateData paramMap:"+paramMap);
                    paramMap.put(CommonConstants.MAP_WHERE, whereMap);
                    populateData(paramMap);
                    observable.searchData(searchTxt, selCol, selColCri, chkCase.isSelected());
                    setColour();
                    whereMap.put("FILTERED_LIST","");
//                    cboSearchCriteria.setVisible(false);
//                    chkCase.setVisible(false);
//                    System.out.println("#$#$ After populateData paramMap:"+paramMap);
                } else {
                    observable.searchData(searchTxt, selCol, selColCri, chkCase.isSelected());
                }
    }//GEN-LAST:event_btnSearchActionPerformed
                        private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
                            // Add your handling code here:
                            this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed
                            private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
                                // Add your handling code here:
                                whenTableRowSelected();
    }//GEN-LAST:event_btnOkActionPerformed

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

    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
        // Add your handling code here:
        if ((evt.getClickCount() == 2) && (evt.getModifiers() == 16)) {
            whenTableRowSelected();
        }
    }//GEN-LAST:event_tblDataMousePressed
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
                                
                                mapParam.put("MAPNAME", "getSelectInwardClearingAuthorizeTOList");
                                //mapParam.put("WHERE", where);
                                // HashMap rMap = (HashMap) dao.getData(mapParam);
                                
                                // HashMap testMap = new HashMap();
                                //testMap.put("MAPNAME", "getSelectOperativeAcctProductTOList");
                                new ViewAll(mapParam).show();
                            }
                            
                            public void update(Observable o, Object arg) {
                                
                            }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
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
    private javax.swing.JTextField txtSearchData;
    // End of variables declaration//GEN-END:variables
}

