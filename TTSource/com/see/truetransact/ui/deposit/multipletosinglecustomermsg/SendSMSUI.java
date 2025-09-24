/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SendSMSUI.java
 *
 * Created on October 10th, 2011, 11:03 PM
 */
package com.see.truetransact.ui.deposit.multipletosinglecustomermsg;

/**
 *
 * @author
 */
import java.util.*;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.DateUtil;

public class SendSMSUI extends CInternalFrame implements Observer {

    /**
     * Vairable Declarations
     */
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    SendSMSOB observable = null;
    private boolean selectMode = false;
    private Date currDate = null;
    private HashMap returnMap = null;

    /**
     * Creates new form TokenConfigUI
     */
    public SendSMSUI() {
        returnMap = null;
        currDate = ClientUtil.getCurrentDate();
        initForm();
    }

    /**
     * Method which is used to initialize the form TokenConfig
     */
    private void initForm() {
        initComponents();
        observable = new SendSMSOB();
        initTableData();
        btnProcess.setEnabled(false);
        ClientUtil.enableDisable(panDepositInterestApplication, false);
        ClientUtil.enableDisable(panProductDetails, true);
        tdtTransDate.setDateValue(CommonUtil.convertObjToStr(currDate.clone()));
        observable.setTdtTransDate(tdtTransDate.getDateValue());
        tdtMDSTransDate.setDateValue(CommonUtil.convertObjToStr(currDate.clone()));
        observable.setTdtMDSTransDate(tdtMDSTransDate.getDateValue());
        tdtRenewTransDate.setDateValue(CommonUtil.convertObjToStr(currDate.clone()));
        observable.setTdtRenewTransDate(tdtRenewTransDate.getDateValue());
        txtSchemeName.setEnabled(false);
        btnSchemeName.setEnabled(true);
    }

    private void initTableData() {
        tblDepositInterestApplication.setModel(observable.getTblDepositInterestApplication());
        tblMDSPrizedMoneyDetails.setModel(observable.getTblMDSPrizedMoneyDetails());
        tblRenewTransTableData.setModel(observable.getTblRenewTransTableData());
    }

    /* Auto Generated Method - update()
     This method called by Observable. It updates the UI with
     Observable's data. If needed add/Remove RadioButtons
     method need to be added.*/
    public void update(Observable observed, Object arg) {

    }

    /* Auto Generated Method - updateOBFields()
     This method called by Save option of UI.
     It updates the OB with UI data.*/
    public void updateOBFields() {
    }

    /* Auto Generated Method - setMandatoryHashMap()
 
     ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
     This method list out all the Input Fields available in the UI.
     It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
    }

    /**
     * Used to set Maximum possible lenghts for TextFields
     */
    private void setMaxLengths() {
    }

