/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TransactionDetailsUI.java
 *
 * Created on April 13, 2011, 10:10 PM
 */
package com.see.truetransact.ui.common.customer;

import java.util.Observer;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Toolkit;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.clientproxy.ProxyParameters;

/**
 *
 * @author Suresh
 */
public class TransactionDetailsUI extends com.see.truetransact.uicomponent.CDialog implements Observer {

//    final CheckCustomerIdRB resourceBundle = new CheckCustomerIdRB();
    private CTable _tblData;
    private HashMap dataHash;
    private ArrayList data;
    private int dataSize;
    private ArrayList _heading;
    private boolean _isAvailable = true;
    Date currDt = null;
    public String branchID;
    public String prodType;
    public String acNum = null;
    public TransactionDetailsUI() {
        initComponents();
        initForm();
        currDt = ClientUtil.getCurrentDate();
    }

    /**
     * Account Number Constructor
     */
    public TransactionDetailsUI(String actNum, String productType) {
        acNum = actNum;
        currDt = ClientUtil.getCurrentDate();
        prodType = productType;
        initComponents();
        setMaxLengths();
        addToTableUsingActNo();
        branchID = TrueTransactMain.BRANCH_ID;
        setupScreen();
        txtCustId.setEnabled(false);
        currDt = ClientUtil.getCurrentDate();
//        lblMemName.setEnabled(false);        
    }

    /**
     * Method which is used to initialize the form TokenConfig
     */
    private void initForm() {
        setMaxLengths();
        setFieldNames();
        internationalize();
        currDt = ClientUtil.getCurrentDate();
    }

    private void setupScreen() {
        setModal(true);
        setTitle("TransactionDetails " + "[" + branchID + "]");
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //system.out.println("@$#@$#@# screenSize : "+screenSize); 
        setSize(650, 450);
        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    private void addToTableUsingActNo() {
        HashMap map = new HashMap();
        HashMap custMap = new HashMap();
        map.put("ACT_NUM", acNum);
        List custIdList = ClientUtil.executeQuery("getCustIdfromMembershipLiability", map);
        //system.out.println("#$@#@$@#@# custIdList: "+custIdList);
        if (custIdList != null && custIdList.size() > 0) {
            custMap = (HashMap) custIdList.get(0);
            System.out.println("CUSTOMER MAP"+custMap);
//            txtCustId.setText(CommonUtil.convertObjToStr(custMap.get("CUST_ID")));
            txtCustId.setText(acNum);
            lblMemName.setText(CommonUtil.convertObjToStr(custMap.get("CUST_NAME")));
        }
        
        if(prodType.equals("AB")){
            HashMap checkMap = new HashMap();
            checkMap.put("ACT_NUM",acNum);
            List checkList = ClientUtil.executeQuery("getOtherBankActReferenceNo", checkMap);
            if(checkList != null && checkList.size() > 0){
                checkMap = (HashMap)checkList.get(0);
                if(checkMap.containsKey("ACT_REF_NO") && checkMap.get("ACT_REF_NO") != null){
                    txtCustId.setText(CommonUtil.convertObjToStr(checkMap.get("ACT_REF_NO")));
                }
            }            
        }
        HashMap clearBalMap = (HashMap)ClientUtil.executeQuery("getBalance"+prodType, map).get(0);
        txtClearBalance.setText(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(clearBalMap.get("CLEAR_BALANCE")).doubleValue()));
        LinkedHashMap where = new LinkedHashMap();
        LinkedHashMap viewMap = new LinkedHashMap();
        where.put("ACT_NUM", acNum);
//        where.put("prod_type",prodType);
        if (prodType.equals("OA") || prodType.equals("AD")) {
            where.put("TABLE", "PASS_BOOK");
        } else if (prodType.equals("TL")) {
            where.put("TABLE", "LOAN_TRANS_DETAILS");
        } else if (prodType.equals("TD")) {
            where.put("TABLE", "DAILY_DEPOSIT_TRANS");
        } else if (prodType.equals("AB")) {
            where.put("TABLE", "JOIN_ALL_TRANS");
        } else if (prodType.equals("SA")) {
            where.put("TABLE", "JOIN_ALL_TRANS_SA");
        } else {
            return;
        }
        viewMap.put(CommonConstants.MAP_NAME, "getSelectTransactionDetails");
        viewMap.put(CommonConstants.MAP_WHERE, where);
        try {
            populateData(viewMap, tblTransTable);
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
        viewMap = null;
        where = null;
    }

    public ArrayList populateData(HashMap whereMap, CTable tblData) {
        _tblData = tblData;
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        }
        dataHash = ClientUtil.executeTableQuery(whereMap);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        //system.out.println("### Data : "+data);
        populateTable();
        whereMap = null;
        return _heading;
    }

