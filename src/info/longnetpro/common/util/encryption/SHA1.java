package info.longnetpro.common.util.encryption;

import java.security.MessageDigest;

public class SHA1 {
	private static String CHARSET = "UTF-8";
	private static int SIZE = 40;

	private static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	public static String digest(String text) {
		MessageDigest md;
		byte[] sha1hash = new byte[SIZE];
		String ret = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.update(text.getBytes(CHARSET), 0, text.length());
			sha1hash = md.digest();
			ret = convertToHex(sha1hash);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}
