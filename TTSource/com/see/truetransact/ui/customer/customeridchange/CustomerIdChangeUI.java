/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerIdChangeUI.java
 *
 * Created on feb 9, 2009, 10:53 AM
 */

package com.see.truetransact.ui.customer.customeridchange;

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
import java.util.Date;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;




/** This form is used to manipulate CustomerIdChangeUI related functionality
 * @author swaroop
 */
public class CustomerIdChangeUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField{
    final int AUTHORIZE=3, CANCEL=0 ;
    int viewType=-1;
    private HashMap mandatoryMap;
    private CustomerIdChangeOB observable;
    private final static Logger log = Logger.getLogger(CustomerIdChangeUI.class);
    CustomerIdChangeRB custIdChangeRB = new CustomerIdChangeRB();
    private String prodType="";
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    String cust_type=null;
    private Date currDt = null;
    /** Creates new form CustomerIdChangeUI */
    public CustomerIdChangeUI() {
        initComponents();
        initStartUp();
    }
    
    private void initStartUp(){
        currDt = ClientUtil.getCurrentDate();
        setMandatoryHashMap();
        setFieldNames();
        internationalize();
        observable = new CustomerIdChangeOB();
        observable.addObserver(this);
        initComponentData();
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
         objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.customer.customeridchange.CustomerIdChangeMRB", ProxyParameters.LANGUAGE);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panProductID);
         setHelpMessage();
         btnAccountNumber.setEnabled(false);
         btnNewCustID.setEnabled(false);
    }
    
