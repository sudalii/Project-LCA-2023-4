package kr.re.ImportTest2;

import kr.re.ImportTest2.component.DBTest;
import kr.re.ImportTest2.component.derdyDb.Database;
import kr.re.ImportTest2.component.derdyDb.DatabaseWizard;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.openlca.core.database.ActorDao;
import org.openlca.core.database.Derby;
//import org.openlca.io.xls.process.*;
import org.openlca.core.database.IDatabase;
import org.openlca.core.database.config.DatabaseConfigList;
import org.openlca.core.model.Actor;
import org.openlca.util.Dirs;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertTrue;


public class MyDbTest {

    @Test
    public void testFromFolder() throws Exception {
        String tempName = "olca_test_db";
        File dir = Files.createTempDirectory(tempName).toFile();
        DatabaseConfigList databaseConfigList = new DatabaseConfigList();
//        List<DerbyConfig> derbyConfigs = databaseConfigList.getDerbyConfigs();
        if (!databaseConfigList.nameExists(tempName)) {
            databaseConfigList.write(dir);
        }
        Assertions.assertTrue(dir.delete());
        try (Derby db = new Derby(dir)) {
            Assertions.assertEquals(dir.getName(), db.getName());
        }
        Dirs.delete(dir.getAbsolutePath());
    }

    @Test
    public void testNoMemLeak() throws Exception {
        Runtime rt = Runtime.getRuntime();
        long initialUsed = rt.totalMemory() - rt.freeMemory();
        for (int i = 0; i < 1000; i++) {
            Derby db = Derby.createInMemory();
            db.close();
            long usedMem = rt.totalMemory() - rt.freeMemory();
            if (initialUsed * 10 < usedMem) {
                Assertions.fail("There is probably a memory leak");
            }
            System.out.println("" + i + "\t" + (usedMem / (1024 * 1024)));
        }
    }

    @Test
    public void testDumpMemoryDB() throws Exception {
        Derby db = Derby.createInMemory();
        Actor a = new Actor();
        a.name = "The Donald";
        a = new ActorDao(db).insert(a);
        long id = a.id;
        Path path = Files.createTempDirectory("_olca_test_");
        db.dump(path.toString());
        db.close();
        db = Derby.restoreInMemory(path.toString());
        a = new ActorDao(db).getForId(id);
        Assertions.assertEquals("The Donald", a.name);
        db.close();
        Dirs.delete(path);
    }

    @Test
    public void testConnectDB() throws Exception {
//        System.out.println(NativeLib.isLibraryDir(DataDir.get().root()));
//        NativeLib.loadFrom(DataDir.get().root());

        String dbName = "openlca"; //db_231031
//        var db = DataDir.get().openDatabase("dbName");

        DatabaseWizard databaseWizard = new DatabaseWizard();
        databaseWizard.runDatabase(false, dbName);

        IDatabase db = Database.get();

/*        ProductSystem system = db.get(ProductSystem.class,
                "7d1cbce0-b5b3-47ba-95b5-014ab3c7f569");*/

        db.close();
    }
//
//    public static void main(String[] args) {
//        System.out.println(System.getProperty("user.home"));
////        System.out.println(NativeLib.isLibraryDir(DataDir.get().root()));
//        NativeLib.loadFrom(DataDir.get().root());
////        try (var db = DataDir.get().openDatabase("ei23")) {
//        var db = DataDir.get().openDatabase("ei29");
///*            var system = db.get(ProductSystem.class,
//                    "7d1cbce0-b5b3-47ba-95b5-014ab3c7f569");
//            var method = new ImpactMethodDao(db)
//                    .getForRefId("207ffac9-aaa8-401d-ac90-874defd3751a");
//            var setup = CalculationSetup.of(system)
//                    .withImpactMethod(method);
//            var calc = new SystemCalculator(db);
//            var r = calc.calculate(setup);
//            var f = r.enviIndex().at(0);
//            System.out.println(f.flow().name + "  -> " + r.getTotalFlowValueOf(f));
//            var impact =  r.impactIndex().at(0);
//            System.out.println(impact.name + "  -> " + r.getTotalImpactValueOf(impact));
//            r.dispose();*/
//        db.close();
////        }
//    }

    @Test
    public void testDB() throws Exception {
        IDatabase db = DBTest.getDb();
    }

    @Test
    public void testRawConnection() throws Exception {
        IDatabase db = DBTest.getDb();

        var query = "select count(*) from tbl_units";
        try (var con = db.createConnection();
             var stmt = con.createStatement();
             var results = stmt.executeQuery(query)) {
            assertTrue(results.next());
            long count = results.getLong(1);
            assertTrue(count >= 0);
        }
    }
}
