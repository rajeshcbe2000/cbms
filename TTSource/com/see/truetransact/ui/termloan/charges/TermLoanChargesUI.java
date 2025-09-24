/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountClosingUI.java
 *
 * Created on August 6, 2003, 10:53 AM
 */

package com.see.truetransact.ui.termloan.charges;

import java.util.HashMap;
import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.ToDateValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI ;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.transferobject.product.operativeacct.OperativeAcctProductTO;
import com.see.truetransact.ui.product.operativeacct.OperativeAcctProductOB;
import com.see.truetransact.ui.operativeaccount.AccountClosingOB;
import com.see.truetransact.transferobject.operativeaccount.AccountClosingTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.termloan.charges.TermLoanChargesOB;
import javax.swing.ScrollPaneConstants;

/** This form is used to manipulate AccountClosing related functionality
 * @author annamalai
 * Modified by Karthik
 * Modified by Sunil, added transaction UI
 */
public class TermLoanChargesUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer,UIMandatoryField{
    final int AUTHORIZE=3, CANCEL=0 ;
    int viewType=-1;
    private HashMap mandatoryMap;
    private TermLoanChargesOB observable;
    private final static Logger log = Logger.getLogger(TermLoanChargesUI.class);
    private TransactionUI transactionUI = new TransactionUI();
    private TransDetailsUI transDetailsUI = null;
    //    AccountClosingRB accountClosingRB = new AccountClosingRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.charges.TermLoanChargesRB", ProxyParameters.LANGUAGE);
    private String prodType="";
    private double calculateInt=0;
    private String chargeAmt = "";
    private String balanceAmt ="";
    private String transProdId = "";
    private boolean depositFlag;
    private LinkedHashMap transactionDetailsTO = null;
    private int ACT_NUM=200,EDIT=1,TXT_NO =100;
    
    
    private boolean btnMainNewPressed = false;
    private boolean btnSubNewPressed = false;
    private boolean mainDetailEnable = false;
    private boolean btnEditPressed=false;
    boolean isFilled = false;
    
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    private boolean fromAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    
    public TermLoanChargesUI(String prodType){
        this.prodType=prodType;
        System.out.println("termloan#####"+prodType);
        initComponents();
        setFieldNames();
        setMaxlength();
    }
    
    /** Creates new form AccountClosingUI */
    public TermLoanChargesUI() {
        initComponents();
        setFieldNames();
        internationalize();
        setObservable();
        initComponentData();
        setMandatoryHashMap();
        observable.resetForm();
        menuEnableDisable();
        ClientUtil.enableDisable(this,false);
        buttonEnableDisable(false);
        btnEdit.setVisible(false);
        btnDelete.setVisible(false);
        txtAccountNumber.setAllowAll(true);
        txtAccountNumber.setMaxLength(16);
        btnEmi.setEnabled(false);
        setMaxlength();
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap=new HashMap();
        mandatoryMap.put("cboProductType",new Boolean(true));
        mandatoryMap.put("cboProductID",new Boolean(true));
        mandatoryMap.put("txtAccountNumber",new Boolean(true));
        mandatoryMap.put("cboChargesType",new Boolean(true));
        mandatoryMap.put("tdtChargesDate",new Boolean(true));
        mandatoryMap.put("txtChargesAmount",new Boolean(true));
        
    }
    
