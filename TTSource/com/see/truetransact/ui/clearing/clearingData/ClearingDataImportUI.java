/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ClearingDataImportUI.java
 *
 * Created on February 25, 2004, 11:55 AM
 */

package com.see.truetransact.ui.clearing.clearingData;

import com.see.truetransact.uicomponent.CInternalFrame;
//import com.see.truetransact.uimandatory.UIMandatoryField;
//import com.see.truetransact.uimandatory.MandatoryCheck;
//import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
//import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizewf.AuthorizeWFUI;
//import com.see.truetransact.ui.common.authorizewf.AuthorizeWFCheckUI;
//import com.see.truetransact.uivalidation.NumericValidation;
//import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.denomination.DenominationUI;
//import com.see.truetransact.transferobject.agent.AgentTO;
//import com.see.truetransact.uicomponent.CFrame;
import com.see.truetransact.commonutil.LocaleConstants;

import com.see.truetransact.uicomponent.CButtonGroup;// To add and Remove the Radio Buttons...
//import com.see.truetransact.ui.common.viewall.TableDialogUI;
//import com.see.truetransact.ui.common.viewphotosign.ViewPhotoSignUI;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;

import com.see.truetransact.clientproxy.ProxyParameters;
//import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;

import com.see.truetransact.ui.TrueTransactMain;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.HashMap;
//import java.util.List;
import java.util.ArrayList;
//import java.util.Observer;
import java.util.Observable;
//import java.util.Date;
import org.apache.log4j.Logger;
//import java.awt.*;
import javax.swing.JFileChooser;
import java.io.*;



/**
 *
 * @author  rahul, bala
 * @todoh Add other modules into the transaction
 *
 */
public class ClearingDataImportUI extends CInternalFrame implements java.util.Observer {
    
//    private int ok = 0;
//    private int yes = 0;
//    private int no = 1;
//    private int cancel = 2;
//    private Date collDT;
    private boolean _intTransferNew;
    private int rowForEdit;    
//    private String transactionIdForEdit;
    private double Amt=0.0;
    Date curDate = null;
    
    private HashMap mandatoryMap;
    ClearingDataImportOB observable;
    final int EDIT=0, DELETE=1, ACCNO=2, AUTHORIZE=3, ACCTHDID = 4;
    int viewType=-1;
    boolean isFilled = false;
    private TransDetailsUI transDetails = null;
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    private File selFile;

    private final static Logger log = Logger.getLogger(ClearingDataImportUI.class);     //Logger
    
    /** Creates new form CashTransaction */
    public ClearingDataImportUI() {
        try{
            curDate = ClientUtil.getCurrentDate();
            initComponents();
            initSetup();
            transDetails = new TransDetailsUI(panInfoClearingDataTransTable);
            btnSourceFile.setEnabled(false);
            btnDestinationFile.setEnabled(false);
        }catch(Exception e){
            e.printStackTrace();
        } 
    }
    
    private void initSetup()throws Exception{
        setFieldNames();
        internationalize();
        setObservable();
        initComponentData();                    // Fill all the combo boxes...
        setMaxLenths();                         // To set the Numeric Validation and the Maximum length of the Text fields...
//     new MandatoryCheck().putMandatoryMarks(getClass().getName(), panCashTransaction);       
        ClientUtil.enableDisable(this, false);  // Disables all when the screen appears for the 1st time
        setButtonEnableDisable();               // Enables/Disables the necessary buttons and menu items...
       
//        observable.resetForm();                 // To reset all the fields in UI...
//        observable.resetLable();                // To reset all the Lables in UI...
//        observable.resetStatus();               // To reset the Satus in the UI...
//        objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.transaction.dailyDepositTrans.DailyDepositTransMRB", ProxyParameters.LANGUAGE);
        this.resetUIData();        
//        setHelpMessage();
    }
    
