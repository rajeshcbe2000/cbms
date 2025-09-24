  /*
   * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
   *
   * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
   * 
   * GroupLoanCustomerUI.java
   *
   * Created on November 26, 2003, 11:27 AM
   */

package com.see.truetransact.ui.termloan.groupLoan;
import com.see.truetransact.ui.termloan.SHG.*;

import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observable;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;

/**
 *
 * @author Suresh
 *
 **/

public class GroupLoanCustomerUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField{
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.groupLoan.GroupLoanCustomerMRB", ProxyParameters.LANGUAGE);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String viewType = new String();
    GroupLoanCustomerOB observable = null;
    final String AUTHORIZE="Authorize";
    HashMap mandatoryMap = null;
    boolean isFilled = false;
    private HashMap productMap = new HashMap();
    private boolean updateMode = false;
    int updateTab=-1;
    double odAmount = 0.0;
    Date currDt = null;
    boolean fromAuthorizeUI = false;    
    AuthorizeListUI authorizeListUI = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    
    /** Creates new form BeanForm */
    public GroupLoanCustomerUI() {
        initComponents();
        initForm();
    }
    
    private void initForm(){
        setFieldNames();
        internationalize();
        observable = new GroupLoanCustomerOB();
        setMaxLengths();
        initComponentData();
        setButtonEnableDisable();
        buttonEnableDisable(false);
        ClientUtil.enableDisable(panSHGDetails,false);
        btnGroupLoan.setEnabled(false);
        txtGroupLoanNum.setEnabled(false);
        btnAccountNo.setEnabled(false);
        lblTotLimit.setText("0.0");
        currDt = ClientUtil.getCurrentDate(); 
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
        //btnMemberNo.setName("btnMemberNo");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSHGDelete.setName("btnSHGDelete");
        btnSHGNew.setName("btnSHGNew");
        btnSHGSave.setName("btnSHGSave");
        btnSave.setName("btnSave");
        btnView.setName("btnView");
        lblArea.setName("lblArea");
        lblAreaVal.setName("lblAreaVal");
        lblCity.setName("lblCity");
        lblCityVal.setName("lblCityVal");
        lblGroupLoan.setName("lblGroupLoan");
        lblMemberName.setName("lblMemberName");
        lblMemberNameVal.setName("lblMemberNameVal");
        //lblMemberNo.setName("lblMemberNo");
        lblMsg.setName("lblMsg");
        lblSpace.setName("lblSpace");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        lblState.setName("lblState");
        lblStateVal.setName("lblStateVal");
        lblStatus.setName("lblStatus");
        lblStreet.setName("lblStreet");
        lblStreetVal.setName("lblStreetVal");
        mbrMain.setName("mbrMain");
        panMemberNo.setName("panMemberNo");
        panSHGBtn.setName("panSHGBtn");
        panSHGDetails.setName("panSHGDetails");
        panSHGRecordDetails.setName("panSHGRecordDetails");
        panSHGTableDetails.setName("panSHGTableDetails");
        panStatus.setName("panStatus");
        srpSHGTable.setName("srpSHGTable");
        tabSHGDetails.setName("tabSHGDetails");
        tblSHGDetails.setName("tblSHGDetails");
        txtGroupLoanNum.setName("txtGroupLoanNum");
        lblCCNo.setName("lblCCNo");
        txtCCNo.setName("txtCCNo");
        txtLimitAmt.setName("txtLimitAmt");
        //txtMemberNo.setName("txtMemberNo");
    }
    
