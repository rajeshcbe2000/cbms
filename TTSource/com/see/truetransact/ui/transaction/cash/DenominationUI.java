/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DenominationUI.java
 *
 * Created on September 28, 2011, 3:03 PM
 */
package com.see.truetransact.ui.transaction.cash;

import com.see.truetransact.clientutil.ClientUtil;
import java.util.HashMap;
import java.util.List;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import java.util.Date;
import java.math.BigDecimal;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.Enumeration;
import java.text.SimpleDateFormat;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uicomponent.CDialog;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;
/**
 *
 * @author  user
 */
public class DenominationUI extends CDialog   {
    DefaultTableModel model = null;
  //  DefaultTreeModel root;
 //   DefaultTreeModel child;
    Date currDt = null;
    private TableModelListener tableModelListener;
    public String transMode="";
    public String trans_pid="";
    public String clsbal="";
    public     String Opamount="",
    amount1=""
    ,countVal="",amount="",sc_Name="",cname="",ac_No="",ac_HD_No=null,prod_Type="",prod_Id=null,trans_id="",trans_type="",
    posted_by="",autho_by="",particulars="",panNo="",token_No="",instr_Type="",
    instr_No="",instr_date="",clear_Bal="";
    DefaultMutableTreeNode selectedNode = null;
    String lblAmt="",lblTransTy;
    
