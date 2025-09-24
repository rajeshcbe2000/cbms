/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TTProgress.java
 *
 * Created on January 16, 2004, 11:41 AM
 */

package com.see.truetransact.ui.login;

import javax.swing.Timer;
import javax.swing.Action;
import javax.swing.AbstractAction;

import java.util.HashMap;
import java.util.List;

import java.sql.*;

import java.awt.event.ActionEvent;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientConstants;
/**
 *
 * @author  bala
 */
public class TTProgress extends javax.swing.JWindow {
    private Timer timer; 
    private Statement stat;
    private List data;
    private int lstSize, increase, dbCount = 0;
    
    /** Creates new form TTProgress */
    public TTProgress() {
        initComponents();
        initSetup();
    }
    
    private void initSetup() {
        updateLocalDB(ClientConstants.HO == null ? "" : ClientConstants.HO);
        increase = Math.abs(pbrProgress.getMaximum() / lstSize);
        timer = new Timer(50, createLoadAction()); 
        timer.start(); 
    }

    public Action createLoadAction() {
        return new AbstractAction("Load Action") { 
            public void actionPerformed (ActionEvent e) { 
                if(pbrProgress.getValue() < pbrProgress.getMaximum()) { 
                    pbrProgress.setValue(pbrProgress.getValue() + increase);
                    dbCount++;
                    if (dbCount < lstSize)
                        updateCustomerData ();
                } else { 
                    if(timer != null) { 
                        lblCount.setText(lblCount.getText() + " " + lstSize);
                        timer.stop();
                        timer = null;
                    } 
                } 
            }
        };
    }
    
