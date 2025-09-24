/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * CashierApprovalUI.java
 *
 * Created on September 28, 2011, 3:03 PM
 */
package com.see.truetransact.ui.transaction.cash;

import com.see.truetransact.clientutil.ClientUtil;
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
import java.util.Date;
import java.math.BigDecimal;
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
import com.see.truetransact.uicomponent.CCheckBox;
import com.see.truetransact.uicomponent.CPopupMenu;
import com.see.truetransact.uicomponent.CTable;
import java.awt.Checkbox;
import java.awt.event.MouseListener;
import java.net.HttpURLConnection;
import javax.print.DocFlavor.STRING;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreeNode;
//import sun.swing.table.DefaultTableCellHeaderRenderer;
//import sun.swing.table.DefaultTableCellHeaderRenderer;

/*
class CheckBoxNodeRenderer  extends CCheckBox implements TableCellRenderer {
private JCheckBox leafRenderer = new JCheckBox();

private DefaultTableCellRenderer nonLeafRenderer = new DefaultTableCellHeaderRenderer();

Color selectionBorderColor, selectionForeground, selectionBackground,
textForeground, textBackground;

protected JCheckBox getLeafRenderer() {
return leafRenderer;
}

public CheckBoxNodeRenderer() {
Font fontValue;
fontValue = UIManager.getFont("Tree.font");
if (fontValue != null) {
leafRenderer.setFont(fontValue);
}
Boolean booleanValue = (Boolean) UIManager
.get("Tree.drawsFocusBorderAroundIcon");
leafRenderer.setFocusPainted((booleanValue != null)
&& (booleanValue.booleanValue()));

selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
selectionForeground = UIManager.getColor("Tree.selectionForeground");
selectionBackground = UIManager.getColor("Tree.selectionBackground");
textForeground = UIManager.getColor("Tree.textForeground");
textBackground = UIManager.getColor("Tree.textBackground");
}

public Component getTreeCellRendererComponent(CTable recieptTable, Object value,
boolean selected, boolean expanded, boolean leaf, int row,
boolean hasFocus) {
return null;

}

@Override
public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
Component returnValue;
if (isSelected) {

String stringValue = table.getValueAt(table.getSelectedRow(),1).toString();
leafRenderer.setText(stringValue);
leafRenderer.setSelected(false);

leafRenderer.setEnabled(table.isEnabled());

if (hasFocus) {
leafRenderer.setForeground(selectionForeground);
leafRenderer.setBackground(selectionBackground);
} else {
leafRenderer.setForeground(textForeground);
leafRenderer.setBackground(textBackground);
}

if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
Object userObject = ((DefaultMutableTreeNode) value)
.getUserObject();
if (userObject instanceof CheckBoxNode) {
CheckBoxNode node = (CheckBoxNode) userObject;
leafRenderer.setText(node.getText());
leafRenderer.setSelected(node.isSelected());
}
}
returnValue = leafRenderer;
} else {
returnValue = nonLeafRenderer.getTableCellRendererComponent(table,
value, isSelected,  hasFocus, row, column);
}
return returnValue;
}
}*/
/**
 *
 * @author  user
 */
public class CashierApprovalUI extends CInternalFrame implements Observer {
    // private CPopupMenu JPopupMenu;

    private static class dataVector {

        public dataVector() {
        }
    }
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
    private CashierApprovalOB observable;
    public ArrayList partsC = new ArrayList();
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

    public void update(Observable observed, Object arg) {
    }
//     CheckBoxEditor cbn=new CheckBoxEditor(tblReciept);

    public CashierApprovalUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setObservable();
        observable.resetForm();
        initTableData();
        // this.setBounds(0,0, 900, 550);
        fillData();
//        getAcHdTree("PENDING");
        cboStatus.setSelectedItem("Receipts");

        cboStatus.setVisible(false);
        btnRecieptActionPerformed1("CREDIT");
        btnRecieptActionPerformedDebit("DEBIT");
        transCount();
        //btnReject.setVisible(true);
        // btnApprove.setVisible(false);
//        tblReciept.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        //check box tree

        //  CheckBoxEditor renderer = new CheckBoxEditor(tblReciept);
        // tblReciept.setCellEditor(renderer);

//            tblReciept.setCellEditor(new CheckBoxEditor(tblReciept));
        //tblReciept.setEditable(true);
        // new CheckBoxEditor(tblReciept).getCellEditorValue();
        // getDisplayDet(partsC);
        // this.partsC=partsC;
        try {
            setOperationMap();
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDisableReject();
        btnRefreshActionPerformed(null);
        //        btnRecieptActionPerformed1("CREDIT");
        tblReciept.getModel().addTableModelListener(tableModelListener);
        setOpBalanceAmount();
        cLabel19.setVisible(false);
        lblRecTotal.setVisible(false);
        cLabel17.setVisible(false);
        lblPayTotal.setVisible(false);
    }

    public void setOpBalanceAmount() {
        HashMap singleAuthorizeMapOpBal = new HashMap();
        singleAuthorizeMapOpBal.put("TRANS_DT", currDt);
        singleAuthorizeMapOpBal.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        List aListOp = ClientUtil.executeQuery("getOpBalAmount", singleAuthorizeMapOpBal);
        lblOpBal.setText("0.00");
        System.out.println("aListOp ---------------- " + aListOp);
        System.out.println("aListOp ------33---------- " + aListOp.size());
        if (aListOp.size() > 0 && aListOp.get(0) != null) {
            HashMap mapop = (HashMap) aListOp.get(0);
            Opamount = mapop.get("CURAMOUNT").toString();
            if (Opamount != null && !Opamount.equalsIgnoreCase("")) {
                lblOpBal.setText(formatCrore(Opamount));
            }
            amtGlobal = mapop.get("CURAMOUNT").toString();
            // else 

        }
    }
    // Set the value of JNDI and the Session Bean...

    private void setOperationMap() throws Exception {
        //  log.info("In setOperationMap()");

        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "CashTransactionJNDI");
        operationMap.put(CommonConstants.HOME, "transaction.cash.CashTransactionHome");
        operationMap.put(CommonConstants.REMOTE, "transaction.cash.CashTransaction");
    }

    public CashierApprovalUI(ArrayList aList) {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setObservable();
        //cboStatus.setSelectedItem("Pending");
    }

    private void setObservable() {
        try {
            observable = CashierApprovalOB.getInstance();
            observable.addObserver(this);
            cashOB = new CashTransactionOB();
            cashOB.addObserver(this);
        } catch (Exception e) {
            System.out.println("Error in setObservable():" + e);
        }
    }

    public void insertIntoDenaomination(String comp) {
        try {
            if (comp != null && comp.equals("single")) {
                for (int k = 0; k < partsC.size(); k++) {
                    trans_pid = partsC.get(k).toString();
                }
                for (int i = 0; i <= 21; i++) {
                    if (tblData.getModel().getValueAt(i, 2) != null
                            && tblData.getModel().getValueAt(i, 3) != null) {
                        if (trans_pid != null && !trans_pid.equalsIgnoreCase("")) {
                            //totalPay += ((Double) tblData.getModel().getValueAt(i,2)).doubleValue();
                            //System.out.println("33-=="+tblData.getModel().getValueAt(i,1));
                            String vv = tblData.getModel().getValueAt(i, 2).toString();

                            //   int len= vv.lastIndexOf(".");
                            //  if(len!=0)
                            //    vv= vv.substring(0, len);
                            //       System.out.println("vvv==="+vv);
                            System.out.println("2222222===" + tblData.getModel().getValueAt(i, 2));
                            String amount = tblData.getModel().getValueAt(i, 3).toString().replaceAll(",", "");
                            String dnomType = tblData.getModel().getValueAt(i, 0).toString();
                            insertData(trans_pid, amount, vv, "CREDIT",
                                    tblData.getModel().getValueAt(i, 1).toString(), dnomType, "single");
                        }
                    }
                }
                for (int i = 0; i <= 21; i++) {
                    if (tblData.getModel().getValueAt(i, 5) != null
                            && tblData.getModel().getValueAt(i, 6) != null) {
                        if (trans_pid != null && !trans_pid.equalsIgnoreCase("")) {
                            String vv = tblData.getModel().getValueAt(i, 5).toString();
                            //  int len= vv.lastIndexOf(".");
                            // if(len!=0)
                            //   vv= vv.substring(0, len);
                            String amount = tblData.getModel().getValueAt(i, 6).toString().replaceAll(",", "");
                            String dnomType = tblData.getModel().getValueAt(i, 0).toString();
                            insertData(trans_pid, amount, vv, "DEBIT",
                                    tblData.getModel().getValueAt(i, 4).toString(), dnomType, "single");
                        }
                    }

                }
            } //single
            else {
                HashMap newMap = new HashMap();
                newMap.put("MULTIPLE", "MULTIPLE");
                observable.execute(newMap);
                HashMap getdata = observable.getProxyReturnMap();
                System.out.println("getdata ppppppp=====" + getdata);
                String transdeno_id = "";
                if (getdata.get("DENO_TRANS_ID") != null) {
                    transdeno_id = getdata.get("DENO_TRANS_ID").toString();
                }
                System.out.println("transdeno_id   ppppppp=====" + transdeno_id);
                for (int i = 0; i <= 21; i++) {
                    if (tblData.getModel().getValueAt(i, 2) != null
                            && tblData.getModel().getValueAt(i, 3) != null) {
                        if (transdeno_id != null && !transdeno_id.equalsIgnoreCase("")) {
                            //  String vv=tblData.getModel().getValueAt(i,2).toString();
                            //  int len= vv.lastIndexOf(".");
                            // if(len!=0)
                            //   vv= vv.substring(0, len);
                            String amount = tblData.getModel().getValueAt(i, 3).toString().replaceAll(",", "");
                            String dnomType = tblData.getModel().getValueAt(i, 0).toString();
                            System.out.println("transdeno_id   MULTIPLE====" + transdeno_id);
                            insertData(transdeno_id, amount, tblData.getModel().getValueAt(i, 2).toString(), "CREDIT",
                                    tblData.getModel().getValueAt(i, 1).toString(), dnomType, "multiple");//trans_pid
                        }
                    }
                }
                for (int i = 0; i <= 21; i++) {
                    if (tblData.getModel().getValueAt(i, 5) != null
                            && tblData.getModel().getValueAt(i, 6) != null) {
                        if (transdeno_id != null && !transdeno_id.equalsIgnoreCase("")) {
                            // String vv=tblData.getModel().getValueAt(i,5).toString();
                            //    int len= vv.lastIndexOf(".");
                            //  if(len!=0)
                            //    vv= vv.substring(0, len);
                            String amount = tblData.getModel().getValueAt(i, 6).toString().replaceAll(",", "");
                            String dnomType = tblData.getModel().getValueAt(i, 0).toString();
                            insertData(transdeno_id, amount, tblData.getModel().getValueAt(i, 5).toString(), "DEBIT",
                                    tblData.getModel().getValueAt(i, 4).toString(), dnomType, "multiple");//trans_pid
                        }
                    }

                }
                //Insert denomination subtables
                if (partsC != null && partsC.size() > 1) {
                    //  String[] parts = trans_pid.split(":");
                    for (int i = 0; i < partsC.size(); i++) {
                        HashMap singleAuthorizeMap11 = new HashMap();
                        singleAuthorizeMap11.put("TRANS_DT", currDt);
                        singleAuthorizeMap11.put("TRANS_ID", partsC.get(i).toString());
                        singleAuthorizeMap11.put("DENO_TRANS_ID", transdeno_id);
                        ClientUtil.execute("insertDenominationSubDetails", singleAuthorizeMap11);
                    }
                }
                //End
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String formatCrore(String str) {
        java.text.DecimalFormat numberFormat = new java.text.DecimalFormat();
        numberFormat.applyPattern("########################0.00");

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

    public void insertData(String transId, String amount, String noOfNotes, String trans_type, String deno_type, String dnomType, String comp) {

        String currency = "INR";
        HashMap singleAuthorizeMap = new HashMap();
        if (comp != null && comp.equals("single")) {
            //  for(int i=0;i<partsC.size();i++)
            // {
            singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            singleAuthorizeMap.put("TRANS_DT", currDt);
            singleAuthorizeMap.put(CommonConstants.TRANS_ID, transId);//partsC.get(i).toString() );//trans id
            singleAuthorizeMap.put("DENOMINATION_VALUE", amount); //amount
            singleAuthorizeMap.put("DENOMINATION_COUNT", noOfNotes);//no of notes
            singleAuthorizeMap.put("CURRENCY", currency);//Currency
            singleAuthorizeMap.put("TRANS_TYPE", trans_type);//Trans type
            singleAuthorizeMap.put("STATUS", "CREATED");//status
            singleAuthorizeMap.put("DENOMINATION_TYPE", deno_type);//Denaomination type
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            singleAuthorizeMap.put("DENOM_TYPE", dnomType);
            ClientUtil.execute("insertDenominationDetails", singleAuthorizeMap);
            //}
        } else {

            System.out.println("transId IN MULTIPLE=====" + transId);
            singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            singleAuthorizeMap.put("TRANS_DT", currDt);
            singleAuthorizeMap.put(CommonConstants.TRANS_ID, transId);//trans id
            singleAuthorizeMap.put("DENOMINATION_VALUE", amount); //amount
            singleAuthorizeMap.put("DENOMINATION_COUNT", noOfNotes);//no of notes
            singleAuthorizeMap.put("CURRENCY", currency);//Currency
            singleAuthorizeMap.put("TRANS_TYPE", trans_type);//Trans type
            singleAuthorizeMap.put("STATUS", "CREATED");//status
            singleAuthorizeMap.put("DENOMINATION_TYPE", deno_type);//Denaomination type
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            singleAuthorizeMap.put("DENOM_TYPE", dnomType);
            singleAuthorizeMap.put("MULTIPLE", "MULTIPLE");
            //singleAuthorizeMap.put("TRANS_SUB_ID",transId );//trans id
            ClientUtil.execute("insertDenominationDetails", singleAuthorizeMap);

            //    observable.execute(singleAuthorizeMap);

        }
        singleAuthorizeMap.clear();
        singleAuthorizeMap = null;
    }    
    
    
    public static boolean isNumeric(String str) {
         System.out.println("str%#%#%"+str);
        try {
            Integer.parseInt(str);
            //   System.out.println("ddd"+d);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    public int NoCol = 0, NoRow = 0;

    private void setTableModelListener() {
        tableModelListener = new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    // System.out.println("Cell " + e.getFirstRow() + ", "
                    // + e.getColumn() + " changed. The new value: "
                    //  + tblData.getModel().getValueAt(e.getFirstRow(),
                    //  e.getColumn()));
                    int row = e.getFirstRow();
                    NoRow = row;
                    int column = e.getColumn();
                    NoCol = column;
                    if (row != 24) {
                        if (column == 1 || column == 2) {

                            TableModel model = tblData.getModel();

                            String quantity = model.getValueAt(row, 1).toString();
                            // System.out.println("jjjjjjjjjjjj"+model.getValueAt(row, 2).toString());
                            //  System.out.println("ssssssssssss===="+isNumeric(model.getValueAt(row, 2).toString()));
                            if (model.getValueAt(row, 2) != null && !model.getValueAt(row, 2).toString().equals("") && !isNumeric(model.getValueAt(row, 2).toString())) {
                                System.out.println("4356346436346====");
                                ClientUtil.showAlertWindow("Please enter Numeric Value!!!");
                                model.setValueAt("0", row, 2);
                                return;
                            }
                            if (model.getValueAt(row, 1) != null) {
                                if (row == 24) {
                                    quantity = "1";
                                }

                                int price = CommonUtil.convertObjToInt(model.getValueAt(row, 2));
                                Double value = null;
                                if (quantity.equals("0.50")) {
                                    value = new Double(0.50 * price);
                                } else {
                                    value = Double.valueOf((quantity)) * price;
                                }
                                // System.out.println("price====="+price +"quantity=="+quantity);
                                //    System.out.println("valuevalue=="+value);
                                model.setValueAt(String.valueOf(value), row, 3);
                                //Total print
                                double totalPay = 0;
                                BigDecimal bm = null;

                                for (int i = 0; i < 24; i++) {
                                    //  total +=((Double) model.getValueAt(i, 2)).doubleValue();
                                    if (tblData.getModel().getValueAt(i, 3) != null) {
                                        // totalPay += ((Double) tblData.getModel().getValueAt(i,2)).doubleValue();
                                        String val = tblData.getModel().getValueAt(i, 3).toString();
                                        val = val.replaceAll(",", "");
                                        // totalPay += Double.parseDouble(tblData.getModel().getValueAt(i,2).toString());
                                        totalPay += Double.parseDouble(val);
                                    }
                                }
                                model.setValueAt(formatCrore(String.valueOf(Double.valueOf(totalPay))), 24, 3);//formatCrore(
                                lblRecTotal.setText(formatCrore(String.valueOf(Double.valueOf(totalPay))));
                                if(totalPay>0){
                                if(lblTransType.getText()!=null && lblTransType.getText().equalsIgnoreCase("Receivable")){
                                    String val = CommonUtil.convertObjToStr(lblAmount.getText());
                                    val = val.replaceAll(",","");
                                    double totalPay1 = Double.parseDouble(val);   
                                    System.out.println("%#$%#$%%%#"+totalPay);
                                    System.out.println("%#$%#$%%%#"+totalPay1);
                                    if(totalPay1>totalPay){
                                        cLabel19.setVisible(true);
                                        lblRecTotal.setVisible(true);
                                        cLabel19.setText("Receivable");
                                        lblRecTotal.setText(formatCrore(String.valueOf(Double.valueOf(totalPay1-totalPay))));
                                    }else{
                                        cLabel19.setVisible(true);
                                        lblRecTotal.setVisible(true);
                                        cLabel19.setText("Payable");
                                        lblRecTotal.setText(formatCrore(String.valueOf(Double.valueOf(totalPay-totalPay1))));
                                    }
                                }
                            } else{
                                    cLabel19.setVisible(false);
                                    lblRecTotal.setVisible(false);
                                }                           
                            }
                            setModified(true);
                        }
                        if (column == 4 || column == 5) {
                            TableModel model = tblData.getModel();
                            String quantity = model.getValueAt(row, 4).toString();
                            if (model.getValueAt(row, 5) != null && !model.getValueAt(row, 5).toString().equals("") && !isNumeric(model.getValueAt(row, 5).toString())) {
                                ClientUtil.showAlertWindow("Please enter Numeric Value!!!");
                                model.setValueAt("0", row, 5);
                                return;
                            }
                            if (model.getValueAt(row, 4) != null) {
                                if (row == 24) {
                                    // System.out.println("ROWW==111=="+row);
                                    quantity = "1";
                                }
                                int price = CommonUtil.convertObjToInt(model.getValueAt(row, 5));
                                Double value = new Double(Integer.parseInt(quantity) * price);
                                model.setValueAt(formatCrore(String.valueOf(value)), row, 6);

                                //Set tiotal
                                double totalRec = 0;

                                for (int i = 0; i < 21; i++) {
                                    //  total +=((Double) model.getValueAt(i, 2)).doubleValue();
                                    if (tblData.getModel().getValueAt(i, 6) != null) {
                                        String val = tblData.getModel().getValueAt(i, 6).toString();
                                        val = val.replaceAll(",", "");
                                        // totalRec +=   Double.parseDouble(tblData.getModel().getValueAt(i,5).toString());
                                        totalRec += Double.parseDouble(val);
                                    }

                                }
                                model.setValueAt(formatCrore(String.valueOf(Double.valueOf(totalRec))), 24, 6);  //formatCrore(
                                lblPayTotal.setText(formatCrore(String.valueOf(Double.valueOf(totalRec))));
                                System.out.println("%#$%#$%%%#"+lblTransType);
                                if(totalRec>0){
                                if(lblTransType.getText()!=null && lblTransType.getText().equalsIgnoreCase("Payable")){
                                    String val = CommonUtil.convertObjToStr(lblAmount.getText());
                                    val = val.replaceAll(",","");
                                    double totalPay = Double.parseDouble(val);   
                                    System.out.println("%#$%#$%%%#"+totalRec);
                                    System.out.println("%#$%#$%%%#"+totalPay);
                                    if(totalPay<totalRec){
                                        cLabel19.setVisible(true);
                                        lblRecTotal.setVisible(true);
                                        cLabel19.setText("Payable");
                                        lblRecTotal.setText(formatCrore(String.valueOf(Double.valueOf(totalRec-totalPay))));
                                    }else{
                                        cLabel19.setVisible(true);
                                        lblRecTotal.setVisible(true);
                                        cLabel19.setText("Receivable");
                                        lblRecTotal.setText(formatCrore(String.valueOf(Double.valueOf(totalPay-totalRec))));
                                    }
                                }}else{
                                    cLabel19.setVisible(false);
                                        lblRecTotal.setVisible(false);
                                }
                                
                                setModified(true);
                            }
                        }
                    }


                }
            }
        };
        tblData.getModel().addTableModelListener(tableModelListener);
    }

    public void fillData() {
        //root = new TreeModel();
        //   child = new DefaultTreeModel("Colors");
        //   root.add(child);
    }

    public void initTableData() {
        String data[][] = {{}};
        String col[] = {"Account No", "Name", "Deposit Date", "Amount", "Maturity Date"};
        DefaultTableModel dataModel = new DefaultTableModel();
        // dataModel.setDataVector(dataVector
        model = new DefaultTableModel(data, col);
        //  tblData.getCellEditor().stopCellEditing();
        tblData.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    //                {"1000", null, null, "1000", null, null},
                    //                {"500", null, null, "500", null, null},
                    //                {"100", null, null, "100", null, null},
                    //                {"50", null, null, "50", null, null},
                    //                {"20", null, null, "20", null, null},
                    //                {"10", null, null, "10", null, null},
                    //                {"5", null, null, "5", null, null},
                    //                {"2", null, null, "2", null, null},
                    //                {"1", null, null, "1", null, null},
                    //                {"Others", null, null, "Others", null, null},
                    //                {"Total", null, null, "Total", null, null}
                    {"NOTE", "2000", null, null, "2000", null, null},
                    {"NOTE", "1000", null, null, "1000", null, null},
                    {"NOTE", "500", null, null, "500", null, null},
                    {"NOTE", "200", null, null, "200", null, null},
                    {"NOTE", "100", null, null, "100", null, null},
                    {"NOTE", "50", null, null, "50", null, null},
                    {"NOTE", "20", null, null, "20", null, null},
                    {"NOTE", "10", null, null, "10", null, null},
                    {"NOTE", "5", null, null, "5", null, null},
                    {"NOTE", "2", null, null, "2", null, null},
                    {"NOTE", "1", null, null, "1", null, null},
                    {"COIN", "20", null, null, "20", null, null},
                    {"COIN", "10", null, null, "10", null, null},
                    {"COIN", "5", null, null, "5", null, null},
                    {"COIN", "2", null, null, "2", null, null},
                    {"COIN", "1", null, null, "1", null, null},
                    {"COIN", "0.50", null, null, "0.50", null, null},
                    {"COIN", "0.25", null, null, "0.25", null, null},
                    {"COIN", "0.20", null, null, "0.20", null, null},
                    {"COIN", "0.10", null, null, "0.10", null, null},
                    {"COIN", "0.05", null, null, "0.05", null, null},
                    {"COIN", "0.02", null, null, "0.02", null, null},
                    {"COIN", "0.01", null, null, "0.01", null, null},
                    {"STAMP", "1", null, null, "1", null, null},
                    //                {null,"Others", null, null, "Others", null, null},
                    {null, "Total", null, null, "Total", null, null}
                },
                new String[]{
                    //                "Receipts", "No", "Amount", "Payments", "No", "Amount"
                    "Type", "Receipts", "No", "Amount", "Payments", "No", "Amount"
                }) {

            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, true, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (rowIndex == 21) {
                    return false;
                }
                return canEdit[columnIndex];
            }
        });
        tblData.setCellSelectionEnabled(true);
