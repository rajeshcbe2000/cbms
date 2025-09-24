/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AdvancesProductSplAssetsTO.java
 * 
 * Created on Tue Apr 12 10:02:23 IST 2005
 */
package com.see.truetransact.transferobject.product.advances;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is ADVANCES_PROD_SPLASSET.
 */
public class AdvancesProductSplAssetsTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String assetCategory = "";
    private Double assetCategoryRateper = null;

    /**
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
    }

    /**
     * Setter/Getter for ASSET_CATEGORY - table Field
     */
    public void setAssetCategory(String assetCategory) {
        this.assetCategory = assetCategory;
    }

    public String getAssetCategory() {
        return assetCategory;
    }

    /**
     * Setter/Getter for ASSET_CATEGORY_RATEPER - table Field
     */
    public void setAssetCategoryRateper(Double assetCategoryRateper) {
        this.assetCategoryRateper = assetCategoryRateper;
    }

    public Double getAssetCategoryRateper() {
        return assetCategoryRateper;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("prodId");
        return prodId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("assetCategory", assetCategory));
        strB.append(getTOString("assetCategoryRateper", assetCategoryRateper));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("assetCategory", assetCategory));
        strB.append(getTOXml("assetCategoryRateper", assetCategoryRateper));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}