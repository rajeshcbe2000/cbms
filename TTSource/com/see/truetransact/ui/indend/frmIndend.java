/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * frmIndend.java
 *
 * Created on September 12, 2011, 12:08 PM
 */
package com.see.truetransact.ui.indend;

import javax.swing.table.DefaultTableModel;
import com.see.truetransact.ui.common.report.PrintClass;
import java.util.*;
import java.text.*;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import java.util.Observable;
import com.see.truetransact.uicomponent.COptionPane;
import java.math.BigDecimal;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.clientutil.ClientUtil;
import java.util.Observer;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.transferobject.indend.IndendTO;
import com.see.truetransact.ui.common.viewall.ViewAll;
import java.util.ResourceBundle;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import java.util.Date;
import javax.swing.table.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.SwingConstants;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;

/**
 *
 * @author userdd
 */
public class frmIndend extends CInternalFrame implements Observer, UIMandatoryField {

    private IndendOB observable;
    private String[][] tabledata;
    private String[] column;
    private IndendMRB objMandatoryRB = new IndendMRB();//Instance for the MandatoryResourceBundle
    private DefaultTableModel model;
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private final String AUTHORIZE = "Authorize";//Variable used when btnAuthorize is clicked
    TransactionUI transactionUI = new TransactionUI();
    double totalAmount = 0.0;
    private String gen_trans = null;
    private TableModelListener tableModelListener;
    public String depoId = "";
    boolean fromAuthorizeUI = false;
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI = null;
    AuthorizeListCreditUI CashierauthorizeListUI = null;
    private int rejectFlag = 0;
    private ArrayList tableList = new ArrayList();
    private HashMap finalMap = null;
    int slno = 0;
    private boolean selectedFlag = false;
    double totSum = 0.0;
    private Iterator processLstIterator;
    private String iridForEdit = "";
    PrintClass print = new PrintClass();
    private Date currDt = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    /**
     * Creates new form ifrNewBorrowing
     */
    public frmIndend() {
        currDt = ClientUtil.getCurrentDate();
        ProxyParameters.BRANCH_ID = ProxyParameters.BRANCH_ID;
        setSelectedBranchID(ProxyParameters.BRANCH_ID);
        initComponents();
        setFieldNames();
        observable = new IndendOB();
        observable.addObserver(this);
        observable.resetForm();
        setMaxLengths();
        initComponentData();
        initTableData();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panIndend, getMandatoryHashMap());
        panTrans.add(transactionUI);
        transactionUI.setSourceScreen("INDEND_REGISTER");
        observable.setTransactionOB(transactionUI.getTransactionOB());
        ClientUtil.enableDisable(panIndend, false);
        setButtonEnableDisable();
        txtStoreName.setEnabled(false);
        btnDepo.setEnabled(false);
        //fillData1();
        //Added By Suresh 21-08-2013
        txtTotAmt.setEnabled(false);
        cScrollPaneTable1.setVisible(false);
        btnaddNew.setEnabled(false);
        btnaddSave.setEnabled(false);
        btnAddDelete.setEnabled(false);
    }

    public void fillData1() {
        HashMap singleAuthorizeMap = new HashMap();
        singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
        singleAuthorizeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        List aList = ClientUtil.executeQuery("getIndendTableData", singleAuthorizeMap);
        //  System.out.println("aListIN PRINT++++++++:"+aList);
        model.setRowCount(0);

        for (int i = 0; i < aList.size(); i++) {

            HashMap map = (HashMap) aList.get(i);
            //System.out.println("Data print :"+map);
            String depId = map.get("DEPID").toString();
            //System.out.println("depId=="+depId);
            String TransType = map.get("TRANS_TYPE").toString();
            // System.out.println("TransType=="+TransType);
            //String DpDate=getDtPrintValue(map.get("DEPOSIT_DT").toString());
            String StoreName = "";
            if (map.get("STORE_NAME") != null) {
                StoreName = map.get("STORE_NAME").toString();
            }
            //System.out.println("StoreName=="+StoreName);
            String pur_amt = "";
            if (map.get("PURCHASE_AMT") != null) {
                pur_amt = map.get("PURCHASE_AMT").toString();
            }
            //System.out.println("pur_amt=="+pur_amt);
            String amt = "";
            if (map.get("AMOUNT") != null) {
                amt = map.get("AMOUNT").toString();
            }
            //System.out.println("amt=="+amt);

            model.addRow(new String[]{depId, TransType, StoreName, formatCrore(pur_amt), formatCrore(amt)});

        }

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        tblData.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tblData.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

    }

    public static String formatCrore(String str) {
        // System.out.println("str===="+str);
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
        //System.out.println("return str=="+str);
        return str;
    }
    /* Auto Generated Method - setFieldNames()
    This method assigns name for all the components.
    Other functions are working based on this name. */

    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        //   lblMsg.setName("lblMsg");
        tbrIndend.setName("tbrIndend");
        lblAmt.setName("lblAmt");
        lblDepoId.setName("lblDepoId");
        lblIndDate.setName("lblIndDate");
        lblPurAmt.setName("lblPurAmt");
        lblStoreName.setName("lblStoreName");
        lblTransType.setName("lblTransType");
        //t

        txtTotalPurAmt.setName("txtAmount");
        txtPurAmount.setName("txtPurAmount");
        txtStoreName.setName("txtStoreName");
        txtDepoId.setName("txtDepoId");
        lblSupplier.setName("lblSupplier");
        cboSupplier.setName("cboSupplier");
        tdtDateIndand.setName("tdtDateIndand");
        tblData.setName("tblData");
        lblVatAmt.setName("lblVatAmt");
        lblPurchBillNo.setName("lblPurchBillNo");
        lblTinNo.setName("lblTinNo");
        lblSalesDate.setName("lblSalesDate");
        txtVatAmt.setName("txtVatAmt");
        txtPurcBillNo.setName("txtPurcBillNo");
        txtTinNo.setName("txtTinNo");
        tdtBillDate.setName("tdtSalesDate");
    }

    public void initTableData() {
        String data[][] = {{}};
        String col[] = {"Depo", "TransType", "Store Name", "Purchase Amount", "Amount"};
        //  DefaultTableModel dataModel = new DefaultTableModel();
        // dataModel.setDataVector(dataVector
        //  model = new DefaultTableModel(data,col);


        model = new DefaultTableModel(data, col) {

            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };



        //  tblData.getCellEditor().stopCellEditing();
        tblData.setModel(model);

    }

    private void setMaxLengths() {
        txtPurAmount.setValidation(new CurrencyValidation(16, 2));
        txtTotalPurAmt.setValidation(new CurrencyValidation(16, 2));
        txtVatAmt.setValidation(new CurrencyValidation(16, 2));
        txtAmount.setValidation(new CurrencyValidation(16, 2));
        txtVatIndAmount.setValidation(new CurrencyValidation(16, 2));
        txtOtherExp.setValidation(new CurrencyValidation(16, 2));
        txtMiscIncome.setValidation(new CurrencyValidation(16, 2));
        txtOtherIncome.setValidation(new CurrencyValidation(16, 2));
        txtGSTAmt.setValidation(new CurrencyValidation(16, 2));// Added by nithya on 27-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction
        txtCommRecd.setValidation(new CurrencyValidation(16, 2));
        txtRoundOff.setValidation(new CurrencyValidation(16, 2));
        txtNarration.setAllowAll(true);
        txtstIndentNo.setAllowAll(true);

    }

    private void setObservable() {
        try {
            observable = IndendOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            //parseException.logException(e,true);
            System.out.println("Error in setObservable():" + e);
        }
    }

    private void initComponentData() {
        try {
            cboTransType.setModel(observable.getCbmTransType());
            cboSupplier.setModel(observable.getCbmSupplierName());
            tdtDateIndand.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
            /*  
            // cboDepoId.setModel(observable.getCbmDepoId());
            cboTransType.setModel(observable.getCbmTransType());
            
            List aList=ClientUtil.executeQuery("Indend.getIndendDepos",new HashMap());
            cboDepoId.addItem("");
            for(int i=0;i<aList.size();i++) {
            HashMap map=(HashMap)aList.get(i);
            String depId=map.get("DEPID").toString();
            String depName=map.get("DEPO_NAME").toString();
            if(depId!=null) {
            
            cboDepoId.addItem(depName);
            }
            }*/
        } catch (ClassCastException e) {
            //parseException.logException(e,true);
            System.out.println("Error in initComponentData():" + e);
        }
    }

    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
//          ArrayList lst = new ArrayList();
        if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)
                || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
            viewMap.put(CommonConstants.MAP_NAME, "Indend.getSelectIndendList");

