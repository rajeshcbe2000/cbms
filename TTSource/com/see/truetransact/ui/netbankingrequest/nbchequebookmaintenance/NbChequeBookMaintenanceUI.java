
/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * NbChequeBookMaintenanceUI.java
 */

package com.see.truetransact.ui.netbankingrequest.nbchequebookmaintenance;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
//import com.see.truetransact.ui.deposit.interestapplication.LienDetailsUI;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
    public class NbChequeBookMaintenanceUI extends CInternalFrame implements Observer {
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    NbChequeBookMaintenanceOB observable = null;
    //int viewType = -1;
    private boolean selectMode = false;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.netbankingrequest.nbchequebookmaintenance.NbChequeBookMaintenanceRB", ProxyParameters.LANGUAGE);
    HashMap mandatoryMap;
    private boolean updateMode = false;
    int updateTab=-1; 
    ArrayList list = new ArrayList();
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final String AUTHORIZE="Authorize";//Variable used when btnAuthorize is clicked
    //HashMap tblTOMap = new HashMap();

    
   //required 
    public NbChequeBookMaintenanceUI() {
        initComponents();
        initForm();
    }
    //required
    private void initForm() {
        setFieldNames();
        internationalize();
        setMandatoryMap();
        setButtonEnableDisable();
        observable = new NbChequeBookMaintenanceOB();
        initTableData();
        ClientUtil.enableDisable(panCbRequest, true);
        
    }
    
    //required
     private void initTableData() {
        tblCbRequest.setModel(observable.getTblCbRequest());
    }
     
     private void panCbRequestEnable(){
         ClientUtil.enableDisable(panCbRequest, true);
     }
     
     private void panCbStopEnable(){
         ClientUtil.enableDisable(panCbStop, true);
     }
     
     private void panCbRevokeEnable(){
         ClientUtil.enableDisable(panCbRevoke, true);
     }
     
    public HashMap getMandatoryMap() {
        return mandatoryMap;
    }

    public void setMandatoryMap() {
        mandatoryMap = new HashMap();
           mandatoryMap.put("lblAccno", new Boolean(true));
           mandatoryMap.put("lblAccno2", new Boolean(true));
           mandatoryMap.put("lblCustid", new Boolean(true));
           mandatoryMap.put("lblCustid2", new Boolean(true));
           mandatoryMap.put("lblCbRequest", new Boolean(true));
           mandatoryMap.put("lblCbRequest2", new Boolean(true));
           mandatoryMap.put("lblNoOfCbLeave", new Boolean(true));
           mandatoryMap.put("lblNoOfCbLeave2", new Boolean(true));
           mandatoryMap.put("lblUsageType", new Boolean(true));
           mandatoryMap.put("lblUsageType2", new Boolean(true));
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
            btnView.setEnabled(!btnView.isEnabled());
        }

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

           lblCustid.setName("lblCustid");
           lblCustid2.setName("lblCustid2");
           lblAccNo.setName("lblAccNo");
           lblAccNo2.setName("lblAccNo2");
           lblCbRequest.setName("lblCbRequest");
           lblCbRequest2.setName("lblCbRequest2");
           lblNoOfCbLeave.setName("lblNoOfCbLeave");
           lblNoOfCbLeave2.setName("lblNoOfCbLeave2");
           lblUsageType.setName("lblUsageType");
           lblUsageType2.setName("lblUsageType2");
       }
       
       private void internationalize() {
           btnSave.setText(resourceBundle.getString("btnSave"));
           btnReject.setText(resourceBundle.getString("btnReject"));
           btnPrint.setText(resourceBundle.getString("btnPrint"));
           btnNew.setText(resourceBundle.getString("btnNew"));
           btnException.setText(resourceBundle.getString("btnException"));
           btnEdit.setText(resourceBundle.getString("btnEdit"));
           btnDelete.setText(resourceBundle.getString("btnDelete"));
           btnClose.setText(resourceBundle.getString("btnClose"));
           btnCancel.setText(resourceBundle.getString("btnCancel"));
           btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
           
           lblCustid.setText(resourceBundle.getString("lblCustid"));
           lblCustid2.setText(resourceBundle.getString("lblCustid2"));
           lblAccNo.setText(resourceBundle.getString("lblAccNo"));
           lblAccNo2.setText(resourceBundle.getString("lblAccNo2"));
           lblCbRequest.setText(resourceBundle.getString("lblCbRequest"));
           lblCbRequest2.setText(resourceBundle.getString("lblCbRequest2"));          
           lblNoOfCbLeave.setText(resourceBundle.getString("lblNoOfCbLeave"));
           lblNoOfCbLeave2.setText(resourceBundle.getString("lblNoOfCbLeave2"));
           lblUsageType.setText(resourceBundle.getString("lblUsageType"));
           lblUsageType2.setText(resourceBundle.getString("lblUsageType2"));
       }
    
       private void btnCheck(){
        btnCancel.setEnabled(true);
        btnClose.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(true);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }
       
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panCbMaintenance = new com.see.truetransact.uicomponent.CPanel();
        tabCbMaintenance = new com.see.truetransact.uicomponent.CTabbedPane();
        panCbRequest = new com.see.truetransact.uicomponent.CPanel();
        panProductData = new com.see.truetransact.uicomponent.CPanel();
        lblCustid = new com.see.truetransact.uicomponent.CLabel();
        lblCbRequest = new com.see.truetransact.uicomponent.CLabel();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfCbLeave = new com.see.truetransact.uicomponent.CLabel();
        lblCustid2 = new javax.swing.JLabel();
        lblCbRequest2 = new javax.swing.JLabel();
        lblAccNo2 = new javax.swing.JLabel();
        lblNoOfCbLeave2 = new javax.swing.JLabel();
        lblUsageType = new com.see.truetransact.uicomponent.CLabel();
        lblUsageType2 = new javax.swing.JLabel();
        srpCbRequest = new com.see.truetransact.uicomponent.CScrollPane();
        tblCbRequest = new com.see.truetransact.uicomponent.CTable();
        panSelectAll = new com.see.truetransact.uicomponent.CPanel();
        lblSelectAll = new com.see.truetransact.uicomponent.CLabel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        lblNbTransId = new com.see.truetransact.uicomponent.CLabel();
        lblNbTransId1 = new javax.swing.JLabel();
        panCbStop = new com.see.truetransact.uicomponent.CPanel();
        panProductDataP = new com.see.truetransact.uicomponent.CPanel();
        lblCustidp = new com.see.truetransact.uicomponent.CLabel();
        lblAccnop = new com.see.truetransact.uicomponent.CLabel();
        lblCustomernamep = new com.see.truetransact.uicomponent.CLabel();
        lblAddressp = new com.see.truetransact.uicomponent.CLabel();
        lblCustid3 = new javax.swing.JLabel();
        lblAccno3 = new javax.swing.JLabel();
        lblCustomername3 = new javax.swing.JLabel();
        lblAddress3 = new javax.swing.JLabel();
        lblPanno = new javax.swing.JLabel();
        lblPanno2 = new javax.swing.JLabel();
        srpProductDetailsP = new com.see.truetransact.uicomponent.CScrollPane();
        tblPanCardDetails = new com.see.truetransact.uicomponent.CTable();
        panSelectAllP = new com.see.truetransact.uicomponent.CPanel();
        lblSelectAll1 = new com.see.truetransact.uicomponent.CLabel();
        chkSelectAll1 = new com.see.truetransact.uicomponent.CCheckBox();
        panCbRevoke = new com.see.truetransact.uicomponent.CPanel();
        panProductDataP1 = new com.see.truetransact.uicomponent.CPanel();
        lblCustidp1 = new com.see.truetransact.uicomponent.CLabel();
        lblAccnop1 = new com.see.truetransact.uicomponent.CLabel();
        lblCustomernamep1 = new com.see.truetransact.uicomponent.CLabel();
        lblAddressp1 = new com.see.truetransact.uicomponent.CLabel();
        lblCustid4 = new javax.swing.JLabel();
        lblAccno4 = new javax.swing.JLabel();
        lblCustomername4 = new javax.swing.JLabel();
        lblAddress4 = new javax.swing.JLabel();
        lblPanno1 = new javax.swing.JLabel();
        lblPanno3 = new javax.swing.JLabel();
        srpProductDetailsP1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblPanCardDetails1 = new com.see.truetransact.uicomponent.CTable();
        panSelectAllP1 = new com.see.truetransact.uicomponent.CPanel();
        lblSelectAll2 = new com.see.truetransact.uicomponent.CLabel();
        chkSelectAll2 = new com.see.truetransact.uicomponent.CCheckBox();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrCbMaintenance = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace46 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace47 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace48 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace49 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace50 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace51 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        mbrCbMaintenance = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMinimumSize(new java.awt.Dimension(800, 675));
        setPreferredSize(new java.awt.Dimension(800, 675));

        panCbMaintenance.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panCbMaintenance.setLayout(new java.awt.GridBagLayout());

        panCbRequest.setLayout(new java.awt.GridBagLayout());

        panProductData.setForeground(new java.awt.Color(102, 0, 255));
        panProductData.setMaximumSize(new java.awt.Dimension(600, 120));
        panProductData.setMinimumSize(new java.awt.Dimension(600, 120));
        panProductData.setPreferredSize(new java.awt.Dimension(600, 120));
        panProductData.setLayout(new java.awt.GridBagLayout());

        lblCustid.setText("Customer Id : ");
        lblCustid.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblCustid, gridBagConstraints);

        lblCbRequest.setText("CB Requset : ");
        lblCbRequest.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblCbRequest, gridBagConstraints);

        lblAccNo.setText("Account Number : ");
        lblAccNo.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblAccNo, gridBagConstraints);

        lblNoOfCbLeave.setText("No Of CB Leave : ");
        lblNoOfCbLeave.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblNoOfCbLeave, gridBagConstraints);

        lblCustid2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblCustid2.setForeground(new java.awt.Color(102, 0, 255));
        lblCustid2.setMaximumSize(new java.awt.Dimension(97, 18));
        lblCustid2.setMinimumSize(new java.awt.Dimension(97, 18));
        lblCustid2.setPreferredSize(new java.awt.Dimension(97, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panProductData.add(lblCustid2, gridBagConstraints);
        lblCustid2.getAccessibleContext().setAccessibleName("JLabel");

        lblCbRequest2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblCbRequest2.setForeground(new java.awt.Color(102, 0, 255));
        lblCbRequest2.setMaximumSize(new java.awt.Dimension(97, 18));
        lblCbRequest2.setMinimumSize(new java.awt.Dimension(97, 18));
        lblCbRequest2.setPreferredSize(new java.awt.Dimension(97, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panProductData.add(lblCbRequest2, gridBagConstraints);

        lblAccNo2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblAccNo2.setForeground(new java.awt.Color(102, 0, 255));
        lblAccNo2.setMaximumSize(new java.awt.Dimension(97, 18));
        lblAccNo2.setMinimumSize(new java.awt.Dimension(97, 18));
        lblAccNo2.setPreferredSize(new java.awt.Dimension(97, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panProductData.add(lblAccNo2, gridBagConstraints);

        lblNoOfCbLeave2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblNoOfCbLeave2.setForeground(new java.awt.Color(102, 0, 255));
        lblNoOfCbLeave2.setMaximumSize(new java.awt.Dimension(180, 18));
        lblNoOfCbLeave2.setMinimumSize(new java.awt.Dimension(180, 18));
        lblNoOfCbLeave2.setPreferredSize(new java.awt.Dimension(180, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panProductData.add(lblNoOfCbLeave2, gridBagConstraints);

        lblUsageType.setText("Usage Type : ");
        lblUsageType.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblUsageType, gridBagConstraints);

        lblUsageType2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblUsageType2.setForeground(new java.awt.Color(102, 0, 255));
        lblUsageType2.setMaximumSize(new java.awt.Dimension(97, 18));
        lblUsageType2.setMinimumSize(new java.awt.Dimension(97, 18));
        lblUsageType2.setPreferredSize(new java.awt.Dimension(97, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        panProductData.add(lblUsageType2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panCbRequest.add(panProductData, gridBagConstraints);

        srpCbRequest.setMaximumSize(new java.awt.Dimension(600, 290));
        srpCbRequest.setMinimumSize(new java.awt.Dimension(600, 290));
        srpCbRequest.setPreferredSize(new java.awt.Dimension(600, 290));
        srpCbRequest.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                srpCbRequestMouseClicked(evt);
            }
        });

        tblCbRequest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Customer Id", "Account Number", "CB Request", "No Of CB Leave", "Usage Type"
            }
        ));
        tblCbRequest.setPreferredScrollableViewportSize(new java.awt.Dimension(415, 140));
        tblCbRequest.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCbRequestMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblCbRequestMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCbRequestMousePressed(evt);
            }
        });
        srpCbRequest.setViewportView(tblCbRequest);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panCbRequest.add(srpCbRequest, gridBagConstraints);

        panSelectAll.setMaximumSize(new java.awt.Dimension(500, 27));
        panSelectAll.setMinimumSize(new java.awt.Dimension(500, 27));
        panSelectAll.setPreferredSize(new java.awt.Dimension(500, 27));
        panSelectAll.setLayout(new java.awt.GridBagLayout());

        lblSelectAll.setText("Select All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 19, 5, 0);
        panSelectAll.add(lblSelectAll, gridBagConstraints);

        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 5, 1);
        panSelectAll.add(chkSelectAll, gridBagConstraints);

        lblNbTransId.setText("NB TRANS ID : ");
        lblNbTransId.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSelectAll.add(lblNbTransId, gridBagConstraints);

        lblNbTransId1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblNbTransId1.setForeground(new java.awt.Color(102, 0, 255));
        lblNbTransId1.setMaximumSize(new java.awt.Dimension(97, 18));
        lblNbTransId1.setMinimumSize(new java.awt.Dimension(97, 18));
        lblNbTransId1.setPreferredSize(new java.awt.Dimension(97, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        panSelectAll.add(lblNbTransId1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panCbRequest.add(panSelectAll, gridBagConstraints);

        tabCbMaintenance.addTab("CB Request", panCbRequest);

        panCbStop.setLayout(new java.awt.GridBagLayout());

        panProductDataP.setForeground(new java.awt.Color(102, 0, 255));
        panProductDataP.setMaximumSize(new java.awt.Dimension(600, 120));
        panProductDataP.setMinimumSize(new java.awt.Dimension(600, 120));
        panProductDataP.setPreferredSize(new java.awt.Dimension(600, 120));
        panProductDataP.setLayout(new java.awt.GridBagLayout());

        lblCustidp.setText("Customer Id : ");
        lblCustidp.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductDataP.add(lblCustidp, gridBagConstraints);

        lblAccnop.setText("Account Number : ");
        lblAccnop.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductDataP.add(lblAccnop, gridBagConstraints);

        lblCustomernamep.setText("Customer Name : ");
        lblCustomernamep.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductDataP.add(lblCustomernamep, gridBagConstraints);

        lblAddressp.setText("Address : ");
        lblAddressp.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductDataP.add(lblAddressp, gridBagConstraints);

        lblCustid3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblCustid3.setForeground(new java.awt.Color(102, 0, 255));
        lblCustid3.setMaximumSize(new java.awt.Dimension(97, 18));
        lblCustid3.setMinimumSize(new java.awt.Dimension(97, 18));
        lblCustid3.setPreferredSize(new java.awt.Dimension(97, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panProductDataP.add(lblCustid3, gridBagConstraints);

        lblAccno3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblAccno3.setForeground(new java.awt.Color(102, 0, 255));
        lblAccno3.setMaximumSize(new java.awt.Dimension(97, 18));
        lblAccno3.setMinimumSize(new java.awt.Dimension(97, 18));
        lblAccno3.setPreferredSize(new java.awt.Dimension(97, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panProductDataP.add(lblAccno3, gridBagConstraints);

        lblCustomername3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblCustomername3.setForeground(new java.awt.Color(102, 0, 255));
        lblCustomername3.setMaximumSize(new java.awt.Dimension(97, 18));
        lblCustomername3.setMinimumSize(new java.awt.Dimension(97, 18));
        lblCustomername3.setPreferredSize(new java.awt.Dimension(97, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panProductDataP.add(lblCustomername3, gridBagConstraints);

        lblAddress3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblAddress3.setForeground(new java.awt.Color(102, 0, 255));
        lblAddress3.setMaximumSize(new java.awt.Dimension(180, 18));
        lblAddress3.setMinimumSize(new java.awt.Dimension(180, 18));
        lblAddress3.setPreferredSize(new java.awt.Dimension(180, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panProductDataP.add(lblAddress3, gridBagConstraints);

        lblPanno.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblPanno.setText("PanCard Number : ");
        lblPanno.setMaximumSize(new java.awt.Dimension(117, 18));
        lblPanno.setMinimumSize(new java.awt.Dimension(117, 18));
        lblPanno.setPreferredSize(new java.awt.Dimension(117, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductDataP.add(lblPanno, gridBagConstraints);

        lblPanno2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblPanno2.setForeground(new java.awt.Color(102, 0, 255));
        lblPanno2.setMaximumSize(new java.awt.Dimension(97, 18));
        lblPanno2.setMinimumSize(new java.awt.Dimension(97, 18));
        lblPanno2.setPreferredSize(new java.awt.Dimension(97, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.ipady = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panProductDataP.add(lblPanno2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panCbStop.add(panProductDataP, gridBagConstraints);

        srpProductDetailsP.setMaximumSize(new java.awt.Dimension(600, 290));
        srpProductDetailsP.setMinimumSize(new java.awt.Dimension(600, 290));
        srpProductDetailsP.setPreferredSize(new java.awt.Dimension(600, 290));

        tblPanCardDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Customer Id", "Account Number", "Customer Name", "Address", "Pan No"
            }
        ));
        tblPanCardDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(415, 140));
        tblPanCardDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPanCardDetailsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblPanCardDetailsMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPanCardDetailsMousePressed(evt);
            }
        });
        srpProductDetailsP.setViewportView(tblPanCardDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panCbStop.add(srpProductDetailsP, gridBagConstraints);

        panSelectAllP.setMaximumSize(new java.awt.Dimension(160, 27));
        panSelectAllP.setMinimumSize(new java.awt.Dimension(160, 27));
        panSelectAllP.setPreferredSize(new java.awt.Dimension(160, 27));
        panSelectAllP.setLayout(new java.awt.GridBagLayout());

        lblSelectAll1.setText("Select All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 19, 5, 0);
        panSelectAllP.add(lblSelectAll1, gridBagConstraints);

        chkSelectAll1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAll1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 5, 1);
        panSelectAllP.add(chkSelectAll1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panCbStop.add(panSelectAllP, gridBagConstraints);

        tabCbMaintenance.addTab("CB Stop", panCbStop);

        panCbRevoke.setLayout(new java.awt.GridBagLayout());

        panProductDataP1.setForeground(new java.awt.Color(102, 0, 255));
        panProductDataP1.setMaximumSize(new java.awt.Dimension(600, 120));
        panProductDataP1.setMinimumSize(new java.awt.Dimension(600, 120));
        panProductDataP1.setPreferredSize(new java.awt.Dimension(600, 120));
        panProductDataP1.setLayout(new java.awt.GridBagLayout());

        lblCustidp1.setText("Customer Id : ");
        lblCustidp1.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductDataP1.add(lblCustidp1, gridBagConstraints);

        lblAccnop1.setText("Account Number : ");
        lblAccnop1.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductDataP1.add(lblAccnop1, gridBagConstraints);

        lblCustomernamep1.setText("Customer Name : ");
        lblCustomernamep1.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductDataP1.add(lblCustomernamep1, gridBagConstraints);

        lblAddressp1.setText("Address : ");
        lblAddressp1.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductDataP1.add(lblAddressp1, gridBagConstraints);

        lblCustid4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblCustid4.setForeground(new java.awt.Color(102, 0, 255));
        lblCustid4.setMaximumSize(new java.awt.Dimension(97, 18));
        lblCustid4.setMinimumSize(new java.awt.Dimension(97, 18));
        lblCustid4.setPreferredSize(new java.awt.Dimension(97, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panProductDataP1.add(lblCustid4, gridBagConstraints);

        lblAccno4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblAccno4.setForeground(new java.awt.Color(102, 0, 255));
        lblAccno4.setMaximumSize(new java.awt.Dimension(97, 18));
        lblAccno4.setMinimumSize(new java.awt.Dimension(97, 18));
        lblAccno4.setPreferredSize(new java.awt.Dimension(97, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panProductDataP1.add(lblAccno4, gridBagConstraints);

        lblCustomername4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblCustomername4.setForeground(new java.awt.Color(102, 0, 255));
        lblCustomername4.setMaximumSize(new java.awt.Dimension(97, 18));
        lblCustomername4.setMinimumSize(new java.awt.Dimension(97, 18));
        lblCustomername4.setPreferredSize(new java.awt.Dimension(97, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panProductDataP1.add(lblCustomername4, gridBagConstraints);

        lblAddress4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblAddress4.setForeground(new java.awt.Color(102, 0, 255));
        lblAddress4.setMaximumSize(new java.awt.Dimension(180, 18));
        lblAddress4.setMinimumSize(new java.awt.Dimension(180, 18));
        lblAddress4.setPreferredSize(new java.awt.Dimension(180, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panProductDataP1.add(lblAddress4, gridBagConstraints);

        lblPanno1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblPanno1.setText("PanCard Number : ");
        lblPanno1.setMaximumSize(new java.awt.Dimension(117, 18));
        lblPanno1.setMinimumSize(new java.awt.Dimension(117, 18));
        lblPanno1.setPreferredSize(new java.awt.Dimension(117, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductDataP1.add(lblPanno1, gridBagConstraints);

        lblPanno3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblPanno3.setForeground(new java.awt.Color(102, 0, 255));
        lblPanno3.setMaximumSize(new java.awt.Dimension(97, 18));
        lblPanno3.setMinimumSize(new java.awt.Dimension(97, 18));
        lblPanno3.setPreferredSize(new java.awt.Dimension(97, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.ipady = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panProductDataP1.add(lblPanno3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panCbRevoke.add(panProductDataP1, gridBagConstraints);

        srpProductDetailsP1.setMaximumSize(new java.awt.Dimension(600, 290));
        srpProductDetailsP1.setMinimumSize(new java.awt.Dimension(600, 290));

        tblPanCardDetails1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Customer Id", "Account Number", "Customer Name", "Address", "Pan No"
            }
        ));
        tblPanCardDetails1.setPreferredScrollableViewportSize(new java.awt.Dimension(415, 140));
        tblPanCardDetails1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPanCardDetails1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblPanCardDetails1MouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPanCardDetails1MousePressed(evt);
            }
        });
        srpProductDetailsP1.setViewportView(tblPanCardDetails1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panCbRevoke.add(srpProductDetailsP1, gridBagConstraints);

        panSelectAllP1.setMaximumSize(new java.awt.Dimension(160, 27));
        panSelectAllP1.setMinimumSize(new java.awt.Dimension(160, 27));
        panSelectAllP1.setPreferredSize(new java.awt.Dimension(160, 27));
        panSelectAllP1.setLayout(new java.awt.GridBagLayout());

        lblSelectAll2.setText("Select All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 19, 5, 0);
        panSelectAllP1.add(lblSelectAll2, gridBagConstraints);

        chkSelectAll2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAll2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 5, 1);
        panSelectAllP1.add(chkSelectAll2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panCbRevoke.add(panSelectAllP1, gridBagConstraints);

        tabCbMaintenance.addTab("CB Revoke", panCbRevoke);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panCbMaintenance.add(tabCbMaintenance, gridBagConstraints);

        getContentPane().add(panCbMaintenance, java.awt.BorderLayout.CENTER);

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

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setFocusable(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrCbMaintenance.add(btnView);

        lblSpace5.setText("     ");
        tbrCbMaintenance.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.setFocusable(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrCbMaintenance.add(btnNew);

        lblSpace46.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace46.setText("     ");
        lblSpace46.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace46.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace46.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCbMaintenance.add(lblSpace46);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrCbMaintenance.add(btnEdit);

        lblSpace47.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace47.setText("     ");
        lblSpace47.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace47.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace47.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCbMaintenance.add(lblSpace47);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.setFocusable(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrCbMaintenance.add(btnDelete);

        lblSpace2.setText("     ");
        tbrCbMaintenance.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrCbMaintenance.add(btnSave);

        lblSpace48.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace48.setText("     ");
        lblSpace48.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace48.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace48.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCbMaintenance.add(lblSpace48);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrCbMaintenance.add(btnCancel);

        lblSpace3.setText("     ");
        tbrCbMaintenance.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrCbMaintenance.add(btnAuthorize);

        lblSpace49.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace49.setText("     ");
        lblSpace49.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace49.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace49.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCbMaintenance.add(lblSpace49);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setFocusable(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrCbMaintenance.add(btnException);

        lblSpace50.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace50.setText("     ");
        lblSpace50.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace50.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace50.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCbMaintenance.add(lblSpace50);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrCbMaintenance.add(btnReject);

        lblSpace4.setText("     ");
        tbrCbMaintenance.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.setFocusable(false);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrCbMaintenance.add(btnPrint);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCbMaintenance.add(lblSpace51);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setFocusable(false);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrCbMaintenance.add(btnClose);

        getContentPane().add(tbrCbMaintenance, java.awt.BorderLayout.NORTH);

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

        mbrCbMaintenance.add(mnuProcess);

        setJMenuBar(mbrCbMaintenance);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // Add your handling code here:
        btnPrintActionPerformed(evt);
    }//GEN-LAST:event_mitPrintActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed

        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        try {
            HashMap hash = new HashMap();
            if (panCbRequest.isShowing() == true) {
                panCbRequestEnable();    // To enable the panChequeBookRequest panel...
                hash.put("REQUESTED_SCREEN", "CHEQUE_REQUEST");
                hash.put("NEW_MODE", "NEW_MODE");
            } else if (panCbStop.isShowing() == true) {
                panCbStopEnable();    // To enable the panChequeBookStop panel...
                hash.put("REQUESTED_SCREEN", "CHEQUE_STOP");
            }else if (panCbRevoke.isShowing() == true) {
                panCbRevokeEnable();    // To enable the panChequeBookRevoke panel...
                hash.put("REQUESTED_SCREEN", "CHEQUE_REVOKE");
            }
            observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
            observable.populateData(hash);   //only operation for populating,when i click new button it should populate to the tables
            setModified(true);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnNew.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnReject.setEnabled(false);
            btnException.setEnabled(false);
            lblCustid2.setEnabled(true);
            lblAccNo2.setEnabled(true);
            lblCbRequest2.setEnabled(true);
            lblNoOfCbLeave2.setEnabled(true);
            lblUsageType2.setEnabled(true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        
        
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        lblStatus.setText(observable.getLblStatus());
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView( ClientConstants.ACTION_STATUS[3]);
        lblStatus.setText(observable.getLblStatus());
        btnSave.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        lblStatus.setText("Save"); 
        //observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.doAction();
        btnCancelActionPerformed(null);
        ClientUtil.clearAll(this);
    
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        observable.resetForm();
        observable.resetCbRequestTableValues();
        setModified(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true); 
        btnException.setEnabled(true);
        btnView.setEnabled(true);
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnSave.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        HashMap where = new HashMap();
        try {
            if (panCbRequest.isShowing() == true) {
                panCbRequestEnable();    // To enable the panChequeBookRequest panel...
                where.put("REQUESTED_SCREEN", "CHEQUE_REQUEST");
            } else if (panCbStop.isShowing() == true) {
                panCbStopEnable();    // To enable the panChequeBookStop panel...
                where.put("REQUESTED_SCREEN", "CHEQUE_STOP");
            }else if (panCbRevoke.isShowing() == true) {
                panCbRevokeEnable();    // To enable the panChequeBookRevoke panel...
                where.put("REQUESTED_SCREEN", "CHEQUE_REVOKE");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
         if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
            viewType.equals(ClientConstants.ACTION_STATUS[17])) 
        {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectCbMaintenanceList");
            ArrayList lst = new ArrayList();
            //lst.add("GBID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
             lst = null;
        }
        else
        {
            viewMap.put(CommonConstants.MAP_NAME, "Borrowings.getSelectAcctHeadTOList");
        }
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        viewMap.put(CommonConstants.MAP_WHERE, where);
        where = null;
         new ViewAll(this,viewMap).show();
       // new ViewAll(this, 
    }
    
    public void fillData(Object  map) {
    
        setModified(true);
        HashMap hash = (HashMap) map;
        
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                HashMap where = new HashMap();
                where.put("NB_TRANS_ID", hash.get("NB_TRANS_ID"));
                where.put("REQUESTED_SCREEN", hash.get("REQUESTED_SCREEN"));
                hash.put(CommonConstants.MAP_WHERE, where);
                if (viewType.equals(ClientConstants.ACTION_STATUS[2])){
                    hash.put("EDIT_MODE", "EDIT_MODE");
                }else if (viewType.equals(ClientConstants.ACTION_STATUS[3])){
                    hash.put("DELETE_MODE", "DELETE_MODE");
                }else if (viewType.equals(AUTHORIZE)){
                    hash.put("AUTHORIZE_MODE", "AUTHORIZE_MODE");
                }
                try {
                    observable.populateData(hash);
                    String nbTransId = (String) hash.get("NB_TRANS_ID");
                    lblNbTransId1.setText(nbTransId);
                } catch (Exception ex) {
                    Logger.getLogger(NbChequeBookMaintenanceUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if (observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW) {
                    ClientUtil.enableDisable(panCbMaintenance, false);
                    String st = CommonUtil.convertObjToStr(tblCbRequest.getValueAt(tblCbRequest.getSelectedRow(), 0));

                    if (st.equals("true")) {
                        tblCbRequest.setValueAt(new Boolean(true), tblCbRequest.getSelectedRow(), 0);
                    } else {     
                        tblCbRequest.setValueAt(new Boolean(false), tblCbRequest.getSelectedRow(), 0);
                    }
                    
                }else  if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                    ClientUtil.enableDisable(panCbMaintenance, true);
                    //txtCustId.setEditable(false);
                }
                if(viewType.equals(AUTHORIZE)){
                    ClientUtil.enableDisable(panCbMaintenance, false);
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
                setButtonEnableDisable();
            }
        }
    }
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
//        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
//        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType != AUTHORIZE){
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put("AUTHORIZE_MODE","AUTHORIZE_MODE");
            try {
            if (panCbRequest.isShowing() == true) {
                panCbRequestEnable();    // To enable the panChequeBookRequest panel...
                whereMap.put("REQUESTED_SCREEN", "CHEQUE_REQUEST");
            } else if (panCbStop.isShowing() == true) {
                panCbStopEnable();    // To enable the panChequeBookStop panel...
                whereMap.put("REQUESTED_SCREEN", "CHEQUE_STOP");
            }else if (panCbRevoke.isShowing() == true) {
                panCbRevokeEnable();    // To enable the panChequeBookRevoke panel...
                whereMap.put("REQUESTED_SCREEN", "CHEQUE_REVOKE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getSelectCbMaintenanceList");
            viewType = AUTHORIZE;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            //            isFilled = false;
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        } else if (viewType == AUTHORIZE){ 
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTH_DT",ClientUtil.getCurrentDateWithTime());
            singleAuthorizeMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            arrList.add(singleAuthorizeMap);
            singleAuthorizeMap.put("USER_ID",TrueTransactMain.USER_ID);
         
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap);
            
            
        }
    }
    
    public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);
        
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setAuthorizeMap(map);
            observable.doAction();
            
            btnCancelActionPerformed(null);
            observable.setStatus();
            //observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
            
        }
    }
    
    
    private void tblCbRequestMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCbRequestMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblCbRequestMousePressed

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
        boolean flag;
        if (chkSelectAll.isSelected() == true) {
            flag = true;
        } else {
            flag = false;
        }
        for (int i = 0; i < tblCbRequest.getRowCount(); i++) {
            if (tblCbRequest.getValueAt(i, 4).equals("Error")) {
            } else {
                tblCbRequest.setValueAt(new Boolean(flag), i, 0);
           }
        }      
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void tblCbRequestMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCbRequestMouseClicked
            String st = CommonUtil.convertObjToStr(tblCbRequest.getValueAt(tblCbRequest.getSelectedRow(), 0));

            if (st.equals("true")) {
                tblCbRequest.setValueAt(new Boolean(false), tblCbRequest.getSelectedRow(), 0);
            } else {     
                tblCbRequest.setValueAt(new Boolean(true), tblCbRequest.getSelectedRow(), 0);
            }        
        lblCustid2.setText(CommonUtil.convertObjToStr(tblCbRequest.getValueAt(tblCbRequest.getSelectedRow(), 1)));
        lblAccNo2.setText(CommonUtil.convertObjToStr(tblCbRequest.getValueAt(tblCbRequest.getSelectedRow(), 2)));
        lblCbRequest2.setText(CommonUtil.convertObjToStr(tblCbRequest.getValueAt(tblCbRequest.getSelectedRow(), 3)));
        lblNoOfCbLeave2.setText(CommonUtil.convertObjToStr(tblCbRequest.getValueAt(tblCbRequest.getSelectedRow(), 4)));
        lblUsageType2.setText(CommonUtil.convertObjToStr(tblCbRequest.getValueAt(tblCbRequest.getSelectedRow(), 5)));
        Map selOutput = new HashMap();
        Map deselOutput = new HashMap();
        for (int i = 0; i < tblCbRequest.getRowCount(); i++) {
            String ab = CommonUtil.convertObjToStr(tblCbRequest.getValueAt(i, 0));
            if (ab.equals("true")) { 
                selOutput.put("CUST_ID", CommonUtil.convertObjToStr(tblCbRequest.getValueAt(tblCbRequest.getSelectedRow(), 1)));
                selOutput.put("ACCOUNT_NUM", CommonUtil.convertObjToStr(tblCbRequest.getValueAt(tblCbRequest.getSelectedRow(), 2)));
                observable.selectedList.add(selOutput);

            }
            else if (ab.equals("false")) { 
                deselOutput.put("CUST_ID", CommonUtil.convertObjToStr(tblCbRequest.getValueAt(tblCbRequest.getSelectedRow(), 1)));
                deselOutput.put("ACCOUNT_NUM", CommonUtil.convertObjToStr(tblCbRequest.getValueAt(tblCbRequest.getSelectedRow(), 2)));
                observable.deselectedList.add(deselOutput);

            }
        }       
    }//GEN-LAST:event_tblCbRequestMouseClicked

    private void tblCbRequestMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCbRequestMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tblCbRequestMouseEntered

    private void tblPanCardDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPanCardDetailsMouseClicked
               
    }//GEN-LAST:event_tblPanCardDetailsMouseClicked

    private void tblPanCardDetailsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPanCardDetailsMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPanCardDetailsMouseEntered

    private void tblPanCardDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPanCardDetailsMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPanCardDetailsMousePressed

    private void chkSelectAll1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAll1ActionPerformed
              // TODO add your handling code here:
    }//GEN-LAST:event_chkSelectAll1ActionPerformed

    private void srpCbRequestMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_srpCbRequestMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_srpCbRequestMouseClicked

        private void tblPanCardDetails1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPanCardDetails1MouseClicked
            // TODO add your handling code here:
        }//GEN-LAST:event_tblPanCardDetails1MouseClicked

        private void tblPanCardDetails1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPanCardDetails1MouseEntered
            // TODO add your handling code here:
        }//GEN-LAST:event_tblPanCardDetails1MouseEntered

        private void tblPanCardDetails1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPanCardDetails1MousePressed
            // TODO add your handling code here:
        }//GEN-LAST:event_tblPanCardDetails1MousePressed

        private void chkSelectAll2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAll2ActionPerformed
            // TODO add your handling code here:
        }//GEN-LAST:event_chkSelectAll2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NbChequeBookMaintenanceUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NbChequeBookMaintenanceUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NbChequeBookMaintenanceUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NbChequeBookMaintenanceUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new NbChequeBookMaintenanceUI().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(NbChequeBookMaintenanceUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
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
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll1;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll2;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private javax.swing.JLabel lblAccNo2;
    private javax.swing.JLabel lblAccno3;
    private javax.swing.JLabel lblAccno4;
    private com.see.truetransact.uicomponent.CLabel lblAccnop;
    private com.see.truetransact.uicomponent.CLabel lblAccnop1;
    private javax.swing.JLabel lblAddress3;
    private javax.swing.JLabel lblAddress4;
    private com.see.truetransact.uicomponent.CLabel lblAddressp;
    private com.see.truetransact.uicomponent.CLabel lblAddressp1;
    private com.see.truetransact.uicomponent.CLabel lblCbRequest;
    private javax.swing.JLabel lblCbRequest2;
    private com.see.truetransact.uicomponent.CLabel lblCustid;
    private javax.swing.JLabel lblCustid2;
    private javax.swing.JLabel lblCustid3;
    private javax.swing.JLabel lblCustid4;
    private com.see.truetransact.uicomponent.CLabel lblCustidp;
    private com.see.truetransact.uicomponent.CLabel lblCustidp1;
    private javax.swing.JLabel lblCustomername3;
    private javax.swing.JLabel lblCustomername4;
    private com.see.truetransact.uicomponent.CLabel lblCustomernamep;
    private com.see.truetransact.uicomponent.CLabel lblCustomernamep1;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNbTransId;
    private javax.swing.JLabel lblNbTransId1;
    private com.see.truetransact.uicomponent.CLabel lblNoOfCbLeave;
    private javax.swing.JLabel lblNoOfCbLeave2;
    private javax.swing.JLabel lblPanno;
    private javax.swing.JLabel lblPanno1;
    private javax.swing.JLabel lblPanno2;
    private javax.swing.JLabel lblPanno3;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll1;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll2;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace46;
    private com.see.truetransact.uicomponent.CLabel lblSpace47;
    private com.see.truetransact.uicomponent.CLabel lblSpace48;
    private com.see.truetransact.uicomponent.CLabel lblSpace49;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace50;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblUsageType;
    private javax.swing.JLabel lblUsageType2;
    private com.see.truetransact.uicomponent.CMenuBar mbrCbMaintenance;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panCbMaintenance;
    private com.see.truetransact.uicomponent.CPanel panCbRequest;
    private com.see.truetransact.uicomponent.CPanel panCbRevoke;
    private com.see.truetransact.uicomponent.CPanel panCbStop;
    private com.see.truetransact.uicomponent.CPanel panProductData;
    private com.see.truetransact.uicomponent.CPanel panProductDataP;
    private com.see.truetransact.uicomponent.CPanel panProductDataP1;
    private com.see.truetransact.uicomponent.CPanel panSelectAll;
    private com.see.truetransact.uicomponent.CPanel panSelectAllP;
    private com.see.truetransact.uicomponent.CPanel panSelectAllP1;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpCbRequest;
    private com.see.truetransact.uicomponent.CScrollPane srpProductDetailsP;
    private com.see.truetransact.uicomponent.CScrollPane srpProductDetailsP1;
    private com.see.truetransact.uicomponent.CTabbedPane tabCbMaintenance;
    private com.see.truetransact.uicomponent.CTable tblCbRequest;
    private com.see.truetransact.uicomponent.CTable tblPanCardDetails;
    private com.see.truetransact.uicomponent.CTable tblPanCardDetails1;
    private javax.swing.JToolBar tbrCbMaintenance;
    // End of variables declaration//GEN-END:variables

 
    public void update(Observable o, Object arg) {
    }

    private LinkedHashMap getTblTOMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    }
