package com.matthewcasperson.validation.ruleimpl;

import com.matthewcasperson.validation.exception.ValidationFailedException;
import com.matthewcasperson.validation.rule.ParameterValidationRuleTemplate;
import org.owasp.html.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Sanitizes html using the OWASP HTML sanitizer with some common defaults
 */
public class SanitizeHTMLValidationRule extends ParameterValidationRuleTemplate {
    private static final Logger LOGGER = Logger.getLogger(SanitizeHTMLValidationRule.class.getName());

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
                final StringBuilder sb = new StringBuilder();
                final HtmlStreamRenderer renderer = HtmlStreamRenderer.create(
                        sb,
                        new Handler<String>() {
                            public void handle(final String errorMessage) {
                                LOGGER.log(Level.INFO, errorMessage);
                            }
                        });

                final HtmlSanitizer.Policy policy = new HtmlPolicyBuilder()
                        .allowCommonBlockElements()
                        .allowCommonInlineFormattingElements()
                        .build(renderer);

                HtmlSanitizer.sanitize(param, policy);

                retValues[paramIndex] = sb.toString();
            }
        }

        return retValues;
    }
}
