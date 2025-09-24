/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PurchaseEntryUI.java
 *
 * Created on September 12, 2011, 12:08 PM
 */
package com.see.truetransact.ui.indend;
//import com.see.iie.tools.workflow.clientutils.ComboBoxModel;
import java.util.*;
import java.text.*;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.Date;
import javax.swing.table.*;
import java.awt.event.*;
import javax.swing.event.*;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;

/**
 *
 * @author  userdd
 */
public class PurchaseEntryUI extends CInternalFrame implements Observer, UIMandatoryField {

    private PurchaseEntryOB observable;
    private PurchaseEntryMRB objMandatoryRB = new PurchaseEntryMRB();//Instance for the MandatoryResourceBundle
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private final String AUTHORIZE = "Authorize";//Variable used when btnAuthorize is clicked
    public double balanceCash = 0.0;
    public String supName = "";
    public String sundryActnum = "";
    public String supActnum = "";
    AuthorizeListUI authorizeListUI = null;
    private TableModelListener tableModelListener;
    private int rejectFlag = 0;
    boolean fromAuthorizeUI = false;
    private Date currDt = null;
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListCreditUI CashierauthorizeListUI=null;
    AuthorizeListDebitUI ManagerauthorizeListUI=null;
    private Date currDate = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    /** Creates new form ifrNewBorrowing */
    public PurchaseEntryUI() {
        setSelectedBranchID(ProxyParameters.BRANCH_ID);
        initComponents();
        setFieldNames();
        setObservable();
        observable.resetForm();
        initComponentData();
        setMaxLengths();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panPurchase, getMandatoryHashMap());
        ClientUtil.enableDisable(panPurchase, false);
        ClientUtil.enableDisable(panOtherDetails, false);
        ClientUtil.enableDisable(panOtherDetails1, false);
        setButtonEnableDisable();
        txtPurAmount.setText("");
        txtPurComm.setText("");
        txtPurchaseRet.setText("");
        txtSundry.setText("");
        txtInvestAcHead.setText("");
        txtCashAmount.setAllowNumber(true);
        txtChequeNo.setAllowAll(true);
        txtNarration.setAllowAll(true);
        lblCashToBeEntered.setText("0.0");
        currDt = ClientUtil.getCurrentDate();
         if(tabShare.getSelectedIndex()==2){
            btnAuthorize.setEnabled(false);
        }
        currDate = (Date)currDt.clone();
    }

    public void initComponentData() {
        cboProductId.setModel(observable.getCbmProdId());
        cboProductId.setEnabled(false);
        cboProductId.setEditable(false);
        txtAccountNumber.setEnabled(false);
        txtAccountNumber.setEditable(false);
        btnAccountNumber.setEnabled(false);
        HashMap whereMap = new HashMap();
        java.util.List lst1 = ClientUtil.executeQuery("getTradeexpenseTableData", whereMap);
        HashMap tradeMap = new HashMap();
        if(tabShare.getSelectedIndex()==2){
            btnAuthorize.setEnabled(false);
        }
        for(int j = 0; j < lst1.size();)
        {
            int i = 0;
            while(i < lst1.size()) 
            {
                tradeMap = (HashMap)lst1.get(j);
                String date=tradeMap.get("EFFECT_DT").toString();
                String result  = date.split(" ")[0];
                tblproduct.getModel().setValueAt(result, i, 0);
                tblproduct.getModel().setValueAt(tradeMap.get("FROM_WEIGHT"), i, 1);
                tblproduct.getModel().setValueAt(tradeMap.get("TO_WEIGHT"), i, 2);
                tblproduct.getModel().setValueAt(tradeMap.get("AMOUNT"), i, 3);
                j++;
                i++;
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
    /* Auto Generated Method - setFieldNames()
    This method assigns name for all the components.
    Other functions are working based on this name. */
 private void setTableModelListener()
    {
        try
        {
            tableModelListener = new TableModelListener() {

                public void tableChanged(TableModelEvent e)
                {
                    if(e.getType() == 0)
                    {
                        System.out.println((new StringBuilder()).append("Cell ").append(e.getFirstRow()).append(", ").append(e.getColumn()).append(" changed. The new value: ").append(tblproductWithAmount.getModel().getValueAt(e.getFirstRow(), e.getColumn())).toString());
                        int row = e.getFirstRow();
                        System.out.println((new StringBuilder()).append("e.getFirstRow()").append(e.getFirstRow()).toString());
                        int column = e.getColumn();
                        int selectedRow = tblproductWithAmount.getSelectedRow();
                        String scheme = CommonUtil.convertObjToStr(tblproductWithAmount.getValueAt(tblproductWithAmount.getSelectedRow(), 1));
                        System.out.println((new StringBuilder()).append("column    ").append(column).toString());
                        double grant = 0;
                        if(column == 4)
                        {
                            TableModel model = tblproductWithAmount.getModel();
                            double sack_no = CommonUtil.convertObjToDouble(tblproductWithAmount.getValueAt(tblproductWithAmount.getSelectedRow(), 4));
                            double amount = CommonUtil.convertObjToDouble(tblproductWithAmount.getValueAt(tblproductWithAmount.getSelectedRow(), 3));
                            System.out.println((new StringBuilder()).append("acc_no  noOfInsPay").append(amount).append(sack_no).toString());
                            double total = sack_no * amount;
                            tblproductWithAmount.setValueAt(Double.valueOf(total), tblproductWithAmount.getSelectedRow(), 5);
                            for(int i = 0; i < tblproductWithAmount.getRowCount(); i++)
                                grant = CommonUtil.convertObjToDouble(tblproductWithAmount.getValueAt(i, 5)) + grant;

                        }
                        lblTotal.setText(CommonUtil.convertObjToStr(Double.valueOf(grant)));
                        if(column == 3)
                            tblproductWithAmount.addMouseListener(new MouseAdapter() {

                                public void mouseClicked(MouseEvent mouseevent)
                                {
                                }
                   
                            }
);
                        boolean chk;
                        if (column == 0) {
                            //    chk = ((Boolean) tblproductWithAmount.getValueAt(tblproductWithAmount.getSelectedRow(), 0)).booleanValue();
                                String acc_no = CommonUtil.convertObjToStr(tblproductWithAmount.getValueAt(tblproductWithAmount.getSelectedRow(), 3));
                                if (scheme.equals("MDS") && acc_no.indexOf("_") != -1) {
                                    acc_no = acc_no.substring(0, acc_no.indexOf("_"));
                            }
                        }
                        if(column == 7)
                            chk = ((Boolean)tblproductWithAmount.getValueAt(tblproductWithAmount.getSelectedRow(), 0)).booleanValue();
                    }
                }

               

            
            
            }
;
            tblproductWithAmount.getModel().addTableModelListener(tableModelListener);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnRejection.setName("btnReject");
        btnSave.setName("btnSave");
        lblPurchaseAmount.setName("lblPurchaseAmount");
        txtPurAmount.setName("txtPurAmount");
        lblPurchaseComm.setName("lblPurchaseComm");
        txtPurComm.setName("txtPurComm");
        lblPurReturn.setName("lblPurReturn");
        txtPurchaseRet.setName("txtPurchaseRet");
        lblSundry.setName("lblSundry");
        txtSundry.setName("txtSundry");
        txtInvestAcHead.setName("txtInvestAcHead");
        lblInvestAcHead.setName("lblInvestAcHead");
    }

    private void setMaxLengths() {
        txtPurAmount.setValidation(new CurrencyValidation(16, 2));
        txtPurComm.setValidation(new CurrencyValidation(16, 2));
        txtPurchaseRet.setValidation(new CurrencyValidation(16, 2));
        txtSundry.setValidation(new CurrencyValidation(16, 2));
        txtInvestAcHead.setValidation(new CurrencyValidation(16, 2));
        txtCashAmount.setValidation(new CurrencyValidation(16, 2));
        HashMap whereMap = new HashMap();
        java.util.List lst1 = ClientUtil.executeQuery("getTradeexpenseTableData", whereMap);
        HashMap tradeDataMap = new HashMap();
        for(int j = 0; j < lst1.size();)
        {
            int i = 0;
            while(i < lst1.size()) 
            {
                tradeDataMap = (HashMap)lst1.get(j);
                tradeDataMap = (HashMap)lst1.get(j);
                String date=tradeDataMap.get("EFFECT_DT").toString();
                String result  = date.split(" ")[0];
                tblproductWithAmount.getModel().setValueAt(result, i, 0);
                tblproductWithAmount.getModel().setValueAt(tradeDataMap.get("FROM_WEIGHT"), i, 1);
                tblproductWithAmount.getModel().setValueAt(tradeDataMap.get("TO_WEIGHT"), i, 2);
                tblproductWithAmount.getModel().setValueAt(tradeDataMap.get("AMOUNT"), i, 3);
                j++;
                i++;
            }
        }

        DefaultTableModel dataModel = new DefaultTableModel();
        tblproductWithAmount.setCellSelectionEnabled(true);
        
        
        setTableModelListener();
    }

    private void setObservable() {
        try {
            observable = PurchaseEntryOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            //parseException.logException(e,true);
            System.out.println("Error in setObservable():" + e);
        }
    }
// private void setTableTitle()
//    {
//        tableTitle.add("Date");
//        tableTitle.add("From Weight");
//        tableTitle.add("To Weight");
//        tableTitle.add("Amount");
//    }
    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)
                || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
            System.out.println("tabstabShare.getSelectedIndex()"+tabShare.getSelectedIndex());
            if(tabShare.getSelectedIndex()==0){
            viewMap.put(CommonConstants.MAP_NAME, "PurchaseEntry.getSelectPurchaseEntryList");
            }
            else if(tabShare.getSelectedIndex()==2){
               viewMap.put(CommonConstants.MAP_NAME, "PurchaseEntry.getSelectTradeExpenseList");   
            }
        }
        if (viewType.equals("SUPPLIER")) {
            viewMap.put(CommonConstants.MAP_NAME, "PurchaseEntry.getSelectSupplierList");
        }
        if (viewType.equals("ACT_NUM")) {
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListAB");

        }    
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        where.put("PROD_ID", ((ComboBoxModel) cboProductId.getModel()).getKeyForSelected());
        viewMap.put(CommonConstants.MAP_WHERE, where);
        where = null;
        if (currField.equals("REPRINT")) {
            HashMap whereMap = new HashMap();
            whereMap.put("FROM_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtReprintDate.getDateValue())));
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            System.out.println("whereMapgjhydj" + whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "PurchaseEntry.getSelectPurchaseEntryReprintList");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
        new ViewAll(this, viewMap).show();
    }

    public void fillData(Object map) {

        HashMap hash = (HashMap) map;
        System.out.println("hash"+hash);
        if (hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
            fromCashierAuthorizeUI = true;
            CashierauthorizeListUI = (AuthorizeListCreditUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            //btnSaveDisable();
            btnRejection.setEnabled(false);
            rejectFlag = 1;
        }
           if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
            fromManagerAuthorizeUI = true;
            ManagerauthorizeListUI = (AuthorizeListDebitUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            //btnSaveDisable();
            btnRejection.setEnabled(false);
            rejectFlag = 1;
        }
            if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            fromNewAuthorizeUI = true;
            newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
            authorizeStatus("AUTHORIZE_BUTTON");
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            hash.remove("PARENT");
            viewType = ClientConstants.VIEW_TYPE_AUTHORIZE;
            btnRejection.setEnabled(false);
            rejectFlag = 1;
        }
            if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            System.out.println("hash.get(PARENT) in p" + hash.get("PARENT"));
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            authorizeStatus("AUTHORIZE_BUTTON");
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            hash.remove("PARENT");
            viewType = ClientConstants.VIEW_TYPE_AUTHORIZE;
            btnRejection.setEnabled(false);
            rejectFlag = 1;
        }
       if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2])|| viewType.equals(ClientConstants.ACTION_STATUS[8]))
            {
        if (CommonUtil.convertObjToStr(hash.get("PURCHASE_TYPE")) != " " && CommonUtil.convertObjToStr(hash.get("PURCHASE_TYPE")).equalsIgnoreCase("INVESTMENT") ) {
            System.out.println("anjuuuu");
            rdoInvestment.setSelected(true);
        } else if (CommonUtil.convertObjToStr(hash.get("PURCHASE_TYPE")) != " " && CommonUtil.convertObjToStr(hash.get("PURCHASE_TYPE")).equalsIgnoreCase("SUNDRY CREDITORS")) {
            System.out.println("anju111111");
            rdoSundry.setSelected(true);
        }
       }}
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                    || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                //observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
              
                HashMap where = new HashMap();
                where.put("PURCHASE_ENTRY_ID", hash.get("PURCHASE_ENTRY_ID"));
                hash.put(CommonConstants.MAP_WHERE, where);
                observable.populateData(hash);
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                    ClientUtil.enableDisable(panPurchase, false);
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    ClientUtil.enableDisable(panPurchase, true);
                }
                if (viewType.equals(AUTHORIZE)) {
                    ClientUtil.enableDisable(panPurchase, false);
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnRejection.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    btnAuthorize.setEnabled(true);
                    btnAuthorize.requestFocusInWindow();
                    btnAuthorize.setFocusable(true);
                    txtSupplier.setText(CommonUtil.convertObjToStr(hash.get("SUPPLIER_NAME")));
                }

