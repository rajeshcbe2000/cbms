/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MDSMemberReceiptEntryUI.java
 *
 * Created on November 26, 2003, 11:27 AM
 */
package com.see.truetransact.ui.mdsapplication.mdsmemberreceiptentry;

import com.see.truetransact.ui.termloan.customerDetailsScreen.CustomerDetailsScreenUI;
import java.util.*;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelListener;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CComboBox;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import static com.see.truetransact.ui.transaction.cash.CashierApprovalUI.isNumeric;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;

/**
 *
 * @author Suresh
 *
 */
public class MDSMemberReceiptEntryUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {

    private final static ClientParseException parseException = ClientParseException.getInstance();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.mdsapplication.mdsmemberreceiptentry.MDSMemberReceiptEntryRB", ProxyParameters.LANGUAGE);
    final MDSMemberReceiptEntryMRB objMandatoryMRB = new MDSMemberReceiptEntryMRB();
    TransactionUI transactionUI = new TransactionUI();
    private HashMap mandatoryMap;
    private MDSMemberReceiptEntryOB observable;
    private String viewType = "";
    private final String AUTHORIZE = "AUTHORIZE";
    private boolean isFilled = false;
    private TableModelListener tableModelListener;
    DefaultTableModel model = null;
    DefaultTreeModel root;
    DefaultTreeModel child;
    private boolean rdoPrizedMember_Yes = false;
    private boolean rdoPrizedMember_No = false;
    HashMap finalMap = new HashMap();
    private Date curr_dt = null;
    private List bufferList = new ArrayList();
    private Object columnNames[] = {"Member No", "Member Name", "Member Type"};
    private ArrayList bonusAmountList = null;
    private ArrayList penalList = null;
    private ArrayList penalRealList = null;
    private ArrayList instList = null;
    private ArrayList narrationList = null;
    //private HashMap splitTransMap;
    private HashMap finalSplitMap = new HashMap();
    private String isSplitMDSTransaction = "";
    private String isWeeklyOrMonthlyScheme = "";
    private int instFrequency = 0;
    boolean fromAuthorizeUI = false;
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;

    /**
     * Creates new form BeanForm
     */
    public MDSMemberReceiptEntryUI() {
        initComponents();
        tabRemittanceProduct.resetVisits();
        settingupUI();
    }

    private void settingupUI() {
        setObservable();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        initComponentData();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panInsideMemberReceiptDetails);
        setMaximumLength();
        setHelpMessage();
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        panMemberReceiptTrans.add(transactionUI);
        transactionUI.setSourceScreen("MDS_MEMBER_RECEIPT");
        observable.setTransactionOB(transactionUI.getTransactionOB());
        initTableData();
        setSizeTableData();
        this.setBounds(0, 0, 720, 550);
        curr_dt = ClientUtil.getCurrentDate();
    }

    private void getTableData() {

        Object rowData[][] = new Object[+bufferList.size()][3];
        //  Integer rowData[][] = new  Integer[buffer1.size()][3];
        int j = 0;
        String d1 = "";
        String d2 = "";
        int i = 0;
        for (i = 0; i < bufferList.size(); i++) {
            HashMap m = new HashMap();
            m = (HashMap) bufferList.get(i);
            rowData[i][0] = m.get("MEM_NO").toString();
            rowData[i][1] = m.get("MEM_NAME").toString();
            rowData[i][2] = m.get("MEM_TYPE").toString();
        }
        tblMemberGrid.setModel(new javax.swing.table.DefaultTableModel(rowData, columnNames) {

            public boolean isCellEditable(int row, int column) {
                //Only the third column
                return false;
            }
        });
        tblMemberGrid.setVisible(true);
    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public void initTableData() {
        tblMemberReceipt.setModel(new javax.swing.table.DefaultTableModel(
                setTableData(),
                new String[]{
                    "MemberNo", "MemberName", "Scheme", "SchemeDesc", "ChittalNo", "Sub No", "InstDue", "NoOfInsPay", "TotPayable", "Bonus", "Discount", "InsAmtPayable",
                    "Interest", "Notice", "Arbitration", "NetAmt",}) {

            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 7 || columnIndex == 12 /*
                         * || columnIndex == 13 commended by sreekrishnan
                         */) {
                    return true;
                }
                return canEdit[columnIndex];
            }
        });
        tblMemberReceipt.setCellSelectionEnabled(true);
        tblMemberReceipt.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblMemberReceiptPropertyChange(evt);
            }
        });
        setTableModelListener();
    }

    private void setTableModelListener() {
        tableModelListener = new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    if (column == 7) {
                        TableModel model = tblMemberReceipt.getModel();
                        String noOfInsPay = CommonUtil.convertObjToStr(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 7));

                        if (model.getValueAt(tblMemberReceipt.getSelectedRow(), 7) != null && !model.getValueAt(tblMemberReceipt.getSelectedRow(), 7).toString().equals("") && !isNumeric(model.getValueAt(tblMemberReceipt.getSelectedRow(), 7).toString())) {
                            ClientUtil.showAlertWindow("Please enter Numeric Value!!!");
                            model.setValueAt("", tblMemberReceipt.getSelectedRow(), 7);
                            return;
                        }

                        if (model.getValueAt(tblMemberReceipt.getSelectedRow(), 7) != null && !model.getValueAt(tblMemberReceipt.getSelectedRow(), 7).toString().equals("") && isNumeric(model.getValueAt(tblMemberReceipt.getSelectedRow(), 7).toString())) {
                            Date selectedBranchDt = ClientUtil.getOtherBranchCurrentDate(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 4)).substring(0, 4)));
