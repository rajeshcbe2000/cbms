/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmptransferUI.java 
 *
 * Created on feb 9, 2009, 10:53 AM
 */

package com.see.truetransact.ui.sysadmin.fixedassets;

import java.util.Map;
import java.util.HashMap;
import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI ;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.customer.customeridchange.CustomerIdChangeOB;
import com.see.truetransact.commonutil.DateUtil;
import java.util.Date;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uicomponent.CButtonGroup;
import java.util.ResourceBundle;
//trans details
//import java.util.LinkedHashMap;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import java.util.Date;
//end..


/** This form is used to manipulate FixedAssetsUI related functionality
 * @author swaroop
 */
public class FixedAssetsTransUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.fixedassets.FixedAssetsTransRB", ProxyParameters.LANGUAGE);
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    TransactionUI transactionUI = new TransactionUI(); //trans details
    double amtBorrow=0.0; //trans details
    HashMap mandatoryMap = null;
    FixedAssetsTransMRB objMandatoryRB = null;
    FixedAssetsTransOB observable = null;
    final int EDIT=0,DELETE=8,ACCNOCHEQUE=2,ACCNOSTOP=3,ACCNOLOOSE=4,VIEW=10,ECSSTOP=7;
//    private final String AUTHORIZE="Authorize";//Variable used when btnAuthorize is clicked
    private int viewType=-1;
