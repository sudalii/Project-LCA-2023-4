package kr.re.ImportTest2.db.config;

import org.junit.Test;
import org.openlca.core.DataDir;
import org.openlca.core.database.IDatabase;
import org.openlca.core.database.config.MySqlConfig;

import static org.junit.Assert.assertEquals;

public class MySqlConfigTest {

    /**
     * In order to run this test, you need a MySQL driver in the classpath and a
     * running MySQL server.
     */
    @Test
//	@Ignore
    public void testConnect() {
        var db = new MySqlConfig()
                .name("openlca")
                .host("localhost")
                .port(3306)
                .user("root")
                .password("6457")
                .connect(DataDir.get().getDatabasesDir());
//        assertEquals(IDatabase.CURRENT_VERSION, db.getVersion());
    }

}
