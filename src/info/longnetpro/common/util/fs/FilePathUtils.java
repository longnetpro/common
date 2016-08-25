package info.longnetpro.common.util.fs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import info.longnetpro.common.util.generic.StringUtils;

public class FilePathUtils {
	public static final char SEPARATOR_CHAR = '/';
	public static final char WINDOWS_SEPARATOR_CHAR = '\\';
	public static final String SEPARATOR = "/";
	public static final String WINDOWS_SEPARATOR = "\\";
	public static final String WINDOWS_NETSHARE_PREFIX = "\\\\";
	public static final String WINDOWS_NETSHARE_PREFIX_TRANSLATED = "//";

	public static String translateFilePath(String path) {
		if (path == null)
			return "";
		return path.replace(WINDOWS_SEPARATOR_CHAR, SEPARATOR_CHAR);
	}

	public static String toOSFilePath(String path) {
		if (path == null)
			return "";
		if (File.separatorChar == WINDOWS_SEPARATOR_CHAR)
			return path.replace(SEPARATOR_CHAR, WINDOWS_SEPARATOR_CHAR);
		else
			return path;
	}

	public static String translateToDirectory(String path) {
		String p = translateFilePath(path);
		if (!p.endsWith(SEPARATOR)) {
			p += SEPARATOR;
		}
		return path;
	}

	public static String currentWorkingDrive() {
		return currentWorkingDrive(File.separatorChar);
	}

	public static String currentWorkingDrive(char separatorChar) {
		if (separatorChar != SEPARATOR_CHAR) {
			String wd = currentWorkingDirectory();
			return wd.substring(0, 2);
		}
		return "";
	}

	public static String universalize(String path) {
		return universalize(path, File.separatorChar);
	}

	public static String universalize(String path, char separatorChar) {
		String p = translateFilePath(path);
		if (p == null || p.isEmpty() || p.equals("."))
			return "./";

		String root = "";

		boolean absolute = false;

		if (separatorChar == WINDOWS_SEPARATOR_CHAR) { // windows system
			absolute = true;
			if (p.length() >= 3 && p.startsWith(WINDOWS_NETSHARE_PREFIX_TRANSLATED)
					&& !p.startsWith(WINDOWS_NETSHARE_PREFIX_TRANSLATED + SEPARATOR)) {
				// is net share (start with // but not ///) //abc/ or //abc
				p = p.substring(2);
				int pos = p.indexOf(SEPARATOR_CHAR);
				if (pos > 0) {
					root = "|" + p.substring(0, pos);
					p = p.substring(pos);
				} else {
					if (p == null || p.isEmpty()) {
						root = currentWorkingDrive();
						p = SEPARATOR;
					} else {
						root = "|" + p;
						p = SEPARATOR;
					}
				}
			} else if (p.startsWith(SEPARATOR)) {
				// is root, start with /
				if (p.charAt(2) == ':') { // start with "/X:"
					return universalize(p.substring(1), separatorChar);
				}
				// p won't change
				root = currentWorkingDrive();
			} else if (p.length() >= 3 && p.substring(1, 3).equals(":/")) {
				// is root, start with drive name (C:/abc)
				root = p.substring(0, 2).toUpperCase();
				p = p.substring(2);
			} else if (p.length() == 2 && p.charAt(1) == ':') {
				// is root, only with drive name (C:)
				root = p.toUpperCase();
				p = SEPARATOR;
			} else
				absolute = false;
		} else if (p.startsWith(SEPARATOR)) {
			absolute = true;
		}

		if (p.endsWith("/.")) {
			p = p.substring(0, p.length() - 1);
		} else if (p.endsWith("/..")) {
			p += SEPARATOR;
		}

		if (absolute)
			p = root + SEPARATOR + p;

		p = p.replaceAll("[/]+", "/");
		p = p.replaceAll("/(?:\\./)+", "/");

		if (absolute) {
			p = SEPARATOR + p;
		}

		return p;
	}

	public static String normalize(String path) {
		return normalize(path, File.separatorChar);
	}

	public static String normalize(String path, boolean addPrefixSlash) {
		return normalize(path, File.separatorChar, addPrefixSlash);
	}

	public static String normalize(String path, char separatorChar) {
		return normalize(path, separatorChar, false);
	}

	public static String normalize(String path, char separatorChar, boolean hasPrefixMark) {
		// path should be universalized
		String p = universalize(path, separatorChar);
		if (p.equals("./"))
			return p;
		boolean absolute = p.startsWith("/");
		if (absolute) {
			p = p.substring(1);
		}
		String[] paths = p.split(SEPARATOR, -1);
		List<String> list = new ArrayList<String>();

		boolean isDir = p.endsWith(SEPARATOR);
		String root = null;
		int i = 0;
		if (absolute) {
			i = 1;
			if (separatorChar == WINDOWS_SEPARATOR_CHAR) {
				root = paths[0];
			}
		}

		for (int j = i; j < paths.length; j++) {
			String pp = paths[j];
			if (pp.equals(".") || pp.isEmpty())
				continue;
			else {
				if (list.size() == 0) {
					list.add(pp);
				} else {
					String s = list.get(list.size() - 1);
					if (!s.equals("..") && pp.equals("..")) {
						list.remove(list.size() - 1);
					} else {
						list.add(pp);
					}
				}
			}
		}

		p = StringUtils.join(list.toArray(new String[] {}), SEPARATOR);

		if (absolute) {
			while (p.startsWith("../")) {
				p = p.substring(3);
			}
			while (p.startsWith("/")) {
				p = p.substring(1);
			}
			if (root != null) {
				p = root + SEPARATOR + p;
			} else
				p = SEPARATOR + p;
		}

		if (isDir && !p.endsWith(SEPARATOR)) {
			p += SEPARATOR;
		}

		if (absolute) {
			if (p.startsWith("|")) {
				p = "//" + p.substring(1);
			}
			if (hasPrefixMark)
				p = SEPARATOR + p;
		}

		return p;
	}

