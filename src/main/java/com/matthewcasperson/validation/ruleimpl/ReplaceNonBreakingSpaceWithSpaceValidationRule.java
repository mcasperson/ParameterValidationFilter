package com.matthewcasperson.validation.ruleimpl;

import com.matthewcasperson.validation.exception.ValidationFailedException;
import com.matthewcasperson.validation.rule.ParameterValidationRuleTemplate;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Replaces any non-breaking spaces
 * 
 * @author mcasperson
 *
 */
public class ReplaceNonBreakingSpaceWithSpaceValidationRule extends ParameterValidationRuleTemplate {
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
				retValues[paramIndex] = param
						.replaceAll("&nbsp;", " ")
						.replaceAll("&#160;", " ")
						.replaceAll("&#xa0;", " ")
						.replaceAll("\u00A0", " ");
			}
		}
		
		return retValues;

	}

}
