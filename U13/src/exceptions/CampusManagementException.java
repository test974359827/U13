package exceptions;

/**
 * Base class that represents campus-management-system exceptions
 * 
 * @author Lukas Roehrig
 *
 */
public abstract class CampusManagementException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new CampusManagementException with the specified error message.
	 * 
	 * @param error message
	 */
	public CampusManagementException(String message) {
		super(message);
	}

}