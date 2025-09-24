/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RecoveryListTallyUI.java
 * @author  Suresh
 *
 */
package com.see.truetransact.ui.salaryrecovery;

import java.util.*;
import java.awt.Color;
import javax.swing.JTable;
import java.awt.Component;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import com.see.truetransact.commonutil.DateUtil;
import javax.swing.table.DefaultTableCellRenderer;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceRateTO;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.uicomponent.COptionPane;
import java.text.DecimalFormat;
import java.awt.event.*;
import javax.swing.JDialog;
import javax.swing.SwingWorker;
//import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.hssf.usermodel.*;

public class RecoveryListTallyUI extends CInternalFrame implements Observer {

    /**
     * Vairable Declarations
     */
    boolean clear = false;
    boolean newMode = true;
    private int selectedRow = -1;
    boolean acceptChanges = true;
    private String viewType = "";
    private Date currDate = null;
    ArrayList disableRowList = null;
    private HashMap returnMap = null;
    HashMap finalMap = new HashMap();
    RecoveryListTallyOB observable = null;
    ArrayList colourList = new ArrayList();
    ArrayList clrList = new ArrayList();
    HashMap recverdList = new HashMap();
    double recoveryAmount;
    double unRecovery;
    private int tabNo;
    ArrayList disableRowList1 = null;
    HashMap prdTypeMap = new HashMap();
    HashMap prod_Map = new HashMap();
    ArrayList Unreco = new ArrayList();
    private TableModelListener tableModelListener;
    TransactionUI transactionUI = new TransactionUI();
    private double sus_amt = 0.0;
    DecimalFormat df = new DecimalFormat("##.##");
    private String unrecCheck = "N";
    HashMap behLikeMap = new HashMap();
    HashMap instNoMap = new HashMap();

    public RecoveryListTallyUI() {
        returnMap = null;
        currDate = ClientUtil.getCurrentDate();
        initForm();
    }

