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

package com.see.truetransact.ui.directoryboardsetting;


import com.see.truetransact.clientexception.ClientParseException;
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
import com.see.truetransact.ui.common.customer.CustDetailsUI;
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
public class DirectoryBoardSettingUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField{

     private String viewType = new String();
     private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap mandatoryMap;
    private DirectoryBoardSettingOB observable;
   // DirectorBoardOB ob;
    final String AUTHORIZE="Authorize";
    private final static Logger log = Logger.getLogger(DirectoryBoardSettingUI.class);
    DirectoryBoardSettingRB directoryboardRB = new DirectoryBoardSettingRB();
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    String txtid=null;
    private CustDetailsUI custDetailsUI = null;
        
    /** Creates new form CustomerIdChangeUI */
    public DirectoryBoardSettingUI() {
        initComponents();
        initStartUp();
        custDetailsUI =  new CustDetailsUI(panAcctDetails);
    }
    
    private void initStartUp(){
      setMandatoryHashMap();
        setFieldNames();
        observable = new DirectoryBoardSettingOB();
        observable.addObserver(this);
        initComponentData();
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
         objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.directoryboardsetting.DirectoryBoardSettingMRB", ProxyParameters.LANGUAGE);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panTranferDetails);
    }
    
    private void setMaxLength() {
     
    txtPriority.setValidation(new NumericValidation());
   
    txtPriority.setAllowAll(true);
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtMemno", new Boolean(true));
        mandatoryMap.put("txtDesig", new Boolean(true));
        mandatoryMap.put("txtPriority", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
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
//    public void setHelpMessage() {
//    }
    
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
        rdoApplType1 = new com.see.truetransact.uicomponent.CButtonGroup();
        panEmpTransfer = new com.see.truetransact.uicomponent.CPanel();
        panTranferDetails = new com.see.truetransact.uicomponent.CPanel();
        lblMemno = new com.see.truetransact.uicomponent.CLabel();
        lblPriority = new javax.swing.JLabel();
        lblDesig = new com.see.truetransact.uicomponent.CLabel();
        txtDesig = new com.see.truetransact.uicomponent.CTextField();
        txtPriority = new com.see.truetransact.uicomponent.CTextField();
        txtMemno = new com.see.truetransact.uicomponent.CTextField();
        btnMembershipNo = new com.see.truetransact.uicomponent.CButton();
        panAcctDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSelDate = new com.see.truetransact.uicomponent.CLabel();
        tdtSelDate = new com.see.truetransact.uicomponent.CDateField();
        lblAccHead = new com.see.truetransact.uicomponent.CLabel();
        btnAccHead = new com.see.truetransact.uicomponent.CButton();
        txtAccHead = new com.see.truetransact.uicomponent.CTextField();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        txtAcctNo = new com.see.truetransact.uicomponent.CTextField();
        btnActSelect = new com.see.truetransact.uicomponent.CButton();
        txtProdId = new com.see.truetransact.uicomponent.CTextField();
        btnProdIdSelect = new com.see.truetransact.uicomponent.CButton();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace62 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace63 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
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
        setMaximumSize(new java.awt.Dimension(800, 625));
        setMinimumSize(new java.awt.Dimension(800, 625));
        setPreferredSize(new java.awt.Dimension(800, 625));

        panEmpTransfer.setMaximumSize(new java.awt.Dimension(650, 520));
        panEmpTransfer.setMinimumSize(new java.awt.Dimension(650, 520));
        panEmpTransfer.setPreferredSize(new java.awt.Dimension(650, 520));
        panEmpTransfer.setLayout(new java.awt.GridBagLayout());

        panTranferDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTranferDetails.setMinimumSize(new java.awt.Dimension(750, 400));
        panTranferDetails.setName("panMaritalStatus"); // NOI18N
        panTranferDetails.setPreferredSize(new java.awt.Dimension(750, 400));
        panTranferDetails.setLayout(new java.awt.GridBagLayout());

        lblMemno.setText("Membership no:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = -6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 6, 0, 0);
        panTranferDetails.add(lblMemno, gridBagConstraints);

        lblPriority.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblPriority.setText("Priority");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 55, 0, 0);
        panTranferDetails.add(lblPriority, gridBagConstraints);

        lblDesig.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 25, 0, 0);
        panTranferDetails.add(lblDesig, gridBagConstraints);

        txtDesig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDesigActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 95;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 2, 0, 0);
        panTranferDetails.add(txtDesig, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 95;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 0, 0);
        panTranferDetails.add(txtPriority, gridBagConstraints);

