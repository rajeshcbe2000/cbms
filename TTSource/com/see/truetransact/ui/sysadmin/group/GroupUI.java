/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GroupUI.java
 *
 * Created on February 25, 2004, 2:06 PM
 */
package com.see.truetransact.ui.sysadmin.group;

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
import java.util.ArrayList;
import javax.swing.tree.TreePath;
import java.util.Date;
/**
 *
 * @author  Pinky
 */

/** Edit will do physical delete in group_screen if you delete any screen assigned
 *  previously to group.
 */

public class GroupUI extends CInternalFrame implements UIMandatoryField, Observer{
    
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.group.GroupRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private GroupMRB objMandatoryRB;
    private GroupOB observable;
    private boolean branchConfig;
    private final String AUTHORIZE = "Authorize";
    private String viewType = "";
    private ScreenModuleTreeNode oldNode = null;
    private HashMap oldChildMap=null;
    private Date currDt = null;
    /** Creates new form BeanForm */
    public GroupUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initSetup();
    }
    
    /** Creates new form BeanForm */
    public GroupUI(boolean branchConfig) {
        currDt = ClientUtil.getCurrentDate();
        this.branchConfig = branchConfig;
        initComponents();
        initSetup();
        if (this.branchConfig) {
            lblGrpID.setText(resourceBundle.getString("lblBranch") + lblGrpID.getText());
            lblGrpDesc.setText(resourceBundle.getString("lblBranch") + lblGrpDesc.getText());
        }
    }
    
    /** Initial set up */
    private void initSetup(){        
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        setHelpMessage();
        setMaxLength();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panGrpAccess);
        observable.resetForm();
        setUp(ClientConstants.ACTIONTYPE_CANCEL,false);
        btnView.setEnabled(!btnView.isEnabled());
    }
    /** Set observable */
    private void setObservable() {
        observable = new GroupOB(branchConfig);
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
        
        txtBranchGroup.setName("txtBranchGroup");
        lblBranchGroup.setName("lblBranchGroup");
        chkNewAllowed.setName("chkNewAllowed");
        chkEditAllowed.setName("chkEditAllowed");
        chkDeleteAllowed.setName("chkDeleteAllowed");
        chkAuthRejAllowed.setName("chkAuthRejAllowed");
        chkExceptionAllowed.setName("chkExceptionAllowed");
        chkPrintAllowed.setName("chkPrintAllowed");
        chkInterbranchAllowed.setName("chkInterbranchAllowed");
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
        lblBranchGroup.setText(resourceBundle.getString("lblBranchGroup"));
        chkNewAllowed.setText(resourceBundle.getString("chkNewAllowed"));
        chkEditAllowed.setText(resourceBundle.getString("chkEditAllowed"));
        chkDeleteAllowed.setText(resourceBundle.getString("chkDeleteAllowed"));
        chkAuthRejAllowed.setText(resourceBundle.getString("chkAuthRejAllowed"));
        chkExceptionAllowed.setText(resourceBundle.getString("chkExceptionAllowed"));
        chkPrintAllowed.setText(resourceBundle.getString("chkPrintAllowed"));
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtGrpDesc", new Boolean(true));
        mandatoryMap.put("txtGrpID", new Boolean(false));
        mandatoryMap.put("treAvaiScreen", new Boolean(false));
        mandatoryMap.put("treGrantScreen", new Boolean(false));
        mandatoryMap.put("txtBranchGroup", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    public void update(Observable observed, Object arg) {
        txtGrpID.setText(observable.getTxtGrpID());
        txtGrpDesc.setText(observable.getTxtGrpDesc());
        txtBranchGroup.setText(observable.getTxtBranchGroup());
        lblStatus.setText(observable.getLblStatus());
        treAvaiScreen.setModel(observable.getAvailableScreenModel());
        treGrantScreen.setModel(observable.getGrantScreenModel());
        
//        System.out.println("@@@@@@@@@@@@treGrantScreen:"+treGrantScreen.getModel());
//        System.out.println("should be interesting:"+observable.createArrayList((DefaultTreeModel)treGrantScreen.getModel()));
//        treGrantScreen.get
        
        //Enable/Disable include/exclude button depending upon the values in list box.
        
        includeExcludeButton((DefaultTreeModel)treAvaiScreen.getModel(),(DefaultTreeModel)treGrantScreen.getModel());
        updateRights();
    }
    
    private void updateRights(){     
        chkNewAllowed.setSelected(observable.getChkNewAllowed());
        chkEditAllowed.setSelected(observable.getChkEditAllowed());
        chkDeleteAllowed.setSelected(observable.getChkDeleteAllowed());
        chkAuthRejAllowed.setSelected(observable.getChkAuthRejAllowed());
        chkExceptionAllowed.setSelected(observable.getChkExceptionAllowed());
        chkPrintAllowed.setSelected(observable.getChkPrintAllowed());
    }
    
    public void updateOBFields() { 
        observable.setTxtBranchGroup(txtBranchGroup.getText());        
        observable.setTxtGrpDesc(txtGrpDesc.getText());
        observable.setGrantScreen(observable.createArrayList((DefaultTreeModel)treGrantScreen.getModel()));        
        updateOBFieldsRights();
    }
    
    public void updateOBFieldsRights() {     
        observable.setChkNewAllowed(chkNewAllowed.isSelected());
        observable.setChkEditAllowed(chkEditAllowed.isSelected());
        observable.setChkDeleteAllowed(chkDeleteAllowed.isSelected());
        observable.setChkAuthRejAllowed(chkAuthRejAllowed.isSelected());
        observable.setChkExceptionAllowed(chkExceptionAllowed.isSelected());
        observable.setChkPrintAllowed(chkPrintAllowed.isSelected());
    }
    
    public void setHelpMessage() {
        objMandatoryRB = new GroupMRB();
        txtGrpID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGrpID"));
        txtGrpDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGrpDesc"));
        
        txtBranchGroup.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchGroup"));
        chkNewAllowed.setHelpMessage(lblMsg, objMandatoryRB.getString("chkNewAllowed"));
        chkEditAllowed.setHelpMessage(lblMsg, objMandatoryRB.getString("chkEditAllowed"));
        chkDeleteAllowed.setHelpMessage(lblMsg, objMandatoryRB.getString("chkDeleteAllowed"));
        chkAuthRejAllowed.setHelpMessage(lblMsg, objMandatoryRB.getString("chkAuthRejAllowed"));
        chkExceptionAllowed.setHelpMessage(lblMsg, objMandatoryRB.getString("chkExceptionAllowed"));
        chkPrintAllowed.setHelpMessage(lblMsg, objMandatoryRB.getString("chkPrintAllowed"));
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
    }
    
    //Enable/Disable include/exclude buttons.
    private void enableDisableButtons(boolean enableDisable) {
        btnInclude.setEnabled(enableDisable);
        btnExclude.setEnabled(enableDisable);
    }
    
    //Set GroupId not editable
    private void editableFields() {
        txtGrpID.setEnabled(false);
    }
    
    /** This will do necessary operation for authorization **/
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)){
           viewType = AUTHORIZE;
            setModified(true);
            HashMap mapParam = new HashMap();
         
            mapParam.put(CommonConstants.MAP_NAME, "getGroupMasterAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeGroupMaster");
            mapParam.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
           CommonRB objCommonRB = new CommonRB();
              String branchGrp = objCommonRB.getString("NO");
            if (branchConfig) branchGrp = objCommonRB.getString("YES");
            HashMap where = new HashMap();
            where.put("BRANCH_GROUP", branchGrp);
           mapParam.put(CommonConstants.MAP_WHERE, where);
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            //            setModified(true);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
//            lblStatus.setText(observable.getLblStatus());
        } else if (viewType.equals(AUTHORIZE)){
            
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);                 
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("GROUP_ID", txtGrpID.getText());
            singleAuthorizeMap.put("AUTHORIZEDT", currDt.clone());
            ClientUtil.execute("authorizeGroupMaster", singleAuthorizeMap);
            viewType = "";
            btnCancelActionPerformed(null);
        }
        oldChildMap=null;
        oldNode = null;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace32 = new com.see.truetransact.uicomponent.CLabel();
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
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        chkNewAllowed = new com.see.truetransact.uicomponent.CCheckBox();
        chkDeleteAllowed = new com.see.truetransact.uicomponent.CCheckBox();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        chkEditAllowed = new com.see.truetransact.uicomponent.CCheckBox();
        chkAuthRejAllowed = new com.see.truetransact.uicomponent.CCheckBox();
        chkPrintAllowed = new com.see.truetransact.uicomponent.CCheckBox();
        chkExceptionAllowed = new com.see.truetransact.uicomponent.CCheckBox();
        cPanel3 = new com.see.truetransact.uicomponent.CPanel();
        chkInterbranchAllowed = new com.see.truetransact.uicomponent.CCheckBox();
        cSeparator1 = new com.see.truetransact.uicomponent.CSeparator();
        txtBranchGroup = new com.see.truetransact.uicomponent.CTextField();
        lblBranchGroup = new com.see.truetransact.uicomponent.CLabel();
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
        tbrAdvances.add(btnView);

        lblSpace5.setText("     ");
        tbrAdvances.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnNew);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace27);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace28);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
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

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnSave);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace29);

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

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace30);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace31);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnReject);

        lblSpace3.setText("     ");
        tbrAdvances.add(lblSpace3);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnPrint);

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace32);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
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

        panGrpAccess.setBorder(javax.swing.BorderFactory.createTitledBorder("Group Access"));
        panGrpAccess.setLayout(new java.awt.GridBagLayout());

        lblGrpID.setText("Group ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGrpAccess.add(lblGrpID, gridBagConstraints);

        lblGrpDesc.setText("Group Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGrpAccess.add(lblGrpDesc, gridBagConstraints);

        txtGrpID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGrpAccess.add(txtGrpID, gridBagConstraints);

        txtGrpDesc.setMinimumSize(new java.awt.Dimension(200, 21));
        txtGrpDesc.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
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
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.48;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panScreens.add(srpAvlScreen, gridBagConstraints);

        srpGranScreen.setPreferredSize(new java.awt.Dimension(260, 150));

        treGrantScreen.setCellRenderer(new GroupTreeCellRenderer());
        treGrantScreen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                treGrantScreenMouseClicked(evt);
            }
        });
        srpGranScreen.setViewportView(treGrantScreen);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
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
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 0.04;
        panScreens.add(panBtn, gridBagConstraints);

        cPanel1.setMaximumSize(new java.awt.Dimension(190, 85));
        cPanel1.setMinimumSize(new java.awt.Dimension(190, 85));
        cPanel1.setPreferredSize(new java.awt.Dimension(190, 85));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        chkNewAllowed.setText("New");
        chkNewAllowed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNewAllowedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        cPanel1.add(chkNewAllowed, gridBagConstraints);

        chkDeleteAllowed.setText("Delete");
        chkDeleteAllowed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDeleteAllowedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        cPanel1.add(chkDeleteAllowed, gridBagConstraints);

        cPanel2.setLayout(new java.awt.GridBagLayout());

        chkEditAllowed.setText("Edit");
        chkEditAllowed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkEditAllowedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        cPanel2.add(chkEditAllowed, gridBagConstraints);

        chkAuthRejAllowed.setText("Authorize");
        chkAuthRejAllowed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAuthRejAllowedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        cPanel2.add(chkAuthRejAllowed, gridBagConstraints);

        chkPrintAllowed.setText("Print");
        chkPrintAllowed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrintAllowedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        cPanel2.add(chkPrintAllowed, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        cPanel1.add(cPanel2, gridBagConstraints);

        chkExceptionAllowed.setText("Exception");
        chkExceptionAllowed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkExceptionAllowedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        cPanel1.add(chkExceptionAllowed, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panScreens.add(cPanel1, gridBagConstraints);

        cPanel3.setMinimumSize(new java.awt.Dimension(180, 30));
        cPanel3.setPreferredSize(new java.awt.Dimension(180, 30));

        chkInterbranchAllowed.setText("Interbranch Allowed");
        chkInterbranchAllowed.setAlignmentY(0.0F);
        chkInterbranchAllowed.setMaximumSize(new java.awt.Dimension(164, 27));
        chkInterbranchAllowed.setMinimumSize(new java.awt.Dimension(164, 27));
        chkInterbranchAllowed.setPreferredSize(new java.awt.Dimension(164, 27));
        cPanel3.add(chkInterbranchAllowed);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panScreens.add(cPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGrpAccess.add(panScreens, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 8, 4);
        panGrpAccess.add(cSeparator1, gridBagConstraints);

        txtBranchGroup.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGrpAccess.add(txtBranchGroup, gridBagConstraints);

        lblBranchGroup.setText("Branch Group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGrpAccess.add(lblBranchGroup, gridBagConstraints);

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
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTIONTYPE_VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void treGrantScreenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treGrantScreenMouseClicked
        // TODO add your handling code here:
        if (treGrantScreen.getSelectionCount()==1){
//            DefaultTreeModel treGrantScreenModel = treGrantScreen.getModel();
//            System.out.println("(ScreenModuleTreeNode)treGrantScreen.getComponentAt(evt.getPoint()):"+treGrantScreen.get ComponentAt(evt.getPoint()));;
//            System.out.println("One node is selected");
//            treGrantScreen.getSelectionModel().getSelect
//            System.out.println("treGrantScreen.getSelectionModel().toString():"+treGrantScreen.getSelectionModel().toString());;
            
            TreePath path = treGrantScreen.getSelectionPath();// .getPath();
//            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            ScreenModuleTreeNode node = (ScreenModuleTreeNode) path.getLastPathComponent();
//            System.out.println("node new allowed:"+node.getChkNewAllowed());
//            nodeMap = (HashMap) node.getUserObject();
//            System.out.println ("nodeMap:" + nodeMap);
//            treGrantScreenModel.
            HashMap childMap = new HashMap();
            System.out.println("@@@@ getDepth() : "+node.getDepth());
            System.out.println("@@@@ node : "+node);
            System.out.println("@@@@ oldNode : "+oldNode);
            if (node.getDepth()==0) {
                if (oldNode!=null) {
//                if (!oldNode.equals(node)) {
                    childMap.putAll(oldChildMap);
                    childMap.put("newAllowed",chkNewAllowed.isSelected() ? "Y" : "N");
                    childMap.put("editAllowed",chkEditAllowed.isSelected() ? "Y" : "N");
                    childMap.put("deleteAllowed",chkDeleteAllowed.isSelected() ? "Y" : "N");
                    childMap.put("authRejAllowed",chkAuthRejAllowed.isSelected() ? "Y" : "N");
                    childMap.put("exceptionAllowed",chkExceptionAllowed.isSelected() ? "Y" : "N");
                    childMap.put("printAllowed",chkPrintAllowed.isSelected() ? "Y" : "N");
                    childMap.put("interbranchAllowed",chkInterbranchAllowed.isSelected() ? "Y" : "N");
                    oldNode.setUserObject(childMap);
//                    ((ScreenModuleTreeNode) path.getLastPathComponent()).setUserObject(childMap);
//                    ScreenModuleTreeNode test = (ScreenModuleTreeNode)node.getParent().;
//                    test.getIndex(oldNode);
//                    test.insert(oldNode
//                    System.out.println("@@@@ test.getIndex(oldNode) : "+node.getParent().getIndex(oldNode));
                    System.out.println("@@@@ childMap : "+childMap);
                }
                oldChildMap = (HashMap)node.getUserObject();
                if (oldChildMap!=null) {
                if (oldChildMap.get("newAllowed")!=null) 
                    chkNewAllowed.setSelected(((String)oldChildMap.get("newAllowed")).equals("Y") ? true : false);
                else
                    chkNewAllowed.setSelected(false);
                if (oldChildMap.get("editAllowed")!=null) 
                    chkEditAllowed.setSelected(((String)oldChildMap.get("editAllowed")).equals("Y") ? true : false);
                else
                    chkEditAllowed.setSelected(false);
                if (oldChildMap.get("deleteAllowed")!=null) 
                    chkDeleteAllowed.setSelected(((String)oldChildMap.get("deleteAllowed")).equals("Y") ? true : false);
                else
                    chkDeleteAllowed.setSelected(false);
                if (oldChildMap.get("authRejAllowed")!=null) 
                    chkAuthRejAllowed.setSelected(((String)oldChildMap.get("authRejAllowed")).equals("Y") ? true : false);
                else
                    chkAuthRejAllowed.setSelected(false);
                if (oldChildMap.get("exceptionAllowed")!=null) 
                    chkExceptionAllowed.setSelected(((String)oldChildMap.get("exceptionAllowed")).equals("Y") ? true : false);
                else
                    chkExceptionAllowed.setSelected(false);
                if (oldChildMap.get("printAllowed")!=null) 
                    chkPrintAllowed.setSelected(((String)oldChildMap.get("printAllowed")).equals("Y") ? true : false);
                else
                    chkPrintAllowed.setSelected(false);
                if (oldChildMap.get("interbranchAllowed")!=null) 
                    chkInterbranchAllowed.setSelected(((String)oldChildMap.get("interbranchAllowed")).equals("Y") ? true : false);
                else
                    chkInterbranchAllowed.setSelected(false);
                } else {
                    chkNewAllowed.setSelected(false);
                    chkEditAllowed.setSelected(false);
                    chkDeleteAllowed.setSelected(false);
                    chkAuthRejAllowed.setSelected(false);
                    chkExceptionAllowed.setSelected(false);
                    chkPrintAllowed.setSelected(false);
                    chkInterbranchAllowed.setSelected(false);
                }
                oldNode = node;
                System.out.println("@@@@ oldChildMap : "+oldChildMap);
            }
//            observable.xp();
        }else if (treGrantScreen.getSelectionCount()>1){
//            System.out.println("More than one node is selected");
//            observable.xp();
        }
    }//GEN-LAST:event_treGrantScreenMouseClicked

    private void chkPrintAllowedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPrintAllowedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkPrintAllowedActionPerformed

    private void chkExceptionAllowedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkExceptionAllowedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkExceptionAllowedActionPerformed

    private void chkAuthRejAllowedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAuthRejAllowedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkAuthRejAllowedActionPerformed

    private void chkDeleteAllowedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDeleteAllowedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkDeleteAllowedActionPerformed

    private void chkEditAllowedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkEditAllowedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkEditAllowedActionPerformed

    private void chkNewAllowedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNewAllowedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkNewAllowedActionPerformed
    
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
       observable.setStatus(); 
         setModified(true);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnExcludeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcludeActionPerformed
        // Add your handling code here:
        observable.includeExclude(treGrantScreen,treAvaiScreen);
        includeExcludeButton((DefaultTreeModel)treAvaiScreen.getModel(),(DefaultTreeModel)treGrantScreen.getModel());
    }//GEN-LAST:event_btnExcludeActionPerformed
    
    private void btnIncludeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncludeActionPerformed
        // Add your handling code here:        
        updateOBFieldsRights();
//        observable.notifyObservers();
        observable.includeExclude(treAvaiScreen,treGrantScreen);
        includeExcludeButton((DefaultTreeModel)treAvaiScreen.getModel(),(DefaultTreeModel)treGrantScreen.getModel());
    }//GEN-LAST:event_btnIncludeActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        oldChildMap=null;
        oldNode = null;
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        setModified(false);
        observable.resetForm();
        oldChildMap=null;
        oldNode = null;
        setUp(ClientConstants.ACTIONTYPE_CANCEL,false);
        viewType = "";
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        screenSelChk();
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
            oldChildMap=null;
            oldNode = null;
            setUp(ClientConstants.ACTIONTYPE_CANCEL, false);
            observable.setResultStatus();
        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    
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
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        setUp(ClientConstants.ACTIONTYPE_EDIT,true);
        callView(ClientConstants.ACTIONTYPE_EDIT);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:        
        setModified(true);
        setUp(ClientConstants.ACTIONTYPE_NEW,true);        
        callView(ClientConstants.ACTIONTYPE_NEW);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
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
        String mapName = "selectGroupMaster";
        
        if (currField == ClientConstants.ACTIONTYPE_NEW || branchConfig){
            branchGrp = objCommonRB.getString("YES"); 
            mapName = "selectBranchGroupMaster";            
        }               
        whereMap.put("BRANCH_GROUP", branchGrp);
        observable.setActionType(currField);
        viewMap.put(CommonConstants.MAP_NAME, mapName);
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this, viewMap).show();
    }
    
    public void fillData(Object  map) {
        setModified(true);
        HashMap hash = (HashMap) map;
          if (viewType != null) {
          int action = observable.getActionType();
        if (action == ClientConstants.ACTIONTYPE_EDIT ||
        action == ClientConstants.ACTIONTYPE_DELETE || viewType.equals(AUTHORIZE)||
        action == ClientConstants.ACTIONTYPE_VIEW) {
           hash.put(CommonConstants.MAP_WHERE, hash.get("GROUP_ID"));
            observable.populateData(hash);
            if(viewType.equals(AUTHORIZE)|| action == ClientConstants.ACTIONTYPE_VIEW){
                enableDisableButtons(false);
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
        }else if (action == ClientConstants.ACTIONTYPE_NEW){
            txtBranchGroup.setText(hash.get("GROUP_ID").toString());
            observable.setTxtBranchGroup(txtBranchGroup.getText());
            observable.setAvailableScreen(observable.getAllAvailableScreen());
            update(null, null);
            updateOBFields();
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
//        if (actionType == ClientConstants.ACTIONTYPE_EDIT){
//            editableFields();
//        }
        enableDisableButtons(isEnable);       
        observable.setActionType(actionType);
        observable.setStatus();        
        txtBranchGroup.setEditable(false); 
        txtBranchGroup.setEnabled(true); 
        txtGrpID.setEditable(false);
        chkAuthRejAllowed.setSelected(false);
        chkDeleteAllowed.setSelected(false);
        chkEditAllowed.setSelected(false);
        chkExceptionAllowed.setSelected(false);
        chkInterbranchAllowed.setSelected(false);
        chkNewAllowed.setSelected(false);
        chkPrintAllowed.setSelected(false);
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
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CPanel cPanel3;
    private com.see.truetransact.uicomponent.CSeparator cSeparator1;
    private com.see.truetransact.uicomponent.CCheckBox chkAuthRejAllowed;
    private com.see.truetransact.uicomponent.CCheckBox chkDeleteAllowed;
    private com.see.truetransact.uicomponent.CCheckBox chkEditAllowed;
    private com.see.truetransact.uicomponent.CCheckBox chkExceptionAllowed;
    private com.see.truetransact.uicomponent.CCheckBox chkInterbranchAllowed;
    private com.see.truetransact.uicomponent.CCheckBox chkNewAllowed;
    private com.see.truetransact.uicomponent.CCheckBox chkPrintAllowed;
    private com.see.truetransact.uicomponent.CLabel lblAvaScreen;
    private com.see.truetransact.uicomponent.CLabel lblBranchGroup;
    private com.see.truetransact.uicomponent.CLabel lblGrantScreen;
    private com.see.truetransact.uicomponent.CLabel lblGrpDesc;
    private com.see.truetransact.uicomponent.CLabel lblGrpID;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
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
    private com.see.truetransact.uicomponent.CTextField txtBranchGroup;
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
        GroupUI gui = new GroupUI(true);
        jf.getContentPane().add(gui);
        jf.setSize(650, 660);
        jf.show();
        gui.show();
    }
}
