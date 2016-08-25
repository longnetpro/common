package info.longnetpro.common.util.encryption;

public class CommonKey {
	private static final String DOMAIN = "longnetpro.info"; // DO NOT MODIFY

	public static final String getCommonKey() {
		// final String COMMON_KEY = "8e7dc6ad760b43701f413899905415b5595ce7ab";
		return SHA1.digest(DOMAIN);
	}
}
