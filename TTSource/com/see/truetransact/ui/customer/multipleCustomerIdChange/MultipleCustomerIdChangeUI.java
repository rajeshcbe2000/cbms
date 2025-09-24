/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MultipleCustomerIdChangeUI.java
 *
 * Created on 20 Feb 2017 
 * Author Kannan AR
 */
package com.see.truetransact.ui.customer.multipleCustomerIdChange;

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
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.customer.customeridchange.CustomerIdChangeOB;
import com.see.truetransact.commonutil.DateUtil;
import java.util.Date;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.uivalidation.DefaultValidation;

/**
 * This form is used to manipulate MultipleCustomerIdChangeUI related
 * functionality
 *
 * @author Kannan AR
 */
public class MultipleCustomerIdChangeUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {

    final int CUST_ID = 1, AUTHORIZE = 3, CANCEL = 0;
    int viewType = -1;
    private HashMap mandatoryMap;
    private MultipleCustomerIdChangeOB observable;
    private final static Logger log = Logger.getLogger(MultipleCustomerIdChangeUI.class);
    MultipleCustomerIdChangeRB custIdChangeRB = new MultipleCustomerIdChangeRB();
    private String prodType = "";
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    String cust_type = null;
    private CTable _tblData;
    private HashMap dataHash;
    private ArrayList _heading;
    private boolean _isAvailable = true;
    private ArrayList data;
    HashMap branchMap = new HashMap();
    final ArrayList TabTitle = new ArrayList();
    final ArrayList custTitle = new ArrayList();
    HashMap selectedAccoutMap = new HashMap();
    ArrayList selectedList = new ArrayList();
    private String prev_custID = "";

    /**
     * Creates new form MultipleCustomerIdChangeUI
     */
    public MultipleCustomerIdChangeUI() {
        initComponents();
        initStartUp();
    }

    private void initStartUp() {
        setMandatoryHashMap();
        setFieldNames();
        internationalize();
        observable = new MultipleCustomerIdChangeOB();
        observable.addObserver(this);
        initComponentData();
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
        /*objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.customer.customeridchange.CustomerIdChangeMRB", ProxyParameters.LANGUAGE);
         new MandatoryCheck().putMandatoryMarks(getClass().getName(), panProductID);*/
        setHelpMessage();

        branchMap = TrueTransactMain.BRANCHINFO;
        txtBranchId.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_CODE")));
        lblBranchName.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_NAME")));
        TabTitle.add("PROD_TYPE");
        TabTitle.add("PROD_ID");
        TabTitle.add("ACT_NUM");
        TabTitle.add("ACCT_STATUS");


        custTitle.add("Member No");
        custTitle.add("Cust ID");
        custTitle.add("Name");
        custTitle.add("Aadhaar No");
        custTitle.add("PAN No");
        custTitle.add("DOB");
        custTitle.add("PassPort No");
        custTitle.add("Phone No");
        custTitle.add("Status");
        custTitle.add("Address");
        btnDateChange.setVisible(false);
        //btnView.setVisible(false);
        btnSearch.setEnabled(false);
        btnClear.setEnabled(false);
        btnAccountAdd.setEnabled(false);
        btnAccountRemove.setEnabled(false);
        btnCustomerIdAI.setEnabled(false);
        txtNewCustomerId.setEnabled(false);
//        btnCustomerIdAI.setVisible(false);
        btnException.setVisible(false);
        btnPrint.setVisible(false);
        btnClear1.setVisible(false);
        cboSearchCriteria.setModel(new javax.swing.DefaultComboBoxModel(new String[]{" ", "Starts with", "Ends with", "Exact Match"}));
    }

