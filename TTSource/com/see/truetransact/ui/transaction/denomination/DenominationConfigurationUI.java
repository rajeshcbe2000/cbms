/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * DenominationConfigurationUI.java
 */
package com.see.truetransact.ui.transaction.denomination;

import java.util.HashMap;
import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.TrueTransactMain;
import java.awt.Color;
import java.awt.Component;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;
import java.math.BigDecimal;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.LinkedHashMap;
public class DenominationConfigurationUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer {

    Date currDt = null;
    DefaultTableModel model = null;
    private String viewType = new String();
    private HashMap mandatoryMap;
    public String particulars = "";
    double Opamount = 0;
    public String Cramount = "";
    private ComboBoxModel cbmBranch_Code;
    final String AUTHORIZE = "Authorize";
    private final static Logger log = Logger.getLogger(DenominationConfigurationUI.class);
    double totalPay = 0;
    double totalCount = 0;
    private String prodType = "";
    int denAmt;
    double Clamount = 0;
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    String cust_type = null;
    public String transMode = "";
    String amtGlobal = "";
    String lblAmt = "", lblTransTy;
    private TableModelListener tableModelListener;
    ArrayList colorList = new ArrayList();

    /** Creates new form CustomerIdChangeUI */
    public DenominationConfigurationUI() {
        initComponents();
        initStartUp();
        initTableData();
        setupScreen();
        fillDropDown();
        cbobranch.setModel(getCbmBranch_Code());
        cbobranch.setSelectedItem(TrueTransactMain.BRANCH_ID);
        currDt = ClientUtil.getCurrentDate();
        //tdtDenoDt.setDateValue(currDt);
        tdtDenoDt.setDateValue(DateUtil.getStringDate(currDt));
        setCrntBalanceAmount();
        //setOpBalanceAmount();

    }

   /* public void setOpBalanceAmount() {
   
        currDt = currDt.clone();
        HashMap singleAuthorizeMapOpBal = new HashMap();
        singleAuthorizeMapOpBal.put("TRANS_DT", currDt);
        singleAuthorizeMapOpBal.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        List aListOp = ClientUtil.executeQuery("getOpBalAmount", singleAuthorizeMapOpBal);
        lblClBal.setText(aListOp.get(0).toString());
        if (aListOp.size() > 0 && aListOp.get(0) != null) {
            HashMap mapop = (HashMap) aListOp.get(0);
            Opamount = mapop.get("OPAMOUNT").toString();
            Clamount = Double.parseDouble(Opamount) + totalPay;
            //System.out.println("Clamount==========" + Clamount + "'''''''''" + totalPay);
        if (Opamount != null && !Opamount.equalsIgnoreCase("")) {
                lblClBal.setText(formatCrore("" + Clamount));
            }
        amtGlobal = mapop.get("OPAMOUNT").toString();
            // else 

        }
             
    }*/
    public void setCrntBalanceAmount() {
        double receiptAmt = 0;
        double paymentAmt = 0;
        double opval = 0;
        double amount1 = 0;
        HashMap singleAuthorizeMapOpBal = new HashMap();
        singleAuthorizeMapOpBal.put("TRANS_DT", currDt);
        singleAuthorizeMapOpBal.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        List aListOp = ClientUtil.executeQuery("getOpBalAmount", singleAuthorizeMapOpBal);
        // lblOpBal.setText("0.00");
        System.out.println("aListOp ---------------- " + aListOp);
        System.out.println("aListOp ------33---------- " + aListOp.size());
        if (aListOp.size() > 0 && aListOp.get(0) != null) {
            HashMap mapop = (HashMap) aListOp.get(0);
            Opamount = CommonUtil.convertObjToDouble(mapop.get("CURAMOUNT"));
            if (Opamount>0) // lblOpBal.setText(formatCrore(Opamount));
            {
                opval = Opamount;
            }
            // else 

        }
               
        HashMap singleAuthorizeMap1 = new HashMap();
        singleAuthorizeMap1.put("TRANS_DT", currDt);
        singleAuthorizeMap1.put(CommonConstants.INITIATED_BRANCH, ProxyParameters.BRANCH_ID);
        singleAuthorizeMap1.put("CASHIER_APPROVAL_STATUS", TrueTransactMain.CASHIER_AUTH_ALLOWED);
        List aListA = ClientUtil.executeQuery("getSumPayRecAmount", singleAuthorizeMap1);//getSumPayRecAmountUnion
        for (int i = 0; i < aListA.size(); i++) {
            HashMap map = (HashMap) aListA.get(i);
            if (map.get("AMOUNT") != null && map.get("TRANS_TYPE").equals("CREDIT")) {//REC_AMOUNT
                amount1 = CommonUtil.convertObjToDouble(map.get("AMOUNT"));
                if (amount1 > 0) {
                    receiptAmt =amount1;
                }
            }
            if (map.get("AMOUNT") != null && map.get("TRANS_TYPE").equals("DEBIT")) {//REC_AMOUNT
                amount1 = CommonUtil.convertObjToDouble(map.get("AMOUNT"));
                if (amount1 > 0) {
                    paymentAmt = amount1;
                }
            }
        }
        System.out.println("opval -- " + opval + "receiptAmt == " + receiptAmt + "paymentAmt==" + paymentAmt);
        double totVal = opval + receiptAmt - paymentAmt;
        Clamount = totVal;
        lblCrBal.setText(formatCrore(String.valueOf(totVal)));        
    }


