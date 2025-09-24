/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ViewLoansTransUI.java
 *
 * Created on April 13, 2011, 10:10 PM
 */
package com.see.truetransact.ui.common.viewall;

import com.see.truetransact.clientproxy.ProxyParameters;
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
import com.sun.tools.javac.v8.util.Convert;

/**
 *
 * @author  Suresh
 */
public class ViewLoansTransUI extends com.see.truetransact.uicomponent.CDialog implements Observer {

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
    private boolean hasPenal = false;
    private String transType = "";
    private double penalAmount = 0.0;
    private boolean penalWaiveOffFinal = false;
    String Debit_Credit = "";
    double serviceTaxAmt=0;


    public ViewLoansTransUI() {
        initComponents();
        initForm();
    }

    /** Account Number Constructor */
    public ViewLoansTransUI(HashMap termLoanDataMap, String totalAmount, String transType, boolean showDueTable, boolean penalDepositFlag, String linkBatchId,String prodType) {
        initComponents();
        currDt = ClientUtil.getCurrentDate();
        lblPenalAmount.setVisible(false);
        lblPenalValue.setVisible(false);
        lblTotalRecievedValue.setVisible(false);
        lblTotalRecievedAmount.setVisible(false);
        lblInterestUpto.setVisible(false);
        lblInterestUptoValue.setVisible(false);
        showDueTableFinal = showDueTable;
        hasPenal = penalDepositFlag;
        this.transType = transType;
        lblTotRecievedVal.setText("");
        termLoanData = (ArrayList) termLoanDataMap.get("DATA");
        if(termLoanDataMap.containsKey("ACT_NUM")){
             linkBatchId = CommonUtil.convertObjToStr(termLoanDataMap.get("ACT_NUM"));
        }
        txtServiceTaxAmt.setText("0.00");
        System.out.println("%#$^%#$^%termLoanDataMap11111:"+termLoanDataMap);
        if(termLoanDataMap.containsKey("SERVICE_TAX") && termLoanDataMap.get("SERVICE_TAX")!=null){
            if(termLoanDataMap.containsKey("AUTHORIZE_VIEW") && termLoanDataMap.get("AUTHORIZE_VIEW")!=null){
                 HashMap selectMap=new HashMap();
                 selectMap.put("LINK_BATCH_ID",linkBatchId);
                 selectMap.put("TODAY_DT",this.currDt);
                 selectMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                 selectMap.put("INSTRUMENT_NO2","SERVICETAX_CHARGE");
                 List resultList = ClientUtil.executeQuery("getAllTransactionViewAD", selectMap);
                 if(resultList!=null && resultList.size()>0){
                     selectMap = (HashMap) resultList.get(0);
                     this.serviceTaxAmt=CommonUtil.convertObjToDouble(selectMap.get("AMOUNT"));
                 }
            }
            else{
               this.serviceTaxAmt=CommonUtil.convertObjToDouble(termLoanDataMap.get("SERVICE_TAX"));
            }
           double totAmt=CommonUtil.convertObjToDouble(totalAmount);
           if(totAmt<=this.serviceTaxAmt){
               this.serviceTaxAmt=totAmt;
           }
           txtServiceTaxAmt.setText(CommonUtil.convertObjToStr(this.serviceTaxAmt));
        }        
      //  System.out.println("%#$^%#$^%termLoanDataNEWWWWWWW:"+termLoanData);
        addToTable(termLoanData, totalAmount, linkBatchId,prodType);
        branchID = TrueTransactMain.BRANCH_ID;
        setupScreen();

    }

    public ViewLoansTransUI(HashMap termLoanDataMap, String totalAmount, String transType, boolean showDueTable, boolean penalDepositFlag, String linkBatchId, boolean authorize) {
        initComponents();
        currDt = ClientUtil.getCurrentDate();
        lblPenalAmount.setVisible(false);
        lblPenalValue.setVisible(false);
        lblTotalRecievedValue.setVisible(false);
        lblTotalRecievedAmount.setVisible(false);
        showDueTableFinal = showDueTable;
        lblInterestUpto.setVisible(false);
        lblInterestUptoValue.setVisible(false);
        hasPenal = penalDepositFlag;
        this.transType = transType;
        lblTotRecievedVal.setText("");
        termLoanData = (ArrayList) termLoanDataMap.get("DATA");
        System.out.println("%#$^%#$^%termLoanDataMap222222:"+termLoanDataMap);
        if(termLoanDataMap.containsKey("SERVICE_TAX") && termLoanDataMap.get("SERVICE_TAX")!=null){
            if(termLoanDataMap.containsKey("AUTHORIZE_VIEW") && termLoanDataMap.get("AUTHORIZE_VIEW")!=null){
                 HashMap selectMap=new HashMap();
                 selectMap.put("LINK_BATCH_ID",linkBatchId);
                 selectMap.put("TODAY_DT",this.currDt);
                 selectMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                 selectMap.put("INSTRUMENT_NO2","SERVICETAX_CHARGE");
                 List resultList = ClientUtil.executeQuery("getAllTransactionViewAD", selectMap);
                 if(resultList!=null && resultList.size()>0){
                     selectMap = (HashMap) resultList.get(0);
                     this.serviceTaxAmt=CommonUtil.convertObjToDouble(selectMap.get("AMOUNT"));
                 }
            }
            else{
               this.serviceTaxAmt=CommonUtil.convertObjToDouble(termLoanDataMap.get("SERVICE_TAX"));
            }
           double totAmt=CommonUtil.convertObjToDouble(totalAmount);
           if(totAmt<=this.serviceTaxAmt){
               this.serviceTaxAmt=totAmt;
           }
           txtServiceTaxAmt.setText(CommonUtil.convertObjToStr(this.serviceTaxAmt));
        }        
        addToAuthTable(termLoanData, totalAmount, linkBatchId);
        branchID = TrueTransactMain.BRANCH_ID;
        setupScreen();

    }

