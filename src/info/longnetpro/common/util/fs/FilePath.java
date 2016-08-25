package info.longnetpro.common.util.fs;

import java.io.File;
import java.io.IOException;

public class FilePath {
	//private static final char SEPARATOR_CHAR = '/';
	private static final char WINDOWS_SEPARATOR_CHAR = '\\';
	private static final String SEPARATOR = "/";
	//private static final String WINDOWS_SEPARATOR = "\\";

	private String path;
	private char separatorChar;
	private boolean caseInsensitive;
	private boolean absolute;
	private boolean directory;

	private boolean debugging = false;

	public FilePath(String path) {
		super();
		this.path = path;
		reset();
	}

	public FilePath() {
		this(FilePathUtils.currentWorkingDirectory());
	}

	public static FilePath create(String path) {
		return new FilePath(path);
	}

	public static FilePath create() {
		return new FilePath();
	}

	public FilePath reset() {
		this.separatorChar = File.separatorChar;
		this.caseInsensitive = (separatorChar == WINDOWS_SEPARATOR_CHAR);
		this.debugging = false;
		return this;
	}

	public FilePath enableDebug() {
		this.debugging = true;
		return this;
	}

	public FilePath disableDebug() {
		this.debugging = false;
		return this;
	}

	protected void debug(String title, String message) {
		if (debugging) {
			System.out.println("--------------- " + title + " ---------------");
			message = message == null ? this.path : message;
			System.out.println(message);
		}
	}

	protected void debug(String title) {
		debug(title, null);
	}

	public FilePath copy(String path) {
		return FilePath.create(path).setSeparatorChar(separatorChar).setCaseInsensitive(caseInsensitive);
	}

	public FilePath copy() {
		return copy(path);
	}

	public FilePath setPath(String path) {
		this.path = path;
		return this;
	}

	public FilePath forDirectory() {
		debug("before forDirectory");
		this.path = FilePathUtils.forDirectory(path);
		debug("after forDirectory");
		return this;
	}

	public FilePath checkStatus() {
		debug("before checkStatus", path);
		String p = FilePathUtils.universalize(path, separatorChar);
		debug("after checkStatus", p);
		absolute = p.startsWith(SEPARATOR);
		directory = p.endsWith(SEPARATOR);
		return this;
	}

	public String getNormalizedPathWithPrefixMark() {
		debug("before getNormalizedPathWithPrefixMark", path);
		String p = FilePathUtils.normalize(path, true);
		debug("after getNormalizedPathWithPrefixMark", p);
		return p;
	}

	public FilePath normalize() {
		debug("before normalize");
		this.path = FilePathUtils.normalize(path);
		debug("after normalize");
		return this;
	}

	public FilePath relativize(String path) throws FilePathNotAbsoluteException {
		debug("before relativize");
		this.path = FilePathUtils.relativizePath(this.path, path, separatorChar, caseInsensitive);
		debug("after relativize");
		return this;
	}

	public FilePath resolve(String path) {
		debug("before resolve");
		this.path = FilePathUtils.resolve(this.path, path, separatorChar);
		debug("after resolve");
		return this;
	}

	public FilePath relativizeAgainst(String base) throws FilePathNotAbsoluteException {
		debug("before relativizeAgainst");
		this.path = FilePathUtils.relativizePath(base, path, separatorChar, caseInsensitive);
		debug("after relativizeAgainst");
		return this;
	}

	public FilePath resolveAgainst(String base) {
		debug("before resolveAgainst");
		this.path = FilePathUtils.resolve(base, path, separatorChar);
		debug("after resolveAgainst");
		return this;
	}

	public FilePath setSeparatorChar(char separatorChar) {
		this.separatorChar = separatorChar;
		return this;
	}

	public FilePath getRealCononicalFilePath() throws IOException {
		return FilePathUtils.getCanonicalFilePath(path);
	}

	public String getRealCanonicalPath() {
		try {
			FilePath fp = getRealCononicalFilePath();
			if (fp == null)
				return null;
			else
				return fp.normalize().getPath();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public char getSeparatorChar() {
		return separatorChar;
	}

	public FilePath setCaseInsensitive(boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
		return this;
	}

	public boolean isCaseInsensitive() {
		return caseInsensitive;
	}

	public boolean isAbsolute() {
		return absolute;
	}

	public boolean isDirectory() {
		return directory;
	}

	public String getPath() {
		debug("GetPath");
		return path;
	}
}
