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
package com.see.truetransact.ui.common.viewall;

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import javax.swing.table.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Component;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.event.ListSelectionListener;
//import javax.swing.DefaultListModel;

import org.apache.log4j.Logger;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.EnhancedComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.viewall.ViewAll;

import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CTable;
import java.util.*;

/**
 * @author bala
 */
public class FrmSuretyTableUI extends com.see.truetransact.uicomponent.CDialog {
//    private final LoanSubsidyRB resourceBundle = new LoanSubsidyRB();

    private TableModelListener tableModelListener;
//    private LoanSubsidyOB observable;
    HashMap dataMap = null;
    List dataList = null;
    CInternalFrame parent = null;
    javax.swing.JList lstSearch;
    java.util.ArrayList arrLst = new java.util.ArrayList();
    Date currDt = null;
    private String subsidyId = "";
    boolean isFilled = false;
    boolean transAmtEdit = false;
    private String sourceScreen = "";
    private ArrayList recoveryList = new ArrayList();
     public String rowData = "";

    /**
     * Creates new form AuthorizeUI
     */
    public FrmSuretyTableUI() {
        setupInit();
        setupScreen();
    }

    /**
     * Creates new form AuthorizeUI
     */
    public FrmSuretyTableUI(CInternalFrame parent, HashMap paramMap) {
        this.parent = parent;
        setupInit();
        setupScreen();
    }

    /**
     * Creates new form AuthorizeUI
     */
    public FrmSuretyTableUI(String sourceScreen, List dataLst) {

        this.sourceScreen = sourceScreen;
        this.dataList = dataLst;
        setupInit();
        setupScreen();
    }

    private void setupInit() {
        System.out.println("0001");
        currDt = ClientUtil.getCurrentDate();
        rowData="";
        initComponents();
        internationalize();
        setObservable();

        setMaxLength();

        initSubsidyTableData();


    }

    private void enableDisableSearchDetails(boolean flag) {
    }

    private void setMaxLength() {
    }

    private void setupScreen() {
        //        setModal(true);

        /*
         * Calculate the screen size
         */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        /*
         * Center frame on the screen
         */
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillData(Object obj) {
        HashMap dataMap = (HashMap) obj;
    }

    private void editDeleteTableData(HashMap map) {
        transAmtEdit = false;
        transAmtEdit = true;
        initSubsidyTableData();
    }

    public void show() {

//        if (isShow) {
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        pack();

        /*
         * Center frame on the screen
         */
        Dimension frameSize = getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
        setModal(true);
        super.show();
        // }


//          super.show();
    }

    /**
     * Bring up and populate the temporary project detail screen.
     */
    private void whenTableRowSelected() {
        int rowIndexSelected = tblData.getSelectedRow();
        setColour();
    }

    private boolean isSelectedRowTicked(com.see.truetransact.uicomponent.CTable table) {
        boolean selected = false;
        for (int i = 0, j = table.getRowCount(); i < j; i++) {
            selected = ((Boolean) table.getValueAt(i, 0)).booleanValue();
            if (!selected) {
                //            table.setForeground(Colu
                break;
            }
        }
        return selected;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panTable = new com.see.truetransact.uicomponent.CPanel();
        panMembershipTable = new com.see.truetransact.uicomponent.CPanel();
        srpMemberShipCTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();

        setTitle("Edit Table UI");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panTable.setMinimumSize(new java.awt.Dimension(400, 250));
        panTable.setPreferredSize(new java.awt.Dimension(400, 250));
        panTable.setLayout(new java.awt.GridBagLayout());

        panMembershipTable.setMinimumSize(new java.awt.Dimension(300, 250));
        panMembershipTable.setPreferredSize(new java.awt.Dimension(300, 250));
        panMembershipTable.setLayout(new java.awt.GridBagLayout());

        srpMemberShipCTable.setMinimumSize(new java.awt.Dimension(450, 200));

        tblData.setBackground(new java.awt.Color(212, 208, 200));
        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Emp No", "Head", "Acct No"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblData.setDragEnabled(true);
        tblData.setInheritsPopupMenu(true);
        tblData.setMaximumSize(new java.awt.Dimension(1000, 1000));
        tblData.setMinimumSize(new java.awt.Dimension(450, 200));
        tblData.setPreferredScrollableViewportSize(new java.awt.Dimension(100, 500));
        tblData.setPreferredSize(new java.awt.Dimension(450, 200));
        tblData.setRequestFocusEnabled(false);
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDataMousePressed(evt);
            }
        });
        srpMemberShipCTable.setViewportView(tblData);

        panMembershipTable.add(srpMemberShipCTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        panTable.add(panMembershipTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panTable, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void initSubsidyTableData() {
        tblData.setModel(new javax.swing.table.DefaultTableModel(
                setTableData(),
                new String[]{
                    "Name", "Emp No", "Head","Acct No"
                }) {

            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
             java.lang.String.class,};
            boolean[] canEdit = new boolean[]{
                false, false, false,false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
           
        });


        tblData.setCellSelectionEnabled(true);
        tblData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblDataPropertyChange(evt);

            }
        });
        setTableModelListener();

    }

    private void tblDataPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblRecoveryListTallyPropertyChange
    }//GEN-LAST:event_tblRecoveryListTallyPropertyChange
