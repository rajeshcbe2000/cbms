/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ChequeEnquiryUI.java
 *
 * Created on January 13, 2015, 10:31 AM
 */

package com.see.truetransact.ui.transaction.chequeenquiry;

import  com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
/**
 *
 * @author  Jeffin John
 *
 */
public class ChequeEnquiryUI extends CInternalFrame {
    
    /** Creates new form LevelControlUI */
    public ChequeEnquiryUI() {
        initComponents();
        initSetup();
    }
    
    private void initSetup(){
        setFieldNames();
        ClientUtil.enableDisable(this, true);
        lblActHolderView_2.setVisible(false);
        lblActHolderView_3.setVisible(false);
        lblActHolderView_4.setVisible(false);
        lblActHolderView_5.setVisible(false);
    }
    
    private void setFieldNames() {
        lblActNo.setName("lblActNo");
        lblActType.setName("lblActType");
        lblChequeLeafNo.setName("lblChequeLeafNo");
        lblActStatus.setName("lblActStatus");
        lblActHolder.setName("lblActHolder");
        panAccountDetails.setName("panAccountDetails");
        panChequeLeafNo.setName("panChequeLeafNo");
        panChequeLeaf.setName("panChequeLeaf");
        txtInstNo1.setName("txtInstNo1");
        txtInstNo2.setName("txtInstNo2");
    }
    
    /** Method used to do Required operation when user clicks btnAuthorize,btnReject or btnReject **/
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panChequeLeaf = new com.see.truetransact.uicomponent.CPanel();
        panChequeLeafNo = new com.see.truetransact.uicomponent.CPanel();
        lblChequeLeafNo = new com.see.truetransact.uicomponent.CLabel();
        txtInstNo1 = new com.see.truetransact.uicomponent.CTextField();
        txtInstNo2 = new com.see.truetransact.uicomponent.CTextField();
        btnFetch = new com.see.truetransact.uicomponent.CButton();
        panAccountDetails = new com.see.truetransact.uicomponent.CPanel();
        lblActNo = new com.see.truetransact.uicomponent.CLabel();
        lblActHolder = new com.see.truetransact.uicomponent.CLabel();
        lblActType = new com.see.truetransact.uicomponent.CLabel();
        lblActNoView = new com.see.truetransact.uicomponent.CLabel();
        lblActHolderView_1 = new com.see.truetransact.uicomponent.CLabel();
        lblActTypeView = new com.see.truetransact.uicomponent.CLabel();
        lblActStatus = new com.see.truetransact.uicomponent.CLabel();
        lblAccountStatusView = new com.see.truetransact.uicomponent.CLabel();
        lblChequeStatus = new com.see.truetransact.uicomponent.CLabel();
        lblChequeStatusView = new com.see.truetransact.uicomponent.CLabel();
        lblActHolderView_2 = new com.see.truetransact.uicomponent.CLabel();
        lblActHolderView_3 = new com.see.truetransact.uicomponent.CLabel();
        lblActHolderView_4 = new com.see.truetransact.uicomponent.CLabel();
        lblActHolderView_5 = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        panChequeLeaf.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panChequeLeaf.setLayout(new java.awt.GridBagLayout());

        panChequeLeafNo.setLayout(new java.awt.GridBagLayout());

        lblChequeLeafNo.setText("Cheque Leaf No");
        panChequeLeafNo.add(lblChequeLeafNo, new java.awt.GridBagConstraints());

        txtInstNo1.setAllowAll(true);
        txtInstNo1.setAllowNumber(true);
        txtInstNo1.setMinimumSize(new java.awt.Dimension(50, 21));
        txtInstNo1.setPreferredSize(new java.awt.Dimension(50, 21));
        txtInstNo1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInstNo1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeLeafNo.add(txtInstNo1, gridBagConstraints);

