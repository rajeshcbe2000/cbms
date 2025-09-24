/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ServiceWiseLoanIssueConfigurationUI.java
 *
 * Created on January 20, 2005, 3:03 PM
 */

package com.see.truetransact.ui.termloan.personalSuretyConfiguration;

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
import com.see.truetransact.uivalidation.CurrencyValidation;

import java.util.Observable;
import java.util.Observer;
import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.commonutil.DateUtil;
import java.util.Date.*;
import java.util.*;
import java.text.*;

public class ServiceWiseLoanIssueConfigurationUI extends CInternalFrame implements Observer, UIMandatoryField{
    
    /** Vairable Declarations */
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.generalbodydetails.GeneralBodyDetailsRB", ProxyParameters.LANGUAGE);//Creating Instance For ResourceBundle-TokenConfigRB
    private ServiceWiseLoanIssueConfigurationOB observable; //Reference for the Observable Class TokenConfigOB
    private ServiceWiseLoanIssueConfigurationMRB objMandatoryRB = new ServiceWiseLoanIssueConfigurationMRB();//Instance for the MandatoryResourceBundle
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final String AUTHORIZE="Authorize";//Variable used when btnAuthorize is clicked
    int updateTab=-1;
    private boolean updateMode = false;
    private String iidd="";
    private int edit=0;
    private  Object columnNames[] = { "Select","Product ID","Product description"};
    private List bufferList=new ArrayList();
    /** Creates new form TokenConfigUI */
    public ServiceWiseLoanIssueConfigurationUI() {
        initForm();
        
    }
    