    /**
     * Method which is used to initialize the form TokenConfig
     */
    private void initForm() {
        initComponents();
        observable = new RecoveryListTallyOB();
        initTableData();
        initComponentData();
        initTallyTableData();
        enableDisBtn(false);
        btnEdit.setEnabled(true);
        btnBrowse.setEnabled(true);
        transactionUI.resetObjects();
        transactionUI.cancelAction(false);
        btnAcceptChanges.setEnabled(false);
        panAccountDetails.setVisible(false);
        panMemberReceiptTrans.add(transactionUI);
        panRecoveryEditDetails.setVisible(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.setSourceScreen("RECOVERY_LIST_TALLY");
        ClientUtil.enableDisable(panSalaryRecoveryList, false);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        txtTotalAmount.setValidation(new CurrencyValidation(14, 2));
        txtTotalDemandTally.setValidation(new CurrencyValidation(14, 2));
        txtTotalRecoveredAmount.setValidation(new CurrencyValidation(14, 2));
        txtTotalRecoveredTallyAmt.setValidation(new CurrencyValidation(14, 2));
    }

    private void initComponentData() {
        cboProdType.setModel(observable.getCbmProdType());
        cboProdId.setModel(observable.getCbmProdId());
    }

    private void initTableData() {
        tblSalaryRecoveryList.setModel(observable.getTblSalaryRecoveryList());
        setSizeTableData();
    }

    private void initTableData1() {
        tblproduct.setModel(observable.getTblSalaryRecoveryList());

    }

    private void setSizeTableData() {
        tblSalaryRecoveryList.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblSalaryRecoveryList.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblSalaryRecoveryList.getColumnModel().getColumn(2).setPreferredWidth(280);
        tblSalaryRecoveryList.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblSalaryRecoveryList.getColumnModel().getColumn(4).setPreferredWidth(100);
    }

    private void setSizeTallyTableData() {
        if (tblRecoveryListTally.getRowCount() > 0) {
            tblRecoveryListTally.getColumnModel().getColumn(0).setPreferredWidth(140);
            tblRecoveryListTally.getColumnModel().getColumn(1).setPreferredWidth(70);
            tblRecoveryListTally.getColumnModel().getColumn(2).setPreferredWidth(30);
            tblRecoveryListTally.getColumnModel().getColumn(3).setPreferredWidth(30);
            tblRecoveryListTally.getColumnModel().getColumn(4).setPreferredWidth(40);
            tblRecoveryListTally.getColumnModel().getColumn(5).setPreferredWidth(30);
            tblRecoveryListTally.getColumnModel().getColumn(6).setPreferredWidth(70);
            tblRecoveryListTally.getColumnModel().getColumn(7).setPreferredWidth(90);
            tblRecoveryListTally.getColumnModel().getColumn(8).setPreferredWidth(90);
        }
    }

    public void update(Observable observed, Object arg) {
    }

    public void updateOBFields() {
    }

    public void setMandatoryHashMap() {
    }

    public void initTallyTableData() {
        tblRecoveryListTally.setModel(new javax.swing.table.DefaultTableModel(
                setTableData(),
                new String[]{
                    "Scheme Name", "A/c Number", "Principal", "Interest", "Penal Interest", "Charges", "Demand", "Recovered Amount", "Unrecovery", "Rec Penal Int", "Inst.Amount"
                }) {

            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, true, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {

                if (columnIndex == 7) {
                    return true;
                }
                return canEdit[columnIndex];
            }
        });
        tblRecoveryListTally.setCellSelectionEnabled(true);
        tblRecoveryListTally.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblRecoveryListTallyPropertyChange(evt);
            }
        });
        setTableModelListener();
        setSizeTallyTableData();
    }

    public void initTallyUnrecoveryTableData() {
        tblRecoveryListTally.setModel(new javax.swing.table.DefaultTableModel(
                setTableData(),
                new String[]{
                    "Scheme Name", "A/c Number", "Principal", "Interest", "Penal Interest", "Charges", "No Of Installment", "Demand", "Recovered Amount", "Unrecovery"
                }) {

            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {

                if (columnIndex == 7) {
                    return true;
                }
                return canEdit[columnIndex];
            }
        });
        tblRecoveryListTally.setCellSelectionEnabled(true);
        tblRecoveryListTally.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblRecoveryListTallyPropertyChange(evt);
            }
        });
        setTableModelListener();
        setSizeTallyTableData();
    }

    private Object[][] setTableData() {
        if (tblSalaryRecoveryList.getRowCount() > 0 && clear == false) {
            HashMap whereMap = new HashMap();
            ArrayList recoveryList = new ArrayList();
            disableRowList = new ArrayList();
            String emp_Ref_No = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(tblSalaryRecoveryList.getSelectedRow(), 1));
            finalMap = (HashMap) observable.getProxyReturnMap();

            recoveryList = (ArrayList) finalMap.get(emp_Ref_No);
            System.out.println("recoveryListrecoveryList" + recoveryList);
            Object totalList[][] = new Object[recoveryList.size()][10];
            whereMap = new HashMap();
            double total_Demand = 0.0;
            double total_RecoveredAmt = 0.0;
            for (int i = 0; i < recoveryList.size(); i++) {
                whereMap = (HashMap) recoveryList.get(i);
                String prodType = CommonUtil.convertObjToStr(whereMap.get("PROD_TYPE"));
                System.out.println("####### whereMap : " + i + "" + whereMap);
                double recAmt = 0.0;
                if (whereMap.containsKey("REC_PRINCIPAL")) {
                    recAmt = (CommonUtil.convertObjToDouble(whereMap.get("REC_PRINCIPAL")) + CommonUtil.convertObjToDouble(whereMap.get("REC_INTEREST")) + CommonUtil.convertObjToDouble(whereMap.get("REC_PENAL")) + CommonUtil.convertObjToDouble(whereMap.get("REC_CHARGES")));
                } else {
                    recAmt = CommonUtil.convertObjToDouble(whereMap.get("RECOVERED_AMOUNT"));
                }
                double totalDemand = CommonUtil.convertObjToDouble(whereMap.get("PRINCIPAL"))+CommonUtil.convertObjToDouble(whereMap.get("CHARGES"))
                        +CommonUtil.convertObjToDouble(whereMap.get("PENAL"))+CommonUtil.convertObjToDouble(whereMap.get("INTEREST"));
                System.out.println("totalDemand" + totalDemand + "recAmt" + recAmt);
                totalList[i][0] = CommonUtil.convertObjToStr(whereMap.get("SCHEME_NAME"));
                totalList[i][1] = CommonUtil.convertObjToStr(whereMap.get("ACT_NUM"));
                totalList[i][2] = CommonUtil.convertObjToStr(whereMap.get("PRINCIPAL"));
                totalList[i][3] = CommonUtil.convertObjToStr(whereMap.get("INTEREST"));
                totalList[i][4] = CommonUtil.convertObjToStr(whereMap.get("PENAL"));
                totalList[i][5] = CommonUtil.convertObjToStr(whereMap.get("CHARGES"));
                totalList[i][6] = CommonUtil.convertObjToStr(totalDemand);
                if (whereMap.containsKey("REC_PENAL")) {
                    if (prodType != null && !prodType.equals("") && (prodType.equals("MDS") || prodType.equals("SA") || prodType.equals("TD"))) {
                        double recPenal = CommonUtil.convertObjToDouble(whereMap.get("REC_PENAL"));
                        if(recPenal > 0){
                            totalList[i][9] = CommonUtil.convertObjToStr(recPenal);
                        }
                    }
                } else {
                    totalList[i][9] = "0";
                }
                total_Demand += totalDemand;
                if (recAmt > 0) {
                    total_RecoveredAmt += recAmt;
                    totalList[i][7] = CommonUtil.convertObjToStr(recAmt);
                    if((totalDemand-recAmt) <= 0){
                        totalList[i][8] = CommonUtil.convertObjToStr("0");
                    }else{
                        totalList[i][8] = CommonUtil.convertObjToStr((double) Math.round((totalDemand - recAmt) * 100) / 100);
                    }
                } else {
                    if (whereMap.containsKey("TALLY_VERIFIED")) {
                        String recoveryVerified = CommonUtil.convertObjToStr(whereMap.get("TALLY_VERIFIED"));
                        if (recoveryVerified != null && !recoveryVerified.equals("") && recoveryVerified.equals("Y")) {
                            totalList[i][7] = CommonUtil.convertObjToStr("0");
                            totalList[i][8] = CommonUtil.convertObjToStr(whereMap.get("TOTAL_DEMAND"));
                        } else {
                            total_RecoveredAmt += CommonUtil.convertObjToDouble(whereMap.get("TOTAL_DEMAND"));
                            totalList[i][7] = CommonUtil.convertObjToStr(whereMap.get("TOTAL_DEMAND"));
                            totalList[i][8] = CommonUtil.convertObjToStr("0");
                        }
                    } else {
                        totalList[i][7] = CommonUtil.convertObjToStr("0");
                        totalList[i][8] = CommonUtil.convertObjToStr(whereMap.get("TOTAL_DEMAND"));
                    }
                }
                if (CommonUtil.convertObjToStr(whereMap.get("PROD_TYPE")).equals("TD") || CommonUtil.convertObjToStr(whereMap.get("PROD_TYPE")).equals("MDS")) {
                    disableRowList.add(String.valueOf(i));
                }
            }
            txtTotalDemandTally.setText(String.valueOf(total_Demand));
            txtTotalRecoveredTallyAmt.setText(String.valueOf(total_RecoveredAmt));
            return totalList;
        }
        return null;
    }

    private void setTableModelListener() {
        tableModelListener = new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    if (column == 7) {
                        if (CommonUtil.convertObjToDouble(tblRecoveryListTally.getModel().getValueAt(e.getFirstRow(), e.getColumn())).doubleValue() > 0) {
                            double demand_Amount = CommonUtil.convertObjToDouble(tblRecoveryListTally.getValueAt(tblRecoveryListTally.getSelectedRow(), 6).toString()).doubleValue();
                            double recovered_Amount = CommonUtil.convertObjToDouble(tblRecoveryListTally.getValueAt(tblRecoveryListTally.getSelectedRow(), 7).toString()).doubleValue();
                            if (demand_Amount > recovered_Amount && recovered_Amount > 0) {
                                ClientUtil.showMessageWindow("Recovered Amount less than Demand Amount !!!");
                            }
                        }
                        TableModel model = tblRecoveryListTally.getModel();
                        calcTallyListTotal();
                    }
                    if (column == 8) {

                        double demand_Amount = CommonUtil.convertObjToDouble(tblRecoveryListTally.getValueAt(tblRecoveryListTally.getSelectedRow(), 6).toString()).doubleValue();
                        double unRecovery = CommonUtil.convertObjToDouble(tblRecoveryListTally.getValueAt(tblRecoveryListTally.getSelectedRow(), 8).toString()).doubleValue();
                        String prodtype = CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(tblRecoveryListTally.getSelectedRow(), column));
                        if (!(disableRowList.contains(String.valueOf(tblRecoveryListTally.getSelectedRow())))) {
                            tblRecoveryListTally.setValueAt(df.format(demand_Amount - unRecovery), tblRecoveryListTally.getSelectedRow(), 7);
                        } else {
                            calcMDSRD();
                        }

                        // }
                        TableModel model = tblRecoveryListTally.getModel();
                        calcTallyListTotal();
                    }
                }
            }
        };
        tblRecoveryListTally.getModel().addTableModelListener(tableModelListener);
    }

    private void setTableModelListener1() {
        System.out.println("1111111111111111llllllllldfjgh");
        try {
            tableModelListener = new TableModelListener() {

                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == 0) {
                        System.out.println((new StringBuilder()).append("Cell ").append(e.getFirstRow()).append(", ").append(e.getColumn()).append(" changed. The new value: ").append(tblproduct.getModel().getValueAt(e.getFirstRow(), e.getColumn())).toString());
                        int row = e.getFirstRow();
                        System.out.println((new StringBuilder()).append("e.getFirstRow()").append(e.getFirstRow()).toString());
                        int column = e.getColumn();




                        if (column == 9) {
                            System.out.println("inside coumn 9 change");
                            TableModel model = tblproduct.getModel();
                            System.out.println("disableRowList1111................. :" + disableRowList1);
                            System.out.println("valueeeeeeee :" + tblproduct.getValueAt(tblproduct.getSelectedRow(), 9));
                            recoveryAmount = CommonUtil.convertObjToDouble(tblproduct.getValueAt(tblproduct.getSelectedRow(), 9));
                            System.out.println("recoveryAmount" + recoveryAmount);
                            if (recoveryAmount > 0 && tblproduct.getValueAt(tblproduct.getSelectedRow(), 9) != null) {
                                System.out.println("hre coming");
                                tblproduct.setValueAt(tblproduct.getValueAt(tblproduct.getSelectedRow(), 7), tblproduct.getSelectedRow(), 8);
                                unRecovery = CommonUtil.convertObjToDouble(tblproduct.getValueAt(tblproduct.getSelectedRow(), 9));
                                System.out.println("sack_nosack_nosack_nosack_no" + unRecovery);
                                double amount = CommonUtil.convertObjToDouble(tblproduct.getValueAt(tblproduct.getSelectedRow(), 8));
                                System.out.println("amountamountamountamountamount" + amount);
                                double total = (double)Math.round((amount - unRecovery)*100)/100;
                                System.out.println("totaltotaltotaltotaltotaltotal" + total);
                                //double amount = CommonUtil.convertObjToDouble(tblproduct.getValueAt(tblproduct.getSelectedRow(), 8))-recoveryAmount;
                                if (!(disableRowList1.contains(String.valueOf(tblproduct.getSelectedRow())))) {
                                    if (total < 0) {
                                        total = 0;
                                    }
                                    tblproduct.setValueAt(Double.valueOf(total), tblproduct.getSelectedRow(), 8);
                                } else {
                                    calcMDSRD1();
                                }


                                if (recoveryAmount > 0) {

                                    unRecovery = CommonUtil.convertObjToDouble(tblproduct.getValueAt(tblproduct.getSelectedRow(), 9));
                                    System.out.println("2222222222222222" + unRecovery);
                                    double principal = CommonUtil.convertObjToDouble(tblproduct.getValueAt(tblproduct.getSelectedRow(), 2));
                                    double penal = CommonUtil.convertObjToDouble(tblproduct.getValueAt(tblproduct.getSelectedRow(), 3));
                                    double penalInt = CommonUtil.convertObjToDouble(tblproduct.getValueAt(tblproduct.getSelectedRow(), 4));
                                    double charges = CommonUtil.convertObjToDouble(tblproduct.getValueAt(tblproduct.getSelectedRow(), 5));
                                    String accountno = CommonUtil.convertObjToStr(tblproduct.getValueAt(tblproduct.getSelectedRow(), 1));
                                    System.out.println("principal" + principal + "penal" + penal + "penalInt" + penalInt + "charges" + charges + "accountno" + accountno);
//                                    double totUnrecAmt = CommonUtil.convertObjToDouble(tblproduct.getValueAt(tblproduct.getSelectedRow(), 7));
//                                    System.out.println("totUnrecAmt" + totUnrecAmt);
                                    double unRecPrinc = 0;
                                    if (principal >= unRecovery) {
                                        System.out.println("1111111");
                                        unRecPrinc = principal - unRecovery;
                                        unRecPrinc = (double) Math.round(unRecPrinc * 100) / 100;
                                        System.out.println("unRecPrinc" + unRecPrinc);
                                        unRecovery = unRecovery - principal;
                                    } else {
                                        System.out.println("2222222");
                                        unRecovery = unRecovery - principal;
                                        unRecPrinc = 0;
                                    }
                                    if (unRecovery < 0) {
                                        unRecovery = 0;
                                    }
                                    System.out.println("unRecoveryunRecoveryunRecovery" + unRecovery);
                                    if (unRecovery >= penal) {
                                        System.out.println("33333333");
                                        unRecovery = unRecovery - penal;
                                        penal = 0;
                                    } else {
                                        System.out.println("444444444");
                                        penal = penal - unRecovery;
                                        penal = (double) Math.round(penal * 100) / 100;
                                        unRecovery = 0;
                                    }
                                    if (unRecovery < 0) {
                                        unRecovery = 0;
                                    }
                                    System.out.println("unRecoveryunRecoveryunRecovery" + unRecovery + "jdjdjdjd" + penal);
                                    if (unRecovery >= penalInt) {
                                        System.out.println("5555555555");
                                        unRecovery = unRecovery - penalInt;
                                        penalInt = 0;
                                    } else {
                                        System.out.println("666666666");
                                        penalInt = penalInt - unRecovery;
                                        penalInt = (double) Math.round(penalInt * 100) / 100;
                                        unRecovery = 0;
                                    }
                                    if (unRecovery < 0) {
                                        unRecovery = 0;
                                    }
                                    System.out.println("unRecoveryunRecoveryunRecovery" + unRecovery + "jdjdjdjd" + penalInt);
                                    if (unRecovery >= charges) {
                                        System.out.println("7777777777");
                                        unRecovery = unRecovery - charges;
                                        charges = 0;
                                    } else {
                                        System.out.println("8888888888");
                                        charges = charges - unRecovery;
                                        charges = (double) Math.round(charges * 100) / 100;
                                        unRecovery = 0;
                                        System.out.println("unRecoveryunRecoveryunRecovery" + unRecovery + "jdjdjdjd" + charges);

                                    }
                                    if (unRecovery < 0) {
                                        unRecovery = 0;
                                    }
                                    String prdtype = "0";
                                    System.out.println("prdTypeMap" + prdTypeMap);
                                    if (prdTypeMap != null && prdTypeMap.containsKey(tblproduct.getSelectedRow())) {
                                        prdtype = CommonUtil.convertObjToStr(prdTypeMap.get(tblproduct.getSelectedRow()));
                                        if (prdtype.equals("TD")) {
                                            prdtype = "1";
                                        } else {
                                            prdtype = "0";
                                        }
                                    }
                                    System.out.println("unreco before" + Unreco);
                                    ArrayList tempList = new ArrayList();
                                    for (int k = 0; k < Unreco.size(); k++) {
                                        ArrayList temp = (ArrayList) Unreco.get(k);
                                        if (temp != null && temp.size() > 0) {
                                            String tempacc = CommonUtil.convertObjToStr(temp.get(5));
                                            if (!accountno.equals(tempacc)) {
                                                tempList.add(temp);

                                            }
                                        }
                                    }
                                    Unreco = tempList;
                                    if (!disableRowList1.contains(String.valueOf(tblproduct.getSelectedRow())) && prdtype.equals("0")) {
                                        ArrayList lst = new ArrayList();
                                        lst.add(unRecPrinc);
                                        lst.add(principal);
                                        lst.add(penal);
                                        lst.add(penalInt);
                                        lst.add(charges);
                                        lst.add(accountno);
                                        System.out.println("lstlstlstlstlst 222" + lst);
                                        Unreco.add(lst);
                                    } else {
                                        ArrayList lst = new ArrayList();
                                        unRecovery = CommonUtil.convertObjToDouble(tblproduct.getValueAt(tblproduct.getSelectedRow(), 8));
                                        principal = CommonUtil.convertObjToDouble(tblproduct.getValueAt(tblproduct.getSelectedRow(), 2)) - unRecovery;
                                        penalInt = CommonUtil.convertObjToDouble(tblproduct.getValueAt(tblproduct.getSelectedRow(), 10));
                                        System.out.println("unRecovery" + unRecovery + "principal" + principal + "penalInt" + penalInt);
                                        unRecovery = unRecovery - penalInt;
                                        System.out.println("unRecovery" + unRecovery);
                                        lst.add(unRecovery);
                                        lst.add(principal);
                                        lst.add(penal);
                                        lst.add(penalInt);
                                        lst.add(charges);
                                        lst.add(accountno);
                                        System.out.println("lstlstlstlstlst 1111" + lst);
                                        Unreco.add(lst);
                                    }
                                    System.out.println("UnrecUnrecUnrecUnrec" + Unreco);


// }
                                }

                            } else {
                                System.out.println("here masmd");
                                tblproduct.setValueAt(tblproduct.getValueAt(tblproduct.getSelectedRow(), 7), tblproduct.getSelectedRow(), 8);
                            }
                            calcUnRecTallyListTotal();

                        }
                        if (column == 8) {
                            System.out.println("iinside column 8 change");
                            double amt = CommonUtil.convertObjToDouble(tblproduct.getValueAt(tblproduct.getSelectedRow(), 8));
                            System.out.println("here value amt" + amt);
                            if (amt < 0) {
                                System.out.println("lscknas,nca,snc,asn,cnasnx,san");
                                tblproduct.setValueAt(tblproduct.getValueAt(tblproduct.getSelectedRow(), 7), tblproduct.getSelectedRow(), 8);
                            }
                        }
                        if (column == 3) {
                            System.out.println("inside column 3 change");
                            tblproduct.addMouseListener(new MouseAdapter() {

                                public void mouseClicked(MouseEvent mouseevent) {
                                }
                            });
                        }

                    }
                }
            };
            tblproduct.getModel().addTableModelListener(tableModelListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calcMDSRD() {
        System.out.println("inside calcMDSRD");
        try {
            String emp_Ref_No = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(tblSalaryRecoveryList.getSelectedRow(), 1));
            finalMap = (HashMap) observable.getProxyReturnMap();
            ArrayList recoveryList = new ArrayList();
            recoveryList = (ArrayList) finalMap.get(emp_Ref_No);
            String prodtype = "";
            for (int i = 0; i < recoveryList.size(); i++) {
                HashMap whereMap = new HashMap();
                whereMap = (HashMap) recoveryList.get(i);
                if (whereMap.get("ACT_NUM").toString().equals(tblRecoveryListTally.getValueAt(tblRecoveryListTally.getSelectedRow(), 1))) {
                    prodtype = whereMap.get("PROD_TYPE").toString();
                    break;
                }
            }
            String chittalNo = "";
            String subNo = "";
            String actnum = CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(tblRecoveryListTally.getSelectedRow(), 1));
            int unRec = CommonUtil.convertObjToInt(tblRecoveryListTally.getValueAt(tblRecoveryListTally.getSelectedRow(), 8));
            System.out.println("unRec" + unRec);
            HashMap chkMap = new HashMap();
            chkMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(observable.getTdtCalcIntUpto())));
            if (actnum.indexOf("_") != -1) {
                chittalNo = actnum.substring(0, actnum.indexOf("_"));
                subNo = actnum.substring(actnum.indexOf("_") + 1, actnum.length());
            }
            chkMap.put("CHITTAL_NO", chittalNo);
            if ((prodtype != null || !prodtype.equals("")) && prodtype.equals("MDS")) {
                //getMDSChittalDetails
                List mdsCountlist = ClientUtil.executeQuery("getTotalCount", chkMap);
                System.out.println("mdsCountlist" + mdsCountlist);
                HashMap mdsCount = (HashMap) (mdsCountlist.get(0));
                System.out.println("mdsCount" + mdsCount);
                int tot = CommonUtil.convertObjToInt(mdsCount.get("TOTAL"));
                int rem = tot - unRec;
                chkMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                List mdsDetails = ClientUtil.executeQuery("getMDSChittalDetails", chkMap);
                HashMap detailMap = (HashMap) mdsDetails.get(0);
                double amt = CommonUtil.convertObjToDouble(detailMap.get("INST_AMT"));
                System.out.println("amtamt" + amt);
                List mdsRecList = ClientUtil.executeQuery("getMDSRecoveryDetail", chkMap);
                double bonus = 0.0;
                double penal = 0.0;
                double total = 0.0;
                System.out.println("rem" + rem);
                if (mdsRecList != null && mdsRecList.size() > 0) {
                    for (int i = 0; i < rem; i++) {
                        HashMap mdsRecmap = (HashMap) mdsRecList.get(i);
                        bonus += CommonUtil.convertObjToDouble(mdsRecmap.get("BONUS_AMT"));
                        penal += CommonUtil.convertObjToDouble(mdsRecmap.get("PENAL_AMT"));
                    }
                }
                total = ((rem * amt)) - bonus + penal;
                total = (double) Math.round(total * 100) / 100;
                tblRecoveryListTally.setValueAt(total, tblRecoveryListTally.getSelectedRow(), 7);
                tblRecoveryListTally.setValueAt(penal, tblRecoveryListTally.getSelectedRow(), 9);
                tblRecoveryListTally.setValueAt(amt, tblRecoveryListTally.getSelectedRow(), 10);
            } else if ((prodtype != null || !prodtype.equals("")) && prodtype.equals("TD")) {
                System.out.println("coming td");
                HashMap depMap = getDepositsDetails(actnum, unRec, CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(tdtDtOfRecovery.getDateValue())));
                System.out.println("depMapdepMap" + depMap);
                if (depMap != null && depMap.size() > 0) {
                    tblRecoveryListTally.setValueAt(CommonUtil.convertObjToDouble(depMap.get("RECOVERY_AMT")), tblRecoveryListTally.getSelectedRow(), 7);
                    tblRecoveryListTally.setValueAt(CommonUtil.convertObjToDouble(depMap.get("PENAL")), tblRecoveryListTally.getSelectedRow(), 9);
                    tblRecoveryListTally.setValueAt(CommonUtil.convertObjToDouble(depMap.get("INST_AMT")), tblRecoveryListTally.getSelectedRow(), 10);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void calcMDSRD1() {
        System.out.println("inside calcMDSRD1");
        try {
            String prodtype = CommonUtil.convertObjToStr(prdTypeMap.get(tblproduct.getSelectedRow()));
            String chittalNo = "";
            String subNo = "";
            String actnum = CommonUtil.convertObjToStr(tblproduct.getValueAt(tblproduct.getSelectedRow(), 1));
            int unRec = CommonUtil.convertObjToInt(tblproduct.getValueAt(tblproduct.getSelectedRow(), 9));
            System.out.println("unRecunRec" + unRec);
            HashMap chkMap = new HashMap();
            chkMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(tdtDtOfRecovery.getDateValue())));

            if (actnum.indexOf("_") != -1) {
                chittalNo = actnum.substring(0, actnum.indexOf("_"));
                subNo = actnum.substring(actnum.indexOf("_") + 1, actnum.length());
            }

            chkMap.put("CHITTAL_NO", chittalNo);
            chkMap.put("DEPOSIT_NO", chittalNo);
            if ((prodtype != null && !prodtype.equals("")) && prodtype.equals("MDS")) {
                List mdsCountlist = ClientUtil.executeQuery("getTotalCountMDS", chkMap);
                HashMap mdsCount = (HashMap) (mdsCountlist.get(0));
                int tot = CommonUtil.convertObjToInt(mdsCount.get("TOTAL"));
                int rem = tot - unRec;
                chkMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                List mdsDetails = ClientUtil.executeQuery("getMDSChittalDetails", chkMap);
                HashMap detailMap = (HashMap) mdsDetails.get(0);
                double amt = CommonUtil.convertObjToDouble(detailMap.get("INST_AMT"));
                List mdsRecList = ClientUtil.executeQuery("getMDSRecoveryDetail", chkMap);
                double bonus = 0.0;
                double penal = 0.0;
                double total = 0.0;
                if (mdsRecList != null && mdsRecList.size() > 0) {
                    for (int i = 0; i < rem; i++) {
                        HashMap mdsRecmap = (HashMap) mdsRecList.get(i);
                        bonus += CommonUtil.convertObjToDouble(mdsRecmap.get("BONUS_AMT"));
                        penal += CommonUtil.convertObjToDouble(mdsRecmap.get("PENAL_AMT"));
                    }
                }
                total = ((rem * amt)) - bonus + penal;
                total = (double) Math.round(total * 100) / 100;
                if (total < 0) {
                    ClientUtil.showMessageWindow("Recovery Amount Cannot be negative!!!");
                    tblproduct.setValueAt(tblproduct.getValueAt(tblproduct.getSelectedRow(), 7), tblproduct.getSelectedRow(), 8);
                    tblproduct.setValueAt("0", tblproduct.getSelectedRow(), 9);
                    tblproduct.setValueAt("0", tblproduct.getSelectedRow(), 10);
                    tblproduct.setValueAt("0", tblproduct.getSelectedRow(), 11);
                }
                tblproduct.setValueAt(total, tblproduct.getSelectedRow(), 8);
                tblproduct.setValueAt(CommonUtil.convertObjToDouble(penal), tblproduct.getSelectedRow(), 10);
                tblproduct.setValueAt(CommonUtil.convertObjToDouble(amt), tblproduct.getSelectedRow(), 11);
            } else if ((prodtype != null && !prodtype.equals("")) && prodtype.equals("TD")) {
                List mdsCountlist = ClientUtil.executeQuery("getTotalCountRD", chkMap);
                HashMap mdsCount = (HashMap) (mdsCountlist.get(0));
                int tot = CommonUtil.convertObjToInt(mdsCount.get("TOTAL"));
                int rem = tot - unRec;
                chkMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                double amt = 0.0;
                List mdsRecList = ClientUtil.executeQuery("getRDRecoveryDetail", chkMap);
                double penal = 0.0;
                double total = 0.0;
                if (mdsRecList != null && mdsRecList.size() > 0) {
                    for (int i = 0; i < rem; i++) {
                        HashMap mdsRecmap = (HashMap) mdsRecList.get(i);
                        penal = CommonUtil.convertObjToDouble(mdsRecmap.get("PENAL_AMT"));
                        amt = CommonUtil.convertObjToDouble(mdsRecmap.get("INST_AMT"));
                    }
                }
                total = ((rem * amt)) + penal;
                total = Math.round(total);
                penal = Math.round(penal);
                if (total < 0) {
                    ClientUtil.showMessageWindow("Recovery Amount Cannot be negative!!!");
                    tblproduct.setValueAt(tblproduct.getValueAt(tblproduct.getSelectedRow(), 7), tblproduct.getSelectedRow(), 8);
                    tblproduct.setValueAt("0", tblproduct.getSelectedRow(), 9);
                    tblproduct.setValueAt("0", tblproduct.getSelectedRow(), 10);
                    tblproduct.setValueAt("0", tblproduct.getSelectedRow(), 11);
                }
                tblproduct.setValueAt(total, tblproduct.getSelectedRow(), 8);
                tblproduct.setValueAt(CommonUtil.convertObjToDouble(penal), tblproduct.getSelectedRow(), 10);
                tblproduct.setValueAt(CommonUtil.convertObjToDouble(amt), tblproduct.getSelectedRow(), 11);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public HashMap getDepositsDetails(String actNum, int no, Date currentDate) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap whereMap = new HashMap();
        String behavesLike = "";
        long actualDelay = 0;
        long due = CommonUtil.convertObjToLong(no);
        System.out.println("###### actNum" + actNum);
        if (actNum.indexOf("_") != -1) {
            actNum = actNum.substring(0, actNum.indexOf("_"));
        }
        whereMap.put("ACT_NUM", actNum);
        List behavesLikeList = ClientUtil.executeQuery("getBehavesLikeForDepositNo", whereMap);
        if (behavesLikeList != null && behavesLikeList.size() > 0) {
            whereMap = (HashMap) behavesLikeList.get(0);
            behavesLike = CommonUtil.convertObjToStr(whereMap.get("BEHAVES_LIKE"));
            if (!behavesLike.equals("") && behavesLike != null) {
                System.out.println("###### behavesLike" + behavesLike + "###### actNum" + actNum);

                if (behavesLike.equals("RECURRING")) {            //Recurring Deposit
                    HashMap accountMap = new HashMap();
                    HashMap lastMap = new HashMap();
                    HashMap rdDataMap = new HashMap();
                    rdDataMap.put("DEPOSIT_NO", actNum);
                    accountMap.put("DEPOSIT_NO", actNum);
                    accountMap.put("BRANCH_ID", "0001");
                    List lst = ClientUtil.executeQuery("getProductIdForDeposits", accountMap);
                    if (lst != null && lst.size() > 0) {
                        accountMap = (HashMap) lst.get(0);
                        Date currDt = (Date) currentDate;
                        //                        Date currDate = (Date) currDt.clone();
                        String insBeyondMaturityDat = "";
                        List recurringLst = ClientUtil.executeQuery("getRecurringDepositDetails", accountMap);
                        if (recurringLst != null && recurringLst.size() > 0) {
                            HashMap recurringMap = new HashMap();
                            recurringMap = (HashMap) recurringLst.get(0);
                            insBeyondMaturityDat = CommonUtil.convertObjToStr(recurringMap.get("INST_BEYOND_MATURITY_DATE"));
                        }
                        long totalDelay = 0;

                        double delayAmt = 0.0;
                        double tot_Inst_paid = 0.0;
                        double depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT")).doubleValue();
                        Date matDt = new Date();
                        matDt.setTime(currentDate.getTime());
                        Date depDt = new Date();
                        depDt.setTime(currentDate.getTime());
                        System.out.println("&&&&&&&&&&&& CurrentDate11111" + currentDate);
                        lastMap.put("DEPOSIT_NO", actNum);
                        lst = ClientUtil.executeQuery("getInterestDeptIntTable", lastMap);
                        if (lst != null && lst.size() > 0) {
                            lastMap = (HashMap) lst.get(0);
                            System.out.println("###### lastMap--->" + lastMap);
                            rdDataMap.put("DEPOSIT_AMT", lastMap.get("DEPOSIT_AMT"));
                            rdDataMap.put("MATURITY_DT", lastMap.get("MATURITY_DT"));
                            tot_Inst_paid = CommonUtil.convertObjToDouble(lastMap.get("TOTAL_INSTALL_PAID")).doubleValue();
                            HashMap prematureDateMap = new HashMap();
                            double monthPeriod = 0.0;
                            Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("MATURITY_DT")));
                            System.out.println("############# MATURITY_DT" + matDate);
                            System.out.println("############# CurrentDate" + currentDate);
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
                            System.out.println("############# MATURITY_DT" + matDate);
                            System.out.println("############# CurrentDate" + currentDate);
                            if (DateUtil.dateDiff((Date) matDt, (Date) currentDate) > 0) {
                                matDt = CommonUtil.getProperDate(currentDate, matDt);
                                prematureDateMap.put("TO_DATE", matDt);
                                prematureDateMap.put("FROM_DATE", lastMap.get("DEPOSIT_DT"));
                                lst = ClientUtil.executeQuery("periodRunMap", prematureDateMap);
                                if (lst != null && lst.size() > 0) {
                                    prematureDateMap = (HashMap) lst.get(0);
                                    System.out.println("############# prematureDateMap" + prematureDateMap);
                                    monthPeriod = CommonUtil.convertObjToDouble(prematureDateMap.get("NO_OF_MONTHS")).doubleValue();
                                    actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
                                    System.out.println("############# actualDelay" + actualDelay + "############# monthPeriod" + monthPeriod + "############# tot_Inst_paid" + tot_Inst_paid);
                                }
                                lst = null;
                            } else {
                                System.out.println("currDate" + currentDate);
                                java.util.GregorianCalendar gactualCurrDateCalendar = new java.util.GregorianCalendar();
                                gactualCurrDateCalendar.setGregorianChange(currentDate);
                                gactualCurrDateCalendar.setTime(currentDate);
                                int curDay = gactualCurrDateCalendar.get(gactualCurrDateCalendar.DAY_OF_MONTH);
                                System.out.println("curDaycurDay" + curDay);
                                List recoveryList = ClientUtil.executeQuery("getRecoveryParameters", whereMap);
                                int gracePeriod = 0;
                                if (recoveryList != null && recoveryList.size() > 0) {
                                    HashMap recoveryDetailsMap = (HashMap) recoveryList.get(0);
                                    if (recoveryDetailsMap != null && recoveryDetailsMap.size() > 0 && recoveryDetailsMap.containsKey("GRACE_PERIOD")) {
                                        gracePeriod = CommonUtil.convertObjToInt(recoveryDetailsMap.get("GRACE_PERIOD"));
                                    }
                                }
                                if (curDay > gracePeriod) {
                                    gactualCurrDateCalendar.add(gactualCurrDateCalendar.MONTH, 1);
                                    gactualCurrDateCalendar.set(gactualCurrDateCalendar.DAY_OF_MONTH, gracePeriod);

                                } else {
                                    gactualCurrDateCalendar.set(gactualCurrDateCalendar.DAY_OF_MONTH, gracePeriod);
                                }
                                Date tempcurrDate = new Date();
                                tempcurrDate = CommonUtil.getProperDate(currDt, gactualCurrDateCalendar.getTime());
                                Date dueDate = new Date();

                                HashMap detailedMap = new HashMap();
                                detailedMap.put("DEPOSIT_NO", actNum + "_1");
                                List newList = ClientUtil.executeQuery("getBalnceDepositDetails", detailedMap);
                                if (newList != null && newList.size() > 0) {
                                    for (int i = 0; i < newList.size(); i++) {
                                        HashMap newMap = (HashMap) newList.get(i);
                                        if (newMap != null && newMap.size() > 0) {
                                            dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(newMap.get("DUE_DATE")));
                                            System.out.println("tempcurrDate" + tempcurrDate + "dueDate" + dueDate);
                                            if (dueDate.compareTo(tempcurrDate) == 0 || dueDate.compareTo(tempcurrDate) < 0) {
                                                actualDelay = actualDelay + 1;
                                            }
                                        }
                                    }
                                }
                                System.out.println("actualDelay" + actualDelay);
                            }
                        }
                        lst = null;

                        if ((DateUtil.dateDiff((Date) matDt, (Date) currentDate) > 0) && !insBeyondMaturityDat.equals("") && insBeyondMaturityDat.equals("N")) {
                            dataMap = new HashMap();
                            return dataMap;
                        }
                        System.out.println("#############%%%%% MATURITY_DT" + matDt);
                        System.out.println("############# %%%% CurrentDate" + currentDate);
                        String prodId = "";
                        HashMap newMap = new HashMap();
                        newMap.put("ACT_NUM", actNum);
                        List productIdList = ClientUtil.executeQuery("getAccNoDet", newMap);
                        if (productIdList != null && productIdList.size() > 0) {
                            HashMap prodMap = (HashMap) productIdList.get(0);
                            if (prodMap != null && prodMap.size() > 0) {
                                prodId = CommonUtil.convertObjToStr(prodMap.get("Product Id"));
                            }
                        }
                        String penalCalcType = "DAYS";//INSTALMENT
                        HashMap dailyMap = new HashMap();
                        dailyMap.put("ROI_GROUP_ID", prodId);
                        List list = (List) ClientUtil.executeQuery("getSelectDepositsCommision", dailyMap);
                        if (list != null && list.size() > 0) {
                            System.out.println("list list list" + list);
                            InterestMaintenanceRateTO objInterestMaintenanceRateTO = (InterestMaintenanceRateTO) list.get(0);
                            if (objInterestMaintenanceRateTO != null) {
                                penalCalcType = CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getInstType());
                            }
                        }
                        //delayed installment calculation...
                        if (DateUtil.dateDiff((Date) matDt, (Date) currDt) < 0 || insBeyondMaturityDat.equals("Y")) {
                            if (penalCalcType != null && !penalCalcType.equals("") && penalCalcType.equals("Installments")) {
                                depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT")).doubleValue();
                                double chargeAmt = depAmt / 100;
                                HashMap delayMap = new HashMap();
                                delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
                                delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
                                lst = ClientUtil.executeQuery("getSelectDelayedRate", delayMap);
                                if (lst != null && lst.size() > 0) {
                                    delayMap = (HashMap) lst.get(0);
                                    delayAmt = CommonUtil.convertObjToDouble(delayMap.get("PENAL_INT")).doubleValue();
                                    delayAmt = delayAmt * chargeAmt;
                                    System.out.println("######recurring delayAmt : " + delayAmt);
                                }
                                lst = null;
                                HashMap depRecMap = new HashMap();
                                depRecMap.put("DEPOSIT_NO", actNum + "_1");
                                List lstRec = ClientUtil.executeQuery("getDepTransactionRecurring", depRecMap);
                                if (lstRec != null && lstRec.size() > 0) {//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
                                    for (int i = 0; i < lstRec.size(); i++) {//when the customer is paid from that dt to today dt....
                                        depRecMap = (HashMap) lstRec.get(i);
                                        Date transDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("TRANS_DT")));
                                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                                        int transMonth = transDt.getMonth() + 1;
                                        int dueMonth = dueDate.getMonth() + 1;
                                        int dueYear = dueDate.getYear() + 1900;
                                        int transYear = transDt.getYear() + 1900;
                                        int delayedInstallment;// = transMonth - dueMonth;
                                        if (dueYear == transYear) {
                                            delayedInstallment = transMonth - dueMonth;
                                        } else {
                                            int diffYear = transYear - dueYear;
                                            delayedInstallment = (diffYear * 12 - dueMonth) + transMonth;
                                        }
                                        if (delayedInstallment < 0) {
                                            delayedInstallment = 0;
                                        }
                                        totalDelay = totalDelay + delayedInstallment;
                                    }
                                }
                                System.out.println("totalDelay    111  :" + totalDelay);
                                lstRec = null;
                                depRecMap = new HashMap();
                                depRecMap.put("DEPOSIT_NO", actNum + "_1");
                                depRecMap.put("DEPOSIT_DT", lastMap.get("DEPOSIT_DT"));
                                // Date recDate=setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtDtOfRecovery.getDateValue()));
                                depRecMap.put("CURR_DT", currentDate);//setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtDtOfRecovery.getDateValue())));
                                depRecMap.put("SL_NO", new Double(tot_Inst_paid));
                                lstRec = ClientUtil.executeQuery("getDepTransRecurr", depRecMap);
                                if (lstRec != null && lstRec.size() > 0) {//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
                                    for (int i = 0; i < lstRec.size(); i++) {//when the customer is paid from that dt to today dt....
                                        depRecMap = (HashMap) lstRec.get(i);
                                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                                        int transMonth = currentDate.getMonth() + 1;
                                        int dueMonth = dueDate.getMonth() + 1;
                                        int dueYear = dueDate.getYear() + 1900;
                                        int transYear = currentDate.getYear() + 1900;
                                        int delayedInstallment;// = transMonth - dueMonth;
                                        if (dueYear == transYear) {
                                            delayedInstallment = transMonth - dueMonth;
                                        } else {
                                            int diffYear = transYear - dueYear;
                                            delayedInstallment = (diffYear * 12 - dueMonth) + transMonth;
                                        }
                                        if (delayedInstallment < 0) {
                                            delayedInstallment = 0;
                                        }
                                        totalDelay = totalDelay + delayedInstallment;
                                    }
                                }
                                lstRec = null;
                                double cummInst = 0.0;
                                actualDelay = actualDelay - 1;
                                if (actualDelay == no) {
                                    cummInst = actualDelay * (actualDelay + 1) / 2;
                                } else {
                                    double diff = actualDelay - no;
                                    cummInst = (actualDelay * (actualDelay + 1) / 2) - (diff * (diff + 1) / 2);
                                }

                                totalDelay = totalDelay - no;
                                ///delayAmt = delayAmt * totalDelay;
                                delayAmt = (cummInst * delayAmt);


                                //delayAmt = (double) delayAmt * 100), 100) / 100;
                                double oldPenalAmt = CommonUtil.convertObjToDouble(accountMap.get("DELAYED_AMOUNT")).doubleValue();
                                long oldPenalMonth = CommonUtil.convertObjToLong(accountMap.get("DELAYED_MONTH"));
                                double balanceAmt = 0.0;
                                if (oldPenalAmt > 0) {
                                    balanceAmt = delayAmt - oldPenalAmt;
                                    totalDelay = totalDelay - oldPenalMonth;
                                } else {
                                    balanceAmt = delayAmt;
                                }
                                double principal = totalDelay * CommonUtil.convertObjToDouble(rdDataMap.get("DEPOSIT_AMT")).doubleValue();
                                double totalDemand = principal + balanceAmt;
                                rdDataMap.put("DELAYED_MONTH", accountMap.get("DELAYED_MONTH"));
                                rdDataMap.put("DEPOSIT_PENAL_AMT", String.valueOf(balanceAmt));
                                rdDataMap.put("PRINCIPAL", String.valueOf(principal));
                                rdDataMap.put("TOTAL_DEMAND", String.valueOf(totalDemand));
                                rdDataMap.put("DEPOSIT_PENAL_MONTH", actualDelay/*
                                         * String.valueOf(totalDelay)
                                         */);
                                double totalamt = ((due) * CommonUtil.convertObjToDouble(rdDataMap.get("DEPOSIT_AMT"))) + balanceAmt;
                                rdDataMap.put("RECOVERY_AMT", String.valueOf(totalamt));
                            }
                            if (penalCalcType != null && !penalCalcType.equals("") && penalCalcType.equals("Days")) {
                                depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT"));
                                dataMap.put("INSTMT_AMT", depAmt);
                                HashMap delayMap = new HashMap();
                                double roi = 0.0;
                                delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
                                delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
                                lst = ClientUtil.executeQuery("getSelectDelayedRate", delayMap);
                                if (lst != null && lst.size() > 0) {
                                    delayMap = (HashMap) lst.get(0);
                                    roi = CommonUtil.convertObjToDouble(delayMap.get("PENAL_INT"));
                                }
                                List lstRec = null;
                                HashMap depRecMap = new HashMap();
                                depRecMap = new HashMap();
                                double penal = 0.0;
                                depRecMap.put("DEPOSIT_NO", actNum + "_1");
                                depRecMap.put("DEPOSIT_DT", lastMap.get("DEPOSIT_DT"));
                                depRecMap.put("CURR_DT", currentDate);
                                depRecMap.put("SL_NO", new Double(tot_Inst_paid));
                                lstRec = ClientUtil.executeQuery("getDepTransRecurr", depRecMap);
                                System.out.println("lstRec" + lstRec);
                                int size = CommonUtil.convertObjToInt(lstRec.size());
                                System.out.println("depAmt" + depAmt + "size" + size);
                                if (lstRec != null && (lstRec.size() - due) > 0) {
                                    for (int i = 0; i < size; i++) {
                                        depRecMap = (HashMap) lstRec.get(i);
                                        System.out.println("depRecMap" + depRecMap);
                                        double amount = depAmt * (i + 1);
                                        System.out.println("amount" + amount);
                                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                                        System.out.println("dueDate" + dueDate + "Jeff DateUtil.dateDiff(dueDate, currDt)" + DateUtil.dateDiff(dueDate, currDt));
                                        if (DateUtil.dateDiff(dueDate, currDt) > 0) {
                                            double diff = DateUtil.dateDiff(dueDate, currDt) - 1;
                                            System.out.println("diff" + diff);
                                            if (diff > 0) {
                                                penal = penal + ((amount * roi * diff) / 36500);
                                                System.out.println("penal" + penal);
                                            }
                                        }
                                    }
                                }
                                penal = Math.round(penal);
                                System.out.println("penal" + penal + "actualDelay");
                                double principal = (actualDelay - due) * CommonUtil.convertObjToDouble(rdDataMap.get("DEPOSIT_AMT"));
                                System.out.println("principal" + principal);
                                double totalDemand = principal + penal;
                                rdDataMap.put("DELAYED_MONTH", accountMap.get("DELAYED_MONTH"));
                                rdDataMap.put("DEPOSIT_PENAL_AMT", String.valueOf(penal));
                                rdDataMap.put("PRINCIPAL", String.valueOf(principal));
                                rdDataMap.put("TOTAL_DEMAND", String.valueOf(totalDemand));
                                rdDataMap.put("DEPOSIT_PENAL_MONTH", String.valueOf(totalDelay));
                                rdDataMap.put("RECOVERY_AMT", String.valueOf(totalDemand));
                            }
                        }
                    }
                    System.out.println("#### rdDataMap--->" + rdDataMap);
                    dataMap.put("PRINCIPAL", rdDataMap.get("PRINCIPAL"));
                    dataMap.put("PENAL", rdDataMap.get("DEPOSIT_PENAL_AMT"));
                    dataMap.put("TOTAL_DEMAND", rdDataMap.get("TOTAL_DEMAND"));
                    dataMap.put("DEPOSIT_PENAL_MONTH", rdDataMap.get("DEPOSIT_PENAL_MONTH"));
                    dataMap.put("INTEREST", new Double(0));
                    dataMap.put("CHARGES", new Double(0));
                    dataMap.put("TOTAL", due);
                    dataMap.put("RECOVERY_AMT", rdDataMap.get("RECOVERY_AMT"));
                    dataMap.put("INST_AMT", rdDataMap.get("DEPOSIT_AMT"));
                    if (CommonUtil.convertObjToDouble(rdDataMap.get("TOTAL_DEMAND")).doubleValue() <= 0.0) {
                        dataMap = null;
                    }
                    System.out.println("######## dataMap" + dataMap);
                }
            }
        }
        return dataMap;
    }

    private void calcTallyListTotal() {
        double totAmt = 0;
        if (tblRecoveryListTally.getRowCount() > 0) {
            for (int i = 0; i < tblRecoveryListTally.getRowCount(); i++) {
                totAmt = totAmt + CommonUtil.convertObjToDouble(tblRecoveryListTally.getValueAt(i, 7).toString()).doubleValue();
            }
            txtTotalRecoveredTallyAmt.setText(String.valueOf(totAmt));
        }
    }

    private void calcUnRecTallyListTotal() {
        double totAmt = 0;
        if (tblproduct.getRowCount() > 0) {
            for (int i = 0; i < tblproduct.getRowCount(); i++) {
                totAmt = totAmt + CommonUtil.convertObjToDouble(tblproduct.getValueAt(i, 8));
            }
            totAmt = Math.round(totAmt * 100.0) / 100.0;
            txtTotalUnRecAmt.setText(CommonUtil.convertObjToStr(totAmt));
            double totDamt = CommonUtil.convertObjToDouble(txtTotDemAmt.getText());
            double total = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(totDamt - totAmt));
            total = Math.round(total * 100.0) / 100.0;
            txtDiffAmt.setText(CommonUtil.convertObjToStr(total));
        }
    }

    public static String formatCrore(String str) {
        java.text.DecimalFormat numberFormat = new java.text.DecimalFormat();
        numberFormat.applyPattern("########################0.00");

        double currData = Double.parseDouble(str.replaceAll(",", ""));
        str = numberFormat.format(currData);

        String num = str.substring(0, str.lastIndexOf(".")).replaceAll(",", "");
        String dec = str.substring(str.lastIndexOf("."));

        String sign = "";
        if (num.substring(0, 1).equals("-")) {
            sign = num.substring(0, 1);
            num = num.substring(1, num.length());
        }
        char[] chrArr = num.toCharArray();
        StringBuffer fmtStrB = new StringBuffer();

        for (int i = chrArr.length - 1, j = 0, k = 0; i >= 0; i--) {
            if ((j == 3 && k == 3) || (j == 2 && k == 5) || (j == 2 && k == 7)) {
                fmtStrB.insert(0, ",");
                if (k == 7) {
                    k = 0;
                }
                j = 0;
            }
            j++;
            k++;

            fmtStrB.insert(0, chrArr[i]);
        }
        fmtStrB.append(dec);

        str = fmtStrB.toString();

        str = sign + str;

        if (str.equals(".00")) {
            str = "0";
        }

        return str;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tabRecoveryListTally = new com.see.truetransact.uicomponent.CTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        panCustomerDetail = new com.see.truetransact.uicomponent.CPanel();
        lblEmployeNo = new com.see.truetransact.uicomponent.CLabel();
        txtEmployeeNo = new com.see.truetransact.uicomponent.CTextField();
        btnEmployeeNo = new com.see.truetransact.uicomponent.CButton();
        lblDtOfBirth = new com.see.truetransact.uicomponent.CLabel();
        tdtDtOfRecovery = new com.see.truetransact.uicomponent.CDateField();
        btnUnrecoveryProcess = new com.see.truetransact.uicomponent.CButton();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblproduct = new com.see.truetransact.uicomponent.CTable();
        panTotalAmount3 = new com.see.truetransact.uicomponent.CPanel();
        btnUnrecoverySave = new com.see.truetransact.uicomponent.CButton();
        lbltotUnRecAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTotDemAmt = new com.see.truetransact.uicomponent.CTextField();
        txtTotalUnRecAmt = new com.see.truetransact.uicomponent.CTextField();
        txtDiffAmt = new com.see.truetransact.uicomponent.CTextField();
        panRecoveryTallyList = new com.see.truetransact.uicomponent.CPanel();
        panImportSalaryRecoveryList = new com.see.truetransact.uicomponent.CPanel();
        lblRecoveredListDate = new com.see.truetransact.uicomponent.CLabel();
        lblRecoveredListDateVal = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        panRecoveryEditDetails = new com.see.truetransact.uicomponent.CPanel();
        btnOk = new com.see.truetransact.uicomponent.CButton();
        lblRecoveredDate = new com.see.truetransact.uicomponent.CLabel();
        tdtRecoveredDate = new com.see.truetransact.uicomponent.CDateField();
        panBrowseDetails = new com.see.truetransact.uicomponent.CPanel();
        lblRecoveredList = new com.see.truetransact.uicomponent.CLabel();
        btnBrowse = new com.see.truetransact.uicomponent.CButton();
        cboEmp_type = new com.see.truetransact.uicomponent.CComboBox();
        panSalaryRecoveryList = new com.see.truetransact.uicomponent.CPanel();
        srpSalaryRecoveryList = new com.see.truetransact.uicomponent.CScrollPane();
        tblSalaryRecoveryList = new com.see.truetransact.uicomponent.CTable();
        panTotalAmount = new com.see.truetransact.uicomponent.CPanel();
        lblTotalTransactionAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTotalAmount = new com.see.truetransact.uicomponent.CTextField();
        txtTotalRecoveredAmount = new com.see.truetransact.uicomponent.CTextField();
        srpTableRecoveryListTally = new com.see.truetransact.uicomponent.CScrollPane();
        tblRecoveryListTally = new com.see.truetransact.uicomponent.CTable();
        panTallyTableTotalAmount = new com.see.truetransact.uicomponent.CPanel();
        txtTotalDemandTally = new com.see.truetransact.uicomponent.CTextField();
        txtTotalRecoveredTallyAmt = new com.see.truetransact.uicomponent.CTextField();
        panSchedBtn = new com.see.truetransact.uicomponent.CPanel();
        btnSchedNew = new com.see.truetransact.uicomponent.CButton();
        btnSchedSave = new com.see.truetransact.uicomponent.CButton();
        btnSchedDelete = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        lblTotalTransactionAmt2 = new com.see.truetransact.uicomponent.CLabel();
        btnAcceptChanges = new com.see.truetransact.uicomponent.CButton();
        panAccountDetails = new com.see.truetransact.uicomponent.CPanel();
        panCustomerNO = new com.see.truetransact.uicomponent.CPanel();
        txtAccountNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNo = new com.see.truetransact.uicomponent.CButton();
        lblAccountNo = new com.see.truetransact.uicomponent.CLabel();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblAccountName = new com.see.truetransact.uicomponent.CLabel();
        panProcessRecoveryList = new com.see.truetransact.uicomponent.CPanel();
        panImportSalaryRecoveryList1 = new com.see.truetransact.uicomponent.CPanel();
        lblRecoveredListDateTally = new com.see.truetransact.uicomponent.CLabel();
        lblRecoveredListDateTallyVal = new com.see.truetransact.uicomponent.CLabel();
        panMemberReceiptTrans = new com.see.truetransact.uicomponent.CPanel();
        btnProcess = new com.see.truetransact.uicomponent.CButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(860, 655));
        setMinimumSize(new java.awt.Dimension(860, 655));
        setPreferredSize(new java.awt.Dimension(860, 645));

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setText("                      ");
        lblStatus.setFont(new java.awt.Font("MS Sans Serif", 1, 12));
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

        tabRecoveryListTally.setMinimumSize(new java.awt.Dimension(850, 590));
        tabRecoveryListTally.setPreferredSize(new java.awt.Dimension(850, 590));

        jPanel1.setLayout(new java.awt.GridBagLayout());

        panCustomerDetail.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panCustomerDetail.setMaximumSize(new java.awt.Dimension(370, 140));
        panCustomerDetail.setMinimumSize(new java.awt.Dimension(370, 140));
        panCustomerDetail.setPreferredSize(new java.awt.Dimension(350, 140));
        panCustomerDetail.setLayout(new java.awt.GridBagLayout());

        lblEmployeNo.setText("Employee No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panCustomerDetail.add(lblEmployeNo, gridBagConstraints);

        txtEmployeeNo.setBackground(new java.awt.Color(220, 220, 220));
        txtEmployeeNo.setAllowAll(true);
        txtEmployeeNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmployeeNoActionPerformed(evt);
            }
        });
        txtEmployeeNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmployeeNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panCustomerDetail.add(txtEmployeeNo, gridBagConstraints);

        btnEmployeeNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEmployeeNo.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnEmployeeNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnEmployeeNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnEmployeeNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEmployeeNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmployeeNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panCustomerDetail.add(btnEmployeeNo, gridBagConstraints);

        lblDtOfBirth.setText("Recovery Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(19, 1, 0, 9);
        panCustomerDetail.add(lblDtOfBirth, gridBagConstraints);

        tdtDtOfRecovery.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtDtOfRecovery.setName("tdtToDate"); // NOI18N
        tdtDtOfRecovery.setPreferredSize(new java.awt.Dimension(101, 19));
        tdtDtOfRecovery.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDtOfRecoveryFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 7);
        panCustomerDetail.add(tdtDtOfRecovery, gridBagConstraints);

        btnUnrecoveryProcess.setForeground(new java.awt.Color(51, 102, 0));
        btnUnrecoveryProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnUnrecoveryProcess.setText("PROCESS");
        btnUnrecoveryProcess.setToolTipText("Start Process");
        btnUnrecoveryProcess.setFont(new java.awt.Font("MS Sans Serif", 1, 12));
        btnUnrecoveryProcess.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnUnrecoveryProcessMousePressed(evt);
            }
        });
        btnUnrecoveryProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnrecoveryProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(12, 4, 4, 4);
        panCustomerDetail.add(btnUnrecoveryProcess, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = -9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(63, 118, 0, 0);
        jPanel1.add(panCustomerDetail, gridBagConstraints);

        cScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        cScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        cScrollPane1.setMaximumSize(new java.awt.Dimension(700, 250));
        cScrollPane1.setMinimumSize(new java.awt.Dimension(700, 250));
        cScrollPane1.setPreferredSize(new java.awt.Dimension(700, 250));

        tblproduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Scheme Name", "A/C Number", "Principal", "Interest", "Penal Interest", "Charges", "No Of Installment", "Demand", "Recovery Amount", "Unrecovery", "Rec Penal Int", "Inst. Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, true, true, true, true, true, true, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblproduct.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblproduct.setMaximumSize(new java.awt.Dimension(2000, 1000));
        tblproduct.setMinimumSize(new java.awt.Dimension(2000, 500));
        tblproduct.setOpaque(false);
        tblproduct.setPreferredScrollableViewportSize(new java.awt.Dimension(2000, 500));
        tblproduct.setPreferredSize(new java.awt.Dimension(2000, 500));
        cScrollPane1.setViewportView(tblproduct);
        tblproduct.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblproduct.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblproduct.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblproduct.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblproduct.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblproduct.getColumnModel().getColumn(6).setPreferredWidth(100);
        tblproduct.getColumnModel().getColumn(8).setPreferredWidth(100);
        tblproduct.getColumnModel().getColumn(9).setPreferredWidth(100);
        tblproduct.getColumnModel().getColumn(11).setMinWidth(0);
        tblproduct.getColumnModel().getColumn(11).setPreferredWidth(0);
        tblproduct.getColumnModel().getColumn(11).setMaxWidth(0);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.ipadx = 133;
        gridBagConstraints.ipady = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 118, 0, 49);
        jPanel1.add(cScrollPane1, gridBagConstraints);

        panTotalAmount3.setMinimumSize(new java.awt.Dimension(780, 30));
        panTotalAmount3.setPreferredSize(new java.awt.Dimension(780, 30));
        panTotalAmount3.setLayout(new java.awt.GridBagLayout());

        btnUnrecoverySave.setForeground(new java.awt.Color(51, 102, 0));
        btnUnrecoverySave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnUnrecoverySave.setText("Save");
        btnUnrecoverySave.setToolTipText("Start Process");
        btnUnrecoverySave.setFont(new java.awt.Font("MS Sans Serif", 1, 12));
        btnUnrecoverySave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnUnrecoverySaveMousePressed(evt);
            }
        });
        btnUnrecoverySave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnrecoverySaveActionPerformed(evt);
            }
        });
        panTotalAmount3.add(btnUnrecoverySave, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 19, 14, 0);
        jPanel1.add(panTotalAmount3, gridBagConstraints);

        lbltotUnRecAmt.setText("Total UnRecovery");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 442, 0, 0);
        jPanel1.add(lbltotUnRecAmt, gridBagConstraints);

        txtTotDemAmt.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 0);
        jPanel1.add(txtTotDemAmt, gridBagConstraints);

        txtTotalUnRecAmt.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        jPanel1.add(txtTotalUnRecAmt, gridBagConstraints);

        txtDiffAmt.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        jPanel1.add(txtDiffAmt, gridBagConstraints);

        tabRecoveryListTally.addTab("Unrecovery", jPanel1);

        panRecoveryTallyList.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panRecoveryTallyList.setMaximumSize(new java.awt.Dimension(800, 950));
        panRecoveryTallyList.setMinimumSize(new java.awt.Dimension(800, 950));
        panRecoveryTallyList.setPreferredSize(new java.awt.Dimension(800, 950));
        panRecoveryTallyList.setLayout(new java.awt.GridBagLayout());

        panImportSalaryRecoveryList.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panImportSalaryRecoveryList.setMinimumSize(new java.awt.Dimension(830, 40));
        panImportSalaryRecoveryList.setPreferredSize(new java.awt.Dimension(830, 40));
        panImportSalaryRecoveryList.setLayout(new java.awt.GridBagLayout());

        lblRecoveredListDate.setText("Recovered List Date : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 2);
        panImportSalaryRecoveryList.add(lblRecoveredListDate, gridBagConstraints);

        lblRecoveredListDateVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        lblRecoveredListDateVal.setMinimumSize(new java.awt.Dimension(100, 21));
        lblRecoveredListDateVal.setName("lblCustNameValue"); // NOI18N
        lblRecoveredListDateVal.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panImportSalaryRecoveryList.add(lblRecoveredListDateVal, gridBagConstraints);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panImportSalaryRecoveryList.add(btnCancel, gridBagConstraints);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setText("EDIT");
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panImportSalaryRecoveryList.add(btnEdit, gridBagConstraints);

        panRecoveryEditDetails.setMinimumSize(new java.awt.Dimension(340, 35));
        panRecoveryEditDetails.setPreferredSize(new java.awt.Dimension(340, 35));
        panRecoveryEditDetails.setLayout(new java.awt.GridBagLayout());

        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 30, 4, 4);
        panRecoveryEditDetails.add(btnOk, gridBagConstraints);

        lblRecoveredDate.setText("Recovered Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRecoveryEditDetails.add(lblRecoveredDate, gridBagConstraints);

        tdtRecoveredDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtRecoveredDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRecoveryEditDetails.add(tdtRecoveredDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panImportSalaryRecoveryList.add(panRecoveryEditDetails, gridBagConstraints);

        panBrowseDetails.setMinimumSize(new java.awt.Dimension(335, 35));
        panBrowseDetails.setPreferredSize(new java.awt.Dimension(335, 35));
        panBrowseDetails.setLayout(new java.awt.GridBagLayout());

        lblRecoveredList.setText("Import Recovered List");
        lblRecoveredList.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 25, 2, 2);
        panBrowseDetails.add(lblRecoveredList, gridBagConstraints);

        btnBrowse.setForeground(new java.awt.Color(51, 51, 255));
        btnBrowse.setText("Browse");
        btnBrowse.setToolTipText("Browse");
        btnBrowse.setFont(new java.awt.Font("MS Sans Serif", 1, 12));
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 15, 2, 2);
        panBrowseDetails.add(btnBrowse, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panImportSalaryRecoveryList.add(panBrowseDetails, gridBagConstraints);

        cboEmp_type.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "               ", "Canteen", "Society" }));
        cboEmp_type.setMinimumSize(new java.awt.Dimension(100, 21));
        cboEmp_type.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboEmp_typeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        panImportSalaryRecoveryList.add(cboEmp_type, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRecoveryTallyList.add(panImportSalaryRecoveryList, gridBagConstraints);

        panSalaryRecoveryList.setBorder(javax.swing.BorderFactory.createTitledBorder("Tally Details"));
        panSalaryRecoveryList.setMinimumSize(new java.awt.Dimension(830, 510));
        panSalaryRecoveryList.setPreferredSize(new java.awt.Dimension(830, 510));
        panSalaryRecoveryList.setLayout(new java.awt.GridBagLayout());

        srpSalaryRecoveryList.setMinimumSize(new java.awt.Dimension(810, 175));
        srpSalaryRecoveryList.setPreferredSize(new java.awt.Dimension(810, 175));

        tblSalaryRecoveryList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SNo.", "Emp Ref.No.", "Member Name", "Total Amount", "Recovered Amount"
            }
        ));
        tblSalaryRecoveryList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblSalaryRecoveryList.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 331));
        tblSalaryRecoveryList.setSelectionBackground(new java.awt.Color(204, 204, 255));
        tblSalaryRecoveryList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSalaryRecoveryListMouseClicked(evt);
            }
        });
        srpSalaryRecoveryList.setViewportView(tblSalaryRecoveryList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        panSalaryRecoveryList.add(srpSalaryRecoveryList, gridBagConstraints);

        panTotalAmount.setMinimumSize(new java.awt.Dimension(780, 30));
        panTotalAmount.setPreferredSize(new java.awt.Dimension(780, 30));
        panTotalAmount.setLayout(new java.awt.GridBagLayout());

        lblTotalTransactionAmt.setText("Total   ");
        lblTotalTransactionAmt.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 450, 4, 4);
        panTotalAmount.add(lblTotalTransactionAmt, gridBagConstraints);

        txtTotalAmount.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalAmount.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        txtTotalAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        panTotalAmount.add(txtTotalAmount, gridBagConstraints);

        txtTotalRecoveredAmount.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalRecoveredAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalRecoveredAmount.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        txtTotalRecoveredAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 40, 4, 4);
        panTotalAmount.add(txtTotalRecoveredAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        panSalaryRecoveryList.add(panTotalAmount, gridBagConstraints);

        srpTableRecoveryListTally.setMinimumSize(new java.awt.Dimension(810, 150));
        srpTableRecoveryListTally.setPreferredSize(new java.awt.Dimension(810, 150));
        srpTableRecoveryListTally.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                srpTableRecoveryListTallyMouseClicked(evt);
            }
        });

        tblRecoveryListTally.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Scheme Name", "Acc No", "Principal", "Interest", "Penal Interest", "Charges", "Demand", "Recovered Amount ", "UnRecovery", "Rec Penal Int", "Inst.Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, true, true, true, true, true, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblRecoveryListTally.setMinimumSize(new java.awt.Dimension(810, 1500));
        tblRecoveryListTally.setOpaque(false);
        tblRecoveryListTally.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 191));
        tblRecoveryListTally.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRecoveryListTallyMouseClicked(evt);
            }
        });
        tblRecoveryListTally.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblRecoveryListTallyPropertyChange(evt);
            }
        });
        srpTableRecoveryListTally.setViewportView(tblRecoveryListTally);
        tblRecoveryListTally.getColumnModel().getColumn(9).setMinWidth(0);
        tblRecoveryListTally.getColumnModel().getColumn(9).setPreferredWidth(0);
        tblRecoveryListTally.getColumnModel().getColumn(9).setMaxWidth(0);
        tblRecoveryListTally.getColumnModel().getColumn(10).setMinWidth(0);
        tblRecoveryListTally.getColumnModel().getColumn(10).setPreferredWidth(0);
        tblRecoveryListTally.getColumnModel().getColumn(10).setMaxWidth(0);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        panSalaryRecoveryList.add(srpTableRecoveryListTally, gridBagConstraints);

        panTallyTableTotalAmount.setMinimumSize(new java.awt.Dimension(780, 80));
        panTallyTableTotalAmount.setPreferredSize(new java.awt.Dimension(780, 80));
        panTallyTableTotalAmount.setLayout(new java.awt.GridBagLayout());

        txtTotalDemandTally.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalDemandTally.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        txtTotalDemandTally.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        panTallyTableTotalAmount.add(txtTotalDemandTally, gridBagConstraints);

        txtTotalRecoveredTallyAmt.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalRecoveredTallyAmt.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        txtTotalRecoveredTallyAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 4, 4);
        panTallyTableTotalAmount.add(txtTotalRecoveredTallyAmt, gridBagConstraints);

        panSchedBtn.setMinimumSize(new java.awt.Dimension(205, 35));
        panSchedBtn.setPreferredSize(new java.awt.Dimension(205, 35));
        panSchedBtn.setLayout(new java.awt.GridBagLayout());

        btnSchedNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnSchedNew.setToolTipText("New");
        btnSchedNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSchedNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSchedNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSchedNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSchedNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedBtn.add(btnSchedNew, gridBagConstraints);

        btnSchedSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSchedSave.setToolTipText("Save");
        btnSchedSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSchedSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSchedSave.setName("btnContactNoAdd"); // NOI18N
        btnSchedSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSchedSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSchedSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedBtn.add(btnSchedSave, gridBagConstraints);

        btnSchedDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnSchedDelete.setToolTipText("Delete");
        btnSchedDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSchedDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSchedDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSchedDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSchedDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedBtn.add(btnSchedDelete, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.setToolTipText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedBtn.add(btnClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(0, 100, 0, 0);
        panTallyTableTotalAmount.add(panSchedBtn, gridBagConstraints);

        lblTotalTransactionAmt2.setText("Total ");
        lblTotalTransactionAmt2.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 30, 4, 4);
        panTallyTableTotalAmount.add(lblTotalTransactionAmt2, gridBagConstraints);

        btnAcceptChanges.setForeground(new java.awt.Color(255, 0, 51));
        btnAcceptChanges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAcceptChanges.setText("Accept Changes");
        btnAcceptChanges.setToolTipText("Accept Changes");
        btnAcceptChanges.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        btnAcceptChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcceptChangesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 30, 4, 4);
        panTallyTableTotalAmount.add(btnAcceptChanges, gridBagConstraints);

        panAccountDetails.setMinimumSize(new java.awt.Dimension(800, 28));
        panAccountDetails.setPreferredSize(new java.awt.Dimension(800, 28));
        panAccountDetails.setLayout(new java.awt.GridBagLayout());

        panCustomerNO.setMinimumSize(new java.awt.Dimension(125, 21));
        panCustomerNO.setPreferredSize(new java.awt.Dimension(125, 21));
        panCustomerNO.setLayout(new java.awt.GridBagLayout());

        txtAccountNo.setEditable(false);
        txtAccountNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustomerNO.add(txtAccountNo, gridBagConstraints);

        btnAccountNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountNo.setMinimumSize(new java.awt.Dimension(28, 28));
        btnAccountNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccountNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustomerNO.add(btnAccountNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountDetails.add(panCustomerNO, gridBagConstraints);

        lblAccountNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAccountNo.setText("Account No");
        lblAccountNo.setMaximumSize(new java.awt.Dimension(95, 18));
        lblAccountNo.setMinimumSize(new java.awt.Dimension(95, 18));
        lblAccountNo.setPreferredSize(new java.awt.Dimension(95, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 4, 2);
        panAccountDetails.add(lblAccountNo, gridBagConstraints);

        lblProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails.add(lblProdId, gridBagConstraints);

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails.add(lblProductType, gridBagConstraints);

        cboProdType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(125);
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails.add(cboProdType, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(200);
        cboProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails.add(cboProdId, gridBagConstraints);

        lblAccountName.setForeground(new java.awt.Color(51, 51, 255));
        lblAccountName.setFont(new java.awt.Font("MS Sans Serif", 1, 12));
        lblAccountName.setMinimumSize(new java.awt.Dimension(150, 21));
        lblAccountName.setName("lblCustNameValue"); // NOI18N
        lblAccountName.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 11, 2, 2);
        panAccountDetails.add(lblAccountName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTallyTableTotalAmount.add(panAccountDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panSalaryRecoveryList.add(panTallyTableTotalAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRecoveryTallyList.add(panSalaryRecoveryList, gridBagConstraints);

        tabRecoveryListTally.addTab("Import/Tally Recovery List", panRecoveryTallyList);

        panProcessRecoveryList.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panProcessRecoveryList.setMaximumSize(new java.awt.Dimension(800, 650));
        panProcessRecoveryList.setMinimumSize(new java.awt.Dimension(800, 650));
        panProcessRecoveryList.setPreferredSize(new java.awt.Dimension(800, 650));
        panProcessRecoveryList.setLayout(new java.awt.GridBagLayout());

        panImportSalaryRecoveryList1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panImportSalaryRecoveryList1.setMinimumSize(new java.awt.Dimension(830, 35));
        panImportSalaryRecoveryList1.setPreferredSize(new java.awt.Dimension(830, 35));
        panImportSalaryRecoveryList1.setLayout(new java.awt.GridBagLayout());

        lblRecoveredListDateTally.setText("Recovered List Date : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 50, 2, 2);
        panImportSalaryRecoveryList1.add(lblRecoveredListDateTally, gridBagConstraints);

        lblRecoveredListDateTallyVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        lblRecoveredListDateTallyVal.setMinimumSize(new java.awt.Dimension(100, 21));
        lblRecoveredListDateTallyVal.setName("lblCustNameValue"); // NOI18N
        lblRecoveredListDateTallyVal.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panImportSalaryRecoveryList1.add(lblRecoveredListDateTallyVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcessRecoveryList.add(panImportSalaryRecoveryList1, gridBagConstraints);

        panMemberReceiptTrans.setMinimumSize(new java.awt.Dimension(850, 225));
        panMemberReceiptTrans.setPreferredSize(new java.awt.Dimension(850, 225));
        panMemberReceiptTrans.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcessRecoveryList.add(panMemberReceiptTrans, gridBagConstraints);

        btnProcess.setForeground(new java.awt.Color(51, 102, 0));
        btnProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnProcess.setText("PROCESS");
        btnProcess.setToolTipText("Start Process");
        btnProcess.setFont(new java.awt.Font("MS Sans Serif", 1, 12));
        btnProcess.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnProcessMousePressed(evt);
            }
        });
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(12, 4, 4, 4);
        panProcessRecoveryList.add(btnProcess, gridBagConstraints);

        tabRecoveryListTally.addTab("Process Recovery List", panProcessRecoveryList);

        getContentPane().add(tabRecoveryListTally, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
    // TODO add your handling code here:
    if (tblSalaryRecoveryList.getRowCount() > 0) {
        int transactionSize = 0;
        if (transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(txtTotalRecoveredAmount.getText()).doubleValue() > 0) {
            ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
            return;
        } else {
            if (CommonUtil.convertObjToDouble(txtTotalRecoveredAmount.getText()).doubleValue() > 0) {
                transactionSize = (transactionUI.getOutputTO()).size();
                if (transactionSize != 1 && CommonUtil.convertObjToDouble(txtTotalRecoveredAmount.getText()).doubleValue() > 0) {
                    ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction");
                    return;
                } else {
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                }
            } else if (transactionUI.getOutputTO().size() > 0) {
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
        }
        if (transactionSize == 0 && CommonUtil.convertObjToDouble(txtTotalRecoveredAmount.getText()).doubleValue() > 0) {
            ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
            return;
        } else if (transactionSize != 0) {
            if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                return;
            }
            if (transactionUI.getOutputTO().size() > 0) {
                try {
                    HashMap whereMap = new HashMap();
                    whereMap.put("EMP_TYPE", observable.getEmp_type());
                    whereMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(observable.getTdtCalcIntUpto())));
                    List tallyVerifyList = null;

                    tallyVerifyList = ClientUtil.executeQuery("checkingTallyVerified", whereMap);

                    if (tallyVerifyList != null && tallyVerifyList.size() > 0) {
//                            String clock_No ="";

                        TableDialogUI tableData = new TableDialogUI("checkingTallyVerified", whereMap);
                        tableData.setTitle("Clock_No are not Verified... !!!");
                        tableData.show();
                    } else {
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                        //Progress bar added by shihad on mantis 10389 on 22.02.2015
                        CommonUtil comm = new CommonUtil();
                        final JDialog loading = comm.addProgressBar();
                        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                            @Override
                            protected Void doInBackground() throws InterruptedException /**
                             * Execute some operation
                             */
                            {
                                observable.doAction(false);
                                return null;
                            }

                            @Override
                            protected void done() {
                                loading.dispose();
                            }
                        };
                        worker.execute();
                        loading.show();
                        try {
                            worker.get();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        } //Progress bar code ends here                       	
                        finalMap = observable.getProxyReturnMap();
                        observable.setFinalMap(finalMap);
                        observable.doActionPerform();
                        if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0 && observable.getProxyReturnMap().containsKey("STATUS")) {
                            HashMap proxyResultMap = observable.getProxyReturnMap();
                            lblStatus.setText(CommonUtil.convertObjToStr(proxyResultMap.get("STATUS")));
                            clearScreen();
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    } else {
        ClientUtil.showMessageWindow("No Record In Recovery List Table !!!");
    }
}//GEN-LAST:event_btnProcessActionPerformed

private void btnProcessMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProcessMousePressed
    // TODO add your handling code here:
    lblStatus.setText("RUNNING...");
}//GEN-LAST:event_btnProcessMousePressed

private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
}//GEN-LAST:event_cboProdIdActionPerformed

private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
    // TODO add your handling code here:
    if (cboProdType.getSelectedIndex() > 0) {        // TODO add your handling code here:
        String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
        observable.setCbmProdId(prodType);
        cboProdId.setModel(observable.getCbmProdId());
        if (prodType.equals("GL")) {
            cboProdId.setEnabled(false);
            txtAccountNo.setText("");
            lblAccountNo.setText("Account Head");
            btnAccountNo.setEnabled(true);
        } else {
            cboProdId.setEnabled(true);
            lblAccountNo.setText("Account No");
            txtAccountNo.setText("");
            btnAccountNo.setEnabled(true);
            txtAccountNo.setEnabled(false);
        }
        if (!prodType.equals("GL")) {
            cboProdId.setModel(observable.getCbmProdId());
        }
    }
}//GEN-LAST:event_cboProdTypeActionPerformed

private void btnAccountNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNoActionPerformed
    // TODO add your handling code here:
    callView("CREDIT_ACC_NO");
}//GEN-LAST:event_btnAccountNoActionPerformed

private void btnAcceptChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcceptChangesActionPerformed
    // TODO add your handling code here:
    if (tblRecoveryListTally.getRowCount() > 0) {
        double recoveredAmount = CommonUtil.convertObjToDouble(tblSalaryRecoveryList.getValueAt(selectedRow, 4)).doubleValue();
        double totalRecoveredAmt = CommonUtil.convertObjToDouble(txtTotalRecoveredTallyAmt.getText()).doubleValue();
        if (recoveredAmount != CommonUtil.convertObjToDouble(txtTotalRecoveredTallyAmt.getText()).doubleValue()) {
            String message = "";
            if (recoveredAmount > totalRecoveredAmt) {
                message = "Short Amount Rs. " + df.format(recoveredAmount - totalRecoveredAmt);
            } else {
                message = "Excess Amount Rs. " + df.format(totalRecoveredAmt - recoveredAmount);
            }
            ClientUtil.showMessageWindow("Amount Not Tallied !!!" + "\n" + message);
            return;
        } else {
            ArrayList recoveryList = new ArrayList();
            ArrayList singleRecoveryList = new ArrayList();
            String empRefNo = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(selectedRow, 1));
            recoveryList = (ArrayList) finalMap.get(empRefNo);
            HashMap whereMap = new HashMap();
            HashMap singleRecoveryMap = new HashMap();
            double total_Demand = 0.0;
            for (int i = 0; i < tblRecoveryListTally.getRowCount(); i++) {
                singleRecoveryMap = new HashMap();
                HashMap updateMap = new HashMap();
                whereMap = (HashMap) recoveryList.get(i);
                singleRecoveryMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 0)));
                singleRecoveryMap.put("ACT_NUM", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 1)));
                singleRecoveryMap.put("PRINCIPAL", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 2)));
                singleRecoveryMap.put("INTEREST", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 3)));
                singleRecoveryMap.put("PENAL", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 4)));
                singleRecoveryMap.put("CHARGES", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 5)));
                singleRecoveryMap.put("TOTAL_DEMAND", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 6)));
                singleRecoveryMap.put("RECOVERED_AMOUNT", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 7)));
                singleRecoveryMap.put("UNRECOVERY", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 8)));
                if (whereMap.get("ACT_NUM").equals(singleRecoveryMap.get("ACT_NUM"))) {
                    singleRecoveryMap.put("PROD_TYPE", whereMap.get("PROD_TYPE"));
                    singleRecoveryMap.put("PROD_ID", whereMap.get("PROD_ID"));
                }
               	String accNo =  CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 1));
                double recoveredAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 7))).doubleValue();
                double unrec = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 8))).doubleValue();
                double chargeAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 5))).doubleValue();
                double penalAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 4))).doubleValue();
                double intAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 3))).doubleValue();
                double princ = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 2))).doubleValue();
                double recPenalAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 9))).doubleValue();
                System.out.println("recoveredAmt"+recoveredAmt+"unrec"+unrec+"princ"+princ+"intAmt"+intAmt);
                System.out.println("####### whereMapt map : " + whereMap);
                String parAcNo =  CommonUtil.convertObjToStr(whereMap.get("PARTICULARS"));
                if (whereMap.containsKey("PROD_ID") && whereMap.get("PROD_ID") != null && !whereMap.get("PROD_ID").equals("") && !whereMap.get("PROD_TYPE").equals("OA")
                        && !whereMap.get("PROD_TYPE").equals("MDS") && !whereMap.get("PROD_TYPE").equals("SA") && !whereMap.get("PROD_TYPE").equals("TD")) {
                    System.out.println("goin inside if");
                    updateMap.put("REC_PRINCIPAL", 0);
                    updateMap.put("REC_PENAL", 0);
                    updateMap.put("REC_INTEREST", 0);
                    updateMap.put("REC_CHARGES", 0);
                    singleRecoveryMap.put("REC_PRINCIPAL", 0);
                    singleRecoveryMap.put("REC_PENAL", 0);
                    singleRecoveryMap.put("REC_INTEREST", 0);
                    singleRecoveryMap.put("REC_CHARGES", 0);
                    if (recoveredAmt > 0) {
                        if (princ > unrec) {
                            princ = CommonUtil.convertObjToDouble((double) Math.round((princ - unrec) * 100) / 100);
                            recoveredAmt = CommonUtil.convertObjToDouble((double) Math.round((recoveredAmt - princ) * 100) / 100);
                            updateMap.put("REC_PRINCIPAL", princ);
                            singleRecoveryMap.put("REC_PRINCIPAL", princ);
                            unrec = 0;
                        } else {
                            unrec = CommonUtil.convertObjToDouble((double) Math.round((unrec - princ) * 100) / 100);
                            recoveredAmt = CommonUtil.convertObjToDouble((double) Math.round((recoveredAmt - 0) * 100) / 100);
                            updateMap.put("REC_PRINCIPAL", new Double(0.0));
                            singleRecoveryMap.put("REC_PRINCIPAL", "0");
                        }
                    }
                    if (recoveredAmt > 0) {
                        if (intAmt > unrec) {
                            intAmt = CommonUtil.convertObjToDouble((double) Math.round((intAmt - unrec) * 100) / 100);
                            recoveredAmt = CommonUtil.convertObjToDouble((double) Math.round((recoveredAmt - intAmt) * 100) / 100);
                            updateMap.put("REC_INTEREST", intAmt);
                            singleRecoveryMap.put("REC_INTEREST", intAmt);
                            unrec = 0;
                        } else {
                            unrec = CommonUtil.convertObjToDouble((double) Math.round((unrec - intAmt) * 100) / 100);
                            recoveredAmt = CommonUtil.convertObjToDouble((double) Math.round((recoveredAmt - 0) * 100) / 100);
                            updateMap.put("REC_INTEREST", new Double(0.0));
                            singleRecoveryMap.put("REC_INTEREST", "0");
                        }
                    }
                    if (recoveredAmt > 0) {
                        if (penalAmt > unrec) {
                            penalAmt = CommonUtil.convertObjToDouble((double) Math.round((penalAmt - unrec) * 100) / 100);
                            recoveredAmt = CommonUtil.convertObjToDouble((double) Math.round((recoveredAmt - penalAmt) * 100) / 100);
                            updateMap.put("REC_PENAL", penalAmt);
                            singleRecoveryMap.put("REC_PENAL", penalAmt);
                            unrec = 0;
                        } else {
                            unrec = CommonUtil.convertObjToDouble((double) Math.round((unrec - penalAmt) * 100) / 100);
                            recoveredAmt = CommonUtil.convertObjToDouble((double) Math.round((recoveredAmt - 0) * 100) / 100);
                            updateMap.put("REC_PENAL", new Double(0.0));
                            singleRecoveryMap.put("REC_PENAL", "0");
                        }
                    }
                    if (recoveredAmt > 0) {
                        if (chargeAmt > unrec) {
                            chargeAmt = CommonUtil.convertObjToDouble((double) Math.round((chargeAmt - unrec) * 100) / 100);
                            recoveredAmt = CommonUtil.convertObjToDouble((double) Math.round((recoveredAmt - chargeAmt) * 100) / 100);
                            updateMap.put("REC_CHARGES", chargeAmt);
                            singleRecoveryMap.put("REC_CHARGES", chargeAmt);
                            unrec = 0;
                        } else {
                            unrec = CommonUtil.convertObjToDouble((double) Math.round((unrec - chargeAmt) * 100) / 100);
                            recoveredAmt = CommonUtil.convertObjToDouble((double) Math.round((recoveredAmt - 0) * 100) / 100);
                            updateMap.put("REC_CHARGES", new Double(0.0));
                            singleRecoveryMap.put("REC_CHARGES", "0");
                        }
                    }
                    singleRecoveryList.add(singleRecoveryMap);
                    updateMap.put("TALLY_VERIFIED", "Y");
                    updateMap.put("ACT_NUM", singleRecoveryMap.get("ACT_NUM"));
                    updateMap.put("EMP_REF_NO", empRefNo);
                    updateMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(observable.getTdtCalcIntUpto())));
                    ClientUtil.execute("updateTallyRecoveredDetails", updateMap);
                } else {
                    String prodType = CommonUtil.convertObjToStr(whereMap.get("PROD_TYPE"));
                    double recoveredPrin = recoveredAmt - recPenalAmt;
                    updateMap.put("REC_PRINCIPAL", recoveredPrin);
                    updateMap.put("REC_PENAL", recPenalAmt);
                    singleRecoveryMap.put("REC_PRINCIPAL", String.valueOf(recoveredPrin));
                    singleRecoveryMap.put("REC_PENAL", String.valueOf(recPenalAmt));
                    updateMap.put("TALLY_VERIFIED", "Y");
                    updateMap.put("ACT_NUM", singleRecoveryMap.get("ACT_NUM"));
                    updateMap.put("EMP_REF_NO", empRefNo);
                    updateMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(observable.getTdtCalcIntUpto())));
                    updateMap.put("REC_INTEREST", new Double(0.0));
                    updateMap.put("REC_CHARGES", new Double(0.0));
                    updateMap.put("TOTAL_DEMAND", String.valueOf(recoveredAmt));
                    singleRecoveryMap.put("TALLY_VERIFIED", "Y");
                    singleRecoveryMap.put("ACT_NUM", singleRecoveryMap.get("ACT_NUM"));
                    singleRecoveryMap.put("EMP_REF_NO", empRefNo);
                    singleRecoveryMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(observable.getTdtCalcIntUpto())));
                    singleRecoveryMap.put("REC_INTEREST", String.valueOf("0"));
                    singleRecoveryMap.put("REC_CHARGES", String.valueOf("0"));
                    singleRecoveryMap.put("TOTAL_DEMAND", String.valueOf(recoveredAmt));
                    singleRecoveryList.add(singleRecoveryMap);
                 
                    if (prodType != null && !prodType.equals("") && prodType.equals("SA")) {
                        HashMap numberMap = new HashMap();
                        numberMap.put("ACCT_NUM", singleRecoveryMap.get("ACT_NUM"));
                        List suspenseAcctList = ClientUtil.executeQuery("getSuspenseInstallmentPresent", numberMap);
                        if (suspenseAcctList != null && suspenseAcctList.size() > 0) {
                            ClientUtil.execute("updateNewTallyRecoveredDetails", updateMap);
                        } else {
                            updateMap.put("PARTICULARS",parAcNo);
                            ClientUtil.execute("updateNewTallyRecoveredDetailsSA", updateMap);
                        }
                    } else {
                        ClientUtil.execute("updateNewTallyRecoveredDetails", updateMap);
                    }
                }
            }
            finalMap.put(empRefNo, singleRecoveryList);
            btnClearActionPerformed(null);
            btnAcceptChanges.setEnabled(false);
        }
    }
}//GEN-LAST:event_btnAcceptChangesActionPerformed

