/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TransDetailsUI.java
 *
 * Created on February 2, 2005, 5:34 PM
 */
package com.see.truetransact.ui.transaction.common;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.awt.Component;
import java.awt.Color;
import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.TableCellEditor;
import javax.swing.JTable;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.common.viewall.TextUI;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.viewphotosign.ViewPhotoSignUI;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.customer.*;
import com.see.truetransact.ui.termloan.emicalculator.TermLoanInstallmentUI;
import com.see.truetransact.uicomponent.CTable;
//import com.see.truetransact.commonutil.interestcalc.Rounding;
import java.util.Date;
/**
 *
 * @author 152699
 */
public class TransDetailsUI extends com.see.truetransact.uicomponent.CPanel {

    private TransDetailsRB transRB = new TransDetailsRB();
    private String prodType = null;
    private String branch = null;
    private String acctNo = null;
    private String accDesc = null;
    private String transType = "";
    private long installmentToPay = 0;
    private String shadowCredit = null;
    private String shadowDebit = null;
    private String flexiDepositAmt = null;
    private String availableBalance = null;
    private String lienAmount = null;
    private String freezeAmount = null;
    private String hideBalance = "";
    private String showBalance = "";
    private String unclearBalance = null;
    private String limitAmount = "";
    private String isMultiDisburse = "";
    public String sanctionAmount = "";
    private HashMap insertPenal = new HashMap();
//    private boolean check=false;
//    private Rounding roundOff = new Rounding();
//    private long diffDays;
//    private boolean isDays=false;
    private String cBalance;
    private String avBalance;
    private Date inst_dt = null;
    //deposit CashTransaction
    private String lastApplDt = null;
    private String intAmount = null;
    private String depInterestAmt = null;
    private String depositAmt = null;
    private boolean isDebitSelect = false;
    private HashMap TermLoanCloseCharge = new HashMap();
    private HashMap calaclatedIntChargeMap = new HashMap();
    private HashMap asAndWhenMap = new HashMap();
    private HashMap kccNature = new HashMap();
    private String expiryDate = null;
    private List chargeList = null;
    private String ots = "";
    private double otsCharges = 0;
//    private List caseChargeList=null;
    private CInternalFrame sourceFrame = null;
    private HashMap actDetailsLoaded;
    private boolean refreshActDetails = false;
    private String penalAmount = null;
    private String penalMonth = null;
    private String todAmount = null;
    private String todUtilized = null;
    public ArrayList tblDataArrayList = null;
    private Map corpDetailMap = new HashMap();
    private String sourceScreen = "";
    private ArrayList selectedList = null;
    private int selectedRow = -1;
    private ArrayList col = null;
    public boolean clearTransFlag = false;
    private String prodId = null;
    private String cashAuthorise = "";
    public String changeInterest="N";
    private String memNo = "";
    private HashMap repayData = new HashMap();
    private HashMap otherBankAccountMap = new HashMap();
    private HashMap printMap = new HashMap();
    long noOfintDays = 0;

    public HashMap getRepayData() {
        return repayData;
    }

    public void setRepayData(HashMap repayData) {
        this.repayData = repayData;
    }
    
    public String getChangeInterest() {
        return changeInterest;
    }

    public String getMemNo() {
        return memNo;
    }

    public void setMemNo(String memNo) {
        this.memNo = memNo;
    }

  

    public String getAccDesc() {
        return accDesc;
    }

    public void setAccDesc(String accDesc) {
        this.accDesc = accDesc;
    }

    public void setChangeInterest(String changeInterest) {
        this.changeInterest = changeInterest;
    }
    private long actualDelay1 = 0;
    String round = "";
    private String accountStatus = "";
    private String productId = null;
    private Date currDt = null;
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getCashAuthorise() {
        return cashAuthorise;
    }

    public void setCashAuthorise(String cashAuthorise) {
        this.cashAuthorise = cashAuthorise;
    }
    double amnt = 0.0;

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    /**
     * Creates new form TransDetailsUI
     */
    public TransDetailsUI(com.see.truetransact.uicomponent.CPanel panLabelValues) {
        initComponents();
        initTable();
        currDt = ClientUtil.getCurrentDate();
        addToScreen(panLabelValues);
        actDetailsLoaded = new HashMap();
        setTransDetails(null, null, null);
    }

    private void addToScreen(com.see.truetransact.uicomponent.CPanel panLabelValues) {
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLabelValues.add(this, gridBagConstraints);
    }

    private void enableDisable(boolean yesno) {
        btnLiabilityReport.setEnabled(yesno);
        btnPhotoSign.setEnabled(yesno);
        btnShadowCR.setEnabled(yesno);
        btnShadowDR.setEnabled(yesno);
        btnPendingTransaction.setEnabled(yesno);
        btnMembershipLia.setEnabled(yesno);
        btnUnclear.setEnabled(yesno);
        //System.out.println("prodTypeprodTypeprodTypeprodType XXXX :"+prodType);
        if (prodType != null) {
            btnViewLedger.setEnabled(true);
        } else {
            btnViewLedger.setEnabled(false);
        }
        if (prodType != null && (prodType.equals("TL") || getSourceScreen().equals("LOAN_ACT_CLOSING"))) {
            btnReport.setEnabled(true);
        } else {
            btnReport.setEnabled(false);
        }
        if (prodType != null && prodType.equals("OA") || prodType != null && prodType.equals("AD")) {
            btnStandInstruction.setEnabled(true);
            btnChqBook.setEnabled(true);
        } else {
            btnStandInstruction.setEnabled(false);
            btnChqBook.setEnabled(false);
        }
         if (prodType != null && prodType.equals("TL")){
           btnRepayShedule.setVisible(true);  
         }else{
            btnRepayShedule.setVisible(false);     
         }
    }

    public void setTransDetails(String prodType, String branch, String acctNo) {
        //System.out.println("setTransDetails : "+acctNo +" prodType22 ===="+prodType);
       	this.prodType = prodType;
        this.branch = branch;
        this.acctNo = acctNo;

        if (prodType != null) {
            enableDisable(true);
        } else {
            enableDisable(false);
        }
        if (prodType == null && branch == null && acctNo == null) {
            actDetailsLoaded = new HashMap();
        }
        //system.out.println("#$#$#$ &*&*& actDetailsLoaded : "+actDetailsLoaded);
        if (!actDetailsLoaded.containsKey(prodType + acctNo + "ActData") && !actDetailsLoaded.containsKey(prodType + acctNo + "Balance") || refreshActDetails) {
            setActData(prodType, branch, acctNo);
            setBalance(prodType, branch, acctNo);
        } else {
            EnhancedTableModel model = (EnhancedTableModel) actDetailsLoaded.get(prodType + acctNo + "ActData");
            if (model != null) {
                tblAcctDetails.setModel(model);
                tblAcctDetails.revalidate();
            }
            model = (EnhancedTableModel) actDetailsLoaded.get(prodType + acctNo + "Balance");
            if (model != null) {
                tblTrans.setModel(model);
                tblTrans.revalidate();
            }
            model = null;
        }
    }

    public void setTransDetails(String prodType, String branch, String acctNo, String transType) {
        //system.out.println("setTransDetails : "+acctNo +" prodType ===== "+prodType);
        this.transType = transType;
        refreshActDetails = true;
        setTransDetails(prodType, branch, acctNo);
    }

    public void setTransDetails(String prodType, String branch, String acctNo, long installmentToPay) {
        //system.out.println("setTransDetails : "+acctNo +" prodType1111 ===== "+prodType);
        this.installmentToPay = installmentToPay;
        refreshActDetails = true;
        setTransDetails(prodType, branch, acctNo);
    }