        txtMemno.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMemnoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 95;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 2, 0, 0);
        panTranferDetails.add(txtMemno, gridBagConstraints);

        btnMembershipNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMembershipNo.setEnabled(false);
        btnMembershipNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMembershipNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMembershipNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 1, 0, 0);
        panTranferDetails.add(btnMembershipNo, gridBagConstraints);

        panAcctDetails.setMaximumSize(new java.awt.Dimension(250, 200));
        panAcctDetails.setMinimumSize(new java.awt.Dimension(250, 200));
        panAcctDetails.setPreferredSize(new java.awt.Dimension(250, 200));
        panAcctDetails.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 16;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 18, 0, 198);
        panTranferDetails.add(panAcctDetails, gridBagConstraints);

        lblSelDate.setText("Selection Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 8, 0, 0);
        panTranferDetails.add(lblSelDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 2, 0, 0);
        panTranferDetails.add(tdtSelDate, gridBagConstraints);

        lblAccHead.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 14, 0, 0);
        panTranferDetails.add(lblAccHead, gridBagConstraints);

        btnAccHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccHead.setToolTipText("Search");
        btnAccHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAccHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAccHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 1, 0, 0);
        panTranferDetails.add(btnAccHead, gridBagConstraints);

        txtAccHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 0, 0, 0);
        panTranferDetails.add(txtAccHead, gridBagConstraints);

        cLabel1.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(68, 23, 0, 0);
        panTranferDetails.add(cLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 99;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(66, 0, 0, 0);
        panTranferDetails.add(cboProdType, gridBagConstraints);

        cLabel2.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 23, 0, 0);
        panTranferDetails.add(cLabel2, gridBagConstraints);

        cLabel3.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 23, 0, 0);
        panTranferDetails.add(cLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 178;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 0, 0, 0);
        panTranferDetails.add(txtAcctNo, gridBagConstraints);

        btnActSelect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnActSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActSelectActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 16;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = -23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 6, 25, 0);
        panTranferDetails.add(btnActSelect, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 4, 0, 0);
        panTranferDetails.add(txtProdId, gridBagConstraints);

        btnProdIdSelect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnProdIdSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdIdSelectActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = -20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 6, 0, 0);
        panTranferDetails.add(btnProdIdSelect, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(42, 32, 43, 32);
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

        lblSpace62.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace62.setText("     ");
        lblSpace62.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace62.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace62.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace62);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

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

        lblSpace63.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace63.setText("     ");
        lblSpace63.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace63.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace63.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace63);

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

    private void txtMemnoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMemnoFocusLost
        // TODO add your handling code here:
        
        if (txtMemno.getText().equals("")) {
            custDetailsUI.updateCustomerInfo("");
        }
        else
        {
            HashMap memberDetMap=new HashMap();
            memberDetMap.put("MEMBER_NO",txtMemno.getText());
            List list=ClientUtil.executeQuery("getMemberName", memberDetMap);
            if(list!=null)
            {
                HashMap map = new HashMap();
                map = (HashMap) list.get(0);
                String CUST_ID = CommonUtil.convertObjToStr(map.get("CUST_ID"));
                updateCustomerInfo(CUST_ID);
            }
            else {
                custDetailsUI.updateCustomerInfo("");
            }
        }
         
    }//GEN-LAST:event_txtMemnoFocusLost

    private void txtDesigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDesigActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDesigActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        btnView.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
    //    popUp("Enquiry");
    }//GEN-LAST:event_btnViewActionPerformed
                            
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mitCloseActionPerformed(evt);
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
        cifClosingAlert();
    }
        
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
    observable.setStatus();
    System.out.println("in edit btn");
     popUp("Edit");
    lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