    // Creates The Instance of InwardClearingOB
    private void setObservable() {
        try{
            observable = new ClearingDataImportOB();
            observable.addObserver(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //Authorize Button to be added...
    private void setFieldNames() {
//        lblAgentType.setName("lblAgentType");
//        lblAccNo.setName("lblAccNo");
//        lblAccHd.setName("lblAccHd");
//        lblAmount.setName("lblAmount");
//        lblBalance.setName("lblBalance");
//        lblBalanceAmount.setName("lblBalanceAmount");
//        lblInitiatorChannel.setName("lblInitiatorChannel");
//        lblProdId.setName("lblProdId");
//        lblProductId.setName("productId");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace6.setName("lblSpace6");
        lblStatus.setName("lblStatus");
//        lblTransactionType.setName("lblTransactionType");
//        cboAgentType.setName("cboAgentType");
//        btnAccHdId.setName("btnAccHdId");
        btnSourceFile.setName("btnSourceFile");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
//        btnDenomination.setName("btnDenomination");
//        btnDailyDepSubDel.setName("btnDailyDepSubDel");
//        btnDailyDepSubNew.setName("btnDailyDepSubNew");
//        btnDailyDepSubSave.setName("btnDailyDepSubSave");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
//        txtAccHdId.setName("txtAccHdId");
//        lblAccHdId.setName("AccHeadId");
        txtSourceFile.setName("txtSourceFile");
//        txtAmount.setName("txtAmount");
//        txtInitiatorChannel.setName("txtInitiatorChannel");
//        rdoTransactionType_Credit.setName("rdoTransactionType_Credit");//        btnCancel.setName("btnCancel");
        mbrMain.setName("mbrMain");
//        panAccHd.setName("panAccHd");
//        lblAgNm.setText("lblAgNm");
//        lblAgNmVal.setText("");
//        lblCustNm.setText("lblCustNm");
//        lblCustnmVal.setText("");
//        lblInstrumentDate.setText("lblInstrumentDate");
//        lblAgNm.setText("lblAgNm");
//        lblAgNmVal.setText("");
//        lblBalanceVal.setText("");
//        lblTotalAmt.setText("lblTotalAmt");
//        lblTotAmtVal.setText("");
    }
    
    private void internationalize() {
//        DailyDepositTransRB resourceBundle = new DailyDepositTransRB();
//        //        resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.transaction.dailyDepositTrans.DailyDepositTransRB", ProxyParameters.LANGUAGE);
//        btnClose.setText(resourceBundle.getString("btnClose"));
//        //        ((javax.swing.border.TitledBorder)panTransaction.getBorder()).setTitle(resourceBundle.getString("panTransaction"));
////        lblInitiatorChannel.setText(resourceBundle.getString("lblInitiatorChannel"));
//        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
//        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
//        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
//        lblAccHdId.setText(resourceBundle.getString("lblAccHdId"));
//        lblAccHd.setText(resourceBundle.getString("lblAccHd"));
//        lblAccNo.setText(resourceBundle.getString("lblAccNo"));
////        lblTransactionType.setText(resourceBundle.getString("lblTransactionType"));
//        lblStatus.setText(resourceBundle.getString("lblStatus"));
//        lblAmount.setText(resourceBundle.getString("lblAmount"));
//        lblProdId.setText(resourceBundle.getString("lblProdId"));
////        lblProductId.setText(resourceBundle.getString("lblProductId"));
////        lblAccHdId.setText(resourceBundle.getString("lblAccHdId"));
//        lblAgentType.setText(resourceBundle.getString("lblAgentType"));
////        lblBalance.setText(resourceBundle.getString("lblBalance"));
//        lblBalanceAmount.setText(resourceBundle.getString("lblBalanceAmount"));
//        
//        btnDelete.setText(resourceBundle.getString("btnDelete"));
//        btnSave.setText(resourceBundle.getString("btnSave"));
//        btnAccNo.setText(resourceBundle.getString("btnAccNo"));
//        btnPrint.setText(resourceBundle.getString("btnPrint"));
//        btnClose.setText(resourceBundle.getString("btnClose"));
//        btnEdit.setText(resourceBundle.getString("btnEdit"));
//        btnNew.setText(resourceBundle.getString("btnNew"));
////        rdoTransactionType_Credit.setText(resourceBundle.getString("rdoTransactionType_Credit"));
//        //        ((javax.swing.border.TitledBorder)panData.getBorder()).setTitle(resourceBundle.getString("panData"));
//        //        ((javax.swing.border.TitledBorder)panLableValues.getBorder()).setTitle(resourceBundle.getString("panLableValues"));
//        btnCancel.setText(resourceBundle.getString("btnCancel"));
//        lblAgNm.setText(resourceBundle.getString("lblAgNm"));
//        
//        lblCustNm.setText(resourceBundle.getString("lblCustNm"));
//        lblInstrumentDate.setText(resourceBundle.getString("lblInstrumentDate"));
//        lblAgNm.setText(resourceBundle.getString("lblAgNm"));
//        lblTotalAmt.setText( resourceBundle.getString("lblTotalAmt"));
    
    }
    
    public void setMandatoryHashMap() {
//        mandatoryMap = new HashMap();
//        mandatoryMap.put("txtAccNo", new Boolean(true));
//        mandatoryMap.put("txtInitiatorChannel", new Boolean(true));
//        mandatoryMap.put("txtAmount", new Boolean(true));
//        mandatoryMap.put("txtAccHdId", new Boolean(true));
//        mandatoryMap.put("rdoTransactionType_Credit", new Boolean(true));
//        mandatoryMap.put("cboAgentType", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    // To Reset the Radio Buttons in the UI after any operation, We've to
    //1. Remove the Radio Buttons fron the Radio Groups...
    //2. Add the Radio Buttons Back in The Radio Groups...
    //a.) To Remove the Radio buttons...
    private void removeRadioButtons() {
        rdoTransactionType.remove(rdoUploadingFile);
        rdoTransactionType.remove(rdoDownLoadingFile);
    }
    
    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        rdoTransactionType = new CButtonGroup();
        rdoTransactionType.add(rdoUploadingFile);
        rdoTransactionType.add(rdoDownLoadingFile);
    }
    
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        lblTransactionTypeValue.setText(observable.getLblTransactionTypeValue());
        lblUserNumberValue.setText(observable.getLblUserNumberValue());
        lblUserNameValue.setText(observable.getLblUserNameValue());
        lblUserCreditRefNoValue.setText(observable.getLblUserCreditRefNoValue());
        lblECSTapeInputNoValue.setText(observable.getLblECSTapeInputNoValue());
        lblSponsorValue.setText(observable.getLblSponsorValue());
        lblUserBankAcNoValue.setText(observable.getLblUserBankAcNoValue());
        lblLedgerFolioNoValue.setText(observable.getLblLedgerFolioNoValue());
        lblUserLimitValue.setText(observable.getLblUserLimitValue());
        lblTotalAmountValue.setText(observable.getLblTotalAmountValue());
        lblSettlementDateValue.setText(observable.getLblSettlementDateValue());
        lblECSItemNoValue.setText(observable.getLblECSItemNoValue());
        lblCheckSumValue.setText(observable.getLblCheckSumValue());
        lblFilterValue.setText(observable.getLblFilterValue());
        rdoUploadingFile.setSelected(observable.getRdoUploadingFile());
        rdoDownLoadingFile.setSelected(observable.getRdoDownLoadingFile());
//        txtAccNo.setText(observable.getTxtAccNo());
//        txtInitiatorChannel.setText(observable.getTxtInitiatorChannel());
//        rdoTransactionType_Credit.setSelected(observable.getRdoTransactionType_Credit());
//        txtAmount.setText(observable.getTxtAmount());
//        cboAgentType.setSelectedItem(observable.getCboAgentType());
//        lblBalance.setText(observable.getBalance());
//        lblTransactionIDDesc1.setText(observable.getLblTransactionId());
//        lblTransactionDateDesc1.setText(observable.getLblTransDate());
//        lblInitiatorIDDesc1.setText(observable.getLblInitiatorId());
//        lblCustnmVal.setText(observable.getLblAccName());
//        lblBalanceVal.setText(observable.getBalance());
//        lblTotAmtVal.setText(CommonUtil.convertObjToStr(new Double(Amt)));
//        tdtInstrumentDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtInstrumentDate()));
        addRadioButtons();
//        txtAccNoActionPerformed(null);
    }
    
    
    public void updateOBFields() {
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.setLblTransactionTypeValue(lblTransactionTypeValue.getText());
        observable.setLblUserNumberValue(lblUserNumberValue.getText());
        observable.setLblUserNameValue(lblUserNameValue.getText());
        observable.setLblUserCreditRefNoValue(lblUserCreditRefNoValue.getText());
        observable.setLblECSTapeInputNoValue(lblECSTapeInputNoValue.getText());
        observable.setLblSponsorValue(lblSponsorValue.getText());
        observable.setLblUserBankAcNoValue(lblUserBankAcNoValue.getText());
        observable.setLblLedgerFolioNoValue(lblLedgerFolioNoValue.getText());
        observable.setLblUserLimitValue(lblUserLimitValue.getText());
        observable.setLblTotalAmountValue(lblTotalAmountValue.getText());
        observable.setLblSettlementDateValue(lblSettlementDateValue.getText());
        observable.setLblECSItemNoValue(lblECSItemNoValue.getText());
        observable.setLblCheckSumValue(lblCheckSumValue.getText());
        observable.setLblFilterValue(lblFilterValue.getText());
        observable.setRdoUploadingFile(rdoUploadingFile.isSelected());
        observable.setRdoDownLoadingFile(rdoDownLoadingFile.isSelected());
//        observable.setTxtInitiatorChannel((String) txtInitiatorChannel.getText());
//        observable.setRdoTransactionType_Credit(rdoTransactionType_Credit.isSelected());
//        observable.setTxtAmount(txtAmount.getText());
//        observable.setTxtAccHd(lblAccHdId.getText());
//        observable.setTdtInstrumentDate(DateUtil.getDateMMDDYYYY(tdtInstrumentDate.getDateValue()));
//        observable.setLblTransactionId(lblTransactionIDDesc1.getText());
        System.out.println("$$$$$$$$$$$ observable.getLblTransactionId() "+observable.getLblTransactionId());
//        double tot_amt=0.0;
//        tot_amt=CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()+CommonUtil.convertObjToDouble(lblBalanceVal.getText()).doubleValue();
//       observable.setCboAgentType(CommonUtil.convertObjToStr(cboAgentType.getSelectedItem()));
        System.out.println("Amt$$$$$$$$$$$"+Amt);
        observable.setTotalGlAmt(Amt);
//        observable.setTotalAmt(Amt);
    }
    
    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        tblClearingDataTransTable.setModel(observable.getTbmClearingData());
    }
    //To set the length of the Text Fields and Numeric Validation...
    private void setMaxLenths() {
        txtSourceFile.setAllowAll(true);
        txtDestinationFile.setAllowAll(true);
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
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
    }

    private void updateTable(){
        this.tblClearingDataTransTable.setModel(observable.getTbmClearingData());
        tblClearingDataTransTable.revalidate();
    }    
    
    private void resetUIData(){
        this._intTransferNew = false;
        this.viewType = -1;
        this.rowForEdit=-1;
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
        panClearingDataImport = new com.see.truetransact.uicomponent.CPanel();
        tabClearingDataImport = new com.see.truetransact.uicomponent.CTabbedPane();
        panClearingDataFile = new com.see.truetransact.uicomponent.CPanel();
        lblSourceFile = new com.see.truetransact.uicomponent.CLabel();
        panSourceFile = new com.see.truetransact.uicomponent.CPanel();
        txtSourceFile = new com.see.truetransact.uicomponent.CTextField();
        btnSourceFile = new com.see.truetransact.uicomponent.CButton();
        panDestinationFile = new com.see.truetransact.uicomponent.CPanel();
        txtDestinationFile = new com.see.truetransact.uicomponent.CTextField();
        btnDestinationFile = new com.see.truetransact.uicomponent.CButton();
        lblDestinationFile = new com.see.truetransact.uicomponent.CLabel();
        panTransactionType = new com.see.truetransact.uicomponent.CPanel();
        rdoUploadingFile = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDownLoadingFile = new com.see.truetransact.uicomponent.CRadioButton();
        lblTypeofFile = new com.see.truetransact.uicomponent.CLabel();
        btnAddDebit = new com.see.truetransact.uicomponent.CButton();
        lblTotalAmount = new com.see.truetransact.uicomponent.CLabel();
        lblUserNumber = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionType = new com.see.truetransact.uicomponent.CLabel();
        lblCheckSum = new com.see.truetransact.uicomponent.CLabel();
        lblFilter = new com.see.truetransact.uicomponent.CLabel();
        lblUserCreditRefNo = new com.see.truetransact.uicomponent.CLabel();
        lblUserName = new com.see.truetransact.uicomponent.CLabel();
        lblSettlementDate = new com.see.truetransact.uicomponent.CLabel();
        lblECSItemNo = new com.see.truetransact.uicomponent.CLabel();
        lblUserLimit = new com.see.truetransact.uicomponent.CLabel();
        lblLedgerFolioNo = new com.see.truetransact.uicomponent.CLabel();
        lblUserBankAcNo = new com.see.truetransact.uicomponent.CLabel();
        lblSponsor = new com.see.truetransact.uicomponent.CLabel();
        lblECSTapeInputNo = new com.see.truetransact.uicomponent.CLabel();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        lblUserBankAcNoValue = new com.see.truetransact.uicomponent.CLabel();
        lblFilterValue = new com.see.truetransact.uicomponent.CLabel();
        lblSponsorValue = new com.see.truetransact.uicomponent.CLabel();
        lblECSTapeInputNoValue = new com.see.truetransact.uicomponent.CLabel();
        lblLedgerFolioNoValue = new com.see.truetransact.uicomponent.CLabel();
        lblCheckSumValue = new com.see.truetransact.uicomponent.CLabel();
        lblTotalAmountValue = new com.see.truetransact.uicomponent.CLabel();
        lblUserLimitValue = new com.see.truetransact.uicomponent.CLabel();
        lblUserCreditRefNoValue = new com.see.truetransact.uicomponent.CLabel();
        lblUserNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblUserNumberValue = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionTypeValue = new com.see.truetransact.uicomponent.CLabel();
        lblECSItemNoValue = new com.see.truetransact.uicomponent.CLabel();
        lblSettlementDateValue = new com.see.truetransact.uicomponent.CLabel();
        panClearingDataTrans = new com.see.truetransact.uicomponent.CPanel();
        panClearingDataTransTable = new com.see.truetransact.uicomponent.CPanel();
        srpClearingDataTransTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblClearingDataTransTable = new com.see.truetransact.uicomponent.CTable();
        panInfoClearingDataTransTable = new com.see.truetransact.uicomponent.CPanel();
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
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));

