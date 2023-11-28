package kr.re.ImportTest2.component.derdyDb;

import org.openlca.core.DataDir;
import org.openlca.core.database.*;
import org.openlca.core.database.config.DerbyConfig;
import org.openlca.nativelib.NativeLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class RunDatabase {

    private final Logger log = LoggerFactory.getLogger(RunDatabase.class);

    private String dbName = null;
    private static IDatabase db = null;

    /**
     * Front Controller와 연결되는 변수들
     *  - TARGET_AMOUNT
     *  - PRODUCT_NAME
     *  - SELECTED_PROCESSES
     *  - METHOD (고정이었나?) -> pass
     *  - CATEGORY (GWP, Water use, Resource depletion) -> 전부 출력
     *  -
     */

    // 추후 main()문 말고 runDb() 메서드로 변경 예정
    // return: db
    public IDatabase runDb() {
        NativeLib.loadFrom(DataDir.get().root());

        removeAllDb();  // *** Test 시 사용 ***

        // db name: 사용자 입력 받을건지, 내부에서 임의로 돌아갈건지.
        dbName = "ei01";

        // db config 설정
        DatabaseWizard databaseWizard = new DatabaseWizard();

        // true: 기존에 생성된 db / false: 기존에 없는 db
        boolean exists = isExist(dbName);

        // db activate
        databaseWizard.runDatabase(true, dbName);

        // db open
//        IDatabase db = Database.get();
        db = DataDir.get().openDatabase(dbName);

        // 기존에 없는 db일 경우 db files import
        if (!exists) {
            try {
                fileImport();
            } catch (Exception e) { // db import 도중 문제가 생기면 clear 후 다시 import
                try {
                    db.clear();
                    fileImport();
                    log.info("because {} db is not imported, clear and re-create.", dbName);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        getOutputPrint();
        return db;
    }

    // 없앨수도 있을 것 같음
    public void closeDb() throws IOException {
        db.close();
        db = null;
    }

    private boolean isExist(String dbName) {
        return Database.getConfigurations().nameExists(dbName);
    }

    private void fileImport() {
//        db.clear();

        String prePath = "D:/Dropbox/2022-KETI/01-Project/01-EXE/산업부-KEIT-리사이클링/02-수행/05-2023/2023-결과물관련";
        String lciaPath = prePath + "/openlca2_lcia_methods_v221_20230426_only_lcia.zip";
        String koreaDbPath = prePath + "/exported";

        FileImport f = new FileImport();

        long beforeTime = System.currentTimeMillis(); // 코드 실행 전에 시간 받아오기
/*        log.info("importing lcia db...");
        f.run(lciaPath);*/

        File excelFiles = new File(koreaDbPath);

        log.info("importing korea db...");
        for (String file : Objects.requireNonNull(excelFiles.list())) {
            String path = koreaDbPath + "/" + file;
            log.info("path={}", path);
            f.run(path);
        }
        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long diffTime = (afterTime - beforeTime)/1000; // 두 개의 실행 시간
        log.info("execute time(sec): {}", diffTime);
    }

    private void notInDirButRegistered(String[] dbNames) {
        log.info("Before Database.getConfigurations={}", Database.getConfigurations().getDerbyConfigs());
        List<String> list = new ArrayList<>(Arrays.asList(dbNames));

        for (String s : list) {
            DerbyConfig config = new DerbyConfig();
            Database.remove(config.name(s));
            log.info("remove {} db", s);
        }
        log.info("After Database.getConfigurations={}", Database.getConfigurations().getDerbyConfigs());
    }

    private void removeAllDb() {
        log.info("Before Database.getConfigurations={}", Database.getConfigurations().getDerbyConfigs());
        String[] dbNames = DataDir.get().getDatabasesDir().list();
        if (dbNames != null) {
            for (String dbName : dbNames) {
                DataDir.get().getDatabaseDir(dbName).delete();
                Database.remove(new DerbyConfig().name(dbName));
            }
            log.info("remove all db");
        }
        log.info("After Database.getConfigurations={}", Database.getConfigurations().getDerbyConfigs());
    }

    private void getOutputPrint() {
        var categoryAll = new ImpactCategoryDao(db).getAll();
        var methods = new ImpactMethodDao(db).getAll();
        var flows = new FlowDao(db).getAll();
        var processes = new ProcessDao(db).getAll();

        System.out.println("db.getName(): " + db.getName() + "\n");
        System.out.println("db.getLibraries(): " + db.getLibraries() + "\n");

        System.out.println("categoryAll: " + categoryAll + "\n");
        System.out.println("methods: " + methods + "\n");
        System.out.println("processes: " + processes + "\n");

        System.out.println("categoryDb size: " + categoryAll.size());
        System.out.println("methods size: " + methods.size());
        System.out.println("processes size: " + processes.size());
        System.out.println("flows size: " + flows.size() + "\n");

        for (var m : methods) {
            if (Objects.equals(m.name, "CML-IA baseline")) {
                System.out.println(m.name + " => " + m.category);

                for (var cg : m.impactCategories) {
                    System.out.println("cg = " + cg);
                }
            }
        }

/*        if (!processes.isEmpty()) {
            for (Process p : processes) {
                System.out.println("process: " + p);
                for (var exchange : p.exchanges) {
//                    if (exchange.isInput) {}
                    System.out.println("exchange = " + exchange);
                }
                System.out.println("\n");
            }
        }*/
    }


}
