/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * frmRentRegister.java
 *
 * Created on September 12, 2011, 12:08 PM
 */

package com.see.truetransact.ui.roomrent;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import java.text.*;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import java.util.Observable;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.uicomponent.COptionPane;
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
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import java.util.Date;
/**
 *
 * @author  userdd
 */
public class frmRentRegister extends CInternalFrame implements Observer, UIMandatoryField{
 Date currDt = ClientUtil.getCurrentDate();
 private RentRegisterOB observable;
 private String[][] tabledata;
 private String[] column;
 private RentRegisterMRB objMandatoryRB = new RentRegisterMRB();//Instance for the MandatoryResourceBundle
 private DefaultTableModel model;
 private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
 private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
 private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
 private final String AUTHORIZE="Authorize";//Variable used when btnAuthorize is clicked
   TransactionUI transactionUI = new TransactionUI();
 // private com.see.truetransact.clientutil.ComboBoxModel cbo;
 //   private com.see.truetransact.clientutil.ComboBoxModel cbo1;
 double totalAmount = 0.0;
 public String txtRmNumber="";
 public String rmNumber="";
 public String rtId="";
     double sanctionAmount = 0.0;
      public String appLoginDate=CommonUtil.convertObjToStr(currDt.clone());
    // public String appLoginDate="10/04/2012";
      public CommonMethod cm;
    /** Creates new form ifrNewBorrowing */
    public frmRentRegister() {
             ProxyParameters.BRANCH_ID = TrueTransactMain.BRANCH_ID;
        setSelectedBranchID(TrueTransactMain.BRANCH_ID);
       // cbo=new com.see.truetransact.clientutil.ComboBoxModel();
       // cbo1=new com.see.truetransact.clientutil.ComboBoxModel();
        initComponents();
       // initComponentData();
        setFieldNames();
        setObservable();
        observable.resetForm();
         setMaxLengths();
    
         setMandatoryHashMap();
         new MandatoryCheck().putMandatoryMarks(getClass().getName(),panRentReg, getMandatoryHashMap());
            panTrans.add(transactionUI);
        transactionUI.setSourceScreen("RENT_REGISTER");
        observable.setTransactionOB(transactionUI.getTransactionOB()); 
        ClientUtil.enableDisable(panRentReg, false);
        setButtonEnableDisable();
         btnClose.setVisible(true);
     //   txtSecAmt.setText("0.00");
     //   txtSalesmanID.setEnabled(false);
        txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
       // txtApplDate.setEnabled(false);     
          cm=new CommonMethod();
           transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        transactionUI.setCallingApplicantName("");
    }
     private void initComponentData() {
        try{
                    
        }catch(ClassCastException e){
            //parseException.logException(e,true);
            System.out.println("Error in initComponentData():"+e);
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
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
      //   lblMsg.setName("lblMsg");
         tbrSale.setName("tbrSale");
         lblBuildingNo.setName("lblBuildingNo");
        lblRoomNo.setName("lblRoomNo");
        lblAppNo.setName("lblAppNo");
        lblApplnDate.setName("lblApplnDate");
        lblName.setName("lblName");
        lblHouseName.setName("lblHouseName");
        lblPlace.setName("lblPlace");
        lblCity.setName("lblCity");
        lblPhNo.setName("lblPhNo");
        lblMobNo.setName("lblMobNo");
        lblEmailId.setName("lblEmailId");
        lblGuardian.setName("lblGuardian");
        lblNominee.setName("lblNominee");
        lblOccDate.setName("lblOccDate");
        lblCommDate.setName("lblCommDate");
        lblRecommBy.setName("lblRecommBy");
        lblAgreNo.setName("lblAgreNo");
        lblAgrDate.setName("lblAgrDate");
        lblRentAmt.setName("lblRentAmt");
        lblRentDate.setName("lblRentDate");
        lblPenalGrPeriod.setName("lblPenalGrPeriod");
        lblAdvAmt.setName("lblAdvAmt");
        lblAdvDetails.setName("lblAdvDetails");
        
        txtBuildingNo.setName("txtBuildingNo");
        txtRoomNo.setName("txtRoomNo");
        txtAppNo.setName("txtAppNo");
        tdtApplnDate.setName("tdtApplnDate");
        txtName.setName("txtName");
        txtHouseName.setName("txtHouseName");
        txtPlace.setName("txtPlace");
        txtCity.setName("txtCity");
        txtPhNo.setName("txtPhNo");
        txtMobNo.setName("txtMobNo");
        txtEmailId.setName("txtEmailId");
        txtGuardian.setName("txtGuardian");
        txtNominee.setName("txtNominee");
        tdtOccDate.setName("tdtOccDate");
        tdtCommDate.setName("tdtCommDate");
        txtRecommBy.setName("txtRecommBy");
        txtAgreNo.setName("txtAgreNo");
        tdtAgrDate.setName("tdtAgrDate");
        txtRentAmt.setName("txtRentAmt");
        cboRentDate.setName("cboRentDate");
        cboPenalGrPeriod.setName("cboPenalGrPeriod");
        txtAdvAmt.setName("txtAdvAmt");
        txtAdvDetails.setName("txtAdvDetails");
         panTrans.setName("panTrans");
         btnBuildingNo.setName("btnBuildingNo");
         btnRoomNo.setName("btnRoomNo");
    }
    private void setMaxLengths() {
          txtRentAmt.setValidation(new CurrencyValidation());
           txtAdvAmt.setValidation(new CurrencyValidation());
           txtMobNo.setMaxLength(12);
           txtPhNo.setMaxLength(12);
       }
      private void setObservable() {
        try{
            observable = RentRegisterOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            //parseException.logException(e,true);
            System.out.println("Error in setObservable():"+e);
        }
    }
    
  
      /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
        HashMap where = new HashMap();
        viewType = currField;
        HashMap viewMap = new HashMap();
         if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
            viewType.equals(ClientConstants.ACTION_STATUS[17])) 
        {
             viewMap.put(CommonConstants.MAP_NAME, "RentRegister.getSelectRentRegisterList");
            ArrayList lst = new ArrayList();
            lst.add("RRID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
        }
         else if(viewType.equals("BUILDING_NUM"))
         {
              viewMap.put(CommonConstants.MAP_NAME, "RentRegister.getBuildingNo");
            ArrayList lst = new ArrayList();
            lst.add("BUILDING_NUM");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
         }
         else if(viewType.equals("ROOMNUM"))
         {
           //  System.out.println("rmNumberm INNNNNNNNNNNNNNNNNNNNNNN=========================="+rmNumber);
             
             if(rmNumber!=null)
             {
               viewMap.put(CommonConstants.MAP_NAME, "RentRegister.getRoomNonew");
                 
            ArrayList lst = new ArrayList();
            lst.add("ROOMNUM");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
             where.put("BUILDING_NO",rmNumber);
             txtRmNumber=rmNumber;
            lst = null;
             }
             else
             {
                 displayAlert("select Building No!!!");
                 return;
             }
         }
        
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
       
        viewMap.put(CommonConstants.MAP_WHERE, where);
        where = null;
        new ViewAll(this,viewMap).show();
   
    }
     public void fillData(Object  map) {

      // setModified(true);
        HashMap hash = (HashMap) map;
      if(viewType.equals("BUILDING_NUM"))
          {
                 txtBuildingNo.setText(hash.get("BUILDING_NUM").toString());
                 rmNumber=hash.get("RMNUMBER").toString();
                 txtRoomNo.setText("");
                 btnBuildingNo.setEnabled(false);
          }
         if(viewType.equals("ROOMNUM"))
          {
                 txtRoomNo.setText(hash.get("ROOMNUM").toString());
                 txtRentAmt.setText(hash.get("RENT_AMT").toString());
                 txtRentAmt.setEnabled(false);
                 btnRoomNo.setEnabled(false);
          }
        if (viewType != null) {
            if (/*viewType.equals("BUILDING_NUM") || viewType.equals("ROOMNUM") ||*/ viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                HashMap where = new HashMap();
                System.out.println("haviewTypeviewType=="+viewType);
             /*    if(viewType.equals("BUILDING_NUM")) {
                    //  data.put("BORROWING_DATA", "BORROWING_DATA");
                    where.put("BUILDING_NUM", hash.get("BUILDING_NUM"));
                }
                 else  if(viewType.equals("ROOMNUM")) {
                    //  data.put("BORROWING_DATA", "BORROWING_DATA");
                    where.put("ROOMNUM", hash.get("ROOMNUM"));
                }
                 else
                 {*/
                    // System.out.println("IN RR ID ========"+hash.get("RRID"));
                     where.put("RRID", hash.get("RRID"));
               //  }
                 rtId = CommonUtil.convertObjToStr(hash.get("RTID")); 
                System.out.println("where===IOOOOOOOOOOOO==="+where);
                 hash.put(CommonConstants.MAP_WHERE, where);
                observable.populateData(hash);
            
            
                if (observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW) {
                    ClientUtil.enableDisable(panRentReg, false);
                }  
                else  if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                    ClientUtil.enableDisable(panRentReg, true);
                }
                if(viewType.equals(AUTHORIZE)){
                    ClientUtil.enableDisable(panRentReg, false);
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
               setButtonEnableDisable();
               if(viewType.equals(AUTHORIZE)){
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
               }
            }
           // setButtonEnableDisable();
        }
          txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
        setModified(true);
       //  txtApplDate.setEnabled(false);  
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
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panRentReg = new com.see.truetransact.uicomponent.CPanel();
        pan1 = new com.see.truetransact.uicomponent.CPanel();
        lblBuildingNo = new com.see.truetransact.uicomponent.CLabel();
        lblAppNo = new com.see.truetransact.uicomponent.CLabel();
        lblApplnDate = new com.see.truetransact.uicomponent.CLabel();
        txtName = new com.see.truetransact.uicomponent.CTextField();
        lblName = new com.see.truetransact.uicomponent.CLabel();
        lblHouseName = new com.see.truetransact.uicomponent.CLabel();
        lblMobNo = new com.see.truetransact.uicomponent.CLabel();
        txtHouseName = new com.see.truetransact.uicomponent.CTextField();
        txtPlace = new com.see.truetransact.uicomponent.CTextField();
        lblPlace = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        txtCity = new com.see.truetransact.uicomponent.CTextField();
        lblPhNo = new com.see.truetransact.uicomponent.CLabel();
        txtPhNo = new com.see.truetransact.uicomponent.CTextField();
        txtMobNo = new com.see.truetransact.uicomponent.CTextField();
        lblRoomNo = new com.see.truetransact.uicomponent.CLabel();
        txtBuildingNo = new com.see.truetransact.uicomponent.CTextField();
        txtRoomNo = new com.see.truetransact.uicomponent.CTextField();
        btnBuildingNo = new com.see.truetransact.uicomponent.CButton();
        btnRoomNo = new com.see.truetransact.uicomponent.CButton();
        txtAppNo = new com.see.truetransact.uicomponent.CTextField();
        tdtApplnDate = new com.see.truetransact.uicomponent.CDateField();
        pan2 = new com.see.truetransact.uicomponent.CPanel();
        lblEmailId = new com.see.truetransact.uicomponent.CLabel();
        lblGuardian = new com.see.truetransact.uicomponent.CLabel();
        lblOccDate = new com.see.truetransact.uicomponent.CLabel();
        txtEmailId = new com.see.truetransact.uicomponent.CTextField();
        txtGuardian = new com.see.truetransact.uicomponent.CTextField();
        txtNominee = new com.see.truetransact.uicomponent.CTextField();
        lblNominee = new com.see.truetransact.uicomponent.CLabel();
        tdtOccDate = new com.see.truetransact.uicomponent.CDateField();
        tdtCommDate = new com.see.truetransact.uicomponent.CDateField();
        lblCommDate = new com.see.truetransact.uicomponent.CLabel();
        lblRecommBy = new com.see.truetransact.uicomponent.CLabel();
        lblAgreNo = new com.see.truetransact.uicomponent.CLabel();
        lblAgrDate = new com.see.truetransact.uicomponent.CLabel();
        txtRecommBy = new com.see.truetransact.uicomponent.CTextField();
        txtAgreNo = new com.see.truetransact.uicomponent.CTextField();
        tdtAgrDate = new com.see.truetransact.uicomponent.CDateField();
        pan3 = new com.see.truetransact.uicomponent.CPanel();
        lblRentAmt = new com.see.truetransact.uicomponent.CLabel();
        lblRentDate = new com.see.truetransact.uicomponent.CLabel();
        lblPenalGrPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblAdvAmt = new com.see.truetransact.uicomponent.CLabel();
        lblAdvDetails = new com.see.truetransact.uicomponent.CLabel();
        txtRentAmt = new com.see.truetransact.uicomponent.CTextField();
        txtAdvAmt = new com.see.truetransact.uicomponent.CTextField();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        txtAdvDetails = new com.see.truetransact.uicomponent.CTextArea();
        cboPenalGrPeriod = new com.see.truetransact.uicomponent.CComboBox();
        cboRentDate = new com.see.truetransact.uicomponent.CComboBox();
        panTrans = new com.see.truetransact.uicomponent.CPanel();
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

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(860, 665));
        setMinimumSize(new java.awt.Dimension(860, 665));
        setPreferredSize(new java.awt.Dimension(860, 665));
        getContentPane().setLayout(null);

