/** Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * IndendLiabilityOB.java
 *
 * Created on September 12, 2011, 12:27 PM
 */
package com.see.truetransact.ui.indend.indendLiability;

import com.see.truetransact.ui.indend.suspenseIndend.*;
import com.see.truetransact.ui.indend.*;
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
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.indend.IndendTO;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.investments.InvestmentsTransTO;
import java.util.Date;
import com.see.truetransact.ui.common.transaction.TransactionOB;

/**
 *
 * @author user
 */
public class IndendLiabilityOB extends CObservable {

    private HashMap finalMap = new HashMap();
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblCheckBookTable;
    private TableModel tbmIndned;

    public TableModel getTbmIndned() {
        return tbmIndned;
    }

    public void setTbmIndned(TableModel tbmIndned) {
        this.tbmIndned = tbmIndned;
    }
    private String txtDepoId = "", txtStoreName = "", txtGenTrans = "", txtPurcBillNo = "", txtTinNo = "",
            cboTransType = "", cboSupplier = "", txtIRNo;
    Date tdtDateIndand = null, tdtSalesDate = null, tdtBillDate = null;
    private String debitOrCredit = "";
    Double txtPurAmount, txtAmount, txtVatAmt, txtOthrExpAmt, txtMiscAmt, txtCommRecvdAmt, txtVatIndAmt;
    private static SqlMap sqlMap = null;
    private TransactionOB transactionOB;
    Date currDt = null;
    private ComboBoxModel cbmDepoId;//Model for ui combobox Agency
    private ComboBoxModel cbmTransType;//Model for ui combobox Type
    private ComboBoxModel cbmSupplierName;
    private final static Logger log = Logger.getLogger(IndendLiabilityOB.class);//Creating Instace of Log
    private ProxyFactory proxy;
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private static IndendLiabilityOB objIndendOB;//Singleton Object Reference
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private ArrayList key, value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private HashMap authMap = new HashMap();
    private int txtslNo = 0;
    private HashMap mapData = null;
    private String txtStatusBy="";//jiby
    private ComboBoxModel cbmIndentHeads;
    private String cboIndentHeads="";

    public String getTxtStatusBy() {
        return txtStatusBy;
    }

    public void setTxtStatusBy(String txtStatusBy) {
        this.txtStatusBy = txtStatusBy;
    }

    /**
     * Creates a new instance of NewBorrowingOB
     */
    public IndendLiabilityOB() {
        try {
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initUIComboBoxModel();
            fillDropdown();
            setTableTile();
            tblCheckBookTable = new EnhancedTableModel(null, tableTitle);
            setChanged();
            notifyObservers();
        } catch (Exception e) {
            //parseException.logException(e,true);
            System.out.println("Error in NewBorrowingOB():" + e);
        }
    }

    static {
        try {
            log.info("Creating ParameterOB...");
            objIndendOB = new IndendLiabilityOB();
        } catch (Exception e) {
            // parseException.logException(e,true);
            System.out.println("Error in static():" + e);
        }
    }

    private void setTableTile() throws Exception {
        tableTitle.add("Sl No");
        tableTitle.add("Trans Type");
        tableTitle.add("Name");
        tableTitle.add("Indend Amount");        
        tableTitle.add("Indend Vat");

        IncVal = new ArrayList();
    }

    private void initUIComboBoxModel() {
        cbmDepoId = new ComboBoxModel();
        cbmTransType = new ComboBoxModel();
        cbmIndentHeads = new ComboBoxModel();
    }
    // Sets the HashMap required to set JNDI,Home and Remote

