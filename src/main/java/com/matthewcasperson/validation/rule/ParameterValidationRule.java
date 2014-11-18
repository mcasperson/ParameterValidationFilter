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

package com.matthewcasperson.validation.rule;

import java.util.Map;

import javax.servlet.ServletRequest;

import com.matthewcasperson.validation.exception.ValidationFailedException;

/**
 * The base interface for all rules that are applied to parameters
 * @author mcasperson
 *
 */
public interface ParameterValidationRule {
	/**
	 * Configure the rule with some custom values.
	 * @param settings A key value mapping of settings
	 */
	void configure(final Map<String, String> settings);
	
	/**
	 * Process the specified parameter. If no further processing is to be done, return false. Otherwise, return true.
	 * @param request The request that holds the parameters we will be modifying
	 * @param name The name of the parameter we are checking
	 * @return A wrapped ServletRequest if some validation was required and instructions on how to proceed
	 * @throws ValidationFailedException when the parameter is invalid and can not be made valid
	 */
	ServletRequest processParameter(final ServletRequest request, final String name) throws ValidationFailedException;

	/**
	 * Fixes and returns a parameter
	 * @param param The input parameters
	 * @return The validated parameters
	 * @throws ValidationFailedException when the parameter is invalid and can not be made valid
	 */
	String[] fixParams(final String[] params) throws ValidationFailedException;
	
	/**
	 * Fixes and returns a parameter
	 * @param param The input parameter
	 * @return The validated parameter
	 * @throws ValidationFailedException when the parameter is invalid and can not be made valid
	 */
	String fixParam(final String param) throws ValidationFailedException;
}