        panClearingDataImport.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panClearingDataImport.setMinimumSize(new java.awt.Dimension(790, 580));
        panClearingDataImport.setPreferredSize(new java.awt.Dimension(790, 580));
        panClearingDataImport.setLayout(new java.awt.GridBagLayout());

        tabClearingDataImport.setMinimumSize(new java.awt.Dimension(790, 580));
        tabClearingDataImport.setName("");
        tabClearingDataImport.setPreferredSize(new java.awt.Dimension(790, 580));

        panClearingDataFile.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panClearingDataFile.setMinimumSize(new java.awt.Dimension(790, 580));
        panClearingDataFile.setPreferredSize(new java.awt.Dimension(790, 580));
        panClearingDataFile.setLayout(new java.awt.GridBagLayout());

        lblSourceFile.setText("Source File Path");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panClearingDataFile.add(lblSourceFile, gridBagConstraints);

        panSourceFile.setMinimumSize(new java.awt.Dimension(500, 25));
        panSourceFile.setPreferredSize(new java.awt.Dimension(500, 25));
        panSourceFile.setLayout(new java.awt.GridBagLayout());

        txtSourceFile.setEditable(false);
        txtSourceFile.setMinimumSize(new java.awt.Dimension(475, 25));
        txtSourceFile.setPreferredSize(new java.awt.Dimension(475, 25));
        txtSourceFile.setEnabled(false);
        txtSourceFile.setOpaque(false);
        txtSourceFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSourceFileActionPerformed(evt);
            }
        });
        panSourceFile.add(txtSourceFile, new java.awt.GridBagConstraints());

        btnSourceFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSourceFile.setToolTipText("Account No.");
        btnSourceFile.setPreferredSize(new java.awt.Dimension(25, 25));
        btnSourceFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSourceFileActionPerformed(evt);
            }
        });
        panSourceFile.add(btnSourceFile, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panClearingDataFile.add(panSourceFile, gridBagConstraints);

        panDestinationFile.setMinimumSize(new java.awt.Dimension(500, 25));
        panDestinationFile.setPreferredSize(new java.awt.Dimension(500, 25));
        panDestinationFile.setLayout(new java.awt.GridBagLayout());

        txtDestinationFile.setEditable(false);
        txtDestinationFile.setMinimumSize(new java.awt.Dimension(475, 25));
        txtDestinationFile.setPreferredSize(new java.awt.Dimension(475, 25));
        txtDestinationFile.setEnabled(false);
        txtDestinationFile.setOpaque(false);
        panDestinationFile.add(txtDestinationFile, new java.awt.GridBagConstraints());

        btnDestinationFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDestinationFile.setToolTipText("Account No.");
        btnDestinationFile.setPreferredSize(new java.awt.Dimension(25, 25));
        btnDestinationFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDestinationFileActionPerformed(evt);
            }
        });
        panDestinationFile.add(btnDestinationFile, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panClearingDataFile.add(panDestinationFile, gridBagConstraints);

        lblDestinationFile.setText("Destination File Path");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panClearingDataFile.add(lblDestinationFile, gridBagConstraints);

        panTransactionType.setMinimumSize(new java.awt.Dimension(400, 23));
        panTransactionType.setPreferredSize(new java.awt.Dimension(400, 23));
        panTransactionType.setLayout(new java.awt.GridBagLayout());

        rdoUploadingFile.setText("Uploading File");
        rdoUploadingFile.setMaximumSize(new java.awt.Dimension(77, 18));
        rdoUploadingFile.setMinimumSize(new java.awt.Dimension(125, 18));
        rdoUploadingFile.setPreferredSize(new java.awt.Dimension(85, 27));
        rdoUploadingFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoUploadingFileActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransactionType.add(rdoUploadingFile, gridBagConstraints);

        rdoDownLoadingFile.setText("DownLoading File");
        rdoDownLoadingFile.setMargin(new java.awt.Insets(2, 5, 2, 2));
        rdoDownLoadingFile.setMaximumSize(new java.awt.Dimension(69, 18));
        rdoDownLoadingFile.setMinimumSize(new java.awt.Dimension(160, 18));
        rdoDownLoadingFile.setPreferredSize(new java.awt.Dimension(160, 18));
        rdoDownLoadingFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDownLoadingFileActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransactionType.add(rdoDownLoadingFile, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 59;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 4, 4);
        panClearingDataFile.add(panTransactionType, gridBagConstraints);

        lblTypeofFile.setText("Type of File");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panClearingDataFile.add(lblTypeofFile, gridBagConstraints);

        btnAddDebit.setText("Ok");
        btnAddDebit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDebitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panClearingDataFile.add(btnAddDebit, gridBagConstraints);

        lblTotalAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalAmount.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panClearingDataFile.add(lblTotalAmount, gridBagConstraints);

        lblUserNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUserNumber.setText("User Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panClearingDataFile.add(lblUserNumber, gridBagConstraints);

        lblTransactionType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTransactionType.setText("ECS Transaction Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panClearingDataFile.add(lblTransactionType, gridBagConstraints);

        lblCheckSum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCheckSum.setText("Check sum");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panClearingDataFile.add(lblCheckSum, gridBagConstraints);

        lblFilter.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFilter.setText("Filter");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panClearingDataFile.add(lblFilter, gridBagConstraints);

        lblUserCreditRefNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUserCreditRefNo.setText("User Credit Reference No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panClearingDataFile.add(lblUserCreditRefNo, gridBagConstraints);

        lblUserName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUserName.setText("User Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panClearingDataFile.add(lblUserName, gridBagConstraints);

        lblSettlementDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSettlementDate.setText("Settlement Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panClearingDataFile.add(lblSettlementDate, gridBagConstraints);

        lblECSItemNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblECSItemNo.setText("ECS item sequence No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panClearingDataFile.add(lblECSItemNo, gridBagConstraints);

        lblUserLimit.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUserLimit.setText("User Defined Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panClearingDataFile.add(lblUserLimit, gridBagConstraints);

        lblLedgerFolioNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLedgerFolioNo.setText("Ledger folio No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panClearingDataFile.add(lblLedgerFolioNo, gridBagConstraints);

        lblUserBankAcNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUserBankAcNo.setText("User's Bank Ac No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panClearingDataFile.add(lblUserBankAcNo, gridBagConstraints);

        lblSponsor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSponsor.setText("Sponsor Bank/Branch");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panClearingDataFile.add(lblSponsor, gridBagConstraints);

        lblECSTapeInputNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblECSTapeInputNo.setText("ECS tape input No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panClearingDataFile.add(lblECSTapeInputNo, gridBagConstraints);

        btnProcess.setText("Process");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearingDataFile.add(btnProcess, gridBagConstraints);

        lblUserBankAcNoValue.setMinimumSize(new java.awt.Dimension(100, 18));
        lblUserBankAcNoValue.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panClearingDataFile.add(lblUserBankAcNoValue, gridBagConstraints);

        lblFilterValue.setMinimumSize(new java.awt.Dimension(100, 18));
        lblFilterValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panClearingDataFile.add(lblFilterValue, gridBagConstraints);

        lblSponsorValue.setMinimumSize(new java.awt.Dimension(100, 18));
        lblSponsorValue.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panClearingDataFile.add(lblSponsorValue, gridBagConstraints);

        lblECSTapeInputNoValue.setMinimumSize(new java.awt.Dimension(100, 18));
        lblECSTapeInputNoValue.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panClearingDataFile.add(lblECSTapeInputNoValue, gridBagConstraints);

        lblLedgerFolioNoValue.setMinimumSize(new java.awt.Dimension(100, 18));
        lblLedgerFolioNoValue.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panClearingDataFile.add(lblLedgerFolioNoValue, gridBagConstraints);

        lblCheckSumValue.setMinimumSize(new java.awt.Dimension(100, 18));
        lblCheckSumValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panClearingDataFile.add(lblCheckSumValue, gridBagConstraints);

        lblTotalAmountValue.setMinimumSize(new java.awt.Dimension(100, 18));
        lblTotalAmountValue.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panClearingDataFile.add(lblTotalAmountValue, gridBagConstraints);

        lblUserLimitValue.setMinimumSize(new java.awt.Dimension(100, 18));
        lblUserLimitValue.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panClearingDataFile.add(lblUserLimitValue, gridBagConstraints);

        lblUserCreditRefNoValue.setMinimumSize(new java.awt.Dimension(100, 18));
        lblUserCreditRefNoValue.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panClearingDataFile.add(lblUserCreditRefNoValue, gridBagConstraints);

        lblUserNameValue.setMinimumSize(new java.awt.Dimension(100, 18));
        lblUserNameValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panClearingDataFile.add(lblUserNameValue, gridBagConstraints);

        lblUserNumberValue.setMinimumSize(new java.awt.Dimension(100, 18));
        lblUserNumberValue.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panClearingDataFile.add(lblUserNumberValue, gridBagConstraints);

        lblTransactionTypeValue.setMinimumSize(new java.awt.Dimension(100, 18));
        lblTransactionTypeValue.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panClearingDataFile.add(lblTransactionTypeValue, gridBagConstraints);

        lblECSItemNoValue.setMinimumSize(new java.awt.Dimension(100, 18));
        lblECSItemNoValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panClearingDataFile.add(lblECSItemNoValue, gridBagConstraints);

        lblSettlementDateValue.setMinimumSize(new java.awt.Dimension(100, 18));
        lblSettlementDateValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panClearingDataFile.add(lblSettlementDateValue, gridBagConstraints);

        tabClearingDataImport.addTab("ECS ClearingDataLoading", panClearingDataFile);

        panClearingDataTrans.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panClearingDataTrans.setMinimumSize(new java.awt.Dimension(790, 580));
        panClearingDataTrans.setPreferredSize(new java.awt.Dimension(790, 580));
        panClearingDataTrans.setLayout(new java.awt.GridBagLayout());

        panClearingDataTransTable.setBorder(javax.swing.BorderFactory.createTitledBorder("ECS Clearing Details"));
        panClearingDataTransTable.setMinimumSize(new java.awt.Dimension(650, 600));
        panClearingDataTransTable.setName("panTransInfo");
        panClearingDataTransTable.setPreferredSize(new java.awt.Dimension(650, 600));
        panClearingDataTransTable.setLayout(new java.awt.GridBagLayout());

        srpClearingDataTransTable.setMinimumSize(new java.awt.Dimension(400, 404));

        tblClearingDataTransTable.setModel(new javax.swing.table.DefaultTableModel(
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
        tblClearingDataTransTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblClearingDataTransTableMousePressed(evt);
            }
        });
        srpClearingDataTransTable.setViewportView(tblClearingDataTransTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panClearingDataTransTable.add(srpClearingDataTransTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearingDataTrans.add(panClearingDataTransTable, gridBagConstraints);

        panInfoClearingDataTransTable.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Info"));
        panInfoClearingDataTransTable.setMinimumSize(new java.awt.Dimension(400, 600));
        panInfoClearingDataTransTable.setName("panInfoPanel");
        panInfoClearingDataTransTable.setPreferredSize(new java.awt.Dimension(400, 600));
        panInfoClearingDataTransTable.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearingDataTrans.add(panInfoClearingDataTransTable, gridBagConstraints);

        tabClearingDataImport.addTab("ECS ClearingDataTransactionDetails", panClearingDataTrans);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panClearingDataImport.add(tabClearingDataImport, gridBagConstraints);

        getContentPane().add(panClearingDataImport, java.awt.BorderLayout.CENTER);

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

    private void tblClearingDataTransTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClearingDataTransTableMousePressed
        // TODO add your handling code here:
//        int rowRenewcount = (int)(Integer.parseInt(CommonUtil.convertObjToStr(tblClearingDataTransTable.getValueAt(tblClearingDataTransTable.getSelectedRow(),0))));
        int selectedRow = -1;
        selectedRow = tblClearingDataTransTable.getSelectedRow();
        String AcctNum = CommonUtil.convertObjToStr(tblClearingDataTransTable.getValueAt(selectedRow, 0));
        String values = CommonUtil.convertObjToStr(tblClearingDataTransTable.getValueAt(selectedRow, 4));
        transDetails.setTransDetails("OA", ProxyParameters.BRANCH_ID, AcctNum);
        if(evt.getClickCount() == 2){
            String[] obj = {("Post"),("Bounce"),("Cancel")};
            int option = COptionPane.showOptionDialog(null, "Action Selection","Please select the Action to be performed.",
            COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj, obj[0]);
            if(option == 0){
                observable.updatingPostValues(selectedRow,"Post");
            }else if(option == 1){
                observable.updatingBounceValues(selectedRow,"Bounce");
            }
        }
    }//GEN-LAST:event_tblClearingDataTransTableMousePressed

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        String filelength = "";
        int i = 0;
        LinkedHashMap dataMap = new LinkedHashMap();
        try {
            FileInputStream fis = new FileInputStream(selFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);
            try {
                while(dis.available()!=0){
                    LinkedHashMap eachlineMap = new LinkedHashMap();
                    filelength = dis.readLine();
                    long transCode = CommonUtil.convertObjToLong(filelength.substring(0,2));
                    if(i != 0){
                        if(transCode == 66){
                            eachlineMap.put("TRANS_CODE",String.valueOf(filelength.substring(0,2)));
                            eachlineMap.put("DESTINATION_SORT_CODE",String.valueOf(filelength.substring(2,11)));
                            eachlineMap.put("DESTINATION_AC_TYPE",String.valueOf(filelength.substring(11,13)));
                            eachlineMap.put("LEDGER_NO",String.valueOf(filelength.substring(13,16)));
                            eachlineMap.put("DESTINATION_AC_NO",String.valueOf(filelength.substring(16,31)));
                            eachlineMap.put("DESTINATION_AC_HOLDERS_NAME",String.valueOf(filelength.substring(31,71)));
                            eachlineMap.put("SPONSOR",String.valueOf(filelength.substring(71,80)));
                            eachlineMap.put("USER_NUMBER",String.valueOf(filelength.substring(80,87)));
                            eachlineMap.put("USER_NAME",String.valueOf(filelength.substring(87,107)));
                            eachlineMap.put("USER_CREDIT_REF",String.valueOf(filelength.substring(107,120)));
                            eachlineMap.put("TOTAL_AMT",String.valueOf(filelength.substring(120,133)));
                            eachlineMap.put("SEQUENCE_NO",String.valueOf(filelength.substring(133,143)));
                            eachlineMap.put("CHECK_SUM",String.valueOf(filelength.substring(143,153)));
                            eachlineMap.put("FILTER",String.valueOf(filelength.substring(153,156)));
                            dataMap.put(String.valueOf(i),eachlineMap);
                            i = i+1;
                        }
                        if(transCode == 99){
                            
                        }
                        if(transCode == 11){
                            i = i + 0;
                        }
                    }else{
                        eachlineMap.put("TRANS_CODE",String.valueOf(filelength.substring(0,2)));
                        eachlineMap.put("USER_NUMBER",String.valueOf(filelength.substring(2,9)));
                        eachlineMap.put("USER_NAME",String.valueOf(filelength.substring(9,49)));
                        eachlineMap.put("USER_CREDIT_REF",String.valueOf(filelength.substring(49,63)));
                        eachlineMap.put("TAPE_INPUT_NO",String.valueOf(filelength.substring(63,72)));
                        eachlineMap.put("SPONSOR",String.valueOf(filelength.substring(72,81)));
                        eachlineMap.put("USERS_AC_NO",String.valueOf(filelength.substring(81,96)));
                        eachlineMap.put("LEDGER_NO",String.valueOf(filelength.substring(96,99)));
                        eachlineMap.put("USER_LIMIT",String.valueOf(filelength.substring(99,112)));
                        eachlineMap.put("TOTAL_AMT",String.valueOf(filelength.substring(112,125)));
                        Date setDate = (Date)curDate.clone();
                        long day = CommonUtil.convertObjToLong(filelength.substring(125,127));
                        long month = CommonUtil.convertObjToLong(filelength.substring(127,129));
                        long year = CommonUtil.convertObjToLong(filelength.substring(129,133));
                        setDate.setDate((int)day);
                        setDate.setMonth((int)month-1);
                        setDate.setYear((int)year-1900);                        
                        eachlineMap.put("SETLLEMENT_DT",setDate);
                        eachlineMap.put("SEQUENCE_NO",String.valueOf(filelength.substring(133,143)));
                        eachlineMap.put("CHECK_SUM",String.valueOf(filelength.substring(143,153)));
                        eachlineMap.put("FILTER",String.valueOf(filelength.substring(153,156)));
                        dataMap.put(String.valueOf(i),eachlineMap);
                        i = i+1;
                        filelength = "";
                    }
                }
                String[] obj ={"OK ","Cancel"};
                int option =COptionPane.showOptionDialog(null,("Total " + i +" Records"), ("Deposit Renewal Option"),
                COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj,obj[0]);
                if(option ==0){
                    System.out.println("dataMap"+dataMap);
                    observable.overallRecords(dataMap);                    
                }else{

                }
//                String filename = "";
//                String line = "";
//                String[] fields = null;
//                java.util.Vector vector = new java.util.Vector();
//                //            FileInputStream fis = new FileInputStream(filename);
//                InputStreamReader isr = new InputStreamReader(fis);
//                LineNumberReader lnr = new LineNumberReader(isr);
//                line = lnr.readLine();
//                if(line != null){
//                    fields = line.split("\\s+");
//                }
//                for(int j = 0;j<fields.length;j++){
//                    System.out.println(j+" = "+fields[j]);
//                }
            } catch(IOException iox) {
                System.out.println("File read error...");
                iox.printStackTrace();
            }
        } catch (FileNotFoundException fnf) {
            System.out.println("File not found...");
            fnf.printStackTrace();
        }
    }//GEN-LAST:event_btnProcessActionPerformed

    private void btnAddDebitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDebitActionPerformed
        // TODO add your handling code here:
        String filelength = "";
        int i = 1;
        HashMap dataMap = new HashMap();
        try {
            FileInputStream fis = new FileInputStream(selFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);
            try {
                while(dis.available()!=0){
                    HashMap eachlineMap = new HashMap();
                    filelength = dis.readLine();
                    long type = CommonUtil.convertObjToLong(filelength.substring(0,2));
                    if(type == 11){
                        lblTransactionTypeValue.setText("DEBIT");
                    }else if(type == 55){
                        lblTransactionTypeValue.setText("CREDIT");
                    }
                    lblUserNumberValue.setText(String.valueOf(filelength.substring(2,9)));
                    lblUserNameValue.setText(String.valueOf(filelength.substring(9,49)));
                    lblUserCreditRefNoValue.setText(String.valueOf(filelength.substring(49,63)));
                    lblECSTapeInputNoValue.setText(String.valueOf(filelength.substring(63,72)));
                    lblSponsorValue.setText(String.valueOf(filelength.substring(72,81)));
                    lblUserBankAcNoValue.setText(String.valueOf(filelength.substring(81,96)));
                    lblLedgerFolioNoValue.setText(String.valueOf(filelength.substring(96,99)));
                    lblUserLimitValue.setText(String.valueOf(filelength.substring(99,112)));
                    lblTotalAmountValue.setText(String.valueOf(filelength.substring(112,125)));
                    Date setDate = (Date)curDate.clone();
                    long day = CommonUtil.convertObjToLong(filelength.substring(125,127));
                    long month = CommonUtil.convertObjToLong(filelength.substring(127,129));
                    long year = CommonUtil.convertObjToLong(filelength.substring(129,133));
                    setDate.setDate((int)day);
                    setDate.setMonth((int)month-1);
                    setDate.setYear((int)year-1900);
                    lblSettlementDateValue.setText(CommonUtil.convertObjToStr(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(setDate))));
                    lblECSItemNoValue.setText(String.valueOf(filelength.substring(133,143)));
                    lblCheckSumValue.setText(String.valueOf(filelength.substring(143,153)));
                    lblFilterValue.setText(String.valueOf(filelength.substring(153,156)));
                    btnProcess.setEnabled(true);
                    break;
                }
//            String filename = "";
//            String line = "";
//            String[] fields = null;
//            java.util.Vector vector = new java.util.Vector();
////            FileInputStream fis = new FileInputStream(filename);
//            InputStreamReader isr = new InputStreamReader(fis);
//            LineNumberReader lnr = new LineNumberReader(isr);
//            line = lnr.readLine();
//            if(line != null){
//                fields = line.split("\\s+");
//            }
//            for(int j = 0;j<fields.length;j++){
//                System.out.println(j+" = "+fields[j]);
//            }
            } catch(IOException iox) {
                System.out.println("File read error...");
                iox.printStackTrace();
            }            
        } catch (FileNotFoundException fnf) {
            System.out.println("File not found...");
            fnf.printStackTrace();
        }
        
    }//GEN-LAST:event_btnAddDebitActionPerformed
    private void resetForm(){
        txtSourceFile.setText("");
        txtDestinationFile.setText("");
        lblTransactionTypeValue.setText("");
        lblUserNumberValue.setText("");
        lblUserNameValue.setText("");
        lblUserCreditRefNoValue.setText("");
        lblECSTapeInputNoValue.setText("");
        lblSponsorValue.setText("");
        lblUserBankAcNoValue.setText("");
        lblLedgerFolioNoValue.setText("");
        lblUserLimitValue.setText("");
        lblTotalAmountValue.setText("");
        lblSettlementDateValue.setText("");
        lblECSItemNoValue.setText("");
        lblCheckSumValue.setText("");
        lblFilterValue.setText("");
    }
    private void rdoDownLoadingFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDownLoadingFileActionPerformed
        // TODO add your handling code here:
        txtSourceFile.setText("");
        txtDestinationFile.setText("");        
    }//GEN-LAST:event_rdoDownLoadingFileActionPerformed

    private void rdoUploadingFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoUploadingFileActionPerformed
        // TODO add your handling code here:
        txtSourceFile.setText("");
        txtDestinationFile.setText("");
        rdoUploadingFile.setEnabled(false);
        rdoDownLoadingFile.setEnabled(false);
        
    }//GEN-LAST:event_rdoUploadingFileActionPerformed

    private void btnDestinationFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDestinationFileActionPerformed
        // TODO add your handling code here:
        if(rdoUploadingFile.isSelected() == false && rdoDownLoadingFile.isSelected() == false){
            ClientUtil.showAlertWindow("Choose file type...");
            return;            
        }else{
//            txtSourceFile.setText("");
            txtDestinationFile.setText("");
            int arrlen = 10000;
            byte[] infile = new byte[arrlen];
    //        Frame parent = new Frame();
    //        FileDialog fd = new FileDialog(this, "Please choose a file:",FileDialog.LOAD);
    //        fd.show();
            final JFileChooser objJFileChooser = new JFileChooser();
            final File selFile;
            byte[] byteArray;
            StringBuffer filePath;
            String fileName;

            if (objJFileChooser.showOpenDialog(null) == objJFileChooser.APPROVE_OPTION){
                selFile = objJFileChooser.getSelectedFile();
                filePath = new StringBuffer(selFile.getAbsolutePath());
                // read the file
                System.out.println("reading file " + filePath);
                try {
                    FileInputStream fis = new FileInputStream(selFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    DataInputStream dis = new DataInputStream(bis);
                    try {
                        int filelength = dis.read(infile);
                        String filestring = new String(infile, 0, filelength);
                        txtDestinationFile.setText(String.valueOf(selFile));
                        System.out.println("FILE CONTENT=" + filestring);
                    } catch(IOException iox) {
                        System.out.println("File read error...");
                        iox.printStackTrace();
                    }
                } catch (FileNotFoundException fnf) {
                    System.out.println("File not found...");
                    fnf.printStackTrace();
                }
            }
        }
        
    }//GEN-LAST:event_btnDestinationFileActionPerformed

    
    

//    private void panEnableDisable(boolean value){
//        //this.lblAccountHeadValue.setText("");
//        ClientUtil.enableDisable(this,value);
//        enableDisableButtons(value);
//        if(observable.getOperation()==ClientConstants.ACTIONTYPE_DELETE)  {
//            ClientUtil.enableDisable(this.panData,false);
//        }
//            ClientUtil.enableDisable(this.panData,true);
//            this.btnAccNo.setEnabled(false);
//            btnDailyDepSubSave.setEnabled(true);
//            btnDailyDepSubNew.setEnabled(false);
////        }
////        observable.resetTransactionDetails();
//    }
    
    
//public void updateData(Observable ob, Object arg) {     
////      txtAccNo.setText(observable.getTxtAccNo());
//      txtAmount.setText(observable.getTxtAmount());
////      lblBalance.setText(observable.getBalance());
//        if(observable.getOperation()!=ClientConstants.ACTIONTYPE_NEW)
//            this.updateTable();
//    }
    
//    private void updationForDaily() {
//        observable.populateDailyTransfer(tblDailyDepositList.getSelectedRow());
//         tdtInstrumentDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtInstrumentDate()));
//        
////        authorizationCheckMap.put(String.valueOf(tblTransList.getSelectedRow()), String.valueOf(tblTransList.getSelectedRow()));
////        updateAccountInfo();
//        _intTransferNew = false;
////            ClientUtil.enableDisable(panData,true);
////            this.btnAccNo.setEnabled(true);
//            
////        } else {
////            ClientUtil.enableDisable(panData,false);
////            btnAccNo.setEnabled(false);
////        }
//        
//        if (observable.getTxtAmount().equals(CommonConstants.CREDIT)) {
//            fieldsEnableDisable (false);
//        } else {
//            fieldsEnableDisable (true);
//        }
//         tdtInstrumentDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtInstrumentDate()));
//         observable.ttNotifyObservers();
//         txtAccNoActionPerformed(null);
//    }         
    
    
    private void fieldsEnableDisable(boolean yesno) {
//        this.txtTokenNo.setEnabled(false);
         if(observable.getOperation()==ClientConstants.ACTIONTYPE_DELETE ||
                observable.getOperation()==ClientConstants.ACTIONTYPE_AUTHORIZE ||
                observable.getOperation()==ClientConstants.ACTIONTYPE_EXCEPTION ||
                observable.getOperation()==ClientConstants.ACTIONTYPE_REJECT ) {
                yesno=false;
         }
    }
    
    
    
    private void txtSourceFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSourceFileActionPerformed
        //        // TODO add your handling code here:
        HashMap hash = new HashMap();
        hash.put("ACT_NUM", txtSourceFile.getText());
        int oldViewType = viewType;
        viewType = ACCNO;
        fillData(hash);
        viewType = oldViewType;
    }//GEN-LAST:event_txtSourceFileActionPerformed

//    private void setDepSubNoFields(boolean val) {
//        ClientUtil.enableDisable(panLables,val);
//    }
//    
//    private void  setBtnDepSubNo(boolean val){
//        btnDailyDepSubNew.setEnabled(val);
////        btnDailyDepSubSave.setEnabled(val);
//        btnDailyDepSubDel.setEnabled(val);
//    }
    
//    private void dispContents() {
//        btnDailyDepSubNew.setEnabled(false);
//        btnDailyDepSubSave.setEnabled(true);
//        btnDailyDepSubDel.setEnabled(false);
//    }
    
    public void updateDailyInfo() {
//        lblBalance.setText(String.valueOf(observable.getBalance()));
        txtSourceFile.setText(String.valueOf(observable.getTxtAccNo()));
        
    }
        
        
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
         observable.setOperation(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        Amt=observable.getTotalGlAmt();
    }//GEN-LAST:event_btnExceptionActionPerformed
                    
    private void btnSourceFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSourceFileActionPerformed
        // Add your handling code here:
        if(rdoUploadingFile.isSelected() == false && rdoDownLoadingFile.isSelected() == false){
            ClientUtil.displayAlert("Choose file type...");
            return;            
        }else{
            txtSourceFile.setText("");
            int arrlen = 10000;
            byte[] infile = new byte[arrlen];
    //        Frame parent = new Frame();
    //        FileDialog fd = new FileDialog(this, "Please choose a file:",FileDialog.LOAD);
    //        fd.show();
            final JFileChooser objJFileChooser = new JFileChooser();
            byte[] byteArray;
            StringBuffer filePath;
            String fileName;

            if (objJFileChooser.showOpenDialog(null) == objJFileChooser.APPROVE_OPTION){
                selFile = objJFileChooser.getSelectedFile();
                filePath = new StringBuffer(selFile.getAbsolutePath());
                // read the file
                System.out.println("reading file " + filePath);
//                try {
//                    FileInputStream fis = new FileInputStream(selFile);
//                    BufferedInputStream bis = new BufferedInputStream(fis);
//                    dis = new DataInputStream(bis);
//                    try {                        
//                        int filelength = dis.read(infile);
//                        String filestring = new String(infile, 0, filelength);
                        txtSourceFile.setText(String.valueOf(selFile));
//                        System.out.println("FILE CONTENT=" + filestring);
//                    } catch(IOException iox) {
//                        System.out.println("File read error...");
//                        iox.printStackTrace();
//                    }
//                } catch (FileNotFoundException fnf) {
//                    System.out.println("File not found...");
//                    fnf.printStackTrace();
//                }
            }
        }
        
    }//GEN-LAST:event_btnSourceFileActionPerformed
            
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
        for (int i=0; i < fltDenomination.length; i++) {
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
//        txtAccNo.setEnabled(isEnable);
//        btnSourceFile.setEnabled(isEnable);
//        if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT) {
//            setEditFieldsEnable(false);
//        }
    }
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
       observable.setOperation(ClientConstants.ACTIONTYPE_REJECT);     
        // Add your handling code here:
        authorizeStatus(CommonConstants.STATUS_REJECTED);
          Amt=observable.getTotalGlAmt();
    }    private void populateInstrumentType() {//GEN-LAST:event_btnRejectActionPerformed

    }
    
    public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setAuthorizeMap(map);
//            observable.doAction();
            btnCancelActionPerformed(null);
        }
    }
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
       observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
//        observable.resetMainPan(); 
      viewType = AUTHORIZE;
       authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
       btnCancel.setEnabled(true);
       btnNew.setEnabled(false);
       btnEdit.setEnabled(false);
       btnCashPayRec.setEnabled(false);
       btnPrint.setEnabled(false);
//          observable.resetMainPan();
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    public void authorizeStatus(String authorizeStatus) {
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setStatus();
        if (viewType == AUTHORIZE && isFilled) {
//            observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
//            HashMap singleAuthorizeMap = new HashMap();
//            ArrayList arrList = new ArrayList();
//            String remarks = COptionPane.showInputDialog(this,"Authorization Remarks");
//            HashMap whereMap=new HashMap();
////            TxTransferTO transTo=new TxTransferTO();
//            whereMap.put("BATCHID",lblTransactionIDDesc1.getText());
//            arrList=(ArrayList)ClientUtil.executeQuery("getBatchTxTransferTOs", whereMap);
////            arrList.add(transTo);
//            cboAgentType.setSelectedItem(observable.getCboAgentType());
//            tdtInstrumentDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtInstrumentDate()));
//            
//            singleAuthorizeMap.put("BATCH_ID", lblTransactionIDDesc1.getText());
//            singleAuthorizeMap.put("REMARKS", remarks);
//            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
//            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
//            authorize(singleAuthorizeMap);
//            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
//            super.setOpenForEditBy(observable.getStatusBy());
//            super.removeEditLock(lblTransactionIDDesc1.getText());
//            isFilled = false;
        } else {
            Amt=0.0;
            observable.resetMainPan(); 
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getSelectInsertedRecords");
            HashMap whereParam = new HashMap();
            whereParam.put("USER_ID", ProxyParameters.USER_ID);
            whereParam.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereParam);
            viewType = AUTHORIZE;
            isFilled = true;
            AuthorizeWFUI authorizeUI = new AuthorizeWFUI(this, mapParam);
            authorizeUI.show();
            lblStatus.setText(observable.getLblStatus());
            setButtonEnableDisable();   
        }
        ClientUtil.enableDisable(this, false);  // Disables the panel...
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
    }
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        observable.resetOBFields(); 
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        System.out.println("Came in Print Button Click");
        HashMap reportParamMap = new HashMap();
        System.out.println("Screen ID in UI "+getScreenID());
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
            
    }//GEN-LAST:event_btnPrintActionPerformed
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        transDetails.setTransDetails(null,null,null);
       observable.setTdtInstrumentDate(DateUtil.getDateMMDDYYYY(""));
        observable.resetForm();                 // Reset the fields in the UI to null...
        observable.resetOBFields();