public String selectedData()
{
    return rowData;
}
private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
// TODO add your handling code here:
    int selrow = tblData.getSelectedRow();
    rowData = CommonUtil.convertObjToStr(tblData.getModel().getValueAt(selrow, 0)).trim() + "||"
            + CommonUtil.convertObjToStr(tblData.getModel().getValueAt(selrow, 1)).trim() + "||"
            + CommonUtil.convertObjToStr(tblData.getModel().getValueAt(selrow, 2)).trim() + "||"
            + CommonUtil.convertObjToStr(tblData.getModel().getValueAt(selrow, 3)).trim();
     System.out.println("data  :"+rowData);
        
    tblData = new CTable();
    ClientUtil.clearAll(this);
    this.dispose();

}//GEN-LAST:event_tblDataMousePressed

    private Object[][] setTableData() {
        DefaultTableModel tblModel = (DefaultTableModel) tblData.getModel();
        HashMap whereMap = new HashMap();
        //added by rishaad for listing waive amount
        List allData= dataList; 
        Object totalList[][] = new Object[allData.size()][4];
        for(int i=0;i<allData.size();i++)
        {
            whereMap = (HashMap)allData.get(i);
            totalList[i][0]=whereMap.get("NAME");
             totalList[i][1]=whereMap.get("EMPLOYEE_NO");
              totalList[i][2]=whereMap.get("HEAD");
               totalList[i][3]=whereMap.get("ACTNUM");
        }
        return totalList;
    }

    private void setTableModelListener() {
        tableModelListener = new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
             
            }
        };
        tblData.getModel().addTableModelListener(tableModelListener);
    }

    public ArrayList getTableData() {
        ArrayList singleList = new ArrayList();
        ArrayList totList = new ArrayList();
        int count = tblData.getModel().getRowCount();
        int columnCount = tblData.getModel().getColumnCount();
        for (int i = 0; i < count; i++) {
            singleList = new ArrayList();
            for (int j = 0; j < columnCount; j++) {
                singleList.add(tblData.getValueAt(i, j));
            }
            totList.add(singleList);
        }
        return totList;
    }

    private void setColour() {
        /*
         * Set a cellrenderer to this table in order format the date
         */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                System.out.println("row #####" + row);
                boolean selected = ((Boolean) table.getValueAt(row, 0)).booleanValue();
                if (!selected) {
                    setForeground(Color.RED);
                } else {
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblData.setDefaultRenderer(Object.class, renderer);
    }

    private void internationalize() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    }

    public void update(Observable o, Object arg) {
    }

    public void updateOBFields() {
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CPanel panMembershipTable;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CScrollPane srpMemberShipCTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    // End of variables declaration//GEN-END:variables
}
