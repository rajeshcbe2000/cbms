/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ArrearOpeningEntryUI.java
 *
 * Created on September 28, 2011, 3:03 PM
 */
package com.see.truetransact.ui.payroll.Arrear;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.transaction.cash.*;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uicomponent.CButtonGroup;
import java.util.HashMap;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.List;
import com.see.truetransact.clientutil.ClientConstants;
import java.util.*;
import java.text.*;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.DateUtil;
import java.util.Date;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.Enumeration;
import java.text.SimpleDateFormat;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uicomponent.CInternalFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.ui.deposit.CommonMethods;
import com.see.truetransact.ui.termloan.dailyLoanTrans.DailyLoanTransUI.SimpleTableModel;
import com.see.truetransact.uicomponent.CCheckBox;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CPopupMenu;
import com.see.truetransact.uicomponent.CTable;
import java.awt.Checkbox;
import java.awt.event.MouseListener;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.print.DocFlavor.STRING;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreeNode;
//import sun.swing.table.DefaultTableCellHeaderRenderer;
//import sun.swing.table.DefaultTableCellHeaderRenderer;

/**
 *
 * @author  user
 */
public class ArrearOpeningEntryUI extends CInternalFrame implements Observer {

    private boolean DEBUG = false;
    DefaultTableModel model = null;
    DefaultTreeModel root;
    DefaultTreeModel child;
    Date currDt = null;
    private TableModelListener tableModelListener;
    public String transMode = "";
    public String trans_pid = "";
    public String clsbal = "";
    private int actionType;
    String link_batch_id = "";
    private boolean selectMode = false;
    public String Opamount = "",
            amount1 = "", countVal = "", amount = "", sc_Name = "", cname = "", ac_No = "", ac_HD_No = null, prod_Type = "", prod_Id = null, trans_id = "", trans_type = "",
            posted_by = "", autho_by = "", particulars = "", panNo = "", token_No = "", instr_Type = "",
            instr_No = "", instr_date = "", clear_Bal = "", screenName = "";
    DefaultMutableTreeNode selectedNode = null;
    private ArrearProcessingOB observable;
    public ArrayList employeeData = new ArrayList();
    private CashTransactionOB cashOB;
    //  CheckBoxEditor objCheckBoxNodeEditor = null;
    String amtGlobal = "";
    private HashMap _authorizeMap;
    private String asAnWhenCustomer = new String();
    private ProxyFactory proxy = null;
    private HashMap operationMap;
    private ArrayList data = new ArrayList();
    private ArrayList _heading = new ArrayList();
    private double totVal = 0;
    TableDialogUI tableDialogUI = null;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    public static String debitProductKey = "";
    public static String debitProductIdKey = "";

    public void update(Observable observed, Object arg) {
    }