    private void setMaxLengths(){
        txtLimitAmt.setValidation(new CurrencyValidation(13, 2));
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        resourceBundle = new GroupLoanCustomerRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblStreet.setText(resourceBundle.getString("lblStreet"));
        lblCity.setText(resourceBundle.getString("lblCity"));
        lblStreetVal.setText(resourceBundle.getString("lblStreetVal"));
        lblGroupLoan.setText(resourceBundle.getString("lblGroupLoan"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblAreaVal.setText(resourceBundle.getString("lblAreaVal"));
        //lblMemberNo.setText(resourceBundle.getString("lblMemberNo"));
        btnSHGSave.setText(resourceBundle.getString("btnSHGSave"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblArea.setText(resourceBundle.getString("lblArea"));
        btnView.setText(resourceBundle.getString("btnView"));
        lblCityVal.setText(resourceBundle.getString("lblCityVal"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        ///btnMemberNo.setText(resourceBundle.getString("btnMemberNo"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblStateVal.setText(resourceBundle.getString("lblStateVal"));
        lblMemberNameVal.setText(resourceBundle.getString("lblMemberNameVal"));
        btnSHGNew.setText(resourceBundle.getString("btnSHGNew"));
        lblMemberName.setText(resourceBundle.getString("lblMemberName"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblState.setText(resourceBundle.getString("lblState"));
        lblSpace6.setText(resourceBundle.getString("lblSpace6"));
        btnSHGDelete.setText(resourceBundle.getString("btnSHGDelete"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblCCNo.setText(resourceBundle.getString("lblCCNo"));
        txtCCNo.setText(resourceBundle.getString("txtCCNo"));
        txtLimitAmt.setText(resourceBundle.getString("txtLimitAmt"));
   }
    
    /* Auto Generated Method - setMandatoryHashMap()
     
    ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    private void initComponentData() {
        try{
            //cboProdId.setModel(observable.getCbmProdId());
            tblSHGDetails.setModel(observable.getTblSHGDetails());
            cboProdTypeCr.setModel(observable.getCbmProdTypCr());
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
    private void setMaximumLength(){
        //        txtDivisionNo.setValidation(new NumericValidation());
    }
    
    /** Auto Generated Method - update()
     * This method called by Observable. It updates the UI with
     * Observable's data. If needed add/Remove RadioButtons
     * method need to be added.*/
    public void update(Observable observed, Object arg) {
    }
    
    
    public void update(){
        txtGroupLoanNum.setText(observable.getGroupLoanNo());
        txtCustomerID.setText(observable.getCustID());
        System.out.println("observable.getGroupLoanName()##"+observable.getGroupLoanName());
        lblGroupName.setText(observable.getGroupLoanName());
        lblAreaVal.setText(observable.getLblAreaVal());
        lblCityVal.setText(observable.getLblCityVal());
        lblMemberNameVal.setText(observable.getLblMemberNameVal());
        lblStateVal.setText(observable.getLblStateVal());
        lblStreetVal.setText(observable.getLblStreetVal());
        txtLimitAmt.setText(observable.getLimitAmt());
        txtCCNo.setText(observable.getCreditNo());
        txtAcctNo.setText(observable.getCustomerActNum());
        txtTransProductId.setText(observable.getCustomerActProdId());
        cboProdTypeCr.setSelectedItem(observable.getCbmProdTypCr().getDataForKey(observable.getCustomerActProdType()));
    }
    
    /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCustID(txtCustomerID.getText());
        observable.setGroupLoanName(lblGroupName.getText());
        observable.setGroupLoanNo(txtGroupLoanNum.getText());
        observable.setLblAreaVal(lblAreaVal.getText());
        observable.setLblCityVal(lblCityVal.getText());
        observable.setLblMemberNameVal(lblMemberNameVal.getText());
        observable.setLblStateVal(lblStateVal.getText());
        observable.setLblStreetVal(lblStreetVal.getText());
        observable.setLimitAmt(txtLimitAmt.getText());
        observable.setCreditNo(txtCCNo.getText());
        observable.setCustomerActNum(txtAcctNo.getText());
        observable.setCustomerActProdId(txtTransProductId.getText());
        observable.setCustomerActProdType(CommonUtil.convertObjToStr(observable.getCbmProdTypCr().getKeyForSelected()));
   }
    
    
    
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        
    }
    
    
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnAccountNo.setEnabled(btnDelete.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        
        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
    
    private void buttonEnableDisable(boolean flag){
        btnSHGNew.setEnabled(flag);
        btnSHGSave.setEnabled(flag);
        btnSHGDelete.setEnabled(flag);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgIsLapsedGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgEFTProductGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPayableBranchGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPrintServicesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgSeriesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        btnException = new com.see.truetransact.uicomponent.CButton();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabSHGDetails = new com.see.truetransact.uicomponent.CTabbedPane();
        panSHGDetails = new com.see.truetransact.uicomponent.CPanel();
        panSHGRecordDetails = new com.see.truetransact.uicomponent.CPanel();
        panMemberNo = new com.see.truetransact.uicomponent.CPanel();
        lblLimitAmt = new com.see.truetransact.uicomponent.CLabel();
        lblStreet = new com.see.truetransact.uicomponent.CLabel();
        lblMemberName = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        lblArea = new com.see.truetransact.uicomponent.CLabel();
        lblGroupName = new com.see.truetransact.uicomponent.CLabel();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        lblStateVal = new com.see.truetransact.uicomponent.CLabel();
        panSHGBtn = new com.see.truetransact.uicomponent.CPanel();
        btnSHGNew = new com.see.truetransact.uicomponent.CButton();
        btnSHGSave = new com.see.truetransact.uicomponent.CButton();
        btnSHGDelete = new com.see.truetransact.uicomponent.CButton();
        lblAreaVal = new com.see.truetransact.uicomponent.CLabel();
        lblCityVal = new com.see.truetransact.uicomponent.CLabel();
        lblStreetVal = new com.see.truetransact.uicomponent.CLabel();
        lblMemberNameVal = new com.see.truetransact.uicomponent.CLabel();
        lblGroupLoan = new com.see.truetransact.uicomponent.CLabel();
        panAccountNo = new com.see.truetransact.uicomponent.CPanel();
        txtCustomerID = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNo = new com.see.truetransact.uicomponent.CButton();
        lblAccountNo = new com.see.truetransact.uicomponent.CLabel();
        panAccountNo1 = new com.see.truetransact.uicomponent.CPanel();
        txtGroupLoanNum = new com.see.truetransact.uicomponent.CTextField();
        btnGroupLoan = new com.see.truetransact.uicomponent.CButton();
        txtLimitAmt = new com.see.truetransact.uicomponent.CTextField();
        lblCCNo = new com.see.truetransact.uicomponent.CLabel();
        txtCCNo = new com.see.truetransact.uicomponent.CTextField();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        lblODAmount = new com.see.truetransact.uicomponent.CLabel();
        panAccountDetails = new com.see.truetransact.uicomponent.CPanel();
        cboProdTypeCr = new com.see.truetransact.uicomponent.CComboBox();
        lblProdTypeCr = new com.see.truetransact.uicomponent.CLabel();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblProdIdCr = new com.see.truetransact.uicomponent.CLabel();
        panAccHd = new com.see.truetransact.uicomponent.CPanel();
        txtAcctNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        panAcctNo = new com.see.truetransact.uicomponent.CPanel();
        panDebitAccHead = new com.see.truetransact.uicomponent.CPanel();
        txtTransProductId = new com.see.truetransact.uicomponent.CTextField();
        btnTransProductId = new com.see.truetransact.uicomponent.CButton();
        panSHGTableDetails = new com.see.truetransact.uicomponent.CPanel();
        srpSHGTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblSHGDetails = new com.see.truetransact.uicomponent.CTable();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        lblTotLimit = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMinimumSize(new java.awt.Dimension(850, 630));
        setPreferredSize(new java.awt.Dimension(850, 630));
        getContentPane().setLayout(new java.awt.GridBagLayout());

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
        tbrAdvances.add(btnView);

        lblSpace6.setText("     ");
        tbrAdvances.add(lblSpace6);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnNew);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnDelete);

        lblSpace3.setText("     ");
        tbrAdvances.add(lblSpace3);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnSave);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnCancel);

        lblSpace4.setText("     ");
        tbrAdvances.add(lblSpace4);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnAuthorize);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnReject);

        lblSpace5.setText("     ");
        tbrAdvances.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrAdvances.add(btnPrint);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(tbrAdvances, gridBagConstraints);

        tabSHGDetails.setMinimumSize(new java.awt.Dimension(550, 490));
        tabSHGDetails.setPreferredSize(new java.awt.Dimension(550, 490));

        panSHGDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSHGDetails.setMinimumSize(new java.awt.Dimension(570, 450));
        panSHGDetails.setPreferredSize(new java.awt.Dimension(570, 450));
        panSHGDetails.setLayout(new java.awt.GridBagLayout());

        panSHGRecordDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSHGRecordDetails.setMinimumSize(new java.awt.Dimension(375, 440));
        panSHGRecordDetails.setPreferredSize(new java.awt.Dimension(375, 440));
        panSHGRecordDetails.setLayout(new java.awt.GridBagLayout());

        panMemberNo.setLayout(new java.awt.GridBagLayout());

        lblLimitAmt.setText("Limit Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        panMemberNo.add(lblLimitAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 50, 0, 0);
        panSHGRecordDetails.add(panMemberNo, gridBagConstraints);

        lblStreet.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 93, 0, 0);
        panSHGRecordDetails.add(lblStreet, gridBagConstraints);

        lblMemberName.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 34, 0, 0);
        panSHGRecordDetails.add(lblMemberName, gridBagConstraints);

        lblCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 105, 0, 0);
        panSHGRecordDetails.add(lblCity, gridBagConstraints);

        lblArea.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 101, 0, 0);
        panSHGRecordDetails.add(lblArea, gridBagConstraints);

        lblGroupName.setForeground(new java.awt.Color(0, 0, 204));
        lblGroupName.setAlignmentY(0.0F);
        lblGroupName.setFont(new java.awt.Font("MS Sans Serif", 1, 14));
        lblGroupName.setMaximumSize(new java.awt.Dimension(350, 18));
        lblGroupName.setMinimumSize(new java.awt.Dimension(350, 18));
        lblGroupName.setPreferredSize(new java.awt.Dimension(350, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 12, 0, 13);
        panSHGRecordDetails.add(lblGroupName, gridBagConstraints);

        lblState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 97, 0, 0);
        panSHGRecordDetails.add(lblState, gridBagConstraints);

        lblStateVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblStateVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblStateVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panSHGRecordDetails.add(lblStateVal, gridBagConstraints);

        panSHGBtn.setMinimumSize(new java.awt.Dimension(95, 35));
        panSHGBtn.setPreferredSize(new java.awt.Dimension(95, 35));
        panSHGBtn.setLayout(new java.awt.GridBagLayout());

        btnSHGNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnSHGNew.setToolTipText("New");
        btnSHGNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSHGNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSHGNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSHGNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSHGNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSHGBtn.add(btnSHGNew, gridBagConstraints);

        btnSHGSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSHGSave.setToolTipText("Save");
        btnSHGSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSHGSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSHGSave.setName("btnContactNoAdd"); // NOI18N
        btnSHGSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSHGSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSHGSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSHGBtn.add(btnSHGSave, gridBagConstraints);

        btnSHGDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnSHGDelete.setToolTipText("Delete");
        btnSHGDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSHGDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSHGDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSHGDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSHGDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSHGBtn.add(btnSHGDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 114, 2, 0);
        panSHGRecordDetails.add(panSHGBtn, gridBagConstraints);

        lblAreaVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblAreaVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblAreaVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panSHGRecordDetails.add(lblAreaVal, gridBagConstraints);

        lblCityVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblCityVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblCityVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panSHGRecordDetails.add(lblCityVal, gridBagConstraints);

        lblStreetVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblStreetVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblStreetVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panSHGRecordDetails.add(lblStreetVal, gridBagConstraints);

        lblMemberNameVal.setForeground(new java.awt.Color(0, 51, 204));
        lblMemberNameVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblMemberNameVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblMemberNameVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panSHGRecordDetails.add(lblMemberNameVal, gridBagConstraints);

        lblGroupLoan.setText("Group Loan Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        panSHGRecordDetails.add(lblGroupLoan, gridBagConstraints);

        panAccountNo.setLayout(new java.awt.GridBagLayout());

        txtCustomerID.setAllowAll(true);
        txtCustomerID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustomerID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustomerIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountNo.add(txtCustomerID, gridBagConstraints);

        btnAccountNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountNo.setEnabled(false);
        btnAccountNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAccountNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAccountNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccountNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountNo.add(btnAccountNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panSHGRecordDetails.add(panAccountNo, gridBagConstraints);

        lblAccountNo.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 56, 0, 0);
        panSHGRecordDetails.add(lblAccountNo, gridBagConstraints);

        panAccountNo1.setLayout(new java.awt.GridBagLayout());

        txtGroupLoanNum.setBackground(new java.awt.Color(204, 204, 204));
        txtGroupLoanNum.setAllowAll(true);
        txtGroupLoanNum.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtGroupLoanNum.setMinimumSize(new java.awt.Dimension(100, 21));
        txtGroupLoanNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGroupLoanNumActionPerformed(evt);
            }
        });
        txtGroupLoanNum.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGroupLoanNumFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panAccountNo1.add(txtGroupLoanNum, gridBagConstraints);

        btnGroupLoan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnGroupLoan.setMaximumSize(new java.awt.Dimension(21, 21));
        btnGroupLoan.setMinimumSize(new java.awt.Dimension(21, 21));
        btnGroupLoan.setPreferredSize(new java.awt.Dimension(21, 21));
        btnGroupLoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGroupLoanActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panAccountNo1.add(btnGroupLoan, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panSHGRecordDetails.add(panAccountNo1, gridBagConstraints);

        txtLimitAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panSHGRecordDetails.add(txtLimitAmt, gridBagConstraints);

        lblCCNo.setText("Credit Card No ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 41, 0, 0);
        panSHGRecordDetails.add(lblCCNo, gridBagConstraints);

        txtCCNo.setAllowAll(true);
        txtCCNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtCCNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCCNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCCNoActionPerformed(evt);
            }
        });
        txtCCNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCCNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panSHGRecordDetails.add(txtCCNo, gridBagConstraints);

        cLabel2.setText("OD Amount ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panSHGRecordDetails.add(cLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panSHGRecordDetails.add(cLabel3, gridBagConstraints);

        lblODAmount.setMaximumSize(new java.awt.Dimension(100, 18));
        lblODAmount.setMinimumSize(new java.awt.Dimension(100, 18));
        lblODAmount.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panSHGRecordDetails.add(lblODAmount, gridBagConstraints);

        panAccountDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Details"));
        panAccountDetails.setMinimumSize(new java.awt.Dimension(300, 130));
        panAccountDetails.setPreferredSize(new java.awt.Dimension(300, 130));
        panAccountDetails.setLayout(new java.awt.GridBagLayout());

        cboProdTypeCr.setMinimumSize(new java.awt.Dimension(150, 21));
        cboProdTypeCr.setPopupWidth(160);
        cboProdTypeCr.setPreferredSize(new java.awt.Dimension(150, 21));
        cboProdTypeCr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeCrActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails.add(cboProdTypeCr, gridBagConstraints);

        lblProdTypeCr.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails.add(lblProdTypeCr, gridBagConstraints);

        lblAccNo.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails.add(lblAccNo, gridBagConstraints);

        lblProdIdCr.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails.add(lblProdIdCr, gridBagConstraints);

        panAccHd.setMinimumSize(new java.awt.Dimension(121, 21));
        panAccHd.setPreferredSize(new java.awt.Dimension(21, 200));
        panAccHd.setLayout(new java.awt.GridBagLayout());

        txtAcctNo.setAllowAll(true);
        txtAcctNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcctNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAcctNoActionPerformed(evt);
            }
        });
        txtAcctNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcctNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccHd.add(txtAcctNo, gridBagConstraints);

        btnAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccNo.setToolTipText("Account Number");
        btnAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNoActionPerformed(evt);
            }
        });
        panAccHd.add(btnAccNo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        panAccountDetails.add(panAccHd, gridBagConstraints);

        panAcctNo.setMinimumSize(new java.awt.Dimension(121, 21));
        panAcctNo.setPreferredSize(new java.awt.Dimension(21, 200));
        panAcctNo.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, -8, 1, 0);
        panAccountDetails.add(panAcctNo, gridBagConstraints);

        panDebitAccHead.setLayout(new java.awt.GridBagLayout());

        txtTransProductId.setAllowAll(true);
        txtTransProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTransProductId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTransProductIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDebitAccHead.add(txtTransProductId, gridBagConstraints);

        btnTransProductId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnTransProductId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnTransProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransProductIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDebitAccHead.add(btnTransProductId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, -28, 0, 0);
        panAccountDetails.add(panDebitAccHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSHGRecordDetails.add(panAccountDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSHGDetails.add(panSHGRecordDetails, gridBagConstraints);

        panSHGTableDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSHGTableDetails.setMinimumSize(new java.awt.Dimension(425, 440));
        panSHGTableDetails.setPreferredSize(new java.awt.Dimension(425, 440));
        panSHGTableDetails.setLayout(null);

        srpSHGTable.setMinimumSize(new java.awt.Dimension(400, 420));
        srpSHGTable.setPreferredSize(new java.awt.Dimension(400, 350));

        tblSHGDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Customer Id", "Customer  Name", "Credit Card No", "Limit"
            }
        ));
        tblSHGDetails.setMinimumSize(new java.awt.Dimension(400, 1000));
        tblSHGDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(250, 400));
        tblSHGDetails.setPreferredSize(new java.awt.Dimension(400, 1000));
        tblSHGDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSHGDetailsMousePressed(evt);
            }
        });
        srpSHGTable.setViewportView(tblSHGDetails);

        panSHGTableDetails.add(srpSHGTable);
        srpSHGTable.setBounds(12, 45, 400, 350);

        cLabel1.setText("Total Limit :");
        panSHGTableDetails.add(cLabel1);
        cLabel1.setBounds(150, 410, 80, 18);

        lblTotLimit.setMaximumSize(new java.awt.Dimension(100, 21));
        lblTotLimit.setMinimumSize(new java.awt.Dimension(100, 21));
        lblTotLimit.setPreferredSize(new java.awt.Dimension(100, 21));
        panSHGTableDetails.add(lblTotLimit);
        lblTotLimit.setBounds(220, 410, 100, 21);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSHGDetails.add(panSHGTableDetails, gridBagConstraints);

        tabSHGDetails.addTab("Group Loan ", panSHGDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabSHGDetails, gridBagConstraints);

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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(panStatus, gridBagConstraints);

        mnuProcess.setText("Process");

        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitAuthorize.setText("Authorize");
        mnuProcess.add(mitAuthorize);

        mitReject.setText("Rejection");
        mnuProcess.add(mitReject);

        mitException.setText("Exception");
        mnuProcess.add(mitException);
        mnuProcess.add(sptException);

        mitPrint.setText("Print");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAccountNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNoActionPerformed
        // TODO add your handling code here:
        //callView("CUST_ID");
        viewType = "CUST_ID";
        new CheckCustomerIdUI(this);
    }//GEN-LAST:event_btnAccountNoActionPerformed

    private void btnSHGDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSHGDeleteActionPerformed
        // TODO add your handling code here:
        String custId =  CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(),0));
        List loanLst = null;
       if(observable.getActionType()!=ClientConstants.ACTIONTYPE_NEW){
            HashMap whereMap = new HashMap();
            whereMap.put("CUST_ID",custId);
            whereMap.put("ACT_NUM",txtGroupLoanNum.getText());
            whereMap.put("TRANS_DT",currDt);
            loanLst=ClientUtil.executeQuery("CheckGroupLoanDetails",whereMap);
            if(loanLst !=null && loanLst.size()>0){
                whereMap = (HashMap) loanLst.get(0);
                System.out.println("checkMap#$#$"+whereMap);
                System.out.println("!@$$$!@@#@$#%$%#%#$#$"+CommonUtil.convertObjToDouble(whereMap.get("DUE_AMT")));
                if(CommonUtil.convertObjToDouble(whereMap.get("DUE_AMT"))>0){
                    ClientUtil.showMessageWindow("This Customer already having Loan, Cannot Delete !!!");
                    return; 
                }else{            
                    observable.deleteTableData(custId,tblSHGDetails.getSelectedRow());
                    observable.resetSHGDetails();
                    resetSHGDetails();
                    calcToatal();
                    buttonEnableDisable(false);
                    btnSHGNew.setEnabled(true);
               }
            }
       }
       if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){                      
                observable.deleteTableData(custId,tblSHGDetails.getSelectedRow());
                observable.resetSHGDetails();
                resetSHGDetails();
                calcToatal();
                buttonEnableDisable(false);
                btnSHGNew.setEnabled(true);           
        }
    }//GEN-LAST:event_btnSHGDeleteActionPerformed

    private void tblSHGDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSHGDetailsMousePressed
        // TODO add your handling code here:
        updateOBFields();
        System.out.println("mouse clicked#####ActNum1111"+observable.getGroupLoanNo());
        updateMode = true;
        updateTab= tblSHGDetails.getSelectedRow();
        observable.setNewData(false);
        String st=CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(),0));
        double amount = CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(),3));
        System.out.println("mouse clicked amount#####"+amount);
        observable.populateSHGDetails(st);
        observable.getCustNameForLoan(txtGroupLoanNum.getText());
        setLimitAmount(amount);
        System.out.println("mouse clicked#####ActNum22222"+observable.getGroupLoanNo());
        System.out.println("mouse clicked#####creditNO"+observable.getCreditNo());
        update();
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE||observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW || observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE ||
        observable.getActionType()==ClientConstants.ACTIONTYPE_EXCEPTION){
            buttonEnableDisable(false);
            ClientUtil.enableDisable(panSHGRecordDetails,false);
        }else {
            buttonEnableDisable(true);
            btnSHGNew.setEnabled(false);
            //btnMemberNo.setEnabled(false);
            //txtMemberNo.setEnabled(false);
        }
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
            btnAccountNo.setEnabled(true);
             btnSHGNew.setEnabled(false);
             btnSHGSave.setEnabled(true);
             btnSHGDelete.setEnabled(true);
             txtLimitAmt.setEnabled(true);
             txtCCNo.setEnabled(true);
             txtGroupLoanNum.setEnabled(true);
             cboProdTypeCr.setEnabled(true);
        }
    }//GEN-LAST:event_tblSHGDetailsMousePressed

    private void setLimitAmount(double amount){
        calcToatal();
        double existAmount =  CommonUtil.convertObjToDouble(lblTotLimit.getText());
        existAmount = existAmount - amount;
        System.out.println("mouse clicked existAmount#####"+existAmount);
        lblTotLimit.setText(String.valueOf(existAmount));
        
    }
    
            
    private void btnSHGSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSHGSaveActionPerformed
        // TODO add your handling code here:
        try{
                double limit = 0.0;
                double od = 0.0;
                double txtAmont = 0.0;
                updateOBFields();                
                if(txtCustomerID.getText().length()>0){
                        limit =  CommonUtil.convertObjToDouble(lblTotLimit.getText());
                        od = CommonUtil.convertObjToDouble(lblODAmount.getText());
                        txtAmont = CommonUtil.convertObjToDouble(txtLimitAmt.getText());
                        System.out.println("lblODAmount.getText()txt#############"+lblODAmount.getText());
                        System.out.println("lblTotLimit.getText()#############"+lblTotLimit.getText()); 
                        System.out.println("observable.getTotalAmount()Save"+limit);
                        System.out.println("odAmount.odAmount()Save"+od);                    
                        if((limit+txtAmont) <= od){
                            if(observable.isDailyGroupLoan(txtGroupLoanNum.getText())){
                                if(cboProdTypeCr.getSelectedItem()!=null && !cboProdTypeCr.getSelectedItem().equals("")){
                                    if(txtTransProductId.getText().length()>0){
                                        if(txtAcctNo.getText().length()>0){
                                           HashMap existMap = observable.isCustActNumExist(txtAcctNo.getText());
                                           if(!(existMap!=null && !existMap.equals("null") && existMap.size()>0 )){
                                                if(!observable.isCustActNumExistInGrid(txtAcctNo.getText())){
                                                    observable.addToTable(updateTab,updateMode);
                                                    tblSHGDetails.setModel(observable.getTblSHGDetails());
                                                    observable.resetSHGDetails();
                                                    resetSHGDetails();
                                                    calcToatal();
                                                    System.out.println("txtTotLimittxtTotLimittxtTotLimit"+lblTotLimit.getText());
                                                    ClientUtil.enableDisable(panSHGRecordDetails,false);
                                                    buttonEnableDisable(false);
                                                    btnSHGNew.setEnabled(true);
                                                    btnGroupLoan.setEnabled(false);
                                                    btnAccountNo.setEnabled(false); 
                                                }else{
                                                    ClientUtil.showAlertWindow("customer account is alredy Saved in Grid!!!");
                                                    return;
                                                }                                                
                                            }else{
                                                ClientUtil.showAlertWindow("customer account is alredy users for customer ,"+existMap.get("CUSTOMER")+ "!!!");
                                                return;
                                            }
                                        }else{
                                            ClientUtil.showAlertWindow("Customer Account No.should be Empty !!!");
                                            return;
                                        }
                                    }else{
                                            ClientUtil.showAlertWindow("Customer Account Product ID should be Empty !!!");
                                            return;
                                    }                                    
                                }else{
                                            ClientUtil.showAlertWindow("Customer Account Product Type should be Empty !!!");
                                            return;
                                }                                 
                            }else{
                                if(txtCCNo.getText()!=null && txtCCNo.getText().length()>0){
                                    observable.addToTable(updateTab,updateMode);
                                    tblSHGDetails.setModel(observable.getTblSHGDetails());
                                    observable.resetSHGDetails();
                                    resetSHGDetails();
                                    calcToatal();
                                    System.out.println("txtTotLimittxtTotLimittxtTotLimit"+lblTotLimit.getText());
                                    ClientUtil.enableDisable(panSHGRecordDetails,false);
                                    buttonEnableDisable(false);
                                    btnSHGNew.setEnabled(true);
                                    btnGroupLoan.setEnabled(false);
                                    btnAccountNo.setEnabled(false);
                                }else{
                                    ClientUtil.showAlertWindow("Please enter Credit card number !!!");
                                    return;
                                }
                            }                            
                    }else{
                        ClientUtil.showAlertWindow("Limit exceeds !!!");
                        return;
                   }
                    //txtGroupName.setEnabled(false);
                }else{
                    ClientUtil.showAlertWindow("Member No Should Not be Empty !!!");
                    return;
                }
                
                
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSHGSaveActionPerformed
    private void resetSHGDetails(){
        txtCustomerID.setText("");
        lblMemberNameVal.setText("");
        lblStreetVal.setText("");
        lblAreaVal.setText("");
        lblCityVal.setText("");
        lblStateVal.setText("");
        //lblGroupName.setText("");
        txtLimitAmt.setText("");
        txtCCNo.setText("");
        txtAcctNo.setText("");
        txtTransProductId.setText("");
        cboProdTypeCr.setSelectedItem("");
        //txtGroupLoanNum.setText("");
    }
    private void btnSHGNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSHGNewActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        btnAccountNo.setEnabled(true);
        btnGroupLoan.setEnabled(true);
        buttonEnableDisable(false);
        btnSHGSave.setEnabled(true);
        observable.setNewData(true);
        txtLimitAmt.setEnabled(true);
        txtCCNo.setEnabled(true);
        txtGroupLoanNum.setEnabled(true);
        if(tblSHGDetails.getRowCount()<=0) {
            //txtGroupName.setEnabled(true);
            //cboProdId.setEnabled(true);
            btnAccountNo.setEnabled(true);
        }
        cboProdTypeCr.setEnabled(true);
    }//GEN-LAST:event_btnSHGNewActionPerformed
     
    /** To display a popUp window for viewing existing data */
    private void callView(String currAction){
        viewType = currAction;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();    
        whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        if (currAction.equalsIgnoreCase("Edit")){
            viewMap.put(CommonConstants.MAP_NAME, "getGroupLoanEdit");
        }else if(currAction.equalsIgnoreCase("Delete")){
            viewMap.put(CommonConstants.MAP_NAME, "getSHGDelete");
        }else if(currAction.equalsIgnoreCase("Enquiry")){
            viewMap.put(CommonConstants.MAP_NAME, "getSHGView");
        }else if(viewType.equals("CUST_ID")){
            viewMap.put(CommonConstants.MAP_NAME, "getCustomer");
            whereMap.put("SELECTED_BRANCH", getSelectedBranchID());
            //whereMap.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
        else if(viewType.equals("GROUP_LOAN_NO")){
            viewMap.put(CommonConstants.MAP_NAME, "getGroupLoanCustomerDetails");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }else if (viewType.equals("PROD_ID")) {
            HashMap where_map = new HashMap();
            where_map.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            String prodType = CommonUtil.convertObjToStr((((ComboBoxModel) (cboProdTypeCr).getModel())).getKeyForSelected());
            viewMap.put(CommonConstants.MAP_NAME, "InterMaintenance.getProductData" + CommonUtil.convertObjToStr(observable.getCbmProdTypCr().getKeyForSelected()));
            viewMap.put(CommonConstants.MAP_WHERE, where_map);
        }else if (viewType.equals("ACCT_NO")) {
            whereMap.put("PROD_ID", txtTransProductId.getText());
            whereMap.put("SELECTED_BRANCH", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            if (observable.getProdType().equals("TD") || observable.getProdType().equals("TL") || observable.getProdType().equals("AB")) {
                if (observable.getProdType().equals("TL") || observable.getProdType().equals("AB")) {
                    whereMap.put("RECEIPT", "RECEIPT");
                }
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                        + CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected()));
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                        + CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected()));
            }
        }
        new ViewAll(this,viewMap).show();
    }
    
    public void fillData(Object obj){
        try{
            HashMap hashMap=(HashMap)obj;
            System.out.println("### fillData Hash : "+hashMap);
            System.out.println("### fillData viewType : "+viewType);
            isFilled = true;
             List lst1=null;
             if (hashMap.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
                fromNewAuthorizeUI = true;
                newauthorizeListUI = (NewAuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                //transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(true);                
            }
             if (hashMap.containsKey("FROM_AUTHORIZE_LIST_UI")) {
                fromAuthorizeUI = true;
                authorizeListUI = (AuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                //transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(true);                
            }
            if(viewType == "CUST_ID"){
                String cust = CommonUtil.convertObjToStr(hashMap.get("CUST_ID"));  
                HashMap custMap = new HashMap();
                custMap.put("CUST_ID",cust);
                List lst=ClientUtil.executeQuery("getClassCustomer",custMap);
                System.out.println("lst####"+lst);
                if(lst !=null && lst.size()>0){
                    ClientUtil.showMessageWindow("C Class members not allowed for Group Loan!!");
                    return;
                }
                System.out.println("### CUST_ID : "+cust);
                txtCustomerID.setText(cust);
                if(hashMap.containsKey("CUST_ID") || hashMap.containsKey("NAME")){
                    lst1=ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashMap);
                    hashMap.put("MEMBERSHIP_NO",hashMap.get("MEMBER_NO"));
                    hashMap.put("CUSTOMER",hashMap.get("NAME"));
                }
                //txtMemberNo.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBERSHIP_NO")));
                lblMemberNameVal.setText(CommonUtil.convertObjToStr(hashMap.get("CUSTOMER")));
                if(lst1!=null && lst1.size()>0){
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    //txtMemberNo.setText("");
                    lblMemberNameVal.setText("");
                    return;
                }
                observable.setCustID(txtCustomerID.getText());
                observable.setLblMemberNameVal(lblMemberNameVal.getText());
                String memberNo ="";
                if(tblSHGDetails.getRowCount()>0) {
                    for(int i=0;i<tblSHGDetails.getRowCount();i++){
                        String membNo = CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(i,0));
                        if(memberNo.equalsIgnoreCase(membNo) && !updateMode) {
                            ClientUtil.displayAlert("Member No Already Exists in this Table");
                            resetSHGDetails();
                            buttonEnableDisable(false);
                            btnSHGNew.setEnabled(true);
                            //btnMemberNo.setEnabled(false);
                            //txtMemberNo.setEnabled(false);
                            return;
                        }
                    }
                }
                txtCustomerID.setText(CommonUtil.convertObjToStr(hashMap.get("CUST_ID")));
                observable.getCustomerAddressDetails(CommonUtil.convertObjToStr(hashMap.get("CUST_ID")));
                ClientUtil.enableDisable(panSHGRecordDetails,false);
                txtLimitAmt.setEnabled(true);
                txtCCNo.setEnabled(true);
                cboProdTypeCr.setEnabled(true);
                update();
                observable.getCustNameForLoan(txtGroupLoanNum.getText());
                lblGroupName.setText(observable.getGroupLoanName()); 
                return;
            }else if(viewType == "GROUP_LOAN_NO"){
                System.out.println("hashMapGROUP_LOAN_NO"+hashMap);
                txtGroupLoanNum.setText(CommonUtil.convertObjToStr(hashMap.get("ACT_NUM")));
                observable.setGroupLoanNo(txtGroupLoanNum.getText());
                observable.getCustNameForLoan(CommonUtil.convertObjToStr(hashMap.get("ACT_NUM")));
                lblGroupName.setText(observable.getGroupLoanName()); 
                //lblODAmount.setText(CurrencyValidation.formatCrore(String.valueOf(observable.setOdAmount(txtGroupLoanNum.getText()))));
                lblODAmount.setText(CommonUtil.convertObjToStr(observable.setOdAmount(txtGroupLoanNum.getText())));
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
                this.setButtonEnableDisable();
                txtGroupLoanNum.setText(CommonUtil.convertObjToStr(hashMap.get("ACT_NUM")));
                observable.setGroupLoanNo(txtGroupLoanNum.getText());
                observable.getCustNameForLoan(CommonUtil.convertObjToStr(hashMap.get("ACT_NUM")));
                lblGroupName.setText(observable.getGroupLoanName()); 
                observable.getData(hashMap);                
                tblSHGDetails.setModel(observable.getTblSHGDetails());
                //lblODAmount.setText(CurrencyValidation.formatCrore(String.valueOf(observable.setOdAmount(txtGroupLoanNum.getText()))));
                lblODAmount.setText(CommonUtil.convertObjToStr(observable.setOdAmount(txtGroupLoanNum.getText())));
                calcToatal();
                
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                this.setButtonEnableDisable();
                txtGroupLoanNum.setText(CommonUtil.convertObjToStr(hashMap.get("ACT_NUM")));
                observable.getData(hashMap);
                observable.getCustNameForLoan(CommonUtil.convertObjToStr(hashMap.get("ACT_NUM")));
                lblGroupName.setText(observable.getGroupLoanName());
                observable.setTxtGroupName(CommonUtil.convertObjToStr(hashMap.get("GROUP_NAME")));
                observable.setCboProdId(CommonUtil.convertObjToStr(observable.getCbmProdId().getDataForKey(CommonUtil.convertObjToStr(hashMap.get("PROD_ID")))));
                observable.setTxtAccountNo(CommonUtil.convertObjToStr(hashMap.get("ACCOUNT_NO")));
                tblSHGDetails.setModel(observable.getTblSHGDetails());
                //lblODAmount.setText(CurrencyValidation.formatCrore(String.valueOf(observable.setOdAmount(txtGroupLoanNum.getText()))));
                lblODAmount.setText(CommonUtil.convertObjToStr(observable.setOdAmount(txtGroupLoanNum.getText())));
                calcToatal();
            }else if (viewType == "ACCT_NO") {
                    txtAcctNo.setText(CommonUtil.convertObjToStr(hashMap.get("ACCOUNTNO")));
                    observable.setAcctNo(txtAcctNo.getText());
            } else if (viewType == "PROD_ID") {
                    txtTransProductId.setText(CommonUtil.convertObjToStr(hashMap.get("PROD_ID")));
                    observable.setProdID(txtTransProductId.getText());
                    //termLoanPayment();
            }
            if(viewType ==  AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                ClientUtil.enableDisable(this,false);
                ClientUtil.enableDisable(panAccountDetails,false);
            }
            hashMap = null;
            btnCancel.setEnabled(true);
        }catch(Exception e){
            e.printStackTrace();
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
    public void calcToatal(){               // Edit Or Authorize Time
        double totLimit = 0;
        if(tblSHGDetails.getRowCount()>0){
            for (int i=0; i< tblSHGDetails.getRowCount(); i++) {
                totLimit = totLimit + CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(i, 3).toString()).doubleValue();
                System.out.println("tttttttttttttttttttttttttttttt"+totLimit);
                lblTotLimit.setText(String.valueOf(totLimit));
                //lblTotLimit.setText(CurrencyValidation.formatCrore(String.valueOf(totLimit)));
            }
        }
    }
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        callView("Enquiry");
        lblStatus.setText("Enquiry");
        btnDelete.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
    }            //    private void enableDisableAliasBranchTable(boolean flag) {//GEN-LAST:event_btnViewActionPerformed
    
    
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(false);
        lblGroupName.setText("");
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void updateAuthorizeStatus(String authorizeStatus) {
        
        if (viewType == AUTHORIZE && isFilled){
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("ACT_NUM", txtGroupLoanNum.getText());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT",ClientUtil.getCurrentDateWithTime());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap,txtGroupLoanNum.getText());
            viewType = "";
            ClientUtil.enableDisable(panSHGDetails,false);
//            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
               if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();                    
                    newauthorizeListUI.displayDetails("Group loan Customer");
                }
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                    authorizeListUI.setFocusToTable();                    
                    authorizeListUI.displayDetails("Group loan Customer");
                }
                //btnCancelActionPerformed(null);
            }
        }  else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put(CommonConstants.INITIATED_BRANCH, TrueTransactMain.BRANCH_ID);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getGroupAuthorizeCustomer");
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
//            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
//                super.setOpenForEditBy(observable.getStatusBy());
//                super.removeEditLock(id);
//            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        btnSHGNew.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        ClientUtil.clearAll(this);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
        lblStatus.setText("Edit");
        btnSHGNew.setEnabled(true);
