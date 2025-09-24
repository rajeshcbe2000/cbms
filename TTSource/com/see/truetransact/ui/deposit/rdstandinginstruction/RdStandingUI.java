/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RdStandingUI.java
 *
 * Created on November 26, 2003, 11:27 AM
 */
package com.see.truetransact.ui.deposit.rdstandinginstruction;
import com.see.truetransact.ui.transaction.multipleStanding.*;

import com.see.truetransact.ui.payroll.payMaster.*;

import com.see.truetransact.ui.termloan.groupLoan.*;
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
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.deposit.CommonMethods;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uicomponent.CInternalFrame;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.*;
import javax.swing.event.*;

/**
 *
 * @author sreeKrishnan
 *
 **/
public class RdStandingUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {

    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.payroll.payMaster.PayRollIndividualMRB", ProxyParameters.LANGUAGE);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String viewType = new String();
    RdStandingOB observable = null;
    final String AUTHORIZE = "Authorize";
    HashMap mandatoryMap = null;
    boolean isFilled = false;
    private HashMap productMap = new HashMap();
    private boolean updateMode = false;
    int updateTab = -1;
    double odAmount = 0.0;
    Date currDt = null;
    String cust_Id = "";
    private TableModelListener tableModelListener;
    private List finalList = null;
    //private EmployeeDetailsUI employeeDetailsUI = null;

    /** Creates new form BeanForm */
    public RdStandingUI() {
        initComponents();
        initForm();
    }

    private void initForm() {
        setFieldNames();
        internationalize();
        setObservable();
        setMaxLengths();
        initComponentData();
        setButtonEnableDisable();
        buttonEnableDisable(false);
        setMandatoryHashMap();
        ClientUtil.enableDisable(panTransaction, false);
        currDt = ClientUtil.getCurrentDate();
        //tabSHGDetails.remove(panTransaction);
        //employeeDetailsUI = new EmployeeDetailsUI(panEmpDetails);
        btnAuthorize.setEnabled(false);
        addRadioButtons();

    }

