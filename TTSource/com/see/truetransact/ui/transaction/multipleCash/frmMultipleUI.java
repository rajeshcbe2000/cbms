/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmptransferUI.java
 *
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
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI ;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.customer.customeridchange.CustomerIdChangeOB;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.product.operativeacct.OperativeAcctProductTO;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.ViewOrgOrRespUI;
import com.see.truetransact.ui.common.viewall.ViewRespUI;
import java.util.Date;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uivalidation.*;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.ui.deposit.TermDepositUI;
import com.see.truetransact.ui.transaction.cash.CashTransactionUI;
import com.see.truetransact.ui.transaction.common.TransCommonUI;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.Date;



/** This form is used to manipulate CustomerIdChangeUI related functionality
 * @author swaroop
 */
public class frmMultipleUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField{

    private String viewType = new String();
//    private HashMap mandatoryMap;
//    private MultipleCashTransactionOB observable;
//    final String AUTHORIZE="Authorize";
//    private final static Logger log = Logger.getLogger(frmMultipleUI.class);
//    MultipleCashTransactionRB complaintRB = new MultipleCashTransactionRB();
//    private String prodType="";
//    java.util.ResourceBundle resourceBundle, objMandatoryRB;
//    String cust_type=null;
//    String txtComplaintid=null;
    
   // private String viewType = new String();
    private HashMap mandatoryMap;
    private MultipleCashTransactionOB observable;
    final String AUTHORIZE="Authorize";
     private final static Logger log = Logger.getLogger(frmMultipleUI.class);
    MultipleCashTransactionRB complaintRB = new MultipleCashTransactionRB();
    private String prodType="";
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    String cust_type=null;
    String txtComplaintid=null;
     private  Object columnNames[] = { "Sl No","Product Type","Account Head","Amount" };
     private List bufferList=new ArrayList(); 
    String prodId="";
    
    
    
    
    
    
   // int viewType = -1;
    boolean isFilled = false;
    private TransDetailsUI transDetails = null;
  //  java.util.ResourceBundle resourceBundle, objMandatoryRB;
    private TransCommonUI transCommonUI = null;
    private boolean flag = false;
    private boolean flagDeposit = false;
    private boolean flagDepAuth = false;
    private boolean flagDepLink = false;
    private boolean flagDepositAuthorize = false;
    private boolean afterSaveCancel = false;
    private CTextField txtDepositAmount;
    private double intAmtDep = 0.0;
    TermDepositUI termDepositUI;
    public String designation = "";
    private String custStatus = "";
    private String depBehavesLike = "";
    private HashMap intMap = new HashMap();
    private boolean reconcilebtnDisable = false;
    ArrayList termLoanDetails = null;
    private boolean termLoanDetailsFlag = false;
    HashMap termLoansDetailsMap = null;
    private String depPartialWithDrawalAllowed = "";
   // private final static Logger log = Logger.getLogger(CashTransactionUI.class);     //Logger
    List chqBalList = null;
    private HashMap asAndWhenMap = null;
    private long noofInstallment2 = 0;
    boolean fromAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    private double orgOrRespAmout = 0.0;
    private String orgOrRespBranchId = "";
    private String orgOrRespBranchName = "";
    private String orgBranch = "";
    private String orgBranchName = "";
    private String orgOrRespCategory = "";
    private String orgOrRespAdviceNo = "";
    private java.util.Date orgOrRespAdviceDt = null;
    private String orgOrRespTransType = "";
    private String orgOrRespDetails = "";
    ViewOrgOrRespUI vieworgOrRespUI = null;
    ViewRespUI viewRespUI = null;
    private Date currDt = null;
    
    
    
        
    /** Creates new form CustomerIdChangeUI */
    public frmMultipleUI() {
        initComponents();
        initStartUp();
    }
    
    private void initStartUp(){
        currDt = ClientUtil.getCurrentDate();
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
//        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panComplains);
         setHelpMessage();
    }
    
