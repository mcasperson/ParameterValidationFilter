package com.matthewcasperson.validation.ruleimpl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.matthewcasperson.validation.exception.ValidationFailedException;
import com.matthewcasperson.validation.rule.ParameterValidationRuleTemplate;

/**
 * Strips out ant text that is matched by the supplied regexes. This is useful
 * for custom sanitisation.
 * 
 * @author mcasperson
 *
 */
public class RemoveRegexMatches extends ParameterValidationRuleTemplate {
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
	 * @param pattern
	 *            The pattern compiled from the supplied settings
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
				/*
				 * Loop over the paramater and strip out ever instance of text that 
				 * matches the regex.
				 */
				String fixedString = param;
				while (true) {
					final Matcher matcher = pattern.matcher(fixedString);
					if (!matcher.find()) {
						break;
					}
					fixedString = fixedString.replace(matcher.group(), "");
				}
				
				retValues[paramIndex] = fixedString;
			}
		}
		
		return retValues;

	}

}
