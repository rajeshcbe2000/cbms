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

package com.see.truetransact.ui.suspenseaccount;

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
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class SuspenseAccountProductUI extends CInternalFrame implements UIMandatoryField, Observer{
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.suspenseaccount.SuspenseAccountProductRB", ProxyParameters.LANGUAGE);
    HashMap mandatoryMap = new HashMap();
    SuspenseAccountProductOB observable;
    SuspenseAccountProductMRB objMandatoryRB;
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
    
    /** Creates new form ShareProductUI */
    public SuspenseAccountProductUI() {
        initComponents();
        setFieldNames();
        internationalize();
        setHelpMessage();
        setObservable();
        initComponentData();
        setMandatoryHashMap();
        setMaxLengths();
        ClientUtil.enableDisable(panSuspenseAccountProduct, false, false, true);
        setButtonEnableDisable();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panSuspenseAccountProduct);
        btnView.setEnabled(true);
        visibleFalse();
        lblIntAcHd.setVisible(false);
        txtIntAcHd.setVisible(false);
        btnIntAcHdSearch.setVisible(false);
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
        txtSuspenseProdName.setAllowAll(true);
        txtSuspenseProdName.setMaxLength(64);
        txtSuspenseProdID.setAllowAll(true);
        //txtSuspenseProdID.setMaxLength(64);
        txtSuspenseProdID.setMaxLength(3);
        txtSuspenseProductHead.setMaxLength(64);
        txtSuspenseProductHead.setAllowAll(true);
    }
    
    private void initComponentData() {
        
    }
    private void setObservable() {
        try{
            observable = SuspenseAccountProductOB.getInstance();  
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
//        btnPurchaseAcHd.setName("btnPurchaseAcHd");
//        btnPurchaseReturnAcHd.setName("btnPurchaseReturnAcHd");
        btnReject.setName("btnReject");
//        btnSalesAcHd.setName("btnSalesAcHd");
//        btnSalesReturnAcHd.setName("btnSalesReturnAcHd");
        btnSave.setName("btnSave");
//        btnTaxAcHd.setName("btnTaxAcHd");
        btnView.setName("btnView");
//        cboUnit.setName("cboUnit");
        lblMsg.setName("lblMsg");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        mbrShareProduct.setName("mbrShareProduct");
        panSuspenseAccountProduct.setName("panSuspenseAccountProduct");
        panStatus.setName("panStatus");
        txtSuspenseProductHead.setName("txtSuspenseProductHead");
        txtSuspenseProdID.setName("txtSuspenseProdID");
        txtSuspenseProdName.setName("txtSuspenseProdName");
        lblSuspenseProductHead.setName("lblSuspenseProductHead");
        lblSuspenseProdID.setName("lblSuspenseProdID");
        lblSuspenseProdName.setName("lblSuspenseProdName");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnView.setText(resourceBundle.getString("btnView"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtSuspenseProdID.setText(observable.getTxtSuspenseProdID());
        txtSuspenseProdName.setText(observable.getTxtSuspenseProdName());
        txtSuspenseProductHead.setText(observable.getTxtSuspenseProductHead());
        txtPrefix.setText(observable.getTxtPrefix());
        if(CommonUtil.convertObjToStr(observable.getChkNegBalnce()).equals("Y")){
          chkNegBalnce.setSelected(true);
        }else{
          chkNegBalnce.setSelected(false);  
        }
        
        if(CommonUtil.convertObjToStr(observable.getChkLoanBehaviour()).equals("Y")){
          chkLoanBehaviour.setSelected(true);
        }else{
          chkLoanBehaviour.setSelected(false);  
        }        
        txtIntAcHd.setText(observable.getTxtIntAcHd());
        txtIntRate.setText(observable.getTxtIntRate());        
    }
    //
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtSuspenseProdID(txtSuspenseProdID.getText());
        observable.setTxtSuspenseProdName(txtSuspenseProdName.getText());
        observable.setTxtSuspenseProductHead(txtSuspenseProductHead.getText());
        observable.setTxtPrefix(txtPrefix.getText());
        if(chkNegBalnce.isSelected()){
             observable.setChkNegBalnce("Y");
        }else{
             observable.setChkNegBalnce("N");
        }
        
         if(chkLoanBehaviour.isSelected()){
             observable.setChkLoanBehaviour("Y");
        }else{
             observable.setChkLoanBehaviour("N");
        }
        
        observable.setTxtIntAcHd(txtIntAcHd.getText());
        observable.setTxtIntRate(txtIntRate.getText());
         
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
        objMandatoryRB = new SuspenseAccountProductMRB();
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
        SuspenseAccountProductUI ui = new SuspenseAccountProductUI();
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
        panSuspenseAccountProduct = new com.see.truetransact.uicomponent.CPanel();
        lblSuspenseProdID = new com.see.truetransact.uicomponent.CLabel();
        txtSuspenseProdID = new com.see.truetransact.uicomponent.CTextField();
        lblSuspenseProdName = new com.see.truetransact.uicomponent.CLabel();
        lblSuspenseProductHead = new com.see.truetransact.uicomponent.CLabel();
        txtSuspenseProductHead = new com.see.truetransact.uicomponent.CTextField();
        btnSuspenseProductHead = new com.see.truetransact.uicomponent.CButton();
        txtSuspenseProdName = new com.see.truetransact.uicomponent.CTextField();
        lblPrefix = new com.see.truetransact.uicomponent.CLabel();
        txtPrefix = new com.see.truetransact.uicomponent.CTextField();
        lblChkNegVal = new com.see.truetransact.uicomponent.CLabel();
        chkNegBalnce = new com.see.truetransact.uicomponent.CCheckBox();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        chkLoanBehaviour = new com.see.truetransact.uicomponent.CCheckBox();
        lblIntAcHd = new com.see.truetransact.uicomponent.CLabel();
        txtIntAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnIntAcHdSearch = new com.see.truetransact.uicomponent.CButton();
        lblIntRate = new com.see.truetransact.uicomponent.CLabel();
        txtIntRate = new com.see.truetransact.uicomponent.CTextField();
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

        panSuspenseAccountProduct.setMaximumSize(new java.awt.Dimension(800, 350));
        panSuspenseAccountProduct.setMinimumSize(new java.awt.Dimension(800, 350));
        panSuspenseAccountProduct.setPreferredSize(new java.awt.Dimension(800, 350));
        panSuspenseAccountProduct.setLayout(new java.awt.GridBagLayout());

        lblSuspenseProdID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 188, 0, 0);
        panSuspenseAccountProduct.add(lblSuspenseProdID, gridBagConstraints);

        txtSuspenseProdID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSuspenseProdID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSuspenseProdIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 6, 0, 0);
        panSuspenseAccountProduct.add(txtSuspenseProdID, gridBagConstraints);

        lblSuspenseProdName.setText("Product Name");
        lblSuspenseProdName.setName("lblStreet"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 166, 0, 0);
        panSuspenseAccountProduct.add(lblSuspenseProdName, gridBagConstraints);

        lblSuspenseProductHead.setText("Suspense Product Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(96, 108, 0, 0);
        panSuspenseAccountProduct.add(lblSuspenseProductHead, gridBagConstraints);

        txtSuspenseProductHead.setEditable(false);
        txtSuspenseProductHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSuspenseProductHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(95, 6, 0, 0);
        panSuspenseAccountProduct.add(txtSuspenseProductHead, gridBagConstraints);

        btnSuspenseProductHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSuspenseProductHead.setEnabled(false);
        btnSuspenseProductHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSuspenseProductHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSuspenseProductHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSuspenseProductHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuspenseProductHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(95, 4, 0, 0);
        panSuspenseAccountProduct.add(btnSuspenseProductHead, gridBagConstraints);

        txtSuspenseProdName.setMinimumSize(new java.awt.Dimension(175, 21));
        txtSuspenseProdName.setPreferredSize(new java.awt.Dimension(175, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 6, 0, 0);
        panSuspenseAccountProduct.add(txtSuspenseProdName, gridBagConstraints);

        lblPrefix.setText("Prefix");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 216, 0, 0);
        panSuspenseAccountProduct.add(lblPrefix, gridBagConstraints);

        txtPrefix.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 6, 0, 0);
        panSuspenseAccountProduct.add(txtPrefix, gridBagConstraints);

        lblChkNegVal.setText("Negative Balance Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 101, 0, 0);
        panSuspenseAccountProduct.add(lblChkNegVal, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 0);
        panSuspenseAccountProduct.add(chkNegBalnce, gridBagConstraints);

        cLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        cLabel1.setText("Loan Behaviour");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 163, 0, 0);
        panSuspenseAccountProduct.add(cLabel1, gridBagConstraints);

        chkLoanBehaviour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLoanBehaviourActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        panSuspenseAccountProduct.add(chkLoanBehaviour, gridBagConstraints);

        lblIntAcHd.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblIntAcHd.setText("Interest AcHd");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.ipadx = 56;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 112, 0, 0);
        panSuspenseAccountProduct.add(lblIntAcHd, gridBagConstraints);

        txtIntAcHd.setAllowAll(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 169;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        panSuspenseAccountProduct.add(txtIntAcHd, gridBagConstraints);

        btnIntAcHdSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnIntAcHdSearch.setMaximumSize(new java.awt.Dimension(21, 21));
        btnIntAcHdSearch.setMinimumSize(new java.awt.Dimension(21, 21));
        btnIntAcHdSearch.setPreferredSize(new java.awt.Dimension(21, 21));
        btnIntAcHdSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntAcHdSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 82);
        panSuspenseAccountProduct.add(btnIntAcHdSearch, gridBagConstraints);

        lblIntRate.setText("Int Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 202, 0, 0);
        panSuspenseAccountProduct.add(lblIntRate, gridBagConstraints);

        txtIntRate.setAllowAll(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 63, 0);
        panSuspenseAccountProduct.add(txtIntRate, gridBagConstraints);

        getContentPane().add(panSuspenseAccountProduct, java.awt.BorderLayout.CENTER);

        mbrShareProduct.setName("mbrCustomer"); // NOI18N

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess"); // NOI18N

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

    private void txtSuspenseProdIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSuspenseProdIDFocusLost
        // TODO add your handling code here:
//        if(txtItemCode.getText().length()>0){
//            txtItemCode.setText(CommonUtil.convertObjToStr(txtItemCode.getText()).toUpperCase());
//            String itemCode = CommonUtil.convertObjToStr(txtItemCode.getText());
//            HashMap locCodeChkMap = new HashMap();
//            locCodeChkMap.put("ITEM_CODE",itemCode);
//            List list = ClientUtil.executeQuery("getItemCodeChk", locCodeChkMap);
//            if(list!= null && list.size() > 0){
//                ClientUtil.showAlertWindow("Item Code Already exists!!!");
//                txtItemCode.setText("");
//            }
//        }
        

      if (observable.getActionType() != ClientConstants.ACTIONTYPE_EDIT) {
            HashMap whereMap = new HashMap();
            whereMap.put("PROD_ID", txtSuspenseProdID.getText());
            List lst = ClientUtil.executeQuery("getAllProductIds", whereMap);
            if (lst != null && lst.size() > 0) {
                HashMap existingProdIdMap = (HashMap) lst.get(0);
                if (existingProdIdMap.containsKey("PROD_ID")) {
                    ClientUtil.showMessageWindow("Id is already exists for Product " + existingProdIdMap.get("PRODUCT") + "\n Please change Product ID first");
                    txtSuspenseProdID.setText("");
                }
            }
        }


        
    }//GEN-LAST:event_txtSuspenseProdIDFocusLost

    private void btnSuspenseProductHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuspenseProductHeadActionPerformed
        // TODO add your handling code here:
        popUp("SUSPENSE_PROD_AC_HEAD");
    }//GEN-LAST:event_btnSuspenseProductHeadActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        viewType = ClientConstants.ACTIONTYPE_DELETE;
        observable.setStatus(); 
        popUp(ClientConstants.ACTION_STATUS[3]);
        ClientUtil.enableDisable(this, false);
        ClientUtil.enableDisable(panSuspenseAccountProduct,false);
        lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void visibleFalse(){
//        btnSalesAcHd.setVisible(false);
//        btnPurchaseAcHd.setVisible(false);
        
//        lblPurchaseReturnAcHd.setVisible(false);
//        txtPurchaseReturnAcHd.setVisible(false);
//        lblSalesReturnAcHd.setVisible(false);
//        txtSalesReturnAcHd.setVisible(false);
//        lblTaxAcHd.setVisible(false);
//        txtTaxAcHd.setVisible(false);
//        btnPurchaseReturnAcHd.setVisible(false);
//        btnSalesReturnAcHd.setVisible(false);
//        btnTaxAcHd.setVisible(false);
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
        ClientUtil.enableDisable(panSuspenseAccountProduct,true);
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
                    lst.add("PROD_ID");
                    viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                    viewMap.put(CommonConstants.MAP_NAME, "getSelectSuspenseProductTOList");
                    viewMap.put(CommonConstants.MAP_WHERE, wheres);
                } else if (actionType.equals(ClientConstants. ACTION_STATUS[3])){
                    viewMap.put(CommonConstants.MAP_NAME, "getSelectSuspenseProductTOList");
                    viewMap.put(CommonConstants.MAP_WHERE, wheres);
                }
                else if(actionType.equals("ViewDetails")){
                    HashMap where = new HashMap();
                    where.put("BRANCH_ID", getSelectedBranchID());
                    viewMap.put(CommonConstants.MAP_NAME, "getSelectSuspenseProductTOList");
                    viewMap.put(CommonConstants.MAP_WHERE, where);
                    where = null;
                } else if(actionType.equals("SUSPENSE_PROD_AC_HEAD")){
                    viewType = LIABILITYGL;
                    HashMap where = new HashMap();
//                    where.put(ACCT_TYPE, AcctStatusConstants.LIABILITY);
//                    where.put(BALANCETYPE, CommonConstants.CREDIT);
                    where.put("BRANCH_ID", getSelectedBranchID());
                    viewMap.put(CommonConstants.MAP_NAME, "product.deposits.getAcctHeadList");
                    viewMap.put(CommonConstants.MAP_WHERE, where);
                    where = null;
                } else if(actionType.equals("INT_AC_HEAD")){
                    viewType = LIABILITYGL;
                    HashMap where = new HashMap();
                    where.put("BRANCH_ID", getSelectedBranchID());
                    viewMap.put(CommonConstants.MAP_NAME, "product.deposits.getAcctHeadList");
                    viewMap.put(CommonConstants.MAP_WHERE, where);
                    where = null;
                }
                else if(actionType.equals("MEMBER_NO")){
                    viewMap.put(CommonConstants.MAP_NAME, "getMemeberShipDetails");
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
            else if(hash.get("ACCOUNT HEAD")!=null && actionType.equals("SUSPENSE_PROD_AC_HEAD")){
                txtSuspenseProductHead.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT HEAD")));
                txtSuspenseProductHead.setEnabled(false);
            }else if(hash.get("ACCOUNT HEAD")!=null && actionType.equals("INT_AC_HEAD")){
                txtIntAcHd.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT HEAD")));
                txtIntAcHd.setEnabled(false);
            }
            else if(actionType.equals(ClientConstants.ACTION_STATUS[2]) ||
            actionType.equals(ClientConstants.ACTION_STATUS[3])|| actionType.equals("DeletedDetails") || actionType.equals("ViewDetails")|| viewType == AUTHORIZE ||
            getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
            ){
                HashMap map = new HashMap();
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
                observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                    map.put("PROD_ID",hash.get("PROD_ID"));
                    map.put(CommonConstants.MAP_WHERE, hash.get("PROD_ID"));
                }else{
                    map.put("PROD_ID",hash.get("PROD_ID"));
                    map.put(CommonConstants.MAP_WHERE, hash.get("PROD_ID"));
                }
                
                observable.getData(map);
                setButtonEnableDisable();
                //For EDIT option enable disable fields and controls appropriately
                if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                    ClientUtil.enableDisable(panSuspenseAccountProduct,true);
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
                ClientUtil.enableDisable(panSuspenseAccountProduct, false);
            }
            if( observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW_MODE){
                ClientUtil.enableDisable(this, false);
            }
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                
            }
        }
        
        
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        ClientUtil.enableDisable(panSuspenseAccountProduct, true, false, true);
        txtSuspenseProductHead.requestFocus();
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
        ClientUtil.clearAll(panSuspenseAccountProduct);
        ClientUtil.enableDisable(panSuspenseAccountProduct,true);
        setModified(true);
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
        map.put("SCREEN_ID",getScreenID());
        map.put("RECORD_KEY", this.txtSuspenseProdID.getText());
        map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        map.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        map.put("CUR_DATE", ClientUtil.getCurrentDate());
        System.out.println("Record Key Map : " + map);
        ClientUtil.execute("deleteEditLock", map);
        observable.resetForm();
        setButtonEnableDisable();
        observable.setStatus();
        ClientUtil.clearAll(this);
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
        String mandatoryMessage = objMandatoryCheck.checkMandatory(getClass().getName(), panSuspenseAccountProduct);
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
        ClientUtil.enableDisable(panSuspenseAccountProduct,false);
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
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
            singleAuthorizeMap.put("PROD_ID", observable.getTxtSuspenseProdID());
            singleAuthorizeMap.put("SUSPENSE_PRODUCT", "SUSPENSE_PRODUCT");