    private void setOperationMap() throws Exception {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "IndendJNDI");
        map.put(CommonConstants.HOME, "indend.IndendHome");
        map.put(CommonConstants.REMOTE, "indend.Indend");
    }

    public com.see.truetransact.clientutil.ComboBoxModel getCbmDepoId() {
        return cbmDepoId;
    }

    public com.see.truetransact.clientutil.ComboBoxModel getCbmTransType() {
        return cbmTransType;
    }

    public static IndendLiabilityOB getInstance() throws Exception {
        return objIndendOB;
    }

    public String getCboIndentHeads() {
        return cboIndentHeads;
    }

    public void setCboIndentHeads(String cboIndentHeads) {
        this.cboIndentHeads = cboIndentHeads;
    }
    
    

    public int getActionType() {
        return _actionType;
    }

    public void setActionType(int actionType) {
        _actionType = actionType;
        //  System.out.println("setActionType IN"+actionType);
        setStatus();
        setChanged();
    }

    public void setStatus() {
        // this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }

    /**
     * Setter for property cbmTokenType.
     *
     * @param cbmTokenType New value of property cbmTokenType.
     */
    public void setCbmDepoId(com.see.truetransact.clientutil.ComboBoxModel cbmBrAgency) {
        this.cbmDepoId = cbmDepoId;
    }

    public void setCbmTransType(com.see.truetransact.clientutil.ComboBoxModel cbmBrType) {
        this.cbmTransType = cbmTransType;
    }

    public ComboBoxModel getCbmIndentHeads() {
        return cbmIndentHeads;
    }

    public void setCbmIndentHeads(ComboBoxModel cbmIndentHeads) {
        this.cbmIndentHeads = cbmIndentHeads;
    }
    
    

    /**
     * Populates two ArrayList key,value
     */
    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public void populateData(HashMap whereMap) {
//        HashMap mapData = null;
        mapData = new HashMap();
        try {
            System.out.println("whereMap==" + whereMap);
            System.out.println("map==" + map);
            mapData = proxy.executeQuery(whereMap, map);
            System.out.println("mapData###>??>>>>" + mapData);
            System.out.println("objmapData==" + ((List) (mapData.get("IndendTO"))).get(0));
            IndendTO objTO = (IndendTO) ((List) (mapData.get("IndendTO"))).get(0);
            System.out.println("ghjy");
            setTableData(mapData);
            System.out.println("mkonib");
            System.out.println("objTOobjTOobjTOobjTO==" + objTO);
            setIndendTO(objTO);
            System.out.println("mapData=UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUu=" + mapData);
            if (mapData.containsKey("TRANSACTION_LIST")) {
                List list = (List) mapData.get("TRANSACTION_LIST");
                System.out.println("mapData=list999999999999999999999999999999999999999999999999=" + list);
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            // parseException.logException(e,true);
            System.out.println("Error in populateData():" + e);

        }
    }

    public void showDatanew(ArrayList aList) {
        IndendTO objTo = (IndendTO) aList.get(0);
        System.out.println("objTo in OB%%%%"+objTo);
        setIndendTO(objTo);
    }

    public void setTableData(HashMap mapData) {
        try{
        List tblData = ((List) (mapData.get("IndendTO")));
        System.out.println("tblData#######"+tblData);
        finalMap = new HashMap();
        ArrayList tblList = new ArrayList();
        for (int i = 0; i < tblData.size(); i++) {
            IndendTO objTo = (IndendTO) tblData.get(i);
            if(objTo.getSlNo() == i+1){
                System.out.println("34545%%%%"+objTo);
            finalMap.put(String.valueOf(i+1),objTo);
            }
            ArrayList sList = new ArrayList();
            
            if (objTo.getTransType() != null) {
                sList.add(objTo.getTransType());
            }            
            if (objTo.getSupplier() != null) {
                sList.add(CommonUtil.convertObjToStr(getCbmSupplierName().getDataForKey(objTo.getSupplier())));
            }else{
                sList.add(CommonUtil.convertObjToStr(objTo.getStoreName()));
            }    
            if (objTo.getPurAmount() != null) {
                sList.add(CommonUtil.convertObjToStr(objTo.getPurAmount()));
            }
            if (objTo.getVatIndAmt() != null) {
                sList.add(CommonUtil.convertObjToStr(objTo.getVatIndAmt()));
            }
            if (objTo.getSlNo() >0) {
                sList.add(objTo.getSlNo());
            }
            tblList.add(sList);
        }
        insertTable(tblList);
        }catch(Exception e){
            e.printStackTrace();
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

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }

    public int getResult() {
        return _result;
    }

     public void showData(String acno) {
        List tblData = ((List) (mapData.get("IndendTO")));
        ArrayList tblList = new ArrayList();
        for (int i = 0; i < tblData.size(); i++) {
            IndendTO objTo = (IndendTO) tblData.get(i);
            if (acno.equals(objTo.getSlNo())) {
                setIndendTO(objTo);
            }
        }
    }
    
    private void setIndendTO(IndendTO objTO) {
        //   setBorrowingNo(objTO.getBorrowingNo());
        if (getActionType() != ClientConstants.ACTIONTYPE_NEW && objTO.getStrIRNo()!=null) {
            setIRNo(objTO.getStrIRNo());
        }
        // setCboDepoId(CommonUtil.convertObjToStr(getCbmDepoId().getDataForKey(objTO.getDepoId())));
        System.out.println("objTO.getVatIndAmt()>>>" + objTO.getVatIndAmt());
        setTxtVatIndAmt(objTO.getVatIndAmt());
        setTxtslNo(objTO.getSlNo());
        setTxtDepoId(objTO.getDepoId());
        setCboTransType(CommonUtil.convertObjToStr(getCbmTransType().getDataForKey(objTO.getTransType())));
        System.out.println("SSS555555555555555555555===" + objTO.getDateIndand());
        setTdtDateIndand(objTO.getDateIndand());
        setTdtBillDate(objTO.getBillDate());
        setTxtStoreName(objTO.getStoreName());
        setTxtPurAmount(objTO.getPurAmount());
        setTxtGenTrans(objTO.getGenTrans());
        setTxtAmount(objTO.getAmount());
        setTxtVatAmt(objTO.getVatAmt());
        System.out.println("getTxtVatAmt>>>" +getTxtVatAmt());
        System.out.println("getTxtMiscAmt>>>" +getTxtMiscAmt());
        setTxtPurcBillNo(objTO.getPurchBillNo());
        setTxtTinNo(objTO.getTinNo());
        setTdtSalesDate(objTO.getSalesDate());
        setTxtMiscAmt(objTO.getMiscAmt());
        setTxtCommRecvdAmt(objTO.getCommRecvdAmt());
        setTxtOthrExpAmt(objTO.getOthrExpAmt());
        System.out.println("objTO.getSupplier()>>>" + objTO.getCommRecvdAmt() + " bdate " + objTO.getBillDate());
        System.out.println("CommonUtil.convertObjToStr(getCbmSupplierName().getDataForKey(objTO.getSupplier()))>>>" + CommonUtil.convertObjToStr(getCbmSupplierName().getDataForKey(objTO.getSupplier())));
        setCboSupplier(CommonUtil.convertObjToStr(getCbmSupplierName().getDataForKey(objTO.getSupplier())));
        setTxtNarration(objTO.getNarration());
        setTxtstIndentNo(objTO.getStIndentNo());
        setCboIndentHeads(CommonUtil.convertObjToStr(getCbmIndentHeads().getDataForKey(objTO.getCboIndentHeads())));
        setChanged();
        notifyObservers();


    }

    public void insertTable(ArrayList tableList) {
        try {
            ArrayList aList = new ArrayList();
            if (tableList != null) {
                for (int i = 0; i < tableList.size(); i++) {
                    ArrayList eachList = new ArrayList();
                    ArrayList eachListadd = new ArrayList();
                    eachList = (ArrayList) tableList.get(i);
                    System.out.println("eachList" + eachList);
                    if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    eachListadd.add(i + 1);
                    setTxtslNo(i + 1);
                    }else{
                    eachListadd.add(CommonUtil.convertObjToInt(eachList.get(4)));    
                    }
                    
                    eachListadd.add((String) eachList.get(0));
                    eachListadd.add((String) eachList.get(1));
                    eachListadd.add((String) eachList.get(2));
                   // if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                        eachListadd.add((String) eachList.get(3));
                    //} else {
                    //    System.out.println("hbhgfbg");
                   //     eachListadd.add("");
                    //}
                    aList.add(eachListadd);
                }
            }
            System.out.println("aList" + aList);
            //tbmIndned.setData(aList);
            //tbmIndned.fireTableDataChanged();
            tblCheckBookTable = new EnhancedTableModel((ArrayList) aList, tableTitle);
            notifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets all the UI Fields
     */
    public void resetForm() {
        setTxtslNo(0);
        setTxtDepoId("");
        setCboTransType("");
        setTxtGenTrans("");
        setTdtDateIndand(null);
        setTxtStoreName("");
        setTxtPurAmount(null);
        setTxtAmount(null);
        setAuthMap(null);
        setTxtVatAmt(null);
        setTxtPurcBillNo("");
        setTxtTinNo("");
        setTdtSalesDate(null);
        setTxtCommRecvdAmt(null);
        setTxtOthrExpAmt(null);
        setTxtMiscAmt(null);
        setTxtVatIndAmt(null);
        setTxtNarration(null);
        setTxtstIndentNo(null);
        setCboSupplier("");
        setCboIndentHeads("");
        notifyObservers();
        setFinalMap(null);
    }
    
    public void destroyObjects() {
         allowedTransactionDetailsTO = null;
        transactionDetailsTO = null;
        deletedTransactionDetailsTO = null;
    }

    public void setIRNo(java.lang.String txtIRNo) {
        this.txtIRNo = txtIRNo;
        setChanged();
    }

    public String getIRNo() {
        return txtIRNo;

    }

    /* Executes Query using the TO object */
    public void execute(String command) {
        /*try {
         System.out.println("GET BOPRRR NO IN EDIT :="+geIndendTO(command));
           
         HashMap term = new HashMap();
         term.put(CommonConstants.MODULE, getModule());
         term.put(CommonConstants.SCREEN, getScreen());
         term.put(CommonConstants.USER_ID, "0001");
         term.put(CommonConstants.BRANCH_ID, "0001");
         term.put("IndendTO", geIndendTO(command));
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
         }*/
        try {
            HashMap term = new HashMap();
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
              command = CommonConstants.TOSTATUS_UPDATE;  
            }
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            term.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            if (!command.equals(CommonConstants.AUTHORIZESTATUS)) {
                term.put("IndendTO", geIndendTO(command));
                if (transactionDetailsTO == null) {
                    transactionDetailsTO = new LinkedHashMap();
                }
                if (deletedTransactionDetailsTO != null) {
                    transactionDetailsTO.put(DELETED_TRANS_TOs, deletedTransactionDetailsTO);
                    deletedTransactionDetailsTO = null;
                }
                transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                allowedTransactionDetailsTO = null;
                term.put("TransactionTO", transactionDetailsTO);
                if (getFinalMap() != null) {
                    System.out.println("getFinalMap() in indendob >>>>>" + getFinalMap());
                    term.put("finalMap", getFinalMap());
                }
                if (getDebitOrCredit() != null) {
                    term.put("DEBITORCREDIT", getDebitOrCredit());
                }
            }
            if (getAuthMap() != null && getAuthMap().size() > 0) {
                if (getAuthMap() != null) {
                    term.put(CommonConstants.AUTHORIZEMAP, getAuthMap());
                }
                if (allowedTransactionDetailsTO != null && allowedTransactionDetailsTO.size() > 0) {
                    if (transactionDetailsTO == null) {
                        transactionDetailsTO = new LinkedHashMap();
                    }
                    transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                    term.put("TransactionTO", transactionDetailsTO);
                    allowedTransactionDetailsTO = null;
                }
//                    authMap = null;
            }
            term.put("INDEND_LIABILITY", "INDEND_LIABILITY");
            System.out.println("Map befor Dao %$%#%#%#" + term);
            HashMap proxyReturnMap = proxy.execute(term, map);
            System.out.println("proxyReturnMap>>>>" + proxyReturnMap);
            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);  
            e.printStackTrace();
            ClientUtil.showMessageWindow(e.getMessage());
            System.out.println("Error in execute():" + e);
        }
    }

    public IndendTO geIndendTO(String command) {
        IndendTO objTO = new IndendTO();
        objTO.setCommand(command);
        objTO.setStrIRNo(getIRNo());
        objTO.setSlNo(getTxtslNo());
        objTO.setVatIndAmt(getTxtVatIndAmt());
        objTO.setDateIndand(getTdtDateIndand());
        objTO.setDepoId(getTxtDepoId());
        objTO.setTransType(getCboTransType());
        System.out.println("getCboSupplier>>>>" + getCboSupplier());
        objTO.setSupplier(getCboSupplier());
        objTO.setStoreName(getTxtStoreName());
        objTO.setPurAmount(getTxtPurAmount());
        objTO.setAmount(getTxtAmount());
        objTO.setGenTrans(getTxtGenTrans());
        objTO.setVatAmt(getTxtVatAmt());
        objTO.setPurchBillNo(getTxtPurcBillNo());
        objTO.setTinNo(getTxtTinNo());
        objTO.setSalesDate(getTdtSalesDate());
        //objTO.setCommRecvdAmt(txtVatAmt);
        objTO.setCommRecvdAmt(getTxtCommRecvdAmt());
        objTO.setMiscAmt(getTxtMiscAmt());
        objTO.setOthrExpAmt(getTxtOthrExpAmt());
        objTO.setBillDate(getTdtBillDate());
        objTO.setNarration(getTxtNarration());
        objTO.setStIndentNo(getTxtstIndentNo());
        objTO.setSalesmanName(txtSalesmanName);
        objTO.setStatusBy(getTxtStatusBy());//jiby
        objTO.setSuspenseAcHd(getSuspenseAcHd(CommonUtil.convertObjToStr(objTO.getSupplier())));
        objTO.setCboIndentHeads(getCboIndentHeads());
       if(command.equals(CommonConstants.TOSTATUS_INSERT) || command.equals(CommonConstants.TOSTATUS_UPDATE)){
                objTO.setAuthorizeStatus("");
                objTO.setAuthorizeBy("");
                objTO.setAuthorizeDte(null);
                objTO.setStatus("CREATED");
        }
       objTO.setOtherIncome(new Double(0));
        System.out.println("objTO###@@@11>>>>" + objTO);
        return objTO;
    }
    
    public String getSuspenseAcHd(String supplierId) {
        HashMap supMap = new HashMap();
        String suspenseAcHd = "";
        supMap.put("SUPPLY_ID", supplierId);
        List susList = (ClientUtil.executeQuery("getIndendSupplierSuspenseAcHd", supMap));
        if(susList!=null && susList.size()>0){
            supMap = (HashMap) susList.get(0);
            if (supMap != null && supMap.size() > 0) {
                suspenseAcHd = CommonUtil.convertObjToStr(supMap.get("SUSPENSE_ACHD"));
            } 
        }
        return suspenseAcHd;
    }

    /**
     * Getter for property authMap.
     *
     * @return Value of property authMap.
     */
    public java.util.HashMap getAuthMap() {
        return authMap;
    }

    /**
     * Setter for property authMap.
     *
     * @param authMap New value of property authMap.
     */
    public void setAuthMap(java.util.HashMap authMap) {
        this.authMap = authMap;
    }
    /* Filling up the the ComboBox in the UI*/

    private void fillDropdown() throws Exception {
        try {
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
            final ArrayList lookup_keys1 = new ArrayList();
            lookup_keys1.add("INDEND_LIABILITY_TRANSTYPE");
            lookup_keys1.add("INDENT.HEADS");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys1);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap) keyValue.get("INDEND_LIABILITY_TRANSTYPE"));
            cbmTransType = new ComboBoxModel(key, value);
            getKeyValue((HashMap) keyValue.get("INDENT.HEADS"));
            cbmIndentHeads= new ComboBoxModel(key, value);
            //This part is fill the principal and interest repayment dropdowns
            /**
             * ****************************
             */
            HashMap param = new HashMap();
            param.put(CommonConstants.MAP_NAME, "getAllSupplierName");
            HashMap where = new HashMap();
            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            param.put(CommonConstants.PARAMFORQUERY, where);
            // where = null;
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap) keyValue.get(CommonConstants.DATA));
//            cbmSupplierName = new ComboBoxModel(key,value);

