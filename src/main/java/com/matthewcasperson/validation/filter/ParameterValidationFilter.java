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

package com.matthewcasperson.validation.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.matthewcasperson.validation.exception.ValidationFailedException;
import com.matthewcasperson.validation.rule.ParameterValidationRule;
import com.matthewcasperson.validation.ruledefinitionimpl.ParameterValidationChain;
import com.matthewcasperson.validation.ruledefinitionimpl.ParameterValidationDefinitionImpl;
import com.matthewcasperson.validation.ruledefinitionimpl.ParameterValidationDefinitionsImpl;
import com.matthewcasperson.validation.utils.SerialisationUtils;
import com.matthewcasperson.validation.utilsimpl.JaxBSerialisationUtilsImpl;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This filter intercepts the parameters sent by the client and cleans them up based on some
 * rules defined in a config file. This means any web application that sits behind this
 * filter can assume that the param object contains sanatised values.
 * @author mcasperson
 *
 */
public class ParameterValidationFilter implements Filter {
	private static final Logger LOGGER = Logger.getLogger(ParameterValidationFilter.class.getName());
	private static final SerialisationUtils SERIALISATION_UTILS = new JaxBSerialisationUtilsImpl();
	
	/**
	 * This is the init-param name that we expect to hold a reference to the
	 * config xml file.
	 */
	private static final String CONFIG_PARAMETER_NAME = "configFile";
	
	/**
	 * The list of validation rules that are to be applied 
	 */
	private ParameterValidationDefinitionsImpl parameterValidationDefinitions;

	@Override
	public void destroy() {
		/*
		 * Nothing to do here
		 */
	}

	/**
	 * This filter implements multiple chains of validation rules. Each chain is executed against each parameter until 
	 * alll validation rules have been executed, or until one of the validation rules stops the execution of the chain.
	 */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		
		//LOGGER.log(Level.INFO, "Parameter Validation Filter processing request");
		
		ServletRequest requestWrapper = request;
		