    private void setupScreen() {
        setTitle("Change [" + ProxyParameters.BRANCH_ID + "]");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("@$#@$#@# screenSize : " + screenSize);
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    public void initTableData() {
        String data[][] = {{}};
       String col[] = {"Account No", "Name", "Deposit Date", "Amount", "Maturity Date"};
        DefaultTableModel dataModel = new DefaultTableModel();
       model = new DefaultTableModel(data, col);
        tblData.setModel(new javax.swing.table.DefaultTableModel(
            setTableData(),   
                new String[]{
                 "Curr_Type", "Denomination", "No", "Amount"
                    }) {

            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (rowIndex == 11 ||rowIndex == 22||rowIndex == 25 ||rowIndex == 24) {
                    return false;
                }
                return canEdit[columnIndex];
            }
        });
        tblData.setCellSelectionEnabled(true);
        setColour();
        calculateTotalValueOfDenominations();
    }
 public void fillDropDown() {
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        HashMap mapBranch = new HashMap();
        List keyValue = null;
        //setCbmShare_Type(Share_Type);

        keyValue = ClientUtil.executeQuery("getBranchcodeValueForDenomination", mapBranch);
        System.out.println("keyValue=======" + keyValue);
        key.add("");
        value.add("");

        if (keyValue != null && keyValue.size() > 0) {
            for (int i = 0; i < keyValue.size(); i++) {
                mapBranch = (HashMap) keyValue.get(i);
                key.add(mapBranch.get("BRANCH_CODE"));
                value.add(mapBranch.get("BRANCH_CODE"));
            }
        }
        System.out.println("key======" + key);
        System.out.println("value======" + value);
        cbmBranch_Code = new ComboBoxModel(key, value);
        key = null;
        value = null;

        keyValue.clear();
        keyValue = null;
        mapBranch.clear();
        mapBranch = null;
    }

