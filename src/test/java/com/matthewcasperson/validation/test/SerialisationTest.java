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

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.matthewcasperson.validation.exception.InvalidConfigurationException;
import com.matthewcasperson.validation.ruledefinitionimpl.ParameterValidationChain;
import com.matthewcasperson.validation.ruledefinitionimpl.ParameterValidationDefinitionImpl;
import com.matthewcasperson.validation.ruledefinitionimpl.ParameterValidationDefinitionsImpl;
import com.matthewcasperson.validation.ruleimpl.FailIfNotRegexMatchValidationRule;
import com.matthewcasperson.validation.utils.SerialisationUtils;
import com.matthewcasperson.validation.utilsimpl.JaxBSerialisationUtilsImpl;
import com.matthewcasperson.validation.utilsimpl.SerialisationUtilsImpl;

/**
 * Tests of the serialisation methods
 * @author mcasperson
 *
 */
public class SerialisationTest {
	private static final SerialisationUtils SERIALISATION_UTILS = new SerialisationUtilsImpl();
	private static final SerialisationUtils JAXB_SERIALISATION_UTILS = new JaxBSerialisationUtilsImpl();
	
	@Test
	public void testSerialisation() {		
		final Foo foo = new Foo("bar");
		final String xml = SERIALISATION_UTILS.writeToXML(foo, Foo.class);
		final Foo foo2 = SERIALISATION_UTILS.readFromXML(xml, Foo.class);
		Assert.assertEquals(foo.getBar(), foo2.getBar());
	}
	
	@Test
	public void testSerialisation2() {		
		final Foo foo = new Foo("bar");
		final String xml = SERIALISATION_UTILS.writeToXML(foo, Foo.class);
		final String foo2 = SERIALISATION_UTILS.readFromXML(xml, String.class);
		Assert.assertNull(foo2);		
	}
	
	@Test
	public void testSerialisation3() {		
		final ParameterValidationDefinitionImpl validationDef = new ParameterValidationDefinitionImpl();
		validationDef.setValidationRuleName("com.matthewcasperson.validation.ruleimpl.TrimTextValidationRule");
		
		final ParameterValidationChain chain = new ParameterValidationChain();
		chain.setParamNamePatternString("hithere");
		chain.setRequestURIPatternString("^/something/else$");
		
		chain.getList().add(validationDef);
		
		final ParameterValidationDefinitionsImpl conatiner = new ParameterValidationDefinitionsImpl();
		conatiner.addRuleDefinition(chain);
		
		final String xml = SERIALISATION_UTILS.writeToXML(conatiner, ParameterValidationDefinitionsImpl.class);
		
		/*
		 * This is the xml that will be placed in the config files
		 */
		System.out.println(xml);
		
		final ParameterValidationDefinitionsImpl conatiner2 = SERIALISATION_UTILS.readFromXML(xml, ParameterValidationDefinitionsImpl.class);
		
		Assert.assertEquals(conatiner.getParameterValidationDefinitions().get(0).getParamNamePattern().pattern(),
				conatiner2.getParameterValidationDefinitions().get(0).getParamNamePattern().pattern());
		
		try {
			Assert.assertEquals(conatiner.getParameterValidationDefinitions().get(0).getList().get(0).getRule().getClass().getName(),
					conatiner2.getParameterValidationDefinitions().get(0).getList().get(0).getRule().getClass().getName());
			} catch (final InvalidConfigurationException ex) {
				Assert.fail();
			}
	}
	
	@Test
	public void testSerialisationJaxb() {		
		final Foo foo = new Foo("bar");
		final String xml = JAXB_SERIALISATION_UTILS.writeToXML(foo, Foo.class);
		final Foo foo2 = JAXB_SERIALISATION_UTILS.readFromXML(xml, Foo.class);
		Assert.assertEquals(foo.getBar(), foo2.getBar());
	}
	