//        observable.resetLable();
        initComponentData();
//        observable.resetDepSubNoTbl();
        ClientUtil.enableDisable(this, false);  // Disables the panel...
        setModified(false);
        setButtonEnableDisable();               // Enables or Disables the buttons and menu Items depending on their previous state...
        if (observable.getActionType()!=ClientConstants.ACTIONTYPE_AUTHORIZE){
            observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);//Sets the Action Type to be performed...
        }
        observable.setStatus();                 // To set the Value of lblStatus...
        viewType = -1;
        resetForm();
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);        
        btnDelete.setEnabled(true);
        btnCashPayRec.setEnabled(true);
        btnPrint.setEnabled(true);
        btnSourceFile.setEnabled(false);
        btnDestinationFile.setEnabled(false);
        lblStatus.setText(observable.getLblStatus());

    }//GEN-LAST:event_btnCancelActionPerformed
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
//        observable.setTotalGlAmt(CommonUtil.convertObjToDouble(lblTotAmtVal.getText()).doubleValue());
//        System.out.println("observable.getTotalGlAmt"+observable.getTotalGlAmt());
//        updateOBFields();
//        StringBuffer mandatoryMessage = new StringBuffer();
//        mandatoryMessage.append(new MandatoryCheck().checkMandatory(getClass().getName(), panCashTransaction));
//       
//        observable.doAction();     
//         HashMap lockMap = new HashMap();
//        ArrayList lst = new ArrayList();
//        lst.add("BATCH_ID");//TRANS_ID
//        lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
//        if (observable.getProxyReturnMap()!=null) {
//            if (observable.getProxyReturnMap().containsKey("TRANS_ID")) {
//                lockMap.put("BATCH_ID", observable.getProxyReturnMap().get("TRANS_ID"));
//            }
//        }
//          if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT) {
//            lockMap.put("BATCH_ID", lblTransactionIDDesc1.getText());
//        }
//         observable.setTotalGlAmt(CommonUtil.convertObjToDouble(lblTotAmtVal.getText()).doubleValue());
//        setEditLockMap(lockMap);
//        setEditLock();
//        // To perform the necessary operation depending on the Action type...
//        observable.resetForm();             // Reset the fields in the UI to null...
//        observable.resetLable();            // Reset the Editable Lables in the UI to null...
////        observable.resetMainPan();
////        setEditFieldsEnable(false);
////        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE)
////            enableDisableButtons(false);        // To disable the buttons(folder) in the UI...
//        ClientUtil.enableDisable(this, false);// Disables the panel...
////        setButtonEnableDisable();           // Enables or Disables the buttons and menu Items depending on their previous state...
//        observable.setResultStatus();// To Reset the Value of lblStatus...
//        enableDisableButtons(false);
//        setModified(false);
//        observable.resetOBFields();
//        //        this.disableButtons();
//        viewType = -1;
//        lblTransactionDateDesc1.setText("");
//        lblTransactionIDDesc1.setText("");
//        lblTotAmtVal.setText("");
//        tdtInstrumentDate.setDateValue("");
//        lblAgNmVal.setText("");
        btnCancelActionPerformed(null);
    }//GEN-LAST:event_btnSaveActionPerformed
                                    
    private void displayAlert(String message){
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
       
        if (ClientUtil.deleteAlert()==0) {
            observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);//Sets the Action Type to be performed...
            btnSaveActionPerformed(evt);
        }
        observable.resetForm();                 // Reset the fields in the UI to null...
