package kr.re.ImportTest2.component.derdyDb;

import org.openlca.core.DataDir;
import org.openlca.core.database.config.DatabaseConfig;
import org.openlca.core.database.config.DerbyConfig;
import org.openlca.core.database.config.MySqlConfig;
import org.openlca.core.database.upgrades.Upgrades;
import org.openlca.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

@Component
public class DatabaseWizard {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
//    private String dbName;

//    @Override

    /**
     * @param dbConfig true: Derby, false: MySQL
     * @param dbName
     * @return
     */
    public boolean runDatabase(boolean dbConfig, String dbName) {
        try {
            // Derby
            if (dbConfig) {
                var config = new DerbyConfig();
                config.name(dbName);
                var runner = new Runner(config);
                runner.createDatabase();
            }
            // MySQL
            else {
                var config = new MySqlConfig();
                var dbDir = getDbDir(dbName);   // folder "../databases" return
                System.out.println("dbDir: "
                        + dbDir.getAbsolutePath());

                if (dbDir != null) {
                    config.name(dbDir.getName());
                }
                var runner = new Runner(config);
                runner.createDatabase();
            }
            return true;
        } catch (Exception e) {
            log.error("Database creation failed", e);
            return false;
        }
    }
    private DerbyConfig initDatabase(String name) {
        var dbDir = getDbDir(name);
        if (dbDir == null)
            return null;
        var config = new DerbyConfig();
        config.name(dbDir.getName());
//        DbTemplate.EMPTY.extract(dbDir);
        var db = Database.activate(config);
        if (db == null)
            return null;
        Upgrades.on(db);
        Database.register((DerbyConfig) config);
        return config;
    }

    private File getDbDir(String name) {
        if (Strings.nullOrEmpty(name))
            return null;
        return new File(DataDir.get().getDatabasesDir(), name);
    }

/*    private MySQL openDatabase() {

    }*/

    private static class Runner {

        private final Logger log = LoggerFactory.getLogger(this.getClass());
        private final DatabaseConfig config;

        Runner(DatabaseConfig config) {
            this.config = config;
        }

        public void createDatabase()
                throws InvocationTargetException, InterruptedException {
            try {
                if (!Database.isActive(config)) {
                    createIt();
                }
            } catch (Exception e) {
                log.error("failed to create database", e);
            }
        }

        private void createIt() {
            var db = Database.activate(config);
            if (db == null) {
                return;
            }

            if (config instanceof MySqlConfig) {
                Database.register((MySqlConfig) config);
                return;
            }
            if (config instanceof DerbyConfig) {
                // upgrades if required
//                Upgrades.on(db);
                Database.register((DerbyConfig) config);
            }
/*            var dir = new File(DataDir.get().getDatabasesDir(), config.name());
            if (dir.exists()) {
                log.error("Failed to create database: folder "
                        + dir + " already exists");
                return;
            }*/
        }
    }
}