System.out.println(" vvvv---"+observable.getProxyReturnMap() +" 777 ---- "+tabShare.getSelectedIndex());
if(tabShare.getSelectedIndex()==2){
    observable.setTradId(CommonUtil.convertObjToStr(hash.get("TRADEEXEPENSE_ID")));
    displayTransDetail1(hash);
}else{
                if (observable.getProxyReturnMap() != null) {
                    displayTransDetail(observable.getProxyReturnMap());
                }
}
                setButtonEnableDisable();
            }
            if (viewType.equals("SUPPLIER")) {
                txtSupplier.setText(CommonUtil.convertObjToStr(hash.get("SUPPLIER_NAME")));
                supName = CommonUtil.convertObjToStr(hash.get("SUPPLIER_ID"));
                sundryActnum = CommonUtil.convertObjToStr(hash.get("SUNDRY_ACT_NUM"));
                supActnum = CommonUtil.convertObjToStr(hash.get("SUP_ACT_NUM"));
                lblCashToBeEntered.setText("0.0");
                HashMap cashMap = new HashMap();
                if(cboTransMode.getSelectedItem().equals("Sundry Creditors")){
                	cashMap.put("SUNDRY_ACT_NUM",hash.get("SUNDRY_ACT_NUM"));
                } else if(cboTransMode.getSelectedItem().equals("Purchase")){
                	cashMap.put("SUNDRY_ACT_NUM",hash.get("SUP_ACT_NUM"));
                }
                List lst1 = ClientUtil.executeQuery("getCashSundryBalance",cashMap);
                System.out.println("lst1"+lst1);
                HashMap Map = new HashMap();
                Map = (HashMap) lst1.get(0);
                System.out.println("MapMapMapMap" + Map);
                lblCashToBeEntered.setText(CommonUtil.convertObjToStr(Map.get("AMOUNT")));
            }
                if (viewType.equals("ACT_NUM")) {
                    txtAccountNumber.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                    //   lblActName.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
            }