//            lst.add("IRID");
//            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
//            lst = null;
        } else if (viewType.equals("DEPO")) {
            viewMap.put(CommonConstants.MAP_NAME, "Indend.getIndendDepos");
//            lst.add("STORE");
//            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
//            lst = null;
        } else if (viewType.equals(ClientConstants.ACTION_STATUS[2])) {
            viewMap.put(CommonConstants.MAP_NAME, "Indend.getSelectIndendListforEdit");
        }
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        viewMap.put(CommonConstants.MAP_WHERE, where);
        where = null;
        new ViewAll(this, viewMap).show();

    }

    public void fillData(Object map) {

        //        setModified(true);
        HashMap hash = (HashMap) map;
        if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            fromNewAuthorizeUI = true;
            newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
            fromManagerAuthorizeUI = true;
            ManagerauthorizeListUI = (AuthorizeListDebitUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
            fromCashierAuthorizeUI = true;
            CashierauthorizeListUI = (AuthorizeListCreditUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
            rejectFlag = 1;
        }
        if (viewType.equals("DEPO")) {
            txtDepoId.setText(hash.get("DEPID").toString());
            txtStoreName.setText(hash.get("DEPO_NAME").toString());
            getStoreName(hash.get("DEPID").toString());
        }
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                    || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                HashMap where = new HashMap();
                //System.out.println("hash.get"+hash.get("BORROWING_NO"));
                where.put("IRID", hash.get("IRID"));
                iridForEdit = CommonUtil.convertObjToStr(hash.get("IRID"));
                // where.put(CommonConstants.BRANCH_ID, "0001");
                hash.put(CommonConstants.MAP_WHERE, where);
                observable.populateData(hash);
                //System.out.println("observable.getFinalMap()??>>>>" + observable.getFinalMap());
                finalMap = observable.getFinalMap();
                tblMultiTransTable.setModel(observable.getTblCheckBookTable());
                calcTotal();
                processLstIterator = finalMap.keySet().iterator();
                String key1 = "";
//                HashMap m1=new HashMap();
                for (int i = 0; i < finalMap.size(); i++) {
                    key1 = (String) processLstIterator.next();
                    //System.out.println("#key1###### : " + key1);
                    IndendTO m1 = (IndendTO) finalMap.get(key1);
                    //System.out.println("###### m1=== : " + m1);
                    for (int j = 0; j < tblMultiTransTable.getRowCount(); j++) {
                        if (tblMultiTransTable.getValueAt(j, 0).equals(key1)) {
                            double totAmount = 0.0;
                            totAmount = ((CommonUtil.convertObjToDouble(m1.getAmount()) + CommonUtil.convertObjToDouble(m1.getVatAmt())
                                    + CommonUtil.convertObjToDouble(m1.getOthrExpAmt())) - (CommonUtil.convertObjToDouble(m1.getMiscAmt()) + CommonUtil.convertObjToDouble(m1.getCommRecvdAmt())));
                            TableModel model = tblMultiTransTable.getModel();
//                 int toRow = tblMultiTransTable.getSelectedRow();
//                 System.out.println("toRow ?@@@11@@ ??>>>>" + toRow);
                            model.setValueAt(totAmount, j, 4);
                        }
                    }
                }

                //depoId=observable.getCboDepoId();
                txtDepoId.setEnabled(true);
                calcTotal();


                // fillTxtNoOfTokens();
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                    ClientUtil.enableDisable(panIndend, false);
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    ClientUtil.enableDisable(panIndend, true);
                }
                if (viewType.equals(ClientConstants.ACTION_STATUS[2])) {
                    txtDepoId.setEnabled(false);
                }
                ClientUtil.enableDisable(panTrans, false, false, true);
                if (viewType.equals(AUTHORIZE)) {

                    ClientUtil.enableDisable(panIndend, false);
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    btnAuthorize.setEnabled(true);
                    btnAuthorize.requestFocusInWindow();
                    btnAuthorize.setFocusable(true);
                    calcTotal();
                }
                setButtonEnableDisable();
//                if (viewType.equals(ClientConstants.ACTION_STATUS[2])) {
//                    ClientUtil.enableDisable(panIndend, false);
//                    btnSave.setEnabled(true);
//                    txtPurAmount.setEnabled(true);
//                    txtVatIndAmount.setEnabled(true);
//                }              
            }
        }
        disableFileds();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            btnAuthorize.setEnabled(true);
            btnAuthorize.requestFocusInWindow();
            btnAuthorize.setFocusable(true);
            calcTotal();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panIndend = new com.see.truetransact.uicomponent.CPanel();
        cScrollPaneTable1 = new com.see.truetransact.uicomponent.CScrollPaneTable();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panTrans = new com.see.truetransact.uicomponent.CPanel();
        panIndeedData = new com.see.truetransact.uicomponent.CPanel();
        lblIndDate = new com.see.truetransact.uicomponent.CLabel();
        tdtBillDate = new com.see.truetransact.uicomponent.CDateField();
        lblDepoId = new com.see.truetransact.uicomponent.CLabel();
        lblStoreName = new com.see.truetransact.uicomponent.CLabel();
        txtStoreName = new com.see.truetransact.uicomponent.CTextField();
        lblTransType = new com.see.truetransact.uicomponent.CLabel();
        cboSupplier = new com.see.truetransact.uicomponent.CComboBox();
        lblPurAmt = new com.see.truetransact.uicomponent.CLabel();
        txtPurAmount = new com.see.truetransact.uicomponent.CTextField();
        lblTinNo = new com.see.truetransact.uicomponent.CLabel();
        txtTinNo = new com.see.truetransact.uicomponent.CTextField();
        txtDepoId = new com.see.truetransact.uicomponent.CTextField();
        btnDepo = new com.see.truetransact.uicomponent.CButton();
        lblAmt = new com.see.truetransact.uicomponent.CLabel();
        lblVatAmt = new com.see.truetransact.uicomponent.CLabel();
        txtPurcBillNo = new com.see.truetransact.uicomponent.CTextField();
        lblSalesDate = new com.see.truetransact.uicomponent.CLabel();
        lblPurchBillNo = new com.see.truetransact.uicomponent.CLabel();
        txtVatAmt = new com.see.truetransact.uicomponent.CTextField();
        tdtDateIndand = new com.see.truetransact.uicomponent.CDateField();
        lblSupplier = new com.see.truetransact.uicomponent.CLabel();
        cboTransType = new com.see.truetransact.uicomponent.CComboBox();
        lblVatIndAmt = new com.see.truetransact.uicomponent.CLabel();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        lblBillDate = new com.see.truetransact.uicomponent.CLabel();
        tdtSalesDate = new com.see.truetransact.uicomponent.CDateField();
        lblOtherExp = new com.see.truetransact.uicomponent.CLabel();
        txtVatIndAmount = new com.see.truetransact.uicomponent.CTextField();
        txtOtherExp = new com.see.truetransact.uicomponent.CTextField();
        lblMiscIncome = new com.see.truetransact.uicomponent.CLabel();
        txtMiscIncome = new com.see.truetransact.uicomponent.CTextField();
        lblCommRecvd = new com.see.truetransact.uicomponent.CLabel();
        txtCommRecd = new com.see.truetransact.uicomponent.CTextField();
        txtSalesmanName = new com.see.truetransact.uicomponent.CTextField();
        lblSalesmanName = new com.see.truetransact.uicomponent.CLabel();
        lblNarration = new com.see.truetransact.uicomponent.CLabel();
        txtNarration = new com.see.truetransact.uicomponent.CTextField();
        lblstIndentNo = new com.see.truetransact.uicomponent.CLabel();
        txtstIndentNo = new com.see.truetransact.uicomponent.CTextField();
        txtTotalPurAmt = new com.see.truetransact.uicomponent.CTextField();
        lblTotalPurAmt = new com.see.truetransact.uicomponent.CLabel();
        panMutipleTrans = new com.see.truetransact.uicomponent.CPanel();
        srpMultiTransTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblMultiTransTable = new com.see.truetransact.uicomponent.CTable();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        lblTotAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTotAmt = new com.see.truetransact.uicomponent.CTextField();
        lblOtherIncme = new com.see.truetransact.uicomponent.CLabel();
        txtOtherIncome = new com.see.truetransact.uicomponent.CTextField();
        panCheckBookBtn = new com.see.truetransact.uicomponent.CPanel();
        btnaddSave = new com.see.truetransact.uicomponent.CButton();
        btnAddDelete = new com.see.truetransact.uicomponent.CButton();
        btnaddNew = new com.see.truetransact.uicomponent.CButton();
        lblGSTAmt = new com.see.truetransact.uicomponent.CLabel();
        txtGSTAmt = new com.see.truetransact.uicomponent.CTextField();
        lblRoundOff = new com.see.truetransact.uicomponent.CLabel();
        txtRoundOff = new com.see.truetransact.uicomponent.CTextField();
        tbrIndend = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace51 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace52 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace53 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace54 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace55 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        mbrTokenConfig = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptView = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setDoubleBuffered(true);
        setFont(new java.awt.Font("Times New Roman", 0, 10)); // NOI18N
        setMaximumSize(new java.awt.Dimension(850, 640));
        setMinimumSize(new java.awt.Dimension(850, 640));
        setPreferredSize(new java.awt.Dimension(850, 640));
        getContentPane().setLayout(null);

        panIndend.setMaximumSize(new java.awt.Dimension(820, 460));
        panIndend.setMinimumSize(new java.awt.Dimension(820, 460));
        panIndend.setPreferredSize(new java.awt.Dimension(820, 460));
        panIndend.setLayout(null);

        cScrollPaneTable1.setMaximumSize(new java.awt.Dimension(454, 210));
        cScrollPaneTable1.setMinimumSize(new java.awt.Dimension(454, 210));
        cScrollPaneTable1.setPreferredSize(new java.awt.Dimension(454, 210));

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Depo", "TransType", "Store Name", "Purchase Amount", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblData.setDragEnabled(true);
        tblData.setMaximumSize(new java.awt.Dimension(454, 210));
        tblData.setMinimumSize(new java.awt.Dimension(454, 210));
        tblData.setPreferredScrollableViewportSize(new java.awt.Dimension(454, 210));
        tblData.setPreferredSize(new java.awt.Dimension(454, 210));
        cScrollPaneTable1.setViewportView(tblData);

        panIndend.add(cScrollPaneTable1);
        cScrollPaneTable1.setBounds(810, 30, 10, 220);

        panTrans.setMaximumSize(new java.awt.Dimension(840, 220));
        panTrans.setMinimumSize(new java.awt.Dimension(840, 220));
        panTrans.setPreferredSize(new java.awt.Dimension(840, 220));
        panTrans.setLayout(new java.awt.GridBagLayout());
        panIndend.add(panTrans);
        panTrans.setBounds(0, 340, 840, 200);

        panIndeedData.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panIndeedData.setMaximumSize(new java.awt.Dimension(340, 250));
        panIndeedData.setMinimumSize(new java.awt.Dimension(340, 250));
        panIndeedData.setPreferredSize(new java.awt.Dimension(700, 350));
        panIndeedData.setLayout(new java.awt.GridBagLayout());

        lblIndDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIndDate.setText("Indent Date");
        lblIndDate.setMaximumSize(new java.awt.Dimension(110, 16));
        lblIndDate.setMinimumSize(new java.awt.Dimension(110, 16));
        lblIndDate.setPreferredSize(new java.awt.Dimension(110, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(lblIndDate, gridBagConstraints);

        tdtBillDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtBillDate.setNextFocusableComponent(tdtSalesDate);
        tdtBillDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(tdtBillDate, gridBagConstraints);

        lblDepoId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDepoId.setText("Depo id");
        lblDepoId.setMaximumSize(new java.awt.Dimension(110, 16));
        lblDepoId.setMinimumSize(new java.awt.Dimension(110, 16));
        lblDepoId.setPreferredSize(new java.awt.Dimension(110, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(lblDepoId, gridBagConstraints);

        lblStoreName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStoreName.setText("Depo name");
        lblStoreName.setMaximumSize(new java.awt.Dimension(110, 16));
        lblStoreName.setMinimumSize(new java.awt.Dimension(110, 16));
        lblStoreName.setPreferredSize(new java.awt.Dimension(110, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(lblStoreName, gridBagConstraints);

        txtStoreName.setEditable(false);
        txtStoreName.setForeground(new java.awt.Color(0, 0, 255));
        txtStoreName.setAllowAll(true);
        txtStoreName.setMaximumSize(new java.awt.Dimension(200, 21));
        txtStoreName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtStoreName.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(txtStoreName, gridBagConstraints);

        lblTransType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTransType.setText("Trans Type");
        lblTransType.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblTransType.setMaximumSize(new java.awt.Dimension(110, 16));
        lblTransType.setMinimumSize(new java.awt.Dimension(110, 16));
        lblTransType.setPreferredSize(new java.awt.Dimension(110, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(lblTransType, gridBagConstraints);

        cboSupplier.setMaximumSize(new java.awt.Dimension(200, 21));
        cboSupplier.setMinimumSize(new java.awt.Dimension(200, 21));
        cboSupplier.setNextFocusableComponent(txtAmount);
        cboSupplier.setPopupWidth(350);
        cboSupplier.setPreferredSize(new java.awt.Dimension(200, 21));
        cboSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSupplierActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(cboSupplier, gridBagConstraints);

        lblPurAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPurAmt.setText("Liability Amount");
        lblPurAmt.setMaximumSize(new java.awt.Dimension(110, 16));
        lblPurAmt.setMinimumSize(new java.awt.Dimension(110, 16));
        lblPurAmt.setPreferredSize(new java.awt.Dimension(110, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(lblPurAmt, gridBagConstraints);

        txtPurAmount.setAllowAll(true);
        txtPurAmount.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPurAmount.setNextFocusableComponent(txtVatIndAmount);
        txtPurAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPurAmountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(txtPurAmount, gridBagConstraints);

        lblTinNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTinNo.setText("TIN No:");
        lblTinNo.setMaximumSize(new java.awt.Dimension(110, 16));
        lblTinNo.setMinimumSize(new java.awt.Dimension(110, 16));
        lblTinNo.setPreferredSize(new java.awt.Dimension(110, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(lblTinNo, gridBagConstraints);

        txtTinNo.setEditable(false);
        txtTinNo.setForeground(new java.awt.Color(0, 0, 255));
        txtTinNo.setAllowAll(true);
        txtTinNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTinNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTinNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTinNoActionPerformed(evt);
            }
        });
        txtTinNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTinNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(txtTinNo, gridBagConstraints);

        txtDepoId.setEnabled(false);
        txtDepoId.setMaximumSize(new java.awt.Dimension(130, 21));
        txtDepoId.setMinimumSize(new java.awt.Dimension(130, 21));
        txtDepoId.setPreferredSize(new java.awt.Dimension(130, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panIndeedData.add(txtDepoId, gridBagConstraints);

        btnDepo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDepo.setToolTipText("Search");
        btnDepo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnDepo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDepo.setNextFocusableComponent(cboTransType);
        btnDepo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDepo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panIndeedData.add(btnDepo, gridBagConstraints);

        lblAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAmt.setText("Purchase Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panIndeedData.add(lblAmt, gridBagConstraints);

        lblVatAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblVatAmt.setText("CESS");
        lblVatAmt.setMaximumSize(new java.awt.Dimension(110, 16));
        lblVatAmt.setMinimumSize(new java.awt.Dimension(110, 16));
        lblVatAmt.setPreferredSize(new java.awt.Dimension(110, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(lblVatAmt, gridBagConstraints);

        txtPurcBillNo.setAllowAll(true);
        txtPurcBillNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurcBillNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPurcBillNo.setNextFocusableComponent(tdtBillDate);
        txtPurcBillNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurcBillNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(txtPurcBillNo, gridBagConstraints);

        lblSalesDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSalesDate.setText("Sales Date");
        lblSalesDate.setMaximumSize(new java.awt.Dimension(110, 16));
        lblSalesDate.setMinimumSize(new java.awt.Dimension(110, 16));
        lblSalesDate.setPreferredSize(new java.awt.Dimension(110, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(lblSalesDate, gridBagConstraints);

        lblPurchBillNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPurchBillNo.setText("Purch Bill No:");
        lblPurchBillNo.setMaximumSize(new java.awt.Dimension(110, 16));
        lblPurchBillNo.setMinimumSize(new java.awt.Dimension(110, 16));
        lblPurchBillNo.setPreferredSize(new java.awt.Dimension(110, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panIndeedData.add(lblPurchBillNo, gridBagConstraints);

        txtVatAmt.setAllowAll(true);
        txtVatAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtVatAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtVatAmt.setNextFocusableComponent(txtOtherExp);
        txtVatAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtVatAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(txtVatAmt, gridBagConstraints);

        tdtDateIndand.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtDateIndand.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(tdtDateIndand, gridBagConstraints);

        lblSupplier.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSupplier.setText("Supplier");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(lblSupplier, gridBagConstraints);

        cboTransType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboTransType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboTransType.setNextFocusableComponent(cboSupplier);
        cboTransType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTransTypeActionPerformed(evt);
            }
        });
        cboTransType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboTransTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(cboTransType, gridBagConstraints);

        lblVatIndAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblVatIndAmt.setText("Vat Indend Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(lblVatIndAmt, gridBagConstraints);

        txtAmount.setAllowAll(true);
        txtAmount.setMaximumSize(new java.awt.Dimension(100, 21));
        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmount.setNextFocusableComponent(txtVatAmt);
        txtAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmountActionPerformed(evt);
            }
        });
        txtAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(txtAmount, gridBagConstraints);

        lblBillDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBillDate.setText("Bill Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(lblBillDate, gridBagConstraints);

        tdtSalesDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtSalesDate.setNextFocusableComponent(txtMiscIncome);
        tdtSalesDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(tdtSalesDate, gridBagConstraints);

        lblOtherExp.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOtherExp.setText("Other Exp.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(lblOtherExp, gridBagConstraints);

        txtVatIndAmount.setAllowAll(true);
        txtVatIndAmount.setMaximumSize(new java.awt.Dimension(100, 21));
        txtVatIndAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtVatIndAmount.setNextFocusableComponent(txtstIndentNo);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(txtVatIndAmount, gridBagConstraints);

        txtOtherExp.setAllowAll(true);
        txtOtherExp.setMaximumSize(new java.awt.Dimension(100, 21));
        txtOtherExp.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOtherExp.setNextFocusableComponent(txtPurAmount);
        txtOtherExp.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOtherExpFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(txtOtherExp, gridBagConstraints);

        lblMiscIncome.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMiscIncome.setText("Misc.Income");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(lblMiscIncome, gridBagConstraints);

        txtMiscIncome.setAllowAll(true);
        txtMiscIncome.setMaximumSize(new java.awt.Dimension(100, 21));
        txtMiscIncome.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMiscIncome.setNextFocusableComponent(txtCommRecd);
        txtMiscIncome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMiscIncomeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(txtMiscIncome, gridBagConstraints);

        lblCommRecvd.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCommRecvd.setText("Comm.Recvd");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(lblCommRecvd, gridBagConstraints);

        txtCommRecd.setAllowAll(true);
        txtCommRecd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtCommRecd.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCommRecd.setNextFocusableComponent(txtNarration);
        txtCommRecd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCommRecdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(txtCommRecd, gridBagConstraints);

        txtSalesmanName.setForeground(new java.awt.Color(0, 0, 255));
        txtSalesmanName.setMaximumSize(new java.awt.Dimension(200, 21));
        txtSalesmanName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtSalesmanName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtSalesmanName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSalesmanNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(txtSalesmanName, gridBagConstraints);

        lblSalesmanName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSalesmanName.setText("Salesman Name");
        lblSalesmanName.setMaximumSize(new java.awt.Dimension(110, 16));
        lblSalesmanName.setMinimumSize(new java.awt.Dimension(110, 16));
        lblSalesmanName.setPreferredSize(new java.awt.Dimension(110, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(lblSalesmanName, gridBagConstraints);

        lblNarration.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNarration.setText("Narration");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(lblNarration, gridBagConstraints);

        txtNarration.setMaximumSize(new java.awt.Dimension(240, 21));
        txtNarration.setMinimumSize(new java.awt.Dimension(240, 21));
        txtNarration.setNextFocusableComponent(btnaddSave);
        txtNarration.setPreferredSize(new java.awt.Dimension(240, 21));
        txtNarration.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNarrationFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(txtNarration, gridBagConstraints);

        lblstIndentNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblstIndentNo.setText("Store Indent No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(lblstIndentNo, gridBagConstraints);

        txtstIndentNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtstIndentNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtstIndentNo.setNextFocusableComponent(txtPurcBillNo);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(txtstIndentNo, gridBagConstraints);

        txtTotalPurAmt.setAllowAll(true);
        txtTotalPurAmt.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        txtTotalPurAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTotalPurAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotalPurAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTotalPurAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(txtTotalPurAmt, gridBagConstraints);

        lblTotalPurAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalPurAmt.setText("Total");
        lblTotalPurAmt.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIndeedData.add(lblTotalPurAmt, gridBagConstraints);

        panMutipleTrans.setMinimumSize(new java.awt.Dimension(280, 225));
        panMutipleTrans.setPreferredSize(new java.awt.Dimension(280, 225));
        panMutipleTrans.setLayout(new java.awt.GridBagLayout());

        srpMultiTransTable.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        srpMultiTransTable.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        srpMultiTransTable.setAutoscrolls(true);
        srpMultiTransTable.setMinimumSize(new java.awt.Dimension(250, 150));
        srpMultiTransTable.setPreferredSize(new java.awt.Dimension(354, 300));

        tblMultiTransTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl No", "Trans Type", "Amount", "Name", "Total"
            }
        ));
        tblMultiTransTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblMultiTransTable.setMinimumSize(new java.awt.Dimension(400, 1500));
        tblMultiTransTable.setPreferredScrollableViewportSize(new java.awt.Dimension(1200, 1500));
        tblMultiTransTable.setPreferredSize(new java.awt.Dimension(400, 1500));
        tblMultiTransTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMultiTransTableMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblMultiTransTableMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblMultiTransTableMouseReleased(evt);
            }
        });
        srpMultiTransTable.setViewportView(tblMultiTransTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 20);
        panMutipleTrans.add(srpMultiTransTable, gridBagConstraints);

        cPanel1.setMinimumSize(new java.awt.Dimension(250, 30));
        cPanel1.setPreferredSize(new java.awt.Dimension(250, 30));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        lblTotAmt.setText("Total Amount");
        lblTotAmt.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel1.add(lblTotAmt, gridBagConstraints);

        txtTotAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTotAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel1.add(txtTotAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMutipleTrans.add(cPanel1, gridBagConstraints);

        lblOtherIncme.setText("Total Income");
        lblOtherIncme.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 27, 2, 8);
        panMutipleTrans.add(lblOtherIncme, gridBagConstraints);

        txtOtherIncome.setEditable(false);
        txtOtherIncome.setAllowAll(true);
        txtOtherIncome.setEnabled(false);
        txtOtherIncome.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOtherIncome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOtherIncomeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 55, 2, 6);
        panMutipleTrans.add(txtOtherIncome, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 0);
        panIndeedData.add(panMutipleTrans, gridBagConstraints);

        panCheckBookBtn.setMinimumSize(new java.awt.Dimension(110, 35));
        panCheckBookBtn.setPreferredSize(new java.awt.Dimension(110, 35));
        panCheckBookBtn.setLayout(new java.awt.GridBagLayout());

        btnaddSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnaddSave.setToolTipText("New");
        btnaddSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnaddSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnaddSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnaddSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheckBookBtn.add(btnaddSave, gridBagConstraints);

        btnAddDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnAddDelete.setToolTipText("Delete");
        btnAddDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAddDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAddDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnAddDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheckBookBtn.add(btnAddDelete, gridBagConstraints);

        btnaddNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnaddNew.setToolTipText("New");
        btnaddNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnaddNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnaddNew.setNextFocusableComponent(txtDepoId);
        btnaddNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnaddNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheckBookBtn.add(btnaddNew, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        panIndeedData.add(panCheckBookBtn, gridBagConstraints);

        lblGSTAmt.setText("GST");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panIndeedData.add(lblGSTAmt, gridBagConstraints);

        txtGSTAmt.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        txtGSTAmt.setAllowNumber(true);
        txtGSTAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtGSTAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGSTAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panIndeedData.add(txtGSTAmt, gridBagConstraints);

        lblRoundOff.setText("Round Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panIndeedData.add(lblRoundOff, gridBagConstraints);

        txtRoundOff.setAllowNumber(true);
        txtRoundOff.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRoundOff.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRoundOffFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panIndeedData.add(txtRoundOff, gridBagConstraints);

        panIndend.add(panIndeedData);
        panIndeedData.setBounds(0, 30, 810, 310);

        tbrIndend.setMaximumSize(new java.awt.Dimension(527, 29));
        tbrIndend.setMinimumSize(new java.awt.Dimension(527, 29));
        tbrIndend.setPreferredSize(new java.awt.Dimension(527, 29));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setFocusable(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        tbrIndend.add(btnView);

        lbSpace3.setText("     ");
        tbrIndend.add(lbSpace3);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.setFocusable(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrIndend.add(btnNew);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrIndend.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrIndend.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrIndend.add(lblSpace52);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.setFocusable(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrIndend.add(btnDelete);

        lbSpace2.setText("     ");
        tbrIndend.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrIndend.add(btnSave);

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrIndend.add(lblSpace53);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrIndend.add(btnCancel);

        lblSpace3.setText("     ");
        tbrIndend.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrIndend.add(btnAuthorize);

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrIndend.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setFocusable(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrIndend.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrIndend.add(lblSpace55);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrIndend.add(btnReject);

        lblSpace5.setText("     ");
        tbrIndend.add(lblSpace5);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setFocusable(false);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrIndend.add(btnClose);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrIndend.add(lblSpace56);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.setFocusable(false);
        tbrIndend.add(btnPrint);

        panIndend.add(tbrIndend);
        tbrIndend.setBounds(0, 0, 527, 29);

        getContentPane().add(panIndend);
        panIndend.setBounds(0, 0, 840, 540);

        panStatus.setMinimumSize(new java.awt.Dimension(250, 22));
        panStatus.setLayout(new java.awt.GridBagLayout());
        getContentPane().add(panStatus);
        panStatus.setBounds(0, 0, 0, 0);
        getContentPane().add(lblMsg);
        lblMsg.setBounds(60, 540, 90, 20);

        lblSpace.setText(" Status :");
        getContentPane().add(lblSpace);
        lblSpace.setBounds(10, 540, 50, 22);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        getContentPane().add(lblStatus);
        lblStatus.setBounds(60, 540, 92, 22);

        mbrTokenConfig.setInheritsPopupMenu(true);

        mnuProcess.setText("Indent Register");
        mnuProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProcessActionPerformed(evt);
            }
        });

        mitNew.setText("New");
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptView);

        mitSave.setText("Save");
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mnuProcess.add(mitClose);

        mbrTokenConfig.add(mnuProcess);

        setJMenuBar(mbrTokenConfig);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDepoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepoActionPerformed
        // TODO add your handling code here:
        callView("DEPO");
        txtDepoId.setEditable(false);
       // btnDepo.setNextFocusableComponent(cboTransType);
    }//GEN-LAST:event_btnDepoActionPerformed

    private void cboSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSupplierActionPerformed
        // TODO add your handling code here:
        disableFileds();
//        cboSupplier.setModel(observable.getCbmSupplierName());
        HashMap suppName = new HashMap();
        suppName.put("SUPPLIERNAME", CommonUtil.convertObjToStr(cboSupplier.getSelectedItem()));
        List suppList = ClientUtil.executeQuery("getSelectTinNo", suppName);
        suppName = new HashMap();

        if (suppList != null && suppList.size() > 0) {
            //System.out.println("suppList>>>" + suppList);
            suppName = (HashMap) suppList.get(0);
            txtTinNo.setText(suppName.get("TIN_NUMBER").toString());
        }
        if (cboSupplier.getSelectedIndex() == 0) {
            txtTinNo.setText("");
        }
       // cboSupplier.setNextFocusableComponent(txtPurcBillNo);
    }//GEN-LAST:event_cboSupplierActionPerformed
    private void disableFileds() {
        // txtPurAmount.setEnabled(false);
        if (cboTransType.getSelectedItem() != null) {
            String item = cboTransType.getSelectedItem().toString();
            if (item != null && item.equals("Purchase")
                    || item.equals("Purchase Return")) {
                txtPurAmount.setEnabled(true);
            } else {
                txtPurAmount.setText("");
            }
        }
    }

    public void getStoreName(String depo) {
        try {
            // txtStoreName.setText("");
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("DEPID", depo);
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            List aList = ClientUtil.executeQuery("Indend.getStoreName", singleAuthorizeMap);
            for (int i = 0; i < aList.size(); i++) {
                HashMap map = (HashMap) aList.get(i);
                String Name = map.get("NAME").toString();
                gen_trans = map.get("GEN_TRANS").toString();
                if (Name != null) {
                    // txtStoreName.setText(Name);
                }
                if (gen_trans != null) {
                    if (gen_trans.equalsIgnoreCase("NO")) {
                        ClientUtil.enableDisable(panTrans, false, false, true);
                    } else {
                        ClientUtil.enableDisable(panTrans, true, false, true);
                    }
                }
            }
            //System.out.println("gen_trans!####!!####>>" + gen_trans);
        } catch (Exception e) {
            System.out.println("Exception in hetStoreName():" + e);
        }
    }
private void txtTinNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTinNoFocusLost
    // TODO add your handling code here:
    //transactionUI.setCallingAmount(txtTotalPurAmt.getText());
    transactionUI.setCallingTransType("TRANSFER");
    double amountIndend = CommonUtil.convertObjToDouble(txtTotalPurAmt.getText()).doubleValue();
    amountIndend = amountIndend + totalAmount;
    /*  if(amountIndend > sanctionAmount){
    ClientUtil.showAlertWindow("Amount Exceeds the Disbursment Limit!!");
    txtAmtBorrowed.setText("");
    amountBorrowed = 0.0;
    }*/
}//GEN-LAST:event_txtTinNoFocusLost

    private void txtPurAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPurAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPurAmountActionPerformed

    private void txtTinNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTinNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTinNoActionPerformed

    public static void main(String args[]) throws Exception {
        try {
            frmIndend objIfr = new frmIndend();
            objIfr.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
//        this.dispose();
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        disableFileds();
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        disableFileds();
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        transactionUI.setSaveEnableValue(ClientConstants.ACTIONTYPE_AUTHORIZE);

        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);

        disableFileds();
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)) {
            viewType = AUTHORIZE;
            //            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);
            whereMap.put("TRANS_DT", currDt.clone());

            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                mapParam.put(CommonConstants.MAP_NAME, "getIndendCashierAuthorizeList");
            } else {
                mapParam.put(CommonConstants.MAP_NAME, "getIndendAuthorizeList");
            }
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeIndend");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            //AuthorizeStatusUI fdf=new AuthorizeStatusUI(this,mapParam);
            authorizeUI.show();
            observable.setStatus();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)) {
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                HashMap countMap = new HashMap();
                //System.out.println("observable.getIRNo()asss>>>>>" + observable.getIRNo());
                countMap.put("IRID", observable.getIRNo());
                List countList = ClientUtil.executeQuery("getCountForReceiptCashierAuthorize", countMap);
                if (countList != null && countList.size() > 0) {
                    //System.out.println("nnnjdj>>>???");
                    countMap = new HashMap();
                    countMap = (HashMap) countList.get(0);
                    //if (CommonUtil.convertObjToInt(countMap.get("COUNT")) != 0) {
                    //    System.out.println("nnnjdj4566>>>???");
                    //   ClientUtil.showMessageWindow("Receipt cash transaction authorization for the Indend Id is pending\nPlease authorize the pending receipt cash transactions for the Indend Id first");
                    //   return;
                    // }
                }
            }
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);//commented on 13-Feb-2012
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("IRID", observable.getIRNo());
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);

            observable.setAuthMap(singleAuthorizeMap);
            observable.execute("");
//            ClientUtil.execute("authorizeIndend", singleAuthorizeMap);
            // ClientUtil.ex
            viewType = "";
            //            super.setOpenForEditBy(observable.getStatusBy());
            //            super.removeEditLock(txtTokenConfigId.getText());
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                if (fromNewAuthorizeUI) {
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.removeSelectedRow();
                    newauthorizeListUI.displayDetails("Indend Transaction");
                }
                if (fromAuthorizeUI) {
                    this.dispose();
                    authorizeListUI.setFocusToTable();
                    authorizeListUI.removeSelectedRow();
                    authorizeListUI.displayDetails("Indend Transaction");
                }
                btnCancelActionPerformed(null);
            }

            //  lblStatus.setText(authorizeStatus);
        }
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
//        cboSupplier.setSelectedIndex(0);
        setModified(false);
        cboSupplier.setEnabled(false);
        if (fromNewAuthorizeUI) {
            this.dispose();
            fromNewAuthorizeUI = false;
            newauthorizeListUI.setFocusToTable();
        }
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.setFocusToTable();
        }
        if (fromCashierAuthorizeUI) {
            CashierauthorizeListUI.removeSelectedRow();
            this.dispose();
            fromCashierAuthorizeUI = false;
            CashierauthorizeListUI.setFocusToTable();
        }
        if (fromManagerAuthorizeUI) {
            ManagerauthorizeListUI.removeSelectedRow();
            this.dispose();
            fromManagerAuthorizeUI = false;
            ManagerauthorizeListUI.setFocusToTable();
        }
        observable.resetForm();
        observable.destroyObjects();
        //txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panIndend, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        disableFileds();
        initTableData();
        txtSalesmanName.setText("");
        tableList = null;
        observable.insertTable(tableList);
        tblMultiTransTable.setModel(observable.getTblCheckBookTable());
        finalMap = null;
        slno = 0;
        selectedFlag = false;
        iridForEdit = "";
        btnaddNew.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        setModified(false);
        HashMap authEditMap = new HashMap();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            //System.out.println("iridForEdit>>>" + iridForEdit);
            authEditMap.put("IRID", iridForEdit);
            List authEditList = ClientUtil.executeQuery("getSelectAuthorizeStatusForIndendEditAfterAuthorize", authEditMap);
            if (authEditList.size() > 0 && authEditList != null) {
                authEditMap = (HashMap) authEditList.get(0);
                if (authEditMap.get("AUTHORIZE_STATUS") != null) {
                    if (authEditMap.get("AUTHORIZE_STATUS").toString().equals("AUTHORIZED") && finalMap != null && (!iridForEdit.equals("") && iridForEdit != null)) {
                        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                            processLstIterator = finalMap.keySet().iterator();
                            String key1 = "";
                            //System.out.println("fmap...." + finalMap.size());
                            for (int i = 0; i < finalMap.size(); i++) {
                                key1 = (String) processLstIterator.next();
                                IndendTO objTO = (IndendTO) finalMap.get(key1);
                                HashMap editVatLiabMap = new HashMap();
                                editVatLiabMap.put("LIABTY_AMT", objTO.getPurAmount());
                                editVatLiabMap.put("VATINDAMT", objTO.getVatIndAmt());
                                editVatLiabMap.put("IRID", iridForEdit);
                                editVatLiabMap.put("SLNO", objTO.getSlNo());
                                ClientUtil.execute("updateVatIndAmtAndLiabilityAmtForIndendEditAfterAuthorize", editVatLiabMap);
                            }
                            iridForEdit = "";
                            settings();
                        }
                    }
                    return;
                }
            }
        }
        //System.out.println("gen_trans@@@1111>>>>>" + gen_trans);
        if (gen_trans != null && gen_trans.equals("YES")) {
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                int transactionSize = 0;
                boolean cash = false;
                boolean transfer = false;
                if (transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(txtTotAmt.getText()).doubleValue() > 0) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                    return;
                } else {
                    if (CommonUtil.convertObjToDouble(txtTotAmt.getText()).doubleValue() > 0 || observable.getActionType() != ClientConstants.ACTIONTYPE_NEW) {
                        transactionSize = (transactionUI.getOutputTO()).size();
                        double transactionAmt = 0.0;
                        //int size = transactionUI.getTransactionOB().getTblTransDetails().getRowCount();
                        //for(int i=0;i<size;i++){
                        //    System.out.println("VALUES##############"+CommonUtil.convertObjToStr(transactionUI.getTransactionOB().getTblTransDetails().getValueAt(i, 2)));
                        transactionAmt += CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
                        // }
                        //transactionAmt+=Double.parseDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal());
                        //System.out.println("transactionUI.getOutputTO()##############" + transactionUI.getOutputTO());
                        if (transactionSize >= 0 && CommonUtil.convertObjToDouble(txtTotAmt.getText()).doubleValue() > 0
                                && CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue() > 0) {
                            if (CommonUtil.convertObjToDouble(txtTotAmt.getText()).doubleValue() != transactionAmt) {
                                ClientUtil.showAlertWindow("Transactions Amounts Are Not Tallied..!!");
                                return;
                            }
                        } else {
                            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                        }
                    } else if (transactionUI.getOutputTO().size() > 0) {
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    }
                }
                if (transactionSize == 0 && CommonUtil.convertObjToDouble(txtTotAmt.getText()).doubleValue() > 0) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                    return;
                } else if (transactionSize != 0) {
                    if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                        return;
                    }
                    if (transactionUI.getOutputTO().size() > 0) {
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                        savePerformed();
                    }
                }

            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                savePerformed();
            }
        } else {

            savePerformed();
        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        disableFileds();
        totSum = 0.0;
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed() {        
        // System.out.println("IN savePerformed");
        String action;
        //    System.out.println("IN observable.getActionType(): "+observable.getActionType());
        //      System.out.println("IN ClientConstants.ACTIONTYPE_NEW: "+ClientConstants.ACTIONTYPE_NEW);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            //     System.out.println("IN savePerformed ACTIONTYPE_NEW");

            action = CommonConstants.TOSTATUS_INSERT;
            saveAction(action);

        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            action = CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            action = CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
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

    /* Calls the execute method of TerminalOB to do insertion or updation or deletion */
    private void saveAction(String status) {
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        //  System.out.println("status saveAction11111: "+status);
        //       txtAmtBorrowed.
        final String mandatoryMessage = checkMandatory(panIndend);
//        StringBuffer message = new StringBuffer(mandatoryMessage);
//        if (txtDepoId.getText().equals("")) {
//            message.append(objMandatoryRB.getString("cboDepoId"));
//        }
//
//        if (cboTransType.getSelectedItem().equals("")) {
//            message.append(objMandatoryRB.getString("cboTransType"));
//        }
//        if (tdtDateIndand.getDateValue().equals("")) {
//            message.append(objMandatoryRB.getString("tdtDateIndand"));
//        }
//        if (observable.getActionType() == (ClientConstants.ACTIONTYPE_NEW)) {
//            if (cboTransType.getSelectedItem().equals("Purchase")) {
//                if (txtPurAmount.getText().equals("")) {
//                    message.append(objMandatoryRB.getString("txtPurAmount"));
//                }
//            }
//        }
//        if (txtAmount.getText().equals("")) {
//            message.append(objMandatoryRB.getString("txtAmount"));
//        }

        //setExpDateOnCalculation();
        //  System.out.println("status saveAction: "+status);

        if (tblMultiTransTable.getRowCount() <= 0) {
            ClientUtil.showAlertWindow("Please Save Value in the grid...");
            return;
        }
        if (mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else {

            if (observable.getActionType() != ClientConstants.ACTIONTYPE_NEW && observable.getActionType() != ClientConstants.ACTIONTYPE_EDIT) {
                updateOBFields();
            }
            observable.setFinalMap(finalMap);
            observable.execute(status);
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("IRNO");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                //   if (observable.getProxyReturnMap()!=null) {
                //      if (observable.getProxyReturnMap().containsKey("CONFIG_ID")) {
                //          lockMap.put("CONFIG_ID", observable.getProxyReturnMap().get("CONFIG_ID"));
                //      }
                //  }
                if (observable.getProxyReturnMap() != null) {
                    //Chaged By Suresh 20-Aug-2013
//                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
//                    ClientUtil.showMessageWindow("INDEND REGISTER ID : " + observable.getProxyReturnMap().get("IRNO"));
//                    }
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
//                    ClientUtil.showMessageWindow("EDITED INDEND REGISTER ID : " +observable.getIRNo());
                        ClientUtil.showMessageWindow("Updated Successfully !!! ");
                        displayTransDetail(observable.getProxyReturnMap());
                    }
                    if (observable.getProxyReturnMap().containsKey("CONFIG_ID")) {
                        lockMap.put("CONFIG_ID", observable.getProxyReturnMap().get("CONFIG_ID"));
                    }
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && observable.getTxtGenTrans().equals("YES")) {
                        displayTransDetail(observable.getProxyReturnMap());
                    } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && observable.getTxtGenTrans().equals("NO")) {
                        ClientUtil.showMessageWindow("Saved ");
                    }
                }
                if (status == CommonConstants.TOSTATUS_UPDATE) {
                    lockMap.put("IRNO", observable.getIRNo());
                }
                //      setEditLockMap(lockMap);
                // setEditLock();
                settings();
            }
        }

    }

    private void displayTransDetail(HashMap proxyResultMap) {
        try {
            System.out.println("@#$@@$@@@$ proxyResultMap : " + proxyResultMap);
            String cashDisplayStr = "Cash Transaction Details...\n";
            String transferDisplayStr = "Transfer Transaction Details...\n";
            String displayStr = "";
            String transId = "";
            String transType = "";
            Object keys[] = proxyResultMap.keySet().toArray();
            int cashCount = 0;
            int CreditcashCount = 0;
            int DebitcashCount = 0;
            int transferCount = 0;
            List tempList = null;
            HashMap transMap = null;
            String actNum = "";
            ArrayList crList = new ArrayList();
            ArrayList drList = new ArrayList();

            for (int i = 0; i < keys.length; i++) {
                if (proxyResultMap.get(keys[i]) instanceof String) {
                    continue;
                }
                tempList = (List) proxyResultMap.get(keys[i]);
                if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                    for (int j = 0; j < tempList.size(); j++) {
                        transMap = (HashMap) tempList.get(j);
                        if (j == 0) {
                            transId = (String) transMap.get("SINGLE_TRANS_ID");
                        }
                        cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                                + "   Trans Type : " + transMap.get("TRANS_TYPE");
                        actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                        if (actNum != null && !actNum.equals("")) {
                            cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                    + "   Amount : " + transMap.get("AMOUNT") + "\n";
                        } else {
                            cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                    + "   Amount : " + transMap.get("AMOUNT") + "\n";
                        }
                        //System.out.println("transMap===" + transMap);
                        if (transMap.get("TRANS_TYPE").equals("DEBIT")) {
                            DebitcashCount++;
                            //System.out.println("DebitcashCount===" + DebitcashCount);
                        }
                        if (transMap.get("TRANS_TYPE").equals("CREDIT")) {
                            CreditcashCount++;
                            //System.out.println("CreditcashCount===" + CreditcashCount);
                        }
                    }
//                cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID");

                    cashCount++;
                } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                    for (int j = 0; j < tempList.size(); j++) {
                        transMap = (HashMap) tempList.get(j);
                        if (j == 0) {
                            transId = (String) transMap.get("SINGLE_TRANS_ID");
                        }
//                    transferDisplayStr += "   Batch Id : " + transMap.get("BATCH_ID");//"Trans Id : " + transMap.get("TRANS_ID")
                        break;
                        //  + "   Batch Id : " + transMap.get("BATCH_ID")
                        //  + "   Trans Type : " + transMap.get("TRANS_TYPE");
//                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
//                    if (actNum != null && !actNum.equals("")) {
//                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
//                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
//                    } else {
//                        transferDisplayStr += "   Account Head : " + transMap.get("AC_HD_ID")
//                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
//                    }
                    }
                    transferDisplayStr += "   Batch Id : " + transMap.get("BATCH_ID");
                    transferCount++;
                }
            }
            if (cashCount > 0) {
                displayStr += cashDisplayStr;
            }
            if (transferCount > 0) {
                displayStr += transferDisplayStr;
            }
            if (!displayStr.equals("")) {
                ClientUtil.showMessageWindow("" + displayStr);
            }

            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE, COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            //System.out.println("#$#$$ yesNo : " + yesNo);
            if (yesNo == 0) {
                TTIntegration ttIntgration = null;
                HashMap paramMap = new HashMap();
                // paramMap.put("TransId", transId);
                paramMap.put("TransDt", ClientUtil.getCurrentDateProperFormat());
                //System.out.println("ClientUtil.getCurrentDateProperFormat()" + ClientUtil.getCurrentDateProperFormat());
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    //System.out.println("observable.getTdtDateIndand()>>>" + observable.getTdtDateIndand());
                    paramMap.put("TransDt", observable.getTdtDateIndand());
                }

                paramMap.put("BranchId", ProxyParameters.BRANCH_ID);


                for (int i = 0; i < keys.length; i++) {
                    if (proxyResultMap.get(keys[i]) instanceof String) {
                        continue;
                    }
                    tempList = (List) proxyResultMap.get(keys[i]);
                    if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                        //transMap = (HashMap) tempList.get(0);
//                if(transMap.get("TRANS_TYPE").equals("DEBIT"))
//                    {
//                        paramMap.put("FromTransId",transMap.get("TRANS_ID"));
//                    }else
//                    {
//                        paramMap.put("FromTransId",transMap.get("TRANS_ID"));
//                    }
                        for (int j = 0; j < tempList.size(); j++) {
                            transMap = (HashMap) tempList.get(j);
                            if (j == 0) {
                                transId = (String) transMap.get("SINGLE_TRANS_ID");
                                paramMap.put("TransId", transId);
                            }
//                    transferDisplayStr += "   Batch Id : " + transMap.get("BATCH_ID");//"Trans Id : " + transMap.get("TRANS_ID")
                            break;
                            //if(transMap.get("TRANS_TYPE").equals("DEBIT"))
                            //{
                            //   drList.add(tempList.get(j));
                            //paramMap.put("ToTransId",transMap.get("TRANS_ID"));
                            //}else
                            //{
                            //crList.add(tempList.get(j));
                            //}}
                            //paramMap.put("ToTransId",transMap.get("TRANS_ID"));
                        }
                    } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                        for (int j = 0; j < tempList.size(); j++) {
                            transMap = (HashMap) tempList.get(j);
                            if (j == 0) {
                                transId = (String) transMap.get("SINGLE_TRANS_ID");
                                paramMap.put("TransId", transId);
                            }
//                    transferDisplayStr += "   Batch Id : " + transMap.get("BATCH_ID");//"Trans Id : " + transMap.get("TRANS_ID")
                            break;
                        }
                    }

                }
                // System.out.println("crList==="+crList);
                // for(int i=0;i<crList.size();i++)
                //{
                //   HashMap mapCr=(HashMap)crList.get(i);
                //   if(i==0)
                //  {
                //    paramMap.put("FromTransIdCr",mapCr.get("SINGLE_TRANS_ID"));  
                //  }
                //  else
                //  {
                //   paramMap.put("ToTransIdCr",mapCr.get("SINGLE_TRANS_ID"));  
                //   }

                // }
                //if(!paramMap.containsKey("ToTransIdCr"))
                //      {
                //         paramMap.put("ToTransIdCr",paramMap.get("FromTransIdCr")); 
                //      }
                //System.out.println("drList==="+drList);
                // for(int i=0;i<drList.size();i++)
                // {
                //    HashMap mapDr=(HashMap)drList.get(i);
                //   if(i==0)
                //   {
                //     paramMap.put("FromTransIdDr",mapDr.get("SINGLE_TRANS_ID"));  
                //   }
                //   else
                //   {
                //      paramMap.put("ToTransIdDr",mapDr.get("SINGLE_TRANS_ID")); 
                //   }

                // }
                // if(!paramMap.containsKey("ToTransIdDr"))
                //          {
                //            paramMap.put("ToTransIdDr",paramMap.get("FromTransIdDr"));  
                //        }

                //System.out.println("paramMap===" + paramMap);
                ttIntgration.setParam(paramMap);
                String reportName = "";
