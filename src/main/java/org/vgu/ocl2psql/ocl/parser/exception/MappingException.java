/**
 * 
 */
package org.vgu.ocl2psql.ocl.parser.exception;

/**
 * @author rherschke
 *
 */
public class MappingException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public MappingException() {
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public MappingException(String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public MappingException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public MappingException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

}