    private void setMaxLength() {
//           txtEmpID.setMaxLength(64);
//           txtEmpID.setValidation(new CurrencyValidation());
//         txtAccountNumber.setMaxLength(16);
//         txtAccountNumber.setAllowAll(true);
//         txtOldCustID.setAllowAll(true);
//         txtNewCustId.setAllowAll(true);
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
    
    
    
    
    
    
      private void getTableData() {
        
                        Object rowData[][] = new Object[+bufferList.size()][5];
                       //  Integer rowData[][] = new  Integer[buffer1.size()][3];
                        int j=0;
                        String d1="";
                        String d2="";
                        int i=0;
                        System.out.println("BuufferrList  "+bufferList.size());

                          
       
        
        for(i=0;i<bufferList.size();i++)
        {
            HashMap m=new HashMap();
            m=(HashMap)bufferList.get(i);
            System.out.println("iii m00001 : "+m.get("SNO"));
            rowData[i][0]=m.get("SNO").toString();
             System.out.println("iii m000011 : "+m.get("DOC_NO"));
             rowData[i][1]=m.get("DOC_NO").toString();
              rowData[i][2]=m.get("DOC_TYPE").toString();
               System.out.println("iii m0000 111: "+m.get("DOC_TYPE"));
                rowData[i][3]=m.get("DOC_DATE").toString();
                 rowData[i][4]=m.get("REG_OFFIC").toString();
        }
         System.out.println("iii m0000 222: ");
      
          
        
        
        
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        
   
   tblCash.setModel(new javax.swing.table.DefaultTableModel(rowData,columnNames){
            
        
         public boolean isCellEditable(int row, int column) {
       //Only the third column
       return false;
   }
        
        }) ;
   tblCash.setVisible(true);

            }
    
    
    
    
    
    
    
    
    
    
    
    /****************** NEW METHODS *****************/
    
//    private void updateAuthorizeStatus(String authorizeStatus) {
//        if (viewType != AUTHORIZE){
//            HashMap mapParam = new HashMap();
//            HashMap whereMap = new HashMap();
//            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
//            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
//            whereMap.put("AUTHORIZE_MODE","AUTHORIZE_MODE");
//            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
//            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
//            mapParam.put(CommonConstants.MAP_NAME, "getEmpTransferDetailsAuthorize");
//            viewType = AUTHORIZE;
//            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
//            //            isFilled = false;
//            authorizeUI.show();
//            btnSave.setEnabled(false);
//            observable.setStatus();
//            lblStatus.setText(observable.getLblStatus());
//        } else if (viewType == AUTHORIZE){
//            ArrayList arrList = new ArrayList();
//            HashMap authorizeMap = new HashMap();
//            HashMap singleAuthorizeMap = new HashMap();
//            singleAuthorizeMap.put("STATUS", authorizeStatus);
//            singleAuthorizeMap.put("EMP_TRANSFER_ID", observable.getTxtEmpTransferID());
//            singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
//            singleAuthorizeMap.put("AUTH_DT",ClientUtil.getCurrentDateWithTime());
//            singleAuthorizeMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
//            String presentBranch = ((ComboBoxModel)cboTransBran.getModel()).getKeyForSelected().toString();
//            if (presentBranch.lastIndexOf("-")!=-1)
//            presentBranch = presentBranch.substring(0,presentBranch.lastIndexOf("-"));
//            presentBranch= presentBranch.trim();
//            singleAuthorizeMap.put("PRESENT_BRANCH_CODE",presentBranch);
//            singleAuthorizeMap.put("EMP_ID", observable.getTxtEmpID());
//            arrList.add(singleAuthorizeMap);         
//            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
//            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
//            authorize(authorizeMap,observable.getTxtEmpTransferID());
//            viewType = "";
//            super.setOpenForEditBy(observable.getStatusBy());
//        }
//    }
//    
//    public void authorize(HashMap map,String id) {
//        System.out.println("Authorize Map : " + map);
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
//        System.out.println("Authorize Map : " + map);
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
    
    /************ END OF NEW METHODS ***************/
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panComplaint = new com.see.truetransact.uicomponent.CPanel();
        panMultiple = new com.see.truetransact.uicomponent.CPanel();
        panCashTransaction = new com.see.truetransact.uicomponent.CPanel();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblCash = new com.see.truetransact.uicomponent.CTable();
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
        lblInputCurrency = new com.see.truetransact.uicomponent.CLabel();
        cboInputCurrency = new com.see.truetransact.uicomponent.CComboBox();
        btnNew_Property1 = new com.see.truetransact.uicomponent.CButton();
        btnlSave_Property1 = new com.see.truetransact.uicomponent.CButton();
        btnDelete_Property1 = new com.see.truetransact.uicomponent.CButton();
        txtInputAmt = new com.see.truetransact.uicomponent.CTextField();
        lblInputAmt = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentType = new com.see.truetransact.uicomponent.CLabel();
        cboInstrumentType = new com.see.truetransact.uicomponent.CComboBox();
        lblInstrumentNo = new com.see.truetransact.uicomponent.CLabel();
        txtInstrumentNo2 = new com.see.truetransact.uicomponent.CTextField();
        tdtInstrumentDate = new com.see.truetransact.uicomponent.CDateField();
        lblInstrumentDate = new com.see.truetransact.uicomponent.CLabel();
        txtTokenNo = new com.see.truetransact.uicomponent.CTextField();
        lblTokenNo = new com.see.truetransact.uicomponent.CLabel();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        lblAmount1 = new com.see.truetransact.uicomponent.CLabel();
        txtParticulars = new com.see.truetransact.uicomponent.CTextField();
        lblParticulars = new com.see.truetransact.uicomponent.CLabel();
        txtNarration = new com.see.truetransact.uicomponent.CTextField();
        lblNarration = new com.see.truetransact.uicomponent.CLabel();
        tbrVisitorsDiary = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnDateChange = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
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

        panComplaint.setMaximumSize(new java.awt.Dimension(600, 500));
        panComplaint.setMinimumSize(new java.awt.Dimension(600, 500));
        panComplaint.setPreferredSize(new java.awt.Dimension(600, 500));
        panComplaint.setLayout(new java.awt.GridBagLayout());

        panMultiple.setMaximumSize(new java.awt.Dimension(750, 600));
        panMultiple.setMinimumSize(new java.awt.Dimension(750, 600));
        panMultiple.setPreferredSize(new java.awt.Dimension(750, 600));
        panMultiple.setLayout(new java.awt.GridBagLayout());

        panCashTransaction.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panCashTransaction.setMinimumSize(new java.awt.Dimension(700, 580));
        panCashTransaction.setPreferredSize(new java.awt.Dimension(700, 580));
        panCashTransaction.setLayout(new java.awt.GridBagLayout());

        cPanel2.setLayout(new java.awt.GridBagLayout());

        cScrollPane1.setMinimumSize(new java.awt.Dimension(600, 300));
        cScrollPane1.setPreferredSize(new java.awt.Dimension(600, 300));

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
        cScrollPane1.setViewportView(tblCash);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 13);
        cPanel2.add(cScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 2, 0);
        panCashTransaction.add(cPanel2, gridBagConstraints);

        panData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panData.setMinimumSize(new java.awt.Dimension(650, 250));
        panData.setPreferredSize(new java.awt.Dimension(650, 250));
        panData.setLayout(null);

        lblAccHd.setText("Account Head");
        panData.add(lblAccHd);
        lblAccHd.setBounds(30, 50, 88, 16);

        lblAmount.setText("Transaction Type");
        panData.add(lblAmount);
        lblAmount.setBounds(10, 90, 110, 16);

        lblProdType.setText("Product Type");
        panData.add(lblProdType);
        lblProdType.setBounds(30, 10, 82, 16);

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(130);
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        panData.add(cboProdType);
        cboProdType.setBounds(120, 10, 100, 21);

        txtAccHdId.setAllowAll(true);
        txtAccHdId.setMinimumSize(new java.awt.Dimension(100, 21));
        panData.add(txtAccHdId);
        txtAccHdId.setBounds(120, 50, 100, 21);