//        tblData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
//            public void propertyChange(java.beans.PropertyChangeEvent evt) {
//                tblDataPropertyChange(evt);
//            }
//        });
        setTableModelListener();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WA
     * RNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panCashTrans = new com.see.truetransact.uicomponent.CPanel();
        panTree = new com.see.truetransact.uicomponent.CPanel();
        cboStatus = new com.see.truetransact.uicomponent.CComboBox();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblReciept = new com.see.truetransact.uicomponent.CTable();
        cScrollPane2 = new com.see.truetransact.uicomponent.CScrollPane();
        tblPayment = new com.see.truetransact.uicomponent.CTable();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        cLabel4 = new com.see.truetransact.uicomponent.CLabel();
        lblOpeningbal = new com.see.truetransact.uicomponent.CLabel();
        lblRecipt = new com.see.truetransact.uicomponent.CLabel();
        lblPayment2 = new com.see.truetransact.uicomponent.CLabel();
        lblBalance = new com.see.truetransact.uicomponent.CLabel();
        btnApprove = new com.see.truetransact.uicomponent.CButton();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        scrTableScroll = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        lblTotReceipts = new com.see.truetransact.uicomponent.CLabel();
        lblTotPayments = new com.see.truetransact.uicomponent.CLabel();
        lblOpBal = new com.see.truetransact.uicomponent.CLabel();
        lblReceipt = new com.see.truetransact.uicomponent.CLabel();
        lblPayment = new com.see.truetransact.uicomponent.CLabel();
        lblClosingBal = new com.see.truetransact.uicomponent.CLabel();
        btnDenaomination = new com.see.truetransact.uicomponent.CButton();
        btnExit = new com.see.truetransact.uicomponent.CButton();
        cLabel17 = new com.see.truetransact.uicomponent.CLabel();
        lblPayTotal = new com.see.truetransact.uicomponent.CLabel();
        cLabel19 = new com.see.truetransact.uicomponent.CLabel();
        lblRecTotal = new com.see.truetransact.uicomponent.CLabel();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        lblBalance1 = new com.see.truetransact.uicomponent.CLabel();
        lblShadawBal = new com.see.truetransact.uicomponent.CLabel();
        btnRefresh = new com.see.truetransact.uicomponent.CButton();
        btnTransDetails = new com.see.truetransact.uicomponent.CButton();
        cLabel6 = new com.see.truetransact.uicomponent.CLabel();
        lblTransType = new com.see.truetransact.uicomponent.CLabel();
        cLabel7 = new com.see.truetransact.uicomponent.CLabel();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(840, 660));
        setMinimumSize(new java.awt.Dimension(840, 660));
        setPreferredSize(new java.awt.Dimension(840, 660));
        getContentPane().setLayout(null);

        panCashTrans.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panCashTrans.setMinimumSize(new java.awt.Dimension(900, 900));
        panCashTrans.setLayout(null);

        panTree.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panTree.setMinimumSize(new java.awt.Dimension(800, 250));
        panTree.setLayout(null);

        cboStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Receipts", "Payments" }));
        cboStatus.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboStatusItemStateChanged(evt);
            }
        });
        cboStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboStatusActionPerformed(evt);
            }
        });
        panTree.add(cboStatus);
        cboStatus.setBounds(540, 160, 270, 21);

        cScrollPane1.setMinimumSize(new java.awt.Dimension(800, 250));
        cScrollPane1.setPreferredSize(new java.awt.Dimension(150, 80));
        cScrollPane1.setWheelScrollingEnabled(false);
        cScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cScrollPane1MouseClicked(evt);
            }
        });

        tblReciept.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "select", "Trans Id", "Product", "Name", "Amount", "Account no", "Status by"
            }
        ));
        tblReciept.setColumnSelectionAllowed(true);
        tblReciept.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRecieptMouseClicked(evt);
            }
        });
        cScrollPane1.setViewportView(tblReciept);
        tblReciept.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        panTree.add(cScrollPane1);
        cScrollPane1.setBounds(10, 30, 800, 120);

        cScrollPane2.setMinimumSize(new java.awt.Dimension(850, 850));

        tblPayment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Select", "Trans Id", "Product", "Name", "Amount", "Account no", "Status"
            }
        ));
        tblPayment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPaymentMouseClicked(evt);
            }
        });
        cScrollPane2.setViewportView(tblPayment);

        panTree.add(cScrollPane2);
        cScrollPane2.setBounds(10, 180, 800, 120);

        cLabel3.setForeground(new java.awt.Color(255, 0, 51));
        cLabel3.setText("PAYMENTS");
        cLabel3.setFont(new java.awt.Font("MS Sans Serif", 1, 18));
        panTree.add(cLabel3);
        cLabel3.setBounds(10, 150, 120, 24);

        cLabel4.setForeground(new java.awt.Color(0, 0, 204));
        cLabel4.setText("RECEIPTS");
        cLabel4.setFont(new java.awt.Font("MS Sans Serif", 1, 18));
        panTree.add(cLabel4);
        cLabel4.setBounds(10, 0, 130, 24);

        panCashTrans.add(panTree);
        panTree.setBounds(0, 0, 820, 310);

        lblOpeningbal.setText("Op Bal");
        panCashTrans.add(lblOpeningbal);
        lblOpeningbal.setBounds(10, 450, 42, 18);

        lblRecipt.setText("Receipt");
        panCashTrans.add(lblRecipt);
        lblRecipt.setBounds(10, 480, 52, 18);

        lblPayment2.setText("Payment");
        lblPayment2.setPreferredSize(new java.awt.Dimension(47, 18));
        panCashTrans.add(lblPayment2);
        lblPayment2.setBounds(10, 510, 60, 18);

        lblBalance.setText("Shadow Bal");
        panCashTrans.add(lblBalance);
        lblBalance.setBounds(10, 570, 70, 18);

        btnApprove.setText("Approve");
        btnApprove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApproveActionPerformed(evt);
            }
        });
        panCashTrans.add(btnApprove);
        btnApprove.setBounds(310, 320, 90, 27);

        btnReject.setText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        panCashTrans.add(btnReject);
        btnReject.setBounds(310, 380, 90, 27);

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1000", null, null, "1000", null, null},
                {"500", null, null, "500", null, null},
                {"100", null, null, "100", null, null},
                {"50", null, null, "50", null, null},
                {"20", null, null, "20", null, null},
                {"10", null, null, "10", null, null},
                {"5", null, null, "5", null, null},
                {"2", null, null, "2", null, null},
                {"1", null, null, "1", null, null},
                {"Others", null, null, "Others", null, null},
                {"Total", null, null, "Total", null, null}
            },
            new String [] {
                "Receipts", "No", "Amount", "Payments", "No", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblData.setCellSelectionEnabled(true);
        tblData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                keypressed(evt);
            }
        });
        scrTableScroll.setViewportView(tblData);

        panCashTrans.add(scrTableScroll);
        scrTableScroll.setBounds(440, 320, 380, 270);

        cPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Total"));
        cPanel1.setLayout(null);

        cLabel1.setText("Receipts");
        cPanel1.add(cLabel1);
        cLabel1.setBounds(30, 20, 70, 18);

        cLabel2.setText("Payments");
        cPanel1.add(cLabel2);
        cLabel2.setBounds(30, 60, 70, 18);

        lblTotReceipts.setText(posted_by);
        lblTotReceipts.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        lblTotReceipts.setMaximumSize(new java.awt.Dimension(74, 14));
        lblTotReceipts.setMinimumSize(new java.awt.Dimension(74, 14));
        lblTotReceipts.setPreferredSize(new java.awt.Dimension(74, 14));
        cPanel1.add(lblTotReceipts);
        lblTotReceipts.setBounds(10, 40, 120, 14);

        lblTotPayments.setText(posted_by);
        lblTotPayments.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        lblTotPayments.setMaximumSize(new java.awt.Dimension(74, 14));
        lblTotPayments.setMinimumSize(new java.awt.Dimension(74, 14));
        lblTotPayments.setPreferredSize(new java.awt.Dimension(74, 14));
        cPanel1.add(lblTotPayments);
        lblTotPayments.setBounds(10, 80, 120, 14);

        panCashTrans.add(cPanel1);
        cPanel1.setBounds(190, 450, 110, 100);

        lblOpBal.setForeground(new java.awt.Color(0, 51, 255));
        lblOpBal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOpBal.setText(particulars);
        lblOpBal.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        lblOpBal.setMaximumSize(new java.awt.Dimension(74, 14));
        lblOpBal.setMinimumSize(new java.awt.Dimension(74, 14));
        lblOpBal.setPreferredSize(new java.awt.Dimension(74, 14));
        panCashTrans.add(lblOpBal);
        lblOpBal.setBounds(70, 450, 120, 14);

        lblReceipt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReceipt.setText(particulars);
        lblReceipt.setMaximumSize(new java.awt.Dimension(74, 14));
        lblReceipt.setMinimumSize(new java.awt.Dimension(74, 14));
        lblReceipt.setPreferredSize(new java.awt.Dimension(74, 14));
        panCashTrans.add(lblReceipt);
        lblReceipt.setBounds(70, 480, 120, 14);

        lblPayment.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPayment.setText(particulars);
        lblPayment.setMaximumSize(new java.awt.Dimension(74, 14));
        lblPayment.setMinimumSize(new java.awt.Dimension(74, 14));
        lblPayment.setPreferredSize(new java.awt.Dimension(74, 14));
        panCashTrans.add(lblPayment);
        lblPayment.setBounds(70, 510, 120, 14);

        lblClosingBal.setForeground(new java.awt.Color(0, 0, 255));
        lblClosingBal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblClosingBal.setText(particulars);
        lblClosingBal.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        lblClosingBal.setMaximumSize(new java.awt.Dimension(74, 14));
        lblClosingBal.setMinimumSize(new java.awt.Dimension(74, 14));
        lblClosingBal.setPreferredSize(new java.awt.Dimension(74, 14));
        panCashTrans.add(lblClosingBal);
        lblClosingBal.setBounds(70, 540, 120, 14);

        btnDenaomination.setText("Change");
        btnDenaomination.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDenaominationActionPerformed(evt);
            }
        });
        panCashTrans.add(btnDenaomination);
        btnDenaomination.setBounds(310, 350, 90, 27);

        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        panCashTrans.add(btnExit);
        btnExit.setBounds(310, 440, 90, 27);

        cLabel17.setText("Den.Pay.Total");
        panCashTrans.add(cLabel17);
        cLabel17.setBounds(310, 520, 90, 18);

        lblPayTotal.setForeground(new java.awt.Color(255, 51, 51));
        lblPayTotal.setText(clear_Bal);
        lblPayTotal.setMaximumSize(new java.awt.Dimension(74, 14));
        lblPayTotal.setMinimumSize(new java.awt.Dimension(74, 14));
        lblPayTotal.setPreferredSize(new java.awt.Dimension(74, 14));
        panCashTrans.add(lblPayTotal);
        lblPayTotal.setBounds(310, 540, 100, 14);

        cLabel19.setForeground(new java.awt.Color(255, 51, 51));
        cLabel19.setText("Den.Rec.Total");
        cLabel19.setFont(new java.awt.Font("MS Sans Serif", 1, 12)); // NOI18N
        panCashTrans.add(cLabel19);
        cLabel19.setBounds(310, 480, 100, 16);

        lblRecTotal.setForeground(new java.awt.Color(255, 51, 51));
        lblRecTotal.setText(clear_Bal);
        lblRecTotal.setFont(new java.awt.Font("MS Sans Serif", 1, 12)); // NOI18N
        lblRecTotal.setMaximumSize(new java.awt.Dimension(74, 14));
        lblRecTotal.setMinimumSize(new java.awt.Dimension(74, 14));
        lblRecTotal.setPreferredSize(new java.awt.Dimension(74, 14));
        panCashTrans.add(lblRecTotal);
        lblRecTotal.setBounds(310, 500, 100, 14);

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        panCashTrans.add(btnClear);
        btnClear.setBounds(310, 410, 90, 27);

        lblBalance1.setText("Curr Balance");
        panCashTrans.add(lblBalance1);
        lblBalance1.setBounds(10, 540, 80, 18);

        lblShadawBal.setForeground(new java.awt.Color(0, 0, 255));
        lblShadawBal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblShadawBal.setText(particulars);
        lblShadawBal.setMaximumSize(new java.awt.Dimension(74, 14));
        lblShadawBal.setMinimumSize(new java.awt.Dimension(74, 14));
        lblShadawBal.setPreferredSize(new java.awt.Dimension(74, 14));
        panCashTrans.add(lblShadawBal);
        lblShadawBal.setBounds(80, 570, 110, 14);

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        panCashTrans.add(btnRefresh);
        btnRefresh.setBounds(190, 560, 120, 27);

        btnTransDetails.setText("Trans.Details");
        btnTransDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransDetailsActionPerformed(evt);
            }
        });
        panCashTrans.add(btnTransDetails);
        btnTransDetails.setBounds(60, 400, 130, 27);

        cLabel6.setText("Transaction Type");
        panCashTrans.add(cLabel6);
        cLabel6.setBounds(30, 370, 110, 18);

        lblTransType.setForeground(new java.awt.Color(0, 0, 204));
        lblTransType.setText(trans_type);
        lblTransType.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        lblTransType.setMaximumSize(new java.awt.Dimension(74, 14));
        lblTransType.setMinimumSize(new java.awt.Dimension(74, 14));
        lblTransType.setPreferredSize(new java.awt.Dimension(74, 14));
        panCashTrans.add(lblTransType);
        lblTransType.setBounds(160, 370, 120, 20);

        cLabel7.setText("Transaction Amt");
        panCashTrans.add(cLabel7);
        cLabel7.setBounds(30, 340, 110, 18);

        lblAmount.setForeground(new java.awt.Color(0, 0, 204));
        lblAmount.setText(amount);
        lblAmount.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        lblAmount.setMaximumSize(new java.awt.Dimension(74, 14));
        lblAmount.setMinimumSize(new java.awt.Dimension(74, 14));
        lblAmount.setPreferredSize(new java.awt.Dimension(74, 14));
        panCashTrans.add(lblAmount);
        lblAmount.setBounds(160, 340, 120, 20);

        getContentPane().add(panCashTrans);
        panCashTrans.setBounds(10, 10, 900, 900);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDenaominationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDenaominationActionPerformed
        // TODO add your handling code here:
        String amt = lblAmount.getText();
        String transType = lblTransType.getText();
        DenominationUI denoUI = new DenominationUI(amt, transType, trans_pid);
        denoUI.show();
        denoUI.setVisible(true);
    }//GEN-LAST:event_btnDenaominationActionPerformed

    private void btnTransDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransDetailsActionPerformed
        // TODO add your handling code here:
        boolean flag = false;
        HashMap selectMap = new HashMap();
        System.out.println("********************" + tblReciept.getValueAt(tblReciept.getSelectedRow(), 1));
        selectMap.put("TRANS_ID", tblReciept.getValueAt(tblReciept.getSelectedRow(), 1));
        selectMap.put("TRANS_DT", currDt.clone());
        selectMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        List aList = ClientUtil.executeQuery("getUniqueIdData", selectMap);
        //   System.out.println("aList ii == "+aList +"trans_id - "+trans_id+" =="+lblTransId.getText());
        if (aList != null) {
            HashMap map1 = null;
            String Id = null;
            if (aList.get(0) != null) {
                map1 = (HashMap) aList.get(0);
                Id = CommonUtil.convertObjToStr(map1.get("SCREEN_NAME"));
            }
            System.out.println("IdIdIdId ii ==" + Id);
            if (Id != null && !Id.equals("") && Id.startsWith("CU")) {
                flag = true;
                HashMap selectMap1 = new HashMap();
                //  selectMap1.put("LINK_BATCH_ID", map.get("ACCT_NUM"));
                //   selectMap1.put("TRANS_ID",lblTransId.getText());
                selectMap1.put("TRANS_ID", tblReciept.getValueAt(tblReciept.getSelectedRow(), 1));
                selectMap1.put("TODAY_DT", currDt.clone());
                selectMap1.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
                tableDialogUI = new TableDialogUI("getAllTransactionViewInterestCashier", selectMap1, "");
                //Added By Suresh
                // txtAmount.setText(tableDialogUI.getTotalAmtValue()); 
                // observable.setTxtAmount(txtAmount.getText());
                tableDialogUI.setTitle("Deposit Interest Application Transaction");
                tableDialogUI.show();
                System.out.println("fffffffffffffff==" + Id);
            }
        }

        if (!flag) {
            tableDialogUI = null;
            System.out.println("prod_Type 0000 ======" + prod_Type + " link_batch_id=====" + link_batch_id);
            //  if(prod_Type.equals("TermLoan")){
            HashMap selectMap1 = new HashMap();
            selectMap1.put("LINK_BATCH_ID", link_batch_id);
            selectMap1.put("TODAY_DT", currDt.clone());
            selectMap1.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
            tableDialogUI = new TableDialogUI("getAllTransactionViewAD", selectMap1, "");

            if (tableDialogUI != null) {
                tableDialogUI.setTitle("Loan/Advances Transaction Details");
            }
            // }
            if (tableDialogUI != null) {
                tableDialogUI.show();
            }
        }
    }//GEN-LAST:event_btnTransDetailsActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