//            
            System.out.println("!@#$@#$@#$singleAuthorizeMap:"+singleAuthorizeMap);
            observable.authorizeSuspenseMaster(singleAuthorizeMap); 
            super.setOpenForEditBy(observable.getStatusBy());
//            super.removeEditLock(this.txtItemCode.getText());
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
            mapParam.put(CommonConstants.MAP_NAME, "getSuspenseAccountProductAuthMode");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeSuspenseAccountProduct");
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
    
    private void chkLoanBehaviourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLoanBehaviourActionPerformed
        // TODO add your handling code here:
        if(chkLoanBehaviour.isSelected()){
            lblIntAcHd.setVisible(true);
            txtIntAcHd.setVisible(true);
            btnIntAcHdSearch.setVisible(true);
        }else{
            lblIntAcHd.setVisible(false);
            txtIntAcHd.setVisible(false);
            btnIntAcHdSearch.setVisible(false);
        }
    }//GEN-LAST:event_chkLoanBehaviourActionPerformed

    private void btnIntAcHdSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIntAcHdSearchActionPerformed
        // TODO add your handling code here:
         popUp("INT_AC_HEAD");
    }//GEN-LAST:event_btnIntAcHdSearchActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnIntAcHdSearch;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSuspenseProductHead;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CCheckBox chkLoanBehaviour;
    private com.see.truetransact.uicomponent.CCheckBox chkNegBalnce;
    private com.see.truetransact.uicomponent.CLabel lblChkNegVal;
    private com.see.truetransact.uicomponent.CLabel lblIntAcHd;
    private com.see.truetransact.uicomponent.CLabel lblIntRate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPrefix;
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
    private com.see.truetransact.uicomponent.CLabel lblSuspenseProdID;
    private com.see.truetransact.uicomponent.CLabel lblSuspenseProdName;
    private com.see.truetransact.uicomponent.CLabel lblSuspenseProductHead;
    private com.see.truetransact.uicomponent.CMenuBar mbrShareProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSuspenseAccountProduct;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrShareProduct;
    private com.see.truetransact.uicomponent.CTextField txtIntAcHd;
    private com.see.truetransact.uicomponent.CTextField txtIntRate;
    private com.see.truetransact.uicomponent.CTextField txtPrefix;
    private com.see.truetransact.uicomponent.CTextField txtSuspenseProdID;
    private com.see.truetransact.uicomponent.CTextField txtSuspenseProdName;
    private com.see.truetransact.uicomponent.CTextField txtSuspenseProductHead;
    // End of variables declaration//GEN-END:variables
    
}
