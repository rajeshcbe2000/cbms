/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MDSStandingInstructionUI.java
 *
 * Created on October 10th, 2011, 11:03 PM
 */
package com.see.truetransact.ui.mdsapplication;

/**
 *
 * @author  
 */
import java.util.*;
import com.see.truetransact.commonutil.DateUtil;
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
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.viewall.TextUI;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CTextField;
import java.awt.Color;
import java.awt.Component;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

public class MDSStandingInstructionUI extends CInternalFrame implements Observer {

    /** Vairable Declarations */
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    MDSStandingInstructionOB observable = null;
    private boolean selectMode = false;
    private List finalList = null;
    private Date currDate = null;
    ArrayList colourList = new ArrayList();
    public int selectedRow = -1;

    /** Creates new form TokenConfigUI */
    public MDSStandingInstructionUI() {
        initForm();
        currDate = ClientUtil.getCurrentDate();
    }

    /** Method which is used to initialize the form TokenConfig */
    private void initForm() {
        initComponents();
        observable = new MDSStandingInstructionOB();
        initTableData();
        txtSchemeName.setAllowAll(true);
        btnCalculate.setEnabled(false);
        btnProcess.setEnabled(false);
        btnClose.setEnabled(false);
        btnClear.setEnabled(false);
        ClientUtil.enableDisable(panStandingInstruction, false);
        btnSchemeName.setEnabled(true);
        btnCalculate.setEnabled(false);
        txtSchemeName.setEnabled(true);
        panReprint.setVisible(false);
        btnRePrint.setVisible(true);
        setSizeTableData();
        cboProdId.setEnabled(true);
        txtInstNo.setEnabled(true);
        try{
            observable.fillProductId();
            cboProdId.setModel(observable.getCbmProdId());    
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void initTableData() {         
        tblStandingInstruction.setModel(observable.getTblStandingInstruction());
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

    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths() {
    }

    /** Method used to Give a Alert when any Mandatory Field is not filled by the user */
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
        if (currField.equalsIgnoreCase("SCHEME_DETAILS")) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectEachSchemeDetails");
        } else if (currField.equalsIgnoreCase("Delete")) {
        }
        new ViewAll(this, viewMap).show();
    }

    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object map) {
        try {
            HashMap hash = (HashMap) map;
            if (viewType != null) {
                if (viewType.equalsIgnoreCase("SCHEME_DETAILS")) {
                    txtSchemeName.setText(CommonUtil.convertObjToStr(hash.get("SCHEME_NAME")));
                    btnSchemeName.setEnabled(true);
                    // Added by nithya on 15-12-2017 for 0008300: Mds_standing_instruction Screen -Installment number selection needed.
                    HashMap schemeMap = new HashMap();
                    schemeMap.put("SCHEME_NAME",hash.get("SCHEME_NAME"));
                    schemeMap.put("CURR_DATE",currDate.clone());
                    schemeMap.put("ADD_MONTHS", CommonUtil.convertObjToInt("-1"));   
                    List freqLst = ClientUtil.executeQuery("getMDSSchemeInstallMentFreq", schemeMap);
                    if(freqLst != null && freqLst.size() > 0){
                        HashMap freqMap = (HashMap)freqLst.get(0);
                        if(freqMap.containsKey("INSTALLMENT_FREQUENCY") && freqMap.get("INSTALLMENT_FREQUENCY") != null){
                            List insDateLst = null;
                            if (CommonUtil.convertObjToInt(freqMap.get("INSTALLMENT_FREQUENCY")) == 7) {
                                insDateLst = ClientUtil.executeQuery("getWeeklyMDSCurrentInsDate", schemeMap);
                            }else{
                                insDateLst = ClientUtil.executeQuery("getMDSCurrentInsDate", schemeMap);
                            }
                            HashMap insDateMap = new HashMap();
                            int curInsNo = 0;
                             if(insDateLst!=null && insDateLst.size()>0){
                                insDateMap = (HashMap)insDateLst.get(0);
                                curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));   
                                txtInstNo.setText(String.valueOf(curInsNo));
                             }
                        }
                    }
                    // End 0008300: Mds_standing_instruction Screen -Installment number selection needed.
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panStandingInstruction = new com.see.truetransact.uicomponent.CPanel();
        panSchemeNameDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSchemeName = new com.see.truetransact.uicomponent.CLabel();
        panSchemeName = new com.see.truetransact.uicomponent.CPanel();
        txtSchemeName = new com.see.truetransact.uicomponent.CTextField();
        btnSchemeName = new com.see.truetransact.uicomponent.CButton();
        btnCalculate = new com.see.truetransact.uicomponent.CButton();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblProduct = new com.see.truetransact.uicomponent.CLabel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtInstNo = new com.see.truetransact.uicomponent.CTextField();
        panSchemeNameTableData = new com.see.truetransact.uicomponent.CPanel();
        srpStandingIns = new com.see.truetransact.uicomponent.CScrollPane();
        tblStandingInstruction = new com.see.truetransact.uicomponent.CTable();
        panSelectAll = new com.see.truetransact.uicomponent.CPanel();
        lblSelectAll = new com.see.truetransact.uicomponent.CLabel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        panProcess = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        btnRePrint = new com.see.truetransact.uicomponent.CButton();
        lblTotalTransactionAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotalTransactionAmt = new com.see.truetransact.uicomponent.CLabel();
        panReprint = new com.see.truetransact.uicomponent.CPanel();
        panReprintBtn = new com.see.truetransact.uicomponent.CPanel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        btnReprintClose = new com.see.truetransact.uicomponent.CButton();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        panReprintDetails = new com.see.truetransact.uicomponent.CPanel();
        lblFromTransID = new com.see.truetransact.uicomponent.CLabel();
        lblToTransID = new com.see.truetransact.uicomponent.CLabel();
        lblTransDate = new com.see.truetransact.uicomponent.CLabel();
        tdtTransDate = new com.see.truetransact.uicomponent.CDateField();
        txtFromTransID = new com.see.truetransact.uicomponent.CTextField();
        txtToTransID = new com.see.truetransact.uicomponent.CTextField();
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
        setMaximumSize(new java.awt.Dimension(860, 650));
        setMinimumSize(new java.awt.Dimension(860, 650));
        setPreferredSize(new java.awt.Dimension(860, 650));

        panStandingInstruction.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panStandingInstruction.setMaximumSize(new java.awt.Dimension(800, 450));
        panStandingInstruction.setMinimumSize(new java.awt.Dimension(800, 450));
        panStandingInstruction.setPreferredSize(new java.awt.Dimension(800, 450));
        panStandingInstruction.setLayout(new java.awt.GridBagLayout());

        panSchemeNameDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Scheme Name"));
        panSchemeNameDetails.setMinimumSize(new java.awt.Dimension(850, 50));
        panSchemeNameDetails.setPreferredSize(new java.awt.Dimension(850, 50));

        lblSchemeName.setText("MDS Scheme Name");

        panSchemeName.setLayout(new java.awt.GridBagLayout());

        txtSchemeName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSchemeName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSchemeNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panSchemeName.add(txtSchemeName, gridBagConstraints);

        btnSchemeName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSchemeName.setEnabled(false);
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSchemeName.add(btnSchemeName, gridBagConstraints);

        btnCalculate.setText("Display");
        btnCalculate.setMaximumSize(new java.awt.Dimension(89, 21));
        btnCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateActionPerformed(evt);
            }
        });

        cboProdId.setPopupWidth(320);
        cboProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdActionPerformed(evt);
            }
        });

        lblProduct.setText("Standing Product");

        cLabel1.setText("No Of Inst");

        txtInstNo.setAllowNumber(true);
        txtInstNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInstNoFocusLost(evt);
            }
        });

        javax.swing.GroupLayout panSchemeNameDetailsLayout = new javax.swing.GroupLayout(panSchemeNameDetails);
        panSchemeNameDetails.setLayout(panSchemeNameDetailsLayout);
        panSchemeNameDetailsLayout.setHorizontalGroup(
            panSchemeNameDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSchemeNameDetailsLayout.createSequentialGroup()
                .addComponent(lblSchemeName, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panSchemeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtInstNo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboProdId, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCalculate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panSchemeNameDetailsLayout.setVerticalGroup(
            panSchemeNameDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSchemeNameDetailsLayout.createSequentialGroup()
                .addGroup(panSchemeNameDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panSchemeNameDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtInstNo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboProdId, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCalculate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panSchemeNameDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(panSchemeName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblSchemeName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 0, 0);
        panStandingInstruction.add(panSchemeNameDetails, gridBagConstraints);

        panSchemeNameTableData.setBorder(javax.swing.BorderFactory.createTitledBorder("MDS Scheme Details"));
        panSchemeNameTableData.setMinimumSize(new java.awt.Dimension(850, 330));
        panSchemeNameTableData.setPreferredSize(new java.awt.Dimension(850, 330));
        panSchemeNameTableData.setLayout(new java.awt.GridBagLayout());

        srpStandingIns.setMinimumSize(new java.awt.Dimension(830, 300));
        srpStandingIns.setPreferredSize(new java.awt.Dimension(830, 300));

        tblStandingInstruction.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Chittal No", "Sub No", "Name", "InstDue", "Ins Amount", "Bonus", "Discount", "MDS Interest", "Prod Type", "Prod Id", "Dr Act No", "Avai. Balance", "Net Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblStandingInstruction.setMaximumSize(new java.awt.Dimension(2147483647, 64));
        tblStandingInstruction.setMinimumSize(new java.awt.Dimension(60, 64));
        tblStandingInstruction.setPreferredScrollableViewportSize(new java.awt.Dimension(804, 296));
        tblStandingInstruction.setPreferredSize(new java.awt.Dimension(775, 3000));
        tblStandingInstruction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblStandingInstructionMouseClicked(evt);
            }
        });
        srpStandingIns.setViewportView(tblStandingInstruction);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        panSchemeNameTableData.add(srpStandingIns, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panStandingInstruction.add(panSchemeNameTableData, gridBagConstraints);

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
        gridBagConstraints.ipadx = -2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 2, 0, 0);
        panStandingInstruction.add(panSelectAll, gridBagConstraints);

        panProcess.setMinimumSize(new java.awt.Dimension(850, 60));
        panProcess.setPreferredSize(new java.awt.Dimension(850, 60));
        panProcess.setLayout(new java.awt.GridBagLayout());

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(29, 4, 4, 4);
        panProcess.add(btnClose, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(29, 4, 4, 20);
        panProcess.add(btnClear, gridBagConstraints);

        btnProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnProcess.setText("PROCESS");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(29, 110, 4, 4);
        panProcess.add(btnProcess, gridBagConstraints);

        btnRePrint.setForeground(new java.awt.Color(255, 0, 51));
        btnRePrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnRePrint.setText("Reprint");
        btnRePrint.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnRePrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRePrintActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(29, 4, 4, 4);
        panProcess.add(btnRePrint, gridBagConstraints);

        lblTotalTransactionAmtVal.setForeground(new java.awt.Color(0, 0, 255));
        lblTotalTransactionAmtVal.setFont(new java.awt.Font("MS Sans Serif", 1, 12)); // NOI18N
        lblTotalTransactionAmtVal.setMinimumSize(new java.awt.Dimension(100, 21));
        lblTotalTransactionAmtVal.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panProcess.add(lblTotalTransactionAmtVal, gridBagConstraints);

        lblTotalTransactionAmt.setForeground(new java.awt.Color(0, 0, 255));
        lblTotalTransactionAmt.setText("Total Net Amout  :    Rs.");
        lblTotalTransactionAmt.setFont(new java.awt.Font("MS Sans Serif", 1, 12)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 50, 4, 4);
        panProcess.add(lblTotalTransactionAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panStandingInstruction.add(panProcess, gridBagConstraints);

        panReprint.setMinimumSize(new java.awt.Dimension(830, 250));
        panReprint.setPreferredSize(new java.awt.Dimension(830, 250));
        panReprint.setLayout(new java.awt.GridBagLayout());

        panReprintBtn.setMinimumSize(new java.awt.Dimension(515, 35));
        panReprintBtn.setPreferredSize(new java.awt.Dimension(515, 35));
        panReprintBtn.setLayout(new java.awt.GridBagLayout());

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setText("CANCEL");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintBtn.add(btnCancel, gridBagConstraints);

        btnReprintClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnReprintClose.setText("Close");
        btnReprintClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReprintCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintBtn.add(btnReprintClose, gridBagConstraints);

        btnPrint.setForeground(new java.awt.Color(255, 0, 51));
        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setText("Print");
        btnPrint.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintBtn.add(btnPrint, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(30, 0, 0, 0);
        panReprint.add(panReprintBtn, gridBagConstraints);

        panReprintDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Reprint Details"));
        panReprintDetails.setMinimumSize(new java.awt.Dimension(415, 145));
        panReprintDetails.setPreferredSize(new java.awt.Dimension(415, 145));
        panReprintDetails.setLayout(new java.awt.GridBagLayout());

        lblFromTransID.setText("From Trans ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintDetails.add(lblFromTransID, gridBagConstraints);

        lblToTransID.setText("To Trans ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintDetails.add(lblToTransID, gridBagConstraints);

        lblTransDate.setText("Trans Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintDetails.add(lblTransDate, gridBagConstraints);

        tdtTransDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtTransDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintDetails.add(tdtTransDate, gridBagConstraints);

        txtFromTransID.setAllowAll(true);
        txtFromTransID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 1);
        panReprintDetails.add(txtFromTransID, gridBagConstraints);

        txtToTransID.setAllowAll(true);
        txtToTransID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 1);
        panReprintDetails.add(txtToTransID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panReprint.add(panReprintDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 12, 9, 0);
        panStandingInstruction.add(panReprint, gridBagConstraints);

        getContentPane().add(panStandingInstruction, java.awt.BorderLayout.CENTER);

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

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        //Added By Suresh
        if (txtFromTransID.getText().length() > 0 && txtToTransID.getText().length() > 0 && tdtTransDate.getDateValue().length() > 0) {
            HashMap paramMap = new HashMap();
            TTIntegration ttIntgration = null;
            paramMap.put("FromTransId", txtFromTransID.getText());
            paramMap.put("ToTransId", txtToTransID.getText());
            paramMap.put("TransDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtTransDate.getDateValue())));
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            System.out.println("####### paramMap" + paramMap);
            ttIntgration.setParam(paramMap);
            ttIntgration.integrationForPrint("MDS_SI_TransferVoucher",true);
            ClientUtil.enableDisable(panReprintDetails, false);
            btnPrint.setEnabled(false);
            ClientUtil.clearAll(this);
        } else {
            ClientUtil.showMessageWindow("Please Enter all Details of Reprint !!!");
        }
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnRePrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRePrintActionPerformed
        // TODO add your handling code here:
        //Added By Suresh
        btnPrint.setEnabled(true);
        panReprint.setVisible(true);
        panProcess.setVisible(false);
        panSelectAll.setVisible(false);
        panSchemeNameDetails.setVisible(false);
        panSchemeNameTableData.setVisible(false);
        ClientUtil.enableDisable(panReprintDetails, true);
        tdtTransDate.setDateValue(DateUtil.getStringDate((Date) currDate.clone()));
    }//GEN-LAST:event_btnRePrintActionPerformed
    private Date getProperDate(Date sourceDate) {
        Date targetDate = (Date) currDate.clone();
        targetDate.setDate(sourceDate.getDate());
        targetDate.setMonth(sourceDate.getMonth());
        targetDate.setYear(sourceDate.getYear());
        return targetDate;
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this);
        btnRePrint.setEnabled(true);
        panReprint.setVisible(false);
        panProcess.setVisible(true);
        panSelectAll.setVisible(true);
        panSchemeNameDetails.setVisible(true);
        panSchemeNameTableData.setVisible(true);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnReprintCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReprintCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnReprintCloseActionPerformed

    private void txtSchemeNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSchemeNameFocusLost
        // TODO add your handling code here:
        if (txtSchemeName.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAME", txtSchemeName.getText());
            List lst = ClientUtil.executeQuery("getSelectSchemeName", whereMap);
            if (lst != null && lst.size() > 0) {
                observable.setTxtSchemeName(txtSchemeName.getText());
                btnCalculate.setEnabled(true);
                btnProcess.setEnabled(false);
                btnClose.setEnabled(true);
                btnClear.setEnabled(true);
            } else {
                ClientUtil.displayAlert("Invalid Scheme Name !!! ");
                txtSchemeName.setText("");
                observable.setTxtSchemeName("");
                btnCalculate.setEnabled(false);
            }
        }
    }//GEN-LAST:event_txtSchemeNameFocusLost

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        setSizeTableData();
        ClientUtil.enableDisable(panStandingInstruction, false);
        ClientUtil.clearAll(this);
        btnSchemeName.setEnabled(true);
        txtSchemeName.setEnabled(true);
        btnCalculate.setEnabled(false);
        btnProcess.setEnabled(false);
        chkSelectAll.setEnabled(false);
        lblTotalTransactionAmtVal.setText(" ");
        cboProdId.setEnabled(true);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        observable.setScreen(this.getScreen());
        btnCalculate.setEnabled(false);
        chkSelectAll.setEnabled(false);
        finalList = observable.getFinalList();
        btnProcess.setEnabled(false);
        HashMap chitMap = new HashMap();
        StringBuffer interbranchList = new StringBuffer();
        // Added by nithya on 25-08-2016
        List finalListWithAuction = new ArrayList();
        List finalListWithOutAuction = new ArrayList();
        if (finalList != null && finalList.size() > 0) {
            System.out.println("#$@$#@$@$@ FinalList : " + finalList);
            for (int i = 0; i < finalList.size(); i++) {
                String chitNo = "";
                String subNo = "";
                chitMap = (HashMap) finalList.get(i);
                chitNo = CommonUtil.convertObjToStr(chitMap.get("CHITTAL_NO"));
                subNo = CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(chitMap.get("SUB_NO")));
                for (int j = 0; j < tblStandingInstruction.getRowCount(); j++) {
                    if (CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(j, 1)).equals(chitNo) && CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(j, 2)).equals(subNo)
                            && !((Boolean) tblStandingInstruction.getValueAt(j, 0)).booleanValue()) {
                        finalList.remove(i--);
                    }
                    if (CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(j, 1)).equals(chitNo) && CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(j, 2)).equals(subNo)
                    && ((Boolean) tblStandingInstruction.getValueAt(j, 0)).booleanValue()) {
                        HashMap existingMap = new HashMap();
                        existingMap.put("ACT_NUM", CommonUtil.convertObjToStr(chitMap.get("DR_ACT_NO")));
                        List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", existingMap);
                        if (mapDataList != null && mapDataList.size() > 0) {
                            existingMap = (HashMap) mapDataList.get(0);
                            if(existingMap != null && !ProxyParameters.BRANCH_ID.equals(CommonUtil.convertObjToStr(existingMap.get("BRANCH_ID")))){
                                Date selectedBranchDt = ClientUtil.getOtherBranchCurrentDate(CommonUtil.convertObjToStr(existingMap.get("BRANCH_ID")));
                                Date currentDate = (Date) currDate.clone();
                                System.out.println("selectedBranchDt : "+selectedBranchDt + " currentDate : "+currentDate);
                                if(selectedBranchDt == null){
                                    if(interbranchList.length() == 0){
                                        interbranchList.append("BOD is not completed for the selected branch " +"\n"+"Interbranch Transaction Not allowed"+ "\n"+"Chittal No : "+chitNo);
                                    }else{
                                        interbranchList.append("Chittal No : "+chitNo);
                                    }
                                }else if(DateUtil.dateDiff(currentDate, selectedBranchDt)!=0){ 
                                    if(interbranchList.length() == 0){
                                        interbranchList.append("Application Date is different in the Selected branch " +"\n"+"Interbranch Transaction Not allowed"+ "\n"+"Chittal No : "+chitNo);
                                    }else{
                                        interbranchList.append( "\n"+ "Chittal No : "+chitNo);
                                    }
                                }
                            }          
                        }   
                    }
                }
            }
            if(interbranchList != null && interbranchList.length()>0){
                ClientUtil.displayAlert("Please deselect the following Chittal no, "+ "\n"+interbranchList);
                btnProcess.setEnabled(true);
                return;
            }
            System.out.println("#$#$$# final List:" + finalList);
            // Added by nithya on 25-08-2016            
            for(int i=0; i<finalList.size();i++){
                HashMap finalMap = (HashMap)finalList.get(i);
                HashMap checkPriorityMap = new HashMap();
                checkPriorityMap.put("SCHEME_NAME",txtSchemeName.getText());
                checkPriorityMap.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
                checkPriorityMap.put("CHITTAL_NO",finalMap.get("CHITTAL_NO"));                
                List standingPriortyList = ClientUtil.executeQuery("getChittalPrizedDetailsForStanding", checkPriorityMap);
                if(standingPriortyList != null && standingPriortyList.size() > 0){
                    finalListWithAuction.add(finalList.get(i));
                }else{
                    finalListWithOutAuction.add(finalList.get(i));
                }                            
            }
            for(int i=0; i<finalListWithOutAuction.size();i++){
                finalListWithAuction.add(finalListWithOutAuction.get(i));
            }
            List finalPriorityList = (List)finalListWithAuction;
            observable.setPriorityList(finalPriorityList);
            System.out.println("finalPriorityList :: " + finalPriorityList);
            // End            
            if (finalList != null && finalList.size() > 0) {
                selectMode = false;
                //added by rishad 05/08/2015 
                CommonUtil comm = new CommonUtil();
                final JDialog loading = comm.addProgressBar();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws InterruptedException /** Execute some operation */
                    {
                        try {
                            //observable.doAction(finalList);
                            observable.doAction(observable.getPriorityList());// Added by nithya on 25-08-2016
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                    @Override
                    protected void done() {
                        loading.dispose();
                    }
                };
                worker.execute();
                loading.show();
                try {
                    worker.get();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                    if (observable.getProxyReturnMap() == null || observable.getProxyReturnMap().size() == 0) {
                        ClientUtil.showMessageWindow(" Transaction Completed !!! ");
                        btnClearActionPerformed(null);
                    } else {
                        HashMap returnMap = observable.getProxyReturnMap();
                        System.out.println("returnMap####"+returnMap);
                        EnhancedTableModel tbm = observable.getTblStandingInstruction();
                        ArrayList head = observable.getTableTitle();
                        ArrayList title = new ArrayList();
                        title.addAll(head);
                        title.add("Status");
                        ArrayList data = tbm.getDataArrayList();
                        ArrayList rowList = null;
                        for (int i = 0; i < tblStandingInstruction.getRowCount(); i++) {
                            rowList = (ArrayList) data.get(i);
                            if (returnMap.containsKey(tblStandingInstruction.getValueAt(i, 1))) {
                                rowList.add("Error");
                            } else {
                                if (((Boolean) tblStandingInstruction.getValueAt(i, 0)).booleanValue()) {
                                    rowList.add("Completed");
                                } else {
                                    rowList.add("Not Processed");
                                }
                            }
                        }
                        tbm.setDataArrayList(data, title);
                        //Disply Trans Details
                        //String transLists="";
                        List transList = new ArrayList();
                        List transListTran = new ArrayList();
                        if (returnMap.containsKey("TRANSACTION_DETAILS")) {
                            transList = (List) returnMap.get("TRANSACTION_DETAILS");
                            transListTran = (List) returnMap.get("TRANSACTION_DETAILS_SI");
                            System.out.println("transList#################"+transList);
                            System.out.println("transListTran#################"+transListTran);
                           //Added By Suresh
                            if (transListTran.size() > 0) {
                                String fromTransId = "";
                                fromTransId = CommonUtil.convertObjToStr(transList.get(0));
                                String toTransId = CommonUtil.convertObjToStr(transList.get(transList.size() - 1));
                                ClientUtil.showMessageWindow("Please note the Transactions ID" + "\n" + "From Trans ID :   " + fromTransId + "\n" + "To Trans ID :   " + toTransId);
                            }
                            int yesNo = 0;
                            String[] options = {"Yes", "No"};
                            yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                    null, options, options[0]);
                            System.out.println("#$#$$ yesNo : " + yesNo);
                            if (yesNo == 0) {
                                TTIntegration ttIntgration = null;
                                HashMap paramMap = null;
                                String fromSingleTransId = CommonUtil.convertObjToStr(transListTran.get(0));
                                //String toSingleTransId = CommonUtil.convertObjToStr(transList.get(transList.size() - 1));
                                paramMap = new HashMap();
                                //paramMap.put("FromTransId", fromTransId);
                                paramMap.put("TransId", fromSingleTransId);
                                paramMap.put("TransDt", currDate);
                                paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                                System.out.println("#####paramMap" + paramMap);
                                ttIntgration.setParam(paramMap);
                                ttIntgration.integrationForPrint("MDSReceiptsTransfer");
                            }
                        }
                        btnClearActionPerformed(null);
                    }
                }
            } else {
                ClientUtil.showMessageWindow(" NO Rows Selected !!! ");
                btnProcess.setEnabled(true);
            }
        }
    }//GEN-LAST:event_btnProcessActionPerformed