//        observable.resetLable();                // Reset the Editable Lables in the UI to null...
        popUp(DELETE);
        Amt=observable.getTotalGlAmt();
    }//GEN-LAST:event_btnDeleteActionPerformed
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
//        Add your handling code here:
        observable.setOperation(ClientConstants.ACTIONTYPE_EDIT);
//          observable.resetMainPan();
//        observable.resetForm();                 // Reset the fields in the UI to null...
//        observable.resetLable();                // Reset the Editable Lables in the UI to null...
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);//Sets the Action Type to be performed...
        popUp(EDIT);
//        btnCancel.setEnabled(true);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnCashPayRec.setEnabled(false);
        btnPrint.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        lblStatus.setText(observable.getLblStatus());
//        btnDailyDepSubDel.setEnabled(true);
//        btnDailyDepSubNew.setEnabled(true);
//        btnDailyDepSubSave.setEnabled(true);
//        ClientUtil.enableDisable(panData, true);
//        cboAgentType.setEnabled(false);
//        tdtInstrumentDate.setEnabled(false);
        
//        rowForEdit = observable.getData(transactionIdForEdit);             
        
//        this.populateUIData(ClientConstants.ACTIONTYPE_EDIT);        
//        observable.setTransStatus(CommonConstants.STATUS_MODIFIED);  
        System.out.println("observable.getTdtInstrumentDate()))"+observable.getTdtInstrumentDate());