    private void initTable() {
        /*
         * Set a cellrenderer to this table in order format the date
         */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Color color = panTrans.getBackground();
                if (column == 0) {
                    setBackground(color);
                    setForeground(Color.BLACK);
                } else {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblTrans.setDefaultRenderer(Object.class, renderer);
        tblAcctDetails.setDefaultRenderer(Object.class, renderer);
    }

    private void setActData(String prodType, String branch, String AccountNo) {
        try {
//            System.out.println("from Act Data"+prodType);
            final HashMap accDataMap = new HashMap();
            accDataMap.put("ACT_NUM", AccountNo);
            accDataMap.put("BRANCH_CODE", branch);
            List resultList = null;
            HashMap resultMap = new HashMap();
            doTermLoanIntCalc(prodType, branch, AccountNo);
            if (prodType != null && prodType.length() > 0 && corpDetailMap.size() < 1) {
                resultList = ClientUtil.executeQuery("getActData" + prodType, accDataMap);
                if (resultList != null && resultList.size() > 0) {
                    resultMap = (HashMap) resultList.get(0);
                }
            } else if (corpDetailMap.size() > 0 && prodType.equals("TL")) {
                resultList = ClientUtil.executeQuery("getActDataCorpTL", accDataMap);
                if (resultList != null && resultList.size() > 0) {
                    resultMap = (HashMap) resultList.get(0);
                }
            }
            // Hiding the balance based on the parameter
            hideBalance = CommonUtil.convertObjToStr(resultMap.get("HIDE_BALANCE"));
            showBalance = CommonUtil.convertObjToStr(resultMap.get("SHOW_BALANCE_TO"));
            //if (resultList != null && resultList.size() > 0) {
            ArrayList data = new ArrayList();
            ArrayList row = new ArrayList();
            //            row.add(transRB.getString("ProductCurrency"));
            //            row.add(CommonUtil.convertObjToStr(resultMap.get("PRODCURRENCY")));
            //            data.add(row);

            String branchID = CommonUtil.convertObjToStr(resultMap.get("BRANCH_CODE"));
            String custId = CommonUtil.convertObjToStr(resultMap.get("CUST_ID"));
            memNo = CommonUtil.convertObjToStr(resultMap.get("MEMBERSHIP_NO"));
            if (branchID != null && branchID.length()>0 && !branchID.equals(ProxyParameters.BRANCH_ID)) {
                branchID = "<html><font color=red>" + branchID + "</font></html>";
            }else if(!TrueTransactMain.selBranch.equals(ProxyParameters.BRANCH_ID)){
                branchID = "<html><font color=red>" + TrueTransactMain.selBranch + "</font></html>";
            }

            row = new ArrayList();
            row.add(transRB.getString("Branch"));
            row.add(branchID);
            data.add(row);
            row = new ArrayList();
            row.add(transRB.getString("CustId"));
            row.add(custId);
            data.add(row);
            row = new ArrayList();
            row.add(transRB.getString("MemberNo"));
            row.add(memNo);
            data.add(row);
            row = new ArrayList();
            row.add(transRB.getString("Status"));
            row.add(CommonUtil.convertObjToStr(resultMap.get("STATUS")));
            data.add(row);
            setAccountStatus(CommonUtil.convertObjToStr(resultMap.get("STATUS")));
            row = new ArrayList();
            row.add(transRB.getString("ModeofOperation"));
            row.add(CommonUtil.convertObjToStr(resultMap.get("OPT_MODE_ID")));
            data.add(row);
            row = new ArrayList();
            row.add(transRB.getString("Category"));
            row.add(CommonUtil.convertObjToStr(resultMap.get("CATEGORY")));
            data.add(row);
            if (prodType != null && prodType.equals("GL")) {
                row = new ArrayList();
                row.add(transRB.getString("BalanceType"));
                row.add(CommonUtil.convertObjToStr(resultMap.get("CONSTITUTION")));
                data.add(row);
                row = new ArrayList();
                row.add(transRB.getString("acctHeadType"));
                row.add(CommonUtil.convertObjToStr(resultMap.get("ACCT_HEAD_TYPE")));
                data.add(row);
            } else {
                row = new ArrayList();
                row.add(transRB.getString("Constitution"));
                row.add(CommonUtil.convertObjToStr(resultMap.get("CONSTITUTION")));
                data.add(row);
            }
            row = new ArrayList();
            row.add(transRB.getString("Cust Status"));
            row.add(CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_STATUS")));
            data.add(row);
            if (!resultMap.isEmpty()) {
                if (resultMap.get("MINOR") != null && resultMap.containsKey("MINOR") && resultMap.get("MINOR").equals("Y")) {
                    List guarList = ClientUtil.executeQuery("getGuarNameRelationshp", resultMap);
                    if (guarList != null && guarList.size() > 0) {
                        HashMap gaurMap = new HashMap();
                        gaurMap = (HashMap) guarList.get(0);
                        row = new ArrayList();
                        row.add(transRB.getString("Account Type"));
                        row.add("MINOR");
                        data.add(row);
                        row = new ArrayList();
                        row.add(transRB.getString("Guardian Name"));
                        row.add(CommonUtil.convertObjToStr(gaurMap.get("GUARDIAN_NAME")));
                        data.add(row);
                        row = new ArrayList();
                        row.add(transRB.getString("Guardian Relationship"));
                        row.add(CommonUtil.convertObjToStr(gaurMap.get("RELATIONSHIP")));
                        data.add(row);
                    }
                }
            }

            row = new ArrayList();
            row.add(transRB.getString("OpeningDate"));
            row.add(DateUtil.getStringDate((java.util.Date) resultMap.get("CREATE_DT")));
            data.add(row);
            if (prodType != null && prodType.equals("TD")) {
                row = new ArrayList();
                row.add(transRB.getString("MaturityDate"));
                row.add(DateUtil.getStringDate((java.util.Date) resultMap.get("MATURITY_DT")));
                data.add(row);
                row = new ArrayList();
                row.add(transRB.getString("intPayFreq"));
                row.add(resultMap.get("INTPAY_FREQ"));
                data.add(row);
                row = new ArrayList();
                row.add(transRB.getString("LastIntApplDt"));
                row.add(DateUtil.getStringDate((java.util.Date) resultMap.get("LAST_INT_APPL_DT")));
                data.add(row);
            }
            if (prodType != null) {
                if (prodType.equals("TL") || prodType.equals("AD") || prodType.equals("ATL") || prodType.equals("AAD")) {
                    row = new ArrayList();
                    row.add(transRB.getString("ExpiryDt"));
                    row.add(DateUtil.getStringDate((java.util.Date) resultMap.get("EXPIRY_DT")));
                    setExpiryDate(CommonUtil.convertObjToStr(resultMap.get("EXPIRY_DT")));
                    data.add(row);
                }
            }
            row = new ArrayList();
            row.add(transRB.getString("Remarks"));
            row.add(CommonUtil.convertObjToStr(resultMap.get("REMARKS")));
            data.add(row);
            setRemarksColor(); // Added by nithya on 20-07-2016 for 3195
            if (prodType != null) {
                if (prodType.equals("OA")) {
                    HashMap chequeMap = new HashMap();
                    row = new ArrayList();
                    row.add(transRB.getString("Unused Cheque Count"));
                    chequeMap.put("ACCT_NO", AccountNo);
                    List chequeList = ClientUtil.executeQuery("getUnusedCheckCounts", chequeMap);
                    if (chequeList != null && chequeList.size() > 0) {
                        chequeMap = (HashMap) chequeList.get(0);
                        row.add(CommonUtil.convertObjToInt(chequeMap.get("CHEQUE_COUNT")));
                    }
                    data.add(row);
                }
            }
            col = new ArrayList();
            //            col.add(transRB.getString("Display"));
            //            col.add(transRB.getString("Value"));
            col.add("");
            col.add("");
            EnhancedTableModel model = new EnhancedTableModel(data, col);
            tblAcctDetails.setModel(model);
            tblAcctDetails.revalidate();
            if (prodType != null && prodType.length() > 0 && AccountNo != null && AccountNo.length() > 0) {
                actDetailsLoaded.put(prodType + AccountNo + "ActData", model);
            }
            if (!CommonUtil.convertObjToStr(resultMap.get("REMARKS")).equals("")) {
                //Changed By Suresh
//                ClientUtil.displayAlert(CommonUtil.convertObjToStr(resultMap.get("REMARKS")));
            }
//            System.out.println("yyy");
            //            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (prodType != null && (prodType.equals("AD") || prodType.equals("TL") || prodType.equals("ATL") || prodType.equals("AAD"))) {
            asAndWhenMap = new HashMap();
        }
    }

    private void doTermLoanIntCalc(String prodType, String branch, String AccountNo) {
//            int freqType=0;
//            double PrincipleAmount=0;
//            double InterestAmount=0;
        String emiInSimpleInterest = "N"; // Added by nithya on 19-05-2020 for KD-380
        insertPenal = new HashMap();
        chargeList = null;
        otsCharges = 0;
        ots = "";
//            caseChargeList=null;
        sourceFrame = null;
//            int numberOfMonth=0;
//            int penalRate=0;
//            double isBalance=0;
//            double overDuePrincipleAmt=0;
//            double overDueInterestAmt=0;
//            double overDueAmt=0;
//            double productAmount=0;
//            HashMap getProduct=new HashMap();
        HashMap product = new HashMap();
        
        final HashMap accDataMap = new HashMap();
        accDataMap.put("ACT_NUM", AccountNo);
        accDataMap.put("BRANCH_CODE", branch);
        HashMap loanInstall = new HashMap();
//            List resultList = null;
//            HashMap resultMap = new HashMap();
        String actNum = CommonUtil.convertObjToStr(accDataMap.get("ACT_NUM"));
//            //system.out.println("transdetails@@@@@@actnumMAP"+accDataMap);
        if (actNum != null) {
            if (actNum.lastIndexOf("_") != -1) {
                actNum = actNum.substring(0, actNum.lastIndexOf("_"));
            }
        } 
        //added by sreekrishnan for kcc transaction--kcc_nature checking
        if (prodType != null && prodType.equals("AD")) {
            HashMap kccNatureMap = new HashMap();
            HashMap kccMap  = new HashMap();
            kccNatureMap.put("ACT_NUM", AccountNo);
            List kccList = ClientUtil.executeQuery("getKccNature", kccNatureMap);
            if (kccList != null && kccList.size() > 0) {
                kccNature = (HashMap) kccList.get(0);
                //System.out.println("kccNature####"+kccNature);
            }             
        }
        //system.out.println("transDetail"+actNum+accDataMap.get("BRANCH_CODE"));
        loanInstall.put("ACT_NUM", actNum);
        loanInstall.put("BRANCH_CODE", accDataMap.get("BRANCH_CODE"));
        if (getCorpDetailMap() != null && getCorpDetailMap().size() > 0
                && CommonUtil.convertObjToStr(getCorpDetailMap().get("BEHAVES_LIKE")).equals("CORP_LOAN")) {
            loanInstall.putAll(getCorpDetailMap());
        }
        //System.out.println("corpDetailMap ===========" + corpDetailMap.size() + "Pyy === " + prodType);
        if (prodType != null && (prodType.equals("TL") || prodType.equals("BILLS")) && corpDetailMap.size() < 1) {
        
            // Added by nithya on 19-05-2020 for KD-380
            if(asAndWhenMap != null && asAndWhenMap.containsKey("EMI_IN_SIMPLEINTREST") && (asAndWhenMap.get("EMI_IN_SIMPLEINTREST") != null) && asAndWhenMap.get("EMI_IN_SIMPLEINTREST").equals("Y")){
                emiInSimpleInterest = "Y";
            }
            // End
            
//                HashMap InstalDate=new HashMap();
//                List getInstallDate=null;
            Date cDate = ClientUtil.getCurrentDateProperFormat();
            HashMap allInstallmentMap = null;
//                double principleAmt=0;
//                double intAmt=0;
            List paidAmt = ClientUtil.executeQuery("getPaidPrinciple", loanInstall);
            allInstallmentMap = (HashMap) paidAmt.get(0);
            //system.out.println("!!!!asAndWhenMap:"+asAndWhenMap);
            double totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
            if (asAndWhenMap == null || (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N"))) {
                paidAmt = ClientUtil.executeQuery("getIntDetails", loanInstall);
                if (paidAmt != null && paidAmt.size() > 0) {
                    allInstallmentMap = (HashMap) paidAmt.get(0);
                }
                double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
                totPrinciple += totExcessAmt;
            }
            List lst = ClientUtil.executeQuery("getAllLoanInstallment", loanInstall);
            inst_dt = null;
            for (int i = 0; i < lst.size(); i++) {
                allInstallmentMap = (HashMap) lst.get(i);
                double instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                if (instalAmt <= totPrinciple) {
                    totPrinciple -= instalAmt;
                    //                        if(lst.size()==1){
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                    //                        }
                } else {
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                    totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue() - totPrinciple;
                    break;
                }

            }

            Date addDt = (Date) cDate.clone();
            Date instDt = DateUtil.addDays(inst_dt, 1);
            addDt.setDate(instDt.getDate());
            addDt.setMonth(instDt.getMonth());
            addDt.setYear(instDt.getYear());
            loanInstall.put("FROM_DATE", addDt);//DateUtil.addDays(inst_dt,1));
            loanInstall.put("TO_DATE", cDate);
            //system.out.println("getTotalamount#####"+loanInstall);
            List lst1 = null;
            if (inst_dt != null && (totPrinciple > 0)) {
                lst1 = ClientUtil.executeQuery("getTotalAmountOverDue", loanInstall);
                //system.out.println("listsize####"+lst1);
            }
            double principle = 0;
            if (lst1 != null && lst1.size() > 0) {
                HashMap map = (HashMap) lst1.get(0);
                principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
            }
            totPrinciple += principle;
            insertPenal = new HashMap();
            String asAnWhen = CommonUtil.convertObjToStr(asAndWhenMap.get("AS_CUSTOMER_COMES"));
            ots = CommonUtil.convertObjToStr(asAndWhenMap.get("OTS"));
//                if(asAndWhenMap !=null && asAnWhen.equals("Y") && ots.equals("Y")){
//                    totPrinciple=CommonUtil.convertObjToDouble(asAndWhenMap.get("PRINCIPAL")).doubleValue();
//                    otsCharges=CommonUtil.convertObjToDouble(asAndWhenMap.get("MISCELLANEOUS CHARGES")).doubleValue();
//                    insertPenal.put("LOAN_BALANCE_PRINCIPAL",new Double(totPrinciple));
//                }

            insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(totPrinciple));
            insertPenal.put("INSTALL_DT", inst_dt);
            //FOR BANKDATABASE INTEREST AND PENAL
            //system.out.println("@@@@asAndWhenMap:"+asAndWhenMap);
            if (asAndWhenMap.containsKey("MORATORIUM_INT_FOR_EMI")) {
                insertPenal.put("MORATORIUM_INT_FOR_EMI", asAndWhenMap.get("MORATORIUM_INT_FOR_EMI"));
            }
		//if modified by chithra for mantis:10401: in advances acct charges is not shown in the cash screen
            if ((prodType != null && prodType.equals("TL"))||(prodType != null && prodType.equals("AD"))) {
                chargeList = ClientUtil.executeQuery("getChargeDetails", loanInstall);
            }
            if (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                //system.out.println("penal ---------------=="+penal);
                List facilitylst = ClientUtil.executeQuery("LoneFacilityDetailAD", loanInstall);
                if (facilitylst != null && facilitylst.size() > 0) {
                    HashMap hash = (HashMap) facilitylst.get(0);
                    hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
                    if (asAndWhenMap.containsKey("PREMATURE")) {
                        insertPenal.put("PREMATURE", asAndWhenMap.get("PREMATURE"));
                    }
                    if (asAndWhenMap.containsKey("PREMATURE") && asAndWhenMap.containsKey("PREMATURE_INT_CALC_AMT")
                            && CommonUtil.convertObjToStr(asAndWhenMap.get("PREMATURE_INT_CALC_AMT")).equals("LOANSANCTIONAMT")) {
                        hash.put("FROM_DT", hash.get("ACCT_OPEN_DT"));
                    } else {
                        hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                        hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
                    }
                    hash.put("TO_DATE", ClientUtil.getCurrentDateProperFormat());
                    if (!(asAndWhenMap != null && asAndWhenMap.containsKey("INSTALL_TYPE") && asAndWhenMap.get("INSTALL_TYPE") != null && asAndWhenMap.get("INSTALL_TYPE").equals("EMI"))) {
                        System.out.println("executing for not EMI");
                        facilitylst = ClientUtil.executeQuery("getPaidPrinciple", hash);// code change 1 ---------
                    } else {
                        if (emiInSimpleInterest.equalsIgnoreCase("Y")) {
                            facilitylst = ClientUtil.executeQuery("getPaidPrinciple", hash);
                        } else {
                            facilitylst = null;
                        }
                        //System.out.println("EMI....aswhenmap::" + asAndWhenMap);
                        if (asAndWhenMap.containsKey("PRINCIPAL_DUE") && asAndWhenMap.get("PRINCIPAL_DUE") != null) {
                            insertPenal.put("CURR_MONTH_PRINCEPLE", asAndWhenMap.get("PRINCIPAL_DUE"));
                            inst_dt = cDate;
                        }
                    }
                    //printMap will be print in server log added by Kannan AR
                    //System.out.println("asAndWhenMap ===== code change1" + asAndWhenMap);
                    printMap = new HashMap();
                    printMap.put("FROM_TRANS_DETAILS", "FROM_TRANS_DETAILS");
                    printMap.put("CALC_INT", interest);
                    printMap.put("CALC_PENAL", penal);
                    if (facilitylst != null && facilitylst.size() > 0 && !asAndWhenMap.containsKey("INT_CALC_FROM_SCREEN")) {
                        //System.out.println("Executing inside....*******");
                        hash = (HashMap) facilitylst.get(0);
                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();                       
                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();                        
                        //system.out.println("penal --2222-------------===="+penal);
                        insertPenal.put("PAID_INTEREST", hash.get("INTEREST"));                        
                        printMap.put("PAID_INT", hash.get("INTEREST"));
                        printMap.put("FIN_INT", interest);
                        printMap.put("PAID_PENAL", hash.get("PENAL"));
                        printMap.put("FIN_PENAL", penal);
                        ClientUtil.executeQuery("PRINT_SERVRSIDE", printMap);
                    }
                }
                //system.out.println("####interest:"+interest);
                if (asAndWhenMap != null && asAndWhenMap.containsKey("REBATE_INTEREST")) {
                    insertPenal.put("REBATE_MODE", asAndWhenMap.get("REBATE_MODE"));
                    insertPenal.put("REBATE_INTEREST", asAndWhenMap.get("REBATE_INTEREST"));
                    insertPenal.put("REBATE_INTEREST_UPTO", asAndWhenMap.get("REBATE_INTEREST_UPTO"));
                } else {
                    insertPenal.put("REBATE_INTEREST", new Double(0));
                }

                if (asAndWhenMap != null && asAndWhenMap.containsKey("INTEREST_WAIVER")) {
                    insertPenal.put("INTEREST_WAIVER", asAndWhenMap.get("INTEREST_WAIVER"));
                } else {
                    insertPenal.put("INTEREST_WAIVER", "N");
                }
                insertPenal.put("ACCT_NUM", asAndWhenMap.get("ACCOUNTNO"));
                if (asAndWhenMap != null && asAndWhenMap.containsKey("PENAL_WAIVER")) {
                    insertPenal.put("PENAL_WAIVER", asAndWhenMap.get("PENAL_WAIVER"));
                } else {
                    insertPenal.put("PENAL_WAIVER", "N");
                }
                if (asAndWhenMap != null && asAndWhenMap.containsKey("NOTICE_WAIVER")) {
                    insertPenal.put("NOTICE_WAIVER", asAndWhenMap.get("NOTICE_WAIVER"));
                } else {
                    insertPenal.put("NOTICE_WAIVER", "N");
                }
                if (asAndWhenMap != null && asAndWhenMap.containsKey("PRINCIPAL_WAIVER")) {
                    insertPenal.put("PRINCIPAL_WAIVER", asAndWhenMap.get("PRINCIPAL_WAIVER"));
                } else {
                    insertPenal.put("PRINCIPAL_WAIVER", "N");
                }
                //added by rishad 24/04/2015
                if (asAndWhenMap != null && asAndWhenMap.containsKey("ARC_WAIVER")) {
                    insertPenal.put("ARC_WAIVER", asAndWhenMap.get("ARC_WAIVER"));
                } else {
                    insertPenal.put("ARC_WAIVER", "N");
                }
                if (asAndWhenMap != null && asAndWhenMap.containsKey("EP_COST_WAIVER")) {
                    insertPenal.put("EP_COST_WAIVER", asAndWhenMap.get("EP_COST_WAIVER"));
                } else {
                    insertPenal.put("EP_COST_WAIVER", "N");
                }
                if (asAndWhenMap != null && asAndWhenMap.containsKey("POSTAGE_WAIVER")) {
                    insertPenal.put("POSTAGE_WAIVER", asAndWhenMap.get("POSTAGE_WAIVER"));
                } else {
                    insertPenal.put("POSTAGE_WAIVER", "N");
                }
               if (asAndWhenMap != null && asAndWhenMap.containsKey("ADVERTISE_WAIVER")) {
                    insertPenal.put("ADVERTISE_WAIVER", asAndWhenMap.get("ADVERTISE_WAIVER"));
                } else {
                    insertPenal.put("ADVERTISE_WAIVER", "N");
                }
                if (asAndWhenMap != null && asAndWhenMap.containsKey("LEGAL_WAIVER")) {
                    insertPenal.put("LEGAL_WAIVER", asAndWhenMap.get("LEGAL_WAIVER"));
                } else {
                    insertPenal.put("LEGAL_WAIVER", "N");
                }
                if (asAndWhenMap != null && asAndWhenMap.containsKey("INSURANCE_WAIVER")) {
                    insertPenal.put("INSURANCE_WAIVER", asAndWhenMap.get("INSURANCE_WAIVER"));
                } else {
                    insertPenal.put("INSURANCE_WAIVER", "N");
                }
                if (asAndWhenMap != null && asAndWhenMap.containsKey("MISCELLANEOUS_WAIVER")) {
                    insertPenal.put("MISCELLANEOUS_WAIVER", asAndWhenMap.get("MISCELLANEOUS_WAIVER"));
                } else {
                    insertPenal.put("MISCELLANEOUS_WAIVER", "N");
                }
                if (asAndWhenMap != null && asAndWhenMap.containsKey("DECREE_WAIVER")) {
                    insertPenal.put("DECREE_WAIVER", asAndWhenMap.get("DECREE_WAIVER"));
                } else {
                    insertPenal.put("DECREE_WAIVER", "N");
                }
                if (asAndWhenMap != null && asAndWhenMap.containsKey("ARBITRARY_WAIVER")) {
                    insertPenal.put("ARBITRARY_WAIVER", asAndWhenMap.get("ARBITRARY_WAIVER"));
                } else {
                    insertPenal.put("ARBITRARY_WAIVER", "N");
                }
                
                // For over due interest
                if (asAndWhenMap != null && asAndWhenMap.containsKey("OVERDUEINT_WAIVER")) {
                    insertPenal.put("OVERDUEINT_WAIVER", asAndWhenMap.get("OVERDUEINT_WAIVER"));
                } else {
                    insertPenal.put("OVERDUEINT_WAIVER", "N");
                }
                // end
                
                if (interest <= 0 && CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y") && prodType.equals("TL")) {
                    double intVal = intAmtForSalRecovery();
                    if (intVal > 0) {
                        interest = intVal;
                    }
                }
                if (interest > 0) {
                    insertPenal.put("CURR_MONTH_INT", new Double(interest));
                } else {
                    if (asAndWhenMap.containsKey("SECRETARIAT_INT") && CommonUtil.convertObjToStr(asAndWhenMap.get("SECRETARIAT_INT")).equals("Y")) {
                        insertPenal.put("CURR_MONTH_INT", CommonUtil.convertObjToDouble(asAndWhenMap.get("MIN_DEBITINT_AMT")));
                    } else {
                        insertPenal.put("CURR_MONTH_INT", new Double(0));
                    }
                }
                //system.out.println("Penel --VVVVVVVVVVVVVVVVVV============="+penal);  
                if (penal > 0) {
                    insertPenal.put("PENAL_INT", new Double(penal));
                } else {
                    insertPenal.put("PENAL_INT", new Double(0));
                }
                insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
                insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
				insertPenal.put("LAST_INT_CALC_DT", asAndWhenMap.get("LAST_INT_CALC_DT"));
                insertPenal.put("ROI", asAndWhenMap.get("ROI"));
                // Added by nithya for 0008470 : overdue interest for EMI Loans
                if (asAndWhenMap.containsKey("EMI_OVERDUE_CHARGE") && asAndWhenMap.get("EMI_OVERDUE_CHARGE") != null) {
                    insertPenal.put("EMI_OVERDUE_CHARGE", asAndWhenMap.get("EMI_OVERDUE_CHARGE"));
                }

//                    caseChargeList=ClientUtil.executeQuery("getTermLoanCaseChargePaidAmount",loanInstall);
               
                chargeList = ClientUtil.executeQuery("getChargeDetails", loanInstall);
            } else {
                List getIntDetails = ClientUtil.executeQuery("getIntDetails", loanInstall);
                HashMap hash = null;
                if (getIntDetails != null) {
                    for (int i = 0; i < getIntDetails.size(); i++) {
                        hash = (HashMap) getIntDetails.get(i);
                        String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                        double pBal = CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue();
                        double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                        double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                        double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
                        pBal -= excess;
                        if (pBal < totPrinciple) {
                            insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(pBal));
                        }
                        if (trn_mode.equals("C*")) {

                            //                       double prevAmt=iBal;
                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                            insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
                            insertPenal.put("EBAL", hash.get("EBAL"));
                            break;
                        } else {
                            if (!trn_mode.equals("DP")) //                            insertPenal.put("CURR_MONTH_INT",new Double (0.0));
                            //                        else
                            {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                            }
                            insertPenal.put("EBAL", hash.get("EBAL"));
                            insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
                        }

                        //                    if(isDebitSelect)
                        //                    insertPenal.remove("CURR_MONTH_PRINCEPLE");
                        //                    isDebitSelect=false;
                        //system.out.println("int principel detailsINSIDE LOAN##"+insertPenal);
                    }
                }
                getIntDetails = ClientUtil.executeQuery("getPenalIntDetails", loanInstall);
                hash = (HashMap) getIntDetails.get(0);
                //System.out.println("Penel --BBBBBBBBBBBBBBBBBBB============="+hash.get("PIBAL"));
                insertPenal.put("PENAL_INT", hash.get("PIBAL"));


            }


            //                List getIntDetails=ClientUtil.executeQuery("getIntDetails", loanInstall);
            //                HashMap hash=null;
            //                if(getIntDetails!=null)
            //                    for(int i=0;i<getIntDetails.size();i++){
            //                        hash=(HashMap)getIntDetails.get(i);
            //                        String trn_mode=CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
            //                        double pBal=CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue();
            //                        double iBal=CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
            //                        double pibal= CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
            //                        if(pBal<totPrinciple)
            //                            insertPenal.put("CURR_MONTH_PRINCEPLE",new Double(pBal));
            //                        if(trn_mode.equals("C*")){
            //
            //                            //                       double prevAmt=iBal;
            //                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
            //                            insertPenal.put("PRINCIPLE_BAL",hash.get("PBAL"));
            //                            insertPenal.put("EBAL",hash.get("EBAL"));
            //                            break;
            //                        }
            //                        else {
            //                            if(!trn_mode.equals("DP"))
            //                                //                            insertPenal.put("CURR_MONTH_INT",new Double (0.0));
            //                                //                        else
            //                                insertPenal.put("CURR_MONTH_INT",String.valueOf(iBal + pibal));
            //                            insertPenal.put("EBAL",hash.get("EBAL"));
            //                            insertPenal.put("PRINCIPLE_BAL",hash.get("PBAL"));
            //                        }
            //
            //                        //                    if(isDebitSelect)
            //                        //                    insertPenal.remove("CURR_MONTH_PRINCEPLE");
            //                        //                    isDebitSelect=false;
            //                        //system.out.println("int principel detailsINSIDE LOAN##"+insertPenal);
            //                    }
            //                getIntDetails=ClientUtil.executeQuery("getPenalIntDetails", loanInstall);
            //                hash=(HashMap)getIntDetails.get(0);
            //                insertPenal.put("PENAL_INT",hash.get("PIBAL"));


            
        } else if (corpDetailMap.size() > 0 && prodType.equals("TL")) {  // For Corporate Loan purpose added by Rajesh
//                HashMap InstalDate=new HashMap();
//                List getInstallDate=null;
            Date cDate = ClientUtil.getCurrentDateProperFormat();
            HashMap allInstallmentMap = null;
//                double principleAmt=0;
//                double intAmt=0;
            List paidAmt = ClientUtil.executeQuery("getCorpLoanPaidPrinciple", loanInstall);
            allInstallmentMap = (HashMap) paidAmt.get(0);
            double totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
            if (asAndWhenMap == null || (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N"))) {
                paidAmt = ClientUtil.executeQuery("getCorpLoanIntDetails", loanInstall);
                if (paidAmt != null && paidAmt.size() > 0) {
                    allInstallmentMap = (HashMap) paidAmt.get(0);
                }
                double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
                totPrinciple += totExcessAmt;
            }
            List lst = ClientUtil.executeQuery("getAllLoanInstallmentForCorpLoan", loanInstall);
            inst_dt = new Date();
            for (int i = 0; i < lst.size(); i++) {
                allInstallmentMap = (HashMap) lst.get(i);
                double instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                if (instalAmt <= totPrinciple) {
                    totPrinciple -= instalAmt;
                    //                        if(lst.size()==1){
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                    //                        }
                } else {
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                    totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue() - totPrinciple;
                    break;
                }

            }
            Date addDt = (Date) cDate.clone();
            Date instDt = DateUtil.addDays(inst_dt, 1);
            addDt.setDate(instDt.getDate());
            addDt.setMonth(instDt.getMonth());
            addDt.setYear(instDt.getYear());
            loanInstall.put("FROM_DATE", addDt);//DateUtil.addDays(inst_dt,1));
            loanInstall.put("TO_DATE", cDate);
            //system.out.println("getTotalamount#####"+loanInstall);
            List lst1 = null;
            if (inst_dt != null && (totPrinciple > 0)) {
                lst1 = ClientUtil.executeQuery("getTotalAmountOverDueForCorpLoan", loanInstall);
                //system.out.println("listsize####"+lst1);
            }
            double principle = 0;
            if (lst1 != null && lst1.size() > 0) {
                HashMap map = (HashMap) lst1.get(0);
                principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
            }
            totPrinciple += principle;
            insertPenal = new HashMap();
            insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(totPrinciple));
            insertPenal.put("INSTALL_DT", inst_dt);
            //FOR BANKDATABASE INTEREST AND PENAL

            if (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                List facilitylst = ClientUtil.executeQuery("LoneFacilityDetailAD", loanInstall);
                if (facilitylst != null && facilitylst.size() > 0) {
                    HashMap hash = (HashMap) facilitylst.get(0);
                    hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
                    hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                    hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
                    hash.put("TO_DATE", ClientUtil.getCurrentDateProperFormat());
                    if (!(asAndWhenMap != null && asAndWhenMap.containsKey("INSTALL_TYPE") && asAndWhenMap.get("INSTALL_TYPE") != null && asAndWhenMap.get("INSTALL_TYPE").equals("EMI"))) {
                        facilitylst = ClientUtil.executeQuery("getPaidPrinciple", hash);//-- code change 2
                    } else {
                        facilitylst = null;
                        if (asAndWhenMap.containsKey("PRINCIPAL_DUE") && asAndWhenMap.get("PRINCIPAL_DUE") != null) {
                            insertPenal.put("CURR_MONTH_PRINCEPLE", asAndWhenMap.get("PRINCIPAL_DUE"));
                            inst_dt = cDate;
                        }
                    }
                    if (facilitylst != null && facilitylst.size() > 0 && !asAndWhenMap.containsKey("INT_CALC_FROM_SCREEN")) {
                        hash = (HashMap) facilitylst.get(0);
                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                    }
                }
                if (interest > 0) {
                    insertPenal.put("CURR_MONTH_INT", new Double(interest));
                } else {
                    insertPenal.put("CURR_MONTH_INT", new Double(0));
                }
                //system.out.println("penal --333 =----------------"+penal);
                if (penal > 0) {
                    insertPenal.put("PENAL_INT", new Double(penal));
                } else {
                    insertPenal.put("PENAL_INT", new Double(0));
                }
                insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
                insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
//                    caseChargeList=ClientUtil.executeQuery("getTermLoanCaseChargePaidAmount",loanInstall);
                chargeList = ClientUtil.executeQuery("getChargeDetails", loanInstall);
               } else {
                List getIntDetails = ClientUtil.executeQuery("getIntDetails", loanInstall);
                HashMap hash = null;
                if (getIntDetails != null) {
                    for (int i = 0; i < getIntDetails.size(); i++) {
                        hash = (HashMap) getIntDetails.get(i);
                        String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                        double pBal = CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue();
                        double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                        double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                        double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
                        pBal -= excess;
                        if (pBal < totPrinciple) {
                            insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(pBal));
                        }
                        if (trn_mode.equals("C*")) {

                            //                       double prevAmt=iBal;
                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                            insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
                            insertPenal.put("EBAL", hash.get("EBAL"));
                            break;
                        } else {
                            if (!trn_mode.equals("DP")) //                            insertPenal.put("CURR_MONTH_INT",new Double (0.0));
                            //                        else
                            {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                            }
                            insertPenal.put("EBAL", hash.get("EBAL"));
                            insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
                        }

                        //                    if(isDebitSelect)
                        //                    insertPenal.remove("CURR_MONTH_PRINCEPLE");
                        //                    isDebitSelect=false;
                        //system.out.println("int principel detailsINSIDE LOAN##"+insertPenal);
                    }
                }
                getIntDetails = ClientUtil.executeQuery("getPenalIntDetails", loanInstall);
                hash = (HashMap) getIntDetails.get(0);
                //system.out.println("Penel --4444444444============="+hash.get("PIBAL"));
                insertPenal.put("PENAL_INT", hash.get("PIBAL"));


            }

        }
       
        if (prodType != null && prodType.equals("ATL")) {
//                HashMap InstalDate=new HashMap();
//                List getInstallDate=null;
            Date cDate = ClientUtil.getCurrentDateProperFormat();
            HashMap allInstallmentMap = null;
//                double principleAmt=0;
//                double intAmt=0;
            List paidAmt = ClientUtil.executeQuery("getAgriPaidPrinciple", loanInstall);
            allInstallmentMap = (HashMap) paidAmt.get(0);
            double totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
            if (asAndWhenMap == null || (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N"))) {
                paidAmt = ClientUtil.executeQuery("getIntDetailsATL", loanInstall);
                if (paidAmt != null && paidAmt.size() > 0) {
                    allInstallmentMap = (HashMap) paidAmt.get(0);
                }
                double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
                totPrinciple += totExcessAmt;
            }
            List lst = ClientUtil.executeQuery("getAllLoanInstallmentATL", loanInstall);
            inst_dt = null;
            for (int i = 0; i < lst.size(); i++) {
                allInstallmentMap = (HashMap) lst.get(i);
                double instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                if (instalAmt <= totPrinciple) {
                    totPrinciple -= instalAmt;
                    //                        if(lst.size()==1){
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                    //                        }
                } else {
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                    totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue() - totPrinciple;
                    break;
                }

            }
            loanInstall.put("FROM_DATE", DateUtil.addDays(inst_dt, 1));
            loanInstall.put("TO_DATE", cDate);
            //system.out.println("getTotalamount#####"+loanInstall);
            List lst1 = null;
            if (inst_dt != null && (totPrinciple > 0)) {
                lst1 = ClientUtil.executeQuery("getTotalAmountOverDueATL", loanInstall);
                //system.out.println("listsize####"+lst1);
            }
            double principle = 0;
            if (lst1 != null && lst1.size() > 0) {
                HashMap map = (HashMap) lst1.get(0);
                principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
            }
            totPrinciple += principle;
            insertPenal = new HashMap();
            insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(totPrinciple));
            insertPenal.put("INSTALL_DT", inst_dt);
            //FOR BANKDATABASE INTEREST AND PENAL

            if (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                List facilitylst = ClientUtil.executeQuery("AgriLoneFacilityDetailAD", loanInstall);
                if (facilitylst != null && facilitylst.size() > 0) {
                    HashMap hash = (HashMap) facilitylst.get(0);
                    hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
                    hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                    hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
                    hash.put("TO_DATE", ClientUtil.getCurrentDateProperFormat());
                    if (!(asAndWhenMap != null && asAndWhenMap.containsKey("INSTALL_TYPE") && asAndWhenMap.get("INSTALL_TYPE") != null && asAndWhenMap.get("INSTALL_TYPE").equals("EMI"))) {
                        facilitylst = ClientUtil.executeQuery("getAgriPaidPrinciple", hash);
                    } else {
                        facilitylst = null;
                        if (asAndWhenMap.containsKey("PRINCIPAL_DUE") && asAndWhenMap.get("PRINCIPAL_DUE") != null) {
                            insertPenal.put("CURR_MONTH_PRINCEPLE", asAndWhenMap.get("PRINCIPAL_DUE"));
                            inst_dt = cDate;
                        }
                    }
                    if (facilitylst != null && facilitylst.size() > 0) {
                        hash = (HashMap) facilitylst.get(0);
                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                    }
                }
                if (interest > 0) {
                    insertPenal.put("CURR_MONTH_INT", new Double(interest));
                } else {
                    insertPenal.put("CURR_MONTH_INT", new Double(0));
                }
                //system.out.println("Penel --55555555555============="+penal);
                if (penal > 0) {
                    insertPenal.put("PENAL_INT", new Double(penal));
                } else {
                    insertPenal.put("PENAL_INT", new Double(0));
                }
                insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
                insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
//                    caseChargeList=ClientUtil.executeQuery("getTermLoanCaseChargePaidAmount",loanInstall);
                chargeList = ClientUtil.executeQuery("getChargeDetails", loanInstall);
               } else {
                List getIntDetails = ClientUtil.executeQuery("getIntDetailsATL", loanInstall);
                HashMap hash = null;
                if (getIntDetails != null) {
                    for (int i = 0; i < getIntDetails.size(); i++) {
                        hash = (HashMap) getIntDetails.get(i);
                        String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                        double pBal = CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue();
                        double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                        double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                        double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
                        pBal -= excess;
                        if (pBal < totPrinciple) {
                            insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(pBal));
                        }
                        if (trn_mode.equals("C*")) {

                            //                       double prevAmt=iBal;
                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                            insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
                            insertPenal.put("EBAL", hash.get("EBAL"));
                            break;
                        } else {
                            if (!trn_mode.equals("DP")) //                            insertPenal.put("CURR_MONTH_INT",new Double (0.0));
                            //                        else
                            {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                            }
                            insertPenal.put("EBAL", hash.get("EBAL"));
                            insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
                        }

                        //                    if(isDebitSelect)
                        //                    insertPenal.remove("CURR_MONTH_PRINCEPLE");
                        //                    isDebitSelect=false;
                        //system.out.println("int principel detailsINSIDE LOAN##"+insertPenal);
                    }
                }
                getIntDetails = ClientUtil.executeQuery("getPenalIntDetailsATL", loanInstall);
                hash = (HashMap) getIntDetails.get(0);
                //system.out.println("Penel --55555555555555555555============="+hash.get("PIBAL"));
                insertPenal.put("PENAL_INT", hash.get("PIBAL"));


            }
        }
       if (prodType != null && prodType.equals("AD")) {
            if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
                if (asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                    List facilitylst = ClientUtil.executeQuery("LoneFacilityDetailAD", loanInstall);
                    double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                    double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    ///System.out.println("interest  9999^^^^^^^^====="+interest);
                    HashMap kccMap = new HashMap();
                    if (facilitylst != null && facilitylst.size() > 0) {
                        HashMap hash = (HashMap) facilitylst.get(0);
                        kccMap.put("LOAN_DT",hash.get("ACCT_OPEN_DT"));
                        hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
                        hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                        hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
                        //babu comm on 28-06-2014
                       // hash.put("TO_DATE", DateUtil.addDaysProperFormat(ClientUtil.getCurrentDateProperFormat(), -1));
                        hash.put("TO_DATE", DateUtil.addDaysProperFormat(ClientUtil.getCurrentDateProperFormat(), 0));
                        facilitylst = ClientUtil.executeQuery("getPaidPrincipleAD", hash); // -- code change 3
                        //System.out.println("facilitylst^^^^^^^^====="+facilitylst);
                        if (facilitylst != null && facilitylst.size() > 0 && !asAndWhenMap.containsKey("INT_CALC_FROM_SCREEN")) {
                            hash = (HashMap) facilitylst.get(0);
                            interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                            //System.out.println("interest6666^^^^^^^^====="+interest);
                            penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                             printMap.put("PAID_INT", hash.get("INTEREST"));
                             printMap.put("FIN_INT", interest);
                             printMap.put("PAID_PENAL", hash.get("PENAL"));
                             printMap.put("FIN_PENAL", penal);
                             ClientUtil.executeQuery("PRINT_SERVRSIDE", printMap);
                        }
                    }

                    if (interest > 0) {
                        insertPenal.put("CURR_MONTH_INT", new Double(interest));
                    } else {
                        insertPenal.put("CURR_MONTH_INT", new Double(0));
                    }
                    //System.out.println("asAndWhenMap :: advance :: " + asAndWhenMap);
                    //System.out.println("Penel --666666666666666============="+penal);
                    if (penal > 0) {
                        insertPenal.put("PENAL_INT", new Double(penal));
                    } else {
                        HashMap mTemp = new HashMap();
                        List lst1 = null;
                        mTemp.put("ACTNUM", loanInstall.get("ACT_NUM"));
                        mTemp.put("ASONDT", DateUtil.addDaysProperFormat(ClientUtil.getCurrentDateProperFormat(), 0));
                        mTemp.put("ROI", asAndWhenMap.get("ROI"));
                        mTemp.put("LOANDATE", kccMap.get("LOAN_DT"));
                        if (kccNature != null && kccNature.size() > 0) {
		                    if(kccNature.containsKey("KCC_NATURE") && 
	                                  kccNature.get("KCC_NATURE")!=null && kccNature.get("KCC_NATURE").equals("Y")){
		                        lst1 = ClientUtil.executeQuery("getPenalInterestAdvKCC", mTemp);
		                    }else{
	                            lst1 = ClientUtil.executeQuery("getPenalInterestAdv", mTemp);
	                        }
						}else{
                            lst1 = ClientUtil.executeQuery("getPenalInterestAdv", mTemp);
                        }
                            
                        if (lst1 != null && lst1.size() > 0) {
                            mTemp = (HashMap) lst1.get(0);
                            insertPenal.put("PENAL_INT", CommonUtil.convertObjToDouble(mTemp.get("INTEREST")).doubleValue());
                        } else {
                            insertPenal.put("PENAL_INT", new Double(0));
                        }
                    }
                    insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
                    insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
//                        caseChargeList=ClientUtil.executeQuery("getTermLoanCaseChargePaidAmount",loanInstall);
                    chargeList = ClientUtil.executeQuery("getChargeDetails", loanInstall);
                   } else {
                    if (prodType != null && prodType.equals("AD")) {
                        List getIntDetails = ClientUtil.executeQuery("getIntDetailsAD", loanInstall);
                        HashMap hash = null;
                        for (int i = 0; i < getIntDetails.size(); i++) {
                            hash = (HashMap) getIntDetails.get(i);
                            String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                            double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                            double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                            double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
                            if (trn_mode.equals("C*")) {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
                                insertPenal.put("EBAL", hash.get("EBAL"));
                                break;
                            } else {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
                                insertPenal.put("EBAL", hash.get("EBAL"));
                            }
                            //system.out.println("int principel detailsINSIDE OD"+insertPenal);
                        }
                        getIntDetails = ClientUtil.executeQuery("getPenalIntDetailsAD", loanInstall);
                        if (getIntDetails.size() > 0) {
                            hash = (HashMap) getIntDetails.get(0);
                            //system.out.println("Penel --777777777777777777============="+hash.get("PIBAL"));
                            insertPenal.put("PENAL_INT", hash.get("PIBAL"));
                        }
                        if (isDebitSelect) {
                            insertPenal.remove("PRINCIPLE_BAL");
                        }
                        isDebitSelect = false;
                    }

                    if (prodType != null && prodType.equals("AD")) {
                        List getIntDetails = ClientUtil.executeQuery("getIntDetailsAD", loanInstall);
                        HashMap hash = null;
                        for (int i = 0; i < getIntDetails.size(); i++) {
                            hash = (HashMap) getIntDetails.get(i);
                            String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                            double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                            double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                            if (trn_mode.equals("C*")) {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                insertPenal.put("PRINCIPLE_BAL", hash.get("PBAL"));
                                insertPenal.put("EBAL", hash.get("EBAL"));
                                break;

                            } else {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                insertPenal.put("PRINCIPLE_BAL", hash.get("PBAL"));
                                insertPenal.put("EBAL", hash.get("EBAL"));
                            }
                            //system.out.println("int principel detailsINSIDE OD"+insertPenal);
                        }
                        getIntDetails = ClientUtil.executeQuery("getPenalIntDetailsAD", loanInstall);
                        if (getIntDetails.size() > 0) {
                            hash = (HashMap) getIntDetails.get(0);
                            //system.out.println("Penel --888888888============="+hash.get("PIBAL"));
                            insertPenal.put("PENAL_INT", hash.get("PIBAL"));
                        }
                        if (isDebitSelect) {
                            insertPenal.remove("PRINCIPLE_BAL");
                        }
                        isDebitSelect = false;
                    }                     
                }
                // Added by nithya on 28-03-2017 for 6063
                if (asAndWhenMap != null && asAndWhenMap.containsKey("INTEREST_WAIVER")) {
                    insertPenal.put("INTEREST_WAIVER", asAndWhenMap.get("INTEREST_WAIVER"));
                } else {
                    insertPenal.put("INTEREST_WAIVER", "N");
                }
                if (asAndWhenMap != null && asAndWhenMap.containsKey("PENAL_WAIVER")) {
                    insertPenal.put("PENAL_WAIVER", asAndWhenMap.get("PENAL_WAIVER"));
                } else {
                    insertPenal.put("PENAL_WAIVER", "N");
                }
                if (asAndWhenMap != null && asAndWhenMap.containsKey("NOTICE_WAIVER")) {
                    insertPenal.put("NOTICE_WAIVER", asAndWhenMap.get("NOTICE_WAIVER"));
                } else {
                    insertPenal.put("NOTICE_WAIVER", "N");
                }
                if (asAndWhenMap != null && asAndWhenMap.containsKey("PRINCIPAL_WAIVER")) {
                    insertPenal.put("PRINCIPAL_WAIVER", asAndWhenMap.get("PRINCIPAL_WAIVER"));
                } else {
                    insertPenal.put("PRINCIPAL_WAIVER", "N");
                }               
                if (asAndWhenMap != null && asAndWhenMap.containsKey("ARC_WAIVER")) {
                    insertPenal.put("ARC_WAIVER", asAndWhenMap.get("ARC_WAIVER"));
                } else {
                    insertPenal.put("ARC_WAIVER", "N");
                }
                if (asAndWhenMap != null && asAndWhenMap.containsKey("EP_COST_WAIVER")) {
                    insertPenal.put("EP_COST_WAIVER", asAndWhenMap.get("EP_COST_WAIVER"));
                } else {
                    insertPenal.put("EP_COST_WAIVER", "N");
                }
                if (asAndWhenMap != null && asAndWhenMap.containsKey("POSTAGE_WAIVER")) {
                    insertPenal.put("POSTAGE_WAIVER", asAndWhenMap.get("POSTAGE_WAIVER"));
                } else {
                    insertPenal.put("POSTAGE_WAIVER", "N");
                }
               if (asAndWhenMap != null && asAndWhenMap.containsKey("ADVERTISE_WAIVER")) {
                    insertPenal.put("ADVERTISE_WAIVER", asAndWhenMap.get("ADVERTISE_WAIVER"));
                } else {
                    insertPenal.put("ADVERTISE_WAIVER", "N");
                }
                if (asAndWhenMap != null && asAndWhenMap.containsKey("LEGAL_WAIVER")) {
                    insertPenal.put("LEGAL_WAIVER", asAndWhenMap.get("LEGAL_WAIVER"));
                } else {
                    insertPenal.put("LEGAL_WAIVER", "N");
                }
                if (asAndWhenMap != null && asAndWhenMap.containsKey("INSURANCE_WAIVER")) {
                    insertPenal.put("INSURANCE_WAIVER", asAndWhenMap.get("INSURANCE_WAIVER"));
                } else {
                    insertPenal.put("INSURANCE_WAIVER", "N");
                }
                if (asAndWhenMap != null && asAndWhenMap.containsKey("MISCELLANEOUS_WAIVER")) {
                    insertPenal.put("MISCELLANEOUS_WAIVER", asAndWhenMap.get("MISCELLANEOUS_WAIVER"));
                } else {
                    insertPenal.put("MISCELLANEOUS_WAIVER", "N");
                }
                if (asAndWhenMap != null && asAndWhenMap.containsKey("DECREE_WAIVER")) {
                    insertPenal.put("DECREE_WAIVER", asAndWhenMap.get("DECREE_WAIVER"));
                } else {
                    insertPenal.put("DECREE_WAIVER", "N");
                }
                if (asAndWhenMap != null && asAndWhenMap.containsKey("ARBITRARY_WAIVER")) {
                    insertPenal.put("ARBITRARY_WAIVER", asAndWhenMap.get("ARBITRARY_WAIVER"));
                } else {
                    insertPenal.put("ARBITRARY_WAIVER", "N");
                }
            }
            //Added by chithra for mantis:10401: in advances acct charges is not shown in the cash screen
            if (prodType != null && prodType.equals("AD")) {
                chargeList = ClientUtil.executeQuery("getChargeDetails", loanInstall);
            }           
        }
        if (prodType != null && prodType.equals("AAD")) {
            if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
                if (asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                    List facilitylst = ClientUtil.executeQuery("LoneFacilityDetailAAD", loanInstall);
                    double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                    double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    if (facilitylst != null && facilitylst.size() > 0) {
                        HashMap hash = (HashMap) facilitylst.get(0);
                        hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
                        hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                        hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
                        hash.put("TO_DATE", DateUtil.addDaysProperFormat(ClientUtil.getCurrentDateProperFormat(), -1));
                        facilitylst = ClientUtil.executeQuery("getPaidPrincipleAAD", hash);  //-- code changes 4
                        if (facilitylst != null && facilitylst.size() > 0 && !asAndWhenMap.containsKey("INT_CALC_FROM_SCREEN")) {
                            hash = (HashMap) facilitylst.get(0);
                            interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                            penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                        }
                    }

                    if (interest > 0) {
                        insertPenal.put("CURR_MONTH_INT", new Double(interest));
                    } else {
                        insertPenal.put("CURR_MONTH_INT", new Double(0));
                    }
                    //system.out.println("Penel --999999999999999============="+penal);
                    if (penal > 0) {
                        insertPenal.put("PENAL_INT", new Double(penal));
                    } else {
                        insertPenal.put("PENAL_INT", new Double(0));
                    }
                    insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
                    insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
//                        caseChargeList=ClientUtil.executeQuery("getTermLoanCaseChargePaidAmount",loanInstall);
                    chargeList = ClientUtil.executeQuery("getChargeDetails", loanInstall);
                    } else {
                    if (prodType != null && prodType.equals("AAD")) {
                        List getIntDetails = ClientUtil.executeQuery("getIntDetailsAAD", loanInstall);
                        HashMap hash = null;
                        for (int i = 0; i < getIntDetails.size(); i++) {
                            hash = (HashMap) getIntDetails.get(i);
                            String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                            double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                            double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                            double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
                            if (trn_mode.equals("C*")) {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
                                insertPenal.put("EBAL", hash.get("EBAL"));
                                break;
                            } else {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
                                insertPenal.put("EBAL", hash.get("EBAL"));
                            }
                            //system.out.println("int principel detailsINSIDE OD"+insertPenal);
                        }
                        getIntDetails = ClientUtil.executeQuery("getPenalIntDetailsAD", loanInstall);
                        if (getIntDetails.size() > 0) {
                            hash = (HashMap) getIntDetails.get(0);
                            //system.out.println("Penel --111000000============="+hash.get("PIBAL"));
                            insertPenal.put("PENAL_INT", hash.get("PIBAL"));
                        }
                        if (isDebitSelect) {
                            insertPenal.remove("PRINCIPLE_BAL");
                        }
                        isDebitSelect = false;
                    }

                    if (prodType != null && prodType.equals("AAD")) {
                        List getIntDetails = ClientUtil.executeQuery("getIntDetailsAD", loanInstall);
                        HashMap hash = null;
                        for (int i = 0; i < getIntDetails.size(); i++) {
                            hash = (HashMap) getIntDetails.get(i);
                            String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                            double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                            double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                            if (trn_mode.equals("C*")) {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                insertPenal.put("PRINCIPLE_BAL", hash.get("PBAL"));
                                insertPenal.put("EBAL", hash.get("EBAL"));
                                break;

                            } else {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                insertPenal.put("PRINCIPLE_BAL", hash.get("PBAL"));
                                insertPenal.put("EBAL", hash.get("EBAL"));
                            }
                            //system.out.println("int principel detailsINSIDE OD"+insertPenal);
                        }
                        getIntDetails = ClientUtil.executeQuery("getPenalIntDetailsAD", loanInstall);
                        if (getIntDetails.size() > 0) {
                            hash = (HashMap) getIntDetails.get(0);
                            //system.out.println("Penel --XXXXXXXXXXXXXX============="+hash.get("PIBAL"));
                            insertPenal.put("PENAL_INT", hash.get("PIBAL"));
                        }
                        if (isDebitSelect) {
                            insertPenal.remove("PRINCIPLE_BAL");
                        }
                        isDebitSelect = false;
                    }


                }
            }
        }
        if (prodType != null && (prodType.equals("AAD") || prodType.equals("ATL"))) {
            loanInstall.put("ACCOUNTNO", loanInstall.get("ACT_NUM"));
            loanInstall.put("TODAY_DT", ClientUtil.getCurrentDateProperFormat());
            List lst = (List) ClientUtil.executeQuery("getAgriSubLimitATL", loanInstall);
//                  Double dbl =(List)ClientUtil.executeQuery("getAgriSubLimitATL",loanInstall); 
            Double fbl = (Double) lst.get(0);
            insertPenal.put("SUBLIMIT", fbl);
        }

        //system.out.println("int principel details"+insertPenal);
