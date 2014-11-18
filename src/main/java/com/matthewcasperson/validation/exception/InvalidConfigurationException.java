package com.matthewcasperson.validation.exception;

/**
 * Represents an invalid configuration
 * @author mcasperson
 *
 */
public class InvalidConfigurationException extends Exception {
	/**
	 * Generated serial number
	 */
	private static final long serialVersionUID = -1648642891475248768L;

	/**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
	public InvalidConfigurationException() {
		
	}
	
	/**
	 * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
	 * @param message the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
	 */
	public InvalidConfigurationException(final String message) {
		super(message);
	}
	
    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * java.security.PrivilegedActionException}).
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since  1.4
     */
    public InvalidConfigurationException(final Throwable cause) {
        super(cause);
    }
}