    private void setMaxLength() {
         txtNewCustId.setMaxLength(64);
         txtOldCustID.setMaxLength(16);
         txtAccountNumber.setMaxLength(16);
         txtAccountNumber.setAllowAll(true);
         txtOldCustID.setAllowAll(true);
         txtNewCustId.setAllowAll(true);
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("cboProductId", new Boolean(true));
        mandatoryMap.put("txtAccountNumber", new Boolean(true));
        mandatoryMap.put("txtOldCustID", new Boolean(true));
        mandatoryMap.put("txtNewCustId", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    /****************** NEW METHODS *****************/
    
    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType != AUTHORIZE){
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put("AUTHORIZE_MODE","AUTHORIZE_MODE");
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getSelectAccountsForEdit");
            viewType = AUTHORIZE;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            //            isFilled = false;
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        } else if (viewType == AUTHORIZE){
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("ACT_NUM", observable.getTxtAccountNumber());
            singleAuthorizeMap.put("OLD_CUST_ID", observable.getOldCustID());
            singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTH_DT",ClientUtil.getCurrentDateWithTime());
            singleAuthorizeMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            singleAuthorizeMap.put("OLD_CUST_ID", observable.getOldCustID());
            singleAuthorizeMap.put("NEW_CUST_ID", observable.getNewCustID());
            singleAuthorizeMap.put("PROD_TYPE", CommonUtil.convertObjToStr(observable.getCbmProdType().getKeyForSelected()));
            if(CommonUtil.convertObjToStr(observable.getCbmProdType().getKeyForSelected()).equalsIgnoreCase("SA")){
                HashMap whrMap = new HashMap();
                whrMap.put("NEW_CUST_ID",observable.getNewCustID());
                List newCustDetailsLst = ClientUtil.executeQuery("getSelectCustDetailsForSuspenseActChange", whrMap);
                if(newCustDetailsLst != null && newCustDetailsLst.size() > 0 ){
                    HashMap custMap = (HashMap)newCustDetailsLst.get(0);
                    singleAuthorizeMap.put("NEW_CUST_NAME", custMap.get("NAME"));
                    singleAuthorizeMap.put("NEW_CUST_ADDRESS", custMap.get("ADDRESS"));
                    singleAuthorizeMap.put("MEMBERSHIP_NO", custMap.get("MEMBERSHIP_NO"));
                }
            }
            arrList.add(singleAuthorizeMap);
            
            singleAuthorizeMap.put("TRANS_ID", observable.getTxtAccountNumber());
            singleAuthorizeMap.put("USER_ID",TrueTransactMain.USER_ID);
            List lst=ClientUtil.executeQuery("selectauthorizationLock", singleAuthorizeMap);
            if(lst !=null && lst.size()>0) {
                HashMap map=new HashMap();
                StringBuffer open=new StringBuffer();
                for(int i=0;i<lst.size();i++){
                    map=(HashMap)lst.get(i);
                    open.append ("\n"+"User Id  :"+" ");
                    open.append(CommonUtil.convertObjToStr(map.get("OPEN_BY"))+"\n");
                    open.append("Mode Of Operation  :" +" ");
                    open.append(CommonUtil.convertObjToStr(map.get("MODE_OF_OPERATION"))+" ");                
                }
                ClientUtil.showMessageWindow("already open by"+open);           
                return;
            }
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap,observable.getTxtAccountNumber(), observable.getTxtStatusBy());
            
            
        }
    }
    
    public void authorize(HashMap map, String ac_num,String statusBy) {
        System.out.println("Authorize Map : " + map);
        
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
//            observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.set_authorizeMap(map);
            observable.doAction();
            if(observable.getResult()!=4){
                super.setOpenForEditBy(statusBy);
                super.removeEditLock(ac_num);
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
            
        }
    }
    public void setHelpMessage() {
        cboProductId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductId"));
        cboProdType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdType"));
        txtAccountNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccountNumber"));
        txtOldCustID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOldCustID"));
        txtNewCustId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNewCustId"));
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
        lblAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        lblOldCustID = new com.see.truetransact.uicomponent.CLabel();
        lblAcctNameDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblNewCustId = new com.see.truetransact.uicomponent.CLabel();
        panNewCustID = new com.see.truetransact.uicomponent.CPanel();
        txtNewCustId = new com.see.truetransact.uicomponent.CTextField();
        btnNewCustID = new com.see.truetransact.uicomponent.CButton();
        cboProductId = new com.see.truetransact.uicomponent.CComboBox();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        txtOldCustID = new com.see.truetransact.uicomponent.CTextField();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        panAcctNum = new com.see.truetransact.uicomponent.CPanel();
        txtAccountNumber = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNumber = new com.see.truetransact.uicomponent.CButton();
        lblAcctNameDisplay2 = new com.see.truetransact.uicomponent.CLabel();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
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
        setMaximumSize(new java.awt.Dimension(500, 500));
        setMinimumSize(new java.awt.Dimension(500, 500));
        setPreferredSize(new java.awt.Dimension(500, 500));

        panAccountInfo.setMinimumSize(new java.awt.Dimension(360, 260));
        panAccountInfo.setPreferredSize(new java.awt.Dimension(360, 260));
        panAccountInfo.setLayout(new java.awt.GridBagLayout());

        panAccountInfoInner.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAccountInfoInner.setMaximumSize(new java.awt.Dimension(350, 260));
        panAccountInfoInner.setMinimumSize(new java.awt.Dimension(350, 260));
        panAccountInfoInner.setPreferredSize(new java.awt.Dimension(350, 260));
        panAccountInfoInner.setLayout(new java.awt.GridBagLayout());

        panProductID.setMaximumSize(new java.awt.Dimension(350, 250));
        panProductID.setMinimumSize(new java.awt.Dimension(350, 250));
        panProductID.setPreferredSize(new java.awt.Dimension(350, 250));
        panProductID.setLayout(new java.awt.GridBagLayout());

        lblAccountNumber.setText("Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(lblAccountNumber, gridBagConstraints);

        lblOldCustID.setText("Existing Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(lblOldCustID, gridBagConstraints);

        lblAcctNameDisplay.setForeground(new java.awt.Color(0, 51, 204));
        lblAcctNameDisplay.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAcctNameDisplay.setMinimumSize(new java.awt.Dimension(225, 21));
        lblAcctNameDisplay.setPreferredSize(new java.awt.Dimension(225, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 90, 2, 2);
        panProductID.add(lblAcctNameDisplay, gridBagConstraints);

        lblNewCustId.setText("New Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(lblNewCustId, gridBagConstraints);

        panNewCustID.setLayout(new java.awt.GridBagLayout());

        txtNewCustId.setMaxLength(10);
        txtNewCustId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNewCustId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNewCustIdActionPerformed(evt);
            }
        });
        txtNewCustId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNewCustIdFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNewCustIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panNewCustID.add(txtNewCustId, gridBagConstraints);

        btnNewCustID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnNewCustID.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnNewCustID.setMaximumSize(new java.awt.Dimension(21, 21));
        btnNewCustID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnNewCustID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnNewCustID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewCustIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panNewCustID.add(btnNewCustID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(panNewCustID, gridBagConstraints);

        cboProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductId.setPopupWidth(225);
        cboProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(cboProductId, gridBagConstraints);

        lblProductId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(lblProductId, gridBagConstraints);

        txtOldCustID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(txtOldCustID, gridBagConstraints);

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(cboProdType, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(lblProdType, gridBagConstraints);

        panAcctNum.setLayout(new java.awt.GridBagLayout());

        txtAccountNumber.setMaxLength(10);
        txtAccountNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccountNumberActionPerformed(evt);
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
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panProductID.add(panAcctNum, gridBagConstraints);

        lblAcctNameDisplay2.setForeground(new java.awt.Color(0, 51, 204));
        lblAcctNameDisplay2.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAcctNameDisplay2.setMinimumSize(new java.awt.Dimension(225, 21));
        lblAcctNameDisplay2.setPreferredSize(new java.awt.Dimension(225, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 90, 2, 2);
        panProductID.add(lblAcctNameDisplay2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAccountInfoInner.add(panProductID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountInfo.add(panAccountInfoInner, gridBagConstraints);

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

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace25);

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

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace26);

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
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnAuthorize);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace27);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace28);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
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

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace30);

        btnDateChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/denomination.gif"))); // NOI18N
        btnDateChange.setToolTipText("Exception");
        btnDateChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateChangeActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnDateChange);

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

        mbrCustomer.setName("mbrCustomer");

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess");

        mitNew.setText("New");
        mitNew.setName("mitNew");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew");
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave");
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose");
        mnuProcess.add(mitClose);

        mbrCustomer.add(mnuProcess);

        setJMenuBar(mbrCustomer);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNumberActionPerformed
        // TODO add your handling code here:
        viewType =1;
        popUp();
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnAccountNumberActionPerformed

    private void txtAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountNumberActionPerformed
        // TODO add your handling code here:
        String ACCOUNTNO = (String) txtAccountNumber.getText();
        if (ACCOUNTNO.length()>0) {
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO)) {
                cboProductIdActionPerformed(null);
                txtAccountNumber.setText(observable.getTxtAccountNumber());
                ACCOUNTNO = (String) txtAccountNumber.getText();
//                txtAccountNumber.setText(ACCOUNTNO);
                viewType = 1;
                HashMap where = new HashMap();
                where.put("ACC_NUM",ACCOUNTNO);
                List lst = ClientUtil.executeQuery("getAccountNumberName"+((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),where);
                 if(lst!=null && lst.size()>0){
                     where=(HashMap)lst.get(0);
                     where.put("ACT_NUM",ACCOUNTNO);
                     fillData(where);
                     txtAccountNumber.setText(ACCOUNTNO);
                     lblAcctNameDisplay.setText(CommonUtil.convertObjToStr(where.get("CUSTOMER_NAME")));
                     txtOldCustID.setText(CommonUtil.convertObjToStr(where.get("CUST_ID")));
                 }else{
                    ClientUtil.displayAlert("Invalid Number");
                    txtAccountNumber.setText("");
                    lblAcctNameDisplay.setText("");
                }       
            } else {
                ClientUtil.showAlertWindow("Invalid Account No.");
                txtAccountNumber.setText("");
                return;
            }
        }       
    }//GEN-LAST:event_txtAccountNumberActionPerformed

    private void btnDateChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateChangeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDateChangeActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        btnView.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        popUp();
         btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        lblStatus.setText(observable.getLblStatus());
        btnDateChange.setEnabled(false);
        String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
    }//GEN-LAST:event_btnViewActionPerformed

    private void txtNewCustIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNewCustIdActionPerformed
        // TODO add your handling code here:
        if(cust_type!=null){
        HashMap newCustMap= new HashMap();
        List custList;
         newCustMap.put("CUST_ID",CommonUtil.convertObjToStr(txtNewCustId.getText()));
         if(cust_type.equalsIgnoreCase("INDIVIDUAL")){
          custList= ClientUtil.executeQuery("EnqCustIDIND", newCustMap);
         }
         else{
            custList= ClientUtil.executeQuery("EnqCustIDCORP", newCustMap); 
         }
         if(custList.size()<=0){
             ClientUtil.displayAlert("Customer Number Entered does Not Exist..Please Enter Valid Cusomer No");
             txtNewCustId.setText("");
             return;
         }
         else{
             newCustMap=null;
             newCustMap= (HashMap)custList.get(0);
             txtNewCustId.setText(CommonUtil.convertObjToStr(newCustMap.get("CUST_ID")));
             lblAcctNameDisplay2.setText(CommonUtil.convertObjToStr(newCustMap.get("CUSTOMER_NAME")));
          }
        }
    }//GEN-LAST:event_txtNewCustIdActionPerformed

    private void txtNewCustIdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNewCustIdFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNewCustIdFocusGained

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        observable.setCboProdType((String) cboProdType.getSelectedItem());
        String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
        observable.setCbmProductId(prodType);
        cboProductId.setModel(observable.getCbmProductId());
        clearProdFields();
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void cboProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIdActionPerformed
        // TODO add your handling code here:
        observable.setCboProductId((String) cboProductId.getSelectedItem());
        clearProdFields();
    }//GEN-LAST:event_cboProductIdActionPerformed
                
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
       observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
         btnCancel.setEnabled(true);
         btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mitCloseActionPerformed(evt);
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
        cifClosingAlert();
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
       observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp();
        btnAccountNumber.setEnabled(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
     popUp();
    lblStatus.setText(observable.getLblStatus());
     String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
     txtAccountNumber.setEnabled(false);
     btnAccountNumber.setEnabled(false);
     txtOldCustID.setEnabled(false);
     cboProdType.setEnabled(false);
     cboProductId.setEnabled(false);
     txtNewCustId.setEnabled(true);
     btnNewCustID.setEnabled(true);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        deletescreenLock();
        viewType = CANCEL ;
         observable.resetForm();  
         setButtonEnableDisable();
         btnView.setEnabled(true);
         btnAccountNumber.setEnabled(false);
         btnNewCustID.setEnabled(false);
         ClientUtil.enableDisable(this, false);
         lblStatus.setText("");
         txtNewCustId.setText("");
         cust_type= null;
         setModified(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        setModified(false);
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panProductID);
        if(mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }else{
        updateOBFields();
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            HashMap actListMap  =new HashMap();
            actListMap.put("ACT_NUM",CommonUtil.convertObjToStr(txtAccountNumber.getText()));
            List actList=ClientUtil.executeQuery("EnqActNum",actListMap );
            if(actList!= null && actList.size()>0){
                    ClientUtil.showMessageWindow("UnAuthorized Record Already Exists For This Accountt Numbber !!! ");
                observable.resetForm();
                return;
            }
        }
        HashMap newCustMap= new HashMap();
        List custList;
        newCustMap.put("CUST_ID",CommonUtil.convertObjToStr(txtNewCustId.getText()));
        if(cust_type.equalsIgnoreCase("INDIVIDUAL")){
            custList= ClientUtil.executeQuery("EnqCustIDIND", newCustMap);
        }
        else{
            custList= ClientUtil.executeQuery("EnqCustIDCORP", newCustMap);
        }
        if(custList.size()<=0){
                ClientUtil.showMessageWindow("Customer Number Entered does Not Exist..Please Enter Valid Cusomer No");
            return;
        }
            savePerformed();
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        enableDisable(true);
        setButtonEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        txtOldCustID.setEnabled(false);
        observable.resetForm();
        btnAccountNumber.setEnabled(true);
        btnNewCustID.setEnabled(true);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        setModified(true);
        
        
        
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
    
    private void txtNewCustIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNewCustIdFocusLost
         
    }//GEN-LAST:event_txtNewCustIdFocusLost
    
    private void btnNewCustIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewCustIDActionPerformed
