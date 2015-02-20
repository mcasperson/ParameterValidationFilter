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

package com.matthewcasperson.validation.test;

import java.util.HashMap;
import java.util.Map;

import com.matthewcasperson.validation.ruleimpl.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.matthewcasperson.validation.exception.ValidationFailedException;

public class ValidationRulesTests {
	@Test
	public void testTrim() {
		try {
			final TrimTextValidationRule rule = new TrimTextValidationRule();
			Assert.assertEquals("trimmed", rule.fixParam("test", "test", "  trimmed   "));
		} catch (final ValidationFailedException ex) {
			Assert.fail();
		}
	}

	@Test
	public void testEncode2() {
		try {
			final HTMLEncodeTextValidationRule rule = new HTMLEncodeTextValidationRule();
			Assert.assertEquals(
					"&lt;script&gt;doSomethningEvil();&lt;/script&gt;",
					rule.fixParam("test", "test", "<script>doSomethningEvil();</script>"));
		} catch (final ValidationFailedException ex) {
			Assert.fail();
		}
	}

	@Test
	public void testEncode3() {
		try {
			final CanonicalizeTextValidationRule rule = new CanonicalizeTextValidationRule();
			Assert.assertEquals(
					"<script>doSomethningEvil();</script>",
					rule.fixParam("test", "test", "&lt;script&gt;doSomethningEvil();&lt;/script&gt;"));
			Assert.assertEquals(
					"<script>doSomethningEvil();</script>",
					rule.fixParam("test", "test", "%26lt%3Bscript%26gt%3BdoSomethningEvil()%3B%26lt%3B%2Fscript%26gt%3B"));
		} catch (final ValidationFailedException ex) {
			Assert.fail();
		}
	}

	/**
	 * Tests that canonicalize can encoding again produces the same result.
	 */
	@Test
	public void testEncode4() {
		try {
			final String testString = "hi&nbsp;there";
			final HTMLEncodeTextValidationRule rule1 = new HTMLEncodeTextValidationRule();
			final CanonicalizeTextValidationRule rule2 = new CanonicalizeTextValidationRule();
			Assert.assertEquals(
					testString,
					rule1.fixParam("test", "test", rule2.fixParam("test", "test", testString)));
		} catch (final ValidationFailedException ex) {
			Assert.fail();
		}
	}

	@Test
	public void testEncode5() {
		try {
			//final String testString = "Please Choose…";
			final String testString = "Please Choose&hellip;";
			final HTMLEncodeTextValidationRule rule1 = new HTMLEncodeTextValidationRule();
			final CanonicalizeTextValidationRule rule2 = new CanonicalizeTextValidationRule();
			Assert.assertEquals(
					testString,
					rule1.fixParam("test", "test", rule2.fixParam("test", "test", testString)));
		} catch (final ValidationFailedException ex) {
			Assert.fail();
		}
	}

	@Test
	public void testEncode6() {
		try {
			final String testString = "&quot;test&quot;";
			final HTMLEncodeTextValidationRule rule1 = new HTMLEncodeTextValidationRule();
			final CanonicalizeTextValidationRule rule2 = new CanonicalizeTextValidationRule();
			Assert.assertEquals(
					testString,
					rule1.fixParam("test", "test", rule2.fixParam("test", "test", testString)));
		} catch (final ValidationFailedException ex) {
			Assert.fail();
		}
	}

	@Test
	public void testFailHtml() {
		final FailIfContainsHTMLValidationRule rule = new FailIfContainsHTMLValidationRule();

		try {
			rule.fixParam("test", "test", "me & you");
			Assert.fail();
		} catch (final ValidationFailedException ex) {

		}

		try {
			rule.fixParam("test", "test", "18511762");

		} catch (final ValidationFailedException ex) {
			Assert.fail();
		}

		/*
			This test doesn't seem to make it through on Bamboo for some reason.
		 */
		/*try {
			//	Although you can't see it, the spaces in this string are non-breaking.
			//	There is no reason why someone should be trying in a non-breaking space.
			rule.fixParam("test", "test", "MR David Arnold");
			Assert.fail();
		} catch (final ValidationFailedException ex) {

		}

		try {
			rule.fixParam("test", "test", "o'neil");

		} catch (final ValidationFailedException ex) {
			Assert.fail();
		}

		try {
			rule.fixParam("test", "test", "IÁNSON");
			Assert.fail();
		} catch (final ValidationFailedException ex) {

		}

		try {
			rule.fixParam("test", "test", "O’brien");
			Assert.fail();
		} catch (final ValidationFailedException ex) {

		}*/
	}

