/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * addBalanceDataUI.java
 *
 * Created on May 26, 2015, 10:10 AM
 */
package com.see.truetransact.ui.supporting.inventory;


import com.see.truetransact.ui.common.viewall.*;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.Observer;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;

import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.transferobject.supporting.balanceupdate.BalanceUpdateTO;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.sun.tools.javac.v8.util.Convert;

/**
 *
 * @author  Sreekrishnan
 */
public class addBalanceDataUI extends com.see.truetransact.uicomponent.CDialog implements Observer {

    //    final CheckCustomerIdRB resourceBundle = new CheckCustomerIdRB();
    private CTable _tblData;
    private HashMap dataHash;
    private ArrayList data;
    private int dataSize;
    private ArrayList _heading;
    private boolean _isAvailable = true;
    private ArrayList termLoanData = new ArrayList();
    Date currDt = null;
    public String branchID;
    private double displayBalance = 0;
    private double totalBalance = 0;
    private double recieptAmount = 0;
    private double paymentAmount = 0;
    private boolean showDueTableFinal = false;
    private boolean hasPenal = false;
    private String transType = "";
    private HashMap balanceData = new HashMap();    

    public HashMap getBalanceData() {
        return balanceData;
    }

    public void setBalanceData(HashMap balanceData) {
        this.balanceData = balanceData;
    }

    public EnhancedTableModel getTbmBalanceData() {
        return tbmBalanceData;
    }

    public void setTbmBalanceData(EnhancedTableModel tbmBalanceData) {
        this.tbmBalanceData = tbmBalanceData;
    }
    private double penalAmount = 0.0;
    private boolean penalWaiveOffFinal = false;
    String Debit_Credit = "";
    double serviceTaxAmt = 0;
    private EnhancedTableModel tbmBalanceData;
    final ArrayList tableTitle = new ArrayList();
    private ComboBoxModel cbmBalType;
    String FinalPrcessType = "";
    double netLoss = 0.0,netProfit =0.0;

    public double getNetLoss() {
        return netLoss;
    }

    public void setNetLoss(double netLoss) {
        this.netLoss = netLoss;
    }

    public double getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(double netProfit) {
        this.netProfit = netProfit;
    }

    public ComboBoxModel getCbmBalType() {
        return cbmBalType;
    }

    public void setCbmBalType(ComboBoxModel cbmBalType) {
        this.cbmBalType = cbmBalType;
    }
    private ArrayList key;
    private ArrayList value;
//    public addBalanceDataUI() {
//        initComponents();
//        initForm();
//    }

    /** Account Number Constructor */
    public addBalanceDataUI(String FinalPrcessType) {
        initComponents();
        this.FinalPrcessType = FinalPrcessType;
        currDt = ClientUtil.getCurrentDate();
        branchID = TrueTransactMain.BRANCH_ID;
        txtAmt.setValidation(new CurrencyValidation(13, 2));
        setTableTile();
        tbmBalanceData = new EnhancedTableModel(null, tableTitle);
        setupScreen();
        if(FinalPrcessType!=null && !FinalPrcessType.equalsIgnoreCase("BALANCE SHEET")){
            fillCombo();
        }else{
            fillBalanceSheetCombo();
        }
    }

    public addBalanceDataUI(HashMap dataMap,String FinalPrcessType) {
        initComponents();
        this.FinalPrcessType = FinalPrcessType;
        currDt = ClientUtil.getCurrentDate();
        branchID = TrueTransactMain.BRANCH_ID;
        txtAmt.setValidation(new CurrencyValidation(13, 2));
        setTableTile();
        tbmBalanceData = new EnhancedTableModel(null, tableTitle);
        setupScreen();
        if(FinalPrcessType!=null && !FinalPrcessType.equalsIgnoreCase("BALANCE SHEET")){
            fillCombo();
        }else{
            fillBalanceSheetCombo();
        }
        setTable(dataMap);
        calcFinalProcessTotal();
    }

    private void setTableTile() {
        tableTitle.add("Remarks");
        tableTitle.add("BalanceType");
        tableTitle.add("Amount");
    }

    public void fillCombo() {
        key = new ArrayList();
        value = new ArrayList();
        value.add("Expendeture");
        value.add("Income");
        key.add("DEBIT");
        key.add("CREDIT");
        cbmBalType = new ComboBoxModel(key, value);
        cboBalType.setModel(getCbmBalType());

    }
    