//        viewType=2;
//        popUp();    
        viewType = 2;
        HashMap sourceMap = new HashMap();
        if(cust_type!=null){
            if(cust_type.equalsIgnoreCase("INDIVIDUAL")){
                sourceMap.put("CUST_TYPE","INDIVIDUAL");
            }
            else{
                sourceMap.put("CUST_TYPE_NOT_INDIVIDUAL","CUST_TYPE_NOT_INDIVIDUAL");
            }
        }
        new CheckCustomerIdUI(this,sourceMap);
    }//GEN-LAST:event_btnNewCustIDActionPerformed
        
    /** To populate Comboboxes */
    private void initComponentData() {
        cboProdType.setModel(observable.getCbmProdType());
    }
    
    /** To display a popUp window for viewing existing data */
    private void popUp(){
        HashMap testMap = null;
        //To display customer info based on the selected ProductID
        
        if( (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ||viewType==1) && viewType!=2){
            testMap = accountViewMap();
            new ViewAll(this, testMap, true).show();
            viewType =-1;
        }
        else if(viewType==2){
            if(cust_type!=null){
                testMap= new HashMap();
                final HashMap whereMap = new HashMap();
                if(cust_type.equalsIgnoreCase("INDIVIDUAL")){
                    testMap.put("MAPNAME", "getSelectCustIDForCustChangeIND");
                }
                else{
                    testMap.put("MAPNAME", "getSelectCustIDForCustChangeCORP");
                }
                whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                testMap.put(CommonConstants.MAP_WHERE, whereMap);
                new ViewAll(this, testMap, true).show();
            }
        }
        
        else if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            testMap= new HashMap();
            testMap.put("MAPNAME", "getSelectAccountsForEdit");
            final HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            testMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, testMap, true).show();
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW){
            testMap= new HashMap();
            testMap.put("MAPNAME", "getSelectAccountsForView");
            final HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            testMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, testMap, true).show();
        }
    }
    private void clearPreviousAccountDetails(){
        
    }
    
    /** Called by the Popup window created thru popUp method */
    public void fillData(Object obj) {
        try{
            final HashMap hash = (HashMap) obj;
            System.out.println("filldata####"+hash);
            if(hash.containsKey("NAME")){
                hash.put("CUSTOMER_NAME",hash.get("NAME"));
            }
            if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || viewType==2){
                fillDataNew(hash);
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && viewType!=2){
                    List actList=ClientUtil.executeQuery("EnqActNum", hash);
                    if(actList!= null && actList.size()>0){
                        ClientUtil.displayAlert("UnAuthorized Record Already Exists For This Act Number");
                        txtAccountNumber.setText("");
                        txtOldCustID.setText("");
                        observable.resetForm();
                        return;
                    }
                }
                
            }
            else if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
                fillDataEdit(hash);
                setButtonEnableDisable();
                observable.setStatus();
            }  else if( observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
            || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION || observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW){
                fillDataEdit(hash);
                setButton4Authorize();
            }
            observable.setCustType(CommonUtil.convertObjToStr(hash.get("CUST_TYPE")));
            if(hash.containsKey("CUST_TYPE")){
            cust_type=CommonUtil.convertObjToStr(hash.get("CUST_TYPE"));
            }
            String ACT_NUM=(String)hash.get("ACT_NUM");
            if(ACT_NUM.equals(""))
                ACT_NUM=(String)hash.get("ACT_NUM");
            String prod_type= (String)((ComboBoxModel)cboProdType.getModel()).getKeyForSelected();
            if(prod_type.equals(""))
                prod_type =(String)hash.get("PROD_TYPE");
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE|| observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
                   if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)
                       hash.put("MODE_OF_OPERATION","EDIT");
                   if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE)
                       hash.put("MODE_OF_OPERATION","DELETE");
                   if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE)
                       hash.put("MODE_OF_OPERATION","AUTHORIZE");
                   hash.put("TRANS_ID",CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
                   hash.put("USER_ID",TrueTransactMain.USER_ID);
                   ClientUtil.execute("insertauthorizationLock", hash);
               }
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                String lockedBy = "";
        HashMap map = new HashMap();
        map.put("SCREEN_ID", getScreenID());
        map.put("RECORD_KEY", CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
        map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        System.out.println("Record Key Map : " + map);
        List lstLock = ClientUtil.executeQuery("selectEditLock", map);
        if (lstLock.size() > 0) {
            lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
            if (!lockedBy.equals(ProxyParameters.USER_ID)) {
                //                            setMode(ClientConstants.ACTIONTYPE_VIEW_MODE);
                btnSave.setEnabled(false);
            } else {
                //                            setMode(ClientConstants.ACTIONTYPE_EDIT);
                btnSave.setEnabled(true);
            }
        } else {
            //                        setMode(ClientConstants.ACTIONTYPE_EDIT);
            btnSave.setEnabled(true);
        }
        setOpenForEditBy(lockedBy);
        if (lockedBy.equals(""))
            ClientUtil.execute("insertEditLock", map);
        if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
            String data = getLockDetails(lockedBy, getScreenID()) ;
            ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
            //                    setMode(ClientConstants.ACTIONTYPE_VIEW_MODE);
            btnSave.setEnabled(false);
        }
            }
        }catch(Exception e){
            log.error(e);
        }
    }
    
    
    
    
    /** To fillData for a new entry */
    private void fillDataNew(HashMap hash){
        if(viewType==2){
            txtNewCustId.setText((String)hash.get("CUST_ID"));
            lblAcctNameDisplay2.setText((String)hash.get("CUSTOMER_NAME"));
        }
        else{
            txtAccountNumber.setText((String)hash.get("ACT_NUM"));
            lblAcctNameDisplay.setText((String)hash.get("CUSTOMER_NAME"));
            String ACCOUNTNO=(String)hash.get("ACT_NUM");
            txtOldCustID.setText((String)hash.get("CUST_ID"));
        }
    }
    
    /** To fillData for existing entry */
    private void fillDataEdit(HashMap hash){
        final HashMap where = (HashMap) hash;
        where.put("USER_ID", ProxyParameters.USER_ID);
        where.put("WHERE", hash.get("ACT_NUM"));
        observable.getData(where);
    }
    
    /** To get popUp data for a new entry */
    private HashMap accountViewMap() {
        final HashMap testMap = new HashMap();
        testMap.put("MAPNAME", "getSelectActForCustIDChng"
                + ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
        String prdId = CommonUtil.convertObjToStr((observable.getCbmProductId()).getKeyForSelected());
        HashMap whrmap = new HashMap();
        whrmap.put("PROD_ID", prdId);
        String behavesLike = "";
        List lstSupName = ClientUtil.executeQuery("getAccHeadForLienDep", whrmap);
        HashMap renewMapData = new HashMap();
        if (lstSupName != null && lstSupName.size() > 0) {
            renewMapData = (HashMap) lstSupName.get(0);
        }
        if (renewMapData != null && renewMapData.containsKey("BEHAVES_LIKE")) {
            behavesLike = CommonUtil.convertObjToStr(renewMapData.get("BEHAVES_LIKE"));
        }
        final HashMap whereMap = new HashMap();
        if (behavesLike != null && behavesLike.length() > 0 && behavesLike.equals("DAILY")) {
            whereMap.put("BEHAVES_LIKE", behavesLike);
        }else{
             whereMap.put("NOT_DAILY", "NOT_DAILY"); 
        }
        whereMap.put("SELECTED_BRANCH", TrueTransactMain.BRANCH_ID);
        whereMap.put("PROD_ID", (String) (observable.getCbmProductId()).getKeyForSelected());
        testMap.put(CommonConstants.MAP_WHERE, whereMap);
        return testMap;
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
        btnDateChange.setEnabled(btnNew.isEnabled());
    }
    
    private void setButton4Authorize() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(btnNew.isEnabled());
        btnDelete.setEnabled(btnNew.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnAccountNumber.setEnabled(false);
        btnNewCustID.setEnabled(false);
    }
    
    public void update(Observable observed, Object arg) {
        //       cboProductId.setSelectedItem( (observable.getCbmProductId()).getDataForKey(observable.getCboProductId()));
        txtAccountNumber.setText(observable.getTxtAccountNumber());
        txtOldCustID.setText(observable.getOldCustID());
        txtNewCustId.setText(observable.getNewCustID());
        lblAcctNameDisplay.setText(observable.getTxtAcctName());
        lblAcctNameDisplay2.setText(observable.getTxtNewAcctName());
        if (observable.getCbmProductId()!=null)
            cboProductId.setModel(observable.getCbmProductId());
        lblStatus.setText(observable.getLblStatus());
    }
    
    public void updateOBFields() {
        observable.setCboProductId((String)(((ComboBoxModel)(cboProductId).getModel())).getKeyForSelected());
        observable.setTxtAccountNumber(txtAccountNumber.getText());
        observable.setOldCustID(txtOldCustID.getText());
        observable.setNewCustID(txtNewCustId.getText());
        observable.setTxtAcctName(lblAcctNameDisplay.getText());
        observable.setTxtNewAcctName(lblAcctNameDisplay2.getText());
        observable.setCboProdType((String) cboProdType.getSelectedItem());
        
    }
    
    private void savePerformed(){
        HashMap lockMap = new HashMap(); 
        ArrayList lst = new ArrayList();
        lst.add("ACT_NUM");
        lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
        lockMap.put("ACT_NUM",CommonUtil.convertObjToStr(txtAccountNumber.getText()));
        observable.doAction() ;
        observable.resetForm();
        enableDisable(false);
        setButtonEnableDisable();
        btnAccountNumber.setEnabled(false);
        lblStatus.setText(observable.getLblStatus());
        viewType = -1;
        ClientUtil.enableDisable(this, false);
        observable.setResultStatus();
        lblStatus.setText(observable.getLblStatus());
        if(observable.getResult()!=4){
            setEditLockMap(lockMap);
            setEditLock();
        }
        //__ Make the Screen Closable..
        setModified(false);
         deletescreenLock();
        observable.ttNotifyObservers();
    }
    private void setFieldNames() {
        cboProductId.setName("cboProductId");
        btnAccountNumber.setName("btnAccountNumber");
        lblAccountNumber.setName("lblAccountNumber");
        lblProductId.setName("lblProductId");
        lblOldCustID.setName("lblOldCustID");
        txtOldCustID.setName("txtOldCustID");
        lblProdType.setName("lblProdType");
        cboProdType.setName("cboProdType");
        txtAccountNumber.setName("txtAccountNumber");
        btnNewCustID.setName("btnNewCustID");
        lblNewCustId.setName("lblNewCustId");
        txtNewCustId.setName("txtNewCustId");
    }
    
    private void internationalize() {
        java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.customer.customeridchange.CustomerIdChangeRB", ProxyParameters.LANGUAGE);
        lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
        lblProductId.setText(resourceBundle.getString("lblProductId"));
        lblOldCustID.setText(resourceBundle.getString("lblOldCustID"));
        lblNewCustId.setText(resourceBundle.getString("lblNewCustId"));
        lblProdType.setText(resourceBundle.getString("lblProdType"));
    }
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    public void clearProdFields(){
        txtAccountNumber.setText("");
        lblAcctNameDisplay.setText("");
        lblAcctNameDisplay2.setText("");
        txtOldCustID.setText("");
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
    private com.see.truetransact.uicomponent.CButton btnAccountNumber;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDateChange;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNewCustID;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboProductId;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblAcctNameDisplay;
    private com.see.truetransact.uicomponent.CLabel lblAcctNameDisplay2;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNewCustId;
    private com.see.truetransact.uicomponent.CLabel lblOldCustID;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace25;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
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
    private com.see.truetransact.uicomponent.CPanel panNewCustID;
    private com.see.truetransact.uicomponent.CPanel panProductID;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransactionType;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CTextField txtAccountNumber;
    private com.see.truetransact.uicomponent.CTextField txtNewCustId;
    private com.see.truetransact.uicomponent.CTextField txtOldCustID;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
        CustomerIdChangeUI tod = new CustomerIdChangeUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(tod);
        j.show();
        tod.show();
    }
}