//            key=new ArrayList();
//             value=new ArrayList();
//           HashMap where1=new HashMap();
//            List aList=ClientUtil.executeQuery("getAllSupplierName1", where1);
//            for(int i=0;i<aList.size();i++)
//            {
//                where1=(HashMap)aList.get(i);
//            key.add(where1.get("KEY"));
//            value.add(where1.get("NAME"));
//            
//                
//            }
            cbmSupplierName = new ComboBoxModel(key, value);


        } catch (NullPointerException e) {
            // parseException.logException(e,true);
            System.out.println("Error in fillDropdown():" + e);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
    }
    private String txtNarration = "";
    private String txtstIndentNo = "";
    private String txtSalesmanName = "";

    public String getTxtSalesmanName() {
        return txtSalesmanName;
    }

    public void setTxtSalesmanName(String txtSalesmanName) {
        this.txtSalesmanName = txtSalesmanName;
    }

    public String getTxtstIndentNo() {
        return txtstIndentNo;
    }

    public void setTxtstIndentNo(String txtstIndentNo) {
        this.txtstIndentNo = txtstIndentNo;
    }

    public String getTxtNarration() {
        return txtNarration;
    }

    public void setTxtNarration(String txtNarration) {
        this.txtNarration = txtNarration;
    }

    /**
     * Getter for property tdtDateIndand.
     *
     * @return Value of property tdtDateIndand.
     */
    public java.util.Date getTdtDateIndand() {
        return tdtDateIndand;
    }

    /**
     * Setter for property tdtDateIndand.
     *
     * @param tdtDateIndand New value of property tdtDateIndand.
     */
    public void setTdtDateIndand(java.util.Date tdtDateIndand) {
        this.tdtDateIndand = tdtDateIndand;
    }

    /**
     * Getter for property txtPurAmount.
     *
     * @return Value of property txtPurAmount.
     */
    public java.lang.Double getTxtPurAmount() {
        return txtPurAmount;
    }

    /**
     * Setter for property txtPurAmount.
     *
     * @param txtPurAmount New value of property txtPurAmount.
     */
    public void setTxtPurAmount(java.lang.Double txtPurAmount) {
        this.txtPurAmount = txtPurAmount;
    }

    /**
     * Getter for property txtAmount.
     *
     * @return Value of property txtAmount.
     */
    public java.lang.Double getTxtAmount() {
        return txtAmount;
    }

    /**
     * Setter for property txtAmount.
     *
     * @param txtAmount New value of property txtAmount.
     */
    public void setTxtAmount(java.lang.Double txtAmount) {
        this.txtAmount = txtAmount;
    }

    /**
     * Getter for property txtStoreName.
     *
     * @return Value of property txtStoreName.
     */
    public java.lang.String getTxtStoreName() {
        return txtStoreName;
    }

    /**
     * Setter for property txtStoreName.
     *
     * @param txtStoreName New value of property txtStoreName.
     */
    public void setTxtStoreName(java.lang.String txtStoreName) {
        this.txtStoreName = txtStoreName;
    }

    /**
     * Getter for property cboTransType.
     *
     * @return Value of property cboTransType.
     */
    public java.lang.String getCboTransType() {
        return cboTransType;
    }

    /**
     * Setter for property cboTransType.
     *
     * @param cboTransType New value of property cboTransType.
     */
    public void setCboTransType(java.lang.String cboTransType) {
        this.cboTransType = cboTransType;
    }

    public com.see.truetransact.ui.common.transaction.TransactionOB getTransactionOB() {
        return transactionOB;
    }

    public java.util.Date getCurrDt() {
        return currDt;
    }

    /**
     * Setter for property currDt.
     *
     * @param currDt New value of property currDt.
     */
    public void setCurrDt(java.util.Date currDt) {
        this.currDt = currDt;
    }

    /**
     * Setter for property transactionOB.
     *
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(com.see.truetransact.ui.common.transaction.TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }

    public java.util.LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }

    /**
     * Setter for property allowedTransactionDetailsTO.
     *
     * @param allowedTransactionDetailsTO New value of property
     * allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    /**
     * Getter for property txtGenTrans.
     *
     * @return Value of property txtGenTrans.
     */
    public java.lang.String getTxtGenTrans() {
        return txtGenTrans;
    }

    /**
     * Setter for property txtGenTrans.
     *
     * @param txtGenTrans New value of property txtGenTrans.
     */
    public void setTxtGenTrans(java.lang.String txtGenTrans) {
        this.txtGenTrans = txtGenTrans;
    }

    /**
     * Getter for property txtDepoId.
     *
     * @return Value of property txtDepoId.
     */
    public java.lang.String getTxtDepoId() {
        return txtDepoId;
    }

    /**
     * Setter for property txtDepoId.
     *
     * @param txtDepoId New value of property txtDepoId.
     */
    public void setTxtDepoId(java.lang.String txtDepoId) {
        this.txtDepoId = txtDepoId;
    }

    /**
     * Getter for property txtVatAmt.
     *
     * @return Value of property txtVatAmt.
     */
    public java.lang.Double getTxtVatAmt() {
        return txtVatAmt;
    }

    /**
     * Setter for property txtVatAmt.
     *
     * @param txtVatAmt New value of property txtVatAmt.
     */
    public void setTxtVatAmt(java.lang.Double txtVatAmt) {
        this.txtVatAmt = txtVatAmt;
    }

    /**
     * Getter for property txtPurcBillNo.
     *
     * @return Value of property txtPurcBillNo.
     */
    public String getTxtPurcBillNo() {
        return txtPurcBillNo;
    }

    /**
     * Setter for property txtPurcBillNo.
     *
     * @param txtPurcBillNo New value of property txtPurcBillNo.
     */
    public void setTxtPurcBillNo(String txtPurcBillNo) {
        this.txtPurcBillNo = txtPurcBillNo;
    }

    /**
     * Getter for property txtTinNo.
     *
     * @return Value of property txtTinNo.
     */
    public String getTxtTinNo() {
        return txtTinNo;
    }

    /**
     * Setter for property txtTinNo.
     *
     * @param txtTinNo New value of property txtTinNo.
     */
    public void setTxtTinNo(String txtTinNo) {
        this.txtTinNo = txtTinNo;
    }

    /**
     * Getter for property tdtSalesDate.
     *
     * @return Value of property tdtSalesDate.
     */
    public Date getTdtSalesDate() {
        return tdtSalesDate;
    }

    /**
     * Setter for property tdtSalesDate.
     *
     * @param tdtSalesDate New value of property tdtSalesDate.
     */
    public void setTdtSalesDate(Date tdtSalesDate) {
        this.tdtSalesDate = tdtSalesDate;
    }

    public ComboBoxModel getCbmSupplierName() {
        return cbmSupplierName;
    }

    public void setCbmSupplierName(ComboBoxModel cbmSupplierName) {
        this.cbmSupplierName = cbmSupplierName;
    }

    public Double getTxtOthrExpAmt() {
        return txtOthrExpAmt;
    }

    public void setTxtOthrExpAmt(Double txtOthrExpAmt) {
        this.txtOthrExpAmt = txtOthrExpAmt;
    }

    public Double getTxtMiscAmt() {
        return txtMiscAmt;
    }

    public void setTxtMiscAmt(Double txtMiscAmt) {
        this.txtMiscAmt = txtMiscAmt;
    }

    public Double getTxtCommRecvdAmt() {
        return txtCommRecvdAmt;
    }

    public void setTxtCommRecvdAmt(Double txtCommRecvdAmt) {
        this.txtCommRecvdAmt = txtCommRecvdAmt;
    }

    public Double getTxtVatIndAmt() {
        return txtVatIndAmt;
    }

    public void setTxtVatIndAmt(Double txtVatIndAmt) {
        this.txtVatIndAmt = txtVatIndAmt;
    }

    public String getCboSupplier() {
        return cboSupplier;
    }

    public void setCboSupplier(String cboSupplier) {
        this.cboSupplier = cboSupplier;
    }

    public EnhancedTableModel getTblCheckBookTable() {
        return tblCheckBookTable;
    }

    public void setTblCheckBookTable(EnhancedTableModel tblCheckBookTable) {
        this.tblCheckBookTable = tblCheckBookTable;
    }

    public HashMap getFinalMap() {
        return finalMap;
    }

    public void setFinalMap(HashMap finalMap) {
        this.finalMap = finalMap;
    }

    public String getDebitOrCredit() {
        return debitOrCredit;
    }

    public void setDebitOrCredit(String debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }

    public Date getTdtBillDate() {
        return tdtBillDate;
    }

    public void setTdtBillDate(Date tdtBillDate) {
        this.tdtBillDate = tdtBillDate;
    }

    public int getTxtslNo() {
        return txtslNo;
    }

    public void setTxtslNo(int txtslNo) {
        this.txtslNo = txtslNo;
    }


}
