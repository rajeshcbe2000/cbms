/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmptransferUI.java
 *
 * Created on feb 9, 2009, 10:53 AM
 */

package com.see.truetransact.ui.sysadmin.branch;

import java.util.HashMap;
import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI ;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.customer.customeridchange.CustomerIdChangeOB;
import com.see.truetransact.commonutil.DateUtil;
import java.util.Date;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uivalidation.*;
import com.see.truetransact.uicomponent.CButtonGroup;
import javax.swing.JOptionPane;




/** This form is used to manipulate CustomerIdChangeUI related functionality
 * @author swaroop
 */
public class ShiftUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField{

    private String viewType = new String();
    private HashMap mandatoryMap;
   private ShiftOB observable;
    final String AUTHORIZE="Authorize";
    private final static Logger log = Logger.getLogger(ShiftUI.class);
//    ShiftRB ShiftRB = new ShiftRB();
    private String prodType="";
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    String cust_type=null;
        
    /** Creates new form CustomerIdChangeUI */
    public ShiftUI() {
        
        initComponents();
        initStartUp();
        ClientUtil.enableDisable(panShift, false, false, true);
        btnCancel.setEnabled(false);
        // btnSave.setEnabled(false);
//         btnAuthorize.setEnabled(false);
         btnReject.setEnabled(false);
         btnException.setEnabled(false);
//         btnEdit.setEnabled(false);
//         btnDelete.setEnabled(false);
    }
    
    private void initStartUp(){
        
        setMandatoryHashMap();
        setFieldNames();
        internationalize();
        observable = new ShiftOB();
        observable.addObserver(this);
        initComponentData();
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
         objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.outwardregister.OutwardMRB", ProxyParameters.LANGUAGE);
//        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panOutward);
         setHelpMessage();
          lblDate.setText("<html> Date : <font color=blue>" + DateUtil.getStringDate(ClientUtil.getCurrentDate()) + "</font></html>");
        //cLabel1.setText("");
    }
    
    private void setMaxLength() {

    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
//        mandatoryMap.put("txaDetails", new Boolean(true));
//        mandatoryMap.put("txtOutwardNo", new Boolean(false));
//        mandatoryMap.put("tdtDate", new Boolean(true));
//        mandatoryMap.put("txaRemarks", new Boolean(false));
//        mandatoryMap.put("txtReferenceNo", new Boolean(true));
           }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    
   
    public void setHelpMessage() {
    }
    
   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panOutwardRegister = new com.see.truetransact.uicomponent.CPanel();
        panShift = new com.see.truetransact.uicomponent.CPanel();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        cboShift = new com.see.truetransact.uicomponent.CComboBox();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        btnStart = new com.see.truetransact.uicomponent.CButton();
        btnEnd = new com.see.truetransact.uicomponent.CButton();
        tbrInwardRegister = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace62 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace65 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace63 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace64 = new com.see.truetransact.uicomponent.CLabel();
        btnDateChange = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrCustomer = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMaximumSize(new java.awt.Dimension(800, 625));
        setMinimumSize(new java.awt.Dimension(350, 360));
        setPreferredSize(new java.awt.Dimension(350, 360));

        panOutwardRegister.setMaximumSize(new java.awt.Dimension(650, 520));
        panOutwardRegister.setMinimumSize(new java.awt.Dimension(350, 350));
        panOutwardRegister.setPreferredSize(new java.awt.Dimension(350, 350));
        panOutwardRegister.setLayout(new java.awt.GridBagLayout());

        panShift.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panShift.setMinimumSize(new java.awt.Dimension(300, 250));
        panShift.setName("panMaritalStatus");
        panShift.setPreferredSize(new java.awt.Dimension(300, 250));
        panShift.setEnabled(false);
        panShift.setLayout(new java.awt.GridBagLayout());

