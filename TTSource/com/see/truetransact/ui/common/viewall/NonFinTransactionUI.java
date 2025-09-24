/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * NonFinTransactionUI.java
 *
 * Created on October 16, 2009, 1:46 PM
 */

package com.see.truetransact.ui.common.viewall;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;

import org.apache.log4j.Logger;

import com.see.truetransact.clientutil.EnhancedComboBoxModel;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CPanel;
import com.see.truetransact.uicomponent.CDialog;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CComboBox;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.Date;
/**
 * @author  Swaroop
 */
public class NonFinTransactionUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {
    private final ViewAllRB resourceBundle = new ViewAllRB();
    private NonFinTransactionOB observable;
    HashMap paramMap = null;
    int amtColumnNo=0;
    double tot = 0;
    String amtColName = "";
    String behavesLike = "";
    boolean collDet=false;
    private Date currDt = null;
    private final static Logger log = Logger.getLogger(ViewAllTransactions.class);
    
    
    /** Creates new form ViewAll */
    public NonFinTransactionUI() {
        setupInit();
        setupScreen();
    }

    private void setupInit() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        internationalize();
        setObservable();
        toFront();
        setCombos();
        btnClear.setVisible(true);
        observable.resetForm();
        update(observable, null);
        tdtFromDate.setDateValue(DateUtil.getStringDate(currDt));
        tdtToDate.setDateValue(DateUtil.getStringDate(currDt));
        observable.setTdtFromDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
        observable.setTdtToDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
        cboBranCode.setSelectedItem(TrueTransactMain.BRANCH_ID);
    }
    
    private void setupScreen() {
//        setModal(true);
        
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
    
    private void setObservable() {
        try {
            observable = NonFinTransactionOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void setCombos() {
        cboProdType.setModel(observable.getCbmProdType());
        cboBranCode.setModel(observable.getCbmBranCode());
    }
    
    
    public void populateData() {
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (btnIntDetPressed) {
            viewMap.put(CommonConstants.MAP_NAME, "getAllIntTransactions"+observable.getProdType());
            if(observable.getLinkMap()!=null && observable.getLinkMap().containsKey("AS_CUSTOMER_COMES") && 
                observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y")) {
                if(observable.getProdType().equals("TL"))
                    viewMap.put(CommonConstants.MAP_NAME,"getAllIntTransactionsAsAnWhenTL");
                else
                    viewMap.put(CommonConstants.MAP_NAME,"getAllIntTransactionsAsAnWhenAD");
            }
        } else if(observable.btnRDTransPressed == true)
            viewMap.put(CommonConstants.MAP_NAME,"getAllRDTransactionsTD");            
        else
            viewMap.put(CommonConstants.MAP_NAME, "getAllTransactions"+observable.getProdType());
        whereMap.put("ACT_NUM", observable.getTxtAccNo());
        whereMap.put("FROM_DT", observable.getTdtFromDate());
        whereMap.put("TO_DT", observable.getTdtToDate());
        if (collDet)
            whereMap.put("BEHAVES_LIKE", "DAILY");
        else
            whereMap.put("BEHAVES_LIKE", null);
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(viewMap, tblData);
            heading = null;
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
        viewMap = null;
        whereMap = null;
    }
    
    public void show() {
        /* The following if condition commented by Rajesh
         * Because observable is not making null after closing the UI
         * So, if no data found in previously opened EnquiryUI instance
         * the observable.isAvailable() is false, so EnquiryUI won't open.
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
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        rdgAndOr = new com.see.truetransact.uicomponent.CButtonGroup();
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblAccNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        panFind = new com.see.truetransact.uicomponent.CPanel();
        btnView = new com.see.truetransact.uicomponent.CButton();
        cboBranCode = new com.see.truetransact.uicomponent.CComboBox();
        lblBran = new com.see.truetransact.uicomponent.CLabel();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(700, 280));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(700, 280));
        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 6, 4, 6);
        panSearchCondition.add(lblProdType, gridBagConstraints);

        cboProdType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(18, 6, 4, 6);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSearchCondition.add(cboProdType, gridBagConstraints);

        lblAccNameValue.setFont(new java.awt.Font("MS Sans Serif", 0, 11));
        lblAccNameValue.setMaximumSize(new java.awt.Dimension(175, 21));
        lblAccNameValue.setMinimumSize(new java.awt.Dimension(175, 21));
        lblAccNameValue.setPreferredSize(new java.awt.Dimension(175, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        panSearchCondition.add(lblAccNameValue, gridBagConstraints);

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSearchCondition.add(lblFromDate, gridBagConstraints);

        tdtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSearchCondition.add(tdtFromDate, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSearchCondition.add(lblToDate, gridBagConstraints);

        tdtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSearchCondition.add(tdtToDate, gridBagConstraints);

        panFind.setLayout(new java.awt.GridBagLayout());

        panFind.setBorder(new javax.swing.border.TitledBorder("Press to view"));
        panFind.setMinimumSize(new java.awt.Dimension(260, 125));
        panFind.setPreferredSize(new java.awt.Dimension(260, 130));
        btnView.setText("View");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });

        panFind.add(btnView, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 6, 3);
        panSearchCondition.add(panFind, gridBagConstraints);

        cboBranCode.setMaximumSize(new java.awt.Dimension(100, 21));
        cboBranCode.setMinimumSize(new java.awt.Dimension(100, 21));
        cboBranCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBranCodeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSearchCondition.add(cboBranCode, gridBagConstraints);

        lblBran.setText("Branch");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSearchCondition.add(lblBran, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
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
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDataMousePressed(evt);
            }
        });
        tblData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblDataMouseMoved(evt);
            }
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tblDataMouseDragged(evt);
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

        panSearch.setLayout(new java.awt.GridBagLayout());

        panSearch.setMinimumSize(new java.awt.Dimension(750, 40));
        panSearch.setPreferredSize(new java.awt.Dimension(750, 40));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif")));
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClose, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif")));
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panSearch, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void cboBranCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBranCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboBranCodeActionPerformed


    boolean btnIntDetPressed = false;
    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        System.out.println("#$#$ Hash : "+hash);
    }
    
    private void tdtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateFocusLost
        // TODO add your handling code here:
        observable.setTdtToDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
        java.util.Date from = observable.getTdtFromDate();
        java.util.Date to = observable.getTdtToDate();
        if (from!=null && to!=null && DateUtil.dateDiff(from,to)<0) {
            displayAlert("To date should be greater than From Date...");
            tdtToDate.setDateValue("");
            tdtToDate.requestFocus();
        }
    }//GEN-LAST:event_tdtToDateFocusLost

    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost
        // TODO add your handling code here:
        observable.setTdtFromDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
//        tdtToDate.setDateValue("");
    }//GEN-LAST:event_tdtFromDateFocusLost

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        
        System.out.println("#$#$ prodType : "+((ComboBoxModel)cboProdType.getModel()).getKeyForSelected());
        observable.setProdType(CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected()));       
            
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
            
    
    private void tblDataMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseDragged
        
    }//GEN-LAST:event_tblDataMouseDragged

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.refreshTable();
        HashMap prodTypeMap = new HashMap();
        String prod_type=((String)((ComboBoxModel)cboProdType.getModel()).getKeyForSelected());
        String branCode=(String)cboBranCode.getModel().getSelectedItem();
        System.out.println("prod_type"+prod_type);
        System.out.println("branCode"+branCode);
        if(branCode!=null || !branCode.equals(""))
           prodTypeMap.put("BCODE",branCode); 
        if(prod_type.equals(""))
            prodTypeMap.put("PROD_TYPE_ALL"," ");
        else if(prod_type.equals("OA"))
              prodTypeMap.put("PROD_TYPE","OPERATIVE_ACCOUNT");
        else if(prod_type.equals("TD"))
              prodTypeMap.put("PROD_TYPE","DEPOSITS");
         else if(prod_type.equals("TL"))
              prodTypeMap.put("PROD_TYPE","TERM_LOANS");
        else if(prod_type.equals("Customers"))
              prodTypeMap.put("PROD_TYPE","CUSTOMER");
        else if(prod_type.equals("AD"))
              prodTypeMap.put("PROD_TYPE","ADVANCES");
        else if(prod_type.equals("AAD"))
              prodTypeMap.put("PROD_TYPE","AGRI_ADVANCES");
        else if(prod_type.equals("ATL"))
              prodTypeMap.put("PROD_TYPE","AGRI_TERM_LOANS");
        else if(prod_type.equals("RM"))
              prodTypeMap.put("PROD_TYPE","REMITTANCE");
        else if(prod_type.equals("SM"))
              prodTypeMap.put("PROD_TYPE","SUPPORTING_MODULE");
         else if(prod_type.equals("SHR"))
              prodTypeMap.put("PROD_TYPE","SHARES");
        else if(prod_type.equals("BILLS"))
              prodTypeMap.put("PROD_TYPE","BILLS");
        
             HashMap whereMap = new HashMap();
              whereMap.put("FROM_DT",observable.getTdtFromDate());
              whereMap.put("TO_DT",observable.getTdtToDate());
             if(!prod_type.equals("")){
             whereMap.put("PROD_TYPE",prodTypeMap.get("PROD_TYPE"));
             }
              if(branCode!=null || !branCode.equals(""))
                  whereMap.put("BCODE",prodTypeMap.get("BCODE"));
             prodTypeMap.put(CommonConstants.MAP_WHERE, whereMap);
             prodTypeMap.put(CommonConstants.MAP_NAME, "getNonFinTransDetails");
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(prodTypeMap, tblData);
            heading = null;
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_btnViewActionPerformed

    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
     
    }//GEN-LAST:event_tblDataMousePressed

    private void tblDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseMoved
  
    }//GEN-LAST:event_tblDataMouseMoved

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        btnClearActionPerformed(null);
        cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        observable.resetForm();
//        update(observable, null);
        tdtFromDate.setDateValue(DateUtil.getStringDate(currDt));
        tdtToDate.setDateValue(DateUtil.getStringDate(currDt));
        observable.setTdtFromDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
        observable.setTdtToDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
    }//GEN-LAST:event_btnClearActionPerformed
    
        private ComboBoxModel getListModel() {
        ComboBoxModel listData = new ComboBoxModel();
        return listData;
    }                            
        
        private void internationalize() {
        }
        
        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
            HashMap mapParam = new HashMap();

            HashMap where = new HashMap();
            where.put("beh", "CA");

            mapParam.put("MAPNAME", "getSelectInwardClearingAuthorizeTOList");
            //mapParam.put("WHERE", where);
            // HashMap rMap = (HashMap) dao.getData(mapParam);

            // HashMap testMap = new HashMap();
            //testMap.put("MAPNAME", "getSelectOperativeAcctProductTOList");
            new ViewAll(mapParam).show();
        }

        public void update(Observable observed, Object arg) {
            ((ComboBoxModel)cboProdType.getModel()).setKeyForSelected(observable.getProdType());
//            tdtFromDate.setDateValue(DateUtil.getStringDate(observable.getTdtFromDate()));
//            tdtToDate.setDateValue(DateUtil.getStringDate(observable.getTdtToDate()));
        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboBranCode;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CLabel lblAccNameValue;
    private com.see.truetransact.uicomponent.CLabel lblBran;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CPanel panFind;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAndOr;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    // End of variables declaration//GEN-END:variables
}

