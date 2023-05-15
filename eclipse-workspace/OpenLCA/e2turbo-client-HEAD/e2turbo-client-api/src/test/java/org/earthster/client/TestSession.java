package org.earthster.client;

import org.earthster.client.database.Database;

public class TestSession {

	private static Database db;

	public static Database getDatabase() {
		if (db == null)
			db = new Database("database");
		return db;
	}

}