//    private String viewwType = "";//Variable used to store ActionType(New,Edit,Delete)
    private int BREAKAGE_ID = 1,MOVEMENT_ID = 2,FROM_ID =3,TO_ID =4,SALE_ID =5, AUTHORIZE=6;//DELETE=7;
    boolean isFilled = false;
    int updateTab=-1;
    private boolean updateMode = false;
    
    //   java.util.ResourceBundle resourceBundle;//validation
    FixedAssetsTransMRB objMandatoryMRB=new FixedAssetsTransMRB();//validation
    
    final int DEPRECIATION=0,SALE=1,MOVEMENT=2,BREAKAGE=3;
    int pan=-1;
    int panEditDelete=-1;
    int view = -1;
    private Date currDt = null;
    
    
     /** Creates new form TokenConfigUI */
    public FixedAssetsTransUI() {
        currDt = ClientUtil.getCurrentDate();
        initForm();
        //trans details
        panTrans.add(transactionUI);
        transactionUI.setSourceScreen("NEW_BORROW");
        observable.setTransactionOB(transactionUI.getTransactionOB());
        btnEdit.setVisible(false);
        //end..
    }
    
    /** Method which is used to initialize the form TokenConfig */
    private void initForm(){
        initComponents();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        observable = new FixedAssetsTransOB();
        observable.addObserver(this);
        setMaxLengths();
        initComponentData();
        // initTableData();
        ClientUtil.enableDisable(panBrokage, false);
        ClientUtil.enableDisable(panMovement, false);
        ClientUtil.enableDisable(panDepreciation, false);
        ClientUtil.enableDisable(panSale, false);
        setButtonEnableDisable();
        ClientUtil.clearAll(this);
        enableDisable(false);
        buttonDisable(false);
        enableDisablePanButton(false);
        btnCalculate.setEnabled(false);
    }
       
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAssetIdBreak.setName("btnAssetIdBreak");
        btnAssetIdMove.setName("btnAssetIdMove");
        btnAssetIdSale.setName("btnAssetIdSale");
        btnAuthorize.setName("btnAuthorize");
        btnBreakageDelete.setName("btnBreakageDelete");
        btnBreakageNew.setName("btnBreakageNew");
        btnBreakageSave.setName("btnBreakageSave");
        btnCalculate.setName("btnCalculate");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDateChange.setName("btnDateChange");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnFromAssetId.setName("btnFromAssetId");
        btnMoveDelete.setName("btnMoveDelete");
        btnMoveNew.setName("btnMoveNew");
        btnMoveSave.setName("btnMoveSave");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSaleDelete.setName("btnSaleDelete");
        btnSaleNew.setName("btnSaleNew");
        btnSaleSave.setName("btnSaleSave");
        btnSave.setName("btnSave");
        btnToAssetId.setName("btnToAssetId");
        btnView.setName("btnView");
        cboBranchIdMove.setName("cboBranchIdMove");
        cboDepartMove.setName("cboDepartMove");
        cboProductType.setName("cboProductType");
        cboProductTypeSale.setName("cboProductTypeSale");
        cboSubType.setName("cboSubType");
        cboSubTypeSale.setName("cboSubTypeSale");
        lblAssetIdBreak.setName("lblDepBatchId");
        lblAssetIdBreak.setName("lblAssetIdBreak");
        lblAssetIdMove.setName("lblAssetIdMove");
        lblAssetIdSale.setName("lblAssetIdSale");
        lblBranchId.setName("lblBranchId");
        lblBranchIdBreak.setName("lblBranchIdBreak");
        lblBranchIdMove.setName("lblBranchIdMove");
        lblBreakageRegion.setName("lblBreakageRegion");
        lblCurrValue.setName("lblCurrValue");
        lblCurrValueBreak.setName("lblCurrValueBreak");
        lblCurrentValueSale.setName("lblCurrentValueSale");
        lblCurrentValueSale1.setName("lblCurrentValueSale1");
        lblDepDate.setName("lblDepDate");
        lblDepart.setName("lblDepart");
        lblDepartBreak.setName("lblDepartBreak");
        lblDepartMove.setName("lblDepartMove");
        lblFaceVal.setName("lblFaceVal");
        lblFaceValBreak.setName("lblFaceValBreak");
        lblFaceValueSale.setName("lblFaceValueSale");
        lblFloor.setName("lblFloor");
        lblFloorBreak.setName("lblFloorBreak");
        lblFloorMove.setName("lblFloorMove");
        lblFromAssetId.setName("lblFromAssetId");
        lblMsg.setName("lblMsg");
        lblProductType.setName("lblProductType");
        lblProductTypeSale.setName("lblProductTypeSale");
        lblPurchasedDate.setName("lblPurchasedDate");
        lblSlNo.setName("lblSlNo");
        lblSlNoBreak.setName("lblSlNoBreak");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        lblSubType.setName("lblSubType");
        lblSubTypeSale.setName("lblSubTypeSale");
        lblToAssetId.setName("lblToAssetId");
        lblTotDepValue.setName("lblTotDepValue");
        lblTotNewCurValue.setName("lblTotNewCurValue");
        lblTotalCurrentValue.setName("lblTotalCurrentValue");
        mbrCustomer.setName("mbrCustomer");
        panBreakageBtn.setName("panBreakageBtn");
        panBreakageDetails.setName("panBreakageDetails");
        panBrokage.setName("panBrokage");
        panDepreciation.setName("panDepreciation");
        panDepreciationSale.setName("panDepreciationSale");
        panFixedAssets.setName("panFixedAssets");
        panMoveBtn.setName("panMoveBtn");
        panMoveList.setName("panMoveList");
        panMovement.setName("panMovement");
        panMovementDetails.setName("panMovementDetails");
        panMovementList.setName("panMovementList");
        panSale.setName("panSale");
        panSaleBtn.setName("panSaleBtn");
        panSaleDetails.setName("panSaleDetails");
        panSaleList.setName("panSaleList");
        panStatus.setName("panStatus");
        panTotalSale.setName("panTotalSale");
        panTotalTrans.setName("panTotalTrans");
        panTrans.setName("panTrans");
        panTransactionDetails.setName("panTransactionDetails");
        srpTokenLost.setName("srpTokenLost");
        srpTokenLostBreakage.setName("srpTokenLostBreakage");
        srpTokenLostMovement.setName("srpTokenLostMovement");
        srpTokenLostSale.setName("srpTokenLostSale");
        tabDepreciationSale.setName("tabDepreciationSale");
        tabFixedAssets.setName("tabFixedAssets");
        tblBreakageList.setName("tblBreakageList");
        tblDepreciationList.setName("tblDepreciationList");
        tblMovementList.setName("tblMovementList");
        tblSaleList.setName("tblSaleList");
        tdtDepDate.setName("tdtDepDate");
        txtAssetIdBreak.setName("txtAssetIdBreak");
        txtAssetIdMove.setName("txtAssetIdMove");
        txtAssetIdSale.setName("txtAssetIdSale");
        txtBranchId.setName("txtBranchId");
        txtBranchIdBreak.setName("txtBranchIdBreak");
        txtBreakageRegion.setName("txtBreakageRegion");
        txtCurrValue.setName("txtCurrValue");
        txtCurrValueBreak.setName("txtCurrValueBreak");
        txtCurrentValueSale.setName("txtCurrentValueSale");
        txtDepart.setName("txtDepart");
        txtDepartBreak.setName("txtDepartBreak");
        txtFaceVal.setName("txtFaceVal");
        txtFaceValBreak.setName("txtFaceValBreak");
        txtFaceValueSale.setName("txtFaceValueSale");
        txtFloor.setName("txtFloor");
        txtFloorBreak.setName("txtFloorBreak");
        txtFloorMove.setName("txtFloorMove");
        txtFromAssetId.setName("txtFromAssetId");
        txtPurchasedDate.setName("txtPurchasedDate");
        txtSlNo.setName("txtSlNo");
        txtSlNoBreak.setName("txtSlNoBreak");
        txtToAssetId.setName("txtToAssetId");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        ((javax.swing.border.TitledBorder)panMovementList.getBorder()).setTitle(resourceBundle.getString("panMovementList"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblTotalCurrentValue.setText(resourceBundle.getString("lblTotalCurrentValue"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblDepartBreak.setText(resourceBundle.getString("lblDepartBreak"));
        lblBranchIdBreak.setText(resourceBundle.getString("lblBranchIdBreak"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblAssetIdSale.setText(resourceBundle.getString("lblAssetIdSale"));
        btnMoveNew.setText(resourceBundle.getString("btnMoveNew"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSubTypeSale.setText(resourceBundle.getString("lblSubTypeSale"));
        btnCalculate.setText(resourceBundle.getString("btnCalculate"));
        lblToAssetId.setText(resourceBundle.getString("lblToAssetId"));
        lblCurrentValueSale.setText(resourceBundle.getString("lblCurrentValueSale"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnAssetIdBreak.setText(resourceBundle.getString("btnAssetIdBreak"));
        lblSlNo.setText(resourceBundle.getString("lblSlNo"));
        ((javax.swing.border.TitledBorder)panBreakageDetails.getBorder()).setTitle(resourceBundle.getString("panBreakageDetails"));
        lblAssetIdBreak.setText(resourceBundle.getString("lblAssetIdBreak"));
        btnMoveSave.setText(resourceBundle.getString("btnMoveSave"));
        btnBreakageNew.setText(resourceBundle.getString("btnBreakageNew"));
        lblCurrValueBreak.setText(resourceBundle.getString("lblCurrValueBreak"));
        lblDepartMove.setText(resourceBundle.getString("lblDepartMove"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblDepDate.setText(resourceBundle.getString("lblDepDate"));
        ((javax.swing.border.TitledBorder)panTransactionDetails.getBorder()).setTitle(resourceBundle.getString("panTransactionDetails"));
        lblTotDepValue.setText(resourceBundle.getString("lblTotDepValue"));
        lblSubType.setText(resourceBundle.getString("lblSubType"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblProductType.setText(resourceBundle.getString("lblProductType"));
        lblDepart.setText(resourceBundle.getString("lblDepart"));
        lblSlNoBreak.setText(resourceBundle.getString("lblSlNoBreak"));
        btnAssetIdMove.setText(resourceBundle.getString("btnAssetIdMove"));
        lblAssetIdMove.setText(resourceBundle.getString("lblAssetIdMove"));
        lblFloorBreak.setText(resourceBundle.getString("lblFloorBreak"));
        ((javax.swing.border.TitledBorder)panSaleDetails.getBorder()).setTitle(resourceBundle.getString("panSaleDetails"));
        btnSaleSave.setText(resourceBundle.getString("btnSaleSave"));
        lblCurrentValueSale1.setText(resourceBundle.getString("lblCurrentValueSale1"));
        btnFromAssetId.setText(resourceBundle.getString("btnFromAssetId"));
        lblFloorMove.setText(resourceBundle.getString("lblFloorMove"));
        lblBranchIdMove.setText(resourceBundle.getString("lblBranchIdMove"));
        btnDateChange.setText(resourceBundle.getString("btnDateChange"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblFaceValueSale.setText(resourceBundle.getString("lblFaceValueSale"));
        btnSaleDelete.setText(resourceBundle.getString("btnSaleDelete"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblProductTypeSale.setText(resourceBundle.getString("lblProductTypeSale"));
        lblPurchasedDate.setText(resourceBundle.getString("lblPurchasedDate"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblCurrValue.setText(resourceBundle.getString("lblCurrValue"));
        lblTotNewCurValue.setText(resourceBundle.getString("lblTotNewCurValue"));
        btnView.setText(resourceBundle.getString("btnView"));
        lblFaceValBreak.setText(resourceBundle.getString("lblFaceValBreak"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnSaleNew.setText(resourceBundle.getString("btnSaleNew"));
        ((javax.swing.border.TitledBorder)panMovementDetails.getBorder()).setTitle(resourceBundle.getString("panMovementDetails"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnBreakageDelete.setText(resourceBundle.getString("btnBreakageDelete"));
        btnAssetIdSale.setText(resourceBundle.getString("btnAssetIdSale"));
        btnMoveDelete.setText(resourceBundle.getString("btnMoveDelete"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblFaceVal.setText(resourceBundle.getString("lblFaceVal"));
        lblFloor.setText(resourceBundle.getString("lblFloor"));
        lblFromAssetId.setText(resourceBundle.getString("lblFromAssetId"));
        btnBreakageSave.setText(resourceBundle.getString("btnBreakageSave"));
        lblBreakageRegion.setText(resourceBundle.getString("lblBreakageRegion"));
        lblBranchId.setText(resourceBundle.getString("lblBranchId"));
        btnToAssetId.setText(resourceBundle.getString("btnToAssetId"));
    }
    
    private void initTableData(){
        tblDepreciationList.setModel(observable.getTblDepreciationList());
    
    }
    /*Setting model to the combobox cboTokenType  */
    private void initComponentData() {
        try{
            tblSaleList.setModel(observable.getTblSaleList());
            tblMovementList.setModel(observable.getTblEmpDetails());
            tblBreakageList.setModel(observable.getTblBreakageList());
            cboBranchIdMove.setModel(observable.getCbmBranchIdMove());
            cboDepartMove.setModel(observable.getCbmDepartMove());
            cboProductType.setModel(observable.getCbmProductType());
            cboSubType.setModel(observable.getCbmSubType());
            cboProductTypeSale.setModel(observable.getCbmProductTypeSale());
            cboSubTypeSale.setModel(observable.getCbmSubTypeSale());
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        cboProductType.setSelectedItem(observable.getCboProductType());
        cboSubType.setSelectedItem(observable.getCboSubType());
        tdtDepDate.setDateValue(observable.getTdtDepDate());
        txtFromAssetId.setText(observable.getTxtFromAssetId());
        txtToAssetId.setText(observable.getTxtToAssetId());
        cboProductTypeSale.setSelectedItem(observable.getCboProductTypeSale());
        cboSubTypeSale.setSelectedItem(observable.getCboSubTypeSale());
        txtAssetIdSale.setText(observable.getTxtAssetIdSale());
        txtFaceValueSale.setText(observable.getTxtFaceValueSale());
        txtCurrentValueSale.setText(observable.getTxtCurrentValueSale());
        txtPurchasedDate.setText(observable.getTxtPurchasedDate());
        txtAssetIdMove.setText(observable.getTxtAssetIdMove());
        txtSlNo.setText(observable.getTxtSlNo());
        txtFloor.setText(observable.getTxtFloor());
        txtFaceVal.setText(observable.getTxtFaceVal());
        txtCurrValue.setText(observable.getTxtCurrValue());
        txtDepart.setText(observable.getTxtDepart());
        txtBranchId.setText(observable.getTxtBranchId());
        cboBranchIdMove.setSelectedItem(observable.getCboBranchIdMove());
        cboDepartMove.setSelectedItem(observable.getCboDepartMove());
        txtFloorMove.setText(observable.getTxtFloorMove());
        txtAssetIdBreak.setText(observable.getTxtAssetIdBreak());
        txtSlNoBreak.setText(observable.getTxtSlNoBreak());
        txtFloorBreak.setText(observable.getTxtFloorBreak());
        txtFaceValBreak.setText(observable.getTxtFaceValBreak());
        txtCurrValueBreak.setText(observable.getTxtCurrValueBreak());
        txtBreakageRegion.setText(observable.getTxtBreakageRegion());
        txtDepartBreak.setText(observable.getTxtDepartBreak());
        txtBranchIdBreak.setText(observable.getTxtBranchIdBreak());
        tblMovementList.setModel(observable.getTblEmpDetails());
        tblBreakageList.setModel(observable.getTblBreakageList());
        tblMovementList.setModel(observable.getTblEmpDetails());
        tblMovementList.setModel(observable.getTblEmpDetails());
    }
    
     
     
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateDepreciationFields() {
        observable.setTdtDepDate(tdtDepDate.getDateValue());
        observable.setCboProductType((String) cboProductType.getSelectedItem());
        observable.setCboSubType((String) cboSubType.getSelectedItem());
        observable.setTxtFromAssetId(txtFromAssetId.getText());
        observable.setTxtToAssetId(txtToAssetId.getText());
    }
    
    public void updateSaleFields() {
        observable.setCboProductTypeSale((String) cboProductTypeSale.getSelectedItem());
        observable.setCboSubTypeSale((String) cboSubTypeSale.getSelectedItem());
        observable.setTxtAssetIdSale(txtAssetIdSale.getText());
        observable.setTxtFaceValueSale(txtFaceValueSale.getText());
        observable.setTxtCurrentValueSale(txtCurrentValueSale.getText());
        observable.setTxtPurchasedDate(txtPurchasedDate.getText());
        System.out.println("khutftywq"+lblTotalCurrentValue.getText());
        observable.setLblTotalCurrentValue(lblTotalCurrentValue.getText());
        
    }
    
    public void updateMovementFields() {
        observable.setTxtAssetIdMove(txtAssetIdMove.getText());
        observable.setTxtSlNo(txtSlNo.getText());
        observable.setTxtFloor(txtFloor.getText());
        observable.setTxtFaceVal(txtFaceVal.getText());
        observable.setTxtCurrValue(txtCurrValue.getText());
        observable.setTxtDepart(txtDepart.getText());
        observable.setTxtBranchId(txtBranchId.getText());
        observable.setCboBranchIdMove((String) cboBranchIdMove.getSelectedItem());
        observable.setCboDepartMove((String) cboDepartMove.getSelectedItem());
        observable.setTxtFloorMove(txtFloorMove.getText());
    }
    
    public void updateBreakageFields() {
        observable.setTxtAssetIdBreak(txtAssetIdBreak.getText());
        observable.setTxtSlNoBreak(txtSlNoBreak.getText());
        observable.setTxtFloorBreak(txtFloorBreak.getText());
        observable.setTxtFaceValBreak(txtFaceValBreak.getText());
        observable.setTxtCurrValueBreak(txtCurrValueBreak.getText());
        observable.setTxtBreakageRegion(txtBreakageRegion.getText());
        observable.setTxtDepartBreak(txtDepartBreak.getText());
        observable.setTxtBranchIdBreak(txtBranchIdBreak.getText());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProductType", new Boolean(false));
        mandatoryMap.put("cboSubType", new Boolean(false));
        mandatoryMap.put("tdtDepDate", new Boolean(false));
        mandatoryMap.put("txtFromAssetId", new Boolean(false));
        mandatoryMap.put("txtToAssetId", new Boolean(false));
        mandatoryMap.put("cboProductTypeSale", new Boolean(false));
        mandatoryMap.put("cboSubTypeSale", new Boolean(false));
        mandatoryMap.put("txtAssetIdSale", new Boolean(false));
        mandatoryMap.put("txtFaceValueSale", new Boolean(false));
        mandatoryMap.put("txtCurrentValueSale", new Boolean(false));
        mandatoryMap.put("txtPurchasedDate", new Boolean(false));
        mandatoryMap.put("txtAssetIdMove", new Boolean(false));
        mandatoryMap.put("txtSlNo", new Boolean(false));
        mandatoryMap.put("txtFloor", new Boolean(false));
        mandatoryMap.put("txtFaceVal", new Boolean(false));
        mandatoryMap.put("txtCurrValue", new Boolean(false));
        mandatoryMap.put("txtDepart", new Boolean(false));
        mandatoryMap.put("txtBranchId", new Boolean(false));
        mandatoryMap.put("cboBranchIdMove", new Boolean(false));
        mandatoryMap.put("cboDepartMove", new Boolean(false));
        mandatoryMap.put("txtFloorMove", new Boolean(false));
        mandatoryMap.put("txtAssetIdBreak", new Boolean(false));
        mandatoryMap.put("txtSlNoBreak", new Boolean(false));
        mandatoryMap.put("txtFloorBreak", new Boolean(false));
        mandatoryMap.put("txtFaceValBreak", new Boolean(false));
        mandatoryMap.put("txtCurrValueBreak", new Boolean(false));
        mandatoryMap.put("txtBreakageRegion", new Boolean(false));
        mandatoryMap.put("txtDepartBreak", new Boolean(false));
        mandatoryMap.put("txtBranchIdBreak", new Boolean(false));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new FixedAssetsTransMRB();
        cboProductType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductType"));
        cboSubType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSubType"));
        tdtDepDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDepDate"));
        txtFromAssetId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFromAssetId"));
        txtToAssetId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtToAssetId"));
        cboProductTypeSale.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductTypeSale"));
        cboSubTypeSale.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSubTypeSale"));
        txtAssetIdSale.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAssetIdSale"));
        txtFaceValueSale.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFaceValueSale"));
        txtCurrentValueSale.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCurrentValueSale"));
        txtPurchasedDate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPurchasedDate"));
        txtAssetIdMove.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAssetIdMove"));
        txtSlNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSlNo"));
        txtFloor.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFloor"));
        txtFaceVal.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFaceVal"));
        txtCurrValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCurrValue"));
        txtDepart.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDepart"));
        txtBranchId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchId"));
        cboBranchIdMove.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBranchIdMove"));
        cboDepartMove.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDepartMove"));
        txtFloorMove.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFloorMove"));
        txtAssetIdBreak.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAssetIdBreak"));
        txtSlNoBreak.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSlNoBreak"));
        txtFloorBreak.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFloorBreak"));
        txtFaceValBreak.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFaceValBreak"));
        txtCurrValueBreak.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCurrValueBreak"));
        txtBreakageRegion.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBreakageRegion"));
        txtDepartBreak.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDepartBreak"));
        txtBranchIdBreak.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchIdBreak"));
    }
    
    public void fillData(Object obj){
        try{
            HashMap hashMap=(HashMap)obj;
            System.out.println("### fillData Hash : "+hashMap);
            HashMap returnMap = null;
            isFilled = true;
            //         if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            if(viewType == BREAKAGE_ID){
                txtAssetIdBreak.setText(CommonUtil.convertObjToStr(hashMap.get("FA_ID")));
                txtSlNoBreak.setText(CommonUtil.convertObjToStr(hashMap.get("SL_NO")));
                txtBranchIdBreak.setText(CommonUtil.convertObjToStr(hashMap.get("SUPPLIED_BRANCH_ID")));
                txtDepartBreak.setText(CommonUtil.convertObjToStr(hashMap.get("DEPARTMENT")));
                txtFloorBreak.setText(CommonUtil.convertObjToStr(hashMap.get("FLOOR")));
                txtFaceValBreak.setText(CommonUtil.convertObjToStr(hashMap.get("FACE_VALUE")));
                txtCurrValueBreak.setText(CommonUtil.convertObjToStr(hashMap.get("CURR_VALUE")));
            }else if(viewType == MOVEMENT_ID){
                txtAssetIdMove.setText(CommonUtil.convertObjToStr(hashMap.get("FA_ID")));
                txtSlNo.setText(CommonUtil.convertObjToStr(hashMap.get("SL_NO")));
                txtBranchId.setText(CommonUtil.convertObjToStr(hashMap.get("SUPPLIED_BRANCH_ID")));
                txtDepart.setText(CommonUtil.convertObjToStr(hashMap.get("DEPARTMENT")));
                txtFloor.setText(CommonUtil.convertObjToStr(hashMap.get("FLOOR")));
                txtFaceVal.setText(CommonUtil.convertObjToStr(hashMap.get("FACE_VALUE")));
                txtCurrValue.setText(CommonUtil.convertObjToStr(hashMap.get("CURR_VALUE")));
                
                observable.setTxtBranchId(CommonUtil.convertObjToStr(hashMap.get("SUPPLIED_BRANCH_ID")));
                observable.setTxtDepart(CommonUtil.convertObjToStr(hashMap.get("DEPARTMENT")));
                observable.setTxtFloor(CommonUtil.convertObjToStr(hashMap.get("FLOOR")));
            }
            if(viewType == FROM_ID){
                txtFromAssetId.setText(CommonUtil.convertObjToStr(hashMap.get("FA_ID")));
            }
            if(viewType == TO_ID ){
                txtToAssetId.setText(CommonUtil.convertObjToStr(hashMap.get("FA_ID")));
            }
            if(viewType == SALE_ID){
                txtAssetIdSale.setText(CommonUtil.convertObjToStr(hashMap.get("FA_ID")));
                txtPurchasedDate.setText(CommonUtil.convertObjToStr(hashMap.get("PURCHASED_DT")));
                txtFaceValueSale.setText(CommonUtil.convertObjToStr(hashMap.get("FACE_VALUE")));
                txtCurrentValueSale.setText(CommonUtil.convertObjToStr(hashMap.get("CURR_VALUE")));
            }
            if(viewType == DELETE){
                if(panSale.isShowing()==true){
                    cboProductTypeSale.setSelectedItem(CommonUtil.convertObjToStr(hashMap.get("ASSET_TYPE")));
                    cboSubTypeSale.setSelectedItem(CommonUtil.convertObjToStr(hashMap.get("ASSET_DESC")));
                    txtAssetIdSale.setText(CommonUtil.convertObjToStr(hashMap.get("FA_ID")));
                    txtPurchasedDate.setText(CommonUtil.convertObjToStr(hashMap.get("SALE_DATE")));
                    txtFaceValueSale.setText(CommonUtil.convertObjToStr(hashMap.get("FACE_VALUE")));
                    txtCurrentValueSale.setText(CommonUtil.convertObjToStr(hashMap.get("CURR_VALUE")));
                    System.out.println("saleebatchid>>>>>>>adss"+CommonUtil.convertObjToStr(hashMap.get("SALE_BATCH_ID")));
                    observable.setSaleBatchId(CommonUtil.convertObjToStr(hashMap.get("SALE_BATCH_ID")));
                }
                if(panDepreciation.isShowing()==true){
                    observable.setDeprBatchId(CommonUtil.convertObjToStr(hashMap.get("DEPR_BATCH_ID")));
                    System.out.println("inghvasdghasg>>"+hashMap.get("DEPR_BATCH_ID"));
                    HashMap where = new HashMap();
                    //System.out.println("hash.get"+hash.get("BORROWING_NO"));
                    where.put("DEPR_BATCH_ID", hashMap.get("DEPR_BATCH_ID"));
                    // where.put(CommonConstants.BRANCH_ID, "0001");
                    hashMap.put(CommonConstants.MAP_WHERE, where);
                    observable.populateData(hashMap,pan);
                    observable.populateDeprTablData(String.valueOf(hashMap.get("DEPR_BATCH_ID")));
                    // initTableData();
                    tblDepreciationList.setModel(observable.getTblDepreciationList());//tblDepreciationList
                }
                if(panMovement.isShowing()==true){
                    observable.setMoveBatchId(CommonUtil.convertObjToStr(hashMap.get("MOVE_BATCH_ID")));
                    HashMap where = new HashMap();
                    //System.out.println("hash.get"+hash.get("BORROWING_NO"));
                    where.put("MOVE_BATCH_ID", hashMap.get("MOVE_BATCH_ID"));
                    // where.put(CommonConstants.BRANCH_ID, "0001");
                    hashMap.put(CommonConstants.MAP_WHERE, where);
                    observable.populateData(hashMap,pan);
                }
                if(panBrokage.isShowing()==true){
                    observable.setBrkBatchId(CommonUtil.convertObjToStr(hashMap.get("BREAK_BATCH_ID")));
                    HashMap where = new HashMap();
                    //System.out.println("hash.get"+hash.get("BORROWING_NO"));
                    where.put("BREAK_BATCH_ID", hashMap.get("BREAK_BATCH_ID"));
                    // where.put(CommonConstants.BRANCH_ID, "0001");
                    hashMap.put(CommonConstants.MAP_WHERE, where);
                    observable.populateData(hashMap,pan);
                }
                
                
                
            }
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                this.setButtonEnableDisable();
                if(panDepreciation.isShowing()==true){
                    panEditDelete=DEPRECIATION;
                    observable.setDeprBatchId(CommonUtil.convertObjToStr(hashMap.get("DEPR_BATCH_ID")));
                    observable.setLblDepBatchId(CommonUtil.convertObjToStr(hashMap.get("DEPR_BATCH_ID")));
                    observable.populateDepreciationData(String.valueOf(hashMap.get("DEPR_BATCH_ID")),panEditDelete);
                    populateTable();
                    lblDepBatchId.setText(observable.getLblDepBatchId());
                }
                else if(panSale.isShowing()==true){
                    panEditDelete=SALE;
                    observable.setSaleBatchId(CommonUtil.convertObjToStr(hashMap.get("SALE_BATCH_ID")));
                    System.out.println("kjghkfghjwefghjkwefg>>>"+CommonUtil.convertObjToStr(hashMap.get("SALE_BATCH_ID")));
                    observable.populateDepreciationData(String.valueOf(hashMap.get("SALE_BATCH_ID")),panEditDelete);
                    populateTable();
                }
                else if(panMovement.isShowing()==true){
                    panEditDelete=MOVEMENT;
                    observable.setMoveBatchId(CommonUtil.convertObjToStr(hashMap.get("MOVE_BATCH_ID")));
                    observable.populateDepreciationData(String.valueOf(hashMap.get("MOVE_BATCH_ID")),panEditDelete);
                    populateTable();
                }
                else if(panBrokage.isShowing()==true){
                    panEditDelete=BREAKAGE;
                    observable.setBrkBatchId(CommonUtil.convertObjToStr(hashMap.get("BREAK_BATCH_ID")));
                    observable.populateDepreciationData(String.valueOf(hashMap.get("BREAK_BATCH_ID")),panEditDelete);
                populateTable();
                }
                
                
                
                btnCancel.setEnabled(true);
            }
            hashMap = null;
            returnMap = null;
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
    private void deletescreenLock(){
        HashMap map=new HashMap();
        map.put("USER_ID",ProxyParameters.USER_ID);
        map.put("TRANS_DT", currDt.clone());
        map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }
    
    private void resetSaleData(){
        cboProductTypeSale.setSelectedItem(null);
        cboSubTypeSale.setSelectedItem(null);
        txtAssetIdSale.setText("");
        txtPurchasedDate.setText("");
        txtFaceValueSale.setText("");
        txtCurrentValueSale.setText("");
    }
    private void resetDeprec(){
        cboProductType.setSelectedItem("");
        cboSubType.setSelectedItem("");
        txtFromAssetId.setText("");
        txtToAssetId.setText("");
        tdtDepDate.setDateValue(null);
    }
    private void resetMovement(){
        txtAssetIdMove.setText("");
        txtSlNo.setText("");
        txtBranchId.setText("");
        txtDepart.setText("");
        txtFloor.setText("");
        txtFaceVal.setText("");
        txtCurrValue.setText("");
        cboBranchIdMove.setSelectedItem("");
        cboDepartMove.setSelectedItem("");
        txtFloorMove.setText("");
    }
    
    private void resetBreakage(){
        txtAssetIdBreak.setText("");
        txtSlNoBreak.setText("");
        txtBranchIdBreak.setText("");
        txtDepartBreak.setText("");
        txtFloorBreak.setText("");
        txtFaceValBreak.setText("");
        txtCurrValueBreak.setText("");
        txtBreakageRegion.setText("");
    }
    
    private void resetSale(){
        cboProductTypeSale.setSelectedItem("");
        cboSubTypeSale.setSelectedItem("");
        txtAssetIdSale.setText("");
        txtPurchasedDate.setText("");
        txtFaceValueSale.setText("");
        txtCurrentValueSale.setText("");
    }
    
    
    private void enableDisablePanButton(boolean flag){
        btnMoveNew.setEnabled(flag);
        btnMoveSave.setEnabled(flag);
        btnMoveDelete.setEnabled(flag);
        btnBreakageNew.setEnabled(flag);
        btnBreakageSave.setEnabled(flag);
        btnBreakageDelete.setEnabled(flag);
        btnSaleNew.setEnabled(flag);
        btnSaleSave.setEnabled(flag);
        btnSaleDelete.setEnabled(flag);
        
    }
    
    private void setMaxLengths(){
        txtBreakageRegion.setAllowAll(true);
    }
    
    private void enableDisable(boolean yesno){
        ClientUtil.enableDisable(this, yesno);
    }
    
    private void buttonDisable(boolean action){
        btnAssetIdSale.setEnabled(action);
        btnFromAssetId.setEnabled(action);
        btnToAssetId.setEnabled(action);
        btnAssetIdMove.setEnabled(action);
        btnAssetIdBreak.setEnabled(action);
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
        
        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        lblStatus.setText(observable.getLblStatus());
    }
    
    /** Method used to check whether the Mandatory Fields in the Form are Filled or not */
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    /** Method used to Give a Alert when any Mandatory Field is not filled by the user */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoApplType = new com.see.truetransact.uicomponent.CButtonGroup();
        panFixedAssets = new com.see.truetransact.uicomponent.CPanel();
        tabFixedAssets = new com.see.truetransact.uicomponent.CTabbedPane();
        panDepreciationSale = new com.see.truetransact.uicomponent.CPanel();
        tabDepreciationSale = new com.see.truetransact.uicomponent.CTabbedPane();
        panDepreciation = new com.see.truetransact.uicomponent.CPanel();
        panTransactionDetails = new com.see.truetransact.uicomponent.CPanel();
        cboProductType = new com.see.truetransact.uicomponent.CComboBox();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        lblSubType = new com.see.truetransact.uicomponent.CLabel();
        cboSubType = new com.see.truetransact.uicomponent.CComboBox();
        tdtDepDate = new com.see.truetransact.uicomponent.CDateField();
        lblDepDate = new com.see.truetransact.uicomponent.CLabel();
        lblFromAssetId = new com.see.truetransact.uicomponent.CLabel();
        txtFromAssetId = new com.see.truetransact.uicomponent.CTextField();
        txtToAssetId = new com.see.truetransact.uicomponent.CTextField();
        lblToAssetId = new com.see.truetransact.uicomponent.CLabel();
        btnFromAssetId = new com.see.truetransact.uicomponent.CButton();
        btnToAssetId = new com.see.truetransact.uicomponent.CButton();
        btnCalculate = new com.see.truetransact.uicomponent.CButton();
        lblDepBatchId = new com.see.truetransact.uicomponent.CLabel();
        srpTokenLost = new com.see.truetransact.uicomponent.CScrollPane();
        tblDepreciationList = new com.see.truetransact.uicomponent.CTable();
        panTotalTrans = new com.see.truetransact.uicomponent.CPanel();
        lblTotDepValue = new com.see.truetransact.uicomponent.CLabel();
        lblTotNewCurValue = new com.see.truetransact.uicomponent.CLabel();
        lblTotalValue = new com.see.truetransact.uicomponent.CLabel();
        panSale = new com.see.truetransact.uicomponent.CPanel();
        panSaleList = new com.see.truetransact.uicomponent.CPanel();
        srpTokenLostSale = new com.see.truetransact.uicomponent.CScrollPane();
        tblSaleList = new com.see.truetransact.uicomponent.CTable();
        panSaleDetails = new com.see.truetransact.uicomponent.CPanel();
        cboProductTypeSale = new com.see.truetransact.uicomponent.CComboBox();
        lblProductTypeSale = new com.see.truetransact.uicomponent.CLabel();
        lblSubTypeSale = new com.see.truetransact.uicomponent.CLabel();
        cboSubTypeSale = new com.see.truetransact.uicomponent.CComboBox();
        lblAssetIdSale = new com.see.truetransact.uicomponent.CLabel();
        txtAssetIdSale = new com.see.truetransact.uicomponent.CTextField();
        btnAssetIdSale = new com.see.truetransact.uicomponent.CButton();
        lblFaceValueSale = new com.see.truetransact.uicomponent.CLabel();
        lblCurrentValueSale = new com.see.truetransact.uicomponent.CLabel();
        txtFaceValueSale = new com.see.truetransact.uicomponent.CTextField();
        txtCurrentValueSale = new com.see.truetransact.uicomponent.CTextField();
        panSaleBtn = new com.see.truetransact.uicomponent.CPanel();
        btnSaleNew = new com.see.truetransact.uicomponent.CButton();
        btnSaleSave = new com.see.truetransact.uicomponent.CButton();
        btnSaleDelete = new com.see.truetransact.uicomponent.CButton();
        lblPurchasedDate = new com.see.truetransact.uicomponent.CLabel();
        txtPurchasedDate = new com.see.truetransact.uicomponent.CTextField();
        panTotalSale = new com.see.truetransact.uicomponent.CPanel();
        lblCurrentValueSale1 = new com.see.truetransact.uicomponent.CLabel();
        lblTotalCurrentValue = new com.see.truetransact.uicomponent.CLabel();
        panTrans = new com.see.truetransact.uicomponent.CPanel();
        panMovement = new com.see.truetransact.uicomponent.CPanel();
        panMoveList = new com.see.truetransact.uicomponent.CPanel();
        panMovementList = new com.see.truetransact.uicomponent.CPanel();
        lblAssetIdMove = new com.see.truetransact.uicomponent.CLabel();
        txtAssetIdMove = new com.see.truetransact.uicomponent.CTextField();
        btnAssetIdMove = new com.see.truetransact.uicomponent.CButton();
        lblSlNo = new com.see.truetransact.uicomponent.CLabel();
        txtSlNo = new com.see.truetransact.uicomponent.CTextField();
        lblBranchId = new com.see.truetransact.uicomponent.CLabel();
        lblDepart = new com.see.truetransact.uicomponent.CLabel();
        lblFloor = new com.see.truetransact.uicomponent.CLabel();
        txtFloor = new com.see.truetransact.uicomponent.CTextField();
        lblFaceVal = new com.see.truetransact.uicomponent.CLabel();
        txtFaceVal = new com.see.truetransact.uicomponent.CTextField();
        lblCurrValue = new com.see.truetransact.uicomponent.CLabel();
        txtCurrValue = new com.see.truetransact.uicomponent.CTextField();
        txtDepart = new com.see.truetransact.uicomponent.CTextField();
        txtBranchId = new com.see.truetransact.uicomponent.CTextField();
        panMovementDetails = new com.see.truetransact.uicomponent.CPanel();
        lblBranchIdMove = new com.see.truetransact.uicomponent.CLabel();
        cboBranchIdMove = new com.see.truetransact.uicomponent.CComboBox();
        lblDepartMove = new com.see.truetransact.uicomponent.CLabel();
        cboDepartMove = new com.see.truetransact.uicomponent.CComboBox();
        lblFloorMove = new com.see.truetransact.uicomponent.CLabel();
        txtFloorMove = new com.see.truetransact.uicomponent.CTextField();
        panMoveBtn = new com.see.truetransact.uicomponent.CPanel();
        btnMoveNew = new com.see.truetransact.uicomponent.CButton();
        btnMoveSave = new com.see.truetransact.uicomponent.CButton();
        btnMoveDelete = new com.see.truetransact.uicomponent.CButton();
        srpTokenLostMovement = new com.see.truetransact.uicomponent.CScrollPane();
        tblMovementList = new com.see.truetransact.uicomponent.CTable();
        panBrokage = new com.see.truetransact.uicomponent.CPanel();
        panBreakageDetails = new com.see.truetransact.uicomponent.CPanel();
        lblAssetIdBreak = new com.see.truetransact.uicomponent.CLabel();
        txtAssetIdBreak = new com.see.truetransact.uicomponent.CTextField();
        btnAssetIdBreak = new com.see.truetransact.uicomponent.CButton();
        txtSlNoBreak = new com.see.truetransact.uicomponent.CTextField();
        lblSlNoBreak = new com.see.truetransact.uicomponent.CLabel();
        lblBranchIdBreak = new com.see.truetransact.uicomponent.CLabel();
        lblDepartBreak = new com.see.truetransact.uicomponent.CLabel();
        lblFloorBreak = new com.see.truetransact.uicomponent.CLabel();
        txtFloorBreak = new com.see.truetransact.uicomponent.CTextField();
        lblFaceValBreak = new com.see.truetransact.uicomponent.CLabel();
        txtFaceValBreak = new com.see.truetransact.uicomponent.CTextField();
        lblBreakageRegion = new com.see.truetransact.uicomponent.CLabel();
        txtCurrValueBreak = new com.see.truetransact.uicomponent.CTextField();
        panBreakageBtn = new com.see.truetransact.uicomponent.CPanel();
        btnBreakageNew = new com.see.truetransact.uicomponent.CButton();
        btnBreakageSave = new com.see.truetransact.uicomponent.CButton();
        btnBreakageDelete = new com.see.truetransact.uicomponent.CButton();
        lblCurrValueBreak = new com.see.truetransact.uicomponent.CLabel();
        txtBreakageRegion = new com.see.truetransact.uicomponent.CTextField();
        txtDepartBreak = new com.see.truetransact.uicomponent.CTextField();
        txtBranchIdBreak = new com.see.truetransact.uicomponent.CTextField();
        srpTokenLostBreakage = new com.see.truetransact.uicomponent.CScrollPane();
        tblBreakageList = new com.see.truetransact.uicomponent.CTable();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace35 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace36 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace37 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace38 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace39 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace40 = new com.see.truetransact.uicomponent.CLabel();
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
        setMaximumSize(new java.awt.Dimension(800, 625));
        setMinimumSize(new java.awt.Dimension(800, 625));
        setPreferredSize(new java.awt.Dimension(800, 625));

        panFixedAssets.setMaximumSize(new java.awt.Dimension(650, 520));
        panFixedAssets.setMinimumSize(new java.awt.Dimension(650, 520));
        panFixedAssets.setPreferredSize(new java.awt.Dimension(650, 520));
        panFixedAssets.setLayout(new java.awt.GridBagLayout());

        panDepreciationSale.setLayout(new java.awt.GridBagLayout());

        tabDepreciationSale.setMinimumSize(new java.awt.Dimension(835, 580));

        panDepreciation.setLayout(new java.awt.GridBagLayout());

        panTransactionDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTransactionDetails.setMinimumSize(new java.awt.Dimension(700, 180));
        panTransactionDetails.setPreferredSize(new java.awt.Dimension(700, 180));
        panTransactionDetails.setLayout(new java.awt.GridBagLayout());

        cboProductType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 3, 0);
        panTransactionDetails.add(cboProductType, gridBagConstraints);

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 11, 3, 0);
        panTransactionDetails.add(lblProductType, gridBagConstraints);

        lblSubType.setText("Sub Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 11, 3, 0);
        panTransactionDetails.add(lblSubType, gridBagConstraints);

        cboSubType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSubType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSubTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 3, 0);
        panTransactionDetails.add(cboSubType, gridBagConstraints);

        tdtDepDate.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtDepDate.setName("tdtFromDate");
        tdtDepDate.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 3, 0);
        panTransactionDetails.add(tdtDepDate, gridBagConstraints);

        lblDepDate.setText("Depreciation Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 11, 3, 0);
        panTransactionDetails.add(lblDepDate, gridBagConstraints);

        lblFromAssetId.setText("From Asset ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 11, 3, 0);
        panTransactionDetails.add(lblFromAssetId, gridBagConstraints);

        txtFromAssetId.setBackground(new java.awt.Color(212, 208, 200));
        txtFromAssetId.setMaxLength(128);
        txtFromAssetId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromAssetId.setName("txtCompany");
        txtFromAssetId.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 3, 0);
        panTransactionDetails.add(txtFromAssetId, gridBagConstraints);

        txtToAssetId.setBackground(new java.awt.Color(212, 208, 200));
        txtToAssetId.setMaxLength(128);
        txtToAssetId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToAssetId.setName("txtCompany");
        txtToAssetId.setValidation(new DefaultValidation());
        txtToAssetId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtToAssetIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 3, 0);
        panTransactionDetails.add(txtToAssetId, gridBagConstraints);

        lblToAssetId.setText("To Asset ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 11, 3, 0);
        panTransactionDetails.add(lblToAssetId, gridBagConstraints);

        btnFromAssetId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromAssetId.setToolTipText("Select");
        btnFromAssetId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFromAssetId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromAssetIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 3, 28);
        panTransactionDetails.add(btnFromAssetId, gridBagConstraints);

        btnToAssetId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToAssetId.setToolTipText("Select");
        btnToAssetId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnToAssetId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToAssetIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 3, 26);
        panTransactionDetails.add(btnToAssetId, gridBagConstraints);

        btnCalculate.setText("Calculate");
        btnCalculate.setMinimumSize(new java.awt.Dimension(100, 27));
        btnCalculate.setPreferredSize(new java.awt.Dimension(100, 27));
        btnCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 11, 2, 1);
        panTransactionDetails.add(btnCalculate, gridBagConstraints);

        lblDepBatchId.setMaximumSize(new java.awt.Dimension(100, 20));
        lblDepBatchId.setMinimumSize(new java.awt.Dimension(100, 20));
        lblDepBatchId.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 11, 2, 1);
        panTransactionDetails.add(lblDepBatchId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        panDepreciation.add(panTransactionDetails, gridBagConstraints);

        srpTokenLost.setMaximumSize(new java.awt.Dimension(310, 450));
        srpTokenLost.setMinimumSize(new java.awt.Dimension(700, 155));
        srpTokenLost.setPreferredSize(new java.awt.Dimension(700, 155));

        tblDepreciationList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Asset ID", "Face Value", "Current Value", "Depreciation  %", "Depreciation Value", "New Current Value"
            }
        ));
        tblDepreciationList.setMaximumSize(new java.awt.Dimension(2147483647, 10000));
        tblDepreciationList.setMinimumSize(new java.awt.Dimension(385, 500));
        tblDepreciationList.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblDepreciationList.setPreferredSize(new java.awt.Dimension(385, 500));
        tblDepreciationList.setOpaque(false);
        srpTokenLost.setViewportView(tblDepreciationList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panDepreciation.add(srpTokenLost, gridBagConstraints);

        panTotalTrans.setMinimumSize(new java.awt.Dimension(695, 25));
        panTotalTrans.setPreferredSize(new java.awt.Dimension(695, 25));
        panTotalTrans.setLayout(new java.awt.GridBagLayout());

        lblTotDepValue.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblTotDepValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotDepValue.setMaximumSize(new java.awt.Dimension(80, 20));
        lblTotDepValue.setMinimumSize(new java.awt.Dimension(80, 20));
        lblTotDepValue.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 0, 0);
        panTotalTrans.add(lblTotDepValue, gridBagConstraints);

        lblTotNewCurValue.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblTotNewCurValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotNewCurValue.setMaximumSize(new java.awt.Dimension(80, 20));
        lblTotNewCurValue.setMinimumSize(new java.awt.Dimension(80, 20));
        lblTotNewCurValue.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        panTotalTrans.add(lblTotNewCurValue, gridBagConstraints);

        lblTotalValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTotalValue.setText("Total Value");
        lblTotalValue.setMaximumSize(new java.awt.Dimension(80, 20));
        lblTotalValue.setMinimumSize(new java.awt.Dimension(80, 20));
        lblTotalValue.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 350, 0, 0);
        panTotalTrans.add(lblTotalValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDepreciation.add(panTotalTrans, gridBagConstraints);

        tabDepreciationSale.addTab("Depreciation", panDepreciation);

        panSale.setMinimumSize(new java.awt.Dimension(830, 580));
        panSale.setPreferredSize(new java.awt.Dimension(830, 580));
        panSale.setLayout(new java.awt.GridBagLayout());

        panSaleList.setMinimumSize(new java.awt.Dimension(830, 280));
        panSaleList.setPreferredSize(new java.awt.Dimension(830, 280));
        panSaleList.setLayout(new java.awt.GridBagLayout());

        srpTokenLostSale.setMaximumSize(new java.awt.Dimension(310, 450));
        srpTokenLostSale.setMinimumSize(new java.awt.Dimension(470, 260));
        srpTokenLostSale.setPreferredSize(new java.awt.Dimension(470, 260));

        tblSaleList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Asset ID", "Face Value", "Current Value"
            }
        ));
        tblSaleList.setMaximumSize(new java.awt.Dimension(2147483647, 10000));
        tblSaleList.setMinimumSize(new java.awt.Dimension(350, 650));
        tblSaleList.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblSaleList.setPreferredSize(new java.awt.Dimension(350, 650));
        tblSaleList.setOpaque(false);
        tblSaleList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSaleListMousePressed(evt);
            }
        });
        srpTokenLostSale.setViewportView(tblSaleList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 0, 0);
        panSaleList.add(srpTokenLostSale, gridBagConstraints);

        panSaleDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Sale"));
        panSaleDetails.setMinimumSize(new java.awt.Dimension(320, 200));
        panSaleDetails.setPreferredSize(new java.awt.Dimension(320, 200));
        panSaleDetails.setLayout(new java.awt.GridBagLayout());

        cboProductTypeSale.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductTypeSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductTypeSaleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 3, 0);
        panSaleDetails.add(cboProductTypeSale, gridBagConstraints);

        lblProductTypeSale.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panSaleDetails.add(lblProductTypeSale, gridBagConstraints);

        lblSubTypeSale.setText("Sub Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panSaleDetails.add(lblSubTypeSale, gridBagConstraints);

        cboSubTypeSale.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 3, 0);
        panSaleDetails.add(cboSubTypeSale, gridBagConstraints);

        lblAssetIdSale.setText("Asset ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panSaleDetails.add(lblAssetIdSale, gridBagConstraints);

        txtAssetIdSale.setBackground(new java.awt.Color(212, 208, 200));
        txtAssetIdSale.setMaxLength(128);
        txtAssetIdSale.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAssetIdSale.setName("txtCompany");
        txtAssetIdSale.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 3, 0);
        panSaleDetails.add(txtAssetIdSale, gridBagConstraints);

        btnAssetIdSale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAssetIdSale.setToolTipText("Select");
        btnAssetIdSale.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAssetIdSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssetIdSaleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 2, 22);
        panSaleDetails.add(btnAssetIdSale, gridBagConstraints);

        lblFaceValueSale.setText("Face Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panSaleDetails.add(lblFaceValueSale, gridBagConstraints);

        lblCurrentValueSale.setText("Current Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panSaleDetails.add(lblCurrentValueSale, gridBagConstraints);

        txtFaceValueSale.setBackground(new java.awt.Color(212, 208, 200));
        txtFaceValueSale.setMaxLength(128);
        txtFaceValueSale.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFaceValueSale.setName("txtCompany");
        txtFaceValueSale.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 3, 0);
        panSaleDetails.add(txtFaceValueSale, gridBagConstraints);

        txtCurrentValueSale.setBackground(new java.awt.Color(212, 208, 200));
        txtCurrentValueSale.setMaxLength(128);
        txtCurrentValueSale.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCurrentValueSale.setName("txtCompany");
        txtCurrentValueSale.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 3, 0);
        panSaleDetails.add(txtCurrentValueSale, gridBagConstraints);

        panSaleBtn.setMinimumSize(new java.awt.Dimension(95, 35));
        panSaleBtn.setPreferredSize(new java.awt.Dimension(95, 35));
        panSaleBtn.setLayout(new java.awt.GridBagLayout());

        btnSaleNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnSaleNew.setToolTipText("New");
        btnSaleNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSaleNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSaleNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSaleNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaleNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSaleBtn.add(btnSaleNew, gridBagConstraints);

        btnSaleSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSaleSave.setToolTipText("Save");
        btnSaleSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSaleSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSaleSave.setName("btnContactNoAdd");
        btnSaleSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSaleSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaleSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSaleBtn.add(btnSaleSave, gridBagConstraints);

        btnSaleDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnSaleDelete.setToolTipText("Delete");
        btnSaleDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSaleDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSaleDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSaleBtn.add(btnSaleDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 3, 0);
        panSaleDetails.add(panSaleBtn, gridBagConstraints);

        lblPurchasedDate.setText("Purchased Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panSaleDetails.add(lblPurchasedDate, gridBagConstraints);

        txtPurchasedDate.setBackground(new java.awt.Color(212, 208, 200));
        txtPurchasedDate.setMaxLength(128);
        txtPurchasedDate.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPurchasedDate.setName("txtCompany");
        txtPurchasedDate.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 3, 0);
        panSaleDetails.add(txtPurchasedDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panSaleList.add(panSaleDetails, gridBagConstraints);

        panTotalSale.setMinimumSize(new java.awt.Dimension(470, 30));
        panTotalSale.setPreferredSize(new java.awt.Dimension(470, 30));
        panTotalSale.setLayout(new java.awt.GridBagLayout());

        lblCurrentValueSale1.setText("Total Current Value");
        lblCurrentValueSale1.setMaximumSize(new java.awt.Dimension(130, 20));
        lblCurrentValueSale1.setMinimumSize(new java.awt.Dimension(130, 20));
        lblCurrentValueSale1.setPreferredSize(new java.awt.Dimension(130, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 170, 0, 0);
        panTotalSale.add(lblCurrentValueSale1, gridBagConstraints);

        lblTotalCurrentValue.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblTotalCurrentValue.setMaximumSize(new java.awt.Dimension(120, 20));
        lblTotalCurrentValue.setMinimumSize(new java.awt.Dimension(120, 20));
        lblTotalCurrentValue.setPreferredSize(new java.awt.Dimension(120, 20));
        lblTotalCurrentValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                lblTotalCurrentValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 20);
        panTotalSale.add(lblTotalCurrentValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 25, 0);
        panSaleList.add(panTotalSale, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        panSale.add(panSaleList, gridBagConstraints);

        panTrans.setMaximumSize(new java.awt.Dimension(800, 190));
        panTrans.setMinimumSize(new java.awt.Dimension(800, 190));
        panTrans.setPreferredSize(new java.awt.Dimension(800, 190));
        panTrans.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSale.add(panTrans, gridBagConstraints);

        tabDepreciationSale.addTab("Sale", panSale);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panDepreciationSale.add(tabDepreciationSale, gridBagConstraints);

        tabFixedAssets.addTab("Depreciation/Sale", panDepreciationSale);

        panMovement.setLayout(new java.awt.GridBagLayout());

        panMoveList.setMinimumSize(new java.awt.Dimension(370, 400));
        panMoveList.setPreferredSize(new java.awt.Dimension(370, 400));
        panMoveList.setLayout(new java.awt.GridBagLayout());

        panMovementList.setBorder(javax.swing.BorderFactory.createTitledBorder("Movement"));
        panMovementList.setMinimumSize(new java.awt.Dimension(320, 200));
        panMovementList.setPreferredSize(new java.awt.Dimension(320, 200));
        panMovementList.setLayout(new java.awt.GridBagLayout());

        lblAssetIdMove.setText("Asset ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        panMovementList.add(lblAssetIdMove, gridBagConstraints);

        txtAssetIdMove.setBackground(new java.awt.Color(212, 208, 200));
        txtAssetIdMove.setMaxLength(128);
        txtAssetIdMove.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAssetIdMove.setName("txtCompany");
        txtAssetIdMove.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panMovementList.add(txtAssetIdMove, gridBagConstraints);

        btnAssetIdMove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAssetIdMove.setToolTipText("Select");
        btnAssetIdMove.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAssetIdMove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssetIdMoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 2, 22);
        panMovementList.add(btnAssetIdMove, gridBagConstraints);

        lblSlNo.setText("Serial Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        panMovementList.add(lblSlNo, gridBagConstraints);

        txtSlNo.setBackground(new java.awt.Color(212, 208, 200));
        txtSlNo.setMaxLength(128);
        txtSlNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSlNo.setName("txtCompany");
        txtSlNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panMovementList.add(txtSlNo, gridBagConstraints);

        lblBranchId.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        panMovementList.add(lblBranchId, gridBagConstraints);

        lblDepart.setText("Department");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        panMovementList.add(lblDepart, gridBagConstraints);

        lblFloor.setText("Floor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        panMovementList.add(lblFloor, gridBagConstraints);

        txtFloor.setBackground(new java.awt.Color(212, 208, 200));
        txtFloor.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFloor.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panMovementList.add(txtFloor, gridBagConstraints);

        lblFaceVal.setText("Face Value");
        lblFaceVal.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        panMovementList.add(lblFaceVal, gridBagConstraints);

        txtFaceVal.setBackground(new java.awt.Color(212, 208, 200));
        txtFaceVal.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFaceVal.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panMovementList.add(txtFaceVal, gridBagConstraints);

        lblCurrValue.setText("Current Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        panMovementList.add(lblCurrValue, gridBagConstraints);

        txtCurrValue.setBackground(new java.awt.Color(212, 208, 200));
        txtCurrValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCurrValue.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panMovementList.add(txtCurrValue, gridBagConstraints);

        txtDepart.setBackground(new java.awt.Color(212, 208, 200));
        txtDepart.setMaxLength(128);
        txtDepart.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDepart.setName("txtCompany");
        txtDepart.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panMovementList.add(txtDepart, gridBagConstraints);

        txtBranchId.setBackground(new java.awt.Color(212, 208, 200));
        txtBranchId.setMaxLength(128);
        txtBranchId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBranchId.setName("txtCompany");
        txtBranchId.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panMovementList.add(txtBranchId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        panMoveList.add(panMovementList, gridBagConstraints);

        panMovementDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Movement Details"));
        panMovementDetails.setMinimumSize(new java.awt.Dimension(320, 150));
        panMovementDetails.setPreferredSize(new java.awt.Dimension(320, 150));
        panMovementDetails.setLayout(new java.awt.GridBagLayout());

        lblBranchIdMove.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panMovementDetails.add(lblBranchIdMove, gridBagConstraints);

        cboBranchIdMove.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panMovementDetails.add(cboBranchIdMove, gridBagConstraints);

        lblDepartMove.setText("Department");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panMovementDetails.add(lblDepartMove, gridBagConstraints);

        cboDepartMove.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDepartMove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDepartMoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panMovementDetails.add(cboDepartMove, gridBagConstraints);

        lblFloorMove.setText("Floor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panMovementDetails.add(lblFloorMove, gridBagConstraints);

        txtFloorMove.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFloorMove.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panMovementDetails.add(txtFloorMove, gridBagConstraints);

        panMoveBtn.setMinimumSize(new java.awt.Dimension(95, 35));
        panMoveBtn.setPreferredSize(new java.awt.Dimension(95, 35));
        panMoveBtn.setLayout(new java.awt.GridBagLayout());

        btnMoveNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnMoveNew.setToolTipText("New");
        btnMoveNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnMoveNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnMoveNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnMoveNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMoveBtn.add(btnMoveNew, gridBagConstraints);

        btnMoveSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnMoveSave.setToolTipText("Save");
        btnMoveSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnMoveSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnMoveSave.setName("btnContactNoAdd");
        btnMoveSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnMoveSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMoveBtn.add(btnMoveSave, gridBagConstraints);

        btnMoveDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnMoveDelete.setToolTipText("Delete");
        btnMoveDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnMoveDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnMoveDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnMoveDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMoveBtn.add(btnMoveDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 3, 0);
        panMovementDetails.add(panMoveBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        panMoveList.add(panMovementDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        panMovement.add(panMoveList, gridBagConstraints);

        srpTokenLostMovement.setMaximumSize(new java.awt.Dimension(310, 450));
        srpTokenLostMovement.setMinimumSize(new java.awt.Dimension(400, 355));
        srpTokenLostMovement.setPreferredSize(new java.awt.Dimension(400, 355));

        tblMovementList.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null, null}
            },
            new String [] {
                "Asset ID", "Branch Code", "Department", "Floor"
            }
        ));
        tblMovementList.setMaximumSize(new java.awt.Dimension(2147483647, 10000));
        tblMovementList.setMinimumSize(new java.awt.Dimension(350, 750));
        tblMovementList.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblMovementList.setPreferredSize(new java.awt.Dimension(350, 1000));
        tblMovementList.setOpaque(false);
        tblMovementList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblMovementListMousePressed(evt);
            }
        });
        srpTokenLostMovement.setViewportView(tblMovementList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panMovement.add(srpTokenLostMovement, gridBagConstraints);

        tabFixedAssets.addTab("Movement", panMovement);

        panBrokage.setLayout(new java.awt.GridBagLayout());

        panBreakageDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Breakage Details"));
        panBreakageDetails.setMinimumSize(new java.awt.Dimension(370, 300));
        panBreakageDetails.setPreferredSize(new java.awt.Dimension(370, 300));
        panBreakageDetails.setLayout(new java.awt.GridBagLayout());

        lblAssetIdBreak.setText("Asset ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panBreakageDetails.add(lblAssetIdBreak, gridBagConstraints);

        txtAssetIdBreak.setBackground(new java.awt.Color(212, 208, 200));
        txtAssetIdBreak.setMaxLength(128);
        txtAssetIdBreak.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAssetIdBreak.setName("txtCompany");
        txtAssetIdBreak.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panBreakageDetails.add(txtAssetIdBreak, gridBagConstraints);

        btnAssetIdBreak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAssetIdBreak.setToolTipText("Select");
        btnAssetIdBreak.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAssetIdBreak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssetIdBreakActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 2, 22);
        panBreakageDetails.add(btnAssetIdBreak, gridBagConstraints);

        txtSlNoBreak.setBackground(new java.awt.Color(212, 208, 200));
        txtSlNoBreak.setMaxLength(128);
        txtSlNoBreak.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSlNoBreak.setName("txtCompany");
        txtSlNoBreak.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panBreakageDetails.add(txtSlNoBreak, gridBagConstraints);

        lblSlNoBreak.setText("Serial Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panBreakageDetails.add(lblSlNoBreak, gridBagConstraints);

        lblBranchIdBreak.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panBreakageDetails.add(lblBranchIdBreak, gridBagConstraints);

        lblDepartBreak.setText("Department");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panBreakageDetails.add(lblDepartBreak, gridBagConstraints);

        lblFloorBreak.setText("Floor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panBreakageDetails.add(lblFloorBreak, gridBagConstraints);

        txtFloorBreak.setBackground(new java.awt.Color(212, 208, 200));
        txtFloorBreak.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFloorBreak.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panBreakageDetails.add(txtFloorBreak, gridBagConstraints);

        lblFaceValBreak.setText("Face Value");
        lblFaceValBreak.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panBreakageDetails.add(lblFaceValBreak, gridBagConstraints);

        txtFaceValBreak.setBackground(new java.awt.Color(212, 208, 200));
        txtFaceValBreak.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFaceValBreak.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panBreakageDetails.add(txtFaceValBreak, gridBagConstraints);

        lblBreakageRegion.setText("Breakage Region");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panBreakageDetails.add(lblBreakageRegion, gridBagConstraints);

        txtCurrValueBreak.setBackground(new java.awt.Color(212, 208, 200));
        txtCurrValueBreak.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCurrValueBreak.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panBreakageDetails.add(txtCurrValueBreak, gridBagConstraints);

        panBreakageBtn.setMinimumSize(new java.awt.Dimension(100, 35));
        panBreakageBtn.setPreferredSize(new java.awt.Dimension(95, 35));
        panBreakageBtn.setLayout(new java.awt.GridBagLayout());

        btnBreakageNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnBreakageNew.setToolTipText("New");
        btnBreakageNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnBreakageNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnBreakageNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnBreakageNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBreakageNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBreakageBtn.add(btnBreakageNew, gridBagConstraints);

        btnBreakageSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnBreakageSave.setToolTipText("Save");
        btnBreakageSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnBreakageSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnBreakageSave.setName("btnContactNoAdd");
        btnBreakageSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnBreakageSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBreakageSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBreakageBtn.add(btnBreakageSave, gridBagConstraints);

        btnBreakageDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnBreakageDelete.setToolTipText("Delete");
        btnBreakageDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnBreakageDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnBreakageDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnBreakageDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBreakageDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 10);
        panBreakageBtn.add(btnBreakageDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        panBreakageDetails.add(panBreakageBtn, gridBagConstraints);

        lblCurrValueBreak.setText("Current Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panBreakageDetails.add(lblCurrValueBreak, gridBagConstraints);

        txtBreakageRegion.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panBreakageDetails.add(txtBreakageRegion, gridBagConstraints);

        txtDepartBreak.setBackground(new java.awt.Color(212, 208, 200));
        txtDepartBreak.setMaxLength(128);
        txtDepartBreak.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDepartBreak.setName("txtCompany");
        txtDepartBreak.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panBreakageDetails.add(txtDepartBreak, gridBagConstraints);

        txtBranchIdBreak.setBackground(new java.awt.Color(212, 208, 200));
        txtBranchIdBreak.setMaxLength(128);
        txtBranchIdBreak.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBranchIdBreak.setName("txtCompany");
        txtBranchIdBreak.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 0);
        panBreakageDetails.add(txtBranchIdBreak, gridBagConstraints);

        panBrokage.add(panBreakageDetails, new java.awt.GridBagConstraints());

        srpTokenLostBreakage.setMaximumSize(new java.awt.Dimension(310, 450));
        srpTokenLostBreakage.setMinimumSize(new java.awt.Dimension(400, 290));
        srpTokenLostBreakage.setPreferredSize(new java.awt.Dimension(400, 290));

        tblBreakageList.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
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
                "Asset ID", "Breakage Region"
            }
        ));
        tblBreakageList.setMaximumSize(new java.awt.Dimension(2147483647, 10000));
        tblBreakageList.setMinimumSize(new java.awt.Dimension(350, 750));
        tblBreakageList.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblBreakageList.setPreferredSize(new java.awt.Dimension(350, 750));
        tblBreakageList.setOpaque(false);
        tblBreakageList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblBreakageListMousePressed(evt);
            }
        });
        srpTokenLostBreakage.setViewportView(tblBreakageList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        panBrokage.add(srpTokenLostBreakage, gridBagConstraints);

        tabFixedAssets.addTab("Breakage", panBrokage);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panFixedAssets.add(tabFixedAssets, gridBagConstraints);

        getContentPane().add(panFixedAssets, java.awt.BorderLayout.CENTER);

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
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
        tbrOperativeAcctProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnNew);

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace34);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace35.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace35.setText("     ");
        lblSpace35.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace35);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
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
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnSave);

        lblSpace36.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace36.setText("     ");
        lblSpace36.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace36);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
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

        lblSpace37.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace37.setText("     ");
        lblSpace37.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace37);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        tbrOperativeAcctProduct.add(btnException);

        lblSpace38.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace38.setText("     ");
        lblSpace38.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace38);

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
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace39.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace39.setText("     ");
        lblSpace39.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace39);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        lblSpace40.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace40.setText("     ");
        lblSpace40.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace40);

        btnDateChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/denomination.gif"))); // NOI18N
        btnDateChange.setToolTipText("Exception");
        btnDateChange.setDisabledIcon(null);
        btnDateChange.setDisabledSelectedIcon(null);
        tbrOperativeAcctProduct.add(btnDateChange);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

        mbrCustomer.setName("mbrCustomer");

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess");

        mitNew.setText("New");
        mitNew.setName("mitNew");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew");
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave");
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose");
        mnuProcess.add(mitClose);

        mbrCustomer.add(mnuProcess);

        setJMenuBar(mbrCustomer);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtToAssetIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtToAssetIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtToAssetIdActionPerformed
    
    private void cboSubTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSubTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboSubTypeActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnReject.setEnabled(true);
        btnException.setEnabled(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void lblTotalCurrentValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lblTotalCurrentValueFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_lblTotalCurrentValueFocusLost
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        // observable.setStatus("DELETE");
        callView(DELETE);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        btnSave.setEnabled(true);
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        lblStatus.setText("Authorize");
          observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
          authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
          btnReject.setEnabled(false);
          btnException.setEnabled(false);
          btnSave.setEnabled(false);
          btnCancel.setEnabled(true);
          btnAuthorize.setEnabled(true);
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled){
          
            if(panDepreciation.isShowing()==true){
                
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", authorizeStatus);
                singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("AUTH_DT",ClientUtil.getCurrentDateWithTime());
                singleAuthorizeMap.put("DEPR_BATCH_ID", observable.getDeprBatchId());
                singleAuthorizeMap.put("PAN","DEPR");
//                singleAuthorizeMap.put("DEPRECIATION_DATE",ClientUtil.getCurrentDateWithTime());
//                singleAuthorizeMap.put("BRANCH_CODE", observable.getDeprBranchCode());
                arrList.add(singleAuthorizeMap);
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorize(authorizeMap,observable.getDeprBatchId());
                viewType = -1;
                super.setOpenForEditBy(observable.getStatusBy());
                singleAuthorizeMap = null;
                authorizeMap = null;
            }
            
            if(panSale.isShowing()==true){
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", authorizeStatus);
                singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("AUTH_DT",ClientUtil.getCurrentDateWithTime());
                singleAuthorizeMap.put("SALE_BATCH_ID", observable.getSaleBatchId());
                singleAuthorizeMap.put("PAN","SALE");
                //                singleAuthorizeMap.put("DEPRECIATION_DATE",ClientUtil.getCurrentDateWithTime());
                //                singleAuthorizeMap.put("BRANCH_CODE", observable.getDeprBranchCode());
                arrList.add(singleAuthorizeMap);
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                System.out.println("observable.getSaleBatchId()>>>>"+observable.getSaleBatchId());
                System.out.println("authorizeMap>>>>1231111dsfsd"+authorizeMap);
                authorize(authorizeMap,observable.getSaleBatchId());
                viewType = -1;
                super.setOpenForEditBy(observable.getStatusBy());
                singleAuthorizeMap = null;
                authorizeMap = null;
                
            }
            
            if(panMovement.isShowing()==true){
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", authorizeStatus);
                singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("AUTH_DT",ClientUtil.getCurrentDateWithTime());
                singleAuthorizeMap.put("MOVE_BATCH_ID", observable.getMoveBatchId());
                singleAuthorizeMap.put("PAN","MOVEMENT");
                //                singleAuthorizeMap.put("DEPRECIATION_DATE",ClientUtil.getCurrentDateWithTime());
                //                singleAuthorizeMap.put("BRANCH_CODE", observable.getDeprBranchCode());
                arrList.add(singleAuthorizeMap);
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                System.out.println("observable.getMoveBatchId()>>>>"+observable.getMoveBatchId());
                System.out.println("authorizeMap>>>>123"+authorizeMap);
                authorize(authorizeMap,observable.getMoveBatchId());
                viewType = -1;
                super.setOpenForEditBy(observable.getStatusBy());
                singleAuthorizeMap = null;
                authorizeMap = null;
                
            }
            
            if(panBrokage.isShowing()==true){
                
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", authorizeStatus);
                singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("AUTH_DT",ClientUtil.getCurrentDateWithTime());
                singleAuthorizeMap.put("BREAK_BATCH_ID", observable.getBrkBatchId());
                singleAuthorizeMap.put("PAN","BROKAGE");
                //                singleAuthorizeMap.put("DEPRECIATION_DATE",ClientUtil.getCurrentDateWithTime());
                //                singleAuthorizeMap.put("BRANCH_CODE", observable.getDeprBranchCode());
                arrList.add(singleAuthorizeMap);
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorize(authorizeMap,observable.getBrkBatchId());
                viewType = -1;
                super.setOpenForEditBy(observable.getStatusBy());
                singleAuthorizeMap = null;
                authorizeMap = null;
                
            }
            
        }else {
            //__ To Save the data in the Internal Frame...
            HashMap whereMap = new HashMap();
            setModified(true);
            
            if(panDepreciation.isShowing()==true){
                whereMap.put(CommonConstants.MAP_NAME, "getSelectFixedAssetDepreciationAuthorize");
                panEditDelete=DEPRECIATION;
                pan = DEPRECIATION;
            }else if(panSale.isShowing()==true){
                whereMap.put(CommonConstants.MAP_NAME, "getSelectFixedAssetSaleAuthorize");
                panEditDelete=SALE;
                pan = SALE;
            }else if(panMovement.isShowing()==true){
                whereMap.put(CommonConstants.MAP_NAME, "getSelectFixedAssetMovementAuthorize");
                panEditDelete=MOVEMENT;
                pan = MOVEMENT;
            }else if(panBrokage.isShowing()==true){
                whereMap.put(CommonConstants.MAP_NAME, "getSelectFixedAssetBreakageAuthorize");
                panEditDelete=BREAKAGE;
                pan = BREAKAGE;
            }
            whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            viewType = AUTHORIZE;
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, whereMap);
            authorizeUI.show();
            whereMap = null;
        }
    }
    
    public void authorize(HashMap map,String id) {
        System.out.println("Authorize Map : " + map);
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            observable.doAction(pan);
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(id);
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    private void tblBreakageListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBreakageListMousePressed
        // TODO add your handling code here:
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            updateBreakage(tblBreakageList.getSelectedRow());
            //            enableDisableIncrement(false);
        }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            updateBreakage(tblBreakageList.getSelectedRow());
            
        }else{
            ClientUtil.enableDisable(this,false);
        }
        txtBreakageRegion.setEnabled(false);
        btnBreakageNew.setEnabled(false);
        btnBreakageSave.setEnabled(true);
        btnBreakageDelete.setEnabled(true);
    }//GEN-LAST:event_tblBreakageListMousePressed

    private void updateBreakage(int selectDARow) {
        observable.populateBreakage(selectDARow); 
        populateBreakageDetails();
    }
    
    private void populateBreakageDetails(){
        txtAssetIdBreak.setText(observable.getTxtAssetIdBreak());
        txtSlNoBreak.setText(observable.getTxtSlNoBreak());
        txtBranchIdBreak.setText(observable.getTxtBranchIdBreak());
        txtDepartBreak.setText(observable.getTxtDepartBreak());
        txtFloorBreak.setText(observable.getTxtFloorBreak());
        txtFaceValBreak.setText(observable.getTxtFaceValBreak());
        txtCurrValueBreak.setText(observable.getTxtCurrValueBreak());
        txtBreakageRegion.setText(observable.getTxtBreakageRegion());
    }
    
    private void tblSaleListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSaleListMousePressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tblSaleListMousePressed

    private void btnSaleNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaleNewActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        observable.setNewData(true);
