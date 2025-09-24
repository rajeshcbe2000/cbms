/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * CustDetailsUI.java
 *
 * Created on February 2, 2005, 5:34 PM
 */

package com.see.truetransact.ui.common.customer;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import java.awt.Component;
import java.awt.Color;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.JTable;

import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.common.customer.CustDetailsTO ;

import com.see.truetransact.commonutil.DateUtil;

/**
 *
 * @author  152691
 */
public class CustDetailsUI extends com.see.truetransact.uicomponent.CPanel {
    private CustDetailsRB resourceBundle = new CustDetailsRB();
    
    
    /** Creates new form CustDetailsUI */
    public CustDetailsUI(com.see.truetransact.uicomponent.CPanel panLabelValues) {
        initComponents();
        initTable();
        addToScreen(panLabelValues);
        updateCustomerInfo("");
   }
   
    public void updateCustomerInfo(String custId){
        HashMap map = new HashMap();
        CustDetailsTO custTo = new CustDetailsTO();
        map.put("CUST_ID", custId) ;
        List output = ClientUtil.executeQuery("getCustDetails", map);
        map = null ;
        if(output.size() > 0){
            custTo = (CustDetailsTO)output.get(0) ;
            output = null ;
        }
            ArrayList row = new ArrayList();
            ArrayList data = new ArrayList();
            row.add(resourceBundle.getString("custId"));
            row.add(custTo.getCustId());
            data.add(row);

            row = new ArrayList();
            row.add(resourceBundle.getString("custName"));
            StringBuffer strB = new StringBuffer();
            strB.append(CommonUtil.convertObjToStr(custTo.getCustFName()));
            strB.append(" ");
            strB.append(CommonUtil.convertObjToStr(custTo.getCustMName()));
            strB.append(" ");
            strB.append(CommonUtil.convertObjToStr(custTo.getCustLName()));
            row.add(strB);
            strB = null ;
            data.add(row);

            row = new ArrayList();
            row.add(resourceBundle.getString("custStreet"));
            row.add(custTo.getCustStreet());
            data.add(row);

            row = new ArrayList();
            row.add(resourceBundle.getString("custArea"));
            row.add(custTo.getCustArea());
            data.add(row);

            row = new ArrayList();
            row.add(resourceBundle.getString("custCity"));
            row.add(custTo.getCustCity());
            data.add(row);

            row = new ArrayList();
            row.add(resourceBundle.getString("custCountry"));
            row.add(custTo.getCustCountry());
            data.add(row);

            row = new ArrayList();
            row.add(resourceBundle.getString("custPinCode"));
            row.add(custTo.getCustPinCode());
            data.add(row);

            row = new ArrayList();
            row.add(resourceBundle.getString("custState"));
            row.add(custTo.getCustState());
            data.add(row);
            row = new ArrayList();
            row.add(resourceBundle.getString("dob"));
            row.add(DateUtil.getStringDate(custTo.getCustDob()));
            data.add(row);

            ArrayList col = new ArrayList();
            col.add("");
            col.add("");

            EnhancedTableModel model = new EnhancedTableModel(data, col);
            tblAcctDetails.setModel(model);
            tblAcctDetails.revalidate();
            custTo = null ;
        
    }
    
    private void addToScreen(com.see.truetransact.uicomponent.CPanel panLabelValues) {
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLabelValues.add(this, gridBagConstraints);
    }
    
    private void initTable() {
        /* Set a cellrenderer to this table in order format the date */
        EnhancedTableModel model = new EnhancedTableModel();
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected,hasFocus, row, column);
                
                Color color = panAcctDetails.getBackground();
                
                if (column == 0) {
                    setBackground(color);
                    setForeground(Color.BLACK);
                } else {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }
                this.setOpaque(true);
                return this;
            }
        };
        tblAcctDetails.setDefaultRenderer(Object.class, renderer);
    }
   
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panTransDetails = new com.see.truetransact.uicomponent.CPanel();
        panAcctDetails = new com.see.truetransact.uicomponent.CPanel();
        scrAcctDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblAcctDetails = new com.see.truetransact.uicomponent.CTable();

        setLayout(new java.awt.BorderLayout());

        setMinimumSize(new java.awt.Dimension(200, 229));
        setPreferredSize(new java.awt.Dimension(200, 229));
        panTransDetails.setLayout(new java.awt.GridBagLayout());

        panTransDetails.setMinimumSize(new java.awt.Dimension(100, 200));
        panTransDetails.setPreferredSize(new java.awt.Dimension(100, 200));
        panAcctDetails.setLayout(new java.awt.GridBagLayout());

        panAcctDetails.setPreferredSize(new java.awt.Dimension(24, 24));
        tblAcctDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrAcctDetails.setViewportView(tblAcctDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAcctDetails.add(scrAcctDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetails.add(panAcctDetails, gridBagConstraints);

        add(panTransDetails, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        javax.swing.JFrame jf = new javax.swing.JFrame();

        CustDetailsUI bui = new CustDetailsUI(null);
        jf.setSize(300,525);
        jf.getContentPane().add(bui);
        jf.show();
        bui.show();
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CPanel panAcctDetails;
    private com.see.truetransact.uicomponent.CPanel panTransDetails;
    private com.see.truetransact.uicomponent.CScrollPane scrAcctDetails;
    private com.see.truetransact.uicomponent.CTable tblAcctDetails;
    // End of variables declaration//GEN-END:variables
}