    private void setObservable() {
        try {
            observable = new RdStandingOB();
            observable.addObserver(this);
            cboAgentId.setModel(observable.getCbmAgentType());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnView.setName("btnView");
        lblMsg.setName("lblMsg");
        lblSpace.setName("lblSpace");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        lblStatus.setName("lblStatus");
        mbrMain.setName("mbrMain");
        panStatus.setName("panStatus");
        tabSHGDetails.setName("tabSHGDetails");
    }

    private void setMaxLengths() {
        //txtAmount.setValidation(new CurrencyValidation(13, 2));
    }

    private void updateCustomerInfo() {
        if (observable.getTxtEmployeeId() != null && !observable.getTxtEmployeeId().equals("")) {
            // employeeDetailsUI.updateCustomerInfo(observable.getTxtEmployeeId());
        }
    }

    /* Auto Generated Method - internationalize()
    This method used to assign display texts from
    the Resource Bundle File. */
    private void internationalize() {
        resourceBundle = new MultipleStandingRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        //lblEmployeeID.setText(resourceBundle.getString("lblEmployeeID"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        //btnSHGSave.setText(resourceBundle.getString("btnSHGSave"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnView.setText(resourceBundle.getString("btnView"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        //btnSHGNew.setText(resourceBundle.getString("btnSHGNew"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace6.setText(resourceBundle.getString("lblSpace6"));
        //btnSHGDelete.setText(resourceBundle.getString("btnSHGDelete"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }

    /* Auto Generated Method - setMandatoryHashMap()
    
    ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
    
    This method list out all the Input Fields available in the UI.
    It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboPayCodes", new Boolean(true));
        mandatoryMap.put("cboProdTypeCr", new Boolean(true));
        mandatoryMap.put("cboAccNo", new Boolean(true));
        mandatoryMap.put("rdoDeduct", new Boolean(true));
        mandatoryMap.put("rdoEarnings", new Boolean(true));
        mandatoryMap.put("rdocontra", new Boolean(true));
        mandatoryMap.put("txtEmployeeID", new Boolean(true));
        mandatoryMap.put("txtAcctNo", new Boolean(true));
        mandatoryMap.put("txtTransProductId", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("tdtFromDt", new Boolean(true));
        mandatoryMap.put("txtRecoveryMonth", new Boolean(true));

    }

    /* Auto Generated Method - getMandatoryHashMap()
    Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private void initComponentData() {
        try {
            //tblSHGDetails.setModel(observable.getTblSHGDetails());
            //cboProdTypeCr.setModel(observable.getCbmProdTypCr());
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    private void setMaximumLength() {
    }

    /** Auto Generated Method - update()
     * This method called by Observable. It updates the UI with
     * Observable's data. If needed add/Remove RadioButtons
     * method need to be added.*/
    public void update(Observable observed, Object arg) {
        if (tabSHGDetails.getSelectedIndex() == 0) {
            //txtAmount.setText(CommonUtil.convertObjToStr(observable.getTotalAmount()));
            //cboProdTypeCr.setSelectedItem(observable.getCbmProdTypCr().getDataForKey(CommonUtil.convertObjToStr(observable.getProdType())));
            //txtTransProductId.setText(CommonUtil.convertObjToStr(observable.getProdID()));
            //txtAcctNo.setText(CommonUtil.convertObjToStr(observable.getAcctNo()));
        } else if (tabSHGDetails.getSelectedIndex() == 1) {
            //txtStandingId.setText(observable.getTxtEmployeeId());
            //lblAgentName.setText(observable.getLblEmployeeName());           
        }
    }

    /* Auto Generated Method - updateOBFields()
    This method called by Save option of UI.
    It updates the OB with UI data.*/
    public void updateOBFields() {
        //String PayCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboTransactionType.getModel()).getKeyForSelected());
        //String PayDesc = CommonUtil.convertObjToStr(((ComboBoxModel) cboTransactionType.getModel()).getSelectedItem());
        //observable.setTxtEmployeeId(txtAccountHead.getText());
        //observable.setLblEmployeeName(lblEmployeeName.getText());
        //observable.setTotalAmount(CommonUtil.convertObjToDouble(txtAmount.getText()));
        //observable.setPayCode(PayCode);
        //observable.setPayDescription(PayDesc);
        //observable.setActiveStaus(chkPayStatus.isSelected());
        observable.setPayTrans(false);
//        if (rdoGeneralLedger.isSelected() == true) {
//            observable.setProdType("GL");
//        } else if (rdoOtherAccounts.isSelected() == true) {
        //String ProdType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected());
        //String ProdId = txtTransProductId.getText();
        //observable.setProdType(ProdType);
        //observable.setProdID(ProdId);
        //observable.setAcctNo(txtAcctNo.getText());
        //observable.setParticulars(txtParticulars.getText());
//        }
//        if (rdoEarnings.isSelected() == true) {
//            observable.setPaycodeType("EARNINGS");
//        } else if (rdocontra.isSelected() == true) {
//            observable.setPaycodeType("CONTRA");
//        } else {
//            observable.setPaycodeType("DEDUCTIONS");
//        }
        //observable.setTdtFromDate(tdtFromDt.getDateValue());
        //observable.setRecoveryMonth(CommonUtil.convertObjToInt(txtRecoveryMonth.getText()));
    }

    public void updateTransOBFields() {
        //String transType = CommonUtil.convertObjToStr(((ComboBoxModel) cboTransactionType.getModel()).getSelectedItem());
        //observable.setTransType(transType);
        observable.setFinalTotalAmount(CommonUtil.convertObjToDouble(lblTotalValue.getText()));
    }

    private void addRadioButtons() {
        //---Account---
        //rdoButtonGroup = new CButtonGroup();
        //rdoButtonGroup.add(rdoGeneralLedger);
        // rdoButtonGroup.add(rdoOtherAccounts);

        rdoPayCodeGrp = new CButtonGroup();
//        rdoPayCodeGrp.add(rdoEarnings);
//        rdoPayCodeGrp.add(rdoDeduct);
//        rdoPayCodeGrp.add(rdocontra);

        rdoTransTypeGrp = new CButtonGroup();
        //rdoTransTypeGrp.add(rdoReceipt);
        //rdoTransTypeGrp.add(rdoPayment);

        rdoTransPaycodeGrp = new CButtonGroup();
        //rdoTransPaycodeGrp.add(rdoEarningsTrans);
        //rdoTransPaycodeGrp.add(rdoDeductTrans);
        //rdoTransPaycodeGrp.add(rdocontratrans);

        rdoTransAccountType = new CButtonGroup();
        //rdoTransAccountType.add(rdoGeneralLedgertrans);
        //rdoTransAccountType.add(rdoOtherAccountsTrans);


    }

    private void removeRadioButtons() {
        //rdoButtonGroup.remove(rdoGeneralLedger);
        //rdoButtonGroup.remove(rdoOtherAccounts);
//        rdoPayCodeGrp.remove(rdoEarnings);
//        rdoPayCodeGrp.remove(rdoDeduct);
//        rdoPayCodeGrp.remove(rdocontra);
//        rdoTransTypeGrp.remove(rdoReceipt);
//        rdoTransTypeGrp.remove(rdoPayment);
//        rdoTransPaycodeGrp.remove(rdoEarningsTrans);
//        rdoTransPaycodeGrp.remove(rdoDeductTrans);
//        rdoTransPaycodeGrp.remove(rdocontratrans);
//        rdoTransAccountType.remove(rdoGeneralLedgertrans);
//        rdoTransAccountType.remove(rdoOtherAccountsTrans);
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
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        //btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }

    private void buttonEnableDisable(boolean flag) {
        //btnSHGNew.setEnabled(flag);
        //btnSHGSave.setEnabled(flag);
        //btnSHGDelete.setEnabled(flag);
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
        radioButtonGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoButtonGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPayCodeGrp = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoTransTypeGrp = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoTransPaycodeGrp = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoTransAccountType = new com.see.truetransact.uicomponent.CButtonGroup();
        btnGrpDrCr = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace51 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace52 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace53 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace54 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace55 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabSHGDetails = new com.see.truetransact.uicomponent.CTabbedPane();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        panTransactionTrans = new com.see.truetransact.uicomponent.CPanel();
        srpStandingAccounts = new com.see.truetransact.uicomponent.CScrollPane();
        tblStandingAccounts = new com.see.truetransact.uicomponent.CTable();
        lblTotal = new com.see.truetransact.uicomponent.CLabel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        lblTotalValue = new com.see.truetransact.uicomponent.CLabel();
        panSHGDetails1 = new com.see.truetransact.uicomponent.CPanel();
        panAgentDetails = new com.see.truetransact.uicomponent.CPanel();
        panAccountNo2 = new com.see.truetransact.uicomponent.CPanel();
        panTransAccountNo = new com.see.truetransact.uicomponent.CPanel();
        lblAgentId = new com.see.truetransact.uicomponent.CLabel();
        cboAgentId = new com.see.truetransact.uicomponent.CComboBox();
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
        setMinimumSize(new java.awt.Dimension(1100, 630));
        setPreferredSize(new java.awt.Dimension(1100, 630));
        getContentPane().setLayout(new java.awt.GridBagLayout());

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

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace52);

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

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace53);

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

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace55);

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

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace56);

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

        panTransaction.setLayout(new java.awt.GridBagLayout());

        panTransactionTrans.setMinimumSize(new java.awt.Dimension(840, 220));
        panTransactionTrans.setPreferredSize(new java.awt.Dimension(840, 280));
        panTransactionTrans.setLayout(new java.awt.GridBagLayout());

        srpStandingAccounts.setMinimumSize(new java.awt.Dimension(800, 200));

