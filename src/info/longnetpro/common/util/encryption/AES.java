package info.longnetpro.common.util.encryption;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	private static final int KEY_SIZE = 128;
	private static final int KEY_LENGTH = KEY_SIZE / 8;
	private static final String CHARSET = "UTF-8";

	private static byte[] getKeyBytes(String keyString) {
		try {
			byte[] keyBytes = Arrays.copyOf(keyString.getBytes(CHARSET), KEY_LENGTH);
			return keyBytes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new byte[KEY_LENGTH];
	}

	public static String encrypt(String key, String data) {
		if (key == null)
			return null;
		String ret = "";
		try {
			byte[] keyBytes = getKeyBytes(key);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
			SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return new String(Base64.encodeToByte(cipher.doFinal(data.getBytes(CHARSET)), false), CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String decrypt(String key, String data) {
		if (key == null)
			return null;
		String ret = "";
		try {
			byte[] keyBytes = getKeyBytes(key);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
			final SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.decode(data)), CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}
