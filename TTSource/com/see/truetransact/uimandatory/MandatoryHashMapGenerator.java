/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * MandatoryHashMapGenerator.java
 *
 * Created on July 25, 2003, 11:59 AM
 */

package com.see.truetransact.uimandatory;
import java.util.HashMap;
import java.io.FileWriter;
import java.util.Set;
import java.util.Iterator;
import java.util.ListResourceBundle;
import com.see.adminsupport.AdminConstants;

/** This class is used to generate the HashMap class
 * @author karthik, Bala
 */
public class MandatoryHashMapGenerator {
    private String componentName;
    /** Creates a new instance of MandatoryHashMapGenerator */
    public MandatoryHashMapGenerator() {
    }
    
    /** To create the HashMap class based on the HashMap & classname given
     * @param hashMap
     * @param className
     * @return
     */    
    public boolean generateHashMap(HashMap hashMap, String className, String filePath) {
        try {
            final Set mandatoryKeySet = hashMap.keySet();
            final Iterator mandatoryKeyIterator = mandatoryKeySet.iterator();
            final int mandatoryKeysLength = mandatoryKeySet.size();
            String classFileName = className.substring(className.lastIndexOf(".")+1);
            classFileName = classFileName.substring(0,classFileName.length()-2)+"HashMap";
            final String pathNameFile = filePath+"/"+classFileName+".java";
            FileWriter mandatoryHashMapFileWriter = new FileWriter(pathNameFile);
            mandatoryHashMapFileWriter.write(AdminConstants.getCopyright(classFileName));
            mandatoryHashMapFileWriter.write("package "+className.substring(0,className.lastIndexOf("."))+";\n\n");
            mandatoryHashMapFileWriter.write("import java.util.HashMap;\n");
            mandatoryHashMapFileWriter.write("import com.see.truetransact.uimandatory.UIMandatoryHashMap;\n\n");
            mandatoryHashMapFileWriter.write("public class "+classFileName+" implements UIMandatoryHashMap {\n");
            mandatoryHashMapFileWriter.write("    private HashMap mandatoryMap;\n\n");
            mandatoryHashMapFileWriter.write("    public "+classFileName+"(){\n");
            mandatoryHashMapFileWriter.write("        mandatoryMap = new HashMap();\n");
            
            StringBuffer mandatoryFields = new StringBuffer();
            for(int i = mandatoryKeysLength,j=0; i > 0;i--,j++){
                componentName = (String)mandatoryKeyIterator.next();
                mandatoryFields.append("        mandatoryMap.put(\"");
                mandatoryFields.append(componentName).append("\", new Boolean(");
                mandatoryFields.append(getState(hashMap));
                mandatoryFields.append("));");
                mandatoryHashMapFileWriter.write(mandatoryFields.toString()+"\n");
                mandatoryFields.delete(0, mandatoryFields.length());
            }
            
            mandatoryHashMapFileWriter.write("    }\n\n");
            mandatoryHashMapFileWriter.write("    public HashMap getMandatoryHashMap(){\n");
            mandatoryHashMapFileWriter.write("        return this.mandatoryMap;\n");
            mandatoryHashMapFileWriter.write("    }\n");
            mandatoryHashMapFileWriter.write("}\n");
            mandatoryHashMapFileWriter.close();
            
           /* int compileReturnCode =
            com.sun.tools.javac.Main.compile(
            new String[] {pathNameFile});
            if (compileReturnCode == 0) {
                //System.out.println("Compiled Successfully!!!");
                return true;
            }*/
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /** To check whether the ComponentName in the given hashmap is set to Mandatory or
     * not. If mandatory(ie,true), return "true",else return "false"
     * @param hashMap
     * @return
     */    
    private String getState(HashMap hashMap){
        if( ((Boolean)hashMap.get(componentName)).booleanValue() ){
           return "true";
        }
        else{
           return "false";
        }
    }
}

