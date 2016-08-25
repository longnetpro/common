package info.longnetpro.common.util.fs;

public class FilePathNotAbsoluteException extends Exception {
	private static final long serialVersionUID = 1L;

	public FilePathNotAbsoluteException(Throwable cause) {
		super(cause);
	}

	public FilePathNotAbsoluteException(String message, Throwable cause) {
		super(message, cause);
	}

	public FilePathNotAbsoluteException(String message) {
		super(message);
	}

	public FilePathNotAbsoluteException() {
		super();
	}
}
