  /*
 * RemittanceProductUI.java
 *
 * Created on November 26, 2003, 11:27 AM
 */
package com.see.truetransact.ui.payroll.payMaster;

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

/**
 *
 * @author sreeKrishnan
 *
 **/
public class PayRollIndividualUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {

    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.payroll.payMaster.PayRollIndividualMRB", ProxyParameters.LANGUAGE);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String viewType = new String();
    payRollIndividualOB observable = null;
    final String AUTHORIZE = "Authorize";
    HashMap mandatoryMap = null;
    boolean isFilled = false;
    private HashMap productMap = new HashMap();
    private boolean updateMode = false;
    int updateTab = -1;
    double odAmount = 0.0;
    Date currDt = null;
    private TransactionUI transactionUI = new TransactionUI();
    String cust_Id = "";
    private EmployeeDetailsUI employeeDetailsUI = null;

    /** Creates new form BeanForm */
    public PayRollIndividualUI() {
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
        buttonEnableDisableEmp(false);
        setMandatoryHashMap();
        ClientUtil.enableDisable(panSHGDetails, false);
        ClientUtil.enableDisable(panTransaction, false);
        ClientUtil.enableDisable(panSHGDetails1, false);
        ClientUtil.enableDisable(panBulkPaymaster, false);
        txtEmployeeID.setEnabled(false);
        lblTotLimit.setText("0.0");
        currDt = ClientUtil.getCurrentDate();
        //tabSHGDetails.remove(panTransaction);
        employeeDetailsUI = new EmployeeDetailsUI(panEmpDetails);
        btnTransProductId.setEnabled(false);
        btnAccNo.setEnabled(false);
        btnProdIdTrans.setEnabled(false);
        btnAccNoTrans.setEnabled(false);
        btnAuthorize.setEnabled(false);
        addRadioButtons();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panSHGRecordDetails);
        panTransaction.setName("Transaction");
        tabSHGDetails.add(panTransaction);
        transactionUI.setSourceScreen("PAY_ROLL_MASTER");
        transactionUI.addToScreen(panTransactionTrans);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        resetTransactionUI();
        panPFDetails.setVisible(false);
        panDrCr.setVisible(false);
          initTableData();
    }
      private void initTableData() {
        tblPayMasterList.setModel(observable.getTblPaymasterList());
    }

    private void setObservable() {
        try {
            observable = new payRollIndividualOB();
            observable.addObserver(this);
            observable.setTransactionOB(transactionUI.getTransactionOB());
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
        btnSHGDelete.setName("btnSHGDelete");
        btnSHGNew.setName("btnSHGNew");
        btnSHGSave.setName("btnSHGSave");
        btnSave.setName("btnSave");
        btnView.setName("btnView");
        lblEmployeeID.setName("lblGroupLoan");
        lblMsg.setName("lblMsg");
        lblSpace.setName("lblSpace");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        lblStatus.setName("lblStatus");
        mbrMain.setName("mbrMain");
        panSHGBtn.setName("panSHGBtn");
        panSHGDetails.setName("panSHGDetails");
        panSHGRecordDetails.setName("panSHGRecordDetails");
        panSHGTableDetails.setName("panSHGTableDetails");
        panStatus.setName("panStatus");
        srpSHGTable.setName("srpSHGTable");
        tabSHGDetails.setName("tabSHGDetails");
        tblSHGDetails.setName("tblSHGDetails");
        txtEmployeeID.setName("txtGroupLoanNum");
        tblEmployeeDetails.setName("tblEmployeeDetails");
    }

    private void setMaxLengths() {
        txtAmount.setValidation(new CurrencyValidation(13, 2));
        txtAmountTrans.setValidation(new CurrencyValidation(13, 2));
    }

    private void updateCustomerInfo() {
        if (observable.getTxtEmployeeId() != null && !observable.getTxtEmployeeId().equals("")) {
            employeeDetailsUI.updateCustomerInfo(observable.getTxtEmployeeId());
        }
    }

    /* Auto Generated Method - internationalize()
    This method used to assign display texts from
    the Resource Bundle File. */
    private void internationalize() {
        resourceBundle = new PayRollIndividualRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblEmployeeID.setText(resourceBundle.getString("lblEmployeeID"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        btnSHGSave.setText(resourceBundle.getString("btnSHGSave"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnView.setText(resourceBundle.getString("btnView"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnSHGNew.setText(resourceBundle.getString("btnSHGNew"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace6.setText(resourceBundle.getString("lblSpace6"));
        btnSHGDelete.setText(resourceBundle.getString("btnSHGDelete"));
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
            tblSHGDetails.setModel(observable.getTblSHGDetails());
            cboProdTypeCr.setModel(observable.getCbmProdTypCr());
            tblEmployeeDetails.setModel(observable.getTblEmployeeDetails());
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
            txtEmployeeID.setText(observable.getTxtEmployeeId());
            updateCustomerInfo();
            lblEmployeeName.setText(observable.getLblEmployeeName());
            txtAmount.setText(CommonUtil.convertObjToStr(observable.getTotalAmount()));
            chkPayStatus.setSelected(observable.isActiveStaus());
            //chkPayTransaction.setSelected(observable.isPayTrans());
            rdoGeneralLedger.setSelected(observable.isRdoGlAccountType());
            rdoOtherAccounts.setSelected(observable.isRdoOtherAccountType());
            cboPayCodes.setSelectedItem(observable.getPayDescription());
            if (rdoGeneralLedger.isSelected() == true) {
                rdoGeneralLedgerActionPerformed(null);
            } else if (rdoOtherAccounts.isSelected() == true) {
                enableDisablePanAccounts(true);
                txtAcctNo.setText(observable.getAcctNo());
                txtAcctNo.setText(observable.getAcctNo());
                txtTransProductId.setText(observable.getProdID());
                cboProdTypeCr.setSelectedItem(observable.getProdType());
            }
            txtRecoveryMonth.setText(CommonUtil.convertObjToStr(observable.getRecoveryMonth()));
            tdtFromDt.setDateValue(observable.getTdtFromDate());
            if (observable.getPaycodeType().equals("EARNINGS")) {
                rdoEarnings.setSelected(true);
                rdoEarningsActionPerformed(null);
                cboPayCodes.setSelectedItem(observable.getPayDescription());
            } else if (observable.getPaycodeType().equals("DEDUCTIONS")) {
                rdoDeduct.setSelected(true);
                rdoDeductActionPerformed(null);
                cboPayCodes.setSelectedItem(observable.getPayDescription());
            } else if (observable.getPaycodeType().equals("CONTRA")) {
                rdocontra.setSelected(true);
                rdocontraActionPerformed(null);
                cboPayCodes.setSelectedItem(observable.getPayDescription());
            }
        } else if (tabSHGDetails.getSelectedIndex() == 1) {
            txtTransEmployeeID.setText(observable.getTxtEmployeeId());
            //updateCustomerInfo();
            lblEmployeeNameTrans.setText(observable.getLblEmployeeName());
            txtAmountTrans.setText(CommonUtil.convertObjToStr(observable.getTotalAmount()));
            //chkPayStatusTrans.setSelected(observable.isActiveStaus());
            //chkPayTransaction.setSelected(observable.isPayTrans());
            rdoGeneralLedgertrans.setSelected(observable.isRdoGlAccountType());
            rdoOtherAccountsTrans.setSelected(observable.isRdoOtherAccountType());
            cboPayCodesTrans.setSelectedItem(observable.getPayDescription());
            if (rdoGeneralLedgertrans.isSelected() == true) {
                rdoGeneralLedgertransActionPerformed(null);
            } else if (rdoOtherAccountsTrans.isSelected() == true) {
                enableDisablePanAccounts(true);
                txtAcctNotrans.setText(observable.getAcctNo());
                txtProdIdTrans.setText(observable.getProdID());
                cboProdTypeCrtrans.setSelectedItem(observable.getProdType());
            }
            //txtRecoveryMonthTrans.setText(CommonUtil.convertObjToStr(observable.getRecoveryMonth()));
            //tdtFromDtTrans.setDateValue(observable.getTdtFromDate());
            if (observable.getPaycodeType().equals("EARNINGS")) {
                rdoEarningsTrans.setSelected(true);
                rdoEarningsTransActionPerformed(null);
                cboPayCodesTrans.setSelectedItem(observable.getPayDescription());
            } else if (observable.getPaycodeType().equals("DEDUCTIONS")) {
                rdoDeduct.setSelected(true);
                rdoDeductTransActionPerformed(null);
                cboPayCodesTrans.setSelectedItem(observable.getPayDescription());
            } else if (observable.getPaycodeType().equals("CONTRA")) {
                rdocontratrans.setSelected(true);
                rdocontratransActionPerformed(null);
                cboPayCodesTrans.setSelectedItem(observable.getPayDescription());
            }
        }
     
    }

    /* Auto Generated Method - updateOBFields()
    This method called by Save option of UI.
    It updates the OB with UI data.*/
    public void updateOBFields() {
        String PayCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboPayCodes.getModel()).getKeyForSelected());
        String PayDesc = CommonUtil.convertObjToStr(((ComboBoxModel) cboPayCodes.getModel()).getSelectedItem());
        observable.setTxtEmployeeId(txtEmployeeID.getText());
        observable.setLblEmployeeName(lblEmployeeName.getText());
        observable.setTotalAmount(CommonUtil.convertObjToDouble(txtAmount.getText()));
        observable.setPayCode(PayCode);
        observable.setPayDescription(PayDesc);
        observable.setActiveStaus(chkPayStatus.isSelected());
        observable.setPayTrans(false);
        if (rdoGeneralLedger.isSelected() == true) {
            observable.setProdType("GL");
        } else if (rdoOtherAccounts.isSelected() == true) {
            String ProdType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected());
            String ProdId = txtTransProductId.getText();
            observable.setProdType(ProdType);
            observable.setProdID(ProdId);
            observable.setAcctNo(txtAcctNo.getText());
        }
        if (rdoEarnings.isSelected() == true) {
            observable.setPaycodeType("EARNINGS");
        } else if (rdocontra.isSelected() == true) {
            observable.setPaycodeType("CONTRA");
        } else {
            observable.setPaycodeType("DEDUCTIONS");
        }
        observable.setTdtFromDate(tdtFromDt.getDateValue());
        observable.setRecoveryMonth(CommonUtil.convertObjToInt(txtRecoveryMonth.getText()));
    }
      public void updateMultiOBFields() {
        String PayCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboPayCodesTrans.getModel()).getKeyForSelected());
        String PayDesc = CommonUtil.convertObjToStr(((ComboBoxModel) cboPayCodesTrans.getModel()).getSelectedItem());
        observable.setTxtEmployeeId(txtTransEmployeeID.getText());
        observable.setLblEmployeeName(lblEmployeeNameTrans.getText());
        observable.setTotalAmount(CommonUtil.convertObjToDouble(txtAmountTrans.getText()));
        observable.setPayCode(PayCode);
        observable.setPayDescription(PayDesc);
        //observable.setActiveStaus(chkPayStatusTrans.isSelected());
        observable.setPayTrans(true);
      }
    public void updateTransOBFields() {
     
        if (rdoGeneralLedgertrans.isSelected() == true) {
            observable.setProdType("GL");
        } else if (rdoOtherAccountsTrans.isSelected() == true) {
            String ProdType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCrtrans.getModel()).getKeyForSelected());
            String ProdId = txtProdIdTrans.getText();
            observable.setProdType(ProdType);
            observable.setProdID(ProdId);
            observable.setAcctNo(txtAcctNotrans.getText());
        }
        if (rdoEarningsTrans.isSelected() == true) {
            observable.setPaycodeType("EARNINGS");
        } else if (rdocontratrans.isSelected() == true) {
            observable.setPaycodeType("CONTRA");        
            if (rdoDr.isSelected() == true) {                
                observable.setContraTransType("DEBIT");
            }else if (rdoCr.isSelected() == true) {
                observable.setContraTransType("CREDIT");
            }
        } else {
            observable.setPaycodeType("DEDUCTIONS");
        }
        if (rdoReceipt.isSelected() == true) {
            observable.setTransType(CommonConstants.CREDIT);
        } else {
            observable.setTransType(CommonConstants.DEBIT);
        }
        //observable.setTdtFromDate(tdtFromDtTrans.getDateValue());
        //observable.setRecoveryMonth(CommonUtil.convertObjToDouble(txtRecoveryMonthTrans.getText()));
    }

    private void addRadioButtons() {
        //---Account---
        //rdoButtonGroup = new CButtonGroup();
        //rdoButtonGroup.add(rdoGeneralLedger);
       // rdoButtonGroup.add(rdoOtherAccounts);

        rdoPayCodeGrp = new CButtonGroup();
        rdoPayCodeGrp.add(rdoEarnings);
        rdoPayCodeGrp.add(rdoDeduct);
        rdoPayCodeGrp.add(rdocontra);

        rdoTransTypeGrp = new CButtonGroup();
        rdoTransTypeGrp.add(rdoReceipt);
        rdoTransTypeGrp.add(rdoPayment);

        rdoTransPaycodeGrp = new CButtonGroup();
        rdoTransPaycodeGrp.add(rdoEarningsTrans);
        rdoTransPaycodeGrp.add(rdoDeductTrans);
        rdoTransPaycodeGrp.add(rdocontratrans);

        rdoTransAccountType = new CButtonGroup();
        rdoTransAccountType.add(rdoGeneralLedgertrans);
        rdoTransAccountType.add(rdoOtherAccountsTrans);
        
        rdoBulkPayCodeGrp = new CButtonGroup();
        rdoBulkPayCodeGrp.add(rdoBulkEarnings);
        rdoBulkPayCodeGrp.add(rdoBulkDeduct);
        rdoBulkPayCodeGrp.add(rdoBulkContra);


    }

    private void removeRadioButtons() {
        //rdoButtonGroup.remove(rdoGeneralLedger);
        //rdoButtonGroup.remove(rdoOtherAccounts);
        rdoPayCodeGrp.remove(rdoEarnings);
        rdoPayCodeGrp.remove(rdoDeduct);
        rdoPayCodeGrp.remove(rdocontra);
        rdoTransTypeGrp.remove(rdoReceipt);
        rdoTransTypeGrp.remove(rdoPayment);
        rdoTransPaycodeGrp.remove(rdoEarningsTrans);
        rdoTransPaycodeGrp.remove(rdoDeductTrans);
        rdoTransPaycodeGrp.remove(rdocontratrans);
        rdoTransAccountType.remove(rdoGeneralLedgertrans);
        rdoTransAccountType.remove(rdoOtherAccountsTrans);
        rdoBulkPayCodeGrp.remove(rdoBulkContra);
        rdoBulkPayCodeGrp.remove(rdoBulkDeduct);
        rdoBulkPayCodeGrp.remove(rdoBulkEarnings);
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
        btnSHGNew.setEnabled(flag);
        btnSHGSave.setEnabled(flag);
        btnSHGDelete.setEnabled(flag);
    }
    
      private void buttonEnableDisableEmp(boolean flag) {
        btnNew_Employee.setEnabled(flag);
        btnSave_Employee.setEnabled(flag);
        btnDeleteEmployee.setEnabled(flag);
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
        cMenuBar1 = new com.see.truetransact.uicomponent.CMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        rdoAcctButtonGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoBulkPayCodeGrp = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace70 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace71 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace72 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabSHGDetails = new com.see.truetransact.uicomponent.CTabbedPane();
        panSHGDetails = new com.see.truetransact.uicomponent.CPanel();
        panSHGRecordDetails = new com.see.truetransact.uicomponent.CPanel();
        panSHGBtn = new com.see.truetransact.uicomponent.CPanel();
        btnSHGNew = new com.see.truetransact.uicomponent.CButton();
        btnSHGSave = new com.see.truetransact.uicomponent.CButton();
        btnSHGDelete = new com.see.truetransact.uicomponent.CButton();
        panAccountNo = new com.see.truetransact.uicomponent.CPanel();
        lblEmployeeName = new com.see.truetransact.uicomponent.CLabel();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        panAccountNo1 = new com.see.truetransact.uicomponent.CPanel();
        txtEmployeeID = new com.see.truetransact.uicomponent.CTextField();
        btnEmployeeID = new com.see.truetransact.uicomponent.CButton();
        cLabel4 = new com.see.truetransact.uicomponent.CLabel();
        lblAccount = new com.see.truetransact.uicomponent.CLabel();
        panRadioGrp = new com.see.truetransact.uicomponent.CPanel();
        rdoGeneralLedger = new com.see.truetransact.uicomponent.CRadioButton();
        rdoOtherAccounts = new com.see.truetransact.uicomponent.CRadioButton();
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
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        cboPayCodes = new com.see.truetransact.uicomponent.CComboBox();
        chkPayStatus = new com.see.truetransact.uicomponent.CCheckBox();
        panPaymentTransaction = new com.see.truetransact.uicomponent.CPanel();
        tdtFromDt = new com.see.truetransact.uicomponent.CDateField();
        lblFromDt = new com.see.truetransact.uicomponent.CLabel();
        lblRevryMnth = new com.see.truetransact.uicomponent.CLabel();
        txtRecoveryMonth = new com.see.truetransact.uicomponent.CTextField();
        cLabel5 = new com.see.truetransact.uicomponent.CLabel();
        lblEmployeeID = new com.see.truetransact.uicomponent.CLabel();
        rdocontra = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDeduct = new com.see.truetransact.uicomponent.CRadioButton();
        rdoEarnings = new com.see.truetransact.uicomponent.CRadioButton();
        panSHGTableDetails = new com.see.truetransact.uicomponent.CPanel();
        srpSHGTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblSHGDetails = new com.see.truetransact.uicomponent.CTable();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        lblTotLimit = new com.see.truetransact.uicomponent.CLabel();
        panEmpDetails = new com.see.truetransact.uicomponent.CPanel();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        panTransactionTrans = new com.see.truetransact.uicomponent.CPanel();
        panSHGDetails1 = new com.see.truetransact.uicomponent.CPanel();
        panSHGRecordDetails1 = new com.see.truetransact.uicomponent.CPanel();
        panAccountNo2 = new com.see.truetransact.uicomponent.CPanel();
        cLabel6 = new com.see.truetransact.uicomponent.CLabel();
        panTransAccountNo = new com.see.truetransact.uicomponent.CPanel();
        panRadioGrp1 = new com.see.truetransact.uicomponent.CPanel();
        cboPayCodesTrans = new com.see.truetransact.uicomponent.CComboBox();
        rdocontratrans = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDeductTrans = new com.see.truetransact.uicomponent.CRadioButton();
        rdoEarningsTrans = new com.see.truetransact.uicomponent.CRadioButton();
        panDrCr = new com.see.truetransact.uicomponent.CPanel();
        rdoDr = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCr = new com.see.truetransact.uicomponent.CRadioButton();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        panAccountDetails1 = new com.see.truetransact.uicomponent.CPanel();
        cboProdTypeCrtrans = new com.see.truetransact.uicomponent.CComboBox();
        lblProdTypeCr1 = new com.see.truetransact.uicomponent.CLabel();
        lblAccNo1 = new com.see.truetransact.uicomponent.CLabel();
        lblProdIdCr1 = new com.see.truetransact.uicomponent.CLabel();
        panAccHd1 = new com.see.truetransact.uicomponent.CPanel();
        txtProdIdTrans = new com.see.truetransact.uicomponent.CTextField();
        btnProdIdTrans = new com.see.truetransact.uicomponent.CButton();
        panAcctNo1 = new com.see.truetransact.uicomponent.CPanel();
        panAccHd2 = new com.see.truetransact.uicomponent.CPanel();
        txtAcctNotrans = new com.see.truetransact.uicomponent.CTextField();
        btnAccNoTrans = new com.see.truetransact.uicomponent.CButton();
        cLabel9 = new com.see.truetransact.uicomponent.CLabel();
        rdoGeneralLedgertrans = new com.see.truetransact.uicomponent.CRadioButton();
        rdoOtherAccountsTrans = new com.see.truetransact.uicomponent.CRadioButton();
        panTransactionType = new com.see.truetransact.uicomponent.CPanel();
        lblAccount1 = new com.see.truetransact.uicomponent.CLabel();
        txtAmountTrans = new com.see.truetransact.uicomponent.CTextField();
        cLabel7 = new com.see.truetransact.uicomponent.CLabel();
        rdoPayment = new com.see.truetransact.uicomponent.CRadioButton();
        rdoReceipt = new com.see.truetransact.uicomponent.CRadioButton();
        lblEmployeeNameTrans = new com.see.truetransact.uicomponent.CLabel();
        lblEmployeeID1 = new com.see.truetransact.uicomponent.CLabel();
        txtTransEmployeeID = new com.see.truetransact.uicomponent.CTextField();
        btnTransEmployeeID = new com.see.truetransact.uicomponent.CButton();
        panPFDetails = new com.see.truetransact.uicomponent.CPanel();
        lblBalText = new com.see.truetransact.uicomponent.CLabel();
        lblBalance = new com.see.truetransact.uicomponent.CLabel();
        btnViewLedger = new com.see.truetransact.uicomponent.CButton();
        panEmployyeDetails = new com.see.truetransact.uicomponent.CPanel();
        panEmployeeTabTools = new com.see.truetransact.uicomponent.CPanel();
        btnSave_Employee = new com.see.truetransact.uicomponent.CButton();
        btnDeleteEmployee = new com.see.truetransact.uicomponent.CButton();
        btnNew_Employee = new com.see.truetransact.uicomponent.CButton();
        srpEmployeeTabCTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblEmployeeDetails = new com.see.truetransact.uicomponent.CTable();
        lblTransTotalValue = new com.see.truetransact.uicomponent.CLabel();
        lblTransTot = new com.see.truetransact.uicomponent.CLabel();
        panBulkPaymaster = new com.see.truetransact.uicomponent.CPanel();
        panSalaryRecoveryOptions = new com.see.truetransact.uicomponent.CPanel();
        panPayType = new com.see.truetransact.uicomponent.CPanel();
        rdoBulkEarnings = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBulkDeduct = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBulkContra = new com.see.truetransact.uicomponent.CRadioButton();
        lblBulkPayCode = new com.see.truetransact.uicomponent.CLabel();
        cboBulkPayCodes = new com.see.truetransact.uicomponent.CComboBox();
        btnBulkDisplay = new com.see.truetransact.uicomponent.CButton();
        panPaymasterList = new com.see.truetransact.uicomponent.CPanel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        srpPaymasterList = new com.see.truetransact.uicomponent.CScrollPane();
        tblPayMasterList = new com.see.truetransact.uicomponent.CTable();
        lblNoOfRecords = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecordsVal = new com.see.truetransact.uicomponent.CLabel();
        lblSelectedCount = new com.see.truetransact.uicomponent.CLabel();
        lblSelectedCountVal = new com.see.truetransact.uicomponent.CLabel();
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

        jMenu1.setText("File");
        cMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        cMenuBar1.add(jMenu2);

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

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace70);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace71);

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

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace72);

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

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace73);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace74);

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

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace75);

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
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 114, 2, 0);
        panSHGRecordDetails.add(panSHGBtn, gridBagConstraints);

        panAccountNo.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        panSHGRecordDetails.add(panAccountNo, gridBagConstraints);

        lblEmployeeName.setForeground(new java.awt.Color(0, 0, 102));
        lblEmployeeName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblEmployeeName.setMaximumSize(new java.awt.Dimension(200, 18));
        lblEmployeeName.setMinimumSize(new java.awt.Dimension(200, 18));
        lblEmployeeName.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panSHGRecordDetails.add(lblEmployeeName, gridBagConstraints);

        cLabel3.setText("Pay Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSHGRecordDetails.add(cLabel3, gridBagConstraints);

        panAccountNo1.setLayout(new java.awt.GridBagLayout());

        txtEmployeeID.setAllowAll(true);
        txtEmployeeID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEmployeeID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmployeeIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountNo1.add(txtEmployeeID, gridBagConstraints);

        btnEmployeeID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEmployeeID.setEnabled(false);
        btnEmployeeID.setMaximumSize(new java.awt.Dimension(21, 21));
        btnEmployeeID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnEmployeeID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEmployeeID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmployeeIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountNo1.add(btnEmployeeID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        panSHGRecordDetails.add(panAccountNo1, gridBagConstraints);

        cLabel4.setText("Pay Account Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSHGRecordDetails.add(cLabel4, gridBagConstraints);

        lblAccount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSHGRecordDetails.add(lblAccount, gridBagConstraints);

        panRadioGrp.setLayout(new java.awt.GridBagLayout());

        rdoButtonGroup.add(rdoGeneralLedger);
        rdoGeneralLedger.setText("General Ledger");
        rdoGeneralLedger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGeneralLedgerActionPerformed(evt);
            }
        });
        panRadioGrp.add(rdoGeneralLedger, new java.awt.GridBagConstraints());

        rdoButtonGroup.add(rdoOtherAccounts);
        rdoOtherAccounts.setText("Other Accounts");
        rdoOtherAccounts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoOtherAccountsActionPerformed(evt);
            }
        });
        panRadioGrp.add(rdoOtherAccounts, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        panSHGRecordDetails.add(panRadioGrp, gridBagConstraints);

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
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSHGRecordDetails.add(panAccountDetails, gridBagConstraints);

        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 0, 25);
        panSHGRecordDetails.add(txtAmount, gridBagConstraints);

        cboPayCodes.setMinimumSize(new java.awt.Dimension(200, 21));
        cboPayCodes.setPopupWidth(160);
        cboPayCodes.setPreferredSize(new java.awt.Dimension(200, 21));
        cboPayCodes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPayCodesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        panSHGRecordDetails.add(cboPayCodes, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 27, 0, 9);
        panSHGRecordDetails.add(chkPayStatus, gridBagConstraints);

        panPaymentTransaction.setMinimumSize(new java.awt.Dimension(250, 65));
        panPaymentTransaction.setPreferredSize(new java.awt.Dimension(250, 70));
        panPaymentTransaction.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 0);
        panPaymentTransaction.add(tdtFromDt, gridBagConstraints);

        lblFromDt.setText("Recovery From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPaymentTransaction.add(lblFromDt, gridBagConstraints);

        lblRevryMnth.setText("No.of Recovery Months");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPaymentTransaction.add(lblRevryMnth, gridBagConstraints);

        txtRecoveryMonth.setAllowAll(true);
        txtRecoveryMonth.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 0);
        panPaymentTransaction.add(txtRecoveryMonth, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        panSHGRecordDetails.add(panPaymentTransaction, gridBagConstraints);

        cLabel5.setText("Pay Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 22);
        panSHGRecordDetails.add(cLabel5, gridBagConstraints);

        lblEmployeeID.setText("Employee ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSHGRecordDetails.add(lblEmployeeID, gridBagConstraints);

        rdocontra.setText("Contribution");
        rdocontra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdocontraActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSHGRecordDetails.add(rdocontra, gridBagConstraints);

        rdoDeduct.setText("Deduction");
        rdoDeduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDeductActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSHGRecordDetails.add(rdoDeduct, gridBagConstraints);

        rdoEarnings.setText("Earnings");
        rdoEarnings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoEarningsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSHGRecordDetails.add(rdoEarnings, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSHGDetails.add(panSHGRecordDetails, gridBagConstraints);

        panSHGTableDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSHGTableDetails.setMinimumSize(new java.awt.Dimension(375, 440));
        panSHGTableDetails.setPreferredSize(new java.awt.Dimension(425, 440));
        panSHGTableDetails.setLayout(null);

        srpSHGTable.setMinimumSize(new java.awt.Dimension(400, 420));
        srpSHGTable.setPreferredSize(new java.awt.Dimension(400, 350));

        tblSHGDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Pay Code", "Pay Description", "Pay type", "Pay Amont"
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
        srpSHGTable.setBounds(20, 40, 400, 350);

        cLabel1.setText("Net Salary");
        panSHGTableDetails.add(cLabel1);
        cLabel1.setBounds(150, 410, 80, 18);

        lblTotLimit.setMaximumSize(new java.awt.Dimension(100, 21));
        lblTotLimit.setMinimumSize(new java.awt.Dimension(100, 21));
        lblTotLimit.setPreferredSize(new java.awt.Dimension(100, 21));
        panSHGTableDetails.add(lblTotLimit);
        lblTotLimit.setBounds(220, 410, 100, 21);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSHGDetails.add(panSHGTableDetails, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        panSHGDetails.add(panEmpDetails, gridBagConstraints);

        tabSHGDetails.addTab("Master", panSHGDetails);

        panTransaction.setLayout(new java.awt.GridBagLayout());

        panTransactionTrans.setMinimumSize(new java.awt.Dimension(840, 220));
        panTransactionTrans.setPreferredSize(new java.awt.Dimension(840, 220));
        panTransactionTrans.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panTransaction.add(panTransactionTrans, gridBagConstraints);

        panSHGDetails1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSHGDetails1.setMinimumSize(new java.awt.Dimension(840, 280));
        panSHGDetails1.setPreferredSize(new java.awt.Dimension(840, 280));
        panSHGDetails1.setLayout(new java.awt.GridBagLayout());

        panSHGRecordDetails1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("Pay Code Details")));
        panSHGRecordDetails1.setMinimumSize(new java.awt.Dimension(375, 50));
        panSHGRecordDetails1.setPreferredSize(new java.awt.Dimension(375, 81));
        panSHGRecordDetails1.setLayout(new java.awt.GridBagLayout());

        panAccountNo2.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        panSHGRecordDetails1.add(panAccountNo2, gridBagConstraints);

        cLabel6.setText("Pay Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSHGRecordDetails1.add(cLabel6, gridBagConstraints);

        panTransAccountNo.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 12, 4, 12);
        panSHGRecordDetails1.add(panTransAccountNo, gridBagConstraints);

        panRadioGrp1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        panSHGRecordDetails1.add(panRadioGrp1, gridBagConstraints);

        cboPayCodesTrans.setMinimumSize(new java.awt.Dimension(200, 21));
        cboPayCodesTrans.setPopupWidth(160);
        cboPayCodesTrans.setPreferredSize(new java.awt.Dimension(200, 21));
        cboPayCodesTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPayCodesTransActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        panSHGRecordDetails1.add(cboPayCodesTrans, gridBagConstraints);

        rdocontratrans.setText("Contribution");
        rdocontratrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdocontratransActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSHGRecordDetails1.add(rdocontratrans, gridBagConstraints);

        rdoDeductTrans.setText("Deduction");
        rdoDeductTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDeductTransActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSHGRecordDetails1.add(rdoDeductTrans, gridBagConstraints);

        rdoEarningsTrans.setText("Earnings");
        rdoEarningsTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoEarningsTransActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSHGRecordDetails1.add(rdoEarningsTrans, gridBagConstraints);

        panDrCr.setMinimumSize(new java.awt.Dimension(40, 60));
        panDrCr.setPreferredSize(new java.awt.Dimension(100, 100));

        btnGrpDrCr.add(rdoDr);
        rdoDr.setText("Dr");
        panDrCr.add(rdoDr);

        btnGrpDrCr.add(rdoCr);
        rdoCr.setText("Cr");
        panDrCr.add(rdoCr);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 4;
        panSHGRecordDetails1.add(panDrCr, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panSHGDetails1.add(panSHGRecordDetails1, gridBagConstraints);

        panAccountDetails1.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Details"));
        panAccountDetails1.setMinimumSize(new java.awt.Dimension(400, 130));
        panAccountDetails1.setPreferredSize(new java.awt.Dimension(400, 130));
        panAccountDetails1.setLayout(new java.awt.GridBagLayout());

        cboProdTypeCrtrans.setMinimumSize(new java.awt.Dimension(150, 21));
        cboProdTypeCrtrans.setPopupWidth(160);
        cboProdTypeCrtrans.setPreferredSize(new java.awt.Dimension(150, 21));
        cboProdTypeCrtrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeCrtransActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails1.add(cboProdTypeCrtrans, gridBagConstraints);

        lblProdTypeCr1.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails1.add(lblProdTypeCr1, gridBagConstraints);

        lblAccNo1.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails1.add(lblAccNo1, gridBagConstraints);

        lblProdIdCr1.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails1.add(lblProdIdCr1, gridBagConstraints);

        panAccHd1.setMinimumSize(new java.awt.Dimension(121, 21));
        panAccHd1.setPreferredSize(new java.awt.Dimension(21, 200));
        panAccHd1.setLayout(new java.awt.GridBagLayout());

        txtProdIdTrans.setAllowAll(true);
        txtProdIdTrans.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProdIdTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProdIdTransActionPerformed(evt);
            }
        });
        txtProdIdTrans.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProdIdTransFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccHd1.add(txtProdIdTrans, gridBagConstraints);

        btnProdIdTrans.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnProdIdTrans.setToolTipText("Account Number");
        btnProdIdTrans.setPreferredSize(new java.awt.Dimension(21, 21));
        btnProdIdTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdIdTransActionPerformed(evt);
            }
        });
        panAccHd1.add(btnProdIdTrans, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        panAccountDetails1.add(panAccHd1, gridBagConstraints);

        panAcctNo1.setMinimumSize(new java.awt.Dimension(121, 21));
        panAcctNo1.setPreferredSize(new java.awt.Dimension(21, 200));
        panAcctNo1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 9);
        panAccountDetails1.add(panAcctNo1, gridBagConstraints);

        panAccHd2.setMinimumSize(new java.awt.Dimension(121, 21));
        panAccHd2.setPreferredSize(new java.awt.Dimension(21, 200));
        panAccHd2.setLayout(new java.awt.GridBagLayout());

        txtAcctNotrans.setAllowAll(true);
        txtAcctNotrans.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcctNotrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAcctNotransActionPerformed(evt);
            }
        });
        txtAcctNotrans.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcctNotransFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccHd2.add(txtAcctNotrans, gridBagConstraints);

        btnAccNoTrans.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccNoTrans.setToolTipText("Account Number");
        btnAccNoTrans.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccNoTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNoTransActionPerformed(evt);
            }
        });
        panAccHd2.add(btnAccNoTrans, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        panAccountDetails1.add(panAccHd2, gridBagConstraints);

        cLabel9.setText("Pay Account Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAccountDetails1.add(cLabel9, gridBagConstraints);

        rdoAcctButtonGroup.add(rdoGeneralLedgertrans);
        rdoGeneralLedgertrans.setText("General Ledger");
        rdoGeneralLedgertrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGeneralLedgertransActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panAccountDetails1.add(rdoGeneralLedgertrans, gridBagConstraints);

        rdoAcctButtonGroup.add(rdoOtherAccountsTrans);
        rdoOtherAccountsTrans.setText("Other Accounts");
        rdoOtherAccountsTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoOtherAccountsTransActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panAccountDetails1.add(rdoOtherAccountsTrans, gridBagConstraints);

        cPanel1.add(panAccountDetails1);

        panSHGDetails1.add(cPanel1, new java.awt.GridBagConstraints());

        panTransactionType.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaction Details"));
        panTransactionType.setMinimumSize(new java.awt.Dimension(375, 100));
        panTransactionType.setPreferredSize(new java.awt.Dimension(375, 110));
        panTransactionType.setLayout(new java.awt.GridBagLayout());

        lblAccount1.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTransactionType.add(lblAccount1, gridBagConstraints);

        txtAmountTrans.setAllowAll(true);
        txtAmountTrans.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmountTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmountTransActionPerformed(evt);
            }
        });
        txtAmountTrans.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountTransFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransactionType.add(txtAmountTrans, gridBagConstraints);

        cLabel7.setText("Transaction Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTransactionType.add(cLabel7, gridBagConstraints);

        rdoPayment.setText("Payment");
        rdoPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPaymentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransactionType.add(rdoPayment, gridBagConstraints);

        rdoReceipt.setText("Reciept");
        rdoReceipt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoReceiptActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransactionType.add(rdoReceipt, gridBagConstraints);

        lblEmployeeNameTrans.setForeground(new java.awt.Color(0, 0, 102));
        lblEmployeeNameTrans.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblEmployeeNameTrans.setMaximumSize(new java.awt.Dimension(200, 18));
        lblEmployeeNameTrans.setMinimumSize(new java.awt.Dimension(200, 18));
        lblEmployeeNameTrans.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panTransactionType.add(lblEmployeeNameTrans, gridBagConstraints);

        lblEmployeeID1.setText("Employee ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTransactionType.add(lblEmployeeID1, gridBagConstraints);

        txtTransEmployeeID.setAllowAll(true);
        txtTransEmployeeID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTransEmployeeID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTransEmployeeIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransactionType.add(txtTransEmployeeID, gridBagConstraints);

        btnTransEmployeeID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnTransEmployeeID.setEnabled(false);
        btnTransEmployeeID.setMaximumSize(new java.awt.Dimension(21, 21));
        btnTransEmployeeID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnTransEmployeeID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnTransEmployeeID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransEmployeeIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTransactionType.add(btnTransEmployeeID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panSHGDetails1.add(panTransactionType, gridBagConstraints);

        panPFDetails.setLayout(new java.awt.GridBagLayout());

        lblBalText.setText("PF Balance :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 11);
        panPFDetails.add(lblBalText, gridBagConstraints);

        lblBalance.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblBalance.setPreferredSize(new java.awt.Dimension(85, 18));
        panPFDetails.add(lblBalance, new java.awt.GridBagConstraints());

        btnViewLedger.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnViewLedger.setText("View PFLedger");
        btnViewLedger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewLedgerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = -7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 0);
        panPFDetails.add(btnViewLedger, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panSHGDetails1.add(panPFDetails, gridBagConstraints);

        panEmployyeDetails.setMinimumSize(new java.awt.Dimension(310, 110));
        panEmployyeDetails.setPreferredSize(new java.awt.Dimension(310, 110));
        panEmployyeDetails.setLayout(new java.awt.GridBagLayout());

        panEmployeeTabTools.setMinimumSize(new java.awt.Dimension(228, 33));
        panEmployeeTabTools.setPreferredSize(new java.awt.Dimension(228, 33));
        panEmployeeTabTools.setLayout(new java.awt.GridBagLayout());

        btnSave_Employee.setText("Save");
        btnSave_Employee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave_EmployeeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panEmployeeTabTools.add(btnSave_Employee, gridBagConstraints);

        btnDeleteEmployee.setText("Delete");
        btnDeleteEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteEmployeeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panEmployeeTabTools.add(btnDeleteEmployee, gridBagConstraints);

        btnNew_Employee.setText("New");
        btnNew_Employee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_EmployeeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panEmployeeTabTools.add(btnNew_Employee, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        panEmployyeDetails.add(panEmployeeTabTools, gridBagConstraints);

        srpEmployeeTabCTable.setMaximumSize(new java.awt.Dimension(300, 180));
        srpEmployeeTabCTable.setMinimumSize(new java.awt.Dimension(300, 80));
        srpEmployeeTabCTable.setPreferredSize(new java.awt.Dimension(300, 80));

        tblEmployeeDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        tblEmployeeDetails.setMinimumSize(new java.awt.Dimension(45, 80));
        tblEmployeeDetails.setPreferredSize(new java.awt.Dimension(45, 80));
        tblEmployeeDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmployeeDetailsMouseClicked(evt);
            }
        });
        srpEmployeeTabCTable.setViewportView(tblEmployeeDetails);

        panEmployyeDetails.add(srpEmployeeTabCTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(-10, 0, 0, 0);
        panSHGDetails1.add(panEmployyeDetails, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 65);
        panSHGDetails1.add(lblTransTotalValue, gridBagConstraints);

        lblTransTot.setText("Total");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panSHGDetails1.add(lblTransTot, gridBagConstraints);

        panTransaction.add(panSHGDetails1, new java.awt.GridBagConstraints());

        tabSHGDetails.addTab("Transaction", panTransaction);

        panSalaryRecoveryOptions.setBorder(javax.swing.BorderFactory.createTitledBorder("Salary Recovery Options"));
        panSalaryRecoveryOptions.setMaximumSize(new java.awt.Dimension(700, 150));
        panSalaryRecoveryOptions.setMinimumSize(new java.awt.Dimension(700, 150));
        panSalaryRecoveryOptions.setPreferredSize(new java.awt.Dimension(700, 150));
        panSalaryRecoveryOptions.setLayout(new java.awt.GridLayout(1, 0));

        panPayType.setMaximumSize(new java.awt.Dimension(300, 150));
        panPayType.setMinimumSize(new java.awt.Dimension(300, 150));
        panPayType.setPreferredSize(new java.awt.Dimension(250, 150));
        panPayType.setLayout(new java.awt.GridBagLayout());

        rdoBulkEarnings.setText("Earnings");
        rdoBulkEarnings.setMaximumSize(new java.awt.Dimension(93, 27));
        rdoBulkEarnings.setMinimumSize(new java.awt.Dimension(93, 27));
        rdoBulkEarnings.setPreferredSize(new java.awt.Dimension(93, 27));
        rdoBulkEarnings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBulkEarningsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 1);
        panPayType.add(rdoBulkEarnings, gridBagConstraints);

        rdoBulkDeduct.setText("Deduction");
        rdoBulkDeduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBulkDeductActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPayType.add(rdoBulkDeduct, gridBagConstraints);

        rdoBulkContra.setText("Contribution");
        rdoBulkContra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBulkContraActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panPayType.add(rdoBulkContra, gridBagConstraints);

        lblBulkPayCode.setText("Pay Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 12);
        panPayType.add(lblBulkPayCode, gridBagConstraints);

        cboBulkPayCodes.setMinimumSize(new java.awt.Dimension(200, 21));
        cboBulkPayCodes.setPopupWidth(160);
        cboBulkPayCodes.setPreferredSize(new java.awt.Dimension(200, 21));
        cboBulkPayCodes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBulkPayCodesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPayType.add(cboBulkPayCodes, gridBagConstraints);

        btnBulkDisplay.setText("Display");
        btnBulkDisplay.setToolTipText("");
        btnBulkDisplay.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnBulkDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBulkDisplayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panPayType.add(btnBulkDisplay, gridBagConstraints);

        panSalaryRecoveryOptions.add(panPayType);

        panBulkPaymaster.add(panSalaryRecoveryOptions);
        panSalaryRecoveryOptions.getAccessibleContext().setAccessibleName("");

        panPaymasterList.setMaximumSize(new java.awt.Dimension(830, 280));
        panPaymasterList.setMinimumSize(new java.awt.Dimension(830, 280));
        panPaymasterList.setOpaque(false);
        panPaymasterList.setPreferredSize(new java.awt.Dimension(830, 280));
        panPaymasterList.setLayout(new java.awt.GridBagLayout());

        chkSelectAll.setText("Select All");
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panPaymasterList.add(chkSelectAll, gridBagConstraints);

        srpPaymasterList.setMinimumSize(new java.awt.Dimension(19, 22));

        tblPayMasterList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblPayMasterList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblPayMasterList.setMaximumSize(new java.awt.Dimension(2147483647, 0));
        tblPayMasterList.setMinimumSize(new java.awt.Dimension(350, 80));
        tblPayMasterList.setPreferredScrollableViewportSize(new java.awt.Dimension(880, 331));
        tblPayMasterList.setRowSelectionAllowed(false);
        tblPayMasterList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPayMasterListMouseClicked(evt);
            }
        });
        tblPayMasterList.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblPayMasterListPropertyChange(evt);
            }
        });
        srpPaymasterList.setViewportView(tblPayMasterList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.ipadx = 811;
        gridBagConstraints.ipady = 231;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panPaymasterList.add(srpPaymasterList, gridBagConstraints);

        lblNoOfRecords.setText("No. of Records Found : ");
        lblNoOfRecords.setMaximumSize(new java.awt.Dimension(140, 18));
        lblNoOfRecords.setMinimumSize(new java.awt.Dimension(140, 18));
        lblNoOfRecords.setPreferredSize(new java.awt.Dimension(140, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 70, 0, 0);
        panPaymasterList.add(lblNoOfRecords, gridBagConstraints);

        lblNoOfRecordsVal.setMaximumSize(new java.awt.Dimension(230, 85));
        lblNoOfRecordsVal.setMinimumSize(new java.awt.Dimension(80, 18));
        lblNoOfRecordsVal.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 18, 0, 0);
        panPaymasterList.add(lblNoOfRecordsVal, gridBagConstraints);

        lblSelectedCount.setText("Selected Items :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 7, 0, 0);
        panPaymasterList.add(lblSelectedCount, gridBagConstraints);

        lblSelectedCountVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblSelectedCountVal.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.ipady = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 18, 0, 0);
        panPaymasterList.add(lblSelectedCountVal, gridBagConstraints);

        panBulkPaymaster.add(panPaymasterList);

        tabSHGDetails.addTab("BulkMaster", panBulkPaymaster);

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
        chkPayStatus.setSelected(false);
        //chkPayTransaction.setSelected(false);
        txtAmount.setText("");
        rdoGeneralLedger.setSelected(false);
        rdoOtherAccounts.setSelected(false);
        txtAcctNo.setText("");
        txtProdIdTrans.setText("");
        cboPayCodes.setSelectedItem("");
        cboProdTypeCr.setSelectedItem("");
        tdtFromDt.setDateValue("");
        txtRecoveryMonth.setText("");
        txtAmountTrans.setText("");
        rdoGeneralLedgertrans.setSelected(false);
        rdoOtherAccountsTrans.setSelected(false);
        txtAcctNotrans.setText("");
        txtTransProductId.setText("");
        cboPayCodesTrans.setSelectedItem("");
        cboProdTypeCrtrans.setSelectedItem("");
    }

    /** To display a popUp window for viewing existing data */
    private void callView(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")) {
            viewMap.put(CommonConstants.MAP_NAME, "getPayMasterEdit");
        } else if (currAction.equalsIgnoreCase("Delete")) {
            viewMap.put(CommonConstants.MAP_NAME, "getSHGDelete");
        } else if (currAction.equalsIgnoreCase("Enquiry")) {
            viewMap.put(CommonConstants.MAP_NAME, "getSHGView");
        } else if (viewType.equals("EMPLOYEE_ID")) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectEmployee");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        } else if (viewType.equals("PAY_CODE")) {
            viewMap.put(CommonConstants.MAP_NAME, "getGroupLoanCustomerDetails");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        } else if (viewType.equals("ACCT_NO")) {
            whereMap.put("PROD_ID", txtTransProductId.getText());
            //commented by rishad for selecting interbranch account for particular account only
//            whereMap.put("SELECTED_BRANCH", TrueTransactMain.BRANCH_ID);
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
        } else if (viewType.equals("ACCT_NO_TRANS")) {
            whereMap.put("PROD_ID", txtProdIdTrans.getText());
            whereMap.put("SELECTED_BRANCH", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            if (observable.getProdType().equals("TD") || observable.getProdType().equals("TL") || observable.getProdType().equals("AB")) {
                if (observable.getProdType().equals("TL") || observable.getProdType().equals("AB")) {
                    whereMap.put("RECEIPT", "RECEIPT");
                }
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                        + CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCrtrans.getModel()).getKeyForSelected()));
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                        + CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCrtrans.getModel()).getKeyForSelected()));
            }
        } else if (viewType.equals("PROD_ID")) {
            HashMap where_map = new HashMap();
            where_map.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            String prodType = CommonUtil.convertObjToStr((((ComboBoxModel) (cboProdTypeCr).getModel())).getKeyForSelected());
            viewMap.put(CommonConstants.MAP_NAME, "InterMaintenance.getProductData" + CommonUtil.convertObjToStr(observable.getCbmProdTypCr().getKeyForSelected()));
            viewMap.put(CommonConstants.MAP_WHERE, where_map);
        }
        new ViewAll(this, viewMap).show();
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
                    txtEmployeeID.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEEID")));
                    lblEmployeeName.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_NAME")));
                    observable.setTxtEmployeeId(txtEmployeeID.getText());
                    observable.setLblEmployeeName(lblEmployeeName.getText());
                    updateCustomerInfo();
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                        btnEmployeeID.setEnabled(true);
                    }
                    cust_Id = CommonUtil.convertObjToStr(hashMap.get("CUST_ID"));
                } else if (viewType == "ACCT_NO") {
                    txtAcctNo.setText(CommonUtil.convertObjToStr(hashMap.get("ACCOUNTNO")));
                    observable.setAcctNo(txtAcctNo.getText());
                    // Added by nithya on 26-06-2018 for 0012105: Payroll - payroll individual, Recurring deposit A/c mapping sub number is not updating with act NUM.
                    String behavesLike = getProductBehavesLike();
                    if(behavesLike.equalsIgnoreCase("RECURRING")){
                      txtAcctNo.setText(CommonUtil.convertObjToStr(hashMap.get("ACCOUNTNO"))+"_1");
                      observable.setAcctNo(txtAcctNo.getText());
                    }
                } else if (viewType == "PROD_ID") {
                    txtTransProductId.setText(CommonUtil.convertObjToStr(hashMap.get("PROD_ID")));
                    observable.setProdID(txtTransProductId.getText());
                    //termLoanPayment();
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    this.setButtonEnableDisable();
                    txtEmployeeID.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEEID")));
                    lblEmployeeName.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_NAME")));
                    observable.setTxtEmployeeId(txtEmployeeID.getText());
                    observable.setLblEmployeeName(lblEmployeeName.getText());
                    observable.getData(hashMap);
                    tblSHGDetails.setModel(observable.getTblSHGDetails());
                    calcToatal();
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                    this.setButtonEnableDisable();
                    tabSHGDetails.add(panTransaction);
                    panTransaction.setName("Transaction");
                    transactionUI.addToScreen(panTransactionTrans);
                    observable.setTransactionOB(transactionUI.getTransactionOB());
                    resetTransactionUI();
                    txtEmployeeID.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEEID")));
                    lblEmployeeName.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_NAME")));
                    observable.getData(hashMap);
                    hashMap.put("TRANS_DT", currDt);
                    observable.getRemittData(hashMap);
                    calcToatal();
                    tblSHGDetails.setModel(observable.getTblSHGDetails());
                    ClientUtil.enableDisable(panSHGRecordDetails, false);
                    btnSHGNew.setEnabled(false);
                    btnSHGSave.setEnabled(false);
                    btnSHGDelete.setEnabled(false);
                }
                if (viewType == AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    ClientUtil.enableDisable(this, false);
                }
                hashMap = null;
                btnCancel.setEnabled(true);
            } else if (tabSHGDetails.getSelectedIndex() == 2) {
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
                    txtTransEmployeeID.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEEID")));
                    lblEmployeeNameTrans.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_NAME")));
                    observable.setTxtEmployeeId(txtTransEmployeeID.getText());
                    observable.setLblEmployeeName(lblEmployeeNameTrans.getText());
                    //updateCustomerInfo();
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                        btnTransEmployeeID.setEnabled(true);
                    }
                    cust_Id = CommonUtil.convertObjToStr(hashMap.get("CUST_ID"));
                } else if (viewType == "ACCT_NO_TRANS") {
                    txtAcctNotrans.setText(CommonUtil.convertObjToStr(hashMap.get("ACCOUNTNO")));
                    observable.setAcctNo(txtAcctNotrans.getText());
                } else if (viewType == "PROD_ID") {
                    txtProdIdTrans.setText(CommonUtil.convertObjToStr(hashMap.get("PROD_ID")));
                    observable.setProdID(txtProdIdTrans.getText());
                    //termLoanPayment();
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    this.setButtonEnableDisable();
                    txtTransEmployeeID.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEEID")));
                    lblEmployeeNameTrans.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_NAME")));
                    observable.setTxtEmployeeId(txtTransEmployeeID.getText());
                    observable.setLblEmployeeName(lblEmployeeNameTrans.getText());
                    observable.getData(hashMap);
                    //tblSHGDetails.setModel(observable.getTblSHGDetails());
                    //calcToatal();
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                    this.setButtonEnableDisable();
                    tabSHGDetails.add(panTransaction);
                    panTransaction.setName("Transaction");
                    transactionUI.addToScreen(panTransactionTrans);
                    observable.setTransactionOB(transactionUI.getTransactionOB());
                    resetTransactionUI();
                    txtTransEmployeeID.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEEID")));
                    lblEmployeeNameTrans.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_NAME")));
                    observable.getData(hashMap);
                    hashMap.put("TRANS_DT", currDt);
                    observable.getRemittData(hashMap);
                    //calcToatal();
                    //tblSHGDetails.setModel(observable.getTblSHGDetails());
                    ClientUtil.enableDisable(panSHGRecordDetails, false);
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
        if (tblSHGDetails.getRowCount() > 0) {
            for (int i = 0; i < tblSHGDetails.getRowCount(); i++) {
                if (CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(i, 2).toString()).equals("EARNINGS") && CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(i, 4).toString()).equals("Y")) {
                    netEarning = netEarning + CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(i, 3));
                } else if (CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(i, 2).toString()).equals("DEDUCTIONS")&& CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(i, 4).toString()).equals("Y")) {
                    netDeduction = netDeduction + CommonUtil.convertObjToDouble(tblSHGDetails.getValueAt(i, 3));
                }
                netSlary = netEarning - netDeduction;
                lblTotLimit.setText(CommonUtil.convertObjToStr(netSlary));
            }
        }
    }