    private void setMaxLength() {
        txtBranchId.setAllowNumber(true);
        txtCustomerName.setMaxLength(16);
        txtMemberNo.setMaxLength(16);
        txtAddress.setMaxLength(16);
        txtPhoneNumber.setMaxLength(16);
        txtPhoneNumber.setAllowNumber(true);
        txtCareOfName.setMaxLength(16);
        txtCustomerID.setMaxLength(16);
        txtEmployeeNo.setMaxLength(16);
        txtUniqueIdNo.setMaxLength(16);
        txtPanNO.setMaxLength(16);
        txtPassPortNo.setMaxLength(16);
        txtAccountNumber.setAllowAll(true);
        txtNewCustomerId.setAllowAll(true);
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

    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType != AUTHORIZE) {
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("INIT_BRANCH", TrueTransactMain.BRANCH_ID);
            whereMap.put("AUTHORIZE_MODE", "AUTHORIZE_MODE");
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getSelectMultipleAccountsForEdit");
            viewType = AUTHORIZE;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            //            isFilled = false;
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        } else if (viewType == AUTHORIZE) {
            if (CommonUtil.convertObjToStr(observable.getMultipleBatchID()).length() > 0) {
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", authorizeStatus);
                singleAuthorizeMap.put("MULTIPLE_BATCH_ID", observable.getMultipleBatchID());
                //singleAuthorizeMap.put("OLD_CUST_ID", observable.getOldCustID());
                singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("AUTH_DT", ClientUtil.getCurrentDateWithTime());
                singleAuthorizeMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                singleAuthorizeMap.put("NEW", TrueTransactMain.BRANCH_ID);
                //singleAuthorizeMap.put("OLD_CUST_ID", observable.getOldCustID());
                //singleAuthorizeMap.put("NEW_CUST_ID", observable.getNewCustID());
                //singleAuthorizeMap.put("PROD_TYPE", CommonUtil.convertObjToStr(observable.getCbmProdType().getKeyForSelected()));
                arrList.add(singleAuthorizeMap);
                //singleAuthorizeMap.put("TRANS_ID", observable.getTxtAccountNumber());
                //singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
                observable.setModule(getModule());
                observable.setScreen(getScreen());
                //Commented By Revathi
//            List lst=ClientUtil.executeQuery("selectauthorizationLock", singleAuthorizeMap);
//            if(lst !=null && lst.size()>0) {
//                HashMap map=new HashMap();
//                StringBuffer open=new StringBuffer();
//                for(int i=0;i<lst.size();i++){
//                    map=(HashMap)lst.get(i);
//                    open.append ("\n"+"User Id  :"+" ");
//                    open.append(CommonUtil.convertObjToStr(map.get("OPEN_BY"))+"\n");
//                    open.append("Mode Of Operation  :" +" ");
//                    open.append(CommonUtil.convertObjToStr(map.get("MODE_OF_OPERATION"))+" ");                
//                }
//                ClientUtil.showMessageWindow("already open by"+open);           
//                return;
//            }
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorize(authorizeMap, observable.getMultipleBatchID(), observable.getTxtStatusBy());
            } else {
                btnCancelActionPerformed(null);
            }
        }
    }

    public void authorize(HashMap map, String ac_num, String statusBy) {
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
//            observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.set_authorizeMap(map);
            observable.doAction();
            if (observable.getResult() != 4) {
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
        /* cboProductId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductId"));
         cboProdType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdType"));
         txtAccountNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccountNumber"));
         txtOldCustID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOldCustID"));
         txtNewCustId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNewCustId"));*/
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

        rdoTransactionType = new com.see.truetransact.uicomponent.CButtonGroup();
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
        panCustDetails = new com.see.truetransact.uicomponent.CPanel();
        panCustomerDetails = new com.see.truetransact.uicomponent.CPanel();
        panEmpBtn = new com.see.truetransact.uicomponent.CPanel();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnSearch = new com.see.truetransact.uicomponent.CButton();
        panCustomerDetail = new com.see.truetransact.uicomponent.CPanel();
        lblCustomerName = new com.see.truetransact.uicomponent.CLabel();
        txtCustomerName = new com.see.truetransact.uicomponent.CTextField();
        lblMemberNo = new com.see.truetransact.uicomponent.CLabel();
        txtMemberNo = new com.see.truetransact.uicomponent.CTextField();
        lblAddress = new com.see.truetransact.uicomponent.CLabel();
        txtAddress = new com.see.truetransact.uicomponent.CTextField();
        lblPhoneNumber = new com.see.truetransact.uicomponent.CLabel();
        txtPhoneNumber = new com.see.truetransact.uicomponent.CTextField();
        lblCareOfName = new com.see.truetransact.uicomponent.CLabel();
        txtCareOfName = new com.see.truetransact.uicomponent.CTextField();
        cboSearchCriteria = new com.see.truetransact.uicomponent.CComboBox();
        panCustomerDetail1 = new com.see.truetransact.uicomponent.CPanel();
        lblUniqueIdNo = new com.see.truetransact.uicomponent.CLabel();
        txtUniqueIdNo = new com.see.truetransact.uicomponent.CTextField();
        lblPanNO = new com.see.truetransact.uicomponent.CLabel();
        txtPanNO = new com.see.truetransact.uicomponent.CTextField();
        lblPassPortNo = new com.see.truetransact.uicomponent.CLabel();
        txtPassPortNo = new com.see.truetransact.uicomponent.CTextField();
        lblDtOfBirth = new com.see.truetransact.uicomponent.CLabel();
        tdtDtOfBirth = new com.see.truetransact.uicomponent.CDateField();
        lblEmployeeNo = new com.see.truetransact.uicomponent.CLabel();
        txtEmployeeNo = new com.see.truetransact.uicomponent.CTextField();
        lblCustomerID = new com.see.truetransact.uicomponent.CLabel();
        txtCustomerID = new com.see.truetransact.uicomponent.CTextField();
        txtBranchId = new com.see.truetransact.uicomponent.CTextField();
        lblBranchId = new com.see.truetransact.uicomponent.CLabel();
        lblBranchName = new com.see.truetransact.uicomponent.CLabel();
        lblAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        txtAccountNumber = new com.see.truetransact.uicomponent.CTextField();
        srpRepaymentCTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblCustomerList = new com.see.truetransact.uicomponent.CTable();
        panAccountdetails = new com.see.truetransact.uicomponent.CPanel();
        tabAccountDetails = new com.see.truetransact.uicomponent.CTabbedPane();
        panAsset = new com.see.truetransact.uicomponent.CPanel();
        srpAssetCTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblAvailAccount = new com.see.truetransact.uicomponent.CTable();
        panEmpBtn1 = new com.see.truetransact.uicomponent.CPanel();
        btnAccountAdd = new com.see.truetransact.uicomponent.CButton();
        btnAccountRemove = new com.see.truetransact.uicomponent.CButton();
        tabAccountDetails1 = new com.see.truetransact.uicomponent.CTabbedPane();
        panAsset1 = new com.see.truetransact.uicomponent.CPanel();
        srpAssetCTable1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblSelectedAccount = new com.see.truetransact.uicomponent.CTable();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        lblCustomerIdAI = new com.see.truetransact.uicomponent.CLabel();
        txtNewCustomerId = new com.see.truetransact.uicomponent.CTextField();
        btnCustomerIdAI = new com.see.truetransact.uicomponent.CButton();
        btnClear1 = new com.see.truetransact.uicomponent.CButton();
        lblNewCustName = new com.see.truetransact.uicomponent.CLabel();
        lblSelectedAcCountsVal = new com.see.truetransact.uicomponent.CLabel();
        lblSelectedAcCounts = new com.see.truetransact.uicomponent.CLabel();
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
        setMaximumSize(new java.awt.Dimension(900, 660));
        setMinimumSize(new java.awt.Dimension(900, 660));
        setPreferredSize(new java.awt.Dimension(900, 660));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
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

        panCustDetails.setMaximumSize(new java.awt.Dimension(875, 640));
        panCustDetails.setMinimumSize(new java.awt.Dimension(875, 640));
        panCustDetails.setPreferredSize(new java.awt.Dimension(875, 640));
        panCustDetails.setLayout(new java.awt.GridBagLayout());

        panCustomerDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Details"));
        panCustomerDetails.setMaximumSize(new java.awt.Dimension(850, 165));
        panCustomerDetails.setMinimumSize(new java.awt.Dimension(850, 165));
        panCustomerDetails.setPreferredSize(new java.awt.Dimension(850, 165));
        panCustomerDetails.setLayout(new java.awt.GridBagLayout());

        panEmpBtn.setMinimumSize(new java.awt.Dimension(170, 35));
        panEmpBtn.setPreferredSize(new java.awt.Dimension(170, 35));
        panEmpBtn.setLayout(new java.awt.GridBagLayout());

        btnClear.setText("Clear");
        btnClear.setMaximumSize(new java.awt.Dimension(75, 25));
        btnClear.setMinimumSize(new java.awt.Dimension(75, 25));
        btnClear.setPreferredSize(new java.awt.Dimension(75, 25));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmpBtn.add(btnClear, gridBagConstraints);

        btnSearch.setText("Search");
        btnSearch.setMaximumSize(new java.awt.Dimension(75, 25));
        btnSearch.setMinimumSize(new java.awt.Dimension(75, 25));
        btnSearch.setPreferredSize(new java.awt.Dimension(75, 25));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmpBtn.add(btnSearch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(panEmpBtn, gridBagConstraints);

        panCustomerDetail.setMaximumSize(new java.awt.Dimension(370, 140));
        panCustomerDetail.setMinimumSize(new java.awt.Dimension(370, 140));
        panCustomerDetail.setPreferredSize(new java.awt.Dimension(370, 140));
        panCustomerDetail.setLayout(new java.awt.GridBagLayout());

        lblCustomerName.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail.add(lblCustomerName, gridBagConstraints);

        txtCustomerName.setMaxLength(128);
        txtCustomerName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustomerName.setName("txtCompany"); // NOI18N
        txtCustomerName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail.add(txtCustomerName, gridBagConstraints);

        lblMemberNo.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail.add(lblMemberNo, gridBagConstraints);

        txtMemberNo.setMaxLength(128);
        txtMemberNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMemberNo.setName("txtCompany"); // NOI18N
        txtMemberNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail.add(txtMemberNo, gridBagConstraints);

        lblAddress.setText("House Name/Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail.add(lblAddress, gridBagConstraints);

        txtAddress.setMaxLength(128);
        txtAddress.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAddress.setName("txtCompany"); // NOI18N
        txtAddress.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail.add(txtAddress, gridBagConstraints);

        lblPhoneNumber.setText("Phone Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail.add(lblPhoneNumber, gridBagConstraints);

        txtPhoneNumber.setMaxLength(128);
        txtPhoneNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPhoneNumber.setName("txtCompany"); // NOI18N
        txtPhoneNumber.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail.add(txtPhoneNumber, gridBagConstraints);

        lblCareOfName.setText("Care Of Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail.add(lblCareOfName, gridBagConstraints);

        txtCareOfName.setMaxLength(128);
        txtCareOfName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCareOfName.setName("txtCompany"); // NOI18N
        txtCareOfName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail.add(txtCareOfName, gridBagConstraints);

        cboSearchCriteria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ends with", "Starts with", "Exact Match", "Pattern Match" }));
        cboSearchCriteria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSearchCriteriaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerDetail.add(cboSearchCriteria, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        panCustomerDetails.add(panCustomerDetail, gridBagConstraints);

        panCustomerDetail1.setMaximumSize(new java.awt.Dimension(200, 140));
        panCustomerDetail1.setMinimumSize(new java.awt.Dimension(200, 140));
        panCustomerDetail1.setPreferredSize(new java.awt.Dimension(200, 140));
        panCustomerDetail1.setLayout(new java.awt.GridBagLayout());

        lblUniqueIdNo.setText("Aadhaar No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail1.add(lblUniqueIdNo, gridBagConstraints);

        txtUniqueIdNo.setMaxLength(128);
        txtUniqueIdNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtUniqueIdNo.setName("txtCompany"); // NOI18N
        txtUniqueIdNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail1.add(txtUniqueIdNo, gridBagConstraints);

        lblPanNO.setText("PAN No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail1.add(lblPanNO, gridBagConstraints);

        txtPanNO.setMaxLength(128);
        txtPanNO.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPanNO.setName("txtCompany"); // NOI18N
        txtPanNO.setValidation(new DefaultValidation());
        txtPanNO.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPanNOFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail1.add(txtPanNO, gridBagConstraints);

        lblPassPortNo.setText("Pass Port No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail1.add(lblPassPortNo, gridBagConstraints);

        txtPassPortNo.setMaxLength(128);
        txtPassPortNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPassPortNo.setName("txtCompany"); // NOI18N
        txtPassPortNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail1.add(txtPassPortNo, gridBagConstraints);

        lblDtOfBirth.setText("Date Of Birth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail1.add(lblDtOfBirth, gridBagConstraints);

        tdtDtOfBirth.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtDtOfBirth.setName("tdtToDate"); // NOI18N
        tdtDtOfBirth.setPreferredSize(new java.awt.Dimension(101, 19));
        tdtDtOfBirth.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDtOfBirthFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail1.add(tdtDtOfBirth, gridBagConstraints);

        lblEmployeeNo.setText("Employee No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail1.add(lblEmployeeNo, gridBagConstraints);

        txtEmployeeNo.setMaxLength(128);
        txtEmployeeNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEmployeeNo.setName("txtCompany"); // NOI18N
        txtEmployeeNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail1.add(txtEmployeeNo, gridBagConstraints);

        lblCustomerID.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail1.add(lblCustomerID, gridBagConstraints);

        txtCustomerID.setMaxLength(128);
        txtCustomerID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustomerID.setName("txtCompany"); // NOI18N
        txtCustomerID.setValidation(new DefaultValidation());
        txtCustomerID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail1.add(txtCustomerID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        panCustomerDetails.add(panCustomerDetail1, gridBagConstraints);

        txtBranchId.setMaxLength(128);
        txtBranchId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBranchId.setName("txtCompany"); // NOI18N
        txtBranchId.setValidation(new DefaultValidation());
        txtBranchId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBranchIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 4, 4);
        panCustomerDetails.add(txtBranchId, gridBagConstraints);

        lblBranchId.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 4, 4);
        panCustomerDetails.add(lblBranchId, gridBagConstraints);

        lblBranchName.setForeground(new java.awt.Color(0, 0, 255));
        lblBranchName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBranchName.setText("Branch Name");
        lblBranchName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblBranchName.setMaximumSize(new java.awt.Dimension(200, 18));
        lblBranchName.setMinimumSize(new java.awt.Dimension(200, 18));
        lblBranchName.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(lblBranchName, gridBagConstraints);

        lblAccountNumber.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(lblAccountNumber, gridBagConstraints);

        txtAccountNumber.setMaxLength(128);
        txtAccountNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountNumber.setName("txtCompany"); // NOI18N
        txtAccountNumber.setValidation(new DefaultValidation());
        txtAccountNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(txtAccountNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panCustDetails.add(panCustomerDetails, gridBagConstraints);

        srpRepaymentCTable.setMaximumSize(new java.awt.Dimension(850, 130));
        srpRepaymentCTable.setMinimumSize(new java.awt.Dimension(850, 130));
        srpRepaymentCTable.setPreferredSize(new java.awt.Dimension(850, 130));

        tblCustomerList.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblCustomerList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Member No", "Cust ID", "Name", "Aadhaar No", "PAN No", "DOB", "PassPort No", "Phone No", "Status", "Address"
            }
        ));
        tblCustomerList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblCustomerList.setDragEnabled(true);
        tblCustomerList.setMaximumSize(new java.awt.Dimension(2147483647, 64));
        tblCustomerList.setMinimumSize(new java.awt.Dimension(750, 0));
        tblCustomerList.setPreferredScrollableViewportSize(new java.awt.Dimension(794, 246));
        tblCustomerList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCustomerListMousePressed(evt);
            }
        });
        srpRepaymentCTable.setViewportView(tblCustomerList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 4);
        panCustDetails.add(srpRepaymentCTable, gridBagConstraints);

        panAccountdetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAccountdetails.setMaximumSize(new java.awt.Dimension(850, 250));
        panAccountdetails.setMinimumSize(new java.awt.Dimension(850, 250));
        panAccountdetails.setPreferredSize(new java.awt.Dimension(850, 250));
        panAccountdetails.setLayout(new java.awt.GridBagLayout());

        tabAccountDetails.setMaximumSize(new java.awt.Dimension(425, 280));
        tabAccountDetails.setMinimumSize(new java.awt.Dimension(425, 280));
        tabAccountDetails.setPreferredSize(new java.awt.Dimension(425, 280));

        panAsset.setMaximumSize(new java.awt.Dimension(100, 128));
        panAsset.setMinimumSize(new java.awt.Dimension(100, 128));
        panAsset.setPreferredSize(new java.awt.Dimension(100, 128));
        panAsset.setLayout(new java.awt.GridBagLayout());

        srpAssetCTable.setMaximumSize(new java.awt.Dimension(350, 170));
        srpAssetCTable.setMinimumSize(new java.awt.Dimension(350, 170));
        srpAssetCTable.setPreferredSize(new java.awt.Dimension(350, 170));

        tblAvailAccount.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblAvailAccount.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PROD_TYPE", "PROD_ID", "ACT_NUM", "ACCT_STATUS"
            }
        ));
        tblAvailAccount.setDragEnabled(true);
        tblAvailAccount.setMaximumSize(new java.awt.Dimension(2147483647, 64));
        tblAvailAccount.setMinimumSize(new java.awt.Dimension(350, 900));
        tblAvailAccount.setOpaque(false);
        tblAvailAccount.setPreferredScrollableViewportSize(new java.awt.Dimension(794, 246));
        tblAvailAccount.setPreferredSize(new java.awt.Dimension(350, 900));
        tblAvailAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAvailAccountMouseClicked(evt);
            }
        });
        srpAssetCTable.setViewportView(tblAvailAccount);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAsset.add(srpAssetCTable, gridBagConstraints);

        tabAccountDetails.addTab("Available Accounts", panAsset);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountdetails.add(tabAccountDetails, gridBagConstraints);

        panEmpBtn1.setMinimumSize(new java.awt.Dimension(100, 70));
        panEmpBtn1.setPreferredSize(new java.awt.Dimension(110, 70));
        panEmpBtn1.setLayout(new java.awt.GridBagLayout());

        btnAccountAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_RIGHTARR.jpg"))); // NOI18N
        btnAccountAdd.setText("Add");
        btnAccountAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        panEmpBtn1.add(btnAccountAdd, gridBagConstraints);

        btnAccountRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_LEFTARR.jpg"))); // NOI18N
        btnAccountRemove.setText("Remove");
        btnAccountRemove.setMaximumSize(new java.awt.Dimension(100, 27));
        btnAccountRemove.setMinimumSize(new java.awt.Dimension(100, 27));
        btnAccountRemove.setPreferredSize(new java.awt.Dimension(100, 27));
        btnAccountRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountRemoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        panEmpBtn1.add(btnAccountRemove, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountdetails.add(panEmpBtn1, gridBagConstraints);

        tabAccountDetails1.setMaximumSize(new java.awt.Dimension(380, 280));
        tabAccountDetails1.setMinimumSize(new java.awt.Dimension(430, 435));
        tabAccountDetails1.setPreferredSize(new java.awt.Dimension(380, 280));

        panAsset1.setMaximumSize(new java.awt.Dimension(100, 128));
        panAsset1.setMinimumSize(new java.awt.Dimension(100, 128));
        panAsset1.setPreferredSize(new java.awt.Dimension(100, 128));
        panAsset1.setLayout(new java.awt.GridBagLayout());

        srpAssetCTable1.setMaximumSize(new java.awt.Dimension(350, 170));
        srpAssetCTable1.setMinimumSize(new java.awt.Dimension(350, 170));
        srpAssetCTable1.setPreferredSize(new java.awt.Dimension(350, 170));

        tblSelectedAccount.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblSelectedAccount.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PROD_TYPE", "PROD_ID", "ACT_NUM", "ACCT_STATUS"
            }
        ));
        tblSelectedAccount.setDragEnabled(true);
        tblSelectedAccount.setMaximumSize(new java.awt.Dimension(2147483647, 64));
        tblSelectedAccount.setMinimumSize(new java.awt.Dimension(350, 900));
        tblSelectedAccount.setOpaque(false);
        tblSelectedAccount.setPreferredScrollableViewportSize(new java.awt.Dimension(794, 246));
        tblSelectedAccount.setPreferredSize(new java.awt.Dimension(350, 900));
        tblSelectedAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSelectedAccountMouseClicked(evt);
            }
        });
        srpAssetCTable1.setViewportView(tblSelectedAccount);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAsset1.add(srpAssetCTable1, gridBagConstraints);

        tabAccountDetails1.addTab("Selected Accounts", panAsset1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountdetails.add(tabAccountDetails1, gridBagConstraints);
        tabAccountDetails1.getAccessibleContext().setAccessibleName("Selected Accounts");

        panSearch.setMinimumSize(new java.awt.Dimension(850, 30));
        panSearch.setPreferredSize(new java.awt.Dimension(750, 30));
        panSearch.setLayout(new java.awt.GridBagLayout());

        lblCustomerIdAI.setText("New Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSearch.add(lblCustomerIdAI, gridBagConstraints);

        txtNewCustomerId.setEditable(false);
        txtNewCustomerId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNewCustomerId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNewCustomerIdActionPerformed(evt);
            }
        });
        txtNewCustomerId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNewCustomerIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panSearch.add(txtNewCustomerId, gridBagConstraints);

        btnCustomerIdAI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustomerIdAI.setToolTipText("Customer Data");
        btnCustomerIdAI.setMaximumSize(new java.awt.Dimension(21, 21));
        btnCustomerIdAI.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCustomerIdAI.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustomerIdAI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerIdAIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSearch.add(btnCustomerIdAI, gridBagConstraints);

        btnClear1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear1.setText("Clear All");
        btnClear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClear1, gridBagConstraints);

        lblNewCustName.setForeground(new java.awt.Color(0, 0, 255));
        lblNewCustName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNewCustName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblNewCustName.setMaximumSize(new java.awt.Dimension(200, 18));
        lblNewCustName.setMinimumSize(new java.awt.Dimension(275, 18));
        lblNewCustName.setPreferredSize(new java.awt.Dimension(275, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(lblNewCustName, gridBagConstraints);

        lblSelectedAcCountsVal.setForeground(new java.awt.Color(255, 0, 51));
        lblSelectedAcCountsVal.setText("Count");
        lblSelectedAcCountsVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSearch.add(lblSelectedAcCountsVal, gridBagConstraints);

        lblSelectedAcCounts.setText("Total Selected A/c : ");
        lblSelectedAcCounts.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSearch.add(lblSelectedAcCounts, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountdetails.add(panSearch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panCustDetails.add(panAccountdetails, gridBagConstraints);

        getContentPane().add(panCustDetails, java.awt.BorderLayout.CENTER);

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
        btnView.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        popUp();
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        lblStatus.setText(observable.getLblStatus());
        btnDateChange.setEnabled(false);
        btnSave.setEnabled(false);
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        //updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
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
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnNew.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnNew.setEnabled(false);
        setModified(true);
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
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
        btnCancel.setEnabled(true);
        btnDelete.setEnabled(false);
        btnNew.setEnabled(false);
        lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp();
        lblStatus.setText(observable.getLblStatus());
        txtAccountNumber.setEnabled(false);
        btnEdit.setEnabled(false);
        btnCancel.setEnabled(true);
        btnDelete.setEnabled(false);
        btnNew.setEnabled(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        viewType = CANCEL;
        observable.resetForm();
        setButtonEnableDisable();
        btnView.setEnabled(true);
//        btnAccountNumber.setEnabled(false);
//        btnNewCustID.setEnabled(false);
        ClientUtil.enableDisable(this, false);
        lblStatus.setText("");
        prev_custID = "";
        selectedList = new ArrayList();
        //txtNewCustId.setText("");
        cust_type = null;
        setModified(false);
        btnSearch.setEnabled(false);
        btnClear.setEnabled(false);
        btnClear1.setEnabled(false);
        btnAccountAdd.setEnabled(false);
        btnAccountRemove.setEnabled(false);
        btnCustomerIdAI.setEnabled(false);
        btnSave.setEnabled(false);
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        btnClearActionPerformed(null);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (selectedList.size() <= 0) {
            ClientUtil.showMessageWindow("No records toSave!!!");
            return;
        } else if (txtNewCustomerId.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Enter Master customer ID!!!");
            return;
        } else {
            updateOBFields();
            savePerformed();
            if (observable.getActionType() != ClientConstants.ACTIONTYPE_FAILED) {
                btnCancelActionPerformed(null);
                btnSave.setEnabled(false);
            }
        }
        //String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panProductID);
        /*String mandatoryMessage = "";
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
         //newCustMap.put("CUST_ID",CommonUtil.convertObjToStr(txtNewCustId.getText()));
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
         }*/
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        enableDisable(true);
        setButtonEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        btnAccountAdd.setEnabled(true);
        btnAccountRemove.setEnabled(true);
        txtNewCustomerId.setEnabled(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        branchMap = TrueTransactMain.BRANCHINFO;
        txtBranchId.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_CODE")));
        lblBranchName.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_NAME")));
        lblNewCustName.setText("");
        setModified(true);
        tblCustomerList.setEnabled(true);
        tblAvailAccount.setEnabled(true);
        tblSelectedAccount.setEnabled(true);
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

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        btnClear1ActionPerformed(null);
        branchMap = TrueTransactMain.BRANCHINFO;
        txtBranchId.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_CODE")));
        lblBranchName.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_NAME")));
        lblSelectedAcCountsVal.setText("");
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        prev_custID = "";
        clearAllTableData();
        HashMap where = new HashMap();
        HashMap viewMap = new HashMap();
        String custID = CommonUtil.convertObjToStr(txtCustomerID.getText());
        String custName = CommonUtil.convertObjToStr(txtCustomerName.getText());
        String careOfName = CommonUtil.convertObjToStr(txtCareOfName.getText());
        String memberNo = CommonUtil.convertObjToStr(txtMemberNo.getText());
        String empNo = CommonUtil.convertObjToStr(txtEmployeeNo.getText());
        String address = CommonUtil.convertObjToStr(txtAddress.getText());
        String uniqueId = CommonUtil.convertObjToStr(txtUniqueIdNo.getText());
        String panNo = CommonUtil.convertObjToStr(txtPanNO.getText());
        String passPortNo = CommonUtil.convertObjToStr(txtPassPortNo.getText());
        String phoneNO = CommonUtil.convertObjToStr(txtPhoneNumber.getText());
        Date dateOfBirth = DateUtil.getDateMMDDYYYY(tdtDtOfBirth.getDateValue());
        String srchCriteria = CommonUtil.convertObjToStr(cboSearchCriteria.getSelectedItem());
        String branchID = CommonUtil.convertObjToStr(txtBranchId.getText());

        viewMap.put(CommonConstants.MAP_NAME, "getSelectCustDetails");
        if (custID.length() > 0) {
            where.put("CUST_ID", "%" + custID.toUpperCase() + "%");
        }
        if (custName.length() > 0) {
            where.put("FNAME", custName);
        }
        if (careOfName.length() > 0) {
            where.put("CARE_0F_NAME", careOfName);
        }
        if (memberNo.length() > 0) {
            where.put("MEMBER_NO", memberNo);
        }
        if (empNo.length() > 0) {
            where.put("EMP_NO", empNo);
        }
        if (address.length() > 0) {
            where.put("ADDRESS", address);
        }
        if (uniqueId.length() > 0) {
            where.put("UNIQUE_ID", uniqueId);
        }
        if (panNo.length() > 0) {
            where.put("PAN_NUMBER", panNo);
        }
        if (passPortNo.length() > 0) {
            where.put("PASSPORT_NUMBER", passPortNo);
        }
        if (tdtDtOfBirth.getDateValue().length() > 0) {
            where.put("DOB", DateUtil.getDateMMDDYYYY(tdtDtOfBirth.getDateValue()));
        }
        if (phoneNO.length() > 0) {
            where.put("PHONE_NUMBER", phoneNO);
        }

        if (srchCriteria.length() > 0 && srchCriteria.equalsIgnoreCase("Starts with")) {
            where.put("STARTSWITH", srchCriteria);
        }
        if (srchCriteria.length() > 0 && srchCriteria.equalsIgnoreCase("Ends with")) {
            where.put("ENDSWITH", srchCriteria);
        }
        if (srchCriteria.length() > 0 && srchCriteria.equalsIgnoreCase("Exact Match")) {
            where.put("EXACTMATCH", srchCriteria);
        }
        if (custName.length() > 0 && srchCriteria.length() == 0) {
            where.put("ALL", "");
        }

//        if (txtAccountNumber.getText().length() > 0) {
//            HashMap hmap = new HashMap();
//            hmap.put("ACT_NUM", txtAccountNumber.getText());
//            List lst = ClientUtil.executeQuery("getSelectCustID", hmap);
//            if (lst != null && lst.size() > 0) {
//                hmap = (HashMap) lst.get(0);
//                custID = CommonUtil.convertObjToStr(hmap.get("CUST_ID"));
//                where.put("CUST_ID", "%" + custID.toUpperCase() + "%");
//            } else {
//                ClientUtil.showMessageWindow("Invalid Account Number, Please Enter valid SB Account Number!!!");
//                txtAccountNumber.setText("");
//                return;
//            }
//        }

        if (where.isEmpty()) {
            ClientUtil.showMessageWindow("Please Enter Atleast One Field!!!" /*Value Along With Branch Code !!!"*/);//Branch code not mandatory by Kannan AR ref. Mr.Rajesh
            return;
        } else {
            try {
                if (branchID.length() > 0) {
                    where.put("BRANCH_ID", branchID);    //Added By Kannan
                }
                viewMap.put(CommonConstants.MAP_WHERE, where);
                populateData(viewMap, tblCustomerList);
                if (tblCustomerList.getRowCount() > 0) {
                    btnSearch.setEnabled(false);
                    ClientUtil.enableDisable(panCustomerDetails, false);
                }
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
            viewMap = null;
            where = null;
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void clearAllTableData() {
        TableModel custModel = new TableModel(new ArrayList(), custTitle);
        tblCustomerList.setModel(custModel);
        TableModel model = new TableModel(new ArrayList(), TabTitle);
        tblAvailAccount.setModel(model);
    }

    public ArrayList populateData(HashMap whereMap, CTable tblData) {
        _tblData = tblData;
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        }
        dataHash = ClientUtil.executeTableQuery(whereMap);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        populateTable();
        whereMap = null;
        return _heading;
    }

    public void populateTable() {
        boolean dataExist;
        if (_heading != null) {
            _isAvailable = true;
            dataExist = true;
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(_heading);
            tableModel.setData(data);
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            _tblData.setAutoResizeMode(0);
            _tblData.doLayout();
            _tblData.setModel(tableSorter);
            _tblData.revalidate();

            _tblData.getColumnModel().getColumn(0).setPreferredWidth(70);
            _tblData.getColumnModel().getColumn(1).setPreferredWidth(130);
            _tblData.getColumnModel().getColumn(2).setPreferredWidth(350);
            _tblData.getColumnModel().getColumn(3).setPreferredWidth(95);
            _tblData.getColumnModel().getColumn(4).setPreferredWidth(120);
            _tblData.getColumnModel().getColumn(5).setPreferredWidth(115);
            _tblData.getColumnModel().getColumn(6).setPreferredWidth(90);
            _tblData.getColumnModel().getColumn(7).setPreferredWidth(80);
            _tblData.getColumnModel().getColumn(8).setPreferredWidth(95);
            _tblData.getColumnModel().getColumn(11).setPreferredWidth(85);
        } else {
            ClientUtil.noDataAlert();
            ClientUtil.clearAll(panCustomerDetails);
            txtBranchId.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_CODE")));
            lblBranchName.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_NAME")));
        }
    }

    private void cboSearchCriteriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSearchCriteriaActionPerformed
        // TODO add your handling code here:
        if (txtCustomerName.getText().length() <= 0 && cboSearchCriteria.getSelectedIndex() > 0) {
            ClientUtil.showMessageWindow("Please Enter Customer Name !!!");
            cboSearchCriteria.setSelectedIndex(0);
            return;
        }
    }//GEN-LAST:event_cboSearchCriteriaActionPerformed

    private void txtPanNOFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPanNOFocusLost
        // TODO add your handling code here:
        if (txtPanNO.getText().length() > 0 && !ClientUtil.validatePAN(txtPanNO)) {
            ClientUtil.showMessageWindow("Invalid Pan Number, Enter Proper Pan No (Format :ABCDE1234F)");
            txtPanNO.setText("");
        }
    }//GEN-LAST:event_txtPanNOFocusLost

    private void tdtDtOfBirthFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDtOfBirthFocusLost
        // TODO add your handling code here:
        ClientUtil.validateLTDate(tdtDtOfBirth);
    }//GEN-LAST:event_tdtDtOfBirthFocusLost

    private void txtCustomerIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerIDFocusLost
        // TODO add your handling code here:       
        if (txtCustomerID.getText().length() > 0) {
            txtBranchId.setText("");
            lblBranchName.setText("Branch Name");
        } else {
            branchMap = TrueTransactMain.BRANCHINFO;
            txtBranchId.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_CODE")));
            lblBranchName.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_NAME")));
        }
    }//GEN-LAST:event_txtCustomerIDFocusLost

    private void txtBranchIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBranchIdFocusLost
        // TODO add your handling code here:
        if (txtBranchId.getText().length() > 0) {
            HashMap dataMap = new HashMap();
            dataMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(txtBranchId.getText()));
            List lst = ClientUtil.executeQuery("getDisplayRegionalOffice", dataMap);

            if (lst != null && lst.size() > 0) {
                dataMap = (HashMap) lst.get(0);
                lblBranchName.setText(CommonUtil.convertObjToStr(dataMap.get("BRANCH NAME")));
            } else {
                ClientUtil.displayAlert("Invalid Branch Code");
                txtBranchId.setText("");
                lblBranchName.setText("Branch Name");
                return;
            }
        } else {
            lblBranchName.setText("Branch Name");
            return;
        }
    }//GEN-LAST:event_txtBranchIdFocusLost

    private void txtAccountNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNumberFocusLost
        // TODO add your handling code here:
        //Added By Kannan
        if (txtBranchId.getText().length() > 0) {
            if (txtAccountNumber.getText().length() > 0) {
                HashMap custMap = new HashMap();
                custMap.put("ACT_NUM", txtAccountNumber.getText());
                custMap.put("BRANCH_CODE", txtBranchId.getText());
                List lst = ClientUtil.executeQuery("getCustIdfromMembershipLiability", custMap);
                if (lst == null || lst.size() <= 0) {
                    ClientUtil.showMessageWindow("Invalid Account Number !!!");
                    txtAccountNumber.setText("");
                    return;
                } else {
                    custMap = (HashMap) lst.get(0);
                    txtCustomerID.setText(CommonUtil.convertObjToStr(custMap.get("CUST_ID")));
                    btnSearchActionPerformed(null);
                }
            }
        }
    }//GEN-LAST:event_txtAccountNumberFocusLost

    private void tblCustomerListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCustomerListMousePressed
        // TODO add your handling code here: 
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (tblCustomerList.getRowCount() > 0 && evt.getClickCount() == 1) {
                HashMap where = new HashMap();
                HashMap viewMap = new HashMap();
                String custID = CommonUtil.convertObjToStr(tblCustomerList.getValueAt(tblCustomerList.getSelectedRow(), 1));
                if (custID.length() > 0) {
                    if (prev_custID.length() > 0 && prev_custID.equals(custID)) {
                        //To avoid getAllAcctsBasedCustID query multiple time running
                    } else {
                        viewMap.put(CommonConstants.MAP_NAME, "getAllAcctsBasedCustID");
                        where.put("CUST_ID", custID.toUpperCase());
                        //where.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                        viewMap.put(CommonConstants.MAP_WHERE, where);
                        try {
                            populateAccountsData(viewMap, tblAvailAccount);
                            ArrayList tempList = new ArrayList();
                            tempList = (ArrayList) data;
                            prev_custID = custID;
                        } catch (Exception e) {
                            System.err.println("Exception " + e.toString() + "Caught");
                            e.printStackTrace();
                        }
                    }
                }
                viewMap = null;
                where = null;

            }
        }
    }//GEN-LAST:event_tblCustomerListMousePressed

    public ArrayList populateAccountsData(HashMap whereMap, CTable tblData) {
        _tblData = tblData;
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        }
        dataHash = ClientUtil.executeTableQuery(whereMap);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        populateAccountsTable();
        whereMap = null;
        return _heading;
    }

    public void populateAccountsTable() {
        boolean dataExist;
        if (_heading != null) {
            _isAvailable = true;
            dataExist = true;
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(_heading);
            tableModel.setData(data);
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            _tblData.setAutoResizeMode(0);
            _tblData.doLayout();
            _tblData.setModel(tableSorter);
            _tblData.revalidate();
            _tblData.getColumnModel().getColumn(0).setPreferredWidth(95);
        } else {
            TableModel model = new TableModel(new ArrayList(), TabTitle);
            tblAvailAccount.setModel(model);
        }
    }

    private void tblAvailAccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAvailAccountMouseClicked
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (tblAvailAccount.getRowCount() > 0) {
                btnAccountAdd.setEnabled(true);
                btnAccountRemove.setEnabled(true);
            }
        }
    }//GEN-LAST:event_tblAvailAccountMouseClicked

    private void btnAccountAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountAddActionPerformed
        // TODO add your handling code here:
        if (tblAvailAccount.getRowCount() > 0) {
            int isSelected = tblAvailAccount.getSelectedRow();
            if (isSelected == -1) {
                ClientUtil.showMessageWindow("First Select Accounts to add!!!");
                return;
            }
            if (selectedList != null && selectedList.size() > 0) {
                boolean isExists = isAccountExists(isSelected);
                if (isExists) {
                    ClientUtil.showMessageWindow("Already This account selected in this Batch!!!");
                    data.remove(isSelected);
                    TableModel availModel = new TableModel(data, TabTitle);
                    tblAvailAccount.setModel(availModel);
                    return;
                }
            }
            addSelectedAccount(isSelected);
            btnCustomerIdAI.setEnabled(true);
            txtNewCustomerId.setEnabled(true);
            lblSelectedAcCountsVal.setText(CommonUtil.convertObjToStr(tblSelectedAccount.getRowCount()));
        } else {
            ClientUtil.showMessageWindow("No Accounts to add!!!");
            return;
        }
    }//GEN-LAST:event_btnAccountAddActionPerformed

    public void addSelectedAccount(int selected) {
        ArrayList tempList = new ArrayList();
        tempList = (ArrayList) data.get(selected);
        if (tblCustomerList.getSelectedRowCount() > 0) {
            tempList.add(tblCustomerList.getValueAt(tblCustomerList.getSelectedRow(), 1));
            tempList.add(tblCustomerList.getValueAt(tblCustomerList.getSelectedRow(), 2));
        }
        boolean isPendingAuth = checkPendingAuthorize(tempList);
        if (isPendingAuth) {
            ClientUtil.showMessageWindow("Already This account selected in Another Batch and Pending For Authorization!!!");
            return;
        }
        selectedList.add(tempList);
        data.remove(selected);
        TableModel selectModel = new TableModel(selectedList, TabTitle);
        tblSelectedAccount.setModel(selectModel);
        TableModel availModel = new TableModel(data, TabTitle);
        tblAvailAccount.setModel(availModel);
    }

    public void removeSelectedAccount(int selected) {
        ArrayList tempList = new ArrayList();
        tempList = (ArrayList) selectedList.get(selected);
        data.add(tempList);
        selectedList.remove(selected);
        TableModel selectModel = new TableModel(selectedList, TabTitle);
        tblSelectedAccount.setModel(selectModel);
        TableModel availModel = new TableModel(data, TabTitle);
        tblAvailAccount.setModel(availModel);
    }

    private boolean isAccountExists(int selectedRow) {
        boolean isExists = false;;
        String selectedAccount = "";
        String existAccount = "";
        ArrayList singleList = new ArrayList();
        ArrayList tempList = new ArrayList();
        if (selectedList != null && selectedList.size() > 0) {
            singleList = (ArrayList) data.get(selectedRow);
            selectedAccount = CommonUtil.convertObjToStr(singleList.get(2));
            for (int i = 0; i < selectedList.size(); i++) {
                tempList = (ArrayList) selectedList.get(i);
                existAccount = CommonUtil.convertObjToStr(tempList.get(2));
                if (existAccount.equals(selectedAccount)) {
                    isExists = true;
                    break;
                }
            }
        }
        return isExists;
    }

    private boolean checkPendingAuthorize(ArrayList singleList) {
        boolean isAcctExists = false;;
        HashMap existMap = new HashMap();
        existMap.put("PROD_TYPE", CommonUtil.convertObjToStr(singleList.get(0)));
        existMap.put("PROD_ID", CommonUtil.convertObjToStr(singleList.get(1)));
        existMap.put("ACT_NUM", CommonUtil.convertObjToStr(singleList.get(2)));
        if (observable.getMultipleBatchID().length() > 0 && observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            existMap.put("MULTIPLE_BATCH_ID", observable.getMultipleBatchID());
        }
        List existLst = ClientUtil.executeQuery("checkPendingAuthorizeAccts", existMap);
        if (existLst != null && existLst.size() > 0) {
            isAcctExists = true;
        }
        return isAcctExists;
    }

    private void btnAccountRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountRemoveActionPerformed
        // TODO add your handling code here:
        if (tblSelectedAccount.getRowCount() > 0) {
            int selected = tblSelectedAccount.getSelectedRow();
            if (selected == -1) {
                ClientUtil.showMessageWindow("First Select Accounts to remove!!!");
                return;
            }
            removeSelectedAccount(selected);
            lblSelectedAcCountsVal.setText(CommonUtil.convertObjToStr(tblSelectedAccount.getRowCount()));
        } else {
            ClientUtil.showMessageWindow("No Accounts to remove!!!");
            return;
        }
    }//GEN-LAST:event_btnAccountRemoveActionPerformed

    private void tblSelectedAccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSelectedAccountMouseClicked
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (tblSelectedAccount.getRowCount() > 0) {
                btnAccountAdd.setEnabled(true);
                btnAccountRemove.setEnabled(true);
            }
        }
    }//GEN-LAST:event_tblSelectedAccountMouseClicked

    private void txtNewCustomerIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNewCustomerIdActionPerformed
        // TODO add your handling code here:        
    }//GEN-LAST:event_txtNewCustomerIdActionPerformed

    private void txtNewCustomerIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNewCustomerIdFocusLost
        // TODO add your handling code here:        
        String customerId = CommonUtil.convertObjToStr(txtNewCustomerId.getText());
        if (customerId.length() > 0) {
            HashMap newCustomerDetailMap = new HashMap();
            newCustomerDetailMap.put("CUSTOMER_ID", txtNewCustomerId.getText());
            newCustomerDetailMap.put("BRANCH_CODE", getSelectedBranchID());
            List mapDataList = ClientUtil.executeQuery("getSelectCustDetails", newCustomerDetailMap);
            if (mapDataList != null && mapDataList.size() > 0) {
                newCustomerDetailMap = (HashMap) mapDataList.get(0);
                viewType = CUST_ID;
                lblNewCustName.setText(CommonUtil.convertObjToStr(newCustomerDetailMap.get("NAME")));
            } else {
                ClientUtil.showAlertWindow("Invalid Customer ID");
                txtNewCustomerId.setText("");
                lblNewCustName.setText("");
                return;
            }

            //viewType=CUST_ID;
            //popUp();
        }
    }//GEN-LAST:event_txtNewCustomerIdFocusLost

    private void btnCustomerIdAIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerIdAIActionPerformed
        // Add your handling code here:
        viewType = CUST_ID;
        new CheckCustomerIdUI(this);