        lblDate.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 80, 3);
        panShift.add(lblDate, gridBagConstraints);

        cboShift.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panShift.add(cboShift, gridBagConstraints);

        cPanel1.setLayout(new java.awt.GridBagLayout());

        btnStart.setText("Start");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(btnStart, gridBagConstraints);

        btnEnd.setText("End");
        btnEnd.setMinimumSize(new java.awt.Dimension(63, 26));
        btnEnd.setPreferredSize(new java.awt.Dimension(63, 26));
        btnEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEndActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(btnEnd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(50, 3, 3, 3);
        panShift.add(cPanel1, gridBagConstraints);

        panOutwardRegister.add(panShift, new java.awt.GridBagConstraints());

        getContentPane().add(panOutwardRegister, java.awt.BorderLayout.CENTER);

        tbrInwardRegister.setMinimumSize(new java.awt.Dimension(350, 32));
        tbrInwardRegister.setPreferredSize(new java.awt.Dimension(350, 32));

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
        tbrInwardRegister.add(btnView);

        lblSpace5.setText("     ");
        tbrInwardRegister.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnNew);

        lblSpace2.setText("     ");
        tbrInwardRegister.add(lblSpace2);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnCancel);

        lblSpace3.setText("     ");
        tbrInwardRegister.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.setFocusable(false);
        btnAuthorize.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAuthorize.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnAuthorize);

        lblSpace62.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace62.setText("     ");
        lblSpace62.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace62.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace62.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInwardRegister.add(lblSpace62);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnException);

        lblSpace65.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace65.setText("     ");
        lblSpace65.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace65.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace65.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInwardRegister.add(lblSpace65);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnReject);

        lblSpace4.setText("     ");
        tbrInwardRegister.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrInwardRegister.add(btnPrint);

        lblSpace63.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace63.setText("     ");
        lblSpace63.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace63.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace63.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInwardRegister.add(lblSpace63);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnClose);

        lblSpace64.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace64.setText("     ");
        lblSpace64.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace64.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace64.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInwardRegister.add(lblSpace64);

        btnDateChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/denomination.gif"))); // NOI18N
        btnDateChange.setToolTipText("Exception");
        btnDateChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateChangeActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnDateChange);

        getContentPane().add(tbrInwardRegister, java.awt.BorderLayout.NORTH);

        panStatus.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        mbrCustomer.setName("mbrCustomer");

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
        mnuProcess.add(mitClose);

        mbrCustomer.add(mnuProcess);

        setJMenuBar(mbrCustomer);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEndActionPerformed
        // TODO add your handling code here:
         final String shift= CommonUtil.convertObjToStr(((ComboBoxModel)(cboShift.getModel())).getSelectedItem());
          if(shift.equals(""))
        {
            JOptionPane.showMessageDialog(this,"Please select Shift","Warning",JOptionPane.WARNING_MESSAGE);
            return;
        }
         
         HashMap mapd = new HashMap();
             HashMap mapd1 = new HashMap();
            //map1.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            mapd.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
         List shiftn= ClientUtil.executeQuery("getShiftNames" , mapd);
         //for()
         System.out.println("shiftngdfgfd=========="+shiftn);
       mapd1= (HashMap) shiftn.get(0);
       if(mapd1.get("SHIFT")!=null)
       {
 System.out.println("mapd1================="+mapd1);
//      if(!sssss.equals("") || !sssss.equals(null))
//      {
         
           
                           HashMap stMap3 = new HashMap();
                            stMap3 = (HashMap) shiftn.get(0);
                            String sname=stMap3.get("SHIFT").toString();
                            System.out.println("sname====="+sname);  
                      if(!sname.equals(shift))
                      {
                           JOptionPane.showMessageDialog(this,shift+" is not started","Warning",JOptionPane.WARNING_MESSAGE);
                           return; 
                      }
            }
      else
      {
         JOptionPane.showMessageDialog(this,"Shift is not started","Warning",JOptionPane.WARNING_MESSAGE);
            return;  
      }
         
        
        HashMap map1 = new HashMap();
            //map1.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            map1.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            //map1.put("USER_ID", ProxyParameters.USER_ID);
            //List lst = ClientUtil.executeQuery("getUserLogoutStatus", map1) ;
//            List lst1=ClientUtil.executeQuery("Auth_view", map1) ;
//           System.out.println("lst1================"+lst1);
                      System.out.println("lst1================hsir");
           
  
//            if(lst1!=null&& lst1.size()>0)
//            {
//           JOptionPane.showMessageDialog(this,"Transactions pending for Authorization.Cannot Change the Shift","Warning",JOptionPane.WARNING_MESSAGE);
//            return;   
//            }
        
           //HashMap hmapn= new HashMap();
        
           
        
        
        System.out.println("shift============="+shift);
        HashMap hmap1= new HashMap();
       // hmap1.put("SH",shift);
        hmap1.put("SHIFT",null);
        hmap1.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
        ClientUtil.execute("updateBranchMasterShift", hmap1);
        JOptionPane.showMessageDialog(this,"Shift Ended","Information",JOptionPane.INFORMATION_MESSAGE);
        cboShift.setSelectedIndex(0);
    }//GEN-LAST:event_btnEndActionPerformed

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        // TODO add your handling code here:
        final String shift= CommonUtil.convertObjToStr(((ComboBoxModel)(cboShift.getModel())).getSelectedItem());
        if(shift.equals(""))
        {
            JOptionPane.showMessageDialog(this,"Please select Shift","Warning",JOptionPane.WARNING_MESSAGE);
            return;
        }
        System.out.println("shift============="+shift);
        HashMap hmap1= new HashMap();
        //hmap1.put("SHIFT",shift);
        hmap1.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
        List list= ClientUtil.executeQuery("getShiftStatus" , hmap1);

         System.out.println("list===================="+list);
         
