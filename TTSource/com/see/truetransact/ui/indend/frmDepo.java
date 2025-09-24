/*
* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * frmDepo.java
 *
 * Created on September 12, 2011, 12:08 PM
 */
package com.see.truetransact.ui.indend;

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
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import java.util.Date;

/**
 *
 * @author userdd
 */
public class frmDepo extends CInternalFrame implements Observer, UIMandatoryField {

    private DepoOB observable;
    private String[][] tabledata;
    private String[] column;
    private DepoMRB objMandatoryRB = new DepoMRB();//Instance for the MandatoryResourceBundle
    private DefaultTableModel model;
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private final String AUTHORIZE = "Authorize";//Variable used when btnAuthorize is clicked
    public String gen_trans = "";
    private Date currDt = null;

    /**
     * Creates new form ifrNewBorrowing
     */
    public frmDepo() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setFieldNames();
        setObservable();
        observable.resetForm();
        setMaxLengths();
        // observable = new IndendOB();
        initComponentData();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panDepo, getMandatoryHashMap());
        ClientUtil.enableDisable(panDepo, false);
        setButtonEnableDisable();
        txtDepoId.setEnabled(false);

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
        //   lblMsg.setName("lblMsg");
        tbrDepo.setName("tbrDepo");
        lblStoreNo.setName("lblStoreNo");
        lblDepoId.setName("lblDepoId");
        lblSalesmanid.setName("lblSalesmanid");
        lblName.setName("lblName");
        lblOpngStock.setName("lblOpngStock");
        lblStockasonDate.setName("lblStockasonDate");
        lblRemarks.setName("lblRemarks");
        lblSalesgpHead.setName("lblSalesgpHead");
        lblPurchasegpHead.setName("lblPurchasegpHead");
        lblSalesReturngpHead.setName("lblSalesReturngpHead");
        lblPurchaseReturngpHead.setName("lblPurchaseReturngpHead");
        lblDeficiategpHead.setName("lblDeficiategpHead");
        lblDamagegpHead.setName("lblDamagegpHead");
        lblOpngVatStock.setName("lblOpngVatStock");
        txtOpngVatStock.setName("txtOpngVatStock");
        txtStoreNo.setName("txtStoreNo");
        txtSalesmanid.setName("txtSalesmanid");
        txtDepoId.setName("txtDepoId");
        txtName.setName("txtName");
        txtOpngStock.setName("txtOpngStock");
        tdtStockasonDate.setName("tdtStockasonDate");
        txaRemarks.setName("txaRemarks");
        txtSalesgpHead.setName("txtSalesgpHead");
        txtPurchasegpHead.setName("txtPurchasegpHead");
        txtSalesReturngpHead.setName("txtSalesReturngpHead");
        txtPurchaseReturngpHead.setName("txtPurchaseReturngpHead");
        txtDeficiategpHead.setName("txtDeficiategpHead");
        txtDamagegpHead.setName("txtDamagegpHead");
        btnSalesgpHead.setName("btnSalesgpHead");
        btnPurchasegpHead.setName("btnPurchasegpHead");
        btnSalesReturngpHead.setName("btnSalesReturngpHead");
        btnPurchasegpReturnHead.setName("btnPurchasegpReturnHead");
        btnDeficiategpHead.setName("btnDeficiategpHead");
        btnDamagegpHead.setName("btnDamagegpHead");
    }

    private void setMaxLengths() {
        //txtPurAmount.setValidation(new CurrencyValidation());
        txtOpngVatStock.setValidation(new NumericValidation(16, 2));
        txtProfitPercentage.setValidation(new NumericValidation(5, 2));
    }

    private void setObservable() {
        try {
            observable = DepoOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            //parseException.logException(e,true);
            System.out.println("Error in setObservable():" + e);
        }
    }

    public void setButtons(boolean flag) {
        btnStore.setEnabled(flag);
        btnSalesmanid.setEnabled(flag);
        btnSalesgpHead.setEnabled(flag);
        btnPurchasegpHead.setEnabled(flag);
        btnSalesReturngpHead.setEnabled(flag);
        btnPurchasegpReturnHead.setEnabled(flag);
        btnDeficiategpHead.setEnabled(flag);
        btnDamagegpHead.setEnabled(flag);
        btnSeviceTaxgpHead.setEnabled(flag);
        btnComRecievegpHead.setEnabled(flag);
        btnMisIncomgpHead.setEnabled(flag);
        btnComRecievegpHead.setEnabled(flag);
        btnPurchaseVatTaxgpHead.setEnabled(flag);
        btnStockHd.setEnabled(flag);
        btnPurchaseReturnVatgpHead.setEnabled(flag);
        btnSaleVatgpHead.setEnabled(flag);
        btnSaleRetrnVatgpHead.setEnabled(flag);
        btnDamageVatgpHead.setEnabled(flag);
        btnDeficitVatgpHead.setEnabled(flag);
        btnSeviceTaxgpHead.setEnabled(flag);
        btnOtherExpensegpHead.setEnabled(flag);
        btnDiscountGoupHead.setEnabled(flag); // Added by nithya on 02-04-2020 for KD-1732
        btnDiscountVatGroupHead.setEnabled(flag);
    }

    private void initComponentData() {

        try {
            // cboDepoId.setModel(observable.getCbmDepoId());
            /*    List aList=ClientUtil.executeQuery("Depo.getSales",new HashMap()); 
             cboSalesmanId.addItem(""); 
             for(int i=0;i<aList.size();i++)
             {
             HashMap map=(HashMap)aList.get(i);
             String Id=map.get("SMID").toString();
             String Name=map.get("NAME").toString();
             if(Id!=null)
             {
             cboSalesmanId.addItem(Name); 
             }
             }
                 
             List aList1=ClientUtil.executeQuery("Depo.getStores",new HashMap()); 
             cboStoreNo.addItem(""); 
             for(int i=0;i<aList1.size();i++)
             {
             HashMap map1=(HashMap)aList1.get(i);
             String Id=map1.get("STNUMBER").toString();
             String Name=map1.get("NAME").toString();
             if(Id!=null)
             {
             cboStoreNo.addItem(Name); 
             }
             }*/
        } catch (ClassCastException e) {
            //parseException.logException(e,true);
            System.out.println("Error in initComponentData():" + e);
        }

    }

    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
        viewType = currField;
        ArrayList lst = new ArrayList();
        HashMap viewMap = new HashMap();

        if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)
                || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
            viewMap.put(CommonConstants.MAP_NAME, "Depo.getSelectDepoList");
            lst.add("DEPID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
        } else if (viewType.equals("STORE")) {
            viewMap.put(CommonConstants.MAP_NAME, "Depo.getStores");
            lst.add("STORE");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
        } else if (viewType.equals("SALES")) {
            viewMap.put(CommonConstants.MAP_NAME, "Depo.getSales");
            lst.add("SALES");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
        } else {
            viewMap.put(CommonConstants.MAP_NAME, "Borrowings.getSelectAcctHeadTOList");
        }
        HashMap where = new HashMap();
        where.put("BRANCH_ID", "0001");
        viewMap.put(CommonConstants.MAP_WHERE, where);
        where = null;
        new ViewAll(this, viewMap).show();

    }

    public void fillData(Object map) {

//        setModified(true);

        HashMap hash = (HashMap) map;
        if (viewType.equals("OTHEREXP_TAX_GROUP_HEAD")) {
            txtOtherExpnsgpHead.setText(hash.get("AC_HD_ID").toString());
        }
        if (viewType.equals("DEFICITVAT_TAX_GROUP_HEAD")) {
            txtDeficitVatgpHead.setText(hash.get("AC_HD_ID").toString());
        }
        if (viewType.equals("DMGVAT_TAX_GROUP_HEAD")) {
            txtDamageVatgpHead.setText(hash.get("AC_HD_ID").toString());
        }
        if (viewType.equals("SALESRETN_TAX_GROUP_HEAD")) {
            txtSaleRetrnVatTaxgpHead.setText(hash.get("AC_HD_ID").toString());
        }
        if (viewType.equals("SALESVAT_TAX_GROUP_HEAD")) {
            txtSaleVatTaxgpHead.setText(hash.get("AC_HD_ID").toString());
        }
        if (viewType.equals("VAT_TAX_GROUP_HEAD")) {
            txtComReciedgpHd.setText(hash.get("AC_HD_ID").toString());
        }
        if (viewType.equals("PURRETN_TAX_GROUP_HEAD")) {
            txtPurRetrnVatgpHead.setText(hash.get("AC_HD_ID").toString());
        }
        if (viewType.equals("MISC_INCOME_GROUP_HEAD")) {
            txtMisIncomegpHead.setText(hash.get("AC_HD_ID").toString());
        }
        if (viewType.equals("SERVICE_TAX_GROUP_HEAD")) {
            txtServiceTaxgpHead.setText(hash.get("AC_HD_ID").toString());
        }
        if (viewType.equals("PURVAT_TAX_GROUP_HEAD")) {
            txtPurVatTaxgpHead.setText(hash.get("AC_HD_ID").toString());
        }
        if (viewType.equals("STOCK_HEAD")) {
            txtStockHd.setText(hash.get("AC_HD_ID").toString());
        }
        if (viewType.equals("DAMAGE_GROUP_HEAD")) {
            txtDamagegpHead.setText(hash.get("AC_HD_ID").toString());
        }
        if (viewType.equals("DEFICIATE_GROUP_HEAD")) {
            txtDeficiategpHead.setText(hash.get("AC_HD_ID").toString());
        }
        if (viewType.equals("PURCHASE_RETURN_GROUP_HEAD")) {
            txtPurchaseReturngpHead.setText(hash.get("AC_HD_ID").toString());
        }
        if (viewType.equals("SALES_RETURN_GROUP_HEAD")) {
            txtSalesReturngpHead.setText(hash.get("AC_HD_ID").toString());
        }
        if (viewType.equals("PURCHASE_GROUP_HEAD")) {
            txtPurchasegpHead.setText(hash.get("AC_HD_ID").toString());
        }
        if (viewType.equals("SALES_GROUP_HEAD")) {
            txtSalesgpHead.setText(hash.get("AC_HD_ID").toString());
        }
        if (viewType.equals("DISCOUNT_TAX_GROUP_HEAD")) { // Adde by nithya on 02-04-2020 for KD-1732
            txtDiscountGroupHead.setText(hash.get("AC_HD_ID").toString());
        }
        if (viewType.equals("DISCOUNT_VAT_TAX_GROUP_HEAD")) {
            txtDiscountVatGroupHead.setText(hash.get("AC_HD_ID").toString());
        }
        
        if (viewType.equals("STORE")) {
            txtStoreNo.setText(hash.get("STNUMBER").toString());
            //.   displayAlert("hash.get(="+hash.get("GEN_TRANS"));
            gen_trans = "";
            if (hash.get("GEN_TRANS") != null) {
                gen_trans = hash.get("GEN_TRANS").toString();
                if (gen_trans.equalsIgnoreCase("NO")) {
                    ClientUtil.enableDisable(panDepo2, false, false, true);
                } else {
                    ClientUtil.enableDisable(panDepo2, true, false, true);
                    txtSalesgpHead.setEnabled(false);
                    txtPurchasegpHead.setEnabled(false);
                    txtSalesReturngpHead.setEnabled(false);
                    txtPurchaseReturngpHead.setEnabled(false);
                    txtDeficiategpHead.setEnabled(false);
                    txtDamagegpHead.setEnabled(false);
                    txtServiceTaxgpHead.setEnabled(false);
                    txtComReciedgpHd.setEnabled(false);
                    txtMisIncomegpHead.setEnabled(false);
                    txtComReciedgpHd.setEnabled(false);
                    txtPurVatTaxgpHead.setEnabled(false);
                    txtStockHd.setEnabled(false);
                    txtPurRetrnVatgpHead.setEnabled(false);
                    txtSaleRetrnVatTaxgpHead.setEnabled(false);
                    txtSaleVatTaxgpHead.setEnabled(false);
                    txtDamageVatgpHead.setEnabled(false);
                    txtDeficitVatgpHead.setEnabled(false);
                    txtOtherExpnsgpHead.setEnabled(false);
                    txtDiscountGroupHead.setEnabled(false); // Added by nithya on 02-04-2020 for KD-1732
                    txtDiscountVatGroupHead.setEnabled(false);
                }
            }




        }
        if (viewType.equals("SALES")) {
            txtSalesmanid.setText(hash.get("SMID").toString());
        }
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                    || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                HashMap where = new HashMap();
                //System.out.println("hash.get"+hash.get("BORROWING_NO"));
                where.put("DEPID", hash.get("DEPID"));
                // where.put(CommonConstants.BRANCH_ID, "0001");
                hash.put(CommonConstants.MAP_WHERE, where);
                observable.populateData(hash);

                // fillTxtNoOfTokens();
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                    ClientUtil.enableDisable(panDepo, false);
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    ClientUtil.enableDisable(panDepo, true);
                }
                if (viewType.equals(AUTHORIZE)) {
                    ClientUtil.enableDisable(panDepo, false);
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
                setButtonEnableDisable();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrDepo = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace51 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace52 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace53 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace54 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace55 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panDepo = new com.see.truetransact.uicomponent.CPanel();
        panDepo1 = new com.see.truetransact.uicomponent.CPanel();
        PanDesign1 = new com.see.truetransact.uicomponent.CPanel();
        lblStoreNo = new com.see.truetransact.uicomponent.CLabel();
        txtStoreNo = new com.see.truetransact.uicomponent.CTextField();
        btnStore = new com.see.truetransact.uicomponent.CButton();
        txtName = new com.see.truetransact.uicomponent.CTextField();
        lblName = new com.see.truetransact.uicomponent.CLabel();
        lblOpngStock = new com.see.truetransact.uicomponent.CLabel();
        txtOpngStock = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txaRemarks = new com.see.truetransact.uicomponent.CTextArea();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        PanDesign2 = new com.see.truetransact.uicomponent.CPanel();
        lblSalesmanid = new com.see.truetransact.uicomponent.CLabel();
        txtSalesmanid = new com.see.truetransact.uicomponent.CTextField();
        btnSalesmanid = new com.see.truetransact.uicomponent.CButton();
        tdtStockasonDate = new com.see.truetransact.uicomponent.CDateField();
        lblStockasonDate = new com.see.truetransact.uicomponent.CLabel();
        lblOpngVatStock = new com.see.truetransact.uicomponent.CLabel();
        txtOpngVatStock = new com.see.truetransact.uicomponent.CTextField();
        txtDepoId = new com.see.truetransact.uicomponent.CTextField();
        lblDepoId = new com.see.truetransact.uicomponent.CLabel();
        lblProfitPercentage = new com.see.truetransact.uicomponent.CLabel();
        txtProfitPercentage = new com.see.truetransact.uicomponent.CTextField();
        panDepo2 = new com.see.truetransact.uicomponent.CPanel();
        lblSalesgpHead = new com.see.truetransact.uicomponent.CLabel();
        txtSalesgpHead = new com.see.truetransact.uicomponent.CTextField();
        btnSalesgpHead = new com.see.truetransact.uicomponent.CButton();
        lblPurchasegpHead = new com.see.truetransact.uicomponent.CLabel();
        txtPurchasegpHead = new com.see.truetransact.uicomponent.CTextField();
        btnPurchasegpHead = new com.see.truetransact.uicomponent.CButton();
        lblSalesReturngpHead = new com.see.truetransact.uicomponent.CLabel();
        txtSalesReturngpHead = new com.see.truetransact.uicomponent.CTextField();
        btnSalesReturngpHead = new com.see.truetransact.uicomponent.CButton();
        lblPurchaseReturngpHead = new com.see.truetransact.uicomponent.CLabel();
        txtPurchaseReturngpHead = new com.see.truetransact.uicomponent.CTextField();
        btnPurchasegpReturnHead = new com.see.truetransact.uicomponent.CButton();
        lblDeficiategpHead = new com.see.truetransact.uicomponent.CLabel();
        txtDeficiategpHead = new com.see.truetransact.uicomponent.CTextField();
        btnDeficiategpHead = new com.see.truetransact.uicomponent.CButton();
        lblDamagegpHead = new com.see.truetransact.uicomponent.CLabel();
        txtDamagegpHead = new com.see.truetransact.uicomponent.CTextField();
        btnDamagegpHead = new com.see.truetransact.uicomponent.CButton();
        lblServiceTaxgphead = new com.see.truetransact.uicomponent.CLabel();
        txtServiceTaxgpHead = new com.see.truetransact.uicomponent.CTextField();
        txtComReciedgpHd = new com.see.truetransact.uicomponent.CTextField();
        lblDamagegpHead2 = new com.see.truetransact.uicomponent.CLabel();
        btnSeviceTaxgpHead = new com.see.truetransact.uicomponent.CButton();
        btnComRecievegpHead = new com.see.truetransact.uicomponent.CButton();
        lblDamagegpHead3 = new com.see.truetransact.uicomponent.CLabel();
        lblDamagegpHead4 = new com.see.truetransact.uicomponent.CLabel();
        lblDamagegpHead5 = new com.see.truetransact.uicomponent.CLabel();
        lblDamagegpHead6 = new com.see.truetransact.uicomponent.CLabel();
        lblDamagegpHead7 = new com.see.truetransact.uicomponent.CLabel();
        lblDamagegpHead8 = new com.see.truetransact.uicomponent.CLabel();
        txtPurVatTaxgpHead = new com.see.truetransact.uicomponent.CTextField();
        txtPurRetrnVatgpHead = new com.see.truetransact.uicomponent.CTextField();
        txtSaleVatTaxgpHead = new com.see.truetransact.uicomponent.CTextField();
        txtSaleRetrnVatTaxgpHead = new com.see.truetransact.uicomponent.CTextField();
        txtDamageVatgpHead = new com.see.truetransact.uicomponent.CTextField();
        txtDeficitVatgpHead = new com.see.truetransact.uicomponent.CTextField();
        btnPurchaseVatTaxgpHead = new com.see.truetransact.uicomponent.CButton();
        btnPurchaseReturnVatgpHead = new com.see.truetransact.uicomponent.CButton();
        btnSaleVatgpHead = new com.see.truetransact.uicomponent.CButton();
        btnSaleRetrnVatgpHead = new com.see.truetransact.uicomponent.CButton();
        btnDamageVatgpHead = new com.see.truetransact.uicomponent.CButton();
        btnDeficitVatgpHead = new com.see.truetransact.uicomponent.CButton();
        lblDamagegpHead9 = new com.see.truetransact.uicomponent.CLabel();
        txtOtherExpnsgpHead = new com.see.truetransact.uicomponent.CTextField();
        btnOtherExpensegpHead = new com.see.truetransact.uicomponent.CButton();
        lblDamagegpHead10 = new com.see.truetransact.uicomponent.CLabel();
        txtMisIncomegpHead = new com.see.truetransact.uicomponent.CTextField();
        btnMisIncomgpHead = new com.see.truetransact.uicomponent.CButton();
        txtStockHd = new com.see.truetransact.uicomponent.CTextField();
        btnStockHd = new com.see.truetransact.uicomponent.CButton();
        lblStockHD = new com.see.truetransact.uicomponent.CLabel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        txtDiscountGroupHead = new com.see.truetransact.uicomponent.CTextField();
        txtDiscountVatGroupHead = new com.see.truetransact.uicomponent.CTextField();
        btnDiscountGoupHead = new com.see.truetransact.uicomponent.CButton();
        btnDiscountVatGroupHead = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(860, 663));
        setPreferredSize(new java.awt.Dimension(860, 663));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        tbrDepo.setMaximumSize(new java.awt.Dimension(367, 25));
        tbrDepo.setMinimumSize(new java.awt.Dimension(367, 25));
        tbrDepo.setPreferredSize(new java.awt.Dimension(367, 25));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
        tbrDepo.add(btnView);

        lbSpace3.setText("     ");
        tbrDepo.add(lbSpace3);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrDepo.add(btnNew);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDepo.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrDepo.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDepo.add(lblSpace52);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrDepo.add(btnDelete);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrDepo.add(btnSave);

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDepo.add(lblSpace53);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrDepo.add(btnCancel);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrDepo.add(btnAuthorize);

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDepo.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrDepo.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDepo.add(lblSpace55);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrDepo.add(btnReject);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrDepo.add(btnPrint);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDepo.add(lblSpace56);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrDepo.add(btnClose);

        getContentPane().add(tbrDepo, new java.awt.GridBagConstraints());

        panDepo.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panDepo.setMaximumSize(new java.awt.Dimension(900, 450));
        panDepo.setMinimumSize(new java.awt.Dimension(900, 450));
        panDepo.setPreferredSize(new java.awt.Dimension(900, 400));
        panDepo.setLayout(new java.awt.GridBagLayout());

        panDepo1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panDepo1.setMaximumSize(new java.awt.Dimension(750, 180));
        panDepo1.setMinimumSize(new java.awt.Dimension(750, 180));
        panDepo1.setPreferredSize(new java.awt.Dimension(750, 180));
        panDepo1.setLayout(new java.awt.GridBagLayout());

        PanDesign1.setMinimumSize(new java.awt.Dimension(350, 150));
        PanDesign1.setPreferredSize(new java.awt.Dimension(350, 150));
        PanDesign1.setLayout(new java.awt.GridBagLayout());

        lblStoreNo.setText("Store No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanDesign1.add(lblStoreNo, gridBagConstraints);

        txtStoreNo.setEnabled(false);
        txtStoreNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtStoreNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        PanDesign1.add(txtStoreNo, gridBagConstraints);

        btnStore.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnStore.setToolTipText("Search");
        btnStore.setMaximumSize(new java.awt.Dimension(25, 25));
        btnStore.setMinimumSize(new java.awt.Dimension(25, 25));
        btnStore.setPreferredSize(new java.awt.Dimension(25, 25));
        btnStore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStoreActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        PanDesign1.add(btnStore, gridBagConstraints);

        txtName.setMaximumSize(new java.awt.Dimension(100, 21));
        txtName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanDesign1.add(txtName, gridBagConstraints);

        lblName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanDesign1.add(lblName, gridBagConstraints);

        lblOpngStock.setText("Opening Stock");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanDesign1.add(lblOpngStock, gridBagConstraints);

        txtOpngStock.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtOpngStock.setAllowAll(true);
        txtOpngStock.setAllowNumber(true);
        txtOpngStock.setMaximumSize(new java.awt.Dimension(100, 21));
        txtOpngStock.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOpngStock.setNextFocusableComponent(tdtStockasonDate);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanDesign1.add(txtOpngStock, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanDesign1.add(lblRemarks, gridBagConstraints);

        txaRemarks.setMaximumSize(new java.awt.Dimension(100, 41));
        txaRemarks.setMinimumSize(new java.awt.Dimension(100, 41));
        txaRemarks.setNextFocusableComponent(txtSalesgpHead);
        txaRemarks.setPreferredSize(new java.awt.Dimension(100, 41));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanDesign1.add(txaRemarks, gridBagConstraints);

        cScrollPane1.setMaximumSize(new java.awt.Dimension(100, 21));
        cScrollPane1.setMinimumSize(new java.awt.Dimension(100, 21));
        cScrollPane1.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanDesign1.add(cScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = -15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 21, 5, 0);
        panDepo1.add(PanDesign1, gridBagConstraints);

        PanDesign2.setMinimumSize(new java.awt.Dimension(350, 150));
        PanDesign2.setPreferredSize(new java.awt.Dimension(350, 150));
        PanDesign2.setLayout(new java.awt.GridBagLayout());

        lblSalesmanid.setText("Salesman ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanDesign2.add(lblSalesmanid, gridBagConstraints);

        txtSalesmanid.setEnabled(false);
        txtSalesmanid.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSalesmanid.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        PanDesign2.add(txtSalesmanid, gridBagConstraints);

        btnSalesmanid.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSalesmanid.setToolTipText("Search");
        btnSalesmanid.setMaximumSize(new java.awt.Dimension(25, 25));
        btnSalesmanid.setMinimumSize(new java.awt.Dimension(25, 25));
        btnSalesmanid.setPreferredSize(new java.awt.Dimension(25, 25));
        btnSalesmanid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesmanidActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        PanDesign2.add(btnSalesmanid, gridBagConstraints);

        tdtStockasonDate.setNextFocusableComponent(txaRemarks);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanDesign2.add(tdtStockasonDate, gridBagConstraints);

        lblStockasonDate.setText("Stock AsOn Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanDesign2.add(lblStockasonDate, gridBagConstraints);

        lblOpngVatStock.setText("Opening Vat Stock");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanDesign2.add(lblOpngVatStock, gridBagConstraints);

        txtOpngVatStock.setMaximumSize(new java.awt.Dimension(100, 21));
        txtOpngVatStock.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanDesign2.add(txtOpngVatStock, gridBagConstraints);

        txtDepoId.setMaximumSize(new java.awt.Dimension(100, 21));
        txtDepoId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanDesign2.add(txtDepoId, gridBagConstraints);

        lblDepoId.setText("Depo ID");
        lblDepoId.setMaximumSize(new java.awt.Dimension(100, 21));
        lblDepoId.setMinimumSize(new java.awt.Dimension(100, 21));
        lblDepoId.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanDesign2.add(lblDepoId, gridBagConstraints);

        lblProfitPercentage.setText("Profit Percentage");
        lblProfitPercentage.setMaximumSize(new java.awt.Dimension(100, 21));
        lblProfitPercentage.setMinimumSize(new java.awt.Dimension(100, 21));
        lblProfitPercentage.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanDesign2.add(lblProfitPercentage, gridBagConstraints);

        txtProfitPercentage.setMaximumSize(new java.awt.Dimension(100, 21));
        txtProfitPercentage.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanDesign2.add(txtProfitPercentage, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 8, 5, 21);
        panDepo1.add(PanDesign2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = -19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 64, 0, 65);
        panDepo.add(panDepo1, gridBagConstraints);

        panDepo2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panDepo2.setMaximumSize(new java.awt.Dimension(750, 280));
        panDepo2.setMinimumSize(new java.awt.Dimension(750, 280));
        panDepo2.setPreferredSize(new java.awt.Dimension(750, 280));
        panDepo2.setLayout(new java.awt.GridBagLayout());

        lblSalesgpHead.setText("Sales GroupHead");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 27, 0, 0);
        panDepo2.add(lblSalesgpHead, gridBagConstraints);

        txtSalesgpHead.setEnabled(false);
        txtSalesgpHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSalesgpHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 22;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipady = -1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 16, 0, 0);
        panDepo2.add(txtSalesgpHead, gridBagConstraints);

        btnSalesgpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSalesgpHead.setToolTipText("Search");
        btnSalesgpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesgpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 23;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = -25;
        gridBagConstraints.ipady = -9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 44);
        panDepo2.add(btnSalesgpHead, gridBagConstraints);

        lblPurchasegpHead.setText("Purchase GroupHead");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 27, 0, 0);
        panDepo2.add(lblPurchasegpHead, gridBagConstraints);

        txtPurchasegpHead.setEnabled(false);
        txtPurchasegpHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurchasegpHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPurchasegpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPurchasegpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 22;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 16, 0, 0);
        panDepo2.add(txtPurchasegpHead, gridBagConstraints);

        btnPurchasegpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPurchasegpHead.setToolTipText("Search");
        btnPurchasegpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchasegpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 23;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -25;
        gridBagConstraints.ipady = -8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 44);
        panDepo2.add(btnPurchasegpHead, gridBagConstraints);

        lblSalesReturngpHead.setText("Sales Return GroupHead");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 27, 0, 0);
        panDepo2.add(lblSalesReturngpHead, gridBagConstraints);

        txtSalesReturngpHead.setEnabled(false);
        txtSalesReturngpHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSalesReturngpHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSalesReturngpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSalesReturngpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 22;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 16, 0, 0);
        panDepo2.add(txtSalesReturngpHead, gridBagConstraints);

        btnSalesReturngpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSalesReturngpHead.setToolTipText("Search");
        btnSalesReturngpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesReturngpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 23;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.ipadx = -25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 44);
        panDepo2.add(btnSalesReturngpHead, gridBagConstraints);

        lblPurchaseReturngpHead.setText("Purchase Return GroupHead");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 30, 0, 0);
        panDepo2.add(lblPurchaseReturngpHead, gridBagConstraints);

        txtPurchaseReturngpHead.setEnabled(false);
        txtPurchaseReturngpHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurchaseReturngpHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 22;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 16, 0, 0);
        panDepo2.add(txtPurchaseReturngpHead, gridBagConstraints);

        btnPurchasegpReturnHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPurchasegpReturnHead.setToolTipText("Search");
        btnPurchasegpReturnHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchasegpReturnHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 23;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = -25;
        gridBagConstraints.ipady = -8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 44);
        panDepo2.add(btnPurchasegpReturnHead, gridBagConstraints);

        lblDeficiategpHead.setText("Deficiate GroupHead");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 28, 0, 0);
        panDepo2.add(lblDeficiategpHead, gridBagConstraints);

        txtDeficiategpHead.setEnabled(false);
        txtDeficiategpHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtDeficiategpHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 22;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 16, 0, 0);
        panDepo2.add(txtDeficiategpHead, gridBagConstraints);

        btnDeficiategpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDeficiategpHead.setToolTipText("Search");
        btnDeficiategpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeficiategpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 23;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.ipadx = -25;
        gridBagConstraints.ipady = -10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 44);
        panDepo2.add(btnDeficiategpHead, gridBagConstraints);

        lblDamagegpHead.setText("Damage GroupHead");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 28, 0, 0);
        panDepo2.add(lblDamagegpHead, gridBagConstraints);

        txtDamagegpHead.setEnabled(false);
        txtDamagegpHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtDamagegpHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 22;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 16, 0, 0);
        panDepo2.add(txtDamagegpHead, gridBagConstraints);

        btnDamagegpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDamagegpHead.setToolTipText("Search");
        btnDamagegpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDamagegpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 23;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = -25;
        gridBagConstraints.ipady = -8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 44);
        panDepo2.add(btnDamagegpHead, gridBagConstraints);

        lblServiceTaxgphead.setText("ServiceTax GroupHead");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 27, 0, 0);
        panDepo2.add(lblServiceTaxgphead, gridBagConstraints);

        txtServiceTaxgpHead.setEnabled(false);
        txtServiceTaxgpHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtServiceTaxgpHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtServiceTaxgpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtServiceTaxgpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 0, 0);
        panDepo2.add(txtServiceTaxgpHead, gridBagConstraints);

        txtComReciedgpHd.setEnabled(false);
        txtComReciedgpHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtComReciedgpHd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 22;
        gridBagConstraints.gridy = 25;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 16, 0, 0);
        panDepo2.add(txtComReciedgpHd, gridBagConstraints);

        lblDamagegpHead2.setText("Commision Recv. GroupHead");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 25;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 28, 0, 0);
        panDepo2.add(lblDamagegpHead2, gridBagConstraints);

        btnSeviceTaxgpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSeviceTaxgpHead.setToolTipText("Search");
        btnSeviceTaxgpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeviceTaxgpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.ipadx = -28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 10, 0, 0);
        panDepo2.add(btnSeviceTaxgpHead, gridBagConstraints);

        btnComRecievegpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnComRecievegpHead.setToolTipText("Search");
        btnComRecievegpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComRecievegpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 23;
        gridBagConstraints.gridy = 25;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.ipadx = -25;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 0, 44);
        panDepo2.add(btnComRecievegpHead, gridBagConstraints);

        lblDamagegpHead3.setText("Purchase VatTax GroupHead");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.ipady = -7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 27, 0, 0);
        panDepo2.add(lblDamagegpHead3, gridBagConstraints);

        lblDamagegpHead4.setText("Purchase Reutrn Vat GroupHead");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 29, 0, 0);
        panDepo2.add(lblDamagegpHead4, gridBagConstraints);

        lblDamagegpHead5.setText("Sale Vat Group Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 27, 0, 0);
        panDepo2.add(lblDamagegpHead5, gridBagConstraints);

        lblDamagegpHead6.setText("Sale Return Vat Group Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 27, 0, 0);
        panDepo2.add(lblDamagegpHead6, gridBagConstraints);

        lblDamagegpHead7.setText("Damage Vat GroupHead");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 27, 0, 0);
        panDepo2.add(lblDamagegpHead7, gridBagConstraints);

        lblDamagegpHead8.setText("Deficite Vat GroupHead");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 27, 0, 0);
        panDepo2.add(lblDamagegpHead8, gridBagConstraints);

        txtPurVatTaxgpHead.setEnabled(false);
        txtPurVatTaxgpHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurVatTaxgpHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipady = -1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panDepo2.add(txtPurVatTaxgpHead, gridBagConstraints);

        txtPurRetrnVatgpHead.setEnabled(false);
        txtPurRetrnVatgpHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurRetrnVatgpHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 0, 0);
        panDepo2.add(txtPurRetrnVatgpHead, gridBagConstraints);

        txtSaleVatTaxgpHead.setEnabled(false);
        txtSaleVatTaxgpHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSaleVatTaxgpHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 4, 0, 0);
        panDepo2.add(txtSaleVatTaxgpHead, gridBagConstraints);

        txtSaleRetrnVatTaxgpHead.setEnabled(false);
        txtSaleRetrnVatTaxgpHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSaleRetrnVatTaxgpHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 0, 0);
        panDepo2.add(txtSaleRetrnVatTaxgpHead, gridBagConstraints);

        txtDamageVatgpHead.setEnabled(false);
        txtDamageVatgpHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtDamageVatgpHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 0, 0);
        panDepo2.add(txtDamageVatgpHead, gridBagConstraints);

        txtDeficitVatgpHead.setEnabled(false);
        txtDeficitVatgpHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtDeficitVatgpHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 0, 0);
        panDepo2.add(txtDeficitVatgpHead, gridBagConstraints);

        btnPurchaseVatTaxgpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPurchaseVatTaxgpHead.setToolTipText("Search");
        btnPurchaseVatTaxgpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseVatTaxgpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = -26;
        gridBagConstraints.ipady = -9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        panDepo2.add(btnPurchaseVatTaxgpHead, gridBagConstraints);

        btnPurchaseReturnVatgpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPurchaseReturnVatgpHead.setToolTipText("Search");
        btnPurchaseReturnVatgpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseReturnVatgpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = -26;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        panDepo2.add(btnPurchaseReturnVatgpHead, gridBagConstraints);

        btnSaleVatgpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSaleVatgpHead.setToolTipText("Search");
        btnSaleVatgpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaleVatgpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = -26;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        panDepo2.add(btnSaleVatgpHead, gridBagConstraints);

        btnSaleRetrnVatgpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSaleRetrnVatgpHead.setToolTipText("Search");
        btnSaleRetrnVatgpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaleRetrnVatgpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -24;
        gridBagConstraints.ipady = -8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 0);
        panDepo2.add(btnSaleRetrnVatgpHead, gridBagConstraints);

        btnDamageVatgpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDamageVatgpHead.setToolTipText("Search");
        btnDamageVatgpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDamageVatgpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -28;
        gridBagConstraints.ipady = -8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        panDepo2.add(btnDamageVatgpHead, gridBagConstraints);

        btnDeficitVatgpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDeficitVatgpHead.setToolTipText("Search");
        btnDeficitVatgpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeficitVatgpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -28;
        gridBagConstraints.ipady = -8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        panDepo2.add(btnDeficitVatgpHead, gridBagConstraints);

        lblDamagegpHead9.setText("Other Expense GroupHead");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 34;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 27, 0, 0);
        panDepo2.add(lblDamagegpHead9, gridBagConstraints);

        txtOtherExpnsgpHead.setEnabled(false);
        txtOtherExpnsgpHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtOtherExpnsgpHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.gridheight = 24;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 4, 0, 0);
        panDepo2.add(txtOtherExpnsgpHead, gridBagConstraints);

        btnOtherExpensegpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnOtherExpensegpHead.setToolTipText("Search");
        btnOtherExpensegpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOtherExpensegpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 38;
        gridBagConstraints.ipadx = -28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 0);
        panDepo2.add(btnOtherExpensegpHead, gridBagConstraints);

        lblDamagegpHead10.setText("Misc. Income GroupHead");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 28, 0, 0);
        panDepo2.add(lblDamagegpHead10, gridBagConstraints);

        txtMisIncomegpHead.setEnabled(false);
        txtMisIncomegpHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtMisIncomegpHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 22;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 16, 0, 0);
        panDepo2.add(txtMisIncomegpHead, gridBagConstraints);

        btnMisIncomgpHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMisIncomgpHead.setToolTipText("Search");
        btnMisIncomgpHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMisIncomgpHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 23;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = -25;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 0, 44);
        panDepo2.add(btnMisIncomgpHead, gridBagConstraints);

        txtStockHd.setEnabled(false);
        txtStockHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtStockHd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 83;
        gridBagConstraints.gridheight = 80;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panDepo2.add(txtStockHd, gridBagConstraints);

        btnStockHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnStockHd.setToolTipText("Search");
        btnStockHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 83;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 120;
        gridBagConstraints.ipadx = -28;
        gridBagConstraints.ipady = -8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 0, 0);
        panDepo2.add(btnStockHd, gridBagConstraints);

        lblStockHD.setText("Stock A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 83;
        gridBagConstraints.gridheight = 41;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 27, 0, 0);
        panDepo2.add(lblStockHD, gridBagConstraints);

        cLabel1.setText("Discount Group Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 44;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 39;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 28, 0, 0);
        panDepo2.add(cLabel1, gridBagConstraints);

        cLabel2.setText("Discount Vat Group Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 123;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.gridheight = 121;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 28, 0, 0);
        panDepo2.add(cLabel2, gridBagConstraints);

        txtDiscountGroupHead.setAllowAll(true);
        txtDiscountGroupHead.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 22;
        gridBagConstraints.gridy = 44;
        gridBagConstraints.gridheight = 40;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 16, 0, 0);
        panDepo2.add(txtDiscountGroupHead, gridBagConstraints);

        txtDiscountVatGroupHead.setAllowAll(true);
        txtDiscountVatGroupHead.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 22;
        gridBagConstraints.gridy = 123;
        gridBagConstraints.gridheight = 122;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 16, 22, 0);
        panDepo2.add(txtDiscountVatGroupHead, gridBagConstraints);

        btnDiscountGoupHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDiscountGoupHead.setToolTipText("Search");
        btnDiscountGoupHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiscountGoupHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 23;
        gridBagConstraints.gridy = 44;
        gridBagConstraints.gridheight = 40;
        gridBagConstraints.ipadx = -25;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 44);
        panDepo2.add(btnDiscountGoupHead, gridBagConstraints);

        btnDiscountVatGroupHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDiscountVatGroupHead.setToolTipText("Search");
        btnDiscountVatGroupHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiscountVatGroupHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 23;
        gridBagConstraints.gridy = 123;
        gridBagConstraints.gridheight = 122;
        gridBagConstraints.ipadx = -25;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 22, 44);
        panDepo2.add(btnDiscountVatGroupHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 64, 16, 65);
        panDepo.add(panDepo2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -21;
        gridBagConstraints.ipady = 99;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(panDepo, gridBagConstraints);

        panStatus.setMinimumSize(new java.awt.Dimension(250, 22));
        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        panStatus.add(lblStatus, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 629;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(panStatus, gridBagConstraints);

        mbrTokenConfig.setInheritsPopupMenu(true);

        mnuProcess.setText("Depo");
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

    private void btnSalesmanidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesmanidActionPerformed
        // TODO add your handling code here:
        callView("SALES");
    }//GEN-LAST:event_btnSalesmanidActionPerformed

    private void btnStoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStoreActionPerformed
        // TODO add your handling code here:
        callView("STORE");
    }//GEN-LAST:event_btnStoreActionPerformed

    private void btnPurchasegpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchasegpHeadActionPerformed
        // TODO add your handling code here:
        callView("PURCHASE_GROUP_HEAD");
    }//GEN-LAST:event_btnPurchasegpHeadActionPerformed

    private void btnComRecievegpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComRecievegpHeadActionPerformed
        // TODO add your handling code here:
        callView("VAT_TAX_GROUP_HEAD");
    }//GEN-LAST:event_btnComRecievegpHeadActionPerformed

    private void btnSeviceTaxgpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeviceTaxgpHeadActionPerformed
        // TODO add your handling code here:
        callView("SERVICE_TAX_GROUP_HEAD");
    }//GEN-LAST:event_btnSeviceTaxgpHeadActionPerformed

    private void btnDamagegpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDamagegpHeadActionPerformed
        // TODO add your handling code here:
        callView("DAMAGE_GROUP_HEAD");
    }//GEN-LAST:event_btnDamagegpHeadActionPerformed

    private void btnDeficiategpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeficiategpHeadActionPerformed
        // TODO add your handling code here:
        callView("DEFICIATE_GROUP_HEAD");
    }//GEN-LAST:event_btnDeficiategpHeadActionPerformed

    private void btnPurchasegpReturnHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchasegpReturnHeadActionPerformed
        // TODO add your handling code here:
        callView("PURCHASE_RETURN_GROUP_HEAD");
    }//GEN-LAST:event_btnPurchasegpReturnHeadActionPerformed

    private void btnSalesReturngpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesReturngpHeadActionPerformed
        // TODO add your handling code here:
        callView("SALES_RETURN_GROUP_HEAD");
    }//GEN-LAST:event_btnSalesReturngpHeadActionPerformed

    private void txtSalesReturngpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSalesReturngpHeadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSalesReturngpHeadActionPerformed

    private void txtPurchasegpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPurchasegpHeadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPurchasegpHeadActionPerformed

    private void btnSalesgpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesgpHeadActionPerformed
        // TODO add your handling code here:
        callView("SALES_GROUP_HEAD");
    }//GEN-LAST:event_btnSalesgpHeadActionPerformed

    public static void main(String args[]) throws Exception {
        try {
            frmDepo objIfrRenewal = new frmDepo();
            objIfrRenewal.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
//       this.dispose();
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
        txtDepoId.setEnabled(false);
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
        txtDepoId.setEnabled(false);
        setButtons(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        txtDepoId.setEnabled(false);
        setButtons(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)) {
            viewType = AUTHORIZE;
//            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getDepoAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeDepo");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            //AuthorizeStatusUI fdf=new AuthorizeStatusUI(this,mapParam);
            authorizeUI.show();
            observable.setStatus();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)) {
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, "0001");//commented on 13-Feb-2012
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("DEPID", observable.getTxtDepoId());
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            ClientUtil.execute("authorizeDepo", singleAuthorizeMap);
            // ClientUtil.ex
            viewType = "";
//            super.setOpenForEditBy(observable.getStatusBy());
//            super.removeEditLock(txtTokenConfigId.getText());
            btnCancelActionPerformed(null);
            //  lblStatus.setText(authorizeStatus);
        }
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setModified(false);
        observable.resetForm();
        //txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panDepo, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        txtDepoId.setEnabled(false);
        setButtons(false);
        txtOpngVatStock.setText("");
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
        txtDepoId.setEnabled(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed() {
        String action;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            //     System.out.println("IN savePerformed ACTIONTYPE_NEW"); 

            action = CommonConstants.TOSTATUS_INSERT;
            saveAction(action);

        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            action = CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            action = CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
    }

    public boolean checkNumber(String value) {
        //String amtRentIn=amountRentText.getText();
        boolean incorrect = true;
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
        // return 
    }

    /* Calls the execute method of TerminalOB to do insertion or updation or deletion */
    private void saveAction(String status) {
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        //  System.out.println("status saveAction11111: "+status);
//       txtAmtBorrowed.
        final String mandatoryMessage = checkMandatory(panDepo);
        StringBuffer message = new StringBuffer(mandatoryMessage);
        if (txtStoreNo.getText().equals("")) {
            message.append(objMandatoryRB.getString("cboStoreNo"));
        }
        if (txtSalesmanid.getText().equals("")) {
            message.append(objMandatoryRB.getString("cboSalesmanId"));
        }

        if (txtName.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtName"));
        }
        double op = CommonUtil.convertObjToDouble(txtOpngStock.getText());
        //  displayAlert("gen_trans="+op);

        if (txtOpngStock.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtOpngStock"));
        }
        if (!checkNumber(txtOpngStock.getText())) {
            message.append(objMandatoryRB.getString("txtOpngStockNo"));
        }

        if (op > 0.0) {
            if (tdtStockasonDate.getDateValue().equals("")) {
                message.append(objMandatoryRB.getString("tdtStockasonDate"));
            }
        }
        //  displayAlert(""+gen_trans);
        if (gen_trans.equalsIgnoreCase("YES")) {
            if (txtSalesgpHead.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtSalesgpHead"));
            }
            if (txtPurchasegpHead.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtPurchasegpHead"));
            }
            if (txtSalesReturngpHead.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtSalesReturngpHead"));
            }

            if (txtPurchaseReturngpHead.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtPurchaseReturngpHead"));
            }
            if (txtDeficiategpHead.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtDeficiategpHead"));
            }
            if (txtDamagegpHead.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtDamagegpHead"));
            }
            if (txtServiceTaxgpHead.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtServiceTaxgpHead"));
            }
            if (txtComReciedgpHd.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtComReciedgpHead"));
            }
            if (txtMisIncomegpHead.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtMisIncomegpHead"));
            }
            if (txtPurVatTaxgpHead.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtPurVatTaxgpHead"));
            }
            if (txtStockHd.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtStockHd"));
            }
            if (txtPurRetrnVatgpHead.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtPurRetrnVatTaxgpHead"));
            }
            if (txtSaleVatTaxgpHead.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtSaleVatTaxgpHead"));
            }
            if (txtSaleRetrnVatTaxgpHead.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtSaleRetrnVatTaxgpHead"));
            }
            if (txtDamageVatgpHead.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtDamageVatgpHead"));
            }
            if (txtDeficitVatgpHead.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtDeficitVatgpHead"));
            }
            if (txtOtherExpnsgpHead.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtOtherExpnsgpHead"));
            }
        } else {
        }
        //setExpDateOnCalculation();
        //  System.out.println("status saveAction: "+status);
        if (message.length() > 0) {
            displayAlert(message.toString());
        } else {
            if (CommonUtil.convertObjToStr(txtProfitPercentage.getText()).length() <= 0) {
                ClientUtil.showMessageWindow("Please Enter The Profit Percentage !!!");
                return;
            }
            updateOBFields();
            observable.execute(status);
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("DEPID");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                //   if (observable.getProxyReturnMap()!=null) {
                //      if (observable.getProxyReturnMap().containsKey("CONFIG_ID")) {
                //          lockMap.put("CONFIG_ID", observable.getProxyReturnMap().get("CONFIG_ID"));
                //      }
                //  }
                if (status == CommonConstants.TOSTATUS_UPDATE) {
                    lockMap.put("DEPID", observable.getTxtDepoId());
                }
                //      setEditLockMap(lockMap);
                // setEditLock();
                settings();
            }
        }

    }

    /**
     * Method used to check whether the Mandatory Fields in the Form are Filled
     * or not
     */
    private String checkMandatory(javax.swing.JComponent component) {
        //  return new MandatoryCheck().checkMandatory(getClass().getName(), component, getMandatoryHashMap());
        return "";
        //validation error
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
    /* set the screen after the updation,insertion, deletion */

    private void settings() {
        observable.resetForm();
        //  txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panDepo, false);
        setButtonEnableDisable();
        observable.setResultStatus();
        txtDepoId.setEnabled(false);
    }

    public Date getDateValue(String date1) {
        DateFormat formatter;
        Date date = null;
        try {
            System.out.println("date1 66666666666=========:" + date1);
            // String str_date=date1;
            //  formatter = new SimpleDateFormat("MM/dd/yyyy");
            //  date = (Date)formatter.parse(str_date);  
            //      System.out.println("dateAFETRRR 66666666666=========:"+date); 




            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
            // String s1 = "2008-03-30T15:30:00.000+02:00";
            // date1 = date1.substring(0, date1.indexOf('.'));
            System.out.println("Result==> " + sdf2.format(sdf1.parse(date1)));
            date = new Date(sdf2.format(sdf1.parse(date1)));
            System.out.println("date IOOOOOOO==> " + date);
        } catch (ParseException e) {
            System.out.println("Error in getDateValue():" + e);
        }
        return date;
    }

    public void updateOBFields() {

        observable.setTxtDepoId(txtDepoId.getText());
        observable.setTxtSalesmanId(txtSalesmanid.getText());
        observable.setTxtStoreNo(txtStoreNo.getText());
        observable.setTxtProfitPercentage(txtProfitPercentage.getText());
        observable.setTxtSalesgpHead(txtSalesgpHead.getText());
        //System.out.println("txtOpngVatStock.getText()>>>" + txtOpngVatStock.getText());
        observable.setTxtVatOpngStock(CommonUtil.convertObjToDouble(txtOpngVatStock.getText()));
        observable.setTxtName(txtName.getText());
        observable.setTxtOpngStock(Double.valueOf(txtOpngStock.getText()));
        observable.setTdtStockasonDate(getDateValue(tdtStockasonDate.getDateValue()));
        observable.setTxaRemarks(txaRemarks.getText());
        observable.setTxtPurchasegpHead(txtPurchasegpHead.getText());
        observable.setTxtOtherExpnsgpHead(txtOtherExpnsgpHead.getText());
        observable.setTxtPurchaseReturngpHead(txtPurchaseReturngpHead.getText());
        observable.setTxtSalesReturngpHead(txtSalesReturngpHead.getText());
        observable.setTxtDamagegpHead(txtDamagegpHead.getText());
        observable.setTxtServiceTaxgpHead(txtServiceTaxgpHead.getText());
        observable.setTxtVatTaxgpHead(txtComReciedgpHd.getText());
        observable.setTxtDeficiategpHead(txtDeficiategpHead.getText());
        observable.setTxtMisIncomegpHead(txtMisIncomegpHead.getText());
        observable.setTxtComReciedgpHd(txtComReciedgpHd.getText());
        observable.setTxtPurRetrnVatgpHead(txtPurRetrnVatgpHead.getText());
        observable.setTxtPurVatTaxgpHead(txtPurVatTaxgpHead.getText());
        observable.setTxtStockHd(txtStockHd.getText());
        observable.setTxtSaleVatTaxgpHead(txtSaleVatTaxgpHead.getText());
        observable.setTxtSaleRetrnVatTaxgpHead(txtSaleRetrnVatTaxgpHead.getText());
        observable.setTxtDamageVatgpHead(txtDamageVatgpHead.getText());
        observable.setTxtDeficitVatgpHead(txtDeficitVatgpHead.getText());
        observable.setTxtDiscountGroupHead(txtDiscountGroupHead.getText());// added by nithya on 02-04-2020 for KD-1732
        observable.setTxtDiscountVatGroupHead(txtDiscountVatGroupHead.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView(ClientConstants.ACTION_STATUS[3]);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        txtDepoId.setEnabled(false);
        setButtons(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        txtDepoId.setEnabled(false);
        setButtons(true);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.resetForm();
        //txtNoOfTokens.setText("");
        ClientUtil.enableDisable(panDepo, true);

        // System.out.println("btnNewActionPerformed ACTION STA: "+ClientConstants.ACTIONTYPE_NEW);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
        txtDepoId.setEnabled(false);
        setButtons(true);
        txtOpngVatStock.setText("");
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
        //  lblStatus.setText(observable.getLblStatus());
        btnView.setEnabled(!btnView.isEnabled());
        txtDepoId.setEnabled(false);
        txtSalesmanid.setEnabled(false);
        txtStoreNo.setEnabled(false);
        txtSalesgpHead.setEnabled(false);
        txtPurchasegpHead.setEnabled(false);
        txtMisIncomegpHead.setEnabled(false);
        txtPurVatTaxgpHead.setEnabled(false);
        txtStockHd.setEnabled(false);
        txtPurRetrnVatgpHead.setEnabled(false);
        txtSaleVatTaxgpHead.setEnabled(false);
        txtSaleRetrnVatTaxgpHead.setEnabled(false);
        txtDamageVatgpHead.setEnabled(false);
        txtDeficitVatgpHead.setEnabled(false);
        txtServiceTaxgpHead.setEnabled(false);
        txtOtherExpnsgpHead.setEnabled(false);
        txtSalesReturngpHead.setEnabled(false);
        txtPurchaseReturngpHead.setEnabled(false);
        txtDeficiategpHead.setEnabled(false);
        txtDamagegpHead.setEnabled(false);
        txtServiceTaxgpHead.setEnabled(false);
        txtComReciedgpHd.setEnabled(false);
        txtDiscountGroupHead.setEnabled(false); // Added by nithya on02-04-2020 for KD-1732
        txtDiscountVatGroupHead.setEnabled(false);
        setButtons(false);
        // txtOpngStock.setText("0");
    }
    private void mnuProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuProcessActionPerformed

private void btnPurchaseVatTaxgpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseVatTaxgpHeadActionPerformed
    callView("PURVAT_TAX_GROUP_HEAD");
}//GEN-LAST:event_btnPurchaseVatTaxgpHeadActionPerformed

private void btnPurchaseReturnVatgpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseReturnVatgpHeadActionPerformed
    callView("PURRETN_TAX_GROUP_HEAD");
}//GEN-LAST:event_btnPurchaseReturnVatgpHeadActionPerformed

private void btnSaleVatgpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaleVatgpHeadActionPerformed
    callView("SALESVAT_TAX_GROUP_HEAD");
}//GEN-LAST:event_btnSaleVatgpHeadActionPerformed

private void btnSaleRetrnVatgpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaleRetrnVatgpHeadActionPerformed
    callView("SALESRETN_TAX_GROUP_HEAD");
}//GEN-LAST:event_btnSaleRetrnVatgpHeadActionPerformed

private void btnDamageVatgpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDamageVatgpHeadActionPerformed
    callView("DMGVAT_TAX_GROUP_HEAD");
}//GEN-LAST:event_btnDamageVatgpHeadActionPerformed

private void btnDeficitVatgpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeficitVatgpHeadActionPerformed
    callView("DEFICITVAT_TAX_GROUP_HEAD");
}//GEN-LAST:event_btnDeficitVatgpHeadActionPerformed

private void btnOtherExpensegpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOtherExpensegpHeadActionPerformed
    callView("OTHEREXP_TAX_GROUP_HEAD");
}//GEN-LAST:event_btnOtherExpensegpHeadActionPerformed

private void btnMisIncomgpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMisIncomgpHeadActionPerformed
    callView("MISC_INCOME_GROUP_HEAD");
}//GEN-LAST:event_btnMisIncomgpHeadActionPerformed

    private void btnStockHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockHdActionPerformed
        // TODO add your handling code here:
        callView("STOCK_HEAD");
    }//GEN-LAST:event_btnStockHdActionPerformed

    private void txtServiceTaxgpHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtServiceTaxgpHeadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtServiceTaxgpHeadActionPerformed

    private void btnDiscountGoupHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiscountGoupHeadActionPerformed
        // TODO add your handling code here:
         callView("DISCOUNT_TAX_GROUP_HEAD");
    }//GEN-LAST:event_btnDiscountGoupHeadActionPerformed

    private void btnDiscountVatGroupHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiscountVatGroupHeadActionPerformed
        // TODO add your handling code here:
        callView("DISCOUNT_VAT_TAX_GROUP_HEAD");
    }//GEN-LAST:event_btnDiscountVatGroupHeadActionPerformed

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtStoreNo", new Boolean(true));
        mandatoryMap.put("txtSalesmanid", new Boolean(true));
        mandatoryMap.put("tdtDateIndand", new Boolean(true));
        mandatoryMap.put("txtName", new Boolean(true));
        mandatoryMap.put("txtPurAmount", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(true));


    }

    public java.util.HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    public String getDtPrintValue(String strDate) {
        try {
            //   System.out.println("strDate getDtPrintValue99999999999999999999999999999999999999================="+strDate);
            //create SimpleDateFormat object with source string date format
            SimpleDateFormat sdfSource = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            //parse the string into Date object
            Date date = sdfSource.parse(strDate);
            //create SimpleDateFormat object with desired date format
            SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
            //parse the date into another format
            strDate = sdfDestination.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }
    /* Auto Generated Method - update()
     This method called by Observable. It updates the UI with
     Observable's data. If needed add/Remove RadioButtons
     method need to be added.*/

    public void update(Observable observed, Object arg) {

        txtDepoId.setText(observable.getTxtDepoId());
        txtSalesmanid.setText(observable.getTxtSalesmanId());
        txtStoreNo.setText(observable.getTxtStoreNo());
        txtProfitPercentage.setText(observable.getTxtProfitPercentage());
        txtSalesgpHead.setText(observable.getTxtSalesgpHead());

        txtName.setText(observable.getTxtName());
        if (observable.getTxtOpngStock() != null) {
            txtOpngStock.setText(String.valueOf(observable.getTxtOpngStock()));
        }
        //System.out.println("observable.getTxtVatOpngStock()>>>"+observable.getTxtVatOpngStock());
        if (observable.getTxtVatOpngStock() != null) {
            txtOpngVatStock.setText(String.valueOf(observable.getTxtVatOpngStock()));
        }
        if (observable.getTdtStockasonDate() != null) {
            tdtStockasonDate.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtStockasonDate())));
        }
        txaRemarks.setText(observable.getTxaRemarks());
        txtPurchasegpHead.setText(observable.getTxtPurchasegpHead());
        txtPurchaseReturngpHead.setText(observable.getTxtPurchaseReturngpHead());
        txtSalesReturngpHead.setText(observable.getTxtSalesReturngpHead());
        txtDamagegpHead.setText(observable.getTxtDamagegpHead());
        txtServiceTaxgpHead.setText(observable.getTxtServiceTaxgpHead());
        txtComReciedgpHd.setText(observable.getTxtVatTaxgpHead());
        txtDeficiategpHead.setText(observable.getTxtDeficiategpHead());
        txtMisIncomegpHead.setText(observable.getTxtMisIncomegpHead());
        txtComReciedgpHd.setText(observable.getTxtComReciedgpHd());
        txtPurVatTaxgpHead.setText(observable.getTxtPurVatTaxgpHead());
        txtStockHd.setText(observable.getTxtStockHd());
        txtPurRetrnVatgpHead.setText(observable.getTxtPurRetrnVatgpHead());
        txtSaleVatTaxgpHead.setText(observable.getTxtSaleVatTaxgpHead());
        txtSaleRetrnVatTaxgpHead.setText(observable.getTxtSaleRetrnVatTaxgpHead());
        txtDamageVatgpHead.setText(observable.getTxtDamageVatgpHead());
        txtDeficitVatgpHead.setText(observable.getTxtDeficitVatgpHead());
        txtOtherExpnsgpHead.setText(observable.getTxtOtherExpnsgpHead());
        txtDiscountGroupHead.setText(observable.getTxtDiscountGroupHead());// Added by nithya on 02-04-2020 for KD-1732
        txtDiscountVatGroupHead.setText(observable.getTxtDiscountVatGroupHead());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CPanel PanDesign1;
    private com.see.truetransact.uicomponent.CPanel PanDesign2;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnComRecievegpHead;
    private com.see.truetransact.uicomponent.CButton btnDamageVatgpHead;
    private com.see.truetransact.uicomponent.CButton btnDamagegpHead;
    private com.see.truetransact.uicomponent.CButton btnDeficiategpHead;
    private com.see.truetransact.uicomponent.CButton btnDeficitVatgpHead;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDiscountGoupHead;
    private com.see.truetransact.uicomponent.CButton btnDiscountVatGroupHead;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnMisIncomgpHead;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnOtherExpensegpHead;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnPurchaseReturnVatgpHead;
    private com.see.truetransact.uicomponent.CButton btnPurchaseVatTaxgpHead;
    private com.see.truetransact.uicomponent.CButton btnPurchasegpHead;
    private com.see.truetransact.uicomponent.CButton btnPurchasegpReturnHead;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSaleRetrnVatgpHead;
    private com.see.truetransact.uicomponent.CButton btnSaleVatgpHead;
    private com.see.truetransact.uicomponent.CButton btnSalesReturngpHead;
    private com.see.truetransact.uicomponent.CButton btnSalesgpHead;
    private com.see.truetransact.uicomponent.CButton btnSalesmanid;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSeviceTaxgpHead;
    private com.see.truetransact.uicomponent.CButton btnStockHd;
    private com.see.truetransact.uicomponent.CButton btnStore;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblDamagegpHead;
    private com.see.truetransact.uicomponent.CLabel lblDamagegpHead10;
    private com.see.truetransact.uicomponent.CLabel lblDamagegpHead2;
    private com.see.truetransact.uicomponent.CLabel lblDamagegpHead3;
    private com.see.truetransact.uicomponent.CLabel lblDamagegpHead4;
    private com.see.truetransact.uicomponent.CLabel lblDamagegpHead5;
    private com.see.truetransact.uicomponent.CLabel lblDamagegpHead6;
    private com.see.truetransact.uicomponent.CLabel lblDamagegpHead7;
    private com.see.truetransact.uicomponent.CLabel lblDamagegpHead8;
    private com.see.truetransact.uicomponent.CLabel lblDamagegpHead9;
    private com.see.truetransact.uicomponent.CLabel lblDeficiategpHead;
    private com.see.truetransact.uicomponent.CLabel lblDepoId;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblOpngStock;
    private com.see.truetransact.uicomponent.CLabel lblOpngVatStock;
    private com.see.truetransact.uicomponent.CLabel lblProfitPercentage;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseReturngpHead;
    private com.see.truetransact.uicomponent.CLabel lblPurchasegpHead;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSalesReturngpHead;
    private com.see.truetransact.uicomponent.CLabel lblSalesgpHead;
    private com.see.truetransact.uicomponent.CLabel lblSalesmanid;
    private com.see.truetransact.uicomponent.CLabel lblServiceTaxgphead;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace52;
    private com.see.truetransact.uicomponent.CLabel lblSpace53;
    private com.see.truetransact.uicomponent.CLabel lblSpace54;
    private com.see.truetransact.uicomponent.CLabel lblSpace55;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStockHD;
    private com.see.truetransact.uicomponent.CLabel lblStockasonDate;
    private com.see.truetransact.uicomponent.CLabel lblStoreNo;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panDepo;
    private com.see.truetransact.uicomponent.CPanel panDepo1;
    private com.see.truetransact.uicomponent.CPanel panDepo2;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CToolBar tbrDepo;
    private com.see.truetransact.uicomponent.CDateField tdtStockasonDate;
    private com.see.truetransact.uicomponent.CTextArea txaRemarks;
    private com.see.truetransact.uicomponent.CTextField txtComReciedgpHd;
    private com.see.truetransact.uicomponent.CTextField txtDamageVatgpHead;
    private com.see.truetransact.uicomponent.CTextField txtDamagegpHead;
    private com.see.truetransact.uicomponent.CTextField txtDeficiategpHead;
    private com.see.truetransact.uicomponent.CTextField txtDeficitVatgpHead;
    private com.see.truetransact.uicomponent.CTextField txtDepoId;
    private com.see.truetransact.uicomponent.CTextField txtDiscountGroupHead;
    private com.see.truetransact.uicomponent.CTextField txtDiscountVatGroupHead;
    private com.see.truetransact.uicomponent.CTextField txtMisIncomegpHead;
    private com.see.truetransact.uicomponent.CTextField txtName;
    private com.see.truetransact.uicomponent.CTextField txtOpngStock;
    private com.see.truetransact.uicomponent.CTextField txtOpngVatStock;
    private com.see.truetransact.uicomponent.CTextField txtOtherExpnsgpHead;
    private com.see.truetransact.uicomponent.CTextField txtProfitPercentage;
    private com.see.truetransact.uicomponent.CTextField txtPurRetrnVatgpHead;
    private com.see.truetransact.uicomponent.CTextField txtPurVatTaxgpHead;
    private com.see.truetransact.uicomponent.CTextField txtPurchaseReturngpHead;
    private com.see.truetransact.uicomponent.CTextField txtPurchasegpHead;
    private com.see.truetransact.uicomponent.CTextField txtSaleRetrnVatTaxgpHead;
    private com.see.truetransact.uicomponent.CTextField txtSaleVatTaxgpHead;
    private com.see.truetransact.uicomponent.CTextField txtSalesReturngpHead;
    private com.see.truetransact.uicomponent.CTextField txtSalesgpHead;
    private com.see.truetransact.uicomponent.CTextField txtSalesmanid;
    private com.see.truetransact.uicomponent.CTextField txtServiceTaxgpHead;
    private com.see.truetransact.uicomponent.CTextField txtStockHd;
    private com.see.truetransact.uicomponent.CTextField txtStoreNo;
    // End of variables declaration//GEN-END:variables
//     private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.clientutil.TableModel tbModel;
}
