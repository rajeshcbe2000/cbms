/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * HeadUI.java
 *
 * Created on August 7, 2003, 3:18 PM
 */

package com.see.truetransact.ui.share;

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.commonutil.AcctStatusConstants;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.ArrayList;
import java.util.Observer;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.util.List;
import java.util.GregorianCalendar;
import com.see.truetransact.ui.share.DeathReliefMasterRB;
import com.see.truetransact.ui.share.DeathReliefMasterOB;
import org.apache.log4j.Logger;
import javax.swing.table.AbstractTableModel;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
/**
 *
 * @author
 */
public class DeathReliefMasterUI extends CInternalFrame implements Observer, UIMandatoryField{
    private String actionType = "";
    private DeathReliefMasterOB observable;
    DeathReliefMasterMRB objMandatoryRB = new DeathReliefMasterMRB();
    private Date curDate = null;
    private String mandatoryMessage="";
    private HashMap mandatoryMap;
    final int AUTHORIZE=3;
    final int DELETE = 1;
    final int LIABILITYGL = 8;
    final int EXPENDITUREGL = 9;
     final int EXPENDITUREGL1 = 10;
      final int EXPENDITUREGL2 = 11;
    private String ACCT_TYPE = "ACCT_TYPE";
    private String BALANCETYPE = "BALANCETYPE";
    private int viewType=-1;
    boolean isFilled = false;
    private final String CLASSNAME = this.getClass().getName();
    boolean flag = false;
    private boolean salSaveButton =  false;
    private boolean tabEdit =  false;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.share.DeathReliefMasterRB", ProxyParameters.LANGUAGE);
   //mmee
    private ArrayList buffer1 =new ArrayList();
    private HashMap bufferMap=new HashMap();
    private List bufferList=new ArrayList();
    private  Object columnNames[] = { "From Date","To Date","Interest Date" };
    //mmee
    private boolean drmMasterFlag = false;
    private boolean drmMasterFlag1= false;
    private static String interestId="";
    private boolean saveflag=false;
    private boolean tabedit=false;
    private int tabrow=-1;
    /** Creates new form HeadUI */
    public DeathReliefMasterUI() {
        initComponents();
        initStartup();
    }
    
