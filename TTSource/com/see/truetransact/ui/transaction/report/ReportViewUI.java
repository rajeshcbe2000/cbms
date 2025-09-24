/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ReportViewUI.java
 *
 * Created on September 28, 2011, 3:03 PM
 */
package com.see.truetransact.ui.transaction.report;

import com.see.truetransact.clientutil.ClientUtil;
import java.util.HashMap;
import com.see.truetransact.clientutil.ClientConstants;
import java.util.List;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.viewall.ViewAll;
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
import com.see.truetransact.uicomponent.CInternalFrame;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;

/**
 *
 * @author  user
 */
public class ReportViewUI extends CInternalFrame {

    DefaultTableModel model = null;
    //  DefaultTreeModel root;
    //   DefaultTreeModel child;
    Date currDt = null;
    private TableModelListener tableModelListener;
    public String transMode = "";
    public String trans_pid = "";
    public String clsbal = "";
    public String Opamount = "",
            amount1 = "", countVal = "", amount = "", sc_Name = "", cname = "", ac_No = "", ac_HD_No = null, prod_Type = "", prod_Id = null, trans_id = "", trans_type = "",
            posted_by = "", autho_by = "", particulars = "", panNo = "", token_No = "", instr_Type = "",
            instr_No = "", instr_date = "", clear_Bal = "";
    DefaultMutableTreeNode selectedNode = null;
    String lblAmt = "", lblTransTy;
    private ComboBoxModel cbmProdType;
    private ComboBoxModel cbmProdId;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private final static Logger log = Logger.getLogger(ReportViewUI.class);
    private final String SHARE_TYPE = "getShareType";  // Added by Rajesh   
    private ComboBoxModel cbmbranch;
    
    public ComboBoxModel getCbmbranch() {
        return cbmbranch;
    }
    
    public void setCbmbranch(ComboBoxModel cbmbranch) {
        this.cbmbranch = cbmbranch;
    }
    
