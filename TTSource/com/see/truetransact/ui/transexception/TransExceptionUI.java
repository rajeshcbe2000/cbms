/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TransExceptionUI.java
 *
 * Created on October 10th, 2011, 11:03 PM
 */
package com.see.truetransact.ui.transexception;

/**
 *
 * @jithin
 */
import java.util.*;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.ClientConstants;
//import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
//import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.ui.common.viewall.TextUI;
import javax.swing.JDialog;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

public class TransExceptionUI extends CInternalFrame implements Observer {

    /**
     * Vairable Declarations
     */
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    TransExceptionOB observable = null;
    private boolean selectMode = false;
    private Date currDate = null;
    private HashMap returnMap = null;
    private List clkno = null;
    int count = 0;
    private String txtName = "";
    private double amount = 0.0;
    private double minimumbalance = 0.0;
    private double total = 0.0;
    private ArrayList listadd = null;
    private ArrayList listTot = new ArrayList();
    private ArrayList newList = new ArrayList();
    int k = 0;
    int i = 0;
    private boolean rdoPrizedMember_Yes = false;
    private boolean rdoPrizedMember_No = false;
    HashMap returnmap = new HashMap();
    HashMap finalMap1 = new HashMap();
    HashMap finalMap = new HashMap();
    HashMap acMap = new HashMap();
    ArrayList recoveryList = null;
    ArrayList recoveryRowList = null;
    String db_actnum = null;
    String db_prodid = null;
    HashMap fMap = new HashMap();
    Date currDt = null;
    private boolean flag = false;
    HashMap termMdsMap = null;
    List changedList = new ArrayList();
    private double actualInterest = 0;
    private double actualPrincipal = 0;
    private double actualPenal = 0;
    private double actualCharges =0;

    /**
     * Creates new form TokenConfigUI
     */
    public TransExceptionUI() {
        returnMap = null;
        currDate = ClientUtil.getCurrentDate();
        initForm();

    }

    /**
     * Method which is used to initialize the form TokenConfig
     */
    private void initForm() {
        initComponents();
        currDt = ClientUtil.getCurrentDate();
        observable = new TransExceptionOB();
        tdtProcessDate.setDateValue(CommonUtil.convertObjToStr(currDate));
        initTableData();
        btnSave.setEnabled(false);
        ClientUtil.enableDisable(panDepositInterestApplication, false);
        tdtReprintDate.setEnabled(true);
                

    }

    public void getClockNo() {
        HashMap where = new HashMap();
        HashMap map = new HashMap();
        HashMap returnMap1 = new HashMap();
        map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        where.put(CommonConstants.MAP_WHERE, map);
        clkno = (ClientUtil.executeQuery("GetClockNO", where));
        // System.out.println("clock_no size..."+clkno.size());
        for (i = 0; i < clkno.size(); i++) {
            returnMap1 = (HashMap) clkno.get(i);
            String clk_no = returnMap1.get("CLOCK_NO").toString();
            String custid = returnMap1.get("CUST_ID").toString();
            System.out.println("clock no is" + clk_no);
            getProducts(custid, clk_no);
        }
        System.out.println("fMap size is init" + fMap);
        getData();
    }

    public void getProducts(String clk_no, String clkno) {
        count++;
        // System.out.println("clock no is 1"+clk_no);
        HashMap where = new HashMap();
        HashMap map = new HashMap();
        map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        map.put("CLOCK_NO", clk_no);
        where.put(CommonConstants.MAP_WHERE, map);
        List aList = (ClientUtil.executeQuery("GetSBdetails", where));
        if (aList.size() > 0) {
            HashMap sbdet = (HashMap) aList.get(0);
            String actnum = sbdet.get("ACT_NUM").toString();
            sbdet.put("CLOCK_NO", clkno);
            txtName = sbdet.get("ACCT_NAME").toString();
            double sbbalance = Double.parseDouble((sbdet.get("TOTAL_BALANCE").toString()));
            double min_balance = Double.parseDouble((sbdet.get("MIN_BALANCE").toString()));
            String cust_id = sbdet.get("CUST_ID").toString();
            System.out.println("balance is..+" + sbbalance);
            if (sbbalance > min_balance) {
                System.out.println("clock no is 2" + clk_no + "and balance is" + sbbalance);
                getDueDetails(sbdet);
            }

        }
    }

