/*
 * DepositMultiClosingUI.java
 *
 * Created on Jan 1, 2015, 11:03 AM
 */
package com.see.truetransact.ui.deposit.multipleclosing;

import com.see.iie.tools.useradmin.util.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.common.viewall.TextUI;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import java.awt.Color;
import java.awt.Component;
import java.util.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.jboss.mq.il.ha.HAServerIL;
import javax.swing.*;

/**
 * @author Shihad
 */
public class DepositMultiClosingUI extends CInternalFrame implements Observer {

    /**
     * Vairable Declarations
     */
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    DepositMultiClosingOB observable = null;
    private boolean selectMode = false;
    private Date currDate = null;
    private HashMap returnMap = null;
    ArrayList colorList = new ArrayList();
    private Boolean flagMaturity;
    List finalList = new ArrayList();
    List calFreqAccountList = new ArrayList();
    boolean fromAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    private int rejectFlag = 0;
    private String AUTHORIZE = "AUTHORIZE";
    double totTransAmt=0.0;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    ArrayList freezDepositLst=new ArrayList(); 

    /**
     * Creates new form TokenConfigUI
     */
    public DepositMultiClosingUI() {
        returnMap = null;
        flagMaturity = new Boolean(true);
        currDate = ClientUtil.getCurrentDate();
        initForm();
    }

    /**
     * Method which is used to initialize the form TokenConfig
     */
    private void initForm() {
        initComponents();
        observable = new DepositMultiClosingOB();
        initTableData();
        txtProductID.setAllowAll(true);
        btnCalculate.setEnabled(false);
        btnProcess.setEnabled(false);
        ClientUtil.enableDisable(panDepositInterestApplication, false);
        btnProductID.setEnabled(true);
        btnCalculate.setEnabled(false);
        txtProductID.setEnabled(true);
        manualEntryEnable(false);
        cboClosinglTransProdType.setModel(observable.getCbmProdType());
        cboClosingInterestTransMode.setModel(observable.getCbmClosingTransMode());
//        cboCategory.setModel(observable.getCbmCategory());
        txtProductID.setEnabled(false);
        btnProductID.setEnabled(false);
        btnClose.setEnabled(false);
        btnClear.setEnabled(false);
        btnNew.setEnabled(true);
        btnNew.setVisible(false);
        btnNewActionPerformed(null);
        btnAuthorize.setEnabled(false);
        rdoPenaltyRateApplicableYes.setSelected(true);
        rdoPenaltyRateApplicableNo.setSelected(false);
        rdoPenaltyRateApplicableYes.setEnabled(true);
        rdoPenaltyRateApplicableNo.setEnabled(true);
    }

    private void initTableData() {
        tblDepositMultiClosing.setModel(observable.getTblDepositInterestApplication());
    }

    private void manualEntryEnable(boolean flag) {
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
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
    }