//        btnDelete.setEnabled(false);
//        btnNew.setEnabled(false);
//        btnEdit.setEnabled(false);
//        btnDelete.setEnabled(false);
//        btnSave.setEnabled(true);
//        btnView.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panSHGDetails,false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        if(tblSHGDetails.getRowCount()>0){
            savePerformed();
            btnCancel.setEnabled(true);
        }else{
            ClientUtil.displayAlert("Please Save the Details to Grid!!");
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed(){
        observable.doAction();
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
            btnCancelActionPerformed(null);
            btnCancel.setEnabled(true);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
        }
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        setModified(false);
        ClientUtil.clearAll(this);
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        if (fromNewAuthorizeUI) {
            this.dispose();
            fromNewAuthorizeUI = false;
            newauthorizeListUI.setFocusToTable();
        }
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.setFocusToTable();
        }
        viewType = "CANCEL" ;
        lblStatus.setText("               ");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);
        observable.resetForm();
        setModified(false);
        btnNew.setEnabled(true);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        btnCancel.setEnabled(false);
        isFilled = false;
        buttonEnableDisable(false);
        //btnMemberNo.setEnabled(false);
        btnAccountNo.setEnabled(false);
        btnGroupLoan.setEnabled(false);
        txtGroupLoanNum.setEnabled(false);
        txtLimitAmt.setEnabled(false);
        lblGroupName.setText("");
        lblODAmount.setText("");
        lblTotLimit.setText("");
        resetSHGDetails();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
