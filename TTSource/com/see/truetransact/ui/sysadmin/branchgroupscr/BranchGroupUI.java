/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchGroupUI.java
 *
 * Created on February 25, 2004, 2:06 PM
 */
package com.see.truetransact.ui.sysadmin.branchgroupscr;

import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.CommonRB;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import java.util.ResourceBundle;
import com.see.truetransact.clientproxy.ProxyParameters;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.ToolTipManager;

import com.see.truetransact.ui.sysadmin.group.ScreenModuleTreeNode;
import com.see.truetransact.ui.sysadmin.group.GroupTreeCellRenderer;
import java.util.Date;
/**
 *
 * @author  Pinky
 */

/** Edit will do physical delete in group_screen if you delete any screen assigned
 *  previously to group.
 */

public class BranchGroupUI extends CInternalFrame implements UIMandatoryField, Observer{
    
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.branchgroupscr.BranchGroupRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private BranchGroupMRB objMandatoryRB;
    private BranchGroupOB observable;
    private boolean branchConfig=true;
    private final String AUTHORIZE = "Authorize";
    private String viewType = "";
    private Date currDt = null;
    /** Creates new form BeanForm */
    public BranchGroupUI() {
        initComponents();
        initSetup();
        lblGrpID.setText(resourceBundle.getString("lblBranch") + lblGrpID.getText());
        lblGrpDesc.setText(resourceBundle.getString("lblBranch") + lblGrpDesc.getText());
    }
    