	@Test
	public void testSerialisationJaxb2() {		
		final Foo foo = new Foo("bar");
		final String xml = JAXB_SERIALISATION_UTILS.writeToXML(foo, Foo.class);
		final String foo2 = JAXB_SERIALISATION_UTILS.readFromXML(xml, String.class);
		Assert.assertNull(foo2);		
	}
	
	@Test
	public void testSerialisationJaxb3() {		
		final Map<String, String> settings = new HashMap<String, String>();
		settings.put("settingKey", "settingValue");
		
		final ParameterValidationDefinitionImpl validationDef = new ParameterValidationDefinitionImpl();
		validationDef.setValidationRuleName("com.matthewcasperson.validation.ruleimpl.TrimTextValidationRule");
		validationDef.setSettings(settings);
		
		final ParameterValidationChain chain = new ParameterValidationChain();
		chain.setParamNamePatternString("hithere");
		chain.setRequestURIPatternString("^/something/else$");
		
		chain.getList().add(validationDef);
		
		final ParameterValidationDefinitionsImpl conatiner = new ParameterValidationDefinitionsImpl();
		conatiner.addRuleDefinition(chain);
		
		final String xml = JAXB_SERIALISATION_UTILS.writeToXML(conatiner, ParameterValidationDefinitionsImpl.class, ParameterValidationDefinitionImpl.class);
		
		/*
		 * This is the xml that will be placed in the config files
		 */
		System.out.println(xml);
		
		final ParameterValidationDefinitionsImpl conatiner2 = JAXB_SERIALISATION_UTILS.readFromXML(xml, ParameterValidationDefinitionsImpl.class, ParameterValidationDefinitionImpl.class);
		
		Assert.assertEquals(conatiner.getParameterValidationDefinitions().get(0).getParamNamePattern().pattern(),
				conatiner2.getParameterValidationDefinitions().get(0).getParamNamePattern().pattern());
		
		try {
		Assert.assertEquals(conatiner.getParameterValidationDefinitions().get(0).getList().get(0).getRule().getClass().getName(),
				conatiner2.getParameterValidationDefinitions().get(0).getList().get(0).getRule().getClass().getName());
		} catch (final InvalidConfigurationException ex) {
			Assert.fail();
		}
	}
	
	/*
	 * Test passing settings through to a rule
	 */
	@Test
	public void testSerialisationJaxb4() {		
		final Map<String, String> settings = new HashMap<String, String>();
		settings.put("pattern", "[A-Z]*");
		
		final ParameterValidationDefinitionImpl validationDef = new ParameterValidationDefinitionImpl();
		validationDef.setValidationRuleName("com.matthewcasperson.validation.ruleimpl.FailIfNotRegexMatchValidationRule");
		validationDef.setSettings(settings);
		
		final ParameterValidationChain chain = new ParameterValidationChain();
		chain.setParamNamePatternString("hithere");
		chain.setRequestURIPatternString("^/something/else$");
		
		chain.getList().add(validationDef);
		
		final ParameterValidationDefinitionsImpl conatiner = new ParameterValidationDefinitionsImpl();
		conatiner.addRuleDefinition(chain);
		
		final String xml = JAXB_SERIALISATION_UTILS.writeToXML(conatiner, ParameterValidationDefinitionsImpl.class, ParameterValidationDefinitionImpl.class);
		
		/*
		 * This is the xml that will be placed in the config files
		 */
		System.out.println(xml);
		
		final ParameterValidationDefinitionsImpl conatiner2 = JAXB_SERIALISATION_UTILS.readFromXML(xml, ParameterValidationDefinitionsImpl.class, ParameterValidationDefinitionImpl.class);
		
		try {
		Assert.assertEquals(((FailIfNotRegexMatchValidationRule)conatiner.getParameterValidationDefinitions().get(0).getList().get(0).getRule()).getPattern().toString(),
				((FailIfNotRegexMatchValidationRule)conatiner2.getParameterValidationDefinitions().get(0).getList().get(0).getRule()).getPattern().toString());
		} catch (final InvalidConfigurationException ex) {
			Assert.fail();
		}
	}
}