public void calcTransToatal() {               // Edit Or Authorize Time
    double netTransAmt = 0;
    if (tblEmployeeDetails.getRowCount() > 0) {
        for (int i = 0; i < tblEmployeeDetails.getRowCount(); i++) {
            netTransAmt = netTransAmt + CommonUtil.convertObjToDouble(tblEmployeeDetails.getValueAt(i, 2));
            lblTransTotalValue.setText(CommonUtil.convertObjToStr(netTransAmt));
        }
    } else {
        lblTransTotalValue.setText("");
    }
     resetTransactionUI();
   // transactionUI.setCallingApplicantName(txtTransEmployeeID.getText());
    transactionUI.setCallingAmount(lblTransTotalValue.getText());
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
            singleAuthorizeMap.put("ACT_NUM", txtEmployeeID.getText());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);            
            authorize(authorizeMap, txtEmployeeID.getText());
            viewType = "";
            ClientUtil.enableDisable(panSHGDetails, false);
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
            if (transactionUI.getOutputTO().size() > 0) {
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            observable.doAction();
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        if (tabSHGDetails.getSelectedIndex() == 2) {
            btnSHGNewActionPerformed();
            observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
            ClientUtil.clearAll(this);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
            btnNew_Employee.setEnabled(true);
            //addRadioButtons();
        } else if (tabSHGDetails.getSelectedIndex() == 0) {
            setModified(true);
            btnSHGNew.setEnabled(true);
            observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
            setButtonEnableDisable();
            ClientUtil.clearAll(this);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
            panPaymentTransaction.setVisible(true);
            addRadioButtons();
        }
         else if (tabSHGDetails.getSelectedIndex() == 1) {
            setModified(true);
//            btnSHGNew.setEnabled(true);
            observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
            setButtonEnableDisable();
            ClientUtil.clearAll(this);
            observable.setStatus();
             ClientUtil.enableDisable(panBulkPaymaster, true);
            rdoBulkEarnings.setEnabled(true);
            rdoBulkDeduct.setEnabled(true);
            rdoBulkContra.setEnabled(true);
            cboBulkPayCodes.setEnabled(true);
            panPaymasterList.setEnabled(true);
            chkSelectAll.setEnabled(true);
            lblStatus.setText(observable.getLblStatus());
            addRadioButtons();
        }
        
        
//         ClientUtil.enableDisable(panBulkPaymaster, false);
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        if (tabSHGDetails.getSelectedIndex() == 0) {
            observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
            callView("Edit");
            lblStatus.setText("Edit");
            btnSHGNew.setEnabled(true);
            panPaymentTransaction.setVisible(true);
            //chkPayTransaction.setVisible(false);
            //lblPayTransaction.setVisible(false);
            btnEmployeeID.setEnabled(false);
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
        ClientUtil.enableDisable(panSHGDetails, false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        try {
            if (tabSHGDetails.getSelectedIndex() == 2) {
                String message = checkMandatoryTransData();
                if (message.trim().length() > 0) {
                    CommonMethods.displayAlert(message);
                    return;
                } else {
                    updateTransOBFields();
                    observable.setGetSelectedTab("TRANSACTION_DATA");
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
//                if (observable.isPayTrans() == true && tblSHGDetails.getRowCount() > 1) {
//                    ClientUtil.showAlertWindow("Multiple Pay codes are not allowed for transactions...Make it one!!");
//                    return;
//                } else {
                        int transactionSize = 0;
                        if (transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(lblTransTotalValue.getText()) > 0) {
                            ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                            return;
                        } else {
                            if (CommonUtil.convertObjToDouble(lblTransTotalValue.getText()) > 0) {
                                transactionSize = (transactionUI.getOutputTO()).size();
                                if (transactionSize != 1 && CommonUtil.convertObjToDouble(lblTransTotalValue.getText()) > 0) {
                                    ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction");
                                    return;
                                } else {
                                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                                }
                            } else if (transactionUI.getOutputTO().size() > 0 && transactionUI.getOutputTO() != null) {
                                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                            }
                        }
                        if (transactionSize == 0 && CommonUtil.convertObjToDouble(lblTransTotalValue.getText()) < 0) {
                            ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                            return;
                        } else if (transactionSize != 0) {
                            if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                                return;
                            }
                            if (transactionUI.getOutputTO().size() > 0) {
                                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                                try {
                                    savePerformed();
                                    btnCancel.setEnabled(true);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    java.util.logging.Logger.getLogger(PayRollIndividualUI.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                        } else if (transactionUI.getOutputTO().size() == 0 && observable.isPayTrans() == false) {
                            try {
                                savePerformed();
                                btnCancel.setEnabled(true);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                java.util.logging.Logger.getLogger(PayRollIndividualUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        // }
                    } else {
                        savePerformed();
                    }
                }
            } else if (tabSHGDetails.getSelectedIndex() == 0) {
                observable.setGetSelectedTab("MASTER_DATA");
                savePerformed();
            }
            else if (tabSHGDetails.getSelectedIndex() == 1) {
                observable.setGetSelectedTab("BULK_DATA");
                ArrayList PayRollList = new ArrayList();
                for (int i = 0; i < tblPayMasterList.getRowCount(); i++) {
                    if (((Boolean) tblPayMasterList.getValueAt(i, 0)).booleanValue()) {
                        ArrayList list = new ArrayList();
                        list.add(tblPayMasterList.getValueAt(i, 1));//empid
                        list.add(tblPayMasterList.getValueAt(i, 2));//paycode
                        list.add(tblPayMasterList.getValueAt(i, 3));//amount
                        list.add(tblPayMasterList.getValueAt(i, 7));//srl_no
                        PayRollList.add(list);
                    }
                }
                observable.setPayrollBukList(PayRollList);
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
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                displayTransDetailNew(observable.getProxyReturnMap());
            }else{
                ClientUtil.showMessageWindow("Save succesfully..!!");
            }
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
        for (int i = 0; i < keys.length; i++) {
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
                        transId = (String) transMap.get("BATCH_ID");
                    }
                    transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    //transIdMap.put(transMap.get("BATCH_ID"), "TRANSFER");// Commented and added the below code by nithya on 19-07-2018 for KD- 167 Need print for Payroll individual screen entries
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"),"TRANSFER");
                }
                transferCount++;
            }
        }
        if (cashCount > 0) {
            displayStr += cashDisplayStr;
        }
        if (transferCount > 0) {
            displayStr += transferDisplayStr;
        }
        if (!displayStr.equals("")) {
            ClientUtil.showMessageWindow("" + displayStr);
        }
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        if (yesNo == 0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("TransDt", currDt);
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            Object keys1[] = transIdMap.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                paramMap.put("TransId", keys1[i]);
                ttIntgration.setParam(paramMap);
                if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {
                    //ttIntgration.integrationForPrint("MDSReceiptsTransfer", false);// Commented and added the below code by nithya on 19-07-2018 for KD-167 Need print for Payroll individual screen entries
                    ttIntgration.integrationForPrint("ReceiptPayment", false);
                } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys1[i])).equals("DEBIT")) {
                    ttIntgration.integrationForPrint("CashPayment", false);
                } else {
                    ttIntgration.integrationForPrint("CashReceipt", false);
                }
            }
        }
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        viewType = "CANCEL";
        lblStatus.setText("");
        rdoDeduct.setSelected(false);
        rdoEarnings.setSelected(false);
        rdocontra.setSelected(false);
        rdoGeneralLedger.setSelected(false);
        rdoOtherAccounts.setSelected(false);
        rdoGeneralLedgertrans.setSelected(false);
        rdoOtherAccountsTrans.setSelected(false);
        rdoPayment.setSelected(false);
        rdoReceipt.setSelected(false);
        rdoDeductTrans.setSelected(false);
        rdoEarningsTrans.setSelected(false);
        rdocontratrans.setSelected(false);
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
        buttonEnableDisableEmp(false);
        txtEmployeeID.setEnabled(false);
        lblTotLimit.setText("");
        txtEmployeeID.setText("");
        lblEmployeeName.setText("");
        txtTransEmployeeID.setText("");
        lblEmployeeNameTrans.setText("");
        lblTransTotalValue.setText("");
        resetSHGDetails();
        resetTransactionUI();
        //tabSHGDetails.remove(panTransaction);
        observable.setPaycodeType("");
        employeeDetailsUI.updateCustomerInfo("");
        removeRadioButtons();
        btnEmployeeID.setEnabled(false);
        btnTransEmployeeID.setEnabled(false);
        btnTransProductId.setEnabled(false);
        btnProdIdTrans.setEnabled(false);
        btnAccNo.setEnabled(false);
        btnAccNoTrans.setEnabled(false);
        lblBalance.setText("");
        panDrCr.setVisible(false);
        observable.resetEmployeeTableValues();
    }//GEN-LAST:event_btnCancelActionPerformed

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

