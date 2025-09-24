/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * FixedAssetsTransUI.java
 *
 * Created on Jan 25, 2009, 10:53 AM
 */
package com.see.truetransact.ui.share;

import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observable;
import com.see.truetransact.ui.deposit.CommonMethods;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.clientexception.ClientParseException;
import javax.swing.JOptionPane;

import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.customer.deathmarking.DeathMarkingUI;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.ui.common.report.PrintClass;
import javax.swing.Popup;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
/**
 * This form is used to manipulate FixedAssetsUI related functionality
 *
 * @author
 */
//public class DrfTransactionUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {
public class DrfTransactionUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {

    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.share.DrfTransactionRB", ProxyParameters.LANGUAGE);
    DrfTransactionMRB objMandatoryRB = new DrfTransactionMRB();
    private HashMap mandatoryMap;
    private DrfTransactionOB observable;
    private TransactionUI transactionUI = new TransactionUI();
    private Date curDate = null;
    final int EDIT = 0, DELETE = 8, ACCNOCHEQUE = 2, ACCNOSTOP = 3, ACCNOLOOSE = 4, VIEW = 10, ECSSTOP = 7,RESOLUTIONNO=11;
    private int viewType = -1;
    private int BREAKAGE_ID = 1, MOVEMENT_ID = 2, FROM_ID = 3, TO_ID = 4, SALE_ID = 5, AUTHORIZE = 6;
    boolean isFilled = false;
    int updateTab = -1;
    private boolean updateMode = false;
    private boolean payment = false;
    private double amount = 0.0;
    private double productAmount = 0.0;
    private double paymentAmount = 0.0;
    private double productPaymentAmount = 0.0;
    boolean flag = false;
    private String viewTypeStr = ClientConstants.VIEW_TYPE_CANCEL;
    final int DRFTRANSACTION = 0;
    int pan = -1;
    int panEditDelete = -1;
    int view = -1;
    String DRF = "";
    private Object columnNames[] = {"DRF Id", "Member No", "Member Name", "Balance", "Interest"};
    private List bufferList = new ArrayList();
    public int memberNo = 45;
    private int rejectFlag=0;
    boolean fromAuthorizeUI = false;
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI=null;
    AuthorizeListCreditUI CashierauthorizeListUI=null;
    PrintClass print=new PrintClass();
    //    TransactionUI transactionUI2 = new TransactionUI(); //trans details
    double amtTrans = 0.0; //trans details
    double TOTAL = 0.0;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    public DrfTransactionUI() {


        initComponents();
        initStartup();
        transactionUI.setSourceScreen("DRF TRANSACTION");
        transactionUI.addToScreen(panTransaction);
        transactionUI.setTransactionMode(CommonConstants.DEBIT);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
    }