private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
    // TODO add your handling code here:
    clear = true;
    acceptChanges = true;
    clearAccDetails();
    txtTotalDemandTally.setText("");
    txtTotalRecoveredTallyAmt.setText("");
    initTallyTableData();
    setSizeTallyTableData();
    enableDisBtn(false);
    btnAcceptChanges.setEnabled(false);
    clrList = new ArrayList();
}//GEN-LAST:event_btnClearActionPerformed

private void btnSchedDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchedDeleteActionPerformed
    // TODO add your handling code here:
    if (tblRecoveryListTally.getRowCount() > 0) {
        String actNo = CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(tblRecoveryListTally.getSelectedRow(), 1));
        HashMap whereMap = new HashMap();
        ArrayList recoveryList = new ArrayList();
        disableRowList = new ArrayList();
        String emp_Ref_No = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(tblSalaryRecoveryList.getSelectedRow(), 1));
        finalMap = (HashMap) observable.getProxyReturnMap();
        recoveryList = (ArrayList) finalMap.get(emp_Ref_No);
        whereMap = new HashMap();
        double total_Demand = 0.0;
        double total_RecoveredAmt = 0.0;
        for (int i = 0; i < recoveryList.size(); i++) {
            whereMap = (HashMap) recoveryList.get(i);
            String act_Num = CommonUtil.convertObjToStr(whereMap.get("ACT_NUM"));
            if (actNo.equals(act_Num)) {
                recoveryList.remove(i);
            }
        }
        finalMap.put(emp_Ref_No, recoveryList);
        //Update Delete Status
        HashMap updateMap = new HashMap();
        updateMap.put("ACT_NUM", actNo);
        updateMap.put("EMP_REF_NO", emp_Ref_No);
        updateMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(observable.getTdtCalcIntUpto())));
        ClientUtil.execute("updateTallyRecoveryStatus", updateMap);
        acceptChanges = true;
        tblSalaryRecoveryListMouseClicked(null);
        clearAccDetails();
        enableDisBtn(false);
        acceptChanges = true;
        btnSchedNew.setEnabled(true);
    }
}//GEN-LAST:event_btnSchedDeleteActionPerformed