//                            if(DateUtil.dateDiff(curr_dt, selectedBranchDt)!=0){
//                                ClientUtil.showAlertWindow("Application Date is different in the Selected Account branch " +"\n"+"Interbranch Transaction Not allowed");     
//                                model.setValueAt("", tblMemberReceipt.getSelectedRow(), 7);
//                                return;
//                            }
                        }

                        if (CommonUtil.convertObjToDouble(noOfInsPay).doubleValue() >= 1) {
                            calcEachChittal();
                            calcTotal();
                            setSizeTableData();
                        } else if (CommonUtil.convertObjToDouble(noOfInsPay).doubleValue() == 0) {//added by chithra on 9-05-14
                            clearDataForZero(tblMemberReceipt.getSelectedRow());
                            calcTotal();
                            setSizeTableData();
                        }
                    }
                    if (column == 12) {
                        if (tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 12) != null && !tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 12).toString().equals("") && !isDouble(CommonUtil.convertObjToStr(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 12)))) {
                            ClientUtil.showAlertWindow("Please enter Numeric Value!!!");
                            tblMemberReceipt.setValueAt("0", tblMemberReceipt.getSelectedRow(), 12);
                            return;
                        }

                        if (!(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 12) != null && !tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 12).toString().equals(""))) {
                            ClientUtil.showAlertWindow("Please enter Numeric Value!!!");
                            tblMemberReceipt.setValueAt("0", tblMemberReceipt.getSelectedRow(), 12);
                            return;
                        }

                        if (CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 12)).doubleValue() >= 0) {
                            double total = CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 11))
                                    + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 12))
                                    + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 13))
                                    + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 14));
                            tblMemberReceipt.setValueAt(total, tblMemberReceipt.getSelectedRow(), 15);
                            calcTotal();
                            setSizeTableData();
                        }

                    }
                }
            }
        };
        tblMemberReceipt.getModel().addTableModelListener(tableModelListener);
    }

    public void clearDataForZero(int selrow) { //added by chithra on 9-05-14
        tblMemberReceipt.getModel().removeTableModelListener(tableModelListener);
        tblMemberReceipt.setValueAt("", selrow, 7);
        tblMemberReceipt.setValueAt("", selrow, 8);
        tblMemberReceipt.setValueAt("", selrow, 9);
        tblMemberReceipt.setValueAt("", selrow, 10);
        tblMemberReceipt.setValueAt("", selrow, 11);
        tblMemberReceipt.setValueAt("", selrow, 12);
        tblMemberReceipt.setValueAt("", selrow, 15);
        tblMemberReceipt.revalidate();
        ((DefaultTableModel) tblMemberReceipt.getModel()).fireTableDataChanged();
        tblMemberReceipt.getModel().addTableModelListener(tableModelListener);
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

    private void setObservable() {
        observable = MDSMemberReceiptEntryOB.getInstance();
        observable.addObserver(this);
    }

    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnMembershipNo.setName("btnMembershipNo");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnView.setName("btnView");
        chkChangedMember.setName("chkChangedMember");
        chkMunnalMember.setName("chkMunnalMember");
        chkThalayalMember.setName("chkThalayalMember");
        lblTotalPayment.setName("lblTotalPayment");
        lblAribitrationAmt.setName("lblAribitrationAmt");
        lblChangedDt.setName("lblChangedDt");
        lblChangedInstNo.setName("lblChangedInstNo");
        lblChangedMember.setName("lblChangedMember");
        lblEarlierMemName.setName("lblEarlierMemName");
        lblEarlierMemNo.setName("lblEarlierMemNo");
        lblMemberName.setName("lblMemberName");
        lblMemberType.setName("lblMemberType");
//        lblMembershipName.setName("lblMembershipName");
        lblMembershipNo.setName("lblMembershipNo");
        lblMembershipType.setName("lblMembershipType");
        lblMsg.setName("lblMsg");
        lblMunnalMember.setName("lblMunnalMember");
        lblNoticeAmt.setName("lblNoticeAmt");
        lblPaidAmount.setName("lblPaidAmount");
        lblPaidDt.setName("lblPaidDt");
        lblPaidInst.setName("lblPaidInst");
        lblSecurityDetails.setName("lblSecurityDetails");
        lblSpace.setName("lblSpace");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        lblStatus.setName("lblStatus");
        lblThalayalMember.setName("lblThalayalMember");
        lblTotBonusAmt.setName("lblTotBonusAmt");
        lblTotDiscountAmt.setName("lblTotDiscountAmt");
        lblTotInstAmt.setName("lblTotInstAmt");
        lblTotInterest.setName("lblTotInterest");
        mbrMain.setName("mbrMain");
        panAmountDetails.setName("panAmountDetails");
        panDiscountPeriodDetails2.setName("panDiscountPeriodDetails2");
        panDiscountPeriodDetails4.setName("panDiscountPeriodDetails4");
        panGeneralRemittance1.setName("panGeneralRemittance1");
        panMemberReceiptTrans.setName("panMemberReceiptTrans");
        panInsideGeneralRemittance4.setName("panInsideGeneralRemittance4");
        panInsideMemberReceiptDetails.setName("panInsideMemberReceiptDetails");
        panMemberDetails.setName("panMemberDetails");
        panMemberReceiptEntryDetails.setName("panMemberReceiptEntryDetails");
        panMembershipNo.setName("panMembershipNo");
        panOtherDetails.setName("panOtherDetails");
        panPaidDetails.setName("panPaidDetails");
        panStatus.setName("panStatus");
        panTableMemberReceipt.setName("panTableMemberReceipt");
        panTable2_SD2.setName("panTable2_SD2");
        panTable2_SD3.setName("panTable2_SD3");
        srpTableMemberReceipt.setName("srpTableMemberReceipt");
        srpTable2_SD2.setName("srpTable2_SD2");
        srpTable2_SD3.setName("srpTable2_SD3");
        tabRemittanceProduct.setName("tabRemittanceProduct");
        tblMemberReceipt.setName("tblMemberReceipt");
        tblSanctionDetails4.setName("tblSanctionDetails4");
        tblSanctionDetails5.setName("tblSanctionDetails5");
        tdtChangedDt.setName("tdtChangedDt");
        tdtPaidDt.setName("tdtPaidDt");
        txtTotalNetAmount.setName("txtTotalNetAmount");
        txtAribitrationAmt.setName("txtAribitrationAmt");
        txtChangedInstNo.setName("txtChangedInstNo");
        txtEarlierMemName.setName("txtEarlierMemName");
        txtEarlierMemNo.setName("txtEarlierMemNo");
        txtMembershipNo.setName("txtMembershipNo");
        txtNoticeAmt.setName("txtNoticeAmt");
        txtPaidAmount.setName("txtPaidAmount");
        txtPaidInst.setName("txtPaidInst");
        txtTotBonusAmt.setName("txtTotBonusAmt");
        txtTotDiscountAmt.setName("txtTotDiscountAmt");
        txtTotInstAmt.setName("txtTotInstAmt");
        txtTotInterest.setName("txtTotInterest");
    }

    /**
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    private void internationalize() {
    }

    /*
     * Auto Generated Method - setMandatoryHashMap()
     *
     * ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     *
     * This method list out all the Input Fields available in the UI. It needs a
     * class level HashMap variable mandatoryMap.
     */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAribitrationAmt", new Boolean(true));
        mandatoryMap.put("txtTotBonusAmt", new Boolean(true));
        mandatoryMap.put("txtTotPenalAmt", new Boolean(true));
        mandatoryMap.put("txtTotInstAmt", new Boolean(true));
        mandatoryMap.put("txtTotalNetAmount", new Boolean(true));
        mandatoryMap.put("txtNoticeAmt", new Boolean(true));
        mandatoryMap.put("txtTotInterest", new Boolean(true));
        mandatoryMap.put("txtTotDiscountAmt", new Boolean(true));
        mandatoryMap.put("txtMembershipNo", new Boolean(true));
        mandatoryMap.put("txtApplicantsName", new Boolean(true));
        mandatoryMap.put("cboTransType", new Boolean(true));
        mandatoryMap.put("txtTransactionAmt", new Boolean(true));
        mandatoryMap.put("txtTransProductId", new Boolean(true));
        mandatoryMap.put("cboProductType", new Boolean(true));
        mandatoryMap.put("txtDebitAccNo", new Boolean(true));
        mandatoryMap.put("tdtChequeDate", new Boolean(true));
        mandatoryMap.put("txtChequeNo", new Boolean(true));
        mandatoryMap.put("txtChequeNo2", new Boolean(true));
        mandatoryMap.put("cboInstrumentType", new Boolean(true));
        mandatoryMap.put("txtTokenNo", new Boolean(true));
        mandatoryMap.put("txtEarlierMemNo", new Boolean(true));
        mandatoryMap.put("txtChangedInstNo", new Boolean(true));
        mandatoryMap.put("txtEarlierMemName", new Boolean(true));
        mandatoryMap.put("chkThalayalMember", new Boolean(true));
        mandatoryMap.put("chkMunnalMember", new Boolean(true));
        mandatoryMap.put("chkChangedMember", new Boolean(true));
        mandatoryMap.put("tdtChangedDt", new Boolean(true));
        mandatoryMap.put("txtPaidAmount", new Boolean(true));
        mandatoryMap.put("txtPaidInst", new Boolean(true));
        mandatoryMap.put("tdtPaidDt", new Boolean(true));
    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private void initComponentData() {
        try {
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    private void setMaximumLength() {
        txtMembershipNo.setAllowAll(true);
        txtTotInstAmt.setValidation(new CurrencyValidation());
        txtTotBonusAmt.setValidation(new CurrencyValidation());
        txtTotDiscountAmt.setValidation(new CurrencyValidation());
        txtTotInterest.setValidation(new CurrencyValidation());
        txtNoticeAmt.setValidation(new CurrencyValidation());
        txtAribitrationAmt.setValidation(new CurrencyValidation());
        txtTotalNetAmount.setValidation(new CurrencyValidation());
    }

    /**
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable Hobserved, Object arg) {
        //        tblMemberReceipt.setModel(observable.getTblMemberRecord());
    }

    private void setSizeTableData() {
        if (tblMemberReceipt.getRowCount() > 0) {
            tblMemberReceipt.getColumnModel().getColumn(0).setPreferredWidth(55);
            tblMemberReceipt.getColumnModel().getColumn(1).setPreferredWidth(55);
            tblMemberReceipt.getColumnModel().getColumn(2).setPreferredWidth(55);
            tblMemberReceipt.getColumnModel().getColumn(3).setPreferredWidth(70);
            tblMemberReceipt.getColumnModel().getColumn(4).setPreferredWidth(120);
            tblMemberReceipt.getColumnModel().getColumn(5).setPreferredWidth(50);
            tblMemberReceipt.getColumnModel().getColumn(6).setPreferredWidth(65);
            tblMemberReceipt.getColumnModel().getColumn(7).setPreferredWidth(75);
            tblMemberReceipt.getColumnModel().getColumn(8).setPreferredWidth(60);
            tblMemberReceipt.getColumnModel().getColumn(9).setPreferredWidth(60);
            tblMemberReceipt.getColumnModel().getColumn(10).setPreferredWidth(80);
            tblMemberReceipt.getColumnModel().getColumn(11).setPreferredWidth(60);
            tblMemberReceipt.getColumnModel().getColumn(12).setPreferredWidth(50);
            tblMemberReceipt.getColumnModel().getColumn(13).setPreferredWidth(55);
            tblMemberReceipt.getColumnModel().getColumn(14).setPreferredWidth(55);
            tblMemberReceipt.getColumnModel().getColumn(15).setPreferredWidth(70);
        }
    }

    public void updateOBFields() {
//        observable.setTxtMembershipNo(txtMembershipNo.getText());
//        observable.setLblMemberName(lblMemberName.getText());
//        observable.setTxtTotalNetAmount(txtTotalNetAmount.getText());
        observable.setBufferList(bufferList);
        observable.setScreen(this.getScreen());
    }

    /*
     * Auto Generated Method - setHelpMessage() This method shows tooltip help
     * for all the input fields available in the UI. It needs the Mandatory
     * Resource Bundle object. Help display Label name should be lblMsg.
     */
    public void setHelpMessage() {
        MDSMemberReceiptEntryMRB objMandatoryRB = new MDSMemberReceiptEntryMRB();
        txtAribitrationAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAribitrationAmt"));
        txtTotBonusAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotBonusAmt"));
        txtTotInstAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotInstAmt"));
        txtNoticeAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoticeAmt"));
        txtTotInterest.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotInterest"));
        txtTotDiscountAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotDiscountAmt"));
        txtMembershipNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMembershipNo"));
        txtEarlierMemNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEarlierMemNo"));
        txtChangedInstNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChangedInstNo"));
        txtEarlierMemName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEarlierMemName"));
        tdtChangedDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtChangedDt"));
        txtPaidAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPaidAmount"));
        txtPaidInst.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPaidInst"));
        tdtPaidDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPaidDt"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgIsLapsedGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgEFTProductGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPayableBranchGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPrintServicesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgSeriesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace40 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace41 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace42 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace43 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace44 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace45 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabRemittanceProduct = new com.see.truetransact.uicomponent.CTabbedPane();
        panMemberReceiptEntryDetails = new com.see.truetransact.uicomponent.CPanel();
        panInsideMemberReceiptDetails = new com.see.truetransact.uicomponent.CPanel();
        panAmountDetails = new com.see.truetransact.uicomponent.CPanel();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblMemberGrid = new com.see.truetransact.uicomponent.CTable();
        panTableMemberReceipt = new com.see.truetransact.uicomponent.CPanel();
        srpTableMemberReceipt = new com.see.truetransact.uicomponent.CScrollPane();
        tblMemberReceipt = new com.see.truetransact.uicomponent.CTable();
        panTotalNetAmount = new com.see.truetransact.uicomponent.CPanel();
        txtTotalNetAmount = new com.see.truetransact.uicomponent.CTextField();
        lblTotalPayment = new com.see.truetransact.uicomponent.CLabel();
        panAmountDetails1 = new com.see.truetransact.uicomponent.CPanel();
        lblAribitrationAmt = new com.see.truetransact.uicomponent.CLabel();
        lblTotBonusAmt = new com.see.truetransact.uicomponent.CLabel();
        lblTotInstAmt = new com.see.truetransact.uicomponent.CLabel();
        txtAribitrationAmt = new com.see.truetransact.uicomponent.CTextField();
        txtTotBonusAmt = new com.see.truetransact.uicomponent.CTextField();
        txtTotInstAmt = new com.see.truetransact.uicomponent.CTextField();
        txtNoticeAmt = new com.see.truetransact.uicomponent.CTextField();
        txtTotInterest = new com.see.truetransact.uicomponent.CTextField();
        txtTotDiscountAmt = new com.see.truetransact.uicomponent.CTextField();
        lblTotDiscountAmt = new com.see.truetransact.uicomponent.CLabel();
        lblTotInterest = new com.see.truetransact.uicomponent.CLabel();
        lblNoticeAmt = new com.see.truetransact.uicomponent.CLabel();
        panMemberDetails = new com.see.truetransact.uicomponent.CPanel();
        lblMembershipType = new com.see.truetransact.uicomponent.CLabel();
        lblMembershipNo = new com.see.truetransact.uicomponent.CLabel();
        lblMemberType = new com.see.truetransact.uicomponent.CLabel();
        panMembershipNo = new com.see.truetransact.uicomponent.CPanel();
        txtMembershipNo = new com.see.truetransact.uicomponent.CTextField();
        btnMembershipNo = new com.see.truetransact.uicomponent.CButton();
        lblMemberName = new com.see.truetransact.uicomponent.CLabel();
        btnAddMember = new com.see.truetransact.uicomponent.CButton();
        btnDisplay = new com.see.truetransact.uicomponent.CButton();
        panMemberReceiptTrans = new com.see.truetransact.uicomponent.CPanel();
        panGeneralRemittance1 = new com.see.truetransact.uicomponent.CPanel();
        panInsideGeneralRemittance4 = new com.see.truetransact.uicomponent.CPanel();
        panDiscountPeriodDetails2 = new com.see.truetransact.uicomponent.CPanel();
        panTable2_SD2 = new com.see.truetransact.uicomponent.CPanel();
        srpTable2_SD2 = new com.see.truetransact.uicomponent.CScrollPane();
        tblSanctionDetails4 = new com.see.truetransact.uicomponent.CTable();
        panOtherDetails = new com.see.truetransact.uicomponent.CPanel();
        txtEarlierMemNo = new com.see.truetransact.uicomponent.CTextField();
        txtChangedInstNo = new com.see.truetransact.uicomponent.CTextField();
        txtEarlierMemName = new com.see.truetransact.uicomponent.CTextField();
        lblChangedInstNo = new com.see.truetransact.uicomponent.CLabel();
        lblEarlierMemName = new com.see.truetransact.uicomponent.CLabel();
        lblEarlierMemNo = new com.see.truetransact.uicomponent.CLabel();
        lblChangedDt = new com.see.truetransact.uicomponent.CLabel();
        lblChangedMember = new com.see.truetransact.uicomponent.CLabel();
        lblMunnalMember = new com.see.truetransact.uicomponent.CLabel();
        lblThalayalMember = new com.see.truetransact.uicomponent.CLabel();
        chkThalayalMember = new com.see.truetransact.uicomponent.CCheckBox();
        chkMunnalMember = new com.see.truetransact.uicomponent.CCheckBox();
        chkChangedMember = new com.see.truetransact.uicomponent.CCheckBox();
        tdtChangedDt = new com.see.truetransact.uicomponent.CDateField();
        panPaidDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSecurityDetails = new com.see.truetransact.uicomponent.CLabel();
        lblPaidInst = new com.see.truetransact.uicomponent.CLabel();
        lblPaidDt = new com.see.truetransact.uicomponent.CLabel();
        txtPaidAmount = new com.see.truetransact.uicomponent.CTextField();
        txtPaidInst = new com.see.truetransact.uicomponent.CTextField();
        tdtPaidDt = new com.see.truetransact.uicomponent.CDateField();
        lblPaidAmount = new com.see.truetransact.uicomponent.CLabel();
        panDiscountPeriodDetails4 = new com.see.truetransact.uicomponent.CPanel();
        panTable2_SD3 = new com.see.truetransact.uicomponent.CPanel();
        srpTable2_SD3 = new com.see.truetransact.uicomponent.CScrollPane();
        tblSanctionDetails5 = new com.see.truetransact.uicomponent.CTable();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMinimumSize(new java.awt.Dimension(855, 665));
        setPreferredSize(new java.awt.Dimension(855, 665));
        getContentPane().setLayout(new java.awt.GridBagLayout());

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
        tbrAdvances.add(btnView);

        lblSpace6.setText("     ");
        tbrAdvances.add(lblSpace6);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnNew);

        lblSpace40.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace40.setText("     ");
        lblSpace40.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace40);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace41.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace41.setText("     ");
        lblSpace41.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace41.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace41.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace41);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnDelete);

        lblSpace3.setText("     ");
        tbrAdvances.add(lblSpace3);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnSave);

        lblSpace42.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace42.setText("     ");
        lblSpace42.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace42.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace42.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace42);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnCancel);

        lblSpace4.setText("     ");
        tbrAdvances.add(lblSpace4);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnAuthorize);

        lblSpace43.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace43.setText("     ");
        lblSpace43.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace43.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace43.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace43);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace44.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace44.setText("     ");
        lblSpace44.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace44.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace44.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace44);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnReject);

        lblSpace5.setText("     ");
        tbrAdvances.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrAdvances.add(btnPrint);

        lblSpace45.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace45.setText("     ");
        lblSpace45.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace45.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace45.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace45);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(tbrAdvances, gridBagConstraints);

        tabRemittanceProduct.setMinimumSize(new java.awt.Dimension(850, 480));
        tabRemittanceProduct.setPreferredSize(new java.awt.Dimension(850, 480));

        panMemberReceiptEntryDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMemberReceiptEntryDetails.setMinimumSize(new java.awt.Dimension(850, 450));
        panMemberReceiptEntryDetails.setPreferredSize(new java.awt.Dimension(850, 450));
        panMemberReceiptEntryDetails.setLayout(new java.awt.GridBagLayout());

        panInsideMemberReceiptDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInsideMemberReceiptDetails.setMinimumSize(new java.awt.Dimension(850, 335));
        panInsideMemberReceiptDetails.setPreferredSize(new java.awt.Dimension(650, 335));
        panInsideMemberReceiptDetails.setLayout(new java.awt.GridBagLayout());

        panAmountDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panAmountDetails.setMinimumSize(new java.awt.Dimension(450, 86));
        panAmountDetails.setPreferredSize(new java.awt.Dimension(450, 86));
        panAmountDetails.setLayout(new java.awt.GridBagLayout());

        cScrollPane1.setMinimumSize(new java.awt.Dimension(600, 90));
        cScrollPane1.setPreferredSize(new java.awt.Dimension(600, 90));

        tblMemberGrid.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Member No", "Memeber Name", "Member Type"
            }
        ));
        tblMemberGrid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMemberGridMouseClicked(evt);
            }
        });
        cScrollPane1.setViewportView(tblMemberGrid);

        panAmountDetails.add(cScrollPane1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideMemberReceiptDetails.add(panAmountDetails, gridBagConstraints);

        panTableMemberReceipt.setMinimumSize(new java.awt.Dimension(840, 465));
        panTableMemberReceipt.setPreferredSize(new java.awt.Dimension(840, 465));
        panTableMemberReceipt.setLayout(new java.awt.GridBagLayout());

        srpTableMemberReceipt.setMinimumSize(new java.awt.Dimension(840, 150));
        srpTableMemberReceipt.setPreferredSize(new java.awt.Dimension(840, 150));

        tblMemberReceipt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MemberNo", "MemberName", "Scheme", "ChittalNo", "Sub No", "InsDue", "NoOfInsPay", "TotPayable", "Bonus", "Discount", "InsAmtPayable", "Interest", "Notice", "Arbitration", "Net Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblMemberReceipt.setMinimumSize(new java.awt.Dimension(840, 1500));
        tblMemberReceipt.setOpaque(false);
        tblMemberReceipt.setPreferredScrollableViewportSize(new java.awt.Dimension(836, 191));
        tblMemberReceipt.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                tblMemberReceiptMouseWheelMoved(evt);
            }
        });
        tblMemberReceipt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMemberReceiptMouseClicked(evt);
            }
        });
        tblMemberReceipt.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblMemberReceiptPropertyChange(evt);
            }
        });
        srpTableMemberReceipt.setViewportView(tblMemberReceipt);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panTableMemberReceipt.add(srpTableMemberReceipt, gridBagConstraints);

        panTotalNetAmount.setMinimumSize(new java.awt.Dimension(840, 21));
        panTotalNetAmount.setPreferredSize(new java.awt.Dimension(840, 21));
        panTotalNetAmount.setLayout(new java.awt.GridBagLayout());

        txtTotalNetAmount.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalNetAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        panTotalNetAmount.add(txtTotalNetAmount, gridBagConstraints);

        lblTotalPayment.setText("Total NetAmount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 607, 5, 0);
        panTotalNetAmount.add(lblTotalPayment, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTableMemberReceipt.add(panTotalNetAmount, gridBagConstraints);

        panAmountDetails1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panAmountDetails1.setMinimumSize(new java.awt.Dimension(840, 50));
        panAmountDetails1.setPreferredSize(new java.awt.Dimension(840, 50));
        panAmountDetails1.setLayout(new java.awt.GridBagLayout());

        lblAribitrationAmt.setText("Total Arbitration Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAmountDetails1.add(lblAribitrationAmt, gridBagConstraints);

        lblTotBonusAmt.setText("Total Bonus Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAmountDetails1.add(lblTotBonusAmt, gridBagConstraints);

        lblTotInstAmt.setText("Total Installment Amt payable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAmountDetails1.add(lblTotInstAmt, gridBagConstraints);

        txtAribitrationAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAmountDetails1.add(txtAribitrationAmt, gridBagConstraints);

        txtTotBonusAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAmountDetails1.add(txtTotBonusAmt, gridBagConstraints);

        txtTotInstAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotInstAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotInstAmtActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAmountDetails1.add(txtTotInstAmt, gridBagConstraints);

        txtNoticeAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAmountDetails1.add(txtNoticeAmt, gridBagConstraints);

        txtTotInterest.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAmountDetails1.add(txtTotInterest, gridBagConstraints);

        txtTotDiscountAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAmountDetails1.add(txtTotDiscountAmt, gridBagConstraints);

        lblTotDiscountAmt.setText("Total Discount Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAmountDetails1.add(lblTotDiscountAmt, gridBagConstraints);

        lblTotInterest.setText("Total MDS Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAmountDetails1.add(lblTotInterest, gridBagConstraints);

        lblNoticeAmt.setText("Total Notice Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAmountDetails1.add(lblNoticeAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panTableMemberReceipt.add(panAmountDetails1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideMemberReceiptDetails.add(panTableMemberReceipt, gridBagConstraints);

        panMemberDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMemberDetails.setMinimumSize(new java.awt.Dimension(380, 86));
        panMemberDetails.setPreferredSize(new java.awt.Dimension(380, 86));
        panMemberDetails.setLayout(new java.awt.GridBagLayout());

        lblMembershipType.setText("Member Type  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(lblMembershipType, gridBagConstraints);

        lblMembershipNo.setText(" Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(lblMembershipNo, gridBagConstraints);

        lblMemberType.setForeground(new java.awt.Color(0, 51, 204));
        lblMemberType.setMinimumSize(new java.awt.Dimension(120, 18));
        lblMemberType.setPreferredSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(lblMemberType, gridBagConstraints);

        panMembershipNo.setLayout(new java.awt.GridBagLayout());

        txtMembershipNo.setAllowAll(true);
        txtMembershipNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMembershipNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMembershipNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 1);
        panMembershipNo.add(txtMembershipNo, gridBagConstraints);

        btnMembershipNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMembershipNo.setEnabled(false);
        btnMembershipNo.setMaximumSize(new java.awt.Dimension(20, 21));
        btnMembershipNo.setMinimumSize(new java.awt.Dimension(20, 21));
        btnMembershipNo.setPreferredSize(new java.awt.Dimension(20, 21));
        btnMembershipNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMembershipNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMembershipNo.add(btnMembershipNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panMemberDetails.add(panMembershipNo, gridBagConstraints);

        lblMemberName.setForeground(new java.awt.Color(0, 51, 204));
        lblMemberName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblMemberName.setMaximumSize(new java.awt.Dimension(150, 18));
        lblMemberName.setMinimumSize(new java.awt.Dimension(150, 18));
        lblMemberName.setPreferredSize(new java.awt.Dimension(150, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 25, 2, 2);
        panMemberDetails.add(lblMemberName, gridBagConstraints);

        btnAddMember.setText("Add");
        btnAddMember.setMaximumSize(new java.awt.Dimension(75, 27));
        btnAddMember.setMinimumSize(new java.awt.Dimension(75, 27));
        btnAddMember.setPreferredSize(new java.awt.Dimension(75, 27));
        btnAddMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddMemberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panMemberDetails.add(btnAddMember, gridBagConstraints);

        btnDisplay.setText("Display");
        btnDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisplayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panMemberDetails.add(btnDisplay, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideMemberReceiptDetails.add(panMemberDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panMemberReceiptEntryDetails.add(panInsideMemberReceiptDetails, gridBagConstraints);

        panMemberReceiptTrans.setMinimumSize(new java.awt.Dimension(850, 225));
        panMemberReceiptTrans.setPreferredSize(new java.awt.Dimension(850, 225));
        panMemberReceiptTrans.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panMemberReceiptEntryDetails.add(panMemberReceiptTrans, gridBagConstraints);

        tabRemittanceProduct.addTab("Member Receipt Entry Details", panMemberReceiptEntryDetails);

        panGeneralRemittance1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panGeneralRemittance1.setMinimumSize(new java.awt.Dimension(570, 450));
        panGeneralRemittance1.setPreferredSize(new java.awt.Dimension(570, 450));
        panGeneralRemittance1.setLayout(new java.awt.GridBagLayout());

        panInsideGeneralRemittance4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInsideGeneralRemittance4.setMinimumSize(new java.awt.Dimension(850, 575));
        panInsideGeneralRemittance4.setPreferredSize(new java.awt.Dimension(850, 575));
        panInsideGeneralRemittance4.setLayout(new java.awt.GridBagLayout());

        panDiscountPeriodDetails2.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaction Details"));
        panDiscountPeriodDetails2.setMinimumSize(new java.awt.Dimension(350, 120));
        panDiscountPeriodDetails2.setPreferredSize(new java.awt.Dimension(400, 120));
        panDiscountPeriodDetails2.setLayout(new java.awt.GridBagLayout());

        panTable2_SD2.setMinimumSize(new java.awt.Dimension(270, 340));
        panTable2_SD2.setPreferredSize(new java.awt.Dimension(270, 340));
        panTable2_SD2.setLayout(new java.awt.GridBagLayout());

        srpTable2_SD2.setMinimumSize(new java.awt.Dimension(260, 340));
        srpTable2_SD2.setPreferredSize(new java.awt.Dimension(260, 340));

        tblSanctionDetails4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MDS Scheme name", "Installment no", "Transaction id", "Transaction Date", "Amount paid"
            }
        ));
        tblSanctionDetails4.setPreferredSize(new java.awt.Dimension(75, 0));
        srpTable2_SD2.setViewportView(tblSanctionDetails4);

        panTable2_SD2.add(srpTable2_SD2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDiscountPeriodDetails2.add(panTable2_SD2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideGeneralRemittance4.add(panDiscountPeriodDetails2, gridBagConstraints);

        panOtherDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panOtherDetails.setMinimumSize(new java.awt.Dimension(400, 200));
        panOtherDetails.setPreferredSize(new java.awt.Dimension(400, 200));
        panOtherDetails.setLayout(new java.awt.GridBagLayout());

        txtEarlierMemNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOtherDetails.add(txtEarlierMemNo, gridBagConstraints);

        txtChangedInstNo.setMinimumSize(new java.awt.Dimension(50, 21));
        txtChangedInstNo.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOtherDetails.add(txtChangedInstNo, gridBagConstraints);

        txtEarlierMemName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOtherDetails.add(txtEarlierMemName, gridBagConstraints);

        lblChangedInstNo.setText("Changed from Installment no ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOtherDetails.add(lblChangedInstNo, gridBagConstraints);

        lblEarlierMemName.setText("Earlier Member name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOtherDetails.add(lblEarlierMemName, gridBagConstraints);

        lblEarlierMemNo.setText("Earlier Member no");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOtherDetails.add(lblEarlierMemNo, gridBagConstraints);

        lblChangedDt.setText("Changed Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOtherDetails.add(lblChangedDt, gridBagConstraints);

        lblChangedMember.setText("Whether the member changed in between");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOtherDetails.add(lblChangedMember, gridBagConstraints);

        lblMunnalMember.setText("Whether the member is a Munnal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOtherDetails.add(lblMunnalMember, gridBagConstraints);

        lblThalayalMember.setText("Whether the member is a Thalayal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOtherDetails.add(lblThalayalMember, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherDetails.add(chkThalayalMember, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherDetails.add(chkMunnalMember, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherDetails.add(chkChangedMember, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOtherDetails.add(tdtChangedDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideGeneralRemittance4.add(panOtherDetails, gridBagConstraints);

        panPaidDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Prized  Details"));
        panPaidDetails.setMinimumSize(new java.awt.Dimension(400, 200));
        panPaidDetails.setPreferredSize(new java.awt.Dimension(400, 200));
        panPaidDetails.setLayout(new java.awt.GridBagLayout());

        lblSecurityDetails.setText("Security Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPaidDetails.add(lblSecurityDetails, gridBagConstraints);

        lblPaidInst.setText("Paid Installment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPaidDetails.add(lblPaidInst, gridBagConstraints);

        lblPaidDt.setText("Paid Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPaidDetails.add(lblPaidDt, gridBagConstraints);

        txtPaidAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPaidDetails.add(txtPaidAmount, gridBagConstraints);

        txtPaidInst.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPaidDetails.add(txtPaidInst, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPaidDetails.add(tdtPaidDt, gridBagConstraints);

        lblPaidAmount.setText("Paid Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPaidDetails.add(lblPaidAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideGeneralRemittance4.add(panPaidDetails, gridBagConstraints);

        panDiscountPeriodDetails4.setBorder(javax.swing.BorderFactory.createTitledBorder("MDS Loan details"));
        panDiscountPeriodDetails4.setMinimumSize(new java.awt.Dimension(350, 120));
        panDiscountPeriodDetails4.setPreferredSize(new java.awt.Dimension(400, 120));
        panDiscountPeriodDetails4.setLayout(new java.awt.GridBagLayout());

        panTable2_SD3.setMinimumSize(new java.awt.Dimension(270, 340));
        panTable2_SD3.setPreferredSize(new java.awt.Dimension(270, 340));
        panTable2_SD3.setLayout(new java.awt.GridBagLayout());

        srpTable2_SD3.setMinimumSize(new java.awt.Dimension(260, 340));
        srpTable2_SD3.setPreferredSize(new java.awt.Dimension(260, 340));

        tblSanctionDetails5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MDS Loan no", "Loan  date", "Loan amount", "outstanding amount", "Overdue amount"
            }
        ));
        tblSanctionDetails5.setPreferredSize(new java.awt.Dimension(75, 0));
        srpTable2_SD3.setViewportView(tblSanctionDetails5);

        panTable2_SD3.add(srpTable2_SD3, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDiscountPeriodDetails4.add(panTable2_SD3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideGeneralRemittance4.add(panDiscountPeriodDetails4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGeneralRemittance1.add(panInsideGeneralRemittance4, gridBagConstraints);

        tabRemittanceProduct.addTab("Other Details", panGeneralRemittance1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabRemittanceProduct, gridBagConstraints);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(panStatus, gridBagConstraints);

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
        mnuProcess.add(sptDelete);

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

        mitAuthorize.setText("Authorize");
        mnuProcess.add(mitAuthorize);

        mitReject.setText("Rejection");
        mnuProcess.add(mitReject);

        mitException.setText("Exception");
        mnuProcess.add(mitException);
        mnuProcess.add(sptException);

        mitPrint.setText("Print");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);
    }// </editor-fold>//GEN-END:initComponents

    private void tblMemberReceiptPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblMemberReceiptPropertyChange
        // TODO add your handling code here:
        if (tblMemberReceipt.getSelectedRowCount() > 0)  {        
        int column = tblMemberReceipt.getEditingColumn();
        if (column == 7) {
            String noOfInsPay = CommonUtil.convertObjToStr(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 7));

            if (tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 7) != null && !tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 7).toString().equals("") && !isNumeric(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 7).toString())) {
                ClientUtil.showAlertWindow("Please enter Numeric Value!!!");
                tblMemberReceipt.setValueAt("", tblMemberReceipt.getSelectedRow(), 7);
                return;
            }
            if (CommonUtil.convertObjToDouble(noOfInsPay).doubleValue() >= 1) {
                calcEachChittal();
                calcTotal();
            } else if (CommonUtil.convertObjToDouble(noOfInsPay).doubleValue() == 0) {               
                String selectedChittal = CommonUtil.convertObjToStr(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 4));                
                if(finalMap.containsKey(selectedChittal) && finalMap.get(selectedChittal) != null){                    
                    finalMap.remove(selectedChittal);
                }                
                clearDataForZero(tblMemberReceipt.getSelectedRow());
                calcTotal();               
            }
        }
        if (column == 12) {
            if (tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 12) != null && !tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 12).toString().equals("") && !isDouble(CommonUtil.convertObjToStr(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 12)))) {
                ClientUtil.showAlertWindow("Please enter Numeric Value!!!");
                tblMemberReceipt.setValueAt("0", tblMemberReceipt.getSelectedRow(), 12);
                return;
            }

            if (!(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 12) != null && !tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 12).toString().equals(""))) {
                ClientUtil.showAlertWindow("Please enter Numeric Value!!!");
                tblMemberReceipt.setValueAt("0", tblMemberReceipt.getSelectedRow(), 12);
                return;
            }
            if (CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 12)).doubleValue() >= 0) {
                double total = CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 11))
                        + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 12))
                        + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 13))
                        + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 14));
                tblMemberReceipt.setValueAt(total, tblMemberReceipt.getSelectedRow(), 15);
                calcTotal();
                
            }
          }
       }
        setSizeTableData();
    }//GEN-LAST:event_tblMemberReceiptPropertyChange

    private void calcEachChittal() {
        try {
            String mem_no = CommonUtil.convertObjToStr(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 0));
            String mem_name = CommonUtil.convertObjToStr(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 1));
            String scheme_Name = CommonUtil.convertObjToStr(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 2));
            String chittalNo = CommonUtil.convertObjToStr(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 4));
            String subNo = CommonUtil.convertObjToStr(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 5));
            String noOfInsPay = CommonUtil.convertObjToStr(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 7));
            double noticAmt = setNoticeChargeAmount(chittalNo, subNo);
            double arbitAmt = setCaseExpensesAmount(chittalNo, subNo);
            String insDue = CommonUtil.convertObjToStr(tblMemberReceipt.getValueAt(tblMemberReceipt.getSelectedRow(), 6));
            int selectedRow = tblMemberReceipt.getSelectedRow();
            HashMap pendingMap = new HashMap();
            pendingMap.put("SCHEME_NAME", scheme_Name);
            pendingMap.put("CHITTAL_NO", chittalNo);
            pendingMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
            List pendingAuthlst = ClientUtil.executeQuery("checkPendingForAuthorization", pendingMap);
            if (pendingAuthlst == null && pendingAuthlst.size() > 0 && CommonUtil.convertObjToDouble(noOfInsPay).doubleValue() < 1) {
                tblMemberReceipt.setValueAt("", tblMemberReceipt.getSelectedRow(), 7);
                tblMemberReceipt.setValueAt("", tblMemberReceipt.getSelectedRow(), 8);
                tblMemberReceipt.setValueAt("", tblMemberReceipt.getSelectedRow(), 9);
                tblMemberReceipt.setValueAt("", tblMemberReceipt.getSelectedRow(), 10);
                tblMemberReceipt.setValueAt("", tblMemberReceipt.getSelectedRow(), 11);
                tblMemberReceipt.setValueAt("", tblMemberReceipt.getSelectedRow(), 12);
                tblMemberReceipt.setValueAt("", tblMemberReceipt.getSelectedRow(), 15);
            }
            if (pendingAuthlst != null && pendingAuthlst.size() > 0) {
                ClientUtil.showMessageWindow(" Transaction pending for this Chittal... Please Authorize OR Reject first  !!! ");
                tblMemberReceipt.setValueAt("", selectedRow, 7);
            } else if (CommonUtil.convertObjToDouble(noOfInsPay).doubleValue() >= 1) {
                HashMap whereMap = new HashMap();
                HashMap dataMap = new HashMap();
                HashMap productMap = new HashMap();
                bonusAmountList = new ArrayList();
                penalRealList = new ArrayList();
                penalList = new ArrayList();
                instList = new ArrayList();
                narrationList = new ArrayList();
                //finalSplitMap = new HashMap();
                String calculateIntOn = "";
                Rounding rod = new Rounding();
                long diffDayPending = 0;
                int noOfInsPaid = 0;
                Date currDate = (Date) curr_dt.clone();
                Date instDate = null;
                boolean bonusAvailabe = true;
                long noOfInstPay = CommonUtil.convertObjToLong(noOfInsPay);
                int instDay = 1;
                int totIns = 0;
                Date startDate = null;
                Date endDate = null;
                Date insDate = null;
                int startMonth = 0;
                int insMonth = 0;
                int curInsNo = 0;
                HashMap installmentMap = new HashMap();
                whereMap.put("SCHEME_NAME", scheme_Name);
                List lst = ClientUtil.executeQuery("getSelectSchemeAcctHead", whereMap);
                if (lst != null && lst.size() > 0) {
                    productMap = (HashMap) lst.get(0);
                    isSplitMDSTransaction = CommonUtil.convertObjToStr(productMap.get("IS_SPLIT_MDS_TRANSACTION"));
                    totIns = CommonUtil.convertObjToInt(productMap.get("NO_OF_INSTALLMENTS"));
                    startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
                    endDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_END_DT")));
                    insDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
                    startMonth = insDate.getMonth();
                    if (productMap.containsKey("INSTALLMENT_FREQUENCY") && productMap.get("INSTALLMENT_FREQUENCY") != null) {
                        if (CommonUtil.convertObjToInt(productMap.get("INSTALLMENT_FREQUENCY")) == 7) {
                            instFrequency = CommonUtil.convertObjToInt(productMap.get("INSTALLMENT_FREQUENCY"));
                            isWeeklyOrMonthlyScheme = "W";
                        } else {
                            instFrequency = CommonUtil.convertObjToInt(productMap.get("INSTALLMENT_FREQUENCY"));
                            isWeeklyOrMonthlyScheme = "M";
                        }
                    }
                    // Added by Rajesh For checking BONUS_FIRST_INSTALLMENT. Based on this for loop initial value will be changed for Penal calculation.
//                    String bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("BONUS_FIRST_INSTALLMENT"));
                    String bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("ADVANCE_COLLECTION"));
                    int startNoForPenal = 0;
                    int addNo = 1;
                    int firstInst_No = -1;
                    if (bonusFirstInst.equals("Y") || CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                        startNoForPenal = 1;
                        addNo = 0;
                        firstInst_No = 0;
                    }
                    bonusAvailabe = true;
                    double insAmt = 0.0;
                    //                    double insAmt = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT")).doubleValue();
                    long pendingInst = 0;
                    int divNo = 0;
                    int count = 0;
                    long insDueAmt = 0;
                    whereMap.put("CHITTAL_NO", chittalNo);
                    whereMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                    List insList = ClientUtil.executeQuery("getNoOfInstalmentsPaid", whereMap);
                    if (insList != null && insList.size() > 0) {
                        whereMap = (HashMap) insList.get(0);
                        noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
                        count = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
                    }
                    HashMap chittalMap = new HashMap();
                    chittalMap.put("CHITTAL_NO", chittalNo);
                    chittalMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                    List chitLst = ClientUtil.executeQuery("getSelctApplnReceiptDetails", chittalMap);
                    if (chitLst != null && chitLst.size() > 0) {
                        chittalMap = (HashMap) chitLst.get(0);
                        instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                        divNo = CommonUtil.convertObjToInt(chittalMap.get("DIVISION_NO"));
                        dataMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(chittalMap.get("BRANCH_CODE")));
                        insAmt = CommonUtil.convertObjToDouble(chittalMap.get("INST_AMT")).doubleValue();
                    }
                    HashMap insDateMap = new HashMap();
                    insDateMap.put("DIVISION_NO", divNo);
                    insDateMap.put("SCHEME_NAME", scheme_Name);
                    insDateMap.put("CURR_DATE", curr_dt.clone());