private void tblSHGDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSHGDetailsMousePressed
    // TODO add your handling code here:        
    updateMode = true;
    updateTab = tblSHGDetails.getSelectedRow();
    observable.setNewData(false);
    String st = CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(), 0));
    observable.populateSHGDetails(st);
    update(null, null);
    if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
            || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
            || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
        buttonEnableDisable(false);
        btnAccNo.setEnabled(false);
        btnTransProductId.setEnabled(false);
        ClientUtil.enableDisable(panSHGRecordDetails, false);
    } else {
        buttonEnableDisable(true);
        btnSHGNew.setEnabled(false);
        ClientUtil.enableDisable(panSHGRecordDetails, true);
        if (rdoGeneralLedger.isSelected()) {
            rdoGeneralLedgerActionPerformed(null);
        }
    }
    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
        btnSHGNew.setEnabled(false);
        btnSHGSave.setEnabled(true);
        btnSHGDelete.setEnabled(true);
        txtEmployeeID.setEnabled(true);
    }
}//GEN-LAST:event_tblSHGDetailsMousePressed

private void cboPayCodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPayCodesActionPerformed
// TODO add your handling code here:
    //addRadioButtons();
}//GEN-LAST:event_cboPayCodesActionPerformed

private void btnTransProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransProductIdActionPerformed
    callView("PROD_ID");
}//GEN-LAST:event_btnTransProductIdActionPerformed

