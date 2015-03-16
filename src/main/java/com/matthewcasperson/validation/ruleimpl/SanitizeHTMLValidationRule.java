package com.matthewcasperson.validation.ruleimpl;

import com.matthewcasperson.validation.exception.ValidationFailedException;
import com.matthewcasperson.validation.rule.ParameterValidationRuleTemplate;
import org.owasp.html.*;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Sanitizes html using the OWASP HTML sanitizer with some common defaults
 */
public class SanitizeHTMLValidationRule extends ParameterValidationRuleTemplate {
    private static final Logger LOGGER = Logger.getLogger(SanitizeHTMLValidationRule.class.getName());

    private static final String A_ELEMENT = "a";
    private static final String HREF_ATTR = "href";
    private static final String[] URL_PROTOCOLS = new String[] {"https", "http"};

    private static final String ALLOW_LINKS = "allowLinks";


    private boolean allowLinks = false;

    public void configure(final Map<String, String> settings) {
        if (settings.containsKey(ALLOW_LINKS)) {
            allowLinks = Boolean.parseBoolean(settings.get(ALLOW_LINKS));
        }
    }

    @Override
    public String[] fixParams(final String name, final String url, final String[] params) throws ValidationFailedException {
        checkNotNull(name);
        checkArgument(!name.trim().isEmpty());
        checkNotNull(url);
        checkArgument(!url.trim().isEmpty());
        checkNotNull(params);
        checkArgument(params.length != 0, "PVF-BUG-0003: params should always have at least one value");

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

                HtmlPolicyBuilder policyBuilder = new HtmlPolicyBuilder()
                        .allowCommonBlockElements()
                        .allowCommonInlineFormattingElements();

                if (allowLinks) {
                    policyBuilder = policyBuilder
                        .allowElements(A_ELEMENT)
                        .allowAttributes(HREF_ATTR).onElements(A_ELEMENT)
                        .allowUrlProtocols(URL_PROTOCOLS);

                }

                final HtmlSanitizer.Policy policy =  policyBuilder.build(renderer);

                HtmlSanitizer.sanitize(param, policy);

                retValues[paramIndex] = sb.toString();
            }
        }

        return retValues;
    }
}