    public ReportViewUI() {
        // this.frm=frm;
        //  trans_pid=TransId;
        lblAmt = amount;
        //lblTransTy=TransTy;
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setupScreen();
//        initTableData();
//      this.setBounds(0,0, 500,330);
//      setSize(570, 280);
        this.setVisible(true);
        try {
            fillData1();
            fillBranch();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ReportViewUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        // this.setTitle("Change");

//        getAcHdTree("PENDING");
        //    cboStatus.setSelectedItem("Pending");
//        tblData.getModel().addTableModelListener(tableModelListener);
        //   btnReject.setVisible(false);
        
    }

    private void setupScreen() {
        //   setModal(true);
        panMemberLiability.setVisible(false);
        setTitle("Account Number For GL [" + ProxyParameters.BRANCH_ID + "]");
        /*
         * Calculate the screen size
         */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("@$#@$#@# screenSize : " + screenSize);
        
        setSize(570, 280);
        /*
         * Center frame on the screen
         */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
    
    public void fillBranch() {
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        HashMap mapShare = new HashMap();
        List keyValue = null;
        keyValue = ClientUtil.executeQuery("getbranchesForBalanceSheet", mapShare);
        key.add("");
        value.add("");
        if (keyValue != null && keyValue.size() > 0) {
            for (int i = 0; i < keyValue.size(); i++) {
                mapShare = (HashMap) keyValue.get(i);
                key.add(mapShare.get("BRANCH_CODE"));
                value.add(mapShare.get("BRANCH_NAME"));
            }
        }
        cbmbranch = new ComboBoxModel(key, value);
        cboBranchCode.setModel(cbmbranch);
        key = null;
        value = null;
        keyValue.clear();
        keyValue = null;
        mapShare.clear();
        mapShare = null;
    }
    
    public void fillData(Object param) {
        System.out.println("In fillData test...............");
        final HashMap hash = (HashMap) param;
        System.out.println("param DATE ======" + param);
        System.out.println("HASH DATE ======" + hash);
        
        
        if (cboProdType.getSelectedItem() != null && !cboProdType.getSelectedItem().equals("")) {
            String prod_type1 = cboProdType.getSelectedItem().toString();
            if (prod_type1.equals("Share") || prod_type1.equals("MMBS") || prod_type1.equals("Investments") || prod_type1.equals("Borrowings")) {
                if (prod_type1.equals("Share")) {
                    txtAccNo.setText(hash.get("SHARE_ACCT_NO").toString());
                }
                if (prod_type1.equals("Borrowings")) {
                    txtAccNo.setText(hash.get("BORROWING_NO").toString());
                }
                if (prod_type1.equals("Investments")) {
                    txtAccNo.setText(hash.get("INVESTMENT_REF_NO").toString());
                }
                if (prod_type1.equals("MMBS")) {
                    txtAccNo.setText(hash.get("CHITTAL_NO").toString());
                }
            } else {
                if (prod_type1.equals("General Ledger")) {
                    String acHdId = hash.get("A/C HEAD DESCRIPTION").toString();
//            String bankType = CommonConstants.BANK_TYPE;
//            System.out.println("bankType" + bankType);
//            String customerAllow = "";
//            String hoAc = "";
//            cboProdId.setSelectedItem("");
//            this.txtAccNo.setText("");
                    txtAccNo.setText(acHdId);                    
                } else {
                    String acHdId = hash.get("ACCOUNTNO").toString();
//            String bankType = CommonConstants.BANK_TYPE;
//            System.out.println("bankType" + bankType);
//            String customerAllow = "";
//            String hoAc = "";
//            cboProdId.setSelectedItem("");
//            this.txtAccNo.setText("");
                    txtAccNo.setText(acHdId);
                }
            }
        }
        // CashTransactionUI cashUI = new CashTransactionUI();
        // frm.fillAccNo(acHdId);
    }

//    public void insertIntoDenaomination() {
//        try {
//            for (int i = 0; i<= 14; i++) {
//                if(tblData.getModel().getValueAt(i,2)!=null &&
//                tblData.getModel().getValueAt(i,3)!=null ) {
//                  //  if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
//                        //totalPay += ((Double) tblData.getModel().getValueAt(i,2)).doubleValue();
//                        //System.out.println("33-=="+tblData.getModel().getValueAt(i,1));
//                        //System.out.println("22==="+tblData.getModel().getValueAt(i,2));
//                        String amount = tblData.getModel().getValueAt(i,3).toString().replaceAll(",", "");
//                        String dnomType= tblData.getModel().getValueAt(i,0).toString();
//                        insertData("CHANGE",amount,tblData.getModel().getValueAt(i,2).toString(),"CREDIT",
//                        tblData.getModel().getValueAt(i,1).toString(),dnomType);
//                 //   }
//                }
//            }
//            for (int i = 0; i<= 14; i++) {
//                if(tblData.getModel().getValueAt(i,5)!=null &&
//                tblData.getModel().getValueAt(i,6)!=null) {
//                 //   if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
//                        String amount = tblData.getModel().getValueAt(i,6).toString().replaceAll(",", "");
//                        String dnomType= tblData.getModel().getValueAt(i,0).toString();
//                        insertData("CHANGE",amount,tblData.getModel().getValueAt(i,5).toString(),"DEBIT",
//                        tblData.getModel().getValueAt(i,4).toString(),dnomType);
//                  //  }
//                }
//                
//            }
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
    private void popUp() {
        String prod_id1 = "", prod_type1 = "", prod_id = "", prod_type = "";        
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        String branchCode = "";
        if (!getCbmbranch().getKeyForSelected().equals("") && getCbmbranch().getKeyForSelected()!=null) {
                branchCode = CommonUtil.convertObjToStr(getCbmbranch().getKeyForSelected());
            }else{
                branchCode = CommonUtil.convertObjToStr(ProxyParameters.BRANCH_ID); 
        }
        if (cboProdType.getSelectedItem() != null && !cboProdType.getSelectedItem().equals("")) {
            prod_type1 = cboProdType.getSelectedItem().toString();
            if (prod_type1.equals("Share") || prod_type1.equals("MMBS") || prod_type1.equals("Investments") || prod_type1.equals("Borrowings")) {
                if (prod_type1.equals("Share")) {
                    if (((ComboBoxModel) cboProdId.getModel()).getKeyForSelected() != null) {
                        prod_id = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
                    }
                    viewMap.put(CommonConstants.MAP_NAME, "getShareNum");
                    
                    whereMap.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                    //whereMap.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
                    whereMap.put("SELECTED_BRANCH", branchCode);
                    setSelectedBranchID(TrueTransactMain.selBranch);
                    
                    viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                    new ViewAll(this, viewMap).show();
                    
                }
                if (prod_type1.equals("Investments")) {
                    if (((ComboBoxModel) cboProdId.getModel()).getKeyForSelected() != null) //  prod_id=((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
                    {
                        prod_id = cboProdId.getSelectedItem().toString();
                    }
                    System.out.println("prod_id INMMMMM=======" + prod_id);
                    whereMap.put("INV_DEC", prod_id);
                    viewMap.put(CommonConstants.MAP_NAME, "getSelectInvReportView");
                    viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                    //viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                    new ViewAll(this, viewMap).show();
                    
                    
                }
                if (prod_type1.equals("MMBS")) {
                    HashMap map1 = new HashMap();
                    HashMap where = new HashMap();
                    HashMap mapTrans = new HashMap();
                    if (((ComboBoxModel) cboProdId.getModel()).getKeyForSelected() != null) {
                        prod_id = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
                    }
                    String sch_name = "";
                    if (cboProdId.getSelectedItem() != null) //   sch_name= cboProdId.getSelectedItem().toString();
                    {
                        sch_name = cbmProdId.getKeyForSelected().toString();
                    }
                    map1.put("SCHEME_NAME", sch_name);
                    List list1 = ClientUtil.executeQuery("getSelectTransDet", map1);
                    if (list1 != null && list1.size() > 0) {
                        mapTrans = (HashMap) list1.get(0);
                        if (CommonUtil.convertObjToStr(mapTrans.get("TRANS_FIRST_INSTALLMENT")).equals("Y")) {
                            whereMap.put("SCHEME_NAMES", sch_name);
                            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                            viewMap.put(CommonConstants.MAP_NAME, "getSelectChittalReceiptEntryDetailsForReportview");
                        } else {
                            whereMap.put("SCHEME_NAMES", sch_name);
                            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                            viewMap.put(CommonConstants.MAP_NAME, "getSelectChittalReceiptEntryDetailsN");                            
                        }
                    }
                    new ViewAll(this, viewMap).show();
                }
                if (prod_type1.equals("Borrowings")) {
                    ArrayList lst = new ArrayList();
                    lst.add("BORROWING_NO");
                    viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                    lst = null;
                    viewMap.put(CommonConstants.MAP_NAME, "BorrwingDisbursal.getSelectBorrowingDList");
                    viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                    new ViewAll(this, viewMap).show();
                }
                
            } else {
                
                
                if (((ComboBoxModel) cboProdId.getModel()).getKeyForSelected() != null) {
                    prod_id = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
                }
                
                if (((ComboBoxModel) cboProdType.getModel()).getKeyForSelected() != null) {
                    prod_type = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
                }
                
                viewMap.put(CommonConstants.MAP_NAME, "Report.getAccountList"
                        + ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
                
                whereMap.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                //whereMap.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
                whereMap.put("SELECTED_BRANCH", branchCode);
                setSelectedBranchID(TrueTransactMain.selBranch);
                
                
                viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                new ViewAll(this, viewMap).show();
            }
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

//    private void setTableModelListener() {
//        tableModelListener = new TableModelListener() {
//            
//            public void tableChanged(TableModelEvent e) {
//                if (e.getType() == TableModelEvent.UPDATE) {
//                    System.out.println("Cell " + e.getFirstRow() + ", "
//                    + e.getColumn() + " changed. The new value: "
//                    + tblData.getModel().getValueAt(e.getFirstRow(),
//                    e.getColumn()));
//                    int row = e.getFirstRow();
//                    int column = e.getColumn();
//                    
//                    if (row!=14) {
//                        if (column == 1 || column == 2) {
//
//                            TableModel model = tblData.getModel();
//
//                            String quantity = model.getValueAt(row, 1).toString();
//
//                            if(model.getValueAt(row, 1)!=null) {
//                                if(row==14) {
//                                    quantity="1";
//                                }
//
//                                double price = CommonUtil.convertObjToDouble(model.getValueAt(row, 2)) .doubleValue();
//                                Double value=null;
//                                if(quantity.equals("0.50")){
//                                     value=new Double(0.50 * price);
//                                }
//                                else{
//                                 value =new Double(Integer.parseInt(quantity) * price);
//                                }
//                                // System.out.println("price====="+price +"quantity=="+quantity);
//                                //    System.out.println("valuevalue=="+value);
//                                model.setValueAt(formatCrore(String.valueOf(value)), row, 3);
//                                //Total print
//                                double totalPay=0;
//                                BigDecimal bm=null;
//
//                                for (int i = 0; i< 14; i++) {
//                                    //  total +=((Double) model.getValueAt(i, 2)).doubleValue();
//                                    if(tblData.getModel().getValueAt(i,3)!=null) {
//                                        // totalPay += ((Double) tblData.getModel().getValueAt(i,2)).doubleValue();
//                                        String val=tblData.getModel().getValueAt(i,3).toString();
//                                        val=val.replaceAll(",","");
//                                        // totalPay += Double.parseDouble(tblData.getModel().getValueAt(i,2).toString());
//                                        totalPay += Double.parseDouble(val);
//                                    }
//                                }
//                                model.setValueAt(formatCrore(String.valueOf(Double.valueOf(totalPay))), 14, 3);//formatCrore(
//
//                            }
//                            //setModified(true);
//                        }
//                        if ( column == 4 || column == 5 ) {
//                            TableModel model = tblData.getModel();
//                            String quantity = model.getValueAt(row, 4).toString();
//                            if(model.getValueAt(row, 4)!=null) {
//                                if(row==15) {
//                                    // System.out.println("ROWW==111=="+row);
//                                    quantity="1";
//                                }
//                                double price = CommonUtil.convertObjToDouble(model.getValueAt(row, 5)).doubleValue();
//                                Double value = new Double(Integer.parseInt(quantity) * price);
//                                model.setValueAt(formatCrore(String.valueOf(value)), row, 6);
//
//                                //Set tiotal
//                                double totalRec=0;
//
//                                for (int i = 0; i< 14; i++) {
//                                    //  total +=((Double) model.getValueAt(i, 2)).doubleValue();
//                                    if(tblData.getModel().getValueAt(i,6)!=null) {
//                                        String val=  tblData.getModel().getValueAt(i,6).toString();
//                                        val=val.replaceAll(",","");
//                                        // totalRec +=   Double.parseDouble(tblData.getModel().getValueAt(i,5).toString());
//                                        totalRec +=   Double.parseDouble(val);
//                                    }
//
//                                }
//                                model.setValueAt(formatCrore(String.valueOf(Double.valueOf(totalRec))), 14, 6);  //formatCrore(
//                              //  setModified(true);
//                            }
//                        }
//                    }
//                    
//                    
//                }
//            }
//        };
//        tblData.getModel().addTableModelListener(tableModelListener);
//    }
    public void fillData1() throws Exception {
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get("PRODUCTTYPE"));
        key.add("Share");
        key.add("MMBS");
        key.add("Investments");
        key.add("Borrowings");
        value.add("Share");
        value.add("MMBS");
        value.add("Investments");
        value.add("Borrowings");
        cbmProdType = new ComboBoxModel(key, value);
        cboProdType.setModel(getCbmProdType());
        System.out.println("getCbmProdType().getKeyForSelected().getKeyForSelected().toString()111>>" + (getCbmProdType().getKeyForSelected().toString()));
        setCbmProdId(getCbmProdType().getKeyForSelected().toString());
        cboProdId.setModel(getCbmProdId());
        //cboProdType1.addItem("");
        // cboProdType1.addItem("Share");
        // cboProdType1.addItem("MMBS");
        // cboProdType1.addItem("Investments");
        // cboProdType1.addItem("Borrowings");

        //root = new TreeModel();
        //   child = new DefaultTreeModel("Colors");
        //   root.add(child);
        btnLoansInstSchedul.setEnabled(false);
        btnFDSubdayBook.setEnabled(false);
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        log.info("In getKeyValue()");
        
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }
    
    public void setCbmProdType(ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }

    public void setCbmProdId(String prodType) {
        System.out.println("prodType in settt =====" + prodType);
        try {
            if (CommonUtil.convertObjToStr(prodType).length() > 1) {
                if (prodType.equals("GL")) {
                    System.out.println(",mnvhu");
                    key = new ArrayList();
                    value = new ArrayList();
                } else if (prodType.equals("Share")) {
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME, SHARE_TYPE);
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
                    cbmProdId = new ComboBoxModel(key, value);
                    cboProdId.setModel(getCbmProdId());
                    System.out.println("prod_type  =====yyyyyyyyyyyyyyy=============" + cboProdId);
                } else if (prodType.equals("MMBS")) {
                    HashMap mapData = new HashMap();
                    mapData.put("CURR_DATE", currDt);
                    /*   lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME,"getSchemeNames");
                    lookUpHash.put(CommonConstants.PARAMFORQUERY,mapData);
                    // lookUpHash.put("CURR_DATE",currDt);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                    cbmProdId= new ComboBoxModel(key,value);
                    cboProdId.setModel(getCbmProdId());*/
                    key = new ArrayList();
                    value = new ArrayList();
                    HashMap singleAuthorizeMap1 = new HashMap();
                    List aList = ClientUtil.executeQuery("getSelectEachSchemeDetailsForReportview", mapData);
                    System.out.println("prod_type  =====mapData=============" + mapData);
                    for (int i = 0; i < aList.size(); i++) {
                        HashMap map = (HashMap) aList.get(i);
                        System.out.println("prod_type  =====map.get(==========" + map.get("SCHEME_NAME"));
                        System.out.println("prod_type  =====map.get(==========" + map.get("SCHEME_DESC"));
                        if (map.get("SCHEME_NAME") != null && map.get("SCHEME_DESC") != null) {
                            if (map.get("SCHEME_NAME") != null && !map.get("SCHEME_NAME").equals("")) {
                                key.add(map.get("SCHEME_NAME").toString());
                            }
                            if (map.get("SCHEME_DESC") != null && !map.get("SCHEME_DESC").equals("")) {
                                value.add(map.get("SCHEME_DESC").toString());
                            }
                            
                        }
                    }
                    System.out.println("prod_type  =====key=============" + key);
                    System.out.println("prod_type  =====value=============" + value);
                    cbmProdId = new ComboBoxModel(key, value);
                    cboProdId.setModel(getCbmProdId());
                } else if (prodType.equals("Investments")) {
                    HashMap mapData = new HashMap();
                    //  mapData.put("CURR_DATE",currDt);
                    key = new ArrayList();
                    value = new ArrayList();
                    HashMap singleAuthorizeMap1 = new HashMap();
                    List aList = ClientUtil.executeQuery("getInvNum", mapData);
                    System.out.println("prod_type  =====mapData=============" + mapData);
                    for (int i = 0; i < aList.size(); i++) {
                        HashMap map = (HashMap) aList.get(i);
                        //   System.out.println("prod_type  =====map.get(=========="+map.get("SCHEME_NAME"));
                        //    System.out.println("prod_type  =====map.get(=========="+map.get("SCHEME_DESC"));
                        if (map.get("IINVESTMENT_PROD_DESC") != null && map.get("INVESTMENT_PROD_ID") != null) {
                            if (map.get("INVESTMENT_PROD_ID") != null && !map.get("INVESTMENT_PROD_ID").equals("")) {
                                key.add(map.get("INVESTMENT_PROD_ID").toString());
                            }
                            if (map.get("IINVESTMENT_PROD_DESC") != null && !map.get("IINVESTMENT_PROD_DESC").equals("")) {
                                value.add(map.get("IINVESTMENT_PROD_DESC").toString());
                            }
                            
                        }
                    }
                    System.out.println("prod_type  =====key=============" + key);
                    System.out.println("prod_type  =====value=============" + value);
                    cbmProdId = new ComboBoxModel(key, value);
                    cboProdId.setModel(getCbmProdId());
                } else if (prodType.equals("Borrowings")) {
                    ((ComboBoxModel) cboProdId.getModel()).removeAllElements();
                    key = null;
                    value = null;
                    //  cbmProdId= new ComboBoxModel(key,value);
                    cboProdId.setModel(getCbmProdId());
                } else {
                    try {
                        System.out.println("kkgjdf");
                        lookUpHash = new HashMap();
                        lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                        keyValue = ClientUtil.populateLookupData(lookUpHash);
                        getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                key = new ArrayList();
                value = new ArrayList();
                key.add("");
                value.add("");
            }
            if (!prodType.equals("Borrowings")) {
                cbmProdId = new ComboBoxModel(key, value);
                this.cbmProdId = cbmProdId;
                System.out.println("cbmProdId>>>>" + cbmProdId);                
            }
//        setChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panCashTrans = new com.see.truetransact.uicomponent.CPanel();
        btnOk = new com.see.truetransact.uicomponent.CButton();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        txtAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        tdtFromDt = new com.see.truetransact.uicomponent.CDateField();
        tdtToDt = new com.see.truetransact.uicomponent.CDateField();
        lblProdType1 = new com.see.truetransact.uicomponent.CLabel();
        lblProdType2 = new com.see.truetransact.uicomponent.CLabel();
        btnLoansInstSchedul = new com.see.truetransact.uicomponent.CButton();
        btnFDSubdayBook = new com.see.truetransact.uicomponent.CButton();
        panMemberLiability = new com.see.truetransact.uicomponent.CPanel();
        chkLiability = new com.see.truetransact.uicomponent.CCheckBox();
        lblMunnal = new com.see.truetransact.uicomponent.CLabel();
        lblBranch = new com.see.truetransact.uicomponent.CLabel();
        cboBranchCode = new com.see.truetransact.uicomponent.CComboBox();

        setClosable(true);
        setResizable(true);
        setTitle("Report View");
        setMaximumSize(new java.awt.Dimension(500, 400));
        setMinimumSize(new java.awt.Dimension(500, 400));
        setPreferredSize(new java.awt.Dimension(500, 400));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panCashTrans.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panCashTrans.setMinimumSize(new java.awt.Dimension(500, 500));
        panCashTrans.setPreferredSize(new java.awt.Dimension(500, 500));
        panCashTrans.setLayout(new java.awt.GridBagLayout());

        btnOk.setText("Show");
        btnOk.setMaximumSize(new java.awt.Dimension(80, 28));
        btnOk.setMinimumSize(new java.awt.Dimension(80, 28));
        btnOk.setPreferredSize(new java.awt.Dimension(80, 28));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCashTrans.add(btnOk, gridBagConstraints);

        lblProdId.setText("Prod ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCashTrans.add(lblProdId, gridBagConstraints);

        cboProdId.setPreferredSize(new java.awt.Dimension(80, 21));
        cboProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCashTrans.add(cboProdId, gridBagConstraints);

        lblProdType.setText("From Dt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCashTrans.add(lblProdType, gridBagConstraints);

        cboProdType.setPreferredSize(new java.awt.Dimension(79, 20));
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 3);
        panCashTrans.add(cboProdType, gridBagConstraints);

        lblAccNo.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panCashTrans.add(lblAccNo, gridBagConstraints);

        txtAccNo.setAllowAll(true);
        txtAccNo.setPreferredSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 70;
        panCashTrans.add(txtAccNo, gridBagConstraints);

        btnAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccNo.setToolTipText("Account Head");
        btnAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panCashTrans.add(btnAccNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCashTrans.add(tdtFromDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCashTrans.add(tdtToDt, gridBagConstraints);

        lblProdType1.setText("Prod Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCashTrans.add(lblProdType1, gridBagConstraints);

        lblProdType2.setText("To Dt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCashTrans.add(lblProdType2, gridBagConstraints);

        btnLoansInstSchedul.setText("Loans Installment Schedule");
        btnLoansInstSchedul.setMaximumSize(new java.awt.Dimension(200, 28));
        btnLoansInstSchedul.setMinimumSize(new java.awt.Dimension(200, 28));
        btnLoansInstSchedul.setPreferredSize(new java.awt.Dimension(200, 28));
        btnLoansInstSchedul.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoansInstSchedulActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCashTrans.add(btnLoansInstSchedul, gridBagConstraints);

        btnFDSubdayBook.setText("FD Subday Book");
        btnFDSubdayBook.setMaximumSize(new java.awt.Dimension(200, 28));
        btnFDSubdayBook.setMinimumSize(new java.awt.Dimension(200, 28));
        btnFDSubdayBook.setPreferredSize(new java.awt.Dimension(200, 28));
        btnFDSubdayBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFDSubdayBookActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCashTrans.add(btnFDSubdayBook, gridBagConstraints);

        panMemberLiability.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panMemberLiability.setMinimumSize(new java.awt.Dimension(100, 24));
        panMemberLiability.setPreferredSize(new java.awt.Dimension(100, 24));
        panMemberLiability.setLayout(new java.awt.GridBagLayout());

        chkLiability.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLiabilityActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMemberLiability.add(chkLiability, gridBagConstraints);

        lblMunnal.setText("Liability");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 2);
        panMemberLiability.add(lblMunnal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCashTrans.add(panMemberLiability, gridBagConstraints);

        lblBranch.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panCashTrans.add(lblBranch, gridBagConstraints);

        cboBranchCode.setPreferredSize(new java.awt.Dimension(145, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panCashTrans.add(cboBranchCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -100;
        gridBagConstraints.ipady = -180;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 14, 9);
        getContentPane().add(panCashTrans, gridBagConstraints);

        getAccessibleContext().setAccessibleName("Re");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
        
        if (cboProdType.getSelectedItem() != null) {
            if (!cboProdType.getSelectedItem().equals("General Ledger") && !cboProdType.getSelectedItem().equals("Borrowings") && cboProdId.getSelectedItem() == null) {
                
                displayAlert("Select Product Id!!!");
                return;
            }
        }
        String acNo = txtAccNo.getText();        
        if (acNo == null || acNo.equals("")) {
            displayAlert("Enter account number!!!");
            return;
        }
        String branchCode = "";
        if (!getCbmbranch().getKeyForSelected().equals("") && getCbmbranch().getKeyForSelected()!=null) {
                branchCode = CommonUtil.convertObjToStr(getCbmbranch().getKeyForSelected());
            }else{
                branchCode = CommonUtil.convertObjToStr(ProxyParameters.BRANCH_ID); 
        } 
        
        if (cboProdType.getSelectedItem() != null && !cboProdType.getSelectedItem().equals("")) {
            String prod_type1 = cboProdType.getSelectedItem().toString();
            
            
            
            if (prod_type1.equals("Share") || prod_type1.equals("MMBS") || prod_type1.equals("Investments") || prod_type1.equals("Borrowings")) {
                if (prod_type1.equals("Share") && chkLiability.isSelected() == false) {
                    String shNo = txtAccNo.getText();
                    if (shNo != null && !shNo.equals("")) {
                        TTIntegration ttIntgration = null;
                        HashMap paramMap = new HashMap();
                        paramMap.put("MemberNo", shNo);
                        paramMap.put("FromDate", DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
                        paramMap.put("ToDate", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                        ttIntgration.setParam(paramMap);
                        ttIntgration.integrationForPrint("ShareLedgerBw", true);
                    }
                }
                if (prod_type1.equals("Share") && chkLiability.isSelected() == true) {
                    String shNo = txtAccNo.getText();
                    if (shNo != null && !shNo.equals("")) {
                        TTIntegration ttIntgration = null;
                        HashMap paramMap = new HashMap();
                        paramMap.put("MemberNo", shNo);
                        paramMap.put("FromDate", DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
                        paramMap.put("ToDate", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                        ttIntgration.setParam(paramMap);
                        ttIntgration.integrationForPrint("MemberLiabilityRegisterDetF2", true);
                    }
                }
                if (prod_type1.equals("Investments")) {
                    System.out.println("ininvvv>>??");
                    TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    paramMap.put("InvestNo", txtAccNo.getText());
                    paramMap.put("From_Dt", DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
                    paramMap.put("To_Dt", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                    System.out.println("paramMap inv...>>>>" + paramMap);
                    ttIntgration.setParam(paramMap);
                    ttIntgration.integrationForPrint("InvestmentLedger", true);
                }
                if (prod_type1.equals("Borrowings")) {
                    if (tdtFromDt.getDateValue() == null) {
                        displayAlert("Enter From Date!!!");
                        return;
                    }
                    if (tdtToDt.getDateValue() == null) {
                        displayAlert("Enter To Date!!!");
                        return;
                    }
                    // Date dtFrom=new Date
                    TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    paramMap.put("BorrowingNo", txtAccNo.getText());
                    paramMap.put("FromDate", DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
                    paramMap.put("ToDate", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                    //paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                    paramMap.put("BranchId", branchCode);
                    ttIntgration.setParam(paramMap);
                    ttIntgration.integrationForPrint("BorrowingLedgerBw", true);
                }
                if (prod_type1.equals("MMBS")) {
                    // Date dtFrom=new Date
                    TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    paramMap.put("ChittalNo", txtAccNo.getText());
                    paramMap.put("From_Dt", DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
                    paramMap.put("To_Dt", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                    // paramMap.put("BranchId", ProxyParameters.BRANCH_ID); 
                    ttIntgration.setParam(paramMap);
                    ttIntgration.integrationForPrint("MDSLedger", true);
                }
                
            }
            if (prod_type1.equals("General Ledger")) {
                TTIntegration ttIntgration = null;
                HashMap paramMap = new HashMap();
                paramMap.put("AcHdDesc", txtAccNo.getText());
                paramMap.put("FromDate", DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
                paramMap.put("ToDate", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                //paramMap.put("BranchId", ProxyParameters.BRANCH_ID);   
                paramMap.put("BranchId", branchCode);
                ttIntgration.setParam(paramMap);
                ttIntgration.integrationForPrint("AcHdSubDay", true);                
            }
        }
        if (getCbmProdType().getKeyForSelected() != null && getCbmProdId().getKeyForSelected() != null) {
            String prod_type = getCbmProdType().getKeyForSelected().toString();
            System.out.println("prod_type   =========  " + prod_type);
            String prod_id = getCbmProdId().getKeyForSelected().toString();
            System.out.println("prod_id   =========  " + prod_id);
            if (prod_type.equals("OA") && txtAccNo.getText() != null) {
                if (tdtFromDt.getDateValue() == null) {
                    displayAlert("Enter From Date!!!");
                    return;
                }
                if (tdtToDt.getDateValue() == null) {
                    displayAlert("Enter To Date!!!");
                    return;
                }
                TTIntegration ttIntgration = null;
                HashMap paramMap = new HashMap();
                paramMap.put("AccountNo", txtAccNo.getText());
                paramMap.put("FromDate", DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
                paramMap.put("ToDate", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                //  paramMap.put("BranchId", ProxyParameters.BRANCH_ID); 
                ttIntgration.setParam(paramMap);
                ttIntgration.integrationForPrint("OALedger", true);
            }
            if (prod_type.equals("TD") && txtAccNo.getText() != null) {
                TTIntegration ttIntgration = null;
                HashMap paramMap = new HashMap();
                paramMap.put("AccountNo", txtAccNo.getText());
                paramMap.put("FromDate", DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
                paramMap.put("ToDate", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                //  paramMap.put("FromDate",tdtFromDt.getDateValue());
                //  paramMap.put("ToDate", tdtToDt.getDateValue());
                //  paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("PROD_ID", prod_id);
                List aList = ClientUtil.executeQuery("getBehavesL", singleAuthorizeMap);
                HashMap map = (HashMap) aList.get(0);                
                ttIntgration.setParam(paramMap);
                String behaves = null;
                if (map.get("BEHAVES_LIKE") != null) {
                    behaves = map.get("BEHAVES_LIKE").toString();
                }
                if (behaves != null && behaves.equals("FIXED")) {
                    ttIntgration.integrationForPrint("TermDepositLedgerBw", true);
                }
                if (behaves != null && behaves.equals("CUMMULATIVE")) {
                    ttIntgration.integrationForPrint("CumulativeDepositLedgerBw", true);
                }
                if (behaves != null && behaves.equals("RECURRING")) {
                    ttIntgration.integrationForPrint("RDLedgerBw", true);
                }
                
                
            }
            if (prod_type.equals("TL") && txtAccNo.getText() != null) {
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("PROD_ID", prod_id);
                List aList = ClientUtil.executeQuery("getBehavesLoan", singleAuthorizeMap);
                HashMap map = (HashMap) aList.get(0);                
                String behaves = null, auth_remarks = null;
                if (map.get("BEHAVES_LIKE") != null) {
                    behaves = map.get("BEHAVES_LIKE").toString();
                }
                if (map.get("AUTHORIZE_REMARK") != null) {
                    auth_remarks = map.get("AUTHORIZE_REMARK").toString();
                }
                System.out.println("behaves Loans ======" + behaves);
                System.out.println("prod_id Loans ======" + prod_id);
                TTIntegration ttIntgration = null;
                HashMap paramMap = new HashMap();
                paramMap.put("AccountNo", txtAccNo.getText());
                paramMap.put("FromDate", DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
                paramMap.put("ToDate", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                ttIntgration.setParam(paramMap);
                if (behaves != null && behaves.equals("SI_BEARING") && auth_remarks != null && auth_remarks.equals("OTHER_LOAN")) {
                    ttIntgration.integrationForPrint("TLLedgerBw", true);
                }
                if (behaves != null && behaves.equals("OD")) {
                    ttIntgration.integrationForPrint("AdLedgerBw", true);
                }
                if (behaves != null && behaves.equals("LOANS_AGAINST_DEPOSITS")) {
                    ttIntgration.integrationForPrint("DLLedgerBw", true);
                }
                if (behaves != null && behaves.equals("SI_BEARING") && auth_remarks != null && auth_remarks.equals("GOLD_LOAN")) {
                    ttIntgration.integrationForPrint("GL_LedgerBw", true);
                }
                if (behaves != null && behaves.equals("SI_BEARING") && auth_remarks != null && auth_remarks.equals("DAILY_LOAN")) {
                    ttIntgration.integrationForPrint("TLLedgerBw", true);
                }
                
            }
            if (prod_type.equals("AD") && txtAccNo.getText() != null) {
                TTIntegration ttIntgration = null;
                HashMap paramMap = new HashMap();
                paramMap.put("AccountNo", txtAccNo.getText());
                paramMap.put("FromDate", DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
                paramMap.put("ToDate", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                ttIntgration.setParam(paramMap);
                ttIntgration.integrationForPrint("AdLedgerBw", true);
            }
            if (prod_type.equals("SA") && txtAccNo.getText() != null) {
                TTIntegration ttIntgration = null;
                HashMap paramMap = new HashMap();
                paramMap.put("AccountNo", txtAccNo.getText());
                paramMap.put("FromDate", DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
                paramMap.put("ToDate", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                //  paramMap.put("FromDate",tdtFromDt.getDateValue());
                //  paramMap.put("ToDate", tdtToDt.getDateValue());
                ttIntgration.setParam(paramMap);
                ttIntgration.integrationForPrint("Suspense_LedgerBw", true);
            }
            if (prod_type.equals("AB") && txtAccNo.getText() != null) {
                TTIntegration ttIntgration = null;
                HashMap paramMap = new HashMap();
                paramMap.put("InvestNo", txtAccNo.getText());
                paramMap.put("FromDate", DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
                paramMap.put("ToDate", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                //  paramMap.put("FromDate",tdtFromDt.getDateValue());
                //  paramMap.put("ToDate", tdtToDt.getDateValue());
                ttIntgration.setParam(paramMap);
                ttIntgration.integrationForPrint("OBAc_LedgerBw", true);
            }
            // }
        }


        // this.dispose();
    }//GEN-LAST:event_btnOkActionPerformed

//    private boolean isDenominationTallied() {
//        boolean tallied = true;
//        try {
//            double transAmt = CommonUtil.convertObjToDouble(lblAmt.replaceAll(",", "")).doubleValue();
//            double receiptDenomination = 0;
//            double paymentDenomination = 0;
//            for (int i = 0; i<=14; i++) {
//                if(tblData.getModel().getValueAt(i,2)!=null &&
//                tblData.getModel().getValueAt(i,3)!=null ) {
//                  //  if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
//                        //totalPay += ((Double) tblData.getModel().getValueAt(i,2)).doubleValue();
//                        //System.out.println("33-=="+tblData.getModel().getValueAt(i,1));
//                        //System.out.println("22==="+tblData.getModel().getValueAt(i,2));
//                        String amount = tblData.getModel().getValueAt(i,3).toString().replaceAll(",", "");
//                        receiptDenomination+=CommonUtil.convertObjToDouble(amount).doubleValue();
//                  //  }
//                }
//            }
//            for (int i = 0; i<= 14; i++) {
//                if(tblData.getModel().getValueAt(i,5)!=null &&
//                tblData.getModel().getValueAt(i,6)!=null) {
//                  //  if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
//                        String amount = tblData.getModel().getValueAt(i,6).toString().replaceAll(",", "");
//                        paymentDenomination+=CommonUtil.convertObjToDouble(amount).doubleValue();
//                  //  }
//                }
//                
//            }  
//            double denominationAmount = 0.0;
//            if(receiptDenomination>0.0 && lblTransTy.equals("CREDIT")){
//                transMode="Receipts";
//            }
//            if(paymentDenomination>0.0 && lblTransTy.equals("DEBIT")){
//                transMode="Payments";
//            }
//            if(transMode.equals("Receipts")){
//                denominationAmount = receiptDenomination-paymentDenomination;
//            }else{
//                denominationAmount = paymentDenomination-receiptDenomination;
//            }
////             double denominationAmount = receiptDenomination-paymentDenomination;
//             if (denominationAmount!=transAmt) {
//                 ClientUtil.showAlertWindow("Denomination Amount : "+denominationAmount+
//                 "\nTransaction Amount :"+transAmt);
//                 tallied = false;
//             }
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
//        return tallied;
//    }
    public void clearTransDetails() {
        
    }
    
    private void enableDisableButtons(boolean val) {
        btnOk.setEnabled(val);
        // btnReject.setEnabled(val);
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
    
    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
        //  final HashMap viewMap = new HashMap();
        ///      System.out.println("getCbmProdType().getKeyForSelected().toString()???>>>>"+getCbmProdType().getKeyForSelected().toString());
        //       System.out.println("getCbmProdid().getKeyForSelected().toString()???>>>>"+getCbmProdId().getKeyForSelected().toString());
        //   viewMap.put("PROD_TYPE", getCbmProdType().getKeyForSelected().toString());
        //   viewMap.put("PROD_ID", getCbmProdId().getKeyForSelected().toString());
        //    viewMap.put(CommonConstants.MAP_WHERE, viewMap);
        //       viewMap.put(CommonConstants.MAP_NAME, "getAllProductAccNoForGlInCash");
        //     new ViewAll(this, viewMap).show();    
        txtAccNo.setText("");
        tdtFromDt.setDateValue("");
        tdtToDt.setDateValue("");
        popUp();
    }//GEN-LAST:event_btnAccNoActionPerformed
    
    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        System.out.println("getCbmProdType().getKeyForSelected().getKeyForSelected().toString()111>>" + (getCbmProdType().getKeyForSelected().toString()));
        setCbmProdId(getCbmProdType().getKeyForSelected().toString());
        txtAccNo.setText("");
        tdtFromDt.setDateValue("");
        tdtToDt.setDateValue("");
        cboProdId.setModel(getCbmProdId());
        if (getCbmProdType().getKeyForSelected().toString().equals("Share")) {
            panMemberLiability.setVisible(true);            
        } else {
            panMemberLiability.setVisible(false);            
        }
        if (getCbmProdType().getKeyForSelected().toString().equals("AD") || getCbmProdType().getKeyForSelected().toString().equals("TL")) {
            btnLoansInstSchedul.setEnabled(true);            
        } else {
            btnLoansInstSchedul.setEnabled(false);
        }
        if (getCbmProdType().getKeyForSelected().toString().equals("TD")) {
            btnFDSubdayBook.setEnabled(true);            
        } else {
            btnFDSubdayBook.setEnabled(false);
        }
        
    }//GEN-LAST:event_cboProdTypeActionPerformed
    
private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_cboProdIdActionPerformed
    
    private void btnLoansInstSchedulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoansInstSchedulActionPerformed
        // TODO add your handling code here:
        if (getCbmProdType().getKeyForSelected().toString().equals("AD") || getCbmProdType().getKeyForSelected().toString().equals("TL")) {
            String accNo = txtAccNo.getText();
            if (accNo != null && !accNo.equals("")) {
                TTIntegration ttIntgration = null;
                HashMap paramMap = new HashMap();
                paramMap.put("AccountNo", accNo);
//                    paramMap.put("FromDate",DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
//                    paramMap.put("ToDate", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                ttIntgration.setParam(paramMap);
                ttIntgration.integrationForPrint("TLInstallmentSchedule", true);
            }
        }
    }//GEN-LAST:event_btnLoansInstSchedulActionPerformed
    
private void btnFDSubdayBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFDSubdayBookActionPerformed
    if (getCbmProdType().getKeyForSelected().toString().equals("TD")) {
        String accNo = txtAccNo.getText();
        if (accNo != null && !accNo.equals("")) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            paramMap.put("ProdDesc", cboProdId.getSelectedItem().toString());
            paramMap.put("FromDate", DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
            paramMap.put("ToDate", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
            ttIntgration.setParam(paramMap);
            ttIntgration.integrationForPrint("DepositSubDay", true);
        }
    }
}//GEN-LAST:event_btnFDSubdayBookActionPerformed
    
    private void chkLiabilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLiabilityActionPerformed
        
    }//GEN-LAST:event_chkLiabilityActionPerformed
    public void setCbmProdId1(String prod_type) {
        try {
            System.out.println("prod_type  ==================" + prod_type);
            HashMap singleAuthorizeMap = new HashMap();
            if (prod_type.equals("Share")) {
                // List aList= ClientUtil.executeQuery("getShareType", singleAuthorizeMap);
                //         for(int i=0;i<aList.size();i++) {
                //                 HashMap map=(HashMap)aList.get(i);
                //               if(map.get("DENOMINATION_ALLOWED")!=null) {
                //                     isDen=map.get("DENOMINATION_ALLOWED").toString();
                //                           
                //               }
                //              }
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME, SHARE_TYPE);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
                cbmProdId = new ComboBoxModel(key, value);
                cboProdId.setModel(getCbmProdId());
                System.out.println("prod_type  =====yyyyyyyyyyyyyyy=============" + cboProdId);
            } else if (prod_type.equals("MMBS")) {
                HashMap mapData = new HashMap();
                mapData.put("CURR_DATE", currDt);
                /*   lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME,"getSchemeNames");
                lookUpHash.put(CommonConstants.PARAMFORQUERY,mapData);
                // lookUpHash.put("CURR_DATE",currDt);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                cbmProdId= new ComboBoxModel(key,value);
                cboProdId.setModel(getCbmProdId());*/
                key = new ArrayList();
                value = new ArrayList();
                HashMap singleAuthorizeMap1 = new HashMap();
                List aList = ClientUtil.executeQuery("getSchemeNames", mapData);
                System.out.println("prod_type  =====mapData=============" + mapData);
                for (int i = 0; i < aList.size(); i++) {
                    HashMap map = (HashMap) aList.get(i);
                    System.out.println("prod_type  =====map.get(==========" + map.get("SCHEME_NAME"));
                    System.out.println("prod_type  =====map.get(==========" + map.get("SCHEME_DESC"));
                    if (map.get("SCHEME_NAME") != null && map.get("SCHEME_DESC") != null) {
                        if (map.get("SCHEME_NAME") != null && !map.get("SCHEME_NAME").equals("")) {
                            key.add(map.get("SCHEME_NAME").toString());
                        }
                        if (map.get("SCHEME_DESC") != null && !map.get("SCHEME_DESC").equals("")) {
                            value.add(map.get("SCHEME_DESC").toString());
                        }
                        
                    }
                }
                System.out.println("prod_type  =====key=============" + key);
                System.out.println("prod_type  =====value=============" + value);
                cbmProdId = new ComboBoxModel(key, value);
                cboProdId.setModel(getCbmProdId());
            } else if (prod_type.equals("Investments")) {
                HashMap mapData = new HashMap();
                //  mapData.put("CURR_DATE",currDt);
                key = new ArrayList();
                value = new ArrayList();
                HashMap singleAuthorizeMap1 = new HashMap();
                List aList = ClientUtil.executeQuery("getInvNum", mapData);
                System.out.println("prod_type  =====mapData=============" + mapData);
                for (int i = 0; i < aList.size(); i++) {
                    HashMap map = (HashMap) aList.get(i);
                    //   System.out.println("prod_type  =====map.get(=========="+map.get("SCHEME_NAME"));
                    //    System.out.println("prod_type  =====map.get(=========="+map.get("SCHEME_DESC"));
                    if (map.get("IINVESTMENT_PROD_DESC") != null && map.get("INVESTMENT_PROD_ID") != null) {
                        if (map.get("INVESTMENT_PROD_ID") != null && !map.get("INVESTMENT_PROD_ID").equals("")) {
                            key.add(map.get("INVESTMENT_PROD_ID").toString());
                        }
                        if (map.get("IINVESTMENT_PROD_DESC") != null && !map.get("IINVESTMENT_PROD_DESC").equals("")) {
                            value.add(map.get("IINVESTMENT_PROD_DESC").toString());
                        }
                        
                    }
                }
                System.out.println("prod_type  =====key=============" + key);
                System.out.println("prod_type  =====value=============" + value);
                cbmProdId = new ComboBoxModel(key, value);
                cboProdId.setModel(getCbmProdId());
            } else if (prod_type.equals("Borrowings")) {
                ((ComboBoxModel) cboProdId.getModel()).removeAllElements();
                key = null;
                value = null;
                //  cbmProdId= new ComboBoxModel(key,value);
                cboProdId.setModel(getCbmProdId());
            } else {
                ((ComboBoxModel) cboProdId.getModel()).removeAllElements();
                key = null;
                value = null;
                //  cbmProdId= new ComboBoxModel(key,value);
                cboProdId.setModel(getCbmProdId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
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
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnFDSubdayBook;
    private com.see.truetransact.uicomponent.CButton btnLoansInstSchedul;
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CComboBox cboBranchCode;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CCheckBox chkLiability;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblBranch;
    private com.see.truetransact.uicomponent.CLabel lblMunnal;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblProdType1;
    private com.see.truetransact.uicomponent.CLabel lblProdType2;
    private com.see.truetransact.uicomponent.CPanel panCashTrans;
    private com.see.truetransact.uicomponent.CPanel panMemberLiability;
    private com.see.truetransact.uicomponent.CDateField tdtFromDt;
    private com.see.truetransact.uicomponent.CDateField tdtToDt;
    private com.see.truetransact.uicomponent.CTextField txtAccNo;
    // End of variables declaration//GEN-END:variables
}