//        viewType = CUST_ID;
//        popUp();
    }//GEN-LAST:event_btnCustomerIdAIActionPerformed

    private void btnClear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear1ActionPerformed
        // TODO add your handling code here:
        txtCustomerName.setText("");
        txtCustomerID.setText("");
        txtCareOfName.setText("");
        txtUniqueIdNo.setText("");
        txtPanNO.setText("");
        txtPassPortNo.setText("");
        tdtDtOfBirth.setDateValue("");
        txtPhoneNumber.setText("");
        lblBranchName.setText("Branch Name");
        lblNewCustName.setText("");
        btnCustomerIdAI.setEnabled(false);
        btnAccountAdd.setEnabled(false);
        btnAccountRemove.setEnabled(false);
        btnSearch.setEnabled(true);
        ClientUtil.enableDisable(panCustomerDetails, true);
        selectedList = new ArrayList();
        ClientUtil.clearAll(this);
    }//GEN-LAST:event_btnClear1ActionPerformed

    /**
     * To populate Comboboxes
     */
    private void initComponentData() {
        //cboProdType.setModel(observable.getCbmProdType());
    }

    /**
     * To display a popUp window for viewing existing data
     */
    private void popUp() {
        HashMap whereMap = new HashMap();
        HashMap viewMap = new HashMap();
        if (viewType == CUST_ID) {
            whereMap.put("BRANCH_CODE", getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_NAME, "getSelectCustDetails");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            whereMap.put("INIT_BRANCH", getSelectedBranchID());
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                viewMap.put(CommonConstants.MAP_NAME, "getSelectMultipleAccountsForEnquiry");
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "getSelectMultipleAccountsForEdit");
            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        }
    }

    /**
     * Called by the Popup window created thru popUp method
     */
    public void fillData(Object obj) {
        try {
            final HashMap hash = (HashMap) obj;
            setModified(true);
            System.out.println("filldata####" + hash);
            if (viewType == CUST_ID) {
                lblNewCustName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
                txtNewCustomerId.setText(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
            }

            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                ArrayList tempList = new ArrayList();
                selectedList = new ArrayList();
                observable.getData(hash);
                btnSearchActionPerformed(null);
                btnCancel.setEnabled(true);
                btnNew.setEnabled(false);
                btnEdit.setEnabled(false);
                btnDelete.setEnabled(false);
                btnAuthorize.setEnabled(false);
                btnReject.setEnabled(false);
                btnSave.setEnabled(true);
                TableModel model = new TableModel(observable.getTblSelectedAccounts().getDataArrayList(), TabTitle);
                tblSelectedAccount.setModel(model);
                tempList.add(observable.getTotalAccts());
                tempList = (ArrayList) tempList.get(0);
                if (tempList != null && tempList.size() > 0) {
                    for (int i = 0; i < tempList.size(); i++) {
                        selectedList.add(tempList.get(i));
                    }
                }
                data = new ArrayList();
                lblSelectedAcCountsVal.setText(CommonUtil.convertObjToStr(tblSelectedAccount.getRowCount()));
            }

            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                tblCustomerList.setEnabled(false);
                tblAvailAccount.setEnabled(false);
                tblSelectedAccount.setEnabled(false);
            } else {
                tblCustomerList.setEnabled(true);
                tblAvailAccount.setEnabled(true);
                tblSelectedAccount.setEnabled(true);
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
                btnAuthorize.setEnabled(true);
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                btnReject.setEnabled(true);
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void enableDisable(boolean yesno) {
        ClientUtil.enableDisable(this, yesno);
        btnSearch.setEnabled(yesno);
        btnClear.setEnabled(yesno);
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
        //btnAccountNumber.setEnabled(false);
        //btnNewCustID.setEnabled(false);
    }

    public void update(Observable observed, Object arg) {
        txtCustomerName.setText(observable.getTxtCustomerName());
        cboSearchCriteria.setSelectedItem(observable.getCboSearchCriteria());
        txtMemberNo.setText(observable.getTxtMemberNo());
        txtAddress.setText(observable.getTxtAddress());
        txtPhoneNumber.setText(observable.getTxtPhoneNumber());
        txtCareOfName.setText(observable.getTxtCareOfName());
        txtCustomerID.setText(observable.getTxtCustomerID());
        txtEmployeeNo.setText(observable.getTxtEmployeeNo());
        txtUniqueIdNo.setText(observable.getTxtUniqueIdNo());
        txtPanNO.setText(observable.getTxtPanNO());
        txtPassPortNo.setText(observable.getTxtPassPortNo());
        tdtDtOfBirth.setDateValue(observable.getTdtDtOfBirth());
        txtBranchId.setText(observable.getTxtBranchId());
        lblBranchName.setText(observable.getLblBranchName());
        txtAccountNumber.setText(observable.getTxtAccountNumber());
        txtNewCustomerId.setText(observable.getTxtNewCustomerId());
        lblNewCustName.setText(observable.getTxtNewAcctName());
        lblStatus.setText(observable.getLblStatus());
    }

    public void updateOBFields() {
        observable.setTxtCustomerName(txtCustomerName.getText());
        observable.setCboSearchCriteria(CommonUtil.convertObjToStr(cboSearchCriteria.getSelectedItem()));
        observable.setTxtMemberNo(txtMemberNo.getText());
        observable.setTxtAddress(txtAddress.getText());
        observable.setTxtPhoneNumber(txtPhoneNumber.getText());
        observable.setTxtCareOfName(txtCareOfName.getText());
        observable.setTxtCustomerID(txtCustomerID.getText());
        observable.setTxtEmployeeNo(txtEmployeeNo.getText());
        observable.setTxtUniqueIdNo(txtUniqueIdNo.getText());
        observable.setTxtPanNO(txtPanNO.getText());
        observable.setTxtPassPortNo(txtPassPortNo.getText());
        observable.setTdtDtOfBirth(tdtDtOfBirth.getDateValue());
        observable.setTxtBranchId(txtBranchId.getText());
        observable.setLblBranchName(lblBranchName.getText());
        observable.setTxtAccountNumber(txtAccountNumber.getText());
        observable.setLblNewCustName(lblNewCustName.getText());
        observable.setTxtNewCustomerId(txtNewCustomerId.getText());
        observable.setNewCustID(txtNewCustomerId.getText());
        observable.setTxtNewAcctName(lblNewCustName.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }

    private void savePerformed() {
        observable.setSelectedAccountList(selectedList);
//        HashMap lockMap = new HashMap(); 
//        ArrayList lst = new ArrayList();
//        lst.add("ACT_NUM");
//        lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
//        lockMap.put("ACT_NUM",CommonUtil.convertObjToStr(txtAccountNumber.getText()));
        observable.doAction();
        observable.resetForm();
        enableDisable(false);
        setButtonEnableDisable();
        //btnAccountNumber.setEnabled(false);
        lblStatus.setText(observable.getLblStatus());
        viewType = -1;
        ClientUtil.enableDisable(this, false);
        observable.setResultStatus();
        if (observable.getResult() != 4) {
//            setEditLockMap(lockMap);Commented By Revathi
//            setEditLock();
        }
        //__ Make the Screen Closable..
        setModified(false);
        observable.ttNotifyObservers();
    }

    private void setFieldNames() {
    }

    private void internationalize() {
        /*java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.customer.customeridchange.CustomerIdChangeRB", ProxyParameters.LANGUAGE);
         lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
         lblProductId.setText(resourceBundle.getString("lblProductId"));
         lblOldCustID.setText(resourceBundle.getString("lblOldCustID"));
         lblNewCustId.setText(resourceBundle.getString("lblNewCustId"));
         lblProdType.setText(resourceBundle.getString("lblProdType"));*/
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    public void clearProdFields() {
        txtAccountNumber.setText("");
    }

    private String getLockDetails(String lockedBy, String screenId) {
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer();
        map.put("LOCKED_BY", lockedBy);
        map.put("SCREEN_ID", screenId);
        java.util.List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if (lstLock.size() > 0) {
            map = (HashMap) (lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME"));
            data.append("\nIP Address : ").append(map.get("IP_ADDR"));
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null;
        map = null;
        return data.toString();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccountAdd;
    private com.see.truetransact.uicomponent.CButton btnAccountRemove;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClear1;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustomerIdAI;
    private com.see.truetransact.uicomponent.CButton btnDateChange;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSearch;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboSearchCriteria;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblAddress;
    private com.see.truetransact.uicomponent.CLabel lblBranchId;
    private com.see.truetransact.uicomponent.CLabel lblBranchName;
    private com.see.truetransact.uicomponent.CLabel lblCareOfName;
    private com.see.truetransact.uicomponent.CLabel lblCustomerID;
    private com.see.truetransact.uicomponent.CLabel lblCustomerIdAI;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblDtOfBirth;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeNo;
    private com.see.truetransact.uicomponent.CLabel lblMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNewCustName;
    private com.see.truetransact.uicomponent.CLabel lblPanNO;
    private com.see.truetransact.uicomponent.CLabel lblPassPortNo;
    private com.see.truetransact.uicomponent.CLabel lblPhoneNumber;
    private com.see.truetransact.uicomponent.CLabel lblSelectedAcCounts;
    private com.see.truetransact.uicomponent.CLabel lblSelectedAcCountsVal;
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
    private com.see.truetransact.uicomponent.CLabel lblUniqueIdNo;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountdetails;
    private com.see.truetransact.uicomponent.CPanel panAsset;
    private com.see.truetransact.uicomponent.CPanel panAsset1;
    private com.see.truetransact.uicomponent.CPanel panCustDetails;
    private com.see.truetransact.uicomponent.CPanel panCustomerDetail;
    private com.see.truetransact.uicomponent.CPanel panCustomerDetail1;
    private com.see.truetransact.uicomponent.CPanel panCustomerDetails;
    private com.see.truetransact.uicomponent.CPanel panEmpBtn;
    private com.see.truetransact.uicomponent.CPanel panEmpBtn1;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransactionType;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpAssetCTable;
    private com.see.truetransact.uicomponent.CScrollPane srpAssetCTable1;
    private com.see.truetransact.uicomponent.CScrollPane srpRepaymentCTable;
    private com.see.truetransact.uicomponent.CTabbedPane tabAccountDetails;
    private com.see.truetransact.uicomponent.CTabbedPane tabAccountDetails1;
    private com.see.truetransact.uicomponent.CTable tblAvailAccount;
    private com.see.truetransact.uicomponent.CTable tblCustomerList;
    private com.see.truetransact.uicomponent.CTable tblSelectedAccount;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtDtOfBirth;
    private com.see.truetransact.uicomponent.CTextField txtAccountNumber;
    private com.see.truetransact.uicomponent.CTextField txtAddress;
    private com.see.truetransact.uicomponent.CTextField txtBranchId;
    private com.see.truetransact.uicomponent.CTextField txtCareOfName;
    private com.see.truetransact.uicomponent.CTextField txtCustomerID;
    private com.see.truetransact.uicomponent.CTextField txtCustomerName;
    private com.see.truetransact.uicomponent.CTextField txtEmployeeNo;
    private com.see.truetransact.uicomponent.CTextField txtMemberNo;
    private com.see.truetransact.uicomponent.CTextField txtNewCustomerId;
    private com.see.truetransact.uicomponent.CTextField txtPanNO;
    private com.see.truetransact.uicomponent.CTextField txtPassPortNo;
    private com.see.truetransact.uicomponent.CTextField txtPhoneNumber;
    private com.see.truetransact.uicomponent.CTextField txtUniqueIdNo;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        MultipleCustomerIdChangeUI tod = new MultipleCustomerIdChangeUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(tod);
        j.show();
        tod.show();
    }
}