//    private void populateAddressData(HashMap addressMap){
//        lblValStreet.setText(CommonUtil.convertObjToStr(addressMap.get("HOUSE_ST")));
//        lblValArea.setText(CommonUtil.convertObjToStr(addressMap.get("AREA")));
//        lblValCity.setText(CommonUtil.convertObjToStr(addressMap.get("CITY")));
//        lblValState.setText(CommonUtil.convertObjToStr(addressMap.get("STATE")));
//        lblValPin.setText(CommonUtil.convertObjToStr(addressMap.get("PIN")));
//    }
    
    
    private void btnCheck(){
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }
    
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
        //        this.dispose();
    }//GEN-LAST:event_mitCloseActionPerformed

private void txtGroupLoanNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGroupLoanNumActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtGroupLoanNumActionPerformed

private void btnGroupLoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGroupLoanActionPerformed
// TODO add your handling code here:
    viewType = "GROUP_LOAN_NO" ;
    callView("GROUP_LOAN_NO");
    
}//GEN-LAST:event_btnGroupLoanActionPerformed

private void txtCustomerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomerIDActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtCustomerIDActionPerformed

private void txtGroupLoanNumFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGroupLoanNumFocusLost
// TODO add your handling code here:
    if(txtGroupLoanNum.getText().length()>0){
            HashMap whereMap = new HashMap();
            whereMap.put("ACCT_NUM",txtGroupLoanNum.getText());
            List lst=ClientUtil.executeQuery("getGroupLoanCustomerDetails",whereMap);
            if(lst !=null && lst.size()>0){
                viewType = "GROUP_LOAN_NO";
                whereMap=(HashMap)lst.get(0);
                    fillData(whereMap);
                    lst=null;
                    whereMap=null;
                    btnSHGNewActionPerformed(null);
            }else{
                ClientUtil.displayAlert("Invalid Group Loan !!! ");
                txtGroupLoanNum.setText("");
                observable.resetForm();
            }
        }
}//GEN-LAST:event_txtGroupLoanNumFocusLost