private void btnSchedSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchedSaveActionPerformed
    // TODO add your handling code here:
    if (txtAccountNo.getText().length() > 0 && cboProdType.getSelectedIndex() > 0) {
        ArrayList recoveryList = new ArrayList();
        ArrayList singleRecoveryList = new ArrayList();
        String empRefNo = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(tblSalaryRecoveryList.getSelectedRow(), 1));
        recoveryList = (ArrayList) finalMap.get(empRefNo);
        HashMap whereMap = new HashMap();
        HashMap singleRecoveryMap = new HashMap();
        double total_Demand = 0.0;
        for (int i = 0; i < tblRecoveryListTally.getRowCount(); i++) {
            singleRecoveryMap = new HashMap();
            whereMap = (HashMap) recoveryList.get(i);
            singleRecoveryMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 0)));
            singleRecoveryMap.put("ACT_NUM", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 1)));
            singleRecoveryMap.put("PRINCIPAL", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 2)));
            singleRecoveryMap.put("INTEREST", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 3)));
            singleRecoveryMap.put("PENAL", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 4)));
            singleRecoveryMap.put("CHARGES", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 5)));
            singleRecoveryMap.put("TOTAL_DEMAND", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 6)));
            singleRecoveryMap.put("RECOVERED_AMOUNT", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 7)));
            if (whereMap.get("ACT_NUM").equals(singleRecoveryMap.get("ACT_NUM"))) {
                singleRecoveryMap.put("PROD_TYPE", whereMap.get("PROD_TYPE"));
                singleRecoveryMap.put("PROD_ID", whereMap.get("PROD_ID"));
            }
            singleRecoveryList.add(singleRecoveryMap);
        }
        String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
        singleRecoveryMap = new HashMap();
        singleRecoveryMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(cboProdId.getSelectedItem()));
        singleRecoveryMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtAccountNo.getText()));
        singleRecoveryMap.put("PRINCIPAL", "");
        singleRecoveryMap.put("INTEREST", "");
        singleRecoveryMap.put("PENAL", "");
        singleRecoveryMap.put("CHARGES", "");
        singleRecoveryMap.put("TOTAL_DEMAND", "");
        singleRecoveryMap.put("RECOVERED_AMOUNT", "");
        singleRecoveryMap.put("PROD_TYPE", prodType);
        singleRecoveryMap.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        singleRecoveryList.add(singleRecoveryMap);
        finalMap.put(empRefNo, singleRecoveryList);
        insertData();
        acceptChanges = true;
        tblSalaryRecoveryListMouseClicked(null);
        clearAccDetails();
        enableDisBtn(false);
        acceptChanges = true;
        btnSchedNew.setEnabled(true);
    } else {
        ClientUtil.showMessageWindow("Please Enter Product Details... !!! ");
    }
}//GEN-LAST:event_btnSchedSaveActionPerformed

