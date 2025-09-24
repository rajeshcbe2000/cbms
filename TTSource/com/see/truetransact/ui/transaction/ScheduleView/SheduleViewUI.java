/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SheduleViewUI.java
 *
 * Created on September 28, 2011, 3:03 PM
 */
package com.see.truetransact.ui.transaction.ScheduleView;
import com.see.truetransact.ui.transaction.report.*;

import com.see.truetransact.clientutil.ClientUtil;
import java.util.HashMap;
import com.see.truetransact.clientutil.ClientConstants;
import java.util.List;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.viewall.ViewAll;
import java.util.Date;
import java.math.BigDecimal;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.Enumeration;
import java.text.SimpleDateFormat;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uicomponent.CDialog;
import com.see.truetransact.uicomponent.CInternalFrame;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;
/**
 *
 * @author  user
 */
public class SheduleViewUI extends CInternalFrame    {
    DefaultTableModel model = null;
  //  DefaultTreeModel root;
 //   DefaultTreeModel child;
    Date currDt = null;
    
    private TableModelListener tableModelListener;
    public String transMode="";
    public String trans_pid="";
    public String clsbal="";
    public     String Opamount="",
    amount1=""
    ,countVal="",amount="",sc_Name="",cname="",ac_No="",ac_HD_No=null,prod_Type="",prod_Id=null,trans_id="",trans_type="",
    posted_by="",autho_by="",particulars="",panNo="",token_No="",instr_Type="",
    instr_No="",instr_date="",clear_Bal="";
    DefaultMutableTreeNode selectedNode = null;
    String lblAmt="",lblTransTy;
    private ComboBoxModel cbmProdType;
    private ComboBoxModel cbmProdId;
    private ComboBoxModel cbmSchedule;
    private ComboBoxModel cbmbranch;
    private ComboBoxModel cbmAccountType;

    public ComboBoxModel getCbmAccountType() {
        return cbmAccountType;
    }

    public void setCbmAccountType(ComboBoxModel cbmAccountType) {
        this.cbmAccountType = cbmAccountType;
    }

   
    

    public ComboBoxModel getCbmbranch() {
        return cbmbranch;
    }

