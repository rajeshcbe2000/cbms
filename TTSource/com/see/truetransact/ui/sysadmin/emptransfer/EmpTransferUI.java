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

package com.see.truetransact.ui.sysadmin.emptransfer;

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
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uivalidation.*;
import com.see.truetransact.uicomponent.CButtonGroup;




/** This form is used to manipulate CustomerIdChangeUI related functionality
 * @author swaroop
 */
public class EmpTransferUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField{

    private String viewType = new String();
    private HashMap mandatoryMap;
    private EmpTransferOB observable;
    final String AUTHORIZE="Authorize";
    private final static Logger log = Logger.getLogger(EmpTransferUI.class);
    EmpTransferRB emptransferRB = new EmpTransferRB();
    private String prodType="";
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    String cust_type=null;
    private Date currDt = null;
    /** Creates new form CustomerIdChangeUI */
    public EmpTransferUI() {
        initComponents();
        initStartUp();
    }
    
    private void initStartUp(){
        currDt = ClientUtil.getCurrentDate();
        setMandatoryHashMap();
        setFieldNames();
        internationalize();
        observable = new EmpTransferOB();
        observable.addObserver(this);
        initComponentData();
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
         objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.emptransfer.EmpTransferMRB", ProxyParameters.LANGUAGE);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panTranferDetails);
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
        mandatoryMap.put("cboRoleCurrBran", new Boolean(true));
        mandatoryMap.put("cboRoleTransBran", new Boolean(true));
        mandatoryMap.put("txtEmpID", new Boolean(true));
        mandatoryMap.put("cboTransBran", new Boolean(true));
        mandatoryMap.put("tdtDoj", new Boolean(false));
        mandatoryMap.put("tdtLastDay", new Boolean(false));
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
            whereMap.put("CURR_DT",currDt.clone());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getEmpTransferDetailsAuthorize");
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
            singleAuthorizeMap.put("EMP_TRANSFER_ID", observable.getTxtEmpTransferID());
            singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTH_DT",ClientUtil.getCurrentDateWithTime());
            singleAuthorizeMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            String presentBranch = ((ComboBoxModel)cboTransBran.getModel()).getKeyForSelected().toString();
            if (presentBranch.lastIndexOf("-")!=-1)
            presentBranch = presentBranch.substring(0,presentBranch.lastIndexOf("-"));
            presentBranch= presentBranch.trim();
            singleAuthorizeMap.put("PRESENT_BRANCH_CODE",presentBranch);
            singleAuthorizeMap.put("EMP_ID", observable.getTxtEmpID());
            arrList.add(singleAuthorizeMap);         
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap,observable.getTxtEmpTransferID());
            viewType = "";
            super.setOpenForEditBy(observable.getStatusBy());
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
        panEmpTransfer = new com.see.truetransact.uicomponent.CPanel();
        panTranferDetails = new com.see.truetransact.uicomponent.CPanel();
        lblTranBran = new com.see.truetransact.uicomponent.CLabel();
        cboTransBran = new com.see.truetransact.uicomponent.CComboBox();
        lblLastDayCurrBran = new com.see.truetransact.uicomponent.CLabel();
        tdtLastDay = new com.see.truetransact.uicomponent.CDateField();
        tdtDoj = new com.see.truetransact.uicomponent.CDateField();
        lblDoj = new com.see.truetransact.uicomponent.CLabel();
        cboRoleCurrBran = new com.see.truetransact.uicomponent.CComboBox();
        cboRoleTransBran = new com.see.truetransact.uicomponent.CComboBox();
        panReqOfficial = new com.see.truetransact.uicomponent.CPanel();
        rdoReq_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoOff_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        panEmpDetails = new com.see.truetransact.uicomponent.CPanel();
        btnEmp = new com.see.truetransact.uicomponent.CButton();
        txtEmpID = new com.see.truetransact.uicomponent.CTextField();
        lblRoleCurr = new com.see.truetransact.uicomponent.CLabel();
        lblRoleTrans = new com.see.truetransact.uicomponent.CLabel();
        lblEmpId = new com.see.truetransact.uicomponent.CLabel();
        lblEmpTransferID = new com.see.truetransact.uicomponent.CLabel();
        lblCurrBranValue = new com.see.truetransact.uicomponent.CLabel();
        lblEmpName = new com.see.truetransact.uicomponent.CLabel();
        lblBranName = new com.see.truetransact.uicomponent.CLabel();
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