private void btnSchedNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchedNewActionPerformed
    // TODO add your handling code here:
    panAccountDetails.setVisible(true);
    cboProdType.setEnabled(true);
    cboProdId.setEnabled(true);
    enableDisBtn(false);
    btnSchedSave.setEnabled(true);
}//GEN-LAST:event_btnSchedNewActionPerformed

private void srpTableRecoveryListTallyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_srpTableRecoveryListTallyMouseClicked
    // TODO add your handling code here:
}//GEN-LAST:event_srpTableRecoveryListTallyMouseClicked

private void tblRecoveryListTallyPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblRecoveryListTallyPropertyChange
    // TODO add your handling code here:
}//GEN-LAST:event_tblRecoveryListTallyPropertyChange

private void tblRecoveryListTallyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRecoveryListTallyMouseClicked
    // TODO add your handling code here:
    System.out.println("coming inside recovery talley action mouse");
    enableDisBtn(true);
    btnSchedSave.setEnabled(false);
    String act_num = (String) tblRecoveryListTally.getValueAt(tblRecoveryListTally.getSelectedRow(), 1);
    String empRefNo = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(tblSalaryRecoveryList.getSelectedRow(), 1));
    ArrayList recoveryList = (ArrayList) finalMap.get(empRefNo);
    HashMap whereMap = new HashMap();
    whereMap = (HashMap) recoveryList.get(0);
    HashMap hMap = new HashMap();
    if (act_num.indexOf("_") != -1) {
        act_num = act_num.substring(0, act_num.indexOf("_"));
    }
    hMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(whereMap.get("INT_CALC_UPTO_DT")))));
    hMap.put("ACT_NUM", act_num);
    hMap.put("CURR_DT",currDate.clone());
    List transList = ClientUtil.executeQuery("getSalaryRecoveryListTallyTransDetails", hMap);
    System.out.println("transList"+transList);
    HashMap hmap = new HashMap();
    hmap.put("ACT_NUM", act_num);
    List aList = ClientUtil.executeQuery("getActstatus", hmap);
    System.out.println("aList"+aList);
    if (aList.size() > 0 || transList.size()>0) {
        HashMap statusMap = (HashMap) aList.get(0);
        String status = statusMap.get("ACCT_STATUS").toString();
        sus_amt = CommonUtil.convertObjToDouble(tblRecoveryListTally.getValueAt(tblRecoveryListTally.getSelectedRow(), 7));
        if ((status.equals("CLOSED") || transList.size()>0) && sus_amt > 0.0) {
            ClientUtil.showMessageWindow("This Account is Closed or Transactions has been Made!!!");
            tblRecoveryListTally.setValueAt("0.0", tblRecoveryListTally.getSelectedRow(), 7);
            int confirm = COptionPane.showConfirmDialog(null, "Do you want to transfer this Amount to Suspense ?", "Message", 0);
            if (confirm == 0) {
                inserttblData();
            }

        }
    }

}//GEN-LAST:event_tblRecoveryListTallyMouseClicked