    /** Method which is used to initialize the form TokenConfig */
    private void initForm(){
        initComponents();
        observable = new ServiceWiseLoanIssueConfigurationOB();
        observable.addObserver(this);
        observable.resetForm();
        setMandatoryHashMap();
        setFieldNames();
        initComponentData();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panIMBP,getMandatoryHashMap());
        txtPastServicePeriod.setValidation(new NumericValidation(10,4));
        txtFromAmt.setValidation(new NumericValidation());
        txtToAmt.setValidation(new NumericValidation());
        txtNoOfSuretiesRequired.setValidation(new NumericValidation());
        setButtonEnableDisable();
        ClientUtil.enableDisable(panIMBP,false);
    }
    
    private void tableData() {
        HashMap where=new HashMap();
        bufferList=ClientUtil.executeQuery("getProductIdDesc", where);
        boolean b=false;
        if(!bufferList.isEmpty())
        { Object rowData[][] =new Object[bufferList.size()][3];
          for(int i=0;i<bufferList.size();i++) {
              HashMap m1=new HashMap();
              m1=(HashMap)bufferList.get(i);
              rowData[i][0]=b;
              rowData[i][1]=m1.get("PROD_ID");
              rowData[i][2]=m1.get("PROD_DESC");
          }
          if(edit==1) {
              b=true;
              List editList= new ArrayList();
              editList= observable.getSelectedList();
              for(int k=0;k<editList.size();k++) {
                  HashMap e1= new HashMap();
                  e1=(HashMap)(editList.get(k));
                  for(int i=0;i<bufferList.size();i++) {
                      HashMap b1= new HashMap();
                      b1= (HashMap)bufferList.get(i);
                      if(e1.get("PROD_ID").toString().equals(b1.get("PROD_ID").toString())) {
                          rowData[i][0]=b;
                          break;
                      }
                  }
              }
              edit=0;
          }
          
          
          tblproduct.setModel(new javax.swing.table.DefaultTableModel(rowData,columnNames){
              
              
              // @Override
              public Class getColumnClass(int column) {
                  if(column==0) {
                      return Boolean.class;
                  }
                  else {
                      return String.class;
                  }
              }
              
              
              public boolean isCellEditable(int row, int column) {
                  //Only the third column
                  if(column==0) {
                      return true;
                  }
                  else {
                      return false;
                  }
              }
              
          }) ;
          tblproduct.setVisible(true);
        }
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
        btnView.setEnabled(!btnView.isEnabled());
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
        
        lblStatus.setName("lblStatus");
        mbrTokenConfig.setName("mbrTokenConfig");
        panStatus.setName("panStatus");
        lblEffectFrom.setName("lblEffectFrom");
        lblPastServicePeriod.setName("lblPastServicePeriod");
        lblFromAmt.setName("lblFromAmt");
        lblToAmt.setName("lblToAmt");
        lblNoOfSuretiesRequired.setName("lblNoOfSuretiesRequired");
        tdtEffectFrom.setName("tdtEffectFrom");
        txtPastServicePeriod.setName("txtPastServicePeriod");
        txtFromAmt.setName("txtFromAmt");
        txtToAmt.setName("txtToAmt");
        txtNoOfSuretiesRequired.setName("txtNoOfSuretiesRequired");
    }
    
    
    
    public void updateOBFields() {
        if(panIMBP.isShowing()) {
            observable.setTdtEffectFrom(DateUtil.getDateMMDDYYYY(tdtEffectFrom.getDateValue()));
            observable.setTxtNoOfSuretiesRequired(CommonUtil.convertObjToInt(txtNoOfSuretiesRequired.getText()));
            observable.setTxtPastServicePeriod(CommonUtil.convertObjToInt(txtPastServicePeriod.getText()));
            observable.setTxtToAmt(CommonUtil.convertObjToInt(txtToAmt.getText()));
            observable.setTxtFromAmt(CommonUtil.convertObjToInt(txtFromAmt.getText()));
            observable.setSelectedList(getSelectedFromTable());
            
        }
    }
    
    public List getSelectedFromTable() {
        List selectedList=new ArrayList();
        for(int i=0;i<tblproduct.getRowCount();i++) {
            
            System.out.println("tab val"+tblproduct.getValueAt(i,0));
            System.out.println("iivalueee>>>"+Boolean.valueOf(tblproduct.getValueAt(i,0).toString()));
            if(tblproduct.getValueAt(i,0).toString().equals("true")) {
                System.out.println("innnnnn ifff");
                HashMap map=new HashMap();
                map.put("PROD_ID",tblproduct.getValueAt(i,1).toString());
                map.put("PROD_DESC",tblproduct.getValueAt(i,2).toString());
                selectedList.add(map);
            }
        }
        System.out.println("selected Listttt"+selectedList);
        return selectedList;
    }
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("tdtEffectFrom", new Boolean(true));
        mandatoryMap.put("txtPastServicePeriod", new Boolean(true));
        mandatoryMap.put("txtFromAmt", new Boolean(true));
        mandatoryMap.put("txtToAmt", new Boolean(true));
        mandatoryMap.put("txtNoOfSuretiesRequired", new Boolean(true));
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
        
    }
    
    
    /*Setting model to the combobox cboTokenType  */
    private void initComponentData() {
        try{
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        if(observable.getTdtEffectFrom()!=null) {
            tdtEffectFrom.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtEffectFrom())));
            txtPastServicePeriod.setText(String.valueOf(observable.getTxtPastServicePeriod()));
            txtFromAmt.setText(String.valueOf(observable.getTxtFromAmt()));
            txtToAmt.setText(String.valueOf(observable.getTxtToAmt()));
            txtNoOfSuretiesRequired.setText(String.valueOf(observable.getTxtNoOfSuretiesRequired()));
            tableData();
        }
    }
    
    public String getDtPrintValue(String strDate) {
        try {
            SimpleDateFormat sdfSource = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            Date date = sdfSource.parse(strDate);
            SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
            strDate = sdfDestination.format(date);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }
    
    
    
    
    
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
    }
    
    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths(){
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

        tbrTokenConfig = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace70 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace71 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace72 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panIMBP = new com.see.truetransact.uicomponent.CPanel();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblproduct = new com.see.truetransact.uicomponent.CTable();
        cPanel6 = new com.see.truetransact.uicomponent.CPanel();
        txtPastServicePeriod = new com.see.truetransact.uicomponent.CTextField();
        lblPastServicePeriod = new com.see.truetransact.uicomponent.CLabel();
        lblFromAmt = new com.see.truetransact.uicomponent.CLabel();
        txtFromAmt = new com.see.truetransact.uicomponent.CTextField();
        lblToAmt = new com.see.truetransact.uicomponent.CLabel();
        txtToAmt = new com.see.truetransact.uicomponent.CTextField();
        lblNoOfSuretiesRequired = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfSuretiesRequired = new com.see.truetransact.uicomponent.CTextField();
        lblEffectFrom = new com.see.truetransact.uicomponent.CLabel();
        tdtEffectFrom = new com.see.truetransact.uicomponent.CDateField();
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

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace70);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        btnEdit.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnEditFocusLost(evt);
            }
        });
        tbrTokenConfig.add(btnEdit);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace71);

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

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace72);

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

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace73);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnException);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace74);

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

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace75);

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

        panIMBP.setLayout(new java.awt.GridBagLayout());

        cPanel1.setLayout(new java.awt.GridLayout(1, 0));

        cScrollPane1.setMinimumSize(new java.awt.Dimension(300, 150));

        tblproduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Select", "Product Type", "Product description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        cScrollPane1.setViewportView(tblproduct);

        cPanel1.add(cScrollPane1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 0);
        panIMBP.add(cPanel1, gridBagConstraints);

        cPanel6.setLayout(new java.awt.GridBagLayout());

        txtPastServicePeriod.setMinimumSize(new java.awt.Dimension(100, 20));
        txtPastServicePeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPastServicePeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel6.add(txtPastServicePeriod, gridBagConstraints);

        lblPastServicePeriod.setText("Past Service Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel6.add(lblPastServicePeriod, gridBagConstraints);

        lblFromAmt.setText("From Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel6.add(lblFromAmt, gridBagConstraints);

        txtFromAmt.setMinimumSize(new java.awt.Dimension(100, 20));
        txtFromAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFromAmtActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel6.add(txtFromAmt, gridBagConstraints);

        lblToAmt.setText("To Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel6.add(lblToAmt, gridBagConstraints);

        txtToAmt.setMinimumSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel6.add(txtToAmt, gridBagConstraints);

        lblNoOfSuretiesRequired.setText("No of Sureties Required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel6.add(lblNoOfSuretiesRequired, gridBagConstraints);

        txtNoOfSuretiesRequired.setMinimumSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel6.add(txtNoOfSuretiesRequired, gridBagConstraints);

        lblEffectFrom.setText("Effect From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel6.add(lblEffectFrom, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel6.add(tdtEffectFrom, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panIMBP.add(cPanel6, gridBagConstraints);

        getContentPane().add(panIMBP, java.awt.BorderLayout.CENTER);

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
    
    private void btnEditFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnEditFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditFocusLost
    
    private void txtPastServicePeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPastServicePeriodFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPastServicePeriodFocusLost
    
    private void txtFromAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFromAmtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFromAmtActionPerformed
    
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
        tblproduct.setModel(new javax.swing.table.DefaultTableModel(null,columnNames));
        //        ClientUtil.enableDisable(panPersonalSuretyData, false);
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
        
        
        
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        edit=1;
        ClientUtil.enableDisable(panIMBP,true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        
    }//GEN-LAST:event_btnEditActionPerformed
    
    
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
        viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
        viewType.equals(ClientConstants.ACTION_STATUS[17])) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectESLIDetails");
            ArrayList lst = new ArrayList();
            lst.add("ESLI_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            
        }
        //        else {
        //            viewMap.put(CommonConstants.MAP_NAME, "Borrowings.getSelectAcctHeadTOList");
        //        }
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
        //          if(viewType.equals("PRICIPAL_GROUP_HEAD"))
        //          {
        //                 txtprinGrpHead.setText(hash.get("AC_HD_ID").toString());
        //          }
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
        System.out.println("viewType============"+viewType);
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                HashMap where = new HashMap();
                //System.out.println("hash.get"+hash.get("BORROWING_NO"));
                where.put("ESLIID", hash.get("ESLI_ID"));
                observable.setElsi_id(CommonUtil.convertObjToStr(hash.get("ESLI_ID")));
                // where.put(CommonConstants.BRANCH_ID, "0001");
                hash.put(CommonConstants.MAP_WHERE, where);
                observable.populateData(hash);
                
                // fillTxtNoOfTokens();
                if (observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW) {
                    //                    ClientUtil.enableDisable(panPersonalSuretyData, false);
                }else  if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                    //                    ClientUtil.enableDisable(panPersonalSuretyData, true);
                }
                if(viewType.equals(AUTHORIZE)){
                    //                    ClientUtil.enableDisable(panPersonalSuretyData, false);
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
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnEdit.setEnabled(true);
        btnNew.setEnabled(true);
        btnDelete.setEnabled(true);
        //        txtCloseBefore.setText("");
        //        txtMaxSurety.setText("");
        
        
        
        // setModified(false);
        //updateOBFields();
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
            System.out.println("actionnnnnnn"+CommonConstants.TOSTATUS_INSERT);
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
        final String mandatoryMessage = checkMandatory(panIMBP);
        StringBuffer message = new StringBuffer("");
        if(panIMBP.isShowing()) {
            if(tdtEffectFrom.getDateValue().equals("")) {
                message.append(objMandatoryRB.getString("tdtEffectFrom"));
                message.append("\n");
            }
            if(txtPastServicePeriod.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtPastServicePeriod"));
                message.append("\n");
            }
            if(txtFromAmt.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtFromAmt"));
                message.append("\n");
            }
            if(txtToAmt.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtToAmt"));
                message.append("\n");
            }
            if(txtNoOfSuretiesRequired.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtNoOfSuretiesRequired"));
                message.append("\n");
            }
        }
        else {
            
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
            settings();
            //            }
            bufferList.clear();
            tableData();
            //clear();
        }
        
    }
    public void clear() {
        tdtEffectFrom.setDateValue(null);
        txtNoOfSuretiesRequired.setText("");
        txtPastServicePeriod.setText("");
        txtFromAmt.setText("");
        txtToAmt.setText("");
    }
    
    private void settings(){
        observable.resetForm();
        //  txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        //  ClientUtil.enableDisable(panPersonalSuretyData, false);
        setButtonEnableDisable();
        observable.setResultStatus();
    }
    
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        ClientUtil.enableDisable(panIMBP,true);
        bufferList.clear();
        tableData();
        observable.resetForm();
        //txtNoOfTokens.setText("");
        //    ClientUtil.enableDisable(panPersonalSuretyData, true);
        
        // System.out.println("btnNewActionPerformed ACTION STA: "+ClientConstants.ACTIONTYPE_NEW);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
        
        
    }//GEN-LAST:event_btnNewActionPerformed
    
    
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
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel6;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblEffectFrom;
    private com.see.truetransact.uicomponent.CLabel lblFromAmt;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNoOfSuretiesRequired;
    private com.see.truetransact.uicomponent.CLabel lblPastServicePeriod;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToAmt;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panIMBP;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CTable tblproduct;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig;
    private com.see.truetransact.uicomponent.CDateField tdtEffectFrom;
    private com.see.truetransact.uicomponent.CTextField txtFromAmt;
    private com.see.truetransact.uicomponent.CTextField txtNoOfSuretiesRequired;
    private com.see.truetransact.uicomponent.CTextField txtPastServicePeriod;
    private com.see.truetransact.uicomponent.CTextField txtToAmt;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] args) {
        ServiceWiseLoanIssueConfigurationUI generalbody = new ServiceWiseLoanIssueConfigurationUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(generalbody);
        j.show();
        generalbody.show();
    }
}