//            transType = cboSupplier.getSelectedItem() + "";
                //  System.out.println("crList.size()==="+crList.size());
                //  System.out.println("drList.size()==="+drList.size());
                //  System.out.println("transferCount==="+transferCount);
                //System.out.println("transferCount===" + transferCount);
                //System.out.println("CreditcashCount===" + CreditcashCount);
                //System.out.println("DebitcashCount===" + DebitcashCount);

                if (transferCount > 0) {
                    reportName = "ReceiptPayment";
                    //System.out.println("ReceiptPayment@@@@@" + reportName);
                    ttIntgration.integrationForPrint(reportName, false);
                }

                if (CreditcashCount > 0) {
                    reportName = "CashReceipt";
                    //System.out.println("CashReceipt@@@@@" + reportName);
                    ttIntgration.integrationForPrint(reportName, false);
                }

                if (DebitcashCount > 0) {
                    reportName = "CashPayment";
                    //System.out.println("CashPayment@@@@@" + reportName);
                    ttIntgration.integrationForPrint(reportName, false);
                }

                // if(drList.size()>0)
                // {
                //     reportName = "CashPayment";
                // }

//            //else if (transType.equals("Purchase") || transType.equals("Sales Return")) { jiby
//            else if (transMap.get("TRANS_TYPE").equals("DEBIT")) {
//                reportName = "CashPayment";
//            } else {
//                reportName = "CashReceipt";
//            }

                //ttIntgration.integrationForPrint(reportName, false);
                // if(crList.size()>0)
                //   {

                //     reportName = "CashReceipt"; 
                //  }
                // ttIntgration.integrationForPrint(reportName, false);

            }
        } catch (Exception e) {
            //ttIntgration.integrationForPrint("CashReceipt", false);
        }


    }

    /**
     * Method used to check whether the Mandatory Fields in the Form are Filled
     * or not
     */
    private String checkMandatory(javax.swing.JComponent component) {
        //  return new MandatoryCheck().checkMandatory(getClass().getName(), component, getMandatoryHashMap());
        return "";
        //validation error
    }

    /**
     * Method used to Give a Alert when any Mandatory Field is not filled by the
     * user
     */
    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    /* set the screen after the updation,insertion, deletion */

    private void settings() {
        observable.resetForm();
        //  txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panIndend, false);
//        setButtonEnableDisable();
        observable.setResultStatus();
        btnCancelActionPerformed(null);
        disableFileds();

    }

    public Date getDateValue(String date1) {
        DateFormat formatter;
        Date date = null;
        try {
            //System.out.println("date1 66666666666=========:" + date1);
            // String str_date=date1;
            //  formatter = new SimpleDateFormat("MM/dd/yyyy");
            //  date = (Date)formatter.parse(str_date);
            //      System.out.println("dateAFETRRR 66666666666=========:"+date);




            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
            // String s1 = "2008-03-30T15:30:00.000+02:00";
            // date1 = date1.substring(0, date1.indexOf('.'));
            //System.out.println("Result==> " + sdf2.format(sdf1.parse(date1)));
            date = new Date(sdf2.format(sdf1.parse(date1)));
            //System.out.println("date IOOOOOOO==> " + date);
        } catch (ParseException e) {
            System.out.println("Error in getDateValue():" + e);
        }
        return date;
    }

    public void updateOBFields() {
      //  System.out.println("kjnglksgnmm gen_trans" + gen_trans + "jkdfkjbjkkjd" + txtStoreName.getText());
        observable.setTxtVatIndAmt(CommonUtil.convertObjToDouble(txtVatIndAmount.getText()));
        observable.setTxtDepoId(txtDepoId.getText());
        observable.setCboTransType((String) cboTransType.getSelectedItem());
        observable.setCboIndentHeads(CommonUtil.convertObjToStr(cboTransType.getSelectedItem()));
        if (!cboTransType.getSelectedItem().equals("Sales")) {
            observable.setTxtPurcBillNo(txtPurcBillNo.getText());
        }
        if (cboTransType.getSelectedItem().equals("Purchase Return") || cboTransType.getSelectedItem().equals("Purchase")) {
//        System.out.println("((ComboBoxModel)cboSupplier.getModel()).getKeyForSelected()).toString()>>>"+((ComboBoxModel)cboSupplier.getModel()).getKeyForSelected().toString());
            observable.setCboSupplier(CommonUtil.convertObjToStr(((ComboBoxModel) cboSupplier.getModel()).getKeyForSelected()).toString());
            observable.setTxtPurAmount(Double.valueOf(txtPurAmount.getText()));
        } else {
            observable.setTxtPurAmount(Double.valueOf(0.0));
        }
        observable.setTdtDateIndand(getDateValue(tdtDateIndand.getDateValue()));

        observable.setTxtAmount(Double.valueOf(txtAmount.getText()));
        observable.setTxtStoreName(txtStoreName.getText());
        //System.out.println("observable.getActionType()111>>>" + observable.getActionType() + "ClientConstants.ACTIONTYPE_NEW>" + ClientConstants.ACTIONTYPE_NEW);
        if (observable.getActionType() == (ClientConstants.ACTIONTYPE_NEW) || observable.getActionType() == (ClientConstants.ACTIONTYPE_EDIT)) {
            observable.setTxtGenTrans(gen_trans);
        }
        observable.setTxtTinNo(txtTinNo.getText());
        observable.setTxtVatAmt(Double.valueOf(txtVatAmt.getText()));
        observable.setTdtBillDate(getDateValue(tdtBillDate.getDateValue()));
        observable.setTdtSalesDate(getDateValue(tdtSalesDate.getDateValue()));
        observable.setTxtCommRecvdAmt(CommonUtil.convertObjToDouble(txtCommRecd.getText()));
        observable.setTxtOthrExpAmt(CommonUtil.convertObjToDouble(txtOtherExp.getText()));
        observable.setTxtMiscAmt(CommonUtil.convertObjToDouble(txtMiscIncome.getText()));
        observable.setTxtOtherIncome(CommonUtil.convertObjToDouble(txtOtherIncome.getText()));
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setTxtNarration(txtNarration.getText());
        observable.setTxtstIndentNo(txtstIndentNo.getText());
        observable.setTxtSalesmanName(txtSalesmanName.getText());
        observable.setTxtStatusBy(TrueTransactMain.USER_ID);//jiby
        observable.setTxtGSTAmt(txtGSTAmt.getText()); // Added by nithya on 27-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction
        observable.setTxtRoundOff(txtRoundOff.getText());
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView(ClientConstants.ACTION_STATUS[3]);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        txtStoreName.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        transactionUI.setSaveEnableValue(ClientConstants.ACTIONTYPE_VIEW);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        txtStoreName.setEnabled(false);
        disableFileds();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            HashMap authEditMap = new HashMap();
            //System.out.println("iridForEdit111>>>" + iridForEdit);
            authEditMap.put("IRID", iridForEdit);
            List authEditList = ClientUtil.executeQuery("getSelectAuthorizeStatusForIndendEditAfterAuthorize", authEditMap);
            if (authEditList.size() > 0 && authEditList != null) {
                authEditMap = (HashMap) authEditList.get(0);
                if (authEditMap.get("AUTHORIZE_STATUS") != null) {
                    if (authEditMap.get("AUTHORIZE_STATUS").toString().equals("AUTHORIZED")) {
                        ClientUtil.enableDisable(panIndend, false);
                        btnSave.setEnabled(true);
                        txtPurAmount.setEnabled(true);
                        txtVatIndAmount.setEnabled(true);
                        txtOtherExp.setEnabled(false);
                        txtstIndentNo.setEnabled(false);
                        txtPurcBillNo.setEnabled(false);
                        tdtBillDate.setEnabled(false);
                        tdtSalesDate.setEnabled(false);
                        txtMiscIncome.setEnabled(false);
                        txtOtherIncome.setEnabled(false);
                        txtCommRecd.setEnabled(false);
                    }
                }
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        ClientUtil.enableDisable(panIndend, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        setModified(true);
        txtTotalPurAmt.setEnabled(true);
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        transactionUI.setSaveEnableValue(ClientConstants.ACTIONTYPE_NEW);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        txtStoreName.setEnabled(false);
        tdtDateIndand.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
        txtSalesmanName.setText("");
        txtPurAmount.setText("");
        finalMap = null;
        iridForEdit = "";
        slno = 0;
        selectedFlag = false;
        btnaddNew.setEnabled(true);
        txtVatAmt.setText("0.00");
        txtGSTAmt.setText("0.00");// Added by nithya on 27-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction
        txtRoundOff.setText("0.0");
        txtMiscIncome.setText("0.00");
        txtOtherIncome.setText("0.00");
        btnDepo.setEnabled(false);
        txtOtherIncome.setEnabled(false);
        // txtDepoId.setEnabled(true);
        // btnDepo.setEnabled(true);
    }//GEN-LAST:event_btnNewActionPerformed
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnSave.setEnabled(!btnSave.isEnabled());
        btnCancel.setEnabled(!btnCancel.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        //  lblStatus.setText(observable.getLblStatus());
        btnView.setEnabled(!btnView.isEnabled());
        txtStoreName.setEnabled(false);
        txtDepoId.setEnabled(false);
//        cboSupplier.setSelectedItem(null);
//        cboSupplier.setEnabled(false);

    }
    private void mnuProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuProcessActionPerformed

private void cboTransTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTransTypeActionPerformed
// TODO add your handling code here:

    //System.out.println("cboTransType>>>" + cboTransType);

    if (cboTransType.getSelectedItem().equals("Sales Return") || cboTransType.getSelectedItem().equals("Sales")) {
        HashMap salesMap = new HashMap();
        txtDepoId.setEnabled(true);
        salesMap.put("DEP_ID", txtDepoId.getText());
        List salesList = ClientUtil.executeQuery("getIndendSalesmanName", salesMap);
        if (salesList != null && salesList.size() > 0) {
            salesMap = (HashMap) salesList.get(0);
        }
        //System.out.println("CommonUtil.convertObjToStr(salesMap.get(SALESNAME))>>>" + CommonUtil.convertObjToStr(salesMap.get("SALESNAME")));
        txtSalesmanName.setEnabled(true);
        tdtSalesDate.setEnabled(true);
        txtTinNo.setEnabled(false);
        txtMiscIncome.setEnabled(true);
        //txtOtherIncome.setEnabled(true);
        txtVatIndAmount.setEnabled(false);
        txtVatIndAmount.setText("");
        txtSalesmanName.setText(CommonUtil.convertObjToStr(salesMap.get("SALESNAME")));
        transactionUI.setCallingApplicantName(CommonUtil.convertObjToStr(salesMap.get("SALESNAME")));
        txtDepoId.setEnabled(false);
        if(cboTransType.getSelectedItem().equals("Sales")){
            txtRoundOff.setEnabled(false);
    } else {
            txtRoundOff.setEnabled(true);
        }
    } else {
        txtRoundOff.setEnabled(true);
        txtSalesmanName.setEnabled(false);
        tdtSalesDate.setEnabled(false);
        txtTinNo.setEnabled(true);
        txtTinNo.setEditable(false);
    }

    if (cboTransType.getSelectedItem().equals("Purchase Return") || cboTransType.getSelectedItem().equals("Purchase")) {
        txtPurAmount.setEnabled(true);
        txtPurAmount.setEditable(true);
        cboSupplier.setEnabled(true);
        txtstIndentNo.setEnabled(true);
        tdtBillDate.setEnabled(true);
        txtPurcBillNo.setEnabled(true);
        txtSalesmanName.setEnabled(false);
        tdtSalesDate.setEnabled(false);
        txtVatIndAmount.setEnabled(true);
        txtVatIndAmount.setEditable(true);
        txtVatIndAmount.setText("0.00");
    } else {
        txtPurAmount.setEnabled(false);
        txtPurAmount.setEditable(false);
        cboSupplier.setEnabled(false);
        txtVatIndAmount.setEnabled(true);
        txtVatIndAmount.setEditable(true);
    }
    if (cboTransType.getSelectedItem().equals("Sales Return")) {
        txtVatIndAmount.setEditable(false);
        txtVatIndAmount.setEnabled(false);
    }
    if (!cboTransType.getSelectedItem().equals("Purchase") && !cboTransType.getSelectedItem().equals("Sales Return")) {
        txtVatAmt.setText("0.00");
        txtVatIndAmount.setText("0.00");
        txtMiscIncome.setText("0.00");
        txtOtherIncome.setText("0.00");
        txtCommRecd.setEnabled(false);
        txtOtherExp.setEnabled(false);
        txtCommRecd.setText("");
        txtOtherExp.setText("");
    } else {
        txtCommRecd.setEnabled(true);
        txtMiscIncome.setEnabled(true);
        //txtOtherIncome.setEnabled(true);
        txtOtherExp.setEnabled(true);
        txtVatAmt.setText("0.00");
        txtOtherExp.setText("0.00");
        txtMiscIncome.setText("0.00");
        txtOtherIncome.setText("0.00");
        txtCommRecd.setText("0.00");
    }
    if (cboTransType.getSelectedItem().equals("Sales")) {
        tdtBillDate.setEnabled(false);
        txtPurcBillNo.setEnabled(false);
        txtMiscIncome.setEnabled(true);
         //txtOtherIncome.setEnabled(true);
        txtstIndentNo.setEnabled(false);
        txtSalesmanName.setEnabled(false);
        txtVatIndAmount.setEnabled(false);
        txtMiscIncome.setText("0.00");
        txtOtherIncome.setText("0.00");
        txtVatAmt.setText("0.00");
        txtVatIndAmount.setText("");
        lblAmt.setText("Sale Amount*");
    }
    if (cboTransType.getSelectedIndex() == 0) {
        cboSupplier.setSelectedIndex(0);
    }

    //Added By Suresh 20-Aug-2013
    if (cboTransType.getSelectedItem().equals("Sales") || cboTransType.getSelectedItem().equals("Sales Return")) {
        lblAmt.setText("Sale Amount*");
        //cboTransType.setNextFocusableComponent(txtAmount);
    } else if (cboTransType.getSelectedItem().equals("Purchase") || cboTransType.getSelectedItem().equals("Purchase Return")) {
        lblAmt.setText("Purchase Amount*");
       // cboTransType.setNextFocusableComponent(cboSupplier);
    } else {
        lblAmt.setText("Amount*");
    }   
     if (cboTransType.getSelectedItem().equals("Sales") || cboTransType.getSelectedItem().equals("Purchase Return")) {//9023
         txtOtherExp.setEnabled(true);
     }
    
}//GEN-LAST:event_cboTransTypeActionPerformed

    private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmountActionPerformed

    private void txtAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusLost
        // TODO add your handling code here:
        setTransactionAmount();
        resetTranAmount();
       // txtAmount.setNextFocusableComponent(txtVatAmt);
    }//GEN-LAST:event_txtAmountFocusLost
    private void resetTranAmount() {
        //transactionUI.cancelAction(false);
        //transactionUI.setButtonEnableDisable(true);
        //transactionUI.resetObjects();
        //transactionUI.setCallingAmount(txtTotalPurAmt.getText());
        txtTotalPurAmt.setEnabled(false);
        txtTotAmt.setEnabled(false);
    }
    private void txtVatAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtVatAmtFocusLost
        // TODO add your handling code here:
        setTransactionAmount();
        resetTranAmount();
        if (cboTransType.getSelectedItem().equals("Purchase") || cboTransType.getSelectedItem().equals("Purchase Return")) {        
            txtVatAmt.setNextFocusableComponent(txtOtherExp);
        } else{
            txtVatAmt.setNextFocusableComponent(tdtSalesDate);
        }
        
    }//GEN-LAST:event_txtVatAmtFocusLost

    private void txtOtherExpFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOtherExpFocusLost
        // TODO add your handling code here:
        setTransactionAmount();
        resetTranAmount();
        //txtOtherExp.setNextFocusableComponent(txtMiscIncome);       
        txtOtherExp.setNextFocusableComponent(txtGSTAmt);// Added by nithya on 27-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction
    }//GEN-LAST:event_txtOtherExpFocusLost

    private void txtMiscIncomeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMiscIncomeFocusLost
        // TODO add your handling code here:
        setTransactionAmount();
        resetTranAmount();
        if (cboTransType.getSelectedItem().equals("Purchase") || cboTransType.getSelectedItem().equals("Purchase Return")) {        
          //  txtMiscIncome.setNextFocusableComponent(txtCommRecd);
        } else{
         //   txtMiscIncome.setNextFocusableComponent(tdtSalesDate);
        }        
    }//GEN-LAST:event_txtMiscIncomeFocusLost

    private void txtCommRecdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCommRecdFocusLost
        // TODO add your handling code here:
        setTransactionAmount();
        resetTranAmount();
      //  txtCommRecd.setNextFocusableComponent(txtstIndentNo);
    }//GEN-LAST:event_txtCommRecdFocusLost

    private void txtTotalPurAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotalPurAmtFocusLost
        // TODO add your handling code here:
        transactionUI.setButtonEnableDisable(true);
        //transactionUI.setCallingAmount(txtTotalPurAmt.getText());

    }//GEN-LAST:event_txtTotalPurAmtFocusLost

    private void cboTransTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboTransTypeFocusLost
        // TODO add your handling code here:
        if (!cboTransType.getSelectedItem().equals("Purchase") && !cboTransType.getSelectedItem().equals("Sales Return")) {
            txtCommRecd.setEnabled(false);
            // txtMiscIncome.setEnabled(false);
            txtOtherExp.setEnabled(false);
        }
        if (cboTransType.getSelectedItem().equals("Purchase") || cboTransType.getSelectedItem().equals("Purchase Return")) {        
            //cboTransType.setNextFocusableComponent(cboSupplier);
        } else{
           // cboTransType.setNextFocusableComponent(txtAmount);
        }
        if (cboTransType.getSelectedItem().equals("Sales") || cboTransType.getSelectedItem().equals("Purchase Return")) {//9023
            txtOtherExp.setEnabled(true);
        }
    }//GEN-LAST:event_cboTransTypeFocusLost

