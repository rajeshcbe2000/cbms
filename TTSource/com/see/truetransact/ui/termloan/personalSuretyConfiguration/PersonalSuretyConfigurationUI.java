/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PersonalSuretyConfigurationUI.java
 *
 * Created on January 20, 2005, 3:03 PM
 */

package com.see.truetransact.ui.termloan.personalSuretyConfiguration;

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
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uivalidation.DefaultValidation;

import java.util.Observable;
import java.util.Observer;
import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.Date.*;
import java.util.*;
import java.text.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PersonalSuretyConfigurationUI extends CInternalFrame implements Observer, UIMandatoryField{
    
    /** Vairable Declarations */
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.generalbodydetails.GeneralBodyDetailsRB", ProxyParameters.LANGUAGE);//Creating Instance For ResourceBundle-TokenConfigRB
    private PersonalSuretyConfigurationOB observable; //Reference for the Observable Class TokenConfigOB
    private PersonalSuretyConfigurationMRB objMandatoryRB = new PersonalSuretyConfigurationMRB();//Instance for the MandatoryResourceBundle
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final String AUTHORIZE="Authorize";//Variable used when btnAuthorize is clicked
    int updateTab=-1;
    private boolean updateMode = false;
    private String iidd="";
    private String sts="";
     private  Object columnNames[] = { "","Product ID","Product description"};
      private List bufferList=new ArrayList();
      List editList=new ArrayList();
    boolean isFilled = false;

    /** Creates new form TokenConfigUI */
    public PersonalSuretyConfigurationUI() {
        initForm();
    }
    
    /** Method which is used to initialize the form TokenConfig */
    private void initForm(){
        initComponents();
        setFieldNames();
        observable = new PersonalSuretyConfigurationOB();
        observable.addObserver(this);
        observable.resetForm();
        setMandatoryHashMap();
        initComponentData();
        txtCloseBefore.setMaxLength(16);
        txtCloseBefore.setValidation(new NumericValidation());
        txtMaxSurety.setMaxLength(16);
        txtMaximumLoanAmount.setAllowNumber(true);
        txtMaximumLoanAmount.setValidation(new CurrencyValidation(14, 2));
        txtMaximumLoanAmount.setMaxLength(16);
        txtMaxNoOfLoans.setAllowNumber(true); // Added by nithya on 21-07-2016 for 4922
        txtMaxLoanSurety.setAllowNumber(true);
        txtMaxLoanSurety.setMaxLength(16);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panPersonalSuretyData, getMandatoryHashMap());
        ClientUtil.enableDisable(panPersonalSurety, false);
        ClientUtil.enableDisable(panIMBPtab, false);
        ClientUtil.enableDisable(panPersonalSuretyData, false);
        ClientUtil.enableDisable(cPanel1, false);
        setButtonEnableDisable();
        cboProdType.setModel(observable.getCbmProdType());
    }
    
    private void tableData() {
        if (cboProdType.getSelectedIndex() > 0) {
            boolean b = false;
            if (!bufferList.isEmpty()) {
                Object rowData[][] = new Object[bufferList.size()][3];
                for (int i = 0; i < bufferList.size(); i++) {
                    HashMap m1 = new HashMap();
                    m1 = (HashMap) bufferList.get(i);
                    rowData[i][0] = b;
                    rowData[i][1] = m1.get("PROD_ID");
                    rowData[i][2] = m1.get("PROD_DESC");
                }
                if (sts.equals("Edit") || sts.equals("Delete") || sts.equals("Enquiry")) {
                    boolean t = true;
                    editList = observable.getSelectedList();
                    for (int k = 0; k < editList.size(); k++) {
                        HashMap m1 = new HashMap();
                        m1 = (HashMap) editList.get(k);
                        for (int i = 0; i < bufferList.size(); i++) {
                            HashMap m2 = new HashMap();
                            m2 = (HashMap) bufferList.get(i);
                            if (m1.get("PROD_ID").toString().equals(m2.get("PROD_ID").toString())) {
                                rowData[i][0] = t;
                                break;
                            }
                        }
                    }
                    sts = "";
                }
                tblproduct.setModel(new javax.swing.table.DefaultTableModel(rowData, columnNames) {
                    // @Override

                    public Class getColumnClass(int column) {
                        if (column == 0) {
                            return Boolean.class;
                        } else {
                            return String.class;
                        }
                    }

                    public boolean isCellEditable(int row, int column) {
                        //Only the third column
                        if (column == 0) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                tblproduct.setVisible(true);
            }
        }
    }
    
    public void getTableDataInit() {
        boolean t = false;
        Object rowData[][] = {{t, "", ""}};
        Object columnNames[] = {"", "Product ID", "Product description"};
        tblproduct.setModel(new javax.swing.table.DefaultTableModel(rowData, columnNames) {

            public Class getColumnClass(int column) {
                if (column == 0) {
                    return Boolean.class;
                } else {
                    return String.class;
                }
            }

            public boolean isCellEditable(int row, int column) {
                //Only the third column
                return false;
            }
        });
        tblproduct.setVisible(true);
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
        lblMaxSurety.setName("lblMaxSurety");
        lblCloseBefore.setName("lblCloseBefore");
        lblCloseBeforeMonth.setName("lblCloseBeforeMonth");
        lblStatus.setName("lblStatus");
        mbrTokenConfig.setName("mbrTokenConfig");
        panStatus.setName("panStatus");
        panPersonalSurety.setName("panPersonalSurety");
        panPersonalSuretyData.setName("panPersonalSuretyData");
        txtMaxSurety.setName("txtMaxSurety");
        txtCloseBefore.setName("txtCloseBefore");
    }
     
      public void updateOBFields() {
        if (panPersonalSurety.isShowing()) {
            observable.setTxtPersonalID(iidd);
            iidd = "";
            observable.setTxtCloseBefore(txtCloseBefore.getText());
            observable.setTxtMaxSurety(txtMaxSurety.getText());
            observable.setPan(1);
            observable.setTxtMaxLoanSurety(txtMaxLoanSurety.getText());
            observable.setTxtMaxSuretyAmt(txtMaxSuretyAmt.getText());
        }
        if (panIMBP.isShowing()) {
            observable.setTxtPersonalID(iidd);
            iidd = "";
            observable.setCboProdType((String) cboProdType.getSelectedItem());
            observable.setTxtMaximumLoanAmount(txtMaximumLoanAmount.getText());
            observable.setSelectedList(getSelectedFromTable());
            observable.setTdtEffectiveDate(tdtEffectiveDate.getDateValue());
            observable.setPan(2);
            observable.setTxtMaxNoOfLoans(txtMaxNoOfLoans.getText()); // Added by nithya on 21-07-2016 for 4922
        }
    }
     
    public List getSelectedFromTable() {
        List selectedList = new ArrayList();
        for (int i = 0; i < tblproduct.getRowCount(); i++) {
            if (tblproduct.getValueAt(i, 0).toString().equals("true")) {
                HashMap map = new HashMap();
                map.put("PROD_ID", tblproduct.getValueAt(i, 1).toString());
                map.put("PROD_DESC", tblproduct.getValueAt(i, 2).toString());
                selectedList.add(map);
            }
        }
        return selectedList;
    }
      public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtMaxSurety", new Boolean(true));
        mandatoryMap.put("txtMaxLoanSurety", new Boolean(true));
        mandatoryMap.put("txtCloseBefore", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private String checkMandatory(javax.swing.JComponent component) {
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
    private void initComponentData() {
        try {
            cboProdType.setModel(observable.getCbmProdType());           
            if (observable.getCbmShareType() != null) {
                cboShareType.setModel(observable.getCbmShareType());
            }
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }
    
    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
        if (observable.getPan() == 1) {
            txtCloseBefore.setText(observable.getTxtCloseBefore());
            txtMaxSurety.setText(observable.getTxtMaxSurety());
            txtMaxLoanSurety.setText(observable.getTxtMaxLoanSurety());
            txtMaxSuretyAmt.setText(observable.getTxtMaxSuretyAmt());
            iidd = observable.getTxtPersonalID();
        }
        if (observable.getPan() == 2) {
            iidd = observable.getTxtPersonalID();
            cboProdType.setSelectedItem(observable.getCboProdType());
            txtMaximumLoanAmount.setText(observable.getTxtMaximumLoanAmount());
            txtMaxNoOfLoans.setText(observable.getTxtMaxNoOfLoans()); // Added by nithya on 21-07-2016 for 4922
            editList = observable.getSelectedList();
            tdtEffectiveDate.setDateValue(observable.getTdtEffectiveDate());
            if (observable.getCboShareType() != null && !observable.getCboShareType().equals("")) {
                cboShareType.setSelectedItem(observable.getCboShareType());
            }
        }
    }

    public String getDtPrintValue(String strDate) {
        try {
            SimpleDateFormat sdfSource = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            //parse the string into Date object
            Date date = sdfSource.parse(strDate);
            //create SimpleDateFormat object with desired date format
            SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
            //parse the date into another format
            strDate = sdfDestination.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }
    
    /*
     * Auto Generated Method - setHelpMessage() This method shows tooltip help
     * for all the input fields available in the UI. It needs the Mandatory
     * Resource Bundle object. Help display Label name should be lblMsg.
     */
    public void setHelpMessage() {
    }

    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths() {
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
        cTabbedPane1 = new com.see.truetransact.uicomponent.CTabbedPane();
        panPersonalSurety = new com.see.truetransact.uicomponent.CPanel();
        panPersonalSuretyData = new com.see.truetransact.uicomponent.CPanel();
        lblCloseBefore = new com.see.truetransact.uicomponent.CLabel();
        lblMaxSurety = new com.see.truetransact.uicomponent.CLabel();
        txtMaxSurety = new com.see.truetransact.uicomponent.CTextField();
        txtCloseBefore = new com.see.truetransact.uicomponent.CTextField();
        lblCloseBeforeMonth = new com.see.truetransact.uicomponent.CLabel();
        lblMaxLoan = new com.see.truetransact.uicomponent.CLabel();
        txtMaxLoanSurety = new com.see.truetransact.uicomponent.CTextField();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        txtMaxSuretyAmt = new com.see.truetransact.uicomponent.CTextField();
        panIMBPtab = new com.see.truetransact.uicomponent.CPanel();
        panIMBP = new com.see.truetransact.uicomponent.CPanel();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        txtMaximumLoanAmount = new com.see.truetransact.uicomponent.CTextField();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblproduct = new com.see.truetransact.uicomponent.CTable();
        lblEffectiveDate = new com.see.truetransact.uicomponent.CLabel();
        tdtEffectiveDate = new com.see.truetransact.uicomponent.CDateField();
        lblShareType = new com.see.truetransact.uicomponent.CLabel();
        cboShareType = new com.see.truetransact.uicomponent.CComboBox();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtMaxNoOfLoans = new com.see.truetransact.uicomponent.CTextField();
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
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
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

        panPersonalSurety.setMaximumSize(new java.awt.Dimension(650, 550));
        panPersonalSurety.setMinimumSize(new java.awt.Dimension(650, 550));
        panPersonalSurety.setPreferredSize(new java.awt.Dimension(650, 550));
        panPersonalSurety.setLayout(new java.awt.GridBagLayout());

        panPersonalSuretyData.setBorder(javax.swing.BorderFactory.createTitledBorder("Personal Surety Configuration"));
        panPersonalSuretyData.setMaximumSize(new java.awt.Dimension(450, 250));
        panPersonalSuretyData.setMinimumSize(new java.awt.Dimension(450, 250));
        panPersonalSuretyData.setPreferredSize(new java.awt.Dimension(450, 250));
        panPersonalSuretyData.setLayout(new java.awt.GridBagLayout());

        lblCloseBefore.setText("Need to close the Loan before");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panPersonalSuretyData.add(lblCloseBefore, gridBagConstraints);

        lblMaxSurety.setText("Maximum Number of Surety Applicable ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panPersonalSuretyData.add(lblMaxSurety, gridBagConstraints);

        txtMaxSurety.setAllowNumber(true);
        txtMaxSurety.setMaxLength(128);
        txtMaxSurety.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMaxSurety.setName("txtCompany");
        txtMaxSurety.setNextFocusableComponent(txtCloseBefore);
        txtMaxSurety.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panPersonalSuretyData.add(txtMaxSurety, gridBagConstraints);

        txtCloseBefore.setAllowNumber(true);
        txtCloseBefore.setMaxLength(128);
        txtCloseBefore.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCloseBefore.setName("txtCompany");
        txtCloseBefore.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panPersonalSuretyData.add(txtCloseBefore, gridBagConstraints);

        lblCloseBeforeMonth.setText("months");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        panPersonalSuretyData.add(lblCloseBeforeMonth, gridBagConstraints);

        lblMaxLoan.setText("Maximum Number of Loan Applicable For Surety");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panPersonalSuretyData.add(lblMaxLoan, gridBagConstraints);

        txtMaxLoanSurety.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMaxLoanSurety.setName("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panPersonalSuretyData.add(txtMaxLoanSurety, gridBagConstraints);

        cLabel3.setText("Maximum surety amount for a member");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panPersonalSuretyData.add(cLabel3, gridBagConstraints);

        txtMaxSuretyAmt.setAllowNumber(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panPersonalSuretyData.add(txtMaxSuretyAmt, gridBagConstraints);

        panPersonalSurety.add(panPersonalSuretyData, new java.awt.GridBagConstraints());

        cTabbedPane1.addTab("Personal Security Configuration", panPersonalSurety);

        panIMBPtab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panIMBP.setLayout(new java.awt.GridBagLayout());

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 6, 4);
        panIMBP.add(lblProdType, gridBagConstraints);

        cLabel2.setText("Maximum Loan Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIMBP.add(cLabel2, gridBagConstraints);

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(150);
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 6, 4);
        panIMBP.add(cboProdType, gridBagConstraints);

        txtMaximumLoanAmount.setAllowNumber(true);
        txtMaximumLoanAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panIMBP.add(txtMaximumLoanAmount, gridBagConstraints);

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
        tblproduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblproductMouseClicked(evt);
            }
        });
        cScrollPane1.setViewportView(tblproduct);

        cPanel1.add(cScrollPane1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 8, 0);
        panIMBP.add(cPanel1, gridBagConstraints);

        lblEffectiveDate.setText("Effective Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 6, 4);
        panIMBP.add(lblEffectiveDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 6, 4);
        panIMBP.add(tdtEffectiveDate, gridBagConstraints);

        lblShareType.setText("Share Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 6, 4);
        panIMBP.add(lblShareType, gridBagConstraints);

        cboShareType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboShareType.setPopupWidth(150);
        cboShareType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboShareTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 6, 4);
        panIMBP.add(cboShareType, gridBagConstraints);

        cLabel1.setText("Maximum No. of Loans");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        panIMBP.add(cLabel1, gridBagConstraints);

        txtMaxNoOfLoans.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panIMBP.add(txtMaxNoOfLoans, gridBagConstraints);

        panIMBPtab.add(panIMBP, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 570, 390));

        cTabbedPane1.addTab("IMBP", panIMBPtab);

        getContentPane().add(cTabbedPane1, java.awt.BorderLayout.CENTER);

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

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        if (cboProdType.getSelectedIndex() > 0) {
            String pid = cboProdType.getSelectedItem().toString();
            HashMap where = new HashMap();
            where.put("AUTH_TYPE", pid);
            bufferList = ClientUtil.executeQuery("getProductDts", where);
            tableData();
        }
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        sts = "Enquiry";
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        callView("Enquiry");
        tblproduct.setEnabled(false);
        btnSave.setEnabled(false);
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
        setModified(false);
        //txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panPersonalSuretyData, false);
        ClientUtil.enableDisable(panIMBPtab, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        getTableDataInit();
       // setButtons(false);
    }//GEN-LAST:event_btnCancelActionPerformed
                    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        sts = "Delete";
        tblproduct.setEnabled(false);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE && isFilled == true) {
            isFilled = false;
            observable.execute(CommonConstants.TOSTATUS_DELETE);
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                lblStatus.setText("Deleted");
                observable.resetForm();
                bufferList.clear();
                ClientUtil.enableDisable(panIMBPtab, false);
            } else if (observable.getResult() == ClientConstants.ACTIONTYPE_FAILED) {
                lblStatus.setText("Failed");
                observable.resetForm();
            }
        } else {
            observable.setActionType(ClientConstants.ACTIONTYPE_DELETE); //__ Sets the Action Type to be performed...            
            callView(ClientConstants.ACTION_STATUS[3]);
            btnAuthorize.setEnabled(false);
            btnReject.setEnabled(false);
            btnException.setEnabled(false);
            lblStatus.setText("Delete");
            btnDelete.setEnabled(true);
            btnNew.setEnabled(false);
            btnEdit.setEnabled(false);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            isFilled = true;
        }
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        sts = "Edit";
        tblproduct.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnDelete.setEnabled(false);

        ClientUtil.enableDisable(panPersonalSurety, true);
        ClientUtil.enableDisable(panIMBPtab, true);
        ClientUtil.enableDisable(cPanel1, true);
        cboShareType.setEnabled(false);
        cboProdType.setEnabled(false);

    }//GEN-LAST:event_btnEditActionPerformed

    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)
                || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
            if (panPersonalSurety.isShowing()) {
                viewMap.put(CommonConstants.MAP_NAME, "PersonalSuretyConfiguration.getSelectPersonalList");
                ArrayList lst = new ArrayList();
                lst.add("PERSONAL_SURETY_ID");
                viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                lst = null;
                observable.setPan(1);
            }
            if (panIMBP.isShowing()) {
                viewMap.put(CommonConstants.MAP_NAME, "PersonalSuretyConfiguration.getSelectIMBPList");
                ArrayList lst = new ArrayList();
                lst.add("IMBP_ID");
                viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                lst = null;
                observable.setPan(2);
                isFilled = false;
            }
        }
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        viewMap.put(CommonConstants.MAP_WHERE, where);
        where = null;
        new ViewAll(this, viewMap).show();
    }
         
     public void fillData(Object  map) {
        HashMap hash = (HashMap) map;
         if (viewType != null) {
             if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                     || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                 HashMap where = new HashMap();
                 if (observable.getPan() == 1) {
                     where.put("PSCID", hash.get("PERSONAL_SURETY_ID"));
                     hash.put(CommonConstants.MAP_WHERE, where);
                     observable.populateData(hash);
                 }
                 if (observable.getPan() == 2) {
                     where.put("IMBPID", hash.get("IMBP_ID"));
                     hash.put(CommonConstants.MAP_WHERE, where);
                     observable.populateData(hash);
                 }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                    ClientUtil.enableDisable(panPersonalSuretyData, false);
                    ClientUtil.enableDisable(panIMBP, false);
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    ClientUtil.enableDisable(panPersonalSuretyData, true);
                    ClientUtil.enableDisable(panIMBP, true);

                }
                if (viewType.equals(AUTHORIZE)) {
                    ClientUtil.enableDisable(panPersonalSuretyData, false);
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
        setModified(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnEdit.setEnabled(true);
        btnNew.setEnabled(true);
        btnDelete.setEnabled(true);
        txtCloseBefore.setText("");
        txtMaxSurety.setText("");
        txtMaxLoanSurety.setText("");
        txtMaxSuretyAmt.setText("");
    }//GEN-LAST:event_btnSaveActionPerformed
   
    private void savePerformed() {
        String action;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            action = CommonConstants.TOSTATUS_INSERT;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            action = CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            action = CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
    }
     
    private void saveAction(String status) {
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        //  System.out.println("status saveAction11111: "+status);
//       txtAmtBorrowed.
        // final String mandatoryMessage = checkMandatory(panGeneralBodyData);
        StringBuffer message = new StringBuffer("");
        if (panPersonalSurety.isShowing()) {
            if (txtCloseBefore.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtCloseBefore"));
                message.append("\n");
            }
            if (txtMaxSurety.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtMaxSurety"));
                message.append("\n");
            }
            if (txtMaxLoanSurety.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtMaxLoanSurety"));
                message.append("\n");
            }
        } else {
        }

        if (message.length() > 0) {
            displayAlert(message.toString());
        } else {
            if (panPersonalSurety.isShowing() && tdtEffectiveDate.getDateValue().equals(null)) {
                ClientUtil.showMessageWindow("Please enter the Date..!!");
                return;
            } else {
                updateOBFields();
                observable.execute(status);
                sts = "";
                editList.clear();
                bufferList.clear();
                getTableDataInit();
                observable.resetForm();
                ClientUtil.enableDisable(panIMBPtab, false);
                ClientUtil.enableDisable(panPersonalSuretyData, false);
                clearAll();
            }
        }
    }

    private void clearAll()
    {
        txtMaximumLoanAmount.setText("");
        cboProdType.setSelectedIndex(0);
        tdtEffectiveDate.setDateValue(null);
        txtMaxSurety.setText("");
        txtCloseBefore.setText("");
        cboShareType.setSelectedItem("");
        txtMaxNoOfLoans.setText(""); // Added by nithya on 21-07-2016 for 4922
        txtMaxLoanSurety.setText("");
        txtMaxSuretyAmt.setText("");
    }
    
     private void settings(){
        observable.resetForm();
      //  txtNoOfTokens.setText("");
       ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panPersonalSuretyData, false);
        setButtonEnableDisable();
        observable.setResultStatus();
    }
    
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        bufferList.clear();
        getTableDataInit();
        observable.resetForm();
        //txtNoOfTokens.setText("");
        ClientUtil.enableDisable(panPersonalSuretyData, true);
        ClientUtil.enableDisable(panIMBPtab, true);
      
             ClientUtil.enableDisable(cPanel1, true);
        // System.out.println("btnNewActionPerformed ACTION STA: "+ClientConstants.ACTIONTYPE_NEW);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
        btnDelete.setEnabled(false);
        clearAll();
    }//GEN-LAST:event_btnNewActionPerformed

    private void cboShareTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboShareTypeActionPerformed
        // TODO add your handling code here:
        final String shareType = CommonUtil.convertObjToStr(((ComboBoxModel)(cboShareType.getModel())).getKeyForSelected());
        observable.setCboShareType(shareType);
    }//GEN-LAST:event_cboShareTypeActionPerformed

    private void tblproductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblproductMouseClicked
        // TODO add your handling code here:
         if (cboProdType.getSelectedIndex() > 0) {
            String prodType = CommonUtil.convertObjToStr(cboProdType.getSelectedItem());
            if (observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
                HashMap dataMap = new HashMap();
                String shareType = "";
                shareType = observable.getCboShareType();                
                int rowCount = 0;
                rowCount = tblproduct.getRowCount();
                int selRow = 0;
                selRow = tblproduct.getSelectedRow();
                if(rowCount>0){
                ArrayList prodIdList = new ArrayList();
                String prodId = "";
                prodId = CommonUtil.convertObjToStr(tblproduct.getValueAt(selRow, 1));
                    prodIdList.add(prodId);                    
                    dataMap.put("PROD_ID_LIST", prodIdList);
                    dataMap.put("prodListSize", prodIdList.size());                        
                }  
                dataMap.put("SHARE_TYPE", shareType);
                dataMap.put("IMBP_TYPE", prodType);                
                boolean isExists = false;
                isExists = observable.chkImbpExists(dataMap);               
                if (isExists) {
                    ClientUtil.showMessageWindow("This type of Settings already exists..!!");
                    tblproduct.revalidate();
                    getTableDataInit();
                    tableData();
                }
            }
        }
    }//GEN-LAST:event_tblproductMouseClicked
    
   

    
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
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CTabbedPane cTabbedPane1;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboShareType;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblCloseBefore;
    private com.see.truetransact.uicomponent.CLabel lblCloseBeforeMonth;
    private com.see.truetransact.uicomponent.CLabel lblEffectiveDate;
    private com.see.truetransact.uicomponent.CLabel lblMaxLoan;
    private com.see.truetransact.uicomponent.CLabel lblMaxSurety;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblShareType;
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
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panIMBP;
    private com.see.truetransact.uicomponent.CPanel panIMBPtab;
    private com.see.truetransact.uicomponent.CPanel panPersonalSurety;
    private com.see.truetransact.uicomponent.CPanel panPersonalSuretyData;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CTable tblproduct;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig;
    private com.see.truetransact.uicomponent.CDateField tdtEffectiveDate;
    private com.see.truetransact.uicomponent.CTextField txtCloseBefore;
    private com.see.truetransact.uicomponent.CTextField txtMaxLoanSurety;
    private com.see.truetransact.uicomponent.CTextField txtMaxNoOfLoans;
    private com.see.truetransact.uicomponent.CTextField txtMaxSurety;
    private com.see.truetransact.uicomponent.CTextField txtMaxSuretyAmt;
    private com.see.truetransact.uicomponent.CTextField txtMaximumLoanAmount;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] args) {
        PersonalSuretyConfigurationUI generalbody = new PersonalSuretyConfigurationUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(generalbody);
        j.show();
        generalbody.show();
    }
}
