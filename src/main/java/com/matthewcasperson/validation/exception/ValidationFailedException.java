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

package com.matthewcasperson.validation.exception;

/**
 * To be thrown when a parameter fails validation in a manner that should stop
 * the current request.
 * 
 * @author mcasperson
 *
 */
public class ValidationFailedException extends Exception {

	/**
	 * Generated serial number
	 */
	private static final long serialVersionUID = -5909157321818082934L;
	
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
	public ValidationFailedException() {
		
	}
	
	/**
	 * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
	 * @param message the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
	 */
	public ValidationFailedException(final String message) {
		super(message);
	}

}
