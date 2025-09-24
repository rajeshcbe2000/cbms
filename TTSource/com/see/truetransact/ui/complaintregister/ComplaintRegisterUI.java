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

package com.see.truetransact.ui.complaintregister;

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
import com.see.truetransact.ui.customer.CheckCustomerIdUI;




/** This form is used to manipulate CustomerIdChangeUI related functionality
 * @author swaroop
 */
public class ComplaintRegisterUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField{

    private String viewType = new String();
    private HashMap mandatoryMap;
    private ComplaintRegisterOB observable;
    final String AUTHORIZE="Authorize";
    private final static Logger log = Logger.getLogger(ComplaintRegisterUI.class);
    ComplaintRegisterRB complaintRB = new ComplaintRegisterRB();
    private String prodType="";
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    String cust_type=null;
    String txtComplaintid=null;
    private Date currDt = null;
    /** Creates new form CustomerIdChangeUI */
    public ComplaintRegisterUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUp();
    }
    
    private void initStartUp(){
        setMandatoryHashMap();
        setFieldNames();
        internationalize();
        observable = new ComplaintRegisterOB();
        observable.addObserver(this);
        initComponentData();
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
         objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.complaintregister.ComplaintRegisterMRB", ProxyParameters.LANGUAGE);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panComplains);
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
        panComplains = new com.see.truetransact.uicomponent.CPanel();
        lblDateofComplaint = new com.see.truetransact.uicomponent.CLabel();
        lblCustId = new com.see.truetransact.uicomponent.CLabel();
        lblNameAddress = new com.see.truetransact.uicomponent.CLabel();
        lblComments = new com.see.truetransact.uicomponent.CLabel();
        tdtDateofComplaint = new com.see.truetransact.uicomponent.CDateField();
        panNameAddress = new com.see.truetransact.uicomponent.CPanel();
        srpNameAddress = new com.see.truetransact.uicomponent.CScrollPane();
        txaNameAddress = new com.see.truetransact.uicomponent.CTextArea();
        panComments = new com.see.truetransact.uicomponent.CPanel();
        scrComments = new com.see.truetransact.uicomponent.CScrollPane();
        txaComments = new com.see.truetransact.uicomponent.CTextArea();
        txtCustid = new com.see.truetransact.uicomponent.CTextField();
        btnCust = new com.see.truetransact.uicomponent.CButton();
        tbrVisitorsDiary = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace62 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace63 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace64 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace65 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace66 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace67 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace68 = new com.see.truetransact.uicomponent.CLabel();
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

        panComplains.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panComplains.setMaximumSize(new java.awt.Dimension(500, 300));
        panComplains.setMinimumSize(new java.awt.Dimension(500, 300));
        panComplains.setName("panMaritalStatus");
        panComplains.setPreferredSize(new java.awt.Dimension(500, 300));
        panComplains.setEnabled(false);
        panComplains.setLayout(new java.awt.GridBagLayout());

        lblDateofComplaint.setText("Date ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panComplains.add(lblDateofComplaint, gridBagConstraints);

        lblCustId.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panComplains.add(lblCustId, gridBagConstraints);

        lblNameAddress.setText("Name & Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panComplains.add(lblNameAddress, gridBagConstraints);

        lblComments.setText("Comments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panComplains.add(lblComments, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panComplains.add(tdtDateofComplaint, gridBagConstraints);

        panNameAddress.setMaximumSize(new java.awt.Dimension(200, 80));
        panNameAddress.setMinimumSize(new java.awt.Dimension(200, 80));
        panNameAddress.setPreferredSize(new java.awt.Dimension(200, 80));
        panNameAddress.setLayout(new java.awt.GridLayout(1, 0));

        srpNameAddress.setViewportView(txaNameAddress);

        panNameAddress.add(srpNameAddress);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panComplains.add(panNameAddress, gridBagConstraints);

        panComments.setMaximumSize(new java.awt.Dimension(200, 80));
        panComments.setMinimumSize(new java.awt.Dimension(200, 80));
        panComments.setPreferredSize(new java.awt.Dimension(200, 80));
        panComments.setLayout(new java.awt.GridLayout(1, 0));

        scrComments.setViewportView(txaComments);

        panComments.add(scrComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panComplains.add(panComments, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panComplains.add(txtCustid, gridBagConstraints);

        btnCust.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCust.setToolTipText("Select Customer");
        btnCust.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCust.setMaximumSize(new java.awt.Dimension(21, 21));
        btnCust.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCust.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCust.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panComplains.add(btnCust, gridBagConstraints);

        panComplaint.add(panComplains, new java.awt.GridBagConstraints());

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

        lblSpace62.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace62.setText("     ");
        lblSpace62.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace62.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace62.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrVisitorsDiary.add(lblSpace62);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnEdit);

        lblSpace63.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace63.setText("     ");
        lblSpace63.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace63.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace63.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrVisitorsDiary.add(lblSpace63);

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

        lblSpace64.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace64.setText("     ");
        lblSpace64.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace64.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace64.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrVisitorsDiary.add(lblSpace64);

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

        lblSpace65.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace65.setText("     ");
        lblSpace65.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace65.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace65.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrVisitorsDiary.add(lblSpace65);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnException);

        lblSpace66.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace66.setText("     ");
        lblSpace66.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace66.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace66.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrVisitorsDiary.add(lblSpace66);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnReject);

        lblSpace4.setText("     ");
        tbrVisitorsDiary.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrVisitorsDiary.add(btnPrint);

        lblSpace67.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace67.setText("     ");
        lblSpace67.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace67.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace67.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrVisitorsDiary.add(lblSpace67);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnClose);

        lblSpace68.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace68.setText("     ");
        lblSpace68.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace68.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace68.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrVisitorsDiary.add(lblSpace68);

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

    private void btnCustActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustActionPerformed
        // TODO add your handling code here:
          //new CheckCustomerIdUI(this);
      //  viewType="CUST";
      popUp("CUST");
    }//GEN-LAST:event_btnCustActionPerformed

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
       
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panComplains);
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
        btnCust.setEnabled(true);
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
                    ClientUtil.enableDisable(panComplains, false);
                   
                } else {
                    ClientUtil.enableDisable(panComplains, true);
                 
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
            txtCustid.setText(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
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
        txtComplaintid=observable.getTxtComplaintid();
        tdtDateofComplaint.setDateValue(observable.getTxtDateofComplaint());
        txtCustid.setText(observable.getTxtEmployeeId());
         txaNameAddress.setText(observable.getTxaNameAddress());
         txaComments.setText(observable.getTxaComments());
         lblStatus.setText(observable.getLblStatus());
       
    }
    
    public void updateOBFields() {
        
        observable.setTxtDateofComplaint(tdtDateofComplaint.getDateValue());
        observable.setTxtEmployeeId(txtCustid.getText());
        observable.setTxaNameAddress(txaNameAddress.getText());
        observable.setTxaComments(txaComments.getText());
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
        tdtDateofComplaint.setName("tdtDateofComplaint");
        txaNameAddress.setName("txaNameAddress");
        txtCustid.setName("txtCustid");
        txaComments.setName("txaComments");
        lblDateofComplaint.setName("lblDateofComplaint");
        lblNameAddress.setName("lblNameAddress");
        lblCustId.setName("lblCustId");
        lblComments.setName("lblComments");
        panComplaint.setName("panComplaint");
        panComplains.setName("panComplains");
       
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
    private com.see.truetransact.uicomponent.CButton btnCust;
    private com.see.truetransact.uicomponent.CButton btnDateChange;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel lblComments;
    private com.see.truetransact.uicomponent.CLabel lblCustId;
    private com.see.truetransact.uicomponent.CLabel lblDateofComplaint;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNameAddress;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace62;
    private com.see.truetransact.uicomponent.CLabel lblSpace63;
    private com.see.truetransact.uicomponent.CLabel lblSpace64;
    private com.see.truetransact.uicomponent.CLabel lblSpace65;
    private com.see.truetransact.uicomponent.CLabel lblSpace66;
    private com.see.truetransact.uicomponent.CLabel lblSpace67;
    private com.see.truetransact.uicomponent.CLabel lblSpace68;
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
    private com.see.truetransact.uicomponent.CPanel panComments;
    private com.see.truetransact.uicomponent.CPanel panComplains;
    private com.see.truetransact.uicomponent.CPanel panComplaint;
    private com.see.truetransact.uicomponent.CPanel panNameAddress;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CScrollPane scrComments;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpNameAddress;
    private javax.swing.JToolBar tbrVisitorsDiary;
    private com.see.truetransact.uicomponent.CDateField tdtDateofComplaint;
    private com.see.truetransact.uicomponent.CTextArea txaComments;
    private com.see.truetransact.uicomponent.CTextArea txaNameAddress;
    private com.see.truetransact.uicomponent.CTextField txtCustid;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
        ComplaintRegisterUI complaints = new ComplaintRegisterUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(complaints);
        j.show();
        complaints.show();
    }
}