    public void getDueDetails(HashMap map) {
        double amt = 0.0;
        String cust_id = map.get("CUST_ID").toString();
        String clock_num = map.get("CLOCK_NO").toString();
        String name = map.get("ACCT_NAME").toString();
        db_actnum = map.get("ACT_NUM").toString();
        db_prodid = map.get("PROD_ID").toString();
        System.out.println("cust_id" + cust_id);
        HashMap where = new HashMap();
        where.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        where.put("CUST_ID", cust_id);
        amount = Double.parseDouble(map.get("TOTAL_BALANCE").toString());
        minimumbalance = Double.parseDouble(map.get("MIN_BALANCE").toString());
        List newDetails = ClientUtil.executeQuery("getDueDetailsTransException", where);
        if (newDetails.size() > 0) {
            for (int j = 0; j < newDetails.size(); j++) {
                HashMap returnmap = new HashMap();
                HashMap products = (HashMap) newDetails.get(j);
                String prodtype = products.get("PRODTYPE").toString();
                String prodid = products.get("PROD_ID").toString();
                String actnum = products.get("ACT_NUM").toString();
                String proddesc = products.get("PROD_DESC").toString();
                //System.out.println("haiii prod"+prodtype);
                if (amount > minimumbalance) {
                    observable.populateData(products);
                    returnMap = observable.getMapData();
                     //System.out.println("haiii returnmap at asd ...."+ returnMap);
                    if (returnMap != null && returnMap.size() > 0) {
                        total = Double.parseDouble(returnMap.get("TOTAL").toString());
                        System.out.println("amount is insufficient....total : "+total +" amount : "+amount +" minimumbalance : "+minimumbalance);
                        if(total>0){
                        if (total < (amount - minimumbalance)) {
                            returnMap.put("TOTAL", amount - minimumbalance);
                            if (prodtype.equals("TL") || prodtype.equals("AD")) {
                                amt = amount - minimumbalance;
                                System.out.println("amount here asd"+amt);
                                returnMap = getLoanDetails(returnMap, prodid, actnum, amt,prodtype);
                                // System.out.println("returnmap in tl after passing the amount..."+returnMap);
                                if (returnMap.containsKey("DATAMAP")) {
                                    HashMap datamapNew = new HashMap();
                                    datamapNew = (HashMap) (returnMap.get("DATAMAP"));
                                    System.out.println("datamap new ......" + datamapNew);
                                    fMap.put(k, datamapNew);
                                    k++;
                                    //finalMap.put("TL",datamapNew);
                                }
                            } else if (prodtype.equals("MDS")) {
                                // getMDSDetails(returnMap,prodid,actnum);
                                amt = amount - minimumbalance;
                                if (returnMap.containsKey("DATAMAP")) {
                                    HashMap datamap = new HashMap();
                                    datamap = (HashMap) (returnMap.get("DATAMAP"));
                                    //  System.out.println("datamap in mds..."+datamap);
                                    int noofinstmnt = Integer.parseInt(datamap.get("PENDING_INST").toString());
                                    int noInstmt = noofinstmnt - 1;
                                    HashMap dataMap = new HashMap();
                                    boolean flag = false;
                                    for (int i = (noofinstmnt); i > 0; i--) {
                                        dataMap = getMDSDetails(returnMap, prodid, actnum, i);
                                        //  System.out.println("datamap..2222..again"+dataMap+"i==="+i);
                                        if (dataMap != null && dataMap.size() > 0) {
                                            String demand = (dataMap.get("NET_AMOUNT").toString());
                                            if (demand != null) {
                                                double totDemand = Double.parseDouble(dataMap.get("NET_AMOUNT").toString());
                                                System.out.println("demand and amt" + totDemand + "===" + amt);
                                                if (amt > totDemand) {
                                                    flag = true;
                                                    break;

                                                } else {
                                                    total = 0.0;
                                                    returnMap.put("TOTAL", total);
                                                }
                                            }
                                        }
                                        dataMap.clear();
                                    }
                                    if (flag == true) {
                                        recoveryList = new ArrayList();
                                        // System.out.println("datamap....again"+dataMap);
                                        if (dataMap != null && dataMap.size() > 0) {
                                            recoveryRowList = new ArrayList();
                                            recoveryRowList.add(CommonUtil.convertObjToStr("MDS"));//PRODTYPE
                                            recoveryRowList.add(CommonUtil.convertObjToStr(actnum));
                                            recoveryRowList.add("");
                                            recoveryRowList.add(CommonUtil.convertObjToStr(dataMap.get("PRINCIPAL")));
                                            recoveryRowList.add(CommonUtil.convertObjToStr(dataMap.get("PENAL")));
                                            recoveryRowList.add(CommonUtil.convertObjToStr(dataMap.get("INTEREST")));
                                            recoveryRowList.add(CommonUtil.convertObjToStr(dataMap.get("BONUS")));//Bonus
                                            recoveryRowList.add(CommonUtil.convertObjToStr(dataMap.get("CHARGES")));
                                            recoveryRowList.add("");//Notice
                                            recoveryRowList.add("");//Arbitration
                                            recoveryRowList.add(prodid);//prodid
                                            recoveryRowList.add("");//total
                                            recoveryList.add(recoveryRowList);
                                            double totDemand = Double.parseDouble(dataMap.get("NET_AMOUNT").toString());
                                            // total=Double.parseDouble(dataMap.get("PRINCIPAL").toString())+Double.parseDouble(dataMap.get("PENAL").toString())+Double.parseDouble(dataMap.get("INTEREST").toString())+Double.parseDouble(dataMap.get("CHARGES").toString());
                                            returnMap.put("TOTAL", totDemand);
                                            System.out.println("TOTAL IN MDS..######" + totDemand);
                                            returnMap.put("RECOVERY_LIST_TABLE_DATA", recoveryList);
                                            returnMap.put("DATAMAP", dataMap);
                                            dataMap.put("RECOVERED_AMOUNT", totDemand);
                                            dataMap.put("PROD_ID", prodid);
                                            dataMap.put("PROD_TYPE", prodtype);
                                            dataMap.put("ACT_NUM", actnum);
                                            dataMap.put("DEBIT_ACT_NUM", db_actnum);
                                            dataMap.put("DEBIT_PROD_ID", db_prodid);
                                            fMap.put(k, dataMap);
                                            k++;
                                        }
                                        flag = false;
                                    }
                                }
                            } else if (prodtype.equals("TD")) {
                                amt = amount - minimumbalance;
                                if (returnMap.containsKey("DATAMAP")) {
                                    HashMap datamap = new HashMap();
                                    datamap = (HashMap) (returnMap.get("DATAMAP"));
                                    System.out.println("datamap in TD..." + datamap);
                                    int noofinstmnt = 1;
                                    if (datamap.get("TOTAL").toString() != null && CommonUtil.convertObjToDouble(datamap.get("TOTAL")).doubleValue()>0) {
                                        noofinstmnt = Integer.parseInt(datamap.get("TOTAL").toString());
                                    }
//                                    int noInstmt = noofinstmnt - 1;
                                    HashMap dataMap = new HashMap();
                                    boolean flag = false;
                                    for (int i = (noofinstmnt); i > 0; i--) {
                                        try {
                                            dataMap = getDepositsDetails(actnum, prodid, i);
                                            System.out.println("datamap..2222..again" + dataMap + "i===" + i);
                                            if (dataMap != null && dataMap.size() > 0) {
                                                String demand = (dataMap.get("TOTAL_DEMAND").toString());
                                                if (demand != null) {
                                                    double totDemand = Double.parseDouble(dataMap.get("TOTAL_DEMAND").toString());
                                                    System.out.println("demand and amt" + totDemand + "===" + amt);
                                                    if (amt > totDemand) {
                                                        flag = true;
                                                        break;

                                                    } else {
                                                        total = 0.0;
                                                        returnMap.put("TOTAL", total);
                                                    }
                                                }
                                            }
                                            dataMap.clear();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (flag == true) {
                                        recoveryList = new ArrayList();
                                        System.out.println("datamap in td....again" + dataMap);
                                        System.out.println("datamap....again" + dataMap);
                                        if (returnMap != null && returnMap.size() > 0) {
                                            recoveryRowList = new ArrayList();
                                            recoveryRowList.add(CommonUtil.convertObjToStr("MDS"));//PRODTYPE
                                            recoveryRowList.add(CommonUtil.convertObjToStr(actnum));
                                            recoveryRowList.add("");
                                            recoveryRowList.add(CommonUtil.convertObjToStr(dataMap.get("PRINCIPAL")));
                                            recoveryRowList.add(CommonUtil.convertObjToStr(dataMap.get("PENAL")));
                                            recoveryRowList.add(CommonUtil.convertObjToStr(dataMap.get("INTEREST")));
                                            recoveryRowList.add("");//Bonus
                                            recoveryRowList.add(CommonUtil.convertObjToStr(dataMap.get("CHARGES")));
                                            recoveryRowList.add("");//Notice
                                            recoveryRowList.add("");//Arbitration
                                            recoveryRowList.add(prodid);//prodid
                                            recoveryRowList.add("");//total
                                            recoveryList.add(recoveryRowList);
                                            total = Double.parseDouble(dataMap.get("PRINCIPAL").toString()) + Math.round(Double.parseDouble(dataMap.get("PENAL").toString())) + Math.round(Double.parseDouble(dataMap.get("INTEREST").toString())) + Math.round(Double.parseDouble(dataMap.get("CHARGES").toString()));
                                            returnMap.put("TOTAL", total);
                                            System.out.println("TOTAL IN TD...######" + total);
                                            returnMap.put("RECOVERY_LIST_TABLE_DATA", recoveryList);
                                            returnMap.put("DATAMAP", dataMap);
                                            dataMap.put("RECOVERED_AMOUNT", total);
                                            dataMap.put("PROD_ID", prodid);
                                            dataMap.put("PROD_TYPE", prodtype);
                                            dataMap.put("ACT_NUM", actnum);
                                            dataMap.put("DEBIT_ACT_NUM", db_actnum);
                                            dataMap.put("DEBIT_PROD_ID", db_prodid);
                                            fMap.put(k, dataMap);
                                            k++;
                                            // finalMap.put("TD", dataMap);
                                        }
                                        flag = false;
                                    }
                                }
                            } else if (prodtype.equals("SA")) {
                                double principal = 0.0;
                                double interest = 0.0;
                                double penal = 0.0;
                                double charges = 0.0;
                                int bill_id = 0;
                                HashMap newMap = new HashMap();
                                if (returnMap.containsKey("RECOVERY_LIST_TABLE_DATA")) {
                                    System.out.println("hai..." + returnMap.get("RECOVERY_LIST_TABLE_DATA"));
                                    bill_id = Integer.parseInt(returnMap.get("BILL_ID").toString());

                                    ArrayList alist1 = new ArrayList();
                                    //tableList = (ArrayList) whereMap.get("INSERT")
                                    alist1 = (ArrayList) returnMap.get("RECOVERY_LIST_TABLE_DATA");
                                    List alist = (List) alist1.get(0);
                                    //alist1=(ArrayList)alist.get(0);
                                    // String j=alist.get(3).toString();

                                    principal = CommonUtil.convertObjToDouble(alist.get(3).toString()).doubleValue();
                                    interest = CommonUtil.convertObjToDouble(alist.get(5).toString()).doubleValue();
                                    penal = CommonUtil.convertObjToDouble(alist.get(4).toString()).doubleValue();
                                    charges = CommonUtil.convertObjToDouble(alist.get(6).toString()).doubleValue();
                                }
                                amt = amount - minimumbalance;
                                ArrayList newList = new ArrayList();
                                newList.add(CommonUtil.convertObjToStr("SA"));
                                newList.add(CommonUtil.convertObjToStr(actnum));
                                newList.add((""));
                                newList.add(CommonUtil.convertObjToStr(principal));
                                newList.add(CommonUtil.convertObjToStr(penal));
                                newList.add(CommonUtil.convertObjToStr(interest));
                                newList.add(CommonUtil.convertObjToStr(charges));
                                Double total1 = ((principal + penal + interest + charges));
                                returnMap.put("RECOVERY_LIST_TABLE_DATA", newList);
                                returnMap.put("TOTAL", total1);
                                newMap.put("RECOVERED_AMOUNT", total1);
                                newMap.put("PROD_ID", prodid);
                                newMap.put("PROD_TYPE", prodtype);
                                newMap.put("ACT_NUM", actnum);
                                newMap.put("PRINCIPAL", new Double(principal));
                                newMap.put("TOTAL_DEMAND", new Double(total1));
                                newMap.put("INTEREST", new Double(0));
                                newMap.put("PENAL", penal);
                                newMap.put("CHARGES", new Double(0));
                                newMap.put("DEBIT_ACT_NUM", db_actnum);
                                newMap.put("DEBIT_PROD_ID", db_prodid);
                                newMap.put("BILL_ID", bill_id);
                                fMap.put(k, newMap);
                                k++;
                                // finalMap.put("SB",newMap);
                                listadd = new ArrayList();
                                listadd.add(new Boolean(false));
                                listadd.add(cust_id);
                                listadd.add(clock_num);
                                listadd.add(name);
                                listadd.add(prodtype);
                                listadd.add(proddesc);//cha
                                listadd.add(actnum);
                                listadd.add(CommonUtil.convertObjToDouble(returnMap.get("TOTAL")).doubleValue());
                                listadd.add(0);
                                listadd.add(principal);
                                listadd.add(interest);
                                listadd.add(penal);
                                listadd.add(0);
                                listadd.add(charges);
                                listadd.add(prodid);
								listadd.add(CommonUtil.convertObjToDouble(returnMap.get("TOTAL")).doubleValue());
                                listadd.add("");
                                listTot.add(listadd);
                                amount = amount - (Double.parseDouble(returnMap.get("TOTAL").toString()));
                                total = 0.0;
                            }
                            System.out.println("returnMap total before insert table" + returnMap.get("TOTAL"));
                            if ((Double.parseDouble(returnMap.get("TOTAL").toString())) > 0.0) {
                                HashMap maps = new HashMap();
                                if (!prodtype.equals("SA")) {
                                    System.out.println("inside here asdas");
                                    if (returnMap.containsKey("DATAMAP")) {
                                        maps = (HashMap) returnMap.get("DATAMAP");
                                        listadd = new ArrayList();
                                        listadd.add(new Boolean(false));
                                        listadd.add(cust_id);
                                        listadd.add(clock_num);
                                        listadd.add(name);
                                        listadd.add(prodtype);
                                        listadd.add(proddesc); ///ch
                                        listadd.add(actnum);
                                        listadd.add(CommonUtil.convertObjToDouble(returnMap.get("TOTAL")));
                                        if (maps.containsKey("NO_OF_INST_PAY")) {
                                            listadd.add(CommonUtil.convertObjToDouble(maps.get("NO_OF_INST_PAY")));
                                        } else {
                                            listadd.add(0);
                                        }
                                        listadd.add(CommonUtil.convertObjToDouble(maps.get("PRINCIPAL")));
                                        listadd.add(CommonUtil.convertObjToDouble(maps.get("INTEREST")));
                                        listadd.add(CommonUtil.convertObjToDouble(maps.get("PENAL")));
                                        if (maps.containsKey("BONUS")) {
                                            listadd.add(CommonUtil.convertObjToDouble(maps.get("BONUS")));
                                        } else {
                                            listadd.add(0);
                                        }
                                        listadd.add(CommonUtil.convertObjToDouble(maps.get("CHARGES")));
                                        listadd.add(prodid);
										listadd.add(CommonUtil.convertObjToDouble(returnMap.get("TOTAL")));
                                        listadd.add(CommonUtil.convertObjToStr(maps.get("CLEAR_BALANCE")));
                                        listTot.add(listadd);
                                        amount = amount - (Double.parseDouble(returnMap.get("TOTAL").toString()));
                                        total = 0.0;
                                        System.out.println("kiaa" + k + " size is" + fMap.size());
                                    }
                                }
                            }
                        } else {
                            System.out.println("elsepart insufficient balance total : "+total +" amount : "+amount +" minimumbalance : "+minimumbalance+" cust_id : "+cust_id+" clock_num : "+clock_num+" name : "+name+" db_actnum : "+db_actnum);
//                            if ((Double.parseDouble(returnMap.get("TOTAL").toString())) > 0.0) {
//                                double bonus = 0.0;
//                                HashMap map1 = new HashMap();
//                                if (returnMap.containsKey("DATAMAP")) {
//                                    map1 = (HashMap) (returnMap.get("DATAMAP"));
//                                    System.out.println("new map1 is" + map1);
//                                    HashMap datamapNew;
//                                    datamapNew = (HashMap) (returnMap.get("DATAMAP"));
//                                    datamapNew.put("RECOVERED_AMOUNT", (Double.parseDouble(returnMap.get("TOTAL").toString())));
//                                    if (map1.containsKey("NO_OF_INST_PAY")) {
//                                        datamapNew.put("NO_OF_INST_PAY", map1.get("NO_OF_INST_PAY"));
//                                    }
//                                    datamapNew.put("PRINCIPAL", map1.get("PRINCIPAL"));
//                                    datamapNew.put("INTEREST", map1.get("INTEREST"));
//                                    datamapNew.put("CHARGES", map1.get("CHARGES"));
//                                    datamapNew.put("PENAL", map1.get("PENAL"));
//                                    if (map1.containsKey("BONUS")) {
//                                        bonus = CommonUtil.convertObjToDouble(map1.get("BONUS"));
//                                    }
//                                    datamapNew.put("BONUS", bonus);
//                                    datamapNew.put("PROD_ID", prodid);
//                                    datamapNew.put("PROD_TYPE", prodtype);
//                                    datamapNew.put("ACT_NUM", actnum);
//                                    datamapNew.put("DEBIT_ACT_NUM", db_actnum);
//                                    datamapNew.put("DEBIT_PROD_ID", db_prodid);
//                                    fMap.put(k, datamapNew);
//                                    System.out.println("kiaa222" + k + " size is" + fMap.size());
////                                if(prodtype.equals("TL")){
////                                    finalMap.put("TL", datamapNew);
////                                }
////                                else if(prodtype.equals("TD")){
////                                    finalMap.put("TD", datamapNew);
////                                }
////                                else if(prodtype.equals("MDS")){
////                                    finalMap.put("MDS", datamapNew);
////                                }
////                                else if(prodtype.equals("SB")){
////                                    finalMap.put("SB", datamapNew);
////                                }
//                                }
//                                listadd = new ArrayList();
//                                listadd.add(new Boolean(false));
//                                listadd.add(cust_id);
//                                listadd.add(clock_num);
//                                listadd.add(name);
//                                listadd.add(prodtype);
//                                listadd.add(proddesc); //cha
//                                listadd.add(actnum);
//                                listadd.add(CommonUtil.convertObjToDouble(returnMap.get("TOTAL")).doubleValue());
//                                if (map1.containsKey("NO_OF_INST_PAY")) {
//                                    listadd.add(CommonUtil.convertObjToDouble(map1.get("NO_OF_INST_PAY")).doubleValue());
//                                } else {
//                                    listadd.add(0);
//                                }
//                                listadd.add(CommonUtil.convertObjToDouble(map1.get("PRINCIPAL")).doubleValue());
//                                listadd.add(CommonUtil.convertObjToDouble(map1.get("INTEREST")).doubleValue());
//                                listadd.add(CommonUtil.convertObjToDouble(map1.get("PENAL")).doubleValue());
//                                listadd.add(bonus);
//                                listadd.add(CommonUtil.convertObjToDouble(map1.get("CHARGES")).doubleValue());
//                                listadd.add(prodid);
//                                listTot.add(listadd);
//
//                                amount = amount - (Double.parseDouble(returnMap.get("TOTAL").toString()));
//                                total = 0.0;
//                                k++;
//                            }
                        }
                        }
                    }
                }


                // returnMap.clear();
            }

        }
    }
    public void getData() {
        System.out.println("Count is..+" + count);
        chkSelectAll.setEnabled(true);
        btnSave.setEnabled(true);
        selectMode = true;
        HashMap newMap = new HashMap();
        newMap.put("INSERT", listTot);
        observable.insertTableData(newMap);
        tblTransExceptionDetails.setModel(observable.getTblTransExceptionDetails());
        setSizeTableData();
        if (tblTransExceptionDetails.getRowCount() == 0) {
            ClientUtil.showMessageWindow(" No Data !!! ");
            btnSave.setEnabled(false);
        }
    }

    private void setSizeTableData() {
        javax.swing.table.TableColumn col = tblTransExceptionDetails.getColumn(tblTransExceptionDetails.getColumnName(0));
        col.setMaxWidth(50);
        col = tblTransExceptionDetails.getColumn(tblTransExceptionDetails.getColumnName(1));
        col.setMaxWidth(120);
        col = tblTransExceptionDetails.getColumn(tblTransExceptionDetails.getColumnName(2));
        col.setMaxWidth(100);
        col = tblTransExceptionDetails.getColumn(tblTransExceptionDetails.getColumnName(3));
        col.setMaxWidth(170);
        col = tblTransExceptionDetails.getColumn(tblTransExceptionDetails.getColumnName(4));
        col.setMaxWidth(80);
        col = tblTransExceptionDetails.getColumn(tblTransExceptionDetails.getColumnName(5));
        col.setMaxWidth(130);
        col = tblTransExceptionDetails.getColumn(tblTransExceptionDetails.getColumnName(6));
        col.setMaxWidth(130);
        col = tblTransExceptionDetails.getColumn(tblTransExceptionDetails.getColumnName(7));
        col.setMaxWidth(80);
    }

    private void initTableData() {
        tblTransExceptionDetails.setModel(observable.getTblTransExceptionDetails());
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
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

    /**
     * Used to set Maximum possible lenghts for TextFields
     */
    private void setMaxLengths() {
    }

    /**
     * Method used to Give a Alert when any Mandatory Field is not filled by the
     * user
     */
    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    public HashMap getDepositsDetails(String actNum, String prodId, int no) throws Exception {
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
                        Date currDt = (Date) currDate.clone();
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
                        matDt.setTime(currDate.getTime());
                        Date depDt = new Date();
                        depDt.setTime(currDate.getTime());
                        System.out.println("&&&&&&&&&&&& CurrentDate11111" + currDate);
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
                            System.out.println("############# CurrentDate" + currDate);
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
                            System.out.println("############# CurrentDate" + currDate);
                            if (DateUtil.dateDiff((Date) matDt, (Date) currDate) > 0) {
                                matDt = setProperDtFormat(matDt);
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
                                int dep = depDt.getMonth() + 1;
                                int curr = currDate.getMonth() + 1;
                                int depYear = depDt.getYear() + 1900;
                                int currYear = currDate.getYear() + 1900;
                                if (depYear == currYear) {
                                    monthPeriod = curr - dep;
                                    actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
                                } else {
                                    int diffYear = currYear - depYear;
                                    monthPeriod = (diffYear * 12 - dep) + curr;
                                    actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
                                }
                                System.out.println("############# else actualDelay" + actualDelay + "############# monthPeriod" + monthPeriod + "############# tot_Inst_paid" + tot_Inst_paid);
                            }
                        }
                        lst = null;

                        if ((DateUtil.dateDiff((Date) matDt, (Date) currDate) > 0) && !insBeyondMaturityDat.equals("") && insBeyondMaturityDat.equals("N")) {
                            dataMap = new HashMap();
                            return dataMap;
                        }
                        System.out.println("#############%%%%% MATURITY_DT" + matDt);
                        System.out.println("############# %%%% CurrentDate" + currDate);
                        //delayed installment calculation...
                        if (DateUtil.dateDiff((Date) matDt, (Date) currDt) < 0 || insBeyondMaturityDat.equals("Y")) {
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
                            lstRec = null;
                            depRecMap = new HashMap();
                            depRecMap.put("DEPOSIT_NO", actNum + "_1");
                            depRecMap.put("DEPOSIT_DT", lastMap.get("DEPOSIT_DT"));
                            depRecMap.put("CURR_DT", currDate);
                            depRecMap.put("SL_NO", String.valueOf(tot_Inst_paid));
                            lstRec = ClientUtil.executeQuery("getDepTransRecurr", depRecMap);
                            if (lstRec != null && lstRec.size() > 0) {//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
                                for (int i = 0; i < lstRec.size(); i++) {//when the customer is paid from that dt to today dt....
                                    depRecMap = (HashMap) lstRec.get(i);
                                    Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                                    int transMonth = currDate.getMonth() + 1;
                                    int dueMonth = dueDate.getMonth() + 1;
                                    int dueYear = dueDate.getYear() + 1900;
                                    int transYear = currDate.getYear() + 1900;
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
                            System.out.println("#### totalDelay--->" + totalDelay);
                            delayAmt = delayAmt * totalDelay;
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
                            double principal = due * CommonUtil.convertObjToDouble(rdDataMap.get("DEPOSIT_AMT")).doubleValue();
                            double totalDemand = principal + balanceAmt;
                            rdDataMap.put("DELAYED_MONTH", accountMap.get("DELAYED_MONTH"));
                            rdDataMap.put("DEPOSIT_PENAL_AMT", String.valueOf(balanceAmt));
                            rdDataMap.put("PRINCIPAL", String.valueOf(principal));
                            rdDataMap.put("TOTAL_DEMAND", String.valueOf(totalDemand));
                            rdDataMap.put("DEPOSIT_PENAL_MONTH", String.valueOf(totalDelay));
                            System.out.println("#### balanceAmt--->" + balanceAmt + "##### totalDelay" + totalDelay);
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

                    if (CommonUtil.convertObjToDouble(rdDataMap.get("TOTAL_DEMAND")).doubleValue() <= 0.0) {
                        dataMap = null;
                    }
                    System.out.println("######## dataMap" + dataMap);
                }
            }
        }
        return dataMap;
    }

    private HashMap getMDSDetails(HashMap returnMap, String prodid, String actnum, int noofinst) {
        HashMap dataMap = new HashMap();
        try {
            String chittalNo = "", subNo = "";
            String prId = prodid;
            chittalNo = actnum;
            if (dataMap != null && dataMap.size() > 0) {
                dataMap.clear();
            }
            //  if (scheme_Name.equals("MDS"))
            //    {
            if (chittalNo.indexOf("_") != -1) {
                subNo = chittalNo.substring(chittalNo.indexOf("_") + 1, chittalNo.length());
                chittalNo = chittalNo.substring(0, chittalNo.indexOf("_"));
            }

            //   }
            String noOfInsPay = String.valueOf(noofinst);
            //For Loop
            //   for(int i=0;i<aList.size();i++)
            //  {
            //      String PID=aList.get(10).toString();
            //     if(PID.equals(prId))
            //     {

            //     }
            // }
            //End

            String noticAmt = "";
            String arbitAmt = "";
            // String insDue = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(),3));
            String insDue = "";
            // int selectedRow = tblTransaction.getSelectedRow();
            HashMap pendingMap = new HashMap();
            pendingMap.put("SCHEME_NAME", prId);
            pendingMap.put("CHITTAL_NO", chittalNo);
            System.out.println("######### subNo" + subNo);
            pendingMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
            //            System.out.println("######### pendingMap"+pendingMap);
            List pendingAuthlst = ClientUtil.executeQuery("checkPendingForAuthorization", pendingMap);
            System.out.println("######### pendingAuthlst=" + pendingAuthlst.size());
            System.out.println("######### noOfInsPay=" + noOfInsPay);
            if (pendingAuthlst != null && pendingAuthlst.size() > 0) {
                ClientUtil.showMessageWindow(" Transaction pending for Chittal " + chittalNo + "  Please Authorize OR Reject first  !!! ");
                //  tblTransaction.setValueAt("", selectedRow, 2);
            } else if (CommonUtil.convertObjToDouble(noOfInsPay).doubleValue() >= 1) {
                System.out.println("######### noOfInsPay IN=" + noOfInsPay);
                HashMap whereMap = new HashMap();

                HashMap productMap = new HashMap();

                long diffDayPending = 0;
                int noOfInsPaid = 0;
                Date currDate = (Date) currDt.clone();
                Date instDate = null;
                boolean bonusAvailabe = true;
                long noOfInstPay = CommonUtil.convertObjToLong(noOfInsPay);
                int instDay = 1;
                int totIns = 0;
                String calculateIntOn = "";
                Date startDate = null;
                Date endDate = null;
                Date insDate = null;
                int startMonth = 0;
                int insMonth = 0;
                int curInsNo = 0;
                HashMap installmentMap = new HashMap();
                whereMap.put("SCHEME_NAME", prId);
                List lst = ClientUtil.executeQuery("getSelectSchemeAcctHead", whereMap);
                System.out.println("#######lst ===" + lst.size() + " LST----" + lst);
                if (lst != null && lst.size() > 0) {
                    productMap = (HashMap) lst.get(0);
                    totIns = CommonUtil.convertObjToInt(productMap.get("NO_OF_INSTALLMENTS"));
                    startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
                    endDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_END_DT")));
                    insDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
                    startMonth = insDate.getMonth();
                    // Added by Rajesh For checking BONUS_FIRST_INSTALLMENT. Based on this for loop initial value will be changed for Penal calculation.
                    //                    String bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("BONUS_FIRST_INSTALLMENT"));
                    String bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("ADVANCE_COLLECTION"));
                    int startNoForPenal = 0;
                    int addNo = 1;
                    int firstInst_No = -1;
                    if (bonusFirstInst.equals("Y")) {
                        startNoForPenal = 1;
                        addNo = 0;
                        firstInst_No = 0;
                    }
                    bonusAvailabe = true;
                    double insAmt = 0.0;
                    //                    double insAmt = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT")).doubleValue();
                    long pendingInst = 0;
                    int divNo = 0;
                    long count = 0;
                    long insDueAmt = 0;
                    whereMap.put("CHITTAL_NO", chittalNo);
                    whereMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                    List insList = ClientUtil.executeQuery("getNoOfInstalmentsPaid", whereMap);
                    System.out.println("#######insList ===" + insList.size() + "insList ===" + insList);
                    if (insList != null && insList.size() > 0) {
                        whereMap = (HashMap) insList.get(0);
                        noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
                        count = CommonUtil.convertObjToLong(whereMap.get("NO_OF_INST"));
                    }

                    HashMap chittalMap = new HashMap();
                    chittalMap.put("CHITTAL_NO", chittalNo);
                    chittalMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                    List chitLst = ClientUtil.executeQuery("getSelctApplnReceiptDetails", chittalMap);
                    System.out.println("#######chitLst ===" + chitLst.size() + "chitLst ===" + chitLst);
                    if (chitLst != null && chitLst.size() > 0) {
                        chittalMap = (HashMap) chitLst.get(0);
                        System.out.println("#######chittalMap ===" + chittalMap);
                        instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                        divNo = CommonUtil.convertObjToInt(chittalMap.get("DIVISION_NO"));
                        dataMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(chittalMap.get("BRANCH_CODE")));
                        insAmt = CommonUtil.convertObjToDouble(chittalMap.get("INST_AMT")).doubleValue();
                    }

                    HashMap insDateMap = new HashMap();
                    insDateMap.put("DIVISION_NO", divNo);
                    insDateMap.put("SCHEME_NAME", prId);
                    insDateMap.put("CURR_DATE", currDt.clone());
                    //                    insDateMap.put("ADD_MONTHS", "0");
                    insDateMap.put("ADD_MONTHS", CommonUtil.convertObjToInt("-1"));
                    List insDateLst = ClientUtil.executeQuery("getMDSCurrentInsDate", insDateMap);
                    System.out.println("#######insDateLst ===" + insDateLst.size() + "insDateLst ===" + insDateLst);
                    if (insDateLst != null && insDateLst.size() > 0) {
                        insDateMap = (HashMap) insDateLst.get(0);
                        System.out.println("#######insDateMap ===" + insDateMap);
                        curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
                        pendingInst = curInsNo - noOfInsPaid;
                        if (instDay < currDate.getDate()) {
                            pendingInst = pendingInst + 1;
                        } else {
                            pendingInst = pendingInst;
                        }
                        if (pendingInst < 0) {
                            pendingInst = 0;
                        }
                        insMonth = startMonth + curInsNo;
                        insDate.setMonth(insMonth);
                    }

                    HashMap prizedMap = new HashMap();
                    prizedMap.put("SCHEME_NAME", prId);
                    prizedMap.put("DIVISION_NO", String.valueOf(divNo));
                    prizedMap.put("CHITTAL_NO", chittalNo);
                    prizedMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                    lst = ClientUtil.executeQuery("getSelectPrizedDetailsEntryRecords", prizedMap);
                    System.out.println("#######prizedMap ===" + lst.size() + "prizedMap ===" + lst);
                    if (lst != null && lst.size() > 0) {
                        prizedMap = (HashMap) lst.get(0);
                        if (prizedMap.get("DRAW") != null && !prizedMap.get("DRAW").equals("") && prizedMap.get("DRAW").equals("Y")) {
                            setRdoPrizedMember_Yes(true);
                        }
                        if (prizedMap.get("AUCTION") != null && !prizedMap.get("AUCTION").equals("") && prizedMap.get("AUCTION").equals("Y")) {
                            setRdoPrizedMember_Yes(true);
                        }
                    } else {
                        setRdoPrizedMember_No(true);
                    }
                    System.out.println("#######totIns ===" + totIns + " noOfInsPaid==" + noOfInsPaid);
                    int balanceIns = totIns - noOfInsPaid;
                    if (balanceIns >= noOfInstPay) {
                        long totDiscAmt = 0;
                        long penalAmt = 0;
                        double netAmt = 0;
                        double insAmtPayable = 0;
                        double totBonusAmt = 0;
                        double bonusAmt = 0;
                        String penalIntType = "";
                        long penalValue = 0;
                        String penalGraceType = "";
                        long penalGraceValue = 0;
                        String penalCalcBaseOn = "";
                        if (pendingInst > 0) {              //pending installment calculation starts...
                            insDueAmt = (long) insAmt * pendingInst;
                            int totPendingInst = (int) pendingInst;
                            double calc = 0;
                            long totInst = pendingInst;
                            penalCalcBaseOn = CommonUtil.convertObjToStr(productMap.get("PENAL_CALC"));
                            System.out.println("#######penalCalcBaseOn ===" + penalCalcBaseOn +"calculateIntOn :"+calculateIntOn);
                            if (getRdoPrizedMember_Yes() == true) {
                                if (productMap.containsKey("PENEL_PZ_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT") != null) {
                                    calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT"));
                                }
                                penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_INT_TYPE"));
                                penalValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_INT_AMT"));
                                penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_GRACE_PERIOD_TYPE"));
                                penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_GRACE_PERIOD"));
                            } else if (getRdoPrizedMember_No() == true) {
                                if (productMap.containsKey("PENEL_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_INT_FULL_AMT_INST_AMT") != null) {
                                    calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_INT_FULL_AMT_INST_AMT"));
                                }
                                penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_INT_TYPE"));
                                penalValue = CommonUtil.convertObjToLong(productMap.get("PENAL_INT_AMT"));
                                penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_GRACE_PERIOD_TYPE"));
                                penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_GRACE_PERIOD"));
                            }
                            List bonusAmout = new ArrayList();
                            System.out.println("calculateIntOn" + calculateIntOn + " inst amt :" + productMap.get("INSTALLMENT_AMOUNT"));
                            if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                                //double instAmount = 0.0;
                                HashMap nextInstMaps = null;
                                for (int i = startNoForPenal; i <= noOfInstPay - addNo; i++) {
                                    nextInstMaps = new HashMap();
                                    nextInstMaps.put("SCHEME_NAME", prId);
                                    nextInstMaps.put("DIVISION_NO", divNo);
                                    nextInstMaps.put("SL_NO", new Double(i + noOfInsPaid + addNo + firstInst_No));
                                    List listRec = ClientUtil.executeQuery("getSelectBonusAndNextInstDateWithoutDivision", nextInstMaps);
                                    if (listRec != null && listRec.size() > 0) {
                                        nextInstMaps = (HashMap) listRec.get(0);
                                    }
                                    System.out.println("nextInstMaps" + nextInstMaps);
                                    if (nextInstMaps != null && nextInstMaps.containsKey("NEXT_BONUS_AMOUNT")) {
                                        bonusAmout.add(CommonUtil.convertObjToDouble(nextInstMaps.get("NEXT_BONUS_AMOUNT")));
                                    } else {
                                        bonusAmout.add(CommonUtil.convertObjToDouble(0));
                                    }
                                }
                            }
                            System.out.println("Nidhin List:" + bonusAmout);
                            for (int j = startNoForPenal; j < noOfInstPay + startNoForPenal; j++) {
                                if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                                    insAmt = 0.0;
                                    double instAmount = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT"));
                                    if (bonusAmout != null && bonusAmout.size() > 0) {
                                        instAmount -= CommonUtil.convertObjToDouble(bonusAmout.get(j - 1));
                                    }
                                    insAmt = instAmount;
                                }
                                HashMap nextInstMap = new HashMap();
                                nextInstMap.put("SCHEME_NAME", prId);
                                nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                                nextInstMap.put("SL_NO", new Double(j + noOfInsPaid));
                                List listRec = ClientUtil.executeQuery("getSelectNextInstDate", nextInstMap);
                                if (listRec != null && listRec.size() > 0) {
                                    double penal = 0;
                                    nextInstMap = (HashMap) listRec.get(0);
                                    instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                                    if (instDay > 0) {
                                        instDate.setDate(instDate.getDate() + instDay - 1);
                                    }
                                    diffDayPending = DateUtil.dateDiff(instDate, currDate);
                                    //Holiday Checking - Added By Suresh
                                    HashMap holidayMap = new HashMap();
                                    boolean checkHoliday = true;
                                    System.out.println("instDate   " + instDate);
                                    instDate = setProperDtFormat(instDate);
                                    System.out.println("instDate   " + instDate);
                                    holidayMap.put("NEXT_DATE", instDate);
                                    holidayMap.put("BRANCH_CODE", getSelectedBranchID());
                                    while (checkHoliday) {
                                        boolean tholiday = false;
                                        List Holiday = ClientUtil.executeQuery("checkHolidayDateOD", holidayMap);
                                        List weeklyOf = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
                                        boolean isHoliday = Holiday.size() > 0 ? true : false;
                                        boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                                        if (isHoliday || isWeekOff) {
                                            System.out.println("#### diffDayPending Holiday True : " + diffDayPending);
                                            if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                                diffDayPending -= 1;
                                                instDate.setDate(instDate.getDate() + 1);
                                            } else {
                                                diffDayPending += 1;
                                                instDate.setDate(instDate.getDate() - 1);
                                            }
                                            holidayMap.put("NEXT_DATE", instDate);
                                            checkHoliday = true;
                                            System.out.println("#### holidayMap : " + holidayMap);
                                        } else {
                                            System.out.println("#### diffDay Holiday False : " + diffDayPending);
                                            checkHoliday = false;
                                        }
                                    }
                                    System.out.println("#### diffDayPending Final : " + diffDayPending);
                                    if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Days")) {
                                        if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                            if (diffDayPending > penalGraceValue) {
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    calc += (diffDayPending * penalValue * insAmt) / 36500;
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                                    calc += penalValue;
                                                }
                                            }
                                        } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                            // To be written
                                            if (diffDayPending > penalGraceValue) {
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                                    calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                                    calc += penalValue;
                                                }
                                            }
                                        }
                                    } else if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Installments")) {
                                        if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                            if (diffDayPending > penalGraceValue) {
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    calc += ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute") && totInst <= noOfInstPay) {
                                                    calc += penalValue;
                                                }
                                            }
                                        } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                            // To be written
                                            if (diffDayPending > penalGraceValue) {
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                                    calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                                    calc += penalValue;
                                                }
                                            }
                                        }
                                    }

                                    //After Scheme End Date Penal Calculating
                                    if ((j + 1 == noOfInstPay + startNoForPenal) && !(penalCalcBaseOn.equals("Days") && penalIntType.equals("Percent")) && (DateUtil.dateDiff(endDate, currDate) > 0)) {
                                        System.out.println("#### endDate : " + endDate);
                                        if (penalIntType.equals("Percent")) {
                                            diffDayPending = DateUtil.dateDiff(endDate, currDate);
                                            System.out.println("#### endDate_diffDayPending : " + diffDayPending);
                                            calc += (double) ((((insAmt * noOfInstPay * penalValue) / 100.0) * diffDayPending) / 365);
                                        }
                                        // Absolute Not Required...
                                    }

                                    penal = (calc + 0.5) - penal;
                                    nextInstMap.put("PENAL", String.valueOf(penal));
                                    installmentMap.put(String.valueOf(j + noOfInsPaid + addNo), nextInstMap);
                                    penal = calc + 0.5;
                                }
                            }
                            if (calc > 0) {
                                penalAmt = (long) (calc + 0.5);

                            }
                        }//pending installment calculation ends...


                        //Discount calculation details Starts...
                        for (int k = 0; k < noOfInstPay; k++) {
                            HashMap nextInstMap = new HashMap();
                            nextInstMap.put("SCHEME_NAME", prId);
                            nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                            nextInstMap.put("SL_NO", new Double(k + noOfInsPaid));
                            List listRec = ClientUtil.executeQuery("getSelectNextInstDate", nextInstMap);
                            if (listRec == null || listRec.size() == 0) {
                                Date curDate = (Date) currDt.clone();
                                int curMonth = curDate.getMonth();
                                curDate.setMonth(curMonth + 1);
                                curDate.setDate(instDay);
                                listRec = new ArrayList();
                                nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                                listRec.add(nextInstMap);
                            }
                            if (listRec != null && listRec.size() > 0) {
                                long discountAmt = 0;
                                nextInstMap = (HashMap) listRec.get(0);
                                instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                                long diffDay = DateUtil.dateDiff(instDate, currDate);
                                //Holiday Checking - Added By Suresh
                                HashMap holidayMap = new HashMap();
                                boolean checkHoliday = true;
                                System.out.println("instDate   " + instDate);
                                instDate = setProperDtFormat(instDate);
                                System.out.println("instDate   " + instDate);
                                holidayMap.put("NEXT_DATE", instDate);
                                holidayMap.put("BRANCH_CODE", getSelectedBranchID());
                                while (checkHoliday) {
                                    boolean tholiday = false;
                                    System.out.println("enterytothecheckholiday" + checkHoliday);
                                    List Holiday = ClientUtil.executeQuery("checkHolidayDateOD", holidayMap);
                                    List weeklyOf = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
                                    boolean isHoliday = Holiday.size() > 0 ? true : false;
                                    boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                                    if (isHoliday || isWeekOff) {
                                        System.out.println("#### diffDay Holiday True : " + diffDay);
                                        if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                            diffDay -= 1;
                                            instDate.setDate(instDate.getDate() + 1);
                                        } else {
                                            diffDay += 1;
                                            instDate.setDate(instDate.getDate() - 1);
                                        }
                                        holidayMap.put("NEXT_DATE", instDate);
                                        checkHoliday = true;
                                        System.out.println("#### holidayMap : " + holidayMap);
                                    } else {
                                        System.out.println("#### diffDay Holiday False : " + diffDay);
                                        checkHoliday = false;
                                    }
                                }
                                if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y")
                                        || productMap.get("BONUS_ALLOWED").equals("N"))) {
                                    String discount = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_ALLOWED"));
                                    if (discount != null && !discount.equals("") && discount.equals("Y")) {
                                        String discountType = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_RATE_TYPE"));
                                        long discountValue = CommonUtil.convertObjToLong(productMap.get("DISCOUNT_RATE_AMT"));
                                        if (getRdoPrizedMember_Yes() == true) {//discount calculation for prized prerson...
                                            String discountPrizedDays = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_DAYS"));
                                            String discountPrizedMonth = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_MONTHS"));
                                            String discountPrizedAfter = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_AFTER"));
                                            String discountPrizedEnd = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_END"));
                                            long discountPrizedValue = CommonUtil.convertObjToLong(productMap.get("DIS_PRIZED_GRACE_PERIOD"));
                                            if (discountPrizedDays != null && !discountPrizedDays.equals("") && discountPrizedDays.equals("D") && diffDay <= discountPrizedValue) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    if (diffDay <= discountPrizedValue) {
                                                        totDiscAmt = totDiscAmt + calc;
                                                    }
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    if (diffDay <= discountPrizedValue) {
                                                        totDiscAmt = totDiscAmt + discountValue;
                                                    }
                                                }
                                            } else if (discountPrizedMonth != null && !discountPrizedMonth.equals("") && discountPrizedMonth.equals("M") && diffDay <= (discountPrizedValue * 30)) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountPrizedAfter != null && !discountPrizedAfter.equals("") && discountPrizedAfter.equals("A") && currDate.getDate() <= discountPrizedValue) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountPrizedEnd != null && !discountPrizedEnd.equals("") && discountPrizedEnd.equals("E") && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else {
                                                totDiscAmt = 0;
                                            }
                                        } else if (getRdoPrizedMember_No() == true) {//discount calculation non prized person...
                                            String discountGraceDays = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_DAYS"));
                                            String discountGraceMonth = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_MONTHS"));
                                            String discountGraceAfter = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_AFTER"));
                                            String discountGraceEnd = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_END"));
                                            long discountGraceValue = CommonUtil.convertObjToLong(productMap.get("DIS_GRACE_PERIOD"));
                                            if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("D")) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    if (diffDay <= discountGraceValue) {
                                                        totDiscAmt = totDiscAmt + calc;
                                                    }
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    if (diffDay <= discountGraceValue) {
                                                        totDiscAmt = totDiscAmt + discountValue;
                                                    }
                                                } else {
                                                    totDiscAmt = 0;
                                                }
                                            } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("M") && diffDay <= discountGraceValue * 30 && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("A") && currDate.getDate() <= discountGraceValue && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("E") && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else {
                                                totDiscAmt = 0;
                                            }
                                        }
                                    } else if (discount != null && !discount.equals("") && discount.equals("N")) {
                                        totDiscAmt = 0;
                                    }
                                    discountAmt = totDiscAmt - discountAmt;
                                    HashMap instMap = new HashMap();
                                    if (installmentMap.containsKey(String.valueOf(k + noOfInsPaid + 1))) {
                                        instMap = (HashMap) installmentMap.get(String.valueOf(k + noOfInsPaid + 1));
                                        instMap.put("DISCOUNT", String.valueOf(discountAmt));
                                        installmentMap.put(String.valueOf(k + noOfInsPaid + 1), instMap);
                                    }
                                    discountAmt = totDiscAmt;
                                }

                            }
                        }


                        //Bonus calculation details Starts...
                        for (int l = startNoForPenal; l <= noOfInstPay - addNo; l++) {
                            //                    for(int l = 1;l<=noOfInstPay;l++){
                            HashMap nextInstMap = new HashMap();
                            nextInstMap.put("SCHEME_NAME", prId);
                            nextInstMap.put("DIVISION_NO", divNo);
                            nextInstMap.put("SL_NO", new Double(l + noOfInsPaid + addNo + firstInst_No));
                            List listRec = ClientUtil.executeQuery("getSelectBonusAndNextInstDateWithoutDivision", nextInstMap);
                            if (listRec == null || listRec.size() == 0) {
                                Date curDate = (Date) currDt.clone();
                                int curMonth = curDate.getMonth();
                                curDate.setMonth(curMonth + 1);
                                curDate.setDate(instDay);
                                listRec = new ArrayList();
                                nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                                listRec.add(nextInstMap);
                                bonusAvailabe = false;
                            }
                            if (listRec != null && listRec.size() > 0) {
                                nextInstMap = (HashMap) listRec.get(0);
                                instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                                bonusAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_BONUS_AMOUNT"))).doubleValue();
                                if (!productMap.get("MULTIPLE_MEMBER").equals("") && (productMap.get("MULTIPLE_MEMBER").equals("Y"))) {
                                    whereMap = new HashMap();
                                    int noOfCoChittal = 0;
                                    whereMap.put("SCHEME_NAME", prId);
                                    whereMap.put("CHITTAL_NUMBER", chittalNo);
                                    List applicationLst = ClientUtil.executeQuery("getSelectChitNoNotinMasterDetails", whereMap); // Count No Of Co-Chittals
                                    if (applicationLst != null && applicationLst.size() > 0) {
                                        noOfCoChittal = applicationLst.size();
                                        bonusAmt = bonusAmt / noOfCoChittal;
                                    }
                                }
                                if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                        && bonusAmt > 0) {
                                    Rounding rod = new Rounding();
                                    bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                                }
                                long diffDay = DateUtil.dateDiff(instDate, currDate);
                                //Holiday Checking - Added By Suresh
                                HashMap holidayMap = new HashMap();
                                boolean checkHoliday = true;
                                System.out.println("instDate   " + instDate);
                                instDate = setProperDtFormat(instDate);
                                System.out.println("instDate   " + instDate);
                                holidayMap.put("NEXT_DATE", instDate);
                                holidayMap.put("BRANCH_CODE", getSelectedBranchID());
                                while (checkHoliday) {
                                    boolean tholiday = false;
                                    System.out.println("enterytothecheckholiday" + checkHoliday);
                                    List Holiday = ClientUtil.executeQuery("checkHolidayDateOD", holidayMap);
                                    List weeklyOf = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
                                    boolean isHoliday = Holiday.size() > 0 ? true : false;
                                    boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                                    if (isHoliday || isWeekOff) {
                                        System.out.println("#### diffDay Holiday True : " + diffDay);
                                        if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                            diffDay -= 1;
                                            instDate.setDate(instDate.getDate() + 1);
                                        } else {
                                            diffDay += 1;
                                            instDate.setDate(instDate.getDate() - 1);
                                        }
                                        holidayMap.put("NEXT_DATE", instDate);
                                        checkHoliday = true;
                                        System.out.println("#### holidayMap : " + holidayMap);
                                    } else {
                                        System.out.println("#### diffDay Holiday False : " + diffDay);
                                        checkHoliday = false;
                                    }
                                }
                                //                            String hoildayInt = CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT"));
                                if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y"))) {
                                    if (bonusAvailabe == true) {
                                        if (getRdoPrizedMember_Yes() == true) {
                                            String bonusPrizedDays = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_DAYS"));
                                            String bonusPrizedMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
                                            String bonusPrizedAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_AFT"));
                                            String bonusPrizedEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_END"));
                                            long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
                                            if (bonusPrizedDays != null && !bonusPrizedDays.equals("") && bonusPrizedDays.equals("D") && diffDay <= bonusPrizedValue) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusPrizedMonth != null && !bonusPrizedMonth.equals("") && bonusPrizedMonth.equals("M") && diffDay <= (bonusPrizedValue * 30)) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusPrizedAfter != null && !bonusPrizedAfter.equals("") && bonusPrizedAfter.equals("A") && currDate.getDate() <= bonusPrizedValue) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusPrizedEnd != null && !bonusPrizedEnd.equals("") && bonusPrizedEnd.equals("E")) {
                                            } else {
                                            }
                                        } else if (getRdoPrizedMember_No() == true) {
                                            String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
                                            String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
                                            String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
                                            String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
                                            long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
                                            if (bonusGraceDays != null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay <= bonusGraceValue) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusGraceMonth != null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay <= (bonusGraceValue * 30)) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusGraceOnAfter != null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && currDate.getDate() <= bonusGraceValue) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusGraceEnd != null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E")) {
                                            } else {
                                            }
                                        }
                                    }
                                    HashMap instMap = new HashMap();
                                    if (installmentMap.containsKey(String.valueOf(l + noOfInsPaid + addNo))) {
                                        Rounding rod = new Rounding();
                                        instMap = (HashMap) installmentMap.get(String.valueOf(l + noOfInsPaid + addNo));
                                        //Added By Suresh
                                        if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                                && bonusAmt > 0) {
                                            bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                                        }
                                        instMap.put("BONUS", String.valueOf(bonusAmt));
                                        installmentMap.put(String.valueOf(l + noOfInsPaid + addNo), instMap);
                                    }
                                }
                            }
                            bonusAmt = 0;
                        }
                        if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                && totBonusAmt > 0) {
                            Rounding rod = new Rounding();
                            totBonusAmt = (double) rod.getNearest((long) (totBonusAmt * 100), 100) / 100;
                        }

                        int insDay = 0;
                        Date paidUpToDate = null;
                        HashMap instDateMap = new HashMap();
                        instDateMap.put("SCHEME_NAME", prId);
                        instDateMap.put("DIVISION_NO", divNo);
                        instDateMap.put("INSTALLMENT_NO", CommonUtil.convertObjToInt(String.valueOf(count)));
                        lst = ClientUtil.executeQuery("getSelectInstUptoPaid", instDateMap);
                        if (lst != null && lst.size() > 0) {
                            instDateMap = (HashMap) lst.get(0);
                            paidUpToDate = setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(instDateMap.get("NEXT_INSTALLMENT_DATE"))));
                        } else {
                            Date startedDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("SCHEME_START_DT")));
                            insDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                            startedDate.setDate(insDay);
                            int stMonth = startedDate.getMonth();
                            startedDate.setMonth(stMonth + (int) count - 1);
                            paidUpToDate = setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(startedDate)));
                        }

                        String narration = "";
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM/yyyy");
                        int noInstPay = CommonUtil.convertObjToInt(noOfInsPay);
                        if (noInstPay == 1) {
                            narration = "Inst#" + (noOfInsPaid + 1);
                            Date dt = DateUtil.addDays(paidUpToDate, 30);
                            narration += " " + sdf.format(dt);
                        } else if (noInstPay > 1) {
                            narration = "Inst#" + (noOfInsPaid + 1);
                            narration += "-" + (noOfInsPaid + noInstPay);
                            Date dt = DateUtil.addDays(paidUpToDate, 30);
                            narration += " " + sdf.format(dt);
                            dt = DateUtil.addDays(paidUpToDate, 30 * noInstPay);
                            narration += " To " + sdf.format(dt);
                        }
                        System.out.println("#$#$# narration :" + narration);
                        if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                            insAmt = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT"));
                        }
                        double instAmt = insAmt * CommonUtil.convertObjToDouble(noOfInsPay).doubleValue();
                        double totalPayable = instAmt - (totBonusAmt + totDiscAmt);
                        netAmt = totalPayable + penalAmt + CommonUtil.convertObjToDouble(noticAmt).doubleValue()
                                + CommonUtil.convertObjToDouble(arbitAmt).doubleValue();

                        dataMap.put("SCHEME_NAME", prId);
                        dataMap.put("CHITTAL_NO", chittalNo);
                        dataMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                        dataMap.put("MEMBER_NAME", "");//lblMemberName.getText()
                        dataMap.put("DIVISION_NO", String.valueOf(divNo));
                        dataMap.put("CHIT_START_DT", startDate);
                        dataMap.put("INSTALLMENT_DATE", insDate);
                        dataMap.put("NO_OF_INSTALLMENTS", String.valueOf(totIns));
                        dataMap.put("CURR_INST", String.valueOf(curInsNo));
                        dataMap.put("PENDING_INST", insDue);
                        dataMap.put("PENDING_DUE_AMT", String.valueOf(insDueAmt));
                        dataMap.put("NO_OF_INST_PAY", noOfInsPay);
                        dataMap.put("INST_AMT_PAYABLE", String.valueOf(totalPayable));
                        dataMap.put("PRINCIPAL", String.valueOf(totalPayable));
                        dataMap.put("PAID_INST", String.valueOf(noOfInsPaid));
                        dataMap.put("TOTAL_PAYABLE", String.valueOf(instAmt));
                        dataMap.put("PAID_DATE", currDate);
                        dataMap.put("INST_AMT", String.valueOf(insAmt));
                        dataMap.put("SCHEME_END_DT", endDate);
                        if (getRdoPrizedMember_Yes() == true) {
                            dataMap.put("PRIZED_MEMBER", "Y");
                        } else {
                            dataMap.put("PRIZED_MEMBER", "N");
                        }
                        dataMap.put("BONUS", String.valueOf(totBonusAmt));
                        dataMap.put("DISCOUNT", String.valueOf(totDiscAmt));
                        dataMap.put("PENAL", String.valueOf(penalAmt));
                        dataMap.put("NOTICE_AMOUNT", noticAmt);
                        dataMap.put("ARBITRATION_AMOUNT", arbitAmt);
                        dataMap.put("NET_AMOUNT", String.valueOf(netAmt));
                        dataMap.put("NARRATION", narration);
                        dataMap.put("INSTALLMENT_MAP", installmentMap);
                        dataMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                        finalMap1.put(chittalNo, dataMap);

                        //                    System.out.println("###### insAmt        : "+insAmt);
                        //                    System.out.println("###### insAmtPayable : "+instAmt);
                        //                    System.out.println("###### totBonusAmt   : "+totBonusAmt);
                        //                    System.out.println("###### totDiscAmt    : "+totDiscAmt);
                        //                    System.out.println("###### totalPayable  : "+totalPayable);
                        //                    System.out.println("###### penalAmt      : "+penalAmt);
                        //                    System.out.println("###### netAmt        : "+netAmt);




                    } else {
                        ClientUtil.showAlertWindow("Exceeds The No Of Total Installment !!! ");

                    }
                }
            }

        } catch (Exception e) {
            parseException.logException(e, true);
        }
        return dataMap;
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDate.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    public HashMap getLoanDetails(HashMap where, String prodid, String actnum, double amt,String prodtype) {

        //  HashMap otherChargesMap=null;
        //   String penalWaiveOff="";
        //get a/c no scheme,
        ArrayList alist1 = new ArrayList();
        // ArrayList alist1=new ArrayList();
        String scheme_Name = prodtype;
        String prod_id = prodid;
        String acNo = actnum;
        double transAmt = amt;
        double principal = 0.0;
        double interest = 0.0;
        double penal = 0.0;
        double charges = 0.0;
        double clearBalance = 0.0;
        if (returnMap.containsKey("RECOVERY_LIST_TABLE_DATA")) {
            System.out.println("hai..." + returnMap.get("RECOVERY_LIST_TABLE_DATA"));
            HashMap newMap = new HashMap();
            //tableList = (ArrayList) whereMap.get("INSERT")
            alist1 = (ArrayList) returnMap.get("RECOVERY_LIST_TABLE_DATA");
            List alist = (List) alist1.get(0);
            //alist1=(ArrayList)alist.get(0);
            String j = alist.get(3).toString();

            principal = CommonUtil.convertObjToDouble(alist.get(3).toString());
            interest = CommonUtil.convertObjToDouble(alist.get(5).toString());
            penal = CommonUtil.convertObjToDouble(alist.get(4).toString());
            charges = CommonUtil.convertObjToDouble(alist.get(6).toString());
            clearBalance = CommonUtil.convertObjToDouble(alist.get(7).toString());
        }
        double paidPrincipal = 0;
        double paidInterest = 0;
        double paidPenal = 0;
        double paidCharges = 0;

        System.out.println("LOAN scheme_Name=" + scheme_Name + " prod_id=" + prod_id + " acNo=" + acNo + " transAmt=" + transAmt + "principal=" + principal);
        try {
            //             double transAmt=Double.parseDouble(loanAmt);
            HashMap ALL_LOAN_AMOUNT = new HashMap();
            HashMap hashList = new HashMap();
            hashList.put(CommonConstants.MAP_WHERE, prod_id);
            List appList = ClientUtil.executeQuery("selectAppropriatTransaction", hashList);
            HashMap appropriateMap = new HashMap();
            if (appList != null && appList.size() > 0) {
                appropriateMap = (HashMap) appList.get(0);
                appropriateMap.remove("PROD_ID");
            } else {
                throw new TTException("Please Enter Hierachy of Transaction  in This Product ");
            }
            System.out.println("appropriateMap####" + appropriateMap);
            java.util.Collection collectedValues = appropriateMap.values();
            java.util.Iterator it = collectedValues.iterator();
            //CashTransactionTO objCashTO =new CashTransactionTO();
            int appTranValue = 0;
            while (it.hasNext()) {
                appTranValue++;
                String hierachyValue = CommonUtil.convertObjToStr(it.next());
                System.out.println("hierachyValue####" + hierachyValue);
                //objCashTO = setCashTransaction(objCashTransactionTO);
                if (hierachyValue.equals("CHARGES")) {
                    if (transAmt > 0 && charges > 0) {
                        if (transAmt >= charges) {
                            transAmt -= charges;
                            paidCharges = charges;
                        } else {
                            paidCharges = transAmt;
                            transAmt -= charges;
                        }

                    }
                }
                //Account Closing Charges
                //                CashTransactionTO objCashTO = setCashTransaction(objCashTransactionTO);

                //
                if (hierachyValue.equals("PENALINTEREST")) {
                    //penal interest
                    if (transAmt > 0 && penal > 0) {
                        if (transAmt >= penal) {
                            transAmt -= penal;
                            paidPenal = penal;
                        } else {
                            paidPenal = transAmt;
                            transAmt -= penal;
                        }

                    }
                }
                if (hierachyValue.equals("INTEREST")) {
                    //interest
                    if (transAmt > 0 && interest > 0) {
                        if (transAmt >= interest) {
                            transAmt -= interest;
                            paidInterest = interest;
                        } else {
                            paidInterest = transAmt;
                            transAmt -= interest;
                        }

                    }
                }
                if (hierachyValue.equals("PRINCIPAL")) {
                    if (transAmt > 0 && principal > 0) {
                        if (transAmt >= principal) {
                            transAmt -= principal;
                            paidPrincipal = principal;
                        } else {
                            paidPrincipal = transAmt;
                            transAmt -= principal;
                        }

                    }
                }
            }
            //             tblTransaction.setValueAt(new Double(paidPrincipal), tblTransaction.getSelectedRow(),3);
            //             tblTransaction.setValueAt(new Double(paidInterest), tblTransaction.getSelectedRow(),5);
            //             tblTransaction.setValueAt(new Double(paidPenal), tblTransaction.getSelectedRow(),4);
            //             tblTransaction.setValueAt(new Double(paidCharges), tblTransaction.getSelectedRow(),7);
            paidPenal = Math.round(paidPenal);
            paidInterest = Math.round(paidInterest);
            paidCharges = Math.round(paidCharges);
            paidPrincipal = Math.round(paidPrincipal);
            System.out.println("here values penal "+paidPenal+"pinter "+paidInterest+"paidchrg "+paidCharges);
            ArrayList newList = new ArrayList();
            newList.add(CommonUtil.convertObjToStr(scheme_Name));
            newList.add(CommonUtil.convertObjToStr(acNo));
            newList.add((""));
            newList.add(CommonUtil.convertObjToStr(paidPrincipal));
            newList.add(CommonUtil.convertObjToStr(paidPenal));
            newList.add(CommonUtil.convertObjToStr(paidInterest));
            newList.add(CommonUtil.convertObjToStr(paidCharges));
            Double total1 = ((paidPrincipal + paidPenal + paidInterest + paidCharges));
            returnMap.put("RECOVERY_LIST_TABLE_DATA", newList);
            returnMap.put("TOTAL", total1);
            HashMap dataMap1 = new HashMap();
            // dataMap.put("BRANCH_CODE",CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
            if (paidCharges > 0) {
                dataMap1.put("CHARGES", String.valueOf(paidCharges));
            } else {
                dataMap1.put("CHARGES", "0");
            }
            dataMap1.put("PRINCIPAL", String.valueOf(paidPrincipal));
            dataMap1.put("INTEREST", String.valueOf(paidInterest));
            dataMap1.put("PENAL", String.valueOf(paidPenal));
            //   totalDemand=paidPrincipal+paidInterest+paidPenal+paidCharges;
            // dataMap1.put("TOTAL_DEMAND", new Double(totalDemand));
            dataMap1.put("PROD_TYPE", scheme_Name);
            dataMap1.put("ACT_NUM", acNo);
            dataMap1.put("RECOVERED_AMOUNT", total1);
            dataMap1.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            dataMap1.put("USER_ID", TrueTransactMain.USER_ID);
            dataMap1.put("PROD_ID", prod_id);
            dataMap1.put("TOTAL", total1);
            dataMap1.put("DEBIT_ACT_NUM", db_actnum);
            dataMap1.put("DEBIT_PROD_ID", db_prodid);
            dataMap1.put("CLEAR_BALANCE", clearBalance);
            returnMap.put("DATAMAP", dataMap1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception in getLoanDetails: " + e);
        }
        return returnMap;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panDepositInterestApplication = new com.see.truetransact.uicomponent.CPanel();
        panProductDetails = new com.see.truetransact.uicomponent.CPanel();
        lblProcessDate = new com.see.truetransact.uicomponent.CLabel();
        tdtProcessDate = new com.see.truetransact.uicomponent.CDateField();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        panProductTableData = new com.see.truetransact.uicomponent.CPanel();
        srpTransExceptionDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblTransExceptionDetails = new com.see.truetransact.uicomponent.CTable();
        panProcess = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        txtTokenNo = new com.see.truetransact.uicomponent.CTextField();
        lblTotalTransactionAmt = new com.see.truetransact.uicomponent.CLabel();
        lblTotalTransactionAmtVal = new com.see.truetransact.uicomponent.CLabel();
        panSelectAll = new com.see.truetransact.uicomponent.CPanel();
        lblSelectAll = new com.see.truetransact.uicomponent.CLabel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        panReprintDetails = new com.see.truetransact.uicomponent.CPanel();
        lblReprintDate = new com.see.truetransact.uicomponent.CLabel();
        tdtReprintDate = new com.see.truetransact.uicomponent.CDateField();
        btnReprint = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(860, 620));
        setMinimumSize(new java.awt.Dimension(860, 620));
        setPreferredSize(new java.awt.Dimension(860, 620));

        panDepositInterestApplication.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panDepositInterestApplication.setMaximumSize(new java.awt.Dimension(800, 450));
        panDepositInterestApplication.setMinimumSize(new java.awt.Dimension(800, 450));
        panDepositInterestApplication.setPreferredSize(new java.awt.Dimension(800, 450));
        panDepositInterestApplication.setLayout(new java.awt.GridBagLayout());

        panProductDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Process"));
        panProductDetails.setMaximumSize(new java.awt.Dimension(400, 110));
        panProductDetails.setMinimumSize(new java.awt.Dimension(400, 110));
        panProductDetails.setPreferredSize(new java.awt.Dimension(400, 110));
        panProductDetails.setLayout(new java.awt.GridBagLayout());

        lblProcessDate.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panProductDetails.add(lblProcessDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panProductDetails.add(tdtProcessDate, gridBagConstraints);

        btnProcess.setText("PROCESS");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(30, 3, 3, 3);
        panProductDetails.add(btnProcess, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -88;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 31, 0, 0);
        panDepositInterestApplication.add(panProductDetails, gridBagConstraints);

        panProductTableData.setBorder(javax.swing.BorderFactory.createTitledBorder("Process Details"));
        panProductTableData.setMinimumSize(new java.awt.Dimension(830, 360));
        panProductTableData.setPreferredSize(new java.awt.Dimension(830, 360));
        panProductTableData.setLayout(new java.awt.GridBagLayout());

        srpTransExceptionDetails.setMinimumSize(new java.awt.Dimension(810, 335));
        srpTransExceptionDetails.setPreferredSize(new java.awt.Dimension(810, 335));

        tblTransExceptionDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Cust ID", "Clock_No", "Name", "Product Type", "Product Id", "Account No", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblTransExceptionDetails.setCellSelectionEnabled(true);
        tblTransExceptionDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 331));
        tblTransExceptionDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTransExceptionDetailsMouseClicked(evt);
            }
        });
        tblTransExceptionDetails.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                tblTransExceptionDetailsInputMethodTextChanged(evt);
            }
        });
        srpTransExceptionDetails.setViewportView(tblTransExceptionDetails);
        tblTransExceptionDetails.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        panProductTableData.add(srpTransExceptionDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.ipadx = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 12);
        panDepositInterestApplication.add(panProductTableData, gridBagConstraints);

        panProcess.setMinimumSize(new java.awt.Dimension(780, 30));
        panProcess.setPreferredSize(new java.awt.Dimension(780, 30));
        panProcess.setLayout(new java.awt.GridBagLayout());

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnClose, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnClear, gridBagConstraints);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnSave.setText("SAVE");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 120, 4, 4);
        panProcess.add(btnSave, gridBagConstraints);

        txtTokenNo.setMinimumSize(new java.awt.Dimension(50, 21));
        txtTokenNo.setPreferredSize(new java.awt.Dimension(50, 21));
        txtTokenNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTokenNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 6);
        panProcess.add(txtTokenNo, gridBagConstraints);

        lblTotalTransactionAmt.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 4);
        panProcess.add(lblTotalTransactionAmt, gridBagConstraints);

        lblTotalTransactionAmtVal.setMinimumSize(new java.awt.Dimension(100, 21));
        lblTotalTransactionAmtVal.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProcess.add(lblTotalTransactionAmtVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = -71;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 13, 0);
        panDepositInterestApplication.add(panProcess, gridBagConstraints);

        panSelectAll.setLayout(new java.awt.GridBagLayout());

        lblSelectAll.setText("Select All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 19, 5, 0);
        panSelectAll.add(lblSelectAll, gridBagConstraints);

        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 5, 1);
        panSelectAll.add(chkSelectAll, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 495;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 2, 0, 0);
        panDepositInterestApplication.add(panSelectAll, gridBagConstraints);

        panReprintDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Process"));
        panReprintDetails.setMaximumSize(new java.awt.Dimension(400, 110));
        panReprintDetails.setMinimumSize(new java.awt.Dimension(400, 110));
        panReprintDetails.setPreferredSize(new java.awt.Dimension(400, 110));
        panReprintDetails.setLayout(new java.awt.GridBagLayout());

        lblReprintDate.setText("Reprint Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panReprintDetails.add(lblReprintDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panReprintDetails.add(tdtReprintDate, gridBagConstraints);

        btnReprint.setText("Reprint");
        btnReprint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReprintActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(30, 3, 3, 3);
        panReprintDetails.add(btnReprint, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = -148;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 133, 0, 0);
        panDepositInterestApplication.add(panReprintDetails, gridBagConstraints);
        panReprintDetails.getAccessibleContext().setAccessibleName("Reprint");

        getContentPane().add(panDepositInterestApplication, java.awt.BorderLayout.CENTER);

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

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
        boolean flags;
        if (chkSelectAll.isSelected() == true) {
            flags = true;
        } else {
            flags = false;
        }
        double totAmount = 0;
        for (int i = 0; i < tblTransExceptionDetails.getRowCount(); i++) {
            tblTransExceptionDetails.setValueAt(new Boolean(flags), i, 0);
            //System.out.println("value at 7"+tblTransExceptionDetails.getValueAt(i, 7).toString());
            double tot = Double.parseDouble(tblTransExceptionDetails.getValueAt(i, 7).toString());
            totAmount = totAmount + tot;
        }
        lblTotalTransactionAmtVal.setText(CurrencyValidation.formatCrore(String.valueOf(totAmount)));
        if (flags == false) {
            lblTotalTransactionAmtVal.setText(CurrencyValidation.formatCrore(String.valueOf(0.0)));
        }


    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
    	CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
        @Override
            protected Void doInBackground() throws InterruptedException /**
             * Execute some operation
             */
            {
            clearall();
            getClockNo();
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
        }            
    }//GEN-LAST:event_btnProcessActionPerformed

    private void txtTokenNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTokenNoFocusLost
        // TODO add your handling code here:
//    
    }//GEN-LAST:event_txtTokenNoFocusLost

    private void enableDisable(boolean enable) {
    }

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clearall();

    }//GEN-LAST:event_btnClearActionPerformed
    public void clearall() {
        observable.resetForm();
        enableDisable(false);
        ClientUtil.clearAll(this);
        chkSelectAll.setEnabled(false);
        tdtProcessDate.setDateValue(CommonUtil.convertObjToStr(currDate));
        listTot.clear();
        newList.clear();
        btnSave.setEnabled(false);
        finalMap.clear();
        acMap.clear();
        lblStatus.setText("          ");
        lblTotalTransactionAmtVal.setText("           ");
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if (chkSelectAll.isSelected()) {
            System.out.println("haiiiiii" + fMap.size());
            if (fMap != null && fMap.size() > 0) {
                for (int i = 0; i < fMap.size(); i++) {
                    HashMap sMap = new HashMap();
                    sMap = (HashMap) (fMap.get(i));
                    String acno = "";
                    if (!CommonUtil.convertObjToStr(sMap.get("ACT_NUM")).equals("")) {
                        acno = sMap.get("ACT_NUM").toString();
                    }
                    if (sMap.get("PROD_TYPE").toString().equals("TL")) {
                        acMap = new HashMap();
                        acMap.put("TL", sMap);
                        finalMap.put(acno, acMap);

                    }
                    //added by rishad 23/04/2014 for AD transaction
                    if (sMap.get("PROD_TYPE").toString().equals("AD")) {
                        acMap = new HashMap();
                        acMap.put("AD", sMap);
                        finalMap.put(acno, acMap);

                    } else if (sMap.get("PROD_TYPE").toString().equals("TD")) {
                        acMap = new HashMap();
                        acMap.put("TD", sMap);
                        finalMap.put(acno, acMap);

                    } else if (sMap.get("PROD_TYPE").toString().equals("SA")) {
                        acMap = new HashMap();
                        acMap.put("SA", sMap);
                        finalMap.put(acno, acMap);

                    } else if (sMap.get("PROD_TYPE").toString().equals("MDS")) {
                        System.out.println("haiiiiii smap size" + sMap.size());
                        acMap = new HashMap();
                        acMap.put("MDS", sMap);
                        finalMap.put(acno, acMap);
                    }

                }
            }

        } else {
            finalMap = new HashMap();
            acMap.clear();
            HashMap chkMap = null;
            newList.clear();
            boolean chk = false;
            for (int i = 0; i < tblTransExceptionDetails.getRowCount(); i++) {
                if (((Boolean) tblTransExceptionDetails.getValueAt(i, 0)).booleanValue()) {

                    String clockno = tblTransExceptionDetails.getValueAt(i, 2).toString();
                    String actnum = tblTransExceptionDetails.getValueAt(i, 6).toString();
                    for (int j = 0; j < fMap.size(); j++) {
                        System.out.println("fMap in seee" + fMap.size());
                        chkMap = new HashMap();
                        chkMap = (HashMap) (fMap.get(j));
                        if ((chkMap.get("ACT_NUM").toString() != null)) {
                            if (chkMap.get("ACT_NUM").toString().equals(actnum)) {
                                System.out.println("HAiiiiiiiiiii");
                                chk = true;
                                break;
                            }
                        }
                    }
                    if (chk == true) {
                        listadd = new ArrayList();
                        listadd.add(tblTransExceptionDetails.getValueAt(i, 0));
                        listadd.add(tblTransExceptionDetails.getValueAt(i, 1));
                        listadd.add(tblTransExceptionDetails.getValueAt(i, 2));
                        listadd.add(tblTransExceptionDetails.getValueAt(i, 3));
                        listadd.add(tblTransExceptionDetails.getValueAt(i, 4));
                        listadd.add(tblTransExceptionDetails.getValueAt(i, 14));
                        listadd.add(tblTransExceptionDetails.getValueAt(i, 6));
                        listadd.add(tblTransExceptionDetails.getValueAt(i, 7));
                        listadd.add(tblTransExceptionDetails.getValueAt(i, 8));
                        listadd.add(tblTransExceptionDetails.getValueAt(i, 9));
                        listadd.add(tblTransExceptionDetails.getValueAt(i, 10));
                        listadd.add(tblTransExceptionDetails.getValueAt(i, 11));
                        listadd.add(tblTransExceptionDetails.getValueAt(i, 12));
                        listadd.add(tblTransExceptionDetails.getValueAt(i, 13));
						listadd.add(tblTransExceptionDetails.getValueAt(i, 14));
                        listadd.add(tblTransExceptionDetails.getValueAt(i, 15));
                        newList.add(listadd);
                        if (chkMap.get("PROD_TYPE").toString().equals("TL")) {
                            acMap = new HashMap();
                            chkMap.put("RECOVERED_AMOUNT", tblTransExceptionDetails.getValueAt(i, 7));
                            chkMap.put("PRINCIPAL", tblTransExceptionDetails.getValueAt(i, 9));
                            chkMap.put("INTEREST", tblTransExceptionDetails.getValueAt(i, 10));
                            chkMap.put("PENAL", tblTransExceptionDetails.getValueAt(i, 11));
                            chkMap.put("CHARGES", tblTransExceptionDetails.getValueAt(i, 13));
                            chkMap.put("CLOCK_NO",tblTransExceptionDetails.getValueAt(i, 2));
                            chkMap.put("OLD_AMT",tblTransExceptionDetails.getValueAt(i, 15));
                            acMap.put("TL", chkMap);
                            finalMap.put(actnum, acMap);

                        }
                        //added by rishad 23/04/2014
                        if (chkMap.get("PROD_TYPE").toString().equals("AD")) {
                            acMap = new HashMap();
                            chkMap.put("RECOVERED_AMOUNT", tblTransExceptionDetails.getValueAt(i, 7));
                            chkMap.put("PRINCIPAL", tblTransExceptionDetails.getValueAt(i, 9));
                            chkMap.put("INTEREST", tblTransExceptionDetails.getValueAt(i, 10));
                            chkMap.put("PENAL", tblTransExceptionDetails.getValueAt(i, 11));
                            chkMap.put("CHARGES", tblTransExceptionDetails.getValueAt(i, 13));
                            chkMap.put("CLOCK_NO",tblTransExceptionDetails.getValueAt(i, 2));
                            acMap.put("AD", chkMap);
                            finalMap.put(actnum, acMap);

                        } else if (chkMap.get("PROD_TYPE").toString().equals("TD")) {
                            acMap = new HashMap();
                            chkMap.put("RECOVERED_AMOUNT", tblTransExceptionDetails.getValueAt(i, 7));
                            chkMap.put("PRINCIPAL", tblTransExceptionDetails.getValueAt(i, 9));
                            chkMap.put("INTEREST", tblTransExceptionDetails.getValueAt(i, 10));
                            chkMap.put("PENAL", tblTransExceptionDetails.getValueAt(i, 11));
                            chkMap.put("CHARGES", tblTransExceptionDetails.getValueAt(i, 13));
                            chkMap.put("CLOCK_NO",tblTransExceptionDetails.getValueAt(i, 2));
                            acMap.put("TD", chkMap);
                            finalMap.put(actnum, acMap);

                        } else if (chkMap.get("PROD_TYPE").toString().equals("SA")) {
                            acMap = new HashMap();
                            chkMap.put("RECOVERED_AMOUNT", tblTransExceptionDetails.getValueAt(i, 7));
                            chkMap.put("PRINCIPAL", tblTransExceptionDetails.getValueAt(i, 9));
                            chkMap.put("INTEREST", tblTransExceptionDetails.getValueAt(i, 10));
                            chkMap.put("PENAL", tblTransExceptionDetails.getValueAt(i, 11));
                            chkMap.put("CHARGES", tblTransExceptionDetails.getValueAt(i, 13));
                            chkMap.put("CLOCK_NO",tblTransExceptionDetails.getValueAt(i, 2));
                            acMap.put("SA", chkMap);
                            finalMap.put(actnum, acMap);

                        } else if (chkMap.get("PROD_TYPE").toString().equals("MDS")) {
                            acMap = new HashMap();
                            chkMap.put("RECOVERED_AMOUNT", tblTransExceptionDetails.getValueAt(i, 7));
                            chkMap.put("NO_OF_INST_PAY", tblTransExceptionDetails.getValueAt(i, 8));
                            chkMap.put("PRINCIPAL", tblTransExceptionDetails.getValueAt(i, 9));
                            chkMap.put("INTEREST", tblTransExceptionDetails.getValueAt(i, 10));
                            chkMap.put("PENAL", tblTransExceptionDetails.getValueAt(i, 11));
                            chkMap.put("BONUS", tblTransExceptionDetails.getValueAt(i, 12));
                            chkMap.put("CHARGES", tblTransExceptionDetails.getValueAt(i, 13));
                            chkMap.put("CLOCK_NO",tblTransExceptionDetails.getValueAt(i, 2));
                            acMap.put("MDS", chkMap);
                            finalMap.put(actnum, acMap);
                            System.out.println("ac map  is" + acMap);
                            System.out.println("final map is" + finalMap);

                        }
                        chk = false;
                    }

                }
            }
            observable.setFinalList(newList);
            System.out.println("size of new List is" + newList.size() + "and final map is.." + finalMap);
        }
        if (finalMap == null || finalMap.isEmpty()) {
            ClientUtil.showMessageWindow(" Please select atleast one account...!!! ");
            return;
        }
        savePerformed();
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed() {
        observable.setFinalMap(finalMap);
        observable.doAction();
        observable.getProxyReturnMap();
        if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
            lblStatus.setText(observable.getProxyReturnMap().get("STATUS").toString());
            ClientUtil.showMessageWindow(" Transaction Completed...!!! "
                    + "\n Transaction Id is :" + observable.getProxyReturnMap().get("TransId").toString());
            printReceipt(observable.getProxyReturnMap());
            clearall();
            System.out.println("single tran id here "+observable.getProxyReturnMap());
        }
    }
    private void printReceipt(HashMap returnMap) //function to print receipt of transaction for each customer, added by shihad on 26/06/2014
    {
        try{
        //System.out.println("here returnmap"+returnMap);
        String transId = (String) returnMap.get("TransId");
        ArrayList clockNoList = (ArrayList) returnMap.get("CLOACK_LIST");
        String reportName = "";
        int yesNo = 0;
        String[] voucherOptions = {"Yes", "No"};
              if (returnMap != null && returnMap.size() > 0) {
                yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, voucherOptions, voucherOptions[0]);
                if (yesNo == 0) {
//                    for(int i=0;i<clockNoList.size();i++)
//                    {    
                    TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
//                    paramMap.put("TransDt", currDt);
//                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                    paramMap.put("TransId", transId);
//                    paramMap.put("ClockNo", CommonUtil.convertObjToStr(clockNoList.get(i)));
                     //Object keys1[] = transIdMap.keySet().toArray();
                    //for (int i = 0; i < keys1.length; i++) {
                        //paramMap.put("TransId", keys1[i]);
                        ttIntgration.setParam(paramMap);
                        //System.out.println("setparam asd"+paramMap);
                       reportName = "TransExceptionReport";
                       ttIntgration.integrationForPrint(reportName, true);
                   // }
//                    }
                }
              }
        }
        catch(Exception e)
        {
        e.printStackTrace();
        }
    }
    private void tblTransExceptionDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTransExceptionDetailsMouseClicked
        // TODO add your handling code here:
        if (selectMode == true) {
            String st = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(tblTransExceptionDetails.getSelectedRow(), 0));
            if (st.equals("true")) {
                tblTransExceptionDetails.setValueAt(new Boolean(false), tblTransExceptionDetails.getSelectedRow(), 0);
            } else {
                tblTransExceptionDetails.setValueAt(new Boolean(true), tblTransExceptionDetails.getSelectedRow(), 0);
            }
            double totAmount = 0;
            for (int i = 0; i < tblTransExceptionDetails.getRowCount(); i++) {
                st = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(i, 0));
                if (st.equals("true")) {
                    totAmount = totAmount + CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(i, 7)).doubleValue();
                }
            }
            lblTotalTransactionAmtVal.setText(CurrencyValidation.formatCrore(String.valueOf(totAmount)));
        }
        if (evt.getClickCount() == 2) {
//            HashMap returnMap = new HashMap();
            if (returnMap != null) {
//                returnMap = observable.getProxyReturnMap();
                if (returnMap.containsKey(tblTransExceptionDetails.getValueAt(
                        tblTransExceptionDetails.getSelectedRow(), 1))) {
                    TTException exception = (TTException) returnMap.get(tblTransExceptionDetails.getValueAt(
                            tblTransExceptionDetails.getSelectedRow(), 1));
                    parseException.logException(exception, true);
                }
            }
        }

        if (evt.getClickCount() >= 2 && (tblTransExceptionDetails.getSelectedColumn() == 7 || tblTransExceptionDetails.getSelectedColumn() == 8 || tblTransExceptionDetails.getSelectedColumn() == 9 || tblTransExceptionDetails.getSelectedColumn() == 10 || tblTransExceptionDetails.getSelectedColumn() == 11 || tblTransExceptionDetails.getSelectedColumn() == 13)) {
            changeAmt();
        }
    }//GEN-LAST:event_tblTransExceptionDetailsMouseClicked
    public void changeAmt() {
        HashMap amountMap = new HashMap();
        if (CommonUtil.convertObjToStr(CommonConstants.OPERATE_MODE).equals(CommonConstants.IMPLEMENTATION)) {
            String tolerance_amt = CommonUtil.convertObjToStr(CommonConstants.TOLERANCE_AMT);
            if (tolerance_amt.length() == 0) {
                ClientUtil.displayAlert("Please Add Tolerance Property in  TT property");
                return;
            }
            int c = tblTransExceptionDetails.getSelectedColumn();
            int r = tblTransExceptionDetails.getSelectedRow();
            String selectedAmt = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(r, c));

            selectedAmt = selectedAmt.replaceAll(",", "");
            amountMap.put("TOLERANCE_AMT", CommonConstants.TOLERANCE_AMT);
            amountMap.put("SELECTED_AMT", selectedAmt);
            amountMap.put("TITLE", "TransExcemption");
            amountMap.put("CALCULATED_AMT", selectedAmt);

            System.out.println("amountMap####" + amountMap);
            TextUI textUI = new TextUI(this, this, amountMap);

        }

    }

    @Override
    public void modifyTransData(Object objData) {
        TextUI obj = (TextUI) objData;
        //System.out.println("obj.getTxtData()"+obj.getTxtData());
        String enteredData = obj.getTxtData();
        int c = tblTransExceptionDetails.getSelectedColumn();
        int r = tblTransExceptionDetails.getSelectedRow();
        
        tblTransExceptionDetails.setValueAt(Double.parseDouble(enteredData), r, c);
        
        boolean chk = ((Boolean) tblTransExceptionDetails.getValueAt(tblTransExceptionDetails.getSelectedRow(), 0)).booleanValue();
        String prodtype = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(r, 4));
        HashMap hmap = new HashMap();
        int no = CommonUtil.convertObjToInt(tblTransExceptionDetails.getValueAt(r, 8));
        String prodid = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(r, 5));
        String actnum = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(r, 6));
        double clearBalance = CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(r, 16)).doubleValue();
        try {
            if (prodtype.equals("MDS") && (c == 8 || c == 10 || c == 11)) {
                hmap = calcEachChittal(c, chk, "empty", r);
                System.out.println("hmap" + hmap);
                if (hmap.get("NO_OF_INSTALLMENTS") != null) {
                    tblTransExceptionDetails.setValueAt(CommonUtil.convertObjToDouble(hmap.get("NO_OF_INSTALLMENTS")), r, 8);
                }
                if (hmap.get("PRINCIPAL") != null) {
                    tblTransExceptionDetails.setValueAt(CommonUtil.convertObjToDouble(hmap.get("PRINCIPAL")), r, 9);
                }
                if (hmap.get("INTEREST") != null) {
                    tblTransExceptionDetails.setValueAt(CommonUtil.convertObjToDouble(hmap.get("INTEREST")), r, 10);
                }
                if (hmap.get("PENAL") != null) {
                    tblTransExceptionDetails.setValueAt(CommonUtil.convertObjToDouble(hmap.get("PENAL")), r, 11);
                }
                if (hmap.get("NET_AMOUNT") != null) {
                    tblTransExceptionDetails.setValueAt(CommonUtil.convertObjToDouble(hmap.get("NET_AMOUNT")), r, 7);
                }

            } else if (prodtype.equals("TD") && c == 8) {
                hmap = getDepositsDetails(actnum, prodid, no);
                if (hmap.get("PRINCIPAL") != null) {
                    tblTransExceptionDetails.setValueAt(CommonUtil.convertObjToDouble(hmap.get("PRINCIPAL")), r, 9);
                }
                if (hmap.get("PENAL") != null) {
                    tblTransExceptionDetails.setValueAt(CommonUtil.convertObjToDouble(hmap.get("PENAL")), r, 11);
                }
                if (hmap.get("TOTAL_DEMAND") != null) {
                    tblTransExceptionDetails.setValueAt(CommonUtil.convertObjToDouble(hmap.get("TOTAL_DEMAND")), r, 7);
                }
            }
            // added by shihad on 28/06/2014
            else if (prodtype.equals("TL") && (c == 7 || c == 10 || c == 11 || c == 13)) { 
                System.out.println("inside tl calc");
                hmap = getLoanDetails(c,r);
                if (hmap.get("PRINCIPAL") != null) {
                    tblTransExceptionDetails.setValueAt(CommonUtil.convertObjToDouble(hmap.get("PRINCIPAL")), r, 9);
                }
                if (hmap.get("INTEREST") != null) {
                    tblTransExceptionDetails.setValueAt(CommonUtil.convertObjToDouble(hmap.get("INTEREST")), r, 10);
                }
                if (hmap.get("PENAL") != null) {
                    tblTransExceptionDetails.setValueAt(CommonUtil.convertObjToDouble(hmap.get("PENAL")), r, 11);
                }
                if (hmap.get("CHARGES") != null) {
                    tblTransExceptionDetails.setValueAt(CommonUtil.convertObjToDouble(hmap.get("CHARGES")), r, 13);
                }
                if (hmap.get("TOTAL") != null) {
                    tblTransExceptionDetails.setValueAt(CommonUtil.convertObjToDouble(hmap.get("TOTAL")), r, 7);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// Function to re-calculate transaction values while editing table cells for TL, added by shihad on 28/06/2014    

    public HashMap getLoanDetails(int column, int selectRow) {
        System.out.println("inside function aS");
        String prod_id = "";
        String amt;
        double totalDemand = 0.0;
        double doubleval = 0.0;
        HashMap dataMap = null;
        String scheme_Name = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(selectRow, 1));
        String acNo = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(selectRow, 6));
        String prodType = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(selectRow, 4));
        prod_id = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(selectRow, 14));
        //  String acNo = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(),1));
        double transAmt = CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 7)).doubleValue();
        doubleval = CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 4)).doubleValue();
        double paidPrincipal = CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 9)).doubleValue();