    public ArrearOpeningEntryUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setObservable();
        employeeDetails();
        addRadioButtons();        
    }

    private void addRadioButtons() {
        rdoBtnGroup = new CButtonGroup();
        rdoBtnGroup.add((rdoBasic));
        rdoBtnGroup.add((rdoDA));
        rdoBtnGroup.add((rdoPF));
        rdoBtnGroup.add((rdoHra));
    }

    private void removeRadioButtons() {
        rdoBtnGroup.remove((rdoBasic));
        rdoBtnGroup.remove((rdoDA));
        rdoBtnGroup.remove((rdoPF));
        rdoBtnGroup.remove((rdoHra));
    }

    private void setObservable() {
        try {
            observable = new ArrearProcessingOB();
            observable.addObserver(this);
            cashOB = new CashTransactionOB();
            cashOB.addObserver(this);
        } catch (Exception e) {
            System.out.println("Error in setObservable():" + e);
        }
    }

    public static String formatCrore(String str) {
        java.text.DecimalFormat numberFormat = new java.text.DecimalFormat();
        double currData = Double.parseDouble(str.replaceAll(",", ""));
        str = numberFormat.format(currData);
        String num = str.substring(0, str.lastIndexOf(".")).replaceAll(",", "");
        String dec = str.substring(str.lastIndexOf("."));
        String sign = "";
        if (num.substring(0, 1).equals("-")) {
            sign = num.substring(0, 1);
            num = num.substring(1, num.length());
        }
        char[] chrArr = num.toCharArray();
        StringBuffer fmtStrB = new StringBuffer();
        for (int i = chrArr.length - 1, j = 0, k = 0; i >= 0; i--) {
            if ((j == 3 && k == 3) || (j == 2 && k == 5) || (j == 2 && k == 7)) {
                fmtStrB.insert(0, ",");
                if (k == 7) {
                    k = 0;
                }
                j = 0;
            }
            j++;
            k++;

            fmtStrB.insert(0, chrArr[i]);
        }
        fmtStrB.append(dec);

        str = fmtStrB.toString();

        str = sign + str;

        if (str.equals(".00")) {
            str = "0";
        }

        return str;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoBtnGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        panCashTrans = new com.see.truetransact.uicomponent.CPanel();
        panTree = new com.see.truetransact.uicomponent.CPanel();
        srpEmployee = new com.see.truetransact.uicomponent.CScrollPane();
        tblEmployee = new com.see.truetransact.uicomponent.CTable();
        panParameters = new com.see.truetransact.uicomponent.CPanel();
        panButtons = new com.see.truetransact.uicomponent.CPanel();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        panDetails = new com.see.truetransact.uicomponent.CPanel();
        tdtFromDt = new com.see.truetransact.uicomponent.CDateField();
        tdtToDt = new com.see.truetransact.uicomponent.CDateField();
        rdoBasic = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDA = new com.see.truetransact.uicomponent.CRadioButton();
        lblFromDt = new com.see.truetransact.uicomponent.CLabel();
        lblToDt = new com.see.truetransact.uicomponent.CLabel();
        rdoPF = new com.see.truetransact.uicomponent.CRadioButton();
        lblAccount = new com.see.truetransact.uicomponent.CLabel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        txtOldDA = new com.see.truetransact.uicomponent.CTextField();
        txtNewDA = new com.see.truetransact.uicomponent.CTextField();
        rdoHra = new com.see.truetransact.uicomponent.CRadioButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Arrear Opening Entry");
        setMaximumSize(new java.awt.Dimension(900, 640));
        setMinimumSize(new java.awt.Dimension(700, 550));
        setPreferredSize(new java.awt.Dimension(700, 550));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panCashTrans.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panCashTrans.setMinimumSize(new java.awt.Dimension(350, 300));
        panCashTrans.setPreferredSize(new java.awt.Dimension(200, 400));
        panCashTrans.setLayout(new java.awt.GridBagLayout());

        panTree.setBorder(javax.swing.BorderFactory.createTitledBorder("Employee"));
        panTree.setMinimumSize(new java.awt.Dimension(250, 350));
        panTree.setPreferredSize(new java.awt.Dimension(380, 300));
        panTree.setLayout(new java.awt.GridBagLayout());

        srpEmployee.setMinimumSize(new java.awt.Dimension(200, 320));
        srpEmployee.setPreferredSize(new java.awt.Dimension(350, 200));

        tblEmployee.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Select", "Name", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmployee.setColumnSelectionAllowed(true);
        tblEmployee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmployeeMouseClicked(evt);
            }
        });
        srpEmployee.setViewportView(tblEmployee);
        tblEmployee.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        panTree.add(srpEmployee, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCashTrans.add(panTree, gridBagConstraints);

        panParameters.setBorder(javax.swing.BorderFactory.createTitledBorder("Arrear Details"));
        panParameters.setMinimumSize(new java.awt.Dimension(300, 350));
        panParameters.setPreferredSize(new java.awt.Dimension(350, 240));
        panParameters.setLayout(new java.awt.GridBagLayout());

        panButtons.setLayout(new java.awt.GridBagLayout());

        btnProcess.setText("Insert");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        panButtons.add(btnProcess, new java.awt.GridBagConstraints());

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panButtons.add(btnClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(19, 0, 0, 0);
        panParameters.add(panButtons, gridBagConstraints);

        panDetails.setMinimumSize(new java.awt.Dimension(250, 250));
        panDetails.setPreferredSize(new java.awt.Dimension(375, 170));
        panDetails.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panDetails.add(tdtFromDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        panDetails.add(tdtToDt, gridBagConstraints);

        rdoBasic.setText("Basic");
        rdoBasic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBasicActionPerformed(evt);
            }
        });
        panDetails.add(rdoBasic, new java.awt.GridBagConstraints());

        rdoDA.setText("DA");
        rdoDA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDAActionPerformed(evt);
            }
        });
        panDetails.add(rdoDA, new java.awt.GridBagConstraints());

        lblFromDt.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panDetails.add(lblFromDt, gridBagConstraints);

        lblToDt.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panDetails.add(lblToDt, gridBagConstraints);

        rdoPF.setText("PF");
        rdoPF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPFActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panDetails.add(rdoPF, gridBagConstraints);

        lblAccount.setForeground(new java.awt.Color(0, 0, 153));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        panDetails.add(lblAccount, gridBagConstraints);

        cLabel1.setText("Old Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        panDetails.add(cLabel1, gridBagConstraints);

        cLabel2.setText("New Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        panDetails.add(cLabel2, gridBagConstraints);

        txtOldDA.setAllowAll(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        panDetails.add(txtOldDA, gridBagConstraints);

        txtNewDA.setAllowAll(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        panDetails.add(txtNewDA, gridBagConstraints);

        rdoHra.setText("HRA");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panDetails.add(rdoHra, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        panParameters.add(panDetails, gridBagConstraints);

        panCashTrans.add(panParameters, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 350;
        gridBagConstraints.ipady = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(30, 140, 27, 178);
        getContentPane().add(panCashTrans, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public boolean checkNumber(String value) {
        //String amtRentIn=amountRentText.getText();
        boolean incorrect = true;
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
        // return
    }

    public String getDtPrintValue(String strDate) {
        try {
            //create SimpleDateFormat object with source string date format
            SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //"yyyy-MM-dd HH:mm:ss "
            //parse the string into Date object
            Date date = sdfSource.parse(strDate);
            //create SimpleDateFormat object with desired date format
            SimpleDateFormat sdfDestination = new SimpleDateFormat("MM/dd/yyyy");
            //parse the date into another format
            strDate = sdfDestination.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

private void tblEmployeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmployeeMouseClicked
    // TODO add your handling code here:
    boolean chk = ((Boolean) tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 0)).booleanValue();
    String strNodeUnselec = "";
    if (!chk) {
        String nodeVal = CommonUtil.convertObjToStr(tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 1));
        link_batch_id = CommonUtil.convertObjToStr(tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 1));
        if (employeeData.contains(nodeVal)) {
            employeeData.remove(nodeVal);
        }
        employeeData.add(nodeVal);
        tblEmployee.setValueAt(true, tblEmployee.getSelectedRow(), 0);
    } else {
        String nodeVal = CommonUtil.convertObjToStr(tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 1));
        strNodeUnselec = nodeVal;
        employeeData.remove(nodeVal);
        tblEmployee.setValueAt(false, tblEmployee.getSelectedRow(), 0);
    }
    if (employeeData != null && employeeData.size() == 0) {
    }
}//GEN-LAST:event_tblEmployeeMouseClicked

private void rdoBasicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBasicActionPerformed
// TODO add your handling code here:
    if (rdoBasic.isSelected() == true) {
        //ClientUtil.showMessageWindow("Select salary Day as From Date and To Date Day!!!");
    }
}//GEN-LAST:event_rdoBasicActionPerformed

private void rdoDAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDAActionPerformed
// TODO add your handling code here:   
    if (rdoDA.isSelected() == true) {
        //ClientUtil.showMessageWindow("Select salary Day as From Date and To Date Day!!!");
    }
}//GEN-LAST:event_rdoDAActionPerformed

private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
// TODO add your handling code here:
    //setModified(true);
    StringBuffer strBMandatory = new StringBuffer();
    boolean count = false;
    for (int i = 0; i < tblEmployee.getRowCount(); i++) {
        if ((Boolean) tblEmployee.getValueAt(i, 0)) {
            count = true;
        }
    }
    if (!count) {
        strBMandatory.append(" NO Rows Selected in Employee Table!!! ");
        strBMandatory.append("\n");
        count = false;
    }
    if (rdoBasic.isSelected() == false && rdoDA.isSelected() == false && rdoPF.isSelected() == false && rdoHra.isSelected()==false) {
        strBMandatory.append("Please select any Arrear Type!!!");
        strBMandatory.append("\n");
    } else if ((tdtFromDt.getDateValue().length() == 0) || (tdtFromDt.getDateValue() == null)) {
        strBMandatory.append("Please select From Date!!!");
        strBMandatory.append("\n");
    } else if ((tdtToDt.getDateValue().length() == 0) || (tdtToDt.getDateValue() == null)) {
        strBMandatory.append("Please select To Date!!!");
        strBMandatory.append("\n");
    } else if ((DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()).compareTo(DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue())) < 0)) {
        strBMandatory.append("To date should be after than From date!!!");
        strBMandatory.append("\n");
    } else if (rdoDA.isSelected() == true || rdoPF.isSelected() == true) {
        
    } else if (rdoPF.isSelected() == true) {
        
    }

    String message = strBMandatory.toString();
    if (message.trim().length() > 0) {
        CommonMethods.displayAlert(message);
        return;
    } else {
        updateOB();
        try {
            CommonUtil comm = new CommonUtil();
            final JDialog loading = comm.addProgressBar();
            SwingWorker worker = new SwingWorker() {

                @Override
                protected Void doInBackground() throws InterruptedException, TTException /** Execute some operation */
                {
                    if (rdoDA.isSelected()) {
                        processArreaOpening("DearNess Allowance");
                        //arrearDetails();
                    }else if (rdoBasic.isSelected()) {
                        processArreaOpening("BASICPAY");
                        //arrearDetails();
                    }else if (rdoPF.isSelected()) {
                        processArreaOpening("PF");
                        //arrearDetails();
                    }else{
                        processArreaOpening("HOUSE RENT ALLOWANCE");
                    }                   
                    return null;
                }

                protected void done() {
                    loading.dispose();
                    for (int i = 0; i < tblEmployee.getRowCount(); i++) {
                        tblEmployee.setValueAt(new Boolean(false), i, 0);
                    }
                    txtNewDA.setText("");
                    txtOldDA.setText("");
                }
            };
            worker.execute();
            loading.show();
            worker.get();
        } catch (Exception ex) {
            ex.printStackTrace();
            ClientUtil.showMessageWindow(ex.getMessage());
        }
    }
}//GEN-LAST:event_btnProcessActionPerformed

