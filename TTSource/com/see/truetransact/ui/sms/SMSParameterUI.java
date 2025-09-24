/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * chequeBookUI.java
 *
 * Created on January 19, 2004, 12:57 PM
 */

package com.see.truetransact.ui.sms;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.TTException;

import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uicomponent.CInternalFrame;

import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;

import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;

import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.common.viewphotosign.ViewPhotoSignUI;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import javax.swing.event.ListSelectionListener;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.EnhancedTableModel;
//__ To test the code...
//import com.see.truetransact.ui.supporting.generateseries.GenerateSeries;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.lang.Double;
import java.util.Date;

import javax.swing.table.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 *
 * @author rahul
 */
public class SMSParameterUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    HashMap mandatoryMap;
    SMSParameterOB observable;
    final int EDIT = 0, DELETE = 1, AUTHORIZE = 2, VIEW = 10;
    int viewType = -1;
    private TableModelListener tableModelListener;
//    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    //    final int CHEQUE=0,STOP=1,LEAF=2,REVOKE=3, ENQUIRY=4, ECS=6;
    //    int pan=-1;
    //    int panEditDelete=-1;
    //    int view = -1;
    //    private final int INFO_MESSAGE = 1;
    //    private String chqIssueId = "" ;
    //    String CHEQUEAUTHID = "";
    //    String Status = "";
    //     HashMap calMap= new HashMap();
    boolean isFilled = false;
    private boolean selectMode = false;
    private HashMap returnMap = null;
    //    boolean isAuth = false;
    //    boolean AUTH = false;
    //    private boolean isRevoked = false;
    //    private boolean iEcsRevoked = false;
    //    private String stopStatus = "";
    Date currDt = null;
    private TransDetailsUI transDetails = null;
    SMSParameterMRB objMandatoryRB = null;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sms.SMSParameterRB", ProxyParameters.LANGUAGE);

    /**
     * Creates new form chequeBookUI
     */
    public SMSParameterUI() {
        try {
            //            setSelectedBranchID(selectedBranchId);
            initComponents();
            currDt = ClientUtil.getCurrentDate();
            initSetup();

        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    // To set The Value of the Buttons Depending on the Value or Condition...
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
        btnView.setEnabled(!btnView.isEnabled());
    }

    // To set The Value of the Buttons Depending on the Value or Condition...
    private void setAuthButtonEnableDisable(boolean value) {
        btnAuthorize.setEnabled(value);
        btnReject.setEnabled(value);
        btnException.setEnabled(value);
    }

    private void initSetup() throws Exception {
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setObservable();
        setButtonEnableDisable();              //__ Enables/Disables the necessary buttons and menu items...
        observable.resetForm();
//        initComponentData();
        setMaxLengths();
        ClientUtil.enableDisable(panProductData, false, false, true);
//        initLoanRebateTableData(new HashMap());
        cboShareType.setModel(observable.getCbmShareType());
        tblData.setModel(observable.getTblData());
        cboSMSProducts.setModel(observable.getCbmSMSProducts());
        cboBranch.setModel(observable.getCbmBranch());
    }

    private void setMaxLengths() {
        txtDebitCashAmt.setMaxLength(16);
        txtDebitCashAmt.setValidation(new CurrencyValidation());
        txtCreditCashAmt.setMaxLength(16);
        txtCreditCashAmt.setValidation(new CurrencyValidation());
        txtDebitTransferAmt.setMaxLength(16);
        txtDebitTransferAmt.setValidation(new CurrencyValidation());
        txtCreditTransferAmt.setMaxLength(16);
        txtCreditTransferAmt.setValidation(new CurrencyValidation());
        txtDebitClearingAmt.setMaxLength(16);
        txtDebitClearingAmt.setValidation(new CurrencyValidation());
        txtCreditClearingAmt.setMaxLength(16);
        txtCreditClearingAmt.setValidation(new CurrencyValidation());
        txtRemarks.setMaxLength(32);
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
        btnView.setName("btnView");
        cboProdId.setName("cboProdId");
        cboProdType.setName("cboProdType");
        chkCreditCash.setName("chkCreditCash");
        chkCreditClearing.setName("chkCreditClearing");
        chkCreditTransfer.setName("chkCreditTransfer");
        chkDebitCash.setName("chkDebitCash");
        chkDebitClearing.setName("chkDebitClearing");
        chkDebitTransfer.setName("chkDebitTransfer");
        lblCash.setName("lblCash");
        lblClearing.setName("lblClearing");
        lblCredit.setName("lblCredit");
        lblCreditCashAmt.setName("lblCreditCashAmt");
        lblCreditClearingAmt.setName("lblCreditClearingAmt");
        lblCreditTransferAmt.setName("lblCreditTransferAmt");
        lblDebit.setName("lblDebit");
        lblDebitCashAmt.setName("lblDebitCashAmt");
        lblDebitClearingAmt.setName("lblDebitClearingAmt");
        lblDebitTransferAmt.setName("lblDebitTransferAmt");
        lblMsg.setName("lblMsg");
        lblProdId.setName("lblProdId");
        lblProdType.setName("lblProdType");
        lblRemarks.setName("lblRemarks");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpaces.setName("lblSpaces");
        lblStatus1.setName("lblStatus1");
        lblTransfer.setName("lblTransfer");
        mbrLoanProduct.setName("mbrLoanProduct");
        panAmountParameters.setName("panAmountParameters");
        panChequeBook.setName("panChequeBook");
        panCreditDebit.setName("panCreditDebit");
        panDueAlerts.setName("panDueAlerts");
        panInformationAlert.setName("panInformationAlert");
        panProduct.setName("panProduct");
        panProductData.setName("panProductData");
        panStatus.setName("panStatus");
        panTransactionAlerts.setName("panTransactionAlerts");
        spt1.setName("spt1");
        spt2.setName("spt2");
        spt3.setName("spt3");
        spt4.setName("spt4");
        spt5.setName("spt5");
        spt6.setName("spt6");
        spt7.setName("spt7");
        sptChequeIssue.setName("sptChequeIssue");
        tabChequeBook.setName("tabChequeBook");
        txtCreditCashAmt.setName("txtCreditCashAmt");
        txtCreditClearingAmt.setName("txtCreditClearingAmt");
        txtCreditTransferAmt.setName("txtCreditTransferAmt");
        txtDebitCashAmt.setName("txtDebitCashAmt");
        txtDebitClearingAmt.setName("txtDebitClearingAmt");
        txtDebitTransferAmt.setName("txtDebitTransferAmt");
        txtRemarks.setName("txtRemarks");
    }

    /* Auto Generated Method - internationalize()
     This method used to assign display texts from
     the Resource Bundle File. */
    private void internationalize() {
        //	null.setText(resourceBundle.getString("null"));
        chkDebitClearing.setText(resourceBundle.getString("chkDebitClearing"));
        lblTransfer.setText(resourceBundle.getString("lblTransfer"));
        lblCredit.setText(resourceBundle.getString("lblCredit"));
        chkCreditTransfer.setText(resourceBundle.getString("chkCreditTransfer"));
        chkDebitCash.setText(resourceBundle.getString("chkDebitCash"));
        chkCreditCash.setText(resourceBundle.getString("chkCreditCash"));
        chkDebitTransfer.setText(resourceBundle.getString("chkDebitTransfer"));
        lblClearing.setText(resourceBundle.getString("lblClearing"));
        lblCash.setText(resourceBundle.getString("lblCash"));
        lblDebit.setText(resourceBundle.getString("lblDebit"));
    }

    /* Auto Generated Method - update()
     This method called by Observable. It updates the UI with
     Observable's data. If needed add/Remove RadioButtons
     method need to be added.*/
    public void update(Observable observed, Object arg) {
//        cboProdType.setSelectedItem(observable.getCboProdType());
//        cboProdId.setSelectedItem(observable.getCboProdId());
        chkDebitClearing.setSelected(observable.getChkDebitClearing());
        chkDebitTransfer.setSelected(observable.getChkDebitTransfer());
        chkDebitCash.setSelected(observable.getChkDebitCash());
        chkCreditClearing.setSelected(observable.getChkCreditClearing());
        chkCreditTransfer.setSelected(observable.getChkCreditTransfer());
        chkCreditCash.setSelected(observable.getChkCreditCash());
        txtDebitTransferAmt.setText(observable.getTxtDebitTransferAmt());
        txtCreditClearingAmt.setText(observable.getTxtCreditClearingAmt());
        txtRemarks.setText(observable.getTxtRemarks());
        txtDebitCashAmt.setText(observable.getTxtDebitCashAmt());
        txtCreditTransferAmt.setText(observable.getTxtCreditTransferAmt());
        txtDebitClearingAmt.setText(observable.getTxtDebitClearingAmt());
        txtCreditCashAmt.setText(observable.getTxtCreditCashAmt());
        if (observable.getRdoReminderYes() == true) {// added by nithya
            rdoReminderYes.setSelected(true);
            rdoReminderNo.setSelected(false);
        }
        if (observable.getRdoReminderNo() == true) {// added by nithya
            rdoReminderNo.setSelected(true);
            rdoReminderYes.setSelected(false);
        }
        if (observable.getRdoTxnAllowedYes() == true) {// added by nithya
            rdoTxnAllowedYes.setSelected(true);
            rdoTxnAllowedNo.setSelected(false);
        }
        if (observable.getRdoTxnAllowedNo() == true) {// added by nithya
            rdoTxnAllowedNo.setSelected(true);
            rdoTxnAllowedYes.setSelected(false);
        }
//        txtTxnAllowed.setText(observable.getTxtTxnAllowed());
        cboShareType.setSelectedItem(observable.getCboShareType());
    }

    /* Auto Generated Method - updateOBFields()
     This method called by Save option of UI.
     It updates the OB with UI data.*/
    public void updateOBFields() {
//        observable.setCboProdType((String)cboProdType.getSelectedItem());
//        observable.setCboProdId((String)cboProdId.getSelectedItem());
        observable.setChkDebitClearing(chkDebitClearing.isSelected());
        observable.setChkDebitTransfer(chkDebitTransfer.isSelected());
        observable.setChkDebitCash(chkDebitCash.isSelected());
        observable.setChkCreditClearing(chkCreditClearing.isSelected());
        observable.setChkCreditTransfer(chkCreditTransfer.isSelected());
        observable.setChkCreditCash(chkCreditCash.isSelected());
        observable.setTxtDebitTransferAmt(txtDebitTransferAmt.getText());
        observable.setTxtCreditClearingAmt(txtCreditClearingAmt.getText());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setTxtDebitCashAmt(txtDebitCashAmt.getText());
        observable.setTxtCreditTransferAmt(txtCreditTransferAmt.getText());
        observable.setTxtDebitClearingAmt(txtDebitClearingAmt.getText());
        observable.setTxtCreditCashAmt(txtCreditCashAmt.getText());
        observable.setRdoReminderYes(rdoReminderYes.isSelected()); // Added by nithya 
        observable.setCboShareType((String) cboShareType.getSelectedItem());
        observable.setRdoReminderNo((rdoReminderNo.isSelected())); // Added by nithya
        observable.setRdoTxnAllowedYes(rdoTxnAllowedYes.isSelected()); // Added by nithya 
        observable.setRdoTxnAllowedNo(rdoTxnAllowedNo.isSelected()); // Added by nithya
    }

    /* Auto Generated Method - setMandatoryHashMap()
 
     ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
     This method list out all the Input Fields available in the UI.
     It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("chkDebitClearing", new Boolean(true));
        mandatoryMap.put("chkDebitTransfer", new Boolean(true));
        mandatoryMap.put("chkDebitCash", new Boolean(true));
        mandatoryMap.put("chkCreditClearing", new Boolean(true));
        mandatoryMap.put("chkCreditTransfer", new Boolean(true));
        mandatoryMap.put("chkCreditCash", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("txtDebitTransferAmt", new Boolean(true));
        mandatoryMap.put("txtCreditClearingAmt", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("txtDebitCashAmt", new Boolean(true));
        mandatoryMap.put("txtCreditTransferAmt", new Boolean(true));
        mandatoryMap.put("txtDebitClearingAmt", new Boolean(true));
        mandatoryMap.put("txtCreditCashAmt", new Boolean(true));
    }

    /* Auto Generated Method - getMandatoryHashMap()
     Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /* Auto Generated Method - setHelpMessage()
     This method shows tooltip help for all the input fields
     available in the UI. It needs the Mandatory Resource Bundle
     object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new SMSParameterMRB();
        cboProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdId"));
        chkDebitClearing.setHelpMessage(lblMsg, objMandatoryRB.getString("chkDebitClearing"));
        chkDebitTransfer.setHelpMessage(lblMsg, objMandatoryRB.getString("chkDebitTransfer"));
        chkDebitCash.setHelpMessage(lblMsg, objMandatoryRB.getString("chkDebitCash"));
        chkCreditClearing.setHelpMessage(lblMsg, objMandatoryRB.getString("chkCreditClearing"));
        chkCreditTransfer.setHelpMessage(lblMsg, objMandatoryRB.getString("chkCreditTransfer"));
        chkCreditCash.setHelpMessage(lblMsg, objMandatoryRB.getString("chkCreditCash"));
        cboProdType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdType"));
        txtDebitTransferAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitTransferAmt"));
        txtCreditClearingAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCreditClearingAmt"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
        txtDebitCashAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitCashAmt"));
        txtCreditTransferAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCreditTransferAmt"));
        txtDebitClearingAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitClearingAmt"));
        txtCreditCashAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCreditCashAmt"));
    }

//    public void initLoanRebateTableData(HashMap hmap) {
//        tblData.setModel(new javax.swing.table.DefaultTableModel(
//                setTableData(hmap),
//                new String[]{
//            "Select", "Group Type"
//        }) {
//            Class[] types = new Class[]{
//                java.lang.Boolean.class,
//                java.lang.String.class
//            };
//            boolean[] canEdit = new boolean[]{
//                true, false
//            };
//
//            public Class getColumnClass(int columnIndex) {
//                return types[columnIndex];
//            }
//
//            public boolean isCellEditable(int rowIndex, int columnIndex) {
//
//
//                return canEdit[columnIndex];
//            }
//        });
//
//
//        tblData.setCellSelectionEnabled(true);
//        tblData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
//            public void propertyChange(java.beans.PropertyChangeEvent evt) {
//                //tblDataPropertyChange(evt);
//                //                lblTotalTransAmt.setText(String.valueOf(observable.totSubsidyAmt((DefaultTableModel)tblData.getModel())));
//            }
//        });
//        setTableModelListener();
////        setSizeTallyTableData();
//
//
//
//    }
    public ArrayList populateDataForTable(HashMap hmap) {
        HashMap hashmap = new HashMap();
        ArrayList arrayList = new ArrayList();
        List list = ClientUtil.executeQuery("getEditLoanRebateValues", hmap);
        if (list.size() > 0 && list != null) {
            for (int i = 0; i < list.size(); i++) {
                hashmap = (HashMap) list.get(i);
                arrayList.add(hashmap);
            }

        }
        return arrayList;
    }

    public ArrayList populateData() {

        HashMap hmap = new HashMap();
        ArrayList linkedlist = new ArrayList();
        String fromAccno = "";
        String toAccNo = "";
//        String prod_id=((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString();
        ArrayList alist = null;
        ArrayList tablelist = new ArrayList();
        hmap.put("FROMACT_NUM", fromAccno);
        hmap.put("TOACT_NUM", toAccNo);
//        if(fromAccno.length()>0 && toAccNo.length()>0 || prod_id.length()>0){
        List list;
//            if(fromAccno.length()>0 && toAccNo.length()>0){
        list = ClientUtil.executeQuery("getSelectGroupSMSParameter", hmap);
        hmap = null;
        if (list.size() > 0 && list != null) {
            for (int i = 0; i < list.size(); i++) {
                hmap = (HashMap) list.get(i);
                linkedlist.add(hmap);

            }

        }
//            }
//            else{
//                hmap.put("PROD_ID",prod_id);
//                list= ClientUtil.executeQuery("getPenalAccountNumberForProd", hmap);
//                hmap=null;
//                if(list.size()>0 && list!=null){
//                    for(int i=0;i<list.size();i++){
//                        hmap=(HashMap)list.get(i);
//                        linkedlist.add(hmap);
//                        
//                    }
//                    
//                }
//            }

        hmap = null;
//        }

        System.out.println("@@@@@@@tablelist" + linkedlist);
        return linkedlist;
    }

    private Object[][] setTableData(HashMap hmap) {
        DefaultTableModel tblModel = (DefaultTableModel) tblData.getModel();
        HashMap whereMap = new HashMap();
        ArrayList recoveryList = new ArrayList();
//        if(viewType==ClientConstants.ACTIONTYPE_DELETE || viewType==ClientConstants.ACTIONTYPE_EDIT || viewType==ClientConstants.ACTIONTYPE_AUTHORIZE || viewType==ClientConstants.ACTIONTYPE_VIEW){
//            recoveryList = (ArrayList )populateDataForTable(hmap);
//            System.out.println("####### recoveryList :" +recoveryList);
//        }else{

        if (hmap != null) {
            recoveryList = (ArrayList) populateData();
        }
//        }
        if (recoveryList != null && recoveryList.size() > 0) {

            Object totalList[][] = new Object[recoveryList.size()][2];
            Object totalListRow[] = new Object[2];


            whereMap = new HashMap();
            double total_Demand = 0.0;
            double total_RecoveredAmt = 0.0;
            for (int i = 0; i < recoveryList.size(); i++) {
                whereMap = (HashMap) recoveryList.get(i);
                System.out.println("####### recoveryList : " + i + "" + recoveryList);
                System.out.println("####### whereMap : " + i + "" + whereMap);
                double lamount = CommonUtil.convertObjToDouble(whereMap.get("INTEREST_AMOUNT")).doubleValue();
//                double amt=(double)getNearest((long)(lamount*100), 100)/100;
//                if(viewType==ClientConstants.ACTIONTYPE_EDIT || viewType==ClientConstants.ACTIONTYPE_NEW){

                if (CommonUtil.convertObjToStr(whereMap.get("LOOKUP_DESC")).equals("NONE")) {
                    continue;
                }
                totalList[i][0] = new Boolean(false);
                totalList[i][1] = CommonUtil.convertObjToStr(whereMap.get("LOOKUP_REF_ID"));
//                    totalList[i][2] = CommonUtil.convertObjToStr(whereMap.get("ACCT_NAME"));
//                    totalList[i][3] = CommonUtil.convertObjToStr(whereMap.get("REBATE_INTEREST"));
//                    totalList[i][4] =new Double(amt);

//                }
//                else{
//                    totalList[i][0] = CommonUtil.convertObjToStr(whereMap.get("ACCT_NUM"));
//                    totalList[i][1] = CommonUtil.convertObjToStr(whereMap.get("ACCT_NAME"));
//                    totalList[i][2] = CommonUtil.convertObjToStr(whereMap.get("REBATE_INTEREST"));
//                    //                    totalList[i][3] = CommonUtil.convertObjToStr(whereMap.get("INTEREST_AMOUNT"));
////                    totalList[i][3] =new Double(amt);
//                }

//                if(observable.isActivateNewRecord()){
//                    if(viewType==ClientConstants.ACTIONTYPE_EDIT || viewType==ClientConstants.ACTIONTYPE_NEW){
//                        totalListRow[0] = new Boolean(true);
//                        totalListRow[1]  = CommonUtil.convertObjToStr(whereMap.get("ACCT_NUM"));
//                        totalListRow[2] = CommonUtil.convertObjToStr(whereMap.get("ACCT_NAME"));
//                        totalListRow[3] = CommonUtil.convertObjToStr(whereMap.get("REBATE_INTEREST"));
//                        totalList[i][4] =new Double(amt);
//                        
//                        
//                    }else{
//                        totalListRow[0]  = CommonUtil.convertObjToStr(whereMap.get("ACCT_NUM"));
//                        totalListRow[1] = CommonUtil.convertObjToStr(whereMap.get("ACCT_NAME"));
//                        totalListRow[2] = CommonUtil.convertObjToStr(whereMap.get("REBATE_INTEREST"));
//                        totalList[i][3] =new Double(amt);
//                    }
//                    
//                    
//                    int count =tblData.getRowCount();
//                    
//                    ((DefaultTableModel)tblData.getModel()).insertRow(count,totalListRow);
//                    ((DefaultTableModel)tblData.getModel()).fireTableRowsInserted(0, count);
//                    
//                }

            }

            return totalList;
        }
        return null;
    }

//    private void setTableModelListener() {
//        tableModelListener = new TableModelListener() {
//            public void tableChanged(TableModelEvent e) {
//                if (e.getType() == TableModelEvent.UPDATE) {
//                    //                    System.out.println("Cell " + e.getFirstRow() + ", "
//                    //                    + e.getColumn() + " changed. The new value: "
//                    //                    + tblRecoveryListTally.getModel().getValueAt(e.getFirstRow(),
//                    //                    e.getColumn()));
//                    int row = e.getFirstRow();
//                    int column = e.getColumn();
//                    if (column == 1) {
////                        if(CommonUtil.convertObjToDouble(tblData.getModel().getValueAt(e.getFirstRow(),e.getColumn())).doubleValue()>0){
////                            double demand_Amount =CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 4).toString()).doubleValue();
////                            double recovered_Amount =CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 5).toString()).doubleValue();
//////                            if(demand_Amount<recovered_Amount && recovered_Amount>0){
//////                                ClientUtil.showMessageWindow("Transaction Amount should not Cross Subsidy Amount !!!");
//////                                tblData.setValueAt(tblData.getValueAt(tblData.getSelectedRow(), 4),  tblData.getSelectedRow(), 5);
//////                            }
////                        }
//                        TableModel model = tblData.getModel();
//                        //                        calcTallyListTotal();
//                    }
//                }
//            }
//        };
//        tblData.getModel().addTableModelListener(tableModelListener);
//    }
    private void setObservable() throws Exception {
        System.out.println("$$$$$$$$$$$$$$getSelectedBranchID() : " + getSelectedBranchID());
        observable = new SMSParameterOB();
        observable.addObserver(this);
        cboProdType.setModel(observable.getCbmProdType());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoLeaf = new com.see.truetransact.uicomponent.CButtonGroup();
        panChequeBook = new com.see.truetransact.uicomponent.CPanel();
        lblSpaces = new com.see.truetransact.uicomponent.CLabel();
        tabChequeBook = new com.see.truetransact.uicomponent.CTabbedPane();
        panTransactionAlerts = new com.see.truetransact.uicomponent.CPanel();
        panProductData = new com.see.truetransact.uicomponent.CPanel();
        panProduct = new com.see.truetransact.uicomponent.CPanel();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        panCreditDebit = new com.see.truetransact.uicomponent.CPanel();
        lblClearing = new com.see.truetransact.uicomponent.CLabel();
        lblTransfer = new com.see.truetransact.uicomponent.CLabel();
        lblCash = new com.see.truetransact.uicomponent.CLabel();
        chkDebitClearing = new com.see.truetransact.uicomponent.CCheckBox();
        chkDebitTransfer = new com.see.truetransact.uicomponent.CCheckBox();
        chkDebitCash = new com.see.truetransact.uicomponent.CCheckBox();
        chkCreditClearing = new com.see.truetransact.uicomponent.CCheckBox();
        chkCreditTransfer = new com.see.truetransact.uicomponent.CCheckBox();
        chkCreditCash = new com.see.truetransact.uicomponent.CCheckBox();
        spt1 = new com.see.truetransact.uicomponent.CSeparator();
        spt3 = new com.see.truetransact.uicomponent.CSeparator();
        spt2 = new com.see.truetransact.uicomponent.CSeparator();
        spt4 = new com.see.truetransact.uicomponent.CSeparator();
        spt5 = new com.see.truetransact.uicomponent.CSeparator();
        spt6 = new com.see.truetransact.uicomponent.CSeparator();
        spt7 = new com.see.truetransact.uicomponent.CSeparator();
        lblCredit = new com.see.truetransact.uicomponent.CLabel();
        lblDebit = new com.see.truetransact.uicomponent.CLabel();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        panAmountParameters = new com.see.truetransact.uicomponent.CPanel();
        lblDebitTransferAmt = new com.see.truetransact.uicomponent.CLabel();
        txtDebitTransferAmt = new com.see.truetransact.uicomponent.CTextField();
        lblDebitCashAmt = new com.see.truetransact.uicomponent.CLabel();
        lblCreditClearingAmt = new com.see.truetransact.uicomponent.CLabel();
        txtCreditClearingAmt = new com.see.truetransact.uicomponent.CTextField();
        lblDebitClearingAmt = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        txtDebitCashAmt = new com.see.truetransact.uicomponent.CTextField();
        txtCreditTransferAmt = new com.see.truetransact.uicomponent.CTextField();
        lblCreditTransferAmt = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtDebitClearingAmt = new com.see.truetransact.uicomponent.CTextField();
        lblCreditCashAmt = new com.see.truetransact.uicomponent.CLabel();
        txtCreditCashAmt = new com.see.truetransact.uicomponent.CTextField();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        lblReminder = new com.see.truetransact.uicomponent.CLabel();
        rdoReminderYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoReminderNo = new com.see.truetransact.uicomponent.CRadioButton();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        lblTxnAllowed = new com.see.truetransact.uicomponent.CLabel();
        rdoTxnAllowedYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoTxnAllowedNo = new com.see.truetransact.uicomponent.CRadioButton();
        sptChequeIssue = new com.see.truetransact.uicomponent.CSeparator();
        panDueAlerts = new com.see.truetransact.uicomponent.CPanel();
        panInformationAlert = new com.see.truetransact.uicomponent.CPanel();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        panProductTableData = new com.see.truetransact.uicomponent.CPanel();
        srpData = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panInfo = new com.see.truetransact.uicomponent.CPanel();
        txtAreaDescription = new com.see.truetransact.uicomponent.CTextArea();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnClose1 = new com.see.truetransact.uicomponent.CButton();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        chkMalayalam = new com.see.truetransact.uicomponent.CCheckBox();
        panInfo1 = new com.see.truetransact.uicomponent.CPanel();
        lblCustType = new com.see.truetransact.uicomponent.CLabel();
        cboShareType = new com.see.truetransact.uicomponent.CComboBox();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        cboBranch = new com.see.truetransact.uicomponent.CComboBox();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        cboSMSProducts = new com.see.truetransact.uicomponent.CComboBox();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrLoantProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace51 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace52 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace53 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace54 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace55 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        mbrLoanProduct = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        sptPrint = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(800, 650));
        setPreferredSize(new java.awt.Dimension(800, 650));

        panChequeBook.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panChequeBook.setLayout(new java.awt.GridBagLayout());

        lblSpaces.setMinimumSize(new java.awt.Dimension(3, 15));
        lblSpaces.setPreferredSize(new java.awt.Dimension(3, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panChequeBook.add(lblSpaces, gridBagConstraints);

        panTransactionAlerts.setLayout(new java.awt.GridBagLayout());

        panProductData.setLayout(new java.awt.GridBagLayout());

        panProduct.setMinimumSize(new java.awt.Dimension(550, 431));
        panProduct.setPreferredSize(new java.awt.Dimension(550, 431));
        panProduct.setLayout(new java.awt.GridBagLayout());

        lblProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 80, 4, 4);
        panProduct.add(lblProdId, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(255);
        cboProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(cboProdId, gridBagConstraints);

        panCreditDebit.setMinimumSize(new java.awt.Dimension(282, 90));
        panCreditDebit.setName("cPanel_Five"); // NOI18N
        panCreditDebit.setPreferredSize(new java.awt.Dimension(290, 90));
        panCreditDebit.setLayout(new java.awt.GridBagLayout());

        lblClearing.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblClearing.setText("Clearing");
        lblClearing.setMaximumSize(new java.awt.Dimension(50, 15));
        lblClearing.setMinimumSize(new java.awt.Dimension(50, 15));
        lblClearing.setName("lblClearing"); // NOI18N
        lblClearing.setPreferredSize(new java.awt.Dimension(50, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(lblClearing, gridBagConstraints);

        lblTransfer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTransfer.setText("Transfer");
        lblTransfer.setName("lblTransfer"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(lblTransfer, gridBagConstraints);

        lblCash.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCash.setText("Cash");
        lblCash.setMaximumSize(new java.awt.Dimension(42, 15));
        lblCash.setMinimumSize(new java.awt.Dimension(42, 15));
        lblCash.setName("lblCash"); // NOI18N
        lblCash.setPreferredSize(new java.awt.Dimension(42, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(lblCash, gridBagConstraints);

        chkDebitClearing.setName("chkDebitClearing"); // NOI18N
        chkDebitClearing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDebitClearingActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(chkDebitClearing, gridBagConstraints);

        chkDebitTransfer.setName("chkCreditTransfer"); // NOI18N
        chkDebitTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDebitTransferActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(chkDebitTransfer, gridBagConstraints);

        chkDebitCash.setName("chkCreditCash"); // NOI18N
        chkDebitCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDebitCashActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(chkDebitCash, gridBagConstraints);

        chkCreditClearing.setName("chkDebitClearing"); // NOI18N
        chkCreditClearing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCreditClearingActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(chkCreditClearing, gridBagConstraints);

        chkCreditTransfer.setName("chkDebitTransfer"); // NOI18N
        chkCreditTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCreditTransferActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(chkCreditTransfer, gridBagConstraints);

        chkCreditCash.setName("chkDebitCash"); // NOI18N
        chkCreditCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCreditCashActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(chkCreditCash, gridBagConstraints);

        spt1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panCreditDebit.add(spt1, gridBagConstraints);

        spt3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panCreditDebit.add(spt3, gridBagConstraints);

        spt2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panCreditDebit.add(spt2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panCreditDebit.add(spt4, gridBagConstraints);

        spt5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panCreditDebit.add(spt5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panCreditDebit.add(spt6, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panCreditDebit.add(spt7, gridBagConstraints);

        lblCredit.setText("Credit");
        lblCredit.setName("lblCredit"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 8);
        panCreditDebit.add(lblCredit, gridBagConstraints);

        lblDebit.setText("Debit");
        lblDebit.setName("lblDebit"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(lblDebit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(panCreditDebit, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 80, 4, 4);
        panProduct.add(lblProdType, gridBagConstraints);

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(110);
        cboProdType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboProdTypeItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(cboProdType, gridBagConstraints);

        panAmountParameters.setBorder(javax.swing.BorderFactory.createTitledBorder("Minimum Amount Limit for Sending Alerts"));
        panAmountParameters.setMinimumSize(new java.awt.Dimension(265, 240));
        panAmountParameters.setPreferredSize(new java.awt.Dimension(265, 240));
        panAmountParameters.setLayout(new java.awt.GridBagLayout());

        lblDebitTransferAmt.setText("Debit Transfer Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmountParameters.add(lblDebitTransferAmt, gridBagConstraints);

        txtDebitTransferAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDebitTransferAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDebitTransferAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmountParameters.add(txtDebitTransferAmt, gridBagConstraints);

        lblDebitCashAmt.setText("Debit Cash Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmountParameters.add(lblDebitCashAmt, gridBagConstraints);

        lblCreditClearingAmt.setText("Credit Clearing Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmountParameters.add(lblCreditClearingAmt, gridBagConstraints);

        txtCreditClearingAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmountParameters.add(txtCreditClearingAmt, gridBagConstraints);

        lblDebitClearingAmt.setText("Debit Clearing Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmountParameters.add(lblDebitClearingAmt, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmountParameters.add(txtRemarks, gridBagConstraints);

        txtDebitCashAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmountParameters.add(txtDebitCashAmt, gridBagConstraints);

        txtCreditTransferAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmountParameters.add(txtCreditTransferAmt, gridBagConstraints);

        lblCreditTransferAmt.setText("Credit Transfer Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmountParameters.add(lblCreditTransferAmt, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmountParameters.add(lblRemarks, gridBagConstraints);

        txtDebitClearingAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmountParameters.add(txtDebitClearingAmt, gridBagConstraints);

        lblCreditCashAmt.setText("Credit Cash Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmountParameters.add(lblCreditCashAmt, gridBagConstraints);

        txtCreditCashAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmountParameters.add(txtCreditCashAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(panAmountParameters, gridBagConstraints);

        cPanel1.setLayout(new java.awt.GridBagLayout());

        lblReminder.setText("Reminder");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        cPanel1.add(lblReminder, gridBagConstraints);

        rdoReminderYes.setText("Yes");
        rdoReminderYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoReminderYesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        cPanel1.add(rdoReminderYes, gridBagConstraints);

        rdoReminderNo.setText("No");
        rdoReminderNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoReminderNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        cPanel1.add(rdoReminderNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        panProduct.add(cPanel1, gridBagConstraints);

        cPanel2.setLayout(new java.awt.GridBagLayout());

        lblTxnAllowed.setText("Txn Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        cPanel2.add(lblTxnAllowed, gridBagConstraints);

        rdoTxnAllowedYes.setText("Yes");
        rdoTxnAllowedYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTxnAllowedYesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        cPanel2.add(rdoTxnAllowedYes, gridBagConstraints);

        rdoTxnAllowedNo.setText("No");
        rdoTxnAllowedNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTxnAllowedNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        cPanel2.add(rdoTxnAllowedNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        panProduct.add(cPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(panProduct, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTransactionAlerts.add(panProductData, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransactionAlerts.add(sptChequeIssue, gridBagConstraints);

        tabChequeBook.addTab("Transaction Alerts", panTransactionAlerts);

        panDueAlerts.setLayout(new java.awt.GridBagLayout());
        tabChequeBook.addTab("Due Alerts", panDueAlerts);

        panInformationAlert.setLayout(new java.awt.GridBagLayout());

        panTable.setMaximumSize(new java.awt.Dimension(650, 150));
        panTable.setMinimumSize(new java.awt.Dimension(700, 310));
        panTable.setPreferredSize(new java.awt.Dimension(700, 310));
        panTable.setLayout(new java.awt.GridBagLayout());

        chkSelectAll.setText("Select All");
        chkSelectAll.setMinimumSize(new java.awt.Dimension(91, 27));
        chkSelectAll.setPreferredSize(new java.awt.Dimension(91, 27));
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTable.add(chkSelectAll, gridBagConstraints);

        panProductTableData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panProductTableData.setMinimumSize(new java.awt.Dimension(710, 300));
        panProductTableData.setPreferredSize(new java.awt.Dimension(710, 300));
        panProductTableData.setLayout(new java.awt.GridBagLayout());

        srpData.setMinimumSize(new java.awt.Dimension(610, 300));
        srpData.setPreferredSize(new java.awt.Dimension(610, 300));

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Share No", "Mobile No", "Name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblData.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 331));
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataMouseClicked(evt);
            }
        });
        srpData.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        panProductTableData.add(srpData, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTable.add(panProductTableData, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInformationAlert.add(panTable, gridBagConstraints);

        panInfo.setMinimumSize(new java.awt.Dimension(710, 125));
        panInfo.setPreferredSize(new java.awt.Dimension(710, 125));
        panInfo.setLayout(new java.awt.GridBagLayout());

        txtAreaDescription.setLineWrap(true);
        txtAreaDescription.setMinimumSize(new java.awt.Dimension(315, 95));
        txtAreaDescription.setPreferredSize(new java.awt.Dimension(325, 95));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panInfo.add(txtAreaDescription, gridBagConstraints);

        cLabel1.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInfo.add(cLabel1, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.setMaximumSize(new java.awt.Dimension(100, 35));
        btnClear.setMinimumSize(new java.awt.Dimension(90, 35));
        btnClear.setPreferredSize(new java.awt.Dimension(90, 35));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInfo.add(btnClear, gridBagConstraints);

        btnClose1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose1.setText("Close");
        btnClose1.setMaximumSize(new java.awt.Dimension(100, 35));
        btnClose1.setMinimumSize(new java.awt.Dimension(90, 35));
        btnClose1.setPreferredSize(new java.awt.Dimension(90, 35));
        btnClose1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClose1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInfo.add(btnClose1, gridBagConstraints);

        btnProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/send_sms.jpg"))); // NOI18N
        btnProcess.setText("PROCESS");
        btnProcess.setMaximumSize(new java.awt.Dimension(150, 35));
        btnProcess.setMinimumSize(new java.awt.Dimension(135, 35));
        btnProcess.setPreferredSize(new java.awt.Dimension(135, 35));
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInfo.add(btnProcess, gridBagConstraints);

        chkMalayalam.setText("Regional Language");
        chkMalayalam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMalayalamActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panInfo.add(chkMalayalam, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInformationAlert.add(panInfo, gridBagConstraints);

        panInfo1.setMinimumSize(new java.awt.Dimension(700, 40));
        panInfo1.setPreferredSize(new java.awt.Dimension(700, 40));
        panInfo1.setLayout(new java.awt.GridBagLayout());

        lblCustType.setText("Product");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInfo1.add(lblCustType, gridBagConstraints);

        cboShareType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboShareTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInfo1.add(cboShareType, gridBagConstraints);

        cLabel2.setText("Branch Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panInfo1.add(cLabel2, gridBagConstraints);

        cboBranch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBranchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panInfo1.add(cboBranch, gridBagConstraints);

        cLabel3.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panInfo1.add(cLabel3, gridBagConstraints);

        cboSMSProducts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSMSProductsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panInfo1.add(cboSMSProducts, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInformationAlert.add(panInfo1, gridBagConstraints);

        tabChequeBook.addTab("Information Alert", panInformationAlert);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panChequeBook.add(tabChequeBook, gridBagConstraints);

        getContentPane().add(panChequeBook, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus1.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrLoantProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnNew);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace52);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrLoantProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnSave);

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace53);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrLoantProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnAuthorize);

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace55);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrLoantProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnPrint);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace56);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnClose);

        getContentPane().add(tbrLoantProduct, java.awt.BorderLayout.NORTH);

        mnuProcess.setText("Process");

        mitNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNew.setMnemonic('N');
        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptProcess);

        mitSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitSave.setMnemonic('S');
        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitPrint.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        mitPrint.setMnemonic('P');
        mitPrint.setText("Print");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);
        mnuProcess.add(sptPrint);

        mitClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mitClose.setMnemonic('l');
        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrLoanProduct.add(mnuProcess);

        setJMenuBar(mbrLoanProduct);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
        observable.setSelectAll(tblData, new Boolean(chkSelectAll.isSelected()));
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void chkCreditClearingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCreditClearingActionPerformed
        // TODO add your handling code here:
        if (chkCreditClearing.isSelected()) {
            txtCreditClearingAmt.setEnabled(true);
        } else {
            txtCreditClearingAmt.setEnabled(false);
            txtCreditClearingAmt.setText("");
        }
    }//GEN-LAST:event_chkCreditClearingActionPerformed

    private void chkDebitClearingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDebitClearingActionPerformed
        // TODO add your handling code here:
        if (chkDebitClearing.isSelected()) {
            txtDebitClearingAmt.setEnabled(true);
        } else {
            txtDebitClearingAmt.setEnabled(false);
            txtDebitClearingAmt.setText("");
        }
    }//GEN-LAST:event_chkDebitClearingActionPerformed

    private void chkCreditTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCreditTransferActionPerformed
        // TODO add your handling code here:
        if (chkCreditTransfer.isSelected()) {
            txtCreditTransferAmt.setEnabled(true);
        } else {
            txtCreditTransferAmt.setEnabled(false);
            txtCreditTransferAmt.setText("");
        }
    }//GEN-LAST:event_chkCreditTransferActionPerformed

    private void chkDebitTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDebitTransferActionPerformed
        // TODO add your handling code here:
        if (chkDebitTransfer.isSelected()) {
            txtDebitTransferAmt.setEnabled(true);
        } else {
            txtDebitTransferAmt.setEnabled(false);
            txtDebitTransferAmt.setText("");
        }
    }//GEN-LAST:event_chkDebitTransferActionPerformed

    private void chkCreditCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCreditCashActionPerformed
        // TODO add your handling code here:
        if (chkCreditCash.isSelected()) {
            txtCreditCashAmt.setEnabled(true);
        } else {
            txtCreditCashAmt.setEnabled(false);
            txtCreditCashAmt.setText("");
        }
    }//GEN-LAST:event_chkCreditCashActionPerformed

    private void chkDebitCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDebitCashActionPerformed
        // TODO add your handling code here:
        if (chkDebitCash.isSelected()) {
            txtDebitCashAmt.setEnabled(true);
        } else {
            txtDebitCashAmt.setEnabled(false);
            txtDebitCashAmt.setText("");
        }
    }//GEN-LAST:event_chkDebitCashActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);//Sets the Action Type to be performed...
        popUp(VIEW);
    }//GEN-LAST:event_btnViewActionPerformed

    private void txtDebitTransferAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDebitTransferAmtFocusLost
    }//GEN-LAST:event_txtDebitTransferAmtFocusLost

    private void cboProdTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboProdTypeItemStateChanged
        observable.setCboProdType(CommonUtil.convertObjToStr(cboProdType.getSelectedItem()));
        if (observable.getCboProdType().length() > 1) {
            final String type = CommonUtil.convertObjToStr((((ComboBoxModel) (cboProdType).getModel())).getKeyForSelected());
            //            System.out.println("ProdTypeTO" + observable.getProdTypeTO());
//            if(observable.getActionType()== ClientConstants.ACTIONTYPE_NEW||observable.getActionType()== ClientConstants.ACTIONTYPE_VIEW){
            observable.getProductID(type);
//                observable.ttNotifyObservers();
            cboProdId.setModel(observable.getCbmProdId());
//            }
        }
    }//GEN-LAST:event_cboProdTypeItemStateChanged

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if (viewType == EDIT || viewType == DELETE || viewType == AUTHORIZE || viewType == VIEW) {
            isFilled = true;
            observable.populateOB(hash);
            if (viewType == EDIT) {
                ClientUtil.enableDisable(panProductData, true, false, true);
                setButtonEnableDisable();
                btnSave.setEnabled(true);
                setAuthButtonEnableDisable(false);
                chkDebitCashActionPerformed(null);
                chkCreditCashActionPerformed(null);
                chkDebitTransferActionPerformed(null);
                chkCreditTransferActionPerformed(null);
                chkDebitClearingActionPerformed(null);
                chkCreditClearingActionPerformed(null);
            } else {
                ClientUtil.enableDisable(panProductData, false, false, true);
                setButtonEnableDisable();
                btnSave.setEnabled(false);
                setAuthButtonEnableDisable(false);
                if (viewType == AUTHORIZE) {
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
                        btnAuthorize.setEnabled(true);
                    } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                        btnReject.setEnabled(true);
                    } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
                        btnException.setEnabled(true);
                    }
                }
            }
            if (viewType != VIEW) {
                setModified(true);
            }
        }
        cboProdType.setEnabled(false);
        cboProdId.setEnabled(false);
    }

    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            HashMap authorizeMap = new HashMap();
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            observable.setAuthorizeMap(authorizeMap);
            observable.doAction();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                btnCancelActionPerformed(null);
                observable.setResultStatus();
            }
        } else {
            isFilled = false;
            popUp(AUTHORIZE);
            viewType = AUTHORIZE;
        }
    }
    private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && observable.checkProdIdExistance()) {
            ClientUtil.showAlertWindow("SMS Parameters for this Product already exists!!!");
            cboProdId.setSelectedItem("");
        }
    }//GEN-LAST:event_cboProdIdActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        observable.resetForm();
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        setAuthButtonEnableDisable(true);
        ClientUtil.enableDisable(panProductData, false, false, true);
        setModified(false);
        isFilled = false;
        viewType = -1;
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        updateOBFields();
        observable.doAction(); //__ To perform the necessary operation depending on the Action type...
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
//            observable.resetForm();
            btnCancelActionPerformed(null);
            observable.setResultStatus();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);//Sets the Action Type to be performed...
        popUp(DELETE);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);//Sets the Action Type to be performed...
        popUp(EDIT);
    }//GEN-LAST:event_btnEditActionPerformed

    // To display the All the Product Id's which r having status as
    // created or updated, in a table...
    private void popUp(int field) {
        ViewAll viewAll;
        final HashMap viewMap = new HashMap();

        viewMap.put(CommonConstants.MAP_NAME, "getSelectSMSParameterTO");

        if (field == AUTHORIZE) {
            HashMap whereListMap = new HashMap();
            whereListMap.put("AUTHORIZE_STATUS", "IS NULL");
            viewMap.put(CommonConstants.MAP_WHERE, whereListMap);

        }
        viewType = field;

//        viewAll.setTitle(title);
        viewAll = new ViewAll(this, viewMap);
        viewAll.show();
    }

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);   //Sets the Action Type to be performed...
        observable.resetForm();
        ClientUtil.enableDisable(panProductData, true, false, true);
        setButtonEnableDisable();
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // Add your handling code here:
        btnPrintActionPerformed(evt);
    }//GEN-LAST:event_mitPrintActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void rdoReminderYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoReminderYesActionPerformed
        // TODO add your handling code here:
        rdoReminderNo.setSelected(false); // Added by nithya
    }//GEN-LAST:event_rdoReminderYesActionPerformed

    private void rdoReminderNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoReminderNoActionPerformed
        // TODO add your handling code here:
        rdoReminderYes.setSelected(false); // Added by nithya
    }//GEN-LAST:event_rdoReminderNoActionPerformed

    private void rdoTxnAllowedYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTxnAllowedYesActionPerformed
        // TODO add your handling code here:
        rdoTxnAllowedYes.setSelected(true);
        rdoTxnAllowedNo.setSelected(false);
    }//GEN-LAST:event_rdoTxnAllowedYesActionPerformed

    private void rdoTxnAllowedNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTxnAllowedNoActionPerformed
        // TODO add your handling code here:
        rdoTxnAllowedYes.setSelected(false);
        rdoTxnAllowedNo.setSelected(true);
    }//GEN-LAST:event_rdoTxnAllowedNoActionPerformed

    private void cboShareTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboShareTypeActionPerformed
        // TODO add your handling code here:
        if (!cboShareType.getSelectedItem().equals("")) {
            displayInterestDetails();
        }
    }//GEN-LAST:event_cboShareTypeActionPerformed

    private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseClicked
        // TODO add your handling code here:
        if (selectMode == true) {
            String st = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 0));
            if (st.equals("true")) {
                tblData.setValueAt(new Boolean(false), tblData.getSelectedRow(), 0);
            } else {
                tblData.setValueAt(new Boolean(true), tblData.getSelectedRow(), 0);
                if(CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 2)).length() == 0){
                    tblData.setValueAt(new Boolean(false), tblData.getSelectedRow(), 0);
                }
            }
        }
        if (evt.getClickCount() == 2) {
            //            HashMap returnMap = new HashMap();
            if (returnMap != null) {
                //                returnMap = observable.getProxyReturnMap();
                if (returnMap.containsKey(tblData.getValueAt(
                        tblData.getSelectedRow(), 1))) {
//            TTException exception = (TTException) returnMap.get(tblData.getValueAt(
//                tblData.getSelectedRow(), 1));
//        parseException.logException(exception, true);
                }
            }
        }
    }//GEN-LAST:event_tblDataMouseClicked

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
//        ClientUtil.enableDisable(panProductTableData, false);
        ClientUtil.clearAll(this);
//        btnCalculate.setEnabled(true);
        btnProcess.setEnabled(false);
        chkSelectAll.setEnabled(false);
        cboShareType.setEnabled(true);
        cboBranch.setEnabled(true);// Added by nithya on 26-11-2019 for KD-721
        cboSMSProducts.setEnabled(true);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnClose1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnClose1ActionPerformed

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        String description = CommonUtil.convertObjToStr(txtAreaDescription.getText());
        try {
            ArrayList alist = new ArrayList();
            HashMap finalMap = null;
//            String command=observable.getCommand();
            for (int i = 0; i < tblData.getRowCount(); i++) {
                String select = CommonUtil.convertObjToStr(tblData.getValueAt(i, 0));
                System.out.println("select######" + select);
                finalMap = new HashMap();

//                 if(viewType==ClientConstants.ACTIONTYPE_NEW){
                if (select.equals("true")) {
//                    finalMap.put("CUSTOMER_GROUP", tblData.getValueAt(i, 1));
                    finalMap.put("CUSTOMER_LIST", tblData.getValueAt(i, 1));
                    finalMap.put("PHONE_NUMBER", tblData.getValueAt(i, 2));
//                    finalMap.put("MESSAGE", description);
                    finalMap.put("MESSAGE", description);
                    alist.add(finalMap);
                }
//                }


            }
            if (alist != null && alist.size() > 0) {
                if (description.length() == 0) {
                    ClientUtil.displayAlert("Please Enter Description Then  send SMS");
                    return;
                }
                observable.doActionPerform(alist);
//                initLoanRebateTableData(null);
//                ClientUtil.displayAlert("Sent SMS SuccessFully");
                ClientUtil.enableDisable(this, false);
                btnProcess.setEnabled(false);
                txtAreaDescription.setText("");

                if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                    returnMap = observable.getProxyReturnMap();
                    System.out.println("#$#$$# returnMap:" + returnMap);
                    List errorList = (ArrayList) returnMap.get("INTEREST_DATA");
                    List transList = new ArrayList();
                    if (errorList != null && errorList.size() > 0) {
                        for (int j = 0; j < errorList.size(); j++) {
                            if (errorList.get(j) instanceof HashMap) {
                                returnMap = (HashMap) errorList.get(j);
                            } else {
                                transList.add(errorList.get(j));
                            }
                        }
                    }
                    ArrayList head = observable.getTableTitle();
                    ArrayList title = new ArrayList();
                    title.addAll(head);
                    title.add("Status");
                    title.remove(0);
                    ArrayList rowList = null;
                    EnhancedTableModel tbm = observable.getTblData();
                    ArrayList data = tbm.getDataArrayList();
                    for (int i = 0; i < tblData.getRowCount(); i++) {
                        rowList = (ArrayList) data.get(i);
                        if (returnMap.containsKey(tblData.getValueAt(i, 1))) {
                            rowList.add("Error");
                        } else {
                            if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
                                rowList.add("Completed");
                            } else {
                                rowList.add("Not Processed");
                            }
                        }
                        rowList.remove(0);
                    }
                    tbm.setDataArrayList(data, title);
                    javax.swing.table.TableColumn col = tblData.getColumn(tblData.getColumnName(0));
                    col.setMaxWidth(200);
                    col = tblData.getColumn(tblData.getColumnName(1));
                    col.setMaxWidth(250);
                    col = tblData.getColumn(tblData.getColumnName(2));
                    col.setMaxWidth(200);
                    col = tblData.getColumn(tblData.getColumnName(3));
                    col.setMaxWidth(200);
                }
            } else {
                ClientUtil.displayAlert("Please Choose Any One Customer Group Then  send SMS");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnProcessActionPerformed

    private void cboSMSProductsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSMSProductsActionPerformed
        // TODO add your handling code here:
        if(cboSMSProducts.getSelectedIndex() > 0 && CommonUtil.convertObjToStr(cboSMSProducts.getSelectedItem()).length() > 0){
          cboShareType.setModel(observable.getCbmShareType());
          String prodType = (String) (((ComboBoxModel) (cboSMSProducts).getModel()).getKeyForSelected());  
          String selectedBranch = (String) (((ComboBoxModel) (cboBranch).getModel()).getKeyForSelected());  
          Boolean dataExists = observable.populateShareTypeCombo(prodType,selectedBranch);
            if (dataExists) {
                cboShareType.setModel(observable.getCbmShareType());
            } else {
                cboShareType.removeAllItems();
            }
        }
//        else{
//            cboShareType.removeAllItems();
//            cboShareType.setModel(observable.getCbmShareType());
//        }
    }//GEN-LAST:event_cboSMSProductsActionPerformed

    private void cboBranchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBranchActionPerformed
        // TODO add your handling code here:
        cboShareType.setModel(observable.getCbmShareType());
        cboSMSProducts.setModel(observable.getCbmSMSProducts());   
    }//GEN-LAST:event_cboBranchActionPerformed

    private void chkMalayalamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMalayalamActionPerformed
        // TODO add your handling code here:
        if(chkMalayalam.isSelected()){
            txtAreaDescription.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18));
        }else{
            txtAreaDescription.setFont(new java.awt.Font("Tahoma", 0, 12));
        }
        
    }//GEN-LAST:event_chkMalayalamActionPerformed
    private void displayInterestDetails() {
        selectMode = true;
        HashMap dataMap = new HashMap();
        dataMap.put("DO_TRANSACTION", new Boolean(false));
        dataMap.put("CHARGES_PROCESS", "CHARGES_PROCESS");
        dataMap.put(CommonConstants.PRODUCT_TYPE, "TD");
        dataMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        chkSelectAll.setEnabled(true);
        tblData.setEnabled(true);
        String shareType = ((ComboBoxModel) cboShareType.getModel()).getKeyForSelected().toString();
        String prodType = (String) (((ComboBoxModel) (cboSMSProducts).getModel()).getKeyForSelected());  
        dataMap.put("SHARE_TYPE", shareType);
        dataMap.put("PROD_TYPE",prodType);    
        if(cboBranch.getSelectedIndex() > 0){
            dataMap.put("SELECTED_BRANCH",cboBranch.getSelectedItem());
        }
        dataMap.put("DEPOSIT_INTEREST_SCREEN", "DEPOSIT_INTEREST_SCREEN");
        System.out.println("displayInterestDetails dataMap : " + dataMap);
        observable.insertTableData(dataMap);
        tblData.setModel(observable.getTblData());        
        System.out.println("observable size : " + observable.getTblData());
        System.out.println("table size : " + tblData.getSize());
        javax.swing.table.TableColumn col = tblData.getColumn(tblData.getColumnName(0));
        col.setMaxWidth(100);
        col = tblData.getColumn(tblData.getColumnName(1));
        col.setMaxWidth(200);
        col = tblData.getColumn(tblData.getColumnName(2));
        col.setMaxWidth(200);
        col = tblData.getColumn(tblData.getColumnName(3));
        col.setMaxWidth(200);
        btnProcess.setEnabled(true);
        txtAreaDescription.setEnabled(true);
        if (tblData.getRowCount() == 0) {
            ClientUtil.showMessageWindow(" No Data !!! ");
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnClose1;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CComboBox cboBranch;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboSMSProducts;
    private com.see.truetransact.uicomponent.CComboBox cboShareType;
    private com.see.truetransact.uicomponent.CCheckBox chkCreditCash;
    private com.see.truetransact.uicomponent.CCheckBox chkCreditClearing;
    private com.see.truetransact.uicomponent.CCheckBox chkCreditTransfer;
    private com.see.truetransact.uicomponent.CCheckBox chkDebitCash;
    private com.see.truetransact.uicomponent.CCheckBox chkDebitClearing;
    private com.see.truetransact.uicomponent.CCheckBox chkDebitTransfer;
    private com.see.truetransact.uicomponent.CCheckBox chkMalayalam;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblCash;
    private com.see.truetransact.uicomponent.CLabel lblClearing;
    private com.see.truetransact.uicomponent.CLabel lblCredit;
    private com.see.truetransact.uicomponent.CLabel lblCreditCashAmt;
    private com.see.truetransact.uicomponent.CLabel lblCreditClearingAmt;
    private com.see.truetransact.uicomponent.CLabel lblCreditTransferAmt;
    private com.see.truetransact.uicomponent.CLabel lblCustType;
    private com.see.truetransact.uicomponent.CLabel lblDebit;
    private com.see.truetransact.uicomponent.CLabel lblDebitCashAmt;
    private com.see.truetransact.uicomponent.CLabel lblDebitClearingAmt;
    private com.see.truetransact.uicomponent.CLabel lblDebitTransferAmt;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblReminder;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace52;
    private com.see.truetransact.uicomponent.CLabel lblSpace53;
    private com.see.truetransact.uicomponent.CLabel lblSpace54;
    private com.see.truetransact.uicomponent.CLabel lblSpace55;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblSpaces;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CLabel lblTransfer;
    private com.see.truetransact.uicomponent.CLabel lblTxnAllowed;
    private com.see.truetransact.uicomponent.CMenuBar mbrLoanProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAmountParameters;
    private com.see.truetransact.uicomponent.CPanel panChequeBook;
    private com.see.truetransact.uicomponent.CPanel panCreditDebit;
    private com.see.truetransact.uicomponent.CPanel panDueAlerts;
    private com.see.truetransact.uicomponent.CPanel panInfo;
    private com.see.truetransact.uicomponent.CPanel panInfo1;
    private com.see.truetransact.uicomponent.CPanel panInformationAlert;
    private com.see.truetransact.uicomponent.CPanel panProduct;
    private com.see.truetransact.uicomponent.CPanel panProductData;
    private com.see.truetransact.uicomponent.CPanel panProductTableData;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CPanel panTransactionAlerts;
    private com.see.truetransact.uicomponent.CButtonGroup rdoLeaf;
    private com.see.truetransact.uicomponent.CRadioButton rdoReminderNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoReminderYes;
    private com.see.truetransact.uicomponent.CRadioButton rdoTxnAllowedNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoTxnAllowedYes;
    private com.see.truetransact.uicomponent.CSeparator spt1;
    private com.see.truetransact.uicomponent.CSeparator spt2;
    private com.see.truetransact.uicomponent.CSeparator spt3;
    private com.see.truetransact.uicomponent.CSeparator spt4;
    private com.see.truetransact.uicomponent.CSeparator spt5;
    private com.see.truetransact.uicomponent.CSeparator spt6;
    private com.see.truetransact.uicomponent.CSeparator spt7;
    private javax.swing.JSeparator sptCancel;
    private com.see.truetransact.uicomponent.CSeparator sptChequeIssue;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpData;
    private com.see.truetransact.uicomponent.CTabbedPane tabChequeBook;
    private com.see.truetransact.uicomponent.CTable tblData;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CTextArea txtAreaDescription;
    private com.see.truetransact.uicomponent.CTextField txtCreditCashAmt;
    private com.see.truetransact.uicomponent.CTextField txtCreditClearingAmt;
    private com.see.truetransact.uicomponent.CTextField txtCreditTransferAmt;
    private com.see.truetransact.uicomponent.CTextField txtDebitCashAmt;
    private com.see.truetransact.uicomponent.CTextField txtDebitClearingAmt;
    private com.see.truetransact.uicomponent.CTextField txtDebitTransferAmt;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    // End of variables declaration//GEN-END:variables
}