    public void fillBalanceSheetCombo() {
        key = new ArrayList();
        value = new ArrayList();
        value.add("Liabilities");
        value.add("Asset");
        key.add("DEBIT");
        key.add("CREDIT");
        cbmBalType = new ComboBoxModel(key, value);
        cboBalType.setModel(getCbmBalType());

    }

        public void calcFinalProcessTotal() {
        double totalExp = 0, totalInc = 0, diff = 0;
        if (tblAddBalanceTable.getRowCount() > 0) {
            for (int i = 0; i < tblAddBalanceTable.getRowCount(); i++) {
                if(CommonUtil.convertObjToStr(tblAddBalanceTable.getValueAt(i, 1)).equals("Expendeture") || 
                        CommonUtil.convertObjToStr(tblAddBalanceTable.getValueAt(i, 1)).equals("Liabilities")){
                    totalExp = totalExp + CommonUtil.convertObjToDouble(tblAddBalanceTable.getValueAt(i, 2)).doubleValue();
                }else{
                    totalInc = totalInc + CommonUtil.convertObjToDouble(tblAddBalanceTable.getValueAt(i, 2)).doubleValue();
                }
                
            }
        }
        System.out.println("totalExp&$&$&$&"+totalExp);
        System.out.println("totalInc&$&$&$&"+totalInc);
        if (totalInc < totalExp) {
            diff = totalExp - totalInc;
            lblNetProfitValue.setText("0.0");
            lblNetLossValue.setText(CommonUtil.convertObjToStr(diff));
            totalInc = totalInc + diff;
            System.out.println("Diffeence-111-------------"+diff);
        } else {
            diff = totalInc - totalExp;
            lblNetProfitValue.setText(CommonUtil.convertObjToStr(diff));
            lblNetLossValue.setText("0.0");
            totalExp = totalExp + diff;
            System.out.println("Diffeence--222------------"+diff);
        }
         System.out.println("FinalPrcessType-------"+FinalPrcessType);
        if(FinalPrcessType.equals("BALANCE SHEET")){
            System.out.println("Diffeence--------------"+diff);
            lblNetProfitValue.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(diff)));
            lblNetLossValue.setText("0.0");
        }        
        this.netLoss = CommonUtil.convertObjToDouble(lblNetLossValue.getText());
        this.netProfit = CommonUtil.convertObjToDouble(lblNetProfitValue.getText());
    }
        
    public void setTable(HashMap dataMap) {
        System.out.println("dataMap^#^#^#^" + dataMap);
        ArrayList IncParRow = null;
        BalanceUpdateTO balanceUpdateTO = new BalanceUpdateTO();
        ArrayList addList = new ArrayList(dataMap.keySet());
        balanceData.putAll(dataMap);
        if (addList != null && addList.size() > 0) {
            for (int i = 0; i < addList.size(); i++) {
                balanceUpdateTO = (BalanceUpdateTO) dataMap.get(addList.get(i));
                IncParRow = new ArrayList();
                IncParRow.add(balanceUpdateTO.getAcctHeadDesc());
                IncParRow.add(cbmBalType.getDataForKey(balanceUpdateTO.getBalanceType()));
                IncParRow.add(balanceUpdateTO.getAmount());
                tbmBalanceData.insertRow(tbmBalanceData.getRowCount(), IncParRow);
            }
        }
        //setBalaceTo();    
        tblAddBalanceTable.setModel(getTbmBalanceData());
        IncParRow = null;
    }

    public void resetTable() {
        tbmBalanceData.setDataArrayList(null, tableTitle);
    }

    /** Method which is used to initialize the form TokenConfig */
    private void initForm() {
        setMaxLengths();
        setFieldNames();
        internationalize();
        currDt = ClientUtil.getCurrentDate();
    }

    private void setupScreen() {
        setModal(true);
        setTitle("Add Balance Data" + "[" + branchID + "]");
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("@$#@$#@# screenSize : " + screenSize);
        setSize(570, 480);
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

    public void show() {
        super.show();
    }

    public void calculateTot() {
        double totDue = 0.0;
        double totRecieved = 0.0;
        double totRecievable = 0.0;
        for (int i = 0; i < _tblData.getRowCount(); i++) {
            totDue = totDue + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 1).toString()).doubleValue();
            totRecieved = totRecieved + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 2).toString()).doubleValue();
            totRecievable = totRecievable + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 3).toString()).doubleValue();

            //  lblTotRecievedVal.setText(CurrencyValidation.formatCrore(String.valueOf(totRecieved)));
            //  lblTotRecievableVal.setText(CurrencyValidation.formatCrore(String.valueOf(totRecievable)));

        }
    }

    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths() {
    }

    /* Auto Generated Method - setFieldNames()
    This method assigns name for all the components.
    Other functions are working based on this name. */
    private void setFieldNames() {
        panMemberShipFacility.setName("panMemberShipFacility");
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panMemberShipFacility = new com.see.truetransact.uicomponent.CPanel();
        panMembershipTable = new com.see.truetransact.uicomponent.CPanel();
        srpAddBalanceTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblAddBalanceTable = new com.see.truetransact.uicomponent.CTable();
        panTotal = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnRemove = new com.see.truetransact.uicomponent.CButton();
        btnClose1 = new com.see.truetransact.uicomponent.CButton();
        lblNetLoss = new com.see.truetransact.uicomponent.CLabel();
        lblNetLossValue = new com.see.truetransact.uicomponent.CLabel();
        lblNetProfitValue = new com.see.truetransact.uicomponent.CLabel();
        lblNetProfit = new com.see.truetransact.uicomponent.CLabel();
        panTotal1 = new com.see.truetransact.uicomponent.CPanel();
        lblTotalBalance = new com.see.truetransact.uicomponent.CLabel();
        lblPaymentBal = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        txtAmt = new com.see.truetransact.uicomponent.CTextField();
        lblPaymentBal1 = new com.see.truetransact.uicomponent.CLabel();
        btnAdd = new com.see.truetransact.uicomponent.CButton();
        cboBalType = new com.see.truetransact.uicomponent.CComboBox();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        panMemberShipFacility.setToolTipText("");
        panMemberShipFacility.setMaximumSize(new java.awt.Dimension(400, 200));
        panMemberShipFacility.setMinimumSize(new java.awt.Dimension(400, 200));
        panMemberShipFacility.setPreferredSize(new java.awt.Dimension(400, 200));
        panMemberShipFacility.setLayout(new java.awt.GridBagLayout());

        panMembershipTable.setMinimumSize(new java.awt.Dimension(300, 300));
        panMembershipTable.setPreferredSize(new java.awt.Dimension(300, 300));
        panMembershipTable.setLayout(new java.awt.GridBagLayout());

        srpAddBalanceTable.setAutoscrolls(true);
        srpAddBalanceTable.setMinimumSize(new java.awt.Dimension(450, 200));
        srpAddBalanceTable.setPreferredSize(new java.awt.Dimension(450, 5000));

        tblAddBalanceTable.setBackground(new java.awt.Color(212, 208, 200));
        tblAddBalanceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Remarks", "Expendeture", "Income"
            }
        ));
        tblAddBalanceTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblAddBalanceTable.setDragEnabled(true);
        tblAddBalanceTable.setInheritsPopupMenu(true);
        tblAddBalanceTable.setMaximumSize(new java.awt.Dimension(100, 100));
        tblAddBalanceTable.setMinimumSize(new java.awt.Dimension(450, 200));
        tblAddBalanceTable.setPreferredScrollableViewportSize(new java.awt.Dimension(50000, 5000));
        tblAddBalanceTable.setPreferredSize(new java.awt.Dimension(450, 3000));
        tblAddBalanceTable.setRequestFocusEnabled(false);
        srpAddBalanceTable.setViewportView(tblAddBalanceTable);

        panMembershipTable.add(srpAddBalanceTable, new java.awt.GridBagConstraints());

        panTotal.setMaximumSize(new java.awt.Dimension(450, 50));
        panTotal.setMinimumSize(new java.awt.Dimension(450, 100));
        panTotal.setPreferredSize(new java.awt.Dimension(450, 100));
        panTotal.setLayout(new java.awt.GridBagLayout());

        btnClose.setForeground(new java.awt.Color(204, 0, 0));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panTotal.add(btnClose, gridBagConstraints);

        btnRemove.setForeground(new java.awt.Color(204, 0, 0));
        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnRemove.setMnemonic('I');
        btnRemove.setText("Remove");
        btnRemove.setMaximumSize(new java.awt.Dimension(100, 26));
        btnRemove.setMinimumSize(new java.awt.Dimension(100, 26));
        btnRemove.setPreferredSize(new java.awt.Dimension(100, 26));
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal.add(btnRemove, gridBagConstraints);

        btnClose1.setForeground(new java.awt.Color(204, 0, 0));
        btnClose1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClose1.setText("Clear");
        btnClose1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClose1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panTotal.add(btnClose1, gridBagConstraints);

        lblNetLoss.setText("Net Loss");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 7);
        panTotal.add(lblNetLoss, gridBagConstraints);

        lblNetLossValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panTotal.add(lblNetLossValue, gridBagConstraints);

        lblNetProfitValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panTotal.add(lblNetProfitValue, gridBagConstraints);

        lblNetProfit.setText("Net Profit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        panTotal.add(lblNetProfit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 42, 0, 42);
        panMembershipTable.add(panTotal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        panMemberShipFacility.add(panMembershipTable, gridBagConstraints);

        panTotal1.setMaximumSize(new java.awt.Dimension(400, 95));
        panTotal1.setMinimumSize(new java.awt.Dimension(400, 100));
        panTotal1.setPreferredSize(new java.awt.Dimension(400, 100));
        panTotal1.setLayout(new java.awt.GridBagLayout());

        lblTotalBalance.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(lblTotalBalance, gridBagConstraints);

        lblPaymentBal.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(lblPaymentBal, gridBagConstraints);

        txtRemarks.setAllowAll(true);
        txtRemarks.setPreferredSize(new java.awt.Dimension(300, 21));
        txtRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRemarksActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        panTotal1.add(txtRemarks, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panTotal1.add(txtAmt, gridBagConstraints);

        lblPaymentBal1.setText("Balance Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(lblPaymentBal1, gridBagConstraints);

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/Down_Arrow.gif"))); // NOI18N
        btnAdd.setMnemonic('I');
        btnAdd.setText("Add");
        btnAdd.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        btnAdd.setMaximumSize(new java.awt.Dimension(100, 26));
        btnAdd.setMinimumSize(new java.awt.Dimension(100, 26));
        btnAdd.setPreferredSize(new java.awt.Dimension(100, 26));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 43, 4, 30);
        panTotal1.add(btnAdd, gridBagConstraints);
        panTotal1.add(cboBalType, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(20, 38, 3, 41);
        panMemberShipFacility.add(panTotal1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panMemberShipFacility, gridBagConstraints);
        panMemberShipFacility.getAccessibleContext().setAccessibleName("MembershipFacifility");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        tblAddBalanceTable = new CTable();
        ClientUtil.clearAll(this);
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

                                    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
                                                                                                                                                                                }//GEN-LAST:event_formWindowClosed

                                                            private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
                                                                                                                                                                                                                                                                                                                                                            }//GEN-LAST:event_formWindowClosing

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
                                                            }//GEN-LAST:event_exitForm

private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
    if(tblAddBalanceTable.getRowCount()>0){
        if(tblAddBalanceTable.getSelectedRow()>=0){
            String remark = CommonUtil.convertObjToStr(tblAddBalanceTable.getValueAt(tblAddBalanceTable.getSelectedRow(), 0));
            BalanceUpdateTO balanceUpdateTO = (BalanceUpdateTO) balanceData.get(remark);
            balanceData.remove(remark);
            resetTable();
            System.out.println("balanceData^#^#^#deleteTableData" + balanceData);
        }else{
            ClientUtil.showAlertWindow("Please select Any Row to Remove!!!");
            return;
        }
    }else{
        ClientUtil.showAlertWindow("Please Add data First!!!");
        return;
    }
    try {
        setTable(balanceData);
        calcFinalProcessTotal();
    } catch (Exception e) {
        e.printStackTrace();
    }

}//GEN-LAST:event_btnRemoveActionPerformed