    /**
     * Used to set Maximum possible lenghts for TextFields
     */
    private void setMaxLengths() {
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

    /*
     * Method used to showPopup ViewAll by Executing a Query
     */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        HashMap hash = new HashMap();
        if (flagMaturity) {
            hash.put("MATURITY", flagMaturity);
        }
        if (currField.equalsIgnoreCase("PROD_DETAILS")) {
            List prodID = new ArrayList();
            viewMap.put(CommonConstants.MAP_NAME, "getFixedDepositProductsMultiple");
        } else if (currField.equalsIgnoreCase("FROM") || currField.equalsIgnoreCase("TO") || currField.equalsIgnoreCase("DEPOSIT_NO_MANUAL")) {
            hash.put("CURR_DATE", getProperDate(currDate));
            hash.put("PRODID", txtProductID.getText());
            hash.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_WHERE, hash);
            viewMap.put(CommonConstants.MAP_NAME, "getRegularDepositAccounts");
        } else if (currField.equalsIgnoreCase("OPERATIVE_NO")) {
            String str = CommonUtil.convertObjToStr(observable.getCbmOAProductID().getKeyForSelected());
            hash.put("PROD_ID", str);
            hash.put("SELECTED_BRANCH", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, hash);
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList" + ((ComboBoxModel) cboClosinglTransProdType.getModel()).getKeyForSelected().toString());
        } else if (currField == "DEPOSIT_CLOSE_TRANS_ACC_NO") {
            String prodType = ((ComboBoxModel) cboClosinglTransProdType.getModel()).getKeyForSelected().toString();
            if (!prodType.equals("GL")) {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                        + ((ComboBoxModel) cboClosinglTransProdType.getModel()).getKeyForSelected().toString());
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
            }
            HashMap whereMap = new HashMap();
            whereMap.put("PROD_ID", ((ComboBoxModel) cboClosinglTransProdId.getModel()).getKeyForSelected());
//            if (whereMap.get("SELECTED_BRANCH") == null) {
//                whereMap.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
//            } else {
//                whereMap.put("SELECTED_BRANCH", getSelectedBranchID());
//            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
        new ViewAll(this, viewMap).show();
    }

    /*
     * Fills up the HashMap with data when user selects the row in ViewAll
     * screen
     */
    public void fillData(Object map) {
        try {
            HashMap hash = (HashMap) map;
            System.out.println("#@@# Hash :" + hash);
            if (viewType != null) {
                if (viewType.equalsIgnoreCase("PROD_DETAILS")) {
                    txtProductID.setText(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                    btnProductID.setEnabled(true);
                }
            }
             if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
                fromNewAuthorizeUI = true;
                newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
                hash.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(com.see.truetransact.clientutil.ClientConstants.ACTIONTYPE_AUTHORIZE);
                rejectFlag = 1;

            }
             if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
                System.out.println("inside hr");
                fromAuthorizeUI = true;
                authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
                hash.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(com.see.truetransact.clientutil.ClientConstants.ACTIONTYPE_AUTHORIZE);
                rejectFlag = 1;

            }
//            if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
//                System.out.println("inside hr");
//                fromAuthorizeUI = true;
//                authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
//                hash.remove("PARENT");
//                viewType = AUTHORIZE;
//                observable.setActionType(com.see.truetransact.clientutil.ClientConstants.ACTIONTYPE_AUTHORIZE);
//                rejectFlag = 1;
//
//            }
            if (viewType.equals("Customer")) {
                //__ To reset the data for the Previous selected Customer..
                final String CUSTID = CommonUtil.convertObjToStr(hash.get("CUST_ID"));
                txtCustID.setText(CUSTID);
                //__ To set the Name of the Customer...
                final String CUSTNAME = CommonUtil.convertObjToStr(hash.get("NAME"));
                lblCustName.setText(CUSTNAME);
                txtFromAccount.setText("");
                txtToAccount.setText("");
                rdoPenaltyRateApplicableYes.setSelected(true);
                rdoPenaltyRateApplicableNo.setSelected(false);
                rdoPenaltyRateApplicableYes.setEnabled(false);
                rdoPenaltyRateApplicableNo.setEnabled(false);
            }
            if (viewType.equals("CUSTOMER ID")) {
                CustInfoDisplay(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
            }
            if (viewType.equals("FROM")) {
                txtFromAccount.setText(CommonUtil.convertObjToStr(hash.get("DEPOSIT_ACT_NUM")));
            }

            if (viewType.equals("TO")) {
                txtToAccount.setText(CommonUtil.convertObjToStr(hash.get("DEPOSIT_ACT_NUM")));
            }
            if (viewType.equals("DEPOSIT_CLOSE_TRANS_ACC_NO")) {
                String prodType = ((ComboBoxModel) cboClosinglTransProdType.getModel()).getKeyForSelected().toString();
                if (prodType != null && !prodType.equals("GL")) {
                    if (prodType.equals("TD")) {
                        hash.put("ACCOUNTNO", hash.get("ACCOUNTNO") + "_1");
                    }
                    txtClosingCustomerIdCr.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                } else {
                    txtClosingCustomerIdCr.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
                }
                if(hash.containsKey("BRANCH_ID")&&hash.get("BRANCH_ID")!=null){
                observable.setCreditBranch(CommonUtil.convertObjToStr(hash.get("BRANCH_ID")));
                }
            }
            if (viewType.equals(AUTHORIZE)) {
                selectMode = true;
                ArrayList rowList = new ArrayList();
                ArrayList tableList = new ArrayList();
                List tempList = ClientUtil.executeQuery("getDepositMultiClosingTemp", hash);
                HashMap tempMap = null;
                for (int i = 0; i < tempList.size(); i++) {
                    tempMap = new HashMap();
                    tempMap = (HashMap) tempList.get(i);
                    System.out.println("temp list hr" + tempMap);
                    rowList = new ArrayList();
                    rowList.add(new Boolean(false));
                    rowList.add(tempMap.get("DEPOSIT_NO"));
                    rowList.add(tempMap.get("CUST_ID"));
                    rowList.add(tempMap.get("DEPOSIT_AMT"));
                    rowList.add(tempMap.get("DEPOSIT_DT"));
                    Date matDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tempMap.get("MATURITY_DT")));
                    if ((DateUtil.dateDiff(CommonUtil.getProperDate(currDate, matDt), currDate)) > 0) {
                        rowList.add("normal");
                    } else {
                        rowList.add("premature");

                    }
                    rowList.add(CommonUtil.convertObjToStr(tempMap.get("MATURITY_DT")));
                    rowList.add(tempMap.get("RATE_OF_INT"));
                    rowList.add(tempMap.get("PAID_INTEREST"));
                    rowList.add(tempMap.get("DR_INTEREST"));
                    if (CommonUtil.convertObjToDouble(tempMap.get("TOTAL_AMT")) > CommonUtil.convertObjToDouble(tempMap.get("TOTAL_BALANCE"))) {
                        rowList.add(tempMap.get("PAY_AMT"));
                        rowList.add("0.0");
                    } else {
                        rowList.add("0.0");
                        rowList.add(tempMap.get("PAY_AMT"));
                    }
                    rowList.add(tempMap.get("TOTAL_AMT"));
                    // Code added by nithya [ To resolve bug reported in testing ] on 13-04-2016 [ 4190 ]
                    Date depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tempMap.get("DEPOSIT_DT")));
                    if(depDt != null && !depDt.equals("")){
                        if(DateUtil.dateDiff(depDt,currDate)>= 0){                           
                           String monthDiff =  CommonUtil.convertObjToStr(DateUtil.dateDiff(depDt,currDate));
                           rowList.add(monthDiff);
                        }else{
                           rowList.add("Prematured");  
                        }
                    }  
                    // End
                    rowList.add(tempMap.get("TDS_AMOUNT"));
                    observable.setTxtProductID(txtProductID.getText());
                    txtCustID.setText(CommonUtil.convertObjToStr(tempMap.get("CUST_ID_UP")));
                    txtProductID.setText(CommonUtil.convertObjToStr(tempMap.get("PROD_ID")));
                    txtFromAccount.setText(CommonUtil.convertObjToStr(tempMap.get("FROM_ACC")));
                    txtToAccount.setText(CommonUtil.convertObjToStr(tempMap.get("TO_ACC")));
                    cboClosingInterestTransMode.setSelectedItem(observable.getCbmClosingTransMode().getDataForKey(tempMap.get("CLOSING_MODE")));
                    if (CommonUtil.convertObjToStr(tempMap.get("CLOSING_MODE")).equals("TRANSFER")) {
                        cboClosinglTransProdType.setSelectedItem(observable.getCbmProdType().getDataForKey(tempMap.get("PROD_TYPE")));
                        cboClosinglTransProdId.setSelectedItem(observable.getCbmRenewalInterestTransProdId().getDataForKey(tempMap.get("PROD_ID_TRANS")));
                        txtClosingCustomerIdCr.setText(CommonUtil.convertObjToStr(tempMap.get("ACT_NUM")));
                    }
                    tableList.add(rowList);
                }
				//added by rishad
                ArrayList tableTitle = new ArrayList();
                tableTitle = observable.tableTitle;
                tableTitle.remove(14);
                observable.setTblDepositInterestApplication(new EnhancedTableModel((ArrayList) tableList, tableTitle));
                tblDepositMultiClosing.setModel(observable.getTblDepositInterestApplication());
                chkSelectAll.setEnabled(true);
                HashMap dataMap = new HashMap();
                if (txtCustID.getText() != null && (!txtCustID.getText().equals(""))) {
                    dataMap.put("CUST_ID", CommonUtil.convertObjToStr(txtCustID.getText()));
                }

                if (txtFromAccount.getText() != null && (!txtFromAccount.getText().equals(""))) {
                    dataMap.put("ACT_FROM", CommonUtil.convertObjToStr(txtFromAccount.getText()));
                }
                if (txtToAccount.getText() != null && (!txtToAccount.getText().equals(""))) {
                    dataMap.put("ACT_TO", CommonUtil.convertObjToStr(txtToAccount.getText()));
                }
                Date tempDt = (Date) currDate.clone();
                dataMap.put("CURR_DT", tempDt);
                dataMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                dataMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                String installType = CommonUtil.convertObjToStr(cboClosingInterestTransMode.getSelectedItem());
                System.out.println("install typeasd a" + installType + " as " + cboClosingInterestTransMode.getSelectedItem());
                dataMap.put("TRANS_MODE", installType);
                if (installType.equals("TRANSFER")) {
                    dataMap.put("PROD_TYPE", CommonUtil.convertObjToStr(cboClosinglTransProdType.getSelectedItem()));
                    dataMap.put("PROD_ID", CommonUtil.convertObjToStr(cboClosinglTransProdId.getSelectedItem()));
                    dataMap.put("AC_NO", CommonUtil.convertObjToStr(txtClosingCustomerIdCr.getText()));
                }
                dataMap.put("AUTHORIZE", "AUTHORIZE");
                dataMap.put("MULTI_DEP_CLOSE_ID", hash.get("MULTI_DEP_CLOSE_ID"));
                observable.insertTableData(dataMap);
                btnAuthorize.setEnabled(true);
                btnProcess.setEnabled(false);
                txtCustID.setEnabled(false);
                txtProductID.setEnabled(false);
                txtFromAccount.setEnabled(false);
                txtToAccount.setEnabled(false);
                cboClosingInterestTransMode.setEnabled(false);
                cboClosinglTransProdType.setEnabled(false);
                cboClosinglTransProdId.setEnabled(false);
                txtClosingCustomerIdCr.setEnabled(false);
                btnCalculate.setEnabled(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CustInfoDisplay(String custId) {

        HashMap custMap = new HashMap();
        custMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        custMap.put("CUST_ID", custId);
        List lst = ClientUtil.executeQuery("getDepositCustomerName", custMap);
        if (lst != null && lst.size() > 0) {
            custMap = (HashMap) lst.get(0);
            lblCustName.setText(CommonUtil.convertObjToStr(custMap.get("FNAME")));
            lst.clear();
            custMap.clear();
        } else {
            ClientUtil.showAlertWindow("Invalid Customer or This Customer not having Deposit A/cs...");
        }
        lst = null;
        custMap = null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panDepositInterestApplication = new com.see.truetransact.uicomponent.CPanel();
        panProductDetails = new com.see.truetransact.uicomponent.CPanel();
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        panFromTonum = new com.see.truetransact.uicomponent.CPanel();
        lblCustID = new com.see.truetransact.uicomponent.CLabel();
        panCustID = new com.see.truetransact.uicomponent.CPanel();
        lblFromAccount = new com.see.truetransact.uicomponent.CLabel();
        lblToAccount = new com.see.truetransact.uicomponent.CLabel();
        panToAccount2 = new com.see.truetransact.uicomponent.CPanel();
        txtCustID = new com.see.truetransact.uicomponent.CTextField();
        btnCustID = new com.see.truetransact.uicomponent.CButton();
        txtFromAccount = new com.see.truetransact.uicomponent.CTextField();
        btnFromAccount = new com.see.truetransact.uicomponent.CButton();
        txtToAccount = new com.see.truetransact.uicomponent.CTextField();
        btnToAccount = new com.see.truetransact.uicomponent.CButton();
        panProductIdMain = new com.see.truetransact.uicomponent.CPanel();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        panProductID = new com.see.truetransact.uicomponent.CPanel();
        txtProductID = new com.see.truetransact.uicomponent.CTextField();
        btnProductID = new com.see.truetransact.uicomponent.CButton();
        panProcessButton = new com.see.truetransact.uicomponent.CPanel();
        btnCalculate = new com.see.truetransact.uicomponent.CButton();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        cboClosingInterestTransMode = new com.see.truetransact.uicomponent.CComboBox();
        cboClosinglTransProdType = new com.see.truetransact.uicomponent.CComboBox();
        cboClosinglTransProdId = new com.see.truetransact.uicomponent.CComboBox();
        btnDepositClostCustomerIdFileOpenCr = new com.see.truetransact.uicomponent.CButton();
        txtClosingCustomerIdCr = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalInterestTransAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalInterestTransProdId = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalInterestTransProdType = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalWithdrawingIntAmt = new com.see.truetransact.uicomponent.CLabel();
        lblPenaltyRateApplicable = new com.see.truetransact.uicomponent.CLabel();
        rdoPenaltyRateApplicableYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPenaltyRateApplicableNo = new com.see.truetransact.uicomponent.CRadioButton();
        panProductTableData = new com.see.truetransact.uicomponent.CPanel();
        srpDepositInterestApplication = new com.see.truetransact.uicomponent.CScrollPane();
        tblDepositMultiClosing = new com.see.truetransact.uicomponent.CTable();
        panSelectAll = new com.see.truetransact.uicomponent.CPanel();
        lblSelectAll = new com.see.truetransact.uicomponent.CLabel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        panProcess = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblTotAccounts = new javax.swing.JLabel();
        lblAccountSelected = new javax.swing.JLabel();
        lblTotNoAccountsVal = new javax.swing.JLabel();
        lblAccountSelectedVal = new javax.swing.JLabel();
        lblTotInt = new javax.swing.JLabel();
        lblTotIntVal = new javax.swing.JLabel();
        lblTotPrinc = new javax.swing.JLabel();
        lblTotPrincVal = new javax.swing.JLabel();
        lblTotIntRecover = new javax.swing.JLabel();
        lblTotIntRecoverVal = new javax.swing.JLabel();
        btnAuthorize = new javax.swing.JButton();
        btnReject = new javax.swing.JButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(980, 640));
        setMinimumSize(new java.awt.Dimension(980, 640));
        setPreferredSize(new java.awt.Dimension(980, 640));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        panDepositInterestApplication.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panDepositInterestApplication.setMaximumSize(new java.awt.Dimension(950, 450));
        panDepositInterestApplication.setMinimumSize(new java.awt.Dimension(950, 450));
        panDepositInterestApplication.setPreferredSize(new java.awt.Dimension(950, 450));
        panDepositInterestApplication.setRequestFocusEnabled(false);
        panDepositInterestApplication.setLayout(null);

        panProductDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Deposit Details"));
        panProductDetails.setMaximumSize(new java.awt.Dimension(940, 140));
        panProductDetails.setMinimumSize(new java.awt.Dimension(940, 140));
        panProductDetails.setPreferredSize(new java.awt.Dimension(940, 140));
        panProductDetails.setLayout(null);

        lblCustName.setForeground(new java.awt.Color(0, 51, 204));
        lblCustName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        panProductDetails.add(lblCustName);
        lblCustName.setBounds(0, 0, 0, 0);

        panFromTonum.setMaximumSize(new java.awt.Dimension(550, 110));
        panFromTonum.setMinimumSize(new java.awt.Dimension(550, 110));
        panFromTonum.setPreferredSize(new java.awt.Dimension(550, 110));
        panFromTonum.setLayout(null);

        lblCustID.setText("Member/Customer ID");
        panFromTonum.add(lblCustID);
        lblCustID.setBounds(10, 10, 123, 18);

        panCustID.setLayout(new java.awt.GridBagLayout());
        panFromTonum.add(panCustID);
        panCustID.setBounds(130, 50, 123, 21);

        lblFromAccount.setText("FromAccount");
        panFromTonum.add(lblFromAccount);
        lblFromAccount.setBounds(480, 10, 80, 18);

        lblToAccount.setText("ToAccount");
        panFromTonum.add(lblToAccount);
        lblToAccount.setBounds(700, 10, 70, 18);

        panToAccount2.setLayout(new java.awt.GridBagLayout());
        panFromTonum.add(panToAccount2);
        panToAccount2.setBounds(0, 0, 0, 0);

        txtCustID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustIDFocusLost(evt);
            }
        });
        panFromTonum.add(txtCustID);
        txtCustID.setBounds(140, 10, 100, 21);

        btnCustID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustID.setEnabled(false);
        btnCustID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCustID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustIDActionPerformed(evt);
            }
        });
        panFromTonum.add(btnCustID);
        btnCustID.setBounds(250, 10, 21, 21);

        txtFromAccount.setAllowAll(true);
        txtFromAccount.setMinimumSize(new java.awt.Dimension(120, 21));
        txtFromAccount.setPreferredSize(new java.awt.Dimension(120, 21));
        panFromTonum.add(txtFromAccount);
        txtFromAccount.setBounds(560, 10, 120, 21);

        btnFromAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromAccount.setEnabled(false);
        btnFromAccount.setMinimumSize(new java.awt.Dimension(21, 21));
        btnFromAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFromAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromAccountActionPerformed(evt);
            }
        });
        panFromTonum.add(btnFromAccount);
        btnFromAccount.setBounds(680, 10, 21, 21);

        txtToAccount.setAllowAll(true);
        txtToAccount.setMinimumSize(new java.awt.Dimension(120, 21));
        txtToAccount.setPreferredSize(new java.awt.Dimension(120, 21));
        panFromTonum.add(txtToAccount);
        txtToAccount.setBounds(770, 10, 130, 21);

        btnToAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToAccount.setEnabled(false);
        btnToAccount.setMinimumSize(new java.awt.Dimension(21, 21));
        btnToAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnToAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToAccountActionPerformed(evt);
            }
        });
        panFromTonum.add(btnToAccount);
        btnToAccount.setBounds(901, 10, 20, 21);

        panProductIdMain.setMaximumSize(new java.awt.Dimension(230, 100));
        panProductIdMain.setMinimumSize(new java.awt.Dimension(230, 100));
        panProductIdMain.setPreferredSize(new java.awt.Dimension(230, 100));
        panProductIdMain.setLayout(new java.awt.GridBagLayout());

        lblProductID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panProductIdMain.add(lblProductID, gridBagConstraints);

        panProductID.setLayout(new java.awt.GridBagLayout());

        txtProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProductID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProductIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panProductID.add(txtProductID, gridBagConstraints);

        btnProductID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnProductID.setEnabled(false);
        btnProductID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnProductID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(btnProductID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 6);
        panProductIdMain.add(panProductID, gridBagConstraints);

        panFromTonum.add(panProductIdMain);
        panProductIdMain.setBounds(280, 0, 200, 40);

        panProductDetails.add(panFromTonum);
        panFromTonum.setBounds(10, 20, 920, 40);

        panProcessButton.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panProcessButton.setMinimumSize(new java.awt.Dimension(140, 80));
        panProcessButton.setPreferredSize(new java.awt.Dimension(140, 80));
        panProcessButton.setLayout(null);

        btnCalculate.setText("Display");
        btnCalculate.setMaximumSize(new java.awt.Dimension(89, 21));
        btnCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateActionPerformed(evt);
            }
        });
        panProcessButton.add(btnCalculate);
        btnCalculate.setBounds(90, 60, 75, 27);

        panProductDetails.add(panProcessButton);
        panProcessButton.setBounds(640, 70, 290, 100);

        cPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cPanel1.setLayout(null);

        cboClosingInterestTransMode.setMinimumSize(new java.awt.Dimension(100, 21));
        cboClosingInterestTransMode.setPopupWidth(100);
        cboClosingInterestTransMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboClosingInterestTransModeActionPerformed(evt);
            }
        });
        cPanel1.add(cboClosingInterestTransMode);
        cboClosingInterestTransMode.setBounds(150, 10, 100, 21);

        cboClosinglTransProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboClosinglTransProdType.setPopupWidth(200);
        cboClosinglTransProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboClosinglTransProdTypeActionPerformed(evt);
            }
        });
        cPanel1.add(cboClosinglTransProdType);
        cboClosinglTransProdType.setBounds(90, 40, 100, 21);

        cboClosinglTransProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboClosinglTransProdId.setPopupWidth(200);
        cboClosinglTransProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboClosinglTransProdIdActionPerformed(evt);
            }
        });
        cPanel1.add(cboClosinglTransProdId);
        cboClosinglTransProdId.setBounds(260, 40, 100, 21);

        btnDepositClostCustomerIdFileOpenCr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDepositClostCustomerIdFileOpenCr.setMaximumSize(new java.awt.Dimension(21, 21));
        btnDepositClostCustomerIdFileOpenCr.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDepositClostCustomerIdFileOpenCr.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDepositClostCustomerIdFileOpenCr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositClostCustomerIdFileOpenCrActionPerformed(evt);
            }
        });
        cPanel1.add(btnDepositClostCustomerIdFileOpenCr);
        btnDepositClostCustomerIdFileOpenCr.setBounds(590, 40, 21, 21);

        txtClosingCustomerIdCr.setEditable(false);
        txtClosingCustomerIdCr.setMinimumSize(new java.awt.Dimension(100, 21));
        cPanel1.add(txtClosingCustomerIdCr);
        txtClosingCustomerIdCr.setBounds(450, 40, 130, 21);

        lblRenewalInterestTransAccNo.setText("Account No");
        cPanel1.add(lblRenewalInterestTransAccNo);
        lblRenewalInterestTransAccNo.setBounds(370, 40, 68, 18);

        lblRenewalInterestTransProdId.setText("Product Id");
        cPanel1.add(lblRenewalInterestTransProdId);
        lblRenewalInterestTransProdId.setBounds(190, 40, 59, 18);

        lblRenewalInterestTransProdType.setText("Product Type");
        lblRenewalInterestTransProdType.setFont(new java.awt.Font("MS Sans Serif", 0, 12)); // NOI18N
        cPanel1.add(lblRenewalInterestTransProdType);
        lblRenewalInterestTransProdType.setBounds(10, 40, 71, 16);

        lblRenewalWithdrawingIntAmt.setText("Deposit Closing Mode");
        lblRenewalWithdrawingIntAmt.setMaximumSize(new java.awt.Dimension(125, 16));
        cPanel1.add(lblRenewalWithdrawingIntAmt);
        lblRenewalWithdrawingIntAmt.setBounds(10, 10, 127, 18);

        lblPenaltyRateApplicable.setText("Penalty Rate Applicable");
        cPanel1.add(lblPenaltyRateApplicable);
        lblPenaltyRateApplicable.setBounds(10, 70, 150, 18);

        rdoPenaltyRateApplicableYes.setText("Yes");
        rdoPenaltyRateApplicableYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPenaltyRateApplicableYesActionPerformed(evt);
            }
        });
        cPanel1.add(rdoPenaltyRateApplicableYes);
        rdoPenaltyRateApplicableYes.setBounds(190, 70, 60, 27);

        rdoPenaltyRateApplicableNo.setText("No");
        rdoPenaltyRateApplicableNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPenaltyRateApplicableNoActionPerformed(evt);
            }
        });
        cPanel1.add(rdoPenaltyRateApplicableNo);
        rdoPenaltyRateApplicableNo.setBounds(270, 70, 60, 27);

        panProductDetails.add(cPanel1);
        cPanel1.setBounds(10, 70, 620, 100);

        panDepositInterestApplication.add(panProductDetails);
        panProductDetails.setBounds(10, 20, 980, 180);
        panProductDetails.getAccessibleContext().setAccessibleName("");

        panProductTableData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panProductTableData.setMinimumSize(new java.awt.Dimension(830, 360));
        panProductTableData.setPreferredSize(new java.awt.Dimension(830, 360));
        panProductTableData.setLayout(null);

        srpDepositInterestApplication.setMinimumSize(new java.awt.Dimension(810, 335));
        srpDepositInterestApplication.setPreferredSize(new java.awt.Dimension(810, 335));
        srpDepositInterestApplication.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                srpDepositInterestApplicationMouseClicked(evt);
            }
        });

        tblDepositMultiClosing.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Cust ID", "Account No", "Name", "Dep AmT", "Dep Date", "Mat Date", "From Date", "To Date", "Interest", "SI A/c No", "Cal Freq"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblDepositMultiClosing.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 331));
        tblDepositMultiClosing.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDepositMultiClosingMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblDepositMultiClosingMouseReleased(evt);
            }
        });
        tblDepositMultiClosing.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblDepositMultiClosingFocusLost(evt);
            }
        });
        srpDepositInterestApplication.setViewportView(tblDepositMultiClosing);

        panProductTableData.add(srpDepositInterestApplication);
        srpDepositInterestApplication.setBounds(5, 32, 930, 300);

        panSelectAll.setMaximumSize(new java.awt.Dimension(200, 27));
        panSelectAll.setMinimumSize(new java.awt.Dimension(200, 27));
        panSelectAll.setPreferredSize(new java.awt.Dimension(200, 27));
        panSelectAll.setLayout(new java.awt.GridBagLayout());

        lblSelectAll.setText("Select All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 19, 5, 0);
        panSelectAll.add(lblSelectAll, gridBagConstraints);

        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 5, 1);
        panSelectAll.add(chkSelectAll, gridBagConstraints);

        panProductTableData.add(panSelectAll);
        panSelectAll.setBounds(10, 10, 110, 20);

        panDepositInterestApplication.add(panProductTableData);
        panProductTableData.setBounds(10, 200, 980, 340);
        panProductTableData.getAccessibleContext().setAccessibleName("Deposit Multiple Renewal");

        panProcess.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panProcess.setMinimumSize(new java.awt.Dimension(880, 30));
        panProcess.setPreferredSize(new java.awt.Dimension(880, 30));
        panProcess.setLayout(new java.awt.GridBagLayout());

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnClose, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnClear, gridBagConstraints);

        btnProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnProcess.setText("PROCESS");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panProcess.add(btnProcess, gridBagConstraints);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panProcess.add(btnNew, gridBagConstraints);

        lblTotAccounts.setText("Tot No.of A/c :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(lblTotAccounts, gridBagConstraints);

        lblAccountSelected.setText("A/c selected :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(lblAccountSelected, gridBagConstraints);

        lblTotNoAccountsVal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotNoAccountsVal.setForeground(new java.awt.Color(255, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(lblTotNoAccountsVal, gridBagConstraints);

        lblAccountSelectedVal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblAccountSelectedVal.setForeground(new java.awt.Color(255, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(lblAccountSelectedVal, gridBagConstraints);

        lblTotInt.setText("Tot Int :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(lblTotInt, gridBagConstraints);

        lblTotIntVal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotIntVal.setForeground(new java.awt.Color(255, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 16;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(lblTotIntVal, gridBagConstraints);

        lblTotPrinc.setText("Tot Principal :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(lblTotPrinc, gridBagConstraints);

        lblTotPrincVal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotPrincVal.setForeground(new java.awt.Color(255, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(lblTotPrincVal, gridBagConstraints);

        lblTotIntRecover.setText("Int Recover :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(lblTotIntRecover, gridBagConstraints);

        lblTotIntRecoverVal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotIntRecoverVal.setForeground(new java.awt.Color(255, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 18;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(lblTotIntRecoverVal, gridBagConstraints);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnAuthorize, gridBagConstraints);

        btnReject.setText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnReject, gridBagConstraints);

        panDepositInterestApplication.add(panProcess);
        panProcess.setBounds(10, 540, 980, 40);

        getContentPane().add(panDepositInterestApplication, java.awt.BorderLayout.CENTER);

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

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblDepositMultiClosingFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblDepositMultiClosingFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDepositMultiClosingFocusLost

    private void srpDepositInterestApplicationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_srpDepositInterestApplicationMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_srpDepositInterestApplicationMouseClicked
    public boolean checkAcNoValid(String actNum) {
        HashMap mapData = new HashMap();
        boolean isExists = false;
        try {//dont delete chck selectalldao
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData);
            if (mapDataList != null && mapDataList.size() > 0) {
                mapData = (HashMap) mapDataList.get(0);
                isExists = true;
                txtClosingCustomerIdCr.setText(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                observable.getCbmProdType().setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                observable.getCbmOAProductID().setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
            } else {

                isExists = false;
            }
            mapDataList = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapData = null;
        return isExists;
    }
    private void tblDepositMultiClosingMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDepositMultiClosingMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDepositMultiClosingMouseReleased
    private Date getProperDate(Date sourceDate) {
        Date targetDate = (Date) currDate.clone();
        targetDate.setDate(sourceDate.getDate());
        targetDate.setMonth(sourceDate.getMonth());
        targetDate.setYear(sourceDate.getYear());
        return targetDate;
    }
    private void btnToAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToAccountActionPerformed
        // TODO add your handling code here:
        if (txtProductID.getText().length() < 0) {
            ClientUtil.showAlertWindow("Please select Product Id!!!");
            return;
        }
        callView("TO");
    }//GEN-LAST:event_btnToAccountActionPerformed

    private void btnFromAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromAccountActionPerformed
        // TODO add your handling code here:
        if (txtProductID.getText().length() < 0) {
            ClientUtil.showAlertWindow("Please select Product Id!!!");
            return;
        }
        callView("FROM");
    }//GEN-LAST:event_btnFromAccountActionPerformed

    private void txtCustIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustIDFocusLost
        // TODO add your handling code here:

        String txtCustomer = txtCustID.getText();
        if (txtCustomer.length() > 0) {
            HashMap cust = new HashMap();
            cust.put("CUSTOMER ID", txtCustomer);
            viewType = "CUSTOMER ID";
            fillData(cust);
        }
    }//GEN-LAST:event_txtCustIDFocusLost

    private void btnCustIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustIDActionPerformed
        // TODO add your handling code here:
        viewType = "Customer";
        new CheckCustomerIdUI(this);
    }//GEN-LAST:event_btnCustIDActionPerformed

    private void txtProductIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductIDFocusLost
        // TODO add your handling code here:
        if (getScreenID() != null && !getScreenID().equals("") && getScreenID().length() > 0) {
            insertScreenLock(getScreenID(), "EDIT");
        }
        if (txtProductID.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("PROD_ID", txtProductID.getText());
            whereMap.put("BEHAVES_LIKE", "FIXED");
            List lst = ClientUtil.executeQuery("getAcctHead", whereMap);
            if (lst != null && lst.size() > 0) {
                whereMap = (HashMap) lst.get(0);
                observable.setTxtProductID(txtProductID.getText());
                enableDisable(true);
            } else {
                ClientUtil.displayAlert("Invalid Product ID !!! ");
                txtProductID.setText("");
                observable.setTxtProductID("");
                enableDisable(false);
            }
        }
    }//GEN-LAST:event_txtProductIDFocusLost

    private void enableDisable(boolean enable) {
        ClientUtil.enableDisable(panCustID, enable, false, true);
        btnCalculate.setEnabled(enable);
    }

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:.
        deleteScreenLock(getScreenID(), "EDIT");
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
       btnClearActionPerformed();
    }//GEN-LAST:event_btnClearActionPerformed
    private void btnClearActionPerformed() {
        observable.resetForm();
        totTransAmt = 0;
        enableDisable(false);
        ClientUtil.enableDisable(panDepositInterestApplication, false);
        ClientUtil.clearAll(this);
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
        btnProductID.setEnabled(true);
        txtProductID.setEnabled(true);
        btnCalculate.setEnabled(false);
        btnProcess.setEnabled(false);
        chkSelectAll.setEnabled(false);
        lblCustName.setText("");
        observable.removeTargetALLTransactionList();
        observable.setAccountsList(null);
        colorList = null;
        manualEntryEnable(false);
        deleteScreenLock(getScreenID(), "EDIT");
        btnNew.setVisible(false);
        btnNewActionPerformed(null);
        txtProductID.setEnabled(true);
        btnProductID.setEnabled(true);
        btnFromAccount.setEnabled(true);
        btnToAccount.setEnabled(true);
        observable.setFinalMap(null);
        lblTotNoAccountsVal.setText("");
        lblAccountSelectedVal.setText("");
        lblTotIntVal.setText("");
        lblTotPrincVal.setText("");
        lblTotIntRecoverVal.setText("");
        btnAuthorize.setEnabled(false);
        rdoPenaltyRateApplicableYes.setSelected(true);
        rdoPenaltyRateApplicableNo.setSelected(false);
        rdoPenaltyRateApplicableYes.setEnabled(true);
        rdoPenaltyRateApplicableNo.setEnabled(true);
        observable.setPenaltyRateApplicatble("Y");
    }
    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        btnProcess.setEnabled(false);
        //added by rishad 21/07/2015 for avoiding doubling issue
        observable.setScreen(this.getScreen());
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException /** Execute some operation */
            {
                saveperformed();
                return null;
            }

            @Override
            protected void done() {
                loading.dispose();
            }
        };
        worker.execute();
        loading.show();
        try {
            worker.get();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }//GEN-LAST:event_btnProcessActionPerformed
    private void saveperformed() {
        // added by rishad 25/06/2015 0010771: Block All Deposit payment greater than 20000 by Cash
        String transType = ((ComboBoxModel) cboClosingInterestTransMode.getModel()).getKeyForSelected().toString();
        if (transType.equalsIgnoreCase("CASH")) {
            HashMap dataMap = new HashMap();
            dataMap.put("PROD_ID", txtProductID.getText());
            List lst = (List) ClientUtil.executeQuery("getLimitAmountForDepProd", dataMap);
            dataMap = null;
            HashMap limitMap = new HashMap();
            if (lst != null && lst.size() > 0) {
                limitMap = (HashMap) lst.get(0);
                double cashLmt = CommonUtil.convertObjToDouble(limitMap.get("MAX_AMT_CASH")).doubleValue();
                if (totTransAmt > cashLmt) {
                    ClientUtil.displayAlert("Cash Limit Exeeded");
                    btnClearActionPerformed();
                    return;
                }
            }
        }
        
        // Added by nithya for interbranch transaction
           if (transType != null && observable.getSelectedBranchID() != null) {
            boolean interbranchFlag = false;
            //System.out.println("ProxyParameters.BRANCH_ID :: " + ProxyParameters.BRANCH_ID);
            //System.out.println("observable.getSelectedBranchID() :: " + observable.getSelectedBranchID());
            if (transType.equalsIgnoreCase((CommonConstants.TX_TRANSFER))) {
                if (!cboClosinglTransProdId.getSelectedItem().equals("") && (((ComboBoxModel) cboClosinglTransProdType.getModel()).getKeyForSelected().toString().equals("OA"))) {
                    String crAcNoBranch = CommonUtil.convertObjToStr(txtClosingCustomerIdCr.getText()).substring(0, 4);
                    //System.out.println("crAcNoBranch :: " + crAcNoBranch);
                    if (ProxyParameters.BRANCH_ID.equals(observable.getSelectedBranchID())) {
                        interbranchFlag = false;
                    } else if (ProxyParameters.BRANCH_ID.equals(crAcNoBranch)) {
                        interbranchFlag = false;
                    } else {
                        interbranchFlag = true;
                    }
                }else{
                    interbranchFlag = false;
                }
            } else {
                interbranchFlag = false;
            }
            if (interbranchFlag) {
                ClientUtil.showAlertWindow("Incase of interbranch transaction either " + "\n" + "Dr or Cr account of the transaction should be of own branch");
                return;
            } else {
                System.out.println("Continue for transactions...");
            }
        }
        // End
        //
        HashMap finalMap = observable.getFinalMap();
        String actNumList = "";
        String paramAct = "";
        boolean yesToContinue = true;
        for (int j = 0; j < tblDepositMultiClosing.getRowCount(); j++) { //added by shihad
            String acNo = CommonUtil.convertObjToStr(tblDepositMultiClosing.getValueAt(j, 1));
            boolean slect = ((Boolean) tblDepositMultiClosing.getValueAt(j, 0)).booleanValue();
            if (slect && finalMap.containsKey(acNo)) {
                actNumList += "'" + acNo + "_1',";
            }
        }
        if (actNumList != null && actNumList.length() > 0) {
            paramAct = actNumList.substring(0, actNumList.length() - 1);
            HashMap param = new HashMap();
            param.put("ACTSTRING", paramAct);
            List pendingList = ClientUtil.executeQuery("getPendingAuthTransactions", param);
            String pendAccounts = "";
            List pendAcctList = new ArrayList();
            if (pendingList != null && pendingList.size() > 0) {
                for (int i = 0; i < pendingList.size(); i++) {
                    HashMap linkMap = new HashMap();
                    linkMap = (HashMap) pendingList.get(i);
                    pendAcctList.add(CommonUtil.convertObjToStr(linkMap.get("LINK_BATCH_ID")));
                    String act = CommonUtil.convertObjToStr(linkMap.get("LINK_BATCH_ID")).substring(0, CommonUtil.convertObjToStr(linkMap.get("LINK_BATCH_ID")).length() - 2);
                    pendAccounts += act + "\n"; //Pending authorization of Interest payment\n
                }
                if (pendAcctList.size() > 0) {
                    for (int i = 0; i < pendAcctList.size(); i++) {
                        for (int j = 0; j < tblDepositMultiClosing.getRowCount(); j++) {
                            String acNo = CommonUtil.convertObjToStr(tblDepositMultiClosing.getValueAt(j, 1));
                            boolean slect = ((Boolean) tblDepositMultiClosing.getValueAt(j, 0)).booleanValue();
                            if (slect && CommonUtil.convertObjToStr(pendAcctList.get(i)).equals(acNo + "_1")) {
                                tblDepositMultiClosing.setValueAt(false, j, 0);
                            }
                        }
                    }
                    if (pendAccounts.length() > 0) {
                        int yesNo = 0;
                        String[] options = {"Yes", "No"};
                        yesNo = COptionPane.showOptionDialog(null, "Transaction pending for authorization"
                                + "\n" + pendAccounts + "\nDo you want to Continue?", CommonConstants.WARNINGTITLE,
                                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                null, options, options[0]);
                        if (yesNo == 0) {
                            yesToContinue = true;
                        } else {
                            yesToContinue = false;
                            return;
                        }
                    }
                }//             
            }
        }
        if (yesToContinue) {
            for (int j = 0; j < tblDepositMultiClosing.getRowCount(); j++) {
                String acNo = CommonUtil.convertObjToStr(tblDepositMultiClosing.getValueAt(j, 1));
                boolean slect = ((Boolean) tblDepositMultiClosing.getValueAt(j, 0)).booleanValue();
                if (!slect && finalMap.containsKey(acNo)) {
                    finalMap.remove(acNo);
                }
            }            
            HashMap term = new HashMap();
            term.put("finalMap", finalMap);
            term.put("MULTIPLE_DEPOSIT_CLOSING", "MULTIPLE_DEPOSIT_CLOSING");
            term.put("CUST_ID_UP", CommonUtil.convertObjToStr(txtCustID.getText()));
            term.put("FROM_ACC_CLOSE", CommonUtil.convertObjToStr(txtFromAccount.getText()));
            term.put("TO_ACC_CLOSE", CommonUtil.convertObjToStr(txtToAccount.getText()));            
            observable.doAction(term);
            if (observable.getProxyReturnMap() != null) {
                HashMap returnMap = observable.getProxyReturnMap();
                if (returnMap.containsKey("STATUS")) {
                    String retStatus = CommonUtil.convertObjToStr(returnMap.get("STATUS"));
                    if (retStatus != null && retStatus.equals("SUCCESS")) {
                        String depositNo = "";
                        for (int j = 0; j < tblDepositMultiClosing.getRowCount(); j++) {
                            boolean slect = ((Boolean) tblDepositMultiClosing.getValueAt(j, 0)).booleanValue();
                            if (slect) {
                                if (j == 0) {
                                    depositNo = depositNo + "'" + tblDepositMultiClosing.getValueAt(j, 1).toString() + "_1" + "'";
                                } else {
                                    depositNo = depositNo + ",'" + tblDepositMultiClosing.getValueAt(j, 1).toString() + "_1" + "'";
                                }

                            }
                        }
                        if (depositNo != null && depositNo.charAt(0) == ',') {
                            depositNo = depositNo.substring(1, depositNo.length());
                        }
                        HashMap dMap = new HashMap();
                        dMap.put("DEP_NO", depositNo);
                        dMap.put("TRANS_DT", ClientUtil.getCurrentDate());
                        String frmBatchId = "", toBatchId = "", singleTransId = "";
                        String frmTransId = "", toTransId = "";
                        int CreditcashCount = 0;
                        int DebitcashCount = 0;
                        HashMap transIdMap = new HashMap();
                        ArrayList<String> singleTranIdList = new ArrayList<String>();
                        ArrayList<Integer> debitCashCountList = new ArrayList<Integer>();
                        ArrayList<Integer> creditCashCountList = new ArrayList<Integer>();
                        List dataList = ClientUtil.executeQuery("getAllMultileBatchIDForDepositClosing", dMap);
                        if (dataList.size() > 0) {
                            for (int i = 0; i < dataList.size(); i++) {
                                HashMap dataMap = (HashMap) dataList.get(i);
                                if (i == 0) {
                                    frmBatchId = CommonUtil.convertObjToStr(dataMap.get("BATCH_ID"));
                                    frmTransId = CommonUtil.convertObjToStr(dataMap.get("TRANS_ID"));
                                }
                                if (i == (dataList.size() - 1)) {
                                    toBatchId = CommonUtil.convertObjToStr(dataMap.get("BATCH_ID"));
                                    toTransId = CommonUtil.convertObjToStr(dataMap.get("TRANS_ID"));
                                }
                                if (!singleTranIdList.contains(CommonUtil.convertObjToStr(dataMap.get("SINGLE_TRANS_ID")))) {
                                    singleTranIdList.add(CommonUtil.convertObjToStr(dataMap.get("SINGLE_TRANS_ID")));
                                }
                            }
                        }
                        if (((ComboBoxModel) cboClosingInterestTransMode.getModel()).getKeyForSelected().toString().equals("CASH")) {
                            for (int i = 0; i < singleTranIdList.size(); i++) {
                                HashMap paramMap = new HashMap();
                                paramMap.put("TRANS_DT", currDate);
                                paramMap.put("SINGLE_TRANS_ID", CommonUtil.convertObjToStr(singleTranIdList.get(i)));
                                List creditDebitList = ClientUtil.executeQuery("getNoOfDebitCreditCash", paramMap);
                                if (creditDebitList != null && creditDebitList.size() > 0) {
                                    HashMap resultMap = new HashMap();
                                    HashMap dataMap = (HashMap) creditDebitList.get(0);
                                    debitCashCountList.add(CommonUtil.convertObjToInt(dataMap.get("DEBIT")));
                                    creditCashCountList.add(CommonUtil.convertObjToInt(dataMap.get("CREDIT")));
                                }
                            }
                        }
                        if (frmBatchId != null && frmBatchId.length() > 0) {
                            ClientUtil.showMessageWindow("Completed Succesfully. From Batch Id " + frmBatchId + " To Batch Id " + toBatchId);
                        } else {
                            ClientUtil.showMessageWindow("Completed Succesfully. From Trans Id " + frmTransId + " To Trans Id " + toTransId);
                        }
                        //Print purpose
                        int yesNo = 0;
                        String[] options = {"Yes", "No"};
                        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                null, options, options[0]);
                        System.out.println("#$#$$ yesNo : " + yesNo);
                        //                            System.out.println("#$#$$ transList.size() : "+transList.size());
                        if (yesNo == 0) {
                            for (int i = 0; i < singleTranIdList.size(); i++) {
                                String reportName = null;
                                TTIntegration ttIntgration = null;
                                HashMap paramMap = null;
                                paramMap = new HashMap();
                                paramMap.put("TransId", singleTranIdList.get(i));
                                paramMap.put("TransDt", currDate);
                                paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                                ttIntgration.setParam(paramMap);
                                String installType = ((ComboBoxModel) cboClosingInterestTransMode.getModel()).getKeyForSelected().toString();
                                if (installType != null && installType.equals("TRANSFER")) {
                                    reportName = "ReceiptPayment";
                                    ttIntgration.integrationForPrint("ReceiptPayment");
                                }
                                if (installType != null && installType.equals("CASH")) {
                                    if (debitCashCountList.get(i) > 0) {
                                        System.out.println("debit count a" + debitCashCountList.get(i));
                                        reportName = "CashPayment";
                                        ttIntgration.integrationForPrint(reportName, false);
                                    }
                                    if (creditCashCountList.get(i) > 0) {
                                        reportName = "CashReceipt";
                                        ttIntgration.integrationForPrint(reportName, false);
                                    }
                                }
                            }
                        }
                    } else {
                    }
                }
            }
            term = null;
            finalMap = null;
            btnClearActionPerformed(null);
            finalMap = new HashMap();
            TrueTransactMain.populateBranches();
            TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
            observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
            setSelectedBranchID(ProxyParameters.BRANCH_ID);
        }
    }
    private void setColour() {
        /*
         * Set a cellrenderer to this table in order format the date
         */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                System.out.println("row #####" + row + "valu " + value.toString());
                if (freezDepositLst.contains(row)) {// Added by nithya on 12-10-2019
                    setForeground(Color.BLUE);
                }
//                else if (column == 11 && CommonUtil.convertObjToDouble(value) > 0) {
//                    setForeground(Color.RED);
//                } 
                else if (CommonUtil.convertObjToDouble(table.getValueAt(row, 11)) > 0) { //KD-3607
                    setForeground(Color.RED);
                }
                else {
                    setForeground(Color.BLACK);
                }
                this.setOpaque(true);
                return this;
            }
        };
        tblDepositMultiClosing.setDefaultRenderer(Object.class, renderer);
    }

    private void tblDepositMultiClosingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDepositMultiClosingMouseClicked
        // TODO add your handling code here:

        List LienList = null;
        int count = 0;
        double totIntPayable = 0.0;
        double totIntRecvable = 0.0;
        double totPrincipal = 0.0;
        double totClosingAmt=0.0;
        if (selectMode == true && tblDepositMultiClosing.getSelectedColumn() == 0) {
            String st = CommonUtil.convertObjToStr(tblDepositMultiClosing.getValueAt(tblDepositMultiClosing.getSelectedRow(), 0));

            if (st.equals("true")) {
                tblDepositMultiClosing.setValueAt(new Boolean(false), tblDepositMultiClosing.getSelectedRow(), 0);
            } else {
                tblDepositMultiClosing.setValueAt(new Boolean(true), tblDepositMultiClosing.getSelectedRow(), 0);
                if (freezDepositLst != null && freezDepositLst.size() > 0) { // Added by nithya on 12-10-2019
                    for (int k = 0; k < freezDepositLst.size(); k++) {
                        if (freezDepositLst.get(k).equals(tblDepositMultiClosing.getSelectedRow())) {
                            tblDepositMultiClosing.setValueAt(new Boolean(false), tblDepositMultiClosing.getSelectedRow(), 0);
                        }
                    }
                }
            }
            double totAmount = 0;
            for (int i = 0; i < tblDepositMultiClosing.getRowCount(); i++) {
                st = CommonUtil.convertObjToStr(tblDepositMultiClosing.getValueAt(i, 0));
                if (st.equals("true")) {
                    totAmount = totAmount + CommonUtil.convertObjToDouble(tblDepositMultiClosing.getValueAt(i, 9)).doubleValue();
                    count = count + 1;
                    totIntPayable = totIntPayable + CommonUtil.convertObjToDouble(tblDepositMultiClosing.getValueAt(i, 10));
                    totIntRecvable = totIntRecvable + CommonUtil.convertObjToDouble(tblDepositMultiClosing.getValueAt(i, 11));
                    totPrincipal = totPrincipal + CommonUtil.convertObjToDouble(tblDepositMultiClosing.getValueAt(i, 3));
                    totClosingAmt= totClosingAmt + CommonUtil.convertObjToDouble(tblDepositMultiClosing.getValueAt(i, 12));
                }
            }
        }
        totTransAmt=totClosingAmt;
        lblAccountSelectedVal.setText(CommonUtil.convertObjToStr(count));
        lblTotIntRecoverVal.setText(CommonUtil.convertObjToStr(Math.round(totIntRecvable)));
        lblTotIntVal.setText(CommonUtil.convertObjToStr(Math.round(totIntPayable)));
        lblTotPrincVal.setText(CommonUtil.convertObjToStr(totPrincipal));
        if (evt.getClickCount() == 2) {
            if (returnMap != null) {
                if (returnMap.containsKey(tblDepositMultiClosing.getValueAt(
                        tblDepositMultiClosing.getSelectedRow(), 1))) {
                    TTException exception = (TTException) returnMap.get(tblDepositMultiClosing.getValueAt(
                            tblDepositMultiClosing.getSelectedRow(), 1));
                    parseException.logException(exception, true);

                }
            }
            if ((tblDepositMultiClosing.getSelectedColumn() == 9) && ((Boolean) tblDepositMultiClosing.getValueAt(tblDepositMultiClosing.getSelectedRow(), 0)).booleanValue()) {
                if (CommonUtil.convertObjToDouble(tblDepositMultiClosing.getValueAt(tblDepositMultiClosing.getSelectedRow(), 9).toString()).doubleValue() > 0) {
                }
            }
            String lienAccno = CommonUtil.convertObjToStr(tblDepositMultiClosing.getValueAt(tblDepositMultiClosing.getSelectedRow(), 2));
            HashMap hmap = new HashMap();
            hmap.put("DEPOSIT_NO", lienAccno);
            LienList = ClientUtil.executeQuery("getLienAccNoForDispiaying", hmap);
            if (LienList != null && LienList.size() > 0) {
                ArrayList displayList = new ArrayList();
                for (int i = 0; i < LienList.size(); i++) {
                    hmap = (HashMap) LienList.get(i);
                    displayList.add(hmap);
                }
            }
            HashMap reportParamMap = new HashMap();
            String actNum = (String) tblDepositMultiClosing.getValueAt(tblDepositMultiClosing.getSelectedRow(), 2);
            com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
            HashMap prodMap = new HashMap();

            HashMap paramMap = new HashMap();
            paramMap.put("AccountNo", actNum);
            paramMap.put("FromDt", ClientUtil.getCurrentDate());
            paramMap.put("ToDt", ClientUtil.getCurrentDate());
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            ttIntgration.setParam(paramMap);
            if (actNum.lastIndexOf('_') != -1) {
                actNum = actNum.substring(0, actNum.lastIndexOf("_"));
            }
            prodMap.put("ACT_NUM", actNum);
            List lst = ClientUtil.executeQuery("getBehavesLikeForDepositNo", prodMap);
            if (lst != null && lst.size() > 0) {
                prodMap = (HashMap) lst.get(0);
                if (prodMap.get("BEHAVES_LIKE").equals("DAILY")) {
                    ttIntgration.integration("DailyLedger");
                } else {
                    ttIntgration.integration("TDLedger");
                }
            }
        }
    }//GEN-LAST:event_tblDepositMultiClosingMouseClicked

    public void enteredAmount() {
        double intAmount = CommonUtil.convertObjToDouble(tblDepositMultiClosing.getValueAt(tblDepositMultiClosing.getSelectedRow(), 9).toString()).doubleValue();
        String intPayAcno = CommonUtil.convertObjToStr(tblDepositMultiClosing.getValueAt(tblDepositMultiClosing.getSelectedRow(), 10));
        double clearBal = 0.0;
        ArrayList list = observable.getAccountsList();
        System.out.println("########### ACCT list : " + list);
        HashMap rdListMap = new HashMap();
        rdListMap = (HashMap) list.get(tblDepositMultiClosing.getSelectedRow());
        String type = CommonUtil.convertObjToStr(rdListMap.get("ACCT_TYPE"));
        double amount = CommonUtil.convertObjToDouble(rdListMap.get("AMOUNT")).doubleValue();
        System.out.println("########### ACCT_TYPE : " + type);
        rdListMap.put("ACT_NUM", intPayAcno);
        List loanList = ClientUtil.executeQuery("LoneFacilityDetailAD", rdListMap);
        if (loanList != null && loanList.size() > 0) {
            rdListMap = (HashMap) loanList.get(0);
            clearBal = CommonUtil.convertObjToDouble(rdListMap.get("CLEAR_BALANCE")).doubleValue();
        }
        HashMap amountMap = new HashMap();
        amountMap.put("TITLE", "Interest Amount");
        if (type.equals("LOANS_AGAINST_DEPOSITS")) {

            amountMap.put("TOTAL_DEMAND", new Double(amount));
            amountMap.put("CLEAR_BALANCE", new Double(clearBal));
        }
        amountMap.put("TOLERANCE_AMT", CommonConstants.TOLERANCE_AMT);
        amountMap.put("SELECTED_AMT", String.valueOf(intAmount));
        amountMap.put("CALCULATED_AMT", String.valueOf(intAmount));
        TextUI textUI = new TextUI(this, this, amountMap);
    }
    
    private void setFreezDepositsDisabled() { // added by nithya on 12-10-2019 
        for (int i = 0; i < tblDepositMultiClosing.getRowCount(); i++) {
            if (freezDepositLst.contains(i)) {
                tblDepositMultiClosing.setValueAt(false, i, 0);
            }
        }
    }

    private void checkForFreezeDeposits(){ // added by nithya on 12-10-2019 
        for (int i = 0; i < tblDepositMultiClosing.getRowCount(); i++) {           
            String depositNo = CommonUtil.convertObjToStr(tblDepositMultiClosing.getValueAt(i, 1));
            HashMap freezeMap = new HashMap();
                freezeMap.put("DEPOSIT_NO",depositNo);
                List lst = ClientUtil.executeQuery("getFreezeAccNoForDep", freezeMap);
                if(lst!=null && lst.size()>0){
                    freezDepositLst.add(i);
                }
        }              
    }
    
    public void modifyTransData(Object obj) {
        TextUI objTextUI = (TextUI) obj;
        String selectedDepNo = "";
        String enteredData = objTextUI.getTxtData();
        double intAmt = CommonUtil.convertObjToDouble(enteredData).doubleValue();
        selectedDepNo = CommonUtil.convertObjToStr(tblDepositMultiClosing.getValueAt(tblDepositMultiClosing.getSelectedRow(), 2));
        if (observable.getFinalList() != null && observable.getFinalList().size() > 0) {
            List interestList = null;
            for (int i = 0; i < observable.getFinalList().size(); i++) {
                String depNo = "";
                String siNo = "";
                interestList = new ArrayList();
                interestList = (ArrayList) observable.getFinalList().get(i);
                depNo = CommonUtil.convertObjToStr(interestList.get(2));
                siNo = CommonUtil.convertObjToStr(interestList.get(10));
                if (selectedDepNo.equals(depNo)) {
                    interestList.set(9, new Double(intAmt));
                }
                if (siNo == null || siNo.length() <= 0) {
                    siNo = "";
                    interestList.set(10, siNo);
                }
            }
        }
        ArrayList list = observable.getAccountsList();
        observable.updateInterestData();
        tblDepositMultiClosing.setModel(observable.getTblDepositInterestApplication());
        setSizeTableData();
    }

    private void btnCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateActionPerformed
        // TODO add your handling code here:
        btnCalculate.setEnabled(false);
        if(rdoPenaltyRateApplicableNo.isSelected()){
            observable.setPenaltyRateApplicatble("N");
        }else{
            observable.setPenaltyRateApplicatble("Y");
        }
        btnCalculateActionPerformed();
    }//GEN-LAST:event_btnCalculateActionPerformed

    private void btnCalculateActionPerformed() {
        String actNumStr = "";
        String custIDStr = "";
        String dispString = "";
        if (txtProductID.getText().length() == 0 && txtCustID.getText().length() == 0) {
            ClientUtil.displayAlert("Enter Prod ID/Customer ID");
            return;
        }
        if (txtProductID.getText().length() > 0 && txtCustID.getText().length() <= 0) {
            int opt = 0;
            if (txtFromAccount.getText().length() == 0 && txtToAccount.getText().length() >= 0) {
                actNumStr = "From Account No not selected.\n";
                ClientUtil.displayAlert("Enter From AccountNo");
                return;
            }
        }
        HashMap whrMap = new HashMap();
        if (txtFromAccount.getText().length() >= 0) {
            whrMap.put("ACT_FROM", txtFromAccount.getText());
        }
        if (txtToAccount.getText().length() >= 0) {
            whrMap.put("ACT_TO", txtToAccount.getText());
        }
        List deathList = ClientUtil.executeQuery("getDeathMarkedCustomerForMultipleTD", whrMap);
        System.out.println("deathList :" + deathList);
        if (deathList.size() > 0) {
            String depno = "";
            for (int k = 0; k < deathList.size(); k++) {
                HashMap hmap = (HashMap) deathList.get(k);
                depno += CommonUtil.convertObjToStr(hmap.get("DEPOSIT_NO"));
                if (k != deathList.size() - 1) {
                    depno += ", ";
                }
            }
            if (depno != null && depno.length() > 0) {
                ClientUtil.showMessageWindow("The customer of account no(s) " + depno + " has been death marked."
                        + "\n Please close the deposits using normal deposit closing screen");
                return;
            }
        }
        String transType = ((ComboBoxModel) cboClosingInterestTransMode.getModel()).getKeyForSelected().toString();
        if (transType.equals(null) || transType.length() <= 0) {
            ClientUtil.displayAlert("Select mode of transaction");
            return;
        }
        if (transType.equals("TRANSFER")) {
            if (cboClosinglTransProdType.getSelectedItem().equals("")) {
                ClientUtil.displayAlert("Product type should not be empty");
                return;
            }
            if (cboClosinglTransProdId.getSelectedItem().equals("") && !cboClosinglTransProdType.getSelectedItem().equals("General Ledger")) {
                ClientUtil.displayAlert("Product id should not be empty");
                return;
            }
            if (txtClosingCustomerIdCr.getText().isEmpty()) {
                ClientUtil.displayAlert("Account number should not be empty");
                return;
            }
        }
        displayDepositClosingDetails();
        chkSelectAll.setEnabled(true);
        cboClosinglTransProdType.setEnabled(false);
        cboClosinglTransProdId.setEnabled(false);
        txtClosingCustomerIdCr.setEnabled(false);
        cboClosingInterestTransMode.setEnabled(false);
    }

    private void displayDepositClosingDetails() {
        selectMode = true;
        observable.setTxtProductID(txtProductID.getText());
        tblDepositMultiClosing.setEnabled(true);
        btnProductID.setEnabled(false);
        btnProcess.setEnabled(true);
        txtProductID.setEnabled(false);
        HashMap dataMap = new HashMap();
        if (txtCustID.getText() != null && (!txtCustID.getText().equals(""))) {
            dataMap.put("CUST_ID", CommonUtil.convertObjToStr(txtCustID.getText()));
        }

        if (txtFromAccount.getText() != null && (!txtFromAccount.getText().equals(""))) {
            dataMap.put("ACT_FROM", CommonUtil.convertObjToStr(txtFromAccount.getText()));
        }
        if (txtToAccount.getText() != null && (!txtToAccount.getText().equals(""))) {
            dataMap.put("ACT_TO", CommonUtil.convertObjToStr(txtToAccount.getText()));
        }
        Date tempDt = (Date) currDate.clone();
        dataMap.put("CURR_DT", tempDt);
        dataMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
        dataMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        String installType = ((ComboBoxModel) cboClosingInterestTransMode.getModel()).getKeyForSelected().toString();
        System.out.println("install typeasd a" + installType + " as " + cboClosingInterestTransMode.getSelectedItem());
        dataMap.put("TRANS_MODE", installType);
        if (installType.equals("TRANSFER")) {
            String prodType = ((ComboBoxModel) cboClosinglTransProdType.getModel()).getKeyForSelected().toString();
            dataMap.put("PROD_TYPE", prodType);
            String prodId = "";
            if (((ComboBoxModel) cboClosinglTransProdId.getModel()).getKeyForSelected() != null) {
                prodId = ((ComboBoxModel) cboClosinglTransProdId.getModel()).getKeyForSelected().toString();
            }
            dataMap.put("PROD_ID", prodId);
            dataMap.put("AC_NO", txtClosingCustomerIdCr.getText());
        }
        String category = "";
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.insertTableData(dataMap);
        tblDepositMultiClosing.setModel(observable.getTblDepositInterestApplication());
        checkForFreezeDeposits();// added by nithya on 12-10-2019 
        setColour();
        List tabList = observable.getFinalList();
        setSizeTableData();
        if (tblDepositMultiClosing.getRowCount() == 0) {
            ClientUtil.showMessageWindow(" No Data !!! ");
            btnProcess.setEnabled(false);
        } else {
            int count = 0;
            for (int i = 0; i < tblDepositMultiClosing.getRowCount(); i++) {
                count = count + 1;
            }
            lblTotIntVal.setText("0");
            lblAccountSelectedVal.setText("0");
            lblTotNoAccountsVal.setText(CommonUtil.convertObjToStr(count));
            lblTotPrincVal.setText("0");
            lblTotIntRecoverVal.setText("0");
            enableDisable(false);
        }

    }

    //Added By Suresh
    private void setSizeTableData() {
        javax.swing.table.TableColumn col = tblDepositMultiClosing.getColumn(tblDepositMultiClosing.getColumnName(0));
        col.setMaxWidth(40);
        col = tblDepositMultiClosing.getColumn(tblDepositMultiClosing.getColumnName(1));
        col.setMaxWidth(180);
        col = tblDepositMultiClosing.getColumn(tblDepositMultiClosing.getColumnName(2));
        col.setMaxWidth(120);
        col = tblDepositMultiClosing.getColumn(tblDepositMultiClosing.getColumnName(3));
        col.setMaxWidth(150);
        col = tblDepositMultiClosing.getColumn(tblDepositMultiClosing.getColumnName(4));
        col.setMaxWidth(65);
        col = tblDepositMultiClosing.getColumn(tblDepositMultiClosing.getColumnName(5));
        col.setMaxWidth(65);
        col = tblDepositMultiClosing.getColumn(tblDepositMultiClosing.getColumnName(6));
        col.setMaxWidth(65);

    }

    private void btnProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductIDActionPerformed
        // TODO add your handling code here:
        callView("PROD_DETAILS");
        if (txtProductID.getText().length() > 0) {
            enableDisable(true);
        }
    }//GEN-LAST:event_btnProductIDActionPerformed

private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
    if (getScreenID() != null && !getScreenID().equals("") && getScreenID().length() > 0) {
        deleteScreenLock(getScreenID(), "EDIT");
    }
    this.dispose();
}//GEN-LAST:event_formInternalFrameClosing

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        panProductIdMain.setEnabled(true);
        panFromTonum.setEnabled(true);
        cPanel1.setEnabled(true);
        txtProductID.setEnabled(true);
        btnProductID.setEnabled(true);
        btnFromAccount.setEnabled(true);
        btnToAccount.setEnabled(true);
        btnClear.setEnabled(true);
        btnCustID.setEnabled(true);
        txtCustID.setEnabled(true);
        cboClosingInterestTransMode.setEnabled(true);
        cboClosinglTransProdType.setEnabled(true);
        txtClosingCustomerIdCr.setEnabled(true);
        btnCalculate.setEnabled(true);
        btnProcess.setEnabled(true);
    }//GEN-LAST:event_btnNewActionPerformed