//            if (viewType.equals(AUTHORIZE)) {
//            HashMap supMap = new HashMap();
//            supMap.put("SUPPLIER_ID", txtSupplier.getText());
//            System.out.println("txtSupplier.getText()"+txtSupplier.getText());
//            List lstSupName = ClientUtil.executeQuery("getSupplierDescoff",supMap);
//            HashMap supMap1 = new HashMap();
//            supMap1 = (HashMap) lstSupName.get(0);
//            txtSupplier.setText(CommonUtil.convertObjToStr(supMap1.get("SUPPLIER_NAME")));
//            }
           }
        if(viewType.equals("REPRINT")){
            try {

                String reportName = "";
                int yesNo = 0;
                String[] options = {"Yes", "No"};
                yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                System.out.println("#$#$$ yesNo : " + yesNo);
                if (yesNo == 0) {
                    TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    paramMap.put("purchaseID", hash.get("PURCHASE_ENTRY_ID"));
                    ttIntgration.setParam(paramMap);
                    ttIntgration.integrationForPrint("PurchaseentryPrint", true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoTransGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        tabShare = new com.see.truetransact.uicomponent.CTabbedPane();
        panAccountOpening = new com.see.truetransact.uicomponent.CPanel();
        panPurchase = new com.see.truetransact.uicomponent.CPanel();
        panIndeedData = new com.see.truetransact.uicomponent.CPanel();
        lblPurchaseAmount = new com.see.truetransact.uicomponent.CLabel();
        lblPurchaseComm = new com.see.truetransact.uicomponent.CLabel();
        txtPurAmount = new com.see.truetransact.uicomponent.CTextField();
        lblPurReturn = new com.see.truetransact.uicomponent.CLabel();
        txtPurchaseRet = new com.see.truetransact.uicomponent.CTextField();
        lblSundry = new com.see.truetransact.uicomponent.CLabel();
        txtInvestAcHead = new com.see.truetransact.uicomponent.CTextField();
        lblInvestAcHead = new com.see.truetransact.uicomponent.CLabel();
        txtSundry = new com.see.truetransact.uicomponent.CTextField();
        txtPurComm = new com.see.truetransact.uicomponent.CTextField();
        cboTransType = new com.see.truetransact.uicomponent.CComboBox();
        lblTransType = new com.see.truetransact.uicomponent.CLabel();
        lblSupplier = new com.see.truetransact.uicomponent.CLabel();
        lblCashToBeEntered = new com.see.truetransact.uicomponent.CLabel();
        txtCashAmount = new com.see.truetransact.uicomponent.CTextField();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        cboProductId = new com.see.truetransact.uicomponent.CComboBox();
        lblAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        txtAccountNumber = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNumber = new com.see.truetransact.uicomponent.CButton();
        lblChequeNo = new com.see.truetransact.uicomponent.CLabel();
        txtChequeNo = new com.see.truetransact.uicomponent.CTextField();
        lblNarration = new com.see.truetransact.uicomponent.CLabel();
        txtNarration = new com.see.truetransact.uicomponent.CTextField();
        rdoInvestment = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSundry = new com.see.truetransact.uicomponent.CRadioButton();
        txtSupplier = new com.see.truetransact.uicomponent.CTextField();
        btnSupplier = new com.see.truetransact.uicomponent.CButton();
        lblTransMode = new com.see.truetransact.uicomponent.CLabel();
        cboTransMode = new com.see.truetransact.uicomponent.CComboBox();
        lblCash1 = new com.see.truetransact.uicomponent.CLabel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        panReprintDetails = new com.see.truetransact.uicomponent.CPanel();
        lblReprintDate = new com.see.truetransact.uicomponent.CLabel();
        tdtReprintDate = new com.see.truetransact.uicomponent.CDateField();
        btnReprint = new com.see.truetransact.uicomponent.CButton();
        panStatus1 = new com.see.truetransact.uicomponent.CPanel();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg1 = new com.see.truetransact.uicomponent.CLabel();
        tbrIndend = new com.see.truetransact.uicomponent.CToolBar();
        panOtherDetails = new com.see.truetransact.uicomponent.CPanel();
        panIMBP = new com.see.truetransact.uicomponent.CPanel();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblproduct = new com.see.truetransact.uicomponent.CTable();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        lblFromServicePeriod = new com.see.truetransact.uicomponent.CLabel();
        lblToServicePeriod = new com.see.truetransact.uicomponent.CLabel();
        lblMaximumLoanAmount = new com.see.truetransact.uicomponent.CLabel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        txtFromWeight = new com.see.truetransact.uicomponent.CTextField();
        txtToWeight = new com.see.truetransact.uicomponent.CTextField();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        btnAdd = new com.see.truetransact.uicomponent.CButton();
        tabShare1 = new com.see.truetransact.uicomponent.CTabbedPane();
        panOtherDetails1 = new com.see.truetransact.uicomponent.CPanel();
        panIMBP1 = new com.see.truetransact.uicomponent.CPanel();
        cScrollPane2 = new com.see.truetransact.uicomponent.CScrollPane();
        tblproductWithAmount = new com.see.truetransact.uicomponent.CTable();
        lblTotal1 = new com.see.truetransact.uicomponent.CLabel();
        lblTotal = new com.see.truetransact.uicomponent.CLabel();
        txtTradeNarration = new com.see.truetransact.uicomponent.CTextField();
        tbrIndend2 = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace65 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace66 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace67 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace7 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace68 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace69 = new com.see.truetransact.uicomponent.CLabel();
        btnRejection = new com.see.truetransact.uicomponent.CButton();
        lblSpace8 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace70 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setDoubleBuffered(true);
        setFont(new java.awt.Font("Times New Roman", 0, 10)); // NOI18N
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        tabShare.setMinimumSize(new java.awt.Dimension(875, 525));
        tabShare.setPreferredSize(new java.awt.Dimension(875, 500));

        panAccountOpening.setMaximumSize(new java.awt.Dimension(875, 420));
        panAccountOpening.setMinimumSize(new java.awt.Dimension(877, 418));
        panAccountOpening.setPreferredSize(new java.awt.Dimension(877, 418));
        panAccountOpening.setLayout(new java.awt.GridBagLayout());

        panPurchase.setMaximumSize(new java.awt.Dimension(850, 500));
        panPurchase.setMinimumSize(new java.awt.Dimension(850, 500));
        panPurchase.setPreferredSize(new java.awt.Dimension(850, 500));
        panPurchase.setLayout(new java.awt.GridBagLayout());

        panIndeedData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panIndeedData.setMaximumSize(new java.awt.Dimension(750, 400));
        panIndeedData.setMinimumSize(new java.awt.Dimension(750, 400));
        panIndeedData.setPreferredSize(new java.awt.Dimension(750, 400));
        panIndeedData.setLayout(new java.awt.GridBagLayout());

        lblPurchaseAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPurchaseAmount.setText("Purchase Amount");
        lblPurchaseAmount.setMaximumSize(new java.awt.Dimension(110, 16));
        lblPurchaseAmount.setMinimumSize(new java.awt.Dimension(110, 16));
        lblPurchaseAmount.setPreferredSize(new java.awt.Dimension(110, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 204, 0, 0);
        panIndeedData.add(lblPurchaseAmount, gridBagConstraints);

        lblPurchaseComm.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPurchaseComm.setText("Purchase Commission");
        lblPurchaseComm.setMaximumSize(new java.awt.Dimension(150, 16));
        lblPurchaseComm.setMinimumSize(new java.awt.Dimension(150, 16));
        lblPurchaseComm.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 164, 0, 0);
        panIndeedData.add(lblPurchaseComm, gridBagConstraints);

        txtPurAmount.setEditable(false);
        txtPurAmount.setAllowAll(true);
        txtPurAmount.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        panIndeedData.add(txtPurAmount, gridBagConstraints);

        lblPurReturn.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPurReturn.setText("Purchase Return");
        lblPurReturn.setMaximumSize(new java.awt.Dimension(110, 16));
        lblPurReturn.setMinimumSize(new java.awt.Dimension(110, 16));
        lblPurReturn.setPreferredSize(new java.awt.Dimension(110, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 204, 0, 0);
        panIndeedData.add(lblPurReturn, gridBagConstraints);

        txtPurchaseRet.setAllowAll(true);
        txtPurchaseRet.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurchaseRet.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPurchaseRet.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurchaseRetFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        panIndeedData.add(txtPurchaseRet, gridBagConstraints);

        lblSundry.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSundry.setText("Sundry Creditors");
        lblSundry.setMaximumSize(new java.awt.Dimension(110, 16));
        lblSundry.setMinimumSize(new java.awt.Dimension(110, 16));
        lblSundry.setPreferredSize(new java.awt.Dimension(110, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 204, 0, 0);
        panIndeedData.add(lblSundry, gridBagConstraints);

        txtInvestAcHead.setAllowAll(true);
        txtInvestAcHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtInvestAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInvestAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInvestAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        panIndeedData.add(txtInvestAcHead, gridBagConstraints);

        lblInvestAcHead.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInvestAcHead.setText("Investment Amount");
        lblInvestAcHead.setMaximumSize(new java.awt.Dimension(150, 16));
        lblInvestAcHead.setMinimumSize(new java.awt.Dimension(150, 16));
        lblInvestAcHead.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 164, 0, 0);
        panIndeedData.add(lblInvestAcHead, gridBagConstraints);

        txtSundry.setAllowAll(true);
        txtSundry.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSundry.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSundry.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSundryFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        panIndeedData.add(txtSundry, gridBagConstraints);

        txtPurComm.setAllowAll(true);
        txtPurComm.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurComm.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPurComm.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurCommFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        panIndeedData.add(txtPurComm, gridBagConstraints);

        cboTransType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Cash", "Transfer" }));
        cboTransType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTransTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 32;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        panIndeedData.add(cboTransType, gridBagConstraints);

        lblTransType.setText("Transaction Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 215, 0, 0);
        panIndeedData.add(lblTransType, gridBagConstraints);

        lblSupplier.setText("Supplier");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 267, 0, 0);
        panIndeedData.add(lblSupplier, gridBagConstraints);

        lblCashToBeEntered.setText("0.0");
        lblCashToBeEntered.setMaximumSize(new java.awt.Dimension(60, 18));
        lblCashToBeEntered.setMinimumSize(new java.awt.Dimension(60, 18));
        lblCashToBeEntered.setPreferredSize(new java.awt.Dimension(60, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 6, 0, 0);
        panIndeedData.add(lblCashToBeEntered, gridBagConstraints);

        txtCashAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCashAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCashAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 0, 0);
        panIndeedData.add(txtCashAmount, gridBagConstraints);

        lblProductId.setText("Bank Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 246, 0, 0);
        panIndeedData.add(lblProductId, gridBagConstraints);

        cboProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 175;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        panIndeedData.add(cboProductId, gridBagConstraints);

        lblAccountNumber.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 246, 0, 0);
        panIndeedData.add(lblAccountNumber, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 194;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        panIndeedData.add(txtAccountNumber, gridBagConstraints);

        btnAccountNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountNumber.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnAccountNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNumberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 0, 0);
        panIndeedData.add(btnAccountNumber, gridBagConstraints);

        lblChequeNo.setText("Cheque No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 250, 0, 0);
        panIndeedData.add(lblChequeNo, gridBagConstraints);

        txtChequeNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        panIndeedData.add(txtChequeNo, gridBagConstraints);

        lblNarration.setText("Narration");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 262, 0, 0);
        panIndeedData.add(lblNarration, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 194;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 40, 0);
        panIndeedData.add(txtNarration, gridBagConstraints);

        rdoInvestment.setText("Investment");
        rdoInvestment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInvestmentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 230, 0, 0);
        panIndeedData.add(rdoInvestment, gridBagConstraints);

        rdoSundry.setText("Sundry Creditors");
        rdoSundry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSundryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panIndeedData.add(rdoSundry, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 194;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        panIndeedData.add(txtSupplier, gridBagConstraints);

        btnSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSupplier.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupplierActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        panIndeedData.add(btnSupplier, gridBagConstraints);

        lblTransMode.setText("Transaction Mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(42, 211, 0, 0);
        panIndeedData.add(lblTransMode, gridBagConstraints);

        cboTransMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Sundry Creditors", "Purchase" }));
        cboTransMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTransModeActionPerformed(evt);
            }
        });
        cboTransMode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboTransModeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(39, 3, 0, 0);
        panIndeedData.add(cboTransMode, gridBagConstraints);

        lblCash1.setText("Cash");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 284, 0, 0);
        panIndeedData.add(lblCash1, gridBagConstraints);

        cLabel1.setText("Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 18, 0, 0);
        panIndeedData.add(cLabel1, gridBagConstraints);

        panReprintDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Process"));
        panReprintDetails.setMaximumSize(new java.awt.Dimension(400, 110));
        panReprintDetails.setMinimumSize(new java.awt.Dimension(400, 110));
        panReprintDetails.setPreferredSize(new java.awt.Dimension(400, 110));
        panReprintDetails.setLayout(new java.awt.GridBagLayout());

        lblReprintDate.setText("Reprint Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panReprintDetails.add(lblReprintDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panReprintDetails.add(tdtReprintDate, gridBagConstraints);

        btnReprint.setText("Reprint");
        btnReprint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReprintActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(30, 3, 3, 3);
        panReprintDetails.add(btnReprint, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.ipadx = -148;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 42, 0, 16);
        panIndeedData.add(panReprintDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipady = 51;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 14);
        panPurchase.add(panIndeedData, gridBagConstraints);

        panStatus1.setLayout(new java.awt.GridBagLayout());

        lblSpace2.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus1.add(lblSpace2, gridBagConstraints);

        lblStatus1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus1.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus1.add(lblStatus1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus1.add(lblMsg1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 0, 0);
        panPurchase.add(panStatus1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -76;
        gridBagConstraints.ipady = -24;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 32);
        panAccountOpening.add(panPurchase, gridBagConstraints);

        tabShare.addTab("Purchase Entry", panAccountOpening);

        tbrIndend.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        tbrIndend.setMaximumSize(new java.awt.Dimension(527, 29));
        tbrIndend.setMinimumSize(new java.awt.Dimension(527, 29));

        panOtherDetails.setLayout(new java.awt.GridBagLayout());

        panIMBP.setLayout(new java.awt.GridBagLayout());

        cPanel1.setLayout(new java.awt.GridLayout(1, 0));

        cScrollPane1.setMaximumSize(new java.awt.Dimension(500, 250));
        cScrollPane1.setMinimumSize(new java.awt.Dimension(500, 250));

        tblproduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Date", "From Weight(Kg", "To Weight(kg)", "Amount"
            }
        ));
        cScrollPane1.setViewportView(tblproduct);

        cPanel1.add(cScrollPane1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 0);
        panIMBP.add(cPanel1, gridBagConstraints);

        cPanel2.setLayout(new java.awt.GridBagLayout());

        lblFromServicePeriod.setText("From Weight(Kg)");
        lblFromServicePeriod.setMaximumSize(new java.awt.Dimension(150, 16));
        lblFromServicePeriod.setMinimumSize(new java.awt.Dimension(150, 16));
        lblFromServicePeriod.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel2.add(lblFromServicePeriod, gridBagConstraints);

        lblToServicePeriod.setText("To Weight(Kg)");
        lblToServicePeriod.setMaximumSize(new java.awt.Dimension(150, 16));
        lblToServicePeriod.setMinimumSize(new java.awt.Dimension(150, 16));
        lblToServicePeriod.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel2.add(lblToServicePeriod, gridBagConstraints);

        lblMaximumLoanAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel2.add(lblMaximumLoanAmount, gridBagConstraints);

        lblFromDate.setText("Effect From Date");
        lblFromDate.setMaximumSize(new java.awt.Dimension(150, 16));
        lblFromDate.setMinimumSize(new java.awt.Dimension(150, 16));
        lblFromDate.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel2.add(lblFromDate, gridBagConstraints);

        txtFromWeight.setAllowNumber(true);
        txtFromWeight.setMaximumSize(new java.awt.Dimension(100, 21));
        txtFromWeight.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromWeight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFromWeightActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel2.add(txtFromWeight, gridBagConstraints);

        txtToWeight.setAllowNumber(true);
        txtToWeight.setMaximumSize(new java.awt.Dimension(100, 21));
        txtToWeight.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel2.add(txtToWeight, gridBagConstraints);

        txtAmount.setAllowNumber(true);
        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel2.add(txtAmount, gridBagConstraints);

        tdtFromDate.setMaximumSize(new java.awt.Dimension(100, 21));
        tdtFromDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtFromDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel2.add(tdtFromDate, gridBagConstraints);

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        cPanel2.add(btnAdd, gridBagConstraints);

        panIMBP.add(cPanel2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 145;
        gridBagConstraints.ipady = 27;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 123, 11, 0);
        panOtherDetails.add(panIMBP, gridBagConstraints);

        tbrIndend.add(panOtherDetails);

        tabShare.addTab("Trade Expense", tbrIndend);

        tabShare1.setMinimumSize(new java.awt.Dimension(875, 525));
        tabShare1.setPreferredSize(new java.awt.Dimension(875, 500));

        panOtherDetails1.setLayout(new java.awt.GridBagLayout());

        panIMBP1.setLayout(new java.awt.GridBagLayout());

        cScrollPane2.setMaximumSize(new java.awt.Dimension(700, 450));
        cScrollPane2.setMinimumSize(new java.awt.Dimension(700, 450));

        tblproductWithAmount.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Date", "From Weight(Kg", "To Weight(kg)", "Amount", "No Of Sacks", "Total Amount"
            }
        ));
        tblproductWithAmount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblproductWithAmountMouseClicked(evt);
            }
        });
        tblproductWithAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblproductWithAmountKeyPressed(evt);
            }
        });
        cScrollPane2.setViewportView(tblproductWithAmount);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipady = -87;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 79, 0, 125);
        panIMBP1.add(cScrollPane2, gridBagConstraints);

        lblTotal1.setText("Narration");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 21;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 256, 0, 0);
        panIMBP1.add(lblTotal1, gridBagConstraints);

        lblTotal.setText("Total");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 182;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 256, 27, 0);
        panIMBP1.add(lblTotal, gridBagConstraints);

        txtTradeNarration.setMinimumSize(new java.awt.Dimension(500, 21));
        txtTradeNarration.setPreferredSize(new java.awt.Dimension(500, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = -228;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        panIMBP1.add(txtTradeNarration, gridBagConstraints);

        panOtherDetails1.add(panIMBP1, new java.awt.GridBagConstraints());

        tabShare1.addTab("", panOtherDetails1);

        tabShare.addTab("Transaction", tabShare1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = -54;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 11, 0);
        getContentPane().add(tabShare, gridBagConstraints);

        tbrIndend2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        tbrIndend2.setMaximumSize(new java.awt.Dimension(527, 39));
        tbrIndend2.setMinimumSize(new java.awt.Dimension(527, 39));
        tbrIndend2.setPreferredSize(new java.awt.Dimension(527, 39));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setFocusable(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        tbrIndend2.add(btnView);

        lbSpace5.setText("     ");
        tbrIndend2.add(lbSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.setFocusable(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrIndend2.add(btnNew);

        lblSpace65.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace65.setText("     ");
        lblSpace65.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace65.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace65.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrIndend2.add(lblSpace65);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrIndend2.add(btnEdit);

        lblSpace66.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace66.setText("     ");
        lblSpace66.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace66.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace66.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrIndend2.add(lblSpace66);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.setFocusable(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrIndend2.add(btnDelete);

        lbSpace6.setText("     ");
        tbrIndend2.add(lbSpace6);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrIndend2.add(btnSave);

        lblSpace67.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace67.setText("     ");
        lblSpace67.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace67.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace67.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrIndend2.add(lblSpace67);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrIndend2.add(btnCancel);

        lblSpace7.setText("     ");
        tbrIndend2.add(lblSpace7);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.setFocusable(true);
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrIndend2.add(btnAuthorize);

        lblSpace68.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace68.setText("     ");
        lblSpace68.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace68.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace68.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrIndend2.add(lblSpace68);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setFocusable(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrIndend2.add(btnException);

        lblSpace69.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace69.setText("     ");
        lblSpace69.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace69.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace69.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrIndend2.add(lblSpace69);

        btnRejection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnRejection.setToolTipText("Reject");
        btnRejection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectionActionPerformed(evt);
            }
        });
        tbrIndend2.add(btnRejection);

        lblSpace8.setText("     ");
        tbrIndend2.add(lblSpace8);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setFocusable(false);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrIndend2.add(btnClose);

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrIndend2.add(lblSpace70);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.setFocusable(false);
        tbrIndend2.add(btnPrint);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 304;
        gridBagConstraints.ipady = -1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(tbrIndend2, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
  	
	private void displayTransDetail1(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap 22222: " + proxyResultMap);
        HashMap getTransMap = new HashMap(); 
         getTransMap.put("BATCH_ID", proxyResultMap.get("TRADEEXEPENSE_ID"));
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        HashMap returnMap = new HashMap();
        List transList = (List) ClientUtil.executeQuery("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) ClientUtil.executeQuery("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnMap.put("CASH_TRANS_LIST", cashList);
        }
        displayTransDetailNew(returnMap);
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;

    }
    public void  displayTransDetailNew(HashMap returnMap){
         String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
      //  System.out.println("jhj>>>>>>>");
        Object keys[] = returnMap.keySet().toArray();
        System.out.println("jhj>>>>>>>adad");
        //System.out.println("keeeeeeeeeeeyyy>>>>>>>>>>>"+keys[]);
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        HashMap transIdMap = new HashMap();
        HashMap transTypeMap = new HashMap();
       // System.out.println("keeeeeeeeeeeyyy>>>>>>>>>>>" + keys.length);
        for (int i = 0; i < keys.length; i++) {
            System.out.println("jhj>>>>>>>adad1211222@@@@" + (returnMap.get(keys[i]) instanceof String));
            if (returnMap.get(keys[i]) instanceof String) {
          //      System.out.println("hdfdasd");
                continue;
            }

          //  System.out.println("hdfdasd@@@@@");
            tempList = (List) returnMap.get(keys[i]);
          //  System.out.println("hdfdasd@@@@@>>>>>" + tempList);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
              //  System.out.println("haaaiii11....>>>");
                for (int j = 0; j < tempList.size(); j++) {
                  //  System.out.println("haaaiii11....>>>aa");
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                       // System.out.println("haaaiii11....>>>bb");
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                      //  System.out.println("haaaiii11....>>>cc");
                        cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                      //  System.out.println("haaaiii11....>>>dd");
                        cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transTypeMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                //System.out.println("haaaiii22....>>>");
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                      //  System.out.println("haaaiii22....>>>aa");
                        transId = (String) transMap.get("BATCH_ID");
                    }
                    transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                       // System.out.println("haaaiii22....>>>bb");
                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                      //  System.out.println("haaaiii22....>>>cc");
                        transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transIdMap.put(transMap.get("BATCH_ID"), "TRANSFER");
                }
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
  
        
    }
    private void btnAddActionPerformed(ActionEvent evt)
    {
        if(txtFromWeight.getText() == null || txtFromWeight.getText().equalsIgnoreCase(""))
        {
            ClientUtil.displayAlert("From Weight should not be empty!!!");
            return;
        }
        if(txtToWeight.getText() == null || txtToWeight.getText().equalsIgnoreCase(""))
        {
            ClientUtil.displayAlert("To Weight should not be empty!!!");
            return;
        }
        if(txtAmount.getText() == null || txtAmount.getText().equalsIgnoreCase(""))
        {
            ClientUtil.displayAlert("To Weight should not be empty!!!");
            return;
        }
        if(tdtFromDate.getDateValue() == null || tdtFromDate.getDateValue().equalsIgnoreCase(""))
        {
            ClientUtil.displayAlert("Date should not be empty!!!");
            return;
        }
        HashMap prodTypeMap = new HashMap();
        prodTypeMap.put("FROM_DATE", (CommonUtil.convertObjToStr(tdtFromDate.getDateValue())));
        prodTypeMap.put("FROM_WEIGHT", txtFromWeight.getText());
        prodTypeMap.put("TO_WEIGHT", txtToWeight.getText());
        prodTypeMap.put("AMOUNT", txtAmount.getText());
        System.out.println((new StringBuilder()).append("prodTypeMapprodTypeMapprodTypeMap").append(prodTypeMap).toString());
        try
        {
            observable.populateData(prodTypeMap, tblproduct);
            tblproduct.setModel(observable.getTmbTradeExpense());
        }
        catch(Exception e)
        {
            System.err.println((new StringBuilder()).append("Exception ").append(e.toString()).append("Caught").toString());
            e.printStackTrace();
        }
        tdtFromDate.setDateValue("");
        txtFromWeight.setText("");
        txtToWeight.setText("");
        txtAmount.setText("");
    }

    private void txtFromWeightActionPerformed(ActionEvent actionevent)
    {
    }

                                    

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
     
       
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        //viewType=AUTHORIZE;
        System.out.println("observable.getProxyReturnMap()observable.getProxyReturnMap()"+observable.getProxyReturnMap());
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnEdit.setEnabled(false);

        btnCancel.setEnabled(true);
       
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
         int tabno=tabShare.getSelectedIndex();
        if (!viewType.equals(AUTHORIZE)) {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            if(tabno==0){
                System.out.println("tabnotabnotabno"+tabno);
            mapParam.put(CommonConstants.MAP_NAME, "getPurchaseEntryAuthorizeList");
            }
            if(tabno==1){
                 System.out.println("tabnotabnotabno"+tabno);
            mapParam.put(CommonConstants.MAP_NAME, "getTradeExpenseAuthorizeList");
            }
            if(tabno==2){
                 System.out.println("tabnotabnotabno"+tabno);
            mapParam.put(CommonConstants.MAP_NAME, "getTradeExpenseEntryAuthorizeList");
            }
          //  mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizePurchaseEntry");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            //AuthorizeStatusUI fdf=new AuthorizeStatusUI(this,mapParam);
            authorizeUI.show();
            observable.setStatus();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            btnNew.setEnabled(false);
            btnDelete.setEnabled(false);
             if(tabShare.getSelectedIndex()==0){
        try{
         int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        System.out.println("#$#$$ yesNo : "+yesNo);
        if (yesNo==0) {
             HashMap map = (HashMap)(observable.getProxyReturnMap()); 
             System.out.println("rrid  asd"+map.get("purid"));          
             System.out.println("returnMap anju"+map);
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("purchaseID", CommonUtil.convertObjToStr(map.get("purid")));
            paramMap.put("TransDt", currDt);
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            ttIntgration.setParam(paramMap);
                    
                    ttIntgration.integrationForPrint("PurchaseentryPrint");
                         
       }
       }
           
             catch (Exception e) {
            e.printStackTrace();
        } 
        }
        } else if (viewType.equals(AUTHORIZE)) {
            System.out.println("anju........2222222222");
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);//commented on 13-Feb-2012
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
           
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            if(tabShare.getSelectedIndex()==0){
              singleAuthorizeMap.put("PURCHASE_ENTRY_ID", observable.getPurId());//observable.getIRNo()   
            }
           else if(tabShare.getSelectedIndex()==2){
              singleAuthorizeMap.put("TRADEEXPENSE_ID", observable.getTradId());//observable.getIRNo()   
            }
            observable.setAuthMap(singleAuthorizeMap);
            observable.execute("");
//          viewType = "";
            if (fromNewAuthorizeUI) {
                newauthorizeListUI.removeSelectedRow();
                this.dispose();
               newauthorizeListUI.setFocusToTable();
//                authorizeListUI.displayDetails("Investment Trans");
            }
            if (fromAuthorizeUI) {
                authorizeListUI.removeSelectedRow();
                this.dispose();
                authorizeListUI.setFocusToTable();
//                authorizeListUI.displayDetails("Investment Trans");
            }
            if (fromCashierAuthorizeUI) {
                CashierauthorizeListUI.removeSelectedRow();
                this.dispose();
                CashierauthorizeListUI.setFocusToTable();
            } 
            if (fromManagerAuthorizeUI) {
                ManagerauthorizeListUI.removeSelectedRow();
                this.dispose();
                ManagerauthorizeListUI.setFocusToTable();
            }
            btnCancelActionPerformed(null);
        }
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panPurchase, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        btnAuthorize.setEnabled(true);
        btnRejection.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(false);
        btnException.setEnabled(false);
        txtPurAmount.setText("");
        txtPurComm.setText("");
        txtPurchaseRet.setText("");
        txtSundry.setText("");
        txtInvestAcHead.setText("");
        supActnum = "";
        sundryActnum = "";
        cboTransMode.setSelectedItem(" ");
        cboTransMode.setSelectedItem("");
        txtPurAmount.setEnabled(false);
        lblTotal.setText("");
        setModified(false);
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
            this.dispose();
            fromCashierAuthorizeUI = false;
            CashierauthorizeListUI.setFocusToTable();
        }
        if (fromManagerAuthorizeUI) {
            this.dispose();
            fromManagerAuthorizeUI = false;
            ManagerauthorizeListUI.setFocusToTable();
        }
    }//GEN-LAST:event_btnCancelActionPerformed

   

    private void savePerformed()
            
    {
        // System.out.println("TODO add your handling code here:"); 
        String action = "";
        System.out.println((new StringBuilder()).append("ACTION ====").append(observable.getActionType()).toString());
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ){
         //   System.out.println("TODO add your handling code here:53"); 
            action=CommonConstants.TOSTATUS_INSERT;
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
        System.out.println("saveActionsaveAction");
        int tabno=tabShare.getSelectedIndex();
        System.out.println("tabnotabno"+tabno);
        observable.setTabNo(tabShare.getSelectedIndex());
        System.out.println("tabShare.getTabCount()tabShare.getTabCount()"+tabShare.getTabCount());
        if(tabno==0){
             System.out.println("saveActionsaveActionfor tab 00000");
        final String mandatoryMessage = checkMandatory(panPurchase);
        StringBuffer message = new StringBuffer(mandatoryMessage);
        /*  if(txtPurComm.getText().equals(""))
        message.append(objMandatoryRB.getString("txtPurComm"));
        
        if(txtPurchaseRet.getText().equals("")) {
        message.append(objMandatoryRB.getString("txtPurchaseRet"));
        }
        if(txtSundry.getText().equals("")) {
        message.append(objMandatoryRB.getString("txtSundry"));
        }
        if(txtInvestAcHead.getText().equals("")) {
        message.append(objMandatoryRB.getString("txtInvestAcHead"));
        }*/

        double purAmt = CommonUtil.convertObjToDouble(txtPurAmount.getText());
        if (cboTransMode.getSelectedIndex() == 0) {
            displayAlert("Select Transaction Mode");
            return;
        }
        if (cboTransMode.getSelectedIndex() == 0) {
            displayAlert("Select Transaction Mode");
            return;
        }
        if (cboTransType.getSelectedIndex() == 0) {
            displayAlert("Select Transaction Type");
            return;
        }
        if (purAmt <= 0) {
            displayAlert("Purchase Amount should not be empty!!!");
            return;
        }
        double purComm = CommonUtil.convertObjToDouble(txtPurComm.getText());
        double purRet = CommonUtil.convertObjToDouble(txtPurchaseRet.getText());
        double sundryCred = CommonUtil.convertObjToDouble(txtSundry.getText());
        double invest = CommonUtil.convertObjToDouble(txtInvestAcHead.getText());
        DecimalFormat df = new DecimalFormat("#.##"); // added by shihad for mantis 10390 on 22.02.2015
        if (message.length() > 0) {
            displayAlert(message.toString());
        } else {
            if (purAmt < CommonUtil.convertObjToDouble(df.format(purComm + purRet + sundryCred + invest))) {
                displayAlert("Purcahse Amount should be grater than or equal to sum of commission,purchase return, sundry creditors and Invest Ac Head");
                return;
            }
            balanceCash = 0.0;
            balanceCash = purAmt - (purComm + purRet + sundryCred + invest);
        }
        }  updateOBFields();
            System.out.println("status 998====" + status);
            observable.execute(status);
           
            
                    
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("PURCHASE_ENTRY_ID");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap() != null) {
                    if (observable.getProxyReturnMap().containsKey("CONFIG_ID")) {
                        lockMap.put("CONFIG_ID", observable.getProxyReturnMap().get("CONFIG_ID"));
                    }
                    displayTransDetail(observable.getProxyReturnMap());
                    System.out.println("tabShare.getSelectedIndex()"+tabShare.getSelectedIndex());
                    
         if(tabShare.getSelectedIndex()==0){
        try{
         int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        System.out.println("#$#$$ yesNo : "+yesNo);
        if (yesNo==0) {
             HashMap map = (HashMap)(observable.getProxyReturnMap()); 
             System.out.println("rrid  asd"+map.get("purid"));          
             System.out.println("returnMap anju"+map);
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("purchaseID", CommonUtil.convertObjToStr(map.get("purid")));
            paramMap.put("TransDt", currDt);
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            ttIntgration.setParam(paramMap);
            ttIntgration.integrationForPrint("PurchaseentryPrint");
                         
       }
       }
            catch (Exception e) {
            e.printStackTrace();
        } 
        }else if(tabShare.getSelectedIndex()==2){
       
             HashMap transTypeMap = new HashMap();
             HashMap transMap = new HashMap();
             HashMap map = (HashMap) (observable.getProxyReturnMap());
             HashMap transCashMap = new HashMap();
             String reportName = "";
             transCashMap.put("TRADE_ID", CommonUtil.convertObjToStr(map.get("TRADEEXEPENSE_ID")));
             HashMap transIdMap = new HashMap();
             List list = null;
             list = ClientUtil.executeQuery("getTradeIdData", transCashMap);
             if (list != null && list.size() > 0) {
                 for (int i = 0; i < list.size(); i++) {
                     transMap = (HashMap) list.get(i);
                     transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                     transTypeMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
                 }
             }
             int yesNo = 0;
             String[] voucherOptions = {"Yes", "No"};
             if (list != null && list.size() > 0) {
                 yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                         COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                         null, voucherOptions, voucherOptions[0]);
                 if (yesNo == 0) {
                     com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
                     HashMap paramMap = new HashMap();
                     paramMap.put("TransDt", currDt.clone());
                     paramMap.put("TransModeType",CommonConstants.GL_TRANSMODE_TYPE);
                     paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                     Object keys1[] = transIdMap.keySet().toArray();
                     for (int i = 0; i < keys1.length; i++) {
                         paramMap.put("TransId", keys1[i]);
                                    ttIntgration.setParam(paramMap);
                                    reportName = "CashPayment";
                                    ttIntgration.integrationForPrint(reportName, true);
                                }
                            }
                        }
                    }
                }
            if (status == CommonConstants.TOSTATUS_UPDATE) {
                lockMap.put("PURCHASE_ENTRY_ID", observable.getPurId());// observable.getIRNo());
            }
            settings();
        }
    }
    private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap : " + proxyResultMap);
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        for (int i = 0; i < keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            System.out.println("tempListtempListtempListtempList"+tempList);
            if(tabShare.getSelectedIndex()==0){
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {

                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        cashDisplayStr += "Purchase Id : " + transMap.get("LINK_BATCH_ID") + "\n";
                        transId = (String) transMap.get("TRANS_ID");
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
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {

                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transferDisplayStr += "Purchase Id" + transMap.get("LINK_BATCH_ID") + "\n";
                        transId = (String) transMap.get("BATCH_ID");
                    }
                    transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                }
                transferCount++;
            }
            }
            if(tabShare.getSelectedIndex()==2){
               for (int j = 0; j < tempList.size(); j++) {
                   System.out.println("tempList================="+tempList);
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        cashDisplayStr += "trade id : " + transMap.get("LINK_BATCH_ID") + "\n";
                        transId = (String) transMap.get("TRANS_ID");
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
                }
                cashCount++;
            }
        }
        if (cashCount > 0) {
            displayStr += cashDisplayStr;
        }
        if (transferCount > 0) {
            displayStr += transferDisplayStr;
        }
        ClientUtil.showMessageWindow("" + displayStr);
        transMap=null;
        /*  int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        System.out.println("#$#$$ yesNo : "+yesNo);
        if (yesNo==0) {
        TTIntegration ttIntgration = null;
        HashMap paramMap = new HashMap();
        paramMap.put("TransId", transId);
        paramMap.put("TransDt", observable.getCurrDt());
        
        paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
        ttIntgration.setParam(paramMap);
        String reportName = "";
        transType = cboTransType.getSelectedItem()+"";
        if(transferCount>0){
        reportName = "ReceiptPayment";
        } else if (transType.equals("Purchase") || transType.equals("Sales Return")) {
        reportName = "CashPayment";
        } else {
        reportName = "CashReceipt";
        }
        ttIntgration.integrationForPrint(reportName, false);
        }*/
      
    }

    /** Method used to check whether the Mandatory Fields in the Form are Filled or not */
    private String checkMandatory(javax.swing.JComponent component) {
        //  return new MandatoryCheck().checkMandatory(getClass().getName(), component, getMandatoryHashMap());
        return "";
        //validation error
    }

    /** Method used to Give a Alert when any Mandatory Field is not filled by the user */
    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    /* set the screen after the updation,insertion, deletion */

    private void settings() {
        observable.resetForm();
        observable.setPurId("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panPurchase, false);
        observable.setResultStatus();
        btnCancelActionPerformed(null);
    }

    public Date getDateValue(String date1) {
        DateFormat formatter;
        Date date = null;
        try {

            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
            // String s1 = "2008-03-30T15:30:00.000+02:00";
            // date1 = date1.substring(0, date1.indexOf('.'));
            System.out.println("Result==> " + sdf2.format(sdf1.parse(date1)));
            date = new Date(sdf2.format(sdf1.parse(date1)));
            System.out.println("date IOOOOOOO==> " + date);
        } catch (ParseException e) {
            System.out.println("Error in getDateValue():" + e);
        }
        return date;
    }

    public void updateOBFields() {
        observable.setBalnceCash(balanceCash);
        observable.setTxtPurAmount(CommonUtil.convertObjToDouble(txtPurAmount.getText()));
        observable.setTxtPurComm(CommonUtil.convertObjToDouble(txtPurComm.getText()));
        observable.setTxtPurchaseRet(CommonUtil.convertObjToDouble(txtPurchaseRet.getText()));
        observable.setTxtSundry(CommonUtil.convertObjToDouble(txtSundry.getText()));
        observable.setTxtInvestAcHead(CommonUtil.convertObjToDouble(txtInvestAcHead.getText()));
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setCboTransType(cboTransType.getSelectedItem().toString());
        observable.setCboTransMode(cboTransMode.getSelectedItem().toString());
        observable.setCboProductId(cboProductId.getSelectedItem().toString());
        observable.setTxtSupplier(supName);
        if(rdoInvestment.isSelected()==true){
            observable.setTxtPurchaseType("INVESTMENT");
            observable.setRdoInvestment(true);
            
        }else{
            observable.setRdoInvestment(false);
        }
        if(rdoSundry.isSelected()==true){
             observable.setTxtPurchaseType("SUNDRY CREDITORS");
             observable.setRdoSundry(true);
        }else{
            observable.setRdoSundry(false);
        }
//        observable.setRdoInvestment(rdoInvestment.isSelected());
//        observable.setRdoSundry(rdoSundry.isSelected());
        if (txtAccountNumber.getText() != null && !txtAccountNumber.getText().equals("")) {
            observable.setTxtAccountNumber(txtAccountNumber.getText());
        } else {
            observable.setTxtAccountNumber(sundryActnum);
        }
      //  observable.setTxtAccountNumber(txtAccountNumber.getText());
        observable.setTxtCashAmount(CommonUtil.convertObjToDouble(txtCashAmount.getText()));
        observable.setTxtChequeNo(txtChequeNo.getText());
        observable.setTxtNarration(txtNarration.getText());
        if(cboTransMode.getSelectedItem().equals("Sundry Creditors")){
       		observable.setTxtSupActnum(sundryActnum); 
        }else{
           
            observable.setTxtSupActnum(supActnum);
        }
        observable.setTdtFromDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
        observable.setTxtFromWeight(CommonUtil.convertObjToDouble(txtFromWeight.getText()));
        observable.setTxtToWeight(CommonUtil.convertObjToDouble(txtToWeight.getText()));
        observable.setTxtAmount(CommonUtil.convertObjToDouble(txtAmount.getText()));
        observable.setLblTotal(Math.round(CommonUtil.convertObjToDouble(lblTotal.getText())));
        observable.setTxtTradeNarration(txtTradeNarration.getText());
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView(ClientConstants.ACTION_STATUS[3]);
        btnAuthorize.setEnabled(false);
        btnRejection.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        btnAuthorize.setEnabled(false);
        btnRejection.setEnabled(false);
        btnException.setEnabled(false);
        btnDelete.setEnabled(true);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnClose.setEnabled(true);
        txtAccountNumber.setEnabled(false);
        txtPurComm.setEnabled(true);
        txtPurchaseRet.setEnabled(true);
        txtSundry.setEnabled(true);
        txtInvestAcHead.setEnabled(true);
		//rdoSundry.setEnabled(false);
        txtPurAmount.setEnabled(false);
        txtCashAmount.setEnabled(false);
        
    }//GEN-LAST:event_btnEditActionPerformed

                                      
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        btnSave.setEnabled(!btnSave.isEnabled());
        btnCancel.setEnabled(!btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        /* txtPurAmount.setText("0.00");
        txtPurComm.setText("0.00");
        txtPurchaseRet.setText("0.00");
        txtSundry.setText("0.00");
        txtInvestAcHead.setText("0.00");*/
        btnReprint.setEnabled(!btnReprint.isEnabled());

    }
    private void mnuProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuProcessActionPerformed

private void txtPurCommFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurCommFocusLost
// TODO add your handling code here:
    calcTotalPurchase();
}//GEN-LAST:event_txtPurCommFocusLost