//            //             if(prodType !=null && (prodType.equals("AD")|| prodType.equals("TL"))){
//            //                 String modeOperation=CommonUtil.convertObjToStr(resultMap.get("STATUS"));
//            //                 if(modeOperation.equals("CLOSED")){
//            //                     insertPenal.put("CURR_MONTH_PRINCEPLE","0.0");
//            //                     insertPenal.put("CURR_MONTH_INT","0.0");
//            //                 }
//            //
//            //             }
//            // Hiding the balance based on the parameter
//            hideBalance = CommonUtil.convertObjToStr(resultMap.get("HIDE_BALANCE"));
//            showBalance = CommonUtil.convertObjToStr(resultMap.get("SHOW_BALANCE_TO"));
//            //if (resultList != null && resultList.size() > 0) {
//            ArrayList data = new ArrayList();
//            ArrayList row = new ArrayList();
//            //            row.add(transRB.getString("ProductCurrency"));
//            //            row.add(CommonUtil.convertObjToStr(resultMap.get("PRODCURRENCY")));
//            //            data.add(row);
//            
//            String branchID = CommonUtil.convertObjToStr(resultMap.get("BRANCH_CODE"));
//            if (!branchID.equals(ProxyParameters.BRANCH_ID)) {
//                branchID = "<html><font color=red>" + branchID + "</font></html>";
//            }
//            
//            row = new ArrayList();
//            row.add(transRB.getString("Branch"));
//            row.add(branchID);
//            data.add(row);
//            row = new ArrayList();
//            row.add(transRB.getString("Status"));
//            row.add(CommonUtil.convertObjToStr(resultMap.get("STATUS")));
//            data.add(row);
//            row = new ArrayList();
//            row.add(transRB.getString("ModeofOperation"));
//            row.add(CommonUtil.convertObjToStr(resultMap.get("OPT_MODE_ID")));
//            data.add(row);
//            row = new ArrayList();
//            row.add(transRB.getString("Category"));
//            row.add(CommonUtil.convertObjToStr(resultMap.get("CATEGORY")));
//            data.add(row);
//            row = new ArrayList();
//            row.add(transRB.getString("Constitution"));
//            row.add(CommonUtil.convertObjToStr(resultMap.get("CONSTITUTION")));
//            data.add(row);
//            row = new ArrayList();
//            row.add(transRB.getString("Cust Status"));
//            row.add(CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_STATUS")));
//            data.add(row);
//            if(!resultMap.isEmpty())
//           if(resultMap.get("MINOR")!=null && resultMap.containsKey("MINOR") && resultMap.get("MINOR").equals("Y")){
//                List guarList= ClientUtil.executeQuery("getGuarNameRelationshp",resultMap);
//                if(guarList!=null && guarList.size()>0){
//                    HashMap gaurMap= new HashMap();
//                    gaurMap=(HashMap)guarList.get(0);
//                    row = new ArrayList();
//                    row.add(transRB.getString("Account Type"));
//                    row.add("MINOR");
//                    data.add(row);
//                    row = new ArrayList();
//                    row.add(transRB.getString("Guardian Name"));
//                    row.add(CommonUtil.convertObjToStr(gaurMap.get("GUARDIAN_NAME")));
//                    data.add(row);
//                    row = new ArrayList();
//                    row.add(transRB.getString("Guardian Relationship"));
//                    row.add(CommonUtil.convertObjToStr(gaurMap.get("RELATIONSHIP")));
//                    data.add(row);
//                }
//            }
//            
//            row = new ArrayList();
//            row.add(transRB.getString("OpeningDate"));
//            row.add(DateUtil.getStringDate((java.util.Date) resultMap.get("CREATE_DT")));
//            data.add(row);
//            if (prodType != null && prodType.equals("TD")){
//                row = new ArrayList();
//                row.add(transRB.getString("MaturityDate"));
//                row.add(DateUtil.getStringDate((java.util.Date) resultMap.get("MATURITY_DT")));
//                data.add(row);
//            }
//            if (prodType != null)
//                if (prodType.equals("TL") || prodType.equals("AD") || prodType.equals("ATL") || prodType.equals("AAD")) {
//                    row = new ArrayList();
//                    row.add(transRB.getString("ExpiryDt"));
//                    row.add(DateUtil.getStringDate((java.util.Date) resultMap.get("EXPIRY_DT")));
//                    setExpiryDate(CommonUtil.convertObjToStr(resultMap.get("EXPIRY_DT")));
//                    data.add(row);
//                }
//            row = new ArrayList();
//            row.add(transRB.getString("Remarks"));
//            row.add(CommonUtil.convertObjToStr(resultMap.get("REMARKS")));
//            data.add(row);
//            ArrayList col = new ArrayList();
//            //            col.add(transRB.getString("Display"));
//            //            col.add(transRB.getString("Value"));
//            col.add("");
//            col.add("");
//            EnhancedTableModel model = new EnhancedTableModel(data, col);
//            tblAcctDetails.setModel(model);
//            tblAcctDetails.revalidate();
//            if (prodType!=null && prodType.length()>0 && AccountNo!=null && AccountNo.length()>0)
//                actDetailsLoaded.put(prodType+AccountNo+"ActData",model);
//            //            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        if(prodType !=null && (prodType.equals("AD") || prodType.equals("TL") || prodType.equals("ATL") || prodType.equals("AAD")))
//            asAndWhenMap=new HashMap();
    }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod <= (roundingFactor / 2)) || (mod <= (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

    public double getRound(double amount, String mode) {  // double result=0.0;
        //system.out.println("in getRounddd "+amount+"  "+mode);
        if (mode != null && !mode.equals("")) {
            if (mode.equals("NO_ROUND_OFF")) {
                //system.out.println(" in no round");
                amount = new Double(amount);
                //system.out.println("interestAmt "+amount);
            } else if (mode.equals("NEAREST_VALUE")) {
                //system.out.println(" in nearest roundingg");
                amount = (double) getNearest((long) (amount * 100), 100) / 100;
            } else if (mode.equals("HIGHER_VALUE")) {
                //system.out.println("in higher valueee");
                double d = amount;
                if (d % 1.0 > 0) {
                    //system.out.println("mode valuee  "+d%1.0);
                    double c = d % 1.0;
                    d = d - c;
                    d = d + 1;
                    //system.out.println("Higher valuuuee "+d);
                } else {
                    //system.out.println("dsf  "+d%1.0);
                    //system.out.println("ggggg "+d);
                }
                amount = d;
                //system.out.println("Higher valuuuee reall "+d);

                //interestAmt = (double)getNearest((long)(interestAmt *100),100)/100;
            } else if (mode.equals("LOWER_VALUE")) {
                //system.out.println("in lower round valueee");
                double d = amount;
                if (d % 1.0 > 0) {
                    //system.out.println("mode valuee  "+d%1.0);
                    double c = d % 1.0;
                    d = d - c;

                    //system.out.println("Higher valuuuee "+d);
                } else {
                    //system.out.println("dsf  "+d%1.0);
                    //system.out.println("ggggg "+d);
                }
                amount = d;
                //system.out.println("Higher valuuuee reall "+d);

            } else {
                //system.out.println(" in no round");
                amount = new Double(amount);
                //system.out.println("interestAmt "+amount);
            }
        } else {
            //system.out.println(" i else  in no round");
            amount = new Double(amount);
            //system.out.println("interestAmt "+amount);

        }
        return amount;
    }
    private void setColour() {
        /* Set a cellrenderer to this table in order format the date */
         DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
             int temprow = -1,tempcolumn = -1;
             public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                 super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                 //System.out.println("row #####" + row);
                 Object obj = tblTrans.getValueAt(row, column);
                 //System.out.println("row ###11##" + row +"colum11n"+column+ "obj#11##" + obj  +" dddd---->"+getChangeInterest());
                
                 if (CommonUtil.convertObjToStr(obj).equals(String.valueOf(transRB.getString("CurrAmt"))) && 
                    (getChangeInterest()!=null && getChangeInterest().equals("Y"))
                         && CommonUtil.convertObjToStr(obj).equals("Interest Due")
                         ||  (temprow == row && tempcolumn+1 == column)) {
                     setForeground(Color.RED);
                     temprow = row;
                     tempcolumn = column;
                 } else {
                     setForeground(Color.BLACK);
                 }
                 this.setOpaque(true);
                 return this;
             }
        };
        tblTrans.setDefaultRenderer(Object.class, renderer);
    }

    // Added by nithya on 20-07-2016 for 3195
    private void setRemarksColor(){
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
             int temprow = -1,tempcolumn = -1;
             public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                 super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                 Object actObj = tblAcctDetails.getValueAt(row, column);                                
                 if(CommonUtil.convertObjToStr(actObj).equals("Remarks") ||  (temprow == row && tempcolumn+1 == column)){
                     setForeground(Color.RED);
                     setFont(new java.awt.Font("MS Sans Serif", java.awt.Font.BOLD, 15));
                     temprow = row;
                     tempcolumn = column;
                 }else {
                     setForeground(Color.BLACK);
                 }
                 this.setOpaque(true);
                 return this;
             }
        };
       
        tblAcctDetails.setDefaultRenderer(Object.class, renderer);
    }
    // End
    private void setBalance(String prodType, String branch, String AccountNo) {
        try {
            HashMap balanceMap = new HashMap();
            otherBankAccountMap = new HashMap();
            balanceMap.put("ACT_NUM", AccountNo);
            if(TrueTransactMain.selBranch.length()>0){
                balanceMap.put("BRANCH_CODE", TrueTransactMain.selBranch);
            }else{
                balanceMap.put("BRANCH_CODE", branch);
            }
            List resultList = null;
            HashMap resultMap = new HashMap();
            double totalRecievable = 0.0;

            // If Hide balance is yes it will not show the balance
            // only for specific hierarchy that will be shown
            if (prodType != null
                    && ((hideBalance.equals("") || hideBalance.equalsIgnoreCase("N"))
                    || (hideBalance.equalsIgnoreCase("Y")
                    && CommonUtil.convertObjToInt(ProxyParameters.HIERARCHY_ID) >= CommonUtil.convertObjToInt(showBalance)))) {
                if (getCorpDetailMap().size() > 0 && prodType.equals("TL")) {
                    resultList = ClientUtil.executeQuery("getCorpBalanceTL", balanceMap);
                } else {
                    resultList = ClientUtil.executeQuery("getBalance" + prodType, balanceMap);
                }
                if (resultList.size() > 0) {
                    resultMap = (HashMap) resultList.get(0);
                }
            }

            String clearBalance = null;
            String totalBalance = null;
            String subsidyAvailableBalance = null;
            String penalWaivedAmt = null;
            String interestWaivedAmt = null;
            Date penalWaivedDate = null;
            String rebateInterest = null;
            String paidRebateInterest = null;
            Date uptoRebatePaidDt = null;
            setAvBalance((resultMap.get("AV_BALANCE") == null) ? "0" : resultMap.get("AV_BALANCE").toString());
            setAvailableBalance((resultMap.get("AVAILABLE_BALANCE") == null) ? "0" : resultMap.get("AVAILABLE_BALANCE").toString());
            setShadowCredit((resultMap.get("SHADOW_CREDIT") == null) ? "0" : resultMap.get("SHADOW_CREDIT").toString());
            setShadowDebit((resultMap.get("SHADOW_DEBIT") == null) ? "0" : resultMap.get("SHADOW_DEBIT").toString());
            setLienAmount((resultMap.get("LIEN_AMOUNT") == null) ? "0" : resultMap.get("LIEN_AMOUNT").toString());
            setFreezeAmount((resultMap.get("FREEZE_AMOUNT") == null) ? "0" : resultMap.get("FREEZE_AMOUNT").toString());
            setFlexiDepositAmt((resultMap.get("FLEXI_DEPOSIT_AMT") == null) ? "0" : resultMap.get("FLEXI_DEPOSIT_AMT").toString());
            clearBalance = CurrencyValidation.formatCrore((resultMap.get("CLEAR_BALANCE") == null) ? "0" : resultMap.get("CLEAR_BALANCE").toString());
            unclearBalance = CurrencyValidation.formatCrore((resultMap.get("UNCLEAR_BALANCE") == null) ? "0" : resultMap.get("UNCLEAR_BALANCE").toString());
            subsidyAvailableBalance = CurrencyValidation.formatCrore((resultMap.get("SUBSIDY_AVAILABLE_AMT") == null) ? "0" : resultMap.get("SUBSIDY_AVAILABLE_AMT").toString());
            penalWaivedAmt = CurrencyValidation.formatCrore((resultMap.get("PENAL_WAIVE_AMT") == null) ? "0" : resultMap.get("PENAL_WAIVE_AMT").toString());
            interestWaivedAmt = CurrencyValidation.formatCrore((resultMap.get("INTEREST_WAIVE_AMT") == null) ? "0" : resultMap.get("INTEREST_WAIVE_AMT").toString());
            paidRebateInterest = CurrencyValidation.formatCrore((resultMap.get("REBATE_AMT") == null) ? "0" : resultMap.get("REBATE_AMT").toString());
            penalWaivedDate = (Date) resultMap.get("PENAL_WAIVE_DT");
            uptoRebatePaidDt = (Date) resultMap.get("REBATE_DT");
            totalBalance = CurrencyValidation.formatCrore((resultMap.get("TOTAL_BALANCE") == null) ? "0" : resultMap.get("TOTAL_BALANCE").toString());
            setCBalance((resultMap.get("CLEAR_BALANCE") == null) ? "0" : resultMap.get("CLEAR_BALANCE").toString());
            ArrayList data = new ArrayList();
            ArrayList row = new ArrayList();
            setIsMultiDisburse("");
            setTodAmount((resultMap.get("TOD_AMOUNT") == null) ? "0" : resultMap.get("TOD_AMOUNT").toString());
            setTodUtilized((resultMap.get("TOD_UTILIZED") == null) ? "0" : resultMap.get("TOD_UTILIZED").toString());
            isDebitSelect = true;
            if (prodType != null && prodType.equals("TD")) {
                String actNum = CommonUtil.convertObjToStr(balanceMap.get("ACT_NUM"));
                if (actNum.lastIndexOf("_") != -1) {
                    actNum = actNum.substring(0, actNum.lastIndexOf("_"));
                }
                if (isIsDebitSelect()) {
                    isDebitSelect = true;
                    row = new ArrayList();
                    HashMap lastMap = new HashMap();
                    double intAmt = 0.0;
                    balanceMap.put("DEPOSIT_NO", actNum);
                    List lst = ClientUtil.executeQuery("getInterestDeptIntTable", balanceMap);
                    if (lst != null && lst.size() > 0) {
                        lastMap = (HashMap) lst.get(0);
                        String prodId = CommonUtil.convertObjToStr(lastMap.get("PROD_ID"));
                        //Added By Akhila
                        HashMap roundMap = new HashMap();
                        // roundMap=
                        roundMap.put("PROD_ID", prodId);
                        List roundgList = ClientUtil.executeQuery("getRoungOffTypeInterest", roundMap);
                        if (!roundgList.isEmpty()) {
                            roundMap = (HashMap) roundgList.get(0);
                        }
                        //added c                       
                        HashMap behavesMap = new HashMap();
                        behavesMap.put("PROD_ID", prodId);
                        List lstBehave = ClientUtil.executeQuery("getBehavesLikeForDeposit", behavesMap);
                        if (lstBehave != null && lstBehave.size() > 0) {
                            behavesMap = (HashMap) lstBehave.get(0);
                            String fdPayMode = CommonUtil.convertObjToStr(lastMap.get("FD_CASH_PAYMENT"));
                            if (behavesMap.get("BEHAVES_LIKE").equals("FIXED")) {
                                double years = CommonUtil.convertObjToDouble(lastMap.get("DEPOSIT_PERIOD_YY")).doubleValue();
                                double month = CommonUtil.convertObjToDouble(lastMap.get("DEPOSIT_PERIOD_MM")).doubleValue();
                                double totIntAmt = CommonUtil.convertObjToDouble(lastMap.get("TOT_INT_AMT")).doubleValue();
                                double freq = CommonUtil.convertObjToDouble(lastMap.get("INTPAY_FREQ")).doubleValue();
                                double principal = CommonUtil.convertObjToDouble(lastMap.get("DEPOSIT_AMT")).doubleValue();
                                double rateOfInterest = CommonUtil.convertObjToDouble(lastMap.get("RATE_OF_INT")).doubleValue();
                                double monthAmt = 0.0;
                                if (years > 0) {
                                    month = years * 12;
                                }
                                if (freq > 0 && freq == 30) {
                                    monthAmt = totIntAmt / month;
                                    // monthAmt = (double)getNearest((long)(monthAmt *100),100)/100;
                                    monthAmt = getRound(month, roundMap.get("INT_ROUNDOFF_TERMS").toString());
                                } else if (freq > 0 && freq == 90) {
                                    freq = freq / 30;
                                    monthAmt = principal * rateOfInterest * freq / 1200;
                                    // monthAmt = (double)getNearest((long)(monthAmt *100),100)/100;
                                    monthAmt = getRound(month, roundMap.get("INT_ROUNDOFF_TERMS").toString());
                                } else if (freq > 0 && freq == 180) {
                                    freq = freq / 30;
                                    monthAmt = principal * rateOfInterest * freq / 1200;
                                    // monthAmt = (double)getNearest((long)(monthAmt *100),100)/100;
                                    monthAmt = getRound(month, roundMap.get("INT_ROUNDOFF_TERMS").toString());
                                } else if (freq > 0 && freq == 360) {
                                    freq = freq / 30;
                                    monthAmt = principal * rateOfInterest * freq / 1200;
                                    // monthAmt = (double)getNearest((long)(monthAmt *100),100)/100;
                                    monthAmt = getRound(month, roundMap.get("INT_ROUNDOFF_TERMS").toString());
                                }
                                double creAmt = 0.0;
                                double debAmt = 0.0;
                                creAmt = CommonUtil.convertObjToDouble(lastMap.get("TOTAL_INT_CREDIT")).doubleValue();
                                debAmt = CommonUtil.convertObjToDouble(lastMap.get("TOTAL_INT_DRAWN")).doubleValue();
                                intAmt = creAmt - debAmt;
                                double calcAmt = monthAmt;
                                int j = 1;
                                //System.out.println("calcAmt" + calcAmt + "intAmt" + intAmt + "monthAmt" + monthAmt);
                                if ((monthAmt < intAmt) && calcAmt > 0) {
                                    for (int i = 0; i < j; i++) {
                                        if ((calcAmt * j) <= intAmt) {
                                            monthAmt = calcAmt * j;
                                            j = j + 1;
                                            //System.out.println("j inside loop" + j);
                                        } else {
                                            break;
                                        }
                                    }
                                }
                                //&& (fdPayMode == null || fdPayMode.equals("N"))
                                // This Line is commented by Rajesh
                                // because after running Interest Application through charges
                                // through Cash Transaction screen could not pay the Interest amount
//                                if(intAmt>0 && monthAmt>0 && monthAmt<=intAmt){  
                                if (intAmt > 0) {
                                    //system.out.println("#######getLastApplDt for intAmt:"+intAmt);
                                    row = new ArrayList();
                                    row.add(transRB.getString("LastInt"));
                                    row.add(DateUtil.getStringDate((java.util.Date) lastMap.get("LAST_INT_APPL_DT")));
                                    data.add(row);
                                    row = new ArrayList();
                                    row.add(transRB.getString("InterestAmt"));
                                    //    monthAmt = (double)getNearest((long)(monthAmt *100),100)/100;
                                    monthAmt = getRound(month, roundMap.get("INT_ROUNDOFF_TERMS").toString());
                                    depInterestAmt = String.valueOf(monthAmt);
                                    setDepInterestAmt((depInterestAmt == null) ? "0" : depInterestAmt);
                                    row.add(getDepInterestAmt());
                                    data.add(row);
                                    isDebitSelect = false;
                                } else {
                                    setDepInterestAmt("0.0");
                                }
                            }
                        }
                        lstBehave = null;
                    }
                    lst = null;
                    row = new ArrayList();
                    lastMap = new HashMap();
                    double totAmt = 0.0;
                    double availAmt = 0.0;
                    lastMap.put("DEPOSIT_NO", actNum);
                    lst = ClientUtil.executeQuery("getLienDetForDeposits", lastMap);
                    if (lst != null && lst.size() > 0) {
                        lastMap = (HashMap) lst.get(0);
                        if (lastMap.containsKey("STATUS")) {
                            if (lastMap.get("STATUS").equals("LIEN")) {
                                totAmt = CommonUtil.convertObjToDouble(lastMap.get("TOTAL_BALANCE")).doubleValue();
                            }
                            availAmt = CommonUtil.convertObjToDouble(lastMap.get("AVAILABLE_BALANCE")).doubleValue();
                            double amount = totAmt - availAmt;
                            row = new ArrayList();
                            row.add(transRB.getString("Lien Amt"));
                            row.add(CurrencyValidation.formatCrore(String.valueOf(amount)));
                            data.add(row);
                        }
                    }
                    lst = null;
                    isDebitSelect = false;
                }
                HashMap accountMap = new HashMap();
                accountMap.put("DEPOSIT_NO", actNum);
                accountMap.put("BRANCH_ID", balanceMap.get("BRANCH_CODE"));
                List lst = ClientUtil.executeQuery("getProductIdForDeposits", accountMap);
                if (lst != null && lst.size() > 0) {
                    accountMap = (HashMap) lst.get(0);
                    HashMap prodMap = new HashMap();
                    prodMap.put("PROD_ID", accountMap.get("PROD_ID"));
                    List lstBehave = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
                    if (lstBehave != null && lstBehave.size() > 0) {
                        prodMap = (HashMap) lstBehave.get(0);
                        if (prodMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                            //Added By Suresh
                            String insBeyondMaturityDat = "";
                            String specialRD = "N";
                            double due = 0.0;
                            double irrdue = 0.0;
                            double installmentDue = 0.0;
                            int curmon = 0;
                            int curYear = 0;
                            int depmon = 0;
                            int depyear = 0;
                            String rdClosingPenalRequired = "";
                            HashMap recurringMap = new HashMap();
                            List recurringLst = ClientUtil.executeQuery("getRecurringDepositDetails", accountMap);
                            if (recurringLst != null && recurringLst.size() > 0) {
                                recurringMap = (HashMap) recurringLst.get(0);
                                insBeyondMaturityDat = CommonUtil.convertObjToStr(recurringMap.get("INST_BEYOND_MATURITY_DATE"));
                                irrdue = CommonUtil.convertObjToDouble(recurringMap.get("RD_IRREGULAR_INSTALLMENTS_DUE")).doubleValue();
                                rdClosingPenalRequired = CommonUtil.convertObjToStr(recurringMap.get("RD_CLOSING_PENAL_REQUIRED"));// Added by nithya on 0-09-2019
                                if(recurringMap.containsKey("SPECIAL_RD") && recurringMap.get("SPECIAL_RD") != null && recurringMap.get("SPECIAL_RD").equals("Y")){
                                  specialRD = "Y"; //2066
                                }
                            }
                            double totalDelay = 0;
                            long actualDelay = 0;
                            double delayAmt = 0.0;
                            double tot_Inst_paid = 0.0;
                            double inst = 0.0;
                            double inst1 = 0.0;
                            double tempDelayAmt = 0;
                            int gracePeriod = 0;
                            double depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT")).doubleValue();
                            Date matDt = (Date) currDt.clone();
                            Date depDt = (Date) currDt.clone();
                            HashMap lastMap = new HashMap();
                            lastMap.put("DEPOSIT_NO", actNum);
                            lst = ClientUtil.executeQuery("getInterestDeptIntTable", lastMap);
                            if (lst != null && lst.size() > 0) {
                                lastMap = (HashMap) lst.get(0);
                                tot_Inst_paid = CommonUtil.convertObjToDouble(lastMap.get("TOTAL_INSTALL_PAID")).doubleValue();
                                HashMap prematureDateMap = new HashMap();
                                double monthPeriod = 0.0;
                                Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("MATURITY_DT")));
                                if (matDate.getDate() > 0) {
                                    matDt.setDate(matDate.getDate());
                                    matDt.setMonth(matDate.getMonth());
                                    matDt.setYear(matDate.getYear());
                                }
                                Date depDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("DEPOSIT_DT")));
                                if (depDate.getDate() > 0) {
                                    depDt.setDate(depDate.getDate());
                                    depDt.setMonth(depDate.getMonth());
                                    depDt.setYear(depDate.getYear());
                                }
                                curmon = currDt.getMonth() + 1;
                                curYear = currDt.getYear() + 1900;
                                depmon = depDt.getMonth() + 1;
                                depyear = depDt.getYear() + 1900;
                                curmon = curmon - depmon;
                                curYear = curYear - depyear;
                                curmon = (curYear * 360) / 30 + curmon;
                                installmentDue = installmentToPay;//CommonUtil.convertObjToDouble(new Integer(curmon)).doubleValue()-tot_Inst_paid;//-installmentToPay;
                                if ((DateUtil.dateDiff((Date) matDt, (Date) currDt) > 0)
                                        && !insBeyondMaturityDat.equals("") && insBeyondMaturityDat.equals("N")
                                        && !CommonUtil.convertObjToStr(getSourceScreen()).equals("DEPOSIT_CLOSING") && specialRD.equalsIgnoreCase("N")) {
                                    ClientUtil.showMessageWindow("Account Already Matured Can not Accept Installment Amounts !!! ");
                                    setTransDetails(null, null, null);
                                    clearTransFlag = true;
                                    return;
                                } else {
                                    clearTransFlag = false;
                                    //system.out.println("GG==="+DateUtil.dateDiff((Date)matDt,(Date)currDt));
                                    if (DateUtil.dateDiff((Date) matDt, (Date) currDt) > 0) {
                                        prematureDateMap.put("TO_DATE", matDt);
                                        prematureDateMap.put("FROM_DATE", lastMap.get("DEPOSIT_DT"));
                                        lst = ClientUtil.executeQuery("periodRunMap", prematureDateMap);
                                        if (lst != null && lst.size() > 0) {
                                            prematureDateMap = (HashMap) lst.get(0);
                                            monthPeriod = CommonUtil.convertObjToDouble(prematureDateMap.get("NO_OF_MONTHS")).doubleValue();
                                            actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
                                        }
                                        lst = null;
                                    } else {
                                        HashMap dailyProdID = new HashMap();
                                        dailyProdID.put("PID", accountMap.get("PROD_ID"));
                                        List dailyFrequency = ClientUtil.executeQuery("getDailyDepositFrequency", dailyProdID);
                                        if (dailyFrequency != null && dailyFrequency.size() > 0) {
                                            HashMap dailyFreq = new HashMap();
                                            dailyFreq = (HashMap) dailyFrequency.get(0);
                                            int freq = 0;
                                            String daily = CommonUtil.convertObjToStr(dailyFreq.get("DEPOSIT_FREQ"));
                                            freq = CommonUtil.convertObjToInt(daily);
                                            if (freq == 7) {
                                                int dep = depDt.getMonth() + 1;
                                                int curr = currDt.getMonth() + 1;
                                                int depYear = depDt.getYear() + 1900;
                                                int currYear = currDt.getYear() + 1900;
                                                HashMap hmap = new HashMap();
                                                String instalPendng = "";
                                                String depNo = acctNo;
                                                depNo = depNo.substring(0, depNo.lastIndexOf("_"));
                                                hmap.put("ACC_NUM", depNo);
                                                hmap.put("CURR_DT", currDt.clone());
                                                hmap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                                                //Added by chithra for mantis:10319: Weekly RD customisation for Ollukkara bank 
                                                List list = null;
                                                freq = 0;
                                                if (prodMap != null && prodMap.containsKey("BEHAVES_LIKE") && prodMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                                                    dailyProdID = new HashMap();
                                                    dailyProdID.put("PID", accountMap.get("PROD_ID"));
                                                    dailyFrequency = ClientUtil.executeQuery("getDailyDepositFrequency", dailyProdID);
                                                    if (dailyFrequency != null && dailyFrequency.size() > 0) {
                                                        dailyFreq = new HashMap();
                                                        dailyFreq = (HashMap) dailyFrequency.get(0);
                                                        daily = CommonUtil.convertObjToStr(dailyFreq.get("DEPOSIT_FREQ"));
                                                        freq = CommonUtil.convertObjToInt(daily);
                                                        if (freq == 7) {
                                                            list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUiForWeek", hmap);
                                                        } else {
                                                            //Added by sreekrishnan for mantis 10452
                                                            HashMap paramMap = new HashMap();
                                                            List paramList = ClientUtil.executeQuery("getSelectParameterForRdPendingCalc", hmap);
                                                            if (paramList != null && paramList.size() > 0) {
                                                                paramMap = (HashMap) paramList.get(0);
                                                                if (paramMap.containsKey("INCLUDE_FULL_MONTH") && !paramMap.get("INCLUDE_FULL_MONTH").equals("") && paramMap.get("INCLUDE_FULL_MONTH").equals("Y")) {
                                                                    list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUiWithMonthEnd", hmap);
                                                                } else {
                                                                    list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUi", hmap);
                                                                }
                                                            } else {
                                                                list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUi", hmap);
                                                            }
                                                        }
                                                    } else {
                                                        //Added by sreekrishnan for mantis 10452
                                                        HashMap paramMap = new HashMap();
                                                        List paramList = ClientUtil.executeQuery("getSelectParameterForRdPendingCalc", hmap);
                                                        if (paramList != null && paramList.size() > 0) {
                                                            paramMap = (HashMap) paramList.get(0);
                                                            if (paramMap.containsKey("INCLUDE_FULL_MONTH") && !paramMap.get("INCLUDE_FULL_MONTH").equals("") && paramMap.get("INCLUDE_FULL_MONTH").equals("Y")) {
                                                                list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUiWithMonthEnd", hmap);
                                                            } else {
                                                                list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUi", hmap);
                                                            }
                                                        } else {
                                                            list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUi", hmap);
                                                        }
                                                    }
                                                }
                                                if (list != null && list.size() > 0) {
                                                    hmap = (HashMap) list.get(0);
                                                    instalPendng = CommonUtil.convertObjToStr(hmap.get("PENDING"));
                                                    actualDelay = (long) CommonUtil.convertObjToInt(instalPendng);
                                                    actualDelay1 = actualDelay;
                                                }
                                                if (actualDelay >= 0) {
                                                    row = new ArrayList();
                                                    row.add(transRB.getString("Actual Delay"));
                                                    row.add(String.valueOf(actualDelay));
                                                    data.add(row);
                                                }
                                                //delayed installment calculation...
                                                if (DateUtil.dateDiff((Date) matDt, (Date) currDt) < 0 || insBeyondMaturityDat.equals("Y")) {
                                                    setPenalAmount(String.valueOf(0.0));
                                                    setPenalMonth(String.valueOf(0.0));
                                                    depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT")).doubleValue();
                                                    double chargeAmt = depAmt / 100;
                                                    if (actualDelay > irrdue) {
                                                        HashMap delayMap = new HashMap();
                                                        delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
                                                        delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
                                                        lst = ClientUtil.executeQuery("getSelectDelayedRate", delayMap);
                                                        if (lst != null && lst.size() > 0) {
                                                            delayMap = (HashMap) lst.get(0);
                                                            delayAmt = CommonUtil.convertObjToDouble(delayMap.get("PENAL_INT")).doubleValue();
                                                            delayAmt = delayAmt * chargeAmt;
                                                            tempDelayAmt = delayAmt;
                                                        }
                                                        lst = null;
                                                        double noInstallment = 0.0;
                                                        noInstallment = installmentDue - irrdue;
                                                        inst = tot_Inst_paid;
                                                        for (int i = 1; i <= noInstallment; i++) {
                                                            inst = inst + 1;
                                                            totalDelay = (curmon + 1 - inst) + totalDelay;
                                                        }
                                                        if (actualDelay1 != 0) {
                                                            delayAmt = delayAmt * installmentToPay;
                                                        } else {
                                                            delayAmt = delayAmt * totalDelay;
                                                        }
                                                    }
                                                    double oldPenalAmt = CommonUtil.convertObjToDouble(accountMap.get("DELAYED_AMOUNT")).doubleValue();
                                                    long oldPenalMonth = CommonUtil.convertObjToLong(accountMap.get("DELAYED_MONTH"));
                                                    double balanceAmt = 0.0;
                                                    if (oldPenalAmt > 0) {
                                                        balanceAmt = delayAmt - oldPenalAmt;
                                                        totalDelay = totalDelay - oldPenalMonth;
                                                    } else {
                                                        balanceAmt = delayAmt;
                                                    }
                                                    if (balanceAmt > 0) {
                                                        row = new ArrayList();
                                                        row.add(transRB.getString("Total Delayed Month"));
                                                        row.add(CommonUtil.convertObjToInt(String.valueOf(totalDelay + oldPenalMonth)));
                                                        data.add(row);
                                                        row = new ArrayList();
                                                        row.add(transRB.getString("Paid Month"));
                                                        row.add(CommonUtil.convertObjToInt(String.valueOf(oldPenalMonth)));
                                                        data.add(row);
                                                        row = new ArrayList();
                                                        row.add(transRB.getString("Paid Amount"));
                                                        row.add(CommonUtil.convertObjToInt(String.valueOf(oldPenalAmt)));
                                                        data.add(row);
                                                        row = new ArrayList();
                                                        row.add(transRB.getString("Delayed Month"));
                                                        row.add(CommonUtil.convertObjToInt(String.valueOf(totalDelay)));
                                                        data.add(row);
                                                        row = new ArrayList();
                                                        row.add(transRB.getString("Delayed Amount"));
                                                        HashMap mapDelayAmtRound = new HashMap();
                                                        mapDelayAmtRound.put("PROD_ID", accountMap.get("PROD_ID"));
                                                        List lstDelayAmtRound = ClientUtil.executeQuery("getSelDelayAmtRoundOff", mapDelayAmtRound);
                                                        mapDelayAmtRound = new HashMap();
                                                        if (lstDelayAmtRound.size() > 0 && lstDelayAmtRound != null) {
                                                            mapDelayAmtRound = (HashMap) lstDelayAmtRound.get(0);
                                                        }
                                                        String roundReq = CommonUtil.convertObjToStr(mapDelayAmtRound.get("PENAL_ROUNDOFF_REQ"));
                                                        round = CommonUtil.convertObjToStr(mapDelayAmtRound.get("PENAL_ROUNDOFF"));
                                                        setRound(round);
                                                        if (roundReq.equals("Y")) {
                                                            if (round.equals("NEAREST_VALUE")) {
                                                                balanceAmt = (double) getNearest((long) (balanceAmt * 100), 100) / 100;
                                                            } else if (round.equals("LOWER_VALUE")) {
                                                                balanceAmt = (double) roundOffLower((long) (balanceAmt * 100), 100) / 100;
                                                            } else if (round.equals("HIGHER_VALUE")) {
                                                                balanceAmt = (double) higher((long) (balanceAmt * 100), 100) / 100;
                                                            } else {
                                                                balanceAmt = new Double(balanceAmt);
                                                            }
                                                        }
                                                        //Added by chithra  on 13-06-14 to show Correct Penal amount
                                                        double cummInst = 0.0;
                                                        if (actualDelay == installmentToPay) {
                                                            cummInst = actualDelay * (actualDelay + 1) / 2;
                                                        } else {
                                                            double diff = actualDelay - installmentToPay;
                                                            cummInst = (actualDelay * (actualDelay + 1) / 2) - (diff * (diff + 1) / 2);
                                                        }
                                                        double penal = tempDelayAmt * cummInst;
                                                        row.add(CurrencyValidation.formatCrore(String.valueOf(penal)));
                                                        data.add(row);
                                                        setPenalAmount(String.valueOf(balanceAmt));
                                                        setPenalMonth(String.valueOf(totalDelay));
                                                    }
                                                }
                                            }else{
                                                //Added By Suresh Penal Calculation for RD
                                                List list = null;
                                                setPenalAmount(String.valueOf(0.0));
                                                setPenalMonth(String.valueOf(0.0));
                                                depDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("DEPOSIT_DT")));
                                                //added by rishad 02/mar/2017 for rd INCLUDE_FULL_MONTH (kottayam)
                                                if (dailyFreq.containsKey("INCLUDE_FULL_MONTH") && !dailyFreq.get("INCLUDE_FULL_MONTH").equals("") && dailyFreq.get("INCLUDE_FULL_MONTH").equals("Y")) {
                                                    String instalPendng = "";
                                                    HashMap hmap = new HashMap();
                                                    String depNo = acctNo;
                                                    depNo = depNo.substring(0, depNo.lastIndexOf("_"));
                                                    hmap.put("ACC_NUM", depNo);
                                                    hmap.put("CURR_DT", currDt.clone());
                                                    list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUiWithMonthEnd", hmap);
                                                    if (list != null && list.size() > 0) {
                                                        hmap = (HashMap) list.get(0);
                                                        instalPendng = CommonUtil.convertObjToStr(hmap.get("PENDING"));
                                                        actualDelay = (long) CommonUtil.convertObjToInt(instalPendng);
                                                        actualDelay1 = actualDelay;
                                                    }
                                                    if (actualDelay >= 0) {
                                                        row = new ArrayList();
                                                        row.add(transRB.getString("Actual Delay"));
                                                        row.add(String.valueOf(actualDelay));
                                                        data.add(row);
                                                    }
                                                    //delayed installment calculation...
                                                    if (DateUtil.dateDiff((Date) matDt, (Date) currDt) < 0 || insBeyondMaturityDat.equals("Y")) {
                                                        setPenalAmount(String.valueOf(0.0));
                                                        setPenalMonth(String.valueOf(0.0));
                                                        depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT")).doubleValue();
                                                        double chargeAmt = depAmt / 100;
                                                        if (actualDelay > irrdue) {
                                                            HashMap delayMap = new HashMap();
                                                            delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
                                                            delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
                                                            lst = ClientUtil.executeQuery("getSelectDelayedRate", delayMap);
                                                            if (lst != null && lst.size() > 0) {
                                                                delayMap = (HashMap) lst.get(0);
                                                                delayAmt = CommonUtil.convertObjToDouble(delayMap.get("PENAL_INT")).doubleValue();
                                                                delayAmt = delayAmt * chargeAmt;
                                                                tempDelayAmt = delayAmt;

                                                            }
                                                            lst = null;
                                                            double noInstallment = 0.0;
                                                            noInstallment = installmentDue - irrdue;
                                                            inst = tot_Inst_paid;
                                                            for (int i = 1; i <= noInstallment; i++) {
                                                                inst = inst + 1;
                                                                totalDelay = (curmon + 1 - inst) + totalDelay;
                                                            }
                                                            if (actualDelay1 != 0) {
                                                                delayAmt = delayAmt * installmentToPay;
                                                            } else {
                                                                delayAmt = delayAmt * totalDelay;
                                                            }
                                                        }
                                                        double oldPenalAmt = CommonUtil.convertObjToDouble(accountMap.get("DELAYED_AMOUNT")).doubleValue();
                                                        long oldPenalMonth = CommonUtil.convertObjToLong(accountMap.get("DELAYED_MONTH"));
                                                        double balanceAmt = 0.0;
                                                        if (oldPenalAmt > 0) {
                                                            balanceAmt = delayAmt - oldPenalAmt;
                                                            totalDelay = totalDelay - oldPenalMonth;
                                                        } else {
                                                            balanceAmt = delayAmt;
                                                        }
                                                        if (balanceAmt > 0) {
                                                            row = new ArrayList();
                                                            row.add(transRB.getString("Total Delayed Month"));
                                                            row.add(CommonUtil.convertObjToInt(String.valueOf(totalDelay + oldPenalMonth)));
                                                            data.add(row);
                                                            row = new ArrayList();
                                                            row.add(transRB.getString("Paid Month"));
                                                            row.add(CommonUtil.convertObjToInt(String.valueOf(oldPenalMonth)));
                                                            data.add(row);
                                                            row = new ArrayList();
                                                            row.add(transRB.getString("Paid Amount"));
                                                            row.add(CommonUtil.convertObjToInt(String.valueOf(oldPenalAmt)));
                                                            data.add(row);
                                                            row = new ArrayList();
                                                            row.add(transRB.getString("Delayed Month"));
                                                            row.add(CommonUtil.convertObjToInt(String.valueOf(totalDelay)));
                                                            data.add(row);
                                                            row = new ArrayList();
                                                            row.add(transRB.getString("Delayed Amount"));
                                                            HashMap mapDelayAmtRound = new HashMap();
                                                            mapDelayAmtRound.put("PROD_ID", accountMap.get("PROD_ID"));
                                                            List lstDelayAmtRound = ClientUtil.executeQuery("getSelDelayAmtRoundOff", mapDelayAmtRound);
                                                            mapDelayAmtRound = new HashMap();
                                                            if (lstDelayAmtRound.size() > 0 && lstDelayAmtRound != null) {
                                                                mapDelayAmtRound = (HashMap) lstDelayAmtRound.get(0);
                                                            }
                                                            String roundReq = CommonUtil.convertObjToStr(mapDelayAmtRound.get("PENAL_ROUNDOFF_REQ"));
                                                            round = CommonUtil.convertObjToStr(mapDelayAmtRound.get("PENAL_ROUNDOFF"));
                                                            setRound(round);
                                                            if (roundReq.equals("Y")) {
                                                                if (round.equals("NEAREST_VALUE")) {
                                                                    balanceAmt = (double) getNearest((long) (balanceAmt * 100), 100) / 100;
                                                                } else if (round.equals("LOWER_VALUE")) {
                                                                    balanceAmt = (double) roundOffLower((long) (balanceAmt * 100), 100) / 100;
                                                                } else if (round.equals("HIGHER_VALUE")) {
                                                                    balanceAmt = (double) higher((long) (balanceAmt * 100), 100) / 100;
                                                                } else {
                                                                    balanceAmt = new Double(balanceAmt);
                                                                }

                                                            }
                                                        //System.out.println("after bal amt[====" + balanceAmt);
                                                        //Added by chithra  on 13-06-14 to show Correct Penal amount
                                                        double cummInst = 0.0;
                                                        if (actualDelay == installmentToPay) {
                                                            cummInst = actualDelay * (actualDelay + 1) / 2;
                                                        } else {
                                                            double diff = actualDelay - installmentToPay;
                                                            cummInst = (actualDelay * (actualDelay + 1) / 2) - (diff * (diff + 1) / 2);
                                                        }
                                                        double penal = tempDelayAmt * cummInst;
                                                        row.add(CurrencyValidation.formatCrore(String.valueOf(penal)));
                                                        data.add(row);       
                                                        setPenalAmount(String.valueOf(penal));
                                                        setPenalMonth(String.valueOf(totalDelay));
                                                    }
                                                }
                                                    
                                                } else {
                                                    gracePeriod = CommonUtil.convertObjToInt(recurringMap.get("GRACE_PERIOD"));
                                                    //System.out.println("########## gracePeriod  : " + gracePeriod);
                                                    depDate = DateUtil.addDays(depDate, gracePeriod);
                                                    //System.out.println("########## After Grace Period DepDate  : " + depDate);
                                                    double chargeAmt = depAmt / 100;
                                                    //System.out.println("####### chargeAmt : " + chargeAmt);
                                                    HashMap penaltyMap = new HashMap();
                                                    HashMap penaltyMap1 = new HashMap();
                                                    penaltyMap.put("DEPOSIT_DT", setProperDtFormat(depDate));
                                                    penaltyMap.put("CURR_DT", setProperDtFormat((Date) currDt.clone()));
                                                    penaltyMap.put("FREQ", 1);
                                                    penaltyMap.put("PAID_INST", tot_Inst_paid);
                                                    List penaltyList = ClientUtil.executeQuery("getPenaltyForDeposit", penaltyMap);     //Based On Query Penaty Calculation
                                                    if (penaltyList != null && penaltyList.size() > 0) {
                                                        penaltyMap = (HashMap) penaltyList.get(0);
                                                        penaltyMap1 = (HashMap) penaltyList.get(0);
                                                        System.out.println("####### INST_TO_PAY : " + installmentToPay);
                                                        if (CommonUtil.convertObjToInt(penaltyMap.get("NO_OF_PENDING_INST")) > 0) {
                                                            if (CommonUtil.convertObjToInt(installmentToPay) > 0) {
                                                                penaltyMap.put("DEPOSIT_DT", setProperDtFormat(depDate));
                                                                penaltyMap.put("CURR_DT", setProperDtFormat((Date) currDt.clone()));
                                                                penaltyMap.put("FREQ", 1);
                                                                penaltyMap.put("PAID_INST", tot_Inst_paid);
                                                                penaltyMap.put("INST_TO_PAY", CommonUtil.convertObjToInt(installmentToPay));
                                                                penaltyList = ClientUtil.executeQuery("getPenaltyForDepositToPay", penaltyMap); //Based On Inst_To_Pay it will calculate Delay Months
                                                                if (penaltyList != null && penaltyList.size() > 0) {
                                                                    penaltyMap = (HashMap) penaltyList.get(0);
                                                                    row = new ArrayList();
                                                                    row.add(transRB.getString("Actual Delay"));
                                                                    row.add(String.valueOf(penaltyMap.get("NO_OF_PENDING_INST")));
                                                                    data.add(row);

                                                                    row = new ArrayList();
                                                                    row.add(transRB.getString("Total Delayed Month"));
                                                                    row.add(CurrencyValidation.formatCrore(String.valueOf(penaltyMap1.get("DELAY_MONTHS"))));
                                                                    data.add(row);

                                                                    row = new ArrayList();
                                                                    row.add("Pending No of Months Delay to Pay");
                                                                    row.add(CurrencyValidation.formatCrore(String.valueOf(penaltyMap.get("DELAY_MONTHS"))));
                                                                    data.add(row);
                                                                    
                                                                    if (rdClosingPenalRequired.equalsIgnoreCase("N")) {// Added by nithya on 04-09-2019
                                                                        HashMap delayMap = new HashMap();
                                                                        delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
                                                                        delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
                                                                        lst = ClientUtil.executeQuery("getSelectDelayedRate", delayMap);
                                                                        if (lst != null && lst.size() > 0) {
                                                                            delayMap = (HashMap) lst.get(0);
                                                                            System.out.println("####### delayMap : " + delayMap);
                                                                            delayAmt = CommonUtil.convertObjToDouble(delayMap.get("ROI")).doubleValue();
                                                                            delayAmt = delayAmt * chargeAmt;
                                                                            //System.out.println("####### delayAmt*chargeAmt : " + delayAmt);
                                                                        }
                                                                        delayAmt = delayAmt * CommonUtil.convertObjToDouble(penaltyMap.get("DELAY_MONTHS"));
                                                                        delayAmt = (double) getNearest((long) (delayAmt * 100), 100) / 100;
                                                                    }
                                                                    System.out.println("####### PENAL : " + delayAmt);
                                                                    if (delayAmt > 0.0) {
                                                                        row = new ArrayList();
                                                                        row.add(transRB.getString("Delayed Amount"));
                                                                        row.add(CurrencyValidation.formatCrore(String.valueOf(delayAmt)));
                                                                        data.add(row);
                                                                        setPenalAmount(String.valueOf(delayAmt));
                                                                        setPenalMonth(String.valueOf(penaltyMap.get("DELAY_MONTHS")));
                                                                    }
                                                                }
                                                            } else {
                                                                row = new ArrayList();
                                                                row.add(transRB.getString("Actual Delay"));
                                                                row.add(String.valueOf(penaltyMap.get("NO_OF_PENDING_INST")));
                                                                data.add(row);

                                                                row = new ArrayList();
                                                                row.add(transRB.getString("Total Delayed Month"));
                                                                row.add(CurrencyValidation.formatCrore(String.valueOf(penaltyMap.get("DELAY_MONTHS"))));
                                                                data.add(row);

                                                                row = new ArrayList();
                                                                row.add("Pending No of Months Delay to Pay");
                                                                row.add(CurrencyValidation.formatCrore(String.valueOf(penaltyMap.get("DELAY_MONTHS"))));
                                                                data.add(row);
                                                                
                                                                if (rdClosingPenalRequired.equalsIgnoreCase("N")) {
                                                                    HashMap delayMap = new HashMap();
                                                                    delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
                                                                    delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
                                                                    lst = ClientUtil.executeQuery("getSelectDelayedRate", delayMap);
                                                                    if (lst != null && lst.size() > 0) {
                                                                        delayMap = (HashMap) lst.get(0);
                                                                        System.out.println("####### delayMap : " + delayMap);
                                                                        delayAmt = CommonUtil.convertObjToDouble(delayMap.get("ROI")).doubleValue();
                                                                        delayAmt = delayAmt * chargeAmt;
                                                                        //System.out.println("####### delayAmt*chargeAmt : " + delayAmt);
                                                                    }
                                                                    delayAmt = delayAmt * CommonUtil.convertObjToDouble(penaltyMap.get("DELAY_MONTHS"));
                                                                    delayAmt = (double) getNearest((long) (delayAmt * 100), 100) / 100;
                                                                }
                                                                System.out.println("####### PENAL : " + delayAmt);
                                                                if (delayAmt > 0.0) {
                                                                    row = new ArrayList();
                                                                    row.add(transRB.getString("Delayed Amount"));
                                                                    row.add(CurrencyValidation.formatCrore(String.valueOf(delayAmt)));
                                                                    data.add(row);
                                                                    setPenalAmount(String.valueOf(delayAmt));
                                                                    setPenalMonth(String.valueOf(penaltyMap.get("DELAY_MONTHS")));
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                               
                                            }
                                        }
                                    }
                                }
                            }
                            lst = null;
                        }
                    }
                    lstBehave = null;
                }
                lst = null;
            }
            if (prodType != null && (prodType.equals("TL") || prodType.equals("ATL") || prodType.equals("AAD") || prodType.equals("AD") || prodType.equals("BILLS"))) {
                row = new ArrayList();
                row.add(transRB.getString("Limit"));
                sanctionAmount = CommonUtil.convertObjToStr((resultMap.get("LIMIT") == null) ? "0" : resultMap.get("LIMIT").toString());
                setLimitAmount(CurrencyValidation.formatCrore((resultMap.get("LIMIT") == null) ? "0" : resultMap.get("LIMIT").toString()));
                //system.out.println("getLimitAmount#####"+getLimitAmount());
                setIsMultiDisburse(CommonUtil.convertObjToStr(resultMap.get("MULTI_DISBURSE")));
                row.add(getLimitAmount());
                data.add(row);
                setColour();
                if (prodType != null && (prodType.equals("AAD") || prodType.equals("ATL"))) {
                    String subLimit = CommonUtil.convertObjToStr((insertPenal.get("SUBLIMIT") == null) ? "0.00" : insertPenal.get("SUBLIMIT"));
                    if (!subLimit.equals("0.00")) {
                        row = new ArrayList();
                        row.add(transRB.getString("Sub Limit"));
                        row.add(subLimit);
                        data.add(row);
                    }
                }

                if (prodType.equals("AD") || prodType.equals("AAD") || prodType.equals("BILLS")) {
                    //                    if (prodType.equals("AD")){
                    //                        double dpAmount = 0.0;
                    //                        HashMap advanceMap = new HashMap();
                    //                        advanceMap.put("ACCT_NUM",AccountNo);
                    ////                        List lst = ClientUtil.executeQuery("getSelectAuthStatusofDP", advanceMap);
                    ////                        if(lst!=null && lst.size()>0){
                    ////                            advanceMap = (HashMap)lst.get(0);
                    ////                            String authStatus = CommonUtil.convertObjToStr(advanceMap.get("AUTHORIZE_STATUS"));
                    ////                            if(authStatus.equals("")){
                    ////                                advanceMap = new HashMap();
                    ////                                advanceMap.put("ACT_NUM",AccountNo);
                    ////                                lst = ClientUtil.executeQuery("getSelectAuthStatusNullAmtofDP", advanceMap);
                    ////                                if(lst!=null && lst.size()>0){
                    ////                                    advanceMap = (HashMap)lst.get(0);
                    ////                                    dpAmount = CommonUtil.convertObjToDouble(advanceMap.get("DRAWING_POWER")).doubleValue();
                    ////                                }
                    ////                            }else
                    ////                            if(authStatus.equals("AUTHORIZED")){
                    //                                advanceMap = new HashMap();
                    //                                advanceMap.put("ACT_NUM",AccountNo);
                    //                                List lst = ClientUtil.executeQuery("getSelectAuthStatusNotNullofDP", advanceMap);
                    //                                if(lst!=null && lst.size()>0){
                    //                                    advanceMap = (HashMap)lst.get(0);
                    //                                    dpAmount = CommonUtil.convertObjToDouble(advanceMap.get("DRAWING_POWER")).doubleValue();
                    //                                }
                    ////                            }
                    //                            if(dpAmount >0){
                    //                                row = new ArrayList();
                    //                                row.add(transRB.getString("DrawingPower"));
                    //                                row.add(CurrencyValidation.formatCrore( (advanceMap.get("DRAWING_POWER")==null) ? "0" : advanceMap.get("DRAWING_POWER").toString() ));
                    //                                data.add(row);
                    //                            }
                    ////                        }
                    //                        advanceMap = null;
                    //                        lst = null;
                    //                    }
                    //                    if(prodType.equals("BILLS")) {
                    row = new ArrayList();
                    row.add(transRB.getString("DrawingPower"));
                    row.add(CurrencyValidation.formatCrore((resultMap.get("DRAWING_POWER") == null) ? "0" : resultMap.get("DRAWING_POWER").toString()));
                    data.add(row);
                    //                    }
                }
            }

            if (prodType != null && (prodType.equals("TL") || prodType.equals("ATL"))) {
                row = new ArrayList();
                row.add(transRB.getString("AvailableBalance"));
                row.add(CurrencyValidation.formatCrore(getAvBalance()));
                data.add(row);
                row = new ArrayList();
                row.add(transRB.getString("LoanBalance"));
            } else {
                row = new ArrayList();
                row.add(transRB.getString("AvailableBalance"));
            }
            if (prodType != null && (prodType.equals("AD") || prodType.equals("AAD") || prodType.equals("BILLS"))) {
                row.add(CurrencyValidation.formatCrore(getAvBalance()));
            } else {
                row.add(CurrencyValidation.formatCrore(getAvailableBalance()));
            }
            data.add(row);

            if (prodType == null || prodType.length() > 0) {
                row = new ArrayList();
                String tempClearBalance = clearBalance;
                tempClearBalance = tempClearBalance.replaceAll(",", "");
                row.add(transRB.getString("ClearBalance"));
                double tempClearBalancedouble = CommonUtil.convertObjToDouble(tempClearBalance).doubleValue();
                // Added currency validation by nithya on 24-04-2019 for KD-49 Cash Format Change - In Cash Screen
                if (clearBalance != null && tempClearBalancedouble < 0 && prodType != null && (prodType.equals("AD") || prodType.equals("TL"))) {
                    //row.add((tempClearBalancedouble * -1) + "  Dr");
                    row.add(CurrencyValidation.formatCrore(String.valueOf(tempClearBalancedouble * -1)) + "  Dr");
                } else if (clearBalance != null && tempClearBalancedouble > 0 && prodType != null && (prodType.equals("AD") || prodType.equals("TL"))) {
                    //row.add(clearBalance + "  Cr");
                    row.add(CurrencyValidation.formatCrore(clearBalance) + "  Cr");
                } else {
                    //row.add(clearBalance);
                    row.add(CurrencyValidation.formatCrore(clearBalance));
                }
                data.add(row);

                row = new ArrayList();
                row.add(transRB.getString("UnclearBalance"));
                row.add(unclearBalance);
                data.add(row);



            }

            if (prodType != null && (prodType.equals("AD") || prodType.equals("TL"))) {
                if (!subsidyAvailableBalance.equals("0.00")) {
                    row = new ArrayList();
                    row.add(transRB.getString("AvailableSubsidy"));
                    row.add(subsidyAvailableBalance);
                    data.add(row);
                }
                if (!penalWaivedAmt.equals("0.00")) {
                    row = new ArrayList();
                    row.add(transRB.getString("WaivedPenal"));
                    row.add(penalWaivedAmt);
                    data.add(row);

                }
                if (!interestWaivedAmt.equals("0.00")) {
                    row = new ArrayList();
                    row.add(transRB.getString("WaivedInterest"));
                    row.add(interestWaivedAmt);
                    data.add(row);

                }
                if (penalWaivedDate != null) {
                    row = new ArrayList();
                    row.add(transRB.getString("penalWaivedDate"));
                    row.add(CommonUtil.convertObjToStr(penalWaivedDate));
                    data.add(row);

                }

            }
            row = new ArrayList();
            row.add(transRB.getString("TotalBalance"));
            String tempTotalBalance = totalBalance;
            tempTotalBalance = tempTotalBalance.replaceAll(",", "");
            double tempTotalBalancedouble = CommonUtil.convertObjToDouble(tempTotalBalance).doubleValue();
            // Added currency validation by nithya on 24-04-2019 for KD-49 Cash Format Change - In Cash Screen
            if (totalBalance != null && tempTotalBalancedouble < 0 && prodType != null && (prodType.equals("AD") || prodType.equals("TL"))) {
                //row.add((tempTotalBalancedouble * -1) + "  Dr");
                row.add(CurrencyValidation.formatCrore(String.valueOf(tempTotalBalancedouble * -1)) + "  Dr");
            } else if (totalBalance != null && tempTotalBalancedouble > 0 && prodType != null && (prodType.equals("AD") || prodType.equals("TL"))) {
                //row.add(totalBalance + "  Cr");
                row.add(CurrencyValidation.formatCrore(totalBalance) + "  Cr");
            } else {
                //row.add(totalBalance);
                row.add(CurrencyValidation.formatCrore(totalBalance));
            }
            data.add(row);
            if (prodType != null && prodType.equals("TD")) {
                setDepositAmt(CommonUtil.convertObjToStr(resultMap.get("TOTAL_BALANCE")));
            }

            if (prodType == null || !prodType.equals("TL") && !prodType.equals("AD") && !prodType.equals("AAD")) {
                row = new ArrayList();
                row.add(transRB.getString("ShadowCredit"));
                row.add(CurrencyValidation.formatCrore(getShadowCredit()));
                data.add(row);

                row = new ArrayList();
                row.add(transRB.getString("ShadowDebit"));
                row.add(CurrencyValidation.formatCrore(getShadowDebit()));
                data.add(row);
            } else {
                //System.out.println("asAndWhenMap --->2222---"+asAndWhenMap+"insertPenal  -->"+insertPenal);
                if (insertPenal.containsKey("LAST_INT_CALC_DT") && insertPenal.get("LAST_INT_CALC_DT") != null) {
                    row = new ArrayList();
                    row.add("Last Int Calc Dt");
                    row.add(DateUtil.getStringDate((Date) insertPenal.get("LAST_INT_CALC_DT")));
                    data.add(row);
                }
                if (insertPenal.containsKey("ROI") && insertPenal.get("ROI") != null) {
                    row = new ArrayList();
                    row.add("Interest Rate");
                    row.add(CurrencyValidation.formatCrore(String.valueOf(insertPenal.get("ROI"))));
                    data.add(row);
                }
            }

            double todAmt = CommonUtil.convertObjToDouble(getTodAmount()).doubleValue();
            if (todAmt > 0) {
                row = new ArrayList();
                row.add(transRB.getString("todAmount"));
                row.add(CurrencyValidation.formatCrore(getTodAmount()));
                data.add(row);
            }
            if (todAmt > 0) {
                row = new ArrayList();
                row.add(transRB.getString("todUtilized"));
                row.add(CurrencyValidation.formatCrore(getTodUtilized()));
                data.add(row);
            }
            double clearBal = 0;
            if (clearBalance != null) {
                clearBalance = clearBalance.replaceAll(",", "");

                clearBal = Double.parseDouble(clearBalance);
            }
            if (prodType != null && (prodType.equals("TL") || prodType.equals("ATL") || prodType.equals("AAD") || prodType.equals("AD") || prodType.equals("BILLS"))) {
                String emi = "0.00";
                //                    if(clearBal<0) { bank database want to commanted
                TermLoanCloseCharge = new HashMap();
                calaclatedIntChargeMap = new HashMap();
                //system.out.println("insertPenal######"+insertPenal);
                if (insertPenal.containsKey("PREMATURE")) {
                    if (insertPenal.containsKey("PAID_INTEREST")
                            && CommonUtil.convertObjToDouble(insertPenal.get("PAID_INTEREST")).doubleValue() > 0) {
                        row = new ArrayList();
                        row.add("Net Interest");
                        row.add(insertPenal.get("INTEREST"));
                        data.add(row);
                        row = new ArrayList();
                        row.add("Paid Interest");
                        row.add(insertPenal.get("PAID_INTEREST"));
                        data.add(row);
                    }
                }
                TermLoanCloseCharge.put("PENAL_WAIVER", insertPenal.get("PENAL_WAIVER"));
                TermLoanCloseCharge.put("INTEREST_WAIVER", insertPenal.get("INTEREST_WAIVER"));
                 //added by rishad 24/04/2015
                TermLoanCloseCharge.put("PRINCIPAL_WAIVER", insertPenal.get("PRINCIPAL_WAIVER"));
                TermLoanCloseCharge.put("ARC_WAIVER", insertPenal.get("ARC_WAIVER"));
                TermLoanCloseCharge.put("NOTICE_WAIVER", insertPenal.get("NOTICE_WAIVER"));
                TermLoanCloseCharge.put("EP_COST_WAIVER", insertPenal.get("EP_COST_WAIVER"));
                TermLoanCloseCharge.put("POSTAGE_WAIVER", insertPenal.get("POSTAGE_WAIVER"));
                TermLoanCloseCharge.put("ADVERTISE_WAIVER", insertPenal.get("ADVERTISE_WAIVER"));
                TermLoanCloseCharge.put("LEGAL_WAIVER", insertPenal.get("LEGAL_WAIVER"));
                TermLoanCloseCharge.put("INSURANCE_WAIVER", insertPenal.get("INSURANCE_WAIVER"));
                TermLoanCloseCharge.put("MISCELLANEOUS_WAIVER", insertPenal.get("MISCELLANEOUS_WAIVER"));
                TermLoanCloseCharge.put("DECREE_WAIVER", insertPenal.get("DECREE_WAIVER"));
                TermLoanCloseCharge.put("ARBITRARY_WAIVER", insertPenal.get("ARBITRARY_WAIVER"));
                TermLoanCloseCharge.put("ACCT_NUM", insertPenal.get("ACCT_NUM"));
                if(insertPenal.containsKey("REBATE_MODE")&& insertPenal.get("REBATE_MODE")!=null){
                 TermLoanCloseCharge.put("REBATE_MODE", insertPenal.get("REBATE_MODE"));}
                TermLoanCloseCharge.put("LOAN_BALANCE_PRINCIPAL", resultMap.get("AVAILABLE_BALANCE"));
                TermLoanCloseCharge.put("CLEAR_BALANCE", resultMap.get("CLEAR_BALANCE"));
                calaclatedIntChargeMap.put("LOAN_BALANCE_PRINCIPAL", resultMap.get("AVAILABLE_BALANCE"));
                calaclatedIntChargeMap.put("CLEAR_BALANCE", resultMap.get("CLEAR_BALANCE"));
                if (subsidyAvailableBalance != null && !subsidyAvailableBalance.equals("0.00")) {
                    subsidyAvailableBalance = subsidyAvailableBalance.replaceAll(",", "");
                    TermLoanCloseCharge.put("AVAILABLE_SUBSIDY", new Double(subsidyAvailableBalance));
                    //totalRecievable -= new Double(subsidyAvailableBalance).doubleValue();
                }
                //                changed for ordering purpose by nikhil
                row = new ArrayList();
                row.add(transRB.getString("Currprince"));

                String prince = CommonUtil.convertObjToStr((insertPenal.get("CURR_MONTH_PRINCEPLE") == null) ? "0.00" : insertPenal.get("CURR_MONTH_PRINCEPLE"));
                double debitPrincipal = Double.parseDouble(getAvailableBalance());
                double actualDue = Double.parseDouble(prince);
                //if debit principal is less then actual installment due
                if (debitPrincipal < actualDue) {
                    prince = String.valueOf(debitPrincipal);
                }
//                TermLoanCloseCharge.put("CURR_MONTH_PRINCEPLE",new Double(prince));
                calaclatedIntChargeMap.put(transRB.getString("Currprince"), new Double(prince));
                if (inst_dt != null) {
                    if (DateUtil.dateDiff((Date) currDt.clone(), inst_dt) <= 0) {
                        if (!prince.equals("0.00") && debitPrincipal > 0) {
                            row.add(CurrencyValidation.formatCrore(String.valueOf(prince)));
                            totalRecievable += CommonUtil.convertObjToDouble(prince).doubleValue();
                            data.add(row);
                            TermLoanCloseCharge.put("CURR_MONTH_PRINCEPLE", new Double(prince));
                        } else {
                            row.add("0.00");
                            data.add(row);
                            TermLoanCloseCharge.put("CURR_MONTH_PRINCEPLE", new Double(0.00));
                        }
                    } else {
                        TermLoanCloseCharge.put("CURR_MONTH_PRINCEPLE", new Double(0.00));
                    }
                }else {
                    row.add("0.00");
                    data.add(row);
//                    TermLoanCloseCharge.put("CURR_MONTH_PRINCEPLE", new Double(0.00));
                }

                emi = CommonUtil.convertObjToStr((insertPenal.get("CURR_MONTH_INT") == null) ? "0.00" : insertPenal.get("CURR_MONTH_INT"));
                if (prodType != null && prodType.equals("TL")) {
                    TermLoanCloseCharge.put("PRINCIPLE_BAL", insertPenal.get("PRINCIPLE_BAL"));
                    TermLoanCloseCharge.put("INSTALL_DT", insertPenal.get("INSTALL_DT"));
                    calaclatedIntChargeMap.put("PRINCIPLE_BAL", insertPenal.get("PRINCIPLE_BAL"));
                    calaclatedIntChargeMap.put("INSTALL_DT", insertPenal.get("INSTALL_DT"));
                }
                TermLoanCloseCharge.put("CURR_MONTH_INT", insertPenal.get("CURR_MONTH_INT"));
                calaclatedIntChargeMap.put("CURR_MONTH_INT", insertPenal.get("CURR_MONTH_INT"));
                
                if (prodType != null && (prodType.equals("TL") || prodType.equals("AD"))) {
                    TermLoanCloseCharge.put("REBATE_INTEREST", insertPenal.get("REBATE_INTEREST"));
                    TermLoanCloseCharge.put("REBATE_INTEREST_UPTO", insertPenal.get("REBATE_INTEREST_UPTO"));
                    calaclatedIntChargeMap.put("REBATE_INTEREST", insertPenal.get("REBATE_INTEREST"));
                    if (penalWaivedDate != null) {
                        row = new ArrayList();
                        row.add(transRB.getString("penalWaivedDate"));
                        row.add(CommonUtil.convertObjToStr(penalWaivedDate));
                        data.add(row);
                    }

                    if (uptoRebatePaidDt != null) {
                        row = new ArrayList();
                        row.add(transRB.getString("uptoRebatePaidDt"));
                        row.add(CommonUtil.convertObjToStr(uptoRebatePaidDt));
                        data.add(row);
                    }

                    if (paidRebateInterest != null) {
                        row = new ArrayList();
                        row.add(transRB.getString("paidRebateInterest"));
                        row.add(CommonUtil.convertObjToStr(paidRebateInterest));
                        data.add(row);
                    }

                    String penalR = CommonUtil.convertObjToStr((insertPenal.get("PENAL_INT") == null) ? "0.00" : insertPenal.get("PENAL_INT"));                    
                    if (CommonUtil.convertObjToDouble(penalR) <= 0) {
                      //  row = new ArrayList();
                        HashMap hmap = new HashMap();
                        String acNo = CommonUtil.convertObjToStr(insertPenal.get("ACCT_NUM"));
                        hmap.put("ACCT_NUM", acNo);
                        List list = ClientUtil.executeQuery("getRebateAllowedForActnum", hmap);
                        hmap = null;
                        if (list != null && list.size() > 0) {
                            hmap = (HashMap) list.get(0);
                            String rebateCalculation = CommonUtil.convertObjToStr(hmap.get("REBATE_CALCULATION"));
                            String rebatePeriod = CommonUtil.convertObjToStr(hmap.get("REBATE_PERIOD"));
                            int finDD = CommonUtil.convertObjToInt(hmap.get("FIN_YEAR_START_DD"));
                            int finMM = CommonUtil.convertObjToInt(hmap.get("FIN_YEAR_START_MM"));
                            String rebateAllowed = CommonUtil.convertObjToStr(hmap.get("REBATE_ALLOWED"));
                            String rebatePercentage = CommonUtil.convertObjToStr(hmap.get("REBATE_PERCENTAGE"));
                            double intper = CommonUtil.convertObjToDouble(hmap.get("INTEREST"));
                            String spl_Rebat =  CommonUtil.convertObjToStr(hmap.get("INT_RATE_REBATE"));
                            double loanIntpercentage = CommonUtil.convertObjToDouble(hmap.get("LOAN_INT_PERCENT")); // Added by nithya on 09-01-2020 for KD-1234
                            //System.out.println("spl_Rebat -- :"+spl_Rebat);
                            String Y = "Y";
                            if (rebateAllowed.equals(Y)) {
                                if (rebateCalculation != null && rebateCalculation.equalsIgnoreCase("Monthly calculation")&&!spl_Rebat.equals("Y")) {
                                    //System.out.println("bbbbb");
//                                    double rAmt = CommonUtil.convertObjToDouble(insertPenal.get("INTEREST")) * intper / 100;
                                     double rAmt = CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("CURR_MONTH_INT")) * intper / 100;
                                    if (rAmt > 0) {
                                        rAmt = (double) getNearest((long) (rAmt * 100), 100) / 100;
                                        asAndWhenMap.put("REBATE_INTEREST", rAmt);
                                        insertPenal.put("REBATE_INTEREST", rAmt);
                                        TermLoanCloseCharge.put("REBATE_INTEREST", insertPenal.get("REBATE_INTEREST"));
                                         TermLoanCloseCharge.put("REBATE_PERCENT", intper);
                                        TermLoanCloseCharge.put("REBATE_INTEREST_UPTO", insertPenal.get("REBATE_INTEREST_UPTO"));
                                        TermLoanCloseCharge.put("REBATE_CALCULATION", "Monthly calculation");
                                        TermLoanCloseCharge.put("SPL_REBATE",spl_Rebat);// Added by nithya on 10-01-2020 for KD-1234
                                        calaclatedIntChargeMap.put("REBATE_INTEREST", insertPenal.get("REBATE_INTEREST"));
                                    }
                                } else if (rebateCalculation != null && rebateCalculation.equalsIgnoreCase("Monthly calculation") && spl_Rebat.equals("Y")) {
                                    if (insertPenal.containsKey("ROI") && insertPenal.get("ROI") != null) {
                                        String intRt = CurrencyValidation.formatCrore(String.valueOf(insertPenal.get("ROI")));
//                                        double rAmt = CommonUtil.convertObjToDouble(insertPenal.get("INTEREST")) / CommonUtil.convertObjToDouble(intRt);
                                        //double rAmt = CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("CURR_MONTH_INT")) / CommonUtil.convertObjToDouble(intRt);
                                        double rAmt = (CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("CURR_MONTH_INT")) *loanIntpercentage) / CommonUtil.convertObjToDouble(intRt); // Added by nithya on 09-01-2020 for KD-1234
                                        if (rAmt > 0) {
                                            rAmt = (double) getNearest((long) (rAmt * 100), 100) / 100;
                                            asAndWhenMap.put("REBATE_INTEREST", rAmt);
                                            insertPenal.put("REBATE_INTEREST", rAmt);
                                            TermLoanCloseCharge.put("REBATE_CALCULATION", "Monthly calculation");
                                            //TermLoanCloseCharge.put("REBATE_PERCENT", intper);// Commented by nithya on 10-01-2020 for KD-1234
                                            TermLoanCloseCharge.put("REBATE_PERCENT", intRt);// Added by nithya on 10-01-2020 for KD-1234
                                            TermLoanCloseCharge.put("REBATE_INTEREST", insertPenal.get("REBATE_INTEREST"));
                                            TermLoanCloseCharge.put("REBATE_INTEREST_UPTO", insertPenal.get("REBATE_INTEREST_UPTO"));
                                            TermLoanCloseCharge.put("SPL_REBATE",spl_Rebat);// Added by nithya on 10-01-2020 for KD-1234
                                            TermLoanCloseCharge.put("LOAN_INT_PERCENT", loanIntpercentage);// Added by nithya on 10-01-2020 for KD-1234
                                            calaclatedIntChargeMap.put("REBATE_INTEREST", insertPenal.get("REBATE_INTEREST"));
                                        }
                                    }
                                }
                            }
                        }

                    }
                      row = new ArrayList();
                    row.add("Available Rebate Interest");
                    row.add(insertPenal.get("REBATE_INTEREST"));
                    data.add(row);

                }

                TermLoanCloseCharge.put("INTEREST", insertPenal.get("INTEREST"));
                TermLoanCloseCharge.put("LOAN_CLOSING_PENAL_INT", insertPenal.get("LOAN_CLOSING_PENAL_INT"));
                calaclatedIntChargeMap.put("INTEREST", insertPenal.get("INTEREST"));
                calaclatedIntChargeMap.put("LOAN_CLOSING_PENAL_INT", insertPenal.get("LOAN_CLOSING_PENAL_INT"));
                //system.out.println("TermLoanCloseCharge######"+TermLoanCloseCharge);
                //system.out.println("intAmtMAP"+insertPenal.get("CURR_MONTH_INT")+"EMI"+emi);
                if (CommonUtil.convertObjToDouble(emi).doubleValue() > 0) {
                    row = new ArrayList();
                    row.add(transRB.getString("CurrAmt"));
                    row.add(CurrencyValidation.formatCrore(String.valueOf(emi)));
                    calaclatedIntChargeMap.put(transRB.getString("CurrAmt"), CommonUtil.convertObjToDouble(emi));
                    totalRecievable += CommonUtil.convertObjToDouble(emi).doubleValue();
                    data.add(row);
                }
                
                // Added by nithya for 0008470 : overdue interest for EMI Loans
                if (prodType.equals("TL")) {
                    if (insertPenal.containsKey("EMI_OVERDUE_CHARGE") && insertPenal.get("EMI_OVERDUE_CHARGE") != null) {
                        String emiDueCharge = CommonUtil.convertObjToStr((insertPenal.get("EMI_OVERDUE_CHARGE") == null) ? "0.00" : insertPenal.get("EMI_OVERDUE_CHARGE"));
                        if (CommonUtil.convertObjToDouble(emiDueCharge).doubleValue() >= 0.0) {
                            TermLoanCloseCharge.put("EMI_OVERDUE_CHARGE", insertPenal.get("EMI_OVERDUE_CHARGE"));
                            calaclatedIntChargeMap.put("EMI_OVERDUE_CHARGE", insertPenal.get("EMI_OVERDUE_CHARGE"));
                            row = new ArrayList();
                            row.add("Overdue Interest");
                            row.add(CurrencyValidation.formatCrore(String.valueOf(emiDueCharge)));
                            totalRecievable += CommonUtil.convertObjToDouble(emiDueCharge).doubleValue();
                            data.add(row);
                        }

                    }
                }                
                // End
                
                //babu added 11-12-2014
                String penal = CommonUtil.convertObjToStr((insertPenal.get("PENAL_INT") == null) ? "0.00" : insertPenal.get("PENAL_INT"));
            	String prevBalance = CommonUtil.convertObjToStr((insertPenal.get("PREV_BALANCE_AMT") == null) ? "0.00" : insertPenal.get("PREV_BALANCE_AMT"));
            	//system.out.println(prevBalance+"prevBalanceifgetpenal"+penal);
                  if (prodType != null) //  changed  by jithin to show penal eventhough value is zero.      //   if((! penal.equals("0.00"))&& (prodType.equals("TL")   || prodType.equals("AD") || prodType.equals("BILLS"))) {   
            	  {
                    if ((prodType.equals("TL") || prodType.equals("AD") || prodType.equals("BILLS"))) {
                      TermLoanCloseCharge.put("PENAL_INT", insertPenal.get("PENAL_INT"));
                      calaclatedIntChargeMap.put(transRB.getString("penalAmt"), insertPenal.get("PENAL_INT"));
                      if (CommonUtil.convertObjToDouble(penal).doubleValue() >= 0.0) {
                        row = new ArrayList();
                        row.add(transRB.getString("penalAmt"));
                        row.add(CurrencyValidation.formatCrore(String.valueOf(penal)));
                        totalRecievable += CommonUtil.convertObjToDouble(penal).doubleValue();
                        data.add(row);
                      }
                      if (!prevBalance.equals("0.00")) {
                        row = new ArrayList();
                        row.add(transRB.getString("prevBalance"));
                        row.add(CurrencyValidation.formatCrore(String.valueOf(prevBalance)));
                        totalRecievable += CommonUtil.convertObjToDouble(prevBalance).doubleValue();
                        data.add(row);
                      }
                    //                    row=new ArrayList();
                    //                    row.add(transRB.getString("prevInterest"));
                    //                    data.add(row);
                    //                row=new ArrayList();
                    //                row.add(transRB.getString("overDue"));
                    //                row.add(overDue);
                    //                data.add(row);
                    //                insertPenal = null;

                }
            }
                if (insertPenal.containsKey("MORATORIUM_INT_FOR_EMI")) {
                    TermLoanCloseCharge.put("MORATORIUM_INT_FOR_EMI", insertPenal.get("MORATORIUM_INT_FOR_EMI"));
                    //                    calaclatedIntChargeMap.put(transRB.getString("CurrAmt"),insertPenal.get("MORATORIUM_INT_FOR_EMI"));
                }
            }
            double flexiAmt = CommonUtil.convertObjToDouble(getFlexiDepositAmt()).doubleValue();
            if (prodType != null && prodType.equals("OA") && flexiAmt > 0) {
                row = new ArrayList();
                row.add(transRB.getString("flexiDeposit"));
                row.add(CurrencyValidation.formatCrore(getFlexiDepositAmt()));
                data.add(row);
                double flexiLienAmt = 0.0;
                HashMap flexiMap = new HashMap();
                flexiMap.put("LIEN_AC_NO", AccountNo);
                List lst = ClientUtil.executeQuery("getDetailsForSBLienAct", flexiMap);
                if (lst != null && lst.size() > 0) {
                    flexiMap = (HashMap) lst.get(0);
                    flexiLienAmt = CommonUtil.convertObjToDouble(flexiMap.get("SUM(LIEN_AMOUNT)")).doubleValue();
                    if (flexiLienAmt > 0) {
                        row = new ArrayList();
                        row.add(transRB.getString("flexiLienDeposit"));
                        row.add(CurrencyValidation.formatCrore(String.valueOf(flexiLienAmt)));
                        data.add(row);
                    }
                }
                lst = null;
            }
            //case charge details
            //              if(caseChargeList !=null && caseChargeList.size()>0){
            //                Map otherChargesMap = new HashMap();
            //                for(int i=0;i<caseChargeList.size();i++){
            //                    HashMap chargeMap=(HashMap)caseChargeList.get(i);
            //
            //                    double Amt=CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
            //                     double chargeAmt=CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
            //                    if(chargeMap.get("CASE_STATUS").equals("EA")){
            //                        row=new ArrayList();
            //                        row.add(transRB.getString("EA COST"));
            //                        row.add(chargeMap.get("FILING_FEES"));
            //                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("FILING_FEES")).doubleValue();
            //                        TermLoanCloseCharge.put("EA_COST",chargeMap.get("FILING_FEES"));
            //                        calaclatedIntChargeMap.put(transRB.getString("EA COST"),chargeMap.get("FILING_FEES"));
            //                        data.add(row);
            //                        row=new ArrayList();
            //                        row.add(transRB.getString("EA EXPENCE"));
            //                        row.add(chargeMap.get("MISC_CHARGES"));
            //                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("MISC_CHARGES")).doubleValue();
            //                        TermLoanCloseCharge.put("EA_EXPENCE",chargeMap.get("MISC_CHARGES"));
            //                        calaclatedIntChargeMap.put(transRB.getString("EA EXPENCE"),chargeMap.get("MISC_CHARGES"));
            //                        data.add(row);
            //
            //
            //
            //                    } else if(chargeMap.get("CASE_STATUS").equals("EP")){
            //                        row=new ArrayList();
            //                        row.add(transRB.getString("EP COST"));
            //                        row.add(chargeMap.get("FILING_FEES"));
            //                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("FILING_FEES")).doubleValue();
            //                        data.add(row);
            //                        TermLoanCloseCharge.put("EP_COST",chargeMap.get("FILING_FEES"));
            //                        calaclatedIntChargeMap.put(transRB.getString("EP COST"),chargeMap.get("FILING_FEES"));
            //                        row=new ArrayList();
            //                        row.add(transRB.getString("EP EXPENCE"));
            //                        row.add(chargeMap.get("MISC_CHARGES"));
            //                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("MISC_CHARGES")).doubleValue();
            //                        data.add(row);
            //                        TermLoanCloseCharge.put("EP_EXPENCE",chargeMap.get("MISC_CHARGES"));
            //                        calaclatedIntChargeMap.put(transRB.getString("EP EXPENCE"),chargeMap.get("MISC_CHARGES"));
            //                    } else if(chargeMap.get("CASE_STATUS").equals("ARC")){
            //                        row=new ArrayList();
            //                        row.add(transRB.getString("ARC COST"));
            //                        row.add(chargeMap.get("FILING_FEES"));
            //                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("FILING_FEES")).doubleValue();
            //                        data.add(row);
            //                        TermLoanCloseCharge.put("ARC_COST",chargeMap.get("FILING_FEES"));
            //                        calaclatedIntChargeMap.put(transRB.getString("ARC COST"),chargeMap.get("FILING_FEES"));
            //                        row=new ArrayList();
            //                        row.add(transRB.getString("ARC EXPENCE"));
            //                        row.add(chargeMap.get("MISC_CHARGES"));
            //                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("MISC_CHARGES")).doubleValue();
            //                        data.add(row);
            //                        TermLoanCloseCharge.put("ARC_EXPENCE",chargeMap.get("MISC_CHARGES"));
            //                        calaclatedIntChargeMap.put(transRB.getString("ARC EXPENCE"),chargeMap.get("MISC_CHARGES"));
            //                    }
            ////                    else {
            ////                        row=new ArrayList();
            ////                        row.add(chargeMap.get("CHARGE_TYPE"));
            ////                        row.add(chargeMap.get("CHARGE_AMT"));
            ////                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
            ////                        data.add(row);
            ////                        row=new ArrayList();
            ////                        otherChargesMap.put(chargeMap.get("CHARGE_TYPE"),chargeMap.get("CHARGE_AMT"));
            ////                    }
            //                }
            //               // TermLoanCloseCharge.put("OTHER_CHARGES",otherChargesMap);
            //            }