     private void setColour() {
        /* Set a cellrenderer to this table in order format the date */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                 if (row==24 || row==20 || row==22) {
                    setForeground(Color.BLUE );
                    setBackground(Color.BLACK);
                    setFont(new java.awt.Font("Tahoma", Font.BOLD, 11));
                } else if (row==25 ) {
                    setForeground(Color.RED);
                    setBackground(Color.BLACK);
                    setFont(new java.awt.Font("Tahoma", Font.BOLD, 12));
                }else {
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblData.setDefaultRenderer(Object.class, renderer);
    }
     
    private void setTableModelListener() {
       try{
        tableModelListener = new TableModelListener() {
        public void tableChanged(TableModelEvent e) {
             if (e.getType() == TableModelEvent.UPDATE) {
                       System.out.println("Cell " + e.getFirstRow() + ", "
                        + e.getColumn() + " changed. The new value: "
                        + tblData.getModel().getValueAt(e.getFirstRow(),
                            e.getColumn()));
                        int row = e.getFirstRow();
                        System.out.println("row======"+row);
                        int column = e.getColumn();
                       if (row != 19)  {
                        if (column == 1 || column == 2) {
                        TableModel model = tblData.getModel();
                        String quantity = model.getValueAt(row, 1).toString();
                            if (model.getValueAt(row, 2) != null && !model.getValueAt(row, 2).toString().equals("") && !isNumeric(model.getValueAt(row, 2).toString())) {
                                System.out.println("4356346436346====");
                                ClientUtil.showAlertWindow("Please enter Numeric Value!!!");
                                model.setValueAt("0", row, 2);
                                return;
                            }
                            if (model.getValueAt(row, 1) != null) {
                                if (row == 19) {
                                   quantity = "1";
                                }

                                double price = CommonUtil.convertObjToDouble(model.getValueAt(row, 2)).doubleValue();
                                Double value = null;
                                if (quantity.equals("0.50")) {
                                    value = new Double(0.50 * price);
                                } else if(quantity.equals("0.25"))
                                {
                                    value = new Double(0.25 * price); 
                                }
                                        else if(quantity.equals("0.10"))
                                {
                                    value = new Double(0.10 * price); 
                                }
                                                else if(quantity.equals("0.05"))
                                {
                                    value = new Double(0.05 * price); 
                                }
                                     else if(quantity.equals("0.01"))
                                {
                                    value = new Double(0.01 * price); 
                                }
                                    else{
                                    value = new Double(Double.parseDouble(quantity) * price);
                                }
                                                               
                                model.setValueAt(formatCrore(String.valueOf(value)), row, 3);
                                BigDecimal bm = null;
                                int count =0;
                                totalPay=0;totalCount=0;
                                double totalNote = 0;double totalCoin = 0;double totalStamp = 0;
                                double totalNoteAmt = 0;double totalCoinAmt = 0;double totalStampAmt = 0;
                                for (int i = 0; i < 19; i++) {
                                if (tblData.getModel().getValueAt(i, 3) != null && tblData.getModel().getValueAt(i, 0)!=null) {
                                    String val = tblData.getModel().getValueAt(i, 3).toString();
                                    //System.out.println("#%@%@%@"+val);
                                    val = val.replaceAll(",","");
                                    totalPay += Double.parseDouble(val);
                                    totalCount += CommonUtil.convertObjToDouble(tblData.getModel().getValueAt(i, 2));
                                    //System.out.println("(tblData.getModel().getValueAt(i, 0))" + (tblData.getModel().getValueAt(i, 0)));
                                    //System.out.println("(tblData.getModel().getValueAt(i, 3))" + (tblData.getModel().getValueAt(i, 3)));
                                    if(CommonUtil.convertObjToStr(tblData.getModel().getValueAt(i, 0)).equalsIgnoreCase("NOTE")){
                                        totalNote += CommonUtil.convertObjToDouble(tblData.getModel().getValueAt(i, 2));   
                                        String val1 = tblData.getModel().getValueAt(i, 3).toString();
                                        val1 = val1.replaceAll(",","");
                                        totalNoteAmt += Double.parseDouble(val1);
                                    }else if(CommonUtil.convertObjToStr(tblData.getModel().getValueAt(i, 0)).equalsIgnoreCase("COIN")){
                                        totalCoin += CommonUtil.convertObjToDouble(tblData.getModel().getValueAt(i, 2));
                                        String val2 = tblData.getModel().getValueAt(i, 3).toString();
                                        val2 = val2.replaceAll(",","");
                                        totalCoinAmt += Double.parseDouble(val2);
                                    }else if(CommonUtil.convertObjToStr(tblData.getModel().getValueAt(i, 0)).equalsIgnoreCase("STAMP")){
                                        totalStamp += CommonUtil.convertObjToDouble(tblData.getModel().getValueAt(i, 2));
                                        String val3 = tblData.getModel().getValueAt(i, 3).toString();
                                        val3 = val3.replaceAll(",","");
                                        totalStampAmt += Double.parseDouble(val3);
                                    }                                    
                                }
                                }
//                                model.setValueAt(formatCrore(String.valueOf(Double.valueOf(totalPay))), 18, 3);//formatCrore(
//                                model.setValueAt(CommonUtil.convertObjToInt(totalCount), 18, 2);
//                                model.setValueAt(formatCrore(String.valueOf(Double.valueOf(totalCoinAmt))), 22, 3);
//                                model.setValueAt(CommonUtil.convertObjToInt(totalCoin), 22, 2);
//                                model.setValueAt(formatCrore(String.valueOf(Double.valueOf(totalNoteAmt))), 11, 3);
//                                model.setValueAt(CommonUtil.convertObjToInt(totalNote), 11, 2);
//                                model.setValueAt(formatCrore(String.valueOf(Double.valueOf(totalStampAmt))), 24, 3);
//                                model.setValueAt(CommonUtil.convertObjToInt(totalStamp), 24, 2);
//                                System.out.println("totalNoteAmt" + totalNoteAmt);
                               DmBal.setText(formatCrore(String.valueOf(Double.valueOf(totalPay))));
                                //lblTotalNote.setText("Notes     : "+CommonUtil.convertObjToInt(totalNote) + "   Total  :"+formatCrore(String.valueOf(Double.valueOf(totalNoteAmt))));
                                //lblTotalCoin.setText("Coins     : "+CommonUtil.convertObjToInt(totalCoin)  + "  Total  :"+formatCrore(String.valueOf(Double.valueOf(totalCoinAmt))));
                                //lblTotalStamp.setText("Stamps   : "+CommonUtil.convertObjToInt(totalStamp) + "  Total  :"+formatCrore(String.valueOf(Double.valueOf(totalStampAmt))));
                            }



                            //   denAmt  =Integer.parseInt(""+model.getValueAt(18,3));
                        }
                       /* if (column == 4 || column == 5) {
                            TableModel model = tblData.getModel();
                            String quantity = model.getValueAt(row, 4).toString();

                            if (model.getValueAt(row, 5) != null && !model.getValueAt(row, 5).toString().equals("") && !isNumeric(model.getValueAt(row, 5).toString())) {
                                ClientUtil.showAlertWindow("Please enter Numeric Value!!!");
                                model.setValueAt("0", row, 5);
                                return;
                            }
                            if (model.getValueAt(row, 4) != null) {
                                if (row == 15) {
                                    // System.out.println("ROWW==111=="+row);
                                    quantity = "1";
                                }
                                double price = CommonUtil.convertObjToDouble(model.getValueAt(row, 5)).doubleValue();
                                Double value = new Double(Integer.parseInt(quantity) * price);
                                model.setValueAt(formatCrore(String.valueOf(value)), row, 6);

                                //Set tiotal
                                double totalRec = 0;

                                for (int i = 0; i < 19; i++) {
                                    //  total +=((Double) model.getValueAt(i, 2)).doubleValue();
                                    if (tblData.getModel().getValueAt(i, 6) != null) {
                                        String val = tblData.getModel().getValueAt(i, 6).toString();
                                        val = val.replaceAll(",", "");
                                        // totalRec +=   Double.parseDouble(tblData.getModel().getValueAt(i,5).toString());
                                        totalRec += Double.parseDouble(val);
                                    }

                                }
                                model.setValueAt(formatCrore(String.valueOf(Double.valueOf(totalRec))), 19, 6);  //formatCrore(
                                //  setModified(true);
                            }
                        }*/
                    }else{
                            colorList.add(e.getFirstRow());
                        }
//setOpBalanceAmount();


                }
            }
        };
        tblData.getModel().addTableModelListener(tableModelListener);
        
        // denAmt  =Integer.parseInt(""+model.getValueAt(18,3));  
    }catch(Exception e){
    e.printStackTrace();
}
    }

    private void initStartUp() {

        setFieldNames();
        internationalize();
       
        initComponentData();
        enableDisable(false);
        //Added by Anju 15/5/2014
        cbobranch.setEnabled(true);
        cbobranch.setSelectedItem(ProxyParameters.BRANCH_ID);       
        setButtonEnableDisable();
        setMaxLength();


        setHelpMessage();
    }

    public ComboBoxModel getCbmBranch_Code() {
        return cbmBranch_Code;
    }

    public void setCbmBranch_Code(ComboBoxModel cbmBranch_Code) {
        this.cbmBranch_Code = cbmBranch_Code;
    }

    private void setMaxLength() {
    }

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    public void setHelpMessage() {
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panOutwardRegister = new com.see.truetransact.uicomponent.CPanel();
        panDenominationBalance = new javax.swing.JPanel();
        lblBalance1 = new com.see.truetransact.uicomponent.CLabel();
        lblcurrentBal = new com.see.truetransact.uicomponent.CLabel();
        DmBal = new com.see.truetransact.uicomponent.CLabel();
        lblCrBal = new com.see.truetransact.uicomponent.CLabel();
        lblTotalNote = new com.see.truetransact.uicomponent.CLabel();
        lblTotalCoin = new com.see.truetransact.uicomponent.CLabel();
        lblTotalStamp = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        scrTableScroll = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        btnView = new com.see.truetransact.uicomponent.CButton();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        tdtDenoDt = new com.see.truetransact.uicomponent.CDateField();
        lblbranch = new com.see.truetransact.uicomponent.CLabel();
        cbobranch = new com.see.truetransact.uicomponent.CComboBox();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        panDenominationBalance1 = new javax.swing.JPanel();
        lblBalance2 = new com.see.truetransact.uicomponent.CLabel();
        DmBal1 = new com.see.truetransact.uicomponent.CLabel();
        lblcurrentBal1 = new com.see.truetransact.uicomponent.CLabel();
        lblCrBal1 = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrCustomer = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptNew = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptSave = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMaximumSize(new java.awt.Dimension(800, 625));
        setMinimumSize(new java.awt.Dimension(800, 625));
        setPreferredSize(new java.awt.Dimension(800, 625));

        panOutwardRegister.setMaximumSize(new java.awt.Dimension(550, 500));
        panOutwardRegister.setMinimumSize(new java.awt.Dimension(550, 500));
        panOutwardRegister.setPreferredSize(new java.awt.Dimension(550, 500));
        panOutwardRegister.setLayout(new java.awt.GridBagLayout());

        panDenominationBalance.setMaximumSize(new java.awt.Dimension(600, 300));
        panDenominationBalance.setMinimumSize(new java.awt.Dimension(600, 300));
        panDenominationBalance.setPreferredSize(new java.awt.Dimension(600, 300));
        panDenominationBalance.setLayout(new java.awt.GridBagLayout());

        lblBalance1.setText("Denomination Balance    :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panDenominationBalance.add(lblBalance1, gridBagConstraints);

        lblcurrentBal.setText("Curr Balance                  :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 29;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panDenominationBalance.add(lblcurrentBal, gridBagConstraints);

        DmBal.setForeground(new java.awt.Color(0, 0, 204));
        DmBal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        DmBal.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        panDenominationBalance.add(DmBal, gridBagConstraints);

        lblCrBal.setForeground(new java.awt.Color(0, 0, 204));
        lblCrBal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCrBal.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        panDenominationBalance.add(lblCrBal, gridBagConstraints);

        lblTotalNote.setForeground(new java.awt.Color(204, 0, 0));
        lblTotalNote.setFont(new java.awt.Font("MS Sans Serif", 1, 12)); // NOI18N
        lblTotalNote.setPreferredSize(new java.awt.Dimension(265, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 100);
        panDenominationBalance.add(lblTotalNote, gridBagConstraints);

        lblTotalCoin.setForeground(new java.awt.Color(204, 0, 0));
        lblTotalCoin.setFont(new java.awt.Font("MS Sans Serif", 1, 12)); // NOI18N
        lblTotalCoin.setPreferredSize(new java.awt.Dimension(265, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 100);
        panDenominationBalance.add(lblTotalCoin, gridBagConstraints);

        lblTotalStamp.setForeground(new java.awt.Color(204, 0, 0));
        lblTotalStamp.setFont(new java.awt.Font("MS Sans Serif", 1, 12)); // NOI18N
        lblTotalStamp.setPreferredSize(new java.awt.Dimension(265, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 100);
        panDenominationBalance.add(lblTotalStamp, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 16;
        gridBagConstraints.ipadx = 49;
        gridBagConstraints.ipady = -235;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 0);
        panOutwardRegister.add(panDenominationBalance, gridBagConstraints);

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.ipady = -3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 127, 0, 0);
        panOutwardRegister.add(btnSave, gridBagConstraints);

        scrTableScroll.setPreferredSize(new java.awt.Dimension(452, 320));

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Curr_Type", "Denomination", "No", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false
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
        tblData.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.ipadx = 598;
        gridBagConstraints.ipady = 323;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        panOutwardRegister.add(scrTableScroll, gridBagConstraints);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Process");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 10, 0, 0);
        panOutwardRegister.add(btnView, gridBagConstraints);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setText("Denom Value For An OldDate");
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 141, 0, 0);
        panOutwardRegister.add(btnEdit, gridBagConstraints);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 26;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 26;
        gridBagConstraints.ipady = -3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 2);
        panOutwardRegister.add(btnClose, gridBagConstraints);

        lblSpace3.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(17, 0, 0, 0);
        panOutwardRegister.add(lblSpace3, gridBagConstraints);

        tdtDenoDt.setMaximumSize(new java.awt.Dimension(101, 21));
        tdtDenoDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDenoDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 4, 0, 0);
        panOutwardRegister.add(tdtDenoDt, gridBagConstraints);

        lblbranch.setText("Branch");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipady = -2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 141, 0, 0);
        panOutwardRegister.add(lblbranch, gridBagConstraints);

        cbobranch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "0001", "0002", "0003" }));
        cbobranch.setMaximumSize(new java.awt.Dimension(100, 21));
        cbobranch.setMinimumSize(new java.awt.Dimension(100, 21));
        cbobranch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbobranchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = -3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panOutwardRegister.add(cbobranch, gridBagConstraints);

        btnNew.setText("New");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 43;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 21, 0, 0);
        panOutwardRegister.add(btnNew, gridBagConstraints);

        panDenominationBalance1.setMaximumSize(new java.awt.Dimension(600, 300));
        panDenominationBalance1.setMinimumSize(new java.awt.Dimension(600, 300));
        panDenominationBalance1.setPreferredSize(new java.awt.Dimension(600, 300));
        panDenominationBalance1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblBalance2.setText("Denomination Balance    :");
        panDenominationBalance1.add(lblBalance2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 170, -1));

        DmBal1.setForeground(new java.awt.Color(0, 0, 255));
        DmBal1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DmBal1.setText(particulars);
        DmBal1.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        DmBal1.setMaximumSize(new java.awt.Dimension(74, 14));
        DmBal1.setMinimumSize(new java.awt.Dimension(74, 14));
        DmBal1.setPreferredSize(new java.awt.Dimension(174, 14));
        panDenominationBalance1.add(DmBal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 130, 20));

        lblcurrentBal1.setText("Curr Balance                  :");
        panDenominationBalance1.add(lblcurrentBal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 180, -1));

        lblCrBal1.setForeground(new java.awt.Color(0, 0, 255));
        lblCrBal1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCrBal1.setText(particulars);
        lblCrBal1.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCrBal1.setMaximumSize(new java.awt.Dimension(74, 14));
        lblCrBal1.setMinimumSize(new java.awt.Dimension(74, 14));
        lblCrBal1.setPreferredSize(new java.awt.Dimension(174, 14));
        panDenominationBalance1.add(lblCrBal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 40, 110, 20));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 16;
        gridBagConstraints.ipadx = 49;
        gridBagConstraints.ipady = -235;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 0);
        panOutwardRegister.add(panDenominationBalance1, gridBagConstraints);

        getContentPane().add(panOutwardRegister, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        mbrCustomer.setName("mbrCustomer"); // NOI18N

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess"); // NOI18N

        mitNew.setText("New");
        mitNew.setName("mitNew"); // NOI18N
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit"); // NOI18N
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete"); // NOI18N
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew"); // NOI18N
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave"); // NOI18N
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel"); // NOI18N
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave"); // NOI18N
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint"); // NOI18N
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose"); // NOI18N
        mnuProcess.add(mitClose);

        mbrCustomer.add(mnuProcess);

        setJMenuBar(mbrCustomer);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        for (int k = 0; k < tblData.getRowCount(); k++) {
            tblData.getModel().setValueAt(0, k, 3);
            tblData.getModel().setValueAt(0, k, 2);
        }
        if (DateUtil.getDateMMDDYYYY(tdtDenoDt.getDateValue()).equals(currDt.clone())|| DateUtil.getDateMMDDYYYY(tdtDenoDt.getDateValue()).equals(null)) {
            tblData.setEnabled(true);
            btnSave.setEnabled(true);
        }
        HashMap whereMap = new HashMap();
        whereMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtDenoDt.getDateValue()));
        List lst1 = ClientUtil.executeQuery("getDenomValueFrmTable", whereMap);
        System.out.println("lst1lst1lst1lst1" + lst1);
        HashMap Map = new HashMap();


        for (int j = 0; j < lst1.size(); j++) {
            for (int i = 0; i < (tblData.getRowCount() - 1); i++) {
                Map = (HashMap) lst1.get(j);

                System.out.println("tblData.getModel().getValueAt(0,i)===" + tblData.getModel().getValueAt(i, 1));
                if (tblData.getModel().getValueAt(i, 1).equals(Map.get("D_TYPE"))
                        && tblData.getModel().getValueAt(i, 0).equals(Map.get("DENOM_TYPE"))) {

                    tblData.getModel().setValueAt(Map.get("D_VAL"), i, 3);
                    tblData.getModel().setValueAt(Map.get("D_COUNT"), i, 2);

                }
            }
            System.out.println("Map88888888" + Map);
        }

        System.out.println("]]]]]]]]]]Map" + Map);

    }//GEN-LAST:event_btnViewActionPerformed

    private Object[][] setTableData() {
        LinkedHashMap whereMap = new LinkedHashMap();
        LinkedHashMap transactionMap = new LinkedHashMap();
        colorList = new ArrayList();
       
        List denomList = ClientUtil.executeQuery("getDenominationData", whereMap);
        //list1... :: [{CURR_TYPE=C, DENOMINATION=0.05, DENOM_DESC=0.05}, {CURR_TYPE=C, DENOMINATION=0.10, DENOM_DESC=0.10}, {CURR_TYPE=C, DENOMINATION=0.20, DENOM_DESC=0.20}, {CURR_TYPE=C, DENOMINATION=0.25, DENOM_DESC=0.25}, {CURR_TYPE=C, DENOMINATION=0.50, DENOM_DESC=0.50}, {CURR_TYPE=C, DENOMINATION=1.00, DENOM_DESC=1.00}, {CURR_TYPE=C, DENOMINATION=2.00, DENOM_DESC=2.00}, {CURR_TYPE=C, DENOMINATION=5.00, DENOM_DESC=5.00}, {CURR_TYPE=C, DENOMINATION=10.00, DENOM_DESC=10.00}, {CURR_TYPE=N, DENOMINATION=1.00, DENOM_DESC=1.00}, {CURR_TYPE=N, DENOMINATION=2.00, DENOM_DESC=2.00}, {CURR_TYPE=N, DENOMINATION=5.00, DENOM_DESC=5.00}, {CURR_TYPE=N, DENOMINATION=10.00, DENOM_DESC=10.00}, {CURR_TYPE=N, DENOMINATION=20.00, DENOM_DESC=20.00}, {CURR_TYPE=N, DENOMINATION=50.00, DENOM_DESC=50.00}, {CURR_TYPE=N, DENOMINATION=100.00, DENOM_DESC=100.00}, {CURR_TYPE=N, DENOMINATION=500.00, DENOM_DESC=500.00}, {CURR_TYPE=N, DENOMINATION=1000.00, DENOM_DESC=1000.00}, {CURR_TYPE=N, DENOMINATION=2000.00, DENOM_DESC=2000.00}]
//Populate complete data

        System.out.println("list1... :: " + denomList );
        HashMap processMap = new HashMap();
        if (denomList != null && denomList.size() > 0) {
            Object totalList[][] = new Object[denomList .size()][19];
            
            for (int j = 0; j < denomList.size(); j++) {
                processMap = (HashMap) denomList.get(j);
                if( CommonUtil.convertObjToStr(processMap.get("CURR_TYPE")).equals("C")){
                    totalList[j][0] = "COIN";
                }else if( CommonUtil.convertObjToStr(processMap.get("CURR_TYPE")).equals("N")){
                    totalList[j][0] = "NOTE";
                }else if( CommonUtil.convertObjToStr(processMap.get("CURR_TYPE")).equals("S")){
                    totalList[j][0] = "STAMP";
                }
                //totalList[j][0] = CommonUtil.convertObjToStr(processMap.get("CURR_TYPE"));
                totalList[j][1] = CommonUtil.convertObjToStr(processMap.get("DENOMINATION"));
               
            }
            System.out.println("Populate complete data");
            return totalList;
        } else {
            ClientUtil.displayAlert("No Data!!! ");
        }
        return null;
    }


 



    
    
    
    
    
    
    
    
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mitCloseActionPerformed(evt);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
        cifClosingAlert();
    }

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
    }//GEN-LAST:event_mitNewActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
    }//GEN-LAST:event_mitSaveActionPerformed

