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

package com.see.truetransact.ui.sysadmin.fixedassets;

import java.util.HashMap;
import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.clientexception.ClientParseException;
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
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uivalidation.*;
import com.see.truetransact.uicomponent.CButtonGroup;


/** This form is used to manipulate FixedAssetsUI related functionality
 * @author swaroop
 */
public class FixedAssetsUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField{
    
    private String viewType = new String();
    private HashMap mandatoryMap;
    private FixedAssetsOB observable;
    final String AUTHORIZE="Authorize";
    private final static Logger log = Logger.getLogger(FixedAssetsUI.class);
    FixedAssetsRB fixedassetsRB = new FixedAssetsRB();
    private String prodType="";
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    boolean isFilled = true;
    //    java.util.ResourceBundle objMandatoryRB;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.fixedassets.FixedAssetsRB", ProxyParameters.LANGUAGE);
    String cust_type=null;
    private Date currDt = null;
    /** Creates new form FixedAssetsUI */
    public FixedAssetsUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUp();
    }
    private void initStartUp(){
        setMandatoryHashMap();
        setFieldNames();
        internationalize();
        observable = new FixedAssetsOB();
        observable.addObserver(this);
        initComponentData();
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panFixedAssets);
        setHelpMessage();
        tabFixedAssets.resetVisits();
        txtAssetId.setEnabled(false);
        enableDisableButtons(false);
        ClientUtil.clearAll(this);
    }
    private void setMaxLength() {
        txtProvision.setValidation(new NumericValidation(2,2));
        txtCurValRoundOff.setValidation(new NumericValidation(4,0));
    }
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAssetType", new Boolean(true));
        mandatoryMap.put("txtAssetDesc", new Boolean(true));
        mandatoryMap.put("txtProvision", new Boolean(true));
                
        mandatoryMap.put("txtpurchaseDebit", new Boolean(true));
        mandatoryMap.put("txtProvDebit", new Boolean(true));
        mandatoryMap.put("txtSellAcHdID", new Boolean(true));
        mandatoryMap.put("txtNullifyingDebit", new Boolean(true));
        mandatoryMap.put("txtNullifyingCredit", new Boolean(true));
    }
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    /****************** NEW METHODS *****************/
    
    private void updateAuthorizeStatus(String authorizeStatus) {
        
        if (viewType == AUTHORIZE && isFilled){
            String strWarnMsg = tabFixedAssets.isAllTabsVisited();
            if (strWarnMsg.length() > 0) {
                CMandatoryDialog cmd = new CMandatoryDialog();
                cmd.setMessage(strWarnMsg);
                cmd.setModal(true);
                cmd.show();
            }else{
                tabFixedAssets.resetVisits();
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", authorizeStatus);
                singleAuthorizeMap.put("FIXED_ASSETS_ID", observable.getAssetID());
                singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("AUTH_DT",ClientUtil.getCurrentDateWithTime());
                singleAuthorizeMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                arrList.add(singleAuthorizeMap);
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorize(authorizeMap,observable.getAssetID());
                viewType = "";
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
            whereMap.put("AUTHORIZE_MODE","AUTHORIZE_MODE");
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getFixedAssetsDetailsAuthorize");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    public void authorize(HashMap map,String id) {
        System.out.println("Authorize Map : " + map);
        
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            observable.doAction();
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(id);
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
            
        }
    }
    public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.set_authorizeMap(map);
            observable.doAction();
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    public void setHelpMessage() {
        FixedAssetsMRB objMandatoryRB = new FixedAssetsMRB();
        txtAssetType.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAssetType"));
        txtAssetDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAssetDesc"));
        txtProvision.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProvision"));
        txtCurValRoundOff.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCurValRoundOff"));
        
        txtpurchaseDebit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtpurchaseDebit"));
        txtProvDebit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProvDebit"));
        txtSellAcHdID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSellAcHdID"));
        txtNullifyingDebit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNullifyingDebit"));
        txtNullifyingCredit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNullifyingCredit"));
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

        rdoApplType = new com.see.truetransact.uicomponent.CButtonGroup();
        panFixedAssets = new com.see.truetransact.uicomponent.CPanel();
        tabFixedAssets = new com.see.truetransact.uicomponent.CTabbedPane();
        panAssetDetails = new com.see.truetransact.uicomponent.CPanel();
        panAssetData = new com.see.truetransact.uicomponent.CPanel();
        txtAssetType = new com.see.truetransact.uicomponent.CTextField();
        txtAssetDesc = new com.see.truetransact.uicomponent.CTextField();
        txtProvision = new com.see.truetransact.uicomponent.CTextField();
        lblAssetDesc = new com.see.truetransact.uicomponent.CLabel();
        lblProvision = new com.see.truetransact.uicomponent.CLabel();
        lblAssetType = new com.see.truetransact.uicomponent.CLabel();
        lblRoundOffType = new com.see.truetransact.uicomponent.CLabel();
        txtAssetId = new com.see.truetransact.uicomponent.CTextField();
        lblDepre = new com.see.truetransact.uicomponent.CLabel();
        rdoCurrentValue = new com.see.truetransact.uicomponent.CRadioButton();
        rdoFaceValue = new com.see.truetransact.uicomponent.CRadioButton();
        lblAssetId = new com.see.truetransact.uicomponent.CLabel();
        lblCurValRoundOff = new com.see.truetransact.uicomponent.CLabel();
        txtCurValRoundOff = new com.see.truetransact.uicomponent.CTextField();
        cboRoundOffType = new com.see.truetransact.uicomponent.CComboBox();
        panAssetHead = new com.see.truetransact.uicomponent.CPanel();
        panAssetHeadDetails = new com.see.truetransact.uicomponent.CPanel();
        txtNullifyingCredit = new com.see.truetransact.uicomponent.CTextField();
        lblNullifyingCredit = new com.see.truetransact.uicomponent.CLabel();
        lblpurchaseDebit = new com.see.truetransact.uicomponent.CLabel();
        lblProvDebit = new com.see.truetransact.uicomponent.CLabel();
        lblSellAcHdID = new com.see.truetransact.uicomponent.CLabel();
        lblNullifyingDebit = new com.see.truetransact.uicomponent.CLabel();
        txtpurchaseDebit = new com.see.truetransact.uicomponent.CTextField();
        btnNullifyingCredit = new com.see.truetransact.uicomponent.CButton();
        btnPurchaseDebit = new com.see.truetransact.uicomponent.CButton();
        btnProvDebit = new com.see.truetransact.uicomponent.CButton();
        txtProvDebit = new com.see.truetransact.uicomponent.CTextField();
        txtSellAcHdID = new com.see.truetransact.uicomponent.CTextField();
        txtNullifyingDebit = new com.see.truetransact.uicomponent.CTextField();
        btnSellAcHdID = new com.see.truetransact.uicomponent.CButton();
        btnNullifyingDebit = new com.see.truetransact.uicomponent.CButton();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace35 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace36 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace37 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace38 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace39 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace40 = new com.see.truetransact.uicomponent.CLabel();
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

        panFixedAssets.setMaximumSize(new java.awt.Dimension(650, 520));
        panFixedAssets.setMinimumSize(new java.awt.Dimension(650, 520));
        panFixedAssets.setPreferredSize(new java.awt.Dimension(650, 520));
        panFixedAssets.setLayout(new java.awt.GridBagLayout());

        panAssetDetails.setLayout(new java.awt.GridBagLayout());

        panAssetData.setBorder(javax.swing.BorderFactory.createTitledBorder("Asset Details"));
        panAssetData.setMinimumSize(new java.awt.Dimension(365, 250));
        panAssetData.setPreferredSize(new java.awt.Dimension(365, 250));
        panAssetData.setLayout(new java.awt.GridBagLayout());

        txtAssetType.setMaxLength(128);
        txtAssetType.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAssetType.setName("txtCompany");
        txtAssetType.setValidation(new DefaultValidation());
        txtAssetType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAssetTypeActionPerformed(evt);
            }
        });
        txtAssetType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAssetTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 3, 9);
        panAssetData.add(txtAssetType, gridBagConstraints);

        txtAssetDesc.setMaxLength(128);
        txtAssetDesc.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAssetDesc.setName("txtCompany");
        txtAssetDesc.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 3, 9);
        panAssetData.add(txtAssetDesc, gridBagConstraints);

        txtProvision.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProvision.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 3, 9);
        panAssetData.add(txtProvision, gridBagConstraints);

        lblAssetDesc.setText("Fixed Asset Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 1);
        panAssetData.add(lblAssetDesc, gridBagConstraints);

        lblProvision.setText("Fixed Asset Depreciation %");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 1);
        panAssetData.add(lblProvision, gridBagConstraints);

        lblAssetType.setText("Fixed Asset Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 1);
        panAssetData.add(lblAssetType, gridBagConstraints);

        lblRoundOffType.setText("Round Off Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 2, 1);
        panAssetData.add(lblRoundOffType, gridBagConstraints);

        txtAssetId.setBackground(new java.awt.Color(212, 208, 200));
        txtAssetId.setMaxLength(128);
        txtAssetId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAssetId.setName("txtCompany");
        txtAssetId.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 3, 9);
        panAssetData.add(txtAssetId, gridBagConstraints);

        lblDepre.setText("Depreciation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAssetData.add(lblDepre, gridBagConstraints);

        rdoApplType.add(rdoCurrentValue);
        rdoCurrentValue.setText("Current Value");
        rdoCurrentValue.setMaximumSize(new java.awt.Dimension(90, 18));
        rdoCurrentValue.setMinimumSize(new java.awt.Dimension(90, 18));
        rdoCurrentValue.setPreferredSize(new java.awt.Dimension(90, 18));
        rdoCurrentValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCurrentValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 16;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panAssetData.add(rdoCurrentValue, gridBagConstraints);

        rdoApplType.add(rdoFaceValue);
        rdoFaceValue.setText("Face Value");
        rdoFaceValue.setMaximumSize(new java.awt.Dimension(90, 18));
        rdoFaceValue.setMinimumSize(new java.awt.Dimension(90, 18));
        rdoFaceValue.setPreferredSize(new java.awt.Dimension(90, 18));
        rdoFaceValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoFaceValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 16;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panAssetData.add(rdoFaceValue, gridBagConstraints);

        lblAssetId.setText("Asset ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 2, 1);
        panAssetData.add(lblAssetId, gridBagConstraints);

        lblCurValRoundOff.setText("Current Value Round Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 2, 1);
        panAssetData.add(lblCurValRoundOff, gridBagConstraints);

        txtCurValRoundOff.setMaxLength(128);
        txtCurValRoundOff.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCurValRoundOff.setName("txtCompany");
        txtCurValRoundOff.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 3, 9);
        panAssetData.add(txtCurValRoundOff, gridBagConstraints);

        cboRoundOffType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 8, 3, 9);
        panAssetData.add(cboRoundOffType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 11, 4);
        panAssetDetails.add(panAssetData, gridBagConstraints);

        tabFixedAssets.addTab("Fixed Asset Details", panAssetDetails);

        panAssetHead.setLayout(new java.awt.GridBagLayout());

        panAssetHeadDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Asset Heads"));
        panAssetHeadDetails.setMinimumSize(new java.awt.Dimension(365, 200));
        panAssetHeadDetails.setPreferredSize(new java.awt.Dimension(365, 200));
        panAssetHeadDetails.setLayout(new java.awt.GridBagLayout());

        txtNullifyingCredit.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panAssetHeadDetails.add(txtNullifyingCredit, gridBagConstraints);

        lblNullifyingCredit.setText("Excess Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panAssetHeadDetails.add(lblNullifyingCredit, gridBagConstraints);

        lblpurchaseDebit.setText("Purchase Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panAssetHeadDetails.add(lblpurchaseDebit, gridBagConstraints);

        lblProvDebit.setText("Depreciation Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panAssetHeadDetails.add(lblProvDebit, gridBagConstraints);

        lblSellAcHdID.setText("Selling Ac Hd Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panAssetHeadDetails.add(lblSellAcHdID, gridBagConstraints);

        lblNullifyingDebit.setText("Excess Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panAssetHeadDetails.add(lblNullifyingDebit, gridBagConstraints);

        txtpurchaseDebit.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panAssetHeadDetails.add(txtpurchaseDebit, gridBagConstraints);

        btnNullifyingCredit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnNullifyingCredit.setToolTipText("Select");
        btnNullifyingCredit.setPreferredSize(new java.awt.Dimension(21, 21));
        btnNullifyingCredit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNullifyingCreditActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 2, 1);
        panAssetHeadDetails.add(btnNullifyingCredit, gridBagConstraints);

        btnPurchaseDebit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPurchaseDebit.setToolTipText("Select");
        btnPurchaseDebit.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPurchaseDebit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseDebitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 2, 1);
        panAssetHeadDetails.add(btnPurchaseDebit, gridBagConstraints);

        btnProvDebit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnProvDebit.setToolTipText("Select");
        btnProvDebit.setPreferredSize(new java.awt.Dimension(21, 21));
        btnProvDebit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProvDebitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 2, 1);
        panAssetHeadDetails.add(btnProvDebit, gridBagConstraints);

        txtProvDebit.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panAssetHeadDetails.add(txtProvDebit, gridBagConstraints);

        txtSellAcHdID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panAssetHeadDetails.add(txtSellAcHdID, gridBagConstraints);

        txtNullifyingDebit.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panAssetHeadDetails.add(txtNullifyingDebit, gridBagConstraints);

        btnSellAcHdID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSellAcHdID.setToolTipText("Select");
        btnSellAcHdID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSellAcHdID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSellAcHdIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 2, 1);
        panAssetHeadDetails.add(btnSellAcHdID, gridBagConstraints);

        btnNullifyingDebit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnNullifyingDebit.setToolTipText("Select");
        btnNullifyingDebit.setPreferredSize(new java.awt.Dimension(21, 21));
        btnNullifyingDebit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNullifyingDebitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 2, 1);
        panAssetHeadDetails.add(btnNullifyingDebit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 11, 4);
        panAssetHead.add(panAssetHeadDetails, gridBagConstraints);

        tabFixedAssets.addTab("Fixed Assets Heads", panAssetHead);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panFixedAssets.add(tabFixedAssets, gridBagConstraints);

        getContentPane().add(panFixedAssets, java.awt.BorderLayout.CENTER);

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

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace34);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace35.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace35.setText("     ");
        lblSpace35.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace35);

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

        lblSpace36.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace36.setText("     ");
        lblSpace36.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace36);

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

        lblSpace37.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace37.setText("     ");
        lblSpace37.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace37);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace38.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace38.setText("     ");
        lblSpace38.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace38);

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

        lblSpace39.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace39.setText("     ");
        lblSpace39.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace39);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        lblSpace40.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace40.setText("     ");
        lblSpace40.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace40);

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
    
    private void txtAssetTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAssetTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAssetTypeActionPerformed
    
    private void rdoCurrentValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCurrentValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoCurrentValueActionPerformed
    
    private void rdoFaceValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoFaceValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoFaceValueActionPerformed
    
    private void txtAssetTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAssetTypeFocusLost
        // TODO add your handling code here:
        HashMap existMap=new HashMap();
        existMap.put("ASSET_TYPE", CommonUtil.convertObjToStr(txtAssetType.getText()));
        List lst=ClientUtil.executeQuery("fixedAssetTypeChk", existMap);
        if(lst.size()>0 && lst != null) {
            existMap= new HashMap();
            existMap=(HashMap) lst.get(0);
            ClientUtil.showMessageWindow("Asset Type Already Exists:" + CommonUtil.convertObjToStr(existMap.get("ASSET_TYPE")));
            txtAssetType.setText("");
            existMap=null;
        }
    }//GEN-LAST:event_txtAssetTypeFocusLost
    
    private void btnNullifyingCreditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNullifyingCreditActionPerformed
        // TODO add your handling code here:
        popUp("NULLYFYING_CREDIT");
        txtAssetType.setEnabled(false);
    }//GEN-LAST:event_btnNullifyingCreditActionPerformed
    
    private void btnNullifyingDebitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNullifyingDebitActionPerformed
        // TODO add your handling code here:
        popUp("NULLYFYING_DEBIT");
        txtAssetType.setEnabled(false);
    }//GEN-LAST:event_btnNullifyingDebitActionPerformed
    
    private void btnSellAcHdIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSellAcHdIDActionPerformed
        // TODO add your handling code here:
        popUp("SELLING_AC_HD");
        txtAssetType.setEnabled(false);
    }//GEN-LAST:event_btnSellAcHdIDActionPerformed
    
    private void btnProvDebitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProvDebitActionPerformed
        // TODO add your handling code here:
        popUp("PROVISION_DEBIT");
        txtAssetType.setEnabled(false);
    }//GEN-LAST:event_btnProvDebitActionPerformed
    
    private void btnPurchaseDebitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseDebitActionPerformed
        // TODO add your handling code here:
        popUp("PURCHASE_DEBIT");
        txtAssetType.setEnabled(false);
    }//GEN-LAST:event_btnPurchaseDebitActionPerformed
    
    private void btnDateChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateChangeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDateChangeActionPerformed
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        btnView.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        popUp("Enquiry");
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
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
        setModified(true);
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
        popUp("Delete");
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp("Edit");
        lblStatus.setText("Edit");
        btnDelete.setEnabled(false);
        txtAssetId.setEnabled(false);
        txtAssetType.setEnabled(false);
        ClientUtil.enableDisable(panAssetHeadDetails,true);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        deletescreenLock();
        viewType = "CANCEL" ;
        observable.resetForm();
        lblStatus.setText("               ");
        observable.setStatus();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);
        setModified(false);
        btnNew.setEnabled(true);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        enableDisableButtons(false);
        rdoFaceValue.setEnabled(false);
        rdoCurrentValue.setEnabled(false);
        isFilled = false;
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        updateOBFields();
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panFixedAssets);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }else{
            savePerformed();
            btnCancel.setEnabled(true);
            enableDisableButtons(false);
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        observable.setStatus();
        setButtonEnableDisable();
        ClientUtil.enableDisable(panAssetData,true);
        ClientUtil.enableDisable(panAssetHeadDetails,true);
        txtpurchaseDebit.setEnabled(true);
        txtProvDebit.setEnabled(true);
        txtSellAcHdID.setEnabled(true);
        txtNullifyingDebit.setEnabled(true);
        txtNullifyingCredit.setEnabled(true);
        enableDisableButtons(true);
        rdoFaceValue.setEnabled(true);
        rdoCurrentValue.setEnabled(true);
        txtAssetId.setEnabled(false);
        ClientUtil.clearAll(this);
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
    
    /** To populate Comboboxes */
    private void initComponentData() {
        try{
            cboRoundOffType.setModel(observable.getCbmRoundOffType());
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
    /** To display a popUp window for viewing existing data */
    private void popUp(String currAction){
        viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getFixedAssetsDetailsEdit");
        }else if(currAction.equalsIgnoreCase("Delete")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getFixedAssetsDetailsDelete");
        }
        else if(currAction.equalsIgnoreCase("Enquiry")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getFixedAssetsDetailsView");
        }
        else if(currAction.equalsIgnoreCase("PURCHASE_DEBIT") || currAction.equalsIgnoreCase("PROVISION_DEBIT") || currAction.equalsIgnoreCase("SELLING_AC_HD")
        || currAction.equalsIgnoreCase("NULLYFYING_DEBIT") || currAction.equalsIgnoreCase("NULLYFYING_CREDIT")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
        }
        new ViewAll(this,viewMap).show();
    }
    
    /** Called by the Popup window created thru popUp method */
    public void fillData(Object map) {
        try{
            isFilled = true;
            setModified(true);
            HashMap hash = (HashMap) map;
            if (viewType != null) {
                if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
                viewType.equals(ClientConstants.ACTION_STATUS[3])|| viewType.equals(AUTHORIZE) ||
                viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                    
                    hash.put(CommonConstants.MAP_WHERE, hash.get("FIXED_ASSETS_ID"));
                    hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                    observable.getData(hash);
                    if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
                    viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                        ClientUtil.enableDisable(panAssetDetails, false);
                        enableDisableButtons(false);
                    } else {
                        ClientUtil.enableDisable(panAssetDetails, true);
                        enableDisableButtons(true);
                    }
                    setButtonEnableDisable();
                    if(viewType ==  AUTHORIZE) {
                        btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                        btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                        btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    }
                }
            }
            if(viewType.equals(ClientConstants.ACTION_STATUS[2]) || viewType.equals(AUTHORIZE)) {
                HashMap screenMap = new HashMap();
                screenMap.put("TRANS_ID",hash.get("FIXED_ASSETS_ID"));
                screenMap.put("USER_ID",ProxyParameters.USER_ID);
                screenMap.put("TRANS_DT", currDt.clone());
                screenMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                List lst=ClientUtil.executeQuery("selectauthorizationLock", screenMap);
                if(lst !=null && lst.size()>0) {
                    screenMap=null;
                    StringBuffer open=new StringBuffer();
                    for(int i=0;i<lst.size();i++){
                        screenMap=(HashMap)lst.get(i);
                        open.append("\n"+"User Id  :"+" ");
                        open.append(CommonUtil.convertObjToStr(screenMap.get("OPEN_BY"))+"\n");
                        open.append("Mode Of Operation  :" +" ");
                        open.append(CommonUtil.convertObjToStr(screenMap.get("MODE_OF_OPERATION"))+" ");
                        btnSave.setEnabled(false);
                        ClientUtil.enableDisable(panAssetDetails, false);
                    }
                    ClientUtil.showMessageWindow("already open by"+open);
                    return;
                }
                else{
                    hash.put("TRANS_ID",hash.get("FIXED_ASSETS_ID"));
                    if(viewType.equals(ClientConstants.ACTION_STATUS[2]))
                        hash.put("MODE_OF_OPERATION","EDIT");
                    if(viewType==AUTHORIZE)
                        hash.put("MODE_OF_OPERATION","AUTHORIZE");
                    hash.put("USER_ID",TrueTransactMain.USER_ID);
                    hash.put("TRANS_DT", currDt.clone());
                    hash.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                    ClientUtil.execute("insertauthorizationLock", hash);
                }
            }
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                String lockedBy = "";
                HashMap Lockmap = new HashMap();
                Lockmap.put("SCREEN_ID", getScreenID());
                Lockmap.put("RECORD_KEY", CommonUtil.convertObjToStr(hash.get("FIXED_ASSETS_ID")));
                Lockmap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
//                Lockmap.put("CUR_DATE", currDt.clone());
                System.out.println("Record Key Map : " + Lockmap);
                List lstLock = ClientUtil.executeQuery("selectEditLock", Lockmap);
                if (lstLock.size() > 0) {
                    lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
                    if (!lockedBy.equals(ProxyParameters.USER_ID)) {
                        btnSave.setEnabled(false);
                        ClientUtil.enableDisable(panAssetDetails, false);
                    } else {
                        btnSave.setEnabled(true);
                        ClientUtil.enableDisable(panAssetDetails, true);
                    }
                } else {
                    btnSave.setEnabled(true);
                    ClientUtil.enableDisable(panAssetDetails, true);
                }
                setOpenForEditBy(lockedBy);
                if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
                    String data = getLockDetails(lockedBy, getScreenID()) ;
                    ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
                    btnSave.setEnabled(false);
                    ClientUtil.enableDisable(panAssetDetails, false);
                }
            }
            if(viewType.equalsIgnoreCase("PURCHASE_DEBIT")){
                txtpurchaseDebit.setText((String) hash.get("A/C HEAD"));
            }
            else if(viewType.equalsIgnoreCase("PROVISION_DEBIT")){
                txtProvDebit.setText((String) hash.get("A/C HEAD"));
            }
            else if(viewType.equalsIgnoreCase("SELLING_AC_HD")){
                txtSellAcHdID.setText((String) hash.get("A/C HEAD"));
            }
            else if(viewType.equalsIgnoreCase("NULLYFYING_DEBIT")){
                txtNullifyingDebit.setText((String) hash.get("A/C HEAD"));
            }
            else if(viewType.equalsIgnoreCase("NULLYFYING_CREDIT")){
                txtNullifyingCredit.setText((String) hash.get("A/C HEAD"));
            }
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
        lblStatus.setText(observable.getLblStatus());
    }
    public void update(Observable observed, Object arg) {
        txtAssetDesc.setText(observable.getAssetDesc());
        txtAssetType.setText(observable.getAssetType());
        txtNullifyingCredit.setText(observable.getNullifyingCredit());
        txtNullifyingDebit.setText(observable.getNullifyingDebit());
        txtProvDebit.setText(observable.getProvDebit());
        txtProvision.setText(observable.getProvision());
        txtCurValRoundOff.setText(observable.getCurValRoundOff());
        cboRoundOffType.setModel(observable.getCbmRoundOffType());
        txtSellAcHdID.setText(observable.getSellingAcID());
        txtpurchaseDebit.setText(observable.getPurchaseDebit());
        txtAssetId.setText(observable.getAssetID());
        if(CommonUtil.convertObjToStr(observable.getRdoDepYesNo()).length()>0 && CommonUtil.convertObjToStr(observable.getRdoDepYesNo()).equals("Current_Value")){
            rdoCurrentValue.setSelected(true);
        }else{
            rdoFaceValue.setSelected(true);
        }
    }
    public void updateOBFields() {
        observable.setAssetDesc(txtAssetDesc.getText());
        observable.setAssetID(txtAssetId.getText());
        observable.setAssetType(txtAssetType.getText());
        observable.setNullifyingCredit(txtNullifyingCredit.getText());
        observable.setNullifyingDebit(txtNullifyingDebit.getText());
        observable.setProvDebit(txtProvDebit.getText());
        observable.setProvision(txtProvision.getText());
        observable.setCurValRoundOff(txtCurValRoundOff.getText());
        observable.setPurchaseDebit(txtpurchaseDebit.getText());
        observable.setSellingAcID(txtSellAcHdID.getText());
        observable.setCboRoundOffType((String) cboRoundOffType.getSelectedItem());
        if(rdoCurrentValue.isSelected()==true)
            observable.setRdoDepYesNo(CommonUtil.convertObjToStr("Current_Value"));
        if(rdoFaceValue.isSelected()==true)
            observable.setRdoDepYesNo(CommonUtil.convertObjToStr("Face_Value"));
    }
    private void savePerformed(){
        updateOBFields();
        observable.doAction() ;
        if(rdoFaceValue.isSelected()==false && rdoCurrentValue.isSelected()==false)
            ClientUtil.displayAlert("Please Select Face Value Or Current Value");
        if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            lst.add("FIXED_ASSETS_ID");
            lockMap.put("FIXED_ASSETS_ID",CommonUtil.convertObjToStr(txtAssetId.getText()));
            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("FIXED_ASSETS_ID")) {
                        lockMap.put("FIXED_ASSETS_ID",observable.getProxyReturnMap().get("FIXED_ASSETS_ID"));
                    }
                }
            }
            if(observable.getResult()==ClientConstants.ACTIONTYPE_EDIT){
                lockMap.put("FIXED_ASSETS_ID", observable.getAssetID());
            }
            setEditLockMap(lockMap);
            setEditLock();
            deletescreenLock();
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
        txtAssetDesc.setName("txtAssetDesc");
        txtAssetType.setName("txtAssetType");
        txtNullifyingCredit.setName("txtNullifyingCredit");
        txtNullifyingCredit.setName("txtNullifyingCredit");
        txtNullifyingDebit.setName("txtNullifyingDebit");
        txtProvDebit.setName("txtProvDebit");
        txtProvision.setName("txtProvision");
        txtSellAcHdID.setName("txtSellAcHdID");
        txtpurchaseDebit.setName("txtpurchaseDebit");
        lblAssetDesc.setName("lblAssetDesc");
        lblAssetType.setName("lblAssetType");
        lblNullifyingDebit.setName("lblNullifyingDebit");
        lblNullifyingCredit.setName("lblNullifyingCredit");
        lblProvDebit.setName("lblProvDebit");
        lblProvision.setName("lblProvision");
        lblpurchaseDebit.setName("lblpurchaseDebit");
        lblSellAcHdID.setName("lblSellAcHdID");
        btnNullifyingCredit.setName("btnNullifyingCredit");
        btnNullifyingDebit.setName("btnNullifyingDebit");
        btnProvDebit.setName("btnProvDebit");
        btnPurchaseDebit.setName("btnPurchaseDebit");
        btnSellAcHdID.setName("btnSellAcHdID");
        panAssetDetails.setName("panAssetDetails");
        panAssetHeadDetails.setName("panAssetHeadDetails");
        panFixedAssets.setName("panFixedAssets");
        txtAssetId.setName("txtAssetId");
        lblAssetId.setName("lblAssetId");
        lblDepre.setName("lblDepre");
        rdoFaceValue.setName("rdoFaceValue");
        rdoCurrentValue.setName("rdoCurrentValue");
        cboRoundOffType.setName("cboRoundOffType");
        lblRoundOffType.setName("lblRoundOffType");
        lblCurValRoundOff.setName("lblCurValRoundOff");
    }
    private void internationalize() {
        lblAssetType.setText(resourceBundle.getString("lblAssetType"));
        lblAssetDesc.setText(resourceBundle.getString("lblAssetDesc"));
        lblProvision.setText(resourceBundle.getString("lblProvision"));
        lblpurchaseDebit.setText(resourceBundle.getString("lblpurchaseDebit"));
        lblProvDebit.setText(resourceBundle.getString("lblProvDebit"));
        lblSellAcHdID.setText(resourceBundle.getString("lblSellAcHdID"));
        lblNullifyingDebit.setText(resourceBundle.getString("lblNullifyingDebit"));
        lblNullifyingCredit.setText(resourceBundle.getString("lblNullifyingCredit"));
        lblDepre.setText(resourceBundle.getString("lblDepre"));
        lblRoundOffType.setText(resourceBundle.getString("lblRoundOffType"));
        lblCurValRoundOff.setText(resourceBundle.getString("lblCurValRoundOff"));
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
    public void enableDisableButtons(boolean flag){
        btnNullifyingCredit.setEnabled(flag);
        btnNullifyingDebit.setEnabled(flag);
        btnSellAcHdID.setEnabled(flag);
        btnProvDebit.setEnabled(flag);
        btnPurchaseDebit.setEnabled(flag);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDateChange;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNullifyingCredit;
    private com.see.truetransact.uicomponent.CButton btnNullifyingDebit;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProvDebit;
    private com.see.truetransact.uicomponent.CButton btnPurchaseDebit;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSellAcHdID;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboRoundOffType;
    private com.see.truetransact.uicomponent.CLabel lblAssetDesc;
    private com.see.truetransact.uicomponent.CLabel lblAssetId;
    private com.see.truetransact.uicomponent.CLabel lblAssetType;
    private com.see.truetransact.uicomponent.CLabel lblCurValRoundOff;
    private com.see.truetransact.uicomponent.CLabel lblDepre;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNullifyingCredit;
    private com.see.truetransact.uicomponent.CLabel lblNullifyingDebit;
    private com.see.truetransact.uicomponent.CLabel lblProvDebit;
    private com.see.truetransact.uicomponent.CLabel lblProvision;
    private com.see.truetransact.uicomponent.CLabel lblRoundOffType;
    private com.see.truetransact.uicomponent.CLabel lblSellAcHdID;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace35;
    private com.see.truetransact.uicomponent.CLabel lblSpace36;
    private com.see.truetransact.uicomponent.CLabel lblSpace37;
    private com.see.truetransact.uicomponent.CLabel lblSpace38;
    private com.see.truetransact.uicomponent.CLabel lblSpace39;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace40;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblpurchaseDebit;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAssetData;
    private com.see.truetransact.uicomponent.CPanel panAssetDetails;
    private com.see.truetransact.uicomponent.CPanel panAssetHead;
    private com.see.truetransact.uicomponent.CPanel panAssetHeadDetails;
    private com.see.truetransact.uicomponent.CPanel panFixedAssets;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoApplType;
    private com.see.truetransact.uicomponent.CRadioButton rdoCurrentValue;
    private com.see.truetransact.uicomponent.CRadioButton rdoFaceValue;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CTabbedPane tabFixedAssets;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CTextField txtAssetDesc;
    private com.see.truetransact.uicomponent.CTextField txtAssetId;
    private com.see.truetransact.uicomponent.CTextField txtAssetType;
    private com.see.truetransact.uicomponent.CTextField txtCurValRoundOff;
    private com.see.truetransact.uicomponent.CTextField txtNullifyingCredit;
    private com.see.truetransact.uicomponent.CTextField txtNullifyingDebit;
    private com.see.truetransact.uicomponent.CTextField txtProvDebit;
    private com.see.truetransact.uicomponent.CTextField txtProvision;
    private com.see.truetransact.uicomponent.CTextField txtSellAcHdID;
    private com.see.truetransact.uicomponent.CTextField txtpurchaseDebit;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
        FixedAssetsUI fa = new FixedAssetsUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(fa);
        j.show();
        fa.show();
    }
}
