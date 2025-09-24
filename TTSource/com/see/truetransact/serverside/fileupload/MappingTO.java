/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * MappingTO.java
 *
 * Created on May 25, 2005, 1:26 PM
 */
package com.see.truetransact.serverside.fileupload;

/**
 *
 * @author 152691
 */
public class MappingTO {

    private String position;
    private String dbName;
    private String tableName;
    private String columnName;
    private String targetDataType;
    private String sourceFormat;
    private String defaultValue;
    private String startPosition;
    private String endPosition;
    private String columnPosition;
    private String tagName;

    /**
     * Creates a new instance of MappingTO
     */
    public MappingTO() {
    }

    /**
     * Getter for property position.
     *
     * @return Value of property position.
     */
    public String getPosition() {
        return position;
    }

    /**
     * Setter for property position.
     *
     * @param position New value of property position.
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Getter for property dbName.
     *
     * @return Value of property dbName.
     */
    public java.lang.String getDbName() {
        return dbName;
    }

    /**
     * Setter for property dbName.
     *
     * @param dbName New value of property dbName.
     */
    public void setDbName(java.lang.String dbName) {
        this.dbName = dbName;
    }

    /**
     * Getter for property tableName.
     *
     * @return Value of property tableName.
     */
    public java.lang.String getTableName() {
        return tableName;
    }

    /**
     * Setter for property tableName.
     *
     * @param tableName New value of property tableName.
     */
    public void setTableName(java.lang.String tableName) {
        this.tableName = tableName;
    }

    /**
     * Getter for property columnName.
     *
     * @return Value of property columnName.
     */
    public java.lang.String getColumnName() {
        return columnName;
    }

    /**
     * Setter for property columnName.
     *
     * @param columnName New value of property columnName.
     */
    public void setColumnName(java.lang.String columnName) {
        this.columnName = columnName;
    }

    /**
     * Getter for property targetDataType.
     *
     * @return Value of property targetDataType.
     */
    public java.lang.String getTargetDataType() {
        return targetDataType;
    }

    /**
     * Setter for property targetDataType.
     *
     * @param targetDataType New value of property targetDataType.
     */
    public void setTargetDataType(java.lang.String targetDataType) {
        this.targetDataType = targetDataType;
    }

    /**
     * Getter for property sourceFormat.
     *
     * @return Value of property sourceFormat.
     */
    public java.lang.String getSourceFormat() {
        return sourceFormat;
    }

    /**
     * Setter for property sourceFormat.
     *
     * @param sourceFormat New value of property sourceFormat.
     */
    public void setSourceFormat(java.lang.String sourceFormat) {
        this.sourceFormat = sourceFormat;
    }

    /**
     * Getter for property defaultValue.
     *
     * @return Value of property defaultValue.
     */
    public java.lang.String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Setter for property defaultValue.
     *
     * @param defaultValue New value of property defaultValue.
     */
    public void setDefaultValue(java.lang.String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Getter for property startPosition.
     *
     * @return Value of property startPosition.
     */
    public String getStartPosition() {
        return startPosition;
    }

    /**
     * Setter for property startPosition.
     *
     * @param startPosition New value of property startPosition.
     */
    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    /**
     * Getter for property endPosition.
     *
     * @return Value of property endPosition.
     */
    public String getEndPosition() {
        return endPosition;
    }

    /**
     * Setter for property endPosition.
     *
     * @param endPosition New value of property endPosition.
     */
    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
    }

    /**
     * Getter for property columnPosition.
     *
     * @return Value of property columnPosition.
     */
    public String getColumnPosition() {
        return columnPosition;
    }

    /**
     * Setter for property columnPosition.
     *
     * @param columnPosition New value of property columnPosition.
     */
    public void setColumnPosition(String columnPosition) {
        this.columnPosition = columnPosition;
    }

    /**
     * Getter for property tagName.
     *
     * @return Value of property tagName.
     */
    public java.lang.String getTagName() {
        return tagName;
    }

    /**
     * Setter for property tagName.
     *
     * @param tagName New value of property tagName.
     */
    public void setTagName(java.lang.String tagName) {
        this.tagName = tagName;
    }

    public String toString() {
        StringBuffer strB = new StringBuffer();
        strB.append(position).append("\t");
        strB.append(dbName).append("\t");
        strB.append(tableName).append("\t");
        strB.append(columnName).append("\t");
        strB.append(targetDataType).append("\t");
        strB.append(sourceFormat).append("\t");
        strB.append(defaultValue).append("\t");
        strB.append(columnPosition).append("\t");
        strB.append(columnName).append("\t");
        strB.append(startPosition).append("\t");
        strB.append(endPosition).append("\t");
        strB.append(tagName).append("\t\n");
        return strB.toString();
    }
}