private void txtPurchaseRetFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurchaseRetFocusLost
// TODO add your handling code here:
    calcTotalPurchase();
}//GEN-LAST:event_txtPurchaseRetFocusLost

private void txtSundryFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSundryFocusLost
// TODO add your handling code here:
    calcTotalPurchase();
}//GEN-LAST:event_txtSundryFocusLost

private void txtInvestAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInvestAcHeadFocusLost
// TODO add your handling code here:
    calcTotalPurchase();
}//GEN-LAST:event_txtInvestAcHeadFocusLost

private void btnAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNumberActionPerformed
    callView("ACT_NUM");
}//GEN-LAST:event_btnAccountNumberActionPerformed

private void cboTransTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTransTypeActionPerformed
// TODO add your handling code here:
    if (cboTransMode.getSelectedIndex() > 0) {
        if (cboTransMode.getSelectedItem().toString().equalsIgnoreCase("Purchase")) {
            if (cboTransType.getSelectedIndex() > 0) {
                if (cboTransType.getSelectedItem().toString().equalsIgnoreCase("Transfer")) {
                    rdoInvestment.setEnabled(true);
                    rdoSundry.setEnabled(true);
                    txtCashAmount.setEditable(false);
                } else {
                    rdoInvestment.setEnabled(false);
                    rdoSundry.setEnabled(false);
                    txtCashAmount.setEditable(true);
                    txtInvestAcHead.setEditable(false);
                    txtSundry.setEditable(false);
                }
            } else {
                rdoInvestment.setEnabled(false);
                rdoSundry.setEnabled(false);
                txtCashAmount.setEditable(false);
            }
        }
        if (cboTransMode.getSelectedItem().toString().equalsIgnoreCase("Sundry Creditors")) {
            if (cboTransType.getSelectedIndex() > 0) {
                if (cboTransType.getSelectedItem().toString().equalsIgnoreCase("Transfer")) {
                    rdoInvestment.setEnabled(true);
                    rdoSundry.setEnabled(true);
                    txtCashAmount.setEditable(false);
                } else {
                    rdoInvestment.setEnabled(false);
                    rdoSundry.setEnabled(false);
                    txtCashAmount.setEditable(true);
                    txtInvestAcHead.setEditable(false);
                    txtSundry.setEditable(false);
                }
            } else {
                rdoInvestment.setEnabled(false);
                rdoSundry.setEnabled(false);
                txtCashAmount.setEditable(false);
            }
        }
    }
    //rdoSundry.setEnabled(false); 
}//GEN-LAST:event_cboTransTypeActionPerformed

