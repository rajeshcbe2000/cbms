/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GLAccountNumberListUI.java
 *
 * Created on September 28, 2011, 3:03 PM
 */
package com.see.truetransact.ui.transaction.cash;

import com.see.truetransact.clientutil.ClientUtil;
import java.util.HashMap;
import java.util.List;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.payroll.Arrear.ArrearProcessingUI;
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
import javax.swing.JInternalFrame;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.transaction.transfer.TransferUI;
import com.see.truetransact.ui.transaction.multipleCash.MultipleCashTransactionUI;
import com.see.truetransact.uivalidation.NumericValidation;
import java.io.IOException;
/**
 *
 * @author  user
 */
public class GLAccountNumberListUI extends CInternalFrame    {
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
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private final static Logger log = Logger.getLogger(GLAccountNumberListUI.class);
    private CashTransactionUI frm;
    private TransferUI frm1;
    private MultipleCashTransactionUI frm2;
    private ArrearProcessingUI frm3;
    private final String SHARE_TYPE = "getShareType";
    public int flag2=0;
    // public int flag3=0;
    
    public GLAccountNumberListUI(CashTransactionUI frm) {
        flag2=1;
        this.frm=frm;
      //  trans_pid=TransId;
        lblAmt=amount;
        //lblTransTy=TransTy;
        currDt = ClientUtil.getCurrentDate();
        initComponents();
         setupScreen();
//        initTableData();
//      this.setBounds(0,0, 500,330);
//      setSize(570, 280);
//        this.setVisible(true);
        try {
            fillData1();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(GLAccountNumberListUI.class.getName()).log(Level.SEVERE, null, ex);
        }
       // this.setTitle("Change");
//txtAccNo.setValidation(new NumericValidation());
       txtAccNo.setAllowAll(true);
//        getAcHdTree("PENDING");
    //    cboStatus.setSelectedItem("Pending");
//        tblData.getModel().addTableModelListener(tableModelListener);
     //   btnReject.setVisible(false);
        
    }
    
     public GLAccountNumberListUI(TransferUI frm) {
         flag2=2;
        this.frm1=frm;
      //  trans_pid=TransId;
        lblAmt=amount;
        //lblTransTy=TransTy;
        currDt = ClientUtil.getCurrentDate();
        initComponents();
         setupScreen();
//        initTableData();
//      this.setBounds(0,0, 500,330);
//      setSize(570, 280);
//        this.setVisible(true);
        try {
            fillData1();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(GLAccountNumberListUI.class.getName()).log(Level.SEVERE, null, ex);
        }
       // this.setTitle("Change");
       txtAccNo.setAllowAll(true);
//        getAcHdTree("PENDING");
    //    cboStatus.setSelectedItem("Pending");
//        tblData.getModel().addTableModelListener(tableModelListener);
     //   btnReject.setVisible(false);
        
    }
     
     public GLAccountNumberListUI(MultipleCashTransactionUI frm) {
        flag2=3;
        this.frm2=frm;
      //  trans_pid=TransId;
        lblAmt=amount;
        //lblTransTy=TransTy;
        currDt = ClientUtil.getCurrentDate();
        initComponents();
         setupScreen();
//        initTableData();
//      this.setBounds(0,0, 500,330);
//      setSize(570, 280);
//        this.setVisible(true);
        try {
            fillData1();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(GLAccountNumberListUI.class.getName()).log(Level.SEVERE, null, ex);
        }
       // this.setTitle("Change");
       txtAccNo.setValidation(new NumericValidation());
//        getAcHdTree("PENDING");
    //    cboStatus.setSelectedItem("Pending");
//        tblData.getModel().addTableModelListener(tableModelListener);
     //   btnReject.setVisible(false);
        
    }
     