//          List stList2 =ClientUtil.executeQuery("getSelectPenalRate",stMap2);
//                        System.out.println("stList2====="+stList2);
                        if(list!=null && list.size()>0)
                        {
                           HashMap stMap2 = new HashMap();
                            stMap2 = (HashMap) list.get(0);
                            int x=Integer.parseInt(stMap2.get("COUNT").toString());
                            System.out.println("Penal_rate====="+x);
                             if(x>0)
        {
            
           List shiftn= ClientUtil.executeQuery("getShiftNames" , hmap1);
            if(shiftn!=null && shiftn.size()>0)
                        {
                           HashMap stMap3 = new HashMap();
                            stMap3 = (HashMap) shiftn.get(0);
                            String sname=stMap3.get("SHIFT").toString();
                            System.out.println("sname====="+sname);
                            
        
          JOptionPane.showMessageDialog(this,sname+" Already Started","Warning",JOptionPane.ERROR_MESSAGE);
            }
        }
                             else
        {
            
            observable.execute("Start");
            
            JOptionPane.showMessageDialog(this,"Shift Started","Information",JOptionPane.INFORMATION_MESSAGE);
        }
                        }
         cboShift.setSelectedIndex(0);
         
//        int x= CommonUtil.convertObjToInt(list.get(0));
//        System.out.println("xxxxxxxxxxxxxxxxx"+x);
       
       
    }//GEN-LAST:event_btnStartActionPerformed

    private void btnDateChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateChangeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDateChangeActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
       
    }//GEN-LAST:event_btnViewActionPerformed
                
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed

    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed

    }//GEN-LAST:event_btnRejectActionPerformed
        
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mitCloseActionPerformed(evt);
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
        cifClosingAlert();
        
    }
            
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        observable.resetForm();
        setModified(false);
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);
//        btnEdit.setEnabled(false);
//        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
        
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
         setModified(true);
         observable.resetForm();
         cboShift.setEnabled(true);
         btnStart.setEnabled(true);
         btnEnd.setEnabled(true);
         btnCancel.setEnabled(true);
//         btnSave.setEnabled(false);
//         btnAuthorize.setEnabled(false);
         btnReject.setEnabled(false);
         btnException.setEnabled(false);
//         btnEdit.setEnabled(false);
//         btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
//        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
//        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
//        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
//        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
//        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
                
    /** To populate Comboboxes */
    private void initComponentData() 
    {
      cboShift.setModel(observable.getCbmShift());
    }
    
    /** To display a popUp window for viewing existing data */
    private void popUp(String currAction){
       viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
           // map.put("BRANCH_CODE","0001");
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getOutwardRegisterEdit"); 
        }else if(currAction.equalsIgnoreCase("Delete")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getOutwardRegisterEdit");
        } else if(currAction.equalsIgnoreCase("EMP")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "setEmpDetailsForTransfer");
        }
        else if(currAction.equalsIgnoreCase("Enquiry")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getEmpTransferDetailsView");
        }
        new ViewAll(this,viewMap).show();
       
    }

    
    /** Called by the Popup window created thru popUp method */
//    public void fillData(Object map) {
//        try{
//           setModified(true);
//        HashMap hash = (HashMap) map;
//        if (viewType != null) {
//            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
//            viewType.equals(ClientConstants.ACTION_STATUS[3])|| viewType.equals(AUTHORIZE) ||
//            viewType.equals(ClientConstants.ACTION_STATUS[17])) {
//                //System.out.println("@@@@@@@@@@@+hash"+hash.get("BOARD_MT_ID"));
//                hash.put(CommonConstants.MAP_WHERE, hash.get("OUTWARD_NO"));
//                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
////                observable.getData(hash);
//                if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
//                viewType.equals(ClientConstants.ACTION_STATUS[17])) {
//                    ClientUtil.enableDisable(panOutward, false);
//                   
//                } else {
//                    ClientUtil.enableDisable(panOutward, true);
//                 
//                }
//                setButtonEnableDisable();
//                if(viewType ==  AUTHORIZE) {
////                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
////                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
////                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
//                }
//            }
//            
//        }
//
//        }catch(Exception e){
//            log.error(e);
//        }
//    }
    
    
    private void enableDisable(boolean yesno){
        ClientUtil.enableDisable(this, yesno);
    }
    
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
////        btnEdit.setEnabled(!btnEdit.isEnabled());
////        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
//        mitEdit.setEnabled(btnEdit.isEnabled());
//        mitDelete.setEnabled(btnDelete.isEnabled());
        
