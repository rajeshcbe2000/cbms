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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.JTextComponent;

/**
 * @author bala
 */
public class GoldLoanItemView extends com.see.truetransact.uicomponent.CDialog {
//    private final LoanSubsidyRB resourceBundle = new LoanSubsidyRB();

    private TableModelListener tableModelListener;
//    private LoanSubsidyOB observable;
    LinkedHashMap dataMap = null;
    CInternalFrame parent = null;
    javax.swing.JList lstSearch;
    java.util.ArrayList arrLst = new java.util.ArrayList();
    Date currDt = null;
    private String subsidyId = "";
    boolean isFilled = false;
    boolean transAmtEdit = false;
    private String sourceScreen = "";
    StringBuffer selDataBuff = new StringBuffer();
    HashMap selItemList = new HashMap();
    /**
     * Creates new form AuthorizeUI
     */
    public GoldLoanItemView() {
        setupInit();
        setupScreen();
    }

    /**
     * Creates new form AuthorizeUI
     */
    public GoldLoanItemView(CInternalFrame parent, HashMap paramMap) {
        this.parent = parent;
        setupInit();
        setupScreen();
    }

    /**
     * Creates new form AuthorizeUI
     */
    public GoldLoanItemView(String sourceScreen, LinkedHashMap dataMap,HashMap selItemList) {

        this.sourceScreen = sourceScreen;
        this.dataMap = dataMap;
        this.selItemList = selItemList;
        setupInit();
        setupScreen();
    }