        tblStandingAccounts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Prod Type", "Prod Id", "Account No", "Amount"
            }
        ));
        tblStandingAccounts.setPreferredScrollableViewportSize(new java.awt.Dimension(796, 196));
        tblStandingAccounts.setPreferredSize(new java.awt.Dimension(800, 2000));
        tblStandingAccounts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblStandingAccountsMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tblStandingAccountsMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblStandingAccountsMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblStandingAccountsMouseReleased(evt);
            }
        });
        tblStandingAccounts.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblStandingAccountsFocusLost(evt);
            }
        });
        tblStandingAccounts.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblStandingAccountsKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tblStandingAccountsKeyTyped(evt);
            }
        });
        srpStandingAccounts.setViewportView(tblStandingAccounts);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panTransactionTrans.add(srpStandingAccounts, gridBagConstraints);

        lblTotal.setText("Total Amount :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(9, 430, 10, 2);
        panTransactionTrans.add(lblTotal, gridBagConstraints);

        chkSelectAll.setText("Select All");
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTransactionTrans.add(chkSelectAll, gridBagConstraints);

        lblTotalValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblTotalValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 30);
        panTransactionTrans.add(lblTotalValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panTransaction.add(panTransactionTrans, gridBagConstraints);

        panSHGDetails1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSHGDetails1.setMinimumSize(new java.awt.Dimension(840, 230));
        panSHGDetails1.setPreferredSize(new java.awt.Dimension(840, 180));
        panSHGDetails1.setLayout(new java.awt.GridBagLayout());

        panAgentDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Standing Details"));
        panAgentDetails.setMinimumSize(new java.awt.Dimension(375, 50));
        panAgentDetails.setPreferredSize(new java.awt.Dimension(375, 100));
        panAgentDetails.setLayout(new java.awt.GridBagLayout());

        panAccountNo2.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        panAgentDetails.add(panAccountNo2, gridBagConstraints);

        panTransAccountNo.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 12, 4, 12);
        panAgentDetails.add(panTransAccountNo, gridBagConstraints);

        lblAgentId.setText("Agent Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAgentDetails.add(lblAgentId, gridBagConstraints);

        cboAgentId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAgentIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panAgentDetails.add(cboAgentId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSHGDetails1.add(panAgentDetails, gridBagConstraints);
        panAgentDetails.getAccessibleContext().setAccessibleName("Agent Details");

        panTransaction.add(panSHGDetails1, new java.awt.GridBagConstraints());

        tabSHGDetails.addTab("Standing Instruction", panTransaction);

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

    private void resetSHGDetails() {
        //chkPayStatus.setSelected(false);
        //chkPayTransaction.setSelected(false);
        //txtAmount.setText("");
//        rdoGeneralLedger.setSelected(false);
//        rdoOtherAccounts.setSelected(false);
        //txtAcctNo.setText("");
        //txtProdIdTrans.setText("");
        //cboTransactionType.setSelectedItem("");
        //cboProdTypeCr.setSelectedItem("");
//        tdtFromDt.setDateValue("");
//        txtRecoveryMonth.setText("");
        //txtAmountTrans.setText("");
        //rdoGeneralLedgertrans.setSelected(false);
        // rdoOtherAccountsTrans.setSelected(false);
        //txtAcctNotrans.setText("");
        //txtTransProductId.setText("");
        //txtParticulars.setText("");
        //cboPayCodesTrans.setSelectedItem("");
        //cboProdTypeCrtrans.setSelectedItem("");
    }

    /** To display a popUp window for viewing existing data */
    private void callView(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")) {
            viewMap.put(CommonConstants.MAP_NAME, "getStandingDetails");
        } else if (currAction.equalsIgnoreCase("Delete")) {
            viewMap.put(CommonConstants.MAP_NAME, "getSHGDelete");
        } else if (currAction.equalsIgnoreCase("Enquiry")) {
            viewMap.put(CommonConstants.MAP_NAME, "getSHGView");
        } else if (viewType.equals("EMPLOYEE_ID")) {
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        } else if (viewType.equals("STANDING_ID")) {
            viewMap.put(CommonConstants.MAP_NAME, "getStandingDetails");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        } else if (viewType.equals("PAY_CODE")) {
            viewMap.put(CommonConstants.MAP_NAME, "getGroupLoanCustomerDetails");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        } else if (viewType.equals("ACCT_NO")) {
            //whereMap.put("PROD_ID", txtTransProductId.getText());
            whereMap.put("SELECTED_BRANCH", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            if (observable.getProdType().equals("TD") || observable.getProdType().equals("TL") || observable.getProdType().equals("AB")) {
                if (observable.getProdType().equals("TL") || observable.getProdType().equals("AB")) {
                    whereMap.put("RECEIPT", "RECEIPT");
                }
                //viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                //        + CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected()));
            } /*else if(CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected()).equals("GL")){
                viewMap.put(CommonConstants.MAP_NAME, "InterMaintenance.getProductData"
                        + CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected()));
            }else{
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                        + CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected()));
            }*/
        } else if (viewType.equals("ACCT_NO_TRANS")) {
            //whereMap.put("PROD_ID", txtProdIdTrans.getText());
            whereMap.put("SELECTED_BRANCH", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            if (observable.getProdType().equals("TD") || observable.getProdType().equals("TL") || observable.getProdType().equals("AB")) {
                if (observable.getProdType().equals("TL") || observable.getProdType().equals("AB")) {
                    whereMap.put("RECEIPT", "RECEIPT");
                }
                //viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                //        + CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCrtrans.getModel()).getKeyForSelected()));
            } else {
                // viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                //        + CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCrtrans.getModel()).getKeyForSelected()));
            }
        } else if (viewType.equals("PROD_ID")) {
            HashMap where_map = new HashMap();
            where_map.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            //String prodType = CommonUtil.convertObjToStr((((ComboBoxModel) (cboProdTypeCr).getModel())).getKeyForSelected());
            viewMap.put(CommonConstants.MAP_NAME, "InterMaintenance.getProductData" + CommonUtil.convertObjToStr(observable.getCbmProdTypCr().getKeyForSelected()));
            viewMap.put(CommonConstants.MAP_WHERE, where_map);
        }
        new ViewAll(this, viewMap).show();
    }

    public void initStandingTableData() {
        // model=new javax.swing.table.DefaultTableModel();

        tblStandingAccounts.setModel(new javax.swing.table.DefaultTableModel(
                setTableDailyLoanData(),
                new String[]{
                    "Select", "Prod Type", "Prod Id", "Account No","Installment", "No.of Inst","Inst Amt","Penal","Total Amt"}) {

            Class[] types = new Class[]{
                java.lang.Boolean.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                true, false, false, false,false, true,false,false,false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 0 || columnIndex == 5) {
                    return true;
                }
                return canEdit[columnIndex];
            }
        });
        //setFont();
        //setSizeTableData();
        //setColour();
        //setUpComboBox(tblBalanceUpdate,tblBalanceUpdate.getColumnModel().getColumn(2));
        tblStandingAccounts.setCellSelectionEnabled(true);
        tblStandingAccounts.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //  tblBalanceUpdatePropertyChange(evt);
            }
        });

        setTableModelListener();
        calc();
    }

    public void calc() {
        double totPayment = 0;
        double totAmount = 0;
        if (tblStandingAccounts.getRowCount() > 0) {
            for (int i = 0; i < tblStandingAccounts.getRowCount(); i++) {
                if ((Boolean) tblStandingAccounts.getValueAt(i, 0)) {
                    totPayment = totPayment + CommonUtil.convertObjToDouble(tblStandingAccounts.getValueAt(i, 8).toString()).doubleValue();
                    //totAmount = CommonUtil.convertObjToDouble(tblStandingAccounts.getValueAt(i, 5)).doubleValue()*CommonUtil.convertObjToDouble(tblStandingAccounts.getValueAt(i, 6)).doubleValue();
                    //tblStandingAccounts.setValueAt(totAmount, i, 7); 
                }
            }
            System.out.println("totPayment%#$%#$"+totPayment);
            lblTotalValue.setText((String.valueOf(totPayment)));
        }
    }

    private void setDailyTableFinalList() {
        finalList = observable.getFinalList();
        HashMap custMap = new HashMap();
        if (finalList != null && finalList.size() > 0) {
            System.out.println("#$@$#@$@$@ FinalList : " + finalList);
            for (int i = 0; i < finalList.size(); i++) {
                String custNo = "";
                double loanBal = 0.0, actBal = 0.0;
                custMap = (HashMap) finalList.get(i);
                custNo = CommonUtil.convertObjToStr(custMap.get("DEPOSIT_NO"));
                //System.out.println("$#@@$@$#$@$ custNo : "+custNo);                
                //if(loanBal>0 && actBal>0){
                for (int j = 0; j < tblStandingAccounts.getRowCount(); j++) {
                    if (CommonUtil.convertObjToStr(tblStandingAccounts.getValueAt(j, 3)).equals(custNo)) {
                        if ((!((Boolean) tblStandingAccounts.getValueAt(j, 0)).booleanValue()
                                || CommonUtil.convertObjToDouble(tblStandingAccounts.getValueAt(j, 5)) == 0
                                || CommonUtil.convertObjToStr(tblStandingAccounts.getValueAt(j, 5)).equals(""))) {
                            finalList.remove(i--);
                        } else {
                            custMap.put("DEPOSIT_PENAL_AMT", tblStandingAccounts.getValueAt(j, 7));
                            custMap.put("TOTAL_AMOUNT", tblStandingAccounts.getValueAt(j, 8));
                        }
                    }
                }
                //}                
            }
            System.out.println("#$########## custMap daily: " + custMap);
            System.out.println("#$########## finalList : daily" + finalList);
            if(!(finalList!=null && finalList.size()>0)){
                ClientUtil.showAlertWindow("Transaction List is Empty!! Please check transaction amounts!!");
                return;
            }else{
                observable.setFinalList(finalList);
            }
        }
    }

    private void setTableModelListener() {
        try {
            tableModelListener = new TableModelListener() {

                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        int row = e.getFirstRow();
                        int column = e.getColumn();
                        int RowCount = tblStandingAccounts.getRowCount();
                        System.out.println("row#####" + row);
                        System.out.println("column#####" + column);
                        if (column == 5 || column == 0) {
                            if (tblStandingAccounts.getValueAt(row, 5) != null && !tblStandingAccounts.getValueAt(row, 5).toString().equals("") && !isNumeric(tblStandingAccounts.getValueAt(row, 5).toString())) {
                                System.out.println("4356346436346====");
                                ClientUtil.showAlertWindow("Please enter Numeric Value!!!");
                                tblStandingAccounts.setValueAt("", row, 5);
                                return;
                            }
                            if (tblStandingAccounts.getValueAt(row, 5) != null && !tblStandingAccounts.getValueAt(row, 5).toString().equals("") && isNegative(CommonUtil.convertObjToDouble(tblStandingAccounts.getValueAt(row, 5)).doubleValue())) {
                                System.out.println("4356346436346====");
                                ClientUtil.showAlertWindow("Negative value not allowed!!!");
                                tblStandingAccounts.setValueAt("", row, 5);
                                return;
                            } 
                            if (column == 5) {
                                tblStandingAccounts.setValueAt(getPendingPenal(CommonUtil.convertObjToStr(tblStandingAccounts.getValueAt(row, 3)),CommonUtil.convertObjToDouble(tblStandingAccounts.getValueAt(row, 5)).doubleValue()), row,7 );
                                double penal =CommonUtil.convertObjToDouble(tblStandingAccounts.getValueAt(row, 7)).doubleValue();
                                double depAmt = CommonUtil.convertObjToDouble(tblStandingAccounts.getValueAt(row, 6)).doubleValue();
                                double instamt = CommonUtil.convertObjToDouble(tblStandingAccounts.getValueAt(row, 5)).doubleValue();
                                double totalAmt = (depAmt*instamt)+penal;
                                System.out.println("penal#@$#@"+penal+"dep%$%$#%"+depAmt+"inst$@#$2"+instamt+"tto$@#$#@"+totalAmt);
                                tblStandingAccounts.setValueAt(totalAmt, row,8);
                            }
                            
                            calc();
                        }
                        //if (column == 0) {
                        //    calc();
                        //}
                    }
                }
            };
            tblStandingAccounts.getModel().addTableModelListener(tableModelListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getPendingPenal(String depositNo,double instPay) {
        double penal = 0.0;
        HashMap penalMap = new HashMap();
        penalMap.put("DEPOSIT_NO",depositNo);
        penalMap.put("INST_PAY",instPay);
        penalMap.put("CURR_DT",currDt.clone());
        penalMap.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
        List penalList = ClientUtil.executeQuery("getRdPendingPenal", penalMap);
        if (penalList != null && penalList.size() > 0) {
                penalMap = (HashMap) penalList.get(0);
                penal = CommonUtil.convertObjToDouble(penalMap.get("PENDING_PENAL"));
        }
        return penal;
    }
    
    public static boolean isNegative(double d) {
        return Double.compare(d, 0.0) < 0;
    }

    public static boolean isNumeric(String str) {
        try {
            //Integer.parseInt(str);
            Float.parseFloat(str);
            //   System.out.println("ddd"+d);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private Object[][] setTableDailyLoanData() {
        HashMap whereMap = new HashMap();
        HashMap dataMap = new HashMap();
        whereMap.put("BRANCH_CODE", getSelectedBranchID());
        whereMap.put("CURR_DT", currDt.clone());
        whereMap.put("AGENT_ID", CommonUtil.convertObjToStr(observable.getCbmAgentType().getKeyForSelected()));
        List loanList = ClientUtil.executeQuery("getRdIntallmentDetails", whereMap);
        if (loanList != null && loanList.size() > 0) {
            Object totalList[][] = new Object[loanList.size()][9];
            for (int j = 0; j < loanList.size(); j++) {
                dataMap = (HashMap) loanList.get(j);
                System.out.println("processMap####" + dataMap);
                totalList[j][0] = new Boolean(false);
                totalList[j][1] = CommonUtil.convertObjToStr(dataMap.get("PROD_DESC"));
                totalList[j][2] = CommonUtil.convertObjToStr(dataMap.get("PROD_ID"));
                totalList[j][3] = CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_NO"));
                totalList[j][4] = CommonUtil.convertObjToStr(dataMap.get("INSTALLENTS"));
                totalList[j][5] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(dataMap.get("PENDING")));
                totalList[j][6] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(dataMap.get("INST_AMT")));
                totalList[j][7] = "";
                totalList[j][8] = "";
            }
            observable.setFinalList(loanList);
            return totalList;
        } else {
            ClientUtil.displayAlert("No Data!!! ");

        }
        return null;
    }

    public void fillData(Object obj) {
        try {
            HashMap hashMap = (HashMap) obj;
            if (tabSHGDetails.getSelectedIndex() == 0) {
                isFilled = true;
                List lst1 = null;
                if (viewType == "EMPLOYEE_ID") {
                    //For checking unauthorized transactions..
//                HashMap checkMap = new HashMap();
//                checkMap.put("LINK_BATCH_ID", txtEmployeeID.getText());
//                List checkList = ClientUtil.executeQuery("getPayMasterTransAuthorizeCheck", checkMap);
//                if (checkList.size() > 0) {
//                    ClientUtil.showMessageWindow("Authorization pending for the Employee ID");
//                    txtEmployeeID.setText("");
//                    lblEmployeeName.setText("");
//                    employeeDetailsUI.updateCustomerInfo("");
//                    return;
//                }
                    //txtAccountHead.setText(CommonUtil.convertObjToStr(hashMap.get("A/C HEAD")));
                    //lblEmployeeName.setText(CommonUtil.convertObjToStr(hashMap.get("A/C HEAD DESCRIPTION")));
                    //observable.setTxtEmployeeId(txtAccountHead.getText());
                    //observable.setLblEmployeeName(lblEmployeeName.getText());
                    updateCustomerInfo();
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                        //btnAccountHead.setEnabled(true);
                    }
                    cust_Id = CommonUtil.convertObjToStr(hashMap.get("CUST_ID"));
                } else if (viewType == "ACCT_NO") {
                    //if(CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected()).equals("GL"))
                        //txtAcctNo.setText(CommonUtil.convertObjToStr(hashMap.get("A/C HEAD")));
                    //else
                        //txtAcctNo.setText(CommonUtil.convertObjToStr(hashMap.get("ACCOUNTNO")));
                    //observable.setAcctNo(txtAcctNo.getText());
                } else if (viewType == "PROD_ID") {
                    //txtTransProductId.setText(CommonUtil.convertObjToStr(hashMap.get("PROD_ID")));
                    //observable.setProdID(txtTransProductId.getText());
                    //termLoanPayment();
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    this.setButtonEnableDisable();
                    //txtAccountHead.setText(CommonUtil.convertObjToStr(hashMap.get("ACCOUNT_ID")));
                    //lblEmployeeName.setText(CommonUtil.convertObjToStr(hashMap.get("ACCOUNT_HEAD")));
                    //cboTransactionType.setSelectedItem(CommonUtil.convertObjToStr(hashMap.get("TRANS_TYPE")));
                    //observable.setTxtEmployeeId(txtAccountHead.getText());
                    //observable.setLblEmployeeName(lblEmployeeName.getText());
                    observable.setStandingId(CommonUtil.convertObjToStr(hashMap.get("STANDING_ID")));
                    observable.getData(hashMap);
                    //tblSHGDetails.setModel(observable.getTblSHGDetails());
                    //calcToatal();
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                    this.setButtonEnableDisable();
                    tabSHGDetails.add(panTransaction);
                    panTransaction.setName("Transaction");
                    //transactionUI.addToScreen(panTransactionTrans);
                    //observable.setTransactionOB(transactionUI.getTransactionOB());
                    resetTransactionUI();
                    //txtAccountHead.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEEID")));
                    //lblEmployeeName.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_NAME")));
                    observable.getData(hashMap);
                    hashMap.put("TRANS_DT", currDt);
                    observable.getRemittData(hashMap);
                    calcToatal();
                    //tblSHGDetails.setModel(observable.getTblSHGDetails());
                    //ClientUtil.enableDisable(panSHGRecordDetails, false);
                    //btnSHGNew.setEnabled(false);
                    //btnSHGSave.setEnabled(false);
                    //btnSHGDelete.setEnabled(false);
                }
                if (viewType == AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    ClientUtil.enableDisable(this, false);
                }
                hashMap = null;
                btnCancel.setEnabled(true);
            } else if (tabSHGDetails.getSelectedIndex() == 1) {
                isFilled = true;
                List lst1 = null;
                if (viewType == "EMPLOYEE_ID") {
                    //For checking unauthorized transactions..
//                    HashMap checkMap = new HashMap();
//                    checkMap.put("LINK_BATCH_ID", txtTransEmployeeID.getText());
//                    List checkList = ClientUtil.executeQuery("getPayMasterTransAuthorizeCheck", checkMap);
//                    if (checkList.size() > 0) {
//                        ClientUtil.showMessageWindow("Authorization pending for the Employee ID");
//                        txtTransEmployeeID.setText("");
//                        lblEmployeeNameTrans.setText("");
//                        //employeeDetailsUI.updateCustomerInfo("");
//                        return;
//                    }
                    //txtStandingId.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEEID")));
                    //lblAgentName.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_NAME")));
                    //observable.setTxtEmployeeId(txtStandingId.getText());
                    //observable.setLblEmployeeName(lblAgentName.getText());
                    //updateCustomerInfo();
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    //    btnStandingId.setEnabled(true);
                    }
                    cust_Id = CommonUtil.convertObjToStr(hashMap.get("CUST_ID"));
                } else if (viewType == "STANDING_ID") {
                    //txtStandingId.setText(CommonUtil.convertObjToStr(hashMap.get("STANDING_ID")));
                    //lblAgentName.setText(CommonUtil.convertObjToStr(hashMap.get("ACCOUNT_HEAD")));
                    //cboTransTransactionType.setSelectedItem(CommonUtil.convertObjToStr(hashMap.get("TRANS_TYPE")));
                    observable.setGlAccountHead(CommonUtil.convertObjToStr(hashMap.get("ACCOUNT_ID")));
                    initStandingTableData();
                    //observable.setProdID(txtTransProductId.getText());
                    //termLoanPayment();
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    this.setButtonEnableDisable();
                    //txtStandingId.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEEID")));
                    //lblAgentName.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_NAME")));
                    //observable.setTxtEmployeeId(txtStandingId.getText());
                    //observable.setLblEmployeeName(lblAgentName.getText());
                    observable.getData(hashMap);
                    //tblSHGDetails.setModel(observable.getTblSHGDetails());
                    //calcToatal();
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                    this.setButtonEnableDisable();
                    tabSHGDetails.add(panTransaction);
                    panTransaction.setName("Transaction");
                    //transactionUI.addToScreen(panTransactionTrans);
                    //observable.setTransactionOB(transactionUI.getTransactionOB());
                    resetTransactionUI();
                    //txtStandingId.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEEID")));
                    //lblAgentName.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_NAME")));
                    observable.getData(hashMap);
                    hashMap.put("TRANS_DT", currDt);
                    observable.getRemittData(hashMap);
                    //calcToatal();
                    //tblSHGDetails.setModel(observable.getTblSHGDetails());
                    //ClientUtil.enableDisable(panSHGRecordDetails, false);
                }
                if (viewType == AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    ClientUtil.enableDisable(this, false);
                }
                hashMap = null;
                btnCancel.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ClientUtil.showMessageWindow(e.getMessage());
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }

    public void termLoanPayment() {
        if (observable.getProdID() != null && CommonUtil.convertObjToStr(observable.getCbmProdTypCr().getKeyForSelected()).equals("TL")) {
            CInternalFrame frm = null;
            frm = new com.see.truetransact.ui.termloan.TermLoanUI();
            frm.setSelectedBranchID(getSelectedBranchID());
            TrueTransactMain.showScreen(frm);
            HashMap hash = new HashMap();
            hash.put("PROD_ID", CommonUtil.convertObjToStr(observable.getProdID()));
            hash.put("PROD_TYPE", CommonUtil.convertObjToStr(observable.getCbmProdTypCr().getKeyForSelected()));
            hash.put("CUSTOMER_ID", cust_Id);
            hash.put("FROM_PAYROLL_MASTER", "FROM_PAYROLL_MASTER");
            frm.fillData(hash);
        }
    }

    public void calcToatal() {               // Edit Or Authorize Time
        double netSlary = 0;
        double netEarning = 0;
        double netDeduction = 0;
        /*if (tblSHGDetails.getRowCount() > 0) {
            for (int i = 0; i < tblSHGDetails.getRowCount(); i++) {
                if (CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(i, 2).toString()).equals("EARNINGS")) {
                    netEarning = netEarning + CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(i, 3));
                } else if (CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(i, 2).toString()).equals("DEDUCTIONS")) {
                    netDeduction = netDeduction + CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(i, 3));
                }
                netSlary = netEarning - netDeduction;
            }
        }*/
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
        btnReject.setEnabled(true);
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
        if (tabSHGDetails.getSelectedIndex() == 1) {
            setModified(true);
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
            btnCancel.setEnabled(true);
            btnReject.setEnabled(false);
            btnException.setEnabled(false);
        }
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            //singleAuthorizeMap.put("ACT_NUM", txtAccountHead.getText());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            //authorize(authorizeMap, txtAccountHead.getText());
            viewType = "";
            //ClientUtil.enableDisable(panSHGDetails, false);
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put("TRANS_DT", currDt);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                mapParam.put(CommonConstants.MAP_NAME, "getPayMasterCashierAuthorize");
            } else {
                mapParam.put(CommonConstants.MAP_NAME, "getPayMasterAuthorize");
            }
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    public void authorize(HashMap map, String id) {
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            observable.doAction();
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        if (tabSHGDetails.getSelectedIndex() == 1) {
            btnSHGNewActionPerformed();
            //addRadioButtons();
        } else if (tabSHGDetails.getSelectedIndex() == 0) {
            setModified(true);
            //btnSHGNew.setEnabled(true);
            observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
            setButtonEnableDisable();
            ClientUtil.clearAll(this);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
            //panPaymentTransaction.setVisible(true);
            addRadioButtons();
            //btnAccountHead.setEnabled(true);
            cboAgentId.setEnabled(true);
        }

    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        if (tabSHGDetails.getSelectedIndex() == 0) {
            observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
            callView("Edit");
            lblStatus.setText("Edit");
            //btnSHGNew.setEnabled(true);
            //panPaymentTransaction.setVisible(true);
            //chkPayTransaction.setVisible(false);
            //lblPayTransaction.setVisible(false);
            //btnAccountHead.setEnabled(false);
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        //ClientUtil.enableDisable(panSHGDetails, false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        System.out.println("tab###############" + tabSHGDetails.getSelectedIndex());
        try {
            if (tabSHGDetails.getSelectedIndex() == 1) {
                String message = checkMandatoryTransData();
                if (message.trim().length() > 0) {
                    CommonMethods.displayAlert(message);
                    return;
                } else {
                    observable.setGetSelectedTab("TRANSACTION_DATA");
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                        updateTransOBFields();
                        savePerformed();
                    } else {
                        //savePerformed();
                    }
                }
            } else if (tabSHGDetails.getSelectedIndex() == 0) {
                observable.setGetSelectedTab("MASTER_DATA");
                savePerformed();
            }
            setModified(false); 
            btnCancelActionPerformed(null);
        } catch (Exception e) {
            e.printStackTrace();
            ClientUtil.showMessageWindow(e.getMessage());
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed() {
        //if (tabSHGDetails.getSelectedIndex() == 0) {
            setDailyTableFinalList();            
        //}
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            System.out.println("#$@#$$@# getProxyReturnMap " + observable.getProxyReturnMap());
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                //ClientUtil.showMessageWindow("Transaction Completed..Batch Id : " +observable.getProxyReturnMap().get("BATCH_ID"));
                displayTransDetailNew(observable.getProxyReturnMap());
            } 
            //if(tabSHGDetails.getSelectedIndex()==0) {
            //    ClientUtil.showMessageWindow("Save succesfully..!!");
            //}
            btnCancelActionPerformed(null);
            btnCancel.setEnabled(true);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
        }
        btnCancel.setEnabled(true);
        //btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        setModified(false);
        ClientUtil.clearAll(this);
    }

    public void displayTransDetailNew(HashMap returnMap) {
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
        Object keys[] = returnMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        HashMap transIdMap = new HashMap();
        HashMap transTypeMap = new HashMap();
        /*for (int i = 0; i < keys.length; i++) {
            if (returnMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) returnMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transTypeMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                    }
                    break;
                    //transferDisplayStr = " Batch Id : " + transMap.get("BATCH_ID");
                    //actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    //if (actNum != null && !actNum.equals("")) {
                    //   transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                    //           + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    //} else {
                    //    transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                    //          + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    // }
                    //transIdMap.put(transMap.get("BATCH_ID"), "TRANSFER");
                }
                transferDisplayStr += "   Batch Id : " + transMap.get("BATCH_ID");
                transferCount++;
            }
        }*/
        /*if (cashCount > 0) {
            displayStr += cashDisplayStr;
        }
        if (transferCount > 0) {
            displayStr += transferDisplayStr;
        }
        if (!displayStr.equals("")) {
            ClientUtil.showMessageWindow("" + displayStr);
        }*/
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Save succesfully ,Do you want to print?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        if (yesNo == 0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("TransId", CommonUtil.convertObjToStr(returnMap.get("SINGLE_TRANS_ID")));
            paramMap.put("TransDt", currDt.clone());
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            ttIntgration.setParam(paramMap);
            ttIntgration.integrationForPrint("CashReceipt", false);
        }
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        viewType = "CANCEL";
        lblStatus.setText("");
//        rdoDeduct.setSelected(false);
//        rdoEarnings.setSelected(false);
//        rdocontra.setSelected(false);
//        rdoGeneralLedger.setSelected(false);
//        rdoOtherAccounts.setSelected(false);
//        rdoGeneralLedgertrans.setSelected(false);
//        rdoOtherAccountsTrans.setSelected(false);
//        rdoPayment.setSelected(false);
//        rdoReceipt.setSelected(false);
//        rdoDeductTrans.setSelected(false);
//        rdoEarningsTrans.setSelected(false);
//        rdocontratrans.setSelected(false);
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);
        observable.resetForm();
        setModified(false);
        btnNew.setEnabled(true);
        btnReject.setEnabled(true);
        //btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        btnCancel.setEnabled(false);
        isFilled = false;
        buttonEnableDisable(false);
        //txtAccountHead.setEnabled(false);
        //txtAccountHead.setText("");
        //lblEmployeeName.setText("");
        //txtStandingId.setText("");
        //lblAgentName.setText("");
        resetSHGDetails();
        resetTransactionUI();
        //tabSHGDetails.remove(panTransaction);
        observable.setPaycodeType("");
        //employeeDetailsUI.updateCustomerInfo("");
        removeRadioButtons();
        //btnAccountHead.setEnabled(false);
        //btnStandingId.setEnabled(false);
        //btnTransProductId.setEnabled(false);
        //btnProdIdTrans.setEnabled(false);
        //btnAccNo.setEnabled(false);
        //btnAccNoTrans.setEnabled(false);
        //lblBalance.setText("");
        //panDrCr.setVisible(false);
        clearTable();
        lblTotalValue.setText("");
        //txtParticulars.setText("");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void clearTable() {
        observable.resetForm();
        if (tblStandingAccounts.getRowCount() > 0) {
            ((DefaultTableModel) tblStandingAccounts.getModel()).setRowCount(0);
        }
    }

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnCheck() {
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

private void tblStandingAccountsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblStandingAccountsKeyTyped
    // TODO add your handling code here:
    //System.out.println("tblSHGDetailsKeyTyped");
    /////checkingPaymentAmount(selectedRow);
}//GEN-LAST:event_tblStandingAccountsKeyTyped

