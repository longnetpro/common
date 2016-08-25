package info.longnetpro.common.util.encryption;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class TrippleDES {
	private static final String CHARSET = "UTF-8";
	private static final int KEY_LENGTH = 8;

	private static byte[] getKeyBytes(String keyString) {
		try {
			final MessageDigest md = MessageDigest.getInstance("md5");
			final byte[] digestBytes = md.digest(Base64.decode(keyString.getBytes(CHARSET)));
			final byte[] keyBytes = Arrays.copyOf(digestBytes, 24);
			for (int j = 0, k = 16; j < 8;) {
				keyBytes[k++] = keyBytes[j++];
			}
			return keyBytes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	private static byte[] getIvString(String ivString) {
		try {
			byte[] ivBytes = Arrays.copyOf(ivString.getBytes(CHARSET), KEY_LENGTH);
			return ivBytes;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new byte[KEY_LENGTH];
	}

	public static String encrypt(String keyString, String ivString, String value) {
		String ret = "";
		try {
			byte[] keyBytes = getKeyBytes(keyString);
			byte[] ivBytes = getIvString(ivString);
			KeySpec keySpec = new DESedeKeySpec(keyBytes);
			SecretKey key = SecretKeyFactory.getInstance("DESede").generateSecret(keySpec);
			IvParameterSpec iv = new IvParameterSpec(ivBytes);

			Cipher ecipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "SunJCE");
			ecipher.init(Cipher.ENCRYPT_MODE, key, iv);

			if (value == null)
				return ret;

			// Encode the string into bytes using utf-8
			byte[] utf8 = value.getBytes(CHARSET);

			// Encrypt
			byte[] enc = ecipher.doFinal(utf8);

			// Encode bytes to base64 to get a string
			return new String(Base64.encodeToByte(enc, false), CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String decrypt(String keyString, String ivString, String value) {
		String ret = "";
		try {
			byte[] keyBytes = getKeyBytes(keyString);
			byte[] ivBytes = getIvString(ivString);
			KeySpec keySpec = new DESedeKeySpec(keyBytes);
			SecretKey key = SecretKeyFactory.getInstance("DESede").generateSecret(keySpec);
			IvParameterSpec iv = new IvParameterSpec(ivBytes);

			Cipher dcipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "SunJCE");
			dcipher.init(Cipher.DECRYPT_MODE, key, iv);

			if (value == null)
				return ret;

			// Decode base64 to get bytes
			byte[] dec = Base64.decode(value.getBytes(CHARSET));

			// Decrypt
			byte[] utf8 = dcipher.doFinal(dec);

			// Decode using utf-8
			return new String(utf8, CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}
