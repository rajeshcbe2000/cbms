/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved..
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchGroupUI.java
 *
 * Created on August 25, 2005, 11:00 AM
 */
package com.see.truetransact.ui.sysadmin.branchgroup;

import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.CommonRB;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;

import javax.swing.DefaultListModel;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIManager;
import java.util.Date;
/**
 *
 * @author  Ashok
 * 
 */
public class BranchGroupUI extends CInternalFrame implements UIMandatoryField, Observer{
    
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.branchgroup.BranchGroupRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private BranchGroupMRB objMandatoryRB;
    private BranchGroupOB observable;
    private boolean branchConfig;
    private String viewType = "";
    private final String AUTHORIZE = "Authorize";
    private Date currDt = null;
    /** Creates new form BeanForm */
    public BranchGroupUI() {
        initComponents();
        initSetup();
    }
    
    /** Creates new form BeanForm */
    public BranchGroupUI(boolean branchConfig) {
        initComponents();
        initSetup();
        this.branchConfig = branchConfig;
    }
    
    /** Initial set up */
    private void initSetup(){
        currDt = ClientUtil.getCurrentDate();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        setHelpMessage();
        setMaxLengths();
        initComponentData();
        observable.resetForm();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        enableDisableButtons(false);
    }
    
    /** Set observable */
    private void setObservable() {
        observable = BranchGroupOB.getInstance();
        observable.addObserver(this);
    }
    