private void tblStandingAccountsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblStandingAccountsKeyReleased
    // TODO add your handling code here:
    // System.out.println("tblSHGDetailsKeyReleased");
    ////checkingPaymentAmount(selectedRow);
}//GEN-LAST:event_tblStandingAccountsKeyReleased

private void tblStandingAccountsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblStandingAccountsFocusLost
    // TODO add your handling code here:
    //System.out.println("tblSHGDetailsFocusLost");
    //checkingPaymentAmount(selectedRow);
}//GEN-LAST:event_tblStandingAccountsFocusLost

private void tblStandingAccountsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStandingAccountsMouseReleased
    // TODO add your handling code here:
    //System.out.println("tblSHGDetailsMouseReleased");
    ////checkingPaymentAmount(selectedRow);
}//GEN-LAST:event_tblStandingAccountsMouseReleased

private void tblStandingAccountsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStandingAccountsMousePressed
    // TODO add your handling code here:
    //System.out.println("tblSHGDetailsMousePressed");
    //checkingPaymentAmount(selectedRow);
}//GEN-LAST:event_tblStandingAccountsMousePressed

private void tblStandingAccountsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStandingAccountsMouseExited
    // TODO add your handling code here:
    //System.out.println("tblSHGDetailsMouseExited");
    // // checkingPaymentAmount(selectedRow);
}//GEN-LAST:event_tblStandingAccountsMouseExited

