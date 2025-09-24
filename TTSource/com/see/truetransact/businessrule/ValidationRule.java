/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ValidationRule.java
 *
 * Created on September 16, 2003, 11:18 AM
 */

package com.see.truetransact.businessrule;

import java.util.HashMap;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;

/**
 * @author  balachandar
 */
public abstract class ValidationRule {
    
    protected String _branchCode = null;
    /** Creates a new instance of ValidationRule */
    public ValidationRule() {
    }

    public abstract void validate(HashMap inputMap) throws Exception;
}
