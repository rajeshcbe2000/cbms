/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ChargesFactory.java
 *
 * Created on January 19, 2005, 2:44 PM
 */
package com.see.truetransact.serverside.charges;

import java.util.HashMap;

/**
 *
 * @author Balachandar
 */
public class ChargeFactory {

    /**
     * Creates a new instance of ChargesFactory
     */
    public ChargeFactory() {
    }

    /**
     * Returns a Charge based on configuration.
     *
     * @throws Exception Throws Exception based on the error
     * @return Returns Charge based on Configuration
     */
    public static Charge createCharge(String productType, String chargeType, HashMap param) throws Exception {
        if (chargeType.equals("Folio")) {
            //return new FolioCharge();
        }
        throw new ChargeNotFoundException("Charge Not Found " + chargeType);
    }
}
