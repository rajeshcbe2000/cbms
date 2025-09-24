/*
* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DepoOB.java
 *
 * Created on September 12, 2011, 12:27 PM
 */

package com.see.truetransact.ui.indend;
import org.apache.log4j.Logger;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CObservable;
import java.util.LinkedHashMap;
import com.see.truetransact.transferobject.indend.depo.DepoTO;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.Date;
import com.see.truetransact.ui.common.transaction.TransactionOB ;

/**
 *
 * @author  user
 */

public class DepoOB extends CObservable{
    private String txtStoreNo = "", txtSalesmanId = "", txtDepoId = "", txtName = "", txtProfitPercentage="",
            txaRemarks = "", txtSalesgpHead = "", txtPurchasegpHead = "",
            txtSalesReturngpHead = "", txtPurchaseReturngpHead = "", txtDeficiategpHead = "",
            txtDamagegpHead = "", txtServiceTaxgpHead = "", txtVatTaxgpHead = "",
            txtMisIncomegpHead = "", txtComReciedgpHd = "", txtPurVatTaxgpHead = "", txtPurRetrnVatgpHead = "", txtSaleVatTaxgpHead = "", txtSaleRetrnVatTaxgpHead = "",
            txtDamageVatgpHead = "", txtDeficitVatgpHead = "", txtOtherExpnsgpHead = "", txtStockHd="";
    Double txtOpngStock, txtVatOpngStock;
    Date tdtStockasonDate = null;
    private static SqlMap sqlMap = null;
    private ComboBoxModel cbmStoreNo;//Model for ui combobox Agency
    private ComboBoxModel cbmSalesmanId;//Model for ui combobox Type
 
    private final static Logger log = Logger.getLogger(DepoOB.class);//Creating Instace of Log
    private ProxyFactory proxy;
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private static DepoOB objDepoOB;//Singleton Object Reference
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private ArrayList key,value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private HashMap authMap = new HashMap();
    private String txtDiscountGroupHead = "";
    private String txtDiscountVatGroupHead = ""; 
      
