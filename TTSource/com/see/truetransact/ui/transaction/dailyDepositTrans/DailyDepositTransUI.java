/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DailyDepositTransUI.java
 *
 * Created on February 25, 2004, 11:55 AM
 */
package com.see.truetransact.ui.transaction.dailyDepositTrans;

import com.see.truetransact.commonutil.Dummy;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizewf.AuthorizeWFUI;
import com.see.truetransact.ui.common.authorizewf.AuthorizeWFCheckUI;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.denomination.DenominationUI;
import com.see.truetransact.transferobject.agent.AgentTO;

import com.see.truetransact.commonutil.LocaleConstants;

import com.see.truetransact.uicomponent.CButtonGroup;// To add and Remove the Radio Buttons...
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.common.viewphotosign.ViewPhotoSignUI;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.OracleResultSet;

import com.see.truetransact.ui.TrueTransactMain;
import java.util.Date;
import java.io.File;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;

import java.util.Iterator;
import javax.swing.JDialog;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import org.jboss.mq.il.ha.HAServerIL;

/**
 *
 * @author  rahul, bala
 * @todoh Add other modules into the transaction
 *
 */
public class DailyDepositTransUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    private int ok = 0;
    private int yes = 0;
    private int no = 1;
    private int cancel = 2;
    private Date collDT;
    private boolean _intTransferNew;
    private boolean tblClicked = false;
    private int rowForEdit;
    private String transactionIdForEdit;
    private double Amt = 0.0;
    Date curDate = null;
    private HashMap authorizeCheckMap = new HashMap();
    private HashMap mandatoryMap;
    DailyDepositTransOB observable;
    final int EDIT = 0, DELETE = 1, ACCNO = 2, AUTHORIZE = 3, ACCTHDID = 4, EXPORT_AGENT_ID = 5,AGENT_PROD_ID=6;
    int viewType = -1;
    boolean isFilled = false;
    private TransDetailsUI transDetails = null;
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    private final static Logger log = Logger.getLogger(DailyDepositTransUI.class);     //Logger
    private java.sql.Connection con = null;
    private ArrayList totCustomerList = null;
    private java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("dd-MM-yyyy");
    private HashMap agentMap = null;
    private String path = "";
    int count = 0;
    private String driverName;
    private Connection conn;
    private Statement stmt;
    private ResultSet rset;
    private String cmd;
    private String dataBaseURL;
    private String userName;
    private String passWord;
    private String SERVER_ADDRESS;
    private String tableName;
    private String agent_code = "";
    private String flagSave="NORMAL";

    /** Creates new form CashTransaction */
    public DailyDepositTransUI() {
        try {
            curDate = ClientUtil.getCurrentDate();
            initComponents();
            initSetup();
            btnDailyDepSubNew.setEnabled(false);
            btnDailyDepSubSave.setEnabled(false);
            btnDailyDepSubDel.setEnabled(false);
            //        btnDenomination.setEnabled(false);
            btnAccNo.setEnabled(false);// Reset the Editable Lables in the UI to null...
            chkLoadFromFile.setVisible(false);
            panLoadFromFile.setVisible(false);
            observable.setFlagSave("NORMAL");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSetup() throws Exception {
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        initComponentData();                    // Fill all the combo boxes...
        setMaxLenths();                         // To set the Numeric Validation and the Maximum length of the Text fields...
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panCashTransaction);
        ClientUtil.enableDisable(this, false);  // Disables all when the screen appears for the 1st time
        //        setButtonEnableDisable();               // Enables/Disables the necessary buttons and menu items...
        enableDisableButtons(false);                 // To disable the buttons(folder) in the Starting...
        observable.setTdtInstrumentDate(DateUtil.getDateMMDDYYYY(""));
        observable.setLblTransactionId("");
        //       observable.setTotalGlAmt(new Double(null).doubleValue());
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
        observable.resetForm();                 // To reset all the fields in UI...
        observable.resetLable();                // To reset all the Lables in UI...
        observable.resetStatus();               // To reset the Satus in the UI...
        chkExportToFile.setVisible(true);
        chkExportToFile.setEnabled(true);
        panExportToFile.setVisible(false);
        btnAgentProdId.setEnabled(false);
        txtAgentProdId.setEnabled(false);
        objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.transaction.dailyDepositTrans.DailyDepositTransMRB", ProxyParameters.LANGUAGE);
        this.resetUIData();
        setHelpMessage();
    }

    // Creates The Instance of InwardClearingOB
    private void setObservable() {
        try {
            observable = new DailyDepositTransOB();
            observable.addObserver(this);
        } catch (Exception e) {
            System.out.println("Exception setObservable :" + e);
        }
    }
    //Authorize Button to be added...

    private void setFieldNames() {
        lblAgentType.setName("lblAgentType");
        lblAccNo.setName("lblAccNo");
        lblAccHd.setName("lblAccHd");
        lblProd_type.setName("lblProd_type");
        //        lblBalance.setName("lblBalance");
        lblBalanceAmount.setName("lblBalanceAmount");
        //        lblInitiatorChannel.setName("lblInitiatorChannel");
        lblProdId.setName("lblProdId");
        lblProd_type.setName("lblProd_type");
//        lblProductId.setName("productId");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace6.setName("lblSpace6");
        lblStatus.setName("lblStatus");
        //        lblTransactionType.setName("lblTransactionType");
        cboAgentType.setName("cboAgentType");
        //        btnAccHdId.setName("btnAccHdId");
        btnAccNo.setName("btnAccNo");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        //        btnDenomination.setName("btnDenomination");
        btnDailyDepSubDel.setName("btnDailyDepSubDel");
        btnDailyDepSubNew.setName("btnDailyDepSubNew");
        btnDailyDepSubSave.setName("btnDailyDepSubSave");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        //        txtAccHdId.setName("txtAccHdId");
        lblAccHdId.setName("AccHeadId");
        txtAccNo.setName("txtAccNo");
        txtAmount.setName("txtAmount");
        //        txtInitiatorChannel.setName("txtInitiatorChannel");
        //        rdoTransactionType_Credit.setName("rdoTransactionType_Credit");//        btnCancel.setName("btnCancel");
        mbrMain.setName("mbrMain");
        //        panAccHd.setName("panAccHd");
        lblAgNm.setText("lblAgNm");
        lblAgNmVal.setText("");
        lblExpAgNmVal.setText("");
        lblCustnmVal.setText("");
        lblInstrumentDate.setText("lblInstrumentDate");
        lblAgNm.setText("lblAgNm");
        lblBalanceVal.setText("");
        lblTotalAmt.setText("lblTotalAmt");
        lblTotAmtVal.setText("");
        chkExportToFile.setName("chkExportToFile");
        txtExportFileName.setName("txtExportFileName");
        lblExportFileName.setName("lblExportFileName");
        btnExportFileBrowse.setName("btnExportFileBrowse");
        btnExportSave.setName("btnExportSave");
        panExportToFile.setName("panExportToFile");

    }

    private void internationalize() {
        DailyDepositTransRB resourceBundle = new DailyDepositTransRB();
        //        resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.transaction.dailyDepositTrans.DailyDepositTransRB", ProxyParameters.LANGUAGE);
        btnClose.setText(resourceBundle.getString("btnClose"));
        //        ((javax.swing.border.TitledBorder)panTransaction.getBorder()).setTitle(resourceBundle.getString("panTransaction"));
        //        lblInitiatorChannel.setText(resourceBundle.getString("lblInitiatorChannel"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblAccHdId.setText(resourceBundle.getString("lblAccHdId"));
        lblAccHd.setText(resourceBundle.getString("lblAccHd"));
        lblAccNo.setText(resourceBundle.getString("lblAccNo"));
        //        lblTransactionType.setText(resourceBundle.getString("lblTransactionType"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblProd_type.setText(resourceBundle.getString("lblProd_type"));
        lblProdId.setText(resourceBundle.getString("lblProdId"));
        //        lblProductId.setText(resourceBundle.getString("lblProductId"));
        //        lblAccHdId.setText(resourceBundle.getString("lblAccHdId"));
        lblAgentType.setText(resourceBundle.getString("lblAgentType"));
        //        lblBalance.setText(resourceBundle.getString("lblBalance"));
        lblBalanceAmount.setText(resourceBundle.getString("lblBalanceAmount"));

        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnAccNo.setText(resourceBundle.getString("btnAccNo"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        //        rdoTransactionType_Credit.setText(resourceBundle.getString("rdoTransactionType_Credit"));
        //        ((javax.swing.border.TitledBorder)panData.getBorder()).setTitle(resourceBundle.getString("panData"));
        //        ((javax.swing.border.TitledBorder)panLableValues.getBorder()).setTitle(resourceBundle.getString("panLableValues"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblAgNm.setText(resourceBundle.getString("lblAgNm"));

        lblInstrumentDate.setText(resourceBundle.getString("lblInstrumentDate"));
        lblAgNm.setText(resourceBundle.getString("lblAgNm"));
        lblTotalAmt.setText(resourceBundle.getString("lblTotalAmt"));

    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAccNo", new Boolean(true));
        mandatoryMap.put("txtInitiatorChannel", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("txtAccHdId", new Boolean(true));
        mandatoryMap.put("rdoTransactionType_Credit", new Boolean(true));
        mandatoryMap.put("cboAgentType", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    // To Reset the Radio Buttons in the UI after any operation, We've to
    //1. Remove the Radio Buttons fron the Radio Groups...
    //2. Add the Radio Buttons Back in The Radio Groups...
    //a.) To Remove the Radio buttons...
    private void removeRadioButtons() {
        //        rdoTransactionType.remove(rdoTransactionType_Credit);
    }

    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        rdoTransactionType = new CButtonGroup();
        //        rdoTransactionType.add(rdoTransactionType_Credit);
    }

    public void update(Observable observed, Object arg) {
        //        removeRadioButtons();
        txtAccNo.setText(observable.getTxtAccNo());
        //        txtInitiatorChannel.setText(observable.getTxtInitiatorChannel());
        //        rdoTransactionType_Credit.setSelected(observable.getRdoTransactionType_Credit());
        txtAmount.setText(observable.getTxtAmount());
        cboAgentType.setSelectedItem(observable.getCboAgentType());
        //        lblBalance.setText(observable.getBalance());
        lblTransactionIDDesc1.setText(observable.getLblTransactionId());
        lblTransactionDateDesc1.setText(observable.getLblTransDate());
        lblInitiatorIDDesc1.setText(observable.getLblInitiatorId());
        lblCustnmVal.setText(observable.getLblAccName());
        lblBalanceVal.setText(observable.getBalance());
        System.out.println("A ---->mt "+Amt);
        lblTotAmtVal.setText(CommonUtil.convertObjToStr(new Double(Amt)));
        tdtInstrumentDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtInstrumentDate()));
        txtProdType.setText(observable.getProdType());
        this.updateTable();

        //        txtAccNoActionPerformed(null);
    }

    public void updateOBFields() {
        chkVerification.setEnabled(true);
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.setTxtAmount(txtAmount.getText());
        observable.setLblAccName(lblCustnmVal.getText());
        observable.setBalance(lblBalanceVal.getText());
        observable.setTdtInstrumentDate(DateUtil.getDateMMDDYYYY(tdtInstrumentDate.getDateValue()));
        observable.setLblTransactionId(lblTransactionIDDesc1.getText());
        observable.setCboAgentType(CommonUtil.convertObjToStr(cboAgentType.getSelectedItem()));
        observable.setTotalGlAmt(Amt);
        if (CommonConstants.VENDOR != null && !CommonConstants.VENDOR.equals("POLPULLY")) {
            observable.setProdType(txtProdType.getText());
        }
        observable.setProdId(txtAgentProdId.getText());
        // txtProdType.setText(observable.getProdType());
    }

    public void setHelpMessage() {
        txtAccNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccNo"));
        //        txtInitiatorChannel.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInitiatorChannel"));
        //        rdoTransactionType_Credit.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoTransactionType_Credit"));
        txtAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmount"));
    }

    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {

        //cboAgentType.setModel(observable.getCbmAgentType());
        //     cboAgentType.setModel(observable.getCboAgentType());
        ///     tblDailyDepositList.setModel(observable.getTbmTransfer());
//        lblProductId.setText(observable.getProdId());
     //   lblAccHdId.setText(observable.getProd_desc());
    }

    //To set the length of the Text Fields and Numeric Validation...
    private void setMaxLenths() {
        txtAmount.setMaxLength(16);
        txtAmount.setValidation(new CurrencyValidation(14, 2));
        txtAccNo.setAllowAll(true);
    }

    private void setEditFieldsEnable(boolean yesno) {
        //        rdoTransactionType_Credit.setEnabled(yesno);
        btnAccNo.setEnabled(yesno);
    }

    // To set The Value of the Buttons Depending on the Value or Condition...
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        btnDelete.setEnabled(false);
        btnDelete.setEnabled(!btnNew.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        //        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
    }

    private void updateTable() {
        this.tblDailyDepositList.setModel(observable.getTbmTransfer());
        this.tblDailyDepositList.revalidate();
    }

    private void resetUIData() {
        this._intTransferNew = false;
        this.viewType = -1;
        this.rowForEdit = -1;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoTransactionType = new com.see.truetransact.uicomponent.CButtonGroup();
        panCashTransaction = new com.see.truetransact.uicomponent.CPanel();
        panData = new com.see.truetransact.uicomponent.CPanel();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        lblAccHd = new com.see.truetransact.uicomponent.CLabel();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        lblCustNameVal = new com.see.truetransact.uicomponent.CLabel();
        lblProd_type = new com.see.truetransact.uicomponent.CLabel();
        panAcctNo = new com.see.truetransact.uicomponent.CPanel();
        txtAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        lblAgentType = new com.see.truetransact.uicomponent.CLabel();
        cboAgentType = new com.see.truetransact.uicomponent.CComboBox();
        panAmt = new com.see.truetransact.uicomponent.CPanel();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        txtAmount1 = new com.see.truetransact.uicomponent.CTextField();
        lblAccHdId = new com.see.truetransact.uicomponent.CLabel();
        lblBalanceAmount = new com.see.truetransact.uicomponent.CLabel();
        tdtInstrumentDate = new com.see.truetransact.uicomponent.CDateField();
        lblInstrumentDate = new com.see.truetransact.uicomponent.CLabel();
        lblBalanceVal = new com.see.truetransact.uicomponent.CLabel();
        lblCustnmVal = new com.see.truetransact.uicomponent.CLabel();
        lblAgNm = new com.see.truetransact.uicomponent.CLabel();
        lblAgNmVal = new com.see.truetransact.uicomponent.CLabel();
        chkLoadFromFile = new com.see.truetransact.uicomponent.CCheckBox();
        panLoadFromFile = new com.see.truetransact.uicomponent.CPanel();
        txtFileName = new com.see.truetransact.uicomponent.CTextField();
        lblFileName = new com.see.truetransact.uicomponent.CLabel();
        btnBrowse = new com.see.truetransact.uicomponent.CButton();
        downloadButton = new com.see.truetransact.uicomponent.CButton();
        panExportToFile = new com.see.truetransact.uicomponent.CPanel();
        txtExportFileName = new com.see.truetransact.uicomponent.CTextField();
        lblExportFileName = new com.see.truetransact.uicomponent.CLabel();
        btnExportSave = new com.see.truetransact.uicomponent.CButton();
        btnExportFileBrowse = new com.see.truetransact.uicomponent.CButton();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        txtExportAgentId = new com.see.truetransact.uicomponent.CTextField();
        btnExportAgent = new com.see.truetransact.uicomponent.CButton();
        lblExportagentId = new com.see.truetransact.uicomponent.CLabel();
        lblExpAgNmVal = new com.see.truetransact.uicomponent.CLabel();
        uploadButton = new com.see.truetransact.uicomponent.CButton();
        chkExportToFile = new com.see.truetransact.uicomponent.CCheckBox();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        txtProdType = new com.see.truetransact.uicomponent.CTextField();
        txtAgentProdId = new com.see.truetransact.uicomponent.CTextField();
        btnAgentProdId = new com.see.truetransact.uicomponent.CButton();
        panLables = new com.see.truetransact.uicomponent.CPanel();
        panDailyDeposit = new com.see.truetransact.uicomponent.CPanel();
        srpDailyDepositDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblDailyDepositList = new com.see.truetransact.uicomponent.CTable();
        btnSavePan = new com.see.truetransact.uicomponent.CPanel();
        btnDailyDepSubNew = new com.see.truetransact.uicomponent.CButton();
        btnDailyDepSubSave = new com.see.truetransact.uicomponent.CButton();
        btnDailyDepSubDel = new com.see.truetransact.uicomponent.CButton();
        panTransaction1 = new com.see.truetransact.uicomponent.CPanel();
        lblTransactionID1 = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionIDDesc1 = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionDate1 = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionDateDesc1 = new com.see.truetransact.uicomponent.CLabel();
        lblInitiatorID1 = new com.see.truetransact.uicomponent.CLabel();
        lblInitiatorIDDesc1 = new com.see.truetransact.uicomponent.CLabel();
        lblTotalAmt = new com.see.truetransact.uicomponent.CLabel();
        lblTotAmtVal = new com.see.truetransact.uicomponent.CLabel();
        com.see.truetransact.uicomponent.CPanel vericficationpanel = new com.see.truetransact.uicomponent.CPanel();
        chkVerification = new com.see.truetransact.uicomponent.CCheckBox();
        lblTotInst = new javax.swing.JLabel();
        lblTotInstVal = new javax.swing.JLabel();
        tbrHead = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnCashPayRec = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace32 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
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
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setPreferredSize(new java.awt.Dimension(700, 625));

        panCashTransaction.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panCashTransaction.setMinimumSize(new java.awt.Dimension(625, 475));
        panCashTransaction.setPreferredSize(new java.awt.Dimension(625, 550));
        panCashTransaction.setLayout(new java.awt.GridBagLayout());

        panData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panData.setMinimumSize(new java.awt.Dimension(333, 450));
        panData.setPreferredSize(new java.awt.Dimension(333, 550));
        panData.setLayout(new java.awt.GridBagLayout());

        lblProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panData.add(lblProdId, gridBagConstraints);

        lblAccHd.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panData.add(lblAccHd, gridBagConstraints);

        lblAccNo.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panData.add(lblAccNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblCustName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblCustNameVal, gridBagConstraints);

        lblProd_type.setText("Prod Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblProd_type, gridBagConstraints);

        panAcctNo.setMinimumSize(new java.awt.Dimension(121, 21));
        panAcctNo.setPreferredSize(new java.awt.Dimension(21, 200));
        panAcctNo.setLayout(new java.awt.GridBagLayout());

        txtAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccNoActionPerformed(evt);
            }
        });
        txtAccNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccNoFocusLost(evt);
            }
        });
        panAcctNo.add(txtAccNo, new java.awt.GridBagConstraints());

        btnAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccNo.setToolTipText("Account No.");
        btnAccNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAccNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNoActionPerformed(evt);
            }
        });
        panAcctNo.add(btnAccNo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panData.add(panAcctNo, gridBagConstraints);

        lblAgentType.setText("Agent Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panData.add(lblAgentType, gridBagConstraints);

        cboAgentType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAgentType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboAgentTypeItemStateChanged(evt);
            }
        });
        cboAgentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAgentTypeActionPerformed(evt);
            }
        });
        cboAgentType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboAgentTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panData.add(cboAgentType, gridBagConstraints);

        panAmt.setMaximumSize(new java.awt.Dimension(123, 29));
        panAmt.setMinimumSize(new java.awt.Dimension(123, 29));
        panAmt.setPreferredSize(new java.awt.Dimension(21, 200));
        panAmt.setLayout(new java.awt.GridBagLayout());

        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAmt.add(txtAmount, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panAmt.add(lblSpace, gridBagConstraints);

        txtAmount1.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmount1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmount1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAmt.add(txtAmount1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 3, 17);
        panData.add(panAmt, gridBagConstraints);

        lblAccHdId.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 38;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panData.add(lblAccHdId, gridBagConstraints);

        lblBalanceAmount.setText("Balance Amount");
        lblBalanceAmount.setMaximumSize(new java.awt.Dimension(1000, 21));
        lblBalanceAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        lblBalanceAmount.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblBalanceAmount, gridBagConstraints);

        tdtInstrumentDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtInstrumentDate.setPreferredSize(new java.awt.Dimension(21, 200));
        tdtInstrumentDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtInstrumentDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panData.add(tdtInstrumentDate, gridBagConstraints);

        lblInstrumentDate.setText("Collection Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panData.add(lblInstrumentDate, gridBagConstraints);

        lblBalanceVal.setText("Balance Amount");
        lblBalanceVal.setMaximumSize(new java.awt.Dimension(1000, 21));
        lblBalanceVal.setMinimumSize(new java.awt.Dimension(100, 21));
        lblBalanceVal.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblBalanceVal, gridBagConstraints);

        lblCustnmVal.setForeground(new java.awt.Color(0, 51, 204));
        lblCustnmVal.setText("Name");
        lblCustnmVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustnmVal.setMaximumSize(new java.awt.Dimension(1000, 21));
        lblCustnmVal.setMinimumSize(new java.awt.Dimension(200, 21));
        lblCustnmVal.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 30, 4, 4);
        panData.add(lblCustnmVal, gridBagConstraints);

        lblAgNm.setText("AgentName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panData.add(lblAgNm, gridBagConstraints);

        lblAgNmVal.setForeground(new java.awt.Color(0, 51, 204));
        lblAgNmVal.setText("AgentNamVal");
        lblAgNmVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panData.add(lblAgNmVal, gridBagConstraints);

        chkLoadFromFile.setText("Load from file");
        chkLoadFromFile.setMaximumSize(new java.awt.Dimension(103, 20));
        chkLoadFromFile.setMinimumSize(new java.awt.Dimension(103, 20));
        chkLoadFromFile.setPreferredSize(new java.awt.Dimension(103, 20));
        chkLoadFromFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLoadFromFileActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panData.add(chkLoadFromFile, gridBagConstraints);

        panLoadFromFile.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLoadFromFile.setMinimumSize(new java.awt.Dimension(200, 70));
        panLoadFromFile.setPreferredSize(new java.awt.Dimension(200, 90));
        panLoadFromFile.setLayout(new java.awt.GridBagLayout());

        txtFileName.setMinimumSize(new java.awt.Dimension(280, 21));
        txtFileName.setPreferredSize(new java.awt.Dimension(280, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoadFromFile.add(txtFileName, gridBagConstraints);

        lblFileName.setText("File");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoadFromFile.add(lblFileName, gridBagConstraints);

        btnBrowse.setText("Browse");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoadFromFile.add(btnBrowse, gridBagConstraints);

        downloadButton.setText("Download");
        downloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panLoadFromFile.add(downloadButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 3, 3);
        panData.add(panLoadFromFile, gridBagConstraints);

        panExportToFile.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panExportToFile.setMinimumSize(new java.awt.Dimension(200, 110));
        panExportToFile.setPreferredSize(new java.awt.Dimension(200, 90));
        panExportToFile.setLayout(new java.awt.GridBagLayout());

        txtExportFileName.setMinimumSize(new java.awt.Dimension(280, 21));
        txtExportFileName.setPreferredSize(new java.awt.Dimension(280, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panExportToFile.add(txtExportFileName, gridBagConstraints);

        lblExportFileName.setText("File");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panExportToFile.add(lblExportFileName, gridBagConstraints);

        btnExportSave.setText("Save");
        btnExportSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panExportToFile.add(btnExportSave, gridBagConstraints);

        btnExportFileBrowse.setText("Browse");
        btnExportFileBrowse.setNextFocusableComponent(cboAgentType);
        btnExportFileBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportFileBrowseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panExportToFile.add(btnExportFileBrowse, gridBagConstraints);

        cPanel1.setMinimumSize(new java.awt.Dimension(280, 21));
        cPanel1.setPreferredSize(new java.awt.Dimension(280, 21));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        txtExportAgentId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtExportAgentId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExportAgentIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel1.add(txtExportAgentId, gridBagConstraints);

        btnExportAgent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnExportAgent.setToolTipText("Account No.");
        btnExportAgent.setMaximumSize(new java.awt.Dimension(21, 21));
        btnExportAgent.setMinimumSize(new java.awt.Dimension(21, 21));
        btnExportAgent.setPreferredSize(new java.awt.Dimension(21, 21));
        btnExportAgent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportAgentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        cPanel1.add(btnExportAgent, gridBagConstraints);

        lblExportagentId.setText("Agent Id");
        lblExportagentId.setMaximumSize(new java.awt.Dimension(91, 18));
        lblExportagentId.setMinimumSize(new java.awt.Dimension(91, 18));
        lblExportagentId.setPreferredSize(new java.awt.Dimension(91, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel1.add(lblExportagentId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        panExportToFile.add(cPanel1, gridBagConstraints);

        lblExpAgNmVal.setForeground(new java.awt.Color(0, 51, 204));
        lblExpAgNmVal.setText("AgentNamVal");
        lblExpAgNmVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panExportToFile.add(lblExpAgNmVal, gridBagConstraints);

        uploadButton.setText("Upload");
        uploadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panExportToFile.add(uploadButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 3, 3);
        panData.add(panExportToFile, gridBagConstraints);

        chkExportToFile.setText("Export File");
        chkExportToFile.setMaximumSize(new java.awt.Dimension(95, 20));
        chkExportToFile.setMinimumSize(new java.awt.Dimension(95, 20));
        chkExportToFile.setPreferredSize(new java.awt.Dimension(95, 20));
        chkExportToFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkExportToFileActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panData.add(chkExportToFile, gridBagConstraints);

        lblAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblAmount, gridBagConstraints);

        txtProdType.setEditable(false);
        txtProdType.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 18);
        panData.add(txtProdType, gridBagConstraints);

        txtAgentProdId.setEditable(false);
        txtAgentProdId.setAllowAll(true);
        txtAgentProdId.setMinimumSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panData.add(txtAgentProdId, gridBagConstraints);

        btnAgentProdId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAgentProdId.setToolTipText("Account No.");
        btnAgentProdId.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAgentProdId.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAgentProdId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAgentProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgentProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        panData.add(btnAgentProdId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCashTransaction.add(panData, gridBagConstraints);

        panLables.setMinimumSize(new java.awt.Dimension(250, 420));
        panLables.setPreferredSize(new java.awt.Dimension(250, 420));
        panLables.setLayout(new java.awt.GridBagLayout());

        panDailyDeposit.setBorder(javax.swing.BorderFactory.createTitledBorder("DailyDepositDetails"));
        panDailyDeposit.setMinimumSize(new java.awt.Dimension(250, 250));
        panDailyDeposit.setName("panTransInfo"); // NOI18N
        panDailyDeposit.setPreferredSize(new java.awt.Dimension(250, 250));
        panDailyDeposit.setLayout(new java.awt.GridBagLayout());

        srpDailyDepositDetails.setMinimumSize(new java.awt.Dimension(400, 404));

        tblDailyDepositList.setModel(new javax.swing.table.DefaultTableModel(
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
        tblDailyDepositList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDailyDepositListMouseClicked(evt);
            }
        });
        srpDailyDepositDetails.setViewportView(tblDailyDepositList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panDailyDeposit.add(srpDailyDepositDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panLables.add(panDailyDeposit, gridBagConstraints);
        panDailyDeposit.getAccessibleContext().setAccessibleName("PanDailyDeposit");

        btnDailyDepSubNew.setText("New");
        btnDailyDepSubNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDailyDepSubNewActionPerformed(evt);
            }
        });
        btnSavePan.add(btnDailyDepSubNew);

        btnDailyDepSubSave.setText("Save");
        btnDailyDepSubSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDailyDepSubSaveActionPerformed(evt);
            }
        });
        btnSavePan.add(btnDailyDepSubSave);

        btnDailyDepSubDel.setText("Delete");
        btnDailyDepSubDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDailyDepSubDelActionPerformed(evt);
            }
        });
        btnSavePan.add(btnDailyDepSubDel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panLables.add(btnSavePan, gridBagConstraints);

        panTransaction1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTransaction1.setMinimumSize(new java.awt.Dimension(200, 100));
        panTransaction1.setPreferredSize(new java.awt.Dimension(200, 100));
        panTransaction1.setLayout(new java.awt.GridBagLayout());

        lblTransactionID1.setText("Transaction ID");
        lblTransactionID1.setMinimumSize(new java.awt.Dimension(75, 21));
        lblTransactionID1.setPreferredSize(new java.awt.Dimension(90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTransaction1.add(lblTransactionID1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTransaction1.add(lblTransactionIDDesc1, gridBagConstraints);

        lblTransactionDate1.setText("Transaction Date");
        lblTransactionDate1.setMinimumSize(new java.awt.Dimension(100, 21));
        lblTransactionDate1.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTransaction1.add(lblTransactionDate1, gridBagConstraints);

        lblTransactionDateDesc1.setMaximumSize(new java.awt.Dimension(100, 21));
        lblTransactionDateDesc1.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTransaction1.add(lblTransactionDateDesc1, gridBagConstraints);

        lblInitiatorID1.setText("Initiator ID");
        lblInitiatorID1.setMinimumSize(new java.awt.Dimension(53, 21));
        lblInitiatorID1.setPreferredSize(new java.awt.Dimension(60, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTransaction1.add(lblInitiatorID1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTransaction1.add(lblInitiatorIDDesc1, gridBagConstraints);

        lblTotalAmt.setText("total Amt");
        lblTotalAmt.setMinimumSize(new java.awt.Dimension(48, 21));
        lblTotalAmt.setPreferredSize(new java.awt.Dimension(60, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTransaction1.add(lblTotalAmt, gridBagConstraints);

        lblTotAmtVal.setMinimumSize(new java.awt.Dimension(60, 21));
        lblTotAmtVal.setPreferredSize(new java.awt.Dimension(60, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTransaction1.add(lblTotAmtVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panLables.add(panTransaction1, gridBagConstraints);

        vericficationpanel.setMinimumSize(new java.awt.Dimension(200, 25));
        vericficationpanel.setPreferredSize(new java.awt.Dimension(200, 25));

        chkVerification.setText("VerifyAll");
        chkVerification.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkVerificationActionPerformed(evt);
            }
        });
        vericficationpanel.add(chkVerification);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 5, 0);
        panLables.add(vericficationpanel, gridBagConstraints);

        lblTotInst.setText("Total Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panLables.add(lblTotInst, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        panLables.add(lblTotInstVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCashTransaction.add(panLables, gridBagConstraints);

        getContentPane().add(panCashTransaction, java.awt.BorderLayout.CENTER);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrHead.add(btnNew);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace27);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrHead.add(btnEdit);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace28);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrHead.add(btnDelete);

        lblSpace2.setText("     ");
        tbrHead.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrHead.add(btnSave);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace29);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrHead.add(btnCancel);

        lblSpace3.setText("     ");
        tbrHead.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrHead.add(btnAuthorize);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace30);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrHead.add(btnException);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace31);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrHead.add(btnReject);

        lblSpace4.setText("     ");
        tbrHead.add(lblSpace4);

        btnCashPayRec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/denomination.gif"))); // NOI18N
        btnCashPayRec.setToolTipText("Exception");
        tbrHead.add(btnCashPayRec);

        lblSpace6.setText("     ");
        tbrHead.add(lblSpace6);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrHead.add(btnPrint);

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace32);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrHead.add(btnClose);

        getContentPane().add(tbrHead, java.awt.BorderLayout.NORTH);

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

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExportSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportSaveActionPerformed
        // TODO add your handling code here:
//        System.out.println("Export path####"+path);
        if (CommonUtil.convertObjToStr(txtExportAgentId.getText()).equals("")) {
            displayAlert("Please select the AgentId!!!");
            return;
        }
        if (!CommonUtil.convertObjToStr(path).equals("")) {
            writeExportFile();
        } else {
            displayAlert("Please select the file!!!");
            return;
        }
    }//GEN-LAST:event_btnExportSaveActionPerformed

    private void btnExportAgentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportAgentActionPerformed
        // TODO add your handling code here:
        popUp(EXPORT_AGENT_ID);

    }//GEN-LAST:event_btnExportAgentActionPerformed

    private void txtExportAgentIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExportAgentIdFocusLost
        // TODO add your handling code here:
//       "getAgentDetails";
    }//GEN-LAST:event_txtExportAgentIdFocusLost

    private void btnExportFileBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportFileBrowseActionPerformed
        // TODO add your handling code here:
        try {
            javax.swing.JFileChooser fc = new javax.swing.JFileChooser();;
            int returnVal = fc.showOpenDialog(null);
//            System.out.println("@#@# dialog opened");
            if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                java.io.File file = fc.getSelectedFile();
                System.out.println("file####" + file);
//                 java.io.File files = new File(fc.getSelectedFile());
                path = fc.getSelectedFile().getAbsolutePath();
                txtExportFileName.setText(path);
//                System.out.println("path####"+path);
                String fname = file.getName();
                fname = fname.toUpperCase();
                if (fname != null || !fname.equalsIgnoreCase("")) {
//                    if(fname.substring(fname.indexOf('.')+1, fname.length()).equals("TXT")) {
//                        txtExportFileName.setText(fc.getSelectedFile().toString());
//                    } else {
//                        displayAlert("File should be .txt format");
//                        return;
//                    }
                } else {
                    displayAlert("Please select a path and type file name...");
                }
            }
        } catch (Exception e) {
            displayAlert("Unable to Export...");
        }
    }//GEN-LAST:event_btnExportFileBrowseActionPerformed
    private void createExportFile(String path) {
        System.out.println("Create new file");


//        File file=new File(path);


//         FileOutputStream fis = new FileOutputStream(file);
//         BufferedInputStream bis = new BufferedInputStream(fis);
//         DataInputStream dis = new DataInputStream(bis);

        //            dis.w
    }
 
    private void writeExportFile() {
        if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("POLPULLY")) {
            HashMap dataMap = new HashMap();
            StringBuffer buffer = new StringBuffer();
            dataMap.put("AGENT_ID", CommonUtil.convertObjToStr(txtExportAgentId.getText()));
            dataMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            List lst = ClientUtil.executeQuery("getDailyDepositCusotmerForPulllpully", dataMap);
            System.out.println("SIZ----" + lst.size());
            if (lst != null && lst.size() > 0) {
                try {
                    java.io.FileWriter write = new java.io.FileWriter(path, false);
                    java.io.PrintWriter print_line = new java.io.PrintWriter(write);
                    print_line.print(lblExpAgNmVal.getText());
                    print_line.print(",");
                    print_line.println("123456");
                    System.out.println("AGENT IF---" + lblExpAgNmVal.getText());
//            buffer.append(lblExpAgNmVal.getText());
//            buffer.append(",");
//            buffer.append(txtExportAgentId.getText());
                    for (int i = 0; i < lst.size(); i++) {
                        HashMap resultMap = (HashMap) lst.get(i);
                        print_line.print(resultMap.get("PROD_ID"));
                        print_line.print(",");
                        print_line.print(resultMap.get("STREET"));
                        print_line.print(",");
                        print_line.print(resultMap.get("DEPOSIT_NO"));
                        print_line.print(",");
                        print_line.print(resultMap.get("FNAME"));
                        print_line.print(",");
                        print_line.print(resultMap.get("LAST_COLL_DT"));
                        print_line.print(",");
                        print_line.print(resultMap.get("CLEAR_BALANCE"));
                        print_line.print(",");
                        print_line.print(resultMap.get("MULT_AMT"));
                        print_line.print(",");
                        print_line.print(resultMap.get("FINE_AMT"));
                        print_line.print(",");
                        print_line.print(resultMap.get("LIMIT_AMT"));
                        print_line.print(",");
                        print_line.print(resultMap.get("FLAG"));
                        //       print_line.print(",");
                        print_line.println(CommonUtil.convertObjToStr(resultMap.get("CREDITOR_NOTICE")));
//                buffer.append(CommonUtil.convertObjToStr(resultMap.get("DEPOSIT_NO")));
//                buffer.append(",");
//                buffer.append(CommonUtil.convertObjToStr(resultMap.get("NAME")));
//                 buffer.append(",");
                    }
                    if (print_line != null) {
                        print_line.close();
                    }
                    if (write != null) {
                        write.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                displayAlert("Export completed...\nFile path is :" + path);
            }
            btnCancelActionPerformed(null);
        } else if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("CHELLANOOR")) {

            HashMap dataMap = new HashMap();
            StringBuffer buffer = new StringBuffer();
            dataMap.put("AGENT_ID", CommonUtil.convertObjToStr(txtExportAgentId.getText()));
            dataMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            List lst = ClientUtil.executeQuery("getDailyDepositCusotmerNew", dataMap);
            HashMap bank = new HashMap();
            bank = (HashMap) ClientUtil.executeQuery("getSelectBankTOList", null).get(0);
            HashMap agent = new HashMap();
            agent.put("Agent_Id", txtExportAgentId.getText());
            List agentid = ClientUtil.executeQuery("getAgentRefwrenceNo", agent);
            HashMap agentresMap = new HashMap();
            if (agentid != null && agentid.size() > 0) {
                agentresMap = (HashMap) agentid.get(0);
            }
            if ((lst != null && lst.size() > 0)) {
                try {
                    java.io.FileWriter write = new java.io.FileWriter(path, false);
                    java.io.PrintWriter print_line = new java.io.PrintWriter(write);
                    String bankname = "CSCB";
                    print_line.printf("%-96s", bankname);
                    print_line.printf("%-8s", agentresMap.get("REF_NO"));
                    print_line.printf("%-18s", lblExpAgNmVal.getText());
                    System.out.println("AGENT IF---" + lblExpAgNmVal.getText());
                    for (int i = 0; i < lst.size(); i++) {
                        HashMap resultMap = (HashMap) lst.get(i);
                        print_line.printf("%-8s", resultMap.get("PROD_ID"));
                        String depno = CommonUtil.convertObjToStr(resultMap.get("DEPOSIT_NO"));
                        print_line.printf("%-8s", resultMap.get("REFERENCE_NO"));
                        String fname = CommonUtil.convertObjToStr(resultMap.get("FNAME"));
                        int length = fname.length();
                        if (length > 13) {
                            print_line.printf("%-18s", fname.substring(0, 13));
                        } else {
                            print_line.printf("%-18s", fname);
                        }
                        print_line.print(resultMap.get("LAST_COLL_DT"));
                        print_line.printf("%-10s", resultMap.get("CLEAR_BALANCE"));
                        print_line.print(resultMap.get("MATURITY_DT"));
                        print_line.print(resultMap.get("DEPOSIT_DT"));
                        print_line.printf("%-10s", resultMap.get("MULT_AMT"));
                    }
                    if (print_line != null) {
                        print_line.close();
                    }
                    if (write != null) {
                        write.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                displayAlert("Export completed...\nFile path is :" + path);
            }
            btnCancelActionPerformed(null);
        } //For koorkkencherry bank
        else if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("KOORKKENCHERRY")) {
            HashMap dataMap = new HashMap();
            StringBuffer buffer = new StringBuffer();
            dataMap.put("AGENT_ID", CommonUtil.convertObjToStr(txtExportAgentId.getText()));
            dataMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            List lst = ClientUtil.executeQuery("getDailyDepositCusotmerKorr", dataMap);
            System.out.println("SIZ----" + lst.size());
            if (lst != null && lst.size() > 0) {
                try {
                    HashMap bank = new HashMap();
                    bank = (HashMap) ClientUtil.executeQuery("getSelectBankTOList", null).get(0);
                    java.io.FileWriter write = new java.io.FileWriter(path, false);
                    java.io.PrintWriter print_line = new java.io.PrintWriter(write);
                    print_line.printf(lblExpAgNmVal.getText());
                    print_line.printf(";");
                    print_line.printf(agent_code);
                    print_line.printf(";");
                    print_line.printf(CommonUtil.convertObjToStr(bank.get("BANK_NAME")));
                    print_line.printf(";");
                    print_line.printf(";");
                    print_line.printf("Agent Reciept");
                    print_line.printf(";");
                    print_line.printf(lblExpAgNmVal.getText());
                    print_line.printf(";");
                    print_line.printf("Thank You");
                    print_line.printf(";");
                    print_line.printf("111111");
                    print_line.printf(";");
                    print_line.printf("0");
                    print_line.println();
                    for (int i = 0; i < lst.size(); i++) {
                        HashMap resultMap = (HashMap) lst.get(i);
                        print_line.print("1");
                        print_line.print(";");
                        print_line.print(resultMap.get("PROD_ID"));
                        print_line.print(";");
                        print_line.print(resultMap.get("PROD_DESC"));//"Daily Deposit");//ch
                        print_line.print(";");
                        print_line.print(resultMap.get("DEPOSIT_NO"));
                        print_line.print(";");
                        print_line.print(resultMap.get("FNAME"));
                        print_line.print(";");
                        print_line.print(resultMap.get("CLEAR_BALANCE"));
                        print_line.print(";");
                        String screen=CommonUtil.convertObjToStr(resultMap.get("SCREEN"));
                        double depAmt = CommonUtil.convertObjToDouble(resultMap.get("DEPOSIT_AMT"));
                        double clearAmt = CommonUtil.convertObjToDouble(resultMap.get("CLEAR_BALANCE"));
                        if(screen!=null && screen.equals("DEPOSIT")){
                             double noOfInst= clearAmt/depAmt;
                             if(noOfInst>0){
                               print_line.print(noOfInst);
                             }else{
                                  print_line.print(""); 
                             }
                        }
                        else{
                            print_line.print(depAmt);
                        }
                        print_line.print(";");
                        print_line.print(resultMap.get("MULT_AMT"));
                        print_line.print(";");
                        print_line.print("");
                        print_line.print(";");
                        Date dt1 = CommonUtil.getProperDate(curDate, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(resultMap.get("LAST_COLL_DT")))); 
                       // Date dt1=DateUtil.getDateMMDDYYYY(strDate);
                         System.out.println("5555555555555---->"+dt1);
                        int year = dt1.getYear()+1900;//strDate.substring(6, 10);
                        int month = dt1.getMonth();//substring(3, 5);
                        int day = dt1.getDate();//substring(0, 2);
                        String mon="";
                        if(String.valueOf((month+1)).length()==1){
                            mon="0"+month;
                        }
                        else{
                            mon=String.valueOf(month);
                        }
                        String DAY1="";
                        if(String.valueOf((day)).length()==1){
                            DAY1="0"+day;
                        }
                        else{
                            DAY1=String.valueOf(day);
                        }
                        print_line.print(String.valueOf(year) + mon + DAY1);
                        System.out.println("dddddddddddddd---->"+year +"mm-->"+ mon +"dd-->"+ DAY1);
                        print_line.print(";");
                        print_line.println("1");
                    }
                    if (print_line != null) {
                        print_line.close();
                    }
                    if (write != null) {
                        write.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                displayAlert("Export completed...\nFile path is :" + path);
            }
            btnCancelActionPerformed(null);
        } //end        
        else {
            //PERIGADOOR 
            HashMap dataMap = new HashMap();
            StringBuffer buffer = new StringBuffer();
            dataMap.put("AGENT_ID", CommonUtil.convertObjToStr(txtExportAgentId.getText()));
            dataMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            List lst = ClientUtil.executeQuery("getDailyDepositCusotmer", dataMap);
            HashMap dataMapL = new HashMap();
            dataMapL.put("AGENT_ID", CommonUtil.convertObjToStr(txtExportAgentId.getText()));
            dataMapL.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            List lstL = ClientUtil.executeQuery("getDailyLoanCusotmer", dataMapL);
            System.out.println("SIZ----" + lstL.size());
            if ((lst != null && lst.size() > 0) || (lstL != null && lstL.size() > 0)) {
                try {
                    java.io.FileWriter write = new java.io.FileWriter(path, false);
                    java.io.PrintWriter print_line = new java.io.PrintWriter(write);
                    print_line.print(lblExpAgNmVal.getText());
                    print_line.print(",");
                    print_line.println("123456");
                    System.out.println("AGENT IF---" + lblExpAgNmVal.getText());

                    for (int i = 0; i < lst.size(); i++) {
                        HashMap resultMap = (HashMap) lst.get(i);
                        print_line.print(resultMap.get("PROD_ID"));
                        //    print_line.print("D");
                        print_line.print(",");
                        print_line.print(resultMap.get("STREET"));
                        print_line.print(",");
                        print_line.print(resultMap.get("DEPOSIT_NO"));
                        print_line.print(",");
                        print_line.print(resultMap.get("FNAME"));
                        print_line.print(",");
                        print_line.print(resultMap.get("LAST_COLL_DT"));
                        print_line.print(",");

                        print_line.print(resultMap.get("CLEAR_BALANCE"));
                        print_line.print(",");
                        print_line.print(resultMap.get("PRIN_BALANCE"));
                        print_line.print(",");
                        print_line.print(resultMap.get("MULT_AMT"));
                        print_line.print(",");
                        print_line.print(resultMap.get("FINE_AMT"));
                        print_line.print(",");
                        print_line.print(resultMap.get("LIMIT_AMT"));
                        print_line.print(",");
                        print_line.print(resultMap.get("FLAG"));
                        print_line.println(CommonUtil.convertObjToStr(resultMap.get("CREDITOR_NOTICE")));
                    }
                    for (int i = 0; i < lstL.size(); i++) {
                        HashMap resultMap = (HashMap) lstL.get(i);
                        print_line.print(resultMap.get("PROD_ID"));
                        //    print_line.print("D");
                        print_line.print(",");
                        print_line.print(resultMap.get("STREET"));
                        print_line.print(",");
                        print_line.print(resultMap.get("DEPOSIT_NO"));
                        print_line.print(",");
                        print_line.print(resultMap.get("FNAME"));
                        print_line.print(",");
                        print_line.print(getDateValue(resultMap.get("LAST_COLL_DT").toString()));
                        print_line.print(",");
                        print_line.print(getClearBalanceLoan(resultMap.get("DEPOSIT_NO").toString(), "", "TL"));
                        print_line.print(",");
                        print_line.print(resultMap.get("PRIN_BALANCE"));
                        print_line.print(",");
                        print_line.print(resultMap.get("MULT_AMT"));
                        print_line.print(",");
                        print_line.print(resultMap.get("FINE_AMT"));
                        print_line.print(",");
                        print_line.print(resultMap.get("LIMIT_AMT"));
                        print_line.print(",");
                        print_line.print(resultMap.get("FLAG"));
                        print_line.println(CommonUtil.convertObjToStr(resultMap.get("CREDITOR_NOTICE")));
                    }
                    if (print_line != null) {
                        print_line.close();
                    }
                    if (write != null) {
                        write.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                displayAlert("Export completed...\nFile path is :" + path);
            }
            btnCancelActionPerformed(null);
        }
    }

    public Date getDDMMyyy(String date) {
        //  String str_date="11-June-07";

        java.util.Date date1 = null;
        try {
            DateFormat formatter;
            formatter = new SimpleDateFormat("dd-MM-yyyy");
            date1 = formatter.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date1;
    }

    public String getDateValue(String date1) {
        DateFormat formatter;
        Date date = null;
        String dt = null;
        // Date dt= new Date();
        try {
            System.out.println("date1 66666666666=========:" + date1);
            // String str_date=date1;
            //  formatter = new SimpleDateFormat("MM/dd/yyyy");
            //  date = (Date)formatter.parse(str_date);
            //      System.out.println("dateAFETRRR 66666666666=========:"+date);




            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
            // String s1 = "2008-03-30T15:30:00.000+02:00";
            // date1 = date1.substring(0, date1.indexOf('.'));
            System.out.println("Result==> " + sdf2.format(sdf1.parse(date1)));
            // date=new Date(sdf2.format(sdf1.parse(date1)));
            //date=sdf2.parse(date1);
            dt = sdf2.format(sdf1.parse(date1));
            System.out.println("date IOOOOOOO==> " + date);
        } catch (Exception e) {
            System.out.println("Error in getDateValue():" + e);
        }
        return dt;
    }
     public Date getDateValue1(String date1) {
        DateFormat formatter;
        Date date = null;
        String dt = null;
        // Date dt= new Date();
        try {
            System.out.println("date1 66666666666=========:" + date1);
            // String str_date=date1;
            //  formatter = new SimpleDateFormat("MM/dd/yyyy");
            //  date = (Date)formatter.parse(str_date);
            //      System.out.println("dateAFETRRR 66666666666=========:"+date);




            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
            // String s1 = "2008-03-30T15:30:00.000+02:00";
            // date1 = date1.substring(0, date1.indexOf('.'));
            System.out.println("Result==> " + sdf2.format(sdf1.parse(date1)));
            // date=new Date(sdf2.format(sdf1.parse(date1)));
            //date=sdf2.parse(date1);
            dt = sdf2.format(sdf1.parse(date1));
           date= sdf2.parse(dt);
            System.out.println("date IOOOOOOO=yyyyy=> " + date);
        } catch (Exception e) {
            System.out.println("Error in getDateValue():" + e);
        }
        return date;
    }
    /*   public String loanclosing(String acct_num,String prodId,String prodType)
    {
    String clearBalance="0";
    try
    {
    HashMap dataMap = new HashMap();
    dataMap.put("FROM_DAILY_LOAN_BALANCE","FROM_DAILY_LOAN_BALANCE");
    dataMap.put("ACT_NUM",acct_num);
    // dataMap.put("ACT_NUM",acct_num);
    dataMap.put("PROD_ID",prodId);
    dataMap.put("PROD_TYPE",prodType);
    HashMap returnMap=  observable.getAccountClosingCharges(dataMap);
    //  System.out.println("returnMap INoooo====="+returnMap);
    double val;
    if(returnMap!=null)
    {
    if(returnMap.get("BALANCE_LOAN")!=null)
    {
    val = -1*CommonUtil.convertObjToDouble(returnMap.get("BALANCE_LOAN"));
    clearBalance = CommonUtil.convertObjToStr(val);
    }
    }
    // LoanClearBalace lc= new LoanClearBalace();
    //  double dval= lc.calcLoanPayments(acct_num,prodId,prodType);
    //  clearBalance=String.valueOf(dval);
    }
    catch(Exception e)
    {
    e.printStackTrace();
    }
    return clearBalance;
    }*/

    public String getClearBalanceLoan(String acct_num, String prodId, String prodType) {
        String clearBalance = "0";
        try {
            HashMap dataMap = new HashMap();
            dataMap.put("FROM_DAILY_LOAN_BALANCE", "FROM_DAILY_LOAN_BALANCE");
            dataMap.put("ACT_NUM", acct_num);
            // dataMap.put("ACT_NUM",acct_num);
            dataMap.put("PROD_ID", prodId);
            dataMap.put("PROD_TYPE", prodType);
            HashMap returnMap = observable.getLoanBalance(dataMap);
            //  HashMap returnMap=   getAccountClosingCharges
            //  System.out.println("returnMap INoooo====="+returnMap);
            double val;
            if (returnMap != null) {
                if (returnMap.get("BALANCE_LOAN") != null) {
                    val = -1 * CommonUtil.convertObjToDouble(returnMap.get("BALANCE_LOAN"));
                    clearBalance = CommonUtil.convertObjToStr(val);
                }
            }
            // LoanClearBalace lc= new LoanClearBalace();
            //  double dval= lc.calcLoanPayments(acct_num,prodId,prodType);
            //  clearBalance=String.valueOf(dval);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clearBalance;
    }
    private void chkExportToFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkExportToFileActionPerformed
        // TODO add your handling code here:
        if (chkExportToFile.isSelected()) {
            ClientUtil.enableDisable(this, false);
            panExportToFile.setVisible(true);
            txtExportAgentId.setEnabled(true);
        } else {
            ClientUtil.enableDisable(panExportToFile, false, false, true);
            txtExportFileName.setText("");
            panExportToFile.setVisible(false);
        }
    }//GEN-LAST:event_chkExportToFileActionPerformed

    private void exportPanVisibleStatus(boolean flag) {
        panExportToFile.setVisible(flag);
    }
    private void chkLoadFromFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLoadFromFileActionPerformed
        // TODO add your handling code here:
        if (chkLoadFromFile.isSelected()) {
            ClientUtil.enableDisable(panLoadFromFile, true, false, true);
            txtFileName.setText("");
            panLoadFromFile.setVisible(true);
        } else {
            ClientUtil.enableDisable(panLoadFromFile, false, false, true);
            txtFileName.setText("");
            panLoadFromFile.setVisible(false);
        }
    }//GEN-LAST:event_chkLoadFromFileActionPerformed

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        // TODO add your handling code here:
        browseActionPerformed();
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void browseActionPerformed() {
        // TODO add your handling code here:

        try {
//            cboAgentType.removeAllItems();
//            observable.getTbmTransfer().setRowCount(0);
            javax.swing.JFileChooser fc = new javax.swing.JFileChooser();;
            int returnVal = fc.showOpenDialog(null);
            if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                java.io.File file = fc.getSelectedFile();
                String fname = file.getName();
                fname = fname.toUpperCase();
                if (fname != null || !fname.equalsIgnoreCase("")) {
//                    if(fname.substring(fname.indexOf('.')+1, fname.length()).equals("TXT")) {
//                        txtFileName.setText(fc.getSelectedFile().toString());
//    
//                        displayAlert("File should be .txt format");
//                        return;
//                    }
                }
                fillCboAgentCode(file);
            }
        } catch (Exception e) {
            displayAlert("File should be .txt format");
        }
    }

    public void fillCboAgentCode(java.io.File file) {

        if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("CHELLANOOR")) {

            try {
                String numberParameter[] = new String[100];
                agentMap = new HashMap();
                ArrayList customerList = new ArrayList();
                totCustomerList = new ArrayList();
                FileInputStream fis = new FileInputStream(file);
                customerList = new ArrayList();
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line = "";
                int x = 0;
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    System.out.println("entered here");
                    numberParameter = strLine.split("DD");

                }
                String splitingfile = numberParameter[0];
                // if(x==0){
                String RefNo = splitingfile.substring(0, 8);
                System.out.println("RefNo" + RefNo.trim());
                HashMap agent = new HashMap();
                agent.put("Ref_NO", RefNo.trim());
                List Agent = ClientUtil.executeQuery("getAgentId1", agent);
                HashMap agentresMap1 = new HashMap();
                if (Agent != null && Agent.size() > 0) {
                    agentresMap1 = (HashMap) Agent.get(0);
                }
                String agentid = CommonUtil.convertObjToStr(agentresMap1.get("AGENT_ID"));
                agentMap.put("AGENT_ID", agentid);
                HashMap dataMap = new HashMap();
                for (int i = 1; i < numberParameter.length; i++) {
                    customerList = new ArrayList();
                    String splitingfile1 = numberParameter[i];

                    if (numberParameter[i].contains("/")) {
                        numberParameter[i] = numberParameter[i].replaceAll("/", "-");
                    }
                    String splitingfile2 = numberParameter[i];
                    customerList.add("DD");
                    customerList.add(splitingfile2.substring(0, 14));
                    customerList.add(splitingfile2.substring(14, 22));
                    String date2 = splitingfile2.substring(22, 30);
                    StringBuffer st1 = new StringBuffer(date2);
                    st1.insert(6, "20");
                    String datenew1 = st1.toString();

                    customerList.add(datenew1);
                    totCustomerList.add(customerList);
                }
                System.out.println("totCustomerList####" + totCustomerList);
                ArrayList key = new ArrayList();
                ArrayList value = new ArrayList();
                key.add("");
                value.add("");
                HashMap map = new HashMap();
                agentMap.put("AGENTID", agentMap.get("AGENT_ID"));

                List lst = ClientUtil.executeQuery("getSelectAgentTO1", agentMap);
                if (lst != null && lst.size() > 0) {
                    AgentTO agTo = new AgentTO();
                    agTo = (AgentTO) lst.get(0);
                    String agCode = CommonUtil.convertObjToStr(agentMap.get("AGENT_ID"));

                    key.add(agTo.getAgentId());//agCode);//map.get("AGENT_ID").toString());//babu 1
                    value.add(agCode);
                    cboAgentType.addItem(agCode);
                    observable.setCbmAgentType(new ComboBoxModel(key, value));
                    cboAgentType.setModel(observable.getCbmAgentType());
                      cboAgentType.setSelectedItem(agCode);
                     }else{
                         ClientUtil.displayAlert("Invlid Agent Id Please check input file");
                         return;
                     }
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("getDataFromAccessDB error : "+e);
        }
        }
      else  if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("KOORKKENCHERRY")) {
            try {
                agentMap = new HashMap();
                ArrayList customerList = new ArrayList();
                totCustomerList = new ArrayList();
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                String line = "";
                int x = 0;
                while (dis.available() > 0 && (line = dis.readLine()).length() > 0) {
                    System.out.println("line#### kor  " + line);
                    String numberParameter[] = (String[]) line.split(";");
                    customerList = new ArrayList();
                    System.out.println("line#### kor " + line);
                    HashMap dataMap = new HashMap();
                    for (int i = 0; i < numberParameter.length; i++) {
                        // if(x==0){
                        agentMap.put("AGENT_ID", numberParameter[0]);
                        //   agentMap.put("NO_TRANS",numberParameter[i+1]);
                        //   agentMap.put("TOT_AMT",numberParameter[i+2]);
                        //    x++;
                        // break;

                        //   }else{
                        if (numberParameter[i].contains("/")) {
                            numberParameter[i] = numberParameter[i].replaceAll("/", "-");
                        }
                        customerList.add(numberParameter[i]);
//                        customerList.add(numberParameter[3]);
//                        customerList.add(numberParameter[1]);
//                        break;
                        // }

//                    System.out.println("dataMap####"+dataMap);
                    }
                    if (!customerList.isEmpty()) {
                        totCustomerList.add(customerList);
                    }
                }
                if (dis != null) {
                    dis.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
                System.out.println("totCustomerList##korrrr##" + totCustomerList);
//            observable.insertTableDataFromFile(totCustomerList);
                cboAgentType.removeAllItems();
                //            String filepath= txtFileName.getText();
                //            String dbUserName="Admin";
                //            String dbPassword="sil123";
                //            getConnection(filepath,dbUserName,dbPassword);
                //            java.sql.Statement s = con.createStatement();
                //            java.sql.ResultSet rs = s.executeQuery("SELECT DISTINCT Ag_Code FROM TRANSACT ");
//            List lst= ClientUtil.executeQuery(""
                ArrayList key = new ArrayList();
                ArrayList value = new ArrayList();
                key.add("");
                value.add("");
                HashMap map = new HashMap();
                agentMap.put("AGENTID", agentMap.get("AGENT_ID"));
                List lst = ClientUtil.executeQuery("getSelectAgentTO1", agentMap);
                if (lst != null && lst.size() > 0) {
                    AgentTO agTo = new AgentTO();
                    agTo = (AgentTO) lst.get(0);
                    String agCode = CommonUtil.convertObjToStr(agentMap.get("AGENT_ID"));

                    key.add(agTo.getAgentId());//agCode);//map.get("AGENT_ID").toString());//babu 1
                    value.add(agCode);
                    cboAgentType.addItem(agCode);
//                   
                    // observable.setCbmAgentType(new ComboBoxModel(key,value));
                    // cboAgentType.setModel(observable.getCbmAgentType());

                } else {
                    ClientUtil.displayAlert("Invlid Agent Id Please check input file");
                    return;
                }
                //            rs.close();
                //            con.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("getDataFromAccessDB error : " + e);
            }
        } else {
            try {


                agentMap = new HashMap();
                ArrayList customerList = new ArrayList();
                totCustomerList = new ArrayList();
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                String line = "";
                int x = 0;

                while (dis.available() > 0 && (line = dis.readLine()).length() > 0) {
                    //            line =dis.readLine();
                    System.out.println("line####" + line);
                    String numberParameter[] = (String[]) line.split(",");
                    customerList = new ArrayList();
                    System.out.println("line####" + line);
                    HashMap dataMap = new HashMap();
                    for (int i = 0; i < numberParameter.length; i++) {
                        if (x == 0) {
                            agentMap.put("AGENT_ID", numberParameter[i]);
                            agentMap.put("NO_TRANS", numberParameter[i + 1]);
                            agentMap.put("TOT_AMT", numberParameter[i + 2]);
                            x++;
                            break;

                        } else {
                            if (numberParameter[i].contains("/")) {
                                numberParameter[i] = numberParameter[i].replaceAll("/", "-");
                            }
                            customerList.add(numberParameter[i]);
//                        customerList.add(numberParameter[3]);
//                        customerList.add(numberParameter[1]);
//                        break;
                        }

//                    System.out.println("dataMap####"+dataMap);
                    }
                    if (!customerList.isEmpty()) {
                        totCustomerList.add(customerList);
                    }
                }
                if (dis != null) {
                    dis.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
                System.out.println("totCustomerList####" + totCustomerList);
//            observable.insertTableDataFromFile(totCustomerList);
                cboAgentType.removeAllItems();
                //            String filepath= txtFileName.getText();
                //            String dbUserName="Admin";
                //            String dbPassword="sil123";
                //            getConnection(filepath,dbUserName,dbPassword);
                //            java.sql.Statement s = con.createStatement();
                //            java.sql.ResultSet rs = s.executeQuery("SELECT DISTINCT Ag_Code FROM TRANSACT ");
//            List lst= ClientUtil.executeQuery(""
                ArrayList key = new ArrayList();
                ArrayList value = new ArrayList();
                key.add("");
                value.add("");
                HashMap map = new HashMap();
                agentMap.put("AGENTID", agentMap.get("AGENT_ID"));
                List lst = ClientUtil.executeQuery("getSelectAgentTO1", agentMap);
                if (lst != null && lst.size() > 0) {
                    AgentTO agTo = new AgentTO();
                    agTo = (AgentTO) lst.get(0);
                    String agCode = CommonUtil.convertObjToStr(agentMap.get("AGENT_ID"));

                    key.add(agTo.getAgentId());//agCode);//map.get("AGENT_ID").toString());//babu 1
                    value.add(agCode);
                    cboAgentType.addItem(agCode);
//                   
//                     observable.setCbmAgentType(new ComboBoxModel(key,value));
//                     cboAgentType.setModel(observable.getCbmAgentType());
                } else {
                    ClientUtil.displayAlert("Invlid Agent Id Please check input file");
                    return;
                }
                //            rs.close();
                //            con.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("getDataFromAccessDB error : " + e);
            }
        }
    }

    //    public void fillCboAgentCode() {
    //        try
    //        {
    //            cboAgentType.removeAllItems();
    //            String filepath= txtFileName.getText();
    //            String dbUserName="Admin";
    //            String dbPassword="sil123";
    //            getConnection(filepath,dbUserName,dbPassword);
    //            java.sql.Statement s = con.createStatement();
    //            java.sql.ResultSet rs = s.executeQuery("SELECT DISTINCT Ag_Code FROM TRANSACT ");
    //            ArrayList key = new ArrayList();
    //            ArrayList value = new ArrayList();
    //            key.add("");
    //            value.add("");
    //            while(rs.next()) {
    //                String agCode=rs.getString("Ag_Code");
    //                key.add(agCode);
    //                value.add(agCode);
    ////                cboAgentType.addItem(agCode);
    //            }
    //            observable.setCbmAgentType(new ComboBoxModel(key,value));
    //            cboAgentType.setModel(observable.getCbmAgentType());
    //            rs.close();
    ////            con.close();
    //        } catch(Exception e) {
    //            e.printStackTrace();
    //             System.out.println("getDataFromAccessDB error : "+e);
    //        }
    //    }
    public void getConnection(String filePath, String dbUName, String dbPass) {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            con = java.sql.DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + filePath, dbUName, dbPass);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Connection get method error : " + e);
        }
        //        return con;
    }

    public void setDataToTable() {
        try {
            HashMap wheremap1 = new HashMap();
            String cbAg = cboAgentType.getSelectedItem().toString();
            lblTransactionDateDesc1.setText(DateUtil.getStringDate(curDate));
            // if(cbAg!=null && !cbAg.equals("") && !txtFileName.getText().equals("")) {
            if (cbAg != null && !cbAg.equals("")) {

                if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("POLPULLY")) {

                    //                initTableData();
                    //                filepath= path.getText();
                    //                con=getConnection(filepath,dbUserName,dbPassword);
//                java.sql.Statement s = con.createStatement();
//                java.sql.ResultSet rs = s.executeQuery("SELECT t.AcNo AS AC_NO,t.CollectAmt AS AMT,m.Name AS NAME, t.CollectDt as DT FROM Transact t INNER JOIN Master m ON t.AcNo=m.AccountNo WHERE t.Ag_Code='"+cbAg+"'");

                    //                model.setRowCount(0);
                    String actNosNotFound = "";
                    for (int i = 0; i < totCustomerList.size(); i++) {// while(rs.next()) {
                        ArrayList singleList = (ArrayList) totCustomerList.get(i);
                        System.out.println("singleList#####" + singleList);
                        String Prod_Type = CommonUtil.convertObjToStr(singleList.get(0));
                        String Name = CommonUtil.convertObjToStr(singleList.get(1));
                        String AcNo = CommonUtil.convertObjToStr(singleList.get(2)) + "_1";   //rs.getString("AC_NO")+"_1";
                        String Amount = CommonUtil.convertObjToStr(singleList.get(3));  //rs.getString("AMT");
                        String stringDate = CommonUtil.convertObjToStr(singleList.get(5));
//                    Date date=DateUtil.getDateMMDDYYYY(stringDate);

                        Date date = DATE_FORMAT.parse(stringDate);

//                    java.util.Date dt = new Date(CommonUtil.convertObjToStr(singleList.get(5)));
//                    dt = dt+1900;
                        System.out.println("Name#####" + Name + "AcNo   " + AcNo + " Amount" + Amount + "date   " + date);
                        Date instDt = new Date(date.getYear(), date.getMonth(), date.getDate());
                        System.out.println("instDt#####" + instDt);
                        tdtInstrumentDate.setDateValue(DateUtil.getStringDate(instDt));
                        observable.setProdType(Prod_Type);
                        btnDailyDepSubNewActionPerformed(null);
                        txtAccNo.setText(AcNo);
                        txtAmount.setText(Amount);
//                    lblCustnmVal.setText(Name);
                        try {

                            observable.setAccountName(AcNo, Prod_Type);
                            lblCustnmVal.setText(observable.getLblAccName());
                        } catch (Exception e) {
                            if (AcNo.length() > 0) {
                                actNosNotFound += AcNo + "\n";
                            }
                            e.printStackTrace();
                            continue;
                        }
                        observable.setTxtAccNo(AcNo);
                        observable.setFlagSave("IMPORT");
                        btnDailyDepSubSaveActionPerformed(null);

                        //                    observable.getTbmTransfer().addRow(new String[]{AcNo,Name,Amount});
                    }
                    if (actNosNotFound.length() > 0) {
                        actNosNotFound = "Following A/c Nos. not found...\n" + actNosNotFound;
                        ClientUtil.showAlertWindow(actNosNotFound);
                    }
//                rs.close();
                    //                con.close();
                }
                if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("KOORKKENCHERRY")) {
                    String actNosNotFound = "";
                    wheremap1.put("AGENT_ID", cbAg);
                    wheremap1.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                    ClientUtil.execute("TruncateTempDailyKor", wheremap1);
                    System.out.println("totCustomerList --->"+totCustomerList +" tot size--->"+totCustomerList.size());
                    for (int i = 0; i < totCustomerList.size(); i++) {// while(rs.next()) {
                        ArrayList singleList = (ArrayList) totCustomerList.get(i);
                        System.out.print("singleList---KORR->"+singleList +"   jjj-->"+singleList.size());
                         String type = CommonUtil.convertObjToStr(singleList.get(5));
                         String code = CommonUtil.convertObjToStr(singleList.get(4));
                         if(code!=null && code.equals("N")){
                        String Prod_Type = "";
                        String AcNo ="";
                        wheremap1.put("TYPE", type);
                        List agentProdLst =ClientUtil.executeQuery("getAgentProdData", wheremap1);
                        for(int j=0;j<agentProdLst.size();j++){
                            HashMap agList=(HashMap)agentProdLst.get(j);
                       //     String agType=CommonUtil.convertObjToStr(agList.get("PROD_EXP_CODE"));
                            String prType=CommonUtil.convertObjToStr(agList.get("PROD_TYPE"));
                            System.out.println("prType ------>"+prType);
                            if(prType.equals("SA")/* && type.equals(agType)*/){
                                Prod_Type = "SA";
                                AcNo = CommonUtil.convertObjToStr(singleList.get(3)); 
                            }
                            else if(prType.equals("OA")/* && type.equals(agType)*/){
                                Prod_Type = "OA";
                                AcNo = CommonUtil.convertObjToStr(singleList.get(3)); 
                            }
                            else{
                                Prod_Type = "TD";
                                AcNo = CommonUtil.convertObjToStr(singleList.get(3)) + "_1";   //rs.getString("AC_NO")+"_1";
                            }
                           /* if(type!=null && (type.equals("V") || type.equals("P") ||type.equals("M")) ){
                                Prod_Type = "SA";
                                AcNo = CommonUtil.convertObjToStr(singleList.get(3)); 
                            }
                            else if(type!=null && (type.equals("G")) ){
                                Prod_Type = "OA";
                                AcNo = CommonUtil.convertObjToStr(singleList.get(3)); 
                            }        
                            else{
                                Prod_Type = "TD";
                                AcNo = CommonUtil.convertObjToStr(singleList.get(3)) + "_1";   //rs.getString("AC_NO")+"_1";
                            }*/
                            txtAgentProdId.setText(CommonUtil.convertObjToStr(agList.get("PROD_ID")));
                            observable.setProdId(CommonUtil.convertObjToStr(agList.get("PROD_ID")));
                        }
                        String Name = CommonUtil.convertObjToStr(singleList.get(0));
                       
                        String Amount = CommonUtil.convertObjToStr(singleList.get(2));  //rs.getString("AMT");
                        String stringDate = CommonUtil.convertObjToStr(singleList.get(6));
//                    Date date=DateUtil.getDateMMDDYYYY(stringDate);
                        String year = stringDate.substring(0, 4);
                        String month = stringDate.substring(4, 6);
                        String day = stringDate.substring(6, 8);
                        System.out.println(stringDate + "year -->" + year + "month-->" + month + "day-->" + day);
                        stringDate = day + "-" + month + "-" + year;
                        Date date = DATE_FORMAT.parse(day + "-" + month + "-" + year);
                        //comm calculation
                        double commAmt = 0;
                        HashMap agMap = new HashMap();
                        agMap.put("ACT_NUM",CommonUtil.convertObjToStr(singleList.get(3)));
                        List agentComm= ClientUtil.executeQuery("getCollectionAgentPercentage",agMap);
                        if(agentComm!=null && agentComm.size()>0){
                            agMap=(HashMap)agentComm.get(0);
                            System.out.println("agMap------>"+agMap);
                            if(agMap!=null && agMap.get("COMM_PER_AC_HOLDR")!=null &&
                                    CommonUtil.convertObjToDouble(agMap.get("COMM_PER_AC_HOLDR"))>0){
                                double agCommPer = CommonUtil.convertObjToDouble(agMap.get("COMM_PER_AC_HOLDR"));
                                double Amt= CommonUtil.convertObjToDouble(singleList.get(2));
                                commAmt=(Amt*agCommPer)/100;
                                 System.out.println("agMap------>"+commAmt);
                            }
                        }
                        //end
                        LoadDataToDBForKorr(singleList, stringDate,commAmt);
                        System.out.println("singleList##hkorrrr###" + singleList);
//                    java.util.Date dt = new Date(CommonUtil.convertObjToStr(singleList.get(5)));
//                    dt = dt+1900;
                        System.out.println("Name###koooooo##" + Name + "AcNo   " + AcNo + " Amount" + Amount + "date   " + date);
                        Date instDt = new Date(date.getYear(), date.getMonth(), date.getDate());
                        System.out.println("instDt#korrrr####" + instDt);
                        tdtInstrumentDate.setDateValue(DateUtil.getStringDate(instDt));
                        System.out.println("observable --->" + observable + "Prod_Type-->" + Prod_Type);
                        if (observable == null) {
                            observable = new DailyDepositTransOB();
                            observable.addObserver(this);
                        }
                        txtProdType.setText(Prod_Type);
                        observable.setProdType(Prod_Type);
                     //   btnDailyDepSubNewActionPerformed(null);
                        txtAccNo.setText(AcNo);
                        txtAmount.setText(Amount);
//                    lblCustnmVal.setText(Name);
                        try {
                            observable.setProdType(Prod_Type);
                            observable.setAccountName(AcNo, Prod_Type);
                            lblCustnmVal.setText(observable.getLblAccName());
                        } catch (Exception e) {
                            if (AcNo.length() > 0) {
                                actNosNotFound += AcNo + "\n";
                            }
                            e.printStackTrace();
                            continue;
                        }
                        observable.setTxtAccNo(AcNo);
                        observable.setProdType(Prod_Type);
                        observable.setFlagSave("IMPORT");
                        btnDailyDepSubSaveActionPerformed(null);
                        //                    observable.getTbmTransfer().addRow(new String[]{AcNo,Name,Amount});
                    }
                    }
                    if (actNosNotFound.length() > 0) {
                        actNosNotFound = "Following A/c Nos. not found...\n" + actNosNotFound;
                        ClientUtil.showAlertWindow(actNosNotFound);
                    }
//                rs.close();
                    //                con.close();
                }
                if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("PERINGANDUR")) {
                    String actNosNotFound = "";
                    for (int i = 0; i < totCustomerList.size(); i++) {
                        ArrayList singleList = (ArrayList) totCustomerList.get(i);
                        System.out.println("singleList#PERINGANDUR####" + singleList);
                        String ProdType = CommonUtil.convertObjToStr(singleList.get(0));
                        String Name = CommonUtil.convertObjToStr(singleList.get(1));
                        String AcNo = "";
                        if (ProdType != null && ProdType.equals("D")) {
                            AcNo = CommonUtil.convertObjToStr(singleList.get(2)) + "_1";   //rs.getString("AC_NO")+"_1";
                        }
                        if (ProdType != null && ProdType.equals("L")) {
                            AcNo = CommonUtil.convertObjToStr(singleList.get(2));//+"_1"; 
                        }
                        if (ProdType != null && ProdType.equals("M")) {
                            AcNo = CommonUtil.convertObjToStr(singleList.get(2)) + "_1";
                        }
                        String Amount = CommonUtil.convertObjToStr(singleList.get(3));  //rs.getString("AMT");
                        String stringDate = CommonUtil.convertObjToStr(singleList.get(5));
                        Date date = DATE_FORMAT.parse(stringDate);
                        System.out.println("Name##PERINGANDUR###" + Name + "AcNo   " + AcNo + " Amount" + Amount + "date   " + date);
                        Date instDt = new Date(date.getYear(), date.getMonth(), date.getDate());
                        System.out.println("instDt###PERINGANDUR##" + instDt);
                        tdtInstrumentDate.setDateValue(DateUtil.getStringDate(instDt));
                        btnDailyDepSubNewActionPerformed(null);
                        txtAccNo.setText(AcNo);
                        txtProdType.setText(ProdType);
                        double abm = CommonUtil.convertObjToDouble(Amount);
                        abm = Math.abs(abm);
                        Amount = String.valueOf(abm);
                        txtAmount.setText(Amount);
                        try {
                            observable.setProdType(ProdType);
                            observable.setAccountName(AcNo, ProdType);

                            lblCustnmVal.setText(observable.getLblAccName());
                        } catch (Exception e) {
                            if (AcNo.length() > 0) {
                                actNosNotFound += AcNo + "\n";
                            }
                            e.printStackTrace();
                            continue;
                        }
                        observable.setProdType(ProdType);
                        observable.setTxtAccNo(AcNo);
                        System.out.println("ProdType OOOOO" + ProdType);
                        observable.setFlagSave("IMPORT");
                        btnDailyDepSubSaveActionPerformed(null);
                    }
                    if (actNosNotFound.length() > 0) {
                        actNosNotFound = "Following A/c Nos. not found...\n" + actNosNotFound;
                        ClientUtil.showAlertWindow(actNosNotFound);
                    }
                }
                if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("CHELLANOOR")) {
                    wheremap1.put("AGENT_ID", cbAg);
                    wheremap1.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                    ClientUtil.execute("TruncateTempDaily", wheremap1);
                    String actNosNotFound = "";
                    for (int i = 0; i < totCustomerList.size(); i++) {// while(rs.next()) {
                        ArrayList singleList = (ArrayList) totCustomerList.get(i);
                        LoadDataToDB(singleList);
                        System.out.println("singleList#####" + singleList);
                        String Prod_Type = CommonUtil.convertObjToStr(singleList.get(0));
                         if(Prod_Type.equals("DD")){
                                Prod_Type = "TD";
                         }
                        String AcNo1 = CommonUtil.convertObjToStr(singleList.get(1));
                        String Amount = CommonUtil.convertObjToStr(singleList.get(2));
                        String stringDate = CommonUtil.convertObjToStr(singleList.get(3));
                        HashMap wheremap = new HashMap();
                        HashMap result = new HashMap();
                        wheremap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                        wheremap.put("REFNO", AcNo1);
                        wheremap.put("AGENT_ID", CommonUtil.convertObjToStr(cboAgentType.getSelectedItem()));
                        List acctList = ClientUtil.executeQuery("getSelectDepNO", wheremap);
                        if (acctList != null && acctList.size()>0) {
                            result = (HashMap) acctList.get(0);
                        }
                        String AcNo = CommonUtil.convertObjToStr(result.get("DEPOSIT_NO")) + "_1";
                        Date date = DATE_FORMAT.parse(stringDate);
                        Date instDt = new Date(date.getYear(), date.getMonth(), date.getDate());
                        tdtInstrumentDate.setDateValue(DateUtil.getStringDate(instDt));
                        observable.setProdType(Prod_Type);
                        txtProdType.setText(Prod_Type);
                        txtAccNo.setText(AcNo);
                        txtAmount.setText(Amount);
                        try {
                            observable.setAccountName(AcNo, Prod_Type);
                            lblCustnmVal.setText(observable.getLblAccName());
                        } catch (Exception e) {
                            if (AcNo.length() > 0) {
                                actNosNotFound += AcNo + "\n";
                            }
                            e.printStackTrace();
                            continue;
                        }
                        if (acctList != null && acctList.size()>0) {
                             observable.setFlagSave("IMPORT");
                             btnDailyDepSubSaveActionPerformed(null);
                        }     
                    }
                    if (actNosNotFound.length() > 0) {
                        actNosNotFound = "Following A/c Nos. not found...\n" + actNosNotFound;
                        ClientUtil.showAlertWindow(actNosNotFound);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void LoadDataToDB(ArrayList singlelist) {
        Iterator<String> it = singlelist.iterator();
        try {
            HashMap wheremap = new HashMap();            
            wheremap.put("AGENT_ID", cboAgentType.getSelectedItem().toString());
            wheremap.put("REF_NO", singlelist.get(1));
            wheremap.put("AMOUNT", singlelist.get(2));
            wheremap.put("date1", singlelist.get(3));
            wheremap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            ClientUtil.execute("InsertIntoTemp", wheremap);

        } catch (Exception e) {

            System.out.println("exception" + e);
            //  ClientUtil.displayAlert("Exception");
        }


    }

    private void LoadDataToDBForKorr(ArrayList singlelist, String date,double commAmt) {
        Iterator<String> it = singlelist.iterator();
        try {
            HashMap wheremap = new HashMap();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy"); 
            Date dateFormat = formatter.parse(date);	
            wheremap.put("AGENT_ID", cboAgentType.getSelectedItem().toString());
            wheremap.put("REF_NO", singlelist.get(3));
            wheremap.put("AMOUNT", CommonUtil.convertObjToDouble(singlelist.get(2)));
            wheremap.put("date1", dateFormat);
            wheremap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            wheremap.put("COMM_AMT", commAmt);
            ClientUtil.execute("InsertIntoTempKor", wheremap);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("exception" + e);
        }
    }
    
    private void txtAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccNoFocusLost
        // TODO add your handling code here:
        txtAccNoActionPerformed();

    }//GEN-LAST:event_txtAccNoFocusLost

    private void txtAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusLost
        // TODO add your handling code here:
        //        double txtAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
        //        String actNum = txtAccNo.getText();
        //        if(actNum.lastIndexOf("_")!=-1)
        //            actNum = actNum.substring(0,actNum.lastIndexOf("_"));
        //        HashMap dailyMap = new HashMap();
        //        dailyMap.put("DEPOSIT_NO",actNum);
        //        List lst = ClientUtil.executeQuery("getDepositDetailsForIntAcDaily", dailyMap);
        //        if(lst!=null && lst.size()>0){
        //            dailyMap = (HashMap)lst.get(0);
        //            double depositAmt = CommonUtil.convertObjToDouble(dailyMap.get("AMOUNT")).doubleValue();
        //            if(txtAmt%depositAmt !=0){
        //                ClientUtil.showAlertWindow("Enter Multiples DepositAmt of "+depositAmt);
        //                txtAmount.setText("");
        //                txtAmount.requestFocus();
        //                return;
        //            }
        //        }
    }//GEN-LAST:event_txtAmountFocusLost
    private void txtAccNoActionPerformed() {
        Date collctDt = DateUtil.getDateMMDDYYYY(tdtInstrumentDate.getDateValue());
        if (collctDt == null) {
            ClientUtil.showAlertWindow("Collection Date Should not be empty...Please cancel again you start freshly");
            tdtInstrumentDate.setEnabled(true);
            return;
        } else {
            HashMap fillMap = new HashMap();
            String deposit_No = CommonUtil.convertObjToStr(txtAccNo.getText());
            if (deposit_No.lastIndexOf("_") != -1) {
                deposit_No = deposit_No.substring(0, deposit_No.lastIndexOf("_"));
            }
            fillMap.put("DEPOSIT_NO", deposit_No);
            List oldList = ClientUtil.executeQuery("getDepositNumFromOldValue", fillMap);
            if (oldList != null && oldList.size() > 0) {


                HashMap actMap = (HashMap) oldList.get(0);
                if (actMap.containsKey("NEW_ACT_NUM") && actMap.get("NEW_ACT_NUM") != null) {
                    deposit_No = CommonUtil.convertObjToStr(actMap.get("NEW_ACT_NUM"));
                    if (deposit_No.lastIndexOf("_") != -1) {
                        deposit_No = deposit_No.substring(0, deposit_No.lastIndexOf("_"));
                    }
                    txtAccNo.setText(deposit_No);
                    viewType = ACCNO;
                    fillMap.put("DEPOSIT_NO", deposit_No);
                }
            }
//            fillMap.put("AGENT_ID", cboAgentType.getSelectedItem());
//            fillMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
//            List lst = ClientUtil.executeQuery("getDepositDetailsForDaily", fillMap);
//            if (lst != null && lst.size() > 0) {
//                fillMap = (HashMap) lst.get(0);
//                viewType = ACCNO;
//                fillData(fillMap);
//            } else {
//                ClientUtil.showAlertWindow("Invalid Account number...");
//                txtAccNo.setText("");
//            }
        }
    }
    private void tdtInstrumentDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtInstrumentDateFocusLost
        // TODO add your handling code here:
        //        if(observable.)
        Date IsDt = DateUtil.getDateMMDDYYYY(tdtInstrumentDate.getDateValue());
        if (IsDt != null) {
            Date collDT = (Date) curDate.clone();
            collDT.setDate(IsDt.getDate());
            collDT.setMonth(IsDt.getMonth());
            collDT.setYear(IsDt.getYear());
            //        collDT=DateUtil.getDateMMDDYYYY(tdtInstrumentDate.getDateValue());
            //        collDT=DateUtil.getDateMMDDYYYY(tdtInstrumentDate.getDateValue());
        } else {
            collDT = DateUtil.getDateMMDDYYYY(tdtInstrumentDate.getDateValue());
        }
        System.out.println("instrumentdate####" + tdtInstrumentDate.getDateValue());
        Date DayDt = (Date) curDate.clone();

        if (collDT != null) {
            if (DateUtil.dateDiff(collDT, DayDt) <= 0) {
                ClientUtil.displayAlert("Collection Date is greater the DayBegin Date");
                tdtInstrumentDate.setDateValue("");
            }
            HashMap collMap = new HashMap();
            collMap.put("COLL_DT", collDT);
            collMap.put("AGENT_NO", cboAgentType.getSelectedItem());
            System.out.println("collMap&&&&&&&&&&&" + collMap);
            List lst = null;
            if (collDT != null) {
                lst = ClientUtil.executeQuery("selectDistinctCollectionDateForDaily", collMap);
            }
            if (lst != null && lst.size() > 0) {
                ClientUtil.displayAlert("Collection Date is Allready Added");
                tdtInstrumentDate.setDateValue("");

            }
            Amt = 0.0;
            collMap.put("AGENTID", collMap.get("AGENT_NO"));
            List agLst = ClientUtil.executeQuery("getSelectAgentTO", collMap);

            AgentTO agTo = new AgentTO();
            agTo = (AgentTO) agLst.get(0);
            Date Appdt = agTo.getAppointedDt();
            System.out.println("collDT#########" + collDT);
            System.out.println("Appdt#########" + Appdt);
            Date lstComDt = agTo.getLastComPaidDt();
            lstComDt = DateUtil.addDays(lstComDt, -1);
            if (DateUtil.dateDiff(Appdt, collDT) < 0) {
                ClientUtil.displayAlert("Collection Date Should be \n Greater Than or Equal to Appointed Date");
                tdtInstrumentDate.setDateValue("");
            }
            if (DateUtil.dateDiff(lstComDt, collDT) <= 0) {
                ClientUtil.displayAlert("Collection Date Should be \n Greater Than Last Commission paid Date");
                tdtInstrumentDate.setDateValue("");
            }
            collMap = null;
        }
    }//GEN-LAST:event_tdtInstrumentDateFocusLost

    private void tblDailyDepositListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDailyDepositListMouseClicked
        // Add your handling code here:
        tblClicked = true;
        btnAgentProdId.setEnabled(true);
        if (observable.getOperation() != ClientConstants.ACTIONTYPE_DELETE
                && observable.getOperation() != ClientConstants.ACTIONTYPE_AUTHORIZE
                && observable.getOperation() != ClientConstants.ACTIONTYPE_EXCEPTION
                && observable.getOperation() != ClientConstants.ACTIONTYPE_REJECT) {
            //                panEnableDisable(true);
        } else {
            ClientUtil.enableDisable(this, false);  // Disables the panel...
        }
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            observable.populatTranferTO(tblDailyDepositList.getSelectedRow());
            authorizeCheckMap.put(String.valueOf(tblDailyDepositList.getSelectedRow()), String.valueOf(tblDailyDepositList.getSelectedRow()));
        }
        updationForDaily();

        //         txtAccNoActionPerformed(null);
        _intTransferNew = false;
        if (observable.getOperation() != ClientConstants.ACTIONTYPE_DELETE
                && observable.getOperation() != ClientConstants.ACTIONTYPE_AUTHORIZE
                && observable.getOperation() != ClientConstants.ACTIONTYPE_EXCEPTION
                && observable.getOperation() != ClientConstants.ACTIONTYPE_REJECT) {
            ClientUtil.enableDisable(this, true);
            cboAgentType.setEnabled(false);
            btnAgentProdId.setEnabled(false);
            tdtInstrumentDate.setEnabled(false);
            btnAccNo.setEnabled(true);
            btnDailyDepSubSave.setEnabled(true);
            btnDailyDepSubNew.setEnabled(false);
            lblTransactionDateDesc1.setText(DateUtil.getStringDate(curDate));

        }
        //         observable.setTotalGlAmt(Amt);
        txtProdType.setEnabled(false);
        txtAgentProdId.setEnabled(false);
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDailyDepositListMouseClicked

    private void panEnableDisable(boolean value) {
        //this.lblAccountHeadValue.setText("");
        ClientUtil.enableDisable(this, value);
        enableDisableButtons(value);
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE) {
            ClientUtil.enableDisable(this.panData, false);
        }
        ClientUtil.enableDisable(this.panData, true);
        this.btnAccNo.setEnabled(false);
        btnDailyDepSubSave.setEnabled(true);
        btnDailyDepSubNew.setEnabled(false);
        //        }
        //        observable.resetTransactionDetails();
    }

    public void updateData(Observable ob, Object arg) {
        //      txtAccNo.setText(observable.getTxtAccNo());
        txtAmount.setText(observable.getTxtAmount());
        //      lblBalance.setText(observable.getBalance());
        if (observable.getOperation() != ClientConstants.ACTIONTYPE_NEW) {
            this.updateTable();
        }
    }

    private void updationForDaily() {
        observable.populateDailyTransfer(tblDailyDepositList.getSelectedRow());
        tdtInstrumentDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtInstrumentDate()));
        txtProdType.setText(CommonUtil.convertObjToStr(observable.getProdType()));
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            cboAgentType.addItem(observable.getCboAgentType());
            cboAgentType.setSelectedItem(observable.getCboAgentType());
            txtAgentProdId.setText(observable.getProdId());
            agentMap=new HashMap();
            agentMap.put("AGENT_ID",observable.getCboAgentType());
            List lst = ClientUtil.executeQuery("getAgentDetailsName", agentMap);
            agentMap = null;
            if (lst != null && lst.size() > 0) {
                agentMap = (HashMap) lst.get(0);
                lblAgNmVal.setText(CommonUtil.convertObjToStr(agentMap.get("AGENT_NAME")));
            }
            lblAccHd.setText("Prod Desc");
            
        }
        if(observable.getFlagSave()!=null && observable.getFlagSave().equals("IMPORT")){
            txtAgentProdId.setText(observable.getProdId());
        }
        try {
             observable.setAccountName(txtAccNo.getText(),txtProdType.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //        authorizationCheckMap.put(String.valueOf(tblTransList.getSelectedRow()), String.valueOf(tblTransList.getSelectedRow()));
        //        updateAccountInfo();
        _intTransferNew = false;
        //            ClientUtil.enableDisable(panData,true);
        //            this.btnAccNo.setEnabled(true);

        //        } else {
        //            ClientUtil.enableDisable(panData,false);
        //            btnAccNo.setEnabled(false);
        //        }

        if (observable.getTxtAmount().equals(CommonConstants.CREDIT)) {
            fieldsEnableDisable(false);
        } else {
            fieldsEnableDisable(true);
        }
        tdtInstrumentDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtInstrumentDate()));
        observable.ttNotifyObservers();
        //         txtAccNoActionPerformed(null);
    }

    private void fieldsEnableDisable(boolean yesno) {
        //        this.txtTokenNo.setEnabled(false);
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getOperation() == ClientConstants.ACTIONTYPE_EXCEPTION
                || observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT) {
            yesno = false;
        }
    }

    private void txtAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccNoActionPerformed
        //        // TODO add your handling code here:
        //        HashMap hash = new HashMap();
        //        hash.put("ACT_NUM", txtAccNo.getText());
        //        int oldViewType = viewType;
        //        viewType = ACCNO;
        //        fillData(hash);
        //        viewType = oldViewType;
        //        populatingAcctNo();
    }//GEN-LAST:event_txtAccNoActionPerformed

    private void btnDailyDepSubDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDailyDepSubDelActionPerformed

//        if (ClientUtil.deleteAlert()==0) { // Add your handling code here:
        observable.setTransStatus(CommonConstants.STATUS_DELETED);
        int rowSelected = this.tblDailyDepositList.getSelectedRow();
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT
                && rowSelected < 0) {
            rowSelected = this.rowForEdit;
        }
        String agentNo = CommonUtil.convertObjToStr(cboAgentType.getSelectedItem());
        System.out.println("agentNo^^^^^^^^^^^" + agentNo);
        observable.deleteDailyData(rowSelected);
        this.updateTable();
        this.updateDailyInfo();
//            observable.setCboAgentType(agentNo);
//             cboAgentType.setSelectedItem(observable.getCboAgentType());
//            double amt=CommonUtil.convertObjToDouble(lblTotAmtVal.getText()).doubleValue();
        Amt = Amt - CommonUtil.convertObjToDouble(observable.getTxtAmount()).doubleValue();
        lblTotAmtVal.setText(CommonUtil.convertObjToStr(new Double(Amt)));

        panEnableDisable(false);
        observable.resetForm();
        observable.resetLable();
        btnDailyDepSubNew.setEnabled(true);
        btnDailyDepSubSave.setEnabled(false);
        cboAgentType.setEnabled(false);
        tdtInstrumentDate.setEnabled(false);
        txtAmount.setEnabled(false);
        txtAccNo.setEnabled(false);
        observable.setCboAgentType(agentNo);
        cboAgentType.setSelectedItem(observable.getCboAgentType());
        lblTransactionDateDesc1.setText(DateUtil.getStringDate(curDate));
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            observable.setTransStatus(CommonConstants.STATUS_CREATED);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            observable.setTransStatus(CommonConstants.STATUS_MODIFIED);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            observable.setTransStatus(CommonConstants.STATUS_DELETED);
        } else {
            observable.setTransStatus(CommonConstants.STATUS_CREATED);
        }
        txtProdType.setEnabled(false);
        txtAgentProdId.setEnabled(false);
