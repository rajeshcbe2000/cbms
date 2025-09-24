/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * frmRent.java
 *
 * Created on September 12, 2011, 12:08 PM
 */

package com.see.truetransact.ui.roomrent;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import java.text.*;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import java.util.Observable;
import java.math.BigDecimal;
import com.see.truetransact.clientutil.ClientUtil;
import java.util.Observer;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.ui.common.viewall.ViewAll;
import java.util.ResourceBundle;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import java.util.Date;
import java.awt.event.*;
import javax.swing.SwingConstants;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author  userdd
 */
public class frmRent extends CInternalFrame implements Observer, UIMandatoryField{
    
 private RentOB observable;
 private String[][] tabledata;
 private String[] column;
 private RentMRB objMandatoryRB = new RentMRB();//Instance for the MandatoryResourceBundle
 private DefaultTableModel model;
 private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
 private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
 private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
 private final String AUTHORIZE="Authorize";//Variable used when btnAuthorize is clicked
      private TableModelListener tableModelListener;
  public boolean isEditTable=false;     
 public CommonMethod cm;
 public String rdDetilsId="";
 private Date currDt = null;
    /** Creates new form ifrNewBorrowing */
    public frmRent() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setFieldNames();
        setObservable();
        observable.resetForm();
        setMaxLengths();
        initTableData();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panRent, getMandatoryHashMap());
        ClientUtil.enableDisable(panRent, false);
        setButtonEnableDisable();
        disableGrpHead();
      cm=new CommonMethod();
      
    }
    
    public Date getProperDateFormat(Object obj) {
        Date curDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            curDt = (Date) currDt.clone();
            curDt.setDate(tempDt.getDate());
            curDt.setMonth(tempDt.getMonth());
            curDt.setYear(tempDt.getYear());
        }
        return curDt;
    }
    
      public void fillData1(String rmNo)
   {
       System.out.println("1111111111111111111111");
        HashMap singleAuthorizeMap = new HashMap();
         singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
         singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
          singleAuthorizeMap.put("RMNUMBER", rmNo);
         System.out.println("aListIN 333333333333333333==:"+rmNo);
          List aList= ClientUtil.executeQuery("getRentTableData", singleAuthorizeMap);
           System.out.println("aListIN PRINYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY:"+aList);
            model.setRowCount(0);
     
                           for(int i=0;i<aList.size();i++)
                           {
                            
                              HashMap map=(HashMap)aList.get(i);
                            System.out.println("aListIN 111111111111111111111111111111111111:"+map);
                              String roomNum=map.get("ROOMNUM").toString();
                              String verNo=map.get("VERSION_NUM").toString();
                              String effDate=getDtPrintValue(map.get("EFF_DATE").toString());
                              String date=null;
                              if(effDate!=null)
                                  {
                                     date=getDtPrintValue(String.valueOf(effDate));
                                 }
                             
                              String rent_amt=map.get("RENT_AMT").toString();
                              String rent_frq=map.get("RENT_FRQ").toString();
                               String penalRate=map.get("PENEL_RATE").toString();
                               String rdId="";
                               if(map.get("RDID")!=null)
                                 rdId=map.get("RDID").toString();
                              
                               model.addRow(new String[]{roomNum,date,formatCrore(rent_amt),rent_frq,penalRate,rdId}); 
                          //   tblData.removeRowSelectionInterval(5,5); 
                       tblData.getColumnModel().getColumn(5).setMaxWidth(0);
        tblData.getColumnModel().getColumn(5).setMinWidth(0);
        tblData.getColumnModel().getColumn(5).setPreferredWidth(0);
                           }
       DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        tblData.getColumnModel().getColumn(2).setCellRenderer( rightRenderer );
        tblData.getColumnModel().getColumn(4).setCellRenderer( rightRenderer );

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
        fmtStrB.append (dec);
       
        str = fmtStrB.toString();
       
        str = sign+str;
       
        if (str.equals(".00")) str = "0";
       
        return str;
    }
   public void initTableData()
   {
     String data[][] ={{}};
     String col[] = {"Room No","Effective date","Rent Amount","Rent Frequency","Penel Rate",""};
     model =new DefaultTableModel(data,col) {
             public boolean isCellEditable(int row, int column) {
                //all cells false
                 return false;
                }
            };
         tblData.setModel(model);
           model.setRowCount(0);
   
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
        tbrSale.setName("tbrSale");
        btnAdvHead.setName("btnAdvHead");
            lblBuildingNo.setName("lblBuildingNo");
            lblBuildingDes.setName("lblBuildingDes");
            lblRentAccHead.setName("lblRentAccHead");
            lblPenelAccHead.setName("lblPenelAccHead");
            lblNoticeAccHead.setName("lblNoticeAccHead");
            lblLegalAccHead.setName("lblLegalAccHead");
            lblArbAccHead.setName("lblArbAccHead");
            lblCourtAccHead.setName("lblCourtAccHead");
            lblExeAccHead.setName("lblExeAccHead");
            lblStatus1.setName("lblStatus1");
            lblRoomNo.setName("lblRoomNo");
         //   lblVerNo.setName("lblVerNo");
            lblEffDate.setName("lblEffDate");
            lblRentAmt.setName("lblRentAmt");
            lblRentFeq.setName("lblRentFeq");
            lblPenelRate.setName("lblPenelRate");
            lblAdvHead.setName("lblAdvHead");
            btnAdd.setName("btnAdd");
            txtRoomNo.setName("txtRoomNo");
            txtBuildingNo.setName("txtBuildingNo");
            txtBuildingDes.setName("txtBuildingDes");
            txtRentAccHead.setName("txtRentAccHead");
            txtPenelAccHead.setName("txtPenelAccHead");
            txtNoticeAccHead.setName("txtNoticeAccHead");
            txtLegalAccHead.setName("txtLegalAccHead");
            txtArbAccHead.setName("txtArbAccHead");
            txtCourtAccHead.setName("txtCourtAccHead");
            txtExeAccHead.setName("txtExeAccHead");
            txtAdvHead.setName("txtAdvHead");
            cboStatus1.setName("cboStatus1");
           // txtVersNo.setName("txtVersNo");
            tdtEffDate.setName("tdtEffDate");
            txtRentAmt.setName("txtRentAmt");
            cboRentFeq.setName("cboRentFeq");
            txtPenelRate.setName("txtPenelRate");
        
    }
    private void setMaxLengths() {
           txtRentAmt.setValidation(new CurrencyValidation());
       }
      private void setObservable() {
        try{
            observable = RentOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            //parseException.logException(e,true);
            System.out.println("Error in setObservable():"+e);
        }
    }
    
  
      /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
        viewType = currField;
        //System.out.println("viewType===="+viewType);
        HashMap viewMap = new HashMap();
         if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
            viewType.equals(ClientConstants.ACTION_STATUS[17])) 
        {
             viewMap.put(CommonConstants.MAP_NAME, "Rent.getSelectRentList");
            ArrayList lst = new ArrayList();
            lst.add("ROOM_NUMBER");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
        }
         else
         {
                viewMap.put(CommonConstants.MAP_NAME, "Borrowings.getSelectAcctHeadTOList");
         }
       
        HashMap where = new HashMap();
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        viewMap.put(CommonConstants.MAP_WHERE, where);
        where = null;
        new ViewAll(this,viewMap).show();
   
    }
     public void fillData(Object  map) {
    
         //System.out.println("000000000000000000000");
//        setModified(true);
        HashMap hash = (HashMap) map;
    
          if(viewType.equals("RENT_GROUP_HEAD"))
          {
                 txtRentAccHead.setText(hash.get("AC_HD_ID").toString());
          }
          if(viewType.equals("PENEL_GROUP_HEAD"))
          {
                 txtPenelAccHead.setText(hash.get("AC_HD_ID").toString());
          }
        if(viewType.equals("NOTICE_GROUP_HEAD"))
          {
                 txtNoticeAccHead.setText(hash.get("AC_HD_ID").toString());
          }
        if(viewType.equals("LEGAL_GROUP_HEAD"))
          {
                 txtLegalAccHead.setText(hash.get("AC_HD_ID").toString());
          }
          if(viewType.equals("ARB_GROUP_HEAD"))
          {
                 txtArbAccHead.setText(hash.get("AC_HD_ID").toString());
          }
          if(viewType.equals("COURT_GROUP_HEAD"))
          {
                 txtCourtAccHead.setText(hash.get("AC_HD_ID").toString());
          }
          if(viewType.equals("EXE_GROUP_HEAD"))
          {
                 txtExeAccHead.setText(hash.get("AC_HD_ID").toString());
          }
          if(viewType.equals("ADVANCE_RENT_HEAD"))
          {
                 txtAdvHead.setText(hash.get("AC_HD_ID").toString());
          }
         
        
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                HashMap where = new HashMap();
                where.put("RMNUMBER", hash.get("ROOM_NUMBER"));
                hash.put(CommonConstants.MAP_WHERE, where);
                System.out.println("Authorized=======");
                observable.populateData(hash);
                System.out.println("hash===="+hash);
                update(observable, map);
                fillData1(hash.get("ROOM_NUMBER").toString());
                if (observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW) 
                {
                    ClientUtil.enableDisable(panRent, false);
                }else  if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                    ClientUtil.enableDisable(panRent, true);
                }
                if(viewType.equals(AUTHORIZE)){
                    ClientUtil.enableDisable(panRent, false);
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
                setButtonEnableDisable();
            }
        }
       disableGrpHead();
       setModified(true);
    }
     public void setButtons(boolean flag)
     {
         btnRentAccHd.setEnabled(flag);
        btnPenelAccHd.setEnabled(flag);
        btnNoticegpHead.setEnabled(flag);
        btnLegalgpHead.setEnabled(flag);
        btnArbgpHead.setEnabled(flag);
        btnCourtgpHead.setEnabled(flag);
        btnExegpHead.setEnabled(flag);
        btnAdvHead.setEnabled(flag);
     }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrSale = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace51 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace52 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace53 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace54 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace55 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panRent = new com.see.truetransact.uicomponent.CPanel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        btnLegalgpHead = new com.see.truetransact.uicomponent.CButton();
        txtRentAccHead = new com.see.truetransact.uicomponent.CTextField();
        txtPenelAccHead = new com.see.truetransact.uicomponent.CTextField();
        txtLegalAccHead = new com.see.truetransact.uicomponent.CTextField();
        txtArbAccHead = new com.see.truetransact.uicomponent.CTextField();
        txtCourtAccHead = new com.see.truetransact.uicomponent.CTextField();
        txtExeAccHead = new com.see.truetransact.uicomponent.CTextField();
        txtNoticeAccHead = new com.see.truetransact.uicomponent.CTextField();
        btnArbgpHead = new com.see.truetransact.uicomponent.CButton();
        btnCourtgpHead = new com.see.truetransact.uicomponent.CButton();
        btnExegpHead = new com.see.truetransact.uicomponent.CButton();
        btnRentAccHd = new com.see.truetransact.uicomponent.CButton();
        btnPenelAccHd = new com.see.truetransact.uicomponent.CButton();
        btnNoticegpHead = new com.see.truetransact.uicomponent.CButton();
        txtBuildingNo = new com.see.truetransact.uicomponent.CTextField();
        cScrollPaneTable1 = new com.see.truetransact.uicomponent.CScrollPaneTable();
        tblData = new com.see.truetransact.uicomponent.CTable();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        txtBuildingDes = new com.see.truetransact.uicomponent.CTextArea();
        cboStatus1 = new com.see.truetransact.uicomponent.CComboBox();
        panDetails = new com.see.truetransact.uicomponent.CPanel();
        lblRoomNo = new com.see.truetransact.uicomponent.CLabel();
        txtRoomNo = new com.see.truetransact.uicomponent.CTextField();
        lblEffDate = new com.see.truetransact.uicomponent.CLabel();
        tdtEffDate = new com.see.truetransact.uicomponent.CDateField();
        txtRentAmt = new com.see.truetransact.uicomponent.CTextField();
        cboRentFeq = new com.see.truetransact.uicomponent.CComboBox();
        txtPenelRate = new com.see.truetransact.uicomponent.CTextField();
        lblPenelRate = new com.see.truetransact.uicomponent.CLabel();
        lblRentFeq = new com.see.truetransact.uicomponent.CLabel();
        lblRentAmt = new com.see.truetransact.uicomponent.CLabel();
        btnDelete1 = new com.see.truetransact.uicomponent.CButton();
        btnSave1 = new com.see.truetransact.uicomponent.CButton();
        btnAdd = new com.see.truetransact.uicomponent.CButton();
        lblBuildingNo = new com.see.truetransact.uicomponent.CLabel();
        lblBuildingDes = new com.see.truetransact.uicomponent.CLabel();
        lblRentAccHead = new com.see.truetransact.uicomponent.CLabel();
        lblPenelAccHead = new com.see.truetransact.uicomponent.CLabel();
        lblNoticeAccHead = new com.see.truetransact.uicomponent.CLabel();
        lblLegalAccHead = new com.see.truetransact.uicomponent.CLabel();
        lblArbAccHead = new com.see.truetransact.uicomponent.CLabel();
        lblCourtAccHead = new com.see.truetransact.uicomponent.CLabel();
        lblExeAccHead = new com.see.truetransact.uicomponent.CLabel();
        lblAdvHead = new com.see.truetransact.uicomponent.CLabel();
        txtAdvHead = new com.see.truetransact.uicomponent.CTextField();
        btnAdvHead = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
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
        setMaximumSize(new java.awt.Dimension(710, 600));
        setMinimumSize(new java.awt.Dimension(710, 600));
        setPreferredSize(new java.awt.Dimension(710, 600));
        getContentPane().setLayout(null);

        tbrSale.setMaximumSize(new java.awt.Dimension(700, 29));
        tbrSale.setMinimumSize(new java.awt.Dimension(700, 29));
        tbrSale.setPreferredSize(new java.awt.Dimension(700, 29));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
        tbrSale.add(btnView);

        lbSpace3.setText("     ");
        tbrSale.add(lbSpace3);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrSale.add(btnNew);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSale.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrSale.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSale.add(lblSpace52);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrSale.add(btnDelete);

        lbSpace2.setText("     ");
        tbrSale.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrSale.add(btnSave);

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSale.add(lblSpace53);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrSale.add(btnCancel);

        lblSpace3.setText("     ");
        tbrSale.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrSale.add(btnAuthorize);

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSale.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrSale.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSale.add(lblSpace55);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrSale.add(btnReject);

        lblSpace5.setText("     ");
        tbrSale.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrSale.add(btnPrint);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSale.add(lblSpace56);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrSale.add(btnClose);

        getContentPane().add(tbrSale);
        tbrSale.setBounds(0, 0, 700, 29);

        panRent.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panRent.setMaximumSize(new java.awt.Dimension(700, 400));
        panRent.setMinimumSize(new java.awt.Dimension(700, 400));
        panRent.setPreferredSize(new java.awt.Dimension(700, 400));
        panRent.setLayout(new java.awt.GridBagLayout());

        lblStatus1.setText("Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 145;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 92;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 40, 0, 0);
        panRent.add(lblStatus1, gridBagConstraints);

        btnLegalgpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLegalgpHead.setToolTipText("Search");
        btnLegalgpHead.setEnabled(false);
        btnLegalgpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLegalgpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panRent.add(btnLegalgpHead, gridBagConstraints);

        txtRentAccHead.setBackground(new java.awt.Color(220, 220, 220));
        txtRentAccHead.setEditable(false);
        txtRentAccHead.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 104;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        panRent.add(txtRentAccHead, gridBagConstraints);

        txtPenelAccHead.setBackground(new java.awt.Color(220, 220, 220));
        txtPenelAccHead.setEditable(false);
        txtPenelAccHead.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 104;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panRent.add(txtPenelAccHead, gridBagConstraints);

        txtLegalAccHead.setBackground(new java.awt.Color(220, 220, 220));
        txtLegalAccHead.setEditable(false);
        txtLegalAccHead.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.ipadx = 104;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panRent.add(txtLegalAccHead, gridBagConstraints);

        txtArbAccHead.setBackground(new java.awt.Color(220, 220, 220));
        txtArbAccHead.setEditable(false);
        txtArbAccHead.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.ipadx = 104;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panRent.add(txtArbAccHead, gridBagConstraints);

        txtCourtAccHead.setBackground(new java.awt.Color(220, 220, 220));
        txtCourtAccHead.setEditable(false);
        txtCourtAccHead.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridheight = 15;
        gridBagConstraints.ipadx = 104;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panRent.add(txtCourtAccHead, gridBagConstraints);

        txtExeAccHead.setBackground(new java.awt.Color(220, 220, 220));
        txtExeAccHead.setEditable(false);
        txtExeAccHead.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 56;
        gridBagConstraints.gridheight = 88;
        gridBagConstraints.ipadx = 104;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panRent.add(txtExeAccHead, gridBagConstraints);

        txtNoticeAccHead.setBackground(new java.awt.Color(220, 220, 220));
        txtNoticeAccHead.setEditable(false);
        txtNoticeAccHead.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 104;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panRent.add(txtNoticeAccHead, gridBagConstraints);

        btnArbgpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnArbgpHead.setToolTipText("Search");
        btnArbgpHead.setEnabled(false);
        btnArbgpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArbgpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panRent.add(btnArbgpHead, gridBagConstraints);

        btnCourtgpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCourtgpHead.setToolTipText("Search");
        btnCourtgpHead.setEnabled(false);
        btnCourtgpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCourtgpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridheight = 29;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panRent.add(btnCourtgpHead, gridBagConstraints);

        btnExegpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnExegpHead.setToolTipText("Search");
        btnExegpHead.setEnabled(false);
        btnExegpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExegpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 56;
        gridBagConstraints.gridheight = 89;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panRent.add(btnExegpHead, gridBagConstraints);

        btnRentAccHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnRentAccHd.setToolTipText("Search");
        btnRentAccHd.setEnabled(false);
        btnRentAccHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRentAccHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        panRent.add(btnRentAccHd, gridBagConstraints);

        btnPenelAccHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPenelAccHd.setToolTipText("Search");
        btnPenelAccHd.setEnabled(false);
        btnPenelAccHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPenelAccHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panRent.add(btnPenelAccHd, gridBagConstraints);

        btnNoticegpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnNoticegpHead.setToolTipText("Search");
        btnNoticegpHead.setEnabled(false);
        btnNoticegpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoticegpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panRent.add(btnNoticegpHead, gridBagConstraints);

        txtBuildingNo.setAllowAll(true);
        txtBuildingNo.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 154;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        panRent.add(txtBuildingNo, gridBagConstraints);

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Room No", "Version No", "Effective Date", "Rent Amount", "Rent Freq", "Penel Rate"
            }
        ));
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataMouseClicked(evt);
            }
        });
        cScrollPaneTable1.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 147;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.ipadx = 587;
        gridBagConstraints.ipady = 33;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 40, 30, 0);
        panRent.add(cScrollPaneTable1, gridBagConstraints);

        cScrollPane1.setViewportView(txtBuildingDes);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 137;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        panRent.add(cScrollPane1, gridBagConstraints);

        cboStatus1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "Vacant", "Occupied", "Damage " }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 145;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 86;
        gridBagConstraints.ipady = -1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panRent.add(cboStatus1, gridBagConstraints);

        panDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Details"));
        panDetails.setLayout(null);

        lblRoomNo.setText("Room Number ");
        panDetails.add(lblRoomNo);
        lblRoomNo.setBounds(20, 30, 130, 18);

        txtRoomNo.setAllowAll(true);
        txtRoomNo.setEnabled(false);
        txtRoomNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRoomNoActionPerformed(evt);
            }
        });
        panDetails.add(txtRoomNo);
        txtRoomNo.setBounds(150, 30, 140, 21);

        lblEffDate.setText("Effective Date");
        panDetails.add(lblEffDate);
        lblEffDate.setBounds(20, 60, 130, 18);

        tdtEffDate.setBackground(new java.awt.Color(220, 220, 220));
        panDetails.add(tdtEffDate);
        tdtEffDate.setBounds(150, 60, 101, 21);

        txtRentAmt.setEnabled(false);
        panDetails.add(txtRentAmt);
        txtRentAmt.setBounds(150, 90, 140, 21);

        cboRentFeq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "Daily", "Monthly", "Quarterly", "Halfyearly", "Yearly", "Lease" }));
        panDetails.add(cboRentFeq);
        cboRentFeq.setBounds(150, 120, 140, 21);

        txtPenelRate.setAllowNumber(true);
        txtPenelRate.setEnabled(false);
        panDetails.add(txtPenelRate);
        txtPenelRate.setBounds(150, 150, 140, 21);

        lblPenelRate.setText("Penel Rate");
        panDetails.add(lblPenelRate);
        lblPenelRate.setBounds(20, 150, 130, 18);

        lblRentFeq.setText("Rent Frequency");
        panDetails.add(lblRentFeq);
        lblRentFeq.setBounds(20, 120, 130, 18);

        lblRentAmt.setText("Rent Amount");
        panDetails.add(lblRentAmt);
        lblRentAmt.setBounds(20, 90, 130, 18);

        btnDelete1.setText("Delete");
        btnDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete1ActionPerformed(evt);
            }
        });
        panDetails.add(btnDelete1);
        btnDelete1.setBounds(210, 180, 80, 27);

        btnSave1.setText("Save");
        btnSave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave1ActionPerformed(evt);
            }
        });
        panDetails.add(btnSave1);
        btnSave1.setBounds(110, 180, 80, 27);

        btnAdd.setText("New");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        panDetails.add(btnAdd);
        btnAdd.setBounds(20, 180, 70, 27);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 14;
        gridBagConstraints.ipadx = 309;
        gridBagConstraints.ipady = 229;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 0, 0);
        panRent.add(panDetails, gridBagConstraints);

        lblBuildingNo.setText("Building Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 55;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 40, 0, 0);
        panRent.add(lblBuildingNo, gridBagConstraints);

        lblBuildingDes.setText("Building Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 35;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 40, 0, 0);
        panRent.add(lblBuildingDes, gridBagConstraints);

        lblRentAccHead.setText("Rent Account head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 39;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 40, 0, 0);
        panRent.add(lblRentAccHead, gridBagConstraints);

        lblPenelAccHead.setText("Penel Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 31;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 40, 0, 0);
        panRent.add(lblPenelAccHead, gridBagConstraints);

        lblNoticeAccHead.setText("Notice Account head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 29;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 40, 0, 0);
        panRent.add(lblNoticeAccHead, gridBagConstraints);

        lblLegalAccHead.setText("Legal Account head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 35;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 40, 0, 0);
        panRent.add(lblLegalAccHead, gridBagConstraints);

        lblArbAccHead.setText("Arb Account head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 46;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 40, 0, 0);
        panRent.add(lblArbAccHead, gridBagConstraints);

        lblCourtAccHead.setText("Court Account head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 15;
        gridBagConstraints.ipadx = 35;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 40, 0, 0);
        panRent.add(lblCourtAccHead, gridBagConstraints);

        lblExeAccHead.setText("Exec Account head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 56;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 88;
        gridBagConstraints.ipadx = 36;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 40, 0, 0);
        panRent.add(lblExeAccHead, gridBagConstraints);

        lblAdvHead.setText("Advance Rent head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 27;
        gridBagConstraints.gridheight = 30;
        gridBagConstraints.ipadx = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 40, 0, 0);
        panRent.add(lblAdvHead, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 27;
        gridBagConstraints.gridheight = 58;
        gridBagConstraints.ipadx = 104;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        panRent.add(txtAdvHead, gridBagConstraints);

        btnAdvHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAdvHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdvHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 27;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.gridheight = 87;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        panRent.add(btnAdvHead, gridBagConstraints);

        getContentPane().add(panRent);
        panRent.setBounds(0, 40, 700, 410);

        panStatus.setMinimumSize(new java.awt.Dimension(250, 22));
        panStatus.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus);
        panStatus.setBounds(322, 794, 0, 0);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        getContentPane().add(lblStatus);
        lblStatus.setBounds(70, 460, 92, 22);

        lblSpace.setText(" Status :");
        getContentPane().add(lblSpace);
        lblSpace.setBounds(10, 460, 50, 22);

        mbrTokenConfig.setInheritsPopupMenu(true);

        mnuProcess.setText("Rent Profile");
        mnuProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProcessActionPerformed(evt);
            }
        });

        mitNew.setText("New");
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptView);

        mitSave.setText("Save");
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mnuProcess.add(mitClose);

        mbrTokenConfig.add(mnuProcess);

        setJMenuBar(mbrTokenConfig);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseClicked
        // TODO add your handling code here:
        
           int row=tblData.rowAtPoint(evt.getPoint());

            int col= tblData.columnAtPoint(evt.getPoint());
            if(col>0)
            {
                isEditTable=true;
            //System.out.println("R= "+row +" C="+col);
            //JOptionPane.showMessageDialog(null,tblData.getValueAt(row,col).toString());
            String rmNo=tblData.getValueAt(row,0).toString();
           // String versionNo=tblData.getValueAt(row,1).toString();
            String effDate=tblData.getValueAt(row,1).toString();
            String rentAmt=tblData.getValueAt(row,2).toString();
            String rentFrq=tblData.getValueAt(row,3).toString(); 
            String penelRate=tblData.getValueAt(row,4).toString();
             rdDetilsId=tblData.getValueAt(row,5).toString();  
            if(rmNo!=null && !rmNo.equalsIgnoreCase(""))
            {
            txtRoomNo.setText(rmNo);
            }
           // if(versionNo!=null && !versionNo.equalsIgnoreCase(""))
           // {
          //  txtVersNo.setText(versionNo);
         //   }
            if(effDate!=null && !effDate.equalsIgnoreCase(""))
            {
             tdtEffDate.setDateValue(effDate);
            }
             if(rentAmt!=null && !rentAmt.equalsIgnoreCase(""))
            {
            txtRentAmt.setText(rentAmt);
            }
             if(rentFrq!=null && !rentFrq.equalsIgnoreCase(""))
            {
            cboRentFeq.setSelectedItem(rentFrq);
            }
              if(penelRate!=null && !penelRate.equalsIgnoreCase(""))
            {
            txtPenelRate.setText(penelRate);
            }
            btnAdd.setEnabled(true);
            btnSave1.setEnabled(true);
            btnDelete1.setEnabled(true);
            txtRoomNo.setEnabled(true);
            txtRentAmt.setEnabled(true);
            tdtEffDate.setEnabled(true);
            txtRentAmt.setEnabled(true);
            cboRentFeq.setEnabled(true);
            txtPenelRate.setEnabled(true);
            }
    }//GEN-LAST:event_tblDataMouseClicked

    private void btnDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete1ActionPerformed
        // TODO add your handling code here:
         clearFields();
          panDetails.setEnabled(true);
        txtRoomNo.setEnabled(true);
      //  txtVersNo.setEnabled(false);
        tdtEffDate.setEnabled(true);
        txtRentAmt.setEnabled(true);
        cboRentFeq.setEnabled(true);
        txtPenelRate.setEnabled(true);
        
        btnAdd.setEnabled(true);
        btnSave1.setEnabled(true);
        btnDelete1.setEnabled(false);
        disableGrpHead();
        isEditTable=false;
        generateDelete();
        
    }//GEN-LAST:event_btnDelete1ActionPerformed
 private void generateDelete()
 {
     try
     {
         int row=tblData.getSelectedRow();
         model.removeRow(row);
          HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("RDID", rdDetilsId);
            singleAuthorizeMap.put("STATUS",CommonConstants.STATUS_DELETED);
            ClientUtil.execute("deleteRentdetails", singleAuthorizeMap);
            fillData1(observable.getTxtRMNo());
     }
     catch(Exception e)
     {
         e.printStackTrace();
     }
 }
    private void disableGrpHead()
   {
        txtRentAccHead.setEnabled(false);
        txtPenelAccHead.setEnabled(false);
        txtNoticeAccHead.setEnabled(false);
        txtLegalAccHead.setEnabled(false);
        txtArbAccHead.setEnabled(false);
        txtCourtAccHead.setEnabled(false);
        txtExeAccHead.setEnabled(false);
        txtAdvHead.setEnabled(false);
   }
  
    private void btnSave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave1ActionPerformed
        // TODO add your handling code here:
        if(txtRoomNo.getText().equals(""))
        {
            displayAlert("Room Number should not be empty!!!");
            txtRoomNo.requestFocus();
            return;
        }
        if(tdtEffDate.getDateValue()==null || tdtEffDate.getDateValue().equals(""))
        {
            displayAlert("Effective Date should not be empty!!!");
            tdtEffDate.requestFocus();
            return;
        }
        if(txtRentAmt.getText().equals(""))
        {
            displayAlert("Rent Amount should not be empty!!!");
            txtRentAmt.requestFocus();
            return;
        }
        if(cboRentFeq.getSelectedItem().equals("Select"))
        {
            displayAlert("Rent Frequency should not be empty!!!");
            cboRentFeq.requestFocus();
            return;
        }
         if(txtPenelRate.getText().equals(""))
        {
            displayAlert("Penel Rate should not be empty!!!");
            txtPenelRate.requestFocus();
            return;
        }
        if(!checkNumber(txtPenelRate.getText()))
        {
             displayAlert("Penel Rate should be a number!!!");
             txtPenelRate.setText("");
             txtPenelRate.requestFocus();
            return;
        }
        if(isEditTable)
        {
           int selCol= tblData.getSelectedColumn();
           int selrow= tblData.getSelectedRow();  
        //   System.out.println("selCol === "+selCol +" selrow==" +selrow +"  iiii=="+txtRentAmt.getText());
            model.setValueAt(txtRoomNo.getText(), selrow, 0);
            model.setValueAt(tdtEffDate.getDateValue(),selrow, 1);
            model.setValueAt(txtRentAmt.getText(),selrow, 2);
            model.setValueAt(cboRentFeq.getSelectedItem().toString(),selrow, 3);
            model.setValueAt(txtPenelRate.getText(),selrow, 4);
            isEditTable=false;
        }
        else
        {
             model.addRow(new String[]{txtRoomNo.getText(),tdtEffDate.getDateValue(),txtRentAmt.getText(),cboRentFeq.getSelectedItem().toString(),txtPenelRate.getText(),""}); 
                tblData.getColumnModel().getColumn(5).setMaxWidth(0);
                tblData.getColumnModel().getColumn(5).setMinWidth(0);
                tblData.getColumnModel().getColumn(5).setPreferredWidth(0);
        }
         clearFields();
           panDetails.setEnabled(false);
        txtRoomNo.setEnabled(false);
        //txtVersNo.setEnabled(false);
        tdtEffDate.setEnabled(false);
        txtRentAmt.setEnabled(false);
        cboRentFeq.setEnabled(false);
        txtPenelRate.setEnabled(false);
         btnAdd.setEnabled(true);
         btnSave1.setEnabled(false);
         btnDelete1.setEnabled(false);
         disableGrpHead();
    }//GEN-LAST:event_btnSave1ActionPerformed
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        clearFields();
          panDetails.setEnabled(true);
        txtRoomNo.setEnabled(true);
      //  txtVersNo.setEnabled(false);
        tdtEffDate.setEnabled(true);
        txtRentAmt.setEnabled(true);
        cboRentFeq.setEnabled(true);
        txtPenelRate.setEnabled(true);
        
        btnAdd.setEnabled(true);
        btnSave1.setEnabled(true);
        btnDelete1.setEnabled(false);
        disableGrpHead();
        isEditTable=false;
    }//GEN-LAST:event_btnAddActionPerformed
    public void clearFields()
    {
        txtRoomNo.setText("");
        tdtEffDate.setDateValue("");
        txtRentAmt.setText("");
        cboRentFeq.setSelectedIndex(0);
        txtPenelRate.setText("");
        disableGrpHead();
        
    }
    private void btnExegpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExegpHeadActionPerformed
        // TODO add your handling code here:
         callView("EXE_GROUP_HEAD");
    }//GEN-LAST:event_btnExegpHeadActionPerformed

    private void btnCourtgpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCourtgpHeadActionPerformed
        // TODO add your handling code here:
         callView("COURT_GROUP_HEAD");
    }//GEN-LAST:event_btnCourtgpHeadActionPerformed

    private void btnArbgpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArbgpHeadActionPerformed
        // TODO add your handling code here:
         callView("ARB_GROUP_HEAD");
    }//GEN-LAST:event_btnArbgpHeadActionPerformed

    private void btnLegalgpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLegalgpHeadActionPerformed
        // TODO add your handling code here:
         callView("LEGAL_GROUP_HEAD");
    }//GEN-LAST:event_btnLegalgpHeadActionPerformed

    private void btnNoticegpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoticegpHeadActionPerformed
        // TODO add your handling code here:
         callView("NOTICE_GROUP_HEAD");
    }//GEN-LAST:event_btnNoticegpHeadActionPerformed

    private void btnPenelAccHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPenelAccHdActionPerformed
        // TODO add your handling code here:
         callView("PENEL_GROUP_HEAD");
    }//GEN-LAST:event_btnPenelAccHdActionPerformed

    private void btnRentAccHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRentAccHdActionPerformed
        // TODO add your handling code here:
          callView("RENT_GROUP_HEAD");
    }//GEN-LAST:event_btnRentAccHdActionPerformed

    private void txtRoomNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRoomNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRoomNoActionPerformed

    
     public static void main(String args[]) throws Exception {
          try {
     //   frmSale objIfrRenewal=new frmSale();
    //    objIfrRenewal.setVisible(true);
         }catch (Exception e){
            e.printStackTrace();
        }
    }
     
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
       cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
         // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        // txtSalesmanID.setEnabled(false);
         setButtons(false);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
      //   txtSalesmanID.setEnabled(false);
        disableGrpHead();
        setButtons(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
       disableGrpHead();
       setButtons(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
  public void authorizeStatus(String authorizeStatus) {
      //System.out.println("viewType========"+viewType);
      
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID,TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put("STATUS_BY", TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
           mapParam.put(CommonConstants.MAP_NAME, "getRentAuthorizeList");
            System.out.println("mapParam====="+mapParam);
//            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeRent");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            AuthorizeStatusUI fdf=new AuthorizeStatusUI(this,mapParam);
            authorizeUI.show();
            observable.setStatus();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)){
            
           /* HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put("AUTHORIZED_BY",TrueTransactMain.USER_ID);//commented on 13-Feb-2012
            List  aList=ClientUtil.executeQuery("getCountForAuth", singleAuthorizeMap);
             for(int i=0;i<aList.size();i++) {
                    HashMap map=(HashMap)aList.get(i);
                    if(map.get("COUNT")!=null) {
                        int j=Integer.parseInt(map.get("COUNT").toString());
                        if(j!=0)
                        {
                            displayAlert("Error");
                            return;
                        }
                    }
             }*/
            
            
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID,TrueTransactMain.USER_ID);//commented on 13-Feb-2012
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt);
            singleAuthorizeMap.put("RMNUMBER", observable.getTxtRMNo());
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
           // System.out.println("IOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            ClientUtil.execute("authorizeRent", singleAuthorizeMap);  
            ClientUtil.execute("authorizeRentDetails", singleAuthorizeMap);
           // ClientUtil.ex
            viewType = "";

            btnCancelActionPerformed(null);
   
        }
      disableGrpHead();
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
     //     observable.resetForm();
     setModified(false);
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panRent, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
              model.setRowCount(0);
              setButtons(false);
        
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
       //   System.out.println("IN btnSaveActionPerformed");
        setModified(false);
          savePerformed();
       //    System.out.println("IN btnSaveActionPerformed111");
          
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
     //    txtSalesmanID.setEnabled(false);
        disableGrpHead();
       
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed(){
       
       // System.out.println("IN savePerformed");
        String action;
      //    System.out.println("IN observable.getActionType(): "+observable.getActionType());
     //      System.out.println("IN ClientConstants.ACTIONTYPE_NEW: "+ClientConstants.ACTIONTYPE_NEW);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW )
        {
      
            action=CommonConstants.TOSTATUS_INSERT;
            saveAction(action);
          
        }
        else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            action=CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        }
        else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            action=CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
       //  txtSalesmanID.setEnabled(false);
    }
    public boolean checkNumber(String value)
    {
        //String amtRentIn=amountRentText.getText();
        boolean incorrect = true;
        try
        {
            Double.parseDouble(value);
            return true;
        }
        catch(NumberFormatException nfe)
        {
          return false;
        }
       // return 
    }
   
        /* Calls the execute method of TerminalOB to do insertion or updation or deletion */
    private void saveAction(String status){
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
      System.out.println("status saveAction11111: "+status);

        final String mandatoryMessage = checkMandatory(panRent);
        StringBuffer message = new StringBuffer(mandatoryMessage);
        if(txtBuildingNo.getText().equals(""))
        {
            message.append(objMandatoryRB.getString("txtBuildingNo"));
        }
        if(cm.getSplCharsCheck(txtBuildingNo.getText()))
        {
            message.append(objMandatoryRB.getString("splCharsCheck")); 
            txtBuildingNo.requestFocus();
            txtBuildingNo.setText("");
           // return;
        }
        if(txtBuildingDes.getText().equals(""))
        {
                message.append(objMandatoryRB.getString("txtaBuildingDes"));
         }
         if(txtRentAccHead.getText().equals(""))
        {
                message.append(objMandatoryRB.getString("txtRentAccHead"));
         }
        if(txtPenelAccHead.getText().equals(""))
        {
                message.append(objMandatoryRB.getString("txtPenelAccHead"));
        }
        if(txtNoticeAccHead.getText().equals(""))
        {
                message.append(objMandatoryRB.getString("txtNoticeAccHead"));
         }
     if(txtLegalAccHead.getText().equals(""))
        {
                message.append(objMandatoryRB.getString("txtLegalAccHead"));
         }
        if(txtArbAccHead.getText().equals(""))
        {
                message.append(objMandatoryRB.getString("txtArbAccHead"));
         }
      
        if(txtCourtAccHead.getText().equals(""))
        {
                message.append(objMandatoryRB.getString("txtCourtGrpHead"));
         }
        if(txtExeAccHead.getText().equals(""))
        {
                message.append(objMandatoryRB.getString("txtExeGrpHead"));
         }
        if(cboStatus1.getSelectedItem().equals("Select") || cboStatus1.getSelectedItem()==null)
        {
                message.append(objMandatoryRB.getString("cboStatus"));
         }
         if(txtAdvHead.getText().equals(""))
         {
         message.append(objMandatoryRB.getString("txtAdvHead"));
         }
       
        System.out.println("ob txt buil"+observable.getTxtBuildingNo()+" txtbulid "+txtBuildingNo.getText());
        
       //Building No duplication check
         if(status=="INSERT" || (status=="UPDATE" && !observable.getTxtBuildingNo().equals(txtBuildingNo.getText())))
         {
             HashMap singleAuthorizeMap = new HashMap();
             singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt);
             singleAuthorizeMap.put("BUILDING_NUM", txtBuildingNo.getText());
             List aList= ClientUtil.executeQuery("getBuildingNoCount", singleAuthorizeMap);
             int count=0;
             for(int i=0;i<aList.size();i++)
             {
                HashMap map=(HashMap)aList.get(i);
                if(map.get("COUNT")!=null)
                {
                  count=Integer.parseInt(map.get("COUNT").toString());
                }
                if(count!=0)
                {
                    displayAlert("Building No already exists!!!");
                    txtBuildingNo.requestFocus();
                    txtBuildingNo.setText("");
                    return;
                }
             }
         }
        
        //