    public DenominationUI(String amount,String TransTy,String TransId) {
        trans_pid=TransId;
        lblAmt=amount;
        lblTransTy=TransTy;
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initTableData();
      //this.setBounds(0,0, 500,330);
        this.setVisible(true);
        fillData();
       // this.setTitle("Change");
        setupScreen();
//        getAcHdTree("PENDING");
    //    cboStatus.setSelectedItem("Pending");
//        tblData.getModel().addTableModelListener(tableModelListener);
     //   btnReject.setVisible(false);
        
    }
      private void setupScreen() {
        setModal(true);
        setTitle("Change ["+ProxyParameters.BRANCH_ID+"]");
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("@$#@$#@# screenSize : "+screenSize);
       // setSize(570, 280);
        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    public void insertIntoDenaomination() {
        try {
            for (int i = 0; i<= 14; i++) {
                if(tblData.getModel().getValueAt(i,2)!=null &&
                tblData.getModel().getValueAt(i,3)!=null ) {
                  //  if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
                        //totalPay += ((Double) tblData.getModel().getValueAt(i,2)).doubleValue();
                        //System.out.println("33-=="+tblData.getModel().getValueAt(i,1));
                        //System.out.println("22==="+tblData.getModel().getValueAt(i,2));
                        String amount = tblData.getModel().getValueAt(i,3).toString().replaceAll(",", "");
                        String dnomType= tblData.getModel().getValueAt(i,0).toString();
                        insertData("CHANGE",amount,tblData.getModel().getValueAt(i,2).toString(),"CREDIT",
                        tblData.getModel().getValueAt(i,1).toString(),dnomType);
                 //   }
                }
            }
            for (int i = 0; i<= 14; i++) {
                if(tblData.getModel().getValueAt(i,5)!=null &&
                tblData.getModel().getValueAt(i,6)!=null) {
                 //   if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
                        String amount = tblData.getModel().getValueAt(i,6).toString().replaceAll(",", "");
                        String dnomType= tblData.getModel().getValueAt(i,0).toString();
                        insertData("CHANGE",amount,tblData.getModel().getValueAt(i,5).toString(),"DEBIT",
                        tblData.getModel().getValueAt(i,4).toString(),dnomType);
                  //  }
                }
                
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static String formatCrore(String str) {
        java.text.DecimalFormat numberFormat = new java.text.DecimalFormat();
        numberFormat.applyPattern("########################0.00");
        
        double currData = Double.parseDouble(str.replaceAll(",",""));
        str = numberFormat.format( currData );
        
        String num = str.substring(0,str.lastIndexOf(".")).replaceAll(",","");
        String dec = str.substring(str.lastIndexOf("."));
        
        String sign = "";
        if (num.substring(0,1).equals("-")) {
            sign = num.substring(0,1);
            num = num.substring(1,num.length());
        }
        
        char[] chrArr = num.toCharArray();
        StringBuffer fmtStrB = new StringBuffer();
        
        for (int i=chrArr.length-1, j=0, k=0; i >= 0; i--) {
            if ((j==3 && k==3) || (j==2 && k==5) || (j==2 && k==7)) {
                fmtStrB.insert(0, ",");
                if (k==7) k = 0;
                j=0;
            }
            j++; k++;
            
            fmtStrB.insert(0, chrArr[i]);
        }
        fmtStrB.append(dec);
        
        str = fmtStrB.toString();
        
        str = sign+str;
        
        if (str.equals(".00")) str = "0";
        
        return str;
    }
    public void insertData(String transId, String amount, String noOfNotes, String trans_type, String deno_type,String dnomType) {
        String currency="INR";
        HashMap singleAuthorizeMap = new HashMap();
        singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        singleAuthorizeMap.put("TRANS_DT", currDt);
        singleAuthorizeMap.put(CommonConstants.TRANS_ID,transId );//trans id
        singleAuthorizeMap.put("DENOMINATION_VALUE", amount); //amount
        singleAuthorizeMap.put("DENOMINATION_COUNT", noOfNotes);//no of notes
        singleAuthorizeMap.put("CURRENCY", currency);//Currency
        singleAuthorizeMap.put("TRANS_TYPE", trans_type);//Trans type
        singleAuthorizeMap.put("STATUS", "CREATED");//status
        singleAuthorizeMap.put("DENOMINATION_TYPE", deno_type);//Denaomination type
        singleAuthorizeMap.put(CommonConstants.BRANCH_ID,  ProxyParameters.BRANCH_ID);
        singleAuthorizeMap.put("DENOM_TYPE",  dnomType);
        ClientUtil.execute("insertDenominationDetails", singleAuthorizeMap);
        singleAuthorizeMap.clear();
        singleAuthorizeMap=null;
    }
        public static boolean isNumeric(String str)  
{  
  try  
  {  
    Integer.parseInt(str);  
   //   System.out.println("ddd"+d);
  }  
  catch(NumberFormatException nfe)  
  {  
    return false;  
  }  
  return true;  
}
    private void setTableModelListener() {
        tableModelListener = new TableModelListener() {
            
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    System.out.println("Cell " + e.getFirstRow() + ", "
                    + e.getColumn() + " changed. The new value: "
                    + tblData.getModel().getValueAt(e.getFirstRow(),
                    e.getColumn()));
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    
                    if (row!=17) {
                        if (column == 1 || column == 2) {

                            TableModel model = tblData.getModel();

                            String quantity = model.getValueAt(row, 1).toString();

                            
                             if(model.getValueAt(row, 2)!=null && !model.getValueAt(row, 2).toString().equals("") && !isNumeric(model.getValueAt(row, 2).toString()))
                            {
                                System.out.println("4356346436346====");
                                ClientUtil.showAlertWindow("Please enter Numeric Value!!!"); 
                                model.setValueAt("0", row, 2);
                               return;
                            }
                            if(model.getValueAt(row, 1)!=null) {
                                if(row==17) {
                                    quantity="1";
                                }

                                double price = CommonUtil.convertObjToDouble(model.getValueAt(row, 2)) .doubleValue();
                                Double value=null;
                                if(quantity.equals("0.50")){
                                     value=new Double(0.50 * price);
                                }
                                else{
                                 value =new Double(Integer.parseInt(quantity) * price);
                                }
                                // System.out.println("price====="+price +"quantity=="+quantity);
                                //    System.out.println("valuevalue=="+value);
                                model.setValueAt(formatCrore(String.valueOf(value)), row, 3);
                                //Total print
                                double totalPay=0;
                                BigDecimal bm=null;

                                for (int i = 0; i< 17; i++) {
                                    //  total +=((Double) model.getValueAt(i, 2)).doubleValue();
                                    if(tblData.getModel().getValueAt(i,3)!=null) {
                                        // totalPay += ((Double) tblData.getModel().getValueAt(i,2)).doubleValue();
                                        String val=tblData.getModel().getValueAt(i,3).toString();
                                        val=val.replaceAll(",","");
                                        // totalPay += Double.parseDouble(tblData.getModel().getValueAt(i,2).toString());
                                        totalPay += Double.parseDouble(val);
                                    }
                                }
                                model.setValueAt(formatCrore(String.valueOf(Double.valueOf(totalPay))), 17, 3);//formatCrore(

                            }
                            //setModified(true);
                        }
                        if ( column == 4 || column == 5 ) {
                            TableModel model = tblData.getModel();
                            String quantity = model.getValueAt(row, 4).toString();
                            
                             if(model.getValueAt(row, 5)!=null && !model.getValueAt(row, 5).toString().equals("") && !isNumeric(model.getValueAt(row, 5).toString()))
                            {
                                ClientUtil.showAlertWindow("Please enter Numeric Value!!!"); 
                                model.setValueAt("0", row, 5);
                               return;
                            }
                            if(model.getValueAt(row, 4)!=null) {
                                if(row==15) {
                                    // System.out.println("ROWW==111=="+row);
                                    quantity="1";
                                }
                                double price = CommonUtil.convertObjToDouble(model.getValueAt(row, 5)).doubleValue();
                                Double value = new Double(Integer.parseInt(quantity) * price);
                                model.setValueAt(formatCrore(String.valueOf(value)), row, 6);

                                //Set tiotal
                                double totalRec=0;

                                for (int i = 0; i< 17; i++) {
                                    //  total +=((Double) model.getValueAt(i, 2)).doubleValue();
                                    if(tblData.getModel().getValueAt(i,6)!=null) {
                                        String val=  tblData.getModel().getValueAt(i,6).toString();
                                        val=val.replaceAll(",","");
                                        // totalRec +=   Double.parseDouble(tblData.getModel().getValueAt(i,5).toString());
                                        totalRec +=   Double.parseDouble(val);
                                    }

                                }
                                model.setValueAt(formatCrore(String.valueOf(Double.valueOf(totalRec))), 17, 6);  //formatCrore(
                              //  setModified(true);
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
        String data[][] ={{}};
        String col[] = {"Account No","Name","Deposit Date","Amount","Maturity Date"};
        DefaultTableModel dataModel = new DefaultTableModel();
        // dataModel.setDataVector(dataVector
        model = new DefaultTableModel(data,col);
        //  tblData.getCellEditor().stopCellEditing();
        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
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
                {"NOTE","1000", null, null, "1000", null, null},
                {"NOTE","500", null, null, "500", null, null},
                {"NOTE","200", null, null, "200", null, null},
                {"NOTE","100", null, null, "100", null, null},
                {"NOTE","50", null, null, "50", null, null},
                {"NOTE","20", null, null, "20", null, null},
                {"NOTE","10", null, null, "10", null, null},
                {"NOTE","5", null, null, "5", null, null},
                {"NOTE","2", null, null, "2", null, null},
                {"NOTE","1", null, null, "1", null, null},
                {"COIN","20", null, null, "20", null, null},
                {"COIN","10", null, null, "10", null, null},
                {"COIN","5", null, null, "5", null, null},
                {"COIN","2", null, null, "2", null, null},
                {"COIN","1", null, null, "1", null, null},
                {"COIN","0.50",null, null, "1", null, null},
//                {null,"Others", null, null, "Others", null, null},
                {null,"Total", null, null, "Total", null, null}
            },
            new String [] {
//                "Receipts", "No", "Amount", "Payments", "No", "Amount"
                "Type","Receipts", "No", "Amount", "Payments", "No", "Amount"
            }
        ) {
            Class[] types = new Class [] {
               java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false,false, true, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (rowIndex==14) {
                    return false;
                }
                return canEdit [columnIndex];
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
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        panCashTrans = new com.see.truetransact.uicomponent.CPanel();
        btnApprove = new com.see.truetransact.uicomponent.CButton();
        scrTableScroll = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();

        getContentPane().setLayout(null);

        setMaximumSize(new java.awt.Dimension(430, 370));
        setMinimumSize(new java.awt.Dimension(430, 370));
        setPreferredSize(new java.awt.Dimension(430, 370));
        panCashTrans.setLayout(null);

        panCashTrans.setBorder(new javax.swing.border.EtchedBorder());
        btnApprove.setText("Save");
        btnApprove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApproveActionPerformed(evt);
            }
        });

        panCashTrans.add(btnApprove);
        btnApprove.setBounds(130, 290, 90, 27);

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
                java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
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
        tblData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblDataPropertyChange(evt);
            }
        });

        scrTableScroll.setViewportView(tblData);

        panCashTrans.add(scrTableScroll);
        scrTableScroll.setBounds(10, 10, 380, 280);

        getContentPane().add(panCashTrans);
        panCashTrans.setBounds(10, 10, 400, 320);

        pack();
    }//GEN-END:initComponents
    
    private void btnApproveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApproveActionPerformed
        // TODO add your handling code here:
        if (isDenominationTallied()) {
            getUpdateApproval();
          //  setModified(false);
            model.fireTableDataChanged();
            this.dispose();
        }
    }//GEN-LAST:event_btnApproveActionPerformed
    
    private boolean isDenominationTallied() {
        boolean tallied = true;
        try {
            double transAmt = CommonUtil.convertObjToDouble(lblAmt.replaceAll(",", "")).doubleValue();
            double receiptDenomination = 0;
            double paymentDenomination = 0;
            for (int i = 0; i<=14; i++) {
                if(tblData.getModel().getValueAt(i,2)!=null &&
                tblData.getModel().getValueAt(i,3)!=null ) {
                  //  if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
                        //totalPay += ((Double) tblData.getModel().getValueAt(i,2)).doubleValue();
                        //System.out.println("33-=="+tblData.getModel().getValueAt(i,1));
                        //System.out.println("22==="+tblData.getModel().getValueAt(i,2));
                        String amount = tblData.getModel().getValueAt(i,3).toString().replaceAll(",", "");
                        receiptDenomination+=CommonUtil.convertObjToDouble(amount).doubleValue();
                  //  }
                }
            }
            for (int i = 0; i<= 14; i++) {
                if(tblData.getModel().getValueAt(i,5)!=null &&
                tblData.getModel().getValueAt(i,6)!=null) {
                  //  if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
                        String amount = tblData.getModel().getValueAt(i,6).toString().replaceAll(",", "");
                        paymentDenomination+=CommonUtil.convertObjToDouble(amount).doubleValue();
                  //  }
                }
                
            }  
            double denominationAmount = 0.0;
            if(receiptDenomination>0.0 && lblTransTy.equals("CREDIT")){
                transMode="Receipts";
            }
            if(paymentDenomination>0.0 && lblTransTy.equals("DEBIT")){
                transMode="Payments";
            }
            if(transMode.equals("Receipts")){
                denominationAmount = receiptDenomination-paymentDenomination;
            }else{
                denominationAmount = paymentDenomination-receiptDenomination;
            }
//             double denominationAmount = receiptDenomination-paymentDenomination;
             if (denominationAmount!=transAmt) {
                 ClientUtil.showAlertWindow("Denomination Amount : "+denominationAmount+
                 "\nTransaction Amount :"+transAmt);
                 tallied = false;
             }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return tallied;
    }
    
    public void getUpdateApproval() {
        try {
        //    if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
             //   HashMap singleAuthorizeMap = new HashMap();
             //   singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            //    singleAuthorizeMap.put("TRANS_DT", currDt);
            //    singleAuthorizeMap.put(CommonConstants.TRANS_ID, trans_pid );
            //    singleAuthorizeMap.put(CommonConstants.BRANCH_ID,  ProxyParameters.BRANCH_ID);
                
           //     ClientUtil.execute("approveCashier", singleAuthorizeMap);
               
                insertIntoDenaomination();
              //  ((DefaultTreeModel)treData.getModel()).removeNodeFromParent(selectedNode);
                //                cboStatusActionPerformed(null);
            //}
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void tblDataPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblDataPropertyChange
        // TODO add your handling code here:
        //  int row=tblData.rowAtPoint(evt.getPoint());
        
        //     int col= tblData.columnAtPoint(evt.getPoint());
        
        //  getMultiplyValues(row,col);
    }//GEN-LAST:event_tblDataPropertyChange
    public void getMultiplyValues() {
        try {
            //System.out.println("R= "+row +" C="+col);
            //JOptionPane.showMessageDialog(null,tblData.getValueAt(row,col).toString());
            //  String AccNo=tblData.getValueAt(row,0).toString();
            //  String Name=tblData.getValueAt(row,1).toString();
            //   String Amt=tblData.getValueAt(row,3).toString();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }    
    public void clearTransDetails() {
        //lblScheme.setText("");
      //  lblName.setText("");
      //  lblTransId.setText("");
      //  lblTransType.setText("");
     //   lblAmount.setText("");
      //  lblPostedBy.setText("");
      //  lblAuthBy.setText("");
      //  lblParticulars.setText("");
      //  lblPanNo.setText("");
      //  lblAcNo.setText("");
      //  lblTkNo.setText("");
      //  lblInstType.setText("");
      //  lblInstNo.setText("");
      //  lblInstDate.setText("");
      //  lblClearBal.setText("");
      //  lblOpBal.setText("");
      //  lblReceipt.setText("");
      //  lblPayment.setText("");
       // lblClosingBal.setText("");
        initTableData();
    }
  
    private void enableDisableButtons(boolean val) {
        btnApprove.setEnabled(val);
       // btnReject.setEnabled(val);
    }
    
    private void displayAlert(String message){
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
        }
        catch(NumberFormatException nfe) {
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
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new CashierApprovalUI().show();
    }
    private DefaultMutableTreeNode addNode(DefaultMutableTreeNode parent,DefaultMutableTreeNode child,Object obj) {
        if (!chkExistance(parent,obj.toString())){
            child = new DefaultMutableTreeNode(obj);
        }
        if (!parent.isNodeChild(child)){
            parent.add(child);
        }
        return child;
    }
  /*  public DefaultTreeModel getAcHdTree(String status) {
        DefaultMutableTreeNode parent = new DefaultMutableTreeNode("Cashier");
        DefaultMutableTreeNode receipts = new DefaultMutableTreeNode("Receipts");
        DefaultMutableTreeNode payments = new DefaultMutableTreeNode("Payments");
        DefaultMutableTreeNode receiptsTr = null;
        DefaultMutableTreeNode paymentsTr = null;
        /////////////////////////////////////////////////////////////////////
        //  String status=cboStatus.getSelectedItem().toString();
        HashMap singleAuthorizeMap = new HashMap();
        singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt);
        singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, status);
        singleAuthorizeMap.put("TRANS_DT", currDt);
        //    singleAuthorizeMap.put("TYPE", "AUTHORIZED");
        singleAuthorizeMap.put(CommonConstants.BRANCH_ID,  ProxyParameters.BRANCH_ID);
        final List objList =  ClientUtil.executeQuery("getCashReceiptsTransTree", singleAuthorizeMap);
        final List objListPay =  ClientUtil.executeQuery("getCashPaymentsTransTree", singleAuthorizeMap);
        // final List objList = ClientUtil.executeQuery("getCashTransTree", null);
        final int objListSize = objList.size();
        for (int i = 0;i<objListSize;i++) {
            receiptsTr = addNode(receipts,receiptsTr,((HashMap)objList.get(i)).get("TRANS_ID"));
        }
        parent.add(receipts);
        for (int j = 0;j<objListPay.size();j++) {
            paymentsTr = addNode(payments,paymentsTr,((HashMap)objListPay.get(j)).get("TRANS_ID"));
        }
        parent.add(payments);
        final DefaultTreeModel treemodel = new DefaultTreeModel(parent);
        //  final DefaultTreeModel treemodelPay = new DefaultTreeModel(payments);
        treData.setModel(treemodel);
        //    treData.setModel(treemodelPay);
        root = null;
        receiptsTr = null;
        paymentsTr = null;
        
        
        return treemodel;
    }*/
    private boolean chkExistance(DefaultMutableTreeNode parent,String chkStr) {
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
    private com.see.truetransact.uicomponent.CPanel panCashTrans;
    private com.see.truetransact.uicomponent.CScrollPane scrTableScroll;
    private com.see.truetransact.uicomponent.CTable tblData;
    // End of variables declaration//GEN-END:variables
    
}

