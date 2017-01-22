package se.joeladonai.userapi.exception;

public class RepositoryException extends Exception {

	private static final long serialVersionUID = 6613396450428733377L;

	public RepositoryException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public RepositoryException(String message) {
		super(message);
	}

}