private void tblSalaryRecoveryListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSalaryRecoveryListMouseClicked
    if (tblSalaryRecoveryList.getRowCount() > 0) {
        if (acceptChanges) {
            if (recverdList != null && recverdList.containsKey(tblSalaryRecoveryList.getSelectedRow())) {
                ArrayList each = (ArrayList) recverdList.get(tblSalaryRecoveryList.getSelectedRow());
                if (each != null && each.size() > 0) {
                    unrecCheck = CommonUtil.convertObjToStr(each.get(1));
                }
            }
            if (unrecCheck != null && unrecCheck.equalsIgnoreCase("Y")) {
            } else {
            }
            clear = false;
            acceptChanges = false;
            initTallyTableData();
            tallyCheckingAmount();
            enableDisBtn(false);
            btnSchedNew.setEnabled(true);
            selectedRow = tblSalaryRecoveryList.getSelectedRow();
            String empRefNo = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(tblSalaryRecoveryList.getSelectedRow(), 1));
            ArrayList recoveryList = (ArrayList) finalMap.get(empRefNo);
            HashMap whereMap = new HashMap();
            for (int j = 0; j < recoveryList.size(); j++) {
                whereMap = (HashMap) recoveryList.get(j);
                System.out.println("whereMapwhereMap" + whereMap);
                HashMap hmap = new HashMap();
                String act_num = CommonUtil.convertObjToStr(whereMap.get("ACT_NUM"));
                hmap.put("ACT_NUM", act_num);
                HashMap hMap = new HashMap();
                if (act_num.indexOf("_") != -1) {
                    act_num = act_num.substring(0, act_num.indexOf("_"));
                }
                Date recDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lblRecoveredListDateVal.getText()));
                hMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDate, recDate));
                hMap.put("ACT_NUM", act_num);
                hMap.put("CURR_DT", currDate.clone());
                List transList = ClientUtil.executeQuery("getSalaryRecoveryListTallyTransDetails", hMap);
                if (transList != null && transList.size() > 0) {
                    clrList.add(String.valueOf(j));
                    setColourTalleyTransaction();
                }
                List aList = ClientUtil.executeQuery("getActstatus", hmap);
                if (aList != null && aList.size() > 0) {
                    HashMap statusMap = (HashMap) aList.get(0);
                    String status = statusMap.get("ACCT_STATUS").toString();
                    if (status.equals("CLOSED")) {
                        clrList.add(String.valueOf(j));
                        setColourTalleyClosed();
                    }
                }
            }
            
        } else {
            if (btnAcceptChanges.isEnabled()) {
                ClientUtil.showMessageWindow("Please Click Accept Changes Button !!!");
            } else {
                ClientUtil.showMessageWindow("Please Click Clear Button !!!");
            }
        }
        btnAcceptChanges.setEnabled(true);
    }
}//GEN-LAST:event_tblSalaryRecoveryListMouseClicked

private void cboEmp_typeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboEmp_typeActionPerformed
    // TODO add your handling code here:
    int sel = cboEmp_type.getSelectedIndex() - 1;
    observable.setEmp_type(String.valueOf(sel));
}//GEN-LAST:event_cboEmp_typeActionPerformed

private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
    // TODO add your handling code here:
    if (cboEmp_type.getSelectedIndex() > 0) {
        try {
            javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
            int result = fc.showOpenDialog(null);
            if (result == fc.APPROVE_OPTION) {
                java.io.File selectedFile = fc.getSelectedFile();
                String name = selectedFile.getName();
                if (name.substring(name.indexOf(".") + 1, name.length()).equals("xls")) {
                    java.io.FileInputStream inpuStream = new java.io.FileInputStream(selectedFile);
                    HSSFWorkbook workbook = new HSSFWorkbook(inpuStream);//new FileInputStream(fileToBeRead));
                    HSSFSheet sheet = workbook.getSheetAt(0);
                    int rows = sheet.getLastRowNum();//sheet.getPhysicalNumberOfRows();
//                    System.out.println("last row number ##"+rows);
                    newMode = true;
                    observable.insertTableData(newMode);
                    tblSalaryRecoveryList.setModel(observable.getTblSalaryRecoveryList());
                    setSizeTableData();
                    lblRecoveredListDateVal.setText(observable.getTdtCalcIntUpto());
                    lblRecoveredListDateTallyVal.setText(observable.getTdtCalcIntUpto());
                    int j = 0;
                    double amt = 0.0, reqamt;

                    for (int i = 1; i < rows; i++) {
                        HSSFRow row = sheet.getRow(i);
                        String empNo = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(j, 1)); 
                        amt = 0.0;
                        reqamt = 0.0;
                        if (row.getCell((short) 4).getCellType() == HSSFCell.CELL_TYPE_BLANK || row.getCell((short) 4).getCellType() == HSSFCell.CELL_TYPE_ERROR) {
                            reqamt = CommonUtil.convertObjToDouble(tblSalaryRecoveryList.getValueAt(j, 3));
                            tblSalaryRecoveryList.setValueAt(String.valueOf(df.format(reqamt)), j++, 4);
                        } else if (row.getCell((short) 4).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                            if (String.valueOf(row.getCell((short) 4).getNumericCellValue()) != null) {
                                amt = Double.parseDouble(String.valueOf(row.getCell((short) 4).getNumericCellValue()));
                            } else {
                                amt = 0.0;
                            }
                            reqamt = CommonUtil.convertObjToDouble(tblSalaryRecoveryList.getValueAt(j, 3));
                            tblSalaryRecoveryList.setValueAt(String.valueOf(df.format(reqamt - amt)), j++, 4);
                        } else if (row.getCell((short) 4).getCellType() == HSSFCell.CELL_TYPE_STRING) {
                            if (String.valueOf(row.getCell((short) 4).getStringCellValue()) != null) {
                                amt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(row.getCell((short) 4).getStringCellValue()));
                            } else {
                                amt = 0.0;
                            }
                            reqamt = CommonUtil.convertObjToDouble(tblSalaryRecoveryList.getValueAt(j, 3));
                            tblSalaryRecoveryList.setValueAt(String.valueOf(df.format(reqamt - amt)), j++, 4);
                        }else{
                            reqamt = CommonUtil.convertObjToDouble(tblSalaryRecoveryList.getValueAt(j, 3));
                            tblSalaryRecoveryList.setValueAt(String.valueOf(df.format(reqamt)), j++, 4);
                        }
                    }
               
                      CommonUtil comm = new CommonUtil();
            final JDialog loading = comm.addProgressBar();
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws InterruptedException /**
                 * Execute some operation
                 */
                {
                    calcRecoveryListTotal();
                    TallyCheck();
                    setColour();
                    btnBrowse.setEnabled(false);
                    observable.doAction(newMode);
                    finalMap = (HashMap) observable.getProxyReturnMap();
                    automateTallyverify();
                    setColour();
                    return null;
                }

                @Override
                protected void done() {
                    loading.dispose();
                }
            };
            worker.execute();
            loading.show();
            try {
                worker.get();
            } catch (Exception e1) {
                e1.printStackTrace();
            } //Pro
                    transactionUI.cancelAction(false);
                    transactionUI.setButtonEnableDisable(true);
                    transactionUI.resetObjects();
                    transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
                    transactionUI.setCallingAmount(txtTotalRecoveredAmount.getText());
                    inpuStream.close();
                    lblStatus.setText("                      ");
                    btnProcess.setEnabled(true);
                    btnEdit.setEnabled(false);
                } else {
                    ClientUtil.displayAlert("Please Select file name should be in Excel format");
                    return;
                }
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } else {
        ClientUtil.displayAlert("Please Select Staff type");
    }
}//GEN-LAST:event_btnBrowseActionPerformed

private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
    // TODO add your handling code here:
    if (tdtRecoveredDate.getDateValue().length() > 0) {
        observable.setTdtCalcIntUpto(tdtRecoveredDate.getDateValue());
        newMode = false;
         observable.insertTableData(newMode);
        tblSalaryRecoveryList.setModel(observable.getTblSalaryRecoveryList());
        if (tblSalaryRecoveryList.getRowCount() > 0) {
            setSizeTableData();
            lblRecoveredListDateVal.setText(observable.getTdtCalcIntUpto());
            lblRecoveredListDateTallyVal.setText(observable.getTdtCalcIntUpto());
            //Progress bar added by shihad on mantis 10389 on 25.02.2015

            calcRecoveryListTotal();
            TallyCheck();
            setColour();
            btnBrowse.setEnabled(false);

            CommonUtil comm = new CommonUtil();
            final JDialog loading = comm.addProgressBar();
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws InterruptedException /**
                 * Execute some operation
                 */
                {
                    observable.doAction(newMode);
                    return null;
                }

                @Override
                protected void done() {
                    loading.dispose();
                }
            };
            worker.execute();
            loading.show();
            try {
                worker.get();
            } catch (Exception e1) {
                e1.printStackTrace();
            } //Progress bar code ends here   
            finalMap = (HashMap) observable.getProxyReturnMap();
            automateTallyverify();
            setColour();
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
            transactionUI.setCallingAmount(txtTotalRecoveredAmount.getText());
            btnOk.setEnabled(false);
            lblStatus.setText("EDIT");
            btnProcess.setEnabled(true);
            btnBrowse.setEnabled(false);
        } else {
            ClientUtil.showMessageWindow("No Data in this Date !!! ");
        }
    } else {
        ClientUtil.showMessageWindow("Please Enter Recovered Date... !!! ");
    }
}//GEN-LAST:event_btnOkActionPerformed