//        }
    }//GEN-LAST:event_btnDailyDepSubDelActionPerformed

    private void setDepSubNoFields(boolean val) {
        ClientUtil.enableDisable(panLables, val);
    }

    private void setBtnDepSubNo(boolean val) {
        btnDailyDepSubNew.setEnabled(val);
        //        btnDailyDepSubSave.setEnabled(val);
        btnDailyDepSubDel.setEnabled(val);
    }

    private void dispContents() {
        btnDailyDepSubNew.setEnabled(false);
        btnDailyDepSubSave.setEnabled(true);
        btnDailyDepSubDel.setEnabled(false);
    }

    private void btnDailyDepSubNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDailyDepSubNewActionPerformed
        // Add your handling code here:
        String agentId = CommonUtil.convertObjToStr(cboAgentType.getSelectedItem());
        Date collctDt = DateUtil.getDateMMDDYYYY(tdtInstrumentDate.getDateValue());
        if (collctDt == null){
           tdtInstrumentDate.setDateValue(DateUtil.getStringDate((Date) curDate.clone()));         
        }
        if (agentId.equals("")) {
            ClientUtil.showAlertWindow("Agent Id should not be empty...");
            return;
        } /*else if (collctDt == null) {
            ClientUtil.showAlertWindow("Collection Date should not be empty...");
            return;
        }*/ else {
            observable.resetDepSubNo();
            updateOBFields();
            dispContents();
            //        observable.resetDepSubNo();
            observable.resetLable();                // Reset the Editable Lables in the UI to null...
            ClientUtil.enableDisable(this, true);   // Enables the panel...
            //        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);//Sets the Action Type to be performed...
            observable.setStatus();                 // To set the Value of lblStatus...
            enableDisableButtons(true);                 // To enable the buttons(folder) in the UI...
            //        observable.setInitiatorChannelValue();  // To Set Initiator Type as Cashier...
            //        textEnable();                          // To set the Text Boxes for Account No. and InitiatorChannel, nonEditable...
            setModified(true);
            // TODO add your handling code here:
            ClientUtil.enableDisable(panData, true);
            cboAgentType.setEnabled(false);
            btnAgentProdId.setEnabled(true);
            tdtInstrumentDate.setEnabled(false);
            lblTransactionDateDesc1.setText(DateUtil.getStringDate(curDate));
            _intTransferNew = true;
            btnSave.setEnabled(true);
            txtProdType.setEnabled(false);
            txtAgentProdId.setEnabled(false);
        }
    }//GEN-LAST:event_btnDailyDepSubNewActionPerformed

    public void updateDailyInfo() {
        //        lblBalance.setText(String.valueOf(observable.getBalance()));
        txtAccNo.setText(String.valueOf(observable.getTxtAccNo()));
        txtProdType.setText(String.valueOf(observable.getProdType()));
    }

    private void btnDailyDepSubSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDailyDepSubSaveActionPerformed
        updateOBFields();
        StringBuffer mandatoryMessage = new StringBuffer();
        mandatoryMessage.append(new MandatoryCheck().checkMandatory(getClass().getName(), panCashTransaction));
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage.toString());
        } else {
            if(CommonUtil.convertObjToDouble(txtAmount.getText())>0){
                if (!this._intTransferNew) {
                    int rowSelected = this.tblDailyDepositList.getSelectedRow();
                    if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT
                        && rowSelected < 0) {
                        rowSelected = this.rowForEdit;
                        observable.setTransStatus(CommonConstants.STATUS_MODIFIED);
                    }
                    observable.insertTransferData(rowSelected);
                } else {
                    if (checkAccNum()) {
                        ClientUtil.displayAlert("Already This Account is Added in The list!!!");
                        return;
                    } else {
                        observable.insertTransferData(-1);
                   
                    }
            }
            }else{
                ClientUtil.displayAlert("Amount should be greater than zero!!!");
                return;
            }             
        }
        observable.setFlagSave("SAVE");
        this.updateTable();
        updateDailyInfo();
        setEditFieldsEnable(true);
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE) {
            enableDisableButtons(false);        // To disable the buttons(folder) in the UI...
        }        //        ClientUtil.enableDisable(this, false);// Disables the panel...
        btnDailyDepSubDel.setEnabled(true);
        btnDailyDepSubNew.setEnabled(true);
        //        btnDenomination.setEnabled(true);
        //        observable.resetForm(); //changed from chandra
        //        System.out.println("txtAccNo"+txtAccNo.getText());
        //        System.out.println("tblDailyDepositList.getValueAt(i,0)))"+tblDailyDepositList.getValueAt(0,0));
        //        System.out.println("tblDailyDepositList.getRowCount()"+tblDailyDepositList.getRowCount());
        //        if(tblDailyDepositList.getRowCount()>0){
        //        for(int i=0;i<tblDailyDepositList.getRowCount();i++){
        //            if(txtAccNo.getText().equals(CommonUtil.convertObjToStr(tblDailyDepositList.getValueAt(i,0)))){
        //                 System.out.println("tblDailyDepositList.getValueAt(i,0)))"+tblDailyDepositList.getValueAt(i,0));
        //                ClientUtil.displayAlert(resourceBundle.getString("AlreadyAdd"));
        ////                return;
        //            }
        //
        //        }}
        txtAccNo.setText("");
        txtProdType.setText("");
        lblCustnmVal.setText("");
        lblBalanceVal.setText("");
        txtAmount.setText("");
        lblTotInstVal.setText("");
        ClientUtil.enableDisable(this, false);
        btnDailyDepSubSave.setEnabled(false);
        //        btnDailyDepSubSave.setEnabled(false);
        //            setButtonEnableDisable();           // Enables or Disables the buttons and menu Items depending on their previous state...
        //            observable.setResultStatus();       // To Reset the Value of lblStatus...
        //            enableDisableButtons(false);
        //            setModified(false);
        //        this.disableButtons();
        viewType = -1;
        Amt = totalAmt();
        lblTotAmtVal.setText(CommonUtil.convertObjToStr(new Double(Amt)));
        // TODO add your handling code here:
        lblTransactionDateDesc1.setText(DateUtil.getStringDate(curDate));
        _intTransferNew = false;
        lblTotAmtVal.setText(CommonUtil.convertObjToStr(new Double(Amt)));
        tblClicked = false;
        txtProdType.setEnabled(false);
        txtAgentProdId.setEnabled(false);
        btnAgentProdId.setEnabled(false);
    }//GEN-LAST:event_btnDailyDepSubSaveActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setOperation(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        Amt = observable.getTotalGlAmt();
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
        // Add your handling code here:
        popUp(ACCNO);
        lblTransactionDateDesc1.setText(DateUtil.getStringDate(curDate));
    }//GEN-LAST:event_btnAccNoActionPerformed

    private void cboAgentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAgentTypeActionPerformed
// //   if (cboAgentType.getSelectedIndex() >=1) {
//           if (/*chkLoadFromFile.isSelected() &&*/ !tblClicked) {
//                setDataToTable();
//            } else {
//             //   observable.setCboAgentType((String)cboAgentType.getSelectedItem());
//                HashMap agentMap=new HashMap();
//              // String agentType= ((ComboBoxModel) cboAgentType.getModel()).getKeyForSelected().toString();
//                 System.out.println("agentType ==============="+agentType);
//                agentMap.put("AGENT_ID",agentType);// cboAgentType.getSelectedItem());
//                List lst=ClientUtil.executeQuery("getAgentDetailsName", agentMap);
//                agentMap=null;
//                if(lst!=null && lst.size()>0) {
//                    agentMap=(HashMap)lst.get(0);
//                    lblAgNmVal.setText(CommonUtil.convertObjToStr(agentMap.get("AGENT_NAME")));
//                    if (chkLoadFromFile.isSelected()){
//                        System.out.println("chkLoadFromFile.isSelected() ==="+chkLoadFromFile.isSelected());
//                        setDataToTable();
//                    }
//                } else {
//                    ClientUtil.showAlertWindow("Agent code found...");
//                    return;
//                }
//           }
//            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ||
//            observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT ) {
//            }
    }//GEN-LAST:event_cboAgentTypeActionPerformed

    private void denomination() {
        //        // Add your handling code here:
        HashMap map = new HashMap();
        if (observable.getDenominationList() != null) {
            map.put("DENOMINATION_LIST", observable.getDenominationList());
        }
        //        if (rdoTransactionType_Credit.isSelected()) {
        //            map.put("TRANS_TYPE", "Deposit");
        //            map.put("CURRENCY_TYPE", LocaleConstants.DEFAULT_CURRENCY); //(String) ((ComboBoxModel) cboInputCurrency.getModel()).getKeyForSelected());
        //            map.put("AMOUNT", txtAmount.getText());//txtInputAmt.getText());
        //        }
        DenominationUI dui = new DenominationUI(map);
        ClientUtil.showDialog(dui);
        Double fltDenomination[] = dui.getFltDenomination();
        com.see.truetransact.uicomponent.CTextField txtCount[] = dui.getTxtCount();
        String denominationType[] = dui.getDenominationType();
        String strCnt = "";
        HashMap denominationMap = null;
        ArrayList denominationList = new ArrayList();
        for (int i = 0; i < fltDenomination.length; i++) {
            strCnt = txtCount[i].getText().trim();
            if (strCnt != null && !strCnt.equals("0") && !strCnt.equals("")) {
                denominationMap = new HashMap();
                denominationMap.put("CURRENCY", LocaleConstants.DEFAULT_CURRENCY);
                denominationMap.put("COUNT", new Double(txtCount[i].getText()));
                denominationMap.put("DENOMINATION", fltDenomination[i]);
                denominationMap.put("DENOMINATION_TYPE", denominationType[i]);
                denominationList.add(denominationMap);
            }
        }
        System.out.println("denominationList:" + denominationList);
        observable.setDenominationList(denominationList);

    }

    private void setProdEnable(boolean isEnable) {
        txtAccNo.setEnabled(isEnable);
        btnAccNo.setEnabled(isEnable);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            setEditFieldsEnable(false);
        }
    }

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        observable.setOperation(ClientConstants.ACTIONTYPE_REJECT);
        // Add your handling code here:
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        Amt = observable.getTotalGlAmt();
    }    private void populateInstrumentType() {//GEN-LAST:event_btnRejectActionPerformed
    }

    public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setAuthorizeMap(map);
            observable.doAction();
            btnCancelActionPerformed(null);
        }
    }
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        if (authorizeCheckMap.size() == tblDailyDepositList.getRowCount()) {
            observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
            authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
            Amt = observable.getTotalGlAmt();
        } else {
            ClientUtil.showAlertWindow("All rows have not been verified.");
        }

        //          observable.resetMainPan();
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    public void authorizeStatus(String authorizeStatus) {
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setStatus();
        observable.setScreen(getScreen()); // Rework - KD-2034 by nithya
        if (viewType == AUTHORIZE && isFilled) {
            observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            String remarks = COptionPane.showInputDialog(this, "Authorization Remarks");
            HashMap whereMap = new HashMap();
            //            TxTransferTO transTo=new TxTransferTO();
            whereMap.put("BATCHID", lblTransactionIDDesc1.getText());
            whereMap.put("TRANS_DT", curDate.clone());
            whereMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            arrList = (ArrayList) ClientUtil.executeQuery("getBatchTxTransferTOs", whereMap);
            //            arrList.add(transTo);
            cboAgentType.setSelectedItem(observable.getCboAgentType());
            tdtInstrumentDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtInstrumentDate()));

            singleAuthorizeMap.put("BATCH_ID", lblTransactionIDDesc1.getText());
            singleAuthorizeMap.put("REMARKS", remarks);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(singleAuthorizeMap);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(lblTransactionIDDesc1.getText());
            btnCancel.setEnabled(true);
         } else {
            Amt = 0.0;
            observable.resetMainPan();
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getDailyDepositTransForAuthorize1");
            isFilled = false;
            HashMap whereParam = new HashMap();
            whereParam.put("USER_ID", ProxyParameters.USER_ID);
            whereParam.put("TRANS_DT", curDate);
            whereParam.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereParam);
            viewType = AUTHORIZE;

            AuthorizeWFUI authorizeUI = new AuthorizeWFUI(this, mapParam);
            authorizeUI.show();
            lblStatus.setText(observable.getLblStatus());
            //            setButtonEnableDisable();
        }
        ClientUtil.enableDisable(this, false);  // Disables the panel...
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        chkVerification.setEnabled(true);
  
    }

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        observable.resetOBFields();
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
        private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
            System.out.println("Came in Print Button Click");
            HashMap reportParamMap = new HashMap();
            System.out.println("Screen ID in UI " + getScreenID());
            com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);

    }//GEN-LAST:event_btnPrintActionPerformed
            private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
                // Add your handling code here:
                //        transDetails.setTransDetails(null,null,null);
                //         Amt=0.0;
                //         observable.setTotalGlAmt(Amt);
                setEditFieldsEnable(true);
                chkVerification.setSelected(false);
                observable.setTdtInstrumentDate(DateUtil.getDateMMDDYYYY(""));
                observable.resetForm();                 // Reset the fields in the UI to null...
                observable.resetOBFields();
                observable.resetLable();
                ClientUtil.enableDisable(panDailyDeposit, false);// Reset the Editable Lables in the UI to null...
                ClientUtil.enableDisable(this, false);  // Disables the panel...
                enableDisableButtons(false);
                setModified(false);
                //        disableButtons();                       // To Disable the folder buttons in the UI...
                //        setButtonEnableDisable();               // Enables or Disables the buttons and menu Items depending on their previous state...
                if (observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE) {
                    observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);//Sets the Action Type to be performed...
                }
                observable.setStatus();                 // To set the Value of lblStatus...
                viewType = -1;
                btnDailyDepSubDel.setEnabled(false);
                btnDailyDepSubNew.setEnabled(false);
                btnDailyDepSubSave.setEnabled(false);
                btnSavePan.setEnabled(false);
                lblTotAmtVal.setText("");
                lblAgNmVal.setText("");
                lblExpAgNmVal.setText("");
                tdtInstrumentDate.setDateValue("");
                lblTransactionDateDesc1.setText("");
                lblTransactionIDDesc1.setText("");
                lblTotAmtVal.setText("");
                lblInitiatorIDDesc1.setText("");
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(true);
                btnException.setEnabled(true);
                authorizeCheckMap.clear();
                btnNew.setEnabled(true);
                btnCancel.setEnabled(true);
                btnEdit.setEnabled(true);
                btnDelete.setEnabled(false);
                btnSave.setEnabled(false);
                observable.setOperation(ClientConstants.ACTIONTYPE_CANCEL);
                observable.setTable();
                tblDailyDepositList.setModel(observable.getTbmTransfer());
                chkLoadFromFile.setVisible(false);
                chkLoadFromFile.setSelected(false);
                chkLoadFromFileActionPerformed(null);
                chkExportToFile.setVisible(true);
                chkExportToFile.setSelected(false);
                chkExportToFile.setEnabled(true);
                //chkExportToFileActionPerformed(null);
                totCustomerList = null;
                agentMap = null;
                tblClicked = false;
                txtExportAgentId.setText("");
                txtExportFileName.setText("");
                lblTotInstVal.setText("");
                txtAgentProdId.setText("");
                lblAccHdId.setText("");
                txtProdType.setText("");
                btnAgentProdId.setEnabled(false);
                HashMap datMap = new HashMap();
                datMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
                ClientUtil.execute("TruncateTempDailyKor", datMap);
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                txtProdType.setEnabled(false);
                txtAgentProdId.setEnabled(false);
                Amt=0;
    }//GEN-LAST:event_btnCancelActionPerformed
                private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
                    btnSave.setEnabled(false);
                    observable.setTotalGlAmt(CommonUtil.convertObjToDouble(lblTotAmtVal.getText()).doubleValue());
                    System.out.println("observable.getTotalGlAmt" + observable.getTotalGlAmt());
                    if (tblDailyDepositList.getRowCount() > 0) {
                        updateOBFields();
                        StringBuffer mandatoryMessage = new StringBuffer();
                        mandatoryMessage.append(new MandatoryCheck().checkMandatory(getClass().getName(), panCashTransaction));
                        if (chkLoadFromFile.isSelected() == true && validateAgentTransDetails()) {
                            return;
                        }
                         CommonUtil comm = new CommonUtil();
                         final JDialog loading = comm.addProgressBar();
                         SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                             @Override
                            protected Void doInBackground() throws InterruptedException 
                            {
                                observable.doAction();
                                return null;
                            }

                            @Override
                            protected void done() {
                                loading.dispose();
                            }
                        };
                        worker.execute();
                        loading.show();
                        try {
                            worker.get();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }                        
                        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                            HashMap lockMap = new HashMap();
                            ArrayList lst = new ArrayList();
                            lst.add("BATCH_ID");//TRANS_ID
                            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                            if (observable.getProxyReturnMap() != null) {
                                if (observable.getProxyReturnMap().containsKey("TRANS_ID")) {
                                    lockMap.put("BATCH_ID", observable.getProxyReturnMap().get("TRANS_ID"));
                                    int yesNo = 0;
                                    String[] options = {"Yes", "No"};
                                    yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                            null, options, options[0]);
                                    System.out.println("#$#$$ yesNo : " + yesNo);
                                    if (yesNo == 0) {
                                        TTIntegration ttIntgration = null;
                                        lockMap.put("TransId", observable.getProxyReturnMap().get("TRANS_ID"));
                                        lockMap.put("TransDt", curDate);
                                        lockMap.put("BranchId", ProxyParameters.BRANCH_ID);
                                        ttIntgration.setParam(lockMap);
//                                        if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
//                                            ttIntgration.integrationForPrint("ReceiptPayment");
//                                        } else {
                                        ttIntgration.integrationForPrint("DDSReceipts", false);
//                                        }
                                    }
                                }
                            
                                if (observable.getProxyReturnMap().containsKey("CASH_TRANS_ID")) {
                                    HashMap dMap=new HashMap();
                                    dMap.put("BATCH_ID",observable.getProxyReturnMap().get("CASH_TRANS_ID"));
                                    dMap.put("TRANS_DT",curDate);
                                    List singleId=ClientUtil.executeQuery("getSingleIdCash", dMap);
                                    String singleTransId="";
                                    if(singleId!=null && singleId.size()>0){
                                        dMap=(HashMap)singleId.get(0);
                                        singleTransId=CommonUtil.convertObjToStr(dMap.get("SINGLE_TRANS_ID"));
                                    }
                                    lockMap.put("BATCH_ID", observable.getProxyReturnMap().get("CASH_TRANS_ID"));
                                    int yesNo = 0;
                                    String[] options = {"Yes", "No"};
                                    yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                            null, options, options[0]);
                                    System.out.println("#$#$$ yesNo : " + yesNo);
                                    if (yesNo == 0) {
                                        TTIntegration ttIntgration = null;
                                        lockMap.put("TransId", singleTransId);
                                        lockMap.put("TransDt", curDate);
                                        lockMap.put("BranchId", ProxyParameters.BRANCH_ID);
                                        ttIntgration.setParam(lockMap);
                                        ttIntgration.integrationForPrint("CashReceipt",false);
                                    }
                                }
                                         //end
                            }
                             HashMap whereMap=new HashMap();
                            whereMap.put("AGENT_ID", observable.getCboAgentType());
                            whereMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                            List ClosedList = ClientUtil.executeQuery("getCollectedClosedAccount", whereMap);
                            String message = "Closed Accounts Are:";
                            if (ClosedList.size() > 0) {
                                for (int i = 0; i < ClosedList.size(); i++) {
                                    HashMap result = (HashMap) ClosedList.get(i);
                                    message = message + "," + CommonUtil.convertObjToStr(result.get("DEPOSIT_NO"));
                                }
                                ClientUtil.showMessageWindow(message);
                            }
                            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                                lockMap.put("BATCH_ID", lblTransactionIDDesc1.getText());
                            }
                            observable.setTotalGlAmt(CommonUtil.convertObjToDouble(lblTotAmtVal.getText()).doubleValue());
                            setEditLockMap(lockMap);
                            setEditLock();
                            // To perform the necessary operation depending on the Action type...
                            observable.resetForm();             // Reset the fields in the UI to null...
                            observable.resetLable();            // Reset the Editable Lables in the UI to null...
                            //        observable.resetMainPan();
                            //        setEditFieldsEnable(false);
                            //        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE)
                            //            enableDisableButtons(false);        // To disable the buttons(folder) in the UI...
                            ClientUtil.enableDisable(this, false);// Disables the panel...
                            //        setButtonEnableDisable();           // Enables or Disables the buttons and menu Items depending on their previous state...
                            observable.setResultStatus();// To Reset the Value of lblStatus...
                            enableDisableButtons(false);
                            setModified(false);
                            observable.resetOBFields();
                            //        this.disableButtons();
                            viewType = -1;
                            lblTransactionDateDesc1.setText("");
                            lblTransactionIDDesc1.setText("");
                            lblTotAmtVal.setText("");
                            tdtInstrumentDate.setDateValue("");
                            lblAgNmVal.setText("");
                            lblExpAgNmVal.setText("");
                            btnCancelActionPerformed(null);
                        }
                        observable.setResultStatus();// To Reset the Value of lblStatus...
                    } else {
                        ClientUtil.showAlertWindow("Atleast one record should be save");
                        return;
                    }
    }//GEN-LAST:event_btnSaveActionPerformed

    private boolean validateAgentTransDetails() {
        int masterTransCount = CommonUtil.convertObjToInt(agentMap.get("NO_TRANS"));
        double masterTransAmt = CommonUtil.convertObjToDouble(agentMap.get("TOT_AMT")).doubleValue();
        int count = 0;
        double transAmt = 0;
        System.out.println("masterTransAmt:" + masterTransAmt);
        System.out.println("totCustomerList:" + totCustomerList + " vv---" + totCustomerList.size());
        if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("CHELLANOOR")) {
            if (totCustomerList != null && totCustomerList.size() > 0) {
                for (int i = 0; i < totCustomerList.size(); i++) {
                    ArrayList singleList = (ArrayList) totCustomerList.get(i);
                    count++;    //=CommonUtil.convertObjToInt(singleList.get(1));
                    transAmt += CommonUtil.convertObjToDouble(singleList.get(1)).doubleValue();

                }
            }
        } else {
            if (totCustomerList != null && totCustomerList.size() > 0) {
                for (int i = 0; i < totCustomerList.size(); i++) {
                    ArrayList singleList = (ArrayList) totCustomerList.get(i);
                    count++;    //=CommonUtil.convertObjToInt(singleList.get(1));
                    transAmt += CommonUtil.convertObjToDouble(singleList.get(3)).doubleValue();

                }
            }
            System.out.println("transAmt:" + transAmt);
            System.out.println("masterTransCount:" + masterTransCount);
            System.out.println("count:" + count);
            if (CommonConstants.VENDOR != null && !CommonConstants.VENDOR.equals("KOORKKENCHERRY")) {
                if (masterTransCount != count) {
                    ClientUtil.showMessageWindow("Agent Transaction Count Got Mismatch in UpLoad File");
                    return true;
                }
            }
            if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("POLPULLY")) {
                if (masterTransAmt != transAmt) {
                    ClientUtil.showMessageWindow("Agent Transaction Amount Got Mismatch in UpLoad File");
                    return true;
                }
            }
        }
        return false;
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    //    private void populateUIData(int operation){
    //        if(this.batchIdForEdit!=null) {
    //         observable.setOperation(operation);
    //            observable.setBatchId(batchIdForEdit);
    //            rowForEdit = observable.getData(transactionIdForEdit);
    //            this._intTransferNew=false;
    // update the account information also
    //            if(transactionIdForEdit!=null){
    //                updateAccountInfo();
    //
    //            /* call the productID selection combo box action handler to update
    //             * the corresponding values
    //             */
    ////                cboProductIDActionPerformed(null);
    //
    //            }
    //            updateDailyInfo();
    //            setupMenuToolBarPanel();
    // Enable all the screen
    //             if(operation==ClientConstants.ACTIONTYPE_EDIT)  {
    //                setModified(true);
    //                ClientUtil.enableDisable(this, true);
    //                this.enableDisableButtons(true);
    //                ClientUtil.enableDisable(this.panData,false);
    //                this.btnAccountNo.setEnabled(false);
    //                this.btnAccountHead.setEnabled(false);
    //             }
    //             else {
    //                 ClientUtil.enableDisable(this, false);
    //                 this.enableDisableButtons(false);
    //             }
    //            if(((String)((ComboBoxModel)
    //                this.cboInstrumentType.getModel()).getKeyForSelected())
    //                            .equalsIgnoreCase("ONLINE_TRANSFER")){
    //
    //                 this.tdtInstrumentDate.setEnabled(false);
    //                 this.txtInstrumentNo1.setEnabled(false);
    //                 this.txtInstrumentNo2.setEnabled(false);
    //                 this.cboInstrumentType.setEnabled(false);
    //            }
    //            if(transactionIdForEdit==null && operation==ClientConstants.ACTIONTYPE_EDIT) {
    //                this.enableDisableButtons(false);
    //                this.btnAddCredit.setEnabled(true);
    //                this.btnAddDebit.setEnabled(true);
    //                ClientUtil.enableDisable(this, false);
    //            }
    //            observable.setStatus();
    //            lblStatusValue.setText(observable.getLblStatus());
    //        }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        //        // Add your handling code here:
        observable.setOperation(ClientConstants.ACTIONTYPE_DELETE);

        if (ClientUtil.deleteAlert() == 0) {
            observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);//Sets the Action Type to be performed...
            btnSaveActionPerformed(evt);
        }
        //        observable.resetForm();                 // Reset the fields in the UI to null...
        //        observable.resetLable();                // Reset the Editable Lables in the UI to null...
        //        popUp(DELETE);
        //        Amt=observable.getTotalGlAmt();
    }//GEN-LAST:event_btnDeleteActionPerformed
        private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
            //        Add your handling code here:
            observable.setOperation(ClientConstants.ACTIONTYPE_EDIT);
            //          observable.resetMainPan();
            //        observable.resetForm();                 // Reset the fields in the UI to null...
            //        observable.resetLable();                // Reset the Editable Lables in the UI to null...
            observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);//Sets the Action Type to be performed...
            popUp(EDIT);

            Amt = observable.getTotalGlAmt();
            System.out.println("Amt##################" + Amt);
            btnDailyDepSubDel.setEnabled(true);
            btnDailyDepSubNew.setEnabled(true);
            btnDailyDepSubSave.setEnabled(true);
            ClientUtil.enableDisable(panData, true);
            cboAgentType.setEnabled(false);
            tdtInstrumentDate.setEnabled(false);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(true);
            btnDelete.setEnabled(true);
            btnNew.setEnabled(false);
            btnEdit.setEnabled(false);
            //        rowForEdit = observable.getData(transactionIdForEdit);

            //        this.populateUIData(ClientConstants.ACTIONTYPE_EDIT);
            observable.setTransStatus(CommonConstants.STATUS_MODIFIED);
            System.out.println("observable.getTdtInstrumentDate()))" + observable.getTdtInstrumentDate());
            tdtInstrumentDate.setDateValue(DateUtil.getStringDate(observable.getTdtInstrumentDate()));
    }//GEN-LAST:event_btnEditActionPerformed
    // To display the All the Product Id's which r having status as
    // created or updated, in a table...

    private void popUp(int field) {
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        viewType = field;
        if (field == EDIT || field == DELETE) {      //Edit=0 and Delete=1
            HashMap resultMap = new HashMap();
            HashMap where = new HashMap();
            ArrayList lst = new ArrayList();
            lst.add("BATCH_ID");//trans_id
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            //            resultMap.put("DEPOSIT_NO", ((ComboBoxModel)cboAgentType.getModel()).getKeyForSelected().toString());
            resultMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_NAME, "getDepositDetailsForEdit");//mapped statement: viewCashTransaction---> result map should be a Hashmap...
            viewMap.put(CommonConstants.MAP_WHERE, resultMap);
        } else if (field == EXPORT_AGENT_ID) {
            HashMap resultMap = new HashMap();
            HashMap where = new HashMap();
            resultMap.put("BRANCHID", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_NAME, "viewAgentData");//mapped statement: viewCashTransaction---> result map should be a Hashmap...
            viewMap.put(CommonConstants.MAP_WHERE, resultMap);
        } 
        else if (field == AGENT_PROD_ID) {
            HashMap resultMap = new HashMap();
            HashMap where = new HashMap();
            resultMap.put("DAILY", "DAILY");
            viewMap.put(CommonConstants.MAP_NAME, "getProductDescription");//mapped statement: viewCashTransaction---> result map should be a Hashmap...
            viewMap.put(CommonConstants.MAP_WHERE, resultMap);
        } else {
            Date collctDt = DateUtil.getDateMMDDYYYY(tdtInstrumentDate.getDateValue());
            if (collctDt == null) {
                ClientUtil.showAlertWindow("Collection Date Should not be empty...Please cancel again you start freshly");
                return;
            } else {
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    observable.resetMainPan();
                }
                updateOBFields();
                HashMap resultMap = new HashMap();
                HashMap where = new HashMap();
                resultMap.put("AGENT_ID", ((ComboBoxModel) cboAgentType.getModel()).getKeyForSelected().toString());
                resultMap.put("AGENT_ID", observable.getCboAgentType());
                resultMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                resultMap.put("PROD_ID", txtAgentProdId.getText());
                System.out.println("######AgentDetails :" + resultMap);
                viewMap.put(CommonConstants.MAP_NAME, "getDepositDetailsForDaily");
                viewMap.put(CommonConstants.MAP_WHERE, resultMap);
            }
        }
        new ViewAll(this, viewMap).show();
    }

    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        isFilled = true;
        final HashMap hash = (HashMap) param;
        if (viewType == EDIT || viewType == DELETE || viewType == AUTHORIZE) {

            setModified(true);

            hash.put("WHERE", hash.get("BATCH_ID"));
            log.info("Hash: " + hash);
            System.out.println("hash@@@@@@" + hash);
            agent_code = CommonUtil.convertObjToStr(hash.get("Machine ID"));
            observable.populateData(hash);// Called to display the Data in the UI fields...
            Amt = CommonUtil.convertObjToDouble(hash.get("AMOUNT")).doubleValue();
            if (viewType == EDIT) {
                int yesNo = 0;
                String[] options = {"Yes", "No"};
                yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                System.out.println("#$#$$ yesNo : " + yesNo);
                if (yesNo == 0) {
                    TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    paramMap.put("TransId", hash.get("BATCH_ID"));
                    paramMap.put("TransDt", curDate);
                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                    ttIntgration.setParam(paramMap);
                    //                        if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
                    //                            ttIntgration.integrationForPrint("ReceiptPayment");
                    //                        } else {
                    ttIntgration.integrationForPrint("DDSReceipts", false);
                    //                        }
                }
            }
            //            // To set the Value of Transaction Id...
            //            final String TRANSID = (String) hash.get("TRANS_ID");
            //            final String ACCOUNTNO = (String) hash.get("AGENT_ID");
            //
            //To Set the Value of Account holder Name and the Balances in UI...
            //            if(!ACCOUNTNO.equals("")){
            //                observable.setTransId(TRANSID);
            //                setButtonEnableDisable();// Enables or Disables the buttons and menu Items depending on their previous state...
            //            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType == AUTHORIZE) {
                ClientUtil.enableDisable(this, false);// Disables the panel...
                chkVerification.setEnabled(true);
                btnAgentProdId.setEnabled(false);
            } else {
                ClientUtil.enableDisable(this, true);// Enables the panel...
                enableDisableButtons(true);         // To Enable the Buttons(folder) buttons in UI...
                //                textDisable();                  // To set the Text Boxes for Account No. and InitiatorChannel, nonEditable...
                this.btnDelete.setEnabled(true);
                btnAgentProdId.setEnabled(true);
                //                setButtonEnableDisable();
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
                btnAgentProdId.setEnabled(false);
            }
            setEditFieldsEnable(false);
            observable.setStatus();             // To set the Value of lblStatus...
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType == AUTHORIZE) {
                ClientUtil.enableDisable(this, false);// Disables the panel...
                enableDisableButtons(false);
            }
            if (viewType == AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getOperation() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
            btnCancel.setEnabled(true);
        } else if (viewType == ACCNO) {
            //                String ACCOUNTNO = CommonUtil.convertObjToStr(hash.get("ACT_NUM"));
            System.out.println("hash ================== " + hash);
            String ACCOUNTNO = CommonUtil.convertObjToStr(hash.get("ACCOUNT_NUMBER"));
            //                observable.setLblAccName(CommonUtil.convertObjToStr(hash.get("CUSTOMER_NAME")));
            try {
                if(hash!=null && hash.get("TYPE")!=null && !hash.get("TYPE").equals("")){
                    String prodType = CommonUtil.convertObjToStr(hash.get("TYPE"));
                    observable.setAccountName(ACCOUNTNO, prodType);
                }else{
                    observable.setAccountName(ACCOUNTNO, "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //                observable.setTxtAccNo(ACCOUNTNO);
            //                observable.setBalance(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
            System.out.println("#######AgentDetailsForDeposits : " + hash);
            //                lblBalance.setText(observable.getBalance());
            observable.ttNotifyObservers();
            //                lblCustnmVal.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER_NAME")));
            lblBalanceVal.setText(CommonUtil.convertObjToStr(hash.get("BALANCE")));
            txtAmount.setText(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
                // this query added by shihad for 10411 on 25.02.2015
            HashMap instParamMap = new HashMap();
            instParamMap.put("DEPOSIT_NO", observable.getTxtAccNo());
            instParamMap.put("TRANS_TYPE", CommonConstants.CREDIT);
            List installNo = ClientUtil.executeQuery("getTotalNoOfTransactionCredit", instParamMap);
            if (installNo != null && installNo.size() > 0) {
                HashMap instMap = new HashMap();
                instMap = (HashMap) installNo.get(0);
                System.out.println("val her" + CommonUtil.convertObjToStr(instMap.get("COUNT")));
                lblTotInstVal.setText(CommonUtil.convertObjToStr(instMap.get("COUNT")));
            }
            if(hash!=null && hash.get("TYPE")!=null && !hash.get("TYPE").equals("") && hash.get("TYPE").equals("DAILY") ){
                txtProdType.setText("TD");   
            }else if(hash!=null && hash.get("TYPE")!=null && !hash.get("TYPE").equals("") && hash.get("TYPE").equals("RECURRING") ){
                txtProdType.setText("TD");   
            }else if(hash!=null && hash.get("TYPE")!=null && !hash.get("TYPE").equals("") && hash.get("TYPE").equals("OA") ){
                txtProdType.setText("OA");   
            }else if(hash!=null && hash.get("TYPE")!=null && !hash.get("TYPE").equals("") && hash.get("TYPE").equals("SA") ){
                txtProdType.setText("SA");   
            }
         } else if (viewType == EXPORT_AGENT_ID) {
            System.out.println("gggggg=====" + hash);
            if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("POLPULLY")) {
                txtExportAgentId.setText(CommonUtil.convertObjToStr(hash.get("MACHINE ID")));
            } else {
                txtExportAgentId.setText(CommonUtil.convertObjToStr(hash.get("AGENT ID")));
            }
            lblExpAgNmVal.setText(CommonUtil.convertObjToStr(hash.get("AGENT NAME")));
        }
        else if (viewType == AGENT_PROD_ID) {
            txtAgentProdId.setText(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
            lblAccHd.setText("Prod Desc");
            lblAccHdId.setText(CommonUtil.convertObjToStr(hash.get("PROD_DESC")));
            lblProd_type.setText("Prod Type");
            //txtProdType.setText(CommonUtil.convertObjToStr(hash.get("PROD_TYPE"))); 
            if(hash!=null && hash.get("PROD_TYPE")!=null && !hash.get("PROD_TYPE").equals("") && hash.get("PROD_TYPE").equals("TD") ){
                txtProdType.setText("TD");   
            }else if(hash!=null && hash.get("PROD_TYPE")!=null && !hash.get("PROD_TYPE").equals("") && hash.get("PROD_TYPE").equals("OA") ){
                txtProdType.setText("OA");   
            }else if(hash!=null && hash.get("PROD_TYPE")!=null && !hash.get("PROD_TYPE").equals("") && hash.get("PROD_TYPE").equals("SA") ){
                txtProdType.setText("SA");   
            }
        }
    }

    //To enable and/or Disable buttons(folder)...
    private void enableDisableButtons(boolean yesno) {
        btnAccNo.setEnabled(yesno);
        txtProdType.setEnabled(false);
        txtAgentProdId.setEnabled(yesno);
        //        btnDenomination.setEnabled(yesno);
        //        txtInitiatorChannel.setEnabled(false);
    }

    private void newSaveEnable() {
        btnDailyDepSubNew.setEnabled(true);
        //        btnDailyDepSubSave.setEnabled(true);
        //        btnDailyDepSubDel.setEnabled(true);
        ClientUtil.enableDisable(panData, false);
        //        btnDenomination.setEnabled(false);
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.resetForm();                 // Reset the fields in the UI to null...
        ClientUtil.enableDisable(this, true);   // Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);//Sets the Action Type to be performed...
        observable.setOperation(ClientConstants.ACTIONTYPE_NEW);
        observable.setTransStatus(CommonConstants.STATUS_CREATED);
        //        setButtonEnableDisable();               // Enables or Disables the buttons and menu Items depending on their previous state...
        observable.setStatus();
        newSaveEnable();// To set the Value of lblStatus...
        //        textDisable();
        setModified(true);
        cboAgentType.setEnabled(true);
        btnAgentProdId.setEnabled(true);
        btnDelete.setEnabled(false);
        btnEdit.setEnabled(false);
        btnSave.setEnabled(true);
        btnNew.setEnabled(false);
        btnCancel.setEnabled(true);
        tdtInstrumentDate.setEnabled(true);
        lblTransactionDateDesc1.setText(DateUtil.getStringDate(curDate));
        lblInitiatorIDDesc1.setText("");
        lblTransactionIDDesc1.setText("");
        lblTotAmtVal.setText("");
        chkLoadFromFile.setVisible(true);
        chkLoadFromFile.setEnabled(true);
        exportPanVisibleStatus(false);
        chkExportToFile.setVisible(false);
        //Added By Suresh
        observable.setAgentType();
        cboAgentType.setModel(observable.getCbmAgentType());
    }//GEN-LAST:event_btnNewActionPerformed
    // To set the Text Boxes for Account No. and InitiatorChannel, nonEditable...

    private void textEnable() {
        txtAccNo.setEnabled(true);             //To make this textBox non editable...
        txtAmount.setEnabled(true);
    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
        private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
            btnCancelActionPerformed(evt);       // Add your handling code here:

    }//GEN-LAST:event_mitCancelActionPerformed
            private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
                btnSaveActionPerformed(evt);        // Add your handling code here:

    }//GEN-LAST:event_mitSaveActionPerformed
                private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
                    btnDeleteActionPerformed(evt);        // Add your handling code here:

    }//GEN-LAST:event_mitDeleteActionPerformed
                    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
                        btnEditActionPerformed(evt);        // Add your handling code here:

    }//GEN-LAST:event_mitEditActionPerformed
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {
	//GEN-FIRST:event_mitNewActionPerformed
                        btnNewActionPerformed(evt);        // Add your handling code here:

    }//GEN-LAST:event_mitNewActionPerformed
                    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
                        System.exit(0);
                        this.dispose();
    }//GEN-LAST:event_exitForm