private void btnSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierActionPerformed
// TODO add your handling code here:
    callView("SUPPLIER");
    lblCashToBeEntered.setEnabled(true);
    
    if(cboTransMode.getSelectedItem().equals("Sundry Creditors")){
    lblPurchaseAmount.setText("Sundry Amount");
    }
    else{
        lblPurchaseAmount.setText("Purchase Amount");
        }
        
}//GEN-LAST:event_btnSupplierActionPerformed

    private void rdoInvestmentActionPerformed(ActionEvent evt)
    {
        if(rdoInvestment.isSelected())
        {
            cboProductId.setEnabled(true);
            rdoSundry.setSelected(false);
            txtAccountNumber.setEnabled(true);
            btnAccountNumber.setEnabled(true);
            txtSundry.setEditable(false);
            txtInvestAcHead.setEditable(true);
        } else
        {
            cboProductId.setEnabled(false);
            rdoSundry.setSelected(true);
            rdoInvestment.setSelected(false);
            txtAccountNumber.setEnabled(false);
            btnAccountNumber.setEnabled(false);
            txtInvestAcHead.setEditable(false);
        }
    }

    private void rdoSundryActionPerformed(ActionEvent evt)
    {
        if(rdoSundry.isSelected())
        {
            cboProductId.setEnabled(false);
            rdoInvestment.setSelected(false);
            txtAccountNumber.setEnabled(false);
            rdoSundry.setSelected(true);
            btnAccountNumber.setEnabled(false);
            txtSundry.setEditable(true);
            txtInvestAcHead.setEditable(false);
        } else
        {
            rdoSundry.setSelected(false);
            cboProductId.setEnabled(false);
            rdoInvestment.setSelected(true);
            txtAccountNumber.setEnabled(false);
            btnAccountNumber.setEnabled(false);
            txtSundry.setEditable(false);
        }
    }

