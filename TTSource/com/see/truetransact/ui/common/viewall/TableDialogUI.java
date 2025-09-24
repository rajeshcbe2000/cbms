/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TableDialogUI.java
 *
 * Created on January 20, 2004, 10:45 AM
 */

package com.see.truetransact.ui.common.viewall;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
//import com.see.truetransact.clientutil.TableColorRenderer;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.transaction.cash.CashTransactionUI;
import com.see.truetransact.clientutil.EnhancedTableModel;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.Dimension;
/**
 *
 * @author  bala
 */
public class TableDialogUI extends com.see.truetransact.uicomponent.CDialog {
    boolean isShow = true;
    CashTransactionUI cashUI=null;
    EnhancedTableModel enhancedTable=new EnhancedTableModel();
    //Added By Suresh
    private String totalAmtValue = "";

    public String getTotalAmtValue() {
        return totalAmtValue;
    }

    public void setTotalAmtValue(String totalAmtValue) {
        this.totalAmtValue = totalAmtValue;
    }
    /** Creates new form TableDataUI */
    public TableDialogUI(String mapName) {
        initComponents();
        initSetup(mapName, null);
        lblTotalAmt.setVisible(false);
        lblTotalAmtValue.setVisible(false);
    }
    
    /**
     * Creates new form TableDataUI
     */
    public TableDialogUI(String mapName, HashMap whereConditionMap) {
        initComponents();
        initSetup(mapName, whereConditionMap);
        if (mapName.equals("showInterbranchTransaction")) {
            setPreferredSize(new Dimension(900, 400));
        } else {
            setPreferredSize(new Dimension(600, 300));//Added by Ajay Sharma for clubing the Cr/Dr/Ub balance together on 26-Mar-2014
        }
        if (whereConditionMap.containsKey("SMALL_SIZE")) {  //Added By Suresh R     05-Oct-2016
            setPreferredSize(new Dimension(450, 300));
        }
        lblTotalAmt.setVisible(false);
        lblTotalAmtValue.setVisible(false);
        if (whereConditionMap.containsKey("RESIZE")) {
            panDenomination.setPreferredSize(new java.awt.Dimension(800, 175));
            tblDenomination.getColumnModel().getColumn(0).setPreferredWidth(80);
            tblDenomination.getColumnModel().getColumn(1).setPreferredWidth(130);
            tblDenomination.getColumnModel().getColumn(2).setPreferredWidth(80);
            tblDenomination.getColumnModel().getColumn(3).setPreferredWidth(120);
            tblDenomination.getColumnModel().getColumn(4).setPreferredWidth(270);
            tblDenomination.getColumnModel().getColumn(5).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(6).setPreferredWidth(60);
            tblDenomination.getColumnModel().getColumn(7).setPreferredWidth(70);
        }
        if (whereConditionMap.containsKey("FROM_CHARGES_SCREEN")) { //Added By Suresh R     05-Oct-2016
            tblDenomination.getColumnModel().getColumn(0).setPreferredWidth(130);
            tblDenomination.getColumnModel().getColumn(1).setPreferredWidth(130);
            tblDenomination.getColumnModel().getColumn(2).setPreferredWidth(350);
        }
        
        if (whereConditionMap.containsKey("3_COLUMNS")) { //Added By Suresh R     06-Apr-2017
            setPreferredSize(new Dimension(463, 280));
            tblDenomination.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblDenomination.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblDenomination.getColumnModel().getColumn(2).setPreferredWidth(150);
        }
    }
    
