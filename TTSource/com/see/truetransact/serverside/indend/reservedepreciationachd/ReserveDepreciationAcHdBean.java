/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TokenConfigBean.java
 * 
 * Created on Mon Jun 24 17:19:05 IST 2019
 */
package com.see.truetransact.serverside.indend.reservedepreciationachd;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;
import java.util.HashMap;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;

public class ReserveDepreciationAcHdBean implements SessionBean, TTDAOImpl {

    ReserveDepreciationAcHdDAO daoReserve = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoReserve = new ReserveDepreciationAcHdDAO();
    }

    public void ejbRemove() {
        daoReserve = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoReserve.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoReserve.executeQuery(obj);
    }
}