//        enableDisableButton(true);
        cboProductTypeSale.setEnabled(true);
        cboSubTypeSale.setEnabled(true);
        btnAssetIdSale.setEnabled(true);
        btnSaleSave.setEnabled(true);
        btnSaleDelete.setEnabled(false);
        btnSaleNew.setEnabled(false);
    }//GEN-LAST:event_btnSaleNewActionPerformed

    private void btnSaleSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaleSaveActionPerformed
        // TODO add your handling code here:
        try{
            updateSaleFields();
            if(panSale.isShowing()==true){
                System.out.println("hjgfhweg");
                pan=SALE;
                
                //validation
                String mandatoryMessage ="";
                StringBuffer message = new StringBuffer(mandatoryMessage);
                
                
                resourceBundle = new FixedAssetsTransRB();
                //final String shareAcctMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDrfTransDetails);
                
                StringBuffer strBMandatory = new StringBuffer();
                if(cboProductTypeSale.getSelectedItem()==null) {
                    message.append(objMandatoryMRB.getString("cboProductTypeSale"));
                }
                if(cboSubTypeSale.getSelectedItem()==null) {
                    message.append(objMandatoryMRB.getString("cboSubTypeSale"));
                }
                if(txtAssetIdSale.getText().equals("")) {
                    message.append(objMandatoryMRB.getString("txtAssetIdSale"));
                }
                
                
                if(message.length() > 0 ) {
                    displayAlert(message.toString());
                    return;
                }
                
                //  updateSaleFields();
                //   btnCancelActionPerformed(evt);
            }
            if(txtAssetIdSale.getText().length()>0){
                String assetIdSale = txtAssetIdSale.getText();
                if(tblSaleList.getRowCount()>0) {
                    for(int i=0;i<tblSaleList.getRowCount();i++){
                        String idSale=CommonUtil.convertObjToStr(tblSaleList.getValueAt(i,0));
                        if(idSale.equalsIgnoreCase(assetIdSale) && !updateMode) {
                            ClientUtil.displayAlert("This Record Already Exists in this Table");
                            enableDisablePanButton(false);
                            resetSale();
                            btnSaleNew.setEnabled(true);
                            btnSaleSave.setEnabled(false);
                            btnAssetIdSale.setEnabled(false);
                            return;
                        }
                    }
                }
            }
            observable.addSaleMapTable(updateTab,updateMode);
            lblTotalCurrentValue.setText(observable.getLblTotalCurrentValue());
            transactionUI.setCallingAmount(lblTotalCurrentValue.getText());
            transactionUI.setCallingTransType("TRANSFER");
            double amountBorrowed = CommonUtil.convertObjToDouble(lblTotalCurrentValue.getText()).doubleValue();
            //  amountBorrowed = amountBorrowed + totalAmount;
            double sanctionAmount=CommonUtil.convertObjToDouble(lblTotalCurrentValue.getText()).doubleValue();
            if(amountBorrowed > sanctionAmount){
                ClientUtil.showAlertWindow("Amount Exceeds the Disbursment Limit!!");
                lblTotalCurrentValue.setText("");
                amountBorrowed = 0.0;
            }
            enableDisablePanButton(false);
            resetSale();
            btnSaleNew.setEnabled(true);
            btnAssetIdSale.setEnabled(false);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSaleSaveActionPerformed

    private void btnBreakageDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBreakageDeleteActionPerformed
        // TODO add your handling code here:
        String s=  CommonUtil.convertObjToStr(tblBreakageList.getValueAt(tblBreakageList.getSelectedRow(),0));
        observable.deleteBreakageTableData(s,tblBreakageList.getSelectedRow()); 
        resetBreakage();
        btnBreakageNew.setEnabled(true);
        btnBreakageDelete.setEnabled(false);
        btnBreakageSave.setEnabled(false);
    }//GEN-LAST:event_btnBreakageDeleteActionPerformed

    private void btnBreakageNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBreakageNewActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        observable.setNewData(true);
        btnAssetIdBreak.setEnabled(true);