    public HashMap getMandatoryHashMap(){
        return null;
    }
    public void update(Observable o, Object arg){
        cboProductType.setSelectedItem(((ComboBoxModel)cboProductType.getModel()).getDataForKey(observable.getProdType()));
        cboProductID.setSelectedItem(
        ((ComboBoxModel)cboProductID.getModel()).getDataForKey(observable.getCboProductID()));
        cboChargesType.setSelectedItem(((ComboBoxModel)cboChargesType.getModel()).getDataForKey(observable.getCboChargesType()));
        txtChargesAmount.setText(CommonUtil.convertObjToStr(observable.getTxtChargesAmount()));
        txtAccountNumber.setText(observable.getTxtAccountNumber());
        tdtChargesDate.setDateValue(observable.getTdtChargesDate());
        tblChargesDetails.setModel(observable.getTblChargesDetails());
        tblChargeDetails.setModel(observable.getTblChargesTotAmt());
        tblTransDetails.setModel(observable.getTblTotAcctDetails());
    }
    private void initComponentData(){
        cboProductType.setModel(observable.getCbmProdType());
        cboProductID.setModel(observable.getCbmProductID());
        cboChargesType.setModel(observable.getCbmChargeType());
        tblChargesDetails.setModel(observable.getTblChargesDetails());
        tblChargeDetails.setModel(observable.getTblChargesTotAmt());
        System.out.println((observable.getCbmProdType().getKeys()));
    }
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAccountNumber.setName("btnAccountNumber");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnChargeSave.setName("btnChargeSave");
        btnChargeNew.setName("btnChargeNew");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnView.setName("btnView");
        cboChargesType.setName("cboChargesType");
        cboProductID.setName("cboProductID");
        cboProductType.setName("cboProductType");
        lblAccountHead.setName("lblAccountHead");
        lblAccountHeadDisplay.setName("lblAccountHeadDisplay");
        lblAccountNumber.setName("lblAccountNumber");
        lblChargeAmount.setName("lblChargeAmount");
        lblChargeType.setName("lblChargeType");
        lblChargesDate.setName("lblChargesDate");
//        lblCustomerName.setName("lblCustomerName");
        lblCustomerNameDisplay.setName("lblCustomerNameDisplay");
        lblMsg.setName("lblMsg");
        lblProductID.setName("lblProductID");
        lblProductType.setName("lblProductType");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        mbrCustomer.setName("mbrCustomer");
        panAccountInfo.setName("panAccountInfo");
        panAccountInfoInner.setName("panAccountInfoInner");
        panAcctNum.setName("panAcctNum");
        panAllChargeDetail.setName("panAllChargeDetail");
        panChargeAllDetails.setName("panChargeAllDetails");
        panChargeDetails.setName("panChargeDetails");
        panProductID.setName("panProductID");
        panStatus.setName("panStatus");
        panTransaction.setName("panTransaction");
        panButtonCharges.setName("panButtonCharges");
        scrChargePan.setName("scrChargePan");
        scrChargesPan.setName("scrChargesPan");
        scrTransDetailsPan.setName("scrTransDetailsPan");
        sptAccountInfo.setName("sptAccountInfo");
        tblChargeDetails.setName("tblChargeDetails");
        tblChargesDetails.setName("tblChargesDetails");
        tblTransDetails.setName("tblTransDetails");
        tdtChargesDate.setName("tdtChargesDate");
        txtAccountNumber.setName("txtAccountNumber");
        txtChargesAmount.setName("txtChargesAmount");
    }
    
    private void setMaxlength(){
        txtChargesAmount.setValidation(new CurrencyValidation(14, 2));
        txtChargesAmount.setMaxLength(16);
    }
    private void internationalize() {
        btnAccountNumber.setText(resourceBundle.getString("btnAccountNumber"));
        //	btnAuthorize.setName("btnAuthorize");
        //	btnCancel.setName("btnCancel");
        btnChargeSave.setText(resourceBundle.getString("btnChargeSave"));
        btnChargeNew.setText(resourceBundle.getString("btnChargeNew"));
        //	btnClose.setName("btnClose");
        //	btnDelete.setName("btnDelete");
        //	btnEdit.setName("btnEdit");
        //	btnException.setName("btnException");
        //	btnNew.setName("btnNew");
        //	btnPrint.setName("btnPrint");
        //	btnReject.setName("btnReject");
        //	btnSave.setName("btnSave");
        //	btnView.setName("btnView");
        //	cboChargesType.setName("cboChargesType");
        //	cboProductID.setName("cboProductID");
        //	cboProductType.setName("cboProductType");
        lblAccountHead.setText(resourceBundle.getString("lblAccountHead"));
        //	lblAccountHeadDisplay.setName("lblAccountHeadDisplay");
        lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
        lblChargeAmount.setText(resourceBundle.getString("lblChargeAmount"));
        lblChargeType.setText(resourceBundle.getString("lblChargeType"));
        lblChargesDate.setText(resourceBundle.getString("lblChargesDate"));
//        lblCustomerName.setText(resourceBundle.getString("lblCustomerName"));
        //	lblCustomerNameDisplay.setName("lblCustomerNameDisplay");
        //	lblMsg.setName("lblMsg");
        lblProductID.setText(resourceBundle.getString("lblProductID"));
        lblProductType.setText(resourceBundle.getString("lblProductType"));
        //	lblSpace1.setName("lblSpace1");
        //	lblSpace2.setName("lblSpace2");
        //	lblSpace3.setName("lblSpace3");
        //	lblSpace4.setName("lblSpace4");
        //	lblSpace5.setName("lblSpace5");
        //	lblStatus.setName("lblStatus");
        //	mbrCustomer.setName("mbrCustomer");
        //	panAccountInfo.setName("panAccountInfo");
        //	panAccountInfoInner.setName("panAccountInfoInner");
        //	panAcctNum.setName("panAcctNum");
        //	panAllChargeDetail.setName("panAllChargeDetail");
        //	panChargeAllDetails.setName("panChargeAllDetails");
        //	panChargeDetails.setName("panChargeDetails");
        //	panProductID.setName("panProductID");
        //	panStatus.setName("panStatus");
        //	panTransaction.setName("panTransaction");
        //	scrChargePan.setName("scrChargePan");
        //	scrChargesPan.setName("scrChargesPan");
        //	scrTransDetailsPan.setName("scrTransDetailsPan");
        //	sptAccountInfo.setName("sptAccountInfo");
        //	tblChargeDetails.setName("tblChargeDetails");
        //	tblChargesDetails.setName("tblChargesDetails");
        //	tblTransDetails.setName("tblTransDetails");
        //	tdtChargesDate.setName("tdtChargesDate");
        //	txtAccountNumber.setName("txtAccountNumber");
        //	txtChargesAmount.setName("txtChargesAmount");
        
    }
    private void menuEnableDisable(){
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        btnSave.setEnabled(!btnSave.isEnabled());
        btnCancel.setEnabled(!btnCancel.isEnabled());
        btnAuthorize.setEnabled(!btnAuthorize.isEnabled());
        btnReject.setEnabled(!btnReject.isEnabled());
    }
    private void buttonEnableDisable(boolean flag){
        btnAccountNumber.setEnabled(flag);
        btnChargeNew.setEnabled(flag);
        btnChargeSave.setEnabled(flag);
    }
    public void fillData(Object obj){
        isFilled=true;
        HashMap map=(HashMap)obj;
        System.out.println("filldata###"+map);
         if (map.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            System.out.println("hash.get(PARENT) tD" + map.get("PARENT"));
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) map.get("PARENT");
            //setAuthorizeStatus("AUTHORIZE_BUTTON");
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
            map.remove("PARENT");
            viewType = AUTHORIZE;
            btnReject.setEnabled(false);
            btnAuthorize.setEnabled(true);
        }
        if (map.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            fromNewAuthorizeUI = true;
            newauthorizeListUI = (NewAuthorizeListUI) map.get("PARENT");
            //setAuthorizeStatus("AUTHORIZE_BUTTON");
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
            map.remove("PARENT");
            viewType = AUTHORIZE;
            btnReject.setEnabled(false);
            btnAuthorize.setEnabled(true);
        } 
         
        if(viewType==ACT_NUM || viewType==EDIT || viewType==AUTHORIZE ||viewType == TXT_NO){
            //         observable.populateData(map);
            String prodType = ((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
            if(prodType!=null && prodType.equals("MDS") && viewType==ACT_NUM){
                txtAccountNumber.setText(CommonUtil.convertObjToStr(map.get("CHITTAL_NO")+"_"+map.get("SUB_NO")));
                observable.setTxtAccountNumber(CommonUtil.convertObjToStr(map.get("CHITTAL_NO")+"_"+map.get("SUB_NO")));
                lblCustomerNameDisplay.setText(CommonUtil.convertObjToStr(map.get("MEMBER_NAME")));
                map.put("ACT_NUM",map.get("ACCOUNTNO"));
            }
            else{
                txtAccountNumber.setText(CommonUtil.convertObjToStr(map.get("ACCOUNTNO")));
                observable.setTxtAccountNumber(CommonUtil.convertObjToStr(map.get("ACCOUNTNO")));
                lblCustomerNameDisplay.setText(CommonUtil.convertObjToStr(map.get("CUSTOMERNAME")));
                map.put("ACT_NUM",map.get("ACCOUNTNO"));
            }
            long noofInstallment=0;
            HashMap loanMap=asAnWhenCustomerComesYesNO(CommonUtil.convertObjToStr(map.get("ACT_NUM")));
            if( viewType!=AUTHORIZE)
                if(loanMap !=null && loanMap.containsKey("INSTALL_TYPE") && loanMap.get("INSTALL_TYPE")!=null && loanMap.get("INSTALL_TYPE").equals("EMI")){
                    String remarks="0";
                    HashMap transMap=new HashMap();
                    transMap.put("LINK_BATCH_ID",txtAccountNumber.getText());
                    transMap.put("TRANS_DT",ClientUtil.getCurrentDate());
                    transMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                    List pendingList=ClientUtil.executeQuery("getPendingTransactionTL",transMap);
                    if(pendingList!=null && pendingList.size()>0){
                        HashMap hashTrans=(HashMap)pendingList.get(0);
                        String trans_actnum=CommonUtil.convertObjToStr(hashTrans.get("LINK_BATCH_ID"));
                        if(trans_actnum.equals(observable.getTxtAccountNumber())){
                            ClientUtil.showMessageWindow(" There is Pending Transaction Plz Authorize OR Reject first  ");
                            hashTrans=null;
                            pendingList=null;
                            return;
                        }
                    }
                    btnEmi.setEnabled(true);
//                    remarks= COptionPane.showInputDialog(this,"NO OF INSTALLMENT WANT TO PAY");
                    if(CommonUtil.convertObjToStr(remarks).equals(""))
                        remarks="0";
                    try {
                        noofInstallment=Long.parseLong(remarks);
                        //                            observable.setNoofInstallment(noofInstallment);
                    } catch (java.lang.NumberFormatException e) {
                        ClientUtil.displayAlert("Invalid Number...");
                        //                        txtAccNoFocusLost(null);
                        return;
                    }
                }else
                    btnEmi.setEnabled(false);
            map.put("NO_OF_INSTALLMENT",new Long(noofInstallment));
            observable.getAccountClosingCharges(map);
            if(viewType==EDIT && txtAccountNumber.getText().length()>0){
                ClientUtil.enableDisable(panProductID,false);
                menuEnableDisable();
                buttonEnableDisable(true);
                btnAccountNumber.setEnabled(false);
                
            }
        }
    }
    public HashMap asAnWhenCustomerComesYesNO(String acct_no){
        HashMap map=new HashMap();
        map.put("ACT_NUM",acct_no);
        map.put("TRANS_DT", ClientUtil.getCurrentDate());
        map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        List lst=ClientUtil.executeQuery("IntCalculationDetail", map);
        if(lst==null || lst.isEmpty()){
            lst=ClientUtil.executeQuery("IntCalculationDetailAD", map);
        }
        if(lst !=null && lst.size()>0){
            map=(HashMap)lst.get(0);
            //            setLinkMap(map);
        }
        return map;
    }
    private void setObservable(){
        observable = TermLoanChargesOB.getInstance();
        observable.addObserver(this);
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

        rdoTransactionType = new com.see.truetransact.uicomponent.CButtonGroup();
        panAccountInfo = new com.see.truetransact.uicomponent.CPanel();
        panAccountInfoInner = new com.see.truetransact.uicomponent.CPanel();
        panProductID = new com.see.truetransact.uicomponent.CPanel();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        cboProductType = new com.see.truetransact.uicomponent.CComboBox();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        cboProductID = new com.see.truetransact.uicomponent.CComboBox();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHeadDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        panAcctNum = new com.see.truetransact.uicomponent.CPanel();
        txtAccountNumber = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNumber = new com.see.truetransact.uicomponent.CButton();
        lblCustomerNameDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblChargeType = new com.see.truetransact.uicomponent.CLabel();
        cboChargesType = new com.see.truetransact.uicomponent.CComboBox();
        lblChargesDate = new com.see.truetransact.uicomponent.CLabel();
        tdtChargesDate = new com.see.truetransact.uicomponent.CDateField();
        lblChargeAmount = new com.see.truetransact.uicomponent.CLabel();
        txtChargesAmount = new com.see.truetransact.uicomponent.CTextField();
        panButtonCharges = new com.see.truetransact.uicomponent.CPanel();
        btnChargeNew = new com.see.truetransact.uicomponent.CButton();
        btnChargeSave = new com.see.truetransact.uicomponent.CButton();
        btnEmi = new com.see.truetransact.uicomponent.CButton();
        lblNarration = new com.see.truetransact.uicomponent.CLabel();
        txtNarration = new com.see.truetransact.uicomponent.CTextField();
        sptAccountInfo = new com.see.truetransact.uicomponent.CSeparator();
        panAllChargeDetail = new com.see.truetransact.uicomponent.CPanel();
        panChargeAllDetails = new com.see.truetransact.uicomponent.CPanel();
        scrChargePan = new com.see.truetransact.uicomponent.CScrollPane();
        tblChargesDetails = new com.see.truetransact.uicomponent.CTable();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        panChargeDetails = new com.see.truetransact.uicomponent.CPanel();
        scrChargesPan = new com.see.truetransact.uicomponent.CScrollPane();
        tblChargeDetails = new com.see.truetransact.uicomponent.CTable();
        scrTransDetailsPan = new com.see.truetransact.uicomponent.CScrollPane();
        tblTransDetails = new com.see.truetransact.uicomponent.CTable();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace32 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace33 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
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
        setMinimumSize(new java.awt.Dimension(680, 600));
        setPreferredSize(new java.awt.Dimension(680, 600));

        panAccountInfo.setMinimumSize(new java.awt.Dimension(552, 429));
        panAccountInfo.setPreferredSize(new java.awt.Dimension(552, 429));
        panAccountInfo.setLayout(new java.awt.GridBagLayout());

        panAccountInfoInner.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAccountInfoInner.setMinimumSize(new java.awt.Dimension(728, 270));
        panAccountInfoInner.setPreferredSize(new java.awt.Dimension(728, 270));
        panAccountInfoInner.setLayout(new java.awt.GridBagLayout());

        panProductID.setMinimumSize(new java.awt.Dimension(300, 175));
        panProductID.setPreferredSize(new java.awt.Dimension(300, 175));
        panProductID.setLayout(new java.awt.GridBagLayout());

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblProductType, gridBagConstraints);

        cboProductType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductType.setPopupWidth(210);
        cboProductType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 2, 6, 6);
        panProductID.add(cboProductType, gridBagConstraints);

        lblProductID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblProductID, gridBagConstraints);

        cboProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductID.setPopupWidth(210);
        cboProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(cboProductID, gridBagConstraints);

        lblAccountHead.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblAccountHead, gridBagConstraints);

        lblAccountHeadDisplay.setForeground(new java.awt.Color(0, 51, 204));
        lblAccountHeadDisplay.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccountHeadDisplay.setMaximumSize(new java.awt.Dimension(2250, 21));
        lblAccountHeadDisplay.setMinimumSize(new java.awt.Dimension(150, 21));
        lblAccountHeadDisplay.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblAccountHeadDisplay, gridBagConstraints);

        lblAccountNumber.setText("Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblAccountNumber, gridBagConstraints);

        panAcctNum.setLayout(new java.awt.GridBagLayout());

        txtAccountNumber.setMaxLength(10);
        txtAccountNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccountNumberActionPerformed(evt);
            }
        });
        txtAccountNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panAcctNum.add(txtAccountNumber, gridBagConstraints);

        btnAccountNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountNumber.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnAccountNumber.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAccountNumber.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAccountNumber.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccountNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNumberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panAcctNum.add(btnAccountNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(panAcctNum, gridBagConstraints);

        lblCustomerNameDisplay.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerNameDisplay.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustomerNameDisplay.setMinimumSize(new java.awt.Dimension(170, 21));
        lblCustomerNameDisplay.setPreferredSize(new java.awt.Dimension(170, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblCustomerNameDisplay, gridBagConstraints);

        lblChargeType.setText("Charges Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblChargeType, gridBagConstraints);

        cboChargesType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboChargesType.setPopupWidth(210);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(cboChargesType, gridBagConstraints);

        lblChargesDate.setText("Charges Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblChargesDate, gridBagConstraints);

        tdtChargesDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtChargesDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 3, 6, 5);
        panProductID.add(tdtChargesDate, gridBagConstraints);

        lblChargeAmount.setText("Charge Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblChargeAmount, gridBagConstraints);

        txtChargesAmount.setMaxLength(16);
        txtChargesAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtChargesAmount.setValidation(new NumericValidation());
        txtChargesAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtChargesAmountActionPerformed(evt);
            }
        });
        txtChargesAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChargesAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 6, 4);
        panProductID.add(txtChargesAmount, gridBagConstraints);

        panButtonCharges.setMinimumSize(new java.awt.Dimension(215, 27));
        panButtonCharges.setPreferredSize(new java.awt.Dimension(215, 27));
        panButtonCharges.setLayout(new java.awt.GridBagLayout());

        btnChargeNew.setText("New");
        btnChargeNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChargeNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panButtonCharges.add(btnChargeNew, gridBagConstraints);

        btnChargeSave.setText("Save");
        btnChargeSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChargeSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panButtonCharges.add(btnChargeSave, gridBagConstraints);

        btnEmi.setText("EMI");
        btnEmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmiActionPerformed(evt);
            }
        });
        btnEmi.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnEmiFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panButtonCharges.add(btnEmi, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        panProductID.add(panButtonCharges, gridBagConstraints);

        lblNarration.setText("Narration");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        panProductID.add(lblNarration, gridBagConstraints);

        txtNarration.setMinimumSize(new java.awt.Dimension(140, 21));
        txtNarration.setPreferredSize(new java.awt.Dimension(140, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panProductID.add(txtNarration, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAccountInfoInner.add(panProductID, gridBagConstraints);

        sptAccountInfo.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptAccountInfo.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        panAccountInfoInner.add(sptAccountInfo, gridBagConstraints);

        panAllChargeDetail.setMinimumSize(new java.awt.Dimension(350, 175));
        panAllChargeDetail.setPreferredSize(new java.awt.Dimension(350, 175));
        panAllChargeDetail.setLayout(new java.awt.GridBagLayout());

        panChargeAllDetails.setMinimumSize(new java.awt.Dimension(350, 175));
        panChargeAllDetails.setPreferredSize(new java.awt.Dimension(350, 175));
        panChargeAllDetails.setLayout(new java.awt.GridBagLayout());

        tblChargesDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblChargesDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblChargesDetailsMouseClicked(evt);
            }
        });
        scrChargePan.setViewportView(tblChargesDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panChargeAllDetails.add(scrChargePan, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAllChargeDetail.add(panChargeAllDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountInfoInner.add(panAllChargeDetail, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountInfo.add(panAccountInfoInner, gridBagConstraints);

        panTransaction.setLayout(new java.awt.GridBagLayout());

        panChargeDetails.setMaximumSize(new java.awt.Dimension(91, 46));
        panChargeDetails.setMinimumSize(new java.awt.Dimension(91, 46));
        panChargeDetails.setPreferredSize(new java.awt.Dimension(91, 46));
        panChargeDetails.setLayout(new java.awt.GridBagLayout());

        scrChargesPan.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrChargesPan.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrChargesPan.setMaximumSize(new java.awt.Dimension(250, 46));
        scrChargesPan.setMinimumSize(new java.awt.Dimension(250, 46));
        scrChargesPan.setPreferredSize(new java.awt.Dimension(250, 46));

        tblChargeDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrChargesPan.setViewportView(tblChargeDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panChargeDetails.add(scrChargesPan, gridBagConstraints);

        scrTransDetailsPan.setMinimumSize(new java.awt.Dimension(227, 60));
        scrTransDetailsPan.setPreferredSize(new java.awt.Dimension(227, 60));

        tblTransDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrTransDetailsPan.setViewportView(tblTransDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panChargeDetails.add(scrTransDetailsPan, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panTransaction.add(panChargeDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountInfo.add(panTransaction, gridBagConstraints);

        getContentPane().add(panAccountInfo, java.awt.BorderLayout.CENTER);

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
        tbrOperativeAcctProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnNew);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace29);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace30);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnSave);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace31);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.setEnabled(false);
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnAuthorize);

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace32);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setEnabled(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace33.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace33.setText("     ");
        lblSpace33.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace33);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.setEnabled(false);
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace34);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

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

    private void btnEmiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmiActionPerformed
        // TODO add your handling code here:
          long   noofInstallment=0;
        String  remarks= COptionPane.showInputDialog(this,"NO OF INSTALLMENT WANT TO PAY");
        if(CommonUtil.convertObjToStr(remarks).equals(""))
            remarks="0";
        try {
            noofInstallment=Long.parseLong(remarks);
        } catch (java.lang.NumberFormatException e) {
            ClientUtil.displayAlert("Invalid Number...");
            return;
        }
        HashMap map=new HashMap();
        map.put("ACT_NUM",txtAccountNumber.getText());
        map.put("PROD_ID", ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
        
        map.put("NO_OF_INSTALLMENT",new Long(noofInstallment));
        observable.getAccountClosingCharges(map);
    }//GEN-LAST:event_btnEmiActionPerformed
    
    private void btnEmiFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnEmiFocusLost
        // TODO add your handling code here:
      
        
    }//GEN-LAST:event_btnEmiFocusLost
    
    private void txtAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountNumberActionPerformed
        // TODO add your handling code here:
        //        callView(TXT_NO);
    }//GEN-LAST:event_txtAccountNumberActionPerformed
    
    private void tdtChargesDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtChargesDateFocusLost
        // TODO add your handling code here:
        if(tdtChargesDate.getDateValue()!=null && tdtChargesDate.getDateValue().length()>0){
            ToDateValidation toDate= new ToDateValidation(ClientUtil.getCurrentDate(), true);
            toDate.setComponent(this.tdtChargesDate);
            if(!toDate.validate()) {
                COptionPane.showMessageDialog(this,"Future Date Not Allowed");
                tdtChargesDate.setDateValue("");
                tdtChargesDate.requestFocus();
                return;
            }
        }
    }//GEN-LAST:event_tdtChargesDateFocusLost
    
    private void tblChargesDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChargesDetailsMouseClicked
        // TODO add your handling code here:  boolean value=false;
        int row=tblChargesDetails.getSelectedRow();
       boolean  value=false;
        if(evt.getClickCount()==2){
            if(tblChargesDetails.getValueAt(row,2)!=null && tblChargesDetails.getValueAt(row,3)!=null){
                String a=CommonUtil.convertObjToStr(tblChargesDetails.getValueAt(row,2));
                ArrayList list= (ArrayList)observable.getTblChargesDetails().getDataArrayList().get(row);
                if(observable.checkDeleteRecords(row)){
                     ClientUtil.displayAlert("Charges since Paid ");
                return;
                }
            }
            int yesNo=ClientUtil.confirmationAlert("Do u Want to Delete Record");
            if(yesNo==0){
                System.out.println("deleted");
                observable.deleteData(tblChargesDetails.getSelectedRow());
            }
        }
      
    }//GEN-LAST:event_tblChargesDetailsMouseClicked
    
    private void btnChargeNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChargeNewActionPerformed
        // TODO add your handling code here:
        ClientUtil.enableDisable(panProductID,true);
        if(btnEditPressed)
            btnAccountNumber.setEnabled(false);
        else
            btnAccountNumber.setEnabled(true);
        resetFields();
        btnSubNewPressed = true;
        cboProductType.setEnabled(false);
        cboProductID.setEnabled(false);
        txtAccountNumber.setEnabled(false);
        btnAccountNumber.setEnabled(false);
        if (btnMainNewPressed && btnSubNewPressed) {
            //            setMainDetail();
        }
    }//GEN-LAST:event_btnChargeNewActionPerformed
    
    private void setMainDetail() {
        cboProductType.setEnabled(true);
        cboProductID.setEnabled(true);
        txtAccountNumber.setEnabled(true);
        cboProductType.setSelectedItem("");
        cboProductID.setSelectedItem("");
        txtAccountNumber.setText("");
    }
    
    private void btnChargeSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChargeSaveActionPerformed
        // TODO add your handling code here:
        MandatoryCheck objMandatory = new MandatoryCheck();
        TermLoanChargesHashMap objMap = new TermLoanChargesHashMap();
        HashMap mandatoryMap = objMap.getMandatoryHashMap();
        String mandatoryMessage = objMandatory.checkMandatory(getClass().getName(),panProductID, mandatoryMap);
        if(mandatoryMessage.length() > 0 ){
            ClientUtil.displayAlert(mandatoryMessage);
            return;
        }
        updateOBFields();
        observable.insertData();
        ClientUtil.enableDisable(panProductID,false);
        btnAccountNumber.setEnabled(false);
        resetFields();
        btnMainNewPressed = false;
        //        ClientUtil.enableDisable(panProductID,false);
        //
    }//GEN-LAST:event_btnChargeSaveActionPerformed
    private void updateOBFields(){
        observable.setCboProductType(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString());
        observable.setCboProductID(((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString());
        observable.setCboChargesType(((ComboBoxModel)cboChargesType.getModel()).getKeyForSelected().toString());
        observable.setTxtChargesAmount(new Double(txtChargesAmount.getText()));
        observable.setTxtNarration(txtNarration.getText()); //KD-3483
        observable.setTxtAccountNumber(txtAccountNumber.getText());
        observable.setTdtChargesDate(tdtChargesDate.getDateValue());
    }
    private void resetFields(){
        cboChargesType.setSelectedItem("");
        txtChargesAmount.setText("");
        tdtChargesDate.setDateValue("");
        txtNarration.setText(""); //KD-3483
    }
    private void cboProductTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductTypeActionPerformed
        // TODO add your handling code here:
        if(cboProductType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
            cboProductID.setModel(new ComboBoxModel());
            observable.setProdType(prodType);
            observable.getProducts();
            cboProductID.setModel(observable.getCbmProductID());
        }
    }//GEN-LAST:event_cboProductTypeActionPerformed
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void txtChargesAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtChargesAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtChargesAmountActionPerformed
    
    private void txtChargesAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChargesAmountFocusLost
        
    }//GEN-LAST:event_txtChargesAmountFocusLost
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authroizeActionPerformed();
        
        
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authroizeActionPerformed();
        
        
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void authroizeActionPerformed(){
        if (viewType == AUTHORIZE && isFilled){
            viewType=0;
            HashMap map=new HashMap();
            map.put("ACT_NUM",txtAccountNumber.getText());
            map.put("AUTHORIZE_BY",TrueTransactMain.USER_ID);
            map.put("AUTHORIZE_DATE",ClientUtil.getCurrentDate());
            map.put("AUTHORIZE_STATUS","AUTHORIZED");
            observable.setAuthorizeMap(map);
            observable.doAction();
            if (observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                observable.resetForm();
            }
            btnCancelActionPerformed();
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE)
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
             if(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT)
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
             if(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION)
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            observable.setResultStatus();
             if(fromNewAuthorizeUI){
                this.dispose();
                newauthorizeListUI.removeSelectedRow();
                newauthorizeListUI.setFocusToTable();
                fromNewAuthorizeUI = false;
                newauthorizeListUI.displayDetails("Termloan Charges");
            }  
              if(fromAuthorizeUI){
                this.dispose();
                authorizeListUI.removeSelectedRow();
                authorizeListUI.setFocusToTable();
                fromAuthorizeUI = false;
                authorizeListUI.displayDetails("Termloan Charges");
            }
        }else{
            HashMap mapParam=new HashMap();
            HashMap map=new HashMap();
            map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            mapParam.put(CommonConstants.MAP_NAME,"viewChargesDetailsForAuthorize");
            isFilled=false;
            mapParam.put(CommonConstants.MAP_WHERE,map);
            
            int actionType=observable.getActionType();
             String authSts="";
            if(actionType==10)
            {
                authSts="REJECTED";
            }
            else if(actionType==8)
            {
                authSts="AUTHORIZED";
            }
            
              mapParam.put(CommonConstants.AUTHORIZESTATUS,authSts);
            
            viewType=AUTHORIZE;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            menuEnableDisable();
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
            btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
            btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            //            if(observable.getActionType()!=ClientConstants.ACTIONTYPE_AUTHORIZE){
            //                btnReject.setEnabled(!btnReject.isEnabled());
            //
            //            }else{
            //                btnAuthorize.setEnabled(!btnAuthorize.isEnabled());
            //            }
            
        }
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
        btnCloseActionPerformed(evt);
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        
    }//GEN-LAST:event_btnDeleteActionPerformed
    private void popUp(String field) {
        
        final HashMap viewMap = new HashMap();
        String loanStatus="";
        if(field.equals("CLOSED_ACCT")){
            loanStatus=field;
            field="Enquirystatus";
        }
        //if ( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||  observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
        if(field.equals("Edit") || field.equals("Delete") || field.equals("Enquirystatus") ) {
            viewType=EDIT;
            //            super.removeEditLock(lblBorrowerNo_2.getText()); remove only accour authorization by abi
            
            //            ArrayList lst = new ArrayList();
            //            lst.add("BORROWER NO");
            //            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            //            lst = null;
            
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
            HashMap editMapCondition = new HashMap();
            editMapCondition.put("BRANCH_ID", getSelectedBranchID());
            if(loanStatus.length()>0)
                editMapCondition.put("CLOSED_ACCT", "CLOSED_ACCT");
            viewMap.put(CommonConstants.MAP_WHERE, editMapCondition);
            //            if (loanType.equals("LTD"))
            viewMap.put(CommonConstants.MAP_NAME, "viewChargesDetails");
            //
            //mapped statement: viewTermLoan---> result map should be a Hashmap in OB...
            
            
        }
        //            updateOBFields();
        //            viewMap.put(CommonConstants.MAP_NAME, "Cash.getGuarantorAccountList"+strSelectedProdType);
        HashMap whereListMap = new HashMap();
        //            whereListMap.put("CUST_ID", txtCustomerID_GD.getText());
        //            whereListMap.put("PROD_ID", strSelectedProdID);
        viewMap.put(CommonConstants.MAP_WHERE, whereListMap);
        //        }
        new ViewAll(this, viewMap).show();
    }
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        btnEditPressed=true;
        popUp("Edit");
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        
    }//GEN-LAST:event_mitCancelActionPerformed
    private void menuEnable(boolean flag){
        btnNew.setEnabled(flag);
        btnEdit.setEnabled(flag);
        btnDelete.setEnabled(flag);
        btnSave.setEnabled(!flag);
        btnCancel.setEnabled(!flag);
        btnAuthorize.setEnabled(flag);
        btnReject.setEnabled(flag);
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        btnCancelActionPerformed();
        menuEnable(true);
        setModified(false);
        lblCustomerNameDisplay.setText("");
        lblAccountHeadDisplay.setText("");
        btnEmi.setEnabled(false);
        if (fromNewAuthorizeUI) {
            this.dispose();
            newauthorizeListUI.setFocusToTable();
            fromNewAuthorizeUI = false;
        } 
         if (fromAuthorizeUI) {
            this.dispose();
            authorizeListUI.setFocusToTable();
            fromAuthorizeUI = false;
        } 
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnCancelActionPerformed(){
        observable.resetForm();
        btnEditPressed=false;
        btnMainNewPressed=false;
        ClientUtil.enableDisable(this,false);
        menuEnableDisable();
        buttonEnableDisable(false);
        observable.setAuthorizeMap(null);
        isFilled=false;
    }
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        //System.out.println("observable.getActionType() : " + observable.getActionType());
        setModified(false);
        if(tblChargesDetails.getRowCount()>0){
            observable.doAction();
            btnEditPressed=false;
            if (observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                observable.resetForm();
            }
            btnCancelActionPerformed(evt);
            observable.setResultStatus();
        } else {
            ClientUtil.displayAlert("With out Data Can't save");
            return;
        }
        
        
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        
        menuEnableDisable();
        buttonEnableDisable(true);
        btnMainNewPressed = true;
        //        btnChargeNewActionPerformed(evt);
        setMainDetail();
        resetFields();
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        menuEnableDisable();
        buttonEnableDisable(true);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        //        btnNewActionPerformed(evt);
        btnMainNewPressed=false;
        btnChargeNewActionPerformed(evt);
        btnMainNewPressed = false;
        
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void txtAccountNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNumberFocusLost
        //        txtAccountNumberActionPerformed(null);
        callView(TXT_NO);
    }//GEN-LAST:event_txtAccountNumberFocusLost
    
    private void btnAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNumberActionPerformed
        callView(ACT_NUM);
    }//GEN-LAST:event_btnAccountNumberActionPerformed
    private void callView(int currField) {
        HashMap viewMap = new HashMap();
        HashMap where = new HashMap();
        viewType = currField;
        if (currField == ACT_NUM) {
            String prodType = ((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
            if(prodType!=null && prodType.equals("MDS")){
                String prodId=((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString();
                HashMap map1 = new HashMap();
                HashMap mapTrans = new HashMap();
                map1.put("SCHEME_NAME",prodId);
                List list1 = ClientUtil.executeQuery("getSelectTransDet", map1);
                if (list1 != null && list1.size() > 0) {
                    mapTrans = (HashMap) list1.get(0);
                    if (CommonUtil.convertObjToStr(mapTrans.get("TRANS_FIRST_INSTALLMENT")).equals("Y")) {
                        where.put("SCHEME_NAMES",prodId);
                        viewMap.put(CommonConstants.MAP_WHERE, where);
                        viewMap.put(CommonConstants.MAP_NAME, "getSelectChittalReceiptEntryDetails");
                    } else {
                        where.put("SCHEME_NAMES", prodId);
                        viewMap.put(CommonConstants.MAP_WHERE, where);
                        viewMap.put(CommonConstants.MAP_NAME, "getSelectChittalReceiptEntryDetailsN");
                    }
                }
            }
            else{
            //            this.txtAccountNo.setText("");
              if(prodType!=null && prodType.equals("TD"))
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListTD");
                else
            viewMap.put(CommonConstants.MAP_NAME, "Transfer.getAccountListCharges");
            HashMap whereListMap = new HashMap();
            whereListMap.put("PROD_ID", ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
            whereListMap.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
            //            if(observable.getTransType() !=null) {
            //                String types="TL";
            //                //                if(observable.getTransType().equals(CommonConstants.DEBIT) && types.equals(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString())){
            //                //                    whereListMap.put(CommonConstants.DEBIT, null);
            //                //                }
            //                if(observable.getTransType().equals(CommonConstants.CREDIT) && types.equals(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString())){
            //                    whereListMap.put(CommonConstants.CREDIT, null);
            //                }
            //            }
            //            ArrayList presentActNums = new ArrayList();
            //            int rowCount = this.tblTransList.getRowCount();
            //
            //            if(rowCount!=0) {
            //                String actNum = "";
            //                for(int i=0;i<rowCount;i++){
            //                    actNum = CommonUtil.convertObjToStr(tblTransList.getValueAt(i,0));
            //                    if (actNum != null && !actNum.equals(""))
            //                        presentActNums.add(actNum);
            //                }
            //            }
            //            if (presentActNums != null)
            //                whereListMap.put("ACT NUM",presentActNums);
            
            viewMap.put(CommonConstants.MAP_WHERE, whereListMap);
            System.out.println("viewMap:"+viewMap);
         }
              new ViewAll(this, viewMap).show();
        }
        if(currField == TXT_NO){
            HashMap whereListMap = new HashMap();
            whereListMap.put("ACCOUNTNO",txtAccountNumber.getText());
            whereListMap.put("PROD_ID", ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
            List lst = ClientUtil.executeQuery("TermLoanChargescustomerName", whereListMap);
            if(lst!=null && lst.size()>0){
                whereListMap = (HashMap)lst.get(0);
                whereListMap.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
                lblCustomerNameDisplay.setText(CommonUtil.convertObjToStr(whereListMap.get("NAME")));
                whereListMap.put("CUSTOMERNAME",whereListMap.get("NAME"));
                whereListMap.put("PROD_ID", ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
                whereListMap.put("ACCOUNTNO",txtAccountNumber.getText());
            }else{
                ClientUtil.displayAlert("Invalid Account No...");
                txtAccountNumber.setText("");
                return;
            }
            fillData(whereListMap);
        }
        if(lblCustomerNameDisplay.getText().equals("")){
            ClientUtil.displayAlert("Invalid Account No...");
            txtAccountNumber.setText("");
        }
    }
    private void cboProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIDActionPerformed
        //To get the AccountHead details for a proper ProductID
        String prodId= ((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString();
        if( prodId !=null && prodId.length()>0){
            observable.setCboProductID(prodId);
            observable.getAccountHeadForProductId(prodId);
            
            lblAccountHeadDisplay.setText(observable.getAccountHeadId());
        }
    }//GEN-LAST:event_cboProductIDActionPerformed
    
    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccountNumber;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnChargeNew;
    private com.see.truetransact.uicomponent.CButton btnChargeSave;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnEmi;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboChargesType;
    private com.see.truetransact.uicomponent.CComboBox cboProductID;
    private com.see.truetransact.uicomponent.CComboBox cboProductType;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadDisplay;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblChargeAmount;
    private com.see.truetransact.uicomponent.CLabel lblChargeType;
    private com.see.truetransact.uicomponent.CLabel lblChargesDate;
    private com.see.truetransact.uicomponent.CLabel lblCustomerNameDisplay;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNarration;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
    private com.see.truetransact.uicomponent.CLabel lblSpace33;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountInfo;
    private com.see.truetransact.uicomponent.CPanel panAccountInfoInner;
    private com.see.truetransact.uicomponent.CPanel panAcctNum;
    private com.see.truetransact.uicomponent.CPanel panAllChargeDetail;
    private com.see.truetransact.uicomponent.CPanel panButtonCharges;
    private com.see.truetransact.uicomponent.CPanel panChargeAllDetails;
    private com.see.truetransact.uicomponent.CPanel panChargeDetails;
    private com.see.truetransact.uicomponent.CPanel panProductID;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransactionType;
    private com.see.truetransact.uicomponent.CScrollPane scrChargePan;
    private com.see.truetransact.uicomponent.CScrollPane scrChargesPan;
    private com.see.truetransact.uicomponent.CScrollPane scrTransDetailsPan;
    private com.see.truetransact.uicomponent.CSeparator sptAccountInfo;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CTable tblChargeDetails;
    private com.see.truetransact.uicomponent.CTable tblChargesDetails;
    private com.see.truetransact.uicomponent.CTable tblTransDetails;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtChargesDate;
    private com.see.truetransact.uicomponent.CTextField txtAccountNumber;
    private com.see.truetransact.uicomponent.CTextField txtChargesAmount;
    private com.see.truetransact.uicomponent.CTextField txtNarration;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
        TermLoanChargesUI ac = new TermLoanChargesUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(ac);
        j.show();
        ac.show();
    }
    
    /**
     * Getter for property mandatoryMap.
     * @return Value of property mandatoryMap.
     */
    public java.util.HashMap getMandatoryMap() {
        return mandatoryMap;
    }
    
    /**
     * Setter for property mandatoryMap.
     * @param mandatoryMap New value of property mandatoryMap.
     */
    public void setMandatoryMap(java.util.HashMap mandatoryMap) {
        this.mandatoryMap = mandatoryMap;
    }
    
}
