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

package com.see.truetransact.ui.supporting.depositperiodwisesetting;

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
public class DepositPeriodwiseSettingUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField{
    
    private String viewType = new String();
    private HashMap mandatoryMap;
    private DepositPeriodwiseSettingOB observable;
    final String AUTHORIZE="Authorize";
    private final static Logger log = Logger.getLogger(DepositPeriodwiseSettingUI.class);
    DepositPeriodwiseSettingRB depositPeriodRB = new DepositPeriodwiseSettingRB();
    private String prodType="";
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    String txtid=null;
    int pan=-1;
    //int panEditDelete=-1;
    final int PERIOD=0,AMOUNT=1,LPERIOD=2,LAMOUNT=3,LODPERIOD=4,BDS=5,FP=6;
    /** Creates new form DepositsUI */
    public DepositPeriodwiseSettingUI() {
        initComponents();
        initStartUp();
    }
    
    private void initStartUp(){
        setMandatoryHashMap();
        setFieldNames();
        observable = new DepositPeriodwiseSettingOB();
        observable.addObserver(this);
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
        objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.supporting.depositperiodwisesetting.DepositPeriodwiseSettingMRB", ProxyParameters.LANGUAGE);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panVisitors);
        cboDescription.setModel(observable.getCbmDesc());
    }
    
