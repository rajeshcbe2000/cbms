/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriCardDAO.java
 *
 * Created on January 1, 2009, 6:01 PM
 */
package com.see.truetransact.serverside.product.loan.agriculturecard;

import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.transferobject.product.loan.agriculturecard.AgriCardTo;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.LinkedHashMap;
import com.ibatis.db.sqlmap.SqlMap;

/**
 *
 * @author Administrator
 */
public class AgriCardDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private String commandMode = null;

    /**
     * Creates a new instance of AgriCardDAO
     */
    public AgriCardDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public java.util.HashMap execute(java.util.HashMap obj) throws Exception {
        System.out.println("Agri Card Loan" + obj);

        return actionPerform(obj);
    }

    public HashMap actionPerform(HashMap obj) throws Exception {
        LinkedHashMap map = new LinkedHashMap();
        if (!obj.containsKey(CommonConstants.AUTHORIZEDATA)) {
            //insert  update
            map = (LinkedHashMap) obj.get("AgriCardTos");
            for (int i = 0; i < map.size(); i++) {
                AgriCardTo objAgriCardTo = (AgriCardTo) map.get(String.valueOf(i + 1));
                System.out.println("objAgriCardTo" + objAgriCardTo);
                double prod_No = CommonUtil.convertObjToDouble(objAgriCardTo.getProdNo()).doubleValue();
                System.out.println("prod_No###" + prod_No);
                if (objAgriCardTo.getStatus().equals("CREATED") && prod_No == 0.0) {
                    doInsert(objAgriCardTo);
                }
                if (objAgriCardTo.getStatus().equals("MODIFIED") || prod_No > 0) {
                    doUpdate(objAgriCardTo);
                }
//            if(objAgriCardTo.getStatus().equals("DELETED"))
//                doDelete(objAgriCardTo);

            }
            if (obj != null && obj.containsKey("TO_DELETED_AT_UPDATE_MODE") && obj.get("TO_DELETED_AT_UPDATE_MODE") != null) {
                map = (LinkedHashMap) obj.get("TO_DELETED_AT_UPDATE_MODE");

                for (int i = 0; i < map.size(); i++) {
                    AgriCardTo objAgriCardTo = (AgriCardTo) map.get(String.valueOf(i + 1));
                    System.out.println("objAgriCardTo" + objAgriCardTo);

                    objAgriCardTo.setStatus("DELETED");
                    doDelete(objAgriCardTo);

                }
            }
        } else {
            authorizePerform(obj);
        }
        return obj;

    }

    public void authorizePerform(HashMap authMap) throws Exception {
        System.out.println("authMap####" + authMap);
        HashMap updateMap = (HashMap) authMap.get(CommonConstants.AUTHORIZEDATA);
        sqlMap.executeUpdate("authorizeAgriCard", updateMap);
        updateMap = null;
        authMap = null;
    }

    public void doInsert(AgriCardTo objAgriCardTo) throws Exception {
        HashMap agriMap = new HashMap();
        agriMap.put("AGRI_CARD_TYPE", objAgriCardTo.getAgriCardType());
        List lst = (List) sqlMap.executeQueryForList("getSelectProdNo", agriMap);
        if (lst != null && lst.size() > 0) {
            agriMap = (HashMap) lst.get(0);
            objAgriCardTo.setProdNo(CommonUtil.convertObjToDouble(agriMap.get("MAX_NO")));
        } else {
            objAgriCardTo.setProdNo(new Double("1"));
        }
        System.out.println("objAgriCardTo" + objAgriCardTo);
        sqlMap.executeUpdate("insertIntoAgriCardTo", objAgriCardTo);

        objAgriCardTo = null;
    }

    public void doUpdate(AgriCardTo objAgriCardTo) throws Exception {
        sqlMap.executeUpdate("updateAgriCardTo", objAgriCardTo);
        objAgriCardTo = null;

    }

    public void doDelete(AgriCardTo objAgriCardTo) throws Exception {
        sqlMap.executeUpdate("deleteAgriCardTo", objAgriCardTo);


    }

    public java.util.HashMap executeQuery(java.util.HashMap obj) throws Exception {
        System.out.println("obj#####" + obj);
        HashMap resultMap = getData(obj);
        return resultMap;
    }

    private HashMap getData(HashMap obj) throws Exception {
        commandMode = (String) obj.get("MODE");
        HashMap resultMap = new HashMap();
        if (commandMode != null && commandMode.equals(CommonConstants.TOSTATUS_INSERT)) {
        }
        if ((commandMode != null && commandMode.equals(CommonConstants.TOSTATUS_UPDATE)) || (commandMode != null && commandMode.equals(CommonConstants.TOSTATUS_DELETE))) {
            List lst = sqlMap.executeQueryForList("getSelectAgriCardTo", obj.get("AGRI_CARD_TYPE"));
            System.out.println("lst#####" + lst);
            resultMap.put("AgriCardList", lst);
        }
//        if(commandMode!=null && commandMode.equals(CommonConstants.TOSTATUS_DELETE)){
//            
//        }
        if ((commandMode != null && commandMode.equals("AUTHORIZE")) || (commandMode != null && commandMode.equals("REJECT"))) {
            List lst = sqlMap.executeQueryForList("getSelectAuthorizeAgriCardTo", obj);
            System.out.println("lst#####" + lst);
            resultMap.put("AgriCardList", lst);
        }
        return resultMap;
    }
}