    public ViewLoansTransUI(HashMap termLoanDataMap, String totalAmount, String transType, boolean showDueTable, boolean penalDepositFlag, boolean penalWaiveOff) {
        initComponents();
        currDt = ClientUtil.getCurrentDate();
        lblPenalAmount.setVisible(false);
        lblPenalValue.setVisible(false);
        lblTotalRecievedValue.setVisible(false);
        lblTotalRecievedAmount.setVisible(false);
        lblInterestUpto.setVisible(false);
        lblInterestUptoValue.setVisible(false);
        showDueTableFinal = showDueTable;
        hasPenal = penalDepositFlag;
        this.transType = transType;
        penalWaiveOffFinal = penalWaiveOff;
        termLoanData = (ArrayList) termLoanDataMap.get("DATA");        
//        System.out.println("%#$^%#$^%termLoanData:"+termLoanData);
        // Added by nithya for KD-3192
        if(termLoanDataMap.containsKey("LOAN_ACTNUM_FROM_TRANSFER_SCREEN") && termLoanDataMap.get("LOAN_ACTNUM_FROM_TRANSFER_SCREEN")!= null){
           addToTable(termLoanData, totalAmount, CommonUtil.convertObjToStr(termLoanDataMap.get("LOAN_ACTNUM_FROM_TRANSFER_SCREEN")),"");  
        }else{
        addToTable(termLoanData, totalAmount, "","");
        }
        branchID = TrueTransactMain.BRANCH_ID;

        setupScreen();

    }

    /** Method which is used to initialize the form TokenConfig */
    private void initForm() {
        setMaxLengths();
        setFieldNames();
        internationalize();
        currDt = ClientUtil.getCurrentDate();
    }