    /**
     * Method used to Give a Alert when any Mandatory Field is not filled by the
     * user
     */
    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        HashMap hash = new HashMap();
        if (currField.equalsIgnoreCase("PROD_DETAILS")) {
            viewMap.put(CommonConstants.MAP_NAME, "getFixedDepositProducts");
        } else if (currField.equalsIgnoreCase("FROM") || currField.equalsIgnoreCase("TO")) {
            hash.put(CommonConstants.BRANCH_ID, com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, hash);
            viewMap.put(CommonConstants.MAP_NAME, "TDCharges.getAcctList");
        } else if (currField.equalsIgnoreCase("SI_NUMBER")) {
            viewMap.put(CommonConstants.MAP_NAME, "getDepositIntPayAccountNo");
        } else if (currField.equalsIgnoreCase("SCHEME_NAME")) {
            HashMap map = new HashMap();
            viewMap.put(CommonConstants.MAP_NAME, "getSelectEachSchemeDetails");
        }
        new ViewAll(this, viewMap).show();
    }

    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object map) {
        try {
            HashMap hash = (HashMap) map;
            System.out.println("#@@# Hash :" + hash);
            if (viewType != null) {
                if (viewType.equalsIgnoreCase("PROD_DETAILS")) {
//                    lblProductName.setText(CommonUtil.convertObjToStr(hash.get("PROD_DESC")));
                }
            }

            if (viewType.equals("Customer")) {
                //__ To reset the data for the Previous selected Customer..
                final String CUSTID = CommonUtil.convertObjToStr(hash.get("CUST_ID"));
                //__ To set the Name of the Customer...
                final String CUSTNAME = CommonUtil.convertObjToStr(hash.get("NAME"));
//                lblCustName.setText(CUSTNAME);
            } else if (viewType.equals("SCHEME_NAME")) {
                //__ To reset the data for the Previous selected Customer..
                final String schemeName = CommonUtil.convertObjToStr(hash.get("SCHEME_NAME"));
                txtSchemeName.setText(schemeName);
                observable.setTxtSchemeName(schemeName);
                //__ To set the Name of the Customer...
//                final String CUSTNAME = CommonUtil.convertObjToStr(hash.get("NAME"));
//                lblCustName.setText(CUSTNAME);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tabChequeBook = new com.see.truetransact.uicomponent.CTabbedPane();
        panDepositInterestApplication = new com.see.truetransact.uicomponent.CPanel();
        panProductDetails = new com.see.truetransact.uicomponent.CPanel();
        btnCalculate = new com.see.truetransact.uicomponent.CButton();
        tdtTransDate = new com.see.truetransact.uicomponent.CDateField();
        lblTransDate = new com.see.truetransact.uicomponent.CLabel();
        panProductTableData = new com.see.truetransact.uicomponent.CPanel();
        srpDepositInterestApplication = new com.see.truetransact.uicomponent.CScrollPane();
        tblDepositInterestApplication = new com.see.truetransact.uicomponent.CTable();
        panSelectAll = new com.see.truetransact.uicomponent.CPanel();
        lblSelectAll = new com.see.truetransact.uicomponent.CLabel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        panProcess = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        panRenewTransInterestApp = new com.see.truetransact.uicomponent.CPanel();
        lblRenewTransInterestApp = new com.see.truetransact.uicomponent.CLabel();
        chkRenewTransInterestApp = new com.see.truetransact.uicomponent.CCheckBox();
        panPrizedMoneyDetails = new com.see.truetransact.uicomponent.CPanel();
        panMDSPrizedMoneyDetails = new com.see.truetransact.uicomponent.CPanel();
        btnMDSCalculate = new com.see.truetransact.uicomponent.CButton();
        tdtMDSTransDate = new com.see.truetransact.uicomponent.CDateField();
        lblMDSTransDate = new com.see.truetransact.uicomponent.CLabel();
        panMDSCustomerId = new com.see.truetransact.uicomponent.CPanel();
        txtSchemeName = new com.see.truetransact.uicomponent.CTextField();
        btnSchemeName = new com.see.truetransact.uicomponent.CButton();
        lblSchemeName = new com.see.truetransact.uicomponent.CLabel();
        panMDSProductTableData = new com.see.truetransact.uicomponent.CPanel();
        srpMDSPrizedMoneyDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblMDSPrizedMoneyDetails = new com.see.truetransact.uicomponent.CTable();
        panMDSSelectAll = new com.see.truetransact.uicomponent.CPanel();
        lblMDSSelectAll = new com.see.truetransact.uicomponent.CLabel();
        chkMDSSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        panMDSProcess = new com.see.truetransact.uicomponent.CPanel();
        btnMDSClose = new com.see.truetransact.uicomponent.CButton();
        btnMDSClear = new com.see.truetransact.uicomponent.CButton();
        btnMDSProcess = new com.see.truetransact.uicomponent.CButton();
        panDepositMultipleRenewal = new com.see.truetransact.uicomponent.CPanel();
        panRenewTrans = new com.see.truetransact.uicomponent.CPanel();
        btnRenewTransDisplay = new com.see.truetransact.uicomponent.CButton();
        tdtRenewTransDate = new com.see.truetransact.uicomponent.CDateField();
        lblRenewTransDate = new com.see.truetransact.uicomponent.CLabel();
        panRenewTransTableData = new com.see.truetransact.uicomponent.CPanel();
        srpRenewTransTableData = new com.see.truetransact.uicomponent.CScrollPane();
        tblRenewTransTableData = new com.see.truetransact.uicomponent.CTable();
        panRenewTransSelectAll = new com.see.truetransact.uicomponent.CPanel();
        lblRenewTransSelectAll = new com.see.truetransact.uicomponent.CLabel();
        chkRenewTransSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        panRenewTransProcess = new com.see.truetransact.uicomponent.CPanel();
        btnRenewTransClose = new com.see.truetransact.uicomponent.CButton();
        btnRenewTransClear = new com.see.truetransact.uicomponent.CButton();
        btnRenewTransProcess = new com.see.truetransact.uicomponent.CButton();
        panRenewTransSingleSelect = new com.see.truetransact.uicomponent.CPanel();
        lblRenewTransSingle = new com.see.truetransact.uicomponent.CLabel();
        chkRenewTransSingle = new com.see.truetransact.uicomponent.CCheckBox();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(700, 620));
        setMinimumSize(new java.awt.Dimension(700, 620));
        setPreferredSize(new java.awt.Dimension(700, 620));

        tabChequeBook.setMinimumSize(new java.awt.Dimension(750, 520));
        tabChequeBook.setPreferredSize(new java.awt.Dimension(750, 520));

        panDepositInterestApplication.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panDepositInterestApplication.setMaximumSize(new java.awt.Dimension(650, 450));
        panDepositInterestApplication.setMinimumSize(new java.awt.Dimension(650, 450));
        panDepositInterestApplication.setPreferredSize(new java.awt.Dimension(650, 450));
        panDepositInterestApplication.setLayout(new java.awt.GridBagLayout());

        panProductDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panProductDetails.setMinimumSize(new java.awt.Dimension(630, 40));
        panProductDetails.setPreferredSize(new java.awt.Dimension(630, 40));
        panProductDetails.setLayout(new java.awt.GridBagLayout());

        btnCalculate.setText("Display");
        btnCalculate.setMaximumSize(new java.awt.Dimension(89, 21));
        btnCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductDetails.add(btnCalculate, gridBagConstraints);

        tdtTransDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtTransDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtTransDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtTransDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductDetails.add(tdtTransDate, gridBagConstraints);

        lblTransDate.setText("Trans Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductDetails.add(lblTransDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 6, 1);
        panDepositInterestApplication.add(panProductDetails, gridBagConstraints);

        panProductTableData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panProductTableData.setMinimumSize(new java.awt.Dimension(630, 360));
        panProductTableData.setPreferredSize(new java.awt.Dimension(630, 360));
        panProductTableData.setLayout(new java.awt.GridBagLayout());

        srpDepositInterestApplication.setMinimumSize(new java.awt.Dimension(610, 335));
        srpDepositInterestApplication.setPreferredSize(new java.awt.Dimension(610, 335));

        tblDepositInterestApplication.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Cust ID", "Name", "Trans Date", "Record Count"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblDepositInterestApplication.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 331));
        tblDepositInterestApplication.setSelectionBackground(new java.awt.Color(255, 255, 102));
        tblDepositInterestApplication.setSelectionForeground(new java.awt.Color(255, 0, 0));
        tblDepositInterestApplication.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDepositInterestApplicationMouseClicked(evt);
            }
        });
        srpDepositInterestApplication.setViewportView(tblDepositInterestApplication);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        panProductTableData.add(srpDepositInterestApplication, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panDepositInterestApplication.add(panProductTableData, gridBagConstraints);
        panProductTableData.getAccessibleContext().setAccessibleName("Deposit Interest Application Details");

        panSelectAll.setMinimumSize(new java.awt.Dimension(101, 27));
        panSelectAll.setPreferredSize(new java.awt.Dimension(101, 27));
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDepositInterestApplication.add(panSelectAll, gridBagConstraints);

        panProcess.setMinimumSize(new java.awt.Dimension(630, 35));
        panProcess.setPreferredSize(new java.awt.Dimension(630, 35));
        panProcess.setLayout(new java.awt.GridBagLayout());

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(100, 35));
        btnClose.setMinimumSize(new java.awt.Dimension(100, 35));
        btnClose.setPreferredSize(new java.awt.Dimension(100, 35));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnClose, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.setMaximumSize(new java.awt.Dimension(100, 35));
        btnClear.setMinimumSize(new java.awt.Dimension(100, 35));
        btnClear.setPreferredSize(new java.awt.Dimension(100, 35));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnClear, gridBagConstraints);

        btnProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/send_sms.jpg"))); // NOI18N
        btnProcess.setText("PROCESS");
        btnProcess.setMaximumSize(new java.awt.Dimension(150, 35));
        btnProcess.setMinimumSize(new java.awt.Dimension(150, 35));
        btnProcess.setPreferredSize(new java.awt.Dimension(150, 35));
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 120, 4, 4);
        panProcess.add(btnProcess, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panDepositInterestApplication.add(panProcess, gridBagConstraints);

        panRenewTransInterestApp.setLayout(new java.awt.GridBagLayout());

        lblRenewTransInterestApp.setText("Show SB balance in SMS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 19, 5, 0);
        panRenewTransInterestApp.add(lblRenewTransInterestApp, gridBagConstraints);

        chkRenewTransInterestApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRenewTransInterestAppActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 5, 1);
        panRenewTransInterestApp.add(chkRenewTransInterestApp, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panDepositInterestApplication.add(panRenewTransInterestApp, gridBagConstraints);

        tabChequeBook.addTab("SMS For Deposit Interest Transfer", panDepositInterestApplication);

        panPrizedMoneyDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panPrizedMoneyDetails.setMaximumSize(new java.awt.Dimension(650, 450));
        panPrizedMoneyDetails.setMinimumSize(new java.awt.Dimension(650, 450));
        panPrizedMoneyDetails.setPreferredSize(new java.awt.Dimension(650, 450));
        panPrizedMoneyDetails.setLayout(new java.awt.GridBagLayout());

        panMDSPrizedMoneyDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panMDSPrizedMoneyDetails.setMinimumSize(new java.awt.Dimension(630, 40));
        panMDSPrizedMoneyDetails.setPreferredSize(new java.awt.Dimension(630, 40));
        panMDSPrizedMoneyDetails.setLayout(new java.awt.GridBagLayout());

        btnMDSCalculate.setText("Display");
        btnMDSCalculate.setMaximumSize(new java.awt.Dimension(89, 21));
        btnMDSCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMDSCalculateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSPrizedMoneyDetails.add(btnMDSCalculate, gridBagConstraints);

        tdtMDSTransDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtMDSTransDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtMDSTransDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtMDSTransDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSPrizedMoneyDetails.add(tdtMDSTransDate, gridBagConstraints);

        lblMDSTransDate.setText("Trans Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSPrizedMoneyDetails.add(lblMDSTransDate, gridBagConstraints);

        panMDSCustomerId.setPreferredSize(new java.awt.Dimension(122, 21));
        panMDSCustomerId.setLayout(new java.awt.GridBagLayout());

        txtSchemeName.setAllowAll(true);
        txtSchemeName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSchemeName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSchemeNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMDSCustomerId.add(txtSchemeName, gridBagConstraints);

        btnSchemeName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSchemeName.setEnabled(false);
        btnSchemeName.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSchemeName.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSchemeName.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSchemeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSchemeNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panMDSCustomerId.add(btnSchemeName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMDSPrizedMoneyDetails.add(panMDSCustomerId, gridBagConstraints);

        lblSchemeName.setText("MDS Scheme Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMDSPrizedMoneyDetails.add(lblSchemeName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 6, 1);
        panPrizedMoneyDetails.add(panMDSPrizedMoneyDetails, gridBagConstraints);

        panMDSProductTableData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panMDSProductTableData.setMinimumSize(new java.awt.Dimension(630, 360));
        panMDSProductTableData.setPreferredSize(new java.awt.Dimension(630, 360));
        panMDSProductTableData.setLayout(new java.awt.GridBagLayout());

        srpMDSPrizedMoneyDetails.setMinimumSize(new java.awt.Dimension(610, 335));
        srpMDSPrizedMoneyDetails.setPreferredSize(new java.awt.Dimension(610, 335));

        tblMDSPrizedMoneyDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Cust ID", "Name", "Trans Date", "Record Count"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblMDSPrizedMoneyDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 331));
        tblMDSPrizedMoneyDetails.setSelectionBackground(new java.awt.Color(255, 255, 102));
        tblMDSPrizedMoneyDetails.setSelectionForeground(new java.awt.Color(255, 0, 0));
        tblMDSPrizedMoneyDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMDSPrizedMoneyDetailsMouseClicked(evt);
            }
        });
        srpMDSPrizedMoneyDetails.setViewportView(tblMDSPrizedMoneyDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        panMDSProductTableData.add(srpMDSPrizedMoneyDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panPrizedMoneyDetails.add(panMDSProductTableData, gridBagConstraints);

        panMDSSelectAll.setLayout(new java.awt.GridBagLayout());

        lblMDSSelectAll.setText("Select All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 19, 5, 0);
        panMDSSelectAll.add(lblMDSSelectAll, gridBagConstraints);

        chkMDSSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMDSSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 5, 1);
        panMDSSelectAll.add(chkMDSSelectAll, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPrizedMoneyDetails.add(panMDSSelectAll, gridBagConstraints);

        panMDSProcess.setMinimumSize(new java.awt.Dimension(630, 35));
        panMDSProcess.setPreferredSize(new java.awt.Dimension(630, 35));
        panMDSProcess.setLayout(new java.awt.GridBagLayout());

        btnMDSClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnMDSClose.setText("Close");
        btnMDSClose.setMaximumSize(new java.awt.Dimension(100, 35));
        btnMDSClose.setMinimumSize(new java.awt.Dimension(100, 35));
        btnMDSClose.setPreferredSize(new java.awt.Dimension(100, 35));
        btnMDSClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMDSCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSProcess.add(btnMDSClose, gridBagConstraints);

        btnMDSClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnMDSClear.setText("Clear");
        btnMDSClear.setMaximumSize(new java.awt.Dimension(100, 35));
        btnMDSClear.setMinimumSize(new java.awt.Dimension(100, 35));
        btnMDSClear.setPreferredSize(new java.awt.Dimension(100, 35));
        btnMDSClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMDSClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSProcess.add(btnMDSClear, gridBagConstraints);

        btnMDSProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/send_sms.jpg"))); // NOI18N
        btnMDSProcess.setText("PROCESS");
        btnMDSProcess.setMaximumSize(new java.awt.Dimension(150, 35));
        btnMDSProcess.setMinimumSize(new java.awt.Dimension(150, 35));
        btnMDSProcess.setPreferredSize(new java.awt.Dimension(150, 35));
        btnMDSProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMDSProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 120, 4, 4);
        panMDSProcess.add(btnMDSProcess, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panPrizedMoneyDetails.add(panMDSProcess, gridBagConstraints);

        tabChequeBook.addTab("SMS For MDS Prized Money Details", panPrizedMoneyDetails);

        panDepositMultipleRenewal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panDepositMultipleRenewal.setMaximumSize(new java.awt.Dimension(650, 450));
        panDepositMultipleRenewal.setMinimumSize(new java.awt.Dimension(650, 450));
        panDepositMultipleRenewal.setPreferredSize(new java.awt.Dimension(650, 450));
        panDepositMultipleRenewal.setLayout(new java.awt.GridBagLayout());

        panRenewTrans.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panRenewTrans.setMinimumSize(new java.awt.Dimension(630, 40));
        panRenewTrans.setPreferredSize(new java.awt.Dimension(630, 40));
        panRenewTrans.setLayout(new java.awt.GridBagLayout());

        btnRenewTransDisplay.setText("Display");
        btnRenewTransDisplay.setMaximumSize(new java.awt.Dimension(89, 21));
        btnRenewTransDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenewTransDisplayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRenewTrans.add(btnRenewTransDisplay, gridBagConstraints);

        tdtRenewTransDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtRenewTransDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtRenewTransDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtRenewTransDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRenewTrans.add(tdtRenewTransDate, gridBagConstraints);

        lblRenewTransDate.setText("Trans Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRenewTrans.add(lblRenewTransDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 6, 1);
        panDepositMultipleRenewal.add(panRenewTrans, gridBagConstraints);

        panRenewTransTableData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panRenewTransTableData.setMinimumSize(new java.awt.Dimension(630, 360));
        panRenewTransTableData.setPreferredSize(new java.awt.Dimension(630, 360));
        panRenewTransTableData.setLayout(new java.awt.GridBagLayout());

        srpRenewTransTableData.setMinimumSize(new java.awt.Dimension(610, 335));
        srpRenewTransTableData.setPreferredSize(new java.awt.Dimension(610, 335));

        tblRenewTransTableData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Cust ID", "Name", "Trans Date", "Record Count"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblRenewTransTableData.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 331));
        tblRenewTransTableData.setSelectionBackground(new java.awt.Color(255, 255, 102));
        tblRenewTransTableData.setSelectionForeground(new java.awt.Color(255, 0, 0));
        tblRenewTransTableData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRenewTransTableDataMouseClicked(evt);
            }
        });
        srpRenewTransTableData.setViewportView(tblRenewTransTableData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        panRenewTransTableData.add(srpRenewTransTableData, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panDepositMultipleRenewal.add(panRenewTransTableData, gridBagConstraints);

        panRenewTransSelectAll.setLayout(new java.awt.GridBagLayout());

        lblRenewTransSelectAll.setText("Select All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 19, 5, 0);
        panRenewTransSelectAll.add(lblRenewTransSelectAll, gridBagConstraints);

        chkRenewTransSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRenewTransSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 5, 1);
        panRenewTransSelectAll.add(chkRenewTransSelectAll, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDepositMultipleRenewal.add(panRenewTransSelectAll, gridBagConstraints);

        panRenewTransProcess.setMinimumSize(new java.awt.Dimension(630, 35));
        panRenewTransProcess.setPreferredSize(new java.awt.Dimension(630, 35));
        panRenewTransProcess.setLayout(new java.awt.GridBagLayout());

        btnRenewTransClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnRenewTransClose.setText("Close");
        btnRenewTransClose.setMaximumSize(new java.awt.Dimension(100, 35));
        btnRenewTransClose.setMinimumSize(new java.awt.Dimension(100, 35));
        btnRenewTransClose.setPreferredSize(new java.awt.Dimension(100, 35));
        btnRenewTransClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenewTransCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRenewTransProcess.add(btnRenewTransClose, gridBagConstraints);

        btnRenewTransClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnRenewTransClear.setText("Clear");
        btnRenewTransClear.setMaximumSize(new java.awt.Dimension(100, 35));
        btnRenewTransClear.setMinimumSize(new java.awt.Dimension(100, 35));
        btnRenewTransClear.setPreferredSize(new java.awt.Dimension(100, 35));
        btnRenewTransClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenewTransClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRenewTransProcess.add(btnRenewTransClear, gridBagConstraints);

        btnRenewTransProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/send_sms.jpg"))); // NOI18N
        btnRenewTransProcess.setText("PROCESS");
        btnRenewTransProcess.setMaximumSize(new java.awt.Dimension(150, 35));
        btnRenewTransProcess.setMinimumSize(new java.awt.Dimension(150, 35));
        btnRenewTransProcess.setPreferredSize(new java.awt.Dimension(150, 35));
        btnRenewTransProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenewTransProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 120, 4, 4);
        panRenewTransProcess.add(btnRenewTransProcess, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panDepositMultipleRenewal.add(panRenewTransProcess, gridBagConstraints);

        panRenewTransSingleSelect.setLayout(new java.awt.GridBagLayout());

        lblRenewTransSingle.setText("Show SB balance in SMS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 19, 5, 0);
        panRenewTransSingleSelect.add(lblRenewTransSingle, gridBagConstraints);

        chkRenewTransSingle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRenewTransSingleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 5, 1);
        panRenewTransSingleSelect.add(chkRenewTransSingle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panDepositMultipleRenewal.add(panRenewTransSingleSelect, gridBagConstraints);

        tabChequeBook.addTab("SMS For Deposit Multiple Renewal", panDepositMultipleRenewal);

        getContentPane().add(tabChequeBook, java.awt.BorderLayout.PAGE_START);
        tabChequeBook.getAccessibleContext().setAccessibleName("");

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

    private void enableDisable(boolean enable) {
//        ClientUtil.enableDisable(panTransType, enable, false, true);
        btnCalculate.setEnabled(enable);
    }

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        ClientUtil.enableDisable(panDepositInterestApplication, false);
        ClientUtil.clearAll(this);
        btnCalculate.setEnabled(true);
        btnProcess.setEnabled(false);
        chkSelectAll.setEnabled(false);
        tdtTransDate.setEnabled(true);
        tdtTransDate.setDateValue(CommonUtil.convertObjToStr(currDate.clone()));
        observable.setTdtTransDate(tdtTransDate.getDateValue());
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        List finalList = new ArrayList();
        List interestList = null;
        btnCalculate.setEnabled(false);
        chkSelectAll.setEnabled(false);
        btnProcess.setEnabled(false);
        if (observable.getFinalList() != null && observable.getFinalList().size() > 0) {
            System.out.println("#$@$#@$@$@ observable FinalList : " + observable.getFinalList());
            for (int i = 0; i < observable.getFinalList().size(); i++) {
                String custId = "";
                interestList = (ArrayList) observable.getFinalList().get(i);
                custId = CommonUtil.convertObjToStr(interestList.get(1));
                System.out.println("custId : " + custId);
                HashMap tempMap = new HashMap();
                for (int j = 0; j < tblDepositInterestApplication.getRowCount(); j++) {
                    if (CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(j, 1)).equals(custId) && ((Boolean) tblDepositInterestApplication.getValueAt(j, 0)).booleanValue()) {
                        tempMap = new HashMap();
                        tempMap.put("CUST_ID", tblDepositInterestApplication.getValueAt(j, 1));
                        tempMap.put("TOTAL_COUNT", tblDepositInterestApplication.getValueAt(j, 4));
                        finalList.add(tempMap);
                    }
                }
            }
            System.out.println("#$#$$# final List:" + finalList);
            if (finalList != null && finalList.size() > 0) {
                selectMode = false;
                observable.doAction(finalList, "DEPOSIT_INTEREST_SCREEN");
                if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                    if (observable.getProxyReturnMap() == null || observable.getProxyReturnMap().size() == 0) {
                        ClientUtil.showMessageWindow(" Transaction Completed !!! ");
                        btnClearActionPerformed(null);
                    } else {
                        returnMap = observable.getProxyReturnMap();
                        List errorList = (ArrayList) returnMap.get("INTEREST_DATA");
                        List transList = new ArrayList();
                        if (errorList != null && errorList.size() > 0) {
                            for (int j = 0; j < errorList.size(); j++) {
                                if (errorList.get(j) instanceof HashMap) {
                                    returnMap = (HashMap) errorList.get(j);
                                } else {
                                    transList.add(errorList.get(j));
                                }
                            }
                        }
                        System.out.println("#$#$$# returnMap:" + returnMap);
                        System.out.println("#$#$$# transList:" + transList);
                        ArrayList head = observable.getTableTitle();
                        ArrayList title = new ArrayList();
                        title.addAll(head);
                        title.add("Status");
                        title.remove(0);
                        ArrayList rowList = null;
                        EnhancedTableModel tbm = observable.getTblDepositInterestApplication();
                        ArrayList data = tbm.getDataArrayList();
                        for (int i = 0; i < tblDepositInterestApplication.getRowCount(); i++) {
                            rowList = (ArrayList) data.get(i);
                            if (returnMap.containsKey(tblDepositInterestApplication.getValueAt(i, 2))) {
                                rowList.add("Error");
                            } else {
                                if (((Boolean) tblDepositInterestApplication.getValueAt(i, 0)).booleanValue()) {
                                    rowList.add("Completed");
                                } else {
                                    rowList.add("Not Processed");
                                }
                            }
                            rowList.remove(0);
                        }
                        tbm.setDataArrayList(data, title);
                        javax.swing.table.TableColumn col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(0));
                        col.setMaxWidth(100);
                        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(1));
                        col.setMaxWidth(150);
                        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(2));
                        col.setMaxWidth(120);
                        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(3));
                        col.setMaxWidth(120);
                        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(4));
                        col.setMaxWidth(120);
                    }
                }
            } else {
                ClientUtil.showMessageWindow(" NO Rows Selected !!! ");
                btnProcess.setEnabled(true);
            }
        }
    }//GEN-LAST:event_btnProcessActionPerformed

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
        boolean flag;
        if (chkSelectAll.isSelected() == true) {
//            System.out.println("chkSelectAll.isSelected() if : " + chkSelectAll.isSelected());
            flag = true;
        } else {
//            System.out.println("chkSelectAll.isSelected() else : " + chkSelectAll.isSelected());
            flag = false;
        }