private void cboClosingInterestTransModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboClosingInterestTransModeActionPerformed
    // TODO add your handling code here:
    String installType = ((ComboBoxModel) cboClosingInterestTransMode.getModel()).getKeyForSelected().toString();
    if (installType.equals("TRANSFER")) {
        cboClosinglTransProdType.setEnabled(true);
        cboClosinglTransProdId.setEnabled(true);
        txtClosingCustomerIdCr.setEnabled(true);
    } else {
        txtClosingCustomerIdCr.setText("");
        cboClosinglTransProdType.setEnabled(false);
        cboClosinglTransProdId.setEnabled(false);
        txtClosingCustomerIdCr.setEnabled(false);
    }
}//GEN-LAST:event_cboClosingInterestTransModeActionPerformed

private void cboClosinglTransProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboClosinglTransProdTypeActionPerformed
    // TODO add your handling code here:
    if (cboClosinglTransProdType.getSelectedIndex() > 0) {
        String prodType = ((ComboBoxModel) cboClosinglTransProdType.getModel()).getKeyForSelected().toString();
        observable.setCbmRenewalInterestTransProdId(prodType);
        if (prodType.equals("GL")) {
            cboClosinglTransProdId.setSelectedItem("");
            cboClosinglTransProdId.setEnabled(false);
            txtClosingCustomerIdCr.setText("");
            lblRenewalInterestTransAccNo.setText("Account Head Id");
            btnDepositClostCustomerIdFileOpenCr.setEnabled(true);
        } else if (prodType.equals("TD") || prodType.equals("TL") || prodType.equals("AD") || prodType.equals("RM")) {
            ClientUtil.showAlertWindow("Not Possible to credit");
            cboClosinglTransProdId.setSelectedItem("");
            cboClosinglTransProdId.setEnabled(false);
            txtClosingCustomerIdCr.setText("");
            btnDepositClostCustomerIdFileOpenCr.setEnabled(false);
            return;
        } else {
            cboClosinglTransProdId.setSelectedItem("");
            cboClosinglTransProdId.setEnabled(true);
            btnDepositClostCustomerIdFileOpenCr.setEnabled(true);
            txtClosingCustomerIdCr.setEnabled(false);
            txtClosingCustomerIdCr.setText("");
        }
        cboClosinglTransProdId.setModel(observable.getCbmRenewalInterestTransProdId());
        if (!prodType.equals("GL") && cboClosinglTransProdId.getSelectedItem().equals("")) {
        }
    }
}//GEN-LAST:event_cboClosinglTransProdTypeActionPerformed

