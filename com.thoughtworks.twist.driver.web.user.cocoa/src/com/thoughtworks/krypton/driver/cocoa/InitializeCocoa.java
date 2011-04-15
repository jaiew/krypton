package com.thoughtworks.krypton.driver.cocoa;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InitializeCocoa {

	public static void init() {
		String tmpDir = System.getProperty("java.io.tmpdir");
		StringBuffer message = new StringBuffer();
		String library = "librococoa.dylib";
		String fileName = tmpDir + File.separator + library;
		if (load(fileName, message))
			return;

		if (extract(fileName, library, message)) {
			return;
		}

		throw new UnsatisfiedLinkError("Could not load cocoa binding library. Reasons: " + message.toString()); //$NON-NLS-1$
	}

	static boolean extract(String fileName, String mappedName, StringBuffer message) {
		FileOutputStream os = null;
		InputStream is = null;
		File file = new File(fileName);
		boolean extracted = false;
		try {
			if (!file.exists()) {
				is = InitializeCocoa.class.getResourceAsStream("/" + mappedName); //$NON-NLS-1$
				if (is != null) {
					extracted = true;
					int read;
					byte[] buffer = new byte[4096];
					os = new FileOutputStream(fileName);
					while ((read = is.read(buffer)) != -1) {
						os.write(buffer, 0, read);
					}
					os.close();
					is.close();
					chmod("755", fileName);
					if (load(fileName, message))
						return true;
				}
			}
		} catch (Throwable e) {
			try {
				if (os != null)
					os.close();
			} catch (IOException e1) {
			}
			try {
				if (is != null)
					is.close();
			} catch (IOException e1) {
			}
			if (extracted && file.exists())
				file.delete();
		}
		return false;
	}

	static void chmod(String permision, String path) {
		try {
			Runtime.getRuntime().exec(new String[] { "chmod", permision, path }).waitFor(); //$NON-NLS-1$
		} catch (Throwable e) {
		}
	}

	static boolean load(String libName, StringBuffer message) {
		try {
			if (libName.indexOf(File.separator) != -1) {
				System.setProperty("jna.library.path", new File(libName).getAbsoluteFile().getParent());
				System.load(libName);
			} else {
				System.loadLibrary(libName);
			}
			return true;
		} catch (UnsatisfiedLinkError e) {
			if (message.length() == 0)
				message.append("\n");
			message.append('\t');
			message.append(e.getMessage());
			message.append("\n");
		}
		return false;
	}
}