private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
    callView("ACCT_NO");
}//GEN-LAST:event_btnAccNoActionPerformed

private void txtAcctNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcctNoFocusLost
}//GEN-LAST:event_txtAcctNoFocusLost

private void txtAcctNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAcctNoActionPerformed
}//GEN-LAST:event_txtAcctNoActionPerformed

private void cboProdTypeCrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeCrActionPerformed
    // TODO add your handling code here:    
}//GEN-LAST:event_cboProdTypeCrActionPerformed

private void rdoOtherAccountsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoOtherAccountsActionPerformed
    rdoOtherAccounts.setSelected(true);
    enableDisablePanAccounts(true);
    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
        cboProdTypeCr.setModel(observable.getCbmProdTypCr());
    }
}//GEN-LAST:event_rdoOtherAccountsActionPerformed

private void rdoGeneralLedgerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoGeneralLedgerActionPerformed
// TODO add your handling code here:
    rdoGeneralLedger.setSelected(true);
    //rdoOtherAccounts.setSelected(false);
    enableDisablePanAccounts(false);
    observable.setProdType("GL");
    cboProdTypeCr.setSelectedIndex(0);
    txtAcctNo.setText("");
    txtTransProductId.setText("");
}//GEN-LAST:event_rdoGeneralLedgerActionPerformed