public static final int getMonthsDifference(Date date1, Date date2) {
    int m1 = date1.getYear() * 12 + date1.getMonth();
    int m2 = date2.getYear() * 12 + date2.getMonth();
    return m2 - m1 + 1;
}

private void processArreaOpening(String arrearType){
    try{
        if(arrearType!=null && arrearType.length()>0){
            HashMap openigMap = new HashMap();      
            int difInMonths = getMonthsDifference(observable.getProperDateFormat(tdtFromDt.getDateValue()),observable.getProperDateFormat(tdtToDt.getDateValue()));
            System.out.println("difInMonths%#%#%#%#"+difInMonths);
            if(tblEmployee.getRowCount()>0){
                for (int i = 0; i < tblEmployee.getRowCount(); i++) {
                    if ((Boolean) tblEmployee.getValueAt(i, 0)) {
                            openigMap.put("PAY_CODE",observable.getPayCodeArrear(arrearType));  
                            openigMap.put("PAY_DESC",observable.getPayDescription(CommonUtil.convertObjToStr(openigMap.get("PAY_CODE")))); 
                        for (int j = 0; j < difInMonths; j++) { 
                            openigMap.put("EMPLOYEEID",CommonUtil.convertObjToStr(tblEmployee.getValueAt(i, 1)));                              
                            openigMap.put("OLD_AMOUNT",CommonUtil.convertObjToDouble(txtOldDA.getText())); 
                            openigMap.put("NEW_AMOUNT",CommonUtil.convertObjToDouble(txtNewDA.getText()));  
                            openigMap.put("DIFFERENCE",CommonUtil.convertObjToDouble(txtNewDA.getText())-CommonUtil.convertObjToDouble(txtOldDA.getText()));
                            Calendar date = Calendar.getInstance();
                            date.setTime(observable.getProperDateFormat(tdtFromDt.getDateValue()));                            
                            date.add(Calendar.MONTH, j);
                            System.out.println("date@#$@#$@#$@#$"+observable.getProperDateFormat(date.getTime()));
                            openigMap.put("FROM_DT",observable.getProperDateFormat(date.getTime()));
                            openigMap.put("TO_DT",observable.getProperDateFormat(date.getTime()));
                            openigMap.put("BASEON",arrearType); 
                            openigMap.put("STATUS","CREATED");
                            ClientUtil.execute("insertOpeningArrearTO",openigMap);                        
                        }
                    }
                }
            }            
            
        }
    }catch(Exception e){
        e.printStackTrace();
    }
}