    private void updateLocalDB(String branchCode) {
        try {
            HashMap whereMap = new HashMap();
            whereMap.put ("BRANCH_CODE", branchCode);
            data = (List) ClientUtil.executeQuery("getSelectCustomerBranchTO", whereMap);
            lstSize = data.size();
            
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            Connection con = DriverManager.getConnection("jdbc:odbc:TTLocal");
            stat = con.createStatement();
            stat.execute("DELETE FROM CUSTOMER");
            
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    
    public void updateCustomerData() {
        HashMap row = (HashMap) data.get(dbCount);
        System.out.println (row);
        
        StringBuffer strBSql = new StringBuffer();
        strBSql.append ("INSERT INTO CUSTOMER (");
        strBSql.append ("RELATIONMANAGER, ");       strBSql.append ("DELETEDDT, ");
        strBSql.append ("VEHICLE, ");               strBSql.append ("COMM_ADDR_TYPE, ");
        strBSql.append ("REMARKS, ");               strBSql.append ("EMAIL_ID, ");
        strBSql.append ("FNAME, ");                 strBSql.append ("MARITALSTATUS, ");
        strBSql.append ("LNAME, ");                 strBSql.append ("NATIONALITY, ");
        strBSql.append ("CUST_TYPE_ID, ");          strBSql.append ("PRIMARY_OCCUPATION, ");
        strBSql.append ("STATUS, ");                strBSql.append ("BRANCH_CODE, ");
        strBSql.append ("EDUCATION, ");             strBSql.append ("CUSTOMERGROUP, ");
        strBSql.append ("RESIDENTIALSTATUS, ");     strBSql.append ("PROFESSION, ");
        strBSql.append ("CREATEDDT, ");             strBSql.append ("NETWORTH, ");
        strBSql.append ("LANGUAGE, ");              strBSql.append ("ANNINCOME, ");
        strBSql.append ("COMP_NAME, ");             strBSql.append ("TITLE, ");
        strBSql.append ("MNAME, ");                 strBSql.append ("PREFERRED_COMM, ");
        strBSql.append ("GENDER, ");                strBSql.append ("MINOR, ");
        strBSql.append ("CUST_ID, ");               strBSql.append ("DOB) VALUES ('");
        strBSql.append (row.get("RELATIONMANAGER") + "', ");    strBSql.append ("null, '"); 
        strBSql.append (row.get("VEHICLE") + "', '");           strBSql.append (row.get("COMM_ADDR_TYPE") + "', '"); 
        strBSql.append (row.get("REMARKS") + "', '");           strBSql.append (row.get("EMAIL_ID") + "', '"); 
        strBSql.append (row.get("FNAME") + "', '");             strBSql.append (row.get("MARITALSTATUS") + "', '"); 
        strBSql.append (row.get("LNAME") + "', '");             strBSql.append (row.get("NATIONALITY") + "', '"); 
        strBSql.append (row.get("CUST_TYPE_ID") + "', '");      strBSql.append (row.get("PRIMARY_OCCUPATION") + "', '"); 
        strBSql.append (row.get("STATUS") + "', '");            strBSql.append (row.get("BRANCH_CODE") + "', '"); 
        strBSql.append (row.get("EDUCATION") + "', '");         strBSql.append (row.get("CUSTOMERGROUP") + "', '"); 
        strBSql.append (row.get("RESIDENTIALSTATUS") + "', '"); strBSql.append (row.get("PROFESSION") + "', '"); 
        strBSql.append (DateUtil.getStringDate((java.util.Date) row.get("CREATEDDT")) + "', '");
        strBSql.append (row.get("NETWORTH") + "', '"); 
        strBSql.append (row.get("LANGUAGE") + "', '");          strBSql.append (row.get("ANNINCOME") + "', '"); 
        strBSql.append (row.get("COMP_NAME") + "', '");         strBSql.append (row.get("TITLE") + "', '"); 
        strBSql.append (row.get("MNAME") + "', '");             strBSql.append (row.get("PREFERRED_COMM") + "', '"); 
        strBSql.append (row.get("GENDER") + "', '");            strBSql.append (row.get("MINOR") + "', '"); 
        strBSql.append (row.get("CUST_ID") + "', '");
        strBSql.append (DateUtil.getStringDate((java.util.Date) row.get("DOB")) + "')"); 

        lblCurrentlCustomer.setText("Customer ID : " + (String) row.get("CUST_ID") + "   [" + (dbCount+1) + "]");
        try {
            stat.execute(strBSql.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panProgress = new com.see.truetransact.uicomponent.CPanel();
        pbrProgress = new com.see.truetransact.uicomponent.CProgressBar();
        panTop = new com.see.truetransact.uicomponent.CPanel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        lblTitle = new com.see.truetransact.uicomponent.CLabel();
        panBottom = new com.see.truetransact.uicomponent.CPanel();
        lblCount = new com.see.truetransact.uicomponent.CLabel();
        lblCurrentlCustomer = new com.see.truetransact.uicomponent.CLabel();
        btnOk = new com.see.truetransact.uicomponent.CButton();

        getContentPane().setLayout(new java.awt.BorderLayout(10, 10));

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        panProgress.setLayout(new java.awt.GridBagLayout());

        pbrProgress.setStringPainted(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panProgress.add(pbrProgress, gridBagConstraints);

        getContentPane().add(panProgress, java.awt.BorderLayout.CENTER);

        panTop.setLayout(new java.awt.GridBagLayout());

        lblMsg.setText("Copying Customer Data from Host Database to Local Database");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        panTop.add(lblMsg, gridBagConstraints);

        lblTitle.setText("Downloading...");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        panTop.add(lblTitle, gridBagConstraints);

        getContentPane().add(panTop, java.awt.BorderLayout.NORTH);

        panBottom.setLayout(new java.awt.GridBagLayout());

        lblCount.setText("Total Number of Customers imported : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        panBottom.add(lblCount, gridBagConstraints);

        lblCurrentlCustomer.setText("Customer ID : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        panBottom.add(lblCurrentlCustomer, gridBagConstraints);

        btnOk.setText("Close");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panBottom.add(btnOk, gridBagConstraints);

        getContentPane().add(panBottom, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // Add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnOkActionPerformed
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        //System.exit(0);
        this.dispose();
    }//GEN-LAST:event_exitForm
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new TTProgress().show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CLabel lblCount;
    private com.see.truetransact.uicomponent.CLabel lblCurrentlCustomer;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblTitle;
    private com.see.truetransact.uicomponent.CPanel panBottom;
    private com.see.truetransact.uicomponent.CPanel panProgress;
    private com.see.truetransact.uicomponent.CPanel panTop;
    private com.see.truetransact.uicomponent.CProgressBar pbrProgress;
    // End of variables declaration//GEN-END:variables
}
