/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * NewBorrowing.java
 *
 * Created on September 12, 2011, 12:08 PM
 */

package com.see.truetransact.ui.borrowings;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import java.text.*;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import java.util.Observable;
import java.math.BigDecimal;
import com.see.truetransact.clientutil.ClientUtil;
import java.util.Observer;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.ui.common.viewall.ViewAll;
import java.util.ResourceBundle;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import java.util.Date;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CButtonGroup;
//trans details
import com.see.truetransact.ui.common.transaction.TransactionUI;
import java.util.LinkedHashMap;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
//end..
/**
 *
 * @author  userdd
 */
public class NewBorrowing extends CInternalFrame implements Observer, UIMandatoryField{
    private boolean updateMode = false;
    int updateTab=-1;
    private NewBorrowingOB observable;
    private String[][] tabledata;
    private String[] column;
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    private int rejectFlag=0;
    AuthorizeListDebitUI ManagerauthorizeListUI=null;
    AuthorizeListCreditUI CashierauthorizeListUI=null;
    private NewBorrowingMRB objMandatoryRB = new NewBorrowingMRB();//Instance for the MandatoryResourceBundle
    private DefaultTableModel model;
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private final String AUTHORIZE="Authorize";//Variable used when btnAuthorize is clicked
    TransactionUI transactionUI = new TransactionUI(); //trans details
    double amtBorrow=0.0; //trans details
    /** Creates new form ifrNewBorrowing */
    public NewBorrowing() {
        initComponents();
        setFieldNames();
        setObservable();
        observable.resetForm();
        setMaxLengths();
        initComponentData();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panBorrowing, getMandatoryHashMap());
        chkGovtLoanActionPerformed(null);
        //trans details
        panTrans.add(transactionUI);
       
        
        transactionUI.setSourceScreen("NEW_BORROW");
        observable.setTransactionOB(transactionUI.getTransactionOB());
        //end..
        ClientUtil.enableDisable(panBorrowing, false);
        setButtonEnableDisable();
        tdtDateExpiry.setEnabled(false);
        panCheckBookTable.setVisible(false);
        transactionUI.setProdType();
        System.out.println("dsgdfgdfgdf");
        
    }
    public void setButtons(boolean flag) {
        btnViewPrin.setEnabled(flag);
        btnViewInt.setEnabled(flag);
        btnViewPen.setEnabled(flag);
        btnViewCharg.setEnabled(flag);
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
        tbrBorrowings.setName("tbrBorrowings");
        lblAgency.setName("lblAgency");
        lblBorrowRefNo.setName("lblBorrowRefNo");
        lblBorrNo.setName("lblBorrNo");
        lblType.setName("lblType");
        lblDescription.setName("lblDescription");
        lblDteSanctioned.setName("lblDteSanctioned");
        lblAmtSanctioned.setName("lblAmtSanctioned");
        //lblAmtBorred.setName("lblAmtBorred");
        lblRateofInt.setName("lblRateofInt");
        lblNoOfInstall.setName("lblNoOfInstall");
        lblPrinRepFrq.setName("lblPrinRepFrq");
        lblIntRepfrq.setName("lblIntRepfrq");
        lblMorotorium.setName("lblMorotorium");
        lblSanctExpDate.setName("lblSanctExpDate");
        lblSecDetails.setName("lblSecDetails");
        lblPrincGrpHead.setName("lblPrincGrpHead");
        lblIntGrpHead.setName("lblIntGrpHead");
        lblPenalGrpHead.setName("lblPenalGrpHead");
        lblChargGrpHead.setName("lblChargGrpHead");
        txtAmount.setName("txtAmount");
        
        cboAgency.setName("cboAgency");
        txtBorrowingRefNo.setName("txtBorrowingRefNo");
        //txtBorrowingNo.setName("txtBorrowingNo");
        cboType.setName("cboType");
        txtaDescription.setName("txtaDescription");
        tdtDateSanctioned.setName("tdtDateSanctioned");
        txtAmtSanctioned.setName("txtAmtSanctioned");
        //  txtAmtBorrowed.setName("txtAmtBorrowed");
        txtRateInterest.setName("txtRateInterest");
        txtnoofInstall.setName("txtnoofInstall");
        cboPrinRepFrq.setName("cboPrinRepFrq");
        cboIntRepFrq.setName("cboIntRepFrq");
        txtMorotorium.setName("txtMorotorium");
        panBorrowing.setName("panBorrowing");
        txaSecurityDet.setName("txaSecurityDet");
        txtprinGrpHead.setName("txtprinGrpHead");
        txtintGrpHead.setName("txtintGrpHead");
        txtpenalGrpHead.setName("txtpenalGrpHead");
        txtchargeGrpHead.setName("txtchargeGrpHead");
        
        btnViewPrin.setName("btnViewPrin");
        btnViewInt.setName("btnViewInt");
        btnViewPen.setName("btnViewPen");
        btnViewCharg.setName("btnViewCharg");
        lblPenalIntRate.setName("lblPenalIntRate");
        txtPenIntRate.setName("txtPenIntRate");
        cboMultidis.setName("cboMultidis");
        cboRenRep.setName("cboRenRep");
        chkGovtLoan.setName("chkGovtLoan");
    }
    private void setMaxLengths() {
        // txtUserID.setAllowAll(true);
        // txtPwd.setAllowAll(true);
        //  this.setBounds(0,0, 800, 700);
        txtToNO.setValidation(new NumericValidation());
        txtFromNO.setValidation(new NumericValidation());
        txtAmtSanctioned.setValidation(new CurrencyValidation());
         //modified by Anju 14/05/2014       
        txtAmount.setValidation(new CurrencyValidation());
        //   txtAmtBorrowed.setValidation(new CurrencyValidation());
        txtRateInterest.setValidation(new NumericValidation(4,2));
        txtPenIntRate.setValidation(new NumericValidation(4,2));
        txtnoofInstall.setValidation(new NumericValidation());
        txtMorotorium.setValidation(new NumericValidation());
        //  txtAmtSanctioned.setValidation(new CurrencyValidation());
    }
    private void setObservable() {
        try{
            observable = NewBorrowingOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            //parseException.logException(e,true);
            System.out.println("Error in setObservable():"+e);
        }
    }
    //cheque details
    private Date curDate = null;
    private String cDate = "";
    private void initComponentData() {
        //cheque details
        // panCheckBookTable.setEnabled(false);
        curDate=ClientUtil.getCurrentDate();
        cDate=CommonUtil.convertObjToStr(curDate);
        tdtDateSanctioned.setDateValue(cDate);
        tdtDateSanctioned.setEnabled(false);
        
        try{
            cboAgency.setModel(observable.getCbmBrAgency());
            cboType.setModel(observable.getCbmBrType());
            cboPrinRepFrq.setModel(observable.getCbmBrPrinRepFrq());
            cboIntRepFrq.setModel(observable.getCbmIntRepFrq());
            cboRenRep.setEnabled(false);
            setButtons(false);
        }catch(ClassCastException e){
            //parseException.logException(e,true);
            System.out.println("Error in initComponentData():"+e);
        }
    }
    
    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
        System.out.println("vivi..............");
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
        viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
        viewType.equals(ClientConstants.ACTION_STATUS[17])) {
            if(viewType.equals(ClientConstants.ACTION_STATUS[3])){
                viewMap.put(CommonConstants.MAP_NAME, "getSelectBorrowingsDeleteList");
            }else{
                viewMap.put(CommonConstants.MAP_NAME, "Borrowings.getSelectBorrowingsList");
            }
            ArrayList lst = new ArrayList();
            lst.add("BORROWING_NO");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
        }
        else {
            viewMap.put(CommonConstants.MAP_NAME, "Borrowings.getSelectAcctHeadTOList");
        }
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        viewMap.put(CommonConstants.MAP_WHERE, where);
        where = null;
        new ViewAll(this,viewMap).show();
        // new ViewAll(this,
    }
    public void fillData(Object  map) {
        
        //        setModified(true);
        HashMap hash = (HashMap) map;
        if(viewType.equals("PRICIPAL_GROUP_HEAD")) {
            txtprinGrpHead.setText(hash.get("AC_HD_ID").toString());
        }
        if(viewType.equals("INT_GROUP_HEAD")) {
            txtintGrpHead.setText(hash.get("AC_HD_ID").toString());
        }
        if(viewType.equals("PENAL_GROUP_HEAD")) {
            txtpenalGrpHead.setText(hash.get("AC_HD_ID").toString());
        }
        if(viewType.equals("CHARGES_GROUP_HEAD")) {
            txtchargeGrpHead.setText(hash.get("AC_HD_ID").toString());
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
                rejectFlag=1;
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
                rejectFlag=1;
            }
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                HashMap where = new HashMap();
                //System.out.println("hash.get"+hash.get("BORROWING_NO"));
                where.put("BORROWING_NO", hash.get("BORROWING_NO"));
                // where.put(CommonConstants.BRANCH_ID, "0001");
                hash.put(CommonConstants.MAP_WHERE, where);
                observable.populateData(hash);
                
                // fillTxtNoOfTokens();
                if (observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW) {
                    ClientUtil.enableDisable(panBorrowing, false);
                }else  if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                    ClientUtil.enableDisable(panBorrowing, true);
                    btnSave.setEnabled(true);
                }
                if(viewType.equals(AUTHORIZE)){
                    ClientUtil.enableDisable(panBorrowing, false);
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
                setButtonEnableDisable();
                if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                    btnSave.setEnabled(true);
                }
            }
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoApplType = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrBorrowings = new com.see.truetransact.uicomponent.CToolBar();
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
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panBorrowing = new com.see.truetransact.uicomponent.CPanel();
        panTrans = new com.see.truetransact.uicomponent.CPanel();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        lblPenalGrpHead = new com.see.truetransact.uicomponent.CLabel();
        lblAmount = new javax.swing.JLabel();
        cboPrinRepFrq = new com.see.truetransact.uicomponent.CComboBox();
        txtprinGrpHead = new com.see.truetransact.uicomponent.CTextField();
        txtPenIntRate = new com.see.truetransact.uicomponent.CTextField();
        lblIntGrpHead = new com.see.truetransact.uicomponent.CLabel();
        tdtDateExpiry = new com.see.truetransact.uicomponent.CDateField();
        lblCheckBookAllowed = new com.see.truetransact.uicomponent.CLabel();
        lblChargGrpHead = new com.see.truetransact.uicomponent.CLabel();
        btnViewPrin = new com.see.truetransact.uicomponent.CButton();
        txtAmtSanctioned1 = new com.see.truetransact.uicomponent.CTextField();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        lblType = new com.see.truetransact.uicomponent.CLabel();
        lblDteSanctioned = new com.see.truetransact.uicomponent.CLabel();
        txtAmtSanctioned = new com.see.truetransact.uicomponent.CTextField();
        btnViewInt = new com.see.truetransact.uicomponent.CButton();
        cboType = new com.see.truetransact.uicomponent.CComboBox();
        lblAgency = new com.see.truetransact.uicomponent.CLabel();
        txtchargeGrpHead = new com.see.truetransact.uicomponent.CTextField();
        lblSecDetails = new com.see.truetransact.uicomponent.CLabel();
        txtintGrpHead = new com.see.truetransact.uicomponent.CTextField();
        lblNoOfInstall = new com.see.truetransact.uicomponent.CLabel();
        btnViewPen = new com.see.truetransact.uicomponent.CButton();
        txtBorrowingRefNo = new com.see.truetransact.uicomponent.CTextField();
        txtpenalGrpHead = new com.see.truetransact.uicomponent.CTextField();
        txtnoofInstall = new com.see.truetransact.uicomponent.CTextField();
        lblBorrNo = new com.see.truetransact.uicomponent.CLabel();
        tdtDateSanctioned = new com.see.truetransact.uicomponent.CDateField();
        lblBorrowRefNo = new com.see.truetransact.uicomponent.CLabel();
        cboMultidis = new com.see.truetransact.uicomponent.CCheckBox();
        cboAgency = new com.see.truetransact.uicomponent.CComboBox();
        lblAmtSanctioned1 = new com.see.truetransact.uicomponent.CLabel();
        lblRateofInt = new com.see.truetransact.uicomponent.CLabel();
        txtBorrowingNo = new com.see.truetransact.uicomponent.CTextField();
        lblIntRepfrq = new com.see.truetransact.uicomponent.CLabel();
        lblSanctExpDate = new com.see.truetransact.uicomponent.CLabel();
        btnViewCharg = new com.see.truetransact.uicomponent.CButton();
        lblAmtSanctioned = new com.see.truetransact.uicomponent.CLabel();
        lblPrincGrpHead = new com.see.truetransact.uicomponent.CLabel();
        srpaSecurityDet = new com.see.truetransact.uicomponent.CScrollPane();
        txaSecurityDet = new com.see.truetransact.uicomponent.CTextArea();
        txtMorotorium = new com.see.truetransact.uicomponent.CTextField();
        panCheckBookAllowed = new com.see.truetransact.uicomponent.CPanel();
        rdoCheckBookAllowed_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCheckBookAllowed_no = new com.see.truetransact.uicomponent.CRadioButton();
        lblPrinRepFrq = new com.see.truetransact.uicomponent.CLabel();
        lblMorotorium = new com.see.truetransact.uicomponent.CLabel();
        cboIntRepFrq = new com.see.truetransact.uicomponent.CComboBox();
        txtRateInterest = new com.see.truetransact.uicomponent.CTextField();
        btnRenewel = new javax.swing.JButton();
        lblDescription = new com.see.truetransact.uicomponent.CLabel();
        panCheckBookTable = new com.see.truetransact.uicomponent.CPanel();
        srpCheckBookTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblCheckBookTable = new com.see.truetransact.uicomponent.CTable();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        lblFromNO = new com.see.truetransact.uicomponent.CLabel();
        txtFromNO = new com.see.truetransact.uicomponent.CTextField();
        lblToNO = new com.see.truetransact.uicomponent.CLabel();
        txtToNO = new com.see.truetransact.uicomponent.CTextField();
        lblNoOfCheques = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfCheques = new com.see.truetransact.uicomponent.CTextField();
        panCheckBookBtn = new com.see.truetransact.uicomponent.CPanel();
        btnCheckBookNew = new com.see.truetransact.uicomponent.CButton();
        btnCheckBookSave = new com.see.truetransact.uicomponent.CButton();
        btnCheckBookDelete = new com.see.truetransact.uicomponent.CButton();
        srpaDescription = new com.see.truetransact.uicomponent.CScrollPane();
        txtaDescription = new com.see.truetransact.uicomponent.CTextArea();
        cboRenRep = new com.see.truetransact.uicomponent.CCheckBox();
        lblPenalIntRate = new com.see.truetransact.uicomponent.CLabel();
        chkGovtLoan = new com.see.truetransact.uicomponent.CCheckBox();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(850, 650));
        setPreferredSize(new java.awt.Dimension(850, 650));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        tbrBorrowings.setMaximumSize(new java.awt.Dimension(527, 29));
        tbrBorrowings.setMinimumSize(new java.awt.Dimension(527, 29));
        tbrBorrowings.setPreferredSize(new java.awt.Dimension(527, 29));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
        tbrBorrowings.add(btnView);

        lbSpace3.setText("     ");
        tbrBorrowings.add(lbSpace3);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrBorrowings.add(btnNew);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBorrowings.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrBorrowings.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBorrowings.add(lblSpace52);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrBorrowings.add(btnDelete);

        lbSpace2.setText("     ");
        tbrBorrowings.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrBorrowings.add(btnSave);

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBorrowings.add(lblSpace53);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrBorrowings.add(btnCancel);

        lblSpace3.setText("     ");
        tbrBorrowings.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrBorrowings.add(btnAuthorize);

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBorrowings.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrBorrowings.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBorrowings.add(lblSpace55);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrBorrowings.add(btnReject);

        lblSpace5.setText("     ");
        tbrBorrowings.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrBorrowings.add(btnPrint);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBorrowings.add(lblSpace56);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrBorrowings.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(tbrBorrowings, gridBagConstraints);

        panBorrowing.setMaximumSize(new java.awt.Dimension(1300, 580));
        panBorrowing.setMinimumSize(new java.awt.Dimension(1300, 580));
        panBorrowing.setPreferredSize(new java.awt.Dimension(1300, 580));
        panBorrowing.setLayout(new java.awt.GridBagLayout());

        panTrans.setMaximumSize(new java.awt.Dimension(850, 225));
        panTrans.setMinimumSize(new java.awt.Dimension(850, 225));
        panTrans.setPreferredSize(new java.awt.Dimension(850, 225));
        panTrans.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panBorrowing.add(panTrans, gridBagConstraints);

        cPanel2.setMinimumSize(new java.awt.Dimension(810, 315));
        cPanel2.setPreferredSize(new java.awt.Dimension(810, 315));
        cPanel2.setLayout(new java.awt.GridBagLayout());

        lblPenalGrpHead.setText("Penal A/c Head ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        cPanel2.add(lblPenalGrpHead, gridBagConstraints);

        lblAmount.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAmount.setText("Amount");
        lblAmount.setMaximumSize(new java.awt.Dimension(74, 16));
        lblAmount.setMinimumSize(new java.awt.Dimension(74, 16));
        lblAmount.setPreferredSize(new java.awt.Dimension(74, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(lblAmount, gridBagConstraints);

        cboPrinRepFrq.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPrinRepFrq.setNextFocusableComponent(cboIntRepFrq);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(cboPrinRepFrq, gridBagConstraints);

        txtprinGrpHead.setEnabled(false);
        txtprinGrpHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtprinGrpHead.setNextFocusableComponent(txtintGrpHead);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(txtprinGrpHead, gridBagConstraints);

        txtPenIntRate.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPenIntRate.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPenIntRate.setNextFocusableComponent(txtRateInterest);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(txtPenIntRate, gridBagConstraints);

        lblIntGrpHead.setText("Interest A/c Head ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        cPanel2.add(lblIntGrpHead, gridBagConstraints);

        tdtDateExpiry.setEnabled(false);
        tdtDateExpiry.setMaximumSize(new java.awt.Dimension(100, 21));
        tdtDateExpiry.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtDateExpiry.setNextFocusableComponent(btnViewPrin);
        tdtDateExpiry.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(tdtDateExpiry, gridBagConstraints);

        lblCheckBookAllowed.setText("Chk Book Alowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        cPanel2.add(lblCheckBookAllowed, gridBagConstraints);

        lblChargGrpHead.setText("Charges A/c Head ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        cPanel2.add(lblChargGrpHead, gridBagConstraints);

        btnViewPrin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnViewPrin.setToolTipText("Search");
        btnViewPrin.setMaximumSize(new java.awt.Dimension(21, 21));
        btnViewPrin.setMinimumSize(new java.awt.Dimension(21, 21));
        btnViewPrin.setNextFocusableComponent(btnViewInt);
        btnViewPrin.setPreferredSize(new java.awt.Dimension(21, 21));
        btnViewPrin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrinGrpAction(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(btnViewPrin, gridBagConstraints);

        txtAmtSanctioned1.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmtSanctioned1.setNextFocusableComponent(txtPenIntRate);
        txtAmtSanctioned1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmtSanctioned1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(txtAmtSanctioned1, gridBagConstraints);

        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(txtAmount, gridBagConstraints);

        lblType.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(lblType, gridBagConstraints);

        lblDteSanctioned.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDteSanctioned.setText("Date of sanction ");
        lblDteSanctioned.setMaximumSize(new java.awt.Dimension(106, 16));
        lblDteSanctioned.setMinimumSize(new java.awt.Dimension(106, 16));
        lblDteSanctioned.setPreferredSize(new java.awt.Dimension(106, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(lblDteSanctioned, gridBagConstraints);

        txtAmtSanctioned.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmtSanctioned.setNextFocusableComponent(txtPenIntRate);
        txtAmtSanctioned.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmtSanctionedFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(txtAmtSanctioned, gridBagConstraints);

        btnViewInt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnViewInt.setToolTipText("Search");
        btnViewInt.setMaximumSize(new java.awt.Dimension(21, 21));
        btnViewInt.setMinimumSize(new java.awt.Dimension(21, 21));
        btnViewInt.setNextFocusableComponent(btnViewPen);
        btnViewInt.setPreferredSize(new java.awt.Dimension(21, 21));
        btnViewInt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntGrpAction(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(btnViewInt, gridBagConstraints);

        cboType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboType.setNextFocusableComponent(txtaDescription);
        cboType.setPopupWidth(120);
        cboType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(cboType, gridBagConstraints);

        lblAgency.setText("Agency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(lblAgency, gridBagConstraints);

        txtchargeGrpHead.setEnabled(false);
        txtchargeGrpHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(txtchargeGrpHead, gridBagConstraints);

        lblSecDetails.setText("Security details ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        cPanel2.add(lblSecDetails, gridBagConstraints);

        txtintGrpHead.setEnabled(false);
        txtintGrpHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtintGrpHead.setNextFocusableComponent(txtpenalGrpHead);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(txtintGrpHead, gridBagConstraints);

        lblNoOfInstall.setText("No of installment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        cPanel2.add(lblNoOfInstall, gridBagConstraints);

        btnViewPen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnViewPen.setToolTipText("Search");
        btnViewPen.setMaximumSize(new java.awt.Dimension(21, 21));
        btnViewPen.setMinimumSize(new java.awt.Dimension(21, 21));
        btnViewPen.setNextFocusableComponent(btnViewCharg);
        btnViewPen.setPreferredSize(new java.awt.Dimension(21, 21));
        btnViewPen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPenalGrpAction(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(btnViewPen, gridBagConstraints);

        txtBorrowingRefNo.setAllowAll(true);
        txtBorrowingRefNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBorrowingRefNo.setNextFocusableComponent(txtBorrowingNo);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(txtBorrowingRefNo, gridBagConstraints);

        txtpenalGrpHead.setEnabled(false);
        txtpenalGrpHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtpenalGrpHead.setNextFocusableComponent(txtchargeGrpHead);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(txtpenalGrpHead, gridBagConstraints);

        txtnoofInstall.setMinimumSize(new java.awt.Dimension(100, 21));
        txtnoofInstall.setNextFocusableComponent(cboPrinRepFrq);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(txtnoofInstall, gridBagConstraints);

        lblBorrNo.setText("Borrowing No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(lblBorrNo, gridBagConstraints);

        tdtDateSanctioned.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtDateSanctioned.setNextFocusableComponent(txtAmtSanctioned);
        tdtDateSanctioned.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(tdtDateSanctioned, gridBagConstraints);

        lblBorrowRefNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBorrowRefNo.setText("Borrowing ref No");
        lblBorrowRefNo.setMaximumSize(new java.awt.Dimension(100, 16));
        lblBorrowRefNo.setMinimumSize(new java.awt.Dimension(100, 16));
        lblBorrowRefNo.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(lblBorrowRefNo, gridBagConstraints);

        cboMultidis.setText("Mult Disbursl");
        cboMultidis.setMaximumSize(new java.awt.Dimension(102, 20));
        cboMultidis.setMinimumSize(new java.awt.Dimension(102, 20));
        cboMultidis.setPreferredSize(new java.awt.Dimension(102, 20));
        cboMultidis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMultidisActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(cboMultidis, gridBagConstraints);

        cboAgency.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAgency.setNextFocusableComponent(txtBorrowingRefNo);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(cboAgency, gridBagConstraints);

        lblAmtSanctioned1.setText("Sanction Order No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        cPanel2.add(lblAmtSanctioned1, gridBagConstraints);

        lblRateofInt.setText("Rate of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        cPanel2.add(lblRateofInt, gridBagConstraints);

        txtBorrowingNo.setEditable(false);
        txtBorrowingNo.setEnabled(false);
        txtBorrowingNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBorrowingNo.setNextFocusableComponent(cboType);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(txtBorrowingNo, gridBagConstraints);

        lblIntRepfrq.setText("Interest Repay Freq");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        cPanel2.add(lblIntRepfrq, gridBagConstraints);

        lblSanctExpDate.setText("Sanction Exp Dt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        cPanel2.add(lblSanctExpDate, gridBagConstraints);

        btnViewCharg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnViewCharg.setToolTipText("Search");
        btnViewCharg.setMaximumSize(new java.awt.Dimension(21, 21));
        btnViewCharg.setMinimumSize(new java.awt.Dimension(21, 21));
        btnViewCharg.setNextFocusableComponent(panTrans);
        btnViewCharg.setPreferredSize(new java.awt.Dimension(21, 21));
        btnViewCharg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChargesGrpAction(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(btnViewCharg, gridBagConstraints);

        lblAmtSanctioned.setText("Sanction Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(lblAmtSanctioned, gridBagConstraints);

        lblPrincGrpHead.setText("Principal A/c Head ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        cPanel2.add(lblPrincGrpHead, gridBagConstraints);

        srpaSecurityDet.setFocusable(false);
        srpaSecurityDet.setMaximumSize(new java.awt.Dimension(100, 40));
        srpaSecurityDet.setMinimumSize(new java.awt.Dimension(100, 40));
        srpaSecurityDet.setPreferredSize(new java.awt.Dimension(100, 40));

        txaSecurityDet.setNextFocusableComponent(txtMorotorium);
        srpaSecurityDet.setViewportView(txaSecurityDet);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(srpaSecurityDet, gridBagConstraints);

        txtMorotorium.setMaximumSize(new java.awt.Dimension(100, 21));
        txtMorotorium.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMorotorium.setNextFocusableComponent(tdtDateExpiry);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(txtMorotorium, gridBagConstraints);

        panCheckBookAllowed.setMinimumSize(new java.awt.Dimension(100, 21));
        panCheckBookAllowed.setPreferredSize(new java.awt.Dimension(100, 21));
        panCheckBookAllowed.setLayout(new java.awt.GridBagLayout());

        rdoCheckBookAllowed_yes.setText("Yes");
        rdoCheckBookAllowed_yes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoCheckBookAllowed_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCheckBookAllowed_yesActionPerformed(evt);
            }
        });
        panCheckBookAllowed.add(rdoCheckBookAllowed_yes, new java.awt.GridBagConstraints());

        rdoCheckBookAllowed_no.setText("No");
        rdoCheckBookAllowed_no.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoCheckBookAllowed_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCheckBookAllowed_noActionPerformed(evt);
            }
        });
        panCheckBookAllowed.add(rdoCheckBookAllowed_no, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(panCheckBookAllowed, gridBagConstraints);

        lblPrinRepFrq.setText("Principal Repay Freq");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        cPanel2.add(lblPrinRepFrq, gridBagConstraints);

        lblMorotorium.setText("Morotorium (months)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        cPanel2.add(lblMorotorium, gridBagConstraints);

        cboIntRepFrq.setMinimumSize(new java.awt.Dimension(100, 21));
        cboIntRepFrq.setNextFocusableComponent(txtMorotorium);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(cboIntRepFrq, gridBagConstraints);

        txtRateInterest.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRateInterest.setNextFocusableComponent(txtnoofInstall);
        txtRateInterest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRateInterestActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(txtRateInterest, gridBagConstraints);

        btnRenewel.setText("Renewal");
        btnRenewel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenewelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        cPanel2.add(btnRenewel, gridBagConstraints);

        lblDescription.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(lblDescription, gridBagConstraints);

        panCheckBookTable.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panCheckBookTable.setMaximumSize(new java.awt.Dimension(500, 100));
        panCheckBookTable.setMinimumSize(new java.awt.Dimension(500, 100));
        panCheckBookTable.setPreferredSize(new java.awt.Dimension(500, 100));
        panCheckBookTable.setLayout(new java.awt.GridBagLayout());

        srpCheckBookTable.setMaximumSize(new java.awt.Dimension(250, 80));
        srpCheckBookTable.setMinimumSize(new java.awt.Dimension(250, 80));
        srpCheckBookTable.setPreferredSize(new java.awt.Dimension(250, 80));

        tblCheckBookTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl No", "Issue Dt", "From No", "To  No", "Quantity"
            }
        ));
        tblCheckBookTable.setMaximumSize(new java.awt.Dimension(250, 80));
        tblCheckBookTable.setMinimumSize(new java.awt.Dimension(250, 80));
        tblCheckBookTable.setPreferredScrollableViewportSize(new java.awt.Dimension(350, 100));
        tblCheckBookTable.setPreferredSize(new java.awt.Dimension(250, 80));
        tblCheckBookTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCheckBookTableMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCheckBookTableMousePressed(evt);
            }
        });
        srpCheckBookTable.setViewportView(tblCheckBookTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 12);
        panCheckBookTable.add(srpCheckBookTable, gridBagConstraints);

        cPanel1.setMinimumSize(new java.awt.Dimension(210, 95));
        cPanel1.setPreferredSize(new java.awt.Dimension(210, 95));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        lblFromNO.setText("From No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel1.add(lblFromNO, gridBagConstraints);

        txtFromNO.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromNO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFromNOActionPerformed(evt);
            }
        });
        txtFromNO.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromNOFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel1.add(txtFromNO, gridBagConstraints);

        lblToNO.setText("To No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel1.add(lblToNO, gridBagConstraints);

        txtToNO.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToNO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtToNOActionPerformed(evt);
            }
        });
        txtToNO.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToNOFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel1.add(txtToNO, gridBagConstraints);

        lblNoOfCheques.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNoOfCheques.setText("No Of Cheques");
        lblNoOfCheques.setMaximumSize(new java.awt.Dimension(35, 18));
        lblNoOfCheques.setMinimumSize(new java.awt.Dimension(35, 18));
        lblNoOfCheques.setPreferredSize(new java.awt.Dimension(35, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel1.add(lblNoOfCheques, gridBagConstraints);

        txtNoOfCheques.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoOfCheques.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoOfChequesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel1.add(txtNoOfCheques, gridBagConstraints);

        panCheckBookBtn.setMaximumSize(new java.awt.Dimension(90, 30));
        panCheckBookBtn.setMinimumSize(new java.awt.Dimension(90, 30));
        panCheckBookBtn.setPreferredSize(new java.awt.Dimension(90, 30));
        panCheckBookBtn.setLayout(new java.awt.GridBagLayout());

        btnCheckBookNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnCheckBookNew.setToolTipText("New");
        btnCheckBookNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCheckBookNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCheckBookNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnCheckBookNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckBookNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCheckBookBtn.add(btnCheckBookNew, gridBagConstraints);

        btnCheckBookSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnCheckBookSave.setToolTipText("Save");
        btnCheckBookSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCheckBookSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCheckBookSave.setName("btnContactNoAdd"); // NOI18N
        btnCheckBookSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnCheckBookSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckBookSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCheckBookBtn.add(btnCheckBookSave, gridBagConstraints);

        btnCheckBookDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnCheckBookDelete.setToolTipText("Delete");
        btnCheckBookDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCheckBookDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCheckBookDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnCheckBookDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckBookDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCheckBookBtn.add(btnCheckBookDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel1.add(panCheckBookBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panCheckBookTable.add(cPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        cPanel2.add(panCheckBookTable, gridBagConstraints);

        srpaDescription.setFocusable(false);
        srpaDescription.setMaximumSize(new java.awt.Dimension(100, 40));
        srpaDescription.setMinimumSize(new java.awt.Dimension(100, 40));
        srpaDescription.setPreferredSize(new java.awt.Dimension(100, 40));

        txtaDescription.setNextFocusableComponent(tdtDateSanctioned);
        srpaDescription.setViewportView(txtaDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(srpaDescription, gridBagConstraints);

        cboRenRep.setText("Renewal Rqd");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(cboRenRep, gridBagConstraints);

        lblPenalIntRate.setText("Penal Int Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(lblPenalIntRate, gridBagConstraints);

        chkGovtLoan.setText("Govt Loan");
        chkGovtLoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkGovtLoanActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        cPanel2.add(chkGovtLoan, gridBagConstraints);

        panBorrowing.add(cPanel2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(panBorrowing, gridBagConstraints);

        panStatus.setMinimumSize(new java.awt.Dimension(250, 22));
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
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(panStatus, gridBagConstraints);

        mbrTokenConfig.setInheritsPopupMenu(true);

        mnuProcess.setText("Borrowing Master");
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
    
    private void cboMultidisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMultidisActionPerformed
        // TODO add your handling code here:
        //Added by Anju 14/05/2014
       if(cboMultidis.isSelected()==true){
            txtAmount.setText(txtAmtSanctioned.getText());
            txtAmount.enable(true);
            lblAmount.setText("Disbursl Amount");
        }else if(cboMultidis.isSelected()==false){
            txtAmount.setText("");
            txtAmount.setText(txtAmtSanctioned.getText());
            lblAmount.setText("Amount");
            txtAmount.enable(false);
        }
    }//GEN-LAST:event_cboMultidisActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
        System.out.println("btnPrintActionPerformed ====== "+getScreenID());
        
    }//GEN-LAST:event_btnPrintActionPerformed
            
    private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    //trans details
    private void txtToNOFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToNOFocusLost
        // TODO add your handling code here:
        if(txtToNO.getText().length()>0){
            if(CommonUtil.convertObjToDouble(txtToNO.getText()).doubleValue() < CommonUtil.convertObjToDouble(txtFromNO.getText()).doubleValue()){
                ClientUtil.showMessageWindow("To No Should be Greater Than From No");
                txtNoOfCheques.setText("");
                txtToNO.setText("");
            }else{
                int noOfCheques = 0;
                noOfCheques = CommonUtil.convertObjToInt(txtToNO.getText()) - CommonUtil.convertObjToInt(txtFromNO.getText());
                noOfCheques+=1;
                txtNoOfCheques.setText(String.valueOf(noOfCheques));
            }
        }
    }//GEN-LAST:event_txtToNOFocusLost
    
    private void txtFromNOFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromNOFocusLost
        // TODO add your handling code here:
        if(txtFromNO.getText().length()>0){
            if(tblCheckBookTable.getRowCount()>0){
                int currentFromNo = CommonUtil.convertObjToInt(txtFromNO.getText());
                for(int i=0; i<tblCheckBookTable.getRowCount();i++){
                    int fromNo = 0;
                    int toNo = 0;
                    fromNo = CommonUtil.convertObjToInt(tblCheckBookTable.getValueAt(i,2));
                    toNo = CommonUtil.convertObjToInt(tblCheckBookTable.getValueAt(i,3));
                    if(fromNo<=currentFromNo && currentFromNo<=toNo){
                        ClientUtil.showMessageWindow("Cheque No Already Issued !!!");
                        txtFromNO.setText("");
                    }
                }
            }
            txtToNO.setText("");
            txtNoOfCheques.setText("");
        }
    }//GEN-LAST:event_txtFromNOFocusLost
    
    private void txtNoOfChequesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoOfChequesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoOfChequesActionPerformed
    
    private void btnCheckBookDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckBookDeleteActionPerformed
        // TODO add your handling code here:
        String s=  CommonUtil.convertObjToStr(tblCheckBookTable.getValueAt(tblCheckBookTable.getSelectedRow(),0));
        observable.deleteTableData(s,tblCheckBookTable.getSelectedRow());
        observable.resetChequeDetails();
        resetChequeDetails();
        ClientUtil.enableDisable(panCheckBookTable,false);
        enableDisableButton(false);
        btnCheckBookNew.setEnabled(true);
    }//GEN-LAST:event_btnCheckBookDeleteActionPerformed
    
    private void txtToNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtToNOActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtToNOActionPerformed
    
    private void tblCheckBookTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCheckBookTableMousePressed
        // TODO add your handling code here:
        updateChequeBookOBFields();
        updateMode = true;
        updateTab= tblCheckBookTable.getSelectedRow();
        observable.setNewData(false);
        String st=CommonUtil.convertObjToStr(tblCheckBookTable.getValueAt(tblCheckBookTable.getSelectedRow(),0));
        observable.populateChequeDetails(st);
        populateChequeDetails();
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE||observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW || observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE ||
        observable.getActionType()==ClientConstants.ACTIONTYPE_EXCEPTION){
            enableDisableButton(false);
            ClientUtil.enableDisable(panCheckBookTable,false);
        }else {
            enableDisableButton(true);
            btnCheckBookNew.setEnabled(false);
            ClientUtil.enableDisable(panCheckBookTable,true);
            txtNoOfCheques.setEnabled(false);
           /* if(statusType.equals("AUTHORIZED")){
                enableDisableButton(false);
                ClientUtil.enableDisable(panChequeDetails,false);
            }
            */
        }
        if(observable.getActionType()!=ClientConstants.ACTIONTYPE_NEW && tblCheckBookTable.getSelectedRowCount()>0 && evt.getClickCount() == 2){
            HashMap whereMap = new HashMap();
            //     whereMap.put("BORROWING_NO", txtSBInternalAccNo.getText());
            String slNo = CommonUtil.convertObjToStr(tblCheckBookTable.getValueAt(tblCheckBookTable.getSelectedRow(), 0));
            whereMap.put("SL_NO", slNo);
            //            TableDialogUI tableData = new TableDialogUI("getInvestmentIssuedChequeDetails", whereMap);
            //            tableData.setTitle("Cheque Book Details");
            //            tableData.show();
        }
    }//GEN-LAST:event_tblCheckBookTableMousePressed
    
    private void tblCheckBookTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCheckBookTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblCheckBookTableMouseClicked
    
    private void txtFromNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFromNOActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtFromNOActionPerformed
    
    private void btnCheckBookNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckBookNewActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        observable.setNewData(true);
        enableDisableButton(false);
        btnCheckBookSave.setEnabled(true);
        ClientUtil.enableDisable(panCheckBookTable,true);
        txtNoOfCheques.setEnabled(false);
    }//GEN-LAST:event_btnCheckBookNewActionPerformed
    
    private void btnCheckBookSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckBookSaveActionPerformed
        // TODO add your handling code here:
        try{
            if(txtFromNO.getText().length()<=0){
                ClientUtil.showMessageWindow("From No Should Not be Empty !!! ");
            }else if(txtToNO.getText().length()<=0){
                ClientUtil.showMessageWindow("To No Should Not be Empty !!! ");
            }else{
                updateChequeBookOBFields();
                observable.addToTable(updateTab,updateMode);
                //System.out.println("row count..."+observable.getTblCheckBookTable().getRowCount());
                tblCheckBookTable.setModel(observable.getTblCheckBookTable());
                observable.resetChequeDetails();
                resetChequeDetails();
                ClientUtil.enableDisable(panCheckBookTable,false);
                enableDisableButton(false);
                btnCheckBookNew.setEnabled(true);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCheckBookSaveActionPerformed
    
    private void rdoCheckBookAllowed_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCheckBookAllowed_yesActionPerformed
        
        if(rdoCheckBookAllowed_yes.isSelected() == true){
            panCheckBookTable.setVisible(true);
            btnCheckBookNew.setEnabled(true);
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoCheckBookAllowed_yesActionPerformed
    
    private void rdoCheckBookAllowed_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCheckBookAllowed_noActionPerformed
        if(rdoCheckBookAllowed_no.isSelected() == true){
            if(tblCheckBookTable.getRowCount()>0){
                ClientUtil.showMessageWindow("First Delete Cheque Book Details");
                rdoCheckBookAllowed_yes.setSelected(true);
            }else{
                panCheckBookTable.setVisible(false);
                btnCheckBookNew.setEnabled(false);
                ClientUtil.enableDisable(panCheckBookTable,false);
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoCheckBookAllowed_noActionPerformed
    
   /* private void txtFromNOFocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        if(txtFromNO.getText().length()>0){
            if(tblCheckBookTable.getRowCount()>0){
                int currentFromNo = CommonUtil.convertObjToInt(txtFromNO.getText());
                for(int i=0; i<tblCheckBookTable.getRowCount();i++){
                    int fromNo = 0;
                    int toNo = 0;
                    fromNo = CommonUtil.convertObjToInt(tblCheckBookTable.getValueAt(i,2));
                    toNo = CommonUtil.convertObjToInt(tblCheckBookTable.getValueAt(i,3));
                    if(fromNo<=currentFromNo && currentFromNo<=toNo){
                        ClientUtil.showMessageWindow("Cheque No Already Issued !!!");
                        txtFromNO.setText("");
                    }
                }
            }
            txtToNO.setText("");
            txtNoOfCheques.setText("");
        }
    }
    */
    /* private void rdoRenewal_noActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if(rdoRenewal_no.isSelected() == true){
            panWithInterestYesNo.setVisible(false);
            btnSave.setEnabled(false);
            ClientUtil.enableDisable(panInsideDepositDetails,false);
        }
    }
     
    private void rdoRenewal_yesActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        long daydiff=0;
        java.util.Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFDMaturityDt.getDateValue()));
        daydiff = DateUtil.dateDiff(matDate, curDate);
        System.out.println("#############  daydiff "+daydiff);
        if(daydiff>=0){
            if(rdoRenewal_yes.isSelected() == true){
                panWithInterestYesNo.setVisible(true);
                btnSave.setEnabled( true);
            }else{
                btnSave.setEnabled(false);
            }
            String behaves=observable.callForBehaves();
            if(behaves.equals("OTHER_BANK_FD")){
                rdoWithInterest_no.setSelected(true);
                ClientUtil.enableDisable(panWithInterestYesNo,false);
                txtFDPricipalAmt.setEnabled(true);
                ClientUtil.enableDisable(panFDInvestmentPeriod,true);
                cboFDInterestPaymentFrequency.setEnabled(true);
                txtFDRateOfInt.setEnabled(true);
                txtFDInterestReceivable.setEnabled(true);
                tdtFDEffectiveDt.setDateValue(tdtFDMaturityDt.getDateValue());
                tdtFDEffectiveDt.setEnabled(true);
            }else if(behaves.equals("OTHER_BANK_CCD")){
                ClientUtil.enableDisable(panWithInterestYesNo,true);
            }
        }else{
            rdoRenewal_no.setSelected(true);
           ClientUtil.showMessageWindow("Can not Renew the Deposit, as the Maturity Date is : "+DateUtil.getStringDate(matDate));
     
        }
    }
     */
    
  /*  private void tblCheckBookTableMousePressed(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        updateChequeBookOBFields();
   updateMode = true;
        updateTab= tblCheckBookTable.getSelectedRow();
        observable.setNewData(false);
        String st=CommonUtil.convertObjToStr(tblCheckBookTable.getValueAt(tblCheckBookTable.getSelectedRow(),0));
        observable.populateChequeDetails(st);
        populateChequeDetails();
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE||observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW || observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE ||
        observable.getActionType()==ClientConstants.ACTIONTYPE_EXCEPTION){
            enableDisableButton(false);
            ClientUtil.enableDisable(panCheckBookTable,false);
        }else {
            enableDisableButton(true);
            btnCheckBookNew.setEnabled(false);
           ClientUtil.enableDisable(panCheckBookTable,true);
            txtNoOfCheques.setEnabled(false);
     /*      if(statusType.equals("AUTHORIZED")){
                enableDisableButton(false);
                ClientUtil.enableDisable(panCheckBookTable,false);
            }
   
   
        }
        if(observable.getActionType()!=ClientConstants.ACTIONTYPE_NEW && tblCheckBookTable.getSelectedRowCount()>0 && evt.getClickCount() == 2){
            HashMap whereMap = new HashMap();
        //4   whereMap.put("BORROWING_NO", txtSBInternalAccNo.getText());
            String slNo = CommonUtil.convertObjToStr(tblCheckBookTable.getValueAt(tblCheckBookTable.getSelectedRow(), 0));
            whereMap.put("SL_NO", slNo);
          //5  TableDialogUI tableData = new TableDialogUI("getInvestmentIssuedChequeDetails", whereMap);
          //6  tableData.setTitle("Cheque Book Details");
//        //7    tableData.show();
        }
    }
   
    private void txtToNOFocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        if(txtToNO.getText().length()>0){
            if(CommonUtil.convertObjToDouble(txtToNO.getText()).doubleValue() < CommonUtil.convertObjToDouble(txtFromNO.getText()).doubleValue()){
                ClientUtil.showMessageWindow("To No Should be Greater Than From No");
                txtNoOfCheques.setText("");
                txtToNO.setText("");
            }else{
                int noOfCheques = 0;
                noOfCheques = CommonUtil.convertObjToInt(txtToNO.getText()) - CommonUtil.convertObjToInt(txtFromNO.getText());
                noOfCheques+=1;
                txtNoOfCheques.setText(String.valueOf(noOfCheques));
            }
        }
    }
   
   
   
   
    private void btnCheckBookDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        String s=  CommonUtil.convertObjToStr(tblCheckBookTable.getValueAt(tblCheckBookTable.getSelectedRow(),0));
        observable.deleteTableData(s,tblCheckBookTable.getSelectedRow());
        observable.resetChequeDetails();
        resetChequeDetails();
//8        ClientUtil.enableDisable(panChequeDetails,false);
        enableDisableButton(false);
        btnCheckBookNew.setEnabled(true);
    }
   
    private void btnCheckBookSaveActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        try{
            if(txtFromNO.getText().length()<=0){
                ClientUtil.showMessageWindow("From No Should Not be Empty !!! ");
            }else if(txtToNO.getText().length()<=0){
                ClientUtil.showMessageWindow("To No Should Not be Empty !!! ");
            }else{
                updateChequeBookOBFields();
                observable.addToTable(updateTab,updateMode);
                tblCheckBookTable.setModel(observable.getTblCheckBookTable());
                observable.resetChequeDetails();
                resetChequeDetails();
//9                ClientUtil.enableDisable(panChequeDetails,false);
                enableDisableButton(false);
                btnCheckBookNew.setEnabled(true);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
   
    private void btnCheckBookNewActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        updateMode = false;
        observable.setNewData(true);
        enableDisableButton(false);
        btnCheckBookSave.setEnabled(true);
//10        ClientUtil.enableDisable(panChequeDetails,true);
        txtNoOfCheques.setEnabled(false);
    }
   */
    
    private void resetChequeDetails(){
        txtFromNO.setText("");
        txtNoOfCheques.setText("");
        txtToNO.setText("");
    }
    
    public void updateChequeBookOBFields() {
        //11        observable.setTxtSBInternalAccNo(txtSBInternalAccNo.getText());
        observable.setTdtChequeIssueDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(curDate)));
        System.out.println("ghjghjgh"+txtFromNO.getText());
        observable.setTxtFromNO(txtFromNO.getText());
        observable.setTxtToNO(txtToNO.getText());
        observable.setTxtNoOfCheques(txtNoOfCheques.getText());
    }
    
    public void populateChequeDetails() {
        txtFromNO.setText(observable.getTxtFromNO());
        txtToNO.setText(observable.getTxtToNO());
        txtNoOfCheques.setText(observable.getTxtNoOfCheques());
    }
    
    private void enableDisableButton(boolean flag){
        btnCheckBookNew.setEnabled(flag);
        btnCheckBookSave.setEnabled(flag);
        btnCheckBookDelete.setEnabled(flag);
    }
    
    
    private void btnRenewelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenewelActionPerformed
        // TODO add your handling code here:
        System.out.println("btnRenewelActionPerformed98876567576567576567566666666666666666");
        ifrRenewal ifrRenewal=new ifrRenewal();
        //        getContentPane().add(ifrRenewal);
        //      ifrRenewal.setBounds(160,10,38,33);
        ifrRenewal.show();
        ifrRenewal.setVisible(true);
    }//GEN-LAST:event_btnRenewelActionPerformed
    
    
    public static void main(String args[]) throws Exception {
        try {
            NewBorrowing objIfrRenewal=new NewBorrowing();
            objIfrRenewal.setVisible(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
        
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        
         cifClosingAlert();
//        this.dispose();
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
        setButtons(false);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        setButtons(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        setButtons(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            //            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID,TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);
            whereMap.put("TRANS_DT", curDate.clone());
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            if(TrueTransactMain.CASHIER_AUTH_ALLOWED!=null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y"))
            { 
                mapParam.put(CommonConstants.MAP_NAME, "getBorrowingCashierAuthorizeList");
            }else{
            mapParam.put(CommonConstants.MAP_NAME, "getBorrowingAuthorizeList");
            }
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeBorrowing");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeBorrowingMasterDisbursal");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            observable.setStatus();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)){
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            //    HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTH_DT",ClientUtil.getCurrentDateWithTime());
            //   singleAuthorizeMap.put("DEPR_BATCH_ID", observable.getDeprBatchId());
            singleAuthorizeMap.put("PAN","DEPR");
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);//commented on 13-Feb-2012
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, curDate.clone());
            singleAuthorizeMap.put("BORROWING_NO", txtBorrowingNo.getText());
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
            ClientUtil.execute("authorizeBorrowing", singleAuthorizeMap);
            ClientUtil.execute("authorizeBorrowingMasterDisbursal", singleAuthorizeMap);
            //                singleAuthorizeMap.put("DEPRECIATION_DATE",ClientUtil.getCurrentDateWithTime());
            //                singleAuthorizeMap.put("BRANCH_CODE", observable.getDeprBranchCode());
            arrList.add(singleAuthorizeMap);
            
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            
            observable.set_authorizeMap(authorizeMap);
            observable.setAuthMap(authorizeMap);
            if(observable.getChkGovtLoan()!=null && observable.getChkGovtLoan().equals("N")){
                if(transactionUI.getOutputTO().size()>0){
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                }
            }
            observable.execute(authorizeStatus);
            
            // ClientUtil.ex
            viewType = "";
            //            super.setOpenForEditBy(observable.getStatusBy());
            //            super.removeEditLock(txtTokenConfigId.getText());
            btnCancelActionPerformed(null);
            //  lblStatus.setText(authorizeStatus);
        }
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setModified(false);
        removeEditLock(txtBorrowingNo.getText());
        observable.resetForm();
        observable.resetTableValues();
        //txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panBorrowing, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        lblAmount.setText("Amount");
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        setButtons(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
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
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        //   System.out.println("IN btnSaveActionPerformed");
        setModified(false);
        final String mandatoryMessage = checkMandatory(panBorrowing);
        StringBuffer message = new StringBuffer(mandatoryMessage);
        if(cboAgency.getSelectedItem().equals("")) {
            message.append(objMandatoryRB.getString("cboAgency"));
        }
        if(txtBorrowingRefNo.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtBorrowingRefNo"));
        }if(txtAmount.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtAmount"));
        }
        if(cboType.getSelectedItem().equals("")) {
            message.append(objMandatoryRB.getString("cboType"));
        }
        if(txtaDescription.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtaDescription"));
        }
        if(tdtDateSanctioned.getDateValue().equals("")) {
            message.append(objMandatoryRB.getString("tdtDateSanctioned"));
        }
        if(txtAmtSanctioned.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtAmtSanctioned"));
        }
      /*   if(txtAmtBorrowed.getText().equals(""))
        {
                message.append(objMandatoryRB.getString("txtAmtBorrowed"));
         }*/
        if(txtRateInterest.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtRateInterest"));
        }
        if(txtnoofInstall.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtnoofInstall"));
        }
        if(cboPrinRepFrq.getSelectedItem().equals("")) {
            message.append(objMandatoryRB.getString("cboPrinRepFrq"));
        }
        if(cboIntRepFrq.getSelectedItem().equals("")) {
            message.append(objMandatoryRB.getString("cboIntRepFrq"));
        }
        if(txtMorotorium.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtMorotorium"));
        }
        if(txaSecurityDet.getText().equals("")) {
            message.append(objMandatoryRB.getString("txaSecurityDet"));
        }
//        if(tdtDateExpiry.getDateValue().equals("")) {
//            message.append(objMandatoryRB.getString("tdtDateExpiry"));
//        }
        if(txtprinGrpHead.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtprinGrpHead"));
        }
        if(txtintGrpHead.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtintGrpHead"));
        }
    //    if(txtpenalGrpHead.getText().equals("")) {
        //    message.append(objMandatoryRB.getString("txtpenalGrpHead"));
      //  }
      // if(txtchargeGrpHead.getText().equals("")) {
       //     message.append(objMandatoryRB.getString("txtchargeGrpHead"));
    //    }
        if(!checkNumber(txtAmtSanctioned.getText())) {
            message.append(objMandatoryRB.getString("txtAmtSanctionedNo"));
        }
       /*  if(!checkNumber(txtAmtBorrowed.getText()))
         {
             message.append(objMandatoryRB.getString("txtAmtBorrowedNo"));
         }*/
        if(!checkNumber(txtRateInterest.getText())) {
            message.append(objMandatoryRB.getString("txtRateInterestNo"));
        }
        if(!checkNumber(txtnoofInstall.getText())) {
            message.append(objMandatoryRB.getString("txtnoofInstallNo"));
        }
        
      /*  if(txtSeriesNo.getText().equals(""))
        {
                message.append(objMandatoryRB.getString("txtSeriesNo"));
         }
         if((CommonUtil.convertObjToInt(txtStartingTokenNo.getText()))> (CommonUtil.convertObjToInt(txtEndingTokenNo.getText())))
         {
          message.append(objMandatoryRB.getString("txtNumber"));
         }
       //Portion is for calculating exp date
      // setExpDateOnCalculation();
       
       
       */
        //setExpDateOnCalculation();
        //  System.out.println("status saveAction: "+status);
        if(message.length() > 0 ){
            displayAlert(message.toString());
        }
        if((!tdtDateExpiry.getDateValue().equals("") && tdtDateExpiry.getDateValue()!=null) && (!tdtDateSanctioned.getDateValue().equals("") && tdtDateSanctioned.getDateValue()!=null)){
            Date d1=DateUtil.getDateMMDDYYYY(tdtDateExpiry.getDateValue());
            Date d2=DateUtil.getDateMMDDYYYY(tdtDateSanctioned.getDateValue());
            if(d1.before(d2)) {
                ClientUtil.showAlertWindow("Sanction Expiry Date should be greater than Date of Sanction");
                tdtDateExpiry.requestFocus();
                return;
            }
            
        }
        //trans details
        if(!chkGovtLoan.isSelected())
        {
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            int transactionSize = 0 ;
            if(transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0){
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
                return;
            }else {
                if(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()>0){
                    amtBorrow=CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                    //     System.out.println("txtAmtBorrowed.getText()0000000000====="+txtAmtBorrowed.getText());
                    transactionSize = (transactionUI.getOutputTO()).size();
                    if(transactionSize != 1 && CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()>0){
                        ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction") ;
                        return;
                    }else{
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    }
                }else if(transactionUI.getOutputTO().size()>0){
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                }
            }
            if(transactionSize == 0 && CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()>0){
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
                return;
            }else if(transactionSize != 0 ){
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
        } else {
            savePerformed();
        }
        //end..       
        //    System.out.println("IN btnSaveActionPerformed111");
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    //cheque details
    private void addRadioButtons() {
        rdoApplType = new CButtonGroup();
        rdoApplType.add(rdoCheckBookAllowed_yes);
        rdoApplType.add(rdoCheckBookAllowed_no);
    }
    
    private void removeRadioButtons() {
        rdoApplType.remove(rdoCheckBookAllowed_yes);
        rdoApplType.remove(rdoCheckBookAllowed_no);
    }
    
    private void savePerformed(){
        
        // System.out.println("IN savePerformed");
        String action;
        //    System.out.println("IN observable.getActionType(): "+observable.getActionType());
        //      System.out.println("IN ClientConstants.ACTIONTYPE_NEW: "+ClientConstants.ACTIONTYPE_NEW);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ){
            //     System.out.println("IN savePerformed ACTIONTYPE_NEW");
            
            action=CommonConstants.TOSTATUS_INSERT;
            saveAction(action);        
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            action=CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            action=CommonConstants.TOSTATUS_DELETE;
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null,"Are you sure, you want to Delete this Account? ", CommonConstants.WARNINGTITLE,
            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
            null, options, options[0]);
            if (yesNo==0) {
                saveAction(action);
            }else{
                btnCancelActionPerformed(null);
            }
        }
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
    public void setExpDateOnCalculation() {
        try {
            
            String sanDate=tdtDateSanctioned.getDateValue();
            // Double noOfInstallm=Double.valueOf(txtnoofInstall.getText());
            String prinRepFrq=cboPrinRepFrq.getSelectedItem().toString();
            //System.out.println("prinRepFrq HHHUUUIJIUHIJUUJ====="+prinRepFrq);
            double prinVal=0;
            if(prinRepFrq.equals("Daily")) {
                prinVal=0;
            }
            if(prinRepFrq.equals("Monthly")) {
                prinVal=1;
            }
            if(prinRepFrq.equals("Quarterly")) {
                prinVal=6;
            }
            if(prinRepFrq.equals("Yearly")) {
                prinVal=12;
            }
            if(prinRepFrq.equals("Bullet Repayment")) {
                prinVal=0;
            }
            BigDecimal moro = new BigDecimal(txtMorotorium.getText());
            BigDecimal decimal1 = new BigDecimal(txtnoofInstall.getText());
            BigDecimal decimal2 = new BigDecimal(prinVal);
            BigDecimal v1=decimal1.divide(decimal2,0);
            BigDecimal actVal=v1.add(moro);
            // int noOfdays=Integer.va
            // System.out.println("actVal *********************===="+actVal);
            // Double value=(noOfInstallm)/(Double.valueOf(prinVal));
            // System.out.println("tdtDateSanctioned *********************===="+tdtDateSanctioned.getDateValue());
            //  System.out.println("actVal *********************===="+tdtDateExpiry.getDateValue());
            String dt = tdtDateSanctioned.getDateValue();  // Start date
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(dt));
            c.add(Calendar.DATE,actVal.intValue());  // number of days to add
            dt = sdf.format(c.getTime());
            tdtDateExpiry.setDateValue(dt);
            observable.setDateExpiry(getDateValue(tdtDateExpiry.getDateValue()));
        }
        catch(Exception e) {
            System.out.println("Exception in calculate exp date :"+e);
        }
    }
    /* Calls the execute method of TerminalOB to do insertion or updation or deletion */
    private void saveAction(String status){
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        //  System.out.println("status saveAction11111: "+status);
        //       txtAmtBorrowed.
        final String mandatoryMessage = checkMandatory(panBorrowing);
        StringBuffer message = new StringBuffer(mandatoryMessage);
        if(cboAgency.getSelectedItem().equals("")) {
            message.append(objMandatoryRB.getString("cboAgency"));
        }
        if(txtBorrowingRefNo.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtBorrowingRefNo"));
        }
        if(cboType.getSelectedItem().equals("")) {
            message.append(objMandatoryRB.getString("cboType"));
        }
        if(txtaDescription.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtaDescription"));
        }
        if(tdtDateSanctioned.getDateValue().equals("")) {
            message.append(objMandatoryRB.getString("tdtDateSanctioned"));
        }
        if(txtAmtSanctioned.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtAmtSanctioned"));
        }if(txtAmount.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtAmount"));
        }
      /*   if(txtAmtBorrowed.getText().equals(""))
        {
                message.append(objMandatoryRB.getString("txtAmtBorrowed"));
         }*/
        if(txtRateInterest.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtRateInterest"));
        }
        if(txtnoofInstall.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtnoofInstall"));
        }
        if(cboPrinRepFrq.getSelectedItem().equals("")) {
            message.append(objMandatoryRB.getString("cboPrinRepFrq"));
        }
        if(cboIntRepFrq.getSelectedItem().equals("")) {
            message.append(objMandatoryRB.getString("cboIntRepFrq"));
        }
        if(txtMorotorium.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtMorotorium"));
        }
        if(txaSecurityDet.getText().equals("")) {
            message.append(objMandatoryRB.getString("txaSecurityDet"));
        }
        if(txtprinGrpHead.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtprinGrpHead"));
        }
        if(txtintGrpHead.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtintGrpHead"));
        }
      //  if(txtpenalGrpHead.getText().equals("")) {
          //  message.append(objMandatoryRB.getString("txtpenalGrpHead"));
      //  }
      //  if(txtchargeGrpHead.getText().equals("")) {
        //    message.append(objMandatoryRB.getString("txtchargeGrpHead"));
      //  }
        if(!checkNumber(txtAmtSanctioned.getText())) {
            message.append(objMandatoryRB.getString("txtAmtSanctionedNo"));
        }
       /*  if(!checkNumber(txtAmtBorrowed.getText()))
         {
             message.append(objMandatoryRB.getString("txtAmtBorrowedNo"));
         }*/
        if(!checkNumber(txtRateInterest.getText())) {
            message.append(objMandatoryRB.getString("txtRateInterestNo"));
        }
        if(!checkNumber(txtnoofInstall.getText())) {
            message.append(objMandatoryRB.getString("txtnoofInstallNo"));
        }
        
      /*  if(txtSeriesNo.getText().equals(""))
        {
                message.append(objMandatoryRB.getString("txtSeriesNo"));
         }
         if((CommonUtil.convertObjToInt(txtStartingTokenNo.getText()))> (CommonUtil.convertObjToInt(txtEndingTokenNo.getText())))
         {
          message.append(objMandatoryRB.getString("txtNumber"));
         }
       //Portion is for calculating exp date
      // setExpDateOnCalculation();
       
       
       */
        //setExpDateOnCalculation();
        //  System.out.println("status saveAction: "+status);
        if(message.length() > 0 ){
            displayAlert(message.toString());
        }else{
            updateOBFields();
            setExpDateOnCalculation();
            observable.execute(status);
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                HashMap lockMap = new HashMap();
                if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){ 
                //HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("BORROWING_NO");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                ClientUtil.showMessageWindow("Borrowing Number is : "+observable.getProxyReturnMap().get("BORROWING_NO"));
                if(observable.getChkGovtLoan()!=null && observable.getChkGovtLoan().equals("N")){
                displayTransDetail(observable.getProxyReturnMap());//trans details
                }
                //   if (observable.getProxyReturnMap()!=null) {
                //      if (observable.getProxyReturnMap().containsKey("CONFIG_ID")) {
                //          lockMap.put("CONFIG_ID", observable.getProxyReturnMap().get("CONFIG_ID"));
                //      }
                //  }
                }
                if (status==CommonConstants.TOSTATUS_UPDATE) {
                    lockMap.put("BORROWING_NO", observable.getBorrowingNo());
                    ClientUtil.showMessageWindow("Updated Successfully");
                }
                //      setEditLockMap(lockMap);
                // setEditLock();
                settings();
            }
        }
        
    }
    /** Method used to check whether the Mandatory Fields in the Form are Filled or not */
    private String checkMandatory(javax.swing.JComponent component){
        //  return new MandatoryCheck().checkMandatory(getClass().getName(), component, getMandatoryHashMap());
        return "";
        //validation error
    }
    
    /** Method used to Give a Alert when any Mandatory Field is not filled by the user */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
         removeEditLock(txtBorrowingNo.getText());
        observable.resetForm();
        btnCancelActionPerformed(null);
        //  txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panBorrowing, false);
        setButtonEnableDisable();
        observable.setResultStatus();
    }
    public Date getDateValue(String date1) {
        DateFormat formatter ;
        Date date=null ;
        try {
            System.out.println("date1 66666666666=========:"+date1);
            // String str_date=date1;
            //  formatter = new SimpleDateFormat("MM/dd/yyyy");
            //  date = (Date)formatter.parse(str_date);
            //      System.out.println("dateAFETRRR 66666666666=========:"+date);
            
            
            
            
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
            // String s1 = "2008-03-30T15:30:00.000+02:00";
            // date1 = date1.substring(0, date1.indexOf('.'));
            System.out.println("Result==> "+sdf2.format(sdf1.parse(date1)));
            date=new Date(sdf2.format(sdf1.parse(date1)));
            System.out.println("date IOOOOOOO==> "+date);
        }
        catch (ParseException e) {
            System.out.println("Error in getDateValue():"+e);
        }
        return date;
    }
    public void updateOBFields() {    
        observable.setBorrowingNo(txtBorrowingNo.getText());
        observable.setCboAgency((String)cboAgency.getSelectedItem());
        observable.setTxtBorrowingRefNo(txtBorrowingRefNo.getText());
        observable.setCboType((String)cboType.getSelectedItem());
        observable.setTxtaDescription(txtaDescription.getText());
        observable.setTxtRateInterest(Double.valueOf(txtRateInterest.getText()));
        observable.setTxtnoofInstall(Double.valueOf(txtnoofInstall.getText()));
        observable.setCboPrinRepFrq((String)cboPrinRepFrq.getSelectedItem());
        observable.setCboIntRepFrq((String)cboIntRepFrq.getSelectedItem());
        observable.setTxtMorotorium(txtMorotorium.getText());
        observable.setTxaSecurityDet(txaSecurityDet.getText());
        observable.setTxtprinGrpHead(txtprinGrpHead.getText());
        observable.setTxtintGrpHead(txtintGrpHead.getText());
        observable.setTxtpenalGrpHead(txtpenalGrpHead.getText());
        observable.setTxtchargeGrpHead(txtchargeGrpHead.getText());
        observable.setTdtDateSanctioned(tdtDateSanctioned.getDateValue());
        // observable.setDateExpiry(getDateValue(tdtDateExpiry.getDateValue()));
        observable.setAmtSanctioned(Double.valueOf(txtAmtSanctioned.getText()));
        // observable.settxtAmtBorrowed(Double.valueOf(txtAmtBorrowed.getText()));
        observable.settxtAmtBorrowed(Double.valueOf("0.0"));
        observable.setTxtAmount(Double.valueOf(txtAmount.getText()));
        observable.setTxtPenalIntRate(txtPenIntRate.getText());
        if(chkGovtLoan.isSelected())
          observable.setChkGovtLoan("Y");
        else
        observable.setChkGovtLoan("N");    
        if(cboMultidis.isSelected())
            observable.setCboMultiDisbursal("YES");
        else
            observable.setCboMultiDisbursal("NO");
        
        if(cboRenRep.isSelected())
            observable.setCboRenReq("YES");
        else
            observable.setCboRenReq("NO");
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setTxtSanctionOrderNo(txtAmtSanctioned1.getText());
        //observable.setTdtSanctionDate(getDateValue(tdtDateSanctioned1.getDateValue()));
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView( ClientConstants.ACTION_STATUS[3]);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        setModified(true);
        panCheckBookTable.setEnabled(false);
        // TODO add your handling code here:
        observable.resetForm();
        observable.resetTableValues();
        //txtNoOfTokens.setText("");
        ClientUtil.enableDisable(panBorrowing, true);
        
        // System.out.println("btnNewActionPerformed ACTION STA: "+ClientConstants.ACTIONTYPE_NEW);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
        btnViewPrin.setEnabled(true);
        btnViewInt.setEnabled(true);
        btnViewPen.setEnabled(true);
        btnViewCharg.setEnabled(true);
        tdtDateSanctioned.setDateValue(cDate);
    }//GEN-LAST:event_btnNewActionPerformed
    private void setButtonEnableDisable() {
        panCheckBookTable.setEnabled(false);
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
        lblStatus.setText(observable.getLblStatus());
        btnView.setEnabled(!btnView.isEnabled());
        
        btnViewPrin.setEnabled(btnViewPrin.isEnabled());
        btnViewInt.setEnabled(btnViewInt.isEnabled());
        btnViewPen.setEnabled(btnViewPen.isEnabled());
        btnViewCharg.setEnabled(btnViewCharg.isEnabled());
        
        txtBorrowingNo.disable();
        txtprinGrpHead.disable();
        txtpenalGrpHead.disable();
        txtintGrpHead.disable();
        txtchargeGrpHead.disable();
        txtNoOfCheques.disable();
        txtAmount.disable();
    }
    private void btnChargesGrpAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChargesGrpAction
        // TODO add your handling code here:
        //  observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        // observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView("CHARGES_GROUP_HEAD");
    }//GEN-LAST:event_btnChargesGrpAction
    
    private void btnPenalGrpAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPenalGrpAction
        // TODO add your handling code here:
        //    observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        //    observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView("PENAL_GROUP_HEAD");
    }//GEN-LAST:event_btnPenalGrpAction
    
    private void btnIntGrpAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIntGrpAction
        // TODO add your handling code here:
        //       observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        callView("INT_GROUP_HEAD");
    }//GEN-LAST:event_btnIntGrpAction
    
    private void btnPrinGrpAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrinGrpAction
        // TODO add your handling code here:
        // TODO add your handling code here:
        //  observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        // lblStatus.setText(observable.getLblStatus());
        callView("PRICIPAL_GROUP_HEAD");
        
        //   btnCheck();
    }//GEN-LAST:event_btnPrinGrpAction
    
    private void mnuProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuProcessActionPerformed
    
    private void txtRateInterestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRateInterestActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRateInterestActionPerformed

    private void txtAmtSanctioned1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmtSanctioned1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmtSanctioned1FocusLost

    private void txtAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusLost
        // TODO add your handling code here:
        transactionUI.setCallingAmount(txtAmount.getText());
        transactionUI.setCallingTransType("TRANSFER");
        double amountBorrowed = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
        //  amountBorrowed = amountBorrowed + totalAmount;
        double sanctionAmount=CommonUtil.convertObjToDouble(txtAmtSanctioned.getText()).doubleValue();
        if(amountBorrowed > sanctionAmount){
            ClientUtil.showAlertWindow("Amount Exceeds the Disbursment Limit!!");
            txtAmount.setText("");
            amountBorrowed = 0.0;
        }
     //Added by Anju 14/05/2014
         if(amountBorrowed >= sanctionAmount &&cboMultidis.isSelected()==true){
            ClientUtil.showAlertWindow("Disbursal Amount should not be greaterthan or equal to sanctioned Amount!!");
            txtAmount.setText("");
            amountBorrowed = 0.0;
        }
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingAmount(txtAmount.getText());
        transactionUI.setCallingTransType("TRANSFER");
    }//GEN-LAST:event_txtAmountFocusLost

    private void txtAmtSanctionedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmtSanctionedFocusLost
        // TODO add your handling code here:
       txtAmount.setText(txtAmtSanctioned.getText());
      //Added by Anju 14/05/2014
       if(cboMultidis.isSelected()==true){
             txtAmount.setText("");
             txtAmount.setEnabled(true); }
        if(cboMultidis.isSelected()==false){
            transactionUI.setCallingAmount(txtAmount.getText());
            transactionUI.setCallingTransType("TRANSFER");
        }
    }//GEN-LAST:event_txtAmtSanctionedFocusLost

    private void cboTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTypeActionPerformed
        // TODO add your handling code here:
        //  String cboType=cboType.getSelectedItem());
        if(cboType.getSelectedItem()!=null && cboType.getSelectedItem().equals("Cash Credit" )) {
            cboRenRep.setEnabled(true);

        }
        else {
            cboRenRep.setEnabled(false);
            cboRenRep.setSelected(false);
        }

    }//GEN-LAST:event_cboTypeActionPerformed

    private void chkGovtLoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkGovtLoanActionPerformed
        // TODO add your handling code here:
                  if (chkGovtLoan.isSelected()) {
                        ClientUtil.enableDisable(panTrans, false, false, true);
                    } else {
                        ClientUtil.enableDisable(panTrans, true, false, true);
                    }
          
    }//GEN-LAST:event_chkGovtLoanActionPerformed
    
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboAgency", new Boolean(false));
        mandatoryMap.put("txtBorrowingRefNo", new Boolean(true));
        //  mandatoryMap.put("txtBorrowingNo", new Boolean(true));
        mandatoryMap.put("cboType", new Boolean(true));
        mandatoryMap.put("txtaDescription", new Boolean(true));
        mandatoryMap.put("tdtDateSanctioned", new Boolean(true));
        mandatoryMap.put("txtAmtSanctioned", new Boolean(true));
        mandatoryMap.put("txtAmtBorrowed", new Boolean(true));
        mandatoryMap.put("txtRateInterest", new Boolean(true));
        mandatoryMap.put("txtnoofInstall", new Boolean(true));
        mandatoryMap.put("cboPrinRepFrq", new Boolean(true));
        mandatoryMap.put("cboIntRepFrq", new Boolean(true));
        mandatoryMap.put("txtMorotorium", new Boolean(true));
        mandatoryMap.put("txaSecurityDet", new Boolean(true));
        mandatoryMap.put("txtprinGrpHead", new Boolean(true));
        mandatoryMap.put("txtintGrpHead", new Boolean(true));
        mandatoryMap.put("txtpenalGrpHead", new Boolean(true));
        mandatoryMap.put("txtchargeGrpHead", new Boolean(true));
        
    }
    public java.util.HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    public String getDtPrintValue(String strDate) {
        try {
            //      System.out.println("strDate getDtPrintValue99999999999999999999999999999999999999================="+strDate);
            //create SimpleDateFormat object with source string date format
            SimpleDateFormat sdfSource = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            //parse the string into Date object
            Date date = sdfSource.parse(strDate);
            //create SimpleDateFormat object with desired date format
            SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
            //parse the date into another format
            strDate = sdfDestination.format(date);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }
   /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        //cheque details
        removeRadioButtons();
        //end..
        txtBorrowingNo.setText(observable.getBorrowingNo());
        cboAgency.setSelectedItem(observable.getCboAgency())  ;
        System.out.println("observable.gettxtAmtBorrowed()>>>"+observable.gettxtAmtBorrowed());
      //  System.out.println("observable.gettxtAmtBorrowed()@@@!!!111122>>>"+observable.getTxtAmount());
        txtAmount.setText(CommonUtil.convertObjToStr(observable.gettxtAmtBorrowed()));
        txtBorrowingRefNo.setText(observable.getTxtBorrowingRefNo());
        cboType.setSelectedItem(observable.getCboType())  ;
        txtaDescription.setText(observable.getTxtaDescription());
        if(observable.getTxtRateInterest()!=null) {
            txtRateInterest.setText(String.valueOf(observable.getTxtRateInterest()));
        }
        if(observable.getTxtnoofInstall()!=null) {
            txtnoofInstall.setText(String.valueOf(observable.getTxtnoofInstall()));
        }
        cboPrinRepFrq.setSelectedItem(observable.getCboPrinRepFrq())  ;
        cboIntRepFrq.setSelectedItem(observable.getCboIntRepFrq())  ;
        txtMorotorium.setText(observable.getTxtMorotorium());
        txaSecurityDet.setText(observable.getTxaSecurityDet());
        txtprinGrpHead.setText(observable.getTxtprinGrpHead());
        txtintGrpHead.setText(observable.getTxtintGrpHead());
        txtpenalGrpHead.setText(observable.getTxtpenalGrpHead());
        txtchargeGrpHead.setText(observable.getTxtchargeGrpHead());
        txtAmtSanctioned1.setText(observable.getTxtSanctionOrderNo());
       // tdtDateSanctioned1.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtSanctionDate())));
        if(observable.getTxtPenalIntRate()!=null) {
            txtPenIntRate.setText(String.valueOf(observable.getTxtPenalIntRate()));
        }
        //  txtPenIntRate.setText(observable.getTxtPenalIntRate());
        if(observable.getCboMultiDisbursal()!=null && observable.getCboMultiDisbursal().equals("YES")) {
            cboMultidis.setSelected(true);
        }
        else {
            cboMultidis.setSelected(false);
        }
        if(observable.getCboRenReq()!=null && observable.getCboRenReq().equals("YES")) {
            cboRenRep.setSelected(true);
        }
        else {
            cboRenRep.setSelected(false);
        }
        if(observable.getChkGovtLoan()!=null && observable.getChkGovtLoan().equals("Y"))
        {
            chkGovtLoan.setSelected(true);
        }
        else
        {
            chkGovtLoan.setSelected(false);
        }
        if(observable.getAmtSanctioned()!=null) {
            txtAmtSanctioned.setText(String.valueOf(observable.getAmtSanctioned()));
        }
        if(observable.gettxtAmtBorrowed()!=null) {
            //   txtAmount.setText(String.valueOf(observable.gettxtAmtBorrowed()));
        }
        //  SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        // String dt1=sdf.format(observable.getDateSanctioned());
        // tdtDateSanctioned.setDateValue(String.valueOf(observable.getDateSanctioned()));
        //  tdtDateExpiry.setDateValue(String.valueOf(observable.getDateExpiry()));
        if(observable.getTdtDateSanctioned()!=null) {
            tdtDateSanctioned.setDateValue(observable.getTdtDateSanctioned());
        }
        if(observable.getDateExpiry()!=null) {
            tdtDateExpiry.setDateValue(getDtPrintValue(String.valueOf(observable.getDateExpiry())));
        }
        //cheque details
        addRadioButtons();
        //end..
    }
    
    //trans details
    private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap : " +proxyResultMap);
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        //System.out.println("keeeeeeeeeeeyyy>>>>>>>>>>>"+keys[]);
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        HashMap transIdMap = new HashMap();
        HashMap transTypeMap = new HashMap();
        for (int i=0; i<keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List)proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        cashDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        cashDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                    transTypeMap.put(transMap.get("TRANS_ID"),transMap.get("TRANS_TYPE"));
                    transIdMap.put(transMap.get("TRANS_ID"),"CASH");
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("BATCH_ID");
                    }
                    transferDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Batch Id : "+transMap.get("BATCH_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        transferDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        transferDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                    transIdMap.put(transMap.get("BATCH_ID"),"TRANSFER");
                }
                transferCount++;
            }
        }
        if(cashCount>0){
            displayStr+=cashDisplayStr;
        }
        if(transferCount>0){
            displayStr+=transferDisplayStr;
        }
        ClientUtil.showMessageWindow(""+displayStr);
        
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        System.out.println("#$#$$ yesNo : "+yesNo);
        if (yesNo==0) {
            //            TTIntegration ttIntgration = null;
            //            HashMap paramMap = new HashMap();
            //            paramMap.put("TransId", transId);
            //            paramMap.put("TransDt", observable.getCurrDt());
            //            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            //            ttIntgration.setParam(paramMap);
            //            ttIntgration.integrationForPrint("ReceiptPayment", false);
            
            TTIntegration ttIntgration = null;
            HashMap printParamMap = new HashMap();
            printParamMap.put("TransDt", observable.getCurrDt());
            printParamMap.put("BranchId", ProxyParameters.BRANCH_ID);
            Object keys1[] = transIdMap.keySet().toArray();
            for (int i=0; i<keys1.length; i++) {
                printParamMap.put("TransId", keys1[i]);
                ttIntgration.setParam(printParamMap);
                if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {
                    ttIntgration.integrationForPrint("ReceiptPayment");
                } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys1[i])).equals("DEBIT")) {
                    ttIntgration.integrationForPrint("CashPayment", false);
                } else {
                    ttIntgration.integrationForPrint("CashReceipt", false);
                }
            }
        }
    }
    //end...
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnCheckBookDelete;
    private com.see.truetransact.uicomponent.CButton btnCheckBookNew;
    private com.see.truetransact.uicomponent.CButton btnCheckBookSave;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private javax.swing.JButton btnRenewel;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnViewCharg;
    private com.see.truetransact.uicomponent.CButton btnViewInt;
    private com.see.truetransact.uicomponent.CButton btnViewPen;
    private com.see.truetransact.uicomponent.CButton btnViewPrin;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CComboBox cboAgency;
    private com.see.truetransact.uicomponent.CComboBox cboIntRepFrq;
    private com.see.truetransact.uicomponent.CCheckBox cboMultidis;
    private com.see.truetransact.uicomponent.CComboBox cboPrinRepFrq;
    private com.see.truetransact.uicomponent.CCheckBox cboRenRep;
    private com.see.truetransact.uicomponent.CComboBox cboType;
    private com.see.truetransact.uicomponent.CCheckBox chkGovtLoan;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblAgency;
    private javax.swing.JLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblAmtSanctioned;
    private com.see.truetransact.uicomponent.CLabel lblAmtSanctioned1;
    private com.see.truetransact.uicomponent.CLabel lblBorrNo;
    private com.see.truetransact.uicomponent.CLabel lblBorrowRefNo;
    private com.see.truetransact.uicomponent.CLabel lblChargGrpHead;
    private com.see.truetransact.uicomponent.CLabel lblCheckBookAllowed;
    private com.see.truetransact.uicomponent.CLabel lblDescription;
    private com.see.truetransact.uicomponent.CLabel lblDteSanctioned;
    private com.see.truetransact.uicomponent.CLabel lblFromNO;
    private com.see.truetransact.uicomponent.CLabel lblIntGrpHead;
    private com.see.truetransact.uicomponent.CLabel lblIntRepfrq;
    private com.see.truetransact.uicomponent.CLabel lblMorotorium;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNoOfCheques;
    private com.see.truetransact.uicomponent.CLabel lblNoOfInstall;
    private com.see.truetransact.uicomponent.CLabel lblPenalGrpHead;
    private com.see.truetransact.uicomponent.CLabel lblPenalIntRate;
    private com.see.truetransact.uicomponent.CLabel lblPrinRepFrq;
    private com.see.truetransact.uicomponent.CLabel lblPrincGrpHead;
    private com.see.truetransact.uicomponent.CLabel lblRateofInt;
    private com.see.truetransact.uicomponent.CLabel lblSanctExpDate;
    private com.see.truetransact.uicomponent.CLabel lblSecDetails;
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
    private com.see.truetransact.uicomponent.CLabel lblToNO;
    private com.see.truetransact.uicomponent.CLabel lblType;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panBorrowing;
    private com.see.truetransact.uicomponent.CPanel panCheckBookAllowed;
    private com.see.truetransact.uicomponent.CPanel panCheckBookBtn;
    private com.see.truetransact.uicomponent.CPanel panCheckBookTable;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTrans;
    private com.see.truetransact.uicomponent.CButtonGroup rdoApplType;
    private com.see.truetransact.uicomponent.CRadioButton rdoCheckBookAllowed_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoCheckBookAllowed_yes;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpCheckBookTable;
    private com.see.truetransact.uicomponent.CScrollPane srpaDescription;
    private com.see.truetransact.uicomponent.CScrollPane srpaSecurityDet;
    private com.see.truetransact.uicomponent.CTable tblCheckBookTable;
    private com.see.truetransact.uicomponent.CToolBar tbrBorrowings;
    private com.see.truetransact.uicomponent.CDateField tdtDateExpiry;
    private com.see.truetransact.uicomponent.CDateField tdtDateSanctioned;
    private com.see.truetransact.uicomponent.CTextArea txaSecurityDet;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtAmtSanctioned;
    private com.see.truetransact.uicomponent.CTextField txtAmtSanctioned1;
    private com.see.truetransact.uicomponent.CTextField txtBorrowingNo;
    private com.see.truetransact.uicomponent.CTextField txtBorrowingRefNo;
    private com.see.truetransact.uicomponent.CTextField txtFromNO;
    private com.see.truetransact.uicomponent.CTextField txtMorotorium;
    private com.see.truetransact.uicomponent.CTextField txtNoOfCheques;
    private com.see.truetransact.uicomponent.CTextField txtPenIntRate;
    private com.see.truetransact.uicomponent.CTextField txtRateInterest;
    private com.see.truetransact.uicomponent.CTextField txtToNO;
    private com.see.truetransact.uicomponent.CTextArea txtaDescription;
    private com.see.truetransact.uicomponent.CTextField txtchargeGrpHead;
    private com.see.truetransact.uicomponent.CTextField txtintGrpHead;
    private com.see.truetransact.uicomponent.CTextField txtnoofInstall;
    private com.see.truetransact.uicomponent.CTextField txtpenalGrpHead;
    private com.see.truetransact.uicomponent.CTextField txtprinGrpHead;
    // End of variables declaration//GEN-END:variables
    //     private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.clientutil.TableModel tbModel;
}