//                    insDateMap.put("ADD_MONTHS", "0");
                    insDateMap.put("ADD_MONTHS", CommonUtil.convertObjToInt("-1"));
                    List insDateLst = null;
                    if (isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("") && isWeeklyOrMonthlyScheme.equals("W")) {
                        insDateLst = ClientUtil.executeQuery("getWeeklyMDSCurrentInsDate", insDateMap);
                    } else {
                        insDateLst = ClientUtil.executeQuery("getMDSCurrentInsDate", insDateMap);
                    }
                    if (insDateLst != null && insDateLst.size() > 0) {
                        insDateMap = (HashMap) insDateLst.get(0);
                        curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
                        pendingInst = curInsNo - noOfInsPaid;
//                        if (instDay < currDate.getDate()) {
//                            pendingInst = pendingInst + 1;
//                        } else {
//                            pendingInst = pendingInst;
//                        }
                        if (bonusFirstInst.equals("Y")) {
                            pendingInst = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO")) - (int) count - 1;
                        } else {
                            pendingInst = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO")) - (int) count;
                        }
                        if (pendingInst < 0) {
                            pendingInst = 0;
                        }
                        insMonth = startMonth + curInsNo;
                        insDate.setMonth(insMonth);
                    }

                    
                    //Checking MDS prized member or not
                    HashMap prizedMap = new HashMap();
                    prizedMap.put("SCHEME_NAME", scheme_Name);
                    prizedMap.put("DIVISION_NO", String.valueOf(divNo));
                    prizedMap.put("CHITTAL_NO", chittalNo);
                    prizedMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                    if (productMap.containsKey("FROM_AUCTION_ENTRY") && productMap.get("FROM_AUCTION_ENTRY") != null && productMap.get("FROM_AUCTION_ENTRY").equals("Y")) {
                        lst = ClientUtil.executeQuery("getSelectPrizedDetailsEntryRecords", prizedMap);
                        System.out.println("lst in FROM_AUCTION_ENTRY" + lst);
                        if (lst != null && lst.size() > 0) {
                            setRdoPrizedMember_Yes(true);
                            setRdoPrizedMember_No(false);
                        } else {
                            setRdoPrizedMember_No(true);
                            setRdoPrizedMember_Yes(false);
                        }
                    } else if (productMap.containsKey("AFTER_CASH_PAYMENT") && productMap.get("AFTER_CASH_PAYMENT") != null && productMap.get("AFTER_CASH_PAYMENT").equals("Y")) {
                        lst = ClientUtil.executeQuery("getSelectPrizedDetailsAfterCashPayment", prizedMap);
                        System.out.println("lst in AFTER_CASH_PAYMENT" + lst);
                        if (lst != null && lst.size() > 0) {
                           prizedMap = (HashMap) lst.get(0);
                            System.out.println("SIIIIII" + prizedMap.size());
                            if (prizedMap.size() >= 1) {
                                setRdoPrizedMember_Yes(true);
                                setRdoPrizedMember_No(false);
                            } else {
                                setRdoPrizedMember_No(true);
                                setRdoPrizedMember_Yes(false);
                            }
                        } else {
                            setRdoPrizedMember_No(true);
                            setRdoPrizedMember_Yes(false);
                        }
                    }else {
                    lst = ClientUtil.executeQuery("getSelectPrizedDetailsEntryRecords", prizedMap);
                    if (lst != null && lst.size() > 0) {
                        prizedMap = (HashMap) lst.get(0);
                        if (prizedMap.get("DRAW") != null && !prizedMap.get("DRAW").equals("") && prizedMap.get("DRAW").equals("Y")) {
                            setRdoPrizedMember_Yes(true);
                            setRdoPrizedMember_No(false);
                        }
                        if (prizedMap.get("AUCTION") != null && !prizedMap.get("AUCTION").equals("") && prizedMap.get("AUCTION").equals("Y")) {
                            setRdoPrizedMember_Yes(true);
                                setRdoPrizedMember_No(false);
                        }
                    } else {
                        setRdoPrizedMember_No(true);
                            setRdoPrizedMember_Yes(false);
                        }
                    }
                    int balanceIns = totIns - noOfInsPaid;
                    if (balanceIns >= noOfInstPay) {
                        long totDiscAmt = 0;
                        long penalAmt = 0;
                        double netAmt = 0;
                        double insAmtPayable = 0;
                        double totBonusAmt = 0;
                        double bonusAmt = 0;
                        String penalIntType = "";
                        double penalValue = 0;
                        String penalGraceType = "";
                        double penalGraceValue = 0;
                        String penalCalcBaseOn = "";
                        if (pendingInst >= 0) {              //pending installment calculation starts...
                            insDueAmt = (long) insAmt * pendingInst;
                            int totPendingInst = (int) pendingInst;
                            double calc = 0;
                            long totInst = pendingInst;
                            penalCalcBaseOn = CommonUtil.convertObjToStr(productMap.get("PENAL_CALC"));
                            if (getRdoPrizedMember_Yes() == true) {
                                if (productMap.containsKey("PENEL_PZ_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT") != null) {
                                    calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT"));
                                }
                                penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_INT_TYPE"));
                                penalValue = CommonUtil.convertObjToDouble(productMap.get("PENAL_PRIZED_INT_AMT"));
                                penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_GRACE_PERIOD_TYPE"));
                                penalGraceValue = CommonUtil.convertObjToDouble(productMap.get("PENAL_PRIZED_GRACE_PERIOD"));
                                if (penalGraceType.equals("Installments") && (penalIntType != null && penalIntType.equals("Percent"))) {
                                    pendingInst -= penalGraceValue;
                                }
                            } else if (getRdoPrizedMember_No() == true) {
                                penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_INT_TYPE"));
                                penalValue = CommonUtil.convertObjToDouble(productMap.get("PENAL_INT_AMT"));
                                penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_GRACE_PERIOD_TYPE"));
                                penalGraceValue = CommonUtil.convertObjToDouble(productMap.get("PENAL_GRACE_PERIOD"));
                                if (penalGraceType.equals("Installments") && (penalIntType != null && penalIntType.equals("Percent"))) {
                                    pendingInst -= penalGraceValue;
                                }
                            }
                            List bonusAmout = new ArrayList();//Added By Nidhin Penal calculation for Installment Amount
                            if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                                //double instAmount = 0.0;
                                HashMap nextInstMaps = null;
                                for (int i = startNoForPenal; i <= noOfInstPay - addNo; i++) {
                                    nextInstMaps = new HashMap();
                                    nextInstMaps.put("SCHEME_NAME", scheme_Name);
                                    nextInstMaps.put("DIVISION_NO", divNo);
                                    nextInstMaps.put("SL_NO", new Double(i + noOfInsPaid + addNo + firstInst_No));
                                    List listRec = ClientUtil.executeQuery("getSelectBonusAndNextInstDateWithoutDivision", nextInstMaps);
                                    if (listRec != null && listRec.size() > 0) {
                                        nextInstMaps = (HashMap) listRec.get(0);
                                    }
                                    if (nextInstMaps != null && nextInstMaps.containsKey("NEXT_BONUS_AMOUNT")) {
                                        bonusAmout.add(CommonUtil.convertObjToDouble(nextInstMaps.get("NEXT_BONUS_AMOUNT")));
                                    }
                                }
                            }
                            double instAmount = 0.0;
                            //added by rishad 10/01/2018 for predefined installment
                            long prependingInst = pendingInst;
                            if (CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                                HashMap nextActMap = new HashMap();
                                nextActMap.put("SCHEME_NAME", scheme_Name);
                                nextActMap.put("SL_NO",CommonUtil.convertObjToDouble(curInsNo));
                                List listAuc = ClientUtil.executeQuery("getSelectNextAuctDate", nextActMap);
                                if (listAuc != null && listAuc.size() > 0) {
                                    nextActMap = (HashMap) listAuc.get(0);
                                }
                                Date drawAuctDate = null;
                                if (nextActMap.containsKey("DRAW_AUCTION_DATE")) {
                                    drawAuctDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextActMap.get("DRAW_AUCTION_DATE")));
                                }