private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    tdtDenoDt.setEnabled(true);
    tblData.setEnabled(false);
    btnSave.setEnabled(false);
    lblCrBal.setVisible(false);
    cbobranch.setEnabled(true);

}//GEN-LAST:event_btnEditActionPerformed

private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
    // TODO add your hanhashdling code here:
   
    
        if (isTallied()){
        
        int result = ClientUtil.confirmationAlert("Save Dinomination?"); 
        if (result == 0) {
            getUpdateApproval();
            // model.fireTableDataChanged();
            this.dispose();
            ClientUtil.displayAlert("Amount is saved!!!");
        } else {
            initTableData();
            setCrntBalanceAmount();
            DmBal.setText("");
            totalPay=0;
            totalCount=0;
            return;
        }

    } else {
        ClientUtil.displayAlert("Amount not tallied!!!");
        totalPay = 0;
        totalCount=0;
        return;
    }





}//GEN-LAST:event_btnSaveActionPerformed
private boolean isTallied() {
        boolean tallied = true;
        System.out.println("Label total #############"+Clamount);
        System.out.println("Entered amount #############"+totalPay);
        System.out.println("DmBal getText####"+DmBal.getText());
        System.out.println("lblCrBal getText #############"+lblCrBal.getText());
     //   if(Clamount==totalPay){
        if(DmBal.getText().equals(lblCrBal.getText())){
            tallied=true;
        }else{
           tallied=false;
        }
        return tallied;
}