    private void setMaxLength() {
        
        txtPeriodFrom.setValidation(new NumericValidation());
        txtPeriodTo.setValidation(new NumericValidation());
        txtPriority.setValidation(new NumericValidation());
        txtPeriodFrom.setAllowAll(true);
        txtPeriodTo.setAllowAll(true);
        txtPriority.setAllowAll(true);
        
        txtamountrange.setValidation(new NumericValidation());
        txtfromamount.setValidation(new NumericValidation());
        txttoamount.setValidation(new NumericValidation());
        txtpriority.setValidation(new NumericValidation());
        txtamountrange.setAllowAll(true);
        txtfromamount.setAllowAll(true);
        txttoamount.setAllowAll(true);
        txtpriority.setAllowAll(true);
        
        
        txtPeriodFrom1.setValidation(new NumericValidation());
        txtPeriodTo1.setValidation(new NumericValidation());
        txtPriority1.setValidation(new NumericValidation());
        txtPeriodFrom1.setAllowAll(true);
        txtPeriodTo1.setAllowAll(true);
        txtPriority1.setAllowAll(true);
        
        
        txtamountrange1.setValidation(new NumericValidation());
        txtfromamount1.setValidation(new NumericValidation());
        txttoamount1.setValidation(new NumericValidation());
        txtpriority1.setValidation(new NumericValidation());
        txtamountrange1.setAllowAll(true);
        txtfromamount1.setAllowAll(true);
        txttoamount1.setAllowAll(true);
        txtpriority1.setAllowAll(true);
        
        txtPriority2.setValidation(new NumericValidation());
        txtPeriodFrom2.setValidation(new NumericValidation());
        txtPeriodTo2.setValidation(new NumericValidation());
        txtPriority2.setAllowAll(true);
        txtPeriodFrom2.setAllowAll(true);
        txtPeriodTo2.setAllowAll(true);
        
        
        txtdoubtfrom.setValidation(new NumericValidation());
        txtdoubtto.setValidation(new NumericValidation());
        txtbadfrom.setValidation(new NumericValidation());
        txtbadto.setValidation(new NumericValidation());
        txtdocdoubtfrom.setValidation(new NumericValidation());
        txtdocdoubtto.setValidation(new NumericValidation());
        txtdocbadfrom.setValidation(new NumericValidation());
        txtdocbadto.setValidation(new NumericValidation());
        
        txtdoubtfrom.setAllowAll(true);
        txtdoubtto.setAllowAll(true);
        txtbadfrom.setAllowAll(true);
        txtbadto.setAllowAll(true);
        txtdocdoubtfrom.setAllowAll(true);
        txtdocdoubtto.setAllowAll(true);
        txtdocbadfrom.setAllowAll(true);
        txtdocbadto.setAllowAll(true);
        
        txtPercentage.setValidation(new NumericValidation());
        txtPercentage.setAllowAll(true);
        
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboPeriodType", new Boolean(true));
        mandatoryMap.put("txtPeriodName", new Boolean(true));
        mandatoryMap.put("txtPeriodFrom", new Boolean(true));
        mandatoryMap.put("txtPeriodTo", new Boolean(true));
        mandatoryMap.put("txtPriority", new Boolean(true));
        
        mandatoryMap.put("txtamountrange", new Boolean(true));
        mandatoryMap.put("txtfromamount", new Boolean(true));
        mandatoryMap.put("txttoamount", new Boolean(true));
        mandatoryMap.put("txtpriority", new Boolean(true));
        
        mandatoryMap.put("cboPeriodType1", new Boolean(true));
        mandatoryMap.put("cboPeriodType2", new Boolean(true));
        mandatoryMap.put("txtPeriodName1", new Boolean(true));
        mandatoryMap.put("txtPeriodFrom1", new Boolean(true));
        mandatoryMap.put("txtPeriodTo1", new Boolean(true));
        mandatoryMap.put("txtPriority1", new Boolean(true));
        
        
        
        mandatoryMap.put("txtamountrange1", new Boolean(true));
        mandatoryMap.put("txtfromamount1", new Boolean(true));
        mandatoryMap.put("txttoamount1", new Boolean(true));
        mandatoryMap.put("txtpriority1", new Boolean(true));
        
        
        mandatoryMap.put("cboPeriodType3", new Boolean(true));
        mandatoryMap.put("cboPeriodType4", new Boolean(true));
        mandatoryMap.put("txtPriority2", new Boolean(true));
        mandatoryMap.put("txtdesc", new Boolean(true));
        mandatoryMap.put("txtPeriodFrom2", new Boolean(true));
        mandatoryMap.put("txtPeriodTo2", new Boolean(true));
        
        mandatoryMap.put("txtdoubtfrom", new Boolean(true));
        mandatoryMap.put("txtdoubtto", new Boolean(true));
        mandatoryMap.put("txtbadfrom", new Boolean(true));
        mandatoryMap.put("txtbadto", new Boolean(true));
        mandatoryMap.put("txtdocdoubtfrom", new Boolean(true));
        mandatoryMap.put("txtdocdoubtto", new Boolean(true));
        mandatoryMap.put("txtdocbadfrom", new Boolean(true));
        mandatoryMap.put("txtdocbadto", new Boolean(true));
        mandatoryMap.put("txtdoubtnarra", new Boolean(true));
        mandatoryMap.put("txtbadnarra", new Boolean(true));
        mandatoryMap.put("txtdocdoubtnara", new Boolean(true));
        mandatoryMap.put("txtdocbadnara", new Boolean(true));
        
        mandatoryMap.put("cboFluidType", new Boolean(true));
        mandatoryMap.put("cboModule", new Boolean(true));
        mandatoryMap.put("cboDescription", new Boolean(true));
        mandatoryMap.put("txtPercentage", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
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

        cPopupMenu1 = new com.see.truetransact.uicomponent.CPopupMenu();
        panVisitors6 = new com.see.truetransact.uicomponent.CTabbedPane();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        panVisitors2 = new com.see.truetransact.uicomponent.CPanel();
        lblPeriodName1 = new com.see.truetransact.uicomponent.CLabel();
        lblPeriodFrom1 = new com.see.truetransact.uicomponent.CLabel();
        lblPeriodTo1 = new com.see.truetransact.uicomponent.CLabel();
        lblPriority1 = new com.see.truetransact.uicomponent.CLabel();
        txtPeriodName1 = new com.see.truetransact.uicomponent.CTextField();
        txtPeriodFrom1 = new com.see.truetransact.uicomponent.CTextField();
        txtPeriodTo1 = new com.see.truetransact.uicomponent.CTextField();
        txtPriority1 = new com.see.truetransact.uicomponent.CTextField();
        lblPeriodType1 = new com.see.truetransact.uicomponent.CLabel();
        cboPeriodType1 = new com.see.truetransact.uicomponent.CComboBox();
        cboPeriodType2 = new com.see.truetransact.uicomponent.CComboBox();
        lblPeriodType2 = new com.see.truetransact.uicomponent.CLabel();
        cPanel3 = new com.see.truetransact.uicomponent.CPanel();
        panVisitors3 = new com.see.truetransact.uicomponent.CPanel();
        lblamountrange1 = new com.see.truetransact.uicomponent.CLabel();
        lblfromamount1 = new com.see.truetransact.uicomponent.CLabel();
        lbltoamount1 = new com.see.truetransact.uicomponent.CLabel();
        lblpriority1 = new com.see.truetransact.uicomponent.CLabel();
        txtamountrange1 = new com.see.truetransact.uicomponent.CTextField();
        txtfromamount1 = new com.see.truetransact.uicomponent.CTextField();
        txttoamount1 = new com.see.truetransact.uicomponent.CTextField();
        txtpriority1 = new com.see.truetransact.uicomponent.CTextField();
        panVisitorsDiary = new com.see.truetransact.uicomponent.CPanel();
        panVisitors = new com.see.truetransact.uicomponent.CPanel();
        lblPeriodName = new com.see.truetransact.uicomponent.CLabel();
        lblPeriodFrom = new com.see.truetransact.uicomponent.CLabel();
        lblPeriodTo = new com.see.truetransact.uicomponent.CLabel();
        lblPriority = new com.see.truetransact.uicomponent.CLabel();
        txtPeriodName = new com.see.truetransact.uicomponent.CTextField();
        txtPeriodFrom = new com.see.truetransact.uicomponent.CTextField();
        txtPeriodTo = new com.see.truetransact.uicomponent.CTextField();
        txtPriority = new com.see.truetransact.uicomponent.CTextField();
        lblPeriodType = new com.see.truetransact.uicomponent.CLabel();
        cboPeriodType = new com.see.truetransact.uicomponent.CComboBox();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        panVisitors1 = new com.see.truetransact.uicomponent.CPanel();
        lblamountrange = new com.see.truetransact.uicomponent.CLabel();
        lblfromamount = new com.see.truetransact.uicomponent.CLabel();
        lbltoamount = new com.see.truetransact.uicomponent.CLabel();
        lblpriority = new com.see.truetransact.uicomponent.CLabel();
        txtamountrange = new com.see.truetransact.uicomponent.CTextField();
        txtfromamount = new com.see.truetransact.uicomponent.CTextField();
        txttoamount = new com.see.truetransact.uicomponent.CTextField();
        txtpriority = new com.see.truetransact.uicomponent.CTextField();
        cPanel4 = new com.see.truetransact.uicomponent.CPanel();
        panVisitors4 = new com.see.truetransact.uicomponent.CPanel();
        lblPeriodFrom2 = new com.see.truetransact.uicomponent.CLabel();
        lblPeriodTo2 = new com.see.truetransact.uicomponent.CLabel();
        lblPriority2 = new com.see.truetransact.uicomponent.CLabel();
        txtPeriodFrom2 = new com.see.truetransact.uicomponent.CTextField();
        txtPeriodTo2 = new com.see.truetransact.uicomponent.CTextField();
        txtPriority2 = new com.see.truetransact.uicomponent.CTextField();
        lblPeriodType3 = new com.see.truetransact.uicomponent.CLabel();
        cboPeriodType3 = new com.see.truetransact.uicomponent.CComboBox();
        cboPeriodType4 = new com.see.truetransact.uicomponent.CComboBox();
        lblPeriodType4 = new com.see.truetransact.uicomponent.CLabel();
        lbldesc = new com.see.truetransact.uicomponent.CLabel();
        txtdesc = new com.see.truetransact.uicomponent.CTextField();
        panVisitors5 = new com.see.truetransact.uicomponent.CPanel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        cLabel4 = new com.see.truetransact.uicomponent.CLabel();
        cLabel5 = new com.see.truetransact.uicomponent.CLabel();
        cLabel6 = new com.see.truetransact.uicomponent.CLabel();
        cLabel7 = new com.see.truetransact.uicomponent.CLabel();
        cLabel8 = new com.see.truetransact.uicomponent.CLabel();
        cLabel9 = new com.see.truetransact.uicomponent.CLabel();
        cLabel10 = new com.see.truetransact.uicomponent.CLabel();
        cLabel11 = new com.see.truetransact.uicomponent.CLabel();
        cLabel12 = new com.see.truetransact.uicomponent.CLabel();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        txtdoubtfrom = new com.see.truetransact.uicomponent.CTextField();
        txtdoubtto = new com.see.truetransact.uicomponent.CTextField();
        txtbadfrom = new com.see.truetransact.uicomponent.CTextField();
        txtbadto = new com.see.truetransact.uicomponent.CTextField();
        txtdocdoubtfrom = new com.see.truetransact.uicomponent.CTextField();
        txtdocdoubtto = new com.see.truetransact.uicomponent.CTextField();
        txtdocbadfrom = new com.see.truetransact.uicomponent.CTextField();
        txtdocbadto = new com.see.truetransact.uicomponent.CTextField();
        txtdoubtnarra = new com.see.truetransact.uicomponent.CTextField();
        txtbadnarra = new com.see.truetransact.uicomponent.CTextField();
        txtdocdoubtnara = new com.see.truetransact.uicomponent.CTextField();
        txtdocbadnara = new com.see.truetransact.uicomponent.CTextField();
        cPanel5 = new com.see.truetransact.uicomponent.CPanel();
        lblFluidtype = new com.see.truetransact.uicomponent.CLabel();
        lblDescription = new com.see.truetransact.uicomponent.CLabel();
        lblPercentage = new com.see.truetransact.uicomponent.CLabel();
        txtPercentage = new com.see.truetransact.uicomponent.CTextField();
        cboDescription = new com.see.truetransact.uicomponent.CComboBox();
        cboFluidType = new com.see.truetransact.uicomponent.CComboBox();
        tbrVisitorsDiary = new javax.swing.JToolBar();
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

        panVisitors2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panVisitors2.setMinimumSize(new java.awt.Dimension(600, 400));
        panVisitors2.setName("panMaritalStatus"); // NOI18N
        panVisitors2.setPreferredSize(new java.awt.Dimension(600, 400));
        panVisitors2.setEnabled(false);
        panVisitors2.setLayout(new java.awt.GridBagLayout());

        lblPeriodName1.setText("Period Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors2.add(lblPeriodName1, gridBagConstraints);

        lblPeriodFrom1.setText("Period From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors2.add(lblPeriodFrom1, gridBagConstraints);

        lblPeriodTo1.setText("Period To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors2.add(lblPeriodTo1, gridBagConstraints);

        lblPriority1.setText("Priority");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors2.add(lblPriority1, gridBagConstraints);

        txtPeriodName1.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPeriodName1.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors2.add(txtPeriodName1, gridBagConstraints);

        txtPeriodFrom1.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPeriodFrom1.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors2.add(txtPeriodFrom1, gridBagConstraints);

        txtPeriodTo1.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors2.add(txtPeriodTo1, gridBagConstraints);

        txtPriority1.setAllowNumber(true);
        txtPriority1.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors2.add(txtPriority1, gridBagConstraints);

        lblPeriodType1.setText("From Period Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors2.add(lblPeriodType1, gridBagConstraints);

        cboPeriodType1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "DD", "MM", "YY" }));
        cboPeriodType1.setMaximumSize(new java.awt.Dimension(100, 21));
        cboPeriodType1.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors2.add(cboPeriodType1, gridBagConstraints);

        cboPeriodType2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "DD", "MM", "YY" }));
        cboPeriodType2.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors2.add(cboPeriodType2, gridBagConstraints);