//            if(ots.equals("Y")){
//                 row=new ArrayList();
//                 row.add(transRB.getString("MISCELLANEOUS CHARGES"));
//                 row.add(String.valueOf(otsCharges));
//                 totalRecievable +=otsCharges;
//                 data.add(row);
//                 TermLoanCloseCharge.put("MISCELLANEOUS CHARGES",new Double(otsCharges));
//                 TermLoanCloseCharge.put("OTS","Y");
//                 calaclatedIntChargeMap.put(transRB.getString("MISCELLANEOUS CHARGES"),new Double(otsCharges));
//            }else{
            if (chargeList != null && chargeList.size() > 0) {
                Map otherChargesMap = new HashMap();
                for (int i = 0; i < chargeList.size(); i++) {
                    HashMap chargeMap = (HashMap) chargeList.get(i);
                    TermLoanCloseCharge.put("OTS", "N");
                    double chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                    if (chargeMap.get("CHARGE_TYPE").equals("POSTAGE CHARGES") && chargeAmt > 0) {
                        row = new ArrayList();
                        row.add(transRB.getString("POSTAGE CHARGES"));
                        row.add(chargeMap.get("CHARGE_AMT"));
                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                        data.add(row);
                        TermLoanCloseCharge.put("POSTAGE CHARGES", chargeMap.get("CHARGE_AMT"));
                        calaclatedIntChargeMap.put(transRB.getString("POSTAGE CHARGES"), chargeMap.get("CHARGE_AMT"));
                    } else if (chargeMap.get("CHARGE_TYPE").equals("ADVERTISE CHARGES") && chargeAmt > 0) {
                        row = new ArrayList();
                        row.add(transRB.getString("ADVERTISE CHARGES"));
                        row.add(chargeMap.get("CHARGE_AMT"));
                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                        data.add(row);
                        TermLoanCloseCharge.put("ADVERTISE CHARGES", chargeMap.get("CHARGE_AMT"));
                        calaclatedIntChargeMap.put(transRB.getString("ADVERTISE CHARGES"), chargeMap.get("CHARGE_AMT"));



                    } else if (chargeMap.get("CHARGE_TYPE").equals("MISCELLANEOUS CHARGES") && chargeAmt > 0) {
                        row = new ArrayList();
                        row.add(transRB.getString("MISCELLANEOUS CHARGES"));
                        row.add(chargeMap.get("CHARGE_AMT"));
                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                        data.add(row);
                        TermLoanCloseCharge.put("MISCELLANEOUS CHARGES", chargeMap.get("CHARGE_AMT"));
                        calaclatedIntChargeMap.put(transRB.getString("MISCELLANEOUS CHARGES"), chargeMap.get("CHARGE_AMT"));
                    } else if (chargeMap.get("CHARGE_TYPE").equals("LEGAL CHARGES") && chargeAmt > 0) {
                        row = new ArrayList();
                        row.add(transRB.getString("LEGAL CHARGES"));
                        row.add(chargeMap.get("CHARGE_AMT"));
                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                        data.add(row);
                        TermLoanCloseCharge.put("LEGAL CHARGES", chargeMap.get("CHARGE_AMT"));
                        calaclatedIntChargeMap.put(transRB.getString("LEGAL CHARGES"), chargeMap.get("CHARGE_AMT"));
                    } else if (chargeMap.get("CHARGE_TYPE").equals("INSURANCE CHARGES") && chargeAmt > 0) {
                        row = new ArrayList();
                        row.add(transRB.getString("INSURANCE CHARGES"));
                        row.add(chargeMap.get("CHARGE_AMT"));
                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                        data.add(row);
                        TermLoanCloseCharge.put("INSURANCE CHARGES", chargeMap.get("CHARGE_AMT"));
                        calaclatedIntChargeMap.put(transRB.getString("INSURANCE CHARGES"), chargeMap.get("CHARGE_AMT"));
                    } else if (chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") && chargeAmt > 0) {
                        row = new ArrayList();
                        row.add(transRB.getString("EXECUTION DECREE CHARGES"));
                        row.add(chargeMap.get("CHARGE_AMT"));
                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                        data.add(row);
                        TermLoanCloseCharge.put("EXECUTION DECREE CHARGES", chargeMap.get("CHARGE_AMT"));
                        calaclatedIntChargeMap.put(transRB.getString("EXECUTION DECREE CHARGES"), chargeMap.get("CHARGE_AMT"));
                    } else if (chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES") && chargeAmt > 0) {
                        row = new ArrayList();
                        row.add(transRB.getString("ARBITRARY CHARGES"));
                        row.add(chargeMap.get("CHARGE_AMT"));
                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                        data.add(row);
                        row = new ArrayList();
                        TermLoanCloseCharge.put("ARBITRARY CHARGES", chargeMap.get("CHARGE_AMT"));
                        calaclatedIntChargeMap.put(transRB.getString("ARBITRARY CHARGES"), chargeMap.get("CHARGE_AMT"));
                    } else if (chargeMap.get("CHARGE_TYPE").equals("NOTICE CHARGES") && chargeAmt > 0) {
                        row = new ArrayList();
                        row.add(transRB.getString("NOTICE CHARGES"));
                        row.add(chargeMap.get("CHARGE_AMT"));
                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                        data.add(row);
                        row = new ArrayList();
                        TermLoanCloseCharge.put("NOTICE CHARGES", chargeMap.get("CHARGE_AMT"));
                        calaclatedIntChargeMap.put(transRB.getString("NOTICE CHARGES"), chargeMap.get("CHARGE_AMT"));
                    } //arc ep starts
                    else if (chargeMap.get("CHARGE_TYPE").equals("ARC_COST") && chargeAmt > 0) {
                        row = new ArrayList();
                        row.add(transRB.getString("ARC_COST"));
                        row.add(chargeMap.get("CHARGE_AMT"));
                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                        data.add(row);
                        row = new ArrayList();
                        TermLoanCloseCharge.put("ARC_COST", chargeMap.get("CHARGE_AMT"));
                        calaclatedIntChargeMap.put(transRB.getString("ARC_COST"), chargeMap.get("CHARGE_AMT"));
                    } else if (chargeMap.get("CHARGE_TYPE").equals("EP_COST") && chargeAmt > 0) {
                        row = new ArrayList();
                        row.add(transRB.getString("EP_COST"));
                        row.add(chargeMap.get("CHARGE_AMT"));
                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                        data.add(row);
                        row = new ArrayList();
                        TermLoanCloseCharge.put("EP_COST", chargeMap.get("CHARGE_AMT"));
                        calaclatedIntChargeMap.put(transRB.getString("EP_COST"), chargeMap.get("CHARGE_AMT"));
                    }//jiv arc ep ends
                    else if (chargeMap.get("CHARGE_TYPE").equals("RECOVERY CHARGES") && chargeAmt > 0) {
                        row = new ArrayList();
                        row.add(transRB.getString("RECOVERY CHARGES"));
                        row.add(chargeMap.get("CHARGE_AMT"));
                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                        data.add(row);
                        row = new ArrayList();
                        TermLoanCloseCharge.put("RECOVERY CHARGES", chargeMap.get("CHARGE_AMT"));
                        calaclatedIntChargeMap.put(transRB.getString("RECOVERY CHARGES"), chargeMap.get("CHARGE_AMT"));
                    }else if (chargeMap.get("CHARGE_TYPE").equals("MEASUREMENT CHARGES") && chargeAmt > 0) {
                        row = new ArrayList();
                        row.add(transRB.getString("MEASUREMENT CHARGES"));
                        row.add(chargeMap.get("CHARGE_AMT"));
                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                        data.add(row);
                        row = new ArrayList();
                        TermLoanCloseCharge.put("MEASUREMENT CHARGES", chargeMap.get("CHARGE_AMT"));
                        calaclatedIntChargeMap.put(transRB.getString("MEASUREMENT CHARGES"), chargeMap.get("CHARGE_AMT"));
                    }else if (chargeMap.get("CHARGE_TYPE").equals("KOLEFIELD EXPENSE") && chargeAmt > 0) {
                        row = new ArrayList();
                        row.add(transRB.getString("KOLEFIELD EXPENSE"));
                        row.add(chargeMap.get("CHARGE_AMT"));
                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                        data.add(row);
                        row = new ArrayList();
                        TermLoanCloseCharge.put("KOLEFIELD EXPENSE", chargeMap.get("CHARGE_AMT"));
                        calaclatedIntChargeMap.put(transRB.getString("KOLEFIELD EXPENSE"), chargeMap.get("CHARGE_AMT"));
                    }else if (chargeMap.get("CHARGE_TYPE").equals("KOLEFIELD OPERATION") && chargeAmt > 0) {
                        row = new ArrayList();
                        row.add(transRB.getString("KOLEFIELD OPERATION"));
                        row.add(chargeMap.get("CHARGE_AMT"));
                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                        data.add(row);
                        row = new ArrayList();
                        TermLoanCloseCharge.put("KOLEFIELD OPERATION", chargeMap.get("CHARGE_AMT"));
                        calaclatedIntChargeMap.put(transRB.getString("KOLEFIELD OPERATION"), chargeMap.get("CHARGE_AMT"));
                    }
                    else {
                        row = new ArrayList();
                        row.add(chargeMap.get("CHARGE_TYPE"));
                        row.add(chargeMap.get("CHARGE_AMT"));
                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                        data.add(row);
                        row = new ArrayList();
                        otherChargesMap.put(chargeMap.get("CHARGE_TYPE"), chargeMap.get("CHARGE_AMT"));
                    }
                }
                TermLoanCloseCharge.put("OTHER_CHARGES", otherChargesMap);
                //                calaclatedIntChargeMap.put(chargeMap.get("CHARGE_TYPE"),otherChargesMap);
            }