private void tblStandingAccountsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStandingAccountsMouseClicked
    // TODO add your handling code here:
    if (evt.getClickCount() == 2) {
        com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
        HashMap paramMap = new HashMap();
        int column = 0;
        /*if ((column = tblSHGDetails.getSelectedColumn()) == 9) {
            if (CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(), column)) != null
                    && !CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(), column)).equals("")) {
                paramMap.put("AccountNo", CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(), column)));
                paramMap.put("FromDate", currDt.clone());
                paramMap.put("ToDate", currDt.clone());
                paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                ttIntgration.setParam(paramMap);
                ttIntgration.integration("OALedger");
            }
        } else if ((column = tblSHGDetails.getSelectedColumn()) == 2) {
            if (CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(), column)) != null
                    && !CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(), column)).equals("")) {
                //paramMap.put("LoanNo", CommonUtil.convertObjToStr(txtAcctNum.getText())); 
                paramMap.put("CustomerId", CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(), column)));
                ttIntgration.setParam(paramMap);
                ttIntgration.integration("GrpLoanLedgerCustomerWise");
            }
        }*/
    }
}//GEN-LAST:event_tblStandingAccountsMouseClicked

private void cboAgentIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAgentIdActionPerformed
// TODO add your handling code here:
    if(cboAgentId.getSelectedItem()!=null && !cboAgentId.getSelectedItem().equals("")){
        initStandingTableData();
        if(tblStandingAccounts.getRowCount()>0){
            chkSelectAll.setEnabled(true);
        }
    }
}//GEN-LAST:event_cboAgentIdActionPerformed