private void txtCCNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCCNoFocusLost
// TODO add your handling code here:
    if (txtCCNo.getText().length() > 0 && !observable.validateCreditNo(txtCCNo)) {
            ClientUtil.showMessageWindow("Invalid Credit Card Number...Enter Proper Credit Card Number With atleast one aplpha-numeric!");
            txtCCNo.setText("");
            txtCCNo.isFocusable();
            return;
        }
    if (txtCCNo.getText().length() > 0 && observable.isExist(txtCCNo.getText())) {
            ClientUtil.showMessageWindow("Credit Card already issued........!");
            txtCCNo.setText("");
            txtCCNo.requestFocus();
            return;      
    }
    String ccNo = txtCCNo.getText();
    for(int i=0;i<tblSHGDetails.getRowCount();i++){ 
            if(CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(i, 2)).equals(ccNo)){
                ClientUtil.showMessageWindow("Credit Card already issued........!");
                txtCCNo.setText("");
                txtCCNo.requestFocus();
                return;        
            }
    }
}//GEN-LAST:event_txtCCNoFocusLost

private void txtCCNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCCNoActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtCCNoActionPerformed

private void cboProdTypeCrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeCrActionPerformed
    // TODO add your handling code here:    
}//GEN-LAST:event_cboProdTypeCrActionPerformed

