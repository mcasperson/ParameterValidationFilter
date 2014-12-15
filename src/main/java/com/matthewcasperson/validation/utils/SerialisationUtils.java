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

package com.matthewcasperson.validation.utils;

/**
 * Util methods for xml serialisation
 * @author mcasperson
 *
 */
public interface SerialisationUtils {
	/**
	 * 
	 * @param object The object to be serialised
	 * @param klass The type of object to return
	 * @param additionalClasses A list of additional classes that might be required to deserialize the object
	 * @param <T> The type of object we are serialising
	 * @return The XML representation of the object
	 */
	<T> String writeToXML(final T object, final Class<T> klass, final Class<?>... additionalClasses);
	/**
	 * 
	 * @param xml The XML representation of the object
	 * @param klass The type of object to return
	 * @param additionalClasses A list of additional classes that might be required to deserialize the object
	 * @param <T> The type of object we are serialising
	 * @return The Java object constructed from the XML
	 */
	<T> T readFromXML(final String xml, final Class<T> klass, final Class<?>... additionalClasses);
}