	public static boolean isFilePathCaseInsensitive() {
		return File.separatorChar == WINDOWS_SEPARATOR_CHAR;
	}

	public static boolean isAbsolutePath(String path, char separatorChar) {
		String p = universalize(path, separatorChar);
		return p.startsWith(SEPARATOR);
	}

	public static boolean isAbsolutePath(String path) {
		return isAbsolutePath(path, File.separatorChar);
	}

	public static String forDirectory(String path) {
		String p = path;
		if (!p.endsWith(SEPARATOR)) {
			p += SEPARATOR;
		}
		return p;
	}

	public static String resolve(String base, String path) {
		return resolve(base, path, File.separatorChar);
	}

	public static String resolve(String base, String path, char separatorChar) {
		// base and path should be universalized.
		String bs = normalize(base, separatorChar);
		boolean baseIsdir = bs.endsWith(SEPARATOR);
		String pp = normalize(path, separatorChar, true);

		if (pp.startsWith(SEPARATOR)) {
			// the path is absolute
			return pp.substring(1);
		}

		String p = null;

		if (baseIsdir) {
			p = bs + pp;
		} else {
			int i = bs.lastIndexOf(SEPARATOR);
			if (i >= 0) {
				p = bs.substring(0, i + 1) + pp;
			} else {
				p = pp;
			}
		}

		p = normalize(p, separatorChar);

		return p;
	}

	public static String currentWorkingDirectory() {
		String cwd = System.getProperty("user.dir");
		if (cwd == null || cwd.isEmpty())
			cwd = "/";
		cwd = forDirectory(cwd);
		return cwd;
	}

	public static String relativizePath(String base, String path) throws FilePathNotAbsoluteException {
		return relativizePath(base, path, File.separatorChar);
	}

	public static String relativizePath(String base, String path, char separatorChar)
			throws FilePathNotAbsoluteException {
		boolean caseInsensitive = separatorChar == WINDOWS_SEPARATOR_CHAR;
		return relativizePath(base, path, separatorChar, caseInsensitive);
	}

	public static String relativizePath(String base, String path, char separatorChar, boolean caseInsensitive)
			throws FilePathNotAbsoluteException {
		final String format = "'%s' is not an absolute path.";
		// both base and path must be absolute path
		String bs = normalize(base, separatorChar, true);
		String ps = normalize(path, separatorChar, true);
		if (!bs.startsWith(SEPARATOR)) {
			throw new FilePathNotAbsoluteException(String.format(format, base));
		}

		if (!ps.startsWith(SEPARATOR)) {
			throw new FilePathNotAbsoluteException(String.format(format, path));
		}

		bs = bs.substring(1);
		ps = ps.substring(1);

		String[] bsf = bs.split(SEPARATOR, -1);
		String[] psf = ps.split(SEPARATOR, -1);

		int i = 0; // index of different
		while (i < bsf.length && i < psf.length) {
			String b1 = bsf[i];
			String p1 = psf[i];
			if (caseInsensitive) {
				if (!b1.equalsIgnoreCase(p1))
					break;
			} else {
				if (!b1.equals(p1))
					break;
			}
			i++;
		}

		if (i == 0) // not same from root
			return ps;

		if (i == psf.length || i == psf.length - 1) {
			if (psf.length == bsf.length) {
				// two paths are the same
				String p = psf[psf.length - 1];
				if (p.isEmpty())
					p = "./";
				return p;
			} else {
				// last
				i = psf.length - 1;
			}
		}

		List<String> list = new ArrayList<String>();
		for (int j = i; j < bsf.length - 1; j++) {
			list.add("..");
		}
		for (int j = i; j < psf.length; j++) {
			list.add(psf[j]);
		}

		String rp = StringUtils.join(list.toArray(new String[] {}), SEPARATOR);

		return rp;
	}

	public static FilePath getCanonicalFilePath(String path) throws IOException {
		File f = new File(path);

		try {
			String fn = f.getCanonicalPath();
			if (!f.exists())
				throw new FileNotFoundException();
			if (f.isDirectory())
				fn += File.separator;
			FilePath fp = new FilePath(fn);
			return fp;
		} catch (IOException e) {
			throw e;
		}
	}

	public static String validateFile(String filename) {
		String fname = filename;
		if (fname != null) {
			FilePath fp = new FilePath(fname);
			fname = fp.getRealCanonicalPath();
		}
		return fname;
	}
}