    private void setupScreen() {
        setModal(true);
        setTitle("Transaction Details" + "[" + branchID + "]");
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("@$#@$#@# screenSize : " + screenSize);
        setSize(570, 480);
        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    private void addToAuthTable(ArrayList termLoanData, String totalAmount, String linkBatchId) {
        //System.out.println("$#%#$%#$%#$termLoanData: is here" + termLoanData);
        String str1 = totalAmount;
        str1 = str1.replaceAll(",", "");
        double totalAmt = CommonUtil.convertObjToDouble(str1).doubleValue();
        ArrayList dueList = new ArrayList();
        ArrayList recievedList = new ArrayList();
        ArrayList recievableList = new ArrayList();
        boolean principalDueExists = false;
        for (int k = 0; k < termLoanData.size(); k++) {
            ArrayList balList = ((ArrayList) termLoanData.get(k));
            if (balList != null && balList.contains("Total Balance")) {
                String str = CommonUtil.convertObjToStr(balList.get(1));
                boolean isDebitBalance = false;

                if (str.indexOf("Dr") > 0) {
                    isDebitBalance = str.indexOf("Dr") != -1 ? true : false;
                    if (isDebitBalance) {
                        str = str.replaceAll("Dr", "");
                        Debit_Credit = "Dr";
                    } else {
                        str = str.replaceAll("Cr", "");
                        Debit_Credit = "Cr";
                    }
                    str = str.replaceAll("-", "");
                    str = str.replaceAll(",", "");
                    totalBalance = CommonUtil.convertObjToDouble(str).doubleValue();
                } else {
                    isDebitBalance = str.indexOf("-") != -1 ? true : false;
                    str = str.replaceAll("-", "");
                    str = str.replaceAll(",", "");
                    totalBalance = CommonUtil.convertObjToDouble(str).doubleValue();
                    if (isDebitBalance) {
                        totalBalance = -1 * totalBalance;
                    }
                }
                str = str.replaceAll("-", "");
                str = str.replaceAll(",", "");
            }
            if (!balList.contains("Limit Amount") && !balList.contains("Available Balance") && !balList.contains("Principal Amount")
                    && !balList.contains("Total Balance") && !balList.contains("Last Int Calc Dt") && !balList.contains("Interest Rate") && !balList.contains("Total Recievable")
                    && !balList.contains("Unclear Balance") && !balList.contains("Clear Balance") && !balList.contains("Drawing Power Amount") && !balList.contains("Loan Closing Amount")
                    && !balList.contains("Rebate Paid Interest") && !balList.contains("Available Rebate Interest")
                    && !balList.contains("Total(IntDue+Penal)") && !balList.contains("Total Waived Penal") && !balList.contains("Total Waived Interest") && !balList.contains("Penal Waived Dt")
                    && showDueTableFinal) {
                String dueAmount = CommonUtil.convertObjToStr(balList.get(1));
                dueAmount = dueAmount.replaceAll(",", "");
                //                balList.remove(1);
                balList.set(1, dueAmount);
                System.out.println("#$%#$%$#%balList" + balList);
                dueList.add(balList);
            }
            if (balList.contains("Pending Penalty for Delayed Months") && hasPenal) {
                String penalAmt = CommonUtil.convertObjToStr(balList.get(1));
                penalAmt = penalAmt.replaceAll(",", "");
                penalAmount = CommonUtil.convertObjToDouble(penalAmt).doubleValue();
                if (penalAmount > 0) {
                    hasPenal = true;
                    lblPenalAmount.setVisible(true);
                    lblPenalValue.setVisible(true);
                    lblTotalRecievedValue.setVisible(true);
                    lblTotalRecievedAmount.setVisible(true);
                } else {
                    hasPenal = false;
                    lblPenalAmount.setVisible(false);
                    lblPenalValue.setVisible(false);
                    lblTotalRecievedValue.setVisible(false);
                    lblTotalRecievedAmount.setVisible(false);
                }
            }
        }
        termLoanData = new ArrayList();
        termLoanData = dueList;
        if(this.serviceTaxAmt>0){
            totalAmt=totalAmt-this.serviceTaxAmt;
        }
        if (dueList != null && dueList.size() == 0) {
            displayBalance = totalAmt;
            if (transType.equals("CREDIT")) {
                recieptAmount = displayBalance;
            } else {
                paymentAmount = displayBalance;
            }
        } else {
            for (int j = dueList.size() - 1; j >= 0; j--) {
                recievedList = new ArrayList();
                recievedList = ((ArrayList) dueList.get(j));
                System.out.println("recievedList^$^$^$^$^"+recievedList);
                double dueAmt = 0.0, recvAmt = 0.0, transAmt = 0.0,penalWave= 0.0;
                String authRemark = "";
                String key = CommonUtil.convertObjToStr(recievedList.get(0));
                String str = CommonUtil.convertObjToStr(recievedList.get(1));
                str = str.replaceAll(",", "");
                dueAmt = CommonUtil.convertObjToDouble(str).doubleValue();
                recvAmt = dueAmt;
                //if (key.equals("Interest Due") || key.equals("Penal Amount") || key.equals("Notice Charges") || key.equals("Postage Charges")
                //        || key.equals("Execution Decree Charges") || key.equals("Arbitrary Charges") || !key.equals("") && linkBatchId != null && linkBatchId.length() > 0) {
                if (!key.equals("") && !key.equals("Principal Due") && linkBatchId != null && linkBatchId.length() > 0) {
                    HashMap selectMap = new HashMap();
                    selectMap.put("LINK_BATCH_ID", linkBatchId);
                    selectMap.put("TODAY_DT", currDt.clone());
                    selectMap.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
                    if (key.equals("Interest Due")) {
                        selectMap.put("AUTHORIZE_REMARKS", "INTEREST");
                    } else if (key.equals("Penal Amount") || key.equals("Penal Interest")) {
                        selectMap.put("AUTHORIZE_REMARKS", "PENAL_INT");
                    } else if (key.equals("Notice Charges")) {
                        selectMap.put("AUTHORIZE_REMARKS", "NOTICE CHARGES");
                    } else if (key.equals("Postage Charges")) {
                        selectMap.put("AUTHORIZE_REMARKS", "POSTAGE CHARGES");
                    } else if (key.equals("Execution Decree Charges")) {
                        selectMap.put("AUTHORIZE_REMARKS", "EXECUTION DECREE CHARGES");
                    } else if (key.equals("Arbitrary Charges")) {
                        selectMap.put("AUTHORIZE_REMARKS", "ARBITRARY CHARGES");
                    }else {
                        selectMap.put("AUTHORIZE_REMARKS",getChargeType(key));
                    }
                    if(this.serviceTaxAmt>0){
                        if(key.equals("Miscellaneous Charges")){
                          selectMap.put("NO_SERVICE_TAX","NO_SERVICE_TAX");
                          selectMap.put("INSTRUMENT_NO2","SERVICETAX_CHARGE");
                        }
                    }
                    System.out.println("selectMap$#$#$#$#$#$#$#$$#"+selectMap);
                    List resultList = ClientUtil.executeQuery("getAllTransactionViewDetails", selectMap);
                    if (resultList != null && resultList.size() > 0) {
                        for (int i = 0; i < resultList.size(); i++) {
                            HashMap loanTransMap = (HashMap) resultList.get(i);
                            authRemark = CommonUtil.convertObjToStr(loanTransMap.get("AUTHORIZE_REMARKS"));
                            if (authRemark.equals("INTEREST") || authRemark.equals("PENAL_INT") || authRemark.equals("NOTICE CHARGES")
                                    || authRemark.equals("POSTAGE CHARGES") || authRemark.equals("ARBITRARY CHARGES") || authRemark.equals("EXECUTION DECREE CHARGES") || !authRemark.equals("") && authRemark != null && authRemark.length() > 0) {
                                transAmt = transAmt + CommonUtil.convertObjToDouble(loanTransMap.get("AMOUNT"));
                            }
                        }
                    } else {
                        if (key.equals("Penal Amount")) {
                            selectMap.put("AUTHORIZE_REMARKS", "PENAL_WAIVEOFF");
                            List WavieResultList = ClientUtil.executeQuery("getAllTransactionViewDetails", selectMap);
                            if (WavieResultList != null && WavieResultList.size() > 0) {
                                HashMap loanTransMap = (HashMap) WavieResultList.get(0);
                                authRemark = CommonUtil.convertObjToStr(loanTransMap.get("AUTHORIZE_REMARKS"));
                                if (authRemark.equals("PENAL_WAIVEOFF")  && authRemark != null && authRemark.length() > 0) {
                                    penalWave = CommonUtil.convertObjToDouble(loanTransMap.get("AMOUNT"));
                                    recievedList.add(String.valueOf(penalWave));
                                    recievedList.add(String.valueOf(CommonUtil.convertObjToDouble(str)-penalWave));
                                }else{
                                    recievedList.add(String.valueOf(0.0));
                                    recievedList.add(String.valueOf(CommonUtil.convertObjToDouble(str)));
                                }
                            }else{
                                    recievedList.add(String.valueOf(0.0));
                                    recievedList.add(String.valueOf(CommonUtil.convertObjToDouble(str)));
                                }
                        }else{
                            recievedList.add(String.valueOf(0.0));
                            recievedList.add(String.valueOf(CommonUtil.convertObjToDouble(str)));
                        }

                    }
//                    if (key.equals("Interest Due") && authRemark.equals("INTEREST")) {
//                        System.out.println("totalAmt########## Interest Due"+totalAmt);
//                        totalAmt = totalAmt - transAmt;
//                        System.out.println("totalAmt########## Interest Due2222"+totalAmt);
//                        recievedList.add(String.valueOf(transAmt));
//                        recievedList.add(String.valueOf(CommonUtil.convertObjToDouble(str) - transAmt));
//                    } else if (key.equals("Penal Amount") && authRemark.equals("PENAL_INT")) {
//                        totalAmt = totalAmt - transAmt;
//                        recievedList.add(String.valueOf(transAmt));
//                        recievedList.add(String.valueOf(CommonUtil.convertObjToDouble(str) - transAmt));
//                    } else if (key.equals("Notice Charges") && authRemark.equals("NOTICE CHARGES")) {
//                        totalAmt = totalAmt - transAmt;
//                        recievedList.add(String.valueOf(transAmt));
//                        recievedList.add(String.valueOf(CommonUtil.convertObjToDouble(str) - transAmt));
//                    } else if (key.equals("Postage Charges") && authRemark.equals("POSTAGE CHARGES")) {
//                        totalAmt = totalAmt - transAmt;
//                        recievedList.add(String.valueOf(transAmt));
//                        recievedList.add(String.valueOf(CommonUtil.convertObjToDouble(str) - transAmt));
//                    } else if (key.equals("Execution Decree Charges") && authRemark.equals("EXECUTION DECREE CHARGES")) {
//                        totalAmt = totalAmt - transAmt;
//                        recievedList.add(String.valueOf(transAmt));
//                        recievedList.add(String.valueOf(CommonUtil.convertObjToDouble(str) - transAmt));
//                    } else if (key.equals("Arbitrary Charges") && authRemark.equals("ARBITRARY CHARGES")) {
//                        totalAmt = totalAmt - transAmt;
//                        recievedList.add(String.valueOf(transAmt));
//                        recievedList.add(String.valueOf(CommonUtil.convertObjToDouble(str) - transAmt));
//                    }
                    if(authRemark != null && authRemark.length() > 0){
                         totalAmt = totalAmt - transAmt;
                         recievedList.add(String.valueOf(transAmt));
                         recievedList.add(String.valueOf(CommonUtil.convertObjToDouble(str) - transAmt));
                    }else {
                        //totalAmt = totalAmt - transAmt;
                        recievedList.add(String.valueOf(0.0));
                        recievedList.add(String.valueOf(CommonUtil.convertObjToDouble(str)));
                    }                    
                    System.out.println("totalAmt##########"+totalAmt);
                } else if (key.equals("Principal Due")) {
                    if (totalAmt < 0) {
                        totalAmt = 0;
                    }
                    recievedList.add(String.valueOf(totalAmt));
                    if((CommonUtil.convertObjToDouble(str) - totalAmt)>0){
                        recievedList.add(String.valueOf(CommonUtil.convertObjToDouble(str) - totalAmt));    
                    }else{
                        recievedList.add(String.valueOf(0.0));
                    }                    
                    displayBalance = totalAmt;
                    recieptAmount = displayBalance;
                 
                    principalDueExists = true;
                }               
                System.out.println("recieptAmount%%%%%"+recieptAmount);
                recievableList.add(recievedList);
            }
            if (!principalDueExists && totalAmt > 0) {
                recieptAmount = totalAmt;
            }
            try {
                System.out.println(" recievableList " + recievableList + " tblMemberShipLiabilityList " + tblMemberShipLiabilityList);
                populateData(recievableList, tblMemberShipLiabilityList);
                recievableList = null;
             } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }

        }
    }
    
    
    public String getChargeType(String chargeDesc){
        HashMap chargeMap = new HashMap();
        String chargeType = "";
        chargeMap.put("CHARGETYPE", chargeDesc);
        List chargeList = ClientUtil.executeQuery("getTermLoanChargeType", chargeMap);
        if (chargeList != null && chargeList.size() > 0) {
            chargeMap = (HashMap) chargeList.get(0);
            chargeType = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_TYPE"));       
        }
        System.out.println("CHARGE_TYPE %$%$%$%$%$%$"+chargeType);
        return chargeType;                
    }