        tbrSale.setMaximumSize(new java.awt.Dimension(367, 30));
        tbrSale.setMinimumSize(new java.awt.Dimension(367, 30));
        tbrSale.setPreferredSize(new java.awt.Dimension(367, 30));

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

        getContentPane().add(tbrSale);
        tbrSale.setBounds(0, 0, 840, 30);

        panStatus.setMinimumSize(new java.awt.Dimension(150, 22));
        panStatus.setPreferredSize(new java.awt.Dimension(150, 22));
        panStatus.setLayout(new java.awt.GridBagLayout());

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panStatus.add(lblStatus, gridBagConstraints);

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panStatus.add(lblSpace, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 500;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus);
        panStatus.setBounds(0, 555, 792, 22);

        panRentReg.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panRentReg.setMaximumSize(new java.awt.Dimension(850, 300));
        panRentReg.setMinimumSize(new java.awt.Dimension(850, 300));
        panRentReg.setPreferredSize(new java.awt.Dimension(850, 300));
        panRentReg.setLayout(new java.awt.GridBagLayout());

        pan1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        pan1.setMaximumSize(new java.awt.Dimension(250, 275));
        pan1.setMinimumSize(new java.awt.Dimension(250, 275));
        pan1.setPreferredSize(new java.awt.Dimension(250, 275));
        pan1.setLayout(new java.awt.GridBagLayout());

