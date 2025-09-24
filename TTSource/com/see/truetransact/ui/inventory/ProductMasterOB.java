/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ProductMasterOB.java
 * 
 * Created on Mon Jun 20 16:52:36 GMT+05:30 2011
 */

package com.see.truetransact.ui.inventory;

import java.util.Observable;
import java.util.HashMap;

/**
 *
 * @author  
 */

public class ProductMasterOB extends Observable{

	private String txtProductDesc = "";
	private String txtProductCode = "";
	private String txtPurchasePrice = "";
	private String txtSellingPrice = "";
	private String cboUnit = "";
	private String txtQty = "";
	private String txtReorderLevel = "";
	private String txtPurchaseAcHd = "";
	private String txtSalesAcHd = "";
	private String txtTaxAcHd = "";
	private String txtPurchaseReturnAcHd = "";
	private String txtSalesReturnAcHd = "";

	// Setter method for txtProductDesc
	void setTxtProductDesc(String txtProductDesc){
		this.txtProductDesc = txtProductDesc;
		setChanged();
	}
	// Getter method for txtProductDesc
	String getTxtProductDesc(){
		return this.txtProductDesc;
	}

	// Setter method for txtProductCode
	void setTxtProductCode(String txtProductCode){
		this.txtProductCode = txtProductCode;
		setChanged();
	}
	// Getter method for txtProductCode
	String getTxtProductCode(){
		return this.txtProductCode;
	}

	// Setter method for txtPurchasePrice
	void setTxtPurchasePrice(String txtPurchasePrice){
		this.txtPurchasePrice = txtPurchasePrice;
		setChanged();
	}
	// Getter method for txtPurchasePrice
	String getTxtPurchasePrice(){
		return this.txtPurchasePrice;
	}

	// Setter method for txtSellingPrice
	void setTxtSellingPrice(String txtSellingPrice){
		this.txtSellingPrice = txtSellingPrice;
		setChanged();
	}
	// Getter method for txtSellingPrice
	String getTxtSellingPrice(){
		return this.txtSellingPrice;
	}

	// Setter method for cboUnit
	void setCboUnit(String cboUnit){
		this.cboUnit = cboUnit;
		setChanged();
	}
	// Getter method for cboUnit
	String getCboUnit(){
		return this.cboUnit;
	}

	// Setter method for txtQty
	void setTxtQty(String txtQty){
		this.txtQty = txtQty;
		setChanged();
	}
	// Getter method for txtQty
	String getTxtQty(){
		return this.txtQty;
	}

	// Setter method for txtReorderLevel
	void setTxtReorderLevel(String txtReorderLevel){
		this.txtReorderLevel = txtReorderLevel;
		setChanged();
	}
	// Getter method for txtReorderLevel
	String getTxtReorderLevel(){
		return this.txtReorderLevel;
	}

	// Setter method for txtPurchaseAcHd
	void setTxtPurchaseAcHd(String txtPurchaseAcHd){
		this.txtPurchaseAcHd = txtPurchaseAcHd;
		setChanged();
	}
	// Getter method for txtPurchaseAcHd
	String getTxtPurchaseAcHd(){
		return this.txtPurchaseAcHd;
	}

	// Setter method for txtSalesAcHd
	void setTxtSalesAcHd(String txtSalesAcHd){
		this.txtSalesAcHd = txtSalesAcHd;
		setChanged();
	}
	// Getter method for txtSalesAcHd
	String getTxtSalesAcHd(){
		return this.txtSalesAcHd;
	}

	// Setter method for txtTaxAcHd
	void setTxtTaxAcHd(String txtTaxAcHd){
		this.txtTaxAcHd = txtTaxAcHd;
		setChanged();
	}
	// Getter method for txtTaxAcHd
	String getTxtTaxAcHd(){
		return this.txtTaxAcHd;
	}

	// Setter method for txtPurchaseReturnAcHd
	void setTxtPurchaseReturnAcHd(String txtPurchaseReturnAcHd){
		this.txtPurchaseReturnAcHd = txtPurchaseReturnAcHd;
		setChanged();
	}
	// Getter method for txtPurchaseReturnAcHd
	String getTxtPurchaseReturnAcHd(){
		return this.txtPurchaseReturnAcHd;
	}

	// Setter method for txtSalesReturnAcHd
	void setTxtSalesReturnAcHd(String txtSalesReturnAcHd){
		this.txtSalesReturnAcHd = txtSalesReturnAcHd;
		setChanged();
	}
	// Getter method for txtSalesReturnAcHd
	String getTxtSalesReturnAcHd(){
		return this.txtSalesReturnAcHd;
	}


}