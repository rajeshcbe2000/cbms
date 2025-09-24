/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ServiceWiseLoanAmountUI.java
 *
 * Created on January 20, 2005, 3:03 PM
 */

package com.see.truetransact.ui.termloan.personalSuretyConfiguration;

/**
 *
 * @author  ashokvijayakumar
 */
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uivalidation.NumericValidation;
import java.util.ResourceBundle;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.DefaultValidation;

import java.util.Observable;
import java.util.Observer;
import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.commonutil.DateUtil;
import java.util.Date.*;
import java.util.*;
import java.text.*;

public class ServiceWiseLoanAmountUI extends CInternalFrame implements Observer, UIMandatoryField{
    
    /** Vairable Declarations */
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.generalbodydetails.GeneralBodyDetailsRB", ProxyParameters.LANGUAGE);//Creating Instance For ResourceBundle-TokenConfigRB
    private ServiceWiseLoanAmountOB observable; //Reference for the Observable Class TokenConfigOB
    private ServiceWiseLoanAmountMRB objMandatoryRB = new ServiceWiseLoanAmountMRB();//Instance for the MandatoryResourceBundle
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final String AUTHORIZE="Authorize";//Variable used when btnAuthorize is clicked
    int updateTab=-1;
    private boolean updateMode = false;
    private String iidd="";
    private String action = "";
    private String sts = "";
    private  Object columnNames[] = { "","Product ID","Product description"};
    private List bufferList=new ArrayList();
    private List editList=new ArrayList();
    // String iidd="";
    /** Creates new form TokenConfigUI */
    public ServiceWiseLoanAmountUI() {
        initForm();
    }
    