//        double actualInterest = CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 10)).doubleValue();
        double paidInterest = CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 10)).doubleValue();
        double paidPenal = CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 11)).doubleValue();
        double paidCharges = CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 13)).doubleValue();
        double totalAmt = CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 7)).doubleValue();
        double clearBal = CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 14)).doubleValue();
        double clearBalance = CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 16)).doubleValue();
        int selectedRow = selectRow;
        System.out.println("total here asd"+transAmt);        
//        if(actualPrincipal == 0 && actualInterest == 0 && actualPenal == 0 && actualCharges == 0){
            if(changedList != null && changedList.size()>0){
//                observable.setFinalTableList(new ArrayList());
                for (int i = 0; i < changedList.size(); i++) {
                    ArrayList aList1 = (ArrayList) changedList.get(i);
                    for (int j = 0; j < aList1.size(); j++) {
                        String actNum = aList1.get(6).toString();
                        if (actNum.equals(acNo) && selectRow == j ) {
                            System.out.println("inside setting value");
                            actualPrincipal = CommonUtil.convertObjToDouble(aList1.get(9).toString()).doubleValue();
                            actualInterest = CommonUtil.convertObjToDouble(aList1.get(10).toString()).doubleValue();
                            actualPenal = CommonUtil.convertObjToDouble(aList1.get(11).toString()).doubleValue();
                            actualCharges = CommonUtil.convertObjToDouble(aList1.get(13).toString()).doubleValue();
                            break;
                        }
                    }
//                    break;
                }
            }