private void txtSalesmanNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSalesmanNameActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtSalesmanNameActionPerformed

    private void tblMultiTransTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMultiTransTableMousePressed
        // TODO add your handling code here:
        selectedFlag = false;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            clearNextadd(true);
        }        
        String slno = "";
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            if (tblMultiTransTable.getRowCount() > 0) {
                slno = CommonUtil.convertObjToStr(tblMultiTransTable.getValueAt(tblMultiTransTable.getSelectedRow(), 0));
                //System.out.println("finalMap##!!???11>>>" + finalMap);
                if (finalMap != null) {
                    slno = CommonUtil.convertObjToStr(tblMultiTransTable.getValueAt(tblMultiTransTable.getSelectedRow(), 0));
                    ArrayList aList = new ArrayList();
                    IndendTO obj = (IndendTO) finalMap.get(slno);
                    aList.add(obj);
                    observable.showDatanew(aList);
                    update();
                    txtTotalPurAmt.setText(CommonUtil.convertObjToStr(tblMultiTransTable.getValueAt(tblMultiTransTable.getSelectedRow(), 4)));
                } else {
                    observable.showData(slno);
                }
                btnaddNew.setEnabled(false);
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    btnaddSave.setEnabled(true);
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
                    btnaddSave.setEnabled(false);
                }
                btnAddDelete.setEnabled(false);
            }
            ClientUtil.enableDisable(panIndeedData, false);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {

            clearNextadd(false);
            slno = CommonUtil.convertObjToStr(tblMultiTransTable.getValueAt(tblMultiTransTable.getSelectedRow(), 0));
            ArrayList aList = new ArrayList();
            //System.out.println("finalMap11@@##??>>>" + finalMap);
            IndendTO obj = (IndendTO) finalMap.get(slno);
            //System.out.println("obj" + obj);
            aList.add(obj);
            observable.showDatanew(aList);
            update();
            txtTotalPurAmt.setText(tblMultiTransTable.getValueAt(tblMultiTransTable.getSelectedRow(), 4).toString());
            btnAddDelete.setEnabled(true);
            //System.out.println("checking");
        }

        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            HashMap authEditMap = new HashMap();
            //System.out.println("iridForEdit111>>>" + iridForEdit);
            authEditMap.put("IRID", iridForEdit);
            List authEditList = ClientUtil.executeQuery("getSelectAuthorizeStatusForIndendEditAfterAuthorize", authEditMap);
            if (authEditList.size() > 0 && authEditList != null) {
                authEditMap = (HashMap) authEditList.get(0);
                if (authEditMap.get("AUTHORIZE_STATUS") != null) {
                    if (authEditMap.get("AUTHORIZE_STATUS").toString().equals("AUTHORIZED")) {
                        ClientUtil.enableDisable(panIndend, false);
                        btnSave.setEnabled(true);
                        txtPurAmount.setEnabled(true);
                        txtVatIndAmount.setEnabled(true);
                        txtOtherExp.setEnabled(false);
                        txtstIndentNo.setEnabled(false);
                        txtPurcBillNo.setEnabled(false);
                        tdtBillDate.setEnabled(false);
                        tdtSalesDate.setEnabled(false);
                        txtMiscIncome.setEnabled(false);
                        txtCommRecd.setEnabled(false);
                        btnaddSave.setEnabled(true);
                        return;
                    }
                }
            }
        }
        //System.out.println("slno444???%%%>>>" + slno);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