//                                if (DateUtil.dateDiff(drawAuctDate, currDate) <= 0) {
//                                    prependingInst = prependingInst - 1;
//                                }

                                int addingGraceDays = CommonUtil.convertObjToInt(penalGraceValue);
                                System.out.println("addingGraceDays :: " + addingGraceDays);
                                if (DateUtil.dateDiff(DateUtil.addDays(drawAuctDate, addingGraceDays), currDate) <= 0) {
                                    prependingInst = prependingInst - 1;
                                }
                              
                            }
                            for (int j = startNoForPenal; j < noOfInstPay + startNoForPenal; j++) {
                                HashMap nextInstMap = new HashMap();
                                if (calculateIntOn.equals("Installment Amount")) {
                                    instAmount = 0.0;
                                    instAmount = CommonUtil.convertObjToDouble(insAmt);
                                    if (bonusAmout != null && bonusAmout.size() > 0) {
                                        instAmount -= CommonUtil.convertObjToDouble(bonusAmout.get(j));
                                    }
                                }
                                nextInstMap.put("SCHEME_NAME", scheme_Name);
                                nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                                nextInstMap.put("SL_NO", new Double(j + noOfInsPaid));
                                List listRec = ClientUtil.executeQuery("getSelectNextInstDate", nextInstMap);
                                if (listRec != null && listRec.size() > 0) {
                                    double penal = 0;
                                    nextInstMap = (HashMap) listRec.get(0);
                                    //Changed by sreekrishnan
                                    if (CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                                        instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("DRAW_AUCTION_DATE")));
                                    } else {
                                        instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                                    }
                                    diffDayPending = DateUtil.dateDiff(instDate, currDate);
                                    //Holiday Checking - Added By Suresh
                                    HashMap holidayMap = new HashMap();
                                    boolean checkHoliday = true;
                                    instDate = setProperDtFormat(instDate);
                                    holidayMap.put("NEXT_DATE", instDate);
                                    holidayMap.put("BRANCH_CODE", getSelectedBranchID());
                                    while (checkHoliday) {
                                        boolean tholiday = false;
                                        List Holiday = ClientUtil.executeQuery("checkHolidayDateOD", holidayMap);
                                        List weeklyOf = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
                                        boolean isHoliday = Holiday.size() > 0 ? true : false;
                                        boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                                        if (isHoliday || isWeekOff) {
                                            if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                                diffDayPending -= 1;
                                                instDate.setDate(instDate.getDate() + 1);
                                            } else {
                                                diffDayPending += 1;
                                                instDate.setDate(instDate.getDate() - 1);
                                            }
                                            holidayMap.put("NEXT_DATE", instDate);
                                            checkHoliday = true;
                                        } else {
                                            checkHoliday = false;
                                        }
                                    }
                                    if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Days")) {
                                        if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                            if (diffDayPending > penalGraceValue) {
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    //calc += (diffDayPending * penalValue * insAmt) / 36500;
                                                    if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                                        calc += (diffDayPending * penalValue * instAmount) / 36500;
                                                    } else {
                                                        calc += (diffDayPending * penalValue * insAmt) / 36500;
                                                    }
                                                    penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                                    calc += penalValue;
                                                    penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                                }
                                            }
                                        } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                            // To be written
                                            if (diffDayPending > penalGraceValue * instFrequency) {
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                                    if (isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("") && isWeeklyOrMonthlyScheme.equals("W")) {
                                                        if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                                            calc += (double) ((instAmount * penalValue) * 7 / 36500.0) * pendingInst--;
                                                        } else {
                                                            calc += (double) ((insAmt * penalValue) * 7 / 36500.0) * pendingInst--;
                                                        }
                                                        penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                                    } else {
                                                        if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                                            calc += (double) ((instAmount * penalValue) / 1200.0) * pendingInst--;
                                                        } else {
                                                            calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                                        }
                                                        penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                                    }
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                                    calc += penalValue;
                                                    penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                                }
                                            }
                                        }
                                    } else if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Installments")) {
                                        if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                            if (diffDayPending > penalGraceValue) {
                                                if (pendingInst == 0) {
                                                    pendingInst = 1;
                                                }
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    if (isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("") && isWeeklyOrMonthlyScheme.equals("W")) {
                                                        if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                                            calc += (double) ((instAmount * penalValue) * 7 / 36500.0) * pendingInst--;
                                                        } else {
                                                            calc += (double) ((insAmt * penalValue) * 7 / 36500.0) * pendingInst--;
                                                        }
                                                        penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                                    } else {
                                                        if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {

                                                            if (CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                                                                calc += ((instAmount * penalValue) / 1200.0) * prependingInst--;
                                                            } else {
                                                                calc += ((instAmount * penalValue) / 1200.0) * pendingInst--;
                                                            }
                                                        } else {
                                                            if (CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                                                                calc += ((insAmt * penalValue) / 1200.0) * prependingInst--;
                                                            } else {
                                                                calc += ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                                            }
                                                        }
                                                        penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                                    }
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute") && totInst <= noOfInstPay) {
                                                    calc += penalValue;
                                                    penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                                }
                                            }
                                        } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                            // To be written
                                            if (diffDayPending > penalGraceValue * instFrequency) {
                                                System.out.println("diffDayPending" + diffDayPending);
                                                System.out.println("penalGraceValue===" + penalGraceValue + "calc====" + calc + "penalIntType==" + penalIntType +"calculateIntOn :: "+ calculateIntOn);
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                                    if (isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("") && isWeeklyOrMonthlyScheme.equals("W")) {
                                                        if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                                            calc = (double) ((pendingInst * (pendingInst + 1) / 2) * instAmount * penalValue) * 7 / 36500;
                                                        } else {
                                                            calc = (double) ((pendingInst * (pendingInst + 1) / 2) * insAmt * penalValue) * 7 / 36500;
                                                        }
                                                        penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                                    } else {
                                                        if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                                            calc += (double) ((instAmount * penalValue) / 1200.0) * pendingInst--;
                                                        } else {
                                                            calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                                        }
                                                        penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                                    }
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                                    calc += penalValue;
                                                    penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            //After Scheme End Date Penal Calculating
                            HashMap c_hash = new HashMap();
                            c_hash.put("SCHEME_NAME", scheme_Name);
                            List closureList = ClientUtil.executeQuery("checkSchemeClosureDetailsForClosed", c_hash);
                            if (closureList != null && closureList.size() > 0) {
                                //Below portion of code added by Jeffin John on 16-05-2014 for Mantis ID- 8858                                       
                                c_hash.put("SCHEME_NAME", scheme_Name);
                                List closedPenal = ClientUtil.executeQuery("getClosedRate", c_hash);
                                double clodespenalRt = 0.0;
                                if (closedPenal != null && closedPenal.size() > 0) {
                                    HashMap penalRate = new HashMap();
                                    penalRate = (HashMap) closedPenal.get(0);
                                    if (penalRate.containsKey("CLOSED_PENAL") && penalRate.get("CLOSED_PENAL") != null) {
                                        clodespenalRt = Double.parseDouble(((HashMap) closedPenal.get(0)).get("CLOSED_PENAL").toString());
                                    }
                                }
                                //Code ends here
                                if (curr_dt.after(endDate)) {
                                    diffDayPending = DateUtil.dateDiff(DateUtil.addDays(endDate, (30 + CommonUtil.convertObjToInt(penalGraceValue))), currDate);
                                    calc += (double) ((((insAmt * noOfInstPay * clodespenalRt) / 100.0) * diffDayPending) / 365);
                                    if (penalList.size() > 0) {
                                        double calcAmt = (double) ((((insAmt * noOfInstPay * clodespenalRt) / 100.0) * diffDayPending) / 365);
                                        double closedAmt = CommonUtil.convertObjToDouble(penalList.get(penalList.size() - 1)) + calcAmt;
                                        penalList.remove(penalList.size() - 1);
                                        penalList.add(rod.getNearest((double) (closedAmt * 100), 100) / 100);
                                    }
                                }
                                // Absolute Not Required...
                            }
                            if (calc > 0) {
                                penalAmt = (long) (calc + 0.5);
                            }
                        }//pending installment calculation ends...
                        //Discount calculation details Starts...
                        for (int k = 0; k < noOfInstPay; k++) {
                            HashMap nextInstMap = new HashMap();
                            nextInstMap.put("SCHEME_NAME", scheme_Name);
                            nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                            nextInstMap.put("SL_NO", new Double(k + noOfInsPaid));
                            List listRec = ClientUtil.executeQuery("getSelectNextInstDate", nextInstMap);
                            if (listRec == null || listRec.size() == 0) {
                                Date curDate = (Date) curr_dt.clone();
                                int curMonth = curDate.getMonth();
                                curDate.setMonth(curMonth + 1);
                                curDate.setDate(instDay);
                                listRec = new ArrayList();
                                nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                                listRec.add(nextInstMap);
                            }
                            if (listRec != null && listRec.size() > 0) {
                                long discountAmt = 0;
                                nextInstMap = (HashMap) listRec.get(0);
                                instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                                long diffDay = DateUtil.dateDiff(instDate, currDate);
                                //Holiday Checking - Added By Suresh
                                HashMap holidayMap = new HashMap();
                                boolean checkHoliday = true;
                                instDate = setProperDtFormat(instDate);
                                holidayMap.put("NEXT_DATE", instDate);
                                holidayMap.put("BRANCH_CODE", getSelectedBranchID());
                                while (checkHoliday) {
                                    boolean tholiday = false;
                                    List Holiday = ClientUtil.executeQuery("checkHolidayDateOD", holidayMap);
                                    List weeklyOf = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
                                    boolean isHoliday = Holiday.size() > 0 ? true : false;
                                    boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                                    if (isHoliday || isWeekOff) {
                                        if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                            diffDay -= 1;
                                            instDate.setDate(instDate.getDate() + 1);
                                        } else {
                                            diffDay += 1;
                                            instDate.setDate(instDate.getDate() - 1);
                                        }
                                        holidayMap.put("NEXT_DATE", instDate);
                                        checkHoliday = true;
                                    } else {
                                        checkHoliday = false;
                                    }
                                }
                                if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y")
                                        || productMap.get("BONUS_ALLOWED").equals("N"))) {
                                    String discount = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_ALLOWED"));
                                    if (discount != null && !discount.equals("") && discount.equals("Y")) {
                                        String discountType = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_RATE_TYPE"));
                                        long discountValue = CommonUtil.convertObjToLong(productMap.get("DISCOUNT_RATE_AMT"));
                                        if (getRdoPrizedMember_Yes() == true) {//discount calculation for prized prerson...
                                            String discountPrizedDays = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_DAYS"));
                                            String discountPrizedMonth = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_MONTHS"));
                                            String discountPrizedAfter = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_AFTER"));
                                            String discountPrizedEnd = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_END"));
                                            long discountPrizedValue = CommonUtil.convertObjToLong(productMap.get("DIS_PRIZED_GRACE_PERIOD"));
                                            if (discountPrizedDays != null && !discountPrizedDays.equals("") && discountPrizedDays.equals("D") && diffDay <= discountPrizedValue) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    if (diffDay <= discountPrizedValue) {
                                                        totDiscAmt = totDiscAmt + calc;
                                                    }
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    if (diffDay <= discountPrizedValue) {
                                                        totDiscAmt = totDiscAmt + discountValue;
                                                    }
                                                }
                                            } else if (discountPrizedMonth != null && !discountPrizedMonth.equals("") && discountPrizedMonth.equals("M") && diffDay <= (discountPrizedValue * 30)) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountPrizedAfter != null && !discountPrizedAfter.equals("") && discountPrizedAfter.equals("A") && currDate.getDate() <= discountPrizedValue) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountPrizedEnd != null && !discountPrizedEnd.equals("") && discountPrizedEnd.equals("E") && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else {
                                                totDiscAmt = 0;
                                            }
                                        } else if (getRdoPrizedMember_No() == true) {//discount calculation non prized person...
                                            String discountGraceDays = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_DAYS"));
                                            String discountGraceMonth = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_MONTHS"));
                                            String discountGraceAfter = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_AFTER"));
                                            String discountGraceEnd = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_END"));
                                            long discountGraceValue = CommonUtil.convertObjToLong(productMap.get("DIS_GRACE_PERIOD"));
                                            if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("D")) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    if (diffDay <= discountGraceValue) {
                                                        totDiscAmt = totDiscAmt + calc;
                                                    }
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    if (diffDay <= discountGraceValue) {
                                                        totDiscAmt = totDiscAmt + discountValue;
                                                    }
                                                } else {
                                                    totDiscAmt = 0;
                                                }
                                            } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("M") && diffDay <= discountGraceValue * 30 && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("A") && currDate.getDate() <= discountGraceValue && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("E") && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else {
                                                totDiscAmt = 0;
                                            }
                                        }
                                    } else if (discount != null && !discount.equals("") && discount.equals("N")) {
                                        totDiscAmt = 0;
                                    }
                                    discountAmt = totDiscAmt - discountAmt;
                                    HashMap instMap = new HashMap();
                                    if (installmentMap.containsKey(String.valueOf(k + noOfInsPaid + 1))) {
                                        instMap = (HashMap) installmentMap.get(String.valueOf(k + noOfInsPaid + 1));
                                        instMap.put("DISCOUNT", String.valueOf(discountAmt));
                                        installmentMap.put(String.valueOf(k + noOfInsPaid + 1), instMap);
                                    }
                                    discountAmt = totDiscAmt;
                                }
                            }
                        }
                        //Bonus calculation details Starts...
                        for (int l = startNoForPenal; l <= noOfInstPay - addNo; l++) {
//                    for(int l = 1;l<=noOfInstPay;l++){
                            HashMap nextInstMap = new HashMap();
                            nextInstMap.put("SCHEME_NAME", scheme_Name);
                            nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                            nextInstMap.put("SL_NO", new Double(l + noOfInsPaid + addNo + firstInst_No));
                            List listRec = ClientUtil.executeQuery("getSelectBonusAndNextInstDateWithoutDivision", nextInstMap);
                            if (listRec == null || listRec.size() == 0) {
                                Date curDate = (Date) curr_dt.clone();
                                int curMonth = curDate.getMonth();
                                curDate.setMonth(curMonth + 1);
                                curDate.setDate(instDay);
                                listRec = new ArrayList();
                                nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                                listRec.add(nextInstMap);
                                bonusAvailabe = false;
                            }
                            if (listRec != null && listRec.size() > 0) {
                                nextInstMap = (HashMap) listRec.get(0);
                                instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                                bonusAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_BONUS_AMOUNT"))).doubleValue();
                                if (!productMap.get("MULTIPLE_MEMBER").equals("") && (productMap.get("MULTIPLE_MEMBER").equals("Y"))) {
                                    whereMap = new HashMap();
                                    int noOfCoChittal = 0;
                                    whereMap.put("SCHEME_NAMES", scheme_Name);
                                    whereMap.put("CHITTAL_NUMBER", chittalNo);
                                    List applicationLst = ClientUtil.executeQuery("getSelectChitNoNotinMasterDetails", whereMap); // Count No Of Co-Chittals
                                    if (applicationLst != null && applicationLst.size() > 0) {
                                        noOfCoChittal = applicationLst.size();
                                        bonusAmt = bonusAmt / noOfCoChittal;
                                    }
                                }
                                if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                        && bonusAmt > 0) {
                                    //Rounding rod = new Rounding();
                                    if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")) {
                                        bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                                    } else {
                                        bonusAmt = (double) rod.lower((long) (bonusAmt * 100), 100) / 100;
                                    }
                                }
                                long diffDay = DateUtil.dateDiff(instDate, currDate);
                                //Holiday Checking - Added By Suresh
                                HashMap holidayMap = new HashMap();
                                boolean checkHoliday = true;
                                instDate = setProperDtFormat(instDate);
                                holidayMap.put("NEXT_DATE", instDate);
                                holidayMap.put("BRANCH_CODE", getSelectedBranchID());
                                while (checkHoliday) {
                                    boolean tholiday = false;
                                    List Holiday = ClientUtil.executeQuery("checkHolidayDateOD", holidayMap);
                                    List weeklyOf = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
                                    boolean isHoliday = Holiday.size() > 0 ? true : false;
                                    boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                                    if (isHoliday || isWeekOff) {
                                        if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                            diffDay -= 1;
                                            instDate.setDate(instDate.getDate() + 1);
                                        } else {
                                            diffDay += 1;
                                            instDate.setDate(instDate.getDate() - 1);
                                        }
                                        holidayMap.put("NEXT_DATE", instDate);
                                        checkHoliday = true;
                                    } else {
                                        checkHoliday = false;
                                    }
                                }
                                //                            String hoildayInt = CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT"));
                                if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y"))) {
                                    String prizedDefaultYesN = "";
                                    if (productMap.containsKey("PRIZED_DEFAULTERS") && productMap.get("PRIZED_DEFAULTERS") != null) {
                                        prizedDefaultYesN = CommonUtil.convertObjToStr(productMap.get("PRIZED_DEFAULTERS"));
                                    }
                                    if (bonusAvailabe == true) {
                                        HashMap nextActMap = new HashMap();
                                        long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
                                        String bonusPrzInMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
                                        if (getRdoPrizedMember_Yes() == true) {
                                            nextActMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(scheme_Name));
                                            nextActMap.put("DIVISION_NO", CommonUtil.convertObjToStr(divNo));
                                            nextActMap.put("SL_NO", CommonUtil.convertObjToDouble(l + noOfInsPaid));
                                            List listAuc = ClientUtil.executeQuery("getSelectNextAuctDate", nextActMap);
                                            if (listAuc != null && listAuc.size() > 0) {
                                                nextActMap = (HashMap) listAuc.get(0);
                                            }
                                            Date drawAuctionDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextActMap.get("DRAW_AUCTION_DATE")));
                                            Calendar cal = null;//Added By Nidhin holiday checking not correct
                                            Date newDate = null;
                                            if (bonusPrzInMonth != null && bonusPrzInMonth.equalsIgnoreCase("M")) {
                                                cal = Calendar.getInstance();
                                                cal.setTime(drawAuctionDate);
                                                cal.add(Calendar.MONTH, CommonUtil.convertObjToInt(bonusPrizedValue));
                                                newDate = cal.getTime();
                                            } else {
                                                newDate = DateUtil.addDays(drawAuctionDate, CommonUtil.convertObjToInt(bonusPrizedValue));
                                            }
                                            long dateDiff = DateUtil.dateDiff(curr_dt, newDate);
                                            //Holiday checking Added  By Nidhin 19/11/2014
                                            HashMap holidayCheckMap = new HashMap();
                                            boolean checkForHoliday = true;
                                            newDate = setProperDtFormat(newDate);
                                            holidayCheckMap.put("NEXT_DATE", newDate);
                                            holidayCheckMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                                            while (checkForHoliday) {
                                                List Holiday = ClientUtil.executeQuery("checkHolidayDateOD", holidayCheckMap);
                                                List weeklyOf = ClientUtil.executeQuery("checkWeeklyOffOD", holidayCheckMap);
                                                boolean isHoliday = Holiday.size() > 0 ? true : false;
                                                boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                                                if (isHoliday || isWeekOff) {
                                                    if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                                        dateDiff += 1;
                                                        newDate.setDate(newDate.getDate() + 1);
                                                    } else {
                                                        dateDiff -= 1;
                                                        newDate.setDate(newDate.getDate() - 1);
                                                    }
                                                    holidayCheckMap.put("NEXT_DATE", newDate);
                                                    checkForHoliday = true;
                                                } else {
                                                    checkForHoliday = false;
                                                }
                                            }
                                            //End for HoliDay Checking
                                            HashMap MdsWhereMap = new HashMap();
                                            MdsWhereMap.put("CHITTAL_NO", chittalNo);
                                            MdsWhereMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                                            MdsWhereMap.put("SCHEME_NAME", scheme_Name);
                                            List paymentList = ClientUtil.executeQuery("getSelectMDSPaymentDetails", MdsWhereMap);
                                            if (paymentList != null && paymentList.size() > 0 && productMap.get("PRIZED_OWNER_BONUS").equals("N")) {
                                            } else if (paymentList != null && paymentList.size() > 0 && productMap.get("PRIZED_OWNER_BONUS").equals("Y")) { //akhila
                                                String bonusPrizedDays = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_DAYS"));
                                                String bonusPrizedMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
                                                String bonusPrizedAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_AFT"));
                                                String bonusPrizedEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_END"));
                                                //long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
                                                if (bonusPrizedDays != null && !bonusPrizedDays.equals("") && bonusPrizedDays.equals("D") && diffDay <= bonusPrizedValue) {
                                                    if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                                        totBonusAmt = totBonusAmt + bonusAmt;
                                                    }
                                                    bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                } else if (bonusPrizedMonth != null && !bonusPrizedMonth.equals("") && bonusPrizedMonth.equals("M") && diffDay <= (bonusPrizedValue * 30)) {
                                                    if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                                        totBonusAmt = totBonusAmt + bonusAmt;
                                                    }
                                                    bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                } else if (bonusPrizedAfter != null && !bonusPrizedAfter.equals("") && bonusPrizedAfter.equals("A") && currDate.getDate() <= bonusPrizedValue) {
                                                    if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                                        totBonusAmt = totBonusAmt + bonusAmt;
                                                    }
                                                    bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                } else if (bonusPrizedEnd != null && !bonusPrizedEnd.equals("") && bonusPrizedEnd.equals("E")) {
                                                } else {
                                                }
                                            } else if (productMap.get("PRIZED_OWNER_BONUS").equals("Y") || productMap.get("PRIZED_OWNER_BONUS").equals("N"))//is work prized owner yes or no
                                            {
                                                String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
                                                String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
                                                String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
                                                String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
                                                long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
                                                if (bonusGraceDays != null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay <= bonusGraceValue) {
                                                    if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                                        totBonusAmt = totBonusAmt + bonusAmt;
                                                    }
                                                    bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                } else if (bonusGraceMonth != null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay <= (bonusGraceValue * 30)) {
                                                    if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                                        totBonusAmt = totBonusAmt + bonusAmt;
                                                    }
                                                    bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                } else if (bonusGraceOnAfter != null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && currDate.getDate() <= bonusGraceValue) {
                                                    if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                                        totBonusAmt = totBonusAmt + bonusAmt;
                                                    }
                                                    bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                } else if (bonusGraceEnd != null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E")) {
                                                } else {
                                                }

                                            } else {
                                            }
                                            //akhila
                                        } else if (getRdoPrizedMember_No() == true) {
                                            String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
                                            String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
                                            String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
                                            String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
                                            long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
                                            if (bonusGraceDays != null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay <= bonusGraceValue) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                                bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                            } else if (bonusGraceMonth != null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay <= (bonusGraceValue * 30)) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                                bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                            } else if (bonusGraceOnAfter != null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && currDate.getDate() <= bonusGraceValue) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                                bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                            } else if (bonusGraceEnd != null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E")) {
                                            } else {
                                            }
                                        }
                                    }
                                    HashMap instMap = new HashMap();
                                    if (installmentMap.containsKey(String.valueOf(l + noOfInsPaid + addNo))) {
                                        //Rounding rod = new Rounding();
                                        instMap = (HashMap) installmentMap.get(String.valueOf(l + noOfInsPaid + addNo));
                                        //Added By Suresh
                                        if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                                && bonusAmt > 0) {
                                            if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")) {
                                                bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                                            } else {
                                                bonusAmt = (double) rod.lower((long) (bonusAmt * 100), 100) / 100;
                                            }
                                        }
                                        instMap.put("BONUS", String.valueOf(bonusAmt));
                                        installmentMap.put(String.valueOf(l + noOfInsPaid + addNo), instMap);
                                    }
                                }
                            }
                            bonusAmt = 0;
                        }
                        if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                && totBonusAmt > 0) {
                            //Rounding rod = new Rounding();
                            if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")) {
                                totBonusAmt = (double) rod.getNearest((long) (totBonusAmt * 100), 100) / 100;
                            } else {
                                totBonusAmt = (double) rod.lower((long) (totBonusAmt * 100), 100) / 100;
                            }
                        }

                        int insDay = 0;
                        Date paidUpToDate = null;
                        HashMap instDateMap = new HashMap();
                        instDateMap.put("SCHEME_NAME", scheme_Name);
                        instDateMap.put("DIVISION_NO", divNo);
                        instDateMap.put("INSTALLMENT_NO", CommonUtil.convertObjToInt(String.valueOf(count)));
                        lst = ClientUtil.executeQuery("getSelectInstUptoPaid", instDateMap);
                        if (lst != null && lst.size() > 0) {
                            instDateMap = (HashMap) lst.get(0);
                            paidUpToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(instDateMap.get("NEXT_INSTALLMENT_DATE")));
                        } else {
                            Date startedDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("SCHEME_START_DT")));
                            insDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                            startedDate.setDate(insDay);
                            int stMonth = startedDate.getMonth();
                            startedDate.setMonth(stMonth + (int) count - 1);
                            paidUpToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(startedDate));
                        }




                        String narration = "";
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM/yyyy");
                        int noInstPay = CommonUtil.convertObjToInt(noOfInsPay);
                        if (noInstPay == 1) {
                            narration = "Inst#" + (noOfInsPaid + 1);
                            // Date dt = DateUtil.addDays(paidUpToDate, 30);
                            Date dt1 = DateUtil.addDays(startDate, 30 * noOfInsPaid);
                            narration += " " + sdf.format(dt1);
                        } else if (noInstPay > 1) {
                            narration = "Inst#" + (noOfInsPaid + 1);
                            narration += "-" + (noOfInsPaid + noInstPay);
                            // Date dt = DateUtil.addDays(paidUpToDate, 30);
                            Date dt1 = DateUtil.addDays(startDate, 30 * noOfInsPaid);
                            narration += " " + sdf.format(dt1);
                            // dt = DateUtil.addDays(paidUpToDate, 30 * noInstPay);
                            dt1 = DateUtil.addDays(dt1, 30 * (noInstPay - 1));
                            narration += " To " + sdf.format(dt1);
                        }
                        //
                        double instAmt = insAmt * CommonUtil.convertObjToDouble(noOfInsPay).doubleValue();
                        double totalPayable = instAmt - (totBonusAmt + totDiscAmt);
                        netAmt = totalPayable + penalAmt + noticAmt + arbitAmt;

                        dataMap.put("SCHEME_NAME", scheme_Name);
                        dataMap.put("CHITTAL_NO", chittalNo);
                        dataMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                        dataMap.put("MEMBER_NAME", mem_name);
                        dataMap.put("DIVISION_NO", String.valueOf(divNo));
                        dataMap.put("CHIT_START_DT", startDate);
                        dataMap.put("INSTALLMENT_DATE", insDate);
                        dataMap.put("NO_OF_INSTALLMENTS", String.valueOf(totIns));
                        dataMap.put("CURR_INST", String.valueOf(curInsNo));
                        dataMap.put("PENDING_INST", insDue);
                        dataMap.put("PENDING_DUE_AMT", String.valueOf(insDueAmt));
                        dataMap.put("NO_OF_INST_PAY", CommonUtil.convertObjToInt(noOfInsPay));
                        dataMap.put("INST_AMT_PAYABLE", String.valueOf(totalPayable));
                        dataMap.put("PAID_INST", String.valueOf(noOfInsPaid));
                        dataMap.put("TOTAL_PAYABLE", String.valueOf(instAmt));
                        dataMap.put("PAID_DATE", currDate);
                        dataMap.put("INST_AMT", String.valueOf(insAmt));
                        dataMap.put("SCHEME_END_DT", endDate);
                        if (getRdoPrizedMember_Yes() == true) {
                            dataMap.put("PRIZED_MEMBER", "Y");
                        } else {
                            dataMap.put("PRIZED_MEMBER", "N");
                        }
                        dataMap.put("BONUS", String.valueOf(totBonusAmt));
                        dataMap.put("DISCOUNT", String.valueOf(totDiscAmt));
                        dataMap.put("PENAL", String.valueOf(penalAmt));
                        dataMap.put("NOTICE_AMOUNT", String.valueOf(noticAmt));
                        dataMap.put("ARBITRATION_AMOUNT", String.valueOf(arbitAmt));
                        dataMap.put("NET_AMOUNT", String.valueOf(netAmt));
                        dataMap.put("NARRATION", narration);
                        dataMap.put("EACH_MONTH_DATA", installmentMap);
                        dataMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                        dataMap.put("MEMBER_NO", mem_no);
                        finalMap.put(chittalNo, dataMap);

                        //For spliting transcations..

                        if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
                            HashMap splitTransMap = new HashMap();
                            ArrayList RealBonusAmountList = new ArrayList();
                            ArrayList RealPenalList = new ArrayList();
                            ArrayList RealInstList = new ArrayList();
                            if (penalList != null && penalList.size() > 0) {
                                double d = 0;
                                double firVal = CommonUtil.convertObjToDouble(penalList.get(0));
                                Collections.reverse(penalList);
                                for (int i = 0; i <= penalList.size(); i++) {
                                    if (i + 1 < penalList.size()) {
                                        d = CommonUtil.convertObjToDouble(penalList.get(i)) - CommonUtil.convertObjToDouble(penalList.get(i + 1));
                                        penalRealList.add(d);
                                    }
                                }
                                penalRealList.add(firVal);
                            }
                            Collections.reverse(penalRealList);
                            Collections.reverse(bonusAmountList);
                            if (penalRealList.size() != noOfInstPay) {
                                penalRealList.add(0);
                            }
                            //for (int p = 0; p < penalRealList.size(); p++) {
                            if (bonusAmountList.size() != penalRealList.size()) {
                                int SplitCount = CommonUtil.convertObjToInt(penalRealList.size() - bonusAmountList.size());
                                for (int i = 0; i < SplitCount; i++) {
                                    bonusAmountList.add(0);
                                }
                            }
                            double insAmta = 0.0;
                            for (int h = 0; h < bonusAmountList.size(); h++) {
                                double instAmounts = insAmt;
                                instAmounts -= CommonUtil.convertObjToDouble(bonusAmountList.get(h));
                                insAmta = instAmounts;
                                instList.add(insAmta);
                            }
                            if (instList.size() != penalRealList.size()) {
                                int SplitCount = 0;
                                int insSize = instList.size();
                                int penalSize = penalRealList.size();
                                SplitCount = insSize - penalSize;
                                for (int i = 0; i < SplitCount; i++) {
                                    penalRealList.add(0);
                                }

                            }
                            //}    
                            for (int k = 0; k < noOfInstPay; k++) {
                                if (noOfInstPay > 0) {
                                    RealBonusAmountList.add(bonusAmountList.get(CommonUtil.convertObjToInt(k)));
                                    RealPenalList.add(penalRealList.get(CommonUtil.convertObjToInt(k)));
                                    RealInstList.add(instList.get(CommonUtil.convertObjToInt(k)));
                                }
                            }
                            //HashMap splitTransMap = new HashMap();
                            splitTransMap.put("BONUS_AMT_LIST", RealBonusAmountList);
                            splitTransMap.put("INST_AMT_LIST", RealInstList);
                            splitTransMap.put("PENAL_AMT_LIST", RealPenalList);
                            splitTransMap.put("NARRATION_LIST", narrationList);
                            splitTransMap.put("CHITTAL_NO", chittalNo);
                            splitTransMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                            splitTransMap.put("IS_SPLIT_MDS_TRANSACTION", isSplitMDSTransaction);
                            splitTransMap.put("INSTALL_NO", noOfInsPaid);
                            splitTransMap.putAll(dataMap);
                            finalSplitMap.put(chittalNo, splitTransMap);
                            observable.setMdsSplitMap(finalSplitMap);
                        }
                        String instAmount = String.valueOf(instAmt);
                        instAmount = CurrencyValidation.formatCrore(instAmount).replaceAll(",", "");

                        String totBonusAmount = String.valueOf(totBonusAmt);
                        totBonusAmount = CurrencyValidation.formatCrore(totBonusAmount).replaceAll(",", "");

                        String totDiscAmount = String.valueOf(totDiscAmt);
                        totDiscAmount = CurrencyValidation.formatCrore(totDiscAmount).replaceAll(",", "");

                        String totalPayableAmount = String.valueOf(totalPayable);
                        totalPayableAmount = CurrencyValidation.formatCrore(totalPayableAmount).replaceAll(",", "");

                        String penalAmount = String.valueOf(penalAmt);
                        penalAmount = CurrencyValidation.formatCrore(penalAmount).replaceAll(",", "");

                        String noticeAmout = String.valueOf(noticAmt);
                        noticeAmout = CurrencyValidation.formatCrore(noticeAmout).replaceAll(",", "");

                        String arbitryAmout = String.valueOf(arbitAmt);
                        arbitryAmout = CurrencyValidation.formatCrore(arbitryAmout).replaceAll(",", "");

                        String netAmount = String.valueOf(netAmt);
                        netAmount = CurrencyValidation.formatCrore(netAmount).replaceAll(",", "");
                        tblMemberReceipt.setValueAt(instAmount, selectedRow, 8);
                        tblMemberReceipt.setValueAt(totBonusAmount, selectedRow, 9);
                        tblMemberReceipt.setValueAt(totDiscAmount, selectedRow, 10);
                        tblMemberReceipt.setValueAt(totalPayableAmount, selectedRow, 11);
                        tblMemberReceipt.setValueAt(penalAmount, selectedRow, 12);
                        tblMemberReceipt.setValueAt(noticeAmout, selectedRow, 13);
                        tblMemberReceipt.setValueAt(arbitryAmout, selectedRow, 14);
                        tblMemberReceipt.setValueAt(netAmount, selectedRow, 15);
                        tblMemberReceipt.revalidate();
                        ((DefaultTableModel) tblMemberReceipt.getModel()).fireTableDataChanged();
                    } else {
                        ClientUtil.showAlertWindow("Exceeds The No Of Total Installment !!! ");
                        tblMemberReceipt.setValueAt("", tblMemberReceipt.getSelectedRow(), 6);
                        tblMemberReceipt.setValueAt("", tblMemberReceipt.getSelectedRow(), 7);
                        tblMemberReceipt.setValueAt("", tblMemberReceipt.getSelectedRow(), 8);
                        tblMemberReceipt.setValueAt("", tblMemberReceipt.getSelectedRow(), 9);
                        tblMemberReceipt.setValueAt("", tblMemberReceipt.getSelectedRow(), 10);
                        tblMemberReceipt.setValueAt("", tblMemberReceipt.getSelectedRow(), 11);
                        tblMemberReceipt.setValueAt("", tblMemberReceipt.getSelectedRow(), 12);
                        tblMemberReceipt.setValueAt("", tblMemberReceipt.getSelectedRow(), 13);
                        tblMemberReceipt.setValueAt("", tblMemberReceipt.getSelectedRow(), 14);
                    }
                }
            }

        } catch (Exception e) {
            parseException.logException(e, true);
            e.printStackTrace();
        }
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) curr_dt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    private void calcTotal() {
        double totNetAmt = 0;
        double totBonusAmt = 0;
        double totDiscountAmt = 0;
        double totInstAmtPayable = 0;
        double totIntAmt = 0;
        double totNoticeAmt = 0;
        double totArbitAmt = 0;
        if (tblMemberReceipt.getRowCount() > 0) {
            for (int i = 0; i < tblMemberReceipt.getRowCount(); i++) {
                totBonusAmt = totBonusAmt + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(i, 9).toString()).doubleValue();
                totDiscountAmt = totDiscountAmt + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(i, 10).toString()).doubleValue();
                totInstAmtPayable = totInstAmtPayable + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(i, 11).toString()).doubleValue();
                totIntAmt = totIntAmt + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(i, 12).toString()).doubleValue();
                totNoticeAmt = totNoticeAmt + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(i, 13).toString()).doubleValue();
                totArbitAmt = totArbitAmt + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(i, 14).toString()).doubleValue();
                totNetAmt = totNetAmt + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(i, 15).toString()).doubleValue();
            }
        }
        txtTotalNetAmount.setText(String.valueOf(totNetAmt));
        txtTotInstAmt.setText(String.valueOf(totInstAmtPayable));
        txtTotBonusAmt.setText(String.valueOf(totBonusAmt));
        txtTotDiscountAmt.setText(String.valueOf(totDiscountAmt));
        txtTotInterest.setText(String.valueOf(totIntAmt));
        txtNoticeAmt.setText(String.valueOf(totNoticeAmt));
        txtAribitrationAmt.setText(String.valueOf(totArbitAmt));
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingAmount(txtTotalNetAmount.getText());

        transactionUI.setCallingApplicantName(lblMemberName.getText());
        if (tblMemberGrid.getRowCount() >= 0) {//added by chithra
            transactionUI.setCallingApplicantName(CommonUtil.convertObjToStr(tblMemberGrid.getValueAt(0, 1)));
        }
        if (tblMemberGrid.getRowCount() == 1) {
            setStandingTransactionUI(txtTotalNetAmount.getText());
        }
    }
    private void txtMembershipNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMembershipNoFocusLost
        // TODO add your handling code here:
        if (txtMembershipNo.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            //whereMap.put("MEMBER_NO", txtMembershipNo.getText());
            whereMap.put("MEMBER_NUMBER", txtMembershipNo.getText());
            List lst = ClientUtil.executeQuery("getSelectMemberList", whereMap);
            if (lst != null && lst.size() > 0) {
                viewType = "MEMBER_LIST";
                whereMap = (HashMap) lst.get(0);
                fillData(whereMap);
                lst = null;
                whereMap = null;
            } else {
                ClientUtil.displayAlert("Invalid Member No!!");
                txtMembershipNo.setText("");
                observable.resetForm();
            }
        }
    }//GEN-LAST:event_txtMembershipNoFocusLost

    private void setStandingTransactionUI(String netAmount) {
        HashMap whereMap = new HashMap();
        double chargeAmount = 0.0;
        whereMap.put("MEMBER_NO", CommonUtil.convertObjToStr(tblMemberGrid.getValueAt(0, 0)));
        List standingList = ClientUtil.executeQuery("getMemberStandingDetails", whereMap);
        if (standingList != null && standingList.size() > 0) {
            whereMap = (HashMap) standingList.get(0);
            if (whereMap != null && whereMap.get("DR_ACT_NO") != null && !whereMap.get("DR_ACT_NO").equals("")) {
                transactionUI.cancelAction(false);
                transactionUI.setButtonEnableDisable(true);
                transactionUI.resetObjects();
                transactionUI.setCallingAmount(netAmount);
                transactionUI.setCallingTransType("TRANSFER");
                transactionUI.setCallingTransProdType(CommonUtil.convertObjToStr(whereMap.get("PROD_TYPE")));
                transactionUI.setCallingProdID(CommonUtil.convertObjToStr(whereMap.get("PROD_ID")));
                transactionUI.setCallingTransAcctNo(CommonUtil.convertObjToStr(whereMap.get("DR_ACT_NO")));
                transactionUI.setCallingApplicantName(CommonUtil.convertObjToStr(whereMap.get("MEMBER_NAME")));
            }
        }
    }

    private Object[][] setTableData() {
        HashMap amap = new HashMap();
        HashMap whereMap1 = new HashMap();
        String mem_no = "";
        String mem_name = "";
        String mem_type = "";
        List alist = new ArrayList();
        for (int y = 0; y < bufferList.size(); y++) {
            amap = new HashMap();
            amap = (HashMap) bufferList.get(y);
            mem_no = amap.get("MEM_NO").toString();
            mem_name = amap.get("MEM_NAME").toString();
            mem_type = amap.get("MEM_TYPE").toString();
            if (mem_no.length() > 0) {
                //whereMap1.put("MEMBER_NO", mem_no);
                whereMap1.put("MEMBER_NUMBER", mem_no);
                List lst = ClientUtil.executeQuery("getSelectMemberList", whereMap1);
                alist.addAll(y, lst);
            }
        }
        Object totalList[][] = new Object[alist.size()][16];
//            if(lst !=null && lst.size()>0){
        //   whereMap = (HashMap)lst.get(0);
//                txtMembershipNo.setText(CommonUtil.convertObjToStr(whereMap.get("MEMBER_NO")));
//                lblMemberName.setText(CommonUtil.convertObjToStr(whereMap.get("MEMBER_NAME")));
//                lblMemberType.setText(CommonUtil.convertObjToStr(whereMap.get("MEMBER_TYPE")));
        // observable.setTxtMembershipNo(CommonUtil.convertObjToStr(whereMap.get("MEMBER_NO")));
        // observable.setLblMemberName(CommonUtil.convertObjToStr(whereMap.get("MEMBER_NAME")));
        if (alist != null && alist.size() > 0) {
            for (int i = 0; i < alist.size(); i++) {
                // java.util.LinkedHashMap whereMap=(java.util.LinkedHashMap)alist.get(i);
                HashMap whereMap = (HashMap) alist.get(i);
                //  whereMap = (HashMap)lst.get(i);
                int divNo = 0;
                int curInsNo = 0;
                int noOfInsPaid = 0;
                long pendingInst = 0;
                long noOfInstWantToPay = 0;
                int instDay = 1;
                Date currDate = (Date) curr_dt.clone();
                totalList[i][0] = (CommonUtil.convertObjToStr(whereMap.get("MEMBER_NO")));
                totalList[i][1] = (CommonUtil.convertObjToStr(whereMap.get("MEMBER_NAME")));
                totalList[i][2] = (CommonUtil.convertObjToStr(whereMap.get("SCHEME_NAME")));
                totalList[i][3] = (CommonUtil.convertObjToStr(whereMap.get("SCHEME_DESC")));
                totalList[i][4] = (CommonUtil.convertObjToStr(whereMap.get("CHITTAL_NO")));
                totalList[i][5] = (CommonUtil.convertObjToStr(whereMap.get("SUB_NO")));
                HashMap chittalMap = new HashMap();
                chittalMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(whereMap.get("CHITTAL_NO")));
                chittalMap.put("SUB_NO", CommonUtil.convertObjToInt(whereMap.get("SUB_NO")));
                List chitLst = ClientUtil.executeQuery("getSelctApplnReceiptDetails", chittalMap);
                if (chitLst != null && chitLst.size() > 0) {
                    chittalMap = (HashMap) chitLst.get(0);
                    divNo = CommonUtil.convertObjToInt(chittalMap.get("DIVISION_NO"));
                    instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                }
                HashMap chitMap = new HashMap();
                chitMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(whereMap.get("CHITTAL_NO")));
                chitMap.put("SUB_NO", CommonUtil.convertObjToInt(whereMap.get("SUB_NO")));
                List insList = ClientUtil.executeQuery("getNoOfInstalmentsPaid", chitMap);
                if (insList != null && insList.size() > 0) {
                    chitMap = (HashMap) insList.get(0);
                    noOfInsPaid = CommonUtil.convertObjToInt(chitMap.get("NO_OF_INST"));
                }
                HashMap insDateMap = new HashMap();
                insDateMap.put("DIVISION_NO", divNo);
                insDateMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(whereMap.get("SCHEME_NAME")));
                insDateMap.put("CURR_DATE", curr_dt.clone());
                insDateMap.put("ADD_MONTHS", CommonUtil.convertObjToInt("-1"));
                List insDateLst = ClientUtil.executeQuery("getMDSCurrentInsDate", insDateMap);
                if (insDateLst != null && insDateLst.size() > 0) {
                    insDateMap = (HashMap) insDateLst.get(0);
                    curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
                    pendingInst = curInsNo - noOfInsPaid;
                    //Commented by sreekrishnan
//                    if (instDay < currDate.getDate()) {
                    pendingInst = pendingInst + 1;
//                    } else {
//                        pendingInst = pendingInst;
//                     }
                    String advance_Collection = "";
                    HashMap prodMap = new HashMap();
                    prodMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(whereMap.get("SCHEME_NAME")));
                    List prodLst = ClientUtil.executeQuery("getSelectSchemeAcctHead", prodMap);
                    if (prodLst != null && prodLst.size() > 0) {
                        prodMap = (HashMap) prodLst.get(0);
                        advance_Collection = CommonUtil.convertObjToStr(prodMap.get("ADVANCE_COLLECTION"));
                    }
                    if (advance_Collection.equals("Y") /*
                             * CommonUtil.convertObjToStr(prodMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")
                             */) {
                        pendingInst = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO")) - (int) noOfInsPaid - 1;
                        noOfInstWantToPay = pendingInst;
                    } else {
                        pendingInst = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO")) - (int) noOfInsPaid;
                        noOfInstWantToPay = pendingInst;
                    }
                    if (pendingInst < 0) {
                        pendingInst = 0;
                    }
                }
                totalList[i][6] = String.valueOf(noOfInstWantToPay);
                totalList[i][7] = "";
                totalList[i][8] = "";
                totalList[i][9] = "";
                totalList[i][10] = "";
                totalList[i][11] = "";
                totalList[i][12] = "";
                totalList[i][13] = "";
                totalList[i][14] = "";
                totalList[i][15] = "";
            }
        }
        return totalList;