//        int totalCount = 0;
        for (int i = 0; i < tblDepositInterestApplication.getRowCount(); i++) {
//            System.out.println("chkSelectAll.isSelected() else : " + tblDepositInterestApplication.getValueAt(i, 4));
            if (tblDepositInterestApplication.getValueAt(i, 4).equals("Error")) {
            } else {
                tblDepositInterestApplication.setValueAt(new Boolean(flag), i, 0);
            }
        }

    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void tblDepositInterestApplicationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDepositInterestApplicationMouseClicked
        // TODO add your handling code here:
        if (selectMode == true) {
            String st = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 0));
            if (st.equals("true")) {
                tblDepositInterestApplication.setValueAt(new Boolean(false), tblDepositInterestApplication.getSelectedRow(), 0);
            } else {
                tblDepositInterestApplication.setValueAt(new Boolean(true), tblDepositInterestApplication.getSelectedRow(), 0);
            }
        }
        if (evt.getClickCount() == 2) {
//            HashMap returnMap = new HashMap();
            if (returnMap != null) {
//                returnMap = observable.getProxyReturnMap();
                if (returnMap.containsKey(tblDepositInterestApplication.getValueAt(
                        tblDepositInterestApplication.getSelectedRow(), 1))) {
                    TTException exception = (TTException) returnMap.get(tblDepositInterestApplication.getValueAt(
                            tblDepositInterestApplication.getSelectedRow(), 1));
                    parseException.logException(exception, true);
                }
            }
        }
    }//GEN-LAST:event_tblDepositInterestApplicationMouseClicked

    private void btnCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateActionPerformed
        // TODO add your handling code here:
        if (tdtTransDate.getDateValue().length() == 0) {
            ClientUtil.showAlertWindow("Please select transaction date");
            return;
//        } else if (tdtTransDate.getDateValue().length() > 0 && DateUtil.dateDiff((Date) currDate.clone(), DateUtil.getDateMMDDYYYY(tdtTransDate.getDateValue())) > 0) {
//            ClientUtil.showAlertWindow("Trasaction date should not be future date..,");
//            return;
        } else {
            btnCalculateActionPerformed();
        }
    }//GEN-LAST:event_btnCalculateActionPerformed

    private void tdtTransDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtTransDateFocusLost
        // TODO add your handling code here:
        if (tdtTransDate.getDateValue().length() > 0) {
            observable.setTdtTransDate(tdtTransDate.getDateValue());
        }
    }//GEN-LAST:event_tdtTransDateFocusLost

    private void btnMDSCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMDSCalculateActionPerformed
        // TODO add your handling code here:
        if (tdtMDSTransDate.getDateValue().length() == 0) {
            ClientUtil.showAlertWindow("Please select transaction date!!!");
            return;
        } else if (txtSchemeName.getText() != null && txtSchemeName.getText().length() == 0) {
            ClientUtil.showAlertWindow("Please select scheme name!!!");
            return;
        } else {
            btnCalculateActionPerformed();
        }
    }//GEN-LAST:event_btnMDSCalculateActionPerformed

    private void tdtMDSTransDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtMDSTransDateFocusLost
        // TODO add your handling code here:
//        if (tdtMDSTransDate.getDateValue().length() == 0) {
//            ClientUtil.showAlertWindow("Please select transaction date");
//            return;
//        } else {
        observable.setTdtMDSTransDate(tdtMDSTransDate.getDateValue());
//            btnCalculateActionPerformed();
//        }
    }//GEN-LAST:event_tdtMDSTransDateFocusLost

    private void tblMDSPrizedMoneyDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMDSPrizedMoneyDetailsMouseClicked
        // TODO add your handling code here:
        if (selectMode == true) {
            String st = CommonUtil.convertObjToStr(tblMDSPrizedMoneyDetails.getValueAt(tblMDSPrizedMoneyDetails.getSelectedRow(), 0));
            if (st.equals("true")) {
                tblMDSPrizedMoneyDetails.setValueAt(new Boolean(false), tblMDSPrizedMoneyDetails.getSelectedRow(), 0);
            } else {
                tblMDSPrizedMoneyDetails.setValueAt(new Boolean(true), tblMDSPrizedMoneyDetails.getSelectedRow(), 0);
            }
        }
        if (evt.getClickCount() == 2) {
//            HashMap returnMap = new HashMap();
            if (returnMap != null) {
//                returnMap = observable.getProxyReturnMap();
                if (returnMap.containsKey(tblMDSPrizedMoneyDetails.getValueAt(
                        tblDepositInterestApplication.getSelectedRow(), 1))) {
                    TTException exception = (TTException) returnMap.get(tblMDSPrizedMoneyDetails.getValueAt(
                            tblMDSPrizedMoneyDetails.getSelectedRow(), 1));
                    parseException.logException(exception, true);
                }
            }
        }
    }//GEN-LAST:event_tblMDSPrizedMoneyDetailsMouseClicked

    private void chkMDSSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMDSSelectAllActionPerformed
        // TODO add your handling code here:
        boolean flag;