    /** Setting Model for the Lists lstAvailBranch, lstGrantedBranch */
    private void initComponentData() {
        lstAvailBranch.setModel(observable.getLsmAvailableBranch());
        lstGrantedBranch.setModel(observable.getLsmGrantedBranch());
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        ((javax.swing.border.TitledBorder)panBranchGroup.getBorder()).setTitle(resourceBundle.getString("panBranchGroup"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnInclude.setText(resourceBundle.getString("btnInclude"));
        lblAvailBranch.setText(resourceBundle.getString("lblAvailBranch"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnExclude.setText(resourceBundle.getString("btnExclude"));
        lblGroupId.setText(resourceBundle.getString("lblGroupId"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblGrantedBranch.setText(resourceBundle.getString("lblGrantedBranch"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblGroupName.setText(resourceBundle.getString("lblGroupName"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtGroupId.setText(observable.getTxtGroupId());
        txtGroupName.setText(observable.getTxtGroupName());
        lstGrantedBranch.setModel(observable.getLsmGrantedBranch());
        lstAvailBranch.setModel(observable.getLsmAvailableBranch());
        lblStatus.setText(observable.getLblStatus());
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtGroupId(txtGroupId.getText());
        observable.setTxtGroupName(txtGroupName.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtGroupID", new Boolean(false));
        mandatoryMap.put("txtGroupName", new Boolean(true));
        mandatoryMap.put("lstAvailBranch", new Boolean(false));
        mandatoryMap.put("lstGrantedBranch", new Boolean(true));
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
        objMandatoryRB = new BranchGroupMRB();
        txtGroupId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGroupId"));
        txtGroupName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGroupName"));
        lstAvailBranch.setHelpMessage(lblMsg, objMandatoryRB.getString("lstAvailBranch"));
        lstGrantedBranch.setHelpMessage(lblMsg, objMandatoryRB.getString("lstGrantedBranch"));
    }
    
/* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnExclude.setName("btnExclude");
        btnInclude.setName("btnInclude");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        cSeparator1.setName("cSeparator1");
        lblAvailBranch.setName("lblAvailBranch");
        lblGroupId.setName("lblGroupId");
        lblGroupName.setName("lblBranchName");
        lblGrantedBranch.setName("lblGrantedBranch");
        lblMsg.setName("lblMsg");
        lblSpace.setName("lblSpace");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        lstAvailBranch.setName("lstAvailBranch");
        lstGrantedBranch.setName("lstGrantedBranch");
        mbrMain.setName("mbrMain");
        panBtn.setName("panBtn");
        panBranchGroup.setName("panBranchGroup");
        panScreens.setName("panScreens");
        panStatus.setName("panStatus");
        srpAvlScreen.setName("srpAvlScreen");
        srpGranScreen.setName("srpGranScreen");
        txtGroupId.setName("txtGroupId");
        txtGroupName.setName("txtGroupName");
    }
    
    /** This method sets the Maximum Length for the textfields **/
    private void setMaxLengths(){
        txtGroupId.setMaxLength(16);
        txtGroupName.setMaxLength(64);
    }
    
    // To set The Buttons Enabled or Disabled Depending on the Value or Condition.
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
        btnView.setEnabled(!btnView.isEnabled());
    }
    
    //Enable/Disable include/exclude buttons.
    private void enableDisableButtons(boolean enableDisable) {
        btnInclude.setEnabled(enableDisable);
        btnExclude.setEnabled(enableDisable);
    }
    
    /* Does necessary operaion when user clicks the save button */
    private void savePerformed()throws Exception{
        updateOBFields();
        setModified(false);
        int actionType = observable.getActionType();
        observable.doSave();
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
            ClientUtil.clearAll(this);
            observable.resetForm();
            ClientUtil.enableDisable(panBranchGroup, false);
            setButtonEnableDisable();
            enableDisableButtons(false);
            observable.setResult(actionType);
            observable.setResultStatus();
        }
    }
    
    /** This will do necessary operation for authorization **/
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getBranchGroupAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeBranchGroup");
            mapParam.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            setModified(true);
            btnCancel.setEnabled(true);
//            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("BRANCH_GROUP_ID", txtGroupId.getText());
            ClientUtil.execute("authorizeBranchGroup", singleAuthorizeMap);
            viewType = "";
            btnCancelActionPerformed(null);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrBranchGL = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace32 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace33 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panBranchGroup = new com.see.truetransact.uicomponent.CPanel();
        lblGroupId = new com.see.truetransact.uicomponent.CLabel();
        lblGroupName = new com.see.truetransact.uicomponent.CLabel();
        txtGroupId = new com.see.truetransact.uicomponent.CTextField();
        txtGroupName = new com.see.truetransact.uicomponent.CTextField();
        panScreens = new com.see.truetransact.uicomponent.CPanel();
        srpAvlScreen = new com.see.truetransact.uicomponent.CScrollPane();
        lstAvailBranch = new com.see.truetransact.uicomponent.CList();
        srpGranScreen = new com.see.truetransact.uicomponent.CScrollPane();
        lstGrantedBranch = new com.see.truetransact.uicomponent.CList();
        lblAvailBranch = new com.see.truetransact.uicomponent.CLabel();
        lblGrantedBranch = new com.see.truetransact.uicomponent.CLabel();
        panBtn = new com.see.truetransact.uicomponent.CPanel();
        btnInclude = new com.see.truetransact.uicomponent.CButton();
        btnExclude = new com.see.truetransact.uicomponent.CButton();
        cSeparator1 = new com.see.truetransact.uicomponent.CSeparator();
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
        tbrBranchGL.add(btnView);

        lblSpace5.setText("     ");
        tbrBranchGL.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrBranchGL.add(btnNew);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBranchGL.add(lblSpace29);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrBranchGL.add(btnEdit);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBranchGL.add(lblSpace30);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrBranchGL.add(btnDelete);

        lblSpace2.setText("     ");
        tbrBranchGL.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrBranchGL.add(btnSave);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBranchGL.add(lblSpace31);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrBranchGL.add(btnCancel);

        lblSpace3.setText("     ");
        tbrBranchGL.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrBranchGL.add(btnAuthorize);

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBranchGL.add(lblSpace32);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrBranchGL.add(btnException);

        lblSpace33.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace33.setText("     ");
        lblSpace33.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBranchGL.add(lblSpace33);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrBranchGL.add(btnReject);

        lblSpace4.setText("     ");
        tbrBranchGL.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrBranchGL.add(btnPrint);

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBranchGL.add(lblSpace34);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrBranchGL.add(btnClose);

        getContentPane().add(tbrBranchGL, java.awt.BorderLayout.NORTH);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        panBranchGroup.setBorder(javax.swing.BorderFactory.createTitledBorder("Branch Grouping"));
        panBranchGroup.setLayout(new java.awt.GridBagLayout());

        lblGroupId.setText("Group Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchGroup.add(lblGroupId, gridBagConstraints);

        lblGroupName.setText("Group Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchGroup.add(lblGroupName, gridBagConstraints);

        txtGroupId.setEditable(false);
        txtGroupId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchGroup.add(txtGroupId, gridBagConstraints);

        txtGroupName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtGroupName.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchGroup.add(txtGroupName, gridBagConstraints);

        panScreens.setLayout(new java.awt.GridBagLayout());

        srpAvlScreen.setPreferredSize(new java.awt.Dimension(260, 150));
        srpAvlScreen.setViewportView(lstAvailBranch);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.48;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panScreens.add(srpAvlScreen, gridBagConstraints);

        srpGranScreen.setPreferredSize(new java.awt.Dimension(260, 150));
        srpGranScreen.setViewportView(lstGrantedBranch);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.48;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panScreens.add(srpGranScreen, gridBagConstraints);

        lblAvailBranch.setText("Available Branches");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panScreens.add(lblAvailBranch, gridBagConstraints);

        lblGrantedBranch.setText("Granted Branches");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panScreens.add(lblGrantedBranch, gridBagConstraints);

        panBtn.setLayout(new java.awt.GridBagLayout());

        btnInclude.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_RIGHTARR.jpg"))); // NOI18N
        btnInclude.setMnemonic('I');
        btnInclude.setText("Include");
        btnInclude.setMaximumSize(new java.awt.Dimension(100, 26));
        btnInclude.setMinimumSize(new java.awt.Dimension(100, 26));
        btnInclude.setPreferredSize(new java.awt.Dimension(100, 26));
        btnInclude.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncludeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtn.add(btnInclude, gridBagConstraints);

        btnExclude.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_LEFTARR.jpg"))); // NOI18N
        btnExclude.setMnemonic('x');
        btnExclude.setText("Exclude");
        btnExclude.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcludeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtn.add(btnExclude, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.04;
        panScreens.add(panBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchGroup.add(panScreens, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 8, 4);
        panBranchGroup.add(cSeparator1, gridBagConstraints);

        getContentPane().add(panBranchGroup, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");

        mitNew.setMnemonic('N');
        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptDelete);

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

        mitClose.setMnemonic('L');
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

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTIONTYPE_VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
        	private void btnExcludeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcludeActionPerformed
                    Object selected[] = lstGrantedBranch.getSelectedValues();
                    observable.removedBranchIds(lstGrantedBranch.getSelectedValues());
                    for (int i=0; i < selected.length; i++) {
                        observable.getLsmGrantedBranch().removeElement(selected[i]);
                        observable.getLsmAvailableBranch().addElement(selected[i]);
                    }
                    includeExcludeButton((DefaultListModel)lstAvailBranch.getModel(),(DefaultListModel)lstGrantedBranch.getModel());
    }//GEN-LAST:event_btnExcludeActionPerformed
                        	private void btnIncludeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncludeActionPerformed
                                    Object selected[] = lstAvailBranch.getSelectedValues();
                                    observable.newBranchIds(lstAvailBranch.getSelectedValues());
                                    for (int i=0; i < selected.length; i++) {
                                        observable.getLsmGrantedBranch().add(i,selected[i]);
                                        observable.getLsmAvailableBranch().removeElement(selected[i]);
                                    }
                                    includeExcludeButton((DefaultListModel)lstAvailBranch.getModel(),(DefaultListModel)lstGrantedBranch.getModel());
    }//GEN-LAST:event_btnIncludeActionPerformed
                                                	private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
                                                            // Add your handling code here:
                                                            cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
                                                                                	private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
                                                                                            // Add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed
                                                                                                                        	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
                                                                                                                                    // Add your handling code here:
                                                                                                                                    observable.resetForm();
                                                                                                                                    setModified(false);
                                                                                                                                    ClientUtil.clearAll(this);
                                                                                                                                    ClientUtil.enableDisable(panBranchGroup, false);
                                                                                                                                    observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
                                                                                                                                    enableDisableButtons(false);
                                                                                                                                    setButtonEnableDisable();
                                                                                                                                    viewType = "";
                                                                                                                                     btnAuthorize.setEnabled(true);
                                                                                                                                     btnReject.setEnabled(true);
                                                                                                                                     btnException.setEnabled(true);
                                                                                                                                     btnView.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed
                                                                                                                                                                        	private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
                                                                                                                                                                                    // Add your handling code here:
                                                                                                                                                                                    try{
                                                                                                                                                                                        String mandatoryMessage = checkMandatory(panBranchGroup);
                                                                                                                                                                                        StringBuffer message = new StringBuffer(mandatoryMessage);
                                                                                                                                                                                        if(observable.getLsmGrantedBranch().size() == 0){
                                                                                                                                                                                            message.append(objMandatoryRB.getString("lstGrantedBranch"));
                                                                                                                                                                                        }
                                                                                                                                                                                        if(message.length() > 0){
                                                                                                                                                                                            displayAlert(message.toString());
                                                                                                                                                                                        }else{
                                                                                                                                                                                            savePerformed();
                                                                                                                                                                                        }
                                                                                                                                                                                    }catch(Exception e){
                                                                                                                                                                                    }
                                                                                                                                                                                     btnAuthorize.setEnabled(true);
                                                                                                                                                                                     btnReject.setEnabled(true);
                                                                                                                                                                                     btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
                                                                                                                                                                                                                                    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
                                                                                                                                                                                                                                        // Add your handling code here:
                                                                                                                                                                                                                                        observable.resetForm();
                                                                                                                                                                                                                                        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
                                                                                                                                                                                                                                        callView(ClientConstants.ACTIONTYPE_DELETE);
                                                                                                                                                                                                                                        ClientUtil.enableDisable(panBranchGroup, false);
                                                                                                                                                                                                                                        setButtonEnableDisable();
                                                                                                                                                                                                                                        enableDisableButtons(false);
                                                                                                                                                                                                                                        btnAuthorize.setEnabled(false);
                                                                                                                                                                                                                                        btnReject.setEnabled(false);
                                                                                                                                                                                                                                        btnException.setEnabled(false);
                                                                                                                                                                                                                                        
    }//GEN-LAST:event_btnDeleteActionPerformed
                                                                                                                                                                                                                                                                                            private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
                                                                                                                                                                                                                                                                                                // Add your handling code here:
                                                                                                                                                                                                                                                                                                observable.resetForm();
                                                                                                                                                                                                                                                                                                observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
                                                                                                                                                                                                                                                                                                callView(ClientConstants.ACTIONTYPE_EDIT);
                                                                                                                                                                                                                                                                                                ClientUtil.enableDisable(panBranchGroup, true);
                                                                                                                                                                                                                                                                                                setButtonEnableDisable();
                                                                                                                                                                                                                                                                                                enableDisableButtons(true);
                                                                                                                                                                                                                                                                                                btnAuthorize.setEnabled(false);
                                                                                                                                                                                                                                                                                                btnReject.setEnabled(false);
                                                                                                                                                                                                                                                                                                btnException.setEnabled(false);
                                                                                                                                                                                                                                                                                                
                                                                                                                                                                                                                                                                                                
    }//GEN-LAST:event_btnEditActionPerformed
                                                                                                                                                                                                                                                                                            
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setModified(true);
        observable.resetForm();
        ClientUtil.clearAll(this);
        enableDisableButtons(true);
        btnExclude.setEnabled(false);
        ClientUtil.enableDisable(panBranchGroup, true);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        observable.fillAvailableBranchIds();
        lstAvailBranch.setModel(observable.getLsmAvailableBranch());
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
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
    
    
    /** Check Whether the Mandatory Fields are filled up */
    private String checkMandatory(JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(),component);
    }
    
    /** Displays an Alert message when user fails to enter data in the mandatoryfields */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /** This methodshows the popup window with relavant data */
    private void callView(int currField) {
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        observable.setActionType(currField);
        viewMap.put(CommonConstants.MAP_NAME, "getSelectBranchGroup");
        new ViewAll(this, viewMap).show();
    }
    
    /** Fills up the UI fields when an row is selected in the Popup window */
    public void fillData(Object  map) {
        HashMap hash = (HashMap) map;
        setModified(true);
        final String groupId = CommonUtil.convertObjToStr(hash.get("BRANCH_GROUP_ID"));
        int action = observable.getActionType();
        if (action == ClientConstants.ACTIONTYPE_EDIT ||
        action == ClientConstants.ACTIONTYPE_DELETE || viewType.equals(AUTHORIZE) ||
        action == ClientConstants.ACTIONTYPE_VIEW) {
            hash.put(CommonConstants.MAP_WHERE, hash.get("BRANCH_GROUP_ID"));
            hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            observable.populateData(hash);
        }
        if(viewType.equals(AUTHORIZE)|| action == ClientConstants.ACTIONTYPE_VIEW){
            ClientUtil.enableDisable(panBranchGroup, false);
            enableDisableButtons(false);
            btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
            btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
            btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            btnSave.setEnabled(false);
        }
        observable.fillDropdown(groupId);
        observable.notifyObservers();
    }
    
    /** Depending upon the values of listbox enable/disable include/exclude button. */
    public void includeExcludeButton(DefaultListModel availableScreen,DefaultListModel grantScreen) {
        int avai=availableScreen.getSize();
        int grant=grantScreen.getSize();
        btnInclude.setEnabled(avai == 0?false:true);
        btnExclude.setEnabled(grant == 0?false:true);
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnExclude;
    private com.see.truetransact.uicomponent.CButton btnInclude;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CSeparator cSeparator1;
    private com.see.truetransact.uicomponent.CLabel lblAvailBranch;
    private com.see.truetransact.uicomponent.CLabel lblGrantedBranch;
    private com.see.truetransact.uicomponent.CLabel lblGroupId;
    private com.see.truetransact.uicomponent.CLabel lblGroupName;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
    private com.see.truetransact.uicomponent.CLabel lblSpace33;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CList lstAvailBranch;
    private com.see.truetransact.uicomponent.CList lstGrantedBranch;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panBranchGroup;
    private com.see.truetransact.uicomponent.CPanel panBtn;
    private com.see.truetransact.uicomponent.CPanel panScreens;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srpAvlScreen;
    private com.see.truetransact.uicomponent.CScrollPane srpGranScreen;
    private javax.swing.JToolBar tbrBranchGL;
    private com.see.truetransact.uicomponent.CTextField txtGroupId;
    private com.see.truetransact.uicomponent.CTextField txtGroupName;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] arg){
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        JFrame jf = new JFrame();
        BranchGroupUI gui = new BranchGroupUI();
        jf.getContentPane().add(gui);
        jf.setSize(650, 660);
        jf.show();
        gui.show();
    }
}
