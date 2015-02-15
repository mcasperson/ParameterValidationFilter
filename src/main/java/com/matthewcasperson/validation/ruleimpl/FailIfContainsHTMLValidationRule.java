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

import java.text.Normalizer;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;

import com.matthewcasperson.validation.exception.ValidationFailedException;
import com.matthewcasperson.validation.rule.ParameterValidationRuleTemplate;

/**
 * A validation rule that will cause the request to fail if the parameter includes any special
 * HTML characters
 * @author mcasperson
 */
public class FailIfContainsHTMLValidationRule extends ParameterValidationRuleTemplate {
	private static final Logger LOGGER = Logger.getLogger(FailIfContainsHTMLValidationRule.class.getName());
	private static final String ALLOW_AMPERSANDS = "allowAmpersands";
	private static final String ALLOW_ACCENTS = "allowAccents";
    private static final String ALLOW_PERCENTS = "allowPercents";
	private boolean allowAmpersands = false;
	private boolean allowAccents = false;
    private boolean allowPercents = false;

	/**
	 * {@inheritDoc}
	 */
	public void configure(final Map<String, String> settings) {
		if (settings.containsKey(ALLOW_AMPERSANDS)) {
			allowAmpersands = Boolean.parseBoolean(settings.get(ALLOW_AMPERSANDS));
		}

		if (settings.containsKey(ALLOW_ACCENTS)) {
			allowAccents = Boolean.parseBoolean(settings.get(ALLOW_ACCENTS));
		}

        if (settings.containsKey(ALLOW_PERCENTS)) {
            allowPercents = Boolean.parseBoolean(settings.get(ALLOW_PERCENTS));
        }
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

		for (int paramIndex = 0, paramLength = params.length; paramIndex < paramLength; ++paramIndex) {
			String param = params[paramIndex];

			if (allowAmpersands) {
				param = param.replaceAll("&", "");
			}

			if (allowAccents) {
				param = Normalizer.normalize(param, Normalizer.Form.NFD);
			}

            if (allowPercents) {
                param = param.replaceAll("%", "");
            }
			
			if (param != null) {
				final String encoded = StringEscapeUtils.escapeHtml4(param);
				
				if (!encoded.equals(param)) {
					throw new ValidationFailedException("Parameter found to have special HTML characters.\nNAME: " + name + "\nVALUE: " + param + "\nURL: " + url);
				}
			}
		}
		
		return params;
	}
}