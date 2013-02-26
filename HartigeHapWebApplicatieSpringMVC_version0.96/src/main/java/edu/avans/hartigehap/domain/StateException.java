package edu.avans.hartigehap.domain;


/**
 *
 * @author Erco
 */
public class StateException extends Exception {
	private static final long serialVersionUID = 1L;

    /**
     * Constructs an instance of <code>StateException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public StateException(String msg) {
        super(msg);
    }
}
