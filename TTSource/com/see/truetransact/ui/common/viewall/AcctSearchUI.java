/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * AcctSearchUI.java
 */
package com.see.truetransact.ui.common.viewall;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import java.util.ArrayList;
import java.util.HashMap;
import com.see.truetransact.clientutil.ComboBoxModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/*
 * @author anjuanand
 */
public class AcctSearchUI extends JDialog {

    private javax.swing.JDialog jDialog;
    javax.swing.JPanel jPanel = new javax.swing.JPanel(new java.awt.GridBagLayout());
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    com.see.truetransact.clientutil.ComboBoxModel cbmProductType = new com.see.truetransact.clientutil.ComboBoxModel();
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblAccSearch;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    static com.see.truetransact.uicomponent.CTextField txtProdId = new com.see.truetransact.uicomponent.CTextField();
    static com.see.truetransact.uicomponent.CTextField txtAccSearch = new com.see.truetransact.uicomponent.CTextField();
    private com.see.truetransact.uicomponent.CButton btnSearchId;
    private com.see.truetransact.uicomponent.CButton btnSearchAcc;
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    final int EDIT = 0, DELETE = 1, PAYEE = 2, DEBIT = 3, TRANS_PROD = 4, ACC_NUM = 5;
    static int viewType = -1;
    private String PROD_ID = "PROD_ID";
    private boolean transToOtherBank = false;

    public com.see.truetransact.clientutil.ComboBoxModel getCbmProductType() {
        return cbmProductType;
    }

    public void setCbmProductType(com.see.truetransact.clientutil.ComboBoxModel cbmProductType) {
        this.cbmProductType = cbmProductType;
    }

    public AcctSearchUI() {
        transToOtherBank = false;
        setupInit();
        setupScreen();
        resetForm();
    }

    public AcctSearchUI(String source) {
    }

    
    public AcctSearchUI(boolean otherBankTrans) {
        transToOtherBank = otherBankTrans;
        setupInit();
        setupScreen();
        resetForm();        
    }
    

    private void setupInit() {
        initComponents();
        setObservable();
        cboProdType.setModel(getCbmProductType());
    }

    private void setObservable() {
        try {
            setProductType();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    private void setProductType() throws Exception {
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get("PRODUCTTYPE"));
        cbmProductType = new com.see.truetransact.clientutil.ComboBoxModel(key, value);
        cbmProductType.removeKeyAndElement("TD");
        cbmProductType.removeKeyAndElement("TL");
        if(!transToOtherBank){
        cbmProductType.removeKeyAndElement("AB");
        }
        cbmProductType.removeKeyAndElement("GL");
        if(cbmProductType.getKeys().contains("MDS")){
          cbmProductType.removeKeyAndElement("MDS");
        }
        //cbmProductType.removeKeyAndElement("AD");
        keyValue = null;
    }

    private void popUp(int field) {
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        cboProdType.setModel(cbmProductType);
        final HashMap viewMap = new HashMap();
        viewType = field;
        String prodType = CommonUtil.convertObjToStr((((ComboBoxModel) (cboProdType).getModel())).getKeyForSelected());

        if (field == TRANS_PROD) {
            HashMap where_map = new HashMap();
            where_map.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_NAME, "InterMaintenance.getProductData" + prodType);
            viewMap.put(CommonConstants.MAP_WHERE, where_map);
            //            viewMap.put(CommonConstants.MAP_NAME, "product.deposits.getAcctHeadList" + observable.getCbmProductType().getKeyForSelected().toString());
        } else if (field == ACC_NUM) {
            HashMap whereMap = new HashMap();
            prodType = CommonUtil.convertObjToStr((((ComboBoxModel) (cboProdType).getModel())).getKeyForSelected());
            if (!prodType.equals("GL")) {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                        + ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
                //                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString());
            }
            whereMap.put("PROD_ID", CommonUtil.convertObjToStr(txtProdId.getText()));
            if (whereMap.get("SELECTED_BRANCH") == null) {
                whereMap.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
            } else {
                whereMap.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);

        }
        viewMap.put("ACCT_SEARCH", "ACCT_SEARCH");
        new ViewAll(this, viewMap).show();

    }

    public String getAccountNo() {
        String AccountNo = CommonUtil.convertObjToStr(txtAccSearch.getText());
        return AccountNo;
    }
    
    public String getProdType(){
        String prodType = CommonUtil.convertObjToStr((((ComboBoxModel) (cboProdType).getModel())).getKeyForSelected());
        return prodType;
    }
    
    public void resetForm() {
        txtProdId.setText(" ");
        txtAccSearch.setText("");
    }

    public void show() {

//        if (isShow) {
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        pack();

        /* Center frame on the screen */
        Dimension frameSize = getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
        this.setSize(350, 250);
        setModal(true);
        super.show();
        // }


//          super.show();
    }

    private void setupScreen() {
        //        setModal(true);

        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
//        this.setSize(250, 350);
    }