private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
// TODO add your handling code here:
    StringBuffer validationMsg = new StringBuffer();
    if(!(txtAmt.getText()!=null && txtAmt.getText().length()>0 && CommonUtil.convertObjToDouble(txtAmt.getText())>0)){
        validationMsg.append("Please Enter the Amount and Should't be Zero!!!");
    }
    if(!(txtRemarks.getText()!=null && txtRemarks.getText().length()>0)){
        validationMsg.append("Please Enter the Remarks!!!");
    }
    if(!(cboBalType.getSelectedItem()!=null && !cboBalType.getSelectedItem().equals(""))){
        validationMsg.append("Please Select the Balance Type!!!");   
    }    
    if(validationMsg!=null && validationMsg.length()>0){    
        ClientUtil.showAlertWindow(validationMsg.toString());
        return;
    }else{
        for (int i = 0; i < tblAddBalanceTable.getRowCount(); i++) {
            if(CommonUtil.convertObjToStr(tblAddBalanceTable.getValueAt(i, 0)).equals(CommonUtil.convertObjToStr(txtRemarks.getText()))) {
                ClientUtil.showAlertWindow("Data Already Present In Grid!!");
                return;
            }
        }
        ArrayList IncParRow = new ArrayList();
        IncParRow.add(txtRemarks.getText());
        IncParRow.add(CommonUtil.convertObjToStr(cboBalType.getSelectedItem()));
        IncParRow.add(txtAmt.getText());
        tbmBalanceData.insertRow(tbmBalanceData.getRowCount(), IncParRow);
        setBalaceTo();
        tblAddBalanceTable.setModel(getTbmBalanceData());
        calcFinalProcessTotal();
        IncParRow = null;
        resetFrom();
    }
}//GEN-LAST:event_btnAddActionPerformed