//            }

            row = new ArrayList();
            row.add(transRB.getString("overDuePrince"));//for expence balance
            String Overprince = CommonUtil.convertObjToStr((insertPenal.get("EBAL") == null) ? "0.00" : insertPenal.get("EBAL"));
            TermLoanCloseCharge.put("OVER_DUE_PRINCIPAL", insertPenal.get("EBAL"));
            calaclatedIntChargeMap.put(transRB.getString("overDuePrince"), insertPenal.get("EBAL"));
            //system.out.println("overprincipal"+Overprince);
            if (!Overprince.equals("0.00") || Overprince.equals("0")) {
                row.add(CurrencyValidation.formatCrore(String.valueOf(Overprince)));
                data.add(row);
            }
            row = new ArrayList();
            row.add(transRB.getString("overDueInt"));
            String overDueInt = CommonUtil.convertObjToStr((insertPenal.get("OVER_DUE_INTEREST") == null) ? "0.00" : insertPenal.get("OVER_DUE_INTEREST"));
            TermLoanCloseCharge.put("OVER_DUE_INTEREST", insertPenal.get("OVER_DUE_INTEREST"));
            calaclatedIntChargeMap.put(transRB.getString("overDueInt"), insertPenal.get("OVER_DUE_INTEREST"));
            //system.out.println("@@@@overint"+overDueInt);
            if (!overDueInt.equals("0.00")) {
                row.add(CurrencyValidation.formatCrore(String.valueOf(overDueInt)));
                totalRecievable += CommonUtil.convertObjToDouble(overDueInt).doubleValue();
                data.add(row);
            }
            //                    }
            // String overDue=CommonUtil.convertObjToStr((insertPenal.get("OVER_DUE")==null)?"0.00":insertPenal.get("OVER_DUE"));
           
          //babu repalce code 11-12-2014
            //            if(prodType !=null && (prodType.equals("AAD") || prodType.equals("ATL"))){
            //                 String subLimit=CommonUtil.convertObjToStr((insertPenal.get("SUBLIMIT")==null)?"0.00":insertPenal.get("SUBLIMIT"));
            //                 row=new ArrayList();
            //                    row.add(transRB.getString("Sub Limit"));
            //                    row.add(subLimit);
            //                    data.add(row);
            //            }
            if (CommonUtil.convertObjToDouble(getLienAmount()).doubleValue() > 0) {
                row = new ArrayList();
                row.add(transRB.getString("Lien"));
                row.add(CurrencyValidation.formatCrore(getLienAmount()));
                data.add(row);
            }

            if (CommonUtil.convertObjToDouble(getFreezeAmount()).doubleValue() > 0) {
                row = new ArrayList();
                row.add(transRB.getString("Freeze"));
                row.add(CurrencyValidation.formatCrore(getFreezeAmount()));
                data.add(row);
            }

            ArrayList col = new ArrayList();
            //            col.add(transRB.getString("Display"));
            //            col.add(transRB.getString("Value"));
            col.add("");
            col.add("");
            //          for the addition of total recievable added by NIKHIL
            //            //system.out.println("#@$@#$@#$getSourceScreen() :"+getSourceScreen());
            String sourceScr = CommonUtil.convertObjToStr(getSourceScreen());
            //system.out.println("sourceScrsourceScr"+sourceScr);
            //system.out.println("prodTypeprodTypeprodType jiiiiiiii"+prodType);
            if (prodType != null) {
                if ((prodType.equals("TL") || prodType.equals("AD")) && (!sourceScr.equals("LOAN_ACT_CLOSING"))) {

                    row = new ArrayList();
                    row.add(transRB.getString("Total Recievable"));
                    row.add(CurrencyValidation.formatCrore(String.valueOf(totalRecievable)));
                    data.add(row);

                }
            }

            if (prodType != null) {
                if ((prodType.equals("TL") || prodType.equals("AD")) && (sourceScr.equals("CASH"))) {

                    double charg = 0.0;
                    //system.out.println("datadatadata"+data.size());
                    //system.out.println("datadatadata"+data);

                    double tot = 0.0;
		//added by rish
                    double totIntPenal=0.0;
                    for (int i = 0; i < data.size(); i++) {
                        ArrayList rowVal = (ArrayList) data.get(i);
                        //system.out.println("rowValrowVal "+rowVal.get(0)+"  "+rowVal.get(1));
                        if (prodType.equals("AD")) {
                            if (rowVal.get(0).toString().equals("Clear Balance")) {
                                //system.out.println("iiiiiiiiiii"+i);

                                if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                    String s = rowVal.get(1).toString();
                                    String c = "" + s.charAt(s.length() - 2);
                                    if (c.equalsIgnoreCase("D")) {
                                        String s1 = s.substring(0, (s.length() - 4));
                                        s1 = s1.replaceAll(",", "");
                                        double d = Double.parseDouble(s1);
                                        amnt += d;
                                        //system.out.println("jibbbysdfdsfeeeee");
                                    }
                                }
                            }
                        } else {
                            String pa = "";
                            if (rowVal.get(0).toString().equals("Principal Amount")) {
                                if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                    pa = rowVal.get(1).toString();
                                }
                                pa = pa.replaceAll(",", "");
                                amnt += Double.parseDouble(pa);
                                //system.out.println("amntPrincipal Amount"+amnt);
                            }
                        }
                        String id = "";
                        if (rowVal.get(0).toString().equals("Interest Due")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                id = rowVal.get(1).toString();
                            }
                            id = id.replaceAll(",", "");
                            amnt += Double.parseDouble(id);
                            totIntPenal+=Double.parseDouble(id);
                            //System.out.println("Interest Due ---->"+amnt);
                        }
                        String pea = "";
                        if (rowVal.get(0).toString().equals("Penal Interest")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                pea = rowVal.get(1).toString();
                            }
                            pea = pea.replaceAll(",", "");
                            amnt += Double.parseDouble(pea);
                            totIntPenal+=Double.parseDouble(pea);
                            //system.out.println("Penal Amount"+amnt);
                        }
                        String ocd = "";
                        if (rowVal.get(0).toString().equals("Other Charges Due")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                ocd = rowVal.get(1).toString();
                            }
                            ocd = ocd.replaceAll(",", "");
                            amnt += Double.parseDouble(ocd);
                            //system.out.println("Other Charges Due"+amnt);
                        }
                        String odia = "";
                        if (rowVal.get(0).toString().equals("Over Due InterestAmt")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                odia = rowVal.get(1).toString();
                            }
                            odia = odia.replaceAll(",", "");
                            amnt += Double.parseDouble(odia);
                            //system.out.println("Over Due InterestAmt"+amnt);
                        }
                        String pc = "";
                        if (rowVal.get(0).toString().equals("Postage Charges")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                pc = rowVal.get(1).toString();
                            }
                            pc = pc.replaceAll(",", "");
                            amnt += Double.parseDouble(pc);
                            //system.out.println("Postage Charges"+amnt);
                        }
                        String ac = "";
                        if (rowVal.get(0).toString().equals("Advertise Charges")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                ac = rowVal.get(1).toString();
                            }
                            ac = ac.replaceAll(",", "");
                            amnt += Double.parseDouble(ac);
                            //system.out.println("Advertise Charges"+amnt);
                        }
                        String nc = "";
                        if (rowVal.get(0).toString().equals("Notice Charges")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                nc = rowVal.get(1).toString();
                            }
                            nc = nc.replaceAll(",", "");
                            amnt += Double.parseDouble(nc);
                            //system.out.println("Notice Charges"+amnt);

                        }
                        String mc = "";
                        if (rowVal.get(0).toString().equals("Miscellaneous Charges")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                mc = rowVal.get(1).toString();
                            }
                            mc = mc.replaceAll(",", "");
                            amnt += Double.parseDouble(mc);
                            //system.out.println("Miscellaneous Charges"+amnt);
                        }
                        String lc = "";
                        if (rowVal.get(0).toString().equals("Legal Charges")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                lc = rowVal.get(1).toString();
                            }
                            lc = lc.replaceAll(",", "");
                            amnt += Double.parseDouble(lc);
                            //system.out.println("Legal Charges"+amnt);
                        }
                        String ic = "";
                        if (rowVal.get(0).toString().equals("Insurance Charges")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                ic = rowVal.get(1).toString();
                                ic = ic.replaceAll(",", "");
                                amnt += Double.parseDouble(ic);
                                //system.out.println("Insurance Charges"+amnt);
                            }
                        }

