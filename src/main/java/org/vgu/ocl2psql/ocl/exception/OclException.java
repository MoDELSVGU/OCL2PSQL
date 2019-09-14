/**
 * 
 */
package org.vgu.ocl2psql.ocl.exception;

/**
 * @author rherschke
 *
 */
public class OclException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public OclException() {
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public OclException(String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public OclException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public OclException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

}
