package info.longnetpro.common.util.system;

import java.net.URISyntaxException;

import info.longnetpro.common.util.fs.FilePathUtils;

public class SystemUtils {
	public static String getClassPath(Class<?> clazz) throws URISyntaxException {
		String path = clazz.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		path = FilePathUtils.normalize(path);
		return path;
	}
}
