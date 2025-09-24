/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * PurchaseEntryOB.java
 */

package com.see.truetransact.ui.indend;

import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.purchaseentry.PurchaseEntryTO;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.uicomponent.CTable;
import java.util.Date;

/**
 *
 * @author  user
 */
public class PurchaseEntryOB extends CObservable {

    private double txtPurAmount = 0.0, txtPurComm = 0.0, txtPurchaseRet = 0.0, txtSundry = 0.0, txtInvestAcHead = 0.0, balnceCash = 0.0;
    private CTable _tblData;
    private HashMap dataHash;
    private ArrayList _heading;
    private ArrayList data;
    private ArrayList tblDatanew = new ArrayList();
    private int dataSize;
    private double  lblTotal;
    final ArrayList tableTitle = new ArrayList();
    private String purId = "";
    private String tradId = "";
    private static SqlMap sqlMap = null;
    private Date tdtFromDate;
    public double  txtFromWeight=0.0;
    public double  txtToWeight=0.0;
    public double  txtAmount=0.0;
    Date currDt = null;
    private final static Logger log = Logger.getLogger(PurchaseEntryOB.class);
    private ProxyFactory proxy;
    private int _actionType;
    private int _result;
    private static PurchaseEntryOB objOB;
    private HashMap map;
    private int operation;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private HashMap authMap = new HashMap();
    private HashMap lookUpHash;
    private ArrayList key, value;
    private HashMap keyValue;
    private int tabNo;
    private ComboBoxModel cbmProdId;
    private ComboBoxModel cbmSupplier;
    private String cboTransType = "";
    private String txtSupplier = "";
    private String txtPurchaseType = "";
    private boolean rdoInvestment;
    private boolean rdoSundry;
    private String cboProductId = "";
    private double txtCashAmount = 0.0;
    private String txtSupActnum = "";
    private String cboTransMode = "";
    private EnhancedTableModel tmbTradeExpense;
    private String txtAccountNumber = "";
    private String txtChequeNo = "";
    private String txtNarration = "";
    private String txtTradeNarration = "";
    public String getTxtPurchaseType() {
        return txtPurchaseType;
    }

    public void setTxtPurchaseType(String txtPurchaseType) {
        this.txtPurchaseType = txtPurchaseType;
    }

    public String getTxtTradeNarration() {
        return txtTradeNarration;
    }

    public void setTxtTradeNarration(String txtTradeNarration) {
        this.txtTradeNarration = txtTradeNarration;
    }

   
    public double getBalnceCash() {
        return balnceCash;
    }

    public void setBalnceCash(double balnceCash) {
        this.balnceCash = balnceCash;
    }

    public String getPurId() {
        return purId;
    }

    public void setPurId(String purId) {
        this.purId = purId;
    }

    public String getTradId() {
        return tradId;
    }

    public void setTradId(String tradId) {
        this.tradId = tradId;
    }

    public double getTxtInvestAcHead() {
        return txtInvestAcHead;
    }

    public void setTxtInvestAcHead(double txtInvestAcHead) {
        this.txtInvestAcHead = txtInvestAcHead;
    }

    public double getTxtPurAmount() {
        return txtPurAmount;
    }

    public void setTxtPurAmount(double txtPurAmount) {
        this.txtPurAmount = txtPurAmount;
    }

    public double getTxtPurComm() {
        return txtPurComm;
    }

    public void setTxtPurComm(double txtPurComm) {
        this.txtPurComm = txtPurComm;
    }

    public double getTxtPurchaseRet() {
        return txtPurchaseRet;
    }

    public void setTxtPurchaseRet(double txtPurchaseRet) {
        this.txtPurchaseRet = txtPurchaseRet;
    }

    public double getTxtSundry() {
        return txtSundry;
    }

    public void setTxtSundry(double txtSundry) {
        this.txtSundry = txtSundry;
    }

    public CTable getTblData() {
        return _tblData;
    }