private void txtAcctNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAcctNoActionPerformed

}//GEN-LAST:event_txtAcctNoActionPerformed

private void txtAcctNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcctNoFocusLost
   
}//GEN-LAST:event_txtAcctNoFocusLost

private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
    viewType = "ACCT_NO";
    callView("ACCT_NO");
}//GEN-LAST:event_btnAccNoActionPerformed

private void txtTransProductIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTransProductIdFocusLost
// TODO add your handling code here: 
    if (txtTransProductId.getText() != null && txtTransProductId.getText().length() > 0) {
        observable.setProdID(txtTransProductId.getText());
        //termLoanPayment();
    }
}//GEN-LAST:event_txtTransProductIdFocusLost

private void btnTransProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransProductIdActionPerformed
    callView("PROD_ID");
}//GEN-LAST:event_btnTransProductIdActionPerformed
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnAccountNo;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnGroupLoan;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSHGDelete;
    private com.see.truetransact.uicomponent.CButton btnSHGNew;
    private com.see.truetransact.uicomponent.CButton btnSHGSave;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTransProductId;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CComboBox cboProdTypeCr;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblAccountNo;
    private com.see.truetransact.uicomponent.CLabel lblArea;
    private com.see.truetransact.uicomponent.CLabel lblAreaVal;
    private com.see.truetransact.uicomponent.CLabel lblCCNo;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblCityVal;
    private com.see.truetransact.uicomponent.CLabel lblGroupLoan;
    private com.see.truetransact.uicomponent.CLabel lblGroupName;
    private com.see.truetransact.uicomponent.CLabel lblLimitAmt;
    private com.see.truetransact.uicomponent.CLabel lblMemberName;
    private com.see.truetransact.uicomponent.CLabel lblMemberNameVal;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblODAmount;
    private com.see.truetransact.uicomponent.CLabel lblProdIdCr;
    private com.see.truetransact.uicomponent.CLabel lblProdTypeCr;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStateVal;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStreet;
    private com.see.truetransact.uicomponent.CLabel lblStreetVal;
    private com.see.truetransact.uicomponent.CLabel lblTotLimit;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitException;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitReject;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccHd;
    private com.see.truetransact.uicomponent.CPanel panAccountDetails;
    private com.see.truetransact.uicomponent.CPanel panAccountNo;
    private com.see.truetransact.uicomponent.CPanel panAccountNo1;
    private com.see.truetransact.uicomponent.CPanel panAcctNo;
    private com.see.truetransact.uicomponent.CPanel panDebitAccHead;
    private com.see.truetransact.uicomponent.CPanel panMemberNo;
    private com.see.truetransact.uicomponent.CPanel panSHGBtn;
    private com.see.truetransact.uicomponent.CPanel panSHGDetails;
    private com.see.truetransact.uicomponent.CPanel panSHGRecordDetails;
    private com.see.truetransact.uicomponent.CPanel panSHGTableDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdgEFTProductGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIsLapsedGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPayableBranchGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrintServicesGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSeriesGR;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CScrollPane srpSHGTable;
    private com.see.truetransact.uicomponent.CTabbedPane tabSHGDetails;
    private com.see.truetransact.uicomponent.CTable tblSHGDetails;
    private javax.swing.JToolBar tbrAdvances;
    public static com.see.truetransact.uicomponent.CTextField txtAcctNo;
    private com.see.truetransact.uicomponent.CTextField txtCCNo;
    private com.see.truetransact.uicomponent.CTextField txtCustomerID;
    private com.see.truetransact.uicomponent.CTextField txtGroupLoanNum;
    private com.see.truetransact.uicomponent.CTextField txtLimitAmt;
    private com.see.truetransact.uicomponent.CTextField txtTransProductId;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] arg){
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        GroupLoanCustomerUI gui = new GroupLoanCustomerUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }

}