private void txtAmount1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmount1FocusLost
// TODO add your handling code here:
}//GEN-LAST:event_txtAmount1FocusLost

private void cboAgentTypeFocusLost()
{
    //   if (cboAgentType.getSelectedIndex() >=1) {
    if (/*
             * chkLoadFromFile.isSelected() &&  
             */!tblClicked) {
        if (CommonConstants.VENDOR != null && !CommonConstants.VENDOR.equals("KOORKKENCHERRY")) {
            observable.setCboAgentType(cboAgentType.getSelectedItem().toString());
        }
        HashMap agentMap = new HashMap();
        if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("KOORKKENCHERRY")) {
            HashMap agMap = new HashMap();
            agMap.put("AGENT_MACHINE_ID", cboAgentType.getSelectedItem().toString());
            List agList = ClientUtil.executeQuery("getCustIdAgent", agMap);
            String custId = "";
            if (agList != null && agList.size() > 0) {
                agMap = (HashMap) agList.get(0);
                custId = CommonUtil.convertObjToStr(agMap.get("AGENT_ID"));
            }
            if(custId!=null && !custId.equals("")){
              agentMap.put("AGENT_ID", custId);// cboAgentType.getSelectedItem()); 
            }
            else{
                agentMap.put("AGENT_ID", cboAgentType.getSelectedItem());
            }
        } else {
            String agentType = ((ComboBoxModel) cboAgentType.getModel()).getSelectedItem().toString();
            agentMap.put("AGENT_ID", agentType);// cboAgentType.getSelectedItem());
        }
        List lst = ClientUtil.executeQuery("getAgentDetailsName", agentMap);
        agentMap = null;
        if (lst != null && lst.size() > 0) {
            agentMap = (HashMap) lst.get(0);
            lblAgNmVal.setText(CommonUtil.convertObjToStr(agentMap.get("AGENT_NAME")));
            if (chkLoadFromFile.isSelected()) {
                System.out.println("chkLoadFromFile.isSelected() ===" + chkLoadFromFile.isSelected());
                setDataToTable();
                observable.setFlagSave("IMPORT");

            }
        } else {
            ClientUtil.showAlertWindow("Agent code found...");
            return;
        }
    }
    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
            || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
    }
    cboAgentType.setEnabled(false);
    cboAgentType.removeFocusListener(null);


}
private void cboAgentTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboAgentTypeFocusLost

    CommonUtil comm = new CommonUtil();
    final JDialog loading = comm.addProgressBar();
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

        @Override
        protected Void doInBackground() throws InterruptedException 
        {
            cboAgentTypeFocusLost();
            return null;
        }

        @Override
        protected void done() {
            loading.dispose();
        }
    };
    worker.execute();
    loading.show();
    try {
        worker.get();
    } catch (Exception e1) {
        e1.printStackTrace();
    }
}//GEN-LAST:event_cboAgentTypeFocusLost
private void chkVerificationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkVerificationActionPerformed
    tblClicked = true;
    if (observable.getOperation() != ClientConstants.ACTIONTYPE_DELETE
            && observable.getOperation() != ClientConstants.ACTIONTYPE_AUTHORIZE
            && observable.getOperation() != ClientConstants.ACTIONTYPE_EXCEPTION
            && observable.getOperation() != ClientConstants.ACTIONTYPE_REJECT) {
    } else {
        ClientUtil.enableDisable(this, false);  // Disables the panel...
    }
    int rowcount = tblDailyDepositList.getRowCount();


    System.out.println("ROWCOUNT......123" + rowcount);
    if (observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT) {
        observable.populatALlTranferTO(rowcount);
        for (int i = 0; i < rowcount; i++) {
            authorizeCheckMap.put(String.valueOf(i), String.valueOf(i));
        }

    }
}//GEN-LAST:event_chkVerificationActionPerformed
private void uploadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadButtonActionPerformed
    try {
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("C:/PALMTECHFILES/transfer 1 2  master.dat");
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println(e.getMessage());
    }
}//GEN-LAST:event_uploadButtonActionPerformed
private void downloadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadButtonActionPerformed
    try {
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("C:/PALMTECHFILES/transfer.exe 1 1 master.trn");
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println(e.getMessage());
    }
}//GEN-LAST:event_downloadButtonActionPerformed

