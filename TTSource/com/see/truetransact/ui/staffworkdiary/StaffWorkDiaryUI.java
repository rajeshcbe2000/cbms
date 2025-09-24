/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * StaffWorkDiaryUI.java
 *
 * Created on January 20, 2005, 3:03 PM
 */

package com.see.truetransact.ui.staffworkdiary;

/**
 *
 * @author  ashokvijayakumar
 */
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uivalidation.NumericValidation;
import java.util.ResourceBundle;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.DefaultValidation;

import java.util.Observable;
import java.util.Observer;
import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.commonutil.DateUtil;
import java.util.Date.*;
import java.util.*;
import java.text.*;

public class StaffWorkDiaryUI extends CInternalFrame implements Observer, UIMandatoryField{
    
    /** Vairable Declarations */
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.staffworkdiary.StaffWorkDiaryRB", ProxyParameters.LANGUAGE);//Creating Instance For ResourceBundle-TokenConfigRB
    private StaffWorkDiaryOB observable; //Reference for the Observable Class TokenConfigOB
    private StaffWorkDiaryMRB objMandatoryRB = new StaffWorkDiaryMRB();//Instance for the MandatoryResourceBundle
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final String AUTHORIZE="Authorize";//Variable used when btnAuthorize is clicked
    int updateTab=-1;
    private boolean updateMode = false;
    private String iidd="";
    
    /** Creates new form TokenConfigUI */
    public StaffWorkDiaryUI() {
        initForm();
    }
    
    /** Method which is used to initialize the form TokenConfig */
    private void initForm(){
        initComponents();
        setFieldNames();
        observable = new StaffWorkDiaryOB();
        observable.addObserver(this);
        observable.resetForm();
       // initComponentData();
        
        setMandatoryHashMap();
        txtTransSummry.setValidation(new NumericValidation(10,4));
         new MandatoryCheck().putMandatoryMarks(getClass().getName(),panStaffWorkData,getMandatoryHashMap());
       // setMaxLengths();
//        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panEmpTraning);
        ClientUtil.enableDisable(panStaffWork, false);
        setButtonEnableDisable();
       // enableDisablePanButton(false);
      //  enableDisablePanGeneralBodyDetails(false);
       // txtTrainingID.setEnabled(false);
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
       // lblStatus.setText(observable.getLblStatus());
        btnView.setEnabled(!btnView.isEnabled());
        btnEmp.setEnabled(!btnNew.isEnabled());
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
        btnEmp.setName("btnEmp");
       // cboTrainDest.setName("cboTrainDest");
        lblDate.setName("lblDate");
        lblStaffID.setName("lblStaffID");
       lblLoginout.setName("lblLoginout");
       lblTransSummry.setName("lblTransSummry");
        lblRemarks.setName("lblRemarks");
        lblStatus.setName("lblStatus");
        mbrTokenConfig.setName("mbrTokenConfig");
        panStatus.setName("panStatus");
      //  panIndEmpDetails.setName("panIndEmpDetails");
       // panEmpList.setName("panEmpList");
        panStaffWork.setName("panStaffWork");
        panStaffWorkData.setName("panStaffWorkData");
       // tblEmpList.setName("tblEmpList");
        txtStaffID.setName("txtStaffID");
        txtLoginout.setName("txtLoginout");
        tdtDate.setName("tdtDate");
        txtTransSummry.setName("txtTransSummry");
        txaRemarks.setName("txaRemarks");
      
        //tdtDate.setName("tdtDate");
       
    }
     
     
    
     public void updateOBFields() {
         observable.setTxtGeneralBoadyID(iidd);
         System.out.println("IIIIIiiiddd in uupppddataeae"+iidd);
         iidd="";
        observable.settdtDate(tdtDate.getDateValue());
        observable.setTxtStaffId(txtStaffID.getText());
        observable.setTxtLoginout(txtLoginout.getText());
         System.out.println("OOOBB"+ observable.getTxtLoginout()+"UUUIII"+txtLoginout.getText());
        observable.setTxtremarks(txaRemarks.getText());
        observable.setTxtTransSummry(txtTransSummry.getText());
       // observable.setTxtGeneralBoadyID()
     }
     // txtTransSummry
     // txtTransSummry
     //
     
