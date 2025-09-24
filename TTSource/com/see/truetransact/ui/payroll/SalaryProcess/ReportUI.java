
/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ReportUI.java
 */

package com.see.truetransact.ui.payroll.SalaryProcess;


import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Date;
import java.util.HashMap;

public class ReportUI extends javax.swing.JDialog {

    Date currDt = null;
    MyReportViewer myReport;

    public ReportUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public ReportUI(SalaryProcessUI salaryUi) {
        initForm();
    }

    public ReportUI(String screen) {
        super();
        initForm();
    }

    private void initForm() {
        initComponents();
        currDt = ClientUtil.getCurrentDate();
        myReport = new MyReportViewer();
    }

       public void show() {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        setModal(true);
        super.show();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panSalaryDetails = new com.see.truetransact.uicomponent.CPanel();
        tdtSalaryDate = new com.see.truetransact.uicomponent.CDateField();
        lblSalaryMonth = new com.see.truetransact.uicomponent.CLabel();
        btnSubmitt = new com.see.truetransact.uicomponent.CButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(150, 150));
        setPreferredSize(new java.awt.Dimension(450, 200));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panSalaryDetails.setMinimumSize(new java.awt.Dimension(250, 90));
        panSalaryDetails.setPreferredSize(new java.awt.Dimension(450, 200));

        lblSalaryMonth.setText("Salary Month:  ");

        btnSubmitt.setText("Submitt");
        btnSubmitt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmittActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panSalaryDetailsLayout = new javax.swing.GroupLayout(panSalaryDetails);
        panSalaryDetails.setLayout(panSalaryDetailsLayout);
        panSalaryDetailsLayout.setHorizontalGroup(
            panSalaryDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
            .addGroup(panSalaryDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panSalaryDetailsLayout.createSequentialGroup()
                    .addGap(0, 30, Short.MAX_VALUE)
                    .addGroup(panSalaryDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panSalaryDetailsLayout.createSequentialGroup()
                            .addComponent(lblSalaryMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(tdtSalaryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panSalaryDetailsLayout.createSequentialGroup()
                            .addGap(56, 56, 56)
                            .addComponent(btnSubmitt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(0, 30, Short.MAX_VALUE)))
        );
        panSalaryDetailsLayout.setVerticalGroup(
            panSalaryDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
            .addGroup(panSalaryDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panSalaryDetailsLayout.createSequentialGroup()
                    .addGap(0, 14, Short.MAX_VALUE)
                    .addGroup(panSalaryDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panSalaryDetailsLayout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addComponent(lblSalaryMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(tdtSalaryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(13, 13, 13)
                    .addComponent(btnSubmitt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 15, Short.MAX_VALUE)))
        );

        getContentPane().add(panSalaryDetails, new java.awt.GridBagConstraints());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSubmittActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmittActionPerformed
        if (tdtSalaryDate.getDateValue() != null) {
            myReport.setSalaryDate(tdtSalaryDate.getDateValue());
            String reportCheck = myReport.acquitenceReport("", "Report");
            if (reportCheck == null || reportCheck.equals("")) {
                ClientUtil.showMessageWindow("The document has no pages!!!");
            } else {
                myReport.openURL("file:///" + CommonConstants.SERVER_PATH + "/report/processreport.html");
            }
        } else {
            ClientUtil.showMessageWindow("Please Select Salary Month");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSubmittActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                ReportUI dialog = new ReportUI(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnSubmitt;
    private com.see.truetransact.uicomponent.CLabel lblSalaryMonth;
    private com.see.truetransact.uicomponent.CPanel panSalaryDetails;
    private com.see.truetransact.uicomponent.CDateField tdtSalaryDate;
    // End of variables declaration//GEN-END:variables
}