private void calculateTotalValueOfDenominations(){
    double coinValue = 0.0;
    double noteValue = 0.0;
    double stampValue = 0.0;
    double totDenoValue = 0.0;
    double totalNote = 0;
    double totalCoin = 0;
    double totalStamp = 0;
    if(tblData.getRowCount() > 0){
        for(int i=0; i<tblData.getRowCount(); i++){
            if(CommonUtil.convertObjToStr(tblData.getValueAt(i, 0)).equals("COIN")){
                coinValue += CommonUtil.convertObjToDouble(tblData.getValueAt(i, 3));
                totalCoin += CommonUtil.convertObjToInt(tblData.getValueAt(i, 2));
            }else if(CommonUtil.convertObjToStr(tblData.getValueAt(i, 0)).equals("NOTE")){
                noteValue += CommonUtil.convertObjToDouble(tblData.getValueAt(i, 3));
                totalNote += CommonUtil.convertObjToInt(tblData.getValueAt(i, 2));
            }else if(CommonUtil.convertObjToStr(tblData.getValueAt(i, 0)).equals("STAMP")){
                stampValue += CommonUtil.convertObjToDouble(tblData.getValueAt(i, 3));
                totalStamp = totalStamp + CommonUtil.convertObjToInt(tblData.getValueAt(i, 2));
            }
            totDenoValue += CommonUtil.convertObjToDouble(tblData.getValueAt(i, 3));
        }
    }
    DmBal.setText(String.valueOf(totDenoValue));
    lblTotalNote.setText("Notes      : "+CommonUtil.convertObjToInt(totalNote) + "   Total  :"+formatCrore(String.valueOf(Double.valueOf(noteValue))));
    lblTotalCoin.setText("Coins      : "+CommonUtil.convertObjToInt(totalCoin)  + "  Total  :"+formatCrore(String.valueOf(Double.valueOf(coinValue))));
    lblTotalStamp.setText("Stamps   : "+CommonUtil.convertObjToInt(totalStamp) + "  Total  :"+formatCrore(String.valueOf(Double.valueOf(stampValue))));                           
    
}