    /** Creates new form TableDataUI */
    public TableDialogUI(String mapName, HashMap whereConditionMap,CashTransactionUI cashTransactionUI) {
        initComponents();
        initSetup(mapName, whereConditionMap);
        setTotalAmt();
        lblTotalAmt.setVisible(true);
        lblTotalAmtValue.setVisible(true);
        cashUI=new CashTransactionUI();
        cashUI=cashTransactionUI;
        btnOk.setFocusable(true);
    }
    public TableDialogUI(HashMap whereMap, String key) {
        initComponents();
        HashMap whereConditionMap = null;
        lblTotalAmt.setVisible(false);
        lblTotalAmtValue.setVisible(false);
        if (key.equals("TRADING SALES")) {    //Added By Revathi 15-sep-2015
            whereConditionMap = (HashMap) whereMap.get("TRADING SALES");
            if (whereConditionMap != null && whereConditionMap.size() > 0) {
                initSetup(whereConditionMap, "TRADING SALES");
                panDenomination.setPreferredSize(new java.awt.Dimension(520, 200));
                tblDenomination.getColumnModel().getColumn(0).setPreferredWidth(90);
                tblDenomination.getColumnModel().getColumn(1).setPreferredWidth(60);
                tblDenomination.getColumnModel().getColumn(2).setPreferredWidth(140);
                tblDenomination.getColumnModel().getColumn(3).setPreferredWidth(60);
                tblDenomination.getColumnModel().getColumn(4).setPreferredWidth(50);
                tblDenomination.getColumnModel().getColumn(5).setPreferredWidth(50);
                tblDenomination.getColumnModel().getColumn(6).setPreferredWidth(50);
                setPreferredSize(new Dimension(520, 200));
                setMinimumSize(new java.awt.Dimension(520, 200));
            }
        } else if (key.equals("UTR_NUMBER")) {     //If Contains Key UTR_NUMBER      FromScreen Remittance->RTGS/NEFT Screen
            whereConditionMap = (HashMap) whereMap.get("UTR_NUMBER");
            if (whereConditionMap != null && whereConditionMap.size() > 0) {
                initSetup(whereConditionMap, "UTR_NUMBER");
                panDenomination.setPreferredSize(new java.awt.Dimension(390, 200));
                tblDenomination.getColumnModel().getColumn(0).setPreferredWidth(180);
                tblDenomination.getColumnModel().getColumn(1).setPreferredWidth(150);
                setPreferredSize(new Dimension(350, 200));
                setMinimumSize(new java.awt.Dimension(300, 200));
            }
        }

    }
    
