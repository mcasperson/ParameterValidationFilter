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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.matthewcasperson.validation.exception.ValidationFailedException;
import com.matthewcasperson.validation.utils.RequestParameterUtils;
import com.matthewcasperson.validation.utilsimpl.RequestParameterUtilsImpl;

/**
 * A template that holds the logic for validating parameters, deferring the actual
 * processing to derived classes.
 */
abstract public class ParameterValidationRuleTemplate implements ParameterValidationRule {
	private static final Logger LOGGER = Logger.getLogger(ParameterValidationRuleTemplate.class.getName());
	private static final RequestParameterUtils REQUEST_PARAMETER_UTILS = new RequestParameterUtilsImpl();
	
	/**
	 * This method will attempt to process the supplied parameter, and either stop all processing by throwning an
	 * exception, or continue with other rules.
	 * @throws ValidationFailedException if the validation failed
	 */
	@Override
	public ServletRequest processParameter(final ServletRequest request, final String name) throws ValidationFailedException {
		checkNotNull(request);
		checkNotNull(name);
		checkArgument(!name.trim().isEmpty());
		
		if (request instanceof HttpServletRequest) {			
			final HttpServletRequest httpServletRequest = (HttpServletRequest)request;	
			
			/*
			 * Get the param
			 */
			final String[] params = REQUEST_PARAMETER_UTILS.getParams(httpServletRequest, name);
			
			/*
			 * Defer to a child class for the actual logic that processes the parameter
			 */
			final String[] processedParams = fixParams(name, ((HttpServletRequest) request).getRequestURL().toString(), params);
			
			checkState(processedParams.length == params.length, "PVF-BUG-0001: fixParams should always return the same number of parameters as it was passed");
							
			/*
			 * Did it make any difference?
			 */
			if (!Arrays.equals(processedParams, params)) {
				/*
				 * If so, wrap up the request with a new version that will return the trimmed version of the param
				 */
				final HttpServletRequestWrapper newRequest = new HttpServletRequestWrapper(httpServletRequest) {

					/**
					 * Override the getParameterValues returning our new array of parameters.
					 */
					public String[] getParameterValues(final String newName) {
						if (name.equals(newName)) {
							return processedParams;
						}

						return super.getParameterValues(newName);
					}

                    /**
                     * Override the getParameter which returns the first value in the array
                     */
                    public String getParameter(final String newName) {
                        if (name.equals(newName)) {
                            if (processedParams == null || processedParams.length == 0) {
                                return null;
                            }

                            return processedParams[0];
                        }

                        return super.getParameter(newName);
                    }
				};
				
				/*
				 * Return the wrapped request and forward the processing instruction from
				 * the validation rule
				 */
				return newRequest;									
			}
			
		}
			
		/*
		 * The default action to take if we have not otherwise continued to process the chain
		 */
		return request;
	}
	
	/**
	 * Defer to the fixParams method;
	 */
	public String fixParam(final String name, final String url, final String param) throws ValidationFailedException {
		checkNotNull(name);
		checkArgument(!name.trim().isEmpty());
		checkNotNull(url);
		checkArgument(!url.trim().isEmpty());
		
		final String[] retValue = this.fixParams(name, url, new String[] {param});
		checkState(retValue != null, "PVF-BUG-0002: fixParams should never return null");
		checkState(retValue.length == 1, "PVF-BUG-0001: fixParams should always return the same number of parameters as it is given");
		return retValue[0];
	}
	
	/**
	 * A default implementation that does nothing
	 */
	public void configure(final Map<String, String> settings) {
		
	}
}