//        tdtInstrumentDate.setDateValue(DateUtil.getStringDate(observable.getTdtInstrumentDate()));
    }//GEN-LAST:event_btnEditActionPerformed
    // To display the All the Product Id's which r having status as
    // created or updated, in a table...
    private void popUp(int field) {
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        viewType = field;
        if(field==EDIT || field==DELETE ){      //Edit=0 and Delete=1
            HashMap resultMap = new HashMap();
            HashMap where=new HashMap();
             ArrayList lst = new ArrayList();
            lst.add("BATCH_ID");//trans_id
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            resultMap.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectInsertedRecords");//mapped statement: viewCashTransaction---> result map should be a Hashmap...
            viewMap.put(CommonConstants.MAP_WHERE, resultMap);
        }else{
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
                observable.resetMainPan(); 
            }
            updateOBFields();
            HashMap resultMap = new HashMap();
            HashMap where=new HashMap();
//            resultMap.put("AGENT_ID", ((ComboBoxModel)cboAgentType.getModel()).getKeyForSelected().toString());
            resultMap.put("AGENT_ID" ,observable.getCboAgentType());
            resultMap.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
            System.out.println("######AgentDetails :" +resultMap);
            viewMap.put(CommonConstants.MAP_NAME, "getDepositDetailsForDaily");
            viewMap.put(CommonConstants.MAP_WHERE, resultMap);
        }
        new ViewAll(this, viewMap).show();
    }
    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        HashMap hash = new HashMap();
        hash = (HashMap) param;
        if (viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE) {
             
            setModified(true);
//            isFilled = true;
            if(viewType==EDIT){
                hash.put("SETLLEMENT_DT", hash.get("SETLLEMENT_DT"));
            }else if(viewType==AUTHORIZE){
                Date setDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("SETLLEMENT_DT")));
                Date currDt = ClientUtil.getCurrentDate();
                if(setDate!=null){
                    currDt.setDate(setDate.getDate());
                    currDt.setMonth(setDate.getMonth());
                    currDt.setYear(setDate.getYear());
                    hash.put("SETLLEMENT_DT", currDt);
                }
            }
            log.info("Hash: " + hash);
            System.out.println("hash@@@@@@"+hash);
            observable.populateData(hash);// Called to display the Data in the UI fields...
            observable.ttNotifyObservers();
            btnAddDebit.setEnabled(false);
            btnProcess.setEnabled(false);
            ClientUtil.enableDisable(this, false);// Disables the panel...
            this.btnDelete.setEnabled(true);
            setButtonEnableDisable();
            observable.setStatus();             // To set the Value of lblStatus...
            if(viewType==AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getOperation() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
        } else if (viewType==ACCNO) {
                String ACCOUNTNO = CommonUtil.convertObjToStr(hash.get("ACT_NUM"));        
//                observable.setLblAccName(CommonUtil.convertObjToStr(hash.get("CUSTOMER_NAME")));
                observable.setAccountName(ACCOUNTNO);
//                observable.setTxtAccNo(ACCOUNTNO);
//                observable.setBalance(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
                System.out.println("#######AgentDetailsForDeposits : "+hash);
//                lblBalance.setText(observable.getBalance());
                observable.ttNotifyObservers();
//                lblCustnmVal.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER_NAME")));
//                lblBalanceVal.setText(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
            } 
        }
    
    //To enable and/or Disable buttons(folder)...
