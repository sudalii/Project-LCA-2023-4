package org.earthster.client.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

/**
 * A helper class for packaging ZIP Files.
 */
public class Zip {

	/**
	 * Extracts the given ZIP file into the same directory.
	 * 
	 * @param zipFile
	 *            a valid ZIP - file.
	 */
	public static void unzip(File zipFile) throws IOException {

		File dir = zipFile.getParentFile();

		ZipFile zip = new ZipFile(zipFile);
		Enumeration<? extends ZipEntry> entries = zip.entries();
		while (entries.hasMoreElements()) {

			ZipEntry zipEntry = entries.nextElement();
			if (zipEntry.isDirectory()) {
				// create a directory
				File _dir = new File(dir, zipEntry.getName());
				if (!_dir.exists()) {
					_dir.mkdirs();
				}

			} else {
				// extract a file
				File _file = new File(dir, zipEntry.getName());
				File _dir = _file.getParentFile();
				if (!_dir.exists()) {
					_dir.mkdirs();
				}
				InputStream _is = zip.getInputStream(zipEntry);
				OutputStream _os = new FileOutputStream(_file);
				IOUtils.copy(_is, _os);
				_os.flush();
				_os.close();
			}
		}
		zip.close();
	}

}
