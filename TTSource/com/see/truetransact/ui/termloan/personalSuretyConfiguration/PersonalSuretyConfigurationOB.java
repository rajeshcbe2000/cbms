/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmpTransferOB.java
 *
 * Created on june , 2010, 4:30 PM Swaroop
 */

package com.see.truetransact.ui.termloan.personalSuretyConfiguration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Date;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.termloan.personalSuretyConfiguration.PersonalSuretyConfigurationTO;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.TTException;
import org.apache.log4j.Logger;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.CommonRB;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.ibatis.db.sqlmap.SqlMap;

/**
 *
 * @author  
 *
 */
public class PersonalSuretyConfigurationOB extends CObservable {

    private String txtMaxSurety = "";
    private String txtMaxLoanSurety = "";
    private String txtCloseBefore = "";
    private String txtPersonalID = "";
    private String cboProdType = "";
    private ComboBoxModel cbmProdType;
    private static SqlMap sqlMap = null;
    private final static Logger log = Logger.getLogger(PersonalSuretyConfigurationOB.class);
    private ProxyFactory proxy;
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private static PersonalSuretyConfigurationOB objPersonalOB;
    private HashMap map;
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private ArrayList key, value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    HashMap lookupMap = null;
    private String txtMaximumLoanAmount = "";
    private int pan = 0;
    private List selectedList = new ArrayList();
    String tdtEffectiveDate = "";
    private String txtMaxNoOfLoans = ""; // Added by nithya on 21-07-2016 for 4922
    private PersonalSuretyConfigurationOB generalBodyDetailsOB;
    PersonalSuretyConfigurationRB objPersonalRB = new PersonalSuretyConfigurationRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private PersonalSuretyConfigurationTO objPersonalTO;
    private String cboShareType = "";
    private ComboBoxModel cbmShareType;
    private String txtMaxSuretyAmt = "";

    // Added by nithya on 21-07-2016 for 4922
    public String getTxtMaxNoOfLoans() {
        return txtMaxNoOfLoans;
    }

    public void setTxtMaxNoOfLoans(String txtMaxNoOfLoans) {
        this.txtMaxNoOfLoans = txtMaxNoOfLoans;
    }
    // End
    
    public String getCboShareType() {
        return cboShareType;
    }

    public void setCboShareType(String cboShareType) {
        this.cboShareType = cboShareType;
    }    

    public ComboBoxModel getCbmShareType() {
        return cbmShareType;
    }

    public void setCbmShareType(ComboBoxModel cbmShareType) {
        this.cbmShareType = cbmShareType;
    }
   