private void btnEmployeeIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmployeeIDActionPerformed
// TODO add your handling code here:   
    callView("EMPLOYEE_ID");
}//GEN-LAST:event_btnEmployeeIDActionPerformed

private void txtEmployeeIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmployeeIDActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtEmployeeIDActionPerformed

private void btnSHGDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSHGDeleteActionPerformed
    // TODO add your handling code here:
    String custId = CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(tblSHGDetails.getSelectedRow(), 0));
    List loanLst = null;
    if (observable.getActionType() != ClientConstants.ACTIONTYPE_NEW) {
        observable.deleteTableData(custId, tblSHGDetails.getSelectedRow());
        observable.resetSHGDetails();
        resetSHGDetails();
        calcToatal();
        buttonEnableDisable(false);
        btnSHGNew.setEnabled(true);
        btnSHGSave.setEnabled(true);
    }
}//GEN-LAST:event_btnSHGDeleteActionPerformed

private void btnSHGSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSHGSaveActionPerformed
    // TODO add your handling code here:
    try {
        String message = checkMandatoryData();
        if (message.trim().length() > 0) {
            CommonMethods.displayAlert(message);
            return;
        } else {
            updateOBFields();
            if (tdtFromDt.getDateValue() != null && txtRecoveryMonth.getText() != null) {
                observable.addToTable(updateTab, updateMode);
                tblSHGDetails.setModel(observable.getTblSHGDetails());
                observable.resetSHGDetails();
                resetSHGDetails();
                calcToatal();
                ClientUtil.enableDisable(panSHGRecordDetails, false);
                removeRadioButtons();
                buttonEnableDisable(false);
                btnSHGNew.setEnabled(true);
            } else {
                ClientUtil.showAlertWindow("Recovery Month and Recovery from date should not be empty !!!");
                return;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}//GEN-LAST:event_btnSHGSaveActionPerformed

    public String checkMandatoryData() {
        String warningMessage = "";        
        StringBuffer strBMandatory = new StringBuffer();
        if ((txtEmployeeID.getText().length() == 0) || (txtEmployeeID.getText() == null)) {
            strBMandatory.append("Employee should not be empty!!!");
            strBMandatory.append("\n");
        } else if (rdoEarnings.isSelected() == false && rdoDeduct.isSelected() == false && rdocontra.isSelected() == false) {
            strBMandatory.append("Please select one of the pay type!!!");
            strBMandatory.append("\n");
        } else if (cboPayCodes.getSelectedItem().equals("") && cboPayCodes.getSelectedItem() == null) {
            strBMandatory.append("Please select any Pay code!!!");
            strBMandatory.append("\n");
        } else if (rdoGeneralLedger.isSelected() == false && rdoOtherAccounts.isSelected() == false) {
            strBMandatory.append("Please select any account type!!!");
            strBMandatory.append("\n");
        } else if (rdoOtherAccounts.isSelected() == true) {
            if (cboProdTypeCr.getSelectedItem().equals("") && cboPayCodes.getSelectedItem() == null) {
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
        } else if ((tdtFromDt.getDateValue().length() == 0) || (tdtFromDt.getDateValue() == null)) {
            strBMandatory.append("Please select recovery Date!!!");
            strBMandatory.append("\n");
        } else if ((DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()).compareTo(currDt) < 0)) {
            strBMandatory.append("From date should be after application date!!!");
            strBMandatory.append("\n");
        } else if ((txtRecoveryMonth.getText().length() == 0) || (txtRecoveryMonth.getText() == null)) {
            strBMandatory.append("Please enter the recovery month!!!");
            strBMandatory.append("\n");
        }
        if (!updateMode) {
            String PayCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboPayCodes.getModel()).getKeyForSelected());
            if (observable.checkPayCodeExistence(PayCode, txtEmployeeID.getText())) {
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
    }
  public String checkMandatoryTableData() {
         String warningMessage = "";
           StringBuffer strBMandatory = new StringBuffer();
       if ((txtTransEmployeeID.getText().length() == 0) || (txtTransEmployeeID.getText() == null)) {
            strBMandatory.append("Employee should not be empty!!!");
            strBMandatory.append("\n");
        } 
       else if((txtAmountTrans.getText().length() == 0) || (txtAmountTrans.getText() == null))
       {
            strBMandatory.append("Please enter the amount!!!");
            strBMandatory.append("\n");
       }
           warningMessage = strBMandatory.toString();
        return warningMessage;
  }
    public String checkMandatoryTransData() {
        String warningMessage = "";
        StringBuffer strBMandatory = new StringBuffer();
//        if ((txtTransEmployeeID.getText().length() == 0) || (txtTransEmployeeID.getText() == null)) {
//            strBMandatory.append("Employee should not be empty!!!");
//            strBMandatory.append("\n");
//        } else
            if (rdoEarningsTrans.isSelected() == false && rdoDeductTrans.isSelected() == false && rdocontratrans.isSelected() == false) {
            strBMandatory.append("Please select one of the pay type!!!");
            strBMandatory.append("\n");
        } else if (cboPayCodesTrans.getSelectedItem().equals("") && cboPayCodesTrans.getSelectedItem() == null) {
            strBMandatory.append("Please select any Pay code!!!");
            strBMandatory.append("\n");
        } else if (rdoGeneralLedgertrans.isSelected() == false && rdoOtherAccountsTrans.isSelected() == false) {
            strBMandatory.append("Please select any account types!!!");
            strBMandatory.append("\n");
        } else if (rdoReceipt.isSelected() == false && rdoPayment.isSelected() == false) {
            strBMandatory.append("Please select Trans Type!!!");
            strBMandatory.append("\n");
        } else if (rdoOtherAccountsTrans.isSelected() == true) {
            if (cboProdTypeCrtrans.getSelectedItem().equals("") && cboProdTypeCrtrans.getSelectedItem() == null) {
                strBMandatory.append("Please select any Product Type!!!");
                strBMandatory.append("\n");
            } else if ((txtProdIdTrans.getText().length() == 0) || (txtProdIdTrans.getText() == null)) {
                strBMandatory.append("Please select any product!!!");
                strBMandatory.append("\n");
            } else if ((txtAcctNotrans.getText().length() == 0) || (txtAcctNotrans.getText() == null)) {
                strBMandatory.append("Please select any account!!!");
                strBMandatory.append("\n");
            }
        } else if ((lblTransTotalValue.getText().length() == 0) || (lblTransTotalValue.getText() == null)) {
            strBMandatory.append("Please enter the amount!!!");
            strBMandatory.append("\n");
        } else if (rdocontratrans.isSelected() == true & (rdoDr.isSelected()==false && rdoCr.isSelected()==false)) {
            strBMandatory.append("Please select Dr or Cr for Contra Transaction!!!");
            strBMandatory.append("\n");
        //} else if ((DateUtil.getDateMMDDYYYY(tdtFromDtTrans.getDateValue()).compareTo(currDt) < 0)) {
        //    strBMandatory.append("From date should be after application date!!!");
        //    strBMandatory.append("\n");
        //} else if ((txtRecoveryMonthTrans.getText().length() == 0) || (txtRecoveryMonthTrans.getText() == null)) {
           // strBMandatory.append("Please enter the recovery month!!!");
           // strBMandatory.append("\n");
        }
        warningMessage = strBMandatory.toString();
        return warningMessage;
    }

    private void btnSHGNewActionPerformed() {
        //updateMode = false;
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        btnTransEmployeeID.setEnabled(false);
         txtAmountTrans.setEnabled(false);
        btnProdIdTrans.setEnabled(false);
        btnAccNoTrans.setEnabled(false);
        cboPayCodesTrans.setEnabled(true);
        //buttonEnableDisable(false);
        // btnSHGSave.setEnabled(true);
        observable.setNewData(true);
        rdoGeneralLedgertrans.setEnabled(true);
        rdoOtherAccountsTrans.setEnabled(true);
        rdoEarningsTrans.setEnabled(true);
        rdocontratrans.setEnabled(true);
        rdoDeductTrans.setEnabled(true);
        cboProdTypeCrtrans.setEnabled(false);
        rdoPayment.setEnabled(true);
        rdoReceipt.setEnabled(true);
        ClientUtil.enableDisable(panAcctNo1, false);
        ClientUtil.enableDisable(panAccHd1, false);
        ClientUtil.enableDisable(panAccHd2, false);
        //chkPayStatusTrans.setEnabled(true);
        //chkPayTransaction.setEnabled(true);
        txtAmountTrans.setEnabled(false);
        //txtRecoveryMonthTrans.setEnabled(true);
        //tdtFromDtTrans.setEnabled(true);
        if (tblSHGDetails.getRowCount() <= 0) {
        }
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        resetTransactionUI();
        transactionUI.resetObjects();
        transactionUI.setSourceScreen("PAY_ROLL_MASTER");
       // transactionUI.setCallingApplicantName("");
        addRadioButtons();
        setButtonEnableDisable();
    }

private void btnSHGNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSHGNewActionPerformed
    // TODO add your handling code here:
    updateMode = false;
    btnEmployeeID.setEnabled(true);
    btnTransProductId.setEnabled(false);
    btnAccNo.setEnabled(false);
    cboPayCodes.setEnabled(true);
    buttonEnableDisable(false);
    btnSHGSave.setEnabled(true);
    rdoGeneralLedger.setEnabled(true);
    rdoOtherAccounts.setEnabled(true);
    rdoEarnings.setEnabled(true);
    rdocontra.setEnabled(true);
    rdoDeduct.setEnabled(true);
    cboProdTypeCr.setEnabled(false);
    ClientUtil.enableDisable(panAcctNo, false);
    ClientUtil.enableDisable(panAccHd, false);
    chkPayStatus.setEnabled(true);
    chkPayStatus.setSelected(true);
    //chkPayTransaction.setEnabled(true);
    txtAmount.setEnabled(true);
    txtRecoveryMonth.setEnabled(true);
    tdtFromDt.setEnabled(true);
    if (tblSHGDetails.getRowCount() <= 0) {
    }
    observable.setNewData(true);
    transactionUI.cancelAction(false);
    transactionUI.setButtonEnableDisable(true);
    transactionUI.resetObjects();
    transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
    resetTransactionUI();
    transactionUI.resetObjects();
    transactionUI.setSourceScreen("PAY_ROLL_MASTER");
    transactionUI.setCallingApplicantName("");
    addRadioButtons();
}//GEN-LAST:event_btnSHGNewActionPerformed

    private void resetTransactionUI() {
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        transactionUI.resetObjects();
        transactionUI.setSourceScreen("PAY_ROLL_MASTER");
        transactionUI.setCallingApplicantName(lblEmployeeNameTrans.getText());// added by nithya on 29-03-2017 for 6094
    }

private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountActionPerformed
// TODO add your handling code here:
    resetTransactionUI();
    transactionUI.setCallingAmount(txtAmount.getText());
}//GEN-LAST:event_txtAmountActionPerformed

