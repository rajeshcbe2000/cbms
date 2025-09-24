/*
* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * frmSale.java
 *
 * Created on September 12, 2011, 12:08 PM
 */

package com.see.truetransact.ui.indend;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import java.text.*;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import java.util.Observable;
import java.math.BigDecimal;
import com.see.truetransact.clientutil.ClientUtil;
import java.util.Observer;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.ui.common.viewall.ViewAll;
import java.util.ResourceBundle;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import java.util.Date;
/**
 *
 * @author  userdd
 */
public class frmSale extends CInternalFrame implements Observer, UIMandatoryField{
    
    private SalesOB observable;
    private String[][] tabledata;
    private String[] column;
    private SaleMRB objMandatoryRB = new SaleMRB();//Instance for the MandatoryResourceBundle
    private DefaultTableModel model;
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private final String AUTHORIZE="Authorize";//Variable used when btnAuthorize is clicked
    private Date currDt = null;
    /** Creates new form ifrNewBorrowing */
    public frmSale() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setFieldNames();
        setObservable();
        observable.resetForm();
        setMaxLengths();
        //   observable = new SalesOB();
        //     initComponentData();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panSale, getMandatoryHashMap());
        ClientUtil.enableDisable(panSale, false);
        setButtonEnableDisable();
        txtSecAmt.setText("0.00");
        txtSalesmanID.setEnabled(false);
        
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
        //   lblMsg.setName("lblMsg");
        tbrSale.setName("tbrSale");
        lblSalesmanId.setName("lblSalesmanId");
        lblName.setName("lblName");
        lblAddress.setName("lblAddress");
        lblSecurityAmt.setName("lblSecurityAmt");
        lblRemarks.setName("lblRemarks");
        