      public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("tdtDate", new Boolean(true));
        mandatoryMap.put("txtStaffID", new Boolean(true));
        mandatoryMap.put("txtLoginout", new Boolean(true));
        mandatoryMap.put("txtTransSummry", new Boolean(true));
        mandatoryMap.put("txaRemarks", new Boolean(false));
//        mandatoryMap.put("txtNoOfTrainees", new Boolean(true));
//        mandatoryMap.put("tdtFrom", new Boolean(true));
//        mandatoryMap.put("tdtTo", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    
    
    
    
     
 /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
//        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
//        btnDelete.setText(resourceBundle.getString("btnDelete"));
//        btnClose.setText(resourceBundle.getString("btnClose"));
//        lblNoOfTokens.setText(resourceBundle.getString("lblNoOfTokens"));
//        btnReject.setText(resourceBundle.getString("btnReject"));
//        btnEdit.setText(resourceBundle.getString("btnEdit"));
//        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
//        btnUserId.setText(resourceBundle.getString("btnUserId"));
//        lblSeriesNo.setText(resourceBundle.getString("lblSeriesNo"));
//        btnException.setText(resourceBundle.getString("btnException"));
//        lblMsg.setText(resourceBundle.getString("lblMsg"));
//        btnNew.setText(resourceBundle.getString("btnNew"));
//        lblEndingTokenNo.setText(resourceBundle.getString("lblEndingTokenNo"));
//        lblStartingTokenNo.setText(resourceBundle.getString("lblStartingTokenNo"));
//        btnSave.setText(resourceBundle.getString("btnSave"));
//        btnCancel.setText(resourceBundle.getString("btnCancel"));
//        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
//        lblTokenType.setText(resourceBundle.getString("lblTokenType"));
//        lbSpace2.setText(resourceBundle.getString("lbSpace2"));
//        lblStatus.setText(resourceBundle.getString("lblStatus"));
//        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
//        btnPrint.setText(resourceBundle.getString("btnPrint"));
//        lblTokenIssueId.setText(resourceBundle.getString("lblTokenIssueId"));
//        lblReceiverId.setText(resourceBundle.getString("lblReceiverId"));
    }

    
    /*Setting model to the combobox cboTokenType  */
