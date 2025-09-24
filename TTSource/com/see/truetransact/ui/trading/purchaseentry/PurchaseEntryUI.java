/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PurchaseEntryUI.java
 *
 * Created on September 12, 2011, 12:08 PM
 */
package com.see.truetransact.ui.trading.purchaseentry;
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
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.Date;
import javax.swing.table.*;
import java.awt.event.*;
import javax.swing.event.*;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
//import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
//import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;

/**
 *
 * @author Revathi L
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
//    AuthorizeListCreditUI CashierauthorizeListUI=null;
//    AuthorizeListDebitUI ManagerauthorizeListUI=null;
    private Date currDate = null;
    boolean isFilled = false;

    /**
     * Creates new form ifrNewBorrowing
     */
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
        txtChequeAmt.setText("");
        txtCashAmount.setAllowNumber(true);
        txtChequeNo.setAllowAll(true);
        txtNarration.setAllowAll(true);
        lblCashToBeEntered.setText("0.0");
        currDt = ClientUtil.getCurrentDate();
        if (tabShare.getSelectedIndex() == 2) {
            btnAuthorize.setEnabled(false);
        }
        currDate = (Date) currDt.clone();
        txtPurchaseretID.setVisible(false);
        lblPurchaseretID.setVisible(false);
        btnPurchaseretID.setVisible(false);
        panPurchaseretID.setVisible(false);
    }

    public void initComponentData() {
        //cboProductId.setModel(observable.getCbmProdId());
        cboTransMode.setModel(observable.getCbmTransMode());
        cboTransType.setModel(observable.getCbmTransType());
//        cboProdID.setEnabled(false);
//        cboProdID.setEditable(false);
//        HashMap whereMap = new HashMap();
//        java.util.List lst1 = ClientUtil.executeQuery("getTradeexpenseTableData", whereMap);
//        HashMap tradeMap = new HashMap();
        if (tabShare.getSelectedIndex() == 2) {
            btnAuthorize.setEnabled(false);
        }
//        for (int j = 0; j < lst1.size();) {
//            int i = 0;
//            while (i < lst1.size()) {
//                tradeMap = (HashMap) lst1.get(j);
//                String date = tradeMap.get("EFFECT_DT").toString();
//                String result = date.split(" ")[0];
//                tblproduct.getModel().setValueAt(result, i, 0);
//                tblproduct.getModel().setValueAt(tradeMap.get("FROM_WEIGHT"), i, 1);
//                tblproduct.getModel().setValueAt(tradeMap.get("TO_WEIGHT"), i, 2);
//                tblproduct.getModel().setValueAt(tradeMap.get("AMOUNT"), i, 3);
//                j++;
//                i++;
//            }
//        }

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

    private void setTableModelListener() {
        try {
            tableModelListener = new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == 0) {
                        //System.out.println((new StringBuilder()).append("Cell ").append(e.getFirstRow()).append(", ").append(e.getColumn()).append(" changed. The new value: ").append(tblproductWithAmount.getModel().getValueAt(e.getFirstRow(), e.getColumn())).toString());
                        int row = e.getFirstRow();
                        //System.out.println((new StringBuilder()).append("e.getFirstRow()").append(e.getFirstRow()).toString());
                        int column = e.getColumn();
                        int selectedRow = tblproductWithAmount.getSelectedRow();
                        String scheme = CommonUtil.convertObjToStr(tblproductWithAmount.getValueAt(tblproductWithAmount.getSelectedRow(), 1));
                        //System.out.println((new StringBuilder()).append("column    ").append(column).toString());
                        double grant = 0;
                        if (column == 4) {
                            TableModel model = tblproductWithAmount.getModel();
                            double sack_no = CommonUtil.convertObjToDouble(tblproductWithAmount.getValueAt(tblproductWithAmount.getSelectedRow(), 4));
                            double amount = CommonUtil.convertObjToDouble(tblproductWithAmount.getValueAt(tblproductWithAmount.getSelectedRow(), 3));
                            //System.out.println((new StringBuilder()).append("acc_no  noOfInsPay").append(amount).append(sack_no).toString());
                            double total = sack_no * amount;
                            tblproductWithAmount.setValueAt(Double.valueOf(total), tblproductWithAmount.getSelectedRow(), 5);
                            for (int i = 0; i < tblproductWithAmount.getRowCount(); i++) {
                                grant = CommonUtil.convertObjToDouble(tblproductWithAmount.getValueAt(i, 5)) + grant;
                            }

                        }
                        lblTotal.setText(CommonUtil.convertObjToStr(Double.valueOf(grant)));
                        if (column == 3) {
                            tblproductWithAmount.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent mouseevent) {
                                }
                            });
                        }
                        boolean chk;
                        if (column == 0) {
                            chk = ((Boolean) tblproductWithAmount.getValueAt(tblproductWithAmount.getSelectedRow(), 0)).booleanValue();
                            String acc_no = CommonUtil.convertObjToStr(tblproductWithAmount.getValueAt(tblproductWithAmount.getSelectedRow(), 3));
                            if (scheme.equals("MDS") && acc_no.indexOf("_") != -1) {
                                acc_no = acc_no.substring(0, acc_no.indexOf("_"));
                            }
                        }
                        if (column == 7) {
                            chk = ((Boolean) tblproductWithAmount.getValueAt(tblproductWithAmount.getSelectedRow(), 0)).booleanValue();
                        }
                    }
                }
            };
            tblproductWithAmount.getModel().addTableModelListener(tableModelListener);
        } catch (Exception e) {
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
        
        btnSave.setName("btnSave");
        lblPurchaseAmount.setName("lblPurchaseAmount");
        txtPurAmount.setName("txtPurAmount");
        lblPurchaseComm.setName("lblPurchaseComm");
        txtPurComm.setName("txtPurComm");
        lblPurReturn.setName("lblPurReturn");
        txtPurchaseRet.setName("txtPurchaseRet");
        lblSundry.setName("lblSundry");
        txtSundry.setName("txtSundry");
        txtChequeAmt.setName("txtInvestAcHead");
        lblChequeAmt.setName("lblInvestAcHead");
    }

    private void setMaxLengths() {
        txtPurAmount.setValidation(new CurrencyValidation(16, 2));
        txtPurComm.setValidation(new CurrencyValidation(16, 2));
        txtPurchaseRet.setValidation(new CurrencyValidation(16, 2));
        txtSundry.setValidation(new CurrencyValidation(16, 2));
        txtChequeAmt.setValidation(new CurrencyValidation(16, 2));
        txtCashAmount.setValidation(new CurrencyValidation(16, 2));
//        HashMap whereMap = new HashMap();
//        java.util.List lst1 = ClientUtil.executeQuery("getTradeexpenseTableData", whereMap);
//        HashMap tradeDataMap = new HashMap();
//        for (int j = 0; j < lst1.size();) {
//            int i = 0;
//            while (i < lst1.size()) {
//                tradeDataMap = (HashMap) lst1.get(j);
//                tradeDataMap = (HashMap) lst1.get(j);
//                String date = tradeDataMap.get("EFFECT_DT").toString();
//                String result = date.split(" ")[0];
//                tblproductWithAmount.getModel().setValueAt(result, i, 0);
//                tblproductWithAmount.getModel().setValueAt(tradeDataMap.get("FROM_WEIGHT"), i, 1);
//                tblproductWithAmount.getModel().setValueAt(tradeDataMap.get("TO_WEIGHT"), i, 2);
//                tblproductWithAmount.getModel().setValueAt(tradeDataMap.get("AMOUNT"), i, 3);
//                j++;
//                i++;
//            }
//        }

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
            if (tabShare.getSelectedIndex() == 0) {
                 HashMap whereMap = new HashMap();
                 if (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                     whereMap.put("AUTHORIZED_STATUS", "AUTHORIZED_STATUS");
                 }else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                     whereMap.put("AUTHORIZED_STATUS", "");
                 }
                viewMap.put(CommonConstants.MAP_NAME, "PurchaseEntry.getSelectPurchaseEntryList");
                viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            } else if (tabShare.getSelectedIndex() == 2) {
                viewMap.put(CommonConstants.MAP_NAME, "PurchaseEntry.getSelectTradeExpenseList");
            }
        }
        if (viewType.equals("SUPPLIER")) {
            HashMap whereMap = new HashMap();
            whereMap.put("BEHAVES_LIKE", "SUNDRY_CREDITORS");
            viewMap.put(CommonConstants.MAP_NAME, "getSupplierLst");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
        if (viewType.equals("ACT_HEAD")) {
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");

        }
        if (viewType.equals("PURCHASE_RET_ID")) {
            HashMap whereMap = new HashMap();
            if (CommonUtil.convertObjToStr(txtSupplier.getText()).length() > 0) {
                whereMap.put("SUPPLIER_ID", txtSupplier.getText());
                viewMap.put(CommonConstants.MAP_NAME, "getTradingPurchaseRetID");
                viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            }else{
                ClientUtil.showMessageWindow("Please Enter Supplier ID...!");
                return;
            }
        }
//        HashMap where = new HashMap();
//        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
//        where.put("PROD_ID", ((ComboBoxModel) cboProdID.getModel()).getKeyForSelected());
//        viewMap.put(CommonConstants.MAP_WHERE, where);
//        where = null;
        if (currField.equals("REPRINT")) {
            HashMap whereMap = new HashMap();
           // whereMap.put("FROM_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtReprintDate.getDateValue())));
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_NAME, "PurchaseEntry.getSelectPurchaseEntryReprintList");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
        new ViewAll(this, viewMap).show();
    }

    public void fillData(Object map) {
        HashMap hash = (HashMap) map;
        isFilled = true;
        if (viewType.equals("SUPPLIER")) {
            txtSupplier.setText(CommonUtil.convertObjToStr(hash.get("SUPPLIER_ID")));
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (rdoSundryCreditors.isSelected() == true) {
                    txtAcHead.setText(CommonUtil.convertObjToStr(hash.get("SUSPENSE_ACCT_NUM")));
                    lblCashToBeEntered.setText(CommonUtil.convertObjToStr(hash.get("AVAILABLE_BALANCE")));
                }
            }
        } else if (viewType.equals("ACT_HEAD")) {
            txtAcHead.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
            lblAccHdDesc.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD DESCRIPTION")));
        }else if (viewType.equals("PURCHASE_RET_ID")) {
            txtPurchaseretID.setText(CommonUtil.convertObjToStr(hash.get("PURCHASE_RET_NO")));
            HashMap purchMap =  new HashMap();
            purchMap.put("SUPPLIER_ID", txtSupplier.getText());
            purchMap.put("PURCHASE_ENTRY_ID", CommonUtil.convertObjToStr(hash.get("VOUCHER_NO")));
            List purchaseEntryLst = ClientUtil.executeQuery("getTradingPurchaseRetDetails", purchMap);
            if (purchaseEntryLst != null && purchaseEntryLst.size() > 0) {
                purchMap = (HashMap) purchaseEntryLst.get(0);
                cboTransType.setSelectedItem(CommonUtil.convertObjToStr(purchMap.get("TRANS_TYPE")));
                if (CommonUtil.convertObjToStr(purchMap.get("CREDIT_FROM")).equals("O")) {
                    rdoOtherBankAc.setSelected(true);
                    txtProdType.setText(CommonUtil.convertObjToStr(purchMap.get("PROD_TYPE")));
                    txtAcHead.setText(CommonUtil.convertObjToStr(purchMap.get("AC_HEAD")));
                    lblAccHdDesc.setText(getAccHeadDesc(txtAcHead.getText()));
                    txtProdID.setVisible(false);
                    lblProdID.setVisible(false);
                } else if (CommonUtil.convertObjToStr(purchMap.get("CREDIT_FROM")).equals("S")) {
                    rdoSundryCreditors.setSelected(true);
                    txtProdType.setText(CommonUtil.convertObjToStr(purchMap.get("PROD_TYPE")));
                    txtProdID.setText(CommonUtil.convertObjToStr(purchMap.get("PROD_ID")));
                    txtAcHead.setText(CommonUtil.convertObjToStr(purchMap.get("AC_HEAD")));
                }
                cboTransType.setEnabled(false);
                txtPurComm.setEnabled(false);
                rdoOtherBankAc.setEnabled(false);
                rdoSundryCreditors.setEnabled(false);
//                txtCashAmount.setText(CommonUtil.convertObjToStr(purchMap.get("CASH_AMT")));
//                txtPurComm.setText(CommonUtil.convertObjToStr(purchMap.get("PURCHASE_COMM_AMT")));
//                txtSundry.setText(CommonUtil.convertObjToStr(purchMap.get("SUNDRY_AMT")));
//                txtChequeAmt.setText(CommonUtil.convertObjToStr(purchMap.get("CHEQUE_AMT")));
//                txtPurAmount.setText(CommonUtil.convertObjToStr(purchMap.get("TOTAL_AMT")));
//                txtChequeNo.setText(CommonUtil.convertObjToStr(purchMap.get("CHEQUE_NO")));
//                txtNarration.setText(CommonUtil.convertObjToStr(purchMap.get("NARRATION")));
//                txtPurchaseRet.setText(CommonUtil.convertObjToStr(purchMap.get("PURCHASE_RET_AMT")));
            }
            purchMap.put("PURCHASE_NO", CommonUtil.convertObjToStr(hash.get("PURCHASE_NO")));
            List purchLst = ClientUtil.executeQuery("getTradingPurchRetTotAmt", purchMap);
            if(purchLst!=null && purchLst.size()>0){
               purchMap = (HashMap) purchLst.get(0); 
               txtPurchaseRet.setText(CommonUtil.convertObjToStr(purchMap.get("TOTAL_AMT")));
               txtPurAmount.setText(CommonUtil.convertObjToStr(purchMap.get("TOTAL_AMT")));
               txtPurchaseRet.setEnabled(true);
            }
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            setButtonEnableDisable();
            observable.getData(hash);
            update();
            //ClientUtil.enableDisable(panPurchase, true);
            if (CommonUtil.convertObjToStr(cboTransMode.getSelectedItem()).equals("Purchase")) {
            if (CommonUtil.convertObjToStr(cboTransType.getSelectedItem()).equals("Transfer")) {
                if (rdoOtherBankAc.isSelected() == true) {
                    txtProdID.setVisible(false);
                    lblProdID.setVisible(false);
                    lblAccHdDesc.setVisible(true);
                    lblAccountHead.setText("Account Head");
                    txtChequeAmt.setEnabled(true);
                    txtChequeNo.setEnabled(true);
                } else {
                    txtProdID.setVisible(true);
                    lblProdID.setVisible(true);
                    lblAccountHead.setText("Account Number");
                    lblAccHdDesc.setVisible(false);
                    txtSundry.setEnabled(true);
                    HashMap actMap = new HashMap();
                    if (txtSupplier.getText().length() > 0) {
                        actMap.put("SUPPLIER_ID", txtSupplier.getText());
                        List actLst = ClientUtil.executeQuery("getSuspenseAcDetails", actMap);
                        if (actLst != null && actLst.size() > 0) {
                            actMap = (HashMap) actLst.get(0);
                            lblCashToBeEntered.setText(CommonUtil.convertObjToStr(actMap.get("AVAILABLE_BALANCE")));
                        }
                    }
                }
            } else {
                rdoOtherBankAc.setEnabled(false);
                rdoSundryCreditors.setEnabled(false);
                ClientUtil.enableDisable(panPurchaseAmtDetails, false);
                txtCashAmount.setEnabled(true);
                txtNarration.setEnabled(true);
            }
            txtPurAmount.setEnabled(false);
            btnSupplier.setEnabled(true);
            txtProdType.setEnabled(false);
            txtProdID.setEnabled(false);
            cboTransMode.setEnabled(false);
            cboTransType.setEnabled(true);
            }else if (CommonUtil.convertObjToStr(cboTransMode.getSelectedItem()).equals("Purchase Return")) {
                ClientUtil.enableDisable(panPurchase, false);
                txtPurchaseRet.setEnabled(true);
                txtNarration.setEnabled(true);
                txtPurchaseretID.setEnabled(true);
                txtSupplier.setEnabled(true);
                btnSupplier.setEnabled(true);
            }
            txtNarration.setEnabled(true);
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                ClientUtil.enableDisable(panPurchase, false);
                btnSupplier.setEnabled(false);
                btnPurchaseretID.setEnabled(false);
                if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                    if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                        displayTransDetail(observable.getProxyReturnMap());
                        observable.setProxyReturnMap(null);
                    }
                }
            } 
        }else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            setButtonEnableDisable();
            observable.getData(hash);
            update();
            ClientUtil.enableDisable(panPurchase, false);
        }
    }
    
    public String getAccHeadDesc(String accHeadID) {
        HashMap map1 = new HashMap();
        map1.put("ACCHD_ID", accHeadID);
        List list1 = ClientUtil.executeQuery("getSelectAcchdDesc", map1);
        if (!list1.isEmpty()) {
            HashMap map2 = new HashMap();
            map2 = (HashMap) list1.get(0);
            String accHeadDesc = map2.get("AC_HD_DESC").toString();
            return accHeadDesc;
        } else {
            return "";
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoTransGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        tabShare = new com.see.truetransact.uicomponent.CTabbedPane();
        panPurchase = new com.see.truetransact.uicomponent.CPanel();
        panIndeedData = new com.see.truetransact.uicomponent.CPanel();
        panPurchaseDetails = new com.see.truetransact.uicomponent.CPanel();
        lblTransMode = new com.see.truetransact.uicomponent.CLabel();
        cboTransMode = new com.see.truetransact.uicomponent.CComboBox();
        lblTransType = new com.see.truetransact.uicomponent.CLabel();
        cboTransType = new com.see.truetransact.uicomponent.CComboBox();
        lblSupplier = new com.see.truetransact.uicomponent.CLabel();
        panSupplier = new com.see.truetransact.uicomponent.CPanel();
        txtSupplier = new com.see.truetransact.uicomponent.CTextField();
        btnSupplier = new com.see.truetransact.uicomponent.CButton();
        panTotalRadioDetails = new com.see.truetransact.uicomponent.CPanel();
        rdoOtherBankAc = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSundryCreditors = new com.see.truetransact.uicomponent.CRadioButton();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        txtProdType = new com.see.truetransact.uicomponent.CTextField();
        lblProdID = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        panAcHead = new com.see.truetransact.uicomponent.CPanel();
        txtAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnAcHead = new com.see.truetransact.uicomponent.CButton();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        lblCashToBeEntered = new com.see.truetransact.uicomponent.CLabel();
        lblAccHdDesc = new com.see.truetransact.uicomponent.CLabel();
        txtProdID = new com.see.truetransact.uicomponent.CTextField();
        lblPurchaseEntryID = new com.see.truetransact.uicomponent.CLabel();
        lblPurchaseEntryIDVal = new com.see.truetransact.uicomponent.CLabel();
        panPurchaseretID = new com.see.truetransact.uicomponent.CPanel();
        txtPurchaseretID = new com.see.truetransact.uicomponent.CTextField();
        btnPurchaseretID = new com.see.truetransact.uicomponent.CButton();
        lblPurchaseretID = new com.see.truetransact.uicomponent.CLabel();
        panPurchaseAmtDetails = new com.see.truetransact.uicomponent.CPanel();
        lblPurchaseAmount = new com.see.truetransact.uicomponent.CLabel();
        lblPurchaseComm = new com.see.truetransact.uicomponent.CLabel();
        txtPurAmount = new com.see.truetransact.uicomponent.CTextField();
        lblPurReturn = new com.see.truetransact.uicomponent.CLabel();
        txtPurchaseRet = new com.see.truetransact.uicomponent.CTextField();
        lblSundry = new com.see.truetransact.uicomponent.CLabel();
        txtChequeAmt = new com.see.truetransact.uicomponent.CTextField();
        lblChequeAmt = new com.see.truetransact.uicomponent.CLabel();
        txtSundry = new com.see.truetransact.uicomponent.CTextField();
        txtPurComm = new com.see.truetransact.uicomponent.CTextField();
        txtCashAmount = new com.see.truetransact.uicomponent.CTextField();
        lblChequeNo = new com.see.truetransact.uicomponent.CLabel();
        txtChequeNo = new com.see.truetransact.uicomponent.CTextField();
        lblNarration = new com.see.truetransact.uicomponent.CLabel();
        txtNarration = new com.see.truetransact.uicomponent.CTextField();
        lblCash1 = new com.see.truetransact.uicomponent.CLabel();
        lblAllignment = new com.see.truetransact.uicomponent.CLabel();
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
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrIndend2 = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace72 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace7 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace76 = new com.see.truetransact.uicomponent.CLabel();
        btnRejection = new com.see.truetransact.uicomponent.CButton();
        lblSpace8 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace77 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        mbrTDSConfig = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMaximumSize(new java.awt.Dimension(855, 600));
        setMinimumSize(new java.awt.Dimension(855, 600));
        setPreferredSize(new java.awt.Dimension(855, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        tabShare.setMinimumSize(new java.awt.Dimension(955, 480));
        tabShare.setPreferredSize(new java.awt.Dimension(955, 480));

        panPurchase.setMaximumSize(new java.awt.Dimension(850, 380));
        panPurchase.setMinimumSize(new java.awt.Dimension(850, 380));
        panPurchase.setPreferredSize(new java.awt.Dimension(850, 380));
        panPurchase.setLayout(new java.awt.GridBagLayout());

        panIndeedData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panIndeedData.setMaximumSize(new java.awt.Dimension(750, 350));
        panIndeedData.setMinimumSize(new java.awt.Dimension(750, 350));
        panIndeedData.setPreferredSize(new java.awt.Dimension(750, 350));
        panIndeedData.setLayout(new java.awt.GridBagLayout());

        panPurchaseDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Process"));
        panPurchaseDetails.setMaximumSize(new java.awt.Dimension(500, 350));
        panPurchaseDetails.setMinimumSize(new java.awt.Dimension(500, 350));
        panPurchaseDetails.setPreferredSize(new java.awt.Dimension(500, 360));
        panPurchaseDetails.setLayout(new java.awt.GridBagLayout());

        lblTransMode.setText("Transaction Mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseDetails.add(lblTransMode, gridBagConstraints);

        cboTransMode.setMinimumSize(new java.awt.Dimension(130, 21));
        cboTransMode.setPreferredSize(new java.awt.Dimension(130, 21));
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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseDetails.add(cboTransMode, gridBagConstraints);

        lblTransType.setText("Transaction Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseDetails.add(lblTransType, gridBagConstraints);

        cboTransType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboTransType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTransTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 32;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseDetails.add(cboTransType, gridBagConstraints);

        lblSupplier.setText("Supplier");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseDetails.add(lblSupplier, gridBagConstraints);

        panSupplier.setMinimumSize(new java.awt.Dimension(125, 23));
        panSupplier.setPreferredSize(new java.awt.Dimension(125, 23));
        panSupplier.setLayout(new java.awt.GridBagLayout());

        txtSupplier.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSupplier.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSupplierFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSupplier.add(txtSupplier, gridBagConstraints);

        btnSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSupplier.setEnabled(false);
        btnSupplier.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSupplier.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSupplier.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupplierActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSupplier.add(btnSupplier, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseDetails.add(panSupplier, gridBagConstraints);

        panTotalRadioDetails.setMinimumSize(new java.awt.Dimension(250, 25));
        panTotalRadioDetails.setPreferredSize(new java.awt.Dimension(250, 25));

        rdoOtherBankAc.setText("Other Bank A/C");
        rdoOtherBankAc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoOtherBankAcActionPerformed(evt);
            }
        });
        panTotalRadioDetails.add(rdoOtherBankAc);

        rdoSundryCreditors.setText("Sundry Creditors");
        rdoSundryCreditors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSundryCreditorsActionPerformed(evt);
            }
        });
        panTotalRadioDetails.add(rdoSundryCreditors);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panPurchaseDetails.add(panTotalRadioDetails, gridBagConstraints);

        lblProdType.setText("Prod Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseDetails.add(lblProdType, gridBagConstraints);

        txtProdType.setMinimumSize(new java.awt.Dimension(150, 21));
        txtProdType.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseDetails.add(txtProdType, gridBagConstraints);

        lblProdID.setText("Prod ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseDetails.add(lblProdID, gridBagConstraints);

        lblAccountHead.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseDetails.add(lblAccountHead, gridBagConstraints);

        panAcHead.setMinimumSize(new java.awt.Dimension(125, 23));
        panAcHead.setPreferredSize(new java.awt.Dimension(125, 23));
        panAcHead.setLayout(new java.awt.GridBagLayout());

        txtAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAcHeadActionPerformed(evt);
            }
        });
        txtAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHead.add(txtAcHead, gridBagConstraints);

        btnAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAcHead.setEnabled(false);
        btnAcHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHead.add(btnAcHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseDetails.add(panAcHead, gridBagConstraints);

        cLabel1.setText("Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseDetails.add(cLabel1, gridBagConstraints);

        lblCashToBeEntered.setForeground(new java.awt.Color(51, 51, 255));
        lblCashToBeEntered.setText("0.0");
        lblCashToBeEntered.setMaximumSize(new java.awt.Dimension(60, 18));
        lblCashToBeEntered.setMinimumSize(new java.awt.Dimension(60, 18));
        lblCashToBeEntered.setPreferredSize(new java.awt.Dimension(60, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseDetails.add(lblCashToBeEntered, gridBagConstraints);

        lblAccHdDesc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblAccHdDesc.setForeground(new java.awt.Color(0, 51, 204));
        lblAccHdDesc.setMinimumSize(new java.awt.Dimension(100, 21));
        lblAccHdDesc.setPreferredSize(new java.awt.Dimension(300, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseDetails.add(lblAccHdDesc, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseDetails.add(txtProdID, gridBagConstraints);

        lblPurchaseEntryID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPurchaseEntryID.setText("Purchase Entry ID");
        lblPurchaseEntryID.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblPurchaseEntryID.setMaximumSize(new java.awt.Dimension(75, 18));
        lblPurchaseEntryID.setMinimumSize(new java.awt.Dimension(75, 18));
        lblPurchaseEntryID.setPreferredSize(new java.awt.Dimension(115, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseDetails.add(lblPurchaseEntryID, gridBagConstraints);

        lblPurchaseEntryIDVal.setForeground(new java.awt.Color(0, 51, 204));
        lblPurchaseEntryIDVal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblPurchaseEntryIDVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblPurchaseEntryIDVal.setMaximumSize(new java.awt.Dimension(10, 18));
        lblPurchaseEntryIDVal.setMinimumSize(new java.awt.Dimension(10, 18));
        lblPurchaseEntryIDVal.setPreferredSize(new java.awt.Dimension(10, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 133;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseDetails.add(lblPurchaseEntryIDVal, gridBagConstraints);

        panPurchaseretID.setMinimumSize(new java.awt.Dimension(125, 23));
        panPurchaseretID.setPreferredSize(new java.awt.Dimension(125, 23));
        panPurchaseretID.setLayout(new java.awt.GridBagLayout());

        txtPurchaseretID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPurchaseretID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurchaseretIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPurchaseretID.add(txtPurchaseretID, gridBagConstraints);

        btnPurchaseretID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPurchaseretID.setEnabled(false);
        btnPurchaseretID.setMaximumSize(new java.awt.Dimension(21, 21));
        btnPurchaseretID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPurchaseretID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPurchaseretID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseretIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPurchaseretID.add(btnPurchaseretID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseDetails.add(panPurchaseretID, gridBagConstraints);

        lblPurchaseretID.setText("Purchase Return ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseDetails.add(lblPurchaseretID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -148;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIndeedData.add(panPurchaseDetails, gridBagConstraints);

        panPurchaseAmtDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Process"));
        panPurchaseAmtDetails.setMaximumSize(new java.awt.Dimension(500, 350));
        panPurchaseAmtDetails.setMinimumSize(new java.awt.Dimension(500, 350));
        panPurchaseAmtDetails.setPreferredSize(new java.awt.Dimension(500, 350));
        panPurchaseAmtDetails.setLayout(new java.awt.GridBagLayout());

        lblPurchaseAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPurchaseAmount.setText("Total Amount");
        lblPurchaseAmount.setMaximumSize(new java.awt.Dimension(110, 16));
        lblPurchaseAmount.setMinimumSize(new java.awt.Dimension(110, 16));
        lblPurchaseAmount.setPreferredSize(new java.awt.Dimension(110, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseAmtDetails.add(lblPurchaseAmount, gridBagConstraints);

        lblPurchaseComm.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPurchaseComm.setText("Purchase Commission");
        lblPurchaseComm.setMaximumSize(new java.awt.Dimension(150, 16));
        lblPurchaseComm.setMinimumSize(new java.awt.Dimension(150, 16));
        lblPurchaseComm.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseAmtDetails.add(lblPurchaseComm, gridBagConstraints);

        txtPurAmount.setEditable(false);
        txtPurAmount.setAllowAll(true);
        txtPurAmount.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseAmtDetails.add(txtPurAmount, gridBagConstraints);

        lblPurReturn.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPurReturn.setText("Purchase Return");
        lblPurReturn.setMaximumSize(new java.awt.Dimension(110, 16));
        lblPurReturn.setMinimumSize(new java.awt.Dimension(110, 16));
        lblPurReturn.setPreferredSize(new java.awt.Dimension(110, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseAmtDetails.add(lblPurReturn, gridBagConstraints);

        txtPurchaseRet.setAllowAll(true);
        txtPurchaseRet.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurchaseRet.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPurchaseRet.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurchaseRetFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseAmtDetails.add(txtPurchaseRet, gridBagConstraints);

        lblSundry.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSundry.setText("Sundry Creditors");
        lblSundry.setMaximumSize(new java.awt.Dimension(110, 16));
        lblSundry.setMinimumSize(new java.awt.Dimension(110, 16));
        lblSundry.setPreferredSize(new java.awt.Dimension(110, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseAmtDetails.add(lblSundry, gridBagConstraints);

        txtChequeAmt.setAllowAll(true);
        txtChequeAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtChequeAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtChequeAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChequeAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseAmtDetails.add(txtChequeAmt, gridBagConstraints);

        lblChequeAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblChequeAmt.setText("Cheque Amount");
        lblChequeAmt.setMaximumSize(new java.awt.Dimension(150, 16));
        lblChequeAmt.setMinimumSize(new java.awt.Dimension(150, 16));
        lblChequeAmt.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseAmtDetails.add(lblChequeAmt, gridBagConstraints);

        txtSundry.setAllowAll(true);
        txtSundry.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSundry.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSundry.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSundryFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseAmtDetails.add(txtSundry, gridBagConstraints);

        txtPurComm.setAllowAll(true);
        txtPurComm.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurComm.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPurComm.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurCommFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseAmtDetails.add(txtPurComm, gridBagConstraints);

        txtCashAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCashAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCashAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseAmtDetails.add(txtCashAmount, gridBagConstraints);

        lblChequeNo.setText("Cheque No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseAmtDetails.add(lblChequeNo, gridBagConstraints);

        txtChequeNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseAmtDetails.add(txtChequeNo, gridBagConstraints);

        lblNarration.setText("Narration");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseAmtDetails.add(lblNarration, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseAmtDetails.add(txtNarration, gridBagConstraints);

        lblCash1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCash1.setText("Cash");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseAmtDetails.add(lblCash1, gridBagConstraints);

        lblAllignment.setForeground(new java.awt.Color(0, 51, 204));
        lblAllignment.setMaximumSize(new java.awt.Dimension(200, 21));
        lblAllignment.setMinimumSize(new java.awt.Dimension(200, 21));
        lblAllignment.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseAmtDetails.add(lblAllignment, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = -148;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIndeedData.add(panPurchaseAmtDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipady = 51;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 14);
        panPurchase.add(panIndeedData, gridBagConstraints);

        tabShare.addTab("Purchase Entry", panPurchase);

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
        gridBagConstraints.gridy = 1;
        getContentPane().add(tabShare, gridBagConstraints);

        panStatus.setMinimumSize(new java.awt.Dimension(850, 22));
        panStatus.setPreferredSize(new java.awt.Dimension(850, 22));
        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        panStatus.add(lblSpace1, new java.awt.GridBagConstraints());

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus, gridBagConstraints);

        lblMsg.setMinimumSize(new java.awt.Dimension(250, 0));
        lblMsg.setPreferredSize(new java.awt.Dimension(250, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        getContentPane().add(panStatus, gridBagConstraints);

        tbrIndend2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        tbrIndend2.setFloatable(false);
        tbrIndend2.setMaximumSize(new java.awt.Dimension(954, 30));
        tbrIndend2.setMinimumSize(new java.awt.Dimension(954, 30));
        tbrIndend2.setPreferredSize(new java.awt.Dimension(954, 30));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setFocusable(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
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

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrIndend2.add(lblSpace72);

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

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrIndend2.add(lblSpace73);

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

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrIndend2.add(lblSpace74);

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

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrIndend2.add(lblSpace75);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setFocusable(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrIndend2.add(btnException);

        lblSpace76.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace76.setText("     ");
        lblSpace76.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrIndend2.add(lblSpace76);

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

        lblSpace77.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace77.setText("     ");
        lblSpace77.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrIndend2.add(lblSpace77);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.setFocusable(false);
        tbrIndend2.add(btnPrint);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        getContentPane().add(tbrIndend2, gridBagConstraints);

        mnuProcess.setText("Process");

        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptView);

        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrTDSConfig.add(mnuProcess);

        setJMenuBar(mbrTDSConfig);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void displayTransDetail(HashMap proxyResultMap) {
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transMode = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        List transIdList = new ArrayList();
        for (int i = 0; i < keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j <= 1) {
                        transId = (String) transMap.get("TRANS_ID");
                        transIdList.add(transId);
                        transMode = "CASH";
                    }
                    cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        cashDisplayStr += "   Account Head : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("BATCH_ID");
                        transIdList.add(transId);
                        transMode = "TRANSFER";
                    }
                    transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        transferDisplayStr += "   Account Head : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
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
        ClientUtil.showMessageWindow("" + displayStr);
    }

    private void btnAddActionPerformed(ActionEvent evt) {
        if (txtFromWeight.getText() == null || txtFromWeight.getText().equalsIgnoreCase("")) {
            ClientUtil.displayAlert("From Weight should not be empty!!!");
            return;
        }
        if (txtToWeight.getText() == null || txtToWeight.getText().equalsIgnoreCase("")) {
            ClientUtil.displayAlert("To Weight should not be empty!!!");
            return;
        }
        if (txtAmount.getText() == null || txtAmount.getText().equalsIgnoreCase("")) {
            ClientUtil.displayAlert("To Weight should not be empty!!!");
            return;
        }
        if (tdtFromDate.getDateValue() == null || tdtFromDate.getDateValue().equalsIgnoreCase("")) {
            ClientUtil.displayAlert("Date should not be empty!!!");
            return;
        }
        HashMap prodTypeMap = new HashMap();
        prodTypeMap.put("FROM_DATE", (CommonUtil.convertObjToStr(tdtFromDate.getDateValue())));
        prodTypeMap.put("FROM_WEIGHT", txtFromWeight.getText());
        prodTypeMap.put("TO_WEIGHT", txtToWeight.getText());
        prodTypeMap.put("AMOUNT", txtAmount.getText());
        try {
//            observable.populateData(prodTypeMap, tblproduct);
        } catch (Exception e) {
            System.err.println((new StringBuilder()).append("Exception ").append(e.toString()).append("Caught").toString());
            e.printStackTrace();
        }
        tdtFromDate.setDateValue("");
        txtFromWeight.setText("");
        txtToWeight.setText("");
        txtAmount.setText("");
    }

   
    
    private void savePerformed() {
        updateOBFields();
        String action;
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ){
            action=CommonConstants.TOSTATUS_INSERT;
            saveAction(action);
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            action=CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            action=CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
        
        //__ Make the Screen Closable..
        setModified(false);
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
      observable.doAction();
                if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                    if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)
                            && observable.getProxyReturnMap().containsKey("PURCHASE_ENTRY_ID")) {
                        ClientUtil.showMessageWindow("Purchase Entry ID : " + observable.getProxyReturnMap().get("PURCHASE_ENTRY_ID"));
                    }
                    if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                        displayTransDetail(observable.getProxyReturnMap());
                        observable.setProxyReturnMap(null);
                    }else if (observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST")) {
                        displayTransDetail(observable.getProxyReturnMap());
                        observable.setProxyReturnMap(null);
                    }
                }
            }
        btnCancelActionPerformed(null);  
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
            date = new Date(sdf2.format(sdf1.parse(date1)));
        } catch (ParseException e) {
            System.out.println("Error in getDateValue():" + e);
        }
        return date;
    }

    public void updateOBFields() {
     observable.setPurchaseEntryID(CommonUtil.convertObjToStr(lblPurchaseEntryIDVal.getText()));   
     observable.setSupplierID(CommonUtil.convertObjToStr(txtSupplier.getText()));
     observable.setCboTransMode(CommonUtil.convertObjToStr(cboTransType.getSelectedItem()));
     observable.setCboTransType(CommonUtil.convertObjToStr(cboTransType.getSelectedItem()));
        if (rdoOtherBankAc.isSelected()) {
            observable.setCreditFrom("O");
        } else if (rdoSundryCreditors.isSelected()) {
            observable.setCreditFrom("S");
        }else{
            observable.setCreditFrom("");
        }
     observable.setProdType(CommonUtil.convertObjToStr(txtProdType.getText()));
     observable.setProdID(CommonUtil.convertObjToStr(txtProdID.getText()));
     observable.setAcHD(CommonUtil.convertObjToStr(txtAcHead.getText()));
     observable.setCash(CommonUtil.convertObjToStr(txtCashAmount.getText()));
     observable.setPurchComm(CommonUtil.convertObjToStr(txtPurComm.getText()));
     observable.setPurchaseRet(CommonUtil.convertObjToStr(txtPurchaseRet.getText()));
     observable.setSundryAmt(CommonUtil.convertObjToStr(txtSundry.getText()));
     observable.setChequeAmt(CommonUtil.convertObjToStr(txtChequeAmt.getText()));
     observable.setTxtChequeNo(CommonUtil.convertObjToStr(txtChequeNo.getText()));
     observable.setTxtNarration(CommonUtil.convertObjToStr(txtNarration.getText()));
     observable.setTotalAmt(CommonUtil.convertObjToStr(txtPurAmount.getText()));
     observable.setPurchaseRetId(CommonUtil.convertObjToStr(txtPurchaseretID.getText()));
    }
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        lblStatus.setText(observable.getLblStatus());
        btnView.setEnabled(!btnView.isEnabled());
    }
    
    private void setEnableDisableForCash() {
        rdoOtherBankAc.setEnabled(false);
        rdoSundryCreditors.setEnabled(false);
        txtCashAmount.setEnabled(true);
        txtChequeAmt.setEnabled(false);
        txtSundry.setEnabled(false);
        txtPurchaseRet.setEnabled(false);
        txtChequeNo.setEnabled(false);
        txtProdType.setEnabled(false);
        txtPurComm.setEnabled(true);
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

private void txtChequeAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChequeAmtFocusLost
// TODO add your handling code here:
    calcTotalPurchase();
}//GEN-LAST:event_txtChequeAmtFocusLost

private void cboTransTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTransTypeActionPerformed
// TODO add your handling code here:
    if (cboTransMode.getSelectedIndex() > 0 && cboTransType.getSelectedIndex() > 0) {
           if (cboTransMode.getSelectedItem().equals("Purchase")) {
               if (cboTransType.getSelectedItem().equals("Transfer")) {
                   txtCashAmount.setEnabled(false);
                   rdoOtherBankAc.setEnabled(true);
                   rdoSundryCreditors.setEnabled(true);
                   txtCashAmount.setText("");
                   txtPurAmount.setText("");
               } else {
                setEnableDisableForCash();
                    if(rdoSundryCreditors.isSelected()==true && txtSundry.getText().length()>0){
                        txtSundry.setText("");
                    }else if(rdoOtherBankAc.isSelected()==true && txtChequeAmt.getText().length()>0){
                        txtChequeAmt.setText("");
                        txtChequeNo.setText("");
                        lblAccHdDesc.setText("");
                    }
                rdoOtherBankAc.setSelected(false);
                rdoSundryCreditors.setSelected(false);
                txtProdType.setText("");
                txtProdID.setText("");
                txtAcHead.setText("");
                txtPurAmount.setText("");
            }
        }else if (cboTransMode.getSelectedItem().equals("Purchase Return")) {
                if (cboTransType.getSelectedItem().equals("Transfer")) {
                   txtPurchaseRet.setEnabled(false);
                   rdoOtherBankAc.setEnabled(true);
                   rdoSundryCreditors.setEnabled(true);
                   txtPurchaseRet.setText("");
                } else {
                   setEnableDisableForCash();
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    if(rdoSundryCreditors.isSelected()==true && txtSundry.getText().length()>0){
                        txtPurchaseRet.setText(txtSundry.getText());
                        txtSundry.setText("");
                        txtProdType.setText("");
                        txtProdID.setText("");
                        txtAcHead.setText("");
                        rdoSundryCreditors.setSelected(false);
                    }else if(rdoOtherBankAc.isSelected()==true && txtChequeAmt.getText().length()>0){
                        txtPurchaseRet.setText(txtChequeAmt.getText());
                        txtChequeAmt.setText("");
                        txtChequeNo.setText("");
                        lblAccHdDesc.setText("");
                        txtProdType.setText("");
                        txtAcHead.setText("");
                    }
                 }
                   txtCashAmount.setEnabled(false);
                   txtPurchaseRet.setEnabled(true);
                   txtPurComm.setEnabled(true);
               }
        }
    }
    //rdoSundry.setEnabled(false); 
}//GEN-LAST:event_cboTransTypeActionPerformed

private void txtCashAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCashAmountFocusLost
// TODO add your handling code here:
    calcTotalPurchase();
}//GEN-LAST:event_txtCashAmountFocusLost

private void cboTransModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTransModeActionPerformed
    if (cboTransMode.getSelectedItem() != null && cboTransMode.getSelectedItem().equals("Purchase Return")) {
        txtPurchaseretID.setVisible(true);
        lblPurchaseretID.setVisible(true);
        btnPurchaseretID.setVisible(true);
        panPurchaseretID.setVisible(true);
        txtPurchaseretID.setEnabled(true);
        btnPurchaseretID.setEnabled(true);
    } else if (cboTransMode.getSelectedItem() != null && cboTransMode.getSelectedItem().equals("Purchase")) {
        txtPurchaseretID.setVisible(false);
        lblPurchaseretID.setVisible(false);
        btnPurchaseretID.setVisible(false);
        panPurchaseretID.setVisible(false);
    }
}//GEN-LAST:event_cboTransModeActionPerformed

private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_btnPrintActionPerformed

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

    private void txtSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProdIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProdIDActionPerformed

    private void txtSupplierFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSupplierFocusLost
        // TODO add your handling code here:
        if (txtSupplier.getText() != null && txtSupplier.getText().length() > 0) {
            // showingCustomerDetails(txtCustomerID.getText());
        }
    }//GEN-LAST:event_txtSupplierFocusLost

    private void btnSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierActionPerformed
        // TODO add your handling code here:
        callView("SUPPLIER");
    }//GEN-LAST:event_btnSupplierActionPerformed

    private void txtAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAcHeadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAcHeadActionPerformed

    private void txtAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcHeadFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAcHeadFocusLost

    private void btnAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcHeadActionPerformed
        // TODO add your handling code here:
        callView("ACT_HEAD");
    }//GEN-LAST:event_btnAcHeadActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_mitCloseActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
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
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectionActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectionActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                    displayTransDetail(observable.getProxyReturnMap());
                    observable.setProxyReturnMap(null);
                } else if (observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST")) {
                    displayTransDetail(observable.getProxyReturnMap());
                    observable.setProxyReturnMap(null);
                }
            }
        }
        btnCancel.setEnabled(true);
        btnRejection.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("PURCHASE_ENTRY_ID", lblPurchaseEntryIDVal.getText());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap);
            viewType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;

        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "PurchaseEntry.getSelectPurchaseEntryList");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    public void authorize(HashMap map) {
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.setAuthorizeMap(map);
            observable.doAction();
            setMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
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
        txtChequeAmt.setText("");
        lblAccHdDesc.setText("");
        lblPurchaseEntryIDVal.setText("");
        supActnum = "";
        sundryActnum = "";
        cboTransMode.setSelectedItem(" ");
        cboTransMode.setSelectedItem("");
        txtPurAmount.setEnabled(false);
        setModified(false);
        //         if (fromAuthorizeUI) {
            //            this.dispose();
            //            fromAuthorizeUI = false;
            //            authorizeListUI.setFocusToTable();
            //        }
        //        if (fromCashierAuthorizeUI) {
            //            this.dispose();
            //            fromCashierAuthorizeUI = false;
            //            CashierauthorizeListUI.setFocusToTable();
            //        }
        //        if (fromManagerAuthorizeUI) {
            //            this.dispose();
            //            fromManagerAuthorizeUI = false;
            //            ManagerauthorizeListUI.setFocusToTable();
            //        }
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (txtSupplier.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Supplier ID should not be empty");
            return;
        } else if (CommonUtil.convertObjToStr(cboTransMode.getSelectedItem()).length() <= 0) {
            ClientUtil.showMessageWindow("Transaction mode should not be empty");
            return;
        } else if (CommonUtil.convertObjToStr(cboTransType.getSelectedItem()).length() <= 0) {
            ClientUtil.showMessageWindow("Transaction Type should not be empty");
            return;
        } else if (CommonUtil.convertObjToStr(cboTransType.getSelectedItem()).equals("Cash") && txtCashAmount.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Cash Amount should not be empty");
            return;
        } else if (CommonUtil.convertObjToStr(cboTransType.getSelectedItem()).equals("Transfer")
                && rdoOtherBankAc.isSelected() == true && txtAcHead.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Account Head should not be empty");
            return;
        } else if (CommonUtil.convertObjToStr(cboTransType.getSelectedItem()).equals("Transfer")
                && rdoOtherBankAc.isSelected() == true && txtChequeAmt.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Cheque amount should not be empty");
            return;
        } else if (CommonUtil.convertObjToStr(cboTransType.getSelectedItem()).equals("Transfer")
                && rdoOtherBankAc.isSelected() == true && txtChequeNo.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Cheque No should not be empty");
            return;
        } else if (CommonUtil.convertObjToStr(cboTransType.getSelectedItem()).equals("Transfer")
                && rdoSundryCreditors.isSelected() == true && txtSundry.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Sundry Credit amount should not be empty");
            return;
        } else {
            savePerformed();
            lblTotal.setText("");
            btnAuthorize.setEnabled(true);
            btnRejection.setEnabled(true);
            btnException.setEnabled(true);
            cboTransMode.setSelectedIndex(0);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

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
        txtAcHead.setEnabled(false);
        txtPurComm.setEnabled(true);
//        txtPurchaseRet.setEnabled(true);
//        txtSundry.setEnabled(true);
//        txtChequeAmt.setEnabled(true);
        //rdoSundry.setEnabled(false);
        txtPurAmount.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
//        tblproductWithAmount.setEnabled(true);
//        tblproduct.setEnabled(true);
//        observable.resetForm();
//        txtPurAmount.setText("");
//        txtPurComm.setText("");
//        txtPurchaseRet.setText("");
//        txtSundry.setText("");
//        txtChequeAmt.setText("");
//        txtCashAmount.setText("");
//        ClientUtil.enableDisable(panPurchase, true);
//        ClientUtil.enableDisable(panOtherDetails, true);
//        ClientUtil.enableDisable(panOtherDetails1, true);
//        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
//        setButtonEnableDisable();
//        btnAuthorize.setEnabled(false);
//        btnRejection.setEnabled(false);
//        btnException.setEnabled(false);
//        btnSave.setEnabled(true);
//        btnCancel.setEnabled(true);
//        rdoOtherBankAc.setEnabled(false);
//        rdoSundryCreditors.setEnabled(false);
//        cboProdID.setEnabled(false);
//        txtTradeNarration.setEnabled(true);
//        txtAcHead.setEnabled(false);
//        btnAcHead.setEnabled(false);
//        txtPurAmount.setEnabled(false);
//        txtSupplier.setEnabled(true);
//        btnSupplier.setEnabled(true);
        ClientUtil.enableDisable(panPurchase, false);
        observable.resetForm();
        setButtonEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        cboTransMode.setEnabled(true);
        cboTransType.setEnabled(true);
        txtSupplier.setEnabled(true);
        btnSupplier.setEnabled(true);
        txtNarration.setEnabled(true);
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed

    private void rdoOtherBankAcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoOtherBankAcActionPerformed
        // TODO add your handling code here:
        if (rdoOtherBankAc.isSelected() == true) {
            rdoSundryCreditors.setSelected(false);
            txtProdType.setEnabled(false);
            txtAcHead.setEnabled(true);
            btnAcHead.setEnabled(true);
            txtProdType.setText("General Ledger");
            txtCashAmount.setEnabled(false);
            txtPurchaseRet.setEnabled(false);
            txtSundry.setEnabled(false);
            txtPurComm.setEnabled(true);
            txtChequeAmt.setEnabled(true);
            txtChequeNo.setEnabled(true);
            lblProdID.setVisible(false);
            txtProdID.setVisible(false);
            txtAcHead.setText("");
            lblAccHdDesc.setVisible(true);
            lblCashToBeEntered.setText("");
            lblAccHdDesc.setText("");
            txtSundry.setText("");
            txtPurAmount.setText("");
//            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
//                if (CommonUtil.convertObjToStr(observable.getCashAmt()).length() > 0) {
//                    txtChequeAmt.setText(CommonUtil.convertObjToStr(observable.getCashAmt()));
//                    txtSundry.setText("");
//                }
//            }
        }
        
    }//GEN-LAST:event_rdoOtherBankAcActionPerformed

    private void rdoSundryCreditorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSundryCreditorsActionPerformed
        // TODO add your handling code here:
        if (rdoSundryCreditors.isSelected() == true) {
            HashMap actMap = new HashMap();
            rdoOtherBankAc.setSelected(false);
            txtProdType.setEnabled(false);
            txtAcHead.setEnabled(true);
            btnAcHead.setEnabled(true);
            txtProdType.setText("Suspense Account");
            if(txtSupplier.getText().length()>0){
            actMap.put("SUPPLIER_ID", txtSupplier.getText());
            }
            List actLst = ClientUtil.executeQuery("getSuspenseAcDetails", actMap);
            if(actLst!=null && actLst.size()>0){
                actMap = (HashMap)actLst.get(0);
                txtProdID.setText(CommonUtil.convertObjToStr(actMap.get("SUSPENSE_PROD_ID")));
                txtAcHead.setText(CommonUtil.convertObjToStr(actMap.get("SUSPENSE_ACCT_NUM")));
            }
            txtAcHead.setEnabled(false);
            lblAccountHead.setText("Account Number");
            lblAccHdDesc.setVisible(false);
            txtCashAmount.setEnabled(false);
            txtPurchaseRet.setEnabled(false);
            txtSundry.setEnabled(true);
            txtChequeAmt.setEnabled(false);
            txtChequeNo.setEnabled(false);
            lblProdID.setVisible(true);
            txtProdID.setVisible(true);
            btnAcHead.setEnabled(false);
            txtChequeAmt.setText("");
            txtChequeNo.setText("");
            txtPurAmount.setText("");
            txtNarration.setEnabled(true);
            
//            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
//                if (CommonUtil.convertObjToStr(observable.getCashAmt()).length() > 0) {
//                    txtSundry.setText(CommonUtil.convertObjToStr(observable.getCashAmt()));
//                    txtChequeAmt.setText("");
//                }
//            }
        }
    }//GEN-LAST:event_rdoSundryCreditorsActionPerformed

    private void txtPurchaseretIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurchaseretIDFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPurchaseretIDFocusLost

    private void btnPurchaseretIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseretIDActionPerformed
        // TODO add your handling code here:
        callView("PURCHASE_RET_ID");
    }//GEN-LAST:event_btnPurchaseretIDActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        callView(ClientConstants.ACTION_STATUS[3]);
    }//GEN-LAST:event_btnViewActionPerformed
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

    private void tblproductWithAmountMouseClicked(MouseEvent mouseevent) {
    }

    private void tblproductWithAmountKeyPressed(KeyEvent keyevent) {
    }

    private void calcTotalPurchase() {
        double purComm = CommonUtil.convertObjToDouble(txtPurComm.getText()).doubleValue();
        double purRet = CommonUtil.convertObjToDouble(txtPurchaseRet.getText()).doubleValue();
        double sundryCred = CommonUtil.convertObjToDouble(txtSundry.getText()).doubleValue();
        double invest = CommonUtil.convertObjToDouble(txtChequeAmt.getText()).doubleValue();
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
    }
    
    public void update() {
      lblPurchaseEntryIDVal.setText(CommonUtil.convertObjToStr(observable.getPurchaseEntryID()));
        txtSupplier.setText(CommonUtil.convertObjToStr(observable.getSupplierID()));
        cboTransMode.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboTransMode()));
        cboTransType.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboTransType()));
        txtProdType.setText(CommonUtil.convertObjToStr(observable.getProdType()));
        txtProdID.setText(CommonUtil.convertObjToStr(observable.getProdID()));
        txtAcHead.setText(CommonUtil.convertObjToStr(observable.getAcHD()));
        lblAccHdDesc.setText(getAccHeadDesc(txtAcHead.getText()));
        txtCashAmount.setText(CommonUtil.convertObjToStr(observable.getCash()));
        txtPurchaseRet.setText(CommonUtil.convertObjToStr(observable.getPurchaseRet()));
        txtSundry.setText(CommonUtil.convertObjToStr(observable.getSundryAmt()));
        txtPurComm.setText(CommonUtil.convertObjToStr(observable.getPurchComm()));
        txtChequeAmt.setText(CommonUtil.convertObjToStr(observable.getChequeAmt()));
        txtPurAmount.setText(CommonUtil.convertObjToStr(observable.getTotalAmt()));
        txtChequeNo.setText(CommonUtil.convertObjToStr(observable.getTxtChequeNo()));
        txtNarration.setText(CommonUtil.convertObjToStr(observable.getTxtNarration()));
        txtPurchaseretID.setText(CommonUtil.convertObjToStr(observable.getPurchaseRetId()));
        if(observable.getCreditFrom().equals("O")){
            rdoOtherBankAc.setSelected(true);
        }else{
            rdoSundryCreditors.setSelected(true);
        }
          
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAcHead;
    private com.see.truetransact.uicomponent.CButton btnAdd;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnPurchaseretID;
    private com.see.truetransact.uicomponent.CButton btnRejection;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSupplier;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane2;
    private com.see.truetransact.uicomponent.CComboBox cboTransMode;
    private com.see.truetransact.uicomponent.CComboBox cboTransType;
    private com.see.truetransact.uicomponent.CLabel lbSpace5;
    private com.see.truetransact.uicomponent.CLabel lbSpace6;
    private com.see.truetransact.uicomponent.CLabel lblAccHdDesc;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAllignment;
    private com.see.truetransact.uicomponent.CLabel lblCash1;
    private com.see.truetransact.uicomponent.CLabel lblCashToBeEntered;
    private com.see.truetransact.uicomponent.CLabel lblChequeAmt;
    private com.see.truetransact.uicomponent.CLabel lblChequeNo;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblFromServicePeriod;
    private com.see.truetransact.uicomponent.CLabel lblMaximumLoanAmount;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNarration;
    private com.see.truetransact.uicomponent.CLabel lblProdID;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblPurReturn;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseAmount;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseComm;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseEntryID;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseEntryIDVal;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseretID;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace7;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblSpace76;
    private com.see.truetransact.uicomponent.CLabel lblSpace77;
    private com.see.truetransact.uicomponent.CLabel lblSpace8;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSundry;
    private com.see.truetransact.uicomponent.CLabel lblSupplier;
    private com.see.truetransact.uicomponent.CLabel lblToServicePeriod;
    private com.see.truetransact.uicomponent.CLabel lblTotal;
    private com.see.truetransact.uicomponent.CLabel lblTotal1;
    private com.see.truetransact.uicomponent.CLabel lblTransMode;
    private com.see.truetransact.uicomponent.CLabel lblTransType;
    private com.see.truetransact.uicomponent.CMenuBar mbrTDSConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAcHead;
    private com.see.truetransact.uicomponent.CPanel panIMBP;
    private com.see.truetransact.uicomponent.CPanel panIMBP1;
    private com.see.truetransact.uicomponent.CPanel panIndeedData;
    private com.see.truetransact.uicomponent.CPanel panOtherDetails;
    private com.see.truetransact.uicomponent.CPanel panOtherDetails1;
    private com.see.truetransact.uicomponent.CPanel panPurchase;
    private com.see.truetransact.uicomponent.CPanel panPurchaseAmtDetails;
    private com.see.truetransact.uicomponent.CPanel panPurchaseDetails;
    private com.see.truetransact.uicomponent.CPanel panPurchaseretID;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSupplier;
    private com.see.truetransact.uicomponent.CPanel panTotalRadioDetails;
    private com.see.truetransact.uicomponent.CRadioButton rdoOtherBankAc;
    private com.see.truetransact.uicomponent.CRadioButton rdoSundryCreditors;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransGroup;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CTabbedPane tabShare;
    private com.see.truetransact.uicomponent.CTabbedPane tabShare1;
    private com.see.truetransact.uicomponent.CTable tblproduct;
    private com.see.truetransact.uicomponent.CTable tblproductWithAmount;
    private com.see.truetransact.uicomponent.CToolBar tbrIndend;
    private com.see.truetransact.uicomponent.CToolBar tbrIndend2;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CTextField txtAcHead;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtCashAmount;
    private com.see.truetransact.uicomponent.CTextField txtChequeAmt;
    private com.see.truetransact.uicomponent.CTextField txtChequeNo;
    private com.see.truetransact.uicomponent.CTextField txtFromWeight;
    private com.see.truetransact.uicomponent.CTextField txtNarration;
    private com.see.truetransact.uicomponent.CTextField txtProdID;
    private com.see.truetransact.uicomponent.CTextField txtProdType;
    private com.see.truetransact.uicomponent.CTextField txtPurAmount;
    private com.see.truetransact.uicomponent.CTextField txtPurComm;
    private com.see.truetransact.uicomponent.CTextField txtPurchaseRet;
    private com.see.truetransact.uicomponent.CTextField txtPurchaseretID;
    private com.see.truetransact.uicomponent.CTextField txtSundry;
    private com.see.truetransact.uicomponent.CTextField txtSupplier;
    private com.see.truetransact.uicomponent.CTextField txtToWeight;
    private com.see.truetransact.uicomponent.CTextField txtTradeNarration;
    // End of variables declaration//GEN-END:variables
}
