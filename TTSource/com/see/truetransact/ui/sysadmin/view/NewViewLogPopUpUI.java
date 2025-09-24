/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * NewViewLogPopUpUI.java
 *
 * Created on January 4, 2005, 3:55 PM
 */

package com.see.truetransact.ui.sysadmin.view;

import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.ui.sysadmin.view.ViewLogRB;

import java.util.HashMap ;
import java.util.ArrayList ;
import java.awt.Dimension;
import java.awt.Toolkit;
/**
 *
 * @author  152713
 */
public class NewViewLogPopUpUI extends com.see.truetransact.uicomponent.CDialog  {
    
    HashMap paramMap = new HashMap();
    CInternalFrame parent = null;
    
    /** Creates new form NewPopUP */
    public NewViewLogPopUpUI(CInternalFrame parent,HashMap paramMap) {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
    }
    
    /** Creates new form NewPopUP */
    public NewViewLogPopUpUI(String data) {
        initComponents();
        //        edpData.setText(parseData(data));
    }
    
    private void setupInit() {
        initComponents();
        setFieldNames();
        internationalize();
        populateData(paramMap);
    }
    
    
    /** Auto Generated Method - setFieldNames()
     * This method assigns name for all the components.
     * Other functions are working based on this name. */
    private void setFieldNames() {
        btnOk.setName("btnOk");
        lblBranchID.setName("lblBranchID");
        lblBrnID.setName("lblBrnID");
        lblModule.setName("lblModule");
        lblMod.setName("lblMod");
        lblScreen.setName("lblScreen");
        lblScr.setName("lblScr");
        lblData.setName("lblData");
        edpData.setName("edpData");
        lblPrimaryKey.setName("lblPrimaryKey");
        lblPrimKey.setName("lblPrimKey");
        panMain.setName("panMain");
        panButton.setName("panMain");
        panFields.setName("panMain");
        panLeft.setName("panMain");
        panRight.setName("panMain");
    }
    
    
    /** Auto Generated Method - internationalize()
     * This method used to assign display texts from
     * the Resource Bundle File. */
    private void internationalize() {
        ViewLogRB resourceBundle = new ViewLogRB();
        btnOk.setText(resourceBundle.getString("btnOk"));
        lblBranchID.setText(resourceBundle.getString("lblBranchID"));
        lblBrnID.setText(resourceBundle.getString("lblBrnID"));
        lblModule.setText(resourceBundle.getString("lblModule"));
        lblMod.setText(resourceBundle.getString("lblMod"));
        lblScreen.setText(resourceBundle.getString("lblScreen"));
        lblScr.setText(resourceBundle.getString("lblScr"));
        lblData.setText(resourceBundle.getString("lblData"));
        lblPrimaryKey.setText(resourceBundle.getString("lblPrimaryKey"));
        lblPrimKey.setText(resourceBundle.getString("lblPrimKey"));
    }
    
