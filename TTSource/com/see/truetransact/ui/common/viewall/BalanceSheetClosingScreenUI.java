/*
 * Pass Book.java
 * Swaroop 
 * Created on December 07, 2009, 1:46 PM
 */
package com.see.truetransact.ui.common.viewall;

import com.ibatis.db.sqlmap.SqlMap;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.apache.log4j.Logger;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.LinkedHashMap;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * @author Swaroop
 */
public class BalanceSheetClosingScreenUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {

    private BalanceSheetClosingScreenOB observable;
    HashMap passMap = new HashMap();
    HashMap paramMap = null;
    int amtColumnNo = 0;
    double tot = 0;
    int act=0;
    String amtColName = "";
    String behavesLike = "";
    private LinkedHashMap Selected = new LinkedHashMap();
    private LinkedHashMap NotSelected = new LinkedHashMap();
    public static final boolean isFrameClosed = false;
    private final static Logger log = Logger.getLogger(BalanceSheetClosingScreenUI.class);
    private static SqlMap sqlMap = null; 
    private boolean btnCancel=false;
    HashMap actMap = null;
    double changeValue = 0.0;
    private TableModelListener tableModelListener; 
    
    private String liableBal ;
     private String assetBal ;
    
    
    
    /**
     * Creates new form PassBook
     */
    public BalanceSheetClosingScreenUI() {

        setupInit();
        setupScreen();
    }

    public BalanceSheetClosingScreenUI(String print) {
        setObservable();
        initComponents();
//        Date tdt=   (Date)  txtClosingYear.getText();
//        txtClosingYear.setValidation(new ToDateValidation());
        //txtLineNo.setValidation(new NumericValidation());

    }

    private void setupInit() {
        initComponents(); 
        setObservable();
        toFront(); 
        fillClosingYear(); 
        panSearchCondition.setVisible(true);
        btnClear.setVisible(true);
        observable.resetForm();
        update(observable, null); 
        btnSave.setEnabled(false);
        setModified(true);
    }

