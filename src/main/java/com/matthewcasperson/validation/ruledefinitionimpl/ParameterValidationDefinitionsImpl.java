/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Matthew Casperson
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.matthewcasperson.validation.ruledefinitionimpl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * An implementation of the validation definitions container. This is the top level
 * object, and it is this object that created by unmarshalling the XML config
 * in the filter init() method.
 * @author mcasperson
 *
 */
@XmlRootElement(name="ParameterValidationChainDatabase")
public class ParameterValidationDefinitionsImpl {
	private static final Logger LOGGER = Logger.getLogger(ParameterValidationDefinitionsImpl.class.getName());
	
	private List<ParameterValidationChain> parameterValidationDefinitions;
	private boolean enforcingMode = false;
	
	
	/**
	 * @return The map containing the chain name to the list of validation rules
	 */
	@XmlElementWrapper(name="ParameterValidationChains")
	@XmlElement(name="ParameterValidationDefinition")
	public List<ParameterValidationChain> getParameterValidationDefinitions() {
		return parameterValidationDefinitions;
	}
	
	/**
	 * 
	 * @param parameterValidationDefinitions The map containing the chain name to the list of validation rules
	 */
	public void setParameterValidationDefinitions(final List<ParameterValidationChain> parameterValidationDefinitions) {
		this.parameterValidationDefinitions = parameterValidationDefinitions;
		
	}
	
	/**
	 * Empty default constructor for the serialisation routines
	 */
	public ParameterValidationDefinitionsImpl() {
		
	}

	/**
	 * Adds a validation rule to the given chain
	 * @param validationChain The validation chain to add
	 */
	public void addRuleDefinition(final ParameterValidationChain validationChain) {
		checkNotNull(validationChain);
		
		if (parameterValidationDefinitions == null) {
			parameterValidationDefinitions = new ArrayList<ParameterValidationChain>();
		}
		
		parameterValidationDefinitions.add(validationChain);
	}

	/**
	 * 
	 * @return true if the filter should return an error code when an exception is thrown, and false otherwise
	 */
	@XmlElement(name="EnforcingMode")
	public boolean getEnforcingMode() {
		return enforcingMode;
	}

	/**
	 * 
	 * @param enforcingMode true if the filter should return an error code when an exception is thrown, and false otherwise
	 */
	public void setEnforcingMode(final boolean enforcingMode) {
		this.enforcingMode = enforcingMode;
	}


}