//        }
        if (transAmt <= 0) {
            double principal1 = 0;
            double interest1 = 0;
            double penal1 = 0;
            double charges1 = 0;
            double bonus = 0;
            tblTransExceptionDetails.setValueAt(new Double(principal1), selectRow, 9);
            tblTransExceptionDetails.setValueAt(new Double(interest1), selectRow, 10);
            tblTransExceptionDetails.setValueAt(new Double(penal1), selectRow, 11);
            tblTransExceptionDetails.setValueAt(new Double(bonus), selectRow, 12);
            tblTransExceptionDetails.setValueAt(new Double(charges1), selectRow, 13);
        } else {
//            double paidPrincipal = 0;
//            double paidInterest = 0;
//            double paidPenal = 0;
//            double paidCharges = 0;
            try {
                double currInt = 0.0;
                HashMap ALL_LOAN_AMOUNT = new HashMap();
                HashMap hashList = new HashMap();
                hashList.put(CommonConstants.MAP_WHERE, prod_id);
                List appList = ClientUtil.executeQuery("selectAppropriatTransaction", hashList);
                HashMap appropriateMap = new HashMap();
                if (appList != null && appList.size() > 0) {
                    appropriateMap = (HashMap) appList.get(0);
                    appropriateMap.remove("PROD_ID");
                } else {
                    throw new TTException("Please Enter Hierachy of Transaction  in This Product ");
                }
                //System.out.println("appropriateMap####" + appropriateMap);
                java.util.Collection collectedValues = appropriateMap.values();
                java.util.Iterator it = collectedValues.iterator();
                //CashTransactionTO objCashTO =new CashTransactionTO();
                int appTranValue = 0;
                while (it.hasNext()) {
                    appTranValue++;
                    String hierachyValue = CommonUtil.convertObjToStr(it.next());
                    //System.out.println("hierachyValue####" + hierachyValue);
                    //objCashTO = setCashTransaction(objCashTransactionTO);
                    if (hierachyValue.equals("CHARGES")) {
                        if (transAmt > 0 && paidCharges > 0) {
                            if (transAmt >= paidCharges) {
                                transAmt -= paidCharges;
                                paidCharges = paidCharges;
                            } else {
                                paidCharges = transAmt;
                                transAmt -= paidCharges;
                            }
                        } else {
                            paidCharges = 0;
                        }
                    }
                    if (hierachyValue.equals("PENALINTEREST")) {
                        //penal interest
                        if (transAmt > 0 && paidPenal > 0) {
                            if (transAmt >= paidPenal) {
                                transAmt -= paidPenal;
                                paidPrincipal = transAmt;
                            } else {
                                paidPenal = transAmt;
                                transAmt -= paidPenal;
                            }
                        } else {
                            paidPenal = 0;
                        }
                    }
                    if (hierachyValue.equals("INTEREST")) {
                        //interest
                        if (transAmt > 0 && paidInterest > 0) {
                            if (transAmt >= paidInterest) {
                                transAmt -= paidInterest;
                                paidPrincipal = transAmt;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= paidInterest;
                            }
                        } else {
                            paidInterest = 0;
                        }
                    }
                    if (hierachyValue.equals("PRINCIPAL")) {
                        if (transAmt > 0 && paidPrincipal > 0) {
                            /*if (transAmt >= principal) {
                                //transAmt -= principal;
                                paidPrincipal = transAmt;
                            } else {
                                paidPrincipal = transAmt;
                                transAmt -= principal;
                            }*/                            
                            if ((clearBalance*-1) <= (paidPrincipal+paidPenal+paidInterest)) {
                                paidPrincipal = clearBalance*-1;
                                paidInterest = totalAmt - paidPrincipal - paidPenal;
                            } else {
                                if(actualInterest<=paidInterest){
                                    paidInterest = actualInterest;
                                    paidPrincipal = totalAmt - (paidInterest + paidPenal);
                                }else{
                                    paidInterest = totalAmt - (paidPrincipal + paidPenal);
                                }
                            }
                        } else {
                            paidPrincipal = 0;
                        }
                    }
                }
                currInt = paidInterest;
                Double total1 = ((paidPrincipal + paidPenal + paidInterest + paidCharges));
                System.out.println("total here"+total1);
                    dataMap = new HashMap();
                    if (paidCharges > 0) {
                        dataMap.put("CHARGES", String.valueOf(paidCharges));
                    } else {
                        dataMap.put("CHARGES", "0");
                    }
                    dataMap.put("PRINCIPAL", String.valueOf(paidPrincipal));
                    dataMap.put("INTEREST", String.valueOf(paidInterest));
                    dataMap.put("PENAL", String.valueOf(paidPenal));
                    dataMap.put("PROD_TYPE", prodType);
                    dataMap.put("ACT_NUM", acNo);
                    dataMap.put("RECOVERED_AMOUNT", doubleval);
                    dataMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                    dataMap.put("USER_ID", TrueTransactMain.USER_ID);
                    dataMap.put("PROD_ID", prod_id);
                    dataMap.put("BONUS", new Double(0));
//                    dataMap.put("DEBIT_ACT_NUM", acNo);
//                    dataMap.put("DEBIT_PROD_ID", prod_id);
                    dataMap.put("TOTAL", total1);
                    fMap.put(k, dataMap);
                    k++;
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
      return dataMap;
    }
    private HashMap calcEachChittal(int column, boolean chk, String selectAll, int selectRow) {
        flag = true;
        HashMap dataMap = new HashMap();
        String calculateIntOn = "";
        try {
            //System.out.println("in ISTTTTTTT");
            String chittalNo = "", subNo = "";
            String prId = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(selectRow, 14));
            chittalNo = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(selectRow, 6));
            String description = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(selectRow, 5));
            dataMap.put("ACT_NUM", chittalNo);
            if (chittalNo.indexOf("_") != -1) {
                subNo = chittalNo.substring(chittalNo.indexOf("_") + 1, chittalNo.length());
                chittalNo = chittalNo.substring(0, chittalNo.indexOf("_"));
            } else {
                subNo = "1";
            }
            String noOfInsPay = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(selectRow, 8));
            String prod_id = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(selectRow, 14));
            double pendDueIn = CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 13));
            double currIn = CommonUtil.convertObjToDouble(noOfInsPay).doubleValue();
            double remainInst = pendDueIn - currIn;
            //System.out.println("remainInst ====================== " + remainInst);
            if (remainInst < 0) {
                remainInst = 0;
            }
            //For Loop
            int selectedRow = selectRow;
            double principal1 = 0;
            double interest1 = 0;
            double penal1 = 0;
            double charges1 = 0;
            double bonus1 = 0;
            principal1 = CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 9));
            interest1 = CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 10));
            penal1 = CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 11));
            charges1 = CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 9));
            bonus1 = CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 12));