	@Test
	public void testFailHtml2() {
		final FailIfContainsHTMLValidationRule rule = new FailIfContainsHTMLValidationRule();
		final Map<String, String> config = new HashMap<String, String>();
		config.put("allowAmpersands", "true");
		rule.configure(config);

		try {
			rule.fixParam("test", "test", "me & you");

		} catch (final ValidationFailedException ex) {
			Assert.fail();
		}

	}

	@Test
	public void testFailHtml3() {
		final FailIfContainsHTMLValidationRule rule = new FailIfContainsHTMLValidationRule();
		final Map<String, String> config = new HashMap<String, String>();
		config.put("allowAccents", "true");
		rule.configure(config);

		try {
			rule.fixParam("test", "test", "Samàntha");

		} catch (final ValidationFailedException ex) {
			Assert.fail();
		}

	}

	@Test
	public void testFailEncoded1() {
		final FailIfNotCanonicalizedValidationRule rule = new FailIfNotCanonicalizedValidationRule();
		final Map<String, String> config = new HashMap<String, String>();
		config.put("allowBackSlash", "true");
		rule.configure(config);

		try {
			rule.fixParam("test", "test", "1\33");

		} catch (final ValidationFailedException ex) {
			Assert.fail();
		}

	}


	@Test
	public void testRegex1() {
		final FailIfNotRegexMatchValidationRule rule = new FailIfNotRegexMatchValidationRule();
		final Map<String, String> settings = new HashMap<String, String>();
		settings.put("pattern", "[A-Z]+");
		rule.configure(settings);
		
		try {
			rule.fixParam("test", "test", "invalid");
			Assert.fail();
		} catch (final ValidationFailedException ex) {
			
		}
		
		try {
			rule.fixParam("test", "test", "VALID");
			
		} catch (final ValidationFailedException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void testNumberOnly1() {
		final NumbersOnlyValidationRule rule = new NumbersOnlyValidationRule();
		
		try {
			Assert.assertEquals(
					"123456",
					rule.fixParam("test", "test", "a123b456c"));
		} catch (final ValidationFailedException ex) {
			
		}
	}
	
	@Test
	public void testRegexRemove() {
		final RemoveRegexMatches rule = new RemoveRegexMatches();
		final Map<String, String> settings = new HashMap<String, String>();
		settings.put("pattern", "<!--\\s*I am an invalid comment\\s*-->|<!--\\s*I am a second invalid comment\\s*-->");
		rule.configure(settings);
		
		try {
			Assert.assertEquals(
					"This should be kept",
					rule.fixParam("test", "test", "<!-- I am an invalid comment -->This should be kept<!-- I am a second invalid comment -->"));
		} catch (final ValidationFailedException ex) {
			
		}
	}

	@Test
	public void testSanitizeHTML() {
		final SanitizeHTMLValidationRule rule = new SanitizeHTMLValidationRule();

		try {
			Assert.assertEquals(
					rule.fixParam("test", "test", "<script>doEvil();</script><div>div contents</div><a href=\"http://example.org\">link</a><a onclick=\"javascript:alert('Oh Noes!!!!')\">Bad Link</a>"),
					"<div>div contents</div>linkBad Link");
		} catch (final ValidationFailedException ex) {

		}
	}

    @Test
    public void testSanitizeHTML2() {
        final SanitizeHTMLValidationRule rule = new SanitizeHTMLValidationRule();
        final Map<String, String> settings = new HashMap<String, String>();
        settings.put("allowLinks", "true");
        rule.configure(settings);

        try {
            Assert.assertEquals(
                rule.fixParam(
                    "test",
                    "test",
                    "<script>doEvil();</script><div>div contents</div><div><a href=\"http://example.org\">link</a><a onclick=\"javascript:alert('Oh Noes!!!!')\">Bad Link</a></div>"),
                "<div>div contents</div><div><a href=\"http://example.org\">link</a>Bad Link</div>");
        } catch (final ValidationFailedException ex) {

        }
    }

	@Test
	public void testNonBreakingSpaceRemovalHTML() {
		final ReplaceNonBreakingSpaceWithSpaceValidationRule rule = new ReplaceNonBreakingSpaceWithSpaceValidationRule();

		try {
			Assert.assertEquals(
					rule.fixParam("test", "test", "Hi&nbsp;there\u00A0you&#160;1&#xa0;2"),
					"Hi there you 1 2");
		} catch (final ValidationFailedException ex) {

		}
	}
}