//        System.out.println("chkMDSSelectAll.isSelected() : "+chkMDSSelectAll.isSelected());
        if (chkMDSSelectAll.isSelected() == true) {
//            System.out.println("chkMDSSelectAll.isSelected() if : "+chkMDSSelectAll.isSelected());
            flag = true;
        } else {
//            System.out.println("chkMDSSelectAll.isSelected() else : "+chkMDSSelectAll.isSelected());
            flag = false;
        }
//        int totalCount = 0;
        for (int i = 0; i < tblMDSPrizedMoneyDetails.getRowCount(); i++) {
            if (tblMDSPrizedMoneyDetails.getValueAt(i, 4).equals("Error")) {
            } else {
                tblMDSPrizedMoneyDetails.setValueAt(new Boolean(flag), i, 0);
            }
        }
    }//GEN-LAST:event_chkMDSSelectAllActionPerformed

    private void btnMDSCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMDSCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnMDSCloseActionPerformed

    private void btnMDSClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMDSClearActionPerformed
        // TODO add your handling code here:
        observable.resetMDSForm();
        ClientUtil.enableDisable(panMDSPrizedMoneyDetails, false);
        ClientUtil.clearAll(this);
        btnMDSCalculate.setEnabled(true);
        chkMDSSelectAll.setEnabled(false);
        tdtMDSTransDate.setEnabled(true);
        tdtMDSTransDate.setDateValue(CommonUtil.convertObjToStr(currDate.clone()));
        observable.setTdtMDSTransDate(tdtMDSTransDate.getDateValue());
    }//GEN-LAST:event_btnMDSClearActionPerformed

    private void btnMDSProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMDSProcessActionPerformed
        // TODO add your handling code here:
        List finalList = new ArrayList();
        List interestList = null;
        btnMDSCalculate.setEnabled(false);
        chkMDSSelectAll.setEnabled(false);
        btnMDSProcess.setEnabled(false);
        if (observable.getFinalList() != null && observable.getFinalList().size() > 0) {
            System.out.println("#$@$#@$@$@ observable FinalList : " + observable.getFinalList());
            for (int i = 0; i < observable.getFinalList().size(); i++) {
                String custId = "";
                interestList = (ArrayList) observable.getFinalList().get(i);
                custId = CommonUtil.convertObjToStr(interestList.get(1));
                System.out.println("custId : " + custId);
                HashMap tempMap = new HashMap();
                for (int j = 0; j < tblMDSPrizedMoneyDetails.getRowCount(); j++) {
                    if (CommonUtil.convertObjToStr(tblMDSPrizedMoneyDetails.getValueAt(j, 1)).equals(custId) && ((Boolean) tblMDSPrizedMoneyDetails.getValueAt(j, 0)).booleanValue()) {
                        tempMap = new HashMap();
                        tempMap.put("CUST_ID", tblMDSPrizedMoneyDetails.getValueAt(j, 1));
                        tempMap.put("TOTAL_COUNT", tblMDSPrizedMoneyDetails.getValueAt(j, 4));
                        finalList.add(tempMap);
                    }
                }
            }
            System.out.println("#$#$$# final List:" + finalList);
            if (finalList != null && finalList.size() > 0) {
                selectMode = false;
                observable.doAction(finalList, "MDS_PRIZED_MONEY_DETAILS_SCREEN");
                if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                    if (observable.getProxyReturnMap() == null || observable.getProxyReturnMap().size() == 0) {
                        ClientUtil.showMessageWindow(" Transaction Completed !!! ");
                        btnClearActionPerformed(null);
                    } else {
                        returnMap = observable.getProxyReturnMap();
                        List errorList = (ArrayList) returnMap.get("INTEREST_DATA");
                        List transList = new ArrayList();
                        if (errorList != null && errorList.size() > 0) {
                            for (int j = 0; j < errorList.size(); j++) {
                                if (errorList.get(j) instanceof HashMap) {
                                    returnMap = (HashMap) errorList.get(j);
                                } else {
                                    transList.add(errorList.get(j));
                                }
                            }
                        }
                        System.out.println("#$#$$# returnMap:" + returnMap);
                        System.out.println("#$#$$# transList:" + transList);
                        ArrayList head = observable.getTableTitle();
                        ArrayList title = new ArrayList();
                        title.addAll(head);
                        title.add("Status");
                        title.remove(0);
                        ArrayList rowList = null;
                        EnhancedTableModel tbm = observable.getTblMDSPrizedMoneyDetails();
                        ArrayList data = tbm.getDataArrayList();
                        for (int i = 0; i < tblMDSPrizedMoneyDetails.getRowCount(); i++) {
                            rowList = (ArrayList) data.get(i);
                            if (returnMap.containsKey(tblMDSPrizedMoneyDetails.getValueAt(i, 2))) {
                                rowList.add("Error");
                            } else {
                                if (((Boolean) tblMDSPrizedMoneyDetails.getValueAt(i, 0)).booleanValue()) {
                                    rowList.add("Completed");
                                } else {
                                    rowList.add("Not Processed");
                                }
                            }
                            rowList.remove(0);
                        }
                        tbm.setDataArrayList(data, title);
                        javax.swing.table.TableColumn col = tblMDSPrizedMoneyDetails.getColumn(tblMDSPrizedMoneyDetails.getColumnName(0));
                        col.setMaxWidth(100);
                        col = tblMDSPrizedMoneyDetails.getColumn(tblMDSPrizedMoneyDetails.getColumnName(1));
                        col.setMaxWidth(150);
                        col = tblMDSPrizedMoneyDetails.getColumn(tblMDSPrizedMoneyDetails.getColumnName(2));
                        col.setMaxWidth(120);
                        col = tblMDSPrizedMoneyDetails.getColumn(tblMDSPrizedMoneyDetails.getColumnName(3));
                        col.setMaxWidth(120);
                        col = tblMDSPrizedMoneyDetails.getColumn(tblMDSPrizedMoneyDetails.getColumnName(4));
                        col.setMaxWidth(120);
                    }
                }
            } else {
                ClientUtil.showMessageWindow(" NO Rows Selected !!! ");
                btnProcess.setEnabled(true);
            }
        }
    }//GEN-LAST:event_btnMDSProcessActionPerformed

    private void txtSchemeNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSchemeNameFocusLost
        // TODO add your handling code here:
        System.out.println("herererereererere");
        if (txtSchemeName.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAME", txtSchemeName.getText());
            List lst = ClientUtil.executeQuery("getSelectSchemeName", whereMap);
            if (lst != null && lst.size() > 0) {
                viewType = "SCHEME_NAME";
                whereMap = (HashMap) lst.get(0);
                List chitLst = ClientUtil.executeQuery("getSelectEachSchemeDetailsList", whereMap);
                if (chitLst != null && chitLst.size() > 0) {
                    whereMap = (HashMap) chitLst.get(0);
                    fillData(whereMap);
//                    txtInstallmentNo.setEnabled(false);
                    chitLst = null;
                    lst = null;
                    whereMap = null;
                }
            } else {
                ClientUtil.displayAlert("Invalid Scheme Name !!! ");
                txtSchemeName.setText("");
            }
        }
    }//GEN-LAST:event_txtSchemeNameFocusLost

    private void btnSchemeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchemeNameActionPerformed
        // TODO add your handling code here:
        callView("SCHEME_NAME");
