/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ImageFileFilter.java
 *
 * Created on March 2, 2004, 2:01 PM
 */

package com.see.truetransact.clientutil;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author  amathan
 */

public class ImageFileFilter extends FileFilter { 
    
    /** Creates a new instance of ImageFileFilter */
    public ImageFileFilter() {
    }
    
    public boolean accept(File objFile) {
        String fileName = objFile.getName().toLowerCase();
        return objFile.isDirectory() || fileName.endsWith(".jpg") ||  fileName.endsWith(".gif");
    }
    
    public String getDescription() {
        return "*.JPG;*.GIF files";
    }    
}