private void txtTransProductIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTransProductIdFocusLost
// TODO add your handling code here: 
    if (txtTransProductId.getText() != null && txtTransProductId.getText().length() > 0) {
        observable.setProdID(txtTransProductId.getText());
        //termLoanPayment();
    }
}//GEN-LAST:event_txtTransProductIdFocusLost

private void rdoEarningsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoEarningsActionPerformed
// TODO add your handling code here:
    if (rdoEarnings.isSelected() == true) {
        try {
            observable.fillDropdown("EARNINGS");
            cboPayCodes.setModel(observable.getCbmPayCode());
            rdoGeneralLedgerActionPerformed(null);
            rdoOtherAccounts.setEnabled(false);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(PayRollIndividualUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}//GEN-LAST:event_rdoEarningsActionPerformed

private void rdoDeductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDeductActionPerformed
// TODO add your handling code here:
    if (rdoDeduct.isSelected() == true) {
        try {
            observable.fillDropdown("DEDUCTIONS");
            cboPayCodes.setModel(observable.getCbmPayCode());
            rdoOtherAccounts.setEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(PayRollIndividualUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}//GEN-LAST:event_rdoDeductActionPerformed

private void rdocontraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdocontraActionPerformed
// TODO add your handling code here:
    if (rdocontra.isSelected() == true) {
        try {
            observable.fillDropdown("CONTRA");
            cboPayCodes.setModel(observable.getCbmPayCode());
            rdoOtherAccounts.setEnabled(true);
        } catch (Exception ex) {
            Logger.getLogger(PayRollIndividualUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}//GEN-LAST:event_rdocontraActionPerformed

private void txtTransEmployeeIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTransEmployeeIDActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtTransEmployeeIDActionPerformed

private void btnTransEmployeeIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransEmployeeIDActionPerformed
// TODO add your handling code here:
    callView("EMPLOYEE_ID");
}//GEN-LAST:event_btnTransEmployeeIDActionPerformed

private void cboPayCodesTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPayCodesTransActionPerformed
// TODO add your handling code here:
    if(txtTransEmployeeID.getText().length()>0){
        if (!cboPayCodesTrans.getSelectedItem().equals("") && cboPayCodesTrans.getSelectedItem() != null) {
            String PayCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboPayCodesTrans.getModel()).getKeyForSelected());
            if(observable.setPFBalance(txtTransEmployeeID.getText(),PayCode)){
                panPFDetails.setVisible(true);
                lblBalance.setText(CommonUtil.convertObjToStr(observable.getPfBalance()));    
            }else{
               panPFDetails.setVisible(false);
            }
        }
    }
}//GEN-LAST:event_cboPayCodesTransActionPerformed

private void rdocontratransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdocontratransActionPerformed
// TODO add your handling code here:
    if (rdocontratrans.isSelected() == true) {
        try {
            observable.fillDropdown("CONTRA");
            cboPayCodesTrans.setModel(observable.getCbmPayCode());
            rdoOtherAccountsTrans.setEnabled(true);
            panDrCr.setVisible(true);
            rdoCr.setEnabled(true);
            rdoDr.setEnabled(true);
        } catch (Exception ex) {
            Logger.getLogger(PayRollIndividualUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}//GEN-LAST:event_rdocontratransActionPerformed

private void rdoDeductTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDeductTransActionPerformed
// TODO add your handling code here:
    if (rdoDeductTrans.isSelected() == true) {
        try {
            observable.fillDropdown("DEDUCTIONS");
            cboPayCodesTrans.setModel(observable.getCbmPayCode());
            rdoOtherAccountsTrans.setEnabled(true);
            panDrCr.setVisible(false);
            rdoCr.setEnabled(false);
            rdoDr.setEnabled(false);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(PayRollIndividualUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}//GEN-LAST:event_rdoDeductTransActionPerformed

private void rdoEarningsTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoEarningsTransActionPerformed
// TODO add your handling code here:
    if (rdoEarningsTrans.isSelected() == true) {
        try {
            observable.fillDropdown("EARNINGS");
            cboPayCodesTrans.setModel(observable.getCbmPayCode());
            //rdoGeneralLedgertransActionPerformed(null);
            //rdoOtherAccounts.setEnabled(false);
            panDrCr.setVisible(false);
            rdoCr.setEnabled(false);
            rdoDr.setEnabled(false);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(PayRollIndividualUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}//GEN-LAST:event_rdoEarningsTransActionPerformed

private void cboProdTypeCrtransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeCrtransActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_cboProdTypeCrtransActionPerformed

private void txtProdIdTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProdIdTransActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtProdIdTransActionPerformed

private void txtProdIdTransFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProdIdTransFocusLost
// TODO add your handling code here:
    if (txtProdIdTrans.getText() != null && txtProdIdTrans.getText().length() > 0) {
        observable.setProdID(txtProdIdTrans.getText());
        //termLoanPayment();
    }
}//GEN-LAST:event_txtProdIdTransFocusLost

private void btnProdIdTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdIdTransActionPerformed
// TODO add your handling code here:
    callView("PROD_ID");
}//GEN-LAST:event_btnProdIdTransActionPerformed

private void rdoPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPaymentActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_rdoPaymentActionPerformed

private void rdoReceiptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoReceiptActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_rdoReceiptActionPerformed

private void txtAmountTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountTransActionPerformed
// TODO add your handling code here:
//    transactionUI.setCallingApplicantName(txtTransEmployeeID.getText());
//    transactionUI.setCallingAmount(txtAmountTrans.getText());
}//GEN-LAST:event_txtAmountTransActionPerformed

private void txtAcctNotransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAcctNotransActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtAcctNotransActionPerformed

private void txtAcctNotransFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcctNotransFocusLost
// TODO add your handling code here:
}//GEN-LAST:event_txtAcctNotransFocusLost

private void btnAccNoTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoTransActionPerformed
// TODO add your handling code here:
    callView("ACCT_NO_TRANS");
}//GEN-LAST:event_btnAccNoTransActionPerformed

private void txtAmountTransFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountTransFocusLost
// TODO add your handling code here:
//    resetTransactionUI();
//    transactionUI.setCallingApplicantName(txtTransEmployeeID.getText());
//    transactionUI.setCallingAmount(txtAmountTrans.getText());
}//GEN-LAST:event_txtAmountTransFocusLost

private void rdoGeneralLedgertransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoGeneralLedgertransActionPerformed
// TODO add your handling code here:
    rdoGeneralLedgertrans.setSelected(true);
    //rdoOtherAccounts.setSelected(false);
    TransEnableDisablePanAccounts(false);
    observable.setProdType("GL");
    cboProdTypeCrtrans.setSelectedIndex(-1);
    txtAcctNotrans.setText("");
    txtProdIdTrans.setText("");
}//GEN-LAST:event_rdoGeneralLedgertransActionPerformed

private void rdoOtherAccountsTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoOtherAccountsTransActionPerformed
// TODO add your handling code here:
    rdoOtherAccountsTrans.setSelected(true);
    TransEnableDisablePanAccounts(true);
    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
        cboProdTypeCrtrans.setModel(observable.getCbmProdTypCr());
    }
}//GEN-LAST:event_rdoOtherAccountsTransActionPerformed

