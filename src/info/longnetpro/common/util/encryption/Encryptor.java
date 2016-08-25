package info.longnetpro.common.util.encryption;

import java.lang.reflect.Method;

public class Encryptor {
	private String key = null;
	private String ivParameter = null;

	public Encryptor() {
		super();
	}

	public static Cipher stringToCipher(String cipherName) {
		Cipher ci = Cipher.AES;

		try {
			ci = Cipher.valueOf(cipherName.toUpperCase());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ci;
	}

	public static String cipherToString(Cipher cipher) {
		String cipherName = "AES";
		if (cipher != null) {
			cipherName = cipher.toString();
		}
		return cipherName;
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
		return encrypt(data, Cipher.AES);
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
		return decrypt(data, Cipher.AES);
	}

	public String AESEncrypt(String data) {
		String text = AES.encrypt(getKey(), data);
		return text;
	}

	public String AESDecrypt(String data) {
		String text = AES.decrypt(getKey(), data);
		return text;
	}

	public String DESEncrypt(String data) {
		String text = DES.encrypt(getKey(), getIvParameter(), data);
		return text;
	}

	public String DESDecrypt(String data) {
		String text = DES.decrypt(getKey(), getIvParameter(), data);
		return text;
	}

	public String TrippleDESEncrypt(String data) {
		String text = TrippleDES.encrypt(getKey(), getIvParameter(), data);
		return text;
	}

	public String TrippleDESDecrypt(String data) {
		String text = TrippleDES.decrypt(getKey(), getIvParameter(), data);
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
		if (key == null) {
			return CommonKey.getCommonKey();
		}
		return key;
	}

	public void setIvParameter(String ivParameter) {
		this.ivParameter = ivParameter;
	}

	public String getIvParameter() {
		if (ivParameter == null) {
			return CommonKey.getIvParameter();
		}
		return ivParameter;
	}
}