    private void initSetup(HashMap whereConditionMap, String key) {
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.MAP_NAME, "");
        whereMap.put("KEY", key);
        if (whereConditionMap != null) {
            whereMap.put(CommonConstants.MAP_WHERE, whereConditionMap);
        }
        if (!ClientUtil.setTableModel(whereMap, tblDenomination)) {
            isShow = false;
        }
    }
    
    /**
     * Creates new form TableDataUI
     */
    public TableDialogUI(String mapName, HashMap whereConditionMap, String string) {
        initComponents();
        initSetup(mapName, whereConditionMap);
        //Added By Suresh
        if (whereConditionMap.containsKey("RESIZE")) {
            panDenomination.setPreferredSize(new java.awt.Dimension(650, 175));
            tblDenomination.getColumnModel().getColumn(0).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(1).setPreferredWidth(120);
            tblDenomination.getColumnModel().getColumn(2).setPreferredWidth(220);
            tblDenomination.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblDenomination.getColumnModel().getColumn(4).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(5).setPreferredWidth(50);
            tblDenomination.getColumnModel().getColumn(6).setPreferredWidth(110);
            tblDenomination.getColumnModel().getColumn(7).setPreferredWidth(100);
        }
        lblTotalAmt.setVisible(true);
        lblTotalAmtValue.setVisible(true);
        setTotalAmt();
        
        if (whereConditionMap.containsKey("DCB_FILE_UPLOAD")) { //Added by Suresh R 04-Aug-2017
            panDenomination.setPreferredSize(new java.awt.Dimension(850, 275));
            tblDenomination.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblDenomination.getColumnModel().getColumn(1).setPreferredWidth(70);
            tblDenomination.getColumnModel().getColumn(2).setPreferredWidth(70);
            tblDenomination.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblDenomination.getColumnModel().getColumn(4).setPreferredWidth(80);
            tblDenomination.getColumnModel().getColumn(5).setPreferredWidth(80);
            tblDenomination.getColumnModel().getColumn(6).setPreferredWidth(260);
            tblDenomination.getColumnModel().getColumn(7).setPreferredWidth(70);
            tblDenomination.getColumnModel().getColumn(8).setPreferredWidth(80);
            lblTotalAmt.setVisible(false);
            lblTotalAmtValue.setVisible(false);
        }
        if (whereConditionMap.containsKey("RECONCILIATION")) { //Added by kannan  24/JUl-2015
            panDenomination.setPreferredSize(new java.awt.Dimension(745, 175));
            tblDenomination.getColumnModel().getColumn(0).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(1).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(2).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(3).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(4).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(5).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(6).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(7).setPreferredWidth(110);
            lblTotalAmt.setVisible(false);
            lblTotalAmtValue.setVisible(false);
        }
        
        if (whereConditionMap.containsKey("APEX_BANK")) { //Added by Revathi 05-Feb-2016
            panDenomination.setPreferredSize(new java.awt.Dimension(745, 275));
            tblDenomination.getColumnModel().getColumn(0).setPreferredWidth(190);
            tblDenomination.getColumnModel().getColumn(1).setPreferredWidth(60);
            tblDenomination.getColumnModel().getColumn(2).setPreferredWidth(60);
            tblDenomination.getColumnModel().getColumn(3).setPreferredWidth(60);
            tblDenomination.getColumnModel().getColumn(4).setPreferredWidth(70);
            tblDenomination.getColumnModel().getColumn(5).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(6).setPreferredWidth(190);
            tblDenomination.getColumnModel().getColumn(7).setPreferredWidth(50);
            tblDenomination.getColumnModel().getColumn(8).setPreferredWidth(70);
            lblTotalAmt.setVisible(false);
            lblTotalAmtValue.setVisible(false);
        }
        if (whereConditionMap.containsKey("SHARE_DIVIDEND")) { //Added by Suresh R 08-Aug-2016
            panDenomination.setPreferredSize(new java.awt.Dimension(745, 275));
            tblDenomination.getColumnModel().getColumn(0).setPreferredWidth(190);
            tblDenomination.getColumnModel().getColumn(1).setPreferredWidth(60);
            tblDenomination.getColumnModel().getColumn(2).setPreferredWidth(60);
            tblDenomination.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblDenomination.getColumnModel().getColumn(4).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(5).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(6).setPreferredWidth(450);
            tblDenomination.getColumnModel().getColumn(7).setPreferredWidth(70);
            tblDenomination.getColumnModel().getColumn(8).setPreferredWidth(70);
            lblTotalAmt.setVisible(false);
            lblTotalAmtValue.setVisible(false);
        }
        if (whereConditionMap.containsKey("SUBDAYRECON")) {//Added by kannan
            panDenomination.setPreferredSize(new java.awt.Dimension(545, 175));
            tblDenomination.getColumnModel().getColumn(0).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(1).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(2).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(3).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(4).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(5).setPreferredWidth(90);
            lblTotalAmt.setVisible(false);
            lblTotalAmtValue.setVisible(false);
        }

        if (whereConditionMap.containsKey("PRERECONCILIATION")) {//Added by kannan
            panDenomination.setPreferredSize(new java.awt.Dimension(720, 175));
            tblDenomination.getColumnModel().getColumn(0).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(1).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(2).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(3).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(4).setPreferredWidth(120);
            tblDenomination.getColumnModel().getColumn(5).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(6).setPreferredWidth(120);
            lblTotalAmt.setVisible(false);
            lblTotalAmtValue.setVisible(false);
        }

        if (whereConditionMap.containsKey("VISIT_TRANS_DETAILS_RECON")) {//Added by kannan RECONCILIATION 22-12-2015
            panDenomination.setPreferredSize(new java.awt.Dimension(950, 175));
            tblDenomination.getColumnModel().getColumn(0).setPreferredWidth(70);
            tblDenomination.getColumnModel().getColumn(1).setPreferredWidth(70);
            tblDenomination.getColumnModel().getColumn(2).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(3).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(4).setPreferredWidth(120);
            tblDenomination.getColumnModel().getColumn(5).setPreferredWidth(90);
            tblDenomination.getColumnModel().getColumn(6).setPreferredWidth(80);
            tblDenomination.getColumnModel().getColumn(7).setPreferredWidth(80);
            tblDenomination.getColumnModel().getColumn(8).setPreferredWidth(225);
            tblDenomination.getColumnModel().getColumn(9).setPreferredWidth(80);
            tblDenomination.getColumnModel().getColumn(10).setPreferredWidth(120);
            tblDenomination.getColumnModel().getColumn(11).setPreferredWidth(120);
            tblDenomination.getColumnModel().getColumn(12).setPreferredWidth(65);
            tblDenomination.getColumnModel().getColumn(13).setPreferredWidth(120);
            tblDenomination.getColumnModel().getColumn(14).setPreferredWidth(120);
            lblTotalAmt.setVisible(false);
            lblTotalAmtValue.setVisible(false);
        }
    }

    private void setTotalAmt() {
        double totAmt = 0;
        double totAmtWithWave = 0;
        if (tblDenomination.getRowCount()>0) {
            for (int i=0; i<tblDenomination.getRowCount(); i++) {
                Object value = tblDenomination.getValueAt(i, 3);
                if (value!=null) {
                    try {
                        if(tblDenomination.getValueAt(i, 6) !=null){
                        Object isSubsidy = tblDenomination.getValueAt(i, 6);
                        if(!CommonUtil.convertObjToStr(isSubsidy).equals("LOAN_SUBSIDY") && !CommonUtil.convertObjToStr(isSubsidy).endsWith("WAIVEOFF")){
                            //System.out.println("#$#$# value from isSubsidy : "+CommonUtil.convertObjToStr(isSubsidy).toString());    
                        totAmt += CommonUtil.convertObjToDouble(value).doubleValue();                        
                        }
                        }else{
                             totAmt += CommonUtil.convertObjToDouble(value).doubleValue();
                        }
                     totAmtWithWave += CommonUtil.convertObjToDouble(value).doubleValue();  
                    } catch(Exception e) {
                        System.out.println("#$#$# value from TableDialogUI : "+value);
                    }
                }
            }
            //System.out.println("total with wave"+totAmtWithWave);
            lblTotalAmtValue.setText(String.valueOf(totAmtWithWave));
            //Added By Suresh
            setTotalAmtValue(String.valueOf(totAmt));
        }
    }
    
    private void initSetup(String mapName, HashMap whereConditionMap) {
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.MAP_NAME, mapName);
        
        if (whereConditionMap != null) {
            whereMap.put(CommonConstants.MAP_WHERE, whereConditionMap);
        }
        
        if (!ClientUtil.setTableModel(whereMap, tblDenomination)) {
            isShow = false;
        }
        /*HashMap map = new HashMap();
        map.put (new Integer("1"), java.awt.Color.yellow);
        TableColorRenderer renderer = new TableColorRenderer(map);
        tblDenomination.setDefaultRenderer(Object.class, renderer);*/
    }
    
    public void show() {
        if (isShow) {
            Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            pack();
            
            /* Center frame on the screen */
            Dimension frameSize = getSize();
            if (frameSize.height > screenSize.height)
                frameSize.height = screenSize.height;
            if (frameSize.width > screenSize.width)
                frameSize.width = screenSize.width;
            setLocation((screenSize.width - frameSize.width) / 2,
            (screenSize.height - frameSize.height) / 2);
            setModal(true);
            super.show();
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panDenomination = new com.see.truetransact.uicomponent.CPanel();
        scrDenomination = new com.see.truetransact.uicomponent.CScrollPane();
        tblDenomination = new com.see.truetransact.uicomponent.CTable();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnOk = new com.see.truetransact.uicomponent.CButton();
        lblTotalAmt = new com.see.truetransact.uicomponent.CLabel();
        lblTotalAmtValue = new com.see.truetransact.uicomponent.CLabel();

        getContentPane().setLayout(new java.awt.BorderLayout(5, 5));

        panDenomination.setPreferredSize(new java.awt.Dimension(400, 175));
        panDenomination.setLayout(new java.awt.GridBagLayout());

        scrDenomination.setFocusable(false);
        scrDenomination.setNextFocusableComponent(btnOk);
        scrDenomination.setPreferredSize(new java.awt.Dimension(550, 404));

        tblDenomination.setModel(new javax.swing.table.DefaultTableModel(
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
        tblDenomination.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDenominationMouseClicked(evt);
            }
        });
        scrDenomination.setViewportView(tblDenomination);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panDenomination.add(scrDenomination, gridBagConstraints);

        getContentPane().add(panDenomination, java.awt.BorderLayout.CENTER);

        panButton.setLayout(new java.awt.GridBagLayout());

        btnOk.setText("Close");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnOk, gridBagConstraints);

        lblTotalAmt.setText("Total Amount :");
        lblTotalAmt.setMaximumSize(new java.awt.Dimension(70, 18));
        lblTotalAmt.setMinimumSize(new java.awt.Dimension(70, 18));
        lblTotalAmt.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 18, 4, 4);
        panButton.add(lblTotalAmt, gridBagConstraints);

        lblTotalAmtValue.setMaximumSize(new java.awt.Dimension(70, 18));
        lblTotalAmtValue.setMinimumSize(new java.awt.Dimension(70, 18));
        lblTotalAmtValue.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(lblTotalAmtValue, gridBagConstraints);

        getContentPane().add(panButton, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void tblDenominationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDenominationMouseClicked
        // TODO add your handling code here:
        int rowSelected=tblDenomination.getSelectedRow();
        //        setEnhancedTable((com.see.truetransact.clientutil.EnhancedTableModel)tblDenomination.get.getModel());
       if(cashUI !=null){
        HashMap cashTrns=new HashMap();
        cashTrns.put("ACCOUNT NO",tblDenomination.getValueAt(rowSelected,1));
        cashTrns.put("TRANS_ID",tblDenomination.getValueAt(rowSelected,0));
        cashTrns.put("SELECTED_ROW",new Integer(rowSelected));
        cashTrns.put("TRANS_DT",tblDenomination.getValueAt(rowSelected,7));
        
        //        ArrayList cashTransList=new ArrayList();
        //        cashTransList=enhancedTable.getDataArrayList();
        //        if(cashTransList!=null && cashTransList.size()>0){
        //        HashMap cashTrns=(HashMap) cashTransList.get(rowSelected);
        //System.out.println("cashtrans@@@"+cashTrns);
        cashUI.fillData(cashTrns);
        cashUI.setButtonEnableDisable(true);
        //        }
       }
    }//GEN-LAST:event_tblDenominationMouseClicked
    
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // Add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnOkActionPerformed
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        this.dispose();
    }//GEN-LAST:event_exitForm
    
    /**
     * Getter for property enhancedTable.
     * @return Value of property enhancedTable.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getEnhancedTable() {
        return enhancedTable;
    }
    
    /**
     * Setter for property enhancedTable.
     * @param enhancedTable New value of property enhancedTable.
     */
    public void setEnhancedTable(com.see.truetransact.clientutil.EnhancedTableModel enhancedTable) {
        this.enhancedTable = enhancedTable;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmtValue;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panDenomination;
    private com.see.truetransact.uicomponent.CScrollPane scrDenomination;
    private com.see.truetransact.uicomponent.CTable tblDenomination;
    // End of variables declaration//GEN-END:variables
    
}