//       if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW)
//         {
        if(message.length() > 0 ){
            displayAlert(message.toString());
       }
        
        else if(model.getRowCount()<1)
        {
            displayAlert("Enter Details!!!"); 
        }
        else if(btnSave1.isEnabled())
        {
             displayAlert("Please save your changes by clicking on 'Save' in details!!!");
             return;
        }
//         }
        
//         else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT)
//         {
//        
//         if(txtRoomNo.getText().equals(""))
//        {
//            displayAlert("Room Number should not be empty!!!");
//            txtRoomNo.requestFocus();
//            return;
//        }
//        if(tdtEffDate.getDateValue()==null || tdtEffDate.getDateValue().equals(""))
//        {
//            displayAlert("Effective Date should not be empty!!!");
//            tdtEffDate.requestFocus();
//            return;
//        }
//        if(txtRentAmt.getText().equals(""))
//        {
//            displayAlert("Rent Amount should not be empty!!!");
//            txtRentAmt.requestFocus();
//            return;
//        }
//        if(cboRentFeq.getSelectedItem().equals("Select"))
//        {
//            displayAlert("Rent Frequency should not be empty!!!");
//            cboRentFeq.requestFocus();
//            return;
//        }
//         if(txtPenelRate.getText().equals(""))
//        {
//            displayAlert("Penel Rate should not be empty!!!");
//            txtPenelRate.requestFocus();
//            return;
//        }
//        if(!checkNumber(txtPenelRate.getText()))
//        {
//             displayAlert("Penel Rate should be a number!!!");
//             txtPenelRate.setText("");
//             txtPenelRate.requestFocus();
//            return;
//        }
//        
//       }
        
        else{
                updateOBFields();
            observable.execute(status);
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
               HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("RMNUMBER");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
          
                if (status==CommonConstants.TOSTATUS_UPDATE) {
                    lockMap.put("RMNUMBER", observable.getRMNo());
                }
         
                settings();
            }
        }
            
    }
        /** Method used to check whether the Mandatory Fields in the Form are Filled or not */
    private String checkMandatory(javax.swing.JComponent component){
//          return new MandatoryCheck().checkMandatory(getClass().getName(), component, getMandatoryHashMap());
         return "";
      //validation error
    }
    
    /** Method used to Give a Alert when any Mandatory Field is not filled by the user */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetForm();
      //  txtNoOfTokens.setText("");
       ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panRent, false);
        setButtonEnableDisable();
        observable.setResultStatus();
             model.setRowCount(0);
     //    txtSalesmanID.setEnabled(false);
             disableGrpHead();
    }
    public Date getDateValue(String date1)
    {
         DateFormat formatter ; 
         Date date=null ; 
        try 
        {  
           // String str_date=date1;
          //  formatter = new SimpleDateFormat("MM/dd/yyyy");
          //  date = (Date)formatter.parse(str_date);  
          //      System.out.println("dateAFETRRR 66666666666=========:"+date); 
                
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
                   // String s1 = "2008-03-30T15:30:00.000+02:00";
                   // date1 = date1.substring(0, date1.indexOf('.'));
                    System.out.println("Result==> "+sdf2.format(sdf1.parse(date1)));
                  date=new Date(sdf2.format(sdf1.parse(date1)));
               //System.out.println("date IOOOOOOO==> "+date);
        }
        catch (ParseException e)
        {
           System.out.println("Error in getDateValue():"+e);
        }  
            return date;
       }
        public void updateOBFields() {
            
            ArrayList aList =new ArrayList();
            
          //   System.out.println("KOOOOO===="+    model.getDataVector());
            
          //  observable.setTxtRMNo("");
            observable. setTxtBuildingNo(txtBuildingNo.getText());
             observable.setTxtaBuildingDes(txtBuildingDes.getText());
             observable.setTxtRentAccHead(txtRentAccHead.getText());
             observable.setTxtPenelAccHead(txtPenelAccHead.getText());
             observable.setTxtNoticeAccHead(txtNoticeAccHead.getText());
             observable.setTxtLegalAccHead(txtLegalAccHead.getText());
             observable.setTxtArbAccHead(txtArbAccHead.getText() );
             observable.setTxtCourtGrpHead(txtCourtAccHead.getText());
             observable.setTxtExeGrpHead(txtExeAccHead.getText());
             observable.setCboStatus(cboStatus1.getSelectedItem().toString());
             observable.setDataV(model.getDataVector());
             observable.setTxtAdvHead(txtAdvHead.getText());
            observable.setModule(getModule());
            observable.setScreen(getScreen());
            observable.setModule(getModule());
            observable.setScreen(getScreen());
            //observable.set
            
        //     txtSalesmanID.setEnabled(false);
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
       // observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView( ClientConstants.ACTION_STATUS[3]);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
         disableGrpHead();
         setButtons(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        callView(ClientConstants.ACTION_STATUS[2]);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
         btnAdd.setEnabled(true);
      disableGrpHead();
      setButtons(true);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
         observable.resetForm();
       
        ClientUtil.enableDisable(panRent, true);
       
        // System.out.println("d ACTION STA: "+ClientConstants.ACTIONTYPE_NEW);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
       
        panDetails.setEnabled(false);
        txtRoomNo.setEnabled(false);
     //   txtVersNo.setEnabled(false);
        tdtEffDate.setEnabled(false);
        txtRentAmt.setEnabled(false);
        cboRentFeq.setEnabled(false);
        txtPenelRate.setEnabled(false);
         btnAdd.setEnabled(true);
         disableGrpHead();
         setButtons(true);
    }//GEN-LAST:event_btnNewActionPerformed
   private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnSave.setEnabled(!btnSave.isEnabled());
        btnCancel.setEnabled(!btnCancel.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
       btnView.setEnabled(!btnView.isEnabled());
       btnAdvHead.setEnabled(!btnAdvHead.isEnabled());
        btnAdd.setEnabled(false);
        btnSave1.setEnabled(false);
        btnDelete1.setEnabled(false);
        lblStatus.setText(observable.getLblStatus());
        disableGrpHead();
        setButtons(false);
       
    }
    private void mnuProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuProcessActionPerformed
private void btnAdvHeadActionPerformed(java.awt.event.ActionEvent evt) {                                           
     callView("ADVANCE_RENT_HEAD");
    }                                          
  
      
     public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtBuildingNo", new Boolean(true));
        mandatoryMap.put("txtBuildingDes", new Boolean(true));
        mandatoryMap.put("txtRentAccHead", new Boolean(true));
        mandatoryMap.put("txtPenelAccHead", new Boolean(true));
        mandatoryMap.put("txtNoticeAccHead", new Boolean(true));
        mandatoryMap.put("txtLegalAccHead", new Boolean(true));
        mandatoryMap.put("txtArbAccHead", new Boolean(true));
        mandatoryMap.put("txtCourtAccHead", new Boolean(true)); 
        mandatoryMap.put("txtExeAccHead", new Boolean(true));
        mandatoryMap.put("cboStatus1", new Boolean(true));
        mandatoryMap.put("txtRoomNo", new Boolean(true));
        mandatoryMap.put("tdtEffDate", new Boolean(true));
        mandatoryMap.put("txtRentAmt", new Boolean(true));
        mandatoryMap.put("txtRentFeq", new Boolean(true));
        mandatoryMap.put("txtPenelRate", new Boolean(true));
        mandatoryMap.put("cboRentFeq", new Boolean(true));
         mandatoryMap.put("txtAdvHead", new Boolean(true));
          
    }
    public java.util.HashMap getMandatoryHashMap() {
           return mandatoryMap;
    }    
    public String getDtPrintValue(String strDate)
    {
         try
         {
         //    System.out.println("strDate getDtPrintValue99999999999999999999999999999999999999================="+strDate);
        //create SimpleDateFormat object with source string date format
        //---SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //parse the string into Date object
        //Date date = null;
        //create SimpleDateFormat object with desired date format
        //SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
        //parse the date into another format
        //date = sdfDestination.parse(strDate);        
        //String ds1 = "2007-06-30";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        strDate = sdf2.format(sdf1.parse(strDate));
        System.out.println("strDate%#%#%#"+strDate);
        }
         catch(Exception e)
         {
            e.printStackTrace(); 
         }
        return strDate;
    }
   /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
               //  getTxtRoomNo
                txtBuildingNo.setText(observable.getTxtBuildingNo());
                txtBuildingDes.setText(observable.getTxtaBuildingDes());
                txtRentAccHead.setText(observable.getTxtRentAccHead());
                txtPenelAccHead.setText(observable.getTxtPenelAccHead());
                txtNoticeAccHead.setText(observable.getTxtNoticeAccHead());
                txtLegalAccHead.setText(observable.getTxtLegalAccHead());
                txtArbAccHead.setText(observable.getTxtArbAccHead());
                txtCourtAccHead.setText(observable.getTxtCourtGrpHead());
                txtExeAccHead.setText(observable.getTxtExeGrpHead());
                txtAdvHead.setText(observable.getTxtAdvHead());
                cboStatus1.setSelectedItem(observable.getCboStatus());
      
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAdd;
    private com.see.truetransact.uicomponent.CButton btnAdvHead;
    private com.see.truetransact.uicomponent.CButton btnArbgpHead;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCourtgpHead;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDelete1;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnExegpHead;
    private com.see.truetransact.uicomponent.CButton btnLegalgpHead;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNoticegpHead;
    private com.see.truetransact.uicomponent.CButton btnPenelAccHd;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnRentAccHd;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSave1;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CScrollPaneTable cScrollPaneTable1;
    private com.see.truetransact.uicomponent.CComboBox cboRentFeq;
    private com.see.truetransact.uicomponent.CComboBox cboStatus1;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblAdvHead;
    private com.see.truetransact.uicomponent.CLabel lblArbAccHead;
    private com.see.truetransact.uicomponent.CLabel lblBuildingDes;
    private com.see.truetransact.uicomponent.CLabel lblBuildingNo;
    private com.see.truetransact.uicomponent.CLabel lblCourtAccHead;
    private com.see.truetransact.uicomponent.CLabel lblEffDate;
    private com.see.truetransact.uicomponent.CLabel lblExeAccHead;
    private com.see.truetransact.uicomponent.CLabel lblLegalAccHead;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNoticeAccHead;
    private com.see.truetransact.uicomponent.CLabel lblPenelAccHead;
    private com.see.truetransact.uicomponent.CLabel lblPenelRate;
    private com.see.truetransact.uicomponent.CLabel lblRentAccHead;
    private com.see.truetransact.uicomponent.CLabel lblRentAmt;
    private com.see.truetransact.uicomponent.CLabel lblRentFeq;
    private com.see.truetransact.uicomponent.CLabel lblRoomNo;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace52;
    private com.see.truetransact.uicomponent.CLabel lblSpace53;
    private com.see.truetransact.uicomponent.CLabel lblSpace54;
    private com.see.truetransact.uicomponent.CLabel lblSpace55;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panDetails;
    private com.see.truetransact.uicomponent.CPanel panRent;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CToolBar tbrSale;
    private com.see.truetransact.uicomponent.CDateField tdtEffDate;
    private com.see.truetransact.uicomponent.CTextField txtAdvHead;
    private com.see.truetransact.uicomponent.CTextField txtArbAccHead;
    private com.see.truetransact.uicomponent.CTextArea txtBuildingDes;
    private com.see.truetransact.uicomponent.CTextField txtBuildingNo;
    private com.see.truetransact.uicomponent.CTextField txtCourtAccHead;
    private com.see.truetransact.uicomponent.CTextField txtExeAccHead;
    private com.see.truetransact.uicomponent.CTextField txtLegalAccHead;
    private com.see.truetransact.uicomponent.CTextField txtNoticeAccHead;
    private com.see.truetransact.uicomponent.CTextField txtPenelAccHead;
    private com.see.truetransact.uicomponent.CTextField txtPenelRate;
    private com.see.truetransact.uicomponent.CTextField txtRentAccHead;
    private com.see.truetransact.uicomponent.CTextField txtRentAmt;
    private com.see.truetransact.uicomponent.CTextField txtRoomNo;
    // End of variables declaration//GEN-END:variables
//     private com.see.truetransact.uicomponent.CTable tblData;
      private com.see.truetransact.clientutil.TableModel tbModel;
}
