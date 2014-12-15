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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.matthewcasperson.validation.exception.InvalidConfigurationException;
import com.matthewcasperson.validation.rule.ParameterValidationRule;

/**
 * This represents a validation rule to be applied as part of a chain.
 * @author mcasperson
 *
 */
@XmlRootElement
public class ParameterValidationDefinitionImpl {
	private static final Logger LOGGER = Logger.getLogger(ParameterValidationDefinitionImpl.class.getName());
	
	private String validationRuleName;
	private Map<String, String> settings;
	transient private ParameterValidationRule rule;
	
	/**
	 * 
	 * @return The custom settings to pass to the rule
	 */
	public Map<String, String> getSettings() {
		return settings;
	}

	/**
	 * 
	 * @param settings The custom settings to pass to the rule
	 */
	public void setSettings(final Map<String, String> settings) {
		this.settings = settings;
	}

	/**
	 * 
	 * @return The fully qualified name of the class that will be used to perform the validation
	 */
	@XmlElement
	public String getValidationRuleName() {
		return validationRuleName;
	}

	/**
	 * 
	 * @param validationRuleName The fully qualified name of the class that will be used to perform the validation
	 */
	public void setValidationRuleName(final String validationRuleName) {
		this.validationRuleName = validationRuleName;
	}

	/**
	 * @return An instance of the class referenced in validationRuleName (or null if the name is
	 * invalid).
	 * @throws InvalidConfigurationException If the rule class could not be constructed
	 */
	public ParameterValidationRule getRule() throws InvalidConfigurationException {
		if (rule == null) {
			try {
				final Class<?> klass = Class.forName(validationRuleName);
				final Constructor<?> ctor = klass.getConstructor();
				this.rule =  (ParameterValidationRule)ctor.newInstance();
				if (this.settings != null) {
					this.rule.configure(this.settings);
				}
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
				throw new InvalidConfigurationException(ex);
			}
		}
		
		return rule;
	}
	
	/**
	 * Empty default constructor for the serialisation routines
	 */
	public ParameterValidationDefinitionImpl() {
		
	}
}
