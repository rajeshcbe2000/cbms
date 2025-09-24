/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TOHeader.java
 *
 * Created on September 10, 2003, 4:35 PM
 */
package com.see.truetransact.transferobject;

/**
 * @author balachandar
 */
public class TOHeader implements java.io.Serializable {

    /**
     * Holds value of property command.
     */
    private String command;
    private String XMLHEADER_START = "\t<TOHeader>\n";
    private String XMLHEADER_END = "\t</TOHeader>\n";

    /**
     * Creates a new instance of TOHeader
     */
    public TOHeader() {
    }

    /**
     * Getter for property command.
     *
     * @return Value of property command.
     *
     */
    public String getCommand() {
        return this.command;
    }

    /**
     * Setter for property command.
     *
     * @param command New value of property command.
     *
     */
    public void setCommand(String command) {
        this.command = command;
    }

    public String toString() {
        StringBuffer strB = new StringBuffer();
        strB.append(TransferObject.SEPARATOR);
        strB.append("command=");
        strB.append(getCommand());
        strB.append(TransferObject.SEPARATOR);
        return strB.toString();
    }

    public String toXML() {
        StringBuffer strB = new StringBuffer();
        strB.append(XMLHEADER_START);
        strB.append("\t\t<command>");
        strB.append(getCommand());
        strB.append("</command>\n");
        strB.append(XMLHEADER_END);
        return strB.toString();
    }
}