     public GLAccountNumberListUI(ArrearProcessingUI frm) {
        flag2=4;
        this.frm3=frm;
      //  trans_pid=TransId;
        lblAmt=amount;
        //lblTransTy=TransTy;
        currDt = ClientUtil.getCurrentDate();
        initComponents();
         setupScreen();
//        initTableData();
//      this.setBounds(0,0, 500,330);
//      setSize(570, 280);
//        this.setVisible(true);
        try {
            fillData1();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(GLAccountNumberListUI.class.getName()).log(Level.SEVERE, null, ex);
        }
       txtAccNo.setValidation(new NumericValidation());        
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
        
        if(cboProdType.getSelectedItem().equals("Share") || cboProdType.getSelectedItem().equals("MMBS") || cboProdType.getSelectedItem().equals("Investments") || cboProdType.getSelectedItem().equals("Borrowings"))
          {
          String prod_type1=cboProdType.getSelectedItem().toString();
           if(prod_type1.equals("Share") || prod_type1.equals("MMBS") || prod_type1.equals("Investments") || prod_type1.equals("Borrowings"))
           {
               if(prod_type1.equals("Share"))
               {
                   txtAccNo.setText(hash.get("SHARE ACCOUNT NO").toString());
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
          }
        else{
            System.out.println("knldsnfjk");
            if(cboProdType.getSelectedItem()!=null && ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().equals("GL")){
             String acHdId = hash.get("A/C HEAD").toString();
             txtAccNo.setText(acHdId);
            }else{
//            String bankType = CommonConstants.BANK_TYPE;
//            System.out.println("bankType" + bankType);
//            String customerAllow = "";
//            String hoAc = "";
//            cboProdId.setSelectedItem("");
//            this.txtAccNo.setText("");
            String acHdId = hash.get("ACT_NUM").toString();
            txtAccNo.setText(acHdId);
            }
        }
          System.out.println("jafjfjj");
//          System.out.println("((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString() gl1111>>>"+((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
        String prodId3="";
        String prod_type1=cboProdType.getSelectedItem().toString();
        if(prod_type1.equals("Share") || prod_type1.equals("MMBS") || prod_type1.equals("Investments") || prod_type1.equals("Borrowings")){
          prodId3=((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();  
        }else if(!((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().equals("GL")){
          prodId3=((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
        }
        //  System.out.println("((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString() gl>>"+((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString());
       //   System.out.println("((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString() gl>>>"+((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
           // CashTransactionUI cashUI = new CashTransactionUI();
        String prodTypeGl = "";
        if(prod_type1.equals("Share")){
         prodTypeGl =  "SH";  
        }else if(prod_type1.equals("MMBS")){
         prodTypeGl = "MDS";   
        }else if(!prod_type1.equals("Investments") && !prod_type1.equals("Borrowings")){
         prodTypeGl =  ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();  
        }
        if(flag2==1)
            frm.fillAccNo(txtAccNo.getText().toString(),prodId3,prodTypeGl);
        if(flag2==2)
            frm1.fillAccNo(txtAccNo.getText().toString(),prodId3,prodTypeGl);
         if(flag2==3)
            frm2.fillAccNo(txtAccNo.getText().toString(),prodId3,prodTypeGl);
         if(flag2==4){
             ArrearProcessingUI.lblAcHdDesc.setText(CommonUtil.convertObjToStr(cboProdType.getSelectedItem()));
             ArrearProcessingUI.lblAccount.setText(txtAccNo.getText());
             ArrearProcessingUI.debitProductKey = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
             if(!((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().equals("GL")){
                ArrearProcessingUI.debitProductIdKey = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());  
             }
         }
        
      }

           private void popUp() {
          String prod_id1= "",prod_type1 ="",prod_id= "",prod_type ="";  
            HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
          if(cboProdType.getSelectedItem().equals("Share") || cboProdType.getSelectedItem().equals("MMBS") || cboProdType.getSelectedItem().equals("Investments") || cboProdType.getSelectedItem().equals("Borrowings"))
          {
           prod_type1=cboProdType.getSelectedItem().toString();
           if(prod_type1.equals("Share") || prod_type1.equals("MMBS") || prod_type1.equals("Investments") || prod_type1.equals("Borrowings"))
           {
               if(prod_type1.equals("Share"))
               {
                   if(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected()!=null)
                      prod_id=((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
                   viewMap.put(CommonConstants.MAP_NAME, "getShareNum1");
                       
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
                       sch_name= cbmProdId.getKeyForSelected().toString();
                    System.out.println("sch_name111>>"+sch_name);
                    map1.put("SCHEME_NAME",sch_name);
                    List list1= ClientUtil.executeQuery("getSelectTransDet", map1);
                    if(list1!=null && list1.size()>0){
                        mapTrans = (HashMap) list1.get(0);
                        if(CommonUtil.convertObjToStr(mapTrans.get("TRANS_FIRST_INSTALLMENT")).equals("Y")){
                            whereMap.put("SCHEME_NAMES",sch_name);
                            System.out.println("sch_name222>>>>"+sch_name);    
                            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                            viewMap.put(CommonConstants.MAP_NAME, "getSelectChittalReceiptEntryDetails");
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
          }
          else
          {
      
       
        if(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected()!=null)
          prod_id=((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
        
         if(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected()!=null)
            prod_type=((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
           
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                        + ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
           
            whereMap.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());


           whereMap.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
            setSelectedBranchID(TrueTransactMain.selBranch);
       

        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this, viewMap).show();
          }
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
    public void insertData(String transId, String amount, String noOfNotes, String trans_type, String deno_type,String dnomType) {
        String currency="INR";
        HashMap singleAuthorizeMap = new HashMap();
        singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        singleAuthorizeMap.put("TRANS_DT", currDt);
        singleAuthorizeMap.put(CommonConstants.TRANS_ID,transId );//trans id
        singleAuthorizeMap.put("DENOMINATION_VALUE", amount); //amount
        singleAuthorizeMap.put("DENOMINATION_COUNT", noOfNotes);//no of notes
        singleAuthorizeMap.put("CURRENCY", currency);//Currency
        singleAuthorizeMap.put("TRANS_TYPE", trans_type);//Trans type
        singleAuthorizeMap.put("STATUS", "CREATED");//status
        singleAuthorizeMap.put("DENOMINATION_TYPE", deno_type);//Denaomination type
        singleAuthorizeMap.put(CommonConstants.BRANCH_ID,  ProxyParameters.BRANCH_ID);
        singleAuthorizeMap.put("DENOM_TYPE",  dnomType);
        ClientUtil.execute("insertDenominationDetails", singleAuthorizeMap);
        singleAuthorizeMap.clear();
        singleAuthorizeMap=null;
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
    
    public void fillData1() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        cbmProdType = new ComboBoxModel(key,value);
        cboProdType.setModel(getCbmProdType());
      //  System.out.println("getCbmProdType().getKeyForSelected().getKeyForSelected().toString()111>>"+(getCbmProdType().getKeyForSelected().toString()));
        setCbmProdId(getCbmProdType().getKeyForSelected().toString());
      //  cboProdType.addItem("");
        cboProdType.addItem("Share");
        cboProdType.addItem("MMBS");
        cboProdType.addItem("Investments");
        cboProdType.addItem("Borrowings");
        cboProdId.setModel(getCbmProdId());
//        cboProdType1.addItem("");
//        cboProdType1.addItem("Share");
//        cboProdType1.addItem("MMBS");
//        cboProdType1.addItem("Investments");
//        cboProdType1.addItem("Borrowings");
        //root = new TreeModel();
        //   child = new DefaultTreeModel("Colors");
        //   root.add(child);
    }
    private void getKeyValue(HashMap keyValue)  throws Exception{
        log.info("In getKeyValue()");
        
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    public ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }

    public void setCbmProdType(ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }
    public void setCbmProdId(String prodType) {
        if (CommonUtil.convertObjToStr(prodType).length()>1) {
            if (prodType.equals("GL")) {
                System.out.println(",mnvhu");
                key = new ArrayList();
                value = new ArrayList();
            } else {
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
        cbmProdId = new ComboBoxModel(key,value);
        this.cbmProdId = cbmProdId;
     if(cbmProdId.getSelectedItem()!=null &&  !cbmProdId.getSelectedItem().equals(""))
        System.out.println("cbmProdId>>>>"+cbmProdId);  
//        setChanged();
    }
     ComboBoxModel getCbmProdId(){
        return cbmProdId;
    }
//    public void initTableData() {
//        String data[][] ={{}};
//        String col[] = {"Account No","Name","Deposit Date","Amount","Maturity Date"};
//        DefaultTableModel dataModel = new DefaultTableModel();
//        // dataModel.setDataVector(dataVector
//        model = new DefaultTableModel(data,col);
//        //  tblData.getCellEditor().stopCellEditing();
//        tblData.setModel(new javax.swing.table.DefaultTableModel(
//            new Object [][] {
////                {"1000", null, null, "1000", null, null},
////                {"500", null, null, "500", null, null},
////                {"100", null, null, "100", null, null},
////                {"50", null, null, "50", null, null},
////                {"20", null, null, "20", null, null},
////                {"10", null, null, "10", null, null},
////                {"5", null, null, "5", null, null},
////                {"2", null, null, "2", null, null},
////                {"1", null, null, "1", null, null},
////                {"Others", null, null, "Others", null, null},
////                {"Total", null, null, "Total", null, null}
//                
//                {"NOTE","1000", null, null, "1000", null, null},
//                {"NOTE","500", null, null, "500", null, null},
//                {"NOTE","100", null, null, "100", null, null},
//                {"NOTE","50", null, null, "50", null, null},
//                {"NOTE","20", null, null, "20", null, null},
//                {"NOTE","10", null, null, "10", null, null},
//                {"NOTE","5", null, null, "5", null, null},
//                {"NOTE","2", null, null, "2", null, null},
//                {"NOTE","1", null, null, "1", null, null},
//                {"COIN","10", null, null, "10", null, null},
//                {"COIN","5", null, null, "5", null, null},
//                {"COIN","2", null, null, "2", null, null},
//                {"COIN","1", null, null, "1", null, null},
//                {"COIN","0.50",null, null, "1", null, null},
////                {null,"Others", null, null, "Others", null, null},
//                {null,"Total", null, null, "Total", null, null}
//            },
//            new String [] {
////                "Receipts", "No", "Amount", "Payments", "No", "Amount"
//                "Type","Receipts", "No", "Amount", "Payments", "No", "Amount"
//            }
//        ) {
//            Class[] types = new Class [] {
//               java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
//            };
//            boolean[] canEdit = new boolean [] {
//                false,false, true, false, false, true, false
//            };
//
//            public Class getColumnClass(int columnIndex) {
//                return types [columnIndex];
//            }
//
//            public boolean isCellEditable(int rowIndex, int columnIndex) {
//                if (rowIndex==14) {
//                    return false;
//                }
//                return canEdit [columnIndex];
//            }
//        });
//        tblData.setCellSelectionEnabled(true);
////        tblData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
////            public void propertyChange(java.beans.PropertyChangeEvent evt) {
////                tblDataPropertyChange(evt);
////            }
////        });
//        setTableModelListener();
//    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panCashTrans = new com.see.truetransact.uicomponent.CPanel();
        btnOk = new com.see.truetransact.uicomponent.CButton();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        txtAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(500, 350));
        setMinimumSize(new java.awt.Dimension(500, 350));
        setPreferredSize(new java.awt.Dimension(500, 350));
        getContentPane().setLayout(null);

        panCashTrans.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panCashTrans.setMinimumSize(new java.awt.Dimension(500, 500));
        panCashTrans.setPreferredSize(new java.awt.Dimension(500, 500));
        panCashTrans.setLayout(new java.awt.GridBagLayout());

        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(80, 28));
        btnOk.setMinimumSize(new java.awt.Dimension(80, 28));
        btnOk.setPreferredSize(new java.awt.Dimension(80, 28));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCashTrans.add(btnOk, gridBagConstraints);

        lblProdId.setText("Prod ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCashTrans.add(lblProdId, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCashTrans.add(cboProdId, gridBagConstraints);

        lblProdType.setText("Prod Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCashTrans.add(lblProdType, gridBagConstraints);

        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCashTrans.add(cboProdType, gridBagConstraints);

        lblAccNo.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panCashTrans.add(lblAccNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 80;
        panCashTrans.add(txtAccNo, gridBagConstraints);

        btnAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccNo.setToolTipText("Account Head");
        btnAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCashTrans.add(btnAccNo, gridBagConstraints);

        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(80, 28));
        btnClose.setMinimumSize(new java.awt.Dimension(80, 28));
        btnClose.setPreferredSize(new java.awt.Dimension(80, 28));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panCashTrans.add(btnClose, gridBagConstraints);

        getContentPane().add(panCashTrans);
        panCashTrans.setBounds(10, 10, 400, 260);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void isTransactionPendingForAuth() throws Exception {
        String accNo = txtAccNo.getText();
        HashMap transMap = new HashMap();
        transMap.put("LINK_BATCH_ID", accNo);
        transMap.put("TRANS_DT", ClientUtil.getCurrentDate());
        transMap.put("BRANCH_ID", (((ComboBoxModel) TrueTransactMain.cboBranchList.getModel()).getKeyForSelected()));
        List pendingList = ClientUtil.executeQuery("getPendingTransactionTL", transMap);
        if (pendingList != null && pendingList.size() > 0) {
            HashMap hashTrans = (HashMap) pendingList.get(0);
            if (hashTrans.containsKey("LINK_BATCH_ID") && hashTrans.get("LINK_BATCH_ID") != null) {
                String trans_actnum = CommonUtil.convertObjToStr(hashTrans.get("LINK_BATCH_ID"));
                if (accNo!=null && trans_actnum!=null && trans_actnum.equals(accNo)) {
                    ClientUtil.showMessageWindow(" There is Pending Transaction Plz Authorize OR Reject first  ");
                    hashTrans = null;
                    pendingList = null;
                    txtAccNo.setText("");
                    return;
                }
            }
        }
    }
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        try {
            // TODO add your handling code here:
           String accNo= txtAccNo.getText();
           if(accNo==null || accNo.equalsIgnoreCase(""))
           {
               displayAlert("Please enter Account No!!!");
               return;
           }
           isTransactionPendingForAuth();
    //          if(cboProdType1.getSelectedItem()!=null && !cboProdType1.getSelectedItem().equals(""))
    //          {
    //          String prod_type1=cboProdType1.getSelectedItem().toString();
    //        if(prod_type1.equals("Share") || prod_type1.equals("MMBS") || prod_type1.equals("Investments") || prod_type1.equals("Borrowings"))
    //           {
    //               if(prod_type1.equals("Share"))
    //               {
    //                    TTIntegration ttIntgration = null;
    //                    HashMap paramMap = new HashMap();
    //                    paramMap.put("TransId", "");
    //                    paramMap.put("TransDt", "");
    //                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
    //                    ttIntgration.setParam(paramMap);
    //                     ttIntgration.integrationForPrint("ShareLedger", false);
    //               }
    //               if(prod_type1.equals("Investments"))
    //               {
    //                    TTIntegration ttIntgration = null;
    //                    HashMap paramMap = new HashMap();
    //                    paramMap.put("TransId", "");
    //                    paramMap.put("TransDt", "");
    //                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
    //                    ttIntgration.setParam(paramMap);
    //                    ttIntgration.integrationForPrint("InvestmentLedger", false);
    //               }
    //               if(prod_type1.equals("Borrowings"))
    //               {
    //                    TTIntegration ttIntgration = null;
    //                    HashMap paramMap = new HashMap();
    //                    paramMap.put("TransId", "");
    //                    paramMap.put("TransDt", "");
    //                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
    //                    ttIntgration.setParam(paramMap);
    //                    ttIntgration.integrationForPrint("BorrowingLedger", false);
    //               }
    //           }
    //          }
            this.dispose();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(GLAccountNumberListUI.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void getUpdateApproval() {
        try {
        //    if(trans_pid!=null && !trans_pid.equalsIgnoreCase("")) {
             //   HashMap singleAuthorizeMap = new HashMap();
             //   singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            //    singleAuthorizeMap.put("TRANS_DT", currDt);
            //    singleAuthorizeMap.put(CommonConstants.TRANS_ID, trans_pid );
            //    singleAuthorizeMap.put(CommonConstants.BRANCH_ID,  ProxyParameters.BRANCH_ID);
                
           //     ClientUtil.execute("approveCashier", singleAuthorizeMap);
               
//                insertIntoDenaomination();
              //  ((DefaultTreeModel)treData.getModel()).removeNodeFromParent(selectedNode);
                //                cboStatusActionPerformed(null);
            //}
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }    public void getMultiplyValues() {
        try {
            //System.out.println("R= "+row +" C="+col);
            //JOptionPane.showMessageDialog(null,tblData.getValueAt(row,col).toString());
            //  String AccNo=tblData.getValueAt(row,0).toString();
            //  String Name=tblData.getValueAt(row,1).toString();
            //   String Amt=tblData.getValueAt(row,3).toString();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }    
    public void clearTransDetails() {
        //lblScheme.setText("");
      //  lblName.setText("");
      //  lblTransId.setText("");
      //  lblTransType.setText("");
     //   lblAmount.setText("");
      //  lblPostedBy.setText("");
      //  lblAuthBy.setText("");
      //  lblParticulars.setText("");
      //  lblPanNo.setText("");
      //  lblAcNo.setText("");
      //  lblTkNo.setText("");
      //  lblInstType.setText("");
      //  lblInstNo.setText("");
      //  lblInstDate.setText("");
      //  lblClearBal.setText("");
      //  lblOpBal.setText("");
      //  lblReceipt.setText("");
      //  lblPayment.setText("");
       // lblClosingBal.setText("");
//        initTableData();
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
//if(cboProdType1.getSelectedItem()!=null && !cboProdType1.getSelectedItem().equals("")){
//        popUp();
//}else{
    if(cboProdType.getSelectedItem()!=null && cboProdType.getSelectedItem().equals("General Ledger")){
    final HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
        new ViewAll(this, viewMap).show();    
    }else{
        if(cboProdType.getSelectedItem().equals("Share") || cboProdType.getSelectedItem().equals("MMBS") || cboProdType.getSelectedItem().equals("Investments") || cboProdType.getSelectedItem().equals("Borrowings")){
         popUp();   
        }else{
        final HashMap viewMap = new HashMap();
        System.out.println("getCbmProdType().getKeyForSelected().toString()???>>>>"+getCbmProdType().getKeyForSelected().toString());
         System.out.println("getCbmProdid().getKeyForSelected().toString()???>>>>"+getCbmProdId().getKeyForSelected().toString());
     viewMap.put("PROD_TYPE", getCbmProdType().getKeyForSelected().toString());
     viewMap.put("PROD_ID", getCbmProdId().getKeyForSelected().toString());
     viewMap.put(CommonConstants.MAP_WHERE, viewMap);
        viewMap.put(CommonConstants.MAP_NAME, "getAllProductAccNoForGlInCash");
        new ViewAll(this, viewMap).show(); 
        }
    }
//}
    }//GEN-LAST:event_btnAccNoActionPerformed

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
//        if(cboProdType1.getSelectedItem()!=null && !cboProdType1.getSelectedItem().equals(""))
//       {
//           displayAlert("Enter only one product type!!!");
//           return;
//       }
        System.out.println("cboProdType.getSelectedItem()11>>>"+cboProdType.getSelectedItem());
        if(cboProdType.getSelectedItem()!=null && cboProdType.getSelectedItem().equals("General Ledger")){
            cboProdId.setEnabled(false);
            lblAccNo.setText("Account Head");
        }else{
          cboProdId.setEnabled(true);
            lblAccNo.setText("Account No");   
        }
         if(cboProdType.getSelectedItem().equals("Share") || cboProdType.getSelectedItem().equals("MMBS") || cboProdType.getSelectedItem().equals("Investments") || cboProdType.getSelectedItem().equals("Borrowings")){
        setCbmProdId1(cboProdType.getSelectedItem().toString());
         }else{
         System.out.println("getCbmProdType().getKeyForSelected().getKeyForSelected().toString()111>>"+(getCbmProdType().getKeyForSelected().toString()));
        setCbmProdId(getCbmProdType().getKeyForSelected().toString());
        cboProdId.setModel(getCbmProdId());
         }
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
                                         

private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {                                          
// TODO add your handling code here:
}                                         
 public void setCbmProdId1(String prod_type)  
 {
     try
     {
      //   System.out.println("prod_type  =================="+prod_type);
     HashMap singleAuthorizeMap = new HashMap();
     if(prod_type.equals("Share"))
     {
         System.out.println("prod_type111  =================="+prod_type);
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
         System.out.println("prod_type222  =================="+prod_type);
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
                List aList= ClientUtil.executeQuery("getSelectEachSchemeDetails", mapData);
                System.out.println("prod_type  =====mapData============="+mapData);
               key.add(" ");
               value.add(" ");
               
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
                 key.add(" ");
               value.add(" ");
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
               // cboProdId.addItem("");
                System.out.println("prod_type  =====key============="+key);
                System.out.println("prod_type  =====value============="+value);
                 cbmProdId= new ComboBoxModel(key,value);
                cboProdId.setModel(getCbmProdId());
     }
     else if(prod_type.equals("Borrowings"))
     {
         System.out.println("prod_type444  =================="+prod_type);
         cboProdId.setSelectedIndex(0);
         cboProdId.setEnabled(false);
       // lookUpHash.put(CommonConstants.MAP_NAME,SHARE_TYPE);
       /// lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
       // keyValue = ClientUtil.populateLookupData(lookUpHash);
       // getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
       //  key=null;value=null;
       //   cboProdId.removeAllItems();
      //   key.add(" ");
       //        value.add(" ");
      //   System.out.println("key>>"+key+"value>>"+value);
      //  cbmProdId= new ComboBoxModel(key,value);
      //   System.out.println("cbmProdId111>>>"+cbmProdId);
        // System.out.println("getCbmProdId()111>>>"+getCbmProdId());
       // cboProdId.setModel(getCbmProdId());
      //  cboProdId.removeAllItems();
     }
     else
     {
          key=null;value=null;
        cbmProdId= new ComboBoxModel(key,value);
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
    public static void main(String args[]) {
        new CashierApprovalUI().show();
    }
    private DefaultMutableTreeNode addNode(DefaultMutableTreeNode parent,DefaultMutableTreeNode child,Object obj) {
        if (!chkExistance(parent,obj.toString())){
            child = new DefaultMutableTreeNode(obj);
        }
        if (!parent.isNodeChild(child)){
            parent.add(child);
        }
        return child;
    }
  /*  public DefaultTreeModel getAcHdTree(String status) {
        DefaultMutableTreeNode parent = new DefaultMutableTreeNode("Cashier");
        DefaultMutableTreeNode receipts = new DefaultMutableTreeNode("Receipts");
        DefaultMutableTreeNode payments = new DefaultMutableTreeNode("Payments");
        DefaultMutableTreeNode receiptsTr = null;
        DefaultMutableTreeNode paymentsTr = null;
        /////////////////////////////////////////////////////////////////////
        //  String status=cboStatus.getSelectedItem().toString();
        HashMap singleAuthorizeMap = new HashMap();
        singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt);
        singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, status);
        singleAuthorizeMap.put("TRANS_DT", currDt);
        //    singleAuthorizeMap.put("TYPE", "AUTHORIZED");
        singleAuthorizeMap.put(CommonConstants.BRANCH_ID,  ProxyParameters.BRANCH_ID);
        final List objList =  ClientUtil.executeQuery("getCashReceiptsTransTree", singleAuthorizeMap);
        final List objListPay =  ClientUtil.executeQuery("getCashPaymentsTransTree", singleAuthorizeMap);
        // final List objList = ClientUtil.executeQuery("getCashTransTree", null);
        final int objListSize = objList.size();
        for (int i = 0;i<objListSize;i++) {
            receiptsTr = addNode(receipts,receiptsTr,((HashMap)objList.get(i)).get("TRANS_ID"));
        }
        parent.add(receipts);
        for (int j = 0;j<objListPay.size();j++) {
            paymentsTr = addNode(payments,paymentsTr,((HashMap)objListPay.get(j)).get("TRANS_ID"));
        }
        parent.add(payments);
        final DefaultTreeModel treemodel = new DefaultTreeModel(parent);
        //  final DefaultTreeModel treemodelPay = new DefaultTreeModel(payments);
        treData.setModel(treemodel);
        //    treData.setModel(treemodelPay);
        root = null;
        receiptsTr = null;
        paymentsTr = null;
        
        
        return treemodel;
    }*/
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
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CPanel panCashTrans;
    private com.see.truetransact.uicomponent.CTextField txtAccNo;
    // End of variables declaration//GEN-END:variables
    
}