//        enableDisableButton(true);
        btnAssetIdBreak.setEnabled(true);
        btnBreakageSave.setEnabled(true);
        btnBreakageDelete.setEnabled(false);
        btnBreakageNew.setEnabled(false);
    }//GEN-LAST:event_btnBreakageNewActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
            setModified(false);
        System.out.println("hjgfhweg2134321"+panSale.isShowing());
        
        if(panSale.isShowing()==true && tblSaleList.getRowCount()<=0 ){
            ClientUtil.showMessageWindow("Please enter values in the grid");
            return;
        }
        if(panDepreciation.isShowing()==true && tblDepreciationList.getRowCount()<=0 ){
            ClientUtil.showMessageWindow("Please enter values in the grid");
            return;
        }
        if(panMovement.isShowing()==true && tblMovementList.getRowCount()<=0 ){
            ClientUtil.showMessageWindow("Please enter values in the grid");
            return;
        }
        if(panBrokage.isShowing()==true && tblBreakageList.getRowCount()<=0 ){
            ClientUtil.showMessageWindow("Please enter values in the grid");
            return;
        }
            
            if(panDepreciation.isShowing()==true){
                pan=DEPRECIATION;
            
            //validation
            String mandatoryMessage ="";
            StringBuffer message = new StringBuffer(mandatoryMessage);
            
            
            resourceBundle = new FixedAssetsTransRB();
            //final String shareAcctMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDrfTransDetails);
            
            StringBuffer strBMandatory = new StringBuffer();
            if(cboProductType.getSelectedIndex()==0) {
                message.append(objMandatoryMRB.getString("cboProductType"));
            }
            
            if(cboSubType.getSelectedItem()==null) {
                message.append(objMandatoryMRB.getString("cboSubType"));
            }
            if(txtFromAssetId.getText().equals("")) {
                message.append(objMandatoryMRB.getString("txtFromAssetId"));
            }
            if(txtToAssetId.getText().equals("")) {
                message.append(objMandatoryMRB.getString("txtToAssetId"));
            }
            
            
            if(message.length() > 0 ) {
                displayAlert(message.toString());
                return;
            }
            
                updateDepreciationFields();
        }
        else if(panSale.isShowing()==true){
            System.out.println("hjgfhweg");
                pan=SALE;
            
        /*    //validation
            String mandatoryMessage ="";
            StringBuffer message = new StringBuffer(mandatoryMessage);
         
         
            resourceBundle = new FixedAssetsTransRB();
            //final String shareAcctMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDrfTransDetails);
         
            StringBuffer strBMandatory = new StringBuffer();
            if(cboProductTypeSale.getSelectedItem()==null) {
                message.append(objMandatoryMRB.getString("cboProductTypeSale"));
            }
            if(cboSubTypeSale.getSelectedItem()==null) {
                message.append(objMandatoryMRB.getString("cboSubTypeSale"));
            }
            if(txtAssetIdSale.getText().equals("")) {
                message.append(objMandatoryMRB.getString("txtAssetIdSale"));
            }
         
         
            if(message.length() > 0 ) {
                displayAlert(message.toString());
                return;
            }
         */
                updateSaleFields();
            //   btnCancelActionPerformed(evt);
            }else if(panMovement.isShowing()==true){
                pan=MOVEMENT;
            
            
       /*     //validation
            String mandatoryMessage ="";
            StringBuffer message = new StringBuffer(mandatoryMessage);
        
        
            resourceBundle = new FixedAssetsTransRB();
            //final String shareAcctMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDrfTransDetails);
        
            StringBuffer strBMandatory = new StringBuffer();
        
            if(txtAssetIdMove.getText().equals("")) {
                message.append(objMandatoryMRB.getString("txtAssetIdMove"));
            }
        
            if(cboBranchIdMove.getSelectedItem()==null) {
                message.append(objMandatoryMRB.getString("cboBranchIdMove"));
            }
        
            if(cboDepartMove.getSelectedItem()==null) {
                message.append(objMandatoryMRB.getString("cboDepartMove"));
            }
        
            if(txtFloorMove.getText().equals("")) {
                message.append(objMandatoryMRB.getString("txtFloorMove"));
            }
        
            if(message.length() > 0 ) {
                displayAlert(message.toString());
                return;
            }
        */
            
            // updateMovementFields();
            
        }  else if(panBrokage.isShowing()==true){
                pan=BREAKAGE;
            
          /*  //validation
            String mandatoryMessage ="";
            StringBuffer message = new StringBuffer(mandatoryMessage);
           
           
            resourceBundle = new FixedAssetsTransRB();
            //final String shareAcctMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDrfTransDetails);
           
            StringBuffer strBMandatory = new StringBuffer();
           
            if(txtAssetIdBreak.getText().equals("")) {
                message.append(objMandatoryMRB.getString("txtAssetIdBreak"));
            }
           
            if(txtBreakageRegion.getText().equals("")) {
                message.append(objMandatoryMRB.getString("txtBreakageRegion"));
            }
           
           
            if(message.length() > 0 ) {
                displayAlert(message.toString());
                return;
            }
           */
                updateBreakageFields();
            }
        if(panSale.isShowing()==true){
            //trans details
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                int transactionSize = 0 ;
                if(transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(lblTotalCurrentValue.getText()).doubleValue() > 0){
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
                    return;
                }else {
                    if(CommonUtil.convertObjToDouble(lblTotalCurrentValue.getText()).doubleValue()>0){
                        amtBorrow=CommonUtil.convertObjToDouble(lblTotalCurrentValue.getText()).doubleValue();
                        //     System.out.println("txtAmtBorrowed.getText()0000000000====="+txtAmtBorrowed.getText());
                        transactionSize = (transactionUI.getOutputTO()).size();
                        if(transactionSize != 1 && CommonUtil.convertObjToDouble(lblTotalCurrentValue.getText()).doubleValue()>0){
                            ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction") ;
                            return;
                        }else{
                            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                        }
                    }else if(transactionUI.getOutputTO().size()>0){
                        System.out.println("transop>>>>>>"+transactionUI.getOutputTO());
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    }
                }
                if(transactionSize == 0 && CommonUtil.convertObjToDouble(lblTotalCurrentValue.getText()).doubleValue()>0){
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
                    return;
                }else if(transactionSize != 0 ){
                    if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                        return;
                    }
                    if(transactionUI.getOutputTO().size()>0){
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                        
                        //   savePerformed();
                        
                    }
                }
                
            }
            
            //end..
        }
            observable.doAction(pan); 
        lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
        System.out.println("resultmap>>>>@@@@kjwdfgw"+observable.getProxyReturnMap());
        if(panSale.isShowing()==true && observable.getProxyReturnMap()!=null){
            //  if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
            if(observable.getProxyReturnMap().get("TRANSFER_TRANS_LIST")!=null || observable.getProxyReturnMap().get("CASH_TRANS_LIST")!=null){
                System.out.println("bbbkjhbjb");
                displayTransDetail(observable.getProxyReturnMap());//trans details
            }
            ClientUtil.showMessageWindow("Sale Id is : "+observable.getProxyReturnMap().get("FIXED_ASSETS_SAL_ID"));
            
        }
        
        if(panDepreciation.isShowing()==true && observable.getProxyReturnMap()!=null){
            ClientUtil.showMessageWindow("Depreciation Id is : "+observable.getProxyReturnMap().get("FIXED_ASSETS_DEPR_ID"));
        }
        
        if(panMovement.isShowing()==true && observable.getProxyReturnMap()!=null){
            ClientUtil.showMessageWindow("Movement Id is : "+observable.getProxyReturnMap().get("FIXED_ASSETS_MOVE_ID"));
        }
        
        if(panBrokage.isShowing()==true && observable.getProxyReturnMap()!=null){
            ClientUtil.showMessageWindow("Breakage Id is : "+observable.getProxyReturnMap().get("FIXED_ASSETS_BRK_ID"));
        }
        
        observable.setProxyReturnMap(null);
        //}
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                btnCancelActionPerformed(null);
                btnCancel.setEnabled(true);
                lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
            }
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
            btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnAssetIdSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAssetIdSaleActionPerformed
        // TODO add your handling code here:
        callView(SALE_ID); 
        cboProductTypeSale.setEnabled(false);
        cboSubTypeSale.setEnabled(false);
    }//GEN-LAST:event_btnAssetIdSaleActionPerformed

    private void cboProductTypeSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductTypeSaleActionPerformed
        // TODO add your handling code here:
        if(cboProductTypeSale.getSelectedIndex()>0){
            HashMap intTangibleMap = new HashMap();
            intTangibleMap.put("ASSET_TYPE",cboProductTypeSale.getSelectedItem());
            observable.setAssetSubTypeSale(intTangibleMap); 
            cboSubTypeSale.setModel(observable.getCbmSubTypeSale());
            observable.setTxtAssetId((String)observable.getCbmProductTypeSale().getKeyForSelected());
        }
    }//GEN-LAST:event_cboProductTypeSaleActionPerformed

    private void btnCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateActionPerformed
        // TODO add your handling code here:
        System.out.println("jhbjkdhadkjwq???>>>111111");
        if(txtFromAssetId.getText().equals("") || txtToAssetId.getText().equals("")){
            System.out.println("jhbjkdhadkjwq???>>>");
            ClientUtil.showMessageWindow("Please fill the above text fields ");
        }
        Map where = new HashMap();
        where.put("FROM_ID",txtFromAssetId.getText());
        where.put("TO_ID",txtToAssetId.getText());
        where.put("ASSET_TYPE",cboProductType.getSelectedItem());
        where.put("ASSET_DESC",cboSubType.getSelectedItem());
        where.put("LAST_PROV_DT",tdtDepDate.getDateValue());
        observable.populateDepreciationTable(where);
        populateTable();
    }//GEN-LAST:event_btnCalculateActionPerformed

    private void populateTable() {
        tblDepreciationList.setModel(observable.getTblDepreciationList());
        tblSaleList.setModel(observable.getTblSaleList());
        tblMovementList.setModel(observable.getTblEmpDetails());
        tblBreakageList.setModel(observable.getTblBreakageList());
        lblTotNewCurValue.setText(observable.getLblTotNewCurValue());
        lblTotDepValue.setText(observable.getLblTotDepValue());
        txtFromAssetId.setEnabled(false);
        txtToAssetId.setEnabled(false);
        btnFromAssetId.setEnabled(false); 
        btnToAssetId.setEnabled(false);
        btnCalculate.setEnabled(false);
        tdtDepDate.setEnabled(false);        
    }
    private void btnToAssetIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToAssetIdActionPerformed
        // TODO add your handling code here:
        callView(TO_ID);
    }//GEN-LAST:event_btnToAssetIdActionPerformed

    private void btnFromAssetIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromAssetIdActionPerformed
        // TODO add your handling code here:
        callView(FROM_ID); 
        cboProductType.setEnabled(false);
        cboSubType.setEnabled(false);
    }//GEN-LAST:event_btnFromAssetIdActionPerformed

    private void btnMoveNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveNewActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        observable.setNewData(true);
        btnAssetIdMove.setEnabled(true);