//    public class CustomTableCellRenderer extends DefaultTableCellRenderer 
//    {
//        public Component getTableCellRendererComponent
//           (JTable table, Object value, boolean isSelected,
//           boolean hasFocus, int row, int column) {
//            Component cell = super.getTableCellRendererComponent
//               (table, value, isSelected, hasFocus, row, column);
//            if( value instanceof Boolean ) {
//                HashMap returnMap = observable.getProxyReturnMap();
//                for (int i=0; i<tblStandingInstruction.getRowCount(); i++) {
//                     if (returnMap.containsKey(tblStandingInstruction.getValueAt(i, 1))) {
//                            cell.setBackground( Color.red );
//                     }
//                }
//
////                Integer amount = (Integer) value;
////                if( amount.intValue() < 0 )
////                {
////                    cell.setBackground( Color.red );
////                    // You can also customize the Font and Foreground this way
////                    // cell.setForeground();
////                    // cell.setFont();
////                }
////                else
////                {
////                    cell.setBackground( Color.white );
////                }
//            }
//            return cell;
//        }
//    }
    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
        boolean flag;
        if (chkSelectAll.isSelected() == true) {
            flag = true;
        } else {
            flag = false;
        }
        for (int i = 0; i < tblStandingInstruction.getRowCount(); i++) {
            //added by rishad 16/03/2015 
            double availbalnce = CommonUtil.convertObjToDouble(tblStandingInstruction.getValueAt(i, 12));
            double netamt = CommonUtil.convertObjToDouble(tblStandingInstruction.getValueAt(i, 13));
            if (availbalnce < netamt && flag == true) {
                tblStandingInstruction.setValueAt(false, i, 0);
            } else {
                tblStandingInstruction.setValueAt(new Boolean(flag), i, 0);
                if(colourList.contains(String.valueOf(i))){// Added by nithya on 22-11-2017 for 8260
                   tblStandingInstruction.setValueAt(false, i, 0);
                }
            }
        }
        //Print Pendinding Authorize List
        if (observable.getPendingList() != null && observable.getPendingList().size() > 0 && chkSelectAll.isSelected() == true) {
            List pendingList = observable.getPendingList();
            System.out.println("##### pendingList : " + pendingList);
            String message = "";
            for (int i = 0; i < pendingList.size(); i++) {
                HashMap pendingMap = new HashMap();
                pendingMap = (HashMap) pendingList.get(i);
                String chitNo = "";
                String subNo = "";
                chitNo = CommonUtil.convertObjToStr(pendingMap.get("CHIT_NO"));
                subNo = CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(pendingMap.get("SUB_NO")));
                for (int j = 0; j < tblStandingInstruction.getRowCount(); j++) {
                    if (CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(j, 1)).equals(chitNo) && CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(j, 2)).equals(subNo)) {
                        message = message + CommonUtil.convertObjToStr(pendingMap.get("CHITTAL_NO")) + "\n";
                        tblStandingInstruction.setValueAt(new Boolean(false), j, 0);
                    }
                }
            }
            if (message.length() > 0) {
                ClientUtil.showMessageWindow("Transaction Pending for Following Chittals... Plz Authorize OR Reject first  !!! \n" + message);
            }
        }
        setTotalAmount();
    }//GEN-LAST:event_chkSelectAllActionPerformed
    private void setTotalAmount() {
        double totAmount = 0;
        String st = "";
        for (int i = 0; i < tblStandingInstruction.getRowCount(); i++) {
            st = CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(i, 0));
            if (st.equals("true")) {
                totAmount = totAmount + CommonUtil.convertObjToDouble((CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(i, 13))).replaceAll(",", "")).doubleValue();
            }
        }
        lblTotalTransactionAmtVal.setText(CurrencyValidation.formatCrore(String.valueOf(totAmount)));
    }
    private void tblStandingInstructionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStandingInstructionMouseClicked
        // TODO add your handling code here:
        if (selectMode == true) {
            String st = CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(tblStandingInstruction.getSelectedRow(), 0));
            if (st.equals("true")) {
                tblStandingInstruction.setValueAt(new Boolean(false), tblStandingInstruction.getSelectedRow(), 0);
            } else {
                //added by rishad //Modified by sreekrishnan
                double availBalance = CommonUtil.convertObjToDouble(tblStandingInstruction.getValueAt(tblStandingInstruction.getSelectedRow(), 12))-
                                      CommonUtil.convertObjToDouble(observable.getDrAcMinBal().get((CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(tblStandingInstruction.getSelectedRow(), 11))))); 
                double netAmt = CommonUtil.convertObjToDouble(tblStandingInstruction.getValueAt(tblStandingInstruction.getSelectedRow(), 13));
                if (availBalance < netAmt) {
                    tblStandingInstruction.setValueAt(new Boolean(false), tblStandingInstruction.getSelectedRow(), 0);
                } else {
                    tblStandingInstruction.setValueAt(new Boolean(true), tblStandingInstruction.getSelectedRow(), 0);
                    if(colourList.contains(String.valueOf(tblStandingInstruction.getSelectedRow()))){// Added by nithya on 22-11-2017 for 8260
                        tblStandingInstruction.setValueAt(new Boolean(false), tblStandingInstruction.getSelectedRow(), 0);
                    }
                }
                //Print Pendinding Authorize List
                if (observable.getPendingList() != null && observable.getPendingList().size() > 0) {
                    List pendingList = observable.getPendingList();
                    System.out.println("##### pendingList : " + pendingList);
                    String message = "";
                    for (int i = 0; i < pendingList.size(); i++) {
                        HashMap pendingMap = new HashMap();
                        pendingMap = (HashMap) pendingList.get(i);
                        String chitNo = "";
                        String subNo = "";
                        chitNo = CommonUtil.convertObjToStr(pendingMap.get("CHIT_NO"));
                        subNo = CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(pendingMap.get("SUB_NO")));
                        if (CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(tblStandingInstruction.getSelectedRow(), 1)).equals(chitNo)
                                && CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(tblStandingInstruction.getSelectedRow(), 2)).equals(subNo)) {
                            message = message + CommonUtil.convertObjToStr(pendingMap.get("CHITTAL_NO"));
                            tblStandingInstruction.setValueAt(new Boolean(false), tblStandingInstruction.getSelectedRow(), 0);
                        }
                    }
                    if (message.length() > 0) {
                        ClientUtil.showMessageWindow("Transaction Pending for this Chittal " + message + " ... Plz Authorize OR Reject first  !!!");
                    }
                }
            }
        }
        setTotalAmount();
        if (evt.getClickCount() == 2) {
            HashMap returnMap = new HashMap();
            if (observable.getProxyReturnMap() != null) {
                returnMap = observable.getProxyReturnMap();
                if (returnMap.containsKey(tblStandingInstruction.getValueAt(
                        tblStandingInstruction.getSelectedRow(), 1))) {
                    TTException exception = (TTException) returnMap.get(tblStandingInstruction.getValueAt(
                            tblStandingInstruction.getSelectedRow(), 1));
                    parseException.logException(exception, true);
                }
            }
        }
        //Added by sreekrishnan
        //String st = CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(tblStandingInstruction.getSelectedRow(), 0));
        //if (st.equals("true")) {
            if (evt.getClickCount() == 2 && tblStandingInstruction.getSelectedColumn()==8) {
                //System.out.println("chittal penal amt map :: " + observable.getChittalPenalAmtMap());
                String chittalNo = CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(tblStandingInstruction.getSelectedRow(), 1));
                double penalAmt = 0.0;
                if(observable.getChittalPenalAmtMap() != null && observable.getChittalPenalAmtMap().containsKey(chittalNo) && observable.getChittalPenalAmtMap().get(chittalNo) != null){
                    penalAmt = CommonUtil.convertObjToDouble(observable.getChittalPenalAmtMap().get(chittalNo));
                }
                HashMap amountMap = new HashMap();
                amountMap.put("TITLE", "Mds Interest Amount");
                amountMap.put("TOLERANCE_AMT", CommonConstants.TOLERANCE_AMT);