private void tblDataPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblDataPropertyChange
   
    double value = 0.0;
    double denomination = 0.0;
    double amount = 0.0;
    if (tblData.getSelectedRowCount() > 0) {
        int row = tblData.getSelectedRow();
        int column = tblData.getEditingColumn();
        if (column == 2) {
            denomination = CommonUtil.convertObjToDouble(tblData.getValueAt(row, 1));
            amount = CommonUtil.convertObjToDouble(tblData.getValueAt(row, 2));
            value = denomination * amount;
            tblData.setValueAt(value, row, 3);            
        }
    }
    calculateTotalValueOfDenominations();    
}//GEN-LAST:event_tblDataPropertyChange

private void tdtDenoDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDenoDtFocusLost
}//GEN-LAST:event_tdtDenoDtFocusLost

private void cbobranchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbobranchActionPerformed
  //Added by Anju 16/04/2014
//    currDt = currDt.clone();
//       HashMap singleAuthorizeMapOpBal = new HashMap();
//       singleAuthorizeMapOpBal.put("BRANCH_CODE",CommonUtil.convertObjToStr(cbobranch.getSelectedItem()));
//       List aListOp = ClientUtil.executeQuery("getCurrBalance", singleAuthorizeMapOpBal);
//       DmBal.setText("0.00");
//                if (aListOp.size() > 0 && aListOp.get(0) != null) {
//                    HashMap mapop = (HashMap) aListOp.get(0);
//                    Cramount = mapop.get("CURAMOUNT").toString();
//                if (Cramount != null ) {
//                    lblCrBal.setText(formatCrore("" + Cramount));
//                }
//
//        }
    setCrntBalanceAmount();
}//GEN-LAST:event_cbobranchActionPerformed

