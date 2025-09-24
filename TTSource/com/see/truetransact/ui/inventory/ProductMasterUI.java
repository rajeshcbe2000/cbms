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

package com.see.truetransact.ui.inventory;

/**
 *
 * @author Rajesh
 *  @modified : 
 *      Added Edit Locking - 08-07-2005
 */

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.uimandatory.MandatoryCheck;

import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;

public class ProductMasterUI extends CInternalFrame implements UIMandatoryField, Observer {
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.inventory.ProductMasterRB", ProxyParameters.LANGUAGE);
    HashMap mandatoryMap = new HashMap();
//    ProductMasterOB observable;
    ProductMasterMRB objMandatoryRB;
    MandatoryCheck objMandatoryCheck = new MandatoryCheck();
    
    /** Creates new form ShareProductUI */
    public ProductMasterUI() {
        initComponents();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
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
        lblProductCode.setName("lblProductCode");
        lblProductDesc.setName("lblProductDesc");
        lblPurchaseAcHd.setName("lblPurchaseAcHd");
        lblPurchasePrice.setName("lblPurchasePrice");
        lblPurchaseReturnAcHd.setName("lblPurchaseReturnAcHd");
        lblQty.setName("lblQty");
        lblReorderLevel.setName("lblReorderLevel");
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
        panProductMaster.setName("panProductMaster");
        panStatus.setName("panStatus");
        txtProductCode.setName("txtProductCode");
        txtProductDesc.setName("txtProductDesc");
        txtPurchaseAcHd.setName("txtPurchaseAcHd");
        txtPurchasePrice.setName("txtPurchasePrice");
        txtPurchaseReturnAcHd.setName("txtPurchaseReturnAcHd");
        txtQty.setName("txtQty");
        txtReorderLevel.setName("txtReorderLevel");
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
        lblProductDesc.setText(resourceBundle.getString("lblProductDesc"));
        lblQty.setText(resourceBundle.getString("lblQty"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblProductCode.setText(resourceBundle.getString("lblProductCode"));
        lblPurchaseAcHd.setText(resourceBundle.getString("lblPurchaseAcHd"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        btnTaxAcHd.setText(resourceBundle.getString("btnTaxAcHd"));
        btnPurchaseAcHd.setText(resourceBundle.getString("btnPurchaseAcHd"));
        lblReorderLevel.setText(resourceBundle.getString("lblReorderLevel"));
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
//        txtProductDesc.setText(observable.getTxtProductDesc());
//        txtProductCode.setText(observable.getTxtProductCode());
//        txtPurchasePrice.setText(observable.getTxtPurchasePrice());
//        txtSellingPrice.setText(observable.getTxtSellingPrice());
//        cboUnit.setSelectedItem(observable.getCboUnit());
//        txtQty.setText(observable.getTxtQty());
//        txtReorderLevel.setText(observable.getTxtReorderLevel());
//        txtPurchaseAcHd.setText(observable.getTxtPurchaseAcHd());
//        txtSalesAcHd.setText(observable.getTxtSalesAcHd());
//        txtTaxAcHd.setText(observable.getTxtTaxAcHd());
//        txtPurchaseReturnAcHd.setText(observable.getTxtPurchaseReturnAcHd());
//        txtSalesReturnAcHd.setText(observable.getTxtSalesReturnAcHd());
    }
//    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
//        observable.setTxtProductDesc(txtProductDesc.getText());
//        observable.setTxtProductCode(txtProductCode.getText());
//        observable.setTxtPurchasePrice(txtPurchasePrice.getText());
//        observable.setTxtSellingPrice(txtSellingPrice.getText());
//        observable.setCboUnit((String) cboUnit.getSelectedItem());
//        observable.setTxtQty(txtQty.getText());
//        observable.setTxtReorderLevel(txtReorderLevel.getText());
//        observable.setTxtPurchaseAcHd(txtPurchaseAcHd.getText());
//        observable.setTxtSalesAcHd(txtSalesAcHd.getText());
//        observable.setTxtTaxAcHd(txtTaxAcHd.getText());
//        observable.setTxtPurchaseReturnAcHd(txtPurchaseReturnAcHd.getText());
//        observable.setTxtSalesReturnAcHd(txtSalesReturnAcHd.getText());
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
        objMandatoryRB = new ProductMasterMRB();
        txtProductDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductDesc"));
        txtProductCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductCode"));
        txtPurchasePrice.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPurchasePrice"));
        txtSellingPrice.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSellingPrice"));
        cboUnit.setHelpMessage(lblMsg, objMandatoryRB.getString("cboUnit"));
        txtQty.setHelpMessage(lblMsg, objMandatoryRB.getString("txtQty"));
        txtReorderLevel.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReorderLevel"));
        txtPurchaseAcHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPurchaseAcHd"));
        txtSalesAcHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSalesAcHd"));
        txtTaxAcHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTaxAcHd"));
        txtPurchaseReturnAcHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPurchaseReturnAcHd"));
        txtSalesReturnAcHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSalesReturnAcHd"));
    }
    
    
    
    
    public static void main(String args[]){
        javax.swing.JFrame frame = new javax.swing.JFrame();
        SupplierMasterUI ui = new SupplierMasterUI();
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
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        tbrShareProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panProductMaster = new com.see.truetransact.uicomponent.CPanel();
        lblProductDesc = new com.see.truetransact.uicomponent.CLabel();
        txtProductDesc = new com.see.truetransact.uicomponent.CTextField();
        lblProductCode = new com.see.truetransact.uicomponent.CLabel();
        txtProductCode = new com.see.truetransact.uicomponent.CTextField();
        lblPurchasePrice = new com.see.truetransact.uicomponent.CLabel();
        txtPurchasePrice = new com.see.truetransact.uicomponent.CTextField();
        lblSellingPrice = new com.see.truetransact.uicomponent.CLabel();
        txtSellingPrice = new com.see.truetransact.uicomponent.CTextField();
        lblUnit = new com.see.truetransact.uicomponent.CLabel();
        cboUnit = new com.see.truetransact.uicomponent.CComboBox();
        lblQty = new com.see.truetransact.uicomponent.CLabel();
        txtQty = new com.see.truetransact.uicomponent.CTextField();
        lblReorderLevel = new com.see.truetransact.uicomponent.CLabel();
        txtReorderLevel = new com.see.truetransact.uicomponent.CTextField();
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
        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif")));
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

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        tbrShareProduct.add(btnNew);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif")));
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        tbrShareProduct.add(btnEdit);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        tbrShareProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrShareProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        tbrShareProduct.add(btnSave);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif")));
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        tbrShareProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrShareProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif")));
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });

        tbrShareProduct.add(btnAuthorize);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif")));
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });

        tbrShareProduct.add(btnReject);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif")));
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });

        tbrShareProduct.add(btnException);

        lblSpace4.setText("     ");
        tbrShareProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif")));
        btnPrint.setToolTipText("Print");
        tbrShareProduct.add(btnPrint);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif")));
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

        lblStatus.setBorder(new javax.swing.border.EtchedBorder());
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

        panProductMaster.setLayout(new java.awt.GridBagLayout());

        panProductMaster.setMaximumSize(new java.awt.Dimension(800, 350));
        panProductMaster.setMinimumSize(new java.awt.Dimension(800, 350));
        panProductMaster.setPreferredSize(new java.awt.Dimension(800, 350));
        lblProductDesc.setText("Product Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductMaster.add(lblProductDesc, gridBagConstraints);

        txtProductDesc.setMaximumSize(new java.awt.Dimension(300, 21));
        txtProductDesc.setMinimumSize(new java.awt.Dimension(300, 21));
        txtProductDesc.setPreferredSize(new java.awt.Dimension(300, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductMaster.add(txtProductDesc, gridBagConstraints);

        lblProductCode.setText("Product Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductMaster.add(lblProductCode, gridBagConstraints);

        txtProductCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductMaster.add(txtProductCode, gridBagConstraints);

        lblPurchasePrice.setText("Purchase Price/Unit");
        lblPurchasePrice.setName("lblStreet");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductMaster.add(lblPurchasePrice, gridBagConstraints);

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
        panProductMaster.add(txtPurchasePrice, gridBagConstraints);

        lblSellingPrice.setText("Selling Price/Unit");
        lblSellingPrice.setName("lblStreet");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductMaster.add(lblSellingPrice, gridBagConstraints);

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
        panProductMaster.add(txtSellingPrice, gridBagConstraints);

        lblUnit.setText("Units in");
        lblUnit.setName("lblCity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductMaster.add(lblUnit, gridBagConstraints);

        cboUnit.setMinimumSize(new java.awt.Dimension(100, 21));
        cboUnit.setName("cboCity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductMaster.add(cboUnit, gridBagConstraints);

        lblQty.setText("Quantity in store");
        lblQty.setName("lblPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductMaster.add(lblQty, gridBagConstraints);

        txtQty.setMaxLength(16);
        txtQty.setMinimumSize(new java.awt.Dimension(100, 21));
        txtQty.setName("txtPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductMaster.add(txtQty, gridBagConstraints);

        lblReorderLevel.setText("Reorder level");
        lblReorderLevel.setName("lblPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductMaster.add(lblReorderLevel, gridBagConstraints);

        txtReorderLevel.setMaxLength(16);
        txtReorderLevel.setMinimumSize(new java.awt.Dimension(100, 21));
        txtReorderLevel.setName("txtPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductMaster.add(txtReorderLevel, gridBagConstraints);

        lblPurchaseAcHd.setText("Purchase Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductMaster.add(lblPurchaseAcHd, gridBagConstraints);

        txtPurchaseAcHd.setEditable(false);
        txtPurchaseAcHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurchaseAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductMaster.add(txtPurchaseAcHd, gridBagConstraints);

        btnPurchaseAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnPurchaseAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnPurchaseAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPurchaseAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPurchaseAcHd.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductMaster.add(btnPurchaseAcHd, gridBagConstraints);

        lblSalesAcHd.setText("Sales Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductMaster.add(lblSalesAcHd, gridBagConstraints);

        txtSalesAcHd.setEditable(false);
        txtSalesAcHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSalesAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductMaster.add(txtSalesAcHd, gridBagConstraints);

        btnSalesAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnSalesAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSalesAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSalesAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSalesAcHd.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductMaster.add(btnSalesAcHd, gridBagConstraints);

        lblTaxAcHd.setText("Tax Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductMaster.add(lblTaxAcHd, gridBagConstraints);

        txtTaxAcHd.setEditable(false);
        txtTaxAcHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTaxAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductMaster.add(txtTaxAcHd, gridBagConstraints);

        btnTaxAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnTaxAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnTaxAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnTaxAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnTaxAcHd.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductMaster.add(btnTaxAcHd, gridBagConstraints);

        txtPurchaseReturnAcHd.setEditable(false);
        txtPurchaseReturnAcHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurchaseReturnAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductMaster.add(txtPurchaseReturnAcHd, gridBagConstraints);

        btnPurchaseReturnAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnPurchaseReturnAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnPurchaseReturnAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPurchaseReturnAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPurchaseReturnAcHd.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductMaster.add(btnPurchaseReturnAcHd, gridBagConstraints);

        lblPurchaseReturnAcHd.setText("Purchase Return Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductMaster.add(lblPurchaseReturnAcHd, gridBagConstraints);

        lblSalesReturnAcHd.setText("Sales Return Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductMaster.add(lblSalesReturnAcHd, gridBagConstraints);

        txtSalesReturnAcHd.setEditable(false);
        txtSalesReturnAcHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSalesReturnAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductMaster.add(txtSalesReturnAcHd, gridBagConstraints);

        btnSalesReturnAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnSalesReturnAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSalesReturnAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSalesReturnAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSalesReturnAcHd.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductMaster.add(btnSalesReturnAcHd, gridBagConstraints);

        getContentPane().add(panProductMaster, java.awt.BorderLayout.CENTER);

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
    }//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
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
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
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
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProductCode;
    private com.see.truetransact.uicomponent.CLabel lblProductDesc;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseAcHd;
    private com.see.truetransact.uicomponent.CLabel lblPurchasePrice;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseReturnAcHd;
    private com.see.truetransact.uicomponent.CLabel lblQty;
    private com.see.truetransact.uicomponent.CLabel lblReorderLevel;
    private com.see.truetransact.uicomponent.CLabel lblSalesAcHd;
    private com.see.truetransact.uicomponent.CLabel lblSalesReturnAcHd;
    private com.see.truetransact.uicomponent.CLabel lblSellingPrice;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
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
    private com.see.truetransact.uicomponent.CPanel panProductMaster;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrShareProduct;
    private com.see.truetransact.uicomponent.CTextField txtProductCode;
    private com.see.truetransact.uicomponent.CTextField txtProductDesc;
    private com.see.truetransact.uicomponent.CTextField txtPurchaseAcHd;
    private com.see.truetransact.uicomponent.CTextField txtPurchasePrice;
    private com.see.truetransact.uicomponent.CTextField txtPurchaseReturnAcHd;
    private com.see.truetransact.uicomponent.CTextField txtQty;
    private com.see.truetransact.uicomponent.CTextField txtReorderLevel;
    private com.see.truetransact.uicomponent.CTextField txtSalesAcHd;
    private com.see.truetransact.uicomponent.CTextField txtSalesReturnAcHd;
    private com.see.truetransact.uicomponent.CTextField txtSellingPrice;
    private com.see.truetransact.uicomponent.CTextField txtTaxAcHd;
    // End of variables declaration//GEN-END:variables
    
}