//                        if (rowVal.get(0).toString().equals("Notice Charges")) {
//                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
//                                String noc = "";
//                                noc = rowVal.get(1).toString();
//                                noc = noc.replaceAll(",", "");
//                                amnt += Double.parseDouble(noc);
                        //system.out.println("Notice Charges"+amnt);
//                            }
//                        }
                        if (rowVal.get(0).toString().equals("Execution Decree Charges")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                String edc = "";
                                edc = rowVal.get(1).toString();
                                edc = edc.replaceAll(",", "");
                                amnt += Double.parseDouble(edc);
                                //system.out.println("Execution Decree Charges"+amnt);
                            }
                        }
                        String eac = "";
                        if (rowVal.get(0).toString().equals("EA Cost")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                eac = rowVal.get(1).toString();
                                eac = eac.replaceAll(",", "");
                                amnt += Double.parseDouble(eac);
                                //system.out.println("EA Cost"+amnt);
                            }
                        }

                        if (rowVal.get(0).toString().equals("EA Expence")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                String eae = "";
                                eae = rowVal.get(1).toString();
                                eae.replaceAll(",", "");
                                amnt += Double.parseDouble(eae);
                                //system.out.println("EA Expence"+amnt);
                            }
                        }
                        if (rowVal.get(0).toString().equals("ARC Cost")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                String arc = "";
                                arc = rowVal.get(1).toString();
                                arc.replaceAll(",", "");
                                amnt += Double.parseDouble(arc);
                                //system.out.println("ARC Cost"+amnt);
                            }
                        }
                        if (rowVal.get(0).toString().equals("ARC Expence")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                String arce = "";
                                arce = rowVal.get(1).toString();
                                arce.replaceAll(",", "");
                                amnt += Double.parseDouble(arce);
                                //system.out.println("ARC Expence"+amnt);
                            }
                        }

                        if (rowVal.get(0).toString().equals("EP Cost")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                String epc = "";
                                epc = rowVal.get(1).toString();
                                epc.replaceAll(",", "");
                                amnt += Double.parseDouble(epc);
                                //system.out.println("EP Cost"+amnt);
                            }
                        }

                        if (rowVal.get(0).toString().equals("EP Expence")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                String epe = "";
                                epe = rowVal.get(1).toString();
                                epe.replaceAll(",", "");
                                amnt += Double.parseDouble(epe);
                                //system.out.println("EP Expence"+amnt);
                            }
                        }
                        // for overdue interest                     
                         String odi = "";
                        if (rowVal.get(0).toString().equals("Overdue Interest")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                odi = rowVal.get(1).toString();
                            }
                            odi = odi.replaceAll(",", "");
                            amnt += Double.parseDouble(odi);
                            //system.out.println("Miscellaneous Charges"+amnt);
                        }
                        // end                        
                        if (rowVal.get(0).toString().equals("Arbitrary Charges")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                String abc = "";
                                abc = rowVal.get(1).toString();
                                abc.replaceAll(",", "");
                                amnt += Double.parseDouble(abc);
                                //system.out.println("Arbitrary Charges"+amnt);
                            }
                        }

                        // Added by nithya on 24-02-2022 for KD-3337
                        if (rowVal.get(0).toString().equals("OTHER CHARGES")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                String abc = "";
                                abc = rowVal.get(1).toString();
                                abc.replaceAll(",", "");
                                amnt += Double.parseDouble(abc);
                            }
                        }
                       
                        if (rowVal.get(0).toString().equals("Recovery Charges")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                String abc = "";
                                abc = rowVal.get(1).toString();
                                abc.replaceAll(",", "");
                                amnt += Double.parseDouble(abc);
                            }
                        }
                        
                        if (rowVal.get(0).toString().equals("Measurement Charges")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                String abc = "";
                                abc = rowVal.get(1).toString();
                                abc.replaceAll(",", "");
                                amnt += Double.parseDouble(abc);
                            }
                        }
                        
                        if (rowVal.get(0).toString().equals("KoleField Expense")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                String abc = "";
                                abc = rowVal.get(1).toString();
                                abc.replaceAll(",", "");
                                amnt += Double.parseDouble(abc);
                            }
                        }
                        
                        if (rowVal.get(0).toString().equals("KoleField Operation Charges")) {
                            if (!rowVal.get(1).toString().equals("") || rowVal.get(1) != null) {
                                String abc = "";
                                abc = rowVal.get(1).toString();
                                abc.replaceAll(",", "");
                                amnt += Double.parseDouble(abc);
                            }
                        }

                    }
                    //system.out.println("amntamntamnt"+amnt);
                    tot = amnt;
                    amnt = 0.0;
                    //String prinDue=  get
                    String productID = "";
                    if (getProdId() != null) {
                        productID = getProdId().toString();
                    }
                    if (productID != null || !productID.equals("")) {
                        HashMap whereMap = new HashMap();

                        whereMap.put("SCHEME_ID", productID);
                        whereMap.put("DEDUCTION_ACCU", "C");
                        if(sourceScr.equals("CASH")){
                            whereMap.put("CHECK_MANDATORY", "CHECK_MANDATORY");
                        }
                        List list = ClientUtil.executeQuery("getChargeDetailsData", whereMap);

                        if (list.size() > 0) {

                            //system.out.println("listlistlistlist"+list);   
                            whereMap = new HashMap();
                            whereMap = (HashMap) list.get(0);
                            String chrg = whereMap.get("AMOUNT").toString();
                            chrg = chrg.replaceAll(",", "");
                            charg = CommonUtil.convertObjToDouble(chrg);
                            tot = charg + tot;
                            charg = 0.0;

                        }
                    }
                    row = new ArrayList();
                    row.add(transRB.getString("Loan Closing Amount"));
                    row.add(CurrencyValidation.formatCrore(String.valueOf(tot)));
                    tot = 0.0;
                    data.add(row);
                    totalRecievable = 0.0;
                    //added by rish 
                     row = new ArrayList();
                      row.add(transRB.getString("Total(IntDue+Penal)"));
                        row.add(CurrencyValidation.formatCrore(String.valueOf(totIntPenal)));
                        totIntPenal=0.0;
                         data.add(row);
                    
                }
            }
            //Added by nithya on 31-12-2019 for KD - 1191           
            if (prodType != null && prodType.equals("TL")) {                
                if (insertPenal.containsKey("LAST_INT_CALC_DT") && insertPenal.get("LAST_INT_CALC_DT") != null) {                   
                    Date lastintdt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(insertPenal.get("LAST_INT_CALC_DT")));                    
                    noOfintDays = DateUtil.dateDiff(lastintdt, currDt) - 1;         // subtracting 1 from days calculated - added by nithya on 31-01-2020 for KD-1191 [ Reported after testing ]           
                }
                row = new ArrayList();    
                row.add(transRB.getString("InterestPeriod"));                
                row.add(noOfintDays+" Days");
                data.add(row);
                setIntPeriodRowColor();
            }
            //End
            
            //Added by sreekrishnan
            if (prodType != null && prodType.equals("AB") ) {
                row = new ArrayList();
                row.add(transRB.getString("OtherInterestAmt"));
                row.add(CurrencyValidation.formatCrore(String.valueOf(0.0)));                
                data.add(row);   
                row = new ArrayList();
                row.add(transRB.getString("OtherPenalAmt"));
                row.add(CurrencyValidation.formatCrore(String.valueOf(0.0)));                
                data.add(row);
                row = new ArrayList();
                row.add(transRB.getString("OtherChargeAmt"));
                row.add(CurrencyValidation.formatCrore(String.valueOf(0.0)));                
                data.add(row);
            }
            EnhancedTableModel model = new EnhancedTableModel(data, col);
            tblTrans.setModel(model);

            //          if(prodType !=null && prodType.equals("TL") && CommonUtil.convertObjToStr(CommonConstants.OPERATE_MODE).equals(CommonConstants.IMPLEMENTATION)){
            //              tblTrans.setEditingColumn(2);
            //              tblTrans.setEditingRow(8);
            //          }
            tblTrans.revalidate();
            if (prodType != null && prodType.length() > 0 && AccountNo != null && AccountNo.length() > 0) {
                actDetailsLoaded.put(prodType + AccountNo + "Balance", model);
            }
            scrTransPan.getViewport().setViewPosition(new java.awt.Point(0, scrTransPan.getMaximumSize().height));
            insertPenal = null;