    public void populateTable() {
        boolean dataExist;
        if (_heading != null) {
            _isAvailable = true;
            dataExist = true;
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(_heading);
            tableModel.setData(data);
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
          //  _tblData.setAutoResizeMode(0);
          //  _tblData.doLayout();
            _tblData.setModel(tableSorter);
           // _tblData.revalidate();
            //_tblData.getColumnModel().getColumn(0).setPreferredWidth(80);
           // _tblData.getColumnModel().getColumn(1).setPreferredWidth(80);
           // _tblData.getColumnModel().getColumn(2).setPreferredWidth(100);
           // _tblData.getColumnModel().getColumn(3).setPreferredWidth(80);
           // _tblData.getColumnModel().getColumn(4).setPreferredWidth(80);
           // _tblData.revalidate();
        }
    }

    /**
     * Used to set Maximum possible lenghts for TextFields
     */
    private void setMaxLengths() {
    }

    /* Auto Generated Method - setFieldNames()
     This method assigns name for all the components.
     Other functions are working based on this name. */
    private void setFieldNames() {
        panTransDetails.setName("panMemberShipFacility");
    }
    /* Auto Generated Method - internationalize()
     This method used to assign display texts from
     the Resource Bundle File. */

    private void internationalize() {
    }

    public void update(java.util.Observable o, Object arg) {
    }
    /* Auto Generated Method - updateOBFields()
     This method called by Save option of UI.
     It updates the OB with UI data.*/

    public void updateOBFields() {
    }