private void txtCashAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCashAmountFocusLost
// TODO add your handling code here:
    calcTotalPurchase();
}//GEN-LAST:event_txtCashAmountFocusLost

private void cboTransModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTransModeActionPerformed
 if(cboTransMode.getSelectedItem()!=null && cboTransMode.getSelectedItem().equals("Sundry Creditors")){
      txtSundry.setEnabled(false);
 }
}//GEN-LAST:event_cboTransModeActionPerformed

private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        tblproductWithAmount.setEnabled(true);
        tblproduct.setEnabled(true);
        observable.resetForm();
        txtPurAmount.setText("");
        txtPurComm.setText("");
        txtPurchaseRet.setText("");
        txtSundry.setText("");
        txtInvestAcHead.setText("");
        txtCashAmount.setText("");
        ClientUtil.enableDisable(panPurchase, true);
        ClientUtil.enableDisable(panOtherDetails, true);
        ClientUtil.enableDisable(panOtherDetails1, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnRejection.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        rdoInvestment.setEnabled(false);
        rdoSundry.setEnabled(false);
        cboProductId.setEnabled(false);
        txtTradeNarration.setEnabled(true);
        txtAccountNumber.setEnabled(false);
        btnAccountNumber.setEnabled(false);
        txtPurAmount.setEnabled(false);
        setModified(true);
        btnReprint.setEnabled(true);
        txtSupplier.setEnabled(false);
}//GEN-LAST:event_btnNewActionPerformed