//                amountMap.put("SELECTED_AMT", tblStandingInstruction.getValueAt(tblStandingInstruction.getSelectedRow(), 8));
//                amountMap.put("CALCULATED_AMT", tblStandingInstruction.getValueAt(tblStandingInstruction.getSelectedRow(), 8));
                amountMap.put("SELECTED_AMT", penalAmt);
                amountMap.put("CALCULATED_AMT", penalAmt);
                TextUI textUI = new TextUI(this, this, amountMap);
            }
       // }
    }//GEN-LAST:event_tblStandingInstructionMouseClicked

    public void modifyTransData(Object obj) {
        try{
        TextUI objTextUI = (TextUI) obj;
        String selectedDepNo = "";
        HashMap dataMap = new HashMap();
        ArrayList rowList = new ArrayList();
        ArrayList tableList = new ArrayList();
        String enteredData = objTextUI.getTxtData();
        double intAmt = CommonUtil.convertObjToDouble(enteredData).doubleValue();
        System.out.println("intAmt@$!$!@"+intAmt);
        selectedDepNo = CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(tblStandingInstruction.getSelectedRow(), 1));
        if (observable.getFinalList() != null && observable.getFinalList().size() > 0) {
            for (int i = 0; i < observable.getFinalList().size(); i++) {
                rowList = new ArrayList();
                dataMap = (HashMap) observable.getFinalList().get(i);
                System.out.println("dataMap$^^$^$"+dataMap);
                String depNo = "";
                String siNo = "";                
                depNo = CommonUtil.convertObjToStr(dataMap.get("CHITTAL_NO"));
                if (selectedDepNo.equals(depNo)) {
                    System.out.println("%#%#%#%#%#$^$^$NET_AMOUNT"+(CommonUtil.convertObjToDouble(dataMap.get("NET_AMOUNT"))));
                    System.out.println("%#%#%#%#%#$^$^$PENAL"+(CommonUtil.convertObjToDouble(dataMap.get("PENAL"))));
                    System.out.println("%#%#%#%#%#"+(CommonUtil.convertObjToDouble(dataMap.get("NET_AMOUNT"))-CommonUtil.convertObjToDouble(dataMap.get("PENAL"))));
                    dataMap.put("NET_AMOUNT", (CommonUtil.convertObjToDouble(dataMap.get("NET_AMOUNT"))-CommonUtil.convertObjToDouble(dataMap.get("PENAL")))+CommonUtil.convertObjToDouble(enteredData));
                    System.out.println("%#%#%#%#%#$^$^$"+(CommonUtil.convertObjToDouble(dataMap.get("NET_AMOUNT"))));
                    dataMap.put("PENAL", new Double(intAmt));
                    tblStandingInstruction.setValueAt(enteredData,tblStandingInstruction.getSelectedRow(), 8);
                    tblStandingInstruction.setValueAt(CommonUtil.convertObjToDouble(dataMap.get("NET_AMOUNT")),tblStandingInstruction.getSelectedRow(), 13);
                }
            }
            TallyCheck();
            setColour();
        } 
        }catch(Exception e){
            e.printStackTrace();
        }
    }
        
    private void btnCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateActionPerformed
        // TODO add your handling code here:
        if (tblStandingInstruction.getRowCount() > 0) {
            observable.resetTableValues();
        }

        if (txtSchemeName.getText().length() > 0) {
            selectMode = true;
            observable.setTxtSchemeName(txtSchemeName.getText());
            tblStandingInstruction.setEnabled(true);
            btnSchemeName.setEnabled(false);
            btnProcess.setEnabled(true);
            chkSelectAll.setEnabled(true);
            txtSchemeName.setEnabled(false);
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAME", txtSchemeName.getText());
            whereMap.put("CURR_DATE", currDate);
            if (cboProdId.getSelectedIndex() > 0 && observable.getCbmProdId() != null) {
                whereMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmProdId().getKeyForSelected()));
            }
            if (CommonUtil.convertObjToStr(txtInstNo.getText()).length() > 0) {
                whereMap.put("CURRENT_INSALL_NO", CommonUtil.convertObjToInt(txtInstNo.getText()));// Added by nithya on 15-12-2017 for 0008300: Mds_standing_instruction Screen -Installment number selection needed.
            } else {
                whereMap.put("CURRENT_INSALL_NO", CommonUtil.convertObjToInt(0));
            }
            //observable.insertTableData(whereMap);
            observable.getTableData(whereMap);//DAO calling
            tblStandingInstruction.setModel(observable.getTblStandingInstruction());
            //tblStandingInstruction.setEditingColumn(8);
            setSizeTableData();
            TallyCheck();
            setColour();
            if (tblStandingInstruction.getRowCount() == 0) {
                ClientUtil.showMessageWindow(" No Data !!! ");
                btnProcess.setEnabled(false);
                btnCalculate.setEnabled(true);
            } else {
                btnCalculate.setEnabled(false);
            }
        } else {
            ClientUtil.showMessageWindow("Select any scheme!!! ");
            return;
        }
    }//GEN-LAST:event_btnCalculateActionPerformed
    private void TallyCheck() {
        ArrayList drAcctNo = new ArrayList();
        ArrayList drAccNo = new ArrayList();
        HashMap sameAccountMap = new HashMap();
        if (tblStandingInstruction.getRowCount() > 0) {
            for (int i = 0; i < tblStandingInstruction.getRowCount(); i++) {
            drAcctNo.add(CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(i, 11)));
            }
            Set<String> unique = new HashSet<String>(drAcctNo);
            for (String key : unique) {
               int noOfDup =  Collections.frequency(drAcctNo, key);
               if(noOfDup > 1){
                   drAccNo.add(key);
               }


            colourList = new ArrayList();
            double balAmt = 0.0;
            for (int i = 0; i < tblStandingInstruction.getRowCount(); i++) {
                double demandAmt = 0;
                double recoveredAmt = 0;
                demandAmt = CommonUtil.convertObjToDouble(tblStandingInstruction.getValueAt(i, 12).toString()).doubleValue();
                recoveredAmt = CommonUtil.convertObjToDouble(tblStandingInstruction.getValueAt(i, 13).toString()).doubleValue();
                if(observable.getDrAcMinBal().containsKey((CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(i, 11))))){
                        //System.out.println("observable.getDrAcMinBal().get(sd)"+(CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(i, 11))));
                           demandAmt = demandAmt - CommonUtil.convertObjToDouble(observable.getDrAcMinBal().get((CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(i, 11))))); 
                        }
                if (demandAmt < recoveredAmt) {
                    colourList.add(String.valueOf(i));
                }
            }
                //System.out.println("color%#$%@#%@%"+colourList);
            }
            Set<String> uniqueDepNo = new HashSet<String>(drAccNo);
            double amts = 0.0;
            double availbal = 0.0;
            //System.out.println("uniqueDepNo^$^$^#"+uniqueDepNo);
            for (String sd : uniqueDepNo) {
                for (int i = 0; i < tblStandingInstruction.getRowCount(); i++) {
                    if (sd.equals(CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(i, 11)))) {
                        //System.out.println("getmin bal%%@%@5"+observable.getDrAcMinBal());                        
                        availbal = CommonUtil.convertObjToDouble(tblStandingInstruction.getValueAt(i, 12));                        
                        amts += CommonUtil.convertObjToDouble(tblStandingInstruction.getValueAt(i, 13));
                        //System.out.println("i-----" + i);
                    }                    
                    if(observable.getDrAcMinBal().containsKey((CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(i, 11))))){
                        //System.out.println("observable.getDrAcMinBal().get(sd)"+(CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(i, 11))));
                           availbal = availbal - CommonUtil.convertObjToDouble(observable.getDrAcMinBal().get((CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(i, 11))))); 
                        }
                    if (availbal < amts) {
                        if (sd.equals(CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(i, 11)))) {
                            colourList.add(String.valueOf(i));
                        }
                    }
                }
                amts = 0.0;
                availbal = 0.0;
            }
            }
    }

    private void setColour() {
        /* Set a cellrenderer to this table in order format the date */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (colourList.contains(String.valueOf(row))) {
                    setForeground(Color.RED);
                } else {
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblStandingInstruction.setDefaultRenderer(Object.class, renderer);
    }

    private void setSizeTableData() {
        tblStandingInstruction.getColumnModel().getColumn(0).setPreferredWidth(45);
        tblStandingInstruction.getColumnModel().getColumn(1).setPreferredWidth(108);
        tblStandingInstruction.getColumnModel().getColumn(2).setPreferredWidth(55);
        tblStandingInstruction.getColumnModel().getColumn(3).setPreferredWidth(130);
        tblStandingInstruction.getColumnModel().getColumn(4).setPreferredWidth(55);
        tblStandingInstruction.getColumnModel().getColumn(5).setPreferredWidth(55);
        tblStandingInstruction.getColumnModel().getColumn(6).setPreferredWidth(55);
        tblStandingInstruction.getColumnModel().getColumn(7).setPreferredWidth(65);
        tblStandingInstruction.getColumnModel().getColumn(8).setPreferredWidth(65);
        tblStandingInstruction.getColumnModel().getColumn(9).setPreferredWidth(75);
        tblStandingInstruction.getColumnModel().getColumn(10).setPreferredWidth(50);
        tblStandingInstruction.getColumnModel().getColumn(11).setPreferredWidth(115);
        tblStandingInstruction.getColumnModel().getColumn(12).setPreferredWidth(95);
        tblStandingInstruction.getColumnModel().getColumn(13).setPreferredWidth(85);
    }
    private void btnSchemeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchemeNameActionPerformed
        // TODO add your handling code here:
        callView("SCHEME_DETAILS");
        if (txtSchemeName.getText().length() > 0) {
            btnCalculate.setEnabled(true);
            btnProcess.setEnabled(false);
            btnClose.setEnabled(true);
            btnClear.setEnabled(true);
        }
    }//GEN-LAST:event_btnSchemeNameActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitNewActionPerformed

private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
// TODO add your handling code here:
    btnCalculate.setEnabled(true);
}//GEN-LAST:event_cboProdIdActionPerformed

    private void txtInstNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInstNoFocusLost
        // TODO add your handling code here:  
        btnCalculate.setEnabled(true); // Added by nithya on 15-12-2017 for 0008300: Mds_standing_instruction Screen -Installment number selection needed.
    }//GEN-LAST:event_txtInstNoFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCalculate;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnRePrint;
    private com.see.truetransact.uicomponent.CButton btnReprintClose;
    private com.see.truetransact.uicomponent.CButton btnSchemeName;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblFromTransID;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProduct;
    private com.see.truetransact.uicomponent.CLabel lblSchemeName;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToTransID;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblTransDate;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panProcess;
    private com.see.truetransact.uicomponent.CPanel panReprint;
    private com.see.truetransact.uicomponent.CPanel panReprintBtn;
    private com.see.truetransact.uicomponent.CPanel panReprintDetails;
    private com.see.truetransact.uicomponent.CPanel panSchemeName;
    private com.see.truetransact.uicomponent.CPanel panSchemeNameDetails;
    private com.see.truetransact.uicomponent.CPanel panSchemeNameTableData;
    private com.see.truetransact.uicomponent.CPanel panSelectAll;
    private com.see.truetransact.uicomponent.CPanel panStandingInstruction;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpStandingIns;
    private com.see.truetransact.uicomponent.CTable tblStandingInstruction;
    private com.see.truetransact.uicomponent.CDateField tdtTransDate;
    private com.see.truetransact.uicomponent.CTextField txtFromTransID;
    private com.see.truetransact.uicomponent.CTextField txtInstNo;
    private com.see.truetransact.uicomponent.CTextField txtSchemeName;
    private com.see.truetransact.uicomponent.CTextField txtToTransID;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        MDSStandingInstructionUI fad = new MDSStandingInstructionUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(fad);
        j.show();
        fad.show();
    }
}
