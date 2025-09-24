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
 * @author Rajesh
 *  @modified : 
 *      Added Edit Locking - 08-07-2005
 */

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.uimandatory.MandatoryCheck;
import java.util.Date;
import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
public class PaddyLocalityMasterUI extends CInternalFrame implements UIMandatoryField, Observer{
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.paddyprocurement.PaddyLocalityMasterRB", ProxyParameters.LANGUAGE);
    HashMap mandatoryMap = new HashMap();
    PaddyLocalityMasterOB observable;
    PaddyLocalityMasterMRB objMandatoryRB;
    MandatoryCheck objMandatoryCheck = new MandatoryCheck();
    private final String CLASSNAME = this.getClass().getName();
    boolean flag = false;
    final int AUTHORIZE=3;
    final int DELETE = 1;
    private String actionType = "";
    private int viewType=-1;
    boolean isFilled = false;
    private Date currDt = null;
    /** Creates new form ShareProductUI */
    public PaddyLocalityMasterUI() {
        Date currDt = ClientUtil.getCurrentDate();
        initComponents();
        setFieldNames();
        internationalize();
        setHelpMessage();
        setObservable();
        initComponentData();
        setMandatoryHashMap();
        setMaxLengths();
        ClientUtil.enableDisable(panLocalityrMaster, false, false, true);
        setButtonEnableDisable();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panLocalityrMaster);
        btnView.setEnabled(true);
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
        cboCity.setName("cboCity");
        cboCountry.setName("cboCountry");
        cboState.setName("cboState");
        
        lblArea.setName("lblArea");
        
