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

package com.see.truetransact.ui.filemanagement;

import com.see.truetransact.ui.inwardregister.*;
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
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
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
public class FileManagementUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField{

    private String viewType = new String();
    private HashMap mandatoryMap;
    private FileManagementOB observable;
    final String AUTHORIZE="Authorize";
    private final static Logger log = Logger.getLogger(FileManagementUI.class);
    InwardRB InwardRB = new InwardRB();
    private String prodType="";
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    String cust_type=null;
    private Date currDt = null;
    /** Creates new form CustomerIdChangeUI */
    public FileManagementUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUp();
    }
    
    private void initStartUp(){
        setMandatoryHashMap();
        setFieldNames();
        internationalize();
        observable = new FileManagementOB();
        observable.addObserver(this);
        initComponentData();
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
         objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.inwardregister.InwardMRB", ProxyParameters.LANGUAGE);
//        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panInward);
         setHelpMessage();
         tdtDate.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
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
        mandatoryMap.put("txtInwardNo", new Boolean(true));
        mandatoryMap.put("txaDetails", new Boolean(true));
        mandatoryMap.put("tdtDate", new Boolean(true));
        mandatoryMap.put("txaRemarks", new Boolean(true));
        mandatoryMap.put("txtSubmittedBy", new Boolean(true));
        mandatoryMap.put("txtReferenceNo", new Boolean(true));
        mandatoryMap.put("txtActionTaken", new Boolean(true));
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

        panInwardRegister = new com.see.truetransact.uicomponent.CPanel();
        panInward = new com.see.truetransact.uicomponent.CPanel();
        lblInward = new com.see.truetransact.uicomponent.CLabel();
        lblSubmittedBy = new com.see.truetransact.uicomponent.CLabel();
        lblDetails = new com.see.truetransact.uicomponent.CLabel();
        lblParticulars = new com.see.truetransact.uicomponent.CLabel();
        tdtDate = new com.see.truetransact.uicomponent.CDateField();
        txtSubmittedBy = new javax.swing.JTextField();
        lblReferenceNo = new javax.swing.JLabel();
        lblFileNo = new javax.swing.JLabel();
        txtFileNo = new javax.swing.JTextField();
        txtApplicationNo = new javax.swing.JTextField();
        lblMemberNo = new com.see.truetransact.uicomponent.CLabel();
        txtmemberNo = new com.see.truetransact.uicomponent.CTextField();
        srpDetails = new com.see.truetransact.uicomponent.CScrollPane();
        txaAddress = new com.see.truetransact.uicomponent.CTextArea();
        scrRemarks = new com.see.truetransact.uicomponent.CScrollPane();
        txaParticulars = new com.see.truetransact.uicomponent.CTextArea();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        scrRemarks1 = new com.see.truetransact.uicomponent.CScrollPane();
        txaRemarks = new com.see.truetransact.uicomponent.CTextArea();
        cButton1 = new com.see.truetransact.uicomponent.CButton();
        panSubmissionDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSubmittedTo = new com.see.truetransact.uicomponent.CLabel();
        cboSubmittedTo = new com.see.truetransact.uicomponent.CComboBox();
        tdtSubmissionAction1 = new com.see.truetransact.uicomponent.CDateField();
        scrRemarks5 = new com.see.truetransact.uicomponent.CScrollPane();
        txaSubmissionAction1 = new com.see.truetransact.uicomponent.CTextArea();
        scrRemarks6 = new com.see.truetransact.uicomponent.CScrollPane();
        txaSubmissionAction2 = new com.see.truetransact.uicomponent.CTextArea();
        scrRemarks7 = new com.see.truetransact.uicomponent.CScrollPane();
        txaSubmissionAction3 = new com.see.truetransact.uicomponent.CTextArea();
        tdtSubmissionAction2 = new com.see.truetransact.uicomponent.CDateField();
        tdtSubmissionAction3 = new com.see.truetransact.uicomponent.CDateField();
        panApprovalDetails = new com.see.truetransact.uicomponent.CPanel();
        lblApprovedBy = new com.see.truetransact.uicomponent.CLabel();
        cboApprovedBy = new com.see.truetransact.uicomponent.CComboBox();
        tdtAprovalAction1 = new com.see.truetransact.uicomponent.CDateField();
        scrRemarks8 = new com.see.truetransact.uicomponent.CScrollPane();
        txaAproveAction1 = new com.see.truetransact.uicomponent.CTextArea();
        scrRemarks9 = new com.see.truetransact.uicomponent.CScrollPane();
        txaAproveAction2 = new com.see.truetransact.uicomponent.CTextArea();
        scrRemarks10 = new com.see.truetransact.uicomponent.CScrollPane();
        txaAproveAction3 = new com.see.truetransact.uicomponent.CTextArea();
        tdtAprovalAction2 = new com.see.truetransact.uicomponent.CDateField();
        tdtAprovalAction3 = new com.see.truetransact.uicomponent.CDateField();
        tbrInwardRegister = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace76 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace77 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace78 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace79 = new com.see.truetransact.uicomponent.CLabel();
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

        panInwardRegister.setMaximumSize(new java.awt.Dimension(650, 520));
        panInwardRegister.setMinimumSize(new java.awt.Dimension(650, 520));
        panInwardRegister.setPreferredSize(new java.awt.Dimension(650, 520));

        panInward.setBorder(javax.swing.BorderFactory.createTitledBorder("General Details"));
        panInward.setEnabled(false);
        panInward.setMinimumSize(new java.awt.Dimension(600, 400));
        panInward.setName("panMaritalStatus"); // NOI18N
        panInward.setPreferredSize(new java.awt.Dimension(600, 400));
        panInward.setLayout(new java.awt.GridBagLayout());

        lblInward.setText("Date ");
        lblInward.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 155, 0, 0);
        panInward.add(lblInward, gridBagConstraints);

        lblSubmittedBy.setText("Submitted By");
        lblSubmittedBy.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 155, 0, 0);
        panInward.add(lblSubmittedBy, gridBagConstraints);

        lblDetails.setText("Address");
        lblDetails.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 155, 0, 0);
        panInward.add(lblDetails, gridBagConstraints);

        lblParticulars.setText("Particulars");
        lblParticulars.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 155, 0, 0);
        panInward.add(lblParticulars, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 18, 0, 0);
        panInward.add(tdtDate, gridBagConstraints);

        txtSubmittedBy.setMaximumSize(new java.awt.Dimension(101, 21));
        txtSubmittedBy.setMinimumSize(new java.awt.Dimension(101, 21));
        txtSubmittedBy.setPreferredSize(new java.awt.Dimension(101, 21));
        txtSubmittedBy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSubmittedByActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 109;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 18, 0, 0);
        panInward.add(txtSubmittedBy, gridBagConstraints);

        lblReferenceNo.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        lblReferenceNo.setText("Application No. ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 155, 0, 0);
        panInward.add(lblReferenceNo, gridBagConstraints);

        lblFileNo.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        lblFileNo.setText("File No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 18, 0, 0);
        panInward.add(lblFileNo, gridBagConstraints);

        txtFileNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtFileNo.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtFileNo.setMaximumSize(new java.awt.Dimension(101, 21));
        txtFileNo.setMinimumSize(new java.awt.Dimension(101, 21));
        txtFileNo.setPreferredSize(new java.awt.Dimension(101, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 201);
        panInward.add(txtFileNo, gridBagConstraints);

        txtApplicationNo.setMinimumSize(new java.awt.Dimension(101, 21));
        txtApplicationNo.setPreferredSize(new java.awt.Dimension(101, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 18, 0, 0);
        panInward.add(txtApplicationNo, gridBagConstraints);

        lblMemberNo.setText("Member Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 155, 0, 0);
        panInward.add(lblMemberNo, gridBagConstraints);

        txtmemberNo.setAllowAll(true);
        txtmemberNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtmemberNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 18, 0, 0);
        panInward.add(txtmemberNo, gridBagConstraints);

        srpDetails.setViewportView(txaAddress);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 187;
        gridBagConstraints.ipady = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 18, 0, 0);
        panInward.add(srpDetails, gridBagConstraints);

        scrRemarks.setViewportView(txaParticulars);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = 184;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 18, 0, 0);
        panInward.add(scrRemarks, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 155, 0, 0);
        panInward.add(lblRemarks, gridBagConstraints);

        scrRemarks1.setViewportView(txaRemarks);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 184;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 18, 10, 0);
        panInward.add(scrRemarks1, gridBagConstraints);

        cButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        cButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = -19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 5, 0, 0);
        panInward.add(cButton1, gridBagConstraints);

        panSubmissionDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Submission Details"));

        lblSubmittedTo.setText("Submitted To");

        scrRemarks5.setViewportView(txaSubmissionAction1);

        scrRemarks6.setViewportView(txaSubmissionAction2);

        scrRemarks7.setViewportView(txaSubmissionAction3);

        javax.swing.GroupLayout panSubmissionDetailsLayout = new javax.swing.GroupLayout(panSubmissionDetails);
        panSubmissionDetails.setLayout(panSubmissionDetailsLayout);
        panSubmissionDetailsLayout.setHorizontalGroup(
            panSubmissionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSubmissionDetailsLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(lblSubmittedTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cboSubmittedTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panSubmissionDetailsLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(tdtSubmissionAction1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(scrRemarks5, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panSubmissionDetailsLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(tdtSubmissionAction2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(scrRemarks6, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panSubmissionDetailsLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(tdtSubmissionAction3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(scrRemarks7, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panSubmissionDetailsLayout.setVerticalGroup(
            panSubmissionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSubmissionDetailsLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(panSubmissionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboSubmittedTo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSubmittedTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(panSubmissionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panSubmissionDetailsLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(tdtSubmissionAction1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scrRemarks5, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(panSubmissionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panSubmissionDetailsLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(tdtSubmissionAction2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scrRemarks6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(panSubmissionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panSubmissionDetailsLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(tdtSubmissionAction3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scrRemarks7, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panApprovalDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Approval Details"));

        lblApprovedBy.setText("Approval Stats");

        scrRemarks8.setViewportView(txaAproveAction1);

        scrRemarks9.setViewportView(txaAproveAction2);

        scrRemarks10.setViewportView(txaAproveAction3);

        javax.swing.GroupLayout panApprovalDetailsLayout = new javax.swing.GroupLayout(panApprovalDetails);
        panApprovalDetails.setLayout(panApprovalDetailsLayout);
        panApprovalDetailsLayout.setHorizontalGroup(
            panApprovalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panApprovalDetailsLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(panApprovalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panApprovalDetailsLayout.createSequentialGroup()
                        .addComponent(tdtAprovalAction2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(scrRemarks9, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panApprovalDetailsLayout.createSequentialGroup()
                        .addComponent(tdtAprovalAction3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(scrRemarks10, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panApprovalDetailsLayout.createSequentialGroup()
                        .addComponent(lblApprovedBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(cboApprovedBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panApprovalDetailsLayout.createSequentialGroup()
                        .addComponent(tdtAprovalAction1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(scrRemarks8, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        panApprovalDetailsLayout.setVerticalGroup(
            panApprovalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panApprovalDetailsLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(panApprovalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboApprovedBy, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblApprovedBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(panApprovalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panApprovalDetailsLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(tdtAprovalAction1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scrRemarks8, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(panApprovalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panApprovalDetailsLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(tdtAprovalAction2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scrRemarks9, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(panApprovalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panApprovalDetailsLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(tdtAprovalAction3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scrRemarks10, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panInwardRegisterLayout = new javax.swing.GroupLayout(panInwardRegister);
        panInwardRegister.setLayout(panInwardRegisterLayout);
        panInwardRegisterLayout.setHorizontalGroup(
            panInwardRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panInwardRegisterLayout.createSequentialGroup()
                .addGroup(panInwardRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInwardRegisterLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(panInward, javax.swing.GroupLayout.PREFERRED_SIZE, 742, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panInwardRegisterLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(panSubmissionDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panApprovalDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        panInwardRegisterLayout.setVerticalGroup(
            panInwardRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panInwardRegisterLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(panInward, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(panInwardRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(panSubmissionDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panApprovalDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        getContentPane().add(panInwardRegister, java.awt.BorderLayout.CENTER);

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
        tbrInwardRegister.add(btnView);

        lblSpace5.setText("     ");
        tbrInwardRegister.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnNew);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInwardRegister.add(lblSpace73);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnEdit);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInwardRegister.add(lblSpace74);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnDelete);

        lblSpace2.setText("     ");
        tbrInwardRegister.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnSave);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInwardRegister.add(lblSpace75);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnCancel);

        lblSpace3.setText("     ");
        tbrInwardRegister.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnAuthorize);

        lblSpace76.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace76.setText("     ");
        lblSpace76.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInwardRegister.add(lblSpace76);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnException);

        lblSpace77.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace77.setText("     ");
        lblSpace77.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInwardRegister.add(lblSpace77);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnReject);

        lblSpace4.setText("     ");
        tbrInwardRegister.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrInwardRegister.add(btnPrint);

        lblSpace78.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace78.setText("     ");
        lblSpace78.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace78.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace78.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInwardRegister.add(lblSpace78);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnClose);

        lblSpace79.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace79.setText("     ");
        lblSpace79.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace79.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace79.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInwardRegister.add(lblSpace79);

        btnDateChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/denomination.gif"))); // NOI18N
        btnDateChange.setToolTipText("Exception");
        btnDateChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateChangeActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnDateChange);

        getContentPane().add(tbrInwardRegister, java.awt.BorderLayout.NORTH);

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

    private void txtSubmittedByActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSubmittedByActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSubmittedByActionPerformed

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
       observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        observable.setStatus();
        popUp("Delete");
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
   observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
    observable.setStatus();
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
        setModified(false);
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        updateOBFields();
        savePerformed();
        
//        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panInward);
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
       // btnEmp.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        HashMap idMap = generateID();
        txtFileNo.setText((String) idMap.get(CommonConstants.DATA));     
        tdtDate.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
        
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

    private void cButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cButton1ActionPerformed
        // TODO add your handling code here:
        new CheckCustomerIdUI(this);
    }//GEN-LAST:event_cButton1ActionPerformed

    private void txtmemberNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtmemberNoFocusLost
        // TODO add your handling code here:
        HashMap whereMap = new HashMap();
        if (CommonUtil.convertObjToStr(txtmemberNo.getText()).length() > 0) {
            String shareNo = "";
            whereMap.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(txtmemberNo.getText()));
            whereMap.put("SELECTED_BRANCH_ID", TrueTransactMain.BRANCH_ID);
            List list = ClientUtil.executeQuery("viewAllShareAcct", whereMap);
            if (list != null && list.size() > 0) {
                whereMap = (HashMap) list.get(0);
                if (whereMap.containsKey("CUSTOMER") && whereMap.get("CUSTOMER") != null) {
                    String customerName = CommonUtil.convertObjToStr(whereMap.get("CUSTOMER"));
                    txtSubmittedBy.setText(customerName);
                    String custId = CommonUtil.convertObjToStr(whereMap.get("CUST_ID"));
                    HashMap customerMap = new HashMap();
                    customerMap.put("CUST_ID", custId);
                    customerMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                    List custListData = ClientUtil.executeQuery("getSelectAccInfoDisplay", customerMap);
                    if (custListData != null && custListData.size() > 0) {
                        customerMap = (HashMap) custListData.get(0);
                        String address = CommonUtil.convertObjToStr(customerMap.get("STREET")) + "\n" + CommonUtil.convertObjToStr(customerMap.get("AREA"));
                        txaAddress.setText(address);
                    }
                }
            }
        }


        
    }//GEN-LAST:event_txtmemberNoFocusLost
                
    /** To populate Comboboxes */
    private void initComponentData() {
//        cboRoleCurrBran.setModel(observable.getCbmRoleInCurrBran());
//        cboRoleTransBran.setModel(observable.getCbmRoleInTranBran());
//        cboTransBran.setModel(observable.getCbmTransferBran());
        cboSubmittedTo.setModel(observable.getCbmSubmittedTo());
        cboApprovedBy.setModel(observable.getCbmapprovedBy());
    }
    
    /** To display a popUp window for viewing existing data */
    private void popUp(String currAction){
       viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            map.put("FILTERED_LIST", "");
            map.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
           // map.put("BRANCH_CODE","0001");
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getFileManagementEdit"); 
        }else if(currAction.equalsIgnoreCase("Delete")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            map.put("FILTERED_LIST", "");
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getFileManagementEdit");
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
                System.out.println("@@@@@@@@@@@+hash"+hash.get("BOARD_MT_ID"));
                hash.put(CommonConstants.MAP_WHERE, hash.get("BOARD_MT_ID"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                observable.getData(hash);
                if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
                viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                    ClientUtil.enableDisable(panInward, false);
                    ClientUtil.enableDisable(panSubmissionDetails, false);
                    ClientUtil.enableDisable(panApprovalDetails, false);
                   
                } else {
                    ClientUtil.enableDisable(panInward, true);
                    ClientUtil.enableDisable(panSubmissionDetails, true);
                    ClientUtil.enableDisable(panApprovalDetails, true);                 
                }
                setButtonEnableDisable();
                if(viewType ==  AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
            }
            
        }
//        if (viewType.equals("EMP")){
//            txtEmpID.setText(CommonUtil.convertObjToStr(hash.get("EMPLOYEE_CODE")));
//                lblCurrBranValue.setText(CommonUtil.convertObjToStr(hash.get("PRESENT_BRANCH_CODE")));
//                lblEmpName.setText(CommonUtil.convertObjToStr(hash.get("FNAME")));
//                lblBranName.setText(CommonUtil.convertObjToStr(hash.get("BRANCH_NAME")));
//        }
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
//                    ClientUtil.enableDisable(panInward, false);
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
//                    ClientUtil.enableDisable(panInward, false);
//                    btnEmp.setEnabled(false);
//                } else {
//                    btnSave.setEnabled(true);
//                    ClientUtil.enableDisable(panInward, true);
//                    btnEmp.setEnabled(true);
//                }
//            } else {
//                btnSave.setEnabled(true);
//                ClientUtil.enableDisable(panInward, true);
//                btnEmp.setEnabled(true);
//            }
//            setOpenForEditBy(lockedBy);
//            if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
//                String data = getLockDetails(lockedBy, getScreenID()) ;
//                ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
//                btnSave.setEnabled(false);
//                ClientUtil.enableDisable(panInward, false);
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
        
        txtFileNo.setEnabled(false);
    }
    
    
    public void update(Observable observed, Object arg) {
        // removeRadioButtons();
        
        
        /*txaAddress.setText(observable.getTxaDetails());
        txaParticulars.setText(observable.getTxaRemarks());
        txtApplicationNo.setText(observable.getTxtReferenceNo());
        txtSubmittedBy.setText(observable.getTxtSubmittedBy());*/
        lblStatus.setText(observable.getLblStatus());
        tdtDate.setDateValue(observable.getTdtApplnDt());
        txtApplicationNo.setText(observable.getTxtApplnNo());
        txtFileNo.setText(observable.getTxtFileNo());
        txtSubmittedBy.setText(observable.getTxtSubmittedBy());
        txtmemberNo.setText(observable.getTxtMemberNo());
        txaAddress.setText(observable.getTxtAddress());
        txaParticulars.setText(observable.getTxtParticulars());
        txaRemarks.setText(observable.getTxtRemarks());
        cboSubmittedTo.setSelectedItem(observable.getCboSubmittedTo());
        tdtSubmissionAction1.setDateValue(observable.getTdtSubmissionDt1());
        tdtSubmissionAction2.setDateValue(observable.getTdtSubmissionDt2());
        tdtSubmissionAction3.setDateValue(observable.getTdtSubmissionDt3());
        txaSubmissionAction1.setText(observable.getTxtSubmissionAction1());
        txaSubmissionAction2.setText(observable.getTxtSubmissionAction2());
        txaSubmissionAction3.setText(observable.getTxtSubmissionAction3());
        cboApprovedBy.setSelectedItem(observable.getCboApprovalStatus());
        tdtAprovalAction1.setDateValue(observable.getTdtApprovalDt1());
        tdtAprovalAction2.setDateValue(observable.getTdtApprovalDt2());
        tdtAprovalAction3.setDateValue(observable.getTdtApprovalDt3());
        txaAproveAction1.setText(observable.getTxtApprovalAction1());
        txaAproveAction2.setText(observable.getTxtApprovalAction2());
        txaAproveAction3.setText(observable.getTxtApprovalAction3());
    }

    public void updateOBFields() {
     
      observable.setTdtApplnDt(tdtDate.getDateValue());  
      observable.setTxtApplnNo(txtApplicationNo.getText());
      observable.setTxtFileNo(txtFileNo.getText());  
      observable.setTxtMemberNo(txtmemberNo.getText());
      observable.setTxtSubmittedBy(txtSubmittedBy.getText());
      observable.setTxtAddress(txaAddress.getText());
      observable.setTxtParticulars(txaParticulars.getText());
      observable.setTxtRemarks(txaRemarks.getText());
      
      observable.setCboSubmittedTo((String) ((ComboBoxModel) cboSubmittedTo.getModel()).getKeyForSelected());
      observable.setTdtSubmissionDt1(tdtSubmissionAction1.getDateValue());
      observable.setTdtSubmissionDt2(tdtSubmissionAction2.getDateValue());
      observable.setTdtSubmissionDt3(tdtSubmissionAction3.getDateValue());
      observable.setTxtSubmissionAction1(txaSubmissionAction1.getText());
      observable.setTxtSubmissionAction2(txaSubmissionAction2.getText());
      observable.setTxtSubmissionAction3(txaSubmissionAction3.getText());      
      
      observable.setCboApprovalStatus((String) ((ComboBoxModel) cboApprovedBy.getModel()).getKeyForSelected());
      observable.setTdtApprovalDt1(tdtAprovalAction1.getDateValue());
      observable.setTdtApprovalDt2(tdtAprovalAction2.getDateValue());
      observable.setTdtApprovalDt3(tdtAprovalAction3.getDateValue());
      observable.setTxtApprovalAction1(txaAproveAction1.getText());
      observable.setTxtApprovalAction2(txaAproveAction2.getText());
      observable.setTxtApprovalAction3(txaAproveAction3.getText());      
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
                    if (observable.getProxyReturnMap().containsKey("BOARD_MT_ID")) {
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
        tdtDate.setName("tdtDate");
        txaAddress.setName("txaDetails");
        txaParticulars.setName("txaRemarks");        
        txtFileNo.setName("txtInwardNo");
        txtApplicationNo.setName("txtReferenceNo");
        txtSubmittedBy.setName("txtSubmittedBy");        
        lblDetails.setName("lblDetails");
        lblSubmittedBy.setName("lblSubmittedBy");
        lblFileNo.setName("lblInwardNo");
        lblReferenceNo.setName("lblReferenceNo");
        lblStatus.setName("lblStatus");
        panInwardRegister.setName("panInwardRegister");
        panInward.setName("panInward");
        
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
    
    public void insertCustTableRecords(HashMap hash) {
          
          if(hash.containsKey("MEMBER NO")){
            txtmemberNo.setText(hash.get("MEMBER NO").toString());
            txtSubmittedBy.setText(hash.get("FNAME").toString());
            txaAddress.setText(hash.get("ADDRESS").toString());
          }
          
     }
    
    public HashMap generateID() {
        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "FILE_NO"); //Here u have to pass APPLICATION_NO or something else
            where.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            List list = null;
//            sqlMap.executeUpdate("updateIDGenerated", where);  // This update statement just updates curr_value=curr_value+1
            list = (List) ClientUtil.executeQuery(mapName, where);  // This will get u the updated curr_value, prefix and length
            //sqlMap.commitTransaction();

            if (list.size() > 0) {
                hash = (HashMap) list.get(0);
                String strPrefix = "", strLen = "";

                // Prefix for the ID.
                if (hash.containsKey("PREFIX")) {
                    strPrefix = (String) hash.get("PREFIX");
                    if (strPrefix == null || strPrefix.trim().length() == 0) {
                        strPrefix = "";
                    }
                }

                // Maximum Length for the ID
                int len = 10;
                if (hash.containsKey("ID_LENGTH")) {
                    strLen = String.valueOf(hash.get("ID_LENGTH"));
                    if (strLen == null || strLen.trim().length() == 0) {
                        len = 10;
                    } else {
                        len = Integer.parseInt(strLen.trim());
                    }
                }

                int numFrom = strPrefix.trim().length();

                String newID = String.valueOf(hash.get("CURR_VALUE"));
                long d = (long) Double.parseDouble(newID) + 1;
                newID = "";
                newID = "" + d;
                // Number Part of the String and incrementing 1 (ex. only 00085 from OGGOT00085)
                result = new HashMap();
                String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
                result.put(CommonConstants.DATA, genID);
                System.out.println("result111>>>>" + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("result222>>>>" + result);
        return result;

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
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton cButton1;
    private com.see.truetransact.uicomponent.CComboBox cboApprovedBy;
    private com.see.truetransact.uicomponent.CComboBox cboSubmittedTo;
    private com.see.truetransact.uicomponent.CLabel lblApprovedBy;
    private com.see.truetransact.uicomponent.CLabel lblDetails;
    private javax.swing.JLabel lblFileNo;
    private com.see.truetransact.uicomponent.CLabel lblInward;
    private com.see.truetransact.uicomponent.CLabel lblMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblParticulars;
    private javax.swing.JLabel lblReferenceNo;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblSpace76;
    private com.see.truetransact.uicomponent.CLabel lblSpace77;
    private com.see.truetransact.uicomponent.CLabel lblSpace78;
    private com.see.truetransact.uicomponent.CLabel lblSpace79;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSubmittedBy;
    private com.see.truetransact.uicomponent.CLabel lblSubmittedTo;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panApprovalDetails;
    private com.see.truetransact.uicomponent.CPanel panInward;
    private com.see.truetransact.uicomponent.CPanel panInwardRegister;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSubmissionDetails;
    private com.see.truetransact.uicomponent.CScrollPane scrRemarks;
    private com.see.truetransact.uicomponent.CScrollPane scrRemarks1;
    private com.see.truetransact.uicomponent.CScrollPane scrRemarks10;
    private com.see.truetransact.uicomponent.CScrollPane scrRemarks5;
    private com.see.truetransact.uicomponent.CScrollPane scrRemarks6;
    private com.see.truetransact.uicomponent.CScrollPane scrRemarks7;
    private com.see.truetransact.uicomponent.CScrollPane scrRemarks8;
    private com.see.truetransact.uicomponent.CScrollPane scrRemarks9;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpDetails;
    private javax.swing.JToolBar tbrInwardRegister;
    private com.see.truetransact.uicomponent.CDateField tdtAprovalAction1;
    private com.see.truetransact.uicomponent.CDateField tdtAprovalAction2;
    private com.see.truetransact.uicomponent.CDateField tdtAprovalAction3;
    private com.see.truetransact.uicomponent.CDateField tdtDate;
    private com.see.truetransact.uicomponent.CDateField tdtSubmissionAction1;
    private com.see.truetransact.uicomponent.CDateField tdtSubmissionAction2;
    private com.see.truetransact.uicomponent.CDateField tdtSubmissionAction3;
    private com.see.truetransact.uicomponent.CTextArea txaAddress;
    private com.see.truetransact.uicomponent.CTextArea txaAproveAction1;
    private com.see.truetransact.uicomponent.CTextArea txaAproveAction2;
    private com.see.truetransact.uicomponent.CTextArea txaAproveAction3;
    private com.see.truetransact.uicomponent.CTextArea txaParticulars;
    private com.see.truetransact.uicomponent.CTextArea txaRemarks;
    private com.see.truetransact.uicomponent.CTextArea txaSubmissionAction1;
    private com.see.truetransact.uicomponent.CTextArea txaSubmissionAction2;
    private com.see.truetransact.uicomponent.CTextArea txaSubmissionAction3;
    private javax.swing.JTextField txtApplicationNo;
    private javax.swing.JTextField txtFileNo;
    private javax.swing.JTextField txtSubmittedBy;
    private com.see.truetransact.uicomponent.CTextField txtmemberNo;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
        FileManagementUI Inward = new FileManagementUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(Inward);
        j.show();
        Inward.show();
    }
}