//        reject();
//        setModified(false);
//        getAcHdTree("PENDING");
//        clearTransDetails();        
        System.out.println("partsC12346>>>>" + partsC);
        if (partsC.size() > 0) {
            //if (isDenominationTallied()) {
            getUpdateReject();
            setModified(false);
            partsC.clear();
            cboStatusActionPerformed(null);
            btnRefreshActionPerformed(null);
            // }
        } else {
            displayAlert("Please select Receipts to Reject!!!");
            return;
        }

    }//GEN-LAST:event_btnRejectActionPerformed
    /*  private HashMap fillData(int rowIndexSelected) {
    
    //TableModel _tableModel = (TableModel) tblData.getModel();
    // ArrayList rowdata = null;
    
    // if (rowIndexSelected > -1) {
    //     rowdata = data;
    // }
    
    HashMap hashdata = new HashMap();
    String strColName = null;
    Object obj = null;
    
    
    // System.out.println("data.size() ================"+data.size());
    // System.out.println("_heading.size() ================"+_heading.size());
    //  for(int i=0;i<_heading.size();i++)
    //  {
    for(int j=0;j<data.size();j++)
    {
    System.out.println("data.get(j).toString() ==============="+data.get(j).toString());
    //hashdata.put(_heading.get(i).toString(), data.get(j).toString());
    ArrayList aList=(ArrayList)data.get(j);
    for(int i=0;i<aList.size();i++)
    {
    System.out.println("daAADDDDDDDDDDDDDDDD==========="+aList.get(i));
    if(aList.get(0)!=null)
    hashdata.put("SCHEME_NAME", aList.get(0).toString());
    else
    hashdata.put("SCHEME_NAME",null);  
    if(aList.get(1)!=null)
    hashdata.put("CHITTAL_NO", aList.get(1).toString());
    else
    hashdata.put("CHITTAL_NO", null);
    if(aList.get(2)!=null)
    hashdata.put("SUB_NO", aList.get(2).toString());
    else
    hashdata.put("SUB_NO",null); 
    if(aList.get(3)!=null)
    hashdata.put("MEMBER_NAME", aList.get(3).toString());
    else
    hashdata.put("MEMBER_NAME", null); 
    if(aList.get(4)!=null) 
    hashdata.put("CHIT_START_DT", aList.get(4).toString());
    else
    hashdata.put("CHIT_START_DT",null);
    if(aList.get(5)!=null) 
    hashdata.put("NET_TRANS_ID", aList.get(5).toString());
    else
    hashdata.put("NET_TRANS_ID", null);  
    if(aList.get(6)!=null) 
    hashdata.put("NET_AMT", aList.get(6).toString());
    else
    hashdata.put("NET_AMT", null);  
    
    }
    }
    
    // }
    return hashdata;
    }*/

    private HashMap fillData(int rowIndexSelected) {



        HashMap hashdata = new HashMap();
        String strColName = null;
        Object obj = null;

        /* for (int i = 0, j = data.size(); i < j; i++) {
        if (rowdata != null)
        obj = rowdata.get(i);
        
        strColName = _heading.get(i).toString().toUpperCase().trim();
        if (obj != null) {
        hashdata.put(strColName, obj);
        } else {
        hashdata.put(strColName, "");
        }
        }*/

        // System.out.println("data.size() ================"+data.size());
        // System.out.println("_heading.size() ================"+_heading.size());

        //  ArrayList head=new ArrayList();
        //  for(int k=0;k<_heading.size();k++)
        //  {
        //       head=(ArrayList)_heading.get(k);
        //          System.out.println("data.gfgfdghfdgfdgfng() ==============="+_heading.get(k));
        //  }
        ArrayList aList = new ArrayList();
        for (int j = 0; j < data.size(); j++) {
            System.out.println("data.get(j).toString() ===============" + data.get(j));
            //hashdata.put(_heading.get(i).toString(), data.get(j).toString());
            aList = (ArrayList) data.get(j);
        }
        for (int l = 0; l < _heading.size(); l++) {
            for (int i = 0; i < aList.size(); i++) {
                System.out.println("head.get(0)=====" + _heading.get(0));
                System.out.println("head.get(1)=====" + _heading.get(1));
                System.out.println("head.get(2)=====" + _heading.get(2));
                //  System.out.println("aList.get(i)====="+aList.get(i));
                // hashdata.put(_heading.get(l).toString(), aList.get(i).toString());
                if (aList.get(0) != null) {
                    hashdata.put(_heading.get(0), aList.get(0).toString());
                } else {
                    hashdata.put(_heading.get(0), null);
                }
                if (aList.get(1) != null) {
                    hashdata.put(_heading.get(1), aList.get(1).toString());
                } else {
                    hashdata.put(_heading.get(1), null);
                }
                if (aList.get(2) != null) {
                    hashdata.put(_heading.get(2), aList.get(2).toString());
                } else {
                    if (_heading.get(2).equals("SHARE ACCOUNT NO")) {
                        hashdata.put(_heading.get(2), "");
                    } else {
                        hashdata.put(_heading.get(2), null);
                    }
                }
                if (aList.get(3) != null) {
                    hashdata.put(_heading.get(3), aList.get(3).toString());
                } else {
                    hashdata.put(_heading.get(3), null);
                }
                if (aList.get(4) != null) {
                    hashdata.put(_heading.get(4), aList.get(4).toString());
                } else {
                    hashdata.put(_heading.get(4), null);
                }
                if (aList.get(5) != null) {
                    hashdata.put(_heading.get(5), aList.get(5).toString());
                } else {
                    hashdata.put(_heading.get(5), null);
                }
                if (aList.get(6) != null) {
                    hashdata.put(_heading.get(6), aList.get(6).toString());
                } else {
                    hashdata.put(_heading.get(6), null);
                }

            }
        }

        //   }
        return hashdata;
    }

    public void reject() {
        //cashOB.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        //authorizeStatus(CommonConstants.STATUS_REJECTED);
        data = null;
        HashMap whereMap = new HashMap();
        HashMap where = new HashMap();
        where.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        where.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        where.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
//          where.put("TRANS_ID",lblTransId.getText());
        CInternalFrame frm = null;
        System.out.println("screenName IN =====" + screenName);
        if (screenName != null || !screenName.equals("")) {

            /*  if (screenName.equals("Customer Master")) {
            frm = new com.see.truetransact.ui.customer.IndividualCustUI();
            whereMap.put(CommonConstants.MAP_NAME, "getUnAuthorizedListForCustomer");*/
            // } else
            if (screenName.equals("SB/Current Account Opening")) {//chk 
                frm = new com.see.truetransact.ui.operativeaccount.AccountsUI();
                whereMap.put(CommonConstants.MAP_NAME, "getSelectAccountMasterAuthorizeTOList1");
            } else if (screenName.equals("SB/Current Account Closing")) {//chk 
                frm = new com.see.truetransact.ui.operativeaccount.AccountClosingUI();
                where.put("AUTHORIZESTATUS", "REJECTED");
                whereMap.put(CommonConstants.MAP_NAME, "getSelectAccountCloseAuthorizeTOList1");
            } else if (screenName.equals("Deposit Account Opening")) {
                frm = new com.see.truetransact.ui.deposit.TermDepositUI();
                where.put("OPENING_MODE", "Normal");
                where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
                whereMap.put(CommonConstants.MAP_NAME, "viewAllDepAccAuthorizeTOList1");
            } else if (screenName.equals("Deposit Account Renewal")) {
                frm = new com.see.truetransact.ui.deposit.TermDepositUI();
                where.put("OPENING_MODE", "Renewal");
                where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
                whereMap.put(CommonConstants.MAP_NAME, "viewAllDepAccAuthorizeTOList1");
            } else if (screenName.equals("Deposit Account Closing")) {
                frm = new com.see.truetransact.ui.deposit.closing.DepositClosingUI();
                whereMap.put(CommonConstants.MAP_NAME, "getDepositAccountCloseAuthorizeTOList1");
            } else if (screenName.equals("Gold Loan Account Opening")) {
                frm = new com.see.truetransact.ui.termloan.GoldLoanUI("OTHERS", CommonUtil.convertObjToStr(prod_Id));
                where.put("AUTHORIZE_REMARK", "!= 'GOLD_LOAN'");
                where.put("AUTHORIZESTATUS", "REJECTED");
                where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
                where.put("STATUS_BY", ProxyParameters.USER_ID);
                whereMap.put(CommonConstants.MAP_NAME, "getSelectTermLoanAuthorizeTOList1");
            } else if (screenName.equals("Loans/Advances Account Opening")) {
                // frm = new com.see.truetransact.ui.termloan.TermLoanUI("OTHERS");
                where.put("AUTHORIZE_REMARK", "= 'GOLD_LOAN'");
                where.put("AUTHORIZESTATUS", "REJECTED");
                where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
                where.put("STATUS_BY", ProxyParameters.USER_ID);
                whereMap.put(CommonConstants.MAP_NAME, "getSelectTermLoanAuthorizeTOList1");
            } else if (screenName.equals("Deposit Loan Account Opening")) {
                frm = new com.see.truetransact.ui.termloan.depositLoan.DepositLoanUI();
                where.put("AUTHORIZESTATUS", "REJECTED");
                where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
                where.put("STATUS_BY", ProxyParameters.USER_ID);
                whereMap.put(CommonConstants.MAP_NAME, "getSelectTermLoanAuthorizeTOListForLTD1");
            } else if (screenName.equals("Loan Account Closing")) {
                frm = new com.see.truetransact.ui.operativeaccount.AccountClosingUI("TermLoan");
                where.put("AUTHORIZESTATUS", "REJECTED");
                whereMap.put(CommonConstants.MAP_NAME, "getSelectLoanAccountCloseAuthorizeTOList1");
            } else if (screenName.equals("Cash Transactions")) {
                frm = new com.see.truetransact.ui.transaction.cash.CashTransactionUI();
                where.put("AUTHORIZESTATUS", "REJECTED");
                where.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                whereMap.put(CommonConstants.MAP_NAME, "getSelectCashTransactionAuthorizeTOList1");
            } else if (screenName.equals("Transfer Transactions")) {//no
                frm = new com.see.truetransact.ui.transaction.transfer.TransferUI();
                where.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                whereMap.put(CommonConstants.MAP_NAME, "getUnAuthorizeMasterTransferTO1");
            } else if (screenName.equals("Share Account")) {
                frm = new com.see.truetransact.ui.share.ShareUI();
                where.put("AUTHORIZESTATUS", "REJECTED");
                where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
                where.put("TRANS_DT", currDt);
                whereMap.put(CommonConstants.MAP_NAME, "viewAllShareAcctAuthorizeTOList1");
            } else if (screenName.equals("MDS Receipts")) {
                frm = new com.see.truetransact.ui.mdsapplication.mdsreceiptentry.MDSReceiptEntryUI();
                where.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
                whereMap.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForReceipt1");
            } else if (screenName.equals("MDS Payments")) {
                frm = new com.see.truetransact.ui.mdsapplication.mdsprizedmoneypayment.MDSPrizedMoneyPaymentUI();
                where.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                whereMap.put(CommonConstants.MAP_NAME, "getPrizedMoneyPaymentAuthorize1");
            }
            whereMap.put(CommonConstants.MAP_WHERE, where);
            if (whereMap.containsKey(CommonConstants.MAP_NAME)) {
                whereMap = ClientUtil.executeTableQuery(whereMap);
                _heading = (ArrayList) whereMap.get(CommonConstants.TABLEHEAD);
                data = (ArrayList) whereMap.get(CommonConstants.TABLEDATA);
                System.out.println("data IN =====" + data);
                System.out.println("_heading IN =====" + _heading);
            }
            HashMap hash = fillData(1);
            hash.put("FROM_CASHIER_APPROVAL_REJ_UI", "");
            System.out.println("hash 1111 =====" + hash);
            System.out.println("ProxyParameters.BRANCH_ID 1111 =====" + ProxyParameters.BRANCH_ID);
            frm.setSelectedBranchID(ProxyParameters.BRANCH_ID);//getSelectedBranchID());
            frm.fillData(hash);
            System.out.println("authorizeStatus 1111 >>>>>=====" + CommonConstants.STATUS_REJECTED);
            System.out.println("frm 1111 >>>>>=====" + frm);
            frm.authorizeStatus(CommonConstants.STATUS_REJECTED);

        }
    }
    /*    public void authorizeStatus(String authorizeStatus) {
    
    HashMap singleAuthorizeMap = new HashMap();
    ArrayList arrList = new ArrayList();
    HashMap authDataMap = new HashMap();
    authDataMap.put("ACCOUNT NO", lblAcNo.getText());
    authDataMap.put("PRODUCT ID", CommonUtil.convertObjToStr(prod_Id));
    
    authDataMap.put("TRANS_ID", lblTransId.getText());
    authDataMap.put("USER_ID",ProxyParameters.USER_ID);
    authDataMap.put("TRANS_DT", currDt.clone());
    authDataMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
    List lst=ClientUtil.executeQuery("selectauthorizationLock", authDataMap);
    if(lst !=null && lst.size()>0) {
    HashMap map=new HashMap();
    StringBuffer open=new StringBuffer();
    for(int i=0;i<lst.size();i++){
    map=(HashMap)lst.get(i);
    open.append ("\n"+"User Id  :"+" ");
    open.append(CommonUtil.convertObjToStr(map.get("OPEN_BY"))+"\n");
    open.append("Mode Of Operation  :" +" ");
    open.append(CommonUtil.convertObjToStr(map.get("MODE_OF_OPERATION"))+" ");                
    }
    ClientUtil.showMessageWindow("already open by"+open);           
    return;
    }
    authDataMap.put("REMARKS", "CASHIER APPROVAL");
    arrList.add(authDataMap);
    singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
    singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
    if(cashOB.getLinkMap() !=null && cashOB.getLinkMap() .containsKey("AS_CUSTOMER_COMES") && cashOB.getLinkMap() .get("AS_CUSTOMER_COMES").equals("Y"))
    singleAuthorizeMap.put("DAILY","DAILY");
    
    authorize(singleAuthorizeMap);
    
    }*/
    /*public void setAuthorizeMap(HashMap authorizeMap) {
    _authorizeMap = authorizeMap;
    }
    
    // Getter for Authorization.
    public HashMap getAuthorizeMap() {
    return _authorizeMap;
    }*/
    /* public void authorize(HashMap map) {
    
    if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
    setActionType(ClientConstants.ACTIONTYPE_REJECT);
    setAuthorizeMap(map);
    doAction();
    
    
    clearTransDetails();
    
    }
    }
    // Returns the Current Value of Action type...
    public int getActionType(){
    return actionType;
    }
    
    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
    this.actionType = actionType;
    //setChanged();
    }*/
    // To perform Appropriate operation... Insert, Update, Delete...
 /*   public void doAction() {
    TTException exception = null;
    //  log.info("In doAction()");
    
    try {
    
    // The following block added by Rajesh to avoid Save operation after Authorization.
    // If one person opened a transaction for Edit and another person opened the same 
    // transaction for Authorization, the system is allowing to save after Authorization also.  
    // So, after authorization again the GL gets updated and a/c level shadow credit/debit goes negative.
    // In this case the should not allow to save or some error message should display.  
    /*  if ((!lblTransId.getText().equals("")) && getActionType()!=ClientConstants.ACTIONTYPE_AUTHORIZE && getActionType()!=ClientConstants.ACTIONTYPE_REJECT) {
    HashMap whereMap = new HashMap();
    whereMap.put("TRANS_ID", getLblTransactionId());
    //screen lock
    
    whereMap.put("USER_ID",ProxyParameters.USER_ID);
    whereMap.put("TRANS_DT", currDt.clone());
    whereMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
    List lstlock=ClientUtil.executeQuery("selectauthorizationLock", whereMap);
    if(lstlock !=null && lstlock.size()>0)
    {
    HashMap map=new HashMap();
    StringBuffer open=new StringBuffer();
    for(int i=0;i<lstlock.size();i++){
    map=(HashMap)lstlock.get(i);
    open.append ("\n"+"User Id  :"+" ");
    open.append(CommonUtil.convertObjToStr(map.get("OPEN_BY"))+"\n");
    open.append("Mode Of Operation  :" +" ");
    open.append(CommonUtil.convertObjToStr(map.get("MODE_OF_OPERATION"))+" ");            
    }
    ClientUtil.showMessageWindow("already opened by"+open);
    
    return;
    }
    //
    whereMap.put("TRANS_DT", currDt.clone());
    whereMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
    List lst = ClientUtil.executeQuery("getCashAuthorizeStatus", whereMap);
    if (lst!=null && lst.size()>0) {
    whereMap = (HashMap) lst.get(0);
    String authStatus = CommonUtil.convertObjToStr(whereMap.get("AUTHORIZE_STATUS"));
    String authBy = CommonUtil.convertObjToStr(whereMap.get("AUTHORIZE_BY"));
    if (!authStatus.equals("")) {
    actionType = ClientConstants.ACTIONTYPE_FAILED;
    throw new TTException("This transaction already "+authStatus.toLowerCase()+" by "+authBy);
    }
    }
    }*/
    // End

    //If actionType such as NEW, EDIT, DELETE, then proceed
         /*   if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
    //If actionType has got propervalue then doActionPerform, else throw error
    if(getAuthorizeMap() != null){
    doActionPerform("");
    }
    } else {
    // log.info("Action Type Not Defined In setCashTransactionTO()");
    }
    } catch (Exception e) {
    //  log.info("Error In doAction()");
    //  setResult(ClientConstants.ACTIONTYPE_FAILED);
    e.printStackTrace();
    if(e instanceof TTException) {
    exception = (TTException) e;
    }
    }
    
    // If TT Exception
    /*    if (exception != null) {
    HashMap exceptionHashMap = exception.getExceptionHashMap();
    System.out.println(exception+"exceptionmap###"+exceptionHashMap);
    if (exceptionHashMap != null) {
    ArrayList list = (ArrayList) exceptionHashMap.get(CommonConstants.EXCEPTION_LIST);
    //(list.get(0) instanceof String && "IB".equalsIgnoreCase((String)list.get(0))) ||
    if(list.size()==1 && list.get(0) instanceof String && ((String)list.get(0)).startsWith("SUSPECIOUS")||CommonUtil.convertObjToStr(list.get(0)).equals("AED")||CommonUtil.convertObjToStr(list.get(0)).equals("ESL")||
    CommonUtil.convertObjToStr(list.get(0)).equals("AEL")) {
    Object[] dialogOption = {"Exception","Cancel"};
    TTException e=new  TTException(CommonUtil.convertObjToStr(list.get(0)));
    
    parseException.setDialogOptions(dialogOption);
    if(parseException.logException(e, true)==0) {//exception
    try{
    setResult(actionType);
    doActionPerform("EXCEPTION");
    } catch(Exception e1) {
    log.info("Error In doAction()");
    e1.printStackTrace();
    if(e1 instanceof TTException) {
    Object[] dialogOption1 = {"OK"};
    parseException.setDialogOptions(dialogOption1);
    exception = (TTException) e1;
    parseException.logException(exception, true);
    }
    }
    }
    Object[] dialogOption1 = {"OK"};
    parseException.setDialogOptions(dialogOption1);
    } 
    else  if(CommonUtil.convertObjToStr(list.get(0)).equals("MIN")){
    Object[] dialogOption = {"Continue","Cancel"};
    parseException.setDialogOptions(dialogOption);
    if(parseException.logException(exception, true)==0) {
    try{
    setResult(actionType);
    doActionPerform("EXCEPTION");
    } catch(Exception e1) {
    log.info("Error In doAction()");
    e1.printStackTrace();
    if(e1 instanceof TTException) {
    Object[] dialogOption1 = {"OK"};
    parseException.setDialogOptions(dialogOption1);
    exception = (TTException) e1;
    parseException.logException(exception, true);
    }
    }
    }
    Object[] dialogOption1 = {"OK"};
    parseException.setDialogOptions(dialogOption1);
    }
    else {
    parseException.logException(exception, true);
    }
    } else { // To Display Transaction No showing String message
    parseException.logException(exception, true);
    setResult(actionType);
    }
    }*/
    /* }*/
    /**
     * Getter for property asAnWhenCustomer.
     * @return Value of property asAnWhenCustomer.
     */
    //  public java.lang.String getAsAnWhenCustomer() {
    //     return asAnWhenCustomer;
    //  }
    /**
     * Setter for property asAnWhenCustomer.
     * @param asAnWhenCustomer New value of property asAnWhenCustomer.
     */
    // public void setAsAnWhenCustomer(java.lang.String asAnWhenCustomer) {
    //     this.asAnWhenCustomer = asAnWhenCustomer;
    // }
    /** To perform the necessary action */
    /*  private void doActionPerform(String parameter) throws Exception{
    // log.info("In doActionPerform()");
    final HashMap data = new HashMap();
    String tranIdList="TRANS IDs\n";
    HashMap proxyReturnMap = new HashMap();
    data.put("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER","ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER");
    if(getAuthorizeMap()!=null){
    setAsAnWhenCustomer("N");
    }*/
    /*    data.put(CommonConstants.MODULE, getModule());
    data.put(CommonConstants.SCREEN, getScreen());
    data.put("PRODUCTTYPE",prod_Type);
     */
    //   data.put(CommonConstants.SELECTED_BRANCH_ID,ProxyParameters.BRANCH_ID );
    //  if(getAuthorizeMap()!=null) {
    //  data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
    //  System.out.println("observableside:  datamap"+data);
    //  proxyReturnMap = proxy.execute(data,operationMap);
    //  }
    //  System.out.println("proxyReturnMap data :"+proxyReturnMap);
    // }*/
    private void btnApproveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApproveActionPerformed
        // TODO add your handling code here:
        if (partsC.size() > 0) {
            if (isDenominationTallied()) {// || !isDenominationTallied()
                getUpdateApproval();
                setModified(false);
                partsC.clear();
               // cboStatusActionPerformed(null);
                btnRecieptActionPerformed1("CREDIT");
                btnRecieptActionPerformedDebit("DEBIT");
                transCount();
                btnRefreshActionPerformed(null);

            }
        } else {
            displayAlert("Please select Receipts or Payments!!!");
            return;
        }
    }//GEN-LAST:event_btnApproveActionPerformed

    private boolean isDenominationTallied() {
        boolean tallied = true;
        String isDen = "N";
        try {
            HashMap singleAuthorizeMap = new HashMap();
            List aList = ClientUtil.executeQuery("getIsDenominationAllowed", singleAuthorizeMap);
            for (int i = 0; i < aList.size(); i++) {
                HashMap map = (HashMap) aList.get(i);
                if (map.get("DENOMINATION_ALLOWED") != null) {
                    isDen = map.get("DENOMINATION_ALLOWED").toString();

                }
            }

            if (isDen != null && isDen.equals("Y")) {
                boolean Dflag = false, Cflag = false;
                double transAmt = CommonUtil.convertObjToDouble(lblAmount.getText().replaceAll(",", "")).doubleValue();
                double receiptDenomination = 0;
                double paymentDenomination = 0;
                for (int i = 0; i <= 21; i++) {
                    //  Cflag=false; 
                    if (tblData.getModel().getValueAt(i, 2) != null
                            && tblData.getModel().getValueAt(i, 3) != null) {
                        System.out.println("trans_pid 111=== " + trans_pid);
                        // if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
                        //totalPay += ((Double) tblData.getModel().getValueAt(i,2)).doubleValue();
                        //System.out.println("33-=="+tblData.getModel().getValueAt(i,1));
                        //System.out.println("22==="+tblData.getModel().getValueAt(i,2));
                        String amount = tblData.getModel().getValueAt(i, 3).toString().replaceAll(",", "");
                        receiptDenomination += CommonUtil.convertObjToDouble(amount).doubleValue();
                        System.out.println("receiptDenomination 111=== " + receiptDenomination);
                        Cflag = true;
                        // }
                    }
                    //if(tblData.getModel().getValueAt(i,2).equals("")
                    //  || tblData.getModel().getValueAt(i,2).equals("0"))
                    //   Cflag=false; 
                }
                for (int i = 0; i <= 21; i++) {
                    //Dflag=false; 
                    if (tblData.getModel().getValueAt(i, 5) != null
                            && tblData.getModel().getValueAt(i, 6) != null) {
                        System.out.println("trans_pid 222=== " + trans_pid);
                        //  if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
                        String amount = tblData.getModel().getValueAt(i, 6).toString().replaceAll(",", "");
                        paymentDenomination += CommonUtil.convertObjToDouble(amount).doubleValue();
                        System.out.println("paymentDenomination 111=== " + paymentDenomination);
                        Dflag = true;
                        //  }
                    }
                    //  if(tblData.getModel().getValueAt(i,5).equals("")
                    //      || tblData.getModel().getValueAt(i,5).equals("0"))
                    //      Dflag=false; 

                }
                double denominationAmount = 0.0;
                if (receiptDenomination > 0.0 && lblTransType.getText().equals("CREDIT")) {
                    transMode = "Receipts";
                } else if (paymentDenomination > 0.0 && lblTransType.getText().equals("DEBIT")) {
                    transMode = "Payments";
                } else if (lblTransType.getText().equals("Payable")) {
                    transMode = "Payable";
                } else if (lblTransType.getText().equals("Receivable")) {
                    transMode = "Receivable";
                }
                System.out.println("receiptDenomination === " + receiptDenomination);
                System.out.println("paymentDenomination === " + paymentDenomination);
                double amt = 0, amtde = 0;
                if (transMode.equals("Receipts") || transMode.equals("Receivable")) {
                    denominationAmount = receiptDenomination - paymentDenomination;
                } else //if(transMode.equals("Payments"))
                {
                    denominationAmount = paymentDenomination - receiptDenomination;
                }
                if (transMode.equals("Payable") || transMode.equals("Receivable")) {
                    amtde = receiptDenomination + paymentDenomination;
                }

                System.out.println("amt 4444444444444 === " + amt);
                // denominationAmount=Math.abs(denominationAmount);
                //  transAmt=Math.abs(transAmt);


                if (receiptDenomination > 0.0 && lblTransType.getText().equals("CREDIT")) {
                    amt = denominationAmount - transAmt;
                } else if (paymentDenomination > 0.0 && lblTransType.getText().equals("DEBIT")) {
                    amt = denominationAmount - transAmt;
                } else {
                    System.out.println("totVal====" + totVal);
                    System.out.println("denominationAmount====" + amtde);
                    amt = amtde - totVal;
                }
                System.out.println("Cflag====" + Cflag + " Dflag === " + Dflag);
                if (paymentDenomination <= transAmt && paymentDenomination == 0.0) {
                    if ((lblTransType.getText().equals("DEBIT") || lblTransType.getText().equals("Payable"))) {
                        ClientUtil.showAlertWindow("Plaese enter payment details!!!");
                        Cflag = false;
                        tallied = false;
                    }
                }
                if (receiptDenomination <= transAmt && receiptDenomination == 0.0) {
                    if ((lblTransType.getText().equals("CREDIT") || lblTransType.getText().equals("Receivable"))) {
                        ClientUtil.showAlertWindow("Plaese enter Receipt details!!!");
                        Dflag = false;
                        tallied = false;
                    }
                }
//             double denominationAmount = receiptDenomination-paymentDenomination;
                System.out.println("denominationAmount 676767 === " + denominationAmount);
                System.out.println("transAmt 676767 === " + transAmt);
                if (denominationAmount != transAmt) {
                    ClientUtil.showAlertWindow("Denomination Amount : " + denominationAmount
                            + "\nTransaction Amount :" + transAmt + "\n"
                            + "Balance Amount=" + amt);
                    tallied = false;
                    // return;
                }
            } else {
                tallied = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tallied;
    }

    public void getUpdateReject() {
        if (partsC != null && partsC.size() >= 1) {
            System.out.println("klahdfjkah" + partsC.size());
            //  String[] parts = trans_pid.split(":");
            for (int i = 0; i < partsC.size(); i++) {
                System.out.println("klahdfjkah11111" + partsC.size());
                HashMap singleAuthorizeMap11 = new HashMap();
                singleAuthorizeMap11.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                singleAuthorizeMap11.put("TRANS_DT", currDt);
                singleAuthorizeMap11.put(CommonConstants.TRANS_ID, partsC.get(i).toString());
                singleAuthorizeMap11.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                ClientUtil.execute("rejectCashier", singleAuthorizeMap11);

            }
            // insertIntoDenaomination("multiple");
            enableDisableButtons(true);
            tblRecieptMouseClicked(null);
            clearTransDetails();
        }
    }

    public void getUpdateApproval() {
        try {
            if (partsC != null && partsC.size() > 1) {
                //  String[] parts = trans_pid.split(":");
                for (int i = 0; i < partsC.size(); i++) {
                    HashMap singleAuthorizeMap11 = new HashMap();
                    singleAuthorizeMap11.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                    singleAuthorizeMap11.put("TRANS_DT", currDt);
                    singleAuthorizeMap11.put(CommonConstants.TRANS_ID, partsC.get(i).toString());
                    singleAuthorizeMap11.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                    ClientUtil.execute("approveCashier", singleAuthorizeMap11);
                    
                    //added by sreekrisnan for update deposit closing transaction togethr(int & prpl)
                    authorizeDepositClosure(partsC.get(i).toString());
                }
                insertIntoDenaomination("multiple");
                enableDisableButtons(true);
                //getAcHdTree("PENDING");
                btnRecieptActionPerformed1("CREDIT");
                clearTransDetails();
            } else if (partsC != null && partsC.size() == 1) {
                for (int i = 0; i < partsC.size(); i++) {
                    HashMap singleAuthorizeMap = new HashMap();
                    singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                    singleAuthorizeMap.put("TRANS_DT", currDt);
                    singleAuthorizeMap.put(CommonConstants.TRANS_ID, partsC.get(i).toString());
                    singleAuthorizeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);

                    ClientUtil.execute("approveCashier", singleAuthorizeMap);
                    authorizeDepositClosure(partsC.get(i).toString());
                    insertIntoDenaomination("single");
                    // ((DefaultTreeModel)treData.getModel()).removeNodeFromParent(selectedNode);
                    //tblRecieptMouseClicked(null);
                    clearTransDetails();
                }
            }//END ELSE  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void authorizeDepositClosure(String tranId) {
        try {
            //First check whether there is closure transaction is there or not...
            HashMap depsoitCloseMap = new HashMap();
            HashMap depsoitCloseMap1 = new HashMap();
            depsoitCloseMap.put("TRANS_DT", currDt);
            depsoitCloseMap.put(CommonConstants.TRANS_ID, tranId);
            depsoitCloseMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            depsoitCloseMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            //depsoitCloseMap.put("TRANS_TYPE", CommonConstants.DEBIT);
            List depositClosureList = ClientUtil.executeQuery("getDepositClosureTransaciton", depsoitCloseMap);
            if (depositClosureList != null && depositClosureList.size() > 0) {
                depsoitCloseMap1 = (HashMap) depositClosureList.get(0);
                if(depsoitCloseMap1.get("AUTHORIZE_STATUS_2")!=null){
                     //Updating both interest and principle transaction using the link batch id
                    HashMap updateMap = new HashMap();
                    updateMap.put("TRANS_DT", currDt);
                    updateMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                    updateMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                    updateMap.put("LINK_BATCH_ID", depsoitCloseMap1.get("LINK_BATCH_ID")); 
                    ClientUtil.execute("DepositClosureCashierApprove", updateMap);
                   
                }else{       
                    ClientUtil.execute("DepositClosureRecievCashierApprove", depsoitCloseMap); 
                }
            }
            //end
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getMultiplyValues() {
        try {
            //System.out.println("R= "+row +" C="+col);
            //JOptionPane.showMessageDialog(null,tblData.getValueAt(row,col).toString());
            //  String AccNo=tblData.getValueAt(row,0).toString();
            //  String Name=tblData.getValueAt(row,1).toString();
            //   String Amt=tblData.getValueAt(row,3).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearTransDetails() {
        // lblScheme.setText("");
        // lblName.setText("");
        // lblTransId.setText("");
        lblTransType.setText("");
        lblAmount.setText("");
        lblRecTotal.setText("");
        lblPayTotal.setText("");
        // lblPostedBy.setText("");
        // lblAuthBy.setText("");
        // lblParticulars.setText("");
        //  lblPanNo.setText("");
        //  lblAcNo.setText("");
        //   lblTkNo.setText("");
        //   lblInstType.setText("");
        //   lblInstNo.setText("");
        //   lblInstDate.setText("");
        //  lblClearBal.setText("");
        //   lblOpBal.setText("");
        lblReceipt.setText("");
        lblPayment.setText("");
        // lblClosingBal.setText("");bb
        initTableData();
        setDisableReject();
        // chkUniqueId="";
        //    flagChk=false;
        //  lblDrCrBalance.setText("");
        //  partsC.clear();
    }

    public void setOpBalDetails(ArrayList trnsId) {
        try {
            /*    HashMap singleAuthorizeMapOpBal = new HashMap();
            singleAuthorizeMapOpBal.put("TRANS_DT", currDt);
            singleAuthorizeMapOpBal.put(CommonConstants.BRANCH_ID,  ProxyParameters.BRANCH_ID);
            //  List aListOp= ClientUtil.executeQuery("getOpAmount", singleAuthorizeMapOpBal);
            List aListOp= ClientUtil.executeQuery("getOpBalAmount", singleAuthorizeMapOpBal);
            lblOpBal.setText("0.00");
            System.out.println("aListOp ---------------- "+aListOp);
            System.out.println("aListOp ------33---------- "+aListOp.size());
            if(aListOp.size()>0 &&  aListOp.get(0)!=null)
            {
            HashMap mapop=(HashMap)aListOp.get(0);
            Opamount=mapop.get("OPAMOUNT").toString();
            if(Opamount!=null && !Opamount.equalsIgnoreCase(""))
            lblOpBal.setText(formatCrore(Opamount));
            // else 
            
            }*/
            //  System.out.println("trnsId ======================="+trnsId);
           /*String transId="";
            if(trnsId.size()==1)
            transId=trnsId.get(0).toString();
            else
            {
            for(int i=0;i<trnsId.size();i++)
            {
            transId=transId+",";
            }
            transId = transId.substring(transId.length()-1); 
            }*/
            double recAmt = 0;
            double payAmt = 0;
            String transType = "";
            for (int i = 0; i < trnsId.size(); i++) {
                HashMap singleAuthorizeMap1 = new HashMap();
                singleAuthorizeMap1.put("TRANS_DT", currDt);
                singleAuthorizeMap1.put(CommonConstants.TRANS_ID, trnsId.get(i).toString());
                // singleAuthorizeMap1.put("TRANS_TYPE", "CREDIT");
                singleAuthorizeMap1.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                List aList1 = ClientUtil.executeQuery("getSumPayRecAmountNew", singleAuthorizeMap1);//getSumPayRecAmountUnion
                HashMap map = (HashMap) aList1.get(0);
                if (map.get("AMOUNT") != null) {//REC_AMOUNT
                    amount1 = map.get("AMOUNT").toString();
                    transType = map.get("TRANS_TYPE").toString();
                    if ((amount1 != null && !amount1.equalsIgnoreCase("")) && (transType != null && transType.equals("CREDIT"))) {
                        recAmt = recAmt + CommonUtil.convertObjToDouble(amount1);
                    }
                    if ((amount1 != null && !amount1.equalsIgnoreCase("")) && (transType != null && transType.equals("DEBIT"))) {
                        payAmt = payAmt + CommonUtil.convertObjToDouble(amount1);
                    }
                }
                /*  HashMap singleAuthorizeMap1 = new HashMap();
                singleAuthorizeMap1.put("TRANS_DT", currDt);
                singleAuthorizeMap1.put(CommonConstants.TRANS_ID, trnsId.get(i).toString());
                singleAuthorizeMap1.put("TRANS_TYPE", "CREDIT");
                singleAuthorizeMap1.put(CommonConstants.BRANCH_ID,  ProxyParameters.BRANCH_ID);
                List aList1= ClientUtil.executeQuery("getSumPayRecAmount", singleAuthorizeMap1);//getSumPayRecAmountUnion
                HashMap map=(HashMap)aList1.get(0);
                if(map.get("AMOUNT")!=null) {//REC_AMOUNT
                amount1=map.get("AMOUNT").toString();
                if(amount1!=null && !amount1.equalsIgnoreCase(""))
                recAmt=recAmt+CommonUtil.convertObjToDouble(amount1);
                }
                
                HashMap singleAuthorizeDe= new HashMap();
                singleAuthorizeDe.put("TRANS_DT", currDt);
                singleAuthorizeDe.put(CommonConstants.TRANS_ID, trnsId.get(i).toString());
                singleAuthorizeDe.put("TRANS_TYPE", "DEBIT");
                singleAuthorizeDe.put(CommonConstants.BRANCH_ID,  ProxyParameters.BRANCH_ID);
                List aListD= ClientUtil.executeQuery("getSumPayRecAmount", singleAuthorizeDe);
                HashMap map1=(HashMap)aListD.get(0);
                if(map1.get("AMOUNT")!=null) {
                amount1=map1.get("AMOUNT").toString();
                if(amount1!=null && !amount1.equalsIgnoreCase(""))
                payAmt=payAmt+CommonUtil.convertObjToDouble(amount1);
                }*/

            }
            //debit
            // for(int i=0;i<trnsId.size();i++)
            /// {

            //   }
            lblReceipt.setText(formatCrore(String.valueOf(recAmt)));
            lblPayment.setText(formatCrore(String.valueOf(payAmt)));
            //Cloasing bal calc
             /*   String opBal=lblOpBal.getText();
            String Rec=lblReceipt.getText();
            String Pay=lblPayment.getText();
            
            if(opBal!=null && !opBal.equals("")) {
            opBal=opBal.replaceAll(",","");
            Rec=Rec.replaceAll(",","");
            Pay=Pay.replaceAll(",","");
            // System.out.println("opBal======"+opBal);
            BigDecimal opBal1 = new BigDecimal(opBal);
            BigDecimal Rec1 = new BigDecimal(Rec);
            BigDecimal Pay1 = new BigDecimal(Pay);
            BigDecimal v1=opBal1.add(Rec1);
            //BigDecimal v2=v1.subtract(Pay1);
            //BigDecimal v2=v1.add(Pay1);bb
            
            // double clsbal=Long.parseLong(opBal)+Long.parseLong(Rec)
            //  -Long.parseLong(Pay);
            // clsbal=String.valueOf(v1)    ;//v2
            //   System.out.println("clsbal======"+clsbal);
            //  lblClosingBal.setText(formatCrore(clsbal));bb
            // lblClosingBal.setText(String.valueOf("0"));
            }
            else {
            lblClosingBal.setText("0.00" );
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDisplayScheme(String transId, String prod_Id, String ac_HD_No, String auth_remarks) {
        System.out.println("schList ==================" + partsC.size());
        //  if(partsC.size()==1 || (auth_remarks!=null && !auth_remarks.equals("")))
        //  {
        HashMap singleAuthorizeMap = new HashMap();
        singleAuthorizeMap.put("TRANS_ID", transId);
        singleAuthorizeMap.put("TRANS_DT", currDt);
        singleAuthorizeMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        List aList = ClientUtil.executeQuery("getTransIdFromScheme", singleAuthorizeMap);
        // for(int k=0;k<aList.size();k++) {
        HashMap map1 = (HashMap) aList.get(0);
        if (map1.get("PROD_TYPE") != null) {
            // if(map1.get("PROD_TYPE").toString().equals("TL") || (auth_remarks!=null && !auth_remarks.equals("")))
            // {
            HashMap singleAuthorizeMapOA = new HashMap();
            singleAuthorizeMapOA.put(CommonConstants.PRODUCT_ID, prod_Id == null ? ac_HD_No : prod_Id);
            //  List aListOA= ClientUtil.executeQuery("getSchemeName"+prod_Type, singleAuthorizeMapOA);
            System.out.println("PPPPP====" + "getSchemeName" + prod_Type);
            // for(int j=0;j<aListOA.size();j++) {
            // HashMap mapOA=(HashMap)aListOA.get(0);
            // sc_Name=mapOA.get("PROD_DESC").toString();
            System.out.println("PPPPPsc_Name" + sc_Name);
            if (sc_Name != null && !sc_Name.equalsIgnoreCase("")) {
//                                        lblScheme.setText(sc_Name);
                //  lblScheme.setToolTipText(sc_Name);
                //   System.out.println("IN77777====="+lblScheme);
            }
            // }
            //}
        }
        //  }
        // }
    }
    ArrayList schList = new ArrayList();

    public void getDisplayTransDetails(ArrayList trnsId) {

        //System.out.println("trnsId.size()INNNNn===" + trnsId.size());
        // amount = "";
        // String opBalance = "", countVal = "", ac_No = "";
        // System.out.println("trnsId 000000+:"+trnsId); 
        //if (trnsId != null && trnsId.size() > 0) {
         //   if (trnsId.contains("Payments") || trnsId.contains("Receipts")) {
        //        return;
          //  }
           // System.out.println("trnsId.size()===" + trnsId.size());
            /*if (trnsId.size() == 1) {
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                singleAuthorizeMap.put("TRANS_DT", currDt);
                for (int i = 0; i < trnsId.size(); i++) {
                    singleAuthorizeMap.put(CommonConstants.TRANS_ID, trnsId.get(i).toString());
                }
                singleAuthorizeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                List aList = ClientUtil.executeQuery("getCashTransData", singleAuthorizeMap);
                System.out.println("aListIN PRINT+++++++000000000+:" + aList);
                System.out.println("aListIN PRINSSSSSSSSSSSSSSSSSS:===" + aList.size());
                String type = "";
                if (aList.size() == 1) {
                    // for (int i = 0; i < aList.size(); i++) {
                    HashMap map = (HashMap) aList.get(0);
                    amount = map.get("AMOUNT").toString();
                    System.out.println("amountttttttttttttt" + amount);
                    lblAmount.setText(amount);
                    type = map.get("TRANS_TYPE").toString();
                    if (type.equals("DEBIT")) {
                        lblTransType.setText("Payable");
                    } else {
                        lblTransType.setText("Recievable");// }
                    }
                } else {
                    double amt = 0;
                    for (int i = 0; i < aList.size(); i++) {
                        HashMap map = (HashMap) aList.get(i);
                        amount1 = map.get("AMOUNT").toString();
                        type = map.get("TRANS_TYPE").toString();
                        amt = amt + new Double(amount1);


                    }
                    lblAmount.setText(String.valueOf(amt));
                    System.out.println("amountttttttttttttt" + amt);
                    if (type.equals("DEBIT")) {
                        lblTransType.setText("Payable");
                    } else {
                        lblTransType.setText("Recievable");//
                    }
                }
            } else {

                if (trnsId != null && trnsId.size() > 1) {
                    //tring[] parts = trnsId.split(":");
                    double amtTotal = 0;
                    double amt = 0;
                    double debtAmt = 0;
                    double creditAmt = 0;
                    String transIdFirst = "", transIdLast = "";
                    String type = "";

                    System.out.println("trnsId IN ELSE==" + trnsId);

                    for (int t = 0; t < trnsId.size(); t++)//babu comm on 09-09-13
                    {

                        HashMap singleAuthorizeMap = new HashMap();
                        singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                        singleAuthorizeMap.put("TRANS_DT", currDt);
                        singleAuthorizeMap.put(CommonConstants.TRANS_ID, trnsId.get(t));
                        singleAuthorizeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                        List aList = ClientUtil.executeQuery("getCashTransData", singleAuthorizeMap);
                        HashMap mapA = (HashMap) aList.get(0);
                        System.out.println("lopppppppppppppppppppppppppppp");
                        amount = mapA.get("AMOUNT").toString();
                        System.out.println("amttttttttttttttttttttttt" + amount);

                        type = mapA.get("TRANS_TYPE").toString();
                        if (type.equals("DEBIT")) {
                            debtAmt = (debtAmt + new Double(amount));
                        } else {
                            creditAmt = creditAmt + new Double(amount);
                        }
                    }
                    amt = creditAmt - debtAmt;
                    // lblAmount.setText(String.valueOf(amt));
                    if (amt < 0) {
                        lblAmount.setText(String.valueOf(-1 * amt));
                        lblTransType.setText("Payable");
                    } else {
                        lblTransType.setText("Recievable");
                    }
                    lblAmount.setText(String.valueOf(amt));
                }
            }*/
        double debitAmt=0.00;
        double creditAmt=0.00;
        double amt=0.00;
        String type="";
        HashMap mapA = new HashMap();
            for(int i=0;i<trnsId.size();i++)
            {
                     HashMap singleAuthorizeMap = new HashMap();
                        singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                        singleAuthorizeMap.put("TRANS_DT", currDt);
                        singleAuthorizeMap.put(CommonConstants.TRANS_ID, trnsId.get(i));
                        singleAuthorizeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                        List aList = ClientUtil.executeQuery("getCashTransDataMap", singleAuthorizeMap); 
                        if (aList != null && aList.size() > 0) {
                            mapA = (HashMap) aList.get(0);
                        }else{
                            HashMap depositClosureMap = new HashMap();
                            List depositClosureList = ClientUtil.executeQuery("getDepositClosureTransaciton", singleAuthorizeMap); 
                            if (depositClosureList != null && depositClosureList.size() > 0) {
                                depositClosureMap = (HashMap) depositClosureList.get(0);
                                if(depositClosureMap.get("AUTHORIZE_STATUS_2")!=null){
                                    singleAuthorizeMap.put("LINK_BATCH_ID", depositClosureMap.get("LINK_BATCH_ID"));
                                    singleAuthorizeMap.put("TRANS_TYPE", CommonConstants.DEBIT);
                                     singleAuthorizeMap.put(CommonConstants.TRANS_ID, trnsId.get(i));
                                    List depositClosureList2 = ClientUtil.executeQuery("getCashTransDepositClosure", singleAuthorizeMap);
                                    if (depositClosureList2 != null && depositClosureList2.size() > 0) {
                                        mapA = (HashMap) depositClosureList2.get(0);
                                    }
                                }else{
                                    singleAuthorizeMap.put(CommonConstants.TRANS_ID, trnsId.get(i));
                                     List depositClosureList1 = ClientUtil.executeQuery("getCashTransDepositClosureRecive", singleAuthorizeMap);
                                     if (depositClosureList1 != null && depositClosureList1.size() > 0) {
                                         mapA = (HashMap) depositClosureList1.get(0);
                                     }
                               }
                           }
                        }
                       
                        System.out.println("lopppppppppppppppppppppppppppp");
                        amount = mapA.get("AMOUNT").toString();
                        System.out.println("amttttttttttttttttttttttt" + amount);
                        type=mapA.get("TRANS_TYPE").toString();
                        if(type.equals("DEBIT"))
                                {
                                        debitAmt=debitAmt+new Double(amount);
                                        lblPayment.setText(formatCrore(String.valueOf(debitAmt)));
                
                                }
                        else{
                                        creditAmt=creditAmt+new Double(amount); 
                                        lblReceipt.setText(formatCrore(String.valueOf(creditAmt)));
                            }
           }
                        amt=creditAmt-debitAmt;
                         if(amt<0) 
                         {
                                lblAmount.setText(formatCrore(String.valueOf(-1*amt)));
                                lblTransType.setText("Payable");
                         }   else {  
                        
                         lblAmount.setText(formatCrore(String.valueOf(amt)));
                         lblTransType.setText("Receivable");
                         }if(amt==0){
                         lblAmount.setText("");
                         lblTransType.setText("");
                         }
                         
        
    }

    public void transCount() {
        try {
            System.out.println("contrrrrrrrrrrrrrrrrr" + tblReciept.getRowCount());
            System.out.println("contrrrrrrrrrrrrrrrrr" + tblPayment.getRowCount());
            lblTotReceipts.setText(CommonUtil.convertObjToStr(tblReciept.getRowCount()));
            lblTotPayments.setText(CommonUtil.convertObjToStr(tblPayment.getRowCount()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCustmerName(String trans_id, Date trans_dt, String branchId) {
        if (partsC.size() >= 1) {
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            singleAuthorizeMap.put("TRANS_DT", trans_dt);
            singleAuthorizeMap.put(CommonConstants.TRANS_ID, trans_id);

            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            List aList = ClientUtil.executeQuery("getCashTransData", singleAuthorizeMap);
            // System.out.println("aList  ---------- "+aList);
            String act_NumVal = "";
            String transIdFirst = "", transIdLast = "";
            for (int i = 0; i < aList.size(); i++) {
                HashMap map = (HashMap) aList.get(i);
                if (map.get("LINK_BATCH_ID") != null) {
                    act_NumVal = map.get("LINK_BATCH_ID").toString();
                }

            }

            //if()

            System.out.println("act_NumVal ---------- " + act_NumVal);
            if (act_NumVal != null) {
                HashMap singleAuthorizeMapCName = new HashMap();
                singleAuthorizeMapCName.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                singleAuthorizeMapCName.put(CommonConstants.AUTHORIZEDT, trans_dt);
                singleAuthorizeMapCName.put(CommonConstants.ACT_NUM, act_NumVal);
                singleAuthorizeMapCName.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                List aListCName = ClientUtil.executeQuery("getCustomerN", singleAuthorizeMapCName);
                for (int q = 0; q < aListCName.size(); q++) {
                    HashMap mapCName = (HashMap) aListCName.get(q);
                    if (mapCName.get("NAME") != null) {
                        cname = mapCName.get("NAME").toString();
                        if (cname != null && !cname.equalsIgnoreCase("")) {
//                                    lblName.setText(cname);
                        }
                    }

                }
            }
        }
    }
    private void cboStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboStatusActionPerformed
        // TODO add your handling code here:
        // String status = cboStatus.getSelectedItem().toString();
        /* if(status!=null && status.equalsIgnoreCase("Authorized")) {
        enableDisableButtons(false);
        // getAcHdTree("AUTHORIZED");
        }
        if(status!=null && status.equalsIgnoreCase("Pending")) {
        enableDisableButtons(true);
        // getAcHdTree("PENDING");
        }
        if(status!=null && status.equalsIgnoreCase("Cancelled")) {
        enableDisableButtons(false);
        // getAcHdTree("REJECTED");
        }*/
        /* if (status != null && status.equalsIgnoreCase("Receipts")) {
        btnRecieptActionPerformed1("CREDIT");
        btnReject.setEnabled(true);
        }
        if (status != null && status.equalsIgnoreCase("Payments")) {
        btnRecieptActionPerformed1("DEBIT");
        btnReject.setEnabled(false);
        }
         */
    }//GEN-LAST:event_cboStatusActionPerformed
    public void setDisableReject() {
        // btnReject.setEnabled(false);
        String status = cboStatus.getSelectedItem().toString();
        if (status != null && status.equalsIgnoreCase("Receipts")) {
            btnReject.setEnabled(true);
        }
        if (status != null && status.equalsIgnoreCase("Payments")) {
            btnReject.setEnabled(false);
        }
    }

    private void enableDisableButtons(boolean val) {
        btnApprove.setEnabled(val);

        if (lblTransType.getText().equals("CREDIT")) {
            btnReject.setEnabled(true);
        }
        if (lblTransType.getText().equals("DEBIT")) {
            btnReject.setEnabled(false);
        }
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

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

private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
// TODO add your handling code here:
    clearTransDetails();
    partsC.clear();
    this.dispose();
}//GEN-LAST:event_btnExitActionPerformed

private void keypressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keypressed
// TODO add your handling code here:
    TableModel model = tblData.getModel();

    // String quantity = model.getValueAt(row, 1).toString();
    if (evt.getKeyCode() == 127) {

        int toCol = tblData.getSelectedColumn();
        int toRow = tblData.getSelectedRow();
        System.out.println("toCol ===== " + toCol + " toRow====" + toRow);
        //  System.out.println("toDelete ===== "+toDelete);
        // if(toDelete==2)
        if (toCol == 2 || toCol == 5) {
            model.setValueAt("0", toRow, toCol);
        }
        //if(toDelete==5)
        //    model.setValueAt("0",toDelete, 5); 
        // ((DefaultTableModel)tblData.getModel()).fireTableDataChanged();
        //      tblData.repaint();
        // }
    }
}//GEN-LAST:event_keypressed

private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
// TODO add your handling code here:
    // Enumeration e = receipts.children();
                                         /* while (e.hasMoreElements()) {
    DefaultMutableTreeNode dmt=(DefaultMutableTreeNode)e.nextElement();
    CheckBoxNode chk = (CheckBoxNode)dmt.getUserObject();
    chk.setSelected(false);
    
    }
    Enumeration e1 = payments.children();
    while (e1.hasMoreElements()) {
    DefaultMutableTreeNode dmt1=(DefaultMutableTreeNode)e1.nextElement();
    CheckBoxNode chk1 = (CheckBoxNode)dmt1.getUserObject();
    chk1.setSelected(false);
    
    }*/
    // for (int j = 0; j < tblReciept.getRowCount(); j++) {
    //    tblReciept.setValueAt(false, j, 0);
    // }

    clearTransDetails();
    partsC.clear();
    //String nodeVal = CommonUtil.convertObjToStr(tblReciept.getSelectedRow());
    //.remove(nodeVal);
    System.out.println("##########@@@@@@@@@" + tblReciept.getRowCount());

    //boolean chk = ((Boolean) tblReciept.getValueAt(tblReciept.getSelectedRow(), 0)).booleanValue();
    //System.out.println("================="+chk);
    for (int i = 0; i < tblReciept.getRowCount(); i++) {
        tblReciept.setValueAt(false, i, 0);
    }
    for (int i = 0; i < tblPayment.getRowCount(); i++) {
        tblPayment.setValueAt(false, i, 0);
    }

    //         treData.updateUI();
    btnRefreshActionPerformed(null);
}//GEN-LAST:event_btnClearActionPerformed

private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
// TODO add your handling code here:
    btnRecieptActionPerformed1("CREDIT");
    btnRecieptActionPerformedDebit("DEBIT");
    transCount();
    lblShadawBal.setText("0.00");
    lblClosingBal.setText("0.00");
    lblRecTotal.setText("0.00");
    lblPayTotal.setText("0.00");
    HashMap singleAuthorizeMap = new HashMap();
    singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
    singleAuthorizeMap.put("TRANS_DT", currDt);
    singleAuthorizeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
    List aList = ClientUtil.executeQuery("getShadowBalance", singleAuthorizeMap);
//      if(aList!=null)
//      {
//         HashMap map=(HashMap)aList.get(0);
//         if(map.get("AMOUNT")!=null )//&& map.get("AUTHORIZE_REMARKS").toString().startsWith("CU"))
//         {
//             lblShadawBal.setText(formatCrore(map.get("AMOUNT").toString()));
//         }
//      }
     /* List aList1= ClientUtil.executeQuery("getOpAmount", singleAuthorizeMap);
    if(aList1!=null )
    {
    HashMap map1=(HashMap)aList1.get(0);
    if(map1.get("OPAMOUNT")!=null )
    lblClosingBal.setText(formatCrore(map1.get("OPAMOUNT").toString()));
    System.out.println(" map1.get(OPAMOUNT).toString()) ====== "+map1.get("OPAMOUNT").toString());
    }*/
    
    double opval = 0;
    // System.out.println(" ooooooooooo ====== "+amtGlobal);
    HashMap singleAuthorizeMapOpBal = new HashMap();
    singleAuthorizeMapOpBal.put("TRANS_DT", currDt);
    singleAuthorizeMapOpBal.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
    List aListOp = ClientUtil.executeQuery("getOpBalAmount", singleAuthorizeMapOpBal);
    // lblOpBal.setText("0.00");
   //   System.out.println("aListOp ---------------- "+aListOp);
  //    System.out.println("aListOp ------33---------- "+aListOp.size());
    if (aListOp.size() > 0 && aListOp.get(0) != null) {
        HashMap mapop = (HashMap) aListOp.get(0);
        Opamount = mapop.get("CURAMOUNT").toString();
        if (Opamount != null && !Opamount.equalsIgnoreCase("")) // lblOpBal.setText(formatCrore(Opamount));
        {
            opval = CommonUtil.convertObjToDouble(mapop.get("CURAMOUNT").toString());
        }
        // else 

    }
    // CommonUtil.convertObjToDouble(lblOpBal.getText());
    double receiptAmt = 0;
    double paymentAmt = 0;
    HashMap singleAuthorizeMap1 = new HashMap();
    singleAuthorizeMap1.put("TRANS_DT", currDt);
    singleAuthorizeMap1.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
    singleAuthorizeMap1.put("CASHIER_APPROVAL_STATUS", TrueTransactMain.CASHIER_AUTH_ALLOWED);
    List aListA = ClientUtil.executeQuery("getSumPayRecAmount", singleAuthorizeMap1);//getSumPayRecAmountUnion
    for (int i = 0; i < aListA.size(); i++) {
        HashMap map = (HashMap) aListA.get(i);
        if (map.get("AMOUNT") != null && map.get("TRANS_TYPE").equals("CREDIT")) {//REC_AMOUNT
            amount1 = map.get("AMOUNT").toString();
            if (amount1 != null && !amount1.equalsIgnoreCase("")) {
                receiptAmt = CommonUtil.convertObjToDouble(amount1);
            }
        }
        if (map.get("AMOUNT") != null && map.get("TRANS_TYPE").equals("DEBIT")) {//REC_AMOUNT
            amount1 = map.get("AMOUNT").toString();
            if (amount1 != null && !amount1.equalsIgnoreCase("")) {
                paymentAmt = CommonUtil.convertObjToDouble(amount1);
            }
        }
    }
    System.out.println("opval -- " + opval + "receiptAmt == " + receiptAmt + "paymentAmt==" + paymentAmt);
    double totVal = opval + receiptAmt - paymentAmt;
    lblClosingBal.setText(formatCrore(String.valueOf(totVal)));
    //added by sreekrishnan
    double shval = 0.0;
    double totShadow = 0.0;
    if (aList != null) {
        HashMap map = (HashMap) aList.get(0);
        if (map.get("AMOUNT") != null)//&& map.get("AUTHORIZE_REMARKS").toString().startsWith("CU"))
        {

            shval = CommonUtil.convertObjToDouble(map.get("AMOUNT").toString());
            totShadow = shval;
            //lblShadawBal.setText(formatCrore(map.get("AMOUNT").toString()));
            lblShadawBal.setText(formatCrore(String.valueOf(totShadow)));
            System.out.println("shadow ---------------- " + totShadow);
        }
    }
    cLabel19.setVisible(false);
    lblRecTotal.setVisible(false);
    cLabel17.setVisible(false);
    lblPayTotal.setVisible(false);
}//GEN-LAST:event_btnRefreshActionPerformed

private void cboStatusItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboStatusItemStateChanged
// TODO add your handling code here:
    //getAcHdTree(cboStatus.getSelectedItem().toString());
    // btnRecieptActionPerformed1(null);
}//GEN-LAST:event_cboStatusItemStateChanged

private void cScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cScrollPane1MouseClicked
// TODO add your handling code here:
}//GEN-LAST:event_cScrollPane1MouseClicked

private void tblRecieptMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRecieptMouseClicked
// TODO add your handling code here:
//    
    createPopUpMenu();

    System.out.println("reached check action performed");
    // ( (DefaultTableModel) tblReciept.getModel() ).addTableModelListener(tableModelListener);      
    boolean chk = ((Boolean) tblReciept.getValueAt(tblReciept.getSelectedRow(), 0)).booleanValue();


    //   CheckBoxNode checkBoxNode =(CheckBoxNode) getCellEditorValue();
    //  System.out.println("="+checkBoxNode.selected);
    String strNodeUnselec = "";
    System.out.println("checkBoxNode.selected : " + chk);
    if (!chk) {
        String nodeVal = CommonUtil.convertObjToStr(tblReciept.getValueAt(tblReciept.getSelectedRow(), 1));
        System.out.println("nodeVal======" + nodeVal);
        // link_batch_id=nodeVal.substring(nodeVal.indexOf(")")+1, nodeVal.length());
        link_batch_id = CommonUtil.convertObjToStr(tblReciept.getValueAt(tblReciept.getSelectedRow(), 5));
        System.out.println(" link_batch_id=========== : " + link_batch_id);
        //int ind=nodeVal.indexOf("(");
        //  nodeVal=nodeVal.substring(0, ind);
        System.out.println("nodeVal selected : " + nodeVal);
        if (partsC.contains(nodeVal)) {
            partsC.remove(nodeVal);
        }

        partsC.add(nodeVal);//checkBoxNode.text);
        tblReciept.setValueAt(true, tblReciept.getSelectedRow(), 0);
        for(int i=0;i<tblReciept.getRowCount();i++){
            if(link_batch_id!=null && link_batch_id.length()>0){
                if(link_batch_id.equalsIgnoreCase(CommonUtil.convertObjToStr(tblReciept.getValueAt(i, 5)))){
                    tblReciept.setValueAt(true, i, 0);                
                    if (partsC.contains(CommonUtil.convertObjToStr(tblReciept.getValueAt(i, 1)))) {
                        partsC.remove(CommonUtil.convertObjToStr(tblReciept.getValueAt(i, 1)));
                    }
                    partsC.add(CommonUtil.convertObjToStr(tblReciept.getValueAt(i, 1)));
                }
            }
        }
    } else {
        String nodeVal = CommonUtil.convertObjToStr(tblReciept.getValueAt(tblReciept.getSelectedRow(), 1));
        System.out.println("nodeVal=====" + nodeVal);
        // int ind=nodeVal.indexOf("(");
        //  nodeVal=nodeVal.substring(0, ind);
        strNodeUnselec = nodeVal;
        System.out.println("nodeVal unselec : " + nodeVal);
        // 
        partsC.remove(nodeVal);//checkBoxNode.text);
        tblReciept.setValueAt(false, tblReciept.getSelectedRow(), 0);
        System.out.println("link_batch_id : " + link_batch_id);
        for(int i=0;i<tblReciept.getRowCount();i++){
            if(link_batch_id!=null && link_batch_id.length()>0){
                if(link_batch_id.equalsIgnoreCase(CommonUtil.convertObjToStr(tblReciept.getValueAt(i, 5)))){
                    tblReciept.setValueAt(false, i, 0);
                    partsC.remove(CommonUtil.convertObjToStr(tblReciept.getValueAt(i, 1)));
                }
            }
        }
    }

    System.out.println("checkBox itemStateChanged=" + partsC);
    // CashierApprovalUI ui= new CashierApprovalUI(partsC);
    System.out.println("flagChk iooooooooooooooo ===== " + flagChk);

    if (!flagChk) {
        getMDSChecked(partsC, CommonUtil.convertObjToStr(tblReciept.getValueAt(tblReciept.getSelectedRow(), 1)), strNodeUnselec);
    }
    getDisplayDet(partsC);
    setDisableReject();
    //  tblReciept.updateUI();//bb
    // fireEditingStopped();
    if (partsC != null && partsC.size() == 0) {
        chkUniqueId = "";
        flagChk = false;
        // treData.updateUI();
    }
    // treData.revalidate(); 

}//GEN-LAST:event_tblRecieptMouseClicked

private void tblPaymentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPaymentMouseClicked
    // TODO add your handling code here:
    System.out.println("reached check action performed");
    // ( (DefaultTableModel) tblReciept.getModel() ).addTableModelListener(tableModelListener);      
    boolean chk = ((Boolean) tblPayment.getValueAt(tblPayment.getSelectedRow(), 0)).booleanValue();


    //   CheckBoxNode checkBoxNode =(CheckBoxNode) getCellEditorValue();
    //  System.out.println("="+checkBoxNode.selected);
    String strNodeUnselec = "";
    System.out.println("checkBoxNode.selected : " + chk);
    if (!chk) {
        String nodeVal = CommonUtil.convertObjToStr(tblPayment.getValueAt(tblPayment.getSelectedRow(), 1));
        System.out.println("nodeVal======" + nodeVal);
        // link_batch_id=nodeVal.substring(nodeVal.indexOf(")")+1, nodeVal.length());
        link_batch_id = CommonUtil.convertObjToStr(tblPayment.getValueAt(tblPayment.getSelectedRow(), 5));
        System.out.println(" link_batch_id=========== : " + link_batch_id);
        //int ind=nodeVal.indexOf("(");
        //  nodeVal=nodeVal.substring(0, ind);
        System.out.println("nodeVal selected : " + nodeVal);
        if (partsC.contains(nodeVal)) {
            partsC.remove(nodeVal);
        }

        partsC.add(nodeVal);//checkBoxNode.text);
        tblPayment.setValueAt(true, tblPayment.getSelectedRow(), 0);
    } else {
        String nodeVal = CommonUtil.convertObjToStr(tblPayment.getValueAt(tblPayment.getSelectedRow(), 1));
        System.out.println("nodeVal=====" + nodeVal);
        // int ind=nodeVal.indexOf("(");
        //  nodeVal=nodeVal.substring(0, ind);
        strNodeUnselec = nodeVal;
        System.out.println("nodeVal unselec : " + nodeVal);
        // 
        partsC.remove(nodeVal);//checkBoxNode.text);
        tblPayment.setValueAt(false, tblPayment.getSelectedRow(), 0);
    }

    System.out.println("checkBox itemStateChanged=" + partsC);
    // CashierApprovalUI ui= new CashierApprovalUI(partsC);
    System.out.println("flagChk iooooooooooooooo ===== " + flagChk);

    if (!flagChk) {
        getMDSChecked(partsC, CommonUtil.convertObjToStr(tblPayment.getValueAt(tblPayment.getSelectedRow(), 1)), strNodeUnselec);
    }
    getDisplayDet(partsC);
    setDisableReject();
    //  tblReciept.updateUI();//bb
    // fireEditingStopped();
    if (partsC != null && partsC.size() == 0) {
        chkUniqueId = "";
        flagChk = false;
        // treData.updateUI();
    }
}//GEN-LAST:event_tblPaymentMouseClicked
    private void btnRecieptActionPerformed1(String type) {
        // btnReject.setVisible(true);
        HashMap shareTypeMap = new HashMap();
        HashMap whereMap = new HashMap();
        // String status = "AUTHORIZED";
        // whereMap.put(CommonConstants.AUTHORIZEDT, currDt);
        whereMap.put("AUTHORIZESTATUS", CommonConstants.STATUS_AUTHORIZED);
        whereMap.put("TRANS_DT", currDt);
        // if(type.e)
        whereMap.put("TRANS_TYPE", type);
        whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);

        shareTypeMap.put(CommonConstants.MAP_WHERE, whereMap);
        shareTypeMap.put(CommonConstants.MAP_NAME, "getCashReceiptsTransTreeNewCredit");
        try {
            // log.info("populateData...");
            observable.populateData(shareTypeMap, tblReciept);
            tblReciept.setModel(observable.getTblReciept());
            selectMode = true;
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
        tblReciept.setEditingColumn(0);
        javax.swing.table.TableColumn col = tblReciept.getColumn(tblReciept.getColumnName(0));
        col.setMaxWidth(50);
        col.setPreferredWidth(50);
        col = tblReciept.getColumn(tblReciept.getColumnName(1));
        col.setMaxWidth(70);
        col.setPreferredWidth(70);
        col = tblReciept.getColumn(tblReciept.getColumnName(2));
        col.setMaxWidth(300);
        col.setPreferredWidth(300);
        System.out.println(" shareTypeMap=======" + shareTypeMap);
        System.out.println("contrrrrrrrrrrrrrrrrr" + tblReciept.getRowCount());
        System.out.println("contrrrrrrrrrrrrrrrrr" + tblPayment.getRowCount());
        //lblTotReceipts.setText(CommonUtil.convertObjToStr(data.size()));
        //lblTotPayments.setText(CommonUtil.convertObjToStr(tblPayment.getRowCount()));
        //  CheckBoxNodeRenderer render = new CheckBoxNodeRenderer();
        //        tblReciept.setCe(render);


        // TODO add your handling code here:
    }

    private void btnRecieptActionPerformedDebit(String Debittype) {
        // btnReject.setVisible(true);
        HashMap DebitMap = new HashMap();
        HashMap whereMap = new HashMap();
        // String status = "AUTHORIZED";
        // whereMap.put(CommonConstants.AUTHORIZEDT, currDt);
        whereMap.put("AUTHORIZESTATUS", CommonConstants.STATUS_AUTHORIZED);
        whereMap.put("TRANS_DT", currDt);
        // if(type.e)
        whereMap.put("TRANS_TYPE", Debittype);
        whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);

        DebitMap.put(CommonConstants.MAP_WHERE, whereMap);
        DebitMap.put(CommonConstants.MAP_NAME, "getCashReceiptsTransTreeNewDebit");
        try {
            // log.info("populateData...");
            observable.populateData(DebitMap, tblPayment);
            tblPayment.setModel(observable.getTblReciept());
            selectMode = true;

        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
        tblPayment.setEditingColumn(0);
        javax.swing.table.TableColumn col = tblPayment.getColumn(tblPayment.getColumnName(0));
        col.setMaxWidth(50);
        col.setPreferredWidth(50);
        col = tblPayment.getColumn(tblPayment.getColumnName(1));
        col.setMaxWidth(70);
        col.setPreferredWidth(70);
        col = tblPayment.getColumn(tblPayment.getColumnName(2));
        col.setMaxWidth(300);
        col.setPreferredWidth(300);
        System.out.println(" shareTypeMap=======" + DebitMap);
        // System.out.println("contrrrrrrrrrrrrrrrrr"+tblReciept.getRowCount());
        System.out.println("contrrrrrrrrrrrrrrrrr" + tblPayment.getRowCount());
        // lblTotReceipts.setText(CommonUtil.convertObjToStr(tblReciept.getRowCount()));
        //lblTotPayments.setText(CommonUtil.convertObjToStr(data.size()));
        //  CheckBoxNodeRenderer render = new CheckBoxNodeRenderer();
        //        tblReciept.setCe(render);


        // TODO add your handling code here:
    }

    /**
     * @param args the command line arguments
     */
    // public static void main(String args[]) {
    //    new CashierApprovalUI().show();
    // }
    public void createPopUpMenu() {
        CPopupMenu popup = new CPopupMenu();
        JMenuItem myMenuItem1 = new JMenuItem("cccccccccccccccccccccc");
        JMenuItem myMenuItem2 = new JMenuItem("bbbbbbbbbbbbbbbbbbbbbb");
        popup.add(myMenuItem1);
        popup.add(myMenuItem2);
        // MouseListener popupListener = new PopupListener(popup);
        // tblReciept.addMouseListener(popupListener);

    }

    private DefaultMutableTreeNode addNode(DefaultMutableTreeNode parent, DefaultMutableTreeNode child, Object obj) {
        if (!chkExistance(parent, obj.toString())) {
            child = new DefaultMutableTreeNode(obj);
        }
        if (!parent.isNodeChild(child)) {
            parent.add(child);
        }
        return child;
    }
    /*  DefaultMutableTreeNode receipts = null;
    DefaultMutableTreeNode payments =null;
    public DefaultTreeModel getAcHdTree(String status) {
    DefaultMutableTreeNode parent = new DefaultMutableTreeNode("Cashier");
    receipts = new DefaultMutableTreeNode("Receipts");
    payments = new DefaultMutableTreeNode("Payments");
    
    DefaultMutableTreeNode receiptsTr = null;
    DefaultMutableTreeNode paymentsTr = null;
    /////////////////////////////////////////////////////////////////////
    //  String status=cboStatus.getSelectedItem().toString();
    HashMap singleAuthorizeMap = new HashMap();
    singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt);
    singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, status);
    System.out.println("Answer :"+singleAuthorizeMap.get(CommonConstants.AUTHORIZESTATUS));
    singleAuthorizeMap.put("TRANS_DT", currDt);
    //    singleAuthorizeMap.put("TYPE", "AUTHORIZED");
    singleAuthorizeMap.put(CommonConstants.BRANCH_ID,  ProxyParameters.BRANCH_ID);
    final List objList =  ClientUtil.executeQuery("getCashReceiptsTransTree", singleAuthorizeMap);
    final List objListPay =  ClientUtil.executeQuery("getCashPaymentsTransTree", singleAuthorizeMap);
    // final List objList = ClientUtil.executeQuery("getCashTransTree", null);
    final int objListSize = objList.size();
    
    for (int i = 0;i<objListSize;i++) {
    CheckBoxNode ch= new CheckBoxNode(((HashMap)objList.get(i)).get("TRANS_ID").toString(),false);
    receiptsTr = addNode(receipts,receiptsTr,(Object)ch);//(Object)ch ((HashMap)objList.get(i)).get("TRANS_ID")
    }
    parent.add(receipts);
    for (int j = 0;j<objListPay.size();j++) {
    CheckBoxNode ch1= new CheckBoxNode(((HashMap)objListPay.get(j)).get("TRANS_ID").toString(),false);
    paymentsTr = addNode(payments,paymentsTr,(Object)ch1);
    }
    parent.add(payments);
    final DefaultTreeModel treemodel = new DefaultTreeModel(parent);
    //  final DefaultTreeModel treemodelPay = new DefaultTreeModel(payments);
    //        treData.setModel(treemodel);
    //    treData.setModel(treemodelPay);
    //babu comm
    // root = null;
    // receiptsTr = null;
    //  paymentsTr = null;
    
    //   CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
    //            treData.setCellRenderer(renderer);
    //            objCheckBoxNodeEditor = new CheckBoxEditor(treData);
    //             treData.setCellEditor(objCheckBoxNodeEditor);
    //            treData.setEditable(true);
    //     new CheckBoxEditor(treData).getTreeCellEditorComponent(treData ,"", true, true, true,0);
    // com.jidesoft.swing.CheckBoxTree checkboxTree = new com.jidesoft.swing.CheckBoxTree();
    // TreeNode yourRoot = new DefaultMutableTreeNode("foo"); CheckboxTree checkboxTree = new CheckboxTree(yourRoot);
    /// checkboxTree.setModel(treemodel);
    
    return treemodel;
    }*/

    private boolean chkExistance(DefaultMutableTreeNode parent, String chkStr) {
        final Enumeration objEnumeration = parent.children();
        while (objEnumeration.hasMoreElements()) {
            if (objEnumeration.nextElement().toString().equals(chkStr)) {
                return true;
            }
        }
        return false;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnApprove;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnDenaomination;
    private com.see.truetransact.uicomponent.CButton btnExit;
    private com.see.truetransact.uicomponent.CButton btnRefresh;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnTransDetails;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel17;
    private com.see.truetransact.uicomponent.CLabel cLabel19;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CLabel cLabel4;
    private com.see.truetransact.uicomponent.CLabel cLabel6;
    private com.see.truetransact.uicomponent.CLabel cLabel7;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane2;
    private com.see.truetransact.uicomponent.CComboBox cboStatus;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblBalance;
    private com.see.truetransact.uicomponent.CLabel lblBalance1;
    private com.see.truetransact.uicomponent.CLabel lblClosingBal;
    private com.see.truetransact.uicomponent.CLabel lblOpBal;
    private com.see.truetransact.uicomponent.CLabel lblOpeningbal;
    private com.see.truetransact.uicomponent.CLabel lblPayTotal;
    private com.see.truetransact.uicomponent.CLabel lblPayment;
    private com.see.truetransact.uicomponent.CLabel lblPayment2;
    private com.see.truetransact.uicomponent.CLabel lblRecTotal;
    private com.see.truetransact.uicomponent.CLabel lblReceipt;
    private com.see.truetransact.uicomponent.CLabel lblRecipt;
    private com.see.truetransact.uicomponent.CLabel lblShadawBal;
    private com.see.truetransact.uicomponent.CLabel lblTotPayments;
    private com.see.truetransact.uicomponent.CLabel lblTotReceipts;
    private com.see.truetransact.uicomponent.CLabel lblTransType;
    private com.see.truetransact.uicomponent.CPanel panCashTrans;
    private com.see.truetransact.uicomponent.CPanel panTree;
    private com.see.truetransact.uicomponent.CScrollPane scrTableScroll;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CTable tblPayment;
    private com.see.truetransact.uicomponent.CTable tblReciept;
    // End of variables declaration//GEN-END:variables

    public void getDisplayDet(ArrayList partsC) {
        clearTransDetails();
        //   partsC=cList;
        System.out.println("partsC IN----" + partsC);
        if (partsC != null) {
            System.out.println("partsC IN-4555455---");
            getDisplayTransDetails(partsC);//node//partsC
        }
    }
    public String chkUniqueId = "";
    public boolean flagChk = false;

    public void getMDSChecked(ArrayList partsC, String ckkbox, String strNodeUnselec) {
        System.out.println("partsC jjjjjjjjjjjjjjj ====" + partsC);
        if (partsC != null && partsC.size() == 1)//&& !TransIdList.contains(strNodeUnselec))
        {
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            singleAuthorizeMap.put("TRANS_DT", currDt);
            singleAuthorizeMap.put(CommonConstants.TRANS_ID, partsC.get(0).toString());
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            List aList = ClientUtil.executeQuery("getUniqueIdData", singleAuthorizeMap);
            if (aList != null) {
                HashMap map = (HashMap) aList.get(0);
                String Id = null;
                if (map.get("AUTHORIZE_REMARKS") != null)//&& map.get("AUTHORIZE_REMARKS").toString().startsWith("CU"))
                {
                    Id = CommonUtil.convertObjToStr(map.get("AUTHORIZE_REMARKS"));
                    // if(Id==null || Id.equals(""))
                    // {
                    //    Id =CommonUtil.convertObjToStr(map.get("SCREEN_NAME"));
                    //  }
                    //  System.out.println("Id === "+Id+"chkUniqueId = "+chkUniqueId);
                    if (chkUniqueId != null && !chkUniqueId.equals("") && Id.equals(chkUniqueId)) {
                        flagChk = true;
                    }
                    System.out.println("Id === " + Id + "chkUniqueId = " + chkUniqueId);
                    if (chkUniqueId != null && !chkUniqueId.equals("") && Id.equals(chkUniqueId)) {
                        flagChk = true;
                    }
                    if (Id != null && !Id.equals("") && Id.startsWith("CU")) {
                        chkUniqueId = Id;

                        HashMap mapo1 = new HashMap();
                        ArrayList TransIdList = new ArrayList();
                        mapo1.put("AUTHORIZE_REMARKS", Id);
                        final List objList1 = ClientUtil.executeQuery("getTransIdUnique", mapo1);
                        for (int i = 0; i < objList1.size(); i++) {
                            HashMap map1 = (HashMap) objList1.get(i);
                            TransIdList.add(map1.get("TRANS_ID"));
                        }

                        //  System.out.println("ccccccccc ===="+receiptsTr.);
                        //  while(receiptsTr.children())
                        //  {
                        //      System.out.println("ccccccccc ====" +receiptsTr.getNextLeaf());
                        //   }
                        System.out.println("TransIdList ====" + TransIdList);
                        // System.out.println("receipts.getChildCount() ===="+receipts.getChildCount());

                        //        Enumeration e = receipts.children();
                        // Enumeration e1 = payments.children();
                        //while (e.hasMoreElements() ) {
                        for (int j = 0; j < tblReciept.getRowCount(); j++) {
                            //  System.out.println("1 e.breadthFirstEnumeration():"+e.nextElement());
                            //  CheckBoxNode chk = (CheckBoxNode)e.nextElement();
                            //  DefaultMutableTreeNode dmt=(DefaultMutableTreeNode)e.nextElement();
                            // CheckBoxNode chk = (CheckBoxNode)dmt.getUserObject();
                            String nodeVal = CommonUtil.convertObjToStr(tblReciept.getValueAt(tblReciept.getSelectedRow(), 2));

                            // System.out.println("1dmt :"+dmt);
                            for (int i = 0; i < objList1.size(); i++) {
                                HashMap map1 = (HashMap) objList1.get(i);
                                //  System.out.println("chk.getText()== :"+chk.getText());
                                System.out.println("map1.get(TRANS_ID)= :" + map1.get("TRANS_ID"));
                                if (!TransIdList.contains(strNodeUnselec)
                                        && nodeVal.contains(map1.get("TRANS_ID").toString())) {
                                    // chk.setSelected(true);
                                    tblReciept.setValueAt(true, tblReciept.getSelectedRow(), 0);
                                    // chk1.setSelected(true);
                                    if (partsC.contains(map1.get("TRANS_ID").toString())) {
                                        partsC.remove(map1.get("TRANS_ID").toString());
                                    }

                                    partsC.add(map1.get("TRANS_ID").toString());
                                }

                            }
                        }
                        /*   Enumeration e1 = payments.children();
                        
                        
                        while (e1.hasMoreElements()) {
                        //  System.out.println("1 e.breadthFirstEnumeration():"+e.nextElement());
                        //  CheckBoxNode chk = (CheckBoxNode)e.nextElement();
                        DefaultMutableTreeNode dmt1=(DefaultMutableTreeNode)e1.nextElement();
                        CheckBoxNode chk1 = (CheckBoxNode)dmt1.getUserObject();
                        
                        System.out.println("chk1chk1chk1-----:"+chk1);
                        System.out.println("chkList ==== :"+TransIdList);
                        for(int i=0;i<objList1.size();i++)
                        {
                        HashMap map1=(HashMap)objList1.get(i);
                        System.out.println("chk.getText()=111= :"+chk1.getText());
                        System.out.println("map1.get(TRANS_ID111111)= :"+map1.get("TRANS_ID"));
                        System.out.println("VVVV= :"+TransIdList.contains(strNodeUnselec) +"strNodeUnselec "+strNodeUnselec);
                        if(!TransIdList.contains(strNodeUnselec) && 
                        chk1.getText().contains(map1.get("TRANS_ID").toString()) )
                        {
                        chk1.setSelected(true);
                        // chkList.add(map1.get("TRANS_ID").toString());
                        if(partsC.contains(map1.get("TRANS_ID").toString()))
                        partsC.remove(map1.get("TRANS_ID").toString());
                        //   else
                        partsC.add(map1.get("TRANS_ID").toString());
                        }
                        else
                        {
                        //   for(int l=0;l<TransIdList.size();l++)
                        //      partsC.remove(TransIdList.get(l).toString());
                        }
                        // partsC.remove(strNodeUnselec);
                        
                        }
                        
                        }
                        System.out.println("partsC11==="+partsC);
                        
                        treData.updateUI();
                        //  TreePath path =new TreePath("Cashier");
                        // visitAllExpandedNodes(treData,path);
                        //                                        printDescendants(receiptsTr);
                        
                        //  receiptsTr.
                        /*  HashMap singleAuthorize = new HashMap();
                        singleAuthorize.put(CommonConstants.AUTHORIZEDT, currDt);
                        singleAuthorize.put(CommonConstants.AUTHORIZESTATUS,"PENDING");
                        singleAuthorize.put("TRANS_DT", currDt);
                        //    singleAuthorizeMap.put("TYPE", "AUTHORIZED");
                        singleAuthorize.put(CommonConstants.BRANCH_ID,  ProxyParameters.BRANCH_ID);
                        final List objList =  ClientUtil.executeQuery("getCashReceiptsTransTree1", singleAuthorize);
                        final int objListSize = objList.size();
                        int i=0;int j=0;
                        for ( i = 0;i<objListSize;i++) {
                        for ( j = 0;j<TransIdList.size();j++) {
                        System.out.println("TransIdList q11===="+TransIdList.get(j).toString());
                        System.out.println("TransIdList q222===="+((HashMap)objList.get(i)).get("TRANS_ID").toString());
                        if(TransIdList.get(j).toString().equals(((HashMap)objList.get(i)).get("TRANS_ID").toString())) 
                        {
                        
                        // CheckBoxNode ch= new CheckBoxNode(((HashMap)objList.get(i)).get("TRANS_ID").toString(),true);
                        // ch.setSelected(true);
                        System.out.println("IOOOOOOOOOOOOOOOOO");
                        ckkbox.setSelected(true);
                        }
                        }
                        }*/
                    }
                }
            }
        }
    }

    public static void printDescendants(DefaultMutableTreeNode root) {
        System.out.println(root);
        Enumeration children = root.children();
        if (children != null) {
            while (children.hasMoreElements()) {
                System.out.println(" children.nextElement() ===" + children.nextElement());
                printDescendants((DefaultMutableTreeNode) children.nextElement());
            }
        }
    }

    public void visitAllExpandedNodes(JTree tree, TreePath parent) {
        if (!tree.isVisible(parent)) {
            return;
        }
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        System.out.println("N1-----------" + node);

        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                //  TreePath path = parent.pathByAddingChild(n);
                System.out.println("99999999999999999999 =====" + n);
                //  visitAllExpandedNodes(tree, path);
            }
        }

    }
    /*class CheckBoxEditor extends DefaultCellEditor  implements TableCellEditor,ItemListener {
    
    CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
    private JCheckBox checkBox;
    private CTable Name;
    ChangeEvent changeEvent = null;
    // public ArrayList partsC=new ArrayList();
    CTable tree;
    //CashierApprovalUI ui;
    
    public CheckBoxEditor(JCheckBox checkBox,CTable name2) {
    super(checkBox);
    renderer.addItemListener(this);
    this.checkBox = checkBox;
    this.checkBox.addItemListener(this);
    this.Name=name2;  
    Component c = getTableCellEditorComponent(name2, "1", true, name2.getSelectedRow(), name2.getSelectedColumn());
    System.out.println("Iam ahrere"+c);
    }
    public Object getCellEditorValue() {
    JCheckBox checkbox = renderer.getLeafRenderer();
    CheckBoxNode checkBoxNode = new CheckBoxNode(checkbox.getText(),
    checkbox.isSelected());
    return checkBoxNode;
    }
    
    public boolean isCellEditable(EventObject event) {
    boolean returnValue = false;
    if (event instanceof MouseEvent) {
    MouseEvent mouseEvent = (MouseEvent) event;
    String path = ""+tree.getValueAt(tblReciept.getSelectedRow(), tblReciept.getSelectedColumn());
    
    }
    return returnValue;
    }
    
    
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    
    Component editor = renderer.getTableCellRendererComponent(table, value,
    isSelected,true,row, column);
    
    // editor always selected / focused
    ItemListener itemListener = new ItemListener() {
    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
    
    System.out.println("asdasdasdasdadasdas");
    if (stopCellEditing()) {
    
    CheckBoxNode checkBoxNode =(CheckBoxNode) getCellEditorValue();
    //  System.out.println("="+checkBoxNode.selected);
    String strNodeUnselec="";
    System.out.println("checkBoxNode.selected : "+checkBoxNode.selected);
    if(checkBoxNode.selected)
    {
    String nodeVal=checkBoxNode.text;
    link_batch_id=nodeVal.substring(nodeVal.indexOf(")")+1, nodeVal.length());
    int ind=nodeVal.indexOf("(");
    nodeVal=nodeVal.substring(0, ind);
    System.out.println("nodeVal selected : "+nodeVal);
    if(partsC.contains(nodeVal))
    partsC.remove(nodeVal);
    
    partsC.add(nodeVal);//checkBoxNode.text);
    }
    else
    {
    String nodeVal=checkBoxNode.text;
    int ind=nodeVal.indexOf("(");
    nodeVal=nodeVal.substring(0, ind);
    strNodeUnselec=nodeVal;
    System.out.println("nodeVal unselec : "+nodeVal);
    // 
    partsC.remove(nodeVal);//checkBoxNode.text);
    }
    System.out.println("checkBox itemStateChanged="+partsC);
    // CashierApprovalUI ui= new CashierApprovalUI(partsC);
    System.out.println("flagChk iooooooooooooooo ===== "+flagChk);
    
    if(!flagChk)
    getMDSChecked(partsC,checkBoxNode,strNodeUnselec);
    getDisplayDet(partsC); 
    setDisableReject(); 
    fireEditingStopped();
    if(partsC!=null && partsC.size()==0)
    {
    chkUniqueId="";
    flagChk=false;
    // treData.updateUI();
    }
    // treData.revalidate(); 
    
    }
    }
    };
    
    if (editor instanceof JCheckBox) {
    ((JCheckBox) editor).addItemListener(itemListener);
    }
    else
    ((JCheckBox) editor).removeItemListener(itemListener);  
    return editor;
    }
    
    @Override
    public void itemStateChanged(ItemEvent e) {
    try
    {
    if(e.getSource() ==checkBox)
    {
    System.out.println("asdsadsadasdasd");
    }
    else
    {
    System.out.println("xxxxxxxxxxxxxxxxxxxxxxx");
    }
    }
    catch(Exception ex)
    {
    
    }
    }
    
    }*/
}
/*
class CheckBoxNode {
String text;

boolean selected;

public CheckBoxNode(String text, boolean selected) {
this.text = text;
this.selected = selected;
}

public boolean isSelected() {
return selected;
}

public void setSelected(boolean newValue) {
selected = newValue;
}

public String getText() {
return text;
}

public void setText(String newValue) {
text = newValue;
}

public String toString() {
return getClass().getName() + "[" + text + "/" + selected + "]";
}
}*/

class NamedVector extends Vector {

    String name;

    public NamedVector(String name) {
        this.name = name;
    }

    public NamedVector(String name, Object elements[]) {
        this.name = name;
        for (int i = 0, n = elements.length; i < n; i++) {
            add(elements[i]);
        }
    }

    public String toString() {
        return "[" + name + "]";
    }
}
