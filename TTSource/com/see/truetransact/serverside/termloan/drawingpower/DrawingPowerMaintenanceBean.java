/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * DrawingPowerMaintenanceBean.java
 * 
 * Created on Fri Jul 16 16:45:20 GMT+05:30 2004
 */
package com.see.truetransact.serverside.termloan.drawingpower;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * DrawingPowerMaintenance Bean which calls the DAO.
 *
 */
public class DrawingPowerMaintenanceBean implements SessionBean, TTDAOImpl {

    DrawingPowerMaintenanceDAO daoDrawingPowerMaintenance = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoDrawingPowerMaintenance = new DrawingPowerMaintenanceDAO();
    }

    public void ejbRemove() {
        daoDrawingPowerMaintenance = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoDrawingPowerMaintenance.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoDrawingPowerMaintenance.executeQuery(obj);
    }
}
