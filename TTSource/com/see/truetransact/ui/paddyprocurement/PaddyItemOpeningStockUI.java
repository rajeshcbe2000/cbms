/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductUI.java
 *
 * Created on November 23, 2004, 4:00 PM
 */

package com.see.truetransact.ui.paddyprocurement;

/**
 *
 * @author 
 *      
 */

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.AcctStatusConstants;

import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
public class PaddyItemOpeningStockUI extends CInternalFrame implements UIMandatoryField, Observer{
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.paddyprocurement.PaddyItemOpeningStockRB", ProxyParameters.LANGUAGE);
    HashMap mandatoryMap = new HashMap();
    PaddyItemOpeningStockOB observable;
    PaddyItemOpeningStockMRB objMandatoryRB;
    MandatoryCheck objMandatoryCheck = new MandatoryCheck();
    private final String CLASSNAME = this.getClass().getName();
    boolean flag = false;
    final int AUTHORIZE=3;
    final int DELETE = 1;
    private String actionType = "";
    private int viewType=-1;
    boolean isFilled = false;
    final int LIABILITYGL = 8;
    final int EXPENDITUREGL = 9;
    private String ACCT_TYPE = "ACCT_TYPE";
    private String BALANCETYPE = "BALANCETYPE";
    private Date currDt = null;
    /** Creates new form ShareProductUI */
    public PaddyItemOpeningStockUI() {
        Date currDt = ClientUtil.getCurrentDate();
        initComponents();
        setFieldNames();
        internationalize();
        setHelpMessage();
        setObservable();
        initComponentData();
        setMandatoryHashMap();
        setMaxLengths();
        ClientUtil.enableDisable(panPaddyItemOpening, false, false, true);
        setButtonEnableDisable();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panPaddyItemOpening);
        btnView.setEnabled(true);
        visibleFalse();
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
        lblStatus.setText(observable.getLblStatus());
    }
    
    private void setMaxLengths() {
        txtItemCode.setMaxLength(12);
        txtItemCode.setAllowAll(true);
        txtItemDesc.setMaxLength(64);
        txtItemDesc.setAllowAll(true);
        txtPurchasePrice.setMaxLength(14);
        txtPurchasePrice.setValidation(new CurrencyValidation());
        txtSellingPrice.setMaxLength(14);
        txtSellingPrice.setValidation(new CurrencyValidation());
        txtQty.setMaxLength(8);
        txtQty.setValidation(new NumericValidation());
        txtOrderLevel.setMaxLength(8);
        txtOrderLevel.setAllowAll(true);
        txtPurchaseAcHd.setMaxLength(64);
        txtPurchaseAcHd.setAllowAll(true);
        txtPurchaseReturnAcHd.setMaxLength(64);
        txtPurchaseReturnAcHd.setAllowAll(true);
        txtSalesAcHd.setMaxLength(64);
        txtSalesAcHd.setAllowAll(true);
        txtSalesReturnAcHd.setMaxLength(64);
        txtSalesReturnAcHd.setAllowAll(true);
        txtTaxAcHd.setMaxLength(64);
        txtTaxAcHd.setAllowAll(true);
    }
    
    private void initComponentData() {
        cboUnit.setModel(observable.getCbmUnit());
    }
    private void setObservable() {
        try{
            observable = PaddyItemOpeningStockOB.getInstance(); 
            observable.addObserver(this);
        }catch(Exception e){
            System.out.println("Exception is caught "+e);
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
        btnPurchaseAcHd.setName("btnPurchaseAcHd");
        btnPurchaseReturnAcHd.setName("btnPurchaseReturnAcHd");
        btnReject.setName("btnReject");
        btnSalesAcHd.setName("btnSalesAcHd");
        btnSalesReturnAcHd.setName("btnSalesReturnAcHd");
        btnSave.setName("btnSave");
        btnTaxAcHd.setName("btnTaxAcHd");
        btnView.setName("btnView");
        cboUnit.setName("cboUnit");
        lblMsg.setName("lblMsg");
        lblItemCode.setName("lblItemCode");
        lblItemDesc.setName("lblItemDesc");
        lblPurchaseAcHd.setName("lblPurchaseAcHd");
        lblPurchasePrice.setName("lblPurchasePrice");
        lblPurchaseReturnAcHd.setName("lblPurchaseReturnAcHd");
        lblQty.setName("lblQty");
        lblOrderLevel.setName("lblOrderLevel");
        lblSalesAcHd.setName("lblSalesAcHd");
        lblSalesReturnAcHd.setName("lblSalesReturnAcHd");
        lblSellingPrice.setName("lblSellingPrice");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        lblTaxAcHd.setName("lblTaxAcHd");
        lblUnit.setName("lblUnit");
        mbrShareProduct.setName("mbrShareProduct");
        panPaddyItemOpening.setName("panPaddyItemOpening");
        panStatus.setName("panStatus");
        txtItemCode.setName("txtItemCode");
        txtItemDesc.setName("txtItemDesc");
        txtPurchaseAcHd.setName("txtPurchaseAcHd");
        txtPurchasePrice.setName("txtPurchasePrice");
        txtPurchaseReturnAcHd.setName("txtPurchaseReturnAcHd");
        txtQty.setName("txtQty");
        txtOrderLevel.setName("txtOrderLevel");
        txtSalesAcHd.setName("txtSalesAcHd");
        txtSalesReturnAcHd.setName("txtSalesReturnAcHd");
        txtSellingPrice.setName("txtSellingPrice");
        txtTaxAcHd.setName("txtTaxAcHd");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblSalesAcHd.setText(resourceBundle.getString("lblSalesAcHd"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblItemCode.setText(resourceBundle.getString("lblItemCode"));
        lblQty.setText(resourceBundle.getString("lblQty"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblItemDesc.setText(resourceBundle.getString("lblItemDesc"));
        lblPurchaseAcHd.setText(resourceBundle.getString("lblPurchaseAcHd"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        btnTaxAcHd.setText(resourceBundle.getString("btnTaxAcHd"));
        btnPurchaseAcHd.setText(resourceBundle.getString("btnPurchaseAcHd"));
        lblOrderLevel.setText(resourceBundle.getString("lblOrderLevel"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnView.setText(resourceBundle.getString("btnView"));
        lblSellingPrice.setText(resourceBundle.getString("lblSellingPrice"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblUnit.setText(resourceBundle.getString("lblUnit"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblTaxAcHd.setText(resourceBundle.getString("lblTaxAcHd"));
        btnSalesAcHd.setText(resourceBundle.getString("btnSalesAcHd"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblPurchasePrice.setText(resourceBundle.getString("lblPurchasePrice"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtItemCode.setText(observable.getTxtItemCode());
        txtItemDesc.setText(observable.getTxtItemDesc()); 
        txtPurchasePrice.setText(observable.getTxtPurchasePrice());
        txtSellingPrice.setText(observable.getTxtSellingPrice());
        cboUnit.setSelectedItem(observable.getCboUnit());
        txtQty.setText(observable.getTxtQty());
        txtOrderLevel.setText(observable.getTxtOrderLevel());
        txtPurchaseAcHd.setText(observable.getTxtPurchaseAcHd());
        txtSalesAcHd.setText(observable.getTxtSalesAcHd());
        txtTaxAcHd.setText(observable.getTxtTaxAcHd());
        txtPurchaseReturnAcHd.setText(observable.getTxtPurchaseReturnAcHd());
        txtSalesReturnAcHd.setText(observable.getTxtSalesReturnAcHd());
    }
//    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtItemCode(txtItemCode.getText()); 
        observable.setTxtItemDesc(txtItemDesc.getText()); 
        observable.setTxtPurchasePrice(txtPurchasePrice.getText());
        observable.setTxtSellingPrice(txtSellingPrice.getText());
        observable.setCboUnit((String) cboUnit.getSelectedItem());
        observable.setTxtQty(txtQty.getText());
        observable.setTxtOrderLevel(txtOrderLevel.getText());
        observable.setTxtPurchaseAcHd(txtPurchaseAcHd.getText());
        observable.setTxtSalesAcHd(txtSalesAcHd.getText());
        observable.setTxtTaxAcHd(txtTaxAcHd.getText());
        observable.setTxtPurchaseReturnAcHd(txtPurchaseReturnAcHd.getText());
        observable.setTxtSalesReturnAcHd(txtSalesReturnAcHd.getText());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtProductDesc", new Boolean(true));
        mandatoryMap.put("txtProductCode", new Boolean(true));
        mandatoryMap.put("txtPurchasePrice", new Boolean(true));
        mandatoryMap.put("txtSellingPrice", new Boolean(true));
        mandatoryMap.put("cboUnit", new Boolean(true));
        mandatoryMap.put("txtQty", new Boolean(true));
        mandatoryMap.put("txtReorderLevel", new Boolean(true));
        mandatoryMap.put("txtPurchaseAcHd", new Boolean(true));
        mandatoryMap.put("txtSalesAcHd", new Boolean(true));
        mandatoryMap.put("txtTaxAcHd", new Boolean(true));
        mandatoryMap.put("txtPurchaseReturnAcHd", new Boolean(true));
        mandatoryMap.put("txtSalesReturnAcHd", new Boolean(true));
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
        objMandatoryRB = new PaddyItemOpeningStockMRB();
//        txtItemCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtItemCode"));
//        txtItemDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtItemDesc"));
//        txtPurchasePrice.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPurchasePrice"));
//        txtSellingPrice.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSellingPrice"));
//        cboUnit.setHelpMessage(lblMsg, objMandatoryRB.getString("cboUnit"));
//        txtQty.setHelpMessage(lblMsg, objMandatoryRB.getString("txtQty"));
//        txtOrderLevel.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOrderLevel"));
//        txtPurchaseAcHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPurchaseAcHd"));
//        txtSalesAcHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSalesAcHd"));
//        txtTaxAcHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTaxAcHd"));
//        txtPurchaseReturnAcHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPurchaseReturnAcHd"));
//        txtSalesReturnAcHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSalesReturnAcHd"));
    }
    
    
    
    
    public static void main(String args[]){
        javax.swing.JFrame frame = new javax.swing.JFrame();
        PaddyItemOpeningStockUI ui = new PaddyItemOpeningStockUI();
        frame.getContentPane().add(ui);
        ui.setVisible(true);
        frame.setVisible(true);
        frame.setSize(600,600);
        frame.show();
        ui.show();
        
        
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrShareProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace51 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace52 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace53 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace54 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace55 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panPaddyItemOpening = new com.see.truetransact.uicomponent.CPanel();
        lblItemDesc = new com.see.truetransact.uicomponent.CLabel();
        txtItemDesc = new com.see.truetransact.uicomponent.CTextField();
        lblItemCode = new com.see.truetransact.uicomponent.CLabel();
        txtItemCode = new com.see.truetransact.uicomponent.CTextField();
        lblPurchasePrice = new com.see.truetransact.uicomponent.CLabel();
        txtPurchasePrice = new com.see.truetransact.uicomponent.CTextField();
        lblSellingPrice = new com.see.truetransact.uicomponent.CLabel();
        txtSellingPrice = new com.see.truetransact.uicomponent.CTextField();
        lblUnit = new com.see.truetransact.uicomponent.CLabel();
        cboUnit = new com.see.truetransact.uicomponent.CComboBox();
        lblQty = new com.see.truetransact.uicomponent.CLabel();
        txtQty = new com.see.truetransact.uicomponent.CTextField();
        lblOrderLevel = new com.see.truetransact.uicomponent.CLabel();
        txtOrderLevel = new com.see.truetransact.uicomponent.CTextField();
        lblPurchaseAcHd = new com.see.truetransact.uicomponent.CLabel();
        txtPurchaseAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnPurchaseAcHd = new com.see.truetransact.uicomponent.CButton();
        lblSalesAcHd = new com.see.truetransact.uicomponent.CLabel();
        txtSalesAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnSalesAcHd = new com.see.truetransact.uicomponent.CButton();
        lblTaxAcHd = new com.see.truetransact.uicomponent.CLabel();
        txtTaxAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnTaxAcHd = new com.see.truetransact.uicomponent.CButton();
        txtPurchaseReturnAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnPurchaseReturnAcHd = new com.see.truetransact.uicomponent.CButton();
        lblPurchaseReturnAcHd = new com.see.truetransact.uicomponent.CLabel();
        lblSalesReturnAcHd = new com.see.truetransact.uicomponent.CLabel();
        txtSalesReturnAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnSalesReturnAcHd = new com.see.truetransact.uicomponent.CButton();
        mbrShareProduct = new com.see.truetransact.uicomponent.CMenuBar();
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
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(800, 400));
        setMinimumSize(new java.awt.Dimension(800, 400));
        setPreferredSize(new java.awt.Dimension(800, 400));

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
        tbrShareProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrShareProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnNew);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace52);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrShareProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnSave);

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace53);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrShareProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnAuthorize);

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace55);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrShareProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrShareProduct.add(btnPrint);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace56);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnClose);

        getContentPane().add(tbrShareProduct, java.awt.BorderLayout.NORTH);

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

        panPaddyItemOpening.setMaximumSize(new java.awt.Dimension(800, 350));
        panPaddyItemOpening.setMinimumSize(new java.awt.Dimension(800, 350));
        panPaddyItemOpening.setPreferredSize(new java.awt.Dimension(800, 350));
        panPaddyItemOpening.setLayout(new java.awt.GridBagLayout());

        lblItemDesc.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPaddyItemOpening.add(lblItemDesc, gridBagConstraints);

        txtItemDesc.setMaximumSize(new java.awt.Dimension(300, 21));
        txtItemDesc.setMinimumSize(new java.awt.Dimension(300, 21));
        txtItemDesc.setPreferredSize(new java.awt.Dimension(300, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPaddyItemOpening.add(txtItemDesc, gridBagConstraints);

        lblItemCode.setText("Item Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPaddyItemOpening.add(lblItemCode, gridBagConstraints);

        txtItemCode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtItemCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtItemCodeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaddyItemOpening.add(txtItemCode, gridBagConstraints);

        lblPurchasePrice.setText("Purchase Price/Unit");
        lblPurchasePrice.setName("lblStreet");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPaddyItemOpening.add(lblPurchasePrice, gridBagConstraints);

        txtPurchasePrice.setMaxLength(256);
        txtPurchasePrice.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPurchasePrice.setName("txtStreet");
        txtPurchasePrice.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPaddyItemOpening.add(txtPurchasePrice, gridBagConstraints);

        lblSellingPrice.setText("Selling Price/Unit");
        lblSellingPrice.setName("lblStreet");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPaddyItemOpening.add(lblSellingPrice, gridBagConstraints);

        txtSellingPrice.setMaxLength(256);
        txtSellingPrice.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSellingPrice.setName("txtStreet");
        txtSellingPrice.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPaddyItemOpening.add(txtSellingPrice, gridBagConstraints);

        lblUnit.setText("Units in");
        lblUnit.setName("lblCity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPaddyItemOpening.add(lblUnit, gridBagConstraints);

        cboUnit.setMinimumSize(new java.awt.Dimension(100, 21));
        cboUnit.setName("cboCity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPaddyItemOpening.add(cboUnit, gridBagConstraints);

        lblQty.setText("Quantity in store");
        lblQty.setName("lblPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPaddyItemOpening.add(lblQty, gridBagConstraints);

        txtQty.setMaxLength(16);
        txtQty.setMinimumSize(new java.awt.Dimension(100, 21));
        txtQty.setName("txtPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPaddyItemOpening.add(txtQty, gridBagConstraints);

        lblOrderLevel.setText("Order level");
        lblOrderLevel.setName("lblPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPaddyItemOpening.add(lblOrderLevel, gridBagConstraints);

        txtOrderLevel.setMaxLength(16);
        txtOrderLevel.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOrderLevel.setName("txtPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPaddyItemOpening.add(txtOrderLevel, gridBagConstraints);

        lblPurchaseAcHd.setText("Purchase Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaddyItemOpening.add(lblPurchaseAcHd, gridBagConstraints);

        txtPurchaseAcHd.setEditable(false);
        txtPurchaseAcHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurchaseAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaddyItemOpening.add(txtPurchaseAcHd, gridBagConstraints);

        btnPurchaseAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPurchaseAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnPurchaseAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPurchaseAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPurchaseAcHd.setEnabled(false);
        btnPurchaseAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPaddyItemOpening.add(btnPurchaseAcHd, gridBagConstraints);

        lblSalesAcHd.setText("Sales Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaddyItemOpening.add(lblSalesAcHd, gridBagConstraints);

        txtSalesAcHd.setEditable(false);
        txtSalesAcHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSalesAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaddyItemOpening.add(txtSalesAcHd, gridBagConstraints);

        btnSalesAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSalesAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSalesAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSalesAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSalesAcHd.setEnabled(false);
        btnSalesAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPaddyItemOpening.add(btnSalesAcHd, gridBagConstraints);

        lblTaxAcHd.setText("Tax Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaddyItemOpening.add(lblTaxAcHd, gridBagConstraints);

        txtTaxAcHd.setEditable(false);
        txtTaxAcHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTaxAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaddyItemOpening.add(txtTaxAcHd, gridBagConstraints);

        btnTaxAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnTaxAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnTaxAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnTaxAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnTaxAcHd.setEnabled(false);
        btnTaxAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaxAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPaddyItemOpening.add(btnTaxAcHd, gridBagConstraints);

        txtPurchaseReturnAcHd.setEditable(false);
        txtPurchaseReturnAcHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurchaseReturnAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaddyItemOpening.add(txtPurchaseReturnAcHd, gridBagConstraints);

        btnPurchaseReturnAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPurchaseReturnAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnPurchaseReturnAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPurchaseReturnAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPurchaseReturnAcHd.setEnabled(false);
        btnPurchaseReturnAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseReturnAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPaddyItemOpening.add(btnPurchaseReturnAcHd, gridBagConstraints);

        lblPurchaseReturnAcHd.setText("Purchase Return Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaddyItemOpening.add(lblPurchaseReturnAcHd, gridBagConstraints);

        lblSalesReturnAcHd.setText("Sales Return Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaddyItemOpening.add(lblSalesReturnAcHd, gridBagConstraints);

        txtSalesReturnAcHd.setEditable(false);
        txtSalesReturnAcHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSalesReturnAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaddyItemOpening.add(txtSalesReturnAcHd, gridBagConstraints);

        btnSalesReturnAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSalesReturnAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSalesReturnAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSalesReturnAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSalesReturnAcHd.setEnabled(false);
        btnSalesReturnAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesReturnAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPaddyItemOpening.add(btnSalesReturnAcHd, gridBagConstraints);

        getContentPane().add(panPaddyItemOpening, java.awt.BorderLayout.CENTER);

        mbrShareProduct.setName("mbrCustomer");

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
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrShareProduct.add(mnuProcess);

        setJMenuBar(mbrShareProduct);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtItemCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtItemCodeFocusLost
        // TODO add your handling code here:
        if(txtItemCode.getText().length()>0){
            txtItemCode.setText(CommonUtil.convertObjToStr(txtItemCode.getText()).toUpperCase());
            String itemCode = CommonUtil.convertObjToStr(txtItemCode.getText());
            HashMap locCodeChkMap = new HashMap();
            locCodeChkMap.put("ITEM_CODE",itemCode);
            List list = ClientUtil.executeQuery("getItemCodeChk", locCodeChkMap);
            if(list!= null && list.size() > 0){
                ClientUtil.showAlertWindow("Item Code Already exists!!!");
                txtItemCode.setText("");
            }
        }
        
    }//GEN-LAST:event_txtItemCodeFocusLost

    private void btnTaxAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaxAcHdActionPerformed
        // TODO add your handling code here:
        popUp("TAX_AC_HEAD");
    }//GEN-LAST:event_btnTaxAcHdActionPerformed

    private void btnSalesReturnAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesReturnAcHdActionPerformed
        // TODO add your handling code here:
        popUp("SALES_RETURN_AC_HEAD");
    }//GEN-LAST:event_btnSalesReturnAcHdActionPerformed

    private void btnSalesAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesAcHdActionPerformed
        // TODO add your handling code here:
        popUp("SALES_AC_HEAD");
    }//GEN-LAST:event_btnSalesAcHdActionPerformed

    private void btnPurchaseReturnAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseReturnAcHdActionPerformed
        // TODO add your handling code here:
        popUp("PURCHASE_RETURN_AC_HEAD");
    }//GEN-LAST:event_btnPurchaseReturnAcHdActionPerformed

    private void btnPurchaseAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseAcHdActionPerformed
        // TODO add your handling code here:
        popUp("PURCHASE_AC_HEAD");
    }//GEN-LAST:event_btnPurchaseAcHdActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        viewType = ClientConstants.ACTIONTYPE_DELETE;
        observable.setStatus(); 
        popUp(ClientConstants.ACTION_STATUS[3]);
        ClientUtil.enableDisable(this, false);
        ClientUtil.enableDisable(panPaddyItemOpening,false);
        lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void visibleFalse(){
//        btnSalesAcHd.setVisible(false);
//        btnPurchaseAcHd.setVisible(false);
        lblPurchaseReturnAcHd.setVisible(false);
        txtPurchaseReturnAcHd.setVisible(false);
        lblSalesReturnAcHd.setVisible(false);
        txtSalesReturnAcHd.setVisible(false);
        lblTaxAcHd.setVisible(false);
        txtTaxAcHd.setVisible(false);
        btnPurchaseReturnAcHd.setVisible(false);
        btnSalesReturnAcHd.setVisible(false);
        btnTaxAcHd.setVisible(false);
    }
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        viewType = ClientConstants.ACTIONTYPE_EDIT;
        observable.setStatus(); 
        popUp(ClientConstants.ACTION_STATUS[2]);
        setModified(true);
        observable.setStatus();
        btnEdit.setEnabled(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(this,false);
        btnCancel.setEnabled(true);
        btnSave.setEnabled(true);
        lblStatus.setText(observable.getLblStatus());
        ClientUtil.enableDisable(panPaddyItemOpening,true);
        txtItemCode.setEnabled(false);
        lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnEditActionPerformed
private void popUp(String actionType){
        this.actionType = actionType;
        if(actionType != null){
            final HashMap viewMap = new HashMap();
            HashMap wheres = new HashMap();
            
            if(actionType.equals(ClientConstants.ACTION_STATUS[2]))  {
                //                for Edit
                ArrayList lst = new ArrayList(); 
                lst.add("DRF NO");
                viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                viewMap.put(CommonConstants.MAP_NAME, "getSelectPaddyItemOpeningTOList");
                viewMap.put(CommonConstants.MAP_WHERE, wheres);
            } else if (actionType.equals(ClientConstants. ACTION_STATUS[3])){
                viewMap.put(CommonConstants.MAP_NAME, "getSelectPaddyItemOpeningTOList");
                viewMap.put(CommonConstants.MAP_WHERE, wheres);
            }
            else if(actionType.equals("ViewDetails")){
                HashMap where = new HashMap();
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "getSelectPaddyItemOpeningTOList");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }
            else if(actionType.equals("PURCHASE_AC_HEAD")){
                viewType = LIABILITYGL;
                HashMap where = new HashMap();
                where.put(ACCT_TYPE, AcctStatusConstants.LIABILITY);  
//                whereMap.put(ACCT_TYPE, AcctStatusConstants.AST_LIAB);
                where.put(BALANCETYPE, CommonConstants.CREDIT); 
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "product.deposits.getAcctHeadList");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }else if(actionType.equals("SALES_AC_HEAD")){
                viewType = LIABILITYGL;
                HashMap where = new HashMap();
                where.put(ACCT_TYPE, AcctStatusConstants.LIABILITY);  
//                whereMap.put(ACCT_TYPE, AcctStatusConstants.AST_LIAB);
                where.put(BALANCETYPE, CommonConstants.CREDIT); 
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "product.deposits.getAcctHeadList");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }
            
            else if(actionType.equals("PURCHASE_RETURN_AC_HEAD")){
                viewType = LIABILITYGL;
                HashMap where = new HashMap();
                where.put(ACCT_TYPE, AcctStatusConstants.LIABILITY);  
//                whereMap.put(ACCT_TYPE, AcctStatusConstants.AST_LIAB);
                where.put(BALANCETYPE, CommonConstants.CREDIT); 
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "product.deposits.getAcctHeadList");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }
            else if(actionType.equals("SALES_RETURN_AC_HEAD")){
                viewType = LIABILITYGL;
                HashMap where = new HashMap();
                where.put(ACCT_TYPE, AcctStatusConstants.LIABILITY);  
//                whereMap.put(ACCT_TYPE, AcctStatusConstants.AST_LIAB);
                where.put(BALANCETYPE, CommonConstants.CREDIT); 
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "product.deposits.getAcctHeadList");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }
            else if(actionType.equals("TAX_AC_HEAD")){
                viewType = LIABILITYGL;
                HashMap where = new HashMap();
                where.put(ACCT_TYPE, AcctStatusConstants.LIABILITY);  
//                whereMap.put(ACCT_TYPE, AcctStatusConstants.AST_LIAB);
                where.put(BALANCETYPE, CommonConstants.CREDIT); 
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "product.deposits.getAcctHeadList");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }
            
            new com.see.truetransact.ui.common.viewall.ViewAll(this, viewMap,true).show();
        }
        
    }
    
    /** To get data based on customer id received from the popup and populate into the
     * screen
     */
    public void fillData(Object obj) {
        isFilled = true;
        setModified(true);
        final HashMap hash = (HashMap) obj;
        System.out.println("@@@@hash"+hash);
        String st = CommonUtil.convertObjToStr(hash.get("STATUS")); 
        if(st.equalsIgnoreCase("DELETED")) {
            flag = true;
        }
        if(hash.get("ACCOUNT HEAD")!=null && actionType.equals("PURCHASE_AC_HEAD")){
            txtPurchaseAcHd.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT HEAD")));
            txtPurchaseAcHd.setEnabled(false);
        }
        else if(hash.get("ACCOUNT HEAD")!=null && actionType.equals("SALES_AC_HEAD")){
            txtSalesAcHd.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT HEAD")));
            txtSalesAcHd.setEnabled(false);
        }
        else if(actionType.equals(ClientConstants.ACTION_STATUS[2]) ||
        actionType.equals(ClientConstants.ACTION_STATUS[3])|| actionType.equals("DeletedDetails") || actionType.equals("ViewDetails")|| viewType == AUTHORIZE ||
        getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        ){
            HashMap map = new HashMap();
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                map.put("ITEM_CODE",hash.get("ITEM_CODE"));
                map.put(CommonConstants.MAP_WHERE, hash.get("ITEM_CODE"));
            }else{
                map.put("ITEM_CODE",hash.get("ITEM_CODE"));
                map.put(CommonConstants.MAP_WHERE, hash.get("ITEM_CODE"));
            }
            
            observable.getData(map);  
            setButtonEnableDisable();
            //For EDIT option enable disable fields and controls appropriately
            if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                ClientUtil.enableDisable(panPaddyItemOpening,true);
            }
            observable.setStatus();
            if(getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE){
                
                btnAuthorize.setVisible(false);
                btnReject.setVisible(false);
                btnException.setVisible(false);
            }
            
            if(viewType==AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                
            }
        }
        if(viewType == AUTHORIZE){
            ClientUtil.enableDisable(panPaddyItemOpening, false);
        }
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW_MODE){
            ClientUtil.enableDisable(this, false);
        }
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            
        }
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        ClientUtil.enableDisable(panPaddyItemOpening, true, false, true);
        txtItemCode.requestFocus();
        observable.resetForm();
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        viewType = ClientConstants.ACTIONTYPE_NEW;
        observable.setStatus();
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(this,false);
        setButtonEnableDisable();
        ClientUtil.clearAll(panPaddyItemOpening);
        ClientUtil.enableDisable(panPaddyItemOpening,true);
        setModified(true);
        HashMap accountHeadMap = new HashMap();
        List accountHeadList = ClientUtil.executeQuery("getSelectAcntHdForPaddy",accountHeadMap); 
        if(accountHeadList != null && accountHeadList.size()>0){ 
            accountHeadMap = (HashMap) accountHeadList.get(0);
            if(accountHeadMap.containsKey("PURCHASE_ACT_HEAD")){
                txtPurchaseAcHd.setEnabled(false);
                txtPurchaseAcHd.setText(CommonUtil.convertObjToStr(accountHeadMap.get("PURCHASE_ACT_HEAD")));
            }
            if(accountHeadMap.containsKey("SALES_ACT_HEAD")){
                txtSalesAcHd.setEnabled(false);
                txtSalesAcHd.setText(CommonUtil.convertObjToStr(accountHeadMap.get("SALES_ACT_HEAD")));
            }
        }else{
            txtPurchaseAcHd.setEnabled(true);
            txtSalesAcHd.setEnabled(true);
        }
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
         cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        viewType = 0;
        ClientUtil.enableDisable(this,false,false,true);
        HashMap map= new HashMap();
        map.put("SCREEN_ID",getScreen());
        map.put("RECORD_KEY", this.txtItemCode.getText());
        map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        map.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        map.put("CUR_DATE", currDt.clone());
        System.out.println("Record Key Map : " + map);
        ClientUtil.execute("deleteEditLock", map);
        observable.resetForm();
        setButtonEnableDisable();
        observable.setStatus();
        ClientUtil.clearAll(this);
        if(getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE){
            cifClosingAlert();
        }
        setModified(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnNew.setEnabled(true);
        btnView.setEnabled(true);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(true);
        btnEdit.setEnabled(true);
        btnClose.setEnabled(true);
        lblStatus.setText("            "); 
    }//GEN-LAST:event_btnCancelActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitNewActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitCancelActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
         updateOBFields();
        String mandatoryMessage = objMandatoryCheck.checkMandatory(getClass().getName(), panPaddyItemOpening);
        if (mandatoryMessage.length()>0) {
            displayAlert(mandatoryMessage);
        } else {
            observable.doAction();
            if (observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                btnCancelActionPerformed(null);
                observable.resetForm();
                btnCancel.setEnabled(true);
                observable.setStatus();
                observable.setResultStatus(); 
                lblStatus.setText(observable.getLblStatus());
            }
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
            observable.ttNotifyObservers();
        }
    }//GEN-LAST:event_btnSaveActionPerformed
  /* To display an alert message if any of the mandatory fields is not inputed */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        popUp("ViewDetails");
        ClientUtil.enableDisable(this, false);
        ClientUtil.enableDisable(panPaddyItemOpening,false);
        lblStatus.setText(observable.getLblStatus());
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setStatus();
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled){
            observable.setResult(observable.getActionType());
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            if(authorizeStatus.equalsIgnoreCase("REJECTED") && flag == true) {
                singleAuthorizeMap.put("DELETESTATUS", "MODIFIED");
                singleAuthorizeMap.put(CommonConstants.STATUS, "REJECTED");
                singleAuthorizeMap.put("DELETEREMARKS", "");
                singleAuthorizeMap.put("STATUSCHECK", "");
            }
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("ITEM_CODE", observable.getTxtItemCode());
            singleAuthorizeMap.put("ITEM_OPENING", "ITEM_OPENING");
            
            System.out.println("!@#$@#$@#$singleAuthorizeMap:"+singleAuthorizeMap);
            observable.authorizeLocationMaster(singleAuthorizeMap);
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(this.txtItemCode.getText());
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
//            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
            viewType = 0;
        } else{
            viewType = AUTHORIZE;
            final HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getPaddyItemOpeningAuthMode");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizePaddyItemOpeningStock");
            isFilled = false;
            setModified(true);
            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            lblStatus.setText(observable.getLblStatus());
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        }
    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
         setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnPurchaseAcHd;
    private com.see.truetransact.uicomponent.CButton btnPurchaseReturnAcHd;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSalesAcHd;
    private com.see.truetransact.uicomponent.CButton btnSalesReturnAcHd;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTaxAcHd;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboUnit;
    private com.see.truetransact.uicomponent.CLabel lblItemCode;
    private com.see.truetransact.uicomponent.CLabel lblItemDesc;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOrderLevel;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseAcHd;
    private com.see.truetransact.uicomponent.CLabel lblPurchasePrice;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseReturnAcHd;
    private com.see.truetransact.uicomponent.CLabel lblQty;
    private com.see.truetransact.uicomponent.CLabel lblSalesAcHd;
    private com.see.truetransact.uicomponent.CLabel lblSalesReturnAcHd;
    private com.see.truetransact.uicomponent.CLabel lblSellingPrice;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace52;
    private com.see.truetransact.uicomponent.CLabel lblSpace53;
    private com.see.truetransact.uicomponent.CLabel lblSpace54;
    private com.see.truetransact.uicomponent.CLabel lblSpace55;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTaxAcHd;
    private com.see.truetransact.uicomponent.CLabel lblUnit;
    private com.see.truetransact.uicomponent.CMenuBar mbrShareProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panPaddyItemOpening;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrShareProduct;
    private com.see.truetransact.uicomponent.CTextField txtItemCode;
    private com.see.truetransact.uicomponent.CTextField txtItemDesc;
    private com.see.truetransact.uicomponent.CTextField txtOrderLevel;
    private com.see.truetransact.uicomponent.CTextField txtPurchaseAcHd;
    private com.see.truetransact.uicomponent.CTextField txtPurchasePrice;
    private com.see.truetransact.uicomponent.CTextField txtPurchaseReturnAcHd;
    private com.see.truetransact.uicomponent.CTextField txtQty;
    private com.see.truetransact.uicomponent.CTextField txtSalesAcHd;
    private com.see.truetransact.uicomponent.CTextField txtSalesReturnAcHd;
    private com.see.truetransact.uicomponent.CTextField txtSellingPrice;
    private com.see.truetransact.uicomponent.CTextField txtTaxAcHd;
    // End of variables declaration//GEN-END:variables
    
}