private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
// TODO add your handling code here:
    if (chkSelectAll.isSelected()) {
        for (int i = 0; i < tblStandingAccounts.getRowCount(); i++) {
            tblStandingAccounts.setValueAt(new Boolean(true), i, 0);
        }
    } else if (!chkSelectAll.isSelected()) {
        for (int i = 0; i < tblStandingAccounts.getRowCount(); i++) {
            tblStandingAccounts.setValueAt(new Boolean(false), i, 0);
        }
    }
}//GEN-LAST:event_chkSelectAllActionPerformed

   /* public String checkMandatoryData() {
        String warningMessage = "";
        StringBuffer strBMandatory = new StringBuffer();
        if ((txtAccountHead.getText().length() == 0) || (txtAccountHead.getText() == null)) {
            strBMandatory.append("Employee should not be empty!!!");
            strBMandatory.append("\n");
        } else if (cboTransactionType.getSelectedItem().equals("") && cboTransactionType.getSelectedItem() == null) {
            strBMandatory.append("Please select any Pay code!!!");
            strBMandatory.append("\n");
        } else if (rdoGeneralLedger.isSelected() == false && rdoOtherAccounts.isSelected() == false) {
            strBMandatory.append("Please select any account type!!!");
            strBMandatory.append("\n");
        } else if (rdoOtherAccounts.isSelected() == true) {
            if (cboProdTypeCr.getSelectedItem().equals("") && cboTransactionType.getSelectedItem() == null) {
                strBMandatory.append("Please select any Product Type!!!");
                strBMandatory.append("\n");
            } else if ((txtTransProductId.getText().length() == 0) || (txtTransProductId.getText() == null)) {
                strBMandatory.append("Please select any product!!!");
                strBMandatory.append("\n");
            } else if ((txtAcctNo.getText().length() == 0) || (txtAcctNo.getText() == null)) {
                strBMandatory.append("Please select any account!!!");
                strBMandatory.append("\n");
            }
        } else if ((txtAmount.getText().length() == 0) || (txtAmount.getText() == null)) {
            strBMandatory.append("Please enter the amount!!!");
            strBMandatory.append("\n");
        }
        if (!updateMode) {
            String PayCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboTransactionType.getModel()).getKeyForSelected());
            if (observable.checkPayCodeExistence(PayCode, txtAccountHead.getText())) {
                strBMandatory.append("Pay code already added for this employee!!!");
                strBMandatory.append("\n");
            }
            for (int i = 0; i < tblSHGDetails.getRowCount(); i++) {
                if (CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(i, 0)).equals(PayCode)) {
                    strBMandatory.append("Pay code already present in grid!!!");
                    strBMandatory.append("\n");
                }
            }
        }
        warningMessage = strBMandatory.toString();
        return warningMessage;
    }*/

    public String checkMandatoryTransData() {
        String warningMessage = "";
        StringBuffer strBMandatory = new StringBuffer();
        /*if ((txtStandingId.getText().length() == 0) || (txtStandingId.getText() == null)) {
            strBMandatory.append("Please select any standing Id!!!");
            strBMandatory.append("\n");
        } */
        /*if (cboTransTransactionType.getSelectedIndex()==0) {
            strBMandatory.append("Please select any Transaction Type!!!");
            strBMandatory.append("\n");
        }*/
        if(tblStandingAccounts.getRowCount()>0){
            boolean count = false;
            for (int i = 0; i < tblStandingAccounts.getRowCount(); i++) {
                if ((Boolean) tblStandingAccounts.getValueAt(i, 0)) {                    
                    count = true;
                }
            }
            if (!count) {
                strBMandatory.append("No Data Selected for transaction!!!");
                count = false;                
            } 
        }
        warningMessage = strBMandatory.toString();
        return warningMessage;
    }

    private void btnSHGNewActionPerformed() {
        //updateMode = false;
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        //btnStandingId.setEnabled(true);
//        btnProdIdTrans.setEnabled(false);
//        btnAccNoTrans.setEnabled(false);
//        cboPayCodesTrans.setEnabled(true);
        //buttonEnableDisable(false);
        // btnSHGSave.setEnabled(true);
        observable.setNewData(true);
//        rdoGeneralLedgertrans.setEnabled(true);
//        rdoOtherAccountsTrans.setEnabled(true);
//        rdoEarningsTrans.setEnabled(true);
//        rdocontratrans.setEnabled(true);
//        rdoDeductTrans.setEnabled(true);
//        cboProdTypeCrtrans.setEnabled(false);
//        rdoPayment.setEnabled(true);
//        rdoReceipt.setEnabled(true);
//        ClientUtil.enableDisable(panAcctNo1, false);
//        ClientUtil.enableDisable(panAccHd1, false);
//        ClientUtil.enableDisable(panAccHd2, false);
        //chkPayStatusTrans.setEnabled(true);
        //chkPayTransaction.setEnabled(true);
        //txtAmountTrans.setEnabled(true);
        //txtRecoveryMonthTrans.setEnabled(true);
        //tdtFromDtTrans.setEnabled(true);
        /*if (tblSHGDetails.getRowCount() <= 0) {
        }
        setButtonEnableDisable();
        cboTransTransactionType.setEnabled(true);*/
    }

    private void resetTransactionUI() {
//        
    }

    private void enableDisablePanAccounts(boolean flag) {
        /*cboProdTypeCr.setEnabled(flag);
        txtTransProductId.setEnabled(flag);
        ClientUtil.enableDisable(panAcctNo, flag);
        ClientUtil.enableDisable(panAccHd, flag);
        txtAcctNo.setEnabled(flag);
        btnAccNo.setEnabled(flag);
        btnTransProductId.setEnabled(flag);*/
    }

    private void TransEnableDisablePanAccounts(boolean flag) {
//        cboProdTypeCrtrans.setEnabled(flag);
//        txtProdIdTrans.setEnabled(flag);
//        ClientUtil.enableDisable(panAccHd1, flag);
//        ClientUtil.enableDisable(panAccHd2, flag);
//        txtAcctNotrans.setEnabled(flag);
//        btnAccNoTrans.setEnabled(flag);
//        btnProdIdTrans.setEnabled(flag);
    }

    private void displayAlert(String message) {
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButtonGroup btnGrpDrCr;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboAgentId;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblAgentId;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace52;
    private com.see.truetransact.uicomponent.CLabel lblSpace53;
    private com.see.truetransact.uicomponent.CLabel lblSpace54;
    private com.see.truetransact.uicomponent.CLabel lblSpace55;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotal;
    private com.see.truetransact.uicomponent.CLabel lblTotalValue;
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
    private com.see.truetransact.uicomponent.CPanel panAccountNo2;
    private com.see.truetransact.uicomponent.CPanel panAgentDetails;
    private com.see.truetransact.uicomponent.CPanel panSHGDetails1;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransAccountNo;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CPanel panTransactionTrans;
    private com.see.truetransact.uicomponent.CButtonGroup radioButtonGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdgEFTProductGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIsLapsedGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPayableBranchGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrintServicesGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSeriesGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdoButtonGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPayCodeGrp;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransAccountType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransPaycodeGrp;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransTypeGrp;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CScrollPane srpStandingAccounts;
    private com.see.truetransact.uicomponent.CTabbedPane tabSHGDetails;
    private com.see.truetransact.uicomponent.CTable tblStandingAccounts;
    private javax.swing.JToolBar tbrAdvances;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] arg) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        RdStandingUI gui = new RdStandingUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}