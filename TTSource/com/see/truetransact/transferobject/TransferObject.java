/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TransferObject.java
 *
 * Created on September 11, 2003, 11:02 AM
 */
package com.see.truetransact.transferobject;

import com.see.truetransact.transferobject.TOHeader;

/**
 * @author balachandar
 */
public abstract class TransferObject implements java.io.Serializable {

    public static final String SEPARATOR = "<%%>";
    public static final String XML_START = "<ROOT>\n";
    public static final String XML_END = "</ROOT>\n";
    public static final String KEY_VAL_SEPARATOR = "+";
    private TOHeader tOHeader = new TOHeader();
    private String _className;
    private String _packageName;
    private String _keyColumns;
    private String initiatedBranch;

    /**
     * Creates a new instance of TransferObject
     */
    public TransferObject() {
    }

    /**
     * Getter for property tOHeader.
     *
     * @return Value of property tOHeader.
     */
    public TOHeader getTOHeader() {
        return tOHeader;
    }

    /**
     * Setter for property tOHeader.
     *
     * @param tOHeader New value of property tOHeader.
     */
    public void setTOHeader(TOHeader tOHeader) {
        this.tOHeader = tOHeader;
    }

    /**
     * Getter for property command.
     *
     * @return Value of property command.
     *
     */
    public String getCommand() {
        return this.tOHeader.getCommand();
    }

    /**
     * Setter for property command.
     *
     * @param command New value of property command.
     *
     */
    public void setCommand(String command) {
        if (this.tOHeader == null) {
            this.tOHeader = new TOHeader();
        }
        this.tOHeader.setCommand(command);
    }

    public void setKeyColumns(String keyCols) {
        _keyColumns = keyCols;
    }

    public String getTOStringStart(String className) {
        _packageName = className;
        _className = className.substring(className.lastIndexOf(".") + 1);

        StringBuffer strBStart = new StringBuffer();
        strBStart.append(tOHeader.toString());
        strBStart.append("class=");
        strBStart.append(_className);
        strBStart.append(SEPARATOR);
        strBStart.append("package=");
        strBStart.append(_packageName);
        strBStart.append(SEPARATOR);
        strBStart.append(initiatedBranch);
        strBStart.append(SEPARATOR);
        return strBStart.toString();
    }

    public String getTOStringKey(String key) {
        return "key=" + _keyColumns + SEPARATOR + "value=" + key + SEPARATOR;
    }

    public String getTOStringEnd() {
        return SEPARATOR;
    }

    public String getTOString(String objName, Object objValue) {
        return SEPARATOR + objName + "=" + objValue;
    }

    public String getTOXmlStart(String className) {
        _packageName = className;
        _className = className.substring(className.lastIndexOf(".") + 1);

        StringBuffer strBStart = new StringBuffer();
        strBStart.append(XML_START);
        strBStart.append(tOHeader.toXML());
        strBStart.append("\t<");
        strBStart.append(_className);
        strBStart.append(" package=\"");
        strBStart.append(_packageName);
        strBStart.append(" initiatedBranch=\"");
        strBStart.append(initiatedBranch);
        strBStart.append("\">\n");
        return strBStart.toString();
    }

    public String getTOXmlKey(String key) {
        return "\t\t<TOkey>\n\t\t\t<key>" + _keyColumns + "</key>\n\t\t\t<value>" + key + "</value>\n\t\t</TOkey>\n";
    }

    public String getTOXmlEnd() {
        StringBuffer strBEnd = new StringBuffer();
        strBEnd.append("\t</");
        strBEnd.append(_className);
        strBEnd.append(">\n");
        strBEnd.append(XML_END);
        return strBEnd.toString();
    }

    public String getTOXml(String objName, Object objValue) {
        return "\t\t<" + objName + ">" + objValue + "</" + objName + ">\n";
    }

    /**
     * Getter for property initiatedBranch.
     *
     * @return Value of property initiatedBranch.
     */
    public java.lang.String getInitiatedBranch() {
        return initiatedBranch;
    }

    /**
     * Setter for property initiatedBranch.
     *
     * @param initiatedBranch New value of property initiatedBranch.
     */
    public void setInitiatedBranch(java.lang.String initiatedBranch) {
        this.initiatedBranch = initiatedBranch;
    }
}