//            txtCommRecdFocusLost(null);
//            TableModel model = tblMultiTransTable.getModel();
//            int toRow = tblMultiTransTable.getSelectedRow();
//            System.out.println("toRow ?@@@11@@ ??>>>>" + toRow);
//            model.setValueAt(txtTotalPurAmt.getText(), toRow, 4);
//            totSum = totSum + CommonUtil.convertObjToDouble(model.getValueAt(toRow, 4));
//            txtTotAmt.setText(CommonUtil.convertObjToStr(totSum));
            btnaddNew.setEnabled(false);
            btnAddDelete.setEnabled(false);
            btnaddSave.setEnabled(true);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            btnaddNew.setEnabled(false);
            btnAddDelete.setEnabled(false);
            btnaddSave.setEnabled(false);
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            // txtDepoId.setEnabled(true);
            // txtDepoId.setEnabled(true);
            // txtStoreName.setEnabled(true);
            cboTransType.setEnabled(true);
            // txtSalesmanName.setEnabled(true);
            cboSupplier.setEnabled(true);
            txtAmount.setEnabled(true);
            txtVatAmt.setEnabled(true);
            txtOtherExp.setEnabled(true);
            btnDepo.setEnabled(true);
            txtNarration.setEnabled(true);
        }
//        txtTransAmt.setText(CommonUtil.convertObjToStr(tblMultiTransTable.getValueAt(tblMultiTransTable.getSelectedRow(), 3)));
    }//GEN-LAST:event_tblMultiTransTableMousePressed

    private void txtTotAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotAmtFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotAmtFocusLost

    private void btnaddSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddSaveActionPerformed
        // TODO add your handling code here:
        String item = CommonUtil.convertObjToStr(cboTransType.getSelectedItem().toString());
        if (tblMultiTransTable.getRowCount() > 0) {
            for (int i = 0; i < tblMultiTransTable.getRowCount(); i++) {
                if (!item.equals(tblMultiTransTable.getValueAt(i, 1))) {
                    //System.out.println("item####" + item);
                    //System.out.println("tblMultiTransTablegetValueAt####" + tblMultiTransTable.getValueAt(i, 1));
                    ClientUtil.showAlertWindow("Multiple transaction not allowed for different Inded Types!!");
                    return;
                }
            }
        }
        if (txtDepoId.getText().equals("") || txtDepoId.getText() == null) {
            ClientUtil.showAlertWindow("Please enter  DEPO ID");
            return;
        }
        //String item = CommonUtil.convertObjToStr(cboTransType.getSelectedItem().toString());

        //Added by Jeffin John on 27-05-2014 for Mantis ID - 9161
        if (item.equals("") || item == null) {
            ClientUtil.showAlertWindow("Please select Trans Type");
            return;
        }

        //System.out.println("item ---- " + item);
        if (item != null && item.equals("Purchase")) {
            double purAmt = CommonUtil.convertObjToDouble(txtPurAmount.getText());
            //System.out.println("purAmt ---- " + purAmt);
            if (purAmt == 0 || purAmt == 0.00) {
                displayAlert("Enter Liability Amount!!!");
                return;
            }
        }
        HashMap depoHeads = new HashMap();
        depoHeads.put("DEPID", CommonUtil.convertObjToStr(txtDepoId.getText()));
        List suppList = ClientUtil.executeQuery("Indend.getAcHeads", depoHeads);
        depoHeads = new HashMap();

        if (suppList != null && suppList.size() > 0) {
            //System.out.println("depoHeads>>>" + suppList);
            depoHeads = (HashMap) suppList.get(0);
            if (item != null && item.equals("Purchase")) {
                String purHead = CommonUtil.convertObjToStr(depoHeads.get("PURCHASE_AC_HD_ID"));
                if (purHead == null || purHead.equals("")) {
                    displayAlert("Purchase Account head not set properly...");
                    return;
                }
            }
            if (item != null && item.equals("Purchase Return")) {
                String purRetHead = CommonUtil.convertObjToStr(depoHeads.get("PUR_RET_AC_HD_ID"));
                if (purRetHead == null || purRetHead.equals("")) {
                    displayAlert("Purchase Return Account head not set properly...");
                    return;
                }
            }
            if (item != null && item.equals("Sales")) {
                String salesHead = CommonUtil.convertObjToStr(depoHeads.get("SALES_AC_HD_ID"));
                if (salesHead == null || salesHead.equals("")) {
                    displayAlert("Sales Account head not set properly...");
                    return;
                }
            }
            if (item != null && item.equals("Sales Return")) {
                String salesRetHead = CommonUtil.convertObjToStr(depoHeads.get("SALES_RET_AC_HD_ID"));
                if (salesRetHead == null || salesRetHead.equals("")) {
                    displayAlert("Sales return Account head not set properly...");
                    return;
                }
            }
            if (item != null && item.equals("Damage")) {
                String damageHead = CommonUtil.convertObjToStr(depoHeads.get("DAMAGE_AC_HD_ID"));
                if (damageHead == null || damageHead.equals("")) {
                    displayAlert("Damage Account head not set properly...");
                    return;
                }
            }
            
            // Added by nithya on 04-04-2020 for KD-1732
            if (item != null && item.equals("Discount")) {
                String damageHead = CommonUtil.convertObjToStr(depoHeads.get("DISCOUNT_AC_HD_ID"));
                if (damageHead == null || damageHead.equals("")) {
                    displayAlert("Discount Account head not set properly...");
                    return;
                }
            }
            
            // End
            
            if (item != null && item.equals("Deficite")) {
                String deficiteHead = CommonUtil.convertObjToStr(depoHeads.get("DEFICIATE_HC_HD_ID"));
                if (deficiteHead == null || deficiteHead.equals("")) {
                    displayAlert("Deficite Account head not set properly...");
                    return;
                }
            }

        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {

            //Added by Jeffin John on 27-05-2014 fpr Mantis ID - 9161

            if ((txtAmount.getText().equals("") || txtAmount.getText() == null
                    || txtDepoId.getText().equals("") || txtDepoId.getText() == null
                    || txtVatAmt.getText().equals("") || txtVatAmt.getText() == null
                    || txtVatIndAmount.getText().equals("") || txtVatIndAmount.getText() == null
                    || txtstIndentNo.getText().equals("") || txtstIndentNo.getText() == null
                    || txtMiscIncome.getText().equals("") || txtMiscIncome.getText() == null) && cboTransType.getSelectedItem().equals("Damage")) {
                ClientUtil.showAlertWindow("Please enter all fields");
                return;
            }


            if ((txtAmount.getText().equals("") || txtAmount.getText() == null
                    || txtDepoId.getText().equals("") || txtDepoId.getText() == null
                    || txtVatAmt.getText().equals("") || txtVatAmt.getText() == null
                    || txtVatIndAmount.getText().equals("") || txtVatIndAmount.getText() == null
                    || txtstIndentNo.getText().equals("") || txtstIndentNo.getText() == null
                    || txtMiscIncome.getText().equals("") || txtMiscIncome.getText() == null) && cboTransType.getSelectedItem().equals("Deficite")) {
                ClientUtil.showAlertWindow("Please enter all fields");
                return;
            }

            if ((txtAmount.getText().equals("") || txtAmount.getText() == null
                    || txtDepoId.getText().equals("") || txtDepoId.getText() == null
                    || txtVatAmt.getText().equals("") || txtVatAmt.getText() == null
                    || txtPurAmount.getText().equals("") || txtPurAmount.getText() == null
                    || txtVatIndAmount.getText().equals("") || txtVatIndAmount.getText() == null
                    || txtstIndentNo.getText().equals("") || txtstIndentNo.getText() == null
                    || txtMiscIncome.getText().equals("") || txtMiscIncome.getText() == null) && cboTransType.getSelectedItem().equals("Purchase Return")) {
                ClientUtil.showAlertWindow("Please enter all fields");
                return;
            }

            if ((txtAmount.getText().equals("") || txtAmount.getText() == null
                    || txtDepoId.getText().equals("") || txtDepoId.getText() == null
                    || txtVatAmt.getText().equals("") || txtVatAmt.getText() == null
                    || txtMiscIncome.getText().equals("") || txtMiscIncome.getText() == null) && cboTransType.getSelectedItem().equals("Sales")) {
                ClientUtil.showAlertWindow("Please enter all fields");
                return;
            }

            if ((txtAmount.getText().equals("") || txtAmount.getText() == null
                    || txtDepoId.getText().equals("") || txtDepoId.getText() == null
                    || txtVatAmt.getText().equals("") || txtVatAmt.getText() == null
                    || txtMiscIncome.getText().equals("") || txtMiscIncome.getText() == null
                    || txtCommRecd.getText().equals("") || txtCommRecd.getText() == null) && cboTransType.getSelectedItem().equals("Sales Return")) {
                ClientUtil.showAlertWindow("Please enter all fields");
                return;
            }

            //End of updation by Jeffin John

            if ((txtAmount.getText().equals("") || txtAmount.getText() == null
                    || txtDepoId.getText().equals("") || txtDepoId.getText() == null
                    || txtVatAmt.getText().equals("") || txtVatAmt.getText() == null
                    || txtPurAmount.getText().equals("") || txtPurAmount.getText() == null
                    || txtVatIndAmount.getText().equals("") || txtVatIndAmount.getText() == null
                    || txtstIndentNo.getText().equals("") || txtstIndentNo.getText() == null
                    || // txtPurcBillNo.getText().equals("") || txtPurcBillNo.getText()==null
                    //tdtBillDate.getDateValue().equals("") || tdtBillDate.getDateValue()==null || 
                    txtMiscIncome.getText().equals("") || txtMiscIncome.getText() == null
                    || txtCommRecd.getText().equals("") || txtCommRecd.getText() == null) && cboTransType.getSelectedItem().equals("Purchase")) {
                ClientUtil.showAlertWindow("Please enter all fields");
                return;
            }
        }
        int selRow = 0;
        double tot = 0.0;
        //System.out.println("slno222???%%%>>>" + slno);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (finalMap != null && tblMultiTransTable.getRowCount() > 0) {
//            IndendTO objTo = (IndendTO)finalMap.get(CommonUtil.convertObjToStr(tblMultiTransTable.getValueAt(tblMultiTransTable.getSelectedRow(), 0)));
//                System.out.println("objTo??>>>@@>>>"+objTo);
//                System.out.println("txtPurAmount.getText()>>>"+txtPurAmount.getText()+"txtVatIndAmount.getText()>>>"+txtVatIndAmount.getText());
//            objTo.setPurAmount(CommonUtil.convertObjToDouble(txtPurAmount.getText()));
//            objTo.setVatIndAmt(CommonUtil.convertObjToDouble(txtVatIndAmount.getText()));
                if (tblMultiTransTable.getSelectedRow() != -1) {
                    TableModel model = tblMultiTransTable.getModel();
                    int toRow = tblMultiTransTable.getSelectedRow();
                    System.out.println("toRow ? ??>>>>" + toRow);
                    model.setValueAt(txtTotalPurAmt.getText(), toRow, 4);
                    model.setValueAt(txtPurAmount.getText(), toRow, 5);//Added by nithya on 28-01-2020 for KD-1342
                }
//                for(int i=0;i<tblMultiTransTable.getRowCount();i++){
//                   tot = tot + CommonUtil.convertObjToDouble(tblMultiTransTable.getValueAt(i, 4)); 
//                }
//                txtTotAmt.setText(CommonUtil.convertObjToStr(tot));
//                transactionUI.setCallingAmount(txtTotAmt.getText());
                getStoreName(txtDepoId.getText().toString());
                updateOBFields();
                String action = CommonConstants.TOSTATUS_UPDATE;
                IndendTO objTo = observable.geIndendTO(action);
                if (tblMultiTransTable.getSelectedRow() != -1) {
                    finalMap.put(String.valueOf(tblMultiTransTable.getValueAt(tblMultiTransTable.getSelectedRow(), 0)), objTo);
                }
                calcTotal();
                setParticulars(objTo);
            }