private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
// TODO add your handling code here:
    observable.resetData();
    rdoBasic.setSelected(false);
    rdoDA.setSelected(false);
    rdoPF.setSelected(false);
    removeRadioButtons();
    addRadioButtons();
    tdtFromDt.setDateValue("");
    tdtToDt.setDateValue("");    
    ClientUtil.clearAll(this);
    for (int i = 0; i < tblEmployee.getRowCount(); i++) {
        tblEmployee.setValueAt(new Boolean(false), i, 0);
    }
    lblAccount.setText("");
    employeeData.clear();
}//GEN-LAST:event_btnClearActionPerformed

private void rdoPFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPFActionPerformed
// TODO add your handling code here:    
    if (rdoPF.isSelected() == true) {
        //ClientUtil.showMessageWindow("Select salary Day as From Date and To Date Day!!!");
    }
}//GEN-LAST:event_rdoPFActionPerformed

    public void clearAll() {
        ClientUtil.enableDisable(panParameters, false);
        ClientUtil.enableDisable(panTree, false);
        ClientUtil.clearAll(panTree);
        ClientUtil.clearAll(panParameters);
    }

    public void clearTable() {
        observable.clearTable();
    }

    public void ViewArrearReport() {
        TTIntegration ttIntgration = null;
        HashMap paramMap = new HashMap();        
        paramMap.put("FromDate", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue())));
        paramMap.put("ToDate", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue())));
        paramMap.put("TransDate", currDt.clone());
        ttIntgration.setParam(paramMap);
        ttIntgration.integrationForPrint("arrearnew", true);
    }

    public void updateOB() {
        if (employeeData.size() > 0) {
            observable.setEmployeeData(employeeData);
        }
        if (rdoBasic.isSelected() == true) {
            observable.setArrearType("BasicPay");
        }
        if (rdoDA.isSelected() == true) {
            observable.setArrearType("DearNess Allowance");
        }
        if (rdoPF.isSelected() == true) {
            observable.setArrearType("PF");
        }
        if (!tdtFromDt.getDateValue().equals("")) {
            observable.setFromDate(DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
        }
        if (!tdtToDt.getDateValue().equals("")) {
            observable.setToDate(DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
        }

    }

    private void employeeDetails() {
        HashMap shareTypeMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("AUTHORIZESTATUS", CommonConstants.STATUS_AUTHORIZED);
        whereMap.put("TRANS_DT", currDt);
        whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        shareTypeMap.put(CommonConstants.MAP_WHERE, whereMap);
        shareTypeMap.put(CommonConstants.MAP_NAME, "getSelectEmployeeData");
        try {
            observable.populateData(shareTypeMap, tblEmployee);
            tblEmployee.setModel(observable.getTblReciept());
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
        tblEmployee.setEditingColumn(0);
        javax.swing.table.TableColumn col = tblEmployee.getColumn(tblEmployee.getColumnName(0));
        col.setMaxWidth(50);
        col.setPreferredWidth(50);
        col = tblEmployee.getColumn(tblEmployee.getColumnName(1));
        col.setMaxWidth(70);
        col.setPreferredWidth(70);
        col = tblEmployee.getColumn(tblEmployee.getColumnName(2));
        col.setMaxWidth(300);
        col.setPreferredWidth(300);
    }

    private void arrearDetails() {
        HashMap shareTypeMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("FROM_DT", observable.getFromDate());
        whereMap.put("TO_DT", observable.getToDate());
        String employee = "";
        for (int i = 0; i < observable.getEmployeeData().size(); i++) {
            if (employee.length() > 0) {
                employee+=",";
            }
            employee+="'" + observable.getEmployeeData().get(i) + "'";
        } 
        whereMap.put("EMPLOYEEID", employee);
        whereMap.put("BASED_ON", observable.getArrearType());
        whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        whereMap.put("TRANS_DT",currDt.clone());
        shareTypeMap.put(CommonConstants.MAP_WHERE, whereMap);
        shareTypeMap.put(CommonConstants.MAP_NAME, "getArrearDetails");
        try {
            
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
        calcArrearTotal();
        tblEmployee.setEditingColumn(0);
        javax.swing.table.TableColumn col = tblEmployee.getColumn(tblEmployee.getColumnName(0));
        col.setMaxWidth(50);
        col.setPreferredWidth(50);
        col = tblEmployee.getColumn(tblEmployee.getColumnName(1));
        col.setMaxWidth(70);
        col.setPreferredWidth(70);
        col = tblEmployee.getColumn(tblEmployee.getColumnName(2));
        col.setMaxWidth(300);
        col.setPreferredWidth(300);
        employee = null;
        whereMap= null;
    }

    public void calcArrearTotal() {
        double total = 0.0;        
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    public static boolean isNumeric(String str) {
        try {
            Float.parseFloat(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] arg) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        ArrearOpeningEntryUI gui = new ArrearOpeningEntryUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    public static com.see.truetransact.uicomponent.CLabel lblAccount;
    private com.see.truetransact.uicomponent.CLabel lblFromDt;
    private com.see.truetransact.uicomponent.CLabel lblToDt;
    private com.see.truetransact.uicomponent.CPanel panButtons;
    private com.see.truetransact.uicomponent.CPanel panCashTrans;
    private com.see.truetransact.uicomponent.CPanel panDetails;
    private com.see.truetransact.uicomponent.CPanel panParameters;
    private com.see.truetransact.uicomponent.CPanel panTree;
    private com.see.truetransact.uicomponent.CRadioButton rdoBasic;
    private com.see.truetransact.uicomponent.CButtonGroup rdoBtnGroup;
    private com.see.truetransact.uicomponent.CRadioButton rdoDA;
    private com.see.truetransact.uicomponent.CRadioButton rdoHra;
    private com.see.truetransact.uicomponent.CRadioButton rdoPF;
    private com.see.truetransact.uicomponent.CScrollPane srpEmployee;
    private com.see.truetransact.uicomponent.CTable tblEmployee;
    private com.see.truetransact.uicomponent.CDateField tdtFromDt;
    private com.see.truetransact.uicomponent.CDateField tdtToDt;
    private com.see.truetransact.uicomponent.CTextField txtNewDA;
    private com.see.truetransact.uicomponent.CTextField txtOldDA;
    // End of variables declaration//GEN-END:variables

    public static void printDescendants(DefaultMutableTreeNode root) {
        System.out.println(root);
        Enumeration children = root.children();
        if (children != null) {
            while (children.hasMoreElements()) {
                printDescendants((DefaultMutableTreeNode) children.nextElement());
            }
        }
    }

    public void visitAllExpandedNodes(JTree tree, TreePath parent) {
        if (!tree.isVisible(parent)) {
            return;
        }
        TreeNode node = (TreeNode) parent.getLastPathComponent();

        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
            }
        }

    }
}