    private void initStartup() {
        curDate = ClientUtil.getCurrentDate();
        setFieldNames();
        internationalize();
        setHelpMessage();
        setMandatoryHashMap();
        setObservable();
        initTableData();
        setButtonEnableDisable();
        setMaximumLength();
        initComponentData();
        resetUI();
        ClientUtil.clearAll(this);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panDrfTransDetails, getMandatoryHashMap());
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), jPanel2, getMandatoryHashMap());
        ClientUtil.enableDisable(panDrfTransactionDetails, false);
        btnCancelActionPerformed(null);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(true);
        chkDueAmtPayment.setSelected(false);
        txtResolutionNo.setAllowAll(true);
        rdoDrfTransaction_Reciept.setSelected(true);
        lblDeathDt.setVisible(false); // Added by nithya on 26-04-2016 for 4307
        tdtDeathDt.setVisible(false); // Added by nithya on 26-04-2016 for 4307
    }

    private void initComponentData() {
        cboDrfTransProdID.setModel(observable.getCbmDrfTransProdID());
        cboProductID.setModel(observable.getCbmDrfTransProdID());
        cboProductID.setSelectedItem(cboDrfTransProdID.getItemAt(0));
        System.out.println("@#$@#$@#$cboDrfTransProdID.getItemAt(0)" + cboDrfTransProdID.getItemAt(0));
        cboDrfTransProdID.setSelectedItem(cboDrfTransProdID.getItemAt(0));
    }

    public void tabbi() {

        Object rowData[][] = {{"", "", "", "", ""}};
        // Object columnNames[] = { "DRF ID","Member No","Member Name","Balance","Interest" };

        tblInterest.setModel(new javax.swing.table.DefaultTableModel(rowData, columnNames) {
            public boolean isCellEditable(int row, int column) {
                //Only the third column
                return false;
            }
        });

        tblInterest.setVisible(true);

    }

    private void tableData() {

        Object rowData[][] = new Object[+bufferList.size()][5];
        String pid = cboProductID.getSelectedItem().toString();
        //  Integer rowData[][] = new  Integer[buffer1.size()][3];
        int j = 0;
        String d1 = "";
        String d2 = "";
        int i = 0;
        System.out.println("BuufferrList  " + bufferList.size());

        for (i = 0; i < bufferList.size(); i++) {
            HashMap m = new HashMap();
            m = (HashMap) bufferList.get(i);
            String iid = m.get("DRF_INTEREST_ID").toString();
            String mNo = m.get("MEMBER_NO").toString();
            String mName = m.get("MEMBER_NAME").toString();
            double interst = calculateInterest(pid, mNo, iid);
            System.out.println("intersmmmmmmmmmm...." + interst);
            if (interst < 0) {
                return;
            }
            m.put("INTEREST", interst);
            System.out.println("iii m00001 : " + m.get(""));
            rowData[i][0] = m.get("DRF_INTEREST_ID").toString();
            //System.out.println("iii m000011 : "+m.get("FROM"));
            rowData[i][1] = m.get("MEMBER_NO").toString();
            rowData[i][2] = m.get("MEMBER_NAME").toString();
            rowData[i][3] = m.get("AMOUNT").toString();
            rowData[i][4] = "" + interst;

            //   System.out.println("iii m0000 111: "+m.get("FROM"));
        }
        System.out.println("iii m0000 222: ");





        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        tblInterest.setModel(new javax.swing.table.DefaultTableModel(rowData, columnNames) {
            public boolean isCellEditable(int row, int column) {
                //Only the third column
                return false;
            }
        });
        tblInterest.setVisible(true);
        TOTAL = 0;
        for (i = 0; i < bufferList.size(); i++) {
            HashMap m = new HashMap();
            m = (HashMap) bufferList.get(i);
            double d = Double.parseDouble(m.get("INTEREST").toString());
            System.out.println("intterestttt>>>>" + d);
            TOTAL = TOTAL + d;
        }

        System.out.println("TOOOOTTTTT>" + TOTAL);

    }

    public double calculateInterest(String pid, String mNo, String iid) {
        double balance = 0.0;
        double it = 0.0;
        Calendar cal = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
        Date L_Date = null;
        Date T_Date = null;
        int year = 0;
        String LDate = "";
        String TDate = "";
        HashMap H_LastDate = new HashMap();
        H_LastDate.put("PROD_ID", pid);
        List L_LastDate = ClientUtil.executeQuery("getRateDts", H_LastDate);
        System.out.println("Prod id=" + pid + "  L_Lsit" + L_LastDate);
        HashMap H_LastDate1 = new HashMap();
        H_LastDate1 = (HashMap) L_LastDate.get(0);
        LDate = H_LastDate1.get("LAST_INTEREST_CALC_DATE").toString();
        try {
            L_Date = (Date) formatter.parse(LDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cal = Calendar.getInstance();
        cal.setTime(L_Date);
        LDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
        cal.add(Calendar.MONTH, 1);
        int nextMonth = cal.get(Calendar.MONTH);
        /*if(nextMonth==1) {
         year=cal.get(Calendar.YEAR)+1;
         }
         else {
         year=cal.get(Calendar.YEAR);
         }*/
        year = cal.get(Calendar.YEAR);
        System.out.println("----- LAST CALCULATED DATE--------" + LDate);

        HashMap H_LastBal = new HashMap();
        H_LastBal.put("PROD_ID", pid);
        H_LastBal.put("L_DATE", LDate);
        H_LastBal.put("MEMBERNO", mNo);

        List L_LastBal = ClientUtil.executeQuery("getBalance1", H_LastBal);
        if (L_LastBal.isEmpty()) {
            balance = 0.0;
        } else {
            HashMap H_LastBal1 = new HashMap();
            H_LastBal1 = (HashMap) L_LastBal.get(0);
            String bal = H_LastBal1.get("SUMM").toString();
            System.out.println("-----MEmeber------" + mNo);
            System.out.println("-----BALANCE AMOUNT------" + bal);
            balance = Double.parseDouble(bal);
        }
        cal = Calendar.getInstance();
        int day = 1;
        // int year=
        cal.set(year, nextMonth, day);
        day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(year, nextMonth, day);
        T_Date = cal.getTime();
        TDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
        System.out.println("------- TO DATE----------" + TDate);
        int o = 0;

        System.out.println("CurrenttDateeee...." + curDate + "      " + (T_Date.compareTo(curDate) == 0) + "            " + (T_Date.compareTo(curDate) < 0));
        //////////
        while ((T_Date.compareTo(curDate) == 0) || (T_Date.compareTo(curDate) < 0)) {
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOO..." + o);
            HashMap count = new HashMap();

            count.put("PROD_ID", pid);
            count.put("MEMBERNO", mNo);
            count.put("FROM", LDate);
            count.put("TO", TDate);
            List count1 = ClientUtil.executeQuery("getCountt", count);
            System.out.println("COUNNNTTTT>>>" + count1.size() + "    <<count>>>>" + count);
            String arry[][] = new String[count1.size() + 1][2];
            System.out.println("-----Count /Status Date of Trans---------" + count1);
            if (count1.size() == 0) {
            } else {
                int j = 0;
                for (int i = 0; i < count1.size(); i++) {

                    Date ddt2 = null;
                    HashMap m2 = new HashMap();
                    m2 = (HashMap) count1.get(i);
                    String dt2 = m2.get("STATUS_DATE").toString();
                    try {
                        ddt2 = (Date) formatter.parse(dt2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    cal = Calendar.getInstance();
                    cal.setTime(ddt2);
                    //......................             //to find begining balance..
                    if ((i == 0) && (cal.get(Calendar.DATE) != 1)) {
                        Date ddt1 = null;
                        HashMap m1 = new HashMap();
                        m1 = (HashMap) count1.get(i);
                        String dt1 = m1.get("STATUS_DATE").toString();
                        try {
                            ddt1 = (Date) formatter.parse(dt1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        cal = Calendar.getInstance();
                        cal.setTime(ddt1);
                        cal.add(Calendar.DATE, -1);
                        dt1 = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);

                        System.out.println("----------DATE TAKEN----" + dt1);


                        // HashMap H_LastBal=new HashMap();
                        H_LastBal.put("PROD_ID", pid);
                        H_LastBal.put("LDate", dt1);
                        H_LastBal.put("MEMBERNO", mNo);

                        L_LastBal = ClientUtil.executeQuery("getBalance1", H_LastBal);
                        if (!L_LastBal.isEmpty()) {
                            HashMap H_LastBal1 = new HashMap();
                            H_LastBal1 = (HashMap) L_LastBal.get(0);
                            String amount = H_LastBal1.get("SUMM").toString();
                            System.out.println("-----MEmeber------" + mNo);
                            System.out.println("-----BALANCE AMOUNT------" + amount);
                            double amt = Double.parseDouble(amount);
                            arry[j][0] = dt1;
                            arry[j][1] = "" + amt;
                            j = j + 1;
                            System.out.println("AMOUNT>>" + amt + " DAT>>>" + dt1);
                        }
                    }
                    Date ddt = null;
                    HashMap m = new HashMap();
                    m = (HashMap) count1.get(i);
                    String dt = m.get("STATUS_DATE").toString();
                    try {
                        ddt = (Date) formatter.parse(dt);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    cal = Calendar.getInstance();
                    cal.setTime(ddt);
                    dt = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);

                    System.out.println("----------DATE TAKEN----" + dt);


                    // HashMap H_LastBal=new HashMap();
                    H_LastBal.put("PROD_ID", pid);
                    H_LastBal.put("L_DATE", dt);
                    H_LastBal.put("MEMBERNO", mNo);

                    L_LastBal = ClientUtil.executeQuery("getBalance1", H_LastBal);

                    HashMap H_LastBal1 = new HashMap();
                    H_LastBal1 = (HashMap) L_LastBal.get(0);
                    String amount = H_LastBal1.get("SUMM").toString();
                    System.out.println("-----MEmeber------" + mNo);
                    System.out.println("-----BALANCE AMOUNT------" + amount);
                    double amt = Double.parseDouble(amount);
                    arry[j][0] = dt;
                    arry[j][1] = "" + amt;
                    j = j + 1;
                    System.out.println("AMOUNT>>" + amt + " DAT>>>" + dt);
                }


                double smaller = Double.parseDouble(arry[0][1]);
                String smallDt = arry[0][0];
                System.out.println("Smm1" + smaller);
                for (int i = 0; i < j; i++) {

                    double nw = Double.parseDouble(arry[i][1]);
                    System.out.println("NNNWWW>>" + nw);

                    if (nw < smaller) {

                        smaller = nw;
                        smallDt = arry[i][0];
                        System.out.println("smmmllrr>>" + smaller);
                    }
                }

                //         Date sm=null;
                //                try {
                //                    sm=(Date)formatter.parse(smallDt);
                //                  } catch (Exception e) {
                //                   e.printStackTrace();
                //                   }
                //
                //        cal = Calendar.getInstance();
                //        cal.setTime(sm);
                //        smallDt = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" +         cal.get(Calendar.YEAR);

                System.out.println("-----Smaller Amount And Date-------" + smaller + "  " + smallDt);

                HashMap rates = new HashMap();
                rates.put("PROD_ID", pid);
                rates.put("I_ID", iid);
                rates.put("DT", smallDt);
                List iRate = ClientUtil.executeQuery("getInterstRate", rates);
                if (iRate.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Interest Rate not set for this period", "Error", JOptionPane.ERROR_MESSAGE);
                    return -1;
                } else {
                    HashMap rateMap = new HashMap();
                    rateMap = (HashMap) iRate.get(0);
                    double irate = Double.parseDouble(rateMap.get("INTEREST_RATE").toString());
                    System.out.println("------INRDT RRTTT------" + irate);
                    double intst = smaller * (irate / 100);
                    System.out.println("-------INNTSTTTTTT----------" + intst);
                    it = it + intst;
                    System.out.println("-------INNTSTTTTTT actull----------" + it);
                }
            }

            L_Date = T_Date;

            LDate = TDate;
            System.out.println("-------  NExt FROM  DATE----------" + LDate);
            cal = Calendar.getInstance();
            cal.setTime(L_Date);
            // LDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" +         cal.get(Calendar.YEAR);
            cal.add(Calendar.MONTH, 1);
            nextMonth = cal.get(Calendar.MONTH);
//            if(nextMonth==1) {
//                year=cal.get(Calendar.YEAR)+1;
//            }
//            else {
//               
//            }

            year = cal.get(Calendar.YEAR);
            cal = Calendar.getInstance();
            day = 1;
            // int year=
            cal.set(year, nextMonth, day);
            day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            cal.set(year, nextMonth, day);
            T_Date = cal.getTime();
            TDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
            System.out.println("-------  NExt TO DATE----------" + TDate);
        }

        return it;
    }

    public void update(Observable observed, Object arg) {
        System.out.println("aswathy............");
        updateDrfTransUI();
    }
    ///* Auto Generated Method - getMandatoryHashMap()
    //   Getter method for setMandatoryHashMap().*/

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private void setMaximumLength() {
        txtDrfTransMemberNo.setMaxLength(16);
        txtDrfTransMemberNo.setAllowAll(true);
        txtDrfTransName.setMaxLength(128);
        txtDrfTransName.setAllowAll(true);
        txtDrfTransAmount.setMaxLength(14);
    }

    private void initTableData() {
        tblDrfTransaction.setModel(observable.getTblDrfTransaction());

    }

    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(!btnDelete.isEnabled());
        btnAuthorize.setEnabled(!btnAuthorize.isEnabled());
        btnReject.setEnabled(!btnReject.isEnabled());
        btnException.setEnabled(!btnException.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());

        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        btnClose.setEnabled(true);
    }

    private void setObservable() {
        /* Singleton pattern can't be implemented as there are two observers using the same observable*/
        // The parameter '1' indicates that the customer type is INDIVIDUAL
        observable = new DrfTransactionOB(1);
        observable.addObserver(this);
    }
    /* Auto Generated Method - setMandatoryHashMap()
      
     //ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     //
     This method list out all the Input Fields available in the UI.
     It needs a class level HashMap variable mandatoryMap. */

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtDrfTransMemberNo", new Boolean(true));
        mandatoryMap.put("txtDrfTransName", new Boolean(true));
        mandatoryMap.put("txtDrfTransAmount", new Boolean(true));
        mandatoryMap.put("cboDrfTransProdID", new Boolean(true));
        mandatoryMap.put("lblDrfTransAddressCont", new Boolean(true));
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("txtResolutionNo", new Boolean(false));
        mandatoryMap.put("tdtResolutionDate", new Boolean(false));


        // mandatoryMap.put("cboProductID",new Boolean(true));
    }
    /* Auto Generated Method - setHelpMessage()
     This method shows tooltip help for all the input fields
     available in the UI. It needs the Mandatory Resource Bundle
     object. Help display Label name should be lblMsg. */

    public void setHelpMessage() {
        objMandatoryRB = new DrfTransactionMRB();
        txtDrfTransAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDrfTransAmount"));
        cboDrfTransProdID.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDrfTransProdID"));
        txtDrfTransName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDrfTransName"));
        txtDrfTransMemberNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDrfTransMemberNo"));
    }

    /* Auto Generated Method - setFieldNames()
     This method assigns name for all the components.
     Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnView.setName("btnView");
        lblMsg.setName("lblMsg");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        panStatus.setName("panStatus");
        tblDrfTransaction.setName("tblDrfTransaction");
        txtDrfTransMemberNo.setName("txtDrfTransMemberNo");
        txtDrfTransName.setName("txtDrfTransName");
        txtDrfTransAmount.setName("txtDrfTransAmount");
        cboDrfTransProdID.setName("cboDrfTransProdID");
        lblDrfTransProdID.setName("lblDrfTransProdID");
        cboProductID.setName("cboProductID");
        txtInterestCalculationFrequency.setName("txtInterestCalculationFrequency");
        txtCalculationCriteria.setName("txtCalculationCriteria");
        txtProductFrequency.setName("txtProductFrequency");
        lblDrfTransMemberNo.setName("lblDrfTransMemberNo");
        lblDrfTransName.setName("lblDrfTransName");
        lblDrfTransAddress.setName("lblDrfTransAddress");
        lblDrfTransAmount.setName("lblDrfTransAmount");
        // txtDebitHead.setName("txtDebitHead");
        txtLastInterestCalculatedDate.setName("txtLastInterestCalculatedDate");
        lblDrfTransAddressCont.setName("lblDrfTransAddressCont");
        cboProductID.setName("cboProductID");
        lblProductID.setName("lblProductID");
        lblResolutionNo.setName("lblResolutionNo");
        txtResolutionNo.setName("txtResolutionNo");
        lblResolutionDate.setName("lblResolutionDate");
        tdtResolutionDate.setName("tdtResolutionDate");

    }

    private void internationalize() {
        resourceBundle = new DrfTransactionRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblDrfTransAmount.setText(resourceBundle.getString("lblDrfTransAmount"));
        lblDrfTransAddress.setText(resourceBundle.getString("lblDrfTransAddress"));
        lblDrfTransMemberNo.setText(resourceBundle.getString("lblDrfTransMemberNo"));
        lblDueAmtPayment.setText(resourceBundle.getString("lblDueAmtPayment"));
        ((javax.swing.border.TitledBorder) panDrfTransDetails.getBorder()).setTitle(resourceBundle.getString("panDrfTransDetails"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnView.setText(resourceBundle.getString("btnView"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        txtDrfTransMemberNo.setName("txtDrfTransMemberNo");
        txtDrfTransName.setName("txtDrfTransName");
        txtDrfTransAmount.setName("txtDrfTransAmount");
        cboDrfTransProdID.setName("cboDrfTransProdID");
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoDrfTransaction = new com.see.truetransact.uicomponent.CButtonGroup();
        panDrfTransactionDetails = new com.see.truetransact.uicomponent.CPanel();
        tabDrfTransaction = new com.see.truetransact.uicomponent.CTabbedPane();
        panDrfTransaction = new com.see.truetransact.uicomponent.CPanel();
        panDrfTransList = new com.see.truetransact.uicomponent.CPanel();
        srpDrfTransaction = new com.see.truetransact.uicomponent.CScrollPane();
        tblDrfTransaction = new com.see.truetransact.uicomponent.CTable();
        panDrfTransDetails = new com.see.truetransact.uicomponent.CPanel();
        lblDrfTransName = new com.see.truetransact.uicomponent.CLabel();
        lblDrfTransAddress = new com.see.truetransact.uicomponent.CLabel();
        lblDrfTransMemberNo = new com.see.truetransact.uicomponent.CLabel();
        lblDrfTransAmount = new com.see.truetransact.uicomponent.CLabel();
        txtDrfTransAmount = new com.see.truetransact.uicomponent.CTextField();
        panAutoRenewal = new com.see.truetransact.uicomponent.CPanel();
        rdoDrfTransaction_Reciept = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDrfTransaction_Payment = new com.see.truetransact.uicomponent.CRadioButton();
        txtDrfTransName = new com.see.truetransact.uicomponent.CTextField();
        chkDueAmtPayment = new com.see.truetransact.uicomponent.CCheckBox();
        lblDueAmtPayment = new com.see.truetransact.uicomponent.CLabel();
        lblDrfTransAddressCont = new com.see.truetransact.uicomponent.CLabel();
        lblDrfTransProdID = new com.see.truetransact.uicomponent.CLabel();
        cboDrfTransProdID = new com.see.truetransact.uicomponent.CComboBox();
        lblDeathMarking = new com.see.truetransact.uicomponent.CLabel();
        chkDeathMarking = new com.see.truetransact.uicomponent.CCheckBox();
        lblResolutionNo = new com.see.truetransact.uicomponent.CLabel();
        txtResolutionNo = new com.see.truetransact.uicomponent.CTextField();
        lblResolutionDate = new com.see.truetransact.uicomponent.CLabel();
        tdtResolutionDate = new com.see.truetransact.uicomponent.CDateField();
        panCustomerId = new com.see.truetransact.uicomponent.CPanel();
        txtDrfTransMemberNo = new com.see.truetransact.uicomponent.CTextField();
        ResolutionSearch = new com.see.truetransact.uicomponent.CButton();
        lblDeathDt = new com.see.truetransact.uicomponent.CLabel();
        tdtDeathDt = new com.see.truetransact.uicomponent.CDateField();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        cboProductID = new javax.swing.JComboBox();
        lblInterestCalculationFrequency = new com.see.truetransact.uicomponent.CLabel();
        lblCalculationCriteria = new com.see.truetransact.uicomponent.CLabel();
        lblProductFrequency = new com.see.truetransact.uicomponent.CLabel();
        lblLastInterestCalculatedDate = new com.see.truetransact.uicomponent.CLabel();
        txtInterestCalculationFrequency = new javax.swing.JTextField();
        txtCalculationCriteria = new javax.swing.JTextField();
        txtProductFrequency = new javax.swing.JTextField();
        txtLastInterestCalculatedDate = new javax.swing.JTextField();
        btnProcess = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInterest = new javax.swing.JTable();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace51 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace52 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace53 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace54 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace55 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace57 = new com.see.truetransact.uicomponent.CLabel();
        btnDateChange = new com.see.truetransact.uicomponent.CButton();
        mbrCustomer = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptNew = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptSave = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMaximumSize(new java.awt.Dimension(800, 670));
        setMinimumSize(new java.awt.Dimension(800, 670));
        setPreferredSize(new java.awt.Dimension(800, 670));

        panDrfTransactionDetails.setMaximumSize(new java.awt.Dimension(820, 520));
        panDrfTransactionDetails.setMinimumSize(new java.awt.Dimension(820, 520));
        panDrfTransactionDetails.setPreferredSize(new java.awt.Dimension(820, 520));
        panDrfTransactionDetails.setLayout(new java.awt.GridBagLayout());

        tabDrfTransaction.setMinimumSize(new java.awt.Dimension(845, 342));
        tabDrfTransaction.setPreferredSize(new java.awt.Dimension(845, 342));

        panDrfTransaction.setMinimumSize(new java.awt.Dimension(830, 313));
        panDrfTransaction.setPreferredSize(new java.awt.Dimension(830, 313));
        panDrfTransaction.setLayout(new java.awt.GridBagLayout());

        panDrfTransList.setMinimumSize(new java.awt.Dimension(830, 300));
        panDrfTransList.setPreferredSize(new java.awt.Dimension(830, 300));
        panDrfTransList.setLayout(new java.awt.GridBagLayout());

        srpDrfTransaction.setMaximumSize(new java.awt.Dimension(470, 245));
        srpDrfTransaction.setMinimumSize(new java.awt.Dimension(470, 215));
        srpDrfTransaction.setPreferredSize(new java.awt.Dimension(470, 225));

        tblDrfTransaction.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Asset ID", "Face Value", "Current Value", "Sale Amount"
            }
        ));
        tblDrfTransaction.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblDrfTransaction.setMaximumSize(new java.awt.Dimension(2147483647, 10000));
        tblDrfTransaction.setMinimumSize(new java.awt.Dimension(350, 750));
        tblDrfTransaction.setOpaque(false);
        tblDrfTransaction.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblDrfTransaction.setPreferredSize(new java.awt.Dimension(350, 750));
        srpDrfTransaction.setViewportView(tblDrfTransaction);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 20, 0, 10);
        panDrfTransList.add(srpDrfTransaction, gridBagConstraints);

        panDrfTransDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("DRF Transaction"));
        panDrfTransDetails.setMinimumSize(new java.awt.Dimension(320, 285));
        panDrfTransDetails.setPreferredSize(new java.awt.Dimension(320, 285));
        panDrfTransDetails.setLayout(new java.awt.GridBagLayout());

        lblDrfTransName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDrfTransName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 6);
        panDrfTransDetails.add(lblDrfTransName, gridBagConstraints);

        lblDrfTransAddress.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDrfTransAddress.setText("Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 42, 6);
        panDrfTransDetails.add(lblDrfTransAddress, gridBagConstraints);

        lblDrfTransMemberNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDrfTransMemberNo.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 6);
        panDrfTransDetails.add(lblDrfTransMemberNo, gridBagConstraints);

        lblDrfTransAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDrfTransAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 6);
        panDrfTransDetails.add(lblDrfTransAmount, gridBagConstraints);

        txtDrfTransAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDrfTransAmount.setValidation(new NumericValidation());
        txtDrfTransAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDrfTransAmountActionPerformed(evt);
            }
        });
        txtDrfTransAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDrfTransAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 0);
        panDrfTransDetails.add(txtDrfTransAmount, gridBagConstraints);

        panAutoRenewal.setMaximumSize(new java.awt.Dimension(185, 16));
        panAutoRenewal.setMinimumSize(new java.awt.Dimension(185, 16));
        panAutoRenewal.setPreferredSize(new java.awt.Dimension(185, 16));
        panAutoRenewal.setLayout(new java.awt.GridBagLayout());

        rdoDrfTransaction.add(rdoDrfTransaction_Reciept);
        rdoDrfTransaction_Reciept.setText("Receipt");
        rdoDrfTransaction_Reciept.setToolTipText("Receipt");
        rdoDrfTransaction_Reciept.setMaximumSize(new java.awt.Dimension(95, 18));
        rdoDrfTransaction_Reciept.setMinimumSize(new java.awt.Dimension(95, 18));
        rdoDrfTransaction_Reciept.setPreferredSize(new java.awt.Dimension(95, 18));
        rdoDrfTransaction_Reciept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDrfTransaction_RecieptActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panAutoRenewal.add(rdoDrfTransaction_Reciept, gridBagConstraints);

        rdoDrfTransaction.add(rdoDrfTransaction_Payment);
        rdoDrfTransaction_Payment.setText("Payment");
        rdoDrfTransaction_Payment.setToolTipText("Payment");
        rdoDrfTransaction_Payment.setMaximumSize(new java.awt.Dimension(95, 18));
        rdoDrfTransaction_Payment.setMinimumSize(new java.awt.Dimension(95, 18));
        rdoDrfTransaction_Payment.setPreferredSize(new java.awt.Dimension(95, 18));
        rdoDrfTransaction_Payment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDrfTransaction_PaymentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panAutoRenewal.add(rdoDrfTransaction_Payment, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, -19, 1, 1);
        panDrfTransDetails.add(panAutoRenewal, gridBagConstraints);

        txtDrfTransName.setMinimumSize(new java.awt.Dimension(160, 21));
        txtDrfTransName.setPreferredSize(new java.awt.Dimension(160, 21));
        txtDrfTransName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDrfTransNameActionPerformed(evt);
            }
        });
        txtDrfTransName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDrfTransNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 0);
        panDrfTransDetails.add(txtDrfTransName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panDrfTransDetails.add(chkDueAmtPayment, gridBagConstraints);

        lblDueAmtPayment.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDueAmtPayment.setText("Due Amount Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        panDrfTransDetails.add(lblDueAmtPayment, gridBagConstraints);

        lblDrfTransAddressCont.setMaximumSize(new java.awt.Dimension(150, 60));
        lblDrfTransAddressCont.setMinimumSize(new java.awt.Dimension(150, 60));
        lblDrfTransAddressCont.setPreferredSize(new java.awt.Dimension(150, 60));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        panDrfTransDetails.add(lblDrfTransAddressCont, gridBagConstraints);

        lblDrfTransProdID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDrfTransProdID.setText("Product ID ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 6);
        panDrfTransDetails.add(lblDrfTransProdID, gridBagConstraints);

        cboDrfTransProdID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDrfTransProdID.setName("cboProfession"); // NOI18N
        cboDrfTransProdID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDrfTransProdIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 4);
        panDrfTransDetails.add(cboDrfTransProdID, gridBagConstraints);

        lblDeathMarking.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDeathMarking.setText("Death Marking");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        panDrfTransDetails.add(lblDeathMarking, gridBagConstraints);

        chkDeathMarking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDeathMarkingActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panDrfTransDetails.add(chkDeathMarking, gridBagConstraints);

        lblResolutionNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblResolutionNo.setText("Resolution No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 6);
        panDrfTransDetails.add(lblResolutionNo, gridBagConstraints);

        txtResolutionNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtResolutionNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtResolutionNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 4, 0);
        panDrfTransDetails.add(txtResolutionNo, gridBagConstraints);

        lblResolutionDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblResolutionDate.setText("Resolution Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        panDrfTransDetails.add(lblResolutionDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 0);
        panDrfTransDetails.add(tdtResolutionDate, gridBagConstraints);

        panCustomerId.setMinimumSize(new java.awt.Dimension(105, 23));
        panCustomerId.setPreferredSize(new java.awt.Dimension(105, 23));
        panCustomerId.setLayout(new java.awt.GridBagLayout());

        txtDrfTransMemberNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDrfTransMemberNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDrfTransMemberNoActionPerformed(evt);
            }
        });
        txtDrfTransMemberNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDrfTransMemberNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustomerId.add(txtDrfTransMemberNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panDrfTransDetails.add(panCustomerId, gridBagConstraints);

        ResolutionSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        ResolutionSearch.setEnabled(false);
        ResolutionSearch.setMaximumSize(new java.awt.Dimension(21, 21));
        ResolutionSearch.setMinimumSize(new java.awt.Dimension(21, 21));
        ResolutionSearch.setPreferredSize(new java.awt.Dimension(21, 21));
        ResolutionSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResolutionSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 35);
        panDrfTransDetails.add(ResolutionSearch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 10, 8, 0);
        panDrfTransList.add(panDrfTransDetails, gridBagConstraints);

        lblDeathDt.setText("Date of Death");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 46, 0, 0);
        panDrfTransList.add(lblDeathDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 0);
        panDrfTransList.add(tdtDeathDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 36, 0, 0);
        panDrfTransaction.add(panDrfTransList, gridBagConstraints);

        panTransaction.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        panTransaction.setMinimumSize(new java.awt.Dimension(552, 95));
        panTransaction.setPreferredSize(new java.awt.Dimension(552, 95));
        panTransaction.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 317;
        gridBagConstraints.ipady = 139;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 17, 2, 17);
        panDrfTransaction.add(panTransaction, gridBagConstraints);

        tabDrfTransaction.addTab("Death Relief Fund", panDrfTransaction);

        jPanel1.setMinimumSize(new java.awt.Dimension(815, 498));
        jPanel1.setPreferredSize(new java.awt.Dimension(815, 498));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Interest Details"));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        lblProductID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 24;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(lblProductID, gridBagConstraints);

        cboProductID.setMaximumSize(new java.awt.Dimension(101, 21));
        cboProductID.setMinimumSize(new java.awt.Dimension(101, 21));
        cboProductID.setPreferredSize(new java.awt.Dimension(101, 21));
        cboProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(cboProductID, gridBagConstraints);

        lblInterestCalculationFrequency.setText("Interest Calculation frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(lblInterestCalculationFrequency, gridBagConstraints);

        lblCalculationCriteria.setText("Calculation Criteria");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(lblCalculationCriteria, gridBagConstraints);

        lblProductFrequency.setText("Product Frequency ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(lblProductFrequency, gridBagConstraints);

        lblLastInterestCalculatedDate.setText("Last Interest calculated Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(lblLastInterestCalculatedDate, gridBagConstraints);

        txtInterestCalculationFrequency.setMaximumSize(new java.awt.Dimension(101, 21));
        txtInterestCalculationFrequency.setMinimumSize(new java.awt.Dimension(101, 21));
        txtInterestCalculationFrequency.setPreferredSize(new java.awt.Dimension(101, 21));
        txtInterestCalculationFrequency.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInterestCalculationFrequencyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(txtInterestCalculationFrequency, gridBagConstraints);

        txtCalculationCriteria.setMaximumSize(new java.awt.Dimension(101, 21));
        txtCalculationCriteria.setMinimumSize(new java.awt.Dimension(101, 21));
        txtCalculationCriteria.setPreferredSize(new java.awt.Dimension(101, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(txtCalculationCriteria, gridBagConstraints);

        txtProductFrequency.setMaximumSize(new java.awt.Dimension(101, 21));
        txtProductFrequency.setMinimumSize(new java.awt.Dimension(101, 21));
        txtProductFrequency.setPreferredSize(new java.awt.Dimension(101, 21));
        txtProductFrequency.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductFrequencyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(txtProductFrequency, gridBagConstraints);

        txtLastInterestCalculatedDate.setMaximumSize(new java.awt.Dimension(101, 21));
        txtLastInterestCalculatedDate.setMinimumSize(new java.awt.Dimension(101, 21));
        txtLastInterestCalculatedDate.setPreferredSize(new java.awt.Dimension(101, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(txtLastInterestCalculatedDate, gridBagConstraints);

        btnProcess.setText("Process");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(btnProcess, gridBagConstraints);

        jPanel1.add(jPanel2, new java.awt.GridBagConstraints());

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setMinimumSize(new java.awt.Dimension(430, 258));
        jPanel3.setPreferredSize(new java.awt.Dimension(430, 258));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setMinimumSize(new java.awt.Dimension(22, 26));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(453, 250));

        tblInterest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "DRF Id", "Member No", "Member Name", "Balance", "Title 5"
            }
        ));
        tblInterest.setMinimumSize(new java.awt.Dimension(200, 150));
        tblInterest.setPreferredSize(new java.awt.Dimension(450, 200));
        jScrollPane1.setViewportView(tblInterest);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 431;
        gridBagConstraints.ipady = 224;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel3.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jPanel3, gridBagConstraints);

        tabDrfTransaction.addTab("Death Relief Fund Interest Calculation", jPanel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panDrfTransactionDetails.add(tabDrfTransaction, gridBagConstraints);

        getContentPane().add(panDrfTransactionDetails, java.awt.BorderLayout.CENTER);

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

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setFocusable(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.setFocusable(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnNew);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace52);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.setFocusable(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnSave);

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace53);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnAuthorize);

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setFocusable(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace55);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.setFocusable(false);
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace56);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setFocusable(false);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        lblSpace57.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace57.setText("     ");
        lblSpace57.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace57);

        btnDateChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/denomination.gif"))); // NOI18N
        btnDateChange.setToolTipText("Exception");
        tbrOperativeAcctProduct.add(btnDateChange);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

        mbrCustomer.setName("mbrCustomer"); // NOI18N

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess"); // NOI18N

        mitNew.setText("New");
        mitNew.setName("mitNew"); // NOI18N
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit"); // NOI18N
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete"); // NOI18N
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew"); // NOI18N
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave"); // NOI18N
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel"); // NOI18N
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave"); // NOI18N
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint"); // NOI18N
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose"); // NOI18N
        mnuProcess.add(mitClose);

        mbrCustomer.add(mnuProcess);

        setJMenuBar(mbrCustomer);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtDrfTransAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDrfTransAmountActionPerformed
        // TODO add your handling code here:
        transactionUI.setCallingApplicantName(txtDrfTransName.getText());
        // TODO add your handling code here:
        transactionUI.setCallingAmount(txtDrfTransAmount.getText());
    }//GEN-LAST:event_txtDrfTransAmountActionPerformed

    private void txtDrfTransNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDrfTransNameActionPerformed
        transactionUI.setCallingApplicantName(txtDrfTransName.getText());
    }//GEN-LAST:event_txtDrfTransNameActionPerformed

    private void txtDrfTransNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDrfTransNameFocusLost
        // TODO add your handling code here:
        // transactionUI.setCallingAmount(txtDrfTransAmount.getText());
        transactionUI.setCallingApplicantName(txtDrfTransName.getText());
    }//GEN-LAST:event_txtDrfTransNameFocusLost

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        bufferList.clear();
        tableData();


        if (cboProductID.getSelectedIndex() == 0) {
            String strMandatory = "Select Product ID";
            CommonMethods.displayAlert(strMandatory);
            return;
        }

        System.out.println("YOOOOOOOOOOOOOOOOOOOOOOOOOOOOOooo");
        String formatedDate = "";

        Calendar cal = null;

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
        Date last = null;
        Date currnt = null;

        HashMap h = new HashMap();
        h.put("PROD_ID", cboProductID.getSelectedItem().toString());
        List rateList = ClientUtil.executeQuery("getRateDts", h);
        if (rateList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Interest Details not set ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        HashMap rateMap = new HashMap();
        rateMap = (HashMap) rateList.get(0);
        String dt1 = rateMap.get("LAST_INTEREST_CALC_DATE").toString();
        //last_interest_calc_date//
        System.out.println("DDATE1>>" + rateMap.get("LAST_INTEREST_CALC_DATE").toString());


        try {
            last = (Date) formatter.parse(dt1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("DATEEEE 11>>>" + last);
        cal = Calendar.getInstance();
        cal.setTime(last);
        String formatedDate1 = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
        System.out.println("LAST DATE>>>..." + formatedDate1);
        h.put("L_DATE", formatedDate1);
        System.out.println("WATZZZZZZZZZZZZZZZZZZ iTTTTTTTTTTTTT?????????????");
        List list1 = ClientUtil.executeQuery("getTabDts", h);
        if (list1.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transaction details found for product from last calculated date ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("List1  Size+ " + list1.size());
        //  List list1=null;
        System.out.println("WATZZZZZZZZZZZZZZZZZZ iTTTTTTTTTTTTT?????????????");
        //h.put();
        List list2 = ClientUtil.executeQuery("getMemberGroup", h);
        System.out.println("List2  Size+" + list2.size());
        int xc = 0;
        System.out.println("WATZZZZZZZZZZZZZZZZZZ iTTTTTTTTTTTTT?????????????");

        for (int k = 0; k < list2.size(); k++) {
            HashMap hn = new HashMap();
            hn = (HashMap) list2.get(k);
            System.out.println("WATZZZZZZZZZZZZZZZZZZ iTTTTTTTTTTTTT?????????????");
            for (int r = 0; r < list1.size(); r++) {
                HashMap gn = new HashMap();
                gn = (HashMap) list1.get(r);
                String dt2 = gn.get("STATUS_DATE").toString();

                try {
                    currnt = (Date) formatter.parse(dt2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("DATEEEE >>>" + currnt);
                //&&(currnt.after(last))&&(currnt.after(last))
                System.out.println("DATEEEE bnn >>>" + currnt.after(last));

                double intrt = 0.0;
                if (hn.get("MEMBER_NO").toString().equals(gn.get("MEMBER_NO").toString())) {

                    HashMap b1 = new HashMap();
                    b1.put("DRF_INTEREST_ID", gn.get("DRF_INTEREST_ID"));
                    b1.put("MEMBER_NO", gn.get("MEMBER_NO"));
                    b1.put("MEMBER_NAME", gn.get("MEMBER_NAME"));
                    b1.put("AMOUNT", hn.get("SUM"));
                    //   b1.put("INTERST",intrt);
                    bufferList.add(b1);
                    xc++;
                    System.out.println("ii>>" + xc + "buferlst>>" + bufferList.size() + "   " + bufferList);



                    break;
                }
            }
        }



        System.out.println("BBBBFFRRR....." + bufferList.size() + "  list>>" + list1.size());
        if (bufferList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Record Found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {


            tableData();
        }

    }//GEN-LAST:event_btnProcessActionPerformed

    private void cboProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIDActionPerformed
        HashMap h = new HashMap();
        bufferList.clear();
        tableData();
        h.put("PROD_ID", cboProductID.getSelectedItem().toString());
        List list = ClientUtil.executeQuery("getProdctDts", h);
        System.out.println("lllliiiiissstttt" + list.size());
        if (list != null && list.size() > 0) {
            HashMap hm = new HashMap();
            hm = (HashMap) list.get(0);
            txtCalculationCriteria.setText(hm.get("INTEREST_CALC_CRITERIA").toString());
            txtInterestCalculationFrequency.setText(hm.get("INTEREST_CALC_FREQUENCY").toString());
            txtProductFrequency.setText(hm.get("INTEREST_PRODUCT_FREQUENCY").toString());


            //
            Date date = null;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
            Calendar cal = null;
            String formatedDate = "";
            //         if(!isValidDate(s1))   {


            try {
                date = (Date) formatter.parse(hm.get("LAST_INTEREST_CALC_DATE").toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(date);

            cal = Calendar.getInstance();
            cal.setTime(date);
            formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
            System.out.println("formatedDate : " + formatedDate);

            //
            txtLastInterestCalculatedDate.setText(formatedDate);


        }

    }//GEN-LAST:event_cboProductIDActionPerformed

    private void txtProductFrequencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductFrequencyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductFrequencyActionPerformed

    private void txtInterestCalculationFrequencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInterestCalculationFrequencyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInterestCalculationFrequencyActionPerformed

    private void txtDrfTransMemberNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDrfTransMemberNoActionPerformed
        if (txtDrfTransMemberNo.getText().length() > 0) {
            double dueAmount = 0.0;
            txtDrfTransMemberNo.setText(CommonUtil.convertObjToStr(txtDrfTransMemberNo.getText()).toUpperCase());
            observable.resetDrfTransListTable();

            HashMap memberDetMap = new HashMap();
            memberDetMap.put("MEMBERSHIP_NO", CommonUtil.convertObjToStr(txtDrfTransMemberNo.getText()));
            memberDetMap.put("PROD_ID", CommonUtil.convertObjToStr(cboDrfTransProdID.getSelectedItem()));
            //            this query is to see if the member no entered is correct
            if (rdoDrfTransaction_Payment.isSelected()) {
                List list1 = ClientUtil.executeQuery("getPaymentDetails", memberDetMap);
                List list = ClientUtil.executeQuery("getRecieptDetails", memberDetMap);


                memberDetMap.put("PAYMENT", "PAYMENT");
                List unauthList = ClientUtil.executeQuery("getSelectUnAuthList", memberDetMap);
                if (unauthList != null && unauthList.size() > 0) {
                    ClientUtil.displayAlert("Already Record exists for authorization can not add a new record");
                    txtDrfTransMemberNo.setText("");
                    return;
                }
                int z = 0;
                if (list != null && list.size() > 0) {
                    if (list1 != null && list1.size() > 0 && z == 0) {

                        int a = ClientUtil.confirmationAlert("Already One Payment is there, Do you want to continue?");
                        int b = 0;
                        z = 1;
                        if (a != b) {
                            txtDrfTransMemberNo.setText("");
                            return;
                        }
                    }
                } else {
                    ClientUtil.displayAlert("No receipt entry available for this member hence no payment can be made");
                    txtDrfTransMemberNo.setText("");
                    return;
                }
            }
            if (rdoDrfTransaction_Reciept.isSelected()) {

                //                 List list=ClientUtil.executeQuery("getRecieptDetails", memberDetMap);

                memberDetMap.put("RECIEPT", "RECIEPT");
                List unauthList = ClientUtil.executeQuery("getSelectUnAuthList", memberDetMap);
                if (unauthList != null && unauthList.size() > 0) {
                    ClientUtil.displayAlert("Already Record exists for authorization can not add a new record");
                    txtDrfTransMemberNo.setText("");
                    return;
                }
                //                 if(list!=null && list.size()>0){
                //
                //                     int a=ClientUtil.confirmationAlert("Already One Receipt exists, Do you want to continue?");
                //                     int b=0;
                //                     if(a!=b){
                //                         txtDrfTransMemberNo.setText("");
                //                         return;
                //                     }
                //                 }
            }
            List memberDetails = ClientUtil.executeQuery("getMemberDetailsForDrf", memberDetMap);
            if (memberDetails != null & memberDetails.size() > 0) {
                memberDetMap = (HashMap) memberDetails.get(0);
                memberDetMap.put("PROD_ID", CommonUtil.convertObjToStr(cboDrfTransProdID.getSelectedItem()));
                System.out.println("#@$@#$@#memberDetMap" + memberDetMap);
                txtDrfTransName.setText(CommonUtil.convertObjToStr(memberDetMap.get("NAME")));
                txtDrfTransName.setEnabled(false);
                lblDrfTransAddressCont.setText(CommonUtil.convertObjToStr(memberDetMap.get("ADDRESS")));

                //                this query is used to check if the member no has already made a transaction before.
                List memberDrfTransDetails = ClientUtil.executeQuery("getMemberDrfTransDetails", memberDetMap);
                if (memberDrfTransDetails != null && memberDrfTransDetails.size() > 0) {
                    observable.populateDrfTransTable(memberDrfTransDetails);
                    HashMap memberTransDetailsMap = (HashMap) memberDrfTransDetails.get(0);
                    double amountPaid = CommonUtil.convertObjToDouble(memberTransDetailsMap.get("AMOUNT")).doubleValue();
                    System.out.println("#@$@#$@#$amountPaid:" + amountPaid);
                    System.out.println("@#$@#$@#$memberTransDetailsMap:" + memberTransDetailsMap);
                    memberTransDetailsMap.put("PROD_ID", CommonUtil.convertObjToStr(cboDrfTransProdID.getSelectedItem()));
                    if (rdoDrfTransaction_Reciept.isSelected() == true) {
                        if (amount >= amountPaid) {
                            double newAmount = amount - amountPaid;
                            txtDrfTransAmount.setText(String.valueOf(newAmount));
                        }
                    } else if (rdoDrfTransaction_Payment.isSelected() == true) {
                        txtDrfTransAmount.setText(String.valueOf(paymentAmount));
                    }


                    chkDueAmtPayment.setSelected(true);
                    chkDueAmtPayment.setEnabled(true);
                } else {
                    HashMap memberTransDetailsMap = new HashMap();
                    memberTransDetailsMap.put("PROD_ID", CommonUtil.convertObjToStr(cboDrfTransProdID.getSelectedItem()));

                    //                    this query is added to get the amount details for the given PROD_ID
                    txtDrfTransAmount.setText(String.valueOf(amount));
                    chkDueAmtPayment.setSelected(false);
                    chkDueAmtPayment.setEnabled(false);

                }
                observable.setTxtDrfTransAmount(txtDrfTransAmount.getText());
                transactionUI.setCallingAmount(observable.getTxtDrfTransAmount());

            } else {
                ClientUtil.showAlertWindow("Entered Membership Number not found");
                txtDrfTransMemberNo.setText("");
                txtDrfTransAmount.setText("");
                txtDrfTransName.setText("");
                lblDrfTransAddressCont.setText("");
            }
        }
    }//GEN-LAST:event_txtDrfTransMemberNoActionPerformed

    private void chkDeathMarkingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDeathMarkingActionPerformed
        // TODO add your handling code here:
        if (rdoDrfTransaction_Payment.isSelected()) {

            HashMap hmap = new HashMap();
            hmap.put("MEMBER_NO", txtDrfTransMemberNo.getText());
            List list = ClientUtil.executeQuery("getMarkingDetailsForDRF", hmap);            
            if (list != null && list.size() > 0) {
                ClientUtil.displayAlert("Already Death marking Details are entered for this customer");
                chkDeathMarking.setSelected(false);
            }
        } else {
            chkDeathMarking.setSelected(false);
        }

    }//GEN-LAST:event_chkDeathMarkingActionPerformed

    private void rdoDrfTransaction_PaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDrfTransaction_PaymentActionPerformed
        // TODO add your handling code here:
//        paymentAmount = productPaymentAmount - productAmount;
//        changed here
        payment = true;
        HashMap hmap = new HashMap();

        hmap.put("PAYMENT", "PAYMENT");
//        List list=ClientUtil.executeQuery("getSelectUnAuthList", hmap);
//        if(list!=null && list.size()>0){
//            ClientUtil.displayAlert("Already Record exists for authorization can not add a new record");
//            txtDrfTransAmount.setText("");
//            return;
//        }else{
        paymentAmount = productPaymentAmount;
        txtDrfTransAmount.setText(String.valueOf(paymentAmount));
//        }
    }//GEN-LAST:event_rdoDrfTransaction_PaymentActionPerformed

    private void rdoDrfTransaction_RecieptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDrfTransaction_RecieptActionPerformed
        // TODO add your handling code here:
        HashMap hmap = new HashMap();

        hmap.put("RECIEPT", "RECIEPT");
//        List list=ClientUtil.executeQuery("getSelectUnAuthList", hmap);
//        if(list!=null && list.size()>0){
//            ClientUtil.displayAlert("Already Record exists for authorization can not add a new record");
//            txtDrfTransAmount.setText("");
//            return;
//        }else
        txtDrfTransAmount.setText(String.valueOf(amount));
        payment = false;

    }//GEN-LAST:event_rdoDrfTransaction_RecieptActionPerformed

    private void cboDrfTransProdIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDrfTransProdIDActionPerformed
        // TODO add your handling code here:
        String prodID = CommonUtil.convertObjToStr(cboDrfTransProdID.getSelectedItem());
        if (prodID.length() > 0) {
            HashMap drfProdDetailsMap = new HashMap();
            drfProdDetailsMap.put("PROD_ID", prodID);
            drfProdDetailsMap.put("CURRENT_DATE", curDate);
            System.out.println("#@$@#$@#$prodID" + prodID);
            List getDrfProdDetails = ClientUtil.executeQuery("getDrfProductDetailsForTrans", drfProdDetailsMap);
            System.out.println("@#$@#$getDrfProdDetails:" + getDrfProdDetails);
            if (getDrfProdDetails != null && getDrfProdDetails.size() > 0) {
                drfProdDetailsMap = (HashMap) getDrfProdDetails.get(0);
                amount = CommonUtil.convertObjToDouble(drfProdDetailsMap.get("AMOUNT")).doubleValue();
                productAmount = amount;
                paymentAmount = CommonUtil.convertObjToDouble(drfProdDetailsMap.get("PAYMENT")).doubleValue();
                productPaymentAmount = paymentAmount;
            }
        }
    }//GEN-LAST:event_cboDrfTransProdIDActionPerformed

    private void txtDrfTransMemberNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDrfTransMemberNoFocusLost
        // TODO add your handling code here:
        if (txtDrfTransMemberNo.getText().length() > 0) {
            double dueAmount = 0.0;
            txtDrfTransMemberNo.setText(CommonUtil.convertObjToStr(txtDrfTransMemberNo.getText()).toUpperCase());
            observable.resetDrfTransListTable();

            HashMap memberDetMap = new HashMap();
            memberDetMap.put("MEMBERSHIP_NO", CommonUtil.convertObjToStr(txtDrfTransMemberNo.getText()));
            memberDetMap.put("PROD_ID", CommonUtil.convertObjToStr(cboDrfTransProdID.getSelectedItem()));
//            this query is to see if the member no entered is correct
            if (rdoDrfTransaction_Payment.isSelected()) {
                List list1 = ClientUtil.executeQuery("getPaymentDetails", memberDetMap);
                List list = ClientUtil.executeQuery("getRecieptDetails", memberDetMap);


                memberDetMap.put("PAYMENT", "PAYMENT");
                List unauthList = ClientUtil.executeQuery("getSelectUnAuthList", memberDetMap);
                if (unauthList != null && unauthList.size() > 0) {
                    ClientUtil.displayAlert("Already Record exists for authorization can not add a new record");
                    txtDrfTransMemberNo.setText("");
                    return;
                }
                int z = 0;
                if (list != null && list.size() > 0) {

                    if (list1 != null && list1.size() > 0 && z == 0) {

                        int a = ClientUtil.confirmationAlert("Already One Payment is there, Do you want to continue?");
                        int b = 0;
                        z = 1;
                        if (a != b) {
                            txtDrfTransMemberNo.setText("");
                            return;
                        }
                    }
                } else {
                    ClientUtil.displayAlert("No receipt entry available for this member hence no payment can be made");
                    txtDrfTransMemberNo.setText("");
                    return;
                }
            }
            if (rdoDrfTransaction_Reciept.isSelected()) {

//                 List list=ClientUtil.executeQuery("getRecieptDetails", memberDetMap);

                memberDetMap.put("RECIEPT", "RECIEPT");
                List unauthList = ClientUtil.executeQuery("getSelectUnAuthList", memberDetMap);
                if (unauthList != null && unauthList.size() > 0) {
                    ClientUtil.displayAlert("Already Record exists for authorization can not add a new record");
                    txtDrfTransMemberNo.setText("");
                    return;
                }
//                 if(list!=null && list.size()>0){
//                     
//                     int a=ClientUtil.confirmationAlert("Already One Receipt exists, Do you want to continue?");
//                     int b=0;
//                     if(a!=b){
//                         txtDrfTransMemberNo.setText("");
//                         return;
//                     }
//                 }
            }
            List memberDetails = ClientUtil.executeQuery("getMemberDetailsForDrf", memberDetMap);
            if (memberDetails != null & memberDetails.size() > 0) {
            //    memberDetMap = new HashMap();
                memberDetMap = (HashMap) memberDetails.get(0);
                memberDetMap.put("PROD_ID", CommonUtil.convertObjToStr(cboDrfTransProdID.getSelectedItem()));
                System.out.println("#@$@#$@#memberDetMap" + memberDetMap.get("PROD_ID"));
                txtDrfTransName.setText(CommonUtil.convertObjToStr(memberDetMap.get("NAME")));
                txtDrfTransName.setEnabled(false);
                lblDrfTransAddressCont.setText(CommonUtil.convertObjToStr(memberDetMap.get("ADDRESS")));
                transactionUI.setCallingApplicantName(txtDrfTransName.getText());
                
                // Added by nithya on 26-04-2016 for 4307
                if (rdoDrfTransaction_Payment.isSelected() == true){ 
                  lblDeathDt.setVisible(true); 
                  tdtDeathDt.setVisible(true);
                  tdtDeathDt.setEnabled(false);
                  HashMap memberNoMap = new HashMap();
                  HashMap detailMap = new HashMap();
                  memberNoMap.put("MEMBER_NO", CommonUtil.convertObjToStr(txtDrfTransMemberNo.getText()));
                  List list = ClientUtil.executeQuery("getMarkingDetailsForDRF", memberNoMap);
                  //System.out.println("getMarkingDetailsForDRF List :: "+ list);
                  if(list != null && list.size() > 0){
                    detailMap = (HashMap)list.get(0);
                    if(detailMap.containsKey("DEATH_DT")){                
                     tdtDeathDt.setDateValue(CommonUtil.convertObjToStr(detailMap.get("DEATH_DT")));
                    }
                  }else{
                      ClientUtil.displayAlert("Customer death is not reported");
                  }                    
                }
                // End
                //                this query is used to check if the member no has already made a transaction before.
                List memberDrfTransDetails = ClientUtil.executeQuery("getMemberDrfTransDetails", memberDetMap);
                if (memberDrfTransDetails != null && memberDrfTransDetails.size() > 0) {
                    double amountPaid = 0.0;
                    observable.populateDrfTransTable(memberDrfTransDetails);
                    for (int i = 0; i < memberDrfTransDetails.size(); i++) {
                        HashMap memberTransDetailsMap = (HashMap) memberDrfTransDetails.get(i);
                        amountPaid = amountPaid + CommonUtil.convertObjToDouble(memberTransDetailsMap.get("AMOUNT")).doubleValue();
                        System.out.println("#@$@#$@#$amountPaid:" + amountPaid);
                        System.out.println("@#$@#$@#$memberTransDetailsMap:" + memberTransDetailsMap);

                        memberTransDetailsMap.put("PROD_ID", CommonUtil.convertObjToStr(cboDrfTransProdID.getSelectedItem()));
                    }
                    if (rdoDrfTransaction_Reciept.isSelected() == true) {
                        if (amount >= amountPaid) {
                            double newAmount = amount - amountPaid;
                            txtDrfTransAmount.setText(String.valueOf(newAmount));
                        }
                    } else if (rdoDrfTransaction_Payment.isSelected() == true) {
                        txtDrfTransAmount.setText(String.valueOf(paymentAmount));
                    }


                    chkDueAmtPayment.setSelected(true);
                    chkDueAmtPayment.setEnabled(true);
                } else {
                    HashMap memberTransDetailsMap = new HashMap();
                    memberTransDetailsMap.put("PROD_ID", CommonUtil.convertObjToStr(cboDrfTransProdID.getSelectedItem()));

                    //                    this query is added to get the amount details for the given PROD_ID
                    txtDrfTransAmount.setText(String.valueOf(amount));
                    chkDueAmtPayment.setSelected(false);
                    chkDueAmtPayment.setEnabled(false);

                }
                observable.setTxtDrfTransAmount(txtDrfTransAmount.getText());
                transactionUI.setCallingAmount(observable.getTxtDrfTransAmount());

            } else {
                ClientUtil.showAlertWindow("Entered Membership Number not found");
                txtDrfTransMemberNo.setText("");
                txtDrfTransAmount.setText("");
                txtDrfTransName.setText("");
                lblDrfTransAddressCont.setText("");
            }
        }

    }//GEN-LAST:event_txtDrfTransMemberNoFocusLost

    private void txtDrfTransAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDrfTransAmountFocusLost
        // TODO add your handling code here:
        transactionUI.setCallingApplicantName(txtDrfTransName.getText());
        // TODO add your handling code here:
        transactionUI.setCallingAmount(txtDrfTransAmount.getText());
        double amountBorrowed = CommonUtil.convertObjToDouble(txtDrfTransAmount.getText()).doubleValue();
    }//GEN-LAST:event_txtDrfTransAmountFocusLost

    /* Auto Generated Method - updateOBFields()
     This method called by Save option of UI.
     It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtDrfTransAmount(txtDrfTransAmount.getText());
        observable.setDrfProdPaymentAmt(String.valueOf(productPaymentAmount));
        observable.setDrfProductAmount(String.valueOf(productAmount));
        observable.setTxtDrfTransName(txtDrfTransName.getText());

        if (chkDueAmtPayment.isSelected()) {
            observable.setChkDueAmtPayment("Y");
        } else {
            observable.setChkDueAmtPayment("N");
        }
        observable.setTxtDrfTransMemberNo(txtDrfTransMemberNo.getText());
        if (rdoDrfTransaction_Payment.isSelected() == true) {
            observable.setRdoDrfTransaction("PAYMENT");
        } else if (rdoDrfTransaction_Reciept.isSelected() == true) {
            observable.setRdoDrfTransaction("RECIEPT");
        }
        observable.setLblDrfTransAddressCont(lblDrfTransAddressCont.getText());
        observable.setCboDrfTransProdID(CommonUtil.convertObjToStr(cboDrfTransProdID.getSelectedItem()));
        //Total Interst
        observable.setTOTAL(TOTAL);
        //Table data
        observable.setBufferList(bufferList);
        observable.setCboProdId(cboProductID.getSelectedItem().toString());
        observable.setTxtResolutionNo(txtResolutionNo.getText());
        // observable.setTdtResolutionDate(getDateVdtResolutionDate.getDateValue());
          observable.setTdtResolutionDate(tdtResolutionDate.getDateValue());
        //observable.setTdtResolutionDate(getDateValue(tdtResolutionDate.getDateValue()));
        observable.setScreen(getScreen());
    }

    public Date getDateValue(String date1) {
        DateFormat formatter;
        Date date = null;
        try {
            //   System.out.println("date1 66666666666=========:"+date1);  
            // String str_date=date1;
            //  formatter = new SimpleDateFormat("MM/dd/yyyy");
            //  date = (Date)formatter.parse(str_date);  
            //      System.out.println("dateAFETRRR 66666666666=========:"+date); 




            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
            // String s1 = "2008-03-30T15:30:00.000+02:00";
            // date1 = date1.substring(0, date1.indexOf('.'));
            //  System.out.println("Result==> "+sdf2.format(sdf1.parse(date1)));
            date = new Date(sdf2.format(sdf1.parse(date1)));
            System.out.println("date IOOOOOOO==> " + date);
        } catch (Exception e) {
            System.out.println("Error in getDateValue():" + e);
        }
        return date;
    }

    private void updateDrfTransUI() {
        System.out.println("anju..............");
        txtDrfTransAmount.setText(observable.getTxtDrfTransAmount());
        cboDrfTransProdID.setSelectedItem(observable.getCboDrfTransProdID());
        txtDrfTransName.setText(observable.getTxtDrfTransName());
        if (observable.getChkDueAmtPayment().equals("Y")) {
            chkDueAmtPayment.setSelected(true);
        } else {
            chkDueAmtPayment.setSelected(true);
        }
        txtDrfTransMemberNo.setText(observable.getTxtDrfTransMemberNo());
        if (observable.getRdoDrfTransaction().equals("PAYMENT")) {
            rdoDrfTransaction_Payment.setSelected(true);
        } else if (observable.getRdoDrfTransaction().equals("RECIEPT")) {
            rdoDrfTransaction_Reciept.setSelected(true);
        }
        lblDrfTransAddressCont.setText(observable.getLblDrfTransAddressCont());
        txtResolutionNo.setText(observable.getTxtResolutionNo());
    }
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed

        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        transactionUI.cancelAction(false);
        resetUI();
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        callView(EDIT);
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panTransaction, false);
        ClientUtil.enableDisable(panDrfTransDetails, false);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnEdit.setEnabled(false);
    }//GEN-LAST:event_btnViewActionPerformed
    private void resetUI() {
        observable.resetDrfTransDetails();
        observable.resetDrfTransListTable();
        lblDrfTransAddressCont.setText("");
    }

    private void callView(int viewType) {
        observable.setStatus();
        final HashMap viewMap = new HashMap();
        //--- If Action type is EDIT or DELETE show the popup Screen
         if(viewType==RESOLUTIONNO)
        {
      
              viewMap.put(CommonConstants.MAP_NAME, "getBoardResolutionAuth"); 
                new ViewAll(this, viewMap).show();
        }

        if (viewType == memberNo) {
            HashMap map = new HashMap();
            viewMap.put(CommonConstants.MAP_NAME, "getMemberDetailsForDrf");
            new ViewAll(this, viewMap).show();
        } else {
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                ArrayList lst = new ArrayList();
                lst.add("DRF TRANSACTION");
                viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                lst = null;
                HashMap whereMap = new HashMap();
                whereMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
                if (observable.getActionType() != ClientConstants.ACTIONTYPE_EDIT) {
                    whereMap.put(CommonConstants.AUTHORIZESTATUS, "AUTHORIZESTATUS");
                }
                viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                viewMap.put(CommonConstants.MAP_NAME, "getDrfTransferAuthMode");
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    viewMap.put(CommonConstants.MAP_NAME, "getDrfTransferEditMode");
                }
                new ViewAll(this, viewMap).show();
            }
        }
    }

    public void fillData(Object obj) {
        try {
            HashMap hashMap = (HashMap) obj;
            System.out.println("### fillData Hash : " + hashMap);
             if(hashMap.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")){
                fromNewAuthorizeUI= true;
                newauthorizeListUI = (NewAuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(false);
                rejectFlag=1;
            }
            if(hashMap.containsKey("FROM_AUTHORIZE_LIST_UI")){
                fromAuthorizeUI = true;
                authorizeListUI = (AuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                 btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(false);
                rejectFlag=1;
            }
            if (hashMap.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
                fromCashierAuthorizeUI = true;
                CashierauthorizeListUI = (AuthorizeListCreditUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                 btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(false);
                rejectFlag=1;
            }
            if (hashMap.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
                fromManagerAuthorizeUI = true;
                ManagerauthorizeListUI = (AuthorizeListDebitUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                 btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(false);
                rejectFlag=1;
            }
            if(viewType ==RESOLUTIONNO)
            {
                System.out.println("entered here"+CommonUtil.convertObjToStr(hashMap.get("RESOLUTION_ID")));
                String resolutionno="";
          		//  HashMap hash = (HashMap) map;
            	resolutionno=CommonUtil.convertObjToStr(hashMap.get("RESOLUTION_ID"));
           		txtResolutionNo.setText(resolutionno);
                tdtResolutionDate.setDateValue((CommonUtil.convertObjToStr(hashMap.get("RESOLUTION_DATE"))));
            
            }
            HashMap returnMap = null;
            if (viewType == memberNo) {
                txtDrfTransMemberNo.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBERSHIP_NO")));
                observable.setCboDrfTransProdID(CommonUtil.convertObjToStr(cboDrfTransProdID.getSelectedItem()));
                observable.setTxtDrfTransMemberNo(txtDrfTransMemberNo.getText());
                txtDrfTransMemberNoFocusLost(null);
            } else {
                if (CommonUtil.convertObjToStr(hashMap.get("RECIEPT_OR_PAYMENT")).equals("RECIEPT")) {
                    rdoDrfTransaction_Reciept.setSelected(true);
                } else if (CommonUtil.convertObjToStr(hashMap.get("RECIEPT_OR_PAYMENT")).equals("PAYMENT")) {
                    rdoDrfTransaction_Payment.setSelected(true);
                }
                if (CommonUtil.convertObjToStr(hashMap.get("DUE_AMOUNT")).equals("Y")) {
                    chkDueAmtPayment.setSelected(true);
                } else if (CommonUtil.convertObjToStr(hashMap.get("DUE_AMOUNT")).equals("N")) {
                    chkDueAmtPayment.setSelected(false);
                }
                HashMap memberDetMap = new HashMap();
                memberDetMap.put("MEMBERSHIP_NO", CommonUtil.convertObjToStr(hashMap.get("MEMBER_NO")));
//            this query is to see if the member no entered is correct
                List memberDetails = ClientUtil.executeQuery("getMemberDetailsForDrf", memberDetMap);
                if (memberDetails != null & memberDetails.size() > 0) {
                    memberDetMap = new HashMap();
                    memberDetMap = (HashMap) memberDetails.get(0);
                    System.out.println("#@$@#$@#memberDetMap" + memberDetMap);
                    txtDrfTransName.setText(CommonUtil.convertObjToStr(memberDetMap.get("NAME")));
                    txtDrfTransName.setEnabled(false);
                    lblDrfTransAddressCont.setText(CommonUtil.convertObjToStr(memberDetMap.get("ADDRESS")));
                }
                txtDrfTransAmount.setText(CommonUtil.convertObjToStr(hashMap.get("AMOUNT")));
                txtDrfTransMemberNo.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBER_NO")));
//                cboDrfTransProdID.setSelectedItem(CommonUtil.convertObjToStr(hashMap.get("FACE_VALUE")));
                cboDrfTransProdID.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmDrfTransProdID().getDataForKey(hashMap.get("FACE_VALUE"))));
                observable.setDrfTransID(CommonUtil.convertObjToStr(hashMap.get("DRF_TRANS_ID")));
                cboDrfTransProdID.setSelectedItem(CommonUtil.convertObjToStr(hashMap.get("DRF_PROD_ID")));

                if (observable.getActionType() == (ClientConstants.ACTIONTYPE_EDIT) || observable.getActionType() == (ClientConstants.ACTIONTYPE_DELETE) || observable.getActionType() == (ClientConstants.ACTIONTYPE_AUTHORIZE) || observable.getActionType() == (ClientConstants.ACTIONTYPE_REJECT)) {
                    isFilled = true;
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                            || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                            || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
//                actionEditDelete(hashMap);
                        panEditDelete = DRFTRANSACTION;
                        observable.populateData(hashMap);
                        observable.setDrfTransID(CommonUtil.convertObjToStr(hashMap.get("DRF_TRANS_ID")));
//                        observable.populateDrfTransData(String.valueOf(hashMap.get("DRF_TRANS_ID")), panEditDelete);
                        observable.populateDrfTransData(hashMap, panEditDelete);
                        observable.setDrfTransID(CommonUtil.convertObjToStr(hashMap.get("DRF_TRANS_ID")));
                        initTableData();
                        btnCancel.setEnabled(true);
                        txtResolutionNo.setText(observable.getTxtResolutionNo());
                        System.out.println("obdate..........." + observable.getTdtResolutionDate());
                        tdtResolutionDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtResolutionDate()));
                        observable.ttNotifyObservers();
                    }
                    if(observable.getActionType() == (ClientConstants.ACTIONTYPE_AUTHORIZE)){
                    btnAuthorize.setEnabled(true);
                    btnAuthorize.requestFocusInWindow();
                    }
                    
                    //--- disable the customerSelection in Edit Mode
                }
            }
            hashMap = null;

            returnMap = null;

        } catch (Exception e) {
            e.printStackTrace();
        }

        //__ To Save the data in the Internal Frame...
        setModified(true);
        if(rejectFlag==1){
           btnReject.setEnabled(false);
       }
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
	        btnAuthorize.setEnabled(true);
	        btnAuthorize.requestFocusInWindow();
       }
    }

    private void actionEditDelete(HashMap hash) {
        //fromActionEditHash = true;
        observable.resetForm();
        observable.setStatus();
        setButtonEnableDisable();
    }
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        DRF = "REJECT";
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        viewType = ClientConstants.ACTIONTYPE_AUTHORIZE;;
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnReject.setEnabled(true);
        btnException.setEnabled(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);

    }//GEN-LAST:event_btnRejectActionPerformed
    private void resetTransactionUI() {
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        setModified(true);
        DRF = "DELETE";
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        viewType = ClientConstants.ACTIONTYPE_DELETE;
        observable.setStatus();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        transactionUI.cancelAction(false);
        resetUI();
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        observable.setStatus();
        callView(DELETE);
//        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
        btnDelete.setEnabled(false);

    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        transactionUI.cancelAction(false);
        resetUI();
       ResolutionSearch.setEnabled(true);
        DRF = "EDIT";
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        callView(EDIT);
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        viewType = ClientConstants.ACTIONTYPE_EDIT;
        observable.setStatus();
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panTransaction, false);
        ClientUtil.enableDisable(panDrfTransDetails, false);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnEdit.setEnabled(false);


    }//GEN-LAST:event_btnEditActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        DRF = "AUTHORIZE";
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        viewType = ClientConstants.ACTIONTYPE_AUTHORIZE;
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);

    }//GEN-LAST:event_btnAuthorizeActionPerformed

    public void authorizeStatus(String authorizeStatus) {
        if (viewType == ClientConstants.ACTIONTYPE_AUTHORIZE && isFilled) {

            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTH_DT", curDate);
            singleAuthorizeMap.put("DRF_TRANS_ID", observable.getDrfTransID());
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap, observable.getDrfTransID());
            viewType = -1;
            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            authorizeMap = null;

        } else {
            //__ To Save the data in the Internal Frame...
            HashMap whereMap = new HashMap();    
            HashMap mapParam = new HashMap();    
            setModified(true);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                mapParam.put(CommonConstants.MAP_NAME, "getDrfTransferAuthMode");
            } else {
                mapParam.put(CommonConstants.MAP_NAME, "getDrfTransferAuthModeWithOutCashier");
            }            
            panEditDelete = DRFTRANSACTION;
            pan = DRFTRANSACTION;
            whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            whereMap.put("TRANS_DT", curDate);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            viewType = AUTHORIZE;
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            whereMap = null;

        }
    }

    public void authorize(HashMap map, String id) {
        System.out.println("Authorize Map : " + map);
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            observable.doAction();

            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(id);
            }
            if (DRF.equals("AUTHORIZE") || DRF.equals("REJECT")) {
                HashMap hashMap = new HashMap();
                hashMap.put("MEMBER_NO", txtDrfTransMemberNo.getText());
                List list = ClientUtil.executeQuery("getDeathDetailsForDRF", hashMap);
                if (list != null && list.size() > 0) {
                    HashMap hmap = (HashMap) list.get(0);
                    hmap.put("DRF", DRF);
                    hmap.put("MEMBER_NO", txtDrfTransMemberNo.getText());
                    TrueTransactMain.showScreen(new DeathMarkingUI(hmap));
                }
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            isFilled = false;
            if (fromNewAuthorizeUI) {
                newauthorizeListUI.removeSelectedRow();
                this.dispose();
                newauthorizeListUI.setFocusToTable();
                newauthorizeListUI.displayDetails("DRF Transaction");
            }
            if(fromAuthorizeUI ){
                authorizeListUI.removeSelectedRow();
                this.dispose();
                authorizeListUI.setFocusToTable();
                authorizeListUI.displayDetails("DRF Transaction");
            }
           super.setOpenForEditBy(observable.getStatusBy());
           observable.setResultStatus();
        }
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:


        resourceBundle = new DrfTransactionRB();
        System.out.println("getClassnam" + getClass().getName());
        final String shareAcctMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDrfTransDetails);
        //        final String othersMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panOtherDet);
        StringBuffer strBMandatory = new StringBuffer();

        int transactionSize = 0;
        if (/*
                 * rdoSharewithDrawal.isSelected()==true &&
                 */(transactionUI.getOutputTO() == null || (transactionUI.getOutputTO()).size() == 0)) {
            strBMandatory.append(resourceBundle.getString("NoRecords"));
            strBMandatory.append("\n");
        } else { //if(rdoShareAddition.isSelected()==false){
            transactionSize = (transactionUI.getOutputTO()).size();
            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
        }
        if (shareAcctMandatoryMessage.length() > 0) {
            strBMandatory.append(shareAcctMandatoryMessage);
        }
        if (!rdoDrfTransaction_Payment.isSelected() && !rdoDrfTransaction_Reciept.isSelected()) {
            strBMandatory.append(resourceBundle.getString("transtype"));
            strBMandatory.append("\n");
        }
        if (payment == true) {
            if (txtResolutionNo.getText().length() == 0) {
                strBMandatory.append(resourceBundle.getString("enterResNo"));
                strBMandatory.append("\n");
            }
            if (tdtResolutionDate.getDateValue().length() == 0) {
                strBMandatory.append(resourceBundle.getString("enterResDate"));
                strBMandatory.append("\n");
            }

        }

        String strMandatory = strBMandatory.toString();


        //--- checks whether the Mandatory fields are entered
        if (strMandatory.length() > 0) {        //--- if all the mandatory fields are not entered,
            CommonMethods.displayAlert(strMandatory);
            return;//--- display the alert
        } else if (strMandatory.length() == 0) { //--- if all the values are entered, save the data
            //Call transaction screen here
            //If transactions exist, proceed to save them
            if (transactionSize > 0/*
                     * && rdoSharewithDrawal.isSelected()==true
                     */) {
                if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                    observable.showAlertWindow(resourceBundle.getString("saveInTxDetailsTable"));
                } else if (transactionUI.isBtnSaveTransactionDetailsFlag()) {
                    //                        int noOfShares = getNoOfShares();
                    double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
                    if (ClientUtil.checkTotalAmountTallied(CommonUtil.convertObjToDouble(txtDrfTransAmount.getText()).doubleValue(), transTotalAmt) == false) {
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NOT_TALLY));
                    } else {
                        String memberNo = CommonUtil.convertObjToStr(txtDrfTransMemberNo.getText());
                        String interBranchCode = null;
                        HashMap shareAcctNumberMap = new HashMap();
                        shareAcctNumberMap.put("SHARE_ACCT_NO", memberNo);
                        List interBranchCodeList = ClientUtil.executeQuery("getShareBranchCode", shareAcctNumberMap);
                        if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                            shareAcctNumberMap = (HashMap) interBranchCodeList.get(0);
                            System.out.println("interBranchCodeMap : " + shareAcctNumberMap);
                            interBranchCode = CommonUtil.convertObjToStr(shareAcctNumberMap.get("BRANCH_CODE"));
                        }
                        boolean interbranchFlag = false;
                        System.out.println("interBranchCode"+interBranchCode+"ProxyParameters.BRANCH_ID"+ProxyParameters.BRANCH_ID+
                                "transactionUI.getTransactionOB().getSelectedTxnType()"+transactionUI.getCallingTransType());
                        if (transactionUI.getCallingTransType() != null && !transactionUI.getCallingTransType().equals("") 
                                && interBranchCode != null && !interBranchCode.equals("")) {
                            if (transactionUI.getCallingTransType().equals(CommonConstants.TX_TRANSFER)) {
                                if (ProxyParameters.BRANCH_ID.equals(interBranchCode)) {
                                    interbranchFlag = false;
                                } else if (ProxyParameters.BRANCH_ID.equals(transactionUI.getTransactionOB().getSelectedTxnBranchId())) {
                                    interbranchFlag = false;
                                } else {
                                    interbranchFlag = true;
                                }
                            } else {
                                interbranchFlag = false;
                            }
                            if (interbranchFlag) {
                                ClientUtil.displayAlert("Incase of interbranch transaction either " + "\n" + "Dr or Cr account of the transaction should be of own branch");
                            } else {
                                savePerformed();
                                observable.setStatus();
                                observable.setResultStatus();
                                lblStatus.setText(observable.getLblStatus());
                            }
                        }
                    }
                } else {
                    savePerformed();
                    observable.setStatus();
                    observable.setResultStatus();
                    lblStatus.setText(observable.getLblStatus());
                }

                //End of Transaction call


                resourceBundle = null;
            } else {
                CommonMethods.displayAlert(resourceBundle.getString("saveAcctDet"));
            }
            payment = false;
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed() {
        try {
            updateOBFields();
            observable.setResult(observable.getActionType());
            observable.doAction();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                if (observable.getProxyReturnMap() != null) {
                    if (observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST") || observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                        displayTransDetail(observable.getProxyReturnMap());
                    }
                }
            }

            if (chkDeathMarking.isSelected()) {
                HashMap hmap = new HashMap();
                hmap.put("MEMBER_NO", txtDrfTransMemberNo.getText());
                hmap.put("DRF", DRF);
                ArrayList list = (ArrayList) ClientUtil.executeQuery("getDeathMarkingDetailsForDRF", hmap);
                if (list != null && list.size() > 0) {
                    TrueTransactMain.showScreen(new DeathMarkingUI(hmap));
                } else {
                    ClientUtil.displayAlert("There are no accounts for this member cannot do death marking");
                }
            }
            if (DRF.equals("DELETE")) {
                HashMap hmap = new HashMap();
                hmap.put("DRF", DRF);
                hmap.put("MEMBER_NO", txtDrfTransMemberNo.getText());
                List list1 = ClientUtil.executeQuery("getDeathDetailsForDRF", hmap);
                if (list1 != null && list1.size() > 0) {
                    hmap = (HashMap) list1.get(0);
                    hmap.put("DRF", DRF);
                    hmap.put("MEMBER_NO", txtDrfTransMemberNo.getText());
                    TrueTransactMain.showScreen(new DeathMarkingUI(hmap));
                }
            }
            observable.makeToNull();
            btnCancelActionPerformed(null);
            //            btnCancelActionPerformed(null);
            observable.ttNotifyObservers();
            observable.setResultStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap : " + proxyResultMap);
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        for (int i = 0; i < keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                    }
                    transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                }
                transferCount++;
            }
        }
        if (cashCount > 0) {
            displayStr += cashDisplayStr;
        }
        if (transferCount > 0) {
            displayStr += transferDisplayStr;
        }
        ClientUtil.showMessageWindow("" + displayStr);

        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        System.out.println("#$#$$ yesNo : " + yesNo);
        if (yesNo == 0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("TransId", transId);
            paramMap.put("TransDt", curDate);
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            ttIntgration.setParam(paramMap);
            //            if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
            //                    ttIntgration.integrationForPrint("ReceiptPayment", false);
            //            } else {
            String reportName = "";
            if (transferCount > 0) {
                reportName = "ReceiptPayment";
            } else if (observable.getRdoDrfTransaction().equals("PAYMENT")) {
                reportName = "CashPayment";
            } else {
                reportName = "CashReceipt";
            }
            ttIntgration.integrationForPrint(reportName, false);
            if(observable.getActionType() == (ClientConstants.ACTIONTYPE_AUTHORIZE)){
             btnAuthorize.setEnabled(true);
             btnAuthorize.requestFocusInWindow();
           }
        }
    }

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        setModified(true);
        resetUI();               // to Reset all the Fields and Status in UI...
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panDrfTransactionDetails, true);
        ClientUtil.enableDisable(jPanel2, false);
        lblProductID.setEnabled(true);
        cboProductID.setEnabled(true);
        btnProcess.setEnabled(true);
        ResolutionSearch.setEnabled(true);
        setButtonEnableDisable();
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnPrint.setEnabled(false);
        observable.resetForm();
        observable.setStatus();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        viewType = ClientConstants.ACTIONTYPE_NEW;
        observable.setStatus();
        btnView.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);   //Sets the Action Type to be performed...
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        DRF = "NEW";
    }//GEN-LAST:event_btnNewActionPerformed

    private void deletescreenLock() {
        HashMap map = new HashMap();
        map.put("USER_ID", ProxyParameters.USER_ID);
        map.put("TRANS_DT", curDate);
        map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setModified(false);
        deletescreenLock();
        observable.resetForm();
        observable.setAuthorizeStatus("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panDrfTransactionDetails, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        resetUI();
        observable.resetForm();
        lblStatus.setText("             ");
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnNew.setEnabled(true);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(true);
        btnSave.setEnabled(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        isFilled = false;
        chkDueAmtPayment.setSelected(false);
        if (fromNewAuthorizeUI) {
            this.dispose();
           newauthorizeListUI.setFocusToTable();
        }
        if (fromAuthorizeUI) {
            this.dispose();
            authorizeListUI.setFocusToTable();
        }
        DRF = "";
        bufferList.clear();
        tableData();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
    }
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
    }//GEN-LAST:event_mitNewActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
    }//GEN-LAST:event_mitSaveActionPerformed

    private void txtResolutionNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtResolutionNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtResolutionNoActionPerformed

private void ResolutionSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResolutionSearchActionPerformed

    viewType=RESOLUTIONNO;
    callView(RESOLUTIONNO);


    // TODO add your handling code here:
}//GEN-LAST:event_ResolutionSearchActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton ResolutionSearch;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDateChange;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private javax.swing.JButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboDrfTransProdID;
    private javax.swing.JComboBox cboProductID;
    private com.see.truetransact.uicomponent.CCheckBox chkDeathMarking;
    private com.see.truetransact.uicomponent.CCheckBox chkDueAmtPayment;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private com.see.truetransact.uicomponent.CLabel lblCalculationCriteria;
    private com.see.truetransact.uicomponent.CLabel lblDeathDt;
    private com.see.truetransact.uicomponent.CLabel lblDeathMarking;
    private com.see.truetransact.uicomponent.CLabel lblDrfTransAddress;
    private com.see.truetransact.uicomponent.CLabel lblDrfTransAddressCont;
    private com.see.truetransact.uicomponent.CLabel lblDrfTransAmount;
    private com.see.truetransact.uicomponent.CLabel lblDrfTransMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblDrfTransName;
    private com.see.truetransact.uicomponent.CLabel lblDrfTransProdID;
    private com.see.truetransact.uicomponent.CLabel lblDueAmtPayment;
    private com.see.truetransact.uicomponent.CLabel lblInterestCalculationFrequency;
    private com.see.truetransact.uicomponent.CLabel lblLastInterestCalculatedDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProductFrequency;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblResolutionDate;
    private com.see.truetransact.uicomponent.CLabel lblResolutionNo;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace52;
    private com.see.truetransact.uicomponent.CLabel lblSpace53;
    private com.see.truetransact.uicomponent.CLabel lblSpace54;
    private com.see.truetransact.uicomponent.CLabel lblSpace55;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblSpace57;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAutoRenewal;
    private com.see.truetransact.uicomponent.CPanel panCustomerId;
    private com.see.truetransact.uicomponent.CPanel panDrfTransDetails;
    private com.see.truetransact.uicomponent.CPanel panDrfTransList;
    private com.see.truetransact.uicomponent.CPanel panDrfTransaction;
    private com.see.truetransact.uicomponent.CPanel panDrfTransactionDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDrfTransaction;
    private com.see.truetransact.uicomponent.CRadioButton rdoDrfTransaction_Payment;
    private com.see.truetransact.uicomponent.CRadioButton rdoDrfTransaction_Reciept;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpDrfTransaction;
    private com.see.truetransact.uicomponent.CTabbedPane tabDrfTransaction;
    private com.see.truetransact.uicomponent.CTable tblDrfTransaction;
    private javax.swing.JTable tblInterest;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtDeathDt;
    private com.see.truetransact.uicomponent.CDateField tdtResolutionDate;
    private javax.swing.JTextField txtCalculationCriteria;
    private com.see.truetransact.uicomponent.CTextField txtDrfTransAmount;
    private com.see.truetransact.uicomponent.CTextField txtDrfTransMemberNo;
    private com.see.truetransact.uicomponent.CTextField txtDrfTransName;
    private javax.swing.JTextField txtInterestCalculationFrequency;
    private javax.swing.JTextField txtLastInterestCalculatedDate;
    private javax.swing.JTextField txtProductFrequency;
    private com.see.truetransact.uicomponent.CTextField txtResolutionNo;
    // End of variables declaration//GEN-END:variables
}