//            for (int i = 0; i < globalList.size(); i++) {
//                ArrayList aList1 = (ArrayList) globalList.get(i);
//                for (int j = 0; j < aList1.size(); j++) {
//                    String PID = aList1.get(11).toString();
//                    if (PID.equals(prod_id)) {
//                        //  if(aList.get(3)!=null)
//                        principal1 = CommonUtil.convertObjToDouble(aList1.get(4).toString()).doubleValue();
//                        //  if(aList.get(5)!=null)
//                        interest1 = CommonUtil.convertObjToDouble(aList1.get(6).toString()).doubleValue();
//                        //   if(aList.get(4)!=null)
//                        penal1 = CommonUtil.convertObjToDouble(aList1.get(5).toString()).doubleValue();
//                        //  if(aList.get(7)!=null)
//                        charges1 = CommonUtil.convertObjToDouble(aList1.get(8).toString()).doubleValue();
//                        bonus1 = CommonUtil.convertObjToDouble(aList1.get(7).toString()).doubleValue();
//                        // displayAlert("principal ="+principal +" interest="+interest+" penal="+penal);
//                    }
//                }
//            }
            //System.out.println("#####noOfInsPayst=" + noOfInsPay);
            if (noOfInsPay == null || noOfInsPay.equals("")) {
//                System.out.println("#####I(*&&^%%%$#$$#@##@@22222222222222222=" + noOfInsPay);
//                tblTransExceptionDetails.setValueAt(principal1, selectedRow, 9);
//                tblTransExceptionDetails.setValueAt(penal1, selectedRow, 11);//penalAmount
//                tblTransExceptionDetails.setValueAt(interest1, selectedRow, 10);//instAmount
//                tblTransExceptionDetails.setValueAt(bonus1, selectedRow, 12);
////                return;
            }
            //End

