/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * EmpDetailsUI.java
 *
 * 
 */
package com.see.truetransact.ui.payroll.pfMaster;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonUtil;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author anjuanand
 */
public class EmpDetailsUI extends com.see.truetransact.uicomponent.CPanel {

    private EmpDetailsRB resourceBundle = new EmpDetailsRB();

    /**
     * Creates new form EmpDetailsUI
     */
    public EmpDetailsUI(com.see.truetransact.uicomponent.CPanel panLabelValues) {
        initComponents();
        initTable();
        addToScreen(panLabelValues);
        updateEmployeeInfo(null);
    }

    public void updateEmployeeInfo(String empId) {
        HashMap map = new HashMap();
        map.put("EMP_ID", empId);
        List output = ClientUtil.executeQuery("getEmplDetails", map);
        map = null;
        if (output != null && output.size() > 0) {
            HashMap dataMap = new HashMap();
            dataMap = (HashMap) output.get(0);
            output = null;

            ArrayList row = new ArrayList();
            ArrayList data = new ArrayList();
            row.add(resourceBundle.getString("custId"));
            row.add(dataMap.get("CUST_ID"));
            data.add(row);

            row = new ArrayList();
            row.add(resourceBundle.getString("custName"));
            StringBuffer strB = new StringBuffer();
            strB.append(CommonUtil.convertObjToStr(dataMap.get("FNAME")));
            strB.append(" ");
            strB.append(CommonUtil.convertObjToStr(dataMap.get("MNAME")));
            strB.append(" ");
            strB.append(CommonUtil.convertObjToStr(dataMap.get("LNAME")));
            row.add(strB);
            strB = null;
            data.add(row);

            row = new ArrayList();
            row.add(resourceBundle.getString("empStreet"));
            row.add(dataMap.get("STREET"));
            data.add(row);

            row = new ArrayList();
            row.add(resourceBundle.getString("empArea"));
            row.add(dataMap.get("AREA"));
            data.add(row);

            row = new ArrayList();
            row.add(resourceBundle.getString("empCity"));
            row.add(dataMap.get("CITY"));
            data.add(row);

            row = new ArrayList();
            row.add(resourceBundle.getString("empCountry"));
            row.add(dataMap.get("COUNTRY_CODE"));
            data.add(row);

            row = new ArrayList();
            row.add(resourceBundle.getString("empPinCode"));
            row.add(dataMap.get("PIN_CODE"));
            data.add(row);

            row = new ArrayList();
            row.add(resourceBundle.getString("empState"));
            row.add(dataMap.get("STATE"));
            data.add(row);
            row = new ArrayList();
            row.add(resourceBundle.getString("dob"));
            row.add(dataMap.get("DOB"));
            data.add(row);

            ArrayList col = new ArrayList();
            col.add("");
            col.add("");

            EnhancedTableModel model = new EnhancedTableModel(data, col);
            tblEmpDetails.setModel(model);
            tblEmpDetails.revalidate();
            dataMap = null;
        } else {
            initComponents();
            initTable();
        }
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
        /*
         * Set a cellrenderer to this table in order format the date
         */
        EnhancedTableModel model = new EnhancedTableModel();
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                Color color = panEmpDetails.getBackground();

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
        tblEmpDetails.setDefaultRenderer(Object.class, renderer);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panEmployeeDetails = new com.see.truetransact.uicomponent.CPanel();
        panEmpDetails = new com.see.truetransact.uicomponent.CPanel();
        scrEmployeeDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblEmpDetails = new com.see.truetransact.uicomponent.CTable();

        setMinimumSize(new java.awt.Dimension(200, 229));
        setPreferredSize(new java.awt.Dimension(200, 229));
        setLayout(new java.awt.BorderLayout());

        panEmployeeDetails.setMinimumSize(new java.awt.Dimension(100, 200));
        panEmployeeDetails.setPreferredSize(new java.awt.Dimension(100, 200));
        panEmployeeDetails.setLayout(new java.awt.GridBagLayout());

        panEmpDetails.setPreferredSize(new java.awt.Dimension(24, 24));
        panEmpDetails.setLayout(new java.awt.GridBagLayout());

        scrEmployeeDetails.setPreferredSize(new java.awt.Dimension(454, 350));

        tblEmpDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scrEmployeeDetails.setViewportView(tblEmpDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panEmpDetails.add(scrEmployeeDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeDetails.add(panEmpDetails, gridBagConstraints);

        add(panEmployeeDetails, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        javax.swing.JFrame jf = new javax.swing.JFrame();

        EmpDetailsUI bui = new EmpDetailsUI(null);
        jf.setSize(300, 525);
        jf.getContentPane().add(bui);
        jf.show();
        bui.show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CPanel panEmpDetails;
    private com.see.truetransact.uicomponent.CPanel panEmployeeDetails;
    private com.see.truetransact.uicomponent.CScrollPane scrEmployeeDetails;
    private com.see.truetransact.uicomponent.CTable tblEmpDetails;
    // End of variables declaration//GEN-END:variables
}