    /** Method which is used to initialize the form TokenConfig */
    private void initForm(){
        initComponents();
        setFieldNames();
        observable = new ServiceWiseLoanAmountOB();
        observable.addObserver(this);
        observable.resetForm();
        // initComponentData();
        setMandatoryHashMap();
        initComponentData();
        // setMaxLengths();
        //        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panEmpTraning);
        //  txtTotalAttandance.setValidation(new NumericValidation(10,4));
        txtFromServicePeriod.setMaxLength(16);
        txtFromServicePeriod.setValidation(new NumericValidation());
        txtToServicePeriod.setMaxLength(16);
        txtToServicePeriod.setValidation(new NumericValidation());
        txtMaximumLoanAmount.setMaxLength(16);
        txtMaximumLoanAmount.setValidation(new NumericValidation());
        txtMinimumLoanAmount.setMaxLength(16);
        txtMinimumLoanAmount.setValidation(new NumericValidation());
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panIMBPtab,getMandatoryHashMap());
        ClientUtil.enableDisable(panIMBPtab, false);
        setButtonEnableDisable();
        //      cboProdType.setModel(observable.getCbmProdType());
        // enableDisablePanButton(false);
        //  enableDisablePanGeneralBodyDetails(false);
        // txtTrainingID.setEnabled(false);
    }
    
    private void tableData() {
        
        // Object rowData[][] = new Object[+bufferList.size()][5];
        //   if(cboProdType.getSelectedIndex()>0)
        //   {
        // String pid=cboProdType.getSelectedItem().toString();
        HashMap where=new HashMap();
        //  where.put("AUTH_TYPE",pid);
        bufferList=ClientUtil.executeQuery("getProductDtsForServiceWiseLoanAmount", where);
        System.out.println("Load BufferelIst>>>"+bufferList.size());
        boolean b=false;
        if(!bufferList.isEmpty())
        { Object rowData[][] =new Object[bufferList.size()][3];
          for(int i=0;i<bufferList.size();i++) {
              HashMap m1=new HashMap();
              m1=(HashMap)bufferList.get(i);
              rowData[i][0]=b;
              rowData[i][1]=m1.get("PROD_ID");
              rowData[i][2]=m1.get("PROD_DESC");
          }
          
          if(sts.equals("Edit")) {
              boolean t=true;
              System.out.println("Ediitttttttteaaaaaain tbl..."+sts);
              editList=observable.getSelectedList();
              System.out.println("Ediitttttttteaaaaaa..."+editList.size());
              for(int k=0;k<editList.size();k++){
                  HashMap m1=new HashMap();
                  m1=(HashMap)editList.get(k);
                  for(int i=0;i<bufferList.size();i++){
                      HashMap m2=new HashMap();
                      m2=(HashMap)bufferList.get(i);
                      
                      System.out.println("iii" +i+"prod id sect.."+m1.get("PROD_ID")+"   "+m2.get("PROD_ID"));
                      if(m1.get("PROD_ID").toString().equals(m2.get("PROD_ID").toString())) {
                          rowData[i][0]=t;
                          
                          System.out.println("rowData[i][0]=="+rowData[i][0].toString()+" rowData[i][0]"+rowData[i][1].toString());
                          break;
                      }
                  }
              }
              sts="";
          }
          
          //  Integer rowData[][] = new  Integer[buffer1.size()][3];
          //        int j=0;
          //        String d1="";
          //        String d2="";
          //        int i=0;
          //        System.out.println("BuufferrList  "+bufferList.size());
          //
          //        for(i=0;i<bufferList.size();i++) {
          //            HashMap m=new HashMap();
          //            m=(HashMap)bufferList.get(i);
          //            String iid=m.get("DRF_INTEREST_ID").toString();
          //            String mNo=m.get("MEMBER_NO").toString();
          //            String mName=m.get("MEMBER_NAME").toString();
          //            double interst=calculateInterest(pid,mNo,iid);
          //            System.out.println("intersmmmmmmmmmm...."+interst);
          //            if(interst<0)
          //            {
          //                return;
          //            }
          //            m.put("INTEREST",interst);
          //            System.out.println("iii m00001 : "+m.get(""));
          //            rowData[i][0]=m.get("DRF_INTEREST_ID").toString();
          //            //System.out.println("iii m000011 : "+m.get("FROM"));
          //            rowData[i][1]=m.get("MEMBER_NO").toString();
          //            rowData[i][2]=m.get("MEMBER_NAME").toString();
          //            rowData[i][3]=m.get("AMOUNT").toString();
          //            rowData[i][4]=""+interst;
          //
          //            //   System.out.println("iii m0000 111: "+m.get("FROM"));
          //        }
          //        System.out.println("iii m0000 222: ");
          //
          //
          
          
          
          //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
          
          
          tblproduct.setModel(new javax.swing.table.DefaultTableModel(rowData,columnNames){
              
              
              // @Override
              public Class getColumnClass(int column) {
                  if(column==0) {
                      return Boolean.class;
                  }
                  else {
                      return String.class;
                  }
              }
              
              
              public boolean isCellEditable(int row, int column) {
                  //Only the third column
                  if(column==0) {
                      return true;
                  }
                  else {
                      return false;
                  }
              }
              
          }) ;
          tblproduct.setVisible(true);
          //        TOTAL=0;
          //        for(i=0;i<bufferList.size();i++) {
          //            HashMap m=new HashMap();
          //            m=(HashMap)bufferList.get(i);
          //            double d=Double.parseDouble(m.get("INTEREST").toString());
          //            System.out.println("intterestttt>>>>"+d);
          //            TOTAL=TOTAL+d;
          //        }
          //
          //        System.out.println("TOOOOTTTTT>"+TOTAL);
          //
        }
        //  }
    }
    
    
    
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        // lblStatus.setText(observable.getLblStatus());
        btnView.setEnabled(!btnView.isEnabled());
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
        // cboTrainDest.setName("cboTrainDest");
        lblFromServicePeriod.setName("lblFromServicePeriod");
        lblToServicePeriod.setName("lblToServicePeriod");
        lblMaximumLoanAmount.setName("lblMaximumLoanAmount");
        lblMinimumLoanAmount.setName("lblMinimumLoanAmount");
        lblFromDate.setName("lblFromDate");
        //        lblRemarks.setName("lblRemarks");
        lblStatus.setName("lblStatus");
        //   mbrTokenConfig.setName("mbrTokenConfig");
        panStatus.setName("panStatus");
        //  panIndEmpDetails.setName("panIndEmpDetails");
        // panEmpList.setName("panEmpList");
        panIMBPtab.setName("panIMBPtab");
        //  panPersonalSuretyData.setName("panPersonalSuretyData");
        // tblEmpList.setName("tblEmpList");
        txtFromServicePeriod.setName("txtFromServicePeriod");
        txtToServicePeriod.setName("txtToServicePeriod");
        txtMaximumLoanAmount.setName("txtMaximumLoanAmount");
        txtMinimumLoanAmount.setName("txtMinimumLoanAmount");
        tdtFromDate.setName("tdtFromDate");
        //        txtTotalAttandance.setName("txtTotalAttandance");
        //       txaRemarks.setName("txaRemarks");
        
        //        tdtDate.setName("tdtDate");
        
    }
    
    
    
    public void updateOBFields() {
        
   /*     if(panPersonalSurety.isShowing()) {
            observable.setTxtPersonalID(iidd);
            System.out.println("IIIIIiiiddd in uupppddataeae"+iidd);
            iidd="";
            observable.setTxtCloseBefore(txtCloseBefore.getText());
            observable.setTxtMaxSurety(txtMaxSurety.getText());
            observable.setPan(1);
        }
    */
        // if(panIMBP.isShowing()) {
        //  observable.setCboProdType((String) cboProdType.getSelectedItem());
        
        observable.setTxtPersonalID(iidd);
        iidd="";
        observable.setTxtFromServicePeriod(txtFromServicePeriod.getText());
        observable.setTxtTotNoOfSureity(txtTotNoOfSureity.getText());
        observable.setTxtTotServicePeriod(txtTotServicePeriod.getText());
        observable.setTxtToServicePeriod(txtToServicePeriod.getText());
        observable.setTxtMinimumLoanAmount(txtMinimumLoanAmount.getText());
        observable.setTdtFromDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
        observable.setTxtMaximumLoanAmount(txtMaximumLoanAmount.getText());
        observable.setSelectedList(getSelectedFromTable());
        
        //    observable.setPan(2);
        //  }
        //        observable.setTxtremarks(txaRemarks.getText());
        //        observable.setTxttotalAttendance(txtTotalAttandance.getText());
        // observable.setTxtGeneralBoadyID()
    }
    
    public List getSelectedFromTable() {
        List selectedList=new ArrayList();
        for(int i=0;i<tblproduct.getRowCount();i++) {
            
            System.out.println("tab val"+tblproduct.getValueAt(i,0));
            System.out.println("iivalueee>>>"+Boolean.valueOf(tblproduct.getValueAt(i,0).toString()));
            if(tblproduct.getValueAt(i,0).toString().equals("true")) {
                System.out.println("innnnnn ifff");
                HashMap map=new HashMap();
                map.put("PROD_ID",tblproduct.getValueAt(i,1).toString());
                map.put("PROD_DESC",tblproduct.getValueAt(i,2).toString());
                selectedList.add(map);
            }
        }
        System.out.println("selected Listttt"+selectedList);
        return selectedList;
    }
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtFromServicePeriod", new Boolean(true));
        mandatoryMap.put("txtToServicePeriod", new Boolean(true));
        mandatoryMap.put("txtMaximumLoanAmount", new Boolean(true));
        //        mandatoryMap.put("txtTotalAttandance", new Boolean(true));
        //        mandatoryMap.put("txaRemarks", new Boolean(false));
        //        mandatoryMap.put("txtNoOfTrainees", new Boolean(true));
        //        mandatoryMap.put("tdtFrom", new Boolean(true));
        //        mandatoryMap.put("tdtTo", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    
    
    
    
    
 /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        //        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        //        btnDelete.setText(resourceBundle.getString("btnDelete"));
        //        btnClose.setText(resourceBundle.getString("btnClose"));
        //        lblNoOfTokens.setText(resourceBundle.getString("lblNoOfTokens"));
        //        btnReject.setText(resourceBundle.getString("btnReject"));
        //        btnEdit.setText(resourceBundle.getString("btnEdit"));
        //        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        //        btnUserId.setText(resourceBundle.getString("btnUserId"));
        //        lblSeriesNo.setText(resourceBundle.getString("lblSeriesNo"));
        //        btnException.setText(resourceBundle.getString("btnException"));
        //        lblMsg.setText(resourceBundle.getString("lblMsg"));
        //        btnNew.setText(resourceBundle.getString("btnNew"));
        //        lblEndingTokenNo.setText(resourceBundle.getString("lblEndingTokenNo"));
        //        lblStartingTokenNo.setText(resourceBundle.getString("lblStartingTokenNo"));
        //        btnSave.setText(resourceBundle.getString("btnSave"));
        //        btnCancel.setText(resourceBundle.getString("btnCancel"));
        //        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        //        lblTokenType.setText(resourceBundle.getString("lblTokenType"));
        //        lbSpace2.setText(resourceBundle.getString("lbSpace2"));
        //        lblStatus.setText(resourceBundle.getString("lblStatus"));
        //        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        //        btnPrint.setText(resourceBundle.getString("btnPrint"));
        //        lblTokenIssueId.setText(resourceBundle.getString("lblTokenIssueId"));
        //        lblReceiverId.setText(resourceBundle.getString("lblReceiverId"));
    }
    
    
    /*Setting model to the combobox cboTokenType  */
    private void initComponentData() {
        try{
            //      cboProdType.setModel(observable.getCbmProdType());
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        //        if(observable.gettdtDate()!=null)
        //           {
        //                 tdtDate.setDateValue(getDtPrintValue(String.valueOf(observable.gettdtDate())));
        //          }
        //
        //      txtCloseBefore.setText(observable.getTxtCloseBefore());
        //        txtMaxSurety.setText(observable.getTxtMaxSurety());
        //observable.gettdtDate());
        
        iidd=observable.getTxtPersonalID();
     
        txtFromServicePeriod.setText(observable.getTxtFromServicePeriod());
        txtToServicePeriod.setText(observable.getTxtToServicePeriod());
        txtTotNoOfSureity.setText(observable.getTxtTotNoOfSureity());
        txtTotServicePeriod.setText(observable.getTxtTotServicePeriod());
        txtMaximumLoanAmount.setText(observable.getTxtMaximumLoanAmount());
        txtMinimumLoanAmount.setText(observable.getTxtMinimumLoanAmount());
        tdtFromDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtFromDate()));
        editList=observable.getSelectedList();
        tableData();
        
        //        cboProdType.setSelectedItem(observable.getCboProdType());
    }
    
    public String getDtPrintValue(String strDate) {
        try {
            //      System.out.println("strDate getDtPrintValue99999999999999999999999999999999999999================="+strDate);
            //create SimpleDateFormat object with source string date format
            SimpleDateFormat sdfSource = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            //parse the string into Date object
            Date date = sdfSource.parse(strDate);
            //create SimpleDateFormat object with desired date format
            SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
            //parse the date into another format
            strDate = sdfDestination.format(date);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }
    
    
    
    
    
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        //        cboTokenType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTokenType"));
        //        cboSeriesNo.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSeriesNo"));
        //        txtStartingTokenNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStartingTokenNo"));
        //        txtEndingTokenNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndingTokenNo"));
        //        txtNoOfTokens.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfTokens"));
        //        txtTokenIssueId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTokenIssueId"));
        //        txtReceiverId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReceiverId"));
    }
    
    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths(){
        //        txtStartingTokenNo.setMaxLength(8);
        //        txtEndingTokenNo.setMaxLength(8);
        // txtEmpID.setAllowAll(true);
    }
    
    /*Makes the button Enable or Disable accordingly when usier clicks new,edit or delete buttons */
    
    
    /** Method used to check whether the Mandatory Fields in the Form are Filled or not */
    
    /** Method used to Give a Alert when any Mandatory Field is not filled by the user */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    
    /** Method to make HelpButton btnUserId enable or disable..accroding to Edit,Delete,New,Save Button Clicked */
    private void setHelpBtnEnableDisable(boolean flag){
        //                btnE.setEnabled(flag);
    }
    
    //    private void btnCheck(){
    //         btnCancel.setEnabled(true);
    //         btnSave.setEnabled(false);
    //         btnNew.setEnabled(false);
    //         btnDelete.setEnabled(false);
    //         btnAuthorize.setEnabled(false);
    //         btnReject.setEnabled(false);
    //         btnException.setEnabled(false);
    //         btnEdit.setEnabled(false);
    //     }
    
    //    private void enableDisablePanButton(boolean flag){
    //        btnEmpDelete.setEnabled(flag);
    //        btnEmpSave.setEnabled(flag);
    //        btnEmpNew.setEnabled(flag);
    //    }
    //    private void enableDisablePanGeneralBodyDetails(boolean flag){
    //        txtEmpID.setEnabled(flag);
    //        btnEmp.setEnabled(flag);
    //    }
    //
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrTokenConfig = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace70 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace71 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace72 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panIMBPtab = new com.see.truetransact.uicomponent.CPanel();
        panIMBP = new com.see.truetransact.uicomponent.CPanel();
        panSureity = new com.see.truetransact.uicomponent.CPanel();
        lblFromServicePeriod = new com.see.truetransact.uicomponent.CLabel();
        lblToServicePeriod = new com.see.truetransact.uicomponent.CLabel();
        lblMaximumLoanAmount = new com.see.truetransact.uicomponent.CLabel();
        lblMinimumLoanAmount = new com.see.truetransact.uicomponent.CLabel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        txtFromServicePeriod = new com.see.truetransact.uicomponent.CTextField();
        txtToServicePeriod = new com.see.truetransact.uicomponent.CTextField();
        txtMaximumLoanAmount = new com.see.truetransact.uicomponent.CTextField();
        txtMinimumLoanAmount = new com.see.truetransact.uicomponent.CTextField();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblPastServicePerson = new com.see.truetransact.uicomponent.CLabel();
        txtTotServicePeriod = new com.see.truetransact.uicomponent.CTextField();
        lblTotNoofSureityRequired = new com.see.truetransact.uicomponent.CLabel();
        txtTotNoOfSureity = new com.see.truetransact.uicomponent.CTextField();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblproduct = new com.see.truetransact.uicomponent.CTable();
        mbrTokenConfig = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptView = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(800, 625));
        setMinimumSize(new java.awt.Dimension(800, 625));
        setPreferredSize(new java.awt.Dimension(800, 625));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnView);

        lbSpace3.setText("     ");
        tbrTokenConfig.add(lbSpace3);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnNew);

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace70);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnEdit);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace71);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnDelete);

        lbSpace2.setText("     ");
        tbrTokenConfig.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnSave);

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace72);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTokenConfig.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnAuthorize);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace73);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnException);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace74);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnReject);

        lblSpace5.setText("     ");
        tbrTokenConfig.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrTokenConfig.add(btnPrint);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace75);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnClose);

        getContentPane().add(tbrTokenConfig, java.awt.BorderLayout.NORTH);

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

        panIMBPtab.setLayout(new java.awt.BorderLayout());

        panIMBP.setLayout(new java.awt.GridBagLayout());

        panSureity.setLayout(new java.awt.GridBagLayout());

        lblFromServicePeriod.setText("From ");
        lblFromServicePeriod.setMaximumSize(new java.awt.Dimension(150, 16));
        lblFromServicePeriod.setMinimumSize(new java.awt.Dimension(150, 16));
        lblFromServicePeriod.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = -97;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 67, 0, 0);
        panSureity.add(lblFromServicePeriod, gridBagConstraints);

        lblToServicePeriod.setText(" To");
        lblToServicePeriod.setMaximumSize(new java.awt.Dimension(150, 16));
        lblToServicePeriod.setMinimumSize(new java.awt.Dimension(150, 16));
        lblToServicePeriod.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.ipadx = -104;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 2, 0, 0);
        panSureity.add(lblToServicePeriod, gridBagConstraints);

        lblMaximumLoanAmount.setText("Maximum Loan Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 65, 0, 0);
        panSureity.add(lblMaximumLoanAmount, gridBagConstraints);

        lblMinimumLoanAmount.setText("Minimum Loan Amount");
        lblMinimumLoanAmount.setMaximumSize(new java.awt.Dimension(150, 16));
        lblMinimumLoanAmount.setMinimumSize(new java.awt.Dimension(150, 16));
        lblMinimumLoanAmount.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 67, 0, 0);
        panSureity.add(lblMinimumLoanAmount, gridBagConstraints);

        lblFromDate.setText("Effect From Date");
        lblFromDate.setMaximumSize(new java.awt.Dimension(150, 16));
        lblFromDate.setMinimumSize(new java.awt.Dimension(150, 16));
        lblFromDate.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 67, 0, 0);
        panSureity.add(lblFromDate, gridBagConstraints);

        txtFromServicePeriod.setMaximumSize(new java.awt.Dimension(100, 21));
        txtFromServicePeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromServicePeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFromServicePeriodActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 4, 0, 0);
        panSureity.add(txtFromServicePeriod, gridBagConstraints);

        txtToServicePeriod.setMaximumSize(new java.awt.Dimension(100, 21));
        txtToServicePeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToServicePeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtToServicePeriodActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 15;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -46;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 1, 0, 0);
        panSureity.add(txtToServicePeriod, gridBagConstraints);

        txtMaximumLoanAmount.setAllowNumber(true);
        txtMaximumLoanAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        panSureity.add(txtMaximumLoanAmount, gridBagConstraints);

        txtMinimumLoanAmount.setMaximumSize(new java.awt.Dimension(100, 21));
        txtMinimumLoanAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 4, 0, 0);
        panSureity.add(txtMinimumLoanAmount, gridBagConstraints);

        tdtFromDate.setMaximumSize(new java.awt.Dimension(100, 21));
        tdtFromDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtFromDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 4, 0, 0);
        panSureity.add(tdtFromDate, gridBagConstraints);

        lblPastServicePerson.setText("year past service person required including borrower");
        lblPastServicePerson.setMaximumSize(new java.awt.Dimension(150, 16));
        lblPastServicePerson.setMinimumSize(new java.awt.Dimension(150, 16));
        lblPastServicePerson.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 28;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 36;
        gridBagConstraints.ipadx = 156;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 4, 0, 0);
        panSureity.add(lblPastServicePerson, gridBagConstraints);

        txtTotServicePeriod.setAllowNumber(true);
        txtTotServicePeriod.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTotServicePeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotServicePeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotServicePeriodActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 64;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -46;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 18, 0, 10);
        panSureity.add(txtTotServicePeriod, gridBagConstraints);

        lblTotNoofSureityRequired.setText("Total  No Of Sureity Required");
        lblTotNoofSureityRequired.setMaximumSize(new java.awt.Dimension(150, 16));
        lblTotNoofSureityRequired.setMinimumSize(new java.awt.Dimension(150, 16));
        lblTotNoofSureityRequired.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 14;
        gridBagConstraints.ipadx = 47;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 59, 0, 0);
        panSureity.add(lblTotNoofSureityRequired, gridBagConstraints);

        txtTotNoOfSureity.setAllowNumber(true);
        txtTotNoOfSureity.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 35;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 1, 11, 0);
        panSureity.add(txtTotNoOfSureity, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panIMBP.add(panSureity, gridBagConstraints);

        cScrollPane1.setMaximumSize(new java.awt.Dimension(752, 402));
        cScrollPane1.setMinimumSize(new java.awt.Dimension(752, 402));
        cScrollPane1.setPreferredSize(new java.awt.Dimension(752, 402));

        tblproduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Select", "Product Type", "Product description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        cScrollPane1.setViewportView(tblproduct);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = -72;
        gridBagConstraints.ipady = -252;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(52, 20, 76, 0);
        panIMBP.add(cScrollPane1, gridBagConstraints);

        panIMBPtab.add(panIMBP, java.awt.BorderLayout.CENTER);

        getContentPane().add(panIMBPtab, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");

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

        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptView);

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

        mbrTokenConfig.add(mnuProcess);

        setJMenuBar(mbrTokenConfig);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void txtFromServicePeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFromServicePeriodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFromServicePeriodActionPerformed
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        //        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        //        observable.setStatus();
        //        lblStatus.setText(observable.getLblStatus());
        //        callView("Enquiry");
        //        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        //..  observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        //.. authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        //..  observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        //..  authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        //..  setModified(true);
        //.. observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        //,,  authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        // btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        //,, btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        //,,  cifClosingAlert();
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        //,, cifClosingAlert();
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        ///,, btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        //,,   btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        
        observable.resetForm();
        setModified(false);
        //txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panIMBPtab, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        lblStatus.setText("");
        // setButtons(false);
        
        
        
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView( ClientConstants.ACTION_STATUS[3]);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        
        
        
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        sts="Edit";
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        lblStatus.setText(CommonConstants.TOSTATUS_UPDATE);
        
    }//GEN-LAST:event_btnEditActionPerformed
    
    
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
        viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
        viewType.equals(ClientConstants.ACTION_STATUS[17])) {
            viewMap.put(CommonConstants.MAP_NAME, "ServiceWiseLoanAmount.getSelectPersonalList");
            ArrayList lst = new ArrayList();
            lst.add("ESLE_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            
        }
        // else {
        //    viewMap.put(CommonConstants.MAP_NAME, "Borrowings.getSelectAcctHeadTOList");
        // }
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        viewMap.put(CommonConstants.MAP_WHERE, where);
        where = null;
        new ViewAll(this,viewMap).show();
        // new ViewAll(this,
    }
    
    
    
    public void fillData(Object  map) {
        
        //        setModified(true);
        HashMap hash = (HashMap) map;
        //          if(viewType.equals("PRICIPAL_GROUP_HEAD"))
        //          {
        //                 txtprinGrpHead.setText(hash.get("AC_HD_ID").toString());
        //          }
        //          if(viewType.equals("INT_GROUP_HEAD"))
        //          {
        //                 txtintGrpHead.setText(hash.get("AC_HD_ID").toString());
        //          }
        //        if(viewType.equals("PENAL_GROUP_HEAD"))
        //          {
        //                 txtpenalGrpHead.setText(hash.get("AC_HD_ID").toString());
        //          }
        //        if(viewType.equals("CHARGES_GROUP_HEAD"))
        //          {
        //                 txtchargeGrpHead.setText(hash.get("AC_HD_ID").toString());
        //          }
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                HashMap where = new HashMap();
                //System.out.println("hash.get"+hash.get("BORROWING_NO"));
                where.put("ESLEID", hash.get("ESLE_ID"));
                // where.put(CommonConstants.BRANCH_ID, "0001");
                hash.put(CommonConstants.MAP_WHERE, where);
                observable.populateData(hash);
                
                // fillTxtNoOfTokens();
                if (observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW) {
                    ClientUtil.enableDisable(panIMBPtab, false);
                }else  if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                    ClientUtil.enableDisable(panIMBPtab, true);
                }
                if(viewType.equals(AUTHORIZE)){
                    ClientUtil.enableDisable(panIMBPtab, false);
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
                setButtonEnableDisable();
            }
        }
    }
    
    
    
    
    
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        savePerformed();
        setModified(false);
        //    System.out.println("IN btnSaveActionPerformed111");
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnEdit.setEnabled(true);
        btnNew.setEnabled(true);
        btnDelete.setEnabled(true);
        txtFromServicePeriod.setText("");
        txtToServicePeriod.setText("");
        txtMaximumLoanAmount.setText("");
        
        
        // setModified(false);
        // updateOBFields();
        // saveAction();
        //        btnAuthorize.setEnabled(true);
        //        btnReject.setEnabled(true);
        //        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    
    private void savePerformed(){
        
        // System.out.println("IN savePerformed");
        //  String action;
        //    System.out.println("IN observable.getActionType(): "+observable.getActionType());
        //      System.out.println("IN ClientConstants.ACTIONTYPE_NEW: "+ClientConstants.ACTIONTYPE_NEW);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ){
            //     System.out.println("IN savePerformed ACTIONTYPE_NEW");
            
            action=CommonConstants.TOSTATUS_INSERT;
            System.out.println("actionnnnnnn"+CommonConstants.TOSTATUS_INSERT);
            saveAction(action);
            
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            System.out.println("updateeee1234>>>>>@@");
            action=CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            action=CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
    }
    
    
    private void saveAction(String status){
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        //  System.out.println("status saveAction11111: "+status);
        //       txtAmtBorrowed.
        // final String mandatoryMessage = checkMandatory(panGeneralBodyData);
        StringBuffer message = new StringBuffer("");
        //   if(panPersonalSurety.isShowing()) {
        
        if(txtFromServicePeriod.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtFromServicePeriod"));
            message.append("\n");
        }
        if(txtToServicePeriod.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtToServicePeriod"));
            message.append("\n");
        }
        if(txtMaximumLoanAmount.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtMaximumLoanAmount"));
            message.append("\n");
        }
        //   }
        else {
            
        }
        //         if(txtTotalAttandance.getText().equals(""))
        //        {
        //                message.append(objMandatoryRB.getString("txtTotalAttandance"));
        //                message.append("\n");
        //         }
        //
        
        //Portion is for calculating exp date
        // setExpDateOnCalculation();
        
        
        // */
        //setExpDateOnCalculation();
        //  System.out.println("status saveAction: "+status);
        if(message.length() > 0 ){
            displayAlert(message.toString());
        }else{
            updateOBFields();
            //setExpDateOnCalculation();
            observable.execute(status);
            //            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
            //                HashMap lockMap = new HashMap();
            //                ArrayList lst = new ArrayList();
            //                lst.add("BORROWING_NO");
            //                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            //             //   if (observable.getProxyReturnMap()!=null) {
            //              //      if (observable.getProxyReturnMap().containsKey("CONFIG_ID")) {
            //              //          lockMap.put("CONFIG_ID", observable.getProxyReturnMap().get("CONFIG_ID"));
            //              //      }
            //              //  }
            //                if (status==CommonConstants.TOSTATUS_UPDATE) {
            //                    lockMap.put("BORROWING_NO", observable.getBorrowingNo());
            //                }
            //          //      setEditLockMap(lockMap);
            //               // setEditLock();
            //                settings();
            //            }
            
            
            bufferList.clear();
            tableData();
        }
        
        System.out.println("observable.getProxyReturnMap()>>>>@@@@aaaaa"+observable.getProxyReturnMap());
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
            if (observable.getProxyReturnMap()!=null) {
                if(action==CommonConstants.TOSTATUS_INSERT){
                    System.out.println("observable.getProxyReturnMap()>>>>111"+observable.getProxyReturnMap());
                    ClientUtil.showMessageWindow("Saved Successfully , ESLE_ID : "+observable.getProxyReturnMap().get("ID_NO"));
                }
                
            }
        }
        
        if(action==CommonConstants.TOSTATUS_UPDATE){
            System.out.println("trimmmmmmm>>>>>>>");
            ClientUtil.showMessageWindow("Updated Successfully");
        }
        
        observable.resetForm();
        //txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panIMBPtab, false);
        // observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
    }
    
    private void settings(){
        observable.resetForm();
        //  txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panIMBPtab, false);
        setButtonEnableDisable();
        observable.setResultStatus();
    }
    
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        bufferList.clear();
        tableData();
        observable.resetForm();
        //txtNoOfTokens.setText("");
        ClientUtil.enableDisable(panIMBPtab, true);
        
        // System.out.println("btnNewActionPerformed ACTION STA: "+ClientConstants.ACTIONTYPE_NEW);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
        lblStatus.setText(CommonConstants.TOSTATUS_INSERT);
        
        
    }//GEN-LAST:event_btnNewActionPerformed