		try {		
			if (parameterValidationDefinitions != null && parameterValidationDefinitions.getParameterValidationDefinitions() != null) {
				
				//LOGGER.log(Level.INFO, "Parameter Validation Filter has loaded the config file");
				
				if (requestWrapper instanceof HttpServletRequest) {
					
					//LOGGER.log(Level.INFO, "Parameter Validation Filter is filtering a HttpServletRequest");
					
					final HttpServletRequest httpServletRequest = (HttpServletRequest)requestWrapper;
										
					/*
					 * Loop over each param. Note that while the validation rules may well
					 * create wrappers that return different values for the params (i.e. requestWrapper is
					 * updated to reference a new wrapper), we use this original copy for the list of 
					 * param keys to loop over.
					 */
					final Enumeration<String> iter = httpServletRequest.getParameterNames();
					while (iter.hasMoreElements()) {
						/*
						 * Get the param name and move the enumerator along
						 */
						final String paramName = iter.nextElement();
						
						//LOGGER.log(Level.INFO, "Parameter Validation Filter processing " + paramName);
						
						/*
						 * Loop over each validation rule in the chain
						 */
						final List<ParameterValidationChain> validationChains = parameterValidationDefinitions.getParameterValidationDefinitions();
						for (final ParameterValidationChain validationChain : validationChains) {													
							
							checkState(validationChain != null, "A validation rule should never be null");
							
							/*
							 * Test this validation rule against the param name
							 */
							
							final boolean paramMatches = validationChain.getParamNamePattern().matcher(paramName).find();
							final boolean uriMatches = validationChain.getRequestURIPattern().matcher(httpServletRequest.getRequestURI()).find();
							
							final boolean paramMatchesAfterNegation = ((paramMatches && !validationChain.isParamNamePatternNegated()) || (!paramMatches && validationChain.isParamNamePatternNegated()));
							final boolean uriMatchesAfterNegation = ((uriMatches && !validationChain.isRequestURIPatternNegated()) || (!uriMatches && validationChain.isRequestURIPatternNegated()));
							
							if (paramMatchesAfterNegation && uriMatchesAfterNegation) {
								
								//LOGGER.log(Level.INFO, "Parameter Validation Filter found matching chain");
								
								/*
								 * Loop over each rule in the chain 
								 */
								for (final ParameterValidationDefinitionImpl validationRule : validationChain.getList()) {
									LOGGER.log(Level.INFO, "Processing " + paramName + " with " + validationRule.getValidationRuleName());
									
									/*
									 * Get the object that will actually do the validation
									 */
									final ParameterValidationRule rule = validationRule.getRule();
									
									/*
									 * It is possible that a bad configuration will result in rule being null
									 */
									checkState(rule != null, "A validation rule should never be null. Check the class name defined in the configuration xml file.");
											
									try {
										final ServletRequest processRequest = rule.processParameter(requestWrapper, paramName);
										
										checkState(processRequest != null, "A validation rule should never return null when processing a paramemter");
										
										/*
										 * The validation rule is expected to return a valid request regardless of the
										 * processing that should or should not be done.
										 */
										requestWrapper = processRequest;
									} catch (final ValidationFailedException ex) {
										/*
										 * Log this as a warning as we are probably interested in knowing when our apps
										 * are getting hit with invalid data.
										 */
										LOGGER.log(Level.WARNING, ex.toString());
										
										/*
										 * In enforcing mode we rethrow the exception to be caught by an outer 
										 * catch block that stops processing and returns a HTTP error code.
										 */
										if (parameterValidationDefinitions.getEnforcingMode()) {
											throw ex;
										} else {
											LOGGER.log(Level.WARNING, "Enforcing mode is not enabled in PVF. Continuing to process the request.");
										}
									}																	
								}
							} else {
								/*
								 * This might be intentional, so log it as an INFO
								 */
								LOGGER.log(Level.INFO, paramName + " has not been validated.");
							}
						}
					}					
				}
			}
			
			/*
			 * Continue to the next filter
			 */
			chain.doFilter(requestWrapper, response);
		} catch (final ValidationFailedException ex) {					
			/*
			 * Stop processing and return a HTTP error code
			 */
			respondWithBadRequest(response);
		}
		catch (final Exception ex) {
			/*
			 * We probably reach this because of some invalid state due to rules returning null
			 * or throwing unchecked exceptions during their own processing. This is logged as
			 * severe as it is most likely a bug in the code.
			 */
			LOGGER.log(Level.SEVERE, ex.toString());
			LOGGER.log(Level.SEVERE, ExceptionUtils.getFullStackTrace(ex));	
			
			/*
			 * Don't allow apps to process raw parameters if this filter has failed
			 */
			respondWithBadRequest(response);
		}
	}
	
	/**
	 * Return with a status code of 400
	 * @param response The servlet request
	 */
	private void respondWithBadRequest(final ServletResponse response) {
		checkNotNull(response);
		
		/*
		 * This is thrown when one of the validation rules determined that a parameter was
		 * sent with invalid data and could not, or should not, be sanitised.
		 */
		if (response instanceof HttpServletResponse) {
			try {
				final HttpServletResponse httpServletResponse = (HttpServletResponse)response;
				httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter data");
			} catch (final IOException ex) {
				/*
				 * This shouldn't happen, but log it if it does
				 */
				LOGGER.log(Level.SEVERE, ex.toString());
				LOGGER.log(Level.SEVERE, ExceptionUtils.getFullStackTrace(ex));	
			}
		}
	}

	/**
	 * Attempts to parse the XML config file. The config file is a JaxB serialisation of a
	 * ParameterValidationDefinitionsImpl object. 
	 */
	@Override
	public void init(final FilterConfig config) throws ServletException {
		try {
			final String configFile = config.getInitParameter(CONFIG_PARAMETER_NAME);
			if (configFile != null) {			
				LOGGER.log(Level.INFO, "Attempting to unmarshall " + configFile);
				final String configXml = IOUtils.toString(config.getServletContext().getResourceAsStream(configFile));
				LOGGER.log(Level.INFO, "configXml is \n" + configXml);
				parameterValidationDefinitions = SERIALISATION_UTILS.readFromXML(configXml, ParameterValidationDefinitionsImpl.class);
			}
		} catch (final Exception ex) {
			/*
			 * This will happen if the supplied XML is invalid. Log the error
			 */
			LOGGER.log(Level.SEVERE, ex.toString());
			LOGGER.log(Level.SEVERE, ExceptionUtils.getFullStackTrace(ex));
			
			/*
			 * Rethrow as we don't want to proceed with invalid configuration
			 */
			throw new ServletException(ex);
		}			
		
	}

}
