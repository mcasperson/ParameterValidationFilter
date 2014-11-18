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

package com.matthewcasperson.validation.utilsimpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import static com.google.common.base.Preconditions.checkNotNull;

import com.matthewcasperson.validation.utils.SerialisationUtils;

/**
 * Implementation of the serialisation util interface using JAXB
 * @author mcasperson
 *
 */
public class JaxBSerialisationUtilsImpl implements SerialisationUtils {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> String writeToXML(final T object, final Class<T> klass, final Class<?>... additionalClasses) {
		checkNotNull(object);
		checkNotNull(klass);
		
		try {
			final ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			
			final List<Class<?>> allClasses = new ArrayList<Class<?>>();
			allClasses.add(klass);
			for (final Class<?> additionalClass : additionalClasses) {
				allClasses.add(additionalClass);
			}
			
			final JAXBContext jaxbContext = JAXBContext.newInstance(allClasses.toArray(new Class[] {}));
			final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	 
			jaxbMarshaller.marshal(object, byteArray);
	        
	        return byteArray.toString();
		} catch (final Exception ex) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T readFromXML(final String xml, final Class<T> klass, final Class<?>... additionalClasses) {
		checkNotNull(xml);
		checkNotNull(klass);
		
		try { 
			final ByteArrayInputStream byteArray = new ByteArrayInputStream(xml.getBytes());
			
			final List<Class<?>> allClasses = new ArrayList<Class<?>>();
			allClasses.add(klass);
			for (final Class<?> additionalClass : additionalClasses) {
				allClasses.add(additionalClass);
			}
			
			final JAXBContext jaxbContext = JAXBContext.newInstance(allClasses.toArray(new Class<?>[] {}));
			 
			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			final Object object = jaxbUnmarshaller.unmarshal(byteArray);
			
			if (klass.isInstance(object)) {
				return (T)object;
			} else {
				return null;
			}
			
		} catch (final Exception ex) {
			return null;
		}
		
	}

}