//                updateOBFields();
//                String action = CommonConstants.TOSTATUS_UPDATE;
//                IndendTO obj = observable.geIndendTO(action);
//                System.out.println("finalMap1111@@####>>>>" + finalMap);
//                if (finalMap == null) {
//                    System.out.println("innn@@@##?");
//                    finalMap = new HashMap();
//                }
//                
//                for(int i=0;i< tblMultiTransTable.getRowCount();i++) {
//                    System.out.println("obj.getSlNo()>>>"+obj.getSlNo());
//                    System.out.println("tblMultiTransTable.getValueAt(tblMultiTransTable.getSelectedRow(), 0)>>>"+tblMultiTransTable.getValueAt(tblMultiTransTable.getSelectedRow(), 0));
//                    if((obj.getSlNo()).equals(CommonUtil.convertObjToStr(tblMultiTransTable.getValueAt(tblMultiTransTable.getSelectedRow(), 0))))
//                    {
//                   String slno1 = obj.getSlNo();
//                        System.out.println("slno1??@#>>>>"+slno1);
//                        System.out.println("obj3??@#>>>>"+obj);
//                    finalMap.put(String.valueOf(slno1), obj);
//                    }
//                }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (tblMultiTransTable.getSelectedRow() != -1) {
//             updateOBFields();
                    TableModel model = tblMultiTransTable.getModel();
                    int toRow = tblMultiTransTable.getSelectedRow();
                    //System.out.println("toRow ? innn??>>>>" + toRow);
                    //System.out.println("txtTotalPurAmt.getText()@###>>>" + txtTotalPurAmt.getText());
                    //model.setValueAt(CommonUtil.convertObjToDouble(txtTotalPurAmt.getText()), toRow, 4); // Commented and added below line of code by nithya [ To solve illegal argument exception indend edit and change liability amount] 
                    model.setValueAt(txtTotalPurAmt.getText(), toRow, 4);
                    model.setValueAt(txtPurAmount.getText(), toRow, 5);//Added by nithya on 28-01-2020 for KD-1342
                }
            }
            clearNextadd(false);
