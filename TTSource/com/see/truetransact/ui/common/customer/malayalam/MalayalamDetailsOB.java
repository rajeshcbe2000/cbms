/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * MalayalamDetailsOB.java
 *
 * Created on Thu Jan 20 15:43:27 IST 2005
 */
package com.see.truetransact.ui.common.customer.malayalam;
/**
 * Author   : Chithra
 * Location : Thrissur
 * Date of Completion : 14-05-2015
 */
import org.apache.log4j.Logger;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.customer.CustRegionalTo;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.*;

public class MalayalamDetailsOB extends CObservable {

    /**
     * Variables Declaration - Corresponding each Variable is in TokenConfigUI
     */
    private String txtRegMName = "";
    private String txtRegMaHname = "";
    private String txtRegMaPlace = "";
    private String txtRegMavillage = "";
    private String txtRegMaAmsam = "";
    private String txtRegMaDesam = "";
    private String txtRegMaGardName = "";
    private String txtMemNo = "";
    private String txtRegName = "";
    private String txtRegHname = "";
    private String txtRegPlace = "";
    private String txtRegvillage = "";
    private String txtRegAmsam = "";
    private String txtRegDesam = "";
    private String txtRegGardName = "";
    private String txtRegCustId = "";
    /**
     * Other Varibales Declartion
     */
    private ProxyFactory proxy;
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private static MalayalamDetailsOB objTokenConfigOB;//Singleton Object Reference
    private final static Logger log = Logger.getLogger(MalayalamDetailsOB.class);//Creating Instace of Log
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private CustRegionalTo objCustRegionalTo;//Reference for the EntityBean TokenConfigTO
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String txttaxHeadId = "";
    String command = "";