        txtInstNo2.setAllowAll(true);
        txtInstNo2.setAllowNumber(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeLeafNo.add(txtInstNo2, gridBagConstraints);

        btnFetch.setText("Fetch");
        btnFetch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFetchActionPerformed(evt);
            }
        });
        panChequeLeafNo.add(btnFetch, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 125;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(136, 44, 0, 71);
        panChequeLeaf.add(panChequeLeafNo, gridBagConstraints);

        panAccountDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Details"));
        panAccountDetails.setLayout(new java.awt.GridBagLayout());

        lblActNo.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAccountDetails.add(lblActNo, gridBagConstraints);

        lblActHolder.setText("Account Holder");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAccountDetails.add(lblActHolder, gridBagConstraints);

        lblActType.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAccountDetails.add(lblActType, gridBagConstraints);

        lblActNoView.setMaximumSize(new java.awt.Dimension(100, 21));
        lblActNoView.setMinimumSize(new java.awt.Dimension(100, 21));
        lblActNoView.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAccountDetails.add(lblActNoView, gridBagConstraints);

        lblActHolderView_1.setMaximumSize(new java.awt.Dimension(100, 21));
        lblActHolderView_1.setMinimumSize(new java.awt.Dimension(100, 21));
        lblActHolderView_1.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAccountDetails.add(lblActHolderView_1, gridBagConstraints);

        lblActTypeView.setMaximumSize(new java.awt.Dimension(100, 21));
        lblActTypeView.setMinimumSize(new java.awt.Dimension(100, 21));
        lblActTypeView.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAccountDetails.add(lblActTypeView, gridBagConstraints);

        lblActStatus.setText("Account Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAccountDetails.add(lblActStatus, gridBagConstraints);

        lblAccountStatusView.setMaximumSize(new java.awt.Dimension(100, 21));
        lblAccountStatusView.setMinimumSize(new java.awt.Dimension(100, 21));
        lblAccountStatusView.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAccountDetails.add(lblAccountStatusView, gridBagConstraints);

        lblChequeStatus.setText("Cheque Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAccountDetails.add(lblChequeStatus, gridBagConstraints);

        lblChequeStatusView.setMaximumSize(new java.awt.Dimension(100, 21));
        lblChequeStatusView.setMinimumSize(new java.awt.Dimension(100, 21));
        lblChequeStatusView.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAccountDetails.add(lblChequeStatusView, gridBagConstraints);

        lblActHolderView_2.setMaximumSize(new java.awt.Dimension(100, 21));
        lblActHolderView_2.setMinimumSize(new java.awt.Dimension(100, 21));
        lblActHolderView_2.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAccountDetails.add(lblActHolderView_2, gridBagConstraints);

        lblActHolderView_3.setMaximumSize(new java.awt.Dimension(100, 21));
        lblActHolderView_3.setMinimumSize(new java.awt.Dimension(100, 21));
        lblActHolderView_3.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAccountDetails.add(lblActHolderView_3, gridBagConstraints);

        lblActHolderView_4.setMaximumSize(new java.awt.Dimension(100, 21));
        lblActHolderView_4.setMinimumSize(new java.awt.Dimension(100, 21));
        lblActHolderView_4.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAccountDetails.add(lblActHolderView_4, gridBagConstraints);

        lblActHolderView_5.setMaximumSize(new java.awt.Dimension(100, 21));
        lblActHolderView_5.setMinimumSize(new java.awt.Dimension(100, 21));
        lblActHolderView_5.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAccountDetails.add(lblActHolderView_5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 106, 132, 0);
        panChequeLeaf.add(panAccountDetails, gridBagConstraints);
        panAccountDetails.getAccessibleContext().setAccessibleName("Details");

        getContentPane().add(panChequeLeaf, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        List nameList = new ArrayList();
        if(hash!=null && hash.size()>0){
            if(hash.containsKey("NAME")){
                nameList = (ArrayList) hash.get("NAME");
                for(int i = 0; i<nameList.size();i++){
                    if(i==0){
                        lblActHolderView_1.setText("1.)"+CommonUtil.convertObjToStr(nameList.get(i)));
                    }
                    if(i==1){
                        lblActHolderView_2.setVisible(true);
                        lblActHolderView_2.setText("2.)"+CommonUtil.convertObjToStr(nameList.get(i)));
                    }
                    if(i==2){
                        lblActHolderView_3.setVisible(true);
                        lblActHolderView_3.setText("3.)"+CommonUtil.convertObjToStr(nameList.get(i)));
                    }
                    if(i==3){
                        lblActHolderView_4.setVisible(true);
                        lblActHolderView_4.setText("4.)"+CommonUtil.convertObjToStr(nameList.get(i)));
                    }
                    if(i==4){
                        lblActHolderView_5.setVisible(true);
                        lblActHolderView_5.setText("5.)"+CommonUtil.convertObjToStr(nameList.get(i)));
                    }
                }
            }
            lblActNoView.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
            lblActTypeView.setText(CommonUtil.convertObjToStr(hash.get("TYPE")));
            lblAccountStatusView.setText(CommonUtil.convertObjToStr(hash.get("STATUS")));
            HashMap chequeMap = new HashMap();
            chequeMap.put("ACT_NUM", CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
            chequeMap.put("INSTNO1", CommonUtil.convertObjToStr(hash.get("INSTNO1")));
            chequeMap.put("INSTNO2", CommonUtil.convertObjToStr(hash.get("INSTNO2")));
            try{
                List chequeList = ClientUtil.executeQuery("getChequeAuthorizedDetails", chequeMap);
                if(chequeList != null && chequeList.size()>0){
                    chequeMap = (HashMap)chequeList.get(0);
                    String authStatus = CommonUtil.convertObjToStr(chequeMap.get("AUTHORIZE_STATUS"));
                    if(authStatus!=null && !authStatus.equals("") && authStatus.equals(CommonConstants.STATUS_AUTHORIZED)){
                        lblChequeStatusView.setText("USED");
                    }
                    if(authStatus!=null && !authStatus.equals("") && authStatus.equals(CommonConstants.STATUS_REJECTED)){
                        lblChequeStatusView.setText("NOT USED");
                    }
                    if(authStatus==null && authStatus.equals("") && authStatus.equals(null)){
                        lblChequeStatusView.setText("CLEARING PENDING");
                    }
                } else {
                    lblChequeStatusView.setText("NOT USED");
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }                            
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

    private void txtInstNo1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInstNo1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInstNo1FocusLost

    private void btnFetchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFetchActionPerformed
        // TODO add your handling code here:
        
        lblActHolderView_2.setVisible(false);
        lblActHolderView_3.setVisible(false);
        lblActHolderView_4.setVisible(false);
        lblActHolderView_5.setVisible(false);
        
        String instNo1 = CommonUtil.convertObjToStr(txtInstNo1.getText());
        String instNo2 = CommonUtil.convertObjToStr(txtInstNo2.getText());
        if (instNo1 == null || instNo1.equals(null) || instNo1.equals("") || instNo2 == null || instNo2.equals(null) || instNo2.equals("")) {
            ClientUtil.showAlertWindow("Please Enter both the fields!!!!!");
            return;
        } else {
            try {
                HashMap chequeLeafMap = new HashMap();
                chequeLeafMap.put("INSTRUMENT1", instNo1);
                chequeLeafMap.put("INSTRUMENT2", instNo2);
                List name = new ArrayList();
                List detailedChequeLooseLeaf = ClientUtil.executeQuery("getLooseLeafRule", chequeLeafMap);
                List detailedChequeIssue = ClientUtil.executeQuery("getChequeIssueNoRule", chequeLeafMap);
                if (detailedChequeLooseLeaf != null && detailedChequeLooseLeaf.size() > 0) {
                    HashMap detailedMap = (HashMap) detailedChequeLooseLeaf.get(0);
                    if (detailedMap != null && detailedMap.size() > 0) {
                        String prodType = CommonUtil.convertObjToStr(detailedMap.get("PROD_TYPE"));
                        String actNo = CommonUtil.convertObjToStr(detailedMap.get("ACCT_NO"));
                        if(prodType != null && !prodType.equals("") && prodType.equals("OA")){
                            detailedMap.put("ACT_NUM", actNo);
                            List operativeList = ClientUtil.executeQuery("getActDataOA", detailedMap);
                            if(operativeList !=null && operativeList.size()>0){
                                HashMap newOAMap = (HashMap) operativeList.get(0);
                                if(newOAMap != null && newOAMap.size() > 0) {
                                    String constitution = CommonUtil.convertObjToStr(newOAMap.get("CONSTITUTION"));
                                    String custId = CommonUtil.convertObjToStr(newOAMap.get("CUST_ID"));
                                    String status = CommonUtil.convertObjToStr(newOAMap.get("STATUS"));
                                    HashMap custDetailsMap = new HashMap();
                                    custDetailsMap.put("AUTHORIZE_CUST_ID", custId);
                                    List custDetailsList = ClientUtil.executeQuery("getCustSelectAccInfoTblDisplay", custDetailsMap);
                                    if(custDetailsList!=null && custDetailsList.size()>0){
                                        custDetailsMap = (HashMap) custDetailsList.get(0);
                                        String custName = CommonUtil.convertObjToStr(custDetailsMap.get("Name"));
                                        name.add(custName);
                                    }
                                    if(constitution.equals("JOINT")){
                                        HashMap jointMap = new HashMap();
                                        jointMap.put("value", actNo);
                                        jointMap.put("CHEQUE_ENQUIRY","CHEQUE_ENQUIRY");
                                        List jointActList = ClientUtil.executeQuery("getSelectAccountJointTO", jointMap);
                                        if(jointActList!=null && jointActList.size()>0){
                                            for(int i=0;i<jointActList.size();i++){
                                                jointMap = (HashMap) jointActList.get(i);
                                                String jointCustId = CommonUtil.convertObjToStr(jointMap.get("CUST_ID"));
                                                custDetailsMap = new HashMap();
                                                custDetailsMap.put("AUTHORIZE_CUST_ID", jointCustId);
                                                custDetailsList = ClientUtil.executeQuery("getCustSelectAccInfoTblDisplay", custDetailsMap);
                                                if(custDetailsList!=null && custDetailsList.size()>0){
                                                    custDetailsMap = (HashMap) custDetailsList.get(0);
                                                    String custName = CommonUtil.convertObjToStr(custDetailsMap.get("Name"));
                                                    name.add(custName);
                                                }
                                            }
                                        }
                                    }
                                    HashMap fillMap = new HashMap();
                                    fillMap.put("NAME", name);
                                    fillMap.put("ACT_NUM", actNo);
                                    fillMap.put("TYPE", constitution);
                                    fillMap.put("STATUS", status);
                                    fillMap.put("INSTNO1", instNo1);
                                    fillMap.put("INSTNO2", instNo2);
                                    fillData(fillMap);
                                }
                            }
                        }
                        if(prodType != null && !prodType.equals("") && prodType.equals("AD")){
                            detailedMap.put("ACT_NUM", actNo);
                            List operativeList = ClientUtil.executeQuery("getActDataAD", detailedMap);
                            if(operativeList !=null && operativeList.size()>0){
                                HashMap newOAMap = (HashMap) operativeList.get(0);
                                if(newOAMap != null && newOAMap.size() > 0) {
                                    String constitution = CommonUtil.convertObjToStr(newOAMap.get("CONSTITUTION"));
                                    String custId = CommonUtil.convertObjToStr(newOAMap.get("CUST_ID"));
                                    String status = CommonUtil.convertObjToStr(newOAMap.get("STATUS"));
                                    HashMap custDetailsMap = new HashMap();
                                    custDetailsMap.put("AUTHORIZE_CUST_ID", custId);
                                    List custDetailsList = ClientUtil.executeQuery("getCustSelectAccInfoTblDisplay", custDetailsMap);
                                    if(custDetailsList!=null && custDetailsList.size()>0){
                                        custDetailsMap = (HashMap) custDetailsList.get(0);
                                        String custName = CommonUtil.convertObjToStr(custDetailsMap.get("Name"));
                                        name.add(custName);
                                    }
                                    if(constitution.equals("JOINT")){
                                        HashMap jointMap = new HashMap();
                                        jointMap.put("value", actNo);
                                        List jointActList = ClientUtil.executeQuery("getSelectTermLoanJointAcctTO", jointMap);
                                        if(jointActList!=null && jointActList.size()>0){
                                            for(int i=0;i<jointActList.size();i++){
                                                jointMap = (HashMap) jointActList.get(i);
                                                String jointCustId = CommonUtil.convertObjToStr(jointMap.get("CUST_ID"));
                                                custDetailsMap = new HashMap();
                                                custDetailsMap.put("AUTHORIZE_CUST_ID", jointCustId);
                                                custDetailsList = ClientUtil.executeQuery("getCustSelectAccInfoTblDisplay", custDetailsMap);
                                                if(custDetailsList!=null && custDetailsList.size()>0){
                                                    custDetailsMap = (HashMap) custDetailsList.get(0);
                                                    String custName = CommonUtil.convertObjToStr(custDetailsMap.get("Name"));
                                                    name.add(custName);
                                                }
                                            }
                                        }
                                    }
                                    HashMap fillMap = new HashMap();
                                    fillMap.put("NAME", name);
                                    fillMap.put("ACT_NUM", actNo);
                                    fillMap.put("TYPE", constitution);
                                    fillMap.put("STATUS", status);
                                    fillMap.put("INSTNO1", instNo1);
                                    fillMap.put("INSTNO2", instNo2);
                                    fillData(fillMap);
                                }
                            }
                        }
                    }
                } else if(detailedChequeIssue != null && detailedChequeIssue.size() > 0) {
                    HashMap detailedMap = (HashMap) detailedChequeIssue.get(0);
                    if (detailedMap != null && detailedMap.size() > 0) {
                        String prodType = CommonUtil.convertObjToStr(detailedMap.get("PROD_TYPE"));
                        String actNo = CommonUtil.convertObjToStr(detailedMap.get("ACCT_NO"));
                        if(prodType != null && !prodType.equals("") && prodType.equals("OA")){
                            detailedMap.put("ACT_NUM", actNo);
                            List operativeList = ClientUtil.executeQuery("getActDataOA", detailedMap);
                            if(operativeList !=null && operativeList.size()>0){
                                HashMap newOAMap = (HashMap) operativeList.get(0);
                                if(newOAMap != null && newOAMap.size() > 0) {
                                    String constitution = CommonUtil.convertObjToStr(newOAMap.get("CONSTITUTION"));
                                    String custId = CommonUtil.convertObjToStr(newOAMap.get("CUST_ID"));
                                    String status = CommonUtil.convertObjToStr(newOAMap.get("STATUS"));
                                    HashMap custDetailsMap = new HashMap();
                                    custDetailsMap.put("AUTHORIZE_CUST_ID", custId);
                                    List custDetailsList = ClientUtil.executeQuery("getCustSelectAccInfoTblDisplay", custDetailsMap);
                                    if(custDetailsList!=null && custDetailsList.size()>0){
                                        custDetailsMap = (HashMap) custDetailsList.get(0);
                                        String custName = CommonUtil.convertObjToStr(custDetailsMap.get("Name"));
                                        name.add(custName);
                                    }
                                    if(constitution.equals("JOINT")){
                                        HashMap jointMap = new HashMap();
                                        jointMap.put("value", actNo);
                                        jointMap.put("CHEQUE_ENQUIRY","CHEQUE_ENQUIRY");
                                        List jointActList = ClientUtil.executeQuery("getSelectAccountJointTO", jointMap);
                                        if(jointActList!=null && jointActList.size()>0){
                                            for(int i=0;i<jointActList.size();i++){
                                                jointMap = (HashMap) jointActList.get(i);
                                                String jointCustId = CommonUtil.convertObjToStr(jointMap.get("CUST_ID"));
                                                custDetailsMap = new HashMap();
                                                custDetailsMap.put("AUTHORIZE_CUST_ID", jointCustId);
                                                custDetailsList = ClientUtil.executeQuery("getCustSelectAccInfoTblDisplay", custDetailsMap);
                                                if(custDetailsList!=null && custDetailsList.size()>0){
                                                    custDetailsMap = (HashMap) custDetailsList.get(0);
                                                    String custName = CommonUtil.convertObjToStr(custDetailsMap.get("Name"));
                                                    name.add(custName);
                                                }
                                            }
                                        }
                                    }
                                    HashMap fillMap = new HashMap();
                                    fillMap.put("NAME", name);
                                    fillMap.put("ACT_NUM", actNo);
                                    fillMap.put("TYPE", constitution);
                                    fillMap.put("STATUS", status);
                                    fillMap.put("INSTNO1", instNo1);
                                    fillMap.put("INSTNO2", instNo2);
                                    fillData(fillMap);
                                }
                            }
                        }
                        if(prodType != null && !prodType.equals("") && prodType.equals("AD")){
                            detailedMap.put("ACT_NUM", actNo);
                            List operativeList = ClientUtil.executeQuery("getActDataAD", detailedMap);
                            if(operativeList !=null && operativeList.size()>0){
                                HashMap newOAMap = (HashMap) operativeList.get(0);
                                if(newOAMap != null && newOAMap.size() > 0) {
                                    String constitution = CommonUtil.convertObjToStr(newOAMap.get("CONSTITUTION"));
                                    String custId = CommonUtil.convertObjToStr(newOAMap.get("CUST_ID"));
                                    String status = CommonUtil.convertObjToStr(newOAMap.get("STATUS"));
                                    HashMap custDetailsMap = new HashMap();
                                    custDetailsMap.put("AUTHORIZE_CUST_ID", custId);
                                    List custDetailsList = ClientUtil.executeQuery("getCustSelectAccInfoTblDisplay", custDetailsMap);
                                    if(custDetailsList!=null && custDetailsList.size()>0){
                                        custDetailsMap = (HashMap) custDetailsList.get(0);
                                        String custName = CommonUtil.convertObjToStr(custDetailsMap.get("Name"));
                                        name.add(custName);
                                    }
                                    if(constitution.equals("JOINT")){
                                        HashMap jointMap = new HashMap();
                                        jointMap.put("value", actNo);
                                        List jointActList = ClientUtil.executeQuery("getSelectTermLoanJointAcctTO", jointMap);
                                        if(jointActList!=null && jointActList.size()>0){
                                            for(int i=0;i<jointActList.size();i++){
                                                jointMap = (HashMap) jointActList.get(i);
                                                String jointCustId = CommonUtil.convertObjToStr(jointMap.get("CUST_ID"));
                                                custDetailsMap = new HashMap();
                                                custDetailsMap.put("AUTHORIZE_CUST_ID", jointCustId);
                                                custDetailsList = ClientUtil.executeQuery("getCustSelectAccInfoTblDisplay", custDetailsMap);
                                                if(custDetailsList!=null && custDetailsList.size()>0){
                                                    custDetailsMap = (HashMap) custDetailsList.get(0);
                                                    String custName = CommonUtil.convertObjToStr(custDetailsMap.get("Name"));
                                                    name.add(custName);
                                                }
                                            }
                                        }
                                    }
                                    HashMap fillMap = new HashMap();
                                    fillMap.put("NAME", name);
                                    fillMap.put("ACT_NUM", actNo);
                                    fillMap.put("TYPE", constitution);
                                    fillMap.put("STATUS", status);
                                    fillMap.put("INSTNO1", instNo1);
                                    fillMap.put("INSTNO2", instNo2);
                                    fillData(fillMap);
                                }
                            }
                        }
                    }
                } else{
                    ClientUtil.showAlertWindow("Not a Vaid Cheque Leaf!!!!!");
                    txtInstNo1.setText("");
                    txtInstNo2.setText("");
                    lblActHolderView_1.setText("");
                    lblAccountStatusView.setText("");
                    lblActNoView.setText("");
                    lblActTypeView.setText("");
                    lblChequeStatusView.setText("");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnFetchActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new ChequeEnquiryUI().show();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnFetch;
    private com.see.truetransact.uicomponent.CLabel lblAccountStatusView;
    private com.see.truetransact.uicomponent.CLabel lblActHolder;
    private com.see.truetransact.uicomponent.CLabel lblActHolderView_1;
    private com.see.truetransact.uicomponent.CLabel lblActHolderView_2;
    private com.see.truetransact.uicomponent.CLabel lblActHolderView_3;
    private com.see.truetransact.uicomponent.CLabel lblActHolderView_4;
    private com.see.truetransact.uicomponent.CLabel lblActHolderView_5;
    private com.see.truetransact.uicomponent.CLabel lblActNo;
    private com.see.truetransact.uicomponent.CLabel lblActNoView;
    private com.see.truetransact.uicomponent.CLabel lblActStatus;
    private com.see.truetransact.uicomponent.CLabel lblActType;
    private com.see.truetransact.uicomponent.CLabel lblActTypeView;
    private com.see.truetransact.uicomponent.CLabel lblChequeLeafNo;
    private com.see.truetransact.uicomponent.CLabel lblChequeStatus;
    private com.see.truetransact.uicomponent.CLabel lblChequeStatusView;
    private com.see.truetransact.uicomponent.CPanel panAccountDetails;
    private com.see.truetransact.uicomponent.CPanel panChequeLeaf;
    private com.see.truetransact.uicomponent.CPanel panChequeLeafNo;
    private com.see.truetransact.uicomponent.CTextField txtInstNo1;
    private com.see.truetransact.uicomponent.CTextField txtInstNo2;
    // End of variables declaration//GEN-END:variables
    
}
