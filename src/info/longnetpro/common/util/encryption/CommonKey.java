package info.longnetpro.common.util.encryption;

class CommonKey {
	private static final String DOMAIN = "longnetpro.info"; // DO NOT MODIFY
	private static final String IV_PARAMETER = "ivp." + DOMAIN;

	static final String getCommonKey() {
		// final String COMMON_KEY = "8e7dc6ad760b43701f413899905415b5595ce7ab";
		return SHA1.digest(DOMAIN);
	}

	static final String getIvParameter() {
		// final String COMMON_IV_PARAMETER =
		// "588733e073ec16cf87494c40bcab2bdfd0eab819";
		return SHA1.digest(IV_PARAMETER);
	}
}