//            }else{
//                ClientUtil.displayAlert("Invalid Member No !!! ");
//                observable.resetForm();
//                txtMembershipNo.setText("");
//                lblMemberName.setText("");
//                lblMemberType.setText("");
//            }
//            return null;
        ////  }
        ////  }

    }

    private double setNoticeChargeAmount(String chittalNo, String subNo) {
        HashMap whereMap = new HashMap();
        double chargeAmount = 0.0;
        whereMap.put("ACT_NUM", chittalNo + "_" + subNo);
        List chargeList = ClientUtil.executeQuery("getMDSNoticeChargeDetails", whereMap);
        if (chargeList != null && chargeList.size() > 0) {
            for (int i = 0; i < chargeList.size(); i++) {
                whereMap = (HashMap) chargeList.get(i);
                chargeAmount += CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
            }
        }
        chargeList = null;
        return chargeAmount;
    }

    private double setCaseExpensesAmount(String chittalNo, String subNo) {
        HashMap whereMap = new HashMap();
        double chargeAmount = 0.0;
        whereMap.put("ACT_NUM", chittalNo + "_" + subNo);
        List chargeList = ClientUtil.executeQuery("getMDSCaseChargeDetails", whereMap);
        if (chargeList != null && chargeList.size() > 0) {
            for (int i = 0; i < chargeList.size(); i++) {
                whereMap = (HashMap) chargeList.get(i);
                chargeAmount += CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
            }
            Rounding rod = new Rounding();
            chargeAmount = (double) rod.getNearest((long) (chargeAmount * 100), 100) / 100;
        }
        chargeList = null;
        return chargeAmount;
    }

    private Object[][] setTableData1() {
        if (txtMembershipNo.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("MEMBER_NO", txtMembershipNo.getText());
            List lst = ClientUtil.executeQuery("getSelectMemberList", whereMap);
            if (lst != null && lst.size() > 0) {
                whereMap = (HashMap) lst.get(0);
                txtMembershipNo.setText(CommonUtil.convertObjToStr(whereMap.get("MEMBER_NO")));
                lblMemberName.setText(CommonUtil.convertObjToStr(whereMap.get("MEMBER_NAME")));
                lblMemberType.setText(CommonUtil.convertObjToStr(whereMap.get("MEMBER_TYPE")));
                observable.setTxtMembershipNo(CommonUtil.convertObjToStr(whereMap.get("MEMBER_NO")));
                observable.setLblMemberName(CommonUtil.convertObjToStr(whereMap.get("MEMBER_NAME")));
                Object totalList[][] = new Object[lst.size()][15];
                if (lst != null && lst.size() > 0) {
                    for (int i = 0; i < lst.size(); i++) {
                        whereMap = (HashMap) lst.get(i);
                        int divNo = 0;
                        int curInsNo = 0;
                        int noOfInsPaid = 0;
                        long pendingInst = 0;
                        int instDay = 1;
                        Date currDate = (Date) curr_dt.clone();
                        totalList[i][0] = CommonUtil.convertObjToStr(whereMap.get("SCHEME_NAME"));
                        totalList[i][1] = (CommonUtil.convertObjToStr(whereMap.get("CHITTAL_NO")));
                        totalList[i][2] = (CommonUtil.convertObjToStr(whereMap.get("SUB_NO")));
                        HashMap chittalMap = new HashMap();
                        chittalMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(whereMap.get("CHITTAL_NO")));
                        chittalMap.put("SUB_NO", CommonUtil.convertObjToInt(whereMap.get("SUB_NO")));
                        List chitLst = ClientUtil.executeQuery("getSelctApplnReceiptDetails", chittalMap);
                        if (chitLst != null && chitLst.size() > 0) {
                            chittalMap = (HashMap) chitLst.get(0);
                            divNo = CommonUtil.convertObjToInt(chittalMap.get("DIVISION_NO"));
                            instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                        }

                        HashMap chitMap = new HashMap();
                        chitMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(whereMap.get("CHITTAL_NO")));
                        chitMap.put("SUB_NO", CommonUtil.convertObjToInt(whereMap.get("SUB_NO")));
                        List insList = ClientUtil.executeQuery("getNoOfInstalmentsPaid", chitMap);
                        if (insList != null && insList.size() > 0) {
                            chitMap = (HashMap) insList.get(0);
                            noOfInsPaid = CommonUtil.convertObjToInt(chitMap.get("NO_OF_INST"));
                        }

                        HashMap insDateMap = new HashMap();
                        insDateMap.put("DIVISION_NO", divNo);
                        insDateMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(whereMap.get("SCHEME_NAME")));
                        insDateMap.put("CURR_DATE", curr_dt.clone());
                        insDateMap.put("ADD_MONTHS", CommonUtil.convertObjToInt("-1"));
                        List insDateLst = ClientUtil.executeQuery("getMDSCurrentInsDate", insDateMap);
                        if (insDateLst != null && insDateLst.size() > 0) {
                            insDateMap = (HashMap) insDateLst.get(0);
                            curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
                            pendingInst = curInsNo - noOfInsPaid;
                            if (instDay < currDate.getDate()) {
                                pendingInst = pendingInst + 1;
                            } else {
                                pendingInst = pendingInst;
                            }
                            if (pendingInst < 0) {
                                pendingInst = 0;
                            }
                        }
                        totalList[i][3] = String.valueOf(pendingInst);
                        totalList[i][4] = "";
                        totalList[i][5] = "";
                        totalList[i][6] = "";
                        totalList[i][7] = "";
                        totalList[i][8] = "";
                        totalList[i][9] = "";
                        totalList[i][10] = "";
                        totalList[i][11] = "";
                        totalList[i][12] = "";
                    }
                }
                return totalList;
            } else {
                ClientUtil.displayAlert("Invalid Member No !!! ");
                observable.resetForm();
                txtMembershipNo.setText("");
                lblMemberName.setText("");
                lblMemberType.setText("");
            }
            return null;
        }
        return null;
    }

    private void btnMembershipNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMembershipNoActionPerformed
        // TODO add your handling code here:
        callView("MEMBER_LIST");
    }//GEN-LAST:event_btnMembershipNoActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        callView("Enquiry");
        lblStatus.setText("Enquiry");
        btnDelete.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
    }            //    private void enableDisableAliasBranchTable(boolean flag) {//GEN-LAST:event_btnViewActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