        txtSalesmanID.setName("txtSalesmanID");
        txtName.setName("txtName");
        txaArea.setName("txaArea");
        txtSecAmt.setName("txtSecAmt");
        txaRemarks.setName("txaRemarks");
        
    }
    private void setMaxLengths() {
        txtSecAmt.setValidation(new CurrencyValidation());
    }
    private void setObservable() {
        try{
            observable = SalesOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            //parseException.logException(e,true);
            System.out.println("Error in setObservable():"+e);
        }
    }
    
    
    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
        viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
        viewType.equals(ClientConstants.ACTION_STATUS[17])) {
            viewMap.put(CommonConstants.MAP_NAME, "Sale.getSelectSaleList");
            ArrayList lst = new ArrayList();
            lst.add("SMID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
        }
        
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        viewMap.put(CommonConstants.MAP_WHERE, where);
        where = null;
        new ViewAll(this,viewMap).show();
        
    }
    public void fillData(Object  map) {
        
        //        setModified(true);
        HashMap hash = (HashMap) map;
        
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                HashMap where = new HashMap();
                //System.out.println("hash.get"+hash.get("BORROWING_NO"));
                where.put("SMID", hash.get("SMID"));
                // where.put(CommonConstants.BRANCH_ID, "0001");
                hash.put(CommonConstants.MAP_WHERE, where);
                observable.populateData(hash);
                
                // fillTxtNoOfTokens();
                if (observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW) {
                    ClientUtil.enableDisable(panSale, false);
                }else  if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                    ClientUtil.enableDisable(panSale, true);
                }
                if(viewType.equals(AUTHORIZE)){
                    ClientUtil.enableDisable(panSale, false);
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
                setButtonEnableDisable();
            }
        }
        txtSalesmanID.setEnabled(false);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrSale = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace51 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace52 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace53 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace54 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace55 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panSale = new com.see.truetransact.uicomponent.CPanel();
        lblName = new com.see.truetransact.uicomponent.CLabel();
        txtSalesmanID = new com.see.truetransact.uicomponent.CTextField();
        txtName = new com.see.truetransact.uicomponent.CTextField();
        lblSalesmanId = new com.see.truetransact.uicomponent.CLabel();
        lblAddress = new com.see.truetransact.uicomponent.CLabel();
        lblSecurityAmt = new com.see.truetransact.uicomponent.CLabel();
        txtSecAmt = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        txaArea = new com.see.truetransact.uicomponent.CTextArea();
        cScrollPane2 = new com.see.truetransact.uicomponent.CScrollPane();
        txaRemarks = new com.see.truetransact.uicomponent.CTextArea();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrTokenConfig = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptView = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        tbrSale.setMaximumSize(new java.awt.Dimension(700, 29));
        tbrSale.setMinimumSize(new java.awt.Dimension(700, 29));
        tbrSale.setPreferredSize(new java.awt.Dimension(700, 29));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
        tbrSale.add(btnView);

        lbSpace3.setText("     ");
        tbrSale.add(lbSpace3);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrSale.add(btnNew);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSale.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrSale.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSale.add(lblSpace52);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrSale.add(btnDelete);

        lbSpace2.setText("     ");
        tbrSale.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrSale.add(btnSave);

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSale.add(lblSpace53);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrSale.add(btnCancel);

        lblSpace3.setText("     ");
        tbrSale.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrSale.add(btnAuthorize);

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSale.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrSale.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSale.add(lblSpace55);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrSale.add(btnReject);

        lblSpace5.setText("     ");
        tbrSale.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrSale.add(btnPrint);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSale.add(lblSpace56);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrSale.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(tbrSale, gridBagConstraints);

        panSale.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSale.setMaximumSize(new java.awt.Dimension(400, 300));
        panSale.setMinimumSize(new java.awt.Dimension(400, 300));
        panSale.setPreferredSize(new java.awt.Dimension(400, 300));
        panSale.setLayout(new java.awt.GridBagLayout());

        lblName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 74;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panSale.add(lblName, gridBagConstraints);

        txtSalesmanID.setEnabled(false);
        txtSalesmanID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSalesmanIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 25);
        panSale.add(txtSalesmanID, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 25);
        panSale.add(txtName, gridBagConstraints);

        lblSalesmanId.setText("Salesman ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 34;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panSale.add(lblSalesmanId, gridBagConstraints);

        lblAddress.setText("Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 61;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panSale.add(lblAddress, gridBagConstraints);

        lblSecurityAmt.setText("Security Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 34;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panSale.add(lblSecurityAmt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 25);
        panSale.add(txtSecAmt, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 58;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panSale.add(lblRemarks, gridBagConstraints);

        cScrollPane1.setViewportView(txaArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 157;
        gridBagConstraints.ipady = 60;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 3, 4);
        panSale.add(cScrollPane1, gridBagConstraints);

        cScrollPane2.setViewportView(txaRemarks);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 157;
        gridBagConstraints.ipady = 56;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 25);
        panSale.add(cScrollPane2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(panSale, gridBagConstraints);

        panStatus.setMinimumSize(new java.awt.Dimension(250, 22));
        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        panStatus.add(lblStatus, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(panStatus, gridBagConstraints);

        mbrTokenConfig.setInheritsPopupMenu(true);

        mnuProcess.setText("Sale");
        mnuProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProcessActionPerformed(evt);
            }
        });

        mitNew.setText("New");
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptView);

        mitSave.setText("Save");
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mnuProcess.add(mitClose);

        mbrTokenConfig.add(mnuProcess);

        setJMenuBar(mbrTokenConfig);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void txtSalesmanIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSalesmanIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSalesmanIDActionPerformed
    
    
    public static void main(String args[]) throws Exception {
        try {
            frmSale objIfrRenewal=new frmSale();
            objIfrRenewal.setVisible(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        txtSalesmanID.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        txtSalesmanID.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        txtSalesmanID.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            //            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID,TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getSaleAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeSale");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            //AuthorizeStatusUI fdf=new AuthorizeStatusUI(this,mapParam);
            authorizeUI.show();
            observable.setStatus();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID,ProxyParameters.USER_ID);//commented on 13-Feb-2012
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("SMID", observable.getSMNo());
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
          //  System.out.println("IOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            ClientUtil.execute("authorizeSale", singleAuthorizeMap);
            // ClientUtil.ex
            viewType = "";
            //            super.setOpenForEditBy(observable.getStatusBy());
            //            super.removeEditLock(txtTokenConfigId.getText());
            btnCancelActionPerformed(null);
            //  lblStatus.setText(authorizeStatus);
        }
        txtSalesmanID.setEnabled(false);
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        //txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panSale, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        txtSalesmanID.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        //   System.out.println("IN btnSaveActionPerformed");
        savePerformed();
        //    System.out.println("IN btnSaveActionPerformed111");
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        txtSalesmanID.setEnabled(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed(){
        
        // System.out.println("IN savePerformed");
        String action;
        //    System.out.println("IN observable.getActionType(): "+observable.getActionType());
        //      System.out.println("IN ClientConstants.ACTIONTYPE_NEW: "+ClientConstants.ACTIONTYPE_NEW);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ){
            //     System.out.println("IN savePerformed ACTIONTYPE_NEW");
            
            action=CommonConstants.TOSTATUS_INSERT;
            saveAction(action);
            
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            action=CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            action=CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
        txtSalesmanID.setEnabled(false);
    }
    public boolean checkNumber(String value) {
        //String amtRentIn=amountRentText.getText();
        boolean incorrect = true;
        try {
            Double.parseDouble(value);
            return true;
        }
        catch(NumberFormatException nfe) {
            return false;
        }
        // return
    }
    
    /* Calls the execute method of TerminalOB to do insertion or updation or deletion */
    private void saveAction(String status){
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        //  System.out.println("status saveAction11111: "+status);
        //       txtAmtBorrowed.
        final String mandatoryMessage = checkMandatory(panSale);
        StringBuffer message = new StringBuffer(mandatoryMessage);
      /*  if(txtSalesmanID.getText().equals(""))
        {
            message.append(objMandatoryRB.getString("txtSalesmanID"));
        }*/
        if(txtName.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtName"));
        }
        /* if(txaArea.getText().equals(""))
        {
            message.append(objMandatoryRB.getString("txaArea"));
        }
        if(txtSecAmt.getText().equals("Purchase"))
        {
            
            message.append(objMandatoryRB.getString("txtSecAmt"));
            
        }
        if(txaRemarks.getText().equals(""))
        {
            message.append(objMandatoryRB.getString("txaRemarks"));
         }*/
        
        //setExpDateOnCalculation();
        //  System.out.println("status saveAction: "+status);
        if(message.length() > 0 ){
            displayAlert(message.toString());
        }else{
            updateOBFields();
            observable.execute(status);
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("SMID");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                //   if (observable.getProxyReturnMap()!=null) {
                //      if (observable.getProxyReturnMap().containsKey("CONFIG_ID")) {
                //          lockMap.put("CONFIG_ID", observable.getProxyReturnMap().get("CONFIG_ID"));
                //      }
                //  }
                if (status==CommonConstants.TOSTATUS_UPDATE) {
                    lockMap.put("SMID", observable.getSMNo());
                }
                //      setEditLockMap(lockMap);
                // setEditLock();
                settings();
            }
        }
        
    }
    /** Method used to check whether the Mandatory Fields in the Form are Filled or not */
    private String checkMandatory(javax.swing.JComponent component){
        //  return new MandatoryCheck().checkMandatory(getClass().getName(), component, getMandatoryHashMap());
        return "";
        //validation error
    }
    
    /** Method used to Give a Alert when any Mandatory Field is not filled by the user */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetForm();
        //  txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panSale, false);
        setButtonEnableDisable();
        observable.setResultStatus();
        txtSalesmanID.setEnabled(false);
    }
    public Date getDateValue(String date1) {
        DateFormat formatter ;
        Date date=null ;
        try {
            System.out.println("date1 66666666666=========:"+date1);
            // String str_date=date1;
            //  formatter = new SimpleDateFormat("MM/dd/yyyy");
            //  date = (Date)formatter.parse(str_date);
            //      System.out.println("dateAFETRRR 66666666666=========:"+date);
            
            
            
            
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
            // String s1 = "2008-03-30T15:30:00.000+02:00";
            // date1 = date1.substring(0, date1.indexOf('.'));
            System.out.println("Result==> "+sdf2.format(sdf1.parse(date1)));
            date=new Date(sdf2.format(sdf1.parse(date1)));
            System.out.println("date IOOOOOOO==> "+date);
        }
        catch (ParseException e) {
            System.out.println("Error in getDateValue():"+e);
        }
        return date;
    }
    public void updateOBFields() {
        
        observable.setSMNo(txtSalesmanID.getText());
        observable.setTxaArea(txaArea.getText());
        observable.setTxtName(txtName.getText());
        if(txtSecAmt.getText()==null || txtSecAmt.getText().equals("")) {
            observable.setTxtSecAmt(Double.valueOf(0));
        }
        else {
            observable.setTxtSecAmt(Double.valueOf(txtSecAmt.getText()));
        }
        observable.setTxaRemarks(txaRemarks.getText());
        
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        txtSalesmanID.setEnabled(false);
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView( ClientConstants.ACTION_STATUS[3]);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        txtSalesmanID.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        txtSalesmanID.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        //txtNoOfTokens.setText("");
        ClientUtil.enableDisable(panSale, true);
        
        // System.out.println("btnNewActionPerformed ACTION STA: "+ClientConstants.ACTIONTYPE_NEW);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
        txtSalesmanID.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnSave.setEnabled(!btnSave.isEnabled());
        btnCancel.setEnabled(!btnCancel.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        //  lblStatus.setText(observable.getLblStatus());
        btnView.setEnabled(!btnView.isEnabled());
        txtSalesmanID.setEnabled(false);
        
        
    }
    private void mnuProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuProcessActionPerformed
    
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtName", new Boolean(true));
       // mandatoryMap.put("tdtDateIndand", new Boolean(true));
        //  mandatoryMap.put("txtBorrowingNo", new Boolean(true));
    //    mandatoryMap.put("cboTransType", new Boolean(true));
    //    mandatoryMap.put("txtPurAmount", new Boolean(true));
    //    mandatoryMap.put("txtAmount", new Boolean(true));
        
        
    }
    public java.util.HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    public String getDtPrintValue(String strDate) {
        try {
        //     System.out.println("strDate getDtPrintValue99999999999999999999999999999999999999================="+strDate);
            //create SimpleDateFormat object with source string date format
            SimpleDateFormat sdfSource = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            //parse the string into Date object
            Date date = sdfSource.parse(strDate);
            //create SimpleDateFormat object with desired date format
            SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
            //parse the date into another format
            strDate = sdfDestination.format(date);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }
   /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtSalesmanID.setText(observable.getSMNo());
        txtName.setText(observable.getTxtName());
        txaArea.setText(observable.getTxaArea());
        if(observable.getTxtSecAmt()!=null)
            txtSecAmt.setText(String.valueOf(observable.getTxtSecAmt()));
        txaRemarks.setText(observable.getTxaRemarks());
        
    }
    
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
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane2;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblAddress;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSalesmanId;
    private com.see.truetransact.uicomponent.CLabel lblSecurityAmt;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace52;
    private com.see.truetransact.uicomponent.CLabel lblSpace53;
    private com.see.truetransact.uicomponent.CLabel lblSpace54;
    private com.see.truetransact.uicomponent.CLabel lblSpace55;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panSale;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CToolBar tbrSale;
    private com.see.truetransact.uicomponent.CTextArea txaArea;
    private com.see.truetransact.uicomponent.CTextArea txaRemarks;
    private com.see.truetransact.uicomponent.CTextField txtName;
    private com.see.truetransact.uicomponent.CTextField txtSalesmanID;
    private com.see.truetransact.uicomponent.CTextField txtSecAmt;
    // End of variables declaration//GEN-END:variables
    //     private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.clientutil.TableModel tbModel;
}