private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
btnEdit.setEnabled(false);
tdtDenoDt.setEnabled(false);
btnView.setEnabled(false);
}//GEN-LAST:event_btnNewActionPerformed
//    private boolean isDenominationTallied() {
//        boolean tallied = true;
//        try {
//            double transAmt = CommonUtil.convertObjToDouble(lblAmt.replaceAll(",", "")).doubleValue();
//            double receiptDenomination = 0;
//            double paymentDenomination = 0;
//            for (int i = 0; i <= 18; i++) {
//                if (tblData.getModel().getValueAt(i, 2) != null
//                        && tblData.getModel().getValueAt(i, 3) != null) {
//                    //  if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
//                    //totalPay += ((Double) tblData.getModel().getValueAt(i,2)).doubleValue();
//                    //System.out.println("33-=="+tblData.getModel().getValueAt(i,1));
//                    //System.out.println("22==="+tblData.getModel().getValueAt(i,2));
//                    String amount = tblData.getModel().getValueAt(i, 3).toString().replaceAll(",", "");
//                    receiptDenomination += CommonUtil.convertObjToDouble(amount).doubleValue();
//                    //  }
//                }
//            }
//            for (int i = 0; i <= 18; i++) {
//                if (tblData.getModel().getValueAt(i, 5) != null
//                        && tblData.getModel().getValueAt(i, 6) != null) {
//                    //  if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
//                    String amount = tblData.getModel().getValueAt(i, 6).toString().replaceAll(",", "");
//                    paymentDenomination += CommonUtil.convertObjToDouble(amount).doubleValue();
//                    //  }
//                }
//
//            }
//            double denominationAmount = 0.0;
//            if (receiptDenomination > 0.0 && lblTransTy.equals("CREDIT")) {
//                transMode = "Receipts";
//            }
////            if(paymentDenomination>0.0 && lblTransTy.equals("DEBIT")){
////                transMode="Payments";
////            }
//            if (transMode.equals("Receipts")) {
//                denominationAmount = receiptDenomination - paymentDenomination;
//            } else {
//                denominationAmount = paymentDenomination - receiptDenomination;
//            }
////             double denominationAmount = receiptDenomination-paymentDenomination;
//            if (denominationAmount != transAmt) {
//                ClientUtil.showAlertWindow("Denomination Amount : " + denominationAmount
//                        + "\nTransaction Amount :" + transAmt);
//                tallied = false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return tallied;
//    }

    public void getUpdateApproval() {
        try {
            DuplicateCheck();
            insertIntoDenaomination();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void DuplicateCheck() {
            HashMap dataMap=new HashMap();
            dataMap.put("TRANS_DT",currDt);
            dataMap.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
            List dlist = ClientUtil.executeQuery("getDuplicateData", dataMap);
            if (dlist.size() > 0 && dlist.get(0) != null) {
                HashMap delMap=new HashMap();
                delMap.put("TRANS_DT",currDt);
                delMap.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
               ClientUtil.execute("deletedDat", delMap); 
            }
            }
    public void insertIntoDenaomination() {
        try {
            for (int i = 0; i < 19; i++) {
                if (tblData.getModel().getValueAt(i, 2) != null && (CommonUtil.convertObjToInt(tblData.getModel().getValueAt(i, 2))>0)
                        && tblData.getModel().getValueAt(i, 3) != null&&tblData.getModel().getValueAt(i, 0)!=null) {
                  //  CommonUtil.convertObjToDouble(tblData.getModel().getValueAt(i, 2)).doubleValue()
                    //  if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
                    //totalPay += ((Double) tblData.getModel().getValueAt(i,2)).doubleValue();
                    //System.out.println("33-=="+tblData.getModel().getValueAt(i,1));
                    //System.out.println("22==="+tblData.getModel().getValueAt(i,2));
                    
                  // String amount = tblData.getModel().getValueAt(i, 3).toString().replaceAll(",", "");
                   Double amount=CommonUtil.convertObjToDouble(tblData.getModel().getValueAt(i, 3)).doubleValue();
                    String dnomType = tblData.getModel().getValueAt(i, 0).toString();
                    insertData("CHANGE", amount, tblData.getModel().getValueAt(i, 2).toString(), "CREDIT",
                            tblData.getModel().getValueAt(i, 1).toString(), dnomType);
                    //   }
                }
            }
            //for (int i = 0; i <= 18; i++) {
              //  if (tblData.getModel().getValueAt(i, 5) != null
                   //     && tblData.getModel().getValueAt(i, 6) != null) {
                   // //   if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
                   // String amount = tblData.getModel().getValueAt(i, 6).toString().replaceAll(",", "");
                   // String dnomType = tblData.getModel().getValueAt(i, 0).toString();
                   // insertData("CHANGE", amount, tblData.getModel().getValueAt(i, 5).toString(), "DEBIT",
                    //        tblData.getModel().getValueAt(i, 4).toString(), dnomType);
                    //  }
               // }

           // }
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

    public void insertData(String transId, Double amount, String noOfNotes, String trans_type, String deno_type, String dnomType) {
        String currency = "INR";
        HashMap singleAuthorizeMap = new HashMap();
        singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        singleAuthorizeMap.put("TRANS_DT", currDt);
        singleAuthorizeMap.put(CommonConstants.TRANS_ID, transId);//trans id
        singleAuthorizeMap.put("DENOMINATION_VALUE", amount); //amount
        //singleAuthorizeMap.put("DENOMINATION_COUNT", noOfNotes);//no of notes
        singleAuthorizeMap.put("CURRENCY", currency);//Currency
        singleAuthorizeMap.put("TRANS_TYPE", trans_type);//Trans type
        singleAuthorizeMap.put("STATUS", "CREATED");//status
        singleAuthorizeMap.put("DENOMINATION_TYPE", deno_type);//Denaomination type
        singleAuthorizeMap.put("BRANCH_CODE",CommonUtil.convertObjToStr(cbobranch.getSelectedItem()));
        singleAuthorizeMap.put("DENOM_TYPE", dnomType);
        ClientUtil.execute("insertDenominationConfigDetails", singleAuthorizeMap);
        singleAuthorizeMap.clear();
        singleAuthorizeMap = null;
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            //   System.out.println("ddd"+d);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void initComponentData() {
    }

    /** To display a popUp window for viewing existing data */
    public void fillData(Object map) {

        setModified(true);
        HashMap hash = (HashMap) map;

        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                    || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)
                    || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                //System.out.println("@@@@@@@@@@@+hash"+hash.get("BOARD_MT_ID"));
                hash.put(CommonConstants.MAP_WHERE, hash.get("OUTWARD_NO"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);

                if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)
                        || viewType.equals(ClientConstants.ACTION_STATUS[17])) {


                    setButtonEnableDisable();

                }

            }

        }
    }

    private void enableDisable(boolean yesno) {
        ClientUtil.enableDisable(this, yesno);
    }

    private void setButtonEnableDisable() {

        btnEdit.setEnabled(!btnEdit.isEnabled());


        mitEdit.setEnabled(btnEdit.isEnabled());



        btnView.setEnabled(!btnView.isEnabled());


    }

    public void update(Observable observed, Object arg) {
        // removeRadioButtons();
    }

    private void setFieldNames() {

        panOutwardRegister.setName("panOutwardRegister");


    }

    private void internationalize() {
        //lblProdType.setText(resourceBundle.getString("lblProdType"));
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CLabel DmBal;
    private com.see.truetransact.uicomponent.CLabel DmBal1;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cbobranch;
    private com.see.truetransact.uicomponent.CLabel lblBalance1;
    private com.see.truetransact.uicomponent.CLabel lblBalance2;
    private com.see.truetransact.uicomponent.CLabel lblCrBal;
    private com.see.truetransact.uicomponent.CLabel lblCrBal1;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblTotalCoin;
    private com.see.truetransact.uicomponent.CLabel lblTotalNote;
    private com.see.truetransact.uicomponent.CLabel lblTotalStamp;
    private com.see.truetransact.uicomponent.CLabel lblbranch;
    private com.see.truetransact.uicomponent.CLabel lblcurrentBal;
    private com.see.truetransact.uicomponent.CLabel lblcurrentBal1;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private javax.swing.JPanel panDenominationBalance;
    private javax.swing.JPanel panDenominationBalance1;
    private com.see.truetransact.uicomponent.CPanel panOutwardRegister;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CScrollPane scrTableScroll;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CDateField tdtDenoDt;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        DenominationConfigurationUI Outward = new DenominationConfigurationUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(Outward);
        j.show();
        Outward.show();
    }
}
