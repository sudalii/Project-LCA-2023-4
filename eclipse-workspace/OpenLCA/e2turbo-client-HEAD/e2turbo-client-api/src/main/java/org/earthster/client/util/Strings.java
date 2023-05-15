package org.earthster.client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Strings {

	/**
	 * Cut a string to the given length. Appends "..." if the string was
	 * truncated.
	 */
	public static String cut(String string, int length) {

		if (string == null || length <= 0)
			return "";

		String str = string.trim();
		if (str.length() <= length)
			return str;

		switch (length) {
		case 1:
			return ".";
		case 2:
			return "..";
		default:
			return str.substring(0, length - 3).concat("...");
		}
	}

	public static String[] readLines(InputStream is) throws IOException {
		if (is == null)
			return new String[0];

		List<String> list = new ArrayList<String>();
		InputStreamReader reader = new InputStreamReader(is);
		BufferedReader buffer = new BufferedReader(reader);
		String line = null;
		while ((line = buffer.readLine()) != null) {
			list.add(line);
		}
		buffer.close();
		return list.toArray(new String[list.size()]);
	}

}