private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    // TODO add your handling code here:
    btnEdit.setEnabled(false);
    tdtRecoveredDate.setEnabled(true);
    panBrowseDetails.setVisible(false);
    panRecoveryEditDetails.setVisible(true);
}//GEN-LAST:event_btnEditActionPerformed

private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
    // TODO add your handling code here:
    newMode = true;
    clearScreen();
    lblStatus.setText("                      ");
    btnOk.setEnabled(true);
    btnEdit.setEnabled(true);
    panBrowseDetails.setVisible(true);
    cboEmp_type.setEnabled(true);
    cboEmp_type.setSelectedIndex(0);
    panRecoveryEditDetails.setVisible(false);
}//GEN-LAST:event_btnCancelActionPerformed

private void txtEmployeeNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmployeeNoActionPerformed
    // TODO add your handling code here:

    String clockNo = txtEmployeeNo.getText();
    if (clockNo != null && !clockNo.equalsIgnoreCase("")) {
        HashMap dMap = new HashMap();

        dMap.put("CLOCK_NO", clockNo);
        List datList = ClientUtil.executeQuery("TransAll.getSelectClockListFocus", dMap);
        System.out.println("datList ============================" + datList);
        if (datList != null && datList.size() > 0) {
            for (int i = 0; i < datList.size(); i++) {
                HashMap map = (HashMap) datList.get(i);
                String CName = "", custName = "", custId = "", memNo = "";
                if (map.get("CLOCK_NO") != null) {
                    CName = map.get("CLOCK_NO").toString();
                }
                if (map.get("MEMBER_NO") != null) {
                    custId = map.get("MEMBER_NO").toString();
                }
                if (map.get("NAME") != null) {
                    CName = map.get("NAME").toString();
                }
                if (map.get("MEM_NO") != null) {
                    memNo = map.get("MEM_NO").toString();
                }

            }
        } else {
        }
    } else {
    }
}//GEN-LAST:event_txtEmployeeNoActionPerformed

private void txtEmployeeNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmployeeNoFocusLost
    // TODO add your handling code here:
}//GEN-LAST:event_txtEmployeeNoFocusLost

private void btnEmployeeNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmployeeNoActionPerformed
    callView("CLOCK_NO");


}//GEN-LAST:event_btnEmployeeNoActionPerformed

private void tdtDtOfRecoveryFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDtOfRecoveryFocusLost
    // TODO add your handling code here:
    // ClientUtil.validateLTDate(tdtDtOfRecovery);
}//GEN-LAST:event_tdtDtOfRecoveryFocusLost

private void btnUnrecoveryProcessMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUnrecoveryProcessMousePressed
// TODO add your handling code here:
}//GEN-LAST:event_btnUnrecoveryProcessMousePressed

private void btnUnrecoveryProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnrecoveryProcessActionPerformed
    if (tdtDtOfRecovery.getDateValue().equals("")) {
        ClientUtil.showAlertWindow("Please enter Recovery Date");
    } else {
        initTallyUnrecoveryTableData();
        HashMap viewMap = new HashMap();
        ArrayList tblList = new ArrayList();
        viewMap.put("RECOVERY_DT", CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(tdtDtOfRecovery.getDateValue())));
        viewMap.put("EMP_NO", txtEmployeeNo.getText());
        List viewList = ClientUtil.executeQuery("viewListDetailsForUnrecovery", viewMap);
        System.out.println("viewListviewListviewListviewListviewListviewList" + viewList);
        HashMap tradeMap = new HashMap();
        disableRowList1 = new ArrayList();
        prdTypeMap = new HashMap();
        prod_Map = new HashMap();
        tblproduct.getModel().removeTableModelListener(tableModelListener);
        double totdemAmt = 0;
        for (int j = 0; j < viewList.size();) {
            int i = 0;

            while (i < viewList.size()) {
                tradeMap = (HashMap) viewList.get(j);
                double totalDemand = CommonUtil.convertObjToDouble(tradeMap.get("PRINCIPAL"))+CommonUtil.convertObjToDouble(tradeMap.get("INTEREST"))
                        +CommonUtil.convertObjToDouble(tradeMap.get("PENAL"))+CommonUtil.convertObjToDouble(tradeMap.get("CHARGES"));
                totalDemand = (double) Math.round(totalDemand * 100)/100;
                tblproduct.getModel().setValueAt(tradeMap.get("SCHEME_NAME"), i, 0);
                tblproduct.getModel().setValueAt(tradeMap.get("ACT_NUM"), i, 1);
                tblproduct.getModel().setValueAt(tradeMap.get("PRINCIPAL"), i, 2);
                tblproduct.getModel().setValueAt(tradeMap.get("INTEREST"), i, 3);
                tblproduct.getModel().setValueAt(tradeMap.get("PENAL"), i, 4);
                tblproduct.getModel().setValueAt(tradeMap.get("CHARGES"), i, 5);
                tblproduct.getModel().setValueAt(tradeMap.get("INSTALLMENT"), i, 6);
                tblproduct.getModel().setValueAt(totalDemand, i, 7);
                tblproduct.getModel().setValueAt(totalDemand, i, 8);
                tblproduct.getModel().setValueAt(0, i, 9);
                tblproduct.getModel().setValueAt(tradeMap.get("PENAL"), i, 10);
                tblproduct.getModel().setValueAt(0, i, 11);
                System.out.println("p typeeeeeeee :" + tradeMap.get("PROD_TYPE"));
                if (CommonUtil.convertObjToStr(tradeMap.get("PROD_TYPE")).equals("TD")
                        || CommonUtil.convertObjToStr(tradeMap.get("PROD_TYPE")).equals("MDS")) {
                    disableRowList1.add(String.valueOf(i));
                    prdTypeMap.put(i, tradeMap.get("PROD_TYPE"));
                }
                ArrayList valList = new ArrayList();
                valList.add(tradeMap.get("PROD_TYPE"));
                valList.add(tradeMap.get("PROD_ID"));
                prod_Map.put(tradeMap.get("ACT_NUM"), valList);
                j++;
                i++;
                totdemAmt += CommonUtil.convertObjToDouble(totalDemand);
            }
        }
        totdemAmt = (double) Math.round(totdemAmt * 100)/100;
        txtTotDemAmt.setText(CommonUtil.convertObjToStr(totdemAmt));
    }
    tblproduct.setCellSelectionEnabled(true);
    setTableModelListener1();
}//GEN-LAST:event_btnUnrecoveryProcessActionPerformed

private void btnUnrecoverySaveMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUnrecoverySaveMousePressed
// TODO add your handling code here:
}//GEN-LAST:event_btnUnrecoverySaveMousePressed