    /** Creates a new instance of NewBorrowingOB */
       public DepoOB() {
         try {
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initUIComboBoxModel();
           fillDropdown();
           }catch(Exception e){
            //parseException.logException(e,true);
                 System.out.println("Error in NewBorrowingOB():"+e);
        }
    }
     static {
        try {
            log.info("Creating ParameterOB...");
            objDepoOB = new DepoOB();
        } catch(Exception e) {
           // parseException.logException(e,true);
               System.out.println("Error in static():"+e);
        }
    }
       private void initUIComboBoxModel(){
        cbmStoreNo = new ComboBoxModel();
        cbmSalesmanId= new ComboBoxModel();
    }
     // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "DepoJNDI");
        map.put(CommonConstants.HOME, "depo.DepoHome");
        map.put(CommonConstants.REMOTE, "depo.Depo");
    }
        public com.see.truetransact.clientutil.ComboBoxModel getCbmStoreNo() {
        return cbmStoreNo;
    }
        public com.see.truetransact.clientutil.ComboBoxModel getCbmSalesmanId() {
        return cbmSalesmanId;
    }

    public String getTxtComReciedgpHd() {
        return txtComReciedgpHd;
    }

    public void setTxtComReciedgpHd(String txtComReciedgpHd) {
        this.txtComReciedgpHd = txtComReciedgpHd;
    }

    public String getTxtDamageVatgpHead() {
        return txtDamageVatgpHead;
    }

    public void setTxtDamageVatgpHead(String txtDamageVatgpHead) {
        this.txtDamageVatgpHead = txtDamageVatgpHead;
    }

    public String getTxtDeficitVatgpHead() {
        return txtDeficitVatgpHead;
    }

    public void setTxtDeficitVatgpHead(String txtDeficitVatgpHead) {
        this.txtDeficitVatgpHead = txtDeficitVatgpHead;
    }

    public String getTxtMisIncomegpHead() {
        return txtMisIncomegpHead;
    }

    public void setTxtMisIncomegpHead(String txtMisIncomegpHead) {
        this.txtMisIncomegpHead = txtMisIncomegpHead;
    }

    public String getTxtOtherExpnsgpHead() {
        return txtOtherExpnsgpHead;
    }

    public void setTxtOtherExpnsgpHead(String txtOtherExpnsgpHead) {
        this.txtOtherExpnsgpHead = txtOtherExpnsgpHead;
    }

    public String getTxtPurRetrnVatgpHead() {
        return txtPurRetrnVatgpHead;
    }

    public void setTxtPurRetrnVatgpHead(String txtPurRetrnVatgpHead) {
        this.txtPurRetrnVatgpHead = txtPurRetrnVatgpHead;
    }

    public String getTxtPurVatTaxgpHead() {
        return txtPurVatTaxgpHead;
    }

    public void setTxtPurVatTaxgpHead(String txtPurVatTaxgpHead) {
        this.txtPurVatTaxgpHead = txtPurVatTaxgpHead;
    }

    public String getTxtSaleRetrnVatTaxgpHead() {
        return txtSaleRetrnVatTaxgpHead;
    }

    public void setTxtSaleRetrnVatTaxgpHead(String txtSaleRetrnVatTaxgpHead) {
        this.txtSaleRetrnVatTaxgpHead = txtSaleRetrnVatTaxgpHead;
    }

    public String getTxtSaleVatTaxgpHead() {
        return txtSaleVatTaxgpHead;
    }

    public void setTxtSaleVatTaxgpHead(String txtSaleVatTaxgpHead) {
        this.txtSaleVatTaxgpHead = txtSaleVatTaxgpHead;
    }
   
 
     public static DepoOB getInstance()throws Exception{
        return objDepoOB;
    }
      public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
      //  System.out.println("setActionType IN"+actionType);
        setStatus();
        setChanged();
    }
      public void setStatus(){
       // this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    /**
     * Setter for property cbmTokenType.
     * @param cbmTokenType New value of property cbmTokenType.
     */
    public void setCbmStoreNo(com.see.truetransact.clientutil.ComboBoxModel cbmStoreNo) {
        this.cbmStoreNo = cbmStoreNo;
    }
    public void setCbmSalesmanId(com.see.truetransact.clientutil.ComboBoxModel cbmSalesmanId) {
        this.cbmSalesmanId = cbmSalesmanId;
    }
  
     /** Populates two ArrayList key,value */
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
     public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
             System.out.println("whereMap=="+whereMap);
             System.out.println("map=="+map);
            mapData = proxy.executeQuery(whereMap, map);
            System.out.println("objmapData=="+((List) mapData.get("DepoTO")).get(0));
            DepoTO objTO =(DepoTO) ((List) mapData.get("DepoTO")).get(0);
           System.out.println("objTOobjTOobjTOobjTO=="+objTO);
            setDepoTO(objTO);
           
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
           // parseException.logException(e,true);
               System.out.println("Error in populateData():"+e);
            
        }
    }
      public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }
       public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
      /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
       public int getResult(){
        return _result;
    }
      private void setDepoTO(DepoTO objTO){
     //   setBorrowingNo(objTO.getBorrowingNo());
          setTxtDepoId(objTO.getDepId());
          setTxtSalesmanId(objTO.getSalesId());
          setTxtStoreNo(objTO.getStoreId()); 
          setTxtProfitPercentage(CommonUtil.convertObjToStr(objTO.getProfitPercentage())); 
          setTxtSalesgpHead(objTO.getSalesachdId());
    
          setTxtName(objTO.getDeponame());
          //System.out.println("objTO.getOpngstock()>>>" + objTO.getOpngstock());
          setTxtOpngStock(objTO.getOpngstock());
          //System.out.println("objTO.getVatOpngStock()>>>" + objTO.getVatOpngStock());
          setTxtVatOpngStock(objTO.getVatOpngStock());
          setTdtStockasonDate(objTO.getStkasondate());
          setTxaRemarks(objTO.getRemarks());
          setTxtPurchasegpHead(objTO.getPurchaseachdId());
          setTxtDeficiategpHead(objTO.getDeficiateachdId());
          setTxtPurchaseReturngpHead(objTO.getPurretachdId());
          setTxtSalesReturngpHead(objTO.getSalretachdId());
          setTxtDamagegpHead(objTO.getDamageachdId());
          setTxtServiceTaxgpHead(objTO.getServiceTaxachdId());
          setTxtVatTaxgpHead(objTO.getVatachdId());
          setTxtMisIncomegpHead(objTO.getMisIncomegpHead());
          setTxtComReciedgpHd(objTO.getComReciedgpHd());
          setTxtPurVatTaxgpHead(objTO.getPurVatTaxgpHead());
          setTxtStockHd(objTO.getStockHd());
          setTxtPurRetrnVatgpHead(objTO.getPurRetrnVatgpHead());
          setTxtSaleVatTaxgpHead(objTO.getSaleVatTaxgpHead());
          setTxtSaleRetrnVatTaxgpHead(objTO.getSaleRetrnVatTaxgpHead());
          setTxtDamageVatgpHead(objTO.getDamageVatgpHead());
          setTxtDeficitVatgpHead(objTO.getDeficitVatgpHead());
          setTxtOtherExpnsgpHead(objTO.getOtherExpnsgpHead());
          setTxtDiscountGroupHead(objTO.getDiscountHead());// Added by nithya on 02-04-2020 for KD-1732
          setTxtDiscountVatGroupHead(objTO.getDiscountVatHead());

     //     setChanged();
        notifyObservers();
     
      
    }
     /**
     * Resets all the UI Fields
     */
    public void resetForm() {
        setTxtDepoId("");
        setTxtSalesmanId("");
        setTxtStoreNo("");
        setTxtProfitPercentage("");
        setTxtSalesgpHead("");
        setTxtName("");
        setTxtOpngStock(null);
        setTdtStockasonDate(null);
        setTxaRemarks("");
        setTxtPurchasegpHead("");
        setTxtPurchaseReturngpHead("");
        setTxtSalesReturngpHead("");
        setTxtDamagegpHead("");
        setTxtServiceTaxgpHead("");
        setTxtVatTaxgpHead("");
        setTxtDeficiategpHead("");
        setTxtMisIncomegpHead("");
        setTxtComReciedgpHd("");
        setTxtPurVatTaxgpHead("");
        setTxtStockHd("");
        setTxtPurRetrnVatgpHead("");
        setTxtVatTaxgpHead("");
        setTxtSaleVatTaxgpHead("");
        setTxtSaleRetrnVatTaxgpHead("");
        setTxtDamageVatgpHead("");
        setTxtDeficitVatgpHead("");
        setTxtOtherExpnsgpHead("");
        setTxtDiscountGroupHead(""); // added by nithya on 02-04-2020 for KD-1732
        setTxtDiscountVatGroupHead("");
        notifyObservers();
    }
    
      
         /* Executes Query using the TO object */
    public void execute(String command) {
        try {
          //  System.out.println("GET BOPRRR NO IN EDIT :="+geIndendTO(command));
           
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, "0001");
            term.put(CommonConstants.BRANCH_ID, "0001");
            term.put("DepoTO", geDepoTO(command));
             System.out.println("GET term IN EDIT :="+term);
              System.out.println("GET map IN EDIT :="+map);
            HashMap proxyReturnMap = proxy.execute(term, map);
//            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
            System.out.println("ACTIONN TYPEEE==="+getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //parseException.logException(e,true);
              System.out.println("Error in execute():"+e);
        }
     
    }
       private DepoTO geDepoTO(String command) {
        DepoTO objTO = new DepoTO();
        objTO.setCommand(command);
        objTO.setDepId(getTxtDepoId());
        objTO.setSalesId(getTxtSalesmanId());
        objTO.setStoreId(getTxtStoreNo());
        objTO.setProfitPercentage(CommonUtil.convertObjToDouble(getTxtProfitPercentage()));
        objTO.setSalesachdId(getTxtSalesgpHead());

        objTO.setDeponame(getTxtName());
        objTO.setOpngstock(getTxtOpngStock());
        //System.out.println("getTxtVatOpngStock()>>>" + getTxtVatOpngStock());
        objTO.setVatOpngStock(getTxtVatOpngStock());
        objTO.setStkasondate(getTdtStockasonDate());
        objTO.setRemarks(getTxaRemarks());
        objTO.setPurchaseachdId(getTxtPurchasegpHead());
        objTO.setPurretachdId(getTxtPurchaseReturngpHead());
        objTO.setSalretachdId(getTxtSalesReturngpHead());
        objTO.setDamageachdId(getTxtDamagegpHead());
        objTO.setServiceTaxachdId(getTxtServiceTaxgpHead());
        objTO.setVatachdId(getTxtVatTaxgpHead());
        objTO.setDeficiateachdId(getTxtDeficiategpHead());
        objTO.setMisIncomegpHead(getTxtMisIncomegpHead());
        objTO.setComReciedgpHd(getTxtComReciedgpHd());
        objTO.setPurVatTaxgpHead(getTxtPurVatTaxgpHead());
        objTO.setStockHd(getTxtStockHd());
        objTO.setPurRetrnVatgpHead(getTxtPurRetrnVatgpHead());
        objTO.setDamageVatgpHead(getTxtDamageVatgpHead());
        objTO.setDeficitVatgpHead(getTxtDeficitVatgpHead());
        objTO.setSaleVatTaxgpHead(getTxtSaleVatTaxgpHead());
        objTO.setSaleRetrnVatTaxgpHead(getTxtSaleRetrnVatTaxgpHead());
        objTO.setOtherExpnsgpHead(getTxtOtherExpnsgpHead());
        objTO.setDiscountHead(getTxtDiscountGroupHead()); // Added by nithya on 02-04-2020 for KD-1732
        objTO.setDiscountVatHead(getTxtDiscountVatGroupHead());

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            objTO.setAuthorizeStatus("");
            objTO.setAuthorizeBy("");
            objTO.setAuthorizeDte(null);
            objTO.setStatus("CREATED");
        }

        return objTO;
    }
           /**
     * Getter for property authMap.
     * @return Value of property authMap.
     */
    public java.util.HashMap getAuthMap() {
        return authMap;
    }
    
    /**
     * Setter for property authMap.
     * @param authMap New value of property authMap.
     */
    public void setAuthMap(java.util.HashMap authMap) {
        this.authMap = authMap;
    }
        /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{
        try{
            //This part is fill the agency dropdown
            log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
         //   final ArrayList lookup_keys = new ArrayList();
        //    lookup_keys.add("INDEND_DEPID");
          //  lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
          //  keyValue = ClientUtil.populateLookupData(lookUpHash);
        //    getKeyValue((HashMap)keyValue.get("INDEND_DEPID"));
           // cbmDepoId = new ComboBoxModel(key,value);
            
            
            
          //  List aList=ClientUtil.executeQuery("Indend.getIndendDepos",keyValue); 
          // cbmDepoId = new ComboBoxModel(aList);
            
            //This part is fill the type dropdown
           // final ArrayList lookup_keys1 = new ArrayList();
          //  lookup_keys1.add("INDEND_TRANSTYPE");
           // lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys1);
         //   keyValue = ClientUtil.populateLookupData(lookUpHash);
         //   getKeyValue((HashMap)keyValue.get("INDEND_TRANSTYPE"));
         //   cbmTransType = new ComboBoxModel(key,value);
           
            //This part is fill the principal and interest repayment dropdowns
           /*******************************/
            
        }catch(NullPointerException e){
           // parseException.logException(e,true);
             System.out.println("Error in fillDropdown():"+e);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /**
     * Getter for property txtDepoId.
     * @return Value of property txtDepoId.
     */
    public java.lang.String getTxtDepoId() {
        return txtDepoId;
    }    
  
    /**
     * Setter for property txtDepoId.
     * @param txtDepoId New value of property txtDepoId.
     */
    public void setTxtDepoId(java.lang.String txtDepoId) {
        this.txtDepoId = txtDepoId;
    }    
    
  
    

    
    /**
     * Getter for property txtName.
     * @return Value of property txtName.
     */
    public java.lang.String getTxtName() {
        return txtName;
    }
    
    /**
     * Setter for property txtName.
     * @param txtName New value of property txtName.
     */
    public void setTxtName(java.lang.String txtName) {
        this.txtName = txtName;
    }
    
    /**
     * Getter for property txaRemarks.
     * @return Value of property txaRemarks.
     */
    public java.lang.String getTxaRemarks() {
        return txaRemarks;
    }
    
    /**
     * Setter for property txaRemarks.
     * @param txaRemarks New value of property txaRemarks.
     */
    public void setTxaRemarks(java.lang.String txaRemarks) {
        this.txaRemarks = txaRemarks;
    }
    
    /**
     * Getter for property txtSalesgpHead.
     * @return Value of property txtSalesgpHead.
     */
    public java.lang.String getTxtSalesgpHead() {
        return txtSalesgpHead;
    }
    
    /**
     * Setter for property txtSalesgpHead.
     * @param txtSalesgpHead New value of property txtSalesgpHead.
     */
    public void setTxtSalesgpHead(java.lang.String txtSalesgpHead) {
        this.txtSalesgpHead = txtSalesgpHead;
    }
    
    /**
     * Getter for property txtPurchasegpHead.
     * @return Value of property txtPurchasegpHead.
     */
    public java.lang.String getTxtPurchasegpHead() {
        return txtPurchasegpHead;
    }
    
    /**
     * Setter for property txtPurchasegpHead.
     * @param txtPurchasegpHead New value of property txtPurchasegpHead.
     */
    public void setTxtPurchasegpHead(java.lang.String txtPurchasegpHead) {
        this.txtPurchasegpHead = txtPurchasegpHead;
    }
    
    /**
     * Getter for property txtSalesReturngpHead.
     * @return Value of property txtSalesReturngpHead.
     */
    public java.lang.String getTxtSalesReturngpHead() {
        return txtSalesReturngpHead;
    }
    
    /**
     * Setter for property txtSalesReturngpHead.
     * @param txtSalesReturngpHead New value of property txtSalesReturngpHead.
     */
    public void setTxtSalesReturngpHead(java.lang.String txtSalesReturngpHead) {
        this.txtSalesReturngpHead = txtSalesReturngpHead;
    }
    
    /**
     * Getter for property txtPurchaseReturngpHead.
     * @return Value of property txtPurchaseReturngpHead.
     */
    public java.lang.String getTxtPurchaseReturngpHead() {
        return txtPurchaseReturngpHead;
    }
    
    /**
     * Setter for property txtPurchaseReturngpHead.
     * @param txtPurchaseReturngpHead New value of property txtPurchaseReturngpHead.
     */
    public void setTxtPurchaseReturngpHead(java.lang.String txtPurchaseReturngpHead) {
        this.txtPurchaseReturngpHead = txtPurchaseReturngpHead;
    }
    
    /**
     * Getter for property txtDeficiategpHead.
     * @return Value of property txtDeficiategpHead.
     */
    public java.lang.String getTxtDeficiategpHead() {
        return txtDeficiategpHead;
    }
    
    /**
     * Setter for property txtDeficiategpHead.
     * @param txtDeficiategpHead New value of property txtDeficiategpHead.
     */
    public void setTxtDeficiategpHead(java.lang.String txtDeficiategpHead) {
        this.txtDeficiategpHead = txtDeficiategpHead;
    }
    
    /**
     * Getter for property txtDamagegpHead.
     * @return Value of property txtDamagegpHead.
     */
    public java.lang.String getTxtDamagegpHead() {
        return txtDamagegpHead;
    }
    
    /**
     * Setter for property txtDamagegpHead.
     * @param txtDamagegpHead New value of property txtDamagegpHead.
     */
    public void setTxtDamagegpHead(java.lang.String txtDamagegpHead) {
        this.txtDamagegpHead = txtDamagegpHead;
    }
    
    /**
     * Getter for property txtServiceTaxgpHead.
     * @return Value of property txtServiceTaxgpHead.
     */
    public java.lang.String getTxtServiceTaxgpHead() {
        return txtServiceTaxgpHead;
    }
    
    /**
     * Setter for property txtServiceTaxgpHead.
     * @param txtServiceTaxgpHead New value of property txtServiceTaxgpHead.
     */
    public void setTxtServiceTaxgpHead(java.lang.String txtServiceTaxgpHead) {
        this.txtServiceTaxgpHead = txtServiceTaxgpHead;
    }
    
    /**
     * Getter for property txtVatTaxgpHead.
     * @return Value of property txtVatTaxgpHead.
     */
    public java.lang.String getTxtVatTaxgpHead() {
        return txtVatTaxgpHead;
    }
    
    /**
     * Setter for property txtVatTaxgpHead.
     * @param txtVatTaxgpHead New value of property txtVatTaxgpHead.
     */
    public void setTxtVatTaxgpHead(java.lang.String txtVatTaxgpHead) {
        this.txtVatTaxgpHead = txtVatTaxgpHead;
    }
    
    /**
     * Getter for property txtOpngStock.
     * @return Value of property txtOpngStock.
     */
    public java.lang.Double getTxtOpngStock() {
        return txtOpngStock;
    }
    
    /**
     * Setter for property txtOpngStock.
     * @param txtOpngStock New value of property txtOpngStock.
     */
    public void setTxtOpngStock(java.lang.Double txtOpngStock) {
        this.txtOpngStock = txtOpngStock;
    }
    
    /**
     * Getter for property tdtStockasonDate.
     * @return Value of property tdtStockasonDate.
     */
    public java.util.Date getTdtStockasonDate() {
        return tdtStockasonDate;
    }
    
    /**
     * Setter for property tdtStockasonDate.
     * @param tdtStockasonDate New value of property tdtStockasonDate.
     */
    public void setTdtStockasonDate(java.util.Date tdtStockasonDate) {
        this.tdtStockasonDate = tdtStockasonDate;
    }

    public String getTxtProfitPercentage() {
        return txtProfitPercentage;
    }

    public void setTxtProfitPercentage(String txtProfitPercentage) {
        this.txtProfitPercentage = txtProfitPercentage;
    }
    
    
    /**
     * Getter for property txtStoreNo.
     * @return Value of property txtStoreNo.
     */
    public java.lang.String getTxtStoreNo() {
        return txtStoreNo;
    }
    
    /**
     * Setter for property txtStoreNo.
     * @param txtStoreNo New value of property txtStoreNo.
     */
    public void setTxtStoreNo(java.lang.String txtStoreNo) {
        this.txtStoreNo = txtStoreNo;
    }
    
    /**
     * Getter for property txtSalesmanId.
     * @return Value of property txtSalesmanId.
     */
    public java.lang.String getTxtSalesmanId() {
        return txtSalesmanId;
    }
    
    /**
     * Setter for property txtSalesmanId.
     * @param txtSalesmanId New value of property txtSalesmanId.
     */
    public void setTxtSalesmanId(java.lang.String txtSalesmanId) {
        this.txtSalesmanId = txtSalesmanId;
    }
    
    public Double getTxtVatOpngStock() {
        return txtVatOpngStock;
    }

    public void setTxtVatOpngStock(Double txtVatOpngStock) {
        this.txtVatOpngStock = txtVatOpngStock;
    }

    public String getTxtStockHd() {
        return txtStockHd;
    }

    public void setTxtStockHd(String txtStockHd) {
        this.txtStockHd = txtStockHd;
    }

    public String getTxtDiscountGroupHead() {
        return txtDiscountGroupHead;
    }

    public void setTxtDiscountGroupHead(String txtDiscountGroupHead) {
        this.txtDiscountGroupHead = txtDiscountGroupHead;
    }

    public String getTxtDiscountVatGroupHead() {
        return txtDiscountVatGroupHead;
    }

    public void setTxtDiscountVatGroupHead(String txtDiscountVatGroupHead) {
        this.txtDiscountVatGroupHead = txtDiscountVatGroupHead;
    }
    
}