        lblPeriodType2.setText("To Period Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors2.add(lblPeriodType2, gridBagConstraints);

        cPanel2.add(panVisitors2);

        panVisitors6.addTab("Loan Periodwise Setting", cPanel2);

        panVisitors3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panVisitors3.setMinimumSize(new java.awt.Dimension(600, 400));
        panVisitors3.setName("panMaritalStatus"); // NOI18N
        panVisitors3.setPreferredSize(new java.awt.Dimension(600, 400));
        panVisitors3.setEnabled(false);
        panVisitors3.setLayout(new java.awt.GridBagLayout());

        lblamountrange1.setText("Amount Range");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 26;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors3.add(lblamountrange1, gridBagConstraints);

        lblfromamount1.setText("From Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors3.add(lblfromamount1, gridBagConstraints);

        lbltoamount1.setText("To Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors3.add(lbltoamount1, gridBagConstraints);

        lblpriority1.setText("Priority");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 73;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors3.add(lblpriority1, gridBagConstraints);

        txtamountrange1.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors3.add(txtamountrange1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors3.add(txtfromamount1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors3.add(txttoamount1, gridBagConstraints);

        txtpriority1.setAllowNumber(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors3.add(txtpriority1, gridBagConstraints);

        cPanel3.add(panVisitors3);

        panVisitors6.addTab("Loan Amountwise Setting", cPanel3);

        panVisitorsDiary.setMaximumSize(new java.awt.Dimension(600, 520));
        panVisitorsDiary.setMinimumSize(new java.awt.Dimension(650, 520));
        panVisitorsDiary.setPreferredSize(new java.awt.Dimension(650, 520));
        panVisitorsDiary.setLayout(new java.awt.GridBagLayout());

        panVisitors.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panVisitors.setMinimumSize(new java.awt.Dimension(600, 400));
        panVisitors.setName("panMaritalStatus"); // NOI18N
        panVisitors.setPreferredSize(new java.awt.Dimension(600, 400));
        panVisitors.setEnabled(false);
        panVisitors.setLayout(new java.awt.GridBagLayout());

        lblPeriodName.setText("Period Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors.add(lblPeriodName, gridBagConstraints);

        lblPeriodFrom.setText("Period From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors.add(lblPeriodFrom, gridBagConstraints);

        lblPeriodTo.setText("Period To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 48;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors.add(lblPeriodTo, gridBagConstraints);

        lblPriority.setText("Priority");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors.add(lblPriority, gridBagConstraints);

        txtPeriodName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors.add(txtPeriodName, gridBagConstraints);

        txtPeriodFrom.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors.add(txtPeriodFrom, gridBagConstraints);

        txtPeriodTo.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors.add(txtPeriodTo, gridBagConstraints);

        txtPriority.setAllowNumber(true);
        txtPriority.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors.add(txtPriority, gridBagConstraints);

        lblPeriodType.setText("Period Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors.add(lblPeriodType, gridBagConstraints);

        cboPeriodType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Days", "Month", "Year" }));
        cboPeriodType.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors.add(cboPeriodType, gridBagConstraints);

        panVisitorsDiary.add(panVisitors, new java.awt.GridBagConstraints());

        panVisitors6.addTab("Deposit Periodwise Setting", panVisitorsDiary);

        panVisitors1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panVisitors1.setMinimumSize(new java.awt.Dimension(600, 400));
        panVisitors1.setName("panMaritalStatus"); // NOI18N
        panVisitors1.setPreferredSize(new java.awt.Dimension(600, 400));
        panVisitors1.setEnabled(false);
        panVisitors1.setLayout(new java.awt.GridBagLayout());

        lblamountrange.setText("Amount Range");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors1.add(lblamountrange, gridBagConstraints);

        lblfromamount.setText("From Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors1.add(lblfromamount, gridBagConstraints);

        lbltoamount.setText("To Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 48;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors1.add(lbltoamount, gridBagConstraints);

        lblpriority.setText("Priority");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors1.add(lblpriority, gridBagConstraints);

        txtamountrange.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors1.add(txtamountrange, gridBagConstraints);

        txtfromamount.setMaximumSize(new java.awt.Dimension(100, 21));
        txtfromamount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtfromamountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors1.add(txtfromamount, gridBagConstraints);

        txttoamount.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors1.add(txttoamount, gridBagConstraints);

        txtpriority.setAllowNumber(true);
        txtpriority.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors1.add(txtpriority, gridBagConstraints);

        cPanel1.add(panVisitors1);

        panVisitors6.addTab("Deposit Amountwise Setting", cPanel1);

        panVisitors4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panVisitors4.setMinimumSize(new java.awt.Dimension(600, 400));
        panVisitors4.setName("panMaritalStatus"); // NOI18N
        panVisitors4.setPreferredSize(new java.awt.Dimension(600, 400));
        panVisitors4.setEnabled(false);
        panVisitors4.setLayout(new java.awt.GridBagLayout());

        lblPeriodFrom2.setText("Period From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 27;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors4.add(lblPeriodFrom2, gridBagConstraints);

        lblPeriodTo2.setText("Period To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 48;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors4.add(lblPeriodTo2, gridBagConstraints);

        lblPriority2.setText("Priority");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors4.add(lblPriority2, gridBagConstraints);

        txtPeriodFrom2.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPeriodFrom2.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors4.add(txtPeriodFrom2, gridBagConstraints);

        txtPeriodTo2.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors4.add(txtPeriodTo2, gridBagConstraints);

        txtPriority2.setAllowNumber(true);
        txtPriority2.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(9, 34, 1, 0);
        panVisitors4.add(txtPriority2, gridBagConstraints);

        lblPeriodType3.setText("From Period Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 1, 0);
        panVisitors4.add(lblPeriodType3, gridBagConstraints);

        cboPeriodType3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "DD", "MM", "YY" }));
        cboPeriodType3.setMaximumSize(new java.awt.Dimension(100, 21));
        cboPeriodType3.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors4.add(cboPeriodType3, gridBagConstraints);

        cboPeriodType4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "DD", "MM", "YY" }));
        cboPeriodType4.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors4.add(cboPeriodType4, gridBagConstraints);

        lblPeriodType4.setText("To Period Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 17;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors4.add(lblPeriodType4, gridBagConstraints);

        lbldesc.setText("Description ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors4.add(lbldesc, gridBagConstraints);

        txtdesc.setAllowNumber(true);
        txtdesc.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panVisitors4.add(txtdesc, gridBagConstraints);

        cPanel4.add(panVisitors4);

        panVisitors6.addTab("LoanOD Periodwise Setting", cPanel4);

        panVisitors5.setLayout(new java.awt.GridBagLayout());

        cLabel1.setText("Personal Doubtfull From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panVisitors5.add(cLabel1, gridBagConstraints);

        cLabel3.setText("Personal Bad From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panVisitors5.add(cLabel3, gridBagConstraints);

        cLabel4.setText("Personal Bad To ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panVisitors5.add(cLabel4, gridBagConstraints);

        cLabel5.setText("Document Doubtfull From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panVisitors5.add(cLabel5, gridBagConstraints);

        cLabel6.setText("Document Doubtfull To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panVisitors5.add(cLabel6, gridBagConstraints);

        cLabel7.setText("Document Bad From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 0, 0);
        panVisitors5.add(cLabel7, gridBagConstraints);

        cLabel8.setText("Document Bad To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 0, 0);
        panVisitors5.add(cLabel8, gridBagConstraints);

        cLabel9.setText("Personal Doubtfull Narration");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 0, 0);
        panVisitors5.add(cLabel9, gridBagConstraints);

        cLabel10.setText("Personal Bad Narration");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 0, 0);
        panVisitors5.add(cLabel10, gridBagConstraints);

        cLabel11.setText("Document Doubtfull Narration");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 0, 0);
        panVisitors5.add(cLabel11, gridBagConstraints);

        cLabel12.setText("Document Bad Narration  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 0, 0);
        panVisitors5.add(cLabel12, gridBagConstraints);

        cLabel2.setText("Personal Doubtfull To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 13;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panVisitors5.add(cLabel2, gridBagConstraints);

        txtdoubtfrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdoubtfromActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 82;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panVisitors5.add(txtdoubtfrom, gridBagConstraints);

        txtdoubtto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdoubttoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panVisitors5.add(txtdoubtto, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panVisitors5.add(txtbadfrom, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panVisitors5.add(txtbadto, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panVisitors5.add(txtdocdoubtfrom, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panVisitors5.add(txtdocdoubtto, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 84;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panVisitors5.add(txtdocbadfrom, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panVisitors5.add(txtdocbadto, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panVisitors5.add(txtdoubtnarra, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panVisitors5.add(txtbadnarra, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panVisitors5.add(txtdocdoubtnara, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panVisitors5.add(txtdocbadnara, gridBagConstraints);

        panVisitors6.addTab("Bad Doubtfull Setting", panVisitors5);

        cPanel5.setLayout(new java.awt.GridBagLayout());

        lblFluidtype.setText("Fluid Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        cPanel5.add(lblFluidtype, gridBagConstraints);

        lblDescription.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 37;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        cPanel5.add(lblDescription, gridBagConstraints);

        lblPercentage.setText("Percentage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 32;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        cPanel5.add(lblPercentage, gridBagConstraints);

        txtPercentage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPercentageActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        cPanel5.add(txtPercentage, gridBagConstraints);

        cboDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDescriptionActionPerformed(evt);
            }
        });
        cboDescription.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboDescriptionFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        cPanel5.add(cboDescription, gridBagConstraints);

        cboFluidType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Asset", "Liability" }));
        cboFluidType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFluidTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        cPanel5.add(cboFluidType, gridBagConstraints);

        panVisitors6.addTab("Fluid Parameter", cPanel5);

        getContentPane().add(panVisitors6, java.awt.BorderLayout.CENTER);
        panVisitors6.getAccessibleContext().setAccessibleName("Deposit Periodwise Setting");

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

        lblSpace63.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace63.setText("     ");
        lblSpace63.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace63.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace63.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrVisitorsDiary.add(lblSpace63);

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

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrVisitorsDiary.add(btnClose);

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
        mnuProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProcessActionPerformed(evt);
            }
        });

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

    private void cboDescriptionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboDescriptionFocusLost
    }//GEN-LAST:event_cboDescriptionFocusLost

    private void cboDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDescriptionActionPerformed
 
    }//GEN-LAST:event_cboDescriptionActionPerformed
    
    private void cboFluidTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFluidTypeActionPerformed
    
    }//GEN-LAST:event_cboFluidTypeActionPerformed
    
    private void txtPercentageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPercentageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPercentageActionPerformed
    
    private void txtdoubttoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdoubttoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdoubttoActionPerformed
    
    private void txtdoubtfromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdoubtfromActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdoubtfromActionPerformed
    
    private void txtfromamountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtfromamountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtfromamountActionPerformed
    
    private void mnuProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuProcessActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mitCloseActionPerformed(evt);
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
        cifClosingAlert();
    }
    
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
        String mandatoryMessage = "";
        StringBuffer str  =  new StringBuffer();
        if(panVisitors.isShowing()==true){
            pan=PERIOD;
            String mandatoryMessage1 = new MandatoryCheck().checkMandatory(getClass().getName(), panVisitors);
            if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
                displayAlert(mandatoryMessage);
            }
            else{
                savePerformed();
            }
        }else if(panVisitors1.isShowing()==true){
            pan=AMOUNT;
            String mandatoryMessage2 = new MandatoryCheck().checkMandatory(getClass().getName(), panVisitors1);
            if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
                displayAlert(mandatoryMessage);
            }
            else{
                savePerformed();
            }
        }else if(panVisitors2.isShowing()==true){
            pan=LPERIOD;
            String mandatoryMessage3 = new MandatoryCheck().checkMandatory(getClass().getName(), panVisitors2);
            if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
                displayAlert(mandatoryMessage);
            }
            else{
                savePerformed();
            }
        }else if(panVisitors3.isShowing()==true){
            pan=LAMOUNT;
            String mandatoryMessage4 = new MandatoryCheck().checkMandatory(getClass().getName(), panVisitors3);
            if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
                displayAlert(mandatoryMessage);
            }
            else{
                savePerformed();
            }
        }
        else if(panVisitors4.isShowing()==true){
            pan=LODPERIOD;
            String mandatoryMessage5 = new MandatoryCheck().checkMandatory(getClass().getName(), panVisitors4);
            if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
                displayAlert(mandatoryMessage);
            }
            else{
                savePerformed();
            }
        }
        else if(panVisitors5.isShowing()==true){
            pan=BDS;
            String mandatoryMessage6 = new MandatoryCheck().checkMandatory(getClass().getName(), panVisitors5);
            if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
                displayAlert(mandatoryMessage);
            }
            else{
                savePerformed();
            }
        }
        
        else if(cPanel5.isShowing()==true){
            pan=FP;
            String mandatoryMessage7 = new MandatoryCheck().checkMandatory(getClass().getName(), cPanel5);
            if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
                displayAlert(mandatoryMessage);
            }
            else{
                savePerformed();
            }
        }
        
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        enableDisable(true);
        setButtonEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // btnDeleteActionPerformed(evt);
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
    private void popUp(String currAction){
        viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")){
            if(pan == PERIOD){
                HashMap map = new HashMap();
                map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, map);
                viewMap.put(CommonConstants.MAP_NAME, "getDepositPeriodwiseSettingEdit");
            }
            if(pan == AMOUNT){
                HashMap map = new HashMap();
                map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, map);
                viewMap.put(CommonConstants.MAP_NAME, "getDepositAmountwiseSettingEdit");
            }
            if(pan == LPERIOD){
                HashMap map = new HashMap();
                map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, map);
                viewMap.put(CommonConstants.MAP_NAME, "getLoanPeriodwiseSettingEdit");
            }
            if(pan == LAMOUNT){
                HashMap map = new HashMap();
                map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, map);
                viewMap.put(CommonConstants.MAP_NAME, "getLoanAmountwiseSettingEdit");
            }
            if(pan == LODPERIOD){
                HashMap map = new HashMap();
                map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, map);
                viewMap.put(CommonConstants.MAP_NAME, "getLoanODPeriodwiseSettingEdit");
            }
            if(pan == BDS){
                HashMap map = new HashMap();
                map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, map);
                viewMap.put(CommonConstants.MAP_NAME, "getBaddoubtfullSettingEdit");
            }
            if(pan == FP){
                HashMap map = new HashMap();
                map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, map);
                viewMap.put(CommonConstants.MAP_NAME, "getFluidparameterEdit");
            }
        }
//        else if(currAction.equalsIgnoreCase("Delete")){
//            HashMap map = new HashMap();
//            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
//            viewMap.put(CommonConstants.MAP_WHERE, map);
//            viewMap.put(CommonConstants.MAP_NAME, "getDepositPeriodwiseSettingEdit");
//              
//        }
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
                    hash.put(CommonConstants.MAP_WHERE, hash.get("ID"));
                    hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                    if(panVisitors.isShowing()==true) {
                        observable.setPan(0);
                    }
                    else if(panVisitors1.isShowing()==true) {
                        observable.setPan(1);
                    }
                    else if(panVisitors2.isShowing()==true) {
                        observable.setPan(2);
                    }
                    else if(panVisitors3.isShowing()==true) {
                        observable.setPan(3);
                    }
                    else  if(panVisitors4.isShowing()==true) {
                        observable.setPan(4);
                    }
                    else  if(panVisitors5.isShowing()==true) {
                        observable.setPan(5);
                    }
                    else  if(panVisitors6.isShowing()==true) {
                        observable.setPan(6);
                    }
                    observable.getData(hash);
                    txtid=hash.get("ID").toString();
                   
                    if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
                    viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                        ClientUtil.enableDisable(panVisitors, false);
                        
                    } else {
                        ClientUtil.enableDisable(panVisitors, true);
                        
                    }
                    
                    if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
                    viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                        ClientUtil.enableDisable(panVisitors1, false);
                        
                    } else {
                        ClientUtil.enableDisable(panVisitors1, true);
                        
                    }
                    
                    if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
                    viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                        ClientUtil.enableDisable(panVisitors2, false);
                        
                    } else {
                        ClientUtil.enableDisable(panVisitors2, true);
                        
                    }
                    if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
                    viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                        ClientUtil.enableDisable(panVisitors3, false);
                        
                    } else {
                        ClientUtil.enableDisable(panVisitors3, true);
                        
                    }
                    if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
                    viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                        ClientUtil.enableDisable(panVisitors4, false);
                        
                    } else {
                        ClientUtil.enableDisable(panVisitors4, true);
                        
                    }
                    if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
                    viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                        ClientUtil.enableDisable(panVisitors5, false);
                        
                    } else {
                        ClientUtil.enableDisable(panVisitors5, true);
                        
                    }
                    if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
                    viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                        ClientUtil.enableDisable(panVisitors6, false);
                        
                    } else {
                        ClientUtil.enableDisable(panVisitors6, true);
                        
                    }
                    setButtonEnableDisable();