//    private void enableDisableButtons(boolean yesno) {
//        btnSourceFile.setEnabled(yesno);
//        btnDenomination.setEnabled(yesno);
//        txtInitiatorChannel.setEnabled(false);
//    }
    
//    private void newSaveEnable() {
//        btnDailyDepSubNew.setEnabled(true);
//        btnDailyDepSubSave.setEnabled(true);
//        btnDailyDepSubDel.setEnabled(true);
//        ClientUtil.enableDisable(panData, false);
//        btnDenomination.setEnabled(false);
//    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
//        observable.resetMainPan();
//        observable.setOperation(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();                 // Reset the fields in the UI to null...
        ClientUtil.enableDisable(this, true);   // Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);//Sets the Action Type to be performed...
//        observable.setOperation(ClientConstants.ACTIONTYPE_NEW);
//        observable.setTransStatus(CommonConstants.STATUS_CREATED);
        setButtonEnableDisable();               // Enables or Disables the buttons and menu Items depending on their previous state...
//        observable.setStatus(); 
//        newSaveEnable();// To set the Value of lblStatus...
//        textDisable();               
        setModified(true);
        btnAddDebit.setEnabled(true);
        btnProcess.setEnabled(true);
        btnSourceFile.setEnabled(true);
        btnDestinationFile.setEnabled(true);
        btnProcess.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnCashPayRec.setEnabled(false);
        btnPrint.setEnabled(false);
