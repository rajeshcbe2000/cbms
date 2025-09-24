/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ShareTransferUI.java
 *
 * Created on February 3, 2005, 3:06 PM
 */

package com.see.truetransact.ui.share.sharetransfer;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;


import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;
/**
 *
 * @author  152721
 */
public class ShareTransferUI extends CInternalFrame  implements java.util.Observer , UIMandatoryField{
    HashMap mandatoryMap;
    ShareTransferOB observable;
    //ShareTransferRB resourceBundle = new ShareTransferRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.share.sharetransfer.ShareTransferRB", ProxyParameters.LANGUAGE);
    
    final int EDIT=0,DELETE=1,AUTHORIZE=2, ACCTFROM=3, ACCTTO=4 , ACCOUNT_W_C=5 ,DIVIDENTACCOUNT=6,DIVIDENDVIEW=7;
    int viewType=-1;
    boolean isFilled = false;
    private TransactionUI transactionUI = new TransactionUI();
    private TransDetailsUI transDetailsUI = null;
    private Date currDt = null;
    /** Creates new form ShareTransferUI */
    public ShareTransferUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initSetup();
        initComponentData();
        //      tabShareDetails.setEnabledAt(0,false);
        //      tabShareDetails.remove(0);
        
        transactionUI.setSourceScreen("ACT_CLOSING");
        transactionUI.addToScreen(panTransaction);
        transactionUI.setTransactionMode(CommonConstants.DEBIT) ;
        //        transDetailsUI =  new TransDetailsUI(panAccountHead);
        observable.setTransactionOB(transactionUI.getTransactionOB());
    }
    private void initComponentData() {
        cboShareType.setModel(observable.getCbmShareType());
        
    }
    
    private void initSetup(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();  // Added by Rajesh
        setMaxLenths();    // Added by Rajesh
        setObservable();
        
        ClientUtil.enableDisable(this, false); //__ Disables all when the screen appears for the 1st time
        setButtonEnableDisable();              //__ Enables/Disables the necessary buttons and menu items...
        setEnableDisableButtons(false);
        observable.resetForm();                //__ Resets the Data in the Form...
        clearAllDividendPay();
        observable.resetTable();
        observable.resetStatus();              //__ to reset the status...
        observable.ttNotifyObservers();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panShareTransData);
        tabShareDetails.remove(0);
    }
    
    private void setObservable() {
        observable = ShareTransferOB.getInstance();
        observable.addObserver(this);
        observable.setTransactionOB(transactionUI.getTransactionOB());
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAcctFrom.setName("btnAcctFrom");
        //        btnAcctTo.setName("btnAcctTo");
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
        lblAcctFrom.setName("lblAcctFrom");
        lblAcctTo.setName("lblAcctTo");
        lblMsg.setName("lblMsg");
        lblNameFrom.setName("lblNameFrom");
        lblNameTo.setName("lblNameTo");
        lblRemarks.setName("lblRemarks");
        lblShareFrom.setName("lblShareFrom");
        lblShareTo.setName("lblShareTo");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus1.setName("lblStatus1");
        lblTransNo.setName("lblTransNo");
        lblTransNoValue.setName("lblTransNoValue");
        mbrShareTransfer.setName("mbrShareTransfer");
        panShareTransData.setName("panShareTransData");
        panStatus.setName("panStatus");
        txtAcctFrom.setName("txtAcctFrom");
        txtAcctTo.setName("txtAcctTo");
        txtRemarks.setName("txtRemarks");
        txtShareFrom.setName("txtShareFrom");
        txtShareTo.setName("txtShareTo");
        tabShareDetails.setTitleAt(0, "Share Transfer");
        tabShareDetails.setTitleAt(1, "Dividend Payment");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnAcctTo.setText(resourceBundle.getString("btnAcctTo"));
        lblAcctTo.setText(resourceBundle.getString("lblAcctTo"));
        lblStatus1.setText(resourceBundle.getString("lblStatus1"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        lblShareTo.setText(resourceBundle.getString("lblShareTo"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnAcctFrom.setText(resourceBundle.getString("btnAcctFrom"));
        lblShareFrom.setText(resourceBundle.getString("lblShareFrom"));
        lblTransNoValue.setText(resourceBundle.getString("lblTransNoValue"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblTransNo.setText(resourceBundle.getString("lblTransNo"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblAcctFrom.setText(resourceBundle.getString("lblAcctFrom"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblNameFrom.setText(resourceBundle.getString("lblNameFrom"));
        lblNameTo.setText(resourceBundle.getString("lblNameTo"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        cboShareType.setSelectedItem(observable.getCboShareType());
        txtAcctFrom.setText(observable.getTxtAcctFrom());
        //        txtAcctTo.setText(observable.getTxtAcctTo());
        txtShareFrom.setText(observable.getTxtShareFrom());
        txtShareTo.setText(observable.getTxtShareTo());
        txtRemarks.setText(observable.getTxtRemarks());
        
        lblTransNoValue.setText(observable.getLblShareTransID());
        
        // to set the values in table...
        tblShareTrans.setModel(observable.getTblShareTrans());
        //        tblShareTrans1.setModel(observable.getTblShareTrans());
        
        //To set the Status...
        lblStatus1.setText(observable.getLblStatus());
        //to Dividend AMt Without Transfer
        txtShareNo.setText(observable.getDivAcno());
        txtDividendAmt.setText(CommonUtil.convertObjToStr(observable.getDivAmt()));
        tdtDividendupTo.setDateValue(CommonUtil.convertObjToStr(observable.getDivUptoDate()));
        
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtAcctFrom(txtAcctFrom.getText());
        //        observable.setTxtAcctTo(txtAcctTo.getText());
        observable.setTxtShareFrom(txtShareFrom.getText());
        observable.setTxtShareTo(txtShareTo.getText());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setCboShareType((String) cboShareType.getSelectedItem());
        observable.setDivAcno(txtShareNo.getText());
        observable.setDivAmt(CommonUtil.convertObjToDouble(txtDividendAmt.getText()));
        observable.setDivUptoDate(DateUtil.getDateMMDDYYYY(tdtDividendupTo.getDateValue()));
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAcctFrom", new Boolean(true));
        mandatoryMap.put("txtAcctTo", new Boolean(true));
        mandatoryMap.put("txtShareFrom", new Boolean(true));
        mandatoryMap.put("txtShareTo", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        ShareTransferMRB objMandatoryRB = new ShareTransferMRB();
        txtAcctFrom.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAcctFrom"));
        //        txtAcctTo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAcctTo"));
        txtShareFrom.setHelpMessage(lblMsg, objMandatoryRB.getString("txtShareFrom"));
        txtShareTo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtShareTo"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
    }
    
    private void setMaxLenths() {
        txtAcctFrom.setMaxLength(16);
        //        txtAcctTo.setMaxLength(16);
        txtShareFrom.setMaxLength(16);
        txtShareFrom.setValidation(new NumericValidation());
        txtShareTo.setMaxLength(16);
        txtShareTo.setValidation(new NumericValidation());
        txtRemarks.setMaxLength(256);
    }
    
    // To set The Value of the Buttons Depending on the Value or Condition...
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(!btnSave.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrLoantProduct = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabShareDetails = new javax.swing.JTabbedPane();
        panShareTransfer = new com.see.truetransact.uicomponent.CPanel();
        panShareTransData = new com.see.truetransact.uicomponent.CPanel();
        lblTransNo = new com.see.truetransact.uicomponent.CLabel();
        lblTransNoValue = new com.see.truetransact.uicomponent.CLabel();
        lblAcctFrom = new com.see.truetransact.uicomponent.CLabel();
        txtAcctFrom = new com.see.truetransact.uicomponent.CTextField();
        btnAcctFrom = new com.see.truetransact.uicomponent.CButton();
        lblAcctTo = new com.see.truetransact.uicomponent.CLabel();
        txtAcctTo = new com.see.truetransact.uicomponent.CTextField();
        btnAcctTo = new com.see.truetransact.uicomponent.CButton();
        lblShareFrom = new com.see.truetransact.uicomponent.CLabel();
        txtShareFrom = new com.see.truetransact.uicomponent.CTextField();
        lblShareTo = new com.see.truetransact.uicomponent.CLabel();
        txtShareTo = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblNameFrom = new com.see.truetransact.uicomponent.CLabel();
        lblNameTo = new com.see.truetransact.uicomponent.CLabel();
        panShareTransTab = new com.see.truetransact.uicomponent.CPanel();
        srpShareTrans = new com.see.truetransact.uicomponent.CScrollPane();
        tblShareTrans = new com.see.truetransact.uicomponent.CTable();
        panShareClose = new com.see.truetransact.uicomponent.CPanel();
        panShareCloseData = new com.see.truetransact.uicomponent.CPanel();
        lblShareNo = new com.see.truetransact.uicomponent.CLabel();
        tdtDividendupTo = new com.see.truetransact.uicomponent.CDateField();
        lblDividendupTo = new com.see.truetransact.uicomponent.CLabel();
        lblShareType = new com.see.truetransact.uicomponent.CLabel();
        cboShareType = new com.see.truetransact.uicomponent.CComboBox();
        lblDividendAmt = new com.see.truetransact.uicomponent.CLabel();
        txtDividendAmt = new com.see.truetransact.uicomponent.CTextField();
        lblShareholderNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblShareholderName = new com.see.truetransact.uicomponent.CLabel();
        panShareNo = new com.see.truetransact.uicomponent.CPanel();
        txtShareNo = new com.see.truetransact.uicomponent.CTextField();
        btnShareNo = new com.see.truetransact.uicomponent.CButton();
        lblBatchId = new com.see.truetransact.uicomponent.CLabel();
        lblBatchIdValue = new com.see.truetransact.uicomponent.CLabel();
        panShareCloseTrans = new com.see.truetransact.uicomponent.CPanel();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        mbrShareTransfer = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        sptPrint = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(825, 575));
        setMinimumSize(new java.awt.Dimension(825, 575));
        setPreferredSize(new java.awt.Dimension(825, 575));

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus1.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnNew);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace26);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace27);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrLoantProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnSave);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace28);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrLoantProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnAuthorize);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace29);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnException);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace30);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrLoantProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnPrint);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace31);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnClose);

        getContentPane().add(tbrLoantProduct, java.awt.BorderLayout.NORTH);

        panShareTransfer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panShareTransfer.setMaximumSize(new java.awt.Dimension(815, 350));
        panShareTransfer.setMinimumSize(new java.awt.Dimension(815, 350));
        panShareTransfer.setPreferredSize(new java.awt.Dimension(815, 350));
        panShareTransfer.setLayout(new java.awt.GridBagLayout());

        panShareTransData.setMaximumSize(new java.awt.Dimension(320, 247));
        panShareTransData.setMinimumSize(new java.awt.Dimension(320, 247));
        panShareTransData.setPreferredSize(new java.awt.Dimension(320, 247));
        panShareTransData.setLayout(new java.awt.GridBagLayout());

        lblTransNo.setText("Transfer Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareTransData.add(lblTransNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareTransData.add(lblTransNoValue, gridBagConstraints);

        lblAcctFrom.setText("Transfer From A/C No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareTransData.add(lblAcctFrom, gridBagConstraints);

        txtAcctFrom.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareTransData.add(txtAcctFrom, gridBagConstraints);

        btnAcctFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAcctFrom.setToolTipText("Agent ID");
        btnAcctFrom.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAcctFrom.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAcctFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcctFromActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panShareTransData.add(btnAcctFrom, gridBagConstraints);

        lblAcctTo.setText("Transfer To A/C No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareTransData.add(lblAcctTo, gridBagConstraints);

        txtAcctTo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareTransData.add(txtAcctTo, gridBagConstraints);

        btnAcctTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAcctTo.setToolTipText("Agent ID");
        btnAcctTo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAcctTo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAcctTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcctToActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panShareTransData.add(btnAcctTo, gridBagConstraints);

        lblShareFrom.setText("Share No From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareTransData.add(lblShareFrom, gridBagConstraints);

        txtShareFrom.setMinimumSize(new java.awt.Dimension(100, 21));
        txtShareFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtShareFromFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareTransData.add(txtShareFrom, gridBagConstraints);

        lblShareTo.setText("Share No To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareTransData.add(lblShareTo, gridBagConstraints);

        txtShareTo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtShareTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtShareToFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareTransData.add(txtShareTo, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareTransData.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareTransData.add(txtRemarks, gridBagConstraints);

        lblNameFrom.setForeground(new java.awt.Color(0, 51, 204));
        lblNameFrom.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblNameFrom.setMaximumSize(new java.awt.Dimension(200, 20));
        lblNameFrom.setMinimumSize(new java.awt.Dimension(200, 20));
        lblNameFrom.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareTransData.add(lblNameFrom, gridBagConstraints);

        lblNameTo.setForeground(new java.awt.Color(0, 51, 204));
        lblNameTo.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblNameTo.setMaximumSize(new java.awt.Dimension(200, 20));
        lblNameTo.setMinimumSize(new java.awt.Dimension(200, 20));
        lblNameTo.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareTransData.add(lblNameTo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareTransfer.add(panShareTransData, gridBagConstraints);

        panShareTransTab.setMinimumSize(new java.awt.Dimension(400, 100));
        panShareTransTab.setPreferredSize(new java.awt.Dimension(400, 100));
        panShareTransTab.setLayout(new java.awt.GridBagLayout());

        srpShareTrans.setMinimumSize(new java.awt.Dimension(400, 100));
        srpShareTrans.setPreferredSize(new java.awt.Dimension(400, 100));

        tblShareTrans.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Serial No", "Share No From", "Share No To", "No of Shares"
            }
        ));
        srpShareTrans.setViewportView(tblShareTrans);

        panShareTransTab.add(srpShareTrans, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareTransfer.add(panShareTransTab, gridBagConstraints);

        tabShareDetails.addTab("tab1", panShareTransfer);

        panShareClose.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panShareClose.setMaximumSize(new java.awt.Dimension(815, 350));
        panShareClose.setMinimumSize(new java.awt.Dimension(815, 350));
        panShareClose.setPreferredSize(new java.awt.Dimension(815, 350));
        panShareClose.setLayout(new java.awt.GridBagLayout());

        panShareCloseData.setMaximumSize(new java.awt.Dimension(320, 207));
        panShareCloseData.setMinimumSize(new java.awt.Dimension(320, 207));
        panShareCloseData.setPreferredSize(new java.awt.Dimension(320, 207));
        panShareCloseData.setLayout(new java.awt.GridBagLayout());

        lblShareNo.setText("Share No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareCloseData.add(lblShareNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panShareCloseData.add(tdtDividendupTo, gridBagConstraints);

        lblDividendupTo.setText("Dividend Up ToDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareCloseData.add(lblDividendupTo, gridBagConstraints);

        lblShareType.setText("Share Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareCloseData.add(lblShareType, gridBagConstraints);

        cboShareType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboShareType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 1);
        panShareCloseData.add(cboShareType, gridBagConstraints);

        lblDividendAmt.setText("Dividend Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareCloseData.add(lblDividendAmt, gridBagConstraints);

        txtDividendAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareCloseData.add(txtDividendAmt, gridBagConstraints);

        lblShareholderNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblShareholderNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblShareholderNameValue.setMaximumSize(new java.awt.Dimension(200, 18));
        lblShareholderNameValue.setMinimumSize(new java.awt.Dimension(200, 18));
        lblShareholderNameValue.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareCloseData.add(lblShareholderNameValue, gridBagConstraints);

        lblShareholderName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblShareholderName.setText("Share Holder Name");
        lblShareholderName.setMaximumSize(new java.awt.Dimension(131, 18));
        lblShareholderName.setMinimumSize(new java.awt.Dimension(131, 18));
        lblShareholderName.setPreferredSize(new java.awt.Dimension(131, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareCloseData.add(lblShareholderName, gridBagConstraints);

        panShareNo.setLayout(new java.awt.GridBagLayout());

        txtShareNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtShareNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtShareNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panShareNo.add(txtShareNo, gridBagConstraints);

        btnShareNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnShareNo.setToolTipText("Share No");
        btnShareNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnShareNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnShareNo.setEnabled(false);
        btnShareNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShareNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panShareNo.add(btnShareNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panShareCloseData.add(panShareNo, gridBagConstraints);

        lblBatchId.setText("Batch Id :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareCloseData.add(lblBatchId, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareCloseData.add(lblBatchIdValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareClose.add(panShareCloseData, gridBagConstraints);

        panShareCloseTrans.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareClose.add(panShareCloseTrans, gridBagConstraints);

        panTransaction.setMaximumSize(new java.awt.Dimension(785, 250));
        panTransaction.setMinimumSize(new java.awt.Dimension(785, 250));
        panTransaction.setPreferredSize(new java.awt.Dimension(785, 250));
        panTransaction.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panShareClose.add(panTransaction, gridBagConstraints);

        tabShareDetails.addTab("tab1", panShareClose);

        getContentPane().add(tabShareDetails, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");

        mitNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNew.setMnemonic('N');
        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptProcess);

        mitSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitSave.setMnemonic('S');
        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitPrint.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        mitPrint.setMnemonic('P');
        mitPrint.setText("Print");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);
        mnuProcess.add(sptPrint);

        mitClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mitClose.setMnemonic('l');
        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrShareTransfer.add(mnuProcess);

        setJMenuBar(mbrShareTransfer);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void txtShareNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtShareNoActionPerformed
        // TODO add your handling code here:
        HashMap hash=new HashMap();
        hash=observable.setAccountName(txtShareNo.getText());
        if(hash!=null && hash.size()>0){
            cboShareType.setSelectedItem(observable.getCbmShareType().getDataForKey(hash.get("SHARE_TYPE")));
            
        txtShareNo.setText(CommonUtil.convertObjToStr(hash.get("SHARE_ACCT_NO")));
        txtDividendAmt.setText(CommonUtil.convertObjToStr(hash.get("DIVIDEND_AMOUNT")));
        tdtDividendupTo.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
        lblShareholderNameValue.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER_NAME")));
        ClientUtil.enableDisable(panShareCloseData,false);
        btnShareNo.setEnabled(false);
        transactionUI.setCallingAmount(txtDividendAmt.getText());
        }
    }//GEN-LAST:event_txtShareNoActionPerformed
    
    private void btnShareNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShareNoActionPerformed
        // TODO add your handling code here:
        popUp(DIVIDENTACCOUNT);
    }//GEN-LAST:event_btnShareNoActionPerformed
    
    private void btnAcctFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcctFromActionPerformed
        // TODO add your handling code here:
        popUp(ACCTFROM);
    }//GEN-LAST:event_btnAcctFromActionPerformed
    
    private void btnAcctToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcctToActionPerformed
        // TODO add your handling code here:
        if (txtAcctFrom.getText().length()>0)
            popUp(ACCTTO);
        else
            displayAlert("Select From A/c No.");
    }//GEN-LAST:event_btnAcctToActionPerformed
    
    private void txtShareFromFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtShareFromFocusLost
        // TODO add your handling code here:
        if(!isTest){
            String message = txtShareNoRule();
            if(message.length() > 0){
                displayAlert(message);
            }
        }
        
        isTest = false;
    }//GEN-LAST:event_txtShareFromFocusLost
    
    private void txtShareToFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtShareToFocusLost
        // TODO add your handling code here:
        if(!isTest){
            String message = txtShareNoRule();
            HashMap dataMap = new HashMap();
            dataMap.put("SHEREACTNO",txtAcctFrom.getText());
            dataMap.put("SHAREFROM",txtShareFrom.getText());
            dataMap.put("SHARETO",txtShareTo.getText());
            if(!observable.validateShareSelection(dataMap)){
                message+=resourceBundle.getString("SHARENOWARNING");
            }
            dataMap = null;
            if(message.length() > 0){
                displayAlert(message);
                txtShareFrom.setText("");
                txtShareTo.setText("");
                txtShareFrom.requestFocus();
            }
        }
        
        isTest = false;
    }//GEN-LAST:event_txtShareToFocusLost
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
            
            authDataMap.put("TRANSFER FROM", txtAcctFrom.getText());
            //            authDataMap.put("TRANSFER TO", txtAcctTo.getText());
            authDataMap.put("SHARE NO FROM", txtShareFrom.getText());
            authDataMap.put("SHARE NO TO", txtShareTo.getText());
            authDataMap.put("SHARE TRANSFER ID", lblTransNoValue.getText());
            singleAuthorizeMap.put("DIVIDENDAUTHORIZE", "DIVIDENDAUTHORIZE");
            singleAuthorizeMap.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(txtShareNo.getText()));
            singleAuthorizeMap.put("BATCH_ID", CommonUtil.convertObjToStr(lblBatchIdValue.getText()));
            double divAmt=CommonUtil.convertObjToDouble(txtDividendAmt.getText()).doubleValue();
            divAmt=divAmt*-1;
            singleAuthorizeMap.put("DIVIDEND_AMOUNT", new Double(divAmt));
            
            
            authDataMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            arrList.add(authDataMap);
            
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(singleAuthorizeMap);
            isFilled = false;
        } else {
            HashMap mapParam = new HashMap();
            
            //__ To Save the data in the Internal Frame...
            setModified(true);
            
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getShareDiviDendAuthorizeList");
            //            mapParam.put(CommonConstants.MAP_NAME, "getShareTransferAuthorizeList");
            viewType = AUTHORIZE;
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            
            //__ If there's no data to be Authorized, call Cancel action...
            if(!isModified()){
                setButtonEnableDisable();
                btnCancelActionPerformed(null);
            }
        }
    }
    
    public void authorize(HashMap map) {
        observable.setAuthorizeMap(map);
        observable.doAction();
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            isFilled = false;
            btnSave.setEnabled(true);
            btnCancelActionPerformed(null);
            observable.setResultStatus();
        }
        observable.setAuthorizeMap(null);
    }
    
    //__ To avaoid the looping of the testCase...
    boolean isTest = false;        private String txtShareNoRule(){
        isTest = true;
        String message = "";
        final String SHAREFROM = txtShareFrom.getText();
        final String SHARETO = txtShareTo.getText();
        if(!(SHAREFROM.equalsIgnoreCase("")
        || SHARETO.equalsIgnoreCase(""))){
            if(SHAREFROM.compareTo(SHARETO) > 0){
                message = resourceBundle.getString("SHAREWARNING");
            }
        }
        return message;
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.resetForm();                 //__ Reset the fields in the UI to null...
        clearAllDividendPay();
        observable.resetTable();
        btnSave.setEnabled(false);
        resetLables();
        ClientUtil.enableDisable(this, false);  //__ Disables the panel...
        setButtonEnableDisable();               //__ Enables or Disables the buttons and menu Items depending on their previous state...
        setEnableDisableButtons(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
       transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_CANCEL);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);  //__ Sets the Action Type to be performed...
        observable.setStatus();                 //__ To set the Value of lblStatus..
        transactionUI.setCallingAmount("0.0");
        
        
        viewType = -1;
        resetTransactionUI();
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
     private void resetTransactionUI(){
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        StringBuffer strBAlert = new StringBuffer();
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panShareTransData);
        //__ Perform the Validation...
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            strBAlert.append(mandatoryMessage+"\n");
        }
        if(txtShareNoRule().length() > 0){
            strBAlert.append(txtShareNoRule()+"\n");
        }
        HashMap dataMap = new HashMap();
        dataMap.put("SHEREACTNO",txtAcctFrom.getText());
        dataMap.put("SHAREFROM",txtShareFrom.getText());
        dataMap.put("SHARETO",txtShareTo.getText());
        if (observable.getDivAmt()==null && CommonUtil.convertObjToDouble(txtDividendAmt.getText()).doubleValue()==0.0){
            if(!observable.validateShareSelection(dataMap) ) {
                strBAlert.append(resourceBundle.getString("SHARENOWARNING"));
            }
        }
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && strBAlert.toString().length() > 0 ){
            displayAlert(strBAlert.toString());
        }else{
            if(observable.getDivAmt().doubleValue()>0.0){
                if( transactionUI.getOutputTO() == null || (transactionUI.getOutputTO()).size() == 0){
                    ClientUtil.displayAlert("Credit Transaction Not Their");
                }else if(CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue()!=observable.getDivAmt().doubleValue()){
                    ClientUtil.displayAlert("Credit Transaction And Debit Transaction Not Tailed ");
                }
                else{
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                }
                
            }
            if(CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue()==observable.getDivAmt().doubleValue())
            observable.doAction();// To perform the necessary operation depending on the Action type...
            //__ If the Operation is Not Failed, Clear the Screen...
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                observable.resetForm();                    //__ Reset the fields in the UI to null...
                observable.resetTable();
                clearAllDividendPay();
                resetLables();
                ClientUtil.enableDisable(this, false);     //__ Disables the panel...
                setButtonEnableDisable();                  //__ Enables or Disables the buttons and menu Items depending on their previous state...
                setEnableDisableButtons(false);
                observable.setResultStatus();              //__ To Reset the Value of lblStatus...
                btnCancelActionPerformed(null);
//                transactionUI.setCallingAmount("0.0");
                
                //__ Make the Screen Closable..
                setModified(false);
            }
        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE); //__ Sets the Action Type to be performed...
        popUp(DELETE);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT); //__ Sets the Action Type to be performed...
        popUp(EDIT);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        if(field==EDIT || field==DELETE || field== AUTHORIZE){ //Edit=0 and Delete=1
            viewMap.put(CommonConstants.MAP_NAME, "viewShareDividendPayData");
            //viewMap.put(CommonConstants.MAP_NAME, "viewShareTransferData"); for transfer
            new ViewAll(this, viewMap).show();
        }
        else if(field == ACCTFROM){
            viewMap.put(CommonConstants.MAP_NAME, "getShareTransData");
            new ViewAll(this, viewMap, true).show();
            
        }else if(field == ACCTTO){
            HashMap whereMap = new HashMap();
            whereMap.put("SHAREACCTNO", txtAcctFrom.getText());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getShareTransData");
            whereMap = null;
            new ViewAll(this, viewMap, true).show();
        }
        //        new ViewAll(this, viewMap).show();
        
        
        else if(field==ACCOUNT_W_C ){
            viewMap.put(CommonConstants.MAP_NAME, "getShareTransData");
            new ViewAll(this, viewMap, true).show();
            viewType=5;
        } else if(field==DIVIDENTACCOUNT ){
            
            String ShareType=CommonUtil.convertObjToStr(cboShareType.getSelectedItem());
            
            if(!ShareType.equals("") && ShareType.length()>0){
                HashMap whereMap = new HashMap();
                observable.setCboShareType(CommonUtil.convertObjToStr(cboShareType.getSelectedItem()));
                whereMap.put("SHARE_TYPE", observable.callForBehaves());
                whereMap.put("DIVIDEND_PAID_STATUS","DIVIDEND_PAID_STATUS");
                viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                viewMap.put(CommonConstants.MAP_NAME, "getSelectDividendUnclaimedTransferList");
                new ViewAll(this, viewMap, true).show();
                viewType=6;
            } else{
                ClientUtil.displayAlert("Please Select The ShareType");
            }
        }
        
        
        
    }
    private void clearAllDividendPay(){
        txtShareNo.setText("");
        txtDividendAmt.setText("");
        tdtDividendupTo.setDateValue(null);
        lblShareholderNameValue.setText("");
        btnShareNo.setEnabled(false);
        ClientUtil.enableDisable(panShareCloseData,false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        
    }
    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if (viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE ) {
            hash.put("DIVIDEND","DIVIDEND");
            hash.put(CommonConstants.MAP_WHERE,hash.get("BATCH_ID"));
            
            
            //            hash.put(CommonConstants.MAP_WHERE, CommonUtil.convertObjToStr(hash.get("SHARE TRANSFER ID")));
            
            observable.populateData(hash);     //__ Called to display the Data in the UI fields...
            lblShareholderNameValue.setText(CommonUtil.convertObjToStr(hash.get("FNAME")));
            lblBatchIdValue.setText(CommonUtil.convertObjToStr(hash.get("BATCH_ID")));
            
            //            observable.setShareTransTabData();
            
            //            lblNameFrom.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER FROM")));
            //            lblNameTo.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER TO")));
            
            //__ If the Action type is Delete...
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType==AUTHORIZE ) {
                ClientUtil.enableDisable(this, false);     //__ Disables the panel...
                setEnableDisableButtons(false);
                
                //__ If the Action Type is Edit...
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                ClientUtil.enableDisable(this, false);     //__ Ennable the panel...
                setEnableDisableButtons(false);
                
            }
            
            observable.setStatus();             //__ To set the Value of lblStatus...
            setButtonEnableDisable();           //__ Enables or Disables the buttons and menu Items depending on their previous state...
            if (viewType== AUTHORIZE ) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
            this.isFilled = true;
            
        }else if(viewType==ACCTFROM){
            //txtAcctFrom.setText(CommonUtil.convertObjToStr(hash.get("SHARE ACCT NO")));
            observable.setTxtAcctFrom(CommonUtil.convertObjToStr(hash.get("SHARE ACCT NO")));
            //__ To get the Data in the ShareTrans Table...
            observable.setShareTransTabData();
            observable.ttNotifyObservers();
            
            lblNameFrom.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
        }else if(viewType==ACCTTO){
            //            txtAcctTo.setText(CommonUtil.convertObjToStr(hash.get("SHARE ACCT NO")));
            lblNameTo.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
        }
        
        else if(viewType==DIVIDENTACCOUNT){
            List lst1=ClientUtil.executeQuery("aunthorizedDividend", hash);
            if(lst1!=null && lst1.size()>0 ){
                ClientUtil.displayAlert("Dividend payment  Authorization pending for this Share No");
            }else{
            txtShareNo.setText(CommonUtil.convertObjToStr(hash.get("SHARE_ACCT_NO")));
            txtDividendAmt.setText(CommonUtil.convertObjToStr(hash.get("DIVIDEND_AMOUNT")));
            tdtDividendupTo.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
            lblShareholderNameValue.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER_NAME")));
            ClientUtil.enableDisable(panShareCloseData,false);
            btnShareNo.setEnabled(false);
            transactionUI.setCallingAmount(txtDividendAmt.getText());
            }
            lst1=null;
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
         transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_CANCEL);
        observable.resetForm();               // to Reset all the Fields and Status in UI...
        clearAllDividendPay();
        observable.resetTable();
        resetLables();
        setButtonEnableDisable();             // Enables/Disables the necessary buttons and menu items...
        setEnableDisableButtons(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(this, true); // Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);   //Sets the Action Type to be performed...
        observable.setStatus();               // To set the Value of lblStatus...
        btnShareNo.setEnabled(true);
        btnSave.setEnabled(true);
        cboShareType.setEnabled(true);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        transactionUI.setButtonEnableDisable(true);
        
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed
    private void setEnableDisableButtons(boolean value){
        btnAcctFrom.setEnabled(value);
        //        btnAcctTo.setEnabled(value);
    }
    
    private void resetLables(){
        lblNameFrom.setText("");
        lblNameTo.setText("");
    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(null);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // TODO add your handling code here:
        btnPrintActionPerformed(null);
    }//GEN-LAST:event_mitPrintActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(null);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(null);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(null);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(null);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(null);
    }//GEN-LAST:event_mitNewActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new ShareTransferUI().show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAcctFrom;
    private com.see.truetransact.uicomponent.CButton btnAcctTo;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnShareNo;
    private com.see.truetransact.uicomponent.CComboBox cboShareType;
    private com.see.truetransact.uicomponent.CLabel lblAcctFrom;
    private com.see.truetransact.uicomponent.CLabel lblAcctTo;
    private com.see.truetransact.uicomponent.CLabel lblBatchId;
    private com.see.truetransact.uicomponent.CLabel lblBatchIdValue;
    private com.see.truetransact.uicomponent.CLabel lblDividendAmt;
    private com.see.truetransact.uicomponent.CLabel lblDividendupTo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNameFrom;
    private com.see.truetransact.uicomponent.CLabel lblNameTo;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblShareFrom;
    private com.see.truetransact.uicomponent.CLabel lblShareNo;
    private com.see.truetransact.uicomponent.CLabel lblShareTo;
    private com.see.truetransact.uicomponent.CLabel lblShareType;
    private com.see.truetransact.uicomponent.CLabel lblShareholderName;
    private com.see.truetransact.uicomponent.CLabel lblShareholderNameValue;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CLabel lblTransNo;
    private com.see.truetransact.uicomponent.CLabel lblTransNoValue;
    private com.see.truetransact.uicomponent.CMenuBar mbrShareTransfer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panShareClose;
    private com.see.truetransact.uicomponent.CPanel panShareCloseData;
    private com.see.truetransact.uicomponent.CPanel panShareCloseTrans;
    private com.see.truetransact.uicomponent.CPanel panShareNo;
    private com.see.truetransact.uicomponent.CPanel panShareTransData;
    private com.see.truetransact.uicomponent.CPanel panShareTransTab;
    private com.see.truetransact.uicomponent.CPanel panShareTransfer;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpShareTrans;
    private javax.swing.JTabbedPane tabShareDetails;
    private com.see.truetransact.uicomponent.CTable tblShareTrans;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CDateField tdtDividendupTo;
    private com.see.truetransact.uicomponent.CTextField txtAcctFrom;
    private com.see.truetransact.uicomponent.CTextField txtAcctTo;
    private com.see.truetransact.uicomponent.CTextField txtDividendAmt;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtShareFrom;
    private com.see.truetransact.uicomponent.CTextField txtShareNo;
    private com.see.truetransact.uicomponent.CTextField txtShareTo;
    // End of variables declaration//GEN-END:variables
    
}