//        tdtDrawOrAuctionDt.setEnabled(true);
//        txtInstallmentNo.setEnabled(false);
        //        txtDivisionNo.setEnabled(true);
        txtSchemeName.setEnabled(false);
    }//GEN-LAST:event_btnSchemeNameActionPerformed

    private void btnRenewTransDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenewTransDisplayActionPerformed
        // TODO add your handling code here:
        if (tdtRenewTransDate.getDateValue().length() == 0) {
            ClientUtil.showAlertWindow("Please select transaction date");
            return;
        } else {
            btnCalculateActionPerformed();
        }
    }//GEN-LAST:event_btnRenewTransDisplayActionPerformed

    private void tdtRenewTransDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtRenewTransDateFocusLost
        // TODO add your handling code here:
        if (tdtRenewTransDate.getDateValue().length() > 0) {
            observable.setTdtRenewTransDate(tdtRenewTransDate.getDateValue());
        }
    }//GEN-LAST:event_tdtRenewTransDateFocusLost

    private void tblRenewTransTableDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRenewTransTableDataMouseClicked
        // TODO add your handling code here:
        if (selectMode == true) {
            String st = CommonUtil.convertObjToStr(tblRenewTransTableData.getValueAt(tblRenewTransTableData.getSelectedRow(), 0));
            if (st.equals("true")) {
                tblRenewTransTableData.setValueAt(new Boolean(false), tblRenewTransTableData.getSelectedRow(), 0);
            } else {
                tblRenewTransTableData.setValueAt(new Boolean(true), tblRenewTransTableData.getSelectedRow(), 0);
            }
        }
        if (evt.getClickCount() == 2) {
            if (returnMap != null) {
                if (returnMap.containsKey(tblRenewTransTableData.getValueAt(
                        tblRenewTransTableData.getSelectedRow(), 1))) {
                    TTException exception = (TTException) returnMap.get(tblRenewTransTableData.getValueAt(
                            tblRenewTransTableData.getSelectedRow(), 1));
                    parseException.logException(exception, true);
                }
            }
        }
    }//GEN-LAST:event_tblRenewTransTableDataMouseClicked

    private void chkRenewTransSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRenewTransSelectAllActionPerformed
        // TODO add your handling code here:
        boolean flag;
        if (chkRenewTransSelectAll.isSelected() == true) {
            flag = true;
        } else {
            flag = false;
        }
        for (int i = 0; i < tblRenewTransTableData.getRowCount(); i++) {
            if (tblRenewTransTableData.getValueAt(i, 4).equals("Error")) {
            } else {
                tblRenewTransTableData.setValueAt(new Boolean(flag), i, 0);
            }
        }
    }//GEN-LAST:event_chkRenewTransSelectAllActionPerformed

    private void btnRenewTransCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenewTransCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnRenewTransCloseActionPerformed

    private void btnRenewTransClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenewTransClearActionPerformed
        // TODO add your handling code here:
        observable.resetDepRenewTableValues();
        ClientUtil.enableDisable(panDepositMultipleRenewal, false);
        ClientUtil.clearAll(this);
        btnRenewTransDisplay.setEnabled(true);
        btnRenewTransProcess.setEnabled(false);
        chkRenewTransSelectAll.setEnabled(false);
        chkRenewTransSingle.setEnabled(false);
        chkRenewTransInterestApp.setEnabled(false);
        tdtRenewTransDate.setEnabled(true);
        tdtRenewTransDate.setDateValue(CommonUtil.convertObjToStr(currDate.clone()));
        observable.setTdtRenewTransDate(tdtRenewTransDate.getDateValue());
    }//GEN-LAST:event_btnRenewTransClearActionPerformed

    private void btnRenewTransProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenewTransProcessActionPerformed
        // TODO add your handling code here:
        List finalList = new ArrayList();
        List interestList = null;
        btnRenewTransDisplay.setEnabled(false);
        chkRenewTransSelectAll.setEnabled(false);
        chkRenewTransSingle.setEnabled(false);
        chkRenewTransInterestApp.setEnabled(false);
        btnRenewTransProcess.setEnabled(false);
        if (observable.getFinalList() != null && observable.getFinalList().size() > 0) {
            System.out.println("#$@$#@$@$@ observable FinalList : " + observable.getFinalList());
            for (int i = 0; i < observable.getFinalList().size(); i++) {
                String custId = "";
                interestList = (ArrayList) observable.getFinalList().get(i);
                custId = CommonUtil.convertObjToStr(interestList.get(1));
                System.out.println("custId : " + custId);
                HashMap tempMap = new HashMap();
                for (int j = 0; j < tblRenewTransTableData.getRowCount(); j++) {
                    if (CommonUtil.convertObjToStr(tblRenewTransTableData.getValueAt(j, 1)).equals(custId) && ((Boolean) tblRenewTransTableData.getValueAt(j, 0)).booleanValue()) {
                        tempMap = new HashMap();
                        tempMap.put("CUST_ID", tblRenewTransTableData.getValueAt(j, 1));
                        tempMap.put("TOTAL_COUNT", tblRenewTransTableData.getValueAt(j, 4));
                        finalList.add(tempMap);
                    }
                }
            }
            System.out.println("#$#$$# final List:" + finalList);
            if (finalList != null && finalList.size() > 0) {
                selectMode = false;
                observable.doAction(finalList, "DEPOSIT_MULTIPLE_RENEWAL");
                if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                    if (observable.getProxyReturnMap() == null || observable.getProxyReturnMap().size() == 0) {
                        ClientUtil.showMessageWindow(" Transaction Completed !!! ");
                        btnClearActionPerformed(null);
                    } else {
                        returnMap = observable.getProxyReturnMap();
                        List errorList = (ArrayList) returnMap.get("INTEREST_DATA");
                        List transList = new ArrayList();
                        if (errorList != null && errorList.size() > 0) {
                            for (int j = 0; j < errorList.size(); j++) {
                                if (errorList.get(j) instanceof HashMap) {
                                    returnMap = (HashMap) errorList.get(j);
                                } else {
                                    transList.add(errorList.get(j));
                                }
                            }
                        }
                        System.out.println("#$#$$# returnMap:" + returnMap);
                        System.out.println("#$#$$# transList:" + transList);
                        ArrayList head = observable.getTableTitle();
                        ArrayList title = new ArrayList();
                        title.addAll(head);
                        title.add("Status");
                        title.remove(0);
                        ArrayList rowList = null;
                        EnhancedTableModel tbm = observable.getTblRenewTransTableData();
                        ArrayList data = tbm.getDataArrayList();
                        for (int i = 0; i < tblRenewTransTableData.getRowCount(); i++) {
                            rowList = (ArrayList) data.get(i);
                            if (returnMap.containsKey(tblRenewTransTableData.getValueAt(i, 2))) {
                                rowList.add("Error");
                            } else {
                                if (((Boolean) tblRenewTransTableData.getValueAt(i, 0)).booleanValue()) {
                                    rowList.add("Completed");
                                } else {
                                    rowList.add("Not Processed");
                                }
                            }
                            rowList.remove(0);
                        }
                        tbm.setDataArrayList(data, title);
                        tblRenewTransTableData.setModel(observable.getTblRenewTransTableData());
                        tblRenewTransTableData.getColumnModel().getColumn(0).setPreferredWidth(80);
                        tblRenewTransTableData.getColumnModel().getColumn(1).setPreferredWidth(295);
                        tblRenewTransTableData.getColumnModel().getColumn(2).setPreferredWidth(70);
                        tblRenewTransTableData.getColumnModel().getColumn(3).setPreferredWidth(75);
                        tblRenewTransTableData.getColumnModel().getColumn(4).setPreferredWidth(70);
                    }
                }
            } else {
                ClientUtil.showMessageWindow(" NO Rows Selected !!! ");
                btnProcess.setEnabled(true);
            }
        }
    }//GEN-LAST:event_btnRenewTransProcessActionPerformed

    private void chkRenewTransSingleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRenewTransSingleActionPerformed
        if (chkRenewTransSingle.isSelected() == true) {
            observable.setChkRenewTransSingle(true);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_chkRenewTransSingleActionPerformed

    private void chkRenewTransInterestAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRenewTransInterestAppActionPerformed
        if (chkRenewTransInterestApp.isSelected() == true) {
            observable.setChkRenewTransInterestApp(true);
        }         // TODO add your handling code here:
    }//GEN-LAST:event_chkRenewTransInterestAppActionPerformed

    private void btnCalculateActionPerformed() {
        displayInterestDetails();
    }

    private void displayInterestDetails() {
        selectMode = true;
        HashMap dataMap = new HashMap();
        dataMap.put("DO_TRANSACTION", new Boolean(false));
        dataMap.put("CHARGES_PROCESS", "CHARGES_PROCESS");
        dataMap.put(CommonConstants.PRODUCT_TYPE, "TD");
        dataMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        if (panDepositInterestApplication.isShowing() == true) {
            btnProcess.setEnabled(true);
            chkSelectAll.setEnabled(true);
            chkRenewTransInterestApp.setEnabled(true);
            tblDepositInterestApplication.setEnabled(true);
            dataMap.put("DEPOSIT_INTEREST_SCREEN", "DEPOSIT_INTEREST_SCREEN");
            System.out.println("displayInterestDetails dataMap : " + dataMap);
            observable.insertTableData(dataMap);
            tblDepositInterestApplication.setModel(observable.getTblDepositInterestApplication());
            javax.swing.table.TableColumn col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(0));
            col.setMaxWidth(100);
            col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(1));
            col.setMaxWidth(150);
            col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(2));
            col.setMaxWidth(120);
            col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(3));
            col.setMaxWidth(120);
            col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(4));
            col.setMaxWidth(120);
            if (tblDepositInterestApplication.getRowCount() == 0) {
                ClientUtil.showMessageWindow(" No Data !!! ");
                btnProcess.setEnabled(false);
            }
        } else if (panMDSPrizedMoneyDetails.isShowing() == true) {
            btnMDSProcess.setEnabled(true);
            chkMDSSelectAll.setEnabled(true);
            tblMDSPrizedMoneyDetails.setEnabled(true);
            dataMap.put("MDS_PRIZED_MONEY_DETAILS_SCREEN", "MDS_PRIZED_MONEY_DETAILS_SCREEN");
            observable.insertTableData(dataMap);
            tblMDSPrizedMoneyDetails.setModel(observable.getTblMDSPrizedMoneyDetails());
            javax.swing.table.TableColumn col = tblMDSPrizedMoneyDetails.getColumn(tblMDSPrizedMoneyDetails.getColumnName(0));
            col.setMaxWidth(100);
            col = tblMDSPrizedMoneyDetails.getColumn(tblMDSPrizedMoneyDetails.getColumnName(1));
            col.setMaxWidth(150);
            col = tblMDSPrizedMoneyDetails.getColumn(tblMDSPrizedMoneyDetails.getColumnName(2));
            col.setMaxWidth(120);
            col = tblMDSPrizedMoneyDetails.getColumn(tblMDSPrizedMoneyDetails.getColumnName(3));
            col.setMaxWidth(120);
            col = tblMDSPrizedMoneyDetails.getColumn(tblMDSPrizedMoneyDetails.getColumnName(4));
            col.setMaxWidth(120);
            if (tblMDSPrizedMoneyDetails.getRowCount() == 0) {
                ClientUtil.showMessageWindow(" No Data !!! ");
                btnProcess.setEnabled(false);
            }
        } else if (panDepositMultipleRenewal.isShowing() == true) {
            btnRenewTransProcess.setEnabled(true);
            chkRenewTransSelectAll.setEnabled(true);
            chkRenewTransSingle.setEnabled(true);
            tblRenewTransTableData.setEnabled(true);
            dataMap.put("DEPOSIT_MULTIPLE_RENEWAL", "DEPOSIT_MULTIPLE_RENEWAL");

            observable.insertTableData(dataMap);
            tblRenewTransTableData.setModel(observable.getTblRenewTransTableData());
            tblRenewTransTableData.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblRenewTransTableData.getColumnModel().getColumn(1).setPreferredWidth(80);
            tblRenewTransTableData.getColumnModel().getColumn(2).setPreferredWidth(300);
            tblRenewTransTableData.getColumnModel().getColumn(3).setPreferredWidth(70);
            tblRenewTransTableData.getColumnModel().getColumn(4).setPreferredWidth(70);
            if (tblRenewTransTableData.getRowCount() == 0) {
                ClientUtil.showMessageWindow(" No Data !!! ");
                btnRenewTransDisplay.setEnabled(false);
            }
        }
