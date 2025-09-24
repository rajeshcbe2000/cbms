/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * YearEndProcessUI.java
 *
 * Created on January 20, 2005, 3:03 PM
 */

package com.see.truetransact.ui.batchprocess.yearendprocess;

/**
 *
 * @author  Rajesh
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
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.ResourceBundle;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.commonutil.DateUtil;

import java.util.*;

public class YearEndProcessUI extends CInternalFrame implements Observer, UIMandatoryField{
    
    /** Vairable Declarations */
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.batchprocess.yearendprocess.YearEndProcessRB", ProxyParameters.LANGUAGE);//Creating Instance For ResourceBundle-YearEndProcessRB
    private YearEndProcessOB observable; //Reference for the Observable Class YearEndProcessOB
    private YearEndProcessMRB objMandatoryRB = new YearEndProcessMRB();//Instance for the MandatoryResourceBundle
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final String AUTHORIZE="Authorize";//Variable used when btnAuthorize is clicked
    private final String ISSUED = "ISSUED";//Varaible used to make TOKEN_STATUS AS ISSUED when authorize button is clicked
    private Date currDt = null;
    
    /** Creates new form YearEndProcessUI */
    public YearEndProcessUI() {
        initForm();
    }
    
    /** Method which is used to initialize the form YearEndProcess */
    private void initForm(){
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setFieldNames();
        internationalize();
        System.out.println("#$#$ BANKINFO inseide YearEndProcess:"+TrueTransactMain.BANKINFO);
        if (!(TrueTransactMain.BANKINFO.get("YEAREND_PROCESS_DT")!=null && 
            DateUtil.dateDiff((Date)TrueTransactMain.BANKINFO.get("YEAREND_PROCESS_DT"), currDt)==0)) {
            displayAlert("Year End Date not specified or Year End Date should be equal to Application Date.\nConnot do Year End Process");
            btnProcess.setEnabled(false);
//            cifClosingAlert();
            return;
        }
        setObservable();
        initComponentData();
        javax.swing.table.TableColumn col = tblIncomeHeads.getColumn(tblIncomeHeads.getColumnName(0));
        col.setMaxWidth(70);
        col = tblIncomeHeads.getColumn(tblIncomeHeads.getColumnName(1));
        col.setMaxWidth(250);
        col = tblIncomeHeads.getColumn(tblIncomeHeads.getColumnName(2));
        col.setMaxWidth(75);
        col = tblExpenditureHeads.getColumn(tblExpenditureHeads.getColumnName(0));
        col.setMaxWidth(70);
        col = tblExpenditureHeads.getColumn(tblExpenditureHeads.getColumnName(1));
        col.setMaxWidth(250);
        col = tblExpenditureHeads.getColumn(tblExpenditureHeads.getColumnName(2));
        col.setMaxWidth(75);
        setMandatoryHashMap();
        setMaxLengths();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panYearEndProcess);
        
        ClientUtil.enableDisable(panYearEndProcess, false);
        setButtonEnableDisable();
        lblLossAcHead.setVisible(false);
        txtLossAcHead.setVisible(false);
        btnLossAcHead.setVisible(false);
    }
    
   /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        panYearEndProcess.setName("panYearEndProcess");
        panProfitLossHeads.setName("panProfitLossHeads");
        lblProfitAcHead.setName("lblProfitAcHead");
        lblLossAcHead.setName("lblLossAcHead");
        panProfitAcHead.setName("panProfitAcHead");
        txtProfitAcHead.setName("txtProfitAcHead");
        btnProfitAcHead.setName("btnProfitAcHead");
        panLossAcHead.setName("panLossAcHead");
        txtLossAcHead.setName("txtLossAcHead");
        btnLossAcHead.setName("btnLossAcHead");
        panTables.setName("panTables");
        srpIncomeHeads.setName("srpIncomeHeads");
        tblIncomeHeads.setName("tblIncomeHeads");
        srpExpenditureHeads.setName("srpExpenditureHeads");
        tblExpenditureHeads.setName("tblExpenditureHeads");
        txtTotalIncome.setName("txtTotalIncome");
        txtTotalExpenditure.setName("txtTotalExpenditure");
        panProfitLoss.setName("panProfitLoss");
        lblProfitLoss.setName("lblProfitLoss");
        txtProfitLoss.setName("txtProfitLoss");
        btnProcess.setName("btnProcess");
        lblSpace1.setName("lblSpace1");
        lblMsg.setName("lblMsg");
        lblStatus.setName("lblStatus");
        panStatus.setName("panStatus");
    }
    
 /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblProfitAcHead.setText(resourceBundle.getString("lblProfitAcHead"));
        lblLossAcHead.setText(resourceBundle.getString("lblLossAcHead"));
        lblProfitLoss.setText(resourceBundle.getString("lblProfitLoss"));
        btnProcess.setText(resourceBundle.getString("btnProcess"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
    }
    
    /** Adds up the Observable to this form */
    private void setObservable() {
        try{
            observable = YearEndProcessOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /*Setting model to the combobox cboTokenType  */
    private void initComponentData() {
        try{
            tblIncomeHeads.setModel(observable.getTbmIncome());
            tblExpenditureHeads.setModel(observable.getTbmExpenditure());
            setRightAlignment(tblIncomeHeads, 2);
            setRightAlignment(tblExpenditureHeads, 2);
            txtTotalIncome.setText(CurrencyValidation.formatCrore(observable.getTxtTotalIncome()));
            txtTotalExpenditure.setText(CurrencyValidation.formatCrore(observable.getTxtTotalExpenditure()));
            if (CommonUtil.convertObjToDouble(observable.getTxtProfitLoss()).doubleValue()>0) {
                lblProfitLoss.setText("PROFIT");
                txtProfitLoss.setText(CurrencyValidation.formatCrore(observable.getTxtProfitLoss()));
                lblProfitLoss.setForeground(java.awt.Color.GREEN);
                txtProfitLoss.setForeground(java.awt.Color.GREEN);
            } else{
                lblProfitLoss.setText("LOSS");
                Double loss = new Double(-1 * CommonUtil.convertObjToDouble(observable.getTxtProfitLoss()).doubleValue());
                String lossStr = CommonUtil.convertObjToStr(loss);
                txtProfitLoss.setText(CurrencyValidation.formatCrore(lossStr));
                lblProfitLoss.setForeground(java.awt.Color.RED);
                txtProfitLoss.setForeground(java.awt.Color.RED);
            }
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
    private void setRightAlignment(com.see.truetransact.uicomponent.CTable _tblData, int col) {
        javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
        r.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        _tblData.getColumnModel().getColumn(col).setCellRenderer(r);
        _tblData.getColumnModel().getColumn(col).sizeWidthToFit();
    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtProfitAcHead.setText(observable.getTxtProfitAcHead());
        txtLossAcHead.setText(observable.getTxtLossAcHead());
        txtTotalIncome.setText(observable.getTxtTotalIncome());
        txtTotalExpenditure.setText(observable.getTxtTotalExpenditure());
        txtProfitLoss.setText(observable.getTxtProfitLoss());
        lblProfitLoss.setText(resourceBundle.getString("lblProfitLoss"));
        lblStatus.setText("Completed");
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtProfitAcHead(txtProfitAcHead.getText());
        observable.setTxtLossAcHead(txtLossAcHead.getText());
        observable.setTxtTotalIncome(txtTotalIncome.getText());
        observable.setTxtTotalExpenditure(txtTotalExpenditure.getText());
        observable.setTxtProfitLoss(txtProfitLoss.getText());
        observable.setLblProfitOrLoss(lblProfitLoss.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtProfitAcHead", new Boolean(true));
        mandatoryMap.put("txtLossAcHead", new Boolean(true));
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
        txtProfitAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProfitAcHead"));
        txtLossAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLossAcHead"));
        txtProfitLoss.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProfitLoss"));
    }
    
    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths(){
        txtProfitAcHead.setMaxLength(16);
        txtLossAcHead.setMaxLength(16);
        txtProfitAcHead.setAllowAll(true);
        txtLossAcHead.setAllowAll(true);
        txtTotalIncome.setValidation(new CurrencyValidation(16,2));
        txtTotalExpenditure.setValidation(new CurrencyValidation(16,2));
        txtProfitLoss.setValidation(new CurrencyValidation(16,2));
    }
    
    /*Makes the button Enable or Disable accordingly when usier clicks new,edit or delete buttons */
    private void setButtonEnableDisable() {
        txtProfitAcHead.setEnabled(true);
        txtLossAcHead.setEnabled(true);
        btnProfitAcHead.setEnabled(true);
        btnLossAcHead.setEnabled(true);
        btnProcess.setEnabled(true);
        txtTotalIncome.setEnabled(false);
        txtTotalExpenditure.setEnabled(false);
        txtProfitLoss.setEnabled(false);
        lblStatus.setText(observable.getLblStatus());
    }
    
    /** Method used to check whether the Mandatory Fields in the Form are Filled or not */
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    /** Method used to Give a Alert when any Mandatory Field is not filled by the user */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "OperativeAcctProduct.getSelectAcctHeadTOList");
        HashMap where = new HashMap();
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        viewMap.put(CommonConstants.MAP_WHERE, where);
        where = null;
        new ViewAll(this,viewMap).show();
    }
    
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object  map) {
        HashMap hash = (HashMap) map;
        if (viewType != null) {
            if (viewType.equals("ProfitHead")) {
                txtProfitAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
            } else if (viewType.equals("LossHead")) {
                txtLossAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
            }
        }
    }
    
    /** This will show the alertwindow **/
    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null,resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        return optionSelected;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        yearEndTabbedpan = new com.see.truetransact.uicomponent.CTabbedPane();
        panYearEndProcess = new com.see.truetransact.uicomponent.CPanel();
        panProfitLossHeads = new com.see.truetransact.uicomponent.CPanel();
        lblProfitAcHead = new com.see.truetransact.uicomponent.CLabel();
        lblLossAcHead = new com.see.truetransact.uicomponent.CLabel();
        panProfitAcHead = new com.see.truetransact.uicomponent.CPanel();
        txtProfitAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnProfitAcHead = new com.see.truetransact.uicomponent.CButton();
        panLossAcHead = new com.see.truetransact.uicomponent.CPanel();
        txtLossAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnLossAcHead = new com.see.truetransact.uicomponent.CButton();
        panTables = new com.see.truetransact.uicomponent.CPanel();
        srpIncomeHeads = new com.see.truetransact.uicomponent.CScrollPane();
        tblIncomeHeads = new com.see.truetransact.uicomponent.CTable();
        srpExpenditureHeads = new com.see.truetransact.uicomponent.CScrollPane();
        tblExpenditureHeads = new com.see.truetransact.uicomponent.CTable();
        txtTotalExpenditure = new com.see.truetransact.uicomponent.CTextField();
        txtTotalIncome = new com.see.truetransact.uicomponent.CTextField();
        panProfitLoss = new com.see.truetransact.uicomponent.CPanel();
        lblProfitLoss = new com.see.truetransact.uicomponent.CLabel();
        txtProfitLoss = new com.see.truetransact.uicomponent.CTextField();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        panCustomer = new com.see.truetransact.uicomponent.CPanel();
        panCustDetails = new com.see.truetransact.uicomponent.CPanel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        chkCustomerAge = new com.see.truetransact.uicomponent.CCheckBox();
        btnAgeProcess = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(850, 680));
        setPreferredSize(new java.awt.Dimension(850, 680));

        panYearEndProcess.setLayout(new java.awt.GridBagLayout());

        panProfitLossHeads.setLayout(new java.awt.GridBagLayout());

        lblProfitAcHead.setText("Profit A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panProfitLossHeads.add(lblProfitAcHead, gridBagConstraints);

        lblLossAcHead.setText("Loss A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panProfitLossHeads.add(lblLossAcHead, gridBagConstraints);

        panProfitAcHead.setLayout(new java.awt.GridBagLayout());

        txtProfitAcHead.setEditable(false);
        txtProfitAcHead.setAllowAll(true);
        txtProfitAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProfitAcHead.add(txtProfitAcHead, gridBagConstraints);

        btnProfitAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnProfitAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnProfitAcHead.setEnabled(false);
        btnProfitAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProfitAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProfitAcHead.add(btnProfitAcHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panProfitLossHeads.add(panProfitAcHead, gridBagConstraints);

        panLossAcHead.setLayout(new java.awt.GridBagLayout());

        txtLossAcHead.setEditable(false);
        txtLossAcHead.setAllowAll(true);
        txtLossAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLossAcHead.add(txtLossAcHead, gridBagConstraints);

        btnLossAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLossAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLossAcHead.setEnabled(false);
        btnLossAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLossAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLossAcHead.add(btnLossAcHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panProfitLossHeads.add(panLossAcHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panYearEndProcess.add(panProfitLossHeads, gridBagConstraints);

        panTables.setMinimumSize(new java.awt.Dimension(825, 500));
        panTables.setPreferredSize(new java.awt.Dimension(825, 500));
        panTables.setLayout(new java.awt.GridBagLayout());

        srpIncomeHeads.setMinimumSize(new java.awt.Dimension(410, 460));
        srpIncomeHeads.setPreferredSize(new java.awt.Dimension(420, 460));

        tblIncomeHeads.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Income Heads", "Income Head Description", "Balance"
            }
        ));
        tblIncomeHeads.setPreferredScrollableViewportSize(new java.awt.Dimension(430, 400));
        srpIncomeHeads.setViewportView(tblIncomeHeads);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panTables.add(srpIncomeHeads, gridBagConstraints);

        srpExpenditureHeads.setMinimumSize(new java.awt.Dimension(410, 460));
        srpExpenditureHeads.setPreferredSize(new java.awt.Dimension(420, 460));

        tblExpenditureHeads.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Expenditure Heads", "Expenditure Head Description", "Balance"
            }
        ));
        tblExpenditureHeads.setPreferredScrollableViewportSize(new java.awt.Dimension(430, 400));
        srpExpenditureHeads.setViewportView(tblExpenditureHeads);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panTables.add(srpExpenditureHeads, gridBagConstraints);

        txtTotalExpenditure.setEditable(false);
        txtTotalExpenditure.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotalExpenditure.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTables.add(txtTotalExpenditure, gridBagConstraints);

        txtTotalIncome.setEditable(false);
        txtTotalIncome.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotalIncome.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTables.add(txtTotalIncome, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panYearEndProcess.add(panTables, gridBagConstraints);

        panProfitLoss.setLayout(new java.awt.GridBagLayout());

        lblProfitLoss.setText("Profit / Loss");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProfitLoss.add(lblProfitLoss, gridBagConstraints);

        txtProfitLoss.setEditable(false);
        txtProfitLoss.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProfitLoss.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProfitLoss.add(txtProfitLoss, gridBagConstraints);

        btnProcess.setText("Process");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 90, 4, 11);
        panProfitLoss.add(btnProcess, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panYearEndProcess.add(panProfitLoss, gridBagConstraints);

        yearEndTabbedpan.addTab("Year Ending Process", panYearEndProcess);

        panCustDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Update Customer Age"));
        panCustDetails.setMinimumSize(new java.awt.Dimension(300, 200));
        panCustDetails.setPreferredSize(new java.awt.Dimension(300, 200));
        panCustDetails.setLayout(new java.awt.GridBagLayout());

        cLabel1.setText("Allow Customer Age Updation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCustDetails.add(cLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCustDetails.add(chkCustomerAge, gridBagConstraints);

        btnAgeProcess.setText("Process");
        btnAgeProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgeProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(18, 18, 18, 18);
        panCustDetails.add(btnAgeProcess, gridBagConstraints);

        javax.swing.GroupLayout panCustomerLayout = new javax.swing.GroupLayout(panCustomer);
        panCustomer.setLayout(panCustomerLayout);
        panCustomerLayout.setHorizontalGroup(
            panCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panCustomerLayout.createSequentialGroup()
                .addGap(178, 178, 178)
                .addComponent(panCustDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(309, Short.MAX_VALUE))
        );
        panCustomerLayout.setVerticalGroup(
            panCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panCustomerLayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(panCustDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(341, Short.MAX_VALUE))
        );

        yearEndTabbedpan.addTab("Customer", panCustomer);

        getContentPane().add(yearEndTabbedpan, java.awt.BorderLayout.PAGE_START);

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

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
//        ClientUtil.confirmationAlert("Please ensure only Income & Expenditure A/c Heads are taken into account.");
        if (txtProfitAcHead.getText().length()!=0) {
        int option = 1;
        try{
            String[] options = {"Yes","No"};
            option = COptionPane.showOptionDialog(null,"Please ensure only Income & Expenditure A/c Heads are taken into account.\n" +
            "Do you want to Continue?", CommonConstants.WARNINGTITLE,
            COptionPane.YES_NO_OPTION, COptionPane.WARNING_MESSAGE,
            null, options, options[1]);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (option == 0) {
            updateOBFields();
            observable.execute("");
            if (observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                observable.removeTblRow();
                update(null, null);
            }
            }
        } else {
            displayAlert("Please select Profit/Loss A/c Head");
        }
    }//GEN-LAST:event_btnProcessActionPerformed

    private void btnLossAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLossAcHeadActionPerformed
        // TODO add your handling code here:
        viewType = "LossHead";
        callView(viewType);
    }//GEN-LAST:event_btnLossAcHeadActionPerformed

    private void btnProfitAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProfitAcHeadActionPerformed
        // TODO add your handling code here:
        viewType = "ProfitHead";
        callView(viewType);
    }//GEN-LAST:event_btnProfitAcHeadActionPerformed

    private void btnAgeProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgeProcessActionPerformed
        if (chkCustomerAge.isSelected()) {
            int option = 1;
            try {
                String[] options = {"Yes", "No"};
                option = COptionPane.showOptionDialog(null, "Do you want to update customer age??", CommonConstants.WARNINGTITLE,
                        COptionPane.YES_NO_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (option == 0) {
//            if ((TrueTransactMain.BANKINFO.get("YEAREND_PROCESS_DT") != null
//                    && DateUtil.dateDiff((Date) TrueTransactMain.BANKINFO.get("YEAREND_PROCESS_DT"), currDt) == 0)) {
                    HashMap updateLoanMap = new HashMap();
                    updateLoanMap.put("PROCESS_DT", currDt.clone());
                    updateLoanMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                    updateLoanMap.put("CUSTOMER_AGE", "CUSTOMER_AGE");
                    System.out.println("sucesssssssss");
                    ClientUtil.execute("updateCustomerAge", updateLoanMap);
                    ClientUtil.showMessageWindow("Successfully Updated!!!");
              //  }

            }
        }
    }//GEN-LAST:event_btnAgeProcessActionPerformed
                                                                                            
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAgeProcess;
    private com.see.truetransact.uicomponent.CButton btnLossAcHead;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnProfitAcHead;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CCheckBox chkCustomerAge;
    private com.see.truetransact.uicomponent.CLabel lblLossAcHead;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProfitAcHead;
    private com.see.truetransact.uicomponent.CLabel lblProfitLoss;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CPanel panCustDetails;
    private com.see.truetransact.uicomponent.CPanel panCustomer;
    private com.see.truetransact.uicomponent.CPanel panLossAcHead;
    private com.see.truetransact.uicomponent.CPanel panProfitAcHead;
    private com.see.truetransact.uicomponent.CPanel panProfitLoss;
    private com.see.truetransact.uicomponent.CPanel panProfitLossHeads;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTables;
    private com.see.truetransact.uicomponent.CPanel panYearEndProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpExpenditureHeads;
    private com.see.truetransact.uicomponent.CScrollPane srpIncomeHeads;
    private com.see.truetransact.uicomponent.CTable tblExpenditureHeads;
    private com.see.truetransact.uicomponent.CTable tblIncomeHeads;
    private com.see.truetransact.uicomponent.CTextField txtLossAcHead;
    private com.see.truetransact.uicomponent.CTextField txtProfitAcHead;
    private com.see.truetransact.uicomponent.CTextField txtProfitLoss;
    private com.see.truetransact.uicomponent.CTextField txtTotalExpenditure;
    private com.see.truetransact.uicomponent.CTextField txtTotalIncome;
    private com.see.truetransact.uicomponent.CTabbedPane yearEndTabbedpan;
    // End of variables declaration//GEN-END:variables
    
}