    /** Creates a new instance of TDS MiantenanceOB */
    public PersonalSuretyConfigurationOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "PersonalSuretyConfigurationJNDI");
            map.put(CommonConstants.HOME, "personalSuretyConfiguration.PersonalSuretyConfigurationHome");
            map.put(CommonConstants.REMOTE, "personalSuretyConfiguration.PersonalSuretyConfiguration");
            initUIComboBoxModel();
            fillDropdown();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void initUIComboBoxModel() {
        cbmProdType = new ComboBoxModel();
    }
     
     private void fillDropdown() throws Exception {
        log.info("in fill dropdown");
        key = new ArrayList();
        value = new ArrayList();
        HashMap m = new HashMap();
        List comboValues = ClientUtil.executeQuery("getComboValues", m);
        if (!comboValues.isEmpty()) {
            key.add("");
            value.add("");
            for (int k = 0; k < comboValues.size(); k++) {
                HashMap keys = new HashMap();
                keys = (HashMap) comboValues.get(k);
                key.add(keys.get("AUTHORIZE_REMARK"));
                value.add(keys.get("AUTHORIZE_REMARK"));
            }
        }
        cbmProdType = new ComboBoxModel(key, value);
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, null);
        key = new ArrayList();
        value = new ArrayList();
        lookUpHash.put(CommonConstants.MAP_NAME, "getShareType");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
        cbmShareType = new ComboBoxModel(key, value);
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    private HashMap populateDataLocal(HashMap obj) throws Exception {
        HashMap keyValue = proxy.executeQuery(obj, lookupMap);
        return keyValue;
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    static {
        try {
            log.info("Creating ParameterOB...");
            objPersonalOB = new PersonalSuretyConfigurationOB();
        } catch (Exception e) {
            // parseException.logException(e,true);
            System.out.println("Error in static():" + e);
        }
    }
        
     public void execute(String command) {
        try {      
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            term.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            term.put("PersonalSuretyConfigurationTO", getPersonalTO(command));
            HashMap proxyReturnMap = proxy.execute(term, map);
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //parseException.logException(e,true);
              System.out.println("Error in execute():"+e);
        }
    } 
     private PersonalSuretyConfigurationTO getPersonalTO(String command){
        PersonalSuretyConfigurationTO objTO = new PersonalSuretyConfigurationTO();
        objTO.setCommand(command);
        if (getPan() == 1) {
            objTO.setGbid(getTxtPersonalID());
            objTO.setMaxSurety(getTxtMaxSurety());
            objTO.setCloseBefore(getTxtCloseBefore());
            objTO.setMaxLoanSurety(getTxtMaxLoanSurety());
            objTO.setMaxSuretyAmt(CommonUtil.convertObjToDouble(getTxtMaxSuretyAmt()));
            objTO.setPan(1);
        }
        if (getPan() == 2) {
            objTO.setGbid(getTxtPersonalID());
            objTO.setProdType(getCboProdType());
            objTO.setMaximumLoanAmount(Double.parseDouble(getTxtMaximumLoanAmount()));
            objTO.setSelectedList(getSelectedList());
            objTO.setPan(2);
            objTO.setEffectiveDate((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtEffectiveDate()))));
            if (getTxtMaxNoOfLoans() != null && !getTxtMaxNoOfLoans().equalsIgnoreCase("")) {
                objTO.setMaxNoOfLoans(Integer.parseInt(getTxtMaxNoOfLoans())); // Added by nithya on 21-07-2016 for 4922
            }
        }
        if (getCboShareType() != null) {
            objTO.setShareType(getCboShareType());
        }

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            objTO.setStatus("CREATED");
        }

        return objTO;
    }
    
     public void setPersonalTO(PersonalSuretyConfigurationTO objTO) {
         if (getPan() == 1) {
             setTxtPersonalID(objTO.getGbid());
             setTxtMaxSurety(objTO.getMaxSurety());
             setTxtCloseBefore(objTO.getCloseBefore());
             setTxtMaxLoanSurety(objTO.getMaxLoanSurety());
             setTxtMaxSuretyAmt(CommonUtil.convertObjToStr(objTO.getMaxSuretyAmt()));
         }
         if (getPan() == 2) {
             setTxtPersonalID(objTO.getGbid());
             setTxtMaximumLoanAmount("" + objTO.getMaximumLoanAmount());
             setTxtMaxNoOfLoans("" + objTO.getMaxNoOfLoans()); // Added by nithya on 21-07-2016 for 4922
             setCboProdType(objTO.getProdType());
             setSelectedList(objTO.getSelectedList());
             setTdtEffectiveDate(CommonUtil.convertObjToStr(objTO.getEffectiveDate()));
             if (objTO.getShareType() != null && !objTO.getShareType().equals("")) {
                 setCboShareType(CommonUtil.convertObjToStr(getCbmShareType().getDataForKey(objTO.getShareType())));
             }
         }
         notifyObservers();
    }
    
    public void resetForm() {
        setTxtCloseBefore("");
        setTxtMaxSurety("");
        setTxtMaxLoanSurety("");
        setCboProdType("");
        setPan(0);
        setTdtEffectiveDate("");
        setTxtMaximumLoanAmount("");
        setCboShareType("");
        setTxtMaxNoOfLoans("");  // Added by nithya on 21-07-2016 for 4922
        setTxtMaxLoanSurety("");
        setTxtMaxSuretyAmt("");
    }

    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }

    public static PersonalSuretyConfigurationOB getInstance() throws Exception {
        return objPersonalOB;
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
        // this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
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
    public int getResult() {
        return _result;
    }

    public void populateData(HashMap whereMap) {
        HashMap mapData = null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            PersonalSuretyConfigurationTO objTO = (PersonalSuretyConfigurationTO) ((List) mapData.get("PersonalSuretyConfigurationTO")).get(0);
            if (mapData.containsKey("selectedList")) {
                objTO.setSelectedList((List) mapData.get("selectedList"));
            }
            setPersonalTO(objTO);
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            System.out.println("Error in populateData():" + e);
        }
    }

    /**
     * Getter for property txtMaxSurety.
     *
     * @return Value of property txtMaxSurety.
     */
    public String getTxtMaxSurety() {
        return txtMaxSurety;
    }

    /**
     * Setter for property txtMaxSurety.
     *
     * @param txtMaxSurety New value of property txtMaxSurety.
     */
    public void setTxtMaxSurety(String txtMaxSurety) {
        this.txtMaxSurety = txtMaxSurety;
    }

    /**
     * Getter for property txtCloseBefore.
     *
     * @return Value of property txtCloseBefore.
     */
    public String getTxtCloseBefore() {
        return txtCloseBefore;
    }

    /**
     * Setter for property txtCloseBefore.
     *
     * @param txtCloseBefore New value of property txtCloseBefore.
     */
    public void setTxtCloseBefore(String txtCloseBefore) {
        this.txtCloseBefore = txtCloseBefore;
    }

    /**
     * Getter for property txtPersonalID.
     *
     * @return Value of property txtPersonalID.
     */
    public String getTxtPersonalID() {
        return txtPersonalID;
    }

    /**
     * Setter for property txtPersonalID.
     *
     * @param txtPersonalID New value of property txtPersonalID.
     */
    public void setTxtPersonalID(String txtPersonalID) {
        this.txtPersonalID = txtPersonalID;
    }

    /**
     * Getter for property cboProdType.
     *
     * @return Value of property cboProdType.
     */
    public String getCboProdType() {
        return cboProdType;
    }

    /**
     * Setter for property cboProdType.
     *
     * @param cboProdType New value of property cboProdType.
     */
    public void setCboProdType(String cboProdType) {
        this.cboProdType = cboProdType;
    }

    /**
     * Getter for property cbmProdType.
     *
     * @return Value of property cbmProdType.
     */
    public ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }

    /**
     * Setter for property cbmProdType.
     *
     * @param cbmProdType New value of property cbmProdType.
     */
    public void setCbmProdType(ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }

    /**
     * Getter for property txtMaximumLoanAmount.
     *
     * @return Value of property txtMaximumLoanAmount.
     */
    public String getTxtMaximumLoanAmount() {
        return txtMaximumLoanAmount;
    }

    /**
     * Setter for property txtMaximumLoanAmount.
     *
     * @param txtMaximumLoanAmount New value of property txtMaximumLoanAmount.
     */
    public void setTxtMaximumLoanAmount(String txtMaximumLoanAmount) {
        this.txtMaximumLoanAmount = txtMaximumLoanAmount;
    }

    public String getTxtMaxLoanSurety() {
        return txtMaxLoanSurety;
    }

    public void setTxtMaxLoanSurety(String txtMaxLoanSurety) {
        this.txtMaxLoanSurety = txtMaxLoanSurety;
    }

    /**
     * Getter for property selectedList.
     *
     * @return Value of property selectedList.
     */
    public List getSelectedList() {
        return selectedList;
    }

    /**
     * Setter for property selectedList.
     *
     * @param selectedList New value of property selectedList.
     */
    public void setSelectedList(List selectedList) {
        this.selectedList = selectedList;
    }

    /**
     * Getter for property pan.
     *
     * @return Value of property pan.
     */
    public int getPan() {
        return pan;
    }

    /**
     * Setter for property pan.
     *
     * @param pan New value of property pan.
     */
    public void setPan(int pan) {
        this.pan = pan;
    }

    /**
     * Getter for property tdtEffectiveDate.
     *
     * @return Value of property tdtEffectiveDate.
     */
    public String getTdtEffectiveDate() {
        return tdtEffectiveDate;
    }

    /**
     * Setter for property tdtEffectiveDate.
     *
     * @param tdtEffectiveDate New value of property tdtEffectiveDate.
     */
    public void setTdtEffectiveDate(String tdtEffectiveDate) {
        this.tdtEffectiveDate = tdtEffectiveDate;
    }
  
    //this function added by Anju Anand for Mantis ID: 0010365
    public boolean chkImbpExists(HashMap dataMap) {
        if (dataMap.containsKey("PROD_ID_LIST") && dataMap.get("PROD_ID_LIST") != null) {
            List prodIdList = null;
            prodIdList = (List) dataMap.get("PROD_ID_LIST");
            for (int i = 0; i < prodIdList.size(); i++) {
                String prodId = "";
                prodId = CommonUtil.convertObjToStr(prodIdList.get(i));
                dataMap.put("PROD_ID", prodId);
                List list = null;
                list = ClientUtil.executeQuery("checkImbpExists", dataMap);
                if (list != null && list.size() > 0) {
                    HashMap resultMap = new HashMap();
                    resultMap = (HashMap) list.get(0);
                    String imbpId = "";
                    imbpId = CommonUtil.convertObjToStr(resultMap.get("IMBP_ID"));
                    if (imbpId != null && !imbpId.equals("")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public String getTxtMaxSuretyAmt() {
        return txtMaxSuretyAmt;
    }

    public void setTxtMaxSuretyAmt(String txtMaxSuretyAmt) {
        this.txtMaxSuretyAmt = txtMaxSuretyAmt;
    }
    
}  