private void btnRejectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectionActionPerformed
         observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnRejection.setEnabled(true);
}//GEN-LAST:event_btnRejectionActionPerformed

private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        savePerformed();
        lblTotal.setText("");
        btnAuthorize.setEnabled(true);
        btnRejection.setEnabled(true);
        btnException.setEnabled(true);
        cboTransMode.setSelectedIndex(0);
       
}//GEN-LAST:event_btnSaveActionPerformed

private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
 cifClosingAlert();
}//GEN-LAST:event_btnCloseActionPerformed

private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_btnPrintActionPerformed

private void txtSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupplierActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtSupplierActionPerformed

private void txtAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountNumberActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtAccountNumberActionPerformed

private void txtCashAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCashAmountActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtCashAmountActionPerformed

private void txtPurchaseRetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPurchaseRetActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtPurchaseRetActionPerformed

private void txtSundryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSundryActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtSundryActionPerformed

private void cboProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIdActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_cboProductIdActionPerformed

private void btnReprintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReprintActionPerformed
 if (tdtReprintDate.getDateValue().length() > 0) {
           callView("REPRINT");
           viewType = "REPRINT";
        }
}//GEN-LAST:event_btnReprintActionPerformed
private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDate.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
}
private void cboTransModeFocusLost(java.awt.event.FocusEvent evt) {                                       
}
    private void tblproductWithAmountMouseClicked(MouseEvent mouseevent)
    {
    }

    private void tblproductWithAmountKeyPressed(KeyEvent keyevent)
    {
    }

    private void calcTotalPurchase()
    {
        double purComm = CommonUtil.convertObjToDouble(txtPurComm.getText()).doubleValue();
        double purRet = CommonUtil.convertObjToDouble(txtPurchaseRet.getText()).doubleValue();
        double sundryCred = CommonUtil.convertObjToDouble(txtSundry.getText()).doubleValue();
        double invest = CommonUtil.convertObjToDouble(txtInvestAcHead.getText()).doubleValue();
        double cashAmt = CommonUtil.convertObjToDouble(txtCashAmount.getText()).doubleValue();
        txtPurAmount.setText(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(Double.valueOf(purComm + purRet + sundryCred + invest + cashAmt))));
    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtPurAmount", new Boolean(true));
        // mandatoryMap.put("txtPurComm", new Boolean(true));
        // mandatoryMap.put("txtPurchaseRet", new Boolean(true));
        // mandatoryMap.put("txtSundry", new Boolean(true));
        // mandatoryMap.put("txtInvestAcHead", new Boolean(true));
    }

    public java.util.HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    public String getDtPrintValue(String strDate) {
        try {
            System.out.println("strDate getDtPrintValue99999999999999999999999999999999999999=================" + strDate);
            //create SimpleDateFormat object with source string date format
            SimpleDateFormat sdfSource = new SimpleDateFormat("EEE MMM dd  zzz yyyy");
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

    @Override
    public void update(Observable observed, Object arg) {
        System.out.println("observable.getTxtPurAmount() -------" + observable.getTxtPurAmount());
        txtPurAmount.setText(CommonUtil.convertObjToStr(observable.getTxtPurAmount()));
        txtPurComm.setText(CommonUtil.convertObjToStr(observable.getTxtPurComm()));
        txtPurchaseRet.setText(CommonUtil.convertObjToStr(observable.getTxtPurchaseRet()));
        txtSundry.setText(CommonUtil.convertObjToStr(observable.getTxtSundry()));
        txtInvestAcHead.setText(CommonUtil.convertObjToStr(observable.getTxtInvestAcHead()));
        txtCashAmount.setText(CommonUtil.convertObjToStr(observable.getTxtCashAmount()));
        txtSupplier.setText(CommonUtil.convertObjToStr(observable.getTxtSupplier()));
        cboTransMode.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboTransMode()));
        cboTransType.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboTransType()));
        txtNarration.setText(CommonUtil.convertObjToStr(observable.getTxtNarration()));
        txtChequeNo.setText(CommonUtil.convertObjToStr(observable.getTxtChequeNo()));
        txtAccountNumber.setText(CommonUtil.convertObjToStr(observable.getTxtAccountNumber()));
        cboProductId.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboProductId()));
        tdtFromDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtFromDate()));
        lblTotal.setText(CommonUtil.convertObjToStr(observable.getLblTotal()));
        txtTradeNarration.setText(CommonUtil.convertObjToStr(observable.getTxtTradeNarration()));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccountNumber;
    private com.see.truetransact.uicomponent.CButton btnAdd;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnRejection;
    private com.see.truetransact.uicomponent.CButton btnReprint;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSupplier;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane2;
    private com.see.truetransact.uicomponent.CComboBox cboProductId;
    private com.see.truetransact.uicomponent.CComboBox cboTransMode;
    private com.see.truetransact.uicomponent.CComboBox cboTransType;
    private com.see.truetransact.uicomponent.CLabel lbSpace5;
    private com.see.truetransact.uicomponent.CLabel lbSpace6;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblCash1;
    private com.see.truetransact.uicomponent.CLabel lblCashToBeEntered;
    private com.see.truetransact.uicomponent.CLabel lblChequeNo;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblFromServicePeriod;
    private com.see.truetransact.uicomponent.CLabel lblInvestAcHead;
    private com.see.truetransact.uicomponent.CLabel lblMaximumLoanAmount;
    private com.see.truetransact.uicomponent.CLabel lblMsg1;
    private com.see.truetransact.uicomponent.CLabel lblNarration;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblPurReturn;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseAmount;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseComm;
    private com.see.truetransact.uicomponent.CLabel lblReprintDate;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace65;
    private com.see.truetransact.uicomponent.CLabel lblSpace66;
    private com.see.truetransact.uicomponent.CLabel lblSpace67;
    private com.see.truetransact.uicomponent.CLabel lblSpace68;
    private com.see.truetransact.uicomponent.CLabel lblSpace69;
    private com.see.truetransact.uicomponent.CLabel lblSpace7;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace8;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CLabel lblSundry;
    private com.see.truetransact.uicomponent.CLabel lblSupplier;
    private com.see.truetransact.uicomponent.CLabel lblToServicePeriod;
    private com.see.truetransact.uicomponent.CLabel lblTotal;
    private com.see.truetransact.uicomponent.CLabel lblTotal1;
    private com.see.truetransact.uicomponent.CLabel lblTransMode;
    private com.see.truetransact.uicomponent.CLabel lblTransType;
    private com.see.truetransact.uicomponent.CPanel panAccountOpening;
    private com.see.truetransact.uicomponent.CPanel panIMBP;
    private com.see.truetransact.uicomponent.CPanel panIMBP1;
    private com.see.truetransact.uicomponent.CPanel panIndeedData;
    private com.see.truetransact.uicomponent.CPanel panOtherDetails;
    private com.see.truetransact.uicomponent.CPanel panOtherDetails1;
    private com.see.truetransact.uicomponent.CPanel panPurchase;
    private com.see.truetransact.uicomponent.CPanel panReprintDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus1;
    private com.see.truetransact.uicomponent.CRadioButton rdoInvestment;
    private com.see.truetransact.uicomponent.CRadioButton rdoSundry;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransGroup;
    private com.see.truetransact.uicomponent.CTabbedPane tabShare;
    private com.see.truetransact.uicomponent.CTabbedPane tabShare1;
    private com.see.truetransact.uicomponent.CTable tblproduct;
    private com.see.truetransact.uicomponent.CTable tblproductWithAmount;
    private com.see.truetransact.uicomponent.CToolBar tbrIndend;
    private com.see.truetransact.uicomponent.CToolBar tbrIndend2;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtReprintDate;
    private com.see.truetransact.uicomponent.CTextField txtAccountNumber;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtCashAmount;
    private com.see.truetransact.uicomponent.CTextField txtChequeNo;
    private com.see.truetransact.uicomponent.CTextField txtFromWeight;
    private com.see.truetransact.uicomponent.CTextField txtInvestAcHead;
    private com.see.truetransact.uicomponent.CTextField txtNarration;
    private com.see.truetransact.uicomponent.CTextField txtPurAmount;
    private com.see.truetransact.uicomponent.CTextField txtPurComm;
    private com.see.truetransact.uicomponent.CTextField txtPurchaseRet;
    private com.see.truetransact.uicomponent.CTextField txtSundry;
    private com.see.truetransact.uicomponent.CTextField txtSupplier;
    private com.see.truetransact.uicomponent.CTextField txtToWeight;
    private com.see.truetransact.uicomponent.CTextField txtTradeNarration;
    // End of variables declaration//GEN-END:variables
}