//        enableDisableButton(true);
        cboBranchIdMove.setEnabled(true);
        cboDepartMove.setEnabled(true);
        txtFloorMove.setEnabled(true);
        btnMoveSave.setEnabled(true);
        btnMoveDelete.setEnabled(false);
        btnMoveNew.setEnabled(false);
    }//GEN-LAST:event_btnMoveNewActionPerformed

    private void btnMoveDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveDeleteActionPerformed
        // TODO add your handling code here:
        String s=  CommonUtil.convertObjToStr(tblMovementList.getValueAt(tblMovementList.getSelectedRow(),0));
   //     observable.deleteTableData(s,tblMovementList.getSelectedRow());
   //     enableDisableButton(false);
        enableDisablePanButton(false);
        btnMoveNew.setEnabled(true);
    }//GEN-LAST:event_btnMoveDeleteActionPerformed

    private void btnMoveSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveSaveActionPerformed
        // TODO add your handling code here:
        try{
            updateMovementFields();
            
            if(panMovement.isShowing()==true){
                pan=MOVEMENT;
                
                
                //validation
                String mandatoryMessage ="";
                StringBuffer message = new StringBuffer(mandatoryMessage);
                
                
                resourceBundle = new FixedAssetsTransRB();
                //final String shareAcctMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDrfTransDetails);
                
                StringBuffer strBMandatory = new StringBuffer();
                
                if(txtAssetIdMove.getText().equals("")) {
                    message.append(objMandatoryMRB.getString("txtAssetIdMove"));
                }
                
                if(cboBranchIdMove.getSelectedItem()==null) {
                    message.append(objMandatoryMRB.getString("cboBranchIdMove"));
                }
                
                if(cboDepartMove.getSelectedItem()==null) {
                    message.append(objMandatoryMRB.getString("cboDepartMove"));
                }
                
                if(txtFloorMove.getText().equals("")) {
                    message.append(objMandatoryMRB.getString("txtFloorMove"));
                }
                
                if(message.length() > 0 ) {
                    displayAlert(message.toString());
                    return;
                }
                
                
                // updateMovementFields();
                
            }
            
            if(txtAssetIdMove.getText().length()>0){
                String assetIdMove = txtAssetIdMove.getText();
                if(tblMovementList.getRowCount()>0) {
                    for(int i=0;i<tblMovementList.getRowCount();i++){
                        String idMove=CommonUtil.convertObjToStr(tblMovementList.getValueAt(i,0));
                        if(idMove.equalsIgnoreCase(assetIdMove) && !updateMode) {
                            ClientUtil.displayAlert("This Record Already Exists in this Table");
                            enableDisablePanButton(false);
                            btnMoveNew.setEnabled(true);
                            ClientUtil.enableDisable(panMovementList, false);
                            resetMovement();
                            btnMoveSave.setEnabled(false);
                            cboBranchIdMove.setEnabled(false);
                            cboDepartMove.setEnabled(false);
                            txtFloorMove.setEnabled(false);
                            return;
                        }
                    }
                }
            }
            observable.addMoveMapTable(updateTab,updateMode);
            enableDisablePanButton(false);
            btnMoveNew.setEnabled(true);
            ClientUtil.enableDisable(panMovementList, false);
            resetMovement();
            cboBranchIdMove.setEnabled(false);
            cboDepartMove.setEnabled(false);
            txtFloorMove.setEnabled(false);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnMoveSaveActionPerformed

    private void tblMovementListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMovementListMousePressed
        // TODO add your handling code here:
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            updateMovement(tblMovementList.getSelectedRow());
            //            enableDisableIncrement(false);
        }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            updateMovement(tblMovementList.getSelectedRow());
            
        }else{
            ClientUtil.enableDisable(this,false);
        }
        cboBranchIdMove.setEnabled(true);
        cboDepartMove.setEnabled(true);
        txtFloorMove.setEnabled(true);
        btnMoveNew.setEnabled(false);
        btnMoveSave.setEnabled(true);
        btnMoveDelete.setEnabled(true);