        btnAccHdId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccHdId.setToolTipText("Account Head");
        btnAccHdId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccHdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccHdIdActionPerformed(evt);
            }
        });
        panData.add(btnAccHdId);
        btnAccHdId.setBounds(230, 50, 21, 21);

        panTransactionType.setMinimumSize(new java.awt.Dimension(160, 23));
        panTransactionType.setPreferredSize(new java.awt.Dimension(150, 23));
        panTransactionType.setLayout(new java.awt.GridBagLayout());

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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = -2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panTransactionType.add(rdoTransactionType_Credit, gridBagConstraints);

        rdoTransactionType_Debit.setText("Payment");
        rdoTransactionType_Debit.setMinimumSize(new java.awt.Dimension(85, 27));
        rdoTransactionType_Debit.setPreferredSize(new java.awt.Dimension(85, 27));
        rdoTransactionType_Debit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTransactionType_DebitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -5;
        gridBagConstraints.ipady = -3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 10);
        panTransactionType.add(rdoTransactionType_Debit, gridBagConstraints);

        panData.add(panTransactionType);
        panTransactionType.setBounds(120, 90, 180, 23);

        lblInitiatorChannel.setText("Initiator Channel Type");
        panData.add(lblInitiatorChannel);
        lblInitiatorChannel.setBounds(0, 120, 139, 16);

        txtInitiatorChannel.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInitiatorChannel.setPreferredSize(new java.awt.Dimension(21, 200));
        panData.add(txtInitiatorChannel);
        txtInitiatorChannel.setBounds(140, 120, 130, 20);

        lblInputCurrency.setText("Currency");
        panData.add(lblInputCurrency);
        lblInputCurrency.setBounds(30, 140, 56, 16);

        cboInputCurrency.setMinimumSize(new java.awt.Dimension(100, 21));
        cboInputCurrency.setPreferredSize(new java.awt.Dimension(21, 200));
        panData.add(cboInputCurrency);
        cboInputCurrency.setBounds(141, 150, 130, 20);

        btnNew_Property1.setText("New");
        btnNew_Property1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_Property1ActionPerformed(evt);
            }
        });
        panData.add(btnNew_Property1);
        btnNew_Property1.setBounds(420, 220, 39, 26);

        btnlSave_Property1.setText("Save");
        btnlSave_Property1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlSave_Property1ActionPerformed(evt);
            }
        });
        panData.add(btnlSave_Property1);
        btnlSave_Property1.setBounds(480, 220, 40, 26);

        btnDelete_Property1.setText("Delete");
        btnDelete_Property1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete_Property1ActionPerformed(evt);
            }
        });
        panData.add(btnDelete_Property1);
        btnDelete_Property1.setBounds(550, 220, 52, 26);

        txtInputAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInputAmt.setPreferredSize(new java.awt.Dimension(21, 200));
        txtInputAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInputAmtFocusLost(evt);
            }
        });
        panData.add(txtInputAmt);
        txtInputAmt.setBounds(140, 180, 110, 20);

        lblInputAmt.setText("Currency Amount");
        panData.add(lblInputAmt);
        lblInputAmt.setBounds(11, 180, 110, 16);

        lblInstrumentType.setText("Instrument Type");
        panData.add(lblInstrumentType);
        lblInstrumentType.setBounds(10, 210, 110, 16);

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
        cboInstrumentType.setBounds(140, 210, 120, 20);

        lblInstrumentNo.setText("Instrument No.");
        panData.add(lblInstrumentNo);
        lblInstrumentNo.setBounds(320, 10, 95, 16);

        txtInstrumentNo2.setMinimumSize(new java.awt.Dimension(100, 21));
        panData.add(txtInstrumentNo2);
        txtInstrumentNo2.setBounds(420, 10, 100, 21);

        tdtInstrumentDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtInstrumentDate.setPreferredSize(new java.awt.Dimension(21, 200));
        tdtInstrumentDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtInstrumentDateFocusLost(evt);
            }
        });
        panData.add(tdtInstrumentDate);
        tdtInstrumentDate.setBounds(420, 30, 100, 30);

        lblInstrumentDate.setText("Instrument Date");
        panData.add(lblInstrumentDate);
        lblInstrumentDate.setBounds(310, 40, 102, 16);

        txtTokenNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTokenNo.setPreferredSize(new java.awt.Dimension(21, 200));
        txtTokenNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTokenNoFocusLost(evt);
            }
        });
        panData.add(txtTokenNo);
        txtTokenNo.setBounds(400, 80, 120, 20);

        lblTokenNo.setText("Token No.");
        panData.add(lblTokenNo);
        lblTokenNo.setBounds(310, 80, 65, 16);

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
        txtAmount.setBounds(400, 110, 100, 21);

        lblAmount1.setText("Amount");
        panData.add(lblAmount1);
        lblAmount1.setBounds(310, 120, 50, 16);

        txtParticulars.setMinimumSize(new java.awt.Dimension(100, 21));
        txtParticulars.setPreferredSize(new java.awt.Dimension(21, 200));
        panData.add(txtParticulars);
        txtParticulars.setBounds(390, 150, 130, 20);

        lblParticulars.setText("Particulars");
        panData.add(lblParticulars);
        lblParticulars.setBounds(300, 150, 66, 16);

        txtNarration.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNarration.setPreferredSize(new java.awt.Dimension(21, 200));
        panData.add(txtNarration);
        txtNarration.setBounds(450, 180, 21, 20);

        lblNarration.setText("Member Name/Narration");
        panData.add(lblNarration);
        lblNarration.setBounds(300, 180, 156, 16);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 21;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        panCashTransaction.add(panData, gridBagConstraints);

        panMultiple.add(panCashTransaction, new java.awt.GridBagConstraints());

        panComplaint.add(panMultiple, new java.awt.GridBagConstraints());

        getContentPane().add(panComplaint, java.awt.BorderLayout.CENTER);

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
        tbrVisitorsDiary.add(btnView);

        lblSpace5.setText("     ");
        tbrVisitorsDiary.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnNew);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnEdit);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
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
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnSave);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
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

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnReject);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnException);

        lblSpace4.setText("     ");
        tbrVisitorsDiary.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrVisitorsDiary.add(btnPrint);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnClose);

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
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
//        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
//        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
//        btnCancel.setEnabled(true);
//        btnReject.setEnabled(false);
//        btnException.setEnabled(false);
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
    ClientUtil.showMessageWindow("Complaints Cannot be edited");
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
       // deletescreenLock();
        viewType = "CANCEL" ;
        observable.resetForm();
        ClientUtil.clearAll(this);
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        lblStatus.setText("");
        setModified(false);
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        updateOBFields();
       
//        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panComplains);
//        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
//            displayAlert(mandatoryMessage);
//        }
//        else{
//            savePerformed();
//        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        enableDisable(true);
        setButtonEnableDisable();