    private void setupScreen() {
//        setModal(true);

        /*
         * Calculate the screen size
         */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        /*
         * Center frame on the screen
         */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    private void setObservable() {
        try {
            observable = BalanceSheetClosingScreenOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillClosingYear() { 
        HashMap mapShare = new HashMap();
        List keyValue = null;
        keyValue = ClientUtil.executeQuery("getClosingYear", mapShare);
        mapShare = (HashMap) keyValue.get(0); 
        tdtClosingYeara.setDateValue(CommonUtil.convertObjToStr(mapShare.get("CLOSING_YEAR")));
        keyValue.clear();
        keyValue = null;
        mapShare.clear();
        mapShare = null;
    }

  

    public void populateData() { 
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        HashMap passbk = new HashMap(); 
        viewMap.put(CommonConstants.MAP_NAME, "getBalanceSheetClosingScreenDetails" );
       // whereMap.put("FROM_SLNO", CommonUtil.convertObjToInt(txtLineNo.getText()));
        whereMap.put("CLSYR", tdtClosingYeara.getDateValue()); 
        whereMap.put("USRID",TrueTransactMain.USER_ID);
        if(tdtClosingYeara.getDateValue().equals("")){
            ClientUtil.displayAlert("Closing Year should not be empty!!!");
            return;
        }
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(viewMap, tblData);
            heading = null;
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
//        observable.getGrandTotalAmount(whereMap);
//        double assetBal=CommonUtil.convertObjToDouble(observable.getAssetBal());
//        double liableBal=CommonUtil.convertObjToDouble(observable.getLiableBal());
//        
       
        viewMap = null;
        whereMap = null;
    }

    public void show() {
        /*
         * The following if condition commented by Rajesh Because observable is
         * not making null after closing the UI So, if no data found in
         * previously opened EnquiryUI instance the observable.isAvailable() is
         * false, so EnquiryUI won't open.
         */
//        if (observable.isAvailable()) {
        super.show();
//        }
    }

    /**
     * Bring up and populate the temporary project detail screen.
     */
    private void whenTableRowSelected() {
        this.dispose();

    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgAndOr = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPassBookType = new com.see.truetransact.uicomponent.CButtonGroup();
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        lblClosingYear = new com.see.truetransact.uicomponent.CLabel();
        tdtClosingYeara = new com.see.truetransact.uicomponent.CDateField();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        panEmpty = new java.awt.Panel();
        txtDifference = new com.see.truetransact.uicomponent.CTextField();
        txtAssets = new com.see.truetransact.uicomponent.CTextField();
        txtLiability = new com.see.truetransact.uicomponent.CTextField();
        lblLiability = new com.see.truetransact.uicomponent.CLabel();
        lblAssetTotal = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblDifference = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(750, 630));
        setPreferredSize(new java.awt.Dimension(750, 630));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(700, 100));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(700, 100));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        lblClosingYear.setText("Closing Year");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(lblClosingYear, gridBagConstraints);

        tdtClosingYeara.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tdtClosingYearaMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tdtClosingYearaMousePressed(evt);
            }
        });
        tdtClosingYeara.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtClosingYearaFocusLost(evt);
            }
        });
        tdtClosingYeara.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tdtClosingYearaKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 4, 6);
        panSearchCondition.add(tdtClosingYeara, gridBagConstraints);

        btnProcess.setText("Process");
        btnProcess.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnProcess.setMaximumSize(new java.awt.Dimension(145, 27));
        btnProcess.setMinimumSize(new java.awt.Dimension(145, 27));
        btnProcess.setPreferredSize(new java.awt.Dimension(145, 27));
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 70);
        panSearchCondition.add(btnProcess, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(panSearchCondition, gridBagConstraints);

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(sptLine, gridBagConstraints);

        panTable.setLayout(new java.awt.GridBagLayout());

        srcTable.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        srcTable.setPreferredSize(new java.awt.Dimension(452, 380));

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblData.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 13)); // NOI18N
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDataMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblDataMouseReleased(evt);
            }
        });
        tblData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tblDataMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblDataMouseMoved(evt);
            }
        });
        tblData.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblDataFocusLost(evt);
            }
        });
        tblData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblDataKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblDataKeyReleased(evt);
            }
        });
        srcTable.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(srcTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panTable, gridBagConstraints);

        panSearch.setMinimumSize(new java.awt.Dimension(750, 100));
        panSearch.setPreferredSize(new java.awt.Dimension(750, 100));
        panSearch.setLayout(new java.awt.GridBagLayout());

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 50);
        panSearch.add(btnClear, gridBagConstraints);

        panEmpty.setMinimumSize(new java.awt.Dimension(150, 50));
        panEmpty.setPreferredSize(new java.awt.Dimension(150, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 100, 0, 0);
        panSearch.add(panEmpty, gridBagConstraints);

        txtDifference.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDifference.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDifferenceActionPerformed(evt);
            }
        });
        txtDifference.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDifferenceFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSearch.add(txtDifference, gridBagConstraints);

        txtAssets.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAssets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAssetsActionPerformed(evt);
            }
        });
        txtAssets.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAssetsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSearch.add(txtAssets, gridBagConstraints);

        txtLiability.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLiability.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLiabilityActionPerformed(evt);
            }
        });
        txtLiability.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLiabilityFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        panSearch.add(txtLiability, gridBagConstraints);

        lblLiability.setText("Liability Total");
        lblLiability.setMaximumSize(new java.awt.Dimension(100, 20));
        lblLiability.setMinimumSize(new java.awt.Dimension(100, 20));
        lblLiability.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panSearch.add(lblLiability, gridBagConstraints);

        lblAssetTotal.setText("Asset Total");
        lblAssetTotal.setMaximumSize(new java.awt.Dimension(100, 20));
        lblAssetTotal.setMinimumSize(new java.awt.Dimension(100, 20));
        lblAssetTotal.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panSearch.add(lblAssetTotal, gridBagConstraints);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 50);
        panSearch.add(btnSave, gridBagConstraints);

        lblDifference.setText("Difference");
        lblDifference.setMaximumSize(new java.awt.Dimension(100, 20));
        lblDifference.setMinimumSize(new java.awt.Dimension(100, 20));
        lblDifference.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panSearch.add(lblDifference, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panSearch, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseReleased
        // TODO add your handling code here:
//        Object ob= new Boolean(true);
//        tblData.setValueAt(ob, tblData.getSelectedRow(), 0);
    }//GEN-LAST:event_tblDataMouseReleased

    private void tblDataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDataKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDataKeyPressed

    private void tblDataKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDataKeyReleased
    }//GEN-LAST:event_tblDataKeyReleased

    private void tblDataFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblDataFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDataFocusLost
    boolean btnIntDetPressed = false;

    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        System.out.println("#$#$ Hash : " + hash);  
        observable.refreshTable(); 
    
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    private void tblDataMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseDragged
        // TODO add your handling code here:          
    }//GEN-LAST:event_tblDataMouseDragged

    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
        // Add your handling code here:
       // int rowcnt = tblData.getRowCount();