//    private void initComponentData() {
//        try{
//            cboTrainDest.setModel(observable.getCbmTrainingDest());
//        }catch(ClassCastException e){
//            parseException.logException(e,true);
//        }
//    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        if(observable.gettdtDate()!=null)
           {
                 tdtDate.setDateValue(getDtPrintValue(String.valueOf(observable.gettdtDate())));
          }
        
        
        //observable.gettdtDate());
        txtStaffID.setText(observable.getTxtStaffId());
        txtTransSummry.setText(observable.getTxtTransSummry());
        txtLoginout.setText(observable.getTxtLoginout());
        txaRemarks.setText(observable.getTxtremarks());
        iidd=observable.getTxtGeneralBoadyID();
          System.out.println("IIIIIiiiddd in uupppddataeae"+iidd+":::::"+observable.getTxtGeneralBoadyID());
          

    }
    
     public String getDtPrintValue(String strDate)
    {
         try
         {
       //      System.out.println("strDate getDtPrintValue99999999999999999999999999999999999999================="+strDate);
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
    
    
    

    
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
//        cboTokenType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTokenType"));
//        cboSeriesNo.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSeriesNo"));
//        txtStartingTokenNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStartingTokenNo"));
//        txtEndingTokenNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndingTokenNo"));
//        txtNoOfTokens.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfTokens"));
//        txtTokenIssueId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTokenIssueId"));
//        txtReceiverId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReceiverId"));
    }
    
    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths(){
//        txtStartingTokenNo.setMaxLength(8);
//        txtEndingTokenNo.setMaxLength(8);
          // txtEmpID.setAllowAll(true);
    }
    
    /*Makes the button Enable or Disable accordingly when usier clicks new,edit or delete buttons */
   
    
    /** Method used to check whether the Mandatory Fields in the Form are Filled or not */
    
    /** Method used to Give a Alert when any Mandatory Field is not filled by the user */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
  
    /** Method to make HelpButton btnUserId enable or disable..accroding to Edit,Delete,New,Save Button Clicked */
    private void setHelpBtnEnableDisable(boolean flag){
//                btnE.setEnabled(flag);
    }

//    private void btnCheck(){
//         btnCancel.setEnabled(true);
//         btnSave.setEnabled(false);
//         btnNew.setEnabled(false);
//         btnDelete.setEnabled(false);
//         btnAuthorize.setEnabled(false);
//         btnReject.setEnabled(false);
//         btnException.setEnabled(false);
//         btnEdit.setEnabled(false);
//     }
    
//    private void enableDisablePanButton(boolean flag){
//        btnEmpDelete.setEnabled(flag);
//        btnEmpSave.setEnabled(flag);
//        btnEmpNew.setEnabled(flag);
//    }
//    private void enableDisablePanGeneralBodyDetails(boolean flag){
//        txtEmpID.setEnabled(flag);
//        btnEmp.setEnabled(flag);
//    }
//    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panStaffWork = new com.see.truetransact.uicomponent.CPanel();
        panStaffWorkData = new com.see.truetransact.uicomponent.CPanel();
        tdtDate = new com.see.truetransact.uicomponent.CDateField();
        lblLoginout = new com.see.truetransact.uicomponent.CLabel();
        lblTransSummry = new com.see.truetransact.uicomponent.CLabel();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        txtLoginout = new com.see.truetransact.uicomponent.CTextField();
        txtTransSummry = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txaRemarks = new com.see.truetransact.uicomponent.CTextArea();
        lblStaffID = new com.see.truetransact.uicomponent.CLabel();
        txtStaffID = new com.see.truetransact.uicomponent.CTextField();
        btnEmp = new com.see.truetransact.uicomponent.CButton();
        tbrTokenConfig = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace57 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace58 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace59 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace60 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace61 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
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

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(800, 625));
        setMinimumSize(new java.awt.Dimension(800, 625));
        setPreferredSize(new java.awt.Dimension(800, 625));

        panStaffWork.setMaximumSize(new java.awt.Dimension(650, 550));
        panStaffWork.setMinimumSize(new java.awt.Dimension(650, 550));
        panStaffWork.setPreferredSize(new java.awt.Dimension(650, 550));
        panStaffWork.setLayout(new java.awt.GridBagLayout());

        panStaffWorkData.setBorder(javax.swing.BorderFactory.createTitledBorder("StaffWorkDiary"));
        panStaffWorkData.setMaximumSize(new java.awt.Dimension(450, 250));
        panStaffWorkData.setMinimumSize(new java.awt.Dimension(450, 250));
        panStaffWorkData.setPreferredSize(new java.awt.Dimension(450, 250));
        panStaffWorkData.setLayout(new java.awt.GridBagLayout());

        tdtDate.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtDate.setName("tdtFromDate");
        tdtDate.setPreferredSize(new java.awt.Dimension(101, 19));
        tdtDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panStaffWorkData.add(tdtDate, gridBagConstraints);

        lblLoginout.setText("Login/Logout Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panStaffWorkData.add(lblLoginout, gridBagConstraints);

        lblTransSummry.setText("Transaction Summary");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panStaffWorkData.add(lblTransSummry, gridBagConstraints);

        lblDate.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panStaffWorkData.add(lblDate, gridBagConstraints);

        txtLoginout.setMaxLength(128);
        txtLoginout.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLoginout.setName("txtCompany");
        txtLoginout.setNextFocusableComponent(txtTransSummry);
        txtLoginout.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panStaffWorkData.add(txtLoginout, gridBagConstraints);

        txtTransSummry.setMaxLength(128);
        txtTransSummry.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTransSummry.setName("txtCompany");
        txtTransSummry.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panStaffWorkData.add(txtTransSummry, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panStaffWorkData.add(lblRemarks, gridBagConstraints);

        txaRemarks.setMaximumSize(new java.awt.Dimension(200, 200));
        txaRemarks.setMinimumSize(new java.awt.Dimension(200, 200));
        txaRemarks.setPreferredSize(new java.awt.Dimension(200, 200));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = -130;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panStaffWorkData.add(txaRemarks, gridBagConstraints);

        lblStaffID.setText("Staff ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panStaffWorkData.add(lblStaffID, gridBagConstraints);

        txtStaffID.setMaxLength(128);
        txtStaffID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtStaffID.setName("txtCompany");
        txtStaffID.setNextFocusableComponent(txtTransSummry);
        txtStaffID.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panStaffWorkData.add(txtStaffID, gridBagConstraints);

        btnEmp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEmp.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnEmp.setMinimumSize(new java.awt.Dimension(25, 25));
        btnEmp.setPreferredSize(new java.awt.Dimension(25, 25));
        btnEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panStaffWorkData.add(btnEmp, gridBagConstraints);

        panStaffWork.add(panStaffWorkData, new java.awt.GridBagConstraints());

        getContentPane().add(panStaffWork, java.awt.BorderLayout.CENTER);

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
        tbrTokenConfig.add(btnView);

        lbSpace3.setText("     ");
        tbrTokenConfig.add(lbSpace3);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnNew);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace56);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnEdit);

        lblSpace57.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace57.setText("     ");
        lblSpace57.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace57);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnDelete);

        lbSpace2.setText("     ");
        tbrTokenConfig.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnSave);

        lblSpace58.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace58.setText("     ");
        lblSpace58.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace58.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace58.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace58);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTokenConfig.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnAuthorize);

        lblSpace59.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace59.setText("     ");
        lblSpace59.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace59.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace59.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace59);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnException);

        lblSpace60.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace60.setText("     ");
        lblSpace60.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace60.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace60.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace60);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnReject);

        lblSpace5.setText("     ");
        tbrTokenConfig.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrTokenConfig.add(btnPrint);

        lblSpace61.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace61.setText("     ");
        lblSpace61.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace61.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace61.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace61);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnClose);

        getContentPane().add(tbrTokenConfig, java.awt.BorderLayout.NORTH);

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

        mnuProcess.setText("Process");

        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptView);

        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrTokenConfig.add(mnuProcess);

        setJMenuBar(mbrTokenConfig);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpActionPerformed
   // observable.setActionType(111);
    callView("EMP");        
    }//GEN-LAST:event_btnEmpActionPerformed

    private void tdtDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDateFocusLost
        // TODO add your handling code here:
//        if(tdtFrom.getDateValue().length()>0)
//        ClientUtil.validateToDate(tdtFrom, DateUtil.getStringDate(ClientUtil.getCurrentDate()));
    }//GEN-LAST:event_tdtDateFocusLost

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
//        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
//        observable.setStatus();
//        lblStatus.setText(observable.getLblStatus());
//        callView("Enquiry");
//        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
                
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
      //..  observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
       //.. authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
      //..  observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
      //..  authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
      //..  setModified(true);
       //.. observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
      //,,  authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
       // btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
       //,, btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
      //,,  cifClosingAlert();
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
       //,, cifClosingAlert();
          cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
       ///,, btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
     //,,   btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed

         observable.resetForm();
        //txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panStaffWorkData, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
       // setButtons(false);
        
        
        
        
    }//GEN-LAST:event_btnCancelActionPerformed
                    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed

        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView( ClientConstants.ACTION_STATUS[3]);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
         btnEmp.setEnabled(false);
        
       
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
      
       observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false); 
        btnEmp.setEnabled(false);
        txtStaffID.setEditable(false);

    }//GEN-LAST:event_btnEditActionPerformed
    
    
     private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        System.out.println("Calll View stsus"+currField);
         if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
            viewType.equals(ClientConstants.ACTION_STATUS[17])) 
        {
             viewMap.put(CommonConstants.MAP_NAME, "StaffWorkDiary.getSelectStaffWorkList");
            ArrayList lst = new ArrayList();
            lst.add("SWDID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
        }
        else
        {
           // viewMap.put(CommonConstants.MAP_WHERE, map);
            System.out.println("INNN EEMMPPP");
            viewMap.put(CommonConstants.MAP_NAME, "setEmpDetailsForTransfer1");
            //viewMap.put(CommonConstants.MAP_NAME, "Borrowings.getSelectAcctHeadTOList");
        }
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        viewMap.put(CommonConstants.MAP_WHERE, where);
        where = null;
         new ViewAll(this,viewMap).show();
       // new ViewAll(this,
    }
    
    
     
     public void fillData(Object  map) {
    
//        setModified(true);
        HashMap hash = (HashMap) map;
          if(viewType.equals("EMP"))
          {
                 txtStaffID.setText(hash.get("EMPLOYEEID").toString());
          }
//          if(viewType.equals("INT_GROUP_HEAD"))
//          {
//                 txtintGrpHead.setText(hash.get("AC_HD_ID").toString());
//          }
//        if(viewType.equals("PENAL_GROUP_HEAD"))
//          {
//                 txtpenalGrpHead.setText(hash.get("AC_HD_ID").toString());
//          }
//        if(viewType.equals("CHARGES_GROUP_HEAD"))
//          {
//                 txtchargeGrpHead.setText(hash.get("AC_HD_ID").toString());
//          }
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                HashMap where = new HashMap();
                //System.out.println("hash.get"+hash.get("BORROWING_NO"));
                where.put("SWDID", hash.get("SWDID"));
             // where.put(CommonConstants.BRANCH_ID, "0001");
                hash.put(CommonConstants.MAP_WHERE, where);
                observable.populateData(hash);
             
              // fillTxtNoOfTokens();
                if (observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW) {
                    ClientUtil.enableDisable(panStaffWorkData, false);
                }else  if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                    ClientUtil.enableDisable(panStaffWorkData, true);
                }
                if(viewType.equals(AUTHORIZE)){
                    ClientUtil.enableDisable(panStaffWorkData, false);
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
                setButtonEnableDisable();
            }
        }
    }
     
     
     
    
    
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        savePerformed();
       //    System.out.println("IN btnSaveActionPerformed111");