//        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EXCEPTION);
        updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("MEMBER_RECEIPT_ID", observable.getMemberTransId());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap, observable.getMemberTransId());
            viewType = "";
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);
            whereMap.put("TRANS_DT", curr_dt.clone());
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                mapParam.put(CommonConstants.MAP_NAME, "getMemberReceiptCashierAuthorize");
            } else {
                mapParam.put(CommonConstants.MAP_NAME, "getMemberReceiptAuthorize");
            }
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            //            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    public void authorize(HashMap map, String id) {
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            if (transactionUI.getOutputTO().size() > 0) {
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            observable.doAction();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            //            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        ClientUtil.clearAll(this);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        setButtonEnableDisable();
        ClientUtil.enableDisable(panMemberReceiptEntryDetails, true);
        ClientUtil.enableDisable(panInsideGeneralRemittance4, true);
        txtMembershipNo.setEnabled(true);
        txtTotInstAmt.setEnabled(false);
        txtTotBonusAmt.setEnabled(false);
        txtTotDiscountAmt.setEnabled(false);
        txtTotInterest.setEnabled(false);
        txtNoticeAmt.setEnabled(false);
        txtAribitrationAmt.setEnabled(false);
        txtTotalNetAmount.setEnabled(false);
        btnMembershipNo.setEnabled(true);
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        //txtMembershipNo.setEnabled(false);
        bufferList = new ArrayList();
        getTableData();
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        callView("Enquiry");
        lblStatus.setText("Edit");
        btnDelete.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        btnSave.setEnabled(false);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            int transactionSize = 0;
            if (transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(txtTotalNetAmount.getText()).doubleValue() > 0) {
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                return;
            } else {
                if (CommonUtil.convertObjToDouble(txtTotalNetAmount.getText()).doubleValue() > 0) {
                    transactionSize = (transactionUI.getOutputTO()).size();
                    if (transactionSize != 1 && CommonUtil.convertObjToDouble(txtTotalNetAmount.getText()).doubleValue() > 0) {
                        ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction");
                        return;
                    } else {
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    }
                } else if (transactionUI.getOutputTO().size() > 0) {
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                }
            }
            if (transactionSize == 0 && CommonUtil.convertObjToDouble(txtTotalNetAmount.getText()).doubleValue() > 0) {
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
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed() {
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            setTableFinalList();
            updateOBFields();
        }
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                if (observable.getProxyReturnMap().containsKey("SINGLE_TANS_ID")) {
                    displayTransDetail(observable.getProxyReturnMap());
                }
            }
            btnCancelActionPerformed(null);
            btnCancel.setEnabled(true);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
        }
        btnCancelActionPerformed(null);
        //        lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        TrueTransactMain.populateBranches();
        TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
        observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
        setSelectedBranchID(ProxyParameters.BRANCH_ID);
    }

    private void displayTransDetail(HashMap proxyResultMap) {
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        if (yesNo == 0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = null;
            String toTransId = "";
            String fromTransId = "";
            String toCashId = "";
            String fromCashId = "";
            if (proxyResultMap.containsKey("TRANSFER") && proxyResultMap.containsKey("SINGLE_TANS_ID")) {
                if (proxyResultMap.containsKey("SINGLE_TANS_ID")) {
                    fromTransId = CommonUtil.convertObjToStr(proxyResultMap.get("SINGLE_TANS_ID"));
                }
//                if (proxyResultMap.containsKey("SINGLE_TANS_ID")) {
//                    toTransId = CommonUtil.convertObjToStr(proxyResultMap.get("SINGLE_TANS_ID"));
//                }
                paramMap = new HashMap();
                paramMap.put("SINGLE_TRANS_ID", fromTransId);
//                paramMap.put("ToTransId", toTransId);
                paramMap.put("TransDt", curr_dt);
                paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                System.out.println("#$#$$ paramMap : " + paramMap);
                ttIntgration.setParam(paramMap);
                ttIntgration.integrationForPrint("MDSMemberReceiptTransfer");
            }
            if (proxyResultMap.containsKey("CASH") && proxyResultMap.containsKey("SINGLE_TANS_ID")) {
                if (proxyResultMap.containsKey("SINGLE_TANS_ID")) {
                    fromCashId = CommonUtil.convertObjToStr(proxyResultMap.get("SINGLE_TANS_ID"));
                }
//                if (proxyResultMap.containsKey("CASH_TO_ID")) {
//                    toCashId = CommonUtil.convertObjToStr(proxyResultMap.get("CASH_TO_ID"));
//                }
                paramMap = new HashMap();
                paramMap.put("SINGLE_TRANS_ID", fromCashId);
//              paramMap.put("ToTransId", toCashId);
                paramMap.put("TransDt", curr_dt);
                paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                System.out.println("#$#$$ paramMap : " + paramMap);
                ttIntgration.setParam(paramMap);
                ttIntgration.integrationForPrint("MDSMemberReceiptCash");
            }
        }
    }

    private void setTableFinalList() {
        setTableFinalEditList();
        //observable.setFinalMap(finalMap);
        if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
            observable.doSplitMDSTransaction();
        }
    }

    private void setTableFinalEditList() {
        HashMap custMap = new HashMap();
        Iterator splitIterator;
        if (finalMap != null && finalMap.size() > 0) {
            String splitKey = "";
            splitIterator = finalMap.keySet().iterator();
            for (int i = 0; i < finalMap.size(); i++) {
                splitKey = (String) splitIterator.next();
                custMap = (HashMap) finalMap.get(splitKey);
                for (int j = 0; j < tblMemberReceipt.getRowCount(); j++) {
                    if (CommonUtil.convertObjToStr(tblMemberReceipt.getValueAt(j, 4)).equals(CommonUtil.convertObjToStr(custMap.get("CHITTAL_NO")))) {
                        if (CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(j, 7)) > 0) {
                            custMap.put("PENAL", tblMemberReceipt.getValueAt(j, 12));
                            double total = CommonUtil.convertObjToDouble(custMap.get("INST_AMT_PAYABLE"))
                                    + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(j, 12))
                                    + CommonUtil.convertObjToDouble(custMap.get("NOTICE_AMOUNT"))
                                    + CommonUtil.convertObjToDouble(custMap.get("ARBITRATION_AMOUNT"));
                            custMap.put("NET_AMOUNT", total);
                        }
                    }
                }

            }
            observable.setFinalMap(finalMap);
        }
    }

    private String periodLengthValidation(CTextField txtField, CComboBox comboField) {
        String message = "";
        String key = CommonUtil.convertObjToStr(((ComboBoxModel) comboField.getModel()).getKeyForSelected());
        if (!ClientUtil.validPeriodMaxLength(txtField, key)) {
            message = objMandatoryMRB.getString(txtField.getName());
        }
        return message;
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        ClientUtil.enableDisable(panMemberReceiptEntryDetails, false);
        ClientUtil.enableDisable(panInsideGeneralRemittance4, false);
        ClientUtil.clearAll(this);
        observable.resetForm();
        btnNew.setEnabled(true);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        btnMembershipNo.setEnabled(false);
        lblStatus.setText("               ");
        lblMemberName.setText("");
        lblMemberType.setText("");
        isFilled = false;
        bufferList.clear();
        getTableData();
        initTableData();
        finalMap = new HashMap();
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        setSizeTableData();
        bufferList.clear();
        getTableData();
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

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

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
        //        this.dispose();
    }//GEN-LAST:event_mitCloseActionPerformed

    private void txtTotInstAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotInstAmtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotInstAmtActionPerformed

    private void btnAddMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddMemberActionPerformed
        // TODO add your handling code here:
        if (txtMembershipNo.getText().length() > 0) {
            String memNo = txtMembershipNo.getText();
            String name = lblMemberName.getText();
            String type = lblMemberType.getText();
            if (tblMemberGrid.getRowCount() > 0) {
                for (int i = 0; i < tblMemberGrid.getRowCount(); i++) {
                    if (memNo.equals(tblMemberGrid.getValueAt(i, 0).toString())) {
                        displayAlert("MemberNo already entered");
                        txtMembershipNo.setText("");
                        lblMemberName.setText("");
                        lblMemberType.setText("");
                        return;
                    }
                }
            }
            HashMap amap = new HashMap();
            amap.put("MEM_NO", memNo);
            amap.put("MEM_NAME", name);
            amap.put("MEM_TYPE", type);
            bufferList.add(amap);
            getTableData();
            txtMembershipNo.setText("");
            lblMemberName.setText("");
            lblMemberType.setText("");
        } else {
            displayAlert("Enter Member No");
            return;
        }
    }//GEN-LAST:event_btnAddMemberActionPerformed

    private void btnDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisplayActionPerformed
        if (tblMemberGrid.getRowCount() > 0) {
            initTableData();
        } else {
            displayAlert("Add data to grid first");
            return;
        }
    }//GEN-LAST:event_btnDisplayActionPerformed