//        deletescreenLock();
        viewType = "CANCEL";
        observable.resetForm();
        ClientUtil.clearAll(this);
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        lblStatus.setText("");
        custDetailsUI.updateCustomerInfo("");
        btnMembershipNo.setEnabled(false);
        tdtSelDate.setDateValue(null);
        txtAccHead.setText("");
//        lblBranName.setText("");
//        lblEmpName.setText("");
        setModified(false);
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
         updateOBFields();
        System.out.println("in save");
  
        StringBuffer str  =  new StringBuffer();
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panTranferDetails);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
           displayAlert(mandatoryMessage);
            System.out.println("in save1");
                   }
        else{
            System.out.println("in save2");
            savePerformed();
            custDetailsUI.updateCustomerInfo("");
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void updateCustomerInfo(String cust_id){
        if(cust_id != null && !cust_id.equals(""))
           custDetailsUI.updateCustomerInfo(cust_id) ;
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        enableDisable(true);
        setButtonEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        btnMembershipNo.setEnabled(true);
        
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
//        btnDeleteActionPerformed(evt);
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

    private void btnMembershipNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMembershipNoActionPerformed
        // TODO add your handling code here:
        popUp("MEMBER_NO");
    }//GEN-LAST:event_btnMembershipNoActionPerformed

    private void btnAccHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccHeadActionPerformed
        // Add your handling code here:
        popUp("MISCSERVCHARGES");
    }//GEN-LAST:event_btnAccHeadActionPerformed

    private void txtAccHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccHeadFocusLost
        // TODO add your handling code here:
        if (!(txtAccHead.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtAccHead, "TermLoan.getSelectAcctHeadTOList");
           // btnAccHead.setToolTipText(getAccHdDesc(txtAccHead.getText()));
        }
    }//GEN-LAST:event_txtAccHeadFocusLost

    private void btnProdIdSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdIdSelectActionPerformed
        // TODO add your handling code here:
         popUp("SELECT_PROD_ID");
    }//GEN-LAST:event_btnProdIdSelectActionPerformed

    private void btnActSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActSelectActionPerformed
        // TODO add your handling code here:
         popUp("SELECT_ACT_NO");
    }//GEN-LAST:event_btnActSelectActionPerformed
            
    /** To populate Comboboxes */
    private void initComponentData() {        
         try {           
            cboProdType.setModel(observable.getCbmProdType());           
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    /** To display a popUp window for viewing existing data */
    private void popUp(String currAction){
       viewType = currAction;
       HashMap whereMap = new HashMap();
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")){
            System.out.println("in edit popup");
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getDirectoryBoardEdit"); 
        }else if(currAction.equalsIgnoreCase("Delete")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getDirectoryBoardEdit");
        } if (currAction.equalsIgnoreCase("MEMBER_NO")) {
            viewMap.put(CommonConstants.MAP_NAME, "getAllMemberDetails");
            viewMap.put(CommonConstants.MAP_WHERE, new HashMap());
        }if (currAction.equalsIgnoreCase("MISCSERVCHARGES")) {
             updateOBFields();
            viewMap.put(CommonConstants.MAP_NAME, "TermLoan.getSelectAcctHeadTOList");
        }
        if (currAction.equalsIgnoreCase("SELECT_PROD_ID")) {
            updateOBFields();
            HashMap where_map = new HashMap();
            where_map.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            String prodType = CommonUtil.convertObjToStr((((ComboBoxModel) (cboProdType).getModel())).getKeyForSelected());
            viewMap.put(CommonConstants.MAP_NAME, "InterMaintenance.getProductData" + CommonUtil.convertObjToStr(observable.getCbmProdType().getKeyForSelected()));
            viewMap.put(CommonConstants.MAP_WHERE, where_map);
        }
        if (currAction.equalsIgnoreCase("SELECT_ACT_NO")) {
            updateOBFields();
            whereMap.put("PROD_ID", txtProdId.getText());
            //added by rishad 23/12/2019 for adding internranch account
            if (TrueTransactMain.selBranch != null) {
                whereMap.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
            } else {
                whereMap.put("SELECTED_BRANCH", TrueTransactMain.BRANCH_ID);
            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                    + CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected()));

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
               hash.put(CommonConstants.MAP_WHERE, hash.get("Directory_ID"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                observable.getData(hash);
                txtMemnoFocusLost(null);
               if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
               viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                    ClientUtil.enableDisable(panTranferDetails, false);
                 
                } else {
                   ClientUtil.enableDisable(panTranferDetails, true);
                 
               }
              setButtonEnableDisable();
//               
           }
           if(viewType.equalsIgnoreCase("MEMBER_NO")){
               if(hash!=null && hash.size()>0){
               txtMemno.setText(CommonUtil.convertObjToStr(hash.get("SHARE_ACCT_NO")));
               String CUST_ID = CommonUtil.convertObjToStr(hash.get("CUST_ID"));
                updateCustomerInfo(CUST_ID);
               }
           }
           if (viewType.equalsIgnoreCase("MISCSERVCHARGES")) {
               if(hash!=null && hash.size()>0){
                   txtAccHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
               } 
           } 
           if (viewType == "SELECT_ACT_NO") {//nithya
               if (CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected()).equals("GL")) {
                   txtAcctNo.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
               } else {
                   txtAcctNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
               }               
           }
           if (viewType == "SELECT_PROD_ID") {//nithya
               txtProdId.setText(CommonUtil.convertObjToStr(hash.get("PROD_ID")));               
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
//       btnDelete.setEnabled(!btnDelete.isEnabled());
       mitNew.setEnabled(btnNew.isEnabled());
       mitEdit.setEnabled(btnEdit.isEnabled());
//       mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(!btnNew.isEnabled());
       btnCancel.setEnabled(!btnNew.isEnabled());
       mitSave.setEnabled(btnSave.isEnabled());
       mitCancel.setEnabled(btnCancel.isEnabled());
        
//        btnAuthorize.setEnabled(btnNew.isEnabled());
//      btnReject.setEnabled(btnNew.isEnabled());
//       btnException.setEnabled(btnNew.isEnabled());
       btnView.setEnabled(!btnView.isEnabled());
   }
    
   public void update(Observable observed, Object arg) {
            txtid=(observable.getTxtid());
           
            txtMemno.setText(observable.getTxtMemno());
            txtDesig.setText(observable.getTxtDesig());
            txtPriority.setText(observable.getTxtPriority());
            lblStatus.setText(observable.getLblStatus());
            txtAccHead.setText(observable.getTxtAccHead());
            tdtSelDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtSelDate()));
            // Added by nithya on 17-03-2017            
            cboProdType.setSelectedItem((observable.getCbmProdType()).getDataForKey(observable.getCboProdType()));
            txtProdId.setText(observable.getTxtProdId());
            txtAcctNo.setText(observable.getTxtActNo());
    }
    
    public void updateOBFields() {
        
            observable.setTxtMemno(txtMemno.getText());
            observable.setTxtDesig(txtDesig.getText());
            observable.setTxtPriority(txtPriority.getText());
            observable.setTxtid(txtid);
            observable.setTdtSelDate(DateUtil.getDateMMDDYYYY(tdtSelDate.getDateValue()));
            observable.setTxtAccHead(txtAccHead.getText());
            // Added by nithya on 17-03-2017
            observable.setCboProdType(CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected()));
            observable.setTxtProdId(txtProdId.getText()); 
            observable.setTxtActNo(txtAcctNo.getText());
            
    }
    
   

    private void savePerformed(){
        updateOBFields();
        observable.doAction();
        if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
               
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("DIRECTOR_ID")) {
                      
                    }
                }
            }
            if(observable.getResult()==ClientConstants.ACTIONTYPE_EDIT) {
                // lockMap.put("EMP_TRANSFER_ID", observable.getTxtEmpTransferID());
            }
            // setEditLockMap(lockMap);
            ////  setEditLock();
            //  deletescreenLock();
        }
        
        observable.resetForm();
        //  observable.resetFormcpy();
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
//            System.out.println("save aaa");
//            updateOBFields();
//            observable.doAction() ;
//            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
//                HashMap lockMap = new HashMap();
//                ArrayList lst = new ArrayList();
////                lst.add("EMP_TRANSFER_ID");
////                lockMap.put("EMP_TRANSFER_ID",CommonUtil.convertObjToStr(lblEmpTransferID.getText()));
////                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
//                if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
//                    lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
//                    if (observable.getProxyReturnMap()!=null) {
////                        if (observable.getProxyReturnMap().containsKey("LEAVE_ID")) {
////                            //lockMap.put("EMP_TRANSFER_ID",observable.getProxyReturnMap().get("LEAVE_ID"));
////                        }
//                    }
//                }
////                if(observable.getResult()==ClientConstants.ACTIONTYPE_EDIT){
////                    lockMap.put("EMP_TRANSFER_ID", observable.getTxtEmpTransferID());
////                }
////                setEditLockMap(lockMap);
////                setEditLock();
////                deletescreenLock();
//            }
//            
//            observable.resetForm();
//  //          enableDisable(false);
////            setButtonEnableDisable();
//            lblStatus.setText(observable.getLblStatus());
//            ClientUtil.enableDisable(this, false);
//            observable.setResultStatus();
//            lblStatus.setText(observable.getLblStatus());
//            //__ Make the Screen Closable..
//            setModified(false);
//            ClientUtil.clearAll(this);
//            observable.ttNotifyObservers();
        }
    
   private void setFieldNames() {
        txtMemno.setName("txtMemno");
        txtDesig.setName("txtDesig");
        txtPriority.setName("txtPriority");
      
    }
    
   
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    
    private void deletescreenLock(){
        HashMap map=new HashMap();
        map.put("USER_ID",ProxyParameters.USER_ID);
        map.put("TRANS_DT", ClientUtil.getCurrentDate());
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
    private com.see.truetransact.uicomponent.CButton btnAccHead;
    private com.see.truetransact.uicomponent.CButton btnActSelect;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnMembershipNo;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnProdIdSelect;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CLabel lblAccHead;
    private com.see.truetransact.uicomponent.CLabel lblDesig;
    private com.see.truetransact.uicomponent.CLabel lblMemno;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private javax.swing.JLabel lblPriority;
    private com.see.truetransact.uicomponent.CLabel lblSelDate;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace62;
    private com.see.truetransact.uicomponent.CLabel lblSpace63;
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
    private com.see.truetransact.uicomponent.CPanel panAcctDetails;
    private com.see.truetransact.uicomponent.CPanel panEmpTransfer;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTranferDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdoApplType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoApplType1;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtSelDate;
    private com.see.truetransact.uicomponent.CTextField txtAccHead;
    private com.see.truetransact.uicomponent.CTextField txtAcctNo;
    private com.see.truetransact.uicomponent.CTextField txtDesig;
    private com.see.truetransact.uicomponent.CTextField txtMemno;
    private com.see.truetransact.uicomponent.CTextField txtPriority;
    private com.see.truetransact.uicomponent.CTextField txtProdId;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
        DirectoryBoardSettingUI dirBrd = new DirectoryBoardSettingUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(dirBrd);
        j.show();
        dirBrd.show();
//        empTran.show();
    

}
}