//            String noticAmt = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(selectRow, 10));
//            String arbitAmt = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(selectRow, 11));
//            String insDue = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(tblTransExceptionDetails.getSelectedRow(),3));
//
//            String inter = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(tblTransExceptionDetails.getSelectedRow(),6));
//            String pene = CommonUtil.convertObjToStr(tblTransExceptionDetails.getValueAt(tblTransExceptionDetails.getSelectedRow(),5));
            
            
            String noticAmt = "";
            String arbitAmt = "";
            String insDue = "";

            String inter = "";
            String pene = "";           
            HashMap pendingMap = new HashMap();
            pendingMap.put("SCHEME_NAME", prId);
            pendingMap.put("CHITTAL_NO", chittalNo);
            //System.out.println("######### subNo" + subNo);
            pendingMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
            //            //System.out.println("######### pendingMap"+pendingMap);
            List pendingAuthlst = ClientUtil.executeQuery("checkPendingForAuthorization", pendingMap);
            //System.out.println("######### pendingAuthlst=" + pendingAuthlst.size());
            //System.out.println("######### noOfInsPay=" + noOfInsPay);
            if (pendingAuthlst != null && pendingAuthlst.size() > 0) {
                //  ClientUtil.showMessageWindow(" Transaction pending for this Chittal... Please Authorize OR Reject first  !!! ");
                // tblTransaction.setValueAt("", selectedRow, 4);
                // tblTransaction.setValueAt(false, selectedRow, 0);
            } else if (CommonUtil.convertObjToDouble(noOfInsPay).doubleValue() >= 1) {
                //System.out.println("######### noOfInsPay IN=" + noOfInsPay);
                HashMap whereMap = new HashMap();

                HashMap productMap = new HashMap();

                long diffDayPending = 0;
                int noOfInsPaid = 0;
                Date currDate = (Date) currDt.clone();
                Date instDate = null;
                boolean bonusAvailabe = true;
                long noOfInstPay = CommonUtil.convertObjToLong(noOfInsPay);
                int instDay = 1;
                int totIns = 0;
                Date startDate = null;
                Date endDate = null;
                Date insDate = null;
                int startMonth = 0;
                int insMonth = 0;
                int curInsNo = 0;
                HashMap installmentMap = new HashMap();
                whereMap.put("SCHEME_NAME", prId);
                List lst = ClientUtil.executeQuery("getSelectSchemeAcctHead", whereMap);
                //System.out.println("#######lst =ooo==" + lst.size() + " LST----" + lst);
                if (lst != null && lst.size() > 0) {
                    productMap = (HashMap) lst.get(0);
                    totIns = CommonUtil.convertObjToInt(productMap.get("NO_OF_INSTALLMENTS"));
                    startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
                    endDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_END_DT")));
                    insDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
                    startMonth = insDate.getMonth();
                    // Added by Rajesh For checking BONUS_FIRST_INSTALLMENT. Based on this for loop initial value will be changed for Penal calculation.
                    //                    String bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("BONUS_FIRST_INSTALLMENT"));
                    String bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("ADVANCE_COLLECTION"));
                    int startNoForPenal = 0;
                    int addNo = 1;
                    int firstInst_No = -1;
                    if (bonusFirstInst.equals("Y")) {
                        startNoForPenal = 1;
                        addNo = 0;
                        firstInst_No = 0;
                    }
                    bonusAvailabe = true;
                    double insAmt = 0.0;
                    //                    double insAmt = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT")).doubleValue();
                    long pendingInst = 0;
                    int divNo = 0;
                    long count = 0;
                    long insDueAmt = 0;
                    whereMap.put("CHITTAL_NO", chittalNo);
                    whereMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                    List insList = ClientUtil.executeQuery("getNoOfInstalmentsPaid", whereMap);
                    //System.out.println("#######insList ===" + insList.size() + "insList ===" + insList);
                    if (insList != null && insList.size() > 0) {
                        whereMap = (HashMap) insList.get(0);
                        noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
                        count = CommonUtil.convertObjToLong(whereMap.get("NO_OF_INST"));
                    }

                    HashMap chittalMap = new HashMap();
                    chittalMap.put("CHITTAL_NO", chittalNo);
                    chittalMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                    List chitLst = ClientUtil.executeQuery("getSelctApplnReceiptDetails", chittalMap);
                    //System.out.println("#######chitLst ===" + chitLst.size() + "chitLst ===" + chitLst);
                    if (chitLst != null && chitLst.size() > 0) {
                        chittalMap = (HashMap) chitLst.get(0);
                        //System.out.println("#######chittalMap ===" + chittalMap);
                        instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                        divNo = CommonUtil.convertObjToInt(chittalMap.get("DIVISION_NO"));
                        dataMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(chittalMap.get("BRANCH_CODE")));
                        insAmt = CommonUtil.convertObjToDouble(chittalMap.get("INST_AMT")).doubleValue();
                    }

                    HashMap insDateMap = new HashMap();
                    insDateMap.put("DIVISION_NO", CommonUtil.convertObjToStr(divNo));
                    insDateMap.put("SCHEME_NAME", prId);
                    insDateMap.put("CURR_DATE", currDt.clone());
                    //                    insDateMap.put("ADD_MONTHS", "0");
                    insDateMap.put("ADD_MONTHS", "-1");
                    List insDateLst = ClientUtil.executeQuery("getTransAllMDSCurrentInsDate", insDateMap);
                    System.out.println("#######insDateLst ===" + insDateLst.size() + "insDateLst ===" + insDateLst);
                    if (insDateLst != null && insDateLst.size() > 0) {
                        insDateMap = (HashMap) insDateLst.get(0);
                        //System.out.println("#######insDateMap ===" + insDateMap);
                        curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
                        pendingInst = curInsNo - noOfInsPaid;
                        if (instDay < currDate.getDate()) {
                            pendingInst = pendingInst + 1;
                        } else {
                            pendingInst = pendingInst;
                        }
                        if (pendingInst < 0) {
                            pendingInst = 0;
                        }
                        insMonth = startMonth + curInsNo;
                        insDate.setMonth(insMonth);
                    }

                    HashMap prizedMap = new HashMap();
                    double bonusAval = 0;
                    prizedMap.put("SCHEME_NAME", prId);
                    prizedMap.put("DIVISION_NO", CommonUtil.convertObjToStr(divNo));
                    prizedMap.put("CHITTAL_NO", chittalNo);
                    prizedMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                    lst = ClientUtil.executeQuery("getSelectPrizedDetailsEntryRecords", prizedMap);
                    //System.out.println("#######prizedMap ===" + lst.size() + "prizedMap ===" + lst);
                    if (lst != null && lst.size() > 0) {
                        prizedMap = (HashMap) lst.get(0);
                        if (prizedMap.get("DRAW") != null && !prizedMap.get("DRAW").equals("") && prizedMap.get("DRAW").equals("Y")) {
                            setRdoPrizedMember_Yes(true);
                        }
                        if (prizedMap.get("AUCTION") != null && !prizedMap.get("AUCTION").equals("") && prizedMap.get("AUCTION").equals("Y")) {
                            setRdoPrizedMember_Yes(true);
                        }
                        bonusAval = CommonUtil.convertObjToDouble(prizedMap.get("PRIZED_AMOUNT"));
                    } else {
                        setRdoPrizedMember_No(true);
                    }
                    //System.out.println("#######totIns 1===" + totIns + " noOfInsPaid==" + noOfInsPaid);
                    int balanceIns = totIns - noOfInsPaid;
                    if (balanceIns >= noOfInstPay) {
                        long totDiscAmt = 0;
                        long penalAmt = 0;
                        double netAmt = 0;
                        double insAmtPayable = 0;
                        double totBonusAmt = 0;
                        double bonusAmt = 0;
                        String penalIntType = "";
                        long penalValue = 0;
                        String penalGraceType = "";
                        long penalGraceValue = 0;
                        String penalCalcBaseOn = "";
                        if (pendingInst > 0) {              //pending installment calculation starts...
                            insDueAmt = (long) insAmt * pendingInst;
                            int totPendingInst = (int) pendingInst;
                            double calc = 0;
                            long totInst = pendingInst;
                            penalCalcBaseOn = CommonUtil.convertObjToStr(productMap.get("PENAL_CALC"));
                            System.out.println("#######penalCalcBaseOn ===" + penalCalcBaseOn +"calculateIntOn :"+calculateIntOn);
                            if (getRdoPrizedMember_Yes() == true) {
                                if (productMap.containsKey("PENEL_PZ_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT") != null) {
                                    calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT"));
                                }
                                penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_INT_TYPE"));
                                penalValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_INT_AMT"));
                                penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_GRACE_PERIOD_TYPE"));
                                penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_GRACE_PERIOD"));
                            } else if (getRdoPrizedMember_No() == true) {
                                if (productMap.containsKey("PENEL_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_INT_FULL_AMT_INST_AMT") != null) {
                                    calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_INT_FULL_AMT_INST_AMT"));
                                }
                                penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_INT_TYPE"));
                                penalValue = CommonUtil.convertObjToLong(productMap.get("PENAL_INT_AMT"));
                                penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_GRACE_PERIOD_TYPE"));
                                penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_GRACE_PERIOD"));
                            }
                            List bonusAmout = new ArrayList();
                            System.out.println("calculateIntOn" + calculateIntOn + " inst amt :" + productMap.get("INSTALLMENT_AMOUNT"));
                            if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                                //double instAmount = 0.0;
                                HashMap nextInstMaps = null;
                                for (int i = startNoForPenal; i <= noOfInstPay - addNo; i++) {
                                    nextInstMaps = new HashMap();
                                    nextInstMaps.put("SCHEME_NAME", prId);
                                    nextInstMaps.put("DIVISION_NO", divNo);
                                    nextInstMaps.put("SL_NO", new Double(i + noOfInsPaid + addNo + firstInst_No));
                                    List listRec = ClientUtil.executeQuery("getSelectBonusAndNextInstDateWithoutDivision", nextInstMaps);
                                    if (listRec != null && listRec.size() > 0) {
                                        nextInstMaps = (HashMap) listRec.get(0);
                                    }
                                    System.out.println("nextInstMaps" + nextInstMaps);
                                    if (nextInstMaps != null && nextInstMaps.containsKey("NEXT_BONUS_AMOUNT")) {
                                        bonusAmout.add(CommonUtil.convertObjToDouble(nextInstMaps.get("NEXT_BONUS_AMOUNT")));
                                    } else {
                                        bonusAmout.add(CommonUtil.convertObjToDouble(0));
                                    }
                                }
                            }
                            System.out.println("Nidhin List:" + bonusAmout);
                            for (int j = startNoForPenal; j < noOfInstPay + startNoForPenal; j++) {
                                if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                                    insAmt = 0.0;
                                    double instAmount = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT"));
                                    if (bonusAmout != null && bonusAmout.size() > 0) {
                                        instAmount -= CommonUtil.convertObjToDouble(bonusAmout.get(j - 1));
                                    }
                                    insAmt = instAmount;
                                }
                                HashMap nextInstMap = new HashMap();
                                nextInstMap.put("SCHEME_NAME", prId);
                                nextInstMap.put("DIVISION_NO", CommonUtil.convertObjToStr(divNo));
                                nextInstMap.put("SL_NO", new Double(j + noOfInsPaid));
                                List listRec = ClientUtil.executeQuery("getSelectNextInstDate", nextInstMap);
                                if (listRec != null && listRec.size() > 0) {
                                    double penal = 0;
                                    nextInstMap = (HashMap) listRec.get(0);
                                    instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                                    if (instDay > 0) {
                                        instDate.setDate(instDate.getDate() + instDay - 1);
                                    }
                                    diffDayPending = DateUtil.dateDiff(instDate, currDate);
                                    //Holiday Checking - Added By Suresh
                                    HashMap holidayMap = new HashMap();
                                    boolean checkHoliday = true;
                                    //System.out.println("instDate   " + instDate);
                                    instDate = setProperDtFormat(instDate);
                                    //System.out.println("instDate   " + instDate);
                                    holidayMap.put("NEXT_DATE", instDate);
                                    holidayMap.put("BRANCH_CODE", getSelectedBranchID());
                                    while (checkHoliday) {
                                        boolean tholiday = false;
                                        List Holiday = ClientUtil.executeQuery("checkHolidayDateOD", holidayMap);
                                        List weeklyOf = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
                                        boolean isHoliday = Holiday.size() > 0 ? true : false;
                                        boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                                        if (isHoliday || isWeekOff) {
                                            //System.out.println("#### diffDayPending Holiday True : " + diffDayPending);
                                            if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                                diffDayPending -= 1;
                                                instDate.setDate(instDate.getDate() + 1);
                                            } else {
                                                diffDayPending += 1;
                                                instDate.setDate(instDate.getDate() - 1);
                                            }
                                            holidayMap.put("NEXT_DATE", instDate);
                                            checkHoliday = true;
                                            //System.out.println("#### holidayMap : " + holidayMap);
                                        } else {
                                            //System.out.println("#### diffDay Holiday False : " + diffDayPending);
                                            checkHoliday = false;
                                        }
                                    }
                                    //System.out.println("#### diffDayPending Final : " + diffDayPending);
                                    if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Days")) {
                                        if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                            if (diffDayPending > penalGraceValue) {
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    calc += (diffDayPending * penalValue * insAmt) / 36500;
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                                    calc += penalValue;
                                                }
                                            }
                                        } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                            // To be written
                                            if (diffDayPending > penalGraceValue) {
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                                    calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                                    calc += penalValue;
                                                }
                                            }
                                        }
                                    } else if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Installments")) {
                                        if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                            if (diffDayPending > penalGraceValue) {
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    calc += ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute") && totInst <= noOfInstPay) {
                                                    calc += penalValue;
                                                }
                                            }
                                        } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                            // To be written
                                            if (diffDayPending > penalGraceValue) {
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                                    calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                                    calc += penalValue;
                                                }
                                            }
                                        }
                                    }
                                    penal = (calc + 0.5) - penal;
                                    nextInstMap.put("PENAL", CommonUtil.convertObjToStr(penal));
                                    installmentMap.put(String.valueOf(j + noOfInsPaid + addNo), nextInstMap);
                                    penal = calc + 0.5;
                                }
                            }
                            if (calc > 0) {
                                penalAmt = (long) (calc + 0.5);

                            }
                        }//pending installment calculation ends...

                        //Discount calculation details Starts...
                        for (int k = 0; k < noOfInstPay; k++) {
                            HashMap nextInstMap = new HashMap();
                            nextInstMap.put("SCHEME_NAME", prId);
                            nextInstMap.put("DIVISION_NO", CommonUtil.convertObjToStr(divNo));
                            nextInstMap.put("SL_NO", new Double(k + noOfInsPaid));
                            List listRec = ClientUtil.executeQuery("getSelectNextInstDate", nextInstMap);
                            if (listRec == null || listRec.size() == 0) {
                                Date curDate = (Date) currDt.clone();
                                int curMonth = curDate.getMonth();
                                curDate.setMonth(curMonth + 1);
                                curDate.setDate(instDay);
                                listRec = new ArrayList();
                                nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                                listRec.add(nextInstMap);
                            }
                            if (listRec != null && listRec.size() > 0) {
                                long discountAmt = 0;
                                nextInstMap = (HashMap) listRec.get(0);
                                instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                                long diffDay = DateUtil.dateDiff(instDate, currDate);
                                if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y")
                                        || productMap.get("BONUS_ALLOWED").equals("N"))) {
                                    String discount = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_ALLOWED"));
                                    if (discount != null && !discount.equals("") && discount.equals("Y")) {
                                        String discountType = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_RATE_TYPE"));
                                        long discountValue = CommonUtil.convertObjToLong(productMap.get("DISCOUNT_RATE_AMT"));
                                        if (getRdoPrizedMember_Yes() == true) {//discount calculation for prized prerson...
                                            String discountPrizedDays = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_DAYS"));
                                            String discountPrizedMonth = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_MONTHS"));
                                            String discountPrizedAfter = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_AFTER"));
                                            String discountPrizedEnd = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_END"));
                                            long discountPrizedValue = CommonUtil.convertObjToLong(productMap.get("DIS_PRIZED_GRACE_PERIOD"));
                                            if (discountPrizedDays != null && !discountPrizedDays.equals("") && discountPrizedDays.equals("D") && diffDay <= discountPrizedValue) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    if (diffDay <= discountPrizedValue) {
                                                        totDiscAmt = totDiscAmt + calc;
                                                    }
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    if (diffDay <= discountPrizedValue) {
                                                        totDiscAmt = totDiscAmt + discountValue;
                                                    }
                                                }
                                            } else if (discountPrizedMonth != null && !discountPrizedMonth.equals("") && discountPrizedMonth.equals("M") && diffDay <= (discountPrizedValue * 30)) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountPrizedAfter != null && !discountPrizedAfter.equals("") && discountPrizedAfter.equals("A") && currDate.getDate() <= discountPrizedValue) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountPrizedEnd != null && !discountPrizedEnd.equals("") && discountPrizedEnd.equals("E") && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else {
                                                totDiscAmt = 0;
                                            }
                                        } else if (getRdoPrizedMember_No() == true) {//discount calculation non prized person...
                                            String discountGraceDays = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_DAYS"));
                                            String discountGraceMonth = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_MONTHS"));
                                            String discountGraceAfter = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_AFTER"));
                                            String discountGraceEnd = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_END"));
                                            long discountGraceValue = CommonUtil.convertObjToLong(productMap.get("DIS_GRACE_PERIOD"));
                                            if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("D")) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    if (diffDay <= discountGraceValue) {
                                                        totDiscAmt = totDiscAmt + calc;
                                                    }
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    if (diffDay <= discountGraceValue) {
                                                        totDiscAmt = totDiscAmt + discountValue;
                                                    }
                                                } else {
                                                    totDiscAmt = 0;
                                                }
                                            } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("M") && diffDay <= discountGraceValue * 30 && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("A") && currDate.getDate() <= discountGraceValue && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("E") && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else {
                                                totDiscAmt = 0;
                                            }
                                        }
                                    } else if (discount != null && !discount.equals("") && discount.equals("N")) {
                                        totDiscAmt = 0;
                                    }
                                    discountAmt = totDiscAmt - discountAmt;
                                    HashMap instMap = new HashMap();
                                    if (installmentMap.containsKey(String.valueOf(k + noOfInsPaid + 1))) {
                                        instMap = (HashMap) installmentMap.get(String.valueOf(k + noOfInsPaid + 1));
                                        instMap.put("DISCOUNT", String.valueOf(discountAmt));
                                        installmentMap.put(String.valueOf(k + noOfInsPaid + 1), instMap);
                                    }
                                    discountAmt = totDiscAmt;
                                }

                            }
                        }

                        //Bonus calculation details Starts...
                        for (int l = startNoForPenal; l <= noOfInstPay - addNo; l++) {
                            //                    for(int l = 1;l<=noOfInstPay;l++){
                            HashMap nextInstMap = new HashMap();
                            nextInstMap.put("SCHEME_NAME", prId);
                            nextInstMap.put("DIVISION_NO", divNo);
                            nextInstMap.put("SL_NO", new Double(l + noOfInsPaid + addNo + firstInst_No));
                            List listRec = ClientUtil.executeQuery("getSelectBonusAndNextInstDateWithoutDivision", nextInstMap);
                            if (listRec == null || listRec.size() == 0) {
                                Date curDate = (Date) currDt.clone();
                                int curMonth = curDate.getMonth();
                                curDate.setMonth(curMonth + 1);
                                curDate.setDate(instDay);
                                listRec = new ArrayList();
                                nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                                listRec.add(nextInstMap);
                                bonusAvailabe = false;
                            }
                            if (listRec != null && listRec.size() > 0) {
                                nextInstMap = (HashMap) listRec.get(0);
                                instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                                bonusAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_BONUS_AMOUNT"))).doubleValue();
                                if (!productMap.get("MULTIPLE_MEMBER").equals("") && (productMap.get("MULTIPLE_MEMBER").equals("Y"))) {
                                    whereMap = new HashMap();
                                    int noOfCoChittal = 0;
                                    whereMap.put("SCHEME_NAME", prId);
                                    whereMap.put("CHITTAL_NUMBER", chittalNo);
                                    List applicationLst = ClientUtil.executeQuery("getSelectChitNoNotinMasterDetails", whereMap); // Count No Of Co-Chittals
                                    if (applicationLst != null && applicationLst.size() > 0) {
                                        noOfCoChittal = applicationLst.size();
                                        bonusAmt = bonusAmt / noOfCoChittal;
                                    }
                                }
                                if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                        && bonusAmt > 0) {
                                    Rounding rod = new Rounding();
                                    bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                                }
                                long diffDay = DateUtil.dateDiff(instDate, currDate);
                                //                            String hoildayInt = CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT"));
                                if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y"))) {
                                    if (bonusAvailabe == true) {
                                        if (getRdoPrizedMember_Yes() == true) {
                                            String bonusPrizedDays = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_DAYS"));
                                            String bonusPrizedMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
                                            String bonusPrizedAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_AFT"));
                                            String bonusPrizedEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_END"));
                                            long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
                                            if (bonusPrizedDays != null && !bonusPrizedDays.equals("") && bonusPrizedDays.equals("D") && diffDay <= bonusPrizedValue) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusPrizedMonth != null && !bonusPrizedMonth.equals("") && bonusPrizedMonth.equals("M") && diffDay <= (bonusPrizedValue * 30)) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusPrizedAfter != null && !bonusPrizedAfter.equals("") && bonusPrizedAfter.equals("A") && currDate.getDate() <= bonusPrizedValue) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusPrizedEnd != null && !bonusPrizedEnd.equals("") && bonusPrizedEnd.equals("E")) {

                                            } else {

                                            }
                                        } else if (getRdoPrizedMember_No() == true) {
                                            String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
                                            String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
                                            String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
                                            String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
                                            long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
                                            if (bonusGraceDays != null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay <= bonusGraceValue) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusGraceMonth != null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay <= (bonusGraceValue * 30)) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusGraceOnAfter != null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && currDate.getDate() <= bonusGraceValue) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusGraceEnd != null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E")) {

                                            } else {
                                            }
                                        }
                                    }
                                    HashMap instMap = new HashMap();
                                    if (installmentMap.containsKey(String.valueOf(l + noOfInsPaid + addNo))) {
                                        Rounding rod = new Rounding();
                                        instMap = (HashMap) installmentMap.get(String.valueOf(l + noOfInsPaid + addNo));
                                        //Added By Suresh
                                        if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                                && bonusAmt > 0) {
                                            bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                                        }
                                        instMap.put("BONUS", CommonUtil.convertObjToStr(bonusAmt));
                                        installmentMap.put(String.valueOf(l + noOfInsPaid + addNo), instMap);
                                    }
                                }
                            }
                            bonusAmt = 0;
                        }
                        if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                && totBonusAmt > 0) {
                            Rounding rod = new Rounding();
                            totBonusAmt = (double) rod.getNearest((long) (totBonusAmt * 100), 100) / 100;
                        }

                        int insDay = 0;
                        Date paidUpToDate = null;
                        HashMap instDateMap = new HashMap();
                        instDateMap.put("SCHEME_NAME", prId);
                        instDateMap.put("DIVISION_NO", CommonUtil.convertObjToInt(divNo));
                        instDateMap.put("INSTALLMENT_NO", CommonUtil.convertObjToInt(String.valueOf(count)));
                        lst = ClientUtil.executeQuery("getSelectInstUptoPaid", instDateMap);
                        if (lst != null && lst.size() > 0) {
                            instDateMap = (HashMap) lst.get(0);
                            paidUpToDate = setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(instDateMap.get("NEXT_INSTALLMENT_DATE"))));
                        } else {
                            Date startedDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("SCHEME_START_DT")));
                            insDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                            startedDate.setDate(insDay);
                            int stMonth = startedDate.getMonth();
                            startedDate.setMonth(stMonth + (int) count - 1);
                            paidUpToDate = setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(startedDate)));
                        }

                        String narration = "";
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM/yyyy");
                        int noInstPay = CommonUtil.convertObjToInt(noOfInsPay);
                        if (noInstPay == 1) {
                            narration = "Inst#" + (noOfInsPaid + 1);
                            Date dt = DateUtil.addDays(paidUpToDate, 30);
                            narration += " " + sdf.format(dt);
                        } else if (noInstPay > 1) {
                            narration = "Inst#" + (noOfInsPaid + 1);
                            narration += "-" + (noOfInsPaid + noInstPay);
                            Date dt = DateUtil.addDays(paidUpToDate, 30);
                            narration += " " + sdf.format(dt);
                            dt = DateUtil.addDays(paidUpToDate, 30 * noInstPay);
                            narration += " To " + sdf.format(dt);
                        }
                        //System.out.println("#$#$# narration :" + narration);
                        //  penalAmt= Long.parseLong(pene);//added by babu 22-Apr-2013
                        if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                            insAmt = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT"));
                        }
                        double instAmt = insAmt * CommonUtil.convertObjToDouble(noOfInsPay).doubleValue();
                        System.out.println("inst amt "+instAmt);
                        double totalPayable = instAmt - (totBonusAmt + totDiscAmt);
                        System.out.println("bonus minus "+(totBonusAmt + totDiscAmt));
                        System.out.println("#$#$# totalPayable :" + totalPayable);
                        System.out.println("penal asd"+penal1);
                        netAmt = totalPayable + penal1 + CommonUtil.convertObjToDouble(noticAmt).doubleValue()
                                 + CommonUtil.convertObjToDouble(arbitAmt).doubleValue();
                        String totalPayableAmount = String.valueOf(totalPayable);
                        totalPayableAmount = CurrencyValidation.formatCrore(totalPayableAmount).replaceAll(",", "");
                        String penalAmount = CommonUtil.convertObjToStr(penal1);

                        // String penalAmount = String.valueOf(inter);
                        penalAmount = CurrencyValidation.formatCrore(penalAmount).replaceAll(",", "");

                        /**
                         * ****************
                         */
                        // chittalFlag=false;
                        //                    //System.out.println("###### insAmt        : "+insAmt);
                        //                    //System.out.println("###### insAmtPayable : "+instAmt);
                        //                    //System.out.println("###### totBonusAmt   : "+totBonusAmt);
                        //                    //System.out.println("###### totDiscAmt    : "+totDiscAmt);
                        //                    //System.out.println("###### totalPayable  : "+totalPayable);
                        //                    //System.out.println("###### penalAmt      : "+penalAmt);
                        //                    //System.out.println("###### netAmt        : "+netAmt);
                        //                    //System.out.println("###### dataMap         : "+dataMap);
                        //System.out.println("###### finalMap        : " + finalMap);
                        //System.out.println("###### installmentMap  :" + installmentMap);
                        String instAmount = CommonUtil.convertObjToStr(instAmt);
                        instAmount = CurrencyValidation.formatCrore(instAmount).replaceAll(",", "");

                        String totBonusAmount = CommonUtil.convertObjToStr(totBonusAmt);
                        totBonusAmount = CurrencyValidation.formatCrore(totBonusAmount).replaceAll(",", "");

                        String totDiscAmount = CommonUtil.convertObjToStr(totDiscAmt);
                        totDiscAmount = CurrencyValidation.formatCrore(totDiscAmount).replaceAll(",", "");

                        String netAmount = CommonUtil.convertObjToStr(netAmt);
                        netAmount = CurrencyValidation.formatCrore(netAmount).replaceAll(",", "");
                        //System.out.println("VALITYYUUUUUUUUUUUUUUUUUUUU===" + totalPayableAmount);
                        System.out.println("no of int"+noOfInsPay);
                        if (CommonUtil.convertObjToDouble(noOfInsPay)!= 0) {
//                            tblTransExceptionDetails.setValueAt(totalPayableAmount, selectedRow, 5);
//                            tblTransExceptionDetails.setValueAt("0.0", selectedRow, 11);//penalAmount
//                            tblTransExceptionDetails.setValueAt(penalAmount, selectedRow, 7);//instAmount
//                            tblTransExceptionDetails.setValueAt(totBonusAmount, selectedRow, 12);
//                            tblTransExceptionDetails.setValueAt(netAmount, selectedRow, 7);
                            System.out.println("inside id asd");
                        } else {
//                            tblTransExceptionDetails.setValueAt(principal1, selectedRow, 5);
//                            tblTransExceptionDetails.setValueAt("0.0", selectedRow, 11);//penalAmount
//                            tblTransExceptionDetails.setValueAt(interest1, selectedRow, 7);//instAmount
//                            tblTransExceptionDetails.setValueAt(bonus1, selectedRow, 12);
//                            tblTransExceptionDetails.setValueAt(netAmount, selectedRow, 7);
//                            System.out.println("inside adasd");
                        }
                        //  tblTransaction.setValueAt(instAmount, selectedRow, 5);
                        //  tblTransaction.setValueAt(totBonusAmount, selectedRow, 6);
                        //  tblTransaction.setValueAt(totDiscAmount, selectedRow, 7);
                        //  tblTransaction.setValueAt(totalPayableAmount, selectedRow, 8);
                        //  tblTransaction.setValueAt(penalAmount, selectedRow, 9);
                        //  tblTransaction.setValueAt(netAmount, selectedRow, 12);

                        //System.out.println("column ==== " + column + " chk=====" + chk);
                        if (chk)//column==0 &&
                        {
                            //babu
                            System.out.println("inside ckh");
                            dataMap.put("SCHEME_NAME", prId);
                            dataMap.put("CHITTAL_NO", chittalNo);
                            dataMap.put("PRINCIPAL", totalPayableAmount);//totalPayableAmount);
                            dataMap.put("INTEREST",interest1);
                            dataMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                            dataMap.put("MEMBER_NAME", "");//lblMemberName.getText()
                            dataMap.put("DIVISION_NO", CommonUtil.convertObjToStr(divNo));
                            dataMap.put("CHIT_START_DT", startDate);
                            dataMap.put("INSTALLMENT_DATE", insDate);
                            dataMap.put("NO_OF_INSTALLMENTS", noOfInsPay);//String.valueOf(remainInst));//String.valueOf(totIns));
                            dataMap.put("CURR_INST", CommonUtil.convertObjToStr(curInsNo));
                            dataMap.put("PENDING_INST", insDue);
                            dataMap.put("PENDING_DUE_AMT", CommonUtil.convertObjToStr(insDueAmt));
                            dataMap.put("NO_OF_INST_PAY", noOfInsPay);
                            dataMap.put("PROD_DESCRIPTION", description);
                            dataMap.put("INST_AMT_PAYABLE", CommonUtil.convertObjToStr(totalPayable));
                            dataMap.put("PAID_INST", CommonUtil.convertObjToStr(noOfInsPaid));
                            dataMap.put("TOTAL_PAYABLE", CommonUtil.convertObjToStr(instAmt));
                            dataMap.put("PAID_DATE", currDate);
                            dataMap.put("INST_AMT", CommonUtil.convertObjToStr(insAmt));
                            dataMap.put("SCHEME_END_DT", endDate);
                            if (getRdoPrizedMember_Yes() == true) {
                                dataMap.put("PRIZED_MEMBER", "Y");
                            } else {
                                dataMap.put("PRIZED_MEMBER", "N");
                            }
                            dataMap.put("BONUS_AVAL", CommonUtil.convertObjToStr(bonusAval));
                            dataMap.put("BONUS",totBonusAmount);
                            dataMap.put("DISCOUNT", totDiscAmt);
                            dataMap.put("PENAL",penalAmount);
                            dataMap.put("NOTICE_AMOUNT", noticAmt);
                            dataMap.put("ARBITRATION_AMOUNT", arbitAmt);
                            dataMap.put("NET_AMOUNT", netAmount);
//                            dataMap.put("TOTAL_DEMAND", CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 5)) + CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 7))
//                                    + CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 9)));//String.valueOf(netAmt));
                            ////
                            dataMap.put("NARRATION", narration);
//                            dataMap.put("EACH_MONTH_DATA", installmentMap);
                            dataMap.put("INSTALLMENT_MAP", installmentMap);
                            dataMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                            dataMap.put("PROD_TYPE", "MDS");
                            dataMap.put("TOTAL", noOfInsPay);
//                            dataMap.put("AMTORNOOFINST", noOfInsPay);
//                            dataMap.put("CLOCK_NO", txtClockNo.getText());
//                            dataMap.put("MEMBER_NO", txtMemberNo.getText());
//                            dataMap.put("CUST_NAME", txtName.getText());
//                            dataMap.put("TOT_PRIN", CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 5)));
//                            dataMap.put("TOT_PENAL", "0.0");
//                            dataMap.put("TOT_INT", CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 7)));
//                            dataMap.put("TOT_OTHERS", CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 9)));
//                            dataMap.put("TOT_GRAND", CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 5)) + CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 7))
//                                    + CommonUtil.convertObjToDouble(tblTransExceptionDetails.getValueAt(selectRow, 9)));
//                            dataMap.put("CHARGES", new Double(0));
                            //System.out.println("noOfInsPay ===456436546==" + tblTransaction.getValueAt(selectRow, 7));
                            if (CommonUtil.convertObjToDouble(noOfInsPay) > 0) {// && !chittalFlag
//                                 termMdsChittalMap=new HashMap();
//                                  termMdsChittalMap.put("MDS",dataMap);
//                                                            finalMap.put(chittalNo,termMdsMap);

//                                termMdsMap = new HashMap();
//                                termMdsMap.put("MDS", dataMap);
                                finalMap1.put(chittalNo, dataMap);
                                System.out.println("finalMap====IIII===" + finalMap);
                            } else {
                                dataMap = null;
                            }
                            //Enf
                        }
                        tblTransExceptionDetails.revalidate();
                        ((DefaultTableModel) tblTransExceptionDetails.getModel()).fireTableDataChanged();

                    } else {
                        ClientUtil.showAlertWindow("Exceeds The No Of Total Installment !!! ");
//                        tblTransExceptionDetails.setValueAt("", selectRow, 4);
//                        tblTransExceptionDetails.setValueAt("", selectRow, 5);
//                        tblTransExceptionDetails.setValueAt("", selectRow, 6);
//                        tblTransExceptionDetails.setValueAt("", selectRow, 7);
//                        tblTransExceptionDetails.setValueAt("", selectRow, 8);
//                        tblTransExceptionDetails.setValueAt("", selectRow, 9);
//                        tblTransExceptionDetails.setValueAt("", selectRow, 10);
//                        tblTransExceptionDetails.setValueAt("", selectRow, 11);
//                         tblTransaction.setValueAt("",tblTransaction.getSelectedRow(),12);
                    }
                }
            }
            
            System.out.println("end of function new");
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        return dataMap;
    }
    private void tblTransExceptionDetailsInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tblTransExceptionDetailsInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTransExceptionDetailsInputMethodTextChanged

private void btnReprintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReprintActionPerformed
 if (tdtReprintDate.getDateValue().length() > 0) {
           callView("Reprint");
           viewType = "Reprint";
        }
}//GEN-LAST:event_btnReprintActionPerformed
private void callView(String currField) {
        HashMap viewMap = new HashMap();
        HashMap transMap = new HashMap();
        if (currField.equals("Reprint")) {
            HashMap whereMap = new HashMap();
            whereMap.put("From_Date", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtReprintDate.getDateValue())));
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_NAME, "getReprintDataForExcemptionDeduction");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            transMap = ClientUtil.executeTableQuery(viewMap);
            System.out.println("viewMap-=-=-=" + transMap);

        } 
       
        new ViewAll(this, viewMap).show();
    }
  public void fillData(Object map) {
        HashMap hash = (HashMap) map;
        
        System.out.println("hash====="+hash);
        System.out.println("viewType"+viewType);
        
         try{
             
        String reportName = "";
         int yesNo = 0;
                String[] options = {"Yes", "No"};
                yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                System.out.println("#$#$$ yesNo : " + yesNo);
                if (yesNo == 0) {
                    TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    paramMap.put("From_Date", hash.get("PROCESS_DATE"));
                    paramMap.put("EMP_NO", hash.get("EMPLOYEE_NO"));
                    paramMap.put("TRANS_EXCEPTION_ID", hash.get("TRANS_EXCEPTION_ID"));
                    ttIntgration.setParam(paramMap);
                    ttIntgration.integrationForPrint("DeductionExcemptionReprint", true);
              }
         }
        catch(Exception e)
        {
        e.printStackTrace();
        }
          
       
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnReprint;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProcessDate;
    private com.see.truetransact.uicomponent.CLabel lblReprintDate;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmtVal;
    private com.see.truetransact.uicomponent.CPanel panDepositInterestApplication;
    private com.see.truetransact.uicomponent.CPanel panProcess;
    private com.see.truetransact.uicomponent.CPanel panProductDetails;
    private com.see.truetransact.uicomponent.CPanel panProductTableData;
    private com.see.truetransact.uicomponent.CPanel panReprintDetails;
    private com.see.truetransact.uicomponent.CPanel panSelectAll;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CScrollPane srpTransExceptionDetails;
    private com.see.truetransact.uicomponent.CTable tblTransExceptionDetails;
    private com.see.truetransact.uicomponent.CDateField tdtProcessDate;
    private com.see.truetransact.uicomponent.CDateField tdtReprintDate;
    private com.see.truetransact.uicomponent.CTextField txtTokenNo;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        TransExceptionUI fad = new TransExceptionUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(fad);
        j.show();
        fad.show();
    }

    public boolean getRdoPrizedMember_Yes() {
        return rdoPrizedMember_Yes;
    }

    /**
     * Setter for property rdoPrizedMember_Yes.
     *
     * @param rdoPrizedMember_Yes New value of property rdoPrizedMember_Yes.
     */
    public void setRdoPrizedMember_Yes(boolean rdoPrizedMember_Yes) {
        this.rdoPrizedMember_Yes = rdoPrizedMember_Yes;
    }

    /**
     * Getter for property rdoPrizedMember_No.
     *
     * @return Value of property rdoPrizedMember_No.
     */
    public boolean getRdoPrizedMember_No() {
        return rdoPrizedMember_No;
    }

    /**
     * Setter for property rdoPrizedMember_No.
     *
     * @param rdoPrizedMember_No New value of property rdoPrizedMember_No.
     */
    public void setRdoPrizedMember_No(boolean rdoPrizedMember_No) {
        this.rdoPrizedMember_No = rdoPrizedMember_No;
    }
}