    /** Initial set up */
    private void initSetup(){
        currDt = ClientUtil.getCurrentDate();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        setHelpMessage();
        setMaxLength();
        observable.resetForm();
        setUp(ClientConstants.ACTIONTYPE_CANCEL,false);
    }
    /** Set observable */
    private void setObservable() {
        observable = new BranchGroupOB(branchConfig);
        observable.addObserver(this);
    }
    
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
        lblAvaScreen.setName("lblAvaScreen");
        lblGrantScreen.setName("lblGrantScreen");
        lblGrpDesc.setName("lblGrpDesc");
        lblGrpID.setName("lblGrpID");
        lblMsg.setName("lblMsg");
        lblSpace.setName("lblSpace");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        treAvaiScreen.setName("treAvaiScreen");
        treGrantScreen.setName("treGrantScreen");
        mbrMain.setName("mbrMain");
        panBtn.setName("panBtn");
        panGrpAccess.setName("panGrpAccess");
        panScreens.setName("panScreens");
        panStatus.setName("panStatus");
        srpAvlScreen.setName("srpAvlScreen");
        srpGranScreen.setName("srpGranScreen");
        txtGrpDesc.setName("txtGrpDesc");
        txtGrpID.setName("txtGrpID");
    }
    
    private void internationalize() {
        btnExclude.setText(resourceBundle.getString("btnExclude"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblGrantScreen.setText(resourceBundle.getString("lblGrantScreen"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        ((TitledBorder)panGrpAccess.getBorder()).setTitle(resourceBundle.getString("panGrpAccess"));
        btnInclude.setText(resourceBundle.getString("btnInclude"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblGrpID.setText(resourceBundle.getString("lblGrpID"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblAvaScreen.setText(resourceBundle.getString("lblAvaScreen"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblGrpDesc.setText(resourceBundle.getString("lblGrpDesc"));
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtGrpDesc", new Boolean(true));
        mandatoryMap.put("txtGrpID", new Boolean(false));
        mandatoryMap.put("treAvaiScreen", new Boolean(false));
        mandatoryMap.put("treGrantScreen", new Boolean(false));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    public void update(Observable observed, Object arg) {
        txtGrpID.setText(observable.getTxtGrpID());
        txtGrpDesc.setText(observable.getTxtGrpDesc());
        lblStatus.setText(observable.getLblStatus());
        treAvaiScreen.setModel(observable.getAvailableScreenModel());
        treGrantScreen.setModel(observable.getGrantScreenModel());
        
        //Enable/Disable include/exclude button depending upon the values in list box.
        
        includeExcludeButton((DefaultTreeModel)treAvaiScreen.getModel(),(DefaultTreeModel)treGrantScreen.getModel());
    }
    public void updateOBFields() {
        observable.setTxtGrpDesc(txtGrpDesc.getText());
        observable.setGrantScreen(observable.createArrayList((DefaultTreeModel)treGrantScreen.getModel()));
    }
    public void setHelpMessage() {
        objMandatoryRB = new BranchGroupMRB();
        txtGrpID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGrpID"));
        txtGrpDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGrpDesc"));
    }
    
    //To set the length of the Text Fields
    private void setMaxLength() {
        txtGrpID.setMaxLength(16);
        txtGrpDesc.setMaxLength(128);
    }
    
    // To set The Value of the Buttons Depending on the Value or Condition.
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
        btnView.setEnabled(!btnView.isEnabled());
    }
    
    //Enable/Disable include/exclude buttons.
    private void enableDisableButtons(boolean enableDisable) {
        btnInclude.setEnabled(enableDisable);
        btnExclude.setEnabled(enableDisable);
    }
    
    //Set GroupId not editable
    private void editableFields() {
        txtGrpID.setEditable(false);
    }
    
    /** This will do necessary operation for authorization **/
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getBranchGroupMasterAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeBranchGroupMaster");
            mapParam.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            CommonRB objCommonRB = new CommonRB();
            String branchGrp = objCommonRB.getString("NO");
            if (branchConfig) branchGrp = objCommonRB.getString("YES");
            HashMap where = new HashMap();
            where.put("BRANCH_GROUP", branchGrp);
            mapParam.put(CommonConstants.MAP_WHERE, where);
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            setModified(true);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("GROUP_ID", txtGrpID.getText());
            singleAuthorizeMap.put("AUTHORIZEDT", currDt.clone());
            ClientUtil.execute("authorizeBranchGroupMaster", singleAuthorizeMap);
            viewType = "";
            btnCancelActionPerformed(null);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        btnException1 = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        btnException = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panGrpAccess = new com.see.truetransact.uicomponent.CPanel();
        lblGrpID = new com.see.truetransact.uicomponent.CLabel();
        lblGrpDesc = new com.see.truetransact.uicomponent.CLabel();
        txtGrpID = new com.see.truetransact.uicomponent.CTextField();
        txtGrpDesc = new com.see.truetransact.uicomponent.CTextField();
        panScreens = new com.see.truetransact.uicomponent.CPanel();
        srpAvlScreen = new com.see.truetransact.uicomponent.CScrollPane();
        treAvaiScreen = new javax.swing.JTree();
        ToolTipManager.sharedInstance().registerComponent(treAvaiScreen);
        srpGranScreen = new com.see.truetransact.uicomponent.CScrollPane();
        treGrantScreen = new javax.swing.JTree();
        ToolTipManager.sharedInstance().registerComponent(treGrantScreen);
        lblAvaScreen = new com.see.truetransact.uicomponent.CLabel();
        lblGrantScreen = new com.see.truetransact.uicomponent.CLabel();
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

        tbrAdvances.add(btnView);

        lblSpace5.setText("     ");
        tbrAdvances.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        tbrAdvances.add(btnNew);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif")));
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        tbrAdvances.add(btnEdit);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        tbrAdvances.add(btnDelete);

        lblSpace2.setText("     ");
        tbrAdvances.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        tbrAdvances.add(btnSave);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif")));
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        tbrAdvances.add(btnCancel);

        lblSpace4.setText("     ");
        tbrAdvances.add(lblSpace4);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif")));
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });

        tbrAdvances.add(btnAuthorize);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif")));
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });

        tbrAdvances.add(btnReject);

        btnException1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif")));
        btnException1.setToolTipText("Exception");
        btnException1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnException1ActionPerformed(evt);
            }
        });

        tbrAdvances.add(btnException1);

        lblSpace3.setText("     ");
        tbrAdvances.add(lblSpace3);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif")));
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        tbrAdvances.add(btnPrint);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif")));
        btnException.setToolTipText("Exception");
        tbrAdvances.add(btnException);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif")));
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        tbrAdvances.add(btnClose);

        getContentPane().add(tbrAdvances, java.awt.BorderLayout.NORTH);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace, gridBagConstraints);

        lblStatus.setBorder(new javax.swing.border.EtchedBorder());
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

        panGrpAccess.setLayout(new java.awt.GridBagLayout());

        panGrpAccess.setBorder(new javax.swing.border.TitledBorder("Group Access"));
        lblGrpID.setText("Group ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGrpAccess.add(lblGrpID, gridBagConstraints);

        lblGrpDesc.setText("Group Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGrpAccess.add(lblGrpDesc, gridBagConstraints);

        txtGrpID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGrpAccess.add(txtGrpID, gridBagConstraints);

        txtGrpDesc.setMinimumSize(new java.awt.Dimension(200, 21));
        txtGrpDesc.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGrpAccess.add(txtGrpDesc, gridBagConstraints);

        panScreens.setLayout(new java.awt.GridBagLayout());

        srpAvlScreen.setPreferredSize(new java.awt.Dimension(260, 150));
        treAvaiScreen.setCellRenderer(new GroupTreeCellRenderer());

        srpAvlScreen.setViewportView(treAvaiScreen);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.48;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panScreens.add(srpAvlScreen, gridBagConstraints);

        srpGranScreen.setPreferredSize(new java.awt.Dimension(260, 150));
        treGrantScreen.setCellRenderer(new GroupTreeCellRenderer());
        srpGranScreen.setViewportView(treGrantScreen);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.48;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panScreens.add(srpGranScreen, gridBagConstraints);

        lblAvaScreen.setText("Available Screens");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panScreens.add(lblAvaScreen, gridBagConstraints);

        lblGrantScreen.setText("Granted Screens");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panScreens.add(lblGrantScreen, gridBagConstraints);

        panBtn.setLayout(new java.awt.GridBagLayout());

        btnInclude.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_RIGHTARR.jpg")));
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

        btnExclude.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_LEFTARR.jpg")));
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
        panGrpAccess.add(panScreens, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 8, 4);
        panGrpAccess.add(cSeparator1, gridBagConstraints);

        getContentPane().add(panGrpAccess, java.awt.BorderLayout.CENTER);

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
    }//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTIONTYPE_VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void btnException1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnException1ActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnException1ActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnExcludeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcludeActionPerformed
        // Add your handling code here:
        observable.includeExclude(treGrantScreen,treAvaiScreen);
        includeExcludeButton((DefaultTreeModel)treAvaiScreen.getModel(),(DefaultTreeModel)treGrantScreen.getModel());
    }//GEN-LAST:event_btnExcludeActionPerformed
    
    private void btnIncludeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncludeActionPerformed
        // Add your handling code here:
        observable.includeExclude(treAvaiScreen,treGrantScreen);
        includeExcludeButton((DefaultTreeModel)treAvaiScreen.getModel(),(DefaultTreeModel)treGrantScreen.getModel());
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
        setModified(false);
        observable.resetForm();
        setUp(ClientConstants.ACTIONTYPE_CANCEL,false);
        viewType = "";
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException1.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        setModified(false);
        final String mandatoryMessage = checkMandatory(panGrpAccess);
        if(mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }else if((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ||observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)&& !screenSelChk()){
             displayAlert(resourceBundle.getString("noGrantScreen"));
        }else{
            updateOBFields();
            observable.doAction();
            observable.resetForm();
            setUp(ClientConstants.ACTIONTYPE_CANCEL, false);
            observable.setResultStatus();
        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException1.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
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
    //To check whether atleast one screen is granted screen
    private boolean screenSelChk(){
        javax.swing.tree.DefaultTreeModel treeModel = (javax.swing.tree.DefaultTreeModel)treGrantScreen.getModel();
        if (treeModel.getChildCount(treeModel.getRoot())==0){
            return false;
        }
        return true;
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        setUp(ClientConstants.ACTIONTYPE_DELETE, false);
        callView(ClientConstants.ACTIONTYPE_DELETE);
        enableDisableButtons(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException1.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        setUp(ClientConstants.ACTIONTYPE_EDIT,true);
        callView(ClientConstants.ACTIONTYPE_EDIT);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException1.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setAvailableScreen(observable.getAllAvailableScreen());
        setUp(ClientConstants.ACTIONTYPE_NEW,true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException1.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
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
    
    private String checkMandatory(JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(),component);
    }
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    private void callView(int currField) {
        HashMap viewMap = new HashMap();
        
        HashMap whereMap = new HashMap();
        
        CommonRB objCommonRB = new CommonRB();
        
        String branchGrp = objCommonRB.getString("NO");
        if (branchConfig) branchGrp = objCommonRB.getString("YES");
        whereMap.put("BRANCH_GROUP", branchGrp);
        
        observable.setActionType(currField);
        viewMap.put(CommonConstants.MAP_NAME, "selectBranchGroupMaster");
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this, viewMap).show();
    }
    public void fillData(Object  map) {
        setModified(true);
        HashMap hash = (HashMap) map;
        int action = observable.getActionType();
        if (action == ClientConstants.ACTIONTYPE_EDIT ||
        action == ClientConstants.ACTIONTYPE_DELETE || viewType.equals(AUTHORIZE)||
        action == ClientConstants.ACTIONTYPE_VIEW) {
            hash.put(CommonConstants.MAP_WHERE, hash.get("GROUP_ID"));
            observable.populateData(hash);
            if(viewType.equals(AUTHORIZE) || action == ClientConstants.ACTIONTYPE_VIEW){
                enableDisableButtons(false);
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException1.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                btnNew.setEnabled(false);
                btnEdit.setEnabled(false);
                btnDelete.setEnabled(false);
            }
        }
    }
    
    /** Depending upon the values of listbox enable/disable include/exclude button. */
    public void includeExcludeButton(DefaultTreeModel availableScreen,DefaultTreeModel grantScreen) {
        int avai=((ScreenModuleTreeNode)availableScreen.getRoot()).getChildCount();
        int grant=((ScreenModuleTreeNode)grantScreen.getRoot()).getChildCount();
        btnInclude.setEnabled(avai == 0?false:true);
        btnExclude.setEnabled(grant == 0?false:true);
    }
    
    /** Setting up initial setup of the form */
    public void setUp(int actionType,boolean isEnable) {
        ClientUtil.enableDisable(this, isEnable);
        setButtonEnableDisable();
        editableFields();
        enableDisableButtons(isEnable);
        
        observable.setActionType(actionType);
        observable.setStatus();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnException1;
    private com.see.truetransact.uicomponent.CButton btnExclude;
    private com.see.truetransact.uicomponent.CButton btnInclude;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CSeparator cSeparator1;
    private com.see.truetransact.uicomponent.CLabel lblAvaScreen;
    private com.see.truetransact.uicomponent.CLabel lblGrantScreen;
    private com.see.truetransact.uicomponent.CLabel lblGrpDesc;
    private com.see.truetransact.uicomponent.CLabel lblGrpID;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panBtn;
    private com.see.truetransact.uicomponent.CPanel panGrpAccess;
    private com.see.truetransact.uicomponent.CPanel panScreens;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srpAvlScreen;
    private com.see.truetransact.uicomponent.CScrollPane srpGranScreen;
    private javax.swing.JToolBar tbrAdvances;
    private javax.swing.JTree treAvaiScreen;
    private javax.swing.JTree treGrantScreen;
    private com.see.truetransact.uicomponent.CTextField txtGrpDesc;
    private com.see.truetransact.uicomponent.CTextField txtGrpID;
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
