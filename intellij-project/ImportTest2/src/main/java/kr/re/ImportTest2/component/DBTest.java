package kr.re.ImportTest2.component;

import org.openlca.core.database.Derby;
import org.openlca.core.database.IDatabase;

import java.io.File;

public class DBTest {

    private static final boolean USE_FILE_BASED_DB = false;

    private static IDatabase db;
    public static IDatabase getDb() {
        if (db == null) {
            db = USE_FILE_BASED_DB
                    ? initFileBasedDb()
                    : Derby.createInMemory();
        }
        return db;
    }

    private static IDatabase initFileBasedDb() {
        String tmpDirPath = System.getProperty("java.io.tmpdir");
        String dbName = "olca_test_db_1.4";
        File tmpDir = new File(tmpDirPath);
        File folder = new File(tmpDir, dbName);
//        return new My
        return new Derby(folder);
    }
}
