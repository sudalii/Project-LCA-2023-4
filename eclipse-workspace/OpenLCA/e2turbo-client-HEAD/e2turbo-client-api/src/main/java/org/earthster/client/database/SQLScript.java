package org.earthster.client.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A helper class for reading SQL statements from a script.
 * 
 * @author ms
 * 
 */
class SQLScript implements Iterator<String> {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private final BufferedReader reader;
	private String next = null;

	public SQLScript(InputStream stream) {
		InputStreamReader streamReader = new InputStreamReader(stream);
		reader = new BufferedReader(streamReader);
	}

	public SQLScript(Reader reader) {
		this.reader = new BufferedReader(reader);
	}

	@Override
	public boolean hasNext() {
		next = null;
		try {
			pointNext();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return next != null;
	}

	@Override
	public String next() {
		String _next = next;
		next = null;
		return _next;
	}

	@Override
	public void remove() {
		next = null;
	}

	private void pointNext() throws IOException {
		final StringBuffer statementBuffer = new StringBuffer();
		String line = null;
		boolean statementReady = false;

		while (!statementReady && (line = reader.readLine()) != null) {

			line = line.trim();
			if (line.startsWith("--") || line.length() == 0) {
				// line comment or empty
				continue;
			}

			if (line.endsWith(";")) {
				// statement line with semicolon
				statementBuffer.append(' ');
				statementBuffer.append(line.substring(0, line.length() - 1));
				statementReady = true;
			} else {
				// statement line without semicolon
				statementBuffer.append(' ');
				statementBuffer.append(line);
				statementReady = false;
			}

		}// while

		if (statementReady) {
			next = statementBuffer.toString();
		}
	}

	public void close() throws IOException {
		reader.close();
	}

	public static void main(String[] args) {
		SQLScript script = new SQLScript(
				SQLScript.class.getResourceAsStream("init.sql"));
		while (script.hasNext()) {
			System.out.println(script.next());
		}
	}

}