    private void initStartup() {
        curDate = ClientUtil.getCurrentDate();
        setFieldNames();
        internationalize();
        setHelpMessage();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panInterestPayable,getMandatoryHashMap());
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panDeathReliefFund,getMandatoryHashMap());
        //new MandatoryCheck().putMandatoryMarks(getClass().getName(),panTableDt,getMandatoryHashMap());
        setObservable();
        initTableData();
        setButtonEnableDisable();
        setMaximumLength();
        btnCancelActionPerformed(null);
       setComboCalculationFrequency();
        setComboCalculationCriteria();
        setComboProductFrequency();
        txtToDt.setEnabled(false);
        txtToDt.setEditable(false);
       getTableData();
       //Added By Revathi.L
       lblNoNominee.setVisible(false);
       txtNoNominees.setVisible(false);
    }
    public void setMandatoryHashMap()
    {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtProductId", new Boolean(true));
        mandatoryMap.put("txtPaymentAmount", new Boolean(true));
        mandatoryMap.put("txtPaymentHeadName", new Boolean(true));
        mandatoryMap.put("txtDrfName", new Boolean(true));
        mandatoryMap.put("txtActHeadName", new Boolean(true));
        //mandatoryMap.put("txtDrfAmount", new Boolean(true));
        mandatoryMap.put("tdtDrfFromDt", new Boolean(true));
        mandatoryMap.put("tdtDrfToDt", new Boolean(true));
        mandatoryMap.put("txtInterestRate",new Boolean(true));
        mandatoryMap.put("cboCalculationFrequency",new Boolean(true));
        mandatoryMap.put("cboCalclulationCriteria",new Boolean(true));
        mandatoryMap.put("cboProductFrequency",new Boolean(true));
        mandatoryMap.put("txtDebitHead",new Boolean(true));
       mandatoryMap.put("tdtCalculatedDt",new Boolean(true));
       mandatoryMap.put("tdtFromDt",new Boolean(true));
        mandatoryMap.put("txtToDt",new Boolean(true));
        mandatoryMap.put("txtInterestRate",new Boolean(true));
    mandatoryMap.put("txtRecoveryHead",new Boolean(true));
    mandatoryMap.put("lblAmountRecovery",new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
    
    //initailizing Table 
     public void getTableData()
    {
       
    Object rowData[][] = { { "", "", "" } };
    Object columnNames[] = { "From Date","To Date","Interest Rate" };   
    
 tblInterest.setModel(new javax.swing.table.DefaultTableModel(rowData,columnNames)
           
            {
            
        
         public boolean isCellEditable(int row, int column) {
       //Only the third column
       return false;
            }
        
        }) ;
           
  
   tblInterest.setVisible(true);

            }
    
    
     
     
     //Table with Data
     private void getTableData1() {
        
        Object rowData[][] = new Object[+bufferList.size()][3];
       //  Integer rowData[][] = new  Integer[buffer1.size()][3];
        int j=0;
        String d1="";
        String d2="";
        int i=0;
        System.out.println("BuufferrList  "+bufferList.size());
        
        //To date Findings
        if(bufferList.size()>0)
        {
        int sz=bufferList.size()-1;
        HashMap h=new HashMap();
        Calendar cal=null;  
        h=(HashMap)bufferList.get(sz);
        String s=h.get("FROM").toString();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); 
       Date dfrm=null;
        try {
       dfrm=(Date)formatter.parse(s);
 } catch (Exception e) {
     e.printStackTrace();
   }
System.out.println(dfrm); 

 cal = Calendar.getInstance();
cal.setTime(dfrm);
//intdd=cal.get(Calendar.DAY_OF_MONTH);
System.out.println("Callll1"+cal.DATE);
cal.add(Calendar.DAY_OF_MONTH, -1);
System.out.println("Callll"+cal.DATE);
String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" +         cal.get(Calendar.YEAR);
        
   // h.put("FROM",formatedDate);  
if(sz>0){
h=(HashMap)bufferList.get(sz-1);
h.put("TO",formatedDate);
bufferList.set(sz-1,h);
}
        }    
        
        
        for(i=0;i<bufferList.size();i++)
        {
            HashMap m=new HashMap();
            m=(HashMap)bufferList.get(i);
            System.out.println("iii m00001 : "+m.get("FROM"));
            rowData[i][0]=m.get("FROM").toString();
             System.out.println("iii m000011 : "+m.get("TO"));
             rowData[i][1]=m.get("TO").toString();
              rowData[i][2]=m.get("INTEREST").toString();
               System.out.println("iii m0000 111: "+m.get("INTEREST"));
        }
         System.out.println("iii m0000 222: ");
      
          
        
        
        
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        
   
   tblInterest.setModel(new javax.swing.table.DefaultTableModel(rowData,columnNames){
            
        
         public boolean isCellEditable(int row, int column) {
       //Only the third column
       return false;
   }
        
        }) ;
   tblInterest.setVisible(true);

            }
     
     
     
     
     
    
    private void setObservable(){
        /* Singleton pattern can't be implemented as there are two observers using the same observable*/
        // The parameter '1' indicates that the customer type is INDIVIDUAL
        observable = new DeathReliefMasterOB(1);
        observable.addObserver(this);
    }
    private void setMaximumLength(){
        txtProductId.setMaxLength(16);
        txtProductId.setAllowAll(true);
        txtDrfName.setMaxLength(128);
        txtProductId.setAllowAll(true);
        txtActHeadName.setMaxLength(128);
        txtActHeadName.setAllowAll(true);
        txtDrfAmount.setMaxLength(14);
        txtDrfAmount.setValidation(new CurrencyValidation(14,2));
        txtPaymentAmount.setValidation(new CurrencyValidation(14,2));
       txtRecoverAmount.setMaxLength(14);
        txtRecoverAmount.setValidation(new CurrencyValidation(14,2));
        //txtInterestRate.setValidation(new NumericValidation(4,2));
        //Added By Revathi.L
        txtNoNominees.setAllowAll(true);
        txtRecoveryHead.setAllowAll(true);
    }
    
    
     public void setComboCalculationFrequency()
    { String Tobe[]=new String[4];
    Tobe[0]="--Select--";
    Tobe[1]="Yearly";
    Tobe[2]="Quarterly";
    Tobe[3]="Monthly";
    
      cboCalculationFrequency.setModel(new javax.swing.DefaultComboBoxModel(Tobe));         
    }
     public void setComboCalculationCriteria()
    { String Tobe[]=new String[4];
    Tobe[0]="--Select--";
    Tobe[1]="Minimum";
    Tobe[2]="Maximum";
    Tobe[3]="Average";
    
      cboCalclulationCriteria.setModel(new javax.swing.DefaultComboBoxModel(Tobe));         
    }
     public void setComboProductFrequency()
    { String Tobe[]=new String[7];
    Tobe[0]="--Select--";
    Tobe[1]="Daily";
    Tobe[2]="Weekly";
    Tobe[3]="Monthly";
    Tobe[4]="Quarterly";
    Tobe[5]="Half yearly";
    Tobe[6]="Yearly";
    
      cboProductFrequency.setModel(new javax.swing.DefaultComboBoxModel(Tobe));         
    }
    
    
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new DeathReliefMasterMRB();
        txtDrfAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDrfAmount"));
        txtPaymentAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPaymentAmount"));
        tdtDrfFromDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDrfFromDt"));
        tdtDrfToDt.setHelpMessage(lblMsg,objMandatoryRB.getString("tdtDrfToDt"));
        txtActHeadName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtActHeadName"));
        txtProductId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductId"));
        txtDrfName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDrfName"));
        txtPaymentHeadName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPaymentHeadName"));
        
    //   lblCalculationFrequency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCalculationFrequency"));
//         cboCalclulationCriteria.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCalclulationCriteria"));
//          cboProductFrequency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductFrequency"));
//           txtDebitHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitHead"));
//           
    }
    
 /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnActHeadName.setName("btnActHeadName");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnDrfDetailsDelete.setName("btnDrfDetailsDelete");
        btnDrfDetailsNew.setName("btnDrfDetailsNew");
        btnDrfDetailsSave.setName("btnDrfDetailsSave");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
      //add
        btnTabNew.setName("btnTabNew");
        btnTabSave.setName("btnTabSave");
        btnTabDelete.setName("btnTabDelete");
        btnDebitHead.setName("btnDebitHead");
       lblCalculationFrequency.setName("lblCalculationFrequency");
       lblCalclulationCriteria.setName("lblCalclulationCriteria");
       lblProductFrequency.setName("lblProductFrequency");
       lblCalculatedDt.setName("lblCalculatedDt");
        lblFromDt.setName("lblFromDt");
        lblToDt.setName("lblToDt");
        lblInterestRate.setName("lblInterestRate");
        lblDebitHead.setName("lblDebitHead");
        cboCalculationFrequency.setName("cboCalculationFrequency");
        cboCalclulationCriteria.setName("cboCalclulationCriteria");
        cboProductFrequency.setName("cboProductFrequency");
        txtDebitHead.setName("txtDebitHead");
        tdtCalculatedDt.setName("tdtCalculatedDt");
        tdtFromDt.setName("tdtFromDt");
        txtToDt.setName("txtToDt");
        txtInterestRate.setName("txtInterestRate");
        
        
        btnNew.setName("btnNew");
        btnPaymentHeadName.setName("btnPaymentHeadName");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnView.setName("btnView");
        dlgMajorHead.setName("dlgMajorHead");
        lblAccountHeadType.setName("lblAccountHeadType");
        lblActHeadName.setName("lblActHeadName");
        lblDrfAmount.setName("lblDrfAmount");
        lblDrfFromDt.setName("lblDrfFromDt");
        lblDrfToDt.setName("lblDrfToDt");
        lblDrfName.setName("lblDrfName");
        lblMsg.setName("lblMsg");
        lblPaymentAmount.setName("lblPaymentAmount");
        lblPaymentHeadName.setName("lblPaymentHeadName");
        lblPaymentHeadType.setName("lblPaymentHeadType");
        lblProductId.setName("lblProductId");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace4.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        lblStatus.setName("lblStatus");
        mbrMain.setName("mbrMain");
        panButton.setName("panButton");
        panDeathReliefFund.setName("panDeathReliefFund");
        panDrfDetails.setName("panDrfDetails");
        panDrfProductDesc.setName("panDrfProductDesc");
        panMajorHeadCode.setName("panMajorHeadCode");
        panStatus.setName("panStatus");
        panTable.setName("panTable");
        srpDrfProdDesc.setName("srpDrfProdDesc");
        srpMajorHead.setName("srpMajorHead");
        tblDrfProdDesc.setName("tblDrfProdDesc");
        tblMajorHead.setName("tblMajorHead");
        tdtDrfFromDt.setName("tdtDrfFromDt");
        tdtDrfToDt.setName("tdtDrfToDt");
        txtActHeadName.setName("txtActHeadName");
        txtDrfAmount.setName("txtDrfAmount");
        txtDrfName.setName("txtDrfName");
        txtPaymentAmount.setName("txtPaymentAmount");
        txtPaymentHeadName.setName("txtPaymentHeadName");
        txtProductId.setName("txtProductId");
        txtRecoveryHead.setName("txtRecoveryHead");
        lblAmountRecovery.setName("lblAmountRecovery");
        lblRecoveryHead.setName("lblRecoveryHead");
    }
    
    public void update(Observable observed, Object arg) {
        //        java.util.Observable observable, Object obj
        updateDrfMaster();
    }
    private void updateDrfMaster(){
        txtProductId.setText(observable.getTxtProductId());
        txtDrfAmount.setText(observable.getTxtDrfAmount());
        txtPaymentAmount.setText(observable.getTxtPaymentAmount());
        tdtDrfFromDt.setDateValue(observable.getTdtDrfFromDt());
        tdtDrfToDt.setDateValue(observable.getTdtDrfToDt());
        txtActHeadName.setText(observable.getTxtActHeadName());
        txtDrfName.setText(observable.getTxtDrfName());
        txtPaymentHeadName.setText(observable.getTxtPaymentHeadName());
        txtDrfName.setText(observable.getTxtDrfName());
        txtRecoveryHead.setText(observable.getTxtRecoveryHead());
        if(observable.getRdAmountRecovery().equals("YES"))
        {
           txtRecoverAmount.setEnabled(true);
           rdAmountRecoveryYes.setSelected(true); 
           txtRecoverAmount.setText(observable.getTxtRecoveryAmount());
        }
        else
        {
            rdAmountRecoveryNo.setSelected(true); 
            txtRecoverAmount.setText("");
             txtRecoverAmount.setEnabled(false);
       }
        
       cboCalclulationCriteria.setSelectedItem(observable. getCboCalculationCriteria());
       cboCalculationFrequency.setSelectedItem(observable.getCboCalculationFrequency());
       cboProductFrequency.setSelectedItem(observable.getCboProductFrequency());
       txtDebitHead.setText(observable.getTxtDebitHead());
       tdtCalculatedDt.setDateValue(observable.getTdtCalculatedDt());
       if(observable.isNominee()==true){//Added By Revathi.L
           chkNominee.setSelected(true);
//           txtNoNominees.setVisible(true);
//           lblNoNominee.setVisible(true);
//           txtNoNominees.setEnabled(true);
//           txtNoNominees.setText(CommonUtil.convertObjToStr(observable.getNoNominee()));
       }else{
           chkNominee.setSelected(false);
           txtNoNominees.setVisible(false);
           lblNoNominee.setVisible(false);
           txtNoNominees.setEnabled(false); 
       }
       bufferList=observable.getBuffer();
       //bufferList.clear();
        System.out.println("inn uii obbb "+bufferList.size());
      bufferList= createListt(bufferList);
       System.out.println("inn uii obbb "+bufferList.size());
       interestId=observable.getInterestID();
        System.out.println("inn uii Innterstid  "+interestId);
       getTableData1();
       
           
    }
    private List createListt(List ol)
    {
        List tabList=new ArrayList();
     String s1="";
     String s2="";
     String s3="";
     int o=ol.size()-1;
     System.out.println("OLLLllll......"+ol.size());
        for(int h=0;h<ol.size();h++)
        {
            HashMap bb=new HashMap();
            
            HashMap m=new HashMap();
            m=(HashMap)ol.get(h);
            System.out.println("iii m00001 : "+m.get("FROM"));
            s1=m.get("FROM").toString();
             System.out.println("iii m000011 : "+m.get("FROM"));
             if(m.get("TO").toString().equals("-"))
             {
                 s2="-";
             }
             else
             {
            s2=m.get("TO").toString();
             }
            s3=m.get("INTEREST").toString();
            System.out.println("iii m0000 111: "+m.get("FROM"));
            
               
              String dt;
   Date date=null;
   DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");   
       Calendar cal=null;  
       String formatedDate ="";
         if(!isValidDate(s1))   {   
               
    
   try {
       date = (Date)formatter.parse(s1);
 } catch (Exception e) {
     e.printStackTrace();
   }
System.out.println(date); 

 cal = Calendar.getInstance();
cal.setTime(date);
 formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" +         cal.get(Calendar.YEAR);
System.out.println("formatedDate : " + formatedDate); 
   bb.put("FROM",formatedDate);
         }
         else
         {
             bb.put("FROM",s1);
         }
   if(!isValidDate(s1)&&(!s2.equals("-"))){
   try {
       date = (Date)formatter.parse(s2);
 } catch (Exception e) {
     e.printStackTrace();
   }
System.out.println(date); 

 cal = Calendar.getInstance();
cal.setTime(date);
formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" +         cal.get(Calendar.YEAR);
System.out.println("formatedDate : " + formatedDate); 
  bb.put("TO",formatedDate); 
   }
   else
   {
        bb.put("TO",s2); 
   }
   bb.put("INTEREST",s3);    
   tabList.add(bb);
        }
     return tabList;
    }
    
    
   