    /* Auto Generated Method - setMandatoryHashMap()
 
     ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
     This method list out all the Input Fields available in the UI.
     It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
    }

    /* Auto Generated Method - getMandatoryHashMap()
     Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panTransDetails = new com.see.truetransact.uicomponent.CPanel();
        panTransDetailTable = new com.see.truetransact.uicomponent.CPanel();
        srpMemberTransCTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblTransTable = new com.see.truetransact.uicomponent.CTable();
        panMemDetails = new com.see.truetransact.uicomponent.CPanel();
        lblCustId = new com.see.truetransact.uicomponent.CLabel();
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        txtCustId = new com.see.truetransact.uicomponent.CTextField();
        lblMemName = new com.see.truetransact.uicomponent.CLabel();
        lblClearBalance = new com.see.truetransact.uicomponent.CLabel();
        txtClearBalance = new com.see.truetransact.uicomponent.CLabel();
        panReportTrans = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnPrint = new com.see.truetransact.uicomponent.CButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        panTransDetails.setMaximumSize(new java.awt.Dimension(825, 575));
        panTransDetails.setMinimumSize(new java.awt.Dimension(600, 200));
        panTransDetails.setPreferredSize(new java.awt.Dimension(600, 200));
        panTransDetails.setLayout(new java.awt.GridBagLayout());

        panTransDetailTable.setMinimumSize(new java.awt.Dimension(350, 200));
        panTransDetailTable.setPreferredSize(new java.awt.Dimension(350, 200));
        panTransDetailTable.setLayout(new java.awt.GridBagLayout());

        srpMemberTransCTable.setMinimumSize(new java.awt.Dimension(600, 175));
        srpMemberTransCTable.setPreferredSize(new java.awt.Dimension(600, 175));

        tblTransTable.setBackground(new java.awt.Color(212, 208, 200));
        tblTransTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Trans ID", "Trans Date", "Particulars", "Amount", "Trans Type"
            }
        ));
        tblTransTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblTransTable.setDragEnabled(true);
        tblTransTable.setMaximumSize(new java.awt.Dimension(500, 400));
        tblTransTable.setMinimumSize(new java.awt.Dimension(60, 64));
        tblTransTable.setPreferredScrollableViewportSize(new java.awt.Dimension(100, 500));
        tblTransTable.setPreferredSize(new java.awt.Dimension(600, 1000));
        srpMemberTransCTable.setViewportView(tblTransTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panTransDetailTable.add(srpMemberTransCTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        panTransDetails.add(panTransDetailTable, gridBagConstraints);

        panMemDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Details"));
        panMemDetails.setMaximumSize(new java.awt.Dimension(265, 75));
        panMemDetails.setMinimumSize(new java.awt.Dimension(400, 90));
        panMemDetails.setPreferredSize(new java.awt.Dimension(300, 90));
        panMemDetails.setLayout(new java.awt.GridBagLayout());

        lblCustId.setText("Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        panMemDetails.add(lblCustId, gridBagConstraints);

        lblCustName.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 4);
        panMemDetails.add(lblCustName, gridBagConstraints);

        txtCustId.setBackground(new java.awt.Color(212, 208, 200));
        txtCustId.setForeground(new java.awt.Color(0, 51, 204));
        txtCustId.setMaxLength(16);
        txtCustId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        panMemDetails.add(txtCustId, gridBagConstraints);

        lblMemName.setForeground(new java.awt.Color(0, 51, 204));
        lblMemName.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        lblMemName.setMaximumSize(new java.awt.Dimension(1000, 21));
        lblMemName.setMinimumSize(new java.awt.Dimension(200, 21));
        lblMemName.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemDetails.add(lblMemName, gridBagConstraints);

        lblClearBalance.setText("Clear Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panMemDetails.add(lblClearBalance, gridBagConstraints);

        txtClearBalance.setForeground(new java.awt.Color(0, 51, 204));
        txtClearBalance.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        txtClearBalance.setMaximumSize(new java.awt.Dimension(1000, 21));
        txtClearBalance.setMinimumSize(new java.awt.Dimension(200, 21));
        txtClearBalance.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemDetails.add(txtClearBalance, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 17, 10);
        panTransDetails.add(panMemDetails, gridBagConstraints);

        panReportTrans.setMaximumSize(new java.awt.Dimension(823, 40));
        panReportTrans.setMinimumSize(new java.awt.Dimension(500, 70));
        panReportTrans.setPreferredSize(new java.awt.Dimension(823, 40));
        panReportTrans.setLayout(new java.awt.GridBagLayout());

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 21, 0, 0);
        panReportTrans.add(btnClose, gridBagConstraints);

        btnPrint.setForeground(new java.awt.Color(255, 0, 51));
        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setText("Print");
        btnPrint.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReportTrans.add(btnPrint, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 42, 0, 42);
        panTransDetails.add(panReportTrans, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panTransDetails, gridBagConstraints);
        panTransDetails.getAccessibleContext().setAccessibleName("MembershipFacifility");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        txtCustId.setText("");
        lblMemName.setText("");
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

                                    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
                                                                                                                                        }//GEN-LAST:event_formWindowClosed

                                                            private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
                                                                                                                                                                                                                                                                                            }//GEN-LAST:event_formWindowClosing

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
                                                    }//GEN-LAST:event_exitForm

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        //Added By Suresh             
        if (acNum != null && !acNum.equalsIgnoreCase("")) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            if (prodType.equals("TD") && acNum.lastIndexOf('_') != -1) {
                acNum = acNum.substring(0, acNum.lastIndexOf("_"));
            }
            paramMap.put("AccountNo", acNum);
            if(prodType.equals("AB")){ // Added by nithya for KD-1916
               paramMap.put("InvestNo", txtCustId.getText());
            }            
            paramMap.put("FromDt", currDt.clone());
            paramMap.put("ToDt", currDt.clone());
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            ttIntgration.setParam(paramMap);
            //Changed BY Suresh
            if (prodType.equals("TD")) {
                HashMap prodMap = new HashMap();
//                if (acNum.lastIndexOf('_') != -1) {
//                    acNum = acNum.substring(0, acNum.lastIndexOf("_"));
//                }
                prodMap.put("ACT_NUM", acNum);
                List lst = ClientUtil.executeQuery("getBehavesLikeForDepositNo", prodMap);
                if (lst != null && lst.size() > 0) {
                    prodMap = (HashMap) lst.get(0);
                    if (prodMap.get("BEHAVES_LIKE").equals("DAILY")) {
                        ttIntgration.integration("DailyLedger");
                    } else if (prodMap.get("BEHAVES_LIKE").equals("RECURRING")) { //Changed By Suresh
                        ttIntgration.integration("Rdledger");
                    } else if (prodMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) { //Changed By Suresh
                        ttIntgration.integration("CumulativeDepositLedger");
                    }else {
                        ttIntgration.integration("TermDepositLedger");
                    }
                }
            } else if (prodType.equals("TL")) {
                //Added BY Suresh
                    HashMap prodMap = new HashMap();
                    prodMap.put("ACT_NUM", acNum);
                    List lst = ClientUtil.executeQuery("getBehavesLikeTLAD", prodMap);
                    if (lst != null && lst.size() > 0) {
                        prodMap = (HashMap) lst.get(0);
                        if (prodMap.get("BEHAVES_LIKE").equals("LOANS_AGAINST_DEPOSITS")) {
                            ttIntgration.integration("DLLedger");
                        } else if (prodMap.get("BEHAVES_LIKE").equals("OD")) {
                            ttIntgration.integration("ADLedger");
                        } else if (prodMap.get("BEHAVES_LIKE").equals("SI_BEARING") && prodMap.get("AUTHORIZE_REMARK").equals("GOLD_LOAN")) {
                            ttIntgration.integration("GL_Ledger");
                        } else if (prodMap.get("BEHAVES_LIKE").equals("SI_BEARING") && prodMap.get("AUTHORIZE_REMARK").equals("OTHER_LOAN")) {
                            ttIntgration.integration("TLLedger");
                        } else if (prodMap.get("BEHAVES_LIKE").equals("SI_BEARING") && prodMap.get("AUTHORIZE_REMARK").equals("DAILY_LOAN")) {
                            ttIntgration.integration("TLLedger");
                        }
                    }
            }else if (prodType.equals("OA")) {
                ttIntgration.integration("OALedger");
            }else if (prodType.equals("SA")) {
                ttIntgration.integration("Suspense_Ledger");
            }else if (prodType.equals("AD")) {
                ttIntgration.integration("ADLedger");
            }else if (prodType.equals("AB")) {
                ttIntgration.integration("OBAc_Ledger");
            }
        } else {
            ClientUtil.displayAlert("Account No Should Not Be Empty!!! ");
        }
    }//GEN-LAST:event_btnPrintActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //        new CheckCustomerIdUI().show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CLabel lblClearBalance;
    private com.see.truetransact.uicomponent.CLabel lblCustId;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblMemName;
    private com.see.truetransact.uicomponent.CPanel panMemDetails;
    private com.see.truetransact.uicomponent.CPanel panReportTrans;
    private com.see.truetransact.uicomponent.CPanel panTransDetailTable;
    private com.see.truetransact.uicomponent.CPanel panTransDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpMemberTransCTable;
    private com.see.truetransact.uicomponent.CTable tblTransTable;
    private com.see.truetransact.uicomponent.CLabel txtClearBalance;
    private com.see.truetransact.uicomponent.CTextField txtCustId;
    // End of variables declaration//GEN-END:variables
}