//        tblMDSPrizedMoneyDetails.setModel(observable.getTblDepositInterestApplication());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCalculate;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnMDSCalculate;
    private com.see.truetransact.uicomponent.CButton btnMDSClear;
    private com.see.truetransact.uicomponent.CButton btnMDSClose;
    private com.see.truetransact.uicomponent.CButton btnMDSProcess;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnRenewTransClear;
    private com.see.truetransact.uicomponent.CButton btnRenewTransClose;
    private com.see.truetransact.uicomponent.CButton btnRenewTransDisplay;
    private com.see.truetransact.uicomponent.CButton btnRenewTransProcess;
    private com.see.truetransact.uicomponent.CButton btnSchemeName;
    private com.see.truetransact.uicomponent.CCheckBox chkMDSSelectAll;
    private com.see.truetransact.uicomponent.CCheckBox chkRenewTransInterestApp;
    private com.see.truetransact.uicomponent.CCheckBox chkRenewTransSelectAll;
    private com.see.truetransact.uicomponent.CCheckBox chkRenewTransSingle;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblMDSSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblMDSTransDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblRenewTransDate;
    private com.see.truetransact.uicomponent.CLabel lblRenewTransInterestApp;
    private com.see.truetransact.uicomponent.CLabel lblRenewTransSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblRenewTransSingle;
    private com.see.truetransact.uicomponent.CLabel lblSchemeName;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTransDate;
    private com.see.truetransact.uicomponent.CPanel panDepositInterestApplication;
    private com.see.truetransact.uicomponent.CPanel panDepositMultipleRenewal;
    private com.see.truetransact.uicomponent.CPanel panMDSCustomerId;
    private com.see.truetransact.uicomponent.CPanel panMDSPrizedMoneyDetails;
    private com.see.truetransact.uicomponent.CPanel panMDSProcess;
    private com.see.truetransact.uicomponent.CPanel panMDSProductTableData;
    private com.see.truetransact.uicomponent.CPanel panMDSSelectAll;
    private com.see.truetransact.uicomponent.CPanel panPrizedMoneyDetails;
    private com.see.truetransact.uicomponent.CPanel panProcess;
    private com.see.truetransact.uicomponent.CPanel panProductDetails;
    private com.see.truetransact.uicomponent.CPanel panProductTableData;
    private com.see.truetransact.uicomponent.CPanel panRenewTrans;
    private com.see.truetransact.uicomponent.CPanel panRenewTransInterestApp;
    private com.see.truetransact.uicomponent.CPanel panRenewTransProcess;
    private com.see.truetransact.uicomponent.CPanel panRenewTransSelectAll;
    private com.see.truetransact.uicomponent.CPanel panRenewTransSingleSelect;
    private com.see.truetransact.uicomponent.CPanel panRenewTransTableData;
    private com.see.truetransact.uicomponent.CPanel panSelectAll;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CScrollPane srpDepositInterestApplication;
    private com.see.truetransact.uicomponent.CScrollPane srpMDSPrizedMoneyDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpRenewTransTableData;
    private com.see.truetransact.uicomponent.CTabbedPane tabChequeBook;
    private com.see.truetransact.uicomponent.CTable tblDepositInterestApplication;
    private com.see.truetransact.uicomponent.CTable tblMDSPrizedMoneyDetails;
    private com.see.truetransact.uicomponent.CTable tblRenewTransTableData;
    private com.see.truetransact.uicomponent.CDateField tdtMDSTransDate;
    private com.see.truetransact.uicomponent.CDateField tdtRenewTransDate;
    private com.see.truetransact.uicomponent.CDateField tdtTransDate;
    private com.see.truetransact.uicomponent.CTextField txtSchemeName;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        SendSMSUI fad = new SendSMSUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(fad);
        j.show();
        fad.show();
    }
}