        panEmpTransfer.setMaximumSize(new java.awt.Dimension(650, 520));
        panEmpTransfer.setMinimumSize(new java.awt.Dimension(650, 520));
        panEmpTransfer.setPreferredSize(new java.awt.Dimension(650, 520));
        panEmpTransfer.setLayout(new java.awt.GridBagLayout());

        panTranferDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTranferDetails.setMinimumSize(new java.awt.Dimension(600, 400));
        panTranferDetails.setName("panMaritalStatus");
        panTranferDetails.setPreferredSize(new java.awt.Dimension(600, 400));
        panTranferDetails.setLayout(new java.awt.GridBagLayout());

        lblTranBran.setText("Transfer Branch");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTranferDetails.add(lblTranBran, gridBagConstraints);

        cboTransBran.setMinimumSize(new java.awt.Dimension(100, 21));
        cboTransBran.setPopupWidth(225);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTranferDetails.add(cboTransBran, gridBagConstraints);

        lblLastDayCurrBran.setText("Last Working Day");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTranferDetails.add(lblLastDayCurrBran, gridBagConstraints);

        tdtLastDay.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtLastDay.setName("tdtDateOfBirth");
        tdtLastDay.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTranferDetails.add(tdtLastDay, gridBagConstraints);

        tdtDoj.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtDoj.setName("tdtDateOfBirth");
        tdtDoj.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTranferDetails.add(tdtDoj, gridBagConstraints);