    /**
     * Consturctor Declaration for TokenConfigOB
     */
    private MalayalamDetailsOB() {
        try {
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initUIComboBoxModel();
            fillDropdown();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    static {
        try {
            log.info("Creating ParameterOB...");
            objTokenConfigOB = new MalayalamDetailsOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "CustRegionalJNDI");
        map.put(CommonConstants.HOME, "customer.custRegional.CustRegionalHome");
        map.put(CommonConstants.REMOTE, "customer.custRegional.CustRegional");
    }

    /**
     * Creating instance for ComboboxModel cbmTokenType
     */
    private void initUIComboBoxModel() {
    }

    /*
     * Filling up the the ComboBox in the UI
     */
    private void fillDropdown() throws Exception {
        try {
            log.info("Inside FillDropDown");

        } catch (NullPointerException e) {
            parseException.logException(e, true);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public String getTxtRegCustId() {
        return txtRegCustId;
    }

    public void setTxtRegCustId(String txtRegCustId) {
        this.txtRegCustId = txtRegCustId;
    }

    public String getTxtRegAmsam() {
        return txtRegAmsam;
    }

    public void setTxtRegAmsam(String txtRegAmsam) {
        this.txtRegAmsam = txtRegAmsam;
    }

    public String getTxtRegDesam() {
        return txtRegDesam;
    }

    public void setTxtRegDesam(String txtRegDesam) {
        this.txtRegDesam = txtRegDesam;
    }

    public String getTxtRegGardName() {
        return txtRegGardName;
    }

    public void setTxtRegGardName(String txtRegGardName) {
        this.txtRegGardName = txtRegGardName;
    }

    public String getTxtRegHname() {
        return txtRegHname;
    }

    public void setTxtRegHname(String txtRegHname) {
        this.txtRegHname = txtRegHname;
    }

    public String getTxtRegName() {
        return txtRegName;
    }

    public void setTxtRegName(String txtRegName) {
        this.txtRegName = txtRegName;
    }

    public String getTxtRegPlace() {
        return txtRegPlace;
    }

    public void setTxtRegPlace(String txtRegPlace) {
        this.txtRegPlace = txtRegPlace;
    }

    public String getTxtRegvillage() {
        return txtRegvillage;
    }

    public void setTxtRegvillage(String txtRegvillage) {
        this.txtRegvillage = txtRegvillage;
    }

    public String getTxttaxHeadId() {
        return txttaxHeadId;
    }

    public void setTxttaxHeadId(String txttaxHeadId) {
        this.txttaxHeadId = txttaxHeadId;
    }

    /**
     * Returns an instance of TokenConfigOB.
     *
     * @return TokenConfigOB
     */
    public static MalayalamDetailsOB getInstance() throws Exception {
        return objTokenConfigOB;
    }

    public String getTxtMemNo() {
        return txtMemNo;
    }

    public void setTxtMemNo(String txtMemNo) {
        this.txtMemNo = txtMemNo;
    }

    public String getTxtRegMName() {
        return txtRegMName;
    }

    public void setTxtRegMName(String txtRegMName) {
        this.txtRegMName = txtRegMName;
    }

    public String getTxtRegMaAmsam() {
        return txtRegMaAmsam;
    }

    public void setTxtRegMaAmsam(String txtRegMaAmsam) {
        this.txtRegMaAmsam = txtRegMaAmsam;
    }

    public String getTxtRegMaDesam() {
        return txtRegMaDesam;
    }

    public void setTxtRegMaDesam(String txtRegMaDesam) {
        this.txtRegMaDesam = txtRegMaDesam;
    }

    public String getTxtRegMaGardName() {
        return txtRegMaGardName;
    }

    public void setTxtRegMaGardName(String txtRegMaGardName) {
        this.txtRegMaGardName = txtRegMaGardName;
    }

    public String getTxtRegMaHname() {
        return txtRegMaHname;
    }

    public void setTxtRegMaHname(String txtRegMaHname) {
        this.txtRegMaHname = txtRegMaHname;
    }

    public String getTxtRegMaPlace() {
        return txtRegMaPlace;
    }

    public void setTxtRegMaPlace(String txtRegMaPlace) {
        this.txtRegMaPlace = txtRegMaPlace;
    }

    public String getTxtRegMavillage() {
        return txtRegMavillage;
    }

    public void setTxtRegMavillage(String txtRegMavillage) {
        this.txtRegMavillage = txtRegMavillage;
    }

    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }

    public int getActionType() {
        return _actionType;
    }

    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }

    /**
     * Getter for property lblStatus.
     *
     * @return Value of property lblStatus.
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }

    /**
     * Setter for property lblStatus.
     *
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }

    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }

    public int getResult() {
        return _result;
    }

    /**
     * Creates an Instance of TokenConfigTO Bean and sets its variables with
     * OBMethods
     */
    private void populateCustRegionalDetails(CustRegionalTo objtCustRegionalTo) {
        System.out.println("objtCustRegionalTo  :" + objtCustRegionalTo);
        setTxtRegMName(objtCustRegionalTo.getFname());
        setTxtRegMaHname(objtCustRegionalTo.getHouseName());
        setTxtRegMaPlace(objtCustRegionalTo.getPlace());
        setTxtRegMavillage(objtCustRegionalTo.getVillage());
        setTxtRegMaAmsam(objtCustRegionalTo.getAmsam());
        setTxtRegMaDesam(objtCustRegionalTo.getDesam());
        setTxtRegMaGardName(objtCustRegionalTo.getCareOfName());
        setTxtMemNo(objtCustRegionalTo.getMemNo());
        setTxtRegCustId(objtCustRegionalTo.getCustId());
    }

    private CustRegionalTo setCustRegionalTo() {
        CustRegionalTo objTo = new CustRegionalTo();
        objTo.setFname(getTxtRegMName());
        objTo.setHouseName(getTxtRegMaHname());
        objTo.setPlace(getTxtRegMaPlace());
        objTo.setVillage(getTxtRegMavillage());
        objTo.setCareOfName(getTxtRegMaGardName());
        objTo.setCity("");
        objTo.setTaluk("");
        objTo.setCountry("");
        objTo.setState("");
        objTo.setAmsam(getTxtRegMaAmsam());
        objTo.setDesam(getTxtRegMaDesam());
        objTo.setMemNo(getTxtMemNo());
        objTo.setCustId(getTxtRegCustId());
        objTo.setCommand(command);
        return objTo;
    }

    /**
     * Resets all the UI Fields
     */
    public void resetForm() {
        setTxtRegMName("");
        setTxtRegMaHname("");
        setTxtRegMaPlace("");
        setTxtRegMavillage("");
        setTxtRegMaAmsam("");
        setTxtRegMaDesam("");
        setTxtRegMaGardName("");
        setTxtRegName("");
        setTxtRegHname("");
        setTxtRegPlace("");
        setTxtRegvillage("");
        setTxtRegAmsam("");
        setTxtRegDesam("");
        setTxtRegGardName("");
        setTxtRegCustId("");
        notifyObservers();
    }

    /*
     * Executes Query using the TO object
     */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            command = command;
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            term.put("CustRegionalTo", setCustRegionalTo());
            HashMap proxyReturnMap = proxy.execute(term, map);
            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
        } catch (Exception e) {
            e.printStackTrace();
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    /*
     * Populates the TO object by executing a Query
     */
    public void getData(HashMap whereMap) {
        HashMap mapData = null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            System.out.println("mapData :" + mapData);
            if (mapData != null && mapData.containsKey("CUST_REGIONAL_DATA")) {
                List valList = (List) mapData.get("CUST_REGIONAL_DATA");
                if (valList != null && valList.size() > 0) {
                    HashMap sing = (HashMap) valList.get(0);
                    if (sing != null && sing.size() > 0) {
                        setTxtRegName(CommonUtil.convertObjToStr(sing.get("FNAME")));
                        setTxtRegHname(CommonUtil.convertObjToStr(sing.get("STREET")));
                        setTxtRegPlace(CommonUtil.convertObjToStr(sing.get("AREA")));
                        setTxtRegvillage(CommonUtil.convertObjToStr(sing.get("VILLAGE")));
                        setTxtRegGardName(CommonUtil.convertObjToStr(sing.get("CARE_OF_NAME")));
                        setTxtRegAmsam(CommonUtil.convertObjToStr(sing.get("AMSAM")));
                        setTxtRegDesam(CommonUtil.convertObjToStr(sing.get("DESAM")));
                        setTxtRegCustId(CommonUtil.convertObjToStr(sing.get("CUST_ID")));
                    }
                } else {
                    setTxtRegName("");
                    setTxtRegHname("");
                    setTxtRegPlace("");
                    setTxtRegvillage("");
                    setTxtRegGardName("");
                    setTxtRegAmsam("");
                    setTxtRegDesam("");
                    setTxtRegCustId("");
                }
            } else {
                setTxtRegName("");
                setTxtRegHname("");
                setTxtRegPlace("");
                setTxtRegvillage("");
                setTxtRegGardName("");
                setTxtRegAmsam("");
                setTxtRegDesam("");
                setTxtRegCustId("");
            }
            if (mapData != null && mapData.containsKey("CUST_REGIONAL_DATA_OBJ")) {
                CustRegionalTo objCustRegionalTo =
                        (CustRegionalTo) ((List) mapData.get("CUST_REGIONAL_DATA_OBJ")).get(0);
                populateCustRegionalDetails(objCustRegionalTo);
                if (objCustRegionalTo == null || objCustRegionalTo.getMemNo().length() <= 0) {
                    setTxtRegMName("");
                    setTxtRegMaHname("");
                    setTxtRegMaPlace("");
                    setTxtRegMavillage("");
                    setTxtRegMaAmsam("");
                    setTxtRegMaDesam("");
                    setTxtRegMaGardName("");
                }
            } else {
                setTxtRegMName("");
                setTxtRegMaHname("");
                setTxtRegMaPlace("");
                setTxtRegMavillage("");
                setTxtRegMaAmsam("");
                setTxtRegMaDesam("");
                setTxtRegMaGardName("");
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);

        }
    }

    public void verifyAcctHead(com.see.truetransact.uicomponent.CTextField accountHead, String mapName) {
        try {
            final HashMap data = new HashMap();
            data.put("ACCT_HD", accountHead.getText());
            data.put(CommonConstants.MAP_NAME, mapName);
            HashMap proxyResultMap = proxy.execute(data, map);
        } catch (Exception e) {
            System.out.println("Error in verifyAcctHead");
            accountHead.setText("");
            parseException.logException(e, true);
        }
    }
    /*
     * Checks for the duplication of TokenType if so retuns a boolean type
     * vairable as true
     */

    public String isTokenTypeExists(Date frmDt) {
        String exists = "";
        HashMap resultMap = null;
        HashMap where = new HashMap();
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        where.put("WEFDATE", frmDt);
        try {
            ArrayList resultList = (ArrayList) ClientUtil.executeQuery("getValidateWEFDate", where);
            where = null;
            if (resultList.size() > 0) {
                for (int i = 0; i < resultList.size(); i++) {
                    resultMap = (HashMap) resultList.get(i);
                    if (resultMap != null && resultMap.containsKey("SERVICETAX_GEN_ID")) {
                        String val = CommonUtil.convertObjToStr(resultMap.get("AUTHORIZED_STATUS"));
                        if (val != null && val.length() > 0) {
                            exists = val;
                        } else {
                            exists = CommonUtil.convertObjToStr(resultMap.get("STATUS"));
                        }
                        break;

                    }
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);

        }
        return exists;
    }
    /*
     * Checks for the duplication of SeriesNo if so retuns a boolean type
     * vairable as true
     */
}