    private void addToTable(ArrayList termLoanData, String totalAmount, String linkBatchId,String prodType) {
      //  System.out.println("$#%#$%#$%#$termLoanData: is there--->" + termLoanData);
        String str1 = totalAmount;
        str1 = str1.replaceAll(",", "");
        double totalAmt = CommonUtil.convertObjToDouble(str1).doubleValue();
        ArrayList dueList = new ArrayList();
        ArrayList recievedList = new ArrayList();
        ArrayList recievableList = new ArrayList();
        boolean principalDueExists = false;
        String clearBalAB = "";
        String totBal = "";
        for (int k = 0; k < termLoanData.size(); k++) {
            ArrayList balList = ((ArrayList) termLoanData.get(k));
            String str = "";
            if (prodType.equals("AB") && balList.contains("Available Balance")) {
                str = CommonUtil.convertObjToStr(balList.get(1));
                clearBalAB = str;
            } else if (!prodType.equals("AB") && balList.contains("Total Balance")) {
                str = CommonUtil.convertObjToStr(balList.get(1));
                totBal = str;
            }
            boolean isDebitBalance = false;
            if (str.indexOf("Dr") > 0) {
                isDebitBalance = str.indexOf("Dr") != -1 ? true : false;
                if (prodType.equals("AB")&& balList.contains("Available Balance")) {
                    str = clearBalAB;
                    str = str.replaceAll("Dr", "");
                    totalBalance = CommonUtil.convertObjToDouble(str).doubleValue();
                } else if (!prodType.equals("AB") && balList.contains("Total Balance")) {
                    str = totBal;
                    str = str.replaceAll("Dr", "");
                    totalBalance = CommonUtil.convertObjToDouble(str).doubleValue();
                }
                if (isDebitBalance) {
                    str = str.replaceAll("Dr", "");
                    Debit_Credit = "Dr";
                } else {
                    str = str.replaceAll("Cr", "");
                    Debit_Credit = "Cr";
                }
                str = str.replaceAll("-", "");
                str = str.replaceAll(",", "");
                totalBalance = CommonUtil.convertObjToDouble(str).doubleValue();
             } else {
                if (prodType.equals("AB")&& balList.contains("Available Balance")) {
                    str = clearBalAB;
                    str = str.replaceAll("-", "");
                    str = str.replaceAll(",", "");
                    totalBalance = CommonUtil.convertObjToDouble(str).doubleValue();
                } else if (!prodType.equals("AB") && balList.contains("Total Balance"))  {
                    str = totBal;
                    str = str.replaceAll("-", "");
                    str = str.replaceAll(",", "");
                    totalBalance = CommonUtil.convertObjToDouble(str).doubleValue();
                }
                isDebitBalance = str.indexOf("-") != -1 ? true : false;
                str = str.replaceAll("-", "");
                str = str.replaceAll(",", "");
                if (isDebitBalance) {
                    totalBalance = -1 * totalBalance;
                }
            }
            if (prodType.equals("AD") && balList != null && balList.contains("Total Balance")) {
                str = CommonUtil.convertObjToStr(balList.get(1));
                                          
                isDebitBalance = str.indexOf("Dr") != -1 ? true : false;
                if (isDebitBalance) {
                    str = str.replaceAll("Dr", "");
                    str = str.replaceAll("-", "");
                    str = str.replaceAll(",", "");
                    totalBalance = CommonUtil.convertObjToDouble(str).doubleValue();
                    totalBalance = -1 * totalBalance;
                } else {
                    str = str.replaceAll("Cr", "");
                    str = str.replaceAll("-", "");
                    str = str.replaceAll(",", "");
                    totalBalance = CommonUtil.convertObjToDouble(str).doubleValue();
                }
                if (isDebitBalance) {
                    totalBalance = -1 * totalBalance;
                }         
                System.out.println("%#$%#$%#$%totalBalance :" + totalBalance);
            }
            // Changes for suspense product : by nithya for 0009297 on 26-02-2018
//            Cash/TRansfer screen while doing the transaction of Suspense A/C, the view button balance is wrong.
//            Here the Balance of a suspense a/C is -2000 . when we try to receipt a Rs 1000/- 
//            then the Balance is shown in view button as Rs 3000/- actual balance is Rs 1000
            if (prodType.equals("SA") && balList != null && balList.contains("Total Balance")) {
                str = CommonUtil.convertObjToStr(balList.get(1));
                                          
                isDebitBalance = str.indexOf("-") != -1 ? true : false;
                if (isDebitBalance) {                    
                    str = str.replaceAll("-", "");
                    str = str.replaceAll(",", "");
                    totalBalance = CommonUtil.convertObjToDouble(str).doubleValue();
                    totalBalance = -1 * totalBalance;
                } else {                    
                    str = str.replaceAll("-", "");
                    str = str.replaceAll(",", "");
                    totalBalance = CommonUtil.convertObjToDouble(str).doubleValue();
                }
                if (isDebitBalance) {
                    totalBalance = -1 * totalBalance;
                    Debit_Credit = "Dr";
                }else{
                    Debit_Credit = "Cr"; 
                }         
                System.out.println("%#$%#$%#$%totalBalance :" + totalBalance);
            }
            // End
            str = str.replaceAll("-", "");
            str = str.replaceAll(",", "");

            if (!balList.contains("Limit Amount") && !balList.contains("Available Balance") && !balList.contains("Principal Amount")
                    && !balList.contains("Total Balance") && !balList.contains("Last Int Calc Dt") && !balList.contains("Interest Rate") && !balList.contains("Total Recievable")
                    && !balList.contains("Unclear Balance") && !balList.contains("Clear Balance") && !balList.contains("Drawing Power Amount") && !balList.contains("Loan Closing Amount")
                    && !balList.contains("Rebate Paid Interest") && !balList.contains("Available Rebate Interest")
                    && !balList.contains("Total(IntDue+Penal)") && !balList.contains("Total Waived Penal") && !balList.contains("Total Waived Interest") && !balList.contains("Penal Waived Dt")
                    && showDueTableFinal) {
                String dueAmount = CommonUtil.convertObjToStr(balList.get(1));
                dueAmount = dueAmount.replaceAll(",", "");
                //                balList.remove(1);
                balList.set(1, dueAmount);
                System.out.println("#$%#$%$#%balList" + balList);
                dueList.add(balList);
            }
            if (balList.contains("Pending Penalty for Delayed Months") && hasPenal) {
                String penalAmt = CommonUtil.convertObjToStr(balList.get(1));
                penalAmt = penalAmt.replaceAll(",", "");
                penalAmount = CommonUtil.convertObjToDouble(penalAmt).doubleValue();
                if (penalAmount > 0) {
                    hasPenal = true;
                    lblPenalAmount.setVisible(true);
                    lblPenalValue.setVisible(true);
                    lblTotalRecievedValue.setVisible(true);
                    lblTotalRecievedAmount.setVisible(true);
                } else {
                    hasPenal = false;
                    lblPenalAmount.setVisible(false);
                    lblPenalValue.setVisible(false);
                    lblTotalRecievedValue.setVisible(false);
                    lblTotalRecievedAmount.setVisible(false);
                }
            }                    
        }
        termLoanData = new ArrayList();
        termLoanData = dueList;
        if(prodType!=null && (prodType.equals("AD") && this.transType.equals(CommonConstants.DEBIT))){
            dueList=new ArrayList();
        }
        if(this.serviceTaxAmt>0){
            totalAmt=totalAmt-this.serviceTaxAmt;
        }
        if (dueList != null && dueList.size() == 0) {
            displayBalance = totalAmt;
            if (transType.equals("CREDIT")) {
                recieptAmount = displayBalance;
            } else {
                paymentAmount = displayBalance;
            }
        } else {
            for (int j = dueList.size() - 1; j >= 0; j--) {
                recievedList = new ArrayList();
                recievedList = ((ArrayList) dueList.get(j));
                double dueAmt = 0.0, recvAmt = 0.0, transAmt = 0.0;
                boolean transFlag = false;
                String key = CommonUtil.convertObjToStr(recievedList.get(0));
                String str = CommonUtil.convertObjToStr(recievedList.get(1));
                str = str.replaceAll(",", "");
                dueAmt = CommonUtil.convertObjToDouble(str).doubleValue();
                recvAmt = dueAmt;
                if (key.equals("Interest Due") || key.equals("Penal Amount") || key.equals("Notice Charges") || key.equals("Postage Charges")
                        || key.equals("Execution Decree Charges") || key.equals("Arbitrary Charges") && linkBatchId != null && linkBatchId.length() > 0) {
                    HashMap selectMap = new HashMap();
                    selectMap.put("LINK_BATCH_ID", linkBatchId);
                    selectMap.put("TODAY_DT", currDt.clone());
                    selectMap.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
                    if (key.equals("Interest Due")) {
                        selectMap.put("AUTHORIZE_REMARKS", "INTEREST");
                    } else if (key.equals("Penal Amount")) {
                        selectMap.put("AUTHORIZE_REMARKS", "PENAL_INT");
                    } else if (key.equals("Notice Charges")) {
                        selectMap.put("AUTHORIZE_REMARKS", "NOTICE CHARGES");
                    } else if (key.equals("Postage Charges")) {
                        selectMap.put("AUTHORIZE_REMARKS", "POSTAGE CHARGES");
                    } else if (key.equals("Execution Decree Charges")) {
                        selectMap.put("AUTHORIZE_REMARKS", "EXECUTION DECREE CHARGES");
                    } else if (key.equals("Arbitrary Charges")) {
                        selectMap.put("AUTHORIZE_REMARKS", "ARBITRARY CHARGES");
                    }
                    List resultList = ClientUtil.executeQuery("getAllTransactionViewAD", selectMap);
                    for (int i = 0; i < resultList.size(); i++) {
                        HashMap loanTransMap = (HashMap) resultList.get(i);
                        String authRemark = "";
                        authRemark = CommonUtil.convertObjToStr(loanTransMap.get("AUTHORIZE_REMARKS"));
                        if (authRemark.equals("INTEREST") || authRemark.equals("PENAL_INT") || authRemark.equals("NOTICE CHARGES")
                                || authRemark.equals("POSTAGE CHARGES") || authRemark.equals("ARBITRARY CHARGES") || authRemark.equals("EXECUTION DECREE CHARGES") && authRemark != null && authRemark.length() > 0) {
                            dueAmt = CommonUtil.convertObjToDouble(loanTransMap.get("AMOUNT"));
                        }
                    }
                }
                if (dueAmt > 0 && totalAmt > 0) {
                    if (totalAmt >= dueAmt) {
                        double recievableAmt = 0; //added by shihad
                        if (penalWaiveOffFinal) {
                            if (!recievedList.get(0).equals("Penal Amount")) {
                                totalAmt = totalAmt - dueAmt;
                            }
                        } else {
                            totalAmt = totalAmt - dueAmt;
                        }
                       if (recievedList.size() > 2 && recievedList.get(2) != null) {
                            recievedList.set(2, String.valueOf(dueAmt));
                        } else {
                            recievedList.add(String.valueOf(dueAmt));
                        }
                        recievableAmt = recvAmt - dueAmt; //added by shihad
                        if (recievedList.size() > 3 && recievedList.get(3) != null) {
//                            recievedList.set(3,String.valueOf(0.0));  
                            recievedList.set(3, String.valueOf(recievableAmt));
                        } else {
//                            recievedList.add(String.valueOf(0.0));
                            recievedList.add(String.valueOf(recievableAmt));
                        }
                    } else {
                        double recievableAmt = 0;
                        if (penalWaiveOffFinal) {
                            if (!recievedList.get(0).equals("Penal Amount")) {
                                if (recievedList.size() > 2 && recievedList.get(2) != null) {
                                    recievedList.set(2, String.valueOf(totalAmt));
                                } else {
                                    recievedList.add(String.valueOf(totalAmt));
                                }
                                recievableAmt = dueAmt - totalAmt;
                                totalAmt = totalAmt - dueAmt;
                            } else {
                                if (recievedList.size() > 2 && recievedList.get(2) != null) {
                                    recievedList.set(2, String.valueOf(dueAmt));
                                } else {
                                    recievedList.add(String.valueOf(dueAmt));
                                }
                                recievableAmt = 0;
                            }
                        } else {
                            if (recievedList.size() > 2 && recievedList.get(2) != null) {
                                recievedList.set(2, String.valueOf(totalAmt));
                            } else {
                                recievedList.add(String.valueOf(totalAmt));
                            }
                            recievableAmt = dueAmt - totalAmt;
                            totalAmt = totalAmt - dueAmt;
                        }

                        if (recievedList.size() > 3 && recievedList.get(3) != null) {
                            recievedList.set(3, String.valueOf(recievableAmt));
                        } else {
                            recievedList.add(String.valueOf(recievableAmt));
                        }
                    }
                } else if (dueAmt > 0 && totalAmt <= 0) {
                    if (recievedList.size() > 2 && recievedList.get(2) != null) {
                        recievedList.set(2, String.valueOf(0.0));
                    } else {
                        recievedList.add(String.valueOf(0.0));
                    }
                    if (recievedList.size() > 3 && recievedList.get(3) != null) {
                        recievedList.set(3, String.valueOf(dueAmt));
                    } else {
                        recievedList.add(String.valueOf(dueAmt));
                    }
                } else if (dueAmt <= 0) {
                    if (recievedList.size() > 2 && recievedList.get(2) != null) {
                        recievedList.set(2, String.valueOf(0.0));
                    } else {
                        recievedList.add(String.valueOf(0.0));
                    }

                    if (recievedList.size() > 3 && recievedList.get(3) != null) {
                        recievedList.set(3, String.valueOf(0.0));
                    } else {
                        recievedList.add(String.valueOf(0.0));
                    }
                }
                if (key.equals("Principal Due")) {
                    if (totalAmt < 0) {
                        totalAmt = 0;
                    }
                    displayBalance = CommonUtil.convertObjToDouble(recievedList.get(2)).doubleValue() + totalAmt;
                    //Babu added for the mantis id 10371
					//recieptAmount = displayBalance;
                    if((prodType!=null && prodType.equals("AD")) && this.transType.equals(CommonConstants.DEBIT)){
                       paymentAmount = displayBalance;
                    }else{
                       recieptAmount = displayBalance;
                    }
                    principalDueExists = true;
                    if(prodType.equals("AD") && totalAmt > 0){
                        recievedList.set(2, String.valueOf(totalAmt));
                    }
                }
                //Added by sreekrishnan for interestPaidUpto
                if (key.equals("Interest Due")) {
                    HashMap interestMap = new HashMap();
                    interestMap.put("ACCOUNTNO", linkBatchId);
                    interestMap.put("ASON_DT",currDt.clone());
                    double intamt = 0.0;
                    if(recievedList!=null && recievedList.get(2)!=null){
                        String intAmount = CommonUtil.convertObjToStr(recievedList.get(2));
                        intAmount = intAmount.replaceAll(",", "");
                        intamt = CommonUtil.convertObjToDouble(intAmount).doubleValue();
                    }else{
                        String intAmount = CommonUtil.convertObjToStr(recievedList.get(1));
                        intAmount = intAmount.replaceAll(",", "");
                        intamt = CommonUtil.convertObjToDouble(intAmount).doubleValue();
                    }
                    interestMap.put("TRANS_AMOUNT",intamt);
                    List interestList = ClientUtil.executeQuery("getInterestPaidUpToDate", interestMap);
                    if (interestList != null && interestList.size() > 0) {
                        interestMap = (HashMap) interestList.get(0);
                        System.out.println("interestMap### for InterestPaidUpToDate" + interestMap); 
                        lblInterestUpto.setVisible(true);
                        lblInterestUptoValue.setVisible(true);
                        lblInterestUptoValue.setText(CommonUtil.convertObjToStr(interestMap.get("INTEREST_UPTO")));                                           
                    }
                }
                recievableList.add(recievedList);
            }
            if (!principalDueExists && totalAmt > 0) {
                if((prodType!=null && prodType.equals("AD")) && this.transType.equals(CommonConstants.DEBIT)){
                     paymentAmount = totalAmt;
                }else{
                     recieptAmount = totalAmt;
                }
            }
        }
        HashMap whereMap = new HashMap();
        dueList = null;
        recievedList = null;
        try {
            populateData(recievableList, tblMemberShipLiabilityList);
            recievableList = null;
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
    }

   
    public ArrayList populateData(ArrayList recievedList, CTable tblData) {
        _tblData = tblData;
        System.out.println(" _tblData " + _tblData);
        data = new ArrayList();
        _heading = new ArrayList();
        _heading.add("Name");
        _heading.add("Due");
        _heading.add("Received");
        _heading.add("Receivable");

        data = recievedList;
        System.out.println("### Data : " + data +"showDueTableFinal-->"+showDueTableFinal);
        if (showDueTableFinal) {
            populateTable();
            panMembershipTable.setVisible(true);
        } else {
            panMembershipTable.setVisible(false);
        }
        displayBalance();
        //        whereMap = null;
        recievedList = null;
        return _heading;
    }

    public void populateTable() {
        boolean dataExist;
        if (_heading != null) {
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

    public void displayBalance() {
        System.out.println(" hasPenal " + hasPenal);
        if (!hasPenal) {
            String balanceText = "";
            double balanceAmt = 0;
            System.out.println(" totalBalance " + totalBalance +"recieptAmount -->"+recieptAmount);
            lblTotalBalValue.setText(CurrencyValidation.formatCrore(String.valueOf(totalBalance)));
            lblRecieptAmtVal.setText(CurrencyValidation.formatCrore(String.valueOf(recieptAmount)));
            lblPaymentValue.setText(CurrencyValidation.formatCrore(String.valueOf(paymentAmount)));
            if (Debit_Credit.equals("Dr")) {
                balanceAmt = totalBalance - recieptAmount + paymentAmount;
            } else if (Debit_Credit.equals("Cr")) {
                balanceAmt = totalBalance + recieptAmount - paymentAmount;
            } else {
                balanceAmt = totalBalance + recieptAmount - paymentAmount;
            }
            System.out.println(" Debit_Credit " + Debit_Credit);
            balanceText = balanceAmt < 0 ? String.valueOf(Math.abs(balanceAmt)) + (Debit_Credit.equals("Dr") ? " Cr." : " Dr.") : String.valueOf(balanceAmt);
//            balanceText = CurrencyValidation.formatCrore(balanceText);
            lblBalanceValue.setText(balanceText);
        } else {
            lblTotalBalValue.setText(CurrencyValidation.formatCrore(String.valueOf(totalBalance)));
            lblRecieptAmtVal.setText(CurrencyValidation.formatCrore(String.valueOf(recieptAmount - penalAmount)));
            lblPaymentValue.setText(CurrencyValidation.formatCrore(String.valueOf(paymentAmount)));

            if (Debit_Credit.equals("Dr")) {
                lblBalanceValue.setText(CurrencyValidation.formatCrore(String.valueOf(totalBalance - recieptAmount - paymentAmount - penalAmount)));

            } else if (Debit_Credit.equals("Cr")) {
                lblBalanceValue.setText(CurrencyValidation.formatCrore(String.valueOf(totalBalance + recieptAmount - paymentAmount - penalAmount)));
            } else {
                lblBalanceValue.setText(CurrencyValidation.formatCrore(String.valueOf(totalBalance + recieptAmount - paymentAmount - penalAmount)));
            }
            lblPenalValue.setText(CurrencyValidation.formatCrore(String.valueOf(penalAmount)));
            lblTotalRecievedValue.setText(CurrencyValidation.formatCrore(String.valueOf(recieptAmount)));
            System.out.println("#$%#$%#$%penalAmount" + penalAmount);
        }
    }

    public void calculateTot() {
        double totDue = 0.0;
        double totRecieved = 0.0;
        double totRecievable = 0.0;
        for (int i = 0; i < _tblData.getRowCount(); i++) {
            totDue = totDue + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 1).toString()).doubleValue();
            totRecieved = totRecieved + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 2).toString()).doubleValue();
            totRecievable = totRecievable + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 3).toString()).doubleValue();
            lblTotDueAmountVal.setText(CurrencyValidation.formatCrore(String.valueOf(totDue)));
          //  lblTotRecievedVal.setText(CurrencyValidation.formatCrore(String.valueOf(totRecieved)));
          //  lblTotRecievableVal.setText(CurrencyValidation.formatCrore(String.valueOf(totRecievable)));

        }
        lblTotRecievedVal.setText(CurrencyValidation.formatCrore(String.valueOf(totRecieved+this.serviceTaxAmt)));
        lblTotRecievableVal.setText(CurrencyValidation.formatCrore(String.valueOf(totRecievable)));
    }

    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths() {
    }

    /* Auto Generated Method - setFieldNames()
    This method assigns name for all the components.
    Other functions are working based on this name. */
    private void setFieldNames() {
        panMemberShipFacility.setName("panMemberShipFacility");
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
        panMembershipTable = new com.see.truetransact.uicomponent.CPanel();
        srpMemberShipCTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblMemberShipLiabilityList = new com.see.truetransact.uicomponent.CTable();
        panTotal = new com.see.truetransact.uicomponent.CPanel();
        lblTotLimitAmount = new com.see.truetransact.uicomponent.CLabel();
        lblTotDueAmountVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotRecievableVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotRecievedVal = new com.see.truetransact.uicomponent.CLabel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtServiceTaxAmt = new com.see.truetransact.uicomponent.CTextField();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panTotal1 = new com.see.truetransact.uicomponent.CPanel();
        lblRecieptAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotalBalance = new com.see.truetransact.uicomponent.CLabel();
        lblTotalBalValue = new com.see.truetransact.uicomponent.CLabel();
        lblRecieptAmt = new com.see.truetransact.uicomponent.CLabel();
        lblBalanceValue = new com.see.truetransact.uicomponent.CLabel();
        lblPaymentBal = new com.see.truetransact.uicomponent.CLabel();
        lblPaymentValue = new com.see.truetransact.uicomponent.CLabel();
        lblBalance = new com.see.truetransact.uicomponent.CLabel();
        lblPenalValue = new com.see.truetransact.uicomponent.CLabel();
        lblPenalAmount = new com.see.truetransact.uicomponent.CLabel();
        lblTotalRecievedAmount = new com.see.truetransact.uicomponent.CLabel();
        lblTotalRecievedValue = new com.see.truetransact.uicomponent.CLabel();
        lblInterestUpto = new com.see.truetransact.uicomponent.CLabel();
        lblInterestUptoValue = new com.see.truetransact.uicomponent.CLabel();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        panMemberShipFacility.setToolTipText("");
        panMemberShipFacility.setMaximumSize(new java.awt.Dimension(400, 200));
        panMemberShipFacility.setMinimumSize(new java.awt.Dimension(400, 200));
        panMemberShipFacility.setPreferredSize(new java.awt.Dimension(400, 200));
        panMemberShipFacility.setLayout(new java.awt.GridBagLayout());

        panMembershipTable.setMinimumSize(new java.awt.Dimension(300, 250));
        panMembershipTable.setPreferredSize(new java.awt.Dimension(300, 250));
        panMembershipTable.setLayout(new java.awt.GridBagLayout());

        srpMemberShipCTable.setMinimumSize(new java.awt.Dimension(450, 200));
        srpMemberShipCTable.setPreferredSize(new java.awt.Dimension(450, 200));

        tblMemberShipLiabilityList.setBackground(new java.awt.Color(212, 208, 200));
        tblMemberShipLiabilityList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Due", "Recieved", "Recievable"
            }
        ));
        tblMemberShipLiabilityList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblMemberShipLiabilityList.setDragEnabled(true);
        tblMemberShipLiabilityList.setInheritsPopupMenu(true);
        tblMemberShipLiabilityList.setMaximumSize(new java.awt.Dimension(1000, 1000));
        tblMemberShipLiabilityList.setMinimumSize(new java.awt.Dimension(450, 200));
        tblMemberShipLiabilityList.setPreferredScrollableViewportSize(new java.awt.Dimension(100, 500));
        tblMemberShipLiabilityList.setPreferredSize(new java.awt.Dimension(450, 200));
        tblMemberShipLiabilityList.setRequestFocusEnabled(false);
        srpMemberShipCTable.setViewportView(tblMemberShipLiabilityList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panMembershipTable.add(srpMemberShipCTable, gridBagConstraints);

        panTotal.setMaximumSize(new java.awt.Dimension(450, 50));
        panTotal.setMinimumSize(new java.awt.Dimension(450, 50));
        panTotal.setPreferredSize(new java.awt.Dimension(450, 50));
        panTotal.setLayout(new java.awt.GridBagLayout());

        lblTotLimitAmount.setText("Total : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 0);
        panTotal.add(lblTotLimitAmount, gridBagConstraints);

        lblTotDueAmountVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblTotDueAmountVal.setMaximumSize(new java.awt.Dimension(100, 20));
        lblTotDueAmountVal.setMinimumSize(new java.awt.Dimension(100, 20));
        lblTotDueAmountVal.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panTotal.add(lblTotDueAmountVal, gridBagConstraints);

        lblTotRecievableVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblTotRecievableVal.setMinimumSize(new java.awt.Dimension(100, 20));
        lblTotRecievableVal.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 37);
        panTotal.add(lblTotRecievableVal, gridBagConstraints);

        lblTotRecievedVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblTotRecievedVal.setMaximumSize(new java.awt.Dimension(100, 20));
        lblTotRecievedVal.setMinimumSize(new java.awt.Dimension(100, 20));
        lblTotRecievedVal.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        panTotal.add(lblTotRecievedVal, gridBagConstraints);

        cLabel1.setText("Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 0);
        panTotal.add(cLabel1, gridBagConstraints);

        txtServiceTaxAmt.setAllowAll(true);
        txtServiceTaxAmt.setEnabled(false);
        txtServiceTaxAmt.setFocusable(false);
        txtServiceTaxAmt.setMaximumSize(new java.awt.Dimension(5, 20));
        txtServiceTaxAmt.setMinimumSize(new java.awt.Dimension(5, 20));
        txtServiceTaxAmt.setPreferredSize(new java.awt.Dimension(5, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 9, 0);
        panTotal.add(txtServiceTaxAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 42, 0, 42);
        panMembershipTable.add(panTotal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        panMemberShipFacility.add(panMembershipTable, gridBagConstraints);

        btnClose.setForeground(new java.awt.Color(204, 0, 0));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        panMemberShipFacility.add(btnClose, gridBagConstraints);

        panTotal1.setMaximumSize(new java.awt.Dimension(300, 95));
        panTotal1.setMinimumSize(new java.awt.Dimension(300, 60));
        panTotal1.setPreferredSize(new java.awt.Dimension(300, 100));
        panTotal1.setLayout(new java.awt.GridBagLayout());

        lblRecieptAmtVal.setText("                    ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 40);
        panTotal1.add(lblRecieptAmtVal, gridBagConstraints);

        lblTotalBalance.setText("Total Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(lblTotalBalance, gridBagConstraints);

        lblTotalBalValue.setText("                     ");
        panTotal1.add(lblTotalBalValue, new java.awt.GridBagConstraints());

        lblRecieptAmt.setText("Reciept Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(lblRecieptAmt, gridBagConstraints);

        lblBalanceValue.setText("                    ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(lblBalanceValue, gridBagConstraints);

        lblPaymentBal.setText("Payment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(lblPaymentBal, gridBagConstraints);

        lblPaymentValue.setText("                    ");
        panTotal1.add(lblPaymentValue, new java.awt.GridBagConstraints());

        lblBalance.setForeground(new java.awt.Color(51, 51, 51));
        lblBalance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBalance.setText("Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(lblBalance, gridBagConstraints);

        lblPenalValue.setText("                    ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 40);
        panTotal1.add(lblPenalValue, gridBagConstraints);

        lblPenalAmount.setText("Penal Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(lblPenalAmount, gridBagConstraints);

        lblTotalRecievedAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalRecievedAmount.setText("Total Amt Recieved");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(lblTotalRecievedAmount, gridBagConstraints);

        lblTotalRecievedValue.setText("                    ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(lblTotalRecievedValue, gridBagConstraints);

        lblInterestUpto.setText("Interest Paid UpTo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(lblInterestUpto, gridBagConstraints);

        lblInterestUptoValue.setMaximumSize(new java.awt.Dimension(84, 18));
        lblInterestUptoValue.setMinimumSize(new java.awt.Dimension(84, 18));
        lblInterestUptoValue.setPreferredSize(new java.awt.Dimension(84, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        panTotal1.add(lblInterestUptoValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(20, 38, 3, 41);
        panMemberShipFacility.add(panTotal1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panMemberShipFacility, gridBagConstraints);
        panMemberShipFacility.getAccessibleContext().setAccessibleName("MembershipFacifility");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        lblTotDueAmountVal.setText("");
        lblTotRecievedVal.setText("");
        lblTotRecievableVal.setText("");
        tblMemberShipLiabilityList = new CTable();
        ClientUtil.clearAll(this);
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel lblBalance;
    private com.see.truetransact.uicomponent.CLabel lblBalanceValue;
    private com.see.truetransact.uicomponent.CLabel lblInterestUpto;
    private com.see.truetransact.uicomponent.CLabel lblInterestUptoValue;
    private com.see.truetransact.uicomponent.CLabel lblPaymentBal;
    private com.see.truetransact.uicomponent.CLabel lblPaymentValue;
    private com.see.truetransact.uicomponent.CLabel lblPenalAmount;
    private com.see.truetransact.uicomponent.CLabel lblPenalValue;
    private com.see.truetransact.uicomponent.CLabel lblRecieptAmt;
    private com.see.truetransact.uicomponent.CLabel lblRecieptAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblTotDueAmountVal;
    private com.see.truetransact.uicomponent.CLabel lblTotLimitAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotRecievableVal;
    private com.see.truetransact.uicomponent.CLabel lblTotRecievedVal;
    private com.see.truetransact.uicomponent.CLabel lblTotalBalValue;
    private com.see.truetransact.uicomponent.CLabel lblTotalBalance;
    private com.see.truetransact.uicomponent.CLabel lblTotalRecievedAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotalRecievedValue;
    private com.see.truetransact.uicomponent.CPanel panMemberShipFacility;
    private com.see.truetransact.uicomponent.CPanel panMembershipTable;
    private com.see.truetransact.uicomponent.CPanel panTotal;
    private com.see.truetransact.uicomponent.CPanel panTotal1;
    private com.see.truetransact.uicomponent.CScrollPane srpMemberShipCTable;
    private com.see.truetransact.uicomponent.CTable tblMemberShipLiabilityList;
    private com.see.truetransact.uicomponent.CTextField txtServiceTaxAmt;
    // End of variables declaration//GEN-END:variables
}