private void btnClose1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose1ActionPerformed
// TODO add your handling code here:
    resetFrom();
    resetTable();
    balanceData = new HashMap();
}//GEN-LAST:event_btnClose1ActionPerformed

private void txtRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRemarksActionPerformed
// TODO add your handling code here:    
}//GEN-LAST:event_txtRemarksActionPerformed

    public void resetFrom() {
        txtAmt.setText("");
        txtRemarks.setText("");
        cboBalType.setSelectedItem("");
    }
    public void setBalaceTo() {
        BalanceUpdateTO balanceUpdateTO = new BalanceUpdateTO();
        balanceUpdateTO.setAcctHeadDesc(txtRemarks.getText());
        balanceUpdateTO.setBalanceType(CommonUtil.convertObjToStr(cbmBalType.getKeyForSelected()));
        balanceUpdateTO.setAmount(CommonUtil.convertObjToDouble(txtAmt.getText()));
        balanceData.put(txtRemarks.getText(), balanceUpdateTO);        
    }

    public ArrayList getTableData() {
        ArrayList singleList = new ArrayList();
        ArrayList totList = new ArrayList();
        BalanceUpdateTO balanceUpdateTO = new BalanceUpdateTO();
        int count = tblAddBalanceTable.getModel().getRowCount();
        int columnCount = tblAddBalanceTable.getModel().getColumnCount();
        for (int i = 0; i < count; i++) {
            singleList = new ArrayList();
            for (int j = 0; j < columnCount; j++) {
                balanceUpdateTO.setAcctHeadDesc(CommonUtil.convertObjToStr(tblAddBalanceTable.getValueAt(i, j)));
                balanceUpdateTO.setBalanceType(CommonUtil.convertObjToStr(tblAddBalanceTable.getValueAt(i, j)));
                balanceUpdateTO.setAcctHeadDesc(CommonUtil.convertObjToStr(tblAddBalanceTable.getValueAt(i, j)));
                singleList.add(balanceUpdateTO);
            }
            totList.add(singleList);
        }
        return totList;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //        new CheckCustomerIdUI().show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAdd;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnClose1;
    private com.see.truetransact.uicomponent.CButton btnRemove;
    private com.see.truetransact.uicomponent.CComboBox cboBalType;
    private com.see.truetransact.uicomponent.CLabel lblNetLoss;
    private com.see.truetransact.uicomponent.CLabel lblNetLossValue;
    private com.see.truetransact.uicomponent.CLabel lblNetProfit;
    private com.see.truetransact.uicomponent.CLabel lblNetProfitValue;
    private com.see.truetransact.uicomponent.CLabel lblPaymentBal;
    private com.see.truetransact.uicomponent.CLabel lblPaymentBal1;
    private com.see.truetransact.uicomponent.CLabel lblTotalBalance;
    private com.see.truetransact.uicomponent.CPanel panMemberShipFacility;
    private com.see.truetransact.uicomponent.CPanel panMembershipTable;
    private com.see.truetransact.uicomponent.CPanel panTotal;
    private com.see.truetransact.uicomponent.CPanel panTotal1;
    private com.see.truetransact.uicomponent.CScrollPane srpAddBalanceTable;
    private com.see.truetransact.uicomponent.CTable tblAddBalanceTable;
    private com.see.truetransact.uicomponent.CTextField txtAmt;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    // End of variables declaration//GEN-END:variables
}
