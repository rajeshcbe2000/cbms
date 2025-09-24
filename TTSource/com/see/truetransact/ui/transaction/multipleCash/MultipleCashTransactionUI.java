/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmptransferUI.java
 
 * Created on feb 9, 2009, 10:53 AM
 */
package com.see.truetransact.ui.transaction.multipleCash;

import java.util.HashMap;
import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.customer.customeridchange.CustomerIdChangeOB;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import java.util.Date;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uivalidation.*;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import javax.swing.JOptionPane;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
/**
 * This form is used to manipulate CustomerIdChangeUI related functionality
 *
 * @author swaroop
 */
public class MultipleCashTransactionUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {

    public String accNo1="";
    final int EDIT = 0, DELETE = 1, ACCNO = 2, AUTHORIZE = 3, ACCTHDID = 4, VIEW = 5, LINK_BATCH_TD = 6, LINK_BATCH = 7, DEBIT_DETAILS = 8, PAN_NUM = 9, TELLER_ENTRY_DETIALS = 10;
    int viewType = -1;
    boolean isFilled = false;
    // private String viewType = new String();
    private HashMap mandatoryMap;
    private MultipleCashTransactionOB observable;
    //  final String AUTHORIZE="Authorize";
    private final static Logger log = Logger.getLogger(MultipleCashTransactionUI.class);
    MultipleCashTransactionRB complaintRB = new MultipleCashTransactionRB();
    private String prodType = "";
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    String cust_type = null;
    String txtComplaintid = null;
    private Object columnNames[] = {"  Sl No", "  Account Head Desc", "  Trans Type", "   Product Type", "  Account Head", "  Amount"};
    private List bufferList = new ArrayList();
    Date currDt = ClientUtil.getCurrentDate();
    boolean fromAuthorizeUI = false;
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI=null;
    AuthorizeListCreditUI CashierauthorizeListUI=null;
    TransactionUI transactionUI = new TransactionUI();
    private int rejectFlag = 0;
     public String prodIdforgl="";
     public String prodtypeforgl="";
     double receiptTot = 0.0;
     double paymentTot = 0.0;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;

    /**
     * Creates new form CustomerIdChangeUI
     */
    public MultipleCashTransactionUI() {
        initComponents();
        initStartUp();
    }

    private void initStartUp() {
        setMandatoryHashMap();
        setFieldNames();
        internationalize();
        observable = new MultipleCashTransactionOB();
        observable.addObserver(this);
        initComponentData();
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
        objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.complaintregister.ComplaintRegisterMRB", ProxyParameters.LANGUAGE);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panData);
        setHelpMessage();
        btnNew_Property1.setEnabled(false);
        btnDelete_Property1.setEnabled(false);
        btnlSave_Property1.setEnabled(false);
        btnAccHdId.setEnabled(false);
        txtTotPayment.setEnabled(false);
        txtTotReceipt.setEnabled(false);
    }

    private void setMaxLength() {
//           txtEmpID.setMaxLength(64);
//           txtEmpID.setValidation(new CurrencyValidation());
//         txtAccountNumber.setMaxLength(16);
//         txtAccountNumber.setAllowAll(true);
//         txtOldCustID.setAllowAll(true);
//         txtNewCustId.setAllowAll(true);
    }

    private void getTableData() {
        Object rowData[][] = new Object[+bufferList.size()][6];//Changed By Suresh
        int j = 0;
        String d1 = "";
        String d2 = "";
        int i = 0;
        double CrAmount=0.0,dbAmount=0.0;
        for (i = 0; i < bufferList.size(); i++) {
            HashMap m = new HashMap();
            m = (HashMap) bufferList.get(i);
            rowData[i][0] = m.get("SNO").toString();
            rowData[i][1] = m.get("AC_HEAD_DESC").toString(); // Added By Suresh
            rowData[i][2] = m.get("TRANS_TYPE").toString();
            rowData[i][3] = m.get("PROD_TYPE").toString();
            rowData[i][4] = m.get("ACC_NO").toString();
            rowData[i][5] = m.get("AMOUNT").toString();
            if (m.get("TRANS_TYPE").toString().equals("DEBIT")) {
                dbAmount = dbAmount + CommonUtil.convertObjToDouble(m.get("AMOUNT").toString());
            } else if (m.get("TRANS_TYPE").toString().equals("CREDIT")) {
                CrAmount = CrAmount + CommonUtil.convertObjToDouble(m.get("AMOUNT").toString());
            }
        }
        if (CrAmount != 0) {
            txtTotReceipt.setText(CommonUtil.convertObjToStr(CrAmount));
        } else {
            txtTotReceipt.setText("");
        }
        if (dbAmount != 0) {
            txtTotPayment.setText(CommonUtil.convertObjToStr(dbAmount));
        } else {
            txtTotPayment.setText("");
        }
        tblCash.setModel(new javax.swing.table.DefaultTableModel(rowData, columnNames) {
            public boolean isCellEditable(int row, int column) {
                //Only the third column
                return false;
            }
        });
        tblCash.setVisible(true);
    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("tdtDateofComplaint", new Boolean(true));
        mandatoryMap.put("txaNameAddress", new Boolean(true));
        mandatoryMap.put("txaComments", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /**
     * **************** NEW METHODS ****************
     */
    private void updateAuthorizeStatus(String authorizeStatus) {

        if (viewType == AUTHORIZE && isFilled) {

            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                HashMap countMap = new HashMap();
                //system.out.println("observable.getIRNo()asssobservable.getCashId()>>>>>" + observable.getTransId());
                countMap.put("IRID", observable.getSingleTransId());
                countMap.put("TRANSDT", currDt.clone());
                List countList = ClientUtil.executeQuery("getCountForReceiptCashierAuthorizeMultipleCash", countMap);
                if (countList != null && countList.size() > 0) {
                    //system.out.println("nnnjdj>>>???");
                    countMap = new HashMap();
                    countMap = (HashMap) countList.get(0);
                    if (CommonUtil.convertObjToInt(countMap.get("COUNT")) != 0) {
                        //system.out.println("nnnjdj4566>>>???");
                        ClientUtil.showMessageWindow("Receipt cash transaction authorization for the cash Id is pending\nPlease authorize the pending receipt cash transactions for the cash Id first");
                        return;
                    }
                }
            }

            int option = 0;
            if (authorizeStatus.equals(CommonConstants.STATUS_REJECTED)) {
                String[] obj = {"Ok", "Cancel"};
                option = COptionPane.showOptionDialog(null, (" Do you want to reject ..."),
                        ("Select The Desired Option"),
                        COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj, obj[0]);
            }
            //system.out.println("opptionn" + option);
            if (option == 1) {
                btnCancelActionPerformed(null);
            } else {


                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", authorizeStatus);

                singleAuthorizeMap.put("SINGLE_TRANS_ID", observable.getSingleTransId());
                singleAuthorizeMap.put("TRANS_ID", observable.getTransId());

                singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("AUTHORIZED_DT", currDt);
                arrList.add(singleAuthorizeMap);
//            if(!CommonUtil.convertObjToStr(observable.getCashId()).equals("")){
//                authorizeMap.put("CASH_ID",observable.getCashId());
//            }
                authorizeMap.put("TRANS_ID", observable.getTransId());
                authorizeMap.put("SINGLE_TRANS_ID", observable.getSingleTransId());
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorize(authorizeMap, observable.getTransId());
                // viewType = "";
                super.setOpenForEditBy(observable.getStatusBy());
                singleAuthorizeMap = null;
                arrList = null;
                authorizeMap = null;
            }
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);
            whereMap.put("TRANS_DT", currDt.clone());
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                mapParam.put(CommonConstants.MAP_NAME, "getCashierAuthorizationsListForMutipleCash");
            } else {
                mapParam.put(CommonConstants.MAP_NAME, "getAuthorizationsListForMutipleCash");
            }
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    public void authorize(HashMap map, String id) {
        //system.out.println("Authorize Map : " + map);

        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
//            if(transactionUI.getOutputTO().size()>0){
//                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
//            }
            observable.doAction();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(id);
                if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.displayDetails("Multiple Cash Tranasction");
                }
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                    authorizeListUI.setFocusToTable();
                    authorizeListUI.displayDetails("Multiple Cash Tranasction");
                }
                if (fromCashierAuthorizeUI) {
                    CashierauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    CashierauthorizeListUI.setFocusToTable();
                } 
                if (fromManagerAuthorizeUI) {
                    ManagerauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    ManagerauthorizeListUI. setFocusToTable();
                }
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

//    
//    public void authorize(HashMap map,String id) {
//        //system.out.println("Authorize Map : " + map);
//        
//        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
//            observable.set_authorizeMap(map);
//            observable.doAction();
//            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
//                   super.setOpenForEditBy(observable.getStatusBy());
//                   super.removeEditLock(id);
//             }
//            btnCancelActionPerformed(null);
//            observable.setStatus();
//            observable.setResultStatus();
//            lblStatus.setText(observable.getLblStatus());
//            
//        }
//    }
//    public void authorize(HashMap map) {
//        //system.out.println("Authorize Map : " + map);
//        
//        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
//            observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
//            observable.set_authorizeMap(map);
//            observable.doAction();
//            btnCancelActionPerformed(null);
//            observable.setStatus();
//            observable.setResultStatus();
//            lblStatus.setText(observable.getLblStatus());
//            
//        }
//    }
    public void setHelpMessage() {
    }

