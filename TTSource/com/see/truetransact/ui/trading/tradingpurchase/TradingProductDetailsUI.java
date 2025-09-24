/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TradingProductDetailsUI.java
 *
 * Created on April 13, 2011, 10:10 PM
 */
package com.see.truetransact.ui.trading.tradingpurchase;

import com.ibatis.db.sqlmap.SqlMap;
import java.util.Observer;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Toolkit;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.servicelocator.ServiceLocator;

/**
 *
 * @author Revathi L
 */
public class TradingProductDetailsUI extends com.see.truetransact.uicomponent.CDialog implements Observer {
    
    private CTable _tblData;
    private HashMap dataHash;
    private ArrayList data;
    private int dataSize;
    private ArrayList _heading;
    private boolean _isAvailable = true;
    Date currDt = null;
    public String branchID;
    public String prodName;
    public String acNum = null;
     private static SqlMap sqlMap = null;
    
    public TradingProductDetailsUI()throws ServiceLocatorException {
        initComponents();
        initForm();
        currDt = ClientUtil.getCurrentDate();
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * Account Number Constructor
     */
    public TradingProductDetailsUI(String productName) {
        prodName = productName;
        initComponents();
        setMaxLengths();
        pendingProductAuthList();
        branchID = TrueTransactMain.BRANCH_ID;
        setupScreen();
        currDt = ClientUtil.getCurrentDate();		
    }

    /**
     * Method which is used to initialize the form TokenConfig
     */
    private void initForm() {
        setMaxLengths();
        setFieldNames();
        internationalize();
    }
    
    private void setupScreen() {
        setModal(true);
        setTitle("TransactionDetails " + "[" + branchID + "]");
        /*
         * Calculate the screen size
         */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        System.out.println("@$#@$#@# screenSize : " + screenSize);
        setSize(650, 450);
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
    
    private void pendingProductAuthList() {
        HashMap map = new HashMap();
        HashMap custMap = new HashMap();
        HashMap depMap = new HashMap();
        map.put("ACT_NUM", acNum);
        map.put("value", acNum); 
        LinkedHashMap where = new LinkedHashMap();
        LinkedHashMap viewMap = new LinkedHashMap();
        where.put("ACT_NUM", acNum);
        viewMap.put(CommonConstants.MAP_NAME, "getTradingProductPendingForAuthorize");
        viewMap.put(CommonConstants.MAP_WHERE, where);
        try {
            populateData(viewMap);
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
        viewMap = null;
        where = null;
    }
    
    public ArrayList populateData(HashMap whereMap) {
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        }
        dataHash = ClientUtil.executeTableQuery(whereMap);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
//        System.out.println("### Data : " + data);
        populateTable();
        setSizeTableData();
        whereMap = null;
        return _heading;
    }
    
    public void populateTable() {
        boolean dataExist;
        if (_heading != null) {
            TableModel model = new TableModel(new ArrayList(), _heading);
            for (int i = 0; i < data.size(); i++) {
                model.insertRow(i, (ArrayList) data.get(i));
            }
            model.fireTableDataChanged();
            tblProduct.setModel(model);
            tblProduct.revalidate();
        }
    }
    
    private void setRightAlignment(int col) {
        javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
        r.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tblProduct.getColumnModel().getColumn(col).setCellRenderer(r);
        tblProduct.getColumnModel().getColumn(col).sizeWidthToFit();
    }

    /**
     * Used to set Maximum possible lenghts for TextFields
     */
    private void setMaxLengths() {
    }

    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */
    private void setFieldNames() {
        panTransDetails.setName("panMemberShipFacility");
    }
    
    private void setSizeTableData() {
        tblProduct.getColumnModel().getColumn(0).setPreferredWidth(30);
        tblProduct.getColumnModel().getColumn(1).setPreferredWidth(50);
        tblProduct.getColumnModel().getColumn(2).setPreferredWidth(20);
        tblProduct.getColumnModel().getColumn(3).setPreferredWidth(25);
        tblProduct.getColumnModel().getColumn(4).setPreferredWidth(40);
        tblProduct.getColumnModel().getColumn(5).setPreferredWidth(3);
        tblProduct.getColumnModel().getColumn(6).setPreferredWidth(30);
    }
    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    
    private void internationalize() {
    }
    
    public void update(java.util.Observable o, Object arg) {
    }
    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    
    public void updateOBFields() {
    }

    /*
     * Auto Generated Method - setMandatoryHashMap()
     *
     * ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     *
     * This method list out all the Input Fields available in the UI. It needs a
     * class level HashMap variable mandatoryMap.
     */
    public void setMandatoryHashMap() {
    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for
     * setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panTransDetails = new com.see.truetransact.uicomponent.CPanel();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        panProductList = new com.see.truetransact.uicomponent.CPanel();
        srpProductListTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblProduct = new com.see.truetransact.uicomponent.CTable();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        panTransDetails.setMaximumSize(new java.awt.Dimension(825, 575));
        panTransDetails.setMinimumSize(new java.awt.Dimension(600, 500));
        panTransDetails.setPreferredSize(new java.awt.Dimension(600, 500));
        panTransDetails.setLayout(new java.awt.GridBagLayout());

        panButton.setMaximumSize(new java.awt.Dimension(823, 40));
        panButton.setMinimumSize(new java.awt.Dimension(500, 40));
        panButton.setPreferredSize(new java.awt.Dimension(823, 40));
        panButton.setLayout(new java.awt.GridBagLayout());

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
        gridBagConstraints.insets = new java.awt.Insets(0, 21, 0, 0);
        panButton.add(btnClose, gridBagConstraints);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setText("Authorize");
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panButton.add(btnAuthorize, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panTransDetails.add(panButton, gridBagConstraints);

        panProductList.setBorder(javax.swing.BorderFactory.createTitledBorder("Pending For Authorization List"));
        panProductList.setMinimumSize(new java.awt.Dimension(600, 350));
        panProductList.setName("panTransInfo"); // NOI18N
        panProductList.setPreferredSize(new java.awt.Dimension(600, 350));
        panProductList.setLayout(new java.awt.GridBagLayout());

        srpProductListTable.setMinimumSize(new java.awt.Dimension(550, 300));
        srpProductListTable.setPreferredSize(new java.awt.Dimension(550, 300));

        tblProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7"
            }
        ));
        tblProduct.setMaximumSize(new java.awt.Dimension(500, 300));
        tblProduct.setMinimumSize(new java.awt.Dimension(500, 300));
        tblProduct.setPreferredScrollableViewportSize(new java.awt.Dimension(600, 200));
        tblProduct.setPreferredSize(new java.awt.Dimension(500, 1000));
        srpProductListTable.setViewportView(tblProduct);

        panProductList.add(srpProductListTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTransDetails.add(panProductList, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panTransDetails, gridBagConstraints);
        panTransDetails.getAccessibleContext().setAccessibleName("MembershipFacifility");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
                                    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
                                                                                                                                        }//GEN-LAST:event_formWindowClosed
    
                                                            private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
                                                                                                                                                                                                                                                                                            }//GEN-LAST:event_formWindowClosing

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
                                                    }//GEN-LAST:event_exitForm
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        HashMap AuthMap = new HashMap();
        AuthMap.put("STATUS", "AUTHORIZED");
        AuthMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
        AuthMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
        try {
            ClientUtil.execute("authorizeNewTradingProduct", AuthMap);
            ClientUtil.clearAll(this);
            btnAuthorize.setEnabled(false);
            this.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //        new CheckCustomerIdUI().show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panProductList;
    private com.see.truetransact.uicomponent.CPanel panTransDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpProductListTable;
    private com.see.truetransact.uicomponent.CTable tblProduct;
    // End of variables declaration//GEN-END:variables
}
