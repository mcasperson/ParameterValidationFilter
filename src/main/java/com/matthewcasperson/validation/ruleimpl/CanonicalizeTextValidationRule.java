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

import java.util.logging.Logger;

import org.owasp.esapi.Encoder;
import org.owasp.esapi.reference.DefaultEncoder;

import com.matthewcasperson.validation.exception.ValidationFailedException;
import com.matthewcasperson.validation.rule.ParameterValidationRuleTemplate;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

/**
 * A validation rule to unencode a value. Sending through pre-encoded values is often a sign 
 * of an injection attack. Uses the OWASP ESAPI library.
 * @author mcasperson
 */
public class CanonicalizeTextValidationRule extends ParameterValidationRuleTemplate {
	private static final Logger LOGGER = Logger.getLogger(CanonicalizeTextValidationRule.class.getName());
	
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
		
		final String[] retValues = new String[params.length];

		for (int paramIndex = 0, paramLength = params.length; paramIndex < paramLength; ++paramIndex) {
			final String param = params[paramIndex];
			
			if (param == null) {
				retValues[paramIndex] = null;
			} else {
				final Encoder encoder = DefaultEncoder.getInstance();
				final String canonicalized = encoder.canonicalize(param, false);
				retValues[paramIndex] = canonicalized;
			}
		}
		
		return retValues;
	}
}