    private void setupInit() {
        System.out.println("0001");
        currDt = ClientUtil.getCurrentDate();

        initComponents();
        internationalize();
        setObservable();

        setMaxLength();

        initSubsidyTableData();
        enableDisableSearchDetails(false);


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

    public StringBuffer getSelDataBuff() {
        return selDataBuff;
    }

    public void setSelDataBuff(StringBuffer selDataBuff) {
        this.selDataBuff = selDataBuff;
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
        panTotal = new com.see.truetransact.uicomponent.CPanel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();

        setTitle("Gold Loan Items");
        setMinimumSize(new java.awt.Dimension(408, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panTable.setMinimumSize(new java.awt.Dimension(400, 250));
        panTable.setPreferredSize(new java.awt.Dimension(400, 250));
        panTable.setLayout(new java.awt.GridBagLayout());

        panMembershipTable.setMinimumSize(new java.awt.Dimension(300, 500));
        panMembershipTable.setPreferredSize(new java.awt.Dimension(300, 500));
        panMembershipTable.setLayout(new java.awt.GridBagLayout());

        srpMemberShipCTable.setMaximumSize(new java.awt.Dimension(700, 700));
        srpMemberShipCTable.setMinimumSize(new java.awt.Dimension(452, 400));
        srpMemberShipCTable.setPreferredSize(new java.awt.Dimension(452, 400));

        tblData.setBackground(new java.awt.Color(212, 208, 200));
        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "select", "Item", "Quantity", "Remarks"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblData.setDragEnabled(true);
        tblData.setInheritsPopupMenu(true);
        tblData.setMaximumSize(new java.awt.Dimension(1000, 1000));
        tblData.setMinimumSize(new java.awt.Dimension(450, 1000));
        tblData.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 1000));
        tblData.setPreferredSize(new java.awt.Dimension(450, 1000));
        tblData.setRequestFocusEnabled(false);
        tblData.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblDataFocusLost(evt);
            }
        });
        srpMemberShipCTable.setViewportView(tblData);
        tblData.getColumnModel().getColumn(0).setMinWidth(20);
        tblData.getColumnModel().getColumn(0).setPreferredWidth(35);
        tblData.getColumnModel().getColumn(0).setMaxWidth(50);
        tblData.getColumnModel().getColumn(3).setPreferredWidth(150);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panMembershipTable.add(srpMemberShipCTable, gridBagConstraints);

        panTotal.setMaximumSize(new java.awt.Dimension(200, 25));
        panTotal.setMinimumSize(new java.awt.Dimension(200, 25));
        panTotal.setPreferredSize(new java.awt.Dimension(200, 25));
        panTotal.setLayout(new java.awt.GridBagLayout());

        btnSave.setText("SAVE");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        panTotal.add(btnSave, new java.awt.GridBagConstraints());

        btnClose.setText("CANCEL");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        panTotal.add(btnClose, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(18, 42, 18, 42);
        panMembershipTable.add(panTotal, gridBagConstraints);

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
                    "Select","Item", "Quantity", "Remarks"
                }) {

            Class[] types = new Class[]{
                java.lang.Boolean.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,};
            boolean[] canEdit = new boolean[]{
                true, false, true,true
            };
            
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 2) {
                    return true;
                }

                return canEdit[columnIndex];
            }
        });
        tblData.setCellSelectionEnabled(true);
        tblData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {

            }
        });
        setTableModelListener();
        tblData.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }

    private void tblDataPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblRecoveryListTallyPropertyChange
    }//GEN-LAST:event_tblRecoveryListTallyPropertyChange

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        boolean selVal = false;
        int count = 0;
         
         
        for (int i = 0; i < tblData.getRowCount(); i++) {
            selVal = ((Boolean) tblData.getValueAt(i, 0)).booleanValue();
            if (selVal) {
                count = count + 1;
                ArrayList temp = new ArrayList();
                temp.add(tblData.getValueAt(i, 1));
                temp.add(tblData.getValueAt(i, 2));
                temp.add(tblData.getValueAt(i, 3));
                String qty = CommonUtil.convertObjToStr(tblData.getValueAt(i, 2));
                String remarks = CommonUtil.convertObjToStr(tblData.getValueAt(i, 3)).trim();
                String regex = "(\\d+)";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(qty);
                String tempQty="";
                if (m.find()) {
                    tempQty = m.group();
                }
                else
                {
                  tempQty="";
                }
                qty = tempQty;
                 
                
             
              //  if(remarks.startsWith(regex))
                if (qty.length() > 0) {
                   // selDataBuff.append(count+"-");
                    selDataBuff.append(tblData.getValueAt(i, 1));
                    selDataBuff.append("-");
                    selDataBuff.append(qty);
                    if(remarks!=null && !remarks.equals("")){
                    selDataBuff.append("-");
                    selDataBuff.append(remarks);
                    }
                    selDataBuff.append("\n");
                } else {
                    ClientUtil.showMessageWindow("Please Enter Quantity in Row " + (i+1));
                    selDataBuff=new StringBuffer();
                    return;
                }

            }

        }
        if (count > 0 && selDataBuff != null) {
            this.dispose();        // TODO add your handling code here:
        } else {
            ClientUtil.showMessageWindow("Please Select Atleast One Item ");
        }
        //tblData.getModel().f
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed

        tblData = new CTable();
        ClientUtil.clearAll(this);
        this.dispose();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCloseActionPerformed

    private void tblDataFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblDataFocusLost
      
    }//GEN-LAST:event_tblDataFocusLost

    private void setTableModelListenerUpdate() {
        tableModelListener = new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    System.out.println("Cell " + e.getFirstRow() + ", "
                            + e.getColumn() + " changed. The new value: "
                            + tblData.getModel().getValueAt(e.getFirstRow(),
                            e.getColumn()));
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    if (column == 2) {
                        TableModel model = tblData.getModel();
                    }
                }
            }
        };
        tblData.getModel().addTableModelListener(tableModelListener);
      //  table.setRowSelectionInterval(row, row);
  // table.setColumnSelectionInterval(0, 0);
    }

    private Object[][] setTableData() {
        DefaultTableModel tblModel = (DefaultTableModel) tblData.getModel();
        HashMap whereMap = new HashMap();
        //added by rishaad for listing waive amount
        whereMap = this.dataMap;
        Object totalList[][] = new Object[whereMap.keySet().size()][4];
        int i = 0;
        for (Iterator it = whereMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            if (selItemList != null && selItemList.size() > 0) {
                if (selItemList.containsKey(entry.getKey())) {
                    String str=CommonUtil.convertObjToStr(selItemList.get(entry.getKey()));
                    String[] strarr=str.split("#");
                    totalList[i][0] = true;
                    totalList[i][2] = strarr[0];
                    if(strarr.length >1)
                     totalList[i][3] = strarr[1];
                } else {
                    totalList[i][0] = false;
                    totalList[i][2] = entry.getValue();
                }
            } else {
                totalList[i][0] = false;
                totalList[i][2] = entry.getValue();
            }
           
        totalList[i][1] = entry.getKey();
            i++;
        }
        return totalList;
    }

    private void setTableModelListener() {
        tableModelListener = new TableModelListener() {
          
            public void tableChanged(TableModelEvent e) {
  
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    if (column == 2) {
                        String digit = CommonUtil.convertObjToStr(tblData.getValueAt(row, column));
                        String regex = "(\\d+)";
                        Pattern p = Pattern.compile(regex);
                        Matcher m = p.matcher(digit);
                        if (m.find()) {
                            String val = m.group();
                            if (digit.equals(val)) {
                                tblData.getModel().removeTableModelListener(tableModelListener);
                                tblData.setValueAt(digit, row, column);
                                tblData.getModel().addTableModelListener(tableModelListener);
                                return;
                            } else {
                                ClientUtil.showMessageWindow("Enter Only Digits");
                                tblData.getModel().removeTableModelListener(tableModelListener);
                                tblData.setValueAt("1", row, column);
                                tblData.getModel().addTableModelListener(tableModelListener);
                                return;
                            }
                        } else {
                            ClientUtil.showMessageWindow("Enter Only Digits");
                            tblData.getModel().removeTableModelListener(tableModelListener);
                            tblData.setValueAt("1", row, column);
                            tblData.getModel().addTableModelListener(tableModelListener);
                            return;
                        }
                    }


                }
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
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CPanel panMembershipTable;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CPanel panTotal;
    private com.see.truetransact.uicomponent.CScrollPane srpMemberShipCTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    // End of variables declaration//GEN-END:variables
}