    public void setCbmbranch(ComboBoxModel cbmbranch) {
        this.cbmbranch = cbmbranch;
    }
    public ComboBoxModel getCbmSchedule() {
        return cbmSchedule;
    }

    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private final static Logger log = Logger.getLogger(SheduleViewUI.class);
      private final String SHARE_TYPE = "getShareType";  // Added by Rajesh   
    public SheduleViewUI() {
       // this.frm=frm;
      //  trans_pid=TransId;
        lblAmt=amount;
        //lblTransTy=TransTy;
        currDt = ClientUtil.getCurrentDate();
        initComponents();
         setupScreen();
//        initTableData();
//      this.setBounds(0,0, 500,330);
//      setSize(570, 280);
         btnLoansInstSchedul.setVisible(false);
         btnFDSubdayBook.setVisible(false);
         lblAccNo.setVisible(false);
         txtAccNo.setVisible(false);
         btnAccNo.setVisible(false);
         cboAccountType.setVisible(false);
         lblAccountType.setVisible(false);
        this.setVisible(true);
        try {
            fillData1();
            fillDataSchedule();
            fillBranch();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SheduleViewUI.class.getName()).log(Level.SEVERE, null, ex);
        }
       // this.setTitle("Change");
       
//        getAcHdTree("PENDING");
    //    cboStatus.setSelectedItem("Pending");
//        tblData.getModel().addTableModelListener(tableModelListener);
     //   btnReject.setVisible(false);
        
    }
      private void setupScreen() {
     //   setModal(true);
        setTitle("Account Number For GL ["+ProxyParameters.BRANCH_ID+"]");
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
      
      public void fillData(Object param) {
          System.out.println("In fillData test...............");
        final HashMap hash = (HashMap) param;
        System.out.println("param DATE ======" + param);
        System.out.println("HASH DATE ======" + hash);
        
        
        if(cboProdType.getSelectedItem()!=null && !cboProdType.getSelectedItem().equals(""))
          {
          String prod_type1=cboProdType.getSelectedItem().toString();
           if(prod_type1.equals("Share") || prod_type1.equals("MMBS") || prod_type1.equals("Investments") || prod_type1.equals("Borrowings"))
           {
               if(prod_type1.equals("Share"))
               {
                   txtAccNo.setText(hash.get("SHARE_ACCT_NO").toString());
               }
               if(prod_type1.equals("Borrowings"))
               {
                   txtAccNo.setText(hash.get("BORROWING_NO").toString());
               }
               if(prod_type1.equals("Investments"))
               {
                   txtAccNo.setText(hash.get("INVESTMENT_REF_NO").toString());
               }
                if(prod_type1.equals("MMBS"))
               {
                   txtAccNo.setText(hash.get("CHITTAL_NO").toString());
           }
          }
        else
        {
            if(prod_type1.equals("General Ledger")){
               String acHdId = hash.get("A/C HEAD DESCRIPTION").toString();
//            String bankType = CommonConstants.BANK_TYPE;
//            System.out.println("bankType" + bankType);
//            String customerAllow = "";
//            String hoAc = "";
//            cboProdId.setSelectedItem("");
//            this.txtAccNo.setText("");
            txtAccNo.setText(acHdId); 
            }else{
        String acHdId = hash.get("ACCOUNTNO").toString();
//            String bankType = CommonConstants.BANK_TYPE;
//            System.out.println("bankType" + bankType);
//            String customerAllow = "";
//            String hoAc = "";
//            cboProdId.setSelectedItem("");
//            this.txtAccNo.setText("");
            txtAccNo.setText(acHdId);
            }
        }
        }
           // CashTransactionUI cashUI = new CashTransactionUI();
           // frm.fillAccNo(acHdId);
      }

//    public void insertIntoDenaomination() {
//        try {
//            for (int i = 0; i<= 14; i++) {
//                if(tblData.getModel().getValueAt(i,2)!=null &&
//                tblData.getModel().getValueAt(i,3)!=null ) {
//                  //  if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
//                        //totalPay += ((Double) tblData.getModel().getValueAt(i,2)).doubleValue();
//                        //System.out.println("33-=="+tblData.getModel().getValueAt(i,1));
//                        //System.out.println("22==="+tblData.getModel().getValueAt(i,2));
//                        String amount = tblData.getModel().getValueAt(i,3).toString().replaceAll(",", "");
//                        String dnomType= tblData.getModel().getValueAt(i,0).toString();
//                        insertData("CHANGE",amount,tblData.getModel().getValueAt(i,2).toString(),"CREDIT",
//                        tblData.getModel().getValueAt(i,1).toString(),dnomType);
//                 //   }
//                }
//            }
//            for (int i = 0; i<= 14; i++) {
//                if(tblData.getModel().getValueAt(i,5)!=null &&
//                tblData.getModel().getValueAt(i,6)!=null) {
//                 //   if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
//                        String amount = tblData.getModel().getValueAt(i,6).toString().replaceAll(",", "");
//                        String dnomType= tblData.getModel().getValueAt(i,0).toString();
//                        insertData("CHANGE",amount,tblData.getModel().getValueAt(i,5).toString(),"DEBIT",
//                        tblData.getModel().getValueAt(i,4).toString(),dnomType);
//                  //  }
//                }
//                
//            }
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
      
       private void popUp() {
          String prod_id1= "",prod_type1 ="",prod_id= "",prod_type ="";  
            HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
          if(cboProdType.getSelectedItem()!=null && !cboProdType.getSelectedItem().equals(""))
          {
           prod_type1=cboProdType.getSelectedItem().toString();
           if(prod_type1.equals("Share") || prod_type1.equals("MMBS") || prod_type1.equals("Investments") || prod_type1.equals("Borrowings"))
           {
               if(prod_type1.equals("Share"))
               {
                   if(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected()!=null)
                      prod_id=((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
                   viewMap.put(CommonConstants.MAP_NAME, "getShareNum");
                       
                    whereMap.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                    whereMap.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
                    setSelectedBranchID(TrueTransactMain.selBranch);
       
 viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
       
               }
               if(prod_type1.equals("Investments"))
               {
                   if(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected()!=null)
                    //  prod_id=((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
                       prod_id=cboProdId.getSelectedItem().toString();
                   System.out.println("prod_id INMMMMM======="+prod_id);
                     whereMap.put("INV_DEC",prod_id);
                     viewMap.put(CommonConstants.MAP_NAME, "getSelectInvReportView");
                    viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                   //viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();

       
               }
               if(prod_type1.equals("MMBS"))
               {
                    HashMap map1= new HashMap();
                    HashMap where = new HashMap();
                    HashMap mapTrans = new HashMap();
                    if(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected()!=null)
                      prod_id=((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
                    String sch_name="";
                    if(cboProdId.getSelectedItem()!=null)
                    //   sch_name= cboProdId.getSelectedItem().toString();
                         sch_name= cbmProdId.getKeyForSelected().toString();
                    map1.put("SCHEME_NAMES",sch_name);
                    List list1= ClientUtil.executeQuery("getSelectTransDet", map1);
                    if(list1!=null && list1.size()>0){
                        mapTrans = (HashMap) list1.get(0);
                        if(CommonUtil.convertObjToStr(mapTrans.get("TRANS_FIRST_INSTALLMENT")).equals("Y")){
                            whereMap.put("SCHEME_NAMES",sch_name);
                            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                            viewMap.put(CommonConstants.MAP_NAME, "getSelectChittalReceiptEntryDetailsForReportview");
                        }
                        else
                        {
                            whereMap.put("SCHEME_NAMES",sch_name);
                            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                            viewMap.put(CommonConstants.MAP_NAME, "getSelectChittalReceiptEntryDetailsN");  
                        }
               }
                     new ViewAll(this, viewMap).show();
           }
               if(prod_type1.equals("Borrowings"))
               {
                   ArrayList lst=new ArrayList();
                   lst.add("BORROWING_NO");
                    viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                    lst = null;
                    viewMap.put(CommonConstants.MAP_NAME, "BorrwingDisbursal.getSelectBorrowingDList");
                     viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
               }
           
          }
          
          else
          {
      
       
        if(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected()!=null)
          prod_id=((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
        
         if(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected()!=null)
            prod_type=((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
           
                viewMap.put(CommonConstants.MAP_NAME, "Report.getAccountList"
                        + ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
           
            whereMap.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());


           whereMap.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
            setSelectedBranchID(TrueTransactMain.selBranch);
       

        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this, viewMap).show();
          }
    }
    }

    public static String formatCrore(String str) {
        java.text.DecimalFormat numberFormat = new java.text.DecimalFormat();
        numberFormat.applyPattern("########################0.00");
        
        double currData = Double.parseDouble(str.replaceAll(",",""));
        str = numberFormat.format( currData );
        
        String num = str.substring(0,str.lastIndexOf(".")).replaceAll(",","");
        String dec = str.substring(str.lastIndexOf("."));
        
        String sign = "";
        if (num.substring(0,1).equals("-")) {
            sign = num.substring(0,1);
            num = num.substring(1,num.length());
        }
        
        char[] chrArr = num.toCharArray();
        StringBuffer fmtStrB = new StringBuffer();
        
        for (int i=chrArr.length-1, j=0, k=0; i >= 0; i--) {
            if ((j==3 && k==3) || (j==2 && k==5) || (j==2 && k==7)) {
                fmtStrB.insert(0, ",");
                if (k==7) k = 0;
                j=0;
            }
            j++; k++;
            
            fmtStrB.insert(0, chrArr[i]);
        }
        fmtStrB.append(dec);
        
        str = fmtStrB.toString();
        
        str = sign+str;
        
        if (str.equals(".00")) str = "0";
        
        return str;
    }
   
//    private void setTableModelListener() {
//        tableModelListener = new TableModelListener() {
//            
//            public void tableChanged(TableModelEvent e) {
//                if (e.getType() == TableModelEvent.UPDATE) {
//                    System.out.println("Cell " + e.getFirstRow() + ", "
//                    + e.getColumn() + " changed. The new value: "
//                    + tblData.getModel().getValueAt(e.getFirstRow(),
//                    e.getColumn()));
//                    int row = e.getFirstRow();
//                    int column = e.getColumn();
//                    
//                    if (row!=14) {
//                        if (column == 1 || column == 2) {
//
//                            TableModel model = tblData.getModel();
//
//                            String quantity = model.getValueAt(row, 1).toString();
//
//                            if(model.getValueAt(row, 1)!=null) {
//                                if(row==14) {
//                                    quantity="1";
//                                }
//
//                                double price = CommonUtil.convertObjToDouble(model.getValueAt(row, 2)) .doubleValue();
//                                Double value=null;
//                                if(quantity.equals("0.50")){
//                                     value=new Double(0.50 * price);
//                                }
//                                else{
//                                 value =new Double(Integer.parseInt(quantity) * price);
//                                }
//                                // System.out.println("price====="+price +"quantity=="+quantity);
//                                //    System.out.println("valuevalue=="+value);
//                                model.setValueAt(formatCrore(String.valueOf(value)), row, 3);
//                                //Total print
//                                double totalPay=0;
//                                BigDecimal bm=null;
//
//                                for (int i = 0; i< 14; i++) {
//                                    //  total +=((Double) model.getValueAt(i, 2)).doubleValue();
//                                    if(tblData.getModel().getValueAt(i,3)!=null) {
//                                        // totalPay += ((Double) tblData.getModel().getValueAt(i,2)).doubleValue();
//                                        String val=tblData.getModel().getValueAt(i,3).toString();
//                                        val=val.replaceAll(",","");
//                                        // totalPay += Double.parseDouble(tblData.getModel().getValueAt(i,2).toString());
//                                        totalPay += Double.parseDouble(val);
//                                    }
//                                }
//                                model.setValueAt(formatCrore(String.valueOf(Double.valueOf(totalPay))), 14, 3);//formatCrore(
//
//                            }
//                            //setModified(true);
//                        }
//                        if ( column == 4 || column == 5 ) {
//                            TableModel model = tblData.getModel();
//                            String quantity = model.getValueAt(row, 4).toString();
//                            if(model.getValueAt(row, 4)!=null) {
//                                if(row==15) {
//                                    // System.out.println("ROWW==111=="+row);
//                                    quantity="1";
//                                }
//                                double price = CommonUtil.convertObjToDouble(model.getValueAt(row, 5)).doubleValue();
//                                Double value = new Double(Integer.parseInt(quantity) * price);
//                                model.setValueAt(formatCrore(String.valueOf(value)), row, 6);
//
//                                //Set tiotal
//                                double totalRec=0;
//
//                                for (int i = 0; i< 14; i++) {
//                                    //  total +=((Double) model.getValueAt(i, 2)).doubleValue();
//                                    if(tblData.getModel().getValueAt(i,6)!=null) {
//                                        String val=  tblData.getModel().getValueAt(i,6).toString();
//                                        val=val.replaceAll(",","");
//                                        // totalRec +=   Double.parseDouble(tblData.getModel().getValueAt(i,5).toString());
//                                        totalRec +=   Double.parseDouble(val);
//                                    }
//
//                                }
//                                model.setValueAt(formatCrore(String.valueOf(Double.valueOf(totalRec))), 14, 6);  //formatCrore(
//                              //  setModified(true);
//                            }
//                        }
//                    }
//                    
//                    
//                }
//            }
//        };
//        tblData.getModel().addTableModelListener(tableModelListener);
//    }
    public void fillDataSchedule() throws Exception{
        //System.out.println("getCbmProdType().getKeyForSelected().getKeyForSelected().toString()111>>"+(getCbmProdType().getKeyForSelected().toString()));
       // setCbmSchedule(getCbmProdType().getKeyForSelected().toString());
        //cboScheduleFormat.setModel(getCbmSchedule());
    }
    public void fillData1() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        key.add("Share");
        key.add("MMBS");
        key.add("Investments");
        key.add("Borrowings");
        value.add("Share");
        value.add("MMBS");
        value.add("Investments");
        value.add("Borrowings");
        key.remove("GL");
        value.remove("General Ledger");
        cbmProdType = new ComboBoxModel(key,value);
        cboProdType.setModel(getCbmProdType());
        System.out.println("getCbmProdType().getKeyForSelected().getKeyForSelected().toString()111>>"+(getCbmProdType().getKeyForSelected().toString()));
        setCbmProdId(getCbmProdType().getKeyForSelected().toString());
        cboProdId.setModel(getCbmProdId());
        //setCbmSchedule(getCbmProdType().getKeyForSelected().toString());
       // cboScheduleFormat.setModel(getCbmSchedule());
        //cboProdType1.addItem("");
        // cboProdType1.addItem("Share");
        // cboProdType1.addItem("MMBS");
        // cboProdType1.addItem("Investments");
        // cboProdType1.addItem("Borrowings");
        //root = new TreeModel();
        //   child = new DefaultTreeModel("Colors");
        //   root.add(child);
        btnLoansInstSchedul.setEnabled(false);
        btnFDSubdayBook.setEnabled(false);
    }
    private void getKeyValue(HashMap keyValue)  throws Exception{
        log.info("In getKeyValue()");
        System.out.println("keyValue####"+keyValue);
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    public ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }

    public void setCbmProdType(ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }
    public void setCbmSchedule(String prodType) {
        System.out.println("prodId in settt ====="+prodType);
        try{
            if (CommonUtil.convertObjToStr(prodType).length()>1) {
                if (prodType.equals("GL")) {
                    System.out.println(",mnvhu");
                    key = new ArrayList();
                    value = new ArrayList();
                }else  if (prodType.equals("Share"))
                {
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME,null);
                    final ArrayList lookup_keys = new ArrayList();
                    lookup_keys.add("SHARE_SCHEDULE");
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get("SHARE_SCHEDULE"));
                    cbmSchedule= new ComboBoxModel(key,value);
                    cboScheduleFormat.setModel(getCbmSchedule());
                    System.out.println("prod_type  =====yyyyyyyyyyyyyyy============="+cbmSchedule);
                }
                /*else  if (prodType.equals("MMBS"))
                {
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME,null);
                    final ArrayList lookup_keys = new ArrayList();
                    lookup_keys.add("MMBS_SCHEDULE");
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get("MMBS_SCHEDULE"));
                    cbmSchedule= new ComboBoxModel(key,value);
                    cboScheduleFormat.setModel(getCbmSchedule());
                    System.out.println("prod_type  =====yyyyyyyyyyyyyyy============="+cbmSchedule);
                }*/else  if (prodType.equals("Investments"))
                {
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME,null);
                    final ArrayList lookup_keys = new ArrayList();
                    lookup_keys.add("INVESTMENT_SCHEDULE");
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get("INVESTMENT_SCHEDULE"));
                    cbmSchedule= new ComboBoxModel(key,value);
                    cboScheduleFormat.setModel(getCbmSchedule());
                    System.out.println("prod_type  =====yyyyyyyyyyyyyyy============="+cbmSchedule);
                }else  if (prodType.equals("Borrowings"))
                {
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME,null);
                    final ArrayList lookup_keys = new ArrayList();
                    lookup_keys.add("BORROWING_SCHEDULE");
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get("BORROWING_SCHEDULE"));
                    cbmSchedule= new ComboBoxModel(key,value);
                    cboScheduleFormat.setModel(getCbmSchedule());
                    System.out.println("prod_type  =====yyyyyyyyyyyyyyy============="+cbmSchedule);
                }else
                {
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME,null);
                    final ArrayList lookup_keys = new ArrayList();
                    lookup_keys.add(prodType+"_SCHEDULE");
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get(prodType+"_SCHEDULE"));
                    cbmSchedule= new ComboBoxModel(key,value);
                    cboScheduleFormat.setModel(getCbmSchedule());
                    System.out.println("prod_type  =====yyyyyyyyyyyyyyy============="+lookup_keys);
                }    
            }    
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    //public void setCbmShare(String prodType) {
   //     System.out.println("prodType in setCbmShare ====="+prodType);
    //    try{
   //         if (prodType.equals("Share")){
      //              lookUpHash = new HashMap();
      //              lookUpHash.put(CommonConstants.MAP_NAME,null);
      //              final ArrayList lookup_keys = new ArrayList();
        //            lookup_keys.add("SHARE_TYPE");
        //         keyValue = ClientUtil.populateLookupData(lookUpHash);
       //             getKeyValue((HashMap)keyValue.get("SHARE_TYPE"));
                    //cbmShare= new ComboBoxModel(key,value);
                    //cboShare.setModel(getCbmShare());
        //            System.out.println("prod_type  =====yyyyyyyyyyyyyyy============="+cbmShare);
        //    }
       // }catch(Exception e)
       // {
      //      e.printStackTrace();
      //  }
    //}
    public void setCbmProdId(String prodType) {
        System.out.println("prodType in settt ====="+prodType);
        try
        {
        if (CommonUtil.convertObjToStr(prodType).length()>1) {
            if (prodType.equals("GL")) {
                System.out.println(",mnvhu");
                key = new ArrayList();
                value = new ArrayList();
            } 
            else  if (prodType.equals("Share"))
            {
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,SHARE_TYPE);
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmProdId= new ComboBoxModel(key,value);
        cboProdId.setModel(getCbmProdId());
         System.out.println("prod_type  =====yyyyyyyyyyyyyyy============="+cboProdId);
     }
     else if(prodType.equals("MMBS"))
     {
         HashMap mapData=new HashMap();
         mapData.put("CURR_DATE",currDt);
     /*   lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"getSchemeNames");
        lookUpHash.put(CommonConstants.PARAMFORQUERY,mapData);
       // lookUpHash.put("CURR_DATE",currDt);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmProdId= new ComboBoxModel(key,value);
        cboProdId.setModel(getCbmProdId());*/
         key=new ArrayList();value=new ArrayList();
          HashMap singleAuthorizeMap1 = new HashMap();
                List aList= ClientUtil.executeQuery("getSelectEachSchemeDetailsForReportview", mapData);
                System.out.println("prod_type  =====mapData============="+mapData);
                for(int i=0;i<aList.size();i++) {
                        HashMap map=(HashMap)aList.get(i);
                        System.out.println("prod_type  =====map.get(=========="+map.get("SCHEME_NAME"));
                         System.out.println("prod_type  =====map.get(=========="+map.get("SCHEME_DESC"));
                        if(map.get("SCHEME_NAME")!=null && map.get("SCHEME_DESC")!=null) {
                            if(map.get("SCHEME_NAME")!=null && !map.get("SCHEME_NAME").equals(""))
                                key.add(map.get("SCHEME_NAME").toString());
                            if(map.get("SCHEME_DESC")!=null && !map.get("SCHEME_DESC").equals(""))
                            value.add(map.get("SCHEME_DESC").toString());
                                    
                        }
                        }
                System.out.println("prod_type  =====key============="+key);
                System.out.println("prod_type  =====value============="+value);
                 cbmProdId= new ComboBoxModel(key,value);
                cboProdId.setModel(getCbmProdId());
     }
     else if(prodType.equals("Investments"))
     {
          HashMap mapData=new HashMap();
       //  mapData.put("CURR_DATE",currDt);
        key=new ArrayList();value=new ArrayList();
          HashMap singleAuthorizeMap1 = new HashMap();
                List aList= ClientUtil.executeQuery("getInvNum", mapData);
                System.out.println("prod_type  =====mapData============="+mapData);
                for(int i=0;i<aList.size();i++) {
                        HashMap map=(HashMap)aList.get(i);
                     //   System.out.println("prod_type  =====map.get(=========="+map.get("SCHEME_NAME"));
                     //    System.out.println("prod_type  =====map.get(=========="+map.get("SCHEME_DESC"));
                        if(map.get("IINVESTMENT_PROD_DESC")!=null && map.get("INVESTMENT_PROD_ID")!=null) {
                            if(map.get("INVESTMENT_PROD_ID")!=null && !map.get("INVESTMENT_PROD_ID").equals(""))
                                key.add(map.get("INVESTMENT_PROD_ID").toString());
                            if(map.get("IINVESTMENT_PROD_DESC")!=null && !map.get("IINVESTMENT_PROD_DESC").equals(""))
                            value.add(map.get("IINVESTMENT_PROD_DESC").toString());
                                    
                        }
                        }
                System.out.println("prod_type  =====key============="+key);
                System.out.println("prod_type  =====value============="+value);
                 cbmProdId= new ComboBoxModel(key,value);
                cboProdId.setModel(getCbmProdId());
     }
     else if(prodType.equals("Borrowings"))
     {
   ((ComboBoxModel) cboProdId.getModel()).removeAllElements();
         key=null;value=null;
      //  cbmProdId= new ComboBoxModel(key,value);
        cboProdId.setModel(getCbmProdId());
            }
            else {
                try {
                    System.out.println("kkgjdf");
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
       if(!prodType.equals("Borrowings") )
       {
        cbmProdId = new ComboBoxModel(key,value);
        this.cbmProdId = cbmProdId;
        System.out.println("cbmProdId>>>>"+cbmProdId);  
       }
//        setChanged();
    }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
     ComboBoxModel getCbmProdId(){
        return cbmProdId;
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panCashTrans = new com.see.truetransact.uicomponent.CPanel();
        btnOk = new com.see.truetransact.uicomponent.CButton();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        txtAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        tdtFromDt = new com.see.truetransact.uicomponent.CDateField();
        tdtToDt = new com.see.truetransact.uicomponent.CDateField();
        lblProdType1 = new com.see.truetransact.uicomponent.CLabel();
        lblProdType2 = new com.see.truetransact.uicomponent.CLabel();
        btnLoansInstSchedul = new com.see.truetransact.uicomponent.CButton();
        btnFDSubdayBook = new com.see.truetransact.uicomponent.CButton();
        cboScheduleFormat = new com.see.truetransact.uicomponent.CComboBox();
        lblScheduleFormat = new com.see.truetransact.uicomponent.CLabel();
        cboBranch = new com.see.truetransact.uicomponent.CComboBox();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        lblAccountType = new com.see.truetransact.uicomponent.CLabel();
        cboAccountType = new com.see.truetransact.uicomponent.CComboBox();

        setClosable(true);
        setResizable(true);
        setTitle("Schedule View");
        setMaximumSize(new java.awt.Dimension(500, 400));
        setMinimumSize(new java.awt.Dimension(500, 400));
        setPreferredSize(new java.awt.Dimension(500, 400));
        getContentPane().setLayout(null);

        panCashTrans.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panCashTrans.setMinimumSize(new java.awt.Dimension(500, 500));
        panCashTrans.setPreferredSize(new java.awt.Dimension(500, 500));
        panCashTrans.setLayout(null);

        btnOk.setText("Show");
        btnOk.setMaximumSize(new java.awt.Dimension(80, 28));
        btnOk.setMinimumSize(new java.awt.Dimension(80, 28));
        btnOk.setPreferredSize(new java.awt.Dimension(80, 28));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        panCashTrans.add(btnOk);
        btnOk.setBounds(170, 230, 80, 28);

        lblProdId.setText("Prod ID");
        panCashTrans.add(lblProdId);
        lblProdId.setBounds(45, 110, 50, 20);

        cboProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdActionPerformed(evt);
            }
        });
        panCashTrans.add(cboProdId);
        cboProdId.setBounds(156, 110, 170, 20);

        lblProdType.setText("From Dt");
        panCashTrans.add(lblProdType);
        lblProdType.setBounds(45, 170, 47, 18);

        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        panCashTrans.add(cboProdType);
        cboProdType.setBounds(156, 51, 170, 21);
        panCashTrans.add(lblAccNo);
        lblAccNo.setBounds(48, 102, 0, 0);

        txtAccNo.setAllowAll(true);
        panCashTrans.add(txtAccNo);
        txtAccNo.setBounds(140, 280, 170, 20);

        btnAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccNo.setToolTipText("Account Head");
        btnAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNoActionPerformed(evt);
            }
        });
        panCashTrans.add(btnAccNo);
        btnAccNo.setBounds(310, 270, 21, 21);
        panCashTrans.add(tdtFromDt);
        tdtFromDt.setBounds(152, 170, 108, 21);
        panCashTrans.add(tdtToDt);
        tdtToDt.setBounds(152, 200, 108, 21);

        lblProdType1.setText("Prod Type");
        panCashTrans.add(lblProdType1);
        lblProdType1.setBounds(45, 51, 59, 18);

        lblProdType2.setText("To Dt");
        panCashTrans.add(lblProdType2);
        lblProdType2.setBounds(45, 200, 31, 18);

        btnLoansInstSchedul.setText("Loans Installment Schedule");
        btnLoansInstSchedul.setMaximumSize(new java.awt.Dimension(200, 28));
        btnLoansInstSchedul.setMinimumSize(new java.awt.Dimension(200, 28));
        btnLoansInstSchedul.setPreferredSize(new java.awt.Dimension(200, 28));
        btnLoansInstSchedul.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoansInstSchedulActionPerformed(evt);
            }
        });
        panCashTrans.add(btnLoansInstSchedul);
        btnLoansInstSchedul.setBounds(350, 160, 40, 28);

        btnFDSubdayBook.setText("FD Subday Book");
        btnFDSubdayBook.setMaximumSize(new java.awt.Dimension(200, 28));
        btnFDSubdayBook.setMinimumSize(new java.awt.Dimension(200, 28));
        btnFDSubdayBook.setPreferredSize(new java.awt.Dimension(200, 28));
        btnFDSubdayBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFDSubdayBookActionPerformed(evt);
            }
        });
        panCashTrans.add(btnFDSubdayBook);
        btnFDSubdayBook.setBounds(350, 200, 30, 28);
        panCashTrans.add(cboScheduleFormat);
        cboScheduleFormat.setBounds(156, 140, 170, 21);

        lblScheduleFormat.setText("Schedule Formats");
        panCashTrans.add(lblScheduleFormat);
        lblScheduleFormat.setBounds(45, 140, 110, 18);
        panCashTrans.add(cboBranch);
        cboBranch.setBounds(156, 20, 170, 21);

        cLabel1.setText("Branch Code");
        panCashTrans.add(cLabel1);
        cLabel1.setBounds(45, 20, 90, 18);

        lblAccountType.setText("Account Type");
        panCashTrans.add(lblAccountType);
        lblAccountType.setBounds(45, 80, 90, 20);
        panCashTrans.add(cboAccountType);
        cboAccountType.setBounds(156, 80, 170, 21);

        getContentPane().add(panCashTrans);
        panCashTrans.setBounds(10, 10, 400, 320);

        getAccessibleContext().setAccessibleName("Re");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public void setCbmAccount(String prodType) {
        try
        {
            if (prodType.equals("AB")||prodType.equals("Investments"))
            {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME,null);
                final ArrayList lookup_keys = new ArrayList();
                lookup_keys.add("ACCOUNTS_OTHER_BANK");
                lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get("ACCOUNTS_OTHER_BANK"));
                cbmAccountType= new ComboBoxModel(key,value);
                cboAccountType.setModel(getCbmAccountType());
            }else if(prodType.equals("Borrowings")){
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME,null);
                final ArrayList lookup_keys = new ArrayList();
                lookup_keys.add("BORROWING_AGENCY");
                lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get("BORROWING_AGENCY"));
                cbmAccountType= new ComboBoxModel(key,value);
                cboAccountType.setModel(getCbmAccountType());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
         if(cboProdType.getSelectedIndex()==0 ){
            displayAlert("Prod Type Should not be empty!");  
            return;
        }     
        if(cboProdId.getSelectedIndex()==0){
            displayAlert("Prod Id Should not be empty!");  
            return;
        }  
        if(cboScheduleFormat.getSelectedIndex()==0){
            displayAlert("Schedule Format Should not be empty!");  
            return;
        }
        if(cboScheduleFormat.getSelectedIndex()>0 && cboProdId.getSelectedIndex()>0 && cboProdType.getSelectedIndex()>0){
            if(tdtFromDt.getDateValue().isEmpty())
            {
                displayAlert("Enter From Date!!!");
                return;
            }
            if(tdtToDt.getDateValue().isEmpty())
            {
                displayAlert("Enter To Date!!!");
                return;
            }
        }
        if(cboScheduleFormat.getSelectedIndex()>0 && cboProdId.getSelectedIndex()>0 && cboProdType.getSelectedIndex()>0
           && !tdtFromDt.getDateValue().isEmpty() && !tdtToDt.getDateValue().isEmpty()){    
            System.out.println("cboProdType####"+cboProdType.getSelectedItem().toString());
            System.out.println("cboProdId####"+cboProdId.getSelectedItem().toString());
            System.out.println("cboScheduleFormat####"+getCbmSchedule().getKeyForSelected().toString());
            System.out.println("getCbmbranch()####"+getCbmbranch().getKeyForSelected().toString());
            String prodType = getCbmProdType().getKeyForSelected().toString();
            String prodId   = getCbmProdId().getKeyForSelected().toString();
            String ProdDesc = cboProdId.getSelectedItem().toString();
            String scheduleFormat = getCbmSchedule().getKeyForSelected().toString();
            String branchCode = getCbmbranch().getKeyForSelected().toString();
            if(prodType.equals("Share"))
            {
                TTIntegration ttIntgration = new TTIntegration();
                HashMap paramMap = new HashMap();
                paramMap.put("BranchId", branchCode);
                paramMap.put("FromDate",DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
                paramMap.put("ToDate", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                paramMap.put("As_On", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                paramMap.put("SharesType", prodId);
                ttIntgration.setParam(paramMap);
                ttIntgration.integrationForPrint(scheduleFormat, true);
            } 
            if(prodType.equals("TD")|| prodType.equals("OA")||prodType.equals("TL")||prodType.equals("AD")||prodType.equals("MMBS"))
            {
                TTIntegration ttIntgration = new TTIntegration();
                HashMap paramMap = new HashMap();
                paramMap.put("BranchId", branchCode);
                paramMap.put("FromDate",DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
                paramMap.put("ToDate", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                paramMap.put("As_On", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                paramMap.put("ProdDesc", ProdDesc);
                ttIntgration.setParam(paramMap);
                ttIntgration.integrationForPrint(scheduleFormat, true);
            }
            if(prodType.equals("AB"))
            {   
                String AccountType = getCbmAccountType().getKeyForSelected().toString();
                TTIntegration ttIntgration = new TTIntegration();
                HashMap paramMap = new HashMap();
                paramMap.put("BranchId", branchCode);
                paramMap.put("FromDate",DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
                paramMap.put("ToDate", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                paramMap.put("As_On", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                paramMap.put("AccountType", AccountType);
                ttIntgration.setParam(paramMap);
                ttIntgration.integrationForPrint(scheduleFormat, true);
            } 
            if(prodType.equals("Investments"))
            {   
                String InvestType = getCbmAccountType().getKeyForSelected().toString();
                TTIntegration ttIntgration = new TTIntegration();
                HashMap paramMap = new HashMap();
                paramMap.put("BranchId", branchCode);
                paramMap.put("FromDate",DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
                paramMap.put("ToDate", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                paramMap.put("As_On", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                paramMap.put("InvestType", InvestType);
                ttIntgration.setParam(paramMap);
                ttIntgration.integrationForPrint(scheduleFormat, true);
            }
            if(prodType.equals("Borrowings"))
            {   
                String BankName = getCbmAccountType().getKeyForSelected().toString();
                TTIntegration ttIntgration = new TTIntegration();
                HashMap paramMap = new HashMap();
                paramMap.put("BranchId", branchCode);
                paramMap.put("FromDate",DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
                paramMap.put("ToDate", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                paramMap.put("As_On", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                paramMap.put("BankName", BankName);
                ttIntgration.setParam(paramMap);
                ttIntgration.integrationForPrint(scheduleFormat, true);
            }
            }else{
                displayAlert("Parameters should not be null!!!");
                return;
        }   
         
    }//GEN-LAST:event_btnOkActionPerformed
    
//    private boolean isDenominationTallied() {
//        boolean tallied = true;
//        try {
//            double transAmt = CommonUtil.convertObjToDouble(lblAmt.replaceAll(",", "")).doubleValue();
//            double receiptDenomination = 0;
//            double paymentDenomination = 0;
//            for (int i = 0; i<=14; i++) {
//                if(tblData.getModel().getValueAt(i,2)!=null &&
//                tblData.getModel().getValueAt(i,3)!=null ) {
//                  //  if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
//                        //totalPay += ((Double) tblData.getModel().getValueAt(i,2)).doubleValue();
//                        //System.out.println("33-=="+tblData.getModel().getValueAt(i,1));
//                        //System.out.println("22==="+tblData.getModel().getValueAt(i,2));
//                        String amount = tblData.getModel().getValueAt(i,3).toString().replaceAll(",", "");
//                        receiptDenomination+=CommonUtil.convertObjToDouble(amount).doubleValue();
//                  //  }
//                }
//            }
//            for (int i = 0; i<= 14; i++) {
//                if(tblData.getModel().getValueAt(i,5)!=null &&
//                tblData.getModel().getValueAt(i,6)!=null) {
//                  //  if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
//                        String amount = tblData.getModel().getValueAt(i,6).toString().replaceAll(",", "");
//                        paymentDenomination+=CommonUtil.convertObjToDouble(amount).doubleValue();
//                  //  }
//                }
//                
//            }  
//            double denominationAmount = 0.0;
//            if(receiptDenomination>0.0 && lblTransTy.equals("CREDIT")){
//                transMode="Receipts";
//            }
//            if(paymentDenomination>0.0 && lblTransTy.equals("DEBIT")){
//                transMode="Payments";
//            }
//            if(transMode.equals("Receipts")){
//                denominationAmount = receiptDenomination-paymentDenomination;
//            }else{
//                denominationAmount = paymentDenomination-receiptDenomination;
//            }
////             double denominationAmount = receiptDenomination-paymentDenomination;
//             if (denominationAmount!=transAmt) {
//                 ClientUtil.showAlertWindow("Denomination Amount : "+denominationAmount+
//                 "\nTransaction Amount :"+transAmt);
//                 tallied = false;
//             }
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
//        return tallied;
//    }
    
    public void fillBranch() {
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        HashMap mapShare = new HashMap();
        List keyValue = null;
        keyValue = ClientUtil.executeQuery("getbranchesForBalanceSheet", mapShare);
        key.add("");
        value.add("");
        if (keyValue != null && keyValue.size() > 0) {
            for (int i = 0; i < keyValue.size(); i++) {
                mapShare = (HashMap) keyValue.get(i);
                key.add(mapShare.get("BRANCH_CODE"));
                value.add(mapShare.get("BRANCH_NAME"));
            }
        }
        cbmbranch = new ComboBoxModel(key, value);
        key = null;
        value = null;
        keyValue.clear();
        keyValue = null;
        mapShare.clear();
        mapShare = null;
        cboBranch.setModel(getCbmbranch());
       
    }      
    public void clearTransDetails() {
 
    }
  
    private void enableDisableButtons(boolean val) {
        btnOk.setEnabled(val);
       // btnReject.setEnabled(val);
    }
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    public boolean checkNumber(String value) {
        //String amtRentIn=amountRentText.getText();
        boolean incorrect = true;
        try {
            Double.parseDouble(value);
            return true;
        }
        catch(NumberFormatException nfe) {
            return false;
        }
        // return
    }
    public String getDtPrintValue(String strDate) {
        try {
            //create SimpleDateFormat object with source string date format
            SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //"yyyy-MM-dd HH:mm:ss "
            //parse the string into Date object
            Date date = sdfSource.parse(strDate);
            //create SimpleDateFormat object with desired date format
            SimpleDateFormat sdfDestination = new SimpleDateFormat("MM/dd/yyyy");
            //parse the date into another format
            strDate = sdfDestination.format(date);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
   //  final HashMap viewMap = new HashMap();
  ///      System.out.println("getCbmProdType().getKeyForSelected().toString()???>>>>"+getCbmProdType().getKeyForSelected().toString());
  //       System.out.println("getCbmProdid().getKeyForSelected().toString()???>>>>"+getCbmProdId().getKeyForSelected().toString());
  //   viewMap.put("PROD_TYPE", getCbmProdType().getKeyForSelected().toString());
  //   viewMap.put("PROD_ID", getCbmProdId().getKeyForSelected().toString());
 //    viewMap.put(CommonConstants.MAP_WHERE, viewMap);
 //       viewMap.put(CommonConstants.MAP_NAME, "getAllProductAccNoForGlInCash");
   //     new ViewAll(this, viewMap).show();    
        txtAccNo.setText("");
        tdtFromDt.setDateValue("");
        tdtToDt.setDateValue("");
        popUp();
    }//GEN-LAST:event_btnAccNoActionPerformed

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
         System.out.println("getCbmProdType().getKeyForSelected().getKeyForSelected().toString()111>>"+(getCbmProdType().getKeyForSelected().toString()));
        setCbmProdId(getCbmProdType().getKeyForSelected().toString());
        txtAccNo.setText("");
        tdtFromDt.setDateValue("");
        tdtToDt.setDateValue("");
        cboProdId.setModel(getCbmProdId());
        setCbmSchedule(getCbmProdType().getKeyForSelected().toString());
        cboScheduleFormat.setModel(getCbmSchedule());
        if(getCbmProdType().getKeyForSelected().toString().equals("AD") || getCbmProdType().getKeyForSelected().toString().equals("TL")){
         btnLoansInstSchedul.setEnabled(true);   
        }else{
            btnLoansInstSchedul.setEnabled(false);
        }
        if(getCbmProdType().getKeyForSelected().toString().equals("TD")){
        btnFDSubdayBook.setEnabled(true);   
        }else{
            btnFDSubdayBook.setEnabled(false);
        }
        if(getCbmProdType().getKeyForSelected().toString().equals("AB")||getCbmProdType().getKeyForSelected().toString().equals("Investments")
           ||getCbmProdType().getKeyForSelected().toString().equals("Borrowings")){
            cboAccountType.setVisible(true);
            lblAccountType.setVisible(true);
            setCbmAccount(getCbmProdType().getKeyForSelected().toString());
            cboAccountType.setModel(getCbmAccountType());
        }else{
            cboAccountType.setVisible(false);
            lblAccountType.setVisible(false); 
        }
        
        
    }//GEN-LAST:event_cboProdTypeActionPerformed

private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_cboProdIdActionPerformed

    private void btnLoansInstSchedulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoansInstSchedulActionPerformed
        // TODO add your handling code here:
        if(getCbmProdType().getKeyForSelected().toString().equals("AD") || getCbmProdType().getKeyForSelected().toString().equals("TL"))
               {
                   String accNo=txtAccNo.getText();
                   if(accNo!=null && !accNo.equals(""))
                   {
                    TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    paramMap.put("AccountNo", accNo);
//                    paramMap.put("FromDate",DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
//                    paramMap.put("ToDate", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                    ttIntgration.setParam(paramMap);
                     ttIntgration.integrationForPrint("TLInstallmentSchedule", true);
                   }
               }
    }//GEN-LAST:event_btnLoansInstSchedulActionPerformed

private void btnFDSubdayBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFDSubdayBookActionPerformed
if(getCbmProdType().getKeyForSelected().toString().equals("TD"))
               {
                   String accNo=txtAccNo.getText();
                   if(accNo!=null && !accNo.equals(""))
                   {
                    TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                     paramMap.put("ProdDesc", cboProdId.getSelectedItem().toString());
                    paramMap.put("FromDate",DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
                   paramMap.put("ToDate", DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
                    ttIntgration.setParam(paramMap);
                     ttIntgration.integrationForPrint("DepositSubDay", true);
                   }
               }
}//GEN-LAST:event_btnFDSubdayBookActionPerformed
 public void setCbmProdId1(String prod_type)  
 {
     try
     {
         System.out.println("prod_type  =================="+prod_type);
     HashMap singleAuthorizeMap = new HashMap();
     if(prod_type.equals("Share"))
     {
       // List aList= ClientUtil.executeQuery("getShareType", singleAuthorizeMap);
       //         for(int i=0;i<aList.size();i++) {
       //                 HashMap map=(HashMap)aList.get(i);
         //               if(map.get("DENOMINATION_ALLOWED")!=null) {
       //                     isDen=map.get("DENOMINATION_ALLOWED").toString();
         //                           
         //               }
          //              }
         lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,SHARE_TYPE);
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmProdId= new ComboBoxModel(key,value);
        cboProdId.setModel(getCbmProdId());
         System.out.println("prod_type  =====yyyyyyyyyyyyyyy============="+cboProdId);
     }
     else if(prod_type.equals("MMBS"))
     {
         HashMap mapData=new HashMap();
         mapData.put("CURR_DATE",currDt);
     /*   lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"getSchemeNames");
        lookUpHash.put(CommonConstants.PARAMFORQUERY,mapData);
       // lookUpHash.put("CURR_DATE",currDt);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmProdId= new ComboBoxModel(key,value);
        cboProdId.setModel(getCbmProdId());*/
         key=new ArrayList();value=new ArrayList();
          HashMap singleAuthorizeMap1 = new HashMap();
                List aList= ClientUtil.executeQuery("getSchemeNames", mapData);
                System.out.println("prod_type  =====mapData============="+mapData);
                for(int i=0;i<aList.size();i++) {
                        HashMap map=(HashMap)aList.get(i);
                        System.out.println("prod_type  =====map.get(=========="+map.get("SCHEME_NAME"));
                         System.out.println("prod_type  =====map.get(=========="+map.get("SCHEME_DESC"));
                        if(map.get("SCHEME_NAME")!=null && map.get("SCHEME_DESC")!=null) {
                            if(map.get("SCHEME_NAME")!=null && !map.get("SCHEME_NAME").equals(""))
                                key.add(map.get("SCHEME_NAME").toString());
                            if(map.get("SCHEME_DESC")!=null && !map.get("SCHEME_DESC").equals(""))
                            value.add(map.get("SCHEME_DESC").toString());
                                    
                        }
                        }
                System.out.println("prod_type  =====key============="+key);
                System.out.println("prod_type  =====value============="+value);
                 cbmProdId= new ComboBoxModel(key,value);
                cboProdId.setModel(getCbmProdId());
     }
     else if(prod_type.equals("Investments"))
     {
          HashMap mapData=new HashMap();
       //  mapData.put("CURR_DATE",currDt);
        key=new ArrayList();value=new ArrayList();
          HashMap singleAuthorizeMap1 = new HashMap();
                List aList= ClientUtil.executeQuery("getInvNum", mapData);
                System.out.println("prod_type  =====mapData============="+mapData);
                for(int i=0;i<aList.size();i++) {
                        HashMap map=(HashMap)aList.get(i);
                     //   System.out.println("prod_type  =====map.get(=========="+map.get("SCHEME_NAME"));
                     //    System.out.println("prod_type  =====map.get(=========="+map.get("SCHEME_DESC"));
                        if(map.get("IINVESTMENT_PROD_DESC")!=null && map.get("INVESTMENT_PROD_ID")!=null) {
                            if(map.get("INVESTMENT_PROD_ID")!=null && !map.get("INVESTMENT_PROD_ID").equals(""))
                                key.add(map.get("INVESTMENT_PROD_ID").toString());
                            if(map.get("IINVESTMENT_PROD_DESC")!=null && !map.get("IINVESTMENT_PROD_DESC").equals(""))
                            value.add(map.get("IINVESTMENT_PROD_DESC").toString());
            
                        }
                        }
                System.out.println("prod_type  =====key============="+key);
                System.out.println("prod_type  =====value============="+value);
            cbmProdId= new ComboBoxModel(key,value);
            cboProdId.setModel(getCbmProdId());
     }
     else if(prod_type.equals("Borrowings"))
     {
   ((ComboBoxModel) cboProdId.getModel()).removeAllElements();
         key=null;value=null;
      //  cbmProdId= new ComboBoxModel(key,value);
        cboProdId.setModel(getCbmProdId());
     }
     else
     {
           ((ComboBoxModel) cboProdId.getModel()).removeAllElements();
          key=null;value=null;
      //  cbmProdId= new ComboBoxModel(key,value);
        cboProdId.setModel(getCbmProdId());
     }
     }catch(Exception e)
     {
         e.printStackTrace();
     }
 }
    /**
     * @param args the command line arguments
     */
  
 
    private boolean chkExistance(DefaultMutableTreeNode parent,String chkStr) {
        final Enumeration objEnumeration = parent.children();
        while (objEnumeration.hasMoreElements()) {
            if (objEnumeration.nextElement().toString().equals(chkStr)) {
                return true;
            }
        }
        return false;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnFDSubdayBook;
    private com.see.truetransact.uicomponent.CButton btnLoansInstSchedul;
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CComboBox cboAccountType;
    private com.see.truetransact.uicomponent.CComboBox cboBranch;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboScheduleFormat;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblAccountType;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblProdType1;
    private com.see.truetransact.uicomponent.CLabel lblProdType2;
    private com.see.truetransact.uicomponent.CLabel lblScheduleFormat;
    private com.see.truetransact.uicomponent.CPanel panCashTrans;
    private com.see.truetransact.uicomponent.CDateField tdtFromDt;
    private com.see.truetransact.uicomponent.CDateField tdtToDt;
    private com.see.truetransact.uicomponent.CTextField txtAccNo;
    // End of variables declaration//GEN-END:variables
    
}