//        observable.notifyObservers();
//        updateOBFields();
//        updateMode = true;
//        updateTab= tblMovementList.getSelectedRow();
//        observable.setNewData(false);
//        String st=CommonUtil.convertObjToStr(tblMovementList.getValueAt(tblMovementList.getSelectedRow(),0));
//  //      observable.populateMovementDetails(st);
//        updateMovement();
// //       observable.resetEmpDetails();
//        enableDisablePanButton(true);
//        if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE||observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT ||
//        observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW || observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE ||
//        observable.getActionType()==ClientConstants.ACTIONTYPE_EXCEPTION){
//            enableDisablePanButton(false);
        
    }//GEN-LAST:event_tblMovementListMousePressed

    public void updateMovement(int selectDARow){
        observable.populateMovement(selectDARow);  
        populateMovementDetails();
     }
    
    private void populateMovementDetails(){
        txtAssetIdMove.setText(observable.getTxtAssetIdMove());
        txtSlNo.setText(observable.getTxtSlNo());
        txtBranchId.setText(observable.getTxtBranchId());
        txtDepart.setText(observable.getTxtDepart());
        txtFloor.setText(observable.getTxtFloor());
        txtFaceVal.setText(observable.getTxtCurrValue());
        txtCurrValue.setText(observable.getTxtCurrValue());
        txtFloorMove.setText(observable.getTxtFloorMove());
        cboBranchIdMove.setSelectedItem(observable.getCboBranchIdMove());
        cboDepartMove.setSelectedItem(observable.getCboDepartMove());
    }
    
    private void btnBreakageSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBreakageSaveActionPerformed
        // TODO add your handling code here:
        try{
            updateBreakageFields();
            
            //validation
            String mandatoryMessage ="";
            StringBuffer message = new StringBuffer(mandatoryMessage);
            
            
            resourceBundle = new FixedAssetsTransRB();
            //final String shareAcctMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDrfTransDetails);
            
            StringBuffer strBMandatory = new StringBuffer();
            
            if(txtAssetIdBreak.getText().equals("")) {
                message.append(objMandatoryMRB.getString("txtAssetIdBreak"));
            }
            
            if(txtBreakageRegion.getText().equals("")) {
                message.append(objMandatoryMRB.getString("txtBreakageRegion"));
            }
            
            
            if(message.length() > 0 ) {
                displayAlert(message.toString());
                return;
            }
            
            if(txtAssetIdBreak.getText().length()>0){
                String assetIdBreak = txtAssetIdBreak.getText();
                if(tblBreakageList.getRowCount()>0) {
                    for(int i=0;i<tblBreakageList.getRowCount();i++){
                        String idBreak=CommonUtil.convertObjToStr(tblBreakageList.getValueAt(i,0));
                        if(idBreak.equalsIgnoreCase(assetIdBreak) && !updateMode) {
                            ClientUtil.displayAlert("This Record Already Exists in this Table");
                            resetBreakage();
                            btnBreakageNew.setEnabled(true);
                            btnBreakageSave.setEnabled(false);
                            txtBreakageRegion.setEnabled(false);
                            return;
                        }
                    }
                }
            }
            observable.addBreakageMapTable(updateTab,updateMode);
            enableDisablePanButton(false);
            resetBreakage();
            btnBreakageNew.setEnabled(true);
            txtBreakageRegion.setEnabled(false);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnBreakageSaveActionPerformed

    private void btnAssetIdMoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAssetIdMoveActionPerformed
        // TODO add your handling code here:
        callView(MOVEMENT_ID);
        ClientUtil.enableDisable(panMovementList, false);
    }//GEN-LAST:event_btnAssetIdMoveActionPerformed

    private void btnAssetIdBreakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAssetIdBreakActionPerformed
        // TODO add your handling code here:
        callView(BREAKAGE_ID);
        ClientUtil.enableDisable(panBreakageDetails, false);
        txtBreakageRegion.setEnabled(true);
        btnAssetIdBreak.setEnabled(false);
    }//GEN-LAST:event_btnAssetIdBreakActionPerformed
    private void callView(int viewType){
        HashMap viewMap = new HashMap();
        HashMap where = new HashMap();
        this.viewType = viewType;
        if(viewType == BREAKAGE_ID || viewType == MOVEMENT_ID ){  
            System.out.println("viewType>>>>>break"+viewType);
            viewMap.put(CommonConstants.MAP_NAME,"getSelectFixedAssetDetailsList");
            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
        }
        if(viewType == FROM_ID || viewType == TO_ID ){  
            viewMap.put(CommonConstants.MAP_NAME,"getSelectFixedAssetDepreciationIdDetailsList");
            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            where.put("ASSET_TYPE",cboProductType.getSelectedItem());
            where.put("ASSET_DESC",cboSubType.getSelectedItem());
            viewMap.put(CommonConstants.MAP_WHERE, where);
        }
        if(viewType == SALE_ID){  
            viewMap.put(CommonConstants.MAP_NAME,"getSelectFixedAssetSaleDetailsList");
            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            where.put("ASSET_TYPE",cboProductTypeSale.getSelectedItem());
            where.put("ASSET_DESC",cboSubTypeSale.getSelectedItem());
            viewMap.put(CommonConstants.MAP_WHERE, where);
        }
        
        if(viewType == DELETE){
            if(panSale.isShowing()==true){
                HashMap map = new HashMap();
                map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, map);
                viewMap.put(CommonConstants.MAP_NAME, "getSelectFixedAssetSaleDelete");
            }
            if(panDepreciation.isShowing()==true){
                pan=DEPRECIATION;
                viewMap.put(CommonConstants.MAP_NAME, "getSelectFixedAssetDepreciationDelete");
                ArrayList lst = new ArrayList();
                lst.add("DEPR_BATCH_ID");
                viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                lst = null;
            }
            if(panMovement.isShowing()==true){
                pan=MOVEMENT;
                viewMap.put(CommonConstants.MAP_NAME, "getSelectFixedAssetMovementDelete");
                ArrayList lst = new ArrayList();
                lst.add("MOVE_BATCH_ID");
                viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                lst = null;
            }
            if(panBrokage.isShowing()==true){
                pan=BREAKAGE;
                viewMap.put(CommonConstants.MAP_NAME, "getSelectFixedAssetBreakageDelete");
                ArrayList lst = new ArrayList();
                lst.add("BREAK_BATCH_ID");
                viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                lst = null;
            }
            
            
            
        }
        