private void btnViewLedgerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewLedgerActionPerformed
        // TODO add your handling code here:
       if(txtTransEmployeeID.getText().length()>0){
            if (!cboPayCodesTrans.getSelectedItem().equals("") && cboPayCodesTrans.getSelectedItem() != null) {  
                String PayCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboPayCodesTrans.getModel()).getKeyForSelected());
                if(observable.setPFBalance(txtTransEmployeeID.getText(),PayCode)){
                    TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    paramMap.put("EmpId", txtTransEmployeeID.getText());
                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                    paramMap.put("AsOnDate", currDt.clone());
                    ttIntgration.setParam(paramMap);
                    ttIntgration.integration("PFlEDGER");   
                } else{
                    ClientUtil.displayAlert("No data!!! ");
                }               
            } else {
                ClientUtil.displayAlert("Pay Code Should Not Be Empty!!! ");
            }
       }else{
            ClientUtil.displayAlert("Employee Should Not Be Empty!!! ");
       }     
}//GEN-LAST:event_btnViewLedgerActionPerformed

    private void tblEmployeeDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmployeeDetailsMouseClicked
        updateMode = true;
        updateTab = tblEmployeeDetails.getSelectedRow();
        observable.setNewData(false);
        String st = CommonUtil.convertObjToStr(tblEmployeeDetails.getValueAt(tblEmployeeDetails.getSelectedRow(), 1));
        String amt = CommonUtil.convertObjToStr(tblEmployeeDetails.getValueAt(tblEmployeeDetails.getSelectedRow(), 2));
        txtTransEmployeeID.setText(st);
        txtAmountTrans.setText(amt);
        buttonEnableDisableEmp(true);
    }//GEN-LAST:event_tblEmployeeDetailsMouseClicked

    private void btnNew_EmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_EmployeeActionPerformed
        updateMode = false;
        txtAmountTrans.setText("");
        txtTransEmployeeID.setText("");
        btnSave_Employee.setEnabled(true);
        btnTransEmployeeID.setEnabled(true);
        txtAmountTrans.setEnabled(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNew_EmployeeActionPerformed

    private void btnSave_EmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave_EmployeeActionPerformed
        // TODO add your handling code here:
        String message = checkMandatoryTableData();
        if (message.trim().length() > 0) {
            CommonMethods.displayAlert(message);
            return;
        } else {
            updateTransOBFields();
            updateMultiOBFields();
            observable.insertEmployeeToTable(updateTab, updateMode);
            tblEmployeeDetails.setModel(observable.getTblEmployeeDetails());
            calcTransToatal();
            txtTransEmployeeID.setText("");
            lblEmployeeNameTrans.setText("");
            txtAmountTrans.setText("");
            btnTransEmployeeID.setEnabled(false);
            txtAmountTrans.setEnabled(false);
            buttonEnableDisableEmp(false);
            btnNew_Employee.setEnabled(true);
        }
       //  System.out.println("data "+tblEmployeeDetails.getValueAt(1, 1)); 
        
    }//GEN-LAST:event_btnSave_EmployeeActionPerformed

    private void btnDeleteEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteEmployeeActionPerformed
        String employeeId = CommonUtil.convertObjToStr(tblEmployeeDetails.getValueAt(tblEmployeeDetails.getSelectedRow(), 1));
        observable.deleteEmployeeTableData(employeeId, tblEmployeeDetails.getSelectedRow());
        tblEmployeeDetails.setModel(observable.getTblEmployeeDetails());
        calcTransToatal();
        txtTransEmployeeID.setText("");
        txtAmountTrans.setText("");
        buttonEnableDisable(false);
        btnNew_Employee.setEnabled(true);
        btnSave_Employee.setEnabled(true);
    }//GEN-LAST:event_btnDeleteEmployeeActionPerformed

    private void rdoBulkEarningsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBulkEarningsActionPerformed
        // TODO add your handling code here:
        if (rdoBulkEarnings.isSelected() == true) {
        try {
            observable.fillBulkDropdown("EARNINGS");
            cboBulkPayCodes.setModel(observable.getCbmBulkPayCode());
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(PayRollIndividualUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }//GEN-LAST:event_rdoBulkEarningsActionPerformed

    private void rdoBulkDeductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBulkDeductActionPerformed
        // TODO add your handling code here:
         if (rdoBulkDeduct.isSelected() == true) {
        try {
            observable.fillBulkDropdown("DEDUCTIONS");
            cboBulkPayCodes.setModel(observable.getCbmBulkPayCode());
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(PayRollIndividualUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }//GEN-LAST:event_rdoBulkDeductActionPerformed

    private void rdoBulkContraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBulkContraActionPerformed
        // TODO add your handling code here:
        if (rdoBulkContra.isSelected() == true) {
            try {
                observable.fillBulkDropdown("CONTRA");
                cboBulkPayCodes.setModel(observable.getCbmBulkPayCode());
                rdoOtherAccounts.setEnabled(true);
            } catch (Exception ex) {
                Logger.getLogger(PayRollIndividualUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_rdoBulkContraActionPerformed

    private void cboBulkPayCodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBulkPayCodesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboBulkPayCodesActionPerformed

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // Add your handling code here:
        observable.setSelectAll(tblPayMasterList, new Boolean(chkSelectAll.isSelected()));
        setSelectedRecord();
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void tblPayMasterListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPayMasterListMouseClicked
        // TODO add your handling code here:
        double totAmount = 0;
        int totalCount = 0;
        String st = "";
        for (int i = 0; i < tblPayMasterList.getRowCount(); i++) {
            st = CommonUtil.convertObjToStr(tblPayMasterList.getValueAt(i, 0));
            if (st.equals("true")) {
//                totAmount = totAmount + CommonUtil.convertObjToDouble(tblPayMasterList.getValueAt(i, 12)).doubleValue();
                totalCount++;
            }
        }
        lblSelectedCountVal.setText(String.valueOf(totalCount));
        
    }//GEN-LAST:event_tblPayMasterListMouseClicked

    private void tblPayMasterListPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblPayMasterListPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPayMasterListPropertyChange

    private void btnBulkDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBulkDisplayActionPerformed
        // TODO add your handling code here:
        String behavesLike = "";
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
             String PayCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboBulkPayCodes.getModel()).getKeyForSelected());
            viewMap.put("PAY_CODE", PayCode);
            viewMap.put(CommonConstants.MAP_NAME, "getBulkPaymaserDetails");
            try {
                ArrayList heading = observable.insertTableData(viewMap, tblPayMasterList);
                heading = null;
                tblPayMasterList.setAutoCreateRowSorter(true);
                lblNoOfRecordsVal.setText(String.valueOf(tblPayMasterList.getRowCount()));
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
        viewMap = null;
        whereMap = null;
    }//GEN-LAST:event_btnBulkDisplayActionPerformed

    private void enableDisablePanAccounts(boolean flag) {
        cboProdTypeCr.setEnabled(flag);
        txtTransProductId.setEnabled(flag);
        ClientUtil.enableDisable(panAcctNo, flag);
        ClientUtil.enableDisable(panAccHd, flag);
        txtAcctNo.setEnabled(flag);
        btnAccNo.setEnabled(flag);
        btnTransProductId.setEnabled(flag);
    }

    private void TransEnableDisablePanAccounts(boolean flag) {
        cboProdTypeCrtrans.setEnabled(flag);
        txtProdIdTrans.setEnabled(flag);
        ClientUtil.enableDisable(panAccHd1, flag);
        ClientUtil.enableDisable(panAccHd2, flag);
        txtAcctNotrans.setEnabled(flag);
        btnAccNoTrans.setEnabled(flag);
        btnProdIdTrans.setEnabled(flag);
    }
     // Added by nithya on 26-06-2018 for 0012105: Payroll - payroll individual, Recurring deposit A/c mapping sub number is not updating with act NUM.
     private String getProductBehavesLike() {
        String behavesLike = "";
        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected());
        if (prodType.equalsIgnoreCase("TD")) {
            String prodId = txtTransProductId.getText();
            HashMap prodIdMap = new HashMap();
            prodIdMap.put("PROD_ID", prodId);
            List behavesLikeList = ClientUtil.executeQuery("getProductBehavesLike", prodIdMap);
            if (behavesLikeList != null && behavesLikeList.size() > 0) {
                prodIdMap = (HashMap) behavesLikeList.get(0);
                if (prodIdMap != null && prodIdMap.size() > 0 && prodIdMap.containsKey("BEHAVES_LIKE")) {
                    behavesLike = CommonUtil.convertObjToStr(prodIdMap.get("BEHAVES_LIKE"));

                }
            }
        }
        return behavesLike;
    }

    private void displayAlert(String message) {
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
        private void setSelectedRecord() {
        int count = 0;
        if (tblPayMasterList.getRowCount() > 0) {
            for (int i = 0, j = tblPayMasterList.getRowCount(); i < j; i++) {
                if (((Boolean) tblPayMasterList.getValueAt(i, 0)).booleanValue()) {
                    count += 1;
                }
            }
        }
        lblSelectedCountVal.setText(String.valueOf(count));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnAccNoTrans;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBulkDisplay;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDeleteEmployee;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnEmployeeID;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButtonGroup btnGrpDrCr;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNew_Employee;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProdIdTrans;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSHGDelete;
    private com.see.truetransact.uicomponent.CButton btnSHGNew;
    private com.see.truetransact.uicomponent.CButton btnSHGSave;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSave_Employee;
    private com.see.truetransact.uicomponent.CButton btnTransEmployeeID;
    private com.see.truetransact.uicomponent.CButton btnTransProductId;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnViewLedger;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CLabel cLabel4;
    private com.see.truetransact.uicomponent.CLabel cLabel5;
    private com.see.truetransact.uicomponent.CLabel cLabel6;
    private com.see.truetransact.uicomponent.CLabel cLabel7;
    private com.see.truetransact.uicomponent.CLabel cLabel9;
    private com.see.truetransact.uicomponent.CMenuBar cMenuBar1;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CComboBox cboBulkPayCodes;
    private com.see.truetransact.uicomponent.CComboBox cboPayCodes;
    private com.see.truetransact.uicomponent.CComboBox cboPayCodesTrans;
    private com.see.truetransact.uicomponent.CComboBox cboProdTypeCr;
    private com.see.truetransact.uicomponent.CComboBox cboProdTypeCrtrans;
    private com.see.truetransact.uicomponent.CCheckBox chkPayStatus;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblAccNo1;
    private com.see.truetransact.uicomponent.CLabel lblAccount;
    private com.see.truetransact.uicomponent.CLabel lblAccount1;
    private com.see.truetransact.uicomponent.CLabel lblBalText;
    private com.see.truetransact.uicomponent.CLabel lblBalance;
    private com.see.truetransact.uicomponent.CLabel lblBulkPayCode;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeID;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeID1;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeName;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeNameTrans;
    private com.see.truetransact.uicomponent.CLabel lblFromDt;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecords;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecordsVal;
    private com.see.truetransact.uicomponent.CLabel lblProdIdCr;
    private com.see.truetransact.uicomponent.CLabel lblProdIdCr1;
    private com.see.truetransact.uicomponent.CLabel lblProdTypeCr;
    private com.see.truetransact.uicomponent.CLabel lblProdTypeCr1;
    private com.see.truetransact.uicomponent.CLabel lblRevryMnth;
    private com.see.truetransact.uicomponent.CLabel lblSelectedCount;
    private com.see.truetransact.uicomponent.CLabel lblSelectedCountVal;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotLimit;
    private com.see.truetransact.uicomponent.CLabel lblTransTot;
    private com.see.truetransact.uicomponent.CLabel lblTransTotalValue;
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
    private com.see.truetransact.uicomponent.CPanel panAccHd1;
    private com.see.truetransact.uicomponent.CPanel panAccHd2;
    private com.see.truetransact.uicomponent.CPanel panAccountDetails;
    private com.see.truetransact.uicomponent.CPanel panAccountDetails1;
    private com.see.truetransact.uicomponent.CPanel panAccountNo;
    private com.see.truetransact.uicomponent.CPanel panAccountNo1;
    private com.see.truetransact.uicomponent.CPanel panAccountNo2;
    private com.see.truetransact.uicomponent.CPanel panAcctNo;
    private com.see.truetransact.uicomponent.CPanel panAcctNo1;
    private com.see.truetransact.uicomponent.CPanel panBulkPaymaster;
    private com.see.truetransact.uicomponent.CPanel panDebitAccHead;
    private com.see.truetransact.uicomponent.CPanel panDrCr;
    private com.see.truetransact.uicomponent.CPanel panEmpDetails;
    private com.see.truetransact.uicomponent.CPanel panEmployeeTabTools;
    private com.see.truetransact.uicomponent.CPanel panEmployyeDetails;
    private com.see.truetransact.uicomponent.CPanel panPFDetails;
    private com.see.truetransact.uicomponent.CPanel panPayType;
    private com.see.truetransact.uicomponent.CPanel panPaymasterList;
    private com.see.truetransact.uicomponent.CPanel panPaymentTransaction;
    private com.see.truetransact.uicomponent.CPanel panRadioGrp;
    private com.see.truetransact.uicomponent.CPanel panRadioGrp1;
    private com.see.truetransact.uicomponent.CPanel panSHGBtn;
    private com.see.truetransact.uicomponent.CPanel panSHGDetails;
    private com.see.truetransact.uicomponent.CPanel panSHGDetails1;
    private com.see.truetransact.uicomponent.CPanel panSHGRecordDetails;
    private com.see.truetransact.uicomponent.CPanel panSHGRecordDetails1;
    private com.see.truetransact.uicomponent.CPanel panSHGTableDetails;
    private com.see.truetransact.uicomponent.CPanel panSalaryRecoveryOptions;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransAccountNo;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CPanel panTransactionTrans;
    private com.see.truetransact.uicomponent.CPanel panTransactionType;
    private com.see.truetransact.uicomponent.CButtonGroup radioButtonGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdgEFTProductGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIsLapsedGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPayableBranchGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrintServicesGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSeriesGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAcctButtonGroup;
    private com.see.truetransact.uicomponent.CRadioButton rdoBulkContra;
    private com.see.truetransact.uicomponent.CRadioButton rdoBulkDeduct;
    private com.see.truetransact.uicomponent.CRadioButton rdoBulkEarnings;
    private com.see.truetransact.uicomponent.CButtonGroup rdoBulkPayCodeGrp;
    private com.see.truetransact.uicomponent.CButtonGroup rdoButtonGroup;
    private com.see.truetransact.uicomponent.CRadioButton rdoCr;
    private com.see.truetransact.uicomponent.CRadioButton rdoDeduct;
    private com.see.truetransact.uicomponent.CRadioButton rdoDeductTrans;
    private com.see.truetransact.uicomponent.CRadioButton rdoDr;
    private com.see.truetransact.uicomponent.CRadioButton rdoEarnings;
    private com.see.truetransact.uicomponent.CRadioButton rdoEarningsTrans;
    private com.see.truetransact.uicomponent.CRadioButton rdoGeneralLedger;
    private com.see.truetransact.uicomponent.CRadioButton rdoGeneralLedgertrans;
    private com.see.truetransact.uicomponent.CRadioButton rdoOtherAccounts;
    private com.see.truetransact.uicomponent.CRadioButton rdoOtherAccountsTrans;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPayCodeGrp;
    private com.see.truetransact.uicomponent.CRadioButton rdoPayment;
    private com.see.truetransact.uicomponent.CRadioButton rdoReceipt;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransAccountType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransPaycodeGrp;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransTypeGrp;
    private com.see.truetransact.uicomponent.CRadioButton rdocontra;
    private com.see.truetransact.uicomponent.CRadioButton rdocontratrans;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CScrollPane srpEmployeeTabCTable;
    private com.see.truetransact.uicomponent.CScrollPane srpPaymasterList;
    private com.see.truetransact.uicomponent.CScrollPane srpSHGTable;
    private com.see.truetransact.uicomponent.CTabbedPane tabSHGDetails;
    private com.see.truetransact.uicomponent.CTable tblEmployeeDetails;
    private com.see.truetransact.uicomponent.CTable tblPayMasterList;
    private com.see.truetransact.uicomponent.CTable tblSHGDetails;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CDateField tdtFromDt;
    public static com.see.truetransact.uicomponent.CTextField txtAcctNo;
    public static com.see.truetransact.uicomponent.CTextField txtAcctNotrans;
    public static com.see.truetransact.uicomponent.CTextField txtAmount;
    public static com.see.truetransact.uicomponent.CTextField txtAmountTrans;
    private com.see.truetransact.uicomponent.CTextField txtEmployeeID;
    public static com.see.truetransact.uicomponent.CTextField txtProdIdTrans;
    private com.see.truetransact.uicomponent.CTextField txtRecoveryMonth;
    private com.see.truetransact.uicomponent.CTextField txtTransEmployeeID;
    private com.see.truetransact.uicomponent.CTextField txtTransProductId;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] arg) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        PayRollIndividualUI gui = new PayRollIndividualUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}