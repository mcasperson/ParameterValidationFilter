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

package com.matthewcasperson.validation.ruleimpl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.matthewcasperson.validation.exception.ValidationFailedException;
import com.matthewcasperson.validation.rule.ParameterValidationRuleTemplate;

/**
 * A validation rule that will fail if the param does not match the supplied regex
 * @author mcasperson
 */
public class FailIfNotRegexMatchValidationRule extends ParameterValidationRuleTemplate {
	private static final Logger LOGGER = Logger.getLogger(FailIfNotRegexMatchValidationRule.class.getName());
	private static final String PATTERN_KEY_NAME = "pattern";
	private Pattern pattern;
	
	/**
	 * 
	 * @return The pattern compiled from the supplied settings
	 */
	public Pattern getPattern() {
		return pattern;
	}

	/**
	 * 
	 * @param pattern The pattern compiled from the supplied settings
	 */
	public void setPattern(final Pattern pattern) {
		this.pattern = pattern;
	}

	/**
	 * {@inheritDoc}
	 */
	public void configure(final Map<String, String> settings) {
		pattern = Pattern.compile(settings.get(PATTERN_KEY_NAME));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] fixParams(final String name, final String url, final String[] params) throws ValidationFailedException {
		checkNotNull(name);
		checkArgument(!name.trim().isEmpty());
		checkNotNull(url);
		checkArgument(!url.trim().isEmpty());
		checkNotNull(params);
		checkArgument(params.length != 0, "params should always have at least one value");
		
		checkState(pattern != null, "The pattern should not be null. Make sure the rule is assigned a valid regex with the setting key " + PATTERN_KEY_NAME + ". e.g \n" + 
				"<ParameterValidationRule>\n" +		
                    "<settings>\n" +	
                        "<entry>\n" +	
                            "<key>pattern</key>\n" +	
                            "<value>A_Valid_Regex</value>\n" +	
                        "</entry>\n" +	
                    "</settings>\n" +	
                    "<validationRuleName>" + this.getClass().getCanonicalName() + "</validationRuleName>\n" +	
                "</ParameterValidationRule>");

		for (int paramIndex = 0, paramLength = params.length; paramIndex < paramLength; ++paramIndex) {
			final String param = params[paramIndex];
			
			if (!pattern.matcher(param).find()) {
				throw new ValidationFailedException("Param did not find a match with the regex " + pattern.toString() + "\nNAME: " + name + "\nVALUE: " + param + "\nURL: " + url);
			}
		}
		
		return params;
	}
}
