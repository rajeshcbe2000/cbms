/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MDSDetailsUI.java
 *
 * Created on April 13, 2011, 10:10 PM
 */

package com.see.truetransact.ui.salaryrecovery;

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
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.clientexception.ClientParseException;
import org.apache.log4j.Logger;
import com.see.truetransact.clientproxy.ProxyParameters;
import javax.swing.table.DefaultTableModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.salaryrecovery.TransAllUI;
import java.text.DecimalFormat;

/**
 *
 * @author  Suresh
 */
public class MDSDetailsUI extends com.see.truetransact.uicomponent.CDialog implements Observer {
    private boolean rdoPrizedMember_Yes = false;
    private boolean rdoPrizedMember_No = false;
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    Date currDt;
    String branchID="";
    DefaultTableModel model = null;
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private ArrayList listadd=null;
    private ArrayList listTot=null;
    double interest=0.0;
    ArrayList newTot=new ArrayList();
    TransAllUI trans=new TransAllUI();
    DecimalFormat df=new DecimalFormat("##.00");
    //private EnhancedTableModel model;
    public MDSDetailsUI(ArrayList mdsList,boolean flag) {
        this.setVisible(flag);
        initComponents();
        setupScreen();
        initForm();
        getMdsdetail(mdsList);
        
        // return interest;
    }
    public MDSDetailsUI(ArrayList mdsList) {
        initComponents();
        setupScreen();
        initForm();
        getMdsdetail(mdsList);
        
        // return interest;
    }
    private void initForm(){
        setMaxLengths();
        setFieldNames();
        internationalize();
        currDt = ClientUtil.getCurrentDate();
        //setMDSTableTitle();
        // model = new EnhancedTableModel(null, tableTitle);
        initTableData();
    }
    public void  initTableData(){
        //       String data[][] = {{}};
        //     String col[] ={"Sl.No","Chittal No","Principal","Interest","Penal","Bonus","Arbitration","Notice"};
        //    DefaultTableModel model=new DefaultTableModel((data,col)
        tblPopupmds.setModel(new javax.swing.table.DefaultTableModel(
        null,
        new String [] {"Sl.No","Description","Chittal No","Principal","Interest","Penal","Bonus","Arbitration","Notice"}) {
            Class[] types = new Class [] {
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
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
                
            };
            boolean[] canEdit = new boolean [] {
                false,false, true, true, true, true, true, true
            };
            
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex==4 ||columnIndex==3 ||columnIndex==5 || columnIndex==6 || columnIndex==7 || columnIndex==8) {
                    return true;
                }
                return canEdit [columnIndex];
            }
        });
        setWidthColumns();
        
        tblPopupmds.setCellSelectionEnabled(true);
    //    ( (DefaultTableModel) tblPopupmds.getModel() ).addRow(new Object[]{"Inst.No","Description","Chittal No","Principal","Interest","Penal","Bonus","Arbitration","Notice"});
        //        tblPopupmds.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
        //            public void propertyChange(java.beans.PropertyChangeEvent evt) {
        //                tblTransactionPropertyChange(evt);
        //            }
        //        });
        
        // setTableModelListener();
        
    }
    public void setWidthColumns(){
        tblPopupmds.getColumnModel().getColumn(0).setPreferredWidth(30);
        tblPopupmds.getColumnModel().getColumn(1).setPreferredWidth(210);
        tblPopupmds.getColumnModel().getColumn(2).setPreferredWidth(110);
        tblPopupmds.getColumnModel().getColumn(3).setPreferredWidth(60);
        tblPopupmds.getColumnModel().getColumn(4).setPreferredWidth(50);
        tblPopupmds.getColumnModel().getColumn(5).setPreferredWidth(30);
        tblPopupmds.getColumnModel().getColumn(6).setPreferredWidth(50);
        tblPopupmds.getColumnModel().getColumn(7).setPreferredWidth(50);
        tblPopupmds.getColumnModel().getColumn(8).setPreferredWidth(50);
        tblPopupmds.getColumnModel().getColumn(8).setPreferredWidth(50);
        
        
        
    }
    public void setMDSTableTitle(){
        tableTitle.add("Sl No");
        tableTitle.add("Chittal No");
        tableTitle.add("Principal");
        tableTitle.add("Interest");
        tableTitle.add("Penal");
        tableTitle.add("Bonus");
        tableTitle.add("Arbitration");
        tableTitle.add("Notice");
        
        IncVal = new ArrayList();
    }
    private void setupScreen() {
        setModal(true);
        setTitle("MDS Details");
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("@$#@$#@# screenSize : "+screenSize);
        setSize(720, 280);
        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
    
    public  void getMdsdetail(ArrayList mdsList){
        listTot=new ArrayList();
        String noofinst=mdsList.get(0).toString();
        String actnum=mdsList.get(1).toString();
        String pid=mdsList.get(2).toString();
        String noticAmt = mdsList.get(3).toString();
        String arbitAmt = mdsList.get(4).toString();
        double  pendDueIn= Double.parseDouble(mdsList.get(5).toString());
        String desc=mdsList.get(6).toString();
        int no=Integer.parseInt(noofinst);
        System.out.println("new list is..."+mdsList);
        for(int i=0;i<no;i++){
            try{
                String chittalNo ="",subNo ="";
                String prId =pid;
                chittalNo = actnum;
                HashMap dataMap = new HashMap();
                dataMap.put("ACT_NUM",chittalNo );
                //  if (scheme_Name.equals("MDS"))
                //    {
                if (chittalNo.indexOf("_")!=-1) {
                    subNo = chittalNo.substring(chittalNo.indexOf("_")+1, chittalNo.length());
                    chittalNo = chittalNo.substring(0,chittalNo.indexOf("_"));
                }
                else
                        {
                           subNo="1"; 
                        }
                
                //   }
                String noOfInsPay =String.valueOf(i+1);
                String  prod_id= pid;
                // double  pendDueIn= CommonUtil.convertObjToDouble(tblTransaction .getValueAt(tblTransaction.getSelectedRow(),12));
                double  currIn= CommonUtil.convertObjToDouble(noOfInsPay).doubleValue();
                double remainInst=pendDueIn-currIn;
                System.out.println("remainInst ====================== "+remainInst);
                if(remainInst<0)
                    remainInst=0;
                //            //For Loop
                //              int selectedRow = tblTransaction.getSelectedRow();
                //            double principal1=0;double interest1=0;double penal1=0;double charges1=0;double bonus1=0;
                //                  for(int i = 0;i<globalList.size();i++){
                //                   ArrayList aList1=(ArrayList)globalList.get(i);
                //                   for(int j = 0;j<aList1.size();j++){
                //                                String PID=aList1.get(11).toString();
                //                                if(PID.equals(prod_id))
                //                                {
                //                                  //  if(aList.get(3)!=null)
                //                                       principal1=CommonUtil.convertObjToDouble(aList1.get(4).toString()).doubleValue();
                //                                   //  if(aList.get(5)!=null)
                //                                       interest1=CommonUtil.convertObjToDouble(aList1.get(6).toString()).doubleValue();
                //                                  //   if(aList.get(4)!=null)
                //                                       penal1=CommonUtil.convertObjToDouble(aList1.get(5).toString()).doubleValue();
                //                                   //  if(aList.get(7)!=null)
                //                                       charges1=CommonUtil.convertObjToDouble(aList1.get(8).toString()).doubleValue();
                //                                       bonus1=CommonUtil.convertObjToDouble(aList1.get(7).toString()).doubleValue();
                //                                      // displayAlert("principal ="+principal +" interest="+interest+" penal="+penal);
                //                                }
                //                         }
                //              }
                //             System.out.println("#####noOfInsPayst="+noOfInsPay);
                //                 if(noOfInsPay==null || noOfInsPay.equals(""))
                //                    {
                //                         System.out.println("#####I(*&&^%%%$#$$#@##@@22222222222222222="+noOfInsPay);
                //                        tblTransaction.setValueAt(principal1, selectedRow, 4);
                //                         tblTransaction.setValueAt(penal1, selectedRow, 5);//penalAmount
                //                         tblTransaction.setValueAt(interest1, selectedRow, 6);//instAmount
                //                        tblTransaction.setValueAt(bonus1, selectedRow, 7);
                //                        return;
                //                    }
                //            //End
                
                // String noticAmt = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(),9));
                //  String arbitAmt = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(),10));
                // String insDue = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(),3));
                String insDue ="";
                
                HashMap pendingMap = new HashMap();
                pendingMap.put("SCHEME_NAME",prId);
                pendingMap.put("CHITTAL_NO",chittalNo);
                System.out.println("######### subNo"+subNo);
                pendingMap.put("SUB_NO",CommonUtil.convertObjToInt(subNo));
                //            System.out.println("######### pendingMap"+pendingMap);
                List pendingAuthlst=ClientUtil.executeQuery("checkPendingForAuthorization", pendingMap);
                System.out.println("######### pendingAuthlst="+pendingAuthlst.size());
                System.out.println("######### noOfInsPay="+noOfInsPay);
                if(pendingAuthlst!=null && pendingAuthlst.size()>0){
                    ClientUtil.showMessageWindow(" Transaction pending for this Chittal... Please Authorize OR Reject first  !!! ");
                    // tblTransaction.setValueAt("", selectedRow, 3);
                }else if(CommonUtil.convertObjToDouble(noOfInsPay).doubleValue()>=1){
                    System.out.println("######### noOfInsPay IN="+noOfInsPay);
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
                    whereMap.put("SCHEME_NAME",prId);
                    List lst = ClientUtil.executeQuery("getSelectSchemeAcctHead",whereMap);
                    System.out.println("#######lst ==="+lst.size()+" LST----"+lst);
                    if(lst!=null && lst.size()>0){
                        productMap = (HashMap)lst.get(0);
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
                        long count=0;
                        long insDueAmt = 0;
                        whereMap.put("CHITTAL_NO",chittalNo);
                        whereMap.put("SUB_NO",CommonUtil.convertObjToInt(subNo));
                        List insList = ClientUtil.executeQuery("getNoOfInstalmentsPaid", whereMap);
                        System.out.println("#######insList ==="+insList.size() +"insList ==="+insList);
                        if(insList!=null && insList.size()>0){
                            whereMap = (HashMap)insList.get(0);
                            noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
                            count = CommonUtil.convertObjToLong(whereMap.get("NO_OF_INST"));
                        }
                        
                        HashMap chittalMap = new HashMap();
                        chittalMap.put("CHITTAL_NO",chittalNo);
                        chittalMap.put("SUB_NO",CommonUtil.convertObjToInt(subNo));
                        List chitLst = ClientUtil.executeQuery("getSelctApplnReceiptDetails", chittalMap);
                        System.out.println("#######chitLst ==="+chitLst.size() +"chitLst ==="+chitLst);
                        if(chitLst!=null &&chitLst.size()>0){
                            chittalMap = (HashMap)chitLst.get(0);
                            System.out.println("#######chittalMap ==="+chittalMap);
                            instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                            divNo = CommonUtil.convertObjToInt(chittalMap.get("DIVISION_NO"));
                            dataMap.put("BRANCH_CODE",CommonUtil.convertObjToStr(chittalMap.get("BRANCH_CODE")));
                            insAmt = CommonUtil.convertObjToDouble(chittalMap.get("INST_AMT")).doubleValue();
                        }
                        
                        HashMap insDateMap = new HashMap();
                        insDateMap.put("DIVISION_NO",divNo);
                        insDateMap.put("SCHEME_NAME",prId);
                        insDateMap.put("CURR_DATE",currDt.clone());
                        //                    insDateMap.put("ADD_MONTHS", "0");
                        insDateMap.put("ADD_MONTHS", CommonUtil.convertObjToInt("-1"));
                        List insDateLst = ClientUtil.executeQuery("getMDSCurrentInsDate", insDateMap);
                        System.out.println("#######insDateLst ==="+insDateLst.size() +"insDateLst ==="+insDateLst);
                        if(insDateLst!=null && insDateLst.size()>0){
                            insDateMap = (HashMap)insDateLst.get(0);
                            System.out.println("#######insDateMap ==="+insDateMap);
                            curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
                            pendingInst = curInsNo - noOfInsPaid;
                            if(instDay<currDate.getDate()){
                                pendingInst = pendingInst+1;
                            }
                            else{
                                pendingInst = pendingInst;
                            }
                            if(pendingInst<0){
                                pendingInst = 0;
                            }
                            insMonth = startMonth + curInsNo;
                            insDate.setMonth(insMonth);
                        }
                        
                        HashMap prizedMap = new HashMap();
                        double bonusAval=0;
                        prizedMap.put("SCHEME_NAME",prId);
                        prizedMap.put("DIVISION_NO",String.valueOf(divNo));
                        prizedMap.put("CHITTAL_NO",chittalNo);
                        prizedMap.put("SUB_NO",CommonUtil.convertObjToInt(subNo));
                        lst = ClientUtil.executeQuery("getSelectPrizedDetailsEntryRecords", prizedMap);
                        System.out.println("#######prizedMap ==="+lst.size() +"prizedMap ==="+lst);
                        if(lst!=null && lst.size()>0){
                            prizedMap = (HashMap)lst.get(0);
                            if(prizedMap.get("DRAW")!=null && !prizedMap.get("DRAW").equals("") && prizedMap.get("DRAW").equals("Y")){
                                 setRdoPrizedMember_Yes(true);
                            }
                            if(prizedMap.get("AUCTION")!=null && !prizedMap.get("AUCTION").equals("") && prizedMap.get("AUCTION").equals("Y")){
                                 setRdoPrizedMember_Yes(true);
                            }
                            bonusAval=  CommonUtil.convertObjToDouble(prizedMap.get("PRIZED_AMOUNT"));
                        }else{
                             setRdoPrizedMember_No(true);
                        }
                        System.out.println("#######totIns ==="+totIns +" noOfInsPaid=="+noOfInsPaid);
                        int balanceIns = totIns - noOfInsPaid;
                        if(balanceIns >= noOfInstPay){
                            long totDiscAmt = 0;
                            long penalAmt = 0;
                            double  netAmt = 0;
                            double  insAmtPayable = 0;
                            double totBonusAmt = 0;
                            double bonusAmt= 0;
                            String penalIntType = "";
                            long penalValue = 0;
                            String penalGraceType = "";
                            long penalGraceValue = 0;
                            String penalCalcBaseOn = "";
                            if(pendingInst>0){              //pending installment calculation starts...
                                insDueAmt = (long)insAmt * pendingInst;
                                int totPendingInst = (int)pendingInst;
                                double calc = 0;
                                long totInst = pendingInst;
                                penalCalcBaseOn = CommonUtil.convertObjToStr(productMap.get("PENAL_CALC"));
                                System.out.println("#######penalCalcBaseOn ===" + penalCalcBaseOn);
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
                            System.out.println("calculateIntOn"+calculateIntOn+" inst amt :"+productMap.get("INSTALLMENT_AMOUNT"));
                                if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                                    //double instAmount = 0.0;
                                    HashMap nextInstMaps = null;
                                    for (int n = startNoForPenal; n <= noOfInstPay - addNo; n++) {
                                        nextInstMaps = new HashMap();
                                        nextInstMaps.put("SCHEME_NAME", prId);
                                        nextInstMaps.put("DIVISION_NO", divNo);
                                        nextInstMaps.put("SL_NO", new Double(n + noOfInsPaid + addNo + firstInst_No));
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
                                    nextInstMap.put("SCHEME_NAME",prId);
                                    nextInstMap.put("DIVISION_NO",String.valueOf(divNo));
                                    nextInstMap.put("SL_NO",new Double(j+noOfInsPaid));
                                    List listRec = ClientUtil.executeQuery("getSelectNextInstDate", nextInstMap);
                                    if(listRec!=null && listRec.size()>0){
                                        double penal = 0;
                                        nextInstMap = (HashMap)listRec.get(0);
                                        instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                                        if (instDay > 0) {
                                            instDate.setDate(instDate.getDate() + instDay - 1);
                                        }
                                        diffDayPending = DateUtil.dateDiff(instDate, currDate);
                                        //Holiday Checking - Added By Suresh
                                        HashMap holidayMap = new HashMap();
                                        boolean checkHoliday=true;
                                        System.out.println("instDate   "+instDate);
                                        instDate = setProperDtFormat(instDate);
                                        System.out.println("instDate   "+instDate);
                                        holidayMap.put("NEXT_DATE",instDate);
                                        holidayMap.put("BRANCH_CODE","0001");
                                        while(checkHoliday){
                                            boolean tholiday = false;
                                            List Holiday=ClientUtil.executeQuery("checkHolidayDateOD",holidayMap);
                                            List weeklyOf=ClientUtil.executeQuery("checkWeeklyOffOD",holidayMap);
                                            boolean isHoliday = Holiday.size()>0 ? true : false;
                                            boolean isWeekOff = weeklyOf.size()>0 ? true : false;
                                            if (isHoliday || isWeekOff) {
                                                System.out.println("#### diffDayPending Holiday True : "+diffDayPending);
                                                if(CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")){
                                                    diffDayPending-=1;
                                                    instDate.setDate(instDate.getDate()+1);
                                                }else{
                                                    diffDayPending+=1;
                                                    instDate.setDate(instDate.getDate()-1);
                                                }
                                                holidayMap.put("NEXT_DATE",instDate);
                                                checkHoliday=true;
                                                System.out.println("#### holidayMap : "+holidayMap);
                                            }else{
                                                System.out.println("#### diffDay Holiday False : "+diffDayPending);
                                                checkHoliday=false;
                                            }
                                        }
                                        System.out.println("#### diffDayPending Final : "+diffDayPending);
                                        if (penalCalcBaseOn!=null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Days")) {
                                            if(penalGraceType!=null && !penalGraceType.equals("") && penalGraceType.equals("Days")){
                                                if(diffDayPending > penalGraceValue){
                                                    if(penalIntType!=null && !penalIntType.equals("") && penalIntType.equals("Percent")){
                                                        calc += (diffDayPending * penalValue * insAmt)/ 36500;
                                                    }else if(penalIntType!=null && !penalIntType.equals("") && penalIntType.equals("Absolute")){
                                                        calc += penalValue;
                                                    }
                                                }
                                            }else if(penalGraceType!=null && !penalGraceType.equals("") && penalGraceType.equals("Installments")){
                                                // To be written
                                                if(diffDayPending > penalGraceValue){
                                                    if(penalIntType!=null && !penalIntType.equals("") && penalIntType.equals("Percent")){
                                                        //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                                        calc += (double)((insAmt * penalValue)/1200.0)*pendingInst--;
                                                    }else if(penalIntType!=null && !penalIntType.equals("") && penalIntType.equals("Absolute")){
                                                        calc += penalValue;
                                                    }
                                                }
                                            }
                                        } else if (penalCalcBaseOn!=null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Installments")) {
                                            if(penalGraceType!=null && !penalGraceType.equals("") && penalGraceType.equals("Days")){
                                                if(diffDayPending > penalGraceValue){
                                                    if(penalIntType!=null && !penalIntType.equals("") && penalIntType.equals("Percent")){
                                                        calc += ((insAmt * penalValue)/1200.0)*pendingInst--;
                                                    }else if(penalIntType!=null && !penalIntType.equals("") && penalIntType.equals("Absolute") && totInst<=noOfInstPay){
                                                        calc += penalValue;
                                                    }
                                                }
                                            }else if(penalGraceType!=null && !penalGraceType.equals("") && penalGraceType.equals("Installments")){
                                                // To be written
                                                if(diffDayPending > penalGraceValue){
                                                    if(penalIntType!=null && !penalIntType.equals("") && penalIntType.equals("Percent")){
                                                        //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                                        calc += (double)((insAmt * penalValue)/1200.0)*pendingInst--;
                                                    }else if(penalIntType!=null && !penalIntType.equals("") && penalIntType.equals("Absolute")){
                                                        calc += penalValue;
                                                    }
                                                }
                                            }
                                        }
                                        //After Scheme End Date Penal Calculating
                                        if((j+1==noOfInstPay+startNoForPenal) && !(penalCalcBaseOn.equals("Days") && penalIntType.equals("Percent")) && (DateUtil.dateDiff(endDate,currDate)>0)){
                                            System.out.println("#### endDate : "+endDate);
                                            if(penalIntType.equals("Percent")){
                                                diffDayPending = DateUtil.dateDiff(endDate,currDate);
                                                System.out.println("#### endDate_diffDayPending : "+diffDayPending);
                                                calc += (double) ((((insAmt * noOfInstPay*penalValue)/100.0)*diffDayPending)/365);
                                            }
                                            // Absolute Not Required...
                                        }
                                        
                                        penal = (calc+0.5) - penal;
                                        nextInstMap.put("PENAL", String.valueOf(penal));
                                        installmentMap.put(String.valueOf(j+noOfInsPaid+addNo),nextInstMap);
                                        penal = calc+0.5;
                                    }
                                }
                                if(calc>0){
                                    penalAmt = (long)(calc+0.5);
                                    
                                }
                            }//pending installment calculation ends...
                            
                            
                            //Discount calculation details Starts...
                            for(int k = 0;k<noOfInstPay;k++){
                                HashMap nextInstMap = new HashMap();
                                nextInstMap.put("SCHEME_NAME",prId);
                                nextInstMap.put("DIVISION_NO",String.valueOf(divNo));
                                nextInstMap.put("SL_NO",new Double(k+noOfInsPaid));
                                List listRec = ClientUtil.executeQuery("getSelectNextInstDate", nextInstMap);
                                if(listRec==null || listRec.size()==0) {
                                    Date curDate = (Date) currDt.clone();
                                    int curMonth = curDate.getMonth();
                                    curDate.setMonth(curMonth+1);
                                    curDate.setDate(instDay);
                                    listRec = new ArrayList();
                                    nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                                    listRec.add(nextInstMap);
                                }
                                if(listRec!=null && listRec.size()>0){
                                    long discountAmt = 0;
                                    nextInstMap = (HashMap)listRec.get(0);
                                    instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                                    long diffDay = DateUtil.dateDiff(instDate,currDate);
                                    if(productMap.get("BONUS_ALLOWED")!=null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y") ||
                                    productMap.get("BONUS_ALLOWED").equals("N"))){
                                        String discount = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_ALLOWED"));
                                        if(discount!=null && !discount.equals("") && discount.equals("Y")){
                                            String discountType = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_RATE_TYPE"));
                                            long discountValue = CommonUtil.convertObjToLong(productMap.get("DISCOUNT_RATE_AMT"));
                                            if(getRdoPrizedMember_Yes() == true){//discount calculation for prized prerson...
                                                String discountPrizedDays = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_DAYS"));
                                                String discountPrizedMonth = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_MONTHS"));
                                                String discountPrizedAfter = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_AFTER"));
                                                String discountPrizedEnd = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_END"));
                                                long discountPrizedValue = CommonUtil.convertObjToLong(productMap.get("DIS_PRIZED_GRACE_PERIOD"));
                                                if(discountPrizedDays!=null && !discountPrizedDays.equals("") && discountPrizedDays.equals("D") && diffDay<=discountPrizedValue){
                                                    if(discountType!=null && !discountType.equals("") && discountType.equals("Percent")){
                                                        long calc = discountValue * (long)insAmt/100;
                                                        if(diffDay<=discountPrizedValue){
                                                            totDiscAmt = totDiscAmt + calc;
                                                        }
                                                    }else if(discountType!=null && !discountType.equals("") && discountType.equals("Absolute")){
                                                        if(diffDay<=discountPrizedValue){
                                                            totDiscAmt = totDiscAmt + discountValue;
                                                        }
                                                    }
                                                }else if(discountPrizedMonth!=null && !discountPrizedMonth.equals("") && discountPrizedMonth.equals("M") && diffDay<=(discountPrizedValue * 30) ){
                                                    if(discountType!=null && !discountType.equals("") && discountType.equals("Percent")){
                                                        long calc = discountValue * (long)insAmt/100;
                                                        totDiscAmt = totDiscAmt + calc;
                                                    }else if(discountType!=null && !discountType.equals("") && discountType.equals("Absolute")){
                                                        totDiscAmt = totDiscAmt + discountValue;
                                                    }
                                                }else if(discountPrizedAfter!=null && !discountPrizedAfter.equals("") && discountPrizedAfter.equals("A") && currDate.getDate()<= discountPrizedValue ){
                                                    if(discountType!=null && !discountType.equals("") && discountType.equals("Percent")){
                                                        long calc = discountValue * (long)insAmt/100;
                                                        totDiscAmt = totDiscAmt + calc;
                                                    }else if(discountType!=null && !discountType.equals("") && discountType.equals("Absolute")){
                                                        totDiscAmt = totDiscAmt + discountValue;
                                                    }
                                                }else if(discountPrizedEnd!=null && !discountPrizedEnd.equals("") && discountPrizedEnd.equals("E") && pendingInst<noOfInstPay){
                                                    if(discountType!=null && !discountType.equals("") && discountType.equals("Percent")){
                                                        long calc = discountValue * (long)insAmt/100;
                                                        totDiscAmt = totDiscAmt + calc;
                                                    }else if(discountType!=null && !discountType.equals("") && discountType.equals("Absolute")){
                                                        totDiscAmt = totDiscAmt + discountValue;
                                                    }
                                                }else{
                                                    totDiscAmt = 0;
                                                }
                                            }else if(getRdoPrizedMember_No() == true){//discount calculation non prized person...
                                                String discountGraceDays = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_DAYS"));
                                                String discountGraceMonth = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_MONTHS"));
                                                String discountGraceAfter = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_AFTER"));
                                                String discountGraceEnd = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_END"));
                                                long discountGraceValue = CommonUtil.convertObjToLong(productMap.get("DIS_GRACE_PERIOD"));
                                                if(discountGraceDays!=null && !discountGraceDays.equals("") && discountGraceDays.equals("D")){
                                                    if(discountType!=null && !discountType.equals("") && discountType.equals("Percent")) {
                                                        long calc = discountValue * (long)insAmt/100;
                                                        if(diffDay<=discountGraceValue){
                                                            totDiscAmt = totDiscAmt + calc;
                                                        }
                                                    }else if(discountType!=null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                        if(diffDay<=discountGraceValue){
                                                            totDiscAmt = totDiscAmt + discountValue;
                                                        }
                                                    }else{
                                                        totDiscAmt = 0;
                                                    }
                                                }else if(discountGraceDays!=null && !discountGraceDays.equals("") && discountGraceDays.equals("M") && diffDay<=discountGraceValue * 30 && pendingInst<noOfInstPay){
                                                    if(discountType!=null && !discountType.equals("") && discountType.equals("Percent")){
                                                        long calc = discountValue * (long)insAmt/100;
                                                        totDiscAmt = totDiscAmt + calc;
                                                    }else if(discountType!=null && !discountType.equals("") && discountType.equals("Absolute")){
                                                        totDiscAmt = totDiscAmt + discountValue;
                                                    }
                                                }else if(discountGraceDays!=null && !discountGraceDays.equals("") && discountGraceDays.equals("A") && currDate.getDate()<= discountGraceValue && pendingInst<noOfInstPay){
                                                    if(discountType!=null && !discountType.equals("") && discountType.equals("Percent")){
                                                        long calc = discountValue * (long)insAmt/100;
                                                        totDiscAmt = totDiscAmt + calc;
                                                    }else if(discountType!=null && !discountType.equals("") && discountType.equals("Absolute")){
                                                        totDiscAmt = totDiscAmt + discountValue;
                                                    }
                                                }else if(discountGraceDays!=null && !discountGraceDays.equals("") && discountGraceDays.equals("E") && pendingInst<noOfInstPay){
                                                    if(discountType!=null && !discountType.equals("") && discountType.equals("Percent")){
                                                        long calc = discountValue * (long)insAmt/100;
                                                        totDiscAmt = totDiscAmt + calc;
                                                    }else if(discountType!=null && !discountType.equals("") && discountType.equals("Absolute")){
                                                        totDiscAmt = totDiscAmt + discountValue;
                                                    }
                                                }else{
                                                    totDiscAmt = 0;
                                                }
                                            }
                                        }else if(discount!=null && !discount.equals("") && discount.equals("N")){
                                            totDiscAmt = 0;
                                        }
                                        discountAmt = totDiscAmt - discountAmt;
                                        HashMap instMap = new HashMap();
                                        if (installmentMap.containsKey(String.valueOf(k+noOfInsPaid+1))) {
                                            instMap = (HashMap)installmentMap.get(String.valueOf(k+noOfInsPaid+1));
                                            instMap.put("DISCOUNT", String.valueOf(discountAmt));
                                            installmentMap.put(String.valueOf(k+noOfInsPaid+1),instMap);
                                        }
                                        discountAmt = totDiscAmt;
                                    }
                                    
                                }
                            }
                            
                            
                            //Bonus calculation details Starts...         
                                            for(int l = startNoForPenal;l<=noOfInstPay-addNo;l++){
                            //                    for(int l = 1;l<=noOfInstPay;l++){
                            HashMap nextInstMap = new HashMap();
                            nextInstMap.put("SCHEME_NAME",prId);
                            nextInstMap.put("DIVISION_NO",divNo);
                            nextInstMap.put("SL_NO",new Double(l+noOfInsPaid+addNo+firstInst_No));
                            List listRec = ClientUtil.executeQuery("getSelectBonusAndNextInstDateWithoutDivision", nextInstMap);
                            if(listRec==null || listRec.size()==0) {
                                Date curDate = (Date) currDt.clone();
                                int curMonth = curDate.getMonth();
                                curDate.setMonth(curMonth+1);
                                curDate.setDate(instDay);
                                listRec = new ArrayList();
                                nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                                listRec.add(nextInstMap);
                                bonusAvailabe = false;
                            }
                            if(listRec!=null && listRec.size()>0){
                                nextInstMap = (HashMap)listRec.get(0);
                                instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                                bonusAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_BONUS_AMOUNT"))).doubleValue();
                                if(!productMap.get("MULTIPLE_MEMBER").equals("") && (productMap.get("MULTIPLE_MEMBER").equals("Y"))){
                                    whereMap = new HashMap();
                                    int noOfCoChittal = 0;
                                    whereMap.put("SCHEME_NAME",prId);
                                    whereMap.put("CHITTAL_NUMBER",chittalNo);
                                    List applicationLst=ClientUtil.executeQuery("getSelectChitNoNotinMasterDetails", whereMap); // Count No Of Co-Chittals
                                    if(applicationLst!=null && applicationLst.size()>0){
                                        noOfCoChittal = applicationLst.size();
                                        bonusAmt=bonusAmt/noOfCoChittal;
                                    }
                                }
                                if(productMap.get("BONUS_ROUNDING")!=null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                && bonusAmt>0 ){
                                    Rounding rod =new Rounding();
                                    bonusAmt = (double)rod.getNearest((long)(bonusAmt *100),100)/100;
                                }
                                long diffDay = DateUtil.dateDiff(instDate,currDate);
                                //                            String hoildayInt = CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT"));
                                if(productMap.get("BONUS_ALLOWED")!=null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y"))){
                                    if(bonusAvailabe == true){
                                        if(getRdoPrizedMember_Yes() == true){
                                            String bonusPrizedDays = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_DAYS"));
                                            String bonusPrizedMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
                                            String bonusPrizedAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_AFT"));
                                            String bonusPrizedEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_END"));
                                            long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
                                            if(bonusPrizedDays!= null && !bonusPrizedDays.equals("") && bonusPrizedDays.equals("D") && diffDay<=bonusPrizedValue){
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            }else if(bonusPrizedMonth!=null && !bonusPrizedMonth.equals("") && bonusPrizedMonth.equals("M") && diffDay<=(bonusPrizedValue * 30)){
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            }else if(bonusPrizedAfter!=null && !bonusPrizedAfter.equals("") && bonusPrizedAfter.equals("A") && currDate.getDate()<= bonusPrizedValue){
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            }else if(bonusPrizedEnd!=null && !bonusPrizedEnd.equals("") && bonusPrizedEnd.equals("E")){
                                                
                                            }else{
                                                
                                            }
                                        }else if(getRdoPrizedMember_No() == true){
                                            String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
                                            String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
                                            String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
                                            String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
                                            long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
                                            if(bonusGraceDays!= null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay<=bonusGraceValue ){
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            }else if(bonusGraceMonth!=null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay<=(bonusGraceValue * 30) ){
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            }else if(bonusGraceOnAfter!=null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && currDate.getDate()<= bonusGraceValue ){
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            }else if(bonusGraceEnd!=null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E") ){
                                                
                                            }else{
                                            }
                                        }
                                    }
                                    HashMap instMap = new HashMap();
                                    if (installmentMap.containsKey(String.valueOf(l+noOfInsPaid+addNo))) {
                                        Rounding rod =new Rounding();
                                        instMap = (HashMap)installmentMap.get(String.valueOf(l+noOfInsPaid+addNo));
                                        //Added By Suresh
                                        if(productMap.get("BONUS_ROUNDING")!=null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                        && bonusAmt>0 ){
                                            bonusAmt = (double)rod.getNearest((long)(bonusAmt *100),100)/100;
                                        }
                                        instMap.put("BONUS", String.valueOf(bonusAmt));
                                        installmentMap.put(String.valueOf(l+noOfInsPaid+addNo),instMap);
                                    }
                                }
                            }
                            bonusAmt= 0;
                        }
                        if(productMap.get("BONUS_ROUNDING")!=null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                        && totBonusAmt>0 ){
                            Rounding rod =new Rounding();
                            totBonusAmt = (double)rod.getNearest((long)(totBonusAmt *100),100)/100;
                        }
                            
                            int insDay = 0;
                            Date paidUpToDate = null;
                            HashMap instDateMap = new HashMap();
                            instDateMap.put("SCHEME_NAME",prId);
                            instDateMap.put("DIVISION_NO", divNo);
                            instDateMap.put("INSTALLMENT_NO",CommonUtil.convertObjToInt(String.valueOf(count)));
                            lst = ClientUtil.executeQuery("getSelectInstUptoPaid", instDateMap);
                            if(lst!=null && lst.size()>0){
                                instDateMap = (HashMap)lst.get(0);
                                paidUpToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(instDateMap.get("NEXT_INSTALLMENT_DATE")));
                            }else{
                                Date startedDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("SCHEME_START_DT")));
                                insDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                                startedDate.setDate(insDay);
                                int stMonth = startedDate.getMonth();
                                startedDate.setMonth(stMonth+(int)count-1);
                                paidUpToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(startedDate));
                            }
                            
                            String narration = "";
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM/yyyy");
                            int noInstPay = CommonUtil.convertObjToInt(noOfInsPay);
                            if (noInstPay == 1) {
                                narration = "Inst#"+(noOfInsPaid+1);
                                Date dt = DateUtil.addDays(paidUpToDate, 30);
                                narration+=" "+sdf.format(dt);
                            } else if (noInstPay > 1) {
                                narration = "Inst#"+(noOfInsPaid+1);
                                narration+="-"+(noOfInsPaid+noInstPay);
                                Date dt = DateUtil.addDays(paidUpToDate, 30);
                                narration+=" "+sdf.format(dt);
                                dt = DateUtil.addDays(paidUpToDate, 30*noInstPay);
                                narration+=" To "+sdf.format(dt);
                            }
                            System.out.println("#$#$# narration :"+narration);
                            if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                                insAmt = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT"));
                            }
                            double instAmt = insAmt * CommonUtil.convertObjToDouble(noOfInsPay).doubleValue();
                            double totalPayable = instAmt-(totBonusAmt+totDiscAmt);
                            netAmt = totalPayable + penalAmt + CommonUtil.convertObjToDouble(noticAmt).doubleValue()
                            + CommonUtil.convertObjToDouble(arbitAmt).doubleValue();
                            String totalPayableAmount = String.valueOf(totalPayable);
                            totalPayableAmount = CurrencyValidation.formatCrore(totalPayableAmount).replaceAll(",", "");
                            String penalAmount = String.valueOf(penalAmt);
                            penalAmount = CurrencyValidation.formatCrore(penalAmount).replaceAll(",", "");
                            dataMap.put("SCHEME_NAME",prId);
                            dataMap.put("CHITTAL_NO",chittalNo);
                            dataMap.put("PRINCIPAL",totalPayableAmount);
                            dataMap.put("INTEREST",penalAmount);
                            dataMap.put("SUB_NO",CommonUtil.convertObjToInt(subNo));
                            //                    dataMap.put("MEMBER_NAME",txtName.getText());//lblMemberName.getText()
                            dataMap.put("DIVISION_NO",String.valueOf(divNo));
                            dataMap.put("CHIT_START_DT",startDate);
                            dataMap.put("INSTALLMENT_DATE",insDate);
                            dataMap.put("NO_OF_INSTALLMENTS",noOfInsPay);//String.valueOf(remainInst));//String.valueOf(totIns));
                            dataMap.put("CURR_INST",String.valueOf(curInsNo));
                            dataMap.put("PENDING_INST",insDue);
                            dataMap.put("PENDING_DUE_AMT",String.valueOf(insDueAmt));
                            dataMap.put("NO_OF_INST_PAY",noOfInsPay);
                            dataMap.put("INST_AMT_PAYABLE",String.valueOf(totalPayable));
                            dataMap.put("PAID_INST",String.valueOf(noOfInsPaid));
                            dataMap.put("TOTAL_PAYABLE",String.valueOf(instAmt));
                            dataMap.put("PAID_DATE",currDate);
                            dataMap.put("INST_AMT",String.valueOf(insAmt));
                            dataMap.put("SCHEME_END_DT",endDate);
                            if(getRdoPrizedMember_Yes() == true){
                                dataMap.put("PRIZED_MEMBER","Y");
                            }else{
                                dataMap.put("PRIZED_MEMBER","N");
                            }
                            dataMap.put("BONUS_AVAL",String.valueOf(bonusAval));
                            dataMap.put("BONUS",String.valueOf(totBonusAmt));
                            dataMap.put("DISCOUNT",String.valueOf(totDiscAmt));
                            dataMap.put("PENAL",String.valueOf(penalAmt));
                            dataMap.put("NOTICE_AMOUNT",noticAmt);
                            dataMap.put("ARBITRATION_AMOUNT",arbitAmt);
                            dataMap.put("NET_AMOUNT",String.valueOf(netAmt));
                            dataMap.put("TOTAL_DEMAND",String.valueOf(netAmt));
                            ////
                            dataMap.put("NARRATION",narration);
                            dataMap.put("EACH_MONTH_DATA",installmentMap);
                            dataMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                            dataMap.put("PROD_TYPE","MDS");
                            dataMap.put("TOTAL",noOfInsPay);
                            dataMap.put("AMTORNOOFINST",noOfInsPay);
                            //  dataMap.put("CLOCK_NO",txtClockNo.getText());
                            //               dataMap.put("MEMBER_NO",txtMemberNo.getText());
                            //                dataMap.put("CUST_NAME",txtName.getText());
                            //                dataMap.put("TOT_PRIN",txtTotPrincipal.getText());
                            //                 dataMap.put("TOT_PENAL",txtTotPenel.getText());
                            //                  dataMap.put("TOT_INT",txtTotInterest.getText());
                            //                   dataMap.put("TOT_OTHERS",txtTotOthers.getText());
                            //                    dataMap.put("TOT_GRAND",txtGrandTotal.getText());
                            //                     dataMap.put("CHARGES", new Double(0));
                            //                    if(CommonUtil.convertObjToDouble(noOfInsPay)>0){
                            //                        termMdsMap=new HashMap();
                            //                        termMdsMap.put("MDS",dataMap);
                            //                        finalMap.put(chittalNo,termMdsMap);
                            //                    }
                            //                    else
                            //                      dataMap=null;
                            
                            //                    System.out.println("###### insAmt        : "+insAmt);
                            //                    System.out.println("###### insAmtPayable : "+instAmt);
                            //                    System.out.println("###### totBonusAmt   : "+totBonusAmt);
                            //                    System.out.println("###### totDiscAmt    : "+totDiscAmt);
                            //                    System.out.println("###### totalPayable  : "+totalPayable);
                            //                    System.out.println("###### penalAmt      : "+penalAmt);
                            //                    System.out.println("###### netAmt        : "+netAmt);
                            //                    System.out.println("###### dataMap         : "+dataMap);
                            //   System.out.println("###### finalMap        : "+finalMap);
                            System.out.println("###### installmentMap  :"+installmentMap);
                            System.out.println("###### dataMap  :"+dataMap);
                            String instAmount = String.valueOf(instAmt);
                            instAmount = CurrencyValidation.formatCrore(instAmount).replaceAll(",", "");
                            
                            String totBonusAmount = String.valueOf(totBonusAmt);
                            totBonusAmount = CurrencyValidation.formatCrore(totBonusAmount).replaceAll(",", "");
                            
                            String totDiscAmount = String.valueOf(totDiscAmt);
                            totDiscAmount = CurrencyValidation.formatCrore(totDiscAmount).replaceAll(",", "");
                            
                            
                            
                            
                            
                            String netAmount = String.valueOf(netAmt);
                            netAmount = CurrencyValidation.formatCrore(netAmount).replaceAll(",", "");
                            
                            System.out.println("i,chittalNo,totalPayableAmount,penalAmount,penalAmt,totBonusAmt,arbitAmt,noticAmt"+i+chittalNo+"   "+totalPayableAmount
                            +" "+penalAmount+" "+penalAmt+" "+totBonusAmt+" "+arbitAmt+" "+noticAmt);
                            if(CommonUtil.convertObjToDouble(noOfInsPay).doubleValue()!=0) {
                                System.out.println("VALITYYUUUUUUUUUUUUUUUUUUUU==="+CommonUtil.convertObjToDouble(noOfInsPay).doubleValue());
                                //    ( (DefaultTableModel) tblPopupmds.getModel() ).addRow(new Object[]{i+1,chittalNo,totalPayableAmount,penalAmount,penalAmt,totBonusAmt,arbitAmt,noticAmt});
                                
                                listadd=new ArrayList();
                                listadd.add(i+(noOfInsPaid+1));
                                listadd.add(desc);
                                listadd.add(chittalNo);
                                listadd.add(totalPayableAmount);
                                listadd.add(penalAmount);
                                listadd.add(penalAmt);
                                listadd.add(totBonusAmt);
                                listadd.add(arbitAmt);
                                listadd.add(noticAmt);
                                listTot.add(listadd);
                                
                                
                                //                        tblTransaction.setValueAt(totalPayableAmount, selectedRow, 4);
                                //                     tblTransaction.setValueAt("0.0", selectedRow, 5);//penalAmount
                                //                     tblTransaction.setValueAt(penalAmount, selectedRow, 6);//instAmount
                                //                    tblTransaction.setValueAt(totBonusAmount, selectedRow, 7);
                            }
                            else {
                                //                        tblTransaction.setValueAt(principal1, selectedRow, 4);
                                //                         tblTransaction.setValueAt("0.0", selectedRow, 5);//penalAmount
                                //                         tblTransaction.setValueAt(interest1, selectedRow, 6);//instAmount
                                //                        tblTransaction.setValueAt(bonus1, selectedRow, 7);
                            }
                            //  tblTransaction.setValueAt(instAmount, selectedRow, 5);
                            //  tblTransaction.setValueAt(totBonusAmount, selectedRow, 6);
                            //  tblTransaction.setValueAt(totDiscAmount, selectedRow, 7);
                            //  tblTransaction.setValueAt(totalPayableAmount, selectedRow, 8);
                            //  tblTransaction.setValueAt(penalAmount, selectedRow, 9);
                            //  tblTransaction.setValueAt(netAmount, selectedRow, 12);
                            //  tblTransaction.revalidate();
                            
                            
                        }else{
                            ClientUtil.showAlertWindow("Exceeds The No Of Total Installment !!! ");
                            //                         tblTransaction.setValueAt("",tblTransaction.getSelectedRow(),3);
                            //                         tblTransaction.setValueAt("",tblTransaction.getSelectedRow(),4);
                            //                        tblTransaction.setValueAt("",tblTransaction.getSelectedRow(),5);
                            //                        tblTransaction.setValueAt("",tblTransaction.getSelectedRow(),6);
                            //                        tblTransaction.setValueAt("",tblTransaction.getSelectedRow(),7);
                            //                        tblTransaction.setValueAt("",tblTransaction.getSelectedRow(),8);
                            //                        tblTransaction.setValueAt("",tblTransaction.getSelectedRow(),9);
                            //                        tblTransaction.setValueAt("",tblTransaction.getSelectedRow(),10);
                            // tblTransaction.setValueAt("",tblTransaction.getSelectedRow(),12);
                        }
                        
                    }
                }
            }catch(Exception e){
                parseException.logException(e,true);
                e.printStackTrace();
            }
        }
        
        ArrayList newadd=null;
        for(int i=0;i<listTot.size();i++){
            newadd=new ArrayList();
            if(i==0){
                newadd=(ArrayList)listTot.get(0);
                newadd.set(5, 0);
                newTot.add(newadd);
            }
            else{
                List aList=(List) listTot.get(i);
                List aList1=(List) listTot.get(i-1);
                newadd.add(aList.get(0));
                newadd.add(aList.get(1));
                newadd.add(aList.get(2));
                newadd.add((Double.parseDouble(aList.get(3).toString()))-(Double.parseDouble(aList1.get(3).toString())));
                newadd.add((Double.parseDouble(aList.get(4).toString()))-(Double.parseDouble(aList1.get(4).toString())));
                newadd.add(0);
                newadd.add((Double.parseDouble(aList.get(6).toString()))-(Double.parseDouble(aList1.get(6).toString())));
                newadd.add((Double.parseDouble(aList.get(7).toString()))-(Double.parseDouble(aList1.get(7).toString())));
                newadd.add((Double.parseDouble(aList.get(8).toString()))-(Double.parseDouble(aList1.get(8).toString())));
                newTot.add(newadd);
                
                // newadd.set(0, [0]);
            }
            //   i+1,chittalNo,totalPayableAmount,penalAmount,penalAmt,totBonusAmt,arbitAmt,noticAmt
            String dfamt="";
            int instno=Integer.parseInt(newadd.get(0).toString());
            String  chittalNo=((newadd.get(2).toString()));
            String descript=((newadd.get(1).toString()));
            double   totalPayableAmount=(Double.parseDouble(newadd.get(3).toString()));
           dfamt=df.format(totalPayableAmount);
           totalPayableAmount=Double.parseDouble(dfamt);
            double    penalAmount=(Double.parseDouble(newadd.get(4).toString()));
            dfamt=df.format(penalAmount);
           penalAmount=Double.parseDouble(dfamt);
            double    penalAmt=0.0;
            double   totBonusAmt=(Double.parseDouble(newadd.get(6).toString()));
            dfamt=df.format(totBonusAmt);
           totBonusAmt=Double.parseDouble(dfamt);
            double    arbitAmt1=0.0;
            if(!newadd.get(7).toString().equals("")){
                arbitAmt1=(Double.parseDouble(newadd.get(7).toString()));
                 dfamt=df.format(arbitAmt1);
                arbitAmt1=Double.parseDouble(dfamt);
            }
            double     noticAmt1=0.0;
            if(!newadd.get(8).toString().equals("")){
                noticAmt1 =(Double.parseDouble(newadd.get(8).toString()));
                 dfamt=df.format(noticAmt1);
                noticAmt1=Double.parseDouble(dfamt);
            }
            ( (DefaultTableModel) tblPopupmds.getModel() ).addRow(new Object[]{instno,descript,chittalNo,totalPayableAmount,penalAmount,penalAmt,totBonusAmt,arbitAmt1,noticAmt1});
            
        }
        setNewTot(newTot);
        //  System.out.println("tableTitle----tableTitle"+tableTitle);
        // model.addRow(listTot);
        //  model=new EnhancedTableModel((ArrayList)newTot,(ArrayList)tableTitle);
        //tblPopupmds.setModel(model);
        //System.out.println("modellll"+tblPopupmds.getModel());
        
        //  System.out.println("intre1 intre1"+trans.getInterest());
        // return  intre;
    }
    
    private Date setProperDtFormat(Date dt) {
        Date tempDt=(Date)currDt.clone();
        if(dt!=null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    
    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths(){
        
    }
    
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
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
        cButton1 = new com.see.truetransact.uicomponent.CButton();
        cScrollMds = new com.see.truetransact.uicomponent.CScrollPane();
        tblPopupmds = new com.see.truetransact.uicomponent.CTable();

        setMinimumSize(new java.awt.Dimension(800, 360));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panMemberShipFacility.setMaximumSize(new java.awt.Dimension(800, 360));
        panMemberShipFacility.setMinimumSize(new java.awt.Dimension(800, 360));
        panMemberShipFacility.setPreferredSize(new java.awt.Dimension(800, 360));
        panMemberShipFacility.setLayout(null);

        cButton1.setText("OK");
        cButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cButton1ActionPerformed(evt);
            }
        });
        panMemberShipFacility.add(cButton1);
        cButton1.setBounds(290, 290, 60, 20);

        cScrollMds.setMaximumSize(new java.awt.Dimension(750, 400));
        cScrollMds.setMinimumSize(new java.awt.Dimension(750, 400));
        cScrollMds.setPreferredSize(new java.awt.Dimension(750, 400));

        tblPopupmds.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl No", "Chittal No", "Principal", "Interest", "Penal", "Bonus", "Arbit.Amoumt", "Notice Amt"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPopupmds.setMaximumSize(new java.awt.Dimension(750, 900));
        tblPopupmds.setMinimumSize(new java.awt.Dimension(750, 900));
        tblPopupmds.setPreferredScrollableViewportSize(new java.awt.Dimension(750, 1500));
        tblPopupmds.setPreferredSize(new java.awt.Dimension(750, 900));
        tblPopupmds.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPopupmdsMouseClicked(evt);
            }
        });
        cScrollMds.setViewportView(tblPopupmds);

        panMemberShipFacility.add(cScrollMds);
        cScrollMds.setBounds(0, 0, 760, 280);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panMemberShipFacility, gridBagConstraints);
        panMemberShipFacility.getAccessibleContext().setAccessibleName("MembershipFacifility");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void cButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cButton1ActionPerformed
        // TODO add your handling code here:
        double principal=0.0;
        double intre=0.0;
         // System.out.println("new00"+newTot);
        for(int i=0;i<tblPopupmds.getRowCount();i++){
            //            List aList2=(List) newTot.get(i);
            double tot=Double.parseDouble(tblPopupmds.getValueAt(i, 4).toString());
            intre=intre+tot;
             List alist=(List) newTot.get(i);
             alist.set(4, tblPopupmds.getValueAt(i, 4).toString());         
        }
        setNewTot(newTot);
        
    //    System.out.println("intre intre"+intre);
        // interest=intre;
        setInterest(intre);
        trans.setInterest(intre);
        this.dispose();
    }//GEN-LAST:event_cButton1ActionPerformed
    
    private void tblPopupmdsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPopupmdsMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tblPopupmdsMouseClicked
    
    
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
        //new MDSDetailsUI.;
    }
    
    /**
     * Getter for property rdoPrizedMember_Yes.
     * @return Value of property rdoPrizedMember_Yes.
     */
    public boolean getRdoPrizedMember_Yes() {
        return rdoPrizedMember_Yes;
    }
    
    /**
     * Setter for property rdoPrizedMember_Yes.
     * @param rdoPrizedMember_Yes New value of property rdoPrizedMember_Yes.
     */
    public void setRdoPrizedMember_Yes(boolean rdoPrizedMember_Yes) {
        this.rdoPrizedMember_Yes = rdoPrizedMember_Yes;
    }
    
    /**
     * Getter for property rdoPrizedMember_No.
     * @return Value of property rdoPrizedMember_No.
     */
    public boolean getRdoPrizedMember_No() {
        return rdoPrizedMember_No;
    }
    
    /**
     * Setter for property rdoPrizedMember_No.
     * @param rdoPrizedMember_No New value of property rdoPrizedMember_No.
     */
    public void setRdoPrizedMember_No(boolean rdoPrizedMember_No) {
        this.rdoPrizedMember_No = rdoPrizedMember_No;
    }
    
    /**
     * Getter for property interest.
     * @return Value of property interest.
     */
    public double getInterest() {
        return interest;
    }
    
    /**
     * Setter for property interest.
     * @param interest New value of property interest.
     */
    public void setInterest(double interest) {
        this.interest = interest;
    }
    
    /**
     * Getter for property newTot.
     * @return Value of property newTot.
     */
    public ArrayList getNewTot() {
        return newTot;
    }
    
    /**
     * Setter for property newTot.
     * @param newTot New value of property newTot.
     */
    public void setNewTot(ArrayList newTot) {
        this.newTot = newTot;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton cButton1;
    private com.see.truetransact.uicomponent.CScrollPane cScrollMds;
    private com.see.truetransact.uicomponent.CPanel panMemberShipFacility;
    private com.see.truetransact.uicomponent.CTable tblPopupmds;
    // End of variables declaration//GEN-END:variables
    
}
