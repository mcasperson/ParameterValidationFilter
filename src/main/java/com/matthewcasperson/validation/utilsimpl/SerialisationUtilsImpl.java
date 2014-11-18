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

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static com.google.common.base.Preconditions.checkNotNull;

import com.matthewcasperson.validation.utils.SerialisationUtils;

/**
 * Implementation of the serialisation util interface using the java XML encoder/decoder
 * @author mcasperson
 *
 */
public class SerialisationUtilsImpl implements SerialisationUtils {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> String writeToXML(final T object, final Class<T> klass, final Class<?>... additionalClasses) {
		checkNotNull(object);
		checkNotNull(klass);
		
		final ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		final XMLEncoder encoder = new XMLEncoder(byteArray);
        encoder.writeObject(object);
        encoder.close();
        
        return byteArray.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T readFromXML(final String xml, final Class<T> klass, final Class<?>... additionalClasses) {
		checkNotNull(xml);
		checkNotNull(klass);
		
		final ByteArrayInputStream byteArray = new ByteArrayInputStream(xml.getBytes());
		final XMLDecoder decoder = new XMLDecoder(byteArray);
		final Object object = decoder.readObject();
		decoder.close();
		 
		if (klass.isInstance(object)) {
			return (T)object;
		} else {
			return null;
		}
		
	}

}
