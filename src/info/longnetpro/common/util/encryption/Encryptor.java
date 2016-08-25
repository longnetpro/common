package info.longnetpro.common.util.encryption;

import java.lang.reflect.Method;

public class Encryptor {
	public enum Cipher {
		AES, TRIPPLEDES, DES;
		private static final long serialVersionUID = 1L;
	}

	private String key = null;
	private String ivParameter = null;

	public Encryptor() {
		super();
	}

	public static Cipher stringToCipher(String cipher) {
		Cipher ci = Encryptor.Cipher.AES;
		if (cipher == null || cipher.equalsIgnoreCase("AES"))
			return ci;
		if (cipher.equalsIgnoreCase("3DES"))
			return Encryptor.Cipher.TRIPPLEDES;
		if (cipher.equalsIgnoreCase("DES"))
			return Encryptor.Cipher.DES;
		return ci;
	}

	public static String cipherToString(Cipher cipher) {
		String str = null;
		switch (cipher) {
		case TRIPPLEDES:
			str = "3DES";
			break;
		case DES:
			str = "DES";
			break;
		case AES:
		default:
			str = "AES";
			break;
		}
		return str;
	}

	public static Encryptor getEncryptor() {
		return null;
	}

	public static Encryptor newEncryptorFromClassName(String className) {
		Class<?> cls = null;

		if (className == null) {
			return null;
		}

		try {
			cls = Class.forName(className);
			Method m = cls.getDeclaredMethod("getEncryptor");
			Encryptor encryptor = (Encryptor) m.invoke(null);
			return encryptor;
			/*
			 * Constructor<?> ctor = cls.getConstructor(); return
			 * (Encryptor)ctor.newInstance();
			 */
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String encrypt(String data, Cipher cipher) {
		String str = null;
		switch (cipher) {
		case TRIPPLEDES:
			str = TrippleDESEncrypt(data);
			break;
		case DES:
			str = DESEncrypt(data);
			break;
		case AES:
		default:
			str = AESEncrypt(data);
			break;
		}
		return str;
	}

	public String encrypt(String data, String cipher) {
		Cipher ci = stringToCipher(cipher);
		return encrypt(data, ci);
	}

	public String encrypt(String data) {
		return encrypt(data, Encryptor.Cipher.AES);
	}

	public String decrypt(String data, Cipher cipher) {
		String str = null;
		switch (cipher) {
		case TRIPPLEDES:
			str = TrippleDESDecrypt(data);
			break;
		case DES:
			str = DESDecrypt(data);
			break;
		case AES:
		default:
			str = AESDecrypt(data);
			break;
		}
		return str;
	}

	public String decrypt(String data, String cipher) {
		Cipher ci = stringToCipher(cipher);
		return decrypt(data, ci);
	}

	public String decrypt(String data) {
		return decrypt(data, Encryptor.Cipher.AES);
	}

	public String AESEncrypt(String data) {
		String text = AES.encrypt(key, data);
		return text;
	}

	public String AESDecrypt(String data) {
		String text = AES.decrypt(key, data);
		return text;
	}

	public String DESEncrypt(String data) {
		String text = DES.encrypt(key, ivParameter, data);
		return text;
	}

	public String DESDecrypt(String data) {
		String text = DES.decrypt(key, ivParameter, data);
		return text;
	}

	public String TrippleDESEncrypt(String data) {
		String text = TrippleDES.encrypt(key, ivParameter, data);
		return text;
	}

	public String TrippleDESDecrypt(String data) {
		String text = TrippleDES.decrypt(key, ivParameter, data);
		return text;
	}

	public String SHA1Digest(String data) {
		String text = SHA1.digest(data);
		return text;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setIvParameter(String ivParameter) {
		this.ivParameter = ivParameter;
	}

	public String getIvParameter() {
		return ivParameter;
	}
}