        lblBuildingNo.setText("Building No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(lblBuildingNo, gridBagConstraints);

        lblAppNo.setText("Application No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(lblAppNo, gridBagConstraints);

        lblApplnDate.setText("Application Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(lblApplnDate, gridBagConstraints);

        txtName.setAllowAll(true);
        txtName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(txtName, gridBagConstraints);

        lblName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(lblName, gridBagConstraints);

        lblHouseName.setText("House Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(lblHouseName, gridBagConstraints);

        lblMobNo.setText("Mobile Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(lblMobNo, gridBagConstraints);

        txtHouseName.setAllowAll(true);
        txtHouseName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtHouseName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHouseNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(txtHouseName, gridBagConstraints);

        txtPlace.setAllowAll(true);
        txtPlace.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(txtPlace, gridBagConstraints);

        lblPlace.setText("Place");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(lblPlace, gridBagConstraints);

        lblCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(lblCity, gridBagConstraints);

        txtCity.setAllowAll(true);
        txtCity.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(txtCity, gridBagConstraints);

        lblPhNo.setText("Phone Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(lblPhNo, gridBagConstraints);

        txtPhNo.setAllowAll(true);
        txtPhNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(txtPhNo, gridBagConstraints);

        txtMobNo.setAllowAll(true);
        txtMobNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(txtMobNo, gridBagConstraints);

        lblRoomNo.setText("Room Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(lblRoomNo, gridBagConstraints);

        txtBuildingNo.setBackground(new java.awt.Color(220, 220, 220));
        txtBuildingNo.setEditable(false);
        txtBuildingNo.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 86;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(txtBuildingNo, gridBagConstraints);

        txtRoomNo.setBackground(new java.awt.Color(220, 220, 220));
        txtRoomNo.setEditable(false);
        txtRoomNo.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 86;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(txtRoomNo, gridBagConstraints);

        btnBuildingNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBuildingNo.setToolTipText("Search");
        btnBuildingNo.setEnabled(false);
        btnBuildingNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuildingNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        pan1.add(btnBuildingNo, gridBagConstraints);

        btnRoomNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnRoomNo.setToolTipText("Search");
        btnRoomNo.setEnabled(false);
        btnRoomNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRoomNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        pan1.add(btnRoomNo, gridBagConstraints);

        txtAppNo.setAllowAll(true);
        txtAppNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(txtAppNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan1.add(tdtApplnDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panRentReg.add(pan1, gridBagConstraints);

        pan2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        pan2.setMaximumSize(new java.awt.Dimension(250, 275));
        pan2.setMinimumSize(new java.awt.Dimension(250, 275));
        pan2.setPreferredSize(new java.awt.Dimension(250, 275));
        pan2.setLayout(new java.awt.GridBagLayout());

        lblEmailId.setText("Email ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan2.add(lblEmailId, gridBagConstraints);

        lblGuardian.setText("Guardian");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan2.add(lblGuardian, gridBagConstraints);

        lblOccDate.setText("Occupied Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan2.add(lblOccDate, gridBagConstraints);

        txtEmailId.setAllowAll(true);
        txtEmailId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEmailId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan2.add(txtEmailId, gridBagConstraints);

        txtGuardian.setAllowAll(true);
        txtGuardian.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan2.add(txtGuardian, gridBagConstraints);

        txtNominee.setAllowAll(true);
        txtNominee.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan2.add(txtNominee, gridBagConstraints);

        lblNominee.setText("Nominee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan2.add(lblNominee, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan2.add(tdtOccDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan2.add(tdtCommDate, gridBagConstraints);

        lblCommDate.setText("Committe Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan2.add(lblCommDate, gridBagConstraints);

        lblRecommBy.setText("Recommended by");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan2.add(lblRecommBy, gridBagConstraints);

        lblAgreNo.setText("Agreement No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan2.add(lblAgreNo, gridBagConstraints);

        lblAgrDate.setText("Agreement Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 68, 2);
        pan2.add(lblAgrDate, gridBagConstraints);

        txtRecommBy.setAllowAll(true);
        txtRecommBy.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan2.add(txtRecommBy, gridBagConstraints);

        txtAgreNo.setAllowAll(true);
        txtAgreNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pan2.add(txtAgreNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 68, 2);
        pan2.add(tdtAgrDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panRentReg.add(pan2, gridBagConstraints);

        pan3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        pan3.setMaximumSize(new java.awt.Dimension(250, 275));
        pan3.setMinimumSize(new java.awt.Dimension(250, 275));
        pan3.setPreferredSize(new java.awt.Dimension(250, 275));
        pan3.setLayout(new java.awt.GridBagLayout());

        lblRentAmt.setText("Rent Amount");
        lblRentAmt.setMaximumSize(new java.awt.Dimension(90, 16));
        lblRentAmt.setMinimumSize(new java.awt.Dimension(94, 16));
        lblRentAmt.setPreferredSize(new java.awt.Dimension(94, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 3);
        pan3.add(lblRentAmt, gridBagConstraints);

        lblRentDate.setText("Rent Date");
        lblRentDate.setMaximumSize(new java.awt.Dimension(94, 16));
        lblRentDate.setMinimumSize(new java.awt.Dimension(94, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 3);
        pan3.add(lblRentDate, gridBagConstraints);

        lblPenalGrPeriod.setText("Penal Grace Period");
        lblPenalGrPeriod.setMaximumSize(new java.awt.Dimension(130, 16));
        lblPenalGrPeriod.setPreferredSize(new java.awt.Dimension(130, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 3);
        pan3.add(lblPenalGrPeriod, gridBagConstraints);

        lblAdvAmt.setText("Advance Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 3);
        pan3.add(lblAdvAmt, gridBagConstraints);

        lblAdvDetails.setText("Advance Acc details");
        lblAdvDetails.setMaximumSize(new java.awt.Dimension(140, 16));
        lblAdvDetails.setPreferredSize(new java.awt.Dimension(140, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 100, 3);
        pan3.add(lblAdvDetails, gridBagConstraints);
        lblAdvDetails.getAccessibleContext().setAccessibleName("Advance Account details");

        txtRentAmt.setAllowAll(true);
        txtRentAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRentAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRentAmtActionPerformed(evt);
            }
        });
        txtRentAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRentAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pan3.add(txtRentAmt, gridBagConstraints);

        txtAdvAmt.setAllowAll(true);
        txtAdvAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAdvAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAdvAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pan3.add(txtAdvAmt, gridBagConstraints);

        cScrollPane1.setViewportView(txtAdvDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 87;
        gridBagConstraints.ipady = 26;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 100, 3);
        pan3.add(cScrollPane1, gridBagConstraints);

        cboPenalGrPeriod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1Select", "1", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        cboPenalGrPeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pan3.add(cboPenalGrPeriod, gridBagConstraints);

        cboRentDate.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        cboRentDate.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRentDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRentDateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pan3.add(cboRentDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panRentReg.add(pan3, gridBagConstraints);

        getContentPane().add(panRentReg);
        panRentReg.setBounds(0, 35, 846, 290);

        panTrans.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTrans.setMaximumSize(new java.awt.Dimension(850, 230));
        panTrans.setMinimumSize(new java.awt.Dimension(850, 230));
        panTrans.setPreferredSize(new java.awt.Dimension(850, 230));
        getContentPane().add(panTrans);
        panTrans.setBounds(0, 325, 846, 230);

        mbrTokenConfig.setInheritsPopupMenu(true);

        mnuProcess.setText("Rent Register");
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

    private void txtNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNameFocusLost
        // TODO add your handling code here:
        double adAmt=0;
        double reAmt=0;
        if(txtAdvAmt.getText().length()>0){
            adAmt=Double.parseDouble(txtAdvAmt.getText());
        }
        if(txtRentAmt.getText().length()>0){
            reAmt=Double.parseDouble(txtRentAmt.getText());
        }
        transactionUI.setCallingAmount(String.valueOf(adAmt+reAmt));
        transactionUI.setCallingApplicantName(txtName.getText());
        transactionUI.setCallingTransType("TRANSFER");
        double amountRent = CommonUtil.convertObjToDouble(txtRentAmt.getText()).doubleValue();
        amountRent = amountRent + totalAmount;
    }//GEN-LAST:event_txtNameFocusLost

    private void txtRentAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRentAmtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRentAmtActionPerformed

    private void btnRoomNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRoomNoActionPerformed
        // TODO add your handling code here:
            callView("ROOMNUM");
    }//GEN-LAST:event_btnRoomNoActionPerformed

    private void btnBuildingNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuildingNoActionPerformed
        // TODO add your handling code here:
           callView("BUILDING_NUM");
    }//GEN-LAST:event_btnBuildingNoActionPerformed

    private void txtRentAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRentAmtFocusLost
        // TODO add your handling code here:
         double adAmt=0;
        double reAmt=0;
        if(txtAdvAmt.getText().length()>0){
            adAmt=Double.parseDouble(txtAdvAmt.getText());
        }
        if(txtRentAmt.getText().length()>0){
            reAmt=Double.parseDouble(txtRentAmt.getText());
        }
        transactionUI.setCallingAmount(String.valueOf(adAmt+reAmt));
        transactionUI.setCallingApplicantName(txtName.getText());
        transactionUI.setCallingTransType("TRANSFER");
        double amountRent = CommonUtil.convertObjToDouble(txtRentAmt.getText()).doubleValue();
        amountRent = amountRent + totalAmount;
        
    }//GEN-LAST:event_txtRentAmtFocusLost

    private void txtEmailIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailIdActionPerformed

    private void txtHouseNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHouseNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHouseNameActionPerformed

    
     public static void main(String args[]) throws Exception {
          try {
      //  frmSale objIfrRenewal=new frmSale();
    //    objIfrRenewal.setVisible(true);
         }catch (Exception e){
            e.printStackTrace();
        }
    }
     
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
       cifClosingAlert();
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
      txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
       //  txtApplDate.setEnabled(false);  
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
      //   txtApplDate.setEnabled(false);  
          btnBuildingNo.setEnabled(false);
         btnRoomNo.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
       //  txtApplDate.setEnabled(false);  
          btnBuildingNo.setEnabled(false);
         btnRoomNo.setEnabled(false);
     //    txtSalesmanID.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
  public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID,TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put(CommonConstants.HIERARCHY_ID,ProxyParameters.HIERARCHY_ID);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getRentRegisterAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeSale");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            AuthorizeStatusUI fdf=new AuthorizeStatusUI(this,mapParam);
            authorizeUI.show();
            observable.setStatus();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID,TrueTransactMain.USER_ID);//commented on 13-Feb-2012
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("RRID", observable.getRRId());
            singleAuthorizeMap.put("RTID", rtId);
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
            observable.setAuthMap(singleAuthorizeMap); 
            observable.execute(authorizeStatus);             
           // System.out.println("IOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            //ClientUtil.execute("authorizeRentRegister", singleAuthorizeMap);
           // ClientUtil.ex
            viewType = "";
       //     super.setOpenForEditBy(observable.getStatusBy());
      //      super.removeEditLock(txtTokenConfigId.getText());
            btnCancelActionPerformed(null);
            lblStatus.setText(authorizeStatus);
            rtId = "";
        }
           txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
       //  txtApplDate.setEnabled(false);  
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        removeEditLock(txtBuildingNo.getText());
        setModified(false);
          observable.resetForm();
        //txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panRentReg, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        lblStatus.setText("");
        setModified(false);
        viewType = "";
//        btnAuthorize.setEnabled(true);
//        btnReject.setEnabled(true);
//        btnException.setEnabled(true);
          transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        transactionUI.setCallingApplicantName("");
         txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
       //  txtApplDate.setEnabled(false);  
          btnBuildingNo.setEnabled(false);
         btnRoomNo.setEnabled(false);
       
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        setModified(false);
         final String mandatoryMessage = checkMandatory(panRentReg);
        StringBuffer message = new StringBuffer(mandatoryMessage);
      
         if(txtBuildingNo.getText().equals(""))
        {
            message.append(objMandatoryRB.getString("txtBuildingNo"));
        }
        
        if(txtRoomNo.getText().equals(""))
        {
            System.out.println("strAppNo909990890889808908");
            message.append(objMandatoryRB.getString("txtRoomNo"));
        }
        String strAppNo = txtAppNo.getText().trim();
         System.out.println("strAppNo909990890889808908"+strAppNo);
        if(strAppNo.equals(""))
        {
            System.out.println("status saveAction:============= "+message.length());
            System.out.println("strAppN++++++++++++++++++++"+strAppNo);
                message.append(objMandatoryRB.getString("txtAppNo"));
                System.out.println("status saveAction:============= "+message.length());
        }
        if(!strAppNo.equals("") && cm.getSplCharsWithoutCheck(txtAppNo.getText()))
        {
            message.append(objMandatoryRB.getString("splCharsWCheck1")); 
            txtAppNo.requestFocus();
            txtAppNo.setText("");
        }
         if(tdtApplnDate.getDateValue().equals(""))
        {
                message.append(objMandatoryRB.getString("txtApplDate"));
         }
        String strName=txtName.getText().trim();
        if(strName.equals(""))
        {
             message.append(objMandatoryRB.getString("txtName"));
        }
         if(!strName.equals("") && cm.getSplCharsWithoutCheck(strName))
        {
            message.append(objMandatoryRB.getString("splCharsWCheck2")); 
            txtName.requestFocus();
            txtName.setText("");
        }
        String strHouseName=txtHouseName.getText().trim();
        if(strHouseName.equals(""))
        {
                message.append(objMandatoryRB.getString("txtHouseName"));
        }
         if(!strHouseName.equals("") && cm.getSplCharsWithoutCheck(strHouseName))
        {
            message.append(objMandatoryRB.getString("splCharsWCheck3")); 
            txtHouseName.requestFocus();
            txtHouseName.setText("");
        }
        String strPlace=txtPlace.getText().trim();
      if(strPlace.equals(""))
        {
                message.append(objMandatoryRB.getString("txtPlace"));
        }
        if(!strPlace.equals("") && cm.getSplCharsWithoutCheck(strPlace))
        {
            message.append(objMandatoryRB.getString("splCharsWCheck4")); 
            txtPlace.requestFocus();
            txtPlace.setText("");
        }
        String strCity=txtCity.getText().trim();
         if(txtCity.getText().equals(""))
        {
                message.append(objMandatoryRB.getString("txtCity"));
        }
        if(!strCity.equals("") && cm.getSplCharsWithoutCheck(strCity))
        {
            message.append(objMandatoryRB.getString("splCharsWCheck5")); 
            txtCity.requestFocus();
            txtCity.setText("");
        }
        String strphNo=txtPhNo.getText().trim();
//         if(strphNo.equals(""))
//        {
//                message.append(objMandatoryRB.getString("txtPhNo"));
//        }
        String strMobNo=txtMobNo.getText().trim();
         if(strMobNo.equals(""))
        {
                message.append(objMandatoryRB.getString("txtMobNo"));
        }
        String strEmailId=txtEmailId.getText().trim();
//         if(strEmailId.equals(""))
//        {
//                message.append(objMandatoryRB.getString("txtEmailId"));
//        }
        String strGuardian=txtGuardian.getText().trim();
//         if(strGuardian.equals(""))
//        {
//                message.append(objMandatoryRB.getString("txtGuardian"));
//        }
        String strNominee=txtNominee.getText().trim();
//         if(strNominee.equals(""))
//        {
//                message.append(objMandatoryRB.getString("txtNominee"));
//        }
        
         if(tdtOccDate.getDateValue().equals(""))
        {
                message.append(objMandatoryRB.getString("tdtOccDate"));
        }
         if(tdtCommDate.getDateValue().equals(""))
        {
                message.append(objMandatoryRB.getString("tdtCommDate"));
        }
        String strRecommBy=txtRecommBy.getText().trim();
         if(strRecommBy.equals(""))
        {
                message.append(objMandatoryRB.getString("txtRecommBy"));
        }
        String strAgreNo=txtAgreNo.getText().trim();
         if(strAgreNo.equals(""))
        {
                message.append(objMandatoryRB.getString("txtAgreNo"));
        }
         if(tdtAgrDate.getDateValue().equals(""))
        {
                message.append(objMandatoryRB.getString("tdtAgrDate"));
        }
        String strRentAmt=txtRentAmt.getText().trim();
         if(strRentAmt.equals(""))
        {
                message.append(objMandatoryRB.getString("txtRentAmt"));
        }
        
          if(cboRentDate.getSelectedItem().equals("Select"))
        {
                message.append(objMandatoryRB.getString("cboRentDate"));
        }
          if(cboPenalGrPeriod.getSelectedItem().equals("Select"))
        {
                message.append(objMandatoryRB.getString("cboPenalGrPeriod"));
        }
        String strAdvAmt=txtAdvAmt.getText().trim();
//          if(strAdvAmt.equals(""))
//        {
//                message.append(objMandatoryRB.getString("txtAdvAmt"));
//        }
        String straAdvDetails=txtAdvDetails.getText().trim();
//          if(straAdvDetails.equals(""))
//        {
//                message.append(objMandatoryRB.getString("txtaAdvDetails"));
//        }
        if(strphNo!=null && !strphNo.equals(""))
        {
             if(!checkNumber(strphNo))
         {
              message.append(objMandatoryRB.getString("txtPhNoNum"));
         }
        }
        if(strMobNo!=null && !strMobNo.equals(""))
        {
         if(!checkNumber(strMobNo))
         {
              message.append(objMandatoryRB.getString("txtMobNoNum"));
         }
        }
        String email=txtEmailId.getText().trim();  
        if(email!=null && !email.equals(""))
        {
            if(!cm.ValidEmailAddress(email))      
        {
          message.append(objMandatoryRB.getString("validEmail"));  
        }
        }
        //setExpDateOnCalculation();
      //  System.out.println("status saveAction: "+status);
        System.out.println("status saveAction:============= "+message.length());
        if(message.length() > 0 )
        {
            
            displayAlert(message.toString());
            return;
       }
        
//          if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)
//           {
//                               int transactionSize = 0 ;
//                if(transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(txtRentAmt.getText()).doubleValue() > 0){
//                    System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
//                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
//                                        return;
//                }else {
//                       if(CommonUtil.convertObjToDouble(txtRentAmt.getText()).doubleValue()>0){
//                        transactionSize = (transactionUI.getOutputTO()).size();
//                        if(transactionSize != 1 && CommonUtil.convertObjToDouble(txtRentAmt.getText()).doubleValue()>0){
//                            ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction") ;
//                            return;
//                        }else{
//                              observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO()); 
//                     }
//                    }else if(transactionUI.getOutputTO().size()>0){
//                         observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
//                    }
//                }
//                if(transactionSize == 0 && CommonUtil.convertObjToDouble(txtRentAmt.getText()).doubleValue()>0){
//                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
//                    return;
//                }else if(transactionSize != 0 ){
//                    if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
//                       ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
//                        return;
//                    }
//                    if(transactionUI.getOutputTO().size()>0){
//                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
//                    }
//                }  
//          }
        if (transactionUI.getOutputTO().size() > 0) {
            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
        }
           savePerformed(); 
        
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed(){
       
       // System.out.println("IN savePerformed");
        String action;
          System.out.println("IN observable.getActionType(): "+observable.getActionType());
           System.out.println("IN ClientConstants.ACTIONTYPE_NEW: "+ClientConstants.ACTIONTYPE_NEW);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ){
             System.out.println("IN savePerformed ACTIONTYPE_NEW"); 
                     
            action=CommonConstants.TOSTATUS_INSERT;
           saveAction(action);
          
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
          action=CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            action=CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
        txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
      //   txtApplDate.setEnabled(false);  
    }
    public boolean checkNumber(String value)
    {
        //String amtRentIn=amountRentText.getText();
        boolean incorrect = true;
        try
        {
            Double.parseDouble(value);
            return true;
        }
        catch(NumberFormatException nfe)
        {
          return false;
        }
       // return 
    }
   
        /* Calls the execute method of TerminalOB to do insertion or updation or deletion */
    private void saveAction(String status)
    {
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        System.out.println("status saveAction909990890889808908: "+status);
//       txtAmtBorrowed.
       
       
      
                updateOBFields();
            observable.execute(status);
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED)
            {
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
               lst.add("RRID");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if(observable.getProxyReturnMap()!=null && observable.getProxyReturnMap().containsKey("INSUFFICIENT_BALANCE")){
                 System.out.println("inside insufficient balance");   
                 ClientUtil.showAlertWindow("Available Balance is Insufficient to process your Transaction");
                return;
                }
                if (observable.getProxyReturnMap()!=null) 
                 {
                    if (observable.getProxyReturnMap().containsKey("CONFIG_ID")) {
                        lockMap.put("CONFIG_ID", observable.getProxyReturnMap().get("CONFIG_ID"));
                    }
                    displayTransDetail(observable.getProxyReturnMap());
                }
                if (status==CommonConstants.TOSTATUS_UPDATE) {
                    lockMap.put("RRID", observable.getRRId());
                }
        
                settings();
            }
        
        
    }
     private void displayTransDetail(HashMap proxyResultMap) {
        try{ 
	        System.out.println("@#$@@$@@@$ proxyResultMap : " +proxyResultMap);
	        String cashDisplayStr = "Cash Transaction Details...\n";
	        String transferDisplayStr = "Transfer Transaction Details...\n";
	        String displayStr = "";
	        String transId = "";
	        String transType = "";
	        Object keys[] = proxyResultMap.keySet().toArray();
	        int cashCount = 0;
	        int transferCount = 0;
	        List tempList = null;
	        HashMap transMap = null;
	        String actNum = "";
	        for (int i=0; i<keys.length; i++) {
	            if (proxyResultMap.get(keys[i]) instanceof String) {
	                continue;
	            }
	           
	            tempList = (List)proxyResultMap.get(keys[i]);
	            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH")!=-1) {
	                for (int j=0; j<tempList.size(); j++) {
	                    transMap = (HashMap) tempList.get(j);
	                    if (j==0) {
	                        transId = (String)transMap.get("SINGLE_TRANS_ID");
	                    }
	                    cashDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
	                    "   Trans Type : "+transMap.get("TRANS_TYPE");
	                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
	                    if(actNum != null && !actNum.equals("")){
	                        cashDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
	                        "   Amount : "+transMap.get("AMOUNT")+"\n";
	                    }else{
	                        cashDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
	                        "   Amount : "+transMap.get("AMOUNT")+"\n";
	                    }
	                }
	                cashCount++;
	            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER")!=-1) {
	                for (int j=0; j<tempList.size(); j++) {
	                    transMap = (HashMap) tempList.get(j);
	                    if (j==0) {
	                        transId = (String)transMap.get("SINGLE_TRANS_ID");
	                    }
	                    transferDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
	                    "   Batch Id : "+transMap.get("BATCH_ID")+
	                    "   Trans Type : "+transMap.get("TRANS_TYPE");
	                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
	                    if(actNum != null && !actNum.equals("")){
	                        transferDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
	                        "   Amount : "+transMap.get("AMOUNT")+"\n";
	                    }else{
	                        transferDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
	                        "   Amount : "+transMap.get("AMOUNT")+"\n";
	                    }
	                }
	                transferCount++;
	            }
	        }
	        if(cashCount>0){
	            displayStr+=cashDisplayStr;
	        } 
	        if(transferCount>0){
	            displayStr+=transferDisplayStr;
	        }
	        if((displayStr==null) || (displayStr.equals("")))
	        {
	            System.out.println("aaaaaaaaaaaaaaaaaaaaaa");
	            System.out.println("displayStr========="+displayStr);
	        ClientUtil.showMessageWindow(""+displayStr+"Success!!!");
	        }
	        else
	        {
	            System.out.println("dddddddddddddddddd");
	         ClientUtil.showMessageWindow(""+displayStr);   
	        }
	        int yesNo = 0;
	        String[] options = {"Yes", "No"};
	        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
	        null, options, options[0]);
	        System.out.println("#$#$$ yesNo : "+yesNo);
	        if (yesNo==0) {
	            TTIntegration ttIntgration = null;
	            HashMap paramMap = new HashMap();
	            paramMap.put("TransId", transId);
	            paramMap.put("TransDt", observable.getCurrDt()); 
	            
	            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
	            ttIntgration.setParam(paramMap);
	//            if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
	//                ttIntgration.integrationForPrint("ReceiptPayment");
	//            } else {
	                ttIntgration.integrationForPrint("ReceiptPayment", false);
	//            }
	        }
        }catch(Exception e){
        	e.printStackTrace();
        }    
    }
        /** Method used to check whether the Mandatory Fields in the Form are Filled or not */
    private String checkMandatory(javax.swing.JComponent component)
    {
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
        removeEditLock(txtBuildingNo.getText());
        observable.resetForm();
      //  txtNoOfTokens.setText("");
       ClientUtil.clearAll(this);
       ClientUtil.enableDisable(panRentReg, false);
       setButtonEnableDisable();
       observable.setResultStatus();
       txtBuildingNo.setEnabled(false);
       txtRoomNo.setEnabled(false);
       // txtApplDate.setEnabled(false);  
    }
    public Date getDateValue(String date1)
    {
         DateFormat formatter ; 
         Date date=null ; 
        try 
        {  
            //   System.out.println("date1 66666666666=========:"+date1);  
           // String str_date=date1;
          //  formatter = new SimpleDateFormat("MM/dd/yyyy");
          //  date = (Date)formatter.parse(str_date);  
          //      System.out.println("dateAFETRRR 66666666666=========:"+date); 
                
                
                
                
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
                   // String s1 = "2008-03-30T15:30:00.000+02:00";
                   // date1 = date1.substring(0, date1.indexOf('.'));
                  //  System.out.println("Result==> "+sdf2.format(sdf1.parse(date1)));
                  date=new Date(sdf2.format(sdf1.parse(date1)));
                 System.out.println("date IOOOOOOO==> "+date);
        }
        catch (ParseException e)
        {
           System.out.println("Error in getDateValue():"+e);
        }  
            return date;
       }
        public void updateOBFields() {
            //observable.setRRId()
            observable.setTxtAppNo(txtAppNo.getText());
            observable.setTxtName(txtName.getText());
            observable.setTxtHouseName(txtHouseName.getText());
            observable.setTxtPlace(txtPlace.getText());
            observable.setTxtCity(txtCity.getText());
            observable.setTxtPhNo(txtPhNo.getText());
            observable.setTxtMobNo(txtMobNo.getText());
            observable.setTxtEmailId(txtEmailId.getText());
            observable.setTxtGuardian(txtGuardian.getText());
            observable.setTxtNominee(txtNominee.getText());
            observable.setTxtRecommBy(txtRecommBy.getText());
            observable.setTxtAgreNo(txtAgreNo.getText());
            observable.setTxtPenalGrPeriod(cboPenalGrPeriod.getSelectedItem().toString());
            observable.setTxtaAdvDetails(txtAdvDetails.getText() );
            observable.setTxtApplDate(getDateValue(tdtApplnDate.getDateValue()));
            observable.setTdtOccDate(getDateValue(tdtOccDate.getDateValue()));
            observable.setTdtCommDate(getDateValue(tdtCommDate.getDateValue()));
            observable.setTdtAgrDate(getDateValue(tdtAgrDate.getDateValue()));
            observable.setCboRentDate(cboRentDate.getSelectedItem().toString());
            observable.setTxtRentAmt(CommonUtil.convertObjToDouble(txtRentAmt.getText()));
            observable.setTxtAdvAmt(CommonUtil.convertObjToDouble(txtAdvAmt.getText()));
            observable.setCboRoomNo(txtRoomNo.getText());
            observable.setCboBuildingNo(txtBuildingNo.getText());
                observable.setTxtRmNumber(txtRmNumber);
             observable.setModule(getModule());
            observable.setScreen(getScreen());
            observable.setModule(getModule());
            observable.setScreen(getScreen());
            txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
      //  txtApplDate.setEnabled(false); 
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView( ClientConstants.ACTION_STATUS[3]);
         transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
         txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
      //  txtApplDate.setEnabled(false); 
         btnBuildingNo.setEnabled(false);
         btnRoomNo.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
       //transactionUI.cancelAction(false);
       
       // setButtonEnableDisable();
        
        //////////////////////////////***********************
//         transactionUI.setButtonEnableDisable(true);
//        transactionUI.cancelAction(false);
//        transactionUI.resetObjects();
        
//        btnAuthorize.setEnabled(false);
//        btnReject.setEnabled(false);
//        btnException.setEnabled(false);
        txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
        
       // txtApplDate.setEnabled(false); 
         btnBuildingNo.setEnabled(true);
         btnRoomNo.setEnabled(true);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
         observable.resetForm();
        //txtNoOfTokens.setText("");
        ClientUtil.enableDisable(panRentReg, true);
      
        // System.out.println("btnNewActionPerformed ACTION STA: "+ClientConstants.ACTIONTYPE_NEW);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
         setButtonEnableDisable();
         transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
       
//        btnAuthorize.setEnabled(false);
//        btnReject.setEnabled(false);
//        btnException.setEnabled(false);
//        btnSave.setEnabled(true);
        txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
      //  txtApplDate.setEnabled(false); 
         btnBuildingNo.setEnabled(true);
         btnRoomNo.setEnabled(true);
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
      btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
      //  txtApplDate.setEnabled(false);
           tdtApplnDate.setDateValue(appLoginDate);
           tdtOccDate.setDateValue(appLoginDate);
           tdtCommDate.setDateValue(appLoginDate);
           tdtAgrDate.setDateValue(appLoginDate);
    //    txtApplDate.setEnabled(false); 
        btnBuildingNo.setEnabled(false);
         btnRoomNo.setEnabled(false);
    }
    private void mnuProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuProcessActionPerformed

private void txtAdvAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAdvAmtFocusLost
// TODO add your handling code here:
     double adAmt=0;
        double reAmt=0;
        if(txtAdvAmt.getText().length()>0){
            adAmt=Double.parseDouble(txtAdvAmt.getText());
        }
        if(txtRentAmt.getText().length()>0){
            reAmt=Double.parseDouble(txtRentAmt.getText());
        }
        transactionUI.setCallingAmount(String.valueOf(adAmt+reAmt));
        transactionUI.setCallingApplicantName(txtName.getText());
        transactionUI.setCallingTransType("TRANSFER");
        double amountRent = CommonUtil.convertObjToDouble(txtRentAmt.getText()).doubleValue();
        amountRent = amountRent + totalAmount;
}//GEN-LAST:event_txtAdvAmtFocusLost

    private void cboRentDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRentDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboRentDateActionPerformed
  
      
     public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        
        mandatoryMap.put("txtBuildingNo", new Boolean(true));
        mandatoryMap.put("txtRoomNo", new Boolean(true));
        mandatoryMap.put("txtAppNo", new Boolean(true));
      //  mandatoryMap.put("txtBorrowingNo", new Boolean(true));
        mandatoryMap.put("tdtApplDate", new Boolean(true));
        mandatoryMap.put("txtName", new Boolean(true));
        mandatoryMap.put("txtHouseName", new Boolean(true));
       mandatoryMap.put("txtPlace", new Boolean(true));
       mandatoryMap.put("txtCity", new Boolean(true));
//       mandatoryMap.put("txtPhNo", new Boolean(true)); 
       mandatoryMap.put("txtMobNo", new Boolean(true));
//       mandatoryMap.put("txtEmailId", new Boolean(true));
//       mandatoryMap.put("txtGuardian", new Boolean(true));
//       mandatoryMap.put("txtNominee", new Boolean(true));
       mandatoryMap.put("tdtOccDate", new Boolean(true));
       mandatoryMap.put("tdtCommDate", new Boolean(true));
       mandatoryMap.put("txtRecommBy", new Boolean(true));
       mandatoryMap.put("txtAgreNo", new Boolean(true));
       mandatoryMap.put("tdtAgrDate", new Boolean(true));
       mandatoryMap.put("txtRentAmt", new Boolean(true));
       mandatoryMap.put("tdtRentDate", new Boolean(true));
       mandatoryMap.put("txtPenalGrPeriod", new Boolean(true));
//       mandatoryMap.put("txtAdvAmt", new Boolean(true));
//       mandatoryMap.put("txtAdvDetails", new Boolean(true));
      mandatoryMap.put("cboRentDate", new Boolean(true)); 
      mandatoryMap.put("cboPenalGrPeriod", new Boolean(true)); 
      
        
    }
    public java.util.HashMap getMandatoryHashMap() {
           return mandatoryMap;
    }    
    public String getDtPrintValue(String strDate)
    {
         try
         {
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
         catch(Exception e)
         {
            e.printStackTrace(); 
         }
        return strDate;
    }
   /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        
        System.out.println("VFG CBO BUILDING NO: "+observable.getCboBuildingNo());
         System.out.println("VFG CBO RRRRRRRRRRRRRRR NO: "+observable.getCboRoomNo());
        txtBuildingNo.setText(observable.getCboBuildingNo());
        txtRoomNo.setText(observable.getCboRoomNo());
        txtAppNo.setText(observable.getTxtAppNo());
         if(observable.getTxtApplDate()!=null)
        {
            tdtApplnDate.setDateValue(getDtPrintValue(String.valueOf(observable.getTxtApplDate())));//getDtPrintValue(String.valueOf(
        }
        txtName.setText(observable.getTxtName());
        txtHouseName.setText(observable.getTxtHouseName());
        txtPlace.setText(observable.getTxtPlace());
        txtCity.setText(observable.getTxtCity());
        txtPhNo.setText(observable.getTxtPhNo());
        txtMobNo.setText(observable.getTxtMobNo());
        txtEmailId.setText(observable.getTxtEmailId());
        txtGuardian.setText(observable.getTxtGuardian());
        txtNominee.setText(observable.getTxtNominee());
        if(observable.getTdtOccDate()!=null)
        {
           tdtOccDate.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtOccDate())));
        }
        if(observable.getTdtCommDate()!=null)
        {
           tdtCommDate.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtCommDate())));
        }
        txtRecommBy.setText(observable.getTxtRecommBy());
        txtAgreNo.setText(observable.getTxtAgreNo());
        if(observable.getTdtAgrDate()!=null)
        {
           tdtAgrDate.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtAgrDate())));
        }
         if(observable.getTxtRentAmt()!=null)
        {
        txtRentAmt.setText(String.valueOf(observable.getTxtRentAmt()));
         }
          if(observable.getCboRentDate()!=null)
        {
        cboRentDate.setSelectedItem(observable.getCboRentDate());
          }
        cboPenalGrPeriod.setSelectedItem(observable.getTxtPenalGrPeriod());
        if(observable.getTxtAdvAmt()!=null)
        {
           txtAdvAmt.setText(String.valueOf(observable.getTxtAdvAmt()));
        }
        txtAdvDetails.setText(observable.getTxtaAdvDetails());
    
      
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBuildingNo;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnRoomNo;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CComboBox cboPenalGrPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboRentDate;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblAdvAmt;
    private com.see.truetransact.uicomponent.CLabel lblAdvDetails;
    private com.see.truetransact.uicomponent.CLabel lblAgrDate;
    private com.see.truetransact.uicomponent.CLabel lblAgreNo;
    private com.see.truetransact.uicomponent.CLabel lblAppNo;
    private com.see.truetransact.uicomponent.CLabel lblApplnDate;
    private com.see.truetransact.uicomponent.CLabel lblBuildingNo;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblCommDate;
    private com.see.truetransact.uicomponent.CLabel lblEmailId;
    private com.see.truetransact.uicomponent.CLabel lblGuardian;
    private com.see.truetransact.uicomponent.CLabel lblHouseName;
    private com.see.truetransact.uicomponent.CLabel lblMobNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblNominee;
    private com.see.truetransact.uicomponent.CLabel lblOccDate;
    private com.see.truetransact.uicomponent.CLabel lblPenalGrPeriod;
    private com.see.truetransact.uicomponent.CLabel lblPhNo;
    private com.see.truetransact.uicomponent.CLabel lblPlace;
    private com.see.truetransact.uicomponent.CLabel lblRecommBy;
    private com.see.truetransact.uicomponent.CLabel lblRentAmt;
    private com.see.truetransact.uicomponent.CLabel lblRentDate;
    private com.see.truetransact.uicomponent.CLabel lblRoomNo;
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
    private com.see.truetransact.uicomponent.CPanel pan1;
    private com.see.truetransact.uicomponent.CPanel pan2;
    private com.see.truetransact.uicomponent.CPanel pan3;
    private com.see.truetransact.uicomponent.CPanel panRentReg;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTrans;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CToolBar tbrSale;
    private com.see.truetransact.uicomponent.CDateField tdtAgrDate;
    private com.see.truetransact.uicomponent.CDateField tdtApplnDate;
    private com.see.truetransact.uicomponent.CDateField tdtCommDate;
    private com.see.truetransact.uicomponent.CDateField tdtOccDate;
    private com.see.truetransact.uicomponent.CTextField txtAdvAmt;
    private com.see.truetransact.uicomponent.CTextArea txtAdvDetails;
    private com.see.truetransact.uicomponent.CTextField txtAgreNo;
    private com.see.truetransact.uicomponent.CTextField txtAppNo;
    private com.see.truetransact.uicomponent.CTextField txtBuildingNo;
    private com.see.truetransact.uicomponent.CTextField txtCity;
    private com.see.truetransact.uicomponent.CTextField txtEmailId;
    private com.see.truetransact.uicomponent.CTextField txtGuardian;
    private com.see.truetransact.uicomponent.CTextField txtHouseName;
    private com.see.truetransact.uicomponent.CTextField txtMobNo;
    private com.see.truetransact.uicomponent.CTextField txtName;
    private com.see.truetransact.uicomponent.CTextField txtNominee;
    private com.see.truetransact.uicomponent.CTextField txtPhNo;
    private com.see.truetransact.uicomponent.CTextField txtPlace;
    private com.see.truetransact.uicomponent.CTextField txtRecommBy;
    private com.see.truetransact.uicomponent.CTextField txtRentAmt;
    private com.see.truetransact.uicomponent.CTextField txtRoomNo;
    // End of variables declaration//GEN-END:variables
//     private com.see.truetransact.uicomponent.CTable tblData;
      private com.see.truetransact.clientutil.TableModel tbModel;
}