//        int row = tblData.getSelectedRow();
//        //double changeValue = 0.0;
//       // Object ob = new Boolean(false);
//        //for (int i = 0; i < rowcnt; i++) {
//        tblData.setEditingColumn(2);
//        tblData.setEditingRow(row);
//        tblData.setCellEditor(null);
//        //tblData.setCellSelectionEnabled(true);
//        tblData.isCellEditable(row, 2);
//         
//            //tblData.setValueAt(changeValue, row, 2);
//            
//           // System.out.println("row count "+rowcnt);
//            System.out.println("row slct "+row);
           // System.out.println("row changeValue "+changeValue);
        //}
//        ob = new Boolean(true);
//        for (int i = row; i < rowcnt; i++) {
//            tblData.setValueAt(changeValue, i, 2);
//            tblData.isC
        //}
        
        //tblData.setValueAt(ob, tblData.getSelectedRow(), 0);

    }//GEN-LAST:event_tblDataMousePressed

    private void tblDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseMoved
        // Add your handling code here:
    }//GEN-LAST:event_tblDataMouseMoved

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        observable.resetForm();
        btnCancel=true;
        update(observable, null);
        //cboMjrHead.set;
        behavesLike = "";
        tdtClosingYeara.setDateValue(null); 
        setModified(false);
        txtAssets.setText("");
        txtLiability.setText("");
        txtDifference.setText("");
        act=0;
    }//GEN-LAST:event_btnClearActionPerformed

    private void txtDifferenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDifferenceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDifferenceActionPerformed

    private void txtDifferenceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDifferenceFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDifferenceFocusLost

    private void txtAssetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAssetsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAssetsActionPerformed

    private void txtAssetsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAssetsFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAssetsFocusLost

    private void txtLiabilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLiabilityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLiabilityActionPerformed

    private void txtLiabilityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLiabilityFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLiabilityFocusLost

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        HashMap mapShare = new HashMap();
        mapShare.put("CLSYR",tdtClosingYeara.getDateValue());
        mapShare.put("USRID",TrueTransactMain.USER_ID);
        ClientUtil.executeQuery("btnProcessQuery", mapShare);
        populateData();
         
        HashMap assetMap = new HashMap();
        List balanceList = null;
        balanceList = ClientUtil.executeQuery("getAssetLiableBalance", mapShare);
        assetMap = (HashMap) balanceList.get(0);  
        assetBal=(CommonUtil.convertObjToStr(assetMap.get("ASSET_AMOUNT")));
        liableBal=(CommonUtil.convertObjToStr(assetMap.get("LIB_AMOUNT"))); 
        assetMap.clear();
        assetMap = null;   
        
    
    
        
         int diff=((CommonUtil.convertObjToInt(liableBal))-(CommonUtil.convertObjToInt(assetBal)));
//        System.out.println("diff  >>>>"+diff);
//        System.out.println("assetBal  >>>>"+assetBal);
//        System.out.println("liableBal  >>>>"+liableBal);
        txtAssets.setText(CommonUtil.convertObjToStr(assetBal));
        txtLiability .setText(CommonUtil.convertObjToStr(liableBal));
        txtDifference.setText(CommonUtil.convertObjToStr(diff));
        if(diff==0){
            btnSave.setEnabled(true);
        }else{
             btnSave.setEnabled(false);
        }
        mapShare.clear();
        mapShare = null;
    }//GEN-LAST:event_btnProcessActionPerformed

    private void tdtClosingYearaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtClosingYearaFocusLost
        // TODO add your handling code here: 
        observable.setTdtFromDate(DateUtil.getDateMMDDYYYY(tdtClosingYeara.getDateValue()));
         
    }//GEN-LAST:event_tdtClosingYearaFocusLost

    private void tdtClosingYearaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdtClosingYearaMouseClicked
//System.out.println("clicked value : ");        // TODO add your handling code here:
    }//GEN-LAST:event_tdtClosingYearaMouseClicked

    private void tdtClosingYearaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdtClosingYearaMousePressed
//System.out.println("pressed value : ");        // TODO add your handling code here:
    }//GEN-LAST:event_tdtClosingYearaMousePressed

    private void tdtClosingYearaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tdtClosingYearaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtClosingYearaKeyPressed

    
    private void setTableModelListener() {
       // flag = false;
        try {
            tableModelListener = new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                   // if (e.getType() == TableModelEvent.UPDATE  ) {
                        System.out.println("Cell " + e.getFirstRow() + ", " + e.getColumn() + " changed. The new value: " + 
                                tblData.getModel().getValueAt(e.getFirstRow(), e.getColumn()));
                        int row = e.getFirstRow();
                        int column = e.getColumn();
                        int selectedRow = tblData.getSelectedRow();
                        boolean chk = ((Boolean) tblData.getValueAt(tblData.getSelectedRow(), 0)).booleanValue();
                        String scheme = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 1));

                }
            };
            tblData.getModel().addTableModelListener(tableModelListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseClicked
        // TODO add your handling code here:
//        int rowSelect=tblData.getSelectedRow();
//        changeValue=CommonUtil.convertObjToDouble(tblData.getValueAt(rowSelect, 2));
//        tblData.setValueAt(changeValue, rowSelect, 2);
       
    }//GEN-LAST:event_tblDataMouseClicked

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
       try{
        String message = "";   
        HashMap mapSave = new HashMap();
        mapSave.put("CLSYR",tdtClosingYeara.getDateValue());
        mapSave.put("USRID",TrueTransactMain.USER_ID);
        //ClientUtil.execute("btnSaveForBalSheetClosing", mapSave);
        List processList = ClientUtil.executeQuery("btnSaveForBalSheetClosing", mapSave);
        if(processList != null && processList.size() > 0){
            HashMap messageMap = (HashMap)processList.get(0);
            if(messageMap.containsKey("MESSAGE") && messageMap.get("MESSAGE") != null){
                message = CommonUtil.convertObjToStr(messageMap.get("MESSAGE"));
            }
        }
        if(message.length() > 0){
          ClientUtil.showMessageWindow(message);
        }
        mapSave.clear();
        mapSave = null;
       }catch(Exception e){
           e.printStackTrace();
           ClientUtil.displayAlert("Data Not Saved");
      }
            
    }//GEN-LAST:event_btnSaveActionPerformed



    private void internationalize() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) { 
        new BalanceSheetClosingScreenUI().show();
    }

    public void update(Observable observed, Object arg) {
        //((ComboBoxModel) cboBranch.getModel()).setKeyForSelected(observable.getProdType());
        
        //System.out.println("0006" + observable.getTxtAccNo());
        //txtClosingYear.setText(observable.getTxtAccNo());
        //tdtFromDate.setDateValue(DateUtil.getStringDate(observable.getTdtFromDate()));
        //tdtToDate.setDateValue(DateUtil.getStringDate(observable.getTdtToDate()));
    }

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CLabel lblAssetTotal;
    private com.see.truetransact.uicomponent.CLabel lblClosingYear;
    private com.see.truetransact.uicomponent.CLabel lblDifference;
    private com.see.truetransact.uicomponent.CLabel lblLiability;
    private java.awt.Panel panEmpty;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAndOr;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPassBookType;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CDateField tdtClosingYeara;
    private com.see.truetransact.uicomponent.CTextField txtAssets;
    private com.see.truetransact.uicomponent.CTextField txtDifference;
    private com.see.truetransact.uicomponent.CTextField txtLiability;
    // End of variables declaration//GEN-END:variables
}
