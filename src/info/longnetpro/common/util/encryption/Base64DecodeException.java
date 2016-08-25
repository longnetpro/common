package info.longnetpro.common.util.encryption;

public class Base64DecodeException extends Exception {
	private static final long serialVersionUID = -2181152224881905579L;

	public Base64DecodeException(Throwable throwable) {
		super(throwable);
	}

	public Base64DecodeException(String string, Throwable throwable) {
		super(string, throwable);
	}

	public Base64DecodeException(String string) {
		super(string);
	}

	public Base64DecodeException() {
		super();
	}
}