private void tblMemberGridMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMemberGridMouseClicked
// TODO add your handling code here:
    if (tblMemberGrid.getRowCount() > 0) {
        if (evt.getClickCount() == 2) {
            new CustomerDetailsScreenUI(CommonUtil.convertObjToStr(tblMemberGrid.getValueAt(tblMemberGrid.getSelectedRow(), 0))).show();
        }
    } else {
        displayAlert("Add Member to grid first");
        return;
    }
}//GEN-LAST:event_tblMemberGridMouseClicked

private void tblMemberReceiptMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_tblMemberReceiptMouseWheelMoved
// TODO add your handling code here:
}//GEN-LAST:event_tblMemberReceiptMouseWheelMoved

    private void tblMemberReceiptMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMemberReceiptMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblMemberReceiptMouseClicked

    /**
     * This method helps in popoualting the data from the data base
     *
     * @param currField Action the argument is passed according to the command
     * issued
     */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (currField.equals("Enquiry")) {
            viewMap.put(CommonConstants.MAP_NAME, "getMemberReceiptEnquiry");
        } else if (currField.equals("Delete")) {
            viewMap.put(CommonConstants.MAP_NAME, "getMemberReceiptDelete");
        } else if (currField.equalsIgnoreCase("MEMBER_LIST")) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectMemberList");
        }
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this, viewMap).show();
    }

    /**
     * This method helps in filling the data frm the data base to respective txt
     * fields
     *
     * @param obj param The selected data from the viewAll() is passed as a
     * param
     */
    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        int remitProduBranchRow = 0;
        isFilled = true;
        if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            fromNewAuthorizeUI = true;
            newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnReject.setEnabled(false);
        }
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnReject.setEnabled(false);
        }
        if (viewType != null) {
            if (viewType.equalsIgnoreCase("MEMBER_LIST")) {
                txtMembershipNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
                lblMemberName.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NAME")));
                lblMemberType.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_TYPE")));
                observable.setTxtMembershipNo(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
                observable.setLblMemberName(CommonUtil.convertObjToStr(hash.get("MEMBER_NAME")));
                //txtMembershipNo.setEnabled(false);
                // initTableData();
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                this.setButtonEnableDisable();
                txtMembershipNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
                observable.setTxtMembershipNo(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
                observable.setMemberTransId(CommonUtil.convertObjToStr(hash.get("RECEIPT_ID")));
                observable.getTransDetails(hash);
                ClientUtil.enableDisable(panInsideMemberReceiptDetails, false);
                btnMembershipNo.setEnabled(false);
                setMemberName();
                initTableDataEdit();
                calcTotalEdit();
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                this.setButtonEnableDisable();
                txtMembershipNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
                observable.setTxtMembershipNo(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
                observable.setMemberTransId(CommonUtil.convertObjToStr(hash.get("RECEIPT_ID")));
                observable.getTransDetails(hash);
                ClientUtil.enableDisable(panInsideMemberReceiptDetails, false);
                btnMembershipNo.setEnabled(false);
                setMemberName();
                initTableDataEdit();
                calcTotalEdit();
            }
        }
        if (viewType == AUTHORIZE) {
            btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
            btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
            btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            ClientUtil.enableDisable(this, false);
        }
        hash = null;
        btnCancel.setEnabled(true);
        //        observable.setStatus();
        //        lblStatus.setText(observable.getLblStatus());

        //__ To Save the data in the Internal Frame...
        setModified(true);
    }

    public void initTableDataEdit() {
        tblMemberReceipt.setModel(new javax.swing.table.DefaultTableModel(
                setTableDataEdit(),
                new String[]{
                    "MemberNo", "MemberName", "Scheme", "SchemeDesc", "ChittalNo", "Sub No", "InstDue", "NoOfInsPay", "TotPayable", "Bonus", "Discount", "InsAmtPayable",
                    "Interest", "Notice", "Arbitration", "NetAmt"
                }) {

            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 6) {
                    return false;
                }
                return canEdit[columnIndex];
            }
        });
        tblMemberReceipt.setCellSelectionEnabled(true);
        setSizeTableData();
        tblMemberReceipt.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblMemberReceiptPropertyChange(evt);
            }
        });
    }

    private Object[][] setTableDataEdit() {
        if (txtMembershipNo.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("MEMBER_RECEIPT_ID", observable.getMemberTransId());
            List lst = ClientUtil.executeQuery("getMemberReceiptDetails", whereMap);
            if (lst != null && lst.size() > 0) {
                Object totalList[][] = new Object[lst.size()][16];
                if (lst != null && lst.size() > 0) {
                    for (int i = 0; i < lst.size(); i++) {
                        whereMap = (HashMap) lst.get(i);
                        totalList[i][0] = CommonUtil.convertObjToStr(whereMap.get("MEMBER_NO"));
                        totalList[i][1] = CommonUtil.convertObjToStr(whereMap.get("CUST_NAME"));
                        totalList[i][2] = CommonUtil.convertObjToStr(whereMap.get("SCHEME_NAME"));
                        totalList[i][3] = CommonUtil.convertObjToStr(whereMap.get("SCHEME_DESC"));
                        totalList[i][4] = CommonUtil.convertObjToStr(whereMap.get("CHITTAL_NO"));
                        totalList[i][5] = CommonUtil.convertObjToStr(whereMap.get("SUB_NO"));
                        totalList[i][6] = CommonUtil.convertObjToStr(whereMap.get("PENDING_INST"));
                        totalList[i][7] = CommonUtil.convertObjToStr(whereMap.get("NO_OF_INST_PAY"));
                        totalList[i][8] = CommonUtil.convertObjToStr(whereMap.get("INST_AMT"));
                        totalList[i][9] = CommonUtil.convertObjToStr(whereMap.get("BONUS_AMT"));
                        totalList[i][10] = CommonUtil.convertObjToStr(whereMap.get("DISCOUNT_AMT"));
                        totalList[i][11] = CommonUtil.convertObjToStr(whereMap.get("TOTAL_PAYABLE"));
                        totalList[i][12] = CommonUtil.convertObjToStr(whereMap.get("PENAL_AMT"));
                        totalList[i][13] = CommonUtil.convertObjToStr(whereMap.get("NOTICE_AMT"));
                        totalList[i][14] = CommonUtil.convertObjToStr(whereMap.get("ARBITRATION_AMT"));
                        totalList[i][15] = CommonUtil.convertObjToStr(whereMap.get("NET_AMT"));
                    }
                    observable.setProductMapDetails(CommonUtil.convertObjToStr(whereMap.get("SCHEME_NAME")));
                }
                return totalList;
            } else {
                ClientUtil.displayAlert("No Data!!! ");
                observable.resetForm();
                txtMembershipNo.setText("");
                lblMemberName.setText("");
                lblMemberType.setText("");
            }
            return null;
        }
        return null;
    }

    private void calcTotalEdit() {
        double totNetAmt = 0;
        double totBonusAmt = 0;
        double totDiscountAmt = 0;
        double totInstAmtPayable = 0;
        double totIntAmt = 0;
        double totNoticeAmt = 0;
        double totArbitAmt = 0;
        if (tblMemberReceipt.getRowCount() > 0) {
            for (int i = 0; i < tblMemberReceipt.getRowCount(); i++) {
                totBonusAmt = totBonusAmt + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(i, 9).toString()).doubleValue();
                totDiscountAmt = totDiscountAmt + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(i, 10).toString()).doubleValue();
                totInstAmtPayable = totInstAmtPayable + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(i, 11).toString()).doubleValue();
                totIntAmt = totIntAmt + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(i, 12).toString()).doubleValue();
                totNoticeAmt = totNoticeAmt + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(i, 13).toString()).doubleValue();
                totArbitAmt = totArbitAmt + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(i, 14).toString()).doubleValue();
                totNetAmt = totNetAmt + CommonUtil.convertObjToDouble(tblMemberReceipt.getValueAt(i, 15).toString()).doubleValue();
            }
        }
        txtTotalNetAmount.setText(String.valueOf(totNetAmt));
        txtTotInstAmt.setText(String.valueOf(totInstAmtPayable));
        txtTotBonusAmt.setText(String.valueOf(totBonusAmt));
        txtTotDiscountAmt.setText(String.valueOf(totDiscountAmt));
        txtTotInterest.setText(String.valueOf(totIntAmt));
        txtNoticeAmt.setText(String.valueOf(totNoticeAmt));
        txtAribitrationAmt.setText(String.valueOf(totArbitAmt));
    }

    public void setMemberName() {
        HashMap whereMap = new HashMap();
        whereMap.put("MEMBER_NO", txtMembershipNo.getText());
        List lst = ClientUtil.executeQuery("getMemberNameAndType", whereMap);
        if (lst != null && lst.size() > 0) {
            whereMap = (HashMap) lst.get(0);
            lblMemberName.setText(CommonUtil.convertObjToStr(whereMap.get("MEMBER_NAME")));
            lblMemberType.setText(CommonUtil.convertObjToStr(whereMap.get("MEMBER_TYPE")));
        }
    }

    private void displayAlert(String message) {
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    /**
     * To Enable or Disable New, Edit, Save and Cancel Button
     */
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAddMember;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDisplay;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnMembershipNo;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CCheckBox chkChangedMember;
    private com.see.truetransact.uicomponent.CCheckBox chkMunnalMember;
    private com.see.truetransact.uicomponent.CCheckBox chkThalayalMember;
    private com.see.truetransact.uicomponent.CLabel lblAribitrationAmt;
    private com.see.truetransact.uicomponent.CLabel lblChangedDt;
    private com.see.truetransact.uicomponent.CLabel lblChangedInstNo;
    private com.see.truetransact.uicomponent.CLabel lblChangedMember;
    private com.see.truetransact.uicomponent.CLabel lblEarlierMemName;
    private com.see.truetransact.uicomponent.CLabel lblEarlierMemNo;
    private com.see.truetransact.uicomponent.CLabel lblMemberName;
    private com.see.truetransact.uicomponent.CLabel lblMemberType;
    private com.see.truetransact.uicomponent.CLabel lblMembershipNo;
    private com.see.truetransact.uicomponent.CLabel lblMembershipType;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblMunnalMember;
    private com.see.truetransact.uicomponent.CLabel lblNoticeAmt;
    private com.see.truetransact.uicomponent.CLabel lblPaidAmount;
    private com.see.truetransact.uicomponent.CLabel lblPaidDt;
    private com.see.truetransact.uicomponent.CLabel lblPaidInst;
    private com.see.truetransact.uicomponent.CLabel lblSecurityDetails;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace40;
    private com.see.truetransact.uicomponent.CLabel lblSpace41;
    private com.see.truetransact.uicomponent.CLabel lblSpace42;
    private com.see.truetransact.uicomponent.CLabel lblSpace43;
    private com.see.truetransact.uicomponent.CLabel lblSpace44;
    private com.see.truetransact.uicomponent.CLabel lblSpace45;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblThalayalMember;
    private com.see.truetransact.uicomponent.CLabel lblTotBonusAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotDiscountAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotInstAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotInterest;
    private com.see.truetransact.uicomponent.CLabel lblTotalPayment;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitException;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitReject;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAmountDetails;
    private com.see.truetransact.uicomponent.CPanel panAmountDetails1;
    private com.see.truetransact.uicomponent.CPanel panDiscountPeriodDetails2;
    private com.see.truetransact.uicomponent.CPanel panDiscountPeriodDetails4;
    private com.see.truetransact.uicomponent.CPanel panGeneralRemittance1;
    private com.see.truetransact.uicomponent.CPanel panInsideGeneralRemittance4;
    private com.see.truetransact.uicomponent.CPanel panInsideMemberReceiptDetails;
    private com.see.truetransact.uicomponent.CPanel panMemberDetails;
    private com.see.truetransact.uicomponent.CPanel panMemberReceiptEntryDetails;
    private com.see.truetransact.uicomponent.CPanel panMemberReceiptTrans;
    private com.see.truetransact.uicomponent.CPanel panMembershipNo;
    private com.see.truetransact.uicomponent.CPanel panOtherDetails;
    private com.see.truetransact.uicomponent.CPanel panPaidDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTable2_SD2;
    private com.see.truetransact.uicomponent.CPanel panTable2_SD3;
    private com.see.truetransact.uicomponent.CPanel panTableMemberReceipt;
    private com.see.truetransact.uicomponent.CPanel panTotalNetAmount;
    private com.see.truetransact.uicomponent.CButtonGroup rdgEFTProductGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIsLapsedGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPayableBranchGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrintServicesGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSeriesGR;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CScrollPane srpTable2_SD2;
    private com.see.truetransact.uicomponent.CScrollPane srpTable2_SD3;
    private com.see.truetransact.uicomponent.CScrollPane srpTableMemberReceipt;
    private com.see.truetransact.uicomponent.CTabbedPane tabRemittanceProduct;
    private com.see.truetransact.uicomponent.CTable tblMemberGrid;
    private com.see.truetransact.uicomponent.CTable tblMemberReceipt;
    private com.see.truetransact.uicomponent.CTable tblSanctionDetails4;
    private com.see.truetransact.uicomponent.CTable tblSanctionDetails5;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CDateField tdtChangedDt;
    private com.see.truetransact.uicomponent.CDateField tdtPaidDt;
    private com.see.truetransact.uicomponent.CTextField txtAribitrationAmt;
    private com.see.truetransact.uicomponent.CTextField txtChangedInstNo;
    private com.see.truetransact.uicomponent.CTextField txtEarlierMemName;
    private com.see.truetransact.uicomponent.CTextField txtEarlierMemNo;
    private com.see.truetransact.uicomponent.CTextField txtMembershipNo;
    private com.see.truetransact.uicomponent.CTextField txtNoticeAmt;
    private com.see.truetransact.uicomponent.CTextField txtPaidAmount;
    private com.see.truetransact.uicomponent.CTextField txtPaidInst;
    private com.see.truetransact.uicomponent.CTextField txtTotBonusAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotDiscountAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotInstAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotInterest;
    private com.see.truetransact.uicomponent.CTextField txtTotalNetAmount;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] arg) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        MDSMemberReceiptEntryUI gui = new MDSMemberReceiptEntryUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }

    /**
     * Getter for property rdoPrizedMember_Yes.
     *
     * @return Value of property rdoPrizedMember_Yes.
     */
    public boolean getRdoPrizedMember_Yes() {
        return rdoPrizedMember_Yes;
    }

    /**
     * Setter for property rdoPrizedMember_Yes.
     *
     * @param rdoPrizedMember_Yes New value of property rdoPrizedMember_Yes.
     */
    public void setRdoPrizedMember_Yes(boolean rdoPrizedMember_Yes) {
        this.rdoPrizedMember_Yes = rdoPrizedMember_Yes;
    }

    /**
     * Getter for property rdoPrizedMember_No.
     *
     * @return Value of property rdoPrizedMember_No.
     */
    public boolean getRdoPrizedMember_No() {
        return rdoPrizedMember_No;
    }

    /**
     * Setter for property rdoPrizedMember_No.
     *
     * @param rdoPrizedMember_No New value of property rdoPrizedMember_No.
     */
    public void setRdoPrizedMember_No(boolean rdoPrizedMember_No) {
        this.rdoPrizedMember_No = rdoPrizedMember_No;
    }
}