//            System.out.println("jjjjjjjjjjjjj");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    private Date setProperDtFormat(Date dt) { //Added By Suresh R
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
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

        tbrTrans = new javax.swing.JToolBar();
        btnPendingTransaction = new com.see.truetransact.uicomponent.CButton();
        btnShadowCR = new com.see.truetransact.uicomponent.CButton();
        btnShadowDR = new com.see.truetransact.uicomponent.CButton();
        btnUnclear = new com.see.truetransact.uicomponent.CButton();
        btnPhotoSign = new com.see.truetransact.uicomponent.CButton();
        btnMembershipLia = new com.see.truetransact.uicomponent.CButton();
        btnViewLedger = new com.see.truetransact.uicomponent.CButton();
        btnReport = new com.see.truetransact.uicomponent.CButton();
        btnStandInstruction = new com.see.truetransact.uicomponent.CButton();
        btnChqBook = new com.see.truetransact.uicomponent.CButton();
        btnLiabilityReport = new com.see.truetransact.uicomponent.CButton();
        btnBillsTransactionDetails = new com.see.truetransact.uicomponent.CButton();
        btnRepayShedule = new com.see.truetransact.uicomponent.CButton();
        panTransDetails = new com.see.truetransact.uicomponent.CPanel();
        panTrans = new com.see.truetransact.uicomponent.CPanel();
        scrTransPan = new com.see.truetransact.uicomponent.CScrollPane();
        tblTrans = new com.see.truetransact.uicomponent.CTable();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        panAcctDetails = new com.see.truetransact.uicomponent.CPanel();
        scrAcctDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblAcctDetails = new com.see.truetransact.uicomponent.CTable();

        setMinimumSize(new java.awt.Dimension(200, 229));
        setPreferredSize(new java.awt.Dimension(200, 229));
        setLayout(new java.awt.BorderLayout());

        tbrTrans.setMargin(new java.awt.Insets(4, 4, 4, 4));

        btnPendingTransaction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/pendingTransaction.gif"))); // NOI18N
        btnPendingTransaction.setToolTipText("Pending Transaction Details");
        btnPendingTransaction.setFocusable(false);
        btnPendingTransaction.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPendingTransaction.setMaximumSize(new java.awt.Dimension(30, 27));
        btnPendingTransaction.setMinimumSize(new java.awt.Dimension(30, 27));
        btnPendingTransaction.setPreferredSize(new java.awt.Dimension(30, 27));
        btnPendingTransaction.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPendingTransaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendingTransactionActionPerformed(evt);
            }
        });
        tbrTrans.add(btnPendingTransaction);

        btnShadowCR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/Rs_out.gif"))); // NOI18N
        btnShadowCR.setText("CR");
        btnShadowCR.setToolTipText("Shadow Credit");
        btnShadowCR.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        btnShadowCR.setMaximumSize(new java.awt.Dimension(0, 0));
        btnShadowCR.setMinimumSize(new java.awt.Dimension(0, 0));
        btnShadowCR.setPreferredSize(new java.awt.Dimension(0, 0));
        btnShadowCR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShadowCRActionPerformed(evt);
            }
        });
        tbrTrans.add(btnShadowCR);

        btnShadowDR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/Rs_in.gif"))); // NOI18N
        btnShadowDR.setText("DR");
        btnShadowDR.setToolTipText("Shadow Debit");
        btnShadowDR.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        btnShadowDR.setMaximumSize(new java.awt.Dimension(0, 0));
        btnShadowDR.setMinimumSize(new java.awt.Dimension(0, 0));
        btnShadowDR.setPreferredSize(new java.awt.Dimension(0, 0));
        btnShadowDR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShadowDRActionPerformed(evt);
            }
        });
        tbrTrans.add(btnShadowDR);

        btnUnclear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/Rs_in.gif"))); // NOI18N
        btnUnclear.setText("UB");
        btnUnclear.setToolTipText("Unclear Balance");
        btnUnclear.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        btnUnclear.setMaximumSize(new java.awt.Dimension(0, 0));
        btnUnclear.setMinimumSize(new java.awt.Dimension(0, 0));
        btnUnclear.setPreferredSize(new java.awt.Dimension(0, 0));
        btnUnclear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnclearActionPerformed(evt);
            }
        });
        tbrTrans.add(btnUnclear);

        btnPhotoSign.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PHOTO_SIGN.gif"))); // NOI18N
        btnPhotoSign.setToolTipText("View Customer Details");
        btnPhotoSign.setMaximumSize(new java.awt.Dimension(70, 27));
        btnPhotoSign.setMinimumSize(new java.awt.Dimension(50, 27));
        btnPhotoSign.setPreferredSize(new java.awt.Dimension(50, 27));
        btnPhotoSign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhotoSignActionPerformed(evt);
            }
        });
        tbrTrans.add(btnPhotoSign);

        btnMembershipLia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/members2.jpg"))); // NOI18N
        btnMembershipLia.setToolTipText("View MemberShip Liability");
        btnMembershipLia.setMaximumSize(new java.awt.Dimension(30, 27));
        btnMembershipLia.setMinimumSize(new java.awt.Dimension(30, 27));
        btnMembershipLia.setPreferredSize(new java.awt.Dimension(30, 27));
        btnMembershipLia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMembershipLiaActionPerformed(evt);
            }
        });
        tbrTrans.add(btnMembershipLia);

        btnViewLedger.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/ledger.jpg"))); // NOI18N
        btnViewLedger.setToolTipText("View Ledger");
        btnViewLedger.setMaximumSize(new java.awt.Dimension(30, 27));
        btnViewLedger.setMinimumSize(new java.awt.Dimension(30, 27));
        btnViewLedger.setPreferredSize(new java.awt.Dimension(30, 27));
        btnViewLedger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewLedgerActionPerformed(evt);
            }
        });
        tbrTrans.add(btnViewLedger);

        btnReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/uicomponent/images/report.gif"))); // NOI18N
        btnReport.setToolTipText("Loan Interest Report Details");
        btnReport.setFocusable(false);
        btnReport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReport.setMaximumSize(new java.awt.Dimension(30, 27));
        btnReport.setMinimumSize(new java.awt.Dimension(30, 27));
        btnReport.setPreferredSize(new java.awt.Dimension(30, 27));
        btnReport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportActionPerformed(evt);
            }
        });
        tbrTrans.add(btnReport);

        btnStandInstruction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/uicomponent/images/StandingInstruction.gif"))); // NOI18N
        btnStandInstruction.setToolTipText("Standing Instruction Details");
        btnStandInstruction.setFocusable(false);
        btnStandInstruction.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnStandInstruction.setMaximumSize(new java.awt.Dimension(30, 27));
        btnStandInstruction.setMinimumSize(new java.awt.Dimension(30, 27));
        btnStandInstruction.setPreferredSize(new java.awt.Dimension(30, 27));
        btnStandInstruction.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnStandInstruction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStandInstructionActionPerformed(evt);
            }
        });
        tbrTrans.add(btnStandInstruction);

        btnChqBook.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/uicomponent/images/ChequeBook.gif"))); // NOI18N
        btnChqBook.setToolTipText("Cheque Book Details");
        btnChqBook.setFocusable(false);
        btnChqBook.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChqBook.setMaximumSize(new java.awt.Dimension(30, 27));
        btnChqBook.setMinimumSize(new java.awt.Dimension(30, 27));
        btnChqBook.setPreferredSize(new java.awt.Dimension(30, 27));
        btnChqBook.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnChqBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChqBookActionPerformed(evt);
            }
        });
        tbrTrans.add(btnChqBook);

        btnLiabilityReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/Membership_information.gif"))); // NOI18N
        btnLiabilityReport.setToolTipText("Membership Liability Report");
        btnLiabilityReport.setFocusable(false);
        btnLiabilityReport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLiabilityReport.setMaximumSize(new java.awt.Dimension(30, 27));
        btnLiabilityReport.setMinimumSize(new java.awt.Dimension(30, 27));
        btnLiabilityReport.setPreferredSize(new java.awt.Dimension(30, 27));
        btnLiabilityReport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLiabilityReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLiabilityReportActionPerformed(evt);
            }
        });
        tbrTrans.add(btnLiabilityReport);

        btnBillsTransactionDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/uicomponent/images/StandingInstruction.gif"))); // NOI18N
        btnBillsTransactionDetails.setToolTipText("Bills Details");
        btnBillsTransactionDetails.setFocusable(false);
        btnBillsTransactionDetails.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillsTransactionDetails.setMaximumSize(new java.awt.Dimension(30, 27));
        btnBillsTransactionDetails.setMinimumSize(new java.awt.Dimension(30, 27));
        btnBillsTransactionDetails.setPreferredSize(new java.awt.Dimension(30, 27));
        btnBillsTransactionDetails.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBillsTransactionDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBillsTransactionDetailsActionPerformed(evt);
            }
        });
        tbrTrans.add(btnBillsTransactionDetails);

        btnRepayShedule.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/uicomponent/images/schedule.gif"))); // NOI18N
        btnRepayShedule.setToolTipText("Loan Repayment schedule");
        btnRepayShedule.setFocusable(false);
        btnRepayShedule.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRepayShedule.setMaximumSize(new java.awt.Dimension(28, 27));
        btnRepayShedule.setMinimumSize(new java.awt.Dimension(28, 27));
        btnRepayShedule.setPreferredSize(new java.awt.Dimension(28, 27));
        btnRepayShedule.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRepayShedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRepaySheduleActionPerformed(evt);
            }
        });
        tbrTrans.add(btnRepayShedule);

        add(tbrTrans, java.awt.BorderLayout.NORTH);

        panTransDetails.setMinimumSize(new java.awt.Dimension(100, 200));
        panTransDetails.setPreferredSize(new java.awt.Dimension(100, 200));
        panTransDetails.setLayout(new java.awt.GridBagLayout());

        panTrans.setPreferredSize(new java.awt.Dimension(24, 24));
        panTrans.setLayout(new java.awt.GridBagLayout());

        tblTrans.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblTrans.setCellSelectionEnabled(true);
        tblTrans.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        tblTrans.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTransMouseClicked(evt);
            }
        });
        scrTransPan.setViewportView(tblTrans);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTrans.add(scrTransPan, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetails.add(panTrans, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        panTransDetails.add(sptLine, gridBagConstraints);

        panAcctDetails.setPreferredSize(new java.awt.Dimension(24, 24));
        panAcctDetails.setLayout(new java.awt.GridBagLayout());

        tblAcctDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ));
        scrAcctDetails.setViewportView(tblAcctDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAcctDetails.add(scrAcctDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetails.add(panAcctDetails, gridBagConstraints);

        add(panTransDetails, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void tblTransMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTransMouseClicked
        // TODO add your handling code here:
        int count = evt.getClickCount();
        if (selectedList == null) {
            selectedList = new ArrayList();
        }
        if (count == 2) {
            String behavesLike = ""; // Added by nithya on 30-04-2021 for KD-2844 
            boolean reccurring = false;
            if (prodType.equals("TD")) {
                String accountNum = acctNo;
                if (accountNum.lastIndexOf("_") != -1) {
                    accountNum = accountNum.substring(0, accountNum.lastIndexOf("_"));
                }
                HashMap accountMap = new HashMap();
                accountMap.put("DEPOSIT_NO", accountNum);
                List resultList = ClientUtil.executeQuery("getSelectRecalculateDetails", accountMap);
                if (resultList != null && resultList.size() > 0) {
                    accountMap = (HashMap) resultList.get(0);
                    if (accountMap != null && accountMap.size() > 0 && accountMap.containsKey("BEHAVES_LIKE")) {
                        behavesLike = CommonUtil.convertObjToStr(accountMap.get("BEHAVES_LIKE"));
                        if (behavesLike.equals("RECURRING")) {
                            reccurring = true;
                        }
                    }
                }
            }
            selectedRow = (int) tblTrans.getSelectedRow();
            selectedList = (ArrayList) ((EnhancedTableModel) tblTrans.getModel()).getDataArrayList().get(selectedRow);
            String key = CommonUtil.convertObjToStr(selectedList.get(0));
            if (!(key.equals(transRB.getString("Limit")) || key.equals(transRB.getString("AvailableBalance")) || key.equals(transRB.getString("Currprince")) || key.equals(transRB.getString("LoanBalance")) || key.equals(transRB.getString("TotalBalance")) || key.equals("LastInt")
                    || key.equals("Last Int Calc Dt") || key.equals("Interest Rate") || key.equals(transRB.getString("Total Recievable")) || key.equals(transRB.getString("DrawingPower"))
                    || key.equals(transRB.getString("ClearBalance")) || key.equals(transRB.getString("TotalBalance")) || key.equals(transRB.getString("UnclearBalance")) || key.equals(transRB.getString("ShadowDebit")) || key.equals(transRB.getString("ShadowCredit"))||getSourceScreen().equalsIgnoreCase("LOAN_FUTURE_TRANSACTION"))) {
                HashMap amountMap = new HashMap();

                selectedList = (ArrayList) ((EnhancedTableModel) tblTrans.getModel()).getDataArrayList().get(selectedRow);
                //system.out.println("selectedListselectedList"+selectedList);
                if ((reccurring || prodType.equals("TL") || prodType.equals("AD") || prodType.equals("AB")) && CommonUtil.convertObjToStr(CommonConstants.OPERATE_MODE).equals(CommonConstants.IMPLEMENTATION)) {
                    key = CommonUtil.convertObjToStr(selectedList.get(0));
                    //system.out.println("keykeykey"+key);
                    String tolerance_amt = CommonUtil.convertObjToStr(CommonConstants.TOLERANCE_AMT);
                    //system.out.println("tolerance_amttolerance_amt"+tolerance_amt);
                    if (tolerance_amt.length() == 0) {
                        ClientUtil.displayAlert("Please Add Tolerance Property in  TT property");
                        return;
                    }
                    String selectedAmt = CommonUtil.convertObjToStr(selectedList.get(1));

                    selectedAmt = selectedAmt.replaceAll(",", "");
                    amountMap.put("TOLERANCE_AMT", CommonConstants.TOLERANCE_AMT);
                    amountMap.put("SELECTED_AMT", selectedAmt);
                    amountMap.put("TITLE", key);
                    amountMap.put("CALCULATED_AMT", calaclatedIntChargeMap.get(key));
                    //system.out.println("amountMap####"+amountMap);
                    if (key.equals("EA Cost") || key.equals("EA Expence") || key.equals("ARC Cost") || key.equals("ARC Expence") || key.equals("EP Cost") || key.equals("EP Expence")) {
                        ClientUtil.showMessageWindow(key + "  Amount is not able to change   Because all are Case Expenceses");
                        return;
                    }

                    TextUI textUI = new TextUI(sourceFrame, this, amountMap);


//             ((EnhancedTableModel)tblTrans.getModel()).setDataArrayList(totalList, co);
                }
            }
        }
    }//GEN-LAST:event_tblTransMouseClicked

    public void modifyTransDatas(TextUI obj) {
        TextUI objTextUI = (TextUI) obj;
        double totalRecievable = 0;
        String enteredData = objTextUI.getTxtData();
        //System.out.println("selectedList@#$@#$#@"+selectedList);
        String key = CommonUtil.convertObjToStr(selectedList.get(0));
        //System.out.println("before  key"+key);        
        if (key.equals("Interest Due")) {

            TermLoanCloseCharge.put("CURR_MONTH_INT", new Double(enteredData));
            TermLoanCloseCharge.put("INTEREST", new Double(enteredData));
            //System.out.println("after  int "+enteredData);
        } else if (key.equals("Penal Interest")) {
            //system.out.println("enteredDataenteredData"+enteredData);
            TermLoanCloseCharge.put("PENAL_INT", new Double(enteredData));
            TermLoanCloseCharge.put("LOAN_CLOSING_PENAL_INT", new Double(enteredData));
            //system.out.println("after penal  "+TermLoanCloseCharge);
        }else if (key.equals("Available Rebate Interest")) {
            TermLoanCloseCharge.put("REBATE_INTEREST", new Double(enteredData));            
        } else if (key.equals("Arbitrary Charges")) {
            TermLoanCloseCharge.put("ARBITRARY CHARGES", new Double(enteredData));

        } else if (key.equals("Recovery Charges")) {
            TermLoanCloseCharge.put("RECOVERY CHARGES", new Double(enteredData));

        }else if (key.equals("Measurement Charges")) {
            TermLoanCloseCharge.put("MEASUREMENT CHARGES", new Double(enteredData));

        } else if (key.equals("KoleField Expense")) {
            TermLoanCloseCharge.put("KOLEFIELD EXPENSE", new Double(enteredData));

        } else if (key.equals("KoleField Operation Charges")) {
            TermLoanCloseCharge.put("KOLEFIELD OPERATION", new Double(enteredData));

        } else if (key.equals("Postage Charges")) {
            TermLoanCloseCharge.put("POSTAGE CHARGES", new Double(enteredData));

        } else if (key.equals("Advertisement Charges")) {
            TermLoanCloseCharge.put("ADVERTISE CHARGES", new Double(enteredData));

        } else if (key.equals("Miscellaneous Charges")) {
            TermLoanCloseCharge.put("MISCELLANEOUS CHARGES", new Double(enteredData));

        } else if (key.equals("Legal Charges")) {
            TermLoanCloseCharge.put("LEGAL CHARGES", new Double(enteredData));

        } else if (key.equals("Insurance Charges")) {
            TermLoanCloseCharge.put("INSURANCE CHARGES", new Double(enteredData));

        } else if (key.equals("Execution Decree Charges")) {
            TermLoanCloseCharge.put("EXECUTION DECREE CHARGES", new Double(enteredData));

        } else if (key.equals("Notice Charges")) {
            TermLoanCloseCharge.put("NOTICE CHARGES", new Double(enteredData));
        } else if (key.equals("OTHER CHARGES")) {
            //System.out.println("OTHER CHARGES ----->"+TermLoanCloseCharge);
            HashMap otherMap = new HashMap();
            otherMap = (HashMap) TermLoanCloseCharge.get("OTHER_CHARGES");
            if (otherMap != null && otherMap.size() > 0 && otherMap.containsKey("OTHER CHARGES")) {
                otherMap.put("OTHER CHARGES" ,new Double(enteredData));
                TermLoanCloseCharge.put("OTHER_CHARGES", otherMap);
            }
        } else if (key.equals("Other Bank Interest")) {
            otherBankAccountMap.put("INTEREST", new Double(enteredData));
        }else if (key.equals("Other Bank Penal")) {
            otherBankAccountMap.put("PENAL_INT", new Double(enteredData));
        }else if (key.equals("Other Bank Charge")) {
            otherBankAccountMap.put("CHARGES", new Double(enteredData));
        } else if(key.equals("Pending Penalty for Delayed Months")){ // Added by nithya on 30-04-2021 for KD-2844
            setPenalAmount(String.valueOf(enteredData));
        }
        //System.out.println("OTHER CHARGES ----->22"+otherBankAccountMap);
        selectedList.set(1, enteredData);
        ArrayList totalList = (ArrayList) getTblDataArrayList();
        //Changed By Suresh         
       // ArrayList totRecivable = (ArrayList) totalList.get(totalList.size() - 1);
       // ArrayList totRecivable = (ArrayList) totalList.get(totalList.size() - 2);
        ArrayList totRecivable = (ArrayList) totalList.get(totalList.size() - 1);
        if (totRecivable.get(0).equals("Total Recievable") || totRecivable.get(0).equals("Loan Closing Amount")) {
            totRecivable.set(1, new Double(calculatetotalRecivableAmount()));
            //Added By Suresh ( Calculate Loan Closing Amount )
            //ArrayList loanClosingAmt = (ArrayList) totalList.get(totalList.size() - 12);
            ArrayList loanClosingAmt = new ArrayList();
            if (totalList.size() > 13) {
                loanClosingAmt = (ArrayList) totalList.get(totalList.size() - 13);
            }
            if ((getSourceScreen() != null && !getSourceScreen().equals("") && !getSourceScreen().equals("LOAN_ACT_CLOSING")) && loanClosingAmt.get(0).equals("Principal Amount")) {
                double totReceivableAmt = 0.0;
                double finalLoanClosingAmt = 0.0;
                String princAmt = CommonUtil.convertObjToStr(loanClosingAmt.get(1));
                princAmt = princAmt.replaceAll(",", "");
                totReceivableAmt = CommonUtil.convertObjToDouble(totRecivable.get(1)).doubleValue();
                finalLoanClosingAmt = CommonUtil.convertObjToDouble(princAmt).doubleValue() + totReceivableAmt;
                tblTrans.setValueAt(new Double(finalLoanClosingAmt), tblTrans.getRowCount() - 1, 1);
                //System.out.println("finalLoanClosingAmt22 ----->"+finalLoanClosingAmt);
            }
        }
        //Changed By Suresh
//        totalList.set(totalList.size() - 1, totRecivable);
        //  totalList.set(totalList.size() - 2, totRecivable);
        totalList.set(totalList.size() - 1, totRecivable);
        totalList.set(selectedRow, selectedList);
        //System.out.println("########### After totalList : " + totalList);
        ((EnhancedTableModel) tblTrans.getModel()).setDataArrayList(totalList, col);
        tblTrans.revalidate();
    }

    public double calculatetotalRecivableAmount() {
        double totalRecievable = 0;
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("EXECUTION DECREE CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("INSURANCE CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("LEGAL CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("MISCELLANEOUS CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("POSTAGE CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("NOTICE CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("ADVERTISE CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("ARBITRARY CHARGES")).doubleValue();
       // totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("LOAN_CLOSING_PENAL_INT")).doubleValue();//jiby
        if (TermLoanCloseCharge.containsKey("OTHER_CHARGES")) {
            HashMap otherMap = new HashMap();
            otherMap = (HashMap) TermLoanCloseCharge.get("OTHER_CHARGES");
            if (otherMap != null && otherMap.size() > 0 && otherMap.containsKey("OTHER CHARGES")) {
                totalRecievable += CommonUtil.convertObjToDouble(otherMap.get("OTHER CHARGES"));
            }
        }
        if(TermLoanCloseCharge.containsKey("RECOVERY CHARGES")){
            totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("RECOVERY CHARGES")).doubleValue();
        }
        if(TermLoanCloseCharge.containsKey("MEASUREMENT CHARGES")){
            totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("MEASUREMENT CHARGES")).doubleValue();
        }
        
        if(TermLoanCloseCharge.containsKey("KOLEFIELD EXPENSE") && TermLoanCloseCharge.get("KOLEFIELD EXPENSE") != null){
            totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("KOLEFIELD EXPENSE")).doubleValue();
        }
        
        if(TermLoanCloseCharge.containsKey("KOLEFIELD OPERATION") && TermLoanCloseCharge.get("KOLEFIELD OPERATION") != null){
            totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("KOLEFIELD OPERATION")).doubleValue();
        }
        
                totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("PENAL_INT")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("CURR_MONTH_INT")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("CURR_MONTH_PRINCEPLE")).doubleValue();
        //system.out.println("fgfhfhgffgfhfggh"+CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("LOAN_CLOSING_PENAL_INT")).doubleValue());
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("EA_COST")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("EA_EXPENCE")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("ARC_COST")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("ARC_EXPENCE")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("EP_COST")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("EP_EXPENCE")).doubleValue();
        //System.out.println("############# totalRecievable : " + totalRecievable);
        return totalRecievable;
    }

    public double calculatetotalRecivableAmountFromAccountClosing() {
        double totalRecievable = 0;
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("EXECUTION DECREE CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("INSURANCE CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("LEGAL CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("MISCELLANEOUS CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("POSTAGE CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("NOTICE CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("ADVERTISE CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("ARBITRARY CHARGES")).doubleValue();
        //totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("LOAN_CLOSING_PENAL_INT")).doubleValue();//jiby
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("PENAL_INT")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("CURR_MONTH_INT")).doubleValue();
        if(TermLoanCloseCharge.containsKey("RECOVERY CHARGES")){
           totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("RECOVERY CHARGES")).doubleValue(); 
        }
         if(TermLoanCloseCharge.containsKey("MEASUREMENT CHARGES")){
           totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("MEASUREMENT CHARGES")).doubleValue(); 
        }
         
        if(TermLoanCloseCharge.containsKey("KOLEFIELD EXPENSE") && TermLoanCloseCharge.get("KOLEFIELD EXPENSE") != null){
            totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("KOLEFIELD EXPENSE")).doubleValue();
        }
        
        if(TermLoanCloseCharge.containsKey("KOLEFIELD OPERATION") && TermLoanCloseCharge.get("KOLEFIELD OPERATION") != null){
            totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("KOLEFIELD OPERATION")).doubleValue();
        } 
         
         if (TermLoanCloseCharge.containsKey("OTHER_CHARGES")) {
            HashMap otherMap = new HashMap();
            otherMap = (HashMap) TermLoanCloseCharge.get("OTHER_CHARGES");
            if (otherMap != null && otherMap.size() > 0 && otherMap.containsKey("OTHER CHARGES")) {
                totalRecievable += CommonUtil.convertObjToDouble(otherMap.get("OTHER CHARGES"));
            }
        }

        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("EA_COST")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("EA_EXPENCE")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("ARC_COST")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("ARC_EXPENCE")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("EP_COST")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("EP_EXPENCE")).doubleValue();
        return totalRecievable;
    }

    public double calculatetotalRecivableAmountForRenewalLoan() {

        double totalRecievable = 0;
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("EXECUTION DECREE CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("INSURANCE CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("LEGAL CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("MISCELLANEOUS CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("POSTAGE CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("NOTICE CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("ADVERTISE CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("ARBITRARY CHARGES")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("PENAL_INT")).doubleValue();// LOAN_CLOSING_PENAL_INT
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("CURR_MONTH_INT")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
        if(TermLoanCloseCharge.containsKey("RECOVERY CHARGES")){
            totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("RECOVERY CHARGES")).doubleValue();
        }if(TermLoanCloseCharge.containsKey("MEASUREMENT CHARGES")){
            totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("MEASUREMENT CHARGES")).doubleValue();
        }
        if(TermLoanCloseCharge.containsKey("KOLEFIELD EXPENSE") && TermLoanCloseCharge.get("KOLEFIELD EXPENSE") != null){
            totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("KOLEFIELD EXPENSE")).doubleValue();
        }
        
        if(TermLoanCloseCharge.containsKey("KOLEFIELD OPERATION") && TermLoanCloseCharge.get("KOLEFIELD OPERATION") != null){
            totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("KOLEFIELD OPERATION")).doubleValue();
        }
        if (TermLoanCloseCharge.containsKey("OTHER_CHARGES")) {
            HashMap otherMap = new HashMap();
            otherMap = (HashMap) TermLoanCloseCharge.get("OTHER_CHARGES");
            if (otherMap != null && otherMap.size() > 0 && otherMap.containsKey("OTHER CHARGES")) {
                totalRecievable += CommonUtil.convertObjToDouble(otherMap.get("OTHER CHARGES"));
            }
        }
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("EA_COST")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("EA_EXPENCE")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("ARC_COST")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("ARC_EXPENCE")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("EP_COST")).doubleValue();
        totalRecievable += CommonUtil.convertObjToDouble(TermLoanCloseCharge.get("EP_EXPENCE")).doubleValue();
        return totalRecievable;

    }
    private void btnViewLedgerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewLedgerActionPerformed
        // TODO add your handling code here:
        //System.out.println("prodType"+prodType+accDesc);
         if(prodType.equalsIgnoreCase("GL")){
        	TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("AcHdId", accDesc);
            paramMap.put("FromDt", currDt.clone());
            paramMap.put("ToDt", currDt.clone());
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            ttIntgration.setParam(paramMap);
            //System.out.println("paramMap"+paramMap);
            if(prodType.equalsIgnoreCase("GL")){
            	ttIntgration.integration("AcHd_Ledger");
         	}
            return;
         }
        if (acctNo != null && !acctNo.equalsIgnoreCase("")) {
            new TransactionDetailsUI(acctNo, prodType).show();
        }
        /**
         * if(acctNo!=null && !acctNo.equalsIgnoreCase("")){ TTIntegration
         * ttIntgration = null; HashMap paramMap = new HashMap();
         * paramMap.put("AccountNo", acctNo); paramMap.put("FromDt",
         * currDt); paramMap.put("ToDt",
         * currDt); paramMap.put("BranchId",
         * ProxyParameters.BRANCH_ID); ttIntgration.setParam(paramMap);
         * //Changed BY Suresh if(prodType.equals("TD")){ HashMap prodMap = new
         * HashMap(); if(acctNo.lastIndexOf('_')!=-1){ acctNo =
         * acctNo.substring(0,acctNo.lastIndexOf("_")); }
         * prodMap.put("ACT_NUM",acctNo); List lst =
         * ClientUtil.executeQuery("getBehavesLikeForDepositNo", prodMap);
         * if(lst!=null && lst.size()>0){ prodMap= (HashMap)lst.get(0);
         * if(prodMap.get("BEHAVES_LIKE").equals("DAILY")){
         * ttIntgration.integration("DailyLedger"); }else{
         * ttIntgration.integration(prodType+"Ledger"); } } }else{
         * ttIntgration.integration(prodType+"Ledger"); } }else{
         * ClientUtil.displayAlert("Account No Should Not Be Empty!!! "); }
         */
    }//GEN-LAST:event_btnViewLedgerActionPerformed

    private void btnMembershipLiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMembershipLiaActionPerformed
        // TODO add your handling code here:
        if (acctNo != null && !acctNo.equalsIgnoreCase("")) {
            new MembershipLiabilityUI(acctNo).show();
        }
    }//GEN-LAST:event_btnMembershipLiaActionPerformed

	private void btnUnclearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnclearActionPerformed
            String str = "";

            if (prodType.equals("GL")) {
                str = "GL";
            }

            HashMap whereMap = new HashMap();
            whereMap.put("ACCT_NO", acctNo);
            whereMap.put("BRANCH_ID", branch);

            TableDialogUI tableData = new TableDialogUI("getUnclearBalance" + str, whereMap);
            tableData.setTitle(transRB.getString("UnclearBalanceTitle"));
            tableData.show();
    }//GEN-LAST:event_btnUnclearActionPerformed

	private void btnPhotoSignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhotoSignActionPerformed
            if (acctNo != null && !acctNo.equalsIgnoreCase("") && !prodType.equals("GL") && !prodType.equals("AB")) { // GL checking added by nithya on 03-07-2020 for KD-2018
                new ViewPhotoSignUI(acctNo, prodType).show();
            }
    }//GEN-LAST:event_btnPhotoSignActionPerformed

	private void btnShadowDRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShadowDRActionPerformed
            String str = "";

            if (prodType.equals("GL")) {
                str = "GL";
            }

            HashMap whereMap = new HashMap();
            whereMap.put("ACCT_NO", acctNo);
            whereMap.put("BRANCH_ID", branch);

            TableDialogUI tableData = new TableDialogUI("getShadowDebit" + str, whereMap);
            tableData.setTitle(transRB.getString("ShadowDebitTitle") + this.shadowDebit);
            tableData.show();
    }//GEN-LAST:event_btnShadowDRActionPerformed
    public long roundOffLower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }
	private void btnShadowCRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShadowCRActionPerformed
            String str = "";

            if (prodType.equals("GL")) {
                str = "GL";
            }

            HashMap whereMap = new HashMap();
            whereMap.put("ACCT_NO", acctNo);
            whereMap.put("BRANCH_ID", branch);

            TableDialogUI tableData = new TableDialogUI("getShadowCredit" + str, whereMap);
            tableData.setTitle(transRB.getString("ShadowCreditTitle") + this.shadowCredit);
            tableData.show();
}//GEN-LAST:event_btnShadowCRActionPerformed
    private void btnChqBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChqBookActionPerformed
        if (acctNo != null && !acctNo.equalsIgnoreCase("")) {
            new ChequeDetailsUI(acctNo).show();
        }
    }//GEN-LAST:event_btnChqBookActionPerformed
    private void btnStandInstructionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStandInstructionActionPerformed
        if (acctNo != null && !acctNo.equalsIgnoreCase("")) {
            new SITransDetailsUI(acctNo, prodType, productId).show();
        }
    }//GEN-LAST:event_btnStandInstructionActionPerformed
    private void btnReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportActionPerformed
        if (acctNo != null && !acctNo.equalsIgnoreCase("") && (prodType.equals("TL") || getSourceScreen().equals("LOAN_ACT_CLOSING"))) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("ACT_NUM", acctNo);
            paramMap.put("ACT_NUM_TO", acctNo);
            paramMap.put("USER_ID", ProxyParameters.USER_ID);
            paramMap.put("DAY_END_DT", currDt.clone());
            paramMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            ttIntgration.setParam(paramMap);
            HashMap prodMap = new HashMap();
            ttIntgration.integration("interestReport");
        }
    }//GEN-LAST:event_btnReportActionPerformed

	private void btnLiabilityReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLiabilityReportActionPerformed
 //    
         