    private void setupScreen() {
        setModal(true);
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
    
    
    /** Populates the Hash Map values to the appropriate lables
     * @param hash
     */
    public void populateData(HashMap hash){
        if (hash.size() > 0){
            lblBrnID.setText(CommonUtil.convertObjToStr(((HashMap)hash.get("LABEL_DISP")).get("BRANCH_ID")));
            lblMod.setText(CommonUtil.convertObjToStr(((HashMap)hash.get("LABEL_DISP")).get("MODULE")));
            lblPrimKey.setText(CommonUtil.convertObjToStr(((HashMap)hash.get("LABEL_DISP")).get("PRIMARY_KEY")));
            lblScr.setText(CommonUtil.convertObjToStr(((HashMap)hash.get("LABEL_DISP")).get("SCREEN")));
            edpData.setText(parseData((HashMap) hash.get("EDITOR_PANE")));
        }
    }
    
    private String parseData(HashMap dataMap){
        StringBuffer strHTMLTag = new StringBuffer("<TABLE BORDER = 1>");
        StringBuffer strHead = new StringBuffer("<TR>");
        StringBuffer strValue = new StringBuffer("<TR>");
        String[] strKeyValue;
        String[] strArray;
        HashMap oneRecord;
        String data;
        if (dataMap.size() > 0){
            // Table Header should be added only one time
            strHead.append("<TD><B>");
            strHead.append("USER ID");
            strHead.append("</B></TD>");
            strHead.append("<TD><B>");
            strHead.append("IP ADDRESS");
            strHead.append("</B></TD>");
            strHead.append("<TD><B>");
            strHead.append("DATE");
            strHead.append("</B></TD>");
            strHead.append("<TD><B>");
            strHead.append("STATUS");
            strHead.append("</B></TD>");
            strHead.append("<TD><B>");
            strHead.append("REMARKS");
            strHead.append("</B></TD>");
        }
        
        for (int recCount = dataMap.size() - 1, k = 0;recCount >= 0;--recCount,++k){
            oneRecord = (HashMap) dataMap.get(String.valueOf(k+1));
            
            strValue.append("<TD>");
            strValue.append(CommonUtil.convertObjToStr(oneRecord.get("USER_ID")));
            strValue.append("</TD>");
            strValue.append("<TD>");
            strValue.append(CommonUtil.convertObjToStr(oneRecord.get("IP_ADDR")));
            strValue.append("</TD>");
            strValue.append("<TD>");
            strValue.append(CommonUtil.convertObjToStr(oneRecord.get("TIME_STAMP")));
            strValue.append("</TD>");
            strValue.append("<TD>");
            strValue.append(CommonUtil.convertObjToStr(oneRecord.get("STATUS")));
            strValue.append("</TD>");
            strValue.append("<TD>");
            strValue.append(CommonUtil.convertObjToStr(oneRecord.get("REMARKS")));
            strValue.append("</TD>");
            
            data = CommonUtil.convertObjToStr(oneRecord.get("DATA"));
            
            strArray = data.split("<%%>");
            for (int i = strArray.length - 1, j = 0;i >= 0;--i,++j){
                strKeyValue = strArray[j].split("=");
                if (strArray[j].length() != 0){
                    if (k == 0){
                        // Table Header should be added only one time
                        strHead.append("<TD><B>");
                        strHead.append(strKeyValue[0]);
                        strHead.append("</B></TD>");
                    }
                    strValue.append("<TD>");
                    if (strKeyValue.length < 2){
                        strValue.append("");
                    }else{
                        strValue.append(strKeyValue[1]);
                    }
                    strValue.append("</TD>");
                    strKeyValue = null;
                }
            }
            if (k == 0){
                // Table Header should be added only one time
                strHead.append("</TR>");
                strHTMLTag.append(strHead);
            }
            strValue.append("</TR>");
            strHTMLTag.append(strValue);
            data = null;
        }
        strHTMLTag.append("</TABLE>");
        return strHTMLTag.toString();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panMain = new com.see.truetransact.uicomponent.CPanel();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnOk = new com.see.truetransact.uicomponent.CButton();
        lblData = new com.see.truetransact.uicomponent.CLabel();
        sprData = new com.see.truetransact.uicomponent.CScrollPane();
        edpData = new com.see.truetransact.uicomponent.CEditorPane();
        panFields = new com.see.truetransact.uicomponent.CPanel();
        panRight = new com.see.truetransact.uicomponent.CPanel();
        lblPrimKey = new com.see.truetransact.uicomponent.CLabel();
        lblBrnID = new com.see.truetransact.uicomponent.CLabel();
        lblBranchID = new com.see.truetransact.uicomponent.CLabel();
        lblPrimaryKey = new com.see.truetransact.uicomponent.CLabel();
        panLeft = new com.see.truetransact.uicomponent.CPanel();
        lblModule = new com.see.truetransact.uicomponent.CLabel();
        lblScreen = new com.see.truetransact.uicomponent.CLabel();
        lblMod = new com.see.truetransact.uicomponent.CLabel();
        lblScr = new com.see.truetransact.uicomponent.CLabel();
        sprFields = new com.see.truetransact.uicomponent.CSeparator();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        panMain.setLayout(new java.awt.GridBagLayout());

        panMain.setMinimumSize(new java.awt.Dimension(450, 400));
        panMain.setPreferredSize(new java.awt.Dimension(450, 400));
        panButton.setLayout(new java.awt.GridBagLayout());

        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnOk, gridBagConstraints);

        lblData.setText("Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(lblData, gridBagConstraints);

        sprData.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sprData.setMinimumSize(new java.awt.Dimension(150, 40));
        sprData.setPreferredSize(new java.awt.Dimension(150, 40));
        sprData.setAutoscrolls(true);
        edpData.setContentType("text/html");
        edpData.setMinimumSize(new java.awt.Dimension(150, 100));
        edpData.setPreferredSize(new java.awt.Dimension(2000, 100));
        edpData.setEnabled(false);
        sprData.setViewportView(edpData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(sprData, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panButton, gridBagConstraints);

        panFields.setLayout(new java.awt.GridBagLayout());

        panFields.setBorder(new javax.swing.border.TitledBorder("Log Details"));
        panRight.setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRight.add(lblPrimKey, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRight.add(lblBrnID, gridBagConstraints);

        lblBranchID.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRight.add(lblBranchID, gridBagConstraints);

        lblPrimaryKey.setText("Primary Key");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRight.add(lblPrimaryKey, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(panRight, gridBagConstraints);

        panLeft.setLayout(new java.awt.GridBagLayout());

        lblModule.setText("Module");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(lblModule, gridBagConstraints);

        lblScreen.setText("Screen");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(lblScreen, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(lblMod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(lblScr, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(panLeft, gridBagConstraints);

        sprFields.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFields.add(sprFields, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panFields, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panMain, gridBagConstraints);

        pack();
    }//GEN-END:initComponents
    
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnOkActionPerformed
    
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        // TODO add your handling code here:
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /** Exit the Application */
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        String data = "<%%>command=UPDATE<%%>class=TermLoanBorrowerTO<%%>package=com.see.truetransact.transferobject.termloan.TermLoanBorrowerTO<%%>";
        new NewViewLogPopUpUI(data).show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CEditorPane edpData;
    private com.see.truetransact.uicomponent.CLabel lblBranchID;
    private com.see.truetransact.uicomponent.CLabel lblBrnID;
    private com.see.truetransact.uicomponent.CLabel lblData;
    private com.see.truetransact.uicomponent.CLabel lblMod;
    private com.see.truetransact.uicomponent.CLabel lblModule;
    private com.see.truetransact.uicomponent.CLabel lblPrimKey;
    private com.see.truetransact.uicomponent.CLabel lblPrimaryKey;
    private com.see.truetransact.uicomponent.CLabel lblScr;
    private com.see.truetransact.uicomponent.CLabel lblScreen;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panFields;
    private com.see.truetransact.uicomponent.CPanel panLeft;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panRight;
    private com.see.truetransact.uicomponent.CScrollPane sprData;
    private com.see.truetransact.uicomponent.CSeparator sprFields;
    // End of variables declaration//GEN-END:variables
    
}
