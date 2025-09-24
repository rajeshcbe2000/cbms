/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RejectionApproveUI.java
 *
 * Created on April 13, 2011, 10:10 PM
 */

package com.see.truetransact.ui.correctiontool;

import com.see.truetransact.ui.common.viewall.*;
import java.util.Observer;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;

import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.transaction.rollback.RollBackUI;
import com.see.truetransact.commonutil.StringEncrypter;
import com.see.truetransact.ui.deposit.closing.DepositClosingUI;
import com.see.truetransact.ui.operativeaccount.AccountClosingUI;
import com.see.truetransact.ui.transaction.transfer.TransferUI;
import com.see.truetransact.uicomponent.CTextArea;
/**
 *
 * @author  Suresh
 */
public class DataCorrectionApproveUI extends com.see.truetransact.uicomponent.CDialog implements Observer {
    
    
    //    final CheckCustomerIdRB resourceBundle = new CheckCustomerIdRB();
    private CTable _tblData;
    private HashMap dataHash;
    private ArrayList data;
    private int dataSize;
    private ArrayList _heading;
    private boolean _isAvailable = true;
    private ArrayList termLoanData = new ArrayList();
    Date currDt = null;
    public String branchID;
    private double displayBalance = 0;
    private double totalBalance = 0;
    private double recieptAmount = 0;
    private double paymentAmount = 0;
    private boolean showDueTableFinal = false;
    private boolean correctionUIStatus = false;
    private boolean transferUIStatus = false;
    private boolean depositClosingStatus = false;
    private boolean actClosingStatus = false;
    private boolean hasPenal = false;
    private String transType = "";
    private double penalAmount = 0.0;
    private StringEncrypter encrypt=null;
    private boolean cancelActionKey=false;
    private String approvalUserId = "";
    private String txtRemarks = "";
    
    
    public DataCorrectionApproveUI(DataCorrectionUI objDataCorrectionUI) {
        super(objDataCorrectionUI, false);
        try {
            correctionUIStatus = true;
            initComponents();
            initForm();
            encrypt = new StringEncrypter();
            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public DataCorrectionApproveUI(TransferUI transferUI) {
        super(transferUI, false);
        try {
            transferUIStatus = true;
            initComponents();
            initForm();
            encrypt = new StringEncrypter();
            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     public DataCorrectionApproveUI(DepositClosingUI depositClosingUI) {
        super(depositClosingUI, false);
        try {
            depositClosingStatus = true;
            initComponents();
            initForm();            
            panRejection.setBorder(new javax.swing.border.TitledBorder("Deposit Closing Approval"));
            encrypt = new StringEncrypter();
            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     public DataCorrectionApproveUI(AccountClosingUI actClosingUI) {
        super(actClosingUI, false);
        try {
            actClosingStatus = true;
            initComponents();
            initForm();
            encrypt = new StringEncrypter();
            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /** Account Number Constructor */
    public DataCorrectionApproveUI(HashMap termLoanDataMap, String totalAmount, String transType, boolean showDueTable, boolean penalDepositFlag) {
        initComponents();
//        lblPenalAmount.setVisible(false);
//        lblPenalValue.setVisible(false);
//        lblTotalRecievedValue.setVisible(false);
//        lblTotalRecievedAmount.setVisible(false);
        currDt = ClientUtil.getCurrentDate();
        showDueTableFinal = showDueTable;
        hasPenal = penalDepositFlag;
        this.transType = transType;
        termLoanData = (ArrayList) termLoanDataMap.get("DATA");
        System.out.println("%#$^%#$^%termLoanData:"+termLoanData);
        addToTable(termLoanData,totalAmount);
        branchID = TrueTransactMain.BRANCH_ID;
        setupScreen();
        
        
    }
    
    /** Method which is used to initialize the form TokenConfig */
    private void initForm(){
        setMaxLengths();
        setFieldNames();
        internationalize();
        currDt = ClientUtil.getCurrentDate();
    }
    
    private void setupScreen() {
        setModal(true);
        setTitle("Transaction Details"+"["+branchID+"]");
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("@$#@$#@# screenSize : "+screenSize);
        setSize(570, 280);
        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
    
    private void addToTable(ArrayList termLoanData,String totalAmount){
        System.out.println("$#%#$%#$%#$termLoanData:"+termLoanData);
        String str1 = totalAmount;
        str1 = str1.replaceAll(",","");
        double totalAmt = CommonUtil.convertObjToDouble(str1).doubleValue();
        ArrayList dueList = new ArrayList();
        ArrayList recievedList = new ArrayList();
        ArrayList recievableList = new ArrayList();
        boolean principalDueExists = false;
        for(int k=0;k<termLoanData.size();k++){
            ArrayList balList=((ArrayList)termLoanData.get(k));
            if(balList != null && balList.contains("Total Balance")){
                String str = CommonUtil.convertObjToStr(balList.get(1));
                boolean isDebitBalance = str.indexOf("-")!=-1 ? true : false;
                str = str.replaceAll("-","");
                str = str.replaceAll(",","");
                totalBalance = CommonUtil.convertObjToDouble(str).doubleValue();
                if (isDebitBalance) {
                    totalBalance = -1 * totalBalance;
                }
                System.out.println("%#$%#$%#$%totalBalance :"+totalBalance);
            }
            if(!balList.contains("Limit Amount") && !balList.contains("Available Balance") && !balList.contains("Principal Amount")
            && !balList.contains("Total Balance") && !balList.contains("Last Int Calc Dt") && !balList.contains("Interest Rate") && !balList.contains("Total Recievable")
            && !balList.contains("Unclear Balance") && !balList.contains("Clear Balance") && !balList.contains("Drawing Power Amount") && showDueTableFinal){
                String dueAmount = CommonUtil.convertObjToStr(balList.get(1));
                dueAmount = dueAmount.replaceAll(",", "");
                //                balList.remove(1);
                balList.set(1, dueAmount);
                System.out.println("#$%#$%$#%balList"+balList);
                dueList.add(balList);
            }
            if(balList.contains("Pending Penalty for Delayed Months") && hasPenal){
                String penalAmt = CommonUtil.convertObjToStr(balList.get(1));
                penalAmt = penalAmt.replaceAll(",", "");
                penalAmount = CommonUtil.convertObjToDouble(penalAmt).doubleValue();
                if(penalAmount > 0){
                    hasPenal = true;
//                    lblPenalAmount.setVisible(true);
//                    lblPenalValue.setVisible(true);
//                    lblTotalRecievedValue.setVisible(true);
//                    lblTotalRecievedAmount.setVisible(true);
                }else{
                    hasPenal = false;
//                    lblPenalAmount.setVisible(false);
//                    lblPenalValue.setVisible(false);
//                    lblTotalRecievedValue.setVisible(false);
//                    lblTotalRecievedAmount.setVisible(false);
                }
            }
        }
        termLoanData = new ArrayList();
        termLoanData = dueList;
        System.out.println("@#%#@$%#$%dueList:"+dueList);
        if (dueList != null && dueList.size()==0) {
            displayBalance = totalAmt;
            if(transType.equals("CREDIT")){
                recieptAmount = displayBalance;
            }else{
                paymentAmount = displayBalance;
            }
        }else{
            for(int j=dueList.size()-1; j>=0;j--){
                recievedList = new ArrayList();
                recievedList=((ArrayList)dueList.get(j));
                double dueAmt= 0.0;
                String key = CommonUtil.convertObjToStr(recievedList.get(0));
                String str= CommonUtil.convertObjToStr(recievedList.get(1));
                str = str.replaceAll(",","");
                dueAmt=CommonUtil.convertObjToDouble(str).doubleValue();
                //            dueAmt= CommonUtil.convertObjToDouble(recievedList.get(1)).doubleValue();
                if(dueAmt > 0 && totalAmt>0 ){
                    if(totalAmt >= dueAmt){
                        totalAmt = totalAmt - dueAmt;
                        if(recievedList.size() > 2 && recievedList.get(2)!= null){
                            recievedList.set(2,String.valueOf(dueAmt));
                        }else{
                            recievedList.add(String.valueOf(dueAmt));
                        }
                        if(recievedList.size() > 3 && recievedList.get(3)!= null){
                            recievedList.set(3,String.valueOf(0.0));
                        }else{
                            recievedList.add(String.valueOf(0.0));
                        }
                    }else{
                        if(recievedList.size() > 2 && recievedList.get(2)!= null){
                            recievedList.set(2,String.valueOf(totalAmt));
                        }else{
                            recievedList.add(String.valueOf(totalAmt));
                        }
                        
                        double recievableAmt = dueAmt - totalAmt;
                        totalAmt = totalAmt - dueAmt;
                        if(recievedList.size() > 3 && recievedList.get(3)!= null){
                            recievedList.set(3,String.valueOf(recievableAmt));
                        }else{
                            recievedList.add(String.valueOf(recievableAmt));
                        }
                        
                        //                    totalAmt = 0.0;
                    }
                }else if(dueAmt > 0  && totalAmt <= 0 ){
                    if(recievedList.size() > 2 && recievedList.get(2)!= null){
                        recievedList.set(2,String.valueOf(0.0));
                    }else{
                        recievedList.add(String.valueOf(0.0));
                    }
                    if(recievedList.size() > 3 && recievedList.get(3)!= null){
                        recievedList.set(3,String.valueOf(dueAmt));
                    }else{
                        recievedList.add(String.valueOf(dueAmt));
                    }
                }else if(dueAmt <=0 ){
                    if(recievedList.size() > 2 && recievedList.get(2)!= null){
                        recievedList.set(2,String.valueOf(0.0));
                    }else{
                        recievedList.add(String.valueOf(0.0));
                    }
                    
                    if(recievedList.size() > 3 && recievedList.get(3)!= null){
                        recievedList.set(3,String.valueOf(0.0));
                    }else{
                        recievedList.add(String.valueOf(0.0));
                    }
                    
                }
                if (key.equals("Principal Due")) {
                    if (totalAmt<0) {
                        totalAmt = 0;
                    }
                    displayBalance = CommonUtil.convertObjToDouble(recievedList.get(2)).doubleValue()+totalAmt;
                    recieptAmount = displayBalance;
                    principalDueExists = true;
                }
                recievableList.add(recievedList);
            }
            if (!principalDueExists && totalAmt>0) {
                recieptAmount = totalAmt;
            }
        }
        System.out.println("#$%#$%#$%recievableList:"+recievableList);
        HashMap whereMap = new HashMap();
        dueList = null;
        recievedList = null;
        try {
            populateData(recievableList);
            recievableList = null;
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
    }
    public void show() {
       
            Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            pack();
            
            /* Center frame on the screen */
            Dimension frameSize = getSize();
            if (frameSize.height > screenSize.height)
                frameSize.height = screenSize.height;
            if (frameSize.width > screenSize.width)
                frameSize.width = screenSize.width;
            setLocation((screenSize.width - frameSize.width) / 2,
            (screenSize.height - frameSize.height) / 2);
            setModal(true);
            super.show();
        
    }
    public ArrayList populateData(ArrayList recievedList) {
       
        data = new ArrayList();
        _heading = new ArrayList();
        _heading.add("Name");
        _heading.add("Due");
        _heading.add("Received");
        _heading.add("Receivable");
        
        data = recievedList;
        System.out.println("### Data : "+data);
        if(showDueTableFinal){
//            populateTable();
//            panMembershipTable.setVisible(true);
        }else{
//            panMembershipTable.setVisible(false);
        }
        displayBalance();
        //        whereMap = null;
        recievedList = null;
        return _heading;
    }
    
    public void populateTable() {
        boolean dataExist;
        if (_heading != null){
            _isAvailable = true;
            dataExist = true;
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(_heading);
            tableModel.setData(data);
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            _tblData.setAutoResizeMode(0);
            _tblData.doLayout();
            _tblData.setModel(tableSorter);
            _tblData.revalidate();
            calculateTot();
            
            //            _tblData.getColumnModel().getColumn(0).setPreferredWidth(100);
            //            _tblData.getColumnModel().getColumn(1).setPreferredWidth(110);
            
        }
    }
    public void displayBalance(){
        if(!hasPenal){
//            lblTotalBalValue.setText(CurrencyValidation.formatCrore(String.valueOf(totalBalance)));
//            lblRecieptAmtVal.setText(CurrencyValidation.formatCrore(String.valueOf(recieptAmount)));
//            lblPaymentValue.setText(CurrencyValidation.formatCrore(String.valueOf(paymentAmount)));
//            lblBalanceValue.setText(CurrencyValidation.formatCrore(String.valueOf(totalBalance+recieptAmount-paymentAmount)));
        }else{
//            lblTotalBalValue.setText(CurrencyValidation.formatCrore(String.valueOf(totalBalance)));
//            lblRecieptAmtVal.setText(CurrencyValidation.formatCrore(String.valueOf(recieptAmount-penalAmount)));
//            lblPaymentValue.setText(CurrencyValidation.formatCrore(String.valueOf(paymentAmount)));
//            lblBalanceValue.setText(CurrencyValidation.formatCrore(String.valueOf(totalBalance+recieptAmount-paymentAmount-penalAmount)));
//            lblPenalValue.setText(CurrencyValidation.formatCrore(String.valueOf(penalAmount)));
//            lblTotalRecievedValue.setText(CurrencyValidation.formatCrore(String.valueOf(recieptAmount)));
            System.out.println("#$%#$%#$%penalAmount"+penalAmount);
        }
    }
    public void calculateTot() {
        double totDue = 0.0;
        double totRecieved = 0.0;
        double totRecievable = 0.0;
        for (int i=0; i<_tblData.getRowCount(); i++) {
            totDue = totDue + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 1).toString()).doubleValue();
            totRecieved = totRecieved + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 2).toString()).doubleValue();
            totRecievable = totRecievable + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 3).toString()).doubleValue();
//            lblTotDueAmountVal.setText(CurrencyValidation.formatCrore(String.valueOf(totDue)));
//            lblTotRecievedVal.setText(CurrencyValidation.formatCrore(String.valueOf(totRecieved)));
//            lblTotRecievableVal.setText(CurrencyValidation.formatCrore(String.valueOf(totRecievable)));
            
        }
    }
    
    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths(){
        
    }
    
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        panMemberShipFacility.setName("panMemberShipFacility");
        panRejection.setName("panRejection");
        lblUserId.setName("lblUserId");
        txtUserId.setName("txtUserId");
        btnLogin.setName("btnLogin");
        btnCancel.setName("btnCancel");
        panTotal.setName("panTotal");
        panRejectionUI.setName("panRejectionUI");
    }
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        
    }
    
    public void update(java.util.Observable o, Object arg) {
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
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return null;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panMemberShipFacility = new com.see.truetransact.uicomponent.CPanel();
        panRejectionUI = new com.see.truetransact.uicomponent.CPanel();
        panTotal = new com.see.truetransact.uicomponent.CPanel();
        btnLogin = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        panRejection = new com.see.truetransact.uicomponent.CPanel();
        lblUserId = new com.see.truetransact.uicomponent.CLabel();
        lblRecieptAmt = new com.see.truetransact.uicomponent.CLabel();
        txtUserId = new com.see.truetransact.uicomponent.CTextField();
        txtPassword = new com.see.truetransact.uicomponent.CPasswordField();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaRemarks = new com.see.truetransact.uicomponent.CTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panMemberShipFacility.setMaximumSize(new java.awt.Dimension(400, 200));
        panMemberShipFacility.setMinimumSize(new java.awt.Dimension(400, 200));
        panMemberShipFacility.setPreferredSize(new java.awt.Dimension(400, 200));

        panRejectionUI.setMinimumSize(new java.awt.Dimension(300, 225));
        panRejectionUI.setPreferredSize(new java.awt.Dimension(300, 225));

        panTotal.setMaximumSize(new java.awt.Dimension(450, 25));
        panTotal.setMinimumSize(new java.awt.Dimension(450, 25));
        panTotal.setPreferredSize(new java.awt.Dimension(200, 25));

        btnLogin.setText("Verify");
        btnLogin.setNextFocusableComponent(btnCancel);
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panTotalLayout = new javax.swing.GroupLayout(panTotal);
        panTotal.setLayout(panTotalLayout);
        panTotalLayout.setHorizontalGroup(
            panTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTotalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panTotalLayout.setVerticalGroup(
            panTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panRejection.setBorder(javax.swing.BorderFactory.createTitledBorder("Correction Approval"));
        panRejection.setMaximumSize(new java.awt.Dimension(300, 120));
        panRejection.setMinimumSize(new java.awt.Dimension(300, 120));
        panRejection.setPreferredSize(new java.awt.Dimension(300, 120));

        lblUserId.setText("User Id");

        lblRecieptAmt.setText("Password");

        txtUserId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtUserId.setNextFocusableComponent(txtPassword);

        txtPassword.setNextFocusableComponent(btnLogin);

        cLabel1.setText("Remarks");

        txaRemarks.setColumns(20);
        txaRemarks.setRows(5);
        jScrollPane1.setViewportView(txaRemarks);

        javax.swing.GroupLayout panRejectionLayout = new javax.swing.GroupLayout(panRejection);
        panRejection.setLayout(panRejectionLayout);
        panRejectionLayout.setHorizontalGroup(
            panRejectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRejectionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRejectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panRejectionLayout.createSequentialGroup()
                        .addComponent(lblUserId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtUserId, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panRejectionLayout.createSequentialGroup()
                        .addComponent(lblRecieptAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panRejectionLayout.createSequentialGroup()
                        .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panRejectionLayout.setVerticalGroup(
            panRejectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRejectionLayout.createSequentialGroup()
                .addGroup(panRejectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUserId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUserId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRejectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRecieptAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panRejectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRejectionLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panRejectionLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panRejectionUILayout = new javax.swing.GroupLayout(panRejectionUI);
        panRejectionUI.setLayout(panRejectionUILayout);
        panRejectionUILayout.setHorizontalGroup(
            panRejectionUILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRejectionUILayout.createSequentialGroup()
                .addGroup(panRejectionUILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panRejection, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panRejectionUILayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        panRejectionUILayout.setVerticalGroup(
            panRejectionUILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRejectionUILayout.createSequentialGroup()
                .addComponent(panRejection, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panMemberShipFacilityLayout = new javax.swing.GroupLayout(panMemberShipFacility);
        panMemberShipFacility.setLayout(panMemberShipFacilityLayout);
        panMemberShipFacilityLayout.setHorizontalGroup(
            panMemberShipFacilityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMemberShipFacilityLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(panRejectionUI, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(262, Short.MAX_VALUE))
        );
        panMemberShipFacilityLayout.setVerticalGroup(
            panMemberShipFacilityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMemberShipFacilityLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(panRejectionUI, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(108, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panMemberShipFacility, gridBagConstraints);
        panMemberShipFacility.getAccessibleContext().setAccessibleName("MembershipFacifility");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        if(correctionUIStatus){
        ClientUtil.showMessageWindow("Data Correction cannot proceed without Approval User Id and Password !!!");
        }
        cancelActionKey=true;
        this.dispose();
         
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        // TODO add your handling code here:
        try{
        
            if(txaRemarks.getText().length() < 10){
                ClientUtil.showMessageWindow("Enter remarks which should not be less than 10 characters !!!");
            }
            else if(validUserDetails()){
            return ;
           }
        else{
             cancelActionKey=false;
            this.dispose();
        }
        }catch (Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnLoginActionPerformed
 public boolean validUserDetails()throws Exception{
       HashMap whereMap=new HashMap();
       HashMap userMap = getLoginDetails();
       if(userMap !=null){
           whereMap.put("APPROVE_USER",userMap.get("USER_ID"));
           whereMap.put("CURR_USER",TrueTransactMain.USER_ID);
           whereMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
           whereMap.put("PWD", encrypt.encrypt(CommonUtil.convertObjToStr(userMap.get("PASSWORD"))));
          
           List lst =ClientUtil.executeQuery("getUserDetails", whereMap);
           if(lst !=null && lst.size()>0){
               setApprovalUserId(txtUserId.getText());
               setTxtRemarks(txaRemarks.getText());
               return false;
           }else{
               ClientUtil.showMessageWindow("Please Enter Valid User and Password Details");
               //txtUserId.setText("");
               txtPassword.setText("");
               txtPassword.requestFocus();
               panRejection.setNextFocusableComponent(txtUserId);
               
           }
       }
       
       return true;
 }
 
                                    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
                                                                                                                                                                                }//GEN-LAST:event_formWindowClosed
                                    
                                    
                                                            private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
                                                                                                                                                                                                                                                                                                                                                            }//GEN-LAST:event_formWindowClosing
                                                            
                                                            /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
                                                            }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //        new CheckCustomerIdUI().show();
    }
    public HashMap getLoginDetails(){
        HashMap loginMap =new HashMap();
        String user =CommonUtil.convertObjToStr(txtUserId.getText());
        String pass =CommonUtil.convertObjToStr(txtPassword.getText());
        if(user.length()>0 && pass.length()>0 ){
            loginMap.put("USER_ID",user);
            loginMap.put("PASSWORD",pass);
        }else{
            ClientUtil.showMessageWindow("Please Enter User and Password Details");
            return null;
        }
    return loginMap;
    }
    
    /**
     * Getter for property cancelActionKey.
     * @return Value of property cancelActionKey.
     */
    public boolean isCancelActionKey() {
        return cancelActionKey;
    }
    
    /**
     * Setter for property cancelActionKey.
     * @param cancelActionKey New value of property cancelActionKey.
     */
    public void setCancelActionKey(boolean cancelActionKey) {
        this.cancelActionKey = cancelActionKey;
    }

    public String getApprovalUserId() {
        return approvalUserId;
    }

    public void setApprovalUserId(String approvalUserId) {
        this.approvalUserId = approvalUserId;
    }

    public String getTxtRemarks() {
        return txtRemarks;
    }

    public void setTxtRemarks(String txtRemarks) {
        this.txtRemarks = txtRemarks;
    }

   
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnLogin;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.see.truetransact.uicomponent.CLabel lblRecieptAmt;
    private com.see.truetransact.uicomponent.CLabel lblUserId;
    private com.see.truetransact.uicomponent.CPanel panMemberShipFacility;
    private com.see.truetransact.uicomponent.CPanel panRejection;
    private com.see.truetransact.uicomponent.CPanel panRejectionUI;
    private com.see.truetransact.uicomponent.CPanel panTotal;
    private com.see.truetransact.uicomponent.CTextArea txaRemarks;
    private com.see.truetransact.uicomponent.CPasswordField txtPassword;
    private com.see.truetransact.uicomponent.CTextField txtUserId;
    // End of variables declaration//GEN-END:variables
    
}