//        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
//        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        
//        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        
//        txtOutwardNo.setVisible(false);
    }
    
    
    public void update(Observable observed, Object arg) 
    {
        cboShift.setSelectedItem(observable.getCboShift());
    }
    
    public void updateOBFields()
    {
     observable.setCboShift((String)cboShift.getSelectedItem()); 
    }
    
    
    
    private void savePerformed(){
//        
//        updateOBFields();s
//        observable.doAction() ;
//        if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
//            HashMap lockMap = new HashMap();
//            ArrayList lst = new ArrayList();
//            // lst.add("EMP_TRANSFER_ID");
//            //lockMap.put("EMP_TRANSFER_ID",CommonUtil.convertObjToStr(lblEmpTransferID.getText()));
//            // lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
//            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
//                //   lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
//                if (observable.getProxyReturnMap()!=null) {
//                    if (observable.getProxyReturnMap().containsKey("BOARD_MT_ID")) {
//                        //lockMap.put("EMP_TRANSFER_ID",observable.getProxyReturnMap().get("VISIT_ID"));
//                    }
//                }
//            }
//            if(observable.getResult()==ClientConstants.ACTIONTYPE_EDIT){
//                // lockMap.put("EMP_TRANSFER_ID", observable.getTxtEmpTransferID());
//            }
//            // setEditLockMap(lockMap);
//            ////  setEditLock();
//            //  deletescreenLock();
//        }
//        
//        observable.resetForm();
//        enableDisable(false);
//        setButtonEnableDisable();
//        lblStatus.setText(observable.getLblStatus());
//        ClientUtil.enableDisable(this, false);
//        observable.setResultStatus();
//        lblStatus.setText(observable.getLblStatus());
//        //__ Make the Screen Closable..
//        setModified(false);
//        ClientUtil.clearAll(this);
//        observable.ttNotifyObservers();
        
    }
    private void setFieldNames() {
//        tdtDate.setName("tdtDate");
        //txaDetails.setName("txaDetails");
        //txaRemarks.setName("txaRemarks");
//        txtOutwardNo.setName("txtOutwardNo");
//        txtReferenceNo.setName("txtReferenceNo");
        lblDate.setName("lblDate");
//        lblDetails.setName("lblDetails");
//        lblOutwardNo.setName("lblOutwardNo");
//        lblReferenceNo.setName("lblReferenceNo");
//        lblStatus.setName("lblStatus");
        panOutwardRegister.setName("panOutwardRegister");
       // panOutward.setName("panOutward");
        
    }
    
    private void internationalize() {
             //lblProdType.setText(resourceBundle.getString("lblProdType"));
    }
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    
    private void deletescreenLock(){
        HashMap map=new HashMap();
        map.put("USER_ID",ProxyParameters.USER_ID);
        map.put("TRANS_DT", ClientUtil.getCurrentDate());
        map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }
    private String getLockDetails(String lockedBy, String screenId){
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer() ;
        map.put("LOCKED_BY", lockedBy) ;
        map.put("SCREEN_ID", screenId) ;
        java.util.List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if(lstLock.size() > 0){
            map = (HashMap)(lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME")) ;
            data.append("\nIP Address : ").append(map.get("IP_ADDR")) ;
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null ;
        map = null ;
        return data.toString();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDateChange;
    private com.see.truetransact.uicomponent.CButton btnEnd;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnStart;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CComboBox cboShift;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace62;
    private com.see.truetransact.uicomponent.CLabel lblSpace63;
    private com.see.truetransact.uicomponent.CLabel lblSpace64;
    private com.see.truetransact.uicomponent.CLabel lblSpace65;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panOutwardRegister;
    private com.see.truetransact.uicomponent.CPanel panShift;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrInwardRegister;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
       ShiftUI Shift = new ShiftUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(Shift);
        j.show();
        Shift.show();
    }
}