//                txtPurAmount.setEnabled(true);
//                txtVatIndAmount.setEnabled(true);

        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            //System.out.println("selectedFlag#############" + selectedFlag);
            if (selectedFlag == true) {
                if (finalMap != null && tblMultiTransTable.getRowCount() > 0) {
//            IndendTO objTo = (IndendTO)finalMap.get(CommonUtil.convertObjToStr(tblMultiTransTable.getValueAt(tblMultiTransTable.getSelectedRow(), 0)));
//                System.out.println("objTo??>>>@@>>>"+objTo);
//                System.out.println("txtPurAmount.getText()>>>"+txtPurAmount.getText()+"txtVatIndAmount.getText()>>>"+txtVatIndAmount.getText());
//            objTo.setPurAmount(CommonUtil.convertObjToDouble(txtPurAmount.getText()));
//            objTo.setVatIndAmt(CommonUtil.convertObjToDouble(txtVatIndAmount.getText()));
                    if (tblMultiTransTable.getSelectedRow() != -1) {
                        TableModel model = tblMultiTransTable.getModel();
                        int toRow = tblMultiTransTable.getSelectedRow();
                        //System.out.println("toRow ? ??>>>>" + toRow);
                        model.setValueAt(txtTotalPurAmt.getText(), toRow, 4);
                        model.setValueAt(txtPurAmount.getText(), toRow, 5);//Added by nithya on 28-01-2020 for KD-1342
                    }
//                for(int i=0;i<tblMultiTransTable.getRowCount();i++){
//                   tot = tot + CommonUtil.convertObjToDouble(tblMultiTransTable.getValueAt(i, 4)); 
//                }
//                txtTotAmt.setText(CommonUtil.convertObjToStr(tot));
//                transactionUI.setCallingAmount(txtTotAmt.getText());
                    getStoreName(txtDepoId.getText().toString());
                    updateOBFields();
                    String action = CommonConstants.TOSTATUS_INSERT;
                    IndendTO objTo = observable.geIndendTO(action);
                    if (tblMultiTransTable.getSelectedRow() != -1) {
                        finalMap.put(String.valueOf(tblMultiTransTable.getValueAt(tblMultiTransTable.getSelectedRow(), 0)), objTo);
                    }
                    //System.out.println("finalMap#############" + finalMap);
                    calcTotal();
                    setParticulars(objTo);
                }
                selectedFlag = false;
            } else {

                if ((CommonUtil.convertObjToDouble(txtAmount.getText())) > 0.0) {
                    HashMap transMap = new HashMap();
                    ArrayList newList = new ArrayList();
                    newList.add(cboTransType.getSelectedItem().toString());
                    newList.add(txtAmount.getText());
                    if (cboTransType.getSelectedItem().equals("Sales Return") || cboTransType.getSelectedItem().equals("Sales")) {
                        newList.add(txtSalesmanName.getText());
                    } else if (cboTransType.getSelectedItem().equals("Purchase Return") || cboTransType.getSelectedItem().equals("Purchase")) {
                        newList.add(cboSupplier.getSelectedItem().toString());
                    } else {
                        newList.add("");
                    }                    
                    newList.add(txtTotalPurAmt.getText());
                    newList.add(txtPurAmount.getText());//Added by nithya on 28-01-2020 for KD-1342
                    if (tableList == null) {
                        tableList = new ArrayList();
                    }
                    //System.out.println("slno??>>>#@#@>>" + slno);
                    //System.out.println("tableList$@#111??>>##>>>" + tableList);
                    //System.out.println("selectedFlag111>>>" + selectedFlag);
                    if (selectedFlag == true && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                        //System.out.println("tblMultiTransTable.getSelectedRow()>>>" + tblMultiTransTable.getSelectedRow());
                        selRow = tblMultiTransTable.getSelectedRow();
                        tableList.remove(tblMultiTransTable.getSelectedRow());

                    }
                    //System.out.println("tableList$@#222??>>##>>>" + tableList);
                    //System.out.println("newList??>>##>>>" + newList);
                    tableList.add(newList);
                    //System.out.println("tableList??>>##>>>" + tableList);
                    observable.insertTable(tableList);
                    tblMultiTransTable.setModel(observable.getTblCheckBookTable());
                    updateOBFields();
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                        String action = CommonConstants.TOSTATUS_INSERT;
                        IndendTO obj = observable.geIndendTO(action);
                        if (finalMap == null) {
                            finalMap = new HashMap();
                        }
                        if (slno < tblMultiTransTable.getRowCount()) {
                            slno = slno + 1;
                            finalMap.put(String.valueOf(slno), obj);
                        } else if (selectedFlag == true && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                            finalMap.put(String.valueOf(selRow + 1), obj);
                            selectedFlag = false;
                        }
                        setParticulars(obj);
                    }
                    calcTotal();
                    //setParticulars(obj);

                }
                selectedFlag = false;
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                clearNextadd(false);
            } else {
                clearNextadd(true);
                txtPurAmount.setEnabled(true);
                txtVatIndAmount.setEnabled(true);
            }
        }
    }//GEN-LAST:event_btnaddSaveActionPerformed

    private void clearNextadd(boolean s) {
        //   changeFieldNamesAccordingTOBehavesAndRdo();
        txtDepoId.setText("");
        txtStoreName.setText("");
        cboTransType.setSelectedItem("");
        txtSalesmanName.setText("");
        cboSupplier.setSelectedItem("");
        txtTinNo.setText("");
        txtAmount.setText("");
        txtVatAmt.setText("");
        txtGSTAmt.setText("");// Added by nithya on 27-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction
        txtRoundOff.setText("");
        txtOtherExp.setText("");
        txtPurAmount.setText("");
        txtVatIndAmount.setText("");
        txtstIndentNo.setText("");
        txtPurcBillNo.setText("");
        tdtBillDate.setDateValue("");
        tdtSalesDate.setDateValue("");
        txtMiscIncome.setText("");
        txtOtherIncome.setText("");
        txtCommRecd.setText("");
        txtTotalPurAmt.setText("");
        txtNarration.setText("");
        txtDepoId.setEnabled(s);
        txtStoreName.setEnabled(s);
        cboTransType.setEnabled(s);
        txtSalesmanName.setEnabled(s);
        cboSupplier.setEnabled(s);
        txtTinNo.setEnabled(s);
        txtAmount.setEnabled(s);
        txtVatAmt.setEnabled(s);
        txtOtherExp.setEnabled(s);
        txtPurAmount.setEnabled(s);
        txtVatIndAmount.setEnabled(s);
        txtstIndentNo.setEnabled(s);
        txtPurcBillNo.setEnabled(s);
        tdtBillDate.setEnabled(s);
        tdtSalesDate.setEnabled(s);
        txtMiscIncome.setEnabled(s);
        //txtOtherIncome.setEnabled(s);
        txtCommRecd.setEnabled(s);
        txtTotalPurAmt.setEnabled(s);
        txtNarration.setEnabled(s);
        btnDepo.setEnabled(s);
        txtStoreName.setEnabled(false);
        txtSalesmanName.setEnabled(false);
        cboSupplier.setEnabled(false);
        txtTinNo.setEnabled(false);
        btnaddSave.setEnabled(true);
    }

    public void setParticulars(IndendTO objTo){        
        String particulars = "";
        String billDate = DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTo.getBillDate())));
        String saleDate = DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTo.getSalesDate())));
        if (objTo.getTransType().equals("Sales") || objTo.getTransType().equals("Sales Return")) {
            particulars = "Sale Dated : " +saleDate;    
        }else if(objTo.getTransType().equals("Purchase") || objTo.getTransType().equals("Purchase Return")) {
            particulars = "By "+objTo.getPurchBillNo()+" "+billDate;                  
        }
        transactionUI.setCallingParticulars(particulars);
    }
    
    public String getSupplierName(String supplierId) {
        HashMap supMap = new HashMap();
        String supplierName = "";
        supMap.put("SUPPLY_ID", supplierId);
        supMap = (HashMap) (ClientUtil.executeQuery("getIndendSupplier", supMap)).get(0);
        if (supMap != null && supMap.size() > 0) {
            supplierName = CommonUtil.convertObjToStr(supMap.get("NAME"));
        }  
        return supplierName;
    }
    
    private void calcTotal() {
        double total = 0.0;
        double creditTotal = 0.0;
        double debitTotal = 0.0;
        String transType = "";
        double crAmt = 0, dbAmt = 0, returnTotalAmt = 0;
        try {
            if (tblMultiTransTable.getRowCount() > 0) {
                for (int i = 0; i < tblMultiTransTable.getRowCount(); i++) {
                    transType = CommonUtil.convertObjToStr(tblMultiTransTable.getValueAt(i, 1));
                    if (transType.equals("Damage") || transType.equals("Deficite") || transType.equals("Sales") || transType.equals("Purchase Return") || transType.equals("Discount")) {
                        debitTotal += CommonUtil.convertObjToDouble(tblMultiTransTable.getValueAt(i, 4));
                    } else if (transType.equals("Sales Return") || transType.equals("Purchase")) {
                        creditTotal += CommonUtil.convertObjToDouble(tblMultiTransTable.getValueAt(i, 4));
                    }
                    if (debitTotal > creditTotal) {
                        total = debitTotal - creditTotal;
                        observable.setDebitOrCredit("DEBIT");
                    } else {
                        observable.setDebitOrCredit("CREDIT");
                        total = creditTotal - debitTotal;
                    }
                    long dr = roundOff((long) (total * 1000));
                    total = dr / 100.0;
                    transactionUI.setCallingAmount(CommonUtil.convertObjToStr(tblMultiTransTable.getValueAt(i, 4)));
                }
            }
            if (total == 0.0) {
                observable.setTxtGenTrans("NO");
                gen_trans = "NO";
            }
            txtTotAmt.setText(String.valueOf(new Double(total).doubleValue()));
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private long roundOff(long amt) {
        long amount = amt / 10;
//        int lastDigit = (int)amt%10;
        int lastDigit = (int) (amt % 10);  //() brackets added because sometimes returns 8 if 0 also.
        if (lastDigit > 5) {
            amount++;
        }
        return amount;
    }
    private void btnAddDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDeleteActionPerformed
        // TODO add your handling code here:
        int s = -1;
        s = tblMultiTransTable.getSelectedRow();
        if (s != -1) {
            tableList.remove(s);
            //finalMap.remove(tblMultiTransTable.getValueAt(s, 2));
            String key = CommonUtil.convertObjToStr(s+1);
            //finalMap.remove(s + 1);
            finalMap.remove(key);
            observable.insertTable(tableList);
            tblMultiTransTable.setModel(observable.getTblCheckBookTable());
            
            //Arrange the HashMap values, code starts here. made by Kannan AR
            int num = 0;
            HashMap tempMap = new HashMap();
            Iterator it = finalMap.keySet().iterator();
            ArrayList keylist = new ArrayList();
            while (it.hasNext()) {
                keylist.add(it.next());
            }
            Collections.sort(keylist);
            for (int i = 0; i < keylist.size(); i++) {
                num++;
                IndendTO indentTO = (IndendTO) finalMap.get(CommonUtil.convertObjToStr(keylist.get(i)));
                indentTO.setSlNo(num);
                tempMap.put(CommonUtil.convertObjToStr(num), indentTO);
            }
            finalMap = tempMap;
            //Arrange the HashMap values, code ends here

            calcTotal();
            clearNextadd(false);
        } else {
            ClientUtil.showAlertWindow("Please Select a row");
        }
    }//GEN-LAST:event_btnAddDeleteActionPerformed

    private void btnaddNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddNewActionPerformed
        // TODO add your handling code here:
        if ((CommonUtil.convertObjToDouble(txtTotalPurAmt.getText()) > 0)) {
            ClientUtil.showAlertWindow("Please Save the Data First");
            return;
        } else {
            clearNextadd(true);
            //  changeFieldNamesAccordingTOBehavesAndRdo();
        }
    }//GEN-LAST:event_btnaddNewActionPerformed

    private void tblMultiTransTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMultiTransTableMouseClicked
        // TODO add your handling code here:
        selectedFlag = true;
//        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW)
//            clearNextadd(true);
    }//GEN-LAST:event_tblMultiTransTableMouseClicked

private void tblMultiTransTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMultiTransTableMouseReleased
// TODO add your handling code here:
    selectedFlag = false;
}//GEN-LAST:event_tblMultiTransTableMouseReleased

private void txtTotAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotAmtActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtTotAmtActionPerformed

private void txtPurcBillNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurcBillNoFocusLost
// TODO add your handling code here:
   // txtPurcBillNo.setNextFocusableComponent(tdtBillDate);
   
    if (txtPurcBillNo.getText().length() > 0 && cboTransType.getSelectedItem().toString().equals("Purchase")) {
        HashMap countMap = new HashMap();      
        countMap.put("DEPID", txtDepoId.getText());
        countMap.put("SUPPLIER_ID", observable.getCbmSupplierName().getKeyForSelected());
        countMap.put("PURCH_BILL_NO", txtPurcBillNo.getText());
        countMap.put("TRAN_DATE", currDt.clone());
        List countList = ClientUtil.executeQuery("getPruchaseBillCount", countMap);
        if(countList != null && countList.size() > 0){
            countMap = (HashMap)countList.get(0);
            if(countMap.containsKey("BILL_CNT") && countMap.get("BILL_CNT") != null && CommonUtil.convertObjToDouble(countMap.get("BILL_CNT")) > 0){
                ClientUtil.showMessageWindow("Bill No Already Exists !!!");
            }
        }
    }
   
   
}//GEN-LAST:event_txtPurcBillNoFocusLost

private void tdtBillDateMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdtBillDateMouseReleased
// TODO add your handling code here:
 //   tdtBillDate.setNextFocusableComponent(txtAmount);
}//GEN-LAST:event_tdtBillDateMouseReleased

private void tdtSalesDateMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdtSalesDateMouseReleased
// TODO add your handling code here:
  //  tdtSalesDate.setNextFocusableComponent(txtMiscIncome);
}//GEN-LAST:event_tdtSalesDateMouseReleased

private void txtstIndentNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtstIndentNoFocusLost
// TODO add your handling code here:
  //  txtstIndentNo.setNextFocusableComponent(txtPurAmount);
}//GEN-LAST:event_txtstIndentNoFocusLost

private void txtPurAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurAmountFocusLost
// TODO add your handling code here:
   // txtPurAmount.setNextFocusableComponent(txtVatIndAmount);
}//GEN-LAST:event_txtPurAmountFocusLost

private void txtVatIndAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtVatIndAmountFocusLost
// TODO add your handling code here:
   // txtVatIndAmount.setNextFocusableComponent(btnaddSave);
}//GEN-LAST:event_txtVatIndAmountFocusLost

    private void txtNarrationFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNarrationFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNarrationFocusLost

private void txtOtherIncomeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOtherIncomeFocusLost
// TODO add your handling code here:
    setTransactionAmount();
    resetTranAmount();
}//GEN-LAST:event_txtOtherIncomeFocusLost

    private void txtGSTAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGSTAmtFocusLost
        // TODO add your handling code here:
        // Added by nithya on 27-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction
        setTransactionAmount();
        resetTranAmount();
        //txtGSTAmt.setNextFocusableComponent(txtPurAmount);
        txtGSTAmt.setNextFocusableComponent(txtRoundOff);
    }//GEN-LAST:event_txtGSTAmtFocusLost

    private void txtRoundOffFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRoundOffFocusLost
        // TODO add your handling code here:
        setTransactionAmount();
        resetTranAmount();
        txtRoundOff.setNextFocusableComponent(txtPurAmount);
    }//GEN-LAST:event_txtRoundOffFocusLost

    private void changeFieldNamesAccordingTOBehavesAndRdo() {
        observable.resetForm();
        txtStoreName.setEnabled(false);
        txtStoreName.setText("");
        tdtDateIndand.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
        txtSalesmanName.setText("");
        txtPurAmount.setText("");
        txtDepoId.setEnabled(false);
        txtDepoId.setText("");

    }

    public void setTransactionAmount() {
        double totAmount = 0.0;
        double misc = 0.0;
        if (txtAmount.getText() == null) {
            txtAmount.setText("0.0");
        }
        if (txtVatAmt.getText() == null) {
            txtVatAmt.setText("0.0");
        }
        if (txtGSTAmt.getText() == null) {
            txtGSTAmt.setText("0.0");
        }
        
        if (txtRoundOff.getText() == null) {
            txtRoundOff.setText("0.0");
        }
        
        if (txtOtherExp.getText() == null) {
            txtOtherExp.setText("0.0");
        }
        if (txtMiscIncome.getText() == null) {
            txtMiscIncome.setText("0.0");
        } 
        if (txtOtherIncome.getText() == null) {
            txtOtherIncome.setText("0.0");
        }else {
             // Added purchase return to the first block along with sales  by nithya on 05-02-2018 for 0009023: Requirement in indend
            if (cboTransType.getSelectedItem().toString().equals("Sales") || cboTransType.getSelectedItem().toString().equals("Purchase Return")) {
                misc = -1 * (CommonUtil.convertObjToDouble(txtMiscIncome.getText())+CommonUtil.convertObjToDouble(txtOtherIncome.getText()));
            } else {
                misc = (CommonUtil.convertObjToDouble(txtMiscIncome.getText())+CommonUtil.convertObjToDouble(txtOtherIncome.getText()));
            }
        }
        if (txtCommRecd.getText() == null) {
            txtCommRecd.setText("0.0");
        }
        totAmount = (CommonUtil.convertObjToDouble(txtAmount.getText()) + CommonUtil.convertObjToDouble(txtVatAmt.getText())+CommonUtil.convertObjToDouble(txtGSTAmt.getText()) + CommonUtil.convertObjToDouble(txtRoundOff.getText())
                + CommonUtil.convertObjToDouble(txtOtherExp.getText())) - (misc + CommonUtil.convertObjToDouble(txtCommRecd.getText()));
        
        if(cboTransType.getSelectedItem().toString().equals("Sales") || cboTransType.getSelectedItem().toString().equals("Purchase Return")){//9032
            totAmount = (CommonUtil.convertObjToDouble(txtAmount.getText()) + CommonUtil.convertObjToDouble(txtVatAmt.getText()) + CommonUtil.convertObjToDouble(txtGSTAmt.getText()) + CommonUtil.convertObjToDouble(txtRoundOff.getText())
                - CommonUtil.convertObjToDouble(txtOtherExp.getText())) - (misc + CommonUtil.convertObjToDouble(txtCommRecd.getText()));
        }
        //transactionUI.setCallingAmount(CommonUtil.convertObjToStr(totAmount));
        if (cboSupplier.getSelectedItem() != null) {
            transactionUI.setCallingApplicantName(CommonUtil.convertObjToStr(cboSupplier.getSelectedItem()));
        }
        if (cboTransType.getSelectedItem().equals("Sales Return") || cboTransType.getSelectedItem().equals("Sales")) {
            transactionUI.setCallingApplicantName(CommonUtil.convertObjToStr(txtSalesmanName.getText()));
        }
        txtTotalPurAmt.setText(CommonUtil.convertObjToStr(totAmount));
        txtTotalPurAmt.setEnabled(false);
    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtDepoId", new Boolean(true));
        mandatoryMap.put("tdtDateIndand", new Boolean(true));
        //  mandatoryMap.put("txtBorrowingNo", new Boolean(true));
        mandatoryMap.put("cboTransType", new Boolean(true));
        mandatoryMap.put("txtPurAmount", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(true));


    }

    public java.util.HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    public String getDtPrintValue(String strDate) {
        try {
            //create SimpleDateFormat object with source string date format
            SimpleDateFormat sdfSource = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            //parse the string into Date object
            Date date = sdfSource.parse(strDate);
            //create SimpleDateFormat object with desired date format
            SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
            //parse the date into another format
            strDate = sdfDestination.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }
    /* Auto Generated Method - update()
    This method called by Observable. It updates the UI with
    Observable's data. If needed add/Remove RadioButtons
    method need to be added.*/

    public void update() {
        // System.out.println("INNNN  QQQQQQQQQQQQQ========"+observable.getTdtDateIndand());
        if (observable.getTdtDateIndand() != null) {
            tdtDateIndand.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtDateIndand())));
        }
        if (observable.getTxtDepoId() != null && !observable.getTxtDepoId().equals("")) {
            txtDepoId.setText(observable.getTxtDepoId());
        }
        if (observable.getCboSupplier() != null && !observable.getCboSupplier().equals("")) {
            cboSupplier.setSelectedItem((observable.getCboSupplier()).toString());
        }
        if (observable.getTxtStoreName() != null && !observable.getTxtStoreName().equals("")) {
            txtStoreName.setText(observable.getTxtStoreName());
        }
        if (observable.getTxtPurAmount() != null) {
            txtPurAmount.setText(String.valueOf(observable.getTxtPurAmount()));
        }
        if (observable.getTxtAmount() != null) {
            txtAmount.setText(String.valueOf(observable.getTxtAmount()));
        }
        txtStoreName.setEnabled(false);

        if (observable.getTxtGenTrans() != null && !observable.getTxtGenTrans().equals("")) {
            gen_trans = observable.getTxtGenTrans();
        }


        if (observable.getTxtVatAmt() != null && !observable.getTxtVatAmt().equals("")) {
            txtVatAmt.setText(String.valueOf(observable.getTxtVatAmt()));
        }
        if (observable.getTxtVatIndAmt() != null && !observable.getTxtVatIndAmt().equals("")) {
            txtVatIndAmount.setText(String.valueOf(observable.getTxtVatIndAmt()));
        }
        if (observable.getTxtMiscAmt() != null && !observable.getTxtMiscAmt().equals("")) {
            txtMiscIncome.setText(String.valueOf(observable.getTxtMiscAmt()));
        }
        if (observable.getTxtOthrExpAmt() != null && !observable.getTxtOthrExpAmt().equals("")) {
            txtOtherExp.setText(String.valueOf(observable.getTxtOthrExpAmt()));
        }
        if (observable.getTxtCommRecvdAmt() != null && !observable.getTxtCommRecvdAmt().equals("")) {
            txtCommRecd.setText(String.valueOf(observable.getTxtCommRecvdAmt()));
        }
        if (observable.getCboTransType() != null && !observable.getCboTransType().equals("")) {
            cboTransType.setSelectedItem(String.valueOf(observable.getCboTransType()));
        }
        if (observable.getTxtPurcBillNo() != null && !observable.getTxtPurcBillNo().equals("")) {
            txtPurcBillNo.setText(observable.getTxtPurcBillNo());
        }

        if (observable.getTxtTinNo() != null && !observable.getTxtTinNo().equals("")) {
            txtTinNo.setText(observable.getTxtTinNo());
        }
        if (observable.getTdtSalesDate() != null && !observable.getTdtSalesDate().equals("")) {
            tdtSalesDate.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtSalesDate())));
        }
        if (observable.getTdtBillDate() != null && !observable.getTdtBillDate().equals("")) {
            tdtBillDate.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtBillDate())));
        }
        if (observable.getTxtNarration() != null && !observable.getTxtNarration().equals("")) {
            txtNarration.setText(observable.getTxtNarration());
        }
        if (observable.getTxtstIndentNo() != null && !observable.getTxtstIndentNo().equals("")) {
            txtstIndentNo.setText(observable.getTxtstIndentNo());
        }
        if (observable.getTxtCommRecvdAmt() != null && !observable.getTxtCommRecvdAmt().equals("")) {
            txtCommRecd.setText(CommonUtil.convertObjToStr(observable.getTxtCommRecvdAmt()));
        }
        if (observable.getTxtVatAmt() != null && !observable.getTxtVatAmt().equals("")) {
            txtVatAmt.setText(CommonUtil.convertObjToStr(observable.getTxtVatAmt()));
        }
        if (observable.getTxtMiscAmt() != null && !observable.getTxtMiscAmt().equals("")) {
            txtMiscIncome.setText(CommonUtil.convertObjToStr(observable.getTxtMiscAmt()));
        }
        if (observable.getTxtOthrExpAmt() != null && !observable.getTxtOthrExpAmt().equals("")) {
            txtOtherExp.setText(CommonUtil.convertObjToStr(observable.getTxtOthrExpAmt()));
        }
        if (observable.getTxtVatIndAmt() != null && !observable.getTxtVatIndAmt().equals("")) {
            txtVatIndAmount.setText(CommonUtil.convertObjToStr(observable.getTxtVatIndAmt()));
        }
        if (observable.getTxtOtherIncome() != null && !observable.getTxtOtherIncome().equals("")) {
            txtOtherIncome.setText(CommonUtil.convertObjToStr(observable.getTxtOtherIncome()));
        }
    }

    public void update(Observable observed, Object arg) {
                if (observable.getTdtDateIndand() != null) {
            tdtDateIndand.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtDateIndand())));
        }
        if (observable.getTxtDepoId() != null && !observable.getTxtDepoId().equals("")) {
            txtDepoId.setText(observable.getTxtDepoId());
        }
        if (observable.getCboSupplier() != null && !observable.getCboSupplier().equals("")) {
            cboSupplier.setSelectedItem((observable.getCboSupplier()).toString());
        }
        if (observable.getTxtStoreName() != null && !observable.getTxtStoreName().equals("")) {
            txtStoreName.setText(observable.getTxtStoreName());
        }
        if (observable.getTxtPurAmount() != null) {
            txtPurAmount.setText(String.valueOf(observable.getTxtPurAmount()));
        }
        if (observable.getTxtAmount() != null) {
            txtAmount.setText(String.valueOf(observable.getTxtAmount()));
        }
        txtStoreName.setEnabled(false);

        if (observable.getTxtGenTrans() != null && !observable.getTxtGenTrans().equals("")) {
            gen_trans = observable.getTxtGenTrans();
        }


        if (observable.getTxtVatAmt() != null && !observable.getTxtVatAmt().equals("")) {
            txtVatAmt.setText(String.valueOf(observable.getTxtVatAmt()));
        }
        if (observable.getTxtVatIndAmt() != null && !observable.getTxtVatIndAmt().equals("")) {
            txtVatIndAmount.setText(String.valueOf(observable.getTxtVatIndAmt()));
        }
        if (observable.getTxtMiscAmt() != null && !observable.getTxtMiscAmt().equals("")) {
            txtMiscIncome.setText(String.valueOf(observable.getTxtMiscAmt()));
        }
        if (observable.getTxtOthrExpAmt() != null && !observable.getTxtOthrExpAmt().equals("")) {
            txtOtherExp.setText(String.valueOf(observable.getTxtOthrExpAmt()));
        }
        if (observable.getTxtCommRecvdAmt() != null && !observable.getTxtCommRecvdAmt().equals("")) {
            txtCommRecd.setText(String.valueOf(observable.getTxtCommRecvdAmt()));
        }
        if (observable.getCboTransType() != null && !observable.getCboTransType().equals("")) {
            cboTransType.setSelectedItem(String.valueOf(observable.getCboTransType()));
        }
        if (observable.getTxtPurcBillNo() != null && !observable.getTxtPurcBillNo().equals("")) {
            txtPurcBillNo.setText(observable.getTxtPurcBillNo());
        }

        if (observable.getTxtTinNo() != null && !observable.getTxtTinNo().equals("")) {
            txtTinNo.setText(observable.getTxtTinNo());
        }
        if (observable.getTdtSalesDate() != null && !observable.getTdtSalesDate().equals("")) {
            tdtSalesDate.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtSalesDate())));
        }
        if (observable.getTdtBillDate() != null && !observable.getTdtBillDate().equals("")) {
            tdtBillDate.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtBillDate())));
        }
        if (observable.getTxtNarration() != null && !observable.getTxtNarration().equals("")) {
            txtNarration.setText(observable.getTxtNarration());
        }
        if (observable.getTxtstIndentNo() != null && !observable.getTxtstIndentNo().equals("")) {
            txtstIndentNo.setText(observable.getTxtstIndentNo());
        }
        if (observable.getTxtCommRecvdAmt() != null && !observable.getTxtCommRecvdAmt().equals("")) {
            txtCommRecd.setText(CommonUtil.convertObjToStr(observable.getTxtCommRecvdAmt()));
        }
        if (observable.getTxtVatAmt() != null && !observable.getTxtVatAmt().equals("")) {
            txtVatAmt.setText(CommonUtil.convertObjToStr(observable.getTxtVatAmt()));
        }
        if (observable.getTxtMiscAmt() != null && !observable.getTxtMiscAmt().equals("")) {
            txtMiscIncome.setText(CommonUtil.convertObjToStr(observable.getTxtMiscAmt()));
        }
        if (observable.getTxtOthrExpAmt() != null && !observable.getTxtOthrExpAmt().equals("")) {
            txtOtherExp.setText(CommonUtil.convertObjToStr(observable.getTxtOthrExpAmt()));
        }
        if (observable.getTxtVatIndAmt() != null && !observable.getTxtVatIndAmt().equals("")) {
            txtVatIndAmount.setText(CommonUtil.convertObjToStr(observable.getTxtVatIndAmt()));
        }
        if (observable.getTxtOtherIncome() != null && !observable.getTxtOtherIncome().equals("")) {
            txtOtherIncome.setText(CommonUtil.convertObjToStr(observable.getTxtOtherIncome()));
        }        
        if (observable.getTxtGSTAmt() != null && !observable.getTxtGSTAmt().equals("")) {
            txtGSTAmt.setText(CommonUtil.convertObjToStr(observable.getTxtGSTAmt()));
        }
        
        if (observable.getTxtRoundOff() != null && !observable.getTxtRoundOff().equals("")) {
            txtRoundOff.setText(CommonUtil.convertObjToStr(observable.getTxtRoundOff()));
        }
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAddDelete;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDepo;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnaddNew;
    private com.see.truetransact.uicomponent.CButton btnaddSave;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CScrollPaneTable cScrollPaneTable1;
    private com.see.truetransact.uicomponent.CComboBox cboSupplier;
    private com.see.truetransact.uicomponent.CComboBox cboTransType;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblAmt;
    private com.see.truetransact.uicomponent.CLabel lblBillDate;
    private com.see.truetransact.uicomponent.CLabel lblCommRecvd;
    private com.see.truetransact.uicomponent.CLabel lblDepoId;
    private com.see.truetransact.uicomponent.CLabel lblGSTAmt;
    private com.see.truetransact.uicomponent.CLabel lblIndDate;
    private com.see.truetransact.uicomponent.CLabel lblMiscIncome;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNarration;
    private com.see.truetransact.uicomponent.CLabel lblOtherExp;
    private com.see.truetransact.uicomponent.CLabel lblOtherIncme;
    private com.see.truetransact.uicomponent.CLabel lblPurAmt;
    private com.see.truetransact.uicomponent.CLabel lblPurchBillNo;
    private com.see.truetransact.uicomponent.CLabel lblRoundOff;
    private com.see.truetransact.uicomponent.CLabel lblSalesDate;
    private com.see.truetransact.uicomponent.CLabel lblSalesmanName;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace52;
    private com.see.truetransact.uicomponent.CLabel lblSpace53;
    private com.see.truetransact.uicomponent.CLabel lblSpace54;
    private com.see.truetransact.uicomponent.CLabel lblSpace55;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStoreName;
    private com.see.truetransact.uicomponent.CLabel lblSupplier;
    private com.see.truetransact.uicomponent.CLabel lblTinNo;
    private com.see.truetransact.uicomponent.CLabel lblTotAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalPurAmt;
    private com.see.truetransact.uicomponent.CLabel lblTransType;
    private com.see.truetransact.uicomponent.CLabel lblVatAmt;
    private com.see.truetransact.uicomponent.CLabel lblVatIndAmt;
    private com.see.truetransact.uicomponent.CLabel lblstIndentNo;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panCheckBookBtn;
    private com.see.truetransact.uicomponent.CPanel panIndeedData;
    private com.see.truetransact.uicomponent.CPanel panIndend;
    private com.see.truetransact.uicomponent.CPanel panMutipleTrans;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTrans;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpMultiTransTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CTable tblMultiTransTable;
    private com.see.truetransact.uicomponent.CToolBar tbrIndend;
    private com.see.truetransact.uicomponent.CDateField tdtBillDate;
    private com.see.truetransact.uicomponent.CDateField tdtDateIndand;
    private com.see.truetransact.uicomponent.CDateField tdtSalesDate;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtCommRecd;
    private com.see.truetransact.uicomponent.CTextField txtDepoId;
    private com.see.truetransact.uicomponent.CTextField txtGSTAmt;
    private com.see.truetransact.uicomponent.CTextField txtMiscIncome;
    private com.see.truetransact.uicomponent.CTextField txtNarration;
    private com.see.truetransact.uicomponent.CTextField txtOtherExp;
    private com.see.truetransact.uicomponent.CTextField txtOtherIncome;
    private com.see.truetransact.uicomponent.CTextField txtPurAmount;
    private com.see.truetransact.uicomponent.CTextField txtPurcBillNo;
    private com.see.truetransact.uicomponent.CTextField txtRoundOff;
    private com.see.truetransact.uicomponent.CTextField txtSalesmanName;
    private com.see.truetransact.uicomponent.CTextField txtStoreName;
    private com.see.truetransact.uicomponent.CTextField txtTinNo;
    private com.see.truetransact.uicomponent.CTextField txtTotAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotalPurAmt;
    private com.see.truetransact.uicomponent.CTextField txtVatAmt;
    private com.see.truetransact.uicomponent.CTextField txtVatIndAmount;
    private com.see.truetransact.uicomponent.CTextField txtstIndentNo;
    // End of variables declaration//GEN-END:variables
    //     private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.clientutil.TableModel tbModel;
}