//        }else if (viewType == EDIT || viewType == DELETE || viewType == VIEW ){
//            viewMap.put(CommonConstants.MAP_NAME,"getSelectIncrementDetails");
//            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
//            viewMap.put(CommonConstants.MAP_WHERE, where);
        
        new ViewAll(this,viewMap).show();  
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        // observable.resetForm();
        //resetMovement();
        setModified(true);
        // Testing for the Selection of Panels...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);   //Sets the Action Type to be performed...
        if(panDepreciation.isShowing()==true){
            panDepreEnable();    // To enable the Depreciation panel...
            pan=DEPRECIATION;
        }else if(panSale.isShowing()==true){
            panSaleEnable();    // To enable the Sale panel...
            pan=SALE;
        }else if(panMovement.isShowing()==true){
            panMovementEnable();    // To enable the Movement panel...
            pan=MOVEMENT;
            
        }if(panBrokage.isShowing()==true){
            panBreakageEnable();    // To enable the Breakage panel...
            pan=BREAKAGE;
        }
        resetUI(pan);      // to Reset all the Fields and Status in UI...
        if(panSale.isShowing()==true){
            //trans details
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
            //end..
        }
        setButtonEnableDisable();
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnPrint.setEnabled(false);
        observable.resetForm();
        resetSaleData();
        resetDeprec();
        resetMovement();
        observable.setStatus();
    }//GEN-LAST:event_btnNewActionPerformed
    
    // To enable the Depreciation panel...
     private void panDepreEnable(){
         ClientUtil.enableDisable(panDepreciation, true);// Enables all when the New button is pressed...
         tdtDepDate.setEnabled(false);
         btnFromAssetId.setEnabled(true);
         btnToAssetId.setEnabled(true);
         btnCalculate.setEnabled(true);
     }
     
     // To enable the Sale panel...
     private void panSaleEnable(){
         ClientUtil.enableDisable(panSaleDetails, false);// Enables all when the New button is pressed...
         btnAssetIdSale.setEnabled(false);
         btnSaleNew.setEnabled(true);
     }
     
     // To enable the Movement panel...
     private void panMovementEnable(){
         ClientUtil.enableDisable(panMovementList, true);// Enables all when the New button is pressed...
         btnAssetIdMove.setEnabled(true);
         btnMoveNew.setEnabled(true);
     }
     
     // To enable the Breakage panel...
     private void panBreakageEnable(){
         ClientUtil.enableDisable(panBreakageDetails, false);// Enables all when the New button is pressed...
         btnBreakageNew.setEnabled(true);
     }
     
     
     // to Reset all the Fields and Status in UI...
     private void resetUI(int value){
         
         if(value == DEPRECIATION){
             System.out.println("Reset Depreciation");
             observable.resetDepreciationDetails();
             
         }else if(value == SALE){
             System.out.println("Reset Sale");
             observable.resetSaleDetails();
             
         }else if(value == MOVEMENT){
             System.out.println("Reset Movement");
             observable.resetMovementDetails();
             
         }
         else if(value == BREAKAGE){
             System.out.println("Reset Breakage");
             observable.resetBreakageDetails();
             
         }
     }
     
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setModified(false);
        deletescreenLock();
        observable.resetForm();
        observable.setAuthorizeStatus("");
       // setHelpBtnEnableDisable(false);
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panBrokage, false);
        ClientUtil.enableDisable(panMovement, false);
        ClientUtil.enableDisable(panSale, false);
        ClientUtil.enableDisable(panDepreciation, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        buttonDisable(false);
        enableDisablePanButton(false);
        lblTotDepValue.setText("");
        lblTotNewCurValue.setText("");
        lblDepBatchId.setText("");
        lblTotalCurrentValue.setText("");
        viewType = -1;
        resetMovement();
        resetBreakage();
        lblStatus.setText("             ");
        isFilled = false;
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnSave.setEnabled(false);
        btnCalculate.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void cboDepartMoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDepartMoveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboDepartMoveActionPerformed
    
    private void cboProductTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductTypeActionPerformed
        // TODO add your handling code here:
        if(cboProductType.getSelectedIndex()>0){
            HashMap intTangibleMap = new HashMap();
            intTangibleMap.put("ASSET_TYPE",cboProductType.getSelectedItem());
            observable.setAssetSubType(intTangibleMap); 
            cboSubType.setModel(observable.getCbmSubType());
        }
    }//GEN-LAST:event_cboProductTypeActionPerformed
    
    //trans details
    private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap : " +proxyResultMap);
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        System.out.println("keeeeeeeeeeeyyy>>>>>>>>>>>"+keys);
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        HashMap transIdMap = new HashMap();
        HashMap transTypeMap = new HashMap();
        for (int i=0; i<keys.length; i++) {
            System.out.println("keeeeeeeeeeeyyy>66666666666667777>>>>>>>>>>"+keys[i]);
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList =(List)proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        cashDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        cashDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                    transTypeMap.put(transMap.get("TRANS_ID"),transMap.get("TRANS_TYPE"));
                    transIdMap.put(transMap.get("TRANS_ID"),"CASH");
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("BATCH_ID");
                    }
                    transferDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Batch Id : "+transMap.get("BATCH_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        transferDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        transferDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                    transIdMap.put(transMap.get("BATCH_ID"),"TRANSFER");
                }
                transferCount++;
            }
        }
        if(cashCount>0){
            displayStr+=cashDisplayStr;
        }
        if(transferCount>0){
            displayStr+=transferDisplayStr;
        }
        ClientUtil.showMessageWindow(""+displayStr);
        
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        System.out.println("#$#$$ yesNo : "+yesNo);
        if (yesNo==0) {
            //            TTIntegration ttIntgration = null;
            //            HashMap paramMap = new HashMap();
            //            paramMap.put("TransId", transId);
            //            paramMap.put("TransDt", observable.getCurrDt());
            //            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            //            ttIntgration.setParam(paramMap);
            //            ttIntgration.integrationForPrint("ReceiptPayment", false);
            
            TTIntegration ttIntgration = null;
            HashMap printParamMap = new HashMap();
            printParamMap.put("TransDt", observable.getCurrDt());
            printParamMap.put("BranchId", ProxyParameters.BRANCH_ID);
            Object keys1[] = transIdMap.keySet().toArray();
            for (int i=0; i<keys1.length; i++) {
                printParamMap.put("TransId", keys1[i]);
                ttIntgration.setParam(printParamMap);
                if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {
                    ttIntgration.integrationForPrint("ReceiptPayment");
                } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys1[i])).equals("DEBIT")) {
                    ttIntgration.integrationForPrint("CashPayment", false);
                } else {
                    ttIntgration.integrationForPrint("CashReceipt", false);
                }
            }
        }
    }
    //end...
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
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAssetIdBreak;
    private com.see.truetransact.uicomponent.CButton btnAssetIdMove;
    private com.see.truetransact.uicomponent.CButton btnAssetIdSale;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBreakageDelete;
    private com.see.truetransact.uicomponent.CButton btnBreakageNew;
    private com.see.truetransact.uicomponent.CButton btnBreakageSave;
    private com.see.truetransact.uicomponent.CButton btnCalculate;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDateChange;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnFromAssetId;
    private com.see.truetransact.uicomponent.CButton btnMoveDelete;
    private com.see.truetransact.uicomponent.CButton btnMoveNew;
    private com.see.truetransact.uicomponent.CButton btnMoveSave;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSaleDelete;
    private com.see.truetransact.uicomponent.CButton btnSaleNew;
    private com.see.truetransact.uicomponent.CButton btnSaleSave;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnToAssetId;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboBranchIdMove;
    private com.see.truetransact.uicomponent.CComboBox cboDepartMove;
    private com.see.truetransact.uicomponent.CComboBox cboProductType;
    private com.see.truetransact.uicomponent.CComboBox cboProductTypeSale;
    private com.see.truetransact.uicomponent.CComboBox cboSubType;
    private com.see.truetransact.uicomponent.CComboBox cboSubTypeSale;
    private com.see.truetransact.uicomponent.CLabel lblAssetIdBreak;
    private com.see.truetransact.uicomponent.CLabel lblAssetIdMove;
    private com.see.truetransact.uicomponent.CLabel lblAssetIdSale;
    private com.see.truetransact.uicomponent.CLabel lblBranchId;
    private com.see.truetransact.uicomponent.CLabel lblBranchIdBreak;
    private com.see.truetransact.uicomponent.CLabel lblBranchIdMove;
    private com.see.truetransact.uicomponent.CLabel lblBreakageRegion;
    private com.see.truetransact.uicomponent.CLabel lblCurrValue;
    private com.see.truetransact.uicomponent.CLabel lblCurrValueBreak;
    private com.see.truetransact.uicomponent.CLabel lblCurrentValueSale;
    private com.see.truetransact.uicomponent.CLabel lblCurrentValueSale1;
    private com.see.truetransact.uicomponent.CLabel lblDepBatchId;
    private com.see.truetransact.uicomponent.CLabel lblDepDate;
    private com.see.truetransact.uicomponent.CLabel lblDepart;
    private com.see.truetransact.uicomponent.CLabel lblDepartBreak;
    private com.see.truetransact.uicomponent.CLabel lblDepartMove;
    private com.see.truetransact.uicomponent.CLabel lblFaceVal;
    private com.see.truetransact.uicomponent.CLabel lblFaceValBreak;
    private com.see.truetransact.uicomponent.CLabel lblFaceValueSale;
    private com.see.truetransact.uicomponent.CLabel lblFloor;
    private com.see.truetransact.uicomponent.CLabel lblFloorBreak;
    private com.see.truetransact.uicomponent.CLabel lblFloorMove;
    private com.see.truetransact.uicomponent.CLabel lblFromAssetId;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblProductTypeSale;
    private com.see.truetransact.uicomponent.CLabel lblPurchasedDate;
    private com.see.truetransact.uicomponent.CLabel lblSlNo;
    private com.see.truetransact.uicomponent.CLabel lblSlNoBreak;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace35;
    private com.see.truetransact.uicomponent.CLabel lblSpace36;
    private com.see.truetransact.uicomponent.CLabel lblSpace37;
    private com.see.truetransact.uicomponent.CLabel lblSpace38;
    private com.see.truetransact.uicomponent.CLabel lblSpace39;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace40;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSubType;
    private com.see.truetransact.uicomponent.CLabel lblSubTypeSale;
    private com.see.truetransact.uicomponent.CLabel lblToAssetId;
    private com.see.truetransact.uicomponent.CLabel lblTotDepValue;
    private com.see.truetransact.uicomponent.CLabel lblTotNewCurValue;
    private com.see.truetransact.uicomponent.CLabel lblTotalCurrentValue;
    private com.see.truetransact.uicomponent.CLabel lblTotalValue;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panBreakageBtn;
    private com.see.truetransact.uicomponent.CPanel panBreakageDetails;
    private com.see.truetransact.uicomponent.CPanel panBrokage;
    private com.see.truetransact.uicomponent.CPanel panDepreciation;
    private com.see.truetransact.uicomponent.CPanel panDepreciationSale;
    private com.see.truetransact.uicomponent.CPanel panFixedAssets;
    private com.see.truetransact.uicomponent.CPanel panMoveBtn;
    private com.see.truetransact.uicomponent.CPanel panMoveList;
    private com.see.truetransact.uicomponent.CPanel panMovement;
    private com.see.truetransact.uicomponent.CPanel panMovementDetails;
    private com.see.truetransact.uicomponent.CPanel panMovementList;
    private com.see.truetransact.uicomponent.CPanel panSale;
    private com.see.truetransact.uicomponent.CPanel panSaleBtn;
    private com.see.truetransact.uicomponent.CPanel panSaleDetails;
    private com.see.truetransact.uicomponent.CPanel panSaleList;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTotalSale;
    private com.see.truetransact.uicomponent.CPanel panTotalTrans;
    private com.see.truetransact.uicomponent.CPanel panTrans;
    private com.see.truetransact.uicomponent.CPanel panTransactionDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdoApplType;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpTokenLost;
    private com.see.truetransact.uicomponent.CScrollPane srpTokenLostBreakage;
    private com.see.truetransact.uicomponent.CScrollPane srpTokenLostMovement;
    private com.see.truetransact.uicomponent.CScrollPane srpTokenLostSale;
    private com.see.truetransact.uicomponent.CTabbedPane tabDepreciationSale;
    private com.see.truetransact.uicomponent.CTabbedPane tabFixedAssets;
    private com.see.truetransact.uicomponent.CTable tblBreakageList;
    private com.see.truetransact.uicomponent.CTable tblDepreciationList;
    private com.see.truetransact.uicomponent.CTable tblMovementList;
    private com.see.truetransact.uicomponent.CTable tblSaleList;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtDepDate;
    private com.see.truetransact.uicomponent.CTextField txtAssetIdBreak;
    private com.see.truetransact.uicomponent.CTextField txtAssetIdMove;
    private com.see.truetransact.uicomponent.CTextField txtAssetIdSale;
    private com.see.truetransact.uicomponent.CTextField txtBranchId;
    private com.see.truetransact.uicomponent.CTextField txtBranchIdBreak;
    private com.see.truetransact.uicomponent.CTextField txtBreakageRegion;
    private com.see.truetransact.uicomponent.CTextField txtCurrValue;
    private com.see.truetransact.uicomponent.CTextField txtCurrValueBreak;
    private com.see.truetransact.uicomponent.CTextField txtCurrentValueSale;
    private com.see.truetransact.uicomponent.CTextField txtDepart;
    private com.see.truetransact.uicomponent.CTextField txtDepartBreak;
    private com.see.truetransact.uicomponent.CTextField txtFaceVal;
    private com.see.truetransact.uicomponent.CTextField txtFaceValBreak;
    private com.see.truetransact.uicomponent.CTextField txtFaceValueSale;
    private com.see.truetransact.uicomponent.CTextField txtFloor;
    private com.see.truetransact.uicomponent.CTextField txtFloorBreak;
    private com.see.truetransact.uicomponent.CTextField txtFloorMove;
    private com.see.truetransact.uicomponent.CTextField txtFromAssetId;
    private com.see.truetransact.uicomponent.CTextField txtPurchasedDate;
    private com.see.truetransact.uicomponent.CTextField txtSlNo;
    private com.see.truetransact.uicomponent.CTextField txtSlNoBreak;
    private com.see.truetransact.uicomponent.CTextField txtToAssetId;
    // End of variables declaration//GEN-END:variables
    
}