    public void fillData(Object obj) {

        HashMap dataMap = (HashMap) obj;
        if (viewType == TRANS_PROD) {
            final String productId = CommonUtil.convertObjToStr(dataMap.get(PROD_ID));
            txtProdId.setText(productId);

        } else if (viewType == ACC_NUM) {
            txtAccSearch.setText(CommonUtil.convertObjToStr(dataMap.get("ACCOUNTNO")));
            txtAccSearch.setVisible(true);
        }

    }

    private void initComponents() {

        jDialog = new JDialog();
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        jDialog.setTitle("");
        jDialog.setMaximumSize(new java.awt.Dimension(150, 120));
        jDialog.setPreferredSize(new java.awt.Dimension(150, 120));
        jDialog.setMinimumSize(new java.awt.Dimension(150, 120));
        jDialog.getContentPane().setLayout(gridBag);
        gridBagConstraints.fill = GridBagConstraints.BOTH;

        jPanel = new JPanel();
        jPanel.setLayout(gridBag);
        jPanel.setName("panAcctSearch");
        jPanel.setBorder(new javax.swing.border.TitledBorder(""));
        jPanel.setMaximumSize(new java.awt.Dimension(150, 120));
        jPanel.setMinimumSize(new java.awt.Dimension(150, 120));
        jPanel.setPreferredSize(new java.awt.Dimension(150, 120));
        gridBagConstraints.fill = GridBagConstraints.BOTH;

        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        lblProdType.setText("Product Type");
        lblProdType.setMaximumSize(new java.awt.Dimension(100, 21));
        lblProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        lblProdType.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.ipadx = 0;
        gridBagConstraints.ipady = 11;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBag.setConstraints(lblProdType, gridBagConstraints);
        jPanel.add(lblProdType);

        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        cboProdType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPreferredSize(new java.awt.Dimension(100, 21));
        cboProdType.setModel(cbmProductType);
        
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 0;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 2);
        gridBag.setConstraints(cboProdType, gridBagConstraints);
        jPanel.add(cboProdType);
        cboProdType.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cboProdTypeActionPerformed(e);
            }

            private void cboProdTypeActionPerformed(ActionEvent e) {
            }
        });

        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        lblProdId.setText("Product Id");
        lblProdId.setMaximumSize(new java.awt.Dimension(100, 21));
        lblProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        lblProdId.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.ipadx = 0;
        gridBagConstraints.ipady = 0;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        gridBag.setConstraints(lblProdId, gridBagConstraints);
        jPanel.add(lblProdId);

        txtProdId.setMaximumSize(new java.awt.Dimension(100, 21));
        txtProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProdId.setPreferredSize(new java.awt.Dimension(100, 21));
        txtProdId.setEditable(true);
        txtProdId.setAllowAll(true);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.ipadx = 0;
        gridBagConstraints.ipady = 0;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 0, 10);
        gridBag.setConstraints(txtProdId, gridBagConstraints);
        jPanel.add(txtProdId, gridBagConstraints);
         txtProdId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProdIdFocusLost(evt);
            }
        });

        lblAccSearch = new com.see.truetransact.uicomponent.CLabel();
        lblAccSearch.setText("Account Number");
        lblAccSearch.setMaximumSize(new java.awt.Dimension(100, 21));
        lblAccSearch.setMinimumSize(new java.awt.Dimension(100, 21));
        lblAccSearch.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.ipadx = 0;
        gridBagConstraints.ipady = 0;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 1, 0);
        gridBag.setConstraints(lblAccSearch, gridBagConstraints);
        jPanel.add(lblAccSearch, gridBagConstraints);

        txtAccSearch.setMaximumSize(new java.awt.Dimension(100, 21));
        txtAccSearch.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccSearch.setPreferredSize(new java.awt.Dimension(100, 21));
        txtAccSearch.setAllowAll(true);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.ipadx = 0;
        gridBagConstraints.ipady = 0;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 0, 10);
        gridBag.setConstraints(txtAccSearch, gridBagConstraints);
        jPanel.add(txtAccSearch, gridBagConstraints);
         txtAccSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccSearchFocusLost(evt);
            }
        });
        
        
        
        btnSearchId = new com.see.truetransact.uicomponent.CButton();
        btnSearchId.setText("Search");
        btnSearchId.setFocusable(true);
        btnSearchId.setMaximumSize(new java.awt.Dimension(56, 20));
        btnSearchId.setMinimumSize(new java.awt.Dimension(56, 20));
        btnSearchId.setPreferredSize(new java.awt.Dimension(56, 20));
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        gridBag.setConstraints(btnSearchId, gridBagConstraints);
        jPanel.add(btnSearchId, gridBagConstraints);
        btnSearchId.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                btnSearchIdActionPerformed(e);
            }

            private void btnSearchIdActionPerformed(ActionEvent e) {
                final HashMap viewMap = new HashMap();
                viewType = 4;
                if (cboProdType.getSelectedIndex() > 0) {
                    popUp(TRANS_PROD);
                } else {
                    ClientUtil.displayAlert("Please select Product Id");
                }
            }
        });

        btnSearchAcc = new com.see.truetransact.uicomponent.CButton();
        btnSearchAcc.setText("Search");
        btnSearchAcc.setFocusable(true);
        btnSearchAcc.setMaximumSize(new java.awt.Dimension(56, 20));
        btnSearchAcc.setMinimumSize(new java.awt.Dimension(56, 20));
        btnSearchAcc.setPreferredSize(new java.awt.Dimension(56, 20));
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        gridBag.setConstraints(btnSearchAcc, gridBagConstraints);
        jPanel.add(btnSearchAcc, gridBagConstraints);
        btnSearchAcc.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnSearchAccActionPerfrmed(e);
            }

            private void btnSearchAccActionPerfrmed(ActionEvent e) {
                if (txtProdId.getText() != null) { 
                    popUp(ACC_NUM);
                } else {
                    ClientUtil.displayAlert("Please select Product Id");
                }
            }
        });

        btnOk = new com.see.truetransact.uicomponent.CButton();
        btnOk.setText("OK");
        btnOk.setFocusable(true);
        btnOk.setMaximumSize(new java.awt.Dimension(56, 20));
        btnOk.setMinimumSize(new java.awt.Dimension(56, 20));
        btnOk.setPreferredSize(new java.awt.Dimension(56, 20));
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.ipadx = 0;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        gridBag.setConstraints(btnOk, gridBagConstraints);
        jPanel.add(btnOk, gridBagConstraints);
        btnOk.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                btnOkActionPerformed(e);

            }

            private void btnOkActionPerformed(ActionEvent e) {
                //dispose();
                if(txtAccSearch.getText().length() == 0){// Added by nithya on 17-07-2019 for KD 557 - Gold loan Renewal did not create transaction
                   ClientUtil.showMessageWindow("Account number should not be empty");
                   return;
                }else{
                    dispose();
                }
            }
        });

        btnCancel = new com.see.truetransact.uicomponent.CButton();
        btnCancel.setText("Cancel");
        btnCancel.setFocusable(true);
        btnCancel.setMaximumSize(new java.awt.Dimension(56, 20));
        btnCancel.setMinimumSize(new java.awt.Dimension(56, 20));
        btnCancel.setPreferredSize(new java.awt.Dimension(56, 20));
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 0;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        gridBag.setConstraints(btnCancel, gridBagConstraints);
        jPanel.add(btnCancel, gridBagConstraints);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                btnCancelActionPerformed(e);
            }

            private void btnCancelActionPerformed(ActionEvent e) {
                txtProdId.setText(" ");
                txtAccSearch.setText(" ");
                dispose();
            }
        });
        add(jPanel, BorderLayout.CENTER);
        setTitle("ACCOUNT SEARCH");
        
    }
    public void txtAccSearchFocusLost(java.awt.event.FocusEvent evt) {
        HashMap supMap = new HashMap();
        if (txtAccSearch.getText() != null && txtAccSearch.getText().length() > 0) {
            supMap.put("ACCT_NUM", txtAccSearch.getText());
            java.util.List lstSup = ClientUtil.executeQuery("getProdIdTypeForSelectedAct", supMap);
            HashMap supMap1 = new HashMap();
            if (lstSup != null && lstSup.size() > 0) {
                supMap1 = (HashMap) lstSup.get(0);
                String accStatus = CommonUtil.convertObjToStr(supMap1.get("ACCT_STATUS"));
                if(accStatus!=null && accStatus.equals("CLOSED")){
                	txtAccSearch.setText(" ");
                    ClientUtil.showAlertWindow("Invalid Account Number!!!");
                    return;
                }else{
                	String Prod_Id = CommonUtil.convertObjToStr(supMap1.get("PROD_ID"));
                	String Prod_Type = CommonUtil.convertObjToStr(supMap1.get("PROD_TYPE"));
                	txtProdId.setText(Prod_Id);
                	cboProdType.setSelectedItem(CommonUtil.convertObjToStr(cbmProductType.getDataForKey(Prod_Type)));
            	}
            }
        }
        return;
    }
    public void txtProdIdFocusLost(java.awt.event.FocusEvent evt) {
        HashMap supMap = new HashMap();
        supMap.put("PROD_ID", txtProdId.getText());
        java.util.List lstSup = ClientUtil.executeQuery("getProdIdTypeForSelectedAct", supMap);
        HashMap supMap1 = new HashMap();
        if (lstSup != null && lstSup.size() > 0) {
            supMap1 = (HashMap) lstSup.get(0);
            String Prod_Type = CommonUtil.convertObjToStr(supMap1.get("PROD_TYPE"));
            cboProdType.setSelectedItem(CommonUtil.convertObjToStr(cbmProductType.getDataForKey(Prod_Type)));
        }
    }
    
     public String getProductId(){
        String prodId = CommonUtil.convertObjToStr(txtProdId.getText());
        return prodId;
    }
    
}