private void btnAgentProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgentProdIdActionPerformed
    // TODO add your handling code here:
     popUp(AGENT_PROD_ID);
}//GEN-LAST:event_btnAgentProdIdActionPerformed

private void cboAgentTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboAgentTypeItemStateChanged
    // TODO add your handling code here:
    if(!chkLoadFromFile.isSelected()){
        HashMap agentMap=new HashMap();
        //String agentType= ((ComboBoxModel) cboAgentType.getModel()).getKeyForSelected().toString();
        agentMap.put("AGENT_ID", cboAgentType.getSelectedItem());
        List lst=ClientUtil.executeQuery("getAgentDetailsName", agentMap);
        agentMap=null;
        if(lst!=null && lst.size()>0) {
           agentMap=(HashMap)lst.get(0);
           lblAgNmVal.setText(CommonUtil.convertObjToStr(agentMap.get("AGENT_NAME")));
       }
    }
}//GEN-LAST:event_cboAgentTypeItemStateChanged

// Function added by shihad for mantis 10411 on 25.02.2015     /* @param args the command line arguments


    public static void main(String args[]) throws Exception {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }

        javax.swing.JFrame jf = new javax.swing.JFrame();
        DailyDepositTransUI gui = new DailyDepositTransUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();

    }

    private boolean checkAccNum() {
        //        System.out.println("tblDailyDepositList.getValueAt(i,0)))"+tblDailyDepositList.getValueAt(0,0));
        if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("KOORKKENCHERRY")) {
        if(tblDailyDepositList.getRowCount()>0){
            for(int i=0;i<tblDailyDepositList.getRowCount();i++){
                if(txtAccNo.getText().equals(CommonUtil.convertObjToStr(tblDailyDepositList.getValueAt(i,0)))){
                    System.out.println("tblDailyDepositList.getValueAt(i,0)))-----"+tblDailyDepositList.getValueAt(i,0));
                   // ClientUtil.showMessageWindow("Account Number Already Exists!!!");
                    return true;
                  }
        
               }
           }
        }
        return false;
    }

    private double totalAmt() {
        double totAmt = 0.0;
        if (tblDailyDepositList.getRowCount() > 0) {
            for (int i = 0; i < tblDailyDepositList.getRowCount(); i++) {
                totAmt = totAmt + CommonUtil.convertObjToDouble(tblDailyDepositList.getValueAt(i, 1)).doubleValue();
                System.out.println("totAmt$$$$$$$$$$$$$$$" + totAmt);

            }
        }
        return totAmt;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnAgentProdId;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBrowse;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnCashPayRec;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDailyDepSubDel;
    private com.see.truetransact.uicomponent.CButton btnDailyDepSubNew;
    private com.see.truetransact.uicomponent.CButton btnDailyDepSubSave;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnExportAgent;
    private com.see.truetransact.uicomponent.CButton btnExportFileBrowse;
    private com.see.truetransact.uicomponent.CButton btnExportSave;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CPanel btnSavePan;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CComboBox cboAgentType;
    private com.see.truetransact.uicomponent.CCheckBox chkExportToFile;
    private com.see.truetransact.uicomponent.CCheckBox chkLoadFromFile;
    private com.see.truetransact.uicomponent.CCheckBox chkVerification;
    private com.see.truetransact.uicomponent.CButton downloadButton;
    private com.see.truetransact.uicomponent.CLabel lblAccHd;
    private com.see.truetransact.uicomponent.CLabel lblAccHdId;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblAgNm;
    private com.see.truetransact.uicomponent.CLabel lblAgNmVal;
    private com.see.truetransact.uicomponent.CLabel lblAgentType;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblBalanceAmount;
    private com.see.truetransact.uicomponent.CLabel lblBalanceVal;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblCustNameVal;
    private com.see.truetransact.uicomponent.CLabel lblCustnmVal;
    private com.see.truetransact.uicomponent.CLabel lblExpAgNmVal;
    private com.see.truetransact.uicomponent.CLabel lblExportFileName;
    private com.see.truetransact.uicomponent.CLabel lblExportagentId;
    private com.see.truetransact.uicomponent.CLabel lblFileName;
    private com.see.truetransact.uicomponent.CLabel lblInitiatorID1;
    private com.see.truetransact.uicomponent.CLabel lblInitiatorIDDesc1;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProd_type;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotAmtVal;
    private javax.swing.JLabel lblTotInst;
    private javax.swing.JLabel lblTotInstVal;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmt;
    private com.see.truetransact.uicomponent.CLabel lblTransactionDate1;
    private com.see.truetransact.uicomponent.CLabel lblTransactionDateDesc1;
    private com.see.truetransact.uicomponent.CLabel lblTransactionID1;
    private com.see.truetransact.uicomponent.CLabel lblTransactionIDDesc1;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAcctNo;
    private com.see.truetransact.uicomponent.CPanel panAmt;
    private com.see.truetransact.uicomponent.CPanel panCashTransaction;
    private com.see.truetransact.uicomponent.CPanel panDailyDeposit;
    private com.see.truetransact.uicomponent.CPanel panData;
    private com.see.truetransact.uicomponent.CPanel panExportToFile;
    private com.see.truetransact.uicomponent.CPanel panLables;
    private com.see.truetransact.uicomponent.CPanel panLoadFromFile;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransaction1;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransactionType;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srpDailyDepositDetails;
    private com.see.truetransact.uicomponent.CTable tblDailyDepositList;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CDateField tdtInstrumentDate;
    private com.see.truetransact.uicomponent.CTextField txtAccNo;
    private com.see.truetransact.uicomponent.CTextField txtAgentProdId;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtAmount1;
    private com.see.truetransact.uicomponent.CTextField txtExportAgentId;
    private com.see.truetransact.uicomponent.CTextField txtExportFileName;
    private com.see.truetransact.uicomponent.CTextField txtFileName;
    private com.see.truetransact.uicomponent.CTextField txtProdType;
    private com.see.truetransact.uicomponent.CButton uploadButton;
    // End of variables declaration//GEN-END:variables
}