//        btnAuthorize.setEnabled(true);
//        btnReject.setEnabled(true);
//        btnException.setEnabled(true);
//        btnEdit.setEnabled(true);
//        btnNew.setEnabled(true);
//        btnDelete.setEnabled(true);
//        txtStaffID.setText("");
//        txtLoginout.setText("");
//        txtTransSummry.setText("");
//        txaRemarks.setText("");
//        tdtDate.setDateValue(null);
//        btnEmp.setEnabled(true);
//        txtStaffID.setEditable(true);
        
      // setModified(false);
     // updateOBFields();
     // saveAction();
//        btnAuthorize.setEnabled(true);
//        btnReject.setEnabled(true);
//        btnException.setEnabled(true);
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
    }
     
     
    private void saveAction(String status){
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
      //  System.out.println("status saveAction11111: "+status);
//       txtAmtBorrowed.
//        final String mandatoryMessage = checkMandatory(panStaffWorkData);
//        StringBuffer message = new StringBuffer(mandatoryMessage);
        StringBuffer message = new StringBuffer("");
          if(txtStaffID.getText().equals(""))
        {
                message.append(objMandatoryRB.getString("txtStaffID"));
                message.append("\n");
         }
        if(tdtDate.getDateValue().toString().equals(""))
        {
            message.append(objMandatoryRB.getString("tdtDate"));
             message.append("\n");
        }
        if(txtLoginout.getText().equals(""))
        {
                message.append(objMandatoryRB.getString("txtLoginout"));
                 message.append("\n");
         }
         
         if(txtTransSummry.getText().equals(""))
        {
                message.append(objMandatoryRB.getString("txtTransSummry"));
                 message.append("\n");
         }
       
        

       //Portion is for calculating exp date
      // setExpDateOnCalculation();
      
       
      // */
        //setExpDateOnCalculation();
      //  System.out.println("status saveAction: "+status);
        if(message.length() > 0 ){
            displayAlert(message.toString());
       }else{
             updateOBFields();
                //setExpDateOnCalculation();
            observable.execute(status);
             btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnEdit.setEnabled(true);
        btnNew.setEnabled(true);
        btnDelete.setEnabled(true);
        txtStaffID.setText("");
        txtLoginout.setText("");
        txtTransSummry.setText("");
        txaRemarks.setText("");
        tdtDate.setDateValue(null);
        btnEmp.setEnabled(false);
        txtStaffID.setEditable(true);
//            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
//                HashMap lockMap = new HashMap();
//                ArrayList lst = new ArrayList();
//                lst.add("BORROWING_NO");
//                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
//             //   if (observable.getProxyReturnMap()!=null) {
//              //      if (observable.getProxyReturnMap().containsKey("CONFIG_ID")) {
//              //          lockMap.put("CONFIG_ID", observable.getProxyReturnMap().get("CONFIG_ID"));
//              //      }
//              //  }
//                if (status==CommonConstants.TOSTATUS_UPDATE) {
//                    lockMap.put("BORROWING_NO", observable.getBorrowingNo());
//                }
//          //      setEditLockMap(lockMap);
//               // setEditLock();
//                settings();
//            }
        }
            
    } 
     
     private void settings(){
        observable.resetForm();
      //  txtNoOfTokens.setText("");
       ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panStaffWorkData, false);
        setButtonEnableDisable();
        observable.setResultStatus();
    }
    
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        txtStaffID.setEditable(true);
        observable.resetForm();
        //txtNoOfTokens.setText("");
        ClientUtil.enableDisable(panStaffWorkData, true);
       
        // System.out.println("btnNewActionPerformed ACTION STA: "+ClientConstants.ACTIONTYPE_NEW);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
        btnEmp.setEnabled(true);
     
        
    }//GEN-LAST:event_btnNewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnEmp;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblLoginout;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblSpace57;
    private com.see.truetransact.uicomponent.CLabel lblSpace58;
    private com.see.truetransact.uicomponent.CLabel lblSpace59;
    private com.see.truetransact.uicomponent.CLabel lblSpace60;
    private com.see.truetransact.uicomponent.CLabel lblSpace61;
    private com.see.truetransact.uicomponent.CLabel lblStaffID;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTransSummry;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panStaffWork;
    private com.see.truetransact.uicomponent.CPanel panStaffWorkData;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig;
    private com.see.truetransact.uicomponent.CDateField tdtDate;
    private com.see.truetransact.uicomponent.CTextArea txaRemarks;
    private com.see.truetransact.uicomponent.CTextField txtLoginout;
    private com.see.truetransact.uicomponent.CTextField txtStaffID;
    private com.see.truetransact.uicomponent.CTextField txtTransSummry;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] args) {
        StaffWorkDiaryUI staffwork = new StaffWorkDiaryUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(staffwork);
        j.show();
        staffwork.show();
    }
}