//        cboAgentType.setEnabled(true);
//        tdtInstrumentDate.setEnabled(true);
//        lblTransactionDateDesc1.setText(DateUtil.getStringDate(curDate));
//        lblInitiatorIDDesc1.setText("");
//        lblTransactionIDDesc1.setText("");
//        lblTotAmtVal.setText("");
    }//GEN-LAST:event_btnNewActionPerformed
    // To set the Text Boxes for Account No. and InitiatorChannel, nonEditable...
//    private void textEnable(){
//        txtAccNo.setEnabled(true);             //To make this textBox non editable...
//        txtAccNo.setEditable(true);
//        txtAmount.setEnabled(true);
//        txtAmount.setEditable(true);
//    }
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
/**
* @param args the command line arguments
                            **/
public static void main(String args[]) throws Exception {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }

        javax.swing.JFrame jf = new javax.swing.JFrame();
//        DailyDepositTransUI gui = new DailyDepositTransUI();
//        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
//        gui.show();
       
}    
//       private boolean checkAccNum(){
//           System.out.println("txtAccNo"+txtAccNo.getText());
////        System.out.println("tblDailyDepositList.getValueAt(i,0)))"+tblDailyDepositList.getValueAt(0,0));
//        System.out.println("tblDailyDepositList.getRowCount()"+tblDailyDepositList.getRowCount());
//        if(tblDailyDepositList.getRowCount()>0){
//        for(int i=0;i<tblDailyDepositList.getRowCount();i++){
//            if(txtAccNo.getText().equals(CommonUtil.convertObjToStr(tblDailyDepositList.getValueAt(i,0)))){
//                 System.out.println("tblDailyDepositList.getValueAt(i,0)))"+tblDailyDepositList.getValueAt(i,0));
////                   ClientUtil.showMessageWindow(resourceBundle.getString("AlreadyAdd"));
//               return true;
//            }
//                
//        }}
//        return false;
//       }
//       private double totalAmt(){
//           double totAmt=0.0;
//            if(tblDailyDepositList.getRowCount()>0){
//        for(int i=0;i<tblDailyDepositList.getRowCount();i++){
//           totAmt= totAmt+CommonUtil.convertObjToDouble(tblDailyDepositList.getValueAt(i,1)).doubleValue();
//           System.out.println("totAmt$$$$$$$$$$$$$$$"+totAmt);
//            
//       }
//            }
//       return totAmt;
//       }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAddDebit;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnCashPayRec;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDestinationFile;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSourceFile;
    private com.see.truetransact.uicomponent.CLabel lblCheckSum;
    private com.see.truetransact.uicomponent.CLabel lblCheckSumValue;
    private com.see.truetransact.uicomponent.CLabel lblDestinationFile;
    private com.see.truetransact.uicomponent.CLabel lblECSItemNo;
    private com.see.truetransact.uicomponent.CLabel lblECSItemNoValue;
    private com.see.truetransact.uicomponent.CLabel lblECSTapeInputNo;
    private com.see.truetransact.uicomponent.CLabel lblECSTapeInputNoValue;
    private com.see.truetransact.uicomponent.CLabel lblFilter;
    private com.see.truetransact.uicomponent.CLabel lblFilterValue;
    private com.see.truetransact.uicomponent.CLabel lblLedgerFolioNo;
    private com.see.truetransact.uicomponent.CLabel lblLedgerFolioNoValue;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSettlementDate;
    private com.see.truetransact.uicomponent.CLabel lblSettlementDateValue;
    private com.see.truetransact.uicomponent.CLabel lblSourceFile;
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
    private com.see.truetransact.uicomponent.CLabel lblSponsor;
    private com.see.truetransact.uicomponent.CLabel lblSponsorValue;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmountValue;
    private com.see.truetransact.uicomponent.CLabel lblTransactionType;
    private com.see.truetransact.uicomponent.CLabel lblTransactionTypeValue;
    private com.see.truetransact.uicomponent.CLabel lblTypeofFile;
    private com.see.truetransact.uicomponent.CLabel lblUserBankAcNo;
    private com.see.truetransact.uicomponent.CLabel lblUserBankAcNoValue;
    private com.see.truetransact.uicomponent.CLabel lblUserCreditRefNo;
    private com.see.truetransact.uicomponent.CLabel lblUserCreditRefNoValue;
    private com.see.truetransact.uicomponent.CLabel lblUserLimit;
    private com.see.truetransact.uicomponent.CLabel lblUserLimitValue;
    private com.see.truetransact.uicomponent.CLabel lblUserName;
    private com.see.truetransact.uicomponent.CLabel lblUserNameValue;
    private com.see.truetransact.uicomponent.CLabel lblUserNumber;
    private com.see.truetransact.uicomponent.CLabel lblUserNumberValue;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panClearingDataFile;
    private com.see.truetransact.uicomponent.CPanel panClearingDataImport;
    private com.see.truetransact.uicomponent.CPanel panClearingDataTrans;
    private com.see.truetransact.uicomponent.CPanel panClearingDataTransTable;
    private com.see.truetransact.uicomponent.CPanel panDestinationFile;
    private com.see.truetransact.uicomponent.CPanel panInfoClearingDataTransTable;
    private com.see.truetransact.uicomponent.CPanel panSourceFile;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransactionType;
    private com.see.truetransact.uicomponent.CRadioButton rdoDownLoadingFile;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransactionType;
    private com.see.truetransact.uicomponent.CRadioButton rdoUploadingFile;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srpClearingDataTransTable;
    private com.see.truetransact.uicomponent.CTabbedPane tabClearingDataImport;
    private com.see.truetransact.uicomponent.CTable tblClearingDataTransTable;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CTextField txtDestinationFile;
    private com.see.truetransact.uicomponent.CTextField txtSourceFile;
    // End of variables declaration//GEN-END:variables
}