    public void setTblData(CTable _tblData) {
        this._tblData = _tblData;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public HashMap getDataHash() {
        return dataHash;
    }

    public void setDataHash(HashMap dataHash) {
        this.dataHash = dataHash;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public double  getLblTotal() {
        return lblTotal;
    }

    public void setLblTotal(double  lblTotal) {
        this.lblTotal = lblTotal;
    }

    public Date getTdtFromDate() {
        return tdtFromDate;
    }

    public void setTdtFromDate(Date tdtFromDate) {
        this.tdtFromDate = tdtFromDate;
    }

    public double  getTxtAmount() {
        return txtAmount;
    }

    public void setTxtAmount(double  txtAmount) {
        this.txtAmount = txtAmount;
    }

    public double  getTxtFromWeight() {
        return txtFromWeight;
    }

    public void setTxtFromWeight(double  txtFromWeight) {
        this.txtFromWeight = txtFromWeight;
    }

    public double  getTxtToWeight() {
        return txtToWeight;
    }

    public void setTxtToWeight(double  txtToWeight) {
        this.txtToWeight = txtToWeight;
    }

    public String getTxtSupActnum() {
        return txtSupActnum;
    }

    public void setTxtSupActnum(String txtSupActnum) {
        this.txtSupActnum = txtSupActnum;
    }

    public double getTxtCashAmount() {
        return txtCashAmount;
    }

    public void setTxtCashAmount(double txtCashAmount) {
        this.txtCashAmount = txtCashAmount;
    }

    public EnhancedTableModel getTmbTradeExpense() {
        return tmbTradeExpense;
    }

    public void setTmbTradeExpense(EnhancedTableModel tmbTradeExpense) {
        this.tmbTradeExpense = tmbTradeExpense;
    }

    public String getCboProductId() {
        return cboProductId;
    }

    public void setCboProductId(String cboProductId) {
        this.cboProductId = cboProductId;
    }

    public String getCboTransType() {
        return cboTransType;
    }

    public void setCboTransType(String cboTransType) {
        this.cboTransType = cboTransType;
    }

    public boolean isRdoSundry() {
        return rdoSundry;
    }

    public void setRdoSundry(boolean rdoSundry) {
        this.rdoSundry = rdoSundry;
    }

    public String getTxtAccountNumber() {
        return txtAccountNumber;
    }

    public void setTxtAccountNumber(String txtAccountNumber) {
        this.txtAccountNumber = txtAccountNumber;
    }

    public String getTxtChequeNo() {
        return txtChequeNo;
    }

    public void setTxtChequeNo(String txtChequeNo) {
        this.txtChequeNo = txtChequeNo;
    }

    public String getTxtNarration() {
        return txtNarration;
    }

    public void setTxtNarration(String txtNarration) {
        this.txtNarration = txtNarration;
    }

    public String getTxtSupplier() {
        return txtSupplier;
    }

    public void setTxtSupplier(String txtSupplier) {
        this.txtSupplier = txtSupplier;
    }

    public boolean isRdoInvestment() {
        return rdoInvestment;
    }

    public void setRdoInvestment(boolean rdoInvestment) {
        this.rdoInvestment = rdoInvestment;
    }

    public ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }

    public void setCbmProdId(ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }

    public ComboBoxModel getCbmSupplier() {
        return cbmSupplier;
    }

    public void setCbmSupplier(ComboBoxModel cbmSupplier) {
        this.cbmSupplier = cbmSupplier;
    }

    public String getCboTransMode() {
        return cboTransMode;
    }

    public void setCboTransMode(String cboTransMode) {
        this.cboTransMode = cboTransMode;
    }

    public int getTabNo() {
        return tabNo;
    }

    public void setTabNo(int tabNo) {
        this.tabNo = tabNo;
    }
    
    public void populateData(HashMap mapID, CTable tblData) {
        //tblDatanew=new ArrayList();
        ArrayList tblDatalist = new ArrayList();
        tblDatalist.add(CommonUtil.convertObjToStr(mapID.get("FROM_DATE")));
        tblDatalist.add(CommonUtil.convertObjToStr(mapID.get("FROM_WEIGHT")));
        tblDatalist.add(CommonUtil.convertObjToStr(mapID.get("TO_WEIGHT")));
        tblDatalist.add(CommonUtil.convertObjToStr(mapID.get("AMOUNT")));
        List tmpList = tblDatalist;
        tblDatanew.add(tmpList);
        tmbTradeExpense = new EnhancedTableModel(tblDatanew, tableTitle);
        setDataSize(tblDatalist.size());
    }

    public PurchaseEntryOB() {

        try {
            setTableTitle();
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            fillDropdown();
        } catch (Exception e) {
        }
    }

    static {
        try {
            log.info("Creating ParameterOB...");
            objOB = new PurchaseEntryOB();
        } catch (Exception e) {
            System.out.println("Error in static():" + e);
        }

    }

    private void setTableTitle() {
        tableTitle.add("From date");
        tableTitle.add("Weight From");
        tableTitle.add("Weigth To");
        tableTitle.add("Amount");
    }

    private void fillDropdown() throws Exception {
        try {

            log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            setCbmProdId("AB");
            makeNull();

        } catch (NullPointerException e) {
            System.out.println("Error in fillDropdown():" + e);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    private void makeNull() {
        key = null;
        value = null;
    }

    public void setCbmProdId(String prodType) {
        if (CommonUtil.convertObjToStr(prodType).length() > 1) {
            if (prodType.equals("GL")) {
                key = new ArrayList();
                value = new ArrayList();
            } else {
                try {
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
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
        cbmProdId = new ComboBoxModel(key, value);
        this.cbmProdId = cbmProdId;
        setChanged();
    }

    private void setOperationMap() throws Exception {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "PurchaseEntryJNDI");
        map.put(CommonConstants.HOME, "purcahseentry.PurchaseEntryHome");
        map.put(CommonConstants.REMOTE, "purcahseentry.PurchaseEntry");
    }

    public static PurchaseEntryOB getInstance() throws Exception {
        return objOB;
    }

    public int getActionType() {
        return _actionType;
    }

    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }

    public void setStatus() {
    }

    public void populateData(HashMap whereMap) {
        HashMap mapData = null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            List lst =(List) mapData.get("PurchaseEntryTO");
            PurchaseEntryTO objTO = (PurchaseEntryTO) ((List) mapData.get("PurchaseEntryTO")).get(0);
            mapData.put("purid",objTO.getPurId());
            setPurchaseEntryTO(objTO);
            if (mapData.containsKey("TRANSFER_TRANS_LIST") || mapData.containsKey("CASH_TRANS_LIST")) {
                List list = (List) mapData.get("TRANSFER_TRANS_LIST");
                setProxyReturnMap(mapData);
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            System.out.println("Error in populateData():" + e);
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
  
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }

    public int getResult() {
        return _result;
    }

    private void setPurchaseEntryTO(PurchaseEntryTO objTO) {
        setTxtPurAmount(objTO.getPurAmount());
        setTxtPurComm(objTO.getPurComm());
        setTxtPurchaseRet(objTO.getPurchaseRet());
        setTxtSundry(objTO.getSundry());
        setTxtInvestAcHead(objTO.getInvestAcHead());
        setBalnceCash(objTO.getBalAmt());
        setPurId(objTO.getPurId());
        setTradId(objTO.getTradeId());
        setCboTransType(objTO.getTransType());
        setCboTransMode(objTO.getTransMode());
        setCboProductId(objTO.getProdId());
        setTxtAccountNumber(objTO.getActnum());
        setTxtChequeNo(objTO.getChequeNo());
        setTxtNarration(objTO.getNarration());
        setTxtSupplier(objTO.getSupplier());
        setTxtCashAmount(objTO.getCashAmount());
        setTxtAmount(objTO.getAmount());
        setTxtPurchaseType(objTO.getPurchaseType());
        setTxtFromWeight(objTO.getFromWeight());
        setTxtToWeight(objTO.getToWeight());
        setTxtTradeNarration(objTO.getTradeNarration());
        setTdtFromDate(objTO.getFromDate());
        setLblTotal(objTO.getGrandTot());
        if (objTO.getIsInvestment().equals("Y")) {
            setRdoInvestment(true);
              setRdoSundry(false);
             
        } else  {
            setRdoInvestment(false);
            setRdoSundry(true);
          
        }

        notifyObservers();
    }

   
    public void resetForm() {
        setTxtPurAmount(0.00);
        setTxtPurComm(0.00);
        setTxtPurchaseRet(0.00);
        setTxtSundry(0.00);
        setTxtInvestAcHead(0.00);
        setBalnceCash(0.00);
        setAuthMap(null);
        setPurId("");
        setTxtAccountNumber("");
        setTxtCashAmount(0.0);
        setTxtChequeNo("");
        setTxtNarration("");
        setTxtSupplier("");
        setTxtAccountNumber("");
        notifyObservers();
        setCboTransMode(" ");
        setCboTransType("");
        setCboProductId(" ");
        setLblTotal(0.00);
        tblDatanew=new ArrayList();
    }
  

    public void execute(String command) {
        try {
            System.out.println("haiiiiiiiiiiiiiiiiiiiiiiiii");
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            term.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            term.put("tab_no", getTabNo());
            term.put("dataList", tblDatanew);
            term.put("totAmount", getLblTotal());
            term.put("tradeNarration", getTxtTradeNarration());
           
            if (!command.equals(CommonConstants.AUTHORIZESTATUS)) {
                term.put("PurchaseEntryTO", gePurchaseEntryTO(command));
            }
           
            if (getAuthMap() != null && getAuthMap().size() > 0) {
                //if (getAuthMap() != null) {
                    term.put(CommonConstants.AUTHORIZEMAP, getAuthMap());
                //}
            }
           
            HashMap proxyReturnMap = proxy.execute(term, map);
            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
           
            
        }
    }

    private PurchaseEntryTO gePurchaseEntryTO(String command) {
        PurchaseEntryTO objTO = new PurchaseEntryTO();
        objTO.setCommand(command);
        objTO.setPurAmount(getTxtPurAmount());
        objTO.setPurComm(getTxtPurComm());
        objTO.setPurchaseRet(getTxtPurchaseRet());
        objTO.setSundry(getTxtSundry());
        objTO.setInvestAcHead(getTxtInvestAcHead());
        objTO.setBalAmt(getBalnceCash());
        objTO.setPurId(getPurId());
        objTO.setTradeId(getTradId());
        objTO.setTransType(getCboTransType());
        objTO.setTransMode(getCboTransMode());
        objTO.setSupplier(getTxtSupplier());
        objTO.setPurchaseType(getTxtPurchaseType());
        if (isRdoInvestment()) {
            objTO.setIsInvestment("Y");
        } else {
            objTO.setIsInvestment("N");
        }
        if (isRdoSundry()) {
            objTO.setIsSundry("Y");
        } else {
            objTO.setIsSundry("N");
        }
        objTO.setCashAmount(getTxtCashAmount());
        objTO.setProdId(getCboProductId());
        objTO.setActnum(getTxtAccountNumber());
        objTO.setChequeNo(getTxtChequeNo());
        objTO.setNarration(getTxtNarration()+" "+getTxtTradeNarration());
        objTO.setSupActnum(getTxtSupActnum());
//        objTO.setNarration(getTxtTradeNarration());
        objTO.setAmount(getTxtAmount());
        objTO.setFromWeight(getTxtFromWeight());
        objTO.setToWeight(getTxtToWeight());
        objTO.setFromDate(getTdtFromDate());
        objTO.setGrandTot(getLblTotal());
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            objTO.setAuthorizeStatus("");
            objTO.setAuthorizeBy("");
            objTO.setAuthorizeDte(null);
            objTO.setStatus("CREATED");
        }

        return objTO;
    }

    
    public java.util.HashMap getAuthMap() {
        return authMap;
    }

    
    public void setAuthMap(java.util.HashMap authMap) {
        this.authMap = authMap;
    }

    public java.util.Date getCurrDt() {
        return currDt;
    }

   
    public void setCurrDt(java.util.Date currDt) {
        this.currDt = currDt;
    }
}