private void cboClosinglTransProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboClosinglTransProdIdActionPerformed
    // TODO add your handling code here:
    txtClosingCustomerIdCr.setText("");
}//GEN-LAST:event_cboClosinglTransProdIdActionPerformed

private void btnDepositClostCustomerIdFileOpenCrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositClostCustomerIdFileOpenCrActionPerformed
    // TODO add your handling code here:
    if (cboClosinglTransProdType.getSelectedIndex() > 0) {
        callView("DEPOSIT_CLOSE_TRANS_ACC_NO");
    } else {
        ClientUtil.showAlertWindow("Product Type should not be empty...");
        return;
    }

}//GEN-LAST:event_btnDepositClostCustomerIdFileOpenCrActionPerformed

private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
    // TODO add your handling code here:
    boolean flag;
    if (chkSelectAll.isSelected() == true) {
        flag = true;
    } else {
        flag = false;
    }
    int count = 0;
    double totIntRecvable = 0.0;
    double totIntPayable = 0.0;
    double totPrincipal = 0.0;
    double totAmount = 0;
    double totClosingAmt=0.0;
    for (int i = 0; i < tblDepositMultiClosing.getRowCount(); i++) {
        tblDepositMultiClosing.setValueAt(new Boolean(flag), i, 0);
//        count = count + 1;
//        totIntPayable = totIntPayable + CommonUtil.convertObjToDouble(tblDepositMultiClosing.getValueAt(i, 10));
//        totIntRecvable = totIntRecvable + CommonUtil.convertObjToDouble(tblDepositMultiClosing.getValueAt(i, 11));
//        totPrincipal = totPrincipal + CommonUtil.convertObjToDouble(tblDepositMultiClosing.getValueAt(i, 3));
//        totClosingAmt= totClosingAmt + CommonUtil.convertObjToDouble(tblDepositMultiClosing.getValueAt(i, 12));       
    }
    setFreezDepositsDisabled();
    for (int i = 0; i < tblDepositMultiClosing.getRowCount(); i++) {
        if (CommonUtil.convertObjToStr(tblDepositMultiClosing.getValueAt(i, 0)).equals("true")) {
            count = count + 1;
            totIntPayable = totIntPayable + CommonUtil.convertObjToDouble(tblDepositMultiClosing.getValueAt(i, 10));
            totIntRecvable = totIntRecvable + CommonUtil.convertObjToDouble(tblDepositMultiClosing.getValueAt(i, 11));
            totPrincipal = totPrincipal + CommonUtil.convertObjToDouble(tblDepositMultiClosing.getValueAt(i, 3));
            totClosingAmt = totClosingAmt + CommonUtil.convertObjToDouble(tblDepositMultiClosing.getValueAt(i, 12));
        }
    }
    if (flag) {
        lblAccountSelectedVal.setText(CommonUtil.convertObjToStr(count));
        lblTotIntRecoverVal.setText(CommonUtil.convertObjToStr(Math.round(totIntRecvable)));
        lblTotIntVal.setText(CommonUtil.convertObjToStr(Math.round(totIntPayable)));
        lblTotPrincVal.setText(CommonUtil.convertObjToStr(totPrincipal));
        totTransAmt=totClosingAmt;
    } else {
        lblAccountSelectedVal.setText("0");
        lblTotPrincVal.setText("0");
        lblTotIntVal.setText("0");
        lblTotIntRecoverVal.setText("0");
        totTransAmt=0;
    }
}//GEN-LAST:event_chkSelectAllActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        HashMap finalMap = observable.getFinalMap();
        System.out.println("auth map hr" + finalMap);
        for (int j = 0; j < tblDepositMultiClosing.getRowCount(); j++) {
            String acNo = CommonUtil.convertObjToStr(tblDepositMultiClosing.getValueAt(j, 1));
            boolean slect = ((Boolean) tblDepositMultiClosing.getValueAt(j, 0)).booleanValue();
            System.out.println("-------->" + acNo + "slect ---->" + slect);
            if ((!slect && finalMap.containsKey(acNo))) {
                finalMap.remove(acNo);
            }
        }
        System.out.println("--finalMap------>" + finalMap);
        HashMap term = new HashMap();
        term.put("finalMap", finalMap);
        term.put("MULTIPLE_DEPOSIT_AUTHORIZE", "MULTIPLE_DEPOSIT_AUTHORIZE");
        term.put(CommonConstants.STATUS_AUTHORIZED, CommonConstants.STATUS_AUTHORIZED);
        observable.doAction(term);
        System.out.println("retrun map " + observable.getProxyReturnMap());
        term = null;
        finalMap = null;
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
             if (observable.getProxyReturnMap() != null) {
                if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.displayDetails("Deposit Multiple Closing");
                }
            }
            if (observable.getProxyReturnMap() != null) {
                  if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.displayDetails("Deposit Multiple Closing");
                }
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                    authorizeListUI.setFocusToTable();
                    authorizeListUI.displayDetails("Multiple Deposit Closing");
                }
            }
            
        }
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        HashMap finalMap = observable.getFinalMap();
        for (int j = 0; j < tblDepositMultiClosing.getRowCount(); j++) {
            String acNo = CommonUtil.convertObjToStr(tblDepositMultiClosing.getValueAt(j, 1));
            boolean slect = ((Boolean) tblDepositMultiClosing.getValueAt(j, 0)).booleanValue();
            System.out.println("-------->" + acNo + "slect ---->" + slect);
            if (!slect && finalMap.containsKey(acNo)) {
                finalMap.remove(acNo);
            }
        }
        System.out.println("--finalMap------>" + finalMap);
        HashMap term = new HashMap();
        term.put("finalMap", finalMap);
        term.put("MULTIPLE_DEPOSIT_AUTHORIZE", "MULTIPLE_DEPOSIT_AUTHORIZE");
        term.put(CommonConstants.STATUS_REJECTED, CommonConstants.STATUS_REJECTED);
        observable.doAction(term);
        term = null;
        finalMap = null;
        System.out.println("retrun map " + observable.getProxyReturnMap());
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if (observable.getProxyReturnMap() != null) {
                if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.displayDetails("Multiple Deposit Closing");
                }
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                    authorizeListUI.setFocusToTable();
                    authorizeListUI.displayDetails("Multiple Deposit Closing");
                }
            }
        }
    }//GEN-LAST:event_btnRejectActionPerformed

    private void rdoPenaltyRateApplicableYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPenaltyRateApplicableYesActionPerformed
        // TODO add your handling code here:
        if(rdoPenaltyRateApplicableYes.isSelected()){
            rdoPenaltyRateApplicableNo.setSelected(false);
        }else{
           rdoPenaltyRateApplicableNo.setSelected(true); 
        }
    }//GEN-LAST:event_rdoPenaltyRateApplicableYesActionPerformed

    private void rdoPenaltyRateApplicableNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPenaltyRateApplicableNoActionPerformed
        // TODO add your handling code here:
          if(rdoPenaltyRateApplicableNo.isSelected()){
            rdoPenaltyRateApplicableYes.setSelected(false);
        }else{
           rdoPenaltyRateApplicableYes.setSelected(true); 
        }
    }//GEN-LAST:event_rdoPenaltyRateApplicableNoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCalculate;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustID;
    private com.see.truetransact.uicomponent.CButton btnDepositClostCustomerIdFileOpenCr;
    private com.see.truetransact.uicomponent.CButton btnFromAccount;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnProductID;
    private javax.swing.JButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnToAccount;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CComboBox cboClosingInterestTransMode;
    private com.see.truetransact.uicomponent.CComboBox cboClosinglTransProdId;
    private com.see.truetransact.uicomponent.CComboBox cboClosinglTransProdType;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private javax.swing.JLabel lblAccountSelected;
    private javax.swing.JLabel lblAccountSelectedVal;
    private com.see.truetransact.uicomponent.CLabel lblCustID;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblFromAccount;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPenaltyRateApplicable;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblRenewalInterestTransAccNo;
    private com.see.truetransact.uicomponent.CLabel lblRenewalInterestTransProdId;
    private com.see.truetransact.uicomponent.CLabel lblRenewalInterestTransProdType;
    private com.see.truetransact.uicomponent.CLabel lblRenewalWithdrawingIntAmt;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToAccount;
    private javax.swing.JLabel lblTotAccounts;
    private javax.swing.JLabel lblTotInt;
    private javax.swing.JLabel lblTotIntRecover;
    private javax.swing.JLabel lblTotIntRecoverVal;
    private javax.swing.JLabel lblTotIntVal;
    private javax.swing.JLabel lblTotNoAccountsVal;
    private javax.swing.JLabel lblTotPrinc;
    private javax.swing.JLabel lblTotPrincVal;
    private com.see.truetransact.uicomponent.CPanel panCustID;
    private com.see.truetransact.uicomponent.CPanel panDepositInterestApplication;
    private com.see.truetransact.uicomponent.CPanel panFromTonum;
    private com.see.truetransact.uicomponent.CPanel panProcess;
    private com.see.truetransact.uicomponent.CPanel panProcessButton;
    private com.see.truetransact.uicomponent.CPanel panProductDetails;
    private com.see.truetransact.uicomponent.CPanel panProductID;
    private com.see.truetransact.uicomponent.CPanel panProductIdMain;
    private com.see.truetransact.uicomponent.CPanel panProductTableData;
    private com.see.truetransact.uicomponent.CPanel panSelectAll;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panToAccount2;
    private com.see.truetransact.uicomponent.CRadioButton rdoPenaltyRateApplicableNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoPenaltyRateApplicableYes;
    private com.see.truetransact.uicomponent.CScrollPane srpDepositInterestApplication;
    private com.see.truetransact.uicomponent.CTable tblDepositMultiClosing;
    private com.see.truetransact.uicomponent.CTextField txtClosingCustomerIdCr;
    private com.see.truetransact.uicomponent.CTextField txtCustID;
    private com.see.truetransact.uicomponent.CTextField txtFromAccount;
    private com.see.truetransact.uicomponent.CTextField txtProductID;
    private com.see.truetransact.uicomponent.CTextField txtToAccount;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        DepositMultiClosingUI fad = new DepositMultiClosingUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(fad);
        j.show();
        fad.show();
    }
}