//         if(prodType.equalsIgnoreCase("GL")){
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            // paramMap.put("MemNo",memNo);
            paramMap.put("MemberNo",memNo);
            paramMap.put("AsOnDt", currDt.clone());
            ttIntgration.setParam(paramMap);
            //System.out.println("paramMap"+paramMap);
           
            	ttIntgration.integration("MemberLiabilityRegisterDetF2");
         	
//            return;
//         }
	}//GEN-LAST:event_btnLiabilityReportActionPerformed

    private void btnPendingTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendingTransactionActionPerformed
        // TODO add your handling code here:
        if (prodType != null && !prodType.equals("") && prodType.length() > 0) {//If Condition Added By Suresh      //To Avoid Null Poiter Exception.
            HashMap whereMap = new HashMap();
            String str = "";
            whereMap.put("ACCT_NO", acctNo);
            if (prodType.equals("GL")) {
                str="GL";
            }
            TableDialogUI tableDataCrDrUb = new TableDialogUI("getShadowCredit" + str, whereMap);
            tableDataCrDrUb.setTitle("Credit / Debit / UnClear Transactions");
            tableDataCrDrUb.show();
        }
    }//GEN-LAST:event_btnPendingTransactionActionPerformed

    private void btnBillsTransactionDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillsTransactionDetailsActionPerformed
        if (acctNo != null && !acctNo.equalsIgnoreCase("")) {
            new BillsTransDetailsUI(acctNo, prodType, productId).show();
        }
    }//GEN-LAST:event_btnBillsTransactionDetailsActionPerformed

    private void btnRepaySheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRepaySheduleActionPerformed
        //System.out.println("repayData ------ XX :" + repayData);
        if (repayData != null && repayData.size() > 0) {
            new TermLoanInstallmentUI(this, repayData, true).show();
        }
    }//GEN-LAST:event_btnRepaySheduleActionPerformed
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        javax.swing.JFrame jf = new javax.swing.JFrame();

        TransDetailsUI bui = new TransDetailsUI(null);
        bui.setTransDetails("OA", "Bran", "OA001001");
        jf.setSize(300, 525);
        jf.getContentPane().add(bui);
        jf.show();
        bui.show();
    }

    /**
     * Getter for property availableBalance.
     *
     * @return Value of property availableBalance.
     *
     */
    public java.lang.String getAvailableBalance() {
        return availableBalance;
    }

    /**
     * Setter for property availableBalance.
     *
     * @param availableBalance New value of property availableBalance.
     */
    public void setAvailableBalance(java.lang.String availableBalance) {
        this.availableBalance = availableBalance;
    }

    /**
     * Getter for property shadowCredit.
     *
     * @return Value of property shadowCredit.
     */
    public java.lang.String getShadowCredit() {
        return shadowCredit;
    }

    /**
     * Setter for property shadowCredit.
     *
     * @param shadowCredit New value of property shadowCredit.
     */
    public void setShadowCredit(java.lang.String shadowCredit) {
        this.shadowCredit = shadowCredit;
    }

    /**
     * Getter for property shadowDebit.
     *
     * @return Value of property shadowDebit.
     */
    public java.lang.String getShadowDebit() {
        return shadowDebit;
    }

    /**
     * Setter for property shadowDebit.
     *
     * @param shadowDebit New value of property shadowDebit.
     */
    public void setShadowDebit(java.lang.String shadowDebit) {
        this.shadowDebit = shadowDebit;
    }

    /**
     * Getter for property lienAmount.
     *
     * @return Value of property lienAmount.
     */
    public java.lang.String getLienAmount() {
        return lienAmount;
    }

    /**
     * Setter for property lienAmount.
     *
     * @param lienAmount New value of property lienAmount.
     */
    public void setLienAmount(java.lang.String lienAmount) {
        this.lienAmount = lienAmount;
    }

    /**
     * Getter for property freezeAmount.
     *
     * @return Value of property freezeAmount.
     */
    public java.lang.String getFreezeAmount() {
        return freezeAmount;
    }

    /**
     * Setter for property freezeAmount.
     *
     * @param freezeAmount New value of property freezeAmount.
     */
    public void setFreezeAmount(java.lang.String freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    /**
     * Getter for property unclearBalance.
     *
     * @return Value of property unclearBalance.
     */
    public java.lang.String getUnclearBalance() {
        return unclearBalance;
    }

    /**
     * Setter for property unclearBalance.
     *
     * @param unclearBalance New value of property unclearBalance.
     */
    public void setUnclearBalance(java.lang.String unclearBalance) {
        this.unclearBalance = unclearBalance;
    }

    /**
     * Getter for property limitAmount.
     *
     * @return Value of property limitAmount.
     */
    public java.lang.String getLimitAmount() {
        return limitAmount;
    }

    /**
     * Setter for property limitAmount.
     *
     * @param limitAmount New value of property limitAmount.
     */
    public void setLimitAmount(java.lang.String limitAmount) {
        this.limitAmount = limitAmount;
    }

    /**
     * Getter for property isMultiDisburse.
     *
     * @return Value of property isMultiDisburse.
     */
    public java.lang.String getIsMultiDisburse() {
        return isMultiDisburse;
    }

    /**
     * Setter for property isMultiDisburse.
     *
     * @param isMultiDisburse New value of property isMultiDisburse.
     */
    public void setIsMultiDisburse(java.lang.String isMultiDisburse) {
        this.isMultiDisburse = isMultiDisburse;
    }

    /**
     * Getter for property cBalance.
     *
     * @return Value of property cBalance.
     */
    public java.lang.String getCBalance() {
        return cBalance;
    }

    /**
     * Setter for property cBalance.
     *
     * @param cBalance New value of property cBalance.
     */
    public void setCBalance(java.lang.String cBalance) {
        this.cBalance = cBalance;
    }

    /**
     * Getter for property TermLoanCloseCharge.
     *
     * @return Value of property TermLoanCloseCharge.
     */
    public java.util.HashMap getTermLoanCloseCharge() {
        return TermLoanCloseCharge;
    }

    /**
     * Setter for property TermLoanCloseCharge.
     *
     * @param TermLoanCloseCharge New value of property TermLoanCloseCharge.
     */
    public void setTermLoanCloseCharge(java.util.HashMap TermLoanCloseCharge) {
        this.TermLoanCloseCharge = TermLoanCloseCharge;
    }

    /**
     * Getter for property avBalance.
     *
     * @return Value of property avBalance.
     */
    public java.lang.String getAvBalance() {
        return avBalance;
    }

    /**
     * Setter for property avBalance.
     *
     * @param avBalance New value of property avBalance.
     */
    public void setAvBalance(java.lang.String avBalance) {
        this.avBalance = avBalance;
    }

    /**
     * Getter for property lastApplDt.
     *
     * @return Value of property lastApplDt.
     */
    public java.lang.String getLastApplDt() {
        return lastApplDt;
    }

    /**
     * Setter for property lastApplDt.
     *
     * @param lastApplDt New value of property lastApplDt.
     */
    public void setLastApplDt(java.lang.String lastApplDt) {
        this.lastApplDt = lastApplDt;
    }

    /**
     * Getter for property intAmount.
     *
     * @return Value of property intAmount.
     */
    public java.lang.String getIntAmount() {
        return intAmount;
    }

    /**
     * Setter for property intAmount.
     *
     * @param intAmount New value of property intAmount.
     */
    public void setIntAmount(java.lang.String intAmount) {
        this.intAmount = intAmount;
    }

    /**
     * Getter for property isDebitSelect.
     *
     * @return Value of property isDebitSelect.
     */
    public boolean isIsDebitSelect() {
        return isDebitSelect;
    }

    /**
     * Setter for property isDebitSelect.
     *
     * @param isDebitSelect New value of property isDebitSelect.
     */
    public void setIsDebitSelect(boolean isDebitSelect) {
        this.isDebitSelect = isDebitSelect;
    }

    /**
     * Getter for property depInterestAmt.
     *
     * @return Value of property depInterestAmt.
     */
    public java.lang.String getDepInterestAmt() {
        return depInterestAmt;
    }

    /**
     * Setter for property depInterestAmt.
     *
     * @param depInterestAmt New value of property depInterestAmt.
     */
    public void setDepInterestAmt(java.lang.String depInterestAmt) {
        this.depInterestAmt = depInterestAmt;
    }

    /**
     * Getter for property expiryDate.
     *
     * @return Value of property expiryDate.
     */
    public java.lang.String getExpiryDate() {
        return expiryDate;
    }

    /**
     * Setter for property expiryDate.
     *
     * @param expiryDate New value of property expiryDate.
     */
    public void setExpiryDate(java.lang.String expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Getter for property refreshActDetails.
     *
     * @return Value of property refreshActDetails.
     */
    public boolean isRefreshActDetails() {
        return refreshActDetails;
    }

    /**
     * Setter for property refreshActDetails.
     *
     * @param refreshActDetails New value of property refreshActDetails.
     */
    public void setRefreshActDetails(boolean refreshActDetails) {
        this.refreshActDetails = refreshActDetails;
    }

    /**
     * Getter for property depositAmt.
     *
     * @return Value of property depositAmt.
     */
    public java.lang.String getDepositAmt() {
        return depositAmt;
    }

    /**
     * Setter for property depositAmt.
     *
     * @param depositAmt New value of property depositAmt.
     */
    public void setDepositAmt(java.lang.String depositAmt) {
        this.depositAmt = depositAmt;
    }

    /**
     * Getter for property asAndWhenMap.
     *
     * @return Value of property asAndWhenMap.
     */
    public java.util.HashMap getAsAndWhenMap() {
        return asAndWhenMap;
    }

    /**
     * Setter for property asAndWhenMap.
     *
     * @param asAndWhenMap New value of property asAndWhenMap.
     */
    public void setAsAndWhenMap(java.util.HashMap asAndWhenMap) {
        this.asAndWhenMap = asAndWhenMap;
    }

    /**
     * Getter for property penalAmount.
     *
     * @return Value of property penalAmount.
     */
    public java.lang.String getPenalAmount() {
        return penalAmount;
    }

    /**
     * Setter for property penalAmount.
     *
     * @param penalAmount New value of property penalAmount.
     */
    public void setPenalAmount(java.lang.String penalAmount) {
        this.penalAmount = penalAmount;
    }

    /**
     * Getter for property penalMonth.
     *
     * @return Value of property penalMonth.
     */
    public java.lang.String getPenalMonth() {
        return penalMonth;
    }

    /**
     * Setter for property penalMonth.
     *
     * @param penalMonth New value of property penalMonth.
     */
    public void setPenalMonth(java.lang.String penalMonth) {
        this.penalMonth = penalMonth;
    }

    /**
     * Getter for property flexiDepositAmt.
     *
     * @return Value of property flexiDepositAmt.
     */
    public java.lang.String getFlexiDepositAmt() {
        return flexiDepositAmt;
    }

    /**
     * Setter for property flexiDepositAmt.
     *
     * @param flexiDepositAmt New value of property flexiDepositAmt.
     */
    public void setFlexiDepositAmt(java.lang.String flexiDepositAmt) {
        this.flexiDepositAmt = flexiDepositAmt;
    }

    /**
     * Getter for property todAmount.
     *
     * @return Value of property todAmount.
     */
    public java.lang.String getTodAmount() {
        return todAmount;
    }

    /**
     * Setter for property todAmount.
     *
     * @param todAmount New value of property todAmount.
     */
    public void setTodAmount(java.lang.String todAmount) {
        this.todAmount = todAmount;
    }

    /**
     * Getter for property todUtilized.
     *
     * @return Value of property todUtilized.
     */
    public java.lang.String getTodUtilized() {
        return todUtilized;
    }

    /**
     * Setter for property todUtilized.
     *
     * @param todUtilized New value of property todUtilized.
     */
    public void setTodUtilized(java.lang.String todUtilized) {
        this.todUtilized = todUtilized;
    }

    public java.util.ArrayList getTblDataArrayList() {
          return ((EnhancedTableModel) tblTrans.getModel()).getDataArrayList();
    }

    /**
     * Getter for property corpDetailMap.
     *
     * @return Value of property corpDetailMap.
     */
    public java.util.Map getCorpDetailMap() {
        return corpDetailMap;
    }

    /**
     * Setter for property corpDetailMap.
     *
     * @param corpDetailMap New value of property corpDetailMap.
     */
    public void setCorpDetailMap(java.util.Map corpDetailMap) {
        this.corpDetailMap = corpDetailMap;
    }

    /**
     * Getter for property sourceScreen.
     *
     * @return Value of property sourceScreen.
     */
    public java.lang.String getSourceScreen() {
        return sourceScreen;
    }

    /**
     * Setter for property sourceScreen.
     *
     * @param sourceScreen New value of property sourceScreen.
     */
    public void setSourceScreen(java.lang.String sourceScreen) {
        this.sourceScreen = sourceScreen;
    }

    /**
     * Getter for property sourceFrame.
     *
     * @return Value of property sourceFrame.
     */
    public com.see.truetransact.uicomponent.CInternalFrame getSourceFrame() {
        return sourceFrame;
    }

    /**
     * Setter for property sourceFrame.
     *
     * @param sourceFrame New value of property sourceFrame.
     */
    public void setSourceFrame(com.see.truetransact.uicomponent.CInternalFrame sourceFrame) {
        this.sourceFrame = sourceFrame;
    }

    public long getInstallmentToPay() {
        return installmentToPay;
    }

    public void setInstallmentToPay(long installmentToPay) {
        this.installmentToPay = installmentToPay;
    }

    /**
     * Getter for property clearTransFlag.
     *
     * @return Value of property clearTransFlag.
     */
    public boolean isClearTransFlag() {
        return clearTransFlag;
    }

    /**
     * Setter for property clearTransFlag.
     *
     * @param clearTransFlag New value of property clearTransFlag.
     */
    public void setClearTransFlag(boolean clearTransFlag) {
        this.clearTransFlag = clearTransFlag;
    }

    public HashMap getOtherBankAccountMap() {
        return otherBankAccountMap;
    }

    public void setOtherBankAccountMap(HashMap otherBankAccountMap) {
        this.otherBankAccountMap = otherBankAccountMap;
    }
    
    private double intAmtForSalRecovery(){
        double intamt =0;
        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y") ) {
                HashMap hmap = new HashMap();
                hmap.put("ACT_NUM", CommonUtil.convertObjToStr(asAndWhenMap.get("ACCOUNTNO")));
                hmap.put("TRANS_DT", (Date)currDt.clone());
                hmap.put("REC_TYPE", "Direct");
                List resltList = ClientUtil.executeQuery("getPenalIntDetailsTL", hmap);
                double intVal = 0, princVal = 0;
                if (resltList != null && resltList.size() > 0) {
                    HashMap ansMap = (HashMap) resltList.get(0);
                    if (ansMap != null && ansMap.containsKey("RESULT")) {
                        String ans = CommonUtil.convertObjToStr(ansMap.get("RESULT"));
                        if (ans != null && ans.length() > 0) {
                            String[] ansArr = ans.split(":");
                            String intStr = "0", princStr = "0";
                            if (ansArr.length > 5) {
                                //System.out.println("ansArr[4] :" + ansArr[5]);
                                if (ansArr[5].contains("=")) {
                                    String[] splArr = ansArr[5].split("=");
                                    if (splArr.length > 1) {
                                        intStr = splArr[1].trim();
                                    }
                                }
                            }
                            if (ansArr.length > 4) {
                                //System.out.println("ansArr[4] :" + ansArr[4]);
                                if (ansArr[4].contains("=")) {
                                    String[] splArr = ansArr[4].split("=");
                                    if (splArr.length > 1) {
                                        princStr = splArr[1].trim();
                                    }
                                }
                            }
                            intVal = CommonUtil.convertObjToDouble(intStr);
                            princVal = CommonUtil.convertObjToDouble(princStr);
                            if (intVal > 0) {
                                intamt = intVal;
                            }
                        }
                    }
                }
            }
        return intamt;
    }
    //Added By Rishad At 19.july/2019 for revalidate transdetails grid after modfying details based in entering trans amount
    public void modifyTransDataBasedOnTransAmt(String key, double enteredData) {
        selectedRow = getRowByValue(tblTrans, key);
        System.out.println("rishad123" + selectedRow);
        if (selectedRow >= 0) {
            selectedList = (ArrayList) ((EnhancedTableModel) tblTrans.getModel()).getDataArrayList().get(selectedRow);
            if (key.equals("Available Rebate Interest")) {
                TermLoanCloseCharge.put("REBATE_INTEREST", new Double(enteredData));
            }
            selectedList.set(1, enteredData);
            ArrayList totalList = (ArrayList) getTblDataArrayList();
            totalList.set(selectedRow, selectedList);
            ((EnhancedTableModel) tblTrans.getModel()).setDataArrayList(totalList, col);
            tblTrans.revalidate();
        }
    }
    
    // Added by nithya on 01-01-20120 for KD-1191
    private void setIntPeriodRowColor(){
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
             int temprow = -1,tempcolumn = -1;
             public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                 super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                 Object actObj = tblTrans.getValueAt(row, column);                                
                 if(CommonUtil.convertObjToStr(actObj).equals("Interest Period") ||  (temprow == row && tempcolumn+1 == column)){
                     setForeground(Color.RED);
                     setFont(new java.awt.Font("MS Sans Serif", java.awt.Font.BOLD, 12));
                     temprow = row;
                     tempcolumn = column;
                 }else {
                     setForeground(Color.BLACK);
                 }
                 this.setOpaque(true);
                 return this;
             }
        };
       
        tblTrans.setDefaultRenderer(Object.class, renderer);
    }

    int getRowByValue(CTable _table, Object value) {
        for (int i = _table.getRowCount() - 1; i >= 0; --i) {
            for (int j = _table.getColumnCount() - 1; j >= 0; --j) {
                if (_table.getValueAt(i, j) != null && _table.getValueAt(i, j).equals(value)) {
                    return i;
                }
            }
        }
        return -1;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnBillsTransactionDetails;
    private com.see.truetransact.uicomponent.CButton btnChqBook;
    private com.see.truetransact.uicomponent.CButton btnLiabilityReport;
    private com.see.truetransact.uicomponent.CButton btnMembershipLia;
    private com.see.truetransact.uicomponent.CButton btnPendingTransaction;
    private com.see.truetransact.uicomponent.CButton btnPhotoSign;
    private com.see.truetransact.uicomponent.CButton btnRepayShedule;
    private com.see.truetransact.uicomponent.CButton btnReport;
    private com.see.truetransact.uicomponent.CButton btnShadowCR;
    private com.see.truetransact.uicomponent.CButton btnShadowDR;
    private com.see.truetransact.uicomponent.CButton btnStandInstruction;
    private com.see.truetransact.uicomponent.CButton btnUnclear;
    private com.see.truetransact.uicomponent.CButton btnViewLedger;
    private com.see.truetransact.uicomponent.CPanel panAcctDetails;
    private com.see.truetransact.uicomponent.CPanel panTrans;
    private com.see.truetransact.uicomponent.CPanel panTransDetails;
    private com.see.truetransact.uicomponent.CScrollPane scrAcctDetails;
    private com.see.truetransact.uicomponent.CScrollPane scrTransPan;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CTable tblAcctDetails;
    private com.see.truetransact.uicomponent.CTable tblTrans;
    private javax.swing.JToolBar tbrTrans;
    // End of variables declaration//GEN-END:variables
}