boolean isValidDate(String input) {
     SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
     try {
          format.parse(input);
          return true;
     }
     catch(Exception e){
          return false;
     }
}
    
    
    
    
    private void internationalize() {
        resourceBundle = new DeathReliefMasterRB();
        btnDrfDetailsNew.setText(resourceBundle.getString("btnDrfDetailsNew"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblDrfName.setText(resourceBundle.getString("lblDrfName"));
        ((javax.swing.border.TitledBorder)panDrfProductDesc.getBorder()).setTitle(resourceBundle.getString("panDrfProductDesc"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblDrfFromDt.setText(resourceBundle.getString("lblDrfFromDt"));
        lblPaymentHeadName.setText(resourceBundle.getString("lblPaymentHeadName"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblProductId.setText(resourceBundle.getString("lblProductId"));
        ((javax.swing.border.TitledBorder)panDrfDetails.getBorder()).setTitle(resourceBundle.getString("panDrfDetails"));
        btnView.setText(resourceBundle.getString("btnView"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnActHeadName.setText(resourceBundle.getString("btnActHeadName"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblAccountHeadType.setText(resourceBundle.getString("lblAccountHeadType"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnDrfDetailsSave.setText(resourceBundle.getString("btnDrfDetailsSave"));
        lblSpace4.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblPaymentHeadType.setText(resourceBundle.getString("lblPaymentHeadType"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblPaymentAmount.setText(resourceBundle.getString("lblPaymentAmount"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblDrfAmount.setText(resourceBundle.getString("lblDrfAmount"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblActHeadName.setText(resourceBundle.getString("lblActHeadName"));
        btnPaymentHeadName.setText(resourceBundle.getString("btnPaymentHeadName"));
        lblSpace6.setText(resourceBundle.getString("lblSpace6"));
        btnDrfDetailsDelete.setText(resourceBundle.getString("btnDrfDetailsDelete"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }
    /* Auto Generated Method - setMandatoryHashMap()
     
//ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
//
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
//    public void setMandatoryHashMap() {
//        mandatoryMap = new HashMap();
//        mandatoryMap.put("txtDrfAmount", new Boolean(true));
//        mandatoryMap.put("txtPaymentAmount", new Boolean(true));
//        mandatoryMap.put("tdtDrfFromDt", new Boolean(true));
//        mandatoryMap.put("tdtDrfToDt",new Boolean(false));
//        mandatoryMap.put("txtActHeadName", new Boolean(true));
//        mandatoryMap.put("txtProductId", new Boolean(true));
//        mandatoryMap.put("txtDrfName", new Boolean(true));
//        mandatoryMap.put("txtPaymentHeadName", new Boolean(true));
//    }
    //
    ///* Auto Generated Method - getMandatoryHashMap()
    //   Getter method for setMandatoryHashMap().*/
//    public HashMap getMandatoryHashMap() {
//        return mandatoryMap;
//    }
//    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtDrfAmount(txtDrfAmount.getText());
        observable.setTxtPaymentAmount(txtPaymentAmount.getText());
        observable.setTdtDrfFromDt(tdtDrfFromDt.getDateValue());
        observable.setTdtDrfToDt(tdtDrfToDt.getDateValue());
        observable.setTxtActHeadName(txtActHeadName.getText());
        observable.setTxtProductId(txtProductId.getText());
        observable.setTxtDrfName(txtDrfName.getText());
        observable.setTxtPaymentHeadName(txtPaymentHeadName.getText());
        //////added by me///////
        observable.setCboCalculationFrequency(cboCalculationFrequency.getSelectedItem().toString());
        System.out.println("UPPDATEOOBB FFEEIILLLDDSSS"+observable.getCboCalculationFrequency());
        observable.setCboCalculationCriteria(cboCalclulationCriteria.getSelectedItem().toString());
        observable.setCboProductFrequency(cboProductFrequency.getSelectedItem().toString());
        observable.setTxtDebitHead(txtDebitHead.getText());
        observable.setTdtCalculatedDt(tdtCalculatedDt.getDateValue());
        observable.setTdtFromDt(tdtFromDt.getDateValue());
        observable.setTxtToDt(txtToDt.getText());
        observable.setTxtInterestRate(txtInterestRate.getText());
        
        observable.setTxtRecoveryHead(txtRecoveryHead.getText());
        if(rdAmountRecoveryYes.isSelected())
        {
        observable.setRdAmountRecovery("YES");
        rdAmountRecoveryYes.setSelected(false);
        txtRecoverAmount.setEnabled(true);
        }
        else
        {
            observable.setRdAmountRecovery("NO");
            txtRecoverAmount.setEnabled(false);
        }
         if(rdAmountRecoveryYes.isSelected())
        {
             txtRecoverAmount.setEnabled(true);
          observable.setTxtRecoveryAmount(txtRecoverAmount.getText());
          
          //rdAmountRecoveryYes.setSelected(false);
         }
         else
         {
             observable.setTxtRecoveryAmount("");
              txtRecoverAmount.setEnabled(false);
         }
        observable.setInterestID(interestId);
        System.out.println("IINNTTID IN UI>>>"+interestId+"  >>>>>"+ observable.getInterestID());
          System.out.println("DDDUI2");
        observable.setBuffer(bufferList);
          System.out.println("DDDUI1");
         //Added By Revathi.L
        if (chkNominee.isSelected() == true) {
            observable.setNominee(true);
            observable.setNoNominee(txtNoNominees.getText());
        }else{
            observable.setNominee(false);
            observable.setNoNominee(txtNoNominees.getText());
        }
    }
    private void initTableData(){
        tblDrfProdDesc.setModel(observable.getTblDrfProdDesc());
        
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        dlgMajorHead = new com.see.truetransact.uicomponent.CDialog();
        srpMajorHead = new com.see.truetransact.uicomponent.CScrollPane();
        tblMajorHead = new com.see.truetransact.uicomponent.CTable();
        cButtonGroup1 = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrHead = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
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
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panDeathReliefFund = new com.see.truetransact.uicomponent.CPanel();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srpDrfProdDesc = new com.see.truetransact.uicomponent.CScrollPane();
        tblDrfProdDesc = new com.see.truetransact.uicomponent.CTable();
        panDrfDetails = new com.see.truetransact.uicomponent.CPanel();
        lblDrfAmount = new com.see.truetransact.uicomponent.CLabel();
        lblPaymentAmount = new com.see.truetransact.uicomponent.CLabel();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnDrfDetailsSave = new com.see.truetransact.uicomponent.CButton();
        btnDrfDetailsNew = new com.see.truetransact.uicomponent.CButton();
        btnDrfDetailsDelete = new com.see.truetransact.uicomponent.CButton();
        txtDrfAmount = new com.see.truetransact.uicomponent.CTextField();
        txtPaymentAmount = new com.see.truetransact.uicomponent.CTextField();
        lblDrfFromDt = new com.see.truetransact.uicomponent.CLabel();
        tdtDrfFromDt = new com.see.truetransact.uicomponent.CDateField();
        lblDrfToDt = new com.see.truetransact.uicomponent.CLabel();
        tdtDrfToDt = new com.see.truetransact.uicomponent.CDateField();
        lblAmountRecovery = new com.see.truetransact.uicomponent.CLabel();
        lblRecoverAmount = new com.see.truetransact.uicomponent.CLabel();
        txtRecoverAmount = new com.see.truetransact.uicomponent.CTextField();
        panExistingCustomer = new com.see.truetransact.uicomponent.CPanel();
        rdAmountRecoveryYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdAmountRecoveryNo = new com.see.truetransact.uicomponent.CRadioButton();
        panDrfProductDesc = new com.see.truetransact.uicomponent.CPanel();
        lblDrfName = new com.see.truetransact.uicomponent.CLabel();
        lblActHeadName = new com.see.truetransact.uicomponent.CLabel();
        txtActHeadName = new com.see.truetransact.uicomponent.CTextField();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        panMajorHeadCode = new com.see.truetransact.uicomponent.CPanel();
        btnActHeadName = new com.see.truetransact.uicomponent.CButton();
        txtProductId = new com.see.truetransact.uicomponent.CTextField();
        txtDrfName = new com.see.truetransact.uicomponent.CTextField();
        lblPaymentHeadName = new com.see.truetransact.uicomponent.CLabel();
        txtPaymentHeadName = new com.see.truetransact.uicomponent.CTextField();
        btnPaymentHeadName = new com.see.truetransact.uicomponent.CButton();
        lblPaymentHeadType = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHeadType = new com.see.truetransact.uicomponent.CLabel();
        txtRecoveryHead = new com.see.truetransact.uicomponent.CTextField();
        lblRecoveryHead = new com.see.truetransact.uicomponent.CLabel();
        btnRecoveryHead = new com.see.truetransact.uicomponent.CButton();
        panDelinked = new com.see.truetransact.uicomponent.CPanel();
        chkNominee = new com.see.truetransact.uicomponent.CCheckBox();
        lblNoNominee = new com.see.truetransact.uicomponent.CLabel();
        txtNoNominees = new com.see.truetransact.uicomponent.CTextField();
        panInterestPayable = new javax.swing.JPanel();
        panRecordDetails = new javax.swing.JPanel();
        lblCalculationFrequency = new javax.swing.JLabel();
        lblCalclulationCriteria = new javax.swing.JLabel();
        lblProductFrequency = new javax.swing.JLabel();
        lblDebitHead = new javax.swing.JLabel();
        lblCalculatedDt = new javax.swing.JLabel();
        cboCalculationFrequency = new javax.swing.JComboBox();
        cboCalclulationCriteria = new javax.swing.JComboBox();
        cboProductFrequency = new javax.swing.JComboBox();
        btnDebitHead = new com.see.truetransact.uicomponent.CButton();
        tdtCalculatedDt = new com.see.truetransact.uicomponent.CDateField();
        txtDebitHead = new javax.swing.JTextField();
        panTableDt = new javax.swing.JPanel();
        lblFromDt = new javax.swing.JLabel();
        lblToDt = new javax.swing.JLabel();
        lblInterestRate = new javax.swing.JLabel();
        tdtFromDt = new com.see.truetransact.uicomponent.CDateField();
        txtToDt = new javax.swing.JTextField();
        txtInterestRate = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInterest = new javax.swing.JTable();
        btnTabNew = new javax.swing.JButton();
        btnTabSave = new javax.swing.JButton();
        btnTabDelete = new javax.swing.JButton();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        sptEdit = new javax.swing.JSeparator();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        dlgMajorHead.getContentPane().setLayout(new java.awt.GridBagLayout());

        tblMajorHead.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Major Head Code", "Major Head Name"
            }
        ));
        srpMajorHead.setViewportView(tblMajorHead);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        dlgMajorHead.getContentPane().add(srpMajorHead, gridBagConstraints);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("DRF Description");
        setMinimumSize(new java.awt.Dimension(730, 114));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrHead.add(btnView);

        lblSpace6.setText("     ");
        tbrHead.add(lblSpace6);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrHead.add(btnNew);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrHead.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace52);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrHead.add(btnDelete);

        lblSpace2.setText("     ");
        tbrHead.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrHead.add(btnSave);

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace53);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrHead.add(btnCancel);

        lblSpace3.setText("     ");
        tbrHead.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrHead.add(btnAuthorize);

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrHead.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace55);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrHead.add(btnReject);

        lblSpace4.setText("     ");
        tbrHead.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrHead.add(btnPrint);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace56);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrHead.add(btnClose);

        getContentPane().add(tbrHead, java.awt.BorderLayout.NORTH);

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

        jTabbedPane1.setNextFocusableComponent(lblMsg);

        panDeathReliefFund.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panDeathReliefFund.setMinimumSize(new java.awt.Dimension(720, 440));
        panDeathReliefFund.setPreferredSize(new java.awt.Dimension(800, 440));
        panDeathReliefFund.setLayout(new java.awt.GridBagLayout());

        panTable.setMinimumSize(new java.awt.Dimension(720, 355));
        panTable.setPreferredSize(new java.awt.Dimension(720, 355));
        panTable.setLayout(new java.awt.GridBagLayout());

        srpDrfProdDesc.setMaximumSize(new java.awt.Dimension(600, 350));
        srpDrfProdDesc.setMinimumSize(new java.awt.Dimension(690, 350));
        srpDrfProdDesc.setPreferredSize(new java.awt.Dimension(690, 350));

        tblDrfProdDesc.setModel(new javax.swing.table.DefaultTableModel(
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
        tblDrfProdDesc.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblDrfProdDesc.setMaximumSize(new java.awt.Dimension(2147483647, 10000));
        tblDrfProdDesc.setMinimumSize(new java.awt.Dimension(680, 750));
        tblDrfProdDesc.setOpaque(false);
        tblDrfProdDesc.setPreferredScrollableViewportSize(new java.awt.Dimension(680, 10000));
        tblDrfProdDesc.setPreferredSize(new java.awt.Dimension(680, 750));
        tblDrfProdDesc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDrfProdDescMousePressed(evt);
            }
        });
        srpDrfProdDesc.setViewportView(tblDrfProdDesc);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 0, 0);
        panTable.add(srpDrfProdDesc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -270;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 33, 0, 13);
        panDeathReliefFund.add(panTable, gridBagConstraints);

        panDrfDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Product Details"));
        panDrfDetails.setMinimumSize(new java.awt.Dimension(200, 200));
        panDrfDetails.setPreferredSize(new java.awt.Dimension(200, 200));
        panDrfDetails.setLayout(new java.awt.GridBagLayout());

        lblDrfAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDrfDetails.add(lblDrfAmount, gridBagConstraints);

        lblPaymentAmount.setText("Payment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDrfDetails.add(lblPaymentAmount, gridBagConstraints);

        panButton.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnDrfDetailsSave.setText("Save");
        btnDrfDetailsSave.setEnabled(false);
        btnDrfDetailsSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDrfDetailsSaveActionPerformed(evt);
            }
        });
        panButton.add(btnDrfDetailsSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(73, 4, -1, -1));

        btnDrfDetailsNew.setText("New");
        btnDrfDetailsNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDrfDetailsNewActionPerformed(evt);
            }
        });
        panButton.add(btnDrfDetailsNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 4, -1, -1));

        btnDrfDetailsDelete.setText("Delete");
        btnDrfDetailsDelete.setEnabled(false);
        btnDrfDetailsDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDrfDetailsDeleteActionPerformed(evt);
            }
        });
        panButton.add(btnDrfDetailsDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(143, 4, -1, -1));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.gridwidth = 15;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDrfDetails.add(panButton, gridBagConstraints);

        txtDrfAmount.setMaxLength(4);
        txtDrfAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDrfDetails.add(txtDrfAmount, gridBagConstraints);

        txtPaymentAmount.setMaxLength(128);
        txtPaymentAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDrfDetails.add(txtPaymentAmount, gridBagConstraints);

        lblDrfFromDt.setText("Effective From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDrfDetails.add(lblDrfFromDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDrfDetails.add(tdtDrfFromDt, gridBagConstraints);

        lblDrfToDt.setText("Effective Till");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDrfDetails.add(lblDrfToDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDrfDetails.add(tdtDrfToDt, gridBagConstraints);

        lblAmountRecovery.setText("Amount Recovery");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDrfDetails.add(lblAmountRecovery, gridBagConstraints);

        lblRecoverAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDrfDetails.add(lblRecoverAmount, gridBagConstraints);

        txtRecoverAmount.setMaxLength(4);
        txtRecoverAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDrfDetails.add(txtRecoverAmount, gridBagConstraints);

        panExistingCustomer.setMinimumSize(new java.awt.Dimension(100, 20));
        panExistingCustomer.setPreferredSize(new java.awt.Dimension(100, 20));
        panExistingCustomer.setLayout(new java.awt.GridBagLayout());

        cButtonGroup1.add(rdAmountRecoveryYes);
        rdAmountRecoveryYes.setText("Yes");
        rdAmountRecoveryYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdAmountRecoveryYesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panExistingCustomer.add(rdAmountRecoveryYes, gridBagConstraints);

        cButtonGroup1.add(rdAmountRecoveryNo);
        rdAmountRecoveryNo.setText("No");
        rdAmountRecoveryNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdAmountRecoveryNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panExistingCustomer.add(rdAmountRecoveryNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDrfDetails.add(panExistingCustomer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 190;
        gridBagConstraints.ipady = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathReliefFund.add(panDrfDetails, gridBagConstraints);

        panDrfProductDesc.setBorder(javax.swing.BorderFactory.createTitledBorder("Product Description"));
        panDrfProductDesc.setMinimumSize(new java.awt.Dimension(200, 70));
        panDrfProductDesc.setPreferredSize(new java.awt.Dimension(200, 70));
        panDrfProductDesc.setLayout(new java.awt.GridBagLayout());

        lblDrfName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDrfProductDesc.add(lblDrfName, gridBagConstraints);

        lblActHeadName.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDrfProductDesc.add(lblActHeadName, gridBagConstraints);

        txtActHeadName.setMaxLength(128);
        txtActHeadName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDrfProductDesc.add(txtActHeadName, gridBagConstraints);

        lblProductId.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDrfProductDesc.add(lblProductId, gridBagConstraints);

        panMajorHeadCode.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDrfProductDesc.add(panMajorHeadCode, gridBagConstraints);

        btnActHeadName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnActHeadName.setEnabled(false);
        btnActHeadName.setMinimumSize(new java.awt.Dimension(21, 21));
        btnActHeadName.setPreferredSize(new java.awt.Dimension(21, 21));
        btnActHeadName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActHeadNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDrfProductDesc.add(btnActHeadName, gridBagConstraints);

        txtProductId.setMaxLength(4);
        txtProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDrfProductDesc.add(txtProductId, gridBagConstraints);

        txtDrfName.setMaxLength(128);
        txtDrfName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDrfProductDesc.add(txtDrfName, gridBagConstraints);

        lblPaymentHeadName.setText("Payment Head ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDrfProductDesc.add(lblPaymentHeadName, gridBagConstraints);

        txtPaymentHeadName.setMaxLength(128);
        txtPaymentHeadName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDrfProductDesc.add(txtPaymentHeadName, gridBagConstraints);

        btnPaymentHeadName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPaymentHeadName.setEnabled(false);
        btnPaymentHeadName.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPaymentHeadName.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPaymentHeadName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaymentHeadNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDrfProductDesc.add(btnPaymentHeadName, gridBagConstraints);

        lblPaymentHeadType.setText("(Expenditure GL)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDrfProductDesc.add(lblPaymentHeadType, gridBagConstraints);

        lblAccountHeadType.setText("(Liability GL)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDrfProductDesc.add(lblAccountHeadType, gridBagConstraints);

        txtRecoveryHead.setMaxLength(128);
        txtRecoveryHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDrfProductDesc.add(txtRecoveryHead, gridBagConstraints);

        lblRecoveryHead.setText("Recovery  Head ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDrfProductDesc.add(lblRecoveryHead, gridBagConstraints);

        btnRecoveryHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnRecoveryHead.setEnabled(false);
        btnRecoveryHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnRecoveryHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnRecoveryHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecoveryHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDrfProductDesc.add(btnRecoveryHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 190;
        gridBagConstraints.ipady = 77;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathReliefFund.add(panDrfProductDesc, gridBagConstraints);

        panDelinked.setBorder(javax.swing.BorderFactory.createTitledBorder("Nominee Details"));
        panDelinked.setMinimumSize(new java.awt.Dimension(390, 60));
        panDelinked.setPreferredSize(new java.awt.Dimension(390, 60));
        panDelinked.setLayout(new java.awt.GridBagLayout());

        chkNominee.setText("Nominee Screen Required For DRF");
        chkNominee.setMaximumSize(new java.awt.Dimension(190, 20));
        chkNominee.setMinimumSize(new java.awt.Dimension(235, 20));
        chkNominee.setPreferredSize(new java.awt.Dimension(235, 20));
        chkNominee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNomineeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDelinked.add(chkNominee, gridBagConstraints);

        lblNoNominee.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNoNominee.setText("Number of Nominees");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDelinked.add(lblNoNominee, gridBagConstraints);

        txtNoNominees.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDelinked.add(txtNoNominees, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathReliefFund.add(panDelinked, gridBagConstraints);

        jTabbedPane1.addTab("Death Relief Fund Master", panDeathReliefFund);

        panInterestPayable.setLayout(new java.awt.GridBagLayout());

        panRecordDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Interest Record Details"));
        panRecordDetails.setLayout(new java.awt.GridBagLayout());

        lblCalculationFrequency.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblCalculationFrequency.setText("Interest Calculation Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panRecordDetails.add(lblCalculationFrequency, gridBagConstraints);

        lblCalclulationCriteria.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblCalclulationCriteria.setText("Calculation Criteria");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panRecordDetails.add(lblCalclulationCriteria, gridBagConstraints);

        lblProductFrequency.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblProductFrequency.setText("Product Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panRecordDetails.add(lblProductFrequency, gridBagConstraints);

        lblDebitHead.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblDebitHead.setText("Interest Debit A/C Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panRecordDetails.add(lblDebitHead, gridBagConstraints);

        lblCalculatedDt.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblCalculatedDt.setText("Last Interest Calculated Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panRecordDetails.add(lblCalculatedDt, gridBagConstraints);

        cboCalculationFrequency.setMaximumSize(new java.awt.Dimension(101, 21));
        cboCalculationFrequency.setMinimumSize(new java.awt.Dimension(101, 21));
        cboCalculationFrequency.setPreferredSize(new java.awt.Dimension(101, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panRecordDetails.add(cboCalculationFrequency, gridBagConstraints);

        cboCalclulationCriteria.setMaximumSize(new java.awt.Dimension(101, 21));
        cboCalclulationCriteria.setMinimumSize(new java.awt.Dimension(101, 21));
        cboCalclulationCriteria.setPreferredSize(new java.awt.Dimension(101, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panRecordDetails.add(cboCalclulationCriteria, gridBagConstraints);

        cboProductFrequency.setMaximumSize(new java.awt.Dimension(101, 21));
        cboProductFrequency.setMinimumSize(new java.awt.Dimension(101, 21));
        cboProductFrequency.setPreferredSize(new java.awt.Dimension(101, 21));
        cboProductFrequency.setVerifyInputWhenFocusTarget(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panRecordDetails.add(cboProductFrequency, gridBagConstraints);

        btnDebitHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDebitHead.setEnabled(false);
        btnDebitHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDebitHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDebitHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDebitHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panRecordDetails.add(btnDebitHead, gridBagConstraints);

        tdtCalculatedDt.setMaximumSize(new java.awt.Dimension(101, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panRecordDetails.add(tdtCalculatedDt, gridBagConstraints);

        txtDebitHead.setMaximumSize(new java.awt.Dimension(4, 20));
        txtDebitHead.setMinimumSize(new java.awt.Dimension(4, 20));
        txtDebitHead.setPreferredSize(new java.awt.Dimension(4, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 88;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panRecordDetails.add(txtDebitHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.ipady = 151;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panInterestPayable.add(panRecordDetails, gridBagConstraints);

        panTableDt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panTableDt.setLayout(new java.awt.GridBagLayout());

        lblFromDt.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblFromDt.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTableDt.add(lblFromDt, gridBagConstraints);

        lblToDt.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblToDt.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTableDt.add(lblToDt, gridBagConstraints);

        lblInterestRate.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblInterestRate.setText("Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTableDt.add(lblInterestRate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTableDt.add(tdtFromDt, gridBagConstraints);

        txtToDt.setMaximumSize(new java.awt.Dimension(4, 20));
        txtToDt.setMinimumSize(new java.awt.Dimension(4, 20));
        txtToDt.setPreferredSize(new java.awt.Dimension(4, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 106;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTableDt.add(txtToDt, gridBagConstraints);

        txtInterestRate.setMaximumSize(new java.awt.Dimension(4, 20));
        txtInterestRate.setMinimumSize(new java.awt.Dimension(4, 20));
        txtInterestRate.setPreferredSize(new java.awt.Dimension(4, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 106;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTableDt.add(txtInterestRate, gridBagConstraints);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblInterest.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tblInterest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", "", null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "From Date", "To Date", "Interest Rate"
            }
        ));
        tblInterest.setMinimumSize(new java.awt.Dimension(200, 150));
        tblInterest.setPreferredSize(new java.awt.Dimension(450, 200));
        tblInterest.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblInterestMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblInterest);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 350, 170));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 13;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTableDt.add(jPanel3, gridBagConstraints);

        btnTabNew.setText("New");
        btnTabNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.ipadx = 35;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTableDt.add(btnTabNew, gridBagConstraints);

        btnTabSave.setText("Save");
        btnTabSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.ipadx = 32;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTableDt.add(btnTabSave, gridBagConstraints);

        btnTabDelete.setText("Delete");
        btnTabDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.ipadx = 19;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTableDt.add(btnTabDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 42;
        gridBagConstraints.ipady = 114;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panInterestPayable.add(panTableDt, gridBagConstraints);

        jTabbedPane1.addTab("Death Relief Fund Interest Settings", panInterestPayable);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.WEST);
        jTabbedPane1.getAccessibleContext().setAccessibleName("Drf Master");

        mnuProcess.setText("Process");
        mnuProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProcessActionPerformed(evt);
            }
        });

        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);
        mnuProcess.add(sptEdit);

        mitDelete.setText("Delete");
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void tblInterestMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInterestMousePressed
 ClientUtil.enableDisable(panTableDt,true);
 //tabEdit=true;
 if(tblInterest.getSelectedRow()==-1)
 {
     return;
     
 }
 else
 { int row=tblInterest.getSelectedRow();
   
//////   String dt;
//////   Date date=null;
//////   DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
//////   try {
//////       date = (Date)formatter.parse(tblInterest.getValueAt(row,0).toString());
////// } catch (Exception e) {
//////     e.printStackTrace();
//////   }
//////System.out.println(date); 
//////
//////Calendar cal = Calendar.getInstance();
//////cal.setTime(date);
//////String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" +         cal.get(Calendar.YEAR);
//////System.out.println("formatedDate : " + formatedDate);  
//////tdtFromDt.setDateValue(formatedDate);
////////tdtDrfFromDt.set
////////tdtDrfFromDt.setDateValue(formatedDate);
//////
////// try {
//////       date = (Date)formatter.parse(tblInterest.getValueAt(row,1).toString());
////// } catch (Exception e) {
//////     e.printStackTrace();
//////   }
//////System.out.println(date); 
//////
////// cal = Calendar.getInstance();
//////cal.setTime(date);
////// formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" +         cal.get(Calendar.YEAR);
//////System.out.println("formatedDate : " + formatedDate); 
//////txtToDt.setText(formatedDate);
////// 
//////           txtInterestRate.setText(tblInterest.getValueAt(row, 2).toString());
//////         //  tblInterest.r
//////           
   tdtFromDt.setDateValue(tblInterest.getValueAt(row,0).toString());
   txtToDt.setText(tblInterest.getValueAt(row,1).toString());
   txtToDt.setEnabled(false);
   txtInterestRate.setText(tblInterest.getValueAt(row,2).toString());
   tabrow=row;
   tabedit=true;
        //   bufferList.remove(row);
   btnTabSave.setEnabled(true);
   btnTabDelete.setEnabled(true);
           
           
 }
 
    }//GEN-LAST:event_tblInterestMousePressed

    private void btnTabDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabDeleteActionPerformed
      if(tblInterest.getSelectedRow()==-1)
      {
          JOptionPane.showMessageDialog(this, "Select a row to Delete", "Error", JOptionPane.ERROR_MESSAGE);
           //f=false;
          // tabedit=false;
            return ; 
       }
      else
      {
          int row=tblInterest.getSelectedRow();
          System.out.println("ROWWWWW>>>"+row+">>>>>>>>>>"+bufferList.size());
           DeathReliefMasterOB drmOB=new DeathReliefMasterOB();
          // drmOB=(DeathReliefMasterOB)buffer1(row);
//           tdtDrfFromDt.setDateValue(tblInterest.getValueAt(row,0).toString());
//           txtToDt.setText(tblInterest.getValueAt(row, 1).toString());
//           txtInterestRate.setText(tblInterest.getValueAt(row, 2).toString()); 
           tdtFromDt.setDateValue("");
                txtToDt.setText(" ");
                txtInterestRate.setText("");
          bufferList.remove(row); 
          getTableData1();
      }
      btnTabSave.setEnabled(false);
      btnTabDelete.setEnabled(false);
      tdtFromDt.setEnabled(false);
      txtInterestRate.setEnabled(false);
    }//GEN-LAST:event_btnTabDeleteActionPerformed

    private void btnTabSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabSaveActionPerformed

         try{
            btnTabDelete.setEnabled(true);
            
            updateOBFields();
            
            //            mandatoryMessage = objMandatoryRB.checkMandatory(CLASSNAME,panDeathReliefFund);
            String mandatoryMessage;
            mandatoryMessage = checkMandatory(this.panTableDt);
                       
            if(mandatoryMessage.length() > 0 ){
                displayAlert(mandatoryMessage);
                
            }else{
                 DeathReliefMasterOB drmOB=new DeathReliefMasterOB();
                String addDts[]=new String[7];

                int bufferSize=bufferList.size();
                if(bufferSize>0&&(!tabedit))
                {
                HashMap h=new HashMap();
        Calendar cal=null;  
        h=(HashMap)bufferList.get(bufferSize-1);
        String s=h.get("FROM").toString();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); 
       Date previous=null;
        try {
       previous=(Date)formatter.parse(s);
       } catch (Exception e) {
        e.printStackTrace();
         }
        System.out.println(previous); 
        
        
        s=tdtFromDt.getDateValue();
         Date current=null;
          try {
       current=(Date)formatter.parse(s);
       } catch (Exception e) {
        e.printStackTrace();
         }
         
     if(current.before(previous)&&(! tabedit)) 
     {
         JOptionPane.showMessageDialog(this, "From Date must be a date after previous From Date ", "Error", JOptionPane.ERROR_MESSAGE);
                     return ;
     }
        
 
                }
                System.out.println("tabedit     "+tabedit);
                if(tabedit)
                {
                     HashMap h=new HashMap();
        Calendar cal=null;  
                    System.out.println("tabrowtabrowtabrow"+tabrow);
                    System.out.println("bufferListbufferList"+bufferList);
                    h=(HashMap)bufferList.get(tabrow);
       // h=(HashMap)bufferList.get(tabrow-1);
        String s=h.get("FROM").toString();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); 
       Date previous=null;
        try {
       previous=(Date)formatter.parse(s);
       } catch (Exception e) {
        e.printStackTrace();
         }
        System.out.println("fff"+previous); 
        
        
        s=tdtFromDt.getDateValue();
         Date current=null;
          try {
       current=(Date)formatter.parse(s);
       } catch (Exception e) {
        e.printStackTrace();
         }
         
     if(current.before(previous)) 
     {
         JOptionPane.showMessageDialog(this, "From Date must be a date after previous From Date ", "Error", JOptionPane.ERROR_MESSAGE);
           //f=false;
          // tabedit=false;
            return ;
     }
     else
     { cal = Calendar.getInstance();
        cal.setTime(current);
        //intdd=cal.get(Calendar.DAY_OF_MONTH);
        System.out.println("Callll1"+cal.DATE);
          cal.add(Calendar.DAY_OF_MONTH, -1);
        System.out.println("Callll"+cal.DATE);
        String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" +         cal.get(Calendar.YEAR);
        
   // h.put("FROM",formatedDate);  
        
       
        h.put("TO",formatedDate);
        //bufferList.set(tabrow-1,h);
        bufferList.set(tabrow,h);
       
     }
         
                }
                
                HashMap h=new HashMap();
                h.put("FROM",tdtFromDt.getDateValue());
                if(txtToDt.getText().equals(""))
                {
                     h.put("TO","-");
                }
                else{
                h.put("TO",txtToDt.getText());
                }
                h.put("INTEREST",txtInterestRate.getText());
               if(tabedit)
               {
                bufferList.set(tabrow,h);
                tabedit=false;
                tabrow=-1;
               }
               else
               {
                bufferList.add(h);
               }
                getTableData1();
                tdtFromDt.setDateValue("");
               // txtToDt.setText("");
                txtToDt.setText("");
                txtToDt.setEditable(false);
                txtInterestRate.setText("");
               
            }
            btnTabSave.setEnabled(false);
            btnTabDelete.setEnabled(false);
            tdtFromDt.setEnabled(false);
      txtInterestRate.setEnabled(false);
         }
         catch(Exception e){
            e.printStackTrace();
        }

    }//GEN-LAST:event_btnTabSaveActionPerformed

    private void btnTabNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabNewActionPerformed
       ClientUtil.enableDisable(panTableDt,true);
       txtToDt.setText("");
       txtToDt.setEditable(false);
       txtToDt.setEnabled(false);
       btnTabSave.setEnabled(true);
       btnTabDelete.setEnabled(true);
       tdtFromDt.setEnabled(true);
       txtInterestRate.setEditable(true);
       tabedit=false;
    }//GEN-LAST:event_btnTabNewActionPerformed

    private void btnDebitHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDebitHeadActionPerformed
        popUp("EXPENDITUREGL2");
    }//GEN-LAST:event_btnDebitHeadActionPerformed

    private void btnRecoveryHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecoveryHeadActionPerformed
         popUp("EXPENDITUREGL1");
    }//GEN-LAST:event_btnRecoveryHeadActionPerformed

    private void rdAmountRecoveryNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdAmountRecoveryNoActionPerformed
        if(rdAmountRecoveryNo.isSelected())
       {
        txtRecoverAmount.setEnabled(false);
       }
    }//GEN-LAST:event_rdAmountRecoveryNoActionPerformed

    private void rdAmountRecoveryYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdAmountRecoveryYesActionPerformed
       if(rdAmountRecoveryYes.isSelected())
       {
        txtRecoverAmount.setEnabled(true);
       }
    }//GEN-LAST:event_rdAmountRecoveryYesActionPerformed
    
    private void btnPaymentHeadNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentHeadNameActionPerformed
        // TODO add your handling code here:
        popUp("EXPENDITUREGL");
    }//GEN-LAST:event_btnPaymentHeadNameActionPerformed
    
    private void btnActHeadNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActHeadNameActionPerformed
        // TODO add your handling code here:
        popUp("LIABILITYGL");
    }//GEN-LAST:event_btnActHeadNameActionPerformed
    
    private void tblDrfProdDescMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDrfProdDescMousePressed
        drmMasterFlag = true;
        updateOBFields();
        observable.setNewDrfMaster(false);
        final String drfSlNo = (String) tblDrfProdDesc.getValueAt(tblDrfProdDesc.getSelectedRow(),0);
        observable.populateDrfMaster(tblDrfProdDesc.getSelectedRow());
        //To enable contact buttons for NEW & EDIT
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(panDeathReliefFund,true);
            
            if(tblDrfProdDesc.getValueAt(0,1).equals("NO"))
            {
                txtRecoverAmount.setEnabled(false);
            }
            btnDrfDetailsDelete.setEnabled(true);
            btnDrfDetailsNew.setEnabled(false);
            btnDrfDetailsSave.setEnabled(true);
        }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || actionType.equals("DeletedDetails")){
            //            objEmployeeMasterUISupport.setContactButtonEnableDisableDefault(false,btnContactNew, btnContactDelete, btnContactAdd);
            //            objEmployeeMasterUISupport.setContactAddEnableDisable(false,btnContactAdd);
        }
        
    }//GEN-LAST:event_tblDrfProdDescMousePressed
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        //        _observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        //        _observable.setStatus();
        //        lblStatus.setText(_observable.getLblStatus());
        //        popUp();
        //        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled){
            observable.setResult(observable.getActionType());
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            if(authorizeStatus.equalsIgnoreCase("REJECTED") && flag == true) {
                singleAuthorizeMap.put("DELETESTATUS", "MODIFIED");
                singleAuthorizeMap.put(CommonConstants.STATUS, "REJECTED");
                singleAuthorizeMap.put("DELETEREMARKS", "");
                singleAuthorizeMap.put("STATUSCHECK", "");
            }
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, curDate.clone());
            singleAuthorizeMap.put("PROD_ID", observable.getTxtProductId());
            singleAuthorizeMap.put("EFFECTIVEDATE",curDate);
            singleAuthorizeMap.put("CREATED_DATE", curDate);
            singleAuthorizeMap.put("DRF_MASTER", "DRF_MASTER");
            
            System.out.println("!@#$@#$@#$singleAuthorizeMap:"+singleAuthorizeMap);
            observable.authorizeDrfMaster(singleAuthorizeMap);
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(this.txtProductId.getText());
            btnCancelActionPerformed(null);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
            viewType = 0;
        } else{
            viewType = AUTHORIZE;
            final HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getDrfMasterAuthorizeMode");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeDrfMaster");
            isFilled = false;
            setModified(true);
            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            lblStatus.setText(observable.getLblStatus());
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        }
    }
    
    private void btnDrfDetailsDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDrfDetailsDeleteActionPerformed
        // Add your handling code here:
        try{
            String alertMsg = "deleteWarningMsg";
            int optionSelected = observable.showAlertWindow(alertMsg);
            if(optionSelected==0){
                System.out.println("IN DELETE UI");
                observable.deleteDrfMaster(tblDrfProdDesc.getSelectedRow());
                ClientUtil.clearAll(panDrfDetails);
                ClientUtil.clearAll(tblDrfProdDesc);
                ClientUtil.enableDisable(panDrfDetails,false);
                btnDrfDetailsNew.setEnabled(true);
                btnDrfDetailsSave.setEnabled(false);
                btnDrfDetailsDelete.setEnabled(false);
                drmMasterFlag = false;
                updateOBFields();
            }else{
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_btnDrfDetailsDeleteActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        viewType = ClientConstants.ACTIONTYPE_DELETE;
        observable.setStatus();
        popUp(ClientConstants.ACTION_STATUS[3]);
        ClientUtil.enableDisable(this, false);
        lblStatus.setText(observable.getLblStatus());
        
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnDrfDetailsNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDrfDetailsNewActionPerformed
        // Add your handling code here:
        btnRecoveryHead.setEnabled(true);
        rdAmountRecoveryNo.setSelected(true);
        txtRecoverAmount.setEnabled(false);
        btnTabNew.setEnabled(true);
        btnDebitHead.setEnabled(true);
        
        updateOBFields();
        observable.setNewDrfMaster(true);
        btnActHeadName.setEnabled(true);
        btnPaymentHeadName.setEnabled(true);
        if(tblDrfProdDesc.getRowCount() > 0){
            ClientUtil.enableDisable(panDrfProductDesc,false);
            ClientUtil.enableDisable(panDrfDetails,true);
        }else{
            ClientUtil.enableDisable(panDrfProductDesc,true);
            ClientUtil.enableDisable(panDrfDetails,true);
        }
        
         ClientUtil.enableDisable(panRecordDetails,true);
         //ClientUtil.enableDisable(panTableDt,true);
         rdAmountRecoveryNo.setSelected(true);
        txtRecoverAmount.setEnabled(false);
        observable.resetDrfDetails();
        observable.ttNotifyObservers();
        btnDrfDetailsSave.setEnabled(true);
        btnDrfDetailsDelete.setEnabled(false);
        btnDrfDetailsNew.setEnabled(false);
        Calendar cal=null;
        cal = Calendar.getInstance();
        cal.setTime(curDate);
        //intdd=cal.get(Calendar.DAY_OF_MONTH);
        System.out.println("Callll1"+cal.DATE);
       // cal.add(Calendar.DAY_OF_MONTH, -1);
        System.out.println("Callll"+cal.DATE);
        String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" +         cal.get(Calendar.YEAR);
        
   // h.put("FROM",formatedDate);  
        
        
//        tdtCalculatedDt.setDateValue(formatedDate);
//        tdtCalculatedDt.setEnabled(false);
    }//GEN-LAST:event_btnDrfDetailsNewActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        viewType = ClientConstants.ACTIONTYPE_EDIT;
        observable.setStatus();
        popUp(ClientConstants.ACTION_STATUS[2]);
        observable.setStatus();
        btnEdit.setEnabled(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(this,false);
        btnCancel.setEnabled(true);
        btnSave.setEnabled(true);
        lblStatus.setText(observable.getLblStatus());
        ClientUtil.enableDisable(panRecordDetails,true);
        btnDebitHead.setEnabled(true);
        chkNominee.setEnabled(true);//Added by Revathi.L
    }//GEN-LAST:event_btnEditActionPerformed
    private void popUp(String actionType){
        this.actionType = actionType;
        if(actionType != null){
            final HashMap viewMap = new HashMap();
            HashMap wheres = new HashMap();
            
            if(actionType.equals(ClientConstants. ACTION_STATUS[2]))  {
                //                for Edit
                ArrayList lst = new ArrayList();
                lst.add("DRF NO");
                viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                viewMap.put(CommonConstants.MAP_NAME, "getSelectDrfMasterTOList");
                viewMap.put(CommonConstants.MAP_WHERE, wheres);
            } else if (actionType.equals(ClientConstants. ACTION_STATUS[3])){
                viewMap.put(CommonConstants.MAP_NAME, "getSelectDrfMasterTOList");
                viewMap.put(CommonConstants.MAP_WHERE, wheres);
            }
            else if(actionType.equals("ViewDetails")){
                HashMap where = new HashMap();
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "getSelectDrfMasterTOList");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }
            else if(actionType.equals("LIABILITYGL")){
                viewType = LIABILITYGL;
                HashMap where = new HashMap();
                where.put(ACCT_TYPE, AcctStatusConstants.LIABILITY);
                //                whereMap.put(ACCT_TYPE, AcctStatusConstants.AST_LIAB);
                where.put(BALANCETYPE, CommonConstants.CREDIT);
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "product.deposits.getAcctHeadList");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }
            else if(actionType.equals("EXPENDITUREGL")){
                viewType = EXPENDITUREGL;
                HashMap where = new HashMap();
                where.put(BALANCETYPE, CommonConstants.DEBIT);
                where.put(ACCT_TYPE, AcctStatusConstants.EXPENDITURE);
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "product.deposits.getAcctHeadList");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }
            else if(actionType.equals("EXPENDITUREGL1")){
                viewType = EXPENDITUREGL1;
                HashMap where = new HashMap();
                where.put(BALANCETYPE, CommonConstants.DEBIT);
                where.put(ACCT_TYPE, AcctStatusConstants.EXPENDITURE);
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "product.deposits.getAcctHeadList");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }
            else if(actionType.equals("EXPENDITUREGL2")){
                viewType = EXPENDITUREGL2;
                HashMap where = new HashMap();
                where.put(BALANCETYPE, CommonConstants.DEBIT);
                where.put(ACCT_TYPE, AcctStatusConstants.EXPENDITURE);
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "product.deposits.getAcctHeadList");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }
            new com.see.truetransact.ui.common.viewall.ViewAll(this, viewMap,true).show();
        }
        
    }
    
    /** To get data based on customer id received from the popup and populate into the
     * screen
     */
    public void fillData(Object obj) {
        isFilled = true;
        setModified(true);
        final HashMap hash = (HashMap) obj;
        System.out.println("@@@@hash"+hash);
        String st = CommonUtil.convertObjToStr(hash.get("STATUS"));
        if(st.equalsIgnoreCase("DELETED")) {
            flag = true;
        }
        if(actionType.equals(ClientConstants.ACTION_STATUS[2]) ||
        actionType.equals(ClientConstants.ACTION_STATUS[3])|| actionType.equals("DeletedDetails") || actionType.equals("ViewDetails")|| viewType == AUTHORIZE ||
        getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
            HashMap map = new HashMap();
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                map.put("PROD_ID",hash.get("PROD_ID"));
                map.put(CommonConstants.MAP_WHERE, hash.get("PROD_ID"));
            }else{
                map.put("PROD_ID",hash.get("PROD_ID"));
                map.put(CommonConstants.MAP_WHERE, hash.get("PROD_ID"));
            }
            observable.getData(map);
            setButtonEnableDisable();
            //            ClientUtil.enableDisable(panMISKYC,false);
            //For EDIT option enable disable fields and controls appropriately
            if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                chkNominee.setEnabled(true);
            }
            observable.setStatus();
            if(getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE){
                
                btnAuthorize.setVisible(false);
                btnReject.setVisible(false);
                btnException.setVisible(false);
            }
            
            if(viewType==AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                
            }
        }
        if(viewType == AUTHORIZE){
            ClientUtil.enableDisable(panDeathReliefFund, false);
        }
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW_MODE){
            ClientUtil.enableDisable(this, false);
        }
        HashMap hashMap=(HashMap)obj;
        System.out.println("### fillData Hash : "+hashMap);
        if(viewType == EXPENDITUREGL){
            txtPaymentHeadName.setText(CommonUtil.convertObjToStr(hashMap.get("ACCOUNT HEAD")));
        }
         if(viewType == EXPENDITUREGL1){
            
            txtRecoveryHead.setText(CommonUtil.convertObjToStr(hashMap.get("ACCOUNT HEAD")));
            
        }
         if(viewType == EXPENDITUREGL2){
            
            txtDebitHead.setText(CommonUtil.convertObjToStr(hashMap.get("ACCOUNT HEAD")));
            
        }
        if(viewType == LIABILITYGL){
            txtActHeadName.setText(CommonUtil.convertObjToStr(hashMap.get("ACCOUNT HEAD")));
        }
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            
        }
    }
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        //        if(observable.getAuthorizeStatus()!=null)
        //            setModified(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        viewType = 0;
        ClientUtil.enableDisable(this,false,false,true);
        HashMap map= new HashMap();
        map.put("SCREEN_ID",getScreen());
        map.put("RECORD_KEY", this.txtProductId.getText());
        map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        map.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        map.put("CUR_DATE", curDate.clone());
        System.out.println("Record Key Map : " + map);
        ClientUtil.execute("deleteEditLock", map);
        setButtonEnableDisable();
        observable.setStatus();
        ClientUtil.clearAll(this);
        if(getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE){
            cifClosingAlert();
        }
        setModified(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnNew.setEnabled(true);
        btnView.setEnabled(true);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(true);
        btnEdit.setEnabled(true);
        lblStatus.setText("            ");
        observable.resetDrfMaster();
        tdtDrfFromDt.setDateValue("");
        tdtDrfToDt.setDateValue("");
        observable.resetDrfMaster();
        observable.resetDrfListTable();
        //Added By Revathi.L
        chkNominee.setSelected(false);
        chkNominee.setEnabled(false);
        txtNoNominees.setText("");
         bufferList.clear();
         getTableData1();
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        updateOBFields();
       // for(int g=0;g<buffer1.size())
        //        mandatoryMessage = objEmployeeMasterUISupport.checkMandatory(CLASSNAME,panAdditionalInfo);
        //        mandatoryMessage += objEmployeeMasterUISupport.checkMandatory(CLASSNAME,panOfficeDetails);
      //  final String mandatoryMessage = checkMandatory(panRecordDetails);
        StringBuffer message = new StringBuffer("");
        if(txtProductId.getText().equals(""))
        { message.append(objMandatoryRB.getString("txtProductId"));
           message.append("\n"); 
        }
         if(txtDrfName.getText().equals(""))
        { message.append(objMandatoryRB.getString("txtDrfName"));
            message.append("\n");
        }
        
         if(txtActHeadName.getText().equals(""))
        { message.append(objMandatoryRB.getString("txtActHeadName"));
            message.append("\n");
        }
         if(txtPaymentHeadName.getText().equals(""))
        { message.append(objMandatoryRB.getString("txtPaymentHeadName"));
            message.append("\n");
        }
         if(txtRecoveryHead.getText().equals(""))
        { message.append(objMandatoryRB.getString("txtRecoveryHead"));
            message.append("\n");
        }
//       if(saveflag==false)
//       {
//           if(rdAmountRecoveryYes.isSelected())
//        {
//         if(txtRecoverAmount.getText().equals(""))
//        { message.append(objMandatoryRB.getString("lblRecoverAmount"));
//            message.append("\n");
//        }
//        }
//           
//           
//        if(tdtDrfFromDt.getDateValue().equals(""))
//        { message.append(objMandatoryRB.getString("tdtDrfFromDt"));
//            message.append("\n");
//        }
//           if(tdtDrfToDt.getDateValue().equals(""))
//        { message.append(objMandatoryRB.getString("tdtDrfToDt"));
//            message.append("\n");
//        }
//        if(txtDrfAmount.getText().equals(""))
//        { message.append(objMandatoryRB.getString("txtDrfAmount"));
//            message.append("\n");
//        }
//        if(txtPaymentAmount.getText().equals(""))
//        { message.append(objMandatoryRB.getString("txtPaymentAmount"));
//            message.append("\n");
//        }
//       }
        if(cboCalculationFrequency.getSelectedIndex()==0)
        {
                message.append(objMandatoryRB.getString("cboCalculationFrequency"));
                message.append("\n");
         }
         if(cboCalclulationCriteria.getSelectedIndex()==0)
        {
            message.append(objMandatoryRB.getString("cboCalclulationCriteria"));
            message.append("\n");
        }
         
         if(cboProductFrequency.getSelectedIndex()==0)
        {
                message.append(objMandatoryRB.getString("cboProductFrequency"));
                message.append("\n");
         }
        
         if(txtDebitHead.getText().equals(""))
        {
                message.append(objMandatoryRB.getString("txtDebitHead"));
                message.append("\n");
         }
         if(tdtCalculatedDt.getDateValue().equals(""))
        {
                message.append(objMandatoryRB.getString("tdtCalculatedDt"));
                message.append("\n");
         }
          if(bufferList.isEmpty())
        {
                message.append("Enter Interest Rate Deatils");
                message.append("\n");
         }
       // String msg=message.getText();
        if( message.length() > 0 ){
            displayAlert(message.toString()); 
            //            objEmployeeMasterUISupport.displayAlert(mandatoryMessage);
        }else{
            observable.doAction();
            System.out.println("DDDUI1");
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                btnCancelActionPerformed(null);
                observable.resetDrfMaster();
                btnCancel.setEnabled(true);
                //                lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
                observable.setStatus();
                observable.setResultStatus();
                lblStatus.setText(observable.getLblStatus());
            }
          //   bufferList.clear();
             getTableData1();
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
            tabEdit=false;
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
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
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        viewType = ClientConstants.ACTIONTYPE_NEW;
        observable.setStatus();
        setButtonEnableDisable();
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(this,false);
        chkNominee.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        lblStatus.setText(observable.getLblStatus());
     //   bufferList.clear();
        getTableData1();
        clearButton();
        
    }//GEN-LAST:event_btnNewActionPerformed
    public void clearButton() {
        //this function is used to disable buttons which are not required during the insert mode and to clear them during cancel
        
        btnDrfDetailsNew.setEnabled(true);
        btnDrfDetailsDelete.setEnabled(false);
        btnDrfDetailsSave.setEnabled(false);
        tdtDrfFromDt.setEnabled(false);
        tdtDrfToDt.setEnabled(false);
        btnTabNew.setEnabled(false);
        btnTabSave.setEnabled(false);
        btnTabDelete.setEnabled(false);
    }
    private void btnDrfDetailsSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDrfDetailsSaveActionPerformed
        try{
            saveflag=true;
            updateOBFields();
            
            //            mandatoryMessage = objMandatoryRB.checkMandatory(CLASSNAME,panDeathReliefFund);
            String mandatoryMessage;
            mandatoryMessage = checkMandatory(this.panDeathReliefFund);
            //To check whether all the mandatory fields of Contact address have been entered.
            //If not entered properly display alert message, else proceed
            if(rdAmountRecoveryYes.isSelected())
        {
         if(txtRecoverAmount.getText().equals(""))
        { mandatoryMessage=mandatoryMessage+(objMandatoryRB.getString("lblRecoverAmount"));
           // message.append("\n");
        }
        }
            if(mandatoryMessage.length() > 0 ){
                displayAlert(mandatoryMessage);
                
            }else{
                if(drmMasterFlag == false){
                    //if the row is empty
                    observable.drfMaster(-1,drmMasterFlag);
                }else{
                    observable.drfMaster(tblDrfProdDesc.getSelectedRow(),drmMasterFlag);
                }
                ClientUtil.clearAll(panDrfDetails);
                ClientUtil.enableDisable(panDeathReliefFund,false);
                btnDrfDetailsNew.setEnabled(true);
                btnDrfDetailsSave.setEnabled(false);
                btnDrfDetailsDelete.setEnabled(false);
                updateOBFields();
                drmMasterFlag = false;
                txtRecoverAmount.setEnabled(false);
                chkNominee.setEnabled(true);
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDrfDetailsSaveActionPerformed
    private String checkMandatory(JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(),component);
    }
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    private void mnuProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcessActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_mnuProcessActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void chkNomineeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNomineeActionPerformed
        //Added By Revathi.L
        if (chkNominee.isSelected() == true) {
//            lblNoNominee.setVisible(true);
//            txtNoNominees.setVisible(true);
            //txtNoNominees.setEnabled(true);
        } else {
            lblNoNominee.setVisible(false);
            txtNoNominees.setVisible(false);
            txtNoNominees.setText("");
        }
    }//GEN-LAST:event_chkNomineeActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnActHeadName;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDebitHead;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDrfDetailsDelete;
    private com.see.truetransact.uicomponent.CButton btnDrfDetailsNew;
    private com.see.truetransact.uicomponent.CButton btnDrfDetailsSave;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPaymentHeadName;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnRecoveryHead;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private javax.swing.JButton btnTabDelete;
    private javax.swing.JButton btnTabNew;
    private javax.swing.JButton btnTabSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButtonGroup cButtonGroup1;
    private javax.swing.JComboBox cboCalclulationCriteria;
    private javax.swing.JComboBox cboCalculationFrequency;
    private javax.swing.JComboBox cboProductFrequency;
    private com.see.truetransact.uicomponent.CCheckBox chkNominee;
    private com.see.truetransact.uicomponent.CDialog dlgMajorHead;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadType;
    private com.see.truetransact.uicomponent.CLabel lblActHeadName;
    private com.see.truetransact.uicomponent.CLabel lblAmountRecovery;
    private javax.swing.JLabel lblCalclulationCriteria;
    private javax.swing.JLabel lblCalculatedDt;
    private javax.swing.JLabel lblCalculationFrequency;
    private javax.swing.JLabel lblDebitHead;
    private com.see.truetransact.uicomponent.CLabel lblDrfAmount;
    private com.see.truetransact.uicomponent.CLabel lblDrfFromDt;
    private com.see.truetransact.uicomponent.CLabel lblDrfName;
    private com.see.truetransact.uicomponent.CLabel lblDrfToDt;
    private javax.swing.JLabel lblFromDt;
    private javax.swing.JLabel lblInterestRate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNoNominee;
    private com.see.truetransact.uicomponent.CLabel lblPaymentAmount;
    private com.see.truetransact.uicomponent.CLabel lblPaymentHeadName;
    private com.see.truetransact.uicomponent.CLabel lblPaymentHeadType;
    private javax.swing.JLabel lblProductFrequency;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblRecoverAmount;
    private com.see.truetransact.uicomponent.CLabel lblRecoveryHead;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace52;
    private com.see.truetransact.uicomponent.CLabel lblSpace53;
    private com.see.truetransact.uicomponent.CLabel lblSpace54;
    private com.see.truetransact.uicomponent.CLabel lblSpace55;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private javax.swing.JLabel lblToDt;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panDeathReliefFund;
    private com.see.truetransact.uicomponent.CPanel panDelinked;
    private com.see.truetransact.uicomponent.CPanel panDrfDetails;
    private com.see.truetransact.uicomponent.CPanel panDrfProductDesc;
    private com.see.truetransact.uicomponent.CPanel panExistingCustomer;
    private javax.swing.JPanel panInterestPayable;
    private com.see.truetransact.uicomponent.CPanel panMajorHeadCode;
    private javax.swing.JPanel panRecordDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private javax.swing.JPanel panTableDt;
    private com.see.truetransact.uicomponent.CRadioButton rdAmountRecoveryNo;
    private com.see.truetransact.uicomponent.CRadioButton rdAmountRecoveryYes;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptEdit;
    private com.see.truetransact.uicomponent.CScrollPane srpDrfProdDesc;
    private com.see.truetransact.uicomponent.CScrollPane srpMajorHead;
    private com.see.truetransact.uicomponent.CTable tblDrfProdDesc;
    private javax.swing.JTable tblInterest;
    private com.see.truetransact.uicomponent.CTable tblMajorHead;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CDateField tdtCalculatedDt;
    private com.see.truetransact.uicomponent.CDateField tdtDrfFromDt;
    private com.see.truetransact.uicomponent.CDateField tdtDrfToDt;
    private com.see.truetransact.uicomponent.CDateField tdtFromDt;
    private com.see.truetransact.uicomponent.CTextField txtActHeadName;
    private javax.swing.JTextField txtDebitHead;
    private com.see.truetransact.uicomponent.CTextField txtDrfAmount;
    private com.see.truetransact.uicomponent.CTextField txtDrfName;
    private javax.swing.JTextField txtInterestRate;
    private com.see.truetransact.uicomponent.CTextField txtNoNominees;
    private com.see.truetransact.uicomponent.CTextField txtPaymentAmount;
    private com.see.truetransact.uicomponent.CTextField txtPaymentHeadName;
    private com.see.truetransact.uicomponent.CTextField txtProductId;
    private com.see.truetransact.uicomponent.CTextField txtRecoverAmount;
    private com.see.truetransact.uicomponent.CTextField txtRecoveryHead;
    private javax.swing.JTextField txtToDt;
    // End of variables declaration//GEN-END:variables
    





//class CCATABLE extends AbstractTableModel
//    { 
//       private String colnames[]={"From Date","To Date","Interest Rate"};
//       private ArrayList buffer =new ArrayList();
//      /// private ArrayList<DeathReliefMasterOB> a=new ArrayList<DeathReliefMasterOB>();
//        int j=0;
//        Integer H;
//        int h;
//        Double dd;
//        Format formatter;
//        public void getTableData()
//        {
//          for(int k=0;k<buffer1.size();k++)
//          {
//               String[] Dts=new String[10];
//               Dts[0]=buffer1.get(k)[0];
//               Dts[1]=buffer1.get(k)[1];
//               Dts[2]=buffer1.get(k)[2];
//               buffer.add(Dts);
//          }
//        }
//         public int getRowCount() {
//            return buffer.size();
//        }
//
//      //  @Override
//        public int getColumnCount() {
//            return 5;
//        }
//
//       // @Override
//        public Object getValueAt(int rowIndex, int columnIndex) {
//          return buffer.get(rowIndex)[columnIndex];
//        }
//        
//         public String getColumnName(int col) {
//            return colnames[col];
//        }
//        }


    
}