private void txtToServicePeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtToServicePeriodActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtToServicePeriodActionPerformed

private void txtTotServicePeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotServicePeriodActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtTotServicePeriodActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblFromServicePeriod;
    private com.see.truetransact.uicomponent.CLabel lblMaximumLoanAmount;
    private com.see.truetransact.uicomponent.CLabel lblMinimumLoanAmount;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPastServicePerson;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToServicePeriod;
    private com.see.truetransact.uicomponent.CLabel lblTotNoofSureityRequired;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panIMBP;
    private com.see.truetransact.uicomponent.CPanel panIMBPtab;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSureity;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CTable tblproduct;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CTextField txtFromServicePeriod;
    private com.see.truetransact.uicomponent.CTextField txtMaximumLoanAmount;
    private com.see.truetransact.uicomponent.CTextField txtMinimumLoanAmount;
    private com.see.truetransact.uicomponent.CTextField txtToServicePeriod;
    private com.see.truetransact.uicomponent.CTextField txtTotNoOfSureity;
    private com.see.truetransact.uicomponent.CTextField txtTotServicePeriod;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] args) {
        ServiceWiseLoanAmountUI generalbody = new ServiceWiseLoanAmountUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(generalbody);
        j.show();
        generalbody.show();
    }
}