        lblDoj.setText("Date Of Joining ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTranferDetails.add(lblDoj, gridBagConstraints);

        cboRoleCurrBran.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRoleCurrBran.setPopupWidth(225);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTranferDetails.add(cboRoleCurrBran, gridBagConstraints);

        cboRoleTransBran.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRoleTransBran.setPopupWidth(225);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTranferDetails.add(cboRoleTransBran, gridBagConstraints);

        panReqOfficial.setMinimumSize(new java.awt.Dimension(200, 21));
        panReqOfficial.setPreferredSize(new java.awt.Dimension(200, 27));
        panReqOfficial.setLayout(new java.awt.GridBagLayout());

        rdoReq_Yes.setText("Request");
        rdoReq_Yes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        panReqOfficial.add(rdoReq_Yes, new java.awt.GridBagConstraints());

        rdoOff_Yes.setText("Official");
        rdoOff_Yes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        panReqOfficial.add(rdoOff_Yes, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTranferDetails.add(panReqOfficial, gridBagConstraints);

        panEmpDetails.setMinimumSize(new java.awt.Dimension(140, 24));
        panEmpDetails.setPreferredSize(new java.awt.Dimension(140, 24));
        panEmpDetails.setLayout(new java.awt.GridBagLayout());

        btnEmp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEmp.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnEmp.setMinimumSize(new java.awt.Dimension(25, 25));
        btnEmp.setPreferredSize(new java.awt.Dimension(25, 25));
        btnEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panEmpDetails.add(btnEmp, gridBagConstraints);

        txtEmpID.setMaxLength(128);
        txtEmpID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEmpID.setName("txtCompany");
        txtEmpID.setValidation(new DefaultValidation());
        txtEmpID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmpIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panEmpDetails.add(txtEmpID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panTranferDetails.add(panEmpDetails, gridBagConstraints);

        lblRoleCurr.setText("Role In Current Branch");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTranferDetails.add(lblRoleCurr, gridBagConstraints);

        lblRoleTrans.setText("Role In Transferred Branch");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTranferDetails.add(lblRoleTrans, gridBagConstraints);

        lblEmpId.setText("Emp ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTranferDetails.add(lblEmpId, gridBagConstraints);

        lblEmpTransferID.setMaximumSize(new java.awt.Dimension(80, 20));
        lblEmpTransferID.setMinimumSize(new java.awt.Dimension(80, 20));
        lblEmpTransferID.setName("lblRelationManager");
        lblEmpTransferID.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTranferDetails.add(lblEmpTransferID, gridBagConstraints);

        lblCurrBranValue.setMaximumSize(new java.awt.Dimension(80, 20));
        lblCurrBranValue.setMinimumSize(new java.awt.Dimension(80, 20));
        lblCurrBranValue.setName("lblRelationManager");
        lblCurrBranValue.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTranferDetails.add(lblCurrBranValue, gridBagConstraints);

        lblEmpName.setMaximumSize(new java.awt.Dimension(220, 20));
        lblEmpName.setMinimumSize(new java.awt.Dimension(220, 20));
        lblEmpName.setName("lblRelationManager");
        lblEmpName.setPreferredSize(new java.awt.Dimension(220, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTranferDetails.add(lblEmpName, gridBagConstraints);

        lblBranName.setMaximumSize(new java.awt.Dimension(220, 20));
        lblBranName.setMinimumSize(new java.awt.Dimension(220, 20));
        lblBranName.setName("lblRelationManager");
        lblBranName.setPreferredSize(new java.awt.Dimension(220, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTranferDetails.add(lblBranName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panEmpTransfer.add(panTranferDetails, gridBagConstraints);

        getContentPane().add(panEmpTransfer, java.awt.BorderLayout.CENTER);

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

    private void btnEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpActionPerformed
        // TODO add your handling code here:
        popUp("EMP");
    }//GEN-LAST:event_btnEmpActionPerformed

    private void txtEmpIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmpIDActionPerformed
        // TODO add your handling code here:
         if(txtEmpID.getText().length()>0){
            HashMap empMap = new HashMap();
            empMap.put("EMP_CODE",txtEmpID.getText());
            empMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            List empList = ClientUtil.executeQuery("getEmpDetailsForTransfer", empMap);
            if(empList!=null && empList.size()>0){
                empMap = null;
                empMap=(HashMap) empList.get(0);
                txtEmpID.setText(CommonUtil.convertObjToStr(empMap.get("EMPLOYEE_CODE")));
                lblCurrBranValue.setText(CommonUtil.convertObjToStr(empMap.get("PRESENT_BRANCH_CODE")));
                lblEmpName.setText(CommonUtil.convertObjToStr(empMap.get("FNAME")));
                lblBranName.setText(CommonUtil.convertObjToStr(empMap.get("BRANCH_NAME")));
            }
            else{
                ClientUtil.displayAlert("Invalid Employee ID");
                txtEmpID.setText("");
            }
        }
    }//GEN-LAST:event_txtEmpIDActionPerformed

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
        popUp("Delete");
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
    btnEmp.setEnabled(true);
     popUp("Edit");
    lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        deletescreenLock();
        viewType = "CANCEL" ;
        observable.resetForm();
        ClientUtil.clearAll(this);
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        lblStatus.setText("");
        lblBranName.setText("");
        lblEmpName.setText("");
        setModified(false);
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        updateOBFields();
       
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panTranferDetails);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }
        else{
            savePerformed();
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        enableDisable(true);
        setButtonEnableDisable();
        btnEmp.setEnabled(true);
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
                
    /** To populate Comboboxes */
    private void initComponentData() {
        cboRoleCurrBran.setModel(observable.getCbmRoleInCurrBran());
        cboRoleTransBran.setModel(observable.getCbmRoleInTranBran());
        cboTransBran.setModel(observable.getCbmTransferBran());
    }
    
    /** To display a popUp window for viewing existing data */
    private void popUp(String currAction){
       viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getEmpTransferDetailsEdit"); 
        }else if(currAction.equalsIgnoreCase("Delete")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getEmpTransferDetailsDelete");
        } else if(currAction.equalsIgnoreCase("EMP")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "setEmpDetailsForTransfer");
        }
        else if(currAction.equalsIgnoreCase("Enquiry")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getEmpTransferDetailsView");
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
                hash.put(CommonConstants.MAP_WHERE, hash.get("EMP_TRANSFER_ID"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                observable.getData(hash);
                if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
                viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                    ClientUtil.enableDisable(panTranferDetails, false);
                    btnEmp.setEnabled(false);
                } else {
                    ClientUtil.enableDisable(panTranferDetails, true);
                    btnEmp.setEnabled(true);
                }
                setButtonEnableDisable();
                if(viewType ==  AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
            }
            
        }
        if (viewType.equals("EMP")){
            txtEmpID.setText(CommonUtil.convertObjToStr(hash.get("EMPLOYEE_CODE")));
                lblCurrBranValue.setText(CommonUtil.convertObjToStr(hash.get("PRESENT_BRANCH_CODE")));
                lblEmpName.setText(CommonUtil.convertObjToStr(hash.get("FNAME")));
                lblBranName.setText(CommonUtil.convertObjToStr(hash.get("BRANCH_NAME")));
        }
         if(viewType.equals(ClientConstants.ACTION_STATUS[2]) || viewType.equals(AUTHORIZE)) {
            HashMap screenMap = new HashMap();
            screenMap.put("TRANS_ID",hash.get("EMP_TRANSFER_ID"));
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
                    ClientUtil.enableDisable(panTranferDetails, false);
                    btnEmp.setEnabled(false);
                }
                ClientUtil.showMessageWindow("already open by"+open);
                return;
            }
            else{
                hash.put("TRANS_ID",hash.get("EMP_TRANSFER_ID"));
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
            Lockmap.put("RECORD_KEY", CommonUtil.convertObjToStr(hash.get("EMP_TRANSFER_ID")));
            Lockmap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            System.out.println("Record Key Map : " + Lockmap);
            List lstLock = ClientUtil.executeQuery("selectEditLock", Lockmap);
            if (lstLock.size() > 0) {
                lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
                if (!lockedBy.equals(ProxyParameters.USER_ID)) {
                    btnSave.setEnabled(false);
                    ClientUtil.enableDisable(panTranferDetails, false);
                    btnEmp.setEnabled(false);
                } else {
                    btnSave.setEnabled(true);
                    ClientUtil.enableDisable(panTranferDetails, true);
                    btnEmp.setEnabled(true);
                }
            } else {
                btnSave.setEnabled(true);
                ClientUtil.enableDisable(panTranferDetails, true);
                btnEmp.setEnabled(true);
            }
            setOpenForEditBy(lockedBy);
            if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
                String data = getLockDetails(lockedBy, getScreenID()) ;
                ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
                btnSave.setEnabled(false);
                ClientUtil.enableDisable(panTranferDetails, false);
                btnEmp.setEnabled(false);
            }
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
    }
    
    
    public void update(Observable observed, Object arg) {
         removeRadioButtons();
        cboRoleCurrBran.setModel(observable.getCbmRoleInCurrBran());
        cboRoleTransBran.setModel(observable.getCbmRoleInTranBran());
        cboTransBran.setModel(observable.getCbmTransferBran());
        txtEmpID.setText(observable.getTxtEmpID());
        tdtDoj.setDateValue(observable.getTxtDoj());
        tdtLastDay.setDateValue(observable.getTxtLastWorkingDay());
        lblCurrBranValue.setText(observable.getTxtCurrBran());
        rdoOff_Yes.setSelected(observable.isRdoOff_Yes());
        rdoReq_Yes.setSelected(observable.isRdoApp_Yes());
        lblStatus.setText(observable.getLblStatus());
        lblEmpTransferID.setText(observable.getTxtEmpTransferID());
        lblEmpName.setText(observable.getEmpName());
        lblCurrBranValue.setText(observable.getCurrBranName());
        addRadioButtons();
    }
    
    public void updateOBFields() {
        observable.setCboRoleInCurrBran((String) cboRoleCurrBran.getSelectedItem());
        observable.setCboRoleInTranBran((String) cboRoleTransBran.getSelectedItem());
        observable.setCboTransferBran((String) cboTransBran.getSelectedItem());
        observable.setTxtEmpID(txtEmpID.getText());
        observable.setTxtDoj(tdtDoj.getDateValue());
        observable.setTxtLastWorkingDay(tdtLastDay.getDateValue());
        observable.setTxtCurrBran(lblCurrBranValue.getText());
        observable.setRdoApp_Yes(rdoReq_Yes.isSelected());
        observable.setRdoOff_Yes(rdoOff_Yes.isSelected()); 
        observable.setEmpName(lblEmpName.getText());
        observable.setCurrBranName(lblCurrBranValue.getText());
    }
    
    private void addRadioButtons() {
        rdoApplType = new CButtonGroup();
        rdoApplType.add(rdoOff_Yes);
        rdoApplType.add(rdoReq_Yes);
        
    }
    
    private void removeRadioButtons() {
        rdoApplType.remove(rdoOff_Yes);
        rdoApplType.remove(rdoReq_Yes);
    }
    
    private void savePerformed(){
        String transBranch = ((ComboBoxModel)cboTransBran.getModel()).getKeyForSelected().toString();
        if (transBranch.lastIndexOf("-")!=-1)
            transBranch = transBranch.substring(0,transBranch.lastIndexOf("-"));
        transBranch= transBranch.trim();
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_DELETE && transBranch.equalsIgnoreCase(lblCurrBranValue.getText())){
            ClientUtil.displayAlert("Current Branch And Transferring Brach Are Same");
            return;
        }
        else{
            
            updateOBFields();
            observable.doAction() ;
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("EMP_TRANSFER_ID");
                lockMap.put("EMP_TRANSFER_ID",CommonUtil.convertObjToStr(lblEmpTransferID.getText()));
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
                    lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                    if (observable.getProxyReturnMap()!=null) {
                        if (observable.getProxyReturnMap().containsKey("LEAVE_ID")) {
                            lockMap.put("EMP_TRANSFER_ID",observable.getProxyReturnMap().get("LEAVE_ID"));
                        }
                    }
                }
                if(observable.getResult()==ClientConstants.ACTIONTYPE_EDIT){
                    lockMap.put("EMP_TRANSFER_ID", observable.getTxtEmpTransferID());
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
    }
    private void setFieldNames() {
        cboRoleCurrBran.setName("cboRoleCurrBran");
        cboRoleTransBran.setName("cboRoleTransBran");
        cboTransBran.setName("cboTransBran");
        txtEmpID.setName("txtEmpID");
        tdtDoj.setName("tdtDoj");
        tdtLastDay.setName("tdtLastDay");
        lblDoj.setName("lblDoj");
        lblEmpId.setName("lblEmpId");
        lblLastDayCurrBran.setName("lblLastDayCurrBran");
        lblTranBran.setName("lblTranBran");
        btnEmp.setName("btnEmp");
        lblLastDayCurrBran.setName("lblLastDayCurrBran");
        lblRoleTrans.setName("lblRoleTrans");
        panEmpTransfer.setName("panEmpTransfer");
        panTranferDetails.setName("panTranferDetails");
        panReqOfficial.setName("panReqOfficial");
        rdoReq_Yes.setName("rdoReq_Yes");
        rdoOff_Yes.setName("rdoOff_Yes");
        panEmpDetails.setName("panEmpDetails");
        lblEmpTransferID.setName("lblEmpTransferID");
        lblEmpName.setName("lblEmpName");
        lblBranName.setName("lblBranName");
        lblCurrBranValue.setName("lblCurrBranValue");
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
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDateChange;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnEmp;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboRoleCurrBran;
    private com.see.truetransact.uicomponent.CComboBox cboRoleTransBran;
    private com.see.truetransact.uicomponent.CComboBox cboTransBran;
    private com.see.truetransact.uicomponent.CLabel lblBranName;
    private com.see.truetransact.uicomponent.CLabel lblCurrBranValue;
    private com.see.truetransact.uicomponent.CLabel lblDoj;
    private com.see.truetransact.uicomponent.CLabel lblEmpId;
    private com.see.truetransact.uicomponent.CLabel lblEmpName;
    private com.see.truetransact.uicomponent.CLabel lblEmpTransferID;
    private com.see.truetransact.uicomponent.CLabel lblLastDayCurrBran;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblRoleCurr;
    private com.see.truetransact.uicomponent.CLabel lblRoleTrans;
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
    private com.see.truetransact.uicomponent.CLabel lblTranBran;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panEmpDetails;
    private com.see.truetransact.uicomponent.CPanel panEmpTransfer;
    private com.see.truetransact.uicomponent.CPanel panReqOfficial;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTranferDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdoApplType;
    private com.see.truetransact.uicomponent.CRadioButton rdoOff_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoReq_Yes;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtDoj;
    private com.see.truetransact.uicomponent.CDateField tdtLastDay;
    private com.see.truetransact.uicomponent.CTextField txtEmpID;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
        EmpTransferUI empTran = new EmpTransferUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(empTran);
        j.show();
        empTran.show();
    }
}