private void btnUnrecoverySaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnrecoverySaveActionPerformed
    inserttblDataUnrecovery();  
}//GEN-LAST:event_btnUnrecoverySaveActionPerformed
    private void clearScreen() {
        observable.resetForm();
        initTableData();
        colourList = null;
        btnClearActionPerformed(null);
        ClientUtil.enableDisable(panRecoveryTallyList, false);
        ClientUtil.enableDisable(panProcessRecoveryList, false);
        finalMap = new HashMap();
        ClientUtil.clearAll(this);
        btnBrowse.setEnabled(true);
        btnProcess.setEnabled(false);
        transactionUI.resetObjects();
        transactionUI.cancelAction(false);
        transactionUI.setMainEnableDisable(false);
        transactionUI.setCallingAmount("");
        lblRecoveredListDateVal.setText("");
        transactionUI.setCallingApplicantName("");
        lblRecoveredListDateTallyVal.setText("");
    }

    private void inserttblData() {
        String empRefNo = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(tblSalaryRecoveryList.getSelectedRow(), 1));
        HashMap sMap = new HashMap();
        sMap.put("EMP_REFNO", empRefNo);
        List aList1 = ClientUtil.executeQuery("getSuspenseActNo", sMap);
        if (aList1.size() > 0) {
            HashMap actMap = new HashMap();
            actMap = (HashMap) aList1.get(0);
            String actno = actMap.get("SUSPENSE_ACCT_NUM").toString();
            String desc = actMap.get("SUSPENSE_PROD_DESC").toString();
            String pid = actMap.get("SUSPENSE_PROD_ID").toString();
            ArrayList recoveryList = new ArrayList();
            ArrayList singleRecoveryList = new ArrayList();
            recoveryList = (ArrayList) finalMap.get(empRefNo);
            HashMap whereMap = new HashMap();
            HashMap singleRecoveryMap = new HashMap();
            double total_Demand = 0.0;
            for (int i = 0; i < tblRecoveryListTally.getRowCount(); i++) {
                singleRecoveryMap = new HashMap();
                whereMap = (HashMap) recoveryList.get(i);
                singleRecoveryMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 0)));
                singleRecoveryMap.put("ACT_NUM", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 1)));
                singleRecoveryMap.put("PRINCIPAL", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 2)));
                singleRecoveryMap.put("INTEREST", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 3)));
                singleRecoveryMap.put("PENAL", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 4)));
                singleRecoveryMap.put("CHARGES", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 5)));
                singleRecoveryMap.put("INT_CALC_UPTO_DT", observable.getTdtCalcIntUpto());
                singleRecoveryMap.put("TOTAL_DEMAND", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 6)));
                singleRecoveryMap.put("RECOVERED_AMOUNT", CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(i, 7)));
                if (whereMap.get("ACT_NUM").equals(singleRecoveryMap.get("ACT_NUM"))) {
                    singleRecoveryMap.put("PROD_TYPE", whereMap.get("PROD_TYPE"));
                    singleRecoveryMap.put("PROD_ID", whereMap.get("PROD_ID"));
                    singleRecoveryMap.put("PARTICULARS",  CommonUtil.convertObjToStr(whereMap.get("PARTICULARS")));
                }
                singleRecoveryList.add(singleRecoveryMap);
            }
            singleRecoveryMap = new HashMap();
            singleRecoveryMap.put("SCHEME_NAME", desc);
            singleRecoveryMap.put("ACT_NUM", actno);
            singleRecoveryMap.put("PRINCIPAL", "");
            singleRecoveryMap.put("INTEREST", "");
            singleRecoveryMap.put("PENAL", "");
            singleRecoveryMap.put("CHARGES", "");
            singleRecoveryMap.put("TOTAL_DEMAND", "");
            singleRecoveryMap.put("INT_CALC_UPTO_DT", observable.getTdtCalcIntUpto());
            singleRecoveryMap.put("RECOVERED_AMOUNT", String.valueOf(sus_amt));
            singleRecoveryMap.put("PROD_TYPE", "SA");
            singleRecoveryMap.put("PROD_ID", pid);
            singleRecoveryMap.put("PARTICULARS",  CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(tblRecoveryListTally.getSelectedRow(), 1)));
            singleRecoveryList.add(singleRecoveryMap);
            finalMap.put(empRefNo, singleRecoveryList);
            insertData1(actno, pid, desc,CommonUtil.convertObjToStr(tblRecoveryListTally.getValueAt(tblRecoveryListTally.getSelectedRow(), 1)));
            acceptChanges = true;
            tblSalaryRecoveryListMouseClicked(null);
            clearAccDetails();
            enableDisBtn(false);
            acceptChanges = true;
            btnSchedNew.setEnabled(true);

        } else {
            ClientUtil.showMessageWindow("No corresponding Suspense Account for this Clockno !!!");
        }
    }

    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        ArrayList lst = new ArrayList();
        if (viewType.equals("CLOCK_NO")) {

            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "TransAll.getSelectClockList");
        }
        if (currField == "CREDIT_ACC_NO") {
            HashMap whereMap = new HashMap();
            viewMap = new HashMap();
            String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
            if (!prodType.equals("GL")) {
                HashMap inputMap = new HashMap();
                String empRefNo = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(selectedRow, 1));
                inputMap.put("EMP_REFNO_NEW", empRefNo);
                List custIDLst = ClientUtil.executeQuery("getCustIdFromEmpRefNo", inputMap);
                if (custIDLst != null && custIDLst.size() > 0) {
                    inputMap = (HashMap) custIDLst.get(0);
                    viewMap.put(CommonConstants.MAP_NAME, "getActNoFromCustomerID");
                    whereMap.put("CUST_ID", inputMap.get("CUST_ID"));
                }
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
            }
            if (cboProdId.getModel() != null && cboProdId.getModel().getSize() > 0) {
                whereMap.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
            }

            whereMap.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
        new ViewAll(this, viewMap).show();
    }

    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        HashMap where = new HashMap();
        if (viewType != null) {
            if (viewType.equals("CLOCK_NO")) {
                where.put("MEMBER_NO", hash.get("MEMBER_NO"));
                txtEmployeeNo.setText(hash.get("CLOCK_NO").toString());;
            }
            System.out.println("#### hash" + hash);
            if (viewType.equals("CREDIT_ACC_NO")) {
                String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
                if (prodType != null && !prodType.equals("GL")) {
                    if (prodType.equals("TD")) {
                        hash.put("ACCOUNTNO", hash.get("ACCOUNTNO") + "_1");
                    }
                    txtAccountNo.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
                    observable.setTxtAccountNo(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
                    lblAccountName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
                } else {
                    txtAccountNo.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
                    observable.setTxtAccountNo(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
                }
            }
        }
    }

    private void insertData() {
        if (txtAccountNo.getText().length() > 0 && cboProdType.getSelectedIndex() > 0) {
            HashMap insertMap = new HashMap();
            String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
            String empRefNo = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(tblSalaryRecoveryList.getSelectedRow(), 1));
            String memberName = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(tblSalaryRecoveryList.getSelectedRow(), 2));
            insertMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(observable.getTdtCalcIntUpto())));
            insertMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtAccountNo.getText()));
            insertMap.put("MEMBER_NAME", memberName);
            insertMap.put("EMP_REF_NO", empRefNo);
            insertMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(cboProdId.getSelectedItem()));
            insertMap.put("PROD_TYPE", prodType);
            if (!prodType.equals("GL")) {
                insertMap.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
            } else {
                insertMap.put("PROD_ID", "");
            }
            insertMap.put("STATUS", CommonConstants.STATUS_CREATED);
            String emptype = "";
            List newList = ClientUtil.executeQuery("getEmptype", insertMap);
            if (newList != null && newList.size() > 0) {
                HashMap empType = (HashMap) newList.get(0);
                emptype = CommonUtil.convertObjToStr(empType.get("EMP_TYPE"));
            }
            newList = null;
            insertMap.put("EMP_TYPE", emptype);
            ClientUtil.execute("insertSalaryRecoveryListDetail", insertMap);
        }
    }

    private void insertData1(String actnum, String pid, String desc,String curActNo) {

        HashMap insertMap = new HashMap();
        String empRefNo = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(tblSalaryRecoveryList.getSelectedRow(), 1));
        String memberName = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(tblSalaryRecoveryList.getSelectedRow(), 2));
        insertMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(observable.getTdtCalcIntUpto())));
        insertMap.put("ACT_NUM", CommonUtil.convertObjToStr(actnum));
        insertMap.put("MEMBER_NAME", memberName);
        insertMap.put("EMP_REF_NO", empRefNo);
        insertMap.put("SCHEME_NAME", desc);
        insertMap.put("PROD_TYPE", "SA");
        insertMap.put("PROD_ID", pid);
        insertMap.put("STATUS", CommonConstants.STATUS_CREATED);
        insertMap.put("PARTICULARS", curActNo);
        String emptype = "";
        List newList = ClientUtil.executeQuery("getEmptype", insertMap);
        if (newList != null && newList.size() > 0) {
            HashMap empType = (HashMap) newList.get(0);
            emptype = CommonUtil.convertObjToStr(empType.get("EMP_TYPE"));
        }
        newList = null;
        insertMap.put("EMP_TYPE", emptype);
        
        
        List detailedList = ClientUtil.executeQuery("getSalaryRecoverListEntryPresent",insertMap);
        if(detailedList != null && detailedList.size()>0){
            //Do Nothing, No need to insert suspense account entry once again.
        }else{
            ClientUtil.execute("insertSalaryRecoveryListDetail", insertMap);
        }
    }

    public int getTabNo() {
        return tabNo;
    }

    public void setTabNo(int tabNo) {
        this.tabNo = tabNo;
    }

    private void inserttblDataUnrecovery() {
        if (txtEmployeeNo.getText().length() > 0 && tdtDtOfRecovery.getDateValue() != null) {
            HashMap updateMap = new HashMap();
            ArrayList idList = new ArrayList();
            for (int i = 0; i < Unreco.size(); i++) {
                List tabList = (List) Unreco.get(i);
                System.out.println("tabListtabListtabList" + tabList);
                double prin = CommonUtil.convertObjToDouble(tabList.get(0));
                double inter = CommonUtil.convertObjToDouble(tabList.get(2));
                double penal = CommonUtil.convertObjToDouble(tabList.get(3));
                double demand = CommonUtil.convertObjToDouble(tabList.get(1));
                double charges = CommonUtil.convertObjToDouble(tabList.get(4));
                String accountno = CommonUtil.convertObjToStr(tabList.get(5));
                updateMap.put("principal", prin);
                updateMap.put("inter", inter);
                updateMap.put("penal", penal);
                updateMap.put("demand", demand);
                updateMap.put("charges", charges);
                updateMap.put("accountno", accountno);
                int instNo = 0;
                String ac_no = accountno;
                if (accountno != null && accountno.indexOf("_") > 0) {
                    ac_no = accountno.substring(0, accountno.indexOf("_"));
                }
                if (instNoMap != null && instNoMap.containsKey(ac_no)) {
                    System.out.println("instNoMap.get(ac_no):" + instNoMap.get(ac_no));
                    instNo = CommonUtil.convertObjToInt(instNoMap.get(ac_no));
                }
                updateMap = new HashMap();
                updateMap.put("REC_PRINCIPAL", prin);
                updateMap.put("REC_PENAL", penal);
                updateMap.put("REC_INTEREST", inter);
                updateMap.put("REC_CHARGES", charges);
                updateMap.put("TALLY_VERIFIED", "Y");
                updateMap.put("DEP_INST_NO", instNo);
                updateMap.put("ACT_NUM", accountno);
                updateMap.put("EMP_REF_NO", txtEmployeeNo.getText());
                updateMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(tdtDtOfRecovery.getDateValue())));
                System.out.println("updateMapupdateMapupdateMapupdateMap" + updateMap);
                ClientUtil.execute("updateTallyRecoveredDetails", updateMap);
                idList.add(accountno);
            }
            ArrayList singleRecoveryList = new ArrayList();
            for (int i = 0; i < tblproduct.getRowCount(); i++) {
                String accountno = CommonUtil.convertObjToStr(tblproduct.getValueAt(i, 1));
                ArrayList valList = new ArrayList();
                String prd_typ = "", prd_id = "";
                if (prod_Map != null && prod_Map.containsKey(accountno)) {
                    valList = (ArrayList) prod_Map.get(accountno);
                    if (valList != null && valList.size() > 0) {
                        prd_typ = CommonUtil.convertObjToStr(valList.get(0));
                        prd_id = CommonUtil.convertObjToStr(valList.get(1));
                    }

                }

                if (!idList.contains(accountno) && accountno != null && accountno.length() > 0) {
                    System.out.println("accountno :" + accountno);
                    double prin = CommonUtil.convertObjToDouble(tblproduct.getValueAt(i, 2));
                    double inter = CommonUtil.convertObjToDouble(tblproduct.getValueAt(i, 3));
                    double penal = CommonUtil.convertObjToDouble(tblproduct.getValueAt(i, 4));
                    double demand = CommonUtil.convertObjToDouble(tblproduct.getValueAt(i, 7));
                    double charges = CommonUtil.convertObjToDouble(tblproduct.getValueAt(i, 5));

                    updateMap.put("principal", prin);
                    updateMap.put("inter", inter);
                    updateMap.put("penal", penal);
                    updateMap.put("demand", demand);
                    updateMap.put("charges", charges);
                    updateMap.put("accountno", accountno);
                    int instNo = 0;
                    String ac_no = accountno;
                    if (accountno != null && accountno.indexOf("_") > 0) {
                        ac_no = accountno.substring(0, accountno.indexOf("_"));
                    }
                    if (instNoMap != null && instNoMap.containsKey(ac_no)) {
                        System.out.println("instNoMap.get(ac_no):" + instNoMap.get(ac_no));
                        instNo = CommonUtil.convertObjToInt(instNoMap.get(ac_no));
                    }
                    // ClientUtil.execute("updateSalaryUnRecoveryList", updateMap);
                    updateMap = new HashMap();
                    updateMap.put("REC_PRINCIPAL", prin);
                    updateMap.put("REC_PENAL", penal);
                    updateMap.put("REC_INTEREST", inter);
                    updateMap.put("REC_CHARGES", charges);
                    updateMap.put("TALLY_VERIFIED", "Y");
                    updateMap.put("DEP_INST_NO", instNo);
                    updateMap.put("ACT_NUM", accountno);
                    updateMap.put("EMP_REF_NO", txtEmployeeNo.getText());
                    updateMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(tdtDtOfRecovery.getDateValue())));
                    ClientUtil.execute("updateTallyRecoveredDetails", updateMap);
                }
            }
            ClientUtil.showMessageWindow("Data Updated");
            clearTabledata();
            txtTotDemAmt.setText("");
            txtTotalUnRecAmt.setText("");
            txtDiffAmt.setText("");
        }
    }

    private void clearTabledata() {
        for (int i = 0; i < tblproduct.getRowCount(); i++) {
            for (int j = 0; j < tblproduct.getColumnCount(); j++) {
                tblproduct.getModel().setValueAt("", i, j);
            }
        }
    }

    private void clearAccDetails() {
        panAccountDetails.setVisible(false);
        cboProdType.setSelectedItem("");
        cboProdId.setSelectedItem("");
        lblAccountName.setText("");
        txtAccountNo.setText("");
    }

    private void enableDisBtn(boolean flag) {
        btnSchedNew.setEnabled(flag);
        btnSchedDelete.setEnabled(flag);
        btnSchedSave.setEnabled(flag);
    }

    private void automateTallyverify() {
        //Jeffin
        if (tblSalaryRecoveryList.getRowCount() > 0) {
            for (int i = 0; i < tblSalaryRecoveryList.getRowCount(); i++) {
                double demandAmt = 0;
                double recoveredAmt = 0;
                demandAmt = CommonUtil.convertObjToDouble(tblSalaryRecoveryList.getValueAt(i, 3).toString()).doubleValue();
                recoveredAmt = CommonUtil.convertObjToDouble(tblSalaryRecoveryList.getValueAt(i, 4).toString()).doubleValue();
                if (demandAmt == recoveredAmt) {
                    ArrayList recoveryList = new ArrayList();
                    ArrayList singleRecoveryList = new ArrayList();
                    String empRefNo = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(i, 1));
                    recoveryList = (ArrayList) finalMap.get(empRefNo);
                 //   System.out.println("recoveryList recoveryList" + recoveryList.size());
                   // System.out.println("recoveryListrecoveryList" + recoveryList);
                    HashMap whereMap = new HashMap();
                    boolean chk = true;
                    for (int j = 0; j < recoveryList.size(); j++) {
                      //  System.out.println("recoveryList : "+recoveryList+"j : "+j);
                        HashMap updateMap = new HashMap();
                        whereMap = (HashMap) recoveryList.get(j);
                     //   System.out.println("whereMapwhereMap" + whereMap);
                        whereMap.put("RECOVERED_AMOUNT", whereMap.get("TOTAL_DEMAND"));
                        singleRecoveryList.add(whereMap);
                        HashMap hmap = new HashMap();
                        String act_num = CommonUtil.convertObjToStr(whereMap.get("ACT_NUM"));
                        hmap.put("ACT_NUM", act_num);
                        List aList = ClientUtil.executeQuery("getActstatus", hmap);
                        if (aList != null && aList.size() > 0) {
                            HashMap statusMap = (HashMap) aList.get(0);
                            String status = statusMap.get("ACCT_STATUS").toString();
                            if (status.equals("CLOSED")) {
                                chk = false;
                                colourList.add(String.valueOf(i));
                                break;
                            }
                        }
                        HashMap hMap = new HashMap();
                        if (act_num.indexOf("_") != -1) {
                            act_num = act_num.substring(0, act_num.indexOf("_"));
                       //     System.out.println("act_num Jeffin"+act_num);
                        }
                        hMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(whereMap.get("INT_CALC_UPTO_DT")))));
                        hMap.put("ACT_NUM", act_num);
                        hMap.put("CURR_DT", currDate.clone());
                        //System.out.println("hmap++"+hMap);
                        List transList = ClientUtil.executeQuery("getSalaryRecoveryListTallyTransDetails", hMap);
                        if (transList != null && transList.size() > 0) {
                          //  System.out.println("transList---transList"+transList);
                            chk = false;
                            colourList.add(String.valueOf(i));
                            break;
                        }
                        updateMap.put("TALLY_VERIFIED", "Y");
                        updateMap.put("ACT_NUM", whereMap.get("ACT_NUM"));
                        updateMap.put("EMP_REF_NO", empRefNo);
                        updateMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(observable.getTdtCalcIntUpto())));
                        updateMap.put("REC_PRINCIPAL", "REC_PRINCIPAL");
                     //   System.out.println("updating"+empRefNo+"updateMap"+updateMap);
                        ClientUtil.execute("updateTallyRecoveredDetailsAutomate", updateMap);
                    }
                    if (chk == true) {
                        finalMap.put(empRefNo, singleRecoveryList);
                    }
                }
            }
        }
    }
    //Calculate Total

    private void calcRecoveryListTotal() {
        double totAmt = 0;
        if (tblSalaryRecoveryList.getRowCount() > 0) {
            for (int i = 0; i < tblSalaryRecoveryList.getRowCount(); i++) {
                totAmt = totAmt + CommonUtil.convertObjToDouble(tblSalaryRecoveryList.getValueAt(i, 3).toString()).doubleValue();
            }
            txtTotalAmount.setText(String.valueOf(totAmt));
        }
        double totRecoveredAmt = 0;
        if (tblSalaryRecoveryList.getRowCount() > 0) {
            for (int i = 0; i < tblSalaryRecoveryList.getRowCount(); i++) {
                totRecoveredAmt = totRecoveredAmt + CommonUtil.convertObjToDouble(tblSalaryRecoveryList.getValueAt(i, 4).toString()).doubleValue();
            }
            txtTotalRecoveredAmount.setText(String.valueOf(totRecoveredAmt));
        }
    }

    //Check Tally
    private void TallyCheck() {
        if (tblSalaryRecoveryList.getRowCount() > 0) {
            colourList = new ArrayList();
            recverdList = new HashMap();
            for (int i = 0; i < tblSalaryRecoveryList.getRowCount(); i++) {
                double demandAmt = 0;
                double recoveredAmt = 0;
                double db_rec_amt = 0, diffAmt = 0;;
                demandAmt = CommonUtil.convertObjToDouble(tblSalaryRecoveryList.getValueAt(i, 3).toString()).doubleValue();//15359
                recoveredAmt = CommonUtil.convertObjToDouble(tblSalaryRecoveryList.getValueAt(i, 4).toString()).doubleValue();//8662.15
                String emp_ref_no = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(i, 1));
                HashMap whereMap = new HashMap();
                whereMap.put("EMP_TYPE", observable.getEmp_type());
                whereMap.put("EMP_REF_NO", emp_ref_no);
                List recoveryAmtList = ClientUtil.executeQuery("getRecoveredAmountForDivision", whereMap);
                if (recoveryAmtList != null && recoveryAmtList.size() > 0) {
                    HashMap oneMap = (HashMap) recoveryAmtList.get(0);
                    if (oneMap != null && oneMap.containsKey("RECOVERED_AMOUNT")) {
                        db_rec_amt = CommonUtil.convertObjToDouble(oneMap.get("RECOVERED_AMOUNT"));//8662.15
                        if (db_rec_amt > 0) {
                            double diff = demandAmt - db_rec_amt;
                            ArrayList singList = new ArrayList();
                            singList.add(diff);
                            singList.add(oneMap.get("UNRECOVERY"));
                            recverdList.put(i, singList);
                        }
                    }

                }
                if (demandAmt != recoveredAmt) {
                    diffAmt = demandAmt - recoveredAmt;//6696.85
                    if (diffAmt == (demandAmt - db_rec_amt)) {
                        System.out.println("adding colour");
                        colourList.add(String.valueOf(i));
                    }
                }
            }
//            System.out.println("###### colourList : "+colourList);
        }
    }

    private void setColour() {
        /*
         * Set a cellrenderer to this table in order format the date
         */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (colourList.contains(String.valueOf(row))) {
                    setForeground(Color.RED);
                } else {
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblSalaryRecoveryList.setDefaultRenderer(Object.class, renderer);
    }
    
    private void setColourTalleyClosed() {
        /*
         * Set a cellrenderer to this table in order format the date
         */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (clrList.contains(String.valueOf(row))) {
                    setForeground(Color.RED);
                } else {
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblRecoveryListTally.setDefaultRenderer(Object.class, renderer);
    }
    
    private void setColourTalleyTransaction() {
        /*
         * Set a cellrenderer to this table in order format the date
         */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (clrList.contains(String.valueOf(row))) {
                    setForeground(Color.MAGENTA);
                } else {
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblRecoveryListTally.setDefaultRenderer(Object.class, renderer);
    }

    private void tallyCheckingAmount() {
        if (tblRecoveryListTally.getRowCount() > 0) {
            double recoveredAmount = CommonUtil.convertObjToDouble(tblSalaryRecoveryList.getValueAt(tblSalaryRecoveryList.getSelectedRow(), 4)).doubleValue();
            double totalRecoveredAmt = CommonUtil.convertObjToDouble(txtTotalRecoveredTallyAmt.getText()).doubleValue();
            System.out.println("recoveredAmount" + recoveredAmount + "totalRecoveredAmt" + totalRecoveredAmt);
            if (recoveredAmount != totalRecoveredAmt && recverdList != null && recverdList.containsKey(tblSalaryRecoveryList.getSelectedRow())) {
                ArrayList each = (ArrayList) recverdList.get(tblSalaryRecoveryList.getSelectedRow());
                if (each != null && each.size() > 0) {
                    recoveredAmount = recoveredAmount + CommonUtil.convertObjToDouble(each.get(0));
                }
            }
            String recAmt = df.format(recoveredAmount);
            String totRecAmt = df.format(totalRecoveredAmt);
            //if(recoveredAmount!=CommonUtil.convertObjToDouble(txtTotalRecoveredTallyAmt.getText()).doubleValue()){
            if (!recAmt.equals(totRecAmt)) {
                String message = "";
                recoveredAmount = Double.parseDouble(df.format(recoveredAmount));
                totalRecoveredAmt = Double.parseDouble(df.format(totalRecoveredAmt));
                if (recoveredAmount > totalRecoveredAmt) {
                    message = "Short Amount Rs. " + (df.format(recoveredAmount - totalRecoveredAmt));
                } else {
                    message = "Excess Amount Rs. " + (df.format(totalRecoveredAmt - recoveredAmount));
                }
                ClientUtil.showMessageWindow("Amount Not Tallied !!!" + "\n" + message);
                return;
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAcceptChanges;
    private com.see.truetransact.uicomponent.CButton btnAccountNo;
    private com.see.truetransact.uicomponent.CButton btnBrowse;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnEmployeeNo;
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnSchedDelete;
    private com.see.truetransact.uicomponent.CButton btnSchedNew;
    private com.see.truetransact.uicomponent.CButton btnSchedSave;
    private com.see.truetransact.uicomponent.CButton btnUnrecoveryProcess;
    private com.see.truetransact.uicomponent.CButton btnUnrecoverySave;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CComboBox cboEmp_type;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private javax.swing.JPanel jPanel1;
    private com.see.truetransact.uicomponent.CLabel lblAccountName;
    private com.see.truetransact.uicomponent.CLabel lblAccountNo;
    private com.see.truetransact.uicomponent.CLabel lblDtOfBirth;
    private com.see.truetransact.uicomponent.CLabel lblEmployeNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblRecoveredDate;
    private com.see.truetransact.uicomponent.CLabel lblRecoveredList;
    private com.see.truetransact.uicomponent.CLabel lblRecoveredListDate;
    private com.see.truetransact.uicomponent.CLabel lblRecoveredListDateTally;
    private com.see.truetransact.uicomponent.CLabel lblRecoveredListDateTallyVal;
    private com.see.truetransact.uicomponent.CLabel lblRecoveredListDateVal;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmt2;
    private com.see.truetransact.uicomponent.CLabel lbltotUnRecAmt;
    private com.see.truetransact.uicomponent.CPanel panAccountDetails;
    private com.see.truetransact.uicomponent.CPanel panBrowseDetails;
    private com.see.truetransact.uicomponent.CPanel panCustomerDetail;
    private com.see.truetransact.uicomponent.CPanel panCustomerNO;
    private com.see.truetransact.uicomponent.CPanel panImportSalaryRecoveryList;
    private com.see.truetransact.uicomponent.CPanel panImportSalaryRecoveryList1;
    private com.see.truetransact.uicomponent.CPanel panMemberReceiptTrans;
    private com.see.truetransact.uicomponent.CPanel panProcessRecoveryList;
    private com.see.truetransact.uicomponent.CPanel panRecoveryEditDetails;
    private com.see.truetransact.uicomponent.CPanel panRecoveryTallyList;
    private com.see.truetransact.uicomponent.CPanel panSalaryRecoveryList;
    private com.see.truetransact.uicomponent.CPanel panSchedBtn;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTallyTableTotalAmount;
    private com.see.truetransact.uicomponent.CPanel panTotalAmount;
    private com.see.truetransact.uicomponent.CPanel panTotalAmount3;
    private com.see.truetransact.uicomponent.CScrollPane srpSalaryRecoveryList;
    private com.see.truetransact.uicomponent.CScrollPane srpTableRecoveryListTally;
    private com.see.truetransact.uicomponent.CTabbedPane tabRecoveryListTally;
    private com.see.truetransact.uicomponent.CTable tblRecoveryListTally;
    private com.see.truetransact.uicomponent.CTable tblSalaryRecoveryList;
    private com.see.truetransact.uicomponent.CTable tblproduct;
    private com.see.truetransact.uicomponent.CDateField tdtDtOfRecovery;
    private com.see.truetransact.uicomponent.CDateField tdtRecoveredDate;
    private com.see.truetransact.uicomponent.CTextField txtAccountNo;
    private com.see.truetransact.uicomponent.CTextField txtDiffAmt;
    private com.see.truetransact.uicomponent.CTextField txtEmployeeNo;
    private com.see.truetransact.uicomponent.CTextField txtTotDemAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmount;
    private com.see.truetransact.uicomponent.CTextField txtTotalDemandTally;
    private com.see.truetransact.uicomponent.CTextField txtTotalRecoveredAmount;
    private com.see.truetransact.uicomponent.CTextField txtTotalRecoveredTallyAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotalUnRecAmt;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        RecoveryListTallyUI fad = new RecoveryListTallyUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(fad);
        j.show();
        fad.show();
    }
}