    /**
     * ********** END OF NEW METHODS **************
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        cButtonGroup1 = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrVisitorsDiary = new javax.swing.JToolBar();
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
        lblSpace57 = new com.see.truetransact.uicomponent.CLabel();
        btnDateChange = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        panCashTransaction = new com.see.truetransact.uicomponent.CPanel();
        panData = new com.see.truetransact.uicomponent.CPanel();
        lblAccHd = new com.see.truetransact.uicomponent.CLabel();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        txtAccHdId = new com.see.truetransact.uicomponent.CTextField();
        btnAccHdId = new com.see.truetransact.uicomponent.CButton();
        panTransactionType = new com.see.truetransact.uicomponent.CPanel();
        rdoTransactionType_Credit = new com.see.truetransact.uicomponent.CRadioButton();
        rdoTransactionType_Debit = new com.see.truetransact.uicomponent.CRadioButton();
        lblInitiatorChannel = new com.see.truetransact.uicomponent.CLabel();
        txtInitiatorChannel = new com.see.truetransact.uicomponent.CTextField();
        lblInstrumentType = new com.see.truetransact.uicomponent.CLabel();
        cboInstrumentType = new com.see.truetransact.uicomponent.CComboBox();
        lblInstrumentNo = new com.see.truetransact.uicomponent.CLabel();
        txtInstrumentNo2 = new com.see.truetransact.uicomponent.CTextField();
        tdtInstrumentDate = new com.see.truetransact.uicomponent.CDateField();
        lblInstrumentDate = new com.see.truetransact.uicomponent.CLabel();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        lblAmount1 = new com.see.truetransact.uicomponent.CLabel();
        txtParticulars = new com.see.truetransact.uicomponent.CTextField();
        lblParticulars = new com.see.truetransact.uicomponent.CLabel();
        lblNarration = new com.see.truetransact.uicomponent.CLabel();
        cPanel3 = new com.see.truetransact.uicomponent.CPanel();
        btnNew_Property1 = new com.see.truetransact.uicomponent.CButton();
        btnlSave_Property1 = new com.see.truetransact.uicomponent.CButton();
        btnDelete_Property1 = new com.see.truetransact.uicomponent.CButton();
        lbHeadDescription = new com.see.truetransact.uicomponent.CLabel();
        srpTxtAreaParticulars = new com.see.truetransact.uicomponent.CScrollPane();
        txtNarration = new com.see.truetransact.uicomponent.CTextArea();
        lblAccName = new com.see.truetransact.uicomponent.CLabel();
        lblHouseName = new com.see.truetransact.uicomponent.CLabel();
        lblAccNoGl = new com.see.truetransact.uicomponent.CLabel();
        lblTotPayment = new com.see.truetransact.uicomponent.CLabel();
        txtTotPayment = new com.see.truetransact.uicomponent.CTextField();
        txtTotReceipt = new com.see.truetransact.uicomponent.CTextField();
        lblTotReceipt = new com.see.truetransact.uicomponent.CLabel();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblCash = new com.see.truetransact.uicomponent.CTable();
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
        setMaximumSize(new java.awt.Dimension(795, 640));
        setMinimumSize(new java.awt.Dimension(795, 640));
        setPreferredSize(new java.awt.Dimension(795, 640));

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
        tbrVisitorsDiary.add(btnView);

        lblSpace5.setText("     ");
        tbrVisitorsDiary.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.setFocusable(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnNew);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrVisitorsDiary.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrVisitorsDiary.add(lblSpace52);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.setFocusable(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnDelete);

        lblSpace2.setText("     ");
        tbrVisitorsDiary.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnSave);

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrVisitorsDiary.add(lblSpace53);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnCancel);

        lblSpace3.setText("     ");
        tbrVisitorsDiary.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnAuthorize);

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrVisitorsDiary.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrVisitorsDiary.add(lblSpace55);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnReject);

        lblSpace4.setText("     ");
        tbrVisitorsDiary.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrVisitorsDiary.add(btnPrint);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrVisitorsDiary.add(lblSpace56);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnClose);

        lblSpace57.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace57.setText("     ");
        lblSpace57.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrVisitorsDiary.add(lblSpace57);

        btnDateChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/denomination.gif"))); // NOI18N
        btnDateChange.setToolTipText("Exception");
        btnDateChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateChangeActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnDateChange);

        getContentPane().add(tbrVisitorsDiary, java.awt.BorderLayout.NORTH);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        cPanel1.setLayout(null);

        panCashTransaction.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panCashTransaction.setMinimumSize(new java.awt.Dimension(700, 550));
        panCashTransaction.setPreferredSize(new java.awt.Dimension(700, 550));
        panCashTransaction.setLayout(new java.awt.GridBagLayout());

        panData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panData.setMinimumSize(new java.awt.Dimension(690, 250));
        panData.setPreferredSize(new java.awt.Dimension(690, 250));
        panData.setLayout(null);

        lblAccHd.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAccHd.setText("Account Head");
        panData.add(lblAccHd);
        lblAccHd.setBounds(70, 50, 82, 18);

        lblAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAmount.setText("Transaction Type");
        panData.add(lblAmount);
        lblAmount.setBounds(50, 100, 100, 30);

        lblProdType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProdType.setText("Product Type");
        panData.add(lblProdType);
        lblProdType.setBounds(70, 20, 77, 20);

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(130);
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        panData.add(cboProdType);
        cboProdType.setBounds(160, 20, 100, 21);

        txtAccHdId.setAllowAll(true);
        txtAccHdId.setMinimumSize(new java.awt.Dimension(100, 21));
        panData.add(txtAccHdId);
        txtAccHdId.setBounds(160, 50, 100, 21);

        btnAccHdId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccHdId.setToolTipText("Account Head");
        btnAccHdId.setMaximumSize(new java.awt.Dimension(25, 25));
        btnAccHdId.setMinimumSize(new java.awt.Dimension(25, 25));
        btnAccHdId.setPreferredSize(new java.awt.Dimension(25, 25));
        btnAccHdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccHdIdActionPerformed(evt);
            }
        });
        panData.add(btnAccHdId);
        btnAccHdId.setBounds(260, 50, 30, 25);

        panTransactionType.setMinimumSize(new java.awt.Dimension(160, 23));
        panTransactionType.setPreferredSize(new java.awt.Dimension(150, 23));
        panTransactionType.setLayout(new java.awt.GridBagLayout());

        cButtonGroup1.add(rdoTransactionType_Credit);
        rdoTransactionType_Credit.setText("Receipt");
        rdoTransactionType_Credit.setMargin(new java.awt.Insets(2, 5, 2, 2));
        rdoTransactionType_Credit.setMaximumSize(new java.awt.Dimension(69, 27));
        rdoTransactionType_Credit.setMinimumSize(new java.awt.Dimension(85, 27));
        rdoTransactionType_Credit.setPreferredSize(new java.awt.Dimension(85, 27));
        rdoTransactionType_Credit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTransactionType_CreditActionPerformed(evt);
            }
        });
        panTransactionType.add(rdoTransactionType_Credit, new java.awt.GridBagConstraints());

        cButtonGroup1.add(rdoTransactionType_Debit);
        rdoTransactionType_Debit.setText("Payment");
        rdoTransactionType_Debit.setMinimumSize(new java.awt.Dimension(85, 27));
        rdoTransactionType_Debit.setPreferredSize(new java.awt.Dimension(85, 27));
        rdoTransactionType_Debit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTransactionType_DebitActionPerformed(evt);
            }
        });
        panTransactionType.add(rdoTransactionType_Debit, new java.awt.GridBagConstraints());

        panData.add(panTransactionType);
        panTransactionType.setBounds(160, 100, 150, 23);

        lblInitiatorChannel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInitiatorChannel.setText("Initiator Channel Type");
        panData.add(lblInitiatorChannel);
        lblInitiatorChannel.setBounds(20, 130, 130, 18);

        txtInitiatorChannel.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInitiatorChannel.setPreferredSize(new java.awt.Dimension(21, 200));
        txtInitiatorChannel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInitiatorChannelActionPerformed(evt);
            }
        });
        panData.add(txtInitiatorChannel);
        txtInitiatorChannel.setBounds(160, 130, 140, 20);

        lblInstrumentType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInstrumentType.setText("Instrument Type");
        panData.add(lblInstrumentType);
        lblInstrumentType.setBounds(50, 160, 100, 20);

        cboInstrumentType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboInstrumentType.setPreferredSize(new java.awt.Dimension(21, 200));
        cboInstrumentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboInstrumentTypeActionPerformed(evt);
            }
        });
        cboInstrumentType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboInstrumentTypeFocusLost(evt);
            }
        });
        panData.add(cboInstrumentType);
        cboInstrumentType.setBounds(160, 160, 140, 20);

        lblInstrumentNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInstrumentNo.setText("Instrument No.");
        panData.add(lblInstrumentNo);
        lblInstrumentNo.setBounds(50, 190, 100, 20);

        txtInstrumentNo2.setAllowNumber(true);
        txtInstrumentNo2.setMinimumSize(new java.awt.Dimension(100, 21));
        panData.add(txtInstrumentNo2);
        txtInstrumentNo2.setBounds(160, 190, 140, 21);

        tdtInstrumentDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtInstrumentDate.setPreferredSize(new java.awt.Dimension(21, 200));
        tdtInstrumentDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtInstrumentDateFocusLost(evt);
            }
        });
        panData.add(tdtInstrumentDate);
        tdtInstrumentDate.setBounds(160, 210, 100, 30);

        lblInstrumentDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInstrumentDate.setText("Instrument Date");
        panData.add(lblInstrumentDate);
        lblInstrumentDate.setBounds(50, 210, 100, 30);

        txtAmount.setAllowNumber(true);
        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
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
        panData.add(txtAmount);
        txtAmount.setBounds(390, 100, 100, 21);

        lblAmount1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAmount1.setText("Amount");
        panData.add(lblAmount1);
        lblAmount1.setBounds(330, 100, 60, 20);

        txtParticulars.setMinimumSize(new java.awt.Dimension(100, 21));
        txtParticulars.setPreferredSize(new java.awt.Dimension(21, 200));
        panData.add(txtParticulars);
        txtParticulars.setBounds(410, 130, 160, 20);

        lblParticulars.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblParticulars.setText("Particulars");
        panData.add(lblParticulars);
        lblParticulars.setBounds(340, 130, 62, 20);

        lblNarration.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNarration.setText("Member Name/Narration");
        panData.add(lblNarration);
        lblNarration.setBounds(310, 170, 141, 18);

        cPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cPanel3.setMinimumSize(new java.awt.Dimension(312, 32));
        cPanel3.setPreferredSize(new java.awt.Dimension(312, 32));
        cPanel3.setLayout(new java.awt.GridBagLayout());

        btnNew_Property1.setText("New");
        btnNew_Property1.setMaximumSize(new java.awt.Dimension(80, 30));
        btnNew_Property1.setMinimumSize(new java.awt.Dimension(80, 30));
        btnNew_Property1.setPreferredSize(new java.awt.Dimension(80, 30));
        btnNew_Property1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_Property1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        cPanel3.add(btnNew_Property1, gridBagConstraints);

        btnlSave_Property1.setText("Save");
        btnlSave_Property1.setMaximumSize(new java.awt.Dimension(80, 30));
        btnlSave_Property1.setMinimumSize(new java.awt.Dimension(80, 30));
        btnlSave_Property1.setPreferredSize(new java.awt.Dimension(80, 30));
        btnlSave_Property1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlSave_Property1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        cPanel3.add(btnlSave_Property1, gridBagConstraints);

        btnDelete_Property1.setText("Delete");
        btnDelete_Property1.setMaximumSize(new java.awt.Dimension(80, 30));
        btnDelete_Property1.setMinimumSize(new java.awt.Dimension(80, 30));
        btnDelete_Property1.setPreferredSize(new java.awt.Dimension(80, 30));
        btnDelete_Property1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete_Property1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        cPanel3.add(btnDelete_Property1, gridBagConstraints);

        panData.add(cPanel3);
        cPanel3.setBounds(390, 210, 280, 50);

        lbHeadDescription.setForeground(new java.awt.Color(0, 0, 255));
        lbHeadDescription.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbHeadDescription.setText("Head Description");
        lbHeadDescription.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        panData.add(lbHeadDescription);
        lbHeadDescription.setBounds(20, 80, 360, 18);

        srpTxtAreaParticulars.setMinimumSize(new java.awt.Dimension(335, 45));
        srpTxtAreaParticulars.setPreferredSize(new java.awt.Dimension(335, 45));

        txtNarration.setBorder(javax.swing.BorderFactory.createBevelBorder(1));
        txtNarration.setLineWrap(true);
        txtNarration.setMinimumSize(new java.awt.Dimension(20, 100));
        txtNarration.setPreferredSize(new java.awt.Dimension(20, 100));
        srpTxtAreaParticulars.setViewportView(txtNarration);

        panData.add(srpTxtAreaParticulars);
        srpTxtAreaParticulars.setBounds(470, 160, 230, 45);

        lblAccName.setForeground(new java.awt.Color(0, 51, 204));
        lblAccName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccName.setMaximumSize(new java.awt.Dimension(1000, 21));
        lblAccName.setMinimumSize(new java.awt.Dimension(100, 21));
        lblAccName.setPreferredSize(new java.awt.Dimension(100, 21));
        panData.add(lblAccName);
        lblAccName.setBounds(360, 40, 320, 21);

        lblHouseName.setForeground(new java.awt.Color(0, 51, 204));
        lblHouseName.setMaximumSize(new java.awt.Dimension(1000, 21));
        lblHouseName.setMinimumSize(new java.awt.Dimension(100, 21));
        lblHouseName.setPreferredSize(new java.awt.Dimension(100, 21));
        panData.add(lblHouseName);
        lblHouseName.setBounds(370, 70, 330, 21);

        lblAccNoGl.setForeground(new java.awt.Color(0, 102, 255));
        lblAccNoGl.setText(accNo1);
        lblAccNoGl.setMaximumSize(new java.awt.Dimension(120, 21));
        lblAccNoGl.setMinimumSize(new java.awt.Dimension(120, 21));
        lblAccNoGl.setPreferredSize(new java.awt.Dimension(120, 21));
        panData.add(lblAccNoGl);
        lblAccNoGl.setBounds(500, 100, 200, 21);

        lblTotPayment.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotPayment.setText("Total Payment");
        panData.add(lblTotPayment);
        lblTotPayment.setBounds(510, 10, 90, 20);

        txtTotPayment.setAllowNumber(true);
        txtTotPayment.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotPaymentActionPerformed(evt);
            }
        });
        txtTotPayment.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTotPaymentFocusLost(evt);
            }
        });
        panData.add(txtTotPayment);
        txtTotPayment.setBounds(600, 10, 100, 21);

        txtTotReceipt.setAllowNumber(true);
        txtTotReceipt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotReceipt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotReceiptActionPerformed(evt);
            }
        });
        txtTotReceipt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTotReceiptFocusLost(evt);
            }
        });
        panData.add(txtTotReceipt);
        txtTotReceipt.setBounds(400, 10, 100, 21);

        lblTotReceipt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotReceipt.setText("Total Receipt");
        panData.add(lblTotReceipt);
        lblTotReceipt.setBounds(300, 10, 100, 20);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 21;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panCashTransaction.add(panData, gridBagConstraints);

        cPanel2.setMinimumSize(new java.awt.Dimension(710, 265));
        cPanel2.setPreferredSize(new java.awt.Dimension(710, 265));
        cPanel2.setLayout(new java.awt.GridBagLayout());

        cScrollPane1.setMinimumSize(new java.awt.Dimension(708, 250));
        cScrollPane1.setPreferredSize(new java.awt.Dimension(708, 250));

        tblCash.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblCash.setMinimumSize(new java.awt.Dimension(300, 72));
        tblCash.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCashMouseClicked(evt);
            }
        });
        cScrollPane1.setViewportView(tblCash);

        cPanel2.add(cScrollPane1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCashTransaction.add(cPanel2, gridBagConstraints);

        cPanel1.add(panCashTransaction);
        panCashTransaction.setBounds(10, 0, 740, 540);

        getContentPane().add(cPanel1, java.awt.BorderLayout.CENTER);

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

    private void btnDateChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateChangeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDateChangeActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
//        btnView.setEnabled(false);
//        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
//        popUp("Enquiry");
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
//        updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
//        btnCancel.setEnabled(true);
//        btnReject.setEnabled(false);
//        btnAuthorize.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
//       observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
//        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
//         btnCancel.setEnabled(true);
//         btnAuthorize.setEnabled(false);
//        btnException.setEnabled(false);
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnNew_Property1.setEnabled(false);
        btnDelete_Property1.setEnabled(false);
        btnlSave_Property1.setEnabled(false);
        tblCash.setEnabled(true);
        btnAccHdId.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnNew_Property1.setEnabled(false);
        btnDelete_Property1.setEnabled(false);
        btnlSave_Property1.setEnabled(false);
        tblCash.setEnabled(true);
        btnAccHdId.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mitCloseActionPerformed(evt);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
        cifClosingAlert();
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
//       observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
//        observable.setStatus();
//        popUp("Delete");
        ClientUtil.showMessageWindow("Complaints Cannot be deleted");
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
//   observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
//    observable.setStatus();
//    popUp("Edit");
        lblStatus.setText(observable.getLblStatus());
        ClientUtil.showMessageWindow("Multiple cash Transactions Cannot be edited");
    }//GEN-LAST:event_btnEditActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // deletescreenLock();
        lblAccNoGl.setText("");
        lblAccName.setText("");
        lblHouseName.setText("");
        observable.resetForm();
        ClientUtil.clearAll(this);
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        lblStatus.setText("");
        setModified(false);
        lbHeadDescription.setText("");
        bufferList.clear();
        isFilled = false;
        getTableData();
        //Added By Suresh
        setSizeTableData();
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
        lblAccNoGl.setText("");
        lblAccName.setText("");
        lblHouseName.setText("");
         receiptTot = 0.0;
         paymentTot = 0.0;
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        updateOBFields();
        if (bufferList.isEmpty()) {
            ClientUtil.displayAlert("Please add data to grid");
            return;
        }
//        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panComplains);
//        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
//            displayAlert(mandatoryMessage);
//        }
//        else{
        savePerformed();
//        }
        setModified(false);
            lblAccNoGl.setText("");
            lblAccName.setText("");
        lblHouseName.setText("");
        btnCancelActionPerformed(null);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        enableDisable(true);
        setButtonEnableDisable();
        setModified(true);
        // btnCust.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        bufferList.clear();
        getTableData();
        isFilled = false;
        cboProdType.setSelectedItem(observable.getCbmProdType().getDataForKey("GL"));
        cboProdType.setEnabled(false);
        txtInitiatorChannel.setText("CASHIER");
        btnNew_Property1.setEnabled(true);
        btnDelete_Property1.setEnabled(true);
        btnlSave_Property1.setEnabled(true);
        tblCash.setEnabled(true);
        btnAccHdId.setEnabled(true); 
lblAccNoGl.setText("");
       lblAccName.setText("");
        lblHouseName.setText("");
        txtAccHdId.setEnabled(false);
        //Added By Suresh
        setSizeTableData();
        receiptTot = 0.0;
        paymentTot = 0.0;
    }//GEN-LAST:event_btnNewActionPerformed
    private void setSizeTableData(){
        tblCash.getColumnModel().getColumn(0).setPreferredWidth(15);
        tblCash.getColumnModel().getColumn(1).setPreferredWidth(300);
        tblCash.getColumnModel().getColumn(2).setPreferredWidth(55);
        tblCash.getColumnModel().getColumn(3).setPreferredWidth(55);
        tblCash.getColumnModel().getColumn(4).setPreferredWidth(55);
        tblCash.getColumnModel().getColumn(5).setPreferredWidth(55);
    }
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void btnAccHdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccHdIdActionPerformed
        viewType = ACCTHDID;
        final HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
        new ViewAll(this, viewMap).show();
    }//GEN-LAST:event_btnAccHdIdActionPerformed

    private void clearProdFields() {
        txtAccHdId.setText("");
        lbHeadDescription.setText("");
    }

    private void populateInstrumentType() {
        ComboBoxModel objModel = new ComboBoxModel();
        objModel.addKeyAndElement("", "");
        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
        if (!prodType.equals("")) {
            if (prodType.equals("OA") || prodType.equals("SA") || prodType.equals("AD") || prodType.equals("AAD") || prodType.equals("TD") || prodType.equals("AB")) {
                objModel.addKeyAndElement("WITHDRAW_SLIP", observable.getCbmInstrumentType().getDataForKey("WITHDRAW_SLIP"));
                objModel.addKeyAndElement("CHEQUE", observable.getCbmInstrumentType().getDataForKey("CHEQUE"));
                objModel.addKeyAndElement("VOUCHER", observable.getCbmInstrumentType().getDataForKey("VOUCHER"));
            } else if (prodType.equals("TL") || prodType.equals("GL") || prodType.equals("ATL")) {
                objModel.addKeyAndElement("VOUCHER", observable.getCbmInstrumentType().getDataForKey("VOUCHER"));
            }
            cboInstrumentType.setModel(objModel);
            //system.out.println("#$#$#$^% Instrument type : " + observable.getCboInstrumentType());
            cboInstrumentType.setSelectedItem(
                    CommonUtil.convertObjToStr(observable.getCboInstrumentType()));
        }
    }

    private void setProdEnable(boolean isEnable) {
//        cboProdId.setEnabled(isEnable);
//        txtAccNo.setEnabled(isEnable);
//        btnAccNo.setEnabled(isEnable);

        btnAccHdId.setEnabled(!isEnable);

        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            setEditFieldsEnable(false);
        }
        //        btnPhoto.setEnabled(isEnable);
    }

    private void setEditFieldsEnable(boolean yesno) {
        rdoTransactionType_Credit.setEnabled(yesno);
        rdoTransactionType_Debit.setEnabled(yesno);
//        txtAccNo.setEnabled(yesno);
//        btnAccNo.setEnabled(yesno);
        btnAccHdId.setEnabled(yesno);
//        cboProdId.setEnabled(yesno);
        cboProdType.setEnabled(yesno);
    }

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        //To Set the Value of Account Head in UI...
        if (cboProdType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
            //system.out.println("***************" + prodType);
//            clearProdFields();
            //Added BY Suresh

            populateInstrumentType();
            observable.setProdType(prodType);
            //this is for depoists

            if (prodType.equals("GL")) {
                if (TrueTransactMain.BRANCH_ID.equals(CommonUtil.convertObjToStr(TrueTransactMain.selBranch))) {
                    //                observable.setCbmProdId(prodType);
                    //                cboProdId.setModel(observable.getCbmProdId());
//                    txtAccNo.setText("");
//                    txtPanNo.setText("");
//                    btnPanNo.setEnabled(false);
                    btnAccHdId.setEnabled(true);
                    if (!(viewType == EDIT || viewType == DELETE || viewType == AUTHORIZE || viewType == VIEW || viewType == LINK_BATCH || viewType == LINK_BATCH_TD)) //  observable.setTxtAccNo("");
                    {
                        setProdEnable(false);
                    }
                    //  cboProdId.setSelectedItem("");
                } else {
                    ClientUtil.displayAlert("InterBranch Transactions Not Allowed For GL");
                    observable.resetForm();
                }
                txtAccHdId.setEnabled(true);
            }
//            } else {
//                if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ||
//                    observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT ) {
//                    productBased();
//                }
//                setProdEnable(true);
//                observable.setCbmProdId(prodType);
//                cboProdId.setModel(observable.getCbmProdId());
//                //                observable.getCbmProdId().setKeyForSelected(observable.getCbmProdId().getDataForKey(observable.getCboProdId()));
//                txtAccHdId.setEnabled(false);
//                btnAccNo.setEnabled(false);
//            }
//            btnViewTermLoanDetails. setEnabled(true);
        }
        if (viewType == AUTHORIZE || viewType == LINK_BATCH || viewType == LINK_BATCH_TD) {
//            cboProdId.setEnabled(false);
//            txtAccNo.setEnabled(false);
//            btnAccNo.setEnabled(false);
        }
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void btnNew_Property1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_Property1ActionPerformed
        lblAccNoGl.setText("");
        lblAccName.setText("");
        lblHouseName.setText("");
//        cboProdType.setSelectedItem("");
        rdoTransactionType_Credit.setSelected(false);
        rdoTransactionType_Debit.setSelected(true);
        //system.out.println("issss" + rdoTransactionType_Debit.isSelected());
        txtInitiatorChannel.setText("");
        //  cboInputCurrency.setSelectedItem("");
//        txtAccHdId.setText("");
//        lbHeadDescription.setText("");
        // txtInputAmt.setText("");
        cboInstrumentType.setSelectedItem("");
        txtInstrumentNo2.setText("");
        tdtInstrumentDate.setDateValue("");
        //  txtTokenNo.setText("");
        txtAmount.setText("");
        txtParticulars.setText("");
        txtNarration.setText("");

        cboProdType.setEnabled(true);
        rdoTransactionType_Credit.setEnabled(true);
        rdoTransactionType_Debit.setEnabled(true);
        txtInitiatorChannel.setEnabled(true);
        // cboInputCurrency.setEnabled(true);
//        txtAccHdId.setEnabled(true);
        //txtInputAmt.setEnabled(true);
        cboInstrumentType.setEnabled(true);
        txtInstrumentNo2.setEnabled(true);
        tdtInstrumentDate.setEnabled(true);
        //  txtTokenNo.setEnabled(true);
        txtAmount.setEnabled(true);
        txtParticulars.setEnabled(true);
        txtNarration.setEnabled(true);

        //system.out.println("issss1" + rdoTransactionType_Debit.isSelected());

//        //        txtAccNo.setText("");
//        //        txtInputAmt.setText("");
//        txtAmount.setText("");
//        //        tdtDocumentDt.setDateValue(null);
//        cboProdType.setEnabled(true);
//        //        cboProdId.setEnabled(true);
//        txtAccHdId.setEnabled(true);
//        //        txtAccNo.setEnabled(true);
//        //        txtInputAmt.setEnabled(true);
//        txtAmount.setEnabled(true);
        cboProdType.setSelectedItem(observable.getCbmProdType().getDataForKey("GL"));
        cboProdType.setEnabled(false);
        txtInitiatorChannel.setText("CASHIER");
        txtAccHdId.setEnabled(false);
    }//GEN-LAST:event_btnNew_Property1ActionPerformed

    private void btnlSave_Property1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlSave_Property1ActionPerformed
        // TODO add your handling code here:
        StringBuffer message = new StringBuffer("");
        if (cboProdType.getSelectedIndex() == 0) {
            message.append("Enter Product Type");
            message.append("\n");
        }
        if (txtAccHdId.getText().equals("")) {
            message.append("Enter Account Head");
            message.append("\n");
        }
        if (!rdoTransactionType_Credit.isSelected() && !rdoTransactionType_Debit.isSelected()) {
            message.append("Select Transaction type");
            message.append("\n");
        }
        if (rdoTransactionType_Debit.isSelected()) {
            if (cboInstrumentType.getSelectedIndex() == 0) {
                message.append("Enter Instrument Type");
                message.append("\n");
            }
            if (tdtInstrumentDate.getDateValue().equals("")) {
                message.append("Enter Instrument Date");
                message.append("\n");
            }
        }
        if (txtAmount.getText().equals("")) {
            message.append("Enter Amount");
            message.append("\n");
        }

        if (message.length() > 0) {
            ClientUtil.displayAlert(message.toString());
            return;
        }
        if(txtAmount.getText()!=null || !txtAmount.getText().equals("")){
//           double receiptTot = 0.0;
//           double paymentTot = 0.0;
           double amt = CommonUtil.convertObjToDouble(txtAmount.getText());
           if(rdoTransactionType_Credit.isSelected()){
             receiptTot = receiptTot + amt;
             txtTotReceipt.setText(CommonUtil.convertObjToStr(receiptTot));
           }else if(rdoTransactionType_Debit.isSelected()){
             paymentTot = paymentTot + amt;
             txtTotPayment.setText(CommonUtil.convertObjToStr(paymentTot));
           }
        }
        String prodType = CommonUtil.convertObjToStr(cboProdType.getSelectedItem());
        String accno = txtAccHdId.getText();
        String debit = "N";
        String credit = "N";
        String instrumentType = "";
        String accHeadDesc = "";
        String instrumentNo = "";
        String instrumentDate = "";
        if (rdoTransactionType_Debit.isSelected()) {
            debit = "Y";
            credit = "N";
            instrumentType = cboInstrumentType.getSelectedItem().toString();
            instrumentNo = txtInstrumentNo2.getText();
            instrumentDate = tdtInstrumentDate.getDateValue();
        } else {
            credit = "Y";
            debit = "N";
        }
        String inistiatorChannel = txtInitiatorChannel.getText();
        String amount = txtAmount.getText();
        String particulars = txtParticulars.getText();
        String membeNarration = txtNarration.getText();
        accHeadDesc = lbHeadDescription.getText();
        String row = CommonUtil.convertObjToStr(new Integer(tblCash.getRowCount() + 1));
        HashMap hmap = new HashMap();
        if (tblCash.getSelectedRow() >= 0) {
            int num = tblCash.getSelectedRow();
            String slno = CommonUtil.convertObjToStr(tblCash.getValueAt(tblCash.getSelectedRow(), 0));
            hmap.put("SNO", slno);
            hmap.put("PROD_TYPE", prodType);
            hmap.put("ACC_NO", accno);
            if (debit.equals("Y")) {
                hmap.put("TRANSACTION_TYPE", "DB");
                hmap.put("TRANS_TYPE", "DEBIT");
                hmap.put("INSTRUMENT_TYPE", instrumentType);
                hmap.put("INSTRUMENT_NO", instrumentNo);
                hmap.put("INSTRUMENT_DATE", instrumentDate);
            } else {
                hmap.put("TRANSACTION_TYPE", "CR");
                hmap.put("TRANS_TYPE", "CREDIT");//Added By Suresh
                hmap.put("INSTRUMENT_TYPE", "");
                hmap.put("INSTRUMENT_NO", "");
                hmap.put("INSTRUMENT_DATE", "");
            }
            //Added By Suresh
            hmap.put("AC_HEAD_DESC", accHeadDesc);
            hmap.put("AMOUNT", amount);
            hmap.put("PARTICULARS", particulars);
            hmap.put("NARRATION", membeNarration);
            System.out.println("accNo1???##1111>>>>"+accNo1);
            hmap.put("LINKID", accNo1);
            bufferList.set(num, hmap);
        } else {
            hmap.put("SNO", row);
            hmap.put("PROD_TYPE", prodType);
            hmap.put("ACC_NO", accno);
            if (debit.equals("Y")) {
                hmap.put("TRANSACTION_TYPE", "DB");
                hmap.put("TRANS_TYPE", "DEBIT");
                hmap.put("INSTRUMENT_TYPE", instrumentType);
                hmap.put("INSTRUMENT_NO", instrumentNo);
                hmap.put("INSTRUMENT_DATE", instrumentDate);

            } else {
                hmap.put("TRANSACTION_TYPE", "CR");
                hmap.put("TRANS_TYPE", "CREDIT");
                hmap.put("INSTRUMENT_TYPE", "");
                hmap.put("INSTRUMENT_NO", "");
                hmap.put("INSTRUMENT_DATE", "");
            }
            hmap.put("AC_HEAD_DESC", accHeadDesc);
            hmap.put("AMOUNT", amount);
            hmap.put("PARTICULARS", particulars);
            hmap.put("NARRATION", membeNarration);
            System.out.println("accNo1???##2222>>>>"+accNo1);
            hmap.put("LINKID", accNo1);
            bufferList.add(hmap);
        }
//        cboProdType.setSelectedItem("");
        rdoTransactionType_Credit.setSelected(false);
        rdoTransactionType_Debit.setSelected(false);
        txtInitiatorChannel.setText("");
//        txtAccHdId.setText("");
//        lbHeadDescription.setText("");
        cboInstrumentType.setSelectedItem("");
        txtInstrumentNo2.setText("");
        tdtInstrumentDate.setDateValue("");
        txtAmount.setText("");
        txtParticulars.setText("");
        txtNarration.setText("");
        cboProdType.setEnabled(false);
        rdoTransactionType_Credit.setEnabled(false);
        rdoTransactionType_Debit.setEnabled(false);
        txtInitiatorChannel.setEnabled(false);
        txtAccHdId.setEnabled(false);
        cboInstrumentType.setEnabled(false);
        txtInstrumentNo2.setEnabled(false);
        tdtInstrumentDate.setEnabled(false);
        txtAmount.setEnabled(false);
        txtParticulars.setEnabled(false);
        txtNarration.setEnabled(false);
        rdoTransactionType_Credit.setSelected(false);
        rdoTransactionType_Debit.setSelected(false);
        btnAccHdId.setEnabled(false);
        getTableData();
        //Added By Suresh
        setSizeTableData();
    }//GEN-LAST:event_btnlSave_Property1ActionPerformed

    private void btnDelete_Property1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete_Property1ActionPerformed
        // TODO add your handling code here:
        if (tblCash.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to Delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            int row = tblCash.getSelectedRow();
            cboProdType.setSelectedItem("");
            rdoTransactionType_Credit.setSelected(false);
            rdoTransactionType_Debit.setSelected(false);
            txtInitiatorChannel.setText("");
            txtAccHdId.setText("");
            lbHeadDescription.setText("");
            cboInstrumentType.setSelectedItem("");
            txtInstrumentNo2.setText("");
            tdtInstrumentDate.setDateValue("");
            txtAmount.setText("");
            txtParticulars.setText("");
            txtNarration.setText("");
            cboProdType.setEnabled(false);
            rdoTransactionType_Credit.setEnabled(false);
            rdoTransactionType_Debit.setEnabled(false);
            txtInitiatorChannel.setEnabled(false);
            txtAccHdId.setEnabled(false);
            cboInstrumentType.setEnabled(false);
            txtInstrumentNo2.setEnabled(false);
            tdtInstrumentDate.setEnabled(false);
            txtAmount.setEnabled(false);
            txtParticulars.setEnabled(false);
            txtNarration.setEnabled(false);
            rdoTransactionType_Credit.setSelected(false);
            rdoTransactionType_Debit.setSelected(false);
            HashMap temp = new HashMap();
            temp = (HashMap) bufferList.get(row);
            System.out.println("temp??###>>>>"+temp);
            if(temp.get("TRANS_TYPE").toString().equals("DEBIT")){
                paymentTot = paymentTot - CommonUtil.convertObjToDouble(temp.get("AMOUNT"));
                txtTotPayment.setText(CommonUtil.convertObjToStr(paymentTot));
            }else{
                receiptTot = receiptTot - CommonUtil.convertObjToDouble(temp.get("AMOUNT"));
                txtTotReceipt.setText(CommonUtil.convertObjToStr(receiptTot));
            }
            int no = Integer.parseInt(temp.get("SNO").toString());
            bufferList.remove(row);
            for (int i = row; i < bufferList.size(); i++) {
                HashMap amap = new HashMap();
                amap = (HashMap) bufferList.get(i);
                bufferList.remove(i);
                amap.put("SNO", no);
                bufferList.add(i, amap);

                no = no + 1;
                //system.out.println("bufff   " + bufferList + "  dfjd no  " + no);
            }
            // bufferList.remove(row);
            getTableData();
            //Added By Suresh
            setSizeTableData();
        }
    }//GEN-LAST:event_btnDelete_Property1ActionPerformed

private void rdoTransactionType_DebitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTransactionType_DebitActionPerformed
    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
//                if (observable.getProdType().equals("TD") || observable.getProdType().equals("TL")) {
//                    txtAccNo.setText("");
//                }
        String token = "";
        List list1;
        boolean tkn = false;
        if (rdoTransactionType_Debit.isSelected()) {
            cboInstrumentType.setEnabled(true);
            txtInstrumentNo2.setEditable(true);
            txtInstrumentNo2.setEnabled(true);
            tdtInstrumentDate.setEnabled(true);
            txtAmount.setEnabled(true);
            // txtPanNo.setText("");
            //  btnPanNo.setEnabled(false);
            if (observable.getProdType().equals("TD")) {
                txtParticulars.setText("");
                txtAmount.setText("");
                //    if (depBehavesLike.equals("DAILY") && depPartialWithDrawalAllowed.equals("Y")) {
                //       txtParticulars.setEnabled(true);
                //   } else {
                //       txtParticulars.setEnabled(false);
                //    }
            } else {
                txtParticulars.setText("To ");
                txtParticulars.setEnabled(true);
//                        txtParticulars.setEditable(true);
            }
            //  if (designation.equals("Teller") && observable.getProdType().equals("OA")) {
//                    txtTokenNo.setEditable(false);
            //      txtTokenNo.setEnabled(false);
            //  } else {
//                        txtTokenNo.setEditable(true);
            //     txtTokenNo.setEnabled(true);
            //  }
        }
    }
    String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
    //system.out.println("prodTypeprodType in test1" + prodType);
    if (!prodType.equals("") && prodType.equals("GL")) {
        HashMap acctMap = new HashMap();
        acctMap.put("ACCT_HEAD", observable.getTxtAccHd());
        List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
        if (head != null && head.size() > 0) {
            acctMap = (HashMap) head.get(0);
            //   if (transCommonUI != null) {
            //       transCommonUI.dispose();
            //       transCommonUI = null;
            //    }
            rdoTransactionType_Credit.setSelected(false);
            if (!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")) {
                if (acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Credit.isSelected() == true) {
                    //   reconcilebtnDisable = true;
                    //  observable.setReconcile("N");
                    //   btnCurrencyInfo.setVisible(true);
                    //  btnCurrencyInfo.setEnabled(true);
                } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Debit.isSelected() == true) {
                    //  reconcilebtnDisable = true;
                    //  observable.setReconcile("N");
                    //  btnCurrencyInfo.setVisible(true);
                    // btnCurrencyInfo.setEnabled(true);
                } else if (acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Debit.isSelected() == true) {
                    // observable.setReconcile("Y");
                    //  btnCurrencyInfo.setVisible(false);
                    //   btnCurrencyInfo.setEnabled(false);
                } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Credit.isSelected() == true) {
                    //   observable.setReconcile("Y");
                    //  btnCurrencyInfo.setVisible(false);
                    //  btnCurrencyInfo.setEnabled(false);
                }
                //   observable.setBalanceType(CommonUtil.convertObjToStr(acctMap.get("BALANCETYPE")));
            } else {
//                        btnCurrencyInfo.setVisible(false);
                //   btnCurrencyInfo.setEnabled(false);
            }
        }
    } else {
        //  btnCurrencyInfo.setVisible(false);
        // btnCurrencyInfo.setEnabled(false);
    }
    // txtParticulars.setText("To ");
}//GEN-LAST:event_rdoTransactionType_DebitActionPerformed

private void rdoTransactionType_CreditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTransactionType_CreditActionPerformed
    // Add your handling code here:
    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
//                if (observable.getProdType().equals("TD") || observable.getProdType().equals("TL")) {
//                    txtAccNo.setText("");
//                }
    }
    if (rdoTransactionType_Credit.isSelected()) {
        String status = "";
        if (status.equals("NRE")) {
            ClientUtil.displayAlert("Credit Transactions Not Allowed For This Product!!!");
            rdoTransactionType_Debit.setSelected(true);
        }
        cboInstrumentType.setSelectedIndex(0);
        //   txtInstrumentNo1.setText("");
        txtInstrumentNo2.setText("");
        tdtInstrumentDate.setDateValue("");

        cboInstrumentType.setEnabled(false);
//                txtInstrumentNo1.setEditable(false);
        // txtInstrumentNo1.setEnabled(false);
//                txtInstrumentNo2.setEditable(false);
        txtInstrumentNo2.setEnabled(false);
        tdtInstrumentDate.setEnabled(false);
        txtParticulars.setText("BY CASH");
//                txtParticulars.setEditable(true);
    }
    String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
    if (!prodType.equals("") && prodType.equals("GL")) {
        HashMap acctMap = new HashMap();
        acctMap.put("ACCT_HEAD", observable.getTxtAccHd());
        List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
        if (head != null && head.size() > 0) {
            acctMap = (HashMap) head.get(0);
            //    if (transCommonUI != null) {
            ///       transCommonUI.dispose();
            //       transCommonUI = null;
            //    }
            rdoTransactionType_Debit.setSelected(false);
            if (!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")) {
                if (acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Credit.isSelected() == true) {
                    //   reconcilebtnDisable = true;
                    //  observable.setReconcile("N");
                    // btnCurrencyInfo.setVisible(true);
                    //  btnCurrencyInfo.setEnabled(true);
                } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Debit.isSelected() == true) {
                    //   reconcilebtnDisable = true;
                    //  observable.setReconcile("N");
                    //  btnCurrencyInfo.setVisible(true);
                    //  btnCurrencyInfo.setEnabled(true);
                } else if (acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Debit.isSelected() == true) {
                    //  observable.setReconcile("Y");
                    //  btnCurrencyInfo.setVisible(false);
                    //  btnCurrencyInfo.setEnabled(false);
                } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Credit.isSelected() == true) {
                    //   observable.setReconcile("Y");
                    //  btnCurrencyInfo.setVisible(false);
                    //  btnCurrencyInfo.setEnabled(false);
                }
                // observable.setBalanceType(CommonUtil.convertObjToStr(acctMap.get("BALANCETYPE")));
            } else {
                //   btnCurrencyInfo.setVisible(false);
                //   btnCurrencyInfo.setEnabled(false);
            }
        }
    } else {
        //  btnCurrencyInfo.setVisible(false);
        //  btnCurrencyInfo.setEnabled(false);
    }
    //  rdoTransactionType_DebitActionPerformed(evt);

    txtParticulars.setText("By Cash");
}//GEN-LAST:event_rdoTransactionType_CreditActionPerformed

private void cboInstrumentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboInstrumentTypeActionPerformed
    // TODO add your handling code here:
    Date curDate = (Date) currDt.clone();
    String instrumentType = CommonUtil.convertObjToStr(((ComboBoxModel) cboInstrumentType.getModel()).getKeyForSelected());
    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && cboInstrumentType.getSelectedIndex() > 0) {

        if (instrumentType.equals("VOUCHER") || instrumentType.equals("WITHDRAW_SLIP")) {
            tdtInstrumentDate.setDateValue(DateUtil.getStringDate(curDate));
            tdtInstrumentDate.setEnabled(false);
        } else {
            tdtInstrumentDate.setDateValue(DateUtil.getStringDate(curDate));
            tdtInstrumentDate.setEnabled(true);
        }
    }
}//GEN-LAST:event_cboInstrumentTypeActionPerformed

private void cboInstrumentTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboInstrumentTypeFocusLost
    //   instrumentTypeFocus();

}//GEN-LAST:event_cboInstrumentTypeFocusLost

private void tdtInstrumentDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtInstrumentDateFocusLost
    // Add your handling code here:
    //ClientUtil.validateLTDate(tdtInstrumentDate);
}//GEN-LAST:event_tdtInstrumentDateFocusLost

private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountActionPerformed
    
}//GEN-LAST:event_txtAmountActionPerformed

private void txtAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusLost
    // TODO add your handling code here:
    double transAmt = 0;
    if (CommonUtil.convertObjToStr(txtAmount.getText()).length() > 0) {
        transAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
    }
    if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
        txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));
    } else {
        txtAmount.setToolTipText("Zero");
    }

    String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
    if (prodType.equals("GL")) {
        //   btnPanNo.setEnabled(false);
        //   txtPanNo.setText("");
    }
    /* added by nikhil for the view TermLoan details operation */
    if ((prodType.equals("TL") || prodType.equals("AD")) && txtAmount.getText().length() > 0) {
        // btnViewTermLoanDetails.setVisible(true);
        //   btnViewTermLoanDetails.setEnabled(true);
//
        if (!(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT)) {
            
        }
    } else {
//             btnViewTermLoanDetails.setVisible(false);
//             btnViewTermLoanDetails.setEnabled(false);
    }
    if (rdoTransactionType_Credit.isSelected() == true) {
        String prodId = "";
        if (!((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString().equals("GL")) {
            //  prodId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
        }
        double penalAmt = 0.0;//CommonUtil.convertObjToDouble(transDetails.getPenalAmount()).doubleValue();
        //system.out.println("#####ProdId :" + prodId + "penalAmt :" + penalAmt);
        HashMap prodMap = new HashMap();
        prodMap.put("PROD_ID", prodId.toString());
        List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
        if (lst != null && lst.size() > 0) {
            prodMap = (HashMap) lst.get(0);
            prodMap = null;
            lst = null;
        }
        //   moreThanLoanAmountAlert();
        if (!prodType.equals("GL")) {
            double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
            if (amount >= TrueTransactMain.PANAMT) {
                //  btnPanNo.setEnabled(true);
            } else if (amount < TrueTransactMain.PANAMT) {
                //  btnPanNo.setEnabled(false);
                //  txtPanNo.setText("");
            }
        }
    }
    if (rdoTransactionType_Debit.isSelected() == true) {
        String prodId = "";
        if (!((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString().equals("GL")) {
            //  prodId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
        }
        //system.out.println("#####ProdId :" + prodId);
        HashMap prodMap = new HashMap();
        prodMap.put("PROD_ID", prodId.toString());
        List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
        //system.out.println("######## BHEAVES :" + lst);
        if (lst.size() > 0) {
            prodMap = (HashMap) lst.get(0);
            if (prodMap.get("BEHAVES_LIKE").equals("FIXED")) {
                HashMap recurringMap = new HashMap();
                double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                lst = ClientUtil.executeQuery("getInterestDeptIntTable", recurringMap);
                if (lst.size() > 0) {
                    recurringMap = (HashMap) lst.get(0);
                    double depAmount = CommonUtil.convertObjToDouble(recurringMap.get("PERIODIC_INT_AMT")).doubleValue();
                    double finalAmount = amount % depAmount;
                    //system.out.println("######## BHEAVES REMAINING :" + finalAmount);
                    if (finalAmount == 0) {
                        txtAmount.setEnabled(false);
                        //system.out.println("######## BHEAVES :" + finalAmount);
                    } else {
                        //                            ClientUtil.displayAlert("Enter Amount in Multiples of Deposit Amount... ");
                        //                            txtAmount.setText("");
                    }
                }
                recurringMap = null;
            } else if (prodMap.get("BEHAVES_LIKE").equals("DAILY") && prodMap.get("PARTIAL_WITHDRAWAL_ALLOWED").equals("Y")) {
                HashMap dailyDepMap = new HashMap();
                double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                //     String depNo = txtAccNo.getText();
                //                    //system.out.println("########Amount : "+amount +"####DepNo :"+depNo);
                //depNo = depNo.substring(0, depNo.lastIndexOf("_"));
                //                    //system.out.println("######## BHEAVES :"+depNo);
                //    dailyDepMap.put("DEPOSIT_NO", depNo);
                lst = ClientUtil.executeQuery("getDepAvailBalForPartialWithDrawal", dailyDepMap);
                if (lst.size() > 0) {
                    dailyDepMap = (HashMap) lst.get(0);
                    double depAmount = CommonUtil.convertObjToDouble(dailyDepMap.get("AVAILABLE_BALANCE")).doubleValue();
                    if (depAmount < amount) {
                        //system.out.println("@#$@#$%$^$%^amount" + amount + " :depAmount: " + depAmount);
                        ClientUtil.displayAlert("Amount Greater than available balance!!");
                        txtAmount.setText("");
                        return;
                    }
                }
                dailyDepMap = null;

            }
            prodMap = null;
            lst = null;
        }
    }
    if (CommonUtil.convertObjToDouble(this.txtAmount.getText()).doubleValue() <= 0) {
        ClientUtil.displayAlert("amount should not be zero or empty");
        return;
    }
}//GEN-LAST:event_txtAmountFocusLost

    private void txtInitiatorChannelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInitiatorChannelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInitiatorChannelActionPerformed

    private void tblCashMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCashMouseClicked
        int row = tblCash.getSelectedRow();
        //system.out.println("row..." + row);
        if (row >= 0) {
            HashMap amap = new HashMap();
            amap = (HashMap) bufferList.get(row);
            //system.out.println("amap...." + amap);
            //   cboProdType.setSelectedItem((ComboBoxModel)observable.getCbmProdType().getDataForKey(amap.get("PROD_TYPE").toString()));
            cboProdType.setSelectedItem(amap.get("PROD_TYPE").toString());
            txtAccHdId.setText(amap.get("ACC_NO").toString());
            if (amap.get("TRANSACTION_TYPE").toString().equals("DB")) {

                rdoTransactionType_Debit.setSelected(true);
                rdoTransactionType_DebitActionPerformed(null);
                //  cboInstrumentType.setSelectedItem((ComboBoxModel)observable.getCbmProdType().getDataForKey(amap.get("INSTRUMENT_TYPE").toString()));
                cboInstrumentType.setSelectedItem(amap.get("INSTRUMENT_TYPE").toString());
                txtInstrumentNo2.setText(amap.get("INSTRUMENT_NO").toString());
                tdtInstrumentDate.setDateValue(amap.get("INSTRUMENT_DATE").toString());

            } else {
                rdoTransactionType_Credit.setSelected(true);
                rdoTransactionType_CreditActionPerformed(null);
                //cboInstrumentType.setSelectedItem((ComboBoxModel)observable.getCbmProdType().getDataForKey(amap.get("INSTRUMENT_TYPE").toString()));
                cboInstrumentType.setSelectedItem("");
                txtInstrumentNo2.setText(amap.get("INSTRUMENT_NO").toString());
                tdtInstrumentDate.setDateValue(amap.get("INSTRUMENT_DATE").toString());
            }
            lbHeadDescription.setText(amap.get("AC_HEAD_DESC").toString());
            txtAmount.setText(amap.get("AMOUNT").toString());
            txtParticulars.setText(amap.get("PARTICULARS").toString());
            txtNarration.setText(amap.get("NARRATION").toString());
            txtAccHdId.setEnabled(false);
			  lblAccNoGl.setText(amap.get("LINKID").toString());
        }
    }//GEN-LAST:event_tblCashMouseClicked

    private void txtAccHdIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccHdIdFocusLost
        // TODO add your handling code here:
        if(txtAccHdId.getText()!=null && !txtAccHdId.getText().equalsIgnoreCase("")){
            HashMap hmap = new HashMap();
            String customerAllow="";
            hmap.put("ACHEAD", txtAccHdId.getText());
            List list = ClientUtil.executeQuery("getCustomerAlloowProperty", hmap);
            if (list != null && list.size() > 0) {
                hmap = (HashMap) list.get(0);
                customerAllow = CommonUtil.convertObjToStr(hmap.get("ALLOW_CUSTOMER_ACNUM"));
                //  hoAc = CommonUtil.convertObjToStr(hmap.get("HO_ACCT"));
            }
            //            if (bankType.equals("DCCB")) {
                //                if (hoAc.equals("Y")) {
                    //                    btnOrgOrResp.setVisible(true);
                    //                    observable.setHoAccount(true);
                    //                } else {
                    //                    observable.setHoAccount(false);
                    //                    btnOrgOrResp.setVisible(false);
                    //                }
                //            }
            System.out.println("customerAllow>>>>"+customerAllow);
            if (customerAllow.equals("Y")) {
                System.out.println("innnnn");
                CInternalFrame frm = new CInternalFrame();
                frm = new com.see.truetransact.ui.transaction.cash.GLAccountNumberListUI(this);
                frm.setSelectedBranchID(getSelectedBranchID());
                //frm.setSize(1000,1000);
                TrueTransactMain.showScreen(frm);

                //               final CInternalFrame frame = new CInternalFrame();
                //               CDesktopPane desktop = new CDesktopPane();
                //               glAccountNumberListUI=new GLAccountNumberListUI();
                //        //frame.setLocation(xOffset * openFrameCount, yOffset * openFrameCount);
                //        frame.setSize(200, 100);
                //        frame.setVisible(true);
                //        frame.getContentPane().add(glAccountNumberListUI);
                //        desktop.add(frame);

                //                GLAccountNumberListUI glAccNo = new GLAccountNumberListUI();
                //                glAccNo.show();
                //                glAccNo.setVisible(true);
                //                String AccNo = COptionPane.showInputDialog(this, "Enter Acc no");
                //                hmap.put("ACC_NUM", AccNo);
                //                List chkList = ClientUtil.executeQuery("checkAccStatus", hmap);
                //                if (chkList != null && chkList.size() > 0) {
                    //                    hmap = (HashMap) chkList.get(0);
                    //                    observable.setLblAccName(CommonUtil.convertObjToStr(hmap.get("NAME")));
                    //                    observable.setTxtAccNo(AccNo);
                    //                    observable.setClosedAccNo(AccNo);
                    //                } else {
                    //                    ClientUtil.displayAlert("Invalid Account number");
                    //                    txtAccHdId.setText("");
                    //                    return;
                    //                }
            }
        }
    }//GEN-LAST:event_txtAccHdIdFocusLost

    private void txtTotPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotPaymentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotPaymentActionPerformed

    private void txtTotPaymentFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotPaymentFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotPaymentFocusLost

    private void txtTotReceiptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotReceiptActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotReceiptActionPerformed

    private void txtTotReceiptFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotReceiptFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotReceiptFocusLost
    public void fillAccNo(String accNo,String prodId,String prodType){
//    System.out.println("in fill accno>>>"+accNo);
//    System.out.println("prodId in fillaccno>>>"+prodId);
//    System.out.println("prodType in fillaccno>>>"+prodType);
    accNo1=accNo;
    prodIdforgl=prodId;
    prodtypeforgl=prodType;
    lblAccNoGl.setVisible(true);
    lblAccNoGl.setEnabled(true);
    if(accNo1!=null)
    lblAccNoGl.setText(accNo1);
    System.out.println("lblAccNoGl.getText>>>"+lblAccNoGl.getText());
//    observable.setAccountName(lblAccNoGl.getText().toString());
//    lblAccName.setText(observable.getLblAccName());
//    lblHouseName.setText(observable.getLblHouseName());
    
            //To set the  Name of the Account Holder...
        String pType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();

  //      System.out.println("observable.getLblAccName()>>>"+observable.getLblAccName());
   //     String prevActNum = observable.getLblAccName();
        System.out.println("pType>>>"+pType);
        if (pType.length() > 0) {
            if (pType.equals("GL")) {
               setAccountName1(lblAccNoGl.getText().toString());
             //   System.out.println("observable.getLblAccName()>>>"+observable.getLblAccName());
              //  lblAccName.setText(observable.getLblAccName());
              //  System.out.println("observable.getLblHouseName()>>>"+observable.getLblHouseName());
              //  lblHouseName.setText(observable.getLblHouseName());
            //    observable.setAccountHead();
            //    System.out.println("observable.getLblAccHdDesc()@@>>>"+observable.getLblAccHdDesc());
              //  lblAccHdDesc.setText(observable.getLblAccHdDesc());
            }
        }

    
}
    
        public void setAccountName1(String AccountNo){
        HashMap resultMap = new HashMap();
        final HashMap accountNameMap = new HashMap();
        List resultList = new ArrayList();
        try {
            if (!prodtypeforgl.equals("")) {
                accountNameMap.put("ACC_NUM",AccountNo);
                String pID = !prodtypeforgl.equals("GL") ? prodIdforgl.toString() : "";
                if(prodtypeforgl.equals("GL") && AccountNo.length()>0){
                    resultList = ClientUtil.executeQuery("getAccountNumberNameTL",accountNameMap);
                } else {
                    resultList = ClientUtil.executeQuery("getAccountNumberName"+prodtypeforgl,accountNameMap);
                    List custHouseNameList = ClientUtil.executeQuery("getCustomerHouseName",accountNameMap);
                    if(custHouseNameList!=null && custHouseNameList.size()>0){
                        HashMap dataMap = new HashMap();
                        dataMap = (HashMap) custHouseNameList.get(0);
                        lblHouseName.setText(CommonUtil.convertObjToStr(dataMap.get("HOUSE_NAME")));
                        System.out.println("lblHouseName>>>"+lblHouseName.getText());
                    }
                }
                if(resultList != null && resultList.size() > 0){
                    if(!prodtypeforgl.equals("GL") && !prodtypeforgl.equals("SH")) {
                        HashMap dataMap = new HashMap();
                        accountNameMap.put("BRANCH_ID", getSelectedBranchID());
                        List lst = (List) ClientUtil.executeQuery("getProdIdForActNo"+prodtypeforgl,accountNameMap);
                        if(lst != null && lst.size() > 0)
                            dataMap = (HashMap) lst.get(0);
                        if(dataMap.get("PROD_ID").equals(pID)){
                            resultMap = (HashMap)resultList.get(0);
                        }
                    } else {
                        resultMap = (HashMap)resultList.get(0);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        if(resultMap.containsKey("CUSTOMER_NAME")){
            lblAccName.setText(CommonUtil.convertObjToStr(resultMap.get("MEMBERSHIP_NO")) + ' ' + ' ' + resultMap.get("CUSTOMER_NAME").toString());
        System.out.println("lblAccName>>>>>>"+lblAccName.getText());
        }
        else
            lblAccName.setText("");
        //            if(resultList != null){
        //                final HashMap resultMap = (HashMap)resultList.get(0);
        //                setLblAccName(resultMap.get("CUSTOMER_NAME").toString());
        //            } else {
        //                setLblAccName("");
        //            }
        //        }catch(Exception e){
        //
        //        }
    }
    private void initComponentData() {

        cboProdType.setModel(observable.getCbmProdType());
        // cboInputCurrency.setModel(observable.getCbmInputCurrency());
        cboInstrumentType.setModel(observable.getCbmInstrumentType());
        cboProdType.setSelectedItem(observable.getCbmProdType().getDataForKey("GL"));
        
    }

    /**
     * To display a popUp window for viewing existing data
     */
    private void popUp(String currAction) {
        //  viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            // map.put("BRANCH_CODE","0001");
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getVisitorsDiaryEdit");
        } else if (currAction.equalsIgnoreCase("Delete")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getVisitorsDiaryEdit");
        } else if (currAction.equalsIgnoreCase("CUST")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "setCustId");
        }

        new ViewAll(this, viewMap).show();

    }

    /**
     * Called by the Popup window created thru popUp method
     */
    public void fillData(Object param) {
        try {
            //system.out.println("In fillData test...............");
            final HashMap hash = (HashMap) param;
            //system.out.println("param DATE ======" + param);
            //system.out.println("HASH DATE ======" + hash);
            double depAmt = 0.0;
            HashMap mapList;
            if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
                fromNewAuthorizeUI = true;
                newauthorizeListUI= (NewAuthorizeListUI) hash.get("PARENT");
                hash.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(false);
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
                btnReject.setEnabled(false);
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
                btnReject.setEnabled(false);
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
                btnReject.setEnabled(false);
                rejectFlag = 1;
            }
            if (viewType == AUTHORIZE) {
                HashMap amap = new HashMap();
                HashMap hmap;
                amap.put("FROM_TRID", hash.get("TRANS_ID"));
                amap.put("TRANS_DT", CommonUtil.getProperDate(currDt,currDt));
                amap.put("CASH", "CASH");
                observable.setTransId(hash.get("TRANS_ID").toString());
                bufferList.clear();
                List listSingleTransId = ClientUtil.executeQuery("getSingTrId", amap);
                if (listSingleTransId != null && listSingleTransId.size() > 0) {
                    HashMap singleTransIdMap = (HashMap) listSingleTransId.get(0);
                    if (singleTransIdMap != null && singleTransIdMap.size() > 0 && singleTransIdMap.containsKey("SINGLE_TRANS_ID")) {
                        observable.setSingleTransId(CommonUtil.convertObjToStr(singleTransIdMap.get("SINGLE_TRANS_ID")));
                        singleTransIdMap.put("TRANS_DT", currDt);
                        List list = ClientUtil.executeQuery("getAllForSelectedFromCash", singleTransIdMap);
                        System.out.println("list###%%%?>>>" + list);
                        for (int i = 0; i < list.size(); i++) {
                            mapList = new HashMap();
                            mapList = (HashMap) list.get(i);
                            System.out.println("mapList####22@@@>>>>" + mapList);
                            hmap = new HashMap();
                            hmap.put("SNO", i + 1);
                            hmap.put("PROD_TYPE", "GL");
                            hmap.put("ACC_NO", mapList.get("AC_HD_ID"));
                            if (mapList.get("TRANS_TYPE").equals("DEBIT")) {
                                hmap.put("TRANSACTION_TYPE", "DB");
                                hmap.put("TRANS_TYPE", "DEBIT");                        //Added By Suresh
                                hmap.put("INSTRUMENT_TYPE", mapList.get("INST_TYPE"));
                                hmap.put("INSTRUMENT_NO", mapList.get("INSTRUMENT_NO2"));
                                hmap.put("INSTRUMENT_DATE", mapList.get("INST_DT"));

                            } else {
                                hmap.put("TRANSACTION_TYPE", "CR");
                                hmap.put("TRANS_TYPE", "CREDIT");                       //Added By Suresh
                                hmap.put("INSTRUMENT_TYPE", "");
                                hmap.put("INSTRUMENT_NO", "");
                                hmap.put("INSTRUMENT_DATE", "");
                            }
                            hmap.put("AC_HEAD_DESC", CommonUtil.convertObjToStr(mapList.get("AC_HD_DESC"))); //Added By Suresh
                            hmap.put("AMOUNT", mapList.get("AMOUNT"));
                            hmap.put("PARTICULARS", mapList.get("PARTICULARS"));
                            hmap.put("NARRATION", mapList.get("NARRATION"));

                            if (mapList.get("LINK_BATCH_ID") != null) {
                                hmap.put("LINKID", mapList.get("LINK_BATCH_ID"));
                            }
                            bufferList.add(hmap);
                            if (mapList.get("LINK_BATCH_ID") != null) {
                                HashMap houseMap = new HashMap();
                                houseMap.put("ACC_NUM", mapList.get("LINK_BATCH_ID").toString());
                                List houseList = ClientUtil.executeQuery("getSelectCustNameHouseNameForMultipleCash", houseMap);
                                if (houseList != null && houseList.size() > 0) {
                                    houseMap = (HashMap) houseList.get(0);
                                    lblHouseName.setText(houseMap.get("STREET").toString());
                                    lblAccName.setText(houseMap.get("FNAME").toString());
                                }
                            }
                        }
                    }
                }
                System.out.println("oooooiiii");
                getTableData();
                isFilled = true;
                //Added By Suresh
                setSizeTableData();
            }

            if (viewType == ACCTHDID) {
                String acHdId = hash.get("A/C HEAD").toString();
                String bankType = CommonConstants.BANK_TYPE;
                //system.out.println("bankType" + bankType);
                String customerAllow = "";
                String hoAc = "";
//            cboProdId.setSelectedItem("");
//            this.txtAccNo.setText("");
                txtAccHdId.setText(acHdId);
                //Added By Suresh
                lbHeadDescription.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD DESCRIPTION")));
                HashMap hmap = new HashMap();
                hmap.put("ACHEAD", acHdId);
                List list = ClientUtil.executeQuery("getCustomerAlloowProperty", hmap);
                if (list != null && list.size() > 0) {
                    hmap = (HashMap) list.get(0);
                    customerAllow = CommonUtil.convertObjToStr(hmap.get("ALLOW_CUSTOMER_ACNUM"));
                    hoAc = CommonUtil.convertObjToStr(hmap.get("HO_ACCT"));
                }
                if (bankType.equals("DCCB")) {
                    if (hoAc.equals("Y")) {
//                    btnOrgOrResp.setVisible(true);
//                    observable.setHoAccount(true);
                    } else {
//                    observable.setHoAccount(false);
//                    btnOrgOrResp.setVisible(false);
                    }
                }
            if (customerAllow.equals("Y")) {  //change1
//                String AccNo = COptionPane.showInputDialog(this, "Enter Acc no");
//                hmap.put("ACC_NUM", AccNo);
//                List chkList = ClientUtil.executeQuery("checkAccStatus", hmap);
//                if (chkList != null && chkList.size() > 0) {
//                    hmap = (HashMap) chkList.get(0);
////                    observable.setLblAccName(CommonUtil.convertObjToStr(hmap.get("NAME")));
////                    observable.setTxtAccNo(AccNo);
////                    observable.setClosedAccNo(AccNo);
//                } else {
//                    ClientUtil.displayAlert("Invalid Account number");
//                    txtAccHdId.setText("");
//                    return;
//                }
                 System.out.println("innnnn@@333");
                CInternalFrame frm = new CInternalFrame();
                frm = new com.see.truetransact.ui.transaction.cash.GLAccountNumberListUI(this);
                frm.setSelectedBranchID(getSelectedBranchID());
               //frm.setSize(1000,1000);
                TrueTransactMain.showScreen(frm);
                
                }
//            observable.setCr_cash(hash.get("CR_CASH").toString());
//            observable.setDr_cash(hash.get("DR_CASH").toString());
//            observable.setBalanceType((String) hash.get("BALANCETYPE"));
                observable.setTxtAccHd(acHdId);
                observable.setCboProdType((String) cboProdType.getSelectedItem());
                //system.out.println("observable.getProdType(), acHdId " + observable.getProdType() + ":" + acHdId);
//            transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, acHdId);
//            transDetails.setSourceFrame(this);
                observable.ttNotifyObservers();

//            this.setRadioButtons();
            }
            if (rejectFlag == 1) {
                btnReject.setEnabled(false);
            }



        } catch (Exception e) {
            log.error(e);
        }
    }

//     private void setRadioButtons() {
//        this.removeRadioButtons();
//        this.rdoTransactionType_Credit.setEnabled(true);
//        this.rdoTransactionType_Debit.setEnabled(true);
//        this.rdoTransactionType_Debit.setSelected(false);
//        this.rdoTransactionType_Credit.setSelected(false);
//        this.rdoTransactionType_DebitActionPerformed(null);
//
//        if (observable.getCr_cash().equalsIgnoreCase("Y")
//                && observable.getDr_cash().equalsIgnoreCase("Y")) {
//            this.addRadioButtons();
//            return;
//        } else if (observable.getCr_cash().equalsIgnoreCase("Y")) {
//            this.rdoTransactionType_Credit.setSelected(true);
//            this.rdoTransactionType_CreditActionPerformed(null);
//            this.rdoTransactionType_Debit.setEnabled(false);
//        } else if (observable.getDr_cash().equalsIgnoreCase("Y")) {
//            this.rdoTransactionType_Debit.setSelected(true);
//            this.rdoTransactionType_DebitActionPerformed(null);
//            this.rdoTransactionType_Credit.setEnabled(false);
//        }
//
//        this.addRadioButtons();
//    }
//    
    private void enableDisable(boolean yesno) {
        ClientUtil.enableDisable(this, yesno);
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

        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());

    }

    public void update(Observable observed, Object arg) {
        // removeRadioButtons();
//        txtComplaintid=observable.getTxtComplaintid();
//        tdtDateofComplaint.setDateValue(observable.getTxtDateofComplaint());
//        txtCustid.setText(observable.getTxtEmployeeId());
//         txaNameAddress.setText(observable.getTxaNameAddress());
//         txaComments.setText(observable.getTxaComments());
//         lblStatus.setText(observable.getLblStatus());
    }

    public void updateOBFields() {

        observable.setList(bufferList);
        observable.setScreen(this.getScreen());
    }

    private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap : " + proxyResultMap);
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
                for (int k = 0; k < tempList.size(); k++) {
                    List list = (ArrayList) tempList.get(k);
                    System.out.println("list here"+list);        
                    for (int j = 0; j < list.size(); j++) {
                        transMap = (HashMap) list.get(j);
                        System.out.println("transMap----"+transMap);
                        if (j <= 1) {
                            transId = (String) transMap.get("SINGLE_TRANS_ID");
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
                            cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                    + "   Amount : " + transMap.get("AMOUNT") + "\n";
                        }
                    }
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
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
                        transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
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

        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        System.out.println("#$#$$ transIdList : " + transIdList);
        if(viewType==AUTHORIZE){
	        btnAuthorize.setEnabled(true);
	        btnAuthorize.requestFocusInWindow();
	        btnAuthorize.setFocusable(true);
       } 
       if (yesNo == 0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("TransDt", currDt);
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
           // for (int i = 0; i < transIdList.size(); i++) {
                paramMap.put("TransId", transIdList.get(0));
                ttIntgration.setParam(paramMap);
                    if (transMap.get("TRANS_TYPE").equals("CREDIT")) {
                          ttIntgration.integrationForPrint("CashReceipt", false);
                    } else {
                           ttIntgration.integrationForPrint("CashPayment", false);
                    }
           // }
        }
    }

    private void savePerformed() {

        updateOBFields();
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {

            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            // lst.add("EMP_TRANSFER_ID");
            //lockMap.put("EMP_TRANSFER_ID",CommonUtil.convertObjToStr(lblEmpTransferID.getText()));
            // lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                //   lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap() != null) {
                    if (observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST") || observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                        displayTransDetail(observable.getProxyReturnMap());
                    }
                }
            }
            if (observable.getResult() == ClientConstants.ACTIONTYPE_EDIT) {
                // lockMap.put("EMP_TRANSFER_ID", observable.getTxtEmpTransferID());
            }
            // setEditLockMap(lockMap);
            ////  setEditLock();
            //  deletescreenLock();
        }