        lblCity.setName("lblCity");
        lblCountry.setName("lblCountry");
        lblMsg.setName("lblMsg");
        lblPincode.setName("lblPincode");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblState.setName("lblState");
        lblStatus.setName("lblStatus");
        lblStreet.setName("lblStreet");
        lblLocalityCode.setName("lblSupplierID");
        lblLocalityName.setName("lblLocalityName");
        mbrShareProduct.setName("mbrShareProduct");
        panStatus.setName("panStatus");
        panLocalityrMaster.setName("panLocalityrMaster");
        txtArea.setName("txtArea");
        txtPincode.setName("txtPincode");
        txtStreet.setName("txtStreet");
        txtLocalityCode.setName("txtLocalityCode");
        txtLocalityName.setName("txtLocalityName");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblStreet.setText(resourceBundle.getString("lblStreet"));
        lblCity.setText(resourceBundle.getString("lblCity"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblPincode.setText(resourceBundle.getString("lblPincode"));
        lblLocalityName.setText(resourceBundle.getString("lblLocalityName"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnView.setText(resourceBundle.getString("btnView"));
        lblArea.setText(resourceBundle.getString("lblArea"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblCountry.setText(resourceBundle.getString("lblCountry"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblLocalityCode.setText(resourceBundle.getString("lblLocalityCode"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblState.setText(resourceBundle.getString("lblState"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }
    
    private void setMaxLengths() {
        txtLocalityCode.setMaxLength(12);
        txtLocalityCode.setAllowAll(true);
        txtLocalityName.setMaxLength(64);
        txtLocalityName.setAllowAll(true);
        txtStreet.setMaxLength(128);
        txtStreet.setAllowAll(true);
        txtArea.setMaxLength(128);
        txtArea.setAllowAll(true);
        txtPincode.setMaxLength(12);
        txtPincode.setValidation(new PincodeValidation_IN());
    }
    
    
    private void setObservable() {
        try{
            observable = PaddyLocalityMasterOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            System.out.println("Exception is caught "+e);
        }
    }
    
    private void initComponentData() {
        cboCity.setModel(observable.getCbmCity());
        cboState.setModel(observable.getCbmState());
        cboCountry.setModel(observable.getCbmCountry());
    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtLocalityName.setText(observable.getTxtLocalityName());
        txtLocalityCode.setText(observable.getTxtLocalityCode());
        txtStreet.setText(observable.getTxtStreet());
        txtArea.setText(observable.getTxtArea());
        cboCity.setSelectedItem(observable.getCboCity());
        cboState.setSelectedItem(observable.getCboState());
        txtPincode.setText(observable.getTxtPincode());
        cboCountry.setSelectedItem(observable.getCboCountry());
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setTxtLocalityName(txtLocalityName.getText());
        observable.setTxtLocalityCode(txtLocalityCode.getText());
        observable.setTxtStreet(txtStreet.getText());
        observable.setTxtArea(txtArea.getText());
        observable.setCboCity((String) cboCity.getSelectedItem());
        observable.setCboState((String) cboState.getSelectedItem());
        observable.setTxtPincode(txtPincode.getText());
        observable.setCboCountry((String) cboCountry.getSelectedItem());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtLocalityName", new Boolean(true));
        mandatoryMap.put("txtLocalityCode", new Boolean(true));
        mandatoryMap.put("txtStreet", new Boolean(false));
        mandatoryMap.put("txtArea", new Boolean(false));
        mandatoryMap.put("cboCity", new Boolean(false));
        mandatoryMap.put("cboState", new Boolean(false));
        mandatoryMap.put("txtPincode", new Boolean(false));
        mandatoryMap.put("cboCountry", new Boolean(false));
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
        objMandatoryRB = new PaddyLocalityMasterMRB();
//        txtLocalityName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLocalityName"));
//        txtLocalityCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLocalityCode"));
//        txtStreet.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStreet"));
//        txtArea.setHelpMessage(lblMsg, objMandatoryRB.getString("txtArea"));
//        cboCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCity"));
//        cboState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboState"));
//        txtPincode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPincode"));
//        cboCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCountry"));
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
    
    public static void main(String args[]){
        javax.swing.JFrame frame = new javax.swing.JFrame();
        PaddyLocalityMasterUI ui = new PaddyLocalityMasterUI();
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
        panLocalityrMaster = new com.see.truetransact.uicomponent.CPanel();
        lblLocalityName = new com.see.truetransact.uicomponent.CLabel();
        txtLocalityName = new com.see.truetransact.uicomponent.CTextField();
        lblLocalityCode = new com.see.truetransact.uicomponent.CLabel();
        txtLocalityCode = new com.see.truetransact.uicomponent.CTextField();
        lblStreet = new com.see.truetransact.uicomponent.CLabel();
        txtStreet = new com.see.truetransact.uicomponent.CTextField();
        lblArea = new com.see.truetransact.uicomponent.CLabel();
        txtArea = new com.see.truetransact.uicomponent.CTextField();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        cboCity = new com.see.truetransact.uicomponent.CComboBox();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        cboState = new com.see.truetransact.uicomponent.CComboBox();
        lblPincode = new com.see.truetransact.uicomponent.CLabel();
        txtPincode = new com.see.truetransact.uicomponent.CTextField();
        lblCountry = new com.see.truetransact.uicomponent.CLabel();
        cboCountry = new com.see.truetransact.uicomponent.CComboBox();
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

        panLocalityrMaster.setMaximumSize(new java.awt.Dimension(800, 350));
        panLocalityrMaster.setMinimumSize(new java.awt.Dimension(800, 350));
        panLocalityrMaster.setPreferredSize(new java.awt.Dimension(800, 350));
        panLocalityrMaster.setLayout(new java.awt.GridBagLayout());

        lblLocalityName.setText("Locality Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLocalityrMaster.add(lblLocalityName, gridBagConstraints);

        txtLocalityName.setMaximumSize(new java.awt.Dimension(300, 21));
        txtLocalityName.setMinimumSize(new java.awt.Dimension(300, 21));
        txtLocalityName.setPreferredSize(new java.awt.Dimension(300, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLocalityrMaster.add(txtLocalityName, gridBagConstraints);

        lblLocalityCode.setText("Locality Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLocalityrMaster.add(lblLocalityCode, gridBagConstraints);

        txtLocalityCode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLocalityCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLocalityCodeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLocalityrMaster.add(txtLocalityCode, gridBagConstraints);

        lblStreet.setText("House No./Street");
        lblStreet.setName("lblStreet");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLocalityrMaster.add(lblStreet, gridBagConstraints);

        txtStreet.setMaxLength(256);
        txtStreet.setMinimumSize(new java.awt.Dimension(200, 21));
        txtStreet.setName("txtStreet");
        txtStreet.setPreferredSize(new java.awt.Dimension(200, 21));
        txtStreet.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLocalityrMaster.add(txtStreet, gridBagConstraints);

        lblArea.setText("Area");
        lblArea.setName("lblArea");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLocalityrMaster.add(lblArea, gridBagConstraints);

        txtArea.setMaxLength(128);
        txtArea.setMinimumSize(new java.awt.Dimension(200, 21));
        txtArea.setName("txtArea");
        txtArea.setPreferredSize(new java.awt.Dimension(200, 21));
        txtArea.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLocalityrMaster.add(txtArea, gridBagConstraints);

        lblCity.setText("City");
        lblCity.setName("lblCity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLocalityrMaster.add(lblCity, gridBagConstraints);

        cboCity.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCity.setName("cboCity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLocalityrMaster.add(cboCity, gridBagConstraints);

        lblState.setText("State");
        lblState.setName("lblState");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLocalityrMaster.add(lblState, gridBagConstraints);

        cboState.setMinimumSize(new java.awt.Dimension(100, 21));
        cboState.setName("cboState");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLocalityrMaster.add(cboState, gridBagConstraints);

        lblPincode.setText("Pincode");
        lblPincode.setName("lblPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLocalityrMaster.add(lblPincode, gridBagConstraints);

        txtPincode.setMaxLength(16);
        txtPincode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPincode.setName("txtPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLocalityrMaster.add(txtPincode, gridBagConstraints);

        lblCountry.setText("Country");
        lblCountry.setName("lblCountry");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 1, 4);
        panLocalityrMaster.add(lblCountry, gridBagConstraints);

        cboCountry.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCountry.setName("cboCountry");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLocalityrMaster.add(cboCountry, gridBagConstraints);

        getContentPane().add(panLocalityrMaster, java.awt.BorderLayout.CENTER);

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

    private void txtLocalityCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLocalityCodeFocusLost
        // TODO add your handling code here:
        if(txtLocalityCode.getText().length()>0){
            txtLocalityCode.setText(CommonUtil.convertObjToStr(txtLocalityCode.getText()).toUpperCase());
            String localityCode = CommonUtil.convertObjToStr(txtLocalityCode.getText());
            HashMap locCodeChkMap = new HashMap();
            locCodeChkMap.put("LOCALITY_CODE",localityCode);
            List list = ClientUtil.executeQuery("getLocalityCodeChk", locCodeChkMap);
            if(list!= null && list.size() > 0){
                ClientUtil.showAlertWindow("Locality Code Already exists!!!");
                txtLocalityCode.setText("");
            }
        }
        
    }//GEN-LAST:event_txtLocalityCodeFocusLost

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        viewType = ClientConstants.ACTIONTYPE_DELETE;
        observable.setStatus(); 
        popUp(ClientConstants.ACTION_STATUS[3]);
        ClientUtil.enableDisable(this, false);
        ClientUtil.enableDisable(panLocalityrMaster,false);
        lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        viewType = ClientConstants.ACTIONTYPE_EDIT;
        observable.setStatus(); 
        popUp(ClientConstants.ACTION_STATUS[2]);
        btnEdit.setEnabled(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(this,false);
        btnCancel.setEnabled(true);
        btnSave.setEnabled(true);
        lblStatus.setText(observable.getLblStatus());
        ClientUtil.enableDisable(panLocalityrMaster,true);
        txtLocalityCode.setEnabled(false);
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
                viewMap.put(CommonConstants.MAP_NAME, "getSelectLocalityMasterTOList");
                viewMap.put(CommonConstants.MAP_WHERE, wheres);
            } else if (actionType.equals(ClientConstants. ACTION_STATUS[3])){
                viewMap.put(CommonConstants.MAP_NAME, "getSelectLocalityMasterTOList");
                viewMap.put(CommonConstants.MAP_WHERE, wheres);
            }
            else if(actionType.equals("ViewDetails")){
                HashMap where = new HashMap();
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "getSelectLocalityMasterTOList");
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
        if(actionType.equals(ClientConstants.ACTION_STATUS[2]) ||
        actionType.equals(ClientConstants.ACTION_STATUS[3])|| actionType.equals("DeletedDetails") || actionType.equals("ViewDetails")|| viewType == AUTHORIZE ||
        getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
            HashMap map = new HashMap();
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                map.put("LOCALITY_CODE",hash.get("LOCALITY_CODE"));
                map.put(CommonConstants.MAP_WHERE, hash.get("LOCALITY_CODE"));
            }else{
                map.put("LOCALITY_CODE",hash.get("LOCALITY_CODE"));
                map.put(CommonConstants.MAP_WHERE, hash.get("LOCALITY_CODE"));
            }
            observable.getData(map); 
            setButtonEnableDisable();
            //For EDIT option enable disable fields and controls appropriately
            if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                ClientUtil.enableDisable(panLocalityrMaster,true);
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
            ClientUtil.enableDisable(panLocalityrMaster, false);
        }
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW_MODE){
            ClientUtil.enableDisable(this, false);
        }
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            
        }
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        viewType = ClientConstants.ACTIONTYPE_NEW;
        observable.setStatus();
        ClientUtil.enableDisable(panLocalityrMaster, true, false, true);
        txtLocalityCode.requestFocus();
        observable.resetForm();
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(this,false);
        setButtonEnableDisable();
        ClientUtil.clearAll(panLocalityrMaster);
        ClientUtil.enableDisable(panLocalityrMaster,true);
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
        map.put("RECORD_KEY", this.txtLocalityCode.getText());
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
        String mandatoryMessage = objMandatoryCheck.checkMandatory(getClass().getName(), panLocalityrMaster);
        if (mandatoryMessage.length()>0) {
            displayAlert(mandatoryMessage);
        } else {
            observable.doAction();
            if (observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) { 
                btnCancelActionPerformed(null);
                observable.resetForm();
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
        ClientUtil.enableDisable(panLocalityrMaster,false);
        lblStatus.setText(observable.getLblStatus());
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
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
            singleAuthorizeMap.put("LOCALITY_CODE", observable.getTxtLocalityCode());
            singleAuthorizeMap.put("LOCALITY_MASTER", "LOCALITY_MASTER");
            
            System.out.println("!@#$@#$@#$singleAuthorizeMap:"+singleAuthorizeMap);
            observable.authorizeLocationMaster(singleAuthorizeMap);
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(this.txtLocalityCode.getText());
            btnCancelActionPerformed(null);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
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
            mapParam.put(CommonConstants.MAP_NAME, "getLocationMasterAuthMode");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeLocalityMaster");
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
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboCity;
    private com.see.truetransact.uicomponent.CComboBox cboCountry;
    private com.see.truetransact.uicomponent.CComboBox cboState;
    private com.see.truetransact.uicomponent.CLabel lblArea;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblCountry;
    private com.see.truetransact.uicomponent.CLabel lblLocalityCode;
    private com.see.truetransact.uicomponent.CLabel lblLocalityName;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPincode;
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
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStreet;
    private com.see.truetransact.uicomponent.CMenuBar mbrShareProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panLocalityrMaster;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrShareProduct;
    private com.see.truetransact.uicomponent.CTextField txtArea;
    private com.see.truetransact.uicomponent.CTextField txtLocalityCode;
    private com.see.truetransact.uicomponent.CTextField txtLocalityName;
    private com.see.truetransact.uicomponent.CTextField txtPincode;
    private com.see.truetransact.uicomponent.CTextField txtStreet;
    // End of variables declaration//GEN-END:variables
    
}
