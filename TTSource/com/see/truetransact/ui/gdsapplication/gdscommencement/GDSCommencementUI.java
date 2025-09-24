  /*
   * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
   *
   * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
   * 
   * GDSCommencementUI.java
   *
   * Created on November 26, 2003, 11:27 AM
   */
package com.see.truetransact.ui.gdsapplication.gdscommencement;

/**
 *
 * @author Nithya
 *
 **/
import com.see.truetransact.ui.mdsapplication.mdsconmmencement.*;
import java.util.*;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uicomponent.CComboBox;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import javax.swing.*;


public class GDSCommencementUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField{
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.gdsapplication.gdscommencement.GDSCommencementRB", ProxyParameters.LANGUAGE);
    final GDSCommencementMRB objMandatoryMRB = new GDSCommencementMRB();
    TransactionUI transactionUI = new TransactionUI();
    private HashMap mandatoryMap;
    private GDSCommencementOB observable;
    private String viewType = "";
    private final String AUTHORIZE="AUTHORIZE";
    private boolean isFilled = false;
    private int PAN=-1,COMMENCEMENT=0,CLOSURE=1;
    private HashMap MDSClosureMap = new HashMap();
    private Date currDt = null;
    /** Creates new form BeanForm */
    public GDSCommencementUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        settingupUI();
        tabRemittanceProduct.resetVisits();
    }
    
    private void settingupUI(){
        setObservable();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        initComponentData();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panInsideGeneralRemittance);
        setMaximumLength();
        setHelpMessage();
        setButtonEnableDisable();
        ClientUtil.enableDisable(this,false);
        panInsideGeneralRemittance1.add(transactionUI);
        transactionUI.setSourceScreen("MDS_COMMENCEMENT");
        txtSchemeName.setEnabled(false);
        tdtStartDt.setEnabled(false);
        txtInstAmt.setEnabled(false);
        txtTotAmt.setEnabled(false);
        btnDelete.setEnabled(true);
        closureButtonsEnableDisable(false);
        ClientUtil.enableDisable(panMDSClosingDetails,false);
        panMDSClosingDetails.setVisible(false);
    }
    
    private void closureButtonsEnableDisable(boolean flag){
        btnProcess.setEnabled(flag);
        btnClear.setEnabled(flag);
        btnSchemeNameClosure.setEnabled(flag);
    }
    
    private void setObservable() {
        observable = GDSCommencementOB.getInstance();
        observable.setTransactionOB(transactionUI.getTransactionOB());
        observable.addObserver(this);
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
        btnSchemeName.setName("btnSchemeName");
        btnView.setName("btnView");
        lblInstAmt.setName("lblInstAmt");
        lblMsg.setName("lblMsg");
        lblSchemeName.setName("lblSchemeName");
        lblSpace.setName("lblSpace");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        lblStartDt.setName("lblStartDt");
        lblStatus.setName("lblStatus");
        lblTotAmt.setName("lblTotAmt");
        mbrMain.setName("mbrMain");
        panCommencement.setName("panCommencement");
        panGeneralRemittance.setName("panGeneralRemittance");
        panInsideGeneralRemittance.setName("panInsideGeneralRemittance");
        panInsideGeneralRemittance1.setName("panInsideGeneralRemittance1");
        panSchemeName.setName("panSchemeName");
        panStatus.setName("panStatus");
        srpCommencement.setName("srpCommencement");
        tabRemittanceProduct.setName("tabRemittanceProduct");
        tblCommencement.setName("tblCommencement");
        tdtStartDt.setName("tdtStartDt");
        txtInstAmt.setName("txtInstAmt");
        txtSchemeName.setName("txtSchemeName");
        txtTotAmt.setName("txtTotAmt");
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
       // resourceBundle = new GDSCommencementRB();
        lblInstAmt.setText(resourceBundle.getString("lblInstAmt"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnView.setText(resourceBundle.getString("btnView"));
        lblSchemeName.setText(resourceBundle.getString("lblSchemeName"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblTotAmt.setText(resourceBundle.getString("lblTotAmt"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnSchemeName.setText(resourceBundle.getString("btnSchemeName"));
        lblStartDt.setText(resourceBundle.getString("lblStartDt"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }
    
    
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        GDSCommencementMRB objMandatoryRB = new GDSCommencementMRB();
        tdtStartDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtStartDt"));
        txtInstAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstAmt"));
        txtSchemeName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSchemeName"));
        txtTotAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotAmt"));
    }
    
    
    /* Auto Generated Method - setMandatoryHashMap()
     
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("tdtStartDt", new Boolean(true));
        mandatoryMap.put("txtInstAmt", new Boolean(true));
        mandatoryMap.put("txtSchemeName", new Boolean(true));
        mandatoryMap.put("txtTotAmt", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void initComponentData() {
        try{
            
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
    private void setMaximumLength(){
        txtInstAmt.setValidation(new CurrencyValidation());
        txtTotAmt.setValidation(new CurrencyValidation());
        txtInstAmtClosure.setValidation(new CurrencyValidation());
        txtTotalAmtReceived.setValidation(new CurrencyValidation());
        txtTotalAmtPaid.setValidation(new CurrencyValidation());
        txtTotalBonusAmount.setValidation(new CurrencyValidation());
        txtTotalBonusAvailable.setValidation(new CurrencyValidation());
        txtTotalBonusPaid.setValidation(new CurrencyValidation());
        txtTotalUnpaidBonus.setValidation(new CurrencyValidation());
        txtTotalPendingPrizedAmt.setValidation(new CurrencyValidation());
        txtTotalPendingCommission.setValidation(new CurrencyValidation());
        txtTotalInsReceivable.setValidation(new CurrencyValidation());
        txtTotalSchemeAmount.setValidation(new CurrencyValidation());
    }
    
    /** Auto Generated Method - update()
     * This method called by Observable. It updates the UI with
     * Observable's data. If needed add/Remove RadioButtons
     * method need to be added.*/
    public void update(Observable observed, Object arg) {
        //        removeRadioButtons();
        tblCommencement.setModel(observable.getTblCommencement());
        txtSchemeName.setText(observable.getTxtSchemeName());
        tdtStartDt.setDateValue(observable.getTdtStartDt());
        txtInstAmt.setText(observable.getTxtInstAmt());
        txtTotAmt.setText(observable.getTxtTotAmt());
        lblStatus.setText(observable.getLblStatus());
        tdtCommencementDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtCommencementDate()));
        //        addRadioButtons();
    }
    
    /** Auto Generated Method - updateOBFields()
     * This method called by Save option of UI.
     * It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setTxtSchemeName(txtSchemeName.getText());
        observable.setTdtStartDt(tdtStartDt.getDateValue());
        observable.setTxtInstAmt(txtInstAmt.getText());
        observable.setTxtTotAmt(txtTotAmt.getText());
        observable.setTdtCommencementDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtCommencementDate.getDateValue())));
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
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace76 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace77 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace78 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabRemittanceProduct = new com.see.truetransact.uicomponent.CTabbedPane();
        panGeneralRemittance = new com.see.truetransact.uicomponent.CPanel();
        panInsideGeneralRemittance = new com.see.truetransact.uicomponent.CPanel();
        lblInstAmt = new com.see.truetransact.uicomponent.CLabel();
        tdtStartDt = new com.see.truetransact.uicomponent.CDateField();
        lblStartDt = new com.see.truetransact.uicomponent.CLabel();
        lblSchemeName = new com.see.truetransact.uicomponent.CLabel();
        txtInstAmt = new com.see.truetransact.uicomponent.CTextField();
        panSchemeName = new com.see.truetransact.uicomponent.CPanel();
        txtSchemeName = new com.see.truetransact.uicomponent.CTextField();
        btnSchemeName = new com.see.truetransact.uicomponent.CButton();
        panCommencement = new com.see.truetransact.uicomponent.CPanel();
        srpCommencement = new com.see.truetransact.uicomponent.CScrollPane();
        tblCommencement = new com.see.truetransact.uicomponent.CTable();
        txtTotAmt = new com.see.truetransact.uicomponent.CTextField();
        lblTotAmt = new com.see.truetransact.uicomponent.CLabel();
        tdtCommencementDate = new com.see.truetransact.uicomponent.CDateField();
        lblStartDt1 = new com.see.truetransact.uicomponent.CLabel();
        panInsideGeneralRemittance1 = new com.see.truetransact.uicomponent.CPanel();
        panMDSClosureDetails = new com.see.truetransact.uicomponent.CPanel();
        panInsideMDSClosureDetails = new com.see.truetransact.uicomponent.CPanel();
        panSchemeNameClosure = new com.see.truetransact.uicomponent.CPanel();
        txtSchemeNameClosure = new com.see.truetransact.uicomponent.CTextField();
        btnSchemeNameClosure = new com.see.truetransact.uicomponent.CButton();
        txtInstAmtClosure = new com.see.truetransact.uicomponent.CTextField();
        lblSchemeNameClosure = new com.see.truetransact.uicomponent.CLabel();
        lblStartDtClosure = new com.see.truetransact.uicomponent.CLabel();
        tdtStartDtClosure = new com.see.truetransact.uicomponent.CDateField();
        lblInstAmtClosure = new com.see.truetransact.uicomponent.CLabel();
        tdtEndDtClosure = new com.see.truetransact.uicomponent.CDateField();
        lblEndDtClosure = new com.see.truetransact.uicomponent.CLabel();
        panProcess = new com.see.truetransact.uicomponent.CPanel();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        panMDSClosingDetails = new com.see.truetransact.uicomponent.CPanel();
        txtTotalAmtReceived = new com.see.truetransact.uicomponent.CTextField();
        lblTotalAmtReceived = new com.see.truetransact.uicomponent.CLabel();
        lblTotalBonusAvailable = new com.see.truetransact.uicomponent.CLabel();
        lblTotalAmtPaid = new com.see.truetransact.uicomponent.CLabel();
        lblTotalUnpaidBonus = new com.see.truetransact.uicomponent.CLabel();
        txtTotalBonusAvailable = new com.see.truetransact.uicomponent.CTextField();
        txtTotalUnpaidBonus = new com.see.truetransact.uicomponent.CTextField();
        txtTotalAmtPaid = new com.see.truetransact.uicomponent.CTextField();
        lblTotalPendingPrizedAmt = new com.see.truetransact.uicomponent.CLabel();
        lblTotalInsReceivable = new com.see.truetransact.uicomponent.CLabel();
        lblTotalBonusPaid = new com.see.truetransact.uicomponent.CLabel();
        txtTotalPendingPrizedAmt = new com.see.truetransact.uicomponent.CTextField();
        txtTotalInsReceivable = new com.see.truetransact.uicomponent.CTextField();
        txtTotalBonusPaid = new com.see.truetransact.uicomponent.CTextField();
        lblTotalPendingCommission = new com.see.truetransact.uicomponent.CLabel();
        txtTotalPendingCommission = new com.see.truetransact.uicomponent.CTextField();
        lblTotalSchemeAmount = new com.see.truetransact.uicomponent.CLabel();
        txtTotalSchemeAmount = new com.see.truetransact.uicomponent.CTextField();
        lblTotalBonusAmount = new com.see.truetransact.uicomponent.CLabel();
        txtTotalBonusAmount = new com.see.truetransact.uicomponent.CTextField();
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
        setMinimumSize(new java.awt.Dimension(850, 650));
        setPreferredSize(new java.awt.Dimension(850, 650));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(29, 27));
        btnView.setPreferredSize(new java.awt.Dimension(29, 27));
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

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace73);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace74);

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

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace75);

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

        lblSpace76.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace76.setText("     ");
        lblSpace76.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace76);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace77.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace77.setText("     ");
        lblSpace77.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace77);

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

        lblSpace78.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace78.setText("     ");
        lblSpace78.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace78.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace78.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace78);

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

        tabRemittanceProduct.setMinimumSize(new java.awt.Dimension(680, 480));
        tabRemittanceProduct.setPreferredSize(new java.awt.Dimension(680, 480));

        panGeneralRemittance.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panGeneralRemittance.setMinimumSize(new java.awt.Dimension(850, 450));
        panGeneralRemittance.setPreferredSize(new java.awt.Dimension(850, 450));
        panGeneralRemittance.setLayout(new java.awt.GridBagLayout());

        panInsideGeneralRemittance.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInsideGeneralRemittance.setMinimumSize(new java.awt.Dimension(850, 300));
        panInsideGeneralRemittance.setPreferredSize(new java.awt.Dimension(850, 300));
        panInsideGeneralRemittance.setLayout(new java.awt.GridBagLayout());

        lblInstAmt.setText("Installment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideGeneralRemittance.add(lblInstAmt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panInsideGeneralRemittance.add(tdtStartDt, gridBagConstraints);

        lblStartDt.setText("GDS Start Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideGeneralRemittance.add(lblStartDt, gridBagConstraints);

        lblSchemeName.setText("GDS Group Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideGeneralRemittance.add(lblSchemeName, gridBagConstraints);

        txtInstAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideGeneralRemittance.add(txtInstAmt, gridBagConstraints);

        panSchemeName.setLayout(new java.awt.GridBagLayout());

        txtSchemeName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSchemeName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSchemeNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSchemeName.add(txtSchemeName, gridBagConstraints);

        btnSchemeName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSchemeName.setEnabled(false);
        btnSchemeName.setMinimumSize(new java.awt.Dimension(28, 28));
        btnSchemeName.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSchemeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSchemeNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSchemeName.add(btnSchemeName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInsideGeneralRemittance.add(panSchemeName, gridBagConstraints);

        panCommencement.setMinimumSize(new java.awt.Dimension(770, 340));
        panCommencement.setPreferredSize(new java.awt.Dimension(770, 340));
        panCommencement.setLayout(new java.awt.GridBagLayout());

        srpCommencement.setMinimumSize(new java.awt.Dimension(760, 220));
        srpCommencement.setPreferredSize(new java.awt.Dimension(760, 220));

        tblCommencement.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Application No", "Application Date", "Member Name", "Munnal", "Thalayal"
            }
        ));
        tblCommencement.setMaximumSize(new java.awt.Dimension(2147483647, 64));
        tblCommencement.setMinimumSize(new java.awt.Dimension(60, 64));
        tblCommencement.setPreferredScrollableViewportSize(new java.awt.Dimension(754, 314));
        tblCommencement.setPreferredSize(new java.awt.Dimension(550, 2000));
        srpCommencement.setViewportView(tblCommencement);

        panCommencement.add(srpCommencement, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(panCommencement, gridBagConstraints);

        txtTotAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideGeneralRemittance.add(txtTotAmt, gridBagConstraints);

        lblTotAmt.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideGeneralRemittance.add(lblTotAmt, gridBagConstraints);

        tdtCommencementDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtCommencementDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panInsideGeneralRemittance.add(tdtCommencementDate, gridBagConstraints);

        lblStartDt1.setText("MDS Commencement Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideGeneralRemittance.add(lblStartDt1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGeneralRemittance.add(panInsideGeneralRemittance, gridBagConstraints);

        panInsideGeneralRemittance1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInsideGeneralRemittance1.setMinimumSize(new java.awt.Dimension(850, 250));
        panInsideGeneralRemittance1.setPreferredSize(new java.awt.Dimension(850, 250));
        panInsideGeneralRemittance1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGeneralRemittance.add(panInsideGeneralRemittance1, gridBagConstraints);

        tabRemittanceProduct.addTab("Commencement Details", panGeneralRemittance);

        panMDSClosureDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMDSClosureDetails.setMinimumSize(new java.awt.Dimension(850, 450));
        panMDSClosureDetails.setPreferredSize(new java.awt.Dimension(850, 450));
        panMDSClosureDetails.setLayout(new java.awt.GridBagLayout());

        panInsideMDSClosureDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Scheme Details"));
        panInsideMDSClosureDetails.setMinimumSize(new java.awt.Dimension(850, 150));
        panInsideMDSClosureDetails.setPreferredSize(new java.awt.Dimension(650, 150));
        panInsideMDSClosureDetails.setLayout(new java.awt.GridBagLayout());

        panSchemeNameClosure.setLayout(new java.awt.GridBagLayout());

        txtSchemeNameClosure.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSchemeNameClosure.add(txtSchemeNameClosure, gridBagConstraints);

        btnSchemeNameClosure.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSchemeNameClosure.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSchemeNameClosure.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSchemeNameClosure.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSchemeNameClosure.setEnabled(false);
        btnSchemeNameClosure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSchemeNameClosureActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSchemeNameClosure.add(btnSchemeNameClosure, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideMDSClosureDetails.add(panSchemeNameClosure, gridBagConstraints);

        txtInstAmtClosure.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideMDSClosureDetails.add(txtInstAmtClosure, gridBagConstraints);

        lblSchemeNameClosure.setText("MDS Scheme Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideMDSClosureDetails.add(lblSchemeNameClosure, gridBagConstraints);

        lblStartDtClosure.setText("MDS Start Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 45, 2, 2);
        panInsideMDSClosureDetails.add(lblStartDtClosure, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideMDSClosureDetails.add(tdtStartDtClosure, gridBagConstraints);

        lblInstAmtClosure.setText("Installment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideMDSClosureDetails.add(lblInstAmtClosure, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideMDSClosureDetails.add(tdtEndDtClosure, gridBagConstraints);

        lblEndDtClosure.setText("MDS End Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 45, 2, 2);
        panInsideMDSClosureDetails.add(lblEndDtClosure, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSClosureDetails.add(panInsideMDSClosureDetails, gridBagConstraints);

        panProcess.setMinimumSize(new java.awt.Dimension(780, 60));
        panProcess.setPreferredSize(new java.awt.Dimension(780, 60));
        panProcess.setLayout(new java.awt.GridBagLayout());

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("CLEAR");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(29, 8, 4, 4);
        panProcess.add(btnClear, gridBagConstraints);

        btnProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnProcess.setText("PROCESS");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(29, 4, 4, 4);
        panProcess.add(btnProcess, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panMDSClosureDetails.add(panProcess, gridBagConstraints);

        panMDSClosingDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panMDSClosingDetails.setMinimumSize(new java.awt.Dimension(500, 270));
        panMDSClosingDetails.setPreferredSize(new java.awt.Dimension(500, 270));
        panMDSClosingDetails.setLayout(new java.awt.GridBagLayout());

        txtTotalAmtReceived.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(txtTotalAmtReceived, gridBagConstraints);

        lblTotalAmtReceived.setText("Total Amount Received From Chittals Including Bonus");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(lblTotalAmtReceived, gridBagConstraints);

        lblTotalBonusAvailable.setText("Total Bonus Available");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(lblTotalBonusAvailable, gridBagConstraints);

        lblTotalAmtPaid.setText("Total Amount Paid To Chittals Including Bonus");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(lblTotalAmtPaid, gridBagConstraints);

        lblTotalUnpaidBonus.setText("Total Unpaid Bonus");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(lblTotalUnpaidBonus, gridBagConstraints);

        txtTotalBonusAvailable.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(txtTotalBonusAvailable, gridBagConstraints);

        txtTotalUnpaidBonus.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(txtTotalUnpaidBonus, gridBagConstraints);

        txtTotalAmtPaid.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(txtTotalAmtPaid, gridBagConstraints);

        lblTotalPendingPrizedAmt.setText("Total Pending Prized Amount To Customers");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(lblTotalPendingPrizedAmt, gridBagConstraints);

        lblTotalInsReceivable.setText("Total Installment Receivable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(lblTotalInsReceivable, gridBagConstraints);

        lblTotalBonusPaid.setText("Total Bonus Paid To Chittals");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(lblTotalBonusPaid, gridBagConstraints);

        txtTotalPendingPrizedAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(txtTotalPendingPrizedAmt, gridBagConstraints);

        txtTotalInsReceivable.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(txtTotalInsReceivable, gridBagConstraints);

        txtTotalBonusPaid.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(txtTotalBonusPaid, gridBagConstraints);

        lblTotalPendingCommission.setText("Total Pending Commission");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(lblTotalPendingCommission, gridBagConstraints);

        txtTotalPendingCommission.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(txtTotalPendingCommission, gridBagConstraints);

        lblTotalSchemeAmount.setText("Total Scheme Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(lblTotalSchemeAmount, gridBagConstraints);

        txtTotalSchemeAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(txtTotalSchemeAmount, gridBagConstraints);

        lblTotalBonusAmount.setText("Total Bonus Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(lblTotalBonusAmount, gridBagConstraints);

        txtTotalBonusAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSClosingDetails.add(txtTotalBonusAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSClosureDetails.add(panMDSClosingDetails, gridBagConstraints);

        tabRemittanceProduct.addTab("Closure Details", panMDSClosureDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabRemittanceProduct, gridBagConstraints);

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
    
    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        try {
            if(MDSClosureMap == null){
                MDSClosureMap = new HashMap();
            }
            btnProcessActionPerform();
            double totalAmount = 0.0;
            double totalDebit = 0.0;
            double totalCredit = 0.0;
            double debitMDSLiability = 0.0;
            double debitMDSReceivable = 0.0;
            double creditMDSAssets = 0.0;
            double creditMDSPayable = 0.0;
            //Bonus
            double totalBonusAmount = 0.0;
            double totalDebitBonus = 0.0;
            double totalCreditBonus = 0.0;
            double debitBonusPayable = 0.0;
            double debitSundryReceipts = 0.0;
            double creditBonusReceivable = 0.0;
            double creditSundryPayment = 0.0;

            totalAmount = CommonUtil.convertObjToDouble(txtTotalSchemeAmount.getText()).doubleValue();
            if(totalAmount>0){
                HashMap whereMap = new HashMap();
                //DEBIT_CALCULATION
                if(totalAmount == CommonUtil.convertObjToDouble(txtTotalAmtReceived.getText()).doubleValue()){
                    totalDebit = totalAmount;
                }else{
                    debitMDSLiability = CommonUtil.convertObjToDouble(txtTotalAmtReceived.getText()).doubleValue();
                    debitMDSReceivable = totalAmount - debitMDSLiability;
                }
                //CREDIT_CALCULATION
                if(totalAmount == CommonUtil.convertObjToDouble(txtTotalAmtPaid.getText()).doubleValue()){
                    totalCredit = totalAmount;
                }else{
                    creditMDSAssets = CommonUtil.convertObjToDouble(txtTotalAmtPaid.getText()).doubleValue();
                    creditMDSPayable = totalAmount - creditMDSAssets;
                }
                System.out.println("##### totalAmount : "+totalAmount);
                System.out.println("##### totalDebit : "+totalDebit);
                System.out.println("##### totalCredit : "+totalCredit);
                System.out.println("##### debitMDSLiability : "+debitMDSLiability);
                System.out.println("##### debitMDSReceivable : "+debitMDSReceivable);
                System.out.println("##### creditMDSAssets : "+creditMDSAssets);
                System.out.println("##### creditMDSPayable : "+creditMDSPayable);

                // Bonus
                whereMap.put("SCHEME_NAME",txtSchemeNameClosure.getText());
                List totalBonusAmtLst=ClientUtil.executeQuery("getTotalBonusAmount",whereMap);
                if(totalBonusAmtLst !=null && totalBonusAmtLst.size()>0){
                    whereMap = (HashMap)totalBonusAmtLst.get(0);
                    totalBonusAmount = CommonUtil.convertObjToDouble(whereMap.get("TOTAL_BONUS_AMOUNT")).doubleValue();
                    txtTotalBonusAmount.setText(String.valueOf(totalBonusAmount));
                    if(totalBonusAmount>0){
                        //DEBIT_BONUS_CALCULATION
                        if(totalBonusAmount == CommonUtil.convertObjToDouble(txtTotalBonusAvailable.getText()).doubleValue()){
                            totalDebitBonus = totalBonusAmount;
                        }else{
                            debitBonusPayable = CommonUtil.convertObjToDouble(txtTotalBonusAvailable.getText()).doubleValue();
                            debitSundryReceipts = totalBonusAmount - debitBonusPayable;
                        }
                        //CREDIT_BONUS_CALCULATION
                        if(totalBonusAmount == CommonUtil.convertObjToDouble(txtTotalBonusPaid.getText()).doubleValue()){
                            totalCreditBonus = totalBonusAmount;
                        }else{
                            creditBonusReceivable = CommonUtil.convertObjToDouble(txtTotalBonusPaid.getText()).doubleValue();
                            creditSundryPayment = totalBonusAmount - creditBonusReceivable;
                        }

                        System.out.println("##### totalBonusAmount : "+totalBonusAmount);
                        System.out.println("##### totalDebitBonus : "+totalDebitBonus);
                        System.out.println("##### totalCreditBonus : "+totalCreditBonus);
                        System.out.println("##### debitBonusPayable : "+debitBonusPayable);
                        System.out.println("##### debitSundryReceipts : "+debitSundryReceipts);
                        System.out.println("##### creditBonusReceivable : "+creditBonusReceivable);
                        System.out.println("##### creditSundryPayment : "+creditSundryPayment);
                    }
                }
                MDSClosureMap.put("TOTAL_SCHEME_AMOUNT", String.valueOf(totalAmount));
                MDSClosureMap.put("TOTAL_DEBIT", String.valueOf(totalDebit));
                MDSClosureMap.put("TOTAL_CREDIT", String.valueOf(totalCredit));
                MDSClosureMap.put("DEBIT_MDS_LIABILITY", String.valueOf(debitMDSLiability));
                MDSClosureMap.put("DEBIT_MDS_RECEIVABLE", String.valueOf(debitMDSReceivable));
                MDSClosureMap.put("CREDIT_MDS_ASSETS", String.valueOf(creditMDSAssets));
                MDSClosureMap.put("CREDIT_MDS_PAYABLE", String.valueOf(creditMDSPayable));
//                MDSClosureMap.put("TOTAL_BONUS_AMOUNT", String.valueOf(totalBonusAmount));
                MDSClosureMap.put("TOTAL_DEBIT_BONUS", String.valueOf(totalDebitBonus));
                MDSClosureMap.put("TOTAL_CREDIT_BONUS", String.valueOf(totalCreditBonus));
                MDSClosureMap.put("DEBIT_BONUS_PAYABLE", String.valueOf(debitBonusPayable));
                MDSClosureMap.put("DEBIT_SUNDRY_RECEIPTS", String.valueOf(debitSundryReceipts));
                MDSClosureMap.put("CREDIT_BONUS_RECEIVABLE", String.valueOf(creditBonusReceivable));
                MDSClosureMap.put("CREDIT_SUNDRY_PAYMENT", String.valueOf(creditSundryPayment));
                System.out.println("##### MDS_CLOSURE_MAP : "+MDSClosureMap);
                btnProcess.setEnabled(false);
                btnSchemeNameClosure.setEnabled(false);
                btnSave.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnProcessActionPerformed
    private void btnProcessActionPerform(){
        panMDSClosingDetails.setVisible(true);
        double totalNDef = 0.0;
        double totalDef = 0.0;
        double paidNDef = 0.0;
        double paidDef = 0.0;
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_NAME",txtSchemeNameClosure.getText());
        List totalSchemeAmtLst=ClientUtil.executeQuery("getTotalAmountPerSchemeDivision",whereMap);
        if(totalSchemeAmtLst !=null && totalSchemeAmtLst.size()>0){
            whereMap = (HashMap)totalSchemeAmtLst.get(0);
            txtTotalSchemeAmount.setText(CommonUtil.convertObjToStr(whereMap.get("TOTAL_SCHEME_AMOUNT")));
        }
        whereMap.put("SCHEME_NAME",txtSchemeNameClosure.getText());
        List totalReceived=ClientUtil.executeQuery("getTotalReceivedAmountNonDefaulter",whereMap);
        List defRecevied=ClientUtil.executeQuery("getTotalReceivedAmountDefaulter",whereMap);
        if(totalReceived !=null && totalReceived.size()>0){
            whereMap = (HashMap)totalReceived.get(0);
            totalNDef=CommonUtil.convertObjToDouble(whereMap.get("TOTAL_RECEIVED"));
            System.out.println("#####TOTAL111 : "+totalNDef);
        }
            if(defRecevied !=null && defRecevied.size()>0){
                  whereMap = (HashMap)defRecevied.get(0);
                  totalDef=CommonUtil.convertObjToDouble(whereMap.get("DEF_AMOUNT"));
            System.out.println("#####TOTAL22222 : "+totalDef);
        }
            System.out.println("#####syummmmmmm : "+(totalNDef+totalDef));
        txtTotalAmtReceived.setText(String.valueOf(totalNDef+totalDef));
        whereMap.put("SCHEME_NAME",txtSchemeNameClosure.getText());
        List totalPaid=ClientUtil.executeQuery("getTotalPaidNDefAmount",whereMap);
        List totalDefPaid=ClientUtil.executeQuery("getTotalPaidDEfAmount",whereMap);
        if(totalPaid !=null && totalPaid.size()>0){
            whereMap = (HashMap)totalPaid.get(0);
            paidNDef=CommonUtil.convertObjToDouble(whereMap.get("TOTAL_PAID"));
            System.out.println("#####TOTAL111 : "+paidNDef);
        }
            if(totalDefPaid !=null && totalDefPaid.size()>0){
                  whereMap = (HashMap)totalDefPaid.get(0);
                  paidDef=CommonUtil.convertObjToDouble(whereMap.get("TOTAL_PAID_DEF"));
            System.out.println("#####TOTAL22222 : "+paidDef);
        }
            System.out.println("#####syummmmmmm : "+(paidNDef+paidDef));
        txtTotalAmtPaid.setText(String.valueOf(paidNDef+paidDef));
        whereMap.put("SCHEME_NAME",txtSchemeNameClosure.getText());
        List totalBonusAvailable=ClientUtil.executeQuery("getTotalBonusAmountAvailable",whereMap);
        if(totalBonusAvailable !=null && totalBonusAvailable.size()>0){
            whereMap = (HashMap)totalBonusAvailable.get(0);
            txtTotalBonusAvailable.setText(CommonUtil.convertObjToStr(whereMap.get("BONUS_AVAILABLE")));
        }
        whereMap.put("SCHEME_NAME",txtSchemeNameClosure.getText());
        List totalBonusPaid=ClientUtil.executeQuery("getTotalBonusAmountPaid",whereMap);
        if(totalBonusPaid !=null && totalBonusPaid.size()>0){
            whereMap = (HashMap)totalBonusPaid.get(0);
            txtTotalBonusPaid.setText(CommonUtil.convertObjToStr(whereMap.get("BONUS_PAID")));
        }
        double unpaidBonus = 0.0;
        if(CommonUtil.convertObjToDouble(txtTotalBonusAvailable.getText()).doubleValue() > CommonUtil.convertObjToDouble(txtTotalBonusPaid.getText()).doubleValue()){
            unpaidBonus = CommonUtil.convertObjToDouble(txtTotalBonusAvailable.getText()).doubleValue() -
            CommonUtil.convertObjToDouble(txtTotalBonusPaid.getText()).doubleValue();
        }
        txtTotalUnpaidBonus.setText(String.valueOf(unpaidBonus));
        if(CommonUtil.convertObjToDouble(txtTotalBonusAvailable.getText()).doubleValue()>0){
            double totalPending = 0.0;
            totalPending = CommonUtil.convertObjToDouble(txtTotalAmtReceived.getText()).doubleValue() -
            CommonUtil.convertObjToDouble(txtTotalAmtPaid.getText()).doubleValue();
            if(totalPending>=0){
                txtTotalPendingPrizedAmt.setText(String.valueOf(totalPending));
            }
        }
        if(observable.getActionType() != ClientConstants.ACTIONTYPE_NEW){
            whereMap.put("SCHEME_NAME",txtSchemeNameClosure.getText());
            List totalBonusAmtLst=ClientUtil.executeQuery("getTotalBonusAmount",whereMap);
            if(totalBonusAmtLst !=null && totalBonusAmtLst.size()>0){
                whereMap = (HashMap)totalBonusAmtLst.get(0);
                txtTotalBonusAmount.setText(String.valueOf(CommonUtil.convertObjToDouble(whereMap.get("TOTAL_BONUS_AMOUNT")).doubleValue()));
            }
        }
        if(CommonUtil.convertObjToDouble(txtTotalBonusAvailable.getText()).doubleValue()>0){
            whereMap.put("SCHEME_NAME",txtSchemeNameClosure.getText());
            List totalPendingCommissionAmtLst=ClientUtil.executeQuery("getpendingCommissionAmount",whereMap);
            if(totalPendingCommissionAmtLst !=null && totalPendingCommissionAmtLst.size()>0){
                whereMap = (HashMap)totalPendingCommissionAmtLst.get(0);
                txtTotalPendingCommission.setText(String.valueOf(CommonUtil.convertObjToDouble(whereMap.get("PENDINGCOMMSSION")).doubleValue()));
                txtTotalInsReceivable.setText(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(txtTotalSchemeAmount.getText())-(CommonUtil.convertObjToDouble(txtTotalAmtReceived.getText()))));
            }
        }
    }
    private void btnSchemeNameClosureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchemeNameClosureActionPerformed
        // TODO add your handling code here:
        callView("CLOSURE_SCHEME_DETAILS");
    }//GEN-LAST:event_btnSchemeNameClosureActionPerformed
    
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this);
        panMDSClosingDetails.setVisible(false);
        btnSchemeNameClosure.setEnabled(true);
    }//GEN-LAST:event_btnClearActionPerformed
    
    private void txtSchemeNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSchemeNameFocusLost
        // TODO add your handling code here:
        if(txtSchemeName.getText().length()>0){
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAMES",txtSchemeName.getText());
            List lst=ClientUtil.executeQuery("getSelectSchemeName",whereMap);
            if(lst !=null && lst.size()>0){
                viewType = "SCHEME_DETAILS";
                whereMap=(HashMap)lst.get(0);
                whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                List chitLst=ClientUtil.executeQuery("getSelectEachSchemeDetailsForCommence",whereMap);
                if(chitLst !=null && chitLst.size()>0){
                    whereMap = (HashMap)chitLst.get(0);
                    fillData(whereMap);
                    chitLst = null;
                    lst=null;
                    whereMap=null;
                }else{
                    ClientUtil.displayAlert("In this Scheme Commencement Already Over !!! ");
                    txtSchemeName.setText("");
                    ClientUtil.clearAll(this);
                }
            }else{
                ClientUtil.displayAlert("Invalid Scheme Name !!! ");
                txtSchemeName.setText("");
            }
        }
    }//GEN-LAST:event_txtSchemeNameFocusLost
    
    private void tdtCommencementDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtCommencementDateFocusLost
        // TODO add your handling code here:
        java.util.Date startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtCommencementDate.getDateValue()));
//        java.util.Date currDt = currDt.clone();
        observable.setTdtCommencementDate(startDate);
        if(DateUtil.dateDiff(currDt,startDate)<0){
            ClientUtil.showAlertWindow("Commencement date Should be on or After Current Date !!!");
            tdtCommencementDate.setDateValue(DateUtil.getStringDate(currDt));
            return;
        }
    }//GEN-LAST:event_tdtCommencementDateFocusLost
    
    private void btnSchemeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchemeNameActionPerformed
        // TODO add your handling code here:
        callView("SCHEME_DETAILS");
    }//GEN-LAST:event_btnSchemeNameActionPerformed
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        if(panGeneralRemittance.isShowing()==true){
            PAN = COMMENCEMENT;
        }else if(panMDSClosureDetails.isShowing()==true){
            PAN = CLOSURE;
        }
        callView("Enquirystatus");
        btnCheck();
    }            //    private void enableDisableAliasBranchTable(boolean flag) {//GEN-LAST:event_btnViewActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        if(panGeneralRemittance.isShowing()==true){
            PAN = COMMENCEMENT;
        }else if(panMDSClosureDetails.isShowing()==true){
            PAN = CLOSURE;
        }
        if(PAN == COMMENCEMENT){
            authorizeStatus(CommonConstants.STATUS_REJECTED);
        }else if(PAN == CLOSURE){
            closureAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        }
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(true);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        if(panGeneralRemittance.isShowing()==true){
            PAN = COMMENCEMENT;
        }else if(panMDSClosureDetails.isShowing()==true){
            PAN = CLOSURE;
        }
        if(PAN == COMMENCEMENT){
            authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        }else if(PAN == CLOSURE){
            closureAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        }
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        if(panGeneralRemittance.isShowing()==true){
            PAN = COMMENCEMENT;
        }else if(panMDSClosureDetails.isShowing()==true){
            PAN = CLOSURE;
        }
        if(PAN == COMMENCEMENT){
            authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        }else if(PAN == CLOSURE){
            closureAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        }
        //__ If there's no data to be Authorized, call Cancel action...
        if(!isModified()){
            setButtonEnableDisable();
            btnCancelActionPerformed(null);
        }
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.setStatus();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        if(panGeneralRemittance.isShowing()==true){  //Changed By Suresh
            PAN = COMMENCEMENT;
            ClientUtil.enableDisable(panInsideGeneralRemittance, true);
            btnSchemeName.setEnabled(true);
            txtSchemeName.setEnabled(true);
            tdtStartDt.setEnabled(false);
            txtInstAmt.setEnabled(false);
            txtTotAmt.setEnabled(false);
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
        }else if(panMDSClosureDetails.isShowing()==true){
            PAN = CLOSURE;
            MDSClosureMap = new HashMap();
            closureButtonsEnableDisable(true);
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        if(panGeneralRemittance.isShowing()==true){
            PAN = COMMENCEMENT;
        }else if(panMDSClosureDetails.isShowing()==true){
            PAN = CLOSURE;
        }
        callView("Edit");
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false);
        txtSchemeName.setEnabled(false);
        tdtStartDt.setEnabled(false);
        txtInstAmt.setEnabled(false);
        txtTotAmt.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        observable.setStatus();
        if(panGeneralRemittance.isShowing()==true){
            PAN = COMMENCEMENT;
            callView("Delete");
        }else if(panMDSClosureDetails.isShowing()==true){
            PAN = CLOSURE;
            callView("Delete");
            btnSave.setEnabled(false);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        btnSave.setEnabled(false);
        if(PAN == COMMENCEMENT){
            System.out.println("##### Commemncement: ");
            java.util.Date startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtStartDt.getDateValue()));
            //        java.util.Date currDt = currDt.clone();
            java.util.Date commenceDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtCommencementDate.getDateValue()));
            if(DateUtil.dateDiff(commenceDt,startDate)>0){
                ClientUtil.showAlertWindow("Commencement Date Should be on or after Scheme Start Date");
                return;
            }else{
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
//                    String status = transactionUI.getCallingStatus();
//                    String transProdType = transactionUI.getCallingTransProdType();
                    int transactionSize = 0 ;
                    if(transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue() > 0){
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
                        return;
                    }else {
                        if(CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue()>0){
                            transactionSize = (transactionUI.getOutputTO()).size();
                            if(transactionSize != 1 && CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue()>0){
                                ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction") ;
                                return;
                            }else{
                                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                            }
                        }else if(transactionUI.getOutputTO().size()>0){
                            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                        }
                    }
                    if(transactionSize == 0 && CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue()>0){
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
                        return;
                    }else if(transactionSize != 0 ){
                        if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                            ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                            return;
                        }else{
                            
                        }
                    }
                }else{
                    if(transactionUI.getOutputTO().size()>0){
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    }
                }
                savePerformed();
            }
        }else if(PAN == CLOSURE){
            try{
//                java.util.Date currDt = currDt.clone();
                System.out.println("##### Closure: ");
                HashMap whereMap = new HashMap();
                observable.setMDSClosureMap(MDSClosureMap);
                observable.closureDoActionPerform();
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                    whereMap.put("SCHEME_NAME",txtSchemeNameClosure.getText());
                    whereMap.put("SCHEME_START_DT",DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtStartDtClosure.getDateValue())));
                    whereMap.put("SCHEME_END_DT",DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtEndDtClosure.getDateValue())));
                    whereMap.put("INSTALLMENT_AMOUNT",txtInstAmtClosure.getText());
                    whereMap.put("SCHEME_CLOSE_DT",(Date)currDt.clone());
                    whereMap.put("STATUS_DT",(Date)currDt.clone());
                    whereMap.put("STATUS",CommonConstants.STATUS_CREATED);
                    whereMap.put("STATUS_BY",TrueTransactMain.USER_ID);
                    ClientUtil.execute("insertMDSClosureDetails", whereMap);
                }
                if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                    HashMap lockMap = new HashMap();
                    if (observable.getProxyReturnMap()!=null) {
                        if (observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST") || observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                            displayTransDetail(observable.getProxyReturnMap());
                        }
                    }
                    btnCancelActionPerformed(null);
                    observable.setResultStatus();
                    lblStatus.setText(observable.getLblStatus());
                    setButtonEnableDisable();
                    btnSave.setEnabled(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap : " +proxyResultMap);
        String transferDisplayStr = "MDS Closure Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        for (int i=0; i<keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List)proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("BATCH_ID");
                    }
                    transferDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Batch Id : "+transMap.get("BATCH_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        transferDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        transferDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                }
                transferCount++;
            }
        }
        
        if(transferCount>0){
            displayStr+=transferDisplayStr;
        }
        if(!displayStr.equals("")) {
            ClientUtil.showMessageWindow(""+displayStr);
        }
    }
    
    private void getTransDetails (String batchId){
        HashMap getTransMap = new HashMap();
        Date curr_dt = (Date) currDt.clone();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", (Date) curr_dt.clone());
        getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        HashMap returnMap = new HashMap();
        List transList = ClientUtil.executeQuery("getTransferDetails", getTransMap);
        if(transList!=null && transList.size()>0){
            returnMap.put("TRANSFER_TRANS_LIST",transList);
        }
        displayTransDetail(returnMap);
        getTransMap.clear();
        getTransMap = null;
        transList = null;
    }
    
    
    private String periodLengthValidation(CTextField txtField, CComboBox comboField){
        String message = "";
        String key = CommonUtil.convertObjToStr(((ComboBoxModel) comboField.getModel()).getKeyForSelected());
        if (!ClientUtil.validPeriodMaxLength(txtField, key)){
            message = objMandatoryMRB.getString(txtField.getName());
        }
        return message;
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.resetStatus();
        observable.resetCommencementTbl();
        observable.resetOBFields();
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        txtSchemeName.setText("");
        tdtStartDt.setDateValue("");
        txtInstAmt.setText("");
        txtTotAmt.setText("");
        btnNew.setEnabled(true);
        btnSave.setEnabled(false);
        ClientUtil.enableDisable(this, false);
        viewType = "";
        lblStatus.setText("             ");
        setButtonEnableDisable();
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnNew.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        btnSave.setEnabled(false);
        btnSchemeName.setEnabled(false);
        tdtCommencementDate.setDateValue("");
        closureButtonsEnableDisable(false);
        ClientUtil.clearAll(this);
        PAN=-1;
        panMDSClosingDetails.setVisible(false);
        MDSClosureMap = null;
        //__ Make the Screen Closable..
        setModified(false);
        
    }//GEN-LAST:event_btnCancelActionPerformed
    public void authorize(HashMap map){
        map.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        //        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setAuthorizeMap(map);
        try{
            if(transactionUI.getOutputTO().size()>0){
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            observable.doAction();
        } catch(Exception e) {
            e.printStackTrace();
        }
        setModified(false);
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
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
    
    public void authorizeStatus(String authorizeStatus) {
        ArrayList arrList = new ArrayList();
        HashMap authDataMap = new HashMap();
        HashMap singleAuthorizeMap = new HashMap();
        if (viewType.equals(AUTHORIZE) && isFilled) {
            authDataMap.put("TRANS_ID", observable.getCommencementTransId());
            arrList.add(authDataMap);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(singleAuthorizeMap);
            super.setOpenForEditBy(observable.getStatusBy());
            btnCancelActionPerformed(null);
            observable.setResult(observable.getActionType());
            observable.setResultStatus();
            viewType = "";
        } else {
            final HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            //whereMap.put(CommonConstants.MAP_NAME, "getSelectCommencementList");//commented by shany
            whereMap.put(CommonConstants.MAP_NAME, "getSelectGDSCommencementList");
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
           // mapParam.put(CommonConstants.MAP_NAME, "getSelectCommencementList");//commented by shany
            mapParam.put(CommonConstants.MAP_NAME, "getSelectGDSCommencementList");
            //            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeRemittanceProduct");
            viewType = AUTHORIZE;
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
            isFilled = false;
            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSaveDisable();
            //            setAuthBtnEnableDisable();
        }
    }
    
    private void closureAuthorizeStatus(String authorizeStatus) {
        HashMap authDataMap = new HashMap();
        HashMap singleAuthorizeMap = new HashMap();
        if (viewType == AUTHORIZE && isFilled){
            try{
                singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                singleAuthorizeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("AUTHORIZED_DT",ClientUtil.getCurrentDateWithTime());
                singleAuthorizeMap.put("SCHEME_NAME",txtSchemeNameClosure.getText());
                if(CommonUtil.convertObjToDouble(txtTotalSchemeAmount.getText()).doubleValue() == 
                CommonUtil.convertObjToDouble(txtTotalAmtReceived.getText()).doubleValue() && 
                CommonUtil.convertObjToDouble(txtTotalSchemeAmount.getText()).doubleValue() == 
                CommonUtil.convertObjToDouble(txtTotalAmtPaid.getText()).doubleValue()){
                    singleAuthorizeMap.put("SCHEME_CLOSED","SCHEME_CLOSED");
                }
                observable.setAuthorizeMap(singleAuthorizeMap);
                observable.closureDoActionPerform();
                btnCancelActionPerformed(null);
                observable.setResult(observable.getActionType());
                observable.setResultStatus();
                viewType = "";
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getAuthMDSClosureDetails");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    
    private void btnSaveDisable(){
        btnSave.setEnabled(false);
        mitSave.setEnabled(false);
        btnCancel.setEnabled(true);
        mitCancel.setEnabled(true);
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

private void txtTotalUnpaidBonusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalUnpaidBonusActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtTotalUnpaidBonusActionPerformed
    
    /** This method helps in popoualting the data from the data base
     * @param currField Action the argument is passed according to the command issued
     */
    private void callView(String currField) {
        updateOBFields();
        viewType = currField;
        HashMap viewMap = new HashMap();
        HashMap where = new HashMap();
        if(PAN == COMMENCEMENT){
            if (currField.equals("Edit")|| currField.equals("Enquirystatus") || currField.equals("Delete")){
                lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
                ArrayList lst = new ArrayList();
                lst.add("PRODUCT ID");
                viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                lst = null;
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where.put("SELECTED_BRANCH_ID",getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "getSelectCommencementEditList");
            } else if (currField.equalsIgnoreCase("SCHEME_DETAILS")) {
                viewMap.put(CommonConstants.MAP_NAME, "getSelectEachGroupMDSDetailsForCommence");
            }
            else if (currField.equalsIgnoreCase("GROUP_DETAILS")) {
                viewMap.put(CommonConstants.MAP_NAME, "getSelectEachGroupMDSDetailsForCommence");
            }
        }else if(PAN == CLOSURE){
            if (currField.equalsIgnoreCase("CLOSURE_SCHEME_DETAILS")) {
                viewMap.put(CommonConstants.MAP_NAME, "getSelectSchemeDetailsClosure");
            }else if (currField.equals("Edit")|| currField.equals("Delete")){
                viewMap.put(CommonConstants.MAP_NAME, "getAuthMDSClosureDetails");
            }else if(currField.equals("Enquirystatus")){
                viewMap.put(CommonConstants.MAP_NAME, "getEnquiryMDSClosureDetails");
            }
        }
        new ViewAll(this, viewMap).show();
    }
    
    /** This method helps in filling the data frm the data base to respective txt fields
     * @param obj param The selected data from the viewAll() is passed as a param
     */
    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        if (viewType != null) {
            if(PAN == COMMENCEMENT){
                if (viewType.equals("Edit") || viewType.equals(AUTHORIZE) ||  viewType.equals("Enquirystatus") || viewType.equals("Delete") ) {
                    isFilled = true;
                    observable.populateData(hash);
                    if ( viewType.equals("Delete") || viewType.equals(AUTHORIZE)|| viewType.equals("Enquirystatus")) {
                        setButtonEnableDisable();
                        ClientUtil.enableDisable(this, false);
                    }
                    if(viewType ==  AUTHORIZE) {
                        btnAuthorize.setEnabled(true);
                        observable.setPredefinedInstallment(CommonUtil.convertObjToStr(hash.get("PREDEFINITION_INSTALLMENT")));  
                    }
                }else if (viewType.equalsIgnoreCase("SCHEME_DETAILS")) {
                    System.out.println("hash ....... "+ hash);
                    observable.setTxtSchemeName(CommonUtil.convertObjToStr(hash.get("GROUP_NO")));
                    transactionUI.setSchemeName(CommonUtil.convertObjToStr(hash.get("GROUP_NO")));
                    List schemeList = ClientUtil.executeQuery("getGDSScheme", hash);
                     int schemeCount=schemeList.size();
                    if (schemeList != null && schemeList.size() > 0) {
                        HashMap scheme = (HashMap) schemeList.get(0);
                        transactionUI.setSchemeName(CommonUtil.convertObjToStr(scheme.get("SCHEME_NAME")));
                    }
                    
                    //
                    observable.resetCommencementTbl();
                    txtSchemeName.setEnabled(false);
                    boolean flag = observable.populateTableRecord(hash);
                    tdtCommencementDate.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
                    observable.setTdtCommencementDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtCommencementDate.getDateValue())));                                    
                    if(flag){
                        btnCancelActionPerformed(null);
                    }else{
                        transactionUI.setCallingAmount(observable.getTxtTotAmt());
                    }
                }
                if (viewType.equalsIgnoreCase("Edit")){
                    ClientUtil.enableDisable(this, true);
                    setButtonEnableDisable();
                    setDelBtnEnableDisable(true);
                }
            }else if(PAN == CLOSURE){
                if (viewType.equalsIgnoreCase("CLOSURE_SCHEME_DETAILS")) {
                    txtSchemeNameClosure.setText(CommonUtil.convertObjToStr(hash.get("SCHEME_NAME")));
                    observable.setTxtSchemeNameClosure(CommonUtil.convertObjToStr(hash.get("SCHEME_NAME")));
                    tdtStartDtClosure.setDateValue(CommonUtil.convertObjToStr(hash.get("START_DT")));
                    tdtEndDtClosure.setDateValue(CommonUtil.convertObjToStr(hash.get("END_DT")));
                    txtInstAmtClosure.setText(CommonUtil.convertObjToStr(hash.get("INST_AMOUNT")));
                    Date curr_Dt = (Date) currDt.clone();
                    Date end_Dt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("END_DT")));
                    if(DateUtil.dateDiff(end_Dt,curr_Dt)<0){
                        ClientUtil.showMessageWindow("MDS End_Dt Greater Than Current Date!!!  The Scheme Can not be Closed !!!");
                        ClientUtil.clearAll(this);
                        observable.setTxtSchemeNameClosure("");
                        btnProcess.setEnabled(false);
                        return;
                    }
                    List lst = ClientUtil.executeQuery("getCountOfPrizedMemberPerScheme", hash);
                    if(lst!= null && lst.size()>0){
                        HashMap whereMap = (HashMap)lst.get(0);
                        double totalCount = 0.0;
                        double totalMembers = 0.0;
                        totalCount = CommonUtil.convertObjToDouble(whereMap.get("COUNT")).doubleValue();
                        totalMembers = CommonUtil.convertObjToDouble(whereMap.get("TOTAL_NO_OF_MEMBERS")).doubleValue();
                        if(totalCount!=totalMembers){
                            ClientUtil.showMessageWindow("MDS Prized Money Details Entry Pending !!! The Scheme Can not be Closed !!!");
                            ClientUtil.clearAll(this);
                            observable.setTxtSchemeNameClosure("");
                            btnProcess.setEnabled(false);
                            return;
                        }
                    }
                    btnSave.setEnabled(false);
                    btnProcess.setEnabled(true);
                }else if (viewType.equals("Edit") || viewType.equals(AUTHORIZE) ||  viewType.equals("Enquirystatus") || viewType.equals("Delete") ) {
                    txtSchemeNameClosure.setText(CommonUtil.convertObjToStr(hash.get("SCHEME_NAME")));
                    observable.setTxtSchemeNameClosure(CommonUtil.convertObjToStr(hash.get("SCHEME_NAME")));
                    tdtStartDtClosure.setDateValue(CommonUtil.convertObjToStr(hash.get("START_DT")));
                    tdtEndDtClosure.setDateValue(CommonUtil.convertObjToStr(hash.get("END_DT")));
                    txtInstAmtClosure.setText(CommonUtil.convertObjToStr(hash.get("INST_AMOUNT")));
                    isFilled = true;
                    setButtonEnableDisable();
                    btnProcessActionPerform();
                    ClientUtil.enableDisable(this, false);
                    getTransDetails(CommonUtil.convertObjToStr(hash.get("SCHEME_NAME")));
                    if(viewType ==  AUTHORIZE) {
                        btnAuthorize.setEnabled(true);
                    }
                }
            }
        }
        
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void savePerformed(){
        updateOBFields();
        boolean productID = false;
        // If the Action Type is  NEW or EDIT, Check for the Uniqueness of Product ID and Product Description
        //System.out.println("observable.getActionType() : " + observable.getActionType());
        //System.out.println("productID : " + productID);
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_COPY){
            //            productID = observable.uniqueProduct();
        }
        if(productID == false){
            //added by rishad for avoiding doubling issue at 05/08/2015
            CommonUtil comm = new CommonUtil();
            final JDialog loading = comm.addProgressBar();
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws InterruptedException /** Execute some operation */
                {
                    try {
                        observable.doSave();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
            //__ if the Action is not Falied, Reset the fields...
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("PRODUCT ID");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("PRODUCT ID")) {
                        lockMap.put("PRODUCT ID", observable.getProxyReturnMap().get("PRODUCT ID"));
                    }
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    //                    lockMap.put("PRODUCT ID", observable.getTxtProductIdGR());
                }
                setEditLockMap(lockMap);
                setEditLock();
                observable.resetOBFields();
                btnCancelActionPerformed(null);
                ClientUtil.enableDisable(this, false);
                if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE ){
                    observable.setResult(observable.getActionType());
                }
                observable.setResultStatus();
                lblStatus.setText(observable.getLblStatus());
                setButtonEnableDisable();
            }
        }
    }
    
    private void enableDisableall(){
        observable.resetOBFields();
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
    }
    
    /** To Enable or Disable New, Edit, Save and Cancel Button */
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        setDelBtnEnableDisable(false);
        setAuthBtnEnableDisable();
    }
    
    /** To Enable or Disable Delete Button */
    private void setDelBtnEnableDisable(boolean enableDisable){
        btnDelete.setEnabled(enableDisable );
        mitDelete.setEnabled(enableDisable);
    }
    
    /** To Enable or Disable Authorize, Rejection and Exception Button */
    private void setAuthBtnEnableDisable(){
        final boolean enableDisable = !btnSave.isEnabled();
        btnAuthorize.setEnabled(enableDisable);
        btnException.setEnabled(enableDisable);
        btnReject.setEnabled(enableDisable);
        mitAuthorize.setEnabled(enableDisable);
        mitException.setEnabled(enableDisable);
        mitReject.setEnabled(enableDisable);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSchemeName;
    private com.see.truetransact.uicomponent.CButton btnSchemeNameClosure;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel lblEndDtClosure;
    private com.see.truetransact.uicomponent.CLabel lblInstAmt;
    private com.see.truetransact.uicomponent.CLabel lblInstAmtClosure;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSchemeName;
    private com.see.truetransact.uicomponent.CLabel lblSchemeNameClosure;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblSpace76;
    private com.see.truetransact.uicomponent.CLabel lblSpace77;
    private com.see.truetransact.uicomponent.CLabel lblSpace78;
    private com.see.truetransact.uicomponent.CLabel lblStartDt;
    private com.see.truetransact.uicomponent.CLabel lblStartDt1;
    private com.see.truetransact.uicomponent.CLabel lblStartDtClosure;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmtPaid;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmtReceived;
    private com.see.truetransact.uicomponent.CLabel lblTotalBonusAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotalBonusAvailable;
    private com.see.truetransact.uicomponent.CLabel lblTotalBonusPaid;
    private com.see.truetransact.uicomponent.CLabel lblTotalInsReceivable;
    private com.see.truetransact.uicomponent.CLabel lblTotalPendingCommission;
    private com.see.truetransact.uicomponent.CLabel lblTotalPendingPrizedAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalSchemeAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotalUnpaidBonus;
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
    private com.see.truetransact.uicomponent.CPanel panCommencement;
    private com.see.truetransact.uicomponent.CPanel panGeneralRemittance;
    private com.see.truetransact.uicomponent.CPanel panInsideGeneralRemittance;
    private com.see.truetransact.uicomponent.CPanel panInsideGeneralRemittance1;
    private com.see.truetransact.uicomponent.CPanel panInsideMDSClosureDetails;
    private com.see.truetransact.uicomponent.CPanel panMDSClosingDetails;
    private com.see.truetransact.uicomponent.CPanel panMDSClosureDetails;
    private com.see.truetransact.uicomponent.CPanel panProcess;
    private com.see.truetransact.uicomponent.CPanel panSchemeName;
    private com.see.truetransact.uicomponent.CPanel panSchemeNameClosure;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdgEFTProductGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIsLapsedGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPayableBranchGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrintServicesGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSeriesGR;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CScrollPane srpCommencement;
    private com.see.truetransact.uicomponent.CTabbedPane tabRemittanceProduct;
    private com.see.truetransact.uicomponent.CTable tblCommencement;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CDateField tdtCommencementDate;
    private com.see.truetransact.uicomponent.CDateField tdtEndDtClosure;
    private com.see.truetransact.uicomponent.CDateField tdtStartDt;
    private com.see.truetransact.uicomponent.CDateField tdtStartDtClosure;
    private com.see.truetransact.uicomponent.CTextField txtInstAmt;
    private com.see.truetransact.uicomponent.CTextField txtInstAmtClosure;
    private com.see.truetransact.uicomponent.CTextField txtSchemeName;
    private com.see.truetransact.uicomponent.CTextField txtSchemeNameClosure;
    private com.see.truetransact.uicomponent.CTextField txtTotAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmtPaid;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmtReceived;
    private com.see.truetransact.uicomponent.CTextField txtTotalBonusAmount;
    private com.see.truetransact.uicomponent.CTextField txtTotalBonusAvailable;
    private com.see.truetransact.uicomponent.CTextField txtTotalBonusPaid;
    private com.see.truetransact.uicomponent.CTextField txtTotalInsReceivable;
    private com.see.truetransact.uicomponent.CTextField txtTotalPendingCommission;
    private com.see.truetransact.uicomponent.CTextField txtTotalPendingPrizedAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotalSchemeAmount;
    private com.see.truetransact.uicomponent.CTextField txtTotalUnpaidBonus;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] arg){
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        GDSCommencementUI gui = new GDSCommencementUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}