        observable.resetForm();
        bufferList.clear();
        getTableData();
        //Added By Suresh
        setSizeTableData();
        // enableDisable(false);
        // setButtonEnableDisable();
        // lblStatus.setText(observable.getLblStatus());
        // ClientUtil.enableDisable(this, false);
        // observable.setResultStatus();
        // lblStatus.setText(observable.getLblStatus());
        //__ Make the Screen Closable..
        // setModified(false);
        //  ClientUtil.clearAll(this);
        // observable.ttNotifyObservers();

    }

    private void setFieldNames() {
        //  tdtDateofComplaint.setName("tdtDateofComplaint");
        //  txaNameAddress.setName("txaNameAddress");
        //  txtCustid.setName("txtCustid");
        //  txaComments.setName("txaComments");
        //   lblDateofComplaint.setName("lblDateofComplaint");
        //   lblNameAddress.setName("lblNameAddress");
        //   lblCustId.setName("lblCustId");
        //   lblComments.setName("lblComments");
        //   panComplaint.setName("panComplaint");
        //   panComplains.setName("panComplains");
    }

    private void internationalize() {
        //        java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.customer.customeridchange.CustomerIdChangeRB", ProxyParameters.LANGUAGE);
        //        lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
        //        lblProd.setText(resourceBundle.getString("lblProd"));
        //        lblOldCustNum.setText(resourceBundle.getString("lblOldCustNum"));
        //        lblNewCustNum.setText(resourceBundle.getString("lblNewCustNum"));
        //        lblProdType.setText(resourceBundle.getString("lblProdType"));
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        //   cmd.setMessage(message);
        //   cmd.show();
    }

    private void deletescreenLock() {
        HashMap map = new HashMap();
        // map.put("USER_ID",ProxyParameters.USER_ID);
        // map.put("TRANS_DT", currDt.clone());
        // map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        // ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccHdId;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDateChange;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDelete_Property1;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNew_Property1;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnlSave_Property1;
    private com.see.truetransact.uicomponent.CButtonGroup cButtonGroup1;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CPanel cPanel3;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CComboBox cboInstrumentType;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CLabel lbHeadDescription;
    private com.see.truetransact.uicomponent.CLabel lblAccHd;
    private com.see.truetransact.uicomponent.CLabel lblAccName;
    private com.see.truetransact.uicomponent.CLabel lblAccNoGl;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblAmount1;
    private com.see.truetransact.uicomponent.CLabel lblHouseName;
    private com.see.truetransact.uicomponent.CLabel lblInitiatorChannel;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentDate;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentNo;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentType;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNarration;
    private com.see.truetransact.uicomponent.CLabel lblParticulars;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
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
    private com.see.truetransact.uicomponent.CLabel lblSpace57;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotPayment;
    private com.see.truetransact.uicomponent.CLabel lblTotReceipt;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panCashTransaction;
    private com.see.truetransact.uicomponent.CPanel panData;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransactionType;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransactionType_Credit;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransactionType_Debit;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpTxtAreaParticulars;
    private com.see.truetransact.uicomponent.CTable tblCash;
    private javax.swing.JToolBar tbrVisitorsDiary;
    private com.see.truetransact.uicomponent.CDateField tdtInstrumentDate;
    private com.see.truetransact.uicomponent.CTextField txtAccHdId;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtInitiatorChannel;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentNo2;
    private com.see.truetransact.uicomponent.CTextArea txtNarration;
    private com.see.truetransact.uicomponent.CTextField txtParticulars;
    private com.see.truetransact.uicomponent.CTextField txtTotPayment;
    private com.see.truetransact.uicomponent.CTextField txtTotReceipt;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        MultipleCashTransactionUI complaints = new MultipleCashTransactionUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(complaints);
        j.show();
        complaints.show();
    }
}