//        btnCust.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        
        
    }//GEN-LAST:event_btnNewActionPerformed
    
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

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        //To Set the Value of Account Head in UI...
        //        if (cboProdType.getSelectedIndex() > 0) {
            //            String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
            //            System.out.println("***************"+prodType);
            //            clearProdFields();
            //            //Added BY Suresh
            //            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
                //                txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
                //            }
            //            populateInstrumentType();
            //            observable.setProdType(prodType);
            //            //this is for depoists
            //
            //            if (prodType.equals("GL")) {
                //                if(TrueTransactMain.BRANCH_ID.equals(CommonUtil.convertObjToStr(TrueTransactMain.selBranch))){
                    //                    //                observable.setCbmProdId(prodType);
                    //                    //                cboProdId.setModel(observable.getCbmProdId());
                    //                    txtAccNo.setText("");
                    //                    txtPanNo.setText("");
                    //                    btnPanNo.setEnabled(false);
                    //                    btnAccHdId.setEnabled(true);
                    //                    if (!( viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE || viewType==VIEW || viewType==LINK_BATCH || viewType==LINK_BATCH_TD))
                    //                    observable.setTxtAccNo("");
                    //                    setProdEnable(false);
                    //                    cboProdId.setSelectedItem("");
                    //                }
                //                else{
                    //                    ClientUtil.displayAlert("InterBranch Transactions Not Allowed For GL");
                    //                    observable.resetForm();
                    //                }
                //                txtAccHdId.setEnabled(true);
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
            //        }
        //        if(viewType==AUTHORIZE || viewType==LINK_BATCH || viewType==LINK_BATCH_TD) {
            //            cboProdId.setEnabled(false);
            //            txtAccNo.setEnabled(false);
            //            btnAccNo.setEnabled(false);
            //        }
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void btnAccHdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccHdIdActionPerformed
        //   viewType = ACCTHDID;
        final HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
        new ViewAll(this, viewMap).show();
    }//GEN-LAST:event_btnAccHdIdActionPerformed

    private void rdoTransactionType_CreditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTransactionType_CreditActionPerformed
        // Add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            //                if (observable.getProdType().equals("TD") || observable.getProdType().equals("TL")) {
                //                    txtAccNo.setText("");
                //                }
        }
        if (rdoTransactionType_Credit.isSelected()) {
            String status = "";
            //   btnOrgOrResp.setText("O");
            //  String prodId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
            HashMap where = new HashMap();
            where.put("PRODUCT_ID", prodId);
            List lst = ClientUtil.executeQuery("getOpAcctProductTOByProdId", where);//OperativeAcctProductTO
            if (lst != null && lst.size() > 0) {
                  OperativeAcctProductTO map = (OperativeAcctProductTO) lst.get(0);
                status = map.getSRemarks();
            }

            if (status.equals("NRE")) {
                ClientUtil.displayAlert("Credit Transactions Not Allowed For This Product!!!");
                rdoTransactionType_Debit.setSelected(true);
            }
            txtTokenNo.setText("");
            //                txtTokenNo.setEditable(false);
            //                txtTokenNo.setEditable(false);
            cboInstrumentType.setSelectedIndex(0);
          // txtInstrumentNo1.setText("");
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
            //  acctMap.put("ACCT_HEAD", observable.getTxtAccHd());
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
                    //    btnCurrencyInfo.setVisible(true);
                        //  btnCurrencyInfo.setEnabled(true);
                    } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Debit.isSelected() == true) {
                        //   reconcilebtnDisable = true;
                        //  observable.setReconcile("N");
                        //  btnCurrencyInfo.setVisible(true);
                        //  btnCurrencyInfo.setEnabled(true);
                    } else if (acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Debit.isSelected() == true) {
                        //  observable.setReconcile("Y");
                        //  btnCurrencyInfo.setVisible(false);
                       // btnCurrencyInfo.setEnabled(false);
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
        //                    rdoTransactionType_DebitActionPerformed(evt);
    }//GEN-LAST:event_rdoTransactionType_CreditActionPerformed

    private void rdoTransactionType_DebitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTransactionType_DebitActionPerformed
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            //                if (observable.getProdType().equals("TD") || observable.getProdType().equals("TL")) {
                //                    txtAccNo.setText("");
                //                }
            //                String token="";
            //                List list1;
            //                boolean tkn= false;
            if (rdoTransactionType_Debit.isSelected()) {
                //   btnOrgOrResp.setText("R");
                // if (custStatus.equals("Y") && txtAccNo.getText().length() > 0) {
                    //     ClientUtil.displayAlert("MINOR ACCOUNT");
                    //  }
                //                        txtTokenNo.setEditable(false);
                //                        txtTokenNo.setEnabled(false);
                //                        HashMap tokenIssue = new HashMap();
                //                        tokenIssue.put("CURRENT_DT", currDt);
                //                        tokenIssue.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
                //                        tokenIssue.put("RECEIVED_BY", TrueTransactMain.USER_ID);
                //                        System.out.println("rdotransaction tokenissue"+tokenIssue);
                //                        List lst = ClientUtil.executeQuery("getStartTokenNo", tokenIssue);
                //                        if (lst.size()>0) {
                    //                            for(int i=0;i<lst.size();i++) {
                        //                                if(tkn==true) {
                            //                                    break;
                            //                                }
                        //                                HashMap tokenIssue1 = new HashMap();
                        //                                tokenIssue1 =(HashMap)lst.get(i);
                        //                                tokenIssue1.put("CURRENT_DT", currDt);
                        //                                tokenIssue1.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
                        //                                tokenIssue1.put("RECEIVED_BY", TrueTransactMain.USER_ID);
                        //                                if(tokenIssue1.get("SERIES_NO")==null) {
                            //                                    list1 = ClientUtil.executeQuery("getMaxNoBasedOnTokenNoForPapeToken", tokenIssue1);
                            //                                }
                        //                                else {
                            //                                    list1 = ClientUtil.executeQuery("getMaxNoBasedOnTokenNo", tokenIssue1);
                            //                                }
                        //                                HashMap map = new HashMap();
                        //                                if(list1!=null && list1.size()>0) {
                            //
                            //                                    map=(HashMap)list1.get(0);
                            //                                    if (CommonUtil.convertObjToInt(map.get("TOKEN_NO"))>=(CommonUtil.convertObjToInt(tokenIssue1.get("TOKEN_END_NO")))) {
                                //                                        continue;
                                //                                    }
                            //                                    else {
                                //                                        token=CommonUtil.convertObjToStr(map.get("SERIES_NO"));
                                //                                        int t= CommonUtil.convertObjToInt(map.get("TOKEN_NO"));
                                //                                        long tk=t+1;
                                //                                        int a=0;
                                //                                        int b=0;
                                //                                        while(a==b) {
                                    //                                            HashMap where = new HashMap();
                                    //                                            where.put("SERIES_NO",token);
                                    //                                            where.put("TOKEN_NO",new Long(tk));
                                    //                                            where.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
                                    //                                            where.put("RECEIVED_BY", TrueTransactMain.USER_ID);
                                    //                                            System.out.println("@@where"+where);
                                    //                                            List lists = ClientUtil.executeQuery("chechForTokenLoss", where);
                                    //                                            HashMap where1 = new HashMap();
                                    //                                            where1=(HashMap)lists.get(0);
                                    //                                            a= CommonUtil.convertObjToInt(where1.get("CNT"));
                                    //                                            if(a==b) {
                                        //                                                token+=tk;
                                        //                                                a=1;
                                        //                                                tkn=true;
                                        //                                                break;
                                        //                                            }
                                    //                                            else{
                                        //                                                tk=tk+1;
                                        //                                                int ten=(CommonUtil.convertObjToInt(tokenIssue1.get("TOKEN_END_NO")));
                                        //                                                if(tk>ten) {
                                            //                                                    ClientUtil.displayAlert("No tokens are available.");
                                            //                                                    btnCancelActionPerformed(null);
                                            //                                                    return;
                                            //                                                }
                                        //                                                a=0;
                                        //                                            }
                                    //                                        }
                                //                                    }
                            //                                }
                        //                                else{
                            //                                    token=CommonUtil.convertObjToStr(tokenIssue1.get("SERIES_NO"));
                            //                                    int t= CommonUtil.convertObjToInt(tokenIssue1.get("TOKEN_START_NO"));
                            //                                    int a=0;
                            //                                    int b=0;
                            //                                    while(a==b) {
                                //                                        HashMap where = new HashMap();
                                //                                        where.put("SERIES_NO",token);
                                //                                        where.put("TOKEN_NO",new Long(t));
                                //                                        where.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
                                //                                        where.put("RECEIVED_BY", TrueTransactMain.USER_ID);
                                //                                        System.out.println("@@where"+where);
                                //                                        List lists = ClientUtil.executeQuery("chechForTokenLoss", where);
                                //                                        HashMap where1 = new HashMap();
                                //                                        where1=(HashMap)lists.get(0);
                                //                                        a= CommonUtil.convertObjToInt(where1.get("CNT"));
                                //                                        if(a==b) {
                                    //                                            token+=t;
                                    //                                            a=1;
                                    //                                            tkn=true;
                                    //                                            break;
                                    //                                        }
                                //                                        else{
                                    //                                            t=t+1;
                                    //                                            int ten=(CommonUtil.convertObjToInt(tokenIssue1.get("TOKEN_END_NO")));
                                    //                                            if(t>ten) {
                                        //                                                ClientUtil.displayAlert("No tokens are available.");
                                        //                                                btnCancelActionPerformed(null);
                                        //                                                return;
                                        //                                            }
                                    //                                            a=0;
                                    //                                        }
                                //                                    }
                            //
                            //                                }
                        //
                        //                            }
                    //
                    //
                    //                        }
                //
                //
                //                        else {
                    //                            ClientUtil.displayAlert("No tokens are available.");
                    //                            btnCancelActionPerformed(null);
                    //                            return;
                    //                        }
                //                        if(tkn==false) {
                    //                            ClientUtil.displayAlert("No tokens are available.");
                    //                            btnCancelActionPerformed(null);
                    //                            return;
                    //                        }
                //                    txtTokenNo.setText(token);
                cboInstrumentType.setEnabled(true);
                //                    txtInstrumentNo1.setEditable(true);
                //   txtInstrumentNo1.setEnabled(true);
                //                    txtInstrumentNo2.setEditable(true);
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
        System.out.println("prodTypeprodType in test1"+prodType);
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
                       /// btnCurrencyInfo.setEnabled(true);
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
                    ////btnCurrencyInfo.setVisible(false);
                    //   btnCurrencyInfo.setEnabled(false);
                }
            }
        } else {
            //  btnCurrencyInfo.setVisible(false);
            // btnCurrencyInfo.setEnabled(false);
        }
    }//GEN-LAST:event_rdoTransactionType_DebitActionPerformed

    private void btnNew_Property1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_Property1ActionPerformed
        cboProdType.setSelectedItem("");
        //       cboProdId.setSelectedItem("");

        txtAccHdId.setText("");
        //        txtAccNo.setText("");
        //        txtInputAmt.setText("");
        txtAmount.setText("");
        //        tdtDocumentDt.setDateValue(null);
        cboProdType.setEnabled(true);
        //        cboProdId.setEnabled(true);
        txtAccHdId.setEnabled(true);
        //        txtAccNo.setEnabled(true);
        //        txtInputAmt.setEnabled(true);
        txtAmount.setEnabled(true);
    }//GEN-LAST:event_btnNew_Property1ActionPerformed

    private void btnlSave_Property1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlSave_Property1ActionPerformed
        // TODO add your handling code here:

        StringBuffer message = new StringBuffer("");
        if(cboProdType.getSelectedIndex()==0)
        {
            message.append("Enter Product Type");
            message.append("\n");
        }
        if(txtAccHdId.getText().equals(""))
        {
            message.append("Enter Account Head");
            message.append("\n");
        }

        if(txtAmount.getText().equals(""))
        {
            message.append("Enter Amount");
            message.append("\n");
        }

        if(message.length() > 0 ){
            displayAlert(message.toString());
            return;
        }
        String prodType=CommonUtil.convertObjToStr(cboProdType.getSelectedItem());
        String accno=txtAccHdId.getText();
        String amount=txtAmount.getText();
        //        String docDate=tdtDocumentDt.getDateValue();
        // int slno=tblDocument.getRowCount();
        //         int selectrow=tblGahanLandDetails.getSelectedRow();

        String row=CommonUtil.convertObjToStr(new Integer(tblCash.getRowCount()+1));
        System.out.println("rowwww "+row);
        HashMap hmap=new HashMap();
        if(tblCash.getSelectedRow()>=0){
            // ty
            int num=tblCash.getSelectedRow();
            String slno=CommonUtil.convertObjToStr(tblCash.getValueAt(tblCash.getSelectedRow(), 0));
            hmap.put("SNO",slno);
            hmap.put("ACC_NO",accno);
            hmap.put("PROD_TYPE",prodType);
            //            hmap.put("DOC_DATE",docDate);
            hmap.put("AMOUNT",amount);
            bufferList.set(num,hmap);
        }else{
            hmap.put("SNO",row);
            //            hmap.put("SNO",slno);
            hmap.put("ACC_NO",accno);
            hmap.put("PROD_TYPE",prodType);
            //            hmap.put("DOC_DATE",docDate);
            hmap.put("AMOUNT",amount);
            bufferList.add(hmap);
        }
        cboProdType.setSelectedItem("");
        //       cboProdId.setSelectedItem("");

        txtAccHdId.setText("");
        //        txtAccNo.setText("");
        //        txtInputAmt.setText("");
        txtAmount.setText("");
        //        tdtDocumentDt.setDateValue(null);
        cboProdType.setEnabled(false);
        //        cboProdId.setEnabled(true);
        txtAccHdId.setEnabled(false);
        //        txtAccNo.setEnabled(true);
        //        txtInputAmt.setEnabled(true);
        txtAmount.setEnabled(false);
        getTableData();
    }//GEN-LAST:event_btnlSave_Property1ActionPerformed

    private void btnDelete_Property1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete_Property1ActionPerformed
        // TODO add your handling code here:
        if(tblCash.getSelectedRow()==-1)
        {
            //    JOptionPane.showMessageDialog(this, "Select a row to Delete", "Error", JOptionPane.ERROR_MESSAGE);
            //f=false;
            // tabedit=false;
            return ;
        }
        else
        {
            int row=tblCash.getSelectedRow();
            System.out.println("ROWWWWW>>>"+row+">>>>>>>>>>"+tblCash.size());

            // drmOB=(DeathReliefMasterOB)buffer1(row);
            //           tdtDrfFromDt.setDateValue(tblInterest.getValueAt(row,0).toString());
            //           txtToDt.setText(tblInterest.getValueAt(row, 1).toString());
            //           txtInterestRate.setText(tblInterest.getValueAt(row, 2).toString());
            cboProdType.setSelectedItem("");

            txtAccHdId.setText("");

            txtAmount.setText("");

            cboProdType.setEnabled(false);

            txtAccHdId.setEnabled(false);

            txtAmount.setEnabled(false);

            HashMap temp=new HashMap();
            temp=(HashMap)bufferList.get(row);
            System.out.println("temp  "+temp);
            int no=Integer.parseInt(temp.get("SNO").toString());
            bufferList.remove(row);
            System.out.println("no  "+no+"bufferList   "+bufferList);
            System.out.println("row+1 "+(row+1)+" bufferList.size  "+bufferList.size());
            for(int i=row;i<bufferList.size();i++)
            {
                HashMap amap=new HashMap();
                amap=(HashMap)bufferList.get(i);
                bufferList.remove(i);
                amap.put("SNO",no);
                bufferList.add(i,amap);

                no=no+1;
                System.out.println("bufff   "+bufferList+"  dfjd no  "+no);
            }
            // bufferList.remove(row);
            getTableData();
        }
    }//GEN-LAST:event_btnDelete_Property1ActionPerformed

    private void txtInputAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInputAmtFocusLost
        // Add your handling code here:
        //                        String amt = "";
        //                        if (cboInputCurrency.getSelectedIndex() > -1) {
            //                            String type = "";
            //                            if (rdoTransactionType_Credit.isSelected()) {
                //                                type = "CREDIT";
                //                            } else {
                //                                type = "DEBIT";
                //                            }
            //                            if(this.txtInputAmt!=null && this.txtInputAmt.getText().length()>0){
                //                                try {
                    //                                    amt = String.valueOf(
                        //                                    ClientUtil.convertCurrency(
                            //                                    (String) ((ComboBoxModel) cboInputCurrency.getModel()).getKeyForSelected(),
                            //                                    lblProductCurrency.getText(),
                            //                                    type, new Double(txtInputAmt.getText()).doubleValue()));
                    //                                } catch (Exception e) {
                    //                                    System.out.println ("currency conversion error");
                    //                                    amt = txtInputAmt.getText();
                    //                                }
                //                            }
            //                        }
        //                        txtAmount.setText(amt);
    }//GEN-LAST:event_txtInputAmtFocusLost

    private void cboInstrumentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboInstrumentTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboInstrumentTypeActionPerformed

    private void cboInstrumentTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboInstrumentTypeFocusLost
        //   instrumentTypeFocus();
    }//GEN-LAST:event_cboInstrumentTypeFocusLost

    private void tdtInstrumentDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtInstrumentDateFocusLost
        // Add your handling code here:
        //ClientUtil.validateLTDate(tdtInstrumentDate);
    }//GEN-LAST:event_tdtInstrumentDateFocusLost

    private void txtTokenNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTokenNoFocusLost
        //   tokenChecking();
    }//GEN-LAST:event_txtTokenNoFocusLost

    private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountActionPerformed
        // TODO add your handling code here:
        //        System.out.println("callchange");
        //        observable.setTxtAmount(txtAmount.getText());
        //        observable.changeAmount();NOW ONLY COMMANT
        //        txtAmount.setText(observable.getTxtAmount());
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

                /*   if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
                    double penalInt = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    if (asAndWhenMap != null && asAndWhenMap.containsKey("PENAL_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("PENAL_WAIVER")).equals("Y") && penalInt > 0) {
                        int result = ClientUtil.confirmationAlert("Do you Want to Waive Penal Interest");
                        if (result == 0) {
                            observable.setPenalWaiveOff(true);
                        } else {
                            observable.setPenalWaiveOff(false);
                        }
                    } else {
                        double totalDue = transDetails.calculatetotalRecivableAmountFromAccountClosing();
                        if (asAndWhenMap != null && asAndWhenMap.containsKey("INTEREST") && CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue() > 0
                            && CommonUtil.convertObjToDouble(asAndWhenMap.get("REBATE_INTEREST")).doubleValue() > 0 && transAmt >= (totalDue - CommonUtil.convertObjToDouble(asAndWhenMap.get("REBATE_INTEREST")).doubleValue())) {
                            int result = ClientUtil.confirmationAlert("Do you Want to  give Rebate Interest");
                            if (result == 0) {
                                observable.setRebateInterest(true);
                            } else {
                                observable.setRebateInterest(false);
                            }
                        }
                    }

                }*/
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
            double penalAmt = CommonUtil.convertObjToDouble(transDetails.getPenalAmount()).doubleValue();
            System.out.println("#####ProdId :" + prodId + "penalAmt :" + penalAmt);
            HashMap prodMap = new HashMap();
            prodMap.put("PROD_ID", prodId.toString());
            List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
            System.out.println("######## BHEAVES :" + lst);
            if (lst != null && lst.size() > 0) {
                prodMap = (HashMap) lst.get(0);
                if (prodMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                    HashMap recurringMap = new HashMap();
                    double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                    String depNo = "";//txtAccNo.getText();
                    System.out.println("########Amount : " + amount + "####DepNo :" + depNo);
                    depNo = depNo.substring(0, depNo.lastIndexOf("_"));
                    System.out.println("######## BHEAVES :" + depNo);
                    recurringMap.put("DEPOSIT_NO", depNo);
                    lst = ClientUtil.executeQuery("getDepositAmountForRecurring", recurringMap);
                    if (lst != null && lst.size() > 0) {
                        recurringMap = (HashMap) lst.get(0);
                        double depAmount = CommonUtil.convertObjToDouble(recurringMap.get("DEPOSIT_AMT")).doubleValue();
                        double finalAmount = 0.0;
                        if (penalAmt > 0) {
                            String[] obj = {"Penalty with Installments", "Installments Only."};
                            int option = COptionPane.showOptionDialog(null, ("Select The Desired Option"), ("Receiving Penal with Installments..."),
                                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj, obj[0]);
                            if (option == 0) {
                                if (amount > penalAmt) {
                                    amount = amount - penalAmt;
                                    finalAmount = amount % depAmount;
                                } else {
                                    finalAmount = amount % (penalAmt + depAmount);
                                }
                                if (finalAmount != 0) {
                                    ClientUtil.displayAlert("Minimum Amount Should Enter ..." + (depAmount + penalAmt));
                                    txtAmount.setText("");
                                } else {
                                    //       observable.setDepositPenalAmt(String.valueOf(penalAmt));
                                    //      observable.setDepositPenalMonth(transDetails.getPenalMonth());
                                }
                            } else {
                                finalAmount = amount % depAmount;
                                if (finalAmount != 0) {
                                    ClientUtil.displayAlert("Enter Amount in Multiples of Deposit Amount...\n"
                                        + "Deposit Amount is : " + depAmount);
                                    txtAmount.setText("");
                                }
                                // observable.setDepositPenalAmt(String.valueOf(0.0));
                                // observable.setDepositPenalMonth(String.valueOf(0.0));
                            }
                        } else {
                            finalAmount = amount % depAmount;
                            observable.setDepositPenalAmt(String.valueOf(0.0));
                            //observable.setDepositPenalMonth(String.valueOf(0.0));
                            if (finalAmount != 0) {
                                ClientUtil.displayAlert("Enter Amount in Multiples of Deposit Amount...\n"
                                    + "Deposit Amount is : " + depAmount);
                                txtAmount.setText("");
                            }
                        }
                        System.out.println("######## BHEAVES REMAINING :" + finalAmount);
                    }
                    recurringMap = null;
                }
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
            System.out.println("#####ProdId :" + prodId);
            HashMap prodMap = new HashMap();
            prodMap.put("PROD_ID", prodId.toString());
            List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
            System.out.println("######## BHEAVES :" + lst);
            if (lst.size() > 0) {
                prodMap = (HashMap) lst.get(0);
                if (prodMap.get("BEHAVES_LIKE").equals("FIXED")) {
                    HashMap recurringMap = new HashMap();
                    double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                       String depNo =""; //txtAccNo.getText();
                    //                    System.out.println("########Amount : "+amount +"####DepNo :"+depNo);
                    depNo = depNo.substring(0, depNo.lastIndexOf("_"));
                    //                    System.out.println("######## BHEAVES :"+depNo);
                    recurringMap.put("DEPOSIT_NO", depNo);
                    lst = ClientUtil.executeQuery("getInterestDeptIntTable", recurringMap);
                    if (lst.size() > 0) {
                        recurringMap = (HashMap) lst.get(0);
                        double depAmount = CommonUtil.convertObjToDouble(recurringMap.get("PERIODIC_INT_AMT")).doubleValue();
                        double finalAmount = amount % depAmount;
                        System.out.println("######## BHEAVES REMAINING :" + finalAmount);
                        if (finalAmount == 0) {
                            txtAmount.setEnabled(false);
                            System.out.println("######## BHEAVES :" + finalAmount);
                        } else {
                            //                            ClientUtil.displayAlert("Enter Amount in Multiples of Deposit Amount... ");
                            //                            txtAmount.setText("");
                        }
                    }
                    recurringMap = null;
                } else if (prodMap.get("BEHAVES_LIKE").equals("DAILY") && prodMap.get("PARTIAL_WITHDRAWAL_ALLOWED").equals("Y")) {
                    HashMap dailyDepMap = new HashMap();
                    double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                    String depNo ="";// txtAccNo.getText();
                    //                    System.out.println("########Amount : "+amount +"####DepNo :"+depNo);
                    depNo = depNo.substring(0, depNo.lastIndexOf("_"));
                    //                    System.out.println("######## BHEAVES :"+depNo);
                    dailyDepMap.put("DEPOSIT_NO", depNo);
                    lst = ClientUtil.executeQuery("getDepAvailBalForPartialWithDrawal", dailyDepMap);
                    if (lst.size() > 0) {
                        dailyDepMap = (HashMap) lst.get(0);
                        double depAmount = CommonUtil.convertObjToDouble(dailyDepMap.get("AVAILABLE_BALANCE")).doubleValue();
                        if (depAmount < amount) {
                            System.out.println("@#$@#$%$^$%^amount" + amount + " :depAmount: " + depAmount);
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
                
    /** To populate Comboboxes */
    private void initComponentData() {
//        cboRoleCurrBran.setModel(observable.getCbmRoleInCurrBran());
//        cboRoleTransBran.setModel(observable.getCbmRoleInTranBran());
//        cboTransBran.setModel(observable.getCbmTransferBran());
    }
    
    /** To display a popUp window for viewing existing data */
    private void popUp(String currAction){
       viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
           // map.put("BRANCH_CODE","0001");
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getVisitorsDiaryEdit"); 
        }else if(currAction.equalsIgnoreCase("Delete")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getVisitorsDiaryEdit");
        } else if(currAction.equalsIgnoreCase("CUST")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "setCustId");
        }
       
        new ViewAll(this,viewMap).show();
       
    }

    
    /** Called by the Popup window created thru popUp method */
    public void fillData(Object map) {
        try{
           setModified(true);
        HashMap hash = (HashMap) map;
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3])|| viewType.equals(AUTHORIZE) ||
            viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                hash.put(CommonConstants.MAP_WHERE, hash.get("VISIT_ID"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                observable.getData(hash);
                if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
                viewType.equals(ClientConstants.ACTION_STATUS[17])) {
//                    ClientUtil.enableDisable(panComplains, false);
                   
                } else {
                //    ClientUtil.enableDisable(panComplains, true);
                 
                }
                setButtonEnableDisable();
                if(viewType ==  AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
            }
            
        }
        if (viewType.equals("CUST")){
//            txtCustid.setText(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
//                lblCurrBranValue.setText(CommonUtil.convertObjToStr(hash.get("PRESENT_BRANCH_CODE")));
//                lblEmpName.setText(CommonUtil.convertObjToStr(hash.get("FNAME")));
//                lblBranName.setText(CommonUtil.convertObjToStr(hash.get("BRANCH_NAME")));
        }
//         if(viewType.equals(ClientConstants.ACTION_STATUS[2]) || viewType.equals(AUTHORIZE)) {
//            HashMap screenMap = new HashMap();
//            screenMap.put("TRANS_ID",hash.get("EMP_TRANSFER_ID"));
//            screenMap.put("USER_ID",ProxyParameters.USER_ID);
//            screenMap.put("TRANS_DT", currDt);
//            screenMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
//            List lst=ClientUtil.executeQuery("selectauthorizationLock", screenMap);
//            if(lst !=null && lst.size()>0) {
//                screenMap=null;
//                StringBuffer open=new StringBuffer();
//                for(int i=0;i<lst.size();i++){
//                    screenMap=(HashMap)lst.get(i);
//                    open.append("\n"+"User Id  :"+" ");
//                    open.append(CommonUtil.convertObjToStr(screenMap.get("OPEN_BY"))+"\n");
//                    open.append("Mode Of Operation  :" +" ");
//                    open.append(CommonUtil.convertObjToStr(screenMap.get("MODE_OF_OPERATION"))+" ");
//                    btnSave.setEnabled(false);
//                    ClientUtil.enableDisable(panVisitors, false);
//                    btnEmp.setEnabled(false);
//                }
//                ClientUtil.showMessageWindow("already open by"+open);
//                return;
//            }
//            else{
//                hash.put("TRANS_ID",hash.get("EMP_TRANSFER_ID"));
//                if(viewType.equals(ClientConstants.ACTION_STATUS[2]))
//                    hash.put("MODE_OF_OPERATION","EDIT");
//                if(viewType==AUTHORIZE)
//                    hash.put("MODE_OF_OPERATION","AUTHORIZE");
//                   hash.put("USER_ID",TrueTransactMain.USER_ID);
//                   hash.put("TRANS_DT", currDt);
//                   hash.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
//                ClientUtil.execute("insertauthorizationLock", hash);
//            }
//        }
//        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
//            String lockedBy = "";
//            HashMap Lockmap = new HashMap();
//            Lockmap.put("SCREEN_ID", getScreenID());
//            Lockmap.put("RECORD_KEY", CommonUtil.convertObjToStr(hash.get("EMP_TRANSFER_ID")));
//            Lockmap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
//            System.out.println("Record Key Map : " + Lockmap);
//            List lstLock = ClientUtil.executeQuery("selectEditLock", Lockmap);
//            if (lstLock.size() > 0) {
//                lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
//                if (!lockedBy.equals(ProxyParameters.USER_ID)) {
//                    btnSave.setEnabled(false);
//                    ClientUtil.enableDisable(panVisitors, false);
//                    btnEmp.setEnabled(false);
//                } else {
//                    btnSave.setEnabled(true);
//                    ClientUtil.enableDisable(panVisitors, true);
//                    btnEmp.setEnabled(true);
//                }
//            } else {
//                btnSave.setEnabled(true);
//                ClientUtil.enableDisable(panVisitors, true);
//                btnEmp.setEnabled(true);
//            }
//            setOpenForEditBy(lockedBy);
//            if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
//                String data = getLockDetails(lockedBy, getScreenID()) ;
//                ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
//                btnSave.setEnabled(false);
//                ClientUtil.enableDisable(panVisitors, false);
//                btnEmp.setEnabled(false);
//            }
//        }
        }catch(Exception e){
            log.error(e);
        }
    }
    
    
    private void enableDisable(boolean yesno){
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
////         lblStatus.setText(observable.getLblStatus());
       
    }
    
    public void updateOBFields() {
        
//        observable.setTxtDateofComplaint(tdtDateofComplaint.getDateValue());
//        observable.setTxtEmployeeId(txtCustid.getText());
//        observable.setTxaNameAddress(txaNameAddress.getText());
//        observable.setTxaComments(txaComments.getText());
    }
    
   
    
   private void savePerformed(){
           
            updateOBFields();
            observable.doAction() ;
           if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
               // lst.add("EMP_TRANSFER_ID");
                //lockMap.put("EMP_TRANSFER_ID",CommonUtil.convertObjToStr(lblEmpTransferID.getText()));
               // lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
                 //   lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                    if (observable.getProxyReturnMap()!=null) {
                        if (observable.getProxyReturnMap().containsKey("VISIT_ID")) {
                            //lockMap.put("EMP_TRANSFER_ID",observable.getProxyReturnMap().get("VISIT_ID"));
                        }
                    }
                }
                if(observable.getResult()==ClientConstants.ACTIONTYPE_EDIT){
                   // lockMap.put("EMP_TRANSFER_ID", observable.getTxtEmpTransferID());
                }
               // setEditLockMap(lockMap);
              ////  setEditLock();
              //  deletescreenLock();
            }
           
            observable.resetForm();
            enableDisable(false);
            setButtonEnableDisable();
            lblStatus.setText(observable.getLblStatus());
           ClientUtil.enableDisable(this, false);
           observable.setResultStatus();
           lblStatus.setText(observable.getLblStatus());
            //__ Make the Screen Closable..
           setModified(false);
            ClientUtil.clearAll(this);
           observable.ttNotifyObservers();
       
   }
    private void setFieldNames() {
//        tdtDateofComplaint.setName("tdtDateofComplaint");
//        txaNameAddress.setName("txaNameAddress");
//        txtCustid.setName("txtCustid");
//        txaComments.setName("txaComments");
//        lblDateofComplaint.setName("lblDateofComplaint");
//        lblNameAddress.setName("lblNameAddress");
//        lblCustId.setName("lblCustId");
//        lblComments.setName("lblComments");
//        panComplaint.setName("panComplaint");
//        panComplains.setName("panComplains");
       
    }
    
    private void internationalize() {
        //        java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.customer.customeridchange.CustomerIdChangeRB", ProxyParameters.LANGUAGE);
        //        lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
        //        lblProd.setText(resourceBundle.getString("lblProd"));
        //        lblOldCustNum.setText(resourceBundle.getString("lblOldCustNum"));
        //        lblNewCustNum.setText(resourceBundle.getString("lblNewCustNum"));
        //        lblProdType.setText(resourceBundle.getString("lblProdType"));
    }
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    
    private void deletescreenLock(){
        HashMap map=new HashMap();
        map.put("USER_ID",ProxyParameters.USER_ID);
        map.put("TRANS_DT", currDt.clone());
        map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }
    private String getLockDetails(String lockedBy, String screenId){
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer() ;
        map.put("LOCKED_BY", lockedBy) ;
        map.put("SCREEN_ID", screenId) ;
        java.util.List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if(lstLock.size() > 0){
            map = (HashMap)(lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME")) ;
            data.append("\nIP Address : ").append(map.get("IP_ADDR")) ;
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null ;
        map = null ;
        return data.toString();
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
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CComboBox cboInputCurrency;
    private com.see.truetransact.uicomponent.CComboBox cboInstrumentType;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CLabel lblAccHd;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblAmount1;
    private com.see.truetransact.uicomponent.CLabel lblInitiatorChannel;
    private com.see.truetransact.uicomponent.CLabel lblInputAmt;
    private com.see.truetransact.uicomponent.CLabel lblInputCurrency;
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
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTokenNo;
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
    private com.see.truetransact.uicomponent.CPanel panComplaint;
    private com.see.truetransact.uicomponent.CPanel panData;
    private com.see.truetransact.uicomponent.CPanel panMultiple;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransactionType;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransactionType_Credit;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransactionType_Debit;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CTable tblCash;
    private javax.swing.JToolBar tbrVisitorsDiary;
    private com.see.truetransact.uicomponent.CDateField tdtInstrumentDate;
    private com.see.truetransact.uicomponent.CTextField txtAccHdId;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtInitiatorChannel;
    private com.see.truetransact.uicomponent.CTextField txtInputAmt;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentNo2;
    private com.see.truetransact.uicomponent.CTextField txtNarration;
    private com.see.truetransact.uicomponent.CTextField txtParticulars;
    private com.see.truetransact.uicomponent.CTextField txtTokenNo;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
        frmMultipleUI complaints = new frmMultipleUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(complaints);
        j.show();
        complaints.show();
    }
}