//                    if(viewType ==  AUTHORIZE) {
//                        //  btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
//                        //    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
//                        // btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
//                    }
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
        //btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        // mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        
        //btnAuthorize.setEnabled(btnNew.isEnabled());
        //    btnReject.setEnabled(btnNew.isEnabled());
        // btnException.setEnabled(btnNew.isEnabled());
        // btnView.setEnabled(!btnView.isEnabled());
        
        // txtid.setVisible(false);
    }
    
    // txaInstiNameAddress
    public void update(Observable observed, Object arg) {
        
        if(pan == PERIOD){
            txtid=(observable.getTxtid());
            cboPeriodType.setSelectedItem(observable.getcboPeriodType());
            txtPeriodName.setText(observable.gettxtPeriodName());
            txtPeriodFrom.setText(observable.gettxtPeriodFrom());
            txtPeriodTo.setText(observable.gettxtPeriodTo());
            txtPriority.setText(observable.gettxtPriority());
            lblStatus.setText(observable.getLblStatus());
        }
        else if(pan == AMOUNT){
            txtid=(observable.getTxtID());
            txtamountrange.setText(observable.gettxtamountrange());
            txtfromamount.setText(observable.gettxtfromamount());
            txttoamount.setText(observable.gettxttoamount());
            txtpriority.setText(observable.gettxtpriority());
            lblStatus.setText(observable.getLblStatus());
        }
        
        else if(pan == LPERIOD){
            txtid=(observable.getTxtid());
            cboPeriodType1.setSelectedItem(observable.getcboPeriodType1());
            cboPeriodType2.setSelectedItem(observable.getcboPeriodType2());
            txtPeriodName1.setText(observable.gettxtPeriodName1());
            txtPeriodFrom1.setText(observable.gettxtPeriodFrom1());
            txtPeriodTo1.setText(observable.gettxtPeriodTo1());
            txtPriority1.setText(observable.gettxtPriority1());
            lblStatus.setText(observable.getLblStatus());
        }
        
        else if(pan == LAMOUNT){
            txtid=(observable.getTxtID());
            txtamountrange1.setText(observable.gettxtamountrange1());
            txtfromamount1.setText(observable.gettxtfromamount1());
            txttoamount1.setText(observable.gettxttoamount1());
            txtpriority1.setText(observable.gettxtpriority1());
            lblStatus.setText(observable.getLblStatus());
        }
        
        else if(pan == LODPERIOD){
            txtid=(observable.getTxtid());
            cboPeriodType3.setSelectedItem(observable.getcboPeriodType3());
            cboPeriodType4.setSelectedItem(observable.getcboPeriodType4());
            txtPriority2.setText(observable.gettxtPriority2());
            txtdesc.setText(observable.gettxtdesc());
            txtPeriodFrom2.setText(observable.gettxtPeriodFrom2());
            txtPeriodTo2.setText(observable.gettxtPeriodTo2());
            lblStatus.setText(observable.getLblStatus());
        }
        
        else if(pan == BDS){
            txtid=(observable.getTxtid());
            txtdoubtfrom.setText(observable.gettxtDoubtfrom());
            txtdoubtto.setText(observable.gettxtDoubtto());
            txtbadfrom.setText(observable.gettxtBadfrom());
            txtbadto.setText(observable.gettxtBadto());
            txtdocdoubtfrom.setText(observable.gettxtDocdoubtfrom());
            txtdocdoubtto.setText(observable.gettxtDocdoubtto());
            txtdocbadfrom.setText(observable.gettxtDocbadfrom());
            txtdocbadto.setText(observable.gettxtDocbadto());
            txtdoubtnarra.setText(observable.gettxtDoubtnarra());
            txtbadnarra.setText(observable.gettxtBadnarra());
            txtdocdoubtnara.setText(observable.gettxtDocdoubtnara());
            txtdocbadnara.setText(observable.gettxtDocbadnara());
            lblStatus.setText(observable.getLblStatus());
        }
        else if(pan == FP){
            txtid=(observable.getTxtid());
            if(observable.getcboFluidType().equals("A"))
            {
               cboFluidType.setSelectedIndex(1); 
            }
            else if(observable.getcboFluidType().equals("L"))
            {
              cboFluidType.setSelectedIndex(2); 
            }
            cboDescription.setSelectedItem(observable.getcboDescription());
            txtPercentage.setText(observable.gettxtPercentage());
            lblStatus.setText(observable.getLblStatus());
        }
    }
    
    public void updateOBFields() {
        
        if(pan == PERIOD){
            observable.setcboPeriodType((String)cboPeriodType.getSelectedItem());
            observable. settxtPeriodName(txtPeriodName.getText());
            observable.settxtPeriodFrom(txtPeriodFrom.getText());
            observable.settxtPeriodTo(txtPeriodTo.getText());
            observable.settxtPriority(txtPriority.getText());
            observable.setTxtid(txtid);
            observable.setPan(0);
        }
        else if(pan == AMOUNT){
            observable.settxtamountrange(txtamountrange.getText());
            observable.settxtfromamount(txtfromamount.getText());
            observable.settxttoamount(txttoamount.getText());
            observable.settxtpriority(txtpriority.getText());
            observable.setTxtID(txtid);
            observable.setPan(1);
        }
        else if(pan == LPERIOD){
            observable.setcboPeriodType1((String)cboPeriodType1.getSelectedItem());
            observable.setcboPeriodType2((String)cboPeriodType2.getSelectedItem());
            observable. settxtPeriodName1(txtPeriodName1.getText());
            observable.settxtPeriodFrom1(txtPeriodFrom1.getText());
            observable.settxtPeriodTo1(txtPeriodTo1.getText());
            observable.settxtPriority1(txtPriority1.getText());
            observable.setTxtid1(txtid);
            observable.setPan(2);
        }
        
        else if(pan == LAMOUNT){
            observable.settxtamountrange1(txtamountrange1.getText());
            observable.settxtfromamount1(txtfromamount1.getText());
            observable.settxttoamount1(txttoamount1.getText());
            observable.settxtpriority1(txtpriority1.getText());
            observable.setTxtID1(txtid);
            observable.setPan(3);
        }
        else if(pan == LODPERIOD){
            observable.setcboPeriodType3((String)cboPeriodType3.getSelectedItem());
            observable.setcboPeriodType4((String)cboPeriodType4.getSelectedItem());
            observable. settxtPriority2(txtPriority2.getText());
            observable.settxtdesc(txtdesc.getText());
            observable.settxtPeriodFrom2(txtPeriodFrom2.getText());
            observable.settxtPeriodTo2(txtPeriodTo2.getText());
            observable.setTxtid1(txtid);
            observable.setPan(4);
        }
        
        else if(pan == BDS){
            observable.settxtDoubtfrom(txtdoubtfrom.getText());
            observable.settxtDoubtto(txtdoubtto.getText());
            observable.settxtBadfrom(txtbadfrom.getText());
            observable.settxtBadto(txtbadto.getText());
            observable.settxtDocdoubtfrom(txtdocdoubtfrom.getText());
            observable.settxtDocdoubtto(txtdocdoubtto.getText());
            observable.settxtDocbadfrom(txtdocbadfrom.getText());
            observable.settxtDocbadto(txtdocbadto.getText());
            observable.settxtDoubtnarra(txtdoubtnarra.getText());
            observable.settxtBadnarra(txtbadnarra.getText());
            observable.settxtDocdoubtnara(txtdocdoubtnara.getText());
            observable.settxtDocbadnara(txtdocbadnara.getText());
            observable.setTxtid5(txtid);
            observable.setPan(5);
        }
        else if(pan == FP){
            if(cboFluidType.getSelectedIndex()==1)
            {
             observable.setcboFluidType("A");   
            }
            
            else if(cboFluidType.getSelectedIndex()==2)
            {
              observable.setcboFluidType("L");  
            }
            observable.settxtModule(CommonUtil.convertObjToStr(((ComboBoxModel) cboDescription.getModel()).getKeyForSelected()));
            observable.setcboDescription((String)cboDescription.getSelectedItem());
            observable.settxtPercentage(txtPercentage.getText());
            observable.setTxtid6(txtid);
            observable.setPan(6);
        }
        
    }
    
    private void savePerformed(){
        
        updateOBFields();
        observable.doAction(pan);
        if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
                //   lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("ID")) {
                        //lockMap.put("EMP_TRANSFER_ID",observable.getProxyReturnMap().get("VISIT_ID"));
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
    }
    
    
    
    private void setFieldNames() {
        
        txtPeriodName.setName("txtPeriodName");
        txtPeriodFrom.setName("txtPeriodFrom");
        
        txtPeriodTo.setName("txtPeriodTo");
        txtPriority.setName("txtPriority");
        cboPeriodType.setName("cboPeriodType");
        
        
        txtPeriodName1.setName("txtPeriodName1");
        txtPeriodFrom1.setName("txtPeriodFrom1");
        
        txtPeriodTo1.setName("txtPeriodTo1");
        txtPriority1.setName("txtPriority1");
        cboPeriodType1.setName("cboPeriodType1");
        cboPeriodType2.setName("cboPeriodType2");
        
        cboPeriodType3.setName("cboPeriodType3");
        cboPeriodType4.setName("cboPeriodType4");
        txtPriority2.setName("txtPriority2");
        txtdesc.setName("txtdesc");
        txtPeriodFrom2.setName("txtPeriodFrom2");
        txtPeriodTo2.setName("txtPeriodTo2");
        
        lblPeriodName.setName("lblPeriodName");
        lblPeriodFrom.setName("lblPeriodFrom");
        lblPeriodTo.setName("lblPeriodTo");
        lblPriority.setName("lblPriority");
        lblPeriodType.setName("lblPeriodType");
        
        lblPeriodName1.setName("lblPeriodName1");
        lblPeriodFrom1.setName("lblPeriodFrom1");
        lblPeriodTo1.setName("lblPeriodTo1");
        lblPriority1.setName("lblPriority1");
        lblPeriodType1.setName("lblPeriodType1");
        lblPeriodType2.setName("lblPeriodType2");
        
        
        lblamountrange.setName("lblamountrange");
        lblfromamount.setName("lblfromamount");
        lbltoamount.setName("lbltoamount");
        lblpriority.setName("lblpriority");
        
        lblPriority2.setName("lblPriority2");
        lbldesc.setName("lbldesc");
        lblPeriodType3.setName("lblPeriodType3");
        lblPeriodFrom2.setName("lblPeriodFrom2");
        lblPeriodType4.setName("lblPeriodType4");
        lblPeriodTo2.setName("lblPeriodTo2");
        
        
        cLabel1.setName("cLabel1");
        txtdoubtfrom.setName("txtdoubtfrom");
        cLabel2.setName("cLabel2");
        txtdoubtto.setName("cLabel2");
        cLabel3.setName("cLabel3");
        txtbadfrom.setName("txtbadfrom");
        cLabel4.setName("cLabel4");
        txtbadto.setName("txtbadto");
        cLabel5.setName("cLabel5");
        txtdocdoubtfrom.setName("txtdocdoubtfrom");
        cLabel6.setName("cLabel6");
        txtdocdoubtto.setName("txtdocdoubtto");
        cLabel7.setName("cLabel7");
        txtdocbadfrom.setName("txtdocbadfrom");
        cLabel8.setName("cLabel8");
        txtdocbadto.setName("txtdocbadto");
        cLabel9.setName("cLabel9");
        txtdoubtnarra.setName("txtdoubtnarra");
        cLabel10.setName("cLabel10");
        txtbadnarra.setName("txtbadnarra");
        cLabel11.setName("cLabel11");
        txtdocdoubtnara.setName("txtdocdoubtnara");
        cLabel12.setName("cLabel12");
        txtdocbadnara.setName("txtdocbadnara");
        
        cboFluidType.setName("cboFluidType");
       // txtModule.setName("txtModule");
        cboDescription.setName("cboDescription");
        txtPercentage.setName("txtPercentage");
        
        lblFluidtype.setName("cboFluidType");
       // lblModule.setName("cboFluidType");
        lblDescription.setName("cboFluidType");
        lblPercentage.setName("cboFluidType");
        
        
        lblamountrange1.setName("lblamountrange1");
        lblfromamount1.setName("lblfromamount1");
        lbltoamount1.setName("lbltoamount1");
        lblpriority1.setName("lblpriority1");
        panVisitorsDiary.setName("panVisitorsDiary");
        panVisitors.setName("panVisitors");
        panVisitors1.setName("panVisitors1");
        panVisitors2.setName("panVisitors2");
        panVisitors3.setName("panVisitors3");
        panVisitors4.setName("panVisitors4");
        panVisitors5.setName("panVisitors5");
        panVisitors6.setName("panVisitors6");
      cPanel5.setName("cPanel5");
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
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel10;
    private com.see.truetransact.uicomponent.CLabel cLabel11;
    private com.see.truetransact.uicomponent.CLabel cLabel12;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CLabel cLabel4;
    private com.see.truetransact.uicomponent.CLabel cLabel5;
    private com.see.truetransact.uicomponent.CLabel cLabel6;
    private com.see.truetransact.uicomponent.CLabel cLabel7;
    private com.see.truetransact.uicomponent.CLabel cLabel8;
    private com.see.truetransact.uicomponent.CLabel cLabel9;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CPanel cPanel3;
    private com.see.truetransact.uicomponent.CPanel cPanel4;
    private com.see.truetransact.uicomponent.CPanel cPanel5;
    private com.see.truetransact.uicomponent.CPopupMenu cPopupMenu1;
    private com.see.truetransact.uicomponent.CComboBox cboDescription;
    private com.see.truetransact.uicomponent.CComboBox cboFluidType;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodType;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodType1;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodType2;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodType3;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodType4;
    private com.see.truetransact.uicomponent.CLabel lblDescription;
    private com.see.truetransact.uicomponent.CLabel lblFluidtype;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPercentage;
    private com.see.truetransact.uicomponent.CLabel lblPeriodFrom;
    private com.see.truetransact.uicomponent.CLabel lblPeriodFrom1;
    private com.see.truetransact.uicomponent.CLabel lblPeriodFrom2;
    private com.see.truetransact.uicomponent.CLabel lblPeriodName;
    private com.see.truetransact.uicomponent.CLabel lblPeriodName1;
    private com.see.truetransact.uicomponent.CLabel lblPeriodTo;
    private com.see.truetransact.uicomponent.CLabel lblPeriodTo1;
    private com.see.truetransact.uicomponent.CLabel lblPeriodTo2;
    private com.see.truetransact.uicomponent.CLabel lblPeriodType;
    private com.see.truetransact.uicomponent.CLabel lblPeriodType1;
    private com.see.truetransact.uicomponent.CLabel lblPeriodType2;
    private com.see.truetransact.uicomponent.CLabel lblPeriodType3;
    private com.see.truetransact.uicomponent.CLabel lblPeriodType4;
    private com.see.truetransact.uicomponent.CLabel lblPriority;
    private com.see.truetransact.uicomponent.CLabel lblPriority1;
    private com.see.truetransact.uicomponent.CLabel lblPriority2;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace62;
    private com.see.truetransact.uicomponent.CLabel lblSpace63;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblamountrange;
    private com.see.truetransact.uicomponent.CLabel lblamountrange1;
    private com.see.truetransact.uicomponent.CLabel lbldesc;
    private com.see.truetransact.uicomponent.CLabel lblfromamount;
    private com.see.truetransact.uicomponent.CLabel lblfromamount1;
    private com.see.truetransact.uicomponent.CLabel lblpriority;
    private com.see.truetransact.uicomponent.CLabel lblpriority1;
    private com.see.truetransact.uicomponent.CLabel lbltoamount;
    private com.see.truetransact.uicomponent.CLabel lbltoamount1;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panVisitors;
    private com.see.truetransact.uicomponent.CPanel panVisitors1;
    private com.see.truetransact.uicomponent.CPanel panVisitors2;
    private com.see.truetransact.uicomponent.CPanel panVisitors3;
    private com.see.truetransact.uicomponent.CPanel panVisitors4;
    private com.see.truetransact.uicomponent.CPanel panVisitors5;
    private com.see.truetransact.uicomponent.CTabbedPane panVisitors6;
    private com.see.truetransact.uicomponent.CPanel panVisitorsDiary;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrVisitorsDiary;
    private com.see.truetransact.uicomponent.CTextField txtPercentage;
    private com.see.truetransact.uicomponent.CTextField txtPeriodFrom;
    private com.see.truetransact.uicomponent.CTextField txtPeriodFrom1;
    private com.see.truetransact.uicomponent.CTextField txtPeriodFrom2;
    private com.see.truetransact.uicomponent.CTextField txtPeriodName;
    private com.see.truetransact.uicomponent.CTextField txtPeriodName1;
    private com.see.truetransact.uicomponent.CTextField txtPeriodTo;
    private com.see.truetransact.uicomponent.CTextField txtPeriodTo1;
    private com.see.truetransact.uicomponent.CTextField txtPeriodTo2;
    private com.see.truetransact.uicomponent.CTextField txtPriority;
    private com.see.truetransact.uicomponent.CTextField txtPriority1;
    private com.see.truetransact.uicomponent.CTextField txtPriority2;
    private com.see.truetransact.uicomponent.CTextField txtamountrange;
    private com.see.truetransact.uicomponent.CTextField txtamountrange1;
    private com.see.truetransact.uicomponent.CTextField txtbadfrom;
    private com.see.truetransact.uicomponent.CTextField txtbadnarra;
    private com.see.truetransact.uicomponent.CTextField txtbadto;
    private com.see.truetransact.uicomponent.CTextField txtdesc;
    private com.see.truetransact.uicomponent.CTextField txtdocbadfrom;
    private com.see.truetransact.uicomponent.CTextField txtdocbadnara;
    private com.see.truetransact.uicomponent.CTextField txtdocbadto;
    private com.see.truetransact.uicomponent.CTextField txtdocdoubtfrom;
    private com.see.truetransact.uicomponent.CTextField txtdocdoubtnara;
    private com.see.truetransact.uicomponent.CTextField txtdocdoubtto;
    private com.see.truetransact.uicomponent.CTextField txtdoubtfrom;
    private com.see.truetransact.uicomponent.CTextField txtdoubtnarra;
    private com.see.truetransact.uicomponent.CTextField txtdoubtto;
    private com.see.truetransact.uicomponent.CTextField txtfromamount;
    private com.see.truetransact.uicomponent.CTextField txtfromamount1;
    private com.see.truetransact.uicomponent.CTextField txtpriority;
    private com.see.truetransact.uicomponent.CTextField txtpriority1;
    private com.see.truetransact.uicomponent.CTextField txttoamount;
    private com.see.truetransact.uicomponent.CTextField txttoamount1;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
        DepositPeriodwiseSettingUI deposits = new DepositPeriodwiseSettingUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(deposits);
        j.show();
        deposits.show();
    }
}

