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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * This class represents a chain of validation operations to be applied to any
 * paramaters that match the name regex and which are sent to a page that
 * matches the URI regex.
 * 
 * @author mcasperson
 *
 */
public class ParameterValidationChain {
	private static final Logger LOGGER = Logger
			.getLogger(ParameterValidationChain.class.getName());

	private List<ParameterValidationDefinitionImpl> list = new ArrayList<ParameterValidationDefinitionImpl>();

	transient private Pattern paramNamePattern;
	transient private Pattern requestURIPattern;

	private String paramNamePatternString;
	private String requestURIPatternString;

	private boolean paramNamePatternNegated;
	private boolean requestURIPatternNegated;

	/**
	 * 
	 * @return true if the pattern defined in paramNamePatternString selects
	 *         params that do not match the pattern, and false otherwise.
	 */
	public boolean isRequestURIPatternNegated() {
		return requestURIPatternNegated;
	}

	/**
	 * 
	 * @param requestURIPatternNegated
	 *            true if the pattern defined in paramNamePatternString selects
	 *            params that do not match the pattern, and false otherwise.
	 */
	public void setRequestURIPatternNegated(
			final boolean requestURIPatternNegated) {
		this.requestURIPatternNegated = requestURIPatternNegated;
	}

	/**
	 * 
	 * @return true if the pattern defined in requestURIPatternString selects
	 *         uris that do not match the pattern, and false otherwise.
	 */
	public boolean isParamNamePatternNegated() {
		return paramNamePatternNegated;
	}

	/**
	 * 
	 * @param paramNamePatternNegated
	 *            true if the pattern defined in requestURIPatternString selects
	 *            uris that do not match the pattern, and false otherwise.
	 */
	public void setParamNamePatternNegated(final boolean paramNamePatternNegated) {
		this.paramNamePatternNegated = paramNamePatternNegated;
	}

	/**
	 * 
	 * @return The string used to create a regex pattern to match request uris
	 */
	@XmlElement
	public String getRequestURIPatternString() {
		return requestURIPatternString;
	}

	/**
	 * 
	 * @param requestURIPatternString
	 *            The string used to create a regex pattern to match request
	 *            uris
	 */
	public void setRequestURIPatternString(final String requestURIPatternString) {
		this.requestURIPatternString = requestURIPatternString;
		this.requestURIPattern = Pattern.compile(requestURIPatternString);
	}

	/**
	 * 
	 * @return The string used to create a regex pattern to match parameter
	 *         names
	 */
	@XmlElement
	public String getParamNamePatternString() {
		return paramNamePatternString;
	}

	/**
	 * 
	 * @param paramNamePatternString
	 *            The string used to create a regex pattern to match parameter
	 *            names
	 */
	public void setParamNamePatternString(final String paramNamePatternString) {
		this.paramNamePatternString = paramNamePatternString;
		this.paramNamePattern = Pattern.compile(paramNamePatternString);
	}

	/**
	 * 
	 * @return The Regex that is matched against the name of the param
	 */
	public Pattern getParamNamePattern() {
		return paramNamePattern;
	}

	/**
	 * 
	 * @return The Regex that is matched against the page being loaded
	 */
	public Pattern getRequestURIPattern() {
		return requestURIPattern;
	}

	/**
	 * 
	 * @return The list of validation rules to be applied as part of this chain
	 */
	@XmlElementWrapper(name = "ParameterValidationRuleList")
	@XmlElement(name = "ParameterValidationRule")
	public List<ParameterValidationDefinitionImpl> getList() {
		return this.list;
	}

	/**
	 * 
	 * @param list
	 *            The list of validation rules to be applied as part of this
	 *            chain
	 */
	public void setList(final List<ParameterValidationDefinitionImpl> list) {
		this.list = list;
	}
}
